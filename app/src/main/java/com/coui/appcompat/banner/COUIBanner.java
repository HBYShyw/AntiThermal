package com.coui.appcompat.banner;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import com.support.nearx.R$id;
import com.support.nearx.R$layout;
import com.support.nearx.R$styleable;
import i3.COUIViewPager2SlideHelper;
import n1.COUIBannerOnPageChangeCallback;
import n1.COUIBannerUtil;
import o1.COUIBannerBaseAdapter;
import p1.COUIMarginPageTransformer;
import p1.COUIScalePageTransformer;

/* loaded from: classes.dex */
public class COUIBanner extends ConstraintLayout {
    protected COUIBannerRecyclerView B;
    protected ViewPager2 C;
    protected COUIViewPager2SlideHelper D;
    protected COUIPageIndicatorKit E;
    private CompositePageTransformer F;
    private ViewPager2.i G;
    private COUIBannerOnPageChangeCallback H;
    private int I;
    private boolean J;
    private int K;
    private int L;
    private int M;
    private int N;
    private boolean O;
    private int P;
    private int Q;
    private int R;
    private Interpolator S;
    private final Runnable T;

    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIBanner.this.J();
            if (COUIBanner.this.H()) {
                COUIBanner.this.R();
            }
        }
    }

    public COUIBanner(Context context) {
        this(context, null);
    }

    private void F() {
        this.H = new COUIBannerOnPageChangeCallback(this);
        this.F = new CompositePageTransformer();
        this.C.setOrientation(0);
        this.C.setOffscreenPageLimit(2);
        this.C.j(this.H);
        this.C.setPageTransformer(this.F);
        COUIViewPager2SlideHelper cOUIViewPager2SlideHelper = new COUIViewPager2SlideHelper(this.C);
        this.D = cOUIViewPager2SlideHelper;
        cOUIViewPager2SlideHelper.f(this.R);
        this.D.g(this.S);
        Q(this.L, this.M, this.N, 1.0f);
    }

    private void G() {
        this.C = (ViewPager2) findViewById(R$id.viewpager);
        this.E = (COUIPageIndicatorKit) findViewById(R$id.indicator);
        COUIBannerRecyclerView cOUIBannerRecyclerView = (COUIBannerRecyclerView) findViewById(R$id.recycler);
        this.B = cOUIBannerRecyclerView;
        if (this.P == 0) {
            this.E.setVisibility(0);
            this.C.setVisibility(0);
            this.B.setVisibility(8);
        } else {
            cOUIBannerRecyclerView.setVisibility(0);
            this.E.setVisibility(8);
            this.C.setVisibility(8);
        }
    }

    private void K(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIBanner);
        int integer = obtainStyledAttributes.getInteger(R$styleable.COUIBanner_couiBannerType, 0);
        this.P = integer;
        this.Q = integer;
        this.J = obtainStyledAttributes.getBoolean(R$styleable.COUIBanner_couiAutoLoop, true);
        this.K = obtainStyledAttributes.getInteger(R$styleable.COUIBanner_couiLoopDuration, 5);
        this.L = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIBanner_couiLeftItemWidth, 0);
        this.M = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIBanner_couiRightItemWidth, 0);
        this.N = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIBanner_couiPageMargin, COUIBannerUtil.f15630a);
        if (getContext().getResources().getConfiguration().screenWidthDp >= 600) {
            this.P = 1;
        }
        obtainStyledAttributes.recycle();
    }

    private void M() {
        getHandler().removeCallbacks(this.T);
    }

    private void N(COUIBannerBaseAdapter cOUIBannerBaseAdapter, boolean z10) {
        getType();
        throw null;
    }

    private void P(int i10, int i11) {
        if (this.C.getOrientation() == 1) {
            ViewPager2 viewPager2 = this.C;
            viewPager2.setPadding(viewPager2.getPaddingLeft(), i10, this.C.getPaddingRight(), i11);
        } else {
            ViewPager2 viewPager22 = this.C;
            viewPager22.setPadding(i10, viewPager22.getPaddingTop(), i11, this.C.getPaddingBottom());
        }
        this.C.setClipToPadding(false);
        this.C.setClipChildren(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void R() {
        getHandler().removeCallbacks(this.T);
        getHandler().postDelayed(this.T, (this.K * 1000) + this.R);
    }

    private void setInfiniteLoop(boolean z10) {
        this.O = z10;
        if (!I()) {
            setAutoLoop(false);
        }
        setStartPosition(I() ? this.I : 0);
    }

    private void setRecyclerViewPadding(int i10) {
        P(i10, i10);
    }

    private void setTypeWithDataChange(int i10) {
        this.P = i10;
        throw null;
    }

    public void E(ViewPager2.k kVar) {
        this.F.b(kVar);
    }

    public boolean H() {
        return this.J;
    }

    public boolean I() {
        return this.O;
    }

    public void J() {
        if (this.P != 0) {
            return;
        }
        this.D.h(((-(getPageMargin() * 2)) - getLeftItemWidth()) - getRightItemWidth());
        this.D.e();
    }

    public void L() {
        getHandler().removeCallbacksAndMessages(null);
        this.B.removeAllViews();
        this.C.removeAllViews();
        this.E.removeAllViews();
    }

    public void O(int i10, boolean z10) {
        this.C.m(i10, z10);
    }

    public void Q(int i10, int i11, int i12, float f10) {
        if (i12 > 0) {
            E(new COUIMarginPageTransformer(i12));
        }
        if (f10 < 1.0f && f10 > 0.0f) {
            E(new COUIScalePageTransformer(f10));
        }
        P(i10 + i12, i11 + i12);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (action != 0) {
            if ((action == 1 || action == 3) && H() && this.P == 0) {
                R();
            }
        } else if (H() && this.P == 0) {
            M();
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public COUIBannerBaseAdapter getAdapter() {
        return null;
    }

    public int getCurrentItem() {
        return this.C.getCurrentItem();
    }

    public COUIPageIndicatorKit getIndicator() {
        return this.E;
    }

    public int getItemCount() {
        getAdapter();
        return 0;
    }

    public int getLeftItemWidth() {
        return this.L;
    }

    public int getLoopDuration() {
        return this.K;
    }

    public ViewPager2.i getOnPageChangeCallback() {
        return this.G;
    }

    public int getPageMargin() {
        return this.N;
    }

    public int getRealCount() {
        getAdapter();
        return 0;
    }

    public int getRightItemWidth() {
        return this.M;
    }

    public int getType() {
        return this.P;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (H() && this.P == 0) {
            R();
        }
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (configuration.screenWidthDp >= 600) {
            setTypeWithDataChange(1);
            return;
        }
        int i10 = this.P;
        int i11 = this.Q;
        if (i10 != i11) {
            setType(i11);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        L();
    }

    public void setAutoLoop(boolean z10) {
        if (!z10) {
            M();
        } else if (this.P == 0) {
            R();
        }
        if (this.P == 1) {
            return;
        }
        this.J = z10;
    }

    public void setBannerAdapter(COUIBannerBaseAdapter cOUIBannerBaseAdapter) {
        N(cOUIBannerBaseAdapter, true);
    }

    public void setCurrentItem(int i10) {
        O(i10, true);
    }

    public void setDuration(int i10) {
        this.R = i10;
        this.D.f(i10);
    }

    public void setInterpolator(Interpolator interpolator) {
        this.S = interpolator;
        this.D.g(interpolator);
    }

    public void setLeftItemWidth(int i10) {
        this.L = i10;
        Q(i10, this.M, this.N, 1.0f);
    }

    public void setLoopDuration(int i10) {
        this.K = i10;
        if (H() && this.P == 0) {
            R();
        }
    }

    public void setPageMargin(int i10) {
        this.N = i10;
        Q(this.L, this.M, i10, 1.0f);
    }

    public void setPageTransformer(ViewPager2.k kVar) {
        this.C.setPageTransformer(kVar);
    }

    public void setRightItemWidth(int i10) {
        this.M = i10;
        Q(this.L, i10, this.N, 1.0f);
    }

    public void setStartPosition(int i10) {
        this.I = i10;
    }

    public void setType(int i10) {
        this.P = i10;
        this.Q = i10;
        setTypeWithDataChange(i10);
    }

    public COUIBanner(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIBanner(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.I = 1;
        this.J = true;
        this.K = 5;
        this.L = 0;
        this.M = 0;
        this.N = COUIBannerUtil.f15630a;
        this.O = true;
        this.P = 0;
        this.Q = 0;
        this.R = 950;
        this.S = new PathInterpolator(0.2f, 0.0f, 0.1f, 1.0f);
        this.T = new a();
        LayoutInflater.from(context).inflate(R$layout.coui_banner_content_layout, this);
        if (attributeSet != null) {
            K(context, attributeSet);
        }
        G();
        F();
    }
}
