package ie;

import android.annotation.SuppressLint;
import android.security.NetworkSecurityPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509TrustManager;
import je.Android10SocketAdapter;
import je.AndroidCertificateChainCleaner;
import je.AndroidSocketAdapter;
import je.BouncyCastleSocketAdapter;
import je.ConscryptSocketAdapter;
import je.DeferredSocketAdapter;
import je.SocketAdapter;
import kotlin.Metadata;
import kotlin.collections.r;
import le.CertificateChainCleaner;
import za.DefaultConstructorMarker;
import za.k;
import zd.Protocol;

/* compiled from: Android10Platform.kt */
@Metadata(bv = {}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0014B\u0007¢\u0006\u0004\b\u0012\u0010\u0013J(\u0010\n\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u00042\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016J\u0012\u0010\u000b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\r\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u0004H\u0017J\u0010\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u000eH\u0016¨\u0006\u0015"}, d2 = {"Lie/a;", "Lie/h;", "Ljavax/net/ssl/SSLSocket;", "sslSocket", "", "hostname", "", "Lzd/y;", "protocols", "Lma/f0;", "e", "g", "", "i", "Ljavax/net/ssl/X509TrustManager;", "trustManager", "Lle/c;", "c", "<init>", "()V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ie.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class Android10Platform extends Platform {

    /* renamed from: e, reason: collision with root package name */
    public static final a f12840e = new a(null);

    /* renamed from: f, reason: collision with root package name */
    private static final boolean f12841f;

    /* renamed from: d, reason: collision with root package name */
    private final List<SocketAdapter> f12842d;

    /* compiled from: Android10Platform.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\t\u0010\nJ\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002R\u0017\u0010\u0005\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\u000b"}, d2 = {"Lie/a$a;", "", "Lie/h;", "a", "", "isSupported", "Z", "b", "()Z", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ie.a$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Platform a() {
            if (b()) {
                return new Android10Platform();
            }
            return null;
        }

        public final boolean b() {
            return Android10Platform.f12841f;
        }
    }

    static {
        f12841f = Platform.f12870a.h();
    }

    public Android10Platform() {
        List o10;
        o10 = r.o(Android10SocketAdapter.f13906a.a(), new DeferredSocketAdapter(AndroidSocketAdapter.f13914f.d()), new DeferredSocketAdapter(ConscryptSocketAdapter.f13928a.a()), new DeferredSocketAdapter(BouncyCastleSocketAdapter.f13922a.a()));
        ArrayList arrayList = new ArrayList();
        for (Object obj : o10) {
            if (((SocketAdapter) obj).b()) {
                arrayList.add(obj);
            }
        }
        this.f12842d = arrayList;
    }

    @Override // ie.Platform
    public CertificateChainCleaner c(X509TrustManager trustManager) {
        k.e(trustManager, "trustManager");
        AndroidCertificateChainCleaner a10 = AndroidCertificateChainCleaner.f13907d.a(trustManager);
        return a10 == null ? super.c(trustManager) : a10;
    }

    @Override // ie.Platform
    public void e(SSLSocket sSLSocket, String str, List<? extends Protocol> list) {
        Object obj;
        k.e(sSLSocket, "sslSocket");
        k.e(list, "protocols");
        Iterator<T> it = this.f12842d.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            } else {
                obj = it.next();
                if (((SocketAdapter) obj).a(sSLSocket)) {
                    break;
                }
            }
        }
        SocketAdapter socketAdapter = (SocketAdapter) obj;
        if (socketAdapter == null) {
            return;
        }
        socketAdapter.d(sSLSocket, str, list);
    }

    @Override // ie.Platform
    public String g(SSLSocket sslSocket) {
        Object obj;
        k.e(sslSocket, "sslSocket");
        Iterator<T> it = this.f12842d.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (((SocketAdapter) obj).a(sslSocket)) {
                break;
            }
        }
        SocketAdapter socketAdapter = (SocketAdapter) obj;
        if (socketAdapter == null) {
            return null;
        }
        return socketAdapter.c(sslSocket);
    }

    @Override // ie.Platform
    @SuppressLint({"NewApi"})
    public boolean i(String hostname) {
        k.e(hostname, "hostname");
        return NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted(hostname);
    }
}
