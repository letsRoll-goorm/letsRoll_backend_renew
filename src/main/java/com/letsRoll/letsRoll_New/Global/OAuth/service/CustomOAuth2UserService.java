package com.letsRoll.letsRoll_New.Global.OAuth.service;

import com.letsRoll.letsRoll_New.Global.OAuth.OAuthAttributes;
import com.letsRoll.letsRoll_New.Global.OAuth.user.CustomOAuth2User;
import com.letsRoll.letsRoll_New.Global.enums.Provider;
import com.letsRoll.letsRoll_New.User.dto.UserAssembler;
import com.letsRoll.letsRoll_New.User.entity.User;
import com.letsRoll.letsRoll_New.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final UserAssembler userAssembler;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        /**
         * DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
         * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
         * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
         * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
         */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest); // OAuth2 정보를 가져온다.

        /**
         * userRequest에서 registrationId 추출 후 registrationId으로 SocialType 저장
         * http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
         * userNameAttributeName은 이후에 nameAttributeKey로 설정된다.
         */
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // OAuth2 서비스 id (google, kakao, naver)
        Provider provider = Provider.getByName(registrationId);

        String userNameAttributeName = userRequest.getClientRegistration() // OAuth2 로그인 시 키(PK)가 되는 값
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 소셜 종류에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.of(provider, userNameAttributeName, attributes);
        User createdUser = getUser(extractAttributes, provider);// getUser() 메소드로 User 객체 생성 후 반환

        // DefaultOAuth2User를 구현한 CustomOAuth2User 객체를 생성해서 반환
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getRole())),
                attributes,
                extractAttributes.getNameAttributesKey(),
                extractAttributes.getOAuth2UserInfo().getId(),
                createdUser.getEmail(),
                createdUser.getRole()
        );
    }

    /**
     * SocialType과 attributes에 들어있는 소셜 로그인의 식별값 id를 통해 회원을 찾아 반환하는 메소드
     * 만약 찾은 회원이 있다면, 그대로 반환하고 없다면 saveUser()를 호출하여 회원을 저장한다.
     */
    private User getUser(OAuthAttributes attributes, Provider provider) {
        Optional<User> findUser = userRepository.findBySocialIdAndProvider(
                        attributes.getOAuth2UserInfo().getId(), provider);

        if (findUser.isEmpty()) {
            log.info("존재하지 않는 유저, 추가하여 return");
            User newUser = userAssembler.toEntity(provider, attributes.getOAuth2UserInfo());
            return userRepository.save(newUser);
        } else {
            log.info("이미 존재하는 user, findUser return");
            return findUser.get();
        }
    }
}
