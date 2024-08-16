package a1;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import b1.EcKeyGenParameterSpec;
import b1.KeyPairContainer;
import e1.FileUtil;
import e1.i;
import i0.EncryptException;
import j0.AesUtil;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.json.JSONException;
import s0.CipherContainer;
import s0.CryptoParameters;
import t0.InvalidArgumentException;

/* compiled from: CryptoKeyStore.java */
/* renamed from: a1.c, reason: use source file name */
/* loaded from: classes.dex */
public class CryptoKeyStore {

    /* renamed from: a, reason: collision with root package name */
    private static final ReadWriteLock f29a = new ReentrantReadWriteLock();

    public static SecretKey c(final Context context, String str, final String str2) {
        return d(str, new Runnable() { // from class: a1.b
            @Override // java.lang.Runnable
            public final void run() {
                CryptoKeyStore.j(str2, context);
            }
        });
    }

    private static SecretKey d(String str, Runnable runnable) {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        if (keyStore.containsAlias(str)) {
            return ((KeyStore.SecretKeyEntry) keyStore.getEntry(str, null)).getSecretKey();
        }
        if (runnable != null) {
            runnable.run();
        }
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "AndroidKeyStore");
        keyGenerator.init(new KeyGenParameterSpec.Builder(str, 3).setBlockModes("CTR", "GCM").setEncryptionPaddings("NoPadding").setKeySize(256).build());
        return keyGenerator.generateKey();
    }

    public static SecretKey e(final Context context, String str, final String str2) {
        try {
            return d(str, new Runnable() { // from class: a1.a
                @Override // java.lang.Runnable
                public final void run() {
                    CryptoKeyStore.k(str2, context);
                }
            });
        } catch (IOException | InvalidAlgorithmParameterException | KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | UnrecoverableEntryException | CertificateException e10) {
            i.b("CryptoKeyStore", "createOrGetSecretKeyToSP error. " + e10);
            throw new KeyStoreException(e10);
        }
    }

    public static void f(Context context, String str) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if (keyStore.containsAlias(str)) {
                i.a("CryptoKeyStore", "deleteEcKeyPair key pair is recorded in the android keystore, delete now, alias = " + str);
                keyStore.deleteEntry(str);
                return;
            }
            File c10 = FileUtil.c(FileUtil.a("eckeypairstore", str), context);
            if (c10.exists()) {
                i.a("CryptoKeyStore", "deleteEcKeyPair key pair is recorded in the private directory, delete now, alias = " + str);
                c10.delete();
            }
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e10) {
            throw new EncryptException(e10);
        }
    }

    public static void g(String str) {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        if (keyStore.containsAlias(str)) {
            keyStore.deleteEntry(str);
        }
    }

    public static KeyPair h(Context context, EcKeyGenParameterSpec ecKeyGenParameterSpec) {
        try {
            String b10 = ecKeyGenParameterSpec.b();
            int i10 = 64;
            int c10 = ecKeyGenParameterSpec.c() & 64;
            if ((ecKeyGenParameterSpec.c() & 64) != 0 && (ecKeyGenParameterSpec.c() & 4) != 0) {
                i10 = 68;
            } else if ((ecKeyGenParameterSpec.c() & 64) == 0) {
                i10 = 4;
            }
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "AndroidKeyStore");
            KeyGenParameterSpec.Builder digests = new KeyGenParameterSpec.Builder(ecKeyGenParameterSpec.b(), i10).setAlgorithmParameterSpec(new ECGenParameterSpec(ecKeyGenParameterSpec.d())).setDigests("NONE", "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512");
            if (ecKeyGenParameterSpec.a() != null) {
                digests.setCertificateNotAfter(ecKeyGenParameterSpec.a());
            }
            keyPairGenerator.initialize(digests.build());
            File c11 = FileUtil.c(FileUtil.a("eckeypairstore", b10), context);
            if (c11.exists()) {
                c11.delete();
            }
            i.a("CryptoKeyStore", "generateEcKeyPair generate success(android keystore solution), alias = " + b10);
            return keyPairGenerator.generateKeyPair();
        } catch (IOException | InvalidAlgorithmParameterException | KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | UnrecoverableEntryException | CertificateException | JSONException | InvalidArgumentException e10) {
            throw new EncryptException(e10);
        }
    }

    public static KeyPair i(Context context, String str) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if (keyStore.containsAlias(str)) {
                i.a("CryptoKeyStore", "getEcKeyPair key pair is recorded in the android keystore, alias = " + str);
                X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(str);
                if (x509Certificate.getNotAfter().before(Calendar.getInstance().getTime())) {
                    i.a("CryptoKeyStore", "getEcKeyPair certificate has expired and has been deleted, alias = " + str);
                    keyStore.deleteEntry(str);
                    return null;
                }
                return new KeyPair(x509Certificate.getPublicKey(), (PrivateKey) keyStore.getKey(str, null));
            }
            File c10 = FileUtil.c(FileUtil.a("eckeypairstore", str), context);
            if (c10.exists()) {
                i.a("CryptoKeyStore", "getEcKeyPair key pair is recorded in the private directory, alias = " + str);
                KeyPairContainer l10 = l(context, str);
                if (l10 != null) {
                    if (l10.b()) {
                        i.a("CryptoKeyStore", "getEcKeyPair key pair has expired and has been deleted, alias = " + str);
                        c10.delete();
                        return null;
                    }
                    return l10.a();
                }
            }
            return null;
        } catch (IOException | InvalidAlgorithmParameterException | KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | UnrecoverableEntryException | CertificateException | InvalidKeySpecException | JSONException | InvalidArgumentException e10) {
            throw new EncryptException(e10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void j(String str, Context context) {
        if (str != null) {
            File c10 = FileUtil.c(FileUtil.a(str), context);
            if (c10.exists()) {
                c10.delete();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void k(String str, Context context) {
        if (str != null) {
            FileUtil.b(context, str);
        }
    }

    private static KeyPairContainer l(Context context, String str) {
        List<String> d10;
        KeyPairContainer b10;
        SecretKey c10 = c(context, "pki_sdk_ecKeyPairGen_key", "eckeypairstore");
        if (c10 != null) {
            File c11 = FileUtil.c(FileUtil.a("eckeypairstore", str), context);
            if (c11.exists() && (d10 = FileUtil.d(c11, f29a)) != null) {
                for (String str2 : d10) {
                    CipherContainer a10 = d.a(str2);
                    if (a10 != null && (b10 = d.b(new String(AesUtil.a(new CryptoParameters.c().k(a10.a()).m(c10).l(a10.b()).h()), StandardCharsets.UTF_8))) != null) {
                        b10.c(str2);
                        return b10;
                    }
                }
            }
            return null;
        }
        throw new KeyStoreException("SecretKey generation error, unable to read key pair list.");
    }
}
