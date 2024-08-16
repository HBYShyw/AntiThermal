package s6;

import android.app.KeyguardManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.os.PowerManager;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import f6.CommonUtil;
import java.text.DecimalFormat;
import java.util.HashMap;
import x5.UploadDataUtil;

/* compiled from: ThermalUploader.java */
/* renamed from: s6.i, reason: use source file name */
/* loaded from: classes.dex */
public class ThermalUploader {

    /* renamed from: a, reason: collision with root package name */
    private Context f18147a;

    /* renamed from: b, reason: collision with root package name */
    private UploadDataUtil f18148b;

    public ThermalUploader(Context context, Looper looper) {
        this.f18148b = null;
        this.f18147a = context;
        this.f18148b = UploadDataUtil.S0(context);
    }

    private boolean a() {
        return ((WifiManager) this.f18147a.getSystemService(ThermalPolicy.KEY_WIFI)).getWifiApState() == 13;
    }

    private boolean b() {
        return ((KeyguardManager) this.f18147a.getSystemService("keyguard")).inKeyguardRestrictedInputMode();
    }

    private boolean c() {
        return ((PowerManager) this.f18147a.getSystemService("power")).isScreenOn();
    }

    public void d(String str, String str2, String str3, String str4, String str5, String str6) {
        HashMap hashMap = new HashMap();
        hashMap.put("zoom_window_pkg", str);
        hashMap.put("foreground_pkg", str3);
        hashMap.put("split_foreground_pkg", str4);
        hashMap.put("tempLevel", str5);
        hashMap.put("ambient_temperature", str6);
        hashMap.put("action", str2);
        this.f18148b.N(hashMap);
    }

    public void e(String str, long j10) {
        HashMap hashMap = new HashMap();
        hashMap.put("foreground_pkg", str);
        hashMap.put("hightemp_duration", String.valueOf(j10));
        this.f18148b.O(hashMap);
    }

    public void f(int i10, int i11, int i12) {
        String y4 = CommonUtil.y();
        boolean z10 = i12 == 2;
        boolean a10 = a();
        boolean c10 = c();
        boolean b10 = b();
        HashMap hashMap = new HashMap();
        hashMap.put("temperature", new DecimalFormat("0.00").format(i11 * 0.1d));
        hashMap.put("isCharging", String.valueOf(z10));
        hashMap.put("foregroundPkg", y4);
        hashMap.put("isWifiHotSpotOn", String.valueOf(a10));
        hashMap.put("isScreenOn", String.valueOf(c10));
        hashMap.put("isKeyguardLock", String.valueOf(b10));
        if (i10 == 0) {
            this.f18148b.P(hashMap);
        } else if (i10 == 1) {
            this.f18148b.R(hashMap);
        } else {
            if (i10 != 2) {
                return;
            }
            this.f18148b.Q(hashMap);
        }
    }
}
