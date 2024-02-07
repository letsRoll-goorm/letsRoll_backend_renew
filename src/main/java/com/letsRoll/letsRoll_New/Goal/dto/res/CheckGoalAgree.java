package com.letsRoll.letsRoll_New.Goal.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CheckGoalAgree {
    private Long goalId;
    private List<String> memberNickNames;
    private List<Long> memberIds;
}
