package vb;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import oc.FqName;
import vb.ReflectJavaType;

/* compiled from: ReflectJavaClassifierType.kt */
/* renamed from: vb.n, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReflectJavaClassifierType extends ReflectJavaType implements fc.j {

    /* renamed from: b, reason: collision with root package name */
    private final Type f19250b;

    /* renamed from: c, reason: collision with root package name */
    private final fc.i f19251c;

    public ReflectJavaClassifierType(Type type) {
        fc.i lVar;
        za.k.e(type, "reflectType");
        this.f19250b = type;
        Type W = W();
        if (W instanceof Class) {
            lVar = new l((Class) W);
        } else if (W instanceof TypeVariable) {
            lVar = new ReflectJavaTypeParameter((TypeVariable) W);
        } else {
            if (!(W instanceof ParameterizedType)) {
                throw new IllegalStateException("Not a classifier type (" + W.getClass() + "): " + W);
            }
            Type rawType = ((ParameterizedType) W).getRawType();
            za.k.c(rawType, "null cannot be cast to non-null type java.lang.Class<*>");
            lVar = new l((Class) rawType);
        }
        this.f19251c = lVar;
    }

    @Override // fc.j
    public boolean E() {
        Type W = W();
        if (!(W instanceof Class)) {
            return false;
        }
        TypeVariable[] typeParameters = ((Class) W).getTypeParameters();
        za.k.d(typeParameters, "getTypeParameters()");
        return (typeParameters.length == 0) ^ true;
    }

    @Override // fc.j
    public String F() {
        throw new UnsupportedOperationException("Type not found: " + W());
    }

    @Override // fc.j
    public List<fc.x> L() {
        int u7;
        List<Type> d10 = reflectClassUtil.d(W());
        ReflectJavaType.a aVar = ReflectJavaType.f19262a;
        u7 = kotlin.collections.s.u(d10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = d10.iterator();
        while (it.hasNext()) {
            arrayList.add(aVar.a((Type) it.next()));
        }
        return arrayList;
    }

    @Override // vb.ReflectJavaType
    public Type W() {
        return this.f19250b;
    }

    @Override // fc.j
    public fc.i c() {
        return this.f19251c;
    }

    @Override // fc.d
    public Collection<fc.a> i() {
        List j10;
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // vb.ReflectJavaType, fc.d
    public fc.a j(FqName fqName) {
        za.k.e(fqName, "fqName");
        return null;
    }

    @Override // fc.d
    public boolean k() {
        return false;
    }

    @Override // fc.j
    public String x() {
        return W().toString();
    }
}
