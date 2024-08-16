package com.coui.appcompat.reddot;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.coui.appcompat.reddot.COUIRedDotFrameLayout;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUIRedDotFrameLayout extends FrameLayout {

    /* renamed from: e, reason: collision with root package name */
    String f7293e;

    /* renamed from: f, reason: collision with root package name */
    private int f7294f;

    /* renamed from: g, reason: collision with root package name */
    private String f7295g;

    /* renamed from: h, reason: collision with root package name */
    private int f7296h;

    /* renamed from: i, reason: collision with root package name */
    private int f7297i;

    /* renamed from: j, reason: collision with root package name */
    private int f7298j;

    /* renamed from: k, reason: collision with root package name */
    private int f7299k;

    /* renamed from: l, reason: collision with root package name */
    private int f7300l;

    /* renamed from: m, reason: collision with root package name */
    private View f7301m;

    /* renamed from: n, reason: collision with root package name */
    private COUIHintRedDot f7302n;

    /* renamed from: o, reason: collision with root package name */
    private int f7303o;

    /* renamed from: p, reason: collision with root package name */
    private final Runnable f7304p;

    public COUIRedDotFrameLayout(Context context) {
        this(context, null);
    }

    private void c() {
        if (this.f7294f != 0) {
            final COUIHintRedDot cOUIHintRedDot = new COUIHintRedDot(getContext());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
            cOUIHintRedDot.setLayoutParams(layoutParams);
            layoutParams.gravity = 8388661;
            cOUIHintRedDot.setPointMode(this.f7294f);
            if (this.f7294f == 2) {
                cOUIHintRedDot.setViewHeight(this.f7300l);
                cOUIHintRedDot.setPointText(this.f7295g);
            } else {
                cOUIHintRedDot.setDotDiameter(this.f7299k);
            }
            post(new Runnable() { // from class: k2.d
                @Override // java.lang.Runnable
                public final void run() {
                    COUIRedDotFrameLayout.this.f(cOUIHintRedDot);
                }
            });
            h();
        }
    }

    private void d(AttributeSet attributeSet, int i10) {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_medium_icon_size);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_large_icon_size);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUIRedDotFrameLayout, i10, 0);
            this.f7294f = obtainStyledAttributes.getInt(R$styleable.COUIRedDotFrameLayout_couiHintRedPointMode, 0);
            this.f7295g = obtainStyledAttributes.getString(R$styleable.COUIRedDotFrameLayout_couiHintRedPointText);
            this.f7296h = obtainStyledAttributes.getInt(R$styleable.COUIRedDotFrameLayout_anchorViewShapeType, 0);
            this.f7303o = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIRedDotFrameLayout_anchorViewDpSize, dimensionPixelSize);
            obtainStyledAttributes.recycle();
        }
        int i11 = this.f7294f;
        if (i11 == 0) {
            return;
        }
        int i12 = this.f7303o;
        if (i12 < dimensionPixelSize) {
            if (i11 == 1 || i11 == 4) {
                this.f7299k = getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_small_reddot_size);
            }
            if (this.f7296h == 0) {
                if (this.f7294f == 2) {
                    this.f7297i = getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_small_number_topend_margin_rectangle);
                } else {
                    this.f7297i = getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_small_icon_topend_margin_rectangle);
                }
            } else {
                int i13 = this.f7294f;
                if (i13 == 1 || i13 == 4) {
                    this.f7298j = getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_small_icon_topend_margin_circle);
                }
            }
        } else if (i12 >= dimensionPixelSize2) {
            if (i11 == 2) {
                this.f7300l = getResources().getDimensionPixelSize(R$dimen.coui_height_large);
            } else {
                this.f7299k = getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_large_reddot_size);
            }
            if (this.f7296h == 0) {
                if (this.f7294f == 2) {
                    this.f7297i = getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_large_number_topend_margin_rectangle);
                } else {
                    this.f7297i = getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_large_icon_topend_margin_rectangle);
                }
            } else {
                int i14 = this.f7294f;
                if (i14 == 1 || i14 == 4) {
                    this.f7298j = getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_large_icon_topend_margin_circle);
                }
            }
        } else {
            if (i11 == 1 || i11 == 4) {
                this.f7299k = getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_medium_reddot_size);
            }
            if (this.f7296h == 0) {
                if (this.f7294f == 2) {
                    this.f7297i = getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_medium_number_topend_margin_rectangle);
                } else {
                    this.f7297i = getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_medium_icon_topend_margin_rectangle);
                }
            } else {
                int i15 = this.f7294f;
                if (i15 == 1 || i15 == 4) {
                    this.f7298j = getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_medium_icon_topend_margin_circle);
                }
            }
        }
        if (this.f7294f == 4) {
            this.f7299k += getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_mode_stroke_extra_diameter);
        }
    }

    private boolean e() {
        return getLayoutDirection() == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void f(COUIHintRedDot cOUIHintRedDot) {
        addView(cOUIHintRedDot);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void g() {
        requestLayout();
    }

    private void h() {
        removeCallbacks(this.f7304p);
        post(this.f7304p);
    }

    private void i() {
        if (this.f7302n == null || this.f7301m == null) {
            for (int i10 = 0; i10 < getChildCount(); i10++) {
                View childAt = getChildAt(i10);
                if (childAt instanceof COUIHintRedDot) {
                    this.f7302n = (COUIHintRedDot) childAt;
                } else {
                    this.f7301m = childAt;
                }
            }
        }
    }

    public COUIHintRedDot getRedDotView() {
        return this.f7302n;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        if (this.f7294f == 0) {
            return;
        }
        View view = this.f7301m;
        if (view != null && this.f7302n != null) {
            if (!e()) {
                View view2 = this.f7301m;
                view2.layout(0, this.f7297i, view2.getMeasuredWidth() + 0, this.f7297i + this.f7301m.getMeasuredHeight());
                COUIHintRedDot cOUIHintRedDot = this.f7302n;
                int width = getWidth() - this.f7302n.getWidth();
                int i14 = this.f7298j;
                int width2 = getWidth();
                int i15 = this.f7298j;
                cOUIHintRedDot.layout(width - i14, i14, width2 - i15, i15 + this.f7302n.getHeight());
                return;
            }
            View view3 = this.f7301m;
            int i16 = this.f7297i;
            view3.layout(i16, i16, view3.getMeasuredWidth() + i16, this.f7297i + this.f7301m.getMeasuredHeight());
            COUIHintRedDot cOUIHintRedDot2 = this.f7302n;
            int i17 = this.f7298j;
            cOUIHintRedDot2.layout(i17, i17, cOUIHintRedDot2.getWidth() + i17, this.f7298j + this.f7302n.getHeight());
            return;
        }
        if (view == null || this.f7302n != null) {
            return;
        }
        view.layout(0, 0, view.getMeasuredWidth() + 0, this.f7301m.getMeasuredHeight());
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        if (this.f7294f == 0) {
            return;
        }
        i();
        View view = this.f7301m;
        if (view != null && this.f7302n != null) {
            setMeasuredDimension(getMeasuredWidth() + this.f7297i, getMeasuredHeight() + this.f7297i);
        } else {
            if (view == null || this.f7302n != null) {
                return;
            }
            setMeasuredDimension(view.getWidth(), this.f7301m.getHeight());
        }
    }

    public COUIRedDotFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIRedDotFrameLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f7293e = "COUIRedDotFrameLayout";
        this.f7294f = 0;
        this.f7296h = 0;
        this.f7300l = getResources().getDimensionPixelSize(R$dimen.coui_height);
        this.f7304p = new Runnable() { // from class: k2.c
            @Override // java.lang.Runnable
            public final void run() {
                COUIRedDotFrameLayout.this.g();
            }
        };
        d(attributeSet, i10);
        c();
    }
}
