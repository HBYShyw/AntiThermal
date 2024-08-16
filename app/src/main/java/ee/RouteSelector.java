package ee;

import com.coui.appcompat.touchsearchview.COUIAccessibilityUtil;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.MutableCollections;
import kotlin.collections.r;
import za.DefaultConstructorMarker;
import za.k;
import zd.Address;
import zd.EventListener;
import zd.HttpUrl;
import zd.d0;

/* compiled from: RouteSelector.kt */
@Metadata(bv = {}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0002\f\tB'\u0012\u0006\u0010\u0010\u001a\u00020\u000f\u0012\u0006\u0010\u0012\u001a\u00020\u0011\u0012\u0006\u0010\u0014\u001a\u00020\u0013\u0012\u0006\u0010\u0016\u001a\u00020\u0015¢\u0006\u0004\b\u0017\u0010\u0018J\u001a\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H\u0002J\b\u0010\t\u001a\u00020\bH\u0002J\b\u0010\n\u001a\u00020\u0004H\u0002J\u0010\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\t\u0010\f\u001a\u00020\bH\u0086\u0002J\t\u0010\u000e\u001a\u00020\rH\u0086\u0002¨\u0006\u0019"}, d2 = {"Lee/j;", "", "Lzd/u;", "url", "Ljava/net/Proxy;", "proxy", "Lma/f0;", "f", "", "b", "d", "e", "a", "Lee/j$b;", "c", "Lzd/a;", "address", "Lee/h;", "routeDatabase", "Lzd/e;", "call", "Lzd/r;", "eventListener", "<init>", "(Lzd/a;Lee/h;Lzd/e;Lzd/r;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ee.j, reason: use source file name */
/* loaded from: classes2.dex */
public final class RouteSelector {

    /* renamed from: i, reason: collision with root package name */
    public static final a f11242i = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final Address f11243a;

    /* renamed from: b, reason: collision with root package name */
    private final RouteDatabase f11244b;

    /* renamed from: c, reason: collision with root package name */
    private final zd.e f11245c;

    /* renamed from: d, reason: collision with root package name */
    private final EventListener f11246d;

    /* renamed from: e, reason: collision with root package name */
    private List<? extends Proxy> f11247e;

    /* renamed from: f, reason: collision with root package name */
    private int f11248f;

    /* renamed from: g, reason: collision with root package name */
    private List<? extends InetSocketAddress> f11249g;

    /* renamed from: h, reason: collision with root package name */
    private final List<d0> f11250h;

    /* compiled from: RouteSelector.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0007\u0010\bR\u0015\u0010\u0006\u001a\u00020\u0003*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\t"}, d2 = {"Lee/j$a;", "", "Ljava/net/InetSocketAddress;", "", "a", "(Ljava/net/InetSocketAddress;)Ljava/lang/String;", "socketHost", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ee.j$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String a(InetSocketAddress inetSocketAddress) {
            k.e(inetSocketAddress, "<this>");
            InetAddress address = inetSocketAddress.getAddress();
            if (address == null) {
                String hostName = inetSocketAddress.getHostName();
                k.d(hostName, "hostName");
                return hostName;
            }
            String hostAddress = address.getHostAddress();
            k.d(hostAddress, "address.hostAddress");
            return hostAddress;
        }
    }

    /* compiled from: RouteSelector.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0015\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\u0006¢\u0006\u0004\b\u000b\u0010\fJ\t\u0010\u0003\u001a\u00020\u0002H\u0086\u0002J\t\u0010\u0005\u001a\u00020\u0004H\u0086\u0002R\u001d\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\u00068\u0006¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\t\u0010\n¨\u0006\r"}, d2 = {"Lee/j$b;", "", "", "b", "Lzd/d0;", "c", "", "routes", "Ljava/util/List;", "a", "()Ljava/util/List;", "<init>", "(Ljava/util/List;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ee.j$b */
    /* loaded from: classes2.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private final List<d0> f11251a;

        /* renamed from: b, reason: collision with root package name */
        private int f11252b;

        public b(List<d0> list) {
            k.e(list, "routes");
            this.f11251a = list;
        }

        public final List<d0> a() {
            return this.f11251a;
        }

        public final boolean b() {
            return this.f11252b < this.f11251a.size();
        }

        public final d0 c() {
            if (b()) {
                List<d0> list = this.f11251a;
                int i10 = this.f11252b;
                this.f11252b = i10 + 1;
                return list.get(i10);
            }
            throw new NoSuchElementException();
        }
    }

    public RouteSelector(Address address, RouteDatabase routeDatabase, zd.e eVar, EventListener eventListener) {
        List<? extends Proxy> j10;
        List<? extends InetSocketAddress> j11;
        k.e(address, "address");
        k.e(routeDatabase, "routeDatabase");
        k.e(eVar, "call");
        k.e(eventListener, "eventListener");
        this.f11243a = address;
        this.f11244b = routeDatabase;
        this.f11245c = eVar;
        this.f11246d = eventListener;
        j10 = r.j();
        this.f11247e = j10;
        j11 = r.j();
        this.f11249g = j11;
        this.f11250h = new ArrayList();
        f(address.getF20493i(), address.getF20491g());
    }

    private final boolean b() {
        return this.f11248f < this.f11247e.size();
    }

    private final Proxy d() {
        if (b()) {
            List<? extends Proxy> list = this.f11247e;
            int i10 = this.f11248f;
            this.f11248f = i10 + 1;
            Proxy proxy = list.get(i10);
            e(proxy);
            return proxy;
        }
        throw new SocketException("No route to " + this.f11243a.getF20493i().getF20716d() + "; exhausted proxy configurations: " + this.f11247e);
    }

    private final void e(Proxy proxy) {
        String f20716d;
        int f20717e;
        ArrayList arrayList = new ArrayList();
        this.f11249g = arrayList;
        if (proxy.type() != Proxy.Type.DIRECT && proxy.type() != Proxy.Type.SOCKS) {
            SocketAddress address = proxy.address();
            if (address instanceof InetSocketAddress) {
                a aVar = f11242i;
                k.d(address, "proxyAddress");
                InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
                f20716d = aVar.a(inetSocketAddress);
                f20717e = inetSocketAddress.getPort();
            } else {
                throw new IllegalArgumentException(k.l("Proxy.address() is not an InetSocketAddress: ", address.getClass()).toString());
            }
        } else {
            f20716d = this.f11243a.getF20493i().getF20716d();
            f20717e = this.f11243a.getF20493i().getF20717e();
        }
        boolean z10 = false;
        if (1 <= f20717e && f20717e < 65536) {
            z10 = true;
        }
        if (z10) {
            if (proxy.type() == Proxy.Type.SOCKS) {
                arrayList.add(InetSocketAddress.createUnresolved(f20716d, f20717e));
                return;
            }
            this.f11246d.m(this.f11245c, f20716d);
            List<InetAddress> a10 = this.f11243a.getF20485a().a(f20716d);
            if (!a10.isEmpty()) {
                this.f11246d.l(this.f11245c, f20716d, a10);
                Iterator<InetAddress> it = a10.iterator();
                while (it.hasNext()) {
                    arrayList.add(new InetSocketAddress(it.next(), f20717e));
                }
                return;
            }
            throw new UnknownHostException(this.f11243a.getF20485a() + " returned no addresses for " + f20716d);
        }
        throw new SocketException("No route to " + f20716d + COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR + f20717e + "; port is out of range");
    }

    private final void f(HttpUrl httpUrl, Proxy proxy) {
        this.f11246d.o(this.f11245c, httpUrl);
        List<Proxy> g6 = g(proxy, httpUrl, this);
        this.f11247e = g6;
        this.f11248f = 0;
        this.f11246d.n(this.f11245c, httpUrl, g6);
    }

    private static final List<Proxy> g(Proxy proxy, HttpUrl httpUrl, RouteSelector routeSelector) {
        List<Proxy> e10;
        if (proxy != null) {
            e10 = CollectionsJVM.e(proxy);
            return e10;
        }
        URI q10 = httpUrl.q();
        if (q10.getHost() == null) {
            return ae.d.v(Proxy.NO_PROXY);
        }
        List<Proxy> select = routeSelector.f11243a.getF20492h().select(q10);
        if (select == null || select.isEmpty()) {
            return ae.d.v(Proxy.NO_PROXY);
        }
        k.d(select, "proxiesOrNull");
        return ae.d.R(select);
    }

    public final boolean a() {
        return b() || (this.f11250h.isEmpty() ^ true);
    }

    public final b c() {
        if (a()) {
            ArrayList arrayList = new ArrayList();
            while (b()) {
                Proxy d10 = d();
                Iterator<? extends InetSocketAddress> it = this.f11249g.iterator();
                while (it.hasNext()) {
                    d0 d0Var = new d0(this.f11243a, d10, it.next());
                    if (this.f11244b.c(d0Var)) {
                        this.f11250h.add(d0Var);
                    } else {
                        arrayList.add(d0Var);
                    }
                }
                if (!arrayList.isEmpty()) {
                    break;
                }
            }
            if (arrayList.isEmpty()) {
                MutableCollections.z(arrayList, this.f11250h);
                this.f11250h.clear();
            }
            return new b(arrayList);
        }
        throw new NoSuchElementException();
    }
}
