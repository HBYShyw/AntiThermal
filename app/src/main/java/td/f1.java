package td;

import kotlin.Metadata;
import ma.Unit;

/* compiled from: CancellableContinuationImpl.kt */
@Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B!\u0012\u0018\u0010\n\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0012\u0004\u0012\u00020\u00040\bj\u0002`\t¢\u0006\u0004\b\u000b\u0010\fJ\u0013\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0016¨\u0006\r"}, d2 = {"Ltd/f1;", "Ltd/i;", "", "cause", "Lma/f0;", "a", "", "toString", "Lkotlin/Function1;", "Lkotlinx/coroutines/CompletionHandler;", "handler", "<init>", "(Lya/l;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
final class f1 extends i {

    /* renamed from: e, reason: collision with root package name */
    private final ya.l<Throwable, Unit> f18737e;

    /* JADX WARN: Multi-variable type inference failed */
    public f1(ya.l<? super Throwable, Unit> lVar) {
        this.f18737e = lVar;
    }

    @Override // td.j
    public void a(Throwable th) {
        this.f18737e.invoke(th);
    }

    @Override // ya.l
    public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
        a(th);
        return Unit.f15173a;
    }

    public String toString() {
        return "InvokeOnCancel[" + DebugStrings.a(this.f18737e) + '@' + DebugStrings.b(this) + ']';
    }
}
