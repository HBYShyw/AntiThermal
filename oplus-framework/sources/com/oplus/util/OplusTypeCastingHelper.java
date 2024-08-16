package com.oplus.util;

/* loaded from: classes.dex */
public final class OplusTypeCastingHelper {
    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T typeCasting(Class<T> type, Object obj) {
        if (obj != 0 && type.isInstance(obj)) {
            return obj;
        }
        return null;
    }
}
