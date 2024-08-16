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

/* compiled from: AffairAudioAbnormal.java */
/* renamed from: o9.a, reason: use source file name */
/* loaded from: classes2.dex */
public class AffairAudioAbnormal implements IAffairCallback {

    /* renamed from: e, reason: collision with root package name */
    private static Context f16278e = null;

    /* renamed from: f, reason: collision with root package name */
    private static final String f16279f = "a";

    /* compiled from: AffairAudioAbnormal.java */
    /* renamed from: o9.a$a */
    /* loaded from: classes2.dex */
    private static class a {

        /* renamed from: a, reason: collision with root package name */
        private static final AffairAudioAbnormal f16280a = new AffairAudioAbnormal();
    }

    public static AffairAudioAbnormal a() {
        f16278e = GuardElfContext.e().c();
        return a.f16280a;
    }

    public void b() {
        registerAction();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        HighPowerSipper highPowerSipper;
        if (SimplePowerMonitorUtils.f17653d) {
            String str = f16279f;
            LocalLog.a(str, "ACTION_AUDIO_SILENCE_PLAYBACK ");
            int intExtra = intent != null ? intent.getIntExtra("android.media.EXTRA_PLAYBACK_PID", -1) : -1;
            String b10 = SimplePowerMonitorUtils.b(intExtra, f16278e);
            LocalLog.a(str, "AFFAIR_AUDIO_SILENCE_PLAYBACK_pid = " + intExtra);
            LocalLog.a(str, "AFFAIR_AUDIO_SILENCE_PLAYBACK_pkgName = " + SimplePowerMonitorUtils.b(intExtra, f16278e));
            HighPowerHelper f10 = HighPowerHelper.f(f16278e);
            if (f10.e().get(b10) == null) {
                highPowerSipper = new HighPowerSipper(b10);
                f10.e().put(b10, highPowerSipper);
            } else {
                highPowerSipper = f10.e().get(b10);
            }
            highPowerSipper.g(true);
            new PowerConsumeStatsImpl(f16278e).n();
            f10.c();
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 1205);
    }
}
