package qd;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import ya.l;

/* compiled from: DFS.java */
/* renamed from: qd.b, reason: use source file name */
/* loaded from: classes2.dex */
public class DFS {

    /* JADX INFO: Add missing generic type declarations: [N] */
    /* compiled from: DFS.java */
    /* renamed from: qd.b$a */
    /* loaded from: classes2.dex */
    static class a<N> extends b<N, Boolean> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ l f17411a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ boolean[] f17412b;

        a(l lVar, boolean[] zArr) {
            this.f17411a = lVar;
            this.f17412b = zArr;
        }

        @Override // qd.DFS.d
        public boolean c(N n10) {
            if (((Boolean) this.f17411a.invoke(n10)).booleanValue()) {
                this.f17412b[0] = true;
            }
            return !this.f17412b[0];
        }

        @Override // qd.DFS.d
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public Boolean a() {
            return Boolean.valueOf(this.f17412b[0]);
        }
    }

    /* compiled from: DFS.java */
    /* renamed from: qd.b$b */
    /* loaded from: classes2.dex */
    public static abstract class b<N, R> implements d<N, R> {
        @Override // qd.DFS.d
        public void b(N n10) {
        }
    }

    /* compiled from: DFS.java */
    /* renamed from: qd.b$c */
    /* loaded from: classes2.dex */
    public interface c<N> {
        Iterable<? extends N> a(N n10);
    }

    /* compiled from: DFS.java */
    /* renamed from: qd.b$d */
    /* loaded from: classes2.dex */
    public interface d<N, R> {
        R a();

        void b(N n10);

        boolean c(N n10);
    }

    /* compiled from: DFS.java */
    /* renamed from: qd.b$e */
    /* loaded from: classes2.dex */
    public interface e<N> {
        boolean a(N n10);
    }

    /* compiled from: DFS.java */
    /* renamed from: qd.b$f */
    /* loaded from: classes2.dex */
    public static class f<N> implements e<N> {

        /* renamed from: a, reason: collision with root package name */
        private final Set<N> f17413a;

        public f() {
            this(new HashSet());
        }

        private static /* synthetic */ void b(int i10) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "visited", "kotlin/reflect/jvm/internal/impl/utils/DFS$VisitedWithSet", "<init>"));
        }

        @Override // qd.DFS.e
        public boolean a(N n10) {
            return this.f17413a.add(n10);
        }

        public f(Set<N> set) {
            if (set == null) {
                b(0);
            }
            this.f17413a = set;
        }
    }

    private static /* synthetic */ void a(int i10) {
        Object[] objArr = new Object[3];
        switch (i10) {
            case 1:
            case 5:
            case 8:
            case 11:
            case 15:
            case 18:
            case 21:
            case 23:
                objArr[0] = "neighbors";
                break;
            case 2:
            case 12:
            case 16:
            case 19:
            case 24:
                objArr[0] = "visited";
                break;
            case 3:
            case 6:
            case 13:
            case 25:
                objArr[0] = "handler";
                break;
            case 4:
            case 7:
            case 17:
            case 20:
            default:
                objArr[0] = "nodes";
                break;
            case 9:
                objArr[0] = "predicate";
                break;
            case 10:
            case 14:
                objArr[0] = "node";
                break;
            case 22:
                objArr[0] = "current";
                break;
        }
        objArr[1] = "kotlin/reflect/jvm/internal/impl/utils/DFS";
        switch (i10) {
            case 7:
            case 8:
            case 9:
                objArr[2] = "ifAny";
                break;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
                objArr[2] = "dfsFromNode";
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
                objArr[2] = "topologicalOrder";
                break;
            case 22:
            case 23:
            case 24:
            case 25:
                objArr[2] = "doDfs";
                break;
            default:
                objArr[2] = "dfs";
                break;
        }
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
    }

    public static <N, R> R b(Collection<N> collection, c<N> cVar, d<N, R> dVar) {
        if (collection == null) {
            a(4);
        }
        if (cVar == null) {
            a(5);
        }
        if (dVar == null) {
            a(6);
        }
        return (R) c(collection, cVar, new f(), dVar);
    }

    public static <N, R> R c(Collection<N> collection, c<N> cVar, e<N> eVar, d<N, R> dVar) {
        if (collection == null) {
            a(0);
        }
        if (cVar == null) {
            a(1);
        }
        if (eVar == null) {
            a(2);
        }
        if (dVar == null) {
            a(3);
        }
        Iterator<N> it = collection.iterator();
        while (it.hasNext()) {
            d(it.next(), cVar, eVar, dVar);
        }
        return dVar.a();
    }

    public static <N> void d(N n10, c<N> cVar, e<N> eVar, d<N, ?> dVar) {
        if (n10 == null) {
            a(22);
        }
        if (cVar == null) {
            a(23);
        }
        if (eVar == null) {
            a(24);
        }
        if (dVar == null) {
            a(25);
        }
        if (eVar.a(n10) && dVar.c(n10)) {
            Iterator<? extends N> it = cVar.a(n10).iterator();
            while (it.hasNext()) {
                d(it.next(), cVar, eVar, dVar);
            }
            dVar.b(n10);
        }
    }

    public static <N> Boolean e(Collection<N> collection, c<N> cVar, l<N, Boolean> lVar) {
        if (collection == null) {
            a(7);
        }
        if (cVar == null) {
            a(8);
        }
        if (lVar == null) {
            a(9);
        }
        return (Boolean) b(collection, cVar, new a(lVar, new boolean[1]));
    }
}
