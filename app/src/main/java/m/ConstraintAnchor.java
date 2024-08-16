package m;

import java.util.HashSet;
import java.util.Iterator;
import l.SolverVariable;

/* compiled from: ConstraintAnchor.java */
/* renamed from: m.d, reason: use source file name */
/* loaded from: classes.dex */
public class ConstraintAnchor {

    /* renamed from: b, reason: collision with root package name */
    public final ConstraintWidget f14733b;

    /* renamed from: c, reason: collision with root package name */
    public final b f14734c;

    /* renamed from: d, reason: collision with root package name */
    public ConstraintAnchor f14735d;

    /* renamed from: g, reason: collision with root package name */
    SolverVariable f14738g;

    /* renamed from: a, reason: collision with root package name */
    private HashSet<ConstraintAnchor> f14732a = null;

    /* renamed from: e, reason: collision with root package name */
    public int f14736e = 0;

    /* renamed from: f, reason: collision with root package name */
    int f14737f = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ConstraintAnchor.java */
    /* renamed from: m.d$a */
    /* loaded from: classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f14739a;

        static {
            int[] iArr = new int[b.values().length];
            f14739a = iArr;
            try {
                iArr[b.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f14739a[b.LEFT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f14739a[b.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f14739a[b.TOP.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f14739a[b.BOTTOM.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f14739a[b.BASELINE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f14739a[b.CENTER_X.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f14739a[b.CENTER_Y.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f14739a[b.NONE.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    /* compiled from: ConstraintAnchor.java */
    /* renamed from: m.d$b */
    /* loaded from: classes.dex */
    public enum b {
        NONE,
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        BASELINE,
        CENTER,
        CENTER_X,
        CENTER_Y
    }

    public ConstraintAnchor(ConstraintWidget constraintWidget, b bVar) {
        this.f14733b = constraintWidget;
        this.f14734c = bVar;
    }

    public boolean a(ConstraintAnchor constraintAnchor, int i10) {
        return b(constraintAnchor, i10, -1, false);
    }

    public boolean b(ConstraintAnchor constraintAnchor, int i10, int i11, boolean z10) {
        if (constraintAnchor == null) {
            l();
            return true;
        }
        if (!z10 && !k(constraintAnchor)) {
            return false;
        }
        this.f14735d = constraintAnchor;
        if (constraintAnchor.f14732a == null) {
            constraintAnchor.f14732a = new HashSet<>();
        }
        this.f14735d.f14732a.add(this);
        if (i10 > 0) {
            this.f14736e = i10;
        } else {
            this.f14736e = 0;
        }
        this.f14737f = i11;
        return true;
    }

    public int c() {
        ConstraintAnchor constraintAnchor;
        if (this.f14733b.P() == 8) {
            return 0;
        }
        if (this.f14737f > -1 && (constraintAnchor = this.f14735d) != null && constraintAnchor.f14733b.P() == 8) {
            return this.f14737f;
        }
        return this.f14736e;
    }

    public final ConstraintAnchor d() {
        switch (a.f14739a[this.f14734c.ordinal()]) {
            case 1:
            case 6:
            case 7:
            case 8:
            case 9:
                return null;
            case 2:
                return this.f14733b.F;
            case 3:
                return this.f14733b.D;
            case 4:
                return this.f14733b.G;
            case 5:
                return this.f14733b.E;
            default:
                throw new AssertionError(this.f14734c.name());
        }
    }

    public ConstraintWidget e() {
        return this.f14733b;
    }

    public SolverVariable f() {
        return this.f14738g;
    }

    public ConstraintAnchor g() {
        return this.f14735d;
    }

    public b h() {
        return this.f14734c;
    }

    public boolean i() {
        HashSet<ConstraintAnchor> hashSet = this.f14732a;
        if (hashSet == null) {
            return false;
        }
        Iterator<ConstraintAnchor> it = hashSet.iterator();
        while (it.hasNext()) {
            if (it.next().d().j()) {
                return true;
            }
        }
        return false;
    }

    public boolean j() {
        return this.f14735d != null;
    }

    public boolean k(ConstraintAnchor constraintAnchor) {
        if (constraintAnchor == null) {
            return false;
        }
        b h10 = constraintAnchor.h();
        b bVar = this.f14734c;
        if (h10 == bVar) {
            return bVar != b.BASELINE || (constraintAnchor.e().T() && e().T());
        }
        switch (a.f14739a[bVar.ordinal()]) {
            case 1:
                return (h10 == b.BASELINE || h10 == b.CENTER_X || h10 == b.CENTER_Y) ? false : true;
            case 2:
            case 3:
                boolean z10 = h10 == b.LEFT || h10 == b.RIGHT;
                if (constraintAnchor.e() instanceof h) {
                    return z10 || h10 == b.CENTER_X;
                }
                return z10;
            case 4:
            case 5:
                boolean z11 = h10 == b.TOP || h10 == b.BOTTOM;
                if (constraintAnchor.e() instanceof h) {
                    return z11 || h10 == b.CENTER_Y;
                }
                return z11;
            case 6:
            case 7:
            case 8:
            case 9:
                return false;
            default:
                throw new AssertionError(this.f14734c.name());
        }
    }

    public void l() {
        HashSet<ConstraintAnchor> hashSet;
        ConstraintAnchor constraintAnchor = this.f14735d;
        if (constraintAnchor != null && (hashSet = constraintAnchor.f14732a) != null) {
            hashSet.remove(this);
        }
        this.f14735d = null;
        this.f14736e = 0;
        this.f14737f = -1;
    }

    public void m(l.c cVar) {
        SolverVariable solverVariable = this.f14738g;
        if (solverVariable == null) {
            this.f14738g = new SolverVariable(SolverVariable.a.UNRESTRICTED, null);
        } else {
            solverVariable.d();
        }
    }

    public void n(int i10) {
        if (j()) {
            this.f14737f = i10;
        }
    }

    public String toString() {
        return this.f14733b.s() + ":" + this.f14734c.toString();
    }
}
