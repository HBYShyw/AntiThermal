package n;

import java.util.ArrayList;
import m.ConstraintWidgetContainer;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: RunGroup.java */
/* renamed from: n.k, reason: use source file name */
/* loaded from: classes.dex */
public class RunGroup {

    /* renamed from: h, reason: collision with root package name */
    public static int f15594h;

    /* renamed from: c, reason: collision with root package name */
    WidgetRun f15597c;

    /* renamed from: d, reason: collision with root package name */
    WidgetRun f15598d;

    /* renamed from: f, reason: collision with root package name */
    int f15600f;

    /* renamed from: g, reason: collision with root package name */
    int f15601g;

    /* renamed from: a, reason: collision with root package name */
    public int f15595a = 0;

    /* renamed from: b, reason: collision with root package name */
    public boolean f15596b = false;

    /* renamed from: e, reason: collision with root package name */
    ArrayList<WidgetRun> f15599e = new ArrayList<>();

    public RunGroup(WidgetRun widgetRun, int i10) {
        this.f15597c = null;
        this.f15598d = null;
        int i11 = f15594h;
        this.f15600f = i11;
        f15594h = i11 + 1;
        this.f15597c = widgetRun;
        this.f15598d = widgetRun;
        this.f15601g = i10;
    }

    private long c(DependencyNode dependencyNode, long j10) {
        WidgetRun widgetRun = dependencyNode.f15573d;
        if (widgetRun instanceof HelperReferences) {
            return j10;
        }
        int size = dependencyNode.f15580k.size();
        long j11 = j10;
        for (int i10 = 0; i10 < size; i10++) {
            Dependency dependency = dependencyNode.f15580k.get(i10);
            if (dependency instanceof DependencyNode) {
                DependencyNode dependencyNode2 = (DependencyNode) dependency;
                if (dependencyNode2.f15573d != widgetRun) {
                    j11 = Math.min(j11, c(dependencyNode2, dependencyNode2.f15575f + j10));
                }
            }
        }
        if (dependencyNode != widgetRun.f15613i) {
            return j11;
        }
        long j12 = j10 - widgetRun.j();
        return Math.min(Math.min(j11, c(widgetRun.f15612h, j12)), j12 - widgetRun.f15612h.f15575f);
    }

    private long d(DependencyNode dependencyNode, long j10) {
        WidgetRun widgetRun = dependencyNode.f15573d;
        if (widgetRun instanceof HelperReferences) {
            return j10;
        }
        int size = dependencyNode.f15580k.size();
        long j11 = j10;
        for (int i10 = 0; i10 < size; i10++) {
            Dependency dependency = dependencyNode.f15580k.get(i10);
            if (dependency instanceof DependencyNode) {
                DependencyNode dependencyNode2 = (DependencyNode) dependency;
                if (dependencyNode2.f15573d != widgetRun) {
                    j11 = Math.max(j11, d(dependencyNode2, dependencyNode2.f15575f + j10));
                }
            }
        }
        if (dependencyNode != widgetRun.f15612h) {
            return j11;
        }
        long j12 = j10 + widgetRun.j();
        return Math.max(Math.max(j11, d(widgetRun.f15613i, j12)), j12 - widgetRun.f15613i.f15575f);
    }

    public void a(WidgetRun widgetRun) {
        this.f15599e.add(widgetRun);
        this.f15598d = widgetRun;
    }

    public long b(ConstraintWidgetContainer constraintWidgetContainer, int i10) {
        WidgetRun widgetRun = this.f15597c;
        if (widgetRun instanceof ChainRun) {
            if (((ChainRun) widgetRun).f15610f != i10) {
                return 0L;
            }
        } else if (i10 == 0) {
            if (!(widgetRun instanceof HorizontalWidgetRun)) {
                return 0L;
            }
        } else if (!(widgetRun instanceof VerticalWidgetRun)) {
            return 0L;
        }
        DependencyNode dependencyNode = (i10 == 0 ? constraintWidgetContainer.f14758e : constraintWidgetContainer.f14760f).f15612h;
        DependencyNode dependencyNode2 = (i10 == 0 ? constraintWidgetContainer.f14758e : constraintWidgetContainer.f14760f).f15613i;
        boolean contains = widgetRun.f15612h.f15581l.contains(dependencyNode);
        boolean contains2 = this.f15597c.f15613i.f15581l.contains(dependencyNode2);
        long j10 = this.f15597c.j();
        if (!contains || !contains2) {
            if (contains) {
                return Math.max(d(this.f15597c.f15612h, r12.f15575f), this.f15597c.f15612h.f15575f + j10);
            }
            if (contains2) {
                return Math.max(-c(this.f15597c.f15613i, r12.f15575f), (-this.f15597c.f15613i.f15575f) + j10);
            }
            return (r12.f15612h.f15575f + this.f15597c.j()) - this.f15597c.f15613i.f15575f;
        }
        long d10 = d(this.f15597c.f15612h, 0L);
        long c10 = c(this.f15597c.f15613i, 0L);
        long j11 = d10 - j10;
        WidgetRun widgetRun2 = this.f15597c;
        int i11 = widgetRun2.f15613i.f15575f;
        if (j11 >= (-i11)) {
            j11 += i11;
        }
        int i12 = widgetRun2.f15612h.f15575f;
        long j12 = ((-c10) - j10) - i12;
        if (j12 >= i12) {
            j12 -= i12;
        }
        float f10 = (float) (widgetRun2.f15606b.p(i10) > 0.0f ? (((float) j12) / r12) + (((float) j11) / (1.0f - r12)) : 0L);
        long j13 = (f10 * r12) + 0.5f + j10 + (f10 * (1.0f - r12)) + 0.5f;
        WidgetRun widgetRun3 = this.f15597c;
        return (widgetRun3.f15612h.f15575f + j13) - widgetRun3.f15613i.f15575f;
    }
}
