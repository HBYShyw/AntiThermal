package com.coui.appcompat.preference;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R$attr;
import androidx.preference.SwitchPreference;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.couiswitch.COUISwitch;
import com.coui.appcompat.reddot.COUIHintRedDot;
import com.support.list.R$dimen;
import com.support.list.R$id;
import com.support.list.R$styleable;

/* loaded from: classes.dex */
public class COUISwitchPreference extends SwitchPreference implements COUICardSupportInterface, COUIRecyclerView.b {

    /* renamed from: h, reason: collision with root package name */
    private final b f7018h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f7019i;

    /* renamed from: j, reason: collision with root package name */
    private COUISwitch f7020j;

    /* renamed from: k, reason: collision with root package name */
    private int f7021k;

    /* renamed from: l, reason: collision with root package name */
    private int f7022l;

    /* renamed from: m, reason: collision with root package name */
    private int f7023m;

    /* renamed from: n, reason: collision with root package name */
    private CharSequence f7024n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f7025o;

    /* renamed from: p, reason: collision with root package name */
    private CharSequence f7026p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f7027q;

    /* renamed from: r, reason: collision with root package name */
    private int f7028r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f7029s;

    /* renamed from: t, reason: collision with root package name */
    private int f7030t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f7031u;

    /* renamed from: v, reason: collision with root package name */
    private int f7032v;

    /* renamed from: w, reason: collision with root package name */
    private TextView f7033w;

    /* renamed from: x, reason: collision with root package name */
    private View f7034x;

    /* loaded from: classes.dex */
    private class b implements CompoundButton.OnCheckedChangeListener {
        private b() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z10) {
            if (COUISwitchPreference.this.isChecked() == z10) {
                return;
            }
            if (!COUISwitchPreference.this.g(Boolean.valueOf(z10))) {
                compoundButton.setChecked(!z10);
            } else {
                COUISwitchPreference.this.setChecked(z10);
            }
        }
    }

    public COUISwitchPreference(Context context) {
        this(context, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean g(Object obj) {
        if (getOnPreferenceChangeListener() == null) {
            return true;
        }
        return getOnPreferenceChangeListener().onPreferenceChange(this, obj);
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public boolean drawDivider() {
        if (!(this.f7034x instanceof COUICardListSelectedItemLayout)) {
            return false;
        }
        int b10 = COUICardListHelper.b(this);
        return b10 == 1 || b10 == 2;
    }

    public CharSequence getAssignment() {
        return this.f7024n;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerEndAlignView() {
        return super.getDividerEndAlignView();
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public int getDividerEndInset() {
        return this.f7023m;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerStartAlignView() {
        return this.f7033w;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public int getDividerStartInset() {
        return this.f7023m;
    }

    public void h(boolean z10) {
        COUISwitch cOUISwitch = this.f7020j;
        if (cOUISwitch != null) {
            cOUISwitch.setTactileFeedbackEnabled(z10);
        }
    }

    public void i(boolean z10) {
        COUISwitch cOUISwitch = this.f7020j;
        if (cOUISwitch != null) {
            cOUISwitch.setShouldPlaySound(z10);
        }
    }

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public boolean isSupportCardUse() {
        return this.f7027q;
    }

    @Override // androidx.preference.SwitchPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        this.f7034x = preferenceViewHolder.itemView;
        View a10 = preferenceViewHolder.a(R$id.coui_preference);
        if (a10 != null) {
            a10.setSoundEffectsEnabled(false);
            a10.setHapticFeedbackEnabled(false);
        }
        View a11 = preferenceViewHolder.a(R.id.switch_widget);
        View a12 = preferenceViewHolder.a(R$id.jump_icon_red_dot);
        if (a11 instanceof COUISwitch) {
            COUISwitch cOUISwitch = (COUISwitch) a11;
            cOUISwitch.setOnCheckedChangeListener(this.f7018h);
            cOUISwitch.setVerticalScrollBarEnabled(false);
            this.f7020j = cOUISwitch;
            int i10 = this.f7032v;
            if (i10 != -1) {
                cOUISwitch.setBarCheckedColor(i10);
            }
        }
        super.onBindViewHolder(preferenceViewHolder);
        if (this.f7019i) {
            COUIPreferenceUtils.d(getContext(), preferenceViewHolder);
        }
        COUIPreferenceUtils.c(preferenceViewHolder, getContext(), this.f7030t, this.f7029s, this.f7028r, this.f7031u);
        View a13 = preferenceViewHolder.a(R$id.img_layout);
        View findViewById = preferenceViewHolder.itemView.findViewById(R.id.icon);
        if (a13 != null) {
            if (findViewById != null) {
                a13.setVisibility(findViewById.getVisibility());
            } else {
                a13.setVisibility(8);
            }
        }
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(R$id.assignment);
        if (textView != null) {
            CharSequence assignment = getAssignment();
            if (!TextUtils.isEmpty(assignment)) {
                textView.setText(assignment);
                textView.setVisibility(0);
            } else {
                textView.setVisibility(8);
            }
        }
        TextView textView2 = (TextView) preferenceViewHolder.a(R.id.title);
        this.f7033w = textView2;
        textView2.setText(this.f7026p);
        if (a12 != null) {
            if (this.f7025o) {
                COUIHintRedDot cOUIHintRedDot = (COUIHintRedDot) a12;
                cOUIHintRedDot.c();
                a12.setVisibility(0);
                cOUIHintRedDot.setPointMode(1);
            } else {
                a12.setVisibility(8);
            }
            a12.invalidate();
        }
        COUICardListHelper.d(preferenceViewHolder.itemView, COUICardListHelper.b(this));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.TwoStatePreference, androidx.preference.Preference
    public void onClick() {
        i(true);
        h(true);
        super.onClick();
    }

    public void setIsCustomIconRadius(boolean z10) {
        this.f7031u = z10;
    }

    @Override // androidx.preference.Preference
    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        this.f7026p = getTitle();
    }

    public COUISwitchPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.switchPreferenceStyle);
    }

    public COUISwitchPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public COUISwitchPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f7018h = new b();
        this.f7031u = false;
        this.f7032v = -1;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPreference, i10, i11);
        this.f7019i = obtainStyledAttributes.getBoolean(R$styleable.COUIPreference_couiEnalbeClickSpan, false);
        this.f7024n = obtainStyledAttributes.getText(R$styleable.COUIPreference_couiAssignment);
        this.f7027q = obtainStyledAttributes.getBoolean(R$styleable.COUIPreference_isSupportCardUse, true);
        this.f7028r = obtainStyledAttributes.getInt(R$styleable.COUIPreference_couiIconStyle, 1);
        this.f7029s = obtainStyledAttributes.getBoolean(R$styleable.COUIPreference_hasBorder, false);
        this.f7030t = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIPreference_preference_icon_radius, 14);
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R$styleable.COUISwitchPreference, i10, i11);
        this.f7025o = obtainStyledAttributes2.getBoolean(R$styleable.COUISwitchPreference_hasTitleRedDot, false);
        obtainStyledAttributes2.recycle();
        this.f7023m = getContext().getResources().getDimensionPixelSize(R$dimen.coui_preference_divider_default_horizontal_padding);
        this.f7026p = getTitle();
        this.f7021k = context.getResources().getDimensionPixelOffset(com.support.appcompat.R$dimen.coui_dot_diameter_small);
        this.f7022l = context.getResources().getDimensionPixelOffset(com.support.appcompat.R$dimen.coui_switch_preference_dot_margin_start);
    }
}
