package ee;

import ee.RouteSelector;
import fe.ExchangeCodec;
import he.ConnectionShutdownException;
import he.ErrorCode;
import he.StreamResetException;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import kotlin.Metadata;
import ma.Unit;
import za.k;
import zd.Address;
import zd.EventListener;
import zd.HttpUrl;
import zd.OkHttpClient;
import zd.d0;

/* compiled from: ExchangeFinder.kt */
@Metadata(bv = {}, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\"\u001a\u00020!\u0012\u0006\u0010\u001d\u001a\u00020\u001c\u0012\u0006\u0010$\u001a\u00020#\u0012\u0006\u0010&\u001a\u00020%¢\u0006\u0004\b'\u0010(J8\u0010\u000b\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0007H\u0002J0\u0010\f\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0007H\u0002J\n\u0010\u000e\u001a\u0004\u0018\u00010\rH\u0002J\u0016\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u0011J\u000e\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0016\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u0007J\u000e\u0010\u001b\u001a\u00020\u00072\u0006\u0010\u001a\u001a\u00020\u0019R\u001a\u0010\u001d\u001a\u00020\u001c8\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u001d\u0010\u001e\u001a\u0004\b\u001f\u0010 ¨\u0006)"}, d2 = {"Lee/d;", "", "", "connectTimeout", "readTimeout", "writeTimeout", "pingIntervalMillis", "", "connectionRetryEnabled", "doExtensiveHealthChecks", "Lee/f;", "c", "b", "Lzd/d0;", "f", "Lzd/x;", "client", "Lfe/g;", "chain", "Lfe/d;", "a", "Ljava/io/IOException;", "e", "Lma/f0;", "h", "Lzd/u;", "url", "g", "Lzd/a;", "address", "Lzd/a;", "d", "()Lzd/a;", "Lee/g;", "connectionPool", "Lee/e;", "call", "Lzd/r;", "eventListener", "<init>", "(Lee/g;Lzd/a;Lee/e;Lzd/r;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ee.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class ExchangeFinder {

    /* renamed from: a, reason: collision with root package name */
    private final RealConnectionPool f11176a;

    /* renamed from: b, reason: collision with root package name */
    private final Address f11177b;

    /* renamed from: c, reason: collision with root package name */
    private final e f11178c;

    /* renamed from: d, reason: collision with root package name */
    private final EventListener f11179d;

    /* renamed from: e, reason: collision with root package name */
    private RouteSelector.b f11180e;

    /* renamed from: f, reason: collision with root package name */
    private RouteSelector f11181f;

    /* renamed from: g, reason: collision with root package name */
    private int f11182g;

    /* renamed from: h, reason: collision with root package name */
    private int f11183h;

    /* renamed from: i, reason: collision with root package name */
    private int f11184i;

    /* renamed from: j, reason: collision with root package name */
    private d0 f11185j;

    public ExchangeFinder(RealConnectionPool realConnectionPool, Address address, e eVar, EventListener eventListener) {
        k.e(realConnectionPool, "connectionPool");
        k.e(address, "address");
        k.e(eVar, "call");
        k.e(eventListener, "eventListener");
        this.f11176a = realConnectionPool;
        this.f11177b = address;
        this.f11178c = eVar;
        this.f11179d = eventListener;
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x0137  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0151  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final RealConnection b(int connectTimeout, int readTimeout, int writeTimeout, int pingIntervalMillis, boolean connectionRetryEnabled) {
        List<d0> a10;
        Socket v7;
        if (!this.f11178c.getF11201t()) {
            RealConnection f11195n = this.f11178c.getF11195n();
            if (f11195n != null) {
                synchronized (f11195n) {
                    if (!f11195n.getF11219l() && g(f11195n.getF11211d().getF20560a().getF20493i())) {
                        v7 = null;
                        Unit unit = Unit.f15173a;
                    }
                    v7 = this.f11178c.v();
                    Unit unit2 = Unit.f15173a;
                }
                if (this.f11178c.getF11195n() != null) {
                    if (v7 == null) {
                        return f11195n;
                    }
                    throw new IllegalStateException("Check failed.".toString());
                }
                if (v7 != null) {
                    ae.d.m(v7);
                }
                this.f11179d.k(this.f11178c, f11195n);
            }
            this.f11182g = 0;
            this.f11183h = 0;
            this.f11184i = 0;
            if (this.f11176a.a(this.f11177b, this.f11178c, null, false)) {
                RealConnection f11195n2 = this.f11178c.getF11195n();
                k.b(f11195n2);
                this.f11179d.j(this.f11178c, f11195n2);
                return f11195n2;
            }
            d0 d0Var = this.f11185j;
            try {
                if (d0Var != null) {
                    k.b(d0Var);
                    this.f11185j = null;
                } else {
                    RouteSelector.b bVar = this.f11180e;
                    if (bVar != null) {
                        k.b(bVar);
                        if (bVar.b()) {
                            RouteSelector.b bVar2 = this.f11180e;
                            k.b(bVar2);
                            d0Var = bVar2.c();
                        }
                    }
                    RouteSelector routeSelector = this.f11181f;
                    if (routeSelector == null) {
                        routeSelector = new RouteSelector(this.f11177b, this.f11178c.getF11186e().getG(), this.f11178c, this.f11179d);
                        this.f11181f = routeSelector;
                    }
                    RouteSelector.b c10 = routeSelector.c();
                    this.f11180e = c10;
                    a10 = c10.a();
                    if (!this.f11178c.getF11201t()) {
                        if (this.f11176a.a(this.f11177b, this.f11178c, a10, false)) {
                            RealConnection f11195n3 = this.f11178c.getF11195n();
                            k.b(f11195n3);
                            this.f11179d.j(this.f11178c, f11195n3);
                            return f11195n3;
                        }
                        d0Var = c10.c();
                        RealConnection realConnection = new RealConnection(this.f11176a, d0Var);
                        this.f11178c.x(realConnection);
                        realConnection.f(connectTimeout, readTimeout, writeTimeout, pingIntervalMillis, connectionRetryEnabled, this.f11178c, this.f11179d);
                        this.f11178c.x(null);
                        this.f11178c.getF11186e().getG().a(realConnection.getF11211d());
                        if (!this.f11176a.a(this.f11177b, this.f11178c, a10, true)) {
                            RealConnection f11195n4 = this.f11178c.getF11195n();
                            k.b(f11195n4);
                            this.f11185j = d0Var;
                            ae.d.m(realConnection.D());
                            this.f11179d.j(this.f11178c, f11195n4);
                            return f11195n4;
                        }
                        synchronized (realConnection) {
                            this.f11176a.e(realConnection);
                            this.f11178c.c(realConnection);
                            Unit unit3 = Unit.f15173a;
                        }
                        this.f11179d.j(this.f11178c, realConnection);
                        return realConnection;
                    }
                    throw new IOException("Canceled");
                }
                realConnection.f(connectTimeout, readTimeout, writeTimeout, pingIntervalMillis, connectionRetryEnabled, this.f11178c, this.f11179d);
                this.f11178c.x(null);
                this.f11178c.getF11186e().getG().a(realConnection.getF11211d());
                if (!this.f11176a.a(this.f11177b, this.f11178c, a10, true)) {
                }
            } catch (Throwable th) {
                this.f11178c.x(null);
                throw th;
            }
            a10 = null;
            RealConnection realConnection2 = new RealConnection(this.f11176a, d0Var);
            this.f11178c.x(realConnection2);
        } else {
            throw new IOException("Canceled");
        }
    }

    private final RealConnection c(int connectTimeout, int readTimeout, int writeTimeout, int pingIntervalMillis, boolean connectionRetryEnabled, boolean doExtensiveHealthChecks) {
        while (true) {
            RealConnection b10 = b(connectTimeout, readTimeout, writeTimeout, pingIntervalMillis, connectionRetryEnabled);
            if (b10.u(doExtensiveHealthChecks)) {
                return b10;
            }
            b10.y();
            if (this.f11185j == null) {
                RouteSelector.b bVar = this.f11180e;
                if (bVar == null ? true : bVar.b()) {
                    continue;
                } else {
                    RouteSelector routeSelector = this.f11181f;
                    if (!(routeSelector != null ? routeSelector.a() : true)) {
                        throw new IOException("exhausted all routes");
                    }
                }
            }
        }
    }

    private final d0 f() {
        RealConnection f11195n;
        if (this.f11182g > 1 || this.f11183h > 1 || this.f11184i > 0 || (f11195n = this.f11178c.getF11195n()) == null) {
            return null;
        }
        synchronized (f11195n) {
            if (f11195n.getF11221n() != 0) {
                return null;
            }
            if (ae.d.j(f11195n.getF11211d().getF20560a().getF20493i(), getF11177b().getF20493i())) {
                return f11195n.getF11211d();
            }
            return null;
        }
    }

    public final ExchangeCodec a(OkHttpClient client, fe.g chain) {
        k.e(client, "client");
        k.e(chain, "chain");
        try {
            return c(chain.getF11466f(), chain.getF11467g(), chain.getF11468h(), client.getE(), client.getF20744j(), !k.a(chain.getF11465e().getF20797b(), "GET")).w(client, chain);
        } catch (RouteException e10) {
            this.h(e10.getF11241f());
            throw e10;
        } catch (IOException e11) {
            this.h(e11);
            throw new RouteException(e11);
        }
    }

    /* renamed from: d, reason: from getter */
    public final Address getF11177b() {
        return this.f11177b;
    }

    public final boolean e() {
        RouteSelector routeSelector;
        boolean z10 = false;
        if (this.f11182g == 0 && this.f11183h == 0 && this.f11184i == 0) {
            return false;
        }
        if (this.f11185j != null) {
            return true;
        }
        d0 f10 = f();
        if (f10 != null) {
            this.f11185j = f10;
            return true;
        }
        RouteSelector.b bVar = this.f11180e;
        if (bVar != null && bVar.b()) {
            z10 = true;
        }
        if (z10 || (routeSelector = this.f11181f) == null) {
            return true;
        }
        return routeSelector.a();
    }

    public final boolean g(HttpUrl url) {
        k.e(url, "url");
        HttpUrl f20493i = this.f11177b.getF20493i();
        return url.getF20717e() == f20493i.getF20717e() && k.a(url.getF20716d(), f20493i.getF20716d());
    }

    public final void h(IOException iOException) {
        k.e(iOException, "e");
        this.f11185j = null;
        if ((iOException instanceof StreamResetException) && ((StreamResetException) iOException).f12469e == ErrorCode.REFUSED_STREAM) {
            this.f11182g++;
        } else if (iOException instanceof ConnectionShutdownException) {
            this.f11183h++;
        } else {
            this.f11184i++;
        }
    }
}
