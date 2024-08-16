package android.os;

/* loaded from: classes.dex */
public class TheiaLogInfo {
    private static final int THEIA_LOGINFO_ANDROID_LOG = 1;
    private static final int THEIA_LOGINFO_ANR_TRACES = 4;
    private static final int THEIA_LOGINFO_BINDER_INFO = 32;
    private static final int THEIA_LOGINFO_DUMPSYS_MEMINFO = 16;
    private static final int THEIA_LOGINFO_DUMPSYS_SF = 8;
    private static final int THEIA_LOGINFO_KERNEL_LOG = 2;
    public long mTheiaLog;

    public TheiaLogInfo(long theiaLog) {
        this.mTheiaLog = theiaLog;
    }

    public long getValue() {
        return this.mTheiaLog;
    }
}
