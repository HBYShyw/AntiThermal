package w6;

import android.content.Context;
import android.os.Bundle;
import com.oplus.epona.Epona;
import com.oplus.epona.Request;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;

/* compiled from: PluginMethodCaller.java */
/* renamed from: w6.a, reason: use source file name */
/* loaded from: classes.dex */
public class PluginMethodCaller {

    /* renamed from: c, reason: collision with root package name */
    private static volatile PluginMethodCaller f19363c;

    /* renamed from: a, reason: collision with root package name */
    private Request f19364a = new Request.Builder().setComponentName("BMMonitorRouteServer").build();

    /* renamed from: b, reason: collision with root package name */
    private Request f19365b = new Request.Builder().setComponentName("CommonRouteServer").build();

    private PluginMethodCaller(Context context) {
    }

    public static PluginMethodCaller c(Context context) {
        if (f19363c == null) {
            synchronized (PluginMethodCaller.class) {
                if (f19363c == null) {
                    f19363c = new PluginMethodCaller(context);
                }
            }
        }
        return f19363c;
    }

    public void a() {
        if (this.f19364a != null) {
            Bundle bundle = new Bundle();
            bundle.putString("bm_hostcall_command", "BMHelperInit");
            this.f19364a.putBundle(bundle);
            Epona.newCall(this.f19364a).execute();
        }
    }

    public void b(boolean z10) {
        if (this.f19364a != null) {
            Bundle bundle = new Bundle();
            bundle.putString("bm_hostcall_command", "enableBenchBoost");
            bundle.putBoolean("enablebb_hostcall_para", z10);
            this.f19364a.putBundle(bundle);
            Epona.newCall(this.f19364a).execute();
        }
    }

    public void d() {
        if (this.f19364a != null) {
            Bundle bundle = new Bundle();
            bundle.putString("bm_hostcall_command", "handleBMEventFirst");
            this.f19364a.putBundle(bundle);
            Epona.newCall(this.f19364a).execute();
        }
    }

    public void e() {
        if (this.f19364a != null) {
            Bundle bundle = new Bundle();
            bundle.putString("bm_hostcall_command", "handleBMEventSecond");
            this.f19364a.putBundle(bundle);
            Epona.newCall(this.f19364a).execute();
        }
    }

    public void f() {
        if (this.f19364a != null) {
            Bundle bundle = new Bundle();
            bundle.putString("bm_hostcall_command", "handleBMEventWhenDismiss");
            this.f19364a.putBundle(bundle);
            Epona.newCall(this.f19364a).execute();
        }
    }

    public void g() {
        if (this.f19364a != null) {
            Bundle bundle = new Bundle();
            bundle.putString("bm_hostcall_command", "handleBMEventWhenScreenOff");
            this.f19364a.putBundle(bundle);
            Epona.newCall(this.f19364a).execute();
        }
    }

    public void h(Bundle bundle) {
        if (this.f19364a != null) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("bm_hostcall_command", "handlePerformanceModeAppChange");
            bundle2.putBundle("bmapp_data_hostcall_para", bundle);
            this.f19364a.putBundle(bundle2);
            Epona.newCall(this.f19364a).execute();
        }
    }

    public void i(Bundle bundle) {
        if (this.f19364a != null) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("bm_hostcall_command", "setAppChangeData");
            bundle2.putBundle("bmapp_data_hostcall_para", bundle);
            this.f19364a.putBundle(bundle2);
            Epona.newCall(this.f19364a).execute();
        }
    }

    public void j(Boolean bool) {
        if (this.f19365b != null) {
            Bundle bundle = new Bundle();
            bundle.putString("common_hostcall_command", "setPluginDynamicLog");
            bundle.putBoolean(ThermalBaseConfig.Item.ATTR_VALUE, bool.booleanValue());
            this.f19365b.putBundle(bundle);
            Epona.newCall(this.f19365b).execute();
        }
    }

    public void k() {
        if (this.f19364a != null) {
            Bundle bundle = new Bundle();
            bundle.putString("bm_hostcall_command", "setPreHighFlag");
            this.f19364a.putBundle(bundle);
            Epona.newCall(this.f19364a).execute();
        }
    }

    public void l() {
        if (this.f19365b != null) {
            Bundle bundle = new Bundle();
            bundle.putString("common_hostcall_command", "updateParameterList");
            this.f19365b.putBundle(bundle);
            Epona.newCall(this.f19365b).execute();
        }
    }

    public void m() {
        if (this.f19365b != null) {
            Bundle bundle = new Bundle();
            bundle.putString("common_hostcall_command", "updatePerformanceModeBMList");
            this.f19365b.putBundle(bundle);
            Epona.newCall(this.f19365b).execute();
        }
    }
}
