package com.letsRoll.letsRoll_New.Global.OAuth.user;

import com.letsRoll.letsRoll_New.Global.enums.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    /**
     * 기본 OAuth2User에서 필드를 추가하기 위해 custom
     * email: OAUth 처음 로그인일 경우, Resource Server가 제공하지 않는 정보를 받아야 할 때(사는 도시, 나이 등)
     *        임의의 Email을 사용하여 AccessToken을 발급받아 회원 식별용으로 Access Token 사용
     * role: 추가 정보(사는 도시, 나이)를 입력했는지 판단하기 위해 필요, 처음 로그인 하는 유저를 Role.GUEST로 설정하고,
     *       이후에 추가 정보 입력해 회원 가입을 진행하면 Role.USER로 업데이트
     */


    // Set<GrantedAuthority> authorities, Map<String, Object> attributes,
    // String nameAttributeKey 외에 아래 필드를 추가로 가진다
    private final String socialId;
    private final String email; //email은 JWT Token을 발급하기 위한 용도
    private final Role role;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String socialId,
                            String email, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.socialId = socialId;
        this.email = email;
        this.role = role;
    }
}
