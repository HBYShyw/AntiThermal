package fe;

import kotlin.Metadata;
import me.BufferedSource;
import zd.MediaType;
import zd.ResponseBody;

/* compiled from: RealResponseBody.kt */
@Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\u0018\u00002\u00020\u0001B!\u0012\b\u0010\t\u001a\u0004\u0018\u00010\b\u0012\u0006\u0010\n\u001a\u00020\u0002\u0012\u0006\u0010\u000b\u001a\u00020\u0006¢\u0006\u0004\b\f\u0010\rJ\b\u0010\u0003\u001a\u00020\u0002H\u0016J\n\u0010\u0005\u001a\u0004\u0018\u00010\u0004H\u0016J\b\u0010\u0007\u001a\u00020\u0006H\u0016¨\u0006\u000e"}, d2 = {"Lfe/h;", "Lzd/c0;", "", "c", "Lzd/w;", "m", "Lme/f;", "u", "", "contentTypeString", "contentLength", "source", "<init>", "(Ljava/lang/String;JLme/f;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: fe.h, reason: use source file name */
/* loaded from: classes2.dex */
public final class RealResponseBody extends ResponseBody {

    /* renamed from: f, reason: collision with root package name */
    private final String f11470f;

    /* renamed from: g, reason: collision with root package name */
    private final long f11471g;

    /* renamed from: h, reason: collision with root package name */
    private final BufferedSource f11472h;

    public RealResponseBody(String str, long j10, BufferedSource bufferedSource) {
        za.k.e(bufferedSource, "source");
        this.f11470f = str;
        this.f11471g = j10;
        this.f11472h = bufferedSource;
    }

    @Override // zd.ResponseBody
    /* renamed from: c, reason: from getter */
    public long getF11471g() {
        return this.f11471g;
    }

    @Override // zd.ResponseBody
    /* renamed from: m */
    public MediaType getF20533f() {
        String str = this.f11470f;
        if (str == null) {
            return null;
        }
        return MediaType.f20732e.b(str);
    }

    @Override // zd.ResponseBody
    /* renamed from: u, reason: from getter */
    public BufferedSource getF11472h() {
        return this.f11472h;
    }
}
