package com.coui.appcompat.preference;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.preference.MultiSelectListPreferenceDialogFragmentCompat;
import com.support.appcompat.R$id;
import com.support.appcompat.R$layout;
import com.support.appcompat.R$style;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import x1.COUIAlertDialogBuilder;
import y1.ChoiceListAdapter;

/* compiled from: COUIMultiSelectListPreferenceDialogFragment.java */
/* renamed from: com.coui.appcompat.preference.f, reason: use source file name */
/* loaded from: classes.dex */
public class COUIMultiSelectListPreferenceDialogFragment extends MultiSelectListPreferenceDialogFragmentCompat {
    private CharSequence G;
    private CharSequence[] H;
    private CharSequence[] I;
    private CharSequence[] J;
    private CharSequence K;
    private CharSequence L;
    private COUIAlertDialogBuilder M;
    private ChoiceListAdapter N;
    private boolean[] O;
    private COUIMultiSelectListPreference P;
    private int[] Q;

    /* compiled from: COUIMultiSelectListPreferenceDialogFragment.java */
    /* renamed from: com.coui.appcompat.preference.f$a */
    /* loaded from: classes.dex */
    class a extends ChoiceListAdapter {
        a(Context context, int i10, CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2, boolean[] zArr, boolean z10) {
            super(context, i10, charSequenceArr, charSequenceArr2, zArr, z10);
        }

        @Override // y1.ChoiceListAdapter, android.widget.Adapter
        public View getView(int i10, View view, ViewGroup viewGroup) {
            View view2 = super.getView(i10, view, viewGroup);
            View findViewById = view2.findViewById(R$id.item_divider);
            int count = getCount();
            if (findViewById != null) {
                if (count != 1 && i10 != count - 1) {
                    findViewById.setVisibility(0);
                } else {
                    findViewById.setVisibility(8);
                }
            }
            return view2;
        }
    }

    private boolean[] A0(Set<String> set) {
        boolean[] zArr = new boolean[this.H.length];
        int i10 = 0;
        while (true) {
            CharSequence[] charSequenceArr = this.H;
            if (i10 >= charSequenceArr.length) {
                return zArr;
            }
            zArr[i10] = set.contains(charSequenceArr[i10].toString());
            i10++;
        }
    }

    private Set<String> B0() {
        HashSet hashSet = new HashSet();
        boolean[] f10 = this.N.f();
        for (int i10 = 0; i10 < f10.length; i10++) {
            CharSequence[] charSequenceArr = this.I;
            if (i10 >= charSequenceArr.length) {
                break;
            }
            if (f10[i10]) {
                hashSet.add(charSequenceArr[i10].toString());
            }
        }
        return hashSet;
    }

    public static COUIMultiSelectListPreferenceDialogFragment C0(String str) {
        COUIMultiSelectListPreferenceDialogFragment cOUIMultiSelectListPreferenceDialogFragment = new COUIMultiSelectListPreferenceDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        cOUIMultiSelectListPreferenceDialogFragment.setArguments(bundle);
        return cOUIMultiSelectListPreferenceDialogFragment;
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment
    public Dialog k0(Bundle bundle) {
        Point point;
        View view;
        this.N = new a(getContext(), R$layout.coui_select_dialog_multichoice, this.H, this.J, this.O, true);
        Context context = getContext();
        Objects.requireNonNull(context);
        this.M = new COUIAlertDialogBuilder(context, R$style.COUIAlertDialog_BottomAssignment).t(this.G).c(this.N, this).p(this.K, this).k(this.L, this);
        Point point2 = new Point();
        COUIMultiSelectListPreference cOUIMultiSelectListPreference = this.P;
        if (cOUIMultiSelectListPreference != null) {
            view = cOUIMultiSelectListPreference.p();
            point = this.P.o();
        } else {
            point = point2;
            view = null;
        }
        if (this.Q != null) {
            int[] iArr = this.Q;
            point = new Point(iArr[0], iArr[1]);
        }
        return this.M.B(view, point);
    }

    @Override // androidx.preference.MultiSelectListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            COUIMultiSelectListPreference cOUIMultiSelectListPreference = (COUIMultiSelectListPreference) r0();
            this.P = cOUIMultiSelectListPreference;
            this.G = cOUIMultiSelectListPreference.f();
            this.H = this.P.i();
            this.I = this.P.j();
            this.J = this.P.q();
            this.K = this.P.h();
            this.L = this.P.g();
            this.O = A0(this.P.l());
            return;
        }
        this.G = bundle.getString("COUIMultiSelectListPreferenceDialogFragment.title");
        this.H = bundle.getCharSequenceArray("MultiSelectListPreferenceDialogFragmentCompat.entries");
        this.I = bundle.getCharSequenceArray("MultiSelectListPreferenceDialogFragmentCompat.entryValues");
        this.J = bundle.getCharSequenceArray("COUIMultiSelectListPreferenceDialogFragment.summarys");
        this.K = bundle.getString("COUIMultiSelectListPreferenceDialogFragment.positiveButtonText");
        this.L = bundle.getString("COUIMultiSelectListPreferenceDialogFragment.negativeButtonTextitle");
        this.O = bundle.getBooleanArray("COUIMultiSelectListPreferenceDialogFragment.values");
        this.Q = bundle.getIntArray("ListPreferenceDialogFragment.SAVE_STATE_POSITION");
    }

    @Override // androidx.preference.MultiSelectListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBooleanArray("COUIMultiSelectListPreferenceDialogFragment.values", this.N.f());
        CharSequence charSequence = this.G;
        if (charSequence != null) {
            bundle.putString("COUIMultiSelectListPreferenceDialogFragment.title", String.valueOf(charSequence));
        }
        bundle.putString("COUIMultiSelectListPreferenceDialogFragment.positiveButtonText", String.valueOf(this.K));
        bundle.putString("COUIMultiSelectListPreferenceDialogFragment.negativeButtonTextitle", String.valueOf(this.L));
        bundle.putCharSequenceArray("COUIMultiSelectListPreferenceDialogFragment.summarys", this.J);
        int[] iArr = {i0().getWindow().getAttributes().x, i0().getWindow().getAttributes().y};
        this.Q = iArr;
        bundle.putIntArray("ListPreferenceDialogFragment.SAVE_STATE_POSITION", iArr);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (r0() == null) {
            g0();
            return;
        }
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = this.M;
        if (cOUIAlertDialogBuilder != null) {
            cOUIAlertDialogBuilder.m0();
        }
    }

    @Override // androidx.preference.MultiSelectListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat
    public void v0(boolean z10) {
        COUIMultiSelectListPreference cOUIMultiSelectListPreference;
        super.v0(z10);
        if (z10) {
            Set<String> B0 = B0();
            if (r0() == null || (cOUIMultiSelectListPreference = (COUIMultiSelectListPreference) r0()) == null || !cOUIMultiSelectListPreference.callChangeListener(B0)) {
                return;
            }
            cOUIMultiSelectListPreference.m(B0);
        }
    }
}
