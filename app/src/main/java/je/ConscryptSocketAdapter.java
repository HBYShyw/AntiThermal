package je;

import ie.ConscryptPlatform;
import ie.Platform;
import java.util.List;
import java.util.Objects;
import javax.net.ssl.SSLSocket;
import je.DeferredSocketAdapter;
import kotlin.Metadata;
import org.conscrypt.Conscrypt;
import za.DefaultConstructorMarker;
import zd.Protocol;

/* compiled from: ConscryptSocketAdapter.kt */
@Metadata(bv = {}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001\u0006B\u0007¢\u0006\u0004\b\u000f\u0010\u0010J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\b\u0010\u0006\u001a\u00020\u0004H\u0016J\u0012\u0010\b\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J(\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\t\u001a\u0004\u0018\u00010\u00072\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0016¨\u0006\u0011"}, d2 = {"Lje/i;", "Lje/k;", "Ljavax/net/ssl/SSLSocket;", "sslSocket", "", "a", "b", "", "c", "hostname", "", "Lzd/y;", "protocols", "Lma/f0;", "d", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: je.i, reason: use source file name */
/* loaded from: classes2.dex */
public final class ConscryptSocketAdapter implements SocketAdapter {

    /* renamed from: a, reason: collision with root package name */
    public static final b f13928a = new b(null);

    /* renamed from: b, reason: collision with root package name */
    private static final DeferredSocketAdapter.a f13929b = new a();

    /* compiled from: ConscryptSocketAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\b"}, d2 = {"je/i$a", "Lje/j$a;", "Ljavax/net/ssl/SSLSocket;", "sslSocket", "", "a", "Lje/k;", "b", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: je.i$a */
    /* loaded from: classes2.dex */
    public static final class a implements DeferredSocketAdapter.a {
        a() {
        }

        @Override // je.DeferredSocketAdapter.a
        public boolean a(SSLSocket sslSocket) {
            za.k.e(sslSocket, "sslSocket");
            return ConscryptPlatform.f12852e.c() && Conscrypt.isConscrypt(sslSocket);
        }

        @Override // je.DeferredSocketAdapter.a
        public SocketAdapter b(SSLSocket sslSocket) {
            za.k.e(sslSocket, "sslSocket");
            return new ConscryptSocketAdapter();
        }
    }

    /* compiled from: ConscryptSocketAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0007\u0010\bR\u0017\u0010\u0003\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006¨\u0006\t"}, d2 = {"Lje/i$b;", "", "Lje/j$a;", "factory", "Lje/j$a;", "a", "()Lje/j$a;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: je.i$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final DeferredSocketAdapter.a a() {
            return ConscryptSocketAdapter.f13929b;
        }
    }

    @Override // je.SocketAdapter
    public boolean a(SSLSocket sslSocket) {
        za.k.e(sslSocket, "sslSocket");
        return Conscrypt.isConscrypt(sslSocket);
    }

    @Override // je.SocketAdapter
    public boolean b() {
        return ConscryptPlatform.f12852e.c();
    }

    @Override // je.SocketAdapter
    public String c(SSLSocket sslSocket) {
        za.k.e(sslSocket, "sslSocket");
        if (a(sslSocket)) {
            return Conscrypt.getApplicationProtocol(sslSocket);
        }
        return null;
    }

    @Override // je.SocketAdapter
    public void d(SSLSocket sSLSocket, String str, List<? extends Protocol> list) {
        za.k.e(sSLSocket, "sslSocket");
        za.k.e(list, "protocols");
        if (a(sSLSocket)) {
            Conscrypt.setUseSessionTickets(sSLSocket, true);
            Object[] array = Platform.f12870a.b(list).toArray(new String[0]);
            Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
            Conscrypt.setApplicationProtocols(sSLSocket, (String[]) array);
        }
    }
}
