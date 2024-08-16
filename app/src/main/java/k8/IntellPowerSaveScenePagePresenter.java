package k8;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import b6.LocalLog;
import com.oplus.battery.R;
import f6.CommonUtil;
import h8.IIntellPowerSaveScenePagePresenter;
import h8.IPage;
import i8.BasePagePresenter;
import j8.EventObserver;
import java.util.HashMap;
import l8.IIntellPowerSaveScenceUpdate;
import v4.GuardElfContext;
import x1.COUIAlertDialogBuilder;
import x5.UploadDataUtil;

/* compiled from: IntellPowerSaveScenePagePresenter.java */
/* renamed from: k8.e, reason: use source file name */
/* loaded from: classes2.dex */
public class IntellPowerSaveScenePagePresenter extends BasePagePresenter implements IIntellPowerSaveScenePagePresenter, IPage {

    /* renamed from: k, reason: collision with root package name */
    private Context f14105k;

    /* renamed from: l, reason: collision with root package name */
    private UploadDataUtil f14106l;

    /* renamed from: m, reason: collision with root package name */
    private IIntellPowerSaveScenceUpdate f14107m;

    /* renamed from: n, reason: collision with root package name */
    private EventObserver f14108n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f14109o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f14110p;

    /* renamed from: q, reason: collision with root package name */
    private AlertDialog f14111q;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: IntellPowerSaveScenePagePresenter.java */
    /* renamed from: k8.e$a */
    /* loaded from: classes2.dex */
    public class a implements DialogInterface.OnClickListener {
        a() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            IntellPowerSaveScenePagePresenter.this.f14107m.e(true, true, true);
            HashMap hashMap = new HashMap();
            hashMap.put("dialog", "true");
            IntellPowerSaveScenePagePresenter.this.f14106l.H(hashMap);
            ((PowerManager) IntellPowerSaveScenePagePresenter.this.f14105k.getApplicationContext().getSystemService("power")).setPowerSaveModeEnabled(false);
            CommonUtil.b0(GuardElfContext.e().c());
            IntellPowerSaveScenePagePresenter.this.f14110p = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: IntellPowerSaveScenePagePresenter.java */
    /* renamed from: k8.e$b */
    /* loaded from: classes2.dex */
    public class b implements DialogInterface.OnClickListener {
        b() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            HashMap hashMap = new HashMap();
            hashMap.put("dialog", "true");
            IntellPowerSaveScenePagePresenter.this.f14106l.H(hashMap);
            CommonUtil.b(GuardElfContext.e().c());
            IntellPowerSaveScenePagePresenter.this.f14107m.e(true, true, false);
            IntellPowerSaveScenePagePresenter.this.f14110p = false;
        }
    }

    public IntellPowerSaveScenePagePresenter(Context context, IIntellPowerSaveScenceUpdate iIntellPowerSaveScenceUpdate) {
        super(ReverseChargPagePresenter.class.getSimpleName());
        this.f14109o = false;
        this.f14110p = false;
        this.f14111q = null;
        this.f14105k = context;
        this.f14107m = iIntellPowerSaveScenceUpdate;
        this.f14106l = UploadDataUtil.S0(context);
        this.f14109o = f6.f.i1();
        this.f14108n = EventObserver.d(this.f14105k.getApplicationContext());
    }

    private void m() {
        CommonUtil.b(GuardElfContext.e().c());
    }

    private void n() {
        if (this.f14110p) {
            return;
        }
        this.f14110p = true;
        Context context = this.f14105k;
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(context, f6.f.k(context));
        cOUIAlertDialogBuilder.h0(R.string.high_performance_title);
        cOUIAlertDialogBuilder.Y(R.string.high_performance_message_new);
        cOUIAlertDialogBuilder.e0(R.string.dialog_button_confirm, new a());
        cOUIAlertDialogBuilder.a0(R.string.dialog_button_cancel, new b());
        cOUIAlertDialogBuilder.d(false);
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.f14111q = a10;
        Window window = a10.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f6.f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        window.setAttributes(attributes);
        this.f14111q.show();
        this.f14111q.i(-1).setTextColor(this.f14105k.getResources().getColor(R.color.battery_num_color_abnormal));
    }

    @Override // h8.IIntellPowerSaveScenePagePresenter
    public void E(int i10) {
        this.f14108n.c(this, i10);
    }

    @Override // h8.IPage
    public void c(int i10, Bundle bundle) {
        if (i10 != 202) {
            return;
        }
        if (bundle.getBoolean("boolean_highpref_state")) {
            IIntellPowerSaveScenceUpdate iIntellPowerSaveScenceUpdate = this.f14107m;
            if (iIntellPowerSaveScenceUpdate == null || !this.f14109o) {
                return;
            }
            iIntellPowerSaveScenceUpdate.e(true, true, true);
            return;
        }
        IIntellPowerSaveScenceUpdate iIntellPowerSaveScenceUpdate2 = this.f14107m;
        if (iIntellPowerSaveScenceUpdate2 == null || !this.f14109o) {
            return;
        }
        iIntellPowerSaveScenceUpdate2.e(true, true, false);
    }

    @Override // h8.IPage
    public void e(Intent intent) {
    }

    @Override // h8.IIntellPowerSaveScenePagePresenter
    public void h(int i10) {
        this.f14108n.m(this, i10);
    }

    @Override // androidx.preference.Preference.c
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (obj instanceof Boolean) {
            boolean booleanValue = ((Boolean) obj).booleanValue();
            String key = preference.getKey();
            if (key == null) {
                LocalLog.a(this.f12677j, "onPreferenceChange: key is null.");
                return false;
            }
            LocalLog.a(this.f12677j, "onPreferenceChange: key=" + key + ", check=" + booleanValue);
            if ("intelligent_deep_sleep_mode".equals(key)) {
                f6.f.E3("intelligent_deep_sleep_mode", booleanValue ? "1" : "0", this.f14105k.getApplicationContext());
                f6.f.f2(this.f14105k, booleanValue);
                this.f14106l.w(booleanValue);
            } else if (key.equals("high_performance_switch_in_more")) {
                HashMap hashMap = new HashMap();
                hashMap.put("switch", String.valueOf(booleanValue));
                this.f14106l.J(hashMap);
                if (1 == CommonUtil.A()) {
                    m();
                } else {
                    n();
                }
            } else if (key.equals("full_power_estimated_time")) {
                f6.f.m2(this.f14105k, booleanValue);
                f6.f.E3("full_power_estimated_time_switch", booleanValue ? "1" : "0", this.f14105k);
            }
            return true;
        }
        LocalLog.a(this.f12677j, "onPreferenceChange: value is not Boolean.");
        return false;
    }
}
