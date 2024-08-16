package gd;

/* compiled from: TypeProjectionBase.java */
/* renamed from: gd.l1, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class TypeProjectionBase implements TypeProjection {
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TypeProjection)) {
            return false;
        }
        TypeProjection typeProjection = (TypeProjection) obj;
        return b() == typeProjection.b() && a() == typeProjection.a() && getType().equals(typeProjection.getType());
    }

    public int hashCode() {
        int hashCode = a().hashCode();
        if (s1.w(getType())) {
            return (hashCode * 31) + 19;
        }
        return (hashCode * 31) + (b() ? 17 : getType().hashCode());
    }

    public String toString() {
        if (b()) {
            return "*";
        }
        if (a() == Variance.INVARIANT) {
            return getType().toString();
        }
        return a() + " " + getType();
    }
}
