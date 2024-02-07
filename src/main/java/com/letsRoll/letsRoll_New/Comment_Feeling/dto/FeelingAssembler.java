package com.letsRoll.letsRoll_New.Comment_Feeling.dto;

import com.letsRoll.letsRoll_New.Comment_Feeling.entity.Comment;
import com.letsRoll.letsRoll_New.Comment_Feeling.entity.Feeling;
import com.letsRoll.letsRoll_New.Member.entity.Member;
import com.letsRoll.letsRoll_New.Global.enums.Emoji;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeelingAssembler {
    public Feeling toFeelingEntity(Comment comment, Member member, Emoji emoji) {
        return Feeling.builder()
                .comment(comment)
                .member(member)
                .emoji(emoji)
                .build();
    }
}
