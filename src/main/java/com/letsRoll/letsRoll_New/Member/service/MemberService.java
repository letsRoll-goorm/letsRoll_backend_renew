package com.letsRoll.letsRoll_New.Member.service;

import com.letsRoll.letsRoll_New.Member.dto.MemberAssembler;
import com.letsRoll.letsRoll_New.Member.dto.res.MemberResDto;
import com.letsRoll.letsRoll_New.Member.entity.Member;
import com.letsRoll.letsRoll_New.Member.repository.MemberRepository;
import com.letsRoll.letsRoll_New.Project.entity.Project;
import com.letsRoll.letsRoll_New.Project.repository.ProjectRepository;
import com.letsRoll.letsRoll_New.Global.exception.BaseException;
import com.letsRoll.letsRoll_New.Global.exception.BaseResponseCode;
import com.letsRoll.letsRoll_New.User.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public List<MemberResDto> getMemberList(Long projectId) {

        Project project = projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_PROJECT));

        List<Member> members = project.getMembers();
        return MemberAssembler.fromEntities(members);
    }
    public MemberResDto getMember(Long projectId, User user) {

        Project project = projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_PROJECT));
        Member member = memberRepository.findMemberByProjectAndUser(project, user)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_PROJECT));
        return MemberAssembler.fromEntity(member);
    }

}
