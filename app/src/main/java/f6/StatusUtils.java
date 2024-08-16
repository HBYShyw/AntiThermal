package f6;

import android.content.Context;
import android.net.wifi.WifiManager;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;

/* compiled from: StatusUtils.java */
/* renamed from: f6.e, reason: use source file name */
/* loaded from: classes.dex */
public class StatusUtils {

    /* renamed from: b, reason: collision with root package name */
    private static StatusUtils f11390b;

    /* renamed from: a, reason: collision with root package name */
    private WifiManager f11391a;

    private StatusUtils(Context context) {
        this.f11391a = (WifiManager) context.getSystemService(ThermalPolicy.KEY_WIFI);
    }

    public static synchronized StatusUtils a(Context context) {
        StatusUtils statusUtils;
        synchronized (StatusUtils.class) {
            if (f11390b == null) {
                f11390b = new StatusUtils(context);
            }
            statusUtils = f11390b;
        }
        return statusUtils;
    }

    public boolean b(Context context) {
        WifiManager wifiManager = this.f11391a;
        if (wifiManager != null) {
            return wifiManager.isWifiApEnabled();
        }
        return false;
    }
}
