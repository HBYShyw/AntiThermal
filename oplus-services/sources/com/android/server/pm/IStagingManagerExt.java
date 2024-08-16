package com.android.server.pm;

import com.android.server.pm.StagingManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IStagingManagerExt {
    public static final String NONE = "none";

    default String getSotaAppState() {
        return "none";
    }

    default boolean isBootFromSotaAppUpdate() {
        return false;
    }

    default boolean isSotaAppSession(StagingManager.StagedSession stagedSession) {
        return false;
    }
}
