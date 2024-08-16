package androidx.preference;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;

/* loaded from: classes.dex */
public class ListPreference extends DialogPreference {

    /* renamed from: k, reason: collision with root package name */
    private CharSequence[] f3258k;

    /* renamed from: l, reason: collision with root package name */
    private CharSequence[] f3259l;

    /* renamed from: m, reason: collision with root package name */
    private String f3260m;

    /* renamed from: n, reason: collision with root package name */
    private String f3261n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f3262o;

    /* loaded from: classes.dex */
    public static final class a implements Preference.f<ListPreference> {

        /* renamed from: a, reason: collision with root package name */
        private static a f3264a;

        private a() {
        }

        public static a b() {
            if (f3264a == null) {
                f3264a = new a();
            }
            return f3264a;
        }

        @Override // androidx.preference.Preference.f
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public CharSequence a(ListPreference listPreference) {
            if (TextUtils.isEmpty(listPreference.l())) {
                return listPreference.getContext().getString(R$string.not_set);
            }
            return listPreference.l();
        }
    }

    public ListPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ListPreference, i10, i11);
        this.f3258k = TypedArrayUtils.m(obtainStyledAttributes, R$styleable.ListPreference_entries, R$styleable.ListPreference_android_entries);
        this.f3259l = TypedArrayUtils.m(obtainStyledAttributes, R$styleable.ListPreference_entryValues, R$styleable.ListPreference_android_entryValues);
        int i12 = R$styleable.ListPreference_useSimpleSummaryProvider;
        if (TypedArrayUtils.b(obtainStyledAttributes, i12, i12, false)) {
            setSummaryProvider(a.b());
        }
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R$styleable.Preference, i10, i11);
        this.f3261n = TypedArrayUtils.k(obtainStyledAttributes2, R$styleable.Preference_summary, R$styleable.Preference_android_summary);
        obtainStyledAttributes2.recycle();
    }

    private int o() {
        return i(this.f3260m);
    }

    @Override // androidx.preference.Preference
    public CharSequence getSummary() {
        if (getSummaryProvider() != null) {
            return getSummaryProvider().a(this);
        }
        CharSequence l10 = l();
        CharSequence summary = super.getSummary();
        String str = this.f3261n;
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
        Log.w("ListPreference", "Setting a summary with a String formatting marker is no longer supported. You should use a SummaryProvider instead.");
        return format;
    }

    public int i(String str) {
        CharSequence[] charSequenceArr;
        if (str == null || (charSequenceArr = this.f3259l) == null) {
            return -1;
        }
        for (int length = charSequenceArr.length - 1; length >= 0; length--) {
            if (this.f3259l[length].equals(str)) {
                return length;
            }
        }
        return -1;
    }

    public CharSequence[] j() {
        return this.f3258k;
    }

    public CharSequence l() {
        CharSequence[] charSequenceArr;
        int o10 = o();
        if (o10 < 0 || (charSequenceArr = this.f3258k) == null) {
            return null;
        }
        return charSequenceArr[o10];
    }

    public CharSequence[] m() {
        return this.f3259l;
    }

    public String n() {
        return this.f3260m;
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
            p(savedState.f3263e);
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
        savedState.f3263e = n();
        return savedState;
    }

    @Override // androidx.preference.Preference
    protected void onSetInitialValue(Object obj) {
        p(getPersistedString((String) obj));
    }

    public void p(String str) {
        boolean z10 = !TextUtils.equals(this.f3260m, str);
        if (z10 || !this.f3262o) {
            this.f3260m = str;
            this.f3262o = true;
            persistString(str);
            if (z10) {
                notifyChanged();
            }
        }
    }

    @Override // androidx.preference.Preference
    public void setSummary(CharSequence charSequence) {
        super.setSummary(charSequence);
        if (charSequence == null && this.f3261n != null) {
            this.f3261n = null;
        } else {
            if (charSequence == null || charSequence.equals(this.f3261n)) {
                return;
            }
            this.f3261n = charSequence.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        String f3263e;

        /* loaded from: classes.dex */
        static class a implements Parcelable.Creator<SavedState> {
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
            this.f3263e = parcel.readString();
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeString(this.f3263e);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    public ListPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public ListPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.a(context, R$attr.dialogPreferenceStyle, R.attr.dialogPreferenceStyle));
    }
}
