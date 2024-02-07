package com.letsRoll.letsRoll_New.Memoir.repository;

import com.letsRoll.letsRoll_New.Member.entity.Member;
import com.letsRoll.letsRoll_New.Memoir.entity.Memoir;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoirRepository extends JpaRepository<Memoir, Long>{
    boolean existsByMember(Member member);
    List<Memoir> findByProjectId(Long projectId);


}
