package gb;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ma.Standard;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: TypesJVM.kt */
/* loaded from: classes2.dex */
public final class t implements TypeVariable<GenericDeclaration>, Type {

    /* renamed from: e, reason: collision with root package name */
    private final KTypeParameter f11634e;

    public t(KTypeParameter kTypeParameter) {
        za.k.e(kTypeParameter, "typeParameter");
        this.f11634e = kTypeParameter;
    }

    public boolean equals(Object obj) {
        if (obj instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable) obj;
            if (za.k.a(getName(), typeVariable.getName()) && za.k.a(getGenericDeclaration(), typeVariable.getGenericDeclaration())) {
                return true;
            }
        }
        return false;
    }

    @Override // java.lang.reflect.TypeVariable
    public Type[] getBounds() {
        int u7;
        Type c10;
        List<KType> upperBounds = this.f11634e.getUpperBounds();
        u7 = kotlin.collections.s.u(upperBounds, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = upperBounds.iterator();
        while (it.hasNext()) {
            c10 = u.c((KType) it.next(), true);
            arrayList.add(c10);
        }
        return (Type[]) arrayList.toArray(new Type[0]);
    }

    @Override // java.lang.reflect.TypeVariable
    public GenericDeclaration getGenericDeclaration() {
        throw new Standard("An operation is not implemented: " + ("getGenericDeclaration() is not yet supported for type variables created from KType: " + this.f11634e));
    }

    @Override // java.lang.reflect.TypeVariable
    public String getName() {
        return this.f11634e.getName();
    }

    @Override // java.lang.reflect.Type
    public String getTypeName() {
        return getName();
    }

    public int hashCode() {
        return getGenericDeclaration().hashCode() ^ getName().hashCode();
    }

    public String toString() {
        return getTypeName();
    }
}
