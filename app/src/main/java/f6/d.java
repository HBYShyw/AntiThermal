package f6;

import android.util.ArrayMap;
import java.lang.reflect.Method;

/* compiled from: ReflectUtil.java */
/* loaded from: classes.dex */
public class d {

    /* renamed from: b, reason: collision with root package name */
    public static volatile d f11388b;

    /* renamed from: a, reason: collision with root package name */
    ArrayMap<String, Method> f11389a = new ArrayMap<>();

    public static d a() {
        if (f11388b == null) {
            synchronized (d.class) {
                if (f11388b == null) {
                    f11388b = new d();
                }
            }
        }
        return f11388b;
    }

    private String c(String str, String str2) {
        return str + ":" + str2;
    }

    public static <T> T d(Object obj, String str) {
        return (T) e(obj, str, null, new Object[0]);
    }

    public static <T> T e(Object obj, String str, Class<?>[] clsArr, Object... objArr) {
        Method declaredMethod;
        if (clsArr != null) {
            try {
                if (clsArr.length > 0) {
                    declaredMethod = obj.getClass().getDeclaredMethod(str, clsArr);
                    return (T) declaredMethod.invoke(obj, objArr);
                }
            } catch (Exception unused) {
                return null;
            }
        }
        declaredMethod = obj.getClass().getDeclaredMethod(str, new Class[0]);
        return (T) declaredMethod.invoke(obj, objArr);
    }

    public Method b(String str, String str2, Class<?>... clsArr) {
        String c10 = c(str, str2);
        synchronized (this.f11389a) {
            Method method = this.f11389a.get(c10);
            if (method != null) {
                return method;
            }
            try {
                method = Class.forName(str).getDeclaredMethod(str2, clsArr);
            } catch (Exception e10) {
                e10.printStackTrace();
            }
            if (method != null) {
                synchronized (this.f11389a) {
                    this.f11389a.put(c10, method);
                }
            }
            return method;
        }
    }
}
