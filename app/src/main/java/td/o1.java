package td;

import kotlin.Metadata;

/* compiled from: JobSupport.kt */
@Metadata(bv = {}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\b \u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003B\u0007¢\u0006\u0004\b\u0017\u0010\u0018J\b\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\u0007\u001a\u00020\u0006H\u0016R\"\u0010\t\u001a\u00020\b8\u0006@\u0006X\u0086.¢\u0006\u0012\n\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0014\u0010\u0012\u001a\u00020\u000f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0016\u0010\u0016\u001a\u0004\u0018\u00010\u00138VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015¨\u0006\u0019"}, d2 = {"Ltd/o1;", "Ltd/x;", "Ltd/u0;", "Ltd/d1;", "Lma/f0;", "a", "", "toString", "Ltd/p1;", "job", "Ltd/p1;", "G", "()Ltd/p1;", "H", "(Ltd/p1;)V", "", "b", "()Z", "isActive", "Ltd/t1;", "f", "()Ltd/t1;", "list", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public abstract class o1 extends x implements u0, d1 {

    /* renamed from: h, reason: collision with root package name */
    public p1 f18767h;

    public final p1 G() {
        p1 p1Var = this.f18767h;
        if (p1Var != null) {
            return p1Var;
        }
        za.k.s("job");
        return null;
    }

    public final void H(p1 p1Var) {
        this.f18767h = p1Var;
    }

    @Override // td.u0
    public void a() {
        G().l0(this);
    }

    @Override // td.d1
    /* renamed from: b */
    public boolean getF18801e() {
        return true;
    }

    @Override // td.d1
    /* renamed from: f */
    public t1 getF18775e() {
        return null;
    }

    @Override // kotlinx.coroutines.internal.n
    public String toString() {
        return DebugStrings.a(this) + '@' + DebugStrings.b(this) + "[job@" + DebugStrings.b(G()) + ']';
    }
}
