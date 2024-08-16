package nd;

import java.util.List;
import nd.g;
import pb.FunctionDescriptor;

/* compiled from: modifierChecks.kt */
/* loaded from: classes2.dex */
public abstract class b {
    public final g a(FunctionDescriptor functionDescriptor) {
        za.k.e(functionDescriptor, "functionDescriptor");
        for (h hVar : b()) {
            if (hVar.b(functionDescriptor)) {
                return hVar.a(functionDescriptor);
            }
        }
        return g.a.f16017b;
    }

    public abstract List<h> b();
}
