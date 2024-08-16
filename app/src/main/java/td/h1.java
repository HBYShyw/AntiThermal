package td;

import kotlin.Metadata;
import ma.Unit;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: JobSupport.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B!\u0012\u0018\u0010\b\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0012\u0004\u0012\u00020\u00040\u0006j\u0002`\u0007¢\u0006\u0004\b\t\u0010\nJ\u0013\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0096\u0002¨\u0006\u000b"}, d2 = {"Ltd/h1;", "Ltd/o1;", "", "cause", "Lma/f0;", "F", "Lkotlin/Function1;", "Lkotlinx/coroutines/CompletionHandler;", "handler", "<init>", "(Lya/l;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class h1 extends o1 {

    /* renamed from: i, reason: collision with root package name */
    private final ya.l<Throwable, Unit> f18745i;

    /* JADX WARN: Multi-variable type inference failed */
    public h1(ya.l<? super Throwable, Unit> lVar) {
        this.f18745i = lVar;
    }

    @Override // td.x
    public void F(Throwable th) {
        this.f18745i.invoke(th);
    }

    @Override // ya.l
    public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
        F(th);
        return Unit.f15173a;
    }
}
