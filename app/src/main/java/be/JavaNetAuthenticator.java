package be;

import java.net.Authenticator;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.List;
import java.util.Objects;
import kotlin.Metadata;
import kotlin.collections._Collections;
import sd.StringsJVM;
import za.DefaultConstructorMarker;
import za.k;
import zd.Address;
import zd.Authenticator;
import zd.Challenge;
import zd.Credentials;
import zd.Dns;
import zd.HttpUrl;
import zd.b0;
import zd.d0;
import zd.z;

/* compiled from: JavaNetAuthenticator.kt */
@Metadata(bv = {}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0011\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0005¢\u0006\u0004\b\u0010\u0010\u0011J\u001c\u0010\b\u001a\u00020\u0007*\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0005H\u0002J\u001c\u0010\u000e\u001a\u0004\u0018\u00010\r2\b\u0010\n\u001a\u0004\u0018\u00010\t2\u0006\u0010\f\u001a\u00020\u000bH\u0016¨\u0006\u0012"}, d2 = {"Lbe/a;", "Lzd/b;", "Ljava/net/Proxy;", "Lzd/u;", "url", "Lzd/q;", "dns", "Ljava/net/InetAddress;", "b", "Lzd/d0;", "route", "Lzd/b0;", "response", "Lzd/z;", "a", "defaultDns", "<init>", "(Lzd/q;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: be.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class JavaNetAuthenticator implements Authenticator {

    /* renamed from: d, reason: collision with root package name */
    private final Dns f4720d;

    /* compiled from: JavaNetAuthenticator.kt */
    @Metadata(k = 3, mv = {1, 6, 0}, xi = 48)
    /* renamed from: be.a$a */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f4721a;

        static {
            int[] iArr = new int[Proxy.Type.values().length];
            iArr[Proxy.Type.DIRECT.ordinal()] = 1;
            f4721a = iArr;
        }
    }

    public JavaNetAuthenticator(Dns dns) {
        k.e(dns, "defaultDns");
        this.f4720d = dns;
    }

    private final InetAddress b(Proxy proxy, HttpUrl httpUrl, Dns dns) {
        Object T;
        Proxy.Type type = proxy.type();
        if ((type == null ? -1 : a.f4721a[type.ordinal()]) == 1) {
            T = _Collections.T(dns.a(httpUrl.getF20716d()));
            return (InetAddress) T;
        }
        SocketAddress address = proxy.address();
        Objects.requireNonNull(address, "null cannot be cast to non-null type java.net.InetSocketAddress");
        InetAddress address2 = ((InetSocketAddress) address).getAddress();
        k.d(address2, "address() as InetSocketAddress).address");
        return address2;
    }

    @Override // zd.Authenticator
    public z a(d0 route, b0 response) {
        boolean r10;
        Address f20560a;
        PasswordAuthentication requestPasswordAuthentication;
        k.e(response, "response");
        List<Challenge> u7 = response.u();
        z f20505e = response.getF20505e();
        HttpUrl f20796a = f20505e.getF20796a();
        boolean z10 = response.getCode() == 407;
        Proxy f20561b = route == null ? null : route.getF20561b();
        if (f20561b == null) {
            f20561b = Proxy.NO_PROXY;
        }
        for (Challenge challenge : u7) {
            r10 = StringsJVM.r("Basic", challenge.getF20582a(), true);
            if (r10) {
                Dns f20485a = (route == null || (f20560a = route.getF20560a()) == null) ? null : f20560a.getF20485a();
                if (f20485a == null) {
                    f20485a = this.f4720d;
                }
                if (z10) {
                    SocketAddress address = f20561b.address();
                    Objects.requireNonNull(address, "null cannot be cast to non-null type java.net.InetSocketAddress");
                    InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
                    String hostName = inetSocketAddress.getHostName();
                    k.d(f20561b, "proxy");
                    requestPasswordAuthentication = java.net.Authenticator.requestPasswordAuthentication(hostName, b(f20561b, f20796a, f20485a), inetSocketAddress.getPort(), f20796a.getF20713a(), challenge.b(), challenge.getF20582a(), f20796a.r(), Authenticator.RequestorType.PROXY);
                } else {
                    String f20716d = f20796a.getF20716d();
                    k.d(f20561b, "proxy");
                    requestPasswordAuthentication = java.net.Authenticator.requestPasswordAuthentication(f20716d, b(f20561b, f20796a, f20485a), f20796a.getF20717e(), f20796a.getF20713a(), challenge.b(), challenge.getF20582a(), f20796a.r(), Authenticator.RequestorType.SERVER);
                }
                if (requestPasswordAuthentication != null) {
                    String str = z10 ? "Proxy-Authorization" : "Authorization";
                    String userName = requestPasswordAuthentication.getUserName();
                    k.d(userName, "auth.userName");
                    char[] password = requestPasswordAuthentication.getPassword();
                    k.d(password, "auth.password");
                    return f20505e.h().d(str, Credentials.a(userName, new String(password), challenge.a())).b();
                }
            }
        }
        return null;
    }

    public /* synthetic */ JavaNetAuthenticator(Dns dns, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((i10 & 1) != 0 ? Dns.f20697b : dns);
    }
}
