package com.android.server.credentials;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class NonCredentialProviderCallerException extends RuntimeException {
    private static final String MESSAGE = " is not an existing Credential Provider.";

    public NonCredentialProviderCallerException(String str) {
        super(str + MESSAGE);
    }
}
