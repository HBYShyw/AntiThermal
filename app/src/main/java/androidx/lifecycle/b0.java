package androidx.lifecycle;

import android.os.Bundle;
import androidx.lifecycle.h;
import androidx.lifecycle.h0;
import b0.SavedStateRegistry;
import b0.SavedStateRegistryOwner;
import kotlin.Metadata;
import w.a;
import za.Lambda;
import za.Reflection;

/* compiled from: SavedStateHandleSupport.kt */
@Metadata(bv = {}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a!\u0010\u0004\u001a\u00020\u0003\"\f\b\u0000\u0010\u0002*\u00020\u0000*\u00020\u0001*\u00028\u0000H\u0007¢\u0006\u0004\b\u0004\u0010\u0005\u001a*\u0010\r\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\b2\b\u0010\u000b\u001a\u0004\u0018\u00010\nH\u0002\u001a\f\u0010\u000f\u001a\u00020\f*\u00020\u000eH\u0007\"\u0018\u0010\u0013\u001a\u00020\u0010*\u00020\u00018@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012\"\u0018\u0010\u0017\u001a\u00020\u0014*\u00020\u00008@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016¨\u0006\u0018"}, d2 = {"Lb0/d;", "Landroidx/lifecycle/k0;", "T", "Lma/f0;", "c", "(Lb0/d;)V", "savedStateRegistryOwner", "viewModelStoreOwner", "", "key", "Landroid/os/Bundle;", "defaultArgs", "Landroidx/lifecycle/a0;", "a", "Lw/a;", "b", "Landroidx/lifecycle/d0;", "e", "(Landroidx/lifecycle/k0;)Landroidx/lifecycle/d0;", "savedStateHandlesVM", "Landroidx/lifecycle/c0;", "d", "(Lb0/d;)Landroidx/lifecycle/c0;", "savedStateHandlesProvider", "lifecycle-viewmodel-savedstate_release"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class b0 {

    /* renamed from: a, reason: collision with root package name */
    public static final a.b<SavedStateRegistryOwner> f3162a = new b();

    /* renamed from: b, reason: collision with root package name */
    public static final a.b<ViewModelStoreOwner> f3163b = new c();

    /* renamed from: c, reason: collision with root package name */
    public static final a.b<Bundle> f3164c = new a();

    /* compiled from: SavedStateHandleSupport.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001¨\u0006\u0003"}, d2 = {"androidx/lifecycle/b0$a", "Lw/a$b;", "Landroid/os/Bundle;", "lifecycle-viewmodel-savedstate_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class a implements a.b<Bundle> {
        a() {
        }
    }

    /* compiled from: SavedStateHandleSupport.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001¨\u0006\u0003"}, d2 = {"androidx/lifecycle/b0$b", "Lw/a$b;", "Lb0/d;", "lifecycle-viewmodel-savedstate_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class b implements a.b<SavedStateRegistryOwner> {
        b() {
        }
    }

    /* compiled from: SavedStateHandleSupport.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001¨\u0006\u0003"}, d2 = {"androidx/lifecycle/b0$c", "Lw/a$b;", "Landroidx/lifecycle/k0;", "lifecycle-viewmodel-savedstate_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class c implements a.b<ViewModelStoreOwner> {
        c() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SavedStateHandleSupport.kt */
    @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"Lw/a;", "Landroidx/lifecycle/d0;", "a", "(Lw/a;)Landroidx/lifecycle/d0;"}, k = 3, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class d extends Lambda implements ya.l<w.a, d0> {

        /* renamed from: e, reason: collision with root package name */
        public static final d f3165e = new d();

        d() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final d0 invoke(w.a aVar) {
            za.k.e(aVar, "$this$initializer");
            return new d0();
        }
    }

    private static final SavedStateHandle a(SavedStateRegistryOwner savedStateRegistryOwner, ViewModelStoreOwner viewModelStoreOwner, String str, Bundle bundle) {
        c0 d10 = d(savedStateRegistryOwner);
        d0 e10 = e(viewModelStoreOwner);
        SavedStateHandle savedStateHandle = e10.f().get(str);
        if (savedStateHandle != null) {
            return savedStateHandle;
        }
        SavedStateHandle a10 = SavedStateHandle.INSTANCE.a(d10.b(str), bundle);
        e10.f().put(str, a10);
        return a10;
    }

    public static final SavedStateHandle b(w.a aVar) {
        za.k.e(aVar, "<this>");
        SavedStateRegistryOwner savedStateRegistryOwner = (SavedStateRegistryOwner) aVar.a(f3162a);
        if (savedStateRegistryOwner != null) {
            ViewModelStoreOwner viewModelStoreOwner = (ViewModelStoreOwner) aVar.a(f3163b);
            if (viewModelStoreOwner != null) {
                Bundle bundle = (Bundle) aVar.a(f3164c);
                String str = (String) aVar.a(h0.c.f3203d);
                if (str != null) {
                    return a(savedStateRegistryOwner, viewModelStoreOwner, str, bundle);
                }
                throw new IllegalArgumentException("CreationExtras must have a value by `VIEW_MODEL_KEY`");
            }
            throw new IllegalArgumentException("CreationExtras must have a value by `VIEW_MODEL_STORE_OWNER_KEY`");
        }
        throw new IllegalArgumentException("CreationExtras must have a value by `SAVED_STATE_REGISTRY_OWNER_KEY`");
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T extends SavedStateRegistryOwner & ViewModelStoreOwner> void c(T t7) {
        za.k.e(t7, "<this>");
        h.c b10 = t7.getLifecycle().b();
        za.k.d(b10, "lifecycle.currentState");
        if (b10 == h.c.INITIALIZED || b10 == h.c.CREATED) {
            if (t7.getSavedStateRegistry().c("androidx.lifecycle.internal.SavedStateHandlesProvider") == null) {
                c0 c0Var = new c0(t7.getSavedStateRegistry(), t7);
                t7.getSavedStateRegistry().h("androidx.lifecycle.internal.SavedStateHandlesProvider", c0Var);
                t7.getLifecycle().a(new SavedStateHandleAttacher(c0Var));
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    public static final c0 d(SavedStateRegistryOwner savedStateRegistryOwner) {
        za.k.e(savedStateRegistryOwner, "<this>");
        SavedStateRegistry.c c10 = savedStateRegistryOwner.getSavedStateRegistry().c("androidx.lifecycle.internal.SavedStateHandlesProvider");
        c0 c0Var = c10 instanceof c0 ? (c0) c10 : null;
        if (c0Var != null) {
            return c0Var;
        }
        throw new IllegalStateException("enableSavedStateHandles() wasn't called prior to createSavedStateHandle() call");
    }

    public static final d0 e(ViewModelStoreOwner viewModelStoreOwner) {
        za.k.e(viewModelStoreOwner, "<this>");
        w.c cVar = new w.c();
        cVar.a(Reflection.b(d0.class), d.f3165e);
        return (d0) new h0(viewModelStoreOwner, cVar.b()).b("androidx.lifecycle.internal.SavedStateHandlesVM", d0.class);
    }
}
