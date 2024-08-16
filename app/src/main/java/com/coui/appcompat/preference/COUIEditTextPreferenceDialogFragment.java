package com.coui.appcompat.preference;

import android.R;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.DialogPreference;
import androidx.preference.EditTextPreferenceDialogFragmentCompat;
import com.coui.appcompat.edittext.COUIEditText;
import com.support.appcompat.R$style;
import x1.COUIAlertDialogBuilder;

/* compiled from: COUIEditTextPreferenceDialogFragment.java */
/* renamed from: com.coui.appcompat.preference.d, reason: use source file name */
/* loaded from: classes.dex */
public class COUIEditTextPreferenceDialogFragment extends EditTextPreferenceDialogFragmentCompat {
    private COUIEditText E;

    /* compiled from: COUIEditTextPreferenceDialogFragment.java */
    /* renamed from: com.coui.appcompat.preference.d$a */
    /* loaded from: classes.dex */
    class a implements TextWatcher {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ AlertDialog f7067e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ boolean f7068f;

        a(AlertDialog alertDialog, boolean z10) {
            this.f7067e = alertDialog;
            this.f7068f = z10;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            Button i10 = this.f7067e.i(-1);
            if (i10 == null || this.f7068f) {
                return;
            }
            i10.setEnabled(!TextUtils.isEmpty(editable));
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }
    }

    public static COUIEditTextPreferenceDialogFragment A0(String str) {
        COUIEditTextPreferenceDialogFragment cOUIEditTextPreferenceDialogFragment = new COUIEditTextPreferenceDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        cOUIEditTextPreferenceDialogFragment.setArguments(bundle);
        return cOUIEditTextPreferenceDialogFragment;
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment
    public Dialog k0(Bundle bundle) {
        FragmentActivity activity = getActivity();
        COUIAlertDialogBuilder k10 = new COUIAlertDialogBuilder(requireContext(), R$style.COUIAlertDialog_BottomAssignment).t(r0().f()).h(r0().e()).p(r0().h(), this).k(r0().g(), this);
        View u02 = u0(activity);
        if (u02 != null) {
            this.E = (COUIEditText) u02.findViewById(R.id.edit);
            t0(u02);
            k10.v(u02);
        }
        if (r0() != null) {
            t0(u02);
        }
        w0(k10);
        AlertDialog a10 = k10.a();
        DialogPreference r02 = r0();
        COUIEditTextPreference cOUIEditTextPreference = null;
        if (r02 != null && (r02 instanceof COUIEditTextPreference)) {
            cOUIEditTextPreference = (COUIEditTextPreference) r02;
        }
        this.E.addTextChangedListener(new a(a10, cOUIEditTextPreference != null ? cOUIEditTextPreference.n() : false));
        return a10;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        COUIEditText cOUIEditText = this.E;
        if (cOUIEditText != null) {
            cOUIEditText.setFocusable(true);
            this.E.requestFocus();
            if (i0() != null) {
                i0().getWindow().setSoftInputMode(5);
            }
        }
    }

    @Override // androidx.preference.EditTextPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        COUIEditText cOUIEditText = this.E;
        if (cOUIEditText != null) {
            bundle.putCharSequence("EditTextPreferenceDialogFragment.text", cOUIEditText.getText());
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (r0() == null) {
            g0();
        }
    }
}
