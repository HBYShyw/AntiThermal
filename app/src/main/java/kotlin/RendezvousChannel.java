package kotlin;

import kotlin.Metadata;
import ma.Unit;
import ya.l;

/* compiled from: RendezvousChannel.kt */
@Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000\u0002B)\u0012 \u0010\f\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\n\u0018\u00010\tj\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\u000b¢\u0006\u0004\b\r\u0010\u000eR\u0014\u0010\u0006\u001a\u00020\u00038DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0014\u0010\b\u001a\u00020\u00038DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005¨\u0006\u000f"}, d2 = {"Lvd/r;", "E", "Lvd/a;", "", "r", "()Z", "isBufferAlwaysEmpty", "s", "isBufferEmpty", "Lkotlin/Function1;", "Lma/f0;", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "onUndeliveredElement", "<init>", "(Lya/l;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: vd.r, reason: use source file name */
/* loaded from: classes2.dex */
public class RendezvousChannel<E> extends a<E> {
    public RendezvousChannel(l<? super E, Unit> lVar) {
        super(lVar);
    }

    @Override // kotlin.a
    protected final boolean r() {
        return true;
    }

    @Override // kotlin.a
    protected final boolean s() {
        return true;
    }
}
