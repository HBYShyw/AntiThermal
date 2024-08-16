package v9;

import android.R;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import com.coui.appcompat.preference.COUIPreferenceFragment;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.google.android.material.appbar.COUIDividerAppBarLayout;

/* compiled from: BasePreferenceFragment.java */
/* loaded from: classes2.dex */
public abstract class d extends COUIPreferenceFragment {

    /* renamed from: e, reason: collision with root package name */
    protected COUIToolbar f19196e;

    /* renamed from: f, reason: collision with root package name */
    private COUIDividerAppBarLayout f19197f;

    /* renamed from: g, reason: collision with root package name */
    private TextView f19198g;

    /* renamed from: h, reason: collision with root package name */
    private RelativeLayout f19199h;

    /* compiled from: BasePreferenceFragment.java */
    /* loaded from: classes2.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            d.this.getActivity().finish();
        }
    }

    private void h0(View view) {
        this.f19198g = (TextView) view.findViewById(R.id.empty);
        if (!TextUtils.isEmpty(d0())) {
            this.f19198g.setText(d0());
        }
        this.f19199h = (RelativeLayout) view.findViewById(com.oplus.battery.R.id.fragment_startup);
        ((TextView) view.findViewById(com.oplus.battery.R.id.loadingText)).setText(f0());
    }

    public String d0() {
        return "";
    }

    public TextView e0() {
        return this.f19198g;
    }

    public String f0() {
        return "";
    }

    public RelativeLayout g0() {
        return this.f19199h;
    }

    public abstract String getTitle();

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        COUIToolbar cOUIToolbar = (COUIToolbar) onCreateView.findViewById(com.oplus.battery.R.id.toolbar);
        this.f19196e = cOUIToolbar;
        if (cOUIToolbar == null) {
            return onCreateView;
        }
        cOUIToolbar.setNavigationIcon(com.oplus.battery.R.drawable.coui_back_arrow);
        this.f19196e.setNavigationOnClickListener(new a());
        ViewCompat.y0(getListView(), true);
        this.f19196e.setTitle(getTitle());
        this.f19196e.setTitleTextColor(getResources().getColor(com.oplus.battery.R.color.coui_color_primary_neutral));
        FragmentActivity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).setSupportActionBar(this.f19196e);
        }
        COUIDividerAppBarLayout cOUIDividerAppBarLayout = (COUIDividerAppBarLayout) onCreateView.findViewById(com.oplus.battery.R.id.appBarLayout);
        this.f19197f = cOUIDividerAppBarLayout;
        cOUIDividerAppBarLayout.setBackground(null);
        View b10 = x9.c.b(getContext());
        this.f19197f.addView(b10, 0, b10.getLayoutParams());
        this.f19197f.bindRecyclerView(getListView());
        View findViewById = this.f19197f.findViewById(com.oplus.battery.R.id.divider_line);
        if (getContext().getResources().getBoolean(com.oplus.battery.R.bool.is_immsersive_theme)) {
            findViewById.setVisibility(8);
        }
        h0(onCreateView);
        return onCreateView;
    }
}
