package com.coui.appcompat.scroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import com.coui.appcompat.viewpager.COUIViewPager2;
import com.support.nearx.R$styleable;
import o2.COUIScrollViewProxy;
import p2.COUICVPScrollViewProxy;
import p2.COUINSVScrollViewProxy;
import p2.COUIRVScrollViewProxy;
import p2.COUISVScrollViewProxy;
import p2.COUIVP2ScrollViewProxy;
import p2.COUIVPScrollViewProxy;

/* loaded from: classes.dex */
public class COUINestedScrollableHost extends FrameLayout {

    /* renamed from: e, reason: collision with root package name */
    protected final int f7323e;

    /* renamed from: f, reason: collision with root package name */
    protected final PointF f7324f;

    /* renamed from: g, reason: collision with root package name */
    protected final PointF f7325g;

    /* renamed from: h, reason: collision with root package name */
    protected COUIScrollViewProxy<?> f7326h;

    /* renamed from: i, reason: collision with root package name */
    protected COUIScrollViewProxy<?> f7327i;

    /* renamed from: j, reason: collision with root package name */
    protected int f7328j;

    /* renamed from: k, reason: collision with root package name */
    protected int f7329k;

    /* renamed from: l, reason: collision with root package name */
    private COUIScrollViewProxy<?> f7330l;

    /* renamed from: m, reason: collision with root package name */
    private COUIScrollViewProxy<?> f7331m;

    public COUINestedScrollableHost(Context context) {
        this(context, null);
    }

    private View d(Class<?> cls) {
        for (int i10 = 0; i10 < getChildCount(); i10++) {
            if (cls.isInstance(getChildAt(i10))) {
                return getChildAt(i10);
            }
        }
        return null;
    }

    private View e(Class<?> cls) {
        View view = (View) getParent();
        if (view != null) {
            while (!cls.isInstance(view) && view != null) {
                view = (View) view.getParent();
            }
            return view;
        }
        throw new IllegalStateException("The NearNestedScrollable must have parent class");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public COUIScrollViewProxy<?> a(int i10) {
        if (i10 == 0) {
            return new COUIVPScrollViewProxy((ViewPager) d(ViewPager.class));
        }
        if (i10 == 1) {
            return new COUIVP2ScrollViewProxy((ViewPager2) d(ViewPager2.class));
        }
        if (i10 == 2) {
            return new COUIRVScrollViewProxy((RecyclerView) d(RecyclerView.class));
        }
        if (i10 == 3) {
            return new COUISVScrollViewProxy((ScrollView) d(ScrollView.class));
        }
        if (i10 == 4) {
            return new COUINSVScrollViewProxy((NestedScrollView) d(NestedScrollView.class));
        }
        if (i10 == 5) {
            return new COUICVPScrollViewProxy((COUIViewPager2) d(COUIViewPager2.class));
        }
        if (i10 != Integer.MAX_VALUE) {
            return null;
        }
        return this.f7331m;
    }

    protected COUIScrollViewProxy<?> b(int i10) {
        if (i10 == 0) {
            return new COUIVPScrollViewProxy((ViewPager) e(ViewPager.class));
        }
        if (i10 == 1) {
            return new COUIVP2ScrollViewProxy((ViewPager2) e(ViewPager2.class));
        }
        if (i10 == 2) {
            return new COUIRVScrollViewProxy((RecyclerView) e(RecyclerView.class));
        }
        if (i10 == 3) {
            return new COUISVScrollViewProxy((ScrollView) e(ScrollView.class));
        }
        if (i10 == 4) {
            return new COUINSVScrollViewProxy((NestedScrollView) e(NestedScrollView.class));
        }
        if (i10 == 5) {
            return new COUICVPScrollViewProxy((COUIViewPager2) e(COUIViewPager2.class));
        }
        if (i10 != Integer.MAX_VALUE) {
            return null;
        }
        return this.f7330l;
    }

    protected void c() {
        this.f7326h = b(this.f7328j);
        this.f7327i = a(this.f7329k);
    }

    protected void f(MotionEvent motionEvent) {
        COUIScrollViewProxy<?> cOUIScrollViewProxy = this.f7326h;
        if (cOUIScrollViewProxy == null || this.f7327i == null) {
            return;
        }
        int a10 = cOUIScrollViewProxy.a();
        if (this.f7327i.b(a10, -1) || this.f7327i.b(a10, 1)) {
            if (motionEvent.getAction() == 0) {
                this.f7324f.x = motionEvent.getX();
                this.f7324f.y = motionEvent.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                return;
            }
            if (motionEvent.getAction() == 2) {
                this.f7325g.x = motionEvent.getX();
                this.f7325g.y = motionEvent.getY();
                PointF pointF = this.f7325g;
                float f10 = pointF.x;
                PointF pointF2 = this.f7324f;
                float f11 = f10 - pointF2.x;
                float f12 = pointF.y - pointF2.y;
                boolean z10 = a10 == 0;
                float abs = Math.abs(f11) * (z10 ? 0.5f : 1.0f);
                float abs2 = Math.abs(f12) * (z10 ? 1.0f : 0.5f);
                int i10 = this.f7323e;
                if (abs > i10 || abs2 > i10) {
                    if (z10 != (abs > abs2)) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        return;
                    }
                    if (this.f7327i.b(a10, z10 ? (int) f11 : (int) f12)) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
            }
        }
    }

    public COUIScrollViewProxy<?> getChildCustom() {
        return this.f7331m;
    }

    public COUIScrollViewProxy<?> getParentCustom() {
        return this.f7330l;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        c();
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        f(motionEvent);
        return super.onInterceptTouchEvent(motionEvent);
    }

    public void setChildCustom(COUIScrollViewProxy<?> cOUIScrollViewProxy) {
        this.f7331m = cOUIScrollViewProxy;
    }

    public void setParentCustom(COUIScrollViewProxy<?> cOUIScrollViewProxy) {
        this.f7330l = cOUIScrollViewProxy;
    }

    public COUINestedScrollableHost(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUINestedScrollableHost(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f7323e = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.f7324f = new PointF();
        this.f7325g = new PointF();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUINestedScrollableHost);
        this.f7328j = obtainStyledAttributes.getInt(R$styleable.COUINestedScrollableHost_couiParent, 0);
        this.f7329k = obtainStyledAttributes.getInt(R$styleable.COUINestedScrollableHost_couiChild, 0);
        obtainStyledAttributes.recycle();
    }
}
