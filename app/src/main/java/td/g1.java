package td;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.Metadata;
import ma.Unit;

/* compiled from: JobSupport.kt */
@Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B!\u0012\u0018\u0010\t\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0012\u0004\u0012\u00020\u00040\u0007j\u0002`\b¢\u0006\u0004\b\n\u0010\u000bJ\u001a\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0096\u0002¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\f"}, d2 = {"Ltd/g1;", "Ltd/k1;", "", "cause", "Lma/f0;", "F", "(Ljava/lang/Throwable;)V", "Lkotlin/Function1;", "Lkotlinx/coroutines/CompletionHandler;", "handler", "<init>", "(Lya/l;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
final class g1 extends k1 {

    /* renamed from: j, reason: collision with root package name */
    private static final /* synthetic */ AtomicIntegerFieldUpdater f18741j = AtomicIntegerFieldUpdater.newUpdater(g1.class, "_invoked");
    private volatile /* synthetic */ int _invoked = 0;

    /* renamed from: i, reason: collision with root package name */
    private final ya.l<Throwable, Unit> f18742i;

    /* JADX WARN: Multi-variable type inference failed */
    public g1(ya.l<? super Throwable, Unit> lVar) {
        this.f18742i = lVar;
    }

    @Override // td.x
    public void F(Throwable cause) {
        if (f18741j.compareAndSet(this, 0, 1)) {
            this.f18742i.invoke(cause);
        }
    }

    @Override // ya.l
    public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
        F(th);
        return Unit.f15173a;
    }
}
