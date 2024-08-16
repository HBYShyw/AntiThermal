package androidx.viewpager2.widget;

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
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.R$styleable;
import androidx.viewpager2.adapter.StatefulAdapter;

/* loaded from: classes.dex */
public final class ViewPager2 extends ViewGroup {

    /* renamed from: y, reason: collision with root package name */
    static boolean f4229y = true;

    /* renamed from: e, reason: collision with root package name */
    private final Rect f4230e;

    /* renamed from: f, reason: collision with root package name */
    private final Rect f4231f;

    /* renamed from: g, reason: collision with root package name */
    private CompositeOnPageChangeCallback f4232g;

    /* renamed from: h, reason: collision with root package name */
    int f4233h;

    /* renamed from: i, reason: collision with root package name */
    boolean f4234i;

    /* renamed from: j, reason: collision with root package name */
    private RecyclerView.j f4235j;

    /* renamed from: k, reason: collision with root package name */
    private LinearLayoutManager f4236k;

    /* renamed from: l, reason: collision with root package name */
    private int f4237l;

    /* renamed from: m, reason: collision with root package name */
    private Parcelable f4238m;

    /* renamed from: n, reason: collision with root package name */
    RecyclerView f4239n;

    /* renamed from: o, reason: collision with root package name */
    private PagerSnapHelper f4240o;

    /* renamed from: p, reason: collision with root package name */
    ScrollEventAdapter f4241p;

    /* renamed from: q, reason: collision with root package name */
    private CompositeOnPageChangeCallback f4242q;

    /* renamed from: r, reason: collision with root package name */
    private FakeDrag f4243r;

    /* renamed from: s, reason: collision with root package name */
    private PageTransformerAdapter f4244s;

    /* renamed from: t, reason: collision with root package name */
    private RecyclerView.m f4245t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f4246u;

    /* renamed from: v, reason: collision with root package name */
    private boolean f4247v;

    /* renamed from: w, reason: collision with root package name */
    private int f4248w;

    /* renamed from: x, reason: collision with root package name */
    e f4249x;

    /* loaded from: classes.dex */
    class a extends g {
        a() {
            super(null);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.g, androidx.recyclerview.widget.RecyclerView.j
        public void onChanged() {
            ViewPager2 viewPager2 = ViewPager2.this;
            viewPager2.f4234i = true;
            viewPager2.f4241p.l();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b extends i {
        b() {
        }

        @Override // androidx.viewpager2.widget.ViewPager2.i
        public void a(int i10) {
            if (i10 == 0) {
                ViewPager2.this.r();
            }
        }

        @Override // androidx.viewpager2.widget.ViewPager2.i
        public void c(int i10) {
            ViewPager2 viewPager2 = ViewPager2.this;
            if (viewPager2.f4233h != i10) {
                viewPager2.f4233h = i10;
                viewPager2.f4249x.q();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c extends i {
        c() {
        }

        @Override // androidx.viewpager2.widget.ViewPager2.i
        public void c(int i10) {
            ViewPager2.this.clearFocus();
            if (ViewPager2.this.hasFocus()) {
                ViewPager2.this.f4239n.requestFocus(2);
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

        void h(CompositeOnPageChangeCallback compositeOnPageChangeCallback, RecyclerView recyclerView) {
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

        /* synthetic */ e(ViewPager2 viewPager2, a aVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f extends e {
        f() {
            super(ViewPager2.this, null);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public boolean b(int i10) {
            return (i10 == 8192 || i10 == 4096) && !ViewPager2.this.h();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public boolean d() {
            return true;
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void j(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (ViewPager2.this.h()) {
                return;
            }
            accessibilityNodeInfoCompat.O(AccessibilityNodeInfoCompat.a.f2331r);
            accessibilityNodeInfoCompat.O(AccessibilityNodeInfoCompat.a.f2330q);
            accessibilityNodeInfoCompat.s0(false);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public boolean k(int i10) {
            if (b(i10)) {
                return false;
            }
            throw new IllegalStateException();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
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

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.recyclerview.widget.LinearLayoutManager
        public void O1(RecyclerView.z zVar, int[] iArr) {
            int offscreenPageLimit = ViewPager2.this.getOffscreenPageLimit();
            if (offscreenPageLimit == -1) {
                super.O1(zVar, iArr);
                return;
            }
            int pageSize = ViewPager2.this.getPageSize() * offscreenPageLimit;
            iArr[0] = pageSize;
            iArr[1] = pageSize;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.p
        public void P0(RecyclerView.v vVar, RecyclerView.z zVar, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.P0(vVar, zVar, accessibilityNodeInfoCompat);
            ViewPager2.this.f4249x.j(accessibilityNodeInfoCompat);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.p
        public boolean j1(RecyclerView.v vVar, RecyclerView.z zVar, int i10, Bundle bundle) {
            if (ViewPager2.this.f4249x.b(i10)) {
                return ViewPager2.this.f4249x.k(i10);
            }
            return super.j1(vVar, zVar, i10, bundle);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.p
        public boolean u1(RecyclerView recyclerView, View view, Rect rect, boolean z10, boolean z11) {
            return false;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class i {
        public void a(int i10) {
        }

        public void b(int i10, float f10, int i11) {
        }

        public void c(int i10) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class j extends e {

        /* renamed from: b, reason: collision with root package name */
        private final AccessibilityViewCommand f4259b;

        /* renamed from: c, reason: collision with root package name */
        private final AccessibilityViewCommand f4260c;

        /* renamed from: d, reason: collision with root package name */
        private RecyclerView.j f4261d;

        /* loaded from: classes.dex */
        class a implements AccessibilityViewCommand {
            a() {
            }

            @Override // androidx.core.view.accessibility.AccessibilityViewCommand
            public boolean perform(View view, AccessibilityViewCommand.a aVar) {
                j.this.v(((ViewPager2) view).getCurrentItem() + 1);
                return true;
            }
        }

        /* loaded from: classes.dex */
        class b implements AccessibilityViewCommand {
            b() {
            }

            @Override // androidx.core.view.accessibility.AccessibilityViewCommand
            public boolean perform(View view, AccessibilityViewCommand.a aVar) {
                j.this.v(((ViewPager2) view).getCurrentItem() - 1);
                return true;
            }
        }

        /* loaded from: classes.dex */
        class c extends g {
            c() {
                super(null);
            }

            @Override // androidx.viewpager2.widget.ViewPager2.g, androidx.recyclerview.widget.RecyclerView.j
            public void onChanged() {
                j.this.w();
            }
        }

        j() {
            super(ViewPager2.this, null);
            this.f4259b = new a();
            this.f4260c = new b();
        }

        private void t(AccessibilityNodeInfo accessibilityNodeInfo) {
            int i10;
            int i11;
            if (ViewPager2.this.getAdapter() == null) {
                i10 = 0;
                i11 = 0;
            } else if (ViewPager2.this.getOrientation() == 1) {
                i10 = ViewPager2.this.getAdapter().getItemCount();
                i11 = 0;
            } else {
                i11 = ViewPager2.this.getAdapter().getItemCount();
                i10 = 0;
            }
            AccessibilityNodeInfoCompat.C0(accessibilityNodeInfo).X(AccessibilityNodeInfoCompat.b.b(i10, i11, false, 0));
        }

        private void u(AccessibilityNodeInfo accessibilityNodeInfo) {
            int itemCount;
            RecyclerView.h adapter = ViewPager2.this.getAdapter();
            if (adapter == null || (itemCount = adapter.getItemCount()) == 0 || !ViewPager2.this.h()) {
                return;
            }
            if (ViewPager2.this.f4233h > 0) {
                accessibilityNodeInfo.addAction(8192);
            }
            if (ViewPager2.this.f4233h < itemCount - 1) {
                accessibilityNodeInfo.addAction(4096);
            }
            accessibilityNodeInfo.setScrollable(true);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public boolean a() {
            return true;
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public boolean c(int i10, Bundle bundle) {
            return i10 == 8192 || i10 == 4096;
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void e(RecyclerView.h<?> hVar) {
            w();
            if (hVar != null) {
                hVar.registerAdapterDataObserver(this.f4261d);
            }
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void f(RecyclerView.h<?> hVar) {
            if (hVar != null) {
                hVar.unregisterAdapterDataObserver(this.f4261d);
            }
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public String g() {
            if (a()) {
                return "androidx.viewpager.widget.ViewPager";
            }
            throw new IllegalStateException();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void h(CompositeOnPageChangeCallback compositeOnPageChangeCallback, RecyclerView recyclerView) {
            ViewCompat.w0(recyclerView, 2);
            this.f4261d = new c();
            if (ViewCompat.v(ViewPager2.this) == 0) {
                ViewCompat.w0(ViewPager2.this, 1);
            }
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void i(AccessibilityNodeInfo accessibilityNodeInfo) {
            t(accessibilityNodeInfo);
            u(accessibilityNodeInfo);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public boolean l(int i10, Bundle bundle) {
            int currentItem;
            if (c(i10, bundle)) {
                if (i10 == 8192) {
                    currentItem = ViewPager2.this.getCurrentItem() - 1;
                } else {
                    currentItem = ViewPager2.this.getCurrentItem() + 1;
                }
                v(currentItem);
                return true;
            }
            throw new IllegalStateException();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void m() {
            w();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void o(AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.setSource(ViewPager2.this);
            accessibilityEvent.setClassName(g());
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void p() {
            w();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void q() {
            w();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void r() {
            w();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void s() {
            w();
        }

        void v(int i10) {
            if (ViewPager2.this.h()) {
                ViewPager2.this.n(i10, true);
            }
        }

        void w() {
            int itemCount;
            ViewPager2 viewPager2 = ViewPager2.this;
            int i10 = R.id.accessibilityActionPageLeft;
            ViewCompat.e0(viewPager2, R.id.accessibilityActionPageLeft);
            ViewCompat.e0(viewPager2, R.id.accessibilityActionPageRight);
            ViewCompat.e0(viewPager2, R.id.accessibilityActionPageUp);
            ViewCompat.e0(viewPager2, R.id.accessibilityActionPageDown);
            if (ViewPager2.this.getAdapter() == null || (itemCount = ViewPager2.this.getAdapter().getItemCount()) == 0 || !ViewPager2.this.h()) {
                return;
            }
            if (ViewPager2.this.getOrientation() == 0) {
                boolean g6 = ViewPager2.this.g();
                int i11 = g6 ? 16908360 : 16908361;
                if (g6) {
                    i10 = 16908361;
                }
                if (ViewPager2.this.f4233h < itemCount - 1) {
                    ViewCompat.g0(viewPager2, new AccessibilityNodeInfoCompat.a(i11, null), null, this.f4259b);
                }
                if (ViewPager2.this.f4233h > 0) {
                    ViewCompat.g0(viewPager2, new AccessibilityNodeInfoCompat.a(i10, null), null, this.f4260c);
                    return;
                }
                return;
            }
            if (ViewPager2.this.f4233h < itemCount - 1) {
                ViewCompat.g0(viewPager2, new AccessibilityNodeInfoCompat.a(R.id.accessibilityActionPageDown, null), null, this.f4259b);
            }
            if (ViewPager2.this.f4233h > 0) {
                ViewCompat.g0(viewPager2, new AccessibilityNodeInfoCompat.a(R.id.accessibilityActionPageUp, null), null, this.f4260c);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface k {
        void a(View view, float f10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class l extends PagerSnapHelper {
        l() {
        }

        @Override // androidx.recyclerview.widget.PagerSnapHelper, androidx.recyclerview.widget.SnapHelper
        public View f(RecyclerView.p pVar) {
            if (ViewPager2.this.f()) {
                return null;
            }
            return super.f(pVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class m extends RecyclerView {
        m(Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public CharSequence getAccessibilityClassName() {
            if (ViewPager2.this.f4249x.d()) {
                return ViewPager2.this.f4249x.n();
            }
            return super.getAccessibilityClassName();
        }

        @Override // android.view.View
        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setFromIndex(ViewPager2.this.f4233h);
            accessibilityEvent.setToIndex(ViewPager2.this.f4233h);
            ViewPager2.this.f4249x.o(accessibilityEvent);
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return ViewPager2.this.h() && super.onInterceptTouchEvent(motionEvent);
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return ViewPager2.this.h() && super.onTouchEvent(motionEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class n implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private final int f4268e;

        /* renamed from: f, reason: collision with root package name */
        private final RecyclerView f4269f;

        n(int i10, RecyclerView recyclerView) {
            this.f4268e = i10;
            this.f4269f = recyclerView;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f4269f.smoothScrollToPosition(this.f4268e);
        }
    }

    public ViewPager2(Context context) {
        super(context);
        this.f4230e = new Rect();
        this.f4231f = new Rect();
        this.f4232g = new CompositeOnPageChangeCallback(3);
        this.f4234i = false;
        this.f4235j = new a();
        this.f4237l = -1;
        this.f4245t = null;
        this.f4246u = false;
        this.f4247v = true;
        this.f4248w = -1;
        e(context, null);
    }

    private RecyclerView.q c() {
        return new d();
    }

    private void e(Context context, AttributeSet attributeSet) {
        this.f4249x = f4229y ? new j() : new f();
        m mVar = new m(context);
        this.f4239n = mVar;
        mVar.setId(ViewCompat.i());
        this.f4239n.setDescendantFocusability(131072);
        h hVar = new h(context);
        this.f4236k = hVar;
        this.f4239n.setLayoutManager(hVar);
        this.f4239n.setScrollingTouchSlop(1);
        o(context, attributeSet);
        this.f4239n.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.f4239n.addOnChildAttachStateChangeListener(c());
        ScrollEventAdapter scrollEventAdapter = new ScrollEventAdapter(this);
        this.f4241p = scrollEventAdapter;
        this.f4243r = new FakeDrag(this, scrollEventAdapter, this.f4239n);
        l lVar = new l();
        this.f4240o = lVar;
        lVar.b(this.f4239n);
        this.f4239n.addOnScrollListener(this.f4241p);
        CompositeOnPageChangeCallback compositeOnPageChangeCallback = new CompositeOnPageChangeCallback(3);
        this.f4242q = compositeOnPageChangeCallback;
        this.f4241p.p(compositeOnPageChangeCallback);
        b bVar = new b();
        c cVar = new c();
        this.f4242q.d(bVar);
        this.f4242q.d(cVar);
        this.f4249x.h(this.f4242q, this.f4239n);
        this.f4242q.d(this.f4232g);
        PageTransformerAdapter pageTransformerAdapter = new PageTransformerAdapter(this.f4236k);
        this.f4244s = pageTransformerAdapter;
        this.f4242q.d(pageTransformerAdapter);
        RecyclerView recyclerView = this.f4239n;
        attachViewToParent(recyclerView, 0, recyclerView.getLayoutParams());
    }

    private void i(RecyclerView.h<?> hVar) {
        if (hVar != null) {
            hVar.registerAdapterDataObserver(this.f4235j);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void l() {
        RecyclerView.h adapter;
        if (this.f4237l == -1 || (adapter = getAdapter()) == 0) {
            return;
        }
        Parcelable parcelable = this.f4238m;
        if (parcelable != null) {
            if (adapter instanceof StatefulAdapter) {
                ((StatefulAdapter) adapter).b(parcelable);
            }
            this.f4238m = null;
        }
        int max = Math.max(0, Math.min(this.f4237l, adapter.getItemCount() - 1));
        this.f4233h = max;
        this.f4237l = -1;
        this.f4239n.scrollToPosition(max);
        this.f4249x.m();
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

    private void q(RecyclerView.h<?> hVar) {
        if (hVar != null) {
            hVar.unregisterAdapterDataObserver(this.f4235j);
        }
    }

    public boolean a() {
        return this.f4243r.b();
    }

    public boolean b() {
        return this.f4243r.d();
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int i10) {
        return this.f4239n.canScrollHorizontally(i10);
    }

    @Override // android.view.View
    public boolean canScrollVertically(int i10) {
        return this.f4239n.canScrollVertically(i10);
    }

    public boolean d(@SuppressLint({"SupportAnnotationUsage"}) float f10) {
        return this.f4243r.e(f10);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        Parcelable parcelable = sparseArray.get(getId());
        if (parcelable instanceof SavedState) {
            int i10 = ((SavedState) parcelable).f4250e;
            sparseArray.put(this.f4239n.getId(), sparseArray.get(i10));
            sparseArray.remove(i10);
        }
        super.dispatchRestoreInstanceState(sparseArray);
        l();
    }

    public boolean f() {
        return this.f4243r.f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean g() {
        return this.f4236k.Z() == 1;
    }

    @Override // android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        if (this.f4249x.a()) {
            return this.f4249x.g();
        }
        return super.getAccessibilityClassName();
    }

    public RecyclerView.h getAdapter() {
        return this.f4239n.getAdapter();
    }

    public int getCurrentItem() {
        return this.f4233h;
    }

    public int getItemDecorationCount() {
        return this.f4239n.getItemDecorationCount();
    }

    public int getOffscreenPageLimit() {
        return this.f4248w;
    }

    public int getOrientation() {
        return this.f4236k.p2();
    }

    int getPageSize() {
        int height;
        int paddingBottom;
        RecyclerView recyclerView = this.f4239n;
        if (getOrientation() == 0) {
            height = recyclerView.getWidth() - recyclerView.getPaddingLeft();
            paddingBottom = recyclerView.getPaddingRight();
        } else {
            height = recyclerView.getHeight() - recyclerView.getPaddingTop();
            paddingBottom = recyclerView.getPaddingBottom();
        }
        return height - paddingBottom;
    }

    public int getScrollState() {
        return this.f4241p.f();
    }

    public boolean h() {
        return this.f4247v;
    }

    public void j(i iVar) {
        this.f4232g.d(iVar);
    }

    public void k() {
        if (this.f4244s.d() == null) {
            return;
        }
        double e10 = this.f4241p.e();
        int i10 = (int) e10;
        float f10 = (float) (e10 - i10);
        this.f4244s.b(i10, f10, Math.round(getPageSize() * f10));
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
            if (this.f4237l != -1) {
                this.f4237l = Math.max(i10, 0);
                return;
            }
            return;
        }
        if (adapter.getItemCount() <= 0) {
            return;
        }
        int min = Math.min(Math.max(i10, 0), adapter.getItemCount() - 1);
        if (min == this.f4233h && this.f4241p.i()) {
            return;
        }
        int i11 = this.f4233h;
        if (min == i11 && z10) {
            return;
        }
        double d10 = i11;
        this.f4233h = min;
        this.f4249x.q();
        if (!this.f4241p.i()) {
            d10 = this.f4241p.e();
        }
        this.f4241p.n(min, z10);
        if (!z10) {
            this.f4239n.scrollToPosition(min);
            return;
        }
        double d11 = min;
        if (Math.abs(d11 - d10) > 3.0d) {
            this.f4239n.scrollToPosition(d11 > d10 ? min - 3 : min + 3);
            RecyclerView recyclerView = this.f4239n;
            recyclerView.post(new n(min, recyclerView));
            return;
        }
        this.f4239n.smoothScrollToPosition(min);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        this.f4249x.i(accessibilityNodeInfo);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        int measuredWidth = this.f4239n.getMeasuredWidth();
        int measuredHeight = this.f4239n.getMeasuredHeight();
        this.f4230e.left = getPaddingLeft();
        this.f4230e.right = (i12 - i10) - getPaddingRight();
        this.f4230e.top = getPaddingTop();
        this.f4230e.bottom = (i13 - i11) - getPaddingBottom();
        Gravity.apply(8388659, measuredWidth, measuredHeight, this.f4230e, this.f4231f);
        RecyclerView recyclerView = this.f4239n;
        Rect rect = this.f4231f;
        recyclerView.layout(rect.left, rect.top, rect.right, rect.bottom);
        if (this.f4234i) {
            r();
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        measureChild(this.f4239n, i10, i11);
        int measuredWidth = this.f4239n.getMeasuredWidth();
        int measuredHeight = this.f4239n.getMeasuredHeight();
        int measuredState = this.f4239n.getMeasuredState();
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
        this.f4237l = savedState.f4251f;
        this.f4238m = savedState.f4252g;
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f4250e = this.f4239n.getId();
        int i10 = this.f4237l;
        if (i10 == -1) {
            i10 = this.f4233h;
        }
        savedState.f4251f = i10;
        Parcelable parcelable = this.f4238m;
        if (parcelable != null) {
            savedState.f4252g = parcelable;
        } else {
            Object adapter = this.f4239n.getAdapter();
            if (adapter instanceof StatefulAdapter) {
                savedState.f4252g = ((StatefulAdapter) adapter).a();
            }
        }
        return savedState;
    }

    @Override // android.view.ViewGroup
    public void onViewAdded(View view) {
        throw new IllegalStateException(ViewPager2.class.getSimpleName() + " does not support direct child views");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p() {
        View f10 = this.f4240o.f(this.f4236k);
        if (f10 == null) {
            return;
        }
        int[] c10 = this.f4240o.c(this.f4236k, f10);
        if (c10[0] == 0 && c10[1] == 0) {
            return;
        }
        this.f4239n.smoothScrollBy(c10[0], c10[1]);
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int i10, Bundle bundle) {
        if (this.f4249x.c(i10, bundle)) {
            return this.f4249x.l(i10, bundle);
        }
        return super.performAccessibilityAction(i10, bundle);
    }

    void r() {
        PagerSnapHelper pagerSnapHelper = this.f4240o;
        if (pagerSnapHelper != null) {
            View f10 = pagerSnapHelper.f(this.f4236k);
            if (f10 == null) {
                return;
            }
            int j02 = this.f4236k.j0(f10);
            if (j02 != this.f4233h && getScrollState() == 0) {
                this.f4242q.c(j02);
            }
            this.f4234i = false;
            return;
        }
        throw new IllegalStateException("Design assumption violated.");
    }

    public void setAdapter(RecyclerView.h hVar) {
        RecyclerView.h adapter = this.f4239n.getAdapter();
        this.f4249x.f(adapter);
        q(adapter);
        this.f4239n.setAdapter(hVar);
        this.f4233h = 0;
        l();
        this.f4249x.e(hVar);
        i(hVar);
    }

    public void setCurrentItem(int i10) {
        m(i10, true);
    }

    @Override // android.view.View
    public void setLayoutDirection(int i10) {
        super.setLayoutDirection(i10);
        this.f4249x.p();
    }

    public void setOffscreenPageLimit(int i10) {
        if (i10 < 1 && i10 != -1) {
            throw new IllegalArgumentException("Offscreen page limit must be OFFSCREEN_PAGE_LIMIT_DEFAULT or a number > 0");
        }
        this.f4248w = i10;
        this.f4239n.requestLayout();
    }

    public void setOrientation(int i10) {
        this.f4236k.C2(i10);
        this.f4249x.r();
    }

    public void setPageTransformer(k kVar) {
        if (kVar != null) {
            if (!this.f4246u) {
                this.f4245t = this.f4239n.getItemAnimator();
                this.f4246u = true;
            }
            this.f4239n.setItemAnimator(null);
        } else if (this.f4246u) {
            this.f4239n.setItemAnimator(this.f4245t);
            this.f4245t = null;
            this.f4246u = false;
        }
        if (kVar == this.f4244s.d()) {
            return;
        }
        this.f4244s.e(kVar);
        k();
    }

    public void setUserInputEnabled(boolean z10) {
        this.f4247v = z10;
        this.f4249x.s();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        int f4250e;

        /* renamed from: f, reason: collision with root package name */
        int f4251f;

        /* renamed from: g, reason: collision with root package name */
        Parcelable f4252g;

        /* loaded from: classes.dex */
        static class a implements Parcelable.ClassLoaderCreator<SavedState> {
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
            this.f4250e = parcel.readInt();
            this.f4251f = parcel.readInt();
            this.f4252g = parcel.readParcelable(classLoader);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f4250e);
            parcel.writeInt(this.f4251f);
            parcel.writeParcelable(this.f4252g, i10);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    public ViewPager2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f4230e = new Rect();
        this.f4231f = new Rect();
        this.f4232g = new CompositeOnPageChangeCallback(3);
        this.f4234i = false;
        this.f4235j = new a();
        this.f4237l = -1;
        this.f4245t = null;
        this.f4246u = false;
        this.f4247v = true;
        this.f4248w = -1;
        e(context, attributeSet);
    }

    public ViewPager2(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f4230e = new Rect();
        this.f4231f = new Rect();
        this.f4232g = new CompositeOnPageChangeCallback(3);
        this.f4234i = false;
        this.f4235j = new a();
        this.f4237l = -1;
        this.f4245t = null;
        this.f4246u = false;
        this.f4247v = true;
        this.f4248w = -1;
        e(context, attributeSet);
    }
}
