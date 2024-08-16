package com.oplus.hardware.gameopt;

/* loaded from: classes.dex */
public interface OplusGameCallback {
    default void onNotify(String info) {
    }

    default void onReadTop(String info) {
    }

    default void onFrameLimit(String info) {
    }

    default void onBigDataReport(String info) {
    }

    default void onNotifyTemp(String info) {
    }

    default void onFrameProduce(String info) {
    }

    default void onInputCollect(String info) {
    }

    default void onCommonCall(String info, int type) {
    }
}
