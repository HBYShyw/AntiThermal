package com.android.server.credentials.metrics;

import android.util.Slog;
import java.util.AbstractMap;
import java.util.Map;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r0v1 com.android.server.credentials.metrics.ApiName, still in use, count: 1, list:
  (r0v1 com.android.server.credentials.metrics.ApiName) from 0x007b: IGET (r0v1 com.android.server.credentials.metrics.ApiName) A[WRAPPED] com.android.server.credentials.metrics.ApiName.mInnerMetricCode int
	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:151)
	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:116)
	at jadx.core.utils.InsnRemover.lambda$unbindInsns$1(InsnRemover.java:88)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
	at jadx.core.utils.InsnRemover.unbindInsns(InsnRemover.java:87)
	at jadx.core.utils.InsnRemover.removeAllAndUnbind(InsnRemover.java:238)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:180)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ApiName {
    UNKNOWN(0),
    GET_CREDENTIAL(1),
    GET_CREDENTIAL_VIA_REGISTRY(9),
    CREATE_CREDENTIAL(2),
    CLEAR_CREDENTIAL(3),
    IS_ENABLED_CREDENTIAL_PROVIDER_SERVICE(4),
    SET_ENABLED_PROVIDERS(5),
    GET_CREDENTIAL_PROVIDER_SERVICES(6),
    REGISTER_CREDENTIAL_DESCRIPTION(7),
    UNREGISTER_CREDENTIAL_DESCRIPTION(8);

    private static final String TAG = "ApiName";
    private static final Map<String, Integer> sRequestInfoToMetric;
    private final int mInnerMetricCode;

    public static ApiName valueOf(String str) {
        return (ApiName) Enum.valueOf(ApiName.class, str);
    }

    public static ApiName[] values() {
        return (ApiName[]) $VALUES.clone();
    }

    static {
        sRequestInfoToMetric = Map.ofEntries(new AbstractMap.SimpleEntry("android.credentials.ui.TYPE_CREATE", Integer.valueOf(r2.mInnerMetricCode)), new AbstractMap.SimpleEntry("android.credentials.ui.TYPE_GET", Integer.valueOf(new ApiName(1).mInnerMetricCode)), new AbstractMap.SimpleEntry("android.credentials.ui.TYPE_GET_VIA_REGISTRY", Integer.valueOf(new ApiName(9).mInnerMetricCode)), new AbstractMap.SimpleEntry("android.credentials.ui.TYPE_UNDEFINED", Integer.valueOf(new ApiName(3).mInnerMetricCode)));
    }

    private ApiName(int i) {
        this.mInnerMetricCode = i;
    }

    public int getMetricCode() {
        return this.mInnerMetricCode;
    }

    public static int getMetricCodeFromRequestInfo(String str) {
        Map<String, Integer> map = sRequestInfoToMetric;
        if (!map.containsKey(str)) {
            Slog.i(TAG, "Attempted to use an unsupported string key request info");
            return UNKNOWN.mInnerMetricCode;
        }
        return map.get(str).intValue();
    }
}
