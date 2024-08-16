package n;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: DependencyNode.java */
/* renamed from: n.f, reason: use source file name */
/* loaded from: classes.dex */
public class DependencyNode implements Dependency {

    /* renamed from: d, reason: collision with root package name */
    WidgetRun f15573d;

    /* renamed from: f, reason: collision with root package name */
    int f15575f;

    /* renamed from: g, reason: collision with root package name */
    public int f15576g;

    /* renamed from: a, reason: collision with root package name */
    public Dependency f15570a = null;

    /* renamed from: b, reason: collision with root package name */
    public boolean f15571b = false;

    /* renamed from: c, reason: collision with root package name */
    public boolean f15572c = false;

    /* renamed from: e, reason: collision with root package name */
    a f15574e = a.UNKNOWN;

    /* renamed from: h, reason: collision with root package name */
    int f15577h = 1;

    /* renamed from: i, reason: collision with root package name */
    DimensionDependency f15578i = null;

    /* renamed from: j, reason: collision with root package name */
    public boolean f15579j = false;

    /* renamed from: k, reason: collision with root package name */
    List<Dependency> f15580k = new ArrayList();

    /* renamed from: l, reason: collision with root package name */
    List<DependencyNode> f15581l = new ArrayList();

    /* compiled from: DependencyNode.java */
    /* renamed from: n.f$a */
    /* loaded from: classes.dex */
    enum a {
        UNKNOWN,
        HORIZONTAL_DIMENSION,
        VERTICAL_DIMENSION,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        BASELINE
    }

    public DependencyNode(WidgetRun widgetRun) {
        this.f15573d = widgetRun;
    }

    @Override // n.Dependency
    public void a(Dependency dependency) {
        Iterator<DependencyNode> it = this.f15581l.iterator();
        while (it.hasNext()) {
            if (!it.next().f15579j) {
                return;
            }
        }
        this.f15572c = true;
        Dependency dependency2 = this.f15570a;
        if (dependency2 != null) {
            dependency2.a(this);
        }
        if (this.f15571b) {
            this.f15573d.a(this);
            return;
        }
        DependencyNode dependencyNode = null;
        int i10 = 0;
        for (DependencyNode dependencyNode2 : this.f15581l) {
            if (!(dependencyNode2 instanceof DimensionDependency)) {
                i10++;
                dependencyNode = dependencyNode2;
            }
        }
        if (dependencyNode != null && i10 == 1 && dependencyNode.f15579j) {
            DimensionDependency dimensionDependency = this.f15578i;
            if (dimensionDependency != null) {
                if (!dimensionDependency.f15579j) {
                    return;
                } else {
                    this.f15575f = this.f15577h * dimensionDependency.f15576g;
                }
            }
            d(dependencyNode.f15576g + this.f15575f);
        }
        Dependency dependency3 = this.f15570a;
        if (dependency3 != null) {
            dependency3.a(this);
        }
    }

    public void b(Dependency dependency) {
        this.f15580k.add(dependency);
        if (this.f15579j) {
            dependency.a(dependency);
        }
    }

    public void c() {
        this.f15581l.clear();
        this.f15580k.clear();
        this.f15579j = false;
        this.f15576g = 0;
        this.f15572c = false;
        this.f15571b = false;
    }

    public void d(int i10) {
        if (this.f15579j) {
            return;
        }
        this.f15579j = true;
        this.f15576g = i10;
        for (Dependency dependency : this.f15580k) {
            dependency.a(dependency);
        }
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.f15573d.f15606b.s());
        sb2.append(":");
        sb2.append(this.f15574e);
        sb2.append("(");
        sb2.append(this.f15579j ? Integer.valueOf(this.f15576g) : "unresolved");
        sb2.append(") <t=");
        sb2.append(this.f15581l.size());
        sb2.append(":d=");
        sb2.append(this.f15580k.size());
        sb2.append(">");
        return sb2.toString();
    }
}
