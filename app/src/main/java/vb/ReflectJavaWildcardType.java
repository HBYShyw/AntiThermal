package vb;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.List;
import kotlin.collections._Arrays;
import vb.ReflectJavaType;

/* compiled from: ReflectJavaWildcardType.kt */
/* renamed from: vb.c0, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReflectJavaWildcardType extends ReflectJavaType implements fc.c0 {

    /* renamed from: b, reason: collision with root package name */
    private final WildcardType f19222b;

    /* renamed from: c, reason: collision with root package name */
    private final Collection<fc.a> f19223c;

    /* renamed from: d, reason: collision with root package name */
    private final boolean f19224d;

    public ReflectJavaWildcardType(WildcardType wildcardType) {
        List j10;
        za.k.e(wildcardType, "reflectType");
        this.f19222b = wildcardType;
        j10 = kotlin.collections.r.j();
        this.f19223c = j10;
    }

    @Override // fc.c0
    public boolean P() {
        Object B;
        Type[] upperBounds = W().getUpperBounds();
        za.k.d(upperBounds, "reflectType.upperBounds");
        B = _Arrays.B(upperBounds);
        return !za.k.a(B, Object.class);
    }

    @Override // fc.c0
    /* renamed from: X, reason: merged with bridge method [inline-methods] */
    public ReflectJavaType J() {
        Object T;
        Object T2;
        Type[] upperBounds = W().getUpperBounds();
        Type[] lowerBounds = W().getLowerBounds();
        if (upperBounds.length <= 1 && lowerBounds.length <= 1) {
            if (lowerBounds.length == 1) {
                ReflectJavaType.a aVar = ReflectJavaType.f19262a;
                za.k.d(lowerBounds, "lowerBounds");
                T2 = _Arrays.T(lowerBounds);
                za.k.d(T2, "lowerBounds.single()");
                return aVar.a((Type) T2);
            }
            if (upperBounds.length != 1) {
                return null;
            }
            za.k.d(upperBounds, "upperBounds");
            T = _Arrays.T(upperBounds);
            Type type = (Type) T;
            if (za.k.a(type, Object.class)) {
                return null;
            }
            ReflectJavaType.a aVar2 = ReflectJavaType.f19262a;
            za.k.d(type, "ub");
            return aVar2.a(type);
        }
        throw new UnsupportedOperationException("Wildcard types with many bounds are not yet supported: " + W());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // vb.ReflectJavaType
    /* renamed from: Y, reason: merged with bridge method [inline-methods] */
    public WildcardType W() {
        return this.f19222b;
    }

    @Override // fc.d
    public Collection<fc.a> i() {
        return this.f19223c;
    }

    @Override // fc.d
    public boolean k() {
        return this.f19224d;
    }
}
