package m;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import l.LinearSystem;
import m.ConstraintWidget;

/* compiled from: Flow.java */
/* loaded from: classes.dex */
public class g extends l {

    /* renamed from: s1, reason: collision with root package name */
    private ConstraintWidget[] f14831s1;
    private int V0 = -1;
    private int W0 = -1;
    private int X0 = -1;
    private int Y0 = -1;
    private int Z0 = -1;

    /* renamed from: a1, reason: collision with root package name */
    private int f14813a1 = -1;

    /* renamed from: b1, reason: collision with root package name */
    private float f14814b1 = 0.5f;

    /* renamed from: c1, reason: collision with root package name */
    private float f14815c1 = 0.5f;

    /* renamed from: d1, reason: collision with root package name */
    private float f14816d1 = 0.5f;

    /* renamed from: e1, reason: collision with root package name */
    private float f14817e1 = 0.5f;

    /* renamed from: f1, reason: collision with root package name */
    private float f14818f1 = 0.5f;

    /* renamed from: g1, reason: collision with root package name */
    private float f14819g1 = 0.5f;

    /* renamed from: h1, reason: collision with root package name */
    private int f14820h1 = 0;

    /* renamed from: i1, reason: collision with root package name */
    private int f14821i1 = 0;

    /* renamed from: j1, reason: collision with root package name */
    private int f14822j1 = 2;

    /* renamed from: k1, reason: collision with root package name */
    private int f14823k1 = 2;

    /* renamed from: l1, reason: collision with root package name */
    private int f14824l1 = 0;

    /* renamed from: m1, reason: collision with root package name */
    private int f14825m1 = -1;

    /* renamed from: n1, reason: collision with root package name */
    private int f14826n1 = 0;

    /* renamed from: o1, reason: collision with root package name */
    private ArrayList<a> f14827o1 = new ArrayList<>();

    /* renamed from: p1, reason: collision with root package name */
    private ConstraintWidget[] f14828p1 = null;

    /* renamed from: q1, reason: collision with root package name */
    private ConstraintWidget[] f14829q1 = null;

    /* renamed from: r1, reason: collision with root package name */
    private int[] f14830r1 = null;

    /* renamed from: t1, reason: collision with root package name */
    private int f14832t1 = 0;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Flow.java */
    /* loaded from: classes.dex */
    public class a {

        /* renamed from: a, reason: collision with root package name */
        private int f14833a;

        /* renamed from: d, reason: collision with root package name */
        private ConstraintAnchor f14836d;

        /* renamed from: e, reason: collision with root package name */
        private ConstraintAnchor f14837e;

        /* renamed from: f, reason: collision with root package name */
        private ConstraintAnchor f14838f;

        /* renamed from: g, reason: collision with root package name */
        private ConstraintAnchor f14839g;

        /* renamed from: h, reason: collision with root package name */
        private int f14840h;

        /* renamed from: i, reason: collision with root package name */
        private int f14841i;

        /* renamed from: j, reason: collision with root package name */
        private int f14842j;

        /* renamed from: k, reason: collision with root package name */
        private int f14843k;

        /* renamed from: q, reason: collision with root package name */
        private int f14849q;

        /* renamed from: b, reason: collision with root package name */
        private ConstraintWidget f14834b = null;

        /* renamed from: c, reason: collision with root package name */
        int f14835c = 0;

        /* renamed from: l, reason: collision with root package name */
        private int f14844l = 0;

        /* renamed from: m, reason: collision with root package name */
        private int f14845m = 0;

        /* renamed from: n, reason: collision with root package name */
        private int f14846n = 0;

        /* renamed from: o, reason: collision with root package name */
        private int f14847o = 0;

        /* renamed from: p, reason: collision with root package name */
        private int f14848p = 0;

        public a(int i10, ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, ConstraintAnchor constraintAnchor3, ConstraintAnchor constraintAnchor4, int i11) {
            this.f14840h = 0;
            this.f14841i = 0;
            this.f14842j = 0;
            this.f14843k = 0;
            this.f14849q = 0;
            this.f14833a = i10;
            this.f14836d = constraintAnchor;
            this.f14837e = constraintAnchor2;
            this.f14838f = constraintAnchor3;
            this.f14839g = constraintAnchor4;
            this.f14840h = g.this.Q0();
            this.f14841i = g.this.S0();
            this.f14842j = g.this.R0();
            this.f14843k = g.this.P0();
            this.f14849q = i11;
        }

        private void h() {
            this.f14844l = 0;
            this.f14845m = 0;
            this.f14834b = null;
            this.f14835c = 0;
            int i10 = this.f14847o;
            for (int i11 = 0; i11 < i10 && this.f14846n + i11 < g.this.f14832t1; i11++) {
                ConstraintWidget constraintWidget = g.this.f14831s1[this.f14846n + i11];
                if (this.f14833a == 0) {
                    int Q = constraintWidget.Q();
                    int i12 = g.this.f14820h1;
                    if (constraintWidget.P() == 8) {
                        i12 = 0;
                    }
                    this.f14844l += Q + i12;
                    int B1 = g.this.B1(constraintWidget, this.f14849q);
                    if (this.f14834b == null || this.f14835c < B1) {
                        this.f14834b = constraintWidget;
                        this.f14835c = B1;
                        this.f14845m = B1;
                    }
                } else {
                    int C1 = g.this.C1(constraintWidget, this.f14849q);
                    int B12 = g.this.B1(constraintWidget, this.f14849q);
                    int i13 = g.this.f14821i1;
                    if (constraintWidget.P() == 8) {
                        i13 = 0;
                    }
                    this.f14845m += B12 + i13;
                    if (this.f14834b == null || this.f14835c < C1) {
                        this.f14834b = constraintWidget;
                        this.f14835c = C1;
                        this.f14844l = C1;
                    }
                }
            }
        }

        public void b(ConstraintWidget constraintWidget) {
            if (this.f14833a == 0) {
                int C1 = g.this.C1(constraintWidget, this.f14849q);
                if (constraintWidget.z() == ConstraintWidget.b.MATCH_CONSTRAINT) {
                    this.f14848p++;
                    C1 = 0;
                }
                this.f14844l += C1 + (constraintWidget.P() != 8 ? g.this.f14820h1 : 0);
                int B1 = g.this.B1(constraintWidget, this.f14849q);
                if (this.f14834b == null || this.f14835c < B1) {
                    this.f14834b = constraintWidget;
                    this.f14835c = B1;
                    this.f14845m = B1;
                }
            } else {
                int C12 = g.this.C1(constraintWidget, this.f14849q);
                int B12 = g.this.B1(constraintWidget, this.f14849q);
                if (constraintWidget.N() == ConstraintWidget.b.MATCH_CONSTRAINT) {
                    this.f14848p++;
                    B12 = 0;
                }
                this.f14845m += B12 + (constraintWidget.P() != 8 ? g.this.f14821i1 : 0);
                if (this.f14834b == null || this.f14835c < C12) {
                    this.f14834b = constraintWidget;
                    this.f14835c = C12;
                    this.f14844l = C12;
                }
            }
            this.f14847o++;
        }

        public void c() {
            this.f14835c = 0;
            this.f14834b = null;
            this.f14844l = 0;
            this.f14845m = 0;
            this.f14846n = 0;
            this.f14847o = 0;
            this.f14848p = 0;
        }

        public void d(boolean z10, int i10, boolean z11) {
            ConstraintWidget constraintWidget;
            char c10;
            int i11 = this.f14847o;
            for (int i12 = 0; i12 < i11 && this.f14846n + i12 < g.this.f14832t1; i12++) {
                ConstraintWidget constraintWidget2 = g.this.f14831s1[this.f14846n + i12];
                if (constraintWidget2 != null) {
                    constraintWidget2.a0();
                }
            }
            if (i11 == 0 || this.f14834b == null) {
                return;
            }
            boolean z12 = z11 && i10 == 0;
            int i13 = -1;
            int i14 = -1;
            for (int i15 = 0; i15 < i11; i15++) {
                int i16 = z10 ? (i11 - 1) - i15 : i15;
                if (this.f14846n + i16 >= g.this.f14832t1) {
                    break;
                }
                if (g.this.f14831s1[this.f14846n + i16].P() == 0) {
                    if (i13 == -1) {
                        i13 = i15;
                    }
                    i14 = i15;
                }
            }
            ConstraintWidget constraintWidget3 = null;
            if (this.f14833a == 0) {
                ConstraintWidget constraintWidget4 = this.f14834b;
                constraintWidget4.z0(g.this.W0);
                int i17 = this.f14841i;
                if (i10 > 0) {
                    i17 += g.this.f14821i1;
                }
                constraintWidget4.E.a(this.f14837e, i17);
                if (z11) {
                    constraintWidget4.G.a(this.f14839g, this.f14843k);
                }
                if (i10 > 0) {
                    this.f14837e.f14733b.G.a(constraintWidget4.E, 0);
                }
                if (g.this.f14823k1 == 3 && !constraintWidget4.T()) {
                    for (int i18 = 0; i18 < i11; i18++) {
                        int i19 = z10 ? (i11 - 1) - i18 : i18;
                        if (this.f14846n + i19 >= g.this.f14832t1) {
                            break;
                        }
                        constraintWidget = g.this.f14831s1[this.f14846n + i19];
                        if (constraintWidget.T()) {
                            break;
                        }
                    }
                }
                constraintWidget = constraintWidget4;
                int i20 = 0;
                while (i20 < i11) {
                    int i21 = z10 ? (i11 - 1) - i20 : i20;
                    if (this.f14846n + i21 >= g.this.f14832t1) {
                        return;
                    }
                    ConstraintWidget constraintWidget5 = g.this.f14831s1[this.f14846n + i21];
                    if (i20 == 0) {
                        constraintWidget5.j(constraintWidget5.D, this.f14836d, this.f14840h);
                    }
                    if (i21 == 0) {
                        int i22 = g.this.V0;
                        float f10 = g.this.f14814b1;
                        if (this.f14846n == 0 && g.this.X0 != -1) {
                            i22 = g.this.X0;
                            f10 = g.this.f14816d1;
                        } else if (z11 && g.this.Z0 != -1) {
                            i22 = g.this.Z0;
                            f10 = g.this.f14818f1;
                        }
                        constraintWidget5.k0(i22);
                        constraintWidget5.j0(f10);
                    }
                    if (i20 == i11 - 1) {
                        constraintWidget5.j(constraintWidget5.F, this.f14838f, this.f14842j);
                    }
                    if (constraintWidget3 != null) {
                        constraintWidget5.D.a(constraintWidget3.F, g.this.f14820h1);
                        if (i20 == i13) {
                            constraintWidget5.D.n(this.f14840h);
                        }
                        constraintWidget3.F.a(constraintWidget5.D, 0);
                        if (i20 == i14 + 1) {
                            constraintWidget3.F.n(this.f14842j);
                        }
                    }
                    if (constraintWidget5 != constraintWidget4) {
                        c10 = 3;
                        if (g.this.f14823k1 == 3 && constraintWidget.T() && constraintWidget5 != constraintWidget && constraintWidget5.T()) {
                            constraintWidget5.H.a(constraintWidget.H, 0);
                        } else {
                            int i23 = g.this.f14823k1;
                            if (i23 == 0) {
                                constraintWidget5.E.a(constraintWidget4.E, 0);
                            } else if (i23 == 1) {
                                constraintWidget5.G.a(constraintWidget4.G, 0);
                            } else if (z12) {
                                constraintWidget5.E.a(this.f14837e, this.f14841i);
                                constraintWidget5.G.a(this.f14839g, this.f14843k);
                            } else {
                                constraintWidget5.E.a(constraintWidget4.E, 0);
                                constraintWidget5.G.a(constraintWidget4.G, 0);
                            }
                        }
                    } else {
                        c10 = 3;
                    }
                    i20++;
                    constraintWidget3 = constraintWidget5;
                }
                return;
            }
            ConstraintWidget constraintWidget6 = this.f14834b;
            constraintWidget6.k0(g.this.V0);
            int i24 = this.f14840h;
            if (i10 > 0) {
                i24 += g.this.f14820h1;
            }
            if (z10) {
                constraintWidget6.F.a(this.f14838f, i24);
                if (z11) {
                    constraintWidget6.D.a(this.f14836d, this.f14842j);
                }
                if (i10 > 0) {
                    this.f14838f.f14733b.D.a(constraintWidget6.F, 0);
                }
            } else {
                constraintWidget6.D.a(this.f14836d, i24);
                if (z11) {
                    constraintWidget6.F.a(this.f14838f, this.f14842j);
                }
                if (i10 > 0) {
                    this.f14836d.f14733b.F.a(constraintWidget6.D, 0);
                }
            }
            int i25 = 0;
            while (i25 < i11 && this.f14846n + i25 < g.this.f14832t1) {
                ConstraintWidget constraintWidget7 = g.this.f14831s1[this.f14846n + i25];
                if (i25 == 0) {
                    constraintWidget7.j(constraintWidget7.E, this.f14837e, this.f14841i);
                    int i26 = g.this.W0;
                    float f11 = g.this.f14815c1;
                    if (this.f14846n == 0 && g.this.Y0 != -1) {
                        i26 = g.this.Y0;
                        f11 = g.this.f14817e1;
                    } else if (z11 && g.this.f14813a1 != -1) {
                        i26 = g.this.f14813a1;
                        f11 = g.this.f14819g1;
                    }
                    constraintWidget7.z0(i26);
                    constraintWidget7.y0(f11);
                }
                if (i25 == i11 - 1) {
                    constraintWidget7.j(constraintWidget7.G, this.f14839g, this.f14843k);
                }
                if (constraintWidget3 != null) {
                    constraintWidget7.E.a(constraintWidget3.G, g.this.f14821i1);
                    if (i25 == i13) {
                        constraintWidget7.E.n(this.f14841i);
                    }
                    constraintWidget3.G.a(constraintWidget7.E, 0);
                    if (i25 == i14 + 1) {
                        constraintWidget3.G.n(this.f14843k);
                    }
                }
                if (constraintWidget7 != constraintWidget6) {
                    if (z10) {
                        int i27 = g.this.f14822j1;
                        if (i27 == 0) {
                            constraintWidget7.F.a(constraintWidget6.F, 0);
                        } else if (i27 == 1) {
                            constraintWidget7.D.a(constraintWidget6.D, 0);
                        } else if (i27 == 2) {
                            constraintWidget7.D.a(constraintWidget6.D, 0);
                            constraintWidget7.F.a(constraintWidget6.F, 0);
                        }
                    } else {
                        int i28 = g.this.f14822j1;
                        if (i28 == 0) {
                            constraintWidget7.D.a(constraintWidget6.D, 0);
                        } else if (i28 == 1) {
                            constraintWidget7.F.a(constraintWidget6.F, 0);
                        } else if (i28 == 2) {
                            if (z12) {
                                constraintWidget7.D.a(this.f14836d, this.f14840h);
                                constraintWidget7.F.a(this.f14838f, this.f14842j);
                            } else {
                                constraintWidget7.D.a(constraintWidget6.D, 0);
                                constraintWidget7.F.a(constraintWidget6.F, 0);
                            }
                        }
                        i25++;
                        constraintWidget3 = constraintWidget7;
                    }
                }
                i25++;
                constraintWidget3 = constraintWidget7;
            }
        }

        public int e() {
            if (this.f14833a == 1) {
                return this.f14845m - g.this.f14821i1;
            }
            return this.f14845m;
        }

        public int f() {
            if (this.f14833a == 0) {
                return this.f14844l - g.this.f14820h1;
            }
            return this.f14844l;
        }

        public void g(int i10) {
            int i11 = this.f14848p;
            if (i11 == 0) {
                return;
            }
            int i12 = this.f14847o;
            int i13 = i10 / i11;
            for (int i14 = 0; i14 < i12 && this.f14846n + i14 < g.this.f14832t1; i14++) {
                ConstraintWidget constraintWidget = g.this.f14831s1[this.f14846n + i14];
                if (this.f14833a == 0) {
                    if (constraintWidget != null && constraintWidget.z() == ConstraintWidget.b.MATCH_CONSTRAINT && constraintWidget.f14772l == 0) {
                        g.this.U0(constraintWidget, ConstraintWidget.b.FIXED, i13, constraintWidget.N(), constraintWidget.w());
                    }
                } else if (constraintWidget != null && constraintWidget.N() == ConstraintWidget.b.MATCH_CONSTRAINT && constraintWidget.f14774m == 0) {
                    g.this.U0(constraintWidget, constraintWidget.z(), constraintWidget.Q(), ConstraintWidget.b.FIXED, i13);
                }
            }
            h();
        }

        public void i(int i10) {
            this.f14846n = i10;
        }

        public void j(int i10, ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, ConstraintAnchor constraintAnchor3, ConstraintAnchor constraintAnchor4, int i11, int i12, int i13, int i14, int i15) {
            this.f14833a = i10;
            this.f14836d = constraintAnchor;
            this.f14837e = constraintAnchor2;
            this.f14838f = constraintAnchor3;
            this.f14839g = constraintAnchor4;
            this.f14840h = i11;
            this.f14841i = i12;
            this.f14842j = i13;
            this.f14843k = i14;
            this.f14849q = i15;
        }
    }

    private void A1(boolean z10) {
        ConstraintWidget constraintWidget;
        if (this.f14830r1 == null || this.f14829q1 == null || this.f14828p1 == null) {
            return;
        }
        for (int i10 = 0; i10 < this.f14832t1; i10++) {
            this.f14831s1[i10].a0();
        }
        int[] iArr = this.f14830r1;
        int i11 = iArr[0];
        int i12 = iArr[1];
        ConstraintWidget constraintWidget2 = null;
        for (int i13 = 0; i13 < i11; i13++) {
            ConstraintWidget constraintWidget3 = this.f14829q1[z10 ? (i11 - i13) - 1 : i13];
            if (constraintWidget3 != null && constraintWidget3.P() != 8) {
                if (i13 == 0) {
                    constraintWidget3.j(constraintWidget3.D, this.D, Q0());
                    constraintWidget3.k0(this.V0);
                    constraintWidget3.j0(this.f14814b1);
                }
                if (i13 == i11 - 1) {
                    constraintWidget3.j(constraintWidget3.F, this.F, R0());
                }
                if (i13 > 0) {
                    constraintWidget3.j(constraintWidget3.D, constraintWidget2.F, this.f14820h1);
                    constraintWidget2.j(constraintWidget2.F, constraintWidget3.D, 0);
                }
                constraintWidget2 = constraintWidget3;
            }
        }
        for (int i14 = 0; i14 < i12; i14++) {
            ConstraintWidget constraintWidget4 = this.f14828p1[i14];
            if (constraintWidget4 != null && constraintWidget4.P() != 8) {
                if (i14 == 0) {
                    constraintWidget4.j(constraintWidget4.E, this.E, S0());
                    constraintWidget4.z0(this.W0);
                    constraintWidget4.y0(this.f14815c1);
                }
                if (i14 == i12 - 1) {
                    constraintWidget4.j(constraintWidget4.G, this.G, P0());
                }
                if (i14 > 0) {
                    constraintWidget4.j(constraintWidget4.E, constraintWidget2.G, this.f14821i1);
                    constraintWidget2.j(constraintWidget2.G, constraintWidget4.E, 0);
                }
                constraintWidget2 = constraintWidget4;
            }
        }
        for (int i15 = 0; i15 < i11; i15++) {
            for (int i16 = 0; i16 < i12; i16++) {
                int i17 = (i16 * i11) + i15;
                if (this.f14826n1 == 1) {
                    i17 = (i15 * i12) + i16;
                }
                ConstraintWidget[] constraintWidgetArr = this.f14831s1;
                if (i17 < constraintWidgetArr.length && (constraintWidget = constraintWidgetArr[i17]) != null && constraintWidget.P() != 8) {
                    ConstraintWidget constraintWidget5 = this.f14829q1[i15];
                    ConstraintWidget constraintWidget6 = this.f14828p1[i16];
                    if (constraintWidget != constraintWidget5) {
                        constraintWidget.j(constraintWidget.D, constraintWidget5.D, 0);
                        constraintWidget.j(constraintWidget.F, constraintWidget5.F, 0);
                    }
                    if (constraintWidget != constraintWidget6) {
                        constraintWidget.j(constraintWidget.E, constraintWidget6.E, 0);
                        constraintWidget.j(constraintWidget.G, constraintWidget6.G, 0);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int B1(ConstraintWidget constraintWidget, int i10) {
        if (constraintWidget == null) {
            return 0;
        }
        if (constraintWidget.N() == ConstraintWidget.b.MATCH_CONSTRAINT) {
            int i11 = constraintWidget.f14774m;
            if (i11 == 0) {
                return 0;
            }
            if (i11 == 2) {
                int i12 = (int) (constraintWidget.f14788t * i10);
                if (i12 != constraintWidget.w()) {
                    U0(constraintWidget, constraintWidget.z(), constraintWidget.Q(), ConstraintWidget.b.FIXED, i12);
                }
                return i12;
            }
            if (i11 == 1) {
                return constraintWidget.w();
            }
            if (i11 == 3) {
                return (int) ((constraintWidget.Q() * constraintWidget.S) + 0.5f);
            }
        }
        return constraintWidget.w();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int C1(ConstraintWidget constraintWidget, int i10) {
        if (constraintWidget == null) {
            return 0;
        }
        if (constraintWidget.z() == ConstraintWidget.b.MATCH_CONSTRAINT) {
            int i11 = constraintWidget.f14772l;
            if (i11 == 0) {
                return 0;
            }
            if (i11 == 2) {
                int i12 = (int) (constraintWidget.f14782q * i10);
                if (i12 != constraintWidget.Q()) {
                    U0(constraintWidget, ConstraintWidget.b.FIXED, i12, constraintWidget.N(), constraintWidget.w());
                }
                return i12;
            }
            if (i11 == 1) {
                return constraintWidget.Q();
            }
            if (i11 == 3) {
                return (int) ((constraintWidget.w() * constraintWidget.S) + 0.5f);
            }
        }
        return constraintWidget.Q();
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0068  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:77:0x011b -> B:22:0x0063). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:78:0x011d -> B:22:0x0063). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:80:0x0123 -> B:22:0x0063). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:81:0x0125 -> B:22:0x0063). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void D1(ConstraintWidget[] constraintWidgetArr, int i10, int i11, int i12, int[] iArr) {
        int i13;
        int i14;
        boolean z10;
        ConstraintWidget constraintWidget;
        if (i11 == 0) {
            int i15 = this.f14825m1;
            if (i15 <= 0) {
                i15 = 0;
                int i16 = 0;
                for (int i17 = 0; i17 < i10; i17++) {
                    if (i17 > 0) {
                        i16 += this.f14820h1;
                    }
                    ConstraintWidget constraintWidget2 = constraintWidgetArr[i17];
                    if (constraintWidget2 != null) {
                        i16 += C1(constraintWidget2, i12);
                        if (i16 > i12) {
                            break;
                        } else {
                            i15++;
                        }
                    }
                }
            }
            i14 = i15;
            i13 = 0;
        } else {
            i13 = this.f14825m1;
            if (i13 <= 0) {
                i13 = 0;
                int i18 = 0;
                for (int i19 = 0; i19 < i10; i19++) {
                    if (i19 > 0) {
                        i18 += this.f14821i1;
                    }
                    ConstraintWidget constraintWidget3 = constraintWidgetArr[i19];
                    if (constraintWidget3 != null) {
                        i18 += B1(constraintWidget3, i12);
                        if (i18 > i12) {
                            break;
                        } else {
                            i13++;
                        }
                    }
                }
            }
            i14 = 0;
        }
        if (this.f14830r1 == null) {
            this.f14830r1 = new int[2];
        }
        if ((i13 != 0 || i11 != 1) && (i14 != 0 || i11 != 0)) {
            z10 = false;
            while (!z10) {
                if (i11 == 0) {
                    i13 = (int) Math.ceil(i10 / i14);
                } else {
                    i14 = (int) Math.ceil(i10 / i13);
                }
                ConstraintWidget[] constraintWidgetArr2 = this.f14829q1;
                if (constraintWidgetArr2 != null && constraintWidgetArr2.length >= i14) {
                    Arrays.fill(constraintWidgetArr2, (Object) null);
                } else {
                    this.f14829q1 = new ConstraintWidget[i14];
                }
                ConstraintWidget[] constraintWidgetArr3 = this.f14828p1;
                if (constraintWidgetArr3 != null && constraintWidgetArr3.length >= i13) {
                    Arrays.fill(constraintWidgetArr3, (Object) null);
                } else {
                    this.f14828p1 = new ConstraintWidget[i13];
                }
                for (int i20 = 0; i20 < i14; i20++) {
                    for (int i21 = 0; i21 < i13; i21++) {
                        int i22 = (i21 * i14) + i20;
                        if (i11 == 1) {
                            i22 = (i20 * i13) + i21;
                        }
                        if (i22 < constraintWidgetArr.length && (constraintWidget = constraintWidgetArr[i22]) != null) {
                            int C1 = C1(constraintWidget, i12);
                            ConstraintWidget[] constraintWidgetArr4 = this.f14829q1;
                            if (constraintWidgetArr4[i20] == null || constraintWidgetArr4[i20].Q() < C1) {
                                this.f14829q1[i20] = constraintWidget;
                            }
                            int B1 = B1(constraintWidget, i12);
                            ConstraintWidget[] constraintWidgetArr5 = this.f14828p1;
                            if (constraintWidgetArr5[i21] == null || constraintWidgetArr5[i21].w() < B1) {
                                this.f14828p1[i21] = constraintWidget;
                            }
                        }
                    }
                }
                int i23 = 0;
                for (int i24 = 0; i24 < i14; i24++) {
                    ConstraintWidget constraintWidget4 = this.f14829q1[i24];
                    if (constraintWidget4 != null) {
                        if (i24 > 0) {
                            i23 += this.f14820h1;
                        }
                        i23 += C1(constraintWidget4, i12);
                    }
                }
                int i25 = 0;
                for (int i26 = 0; i26 < i13; i26++) {
                    ConstraintWidget constraintWidget5 = this.f14828p1[i26];
                    if (constraintWidget5 != null) {
                        if (i26 > 0) {
                            i25 += this.f14821i1;
                        }
                        i25 += B1(constraintWidget5, i12);
                    }
                }
                iArr[0] = i23;
                iArr[1] = i25;
                if (i11 != 0) {
                    if (i25 > i12 && i13 > 1) {
                        i13--;
                    }
                } else if (i23 > i12 && i14 > 1) {
                    i14--;
                }
                while (!z10) {
                }
            }
            int[] iArr2 = this.f14830r1;
            iArr2[0] = i14;
            iArr2[1] = i13;
        }
        z10 = true;
        while (!z10) {
        }
        int[] iArr22 = this.f14830r1;
        iArr22[0] = i14;
        iArr22[1] = i13;
    }

    private void E1(ConstraintWidget[] constraintWidgetArr, int i10, int i11, int i12, int[] iArr) {
        int i13;
        int i14;
        int i15;
        ConstraintAnchor constraintAnchor;
        int R0;
        ConstraintAnchor constraintAnchor2;
        int P0;
        int i16;
        if (i10 == 0) {
            return;
        }
        this.f14827o1.clear();
        a aVar = new a(i11, this.D, this.E, this.F, this.G, i12);
        this.f14827o1.add(aVar);
        if (i11 == 0) {
            i13 = 0;
            int i17 = 0;
            int i18 = 0;
            while (i18 < i10) {
                ConstraintWidget constraintWidget = constraintWidgetArr[i18];
                int C1 = C1(constraintWidget, i12);
                if (constraintWidget.z() == ConstraintWidget.b.MATCH_CONSTRAINT) {
                    i13++;
                }
                int i19 = i13;
                boolean z10 = (i17 == i12 || (this.f14820h1 + i17) + C1 > i12) && aVar.f14834b != null;
                if (!z10 && i18 > 0 && (i16 = this.f14825m1) > 0 && i18 % i16 == 0) {
                    z10 = true;
                }
                if (z10) {
                    aVar = new a(i11, this.D, this.E, this.F, this.G, i12);
                    aVar.i(i18);
                    this.f14827o1.add(aVar);
                } else if (i18 > 0) {
                    i17 += this.f14820h1 + C1;
                    aVar.b(constraintWidget);
                    i18++;
                    i13 = i19;
                }
                i17 = C1;
                aVar.b(constraintWidget);
                i18++;
                i13 = i19;
            }
        } else {
            i13 = 0;
            int i20 = 0;
            int i21 = 0;
            while (i21 < i10) {
                ConstraintWidget constraintWidget2 = constraintWidgetArr[i21];
                int B1 = B1(constraintWidget2, i12);
                if (constraintWidget2.N() == ConstraintWidget.b.MATCH_CONSTRAINT) {
                    i13++;
                }
                int i22 = i13;
                boolean z11 = (i20 == i12 || (this.f14821i1 + i20) + B1 > i12) && aVar.f14834b != null;
                if (!z11 && i21 > 0 && (i14 = this.f14825m1) > 0 && i21 % i14 == 0) {
                    z11 = true;
                }
                if (z11) {
                    aVar = new a(i11, this.D, this.E, this.F, this.G, i12);
                    aVar.i(i21);
                    this.f14827o1.add(aVar);
                } else if (i21 > 0) {
                    i20 += this.f14821i1 + B1;
                    aVar.b(constraintWidget2);
                    i21++;
                    i13 = i22;
                }
                i20 = B1;
                aVar.b(constraintWidget2);
                i21++;
                i13 = i22;
            }
        }
        int size = this.f14827o1.size();
        ConstraintAnchor constraintAnchor3 = this.D;
        ConstraintAnchor constraintAnchor4 = this.E;
        ConstraintAnchor constraintAnchor5 = this.F;
        ConstraintAnchor constraintAnchor6 = this.G;
        int Q0 = Q0();
        int S0 = S0();
        int R02 = R0();
        int P02 = P0();
        ConstraintWidget.b z12 = z();
        ConstraintWidget.b bVar = ConstraintWidget.b.WRAP_CONTENT;
        boolean z13 = z12 == bVar || N() == bVar;
        if (i13 > 0 && z13) {
            for (int i23 = 0; i23 < size; i23++) {
                a aVar2 = this.f14827o1.get(i23);
                if (i11 == 0) {
                    aVar2.g(i12 - aVar2.f());
                } else {
                    aVar2.g(i12 - aVar2.e());
                }
            }
        }
        int i24 = S0;
        int i25 = R02;
        int i26 = 0;
        int i27 = 0;
        int i28 = 0;
        int i29 = Q0;
        ConstraintAnchor constraintAnchor7 = constraintAnchor4;
        ConstraintAnchor constraintAnchor8 = constraintAnchor3;
        int i30 = P02;
        while (i28 < size) {
            a aVar3 = this.f14827o1.get(i28);
            if (i11 == 0) {
                if (i28 < size - 1) {
                    constraintAnchor2 = this.f14827o1.get(i28 + 1).f14834b.E;
                    P0 = 0;
                } else {
                    constraintAnchor2 = this.G;
                    P0 = P0();
                }
                ConstraintAnchor constraintAnchor9 = aVar3.f14834b.G;
                ConstraintAnchor constraintAnchor10 = constraintAnchor8;
                ConstraintAnchor constraintAnchor11 = constraintAnchor8;
                int i31 = i26;
                ConstraintAnchor constraintAnchor12 = constraintAnchor7;
                int i32 = i27;
                ConstraintAnchor constraintAnchor13 = constraintAnchor5;
                ConstraintAnchor constraintAnchor14 = constraintAnchor5;
                i15 = i28;
                aVar3.j(i11, constraintAnchor10, constraintAnchor12, constraintAnchor13, constraintAnchor2, i29, i24, i25, P0, i12);
                int max = Math.max(i32, aVar3.f());
                i26 = i31 + aVar3.e();
                if (i15 > 0) {
                    i26 += this.f14821i1;
                }
                constraintAnchor8 = constraintAnchor11;
                i27 = max;
                i24 = 0;
                constraintAnchor7 = constraintAnchor9;
                constraintAnchor = constraintAnchor14;
                int i33 = P0;
                constraintAnchor6 = constraintAnchor2;
                i30 = i33;
            } else {
                ConstraintAnchor constraintAnchor15 = constraintAnchor8;
                int i34 = i26;
                int i35 = i27;
                i15 = i28;
                if (i15 < size - 1) {
                    constraintAnchor = this.f14827o1.get(i15 + 1).f14834b.D;
                    R0 = 0;
                } else {
                    constraintAnchor = this.F;
                    R0 = R0();
                }
                ConstraintAnchor constraintAnchor16 = aVar3.f14834b.F;
                aVar3.j(i11, constraintAnchor15, constraintAnchor7, constraintAnchor, constraintAnchor6, i29, i24, R0, i30, i12);
                i27 = i35 + aVar3.f();
                int max2 = Math.max(i34, aVar3.e());
                if (i15 > 0) {
                    i27 += this.f14820h1;
                }
                i26 = max2;
                i29 = 0;
                i25 = R0;
                constraintAnchor8 = constraintAnchor16;
            }
            i28 = i15 + 1;
            constraintAnchor5 = constraintAnchor;
        }
        iArr[0] = i27;
        iArr[1] = i26;
    }

    private void F1(ConstraintWidget[] constraintWidgetArr, int i10, int i11, int i12, int[] iArr) {
        a aVar;
        if (i10 == 0) {
            return;
        }
        if (this.f14827o1.size() == 0) {
            aVar = new a(i11, this.D, this.E, this.F, this.G, i12);
            this.f14827o1.add(aVar);
        } else {
            a aVar2 = this.f14827o1.get(0);
            aVar2.c();
            aVar = aVar2;
            aVar.j(i11, this.D, this.E, this.F, this.G, Q0(), S0(), R0(), P0(), i12);
        }
        for (int i13 = 0; i13 < i10; i13++) {
            aVar.b(constraintWidgetArr[i13]);
        }
        iArr[0] = aVar.f();
        iArr[1] = aVar.e();
    }

    public void G1(float f10) {
        this.f14816d1 = f10;
    }

    public void H1(int i10) {
        this.X0 = i10;
    }

    public void I1(float f10) {
        this.f14817e1 = f10;
    }

    public void J1(int i10) {
        this.Y0 = i10;
    }

    public void K1(int i10) {
        this.f14822j1 = i10;
    }

    public void L1(float f10) {
        this.f14814b1 = f10;
    }

    public void M1(int i10) {
        this.f14820h1 = i10;
    }

    public void N1(int i10) {
        this.V0 = i10;
    }

    public void O1(float f10) {
        this.f14818f1 = f10;
    }

    public void P1(int i10) {
        this.Z0 = i10;
    }

    public void Q1(float f10) {
        this.f14819g1 = f10;
    }

    public void R1(int i10) {
        this.f14813a1 = i10;
    }

    public void S1(int i10) {
        this.f14825m1 = i10;
    }

    @Override // m.l
    public void T0(int i10, int i11, int i12, int i13) {
        int i14;
        int i15;
        int[] iArr;
        boolean z10;
        if (this.H0 > 0 && !V0()) {
            Y0(0, 0);
            X0(false);
            return;
        }
        int Q0 = Q0();
        int R0 = R0();
        int S0 = S0();
        int P0 = P0();
        int[] iArr2 = new int[2];
        int i16 = (i11 - Q0) - R0;
        int i17 = this.f14826n1;
        if (i17 == 1) {
            i16 = (i13 - S0) - P0;
        }
        int i18 = i16;
        if (i17 == 0) {
            if (this.V0 == -1) {
                this.V0 = 0;
            }
            if (this.W0 == -1) {
                this.W0 = 0;
            }
        } else {
            if (this.V0 == -1) {
                this.V0 = 0;
            }
            if (this.W0 == -1) {
                this.W0 = 0;
            }
        }
        ConstraintWidget[] constraintWidgetArr = this.G0;
        int i19 = 0;
        int i20 = 0;
        while (true) {
            i14 = this.H0;
            if (i19 >= i14) {
                break;
            }
            if (this.G0[i19].P() == 8) {
                i20++;
            }
            i19++;
        }
        if (i20 > 0) {
            constraintWidgetArr = new ConstraintWidget[i14 - i20];
            int i21 = 0;
            for (int i22 = 0; i22 < this.H0; i22++) {
                ConstraintWidget constraintWidget = this.G0[i22];
                if (constraintWidget.P() != 8) {
                    constraintWidgetArr[i21] = constraintWidget;
                    i21++;
                }
            }
            i15 = i21;
        } else {
            i15 = i14;
        }
        this.f14831s1 = constraintWidgetArr;
        this.f14832t1 = i15;
        int i23 = this.f14824l1;
        if (i23 == 0) {
            iArr = iArr2;
            z10 = true;
            F1(constraintWidgetArr, i15, this.f14826n1, i18, iArr2);
        } else if (i23 == 1) {
            z10 = true;
            iArr = iArr2;
            E1(constraintWidgetArr, i15, this.f14826n1, i18, iArr2);
        } else if (i23 != 2) {
            z10 = true;
            iArr = iArr2;
        } else {
            z10 = true;
            iArr = iArr2;
            D1(constraintWidgetArr, i15, this.f14826n1, i18, iArr2);
        }
        int i24 = iArr[0] + Q0 + R0;
        int i25 = iArr[z10 ? 1 : 0] + S0 + P0;
        if (i10 == 1073741824) {
            i24 = i11;
        } else if (i10 == Integer.MIN_VALUE) {
            i24 = Math.min(i24, i11);
        } else if (i10 != 0) {
            i24 = 0;
        }
        if (i12 == 1073741824) {
            i25 = i13;
        } else if (i12 == Integer.MIN_VALUE) {
            i25 = Math.min(i25, i13);
        } else if (i12 != 0) {
            i25 = 0;
        }
        Y0(i24, i25);
        F0(i24);
        i0(i25);
        if (this.H0 <= 0) {
            z10 = false;
        }
        X0(z10);
    }

    public void T1(int i10) {
        this.f14826n1 = i10;
    }

    public void U1(int i10) {
        this.f14823k1 = i10;
    }

    public void V1(float f10) {
        this.f14815c1 = f10;
    }

    public void W1(int i10) {
        this.f14821i1 = i10;
    }

    public void X1(int i10) {
        this.W0 = i10;
    }

    public void Y1(int i10) {
        this.f14824l1 = i10;
    }

    @Override // m.ConstraintWidget
    public void f(LinearSystem linearSystem) {
        super.f(linearSystem);
        boolean c12 = H() != null ? ((ConstraintWidgetContainer) H()).c1() : false;
        int i10 = this.f14824l1;
        if (i10 != 0) {
            if (i10 == 1) {
                int size = this.f14827o1.size();
                int i11 = 0;
                while (i11 < size) {
                    this.f14827o1.get(i11).d(c12, i11, i11 == size + (-1));
                    i11++;
                }
            } else if (i10 == 2) {
                A1(c12);
            }
        } else if (this.f14827o1.size() > 0) {
            this.f14827o1.get(0).d(c12, 0, true);
        }
        X0(false);
    }

    @Override // m.HelperWidget, m.ConstraintWidget
    public void l(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.l(constraintWidget, hashMap);
        g gVar = (g) constraintWidget;
        this.V0 = gVar.V0;
        this.W0 = gVar.W0;
        this.X0 = gVar.X0;
        this.Y0 = gVar.Y0;
        this.Z0 = gVar.Z0;
        this.f14813a1 = gVar.f14813a1;
        this.f14814b1 = gVar.f14814b1;
        this.f14815c1 = gVar.f14815c1;
        this.f14816d1 = gVar.f14816d1;
        this.f14817e1 = gVar.f14817e1;
        this.f14818f1 = gVar.f14818f1;
        this.f14819g1 = gVar.f14819g1;
        this.f14820h1 = gVar.f14820h1;
        this.f14821i1 = gVar.f14821i1;
        this.f14822j1 = gVar.f14822j1;
        this.f14823k1 = gVar.f14823k1;
        this.f14824l1 = gVar.f14824l1;
        this.f14825m1 = gVar.f14825m1;
        this.f14826n1 = gVar.f14826n1;
    }
}
