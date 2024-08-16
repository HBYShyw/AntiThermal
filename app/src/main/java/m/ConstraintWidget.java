package m;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import l.LinearSystem;
import l.SolverVariable;
import m.ConstraintAnchor;
import n.ChainRun;
import n.DependencyNode;
import n.HorizontalWidgetRun;
import n.VerticalWidgetRun;
import n.WidgetRun;

/* compiled from: ConstraintWidget.java */
/* renamed from: m.e, reason: use source file name */
/* loaded from: classes.dex */
public class ConstraintWidget {
    public static float F0 = 0.5f;
    public float[] A0;
    private boolean B;
    protected ConstraintWidget[] B0;
    protected ConstraintWidget[] C0;
    ConstraintWidget D0;
    ConstraintWidget E0;
    ConstraintAnchor K;
    public ConstraintAnchor[] L;
    protected ArrayList<ConstraintAnchor> M;
    private boolean[] N;
    public b[] O;
    public ConstraintWidget P;
    int Q;
    int R;
    public float S;
    protected int T;
    protected int U;
    protected int V;
    int W;
    int X;
    protected int Y;
    protected int Z;

    /* renamed from: a0, reason: collision with root package name */
    int f14751a0;

    /* renamed from: b0, reason: collision with root package name */
    protected int f14753b0;

    /* renamed from: c, reason: collision with root package name */
    public ChainRun f14754c;

    /* renamed from: c0, reason: collision with root package name */
    protected int f14755c0;

    /* renamed from: d, reason: collision with root package name */
    public ChainRun f14756d;

    /* renamed from: d0, reason: collision with root package name */
    float f14757d0;

    /* renamed from: e0, reason: collision with root package name */
    float f14759e0;

    /* renamed from: f0, reason: collision with root package name */
    private Object f14761f0;

    /* renamed from: g0, reason: collision with root package name */
    private int f14763g0;

    /* renamed from: h0, reason: collision with root package name */
    private int f14765h0;

    /* renamed from: i0, reason: collision with root package name */
    private String f14767i0;

    /* renamed from: j0, reason: collision with root package name */
    private String f14769j0;

    /* renamed from: k0, reason: collision with root package name */
    int f14771k0;

    /* renamed from: l0, reason: collision with root package name */
    int f14773l0;

    /* renamed from: m0, reason: collision with root package name */
    int f14775m0;

    /* renamed from: n0, reason: collision with root package name */
    int f14777n0;

    /* renamed from: o0, reason: collision with root package name */
    boolean f14779o0;

    /* renamed from: p0, reason: collision with root package name */
    boolean f14781p0;

    /* renamed from: q0, reason: collision with root package name */
    boolean f14783q0;

    /* renamed from: r0, reason: collision with root package name */
    boolean f14785r0;

    /* renamed from: s0, reason: collision with root package name */
    boolean f14787s0;

    /* renamed from: t0, reason: collision with root package name */
    boolean f14789t0;

    /* renamed from: u, reason: collision with root package name */
    public boolean f14790u;

    /* renamed from: u0, reason: collision with root package name */
    boolean f14791u0;

    /* renamed from: v, reason: collision with root package name */
    public boolean f14792v;

    /* renamed from: v0, reason: collision with root package name */
    boolean f14793v0;

    /* renamed from: w0, reason: collision with root package name */
    int f14795w0;

    /* renamed from: x0, reason: collision with root package name */
    int f14797x0;

    /* renamed from: y0, reason: collision with root package name */
    boolean f14799y0;

    /* renamed from: z0, reason: collision with root package name */
    boolean f14801z0;

    /* renamed from: a, reason: collision with root package name */
    public boolean f14750a = false;

    /* renamed from: b, reason: collision with root package name */
    public WidgetRun[] f14752b = new WidgetRun[2];

    /* renamed from: e, reason: collision with root package name */
    public HorizontalWidgetRun f14758e = new HorizontalWidgetRun(this);

    /* renamed from: f, reason: collision with root package name */
    public VerticalWidgetRun f14760f = new VerticalWidgetRun(this);

    /* renamed from: g, reason: collision with root package name */
    public boolean[] f14762g = {true, true};

    /* renamed from: h, reason: collision with root package name */
    public int[] f14764h = {0, 0, 0, 0};

    /* renamed from: i, reason: collision with root package name */
    boolean f14766i = false;

    /* renamed from: j, reason: collision with root package name */
    public int f14768j = -1;

    /* renamed from: k, reason: collision with root package name */
    public int f14770k = -1;

    /* renamed from: l, reason: collision with root package name */
    public int f14772l = 0;

    /* renamed from: m, reason: collision with root package name */
    public int f14774m = 0;

    /* renamed from: n, reason: collision with root package name */
    public int[] f14776n = new int[2];

    /* renamed from: o, reason: collision with root package name */
    public int f14778o = 0;

    /* renamed from: p, reason: collision with root package name */
    public int f14780p = 0;

    /* renamed from: q, reason: collision with root package name */
    public float f14782q = 1.0f;

    /* renamed from: r, reason: collision with root package name */
    public int f14784r = 0;

    /* renamed from: s, reason: collision with root package name */
    public int f14786s = 0;

    /* renamed from: t, reason: collision with root package name */
    public float f14788t = 1.0f;

    /* renamed from: w, reason: collision with root package name */
    int f14794w = -1;

    /* renamed from: x, reason: collision with root package name */
    float f14796x = 1.0f;

    /* renamed from: y, reason: collision with root package name */
    private int[] f14798y = {Integer.MAX_VALUE, Integer.MAX_VALUE};

    /* renamed from: z, reason: collision with root package name */
    private float f14800z = 0.0f;
    private boolean A = false;
    private boolean C = false;
    public ConstraintAnchor D = new ConstraintAnchor(this, ConstraintAnchor.b.LEFT);
    public ConstraintAnchor E = new ConstraintAnchor(this, ConstraintAnchor.b.TOP);
    public ConstraintAnchor F = new ConstraintAnchor(this, ConstraintAnchor.b.RIGHT);
    public ConstraintAnchor G = new ConstraintAnchor(this, ConstraintAnchor.b.BOTTOM);
    ConstraintAnchor H = new ConstraintAnchor(this, ConstraintAnchor.b.BASELINE);
    ConstraintAnchor I = new ConstraintAnchor(this, ConstraintAnchor.b.CENTER_X);
    ConstraintAnchor J = new ConstraintAnchor(this, ConstraintAnchor.b.CENTER_Y);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ConstraintWidget.java */
    /* renamed from: m.e$a */
    /* loaded from: classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f14802a;

        /* renamed from: b, reason: collision with root package name */
        static final /* synthetic */ int[] f14803b;

        static {
            int[] iArr = new int[b.values().length];
            f14803b = iArr;
            try {
                iArr[b.FIXED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f14803b[b.WRAP_CONTENT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f14803b[b.MATCH_PARENT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f14803b[b.MATCH_CONSTRAINT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            int[] iArr2 = new int[ConstraintAnchor.b.values().length];
            f14802a = iArr2;
            try {
                iArr2[ConstraintAnchor.b.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f14802a[ConstraintAnchor.b.TOP.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f14802a[ConstraintAnchor.b.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f14802a[ConstraintAnchor.b.BOTTOM.ordinal()] = 4;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f14802a[ConstraintAnchor.b.BASELINE.ordinal()] = 5;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                f14802a[ConstraintAnchor.b.CENTER.ordinal()] = 6;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                f14802a[ConstraintAnchor.b.CENTER_X.ordinal()] = 7;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                f14802a[ConstraintAnchor.b.CENTER_Y.ordinal()] = 8;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                f14802a[ConstraintAnchor.b.NONE.ordinal()] = 9;
            } catch (NoSuchFieldError unused13) {
            }
        }
    }

    /* compiled from: ConstraintWidget.java */
    /* renamed from: m.e$b */
    /* loaded from: classes.dex */
    public enum b {
        FIXED,
        WRAP_CONTENT,
        MATCH_CONSTRAINT,
        MATCH_PARENT
    }

    public ConstraintWidget() {
        ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.b.CENTER);
        this.K = constraintAnchor;
        this.L = new ConstraintAnchor[]{this.D, this.F, this.E, this.G, this.H, constraintAnchor};
        this.M = new ArrayList<>();
        this.N = new boolean[2];
        b bVar = b.FIXED;
        this.O = new b[]{bVar, bVar};
        this.P = null;
        this.Q = 0;
        this.R = 0;
        this.S = 0.0f;
        this.T = -1;
        this.U = 0;
        this.V = 0;
        this.W = 0;
        this.X = 0;
        this.Y = 0;
        this.Z = 0;
        this.f14751a0 = 0;
        float f10 = F0;
        this.f14757d0 = f10;
        this.f14759e0 = f10;
        this.f14763g0 = 0;
        this.f14765h0 = 0;
        this.f14767i0 = null;
        this.f14769j0 = null;
        this.f14791u0 = false;
        this.f14793v0 = false;
        this.f14795w0 = 0;
        this.f14797x0 = 0;
        this.A0 = new float[]{-1.0f, -1.0f};
        this.B0 = new ConstraintWidget[]{null, null};
        this.C0 = new ConstraintWidget[]{null, null};
        this.D0 = null;
        this.E0 = null;
        d();
    }

    private boolean V(int i10) {
        int i11 = i10 * 2;
        ConstraintAnchor[] constraintAnchorArr = this.L;
        if (constraintAnchorArr[i11].f14735d != null && constraintAnchorArr[i11].f14735d.f14735d != constraintAnchorArr[i11]) {
            int i12 = i11 + 1;
            if (constraintAnchorArr[i12].f14735d != null && constraintAnchorArr[i12].f14735d.f14735d == constraintAnchorArr[i12]) {
                return true;
            }
        }
        return false;
    }

    private void d() {
        this.M.add(this.D);
        this.M.add(this.E);
        this.M.add(this.F);
        this.M.add(this.G);
        this.M.add(this.I);
        this.M.add(this.J);
        this.M.add(this.K);
        this.M.add(this.H);
    }

    /* JADX WARN: Code restructure failed: missing block: B:58:0x0468, code lost:
    
        if (r0[r22] == r1) goto L296;
     */
    /* JADX WARN: Removed duplicated region for block: B:108:0x02ea A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x02f9  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0343 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0344  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x032f  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0286  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x028a  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x0429  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x00ce  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:292:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x01c1  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0437 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:69:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void h(LinearSystem linearSystem, boolean z10, boolean z11, boolean z12, boolean z13, SolverVariable solverVariable, SolverVariable solverVariable2, b bVar, boolean z14, ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int i10, int i11, int i12, int i13, float f10, boolean z15, boolean z16, boolean z17, boolean z18, int i14, int i15, int i16, int i17, float f11, boolean z19) {
        int i18;
        boolean z20;
        int i19;
        SolverVariable solverVariable3;
        int i20;
        int i21;
        int i22;
        SolverVariable solverVariable4;
        SolverVariable solverVariable5;
        SolverVariable solverVariable6;
        int i23;
        int i24;
        boolean z21;
        boolean z22;
        SolverVariable q10;
        SolverVariable q11;
        SolverVariable solverVariable7;
        SolverVariable solverVariable8;
        SolverVariable solverVariable9;
        SolverVariable solverVariable10;
        int i25;
        int i26;
        char c10;
        int i27;
        ConstraintAnchor constraintAnchor3;
        boolean z23;
        boolean z24;
        boolean z25;
        int i28;
        int i29;
        boolean z26;
        int i30;
        boolean z27;
        boolean z28;
        SolverVariable solverVariable11;
        ConstraintWidget constraintWidget;
        boolean z29;
        ConstraintWidget constraintWidget2;
        SolverVariable solverVariable12;
        int i31;
        int i32;
        int i33;
        ConstraintWidget constraintWidget3;
        SolverVariable solverVariable13;
        ConstraintWidget constraintWidget4;
        SolverVariable solverVariable14;
        int i34;
        int i35;
        boolean z30;
        int i36;
        int i37;
        boolean z31;
        int i38;
        int i39;
        int i40;
        int i41;
        boolean z32;
        SolverVariable solverVariable15;
        int i42;
        SolverVariable q12 = linearSystem.q(constraintAnchor);
        SolverVariable q13 = linearSystem.q(constraintAnchor2);
        SolverVariable q14 = linearSystem.q(constraintAnchor.g());
        SolverVariable q15 = linearSystem.q(constraintAnchor2.g());
        LinearSystem.w();
        boolean j10 = constraintAnchor.j();
        boolean j11 = constraintAnchor2.j();
        boolean j12 = this.K.j();
        int i43 = j11 ? (j10 ? 1 : 0) + 1 : j10 ? 1 : 0;
        if (j12) {
            i43++;
        }
        int i44 = z15 ? 3 : i14;
        int i45 = a.f14803b[bVar.ordinal()];
        if (i45 == 1 || i45 == 2 || i45 == 3 || i45 != 4) {
            i18 = i44;
        } else {
            i18 = i44;
            if (i18 != 4) {
                z20 = true;
                if (this.f14765h0 != 8) {
                    i19 = 0;
                    z20 = false;
                } else {
                    i19 = i11;
                }
                if (z19) {
                    solverVariable3 = q15;
                    i20 = 8;
                } else {
                    if (!j10 && !j11 && !j12) {
                        linearSystem.f(q12, i10);
                    } else if (j10 && !j11) {
                        solverVariable3 = q15;
                        i20 = 8;
                        linearSystem.e(q12, q14, constraintAnchor.c(), 8);
                    }
                    solverVariable3 = q15;
                    i20 = 8;
                }
                if (z20) {
                    if (z14) {
                        linearSystem.e(q13, q12, 0, 3);
                        if (i12 > 0) {
                            linearSystem.h(q13, q12, i12, 8);
                        }
                        if (i13 < Integer.MAX_VALUE) {
                            linearSystem.j(q13, q12, i13, 8);
                        }
                    } else {
                        linearSystem.e(q13, q12, i19, i20);
                    }
                    i24 = i16;
                    i21 = i17;
                    solverVariable4 = q14;
                    solverVariable5 = q13;
                } else {
                    if (i43 != 2 && !z15 && (i18 == 1 || i18 == 0)) {
                        int max = Math.max(i16, i19);
                        if (i17 > 0) {
                            max = Math.min(i17, max);
                        }
                        linearSystem.e(q13, q12, max, 8);
                        z22 = z13;
                        i24 = i16;
                        i21 = i17;
                        solverVariable4 = q14;
                        solverVariable5 = q13;
                        solverVariable6 = solverVariable3;
                        z21 = false;
                        i23 = i43;
                        if (z19) {
                        }
                        if (i27 >= i26) {
                            return;
                        } else {
                            return;
                        }
                    }
                    int i46 = i16 == -2 ? i19 : i16;
                    i21 = i17 == -2 ? i19 : i17;
                    if (i19 > 0 && i18 != 1) {
                        i19 = 0;
                    }
                    if (i46 > 0) {
                        linearSystem.h(q13, q12, i46, 8);
                        i19 = Math.max(i19, i46);
                    }
                    if (i21 > 0) {
                        if ((z11 && i18 == 1) ? false : true) {
                            i22 = 8;
                            linearSystem.j(q13, q12, i21, 8);
                        } else {
                            i22 = 8;
                        }
                        i19 = Math.min(i19, i21);
                    } else {
                        i22 = 8;
                    }
                    if (i18 != 1) {
                        if (i18 == 2) {
                            ConstraintAnchor.b h10 = constraintAnchor.h();
                            ConstraintAnchor.b bVar2 = ConstraintAnchor.b.TOP;
                            if (h10 != bVar2 && constraintAnchor.h() != ConstraintAnchor.b.BOTTOM) {
                                q10 = linearSystem.q(this.P.n(ConstraintAnchor.b.LEFT));
                                q11 = linearSystem.q(this.P.n(ConstraintAnchor.b.RIGHT));
                            } else {
                                q10 = linearSystem.q(this.P.n(bVar2));
                                q11 = linearSystem.q(this.P.n(ConstraintAnchor.b.BOTTOM));
                            }
                            int i47 = i46;
                            solverVariable6 = solverVariable3;
                            solverVariable4 = q14;
                            i23 = i43;
                            solverVariable5 = q13;
                            linearSystem.d(linearSystem.r().k(q13, q12, q11, q10, f11));
                            z22 = z13;
                            i24 = i47;
                            z21 = false;
                        } else {
                            solverVariable4 = q14;
                            solverVariable5 = q13;
                            int i48 = i46;
                            solverVariable6 = solverVariable3;
                            i23 = i43;
                            i24 = i48;
                            z21 = z20;
                            z22 = true;
                        }
                        if (z19) {
                            solverVariable7 = solverVariable2;
                            solverVariable8 = solverVariable5;
                            solverVariable9 = q12;
                            solverVariable10 = solverVariable;
                            i25 = 8;
                            i26 = 2;
                            c10 = 1;
                            i27 = i23;
                        } else {
                            if (!z16) {
                                if ((j10 || j11 || j12) && (!j10 || j11)) {
                                    if (!j10 && j11) {
                                        linearSystem.e(solverVariable5, solverVariable6, -constraintAnchor2.c(), 8);
                                        if (z11) {
                                            linearSystem.h(q12, solverVariable, 0, 5);
                                        }
                                    } else if (j10 && j11) {
                                        ConstraintWidget constraintWidget5 = constraintAnchor.f14735d.f14733b;
                                        ConstraintWidget constraintWidget6 = constraintAnchor2.f14735d.f14733b;
                                        ConstraintWidget H = H();
                                        int i49 = 6;
                                        if (z21) {
                                            if (i18 == 0) {
                                                if (i21 == 0 && i24 == 0) {
                                                    z32 = false;
                                                    i40 = 8;
                                                    i41 = 8;
                                                    z26 = true;
                                                } else {
                                                    z26 = false;
                                                    i40 = 5;
                                                    i41 = 5;
                                                    z32 = true;
                                                }
                                                if ((constraintWidget5 instanceof m.a) || (constraintWidget6 instanceof m.a)) {
                                                    i30 = i40;
                                                    i28 = 6;
                                                    z25 = z32;
                                                    z24 = false;
                                                    z23 = true;
                                                    i29 = 4;
                                                    if (z24 || solverVariable4 != solverVariable6 || constraintWidget5 == H) {
                                                        z27 = z24;
                                                        z28 = z23;
                                                    } else {
                                                        z27 = false;
                                                        z28 = false;
                                                    }
                                                    if (z25) {
                                                        solverVariable11 = solverVariable5;
                                                        constraintWidget = H;
                                                        z29 = z23;
                                                        constraintWidget2 = constraintWidget5;
                                                        solverVariable12 = q12;
                                                        i31 = i18;
                                                        i32 = 8;
                                                        i33 = 4;
                                                        constraintWidget3 = constraintWidget6;
                                                    } else {
                                                        solverVariable11 = solverVariable5;
                                                        i33 = 4;
                                                        z29 = true;
                                                        constraintWidget = H;
                                                        i31 = i18;
                                                        constraintWidget3 = constraintWidget6;
                                                        i32 = 8;
                                                        constraintWidget2 = constraintWidget5;
                                                        solverVariable12 = q12;
                                                        linearSystem.c(q12, solverVariable4, constraintAnchor.c(), f10, solverVariable6, solverVariable11, constraintAnchor2.c(), this.f14765h0 == 8 ? 4 : i28);
                                                    }
                                                    if (this.f14765h0 != i32) {
                                                        return;
                                                    }
                                                    if (z27) {
                                                        if (!z11 || solverVariable4 == solverVariable6 || z21) {
                                                            constraintWidget4 = constraintWidget2;
                                                        } else {
                                                            constraintWidget4 = constraintWidget2;
                                                            if ((constraintWidget4 instanceof m.a) || (constraintWidget3 instanceof m.a)) {
                                                                i37 = 6;
                                                                solverVariable14 = solverVariable12;
                                                                linearSystem.h(solverVariable14, solverVariable4, constraintAnchor.c(), i37);
                                                                solverVariable13 = solverVariable11;
                                                                linearSystem.j(solverVariable13, solverVariable6, -constraintAnchor2.c(), i37);
                                                                i30 = i37;
                                                            }
                                                        }
                                                        i37 = i30;
                                                        solverVariable14 = solverVariable12;
                                                        linearSystem.h(solverVariable14, solverVariable4, constraintAnchor.c(), i37);
                                                        solverVariable13 = solverVariable11;
                                                        linearSystem.j(solverVariable13, solverVariable6, -constraintAnchor2.c(), i37);
                                                        i30 = i37;
                                                    } else {
                                                        solverVariable13 = solverVariable11;
                                                        constraintWidget4 = constraintWidget2;
                                                        solverVariable14 = solverVariable12;
                                                    }
                                                    if (!z11 || !z18 || (constraintWidget4 instanceof m.a) || (constraintWidget3 instanceof m.a)) {
                                                        i34 = i29;
                                                        i35 = i30;
                                                        z30 = z28;
                                                    } else {
                                                        i34 = 6;
                                                        i35 = 6;
                                                        z30 = z29;
                                                    }
                                                    if (z30) {
                                                        if (z26 && (!z17 || z12)) {
                                                            if (constraintWidget4 != constraintWidget && constraintWidget3 != constraintWidget) {
                                                                i49 = i34;
                                                            }
                                                            if ((constraintWidget4 instanceof h) || (constraintWidget3 instanceof h)) {
                                                                i49 = 5;
                                                            }
                                                            if ((constraintWidget4 instanceof m.a) || (constraintWidget3 instanceof m.a)) {
                                                                i49 = 5;
                                                            }
                                                            i34 = Math.max(z17 ? 5 : i49, i34);
                                                        }
                                                        int i50 = i34;
                                                        if (z11) {
                                                            i50 = Math.min(i35, i50);
                                                            if (z15 && !z17 && (constraintWidget4 == constraintWidget || constraintWidget3 == constraintWidget)) {
                                                                i50 = i33;
                                                            }
                                                        }
                                                        linearSystem.e(solverVariable14, solverVariable4, constraintAnchor.c(), i50);
                                                        linearSystem.e(solverVariable13, solverVariable6, -constraintAnchor2.c(), i50);
                                                    }
                                                    if (z11) {
                                                        i36 = i32;
                                                        int c11 = solverVariable == solverVariable4 ? constraintAnchor.c() : 0;
                                                        if (solverVariable4 != solverVariable) {
                                                            linearSystem.h(solverVariable14, solverVariable, c11, 5);
                                                        }
                                                    } else {
                                                        i36 = i32;
                                                    }
                                                    if (z11 && z21 && i12 == 0 && i24 == 0) {
                                                        if (z21 && i31 == 3) {
                                                            linearSystem.h(solverVariable13, solverVariable14, 0, i36);
                                                        } else {
                                                            linearSystem.h(solverVariable13, solverVariable14, 0, 5);
                                                        }
                                                    }
                                                } else {
                                                    i30 = i40;
                                                    i28 = 6;
                                                    z25 = z32;
                                                    z24 = false;
                                                    z23 = true;
                                                    i29 = i41;
                                                    if (z24) {
                                                    }
                                                    z27 = z24;
                                                    z28 = z23;
                                                    if (z25) {
                                                    }
                                                    if (this.f14765h0 != i32) {
                                                    }
                                                }
                                            } else {
                                                if (i18 == 1) {
                                                    i30 = 8;
                                                    i28 = 6;
                                                    z24 = true;
                                                    z23 = true;
                                                    i29 = 4;
                                                    z26 = false;
                                                    z25 = true;
                                                } else if (i18 != 3) {
                                                    z23 = true;
                                                    i28 = 6;
                                                    z24 = false;
                                                    i29 = 4;
                                                    z26 = false;
                                                    z25 = false;
                                                } else if (this.f14794w == -1) {
                                                    i28 = z17 ? z11 ? 5 : 4 : 8;
                                                    z24 = true;
                                                    z23 = true;
                                                    i29 = 5;
                                                    z26 = true;
                                                    z25 = true;
                                                    i30 = 8;
                                                } else if (z15) {
                                                    if (i15 != 2) {
                                                        z23 = true;
                                                        if (i15 != 1) {
                                                            z31 = false;
                                                            if (z31) {
                                                                i38 = 8;
                                                                i39 = 5;
                                                            } else {
                                                                i38 = 5;
                                                                i39 = 4;
                                                            }
                                                            i30 = i38;
                                                            i29 = i39;
                                                            z24 = z23;
                                                            z26 = z24;
                                                            z25 = z26;
                                                            i28 = 6;
                                                        }
                                                    } else {
                                                        z23 = true;
                                                    }
                                                    z31 = z23;
                                                    if (z31) {
                                                    }
                                                    i30 = i38;
                                                    i29 = i39;
                                                    z24 = z23;
                                                    z26 = z24;
                                                    z25 = z26;
                                                    i28 = 6;
                                                } else {
                                                    z23 = true;
                                                    if (i21 > 0) {
                                                        z24 = true;
                                                        z26 = true;
                                                        z25 = true;
                                                        i28 = 6;
                                                        i29 = 5;
                                                    } else if (i21 != 0 || i24 != 0) {
                                                        z24 = true;
                                                        z26 = true;
                                                        z25 = true;
                                                        i28 = 6;
                                                        i29 = 4;
                                                    } else if (z17) {
                                                        i30 = (constraintWidget5 == H || constraintWidget6 == H) ? 5 : 4;
                                                        z24 = true;
                                                        z26 = true;
                                                        z25 = true;
                                                        i28 = 6;
                                                        i29 = 4;
                                                    } else {
                                                        z24 = true;
                                                        z26 = true;
                                                        z25 = true;
                                                        i28 = 6;
                                                        i29 = 8;
                                                    }
                                                }
                                                if (z24) {
                                                }
                                                z27 = z24;
                                                z28 = z23;
                                                if (z25) {
                                                }
                                                if (this.f14765h0 != i32) {
                                                }
                                            }
                                            if (z11 || !z22) {
                                                return;
                                            }
                                            if (constraintAnchor2.f14735d != null) {
                                                i42 = constraintAnchor2.c();
                                                solverVariable15 = solverVariable2;
                                            } else {
                                                solverVariable15 = solverVariable2;
                                                i42 = 0;
                                            }
                                            if (solverVariable6 != solverVariable15) {
                                                linearSystem.h(solverVariable15, solverVariable13, i42, 5);
                                                return;
                                            }
                                            return;
                                        }
                                        z23 = true;
                                        z24 = true;
                                        z25 = true;
                                        i28 = 6;
                                        i29 = 4;
                                        z26 = false;
                                        i30 = 5;
                                        if (z24) {
                                        }
                                        z27 = z24;
                                        z28 = z23;
                                        if (z25) {
                                        }
                                        if (this.f14765h0 != i32) {
                                        }
                                    }
                                }
                                solverVariable13 = solverVariable5;
                                if (z11) {
                                    return;
                                } else {
                                    return;
                                }
                            }
                            solverVariable7 = solverVariable2;
                            solverVariable8 = solverVariable5;
                            solverVariable9 = q12;
                            solverVariable10 = solverVariable;
                            i27 = i23;
                            i25 = 8;
                            i26 = 2;
                            c10 = 1;
                        }
                        if (i27 >= i26 && z11 && z22) {
                            linearSystem.h(solverVariable9, solverVariable10, 0, i25);
                            char c12 = (z10 || this.H.f14735d == null) ? c10 : (char) 0;
                            if (z10 || (constraintAnchor3 = this.H.f14735d) == null) {
                                c10 = c12;
                            } else {
                                ConstraintWidget constraintWidget7 = constraintAnchor3.f14733b;
                                if (constraintWidget7.S != 0.0f) {
                                    b[] bVarArr = constraintWidget7.O;
                                    b bVar3 = bVarArr[0];
                                    b bVar4 = b.MATCH_CONSTRAINT;
                                    if (bVar3 == bVar4) {
                                    }
                                }
                                c10 = 0;
                            }
                            if (c10 != 0) {
                                linearSystem.h(solverVariable7, solverVariable8, 0, i25);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    if (z11) {
                        linearSystem.e(q13, q12, i19, i22);
                    } else if (z16) {
                        linearSystem.e(q13, q12, i19, 5);
                        linearSystem.j(q13, q12, i19, i22);
                    } else {
                        linearSystem.e(q13, q12, i19, 5);
                        linearSystem.j(q13, q12, i19, i22);
                    }
                    solverVariable4 = q14;
                    solverVariable5 = q13;
                    i24 = i46;
                }
                z21 = z20;
                solverVariable6 = solverVariable3;
                z22 = z13;
                i23 = i43;
                if (z19) {
                }
                if (i27 >= i26) {
                }
            }
        }
        z20 = false;
        if (this.f14765h0 != 8) {
        }
        if (z19) {
        }
        if (z20) {
        }
        z21 = z20;
        solverVariable6 = solverVariable3;
        z22 = z13;
        i23 = i43;
        if (z19) {
        }
        if (i27 >= i26) {
        }
    }

    public int A() {
        ConstraintAnchor constraintAnchor = this.D;
        int i10 = constraintAnchor != null ? 0 + constraintAnchor.f14736e : 0;
        ConstraintAnchor constraintAnchor2 = this.F;
        return constraintAnchor2 != null ? i10 + constraintAnchor2.f14736e : i10;
    }

    public void A0(int i10, int i11) {
        this.V = i10;
        int i12 = i11 - i10;
        this.R = i12;
        int i13 = this.f14755c0;
        if (i12 < i13) {
            this.R = i13;
        }
    }

    public int B(int i10) {
        if (i10 == 0) {
            return Q();
        }
        if (i10 == 1) {
            return w();
        }
        return 0;
    }

    public void B0(b bVar) {
        this.O[1] = bVar;
    }

    public int C() {
        return this.f14798y[1];
    }

    public void C0(int i10, int i11, int i12, float f10) {
        this.f14774m = i10;
        this.f14784r = i11;
        if (i12 == Integer.MAX_VALUE) {
            i12 = 0;
        }
        this.f14786s = i12;
        this.f14788t = f10;
        if (f10 <= 0.0f || f10 >= 1.0f || i10 != 0) {
            return;
        }
        this.f14774m = 2;
    }

    public int D() {
        return this.f14798y[0];
    }

    public void D0(float f10) {
        this.A0[1] = f10;
    }

    public int E() {
        return this.f14755c0;
    }

    public void E0(int i10) {
        this.f14765h0 = i10;
    }

    public int F() {
        return this.f14753b0;
    }

    public void F0(int i10) {
        this.Q = i10;
        int i11 = this.f14753b0;
        if (i10 < i11) {
            this.Q = i11;
        }
    }

    public ConstraintWidget G(int i10) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (i10 != 0) {
            if (i10 == 1 && (constraintAnchor2 = (constraintAnchor = this.G).f14735d) != null && constraintAnchor2.f14735d == constraintAnchor) {
                return constraintAnchor2.f14733b;
            }
            return null;
        }
        ConstraintAnchor constraintAnchor3 = this.F;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.f14735d;
        if (constraintAnchor4 == null || constraintAnchor4.f14735d != constraintAnchor3) {
            return null;
        }
        return constraintAnchor4.f14733b;
    }

    public void G0(int i10) {
        this.U = i10;
    }

    public ConstraintWidget H() {
        return this.P;
    }

    public void H0(int i10) {
        this.V = i10;
    }

    public ConstraintWidget I(int i10) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (i10 != 0) {
            if (i10 == 1 && (constraintAnchor2 = (constraintAnchor = this.E).f14735d) != null && constraintAnchor2.f14735d == constraintAnchor) {
                return constraintAnchor2.f14733b;
            }
            return null;
        }
        ConstraintAnchor constraintAnchor3 = this.D;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.f14735d;
        if (constraintAnchor4 == null || constraintAnchor4.f14735d != constraintAnchor3) {
            return null;
        }
        return constraintAnchor4.f14733b;
    }

    public void I0(boolean z10, boolean z11, boolean z12, boolean z13) {
        if (this.f14794w == -1) {
            if (z12 && !z13) {
                this.f14794w = 0;
            } else if (!z12 && z13) {
                this.f14794w = 1;
                if (this.T == -1) {
                    this.f14796x = 1.0f / this.f14796x;
                }
            }
        }
        if (this.f14794w == 0 && (!this.E.j() || !this.G.j())) {
            this.f14794w = 1;
        } else if (this.f14794w == 1 && (!this.D.j() || !this.F.j())) {
            this.f14794w = 0;
        }
        if (this.f14794w == -1 && (!this.E.j() || !this.G.j() || !this.D.j() || !this.F.j())) {
            if (this.E.j() && this.G.j()) {
                this.f14794w = 0;
            } else if (this.D.j() && this.F.j()) {
                this.f14796x = 1.0f / this.f14796x;
                this.f14794w = 1;
            }
        }
        if (this.f14794w == -1) {
            int i10 = this.f14778o;
            if (i10 > 0 && this.f14784r == 0) {
                this.f14794w = 0;
            } else {
                if (i10 != 0 || this.f14784r <= 0) {
                    return;
                }
                this.f14796x = 1.0f / this.f14796x;
                this.f14794w = 1;
            }
        }
    }

    public int J() {
        return R() + this.Q;
    }

    public void J0(boolean z10, boolean z11) {
        int i10;
        int i11;
        boolean k10 = z10 & this.f14758e.k();
        boolean k11 = z11 & this.f14760f.k();
        HorizontalWidgetRun horizontalWidgetRun = this.f14758e;
        int i12 = horizontalWidgetRun.f15612h.f15576g;
        VerticalWidgetRun verticalWidgetRun = this.f14760f;
        int i13 = verticalWidgetRun.f15612h.f15576g;
        int i14 = horizontalWidgetRun.f15613i.f15576g;
        int i15 = verticalWidgetRun.f15613i.f15576g;
        int i16 = i15 - i13;
        if (i14 - i12 < 0 || i16 < 0 || i12 == Integer.MIN_VALUE || i12 == Integer.MAX_VALUE || i13 == Integer.MIN_VALUE || i13 == Integer.MAX_VALUE || i14 == Integer.MIN_VALUE || i14 == Integer.MAX_VALUE || i15 == Integer.MIN_VALUE || i15 == Integer.MAX_VALUE) {
            i14 = 0;
            i12 = 0;
            i15 = 0;
            i13 = 0;
        }
        int i17 = i14 - i12;
        int i18 = i15 - i13;
        if (k10) {
            this.U = i12;
        }
        if (k11) {
            this.V = i13;
        }
        if (this.f14765h0 == 8) {
            this.Q = 0;
            this.R = 0;
            return;
        }
        if (k10) {
            if (this.O[0] == b.FIXED && i17 < (i11 = this.Q)) {
                i17 = i11;
            }
            this.Q = i17;
            int i19 = this.f14753b0;
            if (i17 < i19) {
                this.Q = i19;
            }
        }
        if (k11) {
            if (this.O[1] == b.FIXED && i18 < (i10 = this.R)) {
                i18 = i10;
            }
            this.R = i18;
            int i20 = this.f14755c0;
            if (i18 < i20) {
                this.R = i20;
            }
        }
    }

    public WidgetRun K(int i10) {
        if (i10 == 0) {
            return this.f14758e;
        }
        if (i10 == 1) {
            return this.f14760f;
        }
        return null;
    }

    public void K0(LinearSystem linearSystem) {
        int x10 = linearSystem.x(this.D);
        int x11 = linearSystem.x(this.E);
        int x12 = linearSystem.x(this.F);
        int x13 = linearSystem.x(this.G);
        HorizontalWidgetRun horizontalWidgetRun = this.f14758e;
        DependencyNode dependencyNode = horizontalWidgetRun.f15612h;
        if (dependencyNode.f15579j) {
            DependencyNode dependencyNode2 = horizontalWidgetRun.f15613i;
            if (dependencyNode2.f15579j) {
                x10 = dependencyNode.f15576g;
                x12 = dependencyNode2.f15576g;
            }
        }
        VerticalWidgetRun verticalWidgetRun = this.f14760f;
        DependencyNode dependencyNode3 = verticalWidgetRun.f15612h;
        if (dependencyNode3.f15579j) {
            DependencyNode dependencyNode4 = verticalWidgetRun.f15613i;
            if (dependencyNode4.f15579j) {
                x11 = dependencyNode3.f15576g;
                x13 = dependencyNode4.f15576g;
            }
        }
        int i10 = x13 - x11;
        if (x12 - x10 < 0 || i10 < 0 || x10 == Integer.MIN_VALUE || x10 == Integer.MAX_VALUE || x11 == Integer.MIN_VALUE || x11 == Integer.MAX_VALUE || x12 == Integer.MIN_VALUE || x12 == Integer.MAX_VALUE || x13 == Integer.MIN_VALUE || x13 == Integer.MAX_VALUE) {
            x13 = 0;
            x10 = 0;
            x11 = 0;
            x12 = 0;
        }
        g0(x10, x11, x12, x13);
    }

    public float L() {
        return this.f14759e0;
    }

    public int M() {
        return this.f14797x0;
    }

    public b N() {
        return this.O[1];
    }

    public int O() {
        int i10 = this.D != null ? 0 + this.E.f14736e : 0;
        return this.F != null ? i10 + this.G.f14736e : i10;
    }

    public int P() {
        return this.f14765h0;
    }

    public int Q() {
        if (this.f14765h0 == 8) {
            return 0;
        }
        return this.Q;
    }

    public int R() {
        ConstraintWidget constraintWidget = this.P;
        if (constraintWidget != null && (constraintWidget instanceof ConstraintWidgetContainer)) {
            return ((ConstraintWidgetContainer) constraintWidget).M0 + this.U;
        }
        return this.U;
    }

    public int S() {
        ConstraintWidget constraintWidget = this.P;
        if (constraintWidget != null && (constraintWidget instanceof ConstraintWidgetContainer)) {
            return ((ConstraintWidgetContainer) constraintWidget).N0 + this.V;
        }
        return this.V;
    }

    public boolean T() {
        return this.A;
    }

    public void U(ConstraintAnchor.b bVar, ConstraintWidget constraintWidget, ConstraintAnchor.b bVar2, int i10, int i11) {
        n(bVar).b(constraintWidget.n(bVar2), i10, i11, true);
    }

    public boolean W() {
        ConstraintAnchor constraintAnchor = this.D;
        ConstraintAnchor constraintAnchor2 = constraintAnchor.f14735d;
        if (constraintAnchor2 != null && constraintAnchor2.f14735d == constraintAnchor) {
            return true;
        }
        ConstraintAnchor constraintAnchor3 = this.F;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.f14735d;
        return constraintAnchor4 != null && constraintAnchor4.f14735d == constraintAnchor3;
    }

    public boolean X() {
        return this.B;
    }

    public boolean Y() {
        ConstraintAnchor constraintAnchor = this.E;
        ConstraintAnchor constraintAnchor2 = constraintAnchor.f14735d;
        if (constraintAnchor2 != null && constraintAnchor2.f14735d == constraintAnchor) {
            return true;
        }
        ConstraintAnchor constraintAnchor3 = this.G;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.f14735d;
        return constraintAnchor4 != null && constraintAnchor4.f14735d == constraintAnchor3;
    }

    public void Z() {
        this.D.l();
        this.E.l();
        this.F.l();
        this.G.l();
        this.H.l();
        this.I.l();
        this.J.l();
        this.K.l();
        this.P = null;
        this.f14800z = 0.0f;
        this.Q = 0;
        this.R = 0;
        this.S = 0.0f;
        this.T = -1;
        this.U = 0;
        this.V = 0;
        this.Y = 0;
        this.Z = 0;
        this.f14751a0 = 0;
        this.f14753b0 = 0;
        this.f14755c0 = 0;
        float f10 = F0;
        this.f14757d0 = f10;
        this.f14759e0 = f10;
        b[] bVarArr = this.O;
        b bVar = b.FIXED;
        bVarArr[0] = bVar;
        bVarArr[1] = bVar;
        this.f14761f0 = null;
        this.f14763g0 = 0;
        this.f14765h0 = 0;
        this.f14769j0 = null;
        this.f14787s0 = false;
        this.f14789t0 = false;
        this.f14795w0 = 0;
        this.f14797x0 = 0;
        this.f14799y0 = false;
        this.f14801z0 = false;
        float[] fArr = this.A0;
        fArr[0] = -1.0f;
        fArr[1] = -1.0f;
        this.f14768j = -1;
        this.f14770k = -1;
        int[] iArr = this.f14798y;
        iArr[0] = Integer.MAX_VALUE;
        iArr[1] = Integer.MAX_VALUE;
        this.f14772l = 0;
        this.f14774m = 0;
        this.f14782q = 1.0f;
        this.f14788t = 1.0f;
        this.f14780p = Integer.MAX_VALUE;
        this.f14786s = Integer.MAX_VALUE;
        this.f14778o = 0;
        this.f14784r = 0;
        this.f14766i = false;
        this.f14794w = -1;
        this.f14796x = 1.0f;
        this.f14791u0 = false;
        this.f14793v0 = false;
        boolean[] zArr = this.f14762g;
        zArr[0] = true;
        zArr[1] = true;
        this.C = false;
        boolean[] zArr2 = this.N;
        zArr2[0] = false;
        zArr2[1] = false;
    }

    public void a0() {
        ConstraintWidget H = H();
        if (H != null && (H instanceof ConstraintWidgetContainer) && ((ConstraintWidgetContainer) H()).Y0()) {
            return;
        }
        int size = this.M.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.M.get(i10).l();
        }
    }

    public void b0(l.c cVar) {
        this.D.m(cVar);
        this.E.m(cVar);
        this.F.m(cVar);
        this.G.m(cVar);
        this.H.m(cVar);
        this.K.m(cVar);
        this.I.m(cVar);
        this.J.m(cVar);
    }

    public void c0(int i10) {
        this.f14751a0 = i10;
        this.A = i10 > 0;
    }

    public void d0(Object obj) {
        this.f14761f0 = obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean e() {
        return (this instanceof l) || (this instanceof h);
    }

    public void e0(String str) {
        this.f14767i0 = str;
    }

    /* JADX WARN: Removed duplicated region for block: B:112:0x023a  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0250  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x025b  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0277  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0375  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x03d2  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x03d7  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0498  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x04cd  */
    /* JADX WARN: Removed duplicated region for block: B:187:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:189:0x04c3  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0492  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x03d4  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x0359  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x025e  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x0244 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void f(LinearSystem linearSystem) {
        boolean z10;
        boolean z11;
        boolean z12;
        boolean z13;
        SolverVariable solverVariable;
        char c10;
        int i10;
        int i11;
        boolean z14;
        int i12;
        int i13;
        boolean z15;
        boolean z16;
        b bVar;
        boolean z17;
        SolverVariable solverVariable2;
        SolverVariable solverVariable3;
        SolverVariable solverVariable4;
        SolverVariable solverVariable5;
        SolverVariable solverVariable6;
        boolean z18;
        DependencyNode dependencyNode;
        LinearSystem linearSystem2;
        SolverVariable solverVariable7;
        SolverVariable solverVariable8;
        SolverVariable solverVariable9;
        int i14;
        int i15;
        int i16;
        int i17;
        SolverVariable solverVariable10;
        SolverVariable solverVariable11;
        ConstraintWidget constraintWidget;
        boolean z19;
        int i18;
        int i19;
        char c11;
        int i20;
        boolean W;
        boolean Y;
        ConstraintWidget constraintWidget2 = this;
        SolverVariable q10 = linearSystem.q(constraintWidget2.D);
        SolverVariable q11 = linearSystem.q(constraintWidget2.F);
        SolverVariable q12 = linearSystem.q(constraintWidget2.E);
        SolverVariable q13 = linearSystem.q(constraintWidget2.G);
        SolverVariable q14 = linearSystem.q(constraintWidget2.H);
        boolean z20 = LinearSystem.f14500r;
        HorizontalWidgetRun horizontalWidgetRun = constraintWidget2.f14758e;
        DependencyNode dependencyNode2 = horizontalWidgetRun.f15612h;
        if (dependencyNode2.f15579j && horizontalWidgetRun.f15613i.f15579j) {
            VerticalWidgetRun verticalWidgetRun = constraintWidget2.f14760f;
            if (verticalWidgetRun.f15612h.f15579j && verticalWidgetRun.f15613i.f15579j) {
                linearSystem.f(q10, dependencyNode2.f15576g);
                linearSystem.f(q11, constraintWidget2.f14758e.f15613i.f15576g);
                linearSystem.f(q12, constraintWidget2.f14760f.f15612h.f15576g);
                linearSystem.f(q13, constraintWidget2.f14760f.f15613i.f15576g);
                linearSystem.f(q14, constraintWidget2.f14760f.f15602k.f15576g);
                ConstraintWidget constraintWidget3 = constraintWidget2.P;
                if (constraintWidget3 != null) {
                    boolean z21 = constraintWidget3 != null && constraintWidget3.O[0] == b.WRAP_CONTENT;
                    boolean z22 = constraintWidget3 != null && constraintWidget3.O[1] == b.WRAP_CONTENT;
                    if (z21 && constraintWidget2.f14762g[0] && !W()) {
                        linearSystem.h(linearSystem.q(constraintWidget2.P.F), q11, 0, 8);
                    }
                    if (z22 && constraintWidget2.f14762g[1] && !Y()) {
                        linearSystem.h(linearSystem.q(constraintWidget2.P.G), q13, 0, 8);
                        return;
                    }
                    return;
                }
                return;
            }
        }
        ConstraintWidget constraintWidget4 = constraintWidget2.P;
        if (constraintWidget4 != null) {
            boolean z23 = constraintWidget4 != null && constraintWidget4.O[0] == b.WRAP_CONTENT;
            z10 = constraintWidget4 != null && constraintWidget4.O[1] == b.WRAP_CONTENT;
            if (constraintWidget2.V(0)) {
                ((ConstraintWidgetContainer) constraintWidget2.P).P0(constraintWidget2, 0);
                W = true;
            } else {
                W = W();
            }
            if (constraintWidget2.V(1)) {
                ((ConstraintWidgetContainer) constraintWidget2.P).P0(constraintWidget2, 1);
                Y = true;
            } else {
                Y = Y();
            }
            if (!W && z23 && constraintWidget2.f14765h0 != 8 && constraintWidget2.D.f14735d == null && constraintWidget2.F.f14735d == null) {
                linearSystem.h(linearSystem.q(constraintWidget2.P.F), q11, 0, 1);
            }
            if (!Y && z10 && constraintWidget2.f14765h0 != 8 && constraintWidget2.E.f14735d == null && constraintWidget2.G.f14735d == null && constraintWidget2.H == null) {
                linearSystem.h(linearSystem.q(constraintWidget2.P.G), q13, 0, 1);
            }
            z11 = z23;
            z13 = W;
            z12 = Y;
        } else {
            z10 = false;
            z11 = false;
            z12 = false;
            z13 = false;
        }
        int i21 = constraintWidget2.Q;
        int i22 = constraintWidget2.f14753b0;
        if (i21 >= i22) {
            i22 = i21;
        }
        int i23 = constraintWidget2.R;
        int i24 = constraintWidget2.f14755c0;
        if (i23 >= i24) {
            i24 = i23;
        }
        b[] bVarArr = constraintWidget2.O;
        b bVar2 = bVarArr[0];
        b bVar3 = b.MATCH_CONSTRAINT;
        boolean z24 = bVar2 != bVar3;
        boolean z25 = bVarArr[1] != bVar3;
        int i25 = constraintWidget2.T;
        constraintWidget2.f14794w = i25;
        float f10 = constraintWidget2.S;
        constraintWidget2.f14796x = f10;
        int i26 = i22;
        int i27 = constraintWidget2.f14772l;
        int i28 = i24;
        int i29 = constraintWidget2.f14774m;
        if (f10 > 0.0f) {
            solverVariable = q10;
            if (constraintWidget2.f14765h0 != 8) {
                if (bVarArr[0] == bVar3 && i27 == 0) {
                    i27 = 3;
                }
                if (bVarArr[1] == bVar3 && i29 == 0) {
                    c11 = 0;
                    i29 = 3;
                } else {
                    c11 = 0;
                }
                if (bVarArr[c11] == bVar3 && bVarArr[1] == bVar3) {
                    i20 = 3;
                    if (i27 == 3 && i29 == 3) {
                        constraintWidget2.I0(z11, z10, z24, z25);
                        c10 = 0;
                        i10 = i27;
                        i11 = i29;
                        i12 = i26;
                        i13 = i28;
                        z14 = true;
                        int[] iArr = constraintWidget2.f14776n;
                        iArr[c10] = i10;
                        iArr[1] = i11;
                        constraintWidget2.f14766i = z14;
                        if (!z14 && ((i19 = constraintWidget2.f14794w) == 0 || i19 == -1)) {
                            z15 = true;
                            b bVar4 = constraintWidget2.O[0];
                            b bVar5 = b.WRAP_CONTENT;
                            boolean z26 = bVar4 != bVar5 && (constraintWidget2 instanceof ConstraintWidgetContainer);
                            int i30 = z26 ? 0 : i12;
                            boolean z27 = !constraintWidget2.K.j();
                            boolean[] zArr = constraintWidget2.N;
                            boolean z28 = zArr[0];
                            boolean z29 = zArr[1];
                            if (constraintWidget2.f14768j != 2) {
                                HorizontalWidgetRun horizontalWidgetRun2 = constraintWidget2.f14758e;
                                DependencyNode dependencyNode3 = horizontalWidgetRun2.f15612h;
                                if (dependencyNode3.f15579j && horizontalWidgetRun2.f15613i.f15579j) {
                                    SolverVariable solverVariable12 = solverVariable;
                                    linearSystem.f(solverVariable12, dependencyNode3.f15576g);
                                    linearSystem.f(q11, constraintWidget2.f14758e.f15613i.f15576g);
                                    if (constraintWidget2.P != null && z11 && constraintWidget2.f14762g[0] && !W()) {
                                        linearSystem.h(linearSystem.q(constraintWidget2.P.F), q11, 0, 8);
                                    }
                                    z16 = z10;
                                    bVar = bVar5;
                                    z17 = z14;
                                    z18 = z11;
                                    solverVariable5 = q11;
                                    solverVariable6 = solverVariable12;
                                    solverVariable2 = q14;
                                    solverVariable3 = q13;
                                    solverVariable4 = q12;
                                    VerticalWidgetRun verticalWidgetRun2 = constraintWidget2.f14760f;
                                    dependencyNode = verticalWidgetRun2.f15612h;
                                    if (dependencyNode.f15579j || !verticalWidgetRun2.f15613i.f15579j) {
                                        linearSystem2 = linearSystem;
                                        solverVariable7 = solverVariable2;
                                        solverVariable8 = solverVariable3;
                                        solverVariable9 = solverVariable4;
                                        i14 = 8;
                                        i15 = 0;
                                        i16 = 1;
                                        i17 = 1;
                                    } else {
                                        linearSystem2 = linearSystem;
                                        solverVariable9 = solverVariable4;
                                        linearSystem2.f(solverVariable9, dependencyNode.f15576g);
                                        solverVariable8 = solverVariable3;
                                        linearSystem2.f(solverVariable8, constraintWidget2.f14760f.f15613i.f15576g);
                                        solverVariable7 = solverVariable2;
                                        linearSystem2.f(solverVariable7, constraintWidget2.f14760f.f15602k.f15576g);
                                        ConstraintWidget constraintWidget5 = constraintWidget2.P;
                                        if (constraintWidget5 == null || z12 || !z16) {
                                            i14 = 8;
                                            i15 = 0;
                                            i16 = 1;
                                        } else {
                                            i16 = 1;
                                            if (constraintWidget2.f14762g[1]) {
                                                i14 = 8;
                                                i15 = 0;
                                                linearSystem2.h(linearSystem2.q(constraintWidget5.G), solverVariable8, 0, 8);
                                            } else {
                                                i14 = 8;
                                                i15 = 0;
                                            }
                                        }
                                        i17 = i15;
                                    }
                                    if ((constraintWidget2.f14770k != 2 ? i15 : i17) == 0) {
                                        boolean z30 = (constraintWidget2.O[i16] == bVar && (constraintWidget2 instanceof ConstraintWidgetContainer)) ? i16 : i15;
                                        if (z30) {
                                            i13 = i15;
                                        }
                                        int i31 = (z17 && ((i18 = constraintWidget2.f14794w) == i16 || i18 == -1)) ? i16 : i15;
                                        ConstraintWidget constraintWidget6 = constraintWidget2.P;
                                        SolverVariable q15 = constraintWidget6 != null ? linearSystem2.q(constraintWidget6.G) : null;
                                        ConstraintWidget constraintWidget7 = constraintWidget2.P;
                                        SolverVariable q16 = constraintWidget7 != null ? linearSystem2.q(constraintWidget7.E) : null;
                                        if (constraintWidget2.f14751a0 > 0 || constraintWidget2.f14765h0 == i14) {
                                            linearSystem2.e(solverVariable7, solverVariable9, o(), i14);
                                            ConstraintAnchor constraintAnchor = constraintWidget2.H.f14735d;
                                            if (constraintAnchor != null) {
                                                linearSystem2.e(solverVariable7, linearSystem2.q(constraintAnchor), i15, i14);
                                                if (z16) {
                                                    linearSystem2.h(q15, linearSystem2.q(constraintWidget2.G), i15, 5);
                                                }
                                                z19 = i15;
                                                solverVariable10 = solverVariable8;
                                                solverVariable11 = solverVariable9;
                                                h(linearSystem, false, z16, z18, constraintWidget2.f14762g[i16], q16, q15, constraintWidget2.O[i16], z30, constraintWidget2.E, constraintWidget2.G, constraintWidget2.V, i13, constraintWidget2.f14755c0, constraintWidget2.f14798y[i16], constraintWidget2.f14759e0, i31, z12, z13, z29, i11, i10, constraintWidget2.f14784r, constraintWidget2.f14786s, constraintWidget2.f14788t, z19);
                                            } else if (constraintWidget2.f14765h0 == i14) {
                                                linearSystem2.e(solverVariable7, solverVariable9, i15, i14);
                                            }
                                        }
                                        z19 = z27;
                                        solverVariable10 = solverVariable8;
                                        solverVariable11 = solverVariable9;
                                        h(linearSystem, false, z16, z18, constraintWidget2.f14762g[i16], q16, q15, constraintWidget2.O[i16], z30, constraintWidget2.E, constraintWidget2.G, constraintWidget2.V, i13, constraintWidget2.f14755c0, constraintWidget2.f14798y[i16], constraintWidget2.f14759e0, i31, z12, z13, z29, i11, i10, constraintWidget2.f14784r, constraintWidget2.f14786s, constraintWidget2.f14788t, z19);
                                    } else {
                                        solverVariable10 = solverVariable8;
                                        solverVariable11 = solverVariable9;
                                    }
                                    if (z17) {
                                        constraintWidget = this;
                                    } else {
                                        constraintWidget = this;
                                        if (constraintWidget.f14794w == 1) {
                                            linearSystem.k(solverVariable10, solverVariable11, solverVariable5, solverVariable6, constraintWidget.f14796x, 8);
                                        } else {
                                            linearSystem.k(solverVariable5, solverVariable6, solverVariable10, solverVariable11, constraintWidget.f14796x, 8);
                                        }
                                    }
                                    if (constraintWidget.K.j()) {
                                        return;
                                    }
                                    linearSystem.b(constraintWidget, constraintWidget.K.g().e(), (float) Math.toRadians(constraintWidget.f14800z + 90.0f), constraintWidget.K.c());
                                    return;
                                }
                                SolverVariable solverVariable13 = solverVariable;
                                ConstraintWidget constraintWidget8 = constraintWidget2.P;
                                SolverVariable q17 = constraintWidget8 != null ? linearSystem.q(constraintWidget8.F) : null;
                                ConstraintWidget constraintWidget9 = constraintWidget2.P;
                                z18 = z11;
                                solverVariable5 = q11;
                                solverVariable6 = solverVariable13;
                                z16 = z10;
                                solverVariable2 = q14;
                                solverVariable3 = q13;
                                solverVariable4 = q12;
                                bVar = bVar5;
                                z17 = z14;
                                h(linearSystem, true, z18, z16, constraintWidget2.f14762g[0], constraintWidget9 != null ? linearSystem.q(constraintWidget9.D) : null, q17, constraintWidget2.O[0], z26, constraintWidget2.D, constraintWidget2.F, constraintWidget2.U, i30, constraintWidget2.f14753b0, constraintWidget2.f14798y[0], constraintWidget2.f14757d0, z15, z13, z12, z28, i10, i11, constraintWidget2.f14778o, constraintWidget2.f14780p, constraintWidget2.f14782q, z27);
                            } else {
                                z16 = z10;
                                bVar = bVar5;
                                z17 = z14;
                                solverVariable2 = q14;
                                solverVariable3 = q13;
                                solverVariable4 = q12;
                                solverVariable5 = q11;
                                solverVariable6 = solverVariable;
                                z18 = z11;
                            }
                            constraintWidget2 = this;
                            VerticalWidgetRun verticalWidgetRun22 = constraintWidget2.f14760f;
                            dependencyNode = verticalWidgetRun22.f15612h;
                            if (dependencyNode.f15579j) {
                            }
                            linearSystem2 = linearSystem;
                            solverVariable7 = solverVariable2;
                            solverVariable8 = solverVariable3;
                            solverVariable9 = solverVariable4;
                            i14 = 8;
                            i15 = 0;
                            i16 = 1;
                            i17 = 1;
                            if ((constraintWidget2.f14770k != 2 ? i15 : i17) == 0) {
                            }
                            if (z17) {
                            }
                            if (constraintWidget.K.j()) {
                            }
                        }
                        z15 = false;
                        b bVar42 = constraintWidget2.O[0];
                        b bVar52 = b.WRAP_CONTENT;
                        if (bVar42 != bVar52) {
                        }
                        if (z26) {
                        }
                        boolean z272 = !constraintWidget2.K.j();
                        boolean[] zArr2 = constraintWidget2.N;
                        boolean z282 = zArr2[0];
                        boolean z292 = zArr2[1];
                        if (constraintWidget2.f14768j != 2) {
                        }
                        constraintWidget2 = this;
                        VerticalWidgetRun verticalWidgetRun222 = constraintWidget2.f14760f;
                        dependencyNode = verticalWidgetRun222.f15612h;
                        if (dependencyNode.f15579j) {
                        }
                        linearSystem2 = linearSystem;
                        solverVariable7 = solverVariable2;
                        solverVariable8 = solverVariable3;
                        solverVariable9 = solverVariable4;
                        i14 = 8;
                        i15 = 0;
                        i16 = 1;
                        i17 = 1;
                        if ((constraintWidget2.f14770k != 2 ? i15 : i17) == 0) {
                        }
                        if (z17) {
                        }
                        if (constraintWidget.K.j()) {
                        }
                    }
                } else {
                    i20 = 3;
                }
                if (bVarArr[0] == bVar3 && i27 == i20) {
                    constraintWidget2.f14794w = 0;
                    int i32 = (int) (f10 * i23);
                    if (bVarArr[1] != bVar3) {
                        i12 = i32;
                        i11 = i29;
                        i13 = i28;
                        i10 = 4;
                        z14 = false;
                        c10 = 0;
                    } else {
                        i10 = i27;
                        z14 = true;
                        i11 = i29;
                        i13 = i28;
                        c10 = 0;
                        i12 = i32;
                    }
                } else {
                    if (bVarArr[1] == bVar3 && i29 == 3) {
                        constraintWidget2.f14794w = 1;
                        if (i25 == -1) {
                            constraintWidget2.f14796x = 1.0f / f10;
                        }
                        c10 = 0;
                        i13 = (int) (constraintWidget2.f14796x * i21);
                        i10 = i27;
                        if (bVarArr[0] != bVar3) {
                            z14 = false;
                            i12 = i26;
                            i11 = 4;
                        } else {
                            i11 = i29;
                            i12 = i26;
                            z14 = true;
                        }
                    }
                    c10 = 0;
                    i10 = i27;
                    i11 = i29;
                    i12 = i26;
                    i13 = i28;
                    z14 = true;
                }
                int[] iArr2 = constraintWidget2.f14776n;
                iArr2[c10] = i10;
                iArr2[1] = i11;
                constraintWidget2.f14766i = z14;
                if (!z14) {
                    z15 = true;
                    b bVar422 = constraintWidget2.O[0];
                    b bVar522 = b.WRAP_CONTENT;
                    if (bVar422 != bVar522) {
                    }
                    if (z26) {
                    }
                    boolean z2722 = !constraintWidget2.K.j();
                    boolean[] zArr22 = constraintWidget2.N;
                    boolean z2822 = zArr22[0];
                    boolean z2922 = zArr22[1];
                    if (constraintWidget2.f14768j != 2) {
                    }
                    constraintWidget2 = this;
                    VerticalWidgetRun verticalWidgetRun2222 = constraintWidget2.f14760f;
                    dependencyNode = verticalWidgetRun2222.f15612h;
                    if (dependencyNode.f15579j) {
                    }
                    linearSystem2 = linearSystem;
                    solverVariable7 = solverVariable2;
                    solverVariable8 = solverVariable3;
                    solverVariable9 = solverVariable4;
                    i14 = 8;
                    i15 = 0;
                    i16 = 1;
                    i17 = 1;
                    if ((constraintWidget2.f14770k != 2 ? i15 : i17) == 0) {
                    }
                    if (z17) {
                    }
                    if (constraintWidget.K.j()) {
                    }
                }
                z15 = false;
                b bVar4222 = constraintWidget2.O[0];
                b bVar5222 = b.WRAP_CONTENT;
                if (bVar4222 != bVar5222) {
                }
                if (z26) {
                }
                boolean z27222 = !constraintWidget2.K.j();
                boolean[] zArr222 = constraintWidget2.N;
                boolean z28222 = zArr222[0];
                boolean z29222 = zArr222[1];
                if (constraintWidget2.f14768j != 2) {
                }
                constraintWidget2 = this;
                VerticalWidgetRun verticalWidgetRun22222 = constraintWidget2.f14760f;
                dependencyNode = verticalWidgetRun22222.f15612h;
                if (dependencyNode.f15579j) {
                }
                linearSystem2 = linearSystem;
                solverVariable7 = solverVariable2;
                solverVariable8 = solverVariable3;
                solverVariable9 = solverVariable4;
                i14 = 8;
                i15 = 0;
                i16 = 1;
                i17 = 1;
                if ((constraintWidget2.f14770k != 2 ? i15 : i17) == 0) {
                }
                if (z17) {
                }
                if (constraintWidget.K.j()) {
                }
            }
        } else {
            solverVariable = q10;
        }
        c10 = 0;
        i10 = i27;
        i11 = i29;
        z14 = false;
        i12 = i26;
        i13 = i28;
        int[] iArr22 = constraintWidget2.f14776n;
        iArr22[c10] = i10;
        iArr22[1] = i11;
        constraintWidget2.f14766i = z14;
        if (!z14) {
        }
        z15 = false;
        b bVar42222 = constraintWidget2.O[0];
        b bVar52222 = b.WRAP_CONTENT;
        if (bVar42222 != bVar52222) {
        }
        if (z26) {
        }
        boolean z272222 = !constraintWidget2.K.j();
        boolean[] zArr2222 = constraintWidget2.N;
        boolean z282222 = zArr2222[0];
        boolean z292222 = zArr2222[1];
        if (constraintWidget2.f14768j != 2) {
        }
        constraintWidget2 = this;
        VerticalWidgetRun verticalWidgetRun222222 = constraintWidget2.f14760f;
        dependencyNode = verticalWidgetRun222222.f15612h;
        if (dependencyNode.f15579j) {
        }
        linearSystem2 = linearSystem;
        solverVariable7 = solverVariable2;
        solverVariable8 = solverVariable3;
        solverVariable9 = solverVariable4;
        i14 = 8;
        i15 = 0;
        i16 = 1;
        i17 = 1;
        if ((constraintWidget2.f14770k != 2 ? i15 : i17) == 0) {
        }
        if (z17) {
        }
        if (constraintWidget.K.j()) {
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:38:0x0084 -> B:31:0x0085). Please report as a decompilation issue!!! */
    public void f0(String str) {
        float f10;
        int i10 = 0;
        if (str != null && str.length() != 0) {
            int i11 = -1;
            int length = str.length();
            int indexOf = str.indexOf(44);
            int i12 = 0;
            if (indexOf > 0 && indexOf < length - 1) {
                String substring = str.substring(0, indexOf);
                if (substring.equalsIgnoreCase("W")) {
                    i11 = 0;
                } else if (substring.equalsIgnoreCase("H")) {
                    i11 = 1;
                }
                i12 = indexOf + 1;
            }
            int indexOf2 = str.indexOf(58);
            if (indexOf2 >= 0 && indexOf2 < length - 1) {
                String substring2 = str.substring(i12, indexOf2);
                String substring3 = str.substring(indexOf2 + 1);
                if (substring2.length() > 0 && substring3.length() > 0) {
                    float parseFloat = Float.parseFloat(substring2);
                    float parseFloat2 = Float.parseFloat(substring3);
                    if (parseFloat > 0.0f && parseFloat2 > 0.0f) {
                        if (i11 == 1) {
                            f10 = Math.abs(parseFloat2 / parseFloat);
                        } else {
                            f10 = Math.abs(parseFloat / parseFloat2);
                        }
                    }
                }
                f10 = i10;
            } else {
                String substring4 = str.substring(i12);
                if (substring4.length() > 0) {
                    f10 = Float.parseFloat(substring4);
                }
                f10 = i10;
            }
            i10 = (f10 > i10 ? 1 : (f10 == i10 ? 0 : -1));
            if (i10 > 0) {
                this.S = f10;
                this.T = i11;
                return;
            }
            return;
        }
        this.S = 0.0f;
    }

    public boolean g() {
        return this.f14765h0 != 8;
    }

    public void g0(int i10, int i11, int i12, int i13) {
        int i14;
        int i15;
        int i16 = i12 - i10;
        int i17 = i13 - i11;
        this.U = i10;
        this.V = i11;
        if (this.f14765h0 == 8) {
            this.Q = 0;
            this.R = 0;
            return;
        }
        b[] bVarArr = this.O;
        b bVar = bVarArr[0];
        b bVar2 = b.FIXED;
        if (bVar == bVar2 && i16 < (i15 = this.Q)) {
            i16 = i15;
        }
        if (bVarArr[1] == bVar2 && i17 < (i14 = this.R)) {
            i17 = i14;
        }
        this.Q = i16;
        this.R = i17;
        int i18 = this.f14755c0;
        if (i17 < i18) {
            this.R = i18;
        }
        int i19 = this.f14753b0;
        if (i16 < i19) {
            this.Q = i19;
        }
    }

    public void h0(boolean z10) {
        this.A = z10;
    }

    public void i(ConstraintAnchor.b bVar, ConstraintWidget constraintWidget, ConstraintAnchor.b bVar2, int i10) {
        ConstraintAnchor.b bVar3;
        ConstraintAnchor.b bVar4;
        boolean z10;
        ConstraintAnchor.b bVar5 = ConstraintAnchor.b.CENTER;
        if (bVar == bVar5) {
            if (bVar2 == bVar5) {
                ConstraintAnchor.b bVar6 = ConstraintAnchor.b.LEFT;
                ConstraintAnchor n10 = n(bVar6);
                ConstraintAnchor.b bVar7 = ConstraintAnchor.b.RIGHT;
                ConstraintAnchor n11 = n(bVar7);
                ConstraintAnchor.b bVar8 = ConstraintAnchor.b.TOP;
                ConstraintAnchor n12 = n(bVar8);
                ConstraintAnchor.b bVar9 = ConstraintAnchor.b.BOTTOM;
                ConstraintAnchor n13 = n(bVar9);
                boolean z11 = true;
                if ((n10 == null || !n10.j()) && (n11 == null || !n11.j())) {
                    i(bVar6, constraintWidget, bVar6, 0);
                    i(bVar7, constraintWidget, bVar7, 0);
                    z10 = true;
                } else {
                    z10 = false;
                }
                if ((n12 == null || !n12.j()) && (n13 == null || !n13.j())) {
                    i(bVar8, constraintWidget, bVar8, 0);
                    i(bVar9, constraintWidget, bVar9, 0);
                } else {
                    z11 = false;
                }
                if (z10 && z11) {
                    n(bVar5).a(constraintWidget.n(bVar5), 0);
                    return;
                }
                if (z10) {
                    ConstraintAnchor.b bVar10 = ConstraintAnchor.b.CENTER_X;
                    n(bVar10).a(constraintWidget.n(bVar10), 0);
                    return;
                } else {
                    if (z11) {
                        ConstraintAnchor.b bVar11 = ConstraintAnchor.b.CENTER_Y;
                        n(bVar11).a(constraintWidget.n(bVar11), 0);
                        return;
                    }
                    return;
                }
            }
            ConstraintAnchor.b bVar12 = ConstraintAnchor.b.LEFT;
            if (bVar2 != bVar12 && bVar2 != ConstraintAnchor.b.RIGHT) {
                ConstraintAnchor.b bVar13 = ConstraintAnchor.b.TOP;
                if (bVar2 == bVar13 || bVar2 == ConstraintAnchor.b.BOTTOM) {
                    i(bVar13, constraintWidget, bVar2, 0);
                    i(ConstraintAnchor.b.BOTTOM, constraintWidget, bVar2, 0);
                    n(bVar5).a(constraintWidget.n(bVar2), 0);
                    return;
                }
                return;
            }
            i(bVar12, constraintWidget, bVar2, 0);
            i(ConstraintAnchor.b.RIGHT, constraintWidget, bVar2, 0);
            n(bVar5).a(constraintWidget.n(bVar2), 0);
            return;
        }
        ConstraintAnchor.b bVar14 = ConstraintAnchor.b.CENTER_X;
        if (bVar == bVar14 && (bVar2 == (bVar4 = ConstraintAnchor.b.LEFT) || bVar2 == ConstraintAnchor.b.RIGHT)) {
            ConstraintAnchor n14 = n(bVar4);
            ConstraintAnchor n15 = constraintWidget.n(bVar2);
            ConstraintAnchor n16 = n(ConstraintAnchor.b.RIGHT);
            n14.a(n15, 0);
            n16.a(n15, 0);
            n(bVar14).a(n15, 0);
            return;
        }
        ConstraintAnchor.b bVar15 = ConstraintAnchor.b.CENTER_Y;
        if (bVar == bVar15 && (bVar2 == (bVar3 = ConstraintAnchor.b.TOP) || bVar2 == ConstraintAnchor.b.BOTTOM)) {
            ConstraintAnchor n17 = constraintWidget.n(bVar2);
            n(bVar3).a(n17, 0);
            n(ConstraintAnchor.b.BOTTOM).a(n17, 0);
            n(bVar15).a(n17, 0);
            return;
        }
        if (bVar == bVar14 && bVar2 == bVar14) {
            ConstraintAnchor.b bVar16 = ConstraintAnchor.b.LEFT;
            n(bVar16).a(constraintWidget.n(bVar16), 0);
            ConstraintAnchor.b bVar17 = ConstraintAnchor.b.RIGHT;
            n(bVar17).a(constraintWidget.n(bVar17), 0);
            n(bVar14).a(constraintWidget.n(bVar2), 0);
            return;
        }
        if (bVar == bVar15 && bVar2 == bVar15) {
            ConstraintAnchor.b bVar18 = ConstraintAnchor.b.TOP;
            n(bVar18).a(constraintWidget.n(bVar18), 0);
            ConstraintAnchor.b bVar19 = ConstraintAnchor.b.BOTTOM;
            n(bVar19).a(constraintWidget.n(bVar19), 0);
            n(bVar15).a(constraintWidget.n(bVar2), 0);
            return;
        }
        ConstraintAnchor n18 = n(bVar);
        ConstraintAnchor n19 = constraintWidget.n(bVar2);
        if (n18.k(n19)) {
            ConstraintAnchor.b bVar20 = ConstraintAnchor.b.BASELINE;
            if (bVar == bVar20) {
                ConstraintAnchor n20 = n(ConstraintAnchor.b.TOP);
                ConstraintAnchor n21 = n(ConstraintAnchor.b.BOTTOM);
                if (n20 != null) {
                    n20.l();
                }
                if (n21 != null) {
                    n21.l();
                }
                i10 = 0;
            } else if (bVar != ConstraintAnchor.b.TOP && bVar != ConstraintAnchor.b.BOTTOM) {
                if (bVar == ConstraintAnchor.b.LEFT || bVar == ConstraintAnchor.b.RIGHT) {
                    ConstraintAnchor n22 = n(bVar5);
                    if (n22.g() != n19) {
                        n22.l();
                    }
                    ConstraintAnchor d10 = n(bVar).d();
                    ConstraintAnchor n23 = n(bVar14);
                    if (n23.j()) {
                        d10.l();
                        n23.l();
                    }
                }
            } else {
                ConstraintAnchor n24 = n(bVar20);
                if (n24 != null) {
                    n24.l();
                }
                ConstraintAnchor n25 = n(bVar5);
                if (n25.g() != n19) {
                    n25.l();
                }
                ConstraintAnchor d11 = n(bVar).d();
                ConstraintAnchor n26 = n(bVar15);
                if (n26.j()) {
                    d11.l();
                    n26.l();
                }
            }
            n18.a(n19, i10);
        }
    }

    public void i0(int i10) {
        this.R = i10;
        int i11 = this.f14755c0;
        if (i10 < i11) {
            this.R = i11;
        }
    }

    public void j(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int i10) {
        if (constraintAnchor.e() == this) {
            i(constraintAnchor.h(), constraintAnchor2.e(), constraintAnchor2.h(), i10);
        }
    }

    public void j0(float f10) {
        this.f14757d0 = f10;
    }

    public void k(ConstraintWidget constraintWidget, float f10, int i10) {
        ConstraintAnchor.b bVar = ConstraintAnchor.b.CENTER;
        U(bVar, constraintWidget, bVar, i10, 0);
        this.f14800z = f10;
    }

    public void k0(int i10) {
        this.f14795w0 = i10;
    }

    public void l(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        this.f14768j = constraintWidget.f14768j;
        this.f14770k = constraintWidget.f14770k;
        this.f14772l = constraintWidget.f14772l;
        this.f14774m = constraintWidget.f14774m;
        int[] iArr = this.f14776n;
        int[] iArr2 = constraintWidget.f14776n;
        iArr[0] = iArr2[0];
        iArr[1] = iArr2[1];
        this.f14778o = constraintWidget.f14778o;
        this.f14780p = constraintWidget.f14780p;
        this.f14784r = constraintWidget.f14784r;
        this.f14786s = constraintWidget.f14786s;
        this.f14788t = constraintWidget.f14788t;
        this.f14790u = constraintWidget.f14790u;
        this.f14792v = constraintWidget.f14792v;
        this.f14794w = constraintWidget.f14794w;
        this.f14796x = constraintWidget.f14796x;
        int[] iArr3 = constraintWidget.f14798y;
        this.f14798y = Arrays.copyOf(iArr3, iArr3.length);
        this.f14800z = constraintWidget.f14800z;
        this.A = constraintWidget.A;
        this.B = constraintWidget.B;
        this.D.l();
        this.E.l();
        this.F.l();
        this.G.l();
        this.H.l();
        this.I.l();
        this.J.l();
        this.K.l();
        this.O = (b[]) Arrays.copyOf(this.O, 2);
        this.P = this.P == null ? null : hashMap.get(constraintWidget.P);
        this.Q = constraintWidget.Q;
        this.R = constraintWidget.R;
        this.S = constraintWidget.S;
        this.T = constraintWidget.T;
        this.U = constraintWidget.U;
        this.V = constraintWidget.V;
        this.W = constraintWidget.W;
        this.X = constraintWidget.X;
        this.Y = constraintWidget.Y;
        this.Z = constraintWidget.Z;
        this.f14751a0 = constraintWidget.f14751a0;
        this.f14753b0 = constraintWidget.f14753b0;
        this.f14755c0 = constraintWidget.f14755c0;
        this.f14757d0 = constraintWidget.f14757d0;
        this.f14759e0 = constraintWidget.f14759e0;
        this.f14761f0 = constraintWidget.f14761f0;
        this.f14763g0 = constraintWidget.f14763g0;
        this.f14765h0 = constraintWidget.f14765h0;
        this.f14767i0 = constraintWidget.f14767i0;
        this.f14769j0 = constraintWidget.f14769j0;
        this.f14771k0 = constraintWidget.f14771k0;
        this.f14773l0 = constraintWidget.f14773l0;
        this.f14775m0 = constraintWidget.f14775m0;
        this.f14777n0 = constraintWidget.f14777n0;
        this.f14779o0 = constraintWidget.f14779o0;
        this.f14781p0 = constraintWidget.f14781p0;
        this.f14783q0 = constraintWidget.f14783q0;
        this.f14785r0 = constraintWidget.f14785r0;
        this.f14787s0 = constraintWidget.f14787s0;
        this.f14789t0 = constraintWidget.f14789t0;
        this.f14791u0 = constraintWidget.f14791u0;
        this.f14793v0 = constraintWidget.f14793v0;
        this.f14795w0 = constraintWidget.f14795w0;
        this.f14797x0 = constraintWidget.f14797x0;
        this.f14799y0 = constraintWidget.f14799y0;
        this.f14801z0 = constraintWidget.f14801z0;
        float[] fArr = this.A0;
        float[] fArr2 = constraintWidget.A0;
        fArr[0] = fArr2[0];
        fArr[1] = fArr2[1];
        ConstraintWidget[] constraintWidgetArr = this.B0;
        ConstraintWidget[] constraintWidgetArr2 = constraintWidget.B0;
        constraintWidgetArr[0] = constraintWidgetArr2[0];
        constraintWidgetArr[1] = constraintWidgetArr2[1];
        ConstraintWidget[] constraintWidgetArr3 = this.C0;
        ConstraintWidget[] constraintWidgetArr4 = constraintWidget.C0;
        constraintWidgetArr3[0] = constraintWidgetArr4[0];
        constraintWidgetArr3[1] = constraintWidgetArr4[1];
        ConstraintWidget constraintWidget2 = constraintWidget.D0;
        this.D0 = constraintWidget2 == null ? null : hashMap.get(constraintWidget2);
        ConstraintWidget constraintWidget3 = constraintWidget.E0;
        this.E0 = constraintWidget3 != null ? hashMap.get(constraintWidget3) : null;
    }

    public void l0(int i10, int i11) {
        this.U = i10;
        int i12 = i11 - i10;
        this.Q = i12;
        int i13 = this.f14753b0;
        if (i12 < i13) {
            this.Q = i13;
        }
    }

    public void m(LinearSystem linearSystem) {
        linearSystem.q(this.D);
        linearSystem.q(this.E);
        linearSystem.q(this.F);
        linearSystem.q(this.G);
        if (this.f14751a0 > 0) {
            linearSystem.q(this.H);
        }
    }

    public void m0(b bVar) {
        this.O[0] = bVar;
    }

    public ConstraintAnchor n(ConstraintAnchor.b bVar) {
        switch (a.f14802a[bVar.ordinal()]) {
            case 1:
                return this.D;
            case 2:
                return this.E;
            case 3:
                return this.F;
            case 4:
                return this.G;
            case 5:
                return this.H;
            case 6:
                return this.K;
            case 7:
                return this.I;
            case 8:
                return this.J;
            case 9:
                return null;
            default:
                throw new AssertionError(bVar.name());
        }
    }

    public void n0(int i10, int i11, int i12, float f10) {
        this.f14772l = i10;
        this.f14778o = i11;
        if (i12 == Integer.MAX_VALUE) {
            i12 = 0;
        }
        this.f14780p = i12;
        this.f14782q = f10;
        if (f10 <= 0.0f || f10 >= 1.0f || i10 != 0) {
            return;
        }
        this.f14772l = 2;
    }

    public int o() {
        return this.f14751a0;
    }

    public void o0(float f10) {
        this.A0[0] = f10;
    }

    public float p(int i10) {
        if (i10 == 0) {
            return this.f14757d0;
        }
        if (i10 == 1) {
            return this.f14759e0;
        }
        return -1.0f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void p0(int i10, boolean z10) {
        this.N[i10] = z10;
    }

    public int q() {
        return S() + this.R;
    }

    public void q0(boolean z10) {
        this.B = z10;
    }

    public Object r() {
        return this.f14761f0;
    }

    public void r0(boolean z10) {
        this.C = z10;
    }

    public String s() {
        return this.f14767i0;
    }

    public void s0(int i10) {
        this.f14798y[1] = i10;
    }

    public b t(int i10) {
        if (i10 == 0) {
            return z();
        }
        if (i10 == 1) {
            return N();
        }
        return null;
    }

    public void t0(int i10) {
        this.f14798y[0] = i10;
    }

    public String toString() {
        String str;
        StringBuilder sb2 = new StringBuilder();
        String str2 = "";
        if (this.f14769j0 != null) {
            str = "type: " + this.f14769j0 + " ";
        } else {
            str = "";
        }
        sb2.append(str);
        if (this.f14767i0 != null) {
            str2 = "id: " + this.f14767i0 + " ";
        }
        sb2.append(str2);
        sb2.append("(");
        sb2.append(this.U);
        sb2.append(", ");
        sb2.append(this.V);
        sb2.append(") - (");
        sb2.append(this.Q);
        sb2.append(" x ");
        sb2.append(this.R);
        sb2.append(")");
        return sb2.toString();
    }

    public float u() {
        return this.S;
    }

    public void u0(int i10) {
        if (i10 < 0) {
            this.f14755c0 = 0;
        } else {
            this.f14755c0 = i10;
        }
    }

    public int v() {
        return this.T;
    }

    public void v0(int i10) {
        if (i10 < 0) {
            this.f14753b0 = 0;
        } else {
            this.f14753b0 = i10;
        }
    }

    public int w() {
        if (this.f14765h0 == 8) {
            return 0;
        }
        return this.R;
    }

    public void w0(int i10, int i11) {
        this.U = i10;
        this.V = i11;
    }

    public float x() {
        return this.f14757d0;
    }

    public void x0(ConstraintWidget constraintWidget) {
        this.P = constraintWidget;
    }

    public int y() {
        return this.f14795w0;
    }

    public void y0(float f10) {
        this.f14759e0 = f10;
    }

    public b z() {
        return this.O[0];
    }

    public void z0(int i10) {
        this.f14797x0 = i10;
    }
}
