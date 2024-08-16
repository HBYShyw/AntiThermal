package n;

import java.util.ArrayList;
import l.LinearSystem;
import m.ConstraintAnchor;
import m.ConstraintWidget;
import m.ConstraintWidgetContainer;
import m.Helper;
import m.Optimizer;

/* compiled from: BasicMeasure.java */
/* renamed from: n.b, reason: use source file name */
/* loaded from: classes.dex */
public class BasicMeasure {

    /* renamed from: a, reason: collision with root package name */
    private final ArrayList<ConstraintWidget> f15546a = new ArrayList<>();

    /* renamed from: b, reason: collision with root package name */
    private a f15547b = new a();

    /* renamed from: c, reason: collision with root package name */
    private ConstraintWidgetContainer f15548c;

    /* compiled from: BasicMeasure.java */
    /* renamed from: n.b$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        public ConstraintWidget.b f15549a;

        /* renamed from: b, reason: collision with root package name */
        public ConstraintWidget.b f15550b;

        /* renamed from: c, reason: collision with root package name */
        public int f15551c;

        /* renamed from: d, reason: collision with root package name */
        public int f15552d;

        /* renamed from: e, reason: collision with root package name */
        public int f15553e;

        /* renamed from: f, reason: collision with root package name */
        public int f15554f;

        /* renamed from: g, reason: collision with root package name */
        public int f15555g;

        /* renamed from: h, reason: collision with root package name */
        public boolean f15556h;

        /* renamed from: i, reason: collision with root package name */
        public boolean f15557i;

        /* renamed from: j, reason: collision with root package name */
        public boolean f15558j;
    }

    /* compiled from: BasicMeasure.java */
    /* renamed from: n.b$b */
    /* loaded from: classes.dex */
    public interface b {
        void a();

        void b(ConstraintWidget constraintWidget, a aVar);
    }

    public BasicMeasure(ConstraintWidgetContainer constraintWidgetContainer) {
        this.f15548c = constraintWidgetContainer;
    }

    private boolean a(b bVar, ConstraintWidget constraintWidget, boolean z10) {
        this.f15547b.f15549a = constraintWidget.z();
        this.f15547b.f15550b = constraintWidget.N();
        this.f15547b.f15551c = constraintWidget.Q();
        this.f15547b.f15552d = constraintWidget.w();
        a aVar = this.f15547b;
        aVar.f15557i = false;
        aVar.f15558j = z10;
        ConstraintWidget.b bVar2 = aVar.f15549a;
        ConstraintWidget.b bVar3 = ConstraintWidget.b.MATCH_CONSTRAINT;
        boolean z11 = bVar2 == bVar3;
        boolean z12 = aVar.f15550b == bVar3;
        boolean z13 = z11 && constraintWidget.S > 0.0f;
        boolean z14 = z12 && constraintWidget.S > 0.0f;
        if (z13 && constraintWidget.f14776n[0] == 4) {
            aVar.f15549a = ConstraintWidget.b.FIXED;
        }
        if (z14 && constraintWidget.f14776n[1] == 4) {
            aVar.f15550b = ConstraintWidget.b.FIXED;
        }
        bVar.b(constraintWidget, aVar);
        constraintWidget.F0(this.f15547b.f15553e);
        constraintWidget.i0(this.f15547b.f15554f);
        constraintWidget.h0(this.f15547b.f15556h);
        constraintWidget.c0(this.f15547b.f15555g);
        a aVar2 = this.f15547b;
        aVar2.f15558j = false;
        return aVar2.f15557i;
    }

    private void b(ConstraintWidgetContainer constraintWidgetContainer) {
        int size = constraintWidgetContainer.G0.size();
        b W0 = constraintWidgetContainer.W0();
        for (int i10 = 0; i10 < size; i10++) {
            ConstraintWidget constraintWidget = constraintWidgetContainer.G0.get(i10);
            if (!(constraintWidget instanceof m.h) && (!constraintWidget.f14758e.f15609e.f15579j || !constraintWidget.f14760f.f15609e.f15579j)) {
                ConstraintWidget.b t7 = constraintWidget.t(0);
                ConstraintWidget.b t10 = constraintWidget.t(1);
                ConstraintWidget.b bVar = ConstraintWidget.b.MATCH_CONSTRAINT;
                if (!(t7 == bVar && constraintWidget.f14772l != 1 && t10 == bVar && constraintWidget.f14774m != 1)) {
                    a(W0, constraintWidget, false);
                }
            }
        }
        W0.a();
    }

    private void c(ConstraintWidgetContainer constraintWidgetContainer, String str, int i10, int i11) {
        int F = constraintWidgetContainer.F();
        int E = constraintWidgetContainer.E();
        constraintWidgetContainer.v0(0);
        constraintWidgetContainer.u0(0);
        constraintWidgetContainer.F0(i10);
        constraintWidgetContainer.i0(i11);
        constraintWidgetContainer.v0(F);
        constraintWidgetContainer.u0(E);
        this.f15548c.M0();
    }

    public long d(ConstraintWidgetContainer constraintWidgetContainer, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18) {
        boolean z10;
        int i19;
        int i20;
        boolean z11;
        boolean z12;
        int i21;
        int i22;
        b bVar;
        int i23;
        boolean z13;
        boolean z14;
        int i24;
        b W0 = constraintWidgetContainer.W0();
        int size = constraintWidgetContainer.G0.size();
        int Q = constraintWidgetContainer.Q();
        int w10 = constraintWidgetContainer.w();
        boolean b10 = Optimizer.b(i10, 128);
        boolean z15 = b10 || Optimizer.b(i10, 64);
        if (z15) {
            for (int i25 = 0; i25 < size; i25++) {
                ConstraintWidget constraintWidget = constraintWidgetContainer.G0.get(i25);
                ConstraintWidget.b z16 = constraintWidget.z();
                ConstraintWidget.b bVar2 = ConstraintWidget.b.MATCH_CONSTRAINT;
                boolean z17 = (z16 == bVar2) && (constraintWidget.N() == bVar2) && constraintWidget.u() > 0.0f;
                if ((constraintWidget.W() && z17) || ((constraintWidget.Y() && z17) || (constraintWidget instanceof m.l) || constraintWidget.W() || constraintWidget.Y())) {
                    z15 = false;
                    break;
                }
            }
        }
        if (z15) {
            boolean z18 = LinearSystem.f14500r;
        }
        int i26 = 2;
        if (z15 && ((i13 == 1073741824 && i15 == 1073741824) || b10)) {
            int min = Math.min(constraintWidgetContainer.D(), i14);
            int min2 = Math.min(constraintWidgetContainer.C(), i16);
            if (i13 == 1073741824 && constraintWidgetContainer.Q() != min) {
                constraintWidgetContainer.F0(min);
                constraintWidgetContainer.Z0();
            }
            if (i15 == 1073741824 && constraintWidgetContainer.w() != min2) {
                constraintWidgetContainer.i0(min2);
                constraintWidgetContainer.Z0();
            }
            if (i13 == 1073741824 && i15 == 1073741824) {
                z10 = constraintWidgetContainer.T0(b10);
                i19 = 2;
            } else {
                boolean U0 = constraintWidgetContainer.U0(b10);
                if (i13 == 1073741824) {
                    z14 = U0 & constraintWidgetContainer.V0(b10, 0);
                    i24 = 1;
                } else {
                    z14 = U0;
                    i24 = 0;
                }
                if (i15 == 1073741824) {
                    boolean V0 = constraintWidgetContainer.V0(b10, 1) & z14;
                    i19 = i24 + 1;
                    z10 = V0;
                } else {
                    i19 = i24;
                    z10 = z14;
                }
            }
            if (z10) {
                constraintWidgetContainer.J0(i13 == 1073741824, i15 == 1073741824);
            }
        } else {
            z10 = false;
            i19 = 0;
        }
        if (z10 && i19 == 2) {
            return 0L;
        }
        if (size > 0) {
            b(constraintWidgetContainer);
        }
        int X0 = constraintWidgetContainer.X0();
        int size2 = this.f15546a.size();
        if (size > 0) {
            c(constraintWidgetContainer, "First pass", Q, w10);
        }
        if (size2 > 0) {
            ConstraintWidget.b z19 = constraintWidgetContainer.z();
            ConstraintWidget.b bVar3 = ConstraintWidget.b.WRAP_CONTENT;
            boolean z20 = z19 == bVar3;
            boolean z21 = constraintWidgetContainer.N() == bVar3;
            int max = Math.max(constraintWidgetContainer.Q(), this.f15548c.F());
            int max2 = Math.max(constraintWidgetContainer.w(), this.f15548c.E());
            int i27 = 0;
            boolean z22 = false;
            while (i27 < size2) {
                ConstraintWidget constraintWidget2 = this.f15546a.get(i27);
                if (constraintWidget2 instanceof m.l) {
                    int Q2 = constraintWidget2.Q();
                    int w11 = constraintWidget2.w();
                    i23 = X0;
                    boolean a10 = z22 | a(W0, constraintWidget2, true);
                    int Q3 = constraintWidget2.Q();
                    int w12 = constraintWidget2.w();
                    if (Q3 != Q2) {
                        constraintWidget2.F0(Q3);
                        if (z20 && constraintWidget2.J() > max) {
                            max = Math.max(max, constraintWidget2.J() + constraintWidget2.n(ConstraintAnchor.b.RIGHT).c());
                        }
                        z13 = true;
                    } else {
                        z13 = a10;
                    }
                    if (w12 != w11) {
                        constraintWidget2.i0(w12);
                        if (z21 && constraintWidget2.q() > max2) {
                            max2 = Math.max(max2, constraintWidget2.q() + constraintWidget2.n(ConstraintAnchor.b.BOTTOM).c());
                        }
                        z13 = true;
                    }
                    z22 = z13 | ((m.l) constraintWidget2).W0();
                } else {
                    i23 = X0;
                }
                i27++;
                X0 = i23;
                i26 = 2;
            }
            int i28 = X0;
            int i29 = 0;
            for (int i30 = i26; i29 < i30; i30 = 2) {
                int i31 = 0;
                while (i31 < size2) {
                    ConstraintWidget constraintWidget3 = this.f15546a.get(i31);
                    if (((constraintWidget3 instanceof Helper) && !(constraintWidget3 instanceof m.l)) || (constraintWidget3 instanceof m.h) || constraintWidget3.P() == 8 || ((constraintWidget3.f14758e.f15609e.f15579j && constraintWidget3.f14760f.f15609e.f15579j) || (constraintWidget3 instanceof m.l))) {
                        i22 = i29;
                        i21 = size2;
                        bVar = W0;
                    } else {
                        int Q4 = constraintWidget3.Q();
                        int w13 = constraintWidget3.w();
                        i21 = size2;
                        int o10 = constraintWidget3.o();
                        i22 = i29;
                        z22 |= a(W0, constraintWidget3, true);
                        int Q5 = constraintWidget3.Q();
                        bVar = W0;
                        int w14 = constraintWidget3.w();
                        if (Q5 != Q4) {
                            constraintWidget3.F0(Q5);
                            if (z20 && constraintWidget3.J() > max) {
                                max = Math.max(max, constraintWidget3.J() + constraintWidget3.n(ConstraintAnchor.b.RIGHT).c());
                            }
                            z22 = true;
                        }
                        if (w14 != w13) {
                            constraintWidget3.i0(w14);
                            if (z21 && constraintWidget3.q() > max2) {
                                max2 = Math.max(max2, constraintWidget3.q() + constraintWidget3.n(ConstraintAnchor.b.BOTTOM).c());
                            }
                            z22 = true;
                        }
                        if (constraintWidget3.T() && o10 != constraintWidget3.o()) {
                            z22 = true;
                        }
                    }
                    i31++;
                    size2 = i21;
                    W0 = bVar;
                    i29 = i22;
                }
                int i32 = i29;
                int i33 = size2;
                b bVar4 = W0;
                if (z22) {
                    c(constraintWidgetContainer, "intermediate pass", Q, w10);
                    z22 = false;
                }
                i29 = i32 + 1;
                size2 = i33;
                W0 = bVar4;
            }
            if (z22) {
                c(constraintWidgetContainer, "2nd pass", Q, w10);
                if (constraintWidgetContainer.Q() < max) {
                    constraintWidgetContainer.F0(max);
                    z11 = true;
                } else {
                    z11 = false;
                }
                if (constraintWidgetContainer.w() < max2) {
                    constraintWidgetContainer.i0(max2);
                    z12 = true;
                } else {
                    z12 = z11;
                }
                if (z12) {
                    c(constraintWidgetContainer, "3rd pass", Q, w10);
                }
            }
            i20 = i28;
        } else {
            i20 = X0;
        }
        constraintWidgetContainer.i1(i20);
        return 0L;
    }

    public void e(ConstraintWidgetContainer constraintWidgetContainer) {
        int i10;
        this.f15546a.clear();
        int size = constraintWidgetContainer.G0.size();
        while (i10 < size) {
            ConstraintWidget constraintWidget = constraintWidgetContainer.G0.get(i10);
            ConstraintWidget.b z10 = constraintWidget.z();
            ConstraintWidget.b bVar = ConstraintWidget.b.MATCH_CONSTRAINT;
            if (z10 != bVar) {
                ConstraintWidget.b z11 = constraintWidget.z();
                ConstraintWidget.b bVar2 = ConstraintWidget.b.MATCH_PARENT;
                i10 = (z11 == bVar2 || constraintWidget.N() == bVar || constraintWidget.N() == bVar2) ? 0 : i10 + 1;
            }
            this.f15546a.add(constraintWidget);
        }
        constraintWidgetContainer.Z0();
    }
}
