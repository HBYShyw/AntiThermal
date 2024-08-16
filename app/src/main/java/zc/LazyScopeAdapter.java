package zc;

import fd.LockBasedStorageManager;
import fd.StorageManager;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: LazyScopeAdapter.kt */
/* renamed from: zc.g, reason: use source file name */
/* loaded from: classes2.dex */
public final class LazyScopeAdapter extends AbstractScopeAdapter {

    /* renamed from: b, reason: collision with root package name */
    private final fd.i<h> f20459b;

    /* compiled from: LazyScopeAdapter.kt */
    /* renamed from: zc.g$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<h> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ya.a<h> f20460e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        a(ya.a<? extends h> aVar) {
            super(0);
            this.f20460e = aVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final h invoke() {
            h invoke = this.f20460e.invoke();
            return invoke instanceof AbstractScopeAdapter ? ((AbstractScopeAdapter) invoke).h() : invoke;
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ LazyScopeAdapter(StorageManager storageManager, ya.a aVar, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(storageManager, aVar);
        if ((i10 & 1) != 0) {
            storageManager = LockBasedStorageManager.f11424e;
            za.k.d(storageManager, "NO_LOCKS");
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public LazyScopeAdapter(ya.a<? extends h> aVar) {
        this(null, aVar, 1, 0 == true ? 1 : 0);
        za.k.e(aVar, "getScope");
    }

    @Override // zc.AbstractScopeAdapter
    protected h i() {
        return this.f20459b.invoke();
    }

    public LazyScopeAdapter(StorageManager storageManager, ya.a<? extends h> aVar) {
        za.k.e(storageManager, "storageManager");
        za.k.e(aVar, "getScope");
        this.f20459b = storageManager.g(new a(aVar));
    }
}
