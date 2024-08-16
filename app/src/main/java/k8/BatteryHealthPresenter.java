package k8;

import android.content.Context;
import androidx.preference.Preference;
import b6.LocalLog;
import h8.IBatteryHealthPresenter;
import i8.BasePagePresenter;
import l8.IBatteryHealthUpdate;
import v4.GuardElfContext;

/* compiled from: BatteryHealthPresenter.java */
/* renamed from: k8.a, reason: use source file name */
/* loaded from: classes2.dex */
public class BatteryHealthPresenter extends BasePagePresenter implements IBatteryHealthPresenter {

    /* renamed from: k, reason: collision with root package name */
    private Context f14080k;

    /* renamed from: l, reason: collision with root package name */
    private IBatteryHealthUpdate f14081l;

    public BatteryHealthPresenter(IBatteryHealthUpdate iBatteryHealthUpdate) {
        super(BatteryHealthPresenter.class.getSimpleName());
        this.f14080k = GuardElfContext.e().c();
        this.f14081l = iBatteryHealthUpdate;
    }

    @Override // androidx.preference.Preference.d
    public boolean M(Preference preference) {
        preference.getKey();
        return true;
    }

    @Override // h8.IBatteryHealthPresenter
    public void a() {
    }

    @Override // h8.IBatteryHealthPresenter
    public void b() {
    }

    @Override // h8.IBatteryHealthPresenter
    public void onDetach() {
        this.f14081l = null;
    }

    @Override // androidx.preference.Preference.c
    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        String key = preference.getKey();
        LocalLog.a(this.f12677j, "onPreferenceChange : key = " + key + " checked = " + booleanValue);
        return true;
    }
}
