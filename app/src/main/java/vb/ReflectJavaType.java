package vb;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Iterator;
import oc.ClassId;
import oc.FqName;
import za.DefaultConstructorMarker;

/* compiled from: ReflectJavaType.kt */
/* renamed from: vb.z, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class ReflectJavaType implements fc.x {

    /* renamed from: a, reason: collision with root package name */
    public static final a f19262a = new a(null);

    /* compiled from: ReflectJavaType.kt */
    /* renamed from: vb.z$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final ReflectJavaType a(Type type) {
            za.k.e(type, "type");
            boolean z10 = type instanceof Class;
            if (z10) {
                Class cls = (Class) type;
                if (cls.isPrimitive()) {
                    return new ReflectJavaPrimitiveType(cls);
                }
            }
            if ((type instanceof GenericArrayType) || (z10 && ((Class) type).isArray())) {
                return new ReflectJavaArrayType(type);
            }
            return type instanceof WildcardType ? new ReflectJavaWildcardType((WildcardType) type) : new ReflectJavaClassifierType(type);
        }
    }

    protected abstract Type W();

    public boolean equals(Object obj) {
        return (obj instanceof ReflectJavaType) && za.k.a(W(), ((ReflectJavaType) obj).W());
    }

    public int hashCode() {
        return W().hashCode();
    }

    @Override // fc.d
    public fc.a j(FqName fqName) {
        Object obj;
        za.k.e(fqName, "fqName");
        Iterator<T> it = i().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            ClassId e10 = ((fc.a) next).e();
            if (za.k.a(e10 != null ? e10.b() : null, fqName)) {
                obj = next;
                break;
            }
        }
        return (fc.a) obj;
    }

    public String toString() {
        return getClass().getName() + ": " + W();
    }
}
