package com.oplus.wrapper.media.permission;

/* loaded from: classes.dex */
public class ClearCallingIdentityContext {
    private final android.media.permission.ClearCallingIdentityContext mClearCallingIdentityContext;

    private ClearCallingIdentityContext(AutoCloseable callingIdentityContext) {
        this.mClearCallingIdentityContext = (android.media.permission.ClearCallingIdentityContext) callingIdentityContext;
    }

    public static ClearCallingIdentityContext create() {
        android.media.permission.ClearCallingIdentityContext create = android.media.permission.ClearCallingIdentityContext.create();
        if (create == null) {
            return null;
        }
        return new ClearCallingIdentityContext(create);
    }

    public void close() {
        this.mClearCallingIdentityContext.close();
    }
}
