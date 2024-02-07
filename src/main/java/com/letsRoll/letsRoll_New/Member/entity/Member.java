package com.letsRoll.letsRoll_New.Member.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.letsRoll.letsRoll_New.Project.entity.Project;
import com.letsRoll.letsRoll_New.User.entity.User;
import com.letsRoll.letsRoll_New.Global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project;

    @NonNull
    @Column(length = 20)
    private String nickname;

    @NonNull
    @Column(length = 10)
    private String role;

    @Builder
    public Member(@NonNull User user, @NonNull Project project, @NonNull String nickname, @NonNull String role) {
        this.user = user;
        this.project = project;
        this.nickname = nickname;
        this.role = role;
    }
}
