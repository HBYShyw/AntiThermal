package androidx.recyclerview.widget;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ChildHelper.java */
/* renamed from: androidx.recyclerview.widget.d, reason: use source file name */
/* loaded from: classes.dex */
public class ChildHelper {

    /* renamed from: a, reason: collision with root package name */
    final b f3672a;

    /* renamed from: b, reason: collision with root package name */
    final a f3673b = new a();

    /* renamed from: c, reason: collision with root package name */
    final List<View> f3674c = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ChildHelper.java */
    /* renamed from: androidx.recyclerview.widget.d$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        long f3675a = 0;

        /* renamed from: b, reason: collision with root package name */
        a f3676b;

        a() {
        }

        private void c() {
            if (this.f3676b == null) {
                this.f3676b = new a();
            }
        }

        void a(int i10) {
            if (i10 >= 64) {
                a aVar = this.f3676b;
                if (aVar != null) {
                    aVar.a(i10 - 64);
                    return;
                }
                return;
            }
            this.f3675a &= ~(1 << i10);
        }

        int b(int i10) {
            a aVar = this.f3676b;
            if (aVar == null) {
                if (i10 >= 64) {
                    return Long.bitCount(this.f3675a);
                }
                return Long.bitCount(((1 << i10) - 1) & this.f3675a);
            }
            if (i10 < 64) {
                return Long.bitCount(((1 << i10) - 1) & this.f3675a);
            }
            return aVar.b(i10 - 64) + Long.bitCount(this.f3675a);
        }

        boolean d(int i10) {
            if (i10 < 64) {
                return ((1 << i10) & this.f3675a) != 0;
            }
            c();
            return this.f3676b.d(i10 - 64);
        }

        void e(int i10, boolean z10) {
            if (i10 >= 64) {
                c();
                this.f3676b.e(i10 - 64, z10);
                return;
            }
            long j10 = this.f3675a;
            boolean z11 = (Long.MIN_VALUE & j10) != 0;
            long j11 = (1 << i10) - 1;
            this.f3675a = ((j10 & (~j11)) << 1) | (j10 & j11);
            if (z10) {
                h(i10);
            } else {
                a(i10);
            }
            if (z11 || this.f3676b != null) {
                c();
                this.f3676b.e(0, z11);
            }
        }

        boolean f(int i10) {
            if (i10 >= 64) {
                c();
                return this.f3676b.f(i10 - 64);
            }
            long j10 = 1 << i10;
            long j11 = this.f3675a;
            boolean z10 = (j11 & j10) != 0;
            long j12 = j11 & (~j10);
            this.f3675a = j12;
            long j13 = j10 - 1;
            this.f3675a = (j12 & j13) | Long.rotateRight((~j13) & j12, 1);
            a aVar = this.f3676b;
            if (aVar != null) {
                if (aVar.d(0)) {
                    h(63);
                }
                this.f3676b.f(0);
            }
            return z10;
        }

        void g() {
            this.f3675a = 0L;
            a aVar = this.f3676b;
            if (aVar != null) {
                aVar.g();
            }
        }

        void h(int i10) {
            if (i10 >= 64) {
                c();
                this.f3676b.h(i10 - 64);
            } else {
                this.f3675a |= 1 << i10;
            }
        }

        public String toString() {
            if (this.f3676b == null) {
                return Long.toBinaryString(this.f3675a);
            }
            return this.f3676b.toString() + "xx" + Long.toBinaryString(this.f3675a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ChildHelper.java */
    /* renamed from: androidx.recyclerview.widget.d$b */
    /* loaded from: classes.dex */
    public interface b {
        View a(int i10);

        void b(View view);

        int c();

        void d();

        int e(View view);

        RecyclerView.c0 f(View view);

        void g(int i10);

        void h(View view);

        void i(View view, int i10);

        void j(int i10);

        void k(View view, int i10, ViewGroup.LayoutParams layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChildHelper(b bVar) {
        this.f3672a = bVar;
    }

    private int h(int i10) {
        if (i10 < 0) {
            return -1;
        }
        int c10 = this.f3672a.c();
        int i11 = i10;
        while (i11 < c10) {
            int b10 = i10 - (i11 - this.f3673b.b(i11));
            if (b10 == 0) {
                while (this.f3673b.d(i11)) {
                    i11++;
                }
                return i11;
            }
            i11 += b10;
        }
        return -1;
    }

    private void l(View view) {
        this.f3674c.add(view);
        this.f3672a.b(view);
    }

    private boolean t(View view) {
        if (!this.f3674c.remove(view)) {
            return false;
        }
        this.f3672a.h(view);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(View view, int i10, boolean z10) {
        int h10;
        if (i10 < 0) {
            h10 = this.f3672a.c();
        } else {
            h10 = h(i10);
        }
        this.f3673b.e(h10, z10);
        if (z10) {
            l(view);
        }
        this.f3672a.i(view, h10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(View view, boolean z10) {
        a(view, -1, z10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(View view, int i10, ViewGroup.LayoutParams layoutParams, boolean z10) {
        int h10;
        if (i10 < 0) {
            h10 = this.f3672a.c();
        } else {
            h10 = h(i10);
        }
        this.f3673b.e(h10, z10);
        if (z10) {
            l(view);
        }
        this.f3672a.k(view, h10, layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(int i10) {
        int h10 = h(i10);
        this.f3673b.f(h10);
        this.f3672a.g(h10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View e(int i10) {
        int size = this.f3674c.size();
        for (int i11 = 0; i11 < size; i11++) {
            View view = this.f3674c.get(i11);
            RecyclerView.c0 f10 = this.f3672a.f(view);
            if (f10.getLayoutPosition() == i10 && !f10.isInvalid() && !f10.isRemoved()) {
                return view;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View f(int i10) {
        return this.f3672a.a(h(i10));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int g() {
        return this.f3672a.c() - this.f3674c.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View i(int i10) {
        return this.f3672a.a(i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int j() {
        return this.f3672a.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(View view) {
        int e10 = this.f3672a.e(view);
        if (e10 >= 0) {
            this.f3673b.h(e10);
            l(view);
        } else {
            throw new IllegalArgumentException("view is not a child, cannot hide " + view);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int m(View view) {
        int e10 = this.f3672a.e(view);
        if (e10 == -1 || this.f3673b.d(e10)) {
            return -1;
        }
        return e10 - this.f3673b.b(e10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean n(View view) {
        return this.f3674c.contains(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o() {
        this.f3673b.g();
        for (int size = this.f3674c.size() - 1; size >= 0; size--) {
            this.f3672a.h(this.f3674c.get(size));
            this.f3674c.remove(size);
        }
        this.f3672a.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p(View view) {
        int e10 = this.f3672a.e(view);
        if (e10 < 0) {
            return;
        }
        if (this.f3673b.f(e10)) {
            t(view);
        }
        this.f3672a.j(e10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(int i10) {
        int h10 = h(i10);
        View a10 = this.f3672a.a(h10);
        if (a10 == null) {
            return;
        }
        if (this.f3673b.f(h10)) {
            t(a10);
        }
        this.f3672a.j(h10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean r(View view) {
        int e10 = this.f3672a.e(view);
        if (e10 == -1) {
            t(view);
            return true;
        }
        if (!this.f3673b.d(e10)) {
            return false;
        }
        this.f3673b.f(e10);
        t(view);
        this.f3672a.j(e10);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(View view) {
        int e10 = this.f3672a.e(view);
        if (e10 >= 0) {
            if (this.f3673b.d(e10)) {
                this.f3673b.a(e10);
                t(view);
                return;
            } else {
                throw new RuntimeException("trying to unhide a view that was not hidden" + view);
            }
        }
        throw new IllegalArgumentException("view is not a child, cannot hide " + view);
    }

    public String toString() {
        return this.f3673b.toString() + ", hidden list:" + this.f3674c.size();
    }
}
