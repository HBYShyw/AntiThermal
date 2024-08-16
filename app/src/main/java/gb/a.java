package gb;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: TypesJVM.kt */
/* loaded from: classes2.dex */
public final class a implements GenericArrayType, Type {

    /* renamed from: e, reason: collision with root package name */
    private final Type f11616e;

    public a(Type type) {
        za.k.e(type, "elementType");
        this.f11616e = type;
    }

    public boolean equals(Object obj) {
        return (obj instanceof GenericArrayType) && za.k.a(getGenericComponentType(), ((GenericArrayType) obj).getGenericComponentType());
    }

    @Override // java.lang.reflect.GenericArrayType
    public Type getGenericComponentType() {
        return this.f11616e;
    }

    @Override // java.lang.reflect.Type
    public String getTypeName() {
        String h10;
        StringBuilder sb2 = new StringBuilder();
        h10 = u.h(this.f11616e);
        sb2.append(h10);
        sb2.append("[]");
        return sb2.toString();
    }

    public int hashCode() {
        return getGenericComponentType().hashCode();
    }

    public String toString() {
        return getTypeName();
    }
}
