package sb;

import gd.n1;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import za.DefaultConstructorMarker;

/* compiled from: ModuleAwareClassDescriptor.kt */
/* loaded from: classes2.dex */
public abstract class t implements ClassDescriptor {

    /* renamed from: e, reason: collision with root package name */
    public static final a f18390e = new a(null);

    /* compiled from: ModuleAwareClassDescriptor.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final zc.h a(ClassDescriptor classDescriptor, n1 n1Var, hd.g gVar) {
            zc.h P;
            za.k.e(classDescriptor, "<this>");
            za.k.e(n1Var, "typeSubstitution");
            za.k.e(gVar, "kotlinTypeRefiner");
            t tVar = classDescriptor instanceof t ? (t) classDescriptor : null;
            if (tVar != null && (P = tVar.P(n1Var, gVar)) != null) {
                return P;
            }
            zc.h I0 = classDescriptor.I0(n1Var);
            za.k.d(I0, "this.getMemberScope(\n   …ubstitution\n            )");
            return I0;
        }

        public final zc.h b(ClassDescriptor classDescriptor, hd.g gVar) {
            zc.h Q;
            za.k.e(classDescriptor, "<this>");
            za.k.e(gVar, "kotlinTypeRefiner");
            t tVar = classDescriptor instanceof t ? (t) classDescriptor : null;
            if (tVar != null && (Q = tVar.Q(gVar)) != null) {
                return Q;
            }
            zc.h M0 = classDescriptor.M0();
            za.k.d(M0, "this.unsubstitutedMemberScope");
            return M0;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract zc.h P(n1 n1Var, hd.g gVar);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract zc.h Q(hd.g gVar);

    @Override // pb.ClassDescriptor, pb.DeclarationDescriptor
    public /* bridge */ /* synthetic */ ClassifierDescriptor a() {
        return a();
    }

    @Override // pb.DeclarationDescriptor
    public /* bridge */ /* synthetic */ DeclarationDescriptor a() {
        return a();
    }
}
