package com.android.server.media;

import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IMediaSessionServiceExt {
    default String checkAndResetReceiverInfo(String str) {
        return "";
    }

    default void init(Context context) {
    }

    default boolean isInHistoryPlayInfoWhiteList(String str) {
        return false;
    }

    default boolean isInMediaBlackList(String str) {
        return false;
    }

    default boolean isMediaControlSupported() {
        return false;
    }

    default void setLastMediaButtonReceiver(Object obj, int i) {
    }
}
