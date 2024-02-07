package com.letsRoll.letsRoll_New.Goal.dto.res;

import com.letsRoll.letsRoll_New.Comment_Feeling.dto.res.CommentInfoDto;
import com.letsRoll.letsRoll_New.Comment_Feeling.dto.res.TodoCommentResDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TimeLineResDto {

    List<CommentInfoDto> goalCommentList;
    List<TodoCommentResDto> todoCommentList;
}
