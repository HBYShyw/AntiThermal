package android.view.result;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.h;
import androidx.lifecycle.o;
import b.ActivityResultContract;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/* loaded from: classes.dex */
public abstract class ActivityResultRegistry {

    /* renamed from: a, reason: collision with root package name */
    private Random f290a = new Random();

    /* renamed from: b, reason: collision with root package name */
    private final Map<Integer, String> f291b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    final Map<String, Integer> f292c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    private final Map<String, d> f293d = new HashMap();

    /* renamed from: e, reason: collision with root package name */
    ArrayList<String> f294e = new ArrayList<>();

    /* renamed from: f, reason: collision with root package name */
    final transient Map<String, c<?>> f295f = new HashMap();

    /* renamed from: g, reason: collision with root package name */
    final Map<String, Object> f296g = new HashMap();

    /* renamed from: h, reason: collision with root package name */
    final Bundle f297h = new Bundle();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [I] */
    /* loaded from: classes.dex */
    public class a<I> extends ActivityResultLauncher<I> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f302a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ ActivityResultContract f303b;

        a(String str, ActivityResultContract activityResultContract) {
            this.f302a = str;
            this.f303b = activityResultContract;
        }

        @Override // android.view.result.ActivityResultLauncher
        public void b(I i10, ActivityOptionsCompat activityOptionsCompat) {
            Integer num = ActivityResultRegistry.this.f292c.get(this.f302a);
            if (num != null) {
                ActivityResultRegistry.this.f294e.add(this.f302a);
                try {
                    ActivityResultRegistry.this.f(num.intValue(), this.f303b, i10, activityOptionsCompat);
                    return;
                } catch (Exception e10) {
                    ActivityResultRegistry.this.f294e.remove(this.f302a);
                    throw e10;
                }
            }
            throw new IllegalStateException("Attempting to launch an unregistered ActivityResultLauncher with contract " + this.f303b + " and input " + i10 + ". You must ensure the ActivityResultLauncher is registered before calling launch().");
        }

        @Override // android.view.result.ActivityResultLauncher
        public void c() {
            ActivityResultRegistry.this.l(this.f302a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [I] */
    /* loaded from: classes.dex */
    public class b<I> extends ActivityResultLauncher<I> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f305a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ ActivityResultContract f306b;

        b(String str, ActivityResultContract activityResultContract) {
            this.f305a = str;
            this.f306b = activityResultContract;
        }

        @Override // android.view.result.ActivityResultLauncher
        public void b(I i10, ActivityOptionsCompat activityOptionsCompat) {
            Integer num = ActivityResultRegistry.this.f292c.get(this.f305a);
            if (num != null) {
                ActivityResultRegistry.this.f294e.add(this.f305a);
                try {
                    ActivityResultRegistry.this.f(num.intValue(), this.f306b, i10, activityOptionsCompat);
                    return;
                } catch (Exception e10) {
                    ActivityResultRegistry.this.f294e.remove(this.f305a);
                    throw e10;
                }
            }
            throw new IllegalStateException("Attempting to launch an unregistered ActivityResultLauncher with contract " + this.f306b + " and input " + i10 + ". You must ensure the ActivityResultLauncher is registered before calling launch().");
        }

        @Override // android.view.result.ActivityResultLauncher
        public void c() {
            ActivityResultRegistry.this.l(this.f305a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class c<O> {

        /* renamed from: a, reason: collision with root package name */
        final ActivityResultCallback<O> f308a;

        /* renamed from: b, reason: collision with root package name */
        final ActivityResultContract<?, O> f309b;

        c(ActivityResultCallback<O> activityResultCallback, ActivityResultContract<?, O> activityResultContract) {
            this.f308a = activityResultCallback;
            this.f309b = activityResultContract;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class d {

        /* renamed from: a, reason: collision with root package name */
        final h f310a;

        /* renamed from: b, reason: collision with root package name */
        private final ArrayList<LifecycleEventObserver> f311b = new ArrayList<>();

        d(h hVar) {
            this.f310a = hVar;
        }

        void a(LifecycleEventObserver lifecycleEventObserver) {
            this.f310a.a(lifecycleEventObserver);
            this.f311b.add(lifecycleEventObserver);
        }

        void b() {
            Iterator<LifecycleEventObserver> it = this.f311b.iterator();
            while (it.hasNext()) {
                this.f310a.c(it.next());
            }
            this.f311b.clear();
        }
    }

    private void a(int i10, String str) {
        this.f291b.put(Integer.valueOf(i10), str);
        this.f292c.put(str, Integer.valueOf(i10));
    }

    private <O> void d(String str, int i10, Intent intent, c<O> cVar) {
        if (cVar != null && cVar.f308a != null && this.f294e.contains(str)) {
            cVar.f308a.a(cVar.f309b.c(i10, intent));
            this.f294e.remove(str);
        } else {
            this.f296g.remove(str);
            this.f297h.putParcelable(str, new ActivityResult(i10, intent));
        }
    }

    private int e() {
        int nextInt = this.f290a.nextInt(2147418112);
        while (true) {
            int i10 = nextInt + 65536;
            if (!this.f291b.containsKey(Integer.valueOf(i10))) {
                return i10;
            }
            nextInt = this.f290a.nextInt(2147418112);
        }
    }

    private void k(String str) {
        if (this.f292c.get(str) != null) {
            return;
        }
        a(e(), str);
    }

    public final boolean b(int i10, int i11, Intent intent) {
        String str = this.f291b.get(Integer.valueOf(i10));
        if (str == null) {
            return false;
        }
        d(str, i11, intent, this.f295f.get(str));
        return true;
    }

    public final <O> boolean c(int i10, @SuppressLint({"UnknownNullness"}) O o10) {
        ActivityResultCallback<?> activityResultCallback;
        String str = this.f291b.get(Integer.valueOf(i10));
        if (str == null) {
            return false;
        }
        c<?> cVar = this.f295f.get(str);
        if (cVar != null && (activityResultCallback = cVar.f308a) != null) {
            if (!this.f294e.remove(str)) {
                return true;
            }
            activityResultCallback.a(o10);
            return true;
        }
        this.f297h.remove(str);
        this.f296g.put(str, o10);
        return true;
    }

    public abstract <I, O> void f(int i10, ActivityResultContract<I, O> activityResultContract, @SuppressLint({"UnknownNullness"}) I i11, ActivityOptionsCompat activityOptionsCompat);

    public final void g(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        ArrayList<Integer> integerArrayList = bundle.getIntegerArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_RCS");
        ArrayList<String> stringArrayList = bundle.getStringArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS");
        if (stringArrayList == null || integerArrayList == null) {
            return;
        }
        this.f294e = bundle.getStringArrayList("KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS");
        this.f290a = (Random) bundle.getSerializable("KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT");
        this.f297h.putAll(bundle.getBundle("KEY_COMPONENT_ACTIVITY_PENDING_RESULT"));
        for (int i10 = 0; i10 < stringArrayList.size(); i10++) {
            String str = stringArrayList.get(i10);
            if (this.f292c.containsKey(str)) {
                Integer remove = this.f292c.remove(str);
                if (!this.f297h.containsKey(str)) {
                    this.f291b.remove(remove);
                }
            }
            a(integerArrayList.get(i10).intValue(), stringArrayList.get(i10));
        }
    }

    public final void h(Bundle bundle) {
        bundle.putIntegerArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_RCS", new ArrayList<>(this.f292c.values()));
        bundle.putStringArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS", new ArrayList<>(this.f292c.keySet()));
        bundle.putStringArrayList("KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS", new ArrayList<>(this.f294e));
        bundle.putBundle("KEY_COMPONENT_ACTIVITY_PENDING_RESULT", (Bundle) this.f297h.clone());
        bundle.putSerializable("KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT", this.f290a);
    }

    public final <I, O> ActivityResultLauncher<I> i(final String str, o oVar, final ActivityResultContract<I, O> activityResultContract, final ActivityResultCallback<O> activityResultCallback) {
        h lifecycle = oVar.getLifecycle();
        if (!lifecycle.b().a(h.c.STARTED)) {
            k(str);
            d dVar = this.f293d.get(str);
            if (dVar == null) {
                dVar = new d(lifecycle);
            }
            dVar.a(new LifecycleEventObserver() { // from class: androidx.activity.result.ActivityResultRegistry.1
                @Override // androidx.lifecycle.LifecycleEventObserver
                public void a(o oVar2, h.b bVar) {
                    if (h.b.ON_START.equals(bVar)) {
                        ActivityResultRegistry.this.f295f.put(str, new c<>(activityResultCallback, activityResultContract));
                        if (ActivityResultRegistry.this.f296g.containsKey(str)) {
                            Object obj = ActivityResultRegistry.this.f296g.get(str);
                            ActivityResultRegistry.this.f296g.remove(str);
                            activityResultCallback.a(obj);
                        }
                        ActivityResult activityResult = (ActivityResult) ActivityResultRegistry.this.f297h.getParcelable(str);
                        if (activityResult != null) {
                            ActivityResultRegistry.this.f297h.remove(str);
                            activityResultCallback.a(activityResultContract.c(activityResult.k(), activityResult.j()));
                            return;
                        }
                        return;
                    }
                    if (h.b.ON_STOP.equals(bVar)) {
                        ActivityResultRegistry.this.f295f.remove(str);
                    } else if (h.b.ON_DESTROY.equals(bVar)) {
                        ActivityResultRegistry.this.l(str);
                    }
                }
            });
            this.f293d.put(str, dVar);
            return new a(str, activityResultContract);
        }
        throw new IllegalStateException("LifecycleOwner " + oVar + " is attempting to register while current state is " + lifecycle.b() + ". LifecycleOwners must call register before they are STARTED.");
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <I, O> ActivityResultLauncher<I> j(String str, ActivityResultContract<I, O> activityResultContract, ActivityResultCallback<O> activityResultCallback) {
        k(str);
        this.f295f.put(str, new c<>(activityResultCallback, activityResultContract));
        if (this.f296g.containsKey(str)) {
            Object obj = this.f296g.get(str);
            this.f296g.remove(str);
            activityResultCallback.a(obj);
        }
        ActivityResult activityResult = (ActivityResult) this.f297h.getParcelable(str);
        if (activityResult != null) {
            this.f297h.remove(str);
            activityResultCallback.a(activityResultContract.c(activityResult.k(), activityResult.j()));
        }
        return new b(str, activityResultContract);
    }

    final void l(String str) {
        Integer remove;
        if (!this.f294e.contains(str) && (remove = this.f292c.remove(str)) != null) {
            this.f291b.remove(remove);
        }
        this.f295f.remove(str);
        if (this.f296g.containsKey(str)) {
            Log.w("ActivityResultRegistry", "Dropping pending result for request " + str + ": " + this.f296g.get(str));
            this.f296g.remove(str);
        }
        if (this.f297h.containsKey(str)) {
            Log.w("ActivityResultRegistry", "Dropping pending result for request " + str + ": " + this.f297h.getParcelable(str));
            this.f297h.remove(str);
        }
        d dVar = this.f293d.get(str);
        if (dVar != null) {
            dVar.b();
            this.f293d.remove(str);
        }
    }
}
