package uc;

import gd.g0;
import pb.ModuleDescriptor;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public abstract class g<T> {

    /* renamed from: a, reason: collision with root package name */
    private final T f18990a;

    public g(T t7) {
        this.f18990a = t7;
    }

    public abstract g0 a(ModuleDescriptor moduleDescriptor);

    public T b() {
        return this.f18990a;
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            T b10 = b();
            g gVar = obj instanceof g ? (g) obj : null;
            if (!za.k.a(b10, gVar != null ? gVar.b() : null)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        T b10 = b();
        if (b10 != null) {
            return b10.hashCode();
        }
        return 0;
    }

    public String toString() {
        return String.valueOf(b());
    }
}
