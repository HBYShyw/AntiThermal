package m;

import java.util.HashMap;
import l.LinearSystem;
import l.SolverVariable;
import m.ConstraintAnchor;
import m.ConstraintWidget;

/* compiled from: Guideline.java */
/* loaded from: classes.dex */
public class h extends ConstraintWidget {
    protected float G0 = -1.0f;
    protected int H0 = -1;
    protected int I0 = -1;
    private ConstraintAnchor J0 = this.E;
    private int K0 = 0;
    private int L0 = 0;

    /* compiled from: Guideline.java */
    /* loaded from: classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f14851a;

        static {
            int[] iArr = new int[ConstraintAnchor.b.values().length];
            f14851a = iArr;
            try {
                iArr[ConstraintAnchor.b.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f14851a[ConstraintAnchor.b.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f14851a[ConstraintAnchor.b.TOP.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f14851a[ConstraintAnchor.b.BOTTOM.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f14851a[ConstraintAnchor.b.BASELINE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f14851a[ConstraintAnchor.b.CENTER.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f14851a[ConstraintAnchor.b.CENTER_X.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f14851a[ConstraintAnchor.b.CENTER_Y.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f14851a[ConstraintAnchor.b.NONE.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    public h() {
        this.M.clear();
        this.M.add(this.J0);
        int length = this.L.length;
        for (int i10 = 0; i10 < length; i10++) {
            this.L[i10] = this.J0;
        }
    }

    @Override // m.ConstraintWidget
    public void K0(LinearSystem linearSystem) {
        if (H() == null) {
            return;
        }
        int x10 = linearSystem.x(this.J0);
        if (this.K0 == 1) {
            G0(x10);
            H0(0);
            i0(H().w());
            F0(0);
            return;
        }
        G0(0);
        H0(x10);
        F0(H().Q());
        i0(0);
    }

    public int L0() {
        return this.K0;
    }

    public int M0() {
        return this.H0;
    }

    public int N0() {
        return this.I0;
    }

    public float O0() {
        return this.G0;
    }

    public void P0(int i10) {
        if (i10 > -1) {
            this.G0 = -1.0f;
            this.H0 = i10;
            this.I0 = -1;
        }
    }

    public void Q0(int i10) {
        if (i10 > -1) {
            this.G0 = -1.0f;
            this.H0 = -1;
            this.I0 = i10;
        }
    }

    public void R0(float f10) {
        if (f10 > -1.0f) {
            this.G0 = f10;
            this.H0 = -1;
            this.I0 = -1;
        }
    }

    public void S0(int i10) {
        if (this.K0 == i10) {
            return;
        }
        this.K0 = i10;
        this.M.clear();
        if (this.K0 == 1) {
            this.J0 = this.D;
        } else {
            this.J0 = this.E;
        }
        this.M.add(this.J0);
        int length = this.L.length;
        for (int i11 = 0; i11 < length; i11++) {
            this.L[i11] = this.J0;
        }
    }

    @Override // m.ConstraintWidget
    public void f(LinearSystem linearSystem) {
        ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer) H();
        if (constraintWidgetContainer == null) {
            return;
        }
        ConstraintAnchor n10 = constraintWidgetContainer.n(ConstraintAnchor.b.LEFT);
        ConstraintAnchor n11 = constraintWidgetContainer.n(ConstraintAnchor.b.RIGHT);
        ConstraintWidget constraintWidget = this.P;
        boolean z10 = constraintWidget != null && constraintWidget.O[0] == ConstraintWidget.b.WRAP_CONTENT;
        if (this.K0 == 0) {
            n10 = constraintWidgetContainer.n(ConstraintAnchor.b.TOP);
            n11 = constraintWidgetContainer.n(ConstraintAnchor.b.BOTTOM);
            ConstraintWidget constraintWidget2 = this.P;
            z10 = constraintWidget2 != null && constraintWidget2.O[1] == ConstraintWidget.b.WRAP_CONTENT;
        }
        if (this.H0 != -1) {
            SolverVariable q10 = linearSystem.q(this.J0);
            linearSystem.e(q10, linearSystem.q(n10), this.H0, 8);
            if (z10) {
                linearSystem.h(linearSystem.q(n11), q10, 0, 5);
                return;
            }
            return;
        }
        if (this.I0 == -1) {
            if (this.G0 != -1.0f) {
                linearSystem.d(LinearSystem.s(linearSystem, linearSystem.q(this.J0), linearSystem.q(n11), this.G0));
                return;
            }
            return;
        }
        SolverVariable q11 = linearSystem.q(this.J0);
        SolverVariable q12 = linearSystem.q(n11);
        linearSystem.e(q11, q12, -this.I0, 8);
        if (z10) {
            linearSystem.h(q11, linearSystem.q(n10), 0, 5);
            linearSystem.h(q12, q11, 0, 5);
        }
    }

    @Override // m.ConstraintWidget
    public boolean g() {
        return true;
    }

    @Override // m.ConstraintWidget
    public void l(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.l(constraintWidget, hashMap);
        h hVar = (h) constraintWidget;
        this.G0 = hVar.G0;
        this.H0 = hVar.H0;
        this.I0 = hVar.I0;
        S0(hVar.K0);
    }

    @Override // m.ConstraintWidget
    public ConstraintAnchor n(ConstraintAnchor.b bVar) {
        switch (a.f14851a[bVar.ordinal()]) {
            case 1:
            case 2:
                if (this.K0 == 1) {
                    return this.J0;
                }
                break;
            case 3:
            case 4:
                if (this.K0 == 0) {
                    return this.J0;
                }
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return null;
        }
        throw new AssertionError(bVar.name());
    }
}
