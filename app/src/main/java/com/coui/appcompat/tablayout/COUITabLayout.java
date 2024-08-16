package com.coui.appcompat.tablayout;

import android.R;
import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.coui.appcompat.scrollview.COUIHorizontalScrollView;
import com.oplus.deepthinker.sdk.app.geofence.Geofence;
import com.support.appcompat.R$attr;
import com.support.bars.R$color;
import com.support.bars.R$dimen;
import com.support.bars.R$style;
import com.support.bars.R$styleable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import m1.COUIEaseInterpolator;
import v1.COUIContextUtil;

/* loaded from: classes.dex */
public class COUITabLayout extends COUIHorizontalScrollView {
    private static final androidx.core.util.e<COUITab> S0 = new androidx.core.util.g(16);
    private int A0;
    private int B0;
    private int C0;
    private int D0;
    private int E0;
    private float F0;
    private float G0;
    private int H0;
    protected final int I;
    private boolean I0;
    protected final COUISlidingTabStrip J;
    private int J0;
    private final ArrayList<COUITab> K;
    private int K0;
    private final ArrayList<c> L;
    private boolean L0;
    private final androidx.core.util.e<COUITabView> M;
    private int M0;
    protected int N;
    private int N0;
    protected int O;
    private int O0;
    protected COUITab P;
    private int P0;
    protected int Q;
    private int Q0;
    protected int R;
    private ArrayList<e> R0;
    protected int S;
    protected int T;
    protected ColorStateList U;
    protected Typeface V;
    protected Typeface W;

    /* renamed from: a0, reason: collision with root package name */
    protected int f7788a0;

    /* renamed from: b0, reason: collision with root package name */
    protected boolean f7789b0;

    /* renamed from: c0, reason: collision with root package name */
    protected boolean f7790c0;

    /* renamed from: d0, reason: collision with root package name */
    protected int f7791d0;

    /* renamed from: e0, reason: collision with root package name */
    protected boolean f7792e0;

    /* renamed from: f0, reason: collision with root package name */
    private int f7793f0;

    /* renamed from: g0, reason: collision with root package name */
    private int f7794g0;

    /* renamed from: h0, reason: collision with root package name */
    private int f7795h0;

    /* renamed from: i0, reason: collision with root package name */
    private int f7796i0;

    /* renamed from: j0, reason: collision with root package name */
    private int f7797j0;

    /* renamed from: k0, reason: collision with root package name */
    private float f7798k0;

    /* renamed from: l0, reason: collision with root package name */
    private int f7799l0;

    /* renamed from: m0, reason: collision with root package name */
    private float f7800m0;

    /* renamed from: n0, reason: collision with root package name */
    @Deprecated
    private int f7801n0;

    /* renamed from: o0, reason: collision with root package name */
    private int f7802o0;

    /* renamed from: p0, reason: collision with root package name */
    private c f7803p0;

    /* renamed from: q0, reason: collision with root package name */
    private c f7804q0;

    /* renamed from: r0, reason: collision with root package name */
    private ValueAnimator f7805r0;

    /* renamed from: s0, reason: collision with root package name */
    private ArgbEvaluator f7806s0;

    /* renamed from: t0, reason: collision with root package name */
    private ViewPager f7807t0;

    /* renamed from: u0, reason: collision with root package name */
    private PagerAdapter f7808u0;

    /* renamed from: v0, reason: collision with root package name */
    private DataSetObserver f7809v0;

    /* renamed from: w0, reason: collision with root package name */
    private f f7810w0;

    /* renamed from: x0, reason: collision with root package name */
    private b f7811x0;

    /* renamed from: y0, reason: collision with root package name */
    private boolean f7812y0;

    /* renamed from: z0, reason: collision with root package name */
    private int f7813z0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements ValueAnimator.AnimatorUpdateListener {
        a() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUITabLayout.this.scrollTo(((Integer) valueAnimator.getAnimatedValue()).intValue(), 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class b implements ViewPager.h {

        /* renamed from: a, reason: collision with root package name */
        private boolean f7815a;

        b() {
        }

        @Override // androidx.viewpager.widget.ViewPager.h
        public void a(ViewPager viewPager, PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2) {
            if (COUITabLayout.this.f7807t0 == viewPager) {
                COUITabLayout.this.d0(pagerAdapter2, this.f7815a);
            }
        }

        void b(boolean z10) {
            this.f7815a = z10;
        }
    }

    /* loaded from: classes.dex */
    public interface c {
        void a(COUITab cOUITab);

        void b(COUITab cOUITab);

        void c(COUITab cOUITab);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class d extends DataSetObserver {
        d() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            COUITabLayout.this.V();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            COUITabLayout.this.V();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class e {

        /* renamed from: a, reason: collision with root package name */
        Drawable f7818a;

        /* renamed from: b, reason: collision with root package name */
        View.OnClickListener f7819b;
    }

    /* loaded from: classes.dex */
    public static class f implements ViewPager.i {

        /* renamed from: a, reason: collision with root package name */
        private final WeakReference<COUITabLayout> f7820a;

        /* renamed from: b, reason: collision with root package name */
        private int f7821b;

        /* renamed from: c, reason: collision with root package name */
        private int f7822c;

        public f(COUITabLayout cOUITabLayout) {
            this.f7820a = new WeakReference<>(cOUITabLayout);
        }

        void a() {
            this.f7821b = 0;
            this.f7822c = 0;
        }

        @Override // androidx.viewpager.widget.ViewPager.i
        public void onPageScrollStateChanged(int i10) {
            this.f7821b = this.f7822c;
            this.f7822c = i10;
        }

        @Override // androidx.viewpager.widget.ViewPager.i
        public void onPageScrolled(int i10, float f10, int i11) {
            COUITabLayout cOUITabLayout = this.f7820a.get();
            if (cOUITabLayout != null) {
                int i12 = this.f7822c;
                cOUITabLayout.f0(i10, f10, i12 != 2 || this.f7821b == 1, (i12 == 2 && this.f7821b == 0) ? false : true);
            }
        }

        @Override // androidx.viewpager.widget.ViewPager.i
        public void onPageSelected(int i10) {
            COUITabLayout cOUITabLayout = this.f7820a.get();
            if (cOUITabLayout == null || cOUITabLayout.getSelectedTabPosition() == i10 || i10 >= cOUITabLayout.getTabCount()) {
                return;
            }
            int i11 = this.f7822c;
            cOUITabLayout.b0(cOUITabLayout.S(i10), i11 == 0 || (i11 == 2 && this.f7821b == 0));
        }
    }

    /* loaded from: classes.dex */
    public static class g implements c {

        /* renamed from: a, reason: collision with root package name */
        private final ViewPager f7823a;

        public g(ViewPager viewPager) {
            this.f7823a = viewPager;
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayout.c
        public void a(COUITab cOUITab) {
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayout.c
        public void b(COUITab cOUITab) {
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayout.c
        public void c(COUITab cOUITab) {
            this.f7823a.setCurrentItem(cOUITab.d(), false);
        }
    }

    public COUITabLayout(Context context) {
        this(context, null);
    }

    private void A(COUITabItem cOUITabItem) {
        COUITab U = U();
        CharSequence charSequence = cOUITabItem.f7785e;
        if (charSequence != null) {
            U.n(charSequence);
        }
        Drawable drawable = cOUITabItem.f7786f;
        if (drawable != null) {
            U.l(drawable);
        }
        int i10 = cOUITabItem.f7787g;
        if (i10 != 0) {
            U.j(i10);
        }
        if (!TextUtils.isEmpty(cOUITabItem.getContentDescription())) {
            U.i(cOUITabItem.getContentDescription());
        }
        x(U);
    }

    private void B(COUITab cOUITab) {
        this.J.addView(cOUITab.f7840b, cOUITab.d(), I());
    }

    private void C(View view) {
        if (view instanceof COUITabItem) {
            A((COUITabItem) view);
            return;
        }
        throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
    }

    private void D(int i10) {
        if (i10 == -1) {
            return;
        }
        if (getWindowToken() != null && ViewCompat.Q(this) && !this.J.c()) {
            int scrollX = getScrollX();
            int F = F(i10, 0.0f);
            if (scrollX != F) {
                Q();
                this.f7805r0.setIntValues(scrollX, F);
                this.f7805r0.start();
            }
            this.J.b(i10, 300);
            return;
        }
        e0(i10, 0.0f, true);
    }

    private void E() {
        k0(true);
    }

    private int F(int i10, float f10) {
        int right;
        int i11 = 0;
        if (getWidth() == 0) {
            return 0;
        }
        View childAt = this.J.getChildAt(i10);
        int i12 = i10 + 1;
        View childAt2 = i12 < this.J.getChildCount() ? this.J.getChildAt(i12) : null;
        if (childAt != null) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childAt.getLayoutParams();
            i11 = layoutParams.rightMargin + childAt.getWidth() + layoutParams.leftMargin;
        }
        if (childAt2 != null) {
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) childAt2.getLayoutParams();
            childAt2.getWidth();
            int i13 = layoutParams2.leftMargin;
        }
        int width = (i11 / 2) - (getWidth() / 2);
        if (childAt != null) {
            LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) childAt.getLayoutParams();
            if (ViewCompat.x(this) == 0) {
                right = (childAt.getLeft() - layoutParams3.leftMargin) + (getPaddingLeft() / 2) + (getPaddingRight() / 2);
            } else {
                right = ((childAt.getRight() + layoutParams3.rightMargin) - (getPaddingLeft() / 2)) - (getPaddingRight() / 2);
            }
            width += right;
        }
        int i14 = (int) (i11 * 0.5f * f10);
        return ViewCompat.x(this) == 0 ? width + i14 : width - i14;
    }

    private void G(COUITab cOUITab, int i10) {
        cOUITab.m(i10);
        this.K.add(i10, cOUITab);
        int size = this.K.size();
        while (true) {
            i10++;
            if (i10 >= size) {
                return;
            } else {
                this.K.get(i10).m(i10);
            }
        }
    }

    private static ColorStateList H(int i10, int i11, int i12) {
        return new ColorStateList(new int[][]{new int[]{R.attr.state_selected, R.attr.state_enabled}, new int[]{-16842913, -16842910}, HorizontalScrollView.EMPTY_STATE_SET}, new int[]{i12, i11, i10});
    }

    private LinearLayout.LayoutParams I() {
        return new LinearLayout.LayoutParams(1, -1);
    }

    private COUITabView J(COUITab cOUITab) {
        androidx.core.util.e<COUITabView> eVar = this.M;
        COUITabView b10 = eVar != null ? eVar.b() : null;
        if (b10 == null) {
            b10 = new COUITabView(getContext(), this);
        }
        b10.setTab(cOUITab);
        b10.setFocusable(true);
        b10.setMinimumWidth(getTabMinWidth());
        b10.setEnabled(isEnabled());
        return b10;
    }

    private void K(COUITab cOUITab) {
        for (int size = this.L.size() - 1; size >= 0; size--) {
            this.L.get(size).b(cOUITab);
        }
    }

    private void L(COUITab cOUITab) {
        for (int size = this.L.size() - 1; size >= 0; size--) {
            this.L.get(size).c(cOUITab);
        }
    }

    private void M(COUITab cOUITab) {
        for (int size = this.L.size() - 1; size >= 0; size--) {
            this.L.get(size).a(cOUITab);
        }
    }

    private void O(Canvas canvas) {
        int width;
        int scrollX;
        int width2;
        int width3;
        int scrollX2;
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_tab_layout_button_width);
        if (this.R0.size() == 1) {
            Drawable drawable = this.R0.get(0).f7818a;
            int i10 = this.M0;
            if (i10 == -1) {
                i10 = getResources().getDimensionPixelSize(R$dimen.coui_tab_layout_button_default_horizontal_margin);
            }
            if (ViewCompat.x(this) == 1) {
                width2 = getScrollX() + i10;
                width3 = dimensionPixelSize + i10;
                scrollX2 = getScrollX();
            } else {
                width2 = (getWidth() - (dimensionPixelSize + i10)) + getScrollX();
                width3 = getWidth() - i10;
                scrollX2 = getScrollX();
            }
            int i11 = width3 + scrollX2;
            int height = getHeight() / 2;
            Resources resources = getResources();
            int i12 = R$dimen.coui_tab_layout_button_default_vertical_margin;
            drawable.setBounds(width2, height - resources.getDimensionPixelSize(i12), i11, (getHeight() / 2) + getResources().getDimensionPixelSize(i12));
            drawable.draw(canvas);
            return;
        }
        if (this.R0.size() >= 2) {
            for (int i13 = 0; i13 < this.R0.size(); i13++) {
                int i14 = this.M0;
                if (i14 == -1) {
                    i14 = getResources().getDimensionPixelSize(R$dimen.coui_tab_layout_multi_button_default_horizontal_margin);
                }
                if (ViewCompat.x(this) == 1) {
                    scrollX = i14 + (getResources().getDimensionPixelSize(R$dimen.coui_tab_layout_multi_button_default_padding) * i13);
                    width = getScrollX();
                } else {
                    width = getWidth() - ((i14 + dimensionPixelSize) + (getResources().getDimensionPixelSize(R$dimen.coui_tab_layout_multi_button_default_padding) * i13));
                    scrollX = getScrollX();
                }
                int i15 = scrollX + width;
                Drawable drawable2 = this.R0.get(i13).f7818a;
                int height2 = getHeight() / 2;
                Resources resources2 = getResources();
                int i16 = R$dimen.coui_tab_layout_button_default_vertical_margin;
                drawable2.setBounds(i15, height2 - resources2.getDimensionPixelSize(i16), i15 + dimensionPixelSize, (getHeight() / 2) + getResources().getDimensionPixelSize(i16));
                drawable2.draw(canvas);
            }
        }
    }

    private void Q() {
        if (this.f7805r0 == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.f7805r0 = valueAnimator;
            valueAnimator.setInterpolator(new COUIEaseInterpolator());
            this.f7805r0.setDuration(300L);
            this.f7805r0.addUpdateListener(new a());
        }
    }

    private void Y(int i10) {
        COUITabView cOUITabView = (COUITabView) this.J.getChildAt(i10);
        this.J.removeViewAt(i10);
        if (cOUITabView != null) {
            cOUITabView.d();
            this.M.a(cOUITabView);
        }
        requestLayout();
    }

    private void g0(int i10, float f10) {
        COUITabView cOUITabView;
        float f11;
        if (Math.abs(f10 - this.f7798k0) > 0.5f || f10 == 0.0f) {
            this.f7797j0 = i10;
        }
        this.f7798k0 = f10;
        if (i10 != this.f7797j0 && isEnabled()) {
            COUITabView cOUITabView2 = (COUITabView) this.J.getChildAt(i10);
            if (f10 >= 0.5f) {
                cOUITabView = (COUITabView) this.J.getChildAt(i10 - 1);
                f11 = f10 - 0.5f;
            } else {
                cOUITabView = (COUITabView) this.J.getChildAt(i10 + 1);
                f11 = 0.5f - f10;
            }
            float f12 = f11 / 0.5f;
            if (cOUITabView.getTextView() != null) {
                cOUITabView.getTextView().setTextColor(((Integer) this.f7806s0.evaluate(f12, Integer.valueOf(this.O), Integer.valueOf(this.N))).intValue());
            }
            if (cOUITabView2.getTextView() != null) {
                cOUITabView2.getTextView().setTextColor(((Integer) this.f7806s0.evaluate(f12, Integer.valueOf(this.N), Integer.valueOf(this.O))).intValue());
            }
        }
        if (f10 != 0.0f || i10 >= getTabCount()) {
            return;
        }
        int i11 = 0;
        while (true) {
            boolean z10 = true;
            if (i11 < getTabCount()) {
                View childAt = this.J.getChildAt(i11);
                COUITabView cOUITabView3 = (COUITabView) childAt;
                if (cOUITabView3.getTextView() != null) {
                    cOUITabView3.getTextView().setTextColor(this.U);
                }
                if (i11 != i10) {
                    z10 = false;
                }
                childAt.setSelected(z10);
                i11++;
            } else {
                this.f7790c0 = true;
                return;
            }
        }
    }

    private int getDefaultHeight() {
        int size = this.K.size();
        boolean z10 = false;
        int i10 = 0;
        while (true) {
            if (i10 < size) {
                COUITab cOUITab = this.K.get(i10);
                if (cOUITab != null && cOUITab.c() != null && !TextUtils.isEmpty(cOUITab.e())) {
                    z10 = true;
                    break;
                }
                i10++;
            } else {
                break;
            }
        }
        return z10 ? 72 : 48;
    }

    private float getScrollPosition() {
        return this.J.getIndicatorPosition();
    }

    private int getTabMinWidth() {
        return 0;
    }

    private int getTabScrollRange() {
        return Math.max(0, ((this.J.getWidth() - getWidth()) - getPaddingLeft()) - getPaddingRight());
    }

    private void i0(ViewPager viewPager, boolean z10, boolean z11) {
        ViewPager viewPager2 = this.f7807t0;
        if (viewPager2 != null) {
            f fVar = this.f7810w0;
            if (fVar != null) {
                viewPager2.removeOnPageChangeListener(fVar);
            }
            b bVar = this.f7811x0;
            if (bVar != null) {
                this.f7807t0.removeOnAdapterChangeListener(bVar);
            }
        }
        c cVar = this.f7804q0;
        if (cVar != null) {
            X(cVar);
            this.f7804q0 = null;
        }
        if (viewPager != null) {
            this.f7807t0 = viewPager;
            if (this.f7810w0 == null) {
                this.f7810w0 = new f(this);
            }
            this.f7810w0.a();
            viewPager.addOnPageChangeListener(this.f7810w0);
            g gVar = new g(viewPager);
            this.f7804q0 = gVar;
            w(gVar);
            if (viewPager.getAdapter() != null) {
                d0(viewPager.getAdapter(), z10);
            }
            if (this.f7811x0 == null) {
                this.f7811x0 = new b();
            }
            this.f7811x0.b(z10);
            viewPager.addOnAdapterChangeListener(this.f7811x0);
            e0(viewPager.getCurrentItem(), 0.0f, true);
        } else {
            this.f7807t0 = null;
            d0(null, false);
        }
        this.f7812y0 = z11;
    }

    private void j0() {
        int size = this.K.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.K.get(i10).o();
        }
    }

    private void l0() {
        this.N = this.U.getDefaultColor();
        int colorForState = this.U.getColorForState(new int[]{R.attr.state_enabled, R.attr.state_selected}, COUIContextUtil.b(getContext(), R$attr.couiColorPrimaryText, 0));
        this.O = colorForState;
        this.f7794g0 = Math.abs(Color.red(colorForState) - Color.red(this.N));
        this.f7795h0 = Math.abs(Color.green(this.O) - Color.green(this.N));
        this.f7796i0 = Math.abs(Color.blue(this.O) - Color.blue(this.N));
    }

    private void setSelectedTabView(int i10) {
        int childCount = this.J.getChildCount();
        if (i10 < childCount) {
            int i11 = 0;
            while (i11 < childCount) {
                this.J.getChildAt(i11).setSelected(i11 == i10);
                i11++;
            }
        }
    }

    int N(int i10) {
        return Math.round(getResources().getDisplayMetrics().density * i10);
    }

    public boolean P(int i10, boolean z10) {
        COUITabView cOUITabView;
        COUITab S = S(i10);
        if (S == null || (cOUITabView = S.f7840b) == null) {
            return false;
        }
        cOUITabView.setEnabled(z10);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int R(int i10, int i11) {
        return Math.min(300, (Math.abs(i10 - i11) * 50) + Geofence.MIN_RADIUS);
    }

    public COUITab S(int i10) {
        if (i10 < 0 || i10 >= getTabCount()) {
            return null;
        }
        return this.K.get(i10);
    }

    public boolean T() {
        return this.L0;
    }

    public COUITab U() {
        COUITab b10 = S0.b();
        if (b10 == null) {
            b10 = new COUITab();
        }
        b10.f7839a = this;
        b10.f7840b = J(b10);
        return b10;
    }

    void V() {
        int currentItem;
        W();
        PagerAdapter pagerAdapter = this.f7808u0;
        if (pagerAdapter != null) {
            int count = pagerAdapter.getCount();
            PagerAdapter pagerAdapter2 = this.f7808u0;
            if (pagerAdapter2 instanceof COUIFragmentStatePagerAdapter) {
                COUIFragmentStatePagerAdapter cOUIFragmentStatePagerAdapter = (COUIFragmentStatePagerAdapter) pagerAdapter2;
                for (int i10 = 0; i10 < count; i10++) {
                    if (cOUIFragmentStatePagerAdapter.b(i10) > 0) {
                        z(U().k(cOUIFragmentStatePagerAdapter.b(i10)), false);
                    } else {
                        z(U().n(cOUIFragmentStatePagerAdapter.getPageTitle(i10)), false);
                    }
                }
            } else {
                for (int i11 = 0; i11 < count; i11++) {
                    z(U().n(this.f7808u0.getPageTitle(i11)), false);
                }
            }
            ViewPager viewPager = this.f7807t0;
            if (viewPager == null || count <= 0 || (currentItem = viewPager.getCurrentItem()) == getSelectedTabPosition() || currentItem >= getTabCount()) {
                return;
            }
            a0(S(currentItem));
        }
    }

    public void W() {
        for (int childCount = this.J.getChildCount() - 1; childCount >= 0; childCount--) {
            Y(childCount);
        }
        Iterator<COUITab> it = this.K.iterator();
        while (it.hasNext()) {
            COUITab next = it.next();
            it.remove();
            next.g();
            S0.a(next);
        }
        this.P = null;
        this.f7789b0 = false;
    }

    public void X(c cVar) {
        this.L.remove(cVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void Z() {
        int childCount = this.J.getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = this.J.getChildAt(i10);
            if (childAt instanceof COUITabView) {
                ((COUITabView) childAt).getTextView().setTextColor(this.U);
            }
        }
    }

    public void a0(COUITab cOUITab) {
        b0(cOUITab, true);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public void addView(View view) {
        C(view);
    }

    public void b0(COUITab cOUITab, boolean z10) {
        COUITab cOUITab2 = this.P;
        if (cOUITab2 == cOUITab) {
            if (cOUITab2 != null) {
                K(cOUITab);
                return;
            }
            return;
        }
        int d10 = cOUITab != null ? cOUITab.d() : -1;
        if (z10) {
            if ((cOUITab2 == null || cOUITab2.d() == -1) && d10 != -1) {
                e0(d10, 0.0f, true);
            } else {
                D(d10);
            }
            if (d10 != -1) {
                setSelectedTabView(d10);
            }
            this.f7797j0 = d10;
        } else if (isEnabled() && this.f7792e0) {
            performHapticFeedback(302);
        }
        if (cOUITab2 != null) {
            M(cOUITab2);
        }
        this.P = cOUITab;
        if (cOUITab != null) {
            L(cOUITab);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void c0(int i10, int i11) {
        ViewCompat.A0(this, i10, 0, i11, 0);
    }

    void d0(PagerAdapter pagerAdapter, boolean z10) {
        DataSetObserver dataSetObserver;
        PagerAdapter pagerAdapter2 = this.f7808u0;
        if (pagerAdapter2 != null && (dataSetObserver = this.f7809v0) != null) {
            pagerAdapter2.unregisterDataSetObserver(dataSetObserver);
        }
        this.f7808u0 = pagerAdapter;
        if (z10 && pagerAdapter != null) {
            if (this.f7809v0 == null) {
                this.f7809v0 = new d();
            }
            pagerAdapter.registerDataSetObserver(this.f7809v0);
        }
        V();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        COUISlidingTabStrip cOUISlidingTabStrip = this.J;
        if (cOUISlidingTabStrip != null) {
            if (cOUISlidingTabStrip.getIndicatorBackgroundPaint() != null) {
                canvas.drawRect(this.J.getIndicatorBackgroundPaddingLeft() + getScrollX(), getHeight() - this.J.getIndicatorBackgroundHeight(), (getWidth() + getScrollX()) - this.J.getIndicatorBackgroundPaddingRight(), getHeight(), this.J.getIndicatorBackgroundPaint());
            }
            if (this.J.getSelectedIndicatorPaint() != null) {
                canvas.drawText(" ", 0.0f, 0.0f, this.J.getSelectedIndicatorPaint());
                if (this.J.getIndicatorRight() > this.J.getIndicatorLeft()) {
                    int paddingLeft = getPaddingLeft() + this.J.getIndicatorLeft();
                    int paddingLeft2 = getPaddingLeft() + this.J.getIndicatorRight();
                    int scrollX = (getScrollX() + getPaddingLeft()) - this.E0;
                    int scrollX2 = ((getScrollX() + getWidth()) - getPaddingRight()) + this.E0;
                    boolean z10 = false;
                    if (paddingLeft2 > scrollX && paddingLeft < scrollX2) {
                        z10 = true;
                    }
                    if (z10) {
                        if (paddingLeft < scrollX) {
                            paddingLeft = scrollX;
                        }
                        if (paddingLeft2 > scrollX2) {
                            paddingLeft2 = scrollX2;
                        }
                        canvas.drawRect(paddingLeft, getHeight() - this.J.f7750l, paddingLeft2, getHeight(), this.J.getSelectedIndicatorPaint());
                    }
                }
                if (this.I0) {
                    canvas.drawRect(getLeft(), getHeight() - 1, getScrollX() + getWidth() + this.E0, getHeight(), this.J.getBottomDividerPaint());
                }
            }
        }
        O(canvas);
    }

    public void e0(int i10, float f10, boolean z10) {
        f0(i10, f10, z10, true);
    }

    public void f0(int i10, float f10, boolean z10, boolean z11) {
        int round = Math.round(i10 + f10);
        if (round < 0 || round >= this.J.getChildCount()) {
            return;
        }
        if (z11) {
            this.J.k(i10, f10);
        } else if (this.J.f7746h != getSelectedTabPosition()) {
            this.J.f7746h = getSelectedTabPosition();
            this.J.o();
        }
        ValueAnimator valueAnimator = this.f7805r0;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.f7805r0.cancel();
        }
        scrollTo(F(i10, f10), 0);
        if (z10) {
            g0(round, f10);
        }
    }

    public float getDefaultIndicatoRatio() {
        return this.F0;
    }

    public int getIndicatorBackgroundHeight() {
        COUISlidingTabStrip cOUISlidingTabStrip = this.J;
        if (cOUISlidingTabStrip == null) {
            return -1;
        }
        return cOUISlidingTabStrip.getIndicatorBackgroundHeight();
    }

    public int getIndicatorBackgroundPaddingLeft() {
        COUISlidingTabStrip cOUISlidingTabStrip = this.J;
        if (cOUISlidingTabStrip == null) {
            return -1;
        }
        return cOUISlidingTabStrip.getIndicatorBackgroundPaddingLeft();
    }

    public int getIndicatorBackgroundPaddingRight() {
        COUISlidingTabStrip cOUISlidingTabStrip = this.J;
        if (cOUISlidingTabStrip == null) {
            return -1;
        }
        return cOUISlidingTabStrip.getIndicatorBackgroundPaddingRight();
    }

    public int getIndicatorBackgroundPaintColor() {
        COUISlidingTabStrip cOUISlidingTabStrip = this.J;
        if (cOUISlidingTabStrip == null) {
            return -1;
        }
        return cOUISlidingTabStrip.getIndicatorBackgroundPaint().getColor();
    }

    public int getIndicatorPadding() {
        return this.E0;
    }

    public float getIndicatorWidthRatio() {
        COUISlidingTabStrip cOUISlidingTabStrip = this.J;
        if (cOUISlidingTabStrip == null) {
            return -1.0f;
        }
        return cOUISlidingTabStrip.getIndicatorWidthRatio();
    }

    public int getRequestedTabMaxWidth() {
        return this.f7788a0;
    }

    public int getRequestedTabMinWidth() {
        return this.f7793f0;
    }

    public int getSelectedIndicatorColor() {
        return this.A0;
    }

    public int getSelectedTabPosition() {
        COUITab cOUITab = this.P;
        if (cOUITab != null) {
            return cOUITab.d();
        }
        return -1;
    }

    public int getTabCount() {
        return this.K.size();
    }

    public int getTabGravity() {
        return this.f7801n0;
    }

    public int getTabMinDivider() {
        return this.C0;
    }

    public int getTabMinMargin() {
        return this.D0;
    }

    public int getTabMode() {
        return this.f7802o0;
    }

    public int getTabPaddingBottom() {
        return this.T;
    }

    public int getTabPaddingEnd() {
        return this.S;
    }

    public int getTabPaddingStart() {
        return this.Q;
    }

    public int getTabPaddingTop() {
        return this.R;
    }

    public COUISlidingTabStrip getTabStrip() {
        return this.J;
    }

    public ColorStateList getTabTextColors() {
        return this.U;
    }

    public float getTabTextSize() {
        return this.f7800m0;
    }

    public void h0(ViewPager viewPager, boolean z10) {
        i0(viewPager, z10, false);
    }

    public void k0(boolean z10) {
        for (int i10 = 0; i10 < this.J.getChildCount(); i10++) {
            COUITabView cOUITabView = (COUITabView) this.J.getChildAt(i10);
            cOUITabView.setMinimumWidth(getTabMinWidth());
            if (cOUITabView.getTextView() != null) {
                ViewCompat.A0(cOUITabView.getTextView(), this.Q, this.R, this.S, this.T);
            }
            if (z10) {
                cOUITabView.requestLayout();
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.f7807t0 == null) {
            ViewParent parent = getParent();
            if (parent instanceof ViewPager) {
                i0((ViewPager) parent, true, true);
            }
        }
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.f7789b0 = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.scrollview.COUIHorizontalScrollView, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.f7812y0) {
            setupWithViewPager(null);
            this.f7812y0 = false;
        }
    }

    @Override // com.coui.appcompat.scrollview.COUIHorizontalScrollView, android.widget.HorizontalScrollView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            for (int i10 = 0; i10 < this.R0.size(); i10++) {
                if (this.R0.get(i10).f7819b != null && this.R0.get(i10).f7818a.getBounds().contains(((int) motionEvent.getX()) + getScrollX(), (int) motionEvent.getY())) {
                    return true;
                }
            }
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        int i14;
        super.onLayout(z10, i10, i11, i12, i13);
        if (!this.f7790c0 || (i14 = this.f7797j0) < 0 || i14 >= this.J.getChildCount()) {
            return;
        }
        this.f7790c0 = false;
        scrollTo(F(this.f7797j0, 0.0f), 0);
    }

    @Override // com.coui.appcompat.scrollview.COUIHorizontalScrollView, android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        int N = N(getDefaultHeight()) + getPaddingTop() + getPaddingBottom();
        int mode = View.MeasureSpec.getMode(i11);
        if (mode == Integer.MIN_VALUE) {
            i11 = View.MeasureSpec.makeMeasureSpec(Math.min(N, View.MeasureSpec.getSize(i11)), 1073741824);
        } else if (mode == 0) {
            i11 = View.MeasureSpec.makeMeasureSpec(N, 1073741824);
        }
        int size = View.MeasureSpec.getSize(i10);
        if (this.Q0 == -1) {
            this.f7788a0 = (int) (size * 0.7f);
        }
        if (View.MeasureSpec.getMode(i10) != 1073741824) {
            setMeasuredDimension(0, 0);
            return;
        }
        int i12 = this.f7802o0;
        if (i12 == 0) {
            getChildAt(0).measure(View.MeasureSpec.makeMeasureSpec(536870911, Integer.MIN_VALUE), i11);
        } else if (i12 == 1) {
            getChildAt(0).measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), i11);
        }
        setMeasuredDimension(size, getChildAt(0).getMeasuredHeight());
    }

    @Override // com.coui.appcompat.scrollview.COUIHorizontalScrollView, android.widget.HorizontalScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            for (int i10 = 0; i10 < this.R0.size(); i10++) {
                if (this.R0.get(i10).f7819b != null && this.R0.get(i10).f7818a.getBounds().contains(((int) motionEvent.getX()) + getScrollX(), (int) motionEvent.getY())) {
                    this.R0.get(i10).f7819b.onClick(this);
                    return true;
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setEnableVibrator(boolean z10) {
        this.f7792e0 = z10;
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        super.setEnabled(z10);
        this.J.setSelectedIndicatorColor(z10 ? this.A0 : this.O0);
        for (int i10 = 0; i10 < getTabCount(); i10++) {
            P(i10, z10);
        }
    }

    public void setIndicatorAnimTime(int i10) {
        COUISlidingTabStrip cOUISlidingTabStrip = this.J;
        if (cOUISlidingTabStrip != null) {
            cOUISlidingTabStrip.setIndicatorAnimTime(i10);
        }
    }

    public void setIndicatorBackgroundColor(int i10) {
        COUISlidingTabStrip cOUISlidingTabStrip = this.J;
        if (cOUISlidingTabStrip == null) {
            return;
        }
        cOUISlidingTabStrip.getIndicatorBackgroundPaint().setColor(i10);
    }

    public void setIndicatorBackgroundHeight(int i10) {
        COUISlidingTabStrip cOUISlidingTabStrip = this.J;
        if (cOUISlidingTabStrip == null) {
            return;
        }
        cOUISlidingTabStrip.setIndicatorBackgroundHeight(i10);
    }

    public void setIndicatorBackgroundPaddingLeft(int i10) {
        COUISlidingTabStrip cOUISlidingTabStrip = this.J;
        if (cOUISlidingTabStrip == null) {
            return;
        }
        cOUISlidingTabStrip.setIndicatorBackgroundPaddingLeft(i10);
    }

    public void setIndicatorBackgroundPaddingRight(int i10) {
        COUISlidingTabStrip cOUISlidingTabStrip = this.J;
        if (cOUISlidingTabStrip == null) {
            return;
        }
        cOUISlidingTabStrip.setIndicatorBackgroundPaddingRight(i10);
    }

    public void setIndicatorPadding(int i10) {
        this.E0 = i10;
        requestLayout();
    }

    public void setIndicatorWidthRatio(float f10) {
        COUISlidingTabStrip cOUISlidingTabStrip = this.J;
        if (cOUISlidingTabStrip == null) {
            return;
        }
        this.F0 = f10;
        cOUISlidingTabStrip.setIndicatorWidthRatio(f10);
    }

    @Deprecated
    public void setOnTabSelectedListener(c cVar) {
        c cVar2 = this.f7803p0;
        if (cVar2 != null) {
            X(cVar2);
        }
        this.f7803p0 = cVar;
        if (cVar != null) {
            w(cVar);
        }
    }

    public void setRequestedTabMaxWidth(int i10) {
        this.f7788a0 = i10;
        this.Q0 = i10;
    }

    public void setRequestedTabMinWidth(int i10) {
        this.f7793f0 = i10;
        this.P0 = i10;
    }

    void setScrollAnimatorListener(Animator.AnimatorListener animatorListener) {
        Q();
        this.f7805r0.addListener(animatorListener);
    }

    public void setSelectedTabIndicatorColor(int i10) {
        this.J.setSelectedIndicatorColor(i10);
        this.A0 = i10;
    }

    public void setSelectedTabIndicatorHeight(int i10) {
        this.J.setSelectedIndicatorHeight(i10);
    }

    public void setTabGravity(int i10) {
    }

    public void setTabMinDivider(int i10) {
        this.C0 = i10;
        requestLayout();
    }

    public void setTabMinMargin(int i10) {
        this.D0 = i10;
        ViewCompat.A0(this, i10, 0, i10, 0);
        requestLayout();
    }

    public void setTabMode(int i10) {
        if (i10 != this.f7802o0) {
            this.f7802o0 = i10;
            E();
        }
    }

    public void setTabPaddingBottom(int i10) {
        this.T = i10;
        requestLayout();
    }

    public void setTabPaddingEnd(int i10) {
        this.S = i10;
        requestLayout();
    }

    public void setTabPaddingStart(int i10) {
        this.Q = i10;
        requestLayout();
    }

    public void setTabPaddingTop(int i10) {
        this.R = i10;
        requestLayout();
    }

    public void setTabTextColors(ColorStateList colorStateList) {
        if (this.U != colorStateList) {
            this.U = colorStateList;
            l0();
            j0();
        }
    }

    public void setTabTextSize(float f10) {
        if (this.J != null) {
            this.G0 = f10;
            this.f7800m0 = f10;
        }
    }

    @Deprecated
    public void setTabsFromPagerAdapter(PagerAdapter pagerAdapter) {
        d0(pagerAdapter, false);
    }

    public void setUpdateindicatorposition(boolean z10) {
        this.L0 = z10;
    }

    public void setupWithViewPager(ViewPager viewPager) {
        h0(viewPager, true);
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return getTabScrollRange() > 0;
    }

    public void w(c cVar) {
        if (this.L.contains(cVar)) {
            return;
        }
        this.L.add(cVar);
    }

    public void x(COUITab cOUITab) {
        z(cOUITab, this.K.isEmpty());
    }

    public void y(COUITab cOUITab, int i10, boolean z10) {
        if (cOUITab.f7839a == this) {
            G(cOUITab, i10);
            B(cOUITab);
            if (z10) {
                cOUITab.h();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("COUITab belongs to a different TabLayout.");
    }

    public void z(COUITab cOUITab, boolean z10) {
        y(cOUITab, this.K.size(), z10);
    }

    public COUITabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, com.support.bars.R$attr.couiTabLayoutStyle);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public void addView(View view, int i10) {
        C(view);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return generateDefaultLayoutParams();
    }

    public COUITabLayout(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.COUITabLayoutBaseStyle);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup, android.view.ViewManager
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        C(view);
    }

    public COUITabLayout(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.K = new ArrayList<>();
        this.L = new ArrayList<>();
        this.M = new androidx.core.util.f(12);
        this.f7788a0 = -1;
        this.f7797j0 = 0;
        this.f7798k0 = 0.0f;
        this.f7806s0 = new ArgbEvaluator();
        this.L0 = false;
        this.R0 = new ArrayList<>();
        if (attributeSet != null) {
            int styleAttribute = attributeSet.getStyleAttribute();
            this.K0 = styleAttribute;
            if (styleAttribute == 0) {
                this.K0 = i10;
            }
        } else {
            this.K0 = 0;
        }
        this.V = Typeface.create("sans-serif-medium", 0);
        this.W = Typeface.create("sans-serif", 0);
        setHorizontalScrollBarEnabled(false);
        COUISlidingTabStrip cOUISlidingTabStrip = new COUISlidingTabStrip(context, this);
        this.J = cOUISlidingTabStrip;
        super.addView(cOUISlidingTabStrip, 0, new FrameLayout.LayoutParams(-2, -1));
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUITabLayout, i10, i11);
        cOUISlidingTabStrip.setSelectedIndicatorHeight(obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUITabLayout_couiTabIndicatorHeight, 0));
        int color = obtainStyledAttributes.getColor(R$styleable.COUITabLayout_couiTabIndicatorColor, 0);
        this.A0 = color;
        cOUISlidingTabStrip.setSelectedIndicatorColor(color);
        this.H0 = obtainStyledAttributes.getColor(R$styleable.COUITabLayout_couiTabBottomDividerColor, 0);
        this.I0 = obtainStyledAttributes.getBoolean(R$styleable.COUITabLayout_couiTabBottomDividerEnabled, false);
        cOUISlidingTabStrip.setBottomDividerColor(this.H0);
        setIndicatorBackgroundHeight(obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUITabLayout_couiTabIndicatorBackgroundHeight, 0));
        setIndicatorBackgroundColor(obtainStyledAttributes.getColor(R$styleable.COUITabLayout_couiTabIndicatorBackgroundColor, 0));
        setIndicatorBackgroundPaddingLeft(obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUITabLayout_couiTabIndicatorBackgroundPaddingLeft, 0));
        setIndicatorBackgroundPaddingRight(obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUITabLayout_couiTabIndicatorBackgroundPaddingRight, 0));
        setIndicatorWidthRatio(obtainStyledAttributes.getFloat(R$styleable.COUITabLayout_couiTabIndicatorWidthRatio, 0.0f));
        this.f7813z0 = getResources().getDimensionPixelOffset(R$dimen.coui_tablayout_default_resize_height);
        this.J0 = getResources().getDimensionPixelOffset(R$dimen.tablayout_long_text_view_height);
        this.C0 = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUITabLayout_couiTabMinDivider, (int) TypedValue.applyDimension(1, 20.0f, getResources().getDisplayMetrics()));
        this.D0 = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUITabLayout_couiTabMinMargin, (int) TypedValue.applyDimension(1, 24.0f, getResources().getDisplayMetrics()));
        this.E0 = getResources().getDimensionPixelOffset(R$dimen.coui_tablayout_indicator_padding);
        int i12 = this.D0;
        c0(i12, i12);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUITabLayout_couiTabPadding, -1);
        this.Q = dimensionPixelSize;
        this.R = dimensionPixelSize;
        this.S = dimensionPixelSize;
        this.T = dimensionPixelSize;
        this.Q = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUITabLayout_couiTabPaddingStart, dimensionPixelSize);
        this.R = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUITabLayout_couiTabPaddingTop, this.R);
        this.S = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUITabLayout_couiTabPaddingEnd, this.S);
        this.T = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUITabLayout_couiTabPaddingBottom, this.T);
        this.Q = Math.max(0, this.Q);
        this.R = Math.max(0, this.R);
        this.S = Math.max(0, this.S);
        this.T = Math.max(0, this.T);
        int resourceId = obtainStyledAttributes.getResourceId(R$styleable.COUITabLayout_couiTabTextAppearance, R$style.TextAppearance_Design_COUITab);
        this.f7799l0 = resourceId;
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(resourceId, androidx.appcompat.R$styleable.TextAppearance);
        try {
            float dimensionPixelSize2 = obtainStyledAttributes2.getDimensionPixelSize(androidx.appcompat.R$styleable.TextAppearance_android_textSize, 0);
            this.f7800m0 = dimensionPixelSize2;
            this.G0 = dimensionPixelSize2;
            this.U = obtainStyledAttributes2.getColorStateList(androidx.appcompat.R$styleable.TextAppearance_android_textColor);
            obtainStyledAttributes2.recycle();
            int i13 = R$styleable.COUITabLayout_couiTabTextColor;
            if (obtainStyledAttributes.hasValue(i13)) {
                this.U = obtainStyledAttributes.getColorStateList(i13);
            }
            this.B0 = COUIContextUtil.b(getContext(), R$attr.couiColorDisabledNeutral, 0);
            int i14 = R$styleable.COUITabLayout_couiTabSelectedTextColor;
            if (obtainStyledAttributes.hasValue(i14)) {
                this.U = H(this.U.getDefaultColor(), this.B0, obtainStyledAttributes.getColor(i14, 0));
            }
            this.f7793f0 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUITabLayout_couiTabMinWidth, -1);
            this.I = obtainStyledAttributes.getResourceId(R$styleable.COUITabLayout_couiTabBackground, 0);
            this.f7802o0 = obtainStyledAttributes.getInt(R$styleable.COUITabLayout_couiTabMode, 1);
            this.f7801n0 = obtainStyledAttributes.getInt(R$styleable.COUITabLayout_couiTabGravity, 0);
            this.f7792e0 = obtainStyledAttributes.getBoolean(R$styleable.COUITabLayout_couiTabEnableVibrator, true);
            this.N0 = this.D0;
            this.O0 = obtainStyledAttributes.getColor(R$styleable.COUITabLayout_couiTabIndicatorDisableColor, getResources().getColor(R$color.couiTabIndicatorDisableColor));
            int i15 = R$styleable.COUITabLayout_couiTabTextSize;
            if (obtainStyledAttributes.hasValue(i15)) {
                float dimension = obtainStyledAttributes.getDimension(i15, 0.0f);
                this.f7800m0 = dimension;
                this.G0 = dimension;
            }
            this.P0 = this.f7793f0;
            this.Q0 = this.f7788a0;
            this.M0 = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUITabLayout_couiTabButtonMarginEnd, -1);
            obtainStyledAttributes.recycle();
            this.f7791d0 = context.getResources().getDimensionPixelSize(com.support.appcompat.R$dimen.coui_dot_horizontal_offset);
            E();
            l0();
            setOverScrollMode(1);
        } catch (Throwable th) {
            obtainStyledAttributes2.recycle();
            throw th;
        }
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public void addView(View view, int i10, ViewGroup.LayoutParams layoutParams) {
        C(view);
    }
}
