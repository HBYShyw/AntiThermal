package za;

import gb.KCallable;
import gb.KFunction;

/* compiled from: FunctionReference.java */
/* renamed from: za.i, reason: use source file name */
/* loaded from: classes2.dex */
public class FunctionReference extends CallableReference implements FunctionBase, KFunction {

    /* renamed from: l, reason: collision with root package name */
    private final int f20369l;

    /* renamed from: m, reason: collision with root package name */
    private final int f20370m;

    public FunctionReference(int i10) {
        this(i10, CallableReference.f20349k, null, null, null, 0);
    }

    @Override // za.CallableReference
    protected KCallable A() {
        return Reflection.a(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // za.CallableReference
    /* renamed from: F, reason: merged with bridge method [inline-methods] */
    public KFunction D() {
        return (KFunction) super.D();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof FunctionReference) {
            FunctionReference functionReference = (FunctionReference) obj;
            return getName().equals(functionReference.getName()) && E().equals(functionReference.E()) && this.f20370m == functionReference.f20370m && this.f20369l == functionReference.f20369l && k.a(B(), functionReference.B()) && k.a(C(), functionReference.C());
        }
        if (obj instanceof KFunction) {
            return obj.equals(s());
        }
        return false;
    }

    @Override // za.FunctionBase
    public int getArity() {
        return this.f20369l;
    }

    public int hashCode() {
        return (((C() == null ? 0 : C().hashCode() * 31) + getName().hashCode()) * 31) + E().hashCode();
    }

    public String toString() {
        KCallable s7 = s();
        if (s7 != this) {
            return s7.toString();
        }
        if ("<init>".equals(getName())) {
            return "constructor (Kotlin reflection is not available)";
        }
        return "function " + getName() + " (Kotlin reflection is not available)";
    }

    public FunctionReference(int i10, Object obj) {
        this(i10, obj, null, null, null, 0);
    }

    public FunctionReference(int i10, Object obj, Class cls, String str, String str2, int i11) {
        super(obj, cls, str, str2, (i11 & 1) == 1);
        this.f20369l = i10;
        this.f20370m = i11 >> 1;
    }
}
