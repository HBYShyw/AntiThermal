package n;

import java.util.ArrayList;
import java.util.Iterator;
import m.ConstraintAnchor;
import m.ConstraintWidget;
import m.ConstraintWidgetContainer;

/* compiled from: ChainRun.java */
/* renamed from: n.c, reason: use source file name */
/* loaded from: classes.dex */
public class ChainRun extends WidgetRun {

    /* renamed from: k, reason: collision with root package name */
    ArrayList<WidgetRun> f15559k;

    /* renamed from: l, reason: collision with root package name */
    private int f15560l;

    public ChainRun(ConstraintWidget constraintWidget, int i10) {
        super(constraintWidget);
        this.f15559k = new ArrayList<>();
        this.f15610f = i10;
        q();
    }

    private void q() {
        ConstraintWidget constraintWidget;
        ConstraintWidget constraintWidget2 = this.f15606b;
        ConstraintWidget I = constraintWidget2.I(this.f15610f);
        while (true) {
            ConstraintWidget constraintWidget3 = I;
            constraintWidget = constraintWidget2;
            constraintWidget2 = constraintWidget3;
            if (constraintWidget2 == null) {
                break;
            } else {
                I = constraintWidget2.I(this.f15610f);
            }
        }
        this.f15606b = constraintWidget;
        this.f15559k.add(constraintWidget.K(this.f15610f));
        ConstraintWidget G = constraintWidget.G(this.f15610f);
        while (G != null) {
            this.f15559k.add(G.K(this.f15610f));
            G = G.G(this.f15610f);
        }
        Iterator<WidgetRun> it = this.f15559k.iterator();
        while (it.hasNext()) {
            WidgetRun next = it.next();
            int i10 = this.f15610f;
            if (i10 == 0) {
                next.f15606b.f14754c = this;
            } else if (i10 == 1) {
                next.f15606b.f14756d = this;
            }
        }
        if ((this.f15610f == 0 && ((ConstraintWidgetContainer) this.f15606b.H()).c1()) && this.f15559k.size() > 1) {
            ArrayList<WidgetRun> arrayList = this.f15559k;
            this.f15606b = arrayList.get(arrayList.size() - 1).f15606b;
        }
        this.f15560l = this.f15610f == 0 ? this.f15606b.y() : this.f15606b.M();
    }

    private ConstraintWidget r() {
        for (int i10 = 0; i10 < this.f15559k.size(); i10++) {
            WidgetRun widgetRun = this.f15559k.get(i10);
            if (widgetRun.f15606b.P() != 8) {
                return widgetRun.f15606b;
            }
        }
        return null;
    }

    private ConstraintWidget s() {
        for (int size = this.f15559k.size() - 1; size >= 0; size--) {
            WidgetRun widgetRun = this.f15559k.get(size);
            if (widgetRun.f15606b.P() != 8) {
                return widgetRun.f15606b;
            }
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:111:0x01a5, code lost:
    
        if (r1 != r7) goto L120;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x01d0, code lost:
    
        r9.f15609e.d(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x01cd, code lost:
    
        r13 = r13 + 1;
        r7 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x01cb, code lost:
    
        if (r1 != r7) goto L120;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x0418, code lost:
    
        r7 = r7 - r10;
     */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00d9  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00eb  */
    @Override // n.WidgetRun, n.Dependency
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void a(Dependency dependency) {
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        float f10;
        boolean z10;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        boolean z11;
        int i20;
        int i21;
        float f11;
        int i22;
        int max;
        int i23;
        int i24;
        if (this.f15612h.f15579j && this.f15613i.f15579j) {
            ConstraintWidget H = this.f15606b.H();
            boolean c12 = (H == null || !(H instanceof ConstraintWidgetContainer)) ? false : ((ConstraintWidgetContainer) H).c1();
            int i25 = this.f15613i.f15576g - this.f15612h.f15576g;
            int size = this.f15559k.size();
            int i26 = 0;
            while (true) {
                i10 = -1;
                i11 = 8;
                if (i26 >= size) {
                    i26 = -1;
                    break;
                } else if (this.f15559k.get(i26).f15606b.P() != 8) {
                    break;
                } else {
                    i26++;
                }
            }
            int i27 = size - 1;
            int i28 = i27;
            while (true) {
                if (i28 < 0) {
                    break;
                }
                if (this.f15559k.get(i28).f15606b.P() != 8) {
                    i10 = i28;
                    break;
                }
                i28--;
            }
            int i29 = 0;
            while (i29 < 2) {
                int i30 = 0;
                i13 = 0;
                i14 = 0;
                int i31 = 0;
                f10 = 0.0f;
                while (i30 < size) {
                    WidgetRun widgetRun = this.f15559k.get(i30);
                    if (widgetRun.f15606b.P() != i11) {
                        i31++;
                        if (i30 > 0 && i30 >= i26) {
                            i13 += widgetRun.f15612h.f15575f;
                        }
                        DimensionDependency dimensionDependency = widgetRun.f15609e;
                        int i32 = dimensionDependency.f15576g;
                        boolean z12 = widgetRun.f15608d != ConstraintWidget.b.MATCH_CONSTRAINT;
                        if (z12) {
                            int i33 = this.f15610f;
                            if (i33 == 0 && !widgetRun.f15606b.f14758e.f15609e.f15579j) {
                                return;
                            }
                            if (i33 == 1 && !widgetRun.f15606b.f14760f.f15609e.f15579j) {
                                return;
                            } else {
                                i23 = i32;
                            }
                        } else {
                            i23 = i32;
                            if (widgetRun.f15605a == 1 && i29 == 0) {
                                i24 = dimensionDependency.f15591m;
                                i14++;
                            } else if (dimensionDependency.f15579j) {
                                i24 = i23;
                            }
                            z12 = true;
                            if (z12) {
                                i14++;
                                float f12 = widgetRun.f15606b.A0[this.f15610f];
                                if (f12 >= 0.0f) {
                                    f10 += f12;
                                }
                            } else {
                                i13 += i24;
                            }
                            if (i30 < i27 && i30 < i10) {
                                i13 += -widgetRun.f15613i.f15575f;
                            }
                        }
                        i24 = i23;
                        if (z12) {
                        }
                        if (i30 < i27) {
                            i13 += -widgetRun.f15613i.f15575f;
                        }
                    }
                    i30++;
                    i11 = 8;
                }
                if (i13 < i25 || i14 == 0) {
                    i12 = i31;
                    break;
                } else {
                    i29++;
                    i11 = 8;
                }
            }
            i12 = 0;
            i13 = 0;
            i14 = 0;
            f10 = 0.0f;
            int i34 = this.f15612h.f15576g;
            if (c12) {
                i34 = this.f15613i.f15576g;
            }
            if (i13 > i25) {
                i34 = c12 ? i34 + ((int) (((i13 - i25) / 2.0f) + 0.5f)) : i34 - ((int) (((i13 - i25) / 2.0f) + 0.5f));
            }
            if (i14 > 0) {
                float f13 = i25 - i13;
                int i35 = (int) ((f13 / i14) + 0.5f);
                int i36 = 0;
                int i37 = 0;
                while (i36 < size) {
                    WidgetRun widgetRun2 = this.f15559k.get(i36);
                    int i38 = i35;
                    int i39 = i13;
                    if (widgetRun2.f15606b.P() != 8 && widgetRun2.f15608d == ConstraintWidget.b.MATCH_CONSTRAINT) {
                        DimensionDependency dimensionDependency2 = widgetRun2.f15609e;
                        if (!dimensionDependency2.f15579j) {
                            if (f10 > 0.0f) {
                                i21 = i34;
                                i22 = (int) (((widgetRun2.f15606b.A0[this.f15610f] * f13) / f10) + 0.5f);
                            } else {
                                i21 = i34;
                                i22 = i38;
                            }
                            if (this.f15610f == 0) {
                                ConstraintWidget constraintWidget = widgetRun2.f15606b;
                                f11 = f13;
                                int i40 = constraintWidget.f14780p;
                                z11 = c12;
                                i20 = i12;
                                max = Math.max(constraintWidget.f14778o, widgetRun2.f15605a == 1 ? Math.min(i22, dimensionDependency2.f15591m) : i22);
                                if (i40 > 0) {
                                    max = Math.min(i40, max);
                                }
                            } else {
                                z11 = c12;
                                i20 = i12;
                                f11 = f13;
                                ConstraintWidget constraintWidget2 = widgetRun2.f15606b;
                                int i41 = constraintWidget2.f14786s;
                                max = Math.max(constraintWidget2.f14784r, widgetRun2.f15605a == 1 ? Math.min(i22, dimensionDependency2.f15591m) : i22);
                                if (i41 > 0) {
                                    max = Math.min(i41, max);
                                }
                            }
                        }
                    }
                    z11 = c12;
                    i20 = i12;
                    i21 = i34;
                    f11 = f13;
                    i36++;
                    i35 = i38;
                    i13 = i39;
                    i34 = i21;
                    f13 = f11;
                    c12 = z11;
                    i12 = i20;
                }
                z10 = c12;
                i15 = i12;
                i16 = i34;
                int i42 = i13;
                if (i37 > 0) {
                    i14 -= i37;
                    int i43 = 0;
                    for (int i44 = 0; i44 < size; i44++) {
                        WidgetRun widgetRun3 = this.f15559k.get(i44);
                        if (widgetRun3.f15606b.P() != 8) {
                            if (i44 > 0 && i44 >= i26) {
                                i43 += widgetRun3.f15612h.f15575f;
                            }
                            i43 += widgetRun3.f15609e.f15576g;
                            if (i44 < i27 && i44 < i10) {
                                i43 += -widgetRun3.f15613i.f15575f;
                            }
                        }
                    }
                    i13 = i43;
                } else {
                    i13 = i42;
                }
                i18 = 2;
                if (this.f15560l == 2 && i37 == 0) {
                    i17 = 0;
                    this.f15560l = 0;
                } else {
                    i17 = 0;
                }
            } else {
                z10 = c12;
                i15 = i12;
                i16 = i34;
                i17 = 0;
                i18 = 2;
            }
            if (i13 > i25) {
                this.f15560l = i18;
            }
            if (i15 > 0 && i14 == 0 && i26 == i10) {
                this.f15560l = i18;
            }
            int i45 = this.f15560l;
            if (i45 == 1) {
                int i46 = i15;
                if (i46 > 1) {
                    i19 = (i25 - i13) / (i46 - 1);
                } else {
                    i19 = i46 == 1 ? (i25 - i13) / 2 : i17;
                }
                if (i14 > 0) {
                    i19 = i17;
                }
                int i47 = i16;
                for (int i48 = i17; i48 < size; i48++) {
                    WidgetRun widgetRun4 = this.f15559k.get(z10 ? size - (i48 + 1) : i48);
                    if (widgetRun4.f15606b.P() == 8) {
                        widgetRun4.f15612h.d(i47);
                        widgetRun4.f15613i.d(i47);
                    } else {
                        if (i48 > 0) {
                            i47 = z10 ? i47 - i19 : i47 + i19;
                        }
                        if (i48 > 0 && i48 >= i26) {
                            if (z10) {
                                i47 -= widgetRun4.f15612h.f15575f;
                            } else {
                                i47 += widgetRun4.f15612h.f15575f;
                            }
                        }
                        if (z10) {
                            widgetRun4.f15613i.d(i47);
                        } else {
                            widgetRun4.f15612h.d(i47);
                        }
                        DimensionDependency dimensionDependency3 = widgetRun4.f15609e;
                        int i49 = dimensionDependency3.f15576g;
                        if (widgetRun4.f15608d == ConstraintWidget.b.MATCH_CONSTRAINT && widgetRun4.f15605a == 1) {
                            i49 = dimensionDependency3.f15591m;
                        }
                        i47 = z10 ? i47 - i49 : i47 + i49;
                        if (z10) {
                            widgetRun4.f15612h.d(i47);
                        } else {
                            widgetRun4.f15613i.d(i47);
                        }
                        widgetRun4.f15611g = true;
                        if (i48 < i27 && i48 < i10) {
                            if (z10) {
                                i47 -= -widgetRun4.f15613i.f15575f;
                            } else {
                                i47 += -widgetRun4.f15613i.f15575f;
                            }
                        }
                    }
                }
                return;
            }
            int i50 = i15;
            if (i45 == 0) {
                int i51 = (i25 - i13) / (i50 + 1);
                if (i14 > 0) {
                    i51 = i17;
                }
                int i52 = i16;
                for (int i53 = i17; i53 < size; i53++) {
                    WidgetRun widgetRun5 = this.f15559k.get(z10 ? size - (i53 + 1) : i53);
                    if (widgetRun5.f15606b.P() == 8) {
                        widgetRun5.f15612h.d(i52);
                        widgetRun5.f15613i.d(i52);
                    } else {
                        int i54 = z10 ? i52 - i51 : i52 + i51;
                        if (i53 > 0 && i53 >= i26) {
                            if (z10) {
                                i54 -= widgetRun5.f15612h.f15575f;
                            } else {
                                i54 += widgetRun5.f15612h.f15575f;
                            }
                        }
                        if (z10) {
                            widgetRun5.f15613i.d(i54);
                        } else {
                            widgetRun5.f15612h.d(i54);
                        }
                        DimensionDependency dimensionDependency4 = widgetRun5.f15609e;
                        int i55 = dimensionDependency4.f15576g;
                        if (widgetRun5.f15608d == ConstraintWidget.b.MATCH_CONSTRAINT && widgetRun5.f15605a == 1) {
                            i55 = Math.min(i55, dimensionDependency4.f15591m);
                        }
                        i52 = z10 ? i54 - i55 : i54 + i55;
                        if (z10) {
                            widgetRun5.f15612h.d(i52);
                        } else {
                            widgetRun5.f15613i.d(i52);
                        }
                        if (i53 < i27 && i53 < i10) {
                            if (z10) {
                                i52 -= -widgetRun5.f15613i.f15575f;
                            } else {
                                i52 += -widgetRun5.f15613i.f15575f;
                            }
                        }
                    }
                }
                return;
            }
            if (i45 == 2) {
                float x10 = this.f15610f == 0 ? this.f15606b.x() : this.f15606b.L();
                if (z10) {
                    x10 = 1.0f - x10;
                }
                int i56 = (int) (((i25 - i13) * x10) + 0.5f);
                if (i56 < 0 || i14 > 0) {
                    i56 = i17;
                }
                int i57 = z10 ? i16 - i56 : i16 + i56;
                for (int i58 = i17; i58 < size; i58++) {
                    WidgetRun widgetRun6 = this.f15559k.get(z10 ? size - (i58 + 1) : i58);
                    if (widgetRun6.f15606b.P() == 8) {
                        widgetRun6.f15612h.d(i57);
                        widgetRun6.f15613i.d(i57);
                    } else {
                        if (i58 > 0 && i58 >= i26) {
                            if (z10) {
                                i57 -= widgetRun6.f15612h.f15575f;
                            } else {
                                i57 += widgetRun6.f15612h.f15575f;
                            }
                        }
                        if (z10) {
                            widgetRun6.f15613i.d(i57);
                        } else {
                            widgetRun6.f15612h.d(i57);
                        }
                        DimensionDependency dimensionDependency5 = widgetRun6.f15609e;
                        int i59 = dimensionDependency5.f15576g;
                        if (widgetRun6.f15608d == ConstraintWidget.b.MATCH_CONSTRAINT && widgetRun6.f15605a == 1) {
                            i59 = dimensionDependency5.f15591m;
                        }
                        i57 += i59;
                        if (z10) {
                            widgetRun6.f15612h.d(i57);
                        } else {
                            widgetRun6.f15613i.d(i57);
                        }
                        if (i58 < i27 && i58 < i10) {
                            if (z10) {
                                i57 -= -widgetRun6.f15613i.f15575f;
                            } else {
                                i57 += -widgetRun6.f15613i.f15575f;
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // n.WidgetRun
    public void d() {
        Iterator<WidgetRun> it = this.f15559k.iterator();
        while (it.hasNext()) {
            it.next().d();
        }
        int size = this.f15559k.size();
        if (size < 1) {
            return;
        }
        ConstraintWidget constraintWidget = this.f15559k.get(0).f15606b;
        ConstraintWidget constraintWidget2 = this.f15559k.get(size - 1).f15606b;
        if (this.f15610f == 0) {
            ConstraintAnchor constraintAnchor = constraintWidget.D;
            ConstraintAnchor constraintAnchor2 = constraintWidget2.F;
            DependencyNode i10 = i(constraintAnchor, 0);
            int c10 = constraintAnchor.c();
            ConstraintWidget r10 = r();
            if (r10 != null) {
                c10 = r10.D.c();
            }
            if (i10 != null) {
                b(this.f15612h, i10, c10);
            }
            DependencyNode i11 = i(constraintAnchor2, 0);
            int c11 = constraintAnchor2.c();
            ConstraintWidget s7 = s();
            if (s7 != null) {
                c11 = s7.F.c();
            }
            if (i11 != null) {
                b(this.f15613i, i11, -c11);
            }
        } else {
            ConstraintAnchor constraintAnchor3 = constraintWidget.E;
            ConstraintAnchor constraintAnchor4 = constraintWidget2.G;
            DependencyNode i12 = i(constraintAnchor3, 1);
            int c12 = constraintAnchor3.c();
            ConstraintWidget r11 = r();
            if (r11 != null) {
                c12 = r11.E.c();
            }
            if (i12 != null) {
                b(this.f15612h, i12, c12);
            }
            DependencyNode i13 = i(constraintAnchor4, 1);
            int c13 = constraintAnchor4.c();
            ConstraintWidget s10 = s();
            if (s10 != null) {
                c13 = s10.G.c();
            }
            if (i13 != null) {
                b(this.f15613i, i13, -c13);
            }
        }
        this.f15612h.f15570a = this;
        this.f15613i.f15570a = this;
    }

    @Override // n.WidgetRun
    public void e() {
        for (int i10 = 0; i10 < this.f15559k.size(); i10++) {
            this.f15559k.get(i10).e();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // n.WidgetRun
    public void f() {
        this.f15607c = null;
        Iterator<WidgetRun> it = this.f15559k.iterator();
        while (it.hasNext()) {
            it.next().f();
        }
    }

    @Override // n.WidgetRun
    public long j() {
        int size = this.f15559k.size();
        long j10 = 0;
        for (int i10 = 0; i10 < size; i10++) {
            j10 = j10 + r4.f15612h.f15575f + this.f15559k.get(i10).j() + r4.f15613i.f15575f;
        }
        return j10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // n.WidgetRun
    public boolean m() {
        int size = this.f15559k.size();
        for (int i10 = 0; i10 < size; i10++) {
            if (!this.f15559k.get(i10).m()) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("ChainRun ");
        sb2.append(this.f15610f == 0 ? "horizontal : " : "vertical : ");
        String sb3 = sb2.toString();
        Iterator<WidgetRun> it = this.f15559k.iterator();
        while (it.hasNext()) {
            String str = sb3 + "<";
            sb3 = (str + it.next()) + "> ";
        }
        return sb3;
    }
}
