package fe;

import java.net.Proxy;
import kotlin.Metadata;
import zd.HttpUrl;
import zd.z;

/* compiled from: RequestLine.kt */
@Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\r\u0010\u000eJ\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\u0016\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004J\u000e\u0010\f\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\n¨\u0006\u000f"}, d2 = {"Lfe/i;", "", "Lzd/z;", "request", "Ljava/net/Proxy$Type;", "proxyType", "", "b", "", "a", "Lzd/u;", "url", "c", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: fe.i, reason: use source file name */
/* loaded from: classes2.dex */
public final class RequestLine {

    /* renamed from: a, reason: collision with root package name */
    public static final RequestLine f11473a = new RequestLine();

    private RequestLine() {
    }

    private final boolean b(z request, Proxy.Type proxyType) {
        return !request.f() && proxyType == Proxy.Type.HTTP;
    }

    public final String a(z request, Proxy.Type proxyType) {
        za.k.e(request, "request");
        za.k.e(proxyType, "proxyType");
        StringBuilder sb2 = new StringBuilder();
        sb2.append(request.getF20797b());
        sb2.append(' ');
        RequestLine requestLine = f11473a;
        if (requestLine.b(request, proxyType)) {
            sb2.append(request.getF20796a());
        } else {
            sb2.append(requestLine.c(request.getF20796a()));
        }
        sb2.append(" HTTP/1.1");
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    public final String c(HttpUrl url) {
        za.k.e(url, "url");
        String d10 = url.d();
        String f10 = url.f();
        if (f10 == null) {
            return d10;
        }
        return d10 + '?' + ((Object) f10);
    }
}
