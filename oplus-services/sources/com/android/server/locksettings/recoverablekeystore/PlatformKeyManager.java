package com.android.server.locksettings.recoverablekeystore;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.RemoteException;
import android.security.GateKeeper;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProtection;
import android.service.gatekeeper.IGateKeeperService;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb;
import com.android.server.slice.SliceClientPermissions;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PlatformKeyManager {
    private static final String DECRYPT_KEY_ALIAS_SUFFIX = "decrypt";
    private static final String ENCRYPT_KEY_ALIAS_SUFFIX = "encrypt";
    private static final byte[] GCM_INSECURE_NONCE_BYTES = new byte[12];
    private static final int GCM_TAG_LENGTH_BITS = 128;
    private static final String KEY_ALGORITHM = "AES";
    private static final String KEY_ALIAS_PREFIX = "com.android.server.locksettings.recoverablekeystore/platform/";
    private static final int KEY_SIZE_BITS = 256;
    private static final String KEY_WRAP_CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    static final int MIN_GENERATION_ID_FOR_UNLOCKED_DEVICE_REQUIRED = 1001000;
    private static final String TAG = "PlatformKeyManager";
    private final Context mContext;
    private final RecoverableKeyStoreDb mDatabase;
    private final KeyStoreProxy mKeyStore;

    public static PlatformKeyManager getInstance(Context context, RecoverableKeyStoreDb recoverableKeyStoreDb) throws KeyStoreException, NoSuchAlgorithmException {
        return new PlatformKeyManager(context.getApplicationContext(), new KeyStoreProxyImpl(getAndLoadAndroidKeyStore()), recoverableKeyStoreDb);
    }

    @VisibleForTesting
    PlatformKeyManager(Context context, KeyStoreProxy keyStoreProxy, RecoverableKeyStoreDb recoverableKeyStoreDb) {
        this.mKeyStore = keyStoreProxy;
        this.mContext = context;
        this.mDatabase = recoverableKeyStoreDb;
    }

    public int getGenerationId(int i) {
        return this.mDatabase.getPlatformKeyGenerationId(i);
    }

    public boolean isDeviceLocked(int i) {
        return ((KeyguardManager) this.mContext.getSystemService(KeyguardManager.class)).isDeviceLocked(i);
    }

    public void invalidatePlatformKey(int i, int i2) {
        if (i2 != -1) {
            try {
                this.mKeyStore.deleteEntry(getEncryptAlias(i, i2));
                this.mKeyStore.deleteEntry(getDecryptAlias(i, i2));
            } catch (KeyStoreException unused) {
            }
        }
    }

    @VisibleForTesting
    void regenerate(int i) throws NoSuchAlgorithmException, KeyStoreException, IOException, RemoteException {
        int generationId = getGenerationId(i);
        int i2 = 1;
        if (generationId != -1) {
            invalidatePlatformKey(i, generationId);
            i2 = 1 + generationId;
        }
        generateAndLoadKey(i, i2);
    }

    public PlatformEncryptionKey getEncryptKey(int i) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, RemoteException {
        init(i);
        try {
            getDecryptKeyInternal(i);
            return getEncryptKeyInternal(i);
        } catch (UnrecoverableKeyException unused) {
            Log.i(TAG, String.format(Locale.US, "Regenerating permanently invalid Platform key for user %d.", Integer.valueOf(i)));
            this.regenerate(i);
            return this.getEncryptKeyInternal(i);
        }
    }

    private PlatformEncryptionKey getEncryptKeyInternal(int i) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
        int generationId = getGenerationId(i);
        String encryptAlias = getEncryptAlias(i, generationId);
        if (!isKeyLoaded(i, generationId)) {
            throw new UnrecoverableKeyException("KeyStore doesn't contain key " + encryptAlias);
        }
        return new PlatformEncryptionKey(generationId, (SecretKey) this.mKeyStore.getKey(encryptAlias, null));
    }

    public PlatformDecryptionKey getDecryptKey(int i) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, RemoteException {
        init(i);
        try {
            PlatformDecryptionKey decryptKeyInternal = getDecryptKeyInternal(i);
            ensureDecryptionKeyIsValid(i, decryptKeyInternal);
            return decryptKeyInternal;
        } catch (UnrecoverableKeyException unused) {
            Log.i(TAG, String.format(Locale.US, "Regenerating permanently invalid Platform key for user %d.", Integer.valueOf(i)));
            regenerate(i);
            return getDecryptKeyInternal(i);
        }
    }

    private PlatformDecryptionKey getDecryptKeyInternal(int i) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
        int generationId = getGenerationId(i);
        String decryptAlias = getDecryptAlias(i, generationId);
        if (!isKeyLoaded(i, generationId)) {
            throw new UnrecoverableKeyException("KeyStore doesn't contain key " + decryptAlias);
        }
        return new PlatformDecryptionKey(generationId, (SecretKey) this.mKeyStore.getKey(decryptAlias, null));
    }

    private void ensureDecryptionKeyIsValid(int i, PlatformDecryptionKey platformDecryptionKey) throws UnrecoverableKeyException {
        try {
            Cipher.getInstance(KEY_WRAP_CIPHER_ALGORITHM).init(4, platformDecryptionKey.getKey(), new GCMParameterSpec(128, GCM_INSECURE_NONCE_BYTES));
        } catch (KeyPermanentlyInvalidatedException e) {
            Log.e(TAG, String.format(Locale.US, "The platform key for user %d became invalid.", Integer.valueOf(i)));
            throw new UnrecoverableKeyException(e.getMessage());
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException unused) {
        }
    }

    void init(int i) throws KeyStoreException, NoSuchAlgorithmException, IOException, RemoteException {
        int generationId = getGenerationId(i);
        if (isKeyLoaded(i, generationId)) {
            Log.i(TAG, String.format(Locale.US, "Platform key generation %d exists already.", Integer.valueOf(generationId)));
            return;
        }
        int i2 = 1;
        if (generationId == -1) {
            Log.i(TAG, "Generating initial platform key generation ID.");
        } else {
            Log.w(TAG, String.format(Locale.US, "Platform generation ID was %d but no entry was present in AndroidKeyStore. Generating fresh key.", Integer.valueOf(generationId)));
            i2 = 1 + generationId;
        }
        generateAndLoadKey(i, Math.max(i2, MIN_GENERATION_ID_FOR_UNLOCKED_DEVICE_REQUIRED));
    }

    private String getEncryptAlias(int i, int i2) {
        return KEY_ALIAS_PREFIX + i + SliceClientPermissions.SliceAuthority.DELIMITER + i2 + SliceClientPermissions.SliceAuthority.DELIMITER + ENCRYPT_KEY_ALIAS_SUFFIX;
    }

    private String getDecryptAlias(int i, int i2) {
        return KEY_ALIAS_PREFIX + i + SliceClientPermissions.SliceAuthority.DELIMITER + i2 + SliceClientPermissions.SliceAuthority.DELIMITER + DECRYPT_KEY_ALIAS_SUFFIX;
    }

    private void setGenerationId(int i, int i2) throws IOException {
        this.mDatabase.setPlatformKeyGenerationId(i, i2);
    }

    private boolean isKeyLoaded(int i, int i2) throws KeyStoreException {
        return this.mKeyStore.containsAlias(getEncryptAlias(i, i2)) && this.mKeyStore.containsAlias(getDecryptAlias(i, i2));
    }

    @VisibleForTesting
    IGateKeeperService getGateKeeperService() {
        return GateKeeper.getService();
    }

    private void generateAndLoadKey(int i, int i2) throws NoSuchAlgorithmException, KeyStoreException, IOException, RemoteException {
        String encryptAlias = getEncryptAlias(i, i2);
        String decryptAlias = getDecryptAlias(i, i2);
        SecretKey generateAesKey = generateAesKey();
        KeyProtection.Builder encryptionPaddings = new KeyProtection.Builder(2).setBlockModes("GCM").setEncryptionPaddings("NoPadding");
        if (i == 0) {
            encryptionPaddings.setUnlockedDeviceRequired(true);
        }
        this.mKeyStore.setEntry(decryptAlias, new KeyStore.SecretKeyEntry(generateAesKey), encryptionPaddings.build());
        this.mKeyStore.setEntry(encryptAlias, new KeyStore.SecretKeyEntry(generateAesKey), new KeyProtection.Builder(1).setBlockModes("GCM").setEncryptionPaddings("NoPadding").build());
        setGenerationId(i, i2);
    }

    private static SecretKey generateAesKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    private static KeyStore getAndLoadAndroidKeyStore() throws KeyStoreException {
        KeyStore keyStore = KeyStore.getInstance(KeyStoreProxyImpl.ANDROID_KEY_STORE_PROVIDER);
        try {
            keyStore.load(null);
            return keyStore;
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new KeyStoreException("Unable to load keystore.", e);
        }
    }
}
