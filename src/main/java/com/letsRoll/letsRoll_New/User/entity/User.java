package com.letsRoll.letsRoll_New.User.entity;

import com.letsRoll.letsRoll_New.Global.common.BaseEntity;
import com.letsRoll.letsRoll_New.Global.enums.Provider;
import com.letsRoll.letsRoll_New.Global.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Builder
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String userName; // 회원 이름

    @NonNull
    private String email; // 이메일

    @NonNull
    @Enumerated(EnumType.STRING)
    private Provider provider; // 네이버, 카카오 등

    @NonNull
    @Enumerated(EnumType.STRING)
    private Role role;

    private String socialId; // 로그인한 소셜 타입의 아이디 별 식별자 값 (일반 로그인인 경우 null)

    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void removeToken() {
        refreshToken = null;
    }
}