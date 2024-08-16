package kotlinx.coroutines.internal;

import kotlin.Metadata;

/* compiled from: Scopes.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\t\u001a\u00020\u0004¢\u0006\u0004\b\n\u0010\u000bJ\b\u0010\u0003\u001a\u00020\u0002H\u0016R\u001a\u0010\u0005\u001a\u00020\u00048\u0016X\u0096\u0004¢\u0006\f\n\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/internal/d;", "Ltd/h0;", "", "toString", "Lqa/g;", "coroutineContext", "Lqa/g;", "e", "()Lqa/g;", "context", "<init>", "(Lqa/g;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: kotlinx.coroutines.internal.d, reason: from toString */
/* loaded from: classes2.dex */
public final class CoroutineScope implements td.h0 {

    /* renamed from: e, reason: collision with root package name */
    private final qa.g f14349e;

    public CoroutineScope(qa.g gVar) {
        this.f14349e = gVar;
    }

    @Override // td.h0
    /* renamed from: e, reason: from getter */
    public qa.g getF14349e() {
        return this.f14349e;
    }

    public String toString() {
        return "CoroutineScope(coroutineContext=" + getF14349e() + ')';
    }
}
