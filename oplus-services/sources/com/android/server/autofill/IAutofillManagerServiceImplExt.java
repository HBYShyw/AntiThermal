package com.android.server.autofill;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAutofillManagerServiceImplExt {
    public static final int COMMIT_REASON_ACTIVITY_FINISHED = 1;
    public static final int COMMIT_REASON_OPLUS_AUTOFILL_ACTIVITY_FINISHED = Integer.MIN_VALUE;

    default int hookHandleCommitReason(int i) {
        return i;
    }
}
