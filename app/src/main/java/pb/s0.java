package pb;

import gd.TypeProjection;
import java.util.List;

/* compiled from: typeParameterUtils.kt */
/* loaded from: classes2.dex */
public final class s0 {

    /* renamed from: a, reason: collision with root package name */
    private final ClassifierDescriptorWithTypeParameters f16726a;

    /* renamed from: b, reason: collision with root package name */
    private final List<TypeProjection> f16727b;

    /* renamed from: c, reason: collision with root package name */
    private final s0 f16728c;

    /* JADX WARN: Multi-variable type inference failed */
    public s0(ClassifierDescriptorWithTypeParameters classifierDescriptorWithTypeParameters, List<? extends TypeProjection> list, s0 s0Var) {
        za.k.e(classifierDescriptorWithTypeParameters, "classifierDescriptor");
        za.k.e(list, "arguments");
        this.f16726a = classifierDescriptorWithTypeParameters;
        this.f16727b = list;
        this.f16728c = s0Var;
    }

    public final List<TypeProjection> a() {
        return this.f16727b;
    }

    public final ClassifierDescriptorWithTypeParameters b() {
        return this.f16726a;
    }

    public final s0 c() {
        return this.f16728c;
    }
}
