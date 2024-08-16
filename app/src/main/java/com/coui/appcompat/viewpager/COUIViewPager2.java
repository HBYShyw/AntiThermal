package com.coui.appcompat.viewpager;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.R$styleable;
import androidx.viewpager2.adapter.StatefulAdapter;
import androidx.viewpager2.widget.ViewPager2;

/* loaded from: classes.dex */
public class COUIViewPager2 extends ViewGroup {
    static boolean A = true;

    /* renamed from: e, reason: collision with root package name */
    private final Rect f8062e;

    /* renamed from: f, reason: collision with root package name */
    private final Rect f8063f;

    /* renamed from: g, reason: collision with root package name */
    private COUICompositeOnPageChangeCallback f8064g;

    /* renamed from: h, reason: collision with root package name */
    int f8065h;

    /* renamed from: i, reason: collision with root package name */
    boolean f8066i;

    /* renamed from: j, reason: collision with root package name */
    private RecyclerView.j f8067j;

    /* renamed from: k, reason: collision with root package name */
    private LinearLayoutManager f8068k;

    /* renamed from: l, reason: collision with root package name */
    private int f8069l;

    /* renamed from: m, reason: collision with root package name */
    private Parcelable f8070m;

    /* renamed from: n, reason: collision with root package name */
    l f8071n;

    /* renamed from: o, reason: collision with root package name */
    private PagerSnapHelper f8072o;

    /* renamed from: p, reason: collision with root package name */
    COUIScrollEventAdapter f8073p;

    /* renamed from: q, reason: collision with root package name */
    private COUICompositeOnPageChangeCallback f8074q;

    /* renamed from: r, reason: collision with root package name */
    private COUIFakeDrag f8075r;

    /* renamed from: s, reason: collision with root package name */
    private COUIPageTransformerAdapter f8076s;

    /* renamed from: t, reason: collision with root package name */
    private RecyclerView.m f8077t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f8078u;

    /* renamed from: v, reason: collision with root package name */
    private boolean f8079v;

    /* renamed from: w, reason: collision with root package name */
    private int f8080w;

    /* renamed from: x, reason: collision with root package name */
    e f8081x;

    /* renamed from: y, reason: collision with root package name */
    private Interpolator f8082y;

    /* renamed from: z, reason: collision with root package name */
    private int f8083z;

    /* loaded from: classes.dex */
    class a extends g {
        a() {
            super(null);
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.g, androidx.recyclerview.widget.RecyclerView.j
        public void onChanged() {
            COUIViewPager2 cOUIViewPager2 = COUIViewPager2.this;
            cOUIViewPager2.f8066i = true;
            cOUIViewPager2.f8073p.l();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b extends ViewPager2.i {
        b() {
        }

        @Override // androidx.viewpager2.widget.ViewPager2.i
        public void a(int i10) {
            if (i10 == 0) {
                COUIViewPager2.this.r();
            }
        }

        @Override // androidx.viewpager2.widget.ViewPager2.i
        public void c(int i10) {
            COUIViewPager2 cOUIViewPager2 = COUIViewPager2.this;
            if (cOUIViewPager2.f8065h != i10) {
                cOUIViewPager2.f8065h = i10;
                cOUIViewPager2.f8081x.q();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c extends ViewPager2.i {
        c() {
        }

        @Override // androidx.viewpager2.widget.ViewPager2.i
        public void c(int i10) {
            COUIViewPager2.this.clearFocus();
            if (COUIViewPager2.this.hasFocus()) {
                COUIViewPager2.this.f8071n.requestFocus(2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements RecyclerView.q {
        d() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.q
        public void a(View view) {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.q
        public void b(View view) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            if (((ViewGroup.MarginLayoutParams) layoutParams).width != -1 || ((ViewGroup.MarginLayoutParams) layoutParams).height != -1) {
                throw new IllegalStateException("Pages must fill the whole ViewPager2 (use match_parent)");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public abstract class e {
        private e() {
        }

        boolean a() {
            return false;
        }

        boolean b(int i10) {
            return false;
        }

        boolean c(int i10, Bundle bundle) {
            return false;
        }

        boolean d() {
            return false;
        }

        void e(RecyclerView.h<?> hVar) {
        }

        void f(RecyclerView.h<?> hVar) {
        }

        String g() {
            throw new IllegalStateException("Not implemented.");
        }

        void h(COUICompositeOnPageChangeCallback cOUICompositeOnPageChangeCallback, RecyclerView recyclerView) {
        }

        void i(AccessibilityNodeInfo accessibilityNodeInfo) {
        }

        void j(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        }

        boolean k(int i10) {
            throw new IllegalStateException("Not implemented.");
        }

        boolean l(int i10, Bundle bundle) {
            throw new IllegalStateException("Not implemented.");
        }

        void m() {
        }

        CharSequence n() {
            throw new IllegalStateException("Not implemented.");
        }

        void o(AccessibilityEvent accessibilityEvent) {
        }

        void p() {
        }

        void q() {
        }

        void r() {
        }

        void s() {
        }

        /* synthetic */ e(COUIViewPager2 cOUIViewPager2, a aVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f extends e {
        f() {
            super(COUIViewPager2.this, null);
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public boolean b(int i10) {
            return (i10 == 8192 || i10 == 4096) && !COUIViewPager2.this.h();
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public boolean d() {
            return true;
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public void j(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (COUIViewPager2.this.h()) {
                return;
            }
            accessibilityNodeInfoCompat.O(AccessibilityNodeInfoCompat.a.f2331r);
            accessibilityNodeInfoCompat.O(AccessibilityNodeInfoCompat.a.f2330q);
            accessibilityNodeInfoCompat.s0(false);
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public boolean k(int i10) {
            if (b(i10)) {
                return false;
            }
            throw new IllegalStateException();
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public CharSequence n() {
            if (d()) {
                return "androidx.viewpager.widget.ViewPager";
            }
            throw new IllegalStateException();
        }
    }

    /* loaded from: classes.dex */
    private static abstract class g extends RecyclerView.j {
        private g() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public abstract void onChanged();

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public final void onItemRangeChanged(int i10, int i11) {
            onChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public final void onItemRangeInserted(int i10, int i11) {
            onChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public final void onItemRangeMoved(int i10, int i11, int i12) {
            onChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public final void onItemRangeRemoved(int i10, int i11) {
            onChanged();
        }

        /* synthetic */ g(a aVar) {
            this();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public final void onItemRangeChanged(int i10, int i11, Object obj) {
            onChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class h extends LinearLayoutManager {
        h(Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
        public void K1(RecyclerView recyclerView, RecyclerView.z zVar, int i10) {
            i iVar = new i(recyclerView.getContext());
            iVar.p(i10);
            L1(iVar);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.recyclerview.widget.LinearLayoutManager
        public void O1(RecyclerView.z zVar, int[] iArr) {
            int offscreenPageLimit = COUIViewPager2.this.getOffscreenPageLimit();
            if (offscreenPageLimit == -1) {
                super.O1(zVar, iArr);
                return;
            }
            int pageSize = COUIViewPager2.this.getPageSize() * offscreenPageLimit;
            iArr[0] = pageSize;
            iArr[1] = pageSize;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.p
        public void P0(RecyclerView.v vVar, RecyclerView.z zVar, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.P0(vVar, zVar, accessibilityNodeInfoCompat);
            COUIViewPager2.this.f8081x.j(accessibilityNodeInfoCompat);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.p
        public boolean j1(RecyclerView.v vVar, RecyclerView.z zVar, int i10, Bundle bundle) {
            if (COUIViewPager2.this.f8081x.b(i10)) {
                return COUIViewPager2.this.f8081x.k(i10);
            }
            return super.j1(vVar, zVar, i10, bundle);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.p
        public boolean u1(RecyclerView recyclerView, View view, Rect rect, boolean z10, boolean z11) {
            return false;
        }
    }

    /* loaded from: classes.dex */
    private class i extends LinearSmoothScroller {
        public i(Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.LinearSmoothScroller, androidx.recyclerview.widget.RecyclerView.y
        protected void o(View view, RecyclerView.z zVar, RecyclerView.y.a aVar) {
            int t7 = t(view, z());
            int u7 = u(view, B());
            if (w((int) Math.sqrt((t7 * t7) + (u7 * u7))) > 0) {
                aVar.d(-t7, -u7, COUIViewPager2.this.f8083z, COUIViewPager2.this.f8082y);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class j extends e {

        /* renamed from: b, reason: collision with root package name */
        private final AccessibilityViewCommand f8094b;

        /* renamed from: c, reason: collision with root package name */
        private final AccessibilityViewCommand f8095c;

        /* renamed from: d, reason: collision with root package name */
        private RecyclerView.j f8096d;

        /* loaded from: classes.dex */
        class a implements AccessibilityViewCommand {
            a() {
            }

            @Override // androidx.core.view.accessibility.AccessibilityViewCommand
            public boolean perform(View view, AccessibilityViewCommand.a aVar) {
                j.this.v(((COUIViewPager2) view).getCurrentItem() + 1);
                return true;
            }
        }

        /* loaded from: classes.dex */
        class b implements AccessibilityViewCommand {
            b() {
            }

            @Override // androidx.core.view.accessibility.AccessibilityViewCommand
            public boolean perform(View view, AccessibilityViewCommand.a aVar) {
                j.this.v(((COUIViewPager2) view).getCurrentItem() - 1);
                return true;
            }
        }

        /* loaded from: classes.dex */
        class c extends g {
            c() {
                super(null);
            }

            @Override // com.coui.appcompat.viewpager.COUIViewPager2.g, androidx.recyclerview.widget.RecyclerView.j
            public void onChanged() {
                j.this.w();
            }
        }

        j() {
            super(COUIViewPager2.this, null);
            this.f8094b = new a();
            this.f8095c = new b();
        }

        private void t(AccessibilityNodeInfo accessibilityNodeInfo) {
            int i10;
            int i11;
            if (COUIViewPager2.this.getAdapter() == null) {
                i10 = 0;
                i11 = 0;
            } else if (COUIViewPager2.this.getOrientation() == 1) {
                i10 = COUIViewPager2.this.getAdapter().getItemCount();
                i11 = 0;
            } else {
                i11 = COUIViewPager2.this.getAdapter().getItemCount();
                i10 = 0;
            }
            AccessibilityNodeInfoCompat.C0(accessibilityNodeInfo).X(AccessibilityNodeInfoCompat.b.b(i10, i11, false, 0));
        }

        private void u(AccessibilityNodeInfo accessibilityNodeInfo) {
            int itemCount;
            RecyclerView.h adapter = COUIViewPager2.this.getAdapter();
            if (adapter == null || (itemCount = adapter.getItemCount()) == 0 || !COUIViewPager2.this.h()) {
                return;
            }
            if (COUIViewPager2.this.f8065h > 0) {
                accessibilityNodeInfo.addAction(8192);
            }
            if (COUIViewPager2.this.f8065h < itemCount - 1) {
                accessibilityNodeInfo.addAction(4096);
            }
            accessibilityNodeInfo.setScrollable(true);
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public boolean a() {
            return true;
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public boolean c(int i10, Bundle bundle) {
            return i10 == 8192 || i10 == 4096;
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public void e(RecyclerView.h<?> hVar) {
            w();
            if (hVar != null) {
                hVar.registerAdapterDataObserver(this.f8096d);
            }
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public void f(RecyclerView.h<?> hVar) {
            if (hVar != null) {
                hVar.unregisterAdapterDataObserver(this.f8096d);
            }
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public String g() {
            if (a()) {
                return "androidx.viewpager.widget.ViewPager";
            }
            throw new IllegalStateException();
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public void h(COUICompositeOnPageChangeCallback cOUICompositeOnPageChangeCallback, RecyclerView recyclerView) {
            ViewCompat.w0(recyclerView, 2);
            this.f8096d = new c();
            if (ViewCompat.v(COUIViewPager2.this) == 0) {
                ViewCompat.w0(COUIViewPager2.this, 1);
            }
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public void i(AccessibilityNodeInfo accessibilityNodeInfo) {
            t(accessibilityNodeInfo);
            u(accessibilityNodeInfo);
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public boolean l(int i10, Bundle bundle) {
            int currentItem;
            if (c(i10, bundle)) {
                if (i10 == 8192) {
                    currentItem = COUIViewPager2.this.getCurrentItem() - 1;
                } else {
                    currentItem = COUIViewPager2.this.getCurrentItem() + 1;
                }
                v(currentItem);
                return true;
            }
            throw new IllegalStateException();
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public void m() {
            w();
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public void o(AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.setSource(COUIViewPager2.this);
            accessibilityEvent.setClassName(g());
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public void p() {
            w();
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public void q() {
            w();
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public void r() {
            w();
        }

        @Override // com.coui.appcompat.viewpager.COUIViewPager2.e
        public void s() {
            w();
        }

        void v(int i10) {
            if (COUIViewPager2.this.h()) {
                COUIViewPager2.this.n(i10, true);
            }
        }

        void w() {
            int itemCount;
            COUIViewPager2 cOUIViewPager2 = COUIViewPager2.this;
            int i10 = R.id.accessibilityActionPageLeft;
            ViewCompat.e0(cOUIViewPager2, R.id.accessibilityActionPageLeft);
            ViewCompat.e0(cOUIViewPager2, R.id.accessibilityActionPageRight);
            ViewCompat.e0(cOUIViewPager2, R.id.accessibilityActionPageUp);
            ViewCompat.e0(cOUIViewPager2, R.id.accessibilityActionPageDown);
            if (COUIViewPager2.this.getAdapter() == null || (itemCount = COUIViewPager2.this.getAdapter().getItemCount()) == 0 || !COUIViewPager2.this.h()) {
                return;
            }
            if (COUIViewPager2.this.getOrientation() == 0) {
                boolean g6 = COUIViewPager2.this.g();
                int i11 = g6 ? 16908360 : 16908361;
                if (g6) {
                    i10 = 16908361;
                }
                if (COUIViewPager2.this.f8065h < itemCount - 1) {
                    ViewCompat.g0(cOUIViewPager2, new AccessibilityNodeInfoCompat.a(i11, null), null, this.f8094b);
                }
                if (COUIViewPager2.this.f8065h > 0) {
                    ViewCompat.g0(cOUIViewPager2, new AccessibilityNodeInfoCompat.a(i10, null), null, this.f8095c);
                    return;
                }
                return;
            }
            if (COUIViewPager2.this.f8065h < itemCount - 1) {
                ViewCompat.g0(cOUIViewPager2, new AccessibilityNodeInfoCompat.a(R.id.accessibilityActionPageDown, null), null, this.f8094b);
            }
            if (COUIViewPager2.this.f8065h > 0) {
                ViewCompat.g0(cOUIViewPager2, new AccessibilityNodeInfoCompat.a(R.id.accessibilityActionPageUp, null), null, this.f8095c);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class k extends PagerSnapHelper {
        k() {
        }

        @Override // androidx.recyclerview.widget.PagerSnapHelper, androidx.recyclerview.widget.SnapHelper
        public View f(RecyclerView.p pVar) {
            if (COUIViewPager2.this.f()) {
                return null;
            }
            return super.f(pVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class l extends COUIRecyclerView {
        l(Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public CharSequence getAccessibilityClassName() {
            if (COUIViewPager2.this.f8081x.d()) {
                return COUIViewPager2.this.f8081x.n();
            }
            return super.getAccessibilityClassName();
        }

        @Override // android.view.View
        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setFromIndex(COUIViewPager2.this.f8065h);
            accessibilityEvent.setToIndex(COUIViewPager2.this.f8065h);
            COUIViewPager2.this.f8081x.o(accessibilityEvent);
        }

        @Override // androidx.recyclerview.widget.COUIRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return COUIViewPager2.this.h() && super.onInterceptTouchEvent(motionEvent);
        }

        @Override // androidx.recyclerview.widget.COUIRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.View
        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return COUIViewPager2.this.h() && super.onTouchEvent(motionEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class m implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private final int f8103e;

        /* renamed from: f, reason: collision with root package name */
        private final RecyclerView f8104f;

        m(int i10, RecyclerView recyclerView) {
            this.f8103e = i10;
            this.f8104f = recyclerView;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f8104f.smoothScrollToPosition(this.f8103e);
        }
    }

    public COUIViewPager2(Context context) {
        super(context);
        this.f8062e = new Rect();
        this.f8063f = new Rect();
        this.f8064g = new COUICompositeOnPageChangeCallback(3);
        this.f8066i = false;
        this.f8067j = new a();
        this.f8069l = -1;
        this.f8077t = null;
        this.f8078u = false;
        this.f8079v = true;
        this.f8080w = -1;
        this.f8082y = new LinearInterpolator();
        this.f8083z = 500;
        e(context, null);
    }

    private RecyclerView.q d() {
        return new d();
    }

    private void e(Context context, AttributeSet attributeSet) {
        e fVar;
        if (A) {
            fVar = new j();
        } else {
            fVar = new f();
        }
        this.f8081x = fVar;
        l lVar = new l(context);
        this.f8071n = lVar;
        lVar.setId(ViewCompat.i());
        this.f8071n.setDescendantFocusability(131072);
        this.f8071n.setIsUseNativeOverScroll(true);
        h hVar = new h(context);
        this.f8068k = hVar;
        this.f8071n.setLayoutManager(hVar);
        this.f8071n.setScrollingTouchSlop(1);
        o(context, attributeSet);
        this.f8071n.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.f8071n.addOnChildAttachStateChangeListener(d());
        COUIScrollEventAdapter cOUIScrollEventAdapter = new COUIScrollEventAdapter(this);
        this.f8073p = cOUIScrollEventAdapter;
        this.f8075r = new COUIFakeDrag(this, cOUIScrollEventAdapter, this.f8071n);
        k kVar = new k();
        this.f8072o = kVar;
        kVar.b(this.f8071n);
        this.f8071n.addOnScrollListener(this.f8073p);
        COUICompositeOnPageChangeCallback cOUICompositeOnPageChangeCallback = new COUICompositeOnPageChangeCallback(3);
        this.f8074q = cOUICompositeOnPageChangeCallback;
        this.f8073p.o(cOUICompositeOnPageChangeCallback);
        b bVar = new b();
        c cVar = new c();
        this.f8074q.d(bVar);
        this.f8074q.d(cVar);
        this.f8081x.h(this.f8074q, this.f8071n);
        this.f8074q.d(this.f8064g);
        COUIPageTransformerAdapter cOUIPageTransformerAdapter = new COUIPageTransformerAdapter(this.f8068k);
        this.f8076s = cOUIPageTransformerAdapter;
        this.f8074q.d(cOUIPageTransformerAdapter);
        l lVar2 = this.f8071n;
        attachViewToParent(lVar2, 0, lVar2.getLayoutParams());
    }

    private void i(RecyclerView.h<?> hVar) {
        if (hVar != null) {
            hVar.registerAdapterDataObserver(this.f8067j);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void l() {
        RecyclerView.h adapter;
        if (this.f8069l == -1 || (adapter = getAdapter()) == 0) {
            return;
        }
        Parcelable parcelable = this.f8070m;
        if (parcelable != null) {
            if (adapter instanceof StatefulAdapter) {
                ((StatefulAdapter) adapter).b(parcelable);
            }
            this.f8070m = null;
        }
        int max = Math.max(0, Math.min(this.f8069l, adapter.getItemCount() - 1));
        this.f8065h = max;
        this.f8069l = -1;
        this.f8071n.scrollToPosition(max);
        this.f8081x.m();
    }

    private void o(Context context, AttributeSet attributeSet) {
        int[] iArr = R$styleable.ViewPager2;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr);
        saveAttributeDataForStyleable(context, iArr, attributeSet, obtainStyledAttributes, 0, 0);
        try {
            setOrientation(obtainStyledAttributes.getInt(R$styleable.ViewPager2_android_orientation, 0));
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    private void p(RecyclerView.h<?> hVar) {
        if (hVar != null) {
            hVar.unregisterAdapterDataObserver(this.f8067j);
        }
    }

    public boolean c() {
        return this.f8075r.b();
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int i10) {
        return this.f8071n.canScrollHorizontally(i10);
    }

    @Override // android.view.View
    public boolean canScrollVertically(int i10) {
        return this.f8071n.canScrollVertically(i10);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        Parcelable parcelable = sparseArray.get(getId());
        if (parcelable instanceof SavedState) {
            int i10 = ((SavedState) parcelable).f8084e;
            sparseArray.put(this.f8071n.getId(), sparseArray.get(i10));
            sparseArray.remove(i10);
        }
        super.dispatchRestoreInstanceState(sparseArray);
        l();
    }

    public boolean f() {
        return this.f8075r.d();
    }

    public boolean g() {
        return this.f8068k.Z() == 1;
    }

    @Override // android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        if (this.f8081x.a()) {
            return this.f8081x.g();
        }
        return super.getAccessibilityClassName();
    }

    public RecyclerView.h getAdapter() {
        return this.f8071n.getAdapter();
    }

    public int getCurrentItem() {
        return this.f8065h;
    }

    public int getDuration() {
        return this.f8083z;
    }

    public Interpolator getInterpolator() {
        return this.f8082y;
    }

    public int getItemDecorationCount() {
        return this.f8071n.getItemDecorationCount();
    }

    public int getOffscreenPageLimit() {
        return this.f8080w;
    }

    @SuppressLint({"WrongConstant"})
    public int getOrientation() {
        return this.f8068k.p2();
    }

    int getPageSize() {
        int height;
        int paddingBottom;
        l lVar = this.f8071n;
        if (getOrientation() == 0) {
            height = lVar.getWidth() - lVar.getPaddingLeft();
            paddingBottom = lVar.getPaddingRight();
        } else {
            height = lVar.getHeight() - lVar.getPaddingTop();
            paddingBottom = lVar.getPaddingBottom();
        }
        return height - paddingBottom;
    }

    public int getScrollState() {
        return this.f8073p.f();
    }

    public boolean h() {
        return this.f8079v;
    }

    public void j(ViewPager2.i iVar) {
        this.f8064g.d(iVar);
    }

    public void k() {
        if (this.f8076s.d() == null) {
            return;
        }
        double e10 = this.f8073p.e();
        int i10 = (int) e10;
        float f10 = (float) (e10 - i10);
        this.f8076s.b(i10, f10, Math.round(getPageSize() * f10));
    }

    public void m(int i10, boolean z10) {
        if (!f()) {
            n(i10, z10);
            return;
        }
        throw new IllegalStateException("Cannot change current item when ViewPager2 is fake dragging");
    }

    void n(int i10, boolean z10) {
        RecyclerView.h adapter = getAdapter();
        if (adapter == null) {
            if (this.f8069l != -1) {
                this.f8069l = Math.max(i10, 0);
                return;
            }
            return;
        }
        if (adapter.getItemCount() <= 0) {
            return;
        }
        int min = Math.min(Math.max(i10, 0), adapter.getItemCount() - 1);
        if (min == this.f8065h && this.f8073p.i()) {
            return;
        }
        int i11 = this.f8065h;
        if (min == i11 && z10) {
            return;
        }
        double d10 = i11;
        this.f8065h = min;
        this.f8081x.q();
        if (!this.f8073p.i()) {
            d10 = this.f8073p.e();
        }
        this.f8073p.m(min, z10);
        if (!z10) {
            this.f8071n.scrollToPosition(min);
            return;
        }
        double d11 = min;
        if (Math.abs(d11 - d10) > 3.0d) {
            this.f8071n.scrollToPosition(d11 > d10 ? min - 3 : min + 3);
            l lVar = this.f8071n;
            lVar.post(new m(min, lVar));
            return;
        }
        this.f8071n.smoothScrollToPosition(min);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        this.f8081x.i(accessibilityNodeInfo);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        int measuredWidth = this.f8071n.getMeasuredWidth();
        int measuredHeight = this.f8071n.getMeasuredHeight();
        this.f8062e.left = getPaddingLeft();
        this.f8062e.right = (i12 - i10) - getPaddingRight();
        this.f8062e.top = getPaddingTop();
        this.f8062e.bottom = (i13 - i11) - getPaddingBottom();
        Gravity.apply(8388659, measuredWidth, measuredHeight, this.f8062e, this.f8063f);
        l lVar = this.f8071n;
        Rect rect = this.f8063f;
        lVar.layout(rect.left, rect.top, rect.right, rect.bottom);
        if (this.f8066i) {
            r();
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        measureChild(this.f8071n, i10, i11);
        int measuredWidth = this.f8071n.getMeasuredWidth();
        int measuredHeight = this.f8071n.getMeasuredHeight();
        int measuredState = this.f8071n.getMeasuredState();
        int paddingLeft = measuredWidth + getPaddingLeft() + getPaddingRight();
        int paddingTop = measuredHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(ViewGroup.resolveSizeAndState(Math.max(paddingLeft, getSuggestedMinimumWidth()), i10, measuredState), ViewGroup.resolveSizeAndState(Math.max(paddingTop, getSuggestedMinimumHeight()), i11, measuredState << 16));
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.f8069l = savedState.f8085f;
        this.f8070m = savedState.f8086g;
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f8084e = this.f8071n.getId();
        int i10 = this.f8069l;
        if (i10 == -1) {
            i10 = this.f8065h;
        }
        savedState.f8085f = i10;
        Parcelable parcelable = this.f8070m;
        if (parcelable != null) {
            savedState.f8086g = parcelable;
        } else {
            Object adapter = this.f8071n.getAdapter();
            if (adapter instanceof StatefulAdapter) {
                savedState.f8086g = ((StatefulAdapter) adapter).a();
            }
        }
        return savedState;
    }

    @Override // android.view.ViewGroup
    public void onViewAdded(View view) {
        throw new IllegalStateException(getClass().getSimpleName() + " does not support direct child views");
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int i10, Bundle bundle) {
        if (this.f8081x.c(i10, bundle)) {
            return this.f8081x.l(i10, bundle);
        }
        return super.performAccessibilityAction(i10, bundle);
    }

    public void q(ViewPager2.i iVar) {
        this.f8064g.e(iVar);
    }

    void r() {
        PagerSnapHelper pagerSnapHelper = this.f8072o;
        if (pagerSnapHelper != null) {
            View f10 = pagerSnapHelper.f(this.f8068k);
            if (f10 == null) {
                return;
            }
            int j02 = this.f8068k.j0(f10);
            if (j02 != this.f8065h && getScrollState() == 0) {
                this.f8074q.c(j02);
            }
            this.f8066i = false;
            return;
        }
        throw new IllegalStateException("Design assumption violated.");
    }

    public void setAdapter(RecyclerView.h hVar) {
        RecyclerView.h adapter = this.f8071n.getAdapter();
        this.f8081x.f(adapter);
        p(adapter);
        this.f8071n.setAdapter(hVar);
        this.f8065h = 0;
        l();
        this.f8081x.e(hVar);
        i(hVar);
    }

    public void setCurrentItem(int i10) {
        m(i10, true);
    }

    public void setDuration(int i10) {
        this.f8083z = i10;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.f8082y = interpolator;
    }

    @Override // android.view.View
    public void setLayoutDirection(int i10) {
        super.setLayoutDirection(i10);
        this.f8081x.p();
    }

    public void setOffscreenPageLimit(int i10) {
        if (i10 < 1 && i10 != -1) {
            throw new IllegalArgumentException("Offscreen page limit must be OFFSCREEN_PAGE_LIMIT_DEFAULT or a number > 0");
        }
        this.f8080w = i10;
        this.f8071n.requestLayout();
    }

    public void setOrientation(int i10) {
        this.f8068k.C2(i10);
        this.f8081x.r();
    }

    public void setOverScrollEnable(boolean z10) {
        this.f8071n.setOverScrollEnable(z10);
    }

    public void setPageTransformer(ViewPager2.k kVar) {
        if (kVar != null) {
            if (!this.f8078u) {
                this.f8077t = this.f8071n.getItemAnimator();
                this.f8078u = true;
            }
            this.f8071n.setItemAnimator(null);
        } else if (this.f8078u) {
            this.f8071n.setItemAnimator(this.f8077t);
            this.f8077t = null;
            this.f8078u = false;
        }
        if (kVar == this.f8076s.d()) {
            return;
        }
        this.f8076s.e(kVar);
        k();
    }

    public void setUserInputEnabled(boolean z10) {
        this.f8079v = z10;
        this.f8081x.s();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        int f8084e;

        /* renamed from: f, reason: collision with root package name */
        int f8085f;

        /* renamed from: g, reason: collision with root package name */
        Parcelable f8086g;

        /* loaded from: classes.dex */
        class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return createFromParcel(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            j(parcel, classLoader);
        }

        private void j(Parcel parcel, ClassLoader classLoader) {
            this.f8084e = parcel.readInt();
            this.f8085f = parcel.readInt();
            this.f8086g = parcel.readParcelable(classLoader);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f8084e);
            parcel.writeInt(this.f8085f);
            parcel.writeParcelable(this.f8086g, i10);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    public COUIViewPager2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f8062e = new Rect();
        this.f8063f = new Rect();
        this.f8064g = new COUICompositeOnPageChangeCallback(3);
        this.f8066i = false;
        this.f8067j = new a();
        this.f8069l = -1;
        this.f8077t = null;
        this.f8078u = false;
        this.f8079v = true;
        this.f8080w = -1;
        this.f8082y = new LinearInterpolator();
        this.f8083z = 500;
        e(context, attributeSet);
    }

    public COUIViewPager2(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f8062e = new Rect();
        this.f8063f = new Rect();
        this.f8064g = new COUICompositeOnPageChangeCallback(3);
        this.f8066i = false;
        this.f8067j = new a();
        this.f8069l = -1;
        this.f8077t = null;
        this.f8078u = false;
        this.f8079v = true;
        this.f8080w = -1;
        this.f8082y = new LinearInterpolator();
        this.f8083z = 500;
        e(context, attributeSet);
    }
}
