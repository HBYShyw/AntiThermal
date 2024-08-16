package n;

import m.ConstraintAnchor;
import m.ConstraintWidget;

/* compiled from: WidgetRun.java */
/* renamed from: n.m, reason: use source file name */
/* loaded from: classes.dex */
public abstract class WidgetRun implements Dependency {

    /* renamed from: a, reason: collision with root package name */
    public int f15605a;

    /* renamed from: b, reason: collision with root package name */
    ConstraintWidget f15606b;

    /* renamed from: c, reason: collision with root package name */
    RunGroup f15607c;

    /* renamed from: d, reason: collision with root package name */
    protected ConstraintWidget.b f15608d;

    /* renamed from: e, reason: collision with root package name */
    DimensionDependency f15609e = new DimensionDependency(this);

    /* renamed from: f, reason: collision with root package name */
    public int f15610f = 0;

    /* renamed from: g, reason: collision with root package name */
    boolean f15611g = false;

    /* renamed from: h, reason: collision with root package name */
    public DependencyNode f15612h = new DependencyNode(this);

    /* renamed from: i, reason: collision with root package name */
    public DependencyNode f15613i = new DependencyNode(this);

    /* renamed from: j, reason: collision with root package name */
    protected b f15614j = b.NONE;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: WidgetRun.java */
    /* renamed from: n.m$a */
    /* loaded from: classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f15615a;

        static {
            int[] iArr = new int[ConstraintAnchor.b.values().length];
            f15615a = iArr;
            try {
                iArr[ConstraintAnchor.b.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f15615a[ConstraintAnchor.b.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f15615a[ConstraintAnchor.b.TOP.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f15615a[ConstraintAnchor.b.BASELINE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f15615a[ConstraintAnchor.b.BOTTOM.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    /* compiled from: WidgetRun.java */
    /* renamed from: n.m$b */
    /* loaded from: classes.dex */
    enum b {
        NONE,
        START,
        END,
        CENTER
    }

    public WidgetRun(ConstraintWidget constraintWidget) {
        this.f15606b = constraintWidget;
    }

    private void l(int i10, int i11) {
        int i12;
        int i13 = this.f15605a;
        if (i13 == 0) {
            this.f15609e.d(g(i11, i10));
            return;
        }
        if (i13 == 1) {
            this.f15609e.d(Math.min(g(this.f15609e.f15591m, i10), i11));
            return;
        }
        if (i13 == 2) {
            ConstraintWidget H = this.f15606b.H();
            if (H != null) {
                if ((i10 == 0 ? H.f14758e : H.f14760f).f15609e.f15579j) {
                    ConstraintWidget constraintWidget = this.f15606b;
                    this.f15609e.d(g((int) ((r9.f15576g * (i10 == 0 ? constraintWidget.f14782q : constraintWidget.f14788t)) + 0.5f), i10));
                    return;
                }
                return;
            }
            return;
        }
        if (i13 != 3) {
            return;
        }
        ConstraintWidget constraintWidget2 = this.f15606b;
        WidgetRun widgetRun = constraintWidget2.f14758e;
        ConstraintWidget.b bVar = widgetRun.f15608d;
        ConstraintWidget.b bVar2 = ConstraintWidget.b.MATCH_CONSTRAINT;
        if (bVar == bVar2 && widgetRun.f15605a == 3) {
            VerticalWidgetRun verticalWidgetRun = constraintWidget2.f14760f;
            if (verticalWidgetRun.f15608d == bVar2 && verticalWidgetRun.f15605a == 3) {
                return;
            }
        }
        if (i10 == 0) {
            widgetRun = constraintWidget2.f14760f;
        }
        if (widgetRun.f15609e.f15579j) {
            float u7 = constraintWidget2.u();
            if (i10 == 1) {
                i12 = (int) ((widgetRun.f15609e.f15576g / u7) + 0.5f);
            } else {
                i12 = (int) ((u7 * widgetRun.f15609e.f15576g) + 0.5f);
            }
            this.f15609e.d(i12);
        }
    }

    @Override // n.Dependency
    public void a(Dependency dependency) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void b(DependencyNode dependencyNode, DependencyNode dependencyNode2, int i10) {
        dependencyNode.f15581l.add(dependencyNode2);
        dependencyNode.f15575f = i10;
        dependencyNode2.f15580k.add(dependencyNode);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void c(DependencyNode dependencyNode, DependencyNode dependencyNode2, int i10, DimensionDependency dimensionDependency) {
        dependencyNode.f15581l.add(dependencyNode2);
        dependencyNode.f15581l.add(this.f15609e);
        dependencyNode.f15577h = i10;
        dependencyNode.f15578i = dimensionDependency;
        dependencyNode2.f15580k.add(dependencyNode);
        dimensionDependency.f15580k.add(dependencyNode);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void d();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void e();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void f();

    /* JADX INFO: Access modifiers changed from: protected */
    public final int g(int i10, int i11) {
        int max;
        if (i11 == 0) {
            ConstraintWidget constraintWidget = this.f15606b;
            int i12 = constraintWidget.f14780p;
            max = Math.max(constraintWidget.f14778o, i10);
            if (i12 > 0) {
                max = Math.min(i12, i10);
            }
            if (max == i10) {
                return i10;
            }
        } else {
            ConstraintWidget constraintWidget2 = this.f15606b;
            int i13 = constraintWidget2.f14786s;
            max = Math.max(constraintWidget2.f14784r, i10);
            if (i13 > 0) {
                max = Math.min(i13, i10);
            }
            if (max == i10) {
                return i10;
            }
        }
        return max;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final DependencyNode h(ConstraintAnchor constraintAnchor) {
        ConstraintAnchor constraintAnchor2 = constraintAnchor.f14735d;
        if (constraintAnchor2 == null) {
            return null;
        }
        ConstraintWidget constraintWidget = constraintAnchor2.f14733b;
        int i10 = a.f15615a[constraintAnchor2.f14734c.ordinal()];
        if (i10 == 1) {
            return constraintWidget.f14758e.f15612h;
        }
        if (i10 == 2) {
            return constraintWidget.f14758e.f15613i;
        }
        if (i10 == 3) {
            return constraintWidget.f14760f.f15612h;
        }
        if (i10 == 4) {
            return constraintWidget.f14760f.f15602k;
        }
        if (i10 != 5) {
            return null;
        }
        return constraintWidget.f14760f.f15613i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final DependencyNode i(ConstraintAnchor constraintAnchor, int i10) {
        ConstraintAnchor constraintAnchor2 = constraintAnchor.f14735d;
        if (constraintAnchor2 == null) {
            return null;
        }
        ConstraintWidget constraintWidget = constraintAnchor2.f14733b;
        WidgetRun widgetRun = i10 == 0 ? constraintWidget.f14758e : constraintWidget.f14760f;
        int i11 = a.f15615a[constraintAnchor2.f14734c.ordinal()];
        if (i11 != 1) {
            if (i11 != 2) {
                if (i11 != 3) {
                    if (i11 != 5) {
                        return null;
                    }
                }
            }
            return widgetRun.f15613i;
        }
        return widgetRun.f15612h;
    }

    public long j() {
        if (this.f15609e.f15579j) {
            return r2.f15576g;
        }
        return 0L;
    }

    public boolean k() {
        return this.f15611g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean m();

    /* JADX INFO: Access modifiers changed from: protected */
    public void n(Dependency dependency, ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int i10) {
        DependencyNode h10 = h(constraintAnchor);
        DependencyNode h11 = h(constraintAnchor2);
        if (h10.f15579j && h11.f15579j) {
            int c10 = h10.f15576g + constraintAnchor.c();
            int c11 = h11.f15576g - constraintAnchor2.c();
            int i11 = c11 - c10;
            if (!this.f15609e.f15579j && this.f15608d == ConstraintWidget.b.MATCH_CONSTRAINT) {
                l(i10, i11);
            }
            DimensionDependency dimensionDependency = this.f15609e;
            if (dimensionDependency.f15579j) {
                if (dimensionDependency.f15576g == i11) {
                    this.f15612h.d(c10);
                    this.f15613i.d(c11);
                    return;
                }
                ConstraintWidget constraintWidget = this.f15606b;
                float x10 = i10 == 0 ? constraintWidget.x() : constraintWidget.L();
                if (h10 == h11) {
                    c10 = h10.f15576g;
                    c11 = h11.f15576g;
                    x10 = 0.5f;
                }
                this.f15612h.d((int) (c10 + 0.5f + (((c11 - c10) - this.f15609e.f15576g) * x10)));
                this.f15613i.d(this.f15612h.f15576g + this.f15609e.f15576g);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void o(Dependency dependency) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void p(Dependency dependency) {
    }
}
