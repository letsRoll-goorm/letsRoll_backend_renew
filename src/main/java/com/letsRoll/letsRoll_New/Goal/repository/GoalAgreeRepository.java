package com.letsRoll.letsRoll_New.Goal.repository;

import com.letsRoll.letsRoll_New.Goal.entity.Goal;
import com.letsRoll.letsRoll_New.Goal.entity.GoalAgree;
import com.letsRoll.letsRoll_New.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoalAgreeRepository extends JpaRepository<GoalAgree, Long>{
    Optional<GoalAgree> findByGoalAndMember(Goal goal, Member member);

    boolean existsByGoalAndMember(Goal goal, Member member);
}
