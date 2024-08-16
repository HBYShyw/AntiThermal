package kotlin;

import kotlin.Metadata;
import kotlinx.coroutines.internal.Symbol;
import ma.Unit;
import ya.l;

/* compiled from: LinkedListChannel.kt */
@Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000\u0002B)\u0012 \u0010\u0010\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u000e\u0018\u00010\rj\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\u000f¢\u0006\u0004\b\u0011\u0010\u0012J\u0017\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00028\u0000H\u0014¢\u0006\u0004\b\u0005\u0010\u0006R\u0014\u0010\n\u001a\u00020\u00078DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\f\u001a\u00020\u00078DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\t¨\u0006\u0013"}, d2 = {"Lvd/n;", "E", "Lvd/a;", "element", "", "i", "(Ljava/lang/Object;)Ljava/lang/Object;", "", "r", "()Z", "isBufferAlwaysEmpty", "s", "isBufferEmpty", "Lkotlin/Function1;", "Lma/f0;", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "onUndeliveredElement", "<init>", "(Lya/l;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: vd.n, reason: use source file name */
/* loaded from: classes2.dex */
public class LinkedListChannel<E> extends a<E> {
    public LinkedListChannel(l<? super E, Unit> lVar) {
        super(lVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlin.c
    public Object i(E element) {
        q<?> k10;
        do {
            Object i10 = super.i(element);
            Symbol symbol = Function1.f19276b;
            if (i10 == symbol) {
                return symbol;
            }
            if (i10 == Function1.f19277c) {
                k10 = k(element);
                if (k10 == null) {
                    return symbol;
                }
            } else {
                if (i10 instanceof j) {
                    return i10;
                }
                throw new IllegalStateException(("Invalid offerInternal result " + i10).toString());
            }
        } while (!(k10 instanceof j));
        return k10;
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
