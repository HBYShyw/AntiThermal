package k8;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.preference.Preference;
import b6.LocalLog;
import com.coui.appcompat.preference.COUISwitchPreference;
import com.oplus.powermanager.fuelgaue.WirelessChargingReminderActivity;
import h8.IPage;
import h8.IWirelessChargSetPagePresenter;
import i8.BasePagePresenter;
import l8.IWirelessChargSetPageUpdate;
import v4.GuardElfContext;

/* compiled from: WirelessChargSetPagePresenter.java */
/* renamed from: k8.p, reason: use source file name */
/* loaded from: classes2.dex */
public class WirelessChargSetPagePresenter extends BasePagePresenter implements IWirelessChargSetPagePresenter, IPage {

    /* renamed from: k, reason: collision with root package name */
    private Context f14217k;

    /* renamed from: l, reason: collision with root package name */
    private IWirelessChargSetPageUpdate f14218l;

    public WirelessChargSetPagePresenter(IWirelessChargSetPageUpdate iWirelessChargSetPageUpdate) {
        super(ReverseChargPagePresenter.class.getSimpleName());
        this.f14217k = GuardElfContext.e().c();
        this.f14218l = iWirelessChargSetPageUpdate;
    }

    @Override // androidx.preference.Preference.d
    public boolean M(Preference preference) {
        String key = preference.getKey();
        if (!"wireless_charging_guide_pref".equals(key)) {
            return true;
        }
        Intent intent = new Intent(this.f14217k, (Class<?>) WirelessChargingReminderActivity.class);
        intent.setFlags(872415232);
        this.f14217k.startActivity(intent);
        return true;
    }

    @Override // h8.IPage
    public void c(int i10, Bundle bundle) {
    }

    @Override // h8.IPage
    public void e(Intent intent) {
    }

    @Override // androidx.preference.Preference.c
    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = preference instanceof COUISwitchPreference ? ((Boolean) obj).booleanValue() : false;
        String key = preference.getKey();
        if (key == null) {
            return false;
        }
        LocalLog.a(this.f12677j, "switch key =" + key + ", check=" + booleanValue);
        if ("low_power_charging_pref".equals(key)) {
            f6.f.E3("low_power_charging_pref", booleanValue ? "1" : "0", this.f14217k);
            f6.f.w2(this.f14217k, booleanValue);
            this.f14218l.P(booleanValue);
            this.f14218l.R();
        } else if ("sleep_optimize_pref".equals(key)) {
            f6.f.E3("silent_mode_type_state", "0", this.f14217k);
            f6.f.Q2(this.f14217k, 0);
            this.f14218l.R();
        } else if ("slient_alwayson_pref".equals(key)) {
            f6.f.E3("silent_mode_type_state", "1", this.f14217k);
            f6.f.Q2(this.f14217k, 1);
            this.f14218l.R();
        } else if ("custom_optimize_pref".equals(key)) {
            f6.f.E3("silent_mode_type_state", "2", this.f14217k);
            f6.f.Q2(this.f14217k, 2);
            this.f14218l.R();
        }
        return true;
    }

    @Override // h8.IWirelessChargSetPagePresenter
    public void p() {
        long X = f6.f.X(this.f14217k, true);
        long X2 = f6.f.X(this.f14217k, false);
        long currentTimeMillis = System.currentTimeMillis();
        if (X <= 0 || X2 <= 0) {
            return;
        }
        if (f6.f.f(this.f14217k)) {
            f6.f.R2(this.f14217k, 1);
            f6.f.O1(this.f14217k, true, X + currentTimeMillis);
            f6.f.O1(this.f14217k, false, currentTimeMillis + X2);
        } else {
            f6.f.R2(this.f14217k, 0);
            f6.f.O1(this.f14217k, true, X + currentTimeMillis);
            f6.f.O1(this.f14217k, false, currentTimeMillis + X2);
        }
    }
}
