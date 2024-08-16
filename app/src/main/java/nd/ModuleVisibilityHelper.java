package nd;

import pb.DeclarationDescriptor;

/* compiled from: ModuleVisibilityHelper.kt */
/* renamed from: nd.l, reason: use source file name */
/* loaded from: classes2.dex */
public interface ModuleVisibilityHelper {

    /* compiled from: ModuleVisibilityHelper.kt */
    /* renamed from: nd.l$a */
    /* loaded from: classes2.dex */
    public static final class a implements ModuleVisibilityHelper {

        /* renamed from: a, reason: collision with root package name */
        public static final a f16034a = new a();

        private a() {
        }

        @Override // nd.ModuleVisibilityHelper
        public boolean a(DeclarationDescriptor declarationDescriptor, DeclarationDescriptor declarationDescriptor2) {
            za.k.e(declarationDescriptor, "what");
            za.k.e(declarationDescriptor2, "from");
            return true;
        }
    }

    boolean a(DeclarationDescriptor declarationDescriptor, DeclarationDescriptor declarationDescriptor2);
}
