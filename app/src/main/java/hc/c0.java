package hc;

import gc.typeEnhancementUtils;
import gd.TypeSystemCommonBackendContext;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import mb.PrimitiveType;
import ob.JavaToKotlinClassMap;
import oc.ClassId;
import oc.FqNameUnsafe;
import xc.JvmClassName;
import xc.JvmPrimitiveType;

/* compiled from: typeSignatureMapping.kt */
/* loaded from: classes2.dex */
public final class c0 {
    public static final <T> T a(n<T> nVar, T t7, boolean z10) {
        za.k.e(nVar, "<this>");
        za.k.e(t7, "possiblyPrimitiveType");
        return z10 ? nVar.e(t7) : t7;
    }

    public static final <T> T b(TypeSystemCommonBackendContext typeSystemCommonBackendContext, kd.i iVar, n<T> nVar, TypeMappingMode typeMappingMode) {
        za.k.e(typeSystemCommonBackendContext, "<this>");
        za.k.e(iVar, "type");
        za.k.e(nVar, "typeFactory");
        za.k.e(typeMappingMode, "mode");
        kd.n F = typeSystemCommonBackendContext.F(iVar);
        if (!typeSystemCommonBackendContext.e0(F)) {
            return null;
        }
        PrimitiveType i10 = typeSystemCommonBackendContext.i(F);
        boolean z10 = true;
        if (i10 != null) {
            T b10 = nVar.b(i10);
            if (!typeSystemCommonBackendContext.h0(iVar) && !typeEnhancementUtils.c(typeSystemCommonBackendContext, iVar)) {
                z10 = false;
            }
            return (T) a(nVar, b10, z10);
        }
        PrimitiveType o10 = typeSystemCommonBackendContext.o(F);
        if (o10 != null) {
            return nVar.c('[' + JvmPrimitiveType.c(o10).d());
        }
        if (typeSystemCommonBackendContext.D(F)) {
            FqNameUnsafe g02 = typeSystemCommonBackendContext.g0(F);
            ClassId n10 = g02 != null ? JavaToKotlinClassMap.f16339a.n(g02) : null;
            if (n10 != null) {
                if (!typeMappingMode.a()) {
                    List<JavaToKotlinClassMap.a> i11 = JavaToKotlinClassMap.f16339a.i();
                    if (!(i11 instanceof Collection) || !i11.isEmpty()) {
                        Iterator<T> it = i11.iterator();
                        while (it.hasNext()) {
                            if (za.k.a(((JavaToKotlinClassMap.a) it.next()).d(), n10)) {
                                break;
                            }
                        }
                    }
                    z10 = false;
                    if (z10) {
                        return null;
                    }
                }
                String f10 = JvmClassName.b(n10).f();
                za.k.d(f10, "byClassId(classId).internalName");
                return nVar.d(f10);
            }
        }
        return null;
    }
}
