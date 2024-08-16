package com.android.server.companion.securechannel;

import android.security.keystore.KeyGenParameterSpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class KeyStoreUtils {
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String TAG = "CDM_SecureChannelKeyStore";

    private KeyStoreUtils() {
    }

    static KeyStore loadKeyStore() throws GeneralSecurityException {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        try {
            keyStore.load(null);
            return keyStore;
        } catch (IOException e) {
            throw new KeyStoreException("Failed to load Android Keystore.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getEncodedCertificateChain(String str) throws GeneralSecurityException {
        Certificate[] certificateChain = loadKeyStore().getCertificateChain(str);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (Certificate certificate : certificateChain) {
            byteArrayOutputStream.writeBytes(certificate.getEncoded());
        }
        return byteArrayOutputStream.toByteArray();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void generateAttestationKeyPair(String str, byte[] bArr) throws GeneralSecurityException {
        KeyGenParameterSpec build = new KeyGenParameterSpec.Builder(str, 12).setAttestationChallenge(bArr).setDigests("SHA-256").build();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", ANDROID_KEYSTORE);
        keyPairGenerator.initialize(build);
        keyPairGenerator.generateKeyPair();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean aliasExists(String str) {
        try {
            return loadKeyStore().containsAlias(str);
        } catch (GeneralSecurityException unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void cleanUp(String str) {
        try {
            KeyStore loadKeyStore = loadKeyStore();
            if (loadKeyStore.containsAlias(str)) {
                loadKeyStore.deleteEntry(str);
            }
        } catch (Exception unused) {
        }
    }
}
