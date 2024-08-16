package x1;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import com.oplus.content.OplusFeatureConfigManager;

/* compiled from: AppFeatureUtil.java */
/* renamed from: x1.a, reason: use source file name */
/* loaded from: classes.dex */
public class AppFeatureUtil {
    public static boolean a(Context context) {
        if (context == null) {
            return false;
        }
        try {
            return OplusFeatureConfigManager.getInstance().hasFeature("oplus.hardware.type.fold");
        } catch (Error | Exception e10) {
            Log.d("AppFeatureUtil", "Load feature_fold failed : " + e10.getMessage());
            return false;
        }
    }

    public static boolean b(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "oplus_system_folding_mode", 0) == 0;
    }

    public static boolean c(Context context) {
        return a(context) && b(context);
    }
}
