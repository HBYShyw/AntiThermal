package oplus.app;

import android.app.ApplicationExitInfo;
import android.os.Bundle;

/* loaded from: classes.dex */
public abstract class OplusAthenaManagerInternal {
    public static final String FORCESTOP = "forcestop";
    public static final String KILLER_PID = "killer_pid";
    public static final String KILLER_PKG = "killer_pkg";
    public static final String KILLER_PROC = "killer_proc";
    public static final String KILLER_UID = "killer_uid";

    public abstract void notifyAppExitInfo(ApplicationExitInfo applicationExitInfo, Bundle bundle);
}
