package m;

import l.LinearSystem;
import m.ConstraintWidget;

/* compiled from: Optimizer.java */
/* renamed from: m.k, reason: use source file name */
/* loaded from: classes.dex */
public class Optimizer {

    /* renamed from: a, reason: collision with root package name */
    static boolean[] f14852a = new boolean[3];

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, ConstraintWidget constraintWidget) {
        constraintWidget.f14768j = -1;
        constraintWidget.f14770k = -1;
        ConstraintWidget.b bVar = constraintWidgetContainer.O[0];
        ConstraintWidget.b bVar2 = ConstraintWidget.b.WRAP_CONTENT;
        if (bVar != bVar2 && constraintWidget.O[0] == ConstraintWidget.b.MATCH_PARENT) {
            int i10 = constraintWidget.D.f14736e;
            int Q = constraintWidgetContainer.Q() - constraintWidget.F.f14736e;
            ConstraintAnchor constraintAnchor = constraintWidget.D;
            constraintAnchor.f14738g = linearSystem.q(constraintAnchor);
            ConstraintAnchor constraintAnchor2 = constraintWidget.F;
            constraintAnchor2.f14738g = linearSystem.q(constraintAnchor2);
            linearSystem.f(constraintWidget.D.f14738g, i10);
            linearSystem.f(constraintWidget.F.f14738g, Q);
            constraintWidget.f14768j = 2;
            constraintWidget.l0(i10, Q);
        }
        if (constraintWidgetContainer.O[1] == bVar2 || constraintWidget.O[1] != ConstraintWidget.b.MATCH_PARENT) {
            return;
        }
        int i11 = constraintWidget.E.f14736e;
        int w10 = constraintWidgetContainer.w() - constraintWidget.G.f14736e;
        ConstraintAnchor constraintAnchor3 = constraintWidget.E;
        constraintAnchor3.f14738g = linearSystem.q(constraintAnchor3);
        ConstraintAnchor constraintAnchor4 = constraintWidget.G;
        constraintAnchor4.f14738g = linearSystem.q(constraintAnchor4);
        linearSystem.f(constraintWidget.E.f14738g, i11);
        linearSystem.f(constraintWidget.G.f14738g, w10);
        if (constraintWidget.f14751a0 > 0 || constraintWidget.P() == 8) {
            ConstraintAnchor constraintAnchor5 = constraintWidget.H;
            constraintAnchor5.f14738g = linearSystem.q(constraintAnchor5);
            linearSystem.f(constraintWidget.H.f14738g, constraintWidget.f14751a0 + i11);
        }
        constraintWidget.f14770k = 2;
        constraintWidget.A0(i11, w10);
    }

    public static final boolean b(int i10, int i11) {
        return (i10 & i11) == i11;
    }
}
