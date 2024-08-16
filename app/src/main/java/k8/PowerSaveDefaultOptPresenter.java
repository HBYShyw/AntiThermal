package k8;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import androidx.preference.Preference;
import b6.LocalLog;
import com.coui.appcompat.preference.COUISwitchPreference;
import d6.ConfigUpdateUtil;
import h8.IPowerSaveDefaultOptPresenter;
import i8.BasePagePresenter;
import l8.IPowerSaveDefaultOptUpdate;
import v4.GuardElfContext;

/* compiled from: PowerSaveDefaultOptPresenter.java */
/* renamed from: k8.k, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerSaveDefaultOptPresenter extends BasePagePresenter implements IPowerSaveDefaultOptPresenter {

    /* renamed from: k, reason: collision with root package name */
    private final int f14167k;

    /* renamed from: l, reason: collision with root package name */
    private final int f14168l;

    /* renamed from: m, reason: collision with root package name */
    private final int f14169m;

    /* renamed from: n, reason: collision with root package name */
    private final int f14170n;

    /* renamed from: o, reason: collision with root package name */
    private final int f14171o;

    /* renamed from: p, reason: collision with root package name */
    private final int f14172p;

    /* renamed from: q, reason: collision with root package name */
    private final int f14173q;

    /* renamed from: r, reason: collision with root package name */
    private final int f14174r;

    /* renamed from: s, reason: collision with root package name */
    private final int f14175s;

    /* renamed from: t, reason: collision with root package name */
    private final int f14176t;

    /* renamed from: u, reason: collision with root package name */
    private Context f14177u;

    /* renamed from: v, reason: collision with root package name */
    private HandlerThread f14178v;

    /* renamed from: w, reason: collision with root package name */
    private Handler f14179w;

    /* renamed from: x, reason: collision with root package name */
    private IPowerSaveDefaultOptUpdate f14180x;

    /* renamed from: y, reason: collision with root package name */
    private ContentObserver f14181y;

    /* compiled from: PowerSaveDefaultOptPresenter.java */
    /* renamed from: k8.k$a */
    /* loaded from: classes2.dex */
    class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            LocalLog.a(PowerSaveDefaultOptPresenter.this.f12677j, "BackLightSwitch observer. state=" + f6.f.e0(PowerSaveDefaultOptPresenter.this.f14177u));
            PowerSaveDefaultOptPresenter.this.f12673f.userActivity(SystemClock.uptimeMillis(), 0, 0);
        }
    }

    /* compiled from: PowerSaveDefaultOptPresenter.java */
    /* renamed from: k8.k$b */
    /* loaded from: classes2.dex */
    private class b extends Handler {
        public b(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i10 = message.what;
            if (i10 != 109) {
                switch (i10) {
                    case 103:
                        f6.f.B2(PowerSaveDefaultOptPresenter.this.f14177u, ((Boolean) message.obj).booleanValue() ? 1 : 0);
                        return;
                    case 104:
                        f6.f.F2(PowerSaveDefaultOptPresenter.this.f14177u, ((Boolean) message.obj).booleanValue() ? 1 : 0);
                        return;
                    case 105:
                        f6.f.G2(PowerSaveDefaultOptPresenter.this.f14177u, ((Boolean) message.obj).booleanValue() ? 1 : 0);
                        return;
                    case 106:
                        boolean booleanValue = ((Boolean) message.obj).booleanValue();
                        PowerSaveDefaultOptPresenter.this.l(booleanValue);
                        f6.f.h2(PowerSaveDefaultOptPresenter.this.f14177u, booleanValue ? 1 : 0);
                        return;
                    default:
                        return;
                }
            }
            boolean booleanValue2 = ((Boolean) message.obj).booleanValue();
            f6.f.E2(PowerSaveDefaultOptPresenter.this.f14177u, booleanValue2 ? 1 : 0);
            if (!PowerSaveDefaultOptPresenter.this.f12673f.isPowerSaveMode()) {
                LocalLog.l(PowerSaveDefaultOptPresenter.this.f12677j, "not power mode refreshChecked = " + booleanValue2);
                return;
            }
            if (booleanValue2) {
                int h02 = f6.f.h0(PowerSaveDefaultOptPresenter.this.f14177u);
                LocalLog.l(PowerSaveDefaultOptPresenter.this.f12677j, "MSG_SCREEN_REFRESH: backupRefreshRate = " + h02);
                Settings.System.putIntForUser(PowerSaveDefaultOptPresenter.this.f14177u.getContentResolver(), "power_save_pre_refresh_state", h02, 0);
            }
            f6.f.D2(PowerSaveDefaultOptPresenter.this.f14177u, booleanValue2 ? 2 : Settings.System.getIntForUser(PowerSaveDefaultOptPresenter.this.f14177u.getContentResolver(), "power_save_pre_refresh_state", 0, 0));
            LocalLog.l(PowerSaveDefaultOptPresenter.this.f12677j, "MSG_SCREEN_REFRESH: refreshChecked = " + booleanValue2);
        }
    }

    public PowerSaveDefaultOptPresenter(IPowerSaveDefaultOptUpdate iPowerSaveDefaultOptUpdate) {
        super(PowerSaveDefaultOptPresenter.class.getSimpleName());
        this.f14167k = 0;
        this.f14168l = 1;
        this.f14169m = 0;
        this.f14170n = 2;
        this.f14171o = 103;
        this.f14172p = 104;
        this.f14173q = 105;
        this.f14174r = 106;
        this.f14175s = 109;
        this.f14176t = 110;
        this.f14177u = null;
        this.f14178v = null;
        this.f14179w = null;
        this.f14181y = new a(new Handler());
        this.f14177u = GuardElfContext.e().c();
        this.f14180x = iPowerSaveDefaultOptUpdate;
    }

    private void k() {
        if (y5.b.H()) {
            this.f14180x.r();
            this.f14180x.X(Boolean.valueOf(f6.f.i0(this.f14177u) != 0));
        } else {
            f6.f.E3("decrease_screen_refresh", String.valueOf(1), this.f14177u);
            LocalLog.a(this.f12677j, "don't support refresh");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l(boolean z10) {
        LocalLog.l(this.f12677j, "handleDisableFiveGState checked: " + z10);
        if (this.f12673f.isPowerSaveMode()) {
            if (z10) {
                if (f6.f.L(this.f14177u) != 0) {
                    f6.f.l2(this.f14177u, 0);
                }
            } else if (f6.f.L(this.f14177u) != 1) {
                f6.f.l2(this.f14177u, 1);
            }
        }
    }

    private void m(boolean z10) {
        if (this.f14179w.hasMessages(104)) {
            this.f14179w.removeMessages(104);
        }
        Message obtain = Message.obtain(this.f14179w, 104);
        obtain.obj = Boolean.valueOf(z10);
        this.f14179w.sendMessage(obtain);
    }

    private void n(boolean z10) {
        if (this.f14179w.hasMessages(103)) {
            this.f14179w.removeMessages(103);
        }
        Message obtain = Message.obtain(this.f14179w, 103);
        obtain.obj = Boolean.valueOf(z10);
        this.f14179w.sendMessage(obtain);
    }

    private void o(boolean z10) {
        if (this.f14179w.hasMessages(109)) {
            this.f14179w.removeMessages(109);
        }
        Message obtain = Message.obtain(this.f14179w, 109);
        obtain.obj = Boolean.valueOf(z10);
        this.f14179w.sendMessage(obtain);
    }

    private void q(boolean z10) {
        if (this.f14179w.hasMessages(110)) {
            this.f14179w.removeMessages(110);
        }
        Message obtain = Message.obtain(this.f14179w, 110);
        obtain.obj = Boolean.valueOf(z10);
        this.f14179w.sendMessage(obtain);
    }

    private void r(boolean z10) {
        if (this.f14179w.hasMessages(106)) {
            this.f14179w.removeMessages(106);
        }
        Message obtain = Message.obtain(this.f14179w, 106);
        obtain.obj = Boolean.valueOf(z10);
        this.f14179w.sendMessage(obtain);
    }

    private void s(boolean z10) {
        if (this.f14179w.hasMessages(105)) {
            this.f14179w.removeMessages(105);
        }
        Message obtain = Message.obtain(this.f14179w, 105);
        obtain.obj = Boolean.valueOf(z10);
        this.f14179w.sendMessage(obtain);
    }

    private String t(boolean z10) {
        return String.valueOf(z10 ? 1 : 0);
    }

    @Override // h8.IPowerSaveDefaultOptPresenter
    public void onCreate(Bundle bundle) {
        HandlerThread handlerThread = new HandlerThread("PowerSecHandler");
        this.f14178v = handlerThread;
        handlerThread.start();
        this.f14179w = new b(this.f14178v.getLooper());
    }

    @Override // h8.IPowerSaveDefaultOptPresenter
    public void onCreatePreferences(Bundle bundle, String str) {
        this.f14180x.A(Boolean.valueOf(f6.f.e0(this.f14177u) != 0));
        this.f14180x.c0(Boolean.valueOf(f6.f.j0(this.f14177u) != 0));
        this.f14180x.U(Boolean.valueOf(f6.f.k0(this.f14177u) != 0));
        if (!f6.f.k1(this.f14177u)) {
            this.f14180x.m();
            if (!ConfigUpdateUtil.n(this.f14177u).x()) {
                f6.f.h2(this.f14177u, 0);
            }
        } else {
            this.f14180x.u(Boolean.valueOf(f6.f.G(this.f14177u) != 0));
        }
        k();
        this.f14177u.getContentResolver().registerContentObserver(Settings.System.getUriFor("power_save_backlight_switch_state"), false, this.f14181y, 0);
    }

    @Override // h8.IPowerSaveDefaultOptPresenter
    public void onDestroy() {
        this.f14177u.getContentResolver().unregisterContentObserver(this.f14181y);
        this.f14178v.quitSafely();
        this.f14178v = null;
        this.f14180x = null;
    }

    @Override // androidx.preference.Preference.c
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (!(preference instanceof COUISwitchPreference)) {
            LocalLog.a(this.f12677j, "onPreferenceChange : preference is not expected");
            return false;
        }
        boolean booleanValue = ((Boolean) obj).booleanValue();
        String key = preference.getKey();
        if ("screen_auto_off_switch".equals(key)) {
            f6.f.E3("screen_auto_off_switch", t(booleanValue), this.f14177u);
            m(booleanValue);
            return true;
        }
        if ("screen_bright_switch".equals(key)) {
            f6.f.E3("screen_bright_switch", t(booleanValue), this.f14177u);
            n(booleanValue);
            return true;
        }
        if ("decrease_screen_refresh".equals(key)) {
            f6.f.E3("decrease_screen_refresh", t(booleanValue), this.f14177u);
            o(booleanValue);
            return true;
        }
        if ("decrease_screen_resolution".equals(key)) {
            q(booleanValue);
            return true;
        }
        if ("back_synchronize_switch".equals(key)) {
            f6.f.E3("back_synchronize_switch", t(booleanValue), this.f14177u);
            s(booleanValue);
            return true;
        }
        if (!"five_g_switch".equals(key)) {
            return true;
        }
        f6.f.E3("five_g_switch", t(booleanValue), this.f14177u);
        r(booleanValue);
        return true;
    }
}
