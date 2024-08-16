package pb;

import java.util.List;
import java.util.Map;
import kd.k;
import oc.Name;

/* compiled from: MultiFieldValueClassRepresentation.kt */
/* renamed from: pb.i0, reason: use source file name */
/* loaded from: classes2.dex */
public final class MultiFieldValueClassRepresentation<Type extends kd.k> extends ValueClassRepresentation<Type> {

    /* renamed from: a, reason: collision with root package name */
    private final List<ma.o<Name, Type>> f16694a;

    /* renamed from: b, reason: collision with root package name */
    private final Map<Name, Type> f16695b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public MultiFieldValueClassRepresentation(List<? extends ma.o<Name, ? extends Type>> list) {
        super(null);
        Map<Name, Type> q10;
        za.k.e(list, "underlyingPropertyNamesToTypes");
        this.f16694a = list;
        q10 = kotlin.collections.m0.q(a());
        if (q10.size() == a().size()) {
            this.f16695b = q10;
            return;
        }
        throw new IllegalArgumentException("Some properties have the same names".toString());
    }

    @Override // pb.ValueClassRepresentation
    public List<ma.o<Name, Type>> a() {
        return this.f16694a;
    }

    public String toString() {
        return "MultiFieldValueClassRepresentation(underlyingPropertyNamesToTypes=" + a() + ')';
    }
}
