package kotlin;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.n;
import td.DebugStrings;
import td.m;

/* compiled from: AbstractChannel.kt */
@Metadata(bv = {}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u000b\b\u0000\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00020\u00022\b\u0012\u0004\u0012\u00028\u00000\u0003J\u0012\u0010\u0007\u001a\u00020\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H\u0016J\b\u0010\t\u001a\u00020\bH\u0016J!\u0010\u000b\u001a\u00020\u00062\u0006\u0010\n\u001a\u00028\u00002\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H\u0016¢\u0006\u0004\b\u000b\u0010\fJ\u0017\u0010\r\u001a\u00020\b2\u0006\u0010\n\u001a\u00028\u0000H\u0016¢\u0006\u0004\b\r\u0010\u000eJ\b\u0010\u0010\u001a\u00020\u000fH\u0016R\u0011\u0010\u0014\u001a\u00020\u00118F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0016\u001a\u00020\u00118F¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0013R\u001a\u0010\u0019\u001a\b\u0012\u0004\u0012\u00028\u00000\u00008VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018R\u001a\u0010\u001b\u001a\b\u0012\u0004\u0012\u00028\u00000\u00008VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u0018¨\u0006\u001c"}, d2 = {"Lvd/j;", "E", "Lvd/s;", "Lvd/q;", "Lkotlinx/coroutines/internal/n$b;", "otherOp", "Lkotlinx/coroutines/internal/a0;", "H", "Lma/f0;", "F", ThermalBaseConfig.Item.ATTR_VALUE, "j", "(Ljava/lang/Object;Lkotlinx/coroutines/internal/n$b;)Lkotlinx/coroutines/internal/a0;", "i", "(Ljava/lang/Object;)V", "", "toString", "", "M", "()Ljava/lang/Throwable;", "sendException", "L", "receiveException", "J", "()Lvd/j;", "offerResult", "K", "pollResult", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class j<E> extends s implements q<E> {

    /* renamed from: h, reason: collision with root package name */
    public final Throwable f19301h;

    @Override // kotlin.s
    public void F() {
    }

    @Override // kotlin.s
    public Symbol H(n.b otherOp) {
        return m.f18761a;
    }

    @Override // kotlin.q
    /* renamed from: J, reason: merged with bridge method [inline-methods] */
    public j<E> c() {
        return this;
    }

    @Override // kotlin.s
    /* renamed from: K, reason: merged with bridge method [inline-methods] */
    public j<E> G() {
        return this;
    }

    public final Throwable L() {
        Throwable th = this.f19301h;
        return th == null ? new k("Channel was closed") : th;
    }

    public final Throwable M() {
        Throwable th = this.f19301h;
        return th == null ? new l("Channel was closed") : th;
    }

    @Override // kotlin.q
    public void i(E value) {
    }

    @Override // kotlin.q
    public Symbol j(E value, n.b otherOp) {
        return m.f18761a;
    }

    @Override // kotlinx.coroutines.internal.n
    public String toString() {
        return "Closed@" + DebugStrings.b(this) + '[' + this.f19301h + ']';
    }
}
