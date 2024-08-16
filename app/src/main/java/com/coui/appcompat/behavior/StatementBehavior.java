package com.coui.appcompat.behavior;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.support.list.R$dimen;
import com.support.list.R$id;

/* loaded from: classes.dex */
public class StatementBehavior extends CoordinatorLayout.Behavior {

    /* renamed from: a, reason: collision with root package name */
    public int f5439a;

    /* renamed from: b, reason: collision with root package name */
    private View f5440b;

    /* renamed from: c, reason: collision with root package name */
    private View f5441c;

    /* renamed from: d, reason: collision with root package name */
    private View f5442d;

    /* renamed from: e, reason: collision with root package name */
    private int f5443e;

    /* renamed from: f, reason: collision with root package name */
    private int f5444f;

    /* renamed from: g, reason: collision with root package name */
    private int f5445g;

    /* renamed from: h, reason: collision with root package name */
    private int[] f5446h;

    /* renamed from: i, reason: collision with root package name */
    private ViewGroup.LayoutParams f5447i;

    /* renamed from: j, reason: collision with root package name */
    private int f5448j;

    /* renamed from: k, reason: collision with root package name */
    private int f5449k;

    /* renamed from: l, reason: collision with root package name */
    private int f5450l;

    /* renamed from: m, reason: collision with root package name */
    private int f5451m;

    /* renamed from: n, reason: collision with root package name */
    private int f5452n;

    /* renamed from: o, reason: collision with root package name */
    private int f5453o;

    /* renamed from: p, reason: collision with root package name */
    private int f5454p;

    /* renamed from: q, reason: collision with root package name */
    private float f5455q;

    /* renamed from: r, reason: collision with root package name */
    private float f5456r;

    /* renamed from: s, reason: collision with root package name */
    private Resources f5457s;

    /* loaded from: classes.dex */
    class a implements View.OnScrollChangeListener {
        a() {
        }

        @Override // android.view.View.OnScrollChangeListener
        public void onScrollChange(View view, int i10, int i11, int i12, int i13) {
            StatementBehavior.this.e();
        }
    }

    public StatementBehavior() {
        this.f5446h = new int[2];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        this.f5442d = null;
        View view = this.f5441c;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (viewGroup.getChildCount() > 0) {
                int i10 = 0;
                while (true) {
                    if (i10 >= viewGroup.getChildCount()) {
                        break;
                    }
                    if (viewGroup.getChildAt(i10).getVisibility() == 0) {
                        this.f5442d = viewGroup.getChildAt(i10);
                        break;
                    }
                    i10++;
                }
            }
        }
        if (this.f5442d == null) {
            this.f5442d = this.f5441c;
        }
        this.f5442d.getLocationOnScreen(this.f5446h);
        int i11 = this.f5446h[1];
        this.f5443e = i11;
        this.f5444f = 0;
        if (i11 < this.f5450l) {
            this.f5444f = this.f5451m;
        } else {
            int i12 = this.f5449k;
            if (i11 > i12) {
                this.f5444f = 0;
            } else {
                this.f5444f = i12 - i11;
            }
        }
        this.f5445g = this.f5444f;
        if (this.f5455q <= 1.0f) {
            float abs = Math.abs(r0) / this.f5451m;
            this.f5455q = abs;
            this.f5440b.setAlpha(abs);
        }
        int i13 = this.f5443e;
        if (i13 < this.f5452n) {
            this.f5444f = this.f5454p;
        } else {
            int i14 = this.f5453o;
            if (i13 > i14) {
                this.f5444f = 0;
            } else {
                this.f5444f = i14 - i13;
            }
        }
        this.f5445g = this.f5444f;
        float abs2 = Math.abs(r0) / this.f5454p;
        this.f5456r = abs2;
        ViewGroup.LayoutParams layoutParams = this.f5447i;
        layoutParams.width = (int) (this.f5439a - (this.f5448j * (1.0f - abs2)));
        this.f5440b.setLayoutParams(layoutParams);
    }

    private void init(Context context) {
        Resources resources = context.getResources();
        this.f5457s = resources;
        this.f5448j = resources.getDimensionPixelOffset(R$dimen.preference_divider_margin_horizontal) * 2;
        this.f5451m = this.f5457s.getDimensionPixelOffset(R$dimen.preference_line_alpha_range_change_offset);
        this.f5454p = this.f5457s.getDimensionPixelOffset(R$dimen.preference_divider_width_change_offset);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, View view3, int i10, int i11) {
        if (this.f5449k <= 0) {
            view.getLocationOnScreen(this.f5446h);
            this.f5449k = this.f5446h[1];
            this.f5441c = view3;
            View findViewById = view.findViewById(R$id.divider_line);
            this.f5440b = findViewById;
            this.f5439a = findViewById.getWidth();
            this.f5447i = this.f5440b.getLayoutParams();
            int i12 = this.f5449k;
            this.f5450l = i12 - this.f5451m;
            int dimensionPixelOffset = i12 - this.f5457s.getDimensionPixelOffset(R$dimen.preference_divider_width_start_count_offset);
            this.f5453o = dimensionPixelOffset;
            this.f5452n = dimensionPixelOffset - this.f5454p;
        }
        view3.setOnScrollChangeListener(new a());
        return false;
    }

    public StatementBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f5446h = new int[2];
        init(context);
    }
}
