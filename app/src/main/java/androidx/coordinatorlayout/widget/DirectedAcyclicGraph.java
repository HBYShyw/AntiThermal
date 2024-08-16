package androidx.coordinatorlayout.widget;

import androidx.core.util.e;
import androidx.core.util.f;
import j.SimpleArrayMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/* compiled from: DirectedAcyclicGraph.java */
/* renamed from: androidx.coordinatorlayout.widget.a, reason: use source file name */
/* loaded from: classes.dex */
public final class DirectedAcyclicGraph<T> {

    /* renamed from: a, reason: collision with root package name */
    private final e<ArrayList<T>> f2081a = new f(10);

    /* renamed from: b, reason: collision with root package name */
    private final SimpleArrayMap<T, ArrayList<T>> f2082b = new SimpleArrayMap<>();

    /* renamed from: c, reason: collision with root package name */
    private final ArrayList<T> f2083c = new ArrayList<>();

    /* renamed from: d, reason: collision with root package name */
    private final HashSet<T> f2084d = new HashSet<>();

    private void e(T t7, ArrayList<T> arrayList, HashSet<T> hashSet) {
        if (arrayList.contains(t7)) {
            return;
        }
        if (!hashSet.contains(t7)) {
            hashSet.add(t7);
            ArrayList<T> arrayList2 = this.f2082b.get(t7);
            if (arrayList2 != null) {
                int size = arrayList2.size();
                for (int i10 = 0; i10 < size; i10++) {
                    e(arrayList2.get(i10), arrayList, hashSet);
                }
            }
            hashSet.remove(t7);
            arrayList.add(t7);
            return;
        }
        throw new RuntimeException("This graph contains cyclic dependencies");
    }

    private ArrayList<T> f() {
        ArrayList<T> b10 = this.f2081a.b();
        return b10 == null ? new ArrayList<>() : b10;
    }

    private void k(ArrayList<T> arrayList) {
        arrayList.clear();
        this.f2081a.a(arrayList);
    }

    public void a(T t7, T t10) {
        if (this.f2082b.containsKey(t7) && this.f2082b.containsKey(t10)) {
            ArrayList<T> arrayList = this.f2082b.get(t7);
            if (arrayList == null) {
                arrayList = f();
                this.f2082b.put(t7, arrayList);
            }
            arrayList.add(t10);
            return;
        }
        throw new IllegalArgumentException("All nodes must be present in the graph before being added as an edge");
    }

    public void b(T t7) {
        if (this.f2082b.containsKey(t7)) {
            return;
        }
        this.f2082b.put(t7, null);
    }

    public void c() {
        int size = this.f2082b.size();
        for (int i10 = 0; i10 < size; i10++) {
            ArrayList<T> n10 = this.f2082b.n(i10);
            if (n10 != null) {
                k(n10);
            }
        }
        this.f2082b.clear();
    }

    public boolean d(T t7) {
        return this.f2082b.containsKey(t7);
    }

    public List g(T t7) {
        return this.f2082b.get(t7);
    }

    public List<T> h(T t7) {
        int size = this.f2082b.size();
        ArrayList arrayList = null;
        for (int i10 = 0; i10 < size; i10++) {
            ArrayList<T> n10 = this.f2082b.n(i10);
            if (n10 != null && n10.contains(t7)) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(this.f2082b.j(i10));
            }
        }
        return arrayList;
    }

    public ArrayList<T> i() {
        this.f2083c.clear();
        this.f2084d.clear();
        int size = this.f2082b.size();
        for (int i10 = 0; i10 < size; i10++) {
            e(this.f2082b.j(i10), this.f2083c, this.f2084d);
        }
        return this.f2083c;
    }

    public boolean j(T t7) {
        int size = this.f2082b.size();
        for (int i10 = 0; i10 < size; i10++) {
            ArrayList<T> n10 = this.f2082b.n(i10);
            if (n10 != null && n10.contains(t7)) {
                return true;
            }
        }
        return false;
    }
}
