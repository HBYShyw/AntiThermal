package androidx.startup;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import e0.Initializer;
import e0.StartupException;
import f0.Trace;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* compiled from: AppInitializer.java */
/* renamed from: androidx.startup.a, reason: use source file name */
/* loaded from: classes.dex */
public final class AppInitializer {

    /* renamed from: d, reason: collision with root package name */
    private static volatile AppInitializer f3950d;

    /* renamed from: e, reason: collision with root package name */
    private static final Object f3951e = new Object();

    /* renamed from: c, reason: collision with root package name */
    final Context f3954c;

    /* renamed from: b, reason: collision with root package name */
    final Set<Class<? extends Initializer<?>>> f3953b = new HashSet();

    /* renamed from: a, reason: collision with root package name */
    final Map<Class<?>, Object> f3952a = new HashMap();

    AppInitializer(Context context) {
        this.f3954c = context.getApplicationContext();
    }

    private <T> T d(Class<? extends Initializer<?>> cls, Set<Class<?>> set) {
        T t7;
        if (Trace.d()) {
            try {
                Trace.a(cls.getSimpleName());
            } finally {
                Trace.b();
            }
        }
        if (!set.contains(cls)) {
            if (!this.f3952a.containsKey(cls)) {
                set.add(cls);
                try {
                    Initializer<?> newInstance = cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                    List<Class<? extends Initializer<?>>> a10 = newInstance.a();
                    if (!a10.isEmpty()) {
                        for (Class<? extends Initializer<?>> cls2 : a10) {
                            if (!this.f3952a.containsKey(cls2)) {
                                d(cls2, set);
                            }
                        }
                    }
                    t7 = (T) newInstance.b(this.f3954c);
                    set.remove(cls);
                    this.f3952a.put(cls, t7);
                } catch (Throwable th) {
                    throw new StartupException(th);
                }
            } else {
                t7 = (T) this.f3952a.get(cls);
            }
            return t7;
        }
        throw new IllegalStateException(String.format("Cannot initialize %s. Cycle detected.", cls.getName()));
    }

    public static AppInitializer e(Context context) {
        if (f3950d == null) {
            synchronized (f3951e) {
                if (f3950d == null) {
                    f3950d = new AppInitializer(context);
                }
            }
        }
        return f3950d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        try {
            try {
                Trace.a("Startup");
                b(this.f3954c.getPackageManager().getProviderInfo(new ComponentName(this.f3954c.getPackageName(), InitializationProvider.class.getName()), 128).metaData);
            } catch (PackageManager.NameNotFoundException e10) {
                throw new StartupException(e10);
            }
        } finally {
            Trace.b();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    void b(Bundle bundle) {
        String string = this.f3954c.getString(R$string.androidx_startup);
        if (bundle != null) {
            try {
                HashSet hashSet = new HashSet();
                for (String str : bundle.keySet()) {
                    if (string.equals(bundle.getString(str, null))) {
                        Class<?> cls = Class.forName(str);
                        if (Initializer.class.isAssignableFrom(cls)) {
                            this.f3953b.add(cls);
                        }
                    }
                }
                Iterator<Class<? extends Initializer<?>>> it = this.f3953b.iterator();
                while (it.hasNext()) {
                    d(it.next(), hashSet);
                }
            } catch (ClassNotFoundException e10) {
                throw new StartupException(e10);
            }
        }
    }

    <T> T c(Class<? extends Initializer<?>> cls) {
        T t7;
        synchronized (f3951e) {
            t7 = (T) this.f3952a.get(cls);
            if (t7 == null) {
                t7 = (T) d(cls, new HashSet());
            }
        }
        return t7;
    }

    public <T> T f(Class<? extends Initializer<T>> cls) {
        return (T) c(cls);
    }

    public boolean g(Class<? extends Initializer<?>> cls) {
        return this.f3953b.contains(cls);
    }
}
