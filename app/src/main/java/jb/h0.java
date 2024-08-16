package jb;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import ub.RuntimeModuleData;
import vb.reflectClassUtil;

/* compiled from: moduleByClassLoader.kt */
/* loaded from: classes2.dex */
public final class h0 {

    /* renamed from: a, reason: collision with root package name */
    private static final ConcurrentMap<p0, WeakReference<RuntimeModuleData>> f13192a = new ConcurrentHashMap();

    public static final RuntimeModuleData a(Class<?> cls) {
        za.k.e(cls, "<this>");
        ClassLoader f10 = reflectClassUtil.f(cls);
        p0 p0Var = new p0(f10);
        ConcurrentMap<p0, WeakReference<RuntimeModuleData>> concurrentMap = f13192a;
        WeakReference<RuntimeModuleData> weakReference = concurrentMap.get(p0Var);
        if (weakReference != null) {
            RuntimeModuleData runtimeModuleData = weakReference.get();
            if (runtimeModuleData != null) {
                return runtimeModuleData;
            }
            concurrentMap.remove(p0Var, weakReference);
        }
        RuntimeModuleData a10 = RuntimeModuleData.f18981c.a(f10);
        while (true) {
            try {
                ConcurrentMap<p0, WeakReference<RuntimeModuleData>> concurrentMap2 = f13192a;
                WeakReference<RuntimeModuleData> putIfAbsent = concurrentMap2.putIfAbsent(p0Var, new WeakReference<>(a10));
                if (putIfAbsent == null) {
                    return a10;
                }
                RuntimeModuleData runtimeModuleData2 = putIfAbsent.get();
                if (runtimeModuleData2 != null) {
                    return runtimeModuleData2;
                }
                concurrentMap2.remove(p0Var, putIfAbsent);
            } finally {
                p0Var.a(null);
            }
        }
    }
}
