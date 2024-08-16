package hb;

import gb.KClass;
import gb.KFunction;
import gb.n;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import jb.KCallableImpl;
import jb.KClassImpl;
import jb.KFunctionImpl;
import pb.ConstructorDescriptor;
import pb.FunctionDescriptor;
import za.k;

/* compiled from: KClasses.kt */
/* loaded from: classes2.dex */
public final class c {
    public static final <T> Collection<n<T, ?>> a(KClass<T> kClass) {
        k.e(kClass, "<this>");
        Collection<KCallableImpl<?>> g6 = ((KClassImpl) kClass).Y().invoke().g();
        ArrayList arrayList = new ArrayList();
        for (T t7 : g6) {
            KCallableImpl kCallableImpl = (KCallableImpl) t7;
            if (d(kCallableImpl) && (kCallableImpl instanceof n)) {
                arrayList.add(t7);
            }
        }
        return arrayList;
    }

    public static final <T> KFunction<T> b(KClass<T> kClass) {
        T t7;
        k.e(kClass, "<this>");
        Iterator<T> it = ((KClassImpl) kClass).X().iterator();
        while (true) {
            if (!it.hasNext()) {
                t7 = null;
                break;
            }
            t7 = it.next();
            KFunction kFunction = (KFunction) t7;
            k.c(kFunction, "null cannot be cast to non-null type kotlin.reflect.jvm.internal.KFunctionImpl");
            FunctionDescriptor L = ((KFunctionImpl) kFunction).L();
            k.c(L, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ConstructorDescriptor");
            if (((ConstructorDescriptor) L).J()) {
                break;
            }
        }
        return (KFunction) t7;
    }

    private static final boolean c(KCallableImpl<?> kCallableImpl) {
        return kCallableImpl.L().r0() != null;
    }

    private static final boolean d(KCallableImpl<?> kCallableImpl) {
        return !c(kCallableImpl);
    }
}
