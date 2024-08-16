package a;

import android.content.Context;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/* compiled from: ContextAwareHelper.java */
/* renamed from: a.a, reason: use source file name */
/* loaded from: classes.dex */
public final class ContextAwareHelper {

    /* renamed from: a, reason: collision with root package name */
    private final Set<OnContextAvailableListener> f1a = new CopyOnWriteArraySet();

    /* renamed from: b, reason: collision with root package name */
    private volatile Context f2b;

    public void a(OnContextAvailableListener onContextAvailableListener) {
        if (this.f2b != null) {
            onContextAvailableListener.a(this.f2b);
        }
        this.f1a.add(onContextAvailableListener);
    }

    public void b() {
        this.f2b = null;
    }

    public void c(Context context) {
        this.f2b = context;
        Iterator<OnContextAvailableListener> it = this.f1a.iterator();
        while (it.hasNext()) {
            it.next().a(context);
        }
    }

    public Context d() {
        return this.f2b;
    }

    public void e(OnContextAvailableListener onContextAvailableListener) {
        this.f1a.remove(onContextAvailableListener);
    }
}
