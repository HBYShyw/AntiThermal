package pb;

import ad.ReceiverValue;
import ad.SuperCallReceiverValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import nd.ModuleVisibilityHelper;
import pb.Visibilities;
import qd.collections;

/* compiled from: DescriptorVisibilities.java */
/* renamed from: pb.t, reason: use source file name */
/* loaded from: classes2.dex */
public class DescriptorVisibilities {

    /* renamed from: a, reason: collision with root package name */
    public static final u f16729a;

    /* renamed from: b, reason: collision with root package name */
    public static final u f16730b;

    /* renamed from: c, reason: collision with root package name */
    public static final u f16731c;

    /* renamed from: d, reason: collision with root package name */
    public static final u f16732d;

    /* renamed from: e, reason: collision with root package name */
    public static final u f16733e;

    /* renamed from: f, reason: collision with root package name */
    public static final u f16734f;

    /* renamed from: g, reason: collision with root package name */
    public static final u f16735g;

    /* renamed from: h, reason: collision with root package name */
    public static final u f16736h;

    /* renamed from: i, reason: collision with root package name */
    public static final u f16737i;

    /* renamed from: j, reason: collision with root package name */
    public static final Set<u> f16738j;

    /* renamed from: k, reason: collision with root package name */
    private static final Map<u, Integer> f16739k;

    /* renamed from: l, reason: collision with root package name */
    public static final u f16740l;

    /* renamed from: m, reason: collision with root package name */
    private static final ReceiverValue f16741m;

    /* renamed from: n, reason: collision with root package name */
    public static final ReceiverValue f16742n;

    /* renamed from: o, reason: collision with root package name */
    @Deprecated
    public static final ReceiverValue f16743o;

    /* renamed from: p, reason: collision with root package name */
    private static final ModuleVisibilityHelper f16744p;

    /* renamed from: q, reason: collision with root package name */
    private static final Map<n1, u> f16745q;

    /* compiled from: DescriptorVisibilities.java */
    /* renamed from: pb.t$a */
    /* loaded from: classes2.dex */
    static class a implements ReceiverValue {
        a() {
        }

        @Override // ad.ReceiverValue
        public gd.g0 getType() {
            throw new IllegalStateException("This method should not be called");
        }
    }

    /* compiled from: DescriptorVisibilities.java */
    /* renamed from: pb.t$b */
    /* loaded from: classes2.dex */
    static class b implements ReceiverValue {
        b() {
        }

        @Override // ad.ReceiverValue
        public gd.g0 getType() {
            throw new IllegalStateException("This method should not be called");
        }
    }

    /* compiled from: DescriptorVisibilities.java */
    /* renamed from: pb.t$c */
    /* loaded from: classes2.dex */
    static class c implements ReceiverValue {
        c() {
        }

        @Override // ad.ReceiverValue
        public gd.g0 getType() {
            throw new IllegalStateException("This method should not be called");
        }
    }

    /* compiled from: DescriptorVisibilities.java */
    /* renamed from: pb.t$d */
    /* loaded from: classes2.dex */
    static class d extends r {
        d(n1 n1Var) {
            super(n1Var);
        }

        private static /* synthetic */ void g(int i10) {
            Object[] objArr = new Object[3];
            if (i10 == 1) {
                objArr[0] = "what";
            } else if (i10 != 2) {
                objArr[0] = "descriptor";
            } else {
                objArr[0] = "from";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/DescriptorVisibilities$1";
            if (i10 == 1 || i10 == 2) {
                objArr[2] = "isVisible";
            } else {
                objArr[2] = "hasContainingSourceFile";
            }
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        private boolean h(DeclarationDescriptor declarationDescriptor) {
            if (declarationDescriptor == null) {
                g(0);
            }
            return sc.e.j(declarationDescriptor) != b1.f16671a;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r3v0, types: [pb.q, pb.m] */
        /* JADX WARN: Type inference failed for: r3v1, types: [pb.m] */
        /* JADX WARN: Type inference failed for: r3v2, types: [pb.m] */
        /* JADX WARN: Type inference failed for: r3v4, types: [pb.m] */
        @Override // pb.u
        public boolean e(ReceiverValue receiverValue, DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility, DeclarationDescriptor declarationDescriptor, boolean z10) {
            if (declarationDescriptorWithVisibility == 0) {
                g(1);
            }
            if (declarationDescriptor == null) {
                g(2);
            }
            if (sc.e.J(declarationDescriptorWithVisibility) && h(declarationDescriptor)) {
                return DescriptorVisibilities.f(declarationDescriptorWithVisibility, declarationDescriptor);
            }
            if (declarationDescriptorWithVisibility instanceof ConstructorDescriptor) {
                ClassifierDescriptorWithTypeParameters b10 = ((ConstructorDescriptor) declarationDescriptorWithVisibility).b();
                if (z10 && sc.e.G(b10) && sc.e.J(b10) && (declarationDescriptor instanceof ConstructorDescriptor) && sc.e.J(declarationDescriptor.b()) && DescriptorVisibilities.f(declarationDescriptorWithVisibility, declarationDescriptor)) {
                    return true;
                }
            }
            while (declarationDescriptorWithVisibility != 0) {
                declarationDescriptorWithVisibility = declarationDescriptorWithVisibility.b();
                if (((declarationDescriptorWithVisibility instanceof ClassDescriptor) && !sc.e.x(declarationDescriptorWithVisibility)) || (declarationDescriptorWithVisibility instanceof PackageFragmentDescriptor)) {
                    break;
                }
            }
            if (declarationDescriptorWithVisibility == 0) {
                return false;
            }
            while (declarationDescriptor != null) {
                if (declarationDescriptorWithVisibility == declarationDescriptor) {
                    return true;
                }
                if (declarationDescriptor instanceof PackageFragmentDescriptor) {
                    return (declarationDescriptorWithVisibility instanceof PackageFragmentDescriptor) && declarationDescriptorWithVisibility.d().equals(((PackageFragmentDescriptor) declarationDescriptor).d()) && sc.e.b(declarationDescriptor, declarationDescriptorWithVisibility);
                }
                declarationDescriptor = declarationDescriptor.b();
            }
            return false;
        }
    }

    /* compiled from: DescriptorVisibilities.java */
    /* renamed from: pb.t$e */
    /* loaded from: classes2.dex */
    static class e extends r {
        e(n1 n1Var) {
            super(n1Var);
        }

        private static /* synthetic */ void g(int i10) {
            Object[] objArr = new Object[3];
            if (i10 != 1) {
                objArr[0] = "what";
            } else {
                objArr[0] = "from";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/DescriptorVisibilities$2";
            objArr[2] = "isVisible";
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        @Override // pb.u
        public boolean e(ReceiverValue receiverValue, DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility, DeclarationDescriptor declarationDescriptor, boolean z10) {
            DeclarationDescriptor q10;
            if (declarationDescriptorWithVisibility == null) {
                g(0);
            }
            if (declarationDescriptor == null) {
                g(1);
            }
            if (!DescriptorVisibilities.f16729a.e(receiverValue, declarationDescriptorWithVisibility, declarationDescriptor, z10)) {
                return false;
            }
            if (receiverValue == DescriptorVisibilities.f16742n) {
                return true;
            }
            if (receiverValue == DescriptorVisibilities.f16741m || (q10 = sc.e.q(declarationDescriptorWithVisibility, ClassDescriptor.class)) == null || !(receiverValue instanceof ad.i)) {
                return false;
            }
            return ((ad.i) receiverValue).w().a().equals(q10.a());
        }
    }

    /* compiled from: DescriptorVisibilities.java */
    /* renamed from: pb.t$f */
    /* loaded from: classes2.dex */
    static class f extends r {
        f(n1 n1Var) {
            super(n1Var);
        }

        private static /* synthetic */ void g(int i10) {
            Object[] objArr = new Object[3];
            if (i10 == 1) {
                objArr[0] = "from";
            } else if (i10 == 2) {
                objArr[0] = "whatDeclaration";
            } else if (i10 != 3) {
                objArr[0] = "what";
            } else {
                objArr[0] = "fromClass";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/DescriptorVisibilities$3";
            if (i10 == 2 || i10 == 3) {
                objArr[2] = "doesReceiverFitForProtectedVisibility";
            } else {
                objArr[2] = "isVisible";
            }
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        private boolean h(ReceiverValue receiverValue, DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility, ClassDescriptor classDescriptor) {
            gd.g0 type;
            if (declarationDescriptorWithVisibility == null) {
                g(2);
            }
            if (classDescriptor == null) {
                g(3);
            }
            if (receiverValue == DescriptorVisibilities.f16743o) {
                return false;
            }
            if (!(declarationDescriptorWithVisibility instanceof CallableMemberDescriptor) || (declarationDescriptorWithVisibility instanceof ConstructorDescriptor) || receiverValue == DescriptorVisibilities.f16742n) {
                return true;
            }
            if (receiverValue == DescriptorVisibilities.f16741m || receiverValue == null) {
                return false;
            }
            if (receiverValue instanceof SuperCallReceiverValue) {
                type = ((SuperCallReceiverValue) receiverValue).b();
            } else {
                type = receiverValue.getType();
            }
            return sc.e.I(type, classDescriptor) || gd.w.a(type);
        }

        @Override // pb.u
        public boolean e(ReceiverValue receiverValue, DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility, DeclarationDescriptor declarationDescriptor, boolean z10) {
            ClassDescriptor classDescriptor;
            if (declarationDescriptorWithVisibility == null) {
                g(0);
            }
            if (declarationDescriptor == null) {
                g(1);
            }
            ClassDescriptor classDescriptor2 = (ClassDescriptor) sc.e.q(declarationDescriptorWithVisibility, ClassDescriptor.class);
            ClassDescriptor classDescriptor3 = (ClassDescriptor) sc.e.r(declarationDescriptor, ClassDescriptor.class, false);
            if (classDescriptor3 == null) {
                return false;
            }
            if (classDescriptor2 != null && sc.e.x(classDescriptor2) && (classDescriptor = (ClassDescriptor) sc.e.q(classDescriptor2, ClassDescriptor.class)) != null && sc.e.H(classDescriptor3, classDescriptor)) {
                return true;
            }
            DeclarationDescriptorWithVisibility M = sc.e.M(declarationDescriptorWithVisibility);
            ClassDescriptor classDescriptor4 = (ClassDescriptor) sc.e.q(M, ClassDescriptor.class);
            if (classDescriptor4 == null) {
                return false;
            }
            if (sc.e.H(classDescriptor3, classDescriptor4) && h(receiverValue, M, classDescriptor3)) {
                return true;
            }
            return e(receiverValue, declarationDescriptorWithVisibility, classDescriptor3.b(), z10);
        }
    }

    /* compiled from: DescriptorVisibilities.java */
    /* renamed from: pb.t$g */
    /* loaded from: classes2.dex */
    static class g extends r {
        g(n1 n1Var) {
            super(n1Var);
        }

        private static /* synthetic */ void g(int i10) {
            Object[] objArr = new Object[3];
            if (i10 != 1) {
                objArr[0] = "what";
            } else {
                objArr[0] = "from";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/DescriptorVisibilities$4";
            objArr[2] = "isVisible";
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        @Override // pb.u
        public boolean e(ReceiverValue receiverValue, DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility, DeclarationDescriptor declarationDescriptor, boolean z10) {
            if (declarationDescriptorWithVisibility == null) {
                g(0);
            }
            if (declarationDescriptor == null) {
                g(1);
            }
            if (sc.e.g(declarationDescriptor).E0(sc.e.g(declarationDescriptorWithVisibility))) {
                return DescriptorVisibilities.f16744p.a(declarationDescriptorWithVisibility, declarationDescriptor);
            }
            return false;
        }
    }

    /* compiled from: DescriptorVisibilities.java */
    /* renamed from: pb.t$h */
    /* loaded from: classes2.dex */
    static class h extends r {
        h(n1 n1Var) {
            super(n1Var);
        }

        private static /* synthetic */ void g(int i10) {
            Object[] objArr = new Object[3];
            if (i10 != 1) {
                objArr[0] = "what";
            } else {
                objArr[0] = "from";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/DescriptorVisibilities$5";
            objArr[2] = "isVisible";
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        @Override // pb.u
        public boolean e(ReceiverValue receiverValue, DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility, DeclarationDescriptor declarationDescriptor, boolean z10) {
            if (declarationDescriptorWithVisibility == null) {
                g(0);
            }
            if (declarationDescriptor == null) {
                g(1);
            }
            return true;
        }
    }

    /* compiled from: DescriptorVisibilities.java */
    /* renamed from: pb.t$i */
    /* loaded from: classes2.dex */
    static class i extends r {
        i(n1 n1Var) {
            super(n1Var);
        }

        private static /* synthetic */ void g(int i10) {
            Object[] objArr = new Object[3];
            if (i10 != 1) {
                objArr[0] = "what";
            } else {
                objArr[0] = "from";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/DescriptorVisibilities$6";
            objArr[2] = "isVisible";
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        @Override // pb.u
        public boolean e(ReceiverValue receiverValue, DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility, DeclarationDescriptor declarationDescriptor, boolean z10) {
            if (declarationDescriptorWithVisibility == null) {
                g(0);
            }
            if (declarationDescriptor == null) {
                g(1);
            }
            throw new IllegalStateException("This method shouldn't be invoked for LOCAL visibility");
        }
    }

    /* compiled from: DescriptorVisibilities.java */
    /* renamed from: pb.t$j */
    /* loaded from: classes2.dex */
    static class j extends r {
        j(n1 n1Var) {
            super(n1Var);
        }

        private static /* synthetic */ void g(int i10) {
            Object[] objArr = new Object[3];
            if (i10 != 1) {
                objArr[0] = "what";
            } else {
                objArr[0] = "from";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/DescriptorVisibilities$7";
            objArr[2] = "isVisible";
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        @Override // pb.u
        public boolean e(ReceiverValue receiverValue, DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility, DeclarationDescriptor declarationDescriptor, boolean z10) {
            if (declarationDescriptorWithVisibility == null) {
                g(0);
            }
            if (declarationDescriptor == null) {
                g(1);
            }
            throw new IllegalStateException("Visibility is unknown yet");
        }
    }

    /* compiled from: DescriptorVisibilities.java */
    /* renamed from: pb.t$k */
    /* loaded from: classes2.dex */
    static class k extends r {
        k(n1 n1Var) {
            super(n1Var);
        }

        private static /* synthetic */ void g(int i10) {
            Object[] objArr = new Object[3];
            if (i10 != 1) {
                objArr[0] = "what";
            } else {
                objArr[0] = "from";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/DescriptorVisibilities$8";
            objArr[2] = "isVisible";
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        @Override // pb.u
        public boolean e(ReceiverValue receiverValue, DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility, DeclarationDescriptor declarationDescriptor, boolean z10) {
            if (declarationDescriptorWithVisibility == null) {
                g(0);
            }
            if (declarationDescriptor == null) {
                g(1);
            }
            return false;
        }
    }

    /* compiled from: DescriptorVisibilities.java */
    /* renamed from: pb.t$l */
    /* loaded from: classes2.dex */
    static class l extends r {
        l(n1 n1Var) {
            super(n1Var);
        }

        private static /* synthetic */ void g(int i10) {
            Object[] objArr = new Object[3];
            if (i10 != 1) {
                objArr[0] = "what";
            } else {
                objArr[0] = "from";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/DescriptorVisibilities$9";
            objArr[2] = "isVisible";
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        @Override // pb.u
        public boolean e(ReceiverValue receiverValue, DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility, DeclarationDescriptor declarationDescriptor, boolean z10) {
            if (declarationDescriptorWithVisibility == null) {
                g(0);
            }
            if (declarationDescriptor == null) {
                g(1);
            }
            return false;
        }
    }

    static {
        Set h10;
        d dVar = new d(Visibilities.e.f16715c);
        f16729a = dVar;
        e eVar = new e(Visibilities.f.f16716c);
        f16730b = eVar;
        f fVar = new f(Visibilities.g.f16717c);
        f16731c = fVar;
        g gVar = new g(Visibilities.b.f16712c);
        f16732d = gVar;
        h hVar = new h(Visibilities.h.f16718c);
        f16733e = hVar;
        i iVar = new i(Visibilities.d.f16714c);
        f16734f = iVar;
        j jVar = new j(Visibilities.a.f16711c);
        f16735g = jVar;
        k kVar = new k(Visibilities.c.f16713c);
        f16736h = kVar;
        l lVar = new l(Visibilities.i.f16719c);
        f16737i = lVar;
        h10 = kotlin.collections.s0.h(dVar, eVar, gVar, iVar);
        f16738j = Collections.unmodifiableSet(h10);
        HashMap e10 = collections.e(4);
        e10.put(eVar, 0);
        e10.put(dVar, 0);
        e10.put(gVar, 1);
        e10.put(fVar, 1);
        e10.put(hVar, 2);
        f16739k = Collections.unmodifiableMap(e10);
        f16740l = hVar;
        f16741m = new a();
        f16742n = new b();
        f16743o = new c();
        Iterator it = ServiceLoader.load(ModuleVisibilityHelper.class, ModuleVisibilityHelper.class.getClassLoader()).iterator();
        f16744p = it.hasNext() ? (ModuleVisibilityHelper) it.next() : ModuleVisibilityHelper.a.f16034a;
        f16745q = new HashMap();
        i(dVar);
        i(eVar);
        i(fVar);
        i(gVar);
        i(hVar);
        i(iVar);
        i(jVar);
        i(kVar);
        i(lVar);
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0061  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x006b  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x007a  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0045  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static /* synthetic */ void a(int i10) {
        String str = i10 != 16 ? "Argument for @NotNull parameter '%s' of %s.%s must not be null" : "@NotNull method %s.%s must not return null";
        Object[] objArr = new Object[i10 != 16 ? 3 : 2];
        if (i10 != 1 && i10 != 3 && i10 != 5 && i10 != 7) {
            switch (i10) {
                case 9:
                    break;
                case 10:
                case 12:
                    objArr[0] = "first";
                    break;
                case 11:
                case 13:
                    objArr[0] = "second";
                    break;
                case 14:
                case 15:
                    objArr[0] = "visibility";
                    break;
                case 16:
                    objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/DescriptorVisibilities";
                    break;
                default:
                    objArr[0] = "what";
                    break;
            }
            if (i10 == 16) {
                objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/DescriptorVisibilities";
            } else {
                objArr[1] = "toDescriptorVisibility";
            }
            switch (i10) {
                case 2:
                case 3:
                    objArr[2] = "isVisibleIgnoringReceiver";
                    break;
                case 4:
                case 5:
                    objArr[2] = "isVisibleWithAnyReceiver";
                    break;
                case 6:
                case 7:
                    objArr[2] = "inSameFile";
                    break;
                case 8:
                case 9:
                    objArr[2] = "findInvisibleMember";
                    break;
                case 10:
                case 11:
                    objArr[2] = "compareLocal";
                    break;
                case 12:
                case 13:
                    objArr[2] = "compare";
                    break;
                case 14:
                    objArr[2] = "isPrivate";
                    break;
                case 15:
                    objArr[2] = "toDescriptorVisibility";
                    break;
                case 16:
                    break;
                default:
                    objArr[2] = "isVisible";
                    break;
            }
            String format = String.format(str, objArr);
            if (i10 != 16) {
                throw new IllegalStateException(format);
            }
            throw new IllegalArgumentException(format);
        }
        objArr[0] = "from";
        if (i10 == 16) {
        }
        switch (i10) {
        }
        String format2 = String.format(str, objArr);
        if (i10 != 16) {
        }
    }

    public static Integer d(u uVar, u uVar2) {
        if (uVar == null) {
            a(12);
        }
        if (uVar2 == null) {
            a(13);
        }
        Integer a10 = uVar.a(uVar2);
        if (a10 != null) {
            return a10;
        }
        Integer a11 = uVar2.a(uVar);
        if (a11 != null) {
            return Integer.valueOf(-a11.intValue());
        }
        return null;
    }

    public static DeclarationDescriptorWithVisibility e(ReceiverValue receiverValue, DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility, DeclarationDescriptor declarationDescriptor, boolean z10) {
        DeclarationDescriptorWithVisibility e10;
        if (declarationDescriptorWithVisibility == null) {
            a(8);
        }
        if (declarationDescriptor == null) {
            a(9);
        }
        for (DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility2 = (DeclarationDescriptorWithVisibility) declarationDescriptorWithVisibility.a(); declarationDescriptorWithVisibility2 != null && declarationDescriptorWithVisibility2.g() != f16734f; declarationDescriptorWithVisibility2 = (DeclarationDescriptorWithVisibility) sc.e.q(declarationDescriptorWithVisibility2, DeclarationDescriptorWithVisibility.class)) {
            if (!declarationDescriptorWithVisibility2.g().e(receiverValue, declarationDescriptorWithVisibility2, declarationDescriptor, z10)) {
                return declarationDescriptorWithVisibility2;
            }
        }
        if (!(declarationDescriptorWithVisibility instanceof sb.i0) || (e10 = e(receiverValue, ((sb.i0) declarationDescriptorWithVisibility).u0(), declarationDescriptor, z10)) == null) {
            return null;
        }
        return e10;
    }

    public static boolean f(DeclarationDescriptor declarationDescriptor, DeclarationDescriptor declarationDescriptor2) {
        if (declarationDescriptor == null) {
            a(6);
        }
        if (declarationDescriptor2 == null) {
            a(7);
        }
        b1 j10 = sc.e.j(declarationDescriptor2);
        if (j10 != b1.f16671a) {
            return j10.equals(sc.e.j(declarationDescriptor));
        }
        return false;
    }

    public static boolean g(u uVar) {
        if (uVar == null) {
            a(14);
        }
        return uVar == f16729a || uVar == f16730b;
    }

    public static boolean h(DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility, DeclarationDescriptor declarationDescriptor, boolean z10) {
        if (declarationDescriptorWithVisibility == null) {
            a(2);
        }
        if (declarationDescriptor == null) {
            a(3);
        }
        return e(f16742n, declarationDescriptorWithVisibility, declarationDescriptor, z10) == null;
    }

    private static void i(u uVar) {
        f16745q.put(uVar.b(), uVar);
    }

    public static u j(n1 n1Var) {
        if (n1Var == null) {
            a(15);
        }
        u uVar = f16745q.get(n1Var);
        if (uVar != null) {
            return uVar;
        }
        throw new IllegalArgumentException("Inapplicable visibility: " + n1Var);
    }
}
