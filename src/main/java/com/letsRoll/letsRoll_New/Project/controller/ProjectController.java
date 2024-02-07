package com.letsRoll.letsRoll_New.Project.controller;

import com.letsRoll.letsRoll_New.Goal.dto.res.GoalResDto;
import com.letsRoll.letsRoll_New.Member.dto.req.MemberAddReq;
import com.letsRoll.letsRoll_New.Memoir.dto.req.MemoirAddReq;
import com.letsRoll.letsRoll_New.Project.dto.ProjectAssembler;
import com.letsRoll.letsRoll_New.Project.dto.req.ProjectStartReq;
import com.letsRoll.letsRoll_New.Project.dto.res.FinishProjectResDto;
import com.letsRoll.letsRoll_New.Project.dto.res.InProgressProjectResDto;
import com.letsRoll.letsRoll_New.Project.dto.res.ProjectResDto;
import com.letsRoll.letsRoll_New.Project.dto.res.StartProjectResDto;
import com.letsRoll.letsRoll_New.Project.repository.ProjectRepository;
import com.letsRoll.letsRoll_New.Project.service.ProjectService;
import com.letsRoll.letsRoll_New.Global.common.BaseResponse;
import com.letsRoll.letsRoll_New.Global.exception.BaseResponseCode;
import com.letsRoll.letsRoll_New.User.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping //프로젝트 등록
    public BaseResponse<StartProjectResDto> startProject(@RequestBody @Valid ProjectStartReq projectStartReq, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new BaseResponse<>(projectService.startProject(projectStartReq));
    }

    @GetMapping("/{projectId}")
    public BaseResponse<ProjectResDto> getProjectDetails(@PathVariable Long projectId, @RequestParam Long userId) {

        return new BaseResponse<>(projectService.getProjectDetails(projectId, userId));
    }



    @GetMapping("/{projectId}/goals")
    public BaseResponse<List<GoalResDto>> getGoalsForProject(@PathVariable Long projectId) {
        List<GoalResDto> goals = projectService.getGoalsForProject(projectId);
        return new BaseResponse<>(goals);
    }

    @PostMapping("/{projectId}/members")
    public BaseResponse<Void> addMemberToProject(@PathVariable Long projectId, @Valid @RequestBody MemberAddReq memberAddReq, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        projectService.addMemberToProject(projectId, memberAddReq, user);
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @PostMapping("/{projectId}/complete")
    public BaseResponse<Void> finishProject(@PathVariable Long projectId) {
        projectService.completeProject(projectId);
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @PostMapping("/{projectId}/memoirs")
    public BaseResponse<Void> addMemoir(@PathVariable Long projectId, @Valid @RequestBody MemoirAddReq addMemoirReq, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        projectService.addMemoir(projectId, addMemoirReq, user);
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @GetMapping("/myproject")
    public BaseResponse<List<InProgressProjectResDto>> myProjectList(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new BaseResponse<>(projectService.myProjectList(user));
    }

    @GetMapping("/end")
    public BaseResponse<List<FinishProjectResDto>> endProjectList(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new BaseResponse<>(projectService.endProjectList(user));
    }
}
