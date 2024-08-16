package androidx.savedstate;

import android.os.Bundle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.h;
import androidx.lifecycle.o;
import b0.SavedStateRegistry;
import b0.SavedStateRegistryOwner;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.Metadata;
import za.k;

/* compiled from: Recreator.kt */
@Metadata(bv = {}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0000\u0018\u0000 \u000f2\u00020\u0001:\u0002\n\u0005B\u000f\u0012\u0006\u0010\f\u001a\u00020\u000b¢\u0006\u0004\b\r\u0010\u000eJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0018\u0010\n\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\bH\u0016¨\u0006\u0010"}, d2 = {"Landroidx/savedstate/Recreator;", "Landroidx/lifecycle/l;", "", "className", "Lma/f0;", "b", "Landroidx/lifecycle/o;", "source", "Landroidx/lifecycle/h$b;", "event", "a", "Lb0/d;", "owner", "<init>", "(Lb0/d;)V", "f", "savedstate_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class Recreator implements LifecycleEventObserver {

    /* renamed from: e, reason: collision with root package name */
    private final SavedStateRegistryOwner f3948e;

    /* compiled from: Recreator.kt */
    @Metadata(bv = {}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\f\u001a\u00020\u000b¢\u0006\u0004\b\r\u0010\u000eJ\b\u0010\u0003\u001a\u00020\u0002H\u0016J\u000e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0004R\u001a\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\b8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0003\u0010\t¨\u0006\u000f"}, d2 = {"Landroidx/savedstate/Recreator$b;", "Lb0/b$c;", "Landroid/os/Bundle;", "a", "", "className", "Lma/f0;", "b", "", "Ljava/util/Set;", "classes", "Lb0/b;", "registry", "<init>", "(Lb0/b;)V", "savedstate_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class b implements SavedStateRegistry.c {

        /* renamed from: a, reason: collision with root package name and from kotlin metadata */
        private final Set<String> classes;

        public b(SavedStateRegistry savedStateRegistry) {
            k.e(savedStateRegistry, "registry");
            this.classes = new LinkedHashSet();
            savedStateRegistry.h("androidx.savedstate.Restarter", this);
        }

        @Override // b0.SavedStateRegistry.c
        public Bundle a() {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("classes_to_restore", new ArrayList<>(this.classes));
            return bundle;
        }

        public final void b(String str) {
            k.e(str, "className");
            this.classes.add(str);
        }
    }

    public Recreator(SavedStateRegistryOwner savedStateRegistryOwner) {
        k.e(savedStateRegistryOwner, "owner");
        this.f3948e = savedStateRegistryOwner;
    }

    private final void b(String str) {
        try {
            Class<? extends U> asSubclass = Class.forName(str, false, Recreator.class.getClassLoader()).asSubclass(SavedStateRegistry.a.class);
            k.d(asSubclass, "{\n                Class.…class.java)\n            }");
            try {
                Constructor declaredConstructor = asSubclass.getDeclaredConstructor(new Class[0]);
                declaredConstructor.setAccessible(true);
                try {
                    Object newInstance = declaredConstructor.newInstance(new Object[0]);
                    k.d(newInstance, "{\n                constr…wInstance()\n            }");
                    ((SavedStateRegistry.a) newInstance).a(this.f3948e);
                } catch (Exception e10) {
                    throw new RuntimeException("Failed to instantiate " + str, e10);
                }
            } catch (NoSuchMethodException e11) {
                throw new IllegalStateException("Class " + asSubclass.getSimpleName() + " must have default constructor in order to be automatically recreated", e11);
            }
        } catch (ClassNotFoundException e12) {
            throw new RuntimeException("Class " + str + " wasn't found", e12);
        }
    }

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void a(o oVar, h.b bVar) {
        k.e(oVar, "source");
        k.e(bVar, "event");
        if (bVar == h.b.ON_CREATE) {
            oVar.getLifecycle().c(this);
            Bundle b10 = this.f3948e.getSavedStateRegistry().b("androidx.savedstate.Restarter");
            if (b10 == null) {
                return;
            }
            ArrayList<String> stringArrayList = b10.getStringArrayList("classes_to_restore");
            if (stringArrayList != null) {
                Iterator<String> it = stringArrayList.iterator();
                while (it.hasNext()) {
                    b(it.next());
                }
                return;
            }
            throw new IllegalStateException("Bundle with restored state for the component \"androidx.savedstate.Restarter\" must contain list of strings by the key \"classes_to_restore\"");
        }
        throw new AssertionError("Next event must be ON_CREATE");
    }
}
