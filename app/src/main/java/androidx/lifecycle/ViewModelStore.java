package androidx.lifecycle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* compiled from: ViewModelStore.java */
/* renamed from: androidx.lifecycle.j0, reason: use source file name */
/* loaded from: classes.dex */
public class ViewModelStore {

    /* renamed from: a, reason: collision with root package name */
    private final HashMap<String, ViewModel> f3205a = new HashMap<>();

    public final void a() {
        Iterator<ViewModel> it = this.f3205a.values().iterator();
        while (it.hasNext()) {
            it.next().a();
        }
        this.f3205a.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final ViewModel b(String str) {
        return this.f3205a.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<String> c() {
        return new HashSet(this.f3205a.keySet());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void d(String str, ViewModel viewModel) {
        ViewModel put = this.f3205a.put(str, viewModel);
        if (put != null) {
            put.d();
        }
    }
}
