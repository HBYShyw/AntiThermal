package j0;

import android.content.Context;
import c1.CryptoEngCmd;
import c1.TAInterfaceException;
import e1.i;
import i0.EncryptException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import s0.CertParameters;
import t0.InvalidArgumentException;

/* compiled from: CertUtil.java */
/* renamed from: j0.b, reason: use source file name */
/* loaded from: classes.dex */
public class CertUtil {
    public static boolean a(CertParameters certParameters) {
        X509Certificate[] x509CertificateArr;
        try {
            if (certParameters != null) {
                CertParameters.c f10 = certParameters.f();
                if (f10 == CertParameters.c.OPLUS_LIST) {
                    return c(certParameters.b(), certParameters.c());
                }
                X509Certificate[] d10 = certParameters.d();
                if (d10 != null) {
                    x509CertificateArr = new X509Certificate[d10.length + 1];
                    System.arraycopy(d10, 0, x509CertificateArr, 1, d10.length);
                } else {
                    x509CertificateArr = new X509Certificate[1];
                }
                x509CertificateArr[0] = certParameters.c();
                if (f10 == CertParameters.c.SYSTEM_LIST) {
                    return g(null, x509CertificateArr);
                }
                if (f10 != CertParameters.c.THIRD_PARTY_LIST) {
                    return false;
                }
                if (certParameters.e() != null) {
                    return h(certParameters.e(), x509CertificateArr);
                }
                throw new InvalidArgumentException("No third-party root certificate is set");
            }
            throw new InvalidArgumentException("certParameters is null");
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | InvalidArgumentException e10) {
            throw new EncryptException(e10);
        }
    }

    public static boolean b(X509Certificate x509Certificate) {
        try {
            if (x509Certificate != null) {
                try {
                    x509Certificate.checkValidity();
                    return true;
                } catch (CertificateExpiredException | CertificateNotYetValidException e10) {
                    e10.printStackTrace();
                    return false;
                }
            }
            throw new InvalidArgumentException("certificate is null");
        } catch (InvalidArgumentException e11) {
            throw new EncryptException(e11);
        }
    }

    private static boolean c(Context context, X509Certificate x509Certificate) {
        try {
            if (CryptoEngCmd.a(x509Certificate)) {
                if (b(x509Certificate)) {
                    return true;
                }
            }
            return false;
        } catch (TAInterfaceException unused) {
            i.a("CertUtil", "checkOplusCert unable to request ta to verify the certificate chain.");
            if (context != null) {
                InputStream open = context.getAssets().open("crypto_android_sdk/oplus_prod_cert_chain/OPlus_Global_Root_CA_E1.pem");
                X509Certificate d10 = d(open);
                open.close();
                InputStream open2 = context.getAssets().open("crypto_android_sdk/oplus_prod_cert_chain/OPlus_Device_CA_E1.pem");
                X509Certificate d11 = d(open2);
                open2.close();
                InputStream open3 = context.getAssets().open("crypto_android_sdk/oplus_prod_cert_chain/OPlus_Service_CA_E1.pem");
                X509Certificate d12 = d(open3);
                open3.close();
                return h(new X509Certificate[]{d10}, new X509Certificate[]{x509Certificate, d11, d12});
            }
            throw new InvalidArgumentException("context is null");
        }
    }

    public static X509Certificate d(InputStream inputStream) {
        return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(inputStream);
    }

    public static X509Certificate e(byte[] bArr) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        X509Certificate d10 = d(byteArrayInputStream);
        byteArrayInputStream.close();
        return d10;
    }

    public static String f(X509Certificate x509Certificate) {
        try {
            if (x509Certificate != null) {
                Matcher matcher = Pattern.compile("(?:^|,\\s?)(?:CN=(?<val>\"(?:[^\"]|\"\")+\"|[^,]+))").matcher(x509Certificate.getSubjectX500Principal().getName());
                if (matcher.find()) {
                    return matcher.group(1);
                }
                return null;
            }
            throw new InvalidArgumentException("cert is null");
        } catch (InvalidArgumentException e10) {
            throw new EncryptException(e10);
        }
    }

    private static boolean g(KeyStore keyStore, X509Certificate[] x509CertificateArr) {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
        trustManagerFactory.init(keyStore);
        try {
            ((X509TrustManager) trustManagerFactory.getTrustManagers()[0]).checkServerTrusted(x509CertificateArr, "RSA");
            return true;
        } catch (CertificateException e10) {
            i.a("CertUtil", "verityCert e = " + e10.toString());
            e10.printStackTrace();
            return false;
        }
    }

    private static boolean h(X509Certificate[] x509CertificateArr, X509Certificate[] x509CertificateArr2) {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        int i10 = 1;
        for (X509Certificate x509Certificate : x509CertificateArr) {
            keyStore.setCertificateEntry("user:" + i10, x509Certificate);
            i10++;
        }
        return g(keyStore, x509CertificateArr2);
    }
}
