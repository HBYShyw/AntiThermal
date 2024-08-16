package com.oplus.oms.split.signature;

import java.security.cert.X509Certificate;

/* loaded from: classes.dex */
final class X509CertificateEx extends X509CertificateWrapper {
    private byte[] a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public X509CertificateEx(X509Certificate var1, byte[] var2) {
        super(var1);
        this.a = var2;
    }

    @Override // com.oplus.oms.split.signature.X509CertificateWrapper, java.security.cert.Certificate
    public byte[] getEncoded() {
        return this.a;
    }
}
