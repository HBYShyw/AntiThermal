package ad;

import gd.o0;
import pb.ClassDescriptor;
import za.k;

/* compiled from: ImplicitClassReceiver.kt */
/* loaded from: classes2.dex */
public class e implements ReceiverValue, i {

    /* renamed from: a, reason: collision with root package name */
    private final ClassDescriptor f231a;

    /* renamed from: b, reason: collision with root package name */
    private final e f232b;

    /* renamed from: c, reason: collision with root package name */
    private final ClassDescriptor f233c;

    public e(ClassDescriptor classDescriptor, e eVar) {
        k.e(classDescriptor, "classDescriptor");
        this.f231a = classDescriptor;
        this.f232b = eVar == null ? this : eVar;
        this.f233c = classDescriptor;
    }

    @Override // ad.ReceiverValue
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public o0 getType() {
        o0 x10 = this.f231a.x();
        k.d(x10, "classDescriptor.defaultType");
        return x10;
    }

    public boolean equals(Object obj) {
        ClassDescriptor classDescriptor = this.f231a;
        e eVar = obj instanceof e ? (e) obj : null;
        return k.a(classDescriptor, eVar != null ? eVar.f231a : null);
    }

    public int hashCode() {
        return this.f231a.hashCode();
    }

    public String toString() {
        return "Class{" + getType() + '}';
    }

    @Override // ad.i
    public final ClassDescriptor w() {
        return this.f231a;
    }
}
