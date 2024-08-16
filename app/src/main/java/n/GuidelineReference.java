package n;

import m.ConstraintWidget;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: GuidelineReference.java */
/* renamed from: n.h, reason: use source file name */
/* loaded from: classes.dex */
public class GuidelineReference extends WidgetRun {
    public GuidelineReference(ConstraintWidget constraintWidget) {
        super(constraintWidget);
        constraintWidget.f14758e.f();
        constraintWidget.f14760f.f();
        this.f15610f = ((m.h) constraintWidget).L0();
    }

    private void q(DependencyNode dependencyNode) {
        this.f15612h.f15580k.add(dependencyNode);
        dependencyNode.f15581l.add(this.f15612h);
    }

    @Override // n.WidgetRun, n.Dependency
    public void a(Dependency dependency) {
        DependencyNode dependencyNode = this.f15612h;
        if (dependencyNode.f15572c && !dependencyNode.f15579j) {
            this.f15612h.d((int) ((dependencyNode.f15581l.get(0).f15576g * ((m.h) this.f15606b).O0()) + 0.5f));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // n.WidgetRun
    public void d() {
        m.h hVar = (m.h) this.f15606b;
        int M0 = hVar.M0();
        int N0 = hVar.N0();
        hVar.O0();
        if (hVar.L0() == 1) {
            if (M0 != -1) {
                this.f15612h.f15581l.add(this.f15606b.P.f14758e.f15612h);
                this.f15606b.P.f14758e.f15612h.f15580k.add(this.f15612h);
                this.f15612h.f15575f = M0;
            } else if (N0 != -1) {
                this.f15612h.f15581l.add(this.f15606b.P.f14758e.f15613i);
                this.f15606b.P.f14758e.f15613i.f15580k.add(this.f15612h);
                this.f15612h.f15575f = -N0;
            } else {
                DependencyNode dependencyNode = this.f15612h;
                dependencyNode.f15571b = true;
                dependencyNode.f15581l.add(this.f15606b.P.f14758e.f15613i);
                this.f15606b.P.f14758e.f15613i.f15580k.add(this.f15612h);
            }
            q(this.f15606b.f14758e.f15612h);
            q(this.f15606b.f14758e.f15613i);
            return;
        }
        if (M0 != -1) {
            this.f15612h.f15581l.add(this.f15606b.P.f14760f.f15612h);
            this.f15606b.P.f14760f.f15612h.f15580k.add(this.f15612h);
            this.f15612h.f15575f = M0;
        } else if (N0 != -1) {
            this.f15612h.f15581l.add(this.f15606b.P.f14760f.f15613i);
            this.f15606b.P.f14760f.f15613i.f15580k.add(this.f15612h);
            this.f15612h.f15575f = -N0;
        } else {
            DependencyNode dependencyNode2 = this.f15612h;
            dependencyNode2.f15571b = true;
            dependencyNode2.f15581l.add(this.f15606b.P.f14760f.f15613i);
            this.f15606b.P.f14760f.f15613i.f15580k.add(this.f15612h);
        }
        q(this.f15606b.f14760f.f15612h);
        q(this.f15606b.f14760f.f15613i);
    }

    @Override // n.WidgetRun
    public void e() {
        if (((m.h) this.f15606b).L0() == 1) {
            this.f15606b.G0(this.f15612h.f15576g);
        } else {
            this.f15606b.H0(this.f15612h.f15576g);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // n.WidgetRun
    public void f() {
        this.f15612h.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // n.WidgetRun
    public boolean m() {
        return false;
    }
}
