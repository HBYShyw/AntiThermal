package y5;

import b6.LocalLog;
import com.oplus.content.OplusFeatureConfigManager;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: FeatureUtil.java */
/* renamed from: y5.c, reason: use source file name */
/* loaded from: classes.dex */
public class FeatureUtil {

    /* renamed from: a, reason: collision with root package name */
    private static ConcurrentHashMap<String, Integer> f19893a = new ConcurrentHashMap<>();

    private static boolean a(String str) {
        boolean hasFeature = OplusFeatureConfigManager.getInstance().hasFeature(str);
        f19893a.put(str, Integer.valueOf(hasFeature ? 1 : 0));
        if (LocalLog.f()) {
            LocalLog.a("FeatureUtil", "initOplusFeatureConfig: " + str + " " + hasFeature);
        }
        return hasFeature;
    }

    public static boolean b(String str) {
        if (str == null || !f19893a.containsKey(str)) {
            return a(str);
        }
        return ((Integer) Objects.requireNonNullElse(f19893a.get(str), 0)).intValue() == 1;
    }
}
