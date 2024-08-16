package zc;

import java.util.Collection;
import oc.Name;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;

/* compiled from: ResolutionScope.kt */
/* renamed from: zc.k, reason: use source file name */
/* loaded from: classes2.dex */
public interface ResolutionScope {

    /* compiled from: ResolutionScope.kt */
    /* renamed from: zc.k$a */
    /* loaded from: classes2.dex */
    public static final class a {
        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Collection a(ResolutionScope resolutionScope, d dVar, ya.l lVar, int i10, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getContributedDescriptors");
            }
            if ((i10 & 1) != 0) {
                dVar = d.f20436o;
            }
            if ((i10 & 2) != 0) {
                lVar = h.f20461a.a();
            }
            return resolutionScope.g(dVar, lVar);
        }
    }

    ClassifierDescriptor e(Name name, xb.b bVar);

    Collection<DeclarationDescriptor> g(d dVar, ya.l<? super Name, Boolean> lVar);
}
