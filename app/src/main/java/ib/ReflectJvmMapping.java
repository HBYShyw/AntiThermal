package ib;

import gb.KFunction;
import gb.KType;
import gb.i;
import gb.l;
import gb.u;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import jb.KCallableImpl;
import jb.KTypeImpl;
import jb.b0;
import jb.o0;
import kb.e;
import za.k;

/* compiled from: ReflectJvmMapping.kt */
/* renamed from: ib.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReflectJvmMapping {
    public static final <T> Constructor<T> a(KFunction<? extends T> kFunction) {
        e<?> F;
        k.e(kFunction, "<this>");
        KCallableImpl<?> b10 = o0.b(kFunction);
        Object b11 = (b10 == null || (F = b10.F()) == null) ? null : F.b();
        if (b11 instanceof Constructor) {
            return (Constructor) b11;
        }
        return null;
    }

    public static final Field b(l<?> lVar) {
        k.e(lVar, "<this>");
        b0<?> d10 = o0.d(lVar);
        if (d10 != null) {
            return d10.Q();
        }
        return null;
    }

    public static final Method c(l<?> lVar) {
        k.e(lVar, "<this>");
        return d(lVar.h());
    }

    public static final Method d(KFunction<?> kFunction) {
        e<?> F;
        k.e(kFunction, "<this>");
        KCallableImpl<?> b10 = o0.b(kFunction);
        Object b11 = (b10 == null || (F = b10.F()) == null) ? null : F.b();
        if (b11 instanceof Method) {
            return (Method) b11;
        }
        return null;
    }

    public static final Method e(i<?> iVar) {
        k.e(iVar, "<this>");
        return d(iVar.k());
    }

    public static final Type f(KType kType) {
        k.e(kType, "<this>");
        Type o10 = ((KTypeImpl) kType).o();
        return o10 == null ? u.f(kType) : o10;
    }
}
