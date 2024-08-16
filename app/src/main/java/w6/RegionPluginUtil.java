package w6;

import android.content.Context;
import android.os.Bundle;
import b6.LocalLog;
import com.oplus.epona.Epona;
import com.oplus.epona.Request;

/* compiled from: RegionPluginUtil.java */
/* renamed from: w6.e, reason: use source file name */
/* loaded from: classes.dex */
public class RegionPluginUtil {

    /* renamed from: c, reason: collision with root package name */
    private static volatile RegionPluginUtil f19384c;

    /* renamed from: a, reason: collision with root package name */
    private Request f19385a;

    /* renamed from: b, reason: collision with root package name */
    private Context f19386b;

    private RegionPluginUtil(Context context) {
        this.f19385a = null;
        this.f19386b = context;
        this.f19385a = new Request.Builder().setComponentName("RestrictPlugin").build();
    }

    public static synchronized RegionPluginUtil a(Context context) {
        RegionPluginUtil regionPluginUtil;
        synchronized (RegionPluginUtil.class) {
            if (f19384c == null) {
                synchronized (RegionPluginUtil.class) {
                    if (f19384c == null) {
                        f19384c = new RegionPluginUtil(context);
                    }
                }
            }
            regionPluginUtil = f19384c;
        }
        return regionPluginUtil;
    }

    public void b() {
        LocalLog.a("RegionPluginUtil", "noteConnectionChange,mRequest=" + this.f19385a);
        if (this.f19385a != null) {
            Bundle bundle = new Bundle();
            bundle.putString("command", "noteConnectionChange");
            this.f19385a.putBundle(bundle);
            Epona.newCall(this.f19385a).execute();
        }
    }

    public void c() {
        if (this.f19385a != null) {
            Bundle bundle = new Bundle();
            bundle.putString("command", "noteGoogleRestrictlistChange");
            this.f19385a.putBundle(bundle);
            Epona.newCall(this.f19385a).execute();
        }
    }

    public void d(String str) {
        if (this.f19385a != null) {
            Bundle bundle = new Bundle();
            bundle.putString("command", "notePackageChange");
            bundle.putString("packagename", str);
            this.f19385a.putBundle(bundle);
            Epona.newCall(this.f19385a).execute();
        }
    }

    public void e(long j10, boolean z10) {
        if (this.f19385a != null) {
            Bundle bundle = new Bundle();
            bundle.putString("command", "startPrediction");
            bundle.putLong("delay", j10);
            bundle.putBoolean("isforced", z10);
            this.f19385a.putBundle(bundle);
            Epona.newCall(this.f19385a).execute();
        }
    }

    public void f(boolean z10) {
        if (this.f19385a != null) {
            Bundle bundle = new Bundle();
            bundle.putString("command", "updateAABAISwitch");
            bundle.putBoolean("aabaiswitch", z10);
            this.f19385a.putBundle(bundle);
            Epona.newCall(this.f19385a).execute();
        }
    }
}
