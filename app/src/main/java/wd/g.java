package wd;

import kotlin.Metadata;
import ma.Unit;
import ya.p;

/* compiled from: Builders.kt */
@Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000\u0002B4\u0012(\u0010\u000b\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0003\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t\u0012\u0006\u0012\u0004\u0018\u00010\n0\bø\u0001\u0000¢\u0006\u0004\b\f\u0010\rJ!\u0010\u0006\u001a\u00020\u00052\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0096@ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000e"}, d2 = {"Lwd/g;", "T", "Lwd/a;", "Lwd/c;", "collector", "Lma/f0;", "a", "(Lwd/c;Lqa/d;)Ljava/lang/Object;", "Lkotlin/Function2;", "Lqa/d;", "", "block", "<init>", "(Lya/p;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
final class g<T> extends a<T> {

    /* renamed from: e, reason: collision with root package name */
    private final p<FlowCollector<? super T>, qa.d<? super Unit>, Object> f19445e;

    /* JADX WARN: Multi-variable type inference failed */
    public g(p<? super FlowCollector<? super T>, ? super qa.d<? super Unit>, ? extends Object> pVar) {
        this.f19445e = pVar;
    }

    @Override // wd.a
    public Object a(FlowCollector<? super T> flowCollector, qa.d<? super Unit> dVar) {
        Object invoke = this.f19445e.invoke(flowCollector, dVar);
        return invoke == ra.b.c() ? invoke : Unit.f15173a;
    }
}
