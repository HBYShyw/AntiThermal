package androidx.lifecycle;

import androidx.lifecycle.h;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: ClassesInfoCache.java */
@Deprecated
/* renamed from: androidx.lifecycle.b, reason: use source file name */
/* loaded from: classes.dex */
final class ClassesInfoCache {

    /* renamed from: c, reason: collision with root package name */
    static ClassesInfoCache f3155c = new ClassesInfoCache();

    /* renamed from: a, reason: collision with root package name */
    private final Map<Class<?>, a> f3156a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    private final Map<Class<?>, Boolean> f3157b = new HashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClassesInfoCache.java */
    @Deprecated
    /* renamed from: androidx.lifecycle.b$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        final Map<h.b, List<b>> f3158a = new HashMap();

        /* renamed from: b, reason: collision with root package name */
        final Map<b, h.b> f3159b;

        a(Map<b, h.b> map) {
            this.f3159b = map;
            for (Map.Entry<b, h.b> entry : map.entrySet()) {
                h.b value = entry.getValue();
                List<b> list = this.f3158a.get(value);
                if (list == null) {
                    list = new ArrayList<>();
                    this.f3158a.put(value, list);
                }
                list.add(entry.getKey());
            }
        }

        private static void b(List<b> list, o oVar, h.b bVar, Object obj) {
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    list.get(size).a(oVar, bVar, obj);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void a(o oVar, h.b bVar, Object obj) {
            b(this.f3158a.get(bVar), oVar, bVar, obj);
            b(this.f3158a.get(h.b.ON_ANY), oVar, bVar, obj);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClassesInfoCache.java */
    @Deprecated
    /* renamed from: androidx.lifecycle.b$b */
    /* loaded from: classes.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        final int f3160a;

        /* renamed from: b, reason: collision with root package name */
        final Method f3161b;

        b(int i10, Method method) {
            this.f3160a = i10;
            this.f3161b = method;
            method.setAccessible(true);
        }

        void a(o oVar, h.b bVar, Object obj) {
            try {
                int i10 = this.f3160a;
                if (i10 == 0) {
                    this.f3161b.invoke(obj, new Object[0]);
                } else if (i10 == 1) {
                    this.f3161b.invoke(obj, oVar);
                } else {
                    if (i10 != 2) {
                        return;
                    }
                    this.f3161b.invoke(obj, oVar, bVar);
                }
            } catch (IllegalAccessException e10) {
                throw new RuntimeException(e10);
            } catch (InvocationTargetException e11) {
                throw new RuntimeException("Failed to call observer method", e11.getCause());
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof b)) {
                return false;
            }
            b bVar = (b) obj;
            return this.f3160a == bVar.f3160a && this.f3161b.getName().equals(bVar.f3161b.getName());
        }

        public int hashCode() {
            return (this.f3160a * 31) + this.f3161b.getName().hashCode();
        }
    }

    ClassesInfoCache() {
    }

    private a a(Class<?> cls, Method[] methodArr) {
        int i10;
        a c10;
        Class<? super Object> superclass = cls.getSuperclass();
        HashMap hashMap = new HashMap();
        if (superclass != null && (c10 = c(superclass)) != null) {
            hashMap.putAll(c10.f3159b);
        }
        for (Class<?> cls2 : cls.getInterfaces()) {
            for (Map.Entry<b, h.b> entry : c(cls2).f3159b.entrySet()) {
                e(hashMap, entry.getKey(), entry.getValue(), cls);
            }
        }
        if (methodArr == null) {
            methodArr = b(cls);
        }
        boolean z10 = false;
        for (Method method : methodArr) {
            OnLifecycleEvent onLifecycleEvent = (OnLifecycleEvent) method.getAnnotation(OnLifecycleEvent.class);
            if (onLifecycleEvent != null) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length <= 0) {
                    i10 = 0;
                } else {
                    if (!parameterTypes[0].isAssignableFrom(o.class)) {
                        throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
                    }
                    i10 = 1;
                }
                h.b value = onLifecycleEvent.value();
                if (parameterTypes.length > 1) {
                    if (parameterTypes[1].isAssignableFrom(h.b.class)) {
                        if (value != h.b.ON_ANY) {
                            throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
                        }
                        i10 = 2;
                    } else {
                        throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
                    }
                }
                if (parameterTypes.length <= 2) {
                    e(hashMap, new b(i10, method), value, cls);
                    z10 = true;
                } else {
                    throw new IllegalArgumentException("cannot have more than 2 params");
                }
            }
        }
        a aVar = new a(hashMap);
        this.f3156a.put(cls, aVar);
        this.f3157b.put(cls, Boolean.valueOf(z10));
        return aVar;
    }

    private Method[] b(Class<?> cls) {
        try {
            return cls.getDeclaredMethods();
        } catch (NoClassDefFoundError e10) {
            throw new IllegalArgumentException("The observer class has some methods that use newer APIs which are not available in the current OS version. Lifecycles cannot access even other methods so you should make sure that your observer classes only access framework classes that are available in your min API level OR use lifecycle:compiler annotation processor.", e10);
        }
    }

    private void e(Map<b, h.b> map, b bVar, h.b bVar2, Class<?> cls) {
        h.b bVar3 = map.get(bVar);
        if (bVar3 == null || bVar2 == bVar3) {
            if (bVar3 == null) {
                map.put(bVar, bVar2);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Method " + bVar.f3161b.getName() + " in " + cls.getName() + " already declared with different @OnLifecycleEvent value: previous value " + bVar3 + ", new value " + bVar2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a c(Class<?> cls) {
        a aVar = this.f3156a.get(cls);
        return aVar != null ? aVar : a(cls, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d(Class<?> cls) {
        Boolean bool = this.f3157b.get(cls);
        if (bool != null) {
            return bool.booleanValue();
        }
        Method[] b10 = b(cls);
        for (Method method : b10) {
            if (((OnLifecycleEvent) method.getAnnotation(OnLifecycleEvent.class)) != null) {
                a(cls, b10);
                return true;
            }
        }
        this.f3157b.put(cls, Boolean.FALSE);
        return false;
    }
}
