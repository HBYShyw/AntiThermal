package ca;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.h0;
import androidx.preference.Preference;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.preference.COUIJumpPreference;
import com.coui.appcompat.preference.COUIPreferenceCategory;
import com.coui.appcompat.preference.COUISwitchPreference;
import com.oplus.battery.R;
import com.oplus.startupapp.view.PreventRecordActivity;
import da.StartupViewModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import x1.COUIAlertDialogBuilder;
import x9.IconUtils;
import z9.AppToShow;

/* compiled from: StartupAppListFragment.java */
/* renamed from: ca.b, reason: use source file name */
/* loaded from: classes2.dex */
public class StartupAppListFragment extends v9.d {

    /* renamed from: i, reason: collision with root package name */
    private Activity f5013i;

    /* renamed from: j, reason: collision with root package name */
    private Drawable f5014j;

    /* renamed from: k, reason: collision with root package name */
    private COUIPreferenceCategory f5015k;

    /* renamed from: l, reason: collision with root package name */
    private COUIPreferenceCategory f5016l;

    /* renamed from: m, reason: collision with root package name */
    private COUIPreferenceCategory f5017m;

    /* renamed from: n, reason: collision with root package name */
    private AlertDialog f5018n;

    /* renamed from: o, reason: collision with root package name */
    private Preference f5019o;

    /* renamed from: p, reason: collision with root package name */
    private Preference f5020p;

    /* renamed from: q, reason: collision with root package name */
    private RecyclerView f5021q;

    /* renamed from: w, reason: collision with root package name */
    private StartupViewModel f5027w;

    /* renamed from: r, reason: collision with root package name */
    private List<AppToShow> f5022r = new ArrayList();

    /* renamed from: s, reason: collision with root package name */
    private List<AppToShow> f5023s = new ArrayList();

    /* renamed from: t, reason: collision with root package name */
    private int f5024t = 0;

    /* renamed from: u, reason: collision with root package name */
    private int f5025u = 0;

    /* renamed from: v, reason: collision with root package name */
    private int f5026v = 2;

    /* renamed from: x, reason: collision with root package name */
    private int f5028x = 0;

    /* renamed from: y, reason: collision with root package name */
    private boolean f5029y = false;

    /* renamed from: z, reason: collision with root package name */
    private Preference.c f5030z = new g();

    /* compiled from: StartupAppListFragment.java */
    /* renamed from: ca.b$a */
    /* loaded from: classes2.dex */
    class a implements Observer<List<AppToShow>> {
        a() {
        }

        @Override // androidx.lifecycle.Observer
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(List<AppToShow> list) {
            if (StartupAppListFragment.this.f5022r.size() > StartupAppListFragment.this.f5026v && list.isEmpty()) {
                Log.d("StartupManager", "suggest refresh database, don't remove all apps");
                return;
            }
            if (StartupAppListFragment.this.f5022r.isEmpty()) {
                StartupAppListFragment.this.f5022r.addAll(list);
            }
            StartupAppListFragment.this.f5027w.h(list, true);
        }
    }

    /* compiled from: StartupAppListFragment.java */
    /* renamed from: ca.b$b */
    /* loaded from: classes2.dex */
    class b implements Observer<List<AppToShow>> {
        b() {
        }

        @Override // androidx.lifecycle.Observer
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(List<AppToShow> list) {
            if (StartupAppListFragment.this.f5023s.size() > StartupAppListFragment.this.f5026v && list.isEmpty()) {
                Log.d("StartupManager", "not suggest refresh database, don't removeall");
                return;
            }
            if (StartupAppListFragment.this.f5023s.isEmpty()) {
                StartupAppListFragment.this.f5023s.addAll(list);
            }
            StartupAppListFragment.this.f5027w.h(list, false);
        }
    }

    /* compiled from: StartupAppListFragment.java */
    /* renamed from: ca.b$c */
    /* loaded from: classes2.dex */
    class c implements Observer<List<AppToShow>> {
        c() {
        }

        @Override // androidx.lifecycle.Observer
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(List<AppToShow> list) {
            ArrayMap arrayMap = new ArrayMap();
            arrayMap.putAll(StartupAppListFragment.this.f5027w.g(list, true));
            StartupAppListFragment.this.G0(list, arrayMap, true);
        }
    }

    /* compiled from: StartupAppListFragment.java */
    /* renamed from: ca.b$d */
    /* loaded from: classes2.dex */
    class d implements Observer<List<AppToShow>> {
        d() {
        }

        @Override // androidx.lifecycle.Observer
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(List<AppToShow> list) {
            ArrayMap arrayMap = new ArrayMap();
            arrayMap.putAll(StartupAppListFragment.this.f5027w.g(list, false));
            StartupAppListFragment.this.G0(list, arrayMap, false);
        }
    }

    /* compiled from: StartupAppListFragment.java */
    /* renamed from: ca.b$e */
    /* loaded from: classes2.dex */
    class e implements Observer<Map<String, Drawable>> {
        e() {
        }

        @Override // androidx.lifecycle.Observer
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(Map<String, Drawable> map) {
            StartupAppListFragment.this.F0(map, true);
            StartupAppListFragment.this.z0(2);
        }
    }

    /* compiled from: StartupAppListFragment.java */
    /* renamed from: ca.b$f */
    /* loaded from: classes2.dex */
    class f implements Observer<Map<String, Drawable>> {
        f() {
        }

        @Override // androidx.lifecycle.Observer
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(Map<String, Drawable> map) {
            StartupAppListFragment.this.F0(map, false);
            StartupAppListFragment.this.z0(1);
        }
    }

    /* compiled from: StartupAppListFragment.java */
    /* renamed from: ca.b$g */
    /* loaded from: classes2.dex */
    class g implements Preference.c {
        g() {
        }

        @Override // androidx.preference.Preference.c
        public boolean onPreferenceChange(Preference preference, Object obj) {
            Log.d("StartupManager", "change preference switch");
            COUISwitchPreference cOUISwitchPreference = (COUISwitchPreference) preference;
            boolean booleanValue = ((Boolean) obj).booleanValue();
            if (StartupAppListFragment.this.f5025u == 0) {
                if (((COUISwitchPreference) StartupAppListFragment.this.f5016l.e(preference.getKey())) == null && booleanValue) {
                    StartupAppListFragment startupAppListFragment = StartupAppListFragment.this;
                    startupAppListFragment.f5024t = startupAppListFragment.f5027w.i(true);
                    if (StartupAppListFragment.this.f5024t >= StartupAppListFragment.this.f5027w.k()) {
                        StartupAppListFragment startupAppListFragment2 = StartupAppListFragment.this;
                        startupAppListFragment2.E0(startupAppListFragment2.f5024t, StartupAppListFragment.this.f5027w.k());
                        return false;
                    }
                }
                cOUISwitchPreference.setChecked(booleanValue);
                if (booleanValue) {
                    if (preference.getSummary().equals(StartupAppListFragment.this.f5013i.getString(R.string.startup_applist_summary_both_forbid))) {
                        preference.setSummary(R.string.startup_applist_summary_both);
                    } else {
                        preference.setSummary(R.string.startup_applist_summary_background);
                    }
                } else if (preference.getSummary().equals(StartupAppListFragment.this.f5013i.getString(R.string.startup_applist_summary_both))) {
                    preference.setSummary(R.string.startup_applist_summary_both_forbid);
                } else {
                    preference.setSummary(R.string.startup_applist_summary_background_forbid);
                }
            } else {
                cOUISwitchPreference.setChecked(booleanValue);
                if (preference.getSummary().equals(StartupAppListFragment.this.f5013i.getString(R.string.associate_startup_recommend_open_summary))) {
                    preference.setSummary(StartupAppListFragment.this.w0(R.string.associate_startup_recommend_open_summary_prefix, R.string.associate_startup_recommend_open_summary_suffix));
                } else if (preference.getSummary().toString().equals(StartupAppListFragment.this.A0(R.string.associate_startup_recommend_open_summary_prefix, R.string.associate_startup_recommend_open_summary_suffix))) {
                    preference.setSummary(StartupAppListFragment.this.f5013i.getString(R.string.associate_startup_recommend_open_summary));
                } else if (preference.getSummary().equals(StartupAppListFragment.this.f5013i.getString(R.string.associate_startup_recommend_close_summary))) {
                    preference.setSummary(StartupAppListFragment.this.w0(R.string.associate_startup_recommend_close_summary_prefix_device, R.string.associate_startup_recommend_close_summary_suffix));
                } else if (preference.getSummary().toString().equals(StartupAppListFragment.this.A0(R.string.associate_startup_recommend_close_summary_prefix_device, R.string.associate_startup_recommend_close_summary_suffix))) {
                    preference.setSummary(StartupAppListFragment.this.f5013i.getString(R.string.associate_startup_recommend_close_summary));
                }
            }
            StartupAppListFragment.this.f5027w.z(preference.getKey(), booleanValue);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StartupAppListFragment.java */
    /* renamed from: ca.b$h */
    /* loaded from: classes2.dex */
    public class h implements Preference.d {
        h() {
        }

        @Override // androidx.preference.Preference.d
        public boolean M(Preference preference) {
            Intent intent = new Intent(StartupAppListFragment.this.f5013i, (Class<?>) PreventRecordActivity.class);
            intent.putExtra("navigate_title_id", R.string.auto_launch_record_title);
            intent.putExtra("show_scene", StartupAppListFragment.this.f5025u);
            StartupAppListFragment.this.startActivity(intent);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String A0(int i10, int i11) {
        StringBuilder sb2 = new StringBuilder(50);
        sb2.append(this.f5013i.getString(i10));
        sb2.append(" ");
        sb2.append(this.f5013i.getString(i11));
        return sb2.toString();
    }

    private void B0() {
        e0().setVisibility(8);
        g0().setVisibility(8);
        this.f5021q.setVisibility(0);
    }

    private void C0() {
        e0().setVisibility(0);
        g0().setVisibility(8);
        this.f5021q.setVisibility(8);
    }

    private void D0() {
        e0().setVisibility(8);
        g0().setVisibility(0);
        this.f5021q.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void F0(Map<String, Drawable> map, boolean z10) {
        COUISwitchPreference cOUISwitchPreference;
        if (map == null || map.isEmpty()) {
            return;
        }
        for (String str : map.keySet()) {
            if (str != null) {
                if (z10) {
                    cOUISwitchPreference = (COUISwitchPreference) this.f5016l.e(str);
                } else {
                    cOUISwitchPreference = (COUISwitchPreference) this.f5017m.e(str);
                }
                if (cOUISwitchPreference != null) {
                    cOUISwitchPreference.setIsCustomIconRadius(true);
                    cOUISwitchPreference.setIcon(map.get(str));
                    if (z10) {
                        this.f5016l.d(cOUISwitchPreference);
                    } else {
                        this.f5017m.d(cOUISwitchPreference);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void G0(List<AppToShow> list, Map<String, Drawable> map, boolean z10) {
        List<AppToShow> list2;
        List<AppToShow> list3;
        COUISwitchPreference cOUISwitchPreference;
        if (z10) {
            if (((COUIPreferenceCategory) getPreferenceScreen().e("applist_select")) == null) {
                getPreferenceScreen().d(this.f5016l);
            }
            if (list == null || list.isEmpty()) {
                getPreferenceScreen().n(this.f5016l);
                return;
            }
        } else {
            if (((COUIPreferenceCategory) getPreferenceScreen().e("applist_donot_sugguest")) == null) {
                getPreferenceScreen().d(this.f5017m);
            }
            if (list == null || list.isEmpty()) {
                getPreferenceScreen().n(this.f5017m);
                return;
            }
        }
        for (int i10 = 0; i10 < list.size(); i10++) {
            AppToShow appToShow = list.get(i10);
            String str = appToShow.f20305b;
            if (str != null) {
                if (z10) {
                    cOUISwitchPreference = (COUISwitchPreference) this.f5016l.e(str);
                } else {
                    cOUISwitchPreference = (COUISwitchPreference) this.f5017m.e(str);
                }
                if (cOUISwitchPreference == null) {
                    cOUISwitchPreference = new COUISwitchPreference(this.f5013i);
                    cOUISwitchPreference.setKey(str);
                }
                cOUISwitchPreference.setIsCustomIconRadius(true);
                cOUISwitchPreference.setOrder(i10);
                cOUISwitchPreference.setChecked(true ^ appToShow.f20308e);
                cOUISwitchPreference.setPersistent(false);
                cOUISwitchPreference.setTitle(appToShow.f20306c);
                if (map != null && !map.isEmpty() && map.get(appToShow.f20305b) != null) {
                    if (map.get(appToShow.f20305b) != null) {
                        cOUISwitchPreference.setIcon(map.get(appToShow.f20305b));
                    }
                } else {
                    cOUISwitchPreference.setIcon(this.f5014j);
                }
                if (this.f5025u == 0) {
                    if (appToShow.f20307d) {
                        cOUISwitchPreference.setSummary(!appToShow.f20308e ? R.string.startup_applist_summary_both : R.string.startup_applist_summary_both_forbid);
                    } else {
                        cOUISwitchPreference.setSummary(!appToShow.f20308e ? R.string.startup_applist_summary_background : R.string.startup_applist_summary_background_forbid);
                    }
                } else if (z10) {
                    if (appToShow.f20308e) {
                        cOUISwitchPreference.setSummary(w0(R.string.associate_startup_recommend_open_summary_prefix, R.string.associate_startup_recommend_open_summary_suffix));
                    } else {
                        cOUISwitchPreference.setSummary(R.string.associate_startup_recommend_open_summary);
                    }
                } else if (!appToShow.f20308e) {
                    cOUISwitchPreference.setSummary(w0(R.string.associate_startup_recommend_close_summary_prefix_device, R.string.associate_startup_recommend_close_summary_suffix));
                } else {
                    cOUISwitchPreference.setSummary(this.f5013i.getString(R.string.associate_startup_recommend_close_summary));
                }
                list.size();
                if (z10) {
                    this.f5016l.d(cOUISwitchPreference);
                } else {
                    this.f5017m.d(cOUISwitchPreference);
                }
                cOUISwitchPreference.setOnPreferenceChangeListener(this.f5030z);
            }
        }
        if (z10 && (list3 = this.f5022r) != null && !list3.isEmpty()) {
            List<String> j10 = this.f5027w.j(this.f5022r, list);
            Iterator<String> it = j10.iterator();
            while (it.hasNext()) {
                COUISwitchPreference cOUISwitchPreference2 = (COUISwitchPreference) this.f5016l.e(it.next());
                if (cOUISwitchPreference2 != null) {
                    this.f5016l.n(cOUISwitchPreference2);
                }
            }
            this.f5027w.y(j10, true);
            this.f5022r.clear();
            this.f5022r.addAll(list);
        }
        if (z10 || (list2 = this.f5023s) == null || list2.isEmpty()) {
            return;
        }
        List<String> j11 = this.f5027w.j(this.f5023s, list);
        Iterator<String> it2 = j11.iterator();
        while (it2.hasNext()) {
            COUISwitchPreference cOUISwitchPreference3 = (COUISwitchPreference) this.f5017m.e(it2.next());
            if (cOUISwitchPreference3 != null) {
                this.f5017m.n(cOUISwitchPreference3);
            }
        }
        this.f5027w.y(j11, false);
        this.f5023s.clear();
        this.f5023s.addAll(list);
    }

    private void initPreference() {
        boolean s7 = this.f5027w.s(true);
        boolean s10 = this.f5027w.s(false);
        if (!s7 || !s10) {
            y0();
        }
        if (s7 && s10) {
            C0();
        } else {
            B0();
        }
        this.f5029y = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SpannableString w0(int i10, int i11) {
        int length = this.f5013i.getString(i10).length();
        SpannableString spannableString = new SpannableString(A0(i10, i11));
        spannableString.setSpan(new ForegroundColorSpan(this.f5013i.getColor(R.color.associate_sugguest_color)), length + 1, spannableString.length(), 33);
        return spannableString;
    }

    private void x0() {
        this.f5015k = (COUIPreferenceCategory) findPreference("launch_record");
        this.f5016l = (COUIPreferenceCategory) findPreference("applist_select");
        this.f5017m = (COUIPreferenceCategory) findPreference("applist_donot_sugguest");
        this.f5019o = findPreference("preference_header");
        this.f5020p = findPreference("preference_bottom");
        getPreferenceScreen().n(this.f5015k);
        getPreferenceScreen().n(this.f5016l);
        getPreferenceScreen().n(this.f5017m);
        getPreferenceScreen().n(this.f5019o);
        getPreferenceScreen().d(this.f5020p);
    }

    private void y0() {
        getPreferenceScreen().d(this.f5015k);
        COUIJumpPreference cOUIJumpPreference = (COUIJumpPreference) findPreference("launch_record_enter");
        cOUIJumpPreference.f(false);
        cOUIJumpPreference.setOnPreferenceClickListener(new h());
    }

    public void E0(int i10, int i11) {
        String str = new String();
        if (i10 == i11) {
            str = this.f5013i.getString(R.string.startup_forbid_dialog_title_device, new Object[]{Integer.valueOf(i11)});
        } else if (i10 > i11) {
            str = this.f5013i.getString(R.string.startup_forbid_dialog_over_title, new Object[]{Integer.valueOf(i11)});
        }
        Log.d("StartupManager", "forbidden dialog allow but not white count :" + i10 + " max allow count :" + i11);
        if (str.length() == 0) {
            return;
        }
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(this.f5013i);
        cOUIAlertDialogBuilder.t(str);
        cOUIAlertDialogBuilder.e0(R.string.startup_forbid_button_ok, null);
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.f5018n = a10;
        WindowManager.LayoutParams attributes = a10.getWindow().getAttributes();
        attributes.setTitle("StartupForbid Dialog");
        this.f5018n.getWindow().setAttributes(attributes);
        this.f5018n.getWindow().setType(2002);
        this.f5018n.show();
    }

    @Override // v9.d
    public String d0() {
        if (this.f5025u == 1) {
            return this.f5013i.getString(R.string.associate_startup_applist_no_select);
        }
        return this.f5013i.getString(R.string.startup_applist_no_select);
    }

    @Override // v9.d
    public String f0() {
        return this.f5013i.getString(R.string.startup_loading);
    }

    @Override // v9.d
    public String getTitle() {
        if (this.f5025u == 0) {
            return this.f5013i.getString(R.string.startup_manager);
        }
        return this.f5013i.getString(R.string.associate_startup_manager);
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f5025u = getArguments().getInt("show_scene");
        getPreferenceScreen().d(this.f5019o);
        StartupViewModel startupViewModel = (StartupViewModel) new h0(this).a(StartupViewModel.class);
        this.f5027w = startupViewModel;
        startupViewModel.r(getActivity().getApplication());
        this.f5027w.x(this.f5025u);
        this.f5027w.w();
        this.f5027w.q().g(getActivity(), new a());
        this.f5027w.n().g(getActivity(), new b());
        this.f5027w.o().g(getActivity(), new c());
        this.f5027w.l().g(getActivity(), new d());
        this.f5027w.p().g(getActivity(), new e());
        this.f5027w.m().g(getActivity(), new f());
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.oplus_permission_startup_app_list);
        FragmentActivity activity = getActivity();
        this.f5013i = activity;
        this.f5014j = IconUtils.b(this.f5013i, activity.getDrawable(R.drawable.file_apk_icon));
        x0();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        v0();
        super.onDestroy();
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.f5021q = getListView();
        D0();
    }

    public void v0() {
        AlertDialog alertDialog = this.f5018n;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public void z0(int i10) {
        StartupViewModel startupViewModel;
        if (i10 == 1) {
            this.f5028x |= 1;
        } else if (i10 == 2) {
            this.f5028x |= 2;
        }
        Log.d("StartupManager", "handleMessage: " + this.f5028x);
        if ((this.f5028x & 3) == 0 || this.f5029y || (startupViewModel = this.f5027w) == null || !startupViewModel.t()) {
            return;
        }
        initPreference();
    }
}
