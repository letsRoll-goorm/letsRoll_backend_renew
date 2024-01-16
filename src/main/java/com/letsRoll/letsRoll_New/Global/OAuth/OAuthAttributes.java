package com.letsRoll.letsRoll_New.Global.OAuth;

import com.letsRoll.letsRoll_New.Global.OAuth.userInfo.NaverOAuth2UserInfo;
import com.letsRoll.letsRoll_New.Global.OAuth.userInfo.OAuth2UserInfo;
import com.letsRoll.letsRoll_New.Global.enums.Provider;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {
    /**
     * 소셜별로 데이터를 받는 데이터를 분기 처리하는 DTO 클래스
     */
    private String nameAttributesKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private OAuth2UserInfo oAuth2UserInfo; // 소셜 타입별 로그인 유저 정보


    public static OAuthAttributes of(Provider provider, String userNameAttributeName, Map<String, Object> attributes) {

        if (provider.equals(Provider.NAVER)) {
            return ofNaver(userNameAttributeName, attributes);
        }
        return null;
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributesKey(userNameAttributeName)
                .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }
}
