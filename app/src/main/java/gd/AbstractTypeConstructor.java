package gd;

import fd.StorageManager;
import id.ErrorUtils;
import java.util.Collection;
import java.util.List;
import kotlin.collections.CollectionsJVM;
import kotlin.collections._Collections;
import ma.Unit;
import mb.KotlinBuiltIns;
import pb.ClassifierDescriptor;
import pb.SupertypeLoopChecker;
import pb.TypeParameterDescriptor;
import za.Lambda;

/* compiled from: AbstractTypeConstructor.kt */
/* renamed from: gd.g, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractTypeConstructor extends ClassifierBasedTypeConstructor {

    /* renamed from: b, reason: collision with root package name */
    private final fd.i<b> f11796b;

    /* renamed from: c, reason: collision with root package name */
    private final boolean f11797c;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AbstractTypeConstructor.kt */
    /* renamed from: gd.g$a */
    /* loaded from: classes2.dex */
    public final class a implements TypeConstructor {

        /* renamed from: a, reason: collision with root package name */
        private final hd.g f11798a;

        /* renamed from: b, reason: collision with root package name */
        private final ma.h f11799b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ AbstractTypeConstructor f11800c;

        /* compiled from: AbstractTypeConstructor.kt */
        /* renamed from: gd.g$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0041a extends Lambda implements ya.a<List<? extends g0>> {

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ AbstractTypeConstructor f11802f;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C0041a(AbstractTypeConstructor abstractTypeConstructor) {
                super(0);
                this.f11802f = abstractTypeConstructor;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<g0> invoke() {
                return hd.h.b(a.this.f11798a, this.f11802f.q());
            }
        }

        public a(AbstractTypeConstructor abstractTypeConstructor, hd.g gVar) {
            ma.h a10;
            za.k.e(gVar, "kotlinTypeRefiner");
            this.f11800c = abstractTypeConstructor;
            this.f11798a = gVar;
            a10 = ma.j.a(ma.l.PUBLICATION, new C0041a(abstractTypeConstructor));
            this.f11799b = a10;
        }

        private final List<g0> d() {
            return (List) this.f11799b.getValue();
        }

        @Override // gd.TypeConstructor
        /* renamed from: e, reason: merged with bridge method [inline-methods] */
        public List<g0> q() {
            return d();
        }

        public boolean equals(Object obj) {
            return this.f11800c.equals(obj);
        }

        @Override // gd.TypeConstructor
        public List<TypeParameterDescriptor> getParameters() {
            List<TypeParameterDescriptor> parameters = this.f11800c.getParameters();
            za.k.d(parameters, "this@AbstractTypeConstructor.parameters");
            return parameters;
        }

        public int hashCode() {
            return this.f11800c.hashCode();
        }

        @Override // gd.TypeConstructor
        public KotlinBuiltIns t() {
            KotlinBuiltIns t7 = this.f11800c.t();
            za.k.d(t7, "this@AbstractTypeConstructor.builtIns");
            return t7;
        }

        public String toString() {
            return this.f11800c.toString();
        }

        @Override // gd.TypeConstructor
        public TypeConstructor u(hd.g gVar) {
            za.k.e(gVar, "kotlinTypeRefiner");
            return this.f11800c.u(gVar);
        }

        @Override // gd.TypeConstructor
        public ClassifierDescriptor v() {
            return this.f11800c.v();
        }

        @Override // gd.TypeConstructor
        public boolean w() {
            return this.f11800c.w();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AbstractTypeConstructor.kt */
    /* renamed from: gd.g$b */
    /* loaded from: classes2.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private final Collection<g0> f11803a;

        /* renamed from: b, reason: collision with root package name */
        private List<? extends g0> f11804b;

        /* JADX WARN: Multi-variable type inference failed */
        public b(Collection<? extends g0> collection) {
            List<? extends g0> e10;
            za.k.e(collection, "allSupertypes");
            this.f11803a = collection;
            e10 = CollectionsJVM.e(ErrorUtils.f12833a.l());
            this.f11804b = e10;
        }

        public final Collection<g0> a() {
            return this.f11803a;
        }

        public final List<g0> b() {
            return this.f11804b;
        }

        public final void c(List<? extends g0> list) {
            za.k.e(list, "<set-?>");
            this.f11804b = list;
        }
    }

    /* compiled from: AbstractTypeConstructor.kt */
    /* renamed from: gd.g$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<b> {
        c() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final b invoke() {
            return new b(AbstractTypeConstructor.this.h());
        }
    }

    /* compiled from: AbstractTypeConstructor.kt */
    /* renamed from: gd.g$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.l<Boolean, b> {

        /* renamed from: e, reason: collision with root package name */
        public static final d f11806e = new d();

        d() {
            super(1);
        }

        public final b a(boolean z10) {
            List e10;
            e10 = CollectionsJVM.e(ErrorUtils.f12833a.l());
            return new b(e10);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ b invoke(Boolean bool) {
            return a(bool.booleanValue());
        }
    }

    /* compiled from: AbstractTypeConstructor.kt */
    /* renamed from: gd.g$e */
    /* loaded from: classes2.dex */
    static final class e extends Lambda implements ya.l<b, Unit> {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: AbstractTypeConstructor.kt */
        /* renamed from: gd.g$e$a */
        /* loaded from: classes2.dex */
        public static final class a extends Lambda implements ya.l<TypeConstructor, Iterable<? extends g0>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ AbstractTypeConstructor f11808e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(AbstractTypeConstructor abstractTypeConstructor) {
                super(1);
                this.f11808e = abstractTypeConstructor;
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Iterable<g0> invoke(TypeConstructor typeConstructor) {
                za.k.e(typeConstructor, "it");
                return this.f11808e.g(typeConstructor, true);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: AbstractTypeConstructor.kt */
        /* renamed from: gd.g$e$b */
        /* loaded from: classes2.dex */
        public static final class b extends Lambda implements ya.l<g0, Unit> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ AbstractTypeConstructor f11809e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            b(AbstractTypeConstructor abstractTypeConstructor) {
                super(1);
                this.f11809e = abstractTypeConstructor;
            }

            public final void a(g0 g0Var) {
                za.k.e(g0Var, "it");
                this.f11809e.o(g0Var);
            }

            @Override // ya.l
            public /* bridge */ /* synthetic */ Unit invoke(g0 g0Var) {
                a(g0Var);
                return Unit.f15173a;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: AbstractTypeConstructor.kt */
        /* renamed from: gd.g$e$c */
        /* loaded from: classes2.dex */
        public static final class c extends Lambda implements ya.l<TypeConstructor, Iterable<? extends g0>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ AbstractTypeConstructor f11810e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            c(AbstractTypeConstructor abstractTypeConstructor) {
                super(1);
                this.f11810e = abstractTypeConstructor;
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Iterable<g0> invoke(TypeConstructor typeConstructor) {
                za.k.e(typeConstructor, "it");
                return this.f11810e.g(typeConstructor, false);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: AbstractTypeConstructor.kt */
        /* renamed from: gd.g$e$d */
        /* loaded from: classes2.dex */
        public static final class d extends Lambda implements ya.l<g0, Unit> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ AbstractTypeConstructor f11811e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            d(AbstractTypeConstructor abstractTypeConstructor) {
                super(1);
                this.f11811e = abstractTypeConstructor;
            }

            public final void a(g0 g0Var) {
                za.k.e(g0Var, "it");
                this.f11811e.p(g0Var);
            }

            @Override // ya.l
            public /* bridge */ /* synthetic */ Unit invoke(g0 g0Var) {
                a(g0Var);
                return Unit.f15173a;
            }
        }

        e() {
            super(1);
        }

        public final void a(b bVar) {
            za.k.e(bVar, "supertypes");
            Collection<g0> a10 = AbstractTypeConstructor.this.l().a(AbstractTypeConstructor.this, bVar.a(), new c(AbstractTypeConstructor.this), new d(AbstractTypeConstructor.this));
            if (a10.isEmpty()) {
                g0 i10 = AbstractTypeConstructor.this.i();
                a10 = i10 != null ? CollectionsJVM.e(i10) : null;
                if (a10 == null) {
                    a10 = kotlin.collections.r.j();
                }
            }
            if (AbstractTypeConstructor.this.k()) {
                SupertypeLoopChecker l10 = AbstractTypeConstructor.this.l();
                AbstractTypeConstructor abstractTypeConstructor = AbstractTypeConstructor.this;
                l10.a(abstractTypeConstructor, a10, new a(abstractTypeConstructor), new b(AbstractTypeConstructor.this));
            }
            AbstractTypeConstructor abstractTypeConstructor2 = AbstractTypeConstructor.this;
            List<g0> list = a10 instanceof List ? (List) a10 : null;
            if (list == null) {
                list = _Collections.z0(a10);
            }
            bVar.c(abstractTypeConstructor2.n(list));
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(b bVar) {
            a(bVar);
            return Unit.f15173a;
        }
    }

    public AbstractTypeConstructor(StorageManager storageManager) {
        za.k.e(storageManager, "storageManager");
        this.f11796b = storageManager.h(new c(), d.f11806e, new e());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x000b, code lost:
    
        r1 = kotlin.collections._Collections.m0(r1.f11796b.invoke().a(), r1.j(r3));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Collection<g0> g(TypeConstructor typeConstructor, boolean z10) {
        List m02;
        AbstractTypeConstructor abstractTypeConstructor = typeConstructor instanceof AbstractTypeConstructor ? (AbstractTypeConstructor) typeConstructor : null;
        if (abstractTypeConstructor != null && m02 != null) {
            return m02;
        }
        Collection<g0> q10 = typeConstructor.q();
        za.k.d(q10, "supertypes");
        return q10;
    }

    protected abstract Collection<g0> h();

    protected g0 i() {
        return null;
    }

    protected Collection<g0> j(boolean z10) {
        List j10;
        j10 = kotlin.collections.r.j();
        return j10;
    }

    protected boolean k() {
        return this.f11797c;
    }

    protected abstract SupertypeLoopChecker l();

    @Override // gd.TypeConstructor
    /* renamed from: m, reason: merged with bridge method [inline-methods] */
    public List<g0> q() {
        return this.f11796b.invoke().b();
    }

    protected List<g0> n(List<g0> list) {
        za.k.e(list, "supertypes");
        return list;
    }

    protected void o(g0 g0Var) {
        za.k.e(g0Var, "type");
    }

    protected void p(g0 g0Var) {
        za.k.e(g0Var, "type");
    }

    @Override // gd.TypeConstructor
    public TypeConstructor u(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        return new a(this, gVar);
    }
}
