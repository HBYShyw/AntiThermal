package vb;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import vb.ReflectJavaType;

/* compiled from: ReflectJavaArrayType.kt */
/* renamed from: vb.k, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReflectJavaArrayType extends ReflectJavaType implements fc.f {

    /* renamed from: b, reason: collision with root package name */
    private final Type f19236b;

    /* renamed from: c, reason: collision with root package name */
    private final ReflectJavaType f19237c;

    /* renamed from: d, reason: collision with root package name */
    private final Collection<fc.a> f19238d;

    /* renamed from: e, reason: collision with root package name */
    private final boolean f19239e;

    public ReflectJavaArrayType(Type type) {
        ReflectJavaType a10;
        List j10;
        za.k.e(type, "reflectType");
        this.f19236b = type;
        Type W = W();
        if (!(W instanceof GenericArrayType)) {
            if (W instanceof Class) {
                Class cls = (Class) W;
                if (cls.isArray()) {
                    ReflectJavaType.a aVar = ReflectJavaType.f19262a;
                    Class<?> componentType = cls.getComponentType();
                    za.k.d(componentType, "getComponentType()");
                    a10 = aVar.a(componentType);
                }
            }
            throw new IllegalArgumentException("Not an array type (" + W().getClass() + "): " + W());
        }
        ReflectJavaType.a aVar2 = ReflectJavaType.f19262a;
        Type genericComponentType = ((GenericArrayType) W).getGenericComponentType();
        za.k.d(genericComponentType, "genericComponentType");
        a10 = aVar2.a(genericComponentType);
        this.f19237c = a10;
        j10 = kotlin.collections.r.j();
        this.f19238d = j10;
    }

    @Override // vb.ReflectJavaType
    protected Type W() {
        return this.f19236b;
    }

    @Override // fc.f
    /* renamed from: X, reason: merged with bridge method [inline-methods] */
    public ReflectJavaType r() {
        return this.f19237c;
    }

    @Override // fc.d
    public Collection<fc.a> i() {
        return this.f19238d;
    }

    @Override // fc.d
    public boolean k() {
        return this.f19239e;
    }
}
