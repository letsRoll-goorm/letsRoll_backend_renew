package com.letsRoll.letsRoll_New.Todo.repository;

import com.letsRoll.letsRoll_New.Member.entity.Member;
import com.letsRoll.letsRoll_New.Todo.entity.TodoEndManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoEndManagerRepository extends JpaRepository<TodoEndManager, Long> {
    Optional<TodoEndManager> findByMember(Member member);
}
