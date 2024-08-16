package androidx.lifecycle;

import androidx.lifecycle.h;
import kotlin.Metadata;
import ma.p;

/* compiled from: WithLifecycleState.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001a\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016¨\u0006\b"}, d2 = {"androidx/lifecycle/WithLifecycleStateKt$suspendWithStateAtLeastUnchecked$2$observer$1", "Landroidx/lifecycle/l;", "Landroidx/lifecycle/o;", "source", "Landroidx/lifecycle/h$b;", "event", "Lma/f0;", "a", "lifecycle-runtime-ktx_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class WithLifecycleStateKt$suspendWithStateAtLeastUnchecked$2$observer$1 implements LifecycleEventObserver {

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ h.c f3143e;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ h f3144f;

    /* renamed from: g, reason: collision with root package name */
    final /* synthetic */ td.k<Object> f3145g;

    /* renamed from: h, reason: collision with root package name */
    final /* synthetic */ ya.a<Object> f3146h;

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void a(o oVar, h.b bVar) {
        Object a10;
        za.k.e(oVar, "source");
        za.k.e(bVar, "event");
        if (bVar == h.b.d(this.f3143e)) {
            this.f3144f.c(this);
            td.k<Object> kVar = this.f3145g;
            ya.a<Object> aVar = this.f3146h;
            try {
                p.a aVar2 = ma.p.f15184e;
                a10 = ma.p.a(aVar.invoke());
            } catch (Throwable th) {
                p.a aVar3 = ma.p.f15184e;
                a10 = ma.p.a(ma.q.a(th));
            }
            kVar.resumeWith(a10);
            return;
        }
        if (bVar == h.b.ON_DESTROY) {
            this.f3144f.c(this);
            td.k<Object> kVar2 = this.f3145g;
            p.a aVar4 = ma.p.f15184e;
            kVar2.resumeWith(ma.p.a(ma.q.a(new j())));
        }
    }
}
