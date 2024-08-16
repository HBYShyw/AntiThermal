package com.android.server.locksettings.recoverablekeystore.storage;

import android.os.ServiceSpecificException;
import android.security.KeyStore2;
import android.security.keystore.KeyProtection;
import android.system.keystore2.KeyDescriptor;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.locksettings.recoverablekeystore.KeyStoreProxy;
import com.android.server.locksettings.recoverablekeystore.KeyStoreProxyImpl;
import com.android.server.slice.SliceClientPermissions;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Locale;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ApplicationKeyStorage {
    private static final String APPLICATION_KEY_ALIAS_PREFIX = "com.android.server.locksettings.recoverablekeystore/application/";
    private static final String APPLICATION_KEY_GRANT_PREFIX = "recoverable_key:";
    private static final String TAG = "RecoverableAppKeyStore";
    private final KeyStoreProxy mKeyStore;

    public static ApplicationKeyStorage getInstance() throws KeyStoreException {
        return new ApplicationKeyStorage(new KeyStoreProxyImpl(KeyStoreProxyImpl.getAndLoadAndroidKeyStore()));
    }

    @VisibleForTesting
    ApplicationKeyStorage(KeyStoreProxy keyStoreProxy) {
        this.mKeyStore = keyStoreProxy;
    }

    public String getGrantAlias(int i, int i2, String str) {
        Log.i(TAG, String.format(Locale.US, "Get %d/%d/%s", Integer.valueOf(i), Integer.valueOf(i2), str));
        return makeKeystoreEngineGrantString(i2, getInternalAlias(i, i2, str));
    }

    public void setSymmetricKeyEntry(int i, int i2, String str, byte[] bArr) throws KeyStoreException {
        Log.i(TAG, String.format(Locale.US, "Set %d/%d/%s: %d bytes of key material", Integer.valueOf(i), Integer.valueOf(i2), str, Integer.valueOf(bArr.length)));
        try {
            this.mKeyStore.setEntry(getInternalAlias(i, i2, str), new KeyStore.SecretKeyEntry(new SecretKeySpec(bArr, "AES")), new KeyProtection.Builder(3).setBlockModes("GCM").setEncryptionPaddings("NoPadding").build());
        } catch (KeyStoreException e) {
            throw new ServiceSpecificException(22, e.getMessage());
        }
    }

    public void deleteEntry(int i, int i2, String str) {
        Log.i(TAG, String.format(Locale.US, "Del %d/%d/%s", Integer.valueOf(i), Integer.valueOf(i2), str));
        try {
            this.mKeyStore.deleteEntry(getInternalAlias(i, i2, str));
        } catch (KeyStoreException e) {
            throw new ServiceSpecificException(22, e.getMessage());
        }
    }

    private String getInternalAlias(int i, int i2, String str) {
        return APPLICATION_KEY_ALIAS_PREFIX + i + SliceClientPermissions.SliceAuthority.DELIMITER + i2 + SliceClientPermissions.SliceAuthority.DELIMITER + str;
    }

    private String makeKeystoreEngineGrantString(int i, String str) {
        if (str == null) {
            return null;
        }
        KeyDescriptor keyDescriptor = new KeyDescriptor();
        keyDescriptor.domain = 0;
        keyDescriptor.nspace = -1L;
        keyDescriptor.alias = str;
        keyDescriptor.blob = null;
        try {
            return String.format("%s%016X", APPLICATION_KEY_GRANT_PREFIX, Long.valueOf(KeyStore2.getInstance().grant(keyDescriptor, i, 261).nspace));
        } catch (android.security.KeyStoreException e) {
            if (e.getNumericErrorCode() == 6) {
                Log.e(TAG, "Failed to get grant for KeyStore key - key not found", e);
                throw new ServiceSpecificException(30, e.getMessage());
            }
            Log.e(TAG, "Failed to get grant for KeyStore key.", e);
            throw new ServiceSpecificException(22, e.getMessage());
        }
    }
}
