package androidx.lifecycle;

import androidx.lifecycle.ClassesInfoCache;
import androidx.lifecycle.h;

/* JADX INFO: Access modifiers changed from: package-private */
@Deprecated
/* loaded from: classes.dex */
public class ReflectiveGenericLifecycleObserver implements LifecycleEventObserver {

    /* renamed from: e, reason: collision with root package name */
    private final Object f3105e;

    /* renamed from: f, reason: collision with root package name */
    private final ClassesInfoCache.a f3106f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReflectiveGenericLifecycleObserver(Object obj) {
        this.f3105e = obj;
        this.f3106f = ClassesInfoCache.f3155c.c(obj.getClass());
    }

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void a(o oVar, h.b bVar) {
        this.f3106f.a(oVar, bVar, this.f3105e);
    }
}
