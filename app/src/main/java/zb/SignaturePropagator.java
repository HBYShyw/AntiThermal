package zb;

import com.oplus.backup.sdk.common.utils.Constants;
import fc.r;
import gd.g0;
import java.util.Collections;
import java.util.List;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;

/* compiled from: SignaturePropagator.java */
/* renamed from: zb.j, reason: use source file name */
/* loaded from: classes2.dex */
public interface SignaturePropagator {

    /* renamed from: a, reason: collision with root package name */
    public static final SignaturePropagator f20411a = new a();

    /* compiled from: SignaturePropagator.java */
    /* renamed from: zb.j$a */
    /* loaded from: classes2.dex */
    static class a implements SignaturePropagator {
        a() {
        }

        private static /* synthetic */ void c(int i10) {
            Object[] objArr = new Object[3];
            switch (i10) {
                case 1:
                    objArr[0] = "owner";
                    break;
                case 2:
                    objArr[0] = "returnType";
                    break;
                case 3:
                    objArr[0] = "valueParameters";
                    break;
                case 4:
                    objArr[0] = "typeParameters";
                    break;
                case 5:
                    objArr[0] = "descriptor";
                    break;
                case 6:
                    objArr[0] = "signatureErrors";
                    break;
                default:
                    objArr[0] = Constants.MessagerConstants.METHOD_KEY;
                    break;
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/load/java/components/SignaturePropagator$1";
            if (i10 == 5 || i10 == 6) {
                objArr[2] = "reportSignatureErrors";
            } else {
                objArr[2] = "resolvePropagatedSignature";
            }
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        @Override // zb.SignaturePropagator
        public void a(CallableMemberDescriptor callableMemberDescriptor, List<String> list) {
            if (callableMemberDescriptor == null) {
                c(5);
            }
            if (list == null) {
                c(6);
            }
            throw new UnsupportedOperationException("Should not be called");
        }

        @Override // zb.SignaturePropagator
        public b b(r rVar, ClassDescriptor classDescriptor, g0 g0Var, g0 g0Var2, List<ValueParameterDescriptor> list, List<TypeParameterDescriptor> list2) {
            if (rVar == null) {
                c(0);
            }
            if (classDescriptor == null) {
                c(1);
            }
            if (g0Var == null) {
                c(2);
            }
            if (list == null) {
                c(3);
            }
            if (list2 == null) {
                c(4);
            }
            return new b(g0Var, g0Var2, list, list2, Collections.emptyList(), false);
        }
    }

    /* compiled from: SignaturePropagator.java */
    /* renamed from: zb.j$b */
    /* loaded from: classes2.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        private final g0 f20412a;

        /* renamed from: b, reason: collision with root package name */
        private final g0 f20413b;

        /* renamed from: c, reason: collision with root package name */
        private final List<ValueParameterDescriptor> f20414c;

        /* renamed from: d, reason: collision with root package name */
        private final List<TypeParameterDescriptor> f20415d;

        /* renamed from: e, reason: collision with root package name */
        private final List<String> f20416e;

        /* renamed from: f, reason: collision with root package name */
        private final boolean f20417f;

        public b(g0 g0Var, g0 g0Var2, List<ValueParameterDescriptor> list, List<TypeParameterDescriptor> list2, List<String> list3, boolean z10) {
            if (g0Var == null) {
                a(0);
            }
            if (list == null) {
                a(1);
            }
            if (list2 == null) {
                a(2);
            }
            if (list3 == null) {
                a(3);
            }
            this.f20412a = g0Var;
            this.f20413b = g0Var2;
            this.f20414c = list;
            this.f20415d = list2;
            this.f20416e = list3;
            this.f20417f = z10;
        }

        private static /* synthetic */ void a(int i10) {
            String str = (i10 == 4 || i10 == 5 || i10 == 6 || i10 == 7) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
            Object[] objArr = new Object[(i10 == 4 || i10 == 5 || i10 == 6 || i10 == 7) ? 2 : 3];
            switch (i10) {
                case 1:
                    objArr[0] = "valueParameters";
                    break;
                case 2:
                    objArr[0] = "typeParameters";
                    break;
                case 3:
                    objArr[0] = "signatureErrors";
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                    objArr[0] = "kotlin/reflect/jvm/internal/impl/load/java/components/SignaturePropagator$PropagatedSignature";
                    break;
                default:
                    objArr[0] = "returnType";
                    break;
            }
            if (i10 == 4) {
                objArr[1] = "getReturnType";
            } else if (i10 == 5) {
                objArr[1] = "getValueParameters";
            } else if (i10 == 6) {
                objArr[1] = "getTypeParameters";
            } else if (i10 != 7) {
                objArr[1] = "kotlin/reflect/jvm/internal/impl/load/java/components/SignaturePropagator$PropagatedSignature";
            } else {
                objArr[1] = "getErrors";
            }
            if (i10 != 4 && i10 != 5 && i10 != 6 && i10 != 7) {
                objArr[2] = "<init>";
            }
            String format = String.format(str, objArr);
            if (i10 != 4 && i10 != 5 && i10 != 6 && i10 != 7) {
                throw new IllegalArgumentException(format);
            }
            throw new IllegalStateException(format);
        }

        public List<String> b() {
            List<String> list = this.f20416e;
            if (list == null) {
                a(7);
            }
            return list;
        }

        public g0 c() {
            return this.f20413b;
        }

        public g0 d() {
            g0 g0Var = this.f20412a;
            if (g0Var == null) {
                a(4);
            }
            return g0Var;
        }

        public List<TypeParameterDescriptor> e() {
            List<TypeParameterDescriptor> list = this.f20415d;
            if (list == null) {
                a(6);
            }
            return list;
        }

        public List<ValueParameterDescriptor> f() {
            List<ValueParameterDescriptor> list = this.f20414c;
            if (list == null) {
                a(5);
            }
            return list;
        }

        public boolean g() {
            return this.f20417f;
        }
    }

    void a(CallableMemberDescriptor callableMemberDescriptor, List<String> list);

    b b(r rVar, ClassDescriptor classDescriptor, g0 g0Var, g0 g0Var2, List<ValueParameterDescriptor> list, List<TypeParameterDescriptor> list2);
}
