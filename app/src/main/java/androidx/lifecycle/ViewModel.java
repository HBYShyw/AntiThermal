package androidx.lifecycle;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/* compiled from: ViewModel.java */
/* renamed from: androidx.lifecycle.g0, reason: use source file name */
/* loaded from: classes.dex */
public abstract class ViewModel {

    /* renamed from: a, reason: collision with root package name */
    private final Map<String, Object> f3179a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    private final Set<Closeable> f3180b = new LinkedHashSet();

    /* renamed from: c, reason: collision with root package name */
    private volatile boolean f3181c = false;

    private static void b(Object obj) {
        if (obj instanceof Closeable) {
            try {
                ((Closeable) obj).close();
            } catch (IOException e10) {
                throw new RuntimeException(e10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a() {
        this.f3181c = true;
        Map<String, Object> map = this.f3179a;
        if (map != null) {
            synchronized (map) {
                Iterator<Object> it = this.f3179a.values().iterator();
                while (it.hasNext()) {
                    b(it.next());
                }
            }
        }
        Set<Closeable> set = this.f3180b;
        if (set != null) {
            synchronized (set) {
                Iterator<Closeable> it2 = this.f3180b.iterator();
                while (it2.hasNext()) {
                    b(it2.next());
                }
            }
        }
        d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <T> T c(String str) {
        T t7;
        Map<String, Object> map = this.f3179a;
        if (map == null) {
            return null;
        }
        synchronized (map) {
            t7 = (T) this.f3179a.get(str);
        }
        return t7;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void d() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public <T> T e(String str, T t7) {
        Object obj;
        synchronized (this.f3179a) {
            obj = this.f3179a.get(str);
            if (obj == 0) {
                this.f3179a.put(str, t7);
            }
        }
        if (obj != 0) {
            t7 = obj;
        }
        if (this.f3181c) {
            b(t7);
        }
        return t7;
    }
}
