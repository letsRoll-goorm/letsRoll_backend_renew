package com.letsRoll.letsRoll_New.Project.service;

import com.letsRoll.letsRoll_New.Goal.dto.GoalAssembler;
import com.letsRoll.letsRoll_New.Goal.dto.res.GoalResDto;
import com.letsRoll.letsRoll_New.Goal.entity.Goal;
import com.letsRoll.letsRoll_New.Goal.entity.GoalAgree;
import com.letsRoll.letsRoll_New.Goal.repository.GoalAgreeRepository;
import com.letsRoll.letsRoll_New.Goal.repository.GoalRepository;
import com.letsRoll.letsRoll_New.Member.dto.req.MemberAddReq;
import com.letsRoll.letsRoll_New.Member.entity.Member;
import com.letsRoll.letsRoll_New.Member.repository.MemberRepository;
import com.letsRoll.letsRoll_New.Memoir.dto.req.MemoirAddReq;
import com.letsRoll.letsRoll_New.Memoir.entity.Memoir;
import com.letsRoll.letsRoll_New.Memoir.repository.MemoirRepository;
import com.letsRoll.letsRoll_New.Project.dto.ProjectAssembler;
import com.letsRoll.letsRoll_New.Project.dto.req.ProjectStartReq;
import com.letsRoll.letsRoll_New.Project.dto.res.FinishProjectResDto;
import com.letsRoll.letsRoll_New.Project.dto.res.InProgressProjectResDto;
import com.letsRoll.letsRoll_New.Project.dto.res.ProjectResDto;
import com.letsRoll.letsRoll_New.Project.dto.res.StartProjectResDto;
import com.letsRoll.letsRoll_New.Project.repository.ProjectRepository;
import com.letsRoll.letsRoll_New.User.entity.User;
import com.letsRoll.letsRoll_New.User.repository.UserRepository;
import com.letsRoll.letsRoll_New.Global.enums.Mode;
import com.letsRoll.letsRoll_New.Global.exception.BaseException;
import com.letsRoll.letsRoll_New.Global.exception.BaseResponseCode;
import com.letsRoll.letsRoll_New.Project.entity.Project;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final MemoirRepository memoirRepository;
    private final GoalAgreeRepository goalAgreeRepository;
    private final ProjectAssembler projectAssembler;

    public void setFinishDateIfGoalsCompleted(Project project) {
        boolean allGoalsCompleted = project.getGoals().stream().allMatch(Goal::getIsComplete);

        // 모든 목표가 완료되었다면 finishDate 설정
        if (allGoalsCompleted) {
            project.setFinishDate(LocalDate.now());
        }
        else{
            throw new BaseException(BaseResponseCode.NOT_COMPLETED_GOAL);
        }
    }


    @Transactional
    public StartProjectResDto startProject(ProjectStartReq projectStartReq) {
        // ProjectStartReq로부터 필요한 정보 추출
        String title = projectStartReq.getTitle();
        String description = projectStartReq.getDescription();
        String password = projectStartReq.getPassword();
        Mode mode = projectStartReq.getMode();
        LocalDate startDate = projectStartReq.getStartDate();
        LocalDate endDate = projectStartReq.getEndDate();

        Project project = projectAssembler.project(title, description, password, mode, startDate, endDate);

        projectRepository.save(project);
        return StartProjectResDto.builder().projectId(project.getId()).build();
    }

    public List<GoalResDto> getGoalsForProject(Long projectId) {
        List<Goal> goals = goalRepository.findByProject_Id(projectId);

        return goals.stream()
                .map(GoalAssembler::fromEntity)
                .collect(Collectors.toList());
    }

    public void addMemberToProject(Long projectId, @Valid MemberAddReq memberAddReq, User user) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_PROJECT));


        String password = memberAddReq.getPassword();
        if (!password.equals(project.getPassword())) {
            throw new BaseException(BaseResponseCode.WRONG_PROJECT_PASSWORD);
        }


        String nickname = memberAddReq.getNickname();
        String role = memberAddReq.getRole();

        Member member = Member.builder()
                .project(project)
                .user(user)
                .nickname(nickname)
                .role(role)
                .build();

        memberRepository.save(member);

        // 프로젝트의 각 Goal에 대해 GoalAgree 생성 또는 가져오기
        List<Goal> goals = project.getGoals();
        for (Goal goal : goals) {
            GoalAgree goalAgree = GoalAgree.builder()
                            .goal(goal)
                            .member(member)
                            .build();

            goalAgreeRepository.save(goalAgree);
        }
    }

    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_PROJECT));
    }

    public void completeProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_PROJECT));

        // 프로젝트의 모든 Goal이 완료된 상태인지 확인
        setFinishDateIfGoalsCompleted(project);

        projectRepository.save(project);
    }

    public void addMemoir(Long projectId, MemoirAddReq addMemoirReq, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_PROJECT));
        // Member 확인
        Member member = memberRepository.findMemberByProjectAndUser(project, user)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_MEMBER));
        // 멤버가 프로젝트에 속해 있는지 확인
        if (!project.getMembers().contains(member)) {
            throw new BaseException(BaseResponseCode.NOT_FOUND_MEMBER);
        }

        // 프로젝트의 모든 Goal이 완료된 상태인지 확인
        boolean allGoalsCompleted = project.getGoals().stream().allMatch(Goal::getIsComplete);
        if (!allGoalsCompleted) {
            throw new BaseException(BaseResponseCode.NOT_COMPLETED_GOAL);
        }

        // 한 번만 Memoir를 작성할 수 있도록 체크
        if (memoirRepository.existsByMember(member)) {
            throw new BaseException(BaseResponseCode.ALREADY_WRITTEN_MEMOIR);
        }

        // Memoir를 생성하여 저장
        Memoir memoir = Memoir.builder()
                .member(member)
                .content(addMemoirReq.getContent())
                .build();

        memoirRepository.save(memoir);
    }

    public List<InProgressProjectResDto> myProjectList(User user) {
        List<Member> members = memberRepository.findMembersByUser(user);
        List<InProgressProjectResDto> inProgressProjectResDtos = new ArrayList<>();
        for (Member member : members) {
            Project project = member.getProject();
            if(project.getFinishDate()!=null)
                inProgressProjectResDtos.add(projectAssembler.inProgressProjectResDto(project));
        }
        return inProgressProjectResDtos;
    }

    public List<FinishProjectResDto> endProjectList(User user) {
        List<Member> members = memberRepository.findMembersByUser(user);
        List<FinishProjectResDto> finishProjectResDtos = new ArrayList<>();
        for (Member member : members) {
            Project project = member.getProject();
            if(project.getFinishDate()==null)
                finishProjectResDtos.add(projectAssembler.finishProjectResDto(project));
        }
        return finishProjectResDtos;
    }

    public ProjectResDto getProjectDetails(Long projectId, Long userId) {
        Project project = getProject(projectId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        Optional<Member> member = memberRepository.findByProjectAndUser(project, user);

        return member.map(value -> projectAssembler.projectResDto(project, value)).orElseGet(() -> projectAssembler.projectResDto(project));
    }

}
