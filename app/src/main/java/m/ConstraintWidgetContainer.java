package m;

import java.util.ArrayList;
import java.util.Arrays;
import l.LinearSystem;
import m.ConstraintWidget;
import n.BasicMeasure;
import n.DependencyGraph;

/* compiled from: ConstraintWidgetContainer.java */
/* renamed from: m.f, reason: use source file name */
/* loaded from: classes.dex */
public class ConstraintWidgetContainer extends WidgetContainer {
    int M0;
    int N0;
    int O0;
    int P0;
    BasicMeasure H0 = new BasicMeasure(this);
    public DependencyGraph I0 = new DependencyGraph(this);
    protected BasicMeasure.b J0 = null;
    private boolean K0 = false;
    protected LinearSystem L0 = new LinearSystem();
    int Q0 = 0;
    int R0 = 0;
    ChainHead[] S0 = new ChainHead[4];
    ChainHead[] T0 = new ChainHead[4];
    public boolean U0 = false;
    public boolean V0 = false;
    public boolean W0 = false;
    public int X0 = 0;
    public int Y0 = 0;
    private int Z0 = 263;

    /* renamed from: a1, reason: collision with root package name */
    public boolean f14809a1 = false;

    /* renamed from: b1, reason: collision with root package name */
    private boolean f14810b1 = false;

    /* renamed from: c1, reason: collision with root package name */
    private boolean f14811c1 = false;

    /* renamed from: d1, reason: collision with root package name */
    int f14812d1 = 0;

    private void R0(ConstraintWidget constraintWidget) {
        int i10 = this.Q0 + 1;
        ChainHead[] chainHeadArr = this.T0;
        if (i10 >= chainHeadArr.length) {
            this.T0 = (ChainHead[]) Arrays.copyOf(chainHeadArr, chainHeadArr.length * 2);
        }
        this.T0[this.Q0] = new ChainHead(constraintWidget, 0, c1());
        this.Q0++;
    }

    private void S0(ConstraintWidget constraintWidget) {
        int i10 = this.R0 + 1;
        ChainHead[] chainHeadArr = this.S0;
        if (i10 >= chainHeadArr.length) {
            this.S0 = (ChainHead[]) Arrays.copyOf(chainHeadArr, chainHeadArr.length * 2);
        }
        this.S0[this.R0] = new ChainHead(constraintWidget, 1, c1());
        this.R0++;
    }

    private void g1() {
        this.Q0 = 0;
        this.R0 = 0;
    }

    @Override // m.ConstraintWidget
    public void J0(boolean z10, boolean z11) {
        super.J0(z10, z11);
        int size = this.G0.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.G0.get(i10).J0(z10, z11);
        }
    }

    /* JADX WARN: Type inference failed for: r11v13 */
    /* JADX WARN: Type inference failed for: r11v8 */
    /* JADX WARN: Type inference failed for: r11v9, types: [boolean] */
    @Override // m.WidgetContainer
    public void M0() {
        boolean z10;
        ?? r11;
        boolean z11;
        this.U = 0;
        this.V = 0;
        int max = Math.max(0, Q());
        int max2 = Math.max(0, w());
        this.f14810b1 = false;
        this.f14811c1 = false;
        boolean z12 = f1(64) || f1(128);
        LinearSystem linearSystem = this.L0;
        linearSystem.f14509g = false;
        linearSystem.f14510h = false;
        if (this.Z0 != 0 && z12) {
            linearSystem.f14510h = true;
        }
        ConstraintWidget.b[] bVarArr = this.O;
        ConstraintWidget.b bVar = bVarArr[1];
        ConstraintWidget.b bVar2 = bVarArr[0];
        ArrayList<ConstraintWidget> arrayList = this.G0;
        ConstraintWidget.b z13 = z();
        ConstraintWidget.b bVar3 = ConstraintWidget.b.WRAP_CONTENT;
        boolean z14 = z13 == bVar3 || N() == bVar3;
        g1();
        int size = this.G0.size();
        for (int i10 = 0; i10 < size; i10++) {
            ConstraintWidget constraintWidget = this.G0.get(i10);
            if (constraintWidget instanceof WidgetContainer) {
                ((WidgetContainer) constraintWidget).M0();
            }
        }
        int i11 = 0;
        boolean z15 = false;
        boolean z16 = true;
        while (z16) {
            int i12 = i11 + 1;
            try {
                this.L0.D();
                g1();
                m(this.L0);
                for (int i13 = 0; i13 < size; i13++) {
                    this.G0.get(i13).m(this.L0);
                }
                z16 = Q0(this.L0);
                if (z16) {
                    this.L0.z();
                }
            } catch (Exception e10) {
                e10.printStackTrace();
                System.out.println("EXCEPTION : " + e10);
            }
            if (z16) {
                k1(this.L0, Optimizer.f14852a);
            } else {
                K0(this.L0);
                for (int i14 = 0; i14 < size; i14++) {
                    this.G0.get(i14).K0(this.L0);
                }
            }
            if (z14 && i12 < 8 && Optimizer.f14852a[2]) {
                int i15 = 0;
                int i16 = 0;
                for (int i17 = 0; i17 < size; i17++) {
                    ConstraintWidget constraintWidget2 = this.G0.get(i17);
                    i15 = Math.max(i15, constraintWidget2.U + constraintWidget2.Q());
                    i16 = Math.max(i16, constraintWidget2.V + constraintWidget2.w());
                }
                int max3 = Math.max(this.f14753b0, i15);
                int max4 = Math.max(this.f14755c0, i16);
                ConstraintWidget.b bVar4 = ConstraintWidget.b.WRAP_CONTENT;
                if (bVar2 != bVar4 || Q() >= max3) {
                    z10 = false;
                } else {
                    F0(max3);
                    this.O[0] = bVar4;
                    z10 = true;
                    z15 = true;
                }
                if (bVar == bVar4 && w() < max4) {
                    i0(max4);
                    this.O[1] = bVar4;
                    z10 = true;
                    z15 = true;
                }
            } else {
                z10 = false;
            }
            int max5 = Math.max(this.f14753b0, Q());
            if (max5 > Q()) {
                F0(max5);
                this.O[0] = ConstraintWidget.b.FIXED;
                z10 = true;
                z15 = true;
            }
            int max6 = Math.max(this.f14755c0, w());
            if (max6 > w()) {
                i0(max6);
                r11 = 1;
                this.O[1] = ConstraintWidget.b.FIXED;
                z10 = true;
                z11 = true;
            } else {
                r11 = 1;
                z11 = z15;
            }
            if (!z11) {
                ConstraintWidget.b bVar5 = this.O[0];
                ConstraintWidget.b bVar6 = ConstraintWidget.b.WRAP_CONTENT;
                if (bVar5 == bVar6 && max > 0 && Q() > max) {
                    this.f14810b1 = r11;
                    this.O[0] = ConstraintWidget.b.FIXED;
                    F0(max);
                    z10 = r11;
                    z11 = z10;
                }
                if (this.O[r11] == bVar6 && max2 > 0 && w() > max2) {
                    this.f14811c1 = r11;
                    this.O[r11] = ConstraintWidget.b.FIXED;
                    i0(max2);
                    z16 = true;
                    z15 = true;
                    i11 = i12;
                }
            }
            z16 = z10;
            z15 = z11;
            i11 = i12;
        }
        this.G0 = arrayList;
        if (z15) {
            ConstraintWidget.b[] bVarArr2 = this.O;
            bVarArr2[0] = bVar2;
            bVarArr2[1] = bVar;
        }
        b0(this.L0.v());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void P0(ConstraintWidget constraintWidget, int i10) {
        if (i10 == 0) {
            R0(constraintWidget);
        } else if (i10 == 1) {
            S0(constraintWidget);
        }
    }

    public boolean Q0(LinearSystem linearSystem) {
        f(linearSystem);
        int size = this.G0.size();
        boolean z10 = false;
        for (int i10 = 0; i10 < size; i10++) {
            ConstraintWidget constraintWidget = this.G0.get(i10);
            constraintWidget.p0(0, false);
            constraintWidget.p0(1, false);
            if (constraintWidget instanceof a) {
                z10 = true;
            }
        }
        if (z10) {
            for (int i11 = 0; i11 < size; i11++) {
                ConstraintWidget constraintWidget2 = this.G0.get(i11);
                if (constraintWidget2 instanceof a) {
                    ((a) constraintWidget2).O0();
                }
            }
        }
        for (int i12 = 0; i12 < size; i12++) {
            ConstraintWidget constraintWidget3 = this.G0.get(i12);
            if (constraintWidget3.e()) {
                constraintWidget3.f(linearSystem);
            }
        }
        for (int i13 = 0; i13 < size; i13++) {
            ConstraintWidget constraintWidget4 = this.G0.get(i13);
            if (constraintWidget4 instanceof ConstraintWidgetContainer) {
                ConstraintWidget.b[] bVarArr = constraintWidget4.O;
                ConstraintWidget.b bVar = bVarArr[0];
                ConstraintWidget.b bVar2 = bVarArr[1];
                ConstraintWidget.b bVar3 = ConstraintWidget.b.WRAP_CONTENT;
                if (bVar == bVar3) {
                    constraintWidget4.m0(ConstraintWidget.b.FIXED);
                }
                if (bVar2 == bVar3) {
                    constraintWidget4.B0(ConstraintWidget.b.FIXED);
                }
                constraintWidget4.f(linearSystem);
                if (bVar == bVar3) {
                    constraintWidget4.m0(bVar);
                }
                if (bVar2 == bVar3) {
                    constraintWidget4.B0(bVar2);
                }
            } else {
                Optimizer.a(this, linearSystem, constraintWidget4);
                if (!constraintWidget4.e()) {
                    constraintWidget4.f(linearSystem);
                }
            }
        }
        if (this.Q0 > 0) {
            b.a(this, linearSystem, 0);
        }
        if (this.R0 > 0) {
            b.a(this, linearSystem, 1);
        }
        return true;
    }

    public boolean T0(boolean z10) {
        return this.I0.f(z10);
    }

    public boolean U0(boolean z10) {
        return this.I0.g(z10);
    }

    public boolean V0(boolean z10, int i10) {
        return this.I0.h(z10, i10);
    }

    public BasicMeasure.b W0() {
        return this.J0;
    }

    public int X0() {
        return this.Z0;
    }

    public boolean Y0() {
        return false;
    }

    @Override // m.WidgetContainer, m.ConstraintWidget
    public void Z() {
        this.L0.D();
        this.M0 = 0;
        this.O0 = 0;
        this.N0 = 0;
        this.P0 = 0;
        this.f14809a1 = false;
        super.Z();
    }

    public void Z0() {
        this.I0.j();
    }

    public void a1() {
        this.I0.k();
    }

    public boolean b1() {
        return this.f14811c1;
    }

    public boolean c1() {
        return this.K0;
    }

    public boolean d1() {
        return this.f14810b1;
    }

    public long e1(int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18) {
        this.M0 = i17;
        this.N0 = i18;
        return this.H0.d(this, i10, i17, i18, i11, i12, i13, i14, i15, i16);
    }

    public boolean f1(int i10) {
        return (this.Z0 & i10) == i10;
    }

    public void h1(BasicMeasure.b bVar) {
        this.J0 = bVar;
        this.I0.n(bVar);
    }

    public void i1(int i10) {
        this.Z0 = i10;
        LinearSystem.f14500r = Optimizer.b(i10, 256);
    }

    public void j1(boolean z10) {
        this.K0 = z10;
    }

    public void k1(LinearSystem linearSystem, boolean[] zArr) {
        zArr[2] = false;
        K0(linearSystem);
        int size = this.G0.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.G0.get(i10).K0(linearSystem);
        }
    }

    public void l1() {
        this.H0.e(this);
    }
}
