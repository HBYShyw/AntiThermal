package android.os;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import com.oplus.os.OplusBuild;

/* loaded from: classes.dex */
public class TheiaSigprotector {
    private static final String TAG = "Theia";
    private static final int THEIA_INIT_TIME = 1000;
    private static final boolean PRE_VERSION = SystemProperties.get("ro.build.version.ota", "ota_version").contains("PRE");
    private static final boolean RELEASE_VERSION = SystemProperties.getBoolean("ro.build.release_type", false);
    private static final boolean CTA_VERSION = SystemProperties.getBoolean("persist.sys.cta", false);

    /* renamed from: -$$Nest$smisCommercialVersion, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m195$$Nest$smisCommercialVersion() {
        return isCommercialVersion();
    }

    /* renamed from: -$$Nest$smisTargetProcess, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m196$$Nest$smisTargetProcess() {
        return isTargetProcess();
    }

    private static boolean isCommercialVersion() {
        String buildVersion = OplusBuild.VERSION.RELEASE;
        boolean isInteractiveRomAction = !TextUtils.isEmpty(buildVersion) && (buildVersion.endsWith("Alpha") || buildVersion.endsWith("Beta"));
        return (!RELEASE_VERSION || isInteractiveRomAction || PRE_VERSION || CTA_VERSION) ? false : true;
    }

    private static boolean isTargetProcess() {
        String processName = Application.getProcessName();
        if (processName == null) {
            return false;
        }
        if (!processName.contains("com.ss.android.article.news") && !processName.contains("com.ss.android.ugc.aweme") && !processName.contains("com.tencent.mm")) {
            return false;
        }
        return true;
    }

    public static void initSigprotector(Looper mainLooper) {
        Looper lp = Looper.getMainLooper();
        if (lp == null) {
            lp = mainLooper;
        }
        new Handler(lp).postDelayed(new Runnable() { // from class: android.os.TheiaSigprotector.1
            @Override // java.lang.Runnable
            public void run() {
                if (TheiaSigprotector.m195$$Nest$smisCommercialVersion() || !TheiaSigprotector.m196$$Nest$smisTargetProcess()) {
                    return;
                }
                Log.i(TheiaSigprotector.TAG, "enter  TheiaSigprotector nativeInitSigprotector");
                TheiaNativeHelper.nativeInitSigprotector();
            }
        }, 1000L);
    }
}
