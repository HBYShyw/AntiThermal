package com.coui.appcompat.preference;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.edittext.COUIEditText;
import com.coui.appcompat.edittext.COUIInputView;
import com.coui.appcompat.edittext.COUIScrolledEditText;
import com.support.list.R$attr;
import com.support.list.R$dimen;
import com.support.list.R$id;
import com.support.list.R$style;
import com.support.list.R$styleable;

/* loaded from: classes.dex */
public class COUIInputPreference extends COUIPreference {
    private COUIEditText F;
    private COUICardListItemInputView G;
    private CharSequence H;
    private CharSequence I;
    private View J;
    private boolean K;
    private int L;

    public COUIInputPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiInputPreferenceStyle);
    }

    public void h(CharSequence charSequence) {
        COUIEditText cOUIEditText = this.F;
        if (cOUIEditText != null) {
            cOUIEditText.setCouiEditTexttNoEllipsisText((String) charSequence);
            this.H = charSequence;
            return;
        }
        if (!TextUtils.equals(this.H, charSequence)) {
            notifyChanged();
        }
        boolean shouldDisableDependents = shouldDisableDependents();
        this.H = charSequence;
        if (charSequence != null) {
            persistString(charSequence.toString());
        }
        boolean shouldDisableDependents2 = shouldDisableDependents();
        if (shouldDisableDependents2 != shouldDisableDependents) {
            notifyDependencyChange(shouldDisableDependents2);
        }
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        this.J = view;
        ViewGroup viewGroup = (ViewGroup) view.findViewById(R$id.edittext_container);
        if (viewGroup != null) {
            if (!this.G.equals((COUICardListItemInputView) viewGroup.findViewById(R.id.input))) {
                ViewParent parent = this.G.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(this.G);
                }
                viewGroup.removeAllViews();
                viewGroup.addView(this.G, -1, -2);
                if (this.K) {
                    viewGroup.setPaddingRelative(viewGroup.getPaddingStart(), viewGroup.getPaddingTop(), viewGroup.getPaddingEnd(), this.L);
                    this.G.getEditText().setBoxBackgroundMode(3);
                }
            }
        }
        this.G.setEnabled(isEnabled());
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
            h(savedState.f6941e);
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
        CharSequence charSequence = this.H;
        if (charSequence != null) {
            savedState.f6941e = charSequence.toString();
        }
        return savedState;
    }

    @Override // androidx.preference.Preference
    protected void onSetInitialValue(boolean z10, Object obj) {
        String str;
        if (TextUtils.isEmpty(this.H)) {
            return;
        }
        if (z10) {
            str = getPersistedString(this.H.toString());
        } else {
            str = (String) obj;
        }
        h(str);
    }

    @Override // androidx.preference.Preference
    public boolean shouldDisableDependents() {
        return TextUtils.isEmpty(this.H) || super.shouldDisableDependents();
    }

    /* loaded from: classes.dex */
    public static class COUICardListItemInputView extends COUIInputView {

        /* renamed from: c0, reason: collision with root package name */
        private boolean f6940c0;

        public COUICardListItemInputView(Context context) {
            super(context);
            this.f6940c0 = false;
        }

        @Override // com.coui.appcompat.edittext.COUIInputView
        protected COUIEditText S(Context context, AttributeSet attributeSet) {
            return new COUIScrolledEditText(context, attributeSet, com.support.appcompat.R$attr.couiInputPreferenceEditTextStyle);
        }

        @Override // com.coui.appcompat.edittext.COUIInputView
        protected int getHasTitlePaddingBottomDimen() {
            return this.f6940c0 ? R$dimen.coui_input_edit_text_has_title_preference_padding_bottom_last_card : R$dimen.coui_input_edit_text_has_title_preference_padding_bottom;
        }

        public COUICardListItemInputView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f6940c0 = false;
        }

        public COUICardListItemInputView(Context context, AttributeSet attributeSet, int i10) {
            super(context, attributeSet, i10);
            this.f6940c0 = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        String f6941e;

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

        public SavedState(Parcel parcel) {
            super(parcel);
            this.f6941e = parcel.readString();
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeString(this.f6941e);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    public COUIInputPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Preference_COUI_COUIInputPreference);
    }

    public COUIInputPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIInputPreference, i10, i11);
        this.H = obtainStyledAttributes.getText(R$styleable.COUIInputPreference_couiContent);
        this.K = obtainStyledAttributes.getBoolean(R$styleable.COUIInputPreference_couiIsLastCard, false);
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, androidx.preference.R$styleable.Preference, i10, i11);
        this.I = obtainStyledAttributes2.getText(androidx.preference.R$styleable.Preference_android_title);
        obtainStyledAttributes2.recycle();
        this.L = context.getResources().getDimensionPixelSize(R$dimen.coui_input_preference_button_layout_padding_bottom);
        COUICardListItemInputView cOUICardListItemInputView = new COUICardListItemInputView(context, attributeSet);
        this.G = cOUICardListItemInputView;
        cOUICardListItemInputView.f6940c0 = this.K;
        this.G.setId(R.id.input);
        this.G.setTitle(this.I);
        this.F = this.G.getEditText();
    }
}
