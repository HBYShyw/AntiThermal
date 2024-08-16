package com.android.server.locksettings.recoverablekeystore;

import javax.crypto.SecretKey;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PlatformEncryptionKey {
    private final int mGenerationId;
    private final SecretKey mKey;

    public PlatformEncryptionKey(int i, SecretKey secretKey) {
        this.mGenerationId = i;
        this.mKey = secretKey;
    }

    public int getGenerationId() {
        return this.mGenerationId;
    }

    public SecretKey getKey() {
        return this.mKey;
    }
}
