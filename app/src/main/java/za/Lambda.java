package za;

import java.io.Serializable;

/* compiled from: Lambda.kt */
/* renamed from: za.m, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class Lambda<R> implements FunctionBase<R>, Serializable {
    private final int arity;

    public Lambda(int i10) {
        this.arity = i10;
    }

    @Override // za.FunctionBase
    public int getArity() {
        return this.arity;
    }

    public String toString() {
        String i10 = Reflection.i(this);
        k.d(i10, "renderLambdaToString(this)");
        return i10;
    }
}
