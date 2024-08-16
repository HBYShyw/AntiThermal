package com.android.server.locksettings.recoverablekeystore;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class KeyStoreProxyImpl implements KeyStoreProxy {
    public static final String ANDROID_KEY_STORE_PROVIDER = "AndroidKeyStore";
    private final KeyStore mKeyStore;

    public KeyStoreProxyImpl(KeyStore keyStore) {
        this.mKeyStore = keyStore;
    }

    @Override // com.android.server.locksettings.recoverablekeystore.KeyStoreProxy
    public boolean containsAlias(String str) throws KeyStoreException {
        return this.mKeyStore.containsAlias(str);
    }

    @Override // com.android.server.locksettings.recoverablekeystore.KeyStoreProxy
    public Key getKey(String str, char[] cArr) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        return this.mKeyStore.getKey(str, cArr);
    }

    @Override // com.android.server.locksettings.recoverablekeystore.KeyStoreProxy
    public void setEntry(String str, KeyStore.Entry entry, KeyStore.ProtectionParameter protectionParameter) throws KeyStoreException {
        this.mKeyStore.setEntry(str, entry, protectionParameter);
    }

    @Override // com.android.server.locksettings.recoverablekeystore.KeyStoreProxy
    public void deleteEntry(String str) throws KeyStoreException {
        this.mKeyStore.deleteEntry(str);
    }

    public static KeyStore getAndLoadAndroidKeyStore() throws KeyStoreException {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_PROVIDER);
        try {
            keyStore.load(null);
            return keyStore;
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new KeyStoreException("Unable to load keystore.", e);
        }
    }
}
