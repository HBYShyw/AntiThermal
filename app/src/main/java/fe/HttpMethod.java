package fe;

import com.oplus.backup.sdk.common.utils.Constants;
import kotlin.Metadata;

/* compiled from: HttpMethod.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\t\u0010\nJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0007J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0007J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002¨\u0006\u000b"}, d2 = {"Lfe/f;", "", "", Constants.MessagerConstants.METHOD_KEY, "", "d", "a", "c", "b", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: fe.f, reason: use source file name */
/* loaded from: classes2.dex */
public final class HttpMethod {

    /* renamed from: a, reason: collision with root package name */
    public static final HttpMethod f11460a = new HttpMethod();

    private HttpMethod() {
    }

    public static final boolean a(String method) {
        za.k.e(method, Constants.MessagerConstants.METHOD_KEY);
        return (za.k.a(method, "GET") || za.k.a(method, "HEAD")) ? false : true;
    }

    public static final boolean d(String method) {
        za.k.e(method, Constants.MessagerConstants.METHOD_KEY);
        return za.k.a(method, "POST") || za.k.a(method, "PUT") || za.k.a(method, "PATCH") || za.k.a(method, "PROPPATCH") || za.k.a(method, "REPORT");
    }

    public final boolean b(String method) {
        za.k.e(method, Constants.MessagerConstants.METHOD_KEY);
        return !za.k.a(method, "PROPFIND");
    }

    public final boolean c(String method) {
        za.k.e(method, Constants.MessagerConstants.METHOD_KEY);
        return za.k.a(method, "PROPFIND");
    }
}
