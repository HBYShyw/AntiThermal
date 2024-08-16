package com.android.server.accounts;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class CryptoHelper {
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 16;
    private static final String KEY_ALGORITHM = "AES";
    private static final String KEY_CIPHER = "cipher";
    private static final String KEY_IV = "iv";
    private static final String KEY_MAC = "mac";
    private static final String MAC_ALGORITHM = "HMACSHA256";
    private static final String TAG = "Account";
    private static CryptoHelper sInstance;
    private final SecretKey mEncryptionKey = KeyGenerator.getInstance(KEY_ALGORITHM).generateKey();
    private final SecretKey mMacKey = KeyGenerator.getInstance(MAC_ALGORITHM).generateKey();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized CryptoHelper getInstance() throws NoSuchAlgorithmException {
        CryptoHelper cryptoHelper;
        synchronized (CryptoHelper.class) {
            if (sInstance == null) {
                sInstance = new CryptoHelper();
            }
            cryptoHelper = sInstance;
        }
        return cryptoHelper;
    }

    private CryptoHelper() throws NoSuchAlgorithmException {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle encryptBundle(Bundle bundle) throws GeneralSecurityException {
        Objects.requireNonNull(bundle, "Cannot encrypt null bundle.");
        Parcel obtain = Parcel.obtain();
        bundle.writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(1, this.mEncryptionKey);
        byte[] doFinal = cipher.doFinal(marshall);
        byte[] iv = cipher.getIV();
        byte[] createMac = createMac(doFinal, iv);
        Bundle bundle2 = new Bundle();
        bundle2.putByteArray(KEY_CIPHER, doFinal);
        bundle2.putByteArray(KEY_MAC, createMac);
        bundle2.putByteArray(KEY_IV, iv);
        return bundle2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle decryptBundle(Bundle bundle) throws GeneralSecurityException {
        Objects.requireNonNull(bundle, "Cannot decrypt null bundle.");
        byte[] byteArray = bundle.getByteArray(KEY_IV);
        byte[] byteArray2 = bundle.getByteArray(KEY_CIPHER);
        if (!verifyMac(byteArray2, byteArray, bundle.getByteArray(KEY_MAC))) {
            Log.w(TAG, "Escrow mac mismatched!");
            return null;
        }
        IvParameterSpec ivParameterSpec = new IvParameterSpec(byteArray);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(2, this.mEncryptionKey, ivParameterSpec);
        byte[] doFinal = cipher.doFinal(byteArray2);
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(doFinal, 0, doFinal.length);
        obtain.setDataPosition(0);
        Bundle bundle2 = new Bundle();
        bundle2.readFromParcel(obtain);
        obtain.recycle();
        return bundle2;
    }

    private boolean verifyMac(byte[] bArr, byte[] bArr2, byte[] bArr3) throws GeneralSecurityException {
        if (bArr == null || bArr.length == 0 || bArr3 == null || bArr3.length == 0) {
            if (!Log.isLoggable(TAG, 2)) {
                return false;
            }
            Log.v(TAG, "Cipher or MAC is empty!");
            return false;
        }
        return constantTimeArrayEquals(bArr3, createMac(bArr, bArr2));
    }

    private byte[] createMac(byte[] bArr, byte[] bArr2) throws GeneralSecurityException {
        Mac mac = Mac.getInstance(MAC_ALGORITHM);
        mac.init(this.mMacKey);
        mac.update(bArr);
        mac.update(bArr2);
        return mac.doFinal();
    }

    private static boolean constantTimeArrayEquals(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null) {
            return bArr == bArr2;
        }
        if (bArr.length != bArr2.length) {
            return false;
        }
        boolean z = true;
        for (int i = 0; i < bArr2.length; i++) {
            z &= bArr[i] == bArr2[i];
        }
        return z;
    }
}
