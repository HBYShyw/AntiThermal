package com.coui.appcompat.buttonBar;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$id;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUIButtonBarLayout extends LinearLayout {
    private int A;
    private int B;
    private int C;
    private int D;
    private int E;
    private int F;
    private int G;
    private int H;
    private int I;
    private int J;
    private boolean K;
    private boolean L;
    private boolean M;
    private int N;
    private int O;
    private int P;
    private int Q;
    private int R;

    /* renamed from: e, reason: collision with root package name */
    private Context f5516e;

    /* renamed from: f, reason: collision with root package name */
    private Button f5517f;

    /* renamed from: g, reason: collision with root package name */
    private Button f5518g;

    /* renamed from: h, reason: collision with root package name */
    private Button f5519h;

    /* renamed from: i, reason: collision with root package name */
    private View f5520i;

    /* renamed from: j, reason: collision with root package name */
    private View f5521j;

    /* renamed from: k, reason: collision with root package name */
    private View f5522k;

    /* renamed from: l, reason: collision with root package name */
    private View f5523l;

    /* renamed from: m, reason: collision with root package name */
    private View f5524m;

    /* renamed from: n, reason: collision with root package name */
    private View f5525n;

    /* renamed from: o, reason: collision with root package name */
    private int f5526o;

    /* renamed from: p, reason: collision with root package name */
    private int f5527p;

    /* renamed from: q, reason: collision with root package name */
    private int f5528q;

    /* renamed from: r, reason: collision with root package name */
    private int f5529r;

    /* renamed from: s, reason: collision with root package name */
    private int f5530s;

    /* renamed from: t, reason: collision with root package name */
    private int f5531t;

    /* renamed from: u, reason: collision with root package name */
    private int f5532u;

    /* renamed from: v, reason: collision with root package name */
    private int f5533v;

    /* renamed from: w, reason: collision with root package name */
    private int f5534w;

    /* renamed from: x, reason: collision with root package name */
    private int f5535x;

    /* renamed from: y, reason: collision with root package name */
    private int f5536y;

    /* renamed from: z, reason: collision with root package name */
    private int f5537z;

    public COUIButtonBarLayout(Context context) {
        super(context, null);
        this.L = true;
        this.M = true;
    }

    private int a(Button button) {
        float measureText;
        if (button == null || button.getVisibility() != 0) {
            return 0;
        }
        if (button.isAllCaps()) {
            measureText = button.getPaint().measureText(button.getText().toString().toUpperCase());
        } else {
            measureText = button.getPaint().measureText(button.getText().toString());
        }
        return (int) measureText;
    }

    private boolean b(View view) {
        return view != null && view.getVisibility() == 0;
    }

    private void c(Context context, AttributeSet attributeSet) {
        this.f5516e = context;
        this.f5526o = context.getResources().getDimensionPixelSize(R$dimen.coui_alert_dialog_button_horizontal_padding);
        this.f5527p = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_alert_dialog_button_padding_top);
        this.f5528q = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_alert_dialog_button_padding_bottom);
        this.f5529r = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_alert_dialog_button_vertical_padding);
        this.B = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_alert_dialog_vertical_button_min_height);
        this.C = this.f5516e.getResources().getDimensionPixelSize(R$dimen.alert_dialog_item_padding_offset);
        this.f5530s = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_bottom_alert_dialog_vertical_button_padding_vertical);
        this.f5531t = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_center_alert_dialog_vertical_button_padding_vertical);
        this.f5532u = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_center_alert_dialog_vertical_button_padding_vertical_offset);
        this.f5533v = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_center_alert_dialog_vertical_button_paddingbottom_vertical_extra);
        this.F = this.f5516e.getResources().getDimensionPixelSize(R$dimen.alert_dialog_list_item_padding_left);
        this.G = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_alert_dialog_vertical_button_divider_vertical_margin);
        this.D = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_alert_dialog_vertical_button_divider_vertical_margin_top);
        this.E = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_alert_dialog_vertical_button_divider_vertical_margin_bottom);
        this.H = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_alert_dialog_horizontal_button_divider_vertical_margin);
        this.I = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_alert_dialog_button_height);
        this.f5535x = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_alert_dialog_neg_vertical_button_padding_vertical_top);
        this.f5536y = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_alert_dialog_neg_vertical_button_padding_vertical_bottom);
        this.A = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_delete_alert_dialog_divider_height_horizontalbutton);
        TypedArray obtainStyledAttributes = this.f5516e.obtainStyledAttributes(attributeSet, R$styleable.COUIButtonBarLayout);
        this.K = obtainStyledAttributes.getBoolean(R$styleable.COUIButtonBarLayout_forceVertical, false);
        this.f5534w = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUIButtonBarLayout_verNegButVerPaddingOffset, 0);
        this.L = obtainStyledAttributes.getBoolean(R$styleable.COUIButtonBarLayout_buttonBarShowDivider, true);
        this.f5537z = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUIButtonBarLayout_buttonBarDividerSize, this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_delete_alert_dialog_divider_height_verticalbutton));
        this.O = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_bottom_alert_dialog_vertical_button_padding_top_extra_new);
        this.P = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_bottom_alert_dialog_vertical_button_padding_bottom_extra_new);
        this.N = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_bottom_alert_dialog_vertical_button_padding_vertical_new);
        this.Q = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_bottom_alert_dialog_horizontal_button_padding_top_extra_new);
        this.R = this.f5516e.getResources().getDimensionPixelSize(R$dimen.coui_bottom_alert_dialog_horizontal_button_padding_bottom_extra_new);
        obtainStyledAttributes.recycle();
    }

    private void d() {
        if (this.f5517f == null || this.f5518g == null || this.f5519h == null || this.f5520i == null || this.f5521j == null || this.f5522k == null || this.f5523l == null || this.f5524m == null || this.f5525n == null) {
            this.f5517f = (Button) findViewById(R.id.button1);
            this.f5518g = (Button) findViewById(R.id.button2);
            this.f5519h = (Button) findViewById(R.id.button3);
            this.f5520i = findViewById(R$id.coui_dialog_button_divider_1);
            this.f5521j = findViewById(R$id.coui_dialog_button_divider_2);
            View view = (View) getParent().getParent();
            this.f5522k = view;
            this.f5523l = view.findViewById(R$id.topPanel);
            this.f5524m = this.f5522k.findViewById(R$id.contentPanel);
            this.f5525n = this.f5522k.findViewById(R$id.customPanel);
        }
    }

    private boolean e(int i10) {
        int buttonCount = getButtonCount();
        if (buttonCount == 0) {
            return false;
        }
        int i11 = ((i10 - ((buttonCount - 1) * this.f5537z)) / buttonCount) - (this.f5526o * 2);
        return a(this.f5517f) > i11 || a(this.f5518g) > i11 || a(this.f5519h) > i11;
    }

    private void f() {
        u(this.f5518g, this.Q);
        t(this.f5518g, this.R);
        u(this.f5517f, this.Q);
        t(this.f5517f, this.R);
        u(this.f5519h, this.Q);
        t(this.f5519h, this.R);
    }

    private void g() {
        if (getButtonCount() == 2) {
            if (b(this.f5517f)) {
                this.f5520i.setVisibility(8);
                o();
                return;
            } else {
                n();
                this.f5521j.setVisibility(8);
                return;
            }
        }
        if (getButtonCount() == 3) {
            n();
            o();
        } else {
            this.f5520i.setVisibility(8);
            this.f5521j.setVisibility(8);
        }
    }

    private void h() {
        if (b(this.f5518g)) {
            if (!b(this.f5517f) && !b(this.f5519h) && !b(this.f5523l) && !b(this.f5524m) && !b(this.f5525n)) {
                u(this.f5518g, this.N + this.O);
            }
            t(this.f5518g, this.N + this.P);
        }
        if (b(this.f5517f)) {
            if (!b(this.f5519h) && !b(this.f5523l) && !b(this.f5524m) && !b(this.f5525n)) {
                u(this.f5517f, this.N + this.O);
            }
            if (!b(this.f5518g)) {
                t(this.f5517f, this.N + this.P);
            }
        }
        if (b(this.f5519h)) {
            if (!b(this.f5523l) && !b(this.f5524m) && !b(this.f5525n)) {
                u(this.f5519h, this.N + this.O);
            }
            if (b(this.f5518g) || b(this.f5517f)) {
                return;
            }
            t(this.f5519h, this.N + this.P);
        }
    }

    private void i() {
        if (getButtonCount() != 0) {
            if (b(this.f5518g)) {
                if (b(this.f5519h) && b(this.f5517f)) {
                    n();
                    o();
                    return;
                } else if (!b(this.f5519h) && !b(this.f5517f) && !b(this.f5523l) && !b(this.f5524m) && !b(this.f5525n)) {
                    this.f5520i.setVisibility(8);
                    this.f5521j.setVisibility(8);
                    return;
                } else {
                    this.f5520i.setVisibility(8);
                    o();
                    return;
                }
            }
            if (b(this.f5519h) && b(this.f5517f)) {
                n();
                return;
            } else {
                this.f5520i.setVisibility(8);
                this.f5521j.setVisibility(8);
                return;
            }
        }
        this.f5520i.setVisibility(8);
        this.f5521j.setVisibility(8);
    }

    private void j() {
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), this.J);
    }

    private void k(Button button, Boolean bool) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.weight = 1.0f;
        layoutParams.width = 0;
        layoutParams.height = -1;
        button.setLayoutParams(layoutParams);
        int i10 = this.f5526o;
        button.setPaddingRelative(i10, this.f5527p, i10, this.f5528q);
        button.setMinHeight(0);
        if (bool.booleanValue()) {
            bringChildToFront(button);
        }
    }

    private void l() {
        setOrientation(0);
        p();
        Button button = this.f5519h;
        Boolean bool = Boolean.TRUE;
        k(button, bool);
        q();
        k(this.f5517f, bool);
        k(this.f5518g, Boolean.FALSE);
    }

    private void m() {
        setOrientation(1);
        setMinimumHeight(0);
        s();
        w();
        v();
        x();
        r();
    }

    private void n() {
        if (this.L) {
            this.f5520i.setVisibility(0);
        } else {
            this.f5520i.setVisibility(8);
        }
    }

    private void o() {
        if (this.L) {
            this.f5521j.setVisibility(0);
        } else {
            this.f5521j.setVisibility(8);
        }
    }

    private void p() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f5520i.getLayoutParams();
        layoutParams.width = this.A;
        layoutParams.height = -1;
        layoutParams.setMarginStart(0);
        layoutParams.setMarginEnd(0);
        int i10 = this.H;
        layoutParams.topMargin = i10;
        layoutParams.bottomMargin = i10;
        this.f5520i.setLayoutParams(layoutParams);
        bringChildToFront(this.f5520i);
    }

    private void q() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f5521j.getLayoutParams();
        layoutParams.width = this.A;
        layoutParams.height = -1;
        layoutParams.setMarginStart(0);
        layoutParams.setMarginEnd(0);
        int i10 = this.H;
        layoutParams.topMargin = i10;
        layoutParams.bottomMargin = i10;
        this.f5521j.setLayoutParams(layoutParams);
        bringChildToFront(this.f5521j);
    }

    private void r() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f5518g.getLayoutParams();
        layoutParams.weight = 0.0f;
        layoutParams.width = -1;
        layoutParams.height = -2;
        this.f5518g.setLayoutParams(layoutParams);
        Button button = this.f5518g;
        int i10 = this.f5529r;
        int i11 = this.N;
        button.setPaddingRelative(i10, i11, i10, i11);
    }

    private void s() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f5519h.getLayoutParams();
        layoutParams.weight = 0.0f;
        layoutParams.width = -1;
        layoutParams.height = -2;
        this.f5519h.setLayoutParams(layoutParams);
        Button button = this.f5519h;
        int i10 = this.f5529r;
        int i11 = this.N;
        button.setPaddingRelative(i10, i11, i10, i11);
    }

    private void t(View view, int i10) {
        view.setPaddingRelative(view.getPaddingStart(), view.getPaddingTop(), view.getPaddingEnd(), i10);
    }

    private void u(View view, int i10) {
        view.setPaddingRelative(view.getPaddingStart(), i10, view.getPaddingEnd(), view.getPaddingBottom());
    }

    private void v() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f5517f.getLayoutParams();
        layoutParams.weight = 0.0f;
        layoutParams.width = -1;
        layoutParams.height = -2;
        this.f5517f.setLayoutParams(layoutParams);
        Button button = this.f5517f;
        int i10 = this.f5529r;
        int i11 = this.N;
        button.setPaddingRelative(i10, i11, i10, i11);
    }

    private void w() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f5520i.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = this.f5537z;
        layoutParams.setMarginStart(this.F);
        layoutParams.setMarginEnd(this.F);
        layoutParams.topMargin = 0;
        layoutParams.bottomMargin = 0;
        this.f5520i.setLayoutParams(layoutParams);
    }

    private void x() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f5521j.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = this.f5537z;
        layoutParams.setMarginStart(this.F);
        layoutParams.setMarginEnd(this.F);
        layoutParams.topMargin = 0;
        layoutParams.bottomMargin = 0;
        this.f5521j.setLayoutParams(layoutParams);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [boolean, int] */
    public int getButtonCount() {
        d();
        ?? b10 = b(this.f5517f);
        int i10 = b10;
        if (b(this.f5518g)) {
            i10 = b10 + 1;
        }
        return b(this.f5519h) ? i10 + 1 : i10;
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        d();
        if (!this.K && (!this.M || !e(getMeasuredWidth()))) {
            l();
            f();
            g();
            super.onMeasure(i10, i11);
            return;
        }
        m();
        h();
        i();
        j();
        super.onMeasure(i10, i11);
    }

    public void setDynamicLayout(boolean z10) {
        this.M = z10;
    }

    public void setForceVertical(boolean z10) {
        this.K = z10;
    }

    public void setVerButDividerVerMargin(int i10) {
        this.G = i10;
    }

    public void setVerButPaddingOffset(int i10) {
        this.C = i10;
        this.D = i10;
        this.E = i10;
    }

    public void setVerButVerPadding(int i10) {
        this.f5530s = i10;
    }

    public void setVerNegButVerPaddingOffset(int i10) {
        this.f5534w = i10;
    }

    public void setVerPaddingBottom(int i10) {
        this.J = i10;
    }

    public COUIButtonBarLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIButtonBarLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.L = true;
        this.M = true;
        c(context, attributeSet);
    }
}
