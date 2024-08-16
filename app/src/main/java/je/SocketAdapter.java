package je;

import java.util.List;
import javax.net.ssl.SSLSocket;
import kotlin.Metadata;
import zd.Protocol;

/* compiled from: SocketAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H&J\u0010\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H&J(\u0010\r\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u00042\b\u0010\b\u001a\u0004\u0018\u00010\u00072\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\tH&J\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0005\u001a\u00020\u0004H&¨\u0006\u000f"}, d2 = {"Lje/k;", "", "", "b", "Ljavax/net/ssl/SSLSocket;", "sslSocket", "a", "", "hostname", "", "Lzd/y;", "protocols", "Lma/f0;", "d", "c", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: je.k, reason: use source file name */
/* loaded from: classes2.dex */
public interface SocketAdapter {
    boolean a(SSLSocket sslSocket);

    boolean b();

    String c(SSLSocket sslSocket);

    void d(SSLSocket sSLSocket, String str, List<? extends Protocol> list);
}
