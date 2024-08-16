package ie;

import java.util.List;
import java.util.Objects;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import kotlin.Metadata;
import sd.StringNumberConversions;
import za.DefaultConstructorMarker;
import za.k;
import zd.Protocol;

/* compiled from: Jdk9Platform.kt */
@Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0016\u0018\u00002\u00020\u0001:\u0001\u000eB\u0007¢\u0006\u0004\b\f\u0010\rJ(\u0010\n\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u00042\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0017J\u0012\u0010\u000b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0017¨\u0006\u000f"}, d2 = {"Lie/f;", "Lie/h;", "Ljavax/net/ssl/SSLSocket;", "sslSocket", "", "hostname", "", "Lzd/y;", "protocols", "Lma/f0;", "e", "g", "<init>", "()V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ie.f, reason: use source file name */
/* loaded from: classes2.dex */
public class Jdk9Platform extends Platform {

    /* renamed from: d, reason: collision with root package name */
    public static final a f12865d = new a(0 == true ? 1 : 0);

    /* renamed from: e, reason: collision with root package name */
    private static final boolean f12866e;

    /* compiled from: Jdk9Platform.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\t\u0010\nJ\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002R\u0017\u0010\u0005\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\u000b"}, d2 = {"Lie/f$a;", "", "Lie/f;", "a", "", "isAvailable", "Z", "b", "()Z", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ie.f$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Jdk9Platform a() {
            if (b()) {
                return new Jdk9Platform();
            }
            return null;
        }

        public final boolean b() {
            return Jdk9Platform.f12866e;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x001f, code lost:
    
        if (r1.intValue() >= 9) goto L13;
     */
    static {
        String property = System.getProperty("java.specification.version");
        Integer j10 = property != null ? StringNumberConversions.j(property) : null;
        boolean z10 = true;
        if (j10 == null) {
            try {
                SSLSocket.class.getMethod("getApplicationProtocol", new Class[0]);
            } catch (NoSuchMethodException unused) {
                z10 = false;
                f12866e = z10;
            }
        }
        f12866e = z10;
    }

    @Override // ie.Platform
    public void e(SSLSocket sSLSocket, String str, List<Protocol> list) {
        k.e(sSLSocket, "sslSocket");
        k.e(list, "protocols");
        SSLParameters sSLParameters = sSLSocket.getSSLParameters();
        Object[] array = Platform.f12870a.b(list).toArray(new String[0]);
        Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        sSLParameters.setApplicationProtocols((String[]) array);
        sSLSocket.setSSLParameters(sSLParameters);
    }

    @Override // ie.Platform
    public String g(SSLSocket sslSocket) {
        k.e(sslSocket, "sslSocket");
        try {
            String applicationProtocol = sslSocket.getApplicationProtocol();
            if (applicationProtocol == null ? true : k.a(applicationProtocol, "")) {
                return null;
            }
            return applicationProtocol;
        } catch (UnsupportedOperationException unused) {
            return null;
        }
    }
}
