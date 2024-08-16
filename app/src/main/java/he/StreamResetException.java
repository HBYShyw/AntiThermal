package he;

import java.io.IOException;
import kotlin.Metadata;

/* compiled from: StreamResetException.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"Lhe/n;", "Ljava/io/IOException;", "Lhe/b;", "errorCode", "<init>", "(Lhe/b;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: he.n, reason: use source file name */
/* loaded from: classes2.dex */
public final class StreamResetException extends IOException {

    /* renamed from: e, reason: collision with root package name */
    public final ErrorCode f12469e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StreamResetException(ErrorCode errorCode) {
        super(za.k.l("stream was reset: ", errorCode));
        za.k.e(errorCode, "errorCode");
        this.f12469e = errorCode;
    }
}
