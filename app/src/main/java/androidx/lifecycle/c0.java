package androidx.lifecycle;

import android.os.Bundle;
import b0.SavedStateRegistry;
import java.util.Map;
import kotlin.Metadata;
import za.Lambda;

/* compiled from: SavedStateHandleSupport.kt */
@Metadata(bv = {}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0015\u001a\u00020\u0014\u0012\u0006\u0010\u0017\u001a\u00020\u0016¢\u0006\u0004\b\u0018\u0010\u0019J\b\u0010\u0003\u001a\u00020\u0002H\u0016J\u0006\u0010\u0005\u001a\u00020\u0004J\u0010\u0010\b\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0007\u001a\u00020\u0006R\u0016\u0010\u000b\u001a\u00020\t8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\b\u0010\nR\u0018\u0010\u000e\u001a\u0004\u0018\u00010\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\f\u0010\rR\u001b\u0010\u0013\u001a\u00020\u000f8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\f\u0010\u0012¨\u0006\u001a"}, d2 = {"Landroidx/lifecycle/c0;", "Lb0/b$c;", "Landroid/os/Bundle;", "a", "Lma/f0;", "d", "", "key", "b", "", "Z", "restored", "c", "Landroid/os/Bundle;", "restoredState", "Landroidx/lifecycle/d0;", "viewModel$delegate", "Lma/h;", "()Landroidx/lifecycle/d0;", "viewModel", "Lb0/b;", "savedStateRegistry", "Landroidx/lifecycle/k0;", "viewModelStoreOwner", "<init>", "(Lb0/b;Landroidx/lifecycle/k0;)V", "lifecycle-viewmodel-savedstate_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class c0 implements SavedStateRegistry.c {

    /* renamed from: a, reason: collision with root package name */
    private final SavedStateRegistry f3166a;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private boolean restored;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private Bundle restoredState;

    /* renamed from: d, reason: collision with root package name */
    private final ma.h f3169d;

    /* compiled from: SavedStateHandleSupport.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Landroidx/lifecycle/d0;", "a", "()Landroidx/lifecycle/d0;"}, k = 3, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    static final class a extends Lambda implements ya.a<d0> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ViewModelStoreOwner f3170e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(ViewModelStoreOwner viewModelStoreOwner) {
            super(0);
            this.f3170e = viewModelStoreOwner;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final d0 invoke() {
            return b0.e(this.f3170e);
        }
    }

    public c0(SavedStateRegistry savedStateRegistry, ViewModelStoreOwner viewModelStoreOwner) {
        ma.h b10;
        za.k.e(savedStateRegistry, "savedStateRegistry");
        za.k.e(viewModelStoreOwner, "viewModelStoreOwner");
        this.f3166a = savedStateRegistry;
        b10 = ma.j.b(new a(viewModelStoreOwner));
        this.f3169d = b10;
    }

    private final d0 c() {
        return (d0) this.f3169d.getValue();
    }

    @Override // b0.SavedStateRegistry.c
    public Bundle a() {
        Bundle bundle = new Bundle();
        Bundle bundle2 = this.restoredState;
        if (bundle2 != null) {
            bundle.putAll(bundle2);
        }
        for (Map.Entry<String, SavedStateHandle> entry : c().f().entrySet()) {
            String key = entry.getKey();
            Bundle a10 = entry.getValue().getF3154e().a();
            if (!za.k.a(a10, Bundle.EMPTY)) {
                bundle.putBundle(key, a10);
            }
        }
        this.restored = false;
        return bundle;
    }

    public final Bundle b(String key) {
        za.k.e(key, "key");
        d();
        Bundle bundle = this.restoredState;
        Bundle bundle2 = bundle != null ? bundle.getBundle(key) : null;
        Bundle bundle3 = this.restoredState;
        if (bundle3 != null) {
            bundle3.remove(key);
        }
        Bundle bundle4 = this.restoredState;
        if (bundle4 != null && bundle4.isEmpty()) {
            this.restoredState = null;
        }
        return bundle2;
    }

    public final void d() {
        if (this.restored) {
            return;
        }
        this.restoredState = this.f3166a.b("androidx.lifecycle.internal.SavedStateHandlesProvider");
        this.restored = true;
        c();
    }
}
