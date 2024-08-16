package m;

import java.util.ArrayList;
import m.ConstraintWidget;

/* compiled from: ChainHead.java */
/* renamed from: m.c, reason: use source file name */
/* loaded from: classes.dex */
public class ChainHead {

    /* renamed from: a, reason: collision with root package name */
    protected ConstraintWidget f14710a;

    /* renamed from: b, reason: collision with root package name */
    protected ConstraintWidget f14711b;

    /* renamed from: c, reason: collision with root package name */
    protected ConstraintWidget f14712c;

    /* renamed from: d, reason: collision with root package name */
    protected ConstraintWidget f14713d;

    /* renamed from: e, reason: collision with root package name */
    protected ConstraintWidget f14714e;

    /* renamed from: f, reason: collision with root package name */
    protected ConstraintWidget f14715f;

    /* renamed from: g, reason: collision with root package name */
    protected ConstraintWidget f14716g;

    /* renamed from: h, reason: collision with root package name */
    protected ArrayList<ConstraintWidget> f14717h;

    /* renamed from: i, reason: collision with root package name */
    protected int f14718i;

    /* renamed from: j, reason: collision with root package name */
    protected int f14719j;

    /* renamed from: k, reason: collision with root package name */
    protected float f14720k = 0.0f;

    /* renamed from: l, reason: collision with root package name */
    int f14721l;

    /* renamed from: m, reason: collision with root package name */
    int f14722m;

    /* renamed from: n, reason: collision with root package name */
    int f14723n;

    /* renamed from: o, reason: collision with root package name */
    boolean f14724o;

    /* renamed from: p, reason: collision with root package name */
    private int f14725p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f14726q;

    /* renamed from: r, reason: collision with root package name */
    protected boolean f14727r;

    /* renamed from: s, reason: collision with root package name */
    protected boolean f14728s;

    /* renamed from: t, reason: collision with root package name */
    protected boolean f14729t;

    /* renamed from: u, reason: collision with root package name */
    protected boolean f14730u;

    /* renamed from: v, reason: collision with root package name */
    private boolean f14731v;

    public ChainHead(ConstraintWidget constraintWidget, int i10, boolean z10) {
        this.f14710a = constraintWidget;
        this.f14725p = i10;
        this.f14726q = z10;
    }

    private void b() {
        int i10 = this.f14725p * 2;
        ConstraintWidget constraintWidget = this.f14710a;
        this.f14724o = true;
        ConstraintWidget constraintWidget2 = constraintWidget;
        boolean z10 = false;
        while (!z10) {
            this.f14718i++;
            ConstraintWidget[] constraintWidgetArr = constraintWidget.C0;
            int i11 = this.f14725p;
            ConstraintWidget constraintWidget3 = null;
            constraintWidgetArr[i11] = null;
            constraintWidget.B0[i11] = null;
            if (constraintWidget.P() != 8) {
                this.f14721l++;
                ConstraintWidget.b t7 = constraintWidget.t(this.f14725p);
                ConstraintWidget.b bVar = ConstraintWidget.b.MATCH_CONSTRAINT;
                if (t7 != bVar) {
                    this.f14722m += constraintWidget.B(this.f14725p);
                }
                int c10 = this.f14722m + constraintWidget.L[i10].c();
                this.f14722m = c10;
                int i12 = i10 + 1;
                this.f14722m = c10 + constraintWidget.L[i12].c();
                int c11 = this.f14723n + constraintWidget.L[i10].c();
                this.f14723n = c11;
                this.f14723n = c11 + constraintWidget.L[i12].c();
                if (this.f14711b == null) {
                    this.f14711b = constraintWidget;
                }
                this.f14713d = constraintWidget;
                ConstraintWidget.b[] bVarArr = constraintWidget.O;
                int i13 = this.f14725p;
                if (bVarArr[i13] == bVar) {
                    int[] iArr = constraintWidget.f14776n;
                    if (iArr[i13] == 0 || iArr[i13] == 3 || iArr[i13] == 2) {
                        this.f14719j++;
                        float[] fArr = constraintWidget.A0;
                        float f10 = fArr[i13];
                        if (f10 > 0.0f) {
                            this.f14720k += fArr[i13];
                        }
                        if (c(constraintWidget, i13)) {
                            if (f10 < 0.0f) {
                                this.f14727r = true;
                            } else {
                                this.f14728s = true;
                            }
                            if (this.f14717h == null) {
                                this.f14717h = new ArrayList<>();
                            }
                            this.f14717h.add(constraintWidget);
                        }
                        if (this.f14715f == null) {
                            this.f14715f = constraintWidget;
                        }
                        ConstraintWidget constraintWidget4 = this.f14716g;
                        if (constraintWidget4 != null) {
                            constraintWidget4.B0[this.f14725p] = constraintWidget;
                        }
                        this.f14716g = constraintWidget;
                    }
                    if (this.f14725p == 0) {
                        if (constraintWidget.f14772l != 0) {
                            this.f14724o = false;
                        } else if (constraintWidget.f14778o != 0 || constraintWidget.f14780p != 0) {
                            this.f14724o = false;
                        }
                    } else if (constraintWidget.f14774m != 0) {
                        this.f14724o = false;
                    } else if (constraintWidget.f14784r != 0 || constraintWidget.f14786s != 0) {
                        this.f14724o = false;
                    }
                    if (constraintWidget.S != 0.0f) {
                        this.f14724o = false;
                        this.f14730u = true;
                    }
                }
            }
            if (constraintWidget2 != constraintWidget) {
                constraintWidget2.C0[this.f14725p] = constraintWidget;
            }
            ConstraintAnchor constraintAnchor = constraintWidget.L[i10 + 1].f14735d;
            if (constraintAnchor != null) {
                ConstraintWidget constraintWidget5 = constraintAnchor.f14733b;
                ConstraintAnchor[] constraintAnchorArr = constraintWidget5.L;
                if (constraintAnchorArr[i10].f14735d != null && constraintAnchorArr[i10].f14735d.f14733b == constraintWidget) {
                    constraintWidget3 = constraintWidget5;
                }
            }
            if (constraintWidget3 == null) {
                constraintWidget3 = constraintWidget;
                z10 = true;
            }
            constraintWidget2 = constraintWidget;
            constraintWidget = constraintWidget3;
        }
        ConstraintWidget constraintWidget6 = this.f14711b;
        if (constraintWidget6 != null) {
            this.f14722m -= constraintWidget6.L[i10].c();
        }
        ConstraintWidget constraintWidget7 = this.f14713d;
        if (constraintWidget7 != null) {
            this.f14722m -= constraintWidget7.L[i10 + 1].c();
        }
        this.f14712c = constraintWidget;
        if (this.f14725p == 0 && this.f14726q) {
            this.f14714e = constraintWidget;
        } else {
            this.f14714e = this.f14710a;
        }
        this.f14729t = this.f14728s && this.f14727r;
    }

    private static boolean c(ConstraintWidget constraintWidget, int i10) {
        if (constraintWidget.P() != 8 && constraintWidget.O[i10] == ConstraintWidget.b.MATCH_CONSTRAINT) {
            int[] iArr = constraintWidget.f14776n;
            if (iArr[i10] == 0 || iArr[i10] == 3) {
                return true;
            }
        }
        return false;
    }

    public void a() {
        if (!this.f14731v) {
            b();
        }
        this.f14731v = true;
    }
}
