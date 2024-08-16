package androidx.lifecycle;

import android.os.Bundle;
import androidx.lifecycle.h;
import b0.SavedStateRegistry;
import b0.SavedStateRegistryOwner;
import java.util.Iterator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class LegacySavedStateHandleController {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class a implements SavedStateRegistry.a {
        a() {
        }

        @Override // b0.SavedStateRegistry.a
        public void a(SavedStateRegistryOwner savedStateRegistryOwner) {
            if (savedStateRegistryOwner instanceof ViewModelStoreOwner) {
                ViewModelStore viewModelStore = ((ViewModelStoreOwner) savedStateRegistryOwner).getViewModelStore();
                SavedStateRegistry savedStateRegistry = savedStateRegistryOwner.getSavedStateRegistry();
                Iterator<String> it = viewModelStore.c().iterator();
                while (it.hasNext()) {
                    LegacySavedStateHandleController.a(viewModelStore.b(it.next()), savedStateRegistry, savedStateRegistryOwner.getLifecycle());
                }
                if (viewModelStore.c().isEmpty()) {
                    return;
                }
                savedStateRegistry.i(a.class);
                return;
            }
            throw new IllegalStateException("Internal error: OnRecreation should be registered only on components that implement ViewModelStoreOwner");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(ViewModel viewModel, SavedStateRegistry savedStateRegistry, h hVar) {
        SavedStateHandleController savedStateHandleController = (SavedStateHandleController) viewModel.c("androidx.lifecycle.savedstate.vm.tag");
        if (savedStateHandleController == null || savedStateHandleController.d()) {
            return;
        }
        savedStateHandleController.b(savedStateRegistry, hVar);
        c(savedStateRegistry, hVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SavedStateHandleController b(SavedStateRegistry savedStateRegistry, h hVar, String str, Bundle bundle) {
        SavedStateHandleController savedStateHandleController = new SavedStateHandleController(str, SavedStateHandle.c(savedStateRegistry.b(str), bundle));
        savedStateHandleController.b(savedStateRegistry, hVar);
        c(savedStateRegistry, hVar);
        return savedStateHandleController;
    }

    private static void c(final SavedStateRegistry savedStateRegistry, final h hVar) {
        h.c b10 = hVar.b();
        if (b10 != h.c.INITIALIZED && !b10.a(h.c.STARTED)) {
            hVar.a(new LifecycleEventObserver() { // from class: androidx.lifecycle.LegacySavedStateHandleController.1
                @Override // androidx.lifecycle.LifecycleEventObserver
                public void a(o oVar, h.b bVar) {
                    if (bVar == h.b.ON_START) {
                        h.this.c(this);
                        savedStateRegistry.i(a.class);
                    }
                }
            });
        } else {
            savedStateRegistry.i(a.class);
        }
    }
}
