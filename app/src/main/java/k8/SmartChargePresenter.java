package k8;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.preference.Preference;
import b6.LocalLog;
import com.coui.appcompat.preference.COUISwitchPreference;
import f6.ChargeUtil;
import h8.ISmartChargePresenter;
import i8.BasePagePresenter;
import l8.ISmartChargeUpdate;
import v4.BatteryStatsManager;
import v4.GuardElfContext;
import x5.UploadDataUtil;
import y5.AppFeature;

/* compiled from: SmartChargePresenter.java */
/* renamed from: k8.o, reason: use source file name */
/* loaded from: classes2.dex */
public class SmartChargePresenter extends BasePagePresenter implements ISmartChargePresenter {

    /* renamed from: k, reason: collision with root package name */
    private final String f14212k;

    /* renamed from: l, reason: collision with root package name */
    private Context f14213l;

    /* renamed from: m, reason: collision with root package name */
    private Handler f14214m;

    /* renamed from: n, reason: collision with root package name */
    private HandlerThread f14215n;

    /* renamed from: o, reason: collision with root package name */
    private ISmartChargeUpdate f14216o;

    public SmartChargePresenter(ISmartChargeUpdate iSmartChargeUpdate) {
        super(SmartChargePresenter.class.getSimpleName());
        this.f14212k = "SmartChargeHandler";
        this.f14213l = null;
        this.f14214m = null;
        this.f14215n = null;
        this.f14216o = null;
        this.f14213l = GuardElfContext.e().c();
        this.f14216o = iSmartChargeUpdate;
    }

    private void i() {
        this.f14216o.N();
    }

    private void j(boolean z10) {
        f6.f.e2(this.f14213l, z10);
        this.f14216o.C(Boolean.valueOf(z10));
        UploadDataUtil.S0(this.f14213l).u0(z10 ? "cm_on" : "cm_off");
    }

    private void k(boolean z10) {
        f6.f.e3(this.f14213l, z10);
        this.f14216o.o(Boolean.valueOf(z10));
        if (y5.b.D() && AppFeature.B() && AppFeature.j() && AppFeature.k() == 1) {
            LocalLog.a(this.f12677j, "updateLifeModeTrigger checked =" + z10);
            ChargeUtil.B(z10 ^ true);
        }
        BatteryStatsManager i10 = BatteryStatsManager.i();
        UploadDataUtil S0 = UploadDataUtil.S0(this.f14213l);
        String str = z10 ? "on" : "off";
        S0.v0("Switch:" + str + ";Time:" + ChargeUtil.g());
        S0.t0("Switch:" + str + ";Temp:" + i10.e());
        S0.s0("Switch:" + str + ";Level:" + i10.c());
    }

    @Override // androidx.preference.Preference.d
    public boolean M(Preference preference) {
        if (!"customize_charge_apps".equals(preference.getKey())) {
            return true;
        }
        i();
        return true;
    }

    @Override // h8.ISmartChargePresenter
    public void a() {
        HandlerThread handlerThread = new HandlerThread("SmartChargeHandler");
        this.f14215n = handlerThread;
        handlerThread.start();
        try {
            boolean J0 = f6.f.J0(this.f14213l);
            this.f14216o.o(Boolean.valueOf(J0));
            LocalLog.a(this.f12677j, "initSmartChargeSwitch: isSmartChargeOn=" + J0);
        } catch (Exception unused) {
            LocalLog.b(this.f12677j, "onCreatePreferences NullPointerException");
        }
    }

    @Override // h8.ISmartChargePresenter
    public void b() {
    }

    @Override // h8.ISmartChargePresenter
    public void onDetach() {
        this.f14215n.quitSafely();
        this.f14215n = null;
        this.f14216o = null;
    }

    @Override // androidx.preference.Preference.c
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (!(preference instanceof COUISwitchPreference)) {
            LocalLog.a(this.f12677j, "onPreferenceChange : preference is not expected");
            return false;
        }
        Boolean bool = (Boolean) obj;
        boolean booleanValue = bool.booleanValue();
        String key = preference.getKey();
        LocalLog.a(this.f12677j, "onPreferenceChange : key = " + key + " checked = " + booleanValue);
        if ("smart_charge_switch".equals(key)) {
            k(booleanValue);
        }
        if (!"customize_charge_switch".equals(preference.getKey())) {
            return true;
        }
        j(bool.booleanValue());
        return true;
    }
}
