package o0;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import s0.n;

/* compiled from: SceneDataMemoryDataSource.java */
/* renamed from: o0.b, reason: use source file name */
/* loaded from: classes.dex */
public class SceneDataMemoryDataSource {

    /* renamed from: a, reason: collision with root package name */
    private final Object f16101a = new Object();

    /* renamed from: b, reason: collision with root package name */
    private final Map<String, n> f16102b = new ConcurrentHashMap();

    public void a(String str, n nVar) {
        synchronized (this.f16101a) {
            this.f16102b.put(str + nVar.e(), nVar);
        }
    }

    public void b() {
        synchronized (this.f16101a) {
            this.f16102b.clear();
        }
    }

    public n c(String str, String str2) {
        synchronized (this.f16101a) {
            if (!this.f16102b.containsKey(str + str2)) {
                return null;
            }
            return this.f16102b.get(str + str2);
        }
    }

    public void d(String str, n nVar) {
        synchronized (this.f16101a) {
            if (this.f16102b.containsKey(str + nVar.e())) {
                if (this.f16102b.get(str + nVar.e()) == nVar) {
                    this.f16102b.remove(str + nVar.e());
                }
            }
        }
    }
}
