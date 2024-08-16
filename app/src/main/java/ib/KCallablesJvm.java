package ib;

import gb.KCallable;
import gb.KFunction;
import gb.i;
import gb.l;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import jb.KCallableImpl;
import jb.o0;
import kb.e;
import za.k;

/* compiled from: KCallablesJvm.kt */
/* renamed from: ib.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class KCallablesJvm {
    public static final boolean a(KCallable<?> kCallable) {
        e<?> H;
        k.e(kCallable, "<this>");
        if (kCallable instanceof i) {
            l lVar = (l) kCallable;
            Field b10 = ReflectJvmMapping.b(lVar);
            if (!(b10 != null ? b10.isAccessible() : true)) {
                return false;
            }
            Method c10 = ReflectJvmMapping.c(lVar);
            if (!(c10 != null ? c10.isAccessible() : true)) {
                return false;
            }
            Method e10 = ReflectJvmMapping.e((i) kCallable);
            if (!(e10 != null ? e10.isAccessible() : true)) {
                return false;
            }
        } else if (kCallable instanceof l) {
            l lVar2 = (l) kCallable;
            Field b11 = ReflectJvmMapping.b(lVar2);
            if (!(b11 != null ? b11.isAccessible() : true)) {
                return false;
            }
            Method c11 = ReflectJvmMapping.c(lVar2);
            if (!(c11 != null ? c11.isAccessible() : true)) {
                return false;
            }
        } else if (kCallable instanceof l.b) {
            Field b12 = ReflectJvmMapping.b(((l.b) kCallable).s());
            if (!(b12 != null ? b12.isAccessible() : true)) {
                return false;
            }
            Method d10 = ReflectJvmMapping.d((KFunction) kCallable);
            if (!(d10 != null ? d10.isAccessible() : true)) {
                return false;
            }
        } else if (kCallable instanceof i.a) {
            Field b13 = ReflectJvmMapping.b(((i.a) kCallable).s());
            if (!(b13 != null ? b13.isAccessible() : true)) {
                return false;
            }
            Method d11 = ReflectJvmMapping.d((KFunction) kCallable);
            if (!(d11 != null ? d11.isAccessible() : true)) {
                return false;
            }
        } else if (kCallable instanceof KFunction) {
            KFunction kFunction = (KFunction) kCallable;
            Method d12 = ReflectJvmMapping.d(kFunction);
            if (!(d12 != null ? d12.isAccessible() : true)) {
                return false;
            }
            KCallableImpl<?> b14 = o0.b(kCallable);
            Object b15 = (b14 == null || (H = b14.H()) == null) ? null : H.b();
            AccessibleObject accessibleObject = b15 instanceof AccessibleObject ? (AccessibleObject) b15 : null;
            if (!(accessibleObject != null ? accessibleObject.isAccessible() : true)) {
                return false;
            }
            Constructor a10 = ReflectJvmMapping.a(kFunction);
            if (!(a10 != null ? a10.isAccessible() : true)) {
                return false;
            }
        } else {
            throw new UnsupportedOperationException("Unknown callable: " + kCallable + " (" + kCallable.getClass() + ')');
        }
        return true;
    }

    public static final void b(KCallable<?> kCallable, boolean z10) {
        e<?> H;
        k.e(kCallable, "<this>");
        if (kCallable instanceof i) {
            l lVar = (l) kCallable;
            Field b10 = ReflectJvmMapping.b(lVar);
            if (b10 != null) {
                b10.setAccessible(z10);
            }
            Method c10 = ReflectJvmMapping.c(lVar);
            if (c10 != null) {
                c10.setAccessible(z10);
            }
            Method e10 = ReflectJvmMapping.e((i) kCallable);
            if (e10 == null) {
                return;
            }
            e10.setAccessible(z10);
            return;
        }
        if (kCallable instanceof l) {
            l lVar2 = (l) kCallable;
            Field b11 = ReflectJvmMapping.b(lVar2);
            if (b11 != null) {
                b11.setAccessible(z10);
            }
            Method c11 = ReflectJvmMapping.c(lVar2);
            if (c11 == null) {
                return;
            }
            c11.setAccessible(z10);
            return;
        }
        if (kCallable instanceof l.b) {
            Field b12 = ReflectJvmMapping.b(((l.b) kCallable).s());
            if (b12 != null) {
                b12.setAccessible(z10);
            }
            Method d10 = ReflectJvmMapping.d((KFunction) kCallable);
            if (d10 == null) {
                return;
            }
            d10.setAccessible(z10);
            return;
        }
        if (kCallable instanceof i.a) {
            Field b13 = ReflectJvmMapping.b(((i.a) kCallable).s());
            if (b13 != null) {
                b13.setAccessible(z10);
            }
            Method d11 = ReflectJvmMapping.d((KFunction) kCallable);
            if (d11 == null) {
                return;
            }
            d11.setAccessible(z10);
            return;
        }
        if (kCallable instanceof KFunction) {
            KFunction kFunction = (KFunction) kCallable;
            Method d12 = ReflectJvmMapping.d(kFunction);
            if (d12 != null) {
                d12.setAccessible(z10);
            }
            KCallableImpl<?> b14 = o0.b(kCallable);
            Object b15 = (b14 == null || (H = b14.H()) == null) ? null : H.b();
            AccessibleObject accessibleObject = b15 instanceof AccessibleObject ? (AccessibleObject) b15 : null;
            if (accessibleObject != null) {
                accessibleObject.setAccessible(true);
            }
            Constructor a10 = ReflectJvmMapping.a(kFunction);
            if (a10 == null) {
                return;
            }
            a10.setAccessible(z10);
            return;
        }
        throw new UnsupportedOperationException("Unknown callable: " + kCallable + " (" + kCallable.getClass() + ')');
    }
}
