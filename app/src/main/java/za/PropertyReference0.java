package za;

import gb.KCallable;
import gb.m;

/* compiled from: PropertyReference0.java */
/* renamed from: za.r, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class PropertyReference0 extends PropertyReference implements gb.m {
    public PropertyReference0(Object obj, Class cls, String str, String str2, int i10) {
        super(obj, cls, str, str2, i10);
    }

    @Override // za.CallableReference
    protected KCallable A() {
        return Reflection.f(this);
    }

    @Override // ya.a
    public Object invoke() {
        return get();
    }

    @Override // gb.l
    public m.a h() {
        return ((gb.m) D()).h();
    }
}
