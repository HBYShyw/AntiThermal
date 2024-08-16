package com.coui.appcompat.preference;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.preference.ListPreferenceDialogFragmentCompat;
import com.support.appcompat.R$id;
import com.support.appcompat.R$layout;
import com.support.appcompat.R$style;
import com.support.list.R$string;
import x1.COUIAlertDialogBuilder;
import y1.ChoiceListAdapter;

/* compiled from: COUIListPreferenceDialogFragment.java */
/* renamed from: com.coui.appcompat.preference.e, reason: use source file name */
/* loaded from: classes.dex */
public class COUIListPreferenceDialogFragment extends ListPreferenceDialogFragmentCompat {
    private CharSequence F;
    private CharSequence[] G;
    private CharSequence[] H;
    private CharSequence[] I;
    private int[] J;
    private COUIAlertDialogBuilder K;
    private int L = -1;
    private COUIListPreference M;

    /* compiled from: COUIListPreferenceDialogFragment.java */
    /* renamed from: com.coui.appcompat.preference.e$a */
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

    /* compiled from: COUIListPreferenceDialogFragment.java */
    /* renamed from: com.coui.appcompat.preference.e$b */
    /* loaded from: classes.dex */
    class b implements DialogInterface.OnClickListener {
        b() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            COUIListPreferenceDialogFragment.this.L = i10;
            COUIListPreferenceDialogFragment.this.onClick(dialogInterface, -1);
            dialogInterface.dismiss();
        }
    }

    public static COUIListPreferenceDialogFragment B0(String str) {
        COUIListPreferenceDialogFragment cOUIListPreferenceDialogFragment = new COUIListPreferenceDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        cOUIListPreferenceDialogFragment.setArguments(bundle);
        return cOUIListPreferenceDialogFragment;
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment
    public Dialog k0(Bundle bundle) {
        boolean[] zArr;
        int i10;
        CharSequence[] charSequenceArr = this.G;
        View view = null;
        if (charSequenceArr == null || (i10 = this.L) < 0 || i10 >= charSequenceArr.length) {
            zArr = null;
        } else {
            boolean[] zArr2 = new boolean[charSequenceArr.length];
            zArr2[i10] = true;
            zArr = zArr2;
        }
        this.K = new COUIAlertDialogBuilder(requireContext(), R$style.COUIAlertDialog_BottomAssignment).t(this.F).a0(R$string.dialog_cancel, null).c(new a(getContext(), R$layout.coui_select_dialog_singlechoice, this.G, this.I, zArr, false), new b());
        Point point = new Point();
        COUIListPreference cOUIListPreference = this.M;
        if (cOUIListPreference != null) {
            view = cOUIListPreference.s();
            point = this.M.r();
        }
        if (this.J != null) {
            int[] iArr = this.J;
            point = new Point(iArr[0], iArr[1]);
        }
        return this.K.B(view, point);
    }

    @Override // androidx.preference.ListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            COUIListPreference cOUIListPreference = (COUIListPreference) r0();
            this.M = cOUIListPreference;
            if (cOUIListPreference.j() != null && this.M.m() != null) {
                this.F = this.M.f();
                this.I = this.M.t();
                COUIListPreference cOUIListPreference2 = this.M;
                this.L = cOUIListPreference2.i(cOUIListPreference2.n());
                this.G = this.M.j();
                this.H = this.M.m();
                return;
            }
            throw new IllegalStateException("ListPreference requires an entries array and an entryValues array.");
        }
        this.L = bundle.getInt("COUIListPreferenceDialogFragment.index", -1);
        this.F = bundle.getString("COUIListPreferenceDialogFragment.title");
        this.G = bundle.getCharSequenceArray("ListPreferenceDialogFragment.entries");
        this.H = bundle.getCharSequenceArray("ListPreferenceDialogFragment.entryValues");
        this.I = bundle.getCharSequenceArray("COUListPreferenceDialogFragment.summarys");
        this.J = bundle.getIntArray("ListPreferenceDialogFragment.SAVE_STATE_POSITION");
    }

    @Override // androidx.preference.ListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("COUIListPreferenceDialogFragment.index", this.L);
        CharSequence charSequence = this.F;
        if (charSequence != null) {
            bundle.putString("COUIListPreferenceDialogFragment.title", String.valueOf(charSequence));
        }
        bundle.putCharSequenceArray("COUListPreferenceDialogFragment.summarys", this.I);
        int[] iArr = {i0().getWindow().getAttributes().x, i0().getWindow().getAttributes().y};
        this.J = iArr;
        bundle.putIntArray("ListPreferenceDialogFragment.SAVE_STATE_POSITION", iArr);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (r0() == null) {
            g0();
            return;
        }
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = this.K;
        if (cOUIAlertDialogBuilder != null) {
            cOUIAlertDialogBuilder.m0();
        }
    }

    @Override // androidx.preference.ListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat
    public void v0(boolean z10) {
        int i10;
        super.v0(z10);
        if (!z10 || this.G == null || (i10 = this.L) < 0) {
            return;
        }
        CharSequence[] charSequenceArr = this.H;
        if (i10 < charSequenceArr.length) {
            String charSequence = charSequenceArr[i10].toString();
            if (r0() != null) {
                COUIListPreference cOUIListPreference = (COUIListPreference) r0();
                if (cOUIListPreference.callChangeListener(charSequence)) {
                    cOUIListPreference.p(charSequence);
                }
            }
        }
    }
}
