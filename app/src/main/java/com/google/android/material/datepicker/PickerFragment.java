package com.google.android.material.datepicker;

import androidx.fragment.app.Fragment;
import java.util.LinkedHashSet;

/* compiled from: PickerFragment.java */
/* renamed from: com.google.android.material.datepicker.m, reason: use source file name */
/* loaded from: classes.dex */
abstract class PickerFragment<S> extends Fragment {

    /* renamed from: e, reason: collision with root package name */
    protected final LinkedHashSet<OnSelectionChangedListener<S>> f8777e = new LinkedHashSet<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d0(OnSelectionChangedListener<S> onSelectionChangedListener) {
        return this.f8777e.add(onSelectionChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e0() {
        this.f8777e.clear();
    }
}
