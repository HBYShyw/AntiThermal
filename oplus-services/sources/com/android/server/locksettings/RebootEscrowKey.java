package com.android.server.locksettings;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class RebootEscrowKey {
    private static final String KEY_ALGO = "AES";
    private static final int KEY_SIZE_BITS = 256;
    private final SecretKey mKey;

    private RebootEscrowKey(SecretKey secretKey) {
        this.mKey = secretKey;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RebootEscrowKey fromKeyBytes(byte[] bArr) {
        return new RebootEscrowKey(new SecretKeySpec(bArr, KEY_ALGO));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RebootEscrowKey generate() throws IOException {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGO);
            keyGenerator.init(256, new SecureRandom());
            return new RebootEscrowKey(keyGenerator.generateKey());
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Could not generate new secret key", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SecretKey getKey() {
        return this.mKey;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] getKeyBytes() {
        return this.mKey.getEncoded();
    }
}
