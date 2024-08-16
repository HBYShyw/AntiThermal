package m8;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.ArrayMap;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import b6.LocalLog;
import com.coui.appcompat.button.COUIButton;
import com.coui.appcompat.preference.COUIPreference;
import com.oplus.anim.EffectiveAnimationView;
import com.oplus.battery.R;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import com.oplus.powermanager.fuelgaue.base.ViewGroupUtil;
import com.oplus.powermanager.powerusage.view.OneKeyTitlePreference;
import com.oplus.powermanager.powerusage.view.PowerCheckboxPreference;
import h8.IPowerInspectPresenter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import k8.PowerInspectPresenter;
import l8.IPowerInspectUpdate;
import t8.BatteryRemainTimeCalculator;
import t8.PowerIssueKey;
import t8.PowerUsageManager;
import u8.AbnormalAppsPowerIssue;
import u8.ApplicationsPowerIssue;
import u8.BasicPowerIssue;
import w8.OneKeyAnimation;

/* compiled from: PowerInspectFragment.java */
/* renamed from: m8.h, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerInspectFragment extends BasePreferenceFragment implements IPowerInspectUpdate {
    private int E;
    private OneKeyAnimation F;

    /* renamed from: o, reason: collision with root package name */
    private ListView f15046o;

    /* renamed from: p, reason: collision with root package name */
    private View f15047p;

    /* renamed from: q, reason: collision with root package name */
    private COUIButton f15048q;

    /* renamed from: r, reason: collision with root package name */
    private TextView f15049r;

    /* renamed from: s, reason: collision with root package name */
    private TextView f15050s;

    /* renamed from: y, reason: collision with root package name */
    private OneKeyTitlePreference f15056y;

    /* renamed from: e, reason: collision with root package name */
    private final String f15036e = "PowerInspectFragment";

    /* renamed from: f, reason: collision with root package name */
    private final String f15037f = "from_notification";

    /* renamed from: g, reason: collision with root package name */
    private final String f15038g = "_text";

    /* renamed from: h, reason: collision with root package name */
    private final int f15039h = 1;

    /* renamed from: i, reason: collision with root package name */
    private final int f15040i = 2;

    /* renamed from: j, reason: collision with root package name */
    private final int f15041j = 3;

    /* renamed from: k, reason: collision with root package name */
    private long f15042k = 0;

    /* renamed from: l, reason: collision with root package name */
    private boolean f15043l = false;

    /* renamed from: m, reason: collision with root package name */
    private boolean f15044m = false;

    /* renamed from: n, reason: collision with root package name */
    private boolean f15045n = true;

    /* renamed from: t, reason: collision with root package name */
    private ViewGroup f15051t = null;

    /* renamed from: u, reason: collision with root package name */
    private final ArrayMap<String, Preference> f15052u = new ArrayMap<>();

    /* renamed from: v, reason: collision with root package name */
    private final ArrayMap<String, PowerCheckboxPreference> f15053v = new ArrayMap<>();

    /* renamed from: w, reason: collision with root package name */
    private final ArrayMap<String, OneKeyTitlePreference> f15054w = new ArrayMap<>();

    /* renamed from: x, reason: collision with root package name */
    private OneKeyTitlePreference f15055x = null;

    /* renamed from: z, reason: collision with root package name */
    private final Map<String, Boolean> f15057z = new HashMap();
    private Context A = null;
    private IPowerInspectPresenter B = null;
    private Bundle C = null;
    private int D = 0;
    private boolean G = false;

    /* compiled from: PowerInspectFragment.java */
    /* renamed from: m8.h$a */
    /* loaded from: classes2.dex */
    class a implements View.OnTouchListener {
        a() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return PowerInspectFragment.this.G();
        }
    }

    private void e0(PreferenceCategory preferenceCategory, BasicPowerIssue basicPowerIssue, int i10, boolean z10) {
        try {
            String str = basicPowerIssue.e() + "_switch";
            PowerCheckboxPreference powerCheckboxPreference = this.f15053v.get(str);
            if (powerCheckboxPreference == null) {
                if (basicPowerIssue.e().equals(PowerIssueKey.APP.b())) {
                    powerCheckboxPreference = new PowerCheckboxPreference(this.A, true, basicPowerIssue.d(), z10, this);
                } else {
                    powerCheckboxPreference = new PowerCheckboxPreference(this.A, false, basicPowerIssue.d(), z10, this);
                }
                powerCheckboxPreference.setKey(str);
                powerCheckboxPreference.p(this.A.getString(basicPowerIssue.h()));
                powerCheckboxPreference.m(this.B, basicPowerIssue);
                this.f15053v.put(str, powerCheckboxPreference);
            }
            if (basicPowerIssue.f() > 0) {
                LocalLog.a("PowerInspectFragment", "show sec" + basicPowerIssue.e());
                powerCheckboxPreference.o(BatteryRemainTimeCalculator.d(this.A, basicPowerIssue.f()));
            } else {
                LocalLog.a("PowerInspectFragment", "not show sec" + basicPowerIssue.e());
                powerCheckboxPreference.o("");
            }
            powerCheckboxPreference.setOrder(i10);
            if (basicPowerIssue.e().equals(PowerIssueKey.APP.b())) {
                powerCheckboxPreference.q(this.A, ((ApplicationsPowerIssue) basicPowerIssue).r());
            } else if (basicPowerIssue.e().equals(PowerIssueKey.ABNORMAL_APP.b())) {
                powerCheckboxPreference.q(this.A, this.B.Q(((AbnormalAppsPowerIssue) basicPowerIssue).r()));
            }
            powerCheckboxPreference.l();
            if (this.C != null) {
                LocalLog.a("PowerInspectFragment", "mSavedInstanceState");
                boolean z11 = this.C.getBoolean(str, basicPowerIssue.g() == 1);
                powerCheckboxPreference.n(z11);
                this.f15057z.put(str, Boolean.valueOf(z11));
                basicPowerIssue.p(z11);
            } else if (basicPowerIssue.g() == 2) {
                powerCheckboxPreference.n(false);
                this.f15057z.put(str, Boolean.FALSE);
            } else if (basicPowerIssue.g() == 1) {
                powerCheckboxPreference.n(true);
                this.f15057z.put(str, Boolean.TRUE);
            }
            preferenceCategory.d(powerCheckboxPreference);
        } catch (Exception e10) {
            LocalLog.b("PowerInspectFragment", "generateCheckboxPreferenceCategory error! " + e10);
        }
    }

    private void f0(PreferenceCategory preferenceCategory, BasicPowerIssue basicPowerIssue, int i10) {
        try {
            String str = basicPowerIssue.e() + "_text";
            Preference preference = this.f15052u.get(str);
            if (preference == null) {
                preference = new COUIPreference(this.A);
                preference.setSelectable(false);
                preference.setKey(str);
                preference.setTitle(this.A.getString(basicPowerIssue.h()));
                preference.setWidgetLayoutResource(R.layout.one_key_textview);
                this.f15052u.put(str, preference);
            }
            preference.setOrder(i10);
            preferenceCategory.d(preference);
        } catch (Exception unused) {
            LocalLog.b("PowerInspectFragment", "generateTextPreferenceCategory error!");
        }
    }

    private OneKeyTitlePreference g0(String str, boolean z10) {
        OneKeyTitlePreference oneKeyTitlePreference = this.f15054w.get(str);
        if (oneKeyTitlePreference == null) {
            oneKeyTitlePreference = new OneKeyTitlePreference(this.A, str, z10, this);
            oneKeyTitlePreference.setTitle(str);
            oneKeyTitlePreference.setKey(str);
            this.f15054w.put(str, oneKeyTitlePreference);
        } else {
            oneKeyTitlePreference.t(z10);
            if (oneKeyTitlePreference.i() > 0) {
                oneKeyTitlePreference.m();
            }
        }
        LocalLog.a("PowerInspectFragment", "generateTipPreferenceCategory summary=" + str + " showSelectTitle=" + z10);
        return oneKeyTitlePreference;
    }

    private int h0() {
        int i10 = 0;
        for (Map.Entry<String, Boolean> entry : this.f15057z.entrySet()) {
            if (entry.getValue().booleanValue()) {
                i10++;
                LocalLog.a("PowerInspectFragment", "checked " + entry.getKey());
            }
        }
        return i10;
    }

    private void k0(int i10) {
        this.f15048q.setEnabled(i10 > 0);
        this.f15048q.setText(R.string.one_key_power_save);
    }

    private void l0(int i10, int i11) {
        if (i10 > 0) {
            String quantityString = this.A.getResources().getQuantityString(R.plurals.battery_detail_view_optimization, i10, Integer.valueOf(i10));
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(quantityString);
            quantityString.length();
            Context context = this.A;
            Object[] objArr = new Object[1];
            if (!this.f15044m) {
                i10 = 0;
            }
            objArr[0] = Integer.valueOf(i10);
            String string = context.getString(R.string.one_key_issues_num, objArr);
            int indexOf = quantityString.indexOf(string);
            if (indexOf != -1) {
                spannableStringBuilder.setSpan(new ForegroundColorSpan(this.A.getColor(R.color.coui_color_primary_red)), indexOf, string.length() + indexOf, 34);
            }
            this.f15050s.setText(spannableStringBuilder);
            if (this.D > 0) {
                OneKeyAnimation oneKeyAnimation = this.F;
                if (oneKeyAnimation != null) {
                    oneKeyAnimation.r(EventType.SCENE_MODE_AUDIO_OUT);
                }
                this.f15050s.setVisibility(4);
            } else {
                OneKeyAnimation oneKeyAnimation2 = this.F;
                if (oneKeyAnimation2 != null) {
                    oneKeyAnimation2.r(EventType.SCENE_MODE_LOCATION);
                }
                this.f15050s.setVisibility(0);
            }
        } else {
            OneKeyAnimation oneKeyAnimation3 = this.F;
            if (oneKeyAnimation3 != null) {
                oneKeyAnimation3.r(EventType.SCENE_MODE_AUDIO_OUT);
            }
            this.f15050s.setVisibility(4);
        }
        m0(i11);
    }

    private void m0(int i10) {
        boolean z10 = true;
        if (this.f15042k > 0) {
            this.f15049r.setText(this.A.getResources().getString(R.string.one_key_can_extend_time) + " " + BatteryRemainTimeCalculator.d(this.A, this.f15042k));
            this.f15049r.setVisibility(0);
            this.D = 0;
        } else {
            LocalLog.a("PowerInspectFragment", "mOpBatteryTime=" + this.f15042k);
            if (i10 > 0) {
                this.f15049r.setText(this.A.getResources().getQuantityString(R.plurals.one_key_select_optimization, i10, Integer.valueOf(i10)));
                this.f15049r.setVisibility(0);
                this.D = 0;
            } else {
                if (!this.f15044m) {
                    this.f15049r.setText(R.string.one_key_finished_all_optimization);
                    this.f15049r.setVisibility(0);
                    this.D = 0;
                    this.f15056y.u(z10);
                }
                if (this.D > 0) {
                    LocalLog.a("PowerInspectFragment", "show mOptimizedCount=" + this.D);
                    Resources resources = this.A.getResources();
                    int i11 = this.D;
                    this.f15049r.setText(resources.getQuantityString(R.plurals.battery_detail_finished_optimization, i11, Integer.valueOf(i11)));
                    this.f15049r.setVisibility(0);
                } else {
                    this.f15049r.setVisibility(8);
                }
            }
        }
        z10 = false;
        this.f15056y.u(z10);
    }

    @Override // l8.IPowerInspectUpdate
    public void D(ArrayList<BasicPowerIssue> arrayList) {
        this.D = arrayList.size();
        LocalLog.a("PowerInspectFragment", "completePowerInspect mOptimizedCount=" + this.D + "");
        Iterator<BasicPowerIssue> it = arrayList.iterator();
        while (it.hasNext()) {
            BasicPowerIssue next = it.next();
            this.f15057z.put(next.e() + "_switch", Boolean.FALSE);
        }
    }

    @Override // l8.IPowerInspectUpdate
    public boolean G() {
        return this.G;
    }

    @Override // l8.IPowerInspectUpdate
    public void H(boolean z10) {
        for (PowerCheckboxPreference powerCheckboxPreference : this.f15053v.values()) {
            if (powerCheckboxPreference == null) {
                LocalLog.b("PowerInspectFragment", "selectAllPreference has null preference");
            } else if (z10 != powerCheckboxPreference.i() && !powerCheckboxPreference.j()) {
                powerCheckboxPreference.onClick(new View(this.A));
            }
        }
    }

    @Override // l8.IPowerInspectUpdate
    public void J(int i10) {
        this.D = i10;
    }

    @Override // l8.IPowerInspectUpdate
    public void K() {
        this.F.r(EventType.SCENE_MODE_AUDIO_OUT);
        this.f15048q.setEnabled(false);
        this.G = true;
    }

    @Override // androidx.fragment.app.Fragment
    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(str, fileDescriptor, printWriter, strArr);
        if (strArr == null || strArr.length <= 0) {
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        for (String str2 : strArr) {
            sb2.append(str2);
            sb2.append(" ");
        }
        LocalLog.l("PowerInspectFragment", "Battery debug:" + ((Object) sb2));
        if ("getPowerInspectList".equals(strArr[0])) {
            ArrayList<BasicPowerIssue> arrayList = new ArrayList<>();
            ArrayList<BasicPowerIssue> arrayList2 = new ArrayList<>();
            PowerUsageManager.x(this.A).y(arrayList, arrayList2);
            printWriter.print("resolved: [");
            Iterator<BasicPowerIssue> it = arrayList.iterator();
            while (it.hasNext()) {
                printWriter.print(it.next().e() + ", ");
            }
            printWriter.print("]\nunResolved: [");
            Iterator<BasicPowerIssue> it2 = arrayList2.iterator();
            while (it2.hasNext()) {
                printWriter.print(it2.next().e() + ", ");
            }
            printWriter.print("]\n");
            printWriter.println("cmd done.");
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return this.A.getString(R.string.battery_ui_power_optimization);
    }

    @Override // l8.IPowerInspectUpdate
    public void i(long j10, String str, boolean z10) {
        PowerCheckboxPreference powerCheckboxPreference = this.f15053v.get(str);
        if (powerCheckboxPreference != null) {
            powerCheckboxPreference.n(!z10);
        }
        this.f15057z.put(str, Boolean.valueOf(!z10));
        this.f15042k += j10;
        this.f15050s.setVisibility(0);
        int h02 = h0();
        k0(h02);
        OneKeyAnimation oneKeyAnimation = this.F;
        if (oneKeyAnimation != null) {
            oneKeyAnimation.r(EventType.SCENE_MODE_LOCATION);
        }
        m0(h02);
        OneKeyTitlePreference oneKeyTitlePreference = this.f15055x;
        if (oneKeyTitlePreference != null) {
            oneKeyTitlePreference.v(this.E != h02);
        }
    }

    /* renamed from: j0, reason: merged with bridge method [inline-methods] */
    public void i0(ArrayList<BasicPowerIssue> arrayList, ArrayList<BasicPowerIssue> arrayList2) {
        if (this.A == null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("updateActivityAfterCheck: mContext ");
            this.A = null;
            sb2.append((Object) null);
            sb2.append(" activity ");
            sb2.append(getActivity() == null);
            LocalLog.b("PowerInspectFragment", sb2.toString());
            return;
        }
        if (this.f15051t.getVisibility() == 0) {
            ViewGroupUtil.setVisibilityWithChild(this.f15051t, 8);
        }
        if (getPreferenceScreen() == null) {
            LocalLog.l("PowerInspectFragment", "updateActivityAfterCheck got error! can not get preference screen");
            return;
        }
        this.f15042k = 0L;
        this.f15044m = false;
        this.E = 0;
        this.f15057z.clear();
        getPreferenceScreen().m();
        OneKeyTitlePreference g02 = g0(this.A.getString(R.string.one_key_unresolved_advice), true);
        g02.setOrder(1);
        if (arrayList2.size() > 0) {
            LocalLog.a("PowerInspectFragment", "addPreference categoryOne");
            getPreferenceScreen().d(g02);
            this.f15044m = true;
            this.E = arrayList2.size();
            for (int i10 = 0; i10 < arrayList2.size(); i10++) {
                BasicPowerIssue basicPowerIssue = arrayList2.get(i10);
                e0(g02, basicPowerIssue, i10, false);
                if (basicPowerIssue.g() == 1) {
                    this.f15042k += basicPowerIssue.f();
                }
            }
        }
        this.f15055x = g02;
        if (this.C != null) {
            this.C = null;
        }
        if (arrayList.size() > 0) {
            OneKeyTitlePreference g03 = g0(this.A.getString(R.string.one_key_resolved_issues), false);
            g03.setOrder(3);
            this.f15056y = g03;
            getPreferenceScreen().d(g03);
            for (int i11 = 0; i11 < arrayList.size(); i11++) {
                f0(g03, arrayList.get(i11), i11);
                this.f15057z.put(arrayList.get(i11).e() + "_switch", Boolean.FALSE);
            }
        }
        int h02 = h0();
        k0(h02);
        l0(this.E, h02);
        this.f15055x.v(h02 < this.E);
        if (this.f15046o.getVisibility() != 0) {
            this.f15046o.setVisibility(0);
        }
        this.f15045n = false;
        this.G = false;
    }

    @Override // l8.IPowerInspectUpdate
    public boolean l() {
        return h0() >= this.E;
    }

    @Override // androidx.fragment.app.Fragment
    @SuppressLint({"ClickableViewAccessibility"})
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.f15051t = (ViewGroup) getActivity().findViewById(R.id.loading_view_layout);
        ListView listView = (ListView) getActivity().findViewById(android.R.id.list);
        this.f15046o = listView;
        listView.setDivider(null);
        this.f15046o.setSelector(new ColorDrawable(0));
        View findViewById = getActivity().findViewById(R.id.one_key_main_header);
        this.f15047p = findViewById;
        this.f15049r = (TextView) findViewById.findViewById(R.id.one_key_save_status);
        this.f15050s = (TextView) this.f15047p.findViewById(R.id.one_key_save_issues_num);
        this.f15043l = getActivity().getIntent().getBooleanExtra("from_notification", false);
        COUIButton cOUIButton = (COUIButton) getActivity().findViewById(R.id.power_use_operation_menu);
        this.f15048q = cOUIButton;
        cOUIButton.setOnClickListener(this.B);
        getListView().setOnTouchListener(new a());
        getListView().setMotionEventSplittingEnabled(false);
        getListView().setItemViewCacheSize(15);
        getListView().setDrawingCacheEnabled(true);
        getListView().setDrawingCacheQuality(1048576);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.A = context;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        PowerInspectPresenter powerInspectPresenter = new PowerInspectPresenter(this);
        this.B = powerInspectPresenter;
        powerInspectPresenter.onCreate(bundle);
        OneKeyAnimation oneKeyAnimation = new OneKeyAnimation();
        this.F = oneKeyAnimation;
        oneKeyAnimation.q(this.B);
        if (bundle != null) {
            this.C = bundle;
            this.D = bundle.getInt("mOptimizedCount", 0);
            LocalLog.a("PowerInspectFragment", "onCreate: mOptimizedCount=" + this.D);
        }
        super.onCreate(bundle);
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.pm_poweruse_check_preference);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        this.B.onDestroy();
        super.onDestroy();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        this.F.n();
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (!this.f15045n) {
            LocalLog.a("PowerInspectFragment", "refreshIssues");
            this.B.V();
        }
        LocalLog.a("PowerInspectFragment", "onResume: PreferenceCategories size=" + this.f15054w.size());
        EffectiveAnimationView effectiveAnimationView = (EffectiveAnimationView) getActivity().findViewById(R.id.flash_animation_initial);
        EffectiveAnimationView effectiveAnimationView2 = (EffectiveAnimationView) getActivity().findViewById(R.id.flash_animation_optimizing);
        EffectiveAnimationView effectiveAnimationView3 = (EffectiveAnimationView) getActivity().findViewById(R.id.flash_animation_finished);
        EffectiveAnimationView effectiveAnimationView4 = (EffectiveAnimationView) getActivity().findViewById(R.id.flash_animation_reset);
        this.F.p(getActivity());
        this.F.m(effectiveAnimationView, effectiveAnimationView2, effectiveAnimationView3, effectiveAnimationView4);
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        LocalLog.a("PowerInspectFragment", "onSaveInstanceState mOptimizedCount=" + this.D);
        for (Map.Entry<String, Boolean> entry : this.f15057z.entrySet()) {
            bundle.putBoolean(entry.getKey(), entry.getValue().booleanValue());
        }
        bundle.putInt("mOptimizedCount", this.D);
        super.onSaveInstanceState(bundle);
    }

    @Override // l8.IPowerInspectUpdate
    public void y(final ArrayList<BasicPowerIssue> arrayList, final ArrayList<BasicPowerIssue> arrayList2) {
        if (getActivity() == null) {
            LocalLog.b("PowerInspectFragment", "updateActivityAfterCheckFromNonUI: activity is null");
        } else {
            getActivity().runOnUiThread(new Runnable() { // from class: m8.g
                @Override // java.lang.Runnable
                public final void run() {
                    PowerInspectFragment.this.i0(arrayList, arrayList2);
                }
            });
        }
    }
}
