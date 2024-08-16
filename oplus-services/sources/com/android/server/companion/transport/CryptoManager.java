package com.android.server.companion.transport;

import android.util.Slog;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CryptoManager {
    private static final String ALGORITHM = "AES";
    private static final int SECRET_KEY_LENGTH = 32;
    private static final String TAG = "CDM_CryptoManager";
    private static final String TRANSFORMATION = "AES/CBC/PKCS7Padding";
    private Cipher mDecryptCipher;
    private Cipher mEncryptCipher;
    private final byte[] mPreSharedKey;
    private SecretKey mSecretKey;

    public CryptoManager(byte[] bArr) {
        if (bArr == null) {
            this.mPreSharedKey = Arrays.copyOf(new byte[0], 32);
        } else {
            this.mPreSharedKey = Arrays.copyOf(bArr, 32);
        }
        this.mSecretKey = new SecretKeySpec(this.mPreSharedKey, ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            this.mEncryptCipher = cipher;
            cipher.init(1, this.mSecretKey);
            this.mDecryptCipher = Cipher.getInstance(TRANSFORMATION);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            Slog.e(TAG, e.getMessage());
        }
    }

    public byte[] encrypt(byte[] bArr) {
        try {
            Cipher cipher = this.mEncryptCipher;
            if (cipher == null) {
                return null;
            }
            byte[] doFinal = cipher.doFinal(bArr);
            return ByteBuffer.allocate(this.mEncryptCipher.getIV().length + 4 + 4 + doFinal.length).putInt(this.mEncryptCipher.getIV().length).put(this.mEncryptCipher.getIV()).putInt(doFinal.length).put(doFinal).array();
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            Slog.e(TAG, e.getMessage());
            return null;
        }
    }

    public byte[] decrypt(byte[] bArr) {
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        byte[] bArr2 = new byte[wrap.getInt()];
        wrap.get(bArr2);
        byte[] bArr3 = new byte[wrap.getInt()];
        wrap.get(bArr3);
        try {
            this.mDecryptCipher.init(2, getKey(), new IvParameterSpec(bArr2));
            return this.mDecryptCipher.doFinal(bArr3);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            Slog.e(TAG, e.getMessage());
            return null;
        }
    }

    private SecretKey getKey() {
        SecretKey secretKey = this.mSecretKey;
        if (secretKey != null) {
            return secretKey;
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(this.mPreSharedKey, ALGORITHM);
        this.mSecretKey = secretKeySpec;
        return secretKeySpec;
    }
}
