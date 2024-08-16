package za;

import gb.KCallable;

/* compiled from: PropertyReference.java */
/* renamed from: za.v, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class PropertyReference extends CallableReference implements gb.l {

    /* renamed from: l, reason: collision with root package name */
    private final boolean f20373l;

    public PropertyReference() {
        this.f20373l = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // za.CallableReference
    /* renamed from: F, reason: merged with bridge method [inline-methods] */
    public gb.l D() {
        if (!this.f20373l) {
            return (gb.l) super.D();
        }
        throw new UnsupportedOperationException("Kotlin reflection is not yet supported for synthetic Java properties");
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof PropertyReference) {
            PropertyReference propertyReference = (PropertyReference) obj;
            return C().equals(propertyReference.C()) && getName().equals(propertyReference.getName()) && E().equals(propertyReference.E()) && k.a(B(), propertyReference.B());
        }
        if (obj instanceof gb.l) {
            return obj.equals(s());
        }
        return false;
    }

    public int hashCode() {
        return (((C().hashCode() * 31) + getName().hashCode()) * 31) + E().hashCode();
    }

    @Override // za.CallableReference
    public KCallable s() {
        return this.f20373l ? this : super.s();
    }

    public String toString() {
        KCallable s7 = s();
        if (s7 != this) {
            return s7.toString();
        }
        return "property " + getName() + " (Kotlin reflection is not available)";
    }

    public PropertyReference(Object obj, Class cls, String str, String str2, int i10) {
        super(obj, cls, str, str2, (i10 & 1) == 1);
        this.f20373l = (i10 & 2) == 2;
    }
}
