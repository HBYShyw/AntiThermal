package n;

import m.ConstraintAnchor;
import m.ConstraintWidget;
import m.Helper;
import n.DependencyNode;
import n.WidgetRun;

/* compiled from: HorizontalWidgetRun.java */
/* renamed from: n.j, reason: use source file name */
/* loaded from: classes.dex */
public class HorizontalWidgetRun extends WidgetRun {

    /* renamed from: k, reason: collision with root package name */
    private static int[] f15592k = new int[2];

    /* compiled from: HorizontalWidgetRun.java */
    /* renamed from: n.j$a */
    /* loaded from: classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f15593a;

        static {
            int[] iArr = new int[WidgetRun.b.values().length];
            f15593a = iArr;
            try {
                iArr[WidgetRun.b.START.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f15593a[WidgetRun.b.END.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f15593a[WidgetRun.b.CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public HorizontalWidgetRun(ConstraintWidget constraintWidget) {
        super(constraintWidget);
        this.f15612h.f15574e = DependencyNode.a.LEFT;
        this.f15613i.f15574e = DependencyNode.a.RIGHT;
        this.f15610f = 0;
    }

    private void q(int[] iArr, int i10, int i11, int i12, int i13, float f10, int i14) {
        int i15 = i11 - i10;
        int i16 = i13 - i12;
        if (i14 != -1) {
            if (i14 == 0) {
                iArr[0] = (int) ((i16 * f10) + 0.5f);
                iArr[1] = i16;
                return;
            } else {
                if (i14 != 1) {
                    return;
                }
                iArr[0] = i15;
                iArr[1] = (int) ((i15 * f10) + 0.5f);
                return;
            }
        }
        int i17 = (int) ((i16 * f10) + 0.5f);
        int i18 = (int) ((i15 / f10) + 0.5f);
        if (i17 <= i15) {
            iArr[0] = i17;
            iArr[1] = i16;
        } else if (i18 <= i16) {
            iArr[0] = i15;
            iArr[1] = i18;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:111:0x02b9, code lost:
    
        if (r14 != 1) goto L135;
     */
    @Override // n.WidgetRun, n.Dependency
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void a(Dependency dependency) {
        float f10;
        float u7;
        float f11;
        int i10;
        int i11 = a.f15593a[this.f15614j.ordinal()];
        if (i11 == 1) {
            p(dependency);
        } else if (i11 == 2) {
            o(dependency);
        } else if (i11 == 3) {
            ConstraintWidget constraintWidget = this.f15606b;
            n(dependency, constraintWidget.D, constraintWidget.F, 0);
            return;
        }
        if (!this.f15609e.f15579j && this.f15608d == ConstraintWidget.b.MATCH_CONSTRAINT) {
            ConstraintWidget constraintWidget2 = this.f15606b;
            int i12 = constraintWidget2.f14772l;
            if (i12 == 2) {
                ConstraintWidget H = constraintWidget2.H();
                if (H != null) {
                    if (H.f14758e.f15609e.f15579j) {
                        this.f15609e.d((int) ((r0.f15576g * this.f15606b.f14782q) + 0.5f));
                    }
                }
            } else if (i12 == 3) {
                int i13 = constraintWidget2.f14774m;
                if (i13 != 0 && i13 != 3) {
                    int v7 = constraintWidget2.v();
                    if (v7 == -1) {
                        ConstraintWidget constraintWidget3 = this.f15606b;
                        f10 = constraintWidget3.f14760f.f15609e.f15576g;
                        u7 = constraintWidget3.u();
                    } else if (v7 == 0) {
                        f11 = r0.f14760f.f15609e.f15576g / this.f15606b.u();
                        i10 = (int) (f11 + 0.5f);
                        this.f15609e.d(i10);
                    } else if (v7 == 1) {
                        ConstraintWidget constraintWidget4 = this.f15606b;
                        f10 = constraintWidget4.f14760f.f15609e.f15576g;
                        u7 = constraintWidget4.u();
                    } else {
                        i10 = 0;
                        this.f15609e.d(i10);
                    }
                    f11 = f10 * u7;
                    i10 = (int) (f11 + 0.5f);
                    this.f15609e.d(i10);
                } else {
                    VerticalWidgetRun verticalWidgetRun = constraintWidget2.f14760f;
                    DependencyNode dependencyNode = verticalWidgetRun.f15612h;
                    DependencyNode dependencyNode2 = verticalWidgetRun.f15613i;
                    boolean z10 = constraintWidget2.D.f14735d != null;
                    boolean z11 = constraintWidget2.E.f14735d != null;
                    boolean z12 = constraintWidget2.F.f14735d != null;
                    boolean z13 = constraintWidget2.G.f14735d != null;
                    int v10 = constraintWidget2.v();
                    if (z10 && z11 && z12 && z13) {
                        float u10 = this.f15606b.u();
                        if (dependencyNode.f15579j && dependencyNode2.f15579j) {
                            DependencyNode dependencyNode3 = this.f15612h;
                            if (dependencyNode3.f15572c && this.f15613i.f15572c) {
                                q(f15592k, dependencyNode3.f15581l.get(0).f15576g + this.f15612h.f15575f, this.f15613i.f15581l.get(0).f15576g - this.f15613i.f15575f, dependencyNode.f15576g + dependencyNode.f15575f, dependencyNode2.f15576g - dependencyNode2.f15575f, u10, v10);
                                this.f15609e.d(f15592k[0]);
                                this.f15606b.f14760f.f15609e.d(f15592k[1]);
                                return;
                            }
                            return;
                        }
                        DependencyNode dependencyNode4 = this.f15612h;
                        if (dependencyNode4.f15579j) {
                            DependencyNode dependencyNode5 = this.f15613i;
                            if (dependencyNode5.f15579j) {
                                if (!dependencyNode.f15572c || !dependencyNode2.f15572c) {
                                    return;
                                }
                                q(f15592k, dependencyNode4.f15576g + dependencyNode4.f15575f, dependencyNode5.f15576g - dependencyNode5.f15575f, dependencyNode.f15581l.get(0).f15576g + dependencyNode.f15575f, dependencyNode2.f15581l.get(0).f15576g - dependencyNode2.f15575f, u10, v10);
                                this.f15609e.d(f15592k[0]);
                                this.f15606b.f14760f.f15609e.d(f15592k[1]);
                            }
                        }
                        DependencyNode dependencyNode6 = this.f15612h;
                        if (!dependencyNode6.f15572c || !this.f15613i.f15572c || !dependencyNode.f15572c || !dependencyNode2.f15572c) {
                            return;
                        }
                        q(f15592k, dependencyNode6.f15581l.get(0).f15576g + this.f15612h.f15575f, this.f15613i.f15581l.get(0).f15576g - this.f15613i.f15575f, dependencyNode.f15581l.get(0).f15576g + dependencyNode.f15575f, dependencyNode2.f15581l.get(0).f15576g - dependencyNode2.f15575f, u10, v10);
                        this.f15609e.d(f15592k[0]);
                        this.f15606b.f14760f.f15609e.d(f15592k[1]);
                    } else if (z10 && z12) {
                        if (!this.f15612h.f15572c || !this.f15613i.f15572c) {
                            return;
                        }
                        float u11 = this.f15606b.u();
                        int i14 = this.f15612h.f15581l.get(0).f15576g + this.f15612h.f15575f;
                        int i15 = this.f15613i.f15581l.get(0).f15576g - this.f15613i.f15575f;
                        if (v10 == -1 || v10 == 0) {
                            int g6 = g(i15 - i14, 0);
                            int i16 = (int) ((g6 * u11) + 0.5f);
                            int g10 = g(i16, 1);
                            if (i16 != g10) {
                                g6 = (int) ((g10 / u11) + 0.5f);
                            }
                            this.f15609e.d(g6);
                            this.f15606b.f14760f.f15609e.d(g10);
                        } else if (v10 == 1) {
                            int g11 = g(i15 - i14, 0);
                            int i17 = (int) ((g11 / u11) + 0.5f);
                            int g12 = g(i17, 1);
                            if (i17 != g12) {
                                g11 = (int) ((g12 * u11) + 0.5f);
                            }
                            this.f15609e.d(g11);
                            this.f15606b.f14760f.f15609e.d(g12);
                        }
                    } else if (z11 && z13) {
                        if (!dependencyNode.f15572c || !dependencyNode2.f15572c) {
                            return;
                        }
                        float u12 = this.f15606b.u();
                        int i18 = dependencyNode.f15581l.get(0).f15576g + dependencyNode.f15575f;
                        int i19 = dependencyNode2.f15581l.get(0).f15576g - dependencyNode2.f15575f;
                        if (v10 != -1) {
                            if (v10 == 0) {
                                int g13 = g(i19 - i18, 1);
                                int i20 = (int) ((g13 * u12) + 0.5f);
                                int g14 = g(i20, 0);
                                if (i20 != g14) {
                                    g13 = (int) ((g14 / u12) + 0.5f);
                                }
                                this.f15609e.d(g14);
                                this.f15606b.f14760f.f15609e.d(g13);
                            }
                        }
                        int g15 = g(i19 - i18, 1);
                        int i21 = (int) ((g15 / u12) + 0.5f);
                        int g16 = g(i21, 0);
                        if (i21 != g16) {
                            g15 = (int) ((g16 * u12) + 0.5f);
                        }
                        this.f15609e.d(g16);
                        this.f15606b.f14760f.f15609e.d(g15);
                    }
                }
            }
        }
        DependencyNode dependencyNode7 = this.f15612h;
        if (dependencyNode7.f15572c) {
            DependencyNode dependencyNode8 = this.f15613i;
            if (dependencyNode8.f15572c) {
                if (dependencyNode7.f15579j && dependencyNode8.f15579j && this.f15609e.f15579j) {
                    return;
                }
                if (!this.f15609e.f15579j && this.f15608d == ConstraintWidget.b.MATCH_CONSTRAINT) {
                    ConstraintWidget constraintWidget5 = this.f15606b;
                    if (constraintWidget5.f14772l == 0 && !constraintWidget5.W()) {
                        DependencyNode dependencyNode9 = this.f15612h.f15581l.get(0);
                        DependencyNode dependencyNode10 = this.f15613i.f15581l.get(0);
                        int i22 = dependencyNode9.f15576g;
                        DependencyNode dependencyNode11 = this.f15612h;
                        int i23 = i22 + dependencyNode11.f15575f;
                        int i24 = dependencyNode10.f15576g + this.f15613i.f15575f;
                        dependencyNode11.d(i23);
                        this.f15613i.d(i24);
                        this.f15609e.d(i24 - i23);
                        return;
                    }
                }
                if (!this.f15609e.f15579j && this.f15608d == ConstraintWidget.b.MATCH_CONSTRAINT && this.f15605a == 1 && this.f15612h.f15581l.size() > 0 && this.f15613i.f15581l.size() > 0) {
                    int min = Math.min((this.f15613i.f15581l.get(0).f15576g + this.f15613i.f15575f) - (this.f15612h.f15581l.get(0).f15576g + this.f15612h.f15575f), this.f15609e.f15591m);
                    ConstraintWidget constraintWidget6 = this.f15606b;
                    int i25 = constraintWidget6.f14780p;
                    int max = Math.max(constraintWidget6.f14778o, min);
                    if (i25 > 0) {
                        max = Math.min(i25, max);
                    }
                    this.f15609e.d(max);
                }
                if (this.f15609e.f15579j) {
                    DependencyNode dependencyNode12 = this.f15612h.f15581l.get(0);
                    DependencyNode dependencyNode13 = this.f15613i.f15581l.get(0);
                    int i26 = dependencyNode12.f15576g + this.f15612h.f15575f;
                    int i27 = dependencyNode13.f15576g + this.f15613i.f15575f;
                    float x10 = this.f15606b.x();
                    if (dependencyNode12 == dependencyNode13) {
                        i26 = dependencyNode12.f15576g;
                        i27 = dependencyNode13.f15576g;
                        x10 = 0.5f;
                    }
                    this.f15612h.d((int) (i26 + 0.5f + (((i27 - i26) - this.f15609e.f15576g) * x10)));
                    this.f15613i.d(this.f15612h.f15576g + this.f15609e.f15576g);
                }
            }
        }
    }

    @Override // n.WidgetRun
    void d() {
        ConstraintWidget H;
        ConstraintWidget H2;
        ConstraintWidget constraintWidget = this.f15606b;
        if (constraintWidget.f14750a) {
            this.f15609e.d(constraintWidget.Q());
        }
        if (!this.f15609e.f15579j) {
            ConstraintWidget.b z10 = this.f15606b.z();
            this.f15608d = z10;
            if (z10 != ConstraintWidget.b.MATCH_CONSTRAINT) {
                ConstraintWidget.b bVar = ConstraintWidget.b.MATCH_PARENT;
                if (z10 == bVar && (((H2 = this.f15606b.H()) != null && H2.z() == ConstraintWidget.b.FIXED) || H2.z() == bVar)) {
                    int Q = (H2.Q() - this.f15606b.D.c()) - this.f15606b.F.c();
                    b(this.f15612h, H2.f14758e.f15612h, this.f15606b.D.c());
                    b(this.f15613i, H2.f14758e.f15613i, -this.f15606b.F.c());
                    this.f15609e.d(Q);
                    return;
                }
                if (this.f15608d == ConstraintWidget.b.FIXED) {
                    this.f15609e.d(this.f15606b.Q());
                }
            }
        } else {
            ConstraintWidget.b bVar2 = this.f15608d;
            ConstraintWidget.b bVar3 = ConstraintWidget.b.MATCH_PARENT;
            if (bVar2 == bVar3 && (((H = this.f15606b.H()) != null && H.z() == ConstraintWidget.b.FIXED) || H.z() == bVar3)) {
                b(this.f15612h, H.f14758e.f15612h, this.f15606b.D.c());
                b(this.f15613i, H.f14758e.f15613i, -this.f15606b.F.c());
                return;
            }
        }
        DimensionDependency dimensionDependency = this.f15609e;
        if (dimensionDependency.f15579j) {
            ConstraintWidget constraintWidget2 = this.f15606b;
            if (constraintWidget2.f14750a) {
                ConstraintAnchor[] constraintAnchorArr = constraintWidget2.L;
                if (constraintAnchorArr[0].f14735d != null && constraintAnchorArr[1].f14735d != null) {
                    if (constraintWidget2.W()) {
                        this.f15612h.f15575f = this.f15606b.L[0].c();
                        this.f15613i.f15575f = -this.f15606b.L[1].c();
                        return;
                    }
                    DependencyNode h10 = h(this.f15606b.L[0]);
                    if (h10 != null) {
                        b(this.f15612h, h10, this.f15606b.L[0].c());
                    }
                    DependencyNode h11 = h(this.f15606b.L[1]);
                    if (h11 != null) {
                        b(this.f15613i, h11, -this.f15606b.L[1].c());
                    }
                    this.f15612h.f15571b = true;
                    this.f15613i.f15571b = true;
                    return;
                }
                if (constraintAnchorArr[0].f14735d != null) {
                    DependencyNode h12 = h(constraintAnchorArr[0]);
                    if (h12 != null) {
                        b(this.f15612h, h12, this.f15606b.L[0].c());
                        b(this.f15613i, this.f15612h, this.f15609e.f15576g);
                        return;
                    }
                    return;
                }
                if (constraintAnchorArr[1].f14735d != null) {
                    DependencyNode h13 = h(constraintAnchorArr[1]);
                    if (h13 != null) {
                        b(this.f15613i, h13, -this.f15606b.L[1].c());
                        b(this.f15612h, this.f15613i, -this.f15609e.f15576g);
                        return;
                    }
                    return;
                }
                if ((constraintWidget2 instanceof Helper) || constraintWidget2.H() == null || this.f15606b.n(ConstraintAnchor.b.CENTER).f14735d != null) {
                    return;
                }
                b(this.f15612h, this.f15606b.H().f14758e.f15612h, this.f15606b.R());
                b(this.f15613i, this.f15612h, this.f15609e.f15576g);
                return;
            }
        }
        if (this.f15608d == ConstraintWidget.b.MATCH_CONSTRAINT) {
            ConstraintWidget constraintWidget3 = this.f15606b;
            int i10 = constraintWidget3.f14772l;
            if (i10 == 2) {
                ConstraintWidget H3 = constraintWidget3.H();
                if (H3 != null) {
                    DimensionDependency dimensionDependency2 = H3.f14760f.f15609e;
                    this.f15609e.f15581l.add(dimensionDependency2);
                    dimensionDependency2.f15580k.add(this.f15609e);
                    DimensionDependency dimensionDependency3 = this.f15609e;
                    dimensionDependency3.f15571b = true;
                    dimensionDependency3.f15580k.add(this.f15612h);
                    this.f15609e.f15580k.add(this.f15613i);
                }
            } else if (i10 == 3) {
                if (constraintWidget3.f14774m == 3) {
                    this.f15612h.f15570a = this;
                    this.f15613i.f15570a = this;
                    VerticalWidgetRun verticalWidgetRun = constraintWidget3.f14760f;
                    verticalWidgetRun.f15612h.f15570a = this;
                    verticalWidgetRun.f15613i.f15570a = this;
                    dimensionDependency.f15570a = this;
                    if (constraintWidget3.Y()) {
                        this.f15609e.f15581l.add(this.f15606b.f14760f.f15609e);
                        this.f15606b.f14760f.f15609e.f15580k.add(this.f15609e);
                        VerticalWidgetRun verticalWidgetRun2 = this.f15606b.f14760f;
                        verticalWidgetRun2.f15609e.f15570a = this;
                        this.f15609e.f15581l.add(verticalWidgetRun2.f15612h);
                        this.f15609e.f15581l.add(this.f15606b.f14760f.f15613i);
                        this.f15606b.f14760f.f15612h.f15580k.add(this.f15609e);
                        this.f15606b.f14760f.f15613i.f15580k.add(this.f15609e);
                    } else if (this.f15606b.W()) {
                        this.f15606b.f14760f.f15609e.f15581l.add(this.f15609e);
                        this.f15609e.f15580k.add(this.f15606b.f14760f.f15609e);
                    } else {
                        this.f15606b.f14760f.f15609e.f15581l.add(this.f15609e);
                    }
                } else {
                    DimensionDependency dimensionDependency4 = constraintWidget3.f14760f.f15609e;
                    dimensionDependency.f15581l.add(dimensionDependency4);
                    dimensionDependency4.f15580k.add(this.f15609e);
                    this.f15606b.f14760f.f15612h.f15580k.add(this.f15609e);
                    this.f15606b.f14760f.f15613i.f15580k.add(this.f15609e);
                    DimensionDependency dimensionDependency5 = this.f15609e;
                    dimensionDependency5.f15571b = true;
                    dimensionDependency5.f15580k.add(this.f15612h);
                    this.f15609e.f15580k.add(this.f15613i);
                    this.f15612h.f15581l.add(this.f15609e);
                    this.f15613i.f15581l.add(this.f15609e);
                }
            }
        }
        ConstraintWidget constraintWidget4 = this.f15606b;
        ConstraintAnchor[] constraintAnchorArr2 = constraintWidget4.L;
        if (constraintAnchorArr2[0].f14735d != null && constraintAnchorArr2[1].f14735d != null) {
            if (constraintWidget4.W()) {
                this.f15612h.f15575f = this.f15606b.L[0].c();
                this.f15613i.f15575f = -this.f15606b.L[1].c();
                return;
            }
            DependencyNode h14 = h(this.f15606b.L[0]);
            DependencyNode h15 = h(this.f15606b.L[1]);
            h14.b(this);
            h15.b(this);
            this.f15614j = WidgetRun.b.CENTER;
            return;
        }
        if (constraintAnchorArr2[0].f14735d != null) {
            DependencyNode h16 = h(constraintAnchorArr2[0]);
            if (h16 != null) {
                b(this.f15612h, h16, this.f15606b.L[0].c());
                c(this.f15613i, this.f15612h, 1, this.f15609e);
                return;
            }
            return;
        }
        if (constraintAnchorArr2[1].f14735d != null) {
            DependencyNode h17 = h(constraintAnchorArr2[1]);
            if (h17 != null) {
                b(this.f15613i, h17, -this.f15606b.L[1].c());
                c(this.f15612h, this.f15613i, -1, this.f15609e);
                return;
            }
            return;
        }
        if ((constraintWidget4 instanceof Helper) || constraintWidget4.H() == null) {
            return;
        }
        b(this.f15612h, this.f15606b.H().f14758e.f15612h, this.f15606b.R());
        c(this.f15613i, this.f15612h, 1, this.f15609e);
    }

    @Override // n.WidgetRun
    public void e() {
        DependencyNode dependencyNode = this.f15612h;
        if (dependencyNode.f15579j) {
            this.f15606b.G0(dependencyNode.f15576g);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // n.WidgetRun
    public void f() {
        this.f15607c = null;
        this.f15612h.c();
        this.f15613i.c();
        this.f15609e.c();
        this.f15611g = false;
    }

    @Override // n.WidgetRun
    boolean m() {
        return this.f15608d != ConstraintWidget.b.MATCH_CONSTRAINT || this.f15606b.f14772l == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r() {
        this.f15611g = false;
        this.f15612h.c();
        this.f15612h.f15579j = false;
        this.f15613i.c();
        this.f15613i.f15579j = false;
        this.f15609e.f15579j = false;
    }

    public String toString() {
        return "HorizontalRun " + this.f15606b.s();
    }
}
