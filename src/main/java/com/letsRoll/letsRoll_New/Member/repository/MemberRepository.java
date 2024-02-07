package com.letsRoll.letsRoll_New.Member.repository;

import com.letsRoll.letsRoll_New.Member.entity.Member;
import com.letsRoll.letsRoll_New.Project.entity.Project;
import com.letsRoll.letsRoll_New.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberById(Long memberId);


    List<Member> findMemberByProject(Project project);

    Optional<Member> findMemberByProjectAndUser(Project project, User user);

    Optional<Member> findByProjectAndUser(Project project, User user);
    List<Member> findMembersByUser(User user);

}
