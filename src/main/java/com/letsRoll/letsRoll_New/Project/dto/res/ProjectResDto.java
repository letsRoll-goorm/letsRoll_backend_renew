package com.letsRoll.letsRoll_New.Project.dto.res;

import com.letsRoll.letsRoll_New.Goal.dto.res.GoalResDto;
import com.letsRoll.letsRoll_New.Member.dto.res.MemberResDto;
import com.letsRoll.letsRoll_New.Global.enums.Mode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProjectResDto {
    private Long projectId;
    private String title;
    private String description;
    private String password;
    private Mode mode;
    private LocalDate startDate;
    private LocalDate finishDate;
    private LocalDate endDate;
    private List<GoalResDto> goals;
    private List<MemberResDto> members;
    private Long memberId = null;
    private float progress;

}
