package gd;

import java.util.ArrayDeque;
import java.util.Set;
import ma.Unit;
import qd.SmartSet;
import za.DefaultConstructorMarker;

/* compiled from: AbstractTypeChecker.kt */
/* loaded from: classes2.dex */
public class f1 {

    /* renamed from: a, reason: collision with root package name */
    private final boolean f11778a;

    /* renamed from: b, reason: collision with root package name */
    private final boolean f11779b;

    /* renamed from: c, reason: collision with root package name */
    private final boolean f11780c;

    /* renamed from: d, reason: collision with root package name */
    private final kd.p f11781d;

    /* renamed from: e, reason: collision with root package name */
    private final AbstractTypePreparator f11782e;

    /* renamed from: f, reason: collision with root package name */
    private final AbstractTypeRefiner f11783f;

    /* renamed from: g, reason: collision with root package name */
    private int f11784g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f11785h;

    /* renamed from: i, reason: collision with root package name */
    private ArrayDeque<kd.k> f11786i;

    /* renamed from: j, reason: collision with root package name */
    private Set<kd.k> f11787j;

    /* compiled from: AbstractTypeChecker.kt */
    /* loaded from: classes2.dex */
    public interface a {

        /* compiled from: AbstractTypeChecker.kt */
        /* renamed from: gd.f1$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0039a implements a {

            /* renamed from: a, reason: collision with root package name */
            private boolean f11788a;

            @Override // gd.f1.a
            public void a(ya.a<Boolean> aVar) {
                za.k.e(aVar, "block");
                if (this.f11788a) {
                    return;
                }
                this.f11788a = aVar.invoke().booleanValue();
            }

            public final boolean b() {
                return this.f11788a;
            }
        }

        void a(ya.a<Boolean> aVar);
    }

    /* compiled from: AbstractTypeChecker.kt */
    /* loaded from: classes2.dex */
    public enum b {
        CHECK_ONLY_LOWER,
        CHECK_SUBTYPE_AND_LOWER,
        SKIP_LOWER
    }

    /* compiled from: AbstractTypeChecker.kt */
    /* loaded from: classes2.dex */
    public static abstract class c {

        /* compiled from: AbstractTypeChecker.kt */
        /* loaded from: classes2.dex */
        public static abstract class a extends c {
            public a() {
                super(null);
            }
        }

        /* compiled from: AbstractTypeChecker.kt */
        /* loaded from: classes2.dex */
        public static final class b extends c {

            /* renamed from: a, reason: collision with root package name */
            public static final b f11793a = new b();

            private b() {
                super(null);
            }

            @Override // gd.f1.c
            public kd.k a(f1 f1Var, kd.i iVar) {
                za.k.e(f1Var, "state");
                za.k.e(iVar, "type");
                return f1Var.j().v0(iVar);
            }
        }

        /* compiled from: AbstractTypeChecker.kt */
        /* renamed from: gd.f1$c$c, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0040c extends c {

            /* renamed from: a, reason: collision with root package name */
            public static final C0040c f11794a = new C0040c();

            private C0040c() {
                super(null);
            }

            @Override // gd.f1.c
            public /* bridge */ /* synthetic */ kd.k a(f1 f1Var, kd.i iVar) {
                return (kd.k) b(f1Var, iVar);
            }

            public Void b(f1 f1Var, kd.i iVar) {
                za.k.e(f1Var, "state");
                za.k.e(iVar, "type");
                throw new UnsupportedOperationException("Should not be called");
            }
        }

        /* compiled from: AbstractTypeChecker.kt */
        /* loaded from: classes2.dex */
        public static final class d extends c {

            /* renamed from: a, reason: collision with root package name */
            public static final d f11795a = new d();

            private d() {
                super(null);
            }

            @Override // gd.f1.c
            public kd.k a(f1 f1Var, kd.i iVar) {
                za.k.e(f1Var, "state");
                za.k.e(iVar, "type");
                return f1Var.j().s0(iVar);
            }
        }

        private c() {
        }

        public /* synthetic */ c(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public abstract kd.k a(f1 f1Var, kd.i iVar);
    }

    public f1(boolean z10, boolean z11, boolean z12, kd.p pVar, AbstractTypePreparator abstractTypePreparator, AbstractTypeRefiner abstractTypeRefiner) {
        za.k.e(pVar, "typeSystemContext");
        za.k.e(abstractTypePreparator, "kotlinTypePreparator");
        za.k.e(abstractTypeRefiner, "kotlinTypeRefiner");
        this.f11778a = z10;
        this.f11779b = z11;
        this.f11780c = z12;
        this.f11781d = pVar;
        this.f11782e = abstractTypePreparator;
        this.f11783f = abstractTypeRefiner;
    }

    public static /* synthetic */ Boolean d(f1 f1Var, kd.i iVar, kd.i iVar2, boolean z10, int i10, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: addSubtypeConstraint");
        }
        if ((i10 & 4) != 0) {
            z10 = false;
        }
        return f1Var.c(iVar, iVar2, z10);
    }

    public Boolean c(kd.i iVar, kd.i iVar2, boolean z10) {
        za.k.e(iVar, "subType");
        za.k.e(iVar2, "superType");
        return null;
    }

    public final void e() {
        ArrayDeque<kd.k> arrayDeque = this.f11786i;
        za.k.b(arrayDeque);
        arrayDeque.clear();
        Set<kd.k> set = this.f11787j;
        za.k.b(set);
        set.clear();
        this.f11785h = false;
    }

    public boolean f(kd.i iVar, kd.i iVar2) {
        za.k.e(iVar, "subType");
        za.k.e(iVar2, "superType");
        return true;
    }

    public b g(kd.k kVar, kd.d dVar) {
        za.k.e(kVar, "subType");
        za.k.e(dVar, "superType");
        return b.CHECK_SUBTYPE_AND_LOWER;
    }

    public final ArrayDeque<kd.k> h() {
        return this.f11786i;
    }

    public final Set<kd.k> i() {
        return this.f11787j;
    }

    public final kd.p j() {
        return this.f11781d;
    }

    public final void k() {
        this.f11785h = true;
        if (this.f11786i == null) {
            this.f11786i = new ArrayDeque<>(4);
        }
        if (this.f11787j == null) {
            this.f11787j = SmartSet.f17432g.a();
        }
    }

    public final boolean l(kd.i iVar) {
        za.k.e(iVar, "type");
        return this.f11780c && this.f11781d.u0(iVar);
    }

    public final boolean m() {
        return this.f11778a;
    }

    public final boolean n() {
        return this.f11779b;
    }

    public final kd.i o(kd.i iVar) {
        za.k.e(iVar, "type");
        return this.f11782e.a(iVar);
    }

    public final kd.i p(kd.i iVar) {
        za.k.e(iVar, "type");
        return this.f11783f.a(iVar);
    }

    public boolean q(ya.l<? super a, Unit> lVar) {
        za.k.e(lVar, "block");
        a.C0039a c0039a = new a.C0039a();
        lVar.invoke(c0039a);
        return c0039a.b();
    }
}
