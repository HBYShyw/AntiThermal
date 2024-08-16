package xd;

import kotlin.Metadata;
import qa.g;
import ya.p;

/* compiled from: SafeCollector.kt */
@Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0003\n\u0002\b\u0005\b\u0000\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0012\u001a\u00020\u0011\u0012\u0006\u0010\u0013\u001a\u00020\u0001¢\u0006\u0004\b\u0014\u0010\u0015J8\u0010\u0007\u001a\u00028\u0000\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00028\u00002\u0018\u0010\u0006\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00028\u00000\u0004H\u0096\u0001¢\u0006\u0004\b\u0007\u0010\bJ*\u0010\f\u001a\u0004\u0018\u00018\u0000\"\b\b\u0000\u0010\t*\u00020\u00052\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00028\u00000\nH\u0096\u0003¢\u0006\u0004\b\f\u0010\rJ\u0015\u0010\u000e\u001a\u00020\u00012\n\u0010\u000b\u001a\u0006\u0012\u0002\b\u00030\nH\u0096\u0001J\u0011\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0096\u0003¨\u0006\u0016"}, d2 = {"Lxd/a;", "Lqa/g;", "R", "initial", "Lkotlin/Function2;", "Lqa/g$b;", "operation", "i0", "(Ljava/lang/Object;Lya/p;)Ljava/lang/Object;", "E", "Lqa/g$c;", "key", "c", "(Lqa/g$c;)Lqa/g$b;", "j0", "context", "o0", "", "e", "originalContext", "<init>", "(Ljava/lang/Throwable;Lqa/g;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class a implements g {

    /* renamed from: e, reason: collision with root package name */
    public final Throwable f19720e;

    /* renamed from: f, reason: collision with root package name */
    private final /* synthetic */ g f19721f;

    public a(Throwable th, g gVar) {
        this.f19720e = th;
        this.f19721f = gVar;
    }

    @Override // qa.g
    public <E extends g.b> E c(g.c<E> key) {
        return (E) this.f19721f.c(key);
    }

    @Override // qa.g
    public <R> R i0(R initial, p<? super R, ? super g.b, ? extends R> operation) {
        return (R) this.f19721f.i0(initial, operation);
    }

    @Override // qa.g
    public g j0(g.c<?> key) {
        return this.f19721f.j0(key);
    }

    @Override // qa.g
    public g o0(g context) {
        return this.f19721f.o0(context);
    }
}
