package ee;

import java.io.IOException;
import kotlin.Metadata;
import za.k;

/* compiled from: RouteException.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u00002\u00060\u0001j\u0002`\u0002B\u0011\b\u0000\u0012\u0006\u0010\u0007\u001a\u00020\u0003¢\u0006\u0004\b\u000e\u0010\u000fJ\u000e\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0003R\u0017\u0010\u0007\u001a\u00020\u00038\u0006¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\t\u0010\nR$\u0010\f\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\u00038\u0006@BX\u0086\u000e¢\u0006\f\n\u0004\b\f\u0010\b\u001a\u0004\b\r\u0010\n¨\u0006\u0010"}, d2 = {"Lee/i;", "Ljava/lang/RuntimeException;", "Lkotlin/RuntimeException;", "Ljava/io/IOException;", "e", "Lma/f0;", "a", "firstConnectException", "Ljava/io/IOException;", "b", "()Ljava/io/IOException;", "<set-?>", "lastConnectException", "c", "<init>", "(Ljava/io/IOException;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ee.i, reason: use source file name */
/* loaded from: classes2.dex */
public final class RouteException extends RuntimeException {

    /* renamed from: e, reason: collision with root package name */
    private final IOException f11240e;

    /* renamed from: f, reason: collision with root package name */
    private IOException f11241f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public RouteException(IOException iOException) {
        super(iOException);
        k.e(iOException, "firstConnectException");
        this.f11240e = iOException;
        this.f11241f = iOException;
    }

    public final void a(IOException iOException) {
        k.e(iOException, "e");
        ma.b.a(this.f11240e, iOException);
        this.f11241f = iOException;
    }

    /* renamed from: b, reason: from getter */
    public final IOException getF11240e() {
        return this.f11240e;
    }

    /* renamed from: c, reason: from getter */
    public final IOException getF11241f() {
        return this.f11241f;
    }
}
