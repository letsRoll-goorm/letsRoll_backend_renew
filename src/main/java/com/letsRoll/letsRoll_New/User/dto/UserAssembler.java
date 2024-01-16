package com.letsRoll.letsRoll_New.User.dto;

import com.letsRoll.letsRoll_New.Global.OAuth.userInfo.OAuth2UserInfo;
import com.letsRoll.letsRoll_New.Global.enums.Provider;
import com.letsRoll.letsRoll_New.Global.enums.Role;
import com.letsRoll.letsRoll_New.User.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAssembler {

    public User toEntity(Provider provider, OAuth2UserInfo oAuth2UserInfo) {
        /**
         * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
         * OAuth2UserInfo에서 값들을 가져와 DB에 저장하는 용도
         * role은 GUEST로 설정
         */
        return User.builder()
                .provider(provider)
                .socialId(oAuth2UserInfo.getId())
                .email(oAuth2UserInfo.getEmail())
                .userName(oAuth2UserInfo.getUserName())
                .role(Role.USER)
                .build();
    }
}
