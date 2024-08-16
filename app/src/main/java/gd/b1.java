package gd;

import pb.DeclarationDescriptor;

/* compiled from: TypeAttributeTranslator.kt */
/* loaded from: classes2.dex */
public interface b1 {

    /* compiled from: TypeAttributeTranslator.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        public static /* synthetic */ c1 a(b1 b1Var, qb.g gVar, TypeConstructor typeConstructor, DeclarationDescriptor declarationDescriptor, int i10, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: toAttributes");
            }
            if ((i10 & 2) != 0) {
                typeConstructor = null;
            }
            if ((i10 & 4) != 0) {
                declarationDescriptor = null;
            }
            return b1Var.a(gVar, typeConstructor, declarationDescriptor);
        }
    }

    c1 a(qb.g gVar, TypeConstructor typeConstructor, DeclarationDescriptor declarationDescriptor);
}
