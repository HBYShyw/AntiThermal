package n;

import m.ConstraintAnchor;
import m.ConstraintWidget;
import m.Helper;
import n.DependencyNode;
import n.WidgetRun;

/* compiled from: VerticalWidgetRun.java */
/* renamed from: n.l, reason: use source file name */
/* loaded from: classes.dex */
public class VerticalWidgetRun extends WidgetRun {

    /* renamed from: k, reason: collision with root package name */
    public DependencyNode f15602k;

    /* renamed from: l, reason: collision with root package name */
    DimensionDependency f15603l;

    /* compiled from: VerticalWidgetRun.java */
    /* renamed from: n.l$a */
    /* loaded from: classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f15604a;

        static {
            int[] iArr = new int[WidgetRun.b.values().length];
            f15604a = iArr;
            try {
                iArr[WidgetRun.b.START.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f15604a[WidgetRun.b.END.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f15604a[WidgetRun.b.CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public VerticalWidgetRun(ConstraintWidget constraintWidget) {
        super(constraintWidget);
        DependencyNode dependencyNode = new DependencyNode(this);
        this.f15602k = dependencyNode;
        this.f15603l = null;
        this.f15612h.f15574e = DependencyNode.a.TOP;
        this.f15613i.f15574e = DependencyNode.a.BOTTOM;
        dependencyNode.f15574e = DependencyNode.a.BASELINE;
        this.f15610f = 1;
    }

    @Override // n.WidgetRun, n.Dependency
    public void a(Dependency dependency) {
        float f10;
        float u7;
        float f11;
        int i10;
        int i11 = a.f15604a[this.f15614j.ordinal()];
        if (i11 == 1) {
            p(dependency);
        } else if (i11 == 2) {
            o(dependency);
        } else if (i11 == 3) {
            ConstraintWidget constraintWidget = this.f15606b;
            n(dependency, constraintWidget.E, constraintWidget.G, 1);
            return;
        }
        DimensionDependency dimensionDependency = this.f15609e;
        if (dimensionDependency.f15572c && !dimensionDependency.f15579j && this.f15608d == ConstraintWidget.b.MATCH_CONSTRAINT) {
            ConstraintWidget constraintWidget2 = this.f15606b;
            int i12 = constraintWidget2.f14774m;
            if (i12 != 2) {
                if (i12 == 3 && constraintWidget2.f14758e.f15609e.f15579j) {
                    int v7 = constraintWidget2.v();
                    if (v7 == -1) {
                        ConstraintWidget constraintWidget3 = this.f15606b;
                        f10 = constraintWidget3.f14758e.f15609e.f15576g;
                        u7 = constraintWidget3.u();
                    } else if (v7 == 0) {
                        f11 = r7.f14758e.f15609e.f15576g * this.f15606b.u();
                        i10 = (int) (f11 + 0.5f);
                        this.f15609e.d(i10);
                    } else if (v7 == 1) {
                        ConstraintWidget constraintWidget4 = this.f15606b;
                        f10 = constraintWidget4.f14758e.f15609e.f15576g;
                        u7 = constraintWidget4.u();
                    } else {
                        i10 = 0;
                        this.f15609e.d(i10);
                    }
                    f11 = f10 / u7;
                    i10 = (int) (f11 + 0.5f);
                    this.f15609e.d(i10);
                }
            } else {
                ConstraintWidget H = constraintWidget2.H();
                if (H != null) {
                    if (H.f14760f.f15609e.f15579j) {
                        this.f15609e.d((int) ((r7.f15576g * this.f15606b.f14788t) + 0.5f));
                    }
                }
            }
        }
        DependencyNode dependencyNode = this.f15612h;
        if (dependencyNode.f15572c) {
            DependencyNode dependencyNode2 = this.f15613i;
            if (dependencyNode2.f15572c) {
                if (dependencyNode.f15579j && dependencyNode2.f15579j && this.f15609e.f15579j) {
                    return;
                }
                if (!this.f15609e.f15579j && this.f15608d == ConstraintWidget.b.MATCH_CONSTRAINT) {
                    ConstraintWidget constraintWidget5 = this.f15606b;
                    if (constraintWidget5.f14772l == 0 && !constraintWidget5.Y()) {
                        DependencyNode dependencyNode3 = this.f15612h.f15581l.get(0);
                        DependencyNode dependencyNode4 = this.f15613i.f15581l.get(0);
                        int i13 = dependencyNode3.f15576g;
                        DependencyNode dependencyNode5 = this.f15612h;
                        int i14 = i13 + dependencyNode5.f15575f;
                        int i15 = dependencyNode4.f15576g + this.f15613i.f15575f;
                        dependencyNode5.d(i14);
                        this.f15613i.d(i15);
                        this.f15609e.d(i15 - i14);
                        return;
                    }
                }
                if (!this.f15609e.f15579j && this.f15608d == ConstraintWidget.b.MATCH_CONSTRAINT && this.f15605a == 1 && this.f15612h.f15581l.size() > 0 && this.f15613i.f15581l.size() > 0) {
                    DependencyNode dependencyNode6 = this.f15612h.f15581l.get(0);
                    int i16 = (this.f15613i.f15581l.get(0).f15576g + this.f15613i.f15575f) - (dependencyNode6.f15576g + this.f15612h.f15575f);
                    DimensionDependency dimensionDependency2 = this.f15609e;
                    int i17 = dimensionDependency2.f15591m;
                    if (i16 < i17) {
                        dimensionDependency2.d(i16);
                    } else {
                        dimensionDependency2.d(i17);
                    }
                }
                if (this.f15609e.f15579j && this.f15612h.f15581l.size() > 0 && this.f15613i.f15581l.size() > 0) {
                    DependencyNode dependencyNode7 = this.f15612h.f15581l.get(0);
                    DependencyNode dependencyNode8 = this.f15613i.f15581l.get(0);
                    int i18 = dependencyNode7.f15576g + this.f15612h.f15575f;
                    int i19 = dependencyNode8.f15576g + this.f15613i.f15575f;
                    float L = this.f15606b.L();
                    if (dependencyNode7 == dependencyNode8) {
                        i18 = dependencyNode7.f15576g;
                        i19 = dependencyNode8.f15576g;
                        L = 0.5f;
                    }
                    this.f15612h.d((int) (i18 + 0.5f + (((i19 - i18) - this.f15609e.f15576g) * L)));
                    this.f15613i.d(this.f15612h.f15576g + this.f15609e.f15576g);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // n.WidgetRun
    public void d() {
        ConstraintWidget H;
        ConstraintWidget H2;
        ConstraintWidget constraintWidget = this.f15606b;
        if (constraintWidget.f14750a) {
            this.f15609e.d(constraintWidget.w());
        }
        if (!this.f15609e.f15579j) {
            this.f15608d = this.f15606b.N();
            if (this.f15606b.T()) {
                this.f15603l = new BaselineDimensionDependency(this);
            }
            ConstraintWidget.b bVar = this.f15608d;
            if (bVar != ConstraintWidget.b.MATCH_CONSTRAINT) {
                if (bVar == ConstraintWidget.b.MATCH_PARENT && (H2 = this.f15606b.H()) != null && H2.N() == ConstraintWidget.b.FIXED) {
                    int w10 = (H2.w() - this.f15606b.E.c()) - this.f15606b.G.c();
                    b(this.f15612h, H2.f14760f.f15612h, this.f15606b.E.c());
                    b(this.f15613i, H2.f14760f.f15613i, -this.f15606b.G.c());
                    this.f15609e.d(w10);
                    return;
                }
                if (this.f15608d == ConstraintWidget.b.FIXED) {
                    this.f15609e.d(this.f15606b.w());
                }
            }
        } else if (this.f15608d == ConstraintWidget.b.MATCH_PARENT && (H = this.f15606b.H()) != null && H.N() == ConstraintWidget.b.FIXED) {
            b(this.f15612h, H.f14760f.f15612h, this.f15606b.E.c());
            b(this.f15613i, H.f14760f.f15613i, -this.f15606b.G.c());
            return;
        }
        DimensionDependency dimensionDependency = this.f15609e;
        boolean z10 = dimensionDependency.f15579j;
        if (z10) {
            ConstraintWidget constraintWidget2 = this.f15606b;
            if (constraintWidget2.f14750a) {
                ConstraintAnchor[] constraintAnchorArr = constraintWidget2.L;
                if (constraintAnchorArr[2].f14735d != null && constraintAnchorArr[3].f14735d != null) {
                    if (constraintWidget2.Y()) {
                        this.f15612h.f15575f = this.f15606b.L[2].c();
                        this.f15613i.f15575f = -this.f15606b.L[3].c();
                    } else {
                        DependencyNode h10 = h(this.f15606b.L[2]);
                        if (h10 != null) {
                            b(this.f15612h, h10, this.f15606b.L[2].c());
                        }
                        DependencyNode h11 = h(this.f15606b.L[3]);
                        if (h11 != null) {
                            b(this.f15613i, h11, -this.f15606b.L[3].c());
                        }
                        this.f15612h.f15571b = true;
                        this.f15613i.f15571b = true;
                    }
                    if (this.f15606b.T()) {
                        b(this.f15602k, this.f15612h, this.f15606b.o());
                        return;
                    }
                    return;
                }
                if (constraintAnchorArr[2].f14735d != null) {
                    DependencyNode h12 = h(constraintAnchorArr[2]);
                    if (h12 != null) {
                        b(this.f15612h, h12, this.f15606b.L[2].c());
                        b(this.f15613i, this.f15612h, this.f15609e.f15576g);
                        if (this.f15606b.T()) {
                            b(this.f15602k, this.f15612h, this.f15606b.o());
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (constraintAnchorArr[3].f14735d != null) {
                    DependencyNode h13 = h(constraintAnchorArr[3]);
                    if (h13 != null) {
                        b(this.f15613i, h13, -this.f15606b.L[3].c());
                        b(this.f15612h, this.f15613i, -this.f15609e.f15576g);
                    }
                    if (this.f15606b.T()) {
                        b(this.f15602k, this.f15612h, this.f15606b.o());
                        return;
                    }
                    return;
                }
                if (constraintAnchorArr[4].f14735d != null) {
                    DependencyNode h14 = h(constraintAnchorArr[4]);
                    if (h14 != null) {
                        b(this.f15602k, h14, 0);
                        b(this.f15612h, this.f15602k, -this.f15606b.o());
                        b(this.f15613i, this.f15612h, this.f15609e.f15576g);
                        return;
                    }
                    return;
                }
                if ((constraintWidget2 instanceof Helper) || constraintWidget2.H() == null || this.f15606b.n(ConstraintAnchor.b.CENTER).f14735d != null) {
                    return;
                }
                b(this.f15612h, this.f15606b.H().f14760f.f15612h, this.f15606b.S());
                b(this.f15613i, this.f15612h, this.f15609e.f15576g);
                if (this.f15606b.T()) {
                    b(this.f15602k, this.f15612h, this.f15606b.o());
                    return;
                }
                return;
            }
        }
        if (!z10 && this.f15608d == ConstraintWidget.b.MATCH_CONSTRAINT) {
            ConstraintWidget constraintWidget3 = this.f15606b;
            int i10 = constraintWidget3.f14774m;
            if (i10 != 2) {
                if (i10 == 3 && !constraintWidget3.Y()) {
                    ConstraintWidget constraintWidget4 = this.f15606b;
                    if (constraintWidget4.f14772l != 3) {
                        DimensionDependency dimensionDependency2 = constraintWidget4.f14758e.f15609e;
                        this.f15609e.f15581l.add(dimensionDependency2);
                        dimensionDependency2.f15580k.add(this.f15609e);
                        DimensionDependency dimensionDependency3 = this.f15609e;
                        dimensionDependency3.f15571b = true;
                        dimensionDependency3.f15580k.add(this.f15612h);
                        this.f15609e.f15580k.add(this.f15613i);
                    }
                }
            } else {
                ConstraintWidget H3 = constraintWidget3.H();
                if (H3 != null) {
                    DimensionDependency dimensionDependency4 = H3.f14760f.f15609e;
                    this.f15609e.f15581l.add(dimensionDependency4);
                    dimensionDependency4.f15580k.add(this.f15609e);
                    DimensionDependency dimensionDependency5 = this.f15609e;
                    dimensionDependency5.f15571b = true;
                    dimensionDependency5.f15580k.add(this.f15612h);
                    this.f15609e.f15580k.add(this.f15613i);
                }
            }
        } else {
            dimensionDependency.b(this);
        }
        ConstraintWidget constraintWidget5 = this.f15606b;
        ConstraintAnchor[] constraintAnchorArr2 = constraintWidget5.L;
        if (constraintAnchorArr2[2].f14735d != null && constraintAnchorArr2[3].f14735d != null) {
            if (constraintWidget5.Y()) {
                this.f15612h.f15575f = this.f15606b.L[2].c();
                this.f15613i.f15575f = -this.f15606b.L[3].c();
            } else {
                DependencyNode h15 = h(this.f15606b.L[2]);
                DependencyNode h16 = h(this.f15606b.L[3]);
                h15.b(this);
                h16.b(this);
                this.f15614j = WidgetRun.b.CENTER;
            }
            if (this.f15606b.T()) {
                c(this.f15602k, this.f15612h, 1, this.f15603l);
            }
        } else if (constraintAnchorArr2[2].f14735d != null) {
            DependencyNode h17 = h(constraintAnchorArr2[2]);
            if (h17 != null) {
                b(this.f15612h, h17, this.f15606b.L[2].c());
                c(this.f15613i, this.f15612h, 1, this.f15609e);
                if (this.f15606b.T()) {
                    c(this.f15602k, this.f15612h, 1, this.f15603l);
                }
                ConstraintWidget.b bVar2 = this.f15608d;
                ConstraintWidget.b bVar3 = ConstraintWidget.b.MATCH_CONSTRAINT;
                if (bVar2 == bVar3 && this.f15606b.u() > 0.0f) {
                    HorizontalWidgetRun horizontalWidgetRun = this.f15606b.f14758e;
                    if (horizontalWidgetRun.f15608d == bVar3) {
                        horizontalWidgetRun.f15609e.f15580k.add(this.f15609e);
                        this.f15609e.f15581l.add(this.f15606b.f14758e.f15609e);
                        this.f15609e.f15570a = this;
                    }
                }
            }
        } else if (constraintAnchorArr2[3].f14735d != null) {
            DependencyNode h18 = h(constraintAnchorArr2[3]);
            if (h18 != null) {
                b(this.f15613i, h18, -this.f15606b.L[3].c());
                c(this.f15612h, this.f15613i, -1, this.f15609e);
                if (this.f15606b.T()) {
                    c(this.f15602k, this.f15612h, 1, this.f15603l);
                }
            }
        } else if (constraintAnchorArr2[4].f14735d != null) {
            DependencyNode h19 = h(constraintAnchorArr2[4]);
            if (h19 != null) {
                b(this.f15602k, h19, 0);
                c(this.f15612h, this.f15602k, -1, this.f15603l);
                c(this.f15613i, this.f15612h, 1, this.f15609e);
            }
        } else if (!(constraintWidget5 instanceof Helper) && constraintWidget5.H() != null) {
            b(this.f15612h, this.f15606b.H().f14760f.f15612h, this.f15606b.S());
            c(this.f15613i, this.f15612h, 1, this.f15609e);
            if (this.f15606b.T()) {
                c(this.f15602k, this.f15612h, 1, this.f15603l);
            }
            ConstraintWidget.b bVar4 = this.f15608d;
            ConstraintWidget.b bVar5 = ConstraintWidget.b.MATCH_CONSTRAINT;
            if (bVar4 == bVar5 && this.f15606b.u() > 0.0f) {
                HorizontalWidgetRun horizontalWidgetRun2 = this.f15606b.f14758e;
                if (horizontalWidgetRun2.f15608d == bVar5) {
                    horizontalWidgetRun2.f15609e.f15580k.add(this.f15609e);
                    this.f15609e.f15581l.add(this.f15606b.f14758e.f15609e);
                    this.f15609e.f15570a = this;
                }
            }
        }
        if (this.f15609e.f15581l.size() == 0) {
            this.f15609e.f15572c = true;
        }
    }

    @Override // n.WidgetRun
    public void e() {
        DependencyNode dependencyNode = this.f15612h;
        if (dependencyNode.f15579j) {
            this.f15606b.H0(dependencyNode.f15576g);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // n.WidgetRun
    public void f() {
        this.f15607c = null;
        this.f15612h.c();
        this.f15613i.c();
        this.f15602k.c();
        this.f15609e.c();
        this.f15611g = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // n.WidgetRun
    public boolean m() {
        return this.f15608d != ConstraintWidget.b.MATCH_CONSTRAINT || this.f15606b.f14774m == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q() {
        this.f15611g = false;
        this.f15612h.c();
        this.f15612h.f15579j = false;
        this.f15613i.c();
        this.f15613i.f15579j = false;
        this.f15602k.c();
        this.f15602k.f15579j = false;
        this.f15609e.f15579j = false;
    }

    public String toString() {
        return "VerticalRun " + this.f15606b.s();
    }
}
