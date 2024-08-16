package yb;

import fd.LockBasedStorageManager;
import java.util.Map;
import oc.FqName;
import za.Lambda;

/* compiled from: JavaNullabilityAnnotationSettings.kt */
/* loaded from: classes2.dex */
public final class e0<T> implements d0<T> {

    /* renamed from: b, reason: collision with root package name */
    private final Map<FqName, T> f20068b;

    /* renamed from: c, reason: collision with root package name */
    private final LockBasedStorageManager f20069c;

    /* renamed from: d, reason: collision with root package name */
    private final fd.h<FqName, T> f20070d;

    /* compiled from: JavaNullabilityAnnotationSettings.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<FqName, T> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ e0<T> f20071e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(e0<T> e0Var) {
            super(1);
            this.f20071e = e0Var;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final T invoke(FqName fqName) {
            za.k.d(fqName, "it");
            return (T) oc.e.a(fqName, this.f20071e.b());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public e0(Map<FqName, ? extends T> map) {
        za.k.e(map, "states");
        this.f20068b = map;
        LockBasedStorageManager lockBasedStorageManager = new LockBasedStorageManager("Java nullability annotation states");
        this.f20069c = lockBasedStorageManager;
        fd.h<FqName, T> b10 = lockBasedStorageManager.b(new a(this));
        za.k.d(b10, "storageManager.createMem…cificFqname(states)\n    }");
        this.f20070d = b10;
    }

    @Override // yb.d0
    public T a(FqName fqName) {
        za.k.e(fqName, "fqName");
        return this.f20070d.invoke(fqName);
    }

    public final Map<FqName, T> b() {
        return this.f20068b;
    }
}
