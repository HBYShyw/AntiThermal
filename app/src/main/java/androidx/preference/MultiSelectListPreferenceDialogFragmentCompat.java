package androidx.preference;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/* compiled from: MultiSelectListPreferenceDialogFragmentCompat.java */
/* renamed from: androidx.preference.d, reason: use source file name */
/* loaded from: classes.dex */
public class MultiSelectListPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {
    Set<String> C = new HashSet();
    boolean D;
    CharSequence[] E;
    CharSequence[] F;

    /* compiled from: MultiSelectListPreferenceDialogFragmentCompat.java */
    /* renamed from: androidx.preference.d$a */
    /* loaded from: classes.dex */
    class a implements DialogInterface.OnMultiChoiceClickListener {
        a() {
        }

        @Override // android.content.DialogInterface.OnMultiChoiceClickListener
        public void onClick(DialogInterface dialogInterface, int i10, boolean z10) {
            if (z10) {
                MultiSelectListPreferenceDialogFragmentCompat multiSelectListPreferenceDialogFragmentCompat = MultiSelectListPreferenceDialogFragmentCompat.this;
                multiSelectListPreferenceDialogFragmentCompat.D |= multiSelectListPreferenceDialogFragmentCompat.C.add(multiSelectListPreferenceDialogFragmentCompat.F[i10].toString());
            } else {
                MultiSelectListPreferenceDialogFragmentCompat multiSelectListPreferenceDialogFragmentCompat2 = MultiSelectListPreferenceDialogFragmentCompat.this;
                multiSelectListPreferenceDialogFragmentCompat2.D |= multiSelectListPreferenceDialogFragmentCompat2.C.remove(multiSelectListPreferenceDialogFragmentCompat2.F[i10].toString());
            }
        }
    }

    private MultiSelectListPreference y0() {
        return (MultiSelectListPreference) r0();
    }

    public static MultiSelectListPreferenceDialogFragmentCompat z0(String str) {
        MultiSelectListPreferenceDialogFragmentCompat multiSelectListPreferenceDialogFragmentCompat = new MultiSelectListPreferenceDialogFragmentCompat();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        multiSelectListPreferenceDialogFragmentCompat.setArguments(bundle);
        return multiSelectListPreferenceDialogFragmentCompat;
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            MultiSelectListPreference y02 = y0();
            if (y02.i() != null && y02.j() != null) {
                this.C.clear();
                this.C.addAll(y02.l());
                this.D = false;
                this.E = y02.i();
                this.F = y02.j();
                return;
            }
            throw new IllegalStateException("MultiSelectListPreference requires an entries array and an entryValues array.");
        }
        this.C.clear();
        this.C.addAll(bundle.getStringArrayList("MultiSelectListPreferenceDialogFragmentCompat.values"));
        this.D = bundle.getBoolean("MultiSelectListPreferenceDialogFragmentCompat.changed", false);
        this.E = bundle.getCharSequenceArray("MultiSelectListPreferenceDialogFragmentCompat.entries");
        this.F = bundle.getCharSequenceArray("MultiSelectListPreferenceDialogFragmentCompat.entryValues");
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putStringArrayList("MultiSelectListPreferenceDialogFragmentCompat.values", new ArrayList<>(this.C));
        bundle.putBoolean("MultiSelectListPreferenceDialogFragmentCompat.changed", this.D);
        bundle.putCharSequenceArray("MultiSelectListPreferenceDialogFragmentCompat.entries", this.E);
        bundle.putCharSequenceArray("MultiSelectListPreferenceDialogFragmentCompat.entryValues", this.F);
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat
    public void v0(boolean z10) {
        if (z10 && this.D) {
            MultiSelectListPreference y02 = y0();
            if (y02.callChangeListener(this.C)) {
                y02.m(this.C);
            }
        }
        this.D = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.PreferenceDialogFragmentCompat
    public void w0(AlertDialog.a aVar) {
        super.w0(aVar);
        int length = this.F.length;
        boolean[] zArr = new boolean[length];
        for (int i10 = 0; i10 < length; i10++) {
            zArr[i10] = this.C.contains(this.F[i10].toString());
        }
        aVar.i(this.E, zArr, new a());
    }
}
