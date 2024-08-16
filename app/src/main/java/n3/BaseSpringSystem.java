package n3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/* compiled from: BaseSpringSystem.java */
/* renamed from: n3.b, reason: use source file name */
/* loaded from: classes.dex */
public class BaseSpringSystem {

    /* renamed from: c, reason: collision with root package name */
    private final SpringLooper f15722c;

    /* renamed from: a, reason: collision with root package name */
    private final Map<String, f> f15720a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    private final Set<f> f15721b = new CopyOnWriteArraySet();

    /* renamed from: d, reason: collision with root package name */
    private final CopyOnWriteArraySet<SpringSystemListener> f15723d = new CopyOnWriteArraySet<>();

    /* renamed from: e, reason: collision with root package name */
    private boolean f15724e = true;

    public BaseSpringSystem(SpringLooper springLooper) {
        if (springLooper != null) {
            this.f15722c = springLooper;
            springLooper.a(this);
            return;
        }
        throw new IllegalArgumentException("springLooper is required");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(String str) {
        f fVar = this.f15720a.get(str);
        if (fVar != null) {
            this.f15721b.add(fVar);
            if (d()) {
                this.f15724e = false;
                this.f15722c.b();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("springId " + str + " does not reference a registered spring");
    }

    void b(double d10) {
        for (f fVar : this.f15721b) {
            if (fVar.r()) {
                fVar.b(d10 / 1000.0d);
            } else {
                this.f15721b.remove(fVar);
            }
        }
    }

    public f c() {
        f fVar = new f(this);
        f(fVar);
        return fVar;
    }

    public boolean d() {
        return this.f15724e;
    }

    public void e(double d10) {
        Iterator<SpringSystemListener> it = this.f15723d.iterator();
        while (it.hasNext()) {
            it.next().b(this);
        }
        b(d10);
        if (this.f15721b.isEmpty()) {
            this.f15724e = true;
        }
        Iterator<SpringSystemListener> it2 = this.f15723d.iterator();
        while (it2.hasNext()) {
            it2.next().a(this);
        }
        if (this.f15724e) {
            this.f15722c.c();
        }
    }

    void f(f fVar) {
        if (fVar != null) {
            if (!this.f15720a.containsKey(fVar.f())) {
                this.f15720a.put(fVar.f(), fVar);
                return;
            }
            throw new IllegalArgumentException("spring is already registered");
        }
        throw new IllegalArgumentException("spring is required");
    }
}
