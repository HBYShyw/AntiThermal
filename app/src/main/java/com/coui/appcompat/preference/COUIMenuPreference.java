package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R$attr;
import com.support.list.R$styleable;
import g2.COUIClickSelectMenu;
import g2.PopupListItem;
import g2.PreciseClickHelper;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class COUIMenuPreference extends COUIPreference {
    private CharSequence[] F;
    private CharSequence[] G;
    private String H;
    private String I;
    private boolean J;
    private ArrayList<PopupListItem> K;
    private COUIClickSelectMenu L;
    private boolean M;
    private PreciseClickHelper.c N;
    private ColorStateList O;
    private final AdapterView.OnItemClickListener P;

    /* loaded from: classes.dex */
    class a implements AdapterView.OnItemClickListener {
        a() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i10, long j10) {
            COUIMenuPreference cOUIMenuPreference = COUIMenuPreference.this;
            if (cOUIMenuPreference.callChangeListener(cOUIMenuPreference.F[i10].toString())) {
                COUIMenuPreference cOUIMenuPreference2 = COUIMenuPreference.this;
                cOUIMenuPreference2.p(cOUIMenuPreference2.F[i10].toString());
            }
            COUIMenuPreference.this.L.d();
        }
    }

    public COUIMenuPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    @Override // androidx.preference.Preference
    public CharSequence getSummary() {
        if (getSummaryProvider() != null) {
            return getSummaryProvider().a(this);
        }
        String l10 = l();
        CharSequence summary = super.getSummary();
        String str = this.I;
        if (str == null) {
            return summary;
        }
        Object[] objArr = new Object[1];
        if (l10 == null) {
            l10 = "";
        }
        objArr[0] = l10;
        String format = String.format(str, objArr);
        if (TextUtils.equals(format, summary)) {
            return summary;
        }
        Log.w("COUIMenuPreference", "Setting a summary with a String formatting marker is no longer supported. You should use a SummaryProvider instead.");
        return format;
    }

    public int j(String str) {
        CharSequence[] charSequenceArr;
        if (str == null || (charSequenceArr = this.F) == null) {
            return 0;
        }
        for (int length = charSequenceArr.length - 1; length >= 0; length--) {
            if (!TextUtils.isEmpty(this.F[length]) && this.F[length].equals(str)) {
                return length;
            }
        }
        return 0;
    }

    public String l() {
        return this.H;
    }

    public void m(CharSequence[] charSequenceArr) {
        this.G = charSequenceArr;
        this.J = false;
        if (charSequenceArr == null || charSequenceArr.length <= 0) {
            return;
        }
        this.K.clear();
        for (CharSequence charSequence : charSequenceArr) {
            this.K.add(new PopupListItem((String) charSequence, true));
        }
    }

    public void n(CharSequence[] charSequenceArr) {
        this.F = charSequenceArr;
        this.J = false;
        if (this.G != null || charSequenceArr == null || charSequenceArr.length <= 0) {
            return;
        }
        this.K.clear();
        for (CharSequence charSequence : charSequenceArr) {
            this.K.add(new PopupListItem((String) charSequence, true));
        }
    }

    public void o(boolean z10) {
        this.M = z10;
        COUIClickSelectMenu cOUIClickSelectMenu = this.L;
        if (cOUIClickSelectMenu != null) {
            cOUIClickSelectMenu.g(z10);
        }
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        if (this.L == null) {
            this.L = new COUIClickSelectMenu(getContext(), preferenceViewHolder.itemView);
        }
        ColorStateList colorStateList = this.O;
        if (colorStateList != null) {
            this.L.f(preferenceViewHolder.itemView, this.K, colorStateList.getDefaultColor());
        } else {
            this.L.e(preferenceViewHolder.itemView, this.K);
        }
        this.L.g(this.M);
        PreciseClickHelper.c cVar = this.N;
        if (cVar != null) {
            this.L.i(cVar);
        }
        this.L.h(this.P);
    }

    @Override // androidx.preference.Preference
    protected Object onGetDefaultValue(TypedArray typedArray, int i10) {
        return typedArray.getString(i10);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.Preference
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable != null && parcelable.getClass().equals(SavedState.class)) {
            SavedState savedState = (SavedState) parcelable;
            super.onRestoreInstanceState(savedState.getSuperState());
            if (this.J) {
                return;
            }
            p(savedState.f6958e);
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.Preference
    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        if (isPersistent()) {
            return onSaveInstanceState;
        }
        SavedState savedState = new SavedState(onSaveInstanceState);
        savedState.f6958e = l();
        return savedState;
    }

    @Override // androidx.preference.Preference
    protected void onSetInitialValue(Object obj) {
        p(getPersistedString((String) obj));
    }

    public void p(String str) {
        if ((!TextUtils.equals(this.H, str)) || !this.J) {
            this.H = str;
            this.J = true;
            if (this.K.size() > 0 && !TextUtils.isEmpty(str)) {
                for (int i10 = 0; i10 < this.K.size(); i10++) {
                    PopupListItem popupListItem = this.K.get(i10);
                    String e10 = popupListItem.e();
                    CharSequence[] charSequenceArr = this.G;
                    if (TextUtils.equals(e10, charSequenceArr != null ? charSequenceArr[j(str)] : str)) {
                        popupListItem.m(true);
                        popupListItem.l(true);
                    } else {
                        popupListItem.m(false);
                        popupListItem.l(false);
                    }
                }
            }
            persistString(str);
            notifyChanged();
        }
    }

    @Override // androidx.preference.Preference
    public void setEnabled(boolean z10) {
        super.setEnabled(z10);
        o(z10);
    }

    @Override // androidx.preference.Preference
    public void setSummary(CharSequence charSequence) {
        super.setSummary(charSequence);
        if (charSequence == null && this.I != null) {
            this.I = null;
        } else {
            if (charSequence == null || charSequence.equals(this.I)) {
                return;
            }
            this.I = charSequence.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        String f6958e;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        }

        SavedState(Parcel parcel) {
            super(parcel);
            this.f6958e = parcel.readString();
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeString(this.f6958e);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    public COUIMenuPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, R$attr.preferenceStyle);
        this.K = new ArrayList<>();
        this.M = true;
        this.P = new a();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIMenuPreference, i10, 0);
        int i12 = R$styleable.COUIMenuPreference_android_entryValues;
        this.F = TypedArrayUtils.m(obtainStyledAttributes, i12, i12);
        int i13 = R$styleable.COUIMenuPreference_android_entries;
        this.G = TypedArrayUtils.m(obtainStyledAttributes, i13, i13);
        this.H = obtainStyledAttributes.getString(R$styleable.COUIMenuPreference_android_value);
        obtainStyledAttributes.recycle();
        n(this.F);
        m(this.G);
        p(this.H);
    }

    public COUIMenuPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }
}
