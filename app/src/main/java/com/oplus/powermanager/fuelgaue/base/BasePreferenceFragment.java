package com.oplus.powermanager.fuelgaue.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.grid.COUIPercentWidthRecyclerView;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.google.android.material.appbar.COUIDividerAppBarLayout;
import com.oplus.battery.R;
import f6.f;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public abstract class BasePreferenceFragment extends OplusHighlightPreferenceFragment {
    private static final int MIDDLE_SCREEN_DP = 600;
    private static final String TAG = "BasePreferenceFragment";
    private COUIDividerAppBarLayout mColorAppBarLayout;
    private View mDividerView;
    private COUIPercentWidthRecyclerView mRecyclerView;
    private COUIToolbar mToolbar;

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.lifecycle.HasDefaultViewModelProviderFactory
    public /* bridge */ /* synthetic */ w.a getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

    public abstract String getTitle();

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        COUIPercentWidthRecyclerView cOUIPercentWidthRecyclerView = (COUIPercentWidthRecyclerView) layoutInflater.inflate(R.layout.preference_percent_recyclerview, viewGroup, false);
        cOUIPercentWidthRecyclerView.setEnablePointerDownAction(false);
        cOUIPercentWidthRecyclerView.setLayoutManager(onCreateLayoutManager());
        COUIDarkModeUtil.b(cOUIPercentWidthRecyclerView, false);
        return cOUIPercentWidthRecyclerView;
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        if (getListView() != null) {
            getListView().setVerticalScrollBarEnabled(false);
        }
        this.mToolbar = (COUIToolbar) onCreateView.findViewById(R.id.toolbar);
        this.mColorAppBarLayout = (COUIDividerAppBarLayout) onCreateView.findViewById(R.id.fragment_appBarLayout);
        this.mRecyclerView = (COUIPercentWidthRecyclerView) getListView();
        COUIToolbar cOUIToolbar = this.mToolbar;
        if (cOUIToolbar == null) {
            return onCreateView;
        }
        cOUIToolbar.setNavigationIcon(R.drawable.coui_back_arrow);
        this.mToolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                BasePreferenceFragment.this.getActivity().finish();
            }
        });
        ViewCompat.y0(getListView(), true);
        this.mToolbar.setTitle(getTitle());
        this.mToolbar.setNavigationContentDescription(R.string.accessibility_bar_back_description);
        this.mToolbar.setTitleTextColor(getResources().getColor(R.color.coui_color_primary_neutral));
        COUIDividerAppBarLayout cOUIDividerAppBarLayout = (COUIDividerAppBarLayout) onCreateView.findViewById(R.id.fragment_appBarLayout);
        this.mColorAppBarLayout = cOUIDividerAppBarLayout;
        if (cOUIDividerAppBarLayout != null) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, 0));
            this.mColorAppBarLayout.addView(imageView, 0, imageView.getLayoutParams());
            this.mColorAppBarLayout.bindRecyclerView(this.mRecyclerView);
        }
        this.mDividerView = onCreateView.findViewById(R.id.divider_line);
        if (f.N(getContext())) {
            this.mDividerView.setVisibility(8);
        }
        return onCreateView;
    }
}
