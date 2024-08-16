package b0;

import android.os.Bundle;
import androidx.lifecycle.h;
import androidx.savedstate.Recreator;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: SavedStateRegistryController.kt */
@Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0001\u0012B\u0011\b\u0002\u0012\u0006\u0010\u000f\u001a\u00020\u000e¢\u0006\u0004\b\u0010\u0010\u0011J\b\u0010\u0003\u001a\u00020\u0002H\u0007J\u0012\u0010\u0006\u001a\u00020\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H\u0007J\u0010\u0010\b\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0004H\u0007R\u0017\u0010\n\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\r¨\u0006\u0013"}, d2 = {"Lb0/c;", "", "Lma/f0;", "c", "Landroid/os/Bundle;", "savedState", "d", "outBundle", "e", "Lb0/b;", "savedStateRegistry", "Lb0/b;", "b", "()Lb0/b;", "Lb0/d;", "owner", "<init>", "(Lb0/d;)V", "a", "savedstate_release"}, k = 1, mv = {1, 6, 0})
/* renamed from: b0.c, reason: use source file name */
/* loaded from: classes.dex */
public final class SavedStateRegistryController {

    /* renamed from: d, reason: collision with root package name */
    public static final a f4526d = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final SavedStateRegistryOwner f4527a;

    /* renamed from: b, reason: collision with root package name */
    private final SavedStateRegistry f4528b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f4529c;

    /* compiled from: SavedStateRegistryController.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0007¨\u0006\b"}, d2 = {"Lb0/c$a;", "", "Lb0/d;", "owner", "Lb0/c;", "a", "<init>", "()V", "savedstate_release"}, k = 1, mv = {1, 6, 0})
    /* renamed from: b0.c$a */
    /* loaded from: classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final SavedStateRegistryController a(SavedStateRegistryOwner owner) {
            k.e(owner, "owner");
            return new SavedStateRegistryController(owner, null);
        }
    }

    private SavedStateRegistryController(SavedStateRegistryOwner savedStateRegistryOwner) {
        this.f4527a = savedStateRegistryOwner;
        this.f4528b = new SavedStateRegistry();
    }

    public /* synthetic */ SavedStateRegistryController(SavedStateRegistryOwner savedStateRegistryOwner, DefaultConstructorMarker defaultConstructorMarker) {
        this(savedStateRegistryOwner);
    }

    public static final SavedStateRegistryController a(SavedStateRegistryOwner savedStateRegistryOwner) {
        return f4526d.a(savedStateRegistryOwner);
    }

    /* renamed from: b, reason: from getter */
    public final SavedStateRegistry getF4528b() {
        return this.f4528b;
    }

    public final void c() {
        h lifecycle = this.f4527a.getLifecycle();
        k.d(lifecycle, "owner.lifecycle");
        if (lifecycle.b() == h.c.INITIALIZED) {
            lifecycle.a(new Recreator(this.f4527a));
            this.f4528b.e(lifecycle);
            this.f4529c = true;
            return;
        }
        throw new IllegalStateException("Restarter must be created only during owner's initialization stage".toString());
    }

    public final void d(Bundle bundle) {
        if (!this.f4529c) {
            c();
        }
        h lifecycle = this.f4527a.getLifecycle();
        k.d(lifecycle, "owner.lifecycle");
        if (!lifecycle.b().a(h.c.STARTED)) {
            this.f4528b.f(bundle);
            return;
        }
        throw new IllegalStateException(("performRestore cannot be called when owner is " + lifecycle.b()).toString());
    }

    public final void e(Bundle bundle) {
        k.e(bundle, "outBundle");
        this.f4528b.g(bundle);
    }
}
