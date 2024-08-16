package androidx.lifecycle;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;
import androidx.lifecycle.h0;
import b0.SavedStateRegistry;
import b0.SavedStateRegistryOwner;
import java.lang.reflect.Constructor;
import java.util.List;
import kotlin.Metadata;

/* compiled from: SavedStateViewModelFactory.kt */
@Metadata(bv = {}, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u00012\u00020\u0002B%\b\u0017\u0012\b\u0010\u0016\u001a\u0004\u0018\u00010\u0014\u0012\u0006\u0010!\u001a\u00020 \u0012\b\u0010\u001b\u001a\u0004\u0018\u00010\u0019¢\u0006\u0004\b\"\u0010#J/\u0010\t\u001a\u00028\u0000\"\b\b\u0000\u0010\u0004*\u00020\u00032\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u00052\u0006\u0010\b\u001a\u00020\u0007H\u0016¢\u0006\u0004\b\t\u0010\nJ-\u0010\r\u001a\u00028\u0000\"\b\b\u0000\u0010\u0004*\u00020\u00032\u0006\u0010\f\u001a\u00020\u000b2\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005¢\u0006\u0004\b\r\u0010\u000eJ'\u0010\u000f\u001a\u00028\u0000\"\b\b\u0000\u0010\u0004*\u00020\u00032\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H\u0016¢\u0006\u0004\b\u000f\u0010\u0010J\u0010\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u0003H\u0017R\u0018\u0010\u0016\u001a\u0004\u0018\u00010\u00148\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\t\u0010\u0015R\u0014\u0010\u0018\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0013\u0010\u0017R\u0018\u0010\u001b\u001a\u0004\u0018\u00010\u00198\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\r\u0010\u001aR\u0018\u0010\u001f\u001a\u0004\u0018\u00010\u001c8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001d\u0010\u001e¨\u0006$"}, d2 = {"Landroidx/lifecycle/e0;", "Landroidx/lifecycle/h0$d;", "Landroidx/lifecycle/h0$b;", "Landroidx/lifecycle/g0;", "T", "Ljava/lang/Class;", "modelClass", "Lw/a;", "extras", "b", "(Ljava/lang/Class;Lw/a;)Landroidx/lifecycle/g0;", "", "key", "d", "(Ljava/lang/String;Ljava/lang/Class;)Landroidx/lifecycle/g0;", "a", "(Ljava/lang/Class;)Landroidx/lifecycle/g0;", "viewModel", "Lma/f0;", "c", "Landroid/app/Application;", "Landroid/app/Application;", "application", "Landroidx/lifecycle/h0$b;", "factory", "Landroid/os/Bundle;", "Landroid/os/Bundle;", "defaultArgs", "Landroidx/lifecycle/h;", "e", "Landroidx/lifecycle/h;", "lifecycle", "Lb0/d;", "owner", "<init>", "(Landroid/app/Application;Lb0/d;Landroid/os/Bundle;)V", "lifecycle-viewmodel-savedstate_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class e0 extends h0.d implements h0.b {

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private Application application;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private final h0.b factory;

    /* renamed from: d, reason: collision with root package name and from kotlin metadata */
    private Bundle defaultArgs;

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private h lifecycle;

    /* renamed from: f, reason: collision with root package name */
    private SavedStateRegistry f3176f;

    @SuppressLint({"LambdaLast"})
    public e0(Application application, SavedStateRegistryOwner savedStateRegistryOwner, Bundle bundle) {
        h0.a aVar;
        za.k.e(savedStateRegistryOwner, "owner");
        this.f3176f = savedStateRegistryOwner.getSavedStateRegistry();
        this.lifecycle = savedStateRegistryOwner.getLifecycle();
        this.defaultArgs = bundle;
        this.application = application;
        if (application != null) {
            aVar = h0.a.INSTANCE.b(application);
        } else {
            aVar = new h0.a();
        }
        this.factory = aVar;
    }

    @Override // androidx.lifecycle.h0.b
    public <T extends ViewModel> T a(Class<T> modelClass) {
        za.k.e(modelClass, "modelClass");
        String canonicalName = modelClass.getCanonicalName();
        if (canonicalName != null) {
            return (T) d(canonicalName, modelClass);
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }

    @Override // androidx.lifecycle.h0.b
    public <T extends ViewModel> T b(Class<T> modelClass, w.a extras) {
        List list;
        Constructor c10;
        List list2;
        za.k.e(modelClass, "modelClass");
        za.k.e(extras, "extras");
        String str = (String) extras.a(h0.c.f3203d);
        if (str != null) {
            if (extras.a(b0.f3162a) != null && extras.a(b0.f3163b) != null) {
                Application application = (Application) extras.a(h0.a.f3196h);
                boolean isAssignableFrom = AndroidViewModel.class.isAssignableFrom(modelClass);
                if (isAssignableFrom && application != null) {
                    list2 = f0.f3177a;
                    c10 = f0.c(modelClass, list2);
                } else {
                    list = f0.f3178b;
                    c10 = f0.c(modelClass, list);
                }
                if (c10 == null) {
                    return (T) this.factory.b(modelClass, extras);
                }
                return (!isAssignableFrom || application == null) ? (T) f0.d(modelClass, c10, b0.b(extras)) : (T) f0.d(modelClass, c10, application, b0.b(extras));
            }
            if (this.lifecycle != null) {
                return (T) d(str, modelClass);
            }
            throw new IllegalStateException("SAVED_STATE_REGISTRY_OWNER_KEY andVIEW_MODEL_STORE_OWNER_KEY must be provided in the creation extras tosuccessfully create a ViewModel.");
        }
        throw new IllegalStateException("VIEW_MODEL_KEY must always be provided by ViewModelProvider");
    }

    @Override // androidx.lifecycle.h0.d
    public void c(ViewModel viewModel) {
        za.k.e(viewModel, "viewModel");
        h hVar = this.lifecycle;
        if (hVar != null) {
            LegacySavedStateHandleController.a(viewModel, this.f3176f, hVar);
        }
    }

    public final <T extends ViewModel> T d(String key, Class<T> modelClass) {
        List list;
        Constructor c10;
        T t7;
        Application application;
        List list2;
        za.k.e(key, "key");
        za.k.e(modelClass, "modelClass");
        if (this.lifecycle != null) {
            boolean isAssignableFrom = AndroidViewModel.class.isAssignableFrom(modelClass);
            if (isAssignableFrom && this.application != null) {
                list2 = f0.f3177a;
                c10 = f0.c(modelClass, list2);
            } else {
                list = f0.f3178b;
                c10 = f0.c(modelClass, list);
            }
            if (c10 == null) {
                return this.application != null ? (T) this.factory.a(modelClass) : (T) h0.c.INSTANCE.a().a(modelClass);
            }
            SavedStateHandleController b10 = LegacySavedStateHandleController.b(this.f3176f, this.lifecycle, key, this.defaultArgs);
            if (isAssignableFrom && (application = this.application) != null) {
                za.k.b(application);
                SavedStateHandle c11 = b10.c();
                za.k.d(c11, "controller.handle");
                t7 = (T) f0.d(modelClass, c10, application, c11);
            } else {
                SavedStateHandle c12 = b10.c();
                za.k.d(c12, "controller.handle");
                t7 = (T) f0.d(modelClass, c10, c12);
            }
            t7.e("androidx.lifecycle.savedstate.vm.tag", b10);
            return t7;
        }
        throw new UnsupportedOperationException("SavedStateViewModelFactory constructed with empty constructor supports only calls to create(modelClass: Class<T>, extras: CreationExtras).");
    }
}
