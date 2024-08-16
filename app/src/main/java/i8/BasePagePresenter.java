package i8;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.PowerManager;
import v4.GuardElfContext;

/* compiled from: BasePagePresenter.java */
/* renamed from: i8.a, reason: use source file name */
/* loaded from: classes.dex */
public class BasePagePresenter {

    /* renamed from: e, reason: collision with root package name */
    public ActivityManager f12672e;

    /* renamed from: f, reason: collision with root package name */
    public PowerManager f12673f;

    /* renamed from: g, reason: collision with root package name */
    public AlarmManager f12674g;

    /* renamed from: h, reason: collision with root package name */
    public BatteryManager f12675h;

    /* renamed from: i, reason: collision with root package name */
    public PackageManager f12676i;

    /* renamed from: j, reason: collision with root package name */
    public String f12677j;

    public BasePagePresenter() {
        this.f12672e = null;
        this.f12673f = null;
        this.f12674g = null;
        this.f12675h = null;
        this.f12676i = null;
        this.f12677j = "BasePagePresenter";
        this.f12672e = GuardElfContext.e().a();
        this.f12673f = GuardElfContext.e().h();
        this.f12674g = GuardElfContext.e().b();
        this.f12675h = GuardElfContext.e().d();
        this.f12676i = GuardElfContext.e().g();
    }

    public BasePagePresenter(String str) {
        this();
        this.f12677j = str;
    }
}
