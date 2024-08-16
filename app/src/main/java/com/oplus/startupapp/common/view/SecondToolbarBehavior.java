package com.oplus.startupapp.common.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.oplus.battery.R;

/* loaded from: classes2.dex */
public class SecondToolbarBehavior extends CoordinatorLayout.Behavior<AppBarLayout> implements AbsListView.OnScrollListener {

    /* renamed from: e, reason: collision with root package name */
    private View f10481e;

    /* renamed from: f, reason: collision with root package name */
    private View f10482f;

    /* renamed from: g, reason: collision with root package name */
    private View f10483g;

    /* renamed from: h, reason: collision with root package name */
    private int f10484h;

    /* renamed from: i, reason: collision with root package name */
    private int f10485i;

    /* renamed from: j, reason: collision with root package name */
    private int f10486j;

    /* renamed from: k, reason: collision with root package name */
    private int[] f10487k;

    /* renamed from: l, reason: collision with root package name */
    private int f10488l;

    /* renamed from: m, reason: collision with root package name */
    private ViewGroup.LayoutParams f10489m;

    /* renamed from: n, reason: collision with root package name */
    private int f10490n;

    /* renamed from: o, reason: collision with root package name */
    private int f10491o;

    /* renamed from: p, reason: collision with root package name */
    private int f10492p;

    /* renamed from: q, reason: collision with root package name */
    private int f10493q;

    /* renamed from: r, reason: collision with root package name */
    private int f10494r;

    /* renamed from: s, reason: collision with root package name */
    private int f10495s;

    /* renamed from: t, reason: collision with root package name */
    private int f10496t;

    /* renamed from: u, reason: collision with root package name */
    private float f10497u;

    /* renamed from: v, reason: collision with root package name */
    private float f10498v;

    /* renamed from: w, reason: collision with root package name */
    private Resources f10499w;

    /* renamed from: x, reason: collision with root package name */
    public int f10500x;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class a implements View.OnScrollChangeListener {
        a() {
        }

        @Override // android.view.View.OnScrollChangeListener
        public void onScrollChange(View view, int i10, int i11, int i12, int i13) {
            SecondToolbarBehavior.this.onListScroll();
        }
    }

    public SecondToolbarBehavior() {
        this.f10487k = new int[2];
    }

    private void init(Context context) {
        Resources resources = context.getResources();
        this.f10499w = resources;
        this.f10490n = resources.getDimensionPixelOffset(R.dimen.common_margin) * 2;
        this.f10493q = this.f10499w.getDimensionPixelOffset(R.dimen.line_alpha_range_change_offset);
        this.f10496t = this.f10499w.getDimensionPixelOffset(R.dimen.divider_width_change_offset);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onListScroll() {
        this.f10483g = null;
        View view = this.f10482f;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (viewGroup.getChildCount() > 0) {
                int i10 = 0;
                while (true) {
                    if (i10 >= viewGroup.getChildCount()) {
                        break;
                    }
                    if (viewGroup.getChildAt(i10).getVisibility() == 0) {
                        this.f10483g = viewGroup.getChildAt(i10);
                        break;
                    }
                    i10++;
                }
            }
        }
        if (this.f10483g == null) {
            this.f10483g = this.f10482f;
        }
        this.f10483g.getLocationOnScreen(this.f10487k);
        int i11 = this.f10487k[1];
        this.f10484h = i11;
        this.f10485i = 0;
        if (i11 < this.f10492p) {
            this.f10485i = this.f10493q;
        } else {
            int i12 = this.f10491o;
            if (i11 > i12) {
                this.f10485i = 0;
            } else {
                this.f10485i = i12 - i11;
            }
        }
        this.f10486j = this.f10485i;
        if (this.f10497u <= 1.0f) {
            float abs = Math.abs(r0) / this.f10493q;
            this.f10497u = abs;
            this.f10481e.setAlpha(abs);
        }
        int i13 = this.f10484h;
        if (i13 < this.f10494r) {
            this.f10485i = this.f10496t;
        } else {
            int i14 = this.f10495s;
            if (i13 > i14) {
                this.f10485i = 0;
            } else {
                this.f10485i = i14 - i13;
            }
        }
        this.f10486j = this.f10485i;
        float abs2 = Math.abs(r0) / this.f10496t;
        this.f10498v = abs2;
        ViewGroup.LayoutParams layoutParams = this.f10489m;
        layoutParams.width = (int) (this.f10500x + (this.f10490n * abs2));
        this.f10481e.setLayoutParams(layoutParams);
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
        if ((i10 & 2) != 0 && coordinatorLayout.getHeight() - view.getHeight() <= appBarLayout.getHeight()) {
            if (this.f10491o <= 0) {
                this.f10491o = appBarLayout.getMeasuredHeight();
                this.f10482f = view2;
                View findViewById = appBarLayout.findViewById(R.id.divider_line);
                this.f10481e = findViewById;
                this.f10500x = findViewById.getWidth();
                this.f10489m = this.f10481e.getLayoutParams();
                this.f10488l = appBarLayout.getMeasuredWidth();
                int i12 = this.f10491o;
                this.f10492p = i12 - this.f10493q;
                int dimensionPixelOffset = i12 - this.f10499w.getDimensionPixelOffset(R.dimen.divider_width_start_count_offset);
                this.f10495s = dimensionPixelOffset;
                this.f10494r = dimensionPixelOffset - this.f10496t;
            }
            view2.setOnScrollChangeListener(new a());
        }
        return false;
    }

    public SecondToolbarBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f10487k = new int[2];
        init(context);
    }
}
