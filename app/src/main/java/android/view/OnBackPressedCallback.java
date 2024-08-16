package android.view;

import androidx.core.util.Consumer;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: OnBackPressedCallback.java */
/* renamed from: androidx.activity.g, reason: use source file name */
/* loaded from: classes.dex */
public abstract class OnBackPressedCallback {

    /* renamed from: a, reason: collision with root package name */
    private boolean f282a;

    /* renamed from: b, reason: collision with root package name */
    private CopyOnWriteArrayList<a> f283b = new CopyOnWriteArrayList<>();

    /* renamed from: c, reason: collision with root package name */
    private Consumer<Boolean> f284c;

    public OnBackPressedCallback(boolean z10) {
        this.f282a = z10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(a aVar) {
        this.f283b.add(aVar);
    }

    public abstract void b();

    public final boolean c() {
        return this.f282a;
    }

    public final void d() {
        Iterator<a> it = this.f283b.iterator();
        while (it.hasNext()) {
            it.next().cancel();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(a aVar) {
        this.f283b.remove(aVar);
    }

    public final void f(boolean z10) {
        this.f282a = z10;
        Consumer<Boolean> consumer = this.f284c;
        if (consumer != null) {
            consumer.accept(Boolean.valueOf(z10));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(Consumer<Boolean> consumer) {
        this.f284c = consumer;
    }
}
