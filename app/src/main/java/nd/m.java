package nd;

import java.util.Collection;
import java.util.List;
import nd.f;
import pb.FunctionDescriptor;
import pb.ValueParameterDescriptor;

/* compiled from: modifierChecks.kt */
/* loaded from: classes2.dex */
final class m implements f {

    /* renamed from: a, reason: collision with root package name */
    public static final m f16035a = new m();

    /* renamed from: b, reason: collision with root package name */
    private static final String f16036b = "should not have varargs or parameters with default values";

    private m() {
    }

    @Override // nd.f
    public boolean a(FunctionDescriptor functionDescriptor) {
        za.k.e(functionDescriptor, "functionDescriptor");
        List<ValueParameterDescriptor> l10 = functionDescriptor.l();
        za.k.d(l10, "functionDescriptor.valueParameters");
        if (!(l10 instanceof Collection) || !l10.isEmpty()) {
            for (ValueParameterDescriptor valueParameterDescriptor : l10) {
                za.k.d(valueParameterDescriptor, "it");
                if (!(!wc.c.c(valueParameterDescriptor) && valueParameterDescriptor.q0() == null)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override // nd.f
    public String b(FunctionDescriptor functionDescriptor) {
        return f.a.a(this, functionDescriptor);
    }

    @Override // nd.f
    public String c() {
        return f16036b;
    }
}
