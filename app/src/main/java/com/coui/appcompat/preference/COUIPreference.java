package com.coui.appcompat.preference;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R$attr;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.reddot.COUIHintRedDot;
import com.support.list.R$dimen;
import com.support.list.R$id;
import com.support.list.R$styleable;
import g2.PreciseClickHelper;

/* loaded from: classes.dex */
public class COUIPreference extends Preference implements COUICardSupportInterface, COUIRecyclerView.b {
    private boolean A;
    protected PreciseClickHelper.c B;
    private PreciseClickHelper C;
    private ColorStateList D;
    private ColorStateList E;

    /* renamed from: e, reason: collision with root package name */
    private Context f6972e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f6973f;

    /* renamed from: g, reason: collision with root package name */
    private int f6974g;

    /* renamed from: h, reason: collision with root package name */
    private int f6975h;

    /* renamed from: i, reason: collision with root package name */
    private int f6976i;

    /* renamed from: j, reason: collision with root package name */
    private int f6977j;

    /* renamed from: k, reason: collision with root package name */
    private View f6978k;

    /* renamed from: l, reason: collision with root package name */
    private View f6979l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f6980m;

    /* renamed from: n, reason: collision with root package name */
    private CharSequence f6981n;

    /* renamed from: o, reason: collision with root package name */
    private int f6982o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f6983p;

    /* renamed from: q, reason: collision with root package name */
    private int f6984q;

    /* renamed from: r, reason: collision with root package name */
    private int f6985r;

    /* renamed from: s, reason: collision with root package name */
    CharSequence f6986s;

    /* renamed from: t, reason: collision with root package name */
    Drawable f6987t;

    /* renamed from: u, reason: collision with root package name */
    private int f6988u;

    /* renamed from: v, reason: collision with root package name */
    private View f6989v;

    /* renamed from: w, reason: collision with root package name */
    private View f6990w;

    /* renamed from: x, reason: collision with root package name */
    private boolean f6991x;

    /* renamed from: y, reason: collision with root package name */
    private boolean f6992y;

    /* renamed from: z, reason: collision with root package name */
    private boolean f6993z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements PreciseClickHelper.c {
        a() {
        }

        @Override // g2.PreciseClickHelper.c
        public void a(View view, int i10, int i11) {
            COUIPreference.this.B.a(view, i10, i11);
        }
    }

    public COUIPreference(Context context) {
        this(context, null);
    }

    private void c() {
        if (this.f6989v == null || this.B == null) {
            return;
        }
        d();
        PreciseClickHelper preciseClickHelper = new PreciseClickHelper(this.f6989v, new a());
        this.C = preciseClickHelper;
        preciseClickHelper.c();
    }

    public void d() {
        PreciseClickHelper preciseClickHelper = this.C;
        if (preciseClickHelper != null) {
            preciseClickHelper.d();
            this.C = null;
        }
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public boolean drawDivider() {
        if (!(this.f6989v instanceof COUICardListSelectedItemLayout)) {
            return false;
        }
        int b10 = COUICardListHelper.b(this);
        return b10 == 1 || b10 == 2;
    }

    public void e(Drawable drawable) {
        if (this.f6987t != drawable) {
            this.f6987t = drawable;
            notifyChanged();
        }
    }

    public void f(boolean z10) {
        if (this.f6973f != z10) {
            this.f6973f = z10;
            notifyChanged();
        }
    }

    public void g(CharSequence charSequence) {
        if ((charSequence != null || this.f6986s == null) && (charSequence == null || charSequence.equals(this.f6986s))) {
            return;
        }
        this.f6986s = charSequence;
        notifyChanged();
    }

    public CharSequence getAssignment() {
        return this.f6981n;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerEndAlignView() {
        return super.getDividerEndAlignView();
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public int getDividerEndInset() {
        return this.f6977j;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerStartAlignView() {
        return this.f6990w;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public int getDividerStartInset() {
        return this.f6977j;
    }

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public boolean isSupportCardUse() {
        return this.f6993z;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        COUICardListHelper.d(preferenceViewHolder.itemView, COUICardListHelper.b(this));
        View a10 = preferenceViewHolder.a(R$id.coui_preference);
        if (a10 != null) {
            int i10 = this.f6988u;
            if (i10 == 1) {
                a10.setClickable(false);
            } else if (i10 == 2) {
                a10.setClickable(true);
            }
        }
        this.f6989v = preferenceViewHolder.itemView;
        c();
        View view = this.f6989v;
        if (view != null) {
            if (view instanceof ListSelectedItemLayout) {
                ((ListSelectedItemLayout) view).setBackgroundAnimationEnabled(this.f6992y);
            }
            View view2 = this.f6989v;
            if (view2 instanceof COUICardListSelectedItemLayout) {
                ((COUICardListSelectedItemLayout) view2).setIsSelected(this.f6991x);
            }
        }
        if (this.f6985r == 0) {
            COUIPreferenceUtils.a(preferenceViewHolder, this.f6987t, this.f6986s, getAssignment());
        } else {
            COUIPreferenceUtils.b(preferenceViewHolder, this.f6987t, this.f6986s, getAssignment(), this.f6985r);
        }
        COUIPreferenceUtils.f(getContext(), preferenceViewHolder, this.D);
        COUIPreferenceUtils.c(preferenceViewHolder, getContext(), this.f6984q, this.f6983p, this.f6982o, this.A);
        COUIPreferenceUtils.e(preferenceViewHolder, this.E);
        if (this.f6980m) {
            COUIPreferenceUtils.d(getContext(), preferenceViewHolder);
        }
        this.f6990w = preferenceViewHolder.a(R.id.title);
        View a11 = preferenceViewHolder.a(R$id.img_layout);
        View a12 = preferenceViewHolder.a(R.id.icon);
        if (a11 != null) {
            if (a12 != null) {
                a11.setVisibility(a12.getVisibility());
            } else {
                a11.setVisibility(8);
            }
        }
        this.f6978k = preferenceViewHolder.a(R$id.img_red_dot);
        this.f6979l = preferenceViewHolder.a(R$id.jump_icon_red_dot);
        View view3 = this.f6978k;
        if (view3 instanceof COUIHintRedDot) {
            if (this.f6974g != 0) {
                ((COUIHintRedDot) view3).c();
                this.f6978k.setVisibility(0);
                ((COUIHintRedDot) this.f6978k).setPointMode(this.f6974g);
                this.f6978k.invalidate();
            } else {
                view3.setVisibility(8);
            }
        }
        View view4 = this.f6979l;
        if (view4 instanceof COUIHintRedDot) {
            if (this.f6975h != 0) {
                ((COUIHintRedDot) view4).c();
                this.f6979l.setVisibility(0);
                ((COUIHintRedDot) this.f6979l).setPointMode(this.f6975h);
                ((COUIHintRedDot) this.f6979l).setPointNumber(this.f6976i);
                this.f6979l.invalidate();
                return;
            }
            view4.setVisibility(8);
        }
    }

    @Override // androidx.preference.Preference
    public void onDetached() {
        d();
        super.onDetached();
    }

    public void setAssignment(CharSequence charSequence) {
        if (TextUtils.equals(this.f6981n, charSequence)) {
            return;
        }
        this.f6981n = charSequence;
        notifyChanged();
    }

    public void setIsCustomIconRadius(boolean z10) {
        this.A = z10;
    }

    public COUIPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.preferenceStyle);
    }

    public COUIPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public COUIPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f6973f = true;
        this.f6988u = 0;
        this.f6991x = false;
        this.f6992y = true;
        this.A = false;
        this.D = null;
        this.E = null;
        this.f6972e = context;
        this.f6977j = context.getResources().getDimensionPixelSize(R$dimen.coui_preference_divider_default_horizontal_padding);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPreference, i10, i11);
        this.f6973f = obtainStyledAttributes.getBoolean(R$styleable.COUIPreference_couiShowDivider, this.f6973f);
        this.f6980m = obtainStyledAttributes.getBoolean(R$styleable.COUIPreference_couiEnalbeClickSpan, false);
        this.f6987t = obtainStyledAttributes.getDrawable(R$styleable.COUIPreference_coui_jump_mark);
        this.f6986s = obtainStyledAttributes.getText(R$styleable.COUIPreference_coui_jump_status1);
        this.f6988u = obtainStyledAttributes.getInt(R$styleable.COUIPreference_couiClickStyle, 0);
        this.f6981n = obtainStyledAttributes.getText(R$styleable.COUIPreference_couiAssignment);
        this.f6985r = obtainStyledAttributes.getInt(R$styleable.COUIPreference_couiAssignmentColor, 0);
        this.f6982o = obtainStyledAttributes.getInt(R$styleable.COUIPreference_couiIconStyle, 1);
        this.f6983p = obtainStyledAttributes.getBoolean(R$styleable.COUIPreference_hasBorder, false);
        this.f6984q = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIPreference_preference_icon_radius, 14);
        this.f6974g = obtainStyledAttributes.getInt(R$styleable.COUIPreference_iconRedDotMode, 0);
        this.f6975h = obtainStyledAttributes.getInt(R$styleable.COUIPreference_endRedDotMode, 0);
        this.f6976i = obtainStyledAttributes.getInt(R$styleable.COUIPreference_endRedDotNum, 0);
        this.f6992y = obtainStyledAttributes.getBoolean(R$styleable.COUIPreference_isBackgroundAnimationEnabled, true);
        this.f6993z = obtainStyledAttributes.getBoolean(R$styleable.COUIPreference_isSupportCardUse, true);
        this.D = obtainStyledAttributes.getColorStateList(R$styleable.COUIPreference_titleTextColor);
        this.E = obtainStyledAttributes.getColorStateList(R$styleable.COUIPreference_couiSummaryColor);
        obtainStyledAttributes.recycle();
    }
}
