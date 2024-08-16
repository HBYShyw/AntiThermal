package x4;

import android.content.Context;

/* compiled from: AbsAppSwitchDetector.java */
/* renamed from: x4.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class AbsAppSwitchDetector {

    /* renamed from: a, reason: collision with root package name */
    protected AppSwitchObserver f19511a;

    /* renamed from: b, reason: collision with root package name */
    protected Context f19512b;

    /* renamed from: c, reason: collision with root package name */
    protected int f19513c = 0;

    public AbsAppSwitchDetector(AppSwitchObserver appSwitchObserver, Context context) {
        this.f19511a = appSwitchObserver;
        this.f19512b = context;
    }

    protected abstract void a();

    protected abstract void b();

    public void c() {
        if (1 == this.f19513c) {
            return;
        }
        a();
        this.f19513c = 1;
    }

    public void d() {
        if (this.f19513c == 0) {
            return;
        }
        b();
        this.f19513c = 0;
    }
}
