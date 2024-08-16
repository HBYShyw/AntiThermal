package androidx.lifecycle;

import androidx.lifecycle.h;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class SingleGeneratedAdapterObserver implements LifecycleEventObserver {

    /* renamed from: e, reason: collision with root package name */
    private final GeneratedAdapter f3142e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SingleGeneratedAdapterObserver(GeneratedAdapter generatedAdapter) {
        this.f3142e = generatedAdapter;
    }

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void a(o oVar, h.b bVar) {
        this.f3142e.a(oVar, bVar, false, null);
        this.f3142e.a(oVar, bVar, true, null);
    }
}
