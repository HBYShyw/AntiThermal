package yb;

import ad.ReceiverValue;
import java.util.HashMap;
import java.util.Map;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorWithVisibility;
import pb.DescriptorVisibilities;
import pb.PackageFragmentDescriptor;
import pb.n1;

/* compiled from: JavaDescriptorVisibilities.java */
/* renamed from: yb.s, reason: use source file name */
/* loaded from: classes2.dex */
public class JavaDescriptorVisibilities {

    /* renamed from: a, reason: collision with root package name */
    public static final pb.u f20131a;

    /* renamed from: b, reason: collision with root package name */
    public static final pb.u f20132b;

    /* renamed from: c, reason: collision with root package name */
    public static final pb.u f20133c;

    /* renamed from: d, reason: collision with root package name */
    private static final Map<n1, pb.u> f20134d;

    /* compiled from: JavaDescriptorVisibilities.java */
    /* renamed from: yb.s$a */
    /* loaded from: classes2.dex */
    static class a extends pb.r {
        a(n1 n1Var) {
            super(n1Var);
        }

        private static /* synthetic */ void g(int i10) {
            Object[] objArr = new Object[3];
            if (i10 != 1) {
                objArr[0] = "what";
            } else {
                objArr[0] = "from";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/load/java/JavaDescriptorVisibilities$1";
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
            return JavaDescriptorVisibilities.d(declarationDescriptorWithVisibility, declarationDescriptor);
        }
    }

    /* compiled from: JavaDescriptorVisibilities.java */
    /* renamed from: yb.s$b */
    /* loaded from: classes2.dex */
    static class b extends pb.r {
        b(n1 n1Var) {
            super(n1Var);
        }

        private static /* synthetic */ void g(int i10) {
            Object[] objArr = new Object[3];
            if (i10 != 1) {
                objArr[0] = "what";
            } else {
                objArr[0] = "from";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/load/java/JavaDescriptorVisibilities$2";
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
            return JavaDescriptorVisibilities.e(receiverValue, declarationDescriptorWithVisibility, declarationDescriptor);
        }
    }

    /* compiled from: JavaDescriptorVisibilities.java */
    /* renamed from: yb.s$c */
    /* loaded from: classes2.dex */
    static class c extends pb.r {
        c(n1 n1Var) {
            super(n1Var);
        }

        private static /* synthetic */ void g(int i10) {
            Object[] objArr = new Object[3];
            if (i10 != 1) {
                objArr[0] = "what";
            } else {
                objArr[0] = "from";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/load/java/JavaDescriptorVisibilities$3";
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
            return JavaDescriptorVisibilities.e(receiverValue, declarationDescriptorWithVisibility, declarationDescriptor);
        }
    }

    static {
        a aVar = new a(tb.a.f18704c);
        f20131a = aVar;
        b bVar = new b(tb.c.f18706c);
        f20132b = bVar;
        c cVar = new c(tb.b.f18705c);
        f20133c = cVar;
        f20134d = new HashMap();
        f(aVar);
        f(bVar);
        f(cVar);
    }

    private static /* synthetic */ void a(int i10) {
        String str = (i10 == 5 || i10 == 6) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 5 || i10 == 6) ? 2 : 3];
        switch (i10) {
            case 1:
                objArr[0] = "from";
                break;
            case 2:
                objArr[0] = "first";
                break;
            case 3:
                objArr[0] = "second";
                break;
            case 4:
                objArr[0] = "visibility";
                break;
            case 5:
            case 6:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/load/java/JavaDescriptorVisibilities";
                break;
            default:
                objArr[0] = "what";
                break;
        }
        if (i10 == 5 || i10 == 6) {
            objArr[1] = "toDescriptorVisibility";
        } else {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/load/java/JavaDescriptorVisibilities";
        }
        if (i10 == 2 || i10 == 3) {
            objArr[2] = "areInSamePackage";
        } else if (i10 == 4) {
            objArr[2] = "toDescriptorVisibility";
        } else if (i10 != 5 && i10 != 6) {
            objArr[2] = "isVisibleForProtectedAndPackage";
        }
        String format = String.format(str, objArr);
        if (i10 != 5 && i10 != 6) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean d(DeclarationDescriptor declarationDescriptor, DeclarationDescriptor declarationDescriptor2) {
        if (declarationDescriptor == null) {
            a(2);
        }
        if (declarationDescriptor2 == null) {
            a(3);
        }
        PackageFragmentDescriptor packageFragmentDescriptor = (PackageFragmentDescriptor) sc.e.r(declarationDescriptor, PackageFragmentDescriptor.class, false);
        PackageFragmentDescriptor packageFragmentDescriptor2 = (PackageFragmentDescriptor) sc.e.r(declarationDescriptor2, PackageFragmentDescriptor.class, false);
        return (packageFragmentDescriptor2 == null || packageFragmentDescriptor == null || !packageFragmentDescriptor.d().equals(packageFragmentDescriptor2.d())) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean e(ReceiverValue receiverValue, DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility, DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptorWithVisibility == null) {
            a(0);
        }
        if (declarationDescriptor == null) {
            a(1);
        }
        if (d(sc.e.M(declarationDescriptorWithVisibility), declarationDescriptor)) {
            return true;
        }
        return DescriptorVisibilities.f16731c.e(receiverValue, declarationDescriptorWithVisibility, declarationDescriptor, false);
    }

    private static void f(pb.u uVar) {
        f20134d.put(uVar.b(), uVar);
    }

    public static pb.u g(n1 n1Var) {
        if (n1Var == null) {
            a(4);
        }
        pb.u uVar = f20134d.get(n1Var);
        if (uVar != null) {
            return uVar;
        }
        pb.u j10 = DescriptorVisibilities.j(n1Var);
        if (j10 == null) {
            a(5);
        }
        return j10;
    }
}
