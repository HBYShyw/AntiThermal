package com.coui.appcompat.preference;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.TextView;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.support.list.R$attr;
import com.support.list.R$dimen;
import com.support.list.R$id;
import com.support.list.R$style;
import com.support.list.R$styleable;

/* loaded from: classes.dex */
public class COUIMarkPreference extends CheckBoxPreference implements COUICardSupportInterface, COUIRecyclerView.b {
    public static final int CIRCLE = 0;
    static final int DEFAULT_RADIUS = 14;
    public static final int HEAD_MARK = 1;
    public static final int ROUND = 1;
    public static final int TAIL_MARK = 0;
    private CharSequence mAssignment;
    private int mDividerDefaultHorizontalPadding;
    private boolean mHasBorder;
    private int mIconStyle;
    private boolean mIsCustom;
    private boolean mIsEnableClickSpan;
    private boolean mIsSupportCardUse;
    private View mItemView;
    int mMarkStyle;
    private int mRadius;
    private boolean mShowDivider;
    private TextView mTitleView;

    public COUIMarkPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.mMarkStyle = 0;
        this.mShowDivider = true;
        this.mIsCustom = false;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIMarkPreference, i10, i11);
        this.mMarkStyle = obtainStyledAttributes.getInt(R$styleable.COUIMarkPreference_couiMarkStyle, 0);
        this.mAssignment = obtainStyledAttributes.getText(R$styleable.COUIMarkPreference_couiMarkAssignment);
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPreference, i10, i11);
        this.mShowDivider = obtainStyledAttributes2.getBoolean(R$styleable.COUIPreference_couiShowDivider, this.mShowDivider);
        this.mIsEnableClickSpan = obtainStyledAttributes2.getBoolean(R$styleable.COUIPreference_couiEnalbeClickSpan, false);
        this.mIsSupportCardUse = obtainStyledAttributes2.getBoolean(R$styleable.COUIPreference_isSupportCardUse, true);
        this.mIconStyle = obtainStyledAttributes2.getInt(R$styleable.COUIPreference_couiIconStyle, 1);
        this.mHasBorder = obtainStyledAttributes2.getBoolean(R$styleable.COUIPreference_hasBorder, false);
        this.mRadius = obtainStyledAttributes2.getDimensionPixelSize(R$styleable.COUIPreference_preference_icon_radius, 14);
        obtainStyledAttributes2.recycle();
        this.mDividerDefaultHorizontalPadding = getContext().getResources().getDimensionPixelSize(R$dimen.coui_preference_divider_default_horizontal_padding);
        setChecked(true);
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public boolean drawDivider() {
        if (!(this.mItemView instanceof COUICardListSelectedItemLayout)) {
            return false;
        }
        int b10 = COUICardListHelper.b(this);
        return b10 == 1 || b10 == 2;
    }

    public CharSequence getAssignment() {
        return this.mAssignment;
    }

    public int getBorderRectRadius(int i10) {
        return (i10 == 1 || i10 == 2 || i10 != 3) ? 14 : 16;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerEndAlignView() {
        return super.getDividerEndAlignView();
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public int getDividerEndInset() {
        return this.mDividerDefaultHorizontalPadding;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerStartAlignView() {
        return this.mTitleView;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public int getDividerStartInset() {
        return this.mDividerDefaultHorizontalPadding;
    }

    public int getMarkStyle() {
        return this.mMarkStyle;
    }

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public boolean isSupportCardUse() {
        return this.mIsSupportCardUse;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.preference.CheckBoxPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mItemView = preferenceViewHolder.itemView;
        View a10 = preferenceViewHolder.a(R$id.coui_tail_mark);
        if (a10 != 0 && (a10 instanceof Checkable)) {
            if (this.mMarkStyle == 0) {
                a10.setVisibility(0);
                ((Checkable) a10).setChecked(isChecked());
            } else {
                a10.setVisibility(8);
            }
        }
        View a11 = preferenceViewHolder.a(R$id.coui_head_mark);
        if (a11 != 0 && (a11 instanceof Checkable)) {
            if (this.mMarkStyle == 1) {
                a11.setVisibility(0);
                ((Checkable) a11).setChecked(isChecked());
            } else {
                a11.setVisibility(8);
            }
        }
        COUIPreferenceUtils.c(preferenceViewHolder, getContext(), this.mRadius, this.mHasBorder, this.mIconStyle, this.mIsCustom);
        this.mTitleView = (TextView) preferenceViewHolder.a(R.id.title);
        View a12 = preferenceViewHolder.a(R$id.img_layout);
        View a13 = preferenceViewHolder.a(R.id.icon);
        if (a12 != null) {
            if (a13 != null) {
                a12.setVisibility(a13.getVisibility());
            } else {
                a12.setVisibility(8);
            }
        }
        if (this.mIsEnableClickSpan) {
            COUIPreferenceUtils.d(getContext(), preferenceViewHolder);
        }
        TextView textView = (TextView) preferenceViewHolder.a(R$id.assignment);
        if (textView != null) {
            CharSequence assignment = getAssignment();
            if (!TextUtils.isEmpty(assignment)) {
                textView.setText(assignment);
                textView.setVisibility(0);
            } else {
                textView.setVisibility(8);
            }
        }
        COUICardListHelper.d(preferenceViewHolder.itemView, COUICardListHelper.b(this));
    }

    public void setAssignment(CharSequence charSequence) {
        if (TextUtils.equals(this.mAssignment, charSequence)) {
            return;
        }
        this.mAssignment = charSequence;
        notifyChanged();
    }

    public void setBorderRectRadius(int i10) {
        this.mRadius = i10;
        notifyChanged();
    }

    public void setIsCustomIconRadius(boolean z10) {
        this.mIsCustom = z10;
    }

    public void setIsEnableClickSpan(boolean z10) {
        this.mIsEnableClickSpan = z10;
    }

    public void setIsSupportCardUse(boolean z10) {
        this.mIsSupportCardUse = z10;
    }

    public void setMarkStyle(int i10) {
        this.mMarkStyle = i10;
    }

    public COUIMarkPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Preference_COUI_COUIMarkPreference);
    }

    public COUIMarkPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiMarkPreferenceStyle);
    }

    public COUIMarkPreference(Context context) {
        this(context, null);
    }
}
