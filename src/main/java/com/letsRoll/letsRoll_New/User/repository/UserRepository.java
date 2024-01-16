package com.letsRoll.letsRoll_New.User.repository;

import com.letsRoll.letsRoll_New.Global.enums.Provider;
import com.letsRoll.letsRoll_New.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialIdAndProvider(String socialId, Provider provider);
    Optional<User> findBySocialIdAndEmailAndStatus(String socialId, String email, String status);
    Optional<User> findByRefreshToken(String refreshToken);
}
