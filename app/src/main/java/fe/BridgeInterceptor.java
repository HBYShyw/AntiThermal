package fe;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.r;
import me.GzipSource;
import me.n;
import sd.StringsJVM;
import zd.Cookie;
import zd.CookieJar;
import zd.MediaType;
import zd.RequestBody;
import zd.ResponseBody;
import zd.b0;
import zd.v;
import zd.z;

/* compiled from: BridgeInterceptor.kt */
@Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\f\u001a\u00020\u000b¢\u0006\u0004\b\r\u0010\u000eJ\u0016\u0010\u0006\u001a\u00020\u00052\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0002J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\u0007H\u0016¨\u0006\u000f"}, d2 = {"Lfe/a;", "Lzd/v;", "", "Lzd/m;", "cookies", "", "b", "Lzd/v$a;", "chain", "Lzd/b0;", "a", "Lzd/n;", "cookieJar", "<init>", "(Lzd/n;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: fe.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class BridgeInterceptor implements v {

    /* renamed from: a, reason: collision with root package name */
    private final CookieJar f11453a;

    public BridgeInterceptor(CookieJar cookieJar) {
        za.k.e(cookieJar, "cookieJar");
        this.f11453a = cookieJar;
    }

    private final String b(List<Cookie> cookies) {
        StringBuilder sb2 = new StringBuilder();
        int i10 = 0;
        for (Object obj : cookies) {
            int i11 = i10 + 1;
            if (i10 < 0) {
                r.t();
            }
            Cookie cookie = (Cookie) obj;
            if (i10 > 0) {
                sb2.append("; ");
            }
            sb2.append(cookie.getF20676a());
            sb2.append('=');
            sb2.append(cookie.getF20677b());
            i10 = i11;
        }
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    @Override // zd.v
    public b0 a(v.a chain) {
        boolean r10;
        ResponseBody f20511k;
        za.k.e(chain, "chain");
        z request = chain.request();
        z.a h10 = request.h();
        RequestBody f20799d = request.getF20799d();
        if (f20799d != null) {
            MediaType f20497b = f20799d.getF20497b();
            if (f20497b != null) {
                h10.d("Content-Type", f20497b.getF20735a());
            }
            long a10 = f20799d.a();
            if (a10 != -1) {
                h10.d("Content-Length", String.valueOf(a10));
                h10.h("Transfer-Encoding");
            } else {
                h10.d("Transfer-Encoding", "chunked");
                h10.h("Content-Length");
            }
        }
        boolean z10 = false;
        if (request.d("Host") == null) {
            h10.d("Host", ae.d.Q(request.getF20796a(), false, 1, null));
        }
        if (request.d("Connection") == null) {
            h10.d("Connection", "Keep-Alive");
        }
        if (request.d("Accept-Encoding") == null && request.d("Range") == null) {
            h10.d("Accept-Encoding", "gzip");
            z10 = true;
        }
        List<Cookie> b10 = this.f11453a.b(request.getF20796a());
        if (!b10.isEmpty()) {
            h10.d("Cookie", b(b10));
        }
        if (request.d("User-Agent") == null) {
            h10.d("User-Agent", "okhttp/4.10.0");
        }
        b0 a11 = chain.a(h10.b());
        HttpHeaders.f(this.f11453a, request.getF20796a(), a11.getF20510j());
        b0.a s7 = a11.e0().s(request);
        if (z10) {
            r10 = StringsJVM.r("gzip", b0.S(a11, "Content-Encoding", null, 2, null), true);
            if (r10 && HttpHeaders.b(a11) && (f20511k = a11.getF20511k()) != null) {
                GzipSource gzipSource = new GzipSource(f20511k.getF11472h());
                s7.l(a11.getF20510j().f().f("Content-Encoding").f("Content-Length").d());
                s7.b(new RealResponseBody(b0.S(a11, "Content-Type", null, 2, null), -1L, n.b(gzipSource)));
            }
        }
        return s7.c();
    }
}
