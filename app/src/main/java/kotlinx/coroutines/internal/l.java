package kotlinx.coroutines.internal;

import kotlin.Metadata;

/* compiled from: LockFreeLinkedList.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\b\u0016\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\n\u0010\u000bJ\u0006\u0010\u0003\u001a\u00020\u0002R\u0011\u0010\u0007\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\t\u001a\u00020\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\u0006¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/internal/l;", "Lkotlinx/coroutines/internal/n;", "", "G", "", "F", "()Z", "isEmpty", "z", "isRemoved", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public class l extends n {
    @Override // kotlinx.coroutines.internal.n
    public /* bridge */ /* synthetic */ boolean A() {
        return ((Boolean) G()).booleanValue();
    }

    public final boolean F() {
        return t() == this;
    }

    public final Void G() {
        throw new IllegalStateException("head cannot be removed".toString());
    }

    @Override // kotlinx.coroutines.internal.n
    public boolean z() {
        return false;
    }
}
