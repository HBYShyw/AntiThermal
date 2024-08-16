package zd;

import java.net.InetSocketAddress;
import java.net.Proxy;
import kotlin.Metadata;

/* compiled from: Route.kt */
@Metadata(bv = {}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u000b\u001a\u00020\n\u0012\u0006\u0010\u0010\u001a\u00020\u000f\u0012\u0006\u0010\u0015\u001a\u00020\u0014¢\u0006\u0004\b\u0019\u0010\u001aJ\u0006\u0010\u0003\u001a\u00020\u0002J\u0013\u0010\u0005\u001a\u00020\u00022\b\u0010\u0004\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\t\u001a\u00020\bH\u0016R\u0017\u0010\u000b\u001a\u00020\n8\u0007¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u0010\u001a\u00020\u000f8\u0007¢\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013R\u0017\u0010\u0015\u001a\u00020\u00148\u0007¢\u0006\f\n\u0004\b\u0015\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018¨\u0006\u001b"}, d2 = {"Lzd/d0;", "", "", "c", "other", "equals", "", "hashCode", "", "toString", "Lzd/a;", "address", "Lzd/a;", "a", "()Lzd/a;", "Ljava/net/Proxy;", "proxy", "Ljava/net/Proxy;", "b", "()Ljava/net/Proxy;", "Ljava/net/InetSocketAddress;", "socketAddress", "Ljava/net/InetSocketAddress;", "d", "()Ljava/net/InetSocketAddress;", "<init>", "(Lzd/a;Ljava/net/Proxy;Ljava/net/InetSocketAddress;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class d0 {

    /* renamed from: a, reason: collision with root package name */
    private final Address f20560a;

    /* renamed from: b, reason: collision with root package name */
    private final Proxy f20561b;

    /* renamed from: c, reason: collision with root package name */
    private final InetSocketAddress f20562c;

    public d0(Address address, Proxy proxy, InetSocketAddress inetSocketAddress) {
        za.k.e(address, "address");
        za.k.e(proxy, "proxy");
        za.k.e(inetSocketAddress, "socketAddress");
        this.f20560a = address;
        this.f20561b = proxy;
        this.f20562c = inetSocketAddress;
    }

    /* renamed from: a, reason: from getter */
    public final Address getF20560a() {
        return this.f20560a;
    }

    /* renamed from: b, reason: from getter */
    public final Proxy getF20561b() {
        return this.f20561b;
    }

    public final boolean c() {
        return this.f20560a.getF20487c() != null && this.f20561b.type() == Proxy.Type.HTTP;
    }

    /* renamed from: d, reason: from getter */
    public final InetSocketAddress getF20562c() {
        return this.f20562c;
    }

    public boolean equals(Object other) {
        if (other instanceof d0) {
            d0 d0Var = (d0) other;
            if (za.k.a(d0Var.f20560a, this.f20560a) && za.k.a(d0Var.f20561b, this.f20561b) && za.k.a(d0Var.f20562c, this.f20562c)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return ((((527 + this.f20560a.hashCode()) * 31) + this.f20561b.hashCode()) * 31) + this.f20562c.hashCode();
    }

    public String toString() {
        return "Route{" + this.f20562c + '}';
    }
}
