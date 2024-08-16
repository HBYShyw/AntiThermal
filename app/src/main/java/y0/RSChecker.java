package y0;

import java.security.KeyException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/* compiled from: RSChecker.java */
/* renamed from: y0.g, reason: use source file name */
/* loaded from: classes.dex */
public abstract class RSChecker {
    public X509Certificate a(X509Certificate x509Certificate) {
        return x509Certificate;
    }

    public X509Certificate b(byte[] bArr) {
        throw new CertificateException("Please set the check of certificate sn correctly");
    }

    public PublicKey c(PublicKey publicKey) {
        return publicKey;
    }

    public PublicKey d(byte[] bArr) {
        throw new KeyException("Please set the check of public key id correctly");
    }
}
