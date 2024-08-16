package m8;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.preference.Preference;
import b6.LocalLog;
import com.coui.appcompat.preference.COUIPreferenceCategory;
import com.coui.appcompat.preference.COUISwitchPreference;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import ia.ThermalCustomizeChargeUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import x9.IconUtils;

/* compiled from: CustomizeChargeFragment.java */
/* renamed from: m8.b, reason: use source file name */
/* loaded from: classes2.dex */
public class CustomizeChargeFragment extends BasePreferenceFragment implements Preference.c {

    /* renamed from: e, reason: collision with root package name */
    private COUIPreferenceCategory f14942e;

    /* renamed from: f, reason: collision with root package name */
    private RelativeLayout f14943f;

    /* renamed from: g, reason: collision with root package name */
    private Context f14944g;

    /* renamed from: h, reason: collision with root package name */
    private Drawable f14945h;

    /* renamed from: i, reason: collision with root package name */
    private List<b> f14946i = new ArrayList();

    /* renamed from: j, reason: collision with root package name */
    private Map<String, Drawable> f14947j = new HashMap();

    /* renamed from: k, reason: collision with root package name */
    private Handler f14948k = new a();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CustomizeChargeFragment.java */
    /* renamed from: m8.b$a */
    /* loaded from: classes2.dex */
    public class a extends Handler {
        a() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1) {
                CustomizeChargeFragment.this.i0();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: CustomizeChargeFragment.java */
    /* renamed from: m8.b$b */
    /* loaded from: classes2.dex */
    public class b {

        /* renamed from: a, reason: collision with root package name */
        public String f14950a;

        /* renamed from: b, reason: collision with root package name */
        public String f14951b;

        /* renamed from: c, reason: collision with root package name */
        public boolean f14952c;

        private b() {
        }

        /* synthetic */ b(CustomizeChargeFragment customizeChargeFragment, a aVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: CustomizeChargeFragment.java */
    /* renamed from: m8.b$c */
    /* loaded from: classes2.dex */
    public class c implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private List<String> f14954e;

        public c(List<String> list) {
            this.f14954e = list;
        }

        @Override // java.lang.Runnable
        public void run() {
            for (int i10 = 0; i10 < this.f14954e.size(); i10++) {
                String str = this.f14954e.get(i10);
                Drawable c10 = ThermalCustomizeChargeUtils.h(CustomizeChargeFragment.this.f14944g).c(str);
                if (c10 != null) {
                    CustomizeChargeFragment.this.f14947j.put(str, c10);
                }
            }
            if (CustomizeChargeFragment.this.f14948k.hasMessages(1)) {
                CustomizeChargeFragment.this.f14948k.removeMessages(1);
            }
            CustomizeChargeFragment.this.f14948k.sendEmptyMessage(1);
        }
    }

    private void h0() {
        ArrayList<String> e10 = ThermalCustomizeChargeUtils.h(this.f14944g).e();
        if (e10 == null || e10.isEmpty()) {
            return;
        }
        Collections.sort(e10);
        for (int i10 = 0; i10 < e10.size(); i10++) {
            String str = e10.get(i10);
            b bVar = new b(this, null);
            bVar.f14950a = str;
            bVar.f14952c = ThermalCustomizeChargeUtils.h(this.f14944g).f(str);
            bVar.f14951b = ThermalCustomizeChargeUtils.h(this.f14944g).d(str);
            LocalLog.a("CustomizeChargeFragment", "after initAppData pkgName =" + bVar.f14950a + " isChecked =" + bVar.f14952c);
            this.f14946i.add(bVar);
        }
        Map<String, Drawable> map = this.f14947j;
        if (map != null && !map.isEmpty()) {
            i0();
        } else {
            new Thread(new c(e10)).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i0() {
        RelativeLayout relativeLayout = this.f14943f;
        if (relativeLayout != null) {
            relativeLayout.setVisibility(8);
        }
        COUIPreferenceCategory cOUIPreferenceCategory = (COUIPreferenceCategory) findPreference("customize_charge_apps_category");
        this.f14942e = cOUIPreferenceCategory;
        if (cOUIPreferenceCategory != null) {
            for (int i10 = 0; i10 < this.f14946i.size(); i10++) {
                b bVar = this.f14946i.get(i10);
                COUISwitchPreference cOUISwitchPreference = (COUISwitchPreference) this.f14942e.e(bVar.f14950a);
                if (cOUISwitchPreference == null) {
                    cOUISwitchPreference = new COUISwitchPreference(this.f14944g);
                    cOUISwitchPreference.setKey(this.f14946i.get(i10).f14950a);
                }
                cOUISwitchPreference.setIsCustomIconRadius(true);
                cOUISwitchPreference.setOrder(i10);
                cOUISwitchPreference.setChecked(bVar.f14952c);
                cOUISwitchPreference.setPersistent(false);
                cOUISwitchPreference.setTitle(bVar.f14951b);
                Map<String, Drawable> map = this.f14947j;
                if (map != null && !map.isEmpty() && this.f14947j.get(bVar.f14950a) != null) {
                    if (this.f14947j.get(bVar.f14950a) != null) {
                        cOUISwitchPreference.setIcon(this.f14947j.get(bVar.f14950a));
                    }
                } else {
                    cOUISwitchPreference.setIcon(this.f14945h);
                }
                cOUISwitchPreference.setOnPreferenceChangeListener(this);
                this.f14942e.d(cOUISwitchPreference);
            }
        }
    }

    private void j0(View view) {
        View inflate = LayoutInflater.from(this.f14944g).inflate(R.layout.fragment_loading, (ViewGroup) view);
        if (inflate != null) {
            RelativeLayout relativeLayout = (RelativeLayout) inflate.findViewById(R.id.fragment_startup);
            this.f14943f = relativeLayout;
            if (relativeLayout != null) {
                relativeLayout.setVisibility(0);
            }
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return getString(R.string.customize_charge_apps_title);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        LocalLog.a("CustomizeChargeFragment", "onAttach");
        super.onAttach(context);
        this.f14944g = context;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        LocalLog.a("CustomizeChargeFragment", "onCreate");
        super.onCreate(bundle);
        h0();
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        LocalLog.a("CustomizeChargeFragment", "onCreatePreferences");
        addPreferencesFromResource(R.xml.customize_charge_apps_preference);
        this.f14945h = IconUtils.b(this.f14944g, this.f14944g.getDrawable(R.drawable.file_apk_icon));
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        LocalLog.a("CustomizeChargeFragment", "onDestroy");
        super.onDestroy();
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onDestroyView() {
        LocalLog.a("CustomizeChargeFragment", "onDestroyView");
        super.onDestroyView();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        LocalLog.a("CustomizeChargeFragment", "onDetach");
        super.onDetach();
    }

    @Override // androidx.preference.Preference.c
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (!(preference instanceof COUISwitchPreference)) {
            LocalLog.a("CustomizeChargeFragment", "onPreferenceChange : preference is not expected");
            return false;
        }
        boolean booleanValue = ((Boolean) obj).booleanValue();
        String key = preference.getKey();
        LocalLog.a("CustomizeChargeFragment", "onPreferenceChange : key = " + key + " checked = " + booleanValue);
        ThermalCustomizeChargeUtils.h(this.f14944g).l(key, booleanValue);
        return true;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        LocalLog.a("CustomizeChargeFragment", "onResume");
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        LocalLog.a("CustomizeChargeFragment", "onViewCreated");
        super.onViewCreated(view, bundle);
        if (view != null) {
            j0(view);
        }
    }
}
