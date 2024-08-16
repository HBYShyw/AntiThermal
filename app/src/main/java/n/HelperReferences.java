package n;

import java.util.Iterator;
import m.ConstraintWidget;
import n.DependencyNode;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: HelperReferences.java */
/* renamed from: n.i, reason: use source file name */
/* loaded from: classes.dex */
public class HelperReferences extends WidgetRun {
    public HelperReferences(ConstraintWidget constraintWidget) {
        super(constraintWidget);
    }

    private void q(DependencyNode dependencyNode) {
        this.f15612h.f15580k.add(dependencyNode);
        dependencyNode.f15581l.add(this.f15612h);
    }

    @Override // n.WidgetRun, n.Dependency
    public void a(Dependency dependency) {
        m.a aVar = (m.a) this.f15606b;
        int M0 = aVar.M0();
        Iterator<DependencyNode> it = this.f15612h.f15581l.iterator();
        int i10 = 0;
        int i11 = -1;
        while (it.hasNext()) {
            int i12 = it.next().f15576g;
            if (i11 == -1 || i12 < i11) {
                i11 = i12;
            }
            if (i10 < i12) {
                i10 = i12;
            }
        }
        if (M0 != 0 && M0 != 2) {
            this.f15612h.d(i10 + aVar.N0());
        } else {
            this.f15612h.d(i11 + aVar.N0());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // n.WidgetRun
    public void d() {
        ConstraintWidget constraintWidget = this.f15606b;
        if (constraintWidget instanceof m.a) {
            this.f15612h.f15571b = true;
            m.a aVar = (m.a) constraintWidget;
            int M0 = aVar.M0();
            boolean L0 = aVar.L0();
            int i10 = 0;
            if (M0 == 0) {
                this.f15612h.f15574e = DependencyNode.a.LEFT;
                while (i10 < aVar.H0) {
                    ConstraintWidget constraintWidget2 = aVar.G0[i10];
                    if (L0 || constraintWidget2.P() != 8) {
                        DependencyNode dependencyNode = constraintWidget2.f14758e.f15612h;
                        dependencyNode.f15580k.add(this.f15612h);
                        this.f15612h.f15581l.add(dependencyNode);
                    }
                    i10++;
                }
                q(this.f15606b.f14758e.f15612h);
                q(this.f15606b.f14758e.f15613i);
                return;
            }
            if (M0 == 1) {
                this.f15612h.f15574e = DependencyNode.a.RIGHT;
                while (i10 < aVar.H0) {
                    ConstraintWidget constraintWidget3 = aVar.G0[i10];
                    if (L0 || constraintWidget3.P() != 8) {
                        DependencyNode dependencyNode2 = constraintWidget3.f14758e.f15613i;
                        dependencyNode2.f15580k.add(this.f15612h);
                        this.f15612h.f15581l.add(dependencyNode2);
                    }
                    i10++;
                }
                q(this.f15606b.f14758e.f15612h);
                q(this.f15606b.f14758e.f15613i);
                return;
            }
            if (M0 == 2) {
                this.f15612h.f15574e = DependencyNode.a.TOP;
                while (i10 < aVar.H0) {
                    ConstraintWidget constraintWidget4 = aVar.G0[i10];
                    if (L0 || constraintWidget4.P() != 8) {
                        DependencyNode dependencyNode3 = constraintWidget4.f14760f.f15612h;
                        dependencyNode3.f15580k.add(this.f15612h);
                        this.f15612h.f15581l.add(dependencyNode3);
                    }
                    i10++;
                }
                q(this.f15606b.f14760f.f15612h);
                q(this.f15606b.f14760f.f15613i);
                return;
            }
            if (M0 != 3) {
                return;
            }
            this.f15612h.f15574e = DependencyNode.a.BOTTOM;
            while (i10 < aVar.H0) {
                ConstraintWidget constraintWidget5 = aVar.G0[i10];
                if (L0 || constraintWidget5.P() != 8) {
                    DependencyNode dependencyNode4 = constraintWidget5.f14760f.f15613i;
                    dependencyNode4.f15580k.add(this.f15612h);
                    this.f15612h.f15581l.add(dependencyNode4);
                }
                i10++;
            }
            q(this.f15606b.f14760f.f15612h);
            q(this.f15606b.f14760f.f15613i);
        }
    }

    @Override // n.WidgetRun
    public void e() {
        ConstraintWidget constraintWidget = this.f15606b;
        if (constraintWidget instanceof m.a) {
            int M0 = ((m.a) constraintWidget).M0();
            if (M0 != 0 && M0 != 1) {
                this.f15606b.H0(this.f15612h.f15576g);
            } else {
                this.f15606b.G0(this.f15612h.f15576g);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // n.WidgetRun
    public void f() {
        this.f15607c = null;
        this.f15612h.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // n.WidgetRun
    public boolean m() {
        return false;
    }
}
