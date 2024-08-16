package fe;

import ee.RealConnection;
import kotlin.Metadata;
import me.Sink;
import me.Source;
import zd.b0;
import zd.z;

/* compiled from: ExchangeCodec.kt */
@Metadata(bv = {}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H&J\u0010\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0002H&J\b\u0010\n\u001a\u00020\bH&J\b\u0010\u000b\u001a\u00020\bH&J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\r\u001a\u00020\fH&J\u0010\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u0010H&J\u0010\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0011\u001a\u00020\u0010H&J\b\u0010\u0015\u001a\u00020\bH&R\u0014\u0010\u0019\u001a\u00020\u00168&X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018¨\u0006\u001a"}, d2 = {"Lfe/d;", "", "Lzd/z;", "request", "", "contentLength", "Lme/y;", "d", "Lma/f0;", "c", "h", "a", "", "expectContinue", "Lzd/b0$a;", "e", "Lzd/b0;", "response", "g", "Lme/a0;", "b", "cancel", "Lee/f;", "f", "()Lee/f;", "connection", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: fe.d, reason: use source file name */
/* loaded from: classes2.dex */
public interface ExchangeCodec {
    void a();

    Source b(b0 response);

    void c(z zVar);

    void cancel();

    Sink d(z request, long contentLength);

    b0.a e(boolean expectContinue);

    /* renamed from: f */
    RealConnection getF12402a();

    long g(b0 response);

    void h();
}
