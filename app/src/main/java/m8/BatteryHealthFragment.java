package m8;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.preference.Preference;
import androidx.recyclerview.widget.RecyclerView;
import b6.LocalLog;
import com.coui.appcompat.card.COUICardInstructionPreference;
import com.coui.appcompat.grid.COUIPercentWidthRecyclerView;
import com.coui.appcompat.preference.COUIPreferenceCategory;
import com.coui.appcompat.preference.COUISwitchPreference;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.BatteryHealthDataPreference;
import com.oplus.powermanager.fuelgaue.BatteryHealthWarningPreference;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import f6.ChargeUtil;
import h8.IBatteryHealthPresenter;
import java.util.ArrayList;
import k8.BatteryHealthPresenter;
import l8.IBatteryHealthUpdate;
import x5.UploadDataUtil;
import y5.AppFeature;

/* compiled from: BatteryHealthFragment.java */
/* renamed from: m8.a, reason: use source file name */
/* loaded from: classes2.dex */
public class BatteryHealthFragment extends BasePreferenceFragment implements Preference.c, IBatteryHealthUpdate {

    /* renamed from: l, reason: collision with root package name */
    private COUIPreferenceCategory f14939l;

    /* renamed from: e, reason: collision with root package name */
    private Context f14932e = null;

    /* renamed from: f, reason: collision with root package name */
    private IBatteryHealthPresenter f14933f = null;

    /* renamed from: g, reason: collision with root package name */
    private COUICardInstructionPreference f14934g = null;

    /* renamed from: h, reason: collision with root package name */
    private BatteryHealthWarningPreference f14935h = null;

    /* renamed from: i, reason: collision with root package name */
    private BatteryHealthDataPreference f14936i = null;

    /* renamed from: j, reason: collision with root package name */
    private COUISwitchPreference f14937j = null;

    /* renamed from: k, reason: collision with root package name */
    private COUISwitchPreference f14938k = null;

    /* renamed from: m, reason: collision with root package name */
    private b f14940m = new b();

    /* compiled from: BatteryHealthFragment.java */
    @SuppressLint({"RestrictedApi"})
    /* renamed from: m8.a$b */
    /* loaded from: classes2.dex */
    private class b extends RecyclerView.o {
        private b() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.o
        public void e(Rect rect, View view, RecyclerView recyclerView, RecyclerView.z zVar) {
            if (BatteryHealthFragment.this.mAdapter.getItem(recyclerView.getChildAdapterPosition(view)) instanceof COUICardInstructionPreference) {
                rect.left = BatteryHealthFragment.this.f14932e.getResources().getDimensionPixelSize(R.dimen.coui_component_card_entrance_large_horizontal_margin);
                rect.right = BatteryHealthFragment.this.f14932e.getResources().getDimensionPixelSize(R.dimen.coui_component_card_entrance_large_horizontal_margin);
            }
        }
    }

    private void e0() {
        this.f14935h = (BatteryHealthWarningPreference) findPreference("waring");
        this.f14936i = (BatteryHealthDataPreference) findPreference("data");
        this.f14934g = (COUICardInstructionPreference) findPreference("life");
        this.f14939l = (COUIPreferenceCategory) findPreference("life_group");
        if (this.f14937j == null) {
            this.f14937j = (COUISwitchPreference) findPreference("smart_charge_protection_switch_in_health");
        }
        if (this.f14938k == null) {
            this.f14938k = (COUISwitchPreference) findPreference("regular_charge_protection_switch_in_health");
        }
        int n10 = ChargeUtil.n(this.f14932e);
        BatteryHealthWarningPreference batteryHealthWarningPreference = this.f14935h;
        if (batteryHealthWarningPreference != null) {
            batteryHealthWarningPreference.setVisible(n10 < 80);
        }
        if (this.f14934g != null) {
            if (AppFeature.j() && AppFeature.k() == 1) {
                int i10 = R.drawable.ic_battery_life_mode_normal;
                if (f6.f.g1(this.f14932e)) {
                    i10 = AppFeature.m() ? R.drawable.ic_battery_life_mode_pad : R.drawable.ic_battery_life_mode_fold;
                }
                Integer[] numArr = {Integer.valueOf(i10)};
                ArrayList arrayList = new ArrayList();
                arrayList.add(new t1.k(numArr, this.f14932e.getString(R.string.charge_health_optimization), this.f14932e.getString(R.string.battery_life_mode_notificate_details)));
                this.f14934g.l(arrayList);
            } else {
                this.f14939l.setVisible(false);
            }
        }
        COUISwitchPreference cOUISwitchPreference = this.f14937j;
        if (cOUISwitchPreference != null) {
            cOUISwitchPreference.setVisible(this.f14932e.getUserId() == 0);
            this.f14937j.setOnPreferenceChangeListener(this);
            this.f14937j.setChecked(f6.f.I0(this.f14932e) == 1);
        }
        COUISwitchPreference cOUISwitchPreference2 = this.f14938k;
        if (cOUISwitchPreference2 != null) {
            cOUISwitchPreference2.setVisible(this.f14932e.getUserId() == 0 && AppFeature.D());
            this.f14938k.setOnPreferenceChangeListener(this);
            this.f14938k.setChecked(f6.f.n0(this.f14932e) == 1);
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return getString(R.string.battery_health_title);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        LocalLog.a("BatteryHealthFragment", "onAttach");
        super.onAttach(context);
        this.f14932e = context;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        BatteryHealthPresenter batteryHealthPresenter = new BatteryHealthPresenter(this);
        this.f14933f = batteryHealthPresenter;
        batteryHealthPresenter.a();
        super.onCreate(bundle);
        e0();
        Context context = this.f14932e;
        f6.f.S1(context, f6.f.p(context) + 1);
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.battery_health_preference);
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment, com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        LocalLog.a("BatteryHealthFragment", "onDetach");
        this.f14933f.onDetach();
        super.onDetach();
    }

    /* JADX WARN: Type inference failed for: r4v1 */
    /* JADX WARN: Type inference failed for: r4v2, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r4v5 */
    @Override // androidx.preference.Preference.c
    public boolean onPreferenceChange(Preference preference, Object obj) {
        ?? booleanValue = preference instanceof COUISwitchPreference ? ((Boolean) obj).booleanValue() : 0;
        String key = preference.getKey();
        if (key == null) {
            return false;
        }
        if (key.equals("smart_charge_protection_switch_in_health")) {
            if (booleanValue != 0) {
                this.f14938k.setChecked(false);
                f6.f.J2(this.f14932e, 0);
            }
            f6.f.d3(this.f14932e, booleanValue);
            UploadDataUtil.S0(this.f14932e).x0(Boolean.toString(booleanValue));
            return true;
        }
        if (!key.equals("regular_charge_protection_switch_in_health")) {
            return true;
        }
        if (booleanValue != 0) {
            this.f14937j.setChecked(false);
            f6.f.d3(this.f14932e, 0);
        }
        f6.f.J2(this.f14932e, booleanValue);
        UploadDataUtil.S0(this.f14932e).r0(Boolean.toString(booleanValue));
        return true;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        this.f14936i.j();
        this.f14935h.m();
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.f14933f.b();
        ((COUIPercentWidthRecyclerView) getListView()).addItemDecoration(this.f14940m);
    }
}
