package td;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;

/* compiled from: AbstractCoroutine.kt */
@Metadata(bv = {}, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0011\b'\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00020\u00022\u00020\u00032\b\u0012\u0004\u0012\u00028\u00000\u00042\u00020\u0005B\u001f\u0012\u0006\u00101\u001a\u00020%\u0012\u0006\u00102\u001a\u00020\f\u0012\u0006\u00103\u001a\u00020\f¢\u0006\u0004\b4\u00105J\u0017\u0010\b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00028\u0000H\u0014¢\u0006\u0004\b\b\u0010\tJ\u0018\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\fH\u0014J\b\u0010\u0010\u001a\u00020\u000fH\u0014J\u0012\u0010\u0013\u001a\u00020\u00072\b\u0010\u0012\u001a\u0004\u0018\u00010\u0011H\u0004J\u001e\u0010\u0016\u001a\u00020\u00072\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00028\u00000\u0014ø\u0001\u0000¢\u0006\u0004\b\u0016\u0010\tJ\u0012\u0010\u0017\u001a\u00020\u00072\b\u0010\u0012\u001a\u0004\u0018\u00010\u0011H\u0014J\u0017\u0010\u0019\u001a\u00020\u00072\u0006\u0010\u0018\u001a\u00020\nH\u0000¢\u0006\u0004\b\u0019\u0010\u001aJ\u000f\u0010\u001b\u001a\u00020\u000fH\u0010¢\u0006\u0004\b\u001b\u0010\u001cJJ\u0010#\u001a\u00020\u0007\"\u0004\b\u0001\u0010\u001d2\u0006\u0010\u001f\u001a\u00020\u001e2\u0006\u0010 \u001a\u00028\u00012\"\u0010\"\u001a\u001e\b\u0001\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00110!ø\u0001\u0000¢\u0006\u0004\b#\u0010$R\u001d\u0010&\u001a\u00020%8\u0006¢\u0006\u0012\n\u0004\b&\u0010'\u0012\u0004\b*\u0010+\u001a\u0004\b(\u0010)R\u0014\u0010-\u001a\u00020%8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b,\u0010)R\u0014\u00100\u001a\u00020\f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b.\u0010/\u0082\u0002\u0004\n\u0002\b\u0019¨\u00066"}, d2 = {"Ltd/a;", "T", "Ltd/p1;", "Ltd/i1;", "Lqa/d;", "Ltd/h0;", ThermalBaseConfig.Item.ATTR_VALUE, "Lma/f0;", "B0", "(Ljava/lang/Object;)V", "", "cause", "", "handled", "A0", "", "t", "", "state", "d0", "Lma/p;", "result", "resumeWith", "z0", "exception", "M", "(Ljava/lang/Throwable;)V", "W", "()Ljava/lang/String;", "R", "Ltd/j0;", "start", "receiver", "Lkotlin/Function2;", "block", "C0", "(Ltd/j0;Ljava/lang/Object;Lya/p;)V", "Lqa/g;", "context", "Lqa/g;", "getContext", "()Lqa/g;", "getContext$annotations", "()V", "e", "coroutineContext", "b", "()Z", "isActive", "parentContext", "initParentJob", "active", "<init>", "(Lqa/g;ZZ)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: td.a, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractCoroutine<T> extends p1 implements qa.d<T>, h0 {

    /* renamed from: f, reason: collision with root package name */
    private final qa.g f18715f;

    public AbstractCoroutine(qa.g gVar, boolean z10, boolean z11) {
        super(z11);
        if (z10) {
            O((i1) gVar.c(i1.f18746d));
        }
        this.f18715f = gVar.o0(this);
    }

    protected void A0(Throwable th, boolean z10) {
    }

    protected void B0(T value) {
    }

    public final <R> void C0(CoroutineStart start, R receiver, ya.p<? super R, ? super qa.d<? super T>, ? extends Object> block) {
        start.b(block, receiver, this);
    }

    @Override // td.p1
    public final void M(Throwable exception) {
        f0.a(this.f18715f, exception);
    }

    @Override // td.p1
    public String W() {
        String b10 = b0.b(this.f18715f);
        if (b10 == null) {
            return super.W();
        }
        return '\"' + b10 + "\":" + super.W();
    }

    @Override // td.p1, td.i1
    public boolean b() {
        return super.b();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // td.p1
    protected final void d0(Object obj) {
        if (obj instanceof v) {
            v vVar = (v) obj;
            A0(vVar.f18800a, vVar.a());
        } else {
            B0(obj);
        }
    }

    @Override // td.h0
    /* renamed from: e, reason: from getter */
    public qa.g getF18715f() {
        return this.f18715f;
    }

    @Override // qa.d
    public final qa.g getContext() {
        return this.f18715f;
    }

    @Override // qa.d
    public final void resumeWith(Object result) {
        Object T = T(z.d(result, null, 1, null));
        if (T == q1.f18780b) {
            return;
        }
        z0(T);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // td.p1
    public String t() {
        return DebugStrings.a(this) + " was cancelled";
    }

    protected void z0(Object obj) {
        o(obj);
    }
}
