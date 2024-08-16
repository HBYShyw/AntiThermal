package ca;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.ArrayMap;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.h0;
import androidx.preference.Preference;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.preference.COUIJumpPreference;
import com.coui.appcompat.preference.COUIPreferenceCategory;
import com.coui.appcompat.preference.COUISwitchPreference;
import com.oplus.anim.EffectiveAnimationView;
import com.oplus.battery.R;
import da.PreventRecordViewModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import v9.d;
import w1.COUIDarkModeUtil;
import x9.IconUtils;
import z9.Record;

/* compiled from: PreventRecordFragment.java */
/* renamed from: ca.a, reason: use source file name */
/* loaded from: classes2.dex */
public class PreventRecordFragment extends d {

    /* renamed from: i, reason: collision with root package name */
    private int f5000i;

    /* renamed from: j, reason: collision with root package name */
    private Activity f5001j;

    /* renamed from: k, reason: collision with root package name */
    private Drawable f5002k;

    /* renamed from: l, reason: collision with root package name */
    private PreventRecordViewModel f5003l;

    /* renamed from: m, reason: collision with root package name */
    private RecyclerView f5004m;

    /* renamed from: n, reason: collision with root package name */
    private EffectiveAnimationView f5005n;

    /* renamed from: o, reason: collision with root package name */
    private COUIPreferenceCategory f5006o;

    /* renamed from: p, reason: collision with root package name */
    private COUIPreferenceCategory f5007p;

    /* renamed from: q, reason: collision with root package name */
    private Preference f5008q;

    /* renamed from: r, reason: collision with root package name */
    private List<Record> f5009r = new ArrayList();

    /* renamed from: s, reason: collision with root package name */
    private Map<String, List<String>> f5010s = new ArrayMap();

    /* compiled from: PreventRecordFragment.java */
    /* renamed from: ca.a$a */
    /* loaded from: classes2.dex */
    class a implements Observer<List<Record>> {
        a() {
        }

        @Override // androidx.lifecycle.Observer
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(List<Record> list) {
            PreventRecordFragment.this.s0(list);
            if (PreventRecordFragment.this.f5009r.isEmpty()) {
                PreventRecordFragment.this.f5009r.addAll(list);
            }
            PreventRecordFragment.this.f5003l.i(list);
            ArrayMap arrayMap = new ArrayMap();
            arrayMap.putAll(PreventRecordFragment.this.f5003l.h(list));
            PreventRecordFragment.this.u0(list, arrayMap);
        }
    }

    /* compiled from: PreventRecordFragment.java */
    /* renamed from: ca.a$b */
    /* loaded from: classes2.dex */
    class b implements Observer<Map<String, Drawable>> {
        b() {
        }

        @Override // androidx.lifecycle.Observer
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(Map<String, Drawable> map) {
            PreventRecordFragment.this.t0(map);
            PreventRecordFragment.this.initPreference();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPreference() {
        getPreferenceScreen().d(this.f5008q);
        if (!this.f5003l.o() && this.f5003l.n()) {
            getPreferenceScreen().d(this.f5006o);
            p0();
        } else {
            q0();
        }
    }

    private String n0(Record record) {
        StringBuilder sb2 = new StringBuilder(50);
        sb2.append(record.f20325c);
        sb2.append(record.f20324b);
        sb2.append(record.f20328f);
        sb2.append(record.f20329g);
        sb2.append(record.f20334l);
        return sb2.toString();
    }

    private void o0() {
        this.f5006o = (COUIPreferenceCategory) findPreference("record_item");
        this.f5007p = (COUIPreferenceCategory) findPreference("record_tile");
        this.f5008q = findPreference("preference_header");
        getPreferenceScreen().n(this.f5006o);
        getPreferenceScreen().n(this.f5008q);
    }

    private void p0() {
        this.f5005n.setVisibility(8);
        this.f5005n.t();
        e0().setVisibility(8);
        g0().setVisibility(8);
        this.f5004m.setVisibility(0);
    }

    private void q0() {
        this.f5005n.setVisibility(0);
        if (COUIDarkModeUtil.a(this.f5001j)) {
            this.f5005n.setAnimation(R.raw.startup_empty_dark);
        } else {
            this.f5005n.setAnimation(R.raw.startup_empty_light);
        }
        this.f5005n.u();
        e0().setVisibility(0);
        g0().setVisibility(8);
        this.f5004m.setVisibility(8);
    }

    private void r0() {
        this.f5005n.setVisibility(8);
        this.f5005n.t();
        e0().setVisibility(8);
        g0().setVisibility(0);
        this.f5004m.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s0(List<Record> list) {
        if (list.isEmpty()) {
            return;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Record record : list) {
            String str = record.f20325c + record.f20324b + record.f20334l;
            if (linkedHashMap.get(str) == null) {
                linkedHashMap.put(str, record);
            } else {
                ((Record) linkedHashMap.get(str)).f20330h += record.f20330h;
            }
        }
        list.clear();
        list.addAll(linkedHashMap.values());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void t0(Map<String, Drawable> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        for (String str : map.keySet()) {
            List<String> list = this.f5010s.get(str);
            if (list != null) {
                Iterator<String> it = list.iterator();
                while (it.hasNext()) {
                    COUIJumpPreference cOUIJumpPreference = (COUIJumpPreference) this.f5006o.e(it.next());
                    if (cOUIJumpPreference != null) {
                        cOUIJumpPreference.setIsCustomIconRadius(true);
                        cOUIJumpPreference.setIcon(map.get(str));
                        this.f5006o.d(cOUIJumpPreference);
                    }
                }
            }
        }
    }

    @Override // v9.d
    public String d0() {
        return this.f5001j.getString(R.string.no_launch_record);
    }

    @Override // v9.d
    public String f0() {
        return this.f5001j.getString(R.string.startup_loading);
    }

    @Override // v9.d
    public String getTitle() {
        if (this.f5000i == 0) {
            this.f5007p.setTitle(this.f5001j.getString(R.string.launch_record_category_auto_title_device));
            return this.f5001j.getString(R.string.launch_record_enter);
        }
        this.f5007p.setTitle(this.f5001j.getString(R.string.launch_record_category_associate_title_device));
        return this.f5001j.getString(R.string.launch_record_enter);
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        PreventRecordViewModel preventRecordViewModel = (PreventRecordViewModel) new h0.a(getActivity().getApplication()).a(PreventRecordViewModel.class);
        this.f5003l = preventRecordViewModel;
        preventRecordViewModel.q(this.f5000i);
        this.f5003l.m().g(this, new a());
        this.f5003l.l().g(this, new b());
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.launch_record_item);
        FragmentActivity activity = getActivity();
        this.f5001j = activity;
        Intent intent = activity.getIntent();
        if (intent != null) {
            this.f5000i = intent.getIntExtra("show_scene", 0);
        }
        this.f5002k = IconUtils.b(this.f5001j, this.f5001j.getDrawable(R.drawable.file_apk_icon));
        o0();
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.f5005n = (EffectiveAnimationView) view.findViewById(R.id.icon_second);
        this.f5004m = getListView();
        r0();
    }

    public void u0(List<Record> list, Map<String, Drawable> map) {
        if (list.isEmpty()) {
            q0();
            return;
        }
        for (Record record : list) {
            String str = record.f20324b;
            if (str != null) {
                List<String> list2 = this.f5010s.get(str);
                if (list2 == null) {
                    list2 = new ArrayList<>();
                }
                String n02 = n0(record);
                if (!list2.contains(n02)) {
                    list2.add(n02);
                    this.f5010s.put(record.f20324b, list2);
                }
                COUIJumpPreference cOUIJumpPreference = (COUIJumpPreference) this.f5006o.e(n02);
                if (cOUIJumpPreference == null) {
                    cOUIJumpPreference = new COUIJumpPreference(this.f5001j);
                    cOUIJumpPreference.setKey(n02);
                    cOUIJumpPreference.e(null);
                }
                cOUIJumpPreference.setIsCustomIconRadius(true);
                int i10 = this.f5000i;
                if (i10 == 0) {
                    if ("0".equals(record.f20328f)) {
                        cOUIJumpPreference.setTitle(this.f5001j.getString(R.string.auto_launch_app_item_title, new Object[]{record.f20326d}));
                    } else if ("2".equals(record.f20328f)) {
                        cOUIJumpPreference.setTitle(this.f5001j.getString(R.string.auto_launch_activity_item_title, new Object[]{record.f20326d}));
                    }
                } else if (1 == i10) {
                    cOUIJumpPreference.setTitle(this.f5001j.getString(R.string.launch_record_item_title, new Object[]{record.f20326d, record.f20327e}));
                }
                if (map != null && map.get(record.f20324b) != null) {
                    cOUIJumpPreference.setIcon(map.get(record.f20324b));
                } else {
                    cOUIJumpPreference.setIcon(this.f5002k);
                }
                Activity activity = this.f5001j;
                long j10 = record.f20333k;
                cOUIJumpPreference.setSummary(this.f5001j.getString(R.string.launch_record_item_summary, new Object[]{String.valueOf(record.f20330h), DateUtils.formatDateRange(activity, j10, j10, 17)}));
                this.f5006o.d(cOUIJumpPreference);
            }
        }
        List<Record> list3 = this.f5009r;
        if (list3 == null || list3.isEmpty()) {
            return;
        }
        List<String> j11 = this.f5003l.j(this.f5009r, list);
        Iterator<String> it = j11.iterator();
        while (it.hasNext()) {
            COUISwitchPreference cOUISwitchPreference = (COUISwitchPreference) this.f5006o.e(it.next());
            if (cOUISwitchPreference != null) {
                this.f5006o.n(cOUISwitchPreference);
            }
        }
        this.f5003l.r(j11);
        this.f5009r.clear();
        this.f5009r.addAll(list);
    }
}
