package com.coui.appcompat.viewpager;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.coui.appcompat.viewpager.COUIViewPager2;
import java.util.Locale;

/* compiled from: COUIScrollEventAdapter.java */
/* renamed from: com.coui.appcompat.viewpager.e, reason: use source file name */
/* loaded from: classes.dex */
public class COUIScrollEventAdapter extends RecyclerView.t {

    /* renamed from: a, reason: collision with root package name */
    private ViewPager2.i f8148a;

    /* renamed from: b, reason: collision with root package name */
    private final COUIViewPager2 f8149b;

    /* renamed from: c, reason: collision with root package name */
    private final RecyclerView f8150c;

    /* renamed from: d, reason: collision with root package name */
    private final LinearLayoutManager f8151d;

    /* renamed from: e, reason: collision with root package name */
    private int f8152e;

    /* renamed from: f, reason: collision with root package name */
    private int f8153f;

    /* renamed from: g, reason: collision with root package name */
    private a f8154g;

    /* renamed from: h, reason: collision with root package name */
    private int f8155h;

    /* renamed from: i, reason: collision with root package name */
    private int f8156i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f8157j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f8158k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f8159l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f8160m;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: COUIScrollEventAdapter.java */
    /* renamed from: com.coui.appcompat.viewpager.e$a */
    /* loaded from: classes.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        int f8161a;

        /* renamed from: b, reason: collision with root package name */
        float f8162b;

        /* renamed from: c, reason: collision with root package name */
        int f8163c;

        a() {
        }

        void a() {
            this.f8161a = -1;
            this.f8162b = 0.0f;
            this.f8163c = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public COUIScrollEventAdapter(COUIViewPager2 cOUIViewPager2) {
        this.f8149b = cOUIViewPager2;
        COUIViewPager2.l lVar = cOUIViewPager2.f8071n;
        this.f8150c = lVar;
        this.f8151d = (LinearLayoutManager) lVar.getLayoutManager();
        this.f8154g = new a();
        n();
    }

    private void a(int i10, float f10, int i11) {
        ViewPager2.i iVar = this.f8148a;
        if (iVar != null) {
            iVar.b(i10, f10, i11);
        }
    }

    private void b(int i10) {
        ViewPager2.i iVar = this.f8148a;
        if (iVar != null) {
            iVar.c(i10);
        }
    }

    private void c(int i10) {
        if ((this.f8152e == 3 && this.f8153f == 0) || this.f8153f == i10) {
            return;
        }
        this.f8153f = i10;
        ViewPager2.i iVar = this.f8148a;
        if (iVar != null) {
            iVar.a(i10);
        }
    }

    private int d() {
        return this.f8151d.b2();
    }

    private boolean j() {
        int i10 = this.f8152e;
        return i10 == 1 || i10 == 4;
    }

    private void n() {
        this.f8152e = 0;
        this.f8153f = 0;
        this.f8154g.a();
        this.f8155h = -1;
        this.f8156i = -1;
        this.f8157j = false;
        this.f8158k = false;
        this.f8160m = false;
        this.f8159l = false;
    }

    private void p(boolean z10) {
        this.f8160m = z10;
        this.f8152e = z10 ? 4 : 1;
        int i10 = this.f8156i;
        if (i10 != -1) {
            this.f8155h = i10;
            this.f8156i = -1;
        } else if (this.f8155h == -1) {
            this.f8155h = d();
        }
        c(1);
    }

    private void q() {
        int top;
        a aVar = this.f8154g;
        int b22 = this.f8151d.b2();
        aVar.f8161a = b22;
        if (b22 == -1) {
            aVar.a();
            return;
        }
        View C = this.f8151d.C(b22);
        if (C == null) {
            aVar.a();
            return;
        }
        int a02 = this.f8151d.a0(C);
        int l02 = this.f8151d.l0(C);
        int o02 = this.f8151d.o0(C);
        int H = this.f8151d.H(C);
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
        if (this.f8151d.p2() == 0) {
            top = (C.getLeft() - a02) - this.f8150c.getPaddingLeft();
            if (this.f8149b.g()) {
                top = -top;
            }
            height = width;
        } else {
            top = (C.getTop() - o02) - this.f8150c.getPaddingTop();
        }
        int i10 = -top;
        aVar.f8163c = i10;
        if (i10 < 0) {
            if (new COUIAnimateLayoutChangeDetector(this.f8151d).d()) {
                throw new IllegalStateException("Page(s) contain a ViewGroup with a LayoutTransition (or animateLayoutChanges=\"true\"), which interferes with the scrolling animation. Make sure to call getLayoutTransition().setAnimateParentHierarchy(false) on all ViewGroups with a LayoutTransition before an animation is started.");
            }
            throw new IllegalStateException(String.format(Locale.US, "Page can only be offset by a positive amount, not by %d", Integer.valueOf(aVar.f8163c)));
        }
        aVar.f8162b = height == 0 ? 0.0f : i10 / height;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double e() {
        q();
        a aVar = this.f8154g;
        return aVar.f8161a + aVar.f8162b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int f() {
        return this.f8153f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean g() {
        return this.f8153f == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean h() {
        return this.f8160m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean i() {
        return this.f8153f == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k() {
        this.f8152e = 4;
        p(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l() {
        this.f8159l = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(int i10, boolean z10) {
        this.f8152e = z10 ? 2 : 3;
        this.f8160m = false;
        boolean z11 = this.f8156i != i10;
        this.f8156i = i10;
        c(2);
        if (z11) {
            b(i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(ViewPager2.i iVar) {
        this.f8148a = iVar;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.t
    public void onScrollStateChanged(RecyclerView recyclerView, int i10) {
        boolean z10 = true;
        if ((this.f8152e != 1 || this.f8153f != 1) && i10 == 1) {
            p(false);
            return;
        }
        if (j() && i10 == 2) {
            if (this.f8158k) {
                c(2);
                this.f8157j = true;
                return;
            }
            return;
        }
        if (j() && i10 == 0) {
            q();
            if (!this.f8158k) {
                int i11 = this.f8154g.f8161a;
                if (i11 != -1) {
                    a(i11, 0.0f, 0);
                }
            } else {
                a aVar = this.f8154g;
                if (aVar.f8163c == 0) {
                    int i12 = this.f8155h;
                    int i13 = aVar.f8161a;
                    if (i12 != i13) {
                        b(i13);
                    }
                } else {
                    z10 = false;
                }
            }
            if (z10) {
                c(0);
                n();
            }
        }
        if (this.f8152e == 2 && i10 == 0 && this.f8159l) {
            q();
            a aVar2 = this.f8154g;
            if (aVar2.f8163c == 0) {
                int i14 = this.f8156i;
                int i15 = aVar2.f8161a;
                if (i14 != i15) {
                    if (i15 == -1) {
                        i15 = 0;
                    }
                    b(i15);
                }
                c(0);
                n();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x001d, code lost:
    
        if ((r5 < 0) == r3.f8149b.g()) goto L14;
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
        this.f8158k = true;
        q();
        if (this.f8157j) {
            this.f8157j = false;
            if (i11 <= 0) {
                if (i11 == 0) {
                }
                z10 = false;
                if (z10) {
                    a aVar = this.f8154g;
                    if (aVar.f8163c != 0) {
                        i12 = aVar.f8161a + 1;
                        this.f8156i = i12;
                        if (this.f8155h != i12) {
                            b(i12);
                        }
                    }
                }
                i12 = this.f8154g.f8161a;
                this.f8156i = i12;
                if (this.f8155h != i12) {
                }
            }
            z10 = true;
            if (z10) {
            }
            i12 = this.f8154g.f8161a;
            this.f8156i = i12;
            if (this.f8155h != i12) {
            }
        } else if (this.f8152e == 0) {
            int i13 = this.f8154g.f8161a;
            if (i13 == -1) {
                i13 = 0;
            }
            b(i13);
        }
        a aVar2 = this.f8154g;
        int i14 = aVar2.f8161a;
        if (i14 == -1) {
            i14 = 0;
        }
        a(i14, aVar2.f8162b, aVar2.f8163c);
        a aVar3 = this.f8154g;
        int i15 = aVar3.f8161a;
        int i16 = this.f8156i;
        if ((i15 == i16 || i16 == -1) && aVar3.f8163c == 0 && this.f8153f != 1) {
            c(0);
            n();
        }
    }
}
