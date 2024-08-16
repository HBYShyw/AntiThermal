package td;

import kotlin.Metadata;
import ma.Unit;
import za.DefaultConstructorMarker;

/* compiled from: CancellableContinuationImpl.kt */
@Metadata(bv = {}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0082\b\u0018\u00002\u00020\u0001BM\u0012\b\u0010\b\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\t\u0012\u0016\b\u0002\u0010\f\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u000b\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0004¢\u0006\u0004\b\u001a\u0010\u001bJ\u001a\u0010\u0007\u001a\u00020\u00062\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u00022\u0006\u0010\u0005\u001a\u00020\u0004JQ\u0010\u000f\u001a\u00020\u00002\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\t2\u0016\b\u0002\u0010\f\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u000b2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0004HÆ\u0001J\t\u0010\u0011\u001a\u00020\u0010HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0012HÖ\u0001J\u0013\u0010\u0016\u001a\u00020\u00152\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003R\u0011\u0010\u0019\u001a\u00020\u00158F¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018¨\u0006\u001c"}, d2 = {"Ltd/u;", "", "Ltd/l;", "cont", "", "cause", "Lma/f0;", "d", "result", "Ltd/i;", "cancelHandler", "Lkotlin/Function1;", "onCancellation", "idempotentResume", "cancelCause", "a", "", "toString", "", "hashCode", "other", "", "equals", "c", "()Z", "cancelled", "<init>", "(Ljava/lang/Object;Ltd/i;Lya/l;Ljava/lang/Object;Ljava/lang/Throwable;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: td.u, reason: from toString */
/* loaded from: classes2.dex */
final /* data */ class CompletedContinuation {

    /* renamed from: a, reason: collision with root package name and from toString */
    public final Object result;

    /* renamed from: b, reason: collision with root package name and from toString */
    public final i cancelHandler;

    /* renamed from: c, reason: collision with root package name and from toString */
    public final ya.l<Throwable, Unit> onCancellation;

    /* renamed from: d, reason: collision with root package name and from toString */
    public final Object idempotentResume;

    /* renamed from: e, reason: collision with root package name and from toString */
    public final Throwable cancelCause;

    /* JADX WARN: Multi-variable type inference failed */
    public CompletedContinuation(Object obj, i iVar, ya.l<? super Throwable, Unit> lVar, Object obj2, Throwable th) {
        this.result = obj;
        this.cancelHandler = iVar;
        this.onCancellation = lVar;
        this.idempotentResume = obj2;
        this.cancelCause = th;
    }

    public static /* synthetic */ CompletedContinuation b(CompletedContinuation completedContinuation, Object obj, i iVar, ya.l lVar, Object obj2, Throwable th, int i10, Object obj3) {
        if ((i10 & 1) != 0) {
            obj = completedContinuation.result;
        }
        if ((i10 & 2) != 0) {
            iVar = completedContinuation.cancelHandler;
        }
        i iVar2 = iVar;
        if ((i10 & 4) != 0) {
            lVar = completedContinuation.onCancellation;
        }
        ya.l lVar2 = lVar;
        if ((i10 & 8) != 0) {
            obj2 = completedContinuation.idempotentResume;
        }
        Object obj4 = obj2;
        if ((i10 & 16) != 0) {
            th = completedContinuation.cancelCause;
        }
        return completedContinuation.a(obj, iVar2, lVar2, obj4, th);
    }

    public final CompletedContinuation a(Object obj, i iVar, ya.l<? super Throwable, Unit> lVar, Object obj2, Throwable th) {
        return new CompletedContinuation(obj, iVar, lVar, obj2, th);
    }

    public final boolean c() {
        return this.cancelCause != null;
    }

    public final void d(l<?> lVar, Throwable th) {
        i iVar = this.cancelHandler;
        if (iVar != null) {
            lVar.n(iVar, th);
        }
        ya.l<Throwable, Unit> lVar2 = this.onCancellation;
        if (lVar2 != null) {
            lVar.p(lVar2, th);
        }
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CompletedContinuation)) {
            return false;
        }
        CompletedContinuation completedContinuation = (CompletedContinuation) other;
        return za.k.a(this.result, completedContinuation.result) && za.k.a(this.cancelHandler, completedContinuation.cancelHandler) && za.k.a(this.onCancellation, completedContinuation.onCancellation) && za.k.a(this.idempotentResume, completedContinuation.idempotentResume) && za.k.a(this.cancelCause, completedContinuation.cancelCause);
    }

    public int hashCode() {
        Object obj = this.result;
        int hashCode = (obj == null ? 0 : obj.hashCode()) * 31;
        i iVar = this.cancelHandler;
        int hashCode2 = (hashCode + (iVar == null ? 0 : iVar.hashCode())) * 31;
        ya.l<Throwable, Unit> lVar = this.onCancellation;
        int hashCode3 = (hashCode2 + (lVar == null ? 0 : lVar.hashCode())) * 31;
        Object obj2 = this.idempotentResume;
        int hashCode4 = (hashCode3 + (obj2 == null ? 0 : obj2.hashCode())) * 31;
        Throwable th = this.cancelCause;
        return hashCode4 + (th != null ? th.hashCode() : 0);
    }

    public String toString() {
        return "CompletedContinuation(result=" + this.result + ", cancelHandler=" + this.cancelHandler + ", onCancellation=" + this.onCancellation + ", idempotentResume=" + this.idempotentResume + ", cancelCause=" + this.cancelCause + ')';
    }

    public /* synthetic */ CompletedContinuation(Object obj, i iVar, ya.l lVar, Object obj2, Throwable th, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(obj, (i10 & 2) != 0 ? null : iVar, (i10 & 4) != 0 ? null : lVar, (i10 & 8) != 0 ? null : obj2, (i10 & 16) != 0 ? null : th);
    }
}
