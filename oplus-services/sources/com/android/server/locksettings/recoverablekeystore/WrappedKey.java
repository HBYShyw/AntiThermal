package com.android.server.locksettings.recoverablekeystore;

import android.util.Log;
import android.util.Pair;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WrappedKey {
    private static final String APPLICATION_KEY_ALGORITHM = "AES";
    private static final int GCM_TAG_LENGTH_BITS = 128;
    private static final String KEY_WRAP_CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    private static final String TAG = "WrappedKey";
    private final byte[] mKeyMaterial;
    private final byte[] mKeyMetadata;
    private final byte[] mNonce;
    private final int mPlatformKeyGenerationId;
    private final int mRecoveryStatus;

    public static WrappedKey fromSecretKey(PlatformEncryptionKey platformEncryptionKey, SecretKey secretKey, byte[] bArr) throws InvalidKeyException, KeyStoreException {
        if (secretKey.getEncoded() == null) {
            throw new InvalidKeyException("key does not expose encoded material. It cannot be wrapped.");
        }
        try {
            Cipher cipher = Cipher.getInstance(KEY_WRAP_CIPHER_ALGORITHM);
            cipher.init(3, platformEncryptionKey.getKey());
            try {
                return new WrappedKey(cipher.getIV(), cipher.wrap(secretKey), bArr, platformEncryptionKey.getGenerationId(), 1);
            } catch (IllegalBlockSizeException e) {
                Throwable cause = e.getCause();
                if (cause instanceof KeyStoreException) {
                    throw ((KeyStoreException) cause);
                }
                throw new RuntimeException("IllegalBlockSizeException should not be thrown by AES/GCM/NoPadding mode.", e);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException unused) {
            throw new RuntimeException("Android does not support AES/GCM/NoPadding. This should never happen.");
        }
    }

    public WrappedKey(byte[] bArr, byte[] bArr2, byte[] bArr3, int i) {
        this(bArr, bArr2, bArr3, i, 1);
    }

    public WrappedKey(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2) {
        this.mNonce = bArr;
        this.mKeyMaterial = bArr2;
        this.mKeyMetadata = bArr3;
        this.mPlatformKeyGenerationId = i;
        this.mRecoveryStatus = i2;
    }

    public byte[] getNonce() {
        return this.mNonce;
    }

    public byte[] getKeyMaterial() {
        return this.mKeyMaterial;
    }

    public byte[] getKeyMetadata() {
        return this.mKeyMetadata;
    }

    public int getPlatformKeyGenerationId() {
        return this.mPlatformKeyGenerationId;
    }

    public int getRecoveryStatus() {
        return this.mRecoveryStatus;
    }

    public static Map<String, Pair<SecretKey, byte[]>> unwrapKeys(PlatformDecryptionKey platformDecryptionKey, Map<String, WrappedKey> map) throws NoSuchAlgorithmException, NoSuchPaddingException, BadPlatformKeyException, InvalidKeyException, InvalidAlgorithmParameterException {
        HashMap hashMap = new HashMap();
        Cipher cipher = Cipher.getInstance(KEY_WRAP_CIPHER_ALGORITHM);
        int generationId = platformDecryptionKey.getGenerationId();
        for (String str : map.keySet()) {
            WrappedKey wrappedKey = map.get(str);
            if (wrappedKey.getPlatformKeyGenerationId() != generationId) {
                throw new BadPlatformKeyException(String.format(Locale.US, "WrappedKey with alias '%s' was wrapped with platform key %d, not platform key %d", str, Integer.valueOf(wrappedKey.getPlatformKeyGenerationId()), Integer.valueOf(platformDecryptionKey.getGenerationId())));
            }
            cipher.init(4, platformDecryptionKey.getKey(), new GCMParameterSpec(128, wrappedKey.getNonce()));
            try {
                hashMap.put(str, Pair.create((SecretKey) cipher.unwrap(wrappedKey.getKeyMaterial(), APPLICATION_KEY_ALGORITHM, 3), wrappedKey.getKeyMetadata()));
            } catch (InvalidKeyException | NoSuchAlgorithmException e) {
                Log.e(TAG, String.format(Locale.US, "Error unwrapping recoverable key with alias '%s'", str), e);
            }
        }
        return hashMap;
    }
}
