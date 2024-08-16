package td;

import kotlin.Metadata;
import ma.Unit;

/* compiled from: CompletionState.kt */
@Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0080\b\u0018\u00002\u00020\u0001B%\u0012\b\u0010\t\u001a\u0004\u0018\u00010\u0001\u0012\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\n¢\u0006\u0004\b\u000e\u0010\u000fJ\t\u0010\u0003\u001a\u00020\u0002HÖ\u0001J\t\u0010\u0005\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\b\u001a\u00020\u00072\b\u0010\u0006\u001a\u0004\u0018\u00010\u0001HÖ\u0003¨\u0006\u0010"}, d2 = {"Ltd/w;", "", "", "toString", "", "hashCode", "other", "", "equals", "result", "Lkotlin/Function1;", "", "Lma/f0;", "onCancellation", "<init>", "(Ljava/lang/Object;Lya/l;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: td.w, reason: from toString */
/* loaded from: classes2.dex */
public final /* data */ class CompletedWithCancellation {

    /* renamed from: a, reason: collision with root package name and from toString */
    public final Object result;

    /* renamed from: b, reason: collision with root package name and from toString */
    public final ya.l<Throwable, Unit> onCancellation;

    /* JADX WARN: Multi-variable type inference failed */
    public CompletedWithCancellation(Object obj, ya.l<? super Throwable, Unit> lVar) {
        this.result = obj;
        this.onCancellation = lVar;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CompletedWithCancellation)) {
            return false;
        }
        CompletedWithCancellation completedWithCancellation = (CompletedWithCancellation) other;
        return za.k.a(this.result, completedWithCancellation.result) && za.k.a(this.onCancellation, completedWithCancellation.onCancellation);
    }

    public int hashCode() {
        Object obj = this.result;
        return ((obj == null ? 0 : obj.hashCode()) * 31) + this.onCancellation.hashCode();
    }

    public String toString() {
        return "CompletedWithCancellation(result=" + this.result + ", onCancellation=" + this.onCancellation + ')';
    }
}
