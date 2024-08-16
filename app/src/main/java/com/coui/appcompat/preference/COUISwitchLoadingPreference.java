package com.coui.appcompat.preference;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreferenceCompat;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.couiswitch.COUISwitch;
import com.support.list.R$attr;
import com.support.list.R$dimen;
import com.support.list.R$id;
import com.support.list.R$style;
import com.support.list.R$styleable;

/* loaded from: classes.dex */
public class COUISwitchLoadingPreference extends SwitchPreferenceCompat implements COUICardSupportInterface, COUIRecyclerView.b {

    /* renamed from: h, reason: collision with root package name */
    View f7008h;

    /* renamed from: i, reason: collision with root package name */
    private int f7009i;

    /* renamed from: j, reason: collision with root package name */
    private TextView f7010j;

    /* renamed from: k, reason: collision with root package name */
    private View f7011k;

    /* renamed from: l, reason: collision with root package name */
    private COUISwitch f7012l;

    /* renamed from: m, reason: collision with root package name */
    private final b f7013m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f7014n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f7015o;

    /* renamed from: p, reason: collision with root package name */
    private COUISwitch.b f7016p;

    /* loaded from: classes.dex */
    private class b implements CompoundButton.OnCheckedChangeListener {
        private b() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z10) {
            if (!COUISwitchLoadingPreference.this.g(Boolean.valueOf(z10))) {
                compoundButton.setChecked(!z10);
            } else {
                COUISwitchLoadingPreference.this.setChecked(z10);
            }
        }
    }

    public COUISwitchLoadingPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiSwitchLoadPreferenceStyle);
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
        if (!(this.f7011k instanceof COUICardListSelectedItemLayout)) {
            return false;
        }
        int b10 = COUICardListHelper.b(this);
        return b10 == 1 || b10 == 2;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerEndAlignView() {
        return super.getDividerEndAlignView();
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    /* renamed from: getDividerEndInset */
    public int getMDividerDefaultHorizontalPadding() {
        return this.f7009i;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerStartAlignView() {
        return this.f7010j;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public int getDividerStartInset() {
        return this.f7009i;
    }

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public boolean isSupportCardUse() {
        return this.f7015o;
    }

    @Override // androidx.preference.SwitchPreferenceCompat, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        this.f7011k = preferenceViewHolder.itemView;
        View a10 = preferenceViewHolder.a(R$id.coui_preference);
        if (a10 != null) {
            a10.setSoundEffectsEnabled(false);
            a10.setHapticFeedbackEnabled(false);
        }
        View a11 = preferenceViewHolder.a(R$id.switchWidget);
        this.f7008h = a11;
        if (a11 instanceof COUISwitch) {
            COUISwitch cOUISwitch = (COUISwitch) a11;
            cOUISwitch.setOnCheckedChangeListener(null);
            cOUISwitch.setVerticalScrollBarEnabled(false);
            this.f7012l = cOUISwitch;
        }
        super.onBindViewHolder(preferenceViewHolder);
        View view = this.f7008h;
        if (view instanceof COUISwitch) {
            COUISwitch cOUISwitch2 = (COUISwitch) view;
            cOUISwitch2.setLoadingStyle(true);
            cOUISwitch2.setOnLoadingStateChangedListener(this.f7016p);
            cOUISwitch2.setOnCheckedChangeListener(this.f7013m);
        }
        if (this.f7014n) {
            COUIPreferenceUtils.d(getContext(), preferenceViewHolder);
        }
        this.f7010j = (TextView) preferenceViewHolder.a(R.id.title);
        View findViewById = preferenceViewHolder.itemView.findViewById(R.id.icon);
        View a12 = preferenceViewHolder.a(R$id.img_layout);
        if (a12 != null) {
            if (findViewById != null) {
                a12.setVisibility(findViewById.getVisibility());
            } else {
                a12.setVisibility(8);
            }
        }
        COUICardListHelper.d(preferenceViewHolder.itemView, COUICardListHelper.b(this));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.TwoStatePreference, androidx.preference.Preference
    public void onClick() {
        COUISwitch cOUISwitch = this.f7012l;
        if (cOUISwitch != null) {
            cOUISwitch.setShouldPlaySound(true);
            this.f7012l.setTactileFeedbackEnabled(true);
            this.f7012l.Q();
        }
    }

    public COUISwitchLoadingPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Preference_COUI_SwitchPreference_Loading);
    }

    public COUISwitchLoadingPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f7013m = new b();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPreference, i10, 0);
        this.f7014n = obtainStyledAttributes.getBoolean(R$styleable.COUIPreference_couiEnalbeClickSpan, false);
        this.f7015o = obtainStyledAttributes.getBoolean(R$styleable.COUIPreference_isSupportCardUse, true);
        obtainStyledAttributes.recycle();
        this.f7009i = getContext().getResources().getDimensionPixelSize(R$dimen.coui_preference_divider_default_horizontal_padding);
    }
}
