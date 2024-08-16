package androidx.lifecycle;

import androidx.lifecycle.h;
import kotlin.Metadata;
import ma.Unit;
import td.Dispatchers;
import td.m1;

/* compiled from: Lifecycle.kt */
@Metadata(bv = {}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u0017\u0012\u0006\u0010\u000f\u001a\u00020\n\u0012\u0006\u0010\u0011\u001a\u00020\u0010¢\u0006\u0004\b\u0014\u0010\u0015J\u0006\u0010\u0004\u001a\u00020\u0003J\u0018\u0010\t\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0007H\u0016R\u001a\u0010\u000f\u001a\u00020\n8\u0010X\u0090\u0004¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000eR\u001a\u0010\u0011\u001a\u00020\u00108\u0016X\u0096\u0004¢\u0006\f\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\u000b\u0010\u0013¨\u0006\u0016"}, d2 = {"Landroidx/lifecycle/LifecycleCoroutineScopeImpl;", "Landroidx/lifecycle/i;", "Landroidx/lifecycle/l;", "Lma/f0;", "c", "Landroidx/lifecycle/o;", "source", "Landroidx/lifecycle/h$b;", "event", "a", "Landroidx/lifecycle/h;", "e", "Landroidx/lifecycle/h;", "b", "()Landroidx/lifecycle/h;", "lifecycle", "Lqa/g;", "coroutineContext", "Lqa/g;", "()Lqa/g;", "<init>", "(Landroidx/lifecycle/h;Lqa/g;)V", "lifecycle-runtime-ktx_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class LifecycleCoroutineScopeImpl extends i implements LifecycleEventObserver {

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private final h lifecycle;

    /* renamed from: f, reason: collision with root package name */
    private final qa.g f3081f;

    /* compiled from: Lifecycle.kt */
    @Metadata(bv = {}, d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Ltd/h0;", "Lma/f0;", "<anonymous>"}, k = 3, mv = {1, 6, 0})
    @sa.f(c = "androidx.lifecycle.LifecycleCoroutineScopeImpl$register$1", f = "Lifecycle.kt", l = {}, m = "invokeSuspend")
    /* loaded from: classes.dex */
    static final class a extends sa.k implements ya.p<td.h0, qa.d<? super Unit>, Object> {

        /* renamed from: i, reason: collision with root package name */
        int f3082i;

        /* renamed from: j, reason: collision with root package name */
        private /* synthetic */ Object f3083j;

        a(qa.d<? super a> dVar) {
            super(2, dVar);
        }

        @Override // sa.a
        public final qa.d<Unit> create(Object obj, qa.d<?> dVar) {
            a aVar = new a(dVar);
            aVar.f3083j = obj;
            return aVar;
        }

        @Override // ya.p
        public final Object invoke(td.h0 h0Var, qa.d<? super Unit> dVar) {
            return ((a) create(h0Var, dVar)).invokeSuspend(Unit.f15173a);
        }

        @Override // sa.a
        public final Object invokeSuspend(Object obj) {
            ra.d.c();
            if (this.f3082i != 0) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ma.q.b(obj);
            td.h0 h0Var = (td.h0) this.f3083j;
            if (LifecycleCoroutineScopeImpl.this.getLifecycle().b().compareTo(h.c.INITIALIZED) >= 0) {
                LifecycleCoroutineScopeImpl.this.getLifecycle().a(LifecycleCoroutineScopeImpl.this);
            } else {
                m1.d(h0Var.getF14349e(), null, 1, null);
            }
            return Unit.f15173a;
        }
    }

    public LifecycleCoroutineScopeImpl(h hVar, qa.g gVar) {
        za.k.e(hVar, "lifecycle");
        za.k.e(gVar, "coroutineContext");
        this.lifecycle = hVar;
        this.f3081f = gVar;
        if (getLifecycle().b() == h.c.DESTROYED) {
            m1.d(getF14349e(), null, 1, null);
        }
    }

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void a(o oVar, h.b bVar) {
        za.k.e(oVar, "source");
        za.k.e(bVar, "event");
        if (getLifecycle().b().compareTo(h.c.DESTROYED) <= 0) {
            getLifecycle().c(this);
            m1.d(getF14349e(), null, 1, null);
        }
    }

    /* renamed from: b, reason: from getter */
    public h getLifecycle() {
        return this.lifecycle;
    }

    public final void c() {
        td.g.b(this, Dispatchers.c().getF19019j(), null, new a(null), 2, null);
    }

    @Override // td.h0
    /* renamed from: e, reason: from getter */
    public qa.g getF14349e() {
        return this.f3081f;
    }
}
