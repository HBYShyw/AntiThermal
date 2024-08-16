package k8;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.preference.Preference;
import b6.LocalLog;
import com.coui.appcompat.preference.COUISwitchPreference;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import h8.IPage;
import h8.IReverseChargPagePresenter;
import i8.BasePagePresenter;
import j8.EventObserver;
import l8.IReverseChargPageUpdate;
import t8.PowerUsageManager;
import v4.GuardElfContext;
import x5.UploadDataUtil;

/* compiled from: ReverseChargPagePresenter.java */
/* renamed from: k8.n, reason: use source file name */
/* loaded from: classes2.dex */
public class ReverseChargPagePresenter extends BasePagePresenter implements IReverseChargPagePresenter, IPage {

    /* renamed from: k, reason: collision with root package name */
    private AlarmManager f14204k;

    /* renamed from: l, reason: collision with root package name */
    private UploadDataUtil f14205l;

    /* renamed from: m, reason: collision with root package name */
    private PowerUsageManager f14206m;

    /* renamed from: n, reason: collision with root package name */
    private PowerUsageManager.c f14207n;

    /* renamed from: o, reason: collision with root package name */
    private Context f14208o;

    /* renamed from: p, reason: collision with root package name */
    private IReverseChargPageUpdate f14209p;

    /* renamed from: q, reason: collision with root package name */
    private EventObserver f14210q;

    /* compiled from: ReverseChargPagePresenter.java */
    /* renamed from: k8.n$a */
    /* loaded from: classes2.dex */
    class a implements PowerUsageManager.c {
        a() {
        }

        @Override // t8.PowerUsageManager.c
        public void a(Intent intent) {
            int intExtra = intent.getIntExtra("plugged", 0);
            int intExtra2 = intent.getIntExtra("level", 0);
            ReverseChargPagePresenter.this.f14209p.t(intent.getIntExtra("status", 1), intExtra, intExtra2, intent.getIntExtra("temperature", 0));
        }
    }

    public ReverseChargPagePresenter(IReverseChargPageUpdate iReverseChargPageUpdate) {
        super(ReverseChargPagePresenter.class.getSimpleName());
        this.f14205l = null;
        this.f14208o = GuardElfContext.e().c();
        this.f14209p = iReverseChargPageUpdate;
    }

    @Override // androidx.preference.Preference.d
    public boolean M(Preference preference) {
        if (!"battery_level_toolow_disable_pref".equals(preference.getKey())) {
            return true;
        }
        this.f14209p.W();
        return true;
    }

    @Override // h8.IReverseChargPagePresenter
    public void a() {
        this.f14204k = (AlarmManager) this.f14208o.getSystemService("alarm");
        this.f14206m = PowerUsageManager.x(this.f14208o);
        this.f14205l = UploadDataUtil.S0(this.f14208o);
        this.f14210q = EventObserver.d(this.f14208o);
    }

    @Override // h8.IReverseChargPagePresenter
    public void b() {
        a aVar = new a();
        this.f14207n = aVar;
        this.f14206m.p(aVar, false);
        this.f14210q.c(this, EventType.SCENE_MODE_AUDIO_IN);
    }

    @Override // h8.IPage
    public void c(int i10, Bundle bundle) {
        if (i10 != 203) {
            return;
        }
        this.f14209p.v(bundle.getBoolean("boolean_wireless_reverse_state"));
    }

    @Override // h8.IPage
    public void e(Intent intent) {
    }

    @Override // h8.IReverseChargPagePresenter
    public void onDetach() {
        this.f14210q.m(this, EventType.SCENE_MODE_AUDIO_IN);
        this.f14206m.C(this.f14207n);
        this.f14209p = null;
    }

    @Override // androidx.preference.Preference.c
    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = preference instanceof COUISwitchPreference ? ((Boolean) obj).booleanValue() : false;
        String key = preference.getKey();
        if (key == null) {
            return false;
        }
        LocalLog.a(this.f12677j, "switch key =" + key + ", check=" + booleanValue);
        if (!"wireless_reverse_charging_pref".equals(key)) {
            return true;
        }
        f6.f.y3(this.f14208o, booleanValue);
        this.f14205l.R0(booleanValue, "batt_reverse_scence_click");
        return true;
    }
}
