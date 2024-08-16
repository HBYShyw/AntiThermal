package ee;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.UnknownServiceException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import kotlin.Metadata;
import za.k;
import zd.ConnectionSpec;

/* compiled from: ConnectionSpecSelector.kt */
@Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0004\b\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\f¢\u0006\u0004\b\u000e\u0010\u000fJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u000e\u0010\b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0002J\u000e\u0010\u000b\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\t¨\u0006\u0010"}, d2 = {"Lee/b;", "", "Ljavax/net/ssl/SSLSocket;", "socket", "", "c", "sslSocket", "Lzd/l;", "a", "Ljava/io/IOException;", "e", "b", "", "connectionSpecs", "<init>", "(Ljava/util/List;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ee.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class ConnectionSpecSelector {

    /* renamed from: a, reason: collision with root package name */
    private final List<ConnectionSpec> f11155a;

    /* renamed from: b, reason: collision with root package name */
    private int f11156b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f11157c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f11158d;

    public ConnectionSpecSelector(List<ConnectionSpec> list) {
        k.e(list, "connectionSpecs");
        this.f11155a = list;
    }

    private final boolean c(SSLSocket socket) {
        int i10 = this.f11156b;
        int size = this.f11155a.size();
        while (i10 < size) {
            int i11 = i10 + 1;
            if (this.f11155a.get(i10).e(socket)) {
                return true;
            }
            i10 = i11;
        }
        return false;
    }

    public final ConnectionSpec a(SSLSocket sslSocket) {
        ConnectionSpec connectionSpec;
        k.e(sslSocket, "sslSocket");
        int i10 = this.f11156b;
        int size = this.f11155a.size();
        while (true) {
            if (i10 >= size) {
                connectionSpec = null;
                break;
            }
            int i11 = i10 + 1;
            connectionSpec = this.f11155a.get(i10);
            if (connectionSpec.e(sslSocket)) {
                this.f11156b = i11;
                break;
            }
            i10 = i11;
        }
        if (connectionSpec != null) {
            this.f11157c = c(sslSocket);
            connectionSpec.c(sslSocket, this.f11158d);
            return connectionSpec;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Unable to find acceptable protocols. isFallback=");
        sb2.append(this.f11158d);
        sb2.append(", modes=");
        sb2.append(this.f11155a);
        sb2.append(", supported protocols=");
        String[] enabledProtocols = sslSocket.getEnabledProtocols();
        k.b(enabledProtocols);
        String arrays = Arrays.toString(enabledProtocols);
        k.d(arrays, "toString(this)");
        sb2.append(arrays);
        throw new UnknownServiceException(sb2.toString());
    }

    public final boolean b(IOException e10) {
        k.e(e10, "e");
        this.f11158d = true;
        return (!this.f11157c || (e10 instanceof ProtocolException) || (e10 instanceof InterruptedIOException) || ((e10 instanceof SSLHandshakeException) && (e10.getCause() instanceof CertificateException)) || (e10 instanceof SSLPeerUnverifiedException) || !(e10 instanceof SSLException)) ? false : true;
    }
}
