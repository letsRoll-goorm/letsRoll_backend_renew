package com.letsRoll.letsRoll_New.Memoir.controller;

import com.letsRoll.letsRoll_New.Memoir.dto.res.MemoirResDto;
import com.letsRoll.letsRoll_New.Memoir.service.MemoirService;
import com.letsRoll.letsRoll_New.Global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/memoirs")
@RequiredArgsConstructor
public class MemoirController {

    private final MemoirService memoirService;

    @GetMapping("/{projectId}")
    public BaseResponse<List<MemoirResDto>> getMemoirs(@PathVariable Long projectId) {
        List<MemoirResDto> memoirs = memoirService.getMemoirs(projectId);
        return new BaseResponse<>(memoirs);
    }

    @GetMapping("/{projectId}/{memoirId}")
    public BaseResponse<MemoirResDto> getMemoir(@PathVariable Long memoirId) {
        MemoirResDto memoir = memoirService.getMemoir(memoirId);
        return new BaseResponse<>(memoir);
    }

}