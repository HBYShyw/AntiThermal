package y6;

import a7.PowerConsumeStats;
import a7.PowerConsumeStatsImpl;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import d6.ConfigUpdateUtil;
import java.util.ArrayList;
import w4.Affair;
import w4.IAffairCallback;

/* compiled from: PowerConsumeManager.java */
/* renamed from: y6.b, reason: use source file name */
/* loaded from: classes.dex */
public class PowerConsumeManager implements IAffairCallback {

    /* renamed from: k, reason: collision with root package name */
    private static PowerConsumeManager f19894k;

    /* renamed from: e, reason: collision with root package name */
    private Context f19895e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f19896f = true;

    /* renamed from: g, reason: collision with root package name */
    private int f19897g = -1;

    /* renamed from: h, reason: collision with root package name */
    private long f19898h;

    /* renamed from: i, reason: collision with root package name */
    ConfigUpdateUtil f19899i;

    /* renamed from: j, reason: collision with root package name */
    PowerConsumeStats f19900j;

    private PowerConsumeManager(Context context) {
        this.f19898h = 0L;
        this.f19899i = null;
        this.f19900j = null;
        this.f19895e = context;
        this.f19898h = SystemClock.uptimeMillis();
        this.f19900j = new PowerConsumeStatsImpl(context);
        this.f19899i = ConfigUpdateUtil.n(context);
        registerAction();
    }

    public static PowerConsumeManager b(Context context) {
        PowerConsumeManager powerConsumeManager;
        synchronized (PowerConsumeManager.class) {
            if (f19894k == null) {
                f19894k = new PowerConsumeManager(context);
            }
            powerConsumeManager = f19894k;
        }
        return powerConsumeManager;
    }

    public ArrayList<PowerConsumeStats.b> a() {
        return this.f19900j.b();
    }

    public void c(int i10, int i11, int i12) {
        if (!this.f19899i.w()) {
            this.f19900j.h(0);
            return;
        }
        if (SystemClock.uptimeMillis() - this.f19898h < 180000) {
            LocalLog.l("PowerConsumeManager", "wait for more time to battery change");
            return;
        }
        boolean z10 = (i11 & 15) != 0;
        if (z10 != this.f19896f) {
            LocalLog.a("PowerConsumeManager", "power consume handleBatteryChanged, charge is " + z10 + ", mIsCharging " + this.f19896f);
            this.f19896f = z10;
            if (z10) {
                this.f19900j.h(i10);
            } else {
                this.f19900j.f(i10);
            }
        }
        if (this.f19897g != i10) {
            this.f19897g = i10;
            this.f19900j.a(i10, i12);
        }
    }

    public void d() {
        LocalLog.a("PowerConsumeManager", "handleOptimization");
        this.f19900j.e();
    }

    public void e() {
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        if (i10 != 204) {
            return;
        }
        int intExtra = intent.getIntExtra("level", 0);
        intent.getIntExtra("status", 0);
        c(intExtra, intent.getIntExtra("plugged", 0), intent.getIntExtra("temperature", 0));
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_CAMERA);
    }
}
