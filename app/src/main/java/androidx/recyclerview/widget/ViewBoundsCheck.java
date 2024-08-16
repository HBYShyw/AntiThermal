package androidx.recyclerview.widget;

import android.view.View;

/* compiled from: ViewBoundsCheck.java */
/* renamed from: androidx.recyclerview.widget.r, reason: use source file name */
/* loaded from: classes.dex */
class ViewBoundsCheck {

    /* renamed from: a, reason: collision with root package name */
    final b f3810a;

    /* renamed from: b, reason: collision with root package name */
    a f3811b = new a();

    /* compiled from: ViewBoundsCheck.java */
    /* renamed from: androidx.recyclerview.widget.r$a */
    /* loaded from: classes.dex */
    static class a {

        /* renamed from: a, reason: collision with root package name */
        int f3812a = 0;

        /* renamed from: b, reason: collision with root package name */
        int f3813b;

        /* renamed from: c, reason: collision with root package name */
        int f3814c;

        /* renamed from: d, reason: collision with root package name */
        int f3815d;

        /* renamed from: e, reason: collision with root package name */
        int f3816e;

        a() {
        }

        void a(int i10) {
            this.f3812a = i10 | this.f3812a;
        }

        boolean b() {
            int i10 = this.f3812a;
            if ((i10 & 7) != 0 && (i10 & (c(this.f3815d, this.f3813b) << 0)) == 0) {
                return false;
            }
            int i11 = this.f3812a;
            if ((i11 & 112) != 0 && (i11 & (c(this.f3815d, this.f3814c) << 4)) == 0) {
                return false;
            }
            int i12 = this.f3812a;
            if ((i12 & 1792) != 0 && (i12 & (c(this.f3816e, this.f3813b) << 8)) == 0) {
                return false;
            }
            int i13 = this.f3812a;
            return (i13 & 28672) == 0 || ((c(this.f3816e, this.f3814c) << 12) & i13) != 0;
        }

        int c(int i10, int i11) {
            if (i10 > i11) {
                return 1;
            }
            return i10 == i11 ? 2 : 4;
        }

        void d() {
            this.f3812a = 0;
        }

        void e(int i10, int i11, int i12, int i13) {
            this.f3813b = i10;
            this.f3814c = i11;
            this.f3815d = i12;
            this.f3816e = i13;
        }
    }

    /* compiled from: ViewBoundsCheck.java */
    /* renamed from: androidx.recyclerview.widget.r$b */
    /* loaded from: classes.dex */
    interface b {
        View a(int i10);

        int b(View view);

        int c();

        int d();

        int e(View view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ViewBoundsCheck(b bVar) {
        this.f3810a = bVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View a(int i10, int i11, int i12, int i13) {
        int c10 = this.f3810a.c();
        int d10 = this.f3810a.d();
        int i14 = i11 > i10 ? 1 : -1;
        View view = null;
        while (i10 != i11) {
            View a10 = this.f3810a.a(i10);
            this.f3811b.e(c10, d10, this.f3810a.b(a10), this.f3810a.e(a10));
            if (i12 != 0) {
                this.f3811b.d();
                this.f3811b.a(i12);
                if (this.f3811b.b()) {
                    return a10;
                }
            }
            if (i13 != 0) {
                this.f3811b.d();
                this.f3811b.a(i13);
                if (this.f3811b.b()) {
                    view = a10;
                }
            }
            i10 += i14;
        }
        return view;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(View view, int i10) {
        this.f3811b.e(this.f3810a.c(), this.f3810a.d(), this.f3810a.b(view), this.f3810a.e(view));
        if (i10 == 0) {
            return false;
        }
        this.f3811b.d();
        this.f3811b.a(i10);
        return this.f3811b.b();
    }
}
