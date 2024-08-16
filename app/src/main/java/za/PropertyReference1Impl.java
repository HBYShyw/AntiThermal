package za;

import gb.KClass;
import gb.KDeclarationContainer;

/* compiled from: PropertyReference1Impl.java */
/* renamed from: za.u, reason: use source file name */
/* loaded from: classes2.dex */
public class PropertyReference1Impl extends PropertyReference1 {
    public PropertyReference1Impl(KDeclarationContainer kDeclarationContainer, String str, String str2) {
        super(CallableReference.f20349k, ((ClassBasedDeclarationContainer) kDeclarationContainer).e(), str, str2, !(kDeclarationContainer instanceof KClass) ? 1 : 0);
    }

    @Override // gb.n
    public Object get(Object obj) {
        return h().d(obj);
    }
}
