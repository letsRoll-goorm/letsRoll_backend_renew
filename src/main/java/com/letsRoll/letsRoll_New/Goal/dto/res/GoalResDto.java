package com.letsRoll.letsRoll_New.Goal.dto.res;

import com.letsRoll.letsRoll_New.Todo.dto.res.TodoListResDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class GoalResDto {
    private Long goalId;
    private Long projectId;
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isComplete;
    private List<GoalAgreeMemberCheckResDto> goalAgreeList;
    private List<TodoListResDto> todoList;

    private float progress;
}