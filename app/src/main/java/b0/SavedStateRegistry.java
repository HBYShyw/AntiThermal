package b0;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.h;
import androidx.lifecycle.o;
import androidx.savedstate.Recreator;
import h.SafeIterableMap;
import java.util.Iterator;
import java.util.Map;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: SavedStateRegistry.kt */
@Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\b\u0007\u0018\u00002\u00020\u0001:\u0003\u001a\u0005\nB\t\b\u0000¢\u0006\u0004\b\u0018\u0010\u0019J\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0007J\u0018\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0006H\u0007J\u0010\u0010\n\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0003\u001a\u00020\u0002J\u0018\u0010\u000e\u001a\u00020\b2\u000e\u0010\r\u001a\n\u0012\u0006\b\u0001\u0012\u00020\f0\u000bH\u0007J\u0017\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u000fH\u0001¢\u0006\u0004\b\u0011\u0010\u0012J\u0019\u0010\u0014\u001a\u00020\b2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0004H\u0001¢\u0006\u0004\b\u0014\u0010\u0015J\u0010\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u0004H\u0007¨\u0006\u001b"}, d2 = {"Lb0/b;", "", "", "key", "Landroid/os/Bundle;", "b", "Lb0/b$c;", "provider", "Lma/f0;", "h", "c", "Ljava/lang/Class;", "Lb0/b$a;", "clazz", "i", "Landroidx/lifecycle/h;", "lifecycle", "e", "(Landroidx/lifecycle/h;)V", "savedState", "f", "(Landroid/os/Bundle;)V", "outBundle", "g", "<init>", "()V", "a", "savedstate_release"}, k = 1, mv = {1, 6, 0})
@SuppressLint({"RestrictedApi"})
/* renamed from: b0.b, reason: use source file name */
/* loaded from: classes.dex */
public final class SavedStateRegistry {

    /* renamed from: g, reason: collision with root package name */
    private static final b f4519g = new b(null);

    /* renamed from: b, reason: collision with root package name */
    private boolean f4521b;

    /* renamed from: c, reason: collision with root package name */
    private Bundle f4522c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f4523d;

    /* renamed from: e, reason: collision with root package name */
    private Recreator.b f4524e;

    /* renamed from: a, reason: collision with root package name */
    private final SafeIterableMap<String, c> f4520a = new SafeIterableMap<>();

    /* renamed from: f, reason: collision with root package name */
    private boolean f4525f = true;

    /* compiled from: SavedStateRegistry.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H&¨\u0006\u0006"}, d2 = {"Lb0/b$a;", "", "Lb0/d;", "owner", "Lma/f0;", "a", "savedstate_release"}, k = 1, mv = {1, 6, 0})
    /* renamed from: b0.b$a */
    /* loaded from: classes.dex */
    public interface a {
        void a(SavedStateRegistryOwner savedStateRegistryOwner);
    }

    /* compiled from: SavedStateRegistry.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0003\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0007"}, d2 = {"Lb0/b$b;", "", "", "SAVED_COMPONENTS_KEY", "Ljava/lang/String;", "<init>", "()V", "savedstate_release"}, k = 1, mv = {1, 6, 0})
    /* renamed from: b0.b$b */
    /* loaded from: classes.dex */
    private static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: SavedStateRegistry.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bæ\u0080\u0001\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H&¨\u0006\u0004"}, d2 = {"Lb0/b$c;", "", "Landroid/os/Bundle;", "a", "savedstate_release"}, k = 1, mv = {1, 6, 0})
    /* renamed from: b0.b$c */
    /* loaded from: classes.dex */
    public interface c {
        Bundle a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void d(SavedStateRegistry savedStateRegistry, o oVar, h.b bVar) {
        k.e(savedStateRegistry, "this$0");
        k.e(oVar, "<anonymous parameter 0>");
        k.e(bVar, "event");
        if (bVar == h.b.ON_START) {
            savedStateRegistry.f4525f = true;
        } else if (bVar == h.b.ON_STOP) {
            savedStateRegistry.f4525f = false;
        }
    }

    public final Bundle b(String key) {
        k.e(key, "key");
        if (this.f4523d) {
            Bundle bundle = this.f4522c;
            if (bundle == null) {
                return null;
            }
            Bundle bundle2 = bundle != null ? bundle.getBundle(key) : null;
            Bundle bundle3 = this.f4522c;
            if (bundle3 != null) {
                bundle3.remove(key);
            }
            Bundle bundle4 = this.f4522c;
            boolean z10 = false;
            if (bundle4 != null && !bundle4.isEmpty()) {
                z10 = true;
            }
            if (!z10) {
                this.f4522c = null;
            }
            return bundle2;
        }
        throw new IllegalStateException("You can consumeRestoredStateForKey only after super.onCreate of corresponding component".toString());
    }

    public final c c(String key) {
        k.e(key, "key");
        Iterator<Map.Entry<String, c>> it = this.f4520a.iterator();
        while (it.hasNext()) {
            Map.Entry<String, c> next = it.next();
            k.d(next, "components");
            String key2 = next.getKey();
            c value = next.getValue();
            if (k.a(key2, key)) {
                return value;
            }
        }
        return null;
    }

    public final void e(h lifecycle) {
        k.e(lifecycle, "lifecycle");
        if (!this.f4521b) {
            lifecycle.a(new LifecycleEventObserver() { // from class: b0.a
                @Override // androidx.lifecycle.LifecycleEventObserver
                public final void a(o oVar, h.b bVar) {
                    SavedStateRegistry.d(SavedStateRegistry.this, oVar, bVar);
                }
            });
            this.f4521b = true;
            return;
        }
        throw new IllegalStateException("SavedStateRegistry was already attached.".toString());
    }

    public final void f(Bundle savedState) {
        if (this.f4521b) {
            if (!this.f4523d) {
                this.f4522c = savedState != null ? savedState.getBundle("androidx.lifecycle.BundlableSavedStateRegistry.key") : null;
                this.f4523d = true;
                return;
            }
            throw new IllegalStateException("SavedStateRegistry was already restored.".toString());
        }
        throw new IllegalStateException("You must call performAttach() before calling performRestore(Bundle).".toString());
    }

    public final void g(Bundle bundle) {
        k.e(bundle, "outBundle");
        Bundle bundle2 = new Bundle();
        Bundle bundle3 = this.f4522c;
        if (bundle3 != null) {
            bundle2.putAll(bundle3);
        }
        SafeIterableMap<String, c>.d f10 = this.f4520a.f();
        k.d(f10, "this.components.iteratorWithAdditions()");
        while (f10.hasNext()) {
            Map.Entry next = f10.next();
            bundle2.putBundle((String) next.getKey(), ((c) next.getValue()).a());
        }
        if (bundle2.isEmpty()) {
            return;
        }
        bundle.putBundle("androidx.lifecycle.BundlableSavedStateRegistry.key", bundle2);
    }

    public final void h(String str, c cVar) {
        k.e(str, "key");
        k.e(cVar, "provider");
        if (!(this.f4520a.i(str, cVar) == null)) {
            throw new IllegalArgumentException("SavedStateProvider with the given key is already registered".toString());
        }
    }

    public final void i(Class<? extends a> cls) {
        k.e(cls, "clazz");
        if (this.f4525f) {
            Recreator.b bVar = this.f4524e;
            if (bVar == null) {
                bVar = new Recreator.b(this);
            }
            this.f4524e = bVar;
            try {
                cls.getDeclaredConstructor(new Class[0]);
                Recreator.b bVar2 = this.f4524e;
                if (bVar2 != null) {
                    String name = cls.getName();
                    k.d(name, "clazz.name");
                    bVar2.b(name);
                    return;
                }
                return;
            } catch (NoSuchMethodException e10) {
                throw new IllegalArgumentException("Class " + cls.getSimpleName() + " must have default constructor in order to be automatically recreated", e10);
            }
        }
        throw new IllegalStateException("Can not perform this action after onSaveInstanceState".toString());
    }
}
