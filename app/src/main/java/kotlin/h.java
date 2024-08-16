package kotlin;

import kotlin.Metadata;
import ma.Unit;
import ya.l;

/* compiled from: Channel.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a>\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\b\"\u0004\b\u0000\u0010\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u00032\u0016\b\u0002\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005¨\u0006\n"}, d2 = {"E", "", "capacity", "Lvd/e;", "onBufferOverflow", "Lkotlin/Function1;", "Lma/f0;", "onUndeliveredElement", "Lvd/f;", "a", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class h {
    public static final <E> f<E> a(int i10, BufferOverflow bufferOverflow, l<? super E, Unit> lVar) {
        if (i10 == -2) {
            return new ArrayChannel(bufferOverflow == BufferOverflow.SUSPEND ? f.f19295a.a() : 1, bufferOverflow, lVar);
        }
        if (i10 == -1) {
            if ((bufferOverflow != BufferOverflow.SUSPEND ? 0 : 1) != 0) {
                return new ConflatedChannel(lVar);
            }
            throw new IllegalArgumentException("CONFLATED capacity cannot be used with non-default onBufferOverflow".toString());
        }
        if (i10 == 0) {
            if (bufferOverflow == BufferOverflow.SUSPEND) {
                return new RendezvousChannel(lVar);
            }
            return new ArrayChannel(1, bufferOverflow, lVar);
        }
        if (i10 != Integer.MAX_VALUE) {
            if (i10 == 1 && bufferOverflow == BufferOverflow.DROP_OLDEST) {
                return new ConflatedChannel(lVar);
            }
            return new ArrayChannel(i10, bufferOverflow, lVar);
        }
        return new LinkedListChannel(lVar);
    }

    public static /* synthetic */ f b(int i10, BufferOverflow bufferOverflow, l lVar, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            i10 = 0;
        }
        if ((i11 & 2) != 0) {
            bufferOverflow = BufferOverflow.SUSPEND;
        }
        if ((i11 & 4) != 0) {
            lVar = null;
        }
        return a(i10, bufferOverflow, lVar);
    }
}
