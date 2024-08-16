package m8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.preference.Preference;
import b6.LocalLog;
import b9.PowerSipper;
import com.coui.appcompat.preference.COUIJumpPreference;
import com.coui.appcompat.preference.COUIPreferenceCategory;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.PowerInspectActivity;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import com.oplus.powermanager.powercurve.graph.SipperListPreference;
import java.lang.ref.WeakReference;
import java.util.List;
import x5.UploadDataUtil;

/* compiled from: PowerRankingFragment.java */
/* renamed from: m8.i, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerRankingFragment extends BasePreferenceFragment {

    /* renamed from: f, reason: collision with root package name */
    private Activity f15060f;

    /* renamed from: g, reason: collision with root package name */
    private Handler f15061g;

    /* renamed from: h, reason: collision with root package name */
    private SipperListPreference f15062h;

    /* renamed from: i, reason: collision with root package name */
    private COUIJumpPreference f15063i;

    /* renamed from: k, reason: collision with root package name */
    private List<PowerSipper> f15065k;

    /* renamed from: p, reason: collision with root package name */
    private int f15070p;

    /* renamed from: e, reason: collision with root package name */
    private Context f15059e = null;

    /* renamed from: j, reason: collision with root package name */
    private boolean f15064j = false;

    /* renamed from: l, reason: collision with root package name */
    private long f15066l = -1;

    /* renamed from: m, reason: collision with root package name */
    private long f15067m = -1;

    /* renamed from: n, reason: collision with root package name */
    private boolean f15068n = false;

    /* renamed from: o, reason: collision with root package name */
    private int f15069o = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PowerRankingFragment.java */
    /* renamed from: m8.i$a */
    /* loaded from: classes2.dex */
    public class a implements Preference.d {
        a() {
        }

        @Override // androidx.preference.Preference.d
        public boolean M(Preference preference) {
            UploadDataUtil.S0(PowerRankingFragment.this.f15059e).g0();
            Intent intent = new Intent(PowerRankingFragment.this.f15059e, (Class<?>) PowerInspectActivity.class);
            intent.setFlags(603979776);
            intent.putExtra("navigate_title_id", R.string.power_usage_details);
            PowerRankingFragment.this.f15059e.startActivity(intent);
            return false;
        }
    }

    /* compiled from: PowerRankingFragment.java */
    /* renamed from: m8.i$b */
    /* loaded from: classes2.dex */
    private static class b extends Handler {

        /* renamed from: a, reason: collision with root package name */
        private WeakReference<PowerRankingFragment> f15072a;

        public b(PowerRankingFragment powerRankingFragment) {
            this.f15072a = new WeakReference<>(powerRankingFragment);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            WeakReference<PowerRankingFragment> weakReference = this.f15072a;
            if (weakReference != null) {
                PowerRankingFragment powerRankingFragment = weakReference.get();
                if (powerRankingFragment == null) {
                    LocalLog.b("PowerRankingFragment", "handleMessage: fragment reference is null");
                    return;
                }
                switch (message.what) {
                    case 101:
                        powerRankingFragment.p0();
                        return;
                    case 102:
                        powerRankingFragment.n0();
                        return;
                    case 103:
                        powerRankingFragment.m0();
                        return;
                    default:
                        return;
                }
            }
            LocalLog.b("PowerRankingFragment", "handleMessage: weak reference is null");
        }
    }

    private void h0() {
        COUIJumpPreference cOUIJumpPreference = (COUIJumpPreference) findPreference("one_key_opt_preference");
        this.f15063i = cOUIJumpPreference;
        if (cOUIJumpPreference != null) {
            cOUIJumpPreference.setOnPreferenceClickListener(new a());
        }
    }

    private void i0() {
        this.f15062h = (SipperListPreference) findPreference("list");
        COUIPreferenceCategory cOUIPreferenceCategory = (COUIPreferenceCategory) getPreferenceScreen().e("power_ranking_list_preference");
        if (this.f15062h != null || cOUIPreferenceCategory == null) {
            return;
        }
        SipperListPreference sipperListPreference = new SipperListPreference(this.f15059e);
        this.f15062h = sipperListPreference;
        sipperListPreference.setKey("list");
        this.f15062h.m(false);
        cOUIPreferenceCategory.d(this.f15062h);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m0() {
        this.f15062h.o();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void n0() {
        if (this.f15059e == null || this.f15063i == null) {
            return;
        }
        LocalLog.a("PowerRankingFragment", "updatePowerIssueText " + this.f15069o);
        if (this.f15069o > 0) {
            Resources resources = this.f15059e.getResources();
            int i10 = this.f15069o;
            this.f15063i.setSummary(resources.getQuantityString(R.plurals.battery_detail_find_optimization, i10, Integer.valueOf(i10)));
            return;
        }
        this.f15063i.setSummary("");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void p0() {
        if (isAdded() && !isDetached()) {
            if (this.f15061g.hasMessages(103)) {
                this.f15061g.removeMessages(103);
            }
            this.f15062h.p(this.f15065k);
            LocalLog.a("PowerRankingFragment", "update fragment, list size " + this.f15065k.size());
            return;
        }
        LocalLog.a("PowerRankingFragment", "updateSipperList: Detached");
    }

    public void c(int i10, int i11) {
        if (i10 == this.f15069o) {
            return;
        }
        this.f15069o = i10;
        this.f15070p = i11;
        Handler handler = this.f15061g;
        if (handler != null) {
            if (handler.hasMessages(102)) {
                this.f15061g.removeMessages(102);
            }
            this.f15061g.sendEmptyMessage(102);
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return getString(R.string.app_power_consumer_title_new);
    }

    public boolean j0(long j10, long j11) {
        return (j10 == this.f15066l && j11 == this.f15067m) ? false : true;
    }

    public void k0(boolean z10) {
        SipperListPreference sipperListPreference = this.f15062h;
        if (sipperListPreference != null) {
            sipperListPreference.m(z10);
        }
    }

    public void l0(long j10, long j11) {
        this.f15066l = j10;
        this.f15067m = j11;
    }

    public void o0(List<PowerSipper> list, boolean z10, boolean z11) {
        if (isAdded() && !isDetached()) {
            this.f15065k = list;
            this.f15068n = z11;
            if (z11) {
                if (isResumed()) {
                    p0();
                    return;
                } else {
                    m0();
                    return;
                }
            }
            if (z10) {
                LocalLog.a("PowerRankingFragment", "updateSipperList: typeSwitching");
                this.f15061g.sendEmptyMessageDelayed(101, 300L);
                return;
            } else {
                p0();
                return;
            }
        }
        LocalLog.a("PowerRankingFragment", "updateSipperList: Detached");
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        LocalLog.a("PowerRankingFragment", "onAttach: ");
        this.f15060f = getActivity();
        this.f15059e = context;
        this.f15061g = new b(this);
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.f15062h.l(configuration);
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        LocalLog.a("PowerRankingFragment", "onCreatePreferences: ");
        addPreferencesFromResource(R.xml.power_ranking_preference);
        h0();
        i0();
        this.f15064j = true;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment, com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        getListView().setNestedScrollingEnabled(false);
        return onCreateView;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        n0();
        k0(true);
        if (!this.f15068n || this.f15065k == null || this.f15062h == null) {
            return;
        }
        if (this.f15061g.hasMessages(101)) {
            this.f15061g.removeMessages(101);
        }
        this.f15061g.sendEmptyMessageDelayed(101, 200L);
        this.f15068n = false;
        LocalLog.a("PowerRankingFragment", "onResume: update list msg");
    }
}
