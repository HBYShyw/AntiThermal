package m;

import java.util.HashMap;
import l.LinearSystem;
import l.SolverVariable;
import m.ConstraintWidget;

/* compiled from: Barrier.java */
/* loaded from: classes.dex */
public class a extends HelperWidget {
    private int I0 = 0;
    private boolean J0 = true;
    private int K0 = 0;

    public boolean L0() {
        return this.J0;
    }

    public int M0() {
        return this.I0;
    }

    public int N0() {
        return this.K0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void O0() {
        for (int i10 = 0; i10 < this.H0; i10++) {
            ConstraintWidget constraintWidget = this.G0[i10];
            int i11 = this.I0;
            if (i11 == 0 || i11 == 1) {
                constraintWidget.p0(0, true);
            } else if (i11 == 2 || i11 == 3) {
                constraintWidget.p0(1, true);
            }
        }
    }

    public void P0(boolean z10) {
        this.J0 = z10;
    }

    public void Q0(int i10) {
        this.I0 = i10;
    }

    public void R0(int i10) {
        this.K0 = i10;
    }

    @Override // m.ConstraintWidget
    public void f(LinearSystem linearSystem) {
        ConstraintAnchor[] constraintAnchorArr;
        boolean z10;
        int i10;
        int i11;
        int i12;
        ConstraintAnchor[] constraintAnchorArr2 = this.L;
        constraintAnchorArr2[0] = this.D;
        constraintAnchorArr2[2] = this.E;
        constraintAnchorArr2[1] = this.F;
        constraintAnchorArr2[3] = this.G;
        int i13 = 0;
        while (true) {
            constraintAnchorArr = this.L;
            if (i13 >= constraintAnchorArr.length) {
                break;
            }
            constraintAnchorArr[i13].f14738g = linearSystem.q(constraintAnchorArr[i13]);
            i13++;
        }
        int i14 = this.I0;
        if (i14 < 0 || i14 >= 4) {
            return;
        }
        ConstraintAnchor constraintAnchor = constraintAnchorArr[i14];
        for (int i15 = 0; i15 < this.H0; i15++) {
            ConstraintWidget constraintWidget = this.G0[i15];
            if ((this.J0 || constraintWidget.g()) && ((((i11 = this.I0) == 0 || i11 == 1) && constraintWidget.z() == ConstraintWidget.b.MATCH_CONSTRAINT && constraintWidget.D.f14735d != null && constraintWidget.F.f14735d != null) || (((i12 = this.I0) == 2 || i12 == 3) && constraintWidget.N() == ConstraintWidget.b.MATCH_CONSTRAINT && constraintWidget.E.f14735d != null && constraintWidget.G.f14735d != null))) {
                z10 = true;
                break;
            }
        }
        z10 = false;
        boolean z11 = this.D.i() || this.F.i();
        boolean z12 = this.E.i() || this.G.i();
        int i16 = !z10 && (((i10 = this.I0) == 0 && z11) || ((i10 == 2 && z12) || ((i10 == 1 && z11) || (i10 == 3 && z12)))) ? 5 : 4;
        for (int i17 = 0; i17 < this.H0; i17++) {
            ConstraintWidget constraintWidget2 = this.G0[i17];
            if (this.J0 || constraintWidget2.g()) {
                SolverVariable q10 = linearSystem.q(constraintWidget2.L[this.I0]);
                ConstraintAnchor[] constraintAnchorArr3 = constraintWidget2.L;
                int i18 = this.I0;
                constraintAnchorArr3[i18].f14738g = q10;
                int i19 = (constraintAnchorArr3[i18].f14735d == null || constraintAnchorArr3[i18].f14735d.f14733b != this) ? 0 : constraintAnchorArr3[i18].f14736e + 0;
                if (i18 != 0 && i18 != 2) {
                    linearSystem.g(constraintAnchor.f14738g, q10, this.K0 + i19, z10);
                } else {
                    linearSystem.i(constraintAnchor.f14738g, q10, this.K0 - i19, z10);
                }
                linearSystem.e(constraintAnchor.f14738g, q10, this.K0 + i19, i16);
            }
        }
        int i20 = this.I0;
        if (i20 == 0) {
            linearSystem.e(this.F.f14738g, this.D.f14738g, 0, 8);
            linearSystem.e(this.D.f14738g, this.P.F.f14738g, 0, 4);
            linearSystem.e(this.D.f14738g, this.P.D.f14738g, 0, 0);
            return;
        }
        if (i20 == 1) {
            linearSystem.e(this.D.f14738g, this.F.f14738g, 0, 8);
            linearSystem.e(this.D.f14738g, this.P.D.f14738g, 0, 4);
            linearSystem.e(this.D.f14738g, this.P.F.f14738g, 0, 0);
        } else if (i20 == 2) {
            linearSystem.e(this.G.f14738g, this.E.f14738g, 0, 8);
            linearSystem.e(this.E.f14738g, this.P.G.f14738g, 0, 4);
            linearSystem.e(this.E.f14738g, this.P.E.f14738g, 0, 0);
        } else if (i20 == 3) {
            linearSystem.e(this.E.f14738g, this.G.f14738g, 0, 8);
            linearSystem.e(this.E.f14738g, this.P.E.f14738g, 0, 4);
            linearSystem.e(this.E.f14738g, this.P.G.f14738g, 0, 0);
        }
    }

    @Override // m.ConstraintWidget
    public boolean g() {
        return true;
    }

    @Override // m.HelperWidget, m.ConstraintWidget
    public void l(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.l(constraintWidget, hashMap);
        a aVar = (a) constraintWidget;
        this.I0 = aVar.I0;
        this.J0 = aVar.J0;
        this.K0 = aVar.K0;
    }

    @Override // m.ConstraintWidget
    public String toString() {
        String str = "[Barrier] " + s() + " {";
        for (int i10 = 0; i10 < this.H0; i10++) {
            ConstraintWidget constraintWidget = this.G0[i10];
            if (i10 > 0) {
                str = str + ", ";
            }
            str = str + constraintWidget.s();
        }
        return str + "}";
    }
}
