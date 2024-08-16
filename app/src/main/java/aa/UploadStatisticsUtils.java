package aa;

import android.content.Context;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;
import com.oplus.statistics.OplusTrack;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import z9.AppToShow;

/* compiled from: UploadStatisticsUtils.java */
/* renamed from: aa.k, reason: use source file name */
/* loaded from: classes2.dex */
public class UploadStatisticsUtils {
    public static void a(Context context, String str, boolean z10) {
        HashMap hashMap = new HashMap();
        String str2 = z10 ? "1" : "0";
        hashMap.put(DeviceDomainManager.ARG_PKG, str);
        hashMap.put("switch", str2);
        c(context, "autoLaunch", hashMap);
    }

    public static void b(Context context, List<AppToShow> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (AppToShow appToShow : list) {
            HashMap hashMap = new HashMap();
            hashMap.put("package", appToShow.f20305b);
            hashMap.put("switch", String.valueOf(appToShow.f20308e ? 1 : 0));
            OplusTrack.onCommon(context, "KVEvent", "QX_auto_start_white_list_new", hashMap);
        }
    }

    public static void c(Context context, String str, Map<String, String> map) {
        OplusTrack.onCommon(context, "20092", str, map);
    }
}
