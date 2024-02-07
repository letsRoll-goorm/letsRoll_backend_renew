package com.letsRoll.letsRoll_New.Project.dto;

import com.letsRoll.letsRoll_New.Goal.dto.GoalAssembler;
import com.letsRoll.letsRoll_New.Member.dto.MemberAssembler;
import com.letsRoll.letsRoll_New.Member.entity.Member;
import com.letsRoll.letsRoll_New.Project.dto.res.FinishProjectResDto;
import com.letsRoll.letsRoll_New.Project.dto.res.InProgressProjectResDto;
import com.letsRoll.letsRoll_New.Project.dto.res.ProjectResDto;
import com.letsRoll.letsRoll_New.Project.entity.Project;
import com.letsRoll.letsRoll_New.Global.enums.Mode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ProjectAssembler {

    public static ProjectResDto projectResDto(Project project, Member member) {
        ProjectResDto projectDto = new ProjectResDto();
        projectDto.setProjectId(project.getId());
        projectDto.setTitle(project.getTitle());
        projectDto.setDescription(project.getDescription());
        projectDto.setPassword(project.getPassword());
        projectDto.setMode(project.getMode());
        projectDto.setStartDate(project.getStartDate());
        projectDto.setFinishDate(project.getFinishDate());
        projectDto.setEndDate(project.getEndDate());
        projectDto.setGoals(GoalAssembler.fromEntities(project.getGoals()));
        projectDto.setMembers(MemberAssembler.fromEntities(project.getMembers()));
        projectDto.setProgress(project.getProgress());
        projectDto.setMemberId(member.getId());
        return projectDto;
    }
    public static ProjectResDto projectResDto(Project project) {
        ProjectResDto projectDto = new ProjectResDto();
        projectDto.setProjectId(project.getId());
        projectDto.setTitle(project.getTitle());
        projectDto.setDescription(project.getDescription());
        projectDto.setPassword(project.getPassword());
        projectDto.setMode(project.getMode());
        projectDto.setStartDate(project.getStartDate());
        projectDto.setFinishDate(project.getFinishDate());
        projectDto.setEndDate(project.getEndDate());
        projectDto.setGoals(GoalAssembler.fromEntities(project.getGoals()));
        projectDto.setMembers(MemberAssembler.fromEntities(project.getMembers()));
        projectDto.setProgress(project.getProgress());
        return projectDto;
    }

    public Project project(String title, String description, String password, Mode mode, LocalDate startDate, LocalDate endDate) {
        return Project.builder()
                .title(title)
                .description(description)
                .password(password)
                .mode(mode)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public InProgressProjectResDto inProgressProjectResDto(Project project) {
        return InProgressProjectResDto.builder()
                .projectId(project.getId())
                .projectTitle(project.getTitle())
                .description(project.getDescription())
                .progress(project.getProgress())
                .build();
    }
    public FinishProjectResDto finishProjectResDto(Project project) {
        return FinishProjectResDto.builder()
                .projectId(project.getId())
                .projectTitle(project.getTitle())
                .description(project.getDescription())
                .build();
    }
}
