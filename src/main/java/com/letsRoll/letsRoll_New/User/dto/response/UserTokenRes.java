package com.letsRoll.letsRoll_New.User.dto.response;

import com.letsRoll.letsRoll_New.Global.enums.Common;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserTokenRes {
    private String accessToken;
    private String refreshToken;

    public static UserTokenRes toDto(String token) {
        String accessToken = token.split(Common.COMMA.getValue())[0];
        String refreshToken = token.split(Common.COMMA.getValue())[1];

        return UserTokenRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
