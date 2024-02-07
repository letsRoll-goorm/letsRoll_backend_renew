package com.letsRoll.letsRoll_New.Memoir.dto.res;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemoirResDto {
    private Long id;
    private String member; // Member 엔티티 대신 MemberResDto의 닉네임 추출
    private String content;
//    private ProjectResDto project; // Project 엔티티 대신 ProjectResDto 사용

    private LocalDateTime updatedAt;
}
