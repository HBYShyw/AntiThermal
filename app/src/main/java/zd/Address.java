package zd;

import com.coui.appcompat.touchsearchview.COUIAccessibilityUtil;
import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;
import java.util.Objects;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import kotlin.Metadata;
import zd.HttpUrl;

/* compiled from: Address.kt */
@Metadata(bv = {}, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B{\u0012\u0006\u0010B\u001a\u00020\n\u0012\u0006\u0010C\u001a\u00020\u0005\u0012\u0006\u0010\r\u001a\u00020\f\u0012\u0006\u0010\u0012\u001a\u00020\u0011\u0012\b\u0010\u0017\u001a\u0004\u0018\u00010\u0016\u0012\b\u0010\u001c\u001a\u0004\u0018\u00010\u001b\u0012\b\u0010!\u001a\u0004\u0018\u00010 \u0012\u0006\u0010&\u001a\u00020%\u0012\b\u0010+\u001a\u0004\u0018\u00010*\u0012\f\u0010;\u001a\b\u0012\u0004\u0012\u00020:09\u0012\f\u0010@\u001a\b\u0012\u0004\u0012\u00020?09\u0012\u0006\u00100\u001a\u00020/¢\u0006\u0004\bD\u0010EJ\u0013\u0010\u0004\u001a\u00020\u00032\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0006\u001a\u00020\u0005H\u0016J\u0017\u0010\b\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0000H\u0000¢\u0006\u0004\b\b\u0010\tJ\b\u0010\u000b\u001a\u00020\nH\u0016R\u0017\u0010\r\u001a\u00020\f8\u0007¢\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0012\u001a\u00020\u00118\u0007¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015R\u0019\u0010\u0017\u001a\u0004\u0018\u00010\u00168\u0007¢\u0006\f\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0019\u0010\u001aR\u0019\u0010\u001c\u001a\u0004\u0018\u00010\u001b8\u0007¢\u0006\f\n\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001e\u0010\u001fR\u0019\u0010!\u001a\u0004\u0018\u00010 8\u0007¢\u0006\f\n\u0004\b!\u0010\"\u001a\u0004\b#\u0010$R\u0017\u0010&\u001a\u00020%8\u0007¢\u0006\f\n\u0004\b&\u0010'\u001a\u0004\b(\u0010)R\u0019\u0010+\u001a\u0004\u0018\u00010*8\u0007¢\u0006\f\n\u0004\b+\u0010,\u001a\u0004\b-\u0010.R\u0017\u00100\u001a\u00020/8\u0007¢\u0006\f\n\u0004\b0\u00101\u001a\u0004\b2\u00103R\u0017\u00105\u001a\u0002048G¢\u0006\f\n\u0004\b5\u00106\u001a\u0004\b7\u00108R\u001d\u0010;\u001a\b\u0012\u0004\u0012\u00020:098G¢\u0006\f\n\u0004\b;\u0010<\u001a\u0004\b=\u0010>R\u001d\u0010@\u001a\b\u0012\u0004\u0012\u00020?098G¢\u0006\f\n\u0004\b@\u0010<\u001a\u0004\bA\u0010>¨\u0006F"}, d2 = {"Lzd/a;", "", "other", "", "equals", "", "hashCode", "that", "d", "(Lzd/a;)Z", "", "toString", "Lzd/q;", "dns", "Lzd/q;", "c", "()Lzd/q;", "Ljavax/net/SocketFactory;", "socketFactory", "Ljavax/net/SocketFactory;", "j", "()Ljavax/net/SocketFactory;", "Ljavax/net/ssl/SSLSocketFactory;", "sslSocketFactory", "Ljavax/net/ssl/SSLSocketFactory;", "k", "()Ljavax/net/ssl/SSLSocketFactory;", "Ljavax/net/ssl/HostnameVerifier;", "hostnameVerifier", "Ljavax/net/ssl/HostnameVerifier;", "e", "()Ljavax/net/ssl/HostnameVerifier;", "Lzd/g;", "certificatePinner", "Lzd/g;", "a", "()Lzd/g;", "Lzd/b;", "proxyAuthenticator", "Lzd/b;", "h", "()Lzd/b;", "Ljava/net/Proxy;", "proxy", "Ljava/net/Proxy;", "g", "()Ljava/net/Proxy;", "Ljava/net/ProxySelector;", "proxySelector", "Ljava/net/ProxySelector;", "i", "()Ljava/net/ProxySelector;", "Lzd/u;", "url", "Lzd/u;", "l", "()Lzd/u;", "", "Lzd/y;", "protocols", "Ljava/util/List;", "f", "()Ljava/util/List;", "Lzd/l;", "connectionSpecs", "b", "uriHost", "uriPort", "<init>", "(Ljava/lang/String;ILzd/q;Ljavax/net/SocketFactory;Ljavax/net/ssl/SSLSocketFactory;Ljavax/net/ssl/HostnameVerifier;Lzd/g;Lzd/b;Ljava/net/Proxy;Ljava/util/List;Ljava/util/List;Ljava/net/ProxySelector;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class Address {

    /* renamed from: a, reason: collision with root package name */
    private final Dns f20485a;

    /* renamed from: b, reason: collision with root package name */
    private final SocketFactory f20486b;

    /* renamed from: c, reason: collision with root package name */
    private final SSLSocketFactory f20487c;

    /* renamed from: d, reason: collision with root package name */
    private final HostnameVerifier f20488d;

    /* renamed from: e, reason: collision with root package name */
    private final CertificatePinner f20489e;

    /* renamed from: f, reason: collision with root package name */
    private final Authenticator f20490f;

    /* renamed from: g, reason: collision with root package name */
    private final Proxy f20491g;

    /* renamed from: h, reason: collision with root package name */
    private final ProxySelector f20492h;

    /* renamed from: i, reason: collision with root package name */
    private final HttpUrl f20493i;

    /* renamed from: j, reason: collision with root package name */
    private final List<Protocol> f20494j;

    /* renamed from: k, reason: collision with root package name */
    private final List<ConnectionSpec> f20495k;

    public Address(String str, int i10, Dns dns, SocketFactory socketFactory, SSLSocketFactory sSLSocketFactory, HostnameVerifier hostnameVerifier, CertificatePinner certificatePinner, Authenticator authenticator, Proxy proxy, List<? extends Protocol> list, List<ConnectionSpec> list2, ProxySelector proxySelector) {
        za.k.e(str, "uriHost");
        za.k.e(dns, "dns");
        za.k.e(socketFactory, "socketFactory");
        za.k.e(authenticator, "proxyAuthenticator");
        za.k.e(list, "protocols");
        za.k.e(list2, "connectionSpecs");
        za.k.e(proxySelector, "proxySelector");
        this.f20485a = dns;
        this.f20486b = socketFactory;
        this.f20487c = sSLSocketFactory;
        this.f20488d = hostnameVerifier;
        this.f20489e = certificatePinner;
        this.f20490f = authenticator;
        this.f20491g = proxy;
        this.f20492h = proxySelector;
        this.f20493i = new HttpUrl.a().v(sSLSocketFactory != null ? "https" : "http").l(str).r(i10).a();
        this.f20494j = ae.d.R(list);
        this.f20495k = ae.d.R(list2);
    }

    /* renamed from: a, reason: from getter */
    public final CertificatePinner getF20489e() {
        return this.f20489e;
    }

    public final List<ConnectionSpec> b() {
        return this.f20495k;
    }

    /* renamed from: c, reason: from getter */
    public final Dns getF20485a() {
        return this.f20485a;
    }

    public final boolean d(Address that) {
        za.k.e(that, "that");
        return za.k.a(this.f20485a, that.f20485a) && za.k.a(this.f20490f, that.f20490f) && za.k.a(this.f20494j, that.f20494j) && za.k.a(this.f20495k, that.f20495k) && za.k.a(this.f20492h, that.f20492h) && za.k.a(this.f20491g, that.f20491g) && za.k.a(this.f20487c, that.f20487c) && za.k.a(this.f20488d, that.f20488d) && za.k.a(this.f20489e, that.f20489e) && this.f20493i.getF20717e() == that.f20493i.getF20717e();
    }

    /* renamed from: e, reason: from getter */
    public final HostnameVerifier getF20488d() {
        return this.f20488d;
    }

    public boolean equals(Object other) {
        if (other instanceof Address) {
            Address address = (Address) other;
            if (za.k.a(this.f20493i, address.f20493i) && d(address)) {
                return true;
            }
        }
        return false;
    }

    public final List<Protocol> f() {
        return this.f20494j;
    }

    /* renamed from: g, reason: from getter */
    public final Proxy getF20491g() {
        return this.f20491g;
    }

    /* renamed from: h, reason: from getter */
    public final Authenticator getF20490f() {
        return this.f20490f;
    }

    public int hashCode() {
        return ((((((((((((((((((527 + this.f20493i.hashCode()) * 31) + this.f20485a.hashCode()) * 31) + this.f20490f.hashCode()) * 31) + this.f20494j.hashCode()) * 31) + this.f20495k.hashCode()) * 31) + this.f20492h.hashCode()) * 31) + Objects.hashCode(this.f20491g)) * 31) + Objects.hashCode(this.f20487c)) * 31) + Objects.hashCode(this.f20488d)) * 31) + Objects.hashCode(this.f20489e);
    }

    /* renamed from: i, reason: from getter */
    public final ProxySelector getF20492h() {
        return this.f20492h;
    }

    /* renamed from: j, reason: from getter */
    public final SocketFactory getF20486b() {
        return this.f20486b;
    }

    /* renamed from: k, reason: from getter */
    public final SSLSocketFactory getF20487c() {
        return this.f20487c;
    }

    /* renamed from: l, reason: from getter */
    public final HttpUrl getF20493i() {
        return this.f20493i;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Address{");
        sb2.append(this.f20493i.getF20716d());
        sb2.append(COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        sb2.append(this.f20493i.getF20717e());
        sb2.append(", ");
        Proxy proxy = this.f20491g;
        sb2.append(proxy != null ? za.k.l("proxy=", proxy) : za.k.l("proxySelector=", this.f20492h));
        sb2.append('}');
        return sb2.toString();
    }
}
