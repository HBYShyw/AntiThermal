package androidx.preference;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;

/* compiled from: ListPreferenceDialogFragmentCompat.java */
/* renamed from: androidx.preference.c, reason: use source file name */
/* loaded from: classes.dex */
public class ListPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {
    int C;
    private CharSequence[] D;
    private CharSequence[] E;

    /* compiled from: ListPreferenceDialogFragmentCompat.java */
    /* renamed from: androidx.preference.c$a */
    /* loaded from: classes.dex */
    class a implements DialogInterface.OnClickListener {
        a() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            ListPreferenceDialogFragmentCompat listPreferenceDialogFragmentCompat = ListPreferenceDialogFragmentCompat.this;
            listPreferenceDialogFragmentCompat.C = i10;
            listPreferenceDialogFragmentCompat.onClick(dialogInterface, -1);
            dialogInterface.dismiss();
        }
    }

    private ListPreference y0() {
        return (ListPreference) r0();
    }

    public static ListPreferenceDialogFragmentCompat z0(String str) {
        ListPreferenceDialogFragmentCompat listPreferenceDialogFragmentCompat = new ListPreferenceDialogFragmentCompat();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        listPreferenceDialogFragmentCompat.setArguments(bundle);
        return listPreferenceDialogFragmentCompat;
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            ListPreference y02 = y0();
            if (y02.j() != null && y02.m() != null) {
                this.C = y02.i(y02.n());
                this.D = y02.j();
                this.E = y02.m();
                return;
            }
            throw new IllegalStateException("ListPreference requires an entries array and an entryValues array.");
        }
        this.C = bundle.getInt("ListPreferenceDialogFragment.index", 0);
        this.D = bundle.getCharSequenceArray("ListPreferenceDialogFragment.entries");
        this.E = bundle.getCharSequenceArray("ListPreferenceDialogFragment.entryValues");
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("ListPreferenceDialogFragment.index", this.C);
        bundle.putCharSequenceArray("ListPreferenceDialogFragment.entries", this.D);
        bundle.putCharSequenceArray("ListPreferenceDialogFragment.entryValues", this.E);
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat
    public void v0(boolean z10) {
        int i10;
        if (!z10 || (i10 = this.C) < 0) {
            return;
        }
        String charSequence = this.E[i10].toString();
        ListPreference y02 = y0();
        if (y02.callChangeListener(charSequence)) {
            y02.p(charSequence);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.PreferenceDialogFragmentCompat
    public void w0(AlertDialog.a aVar) {
        super.w0(aVar);
        aVar.r(this.D, this.C, new a());
        aVar.p(null, null);
    }
}
