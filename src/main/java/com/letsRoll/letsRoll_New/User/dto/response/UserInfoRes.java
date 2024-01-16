package com.letsRoll.letsRoll_New.User.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class UserInfoRes {
    private String userId;
    private String email;
    private String userName;
    private String socialId;

}
