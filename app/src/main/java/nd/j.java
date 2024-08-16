package nd;

import gd.g0;
import mb.ReflectionTypes;
import nd.f;
import pb.FunctionDescriptor;
import pb.ValueParameterDescriptor;

/* compiled from: modifierChecks.kt */
/* loaded from: classes2.dex */
final class j implements f {

    /* renamed from: a, reason: collision with root package name */
    public static final j f16029a = new j();

    /* renamed from: b, reason: collision with root package name */
    private static final String f16030b = "second parameter must be of type KProperty<*> or its supertype";

    private j() {
    }

    @Override // nd.f
    public boolean a(FunctionDescriptor functionDescriptor) {
        za.k.e(functionDescriptor, "functionDescriptor");
        ValueParameterDescriptor valueParameterDescriptor = functionDescriptor.l().get(1);
        ReflectionTypes.b bVar = ReflectionTypes.f15249k;
        za.k.d(valueParameterDescriptor, "secondParameter");
        g0 a10 = bVar.a(wc.c.p(valueParameterDescriptor));
        if (a10 == null) {
            return false;
        }
        g0 type = valueParameterDescriptor.getType();
        za.k.d(type, "secondParameter.type");
        return ld.a.p(a10, ld.a.t(type));
    }

    @Override // nd.f
    public String b(FunctionDescriptor functionDescriptor) {
        return f.a.a(this, functionDescriptor);
    }

    @Override // nd.f
    public String c() {
        return f16030b;
    }
}
