package androidx.lifecycle;

import androidx.lifecycle.h;
import b0.SavedStateRegistry;

/* loaded from: classes.dex */
final class SavedStateHandleController implements LifecycleEventObserver {

    /* renamed from: e, reason: collision with root package name */
    private final String f3139e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f3140f = false;

    /* renamed from: g, reason: collision with root package name */
    private final SavedStateHandle f3141g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SavedStateHandleController(String str, SavedStateHandle savedStateHandle) {
        this.f3139e = str;
        this.f3141g = savedStateHandle;
    }

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void a(o oVar, h.b bVar) {
        if (bVar == h.b.ON_DESTROY) {
            this.f3140f = false;
            oVar.getLifecycle().c(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(SavedStateRegistry savedStateRegistry, h hVar) {
        if (!this.f3140f) {
            this.f3140f = true;
            hVar.a(this);
            savedStateRegistry.h(this.f3139e, this.f3141g.getF3154e());
            return;
        }
        throw new IllegalStateException("Already attached to lifecycleOwner");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SavedStateHandle c() {
        return this.f3141g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d() {
        return this.f3140f;
    }
}
