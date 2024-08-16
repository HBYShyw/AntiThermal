package n;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import m.ConstraintAnchor;
import m.ConstraintWidget;
import m.ConstraintWidgetContainer;
import m.HelperWidget;
import n.BasicMeasure;

/* compiled from: DependencyGraph.java */
/* renamed from: n.e, reason: use source file name */
/* loaded from: classes.dex */
public class DependencyGraph {

    /* renamed from: a, reason: collision with root package name */
    private ConstraintWidgetContainer f15561a;

    /* renamed from: d, reason: collision with root package name */
    private ConstraintWidgetContainer f15564d;

    /* renamed from: b, reason: collision with root package name */
    private boolean f15562b = true;

    /* renamed from: c, reason: collision with root package name */
    private boolean f15563c = true;

    /* renamed from: e, reason: collision with root package name */
    private ArrayList<WidgetRun> f15565e = new ArrayList<>();

    /* renamed from: f, reason: collision with root package name */
    private ArrayList<RunGroup> f15566f = new ArrayList<>();

    /* renamed from: g, reason: collision with root package name */
    private BasicMeasure.b f15567g = null;

    /* renamed from: h, reason: collision with root package name */
    private BasicMeasure.a f15568h = new BasicMeasure.a();

    /* renamed from: i, reason: collision with root package name */
    ArrayList<RunGroup> f15569i = new ArrayList<>();

    public DependencyGraph(ConstraintWidgetContainer constraintWidgetContainer) {
        this.f15561a = constraintWidgetContainer;
        this.f15564d = constraintWidgetContainer;
    }

    private void a(DependencyNode dependencyNode, int i10, int i11, DependencyNode dependencyNode2, ArrayList<RunGroup> arrayList, RunGroup runGroup) {
        WidgetRun widgetRun = dependencyNode.f15573d;
        if (widgetRun.f15607c == null) {
            ConstraintWidgetContainer constraintWidgetContainer = this.f15561a;
            if (widgetRun == constraintWidgetContainer.f14758e || widgetRun == constraintWidgetContainer.f14760f) {
                return;
            }
            if (runGroup == null) {
                runGroup = new RunGroup(widgetRun, i11);
                arrayList.add(runGroup);
            }
            widgetRun.f15607c = runGroup;
            runGroup.a(widgetRun);
            for (Dependency dependency : widgetRun.f15612h.f15580k) {
                if (dependency instanceof DependencyNode) {
                    a((DependencyNode) dependency, i10, 0, dependencyNode2, arrayList, runGroup);
                }
            }
            for (Dependency dependency2 : widgetRun.f15613i.f15580k) {
                if (dependency2 instanceof DependencyNode) {
                    a((DependencyNode) dependency2, i10, 1, dependencyNode2, arrayList, runGroup);
                }
            }
            if (i10 == 1 && (widgetRun instanceof VerticalWidgetRun)) {
                for (Dependency dependency3 : ((VerticalWidgetRun) widgetRun).f15602k.f15580k) {
                    if (dependency3 instanceof DependencyNode) {
                        a((DependencyNode) dependency3, i10, 2, dependencyNode2, arrayList, runGroup);
                    }
                }
            }
            for (DependencyNode dependencyNode3 : widgetRun.f15612h.f15581l) {
                if (dependencyNode3 == dependencyNode2) {
                    runGroup.f15596b = true;
                }
                a(dependencyNode3, i10, 0, dependencyNode2, arrayList, runGroup);
            }
            for (DependencyNode dependencyNode4 : widgetRun.f15613i.f15581l) {
                if (dependencyNode4 == dependencyNode2) {
                    runGroup.f15596b = true;
                }
                a(dependencyNode4, i10, 1, dependencyNode2, arrayList, runGroup);
            }
            if (i10 == 1 && (widgetRun instanceof VerticalWidgetRun)) {
                Iterator<DependencyNode> it = ((VerticalWidgetRun) widgetRun).f15602k.f15581l.iterator();
                while (it.hasNext()) {
                    a(it.next(), i10, 2, dependencyNode2, arrayList, runGroup);
                }
            }
        }
    }

    private boolean b(ConstraintWidgetContainer constraintWidgetContainer) {
        int i10;
        ConstraintWidget.b bVar;
        int i11;
        ConstraintWidget.b bVar2;
        ConstraintWidget.b bVar3;
        ConstraintWidget.b bVar4;
        Iterator<ConstraintWidget> it = constraintWidgetContainer.G0.iterator();
        while (it.hasNext()) {
            ConstraintWidget next = it.next();
            ConstraintWidget.b[] bVarArr = next.O;
            ConstraintWidget.b bVar5 = bVarArr[0];
            ConstraintWidget.b bVar6 = bVarArr[1];
            if (next.P() == 8) {
                next.f14750a = true;
            } else {
                if (next.f14782q < 1.0f && bVar5 == ConstraintWidget.b.MATCH_CONSTRAINT) {
                    next.f14772l = 2;
                }
                if (next.f14788t < 1.0f && bVar6 == ConstraintWidget.b.MATCH_CONSTRAINT) {
                    next.f14774m = 2;
                }
                if (next.u() > 0.0f) {
                    ConstraintWidget.b bVar7 = ConstraintWidget.b.MATCH_CONSTRAINT;
                    if (bVar5 == bVar7 && (bVar6 == ConstraintWidget.b.WRAP_CONTENT || bVar6 == ConstraintWidget.b.FIXED)) {
                        next.f14772l = 3;
                    } else if (bVar6 == bVar7 && (bVar5 == ConstraintWidget.b.WRAP_CONTENT || bVar5 == ConstraintWidget.b.FIXED)) {
                        next.f14774m = 3;
                    } else if (bVar5 == bVar7 && bVar6 == bVar7) {
                        if (next.f14772l == 0) {
                            next.f14772l = 3;
                        }
                        if (next.f14774m == 0) {
                            next.f14774m = 3;
                        }
                    }
                }
                ConstraintWidget.b bVar8 = ConstraintWidget.b.MATCH_CONSTRAINT;
                if (bVar5 == bVar8 && next.f14772l == 1 && (next.D.f14735d == null || next.F.f14735d == null)) {
                    bVar5 = ConstraintWidget.b.WRAP_CONTENT;
                }
                ConstraintWidget.b bVar9 = bVar5;
                if (bVar6 == bVar8 && next.f14774m == 1 && (next.E.f14735d == null || next.G.f14735d == null)) {
                    bVar6 = ConstraintWidget.b.WRAP_CONTENT;
                }
                ConstraintWidget.b bVar10 = bVar6;
                HorizontalWidgetRun horizontalWidgetRun = next.f14758e;
                horizontalWidgetRun.f15608d = bVar9;
                int i12 = next.f14772l;
                horizontalWidgetRun.f15605a = i12;
                VerticalWidgetRun verticalWidgetRun = next.f14760f;
                verticalWidgetRun.f15608d = bVar10;
                int i13 = next.f14774m;
                verticalWidgetRun.f15605a = i13;
                ConstraintWidget.b bVar11 = ConstraintWidget.b.MATCH_PARENT;
                if ((bVar9 == bVar11 || bVar9 == ConstraintWidget.b.FIXED || bVar9 == ConstraintWidget.b.WRAP_CONTENT) && (bVar10 == bVar11 || bVar10 == ConstraintWidget.b.FIXED || bVar10 == ConstraintWidget.b.WRAP_CONTENT)) {
                    int Q = next.Q();
                    if (bVar9 == bVar11) {
                        i10 = (constraintWidgetContainer.Q() - next.D.f14736e) - next.F.f14736e;
                        bVar = ConstraintWidget.b.FIXED;
                    } else {
                        i10 = Q;
                        bVar = bVar9;
                    }
                    int w10 = next.w();
                    if (bVar10 == bVar11) {
                        i11 = (constraintWidgetContainer.w() - next.E.f14736e) - next.G.f14736e;
                        bVar2 = ConstraintWidget.b.FIXED;
                    } else {
                        i11 = w10;
                        bVar2 = bVar10;
                    }
                    l(next, bVar, i10, bVar2, i11);
                    next.f14758e.f15609e.d(next.Q());
                    next.f14760f.f15609e.d(next.w());
                    next.f14750a = true;
                } else {
                    if (bVar9 == bVar8 && (bVar10 == (bVar4 = ConstraintWidget.b.WRAP_CONTENT) || bVar10 == ConstraintWidget.b.FIXED)) {
                        if (i12 == 3) {
                            if (bVar10 == bVar4) {
                                l(next, bVar4, 0, bVar4, 0);
                            }
                            int w11 = next.w();
                            int i14 = (int) ((w11 * next.S) + 0.5f);
                            ConstraintWidget.b bVar12 = ConstraintWidget.b.FIXED;
                            l(next, bVar12, i14, bVar12, w11);
                            next.f14758e.f15609e.d(next.Q());
                            next.f14760f.f15609e.d(next.w());
                            next.f14750a = true;
                        } else if (i12 == 1) {
                            l(next, bVar4, 0, bVar10, 0);
                            next.f14758e.f15609e.f15591m = next.Q();
                        } else if (i12 == 2) {
                            ConstraintWidget.b[] bVarArr2 = constraintWidgetContainer.O;
                            ConstraintWidget.b bVar13 = bVarArr2[0];
                            ConstraintWidget.b bVar14 = ConstraintWidget.b.FIXED;
                            if (bVar13 == bVar14 || bVarArr2[0] == bVar11) {
                                l(next, bVar14, (int) ((next.f14782q * constraintWidgetContainer.Q()) + 0.5f), bVar10, next.w());
                                next.f14758e.f15609e.d(next.Q());
                                next.f14760f.f15609e.d(next.w());
                                next.f14750a = true;
                            }
                        } else {
                            ConstraintAnchor[] constraintAnchorArr = next.L;
                            if (constraintAnchorArr[0].f14735d == null || constraintAnchorArr[1].f14735d == null) {
                                l(next, bVar4, 0, bVar10, 0);
                                next.f14758e.f15609e.d(next.Q());
                                next.f14760f.f15609e.d(next.w());
                                next.f14750a = true;
                            }
                        }
                    }
                    if (bVar10 == bVar8 && (bVar9 == (bVar3 = ConstraintWidget.b.WRAP_CONTENT) || bVar9 == ConstraintWidget.b.FIXED)) {
                        if (i13 == 3) {
                            if (bVar9 == bVar3) {
                                l(next, bVar3, 0, bVar3, 0);
                            }
                            int Q2 = next.Q();
                            float f10 = next.S;
                            if (next.v() == -1) {
                                f10 = 1.0f / f10;
                            }
                            ConstraintWidget.b bVar15 = ConstraintWidget.b.FIXED;
                            l(next, bVar15, Q2, bVar15, (int) ((Q2 * f10) + 0.5f));
                            next.f14758e.f15609e.d(next.Q());
                            next.f14760f.f15609e.d(next.w());
                            next.f14750a = true;
                        } else if (i13 == 1) {
                            l(next, bVar9, 0, bVar3, 0);
                            next.f14760f.f15609e.f15591m = next.w();
                        } else if (i13 == 2) {
                            ConstraintWidget.b[] bVarArr3 = constraintWidgetContainer.O;
                            ConstraintWidget.b bVar16 = bVarArr3[1];
                            ConstraintWidget.b bVar17 = ConstraintWidget.b.FIXED;
                            if (bVar16 == bVar17 || bVarArr3[1] == bVar11) {
                                l(next, bVar9, next.Q(), bVar17, (int) ((next.f14788t * constraintWidgetContainer.w()) + 0.5f));
                                next.f14758e.f15609e.d(next.Q());
                                next.f14760f.f15609e.d(next.w());
                                next.f14750a = true;
                            }
                        } else {
                            ConstraintAnchor[] constraintAnchorArr2 = next.L;
                            if (constraintAnchorArr2[2].f14735d == null || constraintAnchorArr2[3].f14735d == null) {
                                l(next, bVar3, 0, bVar10, 0);
                                next.f14758e.f15609e.d(next.Q());
                                next.f14760f.f15609e.d(next.w());
                                next.f14750a = true;
                            }
                        }
                    }
                    if (bVar9 == bVar8 && bVar10 == bVar8) {
                        if (i12 == 1 || i13 == 1) {
                            ConstraintWidget.b bVar18 = ConstraintWidget.b.WRAP_CONTENT;
                            l(next, bVar18, 0, bVar18, 0);
                            next.f14758e.f15609e.f15591m = next.Q();
                            next.f14760f.f15609e.f15591m = next.w();
                        } else if (i13 == 2 && i12 == 2) {
                            ConstraintWidget.b[] bVarArr4 = constraintWidgetContainer.O;
                            ConstraintWidget.b bVar19 = bVarArr4[0];
                            ConstraintWidget.b bVar20 = ConstraintWidget.b.FIXED;
                            if (bVar19 == bVar20 || bVarArr4[0] == bVar20) {
                                if (bVarArr4[1] == bVar20 || bVarArr4[1] == bVar20) {
                                    l(next, bVar20, (int) ((next.f14782q * constraintWidgetContainer.Q()) + 0.5f), bVar20, (int) ((next.f14788t * constraintWidgetContainer.w()) + 0.5f));
                                    next.f14758e.f15609e.d(next.Q());
                                    next.f14760f.f15609e.d(next.w());
                                    next.f14750a = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private int e(ConstraintWidgetContainer constraintWidgetContainer, int i10) {
        int size = this.f15569i.size();
        long j10 = 0;
        for (int i11 = 0; i11 < size; i11++) {
            j10 = Math.max(j10, this.f15569i.get(i11).b(constraintWidgetContainer, i10));
        }
        return (int) j10;
    }

    private void i(WidgetRun widgetRun, int i10, ArrayList<RunGroup> arrayList) {
        for (Dependency dependency : widgetRun.f15612h.f15580k) {
            if (dependency instanceof DependencyNode) {
                a((DependencyNode) dependency, i10, 0, widgetRun.f15613i, arrayList, null);
            } else if (dependency instanceof WidgetRun) {
                a(((WidgetRun) dependency).f15612h, i10, 0, widgetRun.f15613i, arrayList, null);
            }
        }
        for (Dependency dependency2 : widgetRun.f15613i.f15580k) {
            if (dependency2 instanceof DependencyNode) {
                a((DependencyNode) dependency2, i10, 1, widgetRun.f15612h, arrayList, null);
            } else if (dependency2 instanceof WidgetRun) {
                a(((WidgetRun) dependency2).f15613i, i10, 1, widgetRun.f15612h, arrayList, null);
            }
        }
        if (i10 == 1) {
            for (Dependency dependency3 : ((VerticalWidgetRun) widgetRun).f15602k.f15580k) {
                if (dependency3 instanceof DependencyNode) {
                    a((DependencyNode) dependency3, i10, 2, null, arrayList, null);
                }
            }
        }
    }

    private void l(ConstraintWidget constraintWidget, ConstraintWidget.b bVar, int i10, ConstraintWidget.b bVar2, int i11) {
        BasicMeasure.a aVar = this.f15568h;
        aVar.f15549a = bVar;
        aVar.f15550b = bVar2;
        aVar.f15551c = i10;
        aVar.f15552d = i11;
        this.f15567g.b(constraintWidget, aVar);
        constraintWidget.F0(this.f15568h.f15553e);
        constraintWidget.i0(this.f15568h.f15554f);
        constraintWidget.h0(this.f15568h.f15556h);
        constraintWidget.c0(this.f15568h.f15555g);
    }

    public void c() {
        d(this.f15565e);
        this.f15569i.clear();
        RunGroup.f15594h = 0;
        i(this.f15561a.f14758e, 0, this.f15569i);
        i(this.f15561a.f14760f, 1, this.f15569i);
        this.f15562b = false;
    }

    public void d(ArrayList<WidgetRun> arrayList) {
        arrayList.clear();
        this.f15564d.f14758e.f();
        this.f15564d.f14760f.f();
        arrayList.add(this.f15564d.f14758e);
        arrayList.add(this.f15564d.f14760f);
        Iterator<ConstraintWidget> it = this.f15564d.G0.iterator();
        HashSet hashSet = null;
        while (it.hasNext()) {
            ConstraintWidget next = it.next();
            if (next instanceof m.h) {
                arrayList.add(new GuidelineReference(next));
            } else {
                if (next.W()) {
                    if (next.f14754c == null) {
                        next.f14754c = new ChainRun(next, 0);
                    }
                    if (hashSet == null) {
                        hashSet = new HashSet();
                    }
                    hashSet.add(next.f14754c);
                } else {
                    arrayList.add(next.f14758e);
                }
                if (next.Y()) {
                    if (next.f14756d == null) {
                        next.f14756d = new ChainRun(next, 1);
                    }
                    if (hashSet == null) {
                        hashSet = new HashSet();
                    }
                    hashSet.add(next.f14756d);
                } else {
                    arrayList.add(next.f14760f);
                }
                if (next instanceof HelperWidget) {
                    arrayList.add(new HelperReferences(next));
                }
            }
        }
        if (hashSet != null) {
            arrayList.addAll(hashSet);
        }
        Iterator<WidgetRun> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            it2.next().f();
        }
        Iterator<WidgetRun> it3 = arrayList.iterator();
        while (it3.hasNext()) {
            WidgetRun next2 = it3.next();
            if (next2.f15606b != this.f15564d) {
                next2.d();
            }
        }
    }

    public boolean f(boolean z10) {
        boolean z11;
        boolean z12 = true;
        boolean z13 = z10 & true;
        if (this.f15562b || this.f15563c) {
            Iterator<ConstraintWidget> it = this.f15561a.G0.iterator();
            while (it.hasNext()) {
                ConstraintWidget next = it.next();
                next.f14750a = false;
                next.f14758e.r();
                next.f14760f.q();
            }
            ConstraintWidgetContainer constraintWidgetContainer = this.f15561a;
            constraintWidgetContainer.f14750a = false;
            constraintWidgetContainer.f14758e.r();
            this.f15561a.f14760f.q();
            this.f15563c = false;
        }
        if (b(this.f15564d)) {
            return false;
        }
        this.f15561a.G0(0);
        this.f15561a.H0(0);
        ConstraintWidget.b t7 = this.f15561a.t(0);
        ConstraintWidget.b t10 = this.f15561a.t(1);
        if (this.f15562b) {
            c();
        }
        int R = this.f15561a.R();
        int S = this.f15561a.S();
        this.f15561a.f14758e.f15612h.d(R);
        this.f15561a.f14760f.f15612h.d(S);
        m();
        ConstraintWidget.b bVar = ConstraintWidget.b.WRAP_CONTENT;
        if (t7 == bVar || t10 == bVar) {
            if (z13) {
                Iterator<WidgetRun> it2 = this.f15565e.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    if (!it2.next().m()) {
                        z13 = false;
                        break;
                    }
                }
            }
            if (z13 && t7 == ConstraintWidget.b.WRAP_CONTENT) {
                this.f15561a.m0(ConstraintWidget.b.FIXED);
                ConstraintWidgetContainer constraintWidgetContainer2 = this.f15561a;
                constraintWidgetContainer2.F0(e(constraintWidgetContainer2, 0));
                ConstraintWidgetContainer constraintWidgetContainer3 = this.f15561a;
                constraintWidgetContainer3.f14758e.f15609e.d(constraintWidgetContainer3.Q());
            }
            if (z13 && t10 == ConstraintWidget.b.WRAP_CONTENT) {
                this.f15561a.B0(ConstraintWidget.b.FIXED);
                ConstraintWidgetContainer constraintWidgetContainer4 = this.f15561a;
                constraintWidgetContainer4.i0(e(constraintWidgetContainer4, 1));
                ConstraintWidgetContainer constraintWidgetContainer5 = this.f15561a;
                constraintWidgetContainer5.f14760f.f15609e.d(constraintWidgetContainer5.w());
            }
        }
        ConstraintWidgetContainer constraintWidgetContainer6 = this.f15561a;
        ConstraintWidget.b[] bVarArr = constraintWidgetContainer6.O;
        ConstraintWidget.b bVar2 = bVarArr[0];
        ConstraintWidget.b bVar3 = ConstraintWidget.b.FIXED;
        if (bVar2 == bVar3 || bVarArr[0] == ConstraintWidget.b.MATCH_PARENT) {
            int Q = constraintWidgetContainer6.Q() + R;
            this.f15561a.f14758e.f15613i.d(Q);
            this.f15561a.f14758e.f15609e.d(Q - R);
            m();
            ConstraintWidgetContainer constraintWidgetContainer7 = this.f15561a;
            ConstraintWidget.b[] bVarArr2 = constraintWidgetContainer7.O;
            if (bVarArr2[1] == bVar3 || bVarArr2[1] == ConstraintWidget.b.MATCH_PARENT) {
                int w10 = constraintWidgetContainer7.w() + S;
                this.f15561a.f14760f.f15613i.d(w10);
                this.f15561a.f14760f.f15609e.d(w10 - S);
            }
            m();
            z11 = true;
        } else {
            z11 = false;
        }
        Iterator<WidgetRun> it3 = this.f15565e.iterator();
        while (it3.hasNext()) {
            WidgetRun next2 = it3.next();
            if (next2.f15606b != this.f15561a || next2.f15611g) {
                next2.e();
            }
        }
        Iterator<WidgetRun> it4 = this.f15565e.iterator();
        while (it4.hasNext()) {
            WidgetRun next3 = it4.next();
            if (z11 || next3.f15606b != this.f15561a) {
                if (!next3.f15612h.f15579j || ((!next3.f15613i.f15579j && !(next3 instanceof GuidelineReference)) || (!next3.f15609e.f15579j && !(next3 instanceof ChainRun) && !(next3 instanceof GuidelineReference)))) {
                    z12 = false;
                    break;
                }
            }
        }
        this.f15561a.m0(t7);
        this.f15561a.B0(t10);
        return z12;
    }

    public boolean g(boolean z10) {
        if (this.f15562b) {
            Iterator<ConstraintWidget> it = this.f15561a.G0.iterator();
            while (it.hasNext()) {
                ConstraintWidget next = it.next();
                next.f14750a = false;
                HorizontalWidgetRun horizontalWidgetRun = next.f14758e;
                horizontalWidgetRun.f15609e.f15579j = false;
                horizontalWidgetRun.f15611g = false;
                horizontalWidgetRun.r();
                VerticalWidgetRun verticalWidgetRun = next.f14760f;
                verticalWidgetRun.f15609e.f15579j = false;
                verticalWidgetRun.f15611g = false;
                verticalWidgetRun.q();
            }
            ConstraintWidgetContainer constraintWidgetContainer = this.f15561a;
            constraintWidgetContainer.f14750a = false;
            HorizontalWidgetRun horizontalWidgetRun2 = constraintWidgetContainer.f14758e;
            horizontalWidgetRun2.f15609e.f15579j = false;
            horizontalWidgetRun2.f15611g = false;
            horizontalWidgetRun2.r();
            VerticalWidgetRun verticalWidgetRun2 = this.f15561a.f14760f;
            verticalWidgetRun2.f15609e.f15579j = false;
            verticalWidgetRun2.f15611g = false;
            verticalWidgetRun2.q();
            c();
        }
        if (b(this.f15564d)) {
            return false;
        }
        this.f15561a.G0(0);
        this.f15561a.H0(0);
        this.f15561a.f14758e.f15612h.d(0);
        this.f15561a.f14760f.f15612h.d(0);
        return true;
    }

    public boolean h(boolean z10, int i10) {
        boolean z11;
        ConstraintWidget.b bVar;
        boolean z12 = true;
        boolean z13 = z10 & true;
        ConstraintWidget.b t7 = this.f15561a.t(0);
        ConstraintWidget.b t10 = this.f15561a.t(1);
        int R = this.f15561a.R();
        int S = this.f15561a.S();
        if (z13 && (t7 == (bVar = ConstraintWidget.b.WRAP_CONTENT) || t10 == bVar)) {
            Iterator<WidgetRun> it = this.f15565e.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                WidgetRun next = it.next();
                if (next.f15610f == i10 && !next.m()) {
                    z13 = false;
                    break;
                }
            }
            if (i10 == 0) {
                if (z13 && t7 == ConstraintWidget.b.WRAP_CONTENT) {
                    this.f15561a.m0(ConstraintWidget.b.FIXED);
                    ConstraintWidgetContainer constraintWidgetContainer = this.f15561a;
                    constraintWidgetContainer.F0(e(constraintWidgetContainer, 0));
                    ConstraintWidgetContainer constraintWidgetContainer2 = this.f15561a;
                    constraintWidgetContainer2.f14758e.f15609e.d(constraintWidgetContainer2.Q());
                }
            } else if (z13 && t10 == ConstraintWidget.b.WRAP_CONTENT) {
                this.f15561a.B0(ConstraintWidget.b.FIXED);
                ConstraintWidgetContainer constraintWidgetContainer3 = this.f15561a;
                constraintWidgetContainer3.i0(e(constraintWidgetContainer3, 1));
                ConstraintWidgetContainer constraintWidgetContainer4 = this.f15561a;
                constraintWidgetContainer4.f14760f.f15609e.d(constraintWidgetContainer4.w());
            }
        }
        if (i10 == 0) {
            ConstraintWidgetContainer constraintWidgetContainer5 = this.f15561a;
            ConstraintWidget.b[] bVarArr = constraintWidgetContainer5.O;
            if (bVarArr[0] == ConstraintWidget.b.FIXED || bVarArr[0] == ConstraintWidget.b.MATCH_PARENT) {
                int Q = constraintWidgetContainer5.Q() + R;
                this.f15561a.f14758e.f15613i.d(Q);
                this.f15561a.f14758e.f15609e.d(Q - R);
                z11 = true;
            }
            z11 = false;
        } else {
            ConstraintWidgetContainer constraintWidgetContainer6 = this.f15561a;
            ConstraintWidget.b[] bVarArr2 = constraintWidgetContainer6.O;
            if (bVarArr2[1] == ConstraintWidget.b.FIXED || bVarArr2[1] == ConstraintWidget.b.MATCH_PARENT) {
                int w10 = constraintWidgetContainer6.w() + S;
                this.f15561a.f14760f.f15613i.d(w10);
                this.f15561a.f14760f.f15609e.d(w10 - S);
                z11 = true;
            }
            z11 = false;
        }
        m();
        Iterator<WidgetRun> it2 = this.f15565e.iterator();
        while (it2.hasNext()) {
            WidgetRun next2 = it2.next();
            if (next2.f15610f == i10 && (next2.f15606b != this.f15561a || next2.f15611g)) {
                next2.e();
            }
        }
        Iterator<WidgetRun> it3 = this.f15565e.iterator();
        while (it3.hasNext()) {
            WidgetRun next3 = it3.next();
            if (next3.f15610f == i10 && (z11 || next3.f15606b != this.f15561a)) {
                if (!next3.f15612h.f15579j || !next3.f15613i.f15579j || (!(next3 instanceof ChainRun) && !next3.f15609e.f15579j)) {
                    z12 = false;
                    break;
                }
            }
        }
        this.f15561a.m0(t7);
        this.f15561a.B0(t10);
        return z12;
    }

    public void j() {
        this.f15562b = true;
    }

    public void k() {
        this.f15563c = true;
    }

    public void m() {
        DimensionDependency dimensionDependency;
        Iterator<ConstraintWidget> it = this.f15561a.G0.iterator();
        while (it.hasNext()) {
            ConstraintWidget next = it.next();
            if (!next.f14750a) {
                ConstraintWidget.b[] bVarArr = next.O;
                boolean z10 = false;
                ConstraintWidget.b bVar = bVarArr[0];
                ConstraintWidget.b bVar2 = bVarArr[1];
                int i10 = next.f14772l;
                int i11 = next.f14774m;
                ConstraintWidget.b bVar3 = ConstraintWidget.b.WRAP_CONTENT;
                boolean z11 = bVar == bVar3 || (bVar == ConstraintWidget.b.MATCH_CONSTRAINT && i10 == 1);
                if (bVar2 == bVar3 || (bVar2 == ConstraintWidget.b.MATCH_CONSTRAINT && i11 == 1)) {
                    z10 = true;
                }
                DimensionDependency dimensionDependency2 = next.f14758e.f15609e;
                boolean z12 = dimensionDependency2.f15579j;
                DimensionDependency dimensionDependency3 = next.f14760f.f15609e;
                boolean z13 = dimensionDependency3.f15579j;
                if (z12 && z13) {
                    ConstraintWidget.b bVar4 = ConstraintWidget.b.FIXED;
                    l(next, bVar4, dimensionDependency2.f15576g, bVar4, dimensionDependency3.f15576g);
                    next.f14750a = true;
                } else if (z12 && z10) {
                    l(next, ConstraintWidget.b.FIXED, dimensionDependency2.f15576g, bVar3, dimensionDependency3.f15576g);
                    if (bVar2 == ConstraintWidget.b.MATCH_CONSTRAINT) {
                        next.f14760f.f15609e.f15591m = next.w();
                    } else {
                        next.f14760f.f15609e.d(next.w());
                        next.f14750a = true;
                    }
                } else if (z13 && z11) {
                    l(next, bVar3, dimensionDependency2.f15576g, ConstraintWidget.b.FIXED, dimensionDependency3.f15576g);
                    if (bVar == ConstraintWidget.b.MATCH_CONSTRAINT) {
                        next.f14758e.f15609e.f15591m = next.Q();
                    } else {
                        next.f14758e.f15609e.d(next.Q());
                        next.f14750a = true;
                    }
                }
                if (next.f14750a && (dimensionDependency = next.f14760f.f15603l) != null) {
                    dimensionDependency.d(next.o());
                }
            }
        }
    }

    public void n(BasicMeasure.b bVar) {
        this.f15567g = bVar;
    }
}
