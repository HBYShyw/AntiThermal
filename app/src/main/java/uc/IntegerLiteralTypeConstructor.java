package uc;

import gd.TypeConstructor;
import gd.TypeProjectionImpl;
import gd.Variance;
import gd.c1;
import gd.g0;
import gd.h0;
import gd.o0;
import gd.o1;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.collections.CollectionsJVM;
import kotlin.collections._Collections;
import ma.NoWhenBranchMatchedException;
import mb.KotlinBuiltIns;
import pb.ClassifierDescriptor;
import pb.ModuleDescriptor;
import pb.TypeParameterDescriptor;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: IntegerLiteralTypeConstructor.kt */
/* renamed from: uc.n, reason: use source file name */
/* loaded from: classes2.dex */
public final class IntegerLiteralTypeConstructor implements TypeConstructor {

    /* renamed from: f, reason: collision with root package name */
    public static final a f18998f = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final long f18999a;

    /* renamed from: b, reason: collision with root package name */
    private final ModuleDescriptor f19000b;

    /* renamed from: c, reason: collision with root package name */
    private final Set<g0> f19001c;

    /* renamed from: d, reason: collision with root package name */
    private final o0 f19002d;

    /* renamed from: e, reason: collision with root package name */
    private final ma.h f19003e;

    /* compiled from: IntegerLiteralTypeConstructor.kt */
    /* renamed from: uc.n$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* JADX INFO: Access modifiers changed from: private */
        /* compiled from: IntegerLiteralTypeConstructor.kt */
        /* renamed from: uc.n$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public enum EnumC0109a {
            COMMON_SUPER_TYPE,
            INTERSECTION_TYPE
        }

        /* compiled from: IntegerLiteralTypeConstructor.kt */
        /* renamed from: uc.n$a$b */
        /* loaded from: classes2.dex */
        public /* synthetic */ class b {

            /* renamed from: a, reason: collision with root package name */
            public static final /* synthetic */ int[] f19007a;

            static {
                int[] iArr = new int[EnumC0109a.values().length];
                try {
                    iArr[EnumC0109a.COMMON_SUPER_TYPE.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[EnumC0109a.INTERSECTION_TYPE.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                f19007a = iArr;
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final o0 a(Collection<? extends o0> collection, EnumC0109a enumC0109a) {
            if (collection.isEmpty()) {
                return null;
            }
            Iterator<T> it = collection.iterator();
            if (it.hasNext()) {
                Object next = it.next();
                while (it.hasNext()) {
                    o0 o0Var = (o0) it.next();
                    next = IntegerLiteralTypeConstructor.f18998f.c((o0) next, o0Var, enumC0109a);
                }
                return (o0) next;
            }
            throw new UnsupportedOperationException("Empty collection can't be reduced.");
        }

        private final o0 c(o0 o0Var, o0 o0Var2, EnumC0109a enumC0109a) {
            if (o0Var == null || o0Var2 == null) {
                return null;
            }
            TypeConstructor W0 = o0Var.W0();
            TypeConstructor W02 = o0Var2.W0();
            boolean z10 = W0 instanceof IntegerLiteralTypeConstructor;
            if (z10 && (W02 instanceof IntegerLiteralTypeConstructor)) {
                return e((IntegerLiteralTypeConstructor) W0, (IntegerLiteralTypeConstructor) W02, enumC0109a);
            }
            if (z10) {
                return d((IntegerLiteralTypeConstructor) W0, o0Var2);
            }
            if (W02 instanceof IntegerLiteralTypeConstructor) {
                return d((IntegerLiteralTypeConstructor) W02, o0Var);
            }
            return null;
        }

        private final o0 d(IntegerLiteralTypeConstructor integerLiteralTypeConstructor, o0 o0Var) {
            if (integerLiteralTypeConstructor.g().contains(o0Var)) {
                return o0Var;
            }
            return null;
        }

        private final o0 e(IntegerLiteralTypeConstructor integerLiteralTypeConstructor, IntegerLiteralTypeConstructor integerLiteralTypeConstructor2, EnumC0109a enumC0109a) {
            Set Y;
            int i10 = b.f19007a[enumC0109a.ordinal()];
            if (i10 == 1) {
                Y = _Collections.Y(integerLiteralTypeConstructor.g(), integerLiteralTypeConstructor2.g());
            } else {
                if (i10 != 2) {
                    throw new NoWhenBranchMatchedException();
                }
                Y = _Collections.E0(integerLiteralTypeConstructor.g(), integerLiteralTypeConstructor2.g());
            }
            return h0.e(c1.f11749f.h(), new IntegerLiteralTypeConstructor(integerLiteralTypeConstructor.f18999a, integerLiteralTypeConstructor.f19000b, Y, null), false);
        }

        public final o0 b(Collection<? extends o0> collection) {
            za.k.e(collection, "types");
            return a(collection, EnumC0109a.INTERSECTION_TYPE);
        }
    }

    /* compiled from: IntegerLiteralTypeConstructor.kt */
    /* renamed from: uc.n$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<List<o0>> {
        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<o0> invoke() {
            List e10;
            List<o0> p10;
            o0 x10 = IntegerLiteralTypeConstructor.this.t().x().x();
            za.k.d(x10, "builtIns.comparable.defaultType");
            e10 = CollectionsJVM.e(new TypeProjectionImpl(Variance.IN_VARIANCE, IntegerLiteralTypeConstructor.this.f19002d));
            p10 = kotlin.collections.r.p(o1.f(x10, e10, null, 2, null));
            if (!IntegerLiteralTypeConstructor.this.i()) {
                p10.add(IntegerLiteralTypeConstructor.this.t().L());
            }
            return p10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: IntegerLiteralTypeConstructor.kt */
    /* renamed from: uc.n$c */
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.l<g0, CharSequence> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f19009e = new c();

        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CharSequence invoke(g0 g0Var) {
            za.k.e(g0Var, "it");
            return g0Var.toString();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private IntegerLiteralTypeConstructor(long j10, ModuleDescriptor moduleDescriptor, Set<? extends g0> set) {
        ma.h b10;
        this.f19002d = h0.e(c1.f11749f.h(), this, false);
        b10 = ma.j.b(new b());
        this.f19003e = b10;
        this.f18999a = j10;
        this.f19000b = moduleDescriptor;
        this.f19001c = set;
    }

    public /* synthetic */ IntegerLiteralTypeConstructor(long j10, ModuleDescriptor moduleDescriptor, Set set, DefaultConstructorMarker defaultConstructorMarker) {
        this(j10, moduleDescriptor, set);
    }

    private final List<g0> h() {
        return (List) this.f19003e.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean i() {
        Collection<g0> a10 = PrimitiveTypeUtil.a(this.f19000b);
        if ((a10 instanceof Collection) && a10.isEmpty()) {
            return true;
        }
        Iterator<T> it = a10.iterator();
        while (it.hasNext()) {
            if (!(!this.f19001c.contains((g0) it.next()))) {
                return false;
            }
        }
        return true;
    }

    private final String j() {
        String c02;
        StringBuilder sb2 = new StringBuilder();
        sb2.append('[');
        c02 = _Collections.c0(this.f19001c, ",", null, null, 0, null, c.f19009e, 30, null);
        sb2.append(c02);
        sb2.append(']');
        return sb2.toString();
    }

    public final Set<g0> g() {
        return this.f19001c;
    }

    @Override // gd.TypeConstructor
    public List<TypeParameterDescriptor> getParameters() {
        List<TypeParameterDescriptor> j10;
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // gd.TypeConstructor
    public Collection<g0> q() {
        return h();
    }

    @Override // gd.TypeConstructor
    public KotlinBuiltIns t() {
        return this.f19000b.t();
    }

    public String toString() {
        return "IntegerLiteralType" + j();
    }

    @Override // gd.TypeConstructor
    public TypeConstructor u(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        return this;
    }

    @Override // gd.TypeConstructor
    public ClassifierDescriptor v() {
        return null;
    }

    @Override // gd.TypeConstructor
    public boolean w() {
        return false;
    }
}
