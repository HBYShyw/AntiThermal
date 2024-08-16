package androidx.recyclerview.widget;

import androidx.recyclerview.widget.OpReorderer;
import androidx.recyclerview.widget.RecyclerView;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdapterHelper.java */
/* renamed from: androidx.recyclerview.widget.a, reason: use source file name */
/* loaded from: classes.dex */
public final class AdapterHelper implements OpReorderer.a {

    /* renamed from: a, reason: collision with root package name */
    private androidx.core.util.e<b> f3597a;

    /* renamed from: b, reason: collision with root package name */
    final ArrayList<b> f3598b;

    /* renamed from: c, reason: collision with root package name */
    final ArrayList<b> f3599c;

    /* renamed from: d, reason: collision with root package name */
    final a f3600d;

    /* renamed from: e, reason: collision with root package name */
    Runnable f3601e;

    /* renamed from: f, reason: collision with root package name */
    final boolean f3602f;

    /* renamed from: g, reason: collision with root package name */
    final OpReorderer f3603g;

    /* renamed from: h, reason: collision with root package name */
    private int f3604h;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AdapterHelper.java */
    /* renamed from: androidx.recyclerview.widget.a$a */
    /* loaded from: classes.dex */
    public interface a {
        void a(int i10, int i11);

        void b(b bVar);

        void c(int i10, int i11, Object obj);

        void d(b bVar);

        RecyclerView.c0 e(int i10);

        void f(int i10, int i11);

        void g(int i10, int i11);

        void h(int i10, int i11);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AdapterHelper.java */
    /* renamed from: androidx.recyclerview.widget.a$b */
    /* loaded from: classes.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        int f3605a;

        /* renamed from: b, reason: collision with root package name */
        int f3606b;

        /* renamed from: c, reason: collision with root package name */
        Object f3607c;

        /* renamed from: d, reason: collision with root package name */
        int f3608d;

        b(int i10, int i11, int i12, Object obj) {
            this.f3605a = i10;
            this.f3606b = i11;
            this.f3608d = i12;
            this.f3607c = obj;
        }

        String a() {
            int i10 = this.f3605a;
            return i10 != 1 ? i10 != 2 ? i10 != 4 ? i10 != 8 ? "??" : "mv" : "up" : "rm" : EventType.STATE_PACKAGE_CHANGED_ADD;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof b)) {
                return false;
            }
            b bVar = (b) obj;
            int i10 = this.f3605a;
            if (i10 != bVar.f3605a) {
                return false;
            }
            if (i10 == 8 && Math.abs(this.f3608d - this.f3606b) == 1 && this.f3608d == bVar.f3606b && this.f3606b == bVar.f3608d) {
                return true;
            }
            if (this.f3608d != bVar.f3608d || this.f3606b != bVar.f3606b) {
                return false;
            }
            Object obj2 = this.f3607c;
            if (obj2 != null) {
                if (!obj2.equals(bVar.f3607c)) {
                    return false;
                }
            } else if (bVar.f3607c != null) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (((this.f3605a * 31) + this.f3606b) * 31) + this.f3608d;
        }

        public String toString() {
            return Integer.toHexString(System.identityHashCode(this)) + "[" + a() + ",s:" + this.f3606b + "c:" + this.f3608d + ",p:" + this.f3607c + "]";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AdapterHelper(a aVar) {
        this(aVar, false);
    }

    private void c(b bVar) {
        v(bVar);
    }

    private void d(b bVar) {
        v(bVar);
    }

    private void f(b bVar) {
        boolean z10;
        char c10;
        int i10 = bVar.f3606b;
        int i11 = bVar.f3608d + i10;
        char c11 = 65535;
        int i12 = i10;
        int i13 = 0;
        while (i12 < i11) {
            if (this.f3600d.e(i12) != null || h(i12)) {
                if (c11 == 0) {
                    k(b(2, i10, i13, null));
                    z10 = true;
                } else {
                    z10 = false;
                }
                c10 = 1;
            } else {
                if (c11 == 1) {
                    v(b(2, i10, i13, null));
                    z10 = true;
                } else {
                    z10 = false;
                }
                c10 = 0;
            }
            if (z10) {
                i12 -= i13;
                i11 -= i13;
                i13 = 1;
            } else {
                i13++;
            }
            i12++;
            c11 = c10;
        }
        if (i13 != bVar.f3608d) {
            a(bVar);
            bVar = b(2, i10, i13, null);
        }
        if (c11 == 0) {
            k(bVar);
        } else {
            v(bVar);
        }
    }

    private void g(b bVar) {
        int i10 = bVar.f3606b;
        int i11 = bVar.f3608d + i10;
        int i12 = 0;
        boolean z10 = -1;
        int i13 = i10;
        while (i10 < i11) {
            if (this.f3600d.e(i10) != null || h(i10)) {
                if (!z10) {
                    k(b(4, i13, i12, bVar.f3607c));
                    i13 = i10;
                    i12 = 0;
                }
                z10 = true;
            } else {
                if (z10) {
                    v(b(4, i13, i12, bVar.f3607c));
                    i13 = i10;
                    i12 = 0;
                }
                z10 = false;
            }
            i12++;
            i10++;
        }
        if (i12 != bVar.f3608d) {
            Object obj = bVar.f3607c;
            a(bVar);
            bVar = b(4, i13, i12, obj);
        }
        if (!z10) {
            k(bVar);
        } else {
            v(bVar);
        }
    }

    private boolean h(int i10) {
        int size = this.f3599c.size();
        for (int i11 = 0; i11 < size; i11++) {
            b bVar = this.f3599c.get(i11);
            int i12 = bVar.f3605a;
            if (i12 == 8) {
                if (n(bVar.f3608d, i11 + 1) == i10) {
                    return true;
                }
            } else if (i12 == 1) {
                int i13 = bVar.f3606b;
                int i14 = bVar.f3608d + i13;
                while (i13 < i14) {
                    if (n(i13, i11 + 1) == i10) {
                        return true;
                    }
                    i13++;
                }
            } else {
                continue;
            }
        }
        return false;
    }

    private void k(b bVar) {
        int i10;
        int i11 = bVar.f3605a;
        if (i11 != 1 && i11 != 8) {
            int z10 = z(bVar.f3606b, i11);
            int i12 = bVar.f3606b;
            int i13 = bVar.f3605a;
            if (i13 == 2) {
                i10 = 0;
            } else {
                if (i13 != 4) {
                    throw new IllegalArgumentException("op should be remove or update." + bVar);
                }
                i10 = 1;
            }
            int i14 = 1;
            for (int i15 = 1; i15 < bVar.f3608d; i15++) {
                int z11 = z(bVar.f3606b + (i10 * i15), bVar.f3605a);
                int i16 = bVar.f3605a;
                if (i16 == 2 ? z11 == z10 : i16 == 4 && z11 == z10 + 1) {
                    i14++;
                } else {
                    b b10 = b(i16, z10, i14, bVar.f3607c);
                    l(b10, i12);
                    a(b10);
                    if (bVar.f3605a == 4) {
                        i12 += i14;
                    }
                    i14 = 1;
                    z10 = z11;
                }
            }
            Object obj = bVar.f3607c;
            a(bVar);
            if (i14 > 0) {
                b b11 = b(bVar.f3605a, z10, i14, obj);
                l(b11, i12);
                a(b11);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("should not dispatch add or move for pre layout");
    }

    private void v(b bVar) {
        this.f3599c.add(bVar);
        int i10 = bVar.f3605a;
        if (i10 == 1) {
            this.f3600d.g(bVar.f3606b, bVar.f3608d);
            return;
        }
        if (i10 == 2) {
            this.f3600d.f(bVar.f3606b, bVar.f3608d);
            return;
        }
        if (i10 == 4) {
            this.f3600d.c(bVar.f3606b, bVar.f3608d, bVar.f3607c);
        } else {
            if (i10 == 8) {
                this.f3600d.a(bVar.f3606b, bVar.f3608d);
                return;
            }
            throw new IllegalArgumentException("Unknown update op type for " + bVar);
        }
    }

    private int z(int i10, int i11) {
        int i12;
        int i13;
        for (int size = this.f3599c.size() - 1; size >= 0; size--) {
            b bVar = this.f3599c.get(size);
            int i14 = bVar.f3605a;
            if (i14 == 8) {
                int i15 = bVar.f3606b;
                int i16 = bVar.f3608d;
                if (i15 < i16) {
                    i13 = i15;
                    i12 = i16;
                } else {
                    i12 = i15;
                    i13 = i16;
                }
                if (i10 < i13 || i10 > i12) {
                    if (i10 < i15) {
                        if (i11 == 1) {
                            bVar.f3606b = i15 + 1;
                            bVar.f3608d = i16 + 1;
                        } else if (i11 == 2) {
                            bVar.f3606b = i15 - 1;
                            bVar.f3608d = i16 - 1;
                        }
                    }
                } else if (i13 == i15) {
                    if (i11 == 1) {
                        bVar.f3608d = i16 + 1;
                    } else if (i11 == 2) {
                        bVar.f3608d = i16 - 1;
                    }
                    i10++;
                } else {
                    if (i11 == 1) {
                        bVar.f3606b = i15 + 1;
                    } else if (i11 == 2) {
                        bVar.f3606b = i15 - 1;
                    }
                    i10--;
                }
            } else {
                int i17 = bVar.f3606b;
                if (i17 <= i10) {
                    if (i14 == 1) {
                        i10 -= bVar.f3608d;
                    } else if (i14 == 2) {
                        i10 += bVar.f3608d;
                    }
                } else if (i11 == 1) {
                    bVar.f3606b = i17 + 1;
                } else if (i11 == 2) {
                    bVar.f3606b = i17 - 1;
                }
            }
        }
        for (int size2 = this.f3599c.size() - 1; size2 >= 0; size2--) {
            b bVar2 = this.f3599c.get(size2);
            if (bVar2.f3605a == 8) {
                int i18 = bVar2.f3608d;
                if (i18 == bVar2.f3606b || i18 < 0) {
                    this.f3599c.remove(size2);
                    a(bVar2);
                }
            } else if (bVar2.f3608d <= 0) {
                this.f3599c.remove(size2);
                a(bVar2);
            }
        }
        return i10;
    }

    @Override // androidx.recyclerview.widget.OpReorderer.a
    public void a(b bVar) {
        if (this.f3602f) {
            return;
        }
        bVar.f3607c = null;
        this.f3597a.a(bVar);
    }

    @Override // androidx.recyclerview.widget.OpReorderer.a
    public b b(int i10, int i11, int i12, Object obj) {
        b b10 = this.f3597a.b();
        if (b10 == null) {
            return new b(i10, i11, i12, obj);
        }
        b10.f3605a = i10;
        b10.f3606b = i11;
        b10.f3608d = i12;
        b10.f3607c = obj;
        return b10;
    }

    public int e(int i10) {
        int size = this.f3598b.size();
        for (int i11 = 0; i11 < size; i11++) {
            b bVar = this.f3598b.get(i11);
            int i12 = bVar.f3605a;
            if (i12 != 1) {
                if (i12 == 2) {
                    int i13 = bVar.f3606b;
                    if (i13 <= i10) {
                        int i14 = bVar.f3608d;
                        if (i13 + i14 > i10) {
                            return -1;
                        }
                        i10 -= i14;
                    } else {
                        continue;
                    }
                } else if (i12 == 8) {
                    int i15 = bVar.f3606b;
                    if (i15 == i10) {
                        i10 = bVar.f3608d;
                    } else {
                        if (i15 < i10) {
                            i10--;
                        }
                        if (bVar.f3608d <= i10) {
                            i10++;
                        }
                    }
                }
            } else if (bVar.f3606b <= i10) {
                i10 += bVar.f3608d;
            }
        }
        return i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i() {
        int size = this.f3599c.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.f3600d.d(this.f3599c.get(i10));
        }
        x(this.f3599c);
        this.f3604h = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j() {
        i();
        int size = this.f3598b.size();
        for (int i10 = 0; i10 < size; i10++) {
            b bVar = this.f3598b.get(i10);
            int i11 = bVar.f3605a;
            if (i11 == 1) {
                this.f3600d.d(bVar);
                this.f3600d.g(bVar.f3606b, bVar.f3608d);
            } else if (i11 == 2) {
                this.f3600d.d(bVar);
                this.f3600d.h(bVar.f3606b, bVar.f3608d);
            } else if (i11 == 4) {
                this.f3600d.d(bVar);
                this.f3600d.c(bVar.f3606b, bVar.f3608d, bVar.f3607c);
            } else if (i11 == 8) {
                this.f3600d.d(bVar);
                this.f3600d.a(bVar.f3606b, bVar.f3608d);
            }
            Runnable runnable = this.f3601e;
            if (runnable != null) {
                runnable.run();
            }
        }
        x(this.f3598b);
        this.f3604h = 0;
    }

    void l(b bVar, int i10) {
        this.f3600d.b(bVar);
        int i11 = bVar.f3605a;
        if (i11 == 2) {
            this.f3600d.h(i10, bVar.f3608d);
        } else {
            if (i11 == 4) {
                this.f3600d.c(i10, bVar.f3608d, bVar.f3607c);
                return;
            }
            throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int m(int i10) {
        return n(i10, 0);
    }

    int n(int i10, int i11) {
        int size = this.f3599c.size();
        while (i11 < size) {
            b bVar = this.f3599c.get(i11);
            int i12 = bVar.f3605a;
            if (i12 == 8) {
                int i13 = bVar.f3606b;
                if (i13 == i10) {
                    i10 = bVar.f3608d;
                } else {
                    if (i13 < i10) {
                        i10--;
                    }
                    if (bVar.f3608d <= i10) {
                        i10++;
                    }
                }
            } else {
                int i14 = bVar.f3606b;
                if (i14 > i10) {
                    continue;
                } else if (i12 == 2) {
                    int i15 = bVar.f3608d;
                    if (i10 < i14 + i15) {
                        return -1;
                    }
                    i10 -= i15;
                } else if (i12 == 1) {
                    i10 += bVar.f3608d;
                }
            }
            i11++;
        }
        return i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean o(int i10) {
        return (this.f3604h & i10) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean p() {
        return this.f3598b.size() > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean q() {
        return (this.f3599c.isEmpty() || this.f3598b.isEmpty()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean r(int i10, int i11, Object obj) {
        if (i11 < 1) {
            return false;
        }
        this.f3598b.add(b(4, i10, i11, obj));
        this.f3604h |= 4;
        return this.f3598b.size() == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean s(int i10, int i11) {
        if (i11 < 1) {
            return false;
        }
        this.f3598b.add(b(1, i10, i11, null));
        this.f3604h |= 1;
        return this.f3598b.size() == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean t(int i10, int i11, int i12) {
        if (i10 == i11) {
            return false;
        }
        if (i12 == 1) {
            this.f3598b.add(b(8, i10, i11, null));
            this.f3604h |= 8;
            return this.f3598b.size() == 1;
        }
        throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean u(int i10, int i11) {
        if (i11 < 1) {
            return false;
        }
        this.f3598b.add(b(2, i10, i11, null));
        this.f3604h |= 2;
        return this.f3598b.size() == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w() {
        this.f3603g.b(this.f3598b);
        int size = this.f3598b.size();
        for (int i10 = 0; i10 < size; i10++) {
            b bVar = this.f3598b.get(i10);
            int i11 = bVar.f3605a;
            if (i11 == 1) {
                c(bVar);
            } else if (i11 == 2) {
                f(bVar);
            } else if (i11 == 4) {
                g(bVar);
            } else if (i11 == 8) {
                d(bVar);
            }
            Runnable runnable = this.f3601e;
            if (runnable != null) {
                runnable.run();
            }
        }
        this.f3598b.clear();
    }

    void x(List<b> list) {
        int size = list.size();
        for (int i10 = 0; i10 < size; i10++) {
            a(list.get(i10));
        }
        list.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y() {
        x(this.f3598b);
        x(this.f3599c);
        this.f3604h = 0;
    }

    AdapterHelper(a aVar, boolean z10) {
        this.f3597a = new androidx.core.util.f(30);
        this.f3598b = new ArrayList<>();
        this.f3599c = new ArrayList<>();
        this.f3604h = 0;
        this.f3600d = aVar;
        this.f3602f = z10;
        this.f3603g = new OpReorderer(this);
    }
}
