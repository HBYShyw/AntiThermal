package com.oplus.epona;

import com.oplus.epona.utils.Logger;

/* loaded from: classes.dex */
final class ParcelableException extends RuntimeException {
    private static final String TAG = "ParcelableException";

    private ParcelableException(Throwable th) {
        super(th);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ParcelableException create(ExceptionInfo exceptionInfo) {
        String name = exceptionInfo.getName();
        String message = exceptionInfo.getMessage();
        try {
            Class<?> cls = Class.forName(name);
            if (Throwable.class.isAssignableFrom(cls)) {
                return new ParcelableException((Throwable) cls.getConstructor(String.class).newInstance(message));
            }
        } catch (ReflectiveOperationException e10) {
            Logger.e(TAG, e10.toString(), new Object[0]);
        }
        return new ParcelableException(new RuntimeException(name + ": " + message));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <T extends Throwable> void maybeRethrow(Class<T> cls) {
        if (cls.isAssignableFrom(getCause().getClass())) {
            throw getCause();
        }
    }
}
