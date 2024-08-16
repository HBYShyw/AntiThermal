package td;

import kotlin.Metadata;
import ma.Unit;

/* compiled from: Builders.common.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0012\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0017\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\u0006\u0010\t\u001a\u00020\u0005¢\u0006\u0004\b\n\u0010\u000bJ\u0010\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0003H\u0014¨\u0006\f"}, d2 = {"Ltd/y1;", "Ltd/a;", "Lma/f0;", "", "exception", "", "K", "Lqa/g;", "parentContext", "active", "<init>", "(Lqa/g;Z)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
class y1 extends AbstractCoroutine<Unit> {
    public y1(qa.g gVar, boolean z10) {
        super(gVar, true, z10);
    }

    @Override // td.p1
    protected boolean K(Throwable exception) {
        f0.a(getContext(), exception);
        return true;
    }
}
