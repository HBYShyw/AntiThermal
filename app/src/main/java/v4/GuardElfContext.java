package v4;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.PowerManager;

/* compiled from: GuardElfContext.java */
/* renamed from: v4.c, reason: use source file name */
/* loaded from: classes.dex */
public class GuardElfContext {

    /* renamed from: g, reason: collision with root package name */
    private static volatile GuardElfContext f19078g;

    /* renamed from: a, reason: collision with root package name */
    private Context f19079a;

    /* renamed from: b, reason: collision with root package name */
    private ActivityManager f19080b;

    /* renamed from: c, reason: collision with root package name */
    private AlarmManager f19081c;

    /* renamed from: d, reason: collision with root package name */
    private PowerManager f19082d;

    /* renamed from: e, reason: collision with root package name */
    private BatteryManager f19083e;

    /* renamed from: f, reason: collision with root package name */
    private PackageManager f19084f;

    private GuardElfContext(Context context) {
        this.f19080b = null;
        this.f19081c = null;
        this.f19082d = null;
        this.f19083e = null;
        this.f19084f = null;
        this.f19079a = context;
        this.f19080b = (ActivityManager) context.getSystemService("activity");
        this.f19081c = (AlarmManager) context.getSystemService("alarm");
        this.f19082d = (PowerManager) context.getSystemService("power");
        this.f19083e = (BatteryManager) context.getSystemService("batterymanager");
        this.f19084f = context.getPackageManager();
    }

    public static GuardElfContext e() {
        return f19078g;
    }

    public static GuardElfContext f(Context context) {
        if (f19078g == null) {
            synchronized (GuardElfContext.class) {
                if (f19078g == null) {
                    f19078g = new GuardElfContext(context);
                }
            }
        }
        return f19078g;
    }

    public ActivityManager a() {
        if (this.f19080b == null) {
            this.f19080b = (ActivityManager) this.f19079a.getSystemService("activity");
        }
        return this.f19080b;
    }

    public AlarmManager b() {
        if (this.f19081c == null) {
            this.f19081c = (AlarmManager) this.f19079a.getSystemService("alarm");
        }
        return this.f19081c;
    }

    public Context c() {
        return this.f19079a;
    }

    public BatteryManager d() {
        if (this.f19083e == null) {
            this.f19083e = (BatteryManager) this.f19079a.getSystemService("batterymanager");
        }
        return this.f19083e;
    }

    public PackageManager g() {
        if (this.f19084f == null) {
            this.f19084f = this.f19079a.getPackageManager();
        }
        return this.f19084f;
    }

    public PowerManager h() {
        if (this.f19082d == null) {
            this.f19082d = (PowerManager) this.f19079a.getSystemService("power");
        }
        return this.f19082d;
    }
}
