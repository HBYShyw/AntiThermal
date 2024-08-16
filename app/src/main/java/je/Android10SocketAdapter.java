package je;

import android.annotation.SuppressLint;
import android.net.ssl.SSLSockets;
import ie.Platform;
import java.io.IOException;
import java.util.List;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import zd.Protocol;

/* compiled from: Android10SocketAdapter.kt */
@Metadata(bv = {}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0005B\u0007¢\u0006\u0004\b\u000f\u0010\u0010J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\b\u0010\u0006\u001a\u00020\u0004H\u0016J\u0012\u0010\b\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0003\u001a\u00020\u0002H\u0017J(\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\t\u001a\u0004\u0018\u00010\u00072\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0017¨\u0006\u0011"}, d2 = {"Lje/a;", "Lje/k;", "Ljavax/net/ssl/SSLSocket;", "sslSocket", "", "a", "b", "", "c", "hostname", "", "Lzd/y;", "protocols", "Lma/f0;", "d", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
@SuppressLint({"NewApi"})
/* renamed from: je.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class Android10SocketAdapter implements SocketAdapter {

    /* renamed from: a, reason: collision with root package name */
    public static final a f13906a = new a(null);

    /* compiled from: Android10SocketAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002J\u0006\u0010\u0005\u001a\u00020\u0004¨\u0006\b"}, d2 = {"Lje/a$a;", "", "Lje/k;", "a", "", "b", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: je.a$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final SocketAdapter a() {
            if (b()) {
                return new Android10SocketAdapter();
            }
            return null;
        }

        public final boolean b() {
            return Platform.f12870a.h();
        }
    }

    @Override // je.SocketAdapter
    public boolean a(SSLSocket sslSocket) {
        za.k.e(sslSocket, "sslSocket");
        return SSLSockets.isSupportedSocket(sslSocket);
    }

    @Override // je.SocketAdapter
    public boolean b() {
        return f13906a.b();
    }

    @Override // je.SocketAdapter
    @SuppressLint({"NewApi"})
    public String c(SSLSocket sslSocket) {
        za.k.e(sslSocket, "sslSocket");
        String applicationProtocol = sslSocket.getApplicationProtocol();
        if (applicationProtocol == null ? true : za.k.a(applicationProtocol, "")) {
            return null;
        }
        return applicationProtocol;
    }

    @Override // je.SocketAdapter
    @SuppressLint({"NewApi"})
    public void d(SSLSocket sSLSocket, String str, List<? extends Protocol> list) {
        za.k.e(sSLSocket, "sslSocket");
        za.k.e(list, "protocols");
        try {
            SSLSockets.setUseSessionTickets(sSLSocket, true);
            SSLParameters sSLParameters = sSLSocket.getSSLParameters();
            Object[] array = Platform.f12870a.b(list).toArray(new String[0]);
            if (array != null) {
                sSLParameters.setApplicationProtocols((String[]) array);
                sSLSocket.setSSLParameters(sSLParameters);
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        } catch (IllegalArgumentException e10) {
            throw new IOException("Android internal error", e10);
        }
    }
}
