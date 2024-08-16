package td;

import kotlin.Metadata;
import ma.Unit;

/* compiled from: JobSupport.kt */
@Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u000f\u0012\u0006\u0010\u000e\u001a\u00020\r¢\u0006\u0004\b\u000f\u0010\u0010J\u0013\u0010\u0006\u001a\u00020\u00052\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u0096\u0002J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0003H\u0016R\u0014\u0010\f\u001a\u00020\t8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b¨\u0006\u0011"}, d2 = {"Ltd/r;", "Ltd/k1;", "Ltd/q;", "", "cause", "Lma/f0;", "F", "", "d", "Ltd/i1;", "getParent", "()Ltd/i1;", "parent", "Ltd/s;", "childJob", "<init>", "(Ltd/s;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class r extends k1 implements q {

    /* renamed from: i, reason: collision with root package name */
    public final s f18786i;

    public r(s sVar) {
        this.f18786i = sVar;
    }

    @Override // td.x
    public void F(Throwable th) {
        this.f18786i.e0(G());
    }

    @Override // td.q
    public boolean d(Throwable cause) {
        return G().x(cause);
    }

    @Override // td.q
    public i1 getParent() {
        return G();
    }

    @Override // ya.l
    public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
        F(th);
        return Unit.f15173a;
    }
}
