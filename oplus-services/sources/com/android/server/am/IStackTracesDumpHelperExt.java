package com.android.server.am;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IStackTracesDumpHelperExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface IStaticExt {
        default boolean isSkipAnrDump() {
            return false;
        }

        default void writeTransactionToTrace(String str) {
        }
    }
}
