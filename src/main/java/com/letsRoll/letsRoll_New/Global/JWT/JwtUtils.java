package com.letsRoll.letsRoll_New.Global.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsRoll.letsRoll_New.Global.enums.Common;
import com.letsRoll.letsRoll_New.Global.exception.BaseException;
import com.letsRoll.letsRoll_New.Global.exception.BaseResponseCode;
import com.letsRoll.letsRoll_New.User.dto.response.UserTokenRes;
import com.letsRoll.letsRoll_New.User.entity.User;
import com.letsRoll.letsRoll_New.User.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

import static com.letsRoll.letsRoll_New.Global.enums.Common.ACTIVE_STATUS;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtils {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;
    public static final String SOCIAL_ID = "socialId";
    public static final String EMAIL = "email";
    public static final String SPACE = " ";


    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${jwt.token-type}")
    public String tokenType;

    @Value("${jwt.access.name}")
    public String accessName;

    @Value("${jwt.refresh.name}")
    public String refreshName;

    @Value("${jwt.access.expiration}")
    public String accessExTime;

    @Value("${jwt.refresh.expiration}")
    public String refreshExTime;


    public String createToken(String socialId, String email)  { //For User
        String access_token = this.createAccessToken(socialId, email);
        String refresh_token = this.createRefreshToken(socialId, email);
        return access_token + Common.COMMA.getValue() + refresh_token;
    }
    public String createAccessToken(String socialId, String email) {
        /*String jws = Jwts.builder()
                .subject(accessName)
                .issuedAt(new Date())
                .claim(SOCIAL_ID, socialId)
                .claim(EMAIL, email)
                .compact();*/
        Claims claims = Jwts.claims()
                .subject(accessName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .build();

        String keyBased64Encoded = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBased64Encoded.getBytes());

        Date ext = new Date();
        ext.setTime(ext.getTime() + Long.parseLong(Objects.requireNonNull(accessExTime)));
        String accessToken = Jwts.builder()
                .header().add("typ", "JWT")
                .and()
                .claims(claims)
                .claim(SOCIAL_ID, socialId)
                .claim(EMAIL, email)
                .expiration(ext)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
        return tokenType + SPACE + accessToken;
    }
    public String createRefreshToken(String socialId, String email)  {
        Claims claims = Jwts.claims()
                .subject(refreshName)
                .issuedAt(new Date())
                .build();
        String keyBased64Encoded = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBased64Encoded.getBytes());

        Date ext = new Date();
        ext.setTime(ext.getTime() + Long.parseLong(Objects.requireNonNull(refreshExTime)));
        String refreshToken = Jwts.builder()
                .header().add("typ", "JWT")
                .and()
                .claims(claims)
                .expiration(ext)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();

        User user = userRepository.findBySocialIdAndEmailAndStatus(socialId, email, ACTIVE_STATUS.getValue())
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        user.updateRefreshToken(tokenType + SPACE + refreshToken);
        userRepository.save(user);
        return tokenType + SPACE + refreshToken;
    }
    public boolean isValidToken(String justToken){ // AccessToken이나 RefreshToken이 현재 유효한지 확인

        String keyBased64Encoded = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBased64Encoded.getBytes());

        Claims claims = Jwts.parser()
                .verifyWith(secretKey) // SecretKey가 Password일 때 방식
                .build()
                .parseSignedClaims(justToken).getPayload();
        return true;
    }

    //response의 body에 token 넣어 보내기
    public void sendTokens(HttpServletResponse response, String socialId, String email) throws Exception {
        response.setStatus(HttpServletResponse.SC_OK);
        UserTokenRes userTokenRes = UserTokenRes.toDto(createToken(socialId, email));
        response.getWriter().write(objectMapper.writeValueAsString(userTokenRes));
    }

    private Claims getJwtBodyFromJustToken(String justToken) {
        try {
            String keyBased64Encoded = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes());
            SecretKey secretKey = Keys.hmacShaKeyFor(keyBased64Encoded.getBytes());
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(justToken)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new BaseException(BaseResponseCode.EXPIRED_TOKEN);
        }
    }

    public Boolean isExpiredToken(String justToken) {
        // Token 유효성 검증
//        try {
            String keyBased64Encoded = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes());
            SecretKey secretKey = Keys.hmacShaKeyFor(keyBased64Encoded.getBytes());

            Date expiration = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(justToken)
                    .getPayload().getExpiration();
            return expiration.before(new Date());
        /*} catch (ExpiredJwtException e) {
            log.error("기한이 만료된 토큰입니다.");
            return false;
        } catch (Exception e) {
            return  false;
        }*/
    }

    public String parseJustTokenFromFullToken(String fullToken) {
        if (StringUtils.hasText(fullToken)
                && fullToken.startsWith(Objects.requireNonNull(tokenType)))
            return fullToken.split(SPACE)[1];
        return null;
    }

    public String getEmailFromFullToken(String fullToken) {
        return String.valueOf(getJwtBodyFromJustToken(parseJustTokenFromFullToken(fullToken)).get("email"));
    }
    public String getSocialIdFromFullToken(String fullToken) {
        return String.valueOf(getJwtBodyFromJustToken(parseJustTokenFromFullToken(fullToken)).get("socialId"));
    }
    public String accessExpiration(String socialId, String email) {
        User user = userRepository.findBySocialIdAndEmailAndStatus(socialId, email, ACTIVE_STATUS.getValue())
                .orElseThrow(() -> new BaseException(BaseResponseCode.EXPIRED_TOKEN));
        String userRefreshToken = user.getRefreshToken();
        if (userRefreshToken == null) throw new BaseException(BaseResponseCode.EXPIRED_TOKEN);
        String refreshEmail = getEmailFromFullToken(userRefreshToken);
        if (refreshEmail.isEmpty()) throw new BaseException(BaseResponseCode.EXPIRED_TOKEN);

        //토큰이 만료되었을 경우.
        return createAccessToken(socialId, refreshEmail);
    }

}
