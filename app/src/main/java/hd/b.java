package hd;

import gd.IntersectionTypeConstructor;
import gd.TypeConstructor;
import gd.TypeProjection;
import gd.TypeSubstitutor;
import gd.TypeSystemCommonBackendContext;
import gd.Variance;
import gd.a0;
import gd.f1;
import gd.g0;
import gd.h0;
import gd.h1;
import gd.i0;
import gd.l0;
import gd.n0;
import gd.o0;
import gd.r0;
import gd.s1;
import gd.v1;
import java.util.Collection;
import java.util.List;
import mb.KotlinBuiltIns;
import mb.PrimitiveType;
import mb.StandardNames;
import oc.FqName;
import oc.FqNameUnsafe;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.ClassifierDescriptor;
import pb.InlineClassRepresentation;
import pb.ModalityUtils;
import pb.TypeAliasDescriptor;
import pb.TypeParameterDescriptor;
import sc.inlineClassesUtils;
import uc.IntegerLiteralTypeConstructor;
import za.Reflection;

/* compiled from: ClassicTypeSystemContext.kt */
/* loaded from: classes2.dex */
public interface b extends TypeSystemCommonBackendContext, kd.r {

    /* compiled from: ClassicTypeSystemContext.kt */
    /* loaded from: classes2.dex */
    public static final class a {

        /* compiled from: ClassicTypeSystemContext.kt */
        /* renamed from: hd.b$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0048a extends f1.c.a {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ b f12211a;

            /* renamed from: b, reason: collision with root package name */
            final /* synthetic */ TypeSubstitutor f12212b;

            C0048a(b bVar, TypeSubstitutor typeSubstitutor) {
                this.f12211a = bVar;
                this.f12212b = typeSubstitutor;
            }

            @Override // gd.f1.c
            public kd.k a(f1 f1Var, kd.i iVar) {
                za.k.e(f1Var, "state");
                za.k.e(iVar, "type");
                b bVar = this.f12211a;
                TypeSubstitutor typeSubstitutor = this.f12212b;
                kd.i v02 = bVar.v0(iVar);
                za.k.c(v02, "null cannot be cast to non-null type org.jetbrains.kotlin.types.KotlinType");
                g0 n10 = typeSubstitutor.n((g0) v02, Variance.INVARIANT);
                za.k.d(n10, "substitutor.safeSubstitu…VARIANT\n                )");
                kd.k c10 = bVar.c(n10);
                za.k.b(c10);
                return c10;
            }
        }

        public static kd.u A(b bVar, kd.m mVar) {
            za.k.e(mVar, "$receiver");
            if (mVar instanceof TypeProjection) {
                Variance a10 = ((TypeProjection) mVar).a();
                za.k.d(a10, "this.projectionKind");
                return kd.q.a(a10);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + mVar + ", " + Reflection.b(mVar.getClass())).toString());
        }

        public static kd.u B(b bVar, kd.o oVar) {
            za.k.e(oVar, "$receiver");
            if (oVar instanceof TypeParameterDescriptor) {
                Variance s7 = ((TypeParameterDescriptor) oVar).s();
                za.k.d(s7, "this.variance");
                return kd.q.a(s7);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + oVar + ", " + Reflection.b(oVar.getClass())).toString());
        }

        public static boolean C(b bVar, kd.i iVar, FqName fqName) {
            za.k.e(iVar, "$receiver");
            za.k.e(fqName, "fqName");
            if (iVar instanceof g0) {
                return ((g0) iVar).i().a(fqName);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + iVar + ", " + Reflection.b(iVar.getClass())).toString());
        }

        public static boolean D(b bVar, kd.o oVar, kd.n nVar) {
            za.k.e(oVar, "$receiver");
            if (oVar instanceof TypeParameterDescriptor) {
                if (nVar == null ? true : nVar instanceof TypeConstructor) {
                    return ld.a.m((TypeParameterDescriptor) oVar, (TypeConstructor) nVar, null, 4, null);
                }
                throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + oVar + ", " + Reflection.b(oVar.getClass())).toString());
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + oVar + ", " + Reflection.b(oVar.getClass())).toString());
        }

        public static boolean E(b bVar, kd.k kVar, kd.k kVar2) {
            za.k.e(kVar, "a");
            za.k.e(kVar2, "b");
            if (kVar instanceof o0) {
                if (kVar2 instanceof o0) {
                    return ((o0) kVar).U0() == ((o0) kVar2).U0();
                }
                throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + kVar2 + ", " + Reflection.b(kVar2.getClass())).toString());
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + kVar + ", " + Reflection.b(kVar.getClass())).toString());
        }

        public static kd.i F(b bVar, List<? extends kd.i> list) {
            za.k.e(list, "types");
            return d.a(list);
        }

        public static boolean G(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                return KotlinBuiltIns.v0((TypeConstructor) nVar, StandardNames.a.f15291b);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static boolean H(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                return ((TypeConstructor) nVar).v() instanceof ClassDescriptor;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static boolean I(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                ClassifierDescriptor v7 = ((TypeConstructor) nVar).v();
                ClassDescriptor classDescriptor = v7 instanceof ClassDescriptor ? (ClassDescriptor) v7 : null;
                return (classDescriptor == null || !ModalityUtils.a(classDescriptor) || classDescriptor.getKind() == ClassKind.ENUM_ENTRY || classDescriptor.getKind() == ClassKind.ANNOTATION_CLASS) ? false : true;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static boolean J(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                return ((TypeConstructor) nVar).w();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static boolean K(b bVar, kd.i iVar) {
            za.k.e(iVar, "$receiver");
            if (iVar instanceof g0) {
                return i0.a((g0) iVar);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + iVar + ", " + Reflection.b(iVar.getClass())).toString());
        }

        public static boolean L(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                ClassifierDescriptor v7 = ((TypeConstructor) nVar).v();
                ClassDescriptor classDescriptor = v7 instanceof ClassDescriptor ? (ClassDescriptor) v7 : null;
                return (classDescriptor != null ? classDescriptor.G0() : null) instanceof InlineClassRepresentation;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static boolean M(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                return nVar instanceof IntegerLiteralTypeConstructor;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static boolean N(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                return nVar instanceof IntersectionTypeConstructor;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static boolean O(b bVar, kd.k kVar) {
            za.k.e(kVar, "$receiver");
            if (kVar instanceof o0) {
                return ((o0) kVar).X0();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + kVar + ", " + Reflection.b(kVar.getClass())).toString());
        }

        public static boolean P(b bVar, kd.i iVar) {
            za.k.e(iVar, "$receiver");
            return iVar instanceof l0;
        }

        public static boolean Q(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                return KotlinBuiltIns.v0((TypeConstructor) nVar, StandardNames.a.f15293c);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static boolean R(b bVar, kd.i iVar) {
            za.k.e(iVar, "$receiver");
            if (iVar instanceof g0) {
                return s1.l((g0) iVar);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + iVar + ", " + Reflection.b(iVar.getClass())).toString());
        }

        public static boolean S(b bVar, kd.d dVar) {
            za.k.e(dVar, "$receiver");
            return dVar instanceof tc.a;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static boolean T(b bVar, kd.k kVar) {
            za.k.e(kVar, "$receiver");
            if (kVar instanceof g0) {
                return KotlinBuiltIns.r0((g0) kVar);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + kVar + ", " + Reflection.b(kVar.getClass())).toString());
        }

        public static boolean U(b bVar, kd.d dVar) {
            za.k.e(dVar, "$receiver");
            if (dVar instanceof i) {
                return ((i) dVar).i1();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + dVar + ", " + Reflection.b(dVar.getClass())).toString());
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static boolean V(b bVar, kd.k kVar) {
            za.k.e(kVar, "$receiver");
            if (kVar instanceof o0) {
                if (!i0.a((g0) kVar)) {
                    o0 o0Var = (o0) kVar;
                    if (!(o0Var.W0().v() instanceof TypeAliasDescriptor) && (o0Var.W0().v() != null || (kVar instanceof tc.a) || (kVar instanceof i) || (kVar instanceof gd.p) || (o0Var.W0() instanceof IntegerLiteralTypeConstructor) || W(bVar, kVar))) {
                        return true;
                    }
                }
                return false;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + kVar + ", " + Reflection.b(kVar.getClass())).toString());
        }

        private static boolean W(b bVar, kd.k kVar) {
            return (kVar instanceof r0) && bVar.f(((r0) kVar).O0());
        }

        public static boolean X(b bVar, kd.m mVar) {
            za.k.e(mVar, "$receiver");
            if (mVar instanceof TypeProjection) {
                return ((TypeProjection) mVar).b();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + mVar + ", " + Reflection.b(mVar.getClass())).toString());
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static boolean Y(b bVar, kd.k kVar) {
            za.k.e(kVar, "$receiver");
            if (kVar instanceof o0) {
                return ld.a.n((g0) kVar);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + kVar + ", " + Reflection.b(kVar.getClass())).toString());
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static boolean Z(b bVar, kd.k kVar) {
            za.k.e(kVar, "$receiver");
            if (kVar instanceof o0) {
                return ld.a.o((g0) kVar);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + kVar + ", " + Reflection.b(kVar.getClass())).toString());
        }

        public static boolean a(b bVar, kd.n nVar, kd.n nVar2) {
            za.k.e(nVar, "c1");
            za.k.e(nVar2, "c2");
            if (nVar instanceof TypeConstructor) {
                if (nVar2 instanceof TypeConstructor) {
                    return za.k.a(nVar, nVar2);
                }
                throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar2 + ", " + Reflection.b(nVar2.getClass())).toString());
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static boolean a0(b bVar, kd.i iVar) {
            za.k.e(iVar, "$receiver");
            return (iVar instanceof v1) && (((v1) iVar).W0() instanceof n);
        }

        public static int b(b bVar, kd.i iVar) {
            za.k.e(iVar, "$receiver");
            if (iVar instanceof g0) {
                return ((g0) iVar).U0().size();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + iVar + ", " + Reflection.b(iVar.getClass())).toString());
        }

        public static boolean b0(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                ClassifierDescriptor v7 = ((TypeConstructor) nVar).v();
                return v7 != null && KotlinBuiltIns.A0(v7);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static kd.l c(b bVar, kd.k kVar) {
            za.k.e(kVar, "$receiver");
            if (kVar instanceof o0) {
                return (kd.l) kVar;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + kVar + ", " + Reflection.b(kVar.getClass())).toString());
        }

        public static kd.k c0(b bVar, kd.g gVar) {
            za.k.e(gVar, "$receiver");
            if (gVar instanceof a0) {
                return ((a0) gVar).e1();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + gVar + ", " + Reflection.b(gVar.getClass())).toString());
        }

        public static kd.d d(b bVar, kd.k kVar) {
            za.k.e(kVar, "$receiver");
            if (kVar instanceof o0) {
                if (kVar instanceof r0) {
                    return bVar.g(((r0) kVar).O0());
                }
                if (kVar instanceof i) {
                    return (i) kVar;
                }
                return null;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + kVar + ", " + Reflection.b(kVar.getClass())).toString());
        }

        public static kd.i d0(b bVar, kd.d dVar) {
            za.k.e(dVar, "$receiver");
            if (dVar instanceof i) {
                return ((i) dVar).h1();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + dVar + ", " + Reflection.b(dVar.getClass())).toString());
        }

        public static kd.e e(b bVar, kd.k kVar) {
            za.k.e(kVar, "$receiver");
            if (kVar instanceof o0) {
                if (kVar instanceof gd.p) {
                    return (gd.p) kVar;
                }
                return null;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + kVar + ", " + Reflection.b(kVar.getClass())).toString());
        }

        public static kd.i e0(b bVar, kd.i iVar) {
            v1 b10;
            za.k.e(iVar, "$receiver");
            if (iVar instanceof v1) {
                b10 = c.b((v1) iVar);
                return b10;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + iVar + ", " + Reflection.b(iVar.getClass())).toString());
        }

        public static kd.f f(b bVar, kd.g gVar) {
            za.k.e(gVar, "$receiver");
            if (gVar instanceof a0) {
                if (gVar instanceof gd.v) {
                    return (gd.v) gVar;
                }
                return null;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + gVar + ", " + Reflection.b(gVar.getClass())).toString());
        }

        public static f1 f0(b bVar, boolean z10, boolean z11) {
            return ClassicTypeCheckerState.b(z10, z11, bVar, null, null, 24, null);
        }

        public static kd.g g(b bVar, kd.i iVar) {
            za.k.e(iVar, "$receiver");
            if (iVar instanceof g0) {
                v1 Z0 = ((g0) iVar).Z0();
                if (Z0 instanceof a0) {
                    return (a0) Z0;
                }
                return null;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + iVar + ", " + Reflection.b(iVar.getClass())).toString());
        }

        public static kd.k g0(b bVar, kd.e eVar) {
            za.k.e(eVar, "$receiver");
            if (eVar instanceof gd.p) {
                return ((gd.p) eVar).i1();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + eVar + ", " + Reflection.b(eVar.getClass())).toString());
        }

        public static kd.j h(b bVar, kd.g gVar) {
            za.k.e(gVar, "$receiver");
            if (gVar instanceof a0) {
                if (gVar instanceof n0) {
                    return (n0) gVar;
                }
                return null;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + gVar + ", " + Reflection.b(gVar.getClass())).toString());
        }

        public static int h0(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                return ((TypeConstructor) nVar).getParameters().size();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static kd.k i(b bVar, kd.i iVar) {
            za.k.e(iVar, "$receiver");
            if (iVar instanceof g0) {
                v1 Z0 = ((g0) iVar).Z0();
                if (Z0 instanceof o0) {
                    return (o0) Z0;
                }
                return null;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + iVar + ", " + Reflection.b(iVar.getClass())).toString());
        }

        public static Collection<kd.i> i0(b bVar, kd.k kVar) {
            za.k.e(kVar, "$receiver");
            kd.n b10 = bVar.b(kVar);
            if (b10 instanceof IntegerLiteralTypeConstructor) {
                return ((IntegerLiteralTypeConstructor) b10).g();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + kVar + ", " + Reflection.b(kVar.getClass())).toString());
        }

        public static kd.m j(b bVar, kd.i iVar) {
            za.k.e(iVar, "$receiver");
            if (iVar instanceof g0) {
                return ld.a.a((g0) iVar);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + iVar + ", " + Reflection.b(iVar.getClass())).toString());
        }

        public static kd.m j0(b bVar, kd.c cVar) {
            za.k.e(cVar, "$receiver");
            if (cVar instanceof j) {
                return ((j) cVar).b();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + cVar + ", " + Reflection.b(cVar.getClass())).toString());
        }

        public static kd.k k(b bVar, kd.k kVar, kd.b bVar2) {
            za.k.e(kVar, "type");
            za.k.e(bVar2, "status");
            if (kVar instanceof o0) {
                return k.b((o0) kVar, bVar2);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + kVar + ", " + Reflection.b(kVar.getClass())).toString());
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static f1.c k0(b bVar, kd.k kVar) {
            za.k.e(kVar, "type");
            if (kVar instanceof o0) {
                return new C0048a(bVar, h1.f11827c.a((g0) kVar).c());
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + kVar + ", " + Reflection.b(kVar.getClass())).toString());
        }

        public static kd.b l(b bVar, kd.d dVar) {
            za.k.e(dVar, "$receiver");
            if (dVar instanceof i) {
                return ((i) dVar).f1();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + dVar + ", " + Reflection.b(dVar.getClass())).toString());
        }

        public static Collection<kd.i> l0(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                Collection<g0> q10 = ((TypeConstructor) nVar).q();
                za.k.d(q10, "this.supertypes");
                return q10;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static kd.i m(b bVar, kd.k kVar, kd.k kVar2) {
            za.k.e(kVar, "lowerBound");
            za.k.e(kVar2, "upperBound");
            if (kVar instanceof o0) {
                if (kVar2 instanceof o0) {
                    return h0.d((o0) kVar, (o0) kVar2);
                }
                throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + bVar + ", " + Reflection.b(bVar.getClass())).toString());
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + bVar + ", " + Reflection.b(bVar.getClass())).toString());
        }

        public static kd.c m0(b bVar, kd.d dVar) {
            za.k.e(dVar, "$receiver");
            if (dVar instanceof i) {
                return ((i) dVar).W0();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + dVar + ", " + Reflection.b(dVar.getClass())).toString());
        }

        public static kd.m n(b bVar, kd.i iVar, int i10) {
            za.k.e(iVar, "$receiver");
            if (iVar instanceof g0) {
                return ((g0) iVar).U0().get(i10);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + iVar + ", " + Reflection.b(iVar.getClass())).toString());
        }

        public static kd.n n0(b bVar, kd.k kVar) {
            za.k.e(kVar, "$receiver");
            if (kVar instanceof o0) {
                return ((o0) kVar).W0();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + kVar + ", " + Reflection.b(kVar.getClass())).toString());
        }

        public static List<kd.m> o(b bVar, kd.i iVar) {
            za.k.e(iVar, "$receiver");
            if (iVar instanceof g0) {
                return ((g0) iVar).U0();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + iVar + ", " + Reflection.b(iVar.getClass())).toString());
        }

        public static kd.k o0(b bVar, kd.g gVar) {
            za.k.e(gVar, "$receiver");
            if (gVar instanceof a0) {
                return ((a0) gVar).f1();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + gVar + ", " + Reflection.b(gVar.getClass())).toString());
        }

        public static FqNameUnsafe p(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                ClassifierDescriptor v7 = ((TypeConstructor) nVar).v();
                za.k.c(v7, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
                return wc.c.m((ClassDescriptor) v7);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static kd.i p0(b bVar, kd.i iVar, boolean z10) {
            za.k.e(iVar, "$receiver");
            if (iVar instanceof kd.k) {
                return bVar.e((kd.k) iVar, z10);
            }
            if (!(iVar instanceof kd.g)) {
                throw new IllegalStateException("sealed".toString());
            }
            kd.g gVar = (kd.g) iVar;
            return bVar.M(bVar.e(bVar.d(gVar), z10), bVar.e(bVar.a(gVar), z10));
        }

        public static kd.o q(b bVar, kd.n nVar, int i10) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                TypeParameterDescriptor typeParameterDescriptor = ((TypeConstructor) nVar).getParameters().get(i10);
                za.k.d(typeParameterDescriptor, "this.parameters[index]");
                return typeParameterDescriptor;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static kd.k q0(b bVar, kd.k kVar, boolean z10) {
            za.k.e(kVar, "$receiver");
            if (kVar instanceof o0) {
                return ((o0) kVar).a1(z10);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + kVar + ", " + Reflection.b(kVar.getClass())).toString());
        }

        public static List<kd.o> r(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                List<TypeParameterDescriptor> parameters = ((TypeConstructor) nVar).getParameters();
                za.k.d(parameters, "this.parameters");
                return parameters;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static PrimitiveType s(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                ClassifierDescriptor v7 = ((TypeConstructor) nVar).v();
                za.k.c(v7, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
                return KotlinBuiltIns.P((ClassDescriptor) v7);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static PrimitiveType t(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                ClassifierDescriptor v7 = ((TypeConstructor) nVar).v();
                za.k.c(v7, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
                return KotlinBuiltIns.S((ClassDescriptor) v7);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static kd.i u(b bVar, kd.o oVar) {
            za.k.e(oVar, "$receiver");
            if (oVar instanceof TypeParameterDescriptor) {
                return ld.a.j((TypeParameterDescriptor) oVar);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + oVar + ", " + Reflection.b(oVar.getClass())).toString());
        }

        public static kd.i v(b bVar, kd.m mVar) {
            za.k.e(mVar, "$receiver");
            if (mVar instanceof TypeProjection) {
                return ((TypeProjection) mVar).getType().Z0();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + mVar + ", " + Reflection.b(mVar.getClass())).toString());
        }

        public static kd.o w(b bVar, kd.t tVar) {
            za.k.e(tVar, "$receiver");
            if (tVar instanceof n) {
                return ((n) tVar).a();
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + tVar + ", " + Reflection.b(tVar.getClass())).toString());
        }

        public static kd.o x(b bVar, kd.n nVar) {
            za.k.e(nVar, "$receiver");
            if (nVar instanceof TypeConstructor) {
                ClassifierDescriptor v7 = ((TypeConstructor) nVar).v();
                if (v7 instanceof TypeParameterDescriptor) {
                    return (TypeParameterDescriptor) v7;
                }
                return null;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + nVar + ", " + Reflection.b(nVar.getClass())).toString());
        }

        public static kd.i y(b bVar, kd.i iVar) {
            za.k.e(iVar, "$receiver");
            if (iVar instanceof g0) {
                return inlineClassesUtils.e((g0) iVar);
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + iVar + ", " + Reflection.b(iVar.getClass())).toString());
        }

        public static List<kd.i> z(b bVar, kd.o oVar) {
            za.k.e(oVar, "$receiver");
            if (oVar instanceof TypeParameterDescriptor) {
                List<g0> upperBounds = ((TypeParameterDescriptor) oVar).getUpperBounds();
                za.k.d(upperBounds, "this.upperBounds");
                return upperBounds;
            }
            throw new IllegalArgumentException(("ClassicTypeSystemContext couldn't handle: " + oVar + ", " + Reflection.b(oVar.getClass())).toString());
        }
    }

    kd.i M(kd.k kVar, kd.k kVar2);

    @Override // kd.p
    kd.k a(kd.g gVar);

    @Override // kd.p
    kd.n b(kd.k kVar);

    @Override // kd.p
    kd.k c(kd.i iVar);

    @Override // kd.p
    kd.k d(kd.g gVar);

    @Override // kd.p
    kd.k e(kd.k kVar, boolean z10);

    @Override // kd.p
    boolean f(kd.k kVar);

    @Override // kd.p
    kd.d g(kd.k kVar);
}
