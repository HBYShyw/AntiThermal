package o9;

import a7.PowerConsumeStatsImpl;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import b6.LocalLog;
import r9.SimplePowerMonitorUtils;
import v4.GuardElfContext;
import w4.Affair;
import w4.IAffairCallback;

/* compiled from: AffairScreenAbnormal.java */
/* renamed from: o9.b, reason: use source file name */
/* loaded from: classes2.dex */
public class AffairScreenAbnormal implements IAffairCallback {

    /* renamed from: e, reason: collision with root package name */
    private static Context f16281e = null;

    /* renamed from: f, reason: collision with root package name */
    private static final String f16282f = "b";

    /* compiled from: AffairScreenAbnormal.java */
    /* renamed from: o9.b$a */
    /* loaded from: classes2.dex */
    private static class a {

        /* renamed from: a, reason: collision with root package name */
        private static final AffairScreenAbnormal f16283a = new AffairScreenAbnormal();
    }

    public static AffairScreenAbnormal a() {
        f16281e = GuardElfContext.e().c();
        return a.f16283a;
    }

    public void b() {
        registerAction();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        HighPowerSipper highPowerSipper;
        if (SimplePowerMonitorUtils.f17653d) {
            String str = f16282f;
            LocalLog.a(str, "ACTION_OPLUS_SCREENON_WAKELOCK_NOTIFY ");
            String stringExtra = intent != null ? intent.getStringExtra("package") : "";
            if (SimplePowerMonitorUtils.f17652c != 0) {
                stringExtra = stringExtra + "." + SimplePowerMonitorUtils.f17652c;
            }
            LocalLog.a(str, "ACTION_OPLUS_SCREENON_WAKELOCK_NOTIFY_pkgName = " + stringExtra);
            HighPowerHelper f10 = HighPowerHelper.f(f16281e);
            if (f10.e().get(stringExtra) == null) {
                highPowerSipper = new HighPowerSipper(stringExtra);
                f10.e().put(stringExtra, highPowerSipper);
            } else {
                highPowerSipper = f10.e().get(stringExtra);
            }
            highPowerSipper.h(true);
            new PowerConsumeStatsImpl(f16281e).n();
            f10.c();
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 1206);
    }
}
