package zd;

import com.coui.appcompat.touchsearchview.COUIAccessibilityUtil;
import com.oplus.backup.sdk.common.utils.Constants;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import fe.HttpMethod;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.m0;
import sd.StringsJVM;
import zd.Headers;

/* compiled from: Request.kt */
@Metadata(bv = {}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001:\u0001\u0019BC\b\u0000\u0012\u0006\u0010\t\u001a\u00020\b\u0012\u0006\u0010\r\u001a\u00020\u0002\u0012\u0006\u0010\u0012\u001a\u00020\u0011\u0012\b\u0010\u0017\u001a\u0004\u0018\u00010\u0016\u0012\u0016\u0010\u001d\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u001c\u0012\u0004\u0012\u00020\u00010\u001b¢\u0006\u0004\b)\u0010*J\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0003\u001a\u00020\u0002J\u0006\u0010\u0006\u001a\u00020\u0005J\b\u0010\u0007\u001a\u00020\u0002H\u0016R\u0017\u0010\t\u001a\u00020\b8\u0007¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\r\u001a\u00020\u00028\u0007¢\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0012\u001a\u00020\u00118\u0007¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015R\u0019\u0010\u0017\u001a\u0004\u0018\u00010\u00168\u0007¢\u0006\f\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0019\u0010\u001aR*\u0010\u001d\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u001c\u0012\u0004\u0012\u00020\u00010\u001b8\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u001d\u0010\u001e\u001a\u0004\b\u001f\u0010 R\u0011\u0010$\u001a\u00020!8F¢\u0006\u0006\u001a\u0004\b\"\u0010#R\u0011\u0010(\u001a\u00020%8G¢\u0006\u0006\u001a\u0004\b&\u0010'¨\u0006+"}, d2 = {"Lzd/z;", "", "", "name", "d", "Lzd/z$a;", "h", "toString", "Lzd/u;", "url", "Lzd/u;", "i", "()Lzd/u;", Constants.MessagerConstants.METHOD_KEY, "Ljava/lang/String;", "g", "()Ljava/lang/String;", "Lzd/t;", "headers", "Lzd/t;", "e", "()Lzd/t;", "Lzd/a0;", "body", "Lzd/a0;", "a", "()Lzd/a0;", "", "Ljava/lang/Class;", "tags", "Ljava/util/Map;", "c", "()Ljava/util/Map;", "", "f", "()Z", "isHttps", "Lzd/d;", "b", "()Lzd/d;", "cacheControl", "<init>", "(Lzd/u;Ljava/lang/String;Lzd/t;Lzd/a0;Ljava/util/Map;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class z {

    /* renamed from: a, reason: collision with root package name */
    private final HttpUrl f20796a;

    /* renamed from: b, reason: collision with root package name */
    private final String f20797b;

    /* renamed from: c, reason: collision with root package name */
    private final Headers f20798c;

    /* renamed from: d, reason: collision with root package name */
    private final RequestBody f20799d;

    /* renamed from: e, reason: collision with root package name */
    private final Map<Class<?>, Object> f20800e;

    /* renamed from: f, reason: collision with root package name */
    private CacheControl f20801f;

    public z(HttpUrl httpUrl, String str, Headers headers, RequestBody requestBody, Map<Class<?>, ? extends Object> map) {
        za.k.e(httpUrl, "url");
        za.k.e(str, Constants.MessagerConstants.METHOD_KEY);
        za.k.e(headers, "headers");
        za.k.e(map, "tags");
        this.f20796a = httpUrl;
        this.f20797b = str;
        this.f20798c = headers;
        this.f20799d = requestBody;
        this.f20800e = map;
    }

    /* renamed from: a, reason: from getter */
    public final RequestBody getF20799d() {
        return this.f20799d;
    }

    public final CacheControl b() {
        CacheControl cacheControl = this.f20801f;
        if (cacheControl != null) {
            return cacheControl;
        }
        CacheControl b10 = CacheControl.f20536n.b(this.f20798c);
        this.f20801f = b10;
        return b10;
    }

    public final Map<Class<?>, Object> c() {
        return this.f20800e;
    }

    public final String d(String name) {
        za.k.e(name, "name");
        return this.f20798c.d(name);
    }

    /* renamed from: e, reason: from getter */
    public final Headers getF20798c() {
        return this.f20798c;
    }

    public final boolean f() {
        return this.f20796a.getF20722j();
    }

    /* renamed from: g, reason: from getter */
    public final String getF20797b() {
        return this.f20797b;
    }

    public final a h() {
        return new a(this);
    }

    /* renamed from: i, reason: from getter */
    public final HttpUrl getF20796a() {
        return this.f20796a;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Request{method=");
        sb2.append(getF20797b());
        sb2.append(", url=");
        sb2.append(getF20796a());
        if (getF20798c().size() != 0) {
            sb2.append(", headers=[");
            int i10 = 0;
            for (ma.o<? extends String, ? extends String> oVar : getF20798c()) {
                int i11 = i10 + 1;
                if (i10 < 0) {
                    kotlin.collections.r.t();
                }
                ma.o<? extends String, ? extends String> oVar2 = oVar;
                String a10 = oVar2.a();
                String b10 = oVar2.b();
                if (i10 > 0) {
                    sb2.append(", ");
                }
                sb2.append(a10);
                sb2.append(COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
                sb2.append(b10);
                i10 = i11;
            }
            sb2.append(']');
        }
        if (!c().isEmpty()) {
            sb2.append(", tags=");
            sb2.append(c());
        }
        sb2.append('}');
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    /* compiled from: Request.kt */
    @Metadata(bv = {}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u000f\b\u0016\u0018\u00002\u00020\u0001B\t\b\u0016¢\u0006\u0004\b+\u0010,B\u0011\b\u0010\u0012\u0006\u0010-\u001a\u00020\u0014¢\u0006\u0004\b+\u0010.J\u0010\u0010\u0004\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0006\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0005H\u0016J\u0018\u0010\t\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J\u0018\u0010\n\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J\u0010\u0010\u000b\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\u0005H\u0016J\u0010\u0010\u000e\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\fH\u0016J\u0010\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\u001a\u0010\u0013\u001a\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u00052\b\u0010\u0010\u001a\u0004\u0018\u00010\u000fH\u0016J\b\u0010\u0015\u001a\u00020\u0014H\u0016R$\u0010\u0003\u001a\u0004\u0018\u00010\u00028\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0003\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\"\u0010\u0012\u001a\u00020\u00058\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0012\u0010\u001b\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\"\u0010\r\u001a\u00020 8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\r\u0010!\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R$\u0010\u0010\u001a\u0004\u0018\u00010\u000f8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0010\u0010&\u001a\u0004\b'\u0010(\"\u0004\b)\u0010*¨\u0006/"}, d2 = {"Lzd/z$a;", "", "Lzd/u;", "url", "n", "", "m", "name", ThermalBaseConfig.Item.ATTR_VALUE, "d", "a", "h", "Lzd/t;", "headers", "e", "Lzd/a0;", "body", "g", Constants.MessagerConstants.METHOD_KEY, "f", "Lzd/z;", "b", "Lzd/u;", "getUrl$okhttp", "()Lzd/u;", "l", "(Lzd/u;)V", "Ljava/lang/String;", "getMethod$okhttp", "()Ljava/lang/String;", "k", "(Ljava/lang/String;)V", "Lzd/t$a;", "Lzd/t$a;", "c", "()Lzd/t$a;", "j", "(Lzd/t$a;)V", "Lzd/a0;", "getBody$okhttp", "()Lzd/a0;", "i", "(Lzd/a0;)V", "<init>", "()V", "request", "(Lzd/z;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        private HttpUrl f20802a;

        /* renamed from: b, reason: collision with root package name */
        private String f20803b;

        /* renamed from: c, reason: collision with root package name */
        private Headers.a f20804c;

        /* renamed from: d, reason: collision with root package name */
        private RequestBody f20805d;

        /* renamed from: e, reason: collision with root package name */
        private Map<Class<?>, Object> f20806e;

        public a() {
            this.f20806e = new LinkedHashMap();
            this.f20803b = "GET";
            this.f20804c = new Headers.a();
        }

        public a a(String name, String value) {
            za.k.e(name, "name");
            za.k.e(value, ThermalBaseConfig.Item.ATTR_VALUE);
            getF20804c().a(name, value);
            return this;
        }

        public z b() {
            HttpUrl httpUrl = this.f20802a;
            if (httpUrl != null) {
                return new z(httpUrl, this.f20803b, this.f20804c.d(), this.f20805d, ae.d.S(this.f20806e));
            }
            throw new IllegalStateException("url == null".toString());
        }

        /* renamed from: c, reason: from getter */
        public final Headers.a getF20804c() {
            return this.f20804c;
        }

        public a d(String name, String value) {
            za.k.e(name, "name");
            za.k.e(value, ThermalBaseConfig.Item.ATTR_VALUE);
            getF20804c().g(name, value);
            return this;
        }

        public a e(Headers headers) {
            za.k.e(headers, "headers");
            j(headers.f());
            return this;
        }

        public a f(String method, RequestBody body) {
            za.k.e(method, Constants.MessagerConstants.METHOD_KEY);
            if (method.length() > 0) {
                if (body == null) {
                    if (!(true ^ HttpMethod.d(method))) {
                        throw new IllegalArgumentException(("method " + method + " must have a request body.").toString());
                    }
                } else if (!HttpMethod.a(method)) {
                    throw new IllegalArgumentException(("method " + method + " must not have a request body.").toString());
                }
                k(method);
                i(body);
                return this;
            }
            throw new IllegalArgumentException("method.isEmpty() == true".toString());
        }

        public a g(RequestBody body) {
            za.k.e(body, "body");
            return f("POST", body);
        }

        public a h(String name) {
            za.k.e(name, "name");
            getF20804c().f(name);
            return this;
        }

        public final void i(RequestBody requestBody) {
            this.f20805d = requestBody;
        }

        public final void j(Headers.a aVar) {
            za.k.e(aVar, "<set-?>");
            this.f20804c = aVar;
        }

        public final void k(String str) {
            za.k.e(str, "<set-?>");
            this.f20803b = str;
        }

        public final void l(HttpUrl httpUrl) {
            this.f20802a = httpUrl;
        }

        public a m(String url) {
            boolean B;
            boolean B2;
            za.k.e(url, "url");
            B = StringsJVM.B(url, "ws:", true);
            if (B) {
                String substring = url.substring(3);
                za.k.d(substring, "this as java.lang.String).substring(startIndex)");
                url = za.k.l("http:", substring);
            } else {
                B2 = StringsJVM.B(url, "wss:", true);
                if (B2) {
                    String substring2 = url.substring(4);
                    za.k.d(substring2, "this as java.lang.String).substring(startIndex)");
                    url = za.k.l("https:", substring2);
                }
            }
            return n(HttpUrl.f20711k.d(url));
        }

        public a n(HttpUrl url) {
            za.k.e(url, "url");
            l(url);
            return this;
        }

        public a(z zVar) {
            Map<Class<?>, Object> u7;
            za.k.e(zVar, "request");
            this.f20806e = new LinkedHashMap();
            this.f20802a = zVar.getF20796a();
            this.f20803b = zVar.getF20797b();
            this.f20805d = zVar.getF20799d();
            if (zVar.c().isEmpty()) {
                u7 = new LinkedHashMap<>();
            } else {
                u7 = m0.u(zVar.c());
            }
            this.f20806e = u7;
            this.f20804c = zVar.getF20798c().f();
        }
    }
}
