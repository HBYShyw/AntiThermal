package za;

import gb.KClass;
import gb.KDeclarationContainer;

/* compiled from: MutablePropertyReference1Impl.java */
/* renamed from: za.o, reason: use source file name */
/* loaded from: classes2.dex */
public class MutablePropertyReference1Impl extends MutablePropertyReference1 {
    public MutablePropertyReference1Impl(KDeclarationContainer kDeclarationContainer, String str, String str2) {
        super(CallableReference.f20349k, ((ClassBasedDeclarationContainer) kDeclarationContainer).e(), str, str2, !(kDeclarationContainer instanceof KClass) ? 1 : 0);
    }

    @Override // gb.n
    public Object get(Object obj) {
        return h().d(obj);
    }

    @Override // gb.j
    public void x(Object obj, Object obj2) {
        k().d(obj, obj2);
    }
}
