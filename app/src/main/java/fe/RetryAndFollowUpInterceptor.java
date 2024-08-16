package fe;

import com.oplus.backup.sdk.common.utils.Constants;
import ee.Exchange;
import ee.RealConnection;
import ee.RouteException;
import he.ConnectionShutdownException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import java.util.List;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import kotlin.Metadata;
import kotlin.collections._Collections;
import kotlin.collections.r;
import za.DefaultConstructorMarker;
import zd.HttpUrl;
import zd.OkHttpClient;
import zd.RequestBody;
import zd.ResponseBody;
import zd.b0;
import zd.d0;
import zd.v;
import zd.z;

/* compiled from: RetryAndFollowUpInterceptor.kt */
@Metadata(bv = {}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001\u0019B\u000f\u0012\u0006\u0010\u001b\u001a\u00020\u001a¢\u0006\u0004\b\u001c\u0010\u001dJ(\u0010\u0003\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\bH\u0002J\u0018\u0010\n\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0006H\u0002J\u0018\u0010\u000b\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\bH\u0002J\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u00062\u0006\u0010\r\u001a\u00020\f2\b\u0010\u000f\u001a\u0004\u0018\u00010\u000eH\u0002J\u001a\u0010\u0013\u001a\u0004\u0018\u00010\u00062\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u0011H\u0002J\u0018\u0010\u0016\u001a\u00020\u00142\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u0014H\u0002J\u0010\u0010\u0019\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\u0017H\u0016¨\u0006\u001e"}, d2 = {"Lfe/j;", "Lzd/v;", "Ljava/io/IOException;", "e", "Lee/e;", "call", "Lzd/z;", "userRequest", "", "requestSendStarted", "f", "d", "Lzd/b0;", "userResponse", "Lee/c;", "exchange", "c", "", Constants.MessagerConstants.METHOD_KEY, "b", "", "defaultDelay", "g", "Lzd/v$a;", "chain", "a", "Lzd/x;", "client", "<init>", "(Lzd/x;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: fe.j, reason: use source file name */
/* loaded from: classes2.dex */
public final class RetryAndFollowUpInterceptor implements v {

    /* renamed from: b, reason: collision with root package name */
    public static final a f11474b = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final OkHttpClient f11475a;

    /* compiled from: RetryAndFollowUpInterceptor.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0003\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0007"}, d2 = {"Lfe/j$a;", "", "", "MAX_FOLLOW_UPS", "I", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: fe.j$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public RetryAndFollowUpInterceptor(OkHttpClient okHttpClient) {
        za.k.e(okHttpClient, "client");
        this.f11475a = okHttpClient;
    }

    private final z b(b0 userResponse, String method) {
        String S;
        HttpUrl o10;
        if (!this.f11475a.getF20746l() || (S = b0.S(userResponse, "Location", null, 2, null)) == null || (o10 = userResponse.getF20505e().getF20796a().o(S)) == null) {
            return null;
        }
        if (!za.k.a(o10.getF20713a(), userResponse.getF20505e().getF20796a().getF20713a()) && !this.f11475a.getF20747m()) {
            return null;
        }
        z.a h10 = userResponse.getF20505e().h();
        if (HttpMethod.a(method)) {
            int code = userResponse.getCode();
            HttpMethod httpMethod = HttpMethod.f11460a;
            boolean z10 = httpMethod.c(method) || code == 308 || code == 307;
            if (httpMethod.b(method) && code != 308 && code != 307) {
                h10.f("GET", null);
            } else {
                h10.f(method, z10 ? userResponse.getF20505e().getF20799d() : null);
            }
            if (!z10) {
                h10.h("Transfer-Encoding");
                h10.h("Content-Length");
                h10.h("Content-Type");
            }
        }
        if (!ae.d.j(userResponse.getF20505e().getF20796a(), o10)) {
            h10.h("Authorization");
        }
        return h10.n(o10).b();
    }

    private final z c(b0 userResponse, Exchange exchange) {
        RealConnection f11164f;
        d0 f11211d = (exchange == null || (f11164f = exchange.getF11164f()) == null) ? null : f11164f.getF11211d();
        int code = userResponse.getCode();
        String f20797b = userResponse.getF20505e().getF20797b();
        if (code != 307 && code != 308) {
            if (code == 401) {
                return this.f11475a.getF20745k().a(f11211d, userResponse);
            }
            if (code == 421) {
                RequestBody f20799d = userResponse.getF20505e().getF20799d();
                if ((f20799d != null && f20799d.e()) || exchange == null || !exchange.k()) {
                    return null;
                }
                exchange.getF11164f().x();
                return userResponse.getF20505e();
            }
            if (code == 503) {
                b0 f20514n = userResponse.getF20514n();
                if ((f20514n == null || f20514n.getCode() != 503) && g(userResponse, Integer.MAX_VALUE) == 0) {
                    return userResponse.getF20505e();
                }
                return null;
            }
            if (code == 407) {
                za.k.b(f11211d);
                if (f11211d.getF20561b().type() == Proxy.Type.HTTP) {
                    return this.f11475a.getF20752r().a(f11211d, userResponse);
                }
                throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
            }
            if (code == 408) {
                if (!this.f11475a.getF20744j()) {
                    return null;
                }
                RequestBody f20799d2 = userResponse.getF20505e().getF20799d();
                if (f20799d2 != null && f20799d2.e()) {
                    return null;
                }
                b0 f20514n2 = userResponse.getF20514n();
                if ((f20514n2 == null || f20514n2.getCode() != 408) && g(userResponse, 0) <= 0) {
                    return userResponse.getF20505e();
                }
                return null;
            }
            switch (code) {
                case 300:
                case 301:
                case 302:
                case 303:
                    break;
                default:
                    return null;
            }
        }
        return b(userResponse, f20797b);
    }

    private final boolean d(IOException e10, boolean requestSendStarted) {
        if (e10 instanceof ProtocolException) {
            return false;
        }
        return e10 instanceof InterruptedIOException ? (e10 instanceof SocketTimeoutException) && !requestSendStarted : (((e10 instanceof SSLHandshakeException) && (e10.getCause() instanceof CertificateException)) || (e10 instanceof SSLPeerUnverifiedException)) ? false : true;
    }

    private final boolean e(IOException e10, ee.e call, z userRequest, boolean requestSendStarted) {
        if (this.f11475a.getF20744j()) {
            return !(requestSendStarted && f(e10, userRequest)) && d(e10, requestSendStarted) && call.w();
        }
        return false;
    }

    private final boolean f(IOException e10, z userRequest) {
        RequestBody f20799d = userRequest.getF20799d();
        return (f20799d != null && f20799d.e()) || (e10 instanceof FileNotFoundException);
    }

    private final int g(b0 userResponse, int defaultDelay) {
        String S = b0.S(userResponse, "Retry-After", null, 2, null);
        if (S == null) {
            return defaultDelay;
        }
        if (!new sd.j("\\d+").b(S)) {
            return Integer.MAX_VALUE;
        }
        Integer valueOf = Integer.valueOf(S);
        za.k.d(valueOf, "valueOf(header)");
        return valueOf.intValue();
    }

    @Override // zd.v
    public b0 a(v.a chain) {
        List j10;
        Exchange f11197p;
        z c10;
        za.k.e(chain, "chain");
        g gVar = (g) chain;
        z f11465e = gVar.getF11465e();
        ee.e f11461a = gVar.getF11461a();
        j10 = r.j();
        b0 b0Var = null;
        boolean z10 = true;
        int i10 = 0;
        while (true) {
            f11461a.i(f11465e, z10);
            try {
                if (!f11461a.getF11201t()) {
                    try {
                        b0 a10 = gVar.a(f11465e);
                        if (b0Var != null) {
                            a10 = a10.e0().p(b0Var.e0().b(null).c()).c();
                        }
                        b0Var = a10;
                        f11197p = f11461a.getF11197p();
                        c10 = c(b0Var, f11197p);
                    } catch (RouteException e10) {
                        if (e(e10.getF11241f(), f11461a, f11465e, false)) {
                            j10 = _Collections.n0(j10, e10.getF11240e());
                            f11461a.j(true);
                            z10 = false;
                        } else {
                            throw ae.d.X(e10.getF11240e(), j10);
                        }
                    } catch (IOException e11) {
                        if (e(e11, f11461a, f11465e, !(e11 instanceof ConnectionShutdownException))) {
                            j10 = _Collections.n0(j10, e11);
                            f11461a.j(true);
                            z10 = false;
                        } else {
                            throw ae.d.X(e11, j10);
                        }
                    }
                    if (c10 == null) {
                        if (f11197p != null && f11197p.getF11163e()) {
                            f11461a.y();
                        }
                        f11461a.j(false);
                        return b0Var;
                    }
                    RequestBody f20799d = c10.getF20799d();
                    if (f20799d != null && f20799d.e()) {
                        f11461a.j(false);
                        return b0Var;
                    }
                    ResponseBody f20511k = b0Var.getF20511k();
                    if (f20511k != null) {
                        ae.d.l(f20511k);
                    }
                    i10++;
                    if (i10 <= 20) {
                        f11461a.j(true);
                        f11465e = c10;
                        z10 = true;
                    } else {
                        throw new ProtocolException(za.k.l("Too many follow-up requests: ", Integer.valueOf(i10)));
                    }
                } else {
                    throw new IOException("Canceled");
                }
            } catch (Throwable th) {
                f11461a.j(true);
                throw th;
            }
        }
    }
}
