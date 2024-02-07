package com.letsRoll.letsRoll_New.Comment_Feeling.entity;

import com.letsRoll.letsRoll_New.Goal.entity.Goal;
import com.letsRoll.letsRoll_New.Member.entity.Member;
import com.letsRoll.letsRoll_New.Todo.entity.Todo;
import com.letsRoll.letsRoll_New.Global.common.BaseEntity;
import com.letsRoll.letsRoll_New.Global.enums.CommentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    @NonNull
    @Enumerated(EnumType.STRING)
    private CommentType type;

    @NonNull
    private String content;

    @Builder
    public Comment(@NonNull Member member, Goal goal, Todo todo, @NonNull CommentType type,@NonNull String content){
        this.member = member;
        this.goal = goal;
        this.todo = todo;
        this.type = type;
        this.content = content;
    }
}
