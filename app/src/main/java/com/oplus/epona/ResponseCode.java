package com.oplus.epona;

/* loaded from: classes.dex */
public enum ResponseCode {
    SUCCESS(1),
    FAILED(-1),
    PERMISSION_DENY(-2);

    private int code;

    ResponseCode(int i10) {
        this.code = i10;
    }

    public int getCode() {
        return this.code;
    }
}
