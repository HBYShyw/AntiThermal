package androidx.preference;

import android.R;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/* compiled from: EditTextPreferenceDialogFragmentCompat.java */
/* renamed from: androidx.preference.a, reason: use source file name */
/* loaded from: classes.dex */
public class EditTextPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {
    private EditText C;
    private CharSequence D;

    private EditTextPreference y0() {
        return (EditTextPreference) r0();
    }

    public static EditTextPreferenceDialogFragmentCompat z0(String str) {
        EditTextPreferenceDialogFragmentCompat editTextPreferenceDialogFragmentCompat = new EditTextPreferenceDialogFragmentCompat();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        editTextPreferenceDialogFragmentCompat.setArguments(bundle);
        return editTextPreferenceDialogFragmentCompat;
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            this.D = y0().j();
        } else {
            this.D = bundle.getCharSequence("EditTextPreferenceDialogFragment.text");
        }
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putCharSequence("EditTextPreferenceDialogFragment.text", this.D);
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat
    protected boolean s0() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.PreferenceDialogFragmentCompat
    public void t0(View view) {
        super.t0(view);
        EditText editText = (EditText) view.findViewById(R.id.edit);
        this.C = editText;
        if (editText != null) {
            editText.requestFocus();
            this.C.setText(this.D);
            EditText editText2 = this.C;
            editText2.setSelection(editText2.getText().length());
            if (y0().i() != null) {
                y0().i().a(this.C);
                return;
            }
            return;
        }
        throw new IllegalStateException("Dialog view must contain an EditText with id @android:id/edit");
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat
    public void v0(boolean z10) {
        if (z10) {
            String obj = this.C.getText().toString();
            EditTextPreference y02 = y0();
            if (y02.callChangeListener(obj)) {
                y02.l(obj);
            }
        }
    }
}
