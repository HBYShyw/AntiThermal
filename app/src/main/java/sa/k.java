package sa;

import za.FunctionBase;
import za.Reflection;

/* compiled from: ContinuationImpl.kt */
/* loaded from: classes2.dex */
public abstract class k extends d implements FunctionBase<Object> {

    /* renamed from: h, reason: collision with root package name */
    private final int f18201h;

    public k(int i10, qa.d<Object> dVar) {
        super(dVar);
        this.f18201h = i10;
    }

    @Override // za.FunctionBase
    public int getArity() {
        return this.f18201h;
    }

    @Override // sa.a
    public String toString() {
        if (getCompletion() == null) {
            String h10 = Reflection.h(this);
            za.k.d(h10, "renderLambdaToString(this)");
            return h10;
        }
        return super.toString();
    }

    public k(int i10) {
        this(i10, null);
    }
}
