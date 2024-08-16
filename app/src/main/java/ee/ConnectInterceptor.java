package ee;

import kotlin.Metadata;
import za.k;
import zd.b0;
import zd.v;

/* compiled from: ConnectInterceptor.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\b"}, d2 = {"Lee/a;", "Lzd/v;", "Lzd/v$a;", "chain", "Lzd/b0;", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ee.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class ConnectInterceptor implements v {

    /* renamed from: a, reason: collision with root package name */
    public static final ConnectInterceptor f11154a = new ConnectInterceptor();

    private ConnectInterceptor() {
    }

    @Override // zd.v
    public b0 a(v.a chain) {
        k.e(chain, "chain");
        fe.g gVar = (fe.g) chain;
        return fe.g.c(gVar, 0, gVar.getF11461a().q(gVar), null, 0, 0, 0, 61, null).a(gVar.getF11465e());
    }
}
