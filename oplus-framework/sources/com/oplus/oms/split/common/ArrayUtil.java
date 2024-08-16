package com.oplus.oms.split.common;

/* loaded from: classes.dex */
public class ArrayUtil {
    private ArrayUtil() {
    }

    public static <T> T safeGet(Object[] objArr, int i, Class<T> cls, T t) {
        if (cls == null || i < 0) {
            return t;
        }
        if (objArr == null || objArr.length <= i) {
            return t;
        }
        T t2 = (T) objArr[i];
        if (t2 != null && cls.isAssignableFrom(t2.getClass())) {
            return t2;
        }
        return t;
    }

    public static <T> T safeGet(Object[] objArr, int i, Class<T> cls) {
        return (T) safeGet(objArr, i, cls, null);
    }
}
