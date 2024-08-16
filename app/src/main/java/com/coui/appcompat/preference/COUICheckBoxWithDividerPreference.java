package com.coui.appcompat.preference;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.checkbox.COUICheckBox;
import com.support.list.R$attr;
import com.support.list.R$dimen;
import com.support.list.R$id;
import com.support.list.R$style;
import com.support.list.R$styleable;

/* loaded from: classes.dex */
public class COUICheckBoxWithDividerPreference extends CheckBoxPreference implements COUICardSupportInterface, COUIRecyclerView.b {

    /* renamed from: e, reason: collision with root package name */
    private boolean f6918e;

    /* renamed from: f, reason: collision with root package name */
    private int f6919f;

    /* renamed from: g, reason: collision with root package name */
    private CharSequence f6920g;

    /* renamed from: h, reason: collision with root package name */
    private LinearLayout f6921h;

    /* renamed from: i, reason: collision with root package name */
    private LinearLayout f6922i;

    /* renamed from: j, reason: collision with root package name */
    private c f6923j;

    /* renamed from: k, reason: collision with root package name */
    private TextView f6924k;

    /* renamed from: l, reason: collision with root package name */
    private View f6925l;

    /* loaded from: classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUICheckBoxWithDividerPreference.this.f6923j != null) {
                COUICheckBoxWithDividerPreference.this.f6923j.a();
            }
        }
    }

    /* loaded from: classes.dex */
    class b implements View.OnClickListener {
        b() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUICheckBoxWithDividerPreference.super.onClick();
        }
    }

    /* loaded from: classes.dex */
    public interface c {
        void a();
    }

    public COUICheckBoxWithDividerPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiCheckBoxWithDividerPreferenceStyle);
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public boolean drawDivider() {
        if (!(this.f6925l instanceof COUICardListSelectedItemLayout)) {
            return false;
        }
        int b10 = COUICardListHelper.b(this);
        return b10 == 1 || b10 == 2;
    }

    public CharSequence getAssignment() {
        return this.f6920g;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    /* renamed from: getDividerEndInset */
    public int getMDividerDefaultHorizontalPadding() {
        return this.f6919f;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerStartAlignView() {
        return this.f6924k;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public int getDividerStartInset() {
        return this.f6919f;
    }

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public boolean isSupportCardUse() {
        return this.f6918e;
    }

    @Override // androidx.preference.CheckBoxPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.f6925l = preferenceViewHolder.itemView;
        View a10 = preferenceViewHolder.a(R.id.checkbox);
        View a11 = preferenceViewHolder.a(R.id.icon);
        View a12 = preferenceViewHolder.a(R$id.img_layout);
        if (a12 != null) {
            if (a11 != null) {
                a12.setVisibility(a11.getVisibility());
            } else {
                a12.setVisibility(8);
            }
        }
        if (a10 != null && (a10 instanceof COUICheckBox)) {
            ((COUICheckBox) a10).setState(isChecked() ? 2 : 0);
        }
        this.f6924k = (TextView) preferenceViewHolder.a(R.id.title);
        LinearLayout linearLayout = (LinearLayout) preferenceViewHolder.itemView.findViewById(R$id.main_layout);
        this.f6921h = linearLayout;
        if (linearLayout != null) {
            linearLayout.setOnClickListener(new a());
            this.f6921h.setClickable(isSelectable());
        }
        LinearLayout linearLayout2 = (LinearLayout) preferenceViewHolder.itemView.findViewById(R$id.check_box_layout);
        this.f6922i = linearLayout2;
        if (linearLayout2 != null) {
            linearLayout2.setOnClickListener(new b());
            this.f6922i.setClickable(isSelectable());
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

    public COUICheckBoxWithDividerPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Preference_COUI_COUICheckBoxWithDividerPreference);
    }

    public COUICheckBoxWithDividerPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUICheckBoxPreference, i10, i11);
        this.f6920g = obtainStyledAttributes.getText(R$styleable.COUICheckBoxPreference_couiCheckBoxAssignment);
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPreference, i10, i11);
        this.f6918e = obtainStyledAttributes2.getBoolean(R$styleable.COUIPreference_isSupportCardUse, true);
        obtainStyledAttributes2.recycle();
        this.f6919f = getContext().getResources().getDimensionPixelSize(R$dimen.coui_preference_divider_default_horizontal_padding);
    }
}
