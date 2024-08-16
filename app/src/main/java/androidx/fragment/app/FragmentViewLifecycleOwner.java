package androidx.fragment.app;

import android.app.Application;
import android.content.ContextWrapper;
import android.os.Bundle;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.e0;
import androidx.lifecycle.h;
import androidx.lifecycle.h0;
import b0.SavedStateRegistry;
import b0.SavedStateRegistryController;
import b0.SavedStateRegistryOwner;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FragmentViewLifecycleOwner.java */
/* renamed from: androidx.fragment.app.v, reason: use source file name */
/* loaded from: classes.dex */
public class FragmentViewLifecycleOwner implements HasDefaultViewModelProviderFactory, SavedStateRegistryOwner, ViewModelStoreOwner {

    /* renamed from: e, reason: collision with root package name */
    private final Fragment f3036e;

    /* renamed from: f, reason: collision with root package name */
    private final ViewModelStore f3037f;

    /* renamed from: g, reason: collision with root package name */
    private h0.b f3038g;

    /* renamed from: h, reason: collision with root package name */
    private LifecycleRegistry f3039h = null;

    /* renamed from: i, reason: collision with root package name */
    private SavedStateRegistryController f3040i = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentViewLifecycleOwner(Fragment fragment, ViewModelStore viewModelStore) {
        this.f3036e = fragment;
        this.f3037f = viewModelStore;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(h.b bVar) {
        this.f3039h.h(bVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        if (this.f3039h == null) {
            this.f3039h = new LifecycleRegistry(this);
            this.f3040i = SavedStateRegistryController.a(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean c() {
        return this.f3039h != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(Bundle bundle) {
        this.f3040i.d(bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(Bundle bundle) {
        this.f3040i.e(bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(h.c cVar) {
        this.f3039h.o(cVar);
    }

    @Override // androidx.lifecycle.HasDefaultViewModelProviderFactory
    public h0.b getDefaultViewModelProviderFactory() {
        h0.b defaultViewModelProviderFactory = this.f3036e.getDefaultViewModelProviderFactory();
        if (!defaultViewModelProviderFactory.equals(this.f3036e.mDefaultFactory)) {
            this.f3038g = defaultViewModelProviderFactory;
            return defaultViewModelProviderFactory;
        }
        if (this.f3038g == null) {
            Application application = null;
            Object applicationContext = this.f3036e.requireContext().getApplicationContext();
            while (true) {
                if (!(applicationContext instanceof ContextWrapper)) {
                    break;
                }
                if (applicationContext instanceof Application) {
                    application = (Application) applicationContext;
                    break;
                }
                applicationContext = ((ContextWrapper) applicationContext).getBaseContext();
            }
            this.f3038g = new e0(application, this, this.f3036e.getArguments());
        }
        return this.f3038g;
    }

    @Override // androidx.lifecycle.o
    public androidx.lifecycle.h getLifecycle() {
        b();
        return this.f3039h;
    }

    @Override // b0.SavedStateRegistryOwner
    public SavedStateRegistry getSavedStateRegistry() {
        b();
        return this.f3040i.getF4528b();
    }

    @Override // androidx.lifecycle.ViewModelStoreOwner
    public ViewModelStore getViewModelStore() {
        b();
        return this.f3037f;
    }
}
