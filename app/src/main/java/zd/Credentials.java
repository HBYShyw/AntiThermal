package zd;

import com.coui.appcompat.touchsearchview.COUIAccessibilityUtil;
import java.nio.charset.Charset;
import kotlin.Metadata;

/* compiled from: Credentials.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\b\u0010\tJ\"\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u00022\b\b\u0002\u0010\u0006\u001a\u00020\u0005H\u0007¨\u0006\n"}, d2 = {"Lzd/o;", "", "", "username", "password", "Ljava/nio/charset/Charset;", "charset", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.o, reason: use source file name */
/* loaded from: classes2.dex */
public final class Credentials {

    /* renamed from: a, reason: collision with root package name */
    public static final Credentials f20688a = new Credentials();

    private Credentials() {
    }

    public static final String a(String username, String password, Charset charset) {
        za.k.e(username, "username");
        za.k.e(password, "password");
        za.k.e(charset, "charset");
        return za.k.l("Basic ", me.g.f15493h.b(username + COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR + password, charset).a());
    }
}
