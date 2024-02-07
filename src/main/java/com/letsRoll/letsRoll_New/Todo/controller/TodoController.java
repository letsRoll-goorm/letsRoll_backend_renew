package com.letsRoll.letsRoll_New.Todo.controller;

import com.letsRoll.letsRoll_New.Todo.dto.req.AddTodoReqDto;
import com.letsRoll.letsRoll_New.Todo.dto.res.*;
import com.letsRoll.letsRoll_New.Todo.service.TodoService;
import com.letsRoll.letsRoll_New.Global.common.BaseResponse;
import com.letsRoll.letsRoll_New.Global.exception.BaseResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @PostMapping
    public BaseResponse<Void> addTodo(
            @RequestBody @Valid AddTodoReqDto addTodoReqDto) {
        todoService.addTodo(addTodoReqDto);
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @GetMapping
    public BaseResponse<List<TodoListResDto>> getTodoListByDate(
            @RequestParam LocalDate date) {
        return new BaseResponse<>(todoService.getTodoListByDate(date));
    }

    @GetMapping("/incomplete")
    public BaseResponse<List<TodoListResDto>> getIncompleteTodoList() {
        return new BaseResponse<>(todoService.getIncompleteTodoList());
    }

    @PatchMapping
    public BaseResponse<Void> changeTodoComplete(@RequestParam long todoId, @RequestParam long memberId) {
        todoService.changeTodoComplete(todoId, memberId);
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @GetMapping("/is-overdue")
    public BaseResponse<Boolean> checkOverdueTodo() {
        return new BaseResponse<>(todoService.checkOverdueTodo());
    }

    @GetMapping("/{memberId}")
    public BaseResponse<List<MyTodoResDto>> getMyTodo(@PathVariable Long memberId) {
        return new BaseResponse<>(todoService.getMyTodo(memberId));
    }

    @GetMapping("/monthly")
    public BaseResponse<MonthlyCheckTodoListResDto> getMonthlyTodo(@RequestParam String yearMonth) {
        return new BaseResponse<>(todoService.getMonthlyTodo(yearMonth));
    }

    @GetMapping("/report/{projectId}")
    public BaseResponse<AllReportTodo> getReportTodo(@PathVariable Long projectId) {
        return new BaseResponse<>(todoService.getReportTodo(projectId));
    }
}