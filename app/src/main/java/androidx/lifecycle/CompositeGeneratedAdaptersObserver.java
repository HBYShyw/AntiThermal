package androidx.lifecycle;

import androidx.lifecycle.h;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class CompositeGeneratedAdaptersObserver implements LifecycleEventObserver {

    /* renamed from: e, reason: collision with root package name */
    private final GeneratedAdapter[] f3074e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompositeGeneratedAdaptersObserver(GeneratedAdapter[] generatedAdapterArr) {
        this.f3074e = generatedAdapterArr;
    }

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void a(o oVar, h.b bVar) {
        MethodCallsLogger methodCallsLogger = new MethodCallsLogger();
        for (GeneratedAdapter generatedAdapter : this.f3074e) {
            generatedAdapter.a(oVar, bVar, false, methodCallsLogger);
        }
        for (GeneratedAdapter generatedAdapter2 : this.f3074e) {
            generatedAdapter2.a(oVar, bVar, true, methodCallsLogger);
        }
    }
}
