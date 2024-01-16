package com.letsRoll.letsRoll_New.Global.enums;

import lombok.Getter;

@Getter
public enum Common {

    ACTIVE_STATUS("active"),
    INACTIVE_STATUS("inactive"),
    SPACE(" "),
    COMMA(",");


    private String value;
    Common(String value) {
        this.value = value;
    }
}
