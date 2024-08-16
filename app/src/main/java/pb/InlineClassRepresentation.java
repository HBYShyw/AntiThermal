package pb;

import java.util.List;
import kd.k;
import kotlin.collections.CollectionsJVM;
import oc.Name;

/* compiled from: InlineClassRepresentation.kt */
/* renamed from: pb.z, reason: use source file name */
/* loaded from: classes2.dex */
public final class InlineClassRepresentation<Type extends kd.k> extends ValueClassRepresentation<Type> {

    /* renamed from: a, reason: collision with root package name */
    private final Name f16758a;

    /* renamed from: b, reason: collision with root package name */
    private final Type f16759b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public InlineClassRepresentation(Name name, Type type) {
        super(null);
        za.k.e(name, "underlyingPropertyName");
        za.k.e(type, "underlyingType");
        this.f16758a = name;
        this.f16759b = type;
    }

    @Override // pb.ValueClassRepresentation
    public List<ma.o<Name, Type>> a() {
        List<ma.o<Name, Type>> e10;
        e10 = CollectionsJVM.e(ma.u.a(this.f16758a, this.f16759b));
        return e10;
    }

    public final Name c() {
        return this.f16758a;
    }

    public final Type d() {
        return this.f16759b;
    }

    public String toString() {
        return "InlineClassRepresentation(underlyingPropertyName=" + this.f16758a + ", underlyingType=" + this.f16759b + ')';
    }
}
