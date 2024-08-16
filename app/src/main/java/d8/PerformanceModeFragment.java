package d8;

import android.content.Context;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import b6.LocalLog;
import com.coui.appcompat.preference.COUIMarkPreference;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import f6.CommonUtil;
import f6.f;
import java.lang.ref.WeakReference;
import v4.GuardElfContext;
import x1.COUIAlertDialogBuilder;

/* compiled from: PerformanceModeFragment.java */
/* renamed from: d8.a, reason: use source file name */
/* loaded from: classes.dex */
public class PerformanceModeFragment extends BasePreferenceFragment implements Preference.c {

    /* renamed from: e, reason: collision with root package name */
    private Context f10778e = null;

    /* renamed from: f, reason: collision with root package name */
    private c f10779f = null;

    /* renamed from: g, reason: collision with root package name */
    private boolean f10780g = false;

    /* renamed from: h, reason: collision with root package name */
    private d f10781h = null;

    /* renamed from: i, reason: collision with root package name */
    private AlertDialog f10782i = null;

    /* renamed from: j, reason: collision with root package name */
    private COUIMarkPreference f10783j = null;

    /* renamed from: k, reason: collision with root package name */
    private COUIMarkPreference f10784k = null;

    /* renamed from: l, reason: collision with root package name */
    private COUIMarkPreference f10785l = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PerformanceModeFragment.java */
    /* renamed from: d8.a$a */
    /* loaded from: classes.dex */
    public class a implements DialogInterface.OnClickListener {
        a() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            ((PowerManager) PerformanceModeFragment.this.f10778e.getSystemService("power")).setPowerSaveModeEnabled(false);
            CommonUtil.j0(GuardElfContext.e().c(), 2);
            PerformanceModeFragment.this.f10780g = false;
            PerformanceModeFragment.this.m0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PerformanceModeFragment.java */
    /* renamed from: d8.a$b */
    /* loaded from: classes.dex */
    public class b implements DialogInterface.OnClickListener {
        b() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            PerformanceModeFragment.this.m0();
            PerformanceModeFragment.this.f10780g = false;
        }
    }

    /* compiled from: PerformanceModeFragment.java */
    /* renamed from: d8.a$c */
    /* loaded from: classes.dex */
    static class c extends Handler {

        /* renamed from: a, reason: collision with root package name */
        private WeakReference<PerformanceModeFragment> f10788a;

        public c(PerformanceModeFragment performanceModeFragment) {
            this.f10788a = new WeakReference<>(performanceModeFragment);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            WeakReference<PerformanceModeFragment> weakReference;
            PerformanceModeFragment performanceModeFragment;
            if (message.what != 100 || (weakReference = this.f10788a) == null || (performanceModeFragment = weakReference.get()) == null) {
                return;
            }
            performanceModeFragment.m0();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PerformanceModeFragment.java */
    /* renamed from: d8.a$d */
    /* loaded from: classes.dex */
    public class d extends ContentObserver {
        public d() {
            super(new Handler());
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            super.onChange(z10);
            PerformanceModeFragment.this.f10779f.sendEmptyMessageDelayed(100, 100L);
        }
    }

    private void h0() {
        if (this.f10780g) {
            return;
        }
        this.f10780g = true;
        Context context = this.f10778e;
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(context, f.k(context));
        cOUIAlertDialogBuilder.h0(R.string.high_performance_title);
        cOUIAlertDialogBuilder.Y(R.string.high_performance_message_new);
        cOUIAlertDialogBuilder.e0(R.string.high_performance_yes, new a());
        cOUIAlertDialogBuilder.a0(R.string.high_performance_cancel, new b());
        cOUIAlertDialogBuilder.d(false);
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.f10782i = a10;
        Window window = a10.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f.n2(attributes, 1);
        } catch (Exception unused) {
            LocalLog.l("PerformanceModeFragment", "setHomeAndMenuKeyState error");
        }
        window.setAttributes(attributes);
        this.f10782i.show();
    }

    private void i0() {
        CommonUtil.j0(GuardElfContext.e().c(), 0);
        m0();
    }

    private void initPreference() {
        this.f10783j = (COUIMarkPreference) findPreference("high_performance_mode");
        this.f10784k = (COUIMarkPreference) findPreference("intelligent_performance_mode");
        this.f10785l = (COUIMarkPreference) findPreference("no_performance_mode");
    }

    private void j0() {
        CommonUtil.j0(GuardElfContext.e().c(), 1);
        m0();
    }

    private void k0() {
        this.f10781h = new d();
        this.f10778e.getContentResolver().registerContentObserver(Settings.System.getUriFor("high_performance_mode_on"), true, this.f10781h, 0);
        this.f10778e.getContentResolver().registerContentObserver(Settings.System.getUriFor("performance_mode_enable"), true, this.f10781h, 0);
    }

    private void l0() {
        if (this.f10781h != null) {
            this.f10778e.getContentResolver().unregisterContentObserver(this.f10781h);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m0() {
        if (this.f10783j == null || this.f10784k == null || this.f10785l == null) {
            return;
        }
        int H = CommonUtil.H(this.f10778e);
        if (H == 0) {
            this.f10783j.setChecked(false);
            this.f10784k.setChecked(false);
            this.f10785l.setChecked(true);
        } else if (H == 1) {
            this.f10783j.setChecked(false);
            this.f10784k.setChecked(true);
            this.f10785l.setChecked(false);
        } else {
            if (H != 2) {
                return;
            }
            this.f10783j.setChecked(true);
            this.f10784k.setChecked(false);
            this.f10785l.setChecked(false);
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return getString(R.string.performance_mode_title);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.f10778e = context;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f10779f = new c(this);
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        addPreferencesFromResource(R.xml.performance_mode_preference);
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment, com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        l0();
    }

    @Override // androidx.preference.Preference.c
    public boolean onPreferenceChange(Preference preference, Object obj) {
        return false;
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.c
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();
        int H = CommonUtil.H(this.f10778e);
        LocalLog.a("PerformanceModeFragment", "onPreferenceClick key = " + key + " current mode = " + H);
        if ("high_performance_mode".equals(key)) {
            if (H != 2) {
                h0();
            }
        } else if ("intelligent_performance_mode".equals(key)) {
            if (H != 1) {
                j0();
            }
        } else if ("no_performance_mode".equals(key) && H != 0) {
            i0();
        }
        m0();
        return true;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        m0();
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        initPreference();
        k0();
    }
}
