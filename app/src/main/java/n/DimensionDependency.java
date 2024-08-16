package n;

import n.DependencyNode;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DimensionDependency.java */
/* renamed from: n.g, reason: use source file name */
/* loaded from: classes.dex */
public class DimensionDependency extends DependencyNode {

    /* renamed from: m, reason: collision with root package name */
    public int f15591m;

    public DimensionDependency(WidgetRun widgetRun) {
        super(widgetRun);
        if (widgetRun instanceof HorizontalWidgetRun) {
            this.f15574e = DependencyNode.a.HORIZONTAL_DIMENSION;
        } else {
            this.f15574e = DependencyNode.a.VERTICAL_DIMENSION;
        }
    }

    @Override // n.DependencyNode
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
}
