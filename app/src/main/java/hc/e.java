package hc;

import gd.IntersectionTypeConstructor;
import gd.TypeConstructor;
import gd.TypeProjection;
import gd.Variance;
import gd.expandedTypeUtils;
import gd.g0;
import gd.s1;
import id.ErrorUtils;
import ma.Unit;
import mb.KotlinBuiltIns;
import mb.functionTypes;
import mb.suspendFunctionTypes;
import oc.FqName;
import oc.SpecialNames;
import pb.CallableDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.ClassifierDescriptor;
import pb.ConstructorDescriptor;
import pb.DeclarationDescriptor;
import pb.PackageFragmentDescriptor;
import pb.PropertyGetterDescriptor;
import pb.TypeAliasDescriptor;
import pb.TypeParameterDescriptor;
import qd.functions;
import sc.inlineClassesUtils;
import sd.StringsJVM;

/* compiled from: descriptorBasedTypeSignatureMapping.kt */
/* loaded from: classes2.dex */
public final class e {
    public static final String a(ClassDescriptor classDescriptor, z<?> zVar) {
        String y4;
        za.k.e(classDescriptor, "klass");
        za.k.e(zVar, "typeMappingConfiguration");
        String a10 = zVar.a(classDescriptor);
        if (a10 != null) {
            return a10;
        }
        DeclarationDescriptor b10 = classDescriptor.b();
        za.k.d(b10, "klass.containingDeclaration");
        String d10 = SpecialNames.b(classDescriptor.getName()).d();
        za.k.d(d10, "safeIdentifier(klass.name).identifier");
        if (b10 instanceof PackageFragmentDescriptor) {
            FqName d11 = ((PackageFragmentDescriptor) b10).d();
            if (d11.d()) {
                return d10;
            }
            StringBuilder sb2 = new StringBuilder();
            String b11 = d11.b();
            za.k.d(b11, "fqName.asString()");
            y4 = StringsJVM.y(b11, '.', '/', false, 4, null);
            sb2.append(y4);
            sb2.append('/');
            sb2.append(d10);
            return sb2.toString();
        }
        ClassDescriptor classDescriptor2 = b10 instanceof ClassDescriptor ? (ClassDescriptor) b10 : null;
        if (classDescriptor2 != null) {
            String e10 = zVar.e(classDescriptor2);
            if (e10 == null) {
                e10 = a(classDescriptor2, zVar);
            }
            return e10 + '$' + d10;
        }
        throw new IllegalArgumentException("Unexpected container: " + b10 + " for " + classDescriptor);
    }

    public static /* synthetic */ String b(ClassDescriptor classDescriptor, z zVar, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            zVar = a0.f12109a;
        }
        return a(classDescriptor, zVar);
    }

    public static final boolean c(CallableDescriptor callableDescriptor) {
        za.k.e(callableDescriptor, "descriptor");
        if (callableDescriptor instanceof ConstructorDescriptor) {
            return true;
        }
        g0 f10 = callableDescriptor.f();
        za.k.b(f10);
        if (KotlinBuiltIns.B0(f10)) {
            g0 f11 = callableDescriptor.f();
            za.k.b(f11);
            if (!s1.l(f11) && !(callableDescriptor instanceof PropertyGetterDescriptor)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v9, types: [T, java.lang.Object] */
    public static final <T> T d(g0 g0Var, n<T> nVar, TypeMappingMode typeMappingMode, z<? extends T> zVar, k<T> kVar, ya.q<? super g0, ? super T, ? super TypeMappingMode, Unit> qVar) {
        T t7;
        g0 g0Var2;
        Object d10;
        za.k.e(g0Var, "kotlinType");
        za.k.e(nVar, "factory");
        za.k.e(typeMappingMode, "mode");
        za.k.e(zVar, "typeMappingConfiguration");
        za.k.e(qVar, "writeGenericType");
        g0 d11 = zVar.d(g0Var);
        if (d11 != null) {
            return (T) d(d11, nVar, typeMappingMode, zVar, kVar, qVar);
        }
        if (functionTypes.q(g0Var)) {
            return (T) d(suspendFunctionTypes.a(g0Var), nVar, typeMappingMode, zVar, kVar, qVar);
        }
        hd.q qVar2 = hd.q.f12241a;
        Object b10 = c0.b(qVar2, g0Var, nVar, typeMappingMode);
        if (b10 != null) {
            ?? r92 = (Object) c0.a(nVar, b10, typeMappingMode.d());
            qVar.g(g0Var, r92, typeMappingMode);
            return r92;
        }
        TypeConstructor W0 = g0Var.W0();
        if (W0 instanceof IntersectionTypeConstructor) {
            IntersectionTypeConstructor intersectionTypeConstructor = (IntersectionTypeConstructor) W0;
            g0 e10 = intersectionTypeConstructor.e();
            if (e10 == null) {
                e10 = zVar.c(intersectionTypeConstructor.q());
            }
            return (T) d(ld.a.w(e10), nVar, typeMappingMode, zVar, kVar, qVar);
        }
        ClassifierDescriptor v7 = W0.v();
        if (v7 != null) {
            if (ErrorUtils.m(v7)) {
                T t10 = (T) nVar.d("error/NonExistentClass");
                zVar.b(g0Var, (ClassDescriptor) v7);
                return t10;
            }
            boolean z10 = v7 instanceof ClassDescriptor;
            if (z10 && KotlinBuiltIns.c0(g0Var)) {
                if (g0Var.U0().size() == 1) {
                    TypeProjection typeProjection = g0Var.U0().get(0);
                    g0 type = typeProjection.getType();
                    za.k.d(type, "memberProjection.type");
                    if (typeProjection.a() == Variance.IN_VARIANCE) {
                        d10 = nVar.d("java/lang/Object");
                    } else {
                        Variance a10 = typeProjection.a();
                        za.k.d(a10, "memberProjection.projectionKind");
                        d10 = d(type, nVar, typeMappingMode.f(a10, true), zVar, kVar, qVar);
                    }
                    return (T) nVar.c('[' + nVar.a(d10));
                }
                throw new UnsupportedOperationException("arrays must have one type argument");
            }
            if (z10) {
                if (inlineClassesUtils.b(v7) && !typeMappingMode.c() && (g0Var2 = (g0) expandedTypeUtils.a(qVar2, g0Var)) != null) {
                    return (T) d(g0Var2, nVar, typeMappingMode.g(), zVar, kVar, qVar);
                }
                if (typeMappingMode.e() && KotlinBuiltIns.k0((ClassDescriptor) v7)) {
                    t7 = (Object) nVar.f();
                } else {
                    ClassDescriptor classDescriptor = (ClassDescriptor) v7;
                    ClassDescriptor T0 = classDescriptor.T0();
                    za.k.d(T0, "descriptor.original");
                    T f10 = zVar.f(T0);
                    if (f10 == null) {
                        if (classDescriptor.getKind() == ClassKind.ENUM_ENTRY) {
                            DeclarationDescriptor b11 = classDescriptor.b();
                            za.k.c(b11, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
                            classDescriptor = (ClassDescriptor) b11;
                        }
                        ClassDescriptor T02 = classDescriptor.T0();
                        za.k.d(T02, "enumClassIfEnumEntry.original");
                        t7 = (Object) nVar.d(a(T02, zVar));
                    } else {
                        t7 = (Object) f10;
                    }
                }
                qVar.g(g0Var, t7, typeMappingMode);
                return t7;
            }
            if (v7 instanceof TypeParameterDescriptor) {
                g0 j10 = ld.a.j((TypeParameterDescriptor) v7);
                if (g0Var.X0()) {
                    j10 = ld.a.u(j10);
                }
                return (T) d(j10, nVar, typeMappingMode, zVar, null, functions.b());
            }
            if ((v7 instanceof TypeAliasDescriptor) && typeMappingMode.b()) {
                return (T) d(((TypeAliasDescriptor) v7).e0(), nVar, typeMappingMode, zVar, kVar, qVar);
            }
            throw new UnsupportedOperationException("Unknown type " + g0Var);
        }
        throw new UnsupportedOperationException("no descriptor for type constructor of " + g0Var);
    }

    public static /* synthetic */ Object e(g0 g0Var, n nVar, TypeMappingMode typeMappingMode, z zVar, k kVar, ya.q qVar, int i10, Object obj) {
        if ((i10 & 32) != 0) {
            qVar = functions.b();
        }
        return d(g0Var, nVar, typeMappingMode, zVar, kVar, qVar);
    }
}
