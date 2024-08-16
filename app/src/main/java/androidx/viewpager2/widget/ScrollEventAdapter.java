package androidx.viewpager2.widget;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Locale;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ScrollEventAdapter.java */
/* renamed from: androidx.viewpager2.widget.f, reason: use source file name */
/* loaded from: classes.dex */
public final class ScrollEventAdapter extends RecyclerView.t {

    /* renamed from: a, reason: collision with root package name */
    private ViewPager2.i f4285a;

    /* renamed from: b, reason: collision with root package name */
    private final ViewPager2 f4286b;

    /* renamed from: c, reason: collision with root package name */
    private final RecyclerView f4287c;

    /* renamed from: d, reason: collision with root package name */
    private final LinearLayoutManager f4288d;

    /* renamed from: e, reason: collision with root package name */
    private int f4289e;

    /* renamed from: f, reason: collision with root package name */
    private int f4290f;

    /* renamed from: g, reason: collision with root package name */
    private a f4291g;

    /* renamed from: h, reason: collision with root package name */
    private int f4292h;

    /* renamed from: i, reason: collision with root package name */
    private int f4293i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f4294j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f4295k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f4296l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f4297m;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ScrollEventAdapter.java */
    /* renamed from: androidx.viewpager2.widget.f$a */
    /* loaded from: classes.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        int f4298a;

        /* renamed from: b, reason: collision with root package name */
        float f4299b;

        /* renamed from: c, reason: collision with root package name */
        int f4300c;

        a() {
        }

        void a() {
            this.f4298a = -1;
            this.f4299b = 0.0f;
            this.f4300c = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ScrollEventAdapter(ViewPager2 viewPager2) {
        this.f4286b = viewPager2;
        RecyclerView recyclerView = viewPager2.f4239n;
        this.f4287c = recyclerView;
        this.f4288d = (LinearLayoutManager) recyclerView.getLayoutManager();
        this.f4291g = new a();
        o();
    }

    private void a(int i10, float f10, int i11) {
        ViewPager2.i iVar = this.f4285a;
        if (iVar != null) {
            iVar.b(i10, f10, i11);
        }
    }

    private void b(int i10) {
        ViewPager2.i iVar = this.f4285a;
        if (iVar != null) {
            iVar.c(i10);
        }
    }

    private void c(int i10) {
        if ((this.f4289e == 3 && this.f4290f == 0) || this.f4290f == i10) {
            return;
        }
        this.f4290f = i10;
        ViewPager2.i iVar = this.f4285a;
        if (iVar != null) {
            iVar.a(i10);
        }
    }

    private int d() {
        return this.f4288d.b2();
    }

    private boolean j() {
        int i10 = this.f4289e;
        return i10 == 1 || i10 == 4;
    }

    private void o() {
        this.f4289e = 0;
        this.f4290f = 0;
        this.f4291g.a();
        this.f4292h = -1;
        this.f4293i = -1;
        this.f4294j = false;
        this.f4295k = false;
        this.f4297m = false;
        this.f4296l = false;
    }

    private void q(boolean z10) {
        this.f4297m = z10;
        this.f4289e = z10 ? 4 : 1;
        int i10 = this.f4293i;
        if (i10 != -1) {
            this.f4292h = i10;
            this.f4293i = -1;
        } else if (this.f4292h == -1) {
            this.f4292h = d();
        }
        c(1);
    }

    private void r() {
        int top;
        a aVar = this.f4291g;
        int b22 = this.f4288d.b2();
        aVar.f4298a = b22;
        if (b22 == -1) {
            aVar.a();
            return;
        }
        View C = this.f4288d.C(b22);
        if (C == null) {
            aVar.a();
            return;
        }
        int a02 = this.f4288d.a0(C);
        int l02 = this.f4288d.l0(C);
        int o02 = this.f4288d.o0(C);
        int H = this.f4288d.H(C);
        ViewGroup.LayoutParams layoutParams = C.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            a02 += marginLayoutParams.leftMargin;
            l02 += marginLayoutParams.rightMargin;
            o02 += marginLayoutParams.topMargin;
            H += marginLayoutParams.bottomMargin;
        }
        int height = C.getHeight() + o02 + H;
        int width = C.getWidth() + a02 + l02;
        if (this.f4288d.p2() == 0) {
            top = (C.getLeft() - a02) - this.f4287c.getPaddingLeft();
            if (this.f4286b.g()) {
                top = -top;
            }
            height = width;
        } else {
            top = (C.getTop() - o02) - this.f4287c.getPaddingTop();
        }
        int i10 = -top;
        aVar.f4300c = i10;
        if (i10 < 0) {
            if (new AnimateLayoutChangeDetector(this.f4288d).d()) {
                throw new IllegalStateException("Page(s) contain a ViewGroup with a LayoutTransition (or animateLayoutChanges=\"true\"), which interferes with the scrolling animation. Make sure to call getLayoutTransition().setAnimateParentHierarchy(false) on all ViewGroups with a LayoutTransition before an animation is started.");
            }
            throw new IllegalStateException(String.format(Locale.US, "Page can only be offset by a positive amount, not by %d", Integer.valueOf(aVar.f4300c)));
        }
        aVar.f4299b = height == 0 ? 0.0f : i10 / height;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double e() {
        r();
        a aVar = this.f4291g;
        return aVar.f4298a + aVar.f4299b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int f() {
        return this.f4290f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean g() {
        return this.f4290f == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean h() {
        return this.f4297m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean i() {
        return this.f4290f == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k() {
        this.f4289e = 4;
        q(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l() {
        this.f4296l = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m() {
        if (!g() || this.f4297m) {
            this.f4297m = false;
            r();
            a aVar = this.f4291g;
            if (aVar.f4300c == 0) {
                int i10 = aVar.f4298a;
                if (i10 != this.f4292h) {
                    b(i10);
                }
                c(0);
                o();
                return;
            }
            c(2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void n(int i10, boolean z10) {
        this.f4289e = z10 ? 2 : 3;
        this.f4297m = false;
        boolean z11 = this.f4293i != i10;
        this.f4293i = i10;
        c(2);
        if (z11) {
            b(i10);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.t
    public void onScrollStateChanged(RecyclerView recyclerView, int i10) {
        boolean z10 = true;
        if ((this.f4289e != 1 || this.f4290f != 1) && i10 == 1) {
            q(false);
            return;
        }
        if (j() && i10 == 2) {
            if (this.f4295k) {
                c(2);
                this.f4294j = true;
                return;
            }
            return;
        }
        if (j() && i10 == 0) {
            r();
            if (!this.f4295k) {
                int i11 = this.f4291g.f4298a;
                if (i11 != -1) {
                    a(i11, 0.0f, 0);
                }
            } else {
                a aVar = this.f4291g;
                if (aVar.f4300c == 0) {
                    int i12 = this.f4292h;
                    int i13 = aVar.f4298a;
                    if (i12 != i13) {
                        b(i13);
                    }
                } else {
                    z10 = false;
                }
            }
            if (z10) {
                c(0);
                o();
            }
        }
        if (this.f4289e == 2 && i10 == 0 && this.f4296l) {
            r();
            a aVar2 = this.f4291g;
            if (aVar2.f4300c == 0) {
                int i14 = this.f4293i;
                int i15 = aVar2.f4298a;
                if (i14 != i15) {
                    if (i15 == -1) {
                        i15 = 0;
                    }
                    b(i15);
                }
                c(0);
                o();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x001d, code lost:
    
        if ((r5 < 0) == r3.f4286b.g()) goto L14;
     */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0039  */
    @Override // androidx.recyclerview.widget.RecyclerView.t
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onScrolled(RecyclerView recyclerView, int i10, int i11) {
        boolean z10;
        int i12;
        this.f4295k = true;
        r();
        if (this.f4294j) {
            this.f4294j = false;
            if (i11 <= 0) {
                if (i11 == 0) {
                }
                z10 = false;
                if (z10) {
                    a aVar = this.f4291g;
                    if (aVar.f4300c != 0) {
                        i12 = aVar.f4298a + 1;
                        this.f4293i = i12;
                        if (this.f4292h != i12) {
                            b(i12);
                        }
                    }
                }
                i12 = this.f4291g.f4298a;
                this.f4293i = i12;
                if (this.f4292h != i12) {
                }
            }
            z10 = true;
            if (z10) {
            }
            i12 = this.f4291g.f4298a;
            this.f4293i = i12;
            if (this.f4292h != i12) {
            }
        } else if (this.f4289e == 0) {
            int i13 = this.f4291g.f4298a;
            if (i13 == -1) {
                i13 = 0;
            }
            b(i13);
        }
        a aVar2 = this.f4291g;
        int i14 = aVar2.f4298a;
        if (i14 == -1) {
            i14 = 0;
        }
        a(i14, aVar2.f4299b, aVar2.f4300c);
        a aVar3 = this.f4291g;
        int i15 = aVar3.f4298a;
        int i16 = this.f4293i;
        if ((i15 == i16 || i16 == -1) && aVar3.f4300c == 0 && this.f4290f != 1) {
            c(0);
            o();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p(ViewPager2.i iVar) {
        this.f4285a = iVar;
    }
}
