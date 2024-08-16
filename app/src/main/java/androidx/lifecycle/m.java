package androidx.lifecycle;

import kotlin.Metadata;
import td.Dispatchers;
import td.a2;

/* compiled from: Lifecycle.kt */
@Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\"\u0015\u0010\u0004\u001a\u00020\u0001*\u00020\u00008F¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0003¨\u0006\u0005"}, d2 = {"Landroidx/lifecycle/h;", "Landroidx/lifecycle/i;", "a", "(Landroidx/lifecycle/h;)Landroidx/lifecycle/i;", "coroutineScope", "lifecycle-runtime-ktx_release"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class m {
    public static final i a(h hVar) {
        LifecycleCoroutineScopeImpl lifecycleCoroutineScopeImpl;
        za.k.e(hVar, "<this>");
        do {
            LifecycleCoroutineScopeImpl lifecycleCoroutineScopeImpl2 = (LifecycleCoroutineScopeImpl) hVar.f3182a.get();
            if (lifecycleCoroutineScopeImpl2 != null) {
                return lifecycleCoroutineScopeImpl2;
            }
            lifecycleCoroutineScopeImpl = new LifecycleCoroutineScopeImpl(hVar, a2.b(null, 1, null).o0(Dispatchers.c().w0()));
        } while (!hVar.f3182a.compareAndSet(null, lifecycleCoroutineScopeImpl));
        lifecycleCoroutineScopeImpl.c();
        return lifecycleCoroutineScopeImpl;
    }
}
