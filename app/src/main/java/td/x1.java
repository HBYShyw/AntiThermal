package td;

import kotlin.Metadata;
import ma.Unit;

/* compiled from: CancellableContinuation.kt */
@Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\t\u001a\u00020\b¢\u0006\u0004\b\n\u0010\u000bJ\u0013\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0016¨\u0006\f"}, d2 = {"Ltd/x1;", "Ltd/e;", "", "cause", "Lma/f0;", "a", "", "toString", "Lkotlinx/coroutines/internal/n;", "node", "<init>", "(Lkotlinx/coroutines/internal/n;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
final class x1 extends e {

    /* renamed from: e, reason: collision with root package name */
    private final kotlinx.coroutines.internal.n f18812e;

    public x1(kotlinx.coroutines.internal.n nVar) {
        this.f18812e = nVar;
    }

    @Override // td.j
    public void a(Throwable th) {
        this.f18812e.A();
    }

    @Override // ya.l
    public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
        a(th);
        return Unit.f15173a;
    }

    public String toString() {
        return "RemoveOnCancel[" + this.f18812e + ']';
    }
}
