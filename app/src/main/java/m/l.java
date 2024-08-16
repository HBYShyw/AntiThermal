package m;

import m.ConstraintWidget;
import n.BasicMeasure;

/* compiled from: VirtualLayout.java */
/* loaded from: classes.dex */
public class l extends HelperWidget {
    private int I0 = 0;
    private int J0 = 0;
    private int K0 = 0;
    private int L0 = 0;
    private int M0 = 0;
    private int N0 = 0;
    private int O0 = 0;
    private int P0 = 0;
    private boolean Q0 = false;
    private int R0 = 0;
    private int S0 = 0;
    protected BasicMeasure.a T0 = new BasicMeasure.a();
    BasicMeasure.b U0 = null;

    public void L0(boolean z10) {
        int i10 = this.M0;
        if (i10 > 0 || this.N0 > 0) {
            if (z10) {
                this.O0 = this.N0;
                this.P0 = i10;
            } else {
                this.O0 = i10;
                this.P0 = this.N0;
            }
        }
    }

    public void M0() {
        for (int i10 = 0; i10 < this.H0; i10++) {
            ConstraintWidget constraintWidget = this.G0[i10];
            if (constraintWidget != null) {
                constraintWidget.r0(true);
            }
        }
    }

    public int N0() {
        return this.S0;
    }

    public int O0() {
        return this.R0;
    }

    public int P0() {
        return this.J0;
    }

    public int Q0() {
        return this.O0;
    }

    public int R0() {
        return this.P0;
    }

    public int S0() {
        return this.I0;
    }

    public void T0(int i10, int i11, int i12, int i13) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void U0(ConstraintWidget constraintWidget, ConstraintWidget.b bVar, int i10, ConstraintWidget.b bVar2, int i11) {
        while (this.U0 == null && H() != null) {
            this.U0 = ((ConstraintWidgetContainer) H()).W0();
        }
        BasicMeasure.a aVar = this.T0;
        aVar.f15549a = bVar;
        aVar.f15550b = bVar2;
        aVar.f15551c = i10;
        aVar.f15552d = i11;
        this.U0.b(constraintWidget, aVar);
        constraintWidget.F0(this.T0.f15553e);
        constraintWidget.i0(this.T0.f15554f);
        constraintWidget.h0(this.T0.f15556h);
        constraintWidget.c0(this.T0.f15555g);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean V0() {
        ConstraintWidget constraintWidget = this.P;
        BasicMeasure.b W0 = constraintWidget != null ? ((ConstraintWidgetContainer) constraintWidget).W0() : null;
        if (W0 == null) {
            return false;
        }
        int i10 = 0;
        while (true) {
            if (i10 >= this.H0) {
                return true;
            }
            ConstraintWidget constraintWidget2 = this.G0[i10];
            if (constraintWidget2 != null && !(constraintWidget2 instanceof h)) {
                ConstraintWidget.b t7 = constraintWidget2.t(0);
                ConstraintWidget.b t10 = constraintWidget2.t(1);
                ConstraintWidget.b bVar = ConstraintWidget.b.MATCH_CONSTRAINT;
                if (!(t7 == bVar && constraintWidget2.f14772l != 1 && t10 == bVar && constraintWidget2.f14774m != 1)) {
                    if (t7 == bVar) {
                        t7 = ConstraintWidget.b.WRAP_CONTENT;
                    }
                    if (t10 == bVar) {
                        t10 = ConstraintWidget.b.WRAP_CONTENT;
                    }
                    BasicMeasure.a aVar = this.T0;
                    aVar.f15549a = t7;
                    aVar.f15550b = t10;
                    aVar.f15551c = constraintWidget2.Q();
                    this.T0.f15552d = constraintWidget2.w();
                    W0.b(constraintWidget2, this.T0);
                    constraintWidget2.F0(this.T0.f15553e);
                    constraintWidget2.i0(this.T0.f15554f);
                    constraintWidget2.c0(this.T0.f15555g);
                }
            }
            i10++;
        }
    }

    public boolean W0() {
        return this.Q0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void X0(boolean z10) {
        this.Q0 = z10;
    }

    public void Y0(int i10, int i11) {
        this.R0 = i10;
        this.S0 = i11;
    }

    public void Z0(int i10) {
        this.K0 = i10;
        this.I0 = i10;
        this.L0 = i10;
        this.J0 = i10;
        this.M0 = i10;
        this.N0 = i10;
    }

    @Override // m.HelperWidget, m.Helper
    public void a(ConstraintWidgetContainer constraintWidgetContainer) {
        M0();
    }

    public void a1(int i10) {
        this.J0 = i10;
    }

    public void b1(int i10) {
        this.N0 = i10;
    }

    public void c1(int i10) {
        this.K0 = i10;
        this.O0 = i10;
    }

    public void d1(int i10) {
        this.L0 = i10;
        this.P0 = i10;
    }

    public void e1(int i10) {
        this.M0 = i10;
        this.O0 = i10;
        this.P0 = i10;
    }

    public void f1(int i10) {
        this.I0 = i10;
    }
}
