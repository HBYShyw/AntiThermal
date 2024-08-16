package androidx.recyclerview.widget;

import androidx.recyclerview.widget.RecyclerView;
import j.LongSparseArray;
import j.SimpleArrayMap;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ViewInfoStore.java */
/* renamed from: androidx.recyclerview.widget.s, reason: use source file name */
/* loaded from: classes.dex */
public class ViewInfoStore {

    /* renamed from: a, reason: collision with root package name */
    final SimpleArrayMap<RecyclerView.c0, a> f3817a = new SimpleArrayMap<>();

    /* renamed from: b, reason: collision with root package name */
    final LongSparseArray<RecyclerView.c0> f3818b = new LongSparseArray<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ViewInfoStore.java */
    /* renamed from: androidx.recyclerview.widget.s$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: d, reason: collision with root package name */
        static androidx.core.util.e<a> f3819d = new androidx.core.util.f(20);

        /* renamed from: a, reason: collision with root package name */
        int f3820a;

        /* renamed from: b, reason: collision with root package name */
        RecyclerView.m.c f3821b;

        /* renamed from: c, reason: collision with root package name */
        RecyclerView.m.c f3822c;

        private a() {
        }

        static void a() {
            do {
            } while (f3819d.b() != null);
        }

        static a b() {
            a b10 = f3819d.b();
            return b10 == null ? new a() : b10;
        }

        static void c(a aVar) {
            aVar.f3820a = 0;
            aVar.f3821b = null;
            aVar.f3822c = null;
            f3819d.a(aVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ViewInfoStore.java */
    /* renamed from: androidx.recyclerview.widget.s$b */
    /* loaded from: classes.dex */
    public interface b {
        void a(RecyclerView.c0 c0Var);

        void b(RecyclerView.c0 c0Var, RecyclerView.m.c cVar, RecyclerView.m.c cVar2);

        void c(RecyclerView.c0 c0Var, RecyclerView.m.c cVar, RecyclerView.m.c cVar2);

        void d(RecyclerView.c0 c0Var, RecyclerView.m.c cVar, RecyclerView.m.c cVar2);
    }

    private RecyclerView.m.c l(RecyclerView.c0 c0Var, int i10) {
        a n10;
        RecyclerView.m.c cVar;
        int g6 = this.f3817a.g(c0Var);
        if (g6 >= 0 && (n10 = this.f3817a.n(g6)) != null) {
            int i11 = n10.f3820a;
            if ((i11 & i10) != 0) {
                int i12 = (~i10) & i11;
                n10.f3820a = i12;
                if (i10 == 4) {
                    cVar = n10.f3821b;
                } else if (i10 == 8) {
                    cVar = n10.f3822c;
                } else {
                    throw new IllegalArgumentException("Must provide flag PRE or POST");
                }
                if ((i12 & 12) == 0) {
                    this.f3817a.l(g6);
                    a.c(n10);
                }
                return cVar;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(RecyclerView.c0 c0Var, RecyclerView.m.c cVar) {
        a aVar = this.f3817a.get(c0Var);
        if (aVar == null) {
            aVar = a.b();
            this.f3817a.put(c0Var, aVar);
        }
        aVar.f3820a |= 2;
        aVar.f3821b = cVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(RecyclerView.c0 c0Var) {
        a aVar = this.f3817a.get(c0Var);
        if (aVar == null) {
            aVar = a.b();
            this.f3817a.put(c0Var, aVar);
        }
        aVar.f3820a |= 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(long j10, RecyclerView.c0 c0Var) {
        this.f3818b.j(j10, c0Var);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(RecyclerView.c0 c0Var, RecyclerView.m.c cVar) {
        a aVar = this.f3817a.get(c0Var);
        if (aVar == null) {
            aVar = a.b();
            this.f3817a.put(c0Var, aVar);
        }
        aVar.f3822c = cVar;
        aVar.f3820a |= 8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(RecyclerView.c0 c0Var, RecyclerView.m.c cVar) {
        a aVar = this.f3817a.get(c0Var);
        if (aVar == null) {
            aVar = a.b();
            this.f3817a.put(c0Var, aVar);
        }
        aVar.f3821b = cVar;
        aVar.f3820a |= 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        this.f3817a.clear();
        this.f3818b.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RecyclerView.c0 g(long j10) {
        return this.f3818b.e(j10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean h(RecyclerView.c0 c0Var) {
        a aVar = this.f3817a.get(c0Var);
        return (aVar == null || (aVar.f3820a & 1) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean i(RecyclerView.c0 c0Var) {
        a aVar = this.f3817a.get(c0Var);
        return (aVar == null || (aVar.f3820a & 4) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j() {
        a.a();
    }

    public void k(RecyclerView.c0 c0Var) {
        p(c0Var);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RecyclerView.m.c m(RecyclerView.c0 c0Var) {
        return l(c0Var, 8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RecyclerView.m.c n(RecyclerView.c0 c0Var) {
        return l(c0Var, 4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(b bVar) {
        for (int size = this.f3817a.size() - 1; size >= 0; size--) {
            RecyclerView.c0 j10 = this.f3817a.j(size);
            a l10 = this.f3817a.l(size);
            int i10 = l10.f3820a;
            if ((i10 & 3) == 3) {
                bVar.a(j10);
            } else if ((i10 & 1) != 0) {
                RecyclerView.m.c cVar = l10.f3821b;
                if (cVar == null) {
                    bVar.a(j10);
                } else {
                    bVar.c(j10, cVar, l10.f3822c);
                }
            } else if ((i10 & 14) == 14) {
                bVar.b(j10, l10.f3821b, l10.f3822c);
            } else if ((i10 & 12) == 12) {
                bVar.d(j10, l10.f3821b, l10.f3822c);
            } else if ((i10 & 4) != 0) {
                bVar.c(j10, l10.f3821b, null);
            } else if ((i10 & 8) != 0) {
                bVar.b(j10, l10.f3821b, l10.f3822c);
            }
            a.c(l10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p(RecyclerView.c0 c0Var) {
        a aVar = this.f3817a.get(c0Var);
        if (aVar == null) {
            return;
        }
        aVar.f3820a &= -2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(RecyclerView.c0 c0Var) {
        int n10 = this.f3818b.n() - 1;
        while (true) {
            if (n10 < 0) {
                break;
            }
            if (c0Var == this.f3818b.o(n10)) {
                this.f3818b.l(n10);
                break;
            }
            n10--;
        }
        a remove = this.f3817a.remove(c0Var);
        if (remove != null) {
            a.c(remove);
        }
    }
}
