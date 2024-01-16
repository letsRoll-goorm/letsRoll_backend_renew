package com.letsRoll.letsRoll_New.Global.enums;

import com.letsRoll.letsRoll_New.Global.exception.BaseException;
import com.letsRoll.letsRoll_New.Global.exception.BaseResponseCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Provider {
    NAVER("naver");

    private String provider;

    Provider(String provider) {
        this.provider = provider;
    }

    public static Provider getByName(String name){
        return Arrays.stream(Provider.values())
                .filter(provider -> provider.getProvider().equals(name))
                .findAny().orElseThrow(() -> new BaseException(BaseResponseCode.INVALID_PROVIDER));
    }
}
