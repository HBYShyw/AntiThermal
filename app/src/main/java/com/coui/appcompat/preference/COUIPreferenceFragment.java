package com.coui.appcompat.preference;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.RecyclerView;
import com.support.list.R$layout;
import w1.COUIDarkModeUtil;

/* compiled from: COUIPreferenceFragment.java */
/* renamed from: com.coui.appcompat.preference.g, reason: use source file name */
/* loaded from: classes.dex */
public class COUIPreferenceFragment extends PreferenceFragmentCompat {
    private static final String DIALOG_FRAGMENT_TAG = "androidx.preference.PreferenceFragment.DIALOG";
    private boolean mEnableInternalDivider = true;
    private COUIPreferenceItemDecoration mPreferenceItemDecoration;

    public COUIPreferenceItemDecoration getItemDecoration() {
        return this.mPreferenceItemDecoration;
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        COUIRecyclerView cOUIRecyclerView = (COUIRecyclerView) layoutInflater.inflate(R$layout.coui_preference_recyclerview, viewGroup, false);
        cOUIRecyclerView.setEnablePointerDownAction(false);
        cOUIRecyclerView.setLayoutManager(onCreateLayoutManager());
        COUIDarkModeUtil.b(cOUIRecyclerView, false);
        return cOUIRecyclerView;
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        setDivider(null);
        setDividerHeight(0);
        return onCreateView;
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onDestroyView() {
        COUIPreferenceItemDecoration cOUIPreferenceItemDecoration = this.mPreferenceItemDecoration;
        if (cOUIPreferenceItemDecoration != null) {
            cOUIPreferenceItemDecoration.o();
        }
        super.onDestroyView();
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.a
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment B0;
        if (getFragmentManager().j0(DIALOG_FRAGMENT_TAG) != null) {
            return;
        }
        if (preference instanceof COUIActivityDialogPreference) {
            B0 = COUIActivityDialogFragment.H0(preference.getKey());
        } else if (preference instanceof COUIEditTextPreference) {
            B0 = COUIEditTextPreferenceDialogFragment.A0(preference.getKey());
        } else if (preference instanceof COUIMultiSelectListPreference) {
            B0 = COUIMultiSelectListPreferenceDialogFragment.C0(preference.getKey());
        } else if (preference instanceof ListPreference) {
            B0 = COUIListPreferenceDialogFragment.B0(preference.getKey());
        } else {
            super.onDisplayPreferenceDialog(preference);
            return;
        }
        B0.setTargetFragment(this, 0);
        B0.q0(getFragmentManager(), DIALOG_FRAGMENT_TAG);
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (getListView() == null || this.mPreferenceItemDecoration == null || !this.mEnableInternalDivider) {
            return;
        }
        getListView().removeItemDecoration(this.mPreferenceItemDecoration);
        getListView().addItemDecoration(this.mPreferenceItemDecoration);
    }

    public void setEnableCOUIPreferenceDivider(boolean z10) {
        this.mEnableInternalDivider = z10;
        if (z10) {
            if (getListView() == null || this.mPreferenceItemDecoration == null) {
                return;
            }
            getListView().removeItemDecoration(this.mPreferenceItemDecoration);
            getListView().addItemDecoration(this.mPreferenceItemDecoration);
            return;
        }
        if (getListView() != null) {
            getListView().removeItemDecoration(this.mPreferenceItemDecoration);
        }
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        super.setPreferenceScreen(preferenceScreen);
        this.mPreferenceItemDecoration = new COUIPreferenceItemDecoration(getContext(), preferenceScreen);
        if (getListView() == null || !this.mEnableInternalDivider) {
            return;
        }
        getListView().addItemDecoration(this.mPreferenceItemDecoration);
    }
}
