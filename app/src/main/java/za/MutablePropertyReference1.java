package za;

import gb.KCallable;
import gb.j;
import gb.n;

/* compiled from: MutablePropertyReference1.java */
/* renamed from: za.n, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class MutablePropertyReference1 extends MutablePropertyReference implements gb.j {
    public MutablePropertyReference1(Object obj, Class cls, String str, String str2, int i10) {
        super(obj, cls, str, str2, i10);
    }

    @Override // za.CallableReference
    protected KCallable A() {
        return Reflection.e(this);
    }

    @Override // ya.l
    public Object invoke(Object obj) {
        return get(obj);
    }

    @Override // gb.l
    public n.a h() {
        return ((gb.j) D()).h();
    }

    @Override // gb.i
    public j.a k() {
        return ((gb.j) D()).k();
    }
}
