package com.letsRoll.letsRoll_New.Memoir.service;

import com.letsRoll.letsRoll_New.Memoir.dto.MemoirAssembler;
import com.letsRoll.letsRoll_New.Memoir.dto.res.MemoirResDto;
import com.letsRoll.letsRoll_New.Memoir.entity.Memoir;
import com.letsRoll.letsRoll_New.Memoir.repository.MemoirRepository;
import com.letsRoll.letsRoll_New.Global.exception.BaseException;
import com.letsRoll.letsRoll_New.Global.exception.BaseResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoirService {

    private final MemoirRepository memoirRepository;


    public List<MemoirResDto> getMemoirs(Long projectId) {
        List<Memoir> memoirs = memoirRepository.findByProjectId(projectId);
        return MemoirAssembler.fromEntities(memoirs);
    }

    public MemoirResDto getMemoir(Long memoirId) {
        Memoir memoir = memoirRepository.findById(memoirId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_MEMOIR));
        return MemoirAssembler.fromEntity(memoir);
    }
}





