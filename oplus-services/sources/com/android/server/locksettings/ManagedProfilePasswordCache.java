package com.android.server.locksettings;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.widget.LockscreenCredential;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

@VisibleForTesting
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ManagedProfilePasswordCache {
    private static final int CACHE_TIMEOUT_SECONDS = (int) TimeUnit.DAYS.toSeconds(7);
    private static final int KEY_LENGTH = 256;
    private static final String TAG = "ManagedProfilePasswordCache";
    private final SparseArray<byte[]> mEncryptedPasswords = new SparseArray<>();
    private final KeyStore mKeyStore;

    public ManagedProfilePasswordCache(KeyStore keyStore) {
        this.mKeyStore = keyStore;
    }

    public void storePassword(int i, LockscreenCredential lockscreenCredential, long j) {
        if (j == 0) {
            return;
        }
        synchronized (this.mEncryptedPasswords) {
            if (this.mEncryptedPasswords.contains(i)) {
                return;
            }
            String encryptionKeyName = getEncryptionKeyName(i);
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", this.mKeyStore.getProvider());
                keyGenerator.init(new KeyGenParameterSpec.Builder(encryptionKeyName, 3).setKeySize(256).setBlockModes("GCM").setNamespace(SyntheticPasswordCrypto.keyNamespace()).setEncryptionPaddings("NoPadding").setUserAuthenticationRequired(true).setBoundToSpecificSecureUserId(j).setUserAuthenticationValidityDurationSeconds(CACHE_TIMEOUT_SECONDS).build());
                SecretKey generateKey = keyGenerator.generateKey();
                try {
                    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                    cipher.init(1, generateKey);
                    this.mEncryptedPasswords.put(i, ArrayUtils.concat(new byte[][]{cipher.getIV(), cipher.doFinal(lockscreenCredential.getCredential())}));
                } catch (GeneralSecurityException e) {
                    Slog.d(TAG, "Cannot encrypt", e);
                }
            } catch (GeneralSecurityException e2) {
                Slog.e(TAG, "Cannot generate key", e2);
            }
        }
    }

    public LockscreenCredential retrievePassword(int i) {
        synchronized (this.mEncryptedPasswords) {
            byte[] bArr = this.mEncryptedPasswords.get(i);
            if (bArr == null) {
                return null;
            }
            try {
                Key key = this.mKeyStore.getKey(getEncryptionKeyName(i), null);
                if (key == null) {
                    return null;
                }
                byte[] copyOf = Arrays.copyOf(bArr, 12);
                byte[] copyOfRange = Arrays.copyOfRange(bArr, 12, bArr.length);
                try {
                    try {
                        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                        cipher.init(2, key, new GCMParameterSpec(128, copyOf));
                        byte[] doFinal = cipher.doFinal(copyOfRange);
                        LockscreenCredential createManagedPassword = LockscreenCredential.createManagedPassword(doFinal);
                        Arrays.fill(doFinal, (byte) 0);
                        return createManagedPassword;
                    } catch (GeneralSecurityException e) {
                        Slog.d(TAG, "Cannot decrypt", e);
                        return null;
                    }
                } catch (UserNotAuthenticatedException unused) {
                    Slog.i(TAG, "Device not unlocked for more than 7 days");
                    return null;
                }
            } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e2) {
                Slog.d(TAG, "Cannot get key", e2);
                return null;
            }
        }
    }

    public void removePassword(int i) {
        synchronized (this.mEncryptedPasswords) {
            String encryptionKeyName = getEncryptionKeyName(i);
            String legacyEncryptionKeyName = getLegacyEncryptionKeyName(i);
            try {
                if (this.mKeyStore.containsAlias(encryptionKeyName)) {
                    this.mKeyStore.deleteEntry(encryptionKeyName);
                }
                if (this.mKeyStore.containsAlias(legacyEncryptionKeyName)) {
                    this.mKeyStore.deleteEntry(legacyEncryptionKeyName);
                }
            } catch (KeyStoreException e) {
                Slog.d(TAG, "Cannot delete key", e);
            }
            if (this.mEncryptedPasswords.contains(i)) {
                Arrays.fill(this.mEncryptedPasswords.get(i), (byte) 0);
                this.mEncryptedPasswords.remove(i);
            }
        }
    }

    private static String getEncryptionKeyName(int i) {
        return "com.android.server.locksettings.unified_profile_cache_v2_" + i;
    }

    private static String getLegacyEncryptionKeyName(int i) {
        return "com.android.server.locksettings.unified_profile_cache_" + i;
    }
}
