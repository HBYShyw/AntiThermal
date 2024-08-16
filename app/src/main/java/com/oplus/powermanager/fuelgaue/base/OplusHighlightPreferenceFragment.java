package com.oplus.powermanager.fuelgaue.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.preference.COUIPreferenceFragment;

/* loaded from: classes.dex */
public abstract class OplusHighlightPreferenceFragment extends COUIPreferenceFragment {
    private static final String SAVE_HIGHLIGHTED_KEY = "android:preference_highlighted";
    public HighlightPreferenceGroupAdapter mAdapter;
    private RecyclerView.h mCurrentRootAdapter;
    public boolean mPreferenceHighlighted = false;
    private boolean mIsDataSetObserverRegistered = false;
    private RecyclerView.j mDataSetObserver = new RecyclerView.j() { // from class: com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment.1
        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onChanged() {
            OplusHighlightPreferenceFragment.this.highlightPreferenceIfNeeded();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeChanged(int i10, int i11) {
            OplusHighlightPreferenceFragment.this.highlightPreferenceIfNeeded();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeInserted(int i10, int i11) {
            OplusHighlightPreferenceFragment.this.highlightPreferenceIfNeeded();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeMoved(int i10, int i11, int i12) {
            OplusHighlightPreferenceFragment.this.highlightPreferenceIfNeeded();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeRemoved(int i10, int i11) {
            OplusHighlightPreferenceFragment.this.highlightPreferenceIfNeeded();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeChanged(int i10, int i11, Object obj) {
            OplusHighlightPreferenceFragment.this.highlightPreferenceIfNeeded();
        }
    };

    public /* bridge */ /* synthetic */ w.a getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

    public void highlightPreferenceIfNeeded() {
        HighlightPreferenceGroupAdapter highlightPreferenceGroupAdapter;
        if (isAdded() && (highlightPreferenceGroupAdapter = this.mAdapter) != null) {
            highlightPreferenceGroupAdapter.requestHighlight(getView(), getListView(), true);
        }
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    @SuppressLint({"RestrictedApi"})
    protected void onBindPreferences() {
        registerObserverIfNeeded();
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.mPreferenceHighlighted = bundle.getBoolean(SAVE_HIGHLIGHTED_KEY);
        }
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    protected RecyclerView.h onCreateAdapter(PreferenceScreen preferenceScreen) {
        Bundle arguments = getArguments();
        String string = arguments == null ? null : arguments.getString(HighlightPreferenceGroupAdapter.EXTRA_FRAGMENT_ARG_KEY);
        FragmentActivity activity = getActivity();
        Intent intent = activity != null ? activity.getIntent() : null;
        if (TextUtils.isEmpty(string) && intent != null) {
            Bundle extras = intent.getExtras();
            string = extras != null ? extras.getString(HighlightPreferenceGroupAdapter.EXTRA_FRAGMENT_ARG_KEY) : null;
        }
        HighlightPreferenceGroupAdapter highlightPreferenceGroupAdapter = new HighlightPreferenceGroupAdapter(preferenceScreen, string, this.mPreferenceHighlighted);
        this.mAdapter = highlightPreferenceGroupAdapter;
        return highlightPreferenceGroupAdapter;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        highlightPreferenceIfNeeded();
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        HighlightPreferenceGroupAdapter highlightPreferenceGroupAdapter = this.mAdapter;
        if (highlightPreferenceGroupAdapter != null) {
            bundle.putBoolean(SAVE_HIGHLIGHTED_KEY, highlightPreferenceGroupAdapter.isHighlightRequested());
        }
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    @SuppressLint({"RestrictedApi"})
    protected void onUnbindPreferences() {
        unregisterObserverIfNeeded();
    }

    public void registerObserverIfNeeded() {
        if (this.mIsDataSetObserverRegistered) {
            return;
        }
        RecyclerView.h hVar = this.mCurrentRootAdapter;
        if (hVar != null) {
            hVar.unregisterAdapterDataObserver(this.mDataSetObserver);
        }
        RecyclerView.h adapter = getListView().getAdapter();
        this.mCurrentRootAdapter = adapter;
        adapter.registerAdapterDataObserver(this.mDataSetObserver);
        this.mIsDataSetObserverRegistered = true;
        highlightPreferenceIfNeeded();
    }

    public void unregisterObserverIfNeeded() {
        if (this.mIsDataSetObserverRegistered) {
            RecyclerView.h hVar = this.mCurrentRootAdapter;
            if (hVar != null) {
                hVar.unregisterAdapterDataObserver(this.mDataSetObserver);
                this.mCurrentRootAdapter = null;
            }
            this.mIsDataSetObserverRegistered = false;
        }
    }
}
