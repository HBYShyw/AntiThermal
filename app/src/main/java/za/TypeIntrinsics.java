package za;

import java.util.List;
import java.util.Map;
import java.util.Set;

/* compiled from: TypeIntrinsics.java */
/* renamed from: za.e0, reason: use source file name */
/* loaded from: classes2.dex */
public class TypeIntrinsics {
    public static List a(Object obj) {
        if ((obj instanceof ab.a) && !(obj instanceof ab.b)) {
            l(obj, "kotlin.collections.MutableList");
        }
        return e(obj);
    }

    public static Map b(Object obj) {
        if ((obj instanceof ab.a) && !(obj instanceof ab.c)) {
            l(obj, "kotlin.collections.MutableMap");
        }
        return f(obj);
    }

    public static Set c(Object obj) {
        if ((obj instanceof ab.a) && !(obj instanceof ab.d)) {
            l(obj, "kotlin.collections.MutableSet");
        }
        return g(obj);
    }

    public static Object d(Object obj, int i10) {
        if (obj != null && !i(obj, i10)) {
            l(obj, "kotlin.jvm.functions.Function" + i10);
        }
        return obj;
    }

    public static List e(Object obj) {
        try {
            return (List) obj;
        } catch (ClassCastException e10) {
            throw k(e10);
        }
    }

    public static Map f(Object obj) {
        try {
            return (Map) obj;
        } catch (ClassCastException e10) {
            throw k(e10);
        }
    }

    public static Set g(Object obj) {
        try {
            return (Set) obj;
        } catch (ClassCastException e10) {
            throw k(e10);
        }
    }

    public static int h(Object obj) {
        if (obj instanceof FunctionBase) {
            return ((FunctionBase) obj).getArity();
        }
        if (obj instanceof ya.a) {
            return 0;
        }
        if (obj instanceof ya.l) {
            return 1;
        }
        if (obj instanceof ya.p) {
            return 2;
        }
        if (obj instanceof ya.q) {
            return 3;
        }
        if (obj instanceof ya.r) {
            return 4;
        }
        if (obj instanceof ya.s) {
            return 5;
        }
        if (obj instanceof ya.t) {
            return 6;
        }
        if (obj instanceof ya.u) {
            return 7;
        }
        if (obj instanceof ya.v) {
            return 8;
        }
        if (obj instanceof ya.w) {
            return 9;
        }
        if (obj instanceof ya.b) {
            return 10;
        }
        if (obj instanceof ya.c) {
            return 11;
        }
        if (obj instanceof ya.d) {
            return 12;
        }
        if (obj instanceof ya.e) {
            return 13;
        }
        if (obj instanceof ya.f) {
            return 14;
        }
        if (obj instanceof ya.g) {
            return 15;
        }
        if (obj instanceof ya.h) {
            return 16;
        }
        if (obj instanceof ya.i) {
            return 17;
        }
        if (obj instanceof ya.j) {
            return 18;
        }
        if (obj instanceof ya.k) {
            return 19;
        }
        if (obj instanceof ya.m) {
            return 20;
        }
        if (obj instanceof ya.n) {
            return 21;
        }
        return obj instanceof ya.o ? 22 : -1;
    }

    public static boolean i(Object obj, int i10) {
        return (obj instanceof ma.c) && h(obj) == i10;
    }

    private static <T extends Throwable> T j(T t7) {
        return (T) k.k(t7, TypeIntrinsics.class.getName());
    }

    public static ClassCastException k(ClassCastException classCastException) {
        throw ((ClassCastException) j(classCastException));
    }

    public static void l(Object obj, String str) {
        m((obj == null ? "null" : obj.getClass().getName()) + " cannot be cast to " + str);
    }

    public static void m(String str) {
        throw k(new ClassCastException(str));
    }
}
