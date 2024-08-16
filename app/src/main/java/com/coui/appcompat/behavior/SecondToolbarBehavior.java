package com.coui.appcompat.behavior;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.support.list.R$bool;
import com.support.list.R$dimen;
import com.support.list.R$id;

/* loaded from: classes.dex */
public class SecondToolbarBehavior extends CoordinatorLayout.Behavior<AppBarLayout> implements AbsListView.OnScrollListener {

    /* renamed from: e, reason: collision with root package name */
    private View f5417e;

    /* renamed from: f, reason: collision with root package name */
    private View f5418f;

    /* renamed from: g, reason: collision with root package name */
    private View f5419g;

    /* renamed from: h, reason: collision with root package name */
    private int f5420h;

    /* renamed from: i, reason: collision with root package name */
    private int f5421i;

    /* renamed from: j, reason: collision with root package name */
    private int[] f5422j;

    /* renamed from: k, reason: collision with root package name */
    private int f5423k;

    /* renamed from: l, reason: collision with root package name */
    private ViewGroup.LayoutParams f5424l;

    /* renamed from: m, reason: collision with root package name */
    private int f5425m;

    /* renamed from: n, reason: collision with root package name */
    private int f5426n;

    /* renamed from: o, reason: collision with root package name */
    private int f5427o;

    /* renamed from: p, reason: collision with root package name */
    private int f5428p;

    /* renamed from: q, reason: collision with root package name */
    private int f5429q;

    /* renamed from: r, reason: collision with root package name */
    private int f5430r;

    /* renamed from: s, reason: collision with root package name */
    private int f5431s;

    /* renamed from: t, reason: collision with root package name */
    private int f5432t;

    /* renamed from: u, reason: collision with root package name */
    private float f5433u;

    /* renamed from: v, reason: collision with root package name */
    private float f5434v;

    /* renamed from: w, reason: collision with root package name */
    private Resources f5435w;

    /* renamed from: x, reason: collision with root package name */
    public int f5436x;

    /* renamed from: y, reason: collision with root package name */
    private boolean f5437y;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements View.OnScrollChangeListener {
        a() {
        }

        @Override // android.view.View.OnScrollChangeListener
        public void onScrollChange(View view, int i10, int i11, int i12, int i13) {
            SecondToolbarBehavior.this.onListScroll();
        }
    }

    public SecondToolbarBehavior() {
        this.f5422j = new int[2];
    }

    private void init(Context context) {
        Resources resources = context.getResources();
        this.f5435w = resources;
        this.f5425m = resources.getDimensionPixelOffset(R$dimen.preference_divider_margin_horizontal);
        this.f5428p = this.f5435w.getDimensionPixelOffset(R$dimen.preference_line_alpha_range_change_offset);
        this.f5431s = this.f5435w.getDimensionPixelOffset(R$dimen.preference_divider_width_change_offset);
        this.f5432t = this.f5435w.getDimensionPixelOffset(R$dimen.preference_divider_width_start_count_offset);
        this.f5437y = this.f5435w.getBoolean(R$bool.is_dialog_preference_immersive);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onListScroll() {
        this.f5419g = null;
        View view = this.f5418f;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (viewGroup.getChildCount() > 0) {
                int i10 = 0;
                while (true) {
                    if (i10 >= viewGroup.getChildCount()) {
                        break;
                    }
                    if (viewGroup.getChildAt(i10).getVisibility() == 0) {
                        this.f5419g = viewGroup.getChildAt(i10);
                        break;
                    }
                    i10++;
                }
            }
        }
        if (this.f5419g == null) {
            this.f5419g = this.f5418f;
        }
        this.f5419g.getLocationOnScreen(this.f5422j);
        int i11 = this.f5422j[1];
        int[] iArr = new int[2];
        this.f5418f.getRootView().getLocationOnScreen(iArr);
        int i12 = iArr[1];
        if (i12 != 0) {
            i11 -= i12;
        }
        this.f5420h = 0;
        if (i11 < this.f5427o) {
            this.f5420h = this.f5428p;
        } else {
            int i13 = this.f5426n;
            if (i11 > i13) {
                this.f5420h = 0;
            } else {
                this.f5420h = i13 - i11;
            }
        }
        this.f5421i = this.f5420h;
        if (this.f5433u <= 1.0f) {
            float abs = Math.abs(r1) / this.f5428p;
            this.f5433u = abs;
            this.f5417e.setAlpha(abs);
        }
        if (i11 < this.f5429q) {
            this.f5420h = this.f5431s;
        } else {
            int i14 = this.f5430r;
            if (i11 > i14) {
                this.f5420h = 0;
            } else {
                this.f5420h = i14 - i11;
            }
        }
        this.f5421i = this.f5420h;
        float abs2 = Math.abs(r0) / this.f5431s;
        this.f5434v = abs2;
        ViewGroup.LayoutParams layoutParams = this.f5424l;
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            int i15 = (int) (this.f5425m * (1.0f - abs2));
            ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin = i15;
            ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = i15;
        }
        this.f5417e.setLayoutParams(layoutParams);
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScroll(AbsListView absListView, int i10, int i11, int i12) {
        onListScroll();
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScrollStateChanged(AbsListView absListView, int i10) {
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, View view2, int i10, int i11) {
        boolean z10 = (i10 & 2) != 0 && coordinatorLayout.getHeight() - view.getHeight() <= appBarLayout.getHeight();
        if (!this.f5437y && z10) {
            if (this.f5426n <= 0) {
                this.f5418f = view2;
                this.f5417e = appBarLayout.findViewById(R$id.divider_line);
            }
            int measuredHeight = appBarLayout.getMeasuredHeight();
            this.f5426n = measuredHeight;
            this.f5427o = measuredHeight - this.f5428p;
            int i12 = measuredHeight - this.f5432t;
            this.f5430r = i12;
            this.f5429q = i12 - this.f5431s;
            this.f5436x = this.f5417e.getWidth();
            this.f5424l = this.f5417e.getLayoutParams();
            this.f5423k = appBarLayout.getMeasuredWidth();
            view2.setOnScrollChangeListener(new a());
        }
        return false;
    }

    public SecondToolbarBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f5422j = new int[2];
        init(context);
    }
}
