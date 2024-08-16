package za;

import gb.KCallable;
import gb.n;

/* compiled from: PropertyReference1.java */
/* renamed from: za.t, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class PropertyReference1 extends PropertyReference implements gb.n {
    public PropertyReference1(Object obj, Class cls, String str, String str2, int i10) {
        super(obj, cls, str, str2, i10);
    }

    @Override // za.CallableReference
    protected KCallable A() {
        return Reflection.g(this);
    }

    @Override // ya.l
    public Object invoke(Object obj) {
        return get(obj);
    }

    @Override // gb.l
    public n.a h() {
        return ((gb.n) D()).h();
    }
}
