package o8;

import android.content.Context;

/* compiled from: BatteryStatusCenter.java */
/* renamed from: o8.a, reason: use source file name */
/* loaded from: classes2.dex */
public class BatteryStatusCenter {

    /* renamed from: f, reason: collision with root package name */
    private static volatile BatteryStatusCenter f16272f;

    /* renamed from: a, reason: collision with root package name */
    private boolean f16273a = false;

    /* renamed from: b, reason: collision with root package name */
    private boolean f16274b = false;

    /* renamed from: c, reason: collision with root package name */
    private boolean f16275c = false;

    /* renamed from: d, reason: collision with root package name */
    private boolean f16276d = false;

    /* renamed from: e, reason: collision with root package name */
    private Context f16277e;

    private BatteryStatusCenter(Context context) {
        this.f16277e = context;
    }

    public static BatteryStatusCenter a(Context context) {
        if (f16272f == null) {
            synchronized (BatteryStatusCenter.class) {
                if (f16272f == null) {
                    f16272f = new BatteryStatusCenter(context);
                }
            }
        }
        return f16272f;
    }

    public boolean b() {
        return this.f16276d;
    }

    public boolean c() {
        return this.f16273a;
    }

    public boolean d() {
        return this.f16274b;
    }

    public void e() {
        this.f16276d = this.f16275c;
    }

    public void f(boolean z10) {
        this.f16275c = z10;
        if (z10) {
            this.f16276d = true;
        }
    }

    public void g(boolean z10) {
        this.f16273a = z10;
    }

    public void h(boolean z10) {
        this.f16274b = z10;
    }
}
