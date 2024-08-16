package com.coui.appcompat.tablayout;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.coui.appcompat.tablayout.COUITabLayout;
import com.coui.appcompat.viewpager.COUIViewPager2;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import m1.COUIMoveEaseInterpolator;

/* compiled from: COUITabLayoutMediator.java */
/* renamed from: com.coui.appcompat.tablayout.d, reason: use source file name */
/* loaded from: classes.dex */
public final class COUITabLayoutMediator {

    /* renamed from: m, reason: collision with root package name */
    private static Method f7848m;

    /* renamed from: n, reason: collision with root package name */
    private static Method f7849n;

    /* renamed from: a, reason: collision with root package name */
    private final COUITabLayout f7850a;

    /* renamed from: b, reason: collision with root package name */
    private final COUIViewPager2 f7851b;

    /* renamed from: c, reason: collision with root package name */
    private final boolean f7852c;

    /* renamed from: d, reason: collision with root package name */
    private final a f7853d;

    /* renamed from: e, reason: collision with root package name */
    private RecyclerView.h f7854e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f7855f;

    /* renamed from: g, reason: collision with root package name */
    private c f7856g;

    /* renamed from: h, reason: collision with root package name */
    private COUITabLayout.c f7857h;

    /* renamed from: i, reason: collision with root package name */
    private RecyclerView.j f7858i;

    /* renamed from: j, reason: collision with root package name */
    private int f7859j;

    /* renamed from: k, reason: collision with root package name */
    private int f7860k;

    /* renamed from: l, reason: collision with root package name */
    private int f7861l;

    /* compiled from: COUITabLayoutMediator.java */
    /* renamed from: com.coui.appcompat.tablayout.d$a */
    /* loaded from: classes.dex */
    public interface a {
        void a(COUITab cOUITab, int i10);
    }

    /* compiled from: COUITabLayoutMediator.java */
    /* renamed from: com.coui.appcompat.tablayout.d$b */
    /* loaded from: classes.dex */
    private class b extends RecyclerView.j {
        b() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onChanged() {
            COUITabLayoutMediator.this.b();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeChanged(int i10, int i11) {
            COUITabLayoutMediator.this.b();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeInserted(int i10, int i11) {
            COUITabLayoutMediator.this.b();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeMoved(int i10, int i11, int i12) {
            COUITabLayoutMediator.this.b();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeRemoved(int i10, int i11) {
            COUITabLayoutMediator.this.b();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeChanged(int i10, int i11, Object obj) {
            COUITabLayoutMediator.this.b();
        }
    }

    /* compiled from: COUITabLayoutMediator.java */
    /* renamed from: com.coui.appcompat.tablayout.d$c */
    /* loaded from: classes.dex */
    private static class c extends ViewPager2.i {

        /* renamed from: a, reason: collision with root package name */
        private final WeakReference<COUITabLayout> f7863a;

        /* renamed from: b, reason: collision with root package name */
        private final WeakReference<COUIViewPager2> f7864b;

        /* renamed from: c, reason: collision with root package name */
        private int f7865c;

        /* renamed from: d, reason: collision with root package name */
        private int f7866d;

        c(COUITabLayout cOUITabLayout, COUIViewPager2 cOUIViewPager2) {
            this.f7863a = new WeakReference<>(cOUITabLayout);
            this.f7864b = new WeakReference<>(cOUIViewPager2);
            d();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.i
        public void a(int i10) {
            this.f7865c = this.f7866d;
            this.f7866d = i10;
        }

        @Override // androidx.viewpager2.widget.ViewPager2.i
        public void b(int i10, float f10, int i11) {
            COUIViewPager2 cOUIViewPager2 = this.f7864b.get();
            COUITabLayout cOUITabLayout = this.f7863a.get();
            if (cOUITabLayout == null || cOUIViewPager2 == null || cOUIViewPager2.f()) {
                return;
            }
            int i12 = this.f7866d;
            boolean z10 = true;
            boolean z11 = i12 != 2 || this.f7865c == 1;
            if (i12 == 2 && this.f7865c == 0) {
                z10 = false;
            }
            if (i12 == 0 && this.f7865c == 0 && f10 != 0.0f) {
                ((RecyclerView) cOUIViewPager2.getChildAt(0)).scrollBy(i11, 0);
                cOUITabLayout.a0(cOUITabLayout.S(i10));
            } else {
                COUITabLayoutMediator.d(cOUITabLayout, i10, f10, z11, z10);
            }
            if (f10 != 0.0f || i10 == cOUITabLayout.getSelectedTabPosition()) {
                return;
            }
            cOUITabLayout.a0(cOUITabLayout.S(i10));
        }

        @Override // androidx.viewpager2.widget.ViewPager2.i
        public void c(int i10) {
            COUITabLayout cOUITabLayout = this.f7863a.get();
            if (cOUITabLayout == null || cOUITabLayout.getSelectedTabPosition() == i10 || i10 >= cOUITabLayout.getTabCount()) {
                return;
            }
            int i11 = this.f7866d;
            COUITabLayoutMediator.c(cOUITabLayout, cOUITabLayout.S(i10), i11 == 0 || (i11 == 2 && this.f7865c == 0));
        }

        void d() {
            this.f7866d = 0;
            this.f7865c = 0;
        }
    }

    /* compiled from: COUITabLayoutMediator.java */
    /* renamed from: com.coui.appcompat.tablayout.d$d */
    /* loaded from: classes.dex */
    private static class d implements COUITabLayout.c {

        /* renamed from: a, reason: collision with root package name */
        private final COUIViewPager2 f7867a;

        /* renamed from: b, reason: collision with root package name */
        private int[] f7868b = new int[2];

        /* renamed from: c, reason: collision with root package name */
        private PathInterpolator f7869c = new COUIMoveEaseInterpolator();

        d(COUIViewPager2 cOUIViewPager2) {
            this.f7867a = cOUIViewPager2;
        }

        private void d(LinearLayoutManager linearLayoutManager, RecyclerView recyclerView, int i10) {
            View C;
            int[] iArr = this.f7868b;
            iArr[0] = 0;
            iArr[1] = 0;
            int b22 = linearLayoutManager.b2();
            if (b22 == -1 || (C = linearLayoutManager.C(b22)) == null) {
                return;
            }
            int a02 = linearLayoutManager.a0(C);
            int l02 = linearLayoutManager.l0(C);
            ViewGroup.LayoutParams layoutParams = C.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                a02 += marginLayoutParams.leftMargin;
                l02 += marginLayoutParams.rightMargin;
            }
            int width = C.getWidth() + a02 + l02;
            int left = (C.getLeft() - a02) - recyclerView.getPaddingLeft();
            if (linearLayoutManager.Z() == 1) {
                width = -width;
            }
            int i11 = ((i10 - b22) * width) + left;
            int[] iArr2 = this.f7868b;
            iArr2[0] = i11;
            iArr2[1] = e(Math.abs(i11), Math.abs(width));
        }

        private int e(int i10, int i11) {
            float f10 = i11 * 3;
            if (i10 <= i11) {
                return 350;
            }
            float f11 = i10;
            if (f11 > f10) {
                return 650;
            }
            return (int) (((f11 / f10) * 300.0f) + 350.0f);
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayout.c
        public void a(COUITab cOUITab) {
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayout.c
        public void b(COUITab cOUITab) {
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayout.c
        public void c(COUITab cOUITab) {
            RecyclerView.h adapter;
            if (cOUITab.f7840b.getSelectedByClick() && (adapter = this.f7867a.getAdapter()) != null && adapter.getItemCount() > 0) {
                int min = Math.min(Math.max(cOUITab.d(), 0), adapter.getItemCount() - 1);
                if (this.f7867a.getChildAt(0) instanceof RecyclerView) {
                    RecyclerView recyclerView = (RecyclerView) this.f7867a.getChildAt(0);
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (linearLayoutManager == null) {
                        return;
                    }
                    d(linearLayoutManager, recyclerView, min);
                    this.f7867a.c();
                    int[] iArr = this.f7868b;
                    recyclerView.smoothScrollBy(iArr[0], 0, this.f7869c, iArr[1]);
                }
            }
        }
    }

    static {
        try {
            Class cls = Boolean.TYPE;
            Method declaredMethod = COUITabLayout.class.getDeclaredMethod("f0", Integer.TYPE, Float.TYPE, cls, cls);
            f7848m = declaredMethod;
            declaredMethod.setAccessible(true);
            Method declaredMethod2 = COUITabLayout.class.getDeclaredMethod("b0", COUITab.class, cls);
            f7849n = declaredMethod2;
            declaredMethod2.setAccessible(true);
        } catch (NoSuchMethodException unused) {
            throw new IllegalStateException("Can't reflect into method TabLayout.setScrollPosition(int, float, boolean, boolean)");
        }
    }

    public COUITabLayoutMediator(COUITabLayout cOUITabLayout, COUIViewPager2 cOUIViewPager2, a aVar) {
        this(cOUITabLayout, cOUIViewPager2, true, aVar);
    }

    static void c(COUITabLayout cOUITabLayout, COUITab cOUITab, boolean z10) {
        try {
            Method method = f7849n;
            if (method != null) {
                method.invoke(cOUITabLayout, cOUITab, Boolean.valueOf(z10));
            } else {
                f("TabLayout.selectTab(TabLayout.Tab, boolean)");
            }
        } catch (Exception unused) {
            e("TabLayout.selectTab(TabLayout.Tab, boolean)");
        }
    }

    static void d(COUITabLayout cOUITabLayout, int i10, float f10, boolean z10, boolean z11) {
        try {
            Method method = f7848m;
            if (method != null) {
                method.invoke(cOUITabLayout, Integer.valueOf(i10), Float.valueOf(f10), Boolean.valueOf(z10), Boolean.valueOf(z11));
            } else {
                f("TabLayout.setScrollPosition(int, float, boolean, boolean)");
            }
        } catch (Exception unused) {
            e("TabLayout.setScrollPosition(int, float, boolean, boolean)");
        }
    }

    private static void e(String str) {
        throw new IllegalStateException("Couldn't invoke method " + str);
    }

    private static void f(String str) {
        throw new IllegalStateException("Method " + str + " not found");
    }

    public void a() {
        if (!this.f7855f) {
            RecyclerView.h adapter = this.f7851b.getAdapter();
            this.f7854e = adapter;
            if (adapter != null) {
                this.f7855f = true;
                c cVar = new c(this.f7850a, this.f7851b);
                this.f7856g = cVar;
                this.f7851b.j(cVar);
                d dVar = new d(this.f7851b);
                this.f7857h = dVar;
                this.f7850a.w(dVar);
                if (this.f7852c) {
                    b bVar = new b();
                    this.f7858i = bVar;
                    this.f7854e.registerAdapterDataObserver(bVar);
                }
                b();
                this.f7850a.e0(this.f7851b.getCurrentItem(), 0.0f, true);
                return;
            }
            throw new IllegalStateException("TabLayoutMediator attached before ViewPager2 has an adapter");
        }
        throw new IllegalStateException("TabLayoutMediator is already attached");
    }

    void b() {
        this.f7850a.W();
        RecyclerView.h hVar = this.f7854e;
        if (hVar != null) {
            int itemCount = hVar.getItemCount();
            for (int i10 = 0; i10 < itemCount; i10++) {
                COUITab U = this.f7850a.U();
                int i11 = this.f7861l;
                if (i11 != 1) {
                    if (i11 == 2) {
                        U.j(this.f7859j);
                    }
                } else if (this.f7860k == i10) {
                    U.j(this.f7859j);
                }
                this.f7853d.a(U, i10);
                this.f7850a.z(U, false);
            }
            if (itemCount > 0) {
                int currentItem = this.f7851b.getCurrentItem();
                COUITab S = this.f7850a.S(currentItem);
                if (currentItem == this.f7850a.getSelectedTabPosition() || S == null) {
                    return;
                }
                S.h();
            }
        }
    }

    public COUITabLayoutMediator(COUITabLayout cOUITabLayout, COUIViewPager2 cOUIViewPager2, boolean z10, a aVar) {
        this.f7850a = cOUITabLayout;
        cOUITabLayout.setUpdateindicatorposition(true);
        this.f7851b = cOUIViewPager2;
        this.f7852c = z10;
        this.f7853d = aVar;
        this.f7861l = 0;
    }
}
