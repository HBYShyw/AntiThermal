package je;

import java.util.List;
import javax.net.ssl.SSLSocket;
import kotlin.Metadata;
import zd.Protocol;

/* compiled from: DeferredSocketAdapter.kt */
@Metadata(bv = {}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001\u0007B\u000f\u0012\u0006\u0010\u0011\u001a\u00020\u0010¢\u0006\u0004\b\u0012\u0010\u0013J\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\b\u0010\u0006\u001a\u00020\u0005H\u0016J\u0010\u0010\u0007\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J(\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\t\u001a\u0004\u0018\u00010\b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0016J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0014"}, d2 = {"Lje/j;", "Lje/k;", "Ljavax/net/ssl/SSLSocket;", "sslSocket", "e", "", "b", "a", "", "hostname", "", "Lzd/y;", "protocols", "Lma/f0;", "d", "c", "Lje/j$a;", "socketAdapterFactory", "<init>", "(Lje/j$a;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: je.j, reason: use source file name */
/* loaded from: classes2.dex */
public final class DeferredSocketAdapter implements SocketAdapter {

    /* renamed from: a, reason: collision with root package name */
    private final a f13930a;

    /* renamed from: b, reason: collision with root package name */
    private SocketAdapter f13931b;

    /* compiled from: DeferredSocketAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H&J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0002H&¨\u0006\b"}, d2 = {"Lje/j$a;", "", "Ljavax/net/ssl/SSLSocket;", "sslSocket", "", "a", "Lje/k;", "b", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: je.j$a */
    /* loaded from: classes2.dex */
    public interface a {
        boolean a(SSLSocket sslSocket);

        SocketAdapter b(SSLSocket sslSocket);
    }

    public DeferredSocketAdapter(a aVar) {
        za.k.e(aVar, "socketAdapterFactory");
        this.f13930a = aVar;
    }

    private final synchronized SocketAdapter e(SSLSocket sslSocket) {
        if (this.f13931b == null && this.f13930a.a(sslSocket)) {
            this.f13931b = this.f13930a.b(sslSocket);
        }
        return this.f13931b;
    }

    @Override // je.SocketAdapter
    public boolean a(SSLSocket sslSocket) {
        za.k.e(sslSocket, "sslSocket");
        return this.f13930a.a(sslSocket);
    }

    @Override // je.SocketAdapter
    public boolean b() {
        return true;
    }

    @Override // je.SocketAdapter
    public String c(SSLSocket sslSocket) {
        za.k.e(sslSocket, "sslSocket");
        SocketAdapter e10 = e(sslSocket);
        if (e10 == null) {
            return null;
        }
        return e10.c(sslSocket);
    }

    @Override // je.SocketAdapter
    public void d(SSLSocket sSLSocket, String str, List<? extends Protocol> list) {
        za.k.e(sSLSocket, "sslSocket");
        za.k.e(list, "protocols");
        SocketAdapter e10 = e(sSLSocket);
        if (e10 == null) {
            return;
        }
        e10.d(sSLSocket, str, list);
    }
}
