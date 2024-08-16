package com.android.server;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IRescuePartyExt {
    default boolean checkAndWaitForToFinishDumpService() {
        return false;
    }

    default void checkForDumpService() {
    }
}
