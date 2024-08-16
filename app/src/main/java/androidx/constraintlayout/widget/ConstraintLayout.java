package androidx.constraintlayout.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;
import m.ConstraintAnchor;
import m.ConstraintWidget;
import m.ConstraintWidgetContainer;
import m.h;
import m.l;
import n.BasicMeasure;

/* loaded from: classes.dex */
public class ConstraintLayout extends ViewGroup {
    private int A;

    /* renamed from: e, reason: collision with root package name */
    SparseArray<View> f1824e;

    /* renamed from: f, reason: collision with root package name */
    private ArrayList<ConstraintHelper> f1825f;

    /* renamed from: g, reason: collision with root package name */
    protected ConstraintWidgetContainer f1826g;

    /* renamed from: h, reason: collision with root package name */
    private int f1827h;

    /* renamed from: i, reason: collision with root package name */
    private int f1828i;

    /* renamed from: j, reason: collision with root package name */
    private int f1829j;

    /* renamed from: k, reason: collision with root package name */
    private int f1830k;

    /* renamed from: l, reason: collision with root package name */
    protected boolean f1831l;

    /* renamed from: m, reason: collision with root package name */
    private int f1832m;

    /* renamed from: n, reason: collision with root package name */
    private ConstraintSet f1833n;

    /* renamed from: o, reason: collision with root package name */
    protected ConstraintLayoutStates f1834o;

    /* renamed from: p, reason: collision with root package name */
    private int f1835p;

    /* renamed from: q, reason: collision with root package name */
    private HashMap<String, Integer> f1836q;

    /* renamed from: r, reason: collision with root package name */
    private int f1837r;

    /* renamed from: s, reason: collision with root package name */
    private int f1838s;

    /* renamed from: t, reason: collision with root package name */
    int f1839t;

    /* renamed from: u, reason: collision with root package name */
    int f1840u;

    /* renamed from: v, reason: collision with root package name */
    int f1841v;

    /* renamed from: w, reason: collision with root package name */
    int f1842w;

    /* renamed from: x, reason: collision with root package name */
    private SparseArray<ConstraintWidget> f1843x;

    /* renamed from: y, reason: collision with root package name */
    b f1844y;

    /* renamed from: z, reason: collision with root package name */
    private int f1845z;

    /* loaded from: classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f1888a;

        static {
            int[] iArr = new int[ConstraintWidget.b.values().length];
            f1888a = iArr;
            try {
                iArr[ConstraintWidget.b.FIXED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f1888a[ConstraintWidget.b.WRAP_CONTENT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f1888a[ConstraintWidget.b.MATCH_PARENT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f1888a[ConstraintWidget.b.MATCH_CONSTRAINT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements BasicMeasure.b {

        /* renamed from: a, reason: collision with root package name */
        ConstraintLayout f1889a;

        /* renamed from: b, reason: collision with root package name */
        int f1890b;

        /* renamed from: c, reason: collision with root package name */
        int f1891c;

        /* renamed from: d, reason: collision with root package name */
        int f1892d;

        /* renamed from: e, reason: collision with root package name */
        int f1893e;

        /* renamed from: f, reason: collision with root package name */
        int f1894f;

        /* renamed from: g, reason: collision with root package name */
        int f1895g;

        public b(ConstraintLayout constraintLayout) {
            this.f1889a = constraintLayout;
        }

        @Override // n.BasicMeasure.b
        public final void a() {
            int childCount = this.f1889a.getChildCount();
            for (int i10 = 0; i10 < childCount; i10++) {
                View childAt = this.f1889a.getChildAt(i10);
                if (childAt instanceof Placeholder) {
                    ((Placeholder) childAt).b(this.f1889a);
                }
            }
            int size = this.f1889a.f1825f.size();
            if (size > 0) {
                for (int i11 = 0; i11 < size; i11++) {
                    ((ConstraintHelper) this.f1889a.f1825f.get(i11)).q(this.f1889a);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:102:0x01f2 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:105:0x0208 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:110:0x0212  */
        /* JADX WARN: Removed duplicated region for block: B:112:0x0218  */
        /* JADX WARN: Removed duplicated region for block: B:115:0x01fd A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:117:0x01e5  */
        /* JADX WARN: Removed duplicated region for block: B:118:0x01d3  */
        /* JADX WARN: Removed duplicated region for block: B:119:0x01c2  */
        /* JADX WARN: Removed duplicated region for block: B:120:0x01af  */
        /* JADX WARN: Removed duplicated region for block: B:126:0x0136  */
        /* JADX WARN: Removed duplicated region for block: B:127:0x0131  */
        /* JADX WARN: Removed duplicated region for block: B:151:0x0120  */
        /* JADX WARN: Removed duplicated region for block: B:20:0x00ba  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x012f  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x0134  */
        /* JADX WARN: Removed duplicated region for block: B:32:0x013b  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x0145  */
        /* JADX WARN: Removed duplicated region for block: B:41:0x0150  */
        /* JADX WARN: Removed duplicated region for block: B:45:0x015b  */
        /* JADX WARN: Removed duplicated region for block: B:50:0x016e A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:58:0x022f  */
        /* JADX WARN: Removed duplicated region for block: B:61:0x0237  */
        /* JADX WARN: Removed duplicated region for block: B:66:0x0246  */
        /* JADX WARN: Removed duplicated region for block: B:68:0x024b  */
        /* JADX WARN: Removed duplicated region for block: B:75:0x0248  */
        /* JADX WARN: Removed duplicated region for block: B:77:0x0231  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x0187  */
        /* JADX WARN: Removed duplicated region for block: B:85:0x01a6  */
        /* JADX WARN: Removed duplicated region for block: B:87:0x01b9  */
        /* JADX WARN: Removed duplicated region for block: B:90:0x01ce  */
        /* JADX WARN: Removed duplicated region for block: B:93:0x01d8  */
        /* JADX WARN: Removed duplicated region for block: B:96:0x01e0  */
        /* JADX WARN: Removed duplicated region for block: B:99:0x01ea  */
        @Override // n.BasicMeasure.b
        @SuppressLint({"WrongCall"})
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final void b(ConstraintWidget constraintWidget, BasicMeasure.a aVar) {
            int i10;
            boolean z10;
            int i11;
            int makeMeasureSpec;
            boolean z11;
            boolean z12;
            boolean z13;
            int measuredWidth;
            int measuredHeight;
            int baseline;
            int i12;
            int max;
            int i13;
            int max2;
            int i14;
            int measuredHeight2;
            int i15;
            boolean z14;
            int childMeasureSpec;
            int childMeasureSpec2;
            if (constraintWidget == null) {
                return;
            }
            if (constraintWidget.P() == 8 && !constraintWidget.X()) {
                aVar.f15553e = 0;
                aVar.f15554f = 0;
                aVar.f15555g = 0;
                return;
            }
            ConstraintWidget.b bVar = aVar.f15549a;
            ConstraintWidget.b bVar2 = aVar.f15550b;
            int i16 = aVar.f15551c;
            int i17 = aVar.f15552d;
            int i18 = this.f1890b + this.f1891c;
            int i19 = this.f1892d;
            View view = (View) constraintWidget.r();
            int[] iArr = a.f1888a;
            int i20 = iArr[bVar.ordinal()];
            if (i20 == 1) {
                int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(i16, 1073741824);
                constraintWidget.f14764h[2] = i16;
                i10 = makeMeasureSpec2;
            } else {
                if (i20 == 2) {
                    i10 = ViewGroup.getChildMeasureSpec(this.f1894f, i19, -2);
                    constraintWidget.f14764h[2] = -2;
                } else if (i20 == 3) {
                    i10 = ViewGroup.getChildMeasureSpec(this.f1894f, i19 + constraintWidget.A(), -1);
                    constraintWidget.f14764h[2] = -1;
                } else if (i20 != 4) {
                    i10 = 0;
                } else {
                    i10 = ViewGroup.getChildMeasureSpec(this.f1894f, i19, -2);
                    boolean z15 = constraintWidget.f14772l == 1;
                    int[] iArr2 = constraintWidget.f14764h;
                    iArr2[2] = 0;
                    if (aVar.f15558j) {
                        boolean z16 = !(!z15 || iArr2[3] == 0 || iArr2[0] == constraintWidget.Q()) || (view instanceof Placeholder);
                        if (!z15 || z16) {
                            i10 = View.MeasureSpec.makeMeasureSpec(constraintWidget.Q(), 1073741824);
                        }
                    }
                }
                z10 = true;
                i11 = iArr[bVar2.ordinal()];
                if (i11 != 1) {
                    makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i17, 1073741824);
                    constraintWidget.f14764h[3] = i17;
                } else {
                    if (i11 != 2) {
                        if (i11 == 3) {
                            childMeasureSpec2 = ViewGroup.getChildMeasureSpec(this.f1895g, i18 + constraintWidget.O(), -1);
                            constraintWidget.f14764h[3] = -1;
                        } else if (i11 == 4) {
                            childMeasureSpec = ViewGroup.getChildMeasureSpec(this.f1895g, i18, -2);
                            boolean z17 = constraintWidget.f14774m == 1;
                            int[] iArr3 = constraintWidget.f14764h;
                            iArr3[3] = 0;
                            if (aVar.f15558j) {
                                boolean z18 = !(!z17 || iArr3[2] == 0 || iArr3[1] == constraintWidget.w()) || (view instanceof Placeholder);
                                if (!z17 || z18) {
                                    childMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(constraintWidget.w(), 1073741824);
                                }
                            }
                        } else {
                            z11 = false;
                            makeMeasureSpec = 0;
                            ConstraintWidget.b bVar3 = ConstraintWidget.b.MATCH_CONSTRAINT;
                            boolean z19 = bVar != bVar3;
                            boolean z20 = bVar2 != bVar3;
                            ConstraintWidget.b bVar4 = ConstraintWidget.b.MATCH_PARENT;
                            boolean z21 = bVar2 != bVar4 || bVar2 == ConstraintWidget.b.FIXED;
                            boolean z22 = bVar != bVar4 || bVar == ConstraintWidget.b.FIXED;
                            z12 = !z19 && constraintWidget.S > 0.0f;
                            z13 = !z20 && constraintWidget.S > 0.0f;
                            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                            if (aVar.f15558j && z19 && constraintWidget.f14772l == 0 && z20 && constraintWidget.f14774m == 0) {
                                max = 0;
                                measuredHeight2 = 0;
                                i15 = -1;
                                baseline = 0;
                                i12 = 0;
                            } else {
                                if (!(view instanceof VirtualLayout) && (constraintWidget instanceof l)) {
                                    ((VirtualLayout) view).v((l) constraintWidget, i10, makeMeasureSpec);
                                } else {
                                    view.measure(i10, makeMeasureSpec);
                                }
                                measuredWidth = view.getMeasuredWidth();
                                measuredHeight = view.getMeasuredHeight();
                                baseline = view.getBaseline();
                                if (!z10) {
                                    int[] iArr4 = constraintWidget.f14764h;
                                    i12 = 0;
                                    iArr4[0] = measuredWidth;
                                    iArr4[2] = measuredHeight;
                                } else {
                                    i12 = 0;
                                    int[] iArr5 = constraintWidget.f14764h;
                                    iArr5[0] = 0;
                                    iArr5[2] = 0;
                                }
                                if (!z11) {
                                    int[] iArr6 = constraintWidget.f14764h;
                                    iArr6[1] = measuredHeight;
                                    iArr6[3] = measuredWidth;
                                } else {
                                    int[] iArr7 = constraintWidget.f14764h;
                                    iArr7[1] = i12;
                                    iArr7[3] = i12;
                                }
                                int i21 = constraintWidget.f14778o;
                                max = i21 <= 0 ? Math.max(i21, measuredWidth) : measuredWidth;
                                i13 = constraintWidget.f14780p;
                                if (i13 > 0) {
                                    max = Math.min(i13, max);
                                }
                                int i22 = constraintWidget.f14784r;
                                max2 = i22 <= 0 ? Math.max(i22, measuredHeight) : measuredHeight;
                                i14 = constraintWidget.f14786s;
                                if (i14 > 0) {
                                    max2 = Math.min(i14, max2);
                                }
                                if (!z12 && z21) {
                                    max = (int) ((max2 * constraintWidget.S) + 0.5f);
                                } else if (z13 && z22) {
                                    max2 = (int) ((max / constraintWidget.S) + 0.5f);
                                }
                                if (measuredWidth == max || measuredHeight != max2) {
                                    if (measuredWidth != max) {
                                        i10 = View.MeasureSpec.makeMeasureSpec(max, 1073741824);
                                    }
                                    if (measuredHeight != max2) {
                                        makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(max2, 1073741824);
                                    }
                                    view.measure(i10, makeMeasureSpec);
                                    max = view.getMeasuredWidth();
                                    measuredHeight2 = view.getMeasuredHeight();
                                    baseline = view.getBaseline();
                                } else {
                                    measuredHeight2 = max2;
                                }
                                i15 = -1;
                            }
                            int i23 = baseline == i15 ? 1 : i12;
                            aVar.f15557i = (max == aVar.f15551c || measuredHeight2 != aVar.f15552d) ? 1 : i12;
                            z14 = !layoutParams.Y ? 1 : i23;
                            if (z14 != 0 && baseline != -1 && constraintWidget.o() != baseline) {
                                aVar.f15557i = true;
                            }
                            aVar.f15553e = max;
                            aVar.f15554f = measuredHeight2;
                            aVar.f15556h = z14;
                            aVar.f15555g = baseline;
                        }
                        makeMeasureSpec = childMeasureSpec2;
                    } else {
                        childMeasureSpec = ViewGroup.getChildMeasureSpec(this.f1895g, i18, -2);
                        constraintWidget.f14764h[3] = -2;
                    }
                    makeMeasureSpec = childMeasureSpec;
                    z11 = true;
                    ConstraintWidget.b bVar32 = ConstraintWidget.b.MATCH_CONSTRAINT;
                    if (bVar != bVar32) {
                    }
                    if (bVar2 != bVar32) {
                    }
                    ConstraintWidget.b bVar42 = ConstraintWidget.b.MATCH_PARENT;
                    if (bVar2 != bVar42) {
                    }
                    if (bVar != bVar42) {
                    }
                    if (z19) {
                    }
                    if (z20) {
                    }
                    LayoutParams layoutParams2 = (LayoutParams) view.getLayoutParams();
                    if (aVar.f15558j) {
                    }
                    if (!(view instanceof VirtualLayout)) {
                    }
                    view.measure(i10, makeMeasureSpec);
                    measuredWidth = view.getMeasuredWidth();
                    measuredHeight = view.getMeasuredHeight();
                    baseline = view.getBaseline();
                    if (!z10) {
                    }
                    if (!z11) {
                    }
                    int i212 = constraintWidget.f14778o;
                    if (i212 <= 0) {
                    }
                    i13 = constraintWidget.f14780p;
                    if (i13 > 0) {
                    }
                    int i222 = constraintWidget.f14784r;
                    if (i222 <= 0) {
                    }
                    i14 = constraintWidget.f14786s;
                    if (i14 > 0) {
                    }
                    if (!z12) {
                    }
                    if (z13) {
                        max2 = (int) ((max / constraintWidget.S) + 0.5f);
                    }
                    if (measuredWidth == max) {
                    }
                    if (measuredWidth != max) {
                    }
                    if (measuredHeight != max2) {
                    }
                    view.measure(i10, makeMeasureSpec);
                    max = view.getMeasuredWidth();
                    measuredHeight2 = view.getMeasuredHeight();
                    baseline = view.getBaseline();
                    i15 = -1;
                    if (baseline == i15) {
                    }
                    aVar.f15557i = (max == aVar.f15551c || measuredHeight2 != aVar.f15552d) ? 1 : i12;
                    if (!layoutParams2.Y) {
                    }
                    if (z14 != 0) {
                        aVar.f15557i = true;
                    }
                    aVar.f15553e = max;
                    aVar.f15554f = measuredHeight2;
                    aVar.f15556h = z14;
                    aVar.f15555g = baseline;
                }
                z11 = false;
                ConstraintWidget.b bVar322 = ConstraintWidget.b.MATCH_CONSTRAINT;
                if (bVar != bVar322) {
                }
                if (bVar2 != bVar322) {
                }
                ConstraintWidget.b bVar422 = ConstraintWidget.b.MATCH_PARENT;
                if (bVar2 != bVar422) {
                }
                if (bVar != bVar422) {
                }
                if (z19) {
                }
                if (z20) {
                }
                LayoutParams layoutParams22 = (LayoutParams) view.getLayoutParams();
                if (aVar.f15558j) {
                }
                if (!(view instanceof VirtualLayout)) {
                }
                view.measure(i10, makeMeasureSpec);
                measuredWidth = view.getMeasuredWidth();
                measuredHeight = view.getMeasuredHeight();
                baseline = view.getBaseline();
                if (!z10) {
                }
                if (!z11) {
                }
                int i2122 = constraintWidget.f14778o;
                if (i2122 <= 0) {
                }
                i13 = constraintWidget.f14780p;
                if (i13 > 0) {
                }
                int i2222 = constraintWidget.f14784r;
                if (i2222 <= 0) {
                }
                i14 = constraintWidget.f14786s;
                if (i14 > 0) {
                }
                if (!z12) {
                }
                if (z13) {
                }
                if (measuredWidth == max) {
                }
                if (measuredWidth != max) {
                }
                if (measuredHeight != max2) {
                }
                view.measure(i10, makeMeasureSpec);
                max = view.getMeasuredWidth();
                measuredHeight2 = view.getMeasuredHeight();
                baseline = view.getBaseline();
                i15 = -1;
                if (baseline == i15) {
                }
                aVar.f15557i = (max == aVar.f15551c || measuredHeight2 != aVar.f15552d) ? 1 : i12;
                if (!layoutParams22.Y) {
                }
                if (z14 != 0) {
                }
                aVar.f15553e = max;
                aVar.f15554f = measuredHeight2;
                aVar.f15556h = z14;
                aVar.f15555g = baseline;
            }
            z10 = false;
            i11 = iArr[bVar2.ordinal()];
            if (i11 != 1) {
            }
            z11 = false;
            ConstraintWidget.b bVar3222 = ConstraintWidget.b.MATCH_CONSTRAINT;
            if (bVar != bVar3222) {
            }
            if (bVar2 != bVar3222) {
            }
            ConstraintWidget.b bVar4222 = ConstraintWidget.b.MATCH_PARENT;
            if (bVar2 != bVar4222) {
            }
            if (bVar != bVar4222) {
            }
            if (z19) {
            }
            if (z20) {
            }
            LayoutParams layoutParams222 = (LayoutParams) view.getLayoutParams();
            if (aVar.f15558j) {
            }
            if (!(view instanceof VirtualLayout)) {
            }
            view.measure(i10, makeMeasureSpec);
            measuredWidth = view.getMeasuredWidth();
            measuredHeight = view.getMeasuredHeight();
            baseline = view.getBaseline();
            if (!z10) {
            }
            if (!z11) {
            }
            int i21222 = constraintWidget.f14778o;
            if (i21222 <= 0) {
            }
            i13 = constraintWidget.f14780p;
            if (i13 > 0) {
            }
            int i22222 = constraintWidget.f14784r;
            if (i22222 <= 0) {
            }
            i14 = constraintWidget.f14786s;
            if (i14 > 0) {
            }
            if (!z12) {
            }
            if (z13) {
            }
            if (measuredWidth == max) {
            }
            if (measuredWidth != max) {
            }
            if (measuredHeight != max2) {
            }
            view.measure(i10, makeMeasureSpec);
            max = view.getMeasuredWidth();
            measuredHeight2 = view.getMeasuredHeight();
            baseline = view.getBaseline();
            i15 = -1;
            if (baseline == i15) {
            }
            aVar.f15557i = (max == aVar.f15551c || measuredHeight2 != aVar.f15552d) ? 1 : i12;
            if (!layoutParams222.Y) {
            }
            if (z14 != 0) {
            }
            aVar.f15553e = max;
            aVar.f15554f = measuredHeight2;
            aVar.f15556h = z14;
            aVar.f15555g = baseline;
        }

        public void c(int i10, int i11, int i12, int i13, int i14, int i15) {
            this.f1890b = i12;
            this.f1891c = i13;
            this.f1892d = i14;
            this.f1893e = i15;
            this.f1894f = i10;
            this.f1895g = i11;
        }
    }

    public ConstraintLayout(Context context) {
        super(context);
        this.f1824e = new SparseArray<>();
        this.f1825f = new ArrayList<>(4);
        this.f1826g = new ConstraintWidgetContainer();
        this.f1827h = 0;
        this.f1828i = 0;
        this.f1829j = Integer.MAX_VALUE;
        this.f1830k = Integer.MAX_VALUE;
        this.f1831l = true;
        this.f1832m = 263;
        this.f1833n = null;
        this.f1834o = null;
        this.f1835p = -1;
        this.f1836q = new HashMap<>();
        this.f1837r = -1;
        this.f1838s = -1;
        this.f1839t = -1;
        this.f1840u = -1;
        this.f1841v = 0;
        this.f1842w = 0;
        this.f1843x = new SparseArray<>();
        this.f1844y = new b(this);
        this.f1845z = 0;
        this.A = 0;
        t(null, 0, 0);
    }

    private boolean C() {
        int childCount = getChildCount();
        boolean z10 = false;
        int i10 = 0;
        while (true) {
            if (i10 >= childCount) {
                break;
            }
            if (getChildAt(i10).isLayoutRequested()) {
                z10 = true;
                break;
            }
            i10++;
        }
        if (z10) {
            z();
        }
        return z10;
    }

    private int getPaddingWidth() {
        int max = Math.max(0, getPaddingLeft()) + Math.max(0, getPaddingRight());
        int max2 = Math.max(0, getPaddingStart()) + Math.max(0, getPaddingEnd());
        return max2 > 0 ? max2 : max;
    }

    private final ConstraintWidget q(int i10) {
        if (i10 == 0) {
            return this.f1826g;
        }
        View view = this.f1824e.get(i10);
        if (view == null && (view = findViewById(i10)) != null && view != this && view.getParent() == this) {
            onViewAdded(view);
        }
        if (view == this) {
            return this.f1826g;
        }
        if (view == null) {
            return null;
        }
        return ((LayoutParams) view.getLayoutParams()).f1873n0;
    }

    private void t(AttributeSet attributeSet, int i10, int i11) {
        this.f1826g.d0(this);
        this.f1826g.h1(this.f1844y);
        this.f1824e.put(getId(), this);
        this.f1833n = null;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.ConstraintLayout_Layout, i10, i11);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i12 = 0; i12 < indexCount; i12++) {
                int index = obtainStyledAttributes.getIndex(i12);
                if (index == R$styleable.ConstraintLayout_Layout_android_minWidth) {
                    this.f1827h = obtainStyledAttributes.getDimensionPixelOffset(index, this.f1827h);
                } else if (index == R$styleable.ConstraintLayout_Layout_android_minHeight) {
                    this.f1828i = obtainStyledAttributes.getDimensionPixelOffset(index, this.f1828i);
                } else if (index == R$styleable.ConstraintLayout_Layout_android_maxWidth) {
                    this.f1829j = obtainStyledAttributes.getDimensionPixelOffset(index, this.f1829j);
                } else if (index == R$styleable.ConstraintLayout_Layout_android_maxHeight) {
                    this.f1830k = obtainStyledAttributes.getDimensionPixelOffset(index, this.f1830k);
                } else if (index == R$styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
                    this.f1832m = obtainStyledAttributes.getInt(index, this.f1832m);
                } else if (index == R$styleable.ConstraintLayout_Layout_layoutDescription) {
                    int resourceId = obtainStyledAttributes.getResourceId(index, 0);
                    if (resourceId != 0) {
                        try {
                            w(resourceId);
                        } catch (Resources.NotFoundException unused) {
                            this.f1834o = null;
                        }
                    }
                } else if (index == R$styleable.ConstraintLayout_Layout_constraintSet) {
                    int resourceId2 = obtainStyledAttributes.getResourceId(index, 0);
                    try {
                        ConstraintSet constraintSet = new ConstraintSet();
                        this.f1833n = constraintSet;
                        constraintSet.x(getContext(), resourceId2);
                    } catch (Resources.NotFoundException unused2) {
                        this.f1833n = null;
                    }
                    this.f1835p = resourceId2;
                }
            }
            obtainStyledAttributes.recycle();
        }
        this.f1826g.i1(this.f1832m);
    }

    private void v() {
        this.f1831l = true;
        this.f1837r = -1;
        this.f1838s = -1;
        this.f1839t = -1;
        this.f1840u = -1;
        this.f1841v = 0;
        this.f1842w = 0;
    }

    private void z() {
        boolean isInEditMode = isInEditMode();
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            ConstraintWidget s7 = s(getChildAt(i10));
            if (s7 != null) {
                s7.Z();
            }
        }
        if (isInEditMode) {
            for (int i11 = 0; i11 < childCount; i11++) {
                View childAt = getChildAt(i11);
                try {
                    String resourceName = getResources().getResourceName(childAt.getId());
                    A(0, resourceName, Integer.valueOf(childAt.getId()));
                    int indexOf = resourceName.indexOf(47);
                    if (indexOf != -1) {
                        resourceName = resourceName.substring(indexOf + 1);
                    }
                    q(childAt.getId()).e0(resourceName);
                } catch (Resources.NotFoundException unused) {
                }
            }
        }
        if (this.f1835p != -1) {
            for (int i12 = 0; i12 < childCount; i12++) {
                View childAt2 = getChildAt(i12);
                if (childAt2.getId() == this.f1835p && (childAt2 instanceof Constraints)) {
                    this.f1833n = ((Constraints) childAt2).getConstraintSet();
                }
            }
        }
        ConstraintSet constraintSet = this.f1833n;
        if (constraintSet != null) {
            constraintSet.f(this, true);
        }
        this.f1826g.O0();
        int size = this.f1825f.size();
        if (size > 0) {
            for (int i13 = 0; i13 < size; i13++) {
                this.f1825f.get(i13).s(this);
            }
        }
        for (int i14 = 0; i14 < childCount; i14++) {
            View childAt3 = getChildAt(i14);
            if (childAt3 instanceof Placeholder) {
                ((Placeholder) childAt3).c(this);
            }
        }
        this.f1843x.clear();
        this.f1843x.put(0, this.f1826g);
        this.f1843x.put(getId(), this.f1826g);
        for (int i15 = 0; i15 < childCount; i15++) {
            View childAt4 = getChildAt(i15);
            this.f1843x.put(childAt4.getId(), s(childAt4));
        }
        for (int i16 = 0; i16 < childCount; i16++) {
            View childAt5 = getChildAt(i16);
            ConstraintWidget s10 = s(childAt5);
            if (s10 != null) {
                LayoutParams layoutParams = (LayoutParams) childAt5.getLayoutParams();
                this.f1826g.b(s10);
                g(isInEditMode, childAt5, s10, layoutParams, this.f1843x);
            }
        }
    }

    public void A(int i10, Object obj, Object obj2) {
        if (i10 == 0 && (obj instanceof String) && (obj2 instanceof Integer)) {
            if (this.f1836q == null) {
                this.f1836q = new HashMap<>();
            }
            String str = (String) obj;
            int indexOf = str.indexOf("/");
            if (indexOf != -1) {
                str = str.substring(indexOf + 1);
            }
            this.f1836q.put(str, Integer.valueOf(((Integer) obj2).intValue()));
        }
    }

    protected void B(ConstraintWidgetContainer constraintWidgetContainer, int i10, int i11, int i12, int i13) {
        ConstraintWidget.b bVar;
        b bVar2 = this.f1844y;
        int i14 = bVar2.f1893e;
        int i15 = bVar2.f1892d;
        ConstraintWidget.b bVar3 = ConstraintWidget.b.FIXED;
        int childCount = getChildCount();
        if (i10 == Integer.MIN_VALUE) {
            bVar = ConstraintWidget.b.WRAP_CONTENT;
            if (childCount == 0) {
                i11 = Math.max(0, this.f1827h);
            }
        } else if (i10 == 0) {
            bVar = ConstraintWidget.b.WRAP_CONTENT;
            if (childCount == 0) {
                i11 = Math.max(0, this.f1827h);
            }
            i11 = 0;
        } else if (i10 != 1073741824) {
            bVar = bVar3;
            i11 = 0;
        } else {
            i11 = Math.min(this.f1829j - i15, i11);
            bVar = bVar3;
        }
        if (i12 == Integer.MIN_VALUE) {
            bVar3 = ConstraintWidget.b.WRAP_CONTENT;
            if (childCount == 0) {
                i13 = Math.max(0, this.f1828i);
            }
        } else if (i12 != 0) {
            if (i12 == 1073741824) {
                i13 = Math.min(this.f1830k - i14, i13);
            }
            i13 = 0;
        } else {
            bVar3 = ConstraintWidget.b.WRAP_CONTENT;
            if (childCount == 0) {
                i13 = Math.max(0, this.f1828i);
            }
            i13 = 0;
        }
        if (i11 != constraintWidgetContainer.Q() || i13 != constraintWidgetContainer.w()) {
            constraintWidgetContainer.a1();
        }
        constraintWidgetContainer.G0(0);
        constraintWidgetContainer.H0(0);
        constraintWidgetContainer.t0(this.f1829j - i15);
        constraintWidgetContainer.s0(this.f1830k - i14);
        constraintWidgetContainer.v0(0);
        constraintWidgetContainer.u0(0);
        constraintWidgetContainer.m0(bVar);
        constraintWidgetContainer.F0(i11);
        constraintWidgetContainer.B0(bVar3);
        constraintWidgetContainer.i0(i13);
        constraintWidgetContainer.v0(this.f1827h - i15);
        constraintWidgetContainer.u0(this.f1828i - i14);
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i10, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, i10, layoutParams);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        Object tag;
        int size;
        ArrayList<ConstraintHelper> arrayList = this.f1825f;
        if (arrayList != null && (size = arrayList.size()) > 0) {
            for (int i10 = 0; i10 < size; i10++) {
                this.f1825f.get(i10).r(this);
            }
        }
        super.dispatchDraw(canvas);
        if (isInEditMode()) {
            int childCount = getChildCount();
            float width = getWidth();
            float height = getHeight();
            for (int i11 = 0; i11 < childCount; i11++) {
                View childAt = getChildAt(i11);
                if (childAt.getVisibility() != 8 && (tag = childAt.getTag()) != null && (tag instanceof String)) {
                    String[] split = ((String) tag).split(",");
                    if (split.length == 4) {
                        int parseInt = Integer.parseInt(split[0]);
                        int parseInt2 = Integer.parseInt(split[1]);
                        int parseInt3 = Integer.parseInt(split[2]);
                        int i12 = (int) ((parseInt / 1080.0f) * width);
                        int i13 = (int) ((parseInt2 / 1920.0f) * height);
                        Paint paint = new Paint();
                        paint.setColor(-65536);
                        float f10 = i12;
                        float f11 = i13;
                        float f12 = i12 + ((int) ((parseInt3 / 1080.0f) * width));
                        canvas.drawLine(f10, f11, f12, f11, paint);
                        float parseInt4 = i13 + ((int) ((Integer.parseInt(split[3]) / 1920.0f) * height));
                        canvas.drawLine(f12, f11, f12, parseInt4, paint);
                        canvas.drawLine(f12, parseInt4, f10, parseInt4, paint);
                        canvas.drawLine(f10, parseInt4, f10, f11, paint);
                        paint.setColor(-16711936);
                        canvas.drawLine(f10, f11, f12, parseInt4, paint);
                        canvas.drawLine(f10, parseInt4, f12, f11, paint);
                    }
                }
            }
        }
    }

    @Override // android.view.View
    public void forceLayout() {
        v();
        super.forceLayout();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void g(boolean z10, View view, ConstraintWidget constraintWidget, LayoutParams layoutParams, SparseArray<ConstraintWidget> sparseArray) {
        float f10;
        ConstraintWidget constraintWidget2;
        ConstraintWidget constraintWidget3;
        ConstraintWidget constraintWidget4;
        ConstraintWidget constraintWidget5;
        int i10;
        layoutParams.c();
        layoutParams.f1875o0 = false;
        constraintWidget.E0(view.getVisibility());
        if (layoutParams.f1849b0) {
            constraintWidget.q0(true);
            constraintWidget.E0(8);
        }
        constraintWidget.d0(view);
        if (view instanceof ConstraintHelper) {
            ((ConstraintHelper) view).o(constraintWidget, this.f1826g.c1());
        }
        if (layoutParams.Z) {
            h hVar = (h) constraintWidget;
            int i11 = layoutParams.f1867k0;
            int i12 = layoutParams.f1869l0;
            float f11 = layoutParams.f1871m0;
            if (f11 != -1.0f) {
                hVar.R0(f11);
                return;
            } else if (i11 != -1) {
                hVar.P0(i11);
                return;
            } else {
                if (i12 != -1) {
                    hVar.Q0(i12);
                    return;
                }
                return;
            }
        }
        int i13 = layoutParams.f1853d0;
        int i14 = layoutParams.f1855e0;
        int i15 = layoutParams.f1857f0;
        int i16 = layoutParams.f1859g0;
        int i17 = layoutParams.f1861h0;
        int i18 = layoutParams.f1863i0;
        float f12 = layoutParams.f1865j0;
        int i19 = layoutParams.f1870m;
        if (i19 != -1) {
            ConstraintWidget constraintWidget6 = sparseArray.get(i19);
            if (constraintWidget6 != null) {
                constraintWidget.k(constraintWidget6, layoutParams.f1874o, layoutParams.f1872n);
            }
        } else {
            if (i13 != -1) {
                ConstraintWidget constraintWidget7 = sparseArray.get(i13);
                if (constraintWidget7 != null) {
                    ConstraintAnchor.b bVar = ConstraintAnchor.b.LEFT;
                    f10 = f12;
                    constraintWidget.U(bVar, constraintWidget7, bVar, ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin, i17);
                } else {
                    f10 = f12;
                }
            } else {
                f10 = f12;
                if (i14 != -1 && (constraintWidget2 = sparseArray.get(i14)) != null) {
                    constraintWidget.U(ConstraintAnchor.b.LEFT, constraintWidget2, ConstraintAnchor.b.RIGHT, ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin, i17);
                }
            }
            if (i15 != -1) {
                ConstraintWidget constraintWidget8 = sparseArray.get(i15);
                if (constraintWidget8 != null) {
                    constraintWidget.U(ConstraintAnchor.b.RIGHT, constraintWidget8, ConstraintAnchor.b.LEFT, ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, i18);
                }
            } else if (i16 != -1 && (constraintWidget3 = sparseArray.get(i16)) != null) {
                ConstraintAnchor.b bVar2 = ConstraintAnchor.b.RIGHT;
                constraintWidget.U(bVar2, constraintWidget3, bVar2, ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, i18);
            }
            int i20 = layoutParams.f1860h;
            if (i20 != -1) {
                ConstraintWidget constraintWidget9 = sparseArray.get(i20);
                if (constraintWidget9 != null) {
                    ConstraintAnchor.b bVar3 = ConstraintAnchor.b.TOP;
                    constraintWidget.U(bVar3, constraintWidget9, bVar3, ((ViewGroup.MarginLayoutParams) layoutParams).topMargin, layoutParams.f1881u);
                }
            } else {
                int i21 = layoutParams.f1862i;
                if (i21 != -1 && (constraintWidget4 = sparseArray.get(i21)) != null) {
                    constraintWidget.U(ConstraintAnchor.b.TOP, constraintWidget4, ConstraintAnchor.b.BOTTOM, ((ViewGroup.MarginLayoutParams) layoutParams).topMargin, layoutParams.f1881u);
                }
            }
            int i22 = layoutParams.f1864j;
            if (i22 != -1) {
                ConstraintWidget constraintWidget10 = sparseArray.get(i22);
                if (constraintWidget10 != null) {
                    constraintWidget.U(ConstraintAnchor.b.BOTTOM, constraintWidget10, ConstraintAnchor.b.TOP, ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin, layoutParams.f1883w);
                }
            } else {
                int i23 = layoutParams.f1866k;
                if (i23 != -1 && (constraintWidget5 = sparseArray.get(i23)) != null) {
                    ConstraintAnchor.b bVar4 = ConstraintAnchor.b.BOTTOM;
                    constraintWidget.U(bVar4, constraintWidget5, bVar4, ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin, layoutParams.f1883w);
                }
            }
            int i24 = layoutParams.f1868l;
            if (i24 != -1) {
                View view2 = this.f1824e.get(i24);
                ConstraintWidget constraintWidget11 = sparseArray.get(layoutParams.f1868l);
                if (constraintWidget11 != null && view2 != null && (view2.getLayoutParams() instanceof LayoutParams)) {
                    LayoutParams layoutParams2 = (LayoutParams) view2.getLayoutParams();
                    layoutParams.Y = true;
                    layoutParams2.Y = true;
                    ConstraintAnchor.b bVar5 = ConstraintAnchor.b.BASELINE;
                    constraintWidget.n(bVar5).b(constraintWidget11.n(bVar5), 0, -1, true);
                    constraintWidget.h0(true);
                    layoutParams2.f1873n0.h0(true);
                    constraintWidget.n(ConstraintAnchor.b.TOP).l();
                    constraintWidget.n(ConstraintAnchor.b.BOTTOM).l();
                }
            }
            float f13 = f10;
            if (f13 >= 0.0f) {
                constraintWidget.j0(f13);
            }
            float f14 = layoutParams.A;
            if (f14 >= 0.0f) {
                constraintWidget.y0(f14);
            }
        }
        if (z10 && ((i10 = layoutParams.Q) != -1 || layoutParams.R != -1)) {
            constraintWidget.w0(i10, layoutParams.R);
        }
        if (!layoutParams.W) {
            if (((ViewGroup.MarginLayoutParams) layoutParams).width == -1) {
                if (layoutParams.T) {
                    constraintWidget.m0(ConstraintWidget.b.MATCH_CONSTRAINT);
                } else {
                    constraintWidget.m0(ConstraintWidget.b.MATCH_PARENT);
                }
                constraintWidget.n(ConstraintAnchor.b.LEFT).f14736e = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
                constraintWidget.n(ConstraintAnchor.b.RIGHT).f14736e = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
            } else {
                constraintWidget.m0(ConstraintWidget.b.MATCH_CONSTRAINT);
                constraintWidget.F0(0);
            }
        } else {
            constraintWidget.m0(ConstraintWidget.b.FIXED);
            constraintWidget.F0(((ViewGroup.MarginLayoutParams) layoutParams).width);
            if (((ViewGroup.MarginLayoutParams) layoutParams).width == -2) {
                constraintWidget.m0(ConstraintWidget.b.WRAP_CONTENT);
            }
        }
        if (!layoutParams.X) {
            if (((ViewGroup.MarginLayoutParams) layoutParams).height == -1) {
                if (layoutParams.U) {
                    constraintWidget.B0(ConstraintWidget.b.MATCH_CONSTRAINT);
                } else {
                    constraintWidget.B0(ConstraintWidget.b.MATCH_PARENT);
                }
                constraintWidget.n(ConstraintAnchor.b.TOP).f14736e = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                constraintWidget.n(ConstraintAnchor.b.BOTTOM).f14736e = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
            } else {
                constraintWidget.B0(ConstraintWidget.b.MATCH_CONSTRAINT);
                constraintWidget.i0(0);
            }
        } else {
            constraintWidget.B0(ConstraintWidget.b.FIXED);
            constraintWidget.i0(((ViewGroup.MarginLayoutParams) layoutParams).height);
            if (((ViewGroup.MarginLayoutParams) layoutParams).height == -2) {
                constraintWidget.B0(ConstraintWidget.b.WRAP_CONTENT);
            }
        }
        constraintWidget.f0(layoutParams.B);
        constraintWidget.o0(layoutParams.E);
        constraintWidget.D0(layoutParams.F);
        constraintWidget.k0(layoutParams.G);
        constraintWidget.z0(layoutParams.H);
        constraintWidget.n0(layoutParams.I, layoutParams.K, layoutParams.M, layoutParams.O);
        constraintWidget.C0(layoutParams.J, layoutParams.L, layoutParams.N, layoutParams.P);
    }

    public int getMaxHeight() {
        return this.f1830k;
    }

    public int getMaxWidth() {
        return this.f1829j;
    }

    public int getMinHeight() {
        return this.f1828i;
    }

    public int getMinWidth() {
        return this.f1827h;
    }

    public int getOptimizationLevel() {
        return this.f1826g.X0();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // android.view.ViewGroup
    /* renamed from: i, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        View content;
        int childCount = getChildCount();
        boolean isInEditMode = isInEditMode();
        for (int i14 = 0; i14 < childCount; i14++) {
            View childAt = getChildAt(i14);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            ConstraintWidget constraintWidget = layoutParams.f1873n0;
            if ((childAt.getVisibility() != 8 || layoutParams.Z || layoutParams.f1847a0 || layoutParams.f1851c0 || isInEditMode) && !layoutParams.f1849b0) {
                int R = constraintWidget.R();
                int S = constraintWidget.S();
                int Q = constraintWidget.Q() + R;
                int w10 = constraintWidget.w() + S;
                childAt.layout(R, S, Q, w10);
                if ((childAt instanceof Placeholder) && (content = ((Placeholder) childAt).getContent()) != null) {
                    content.setVisibility(0);
                    content.layout(R, S, Q, w10);
                }
            }
        }
        int size = this.f1825f.size();
        if (size > 0) {
            for (int i15 = 0; i15 < size; i15++) {
                this.f1825f.get(i15).p(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int i10, int i11) {
        this.f1845z = i10;
        this.A = i11;
        this.f1826g.j1(u());
        if (this.f1831l) {
            this.f1831l = false;
            if (C()) {
                this.f1826g.l1();
            }
        }
        y(this.f1826g, this.f1832m, i10, i11);
        x(i10, i11, this.f1826g.Q(), this.f1826g.w(), this.f1826g.d1(), this.f1826g.b1());
    }

    @Override // android.view.ViewGroup
    public void onViewAdded(View view) {
        super.onViewAdded(view);
        ConstraintWidget s7 = s(view);
        if ((view instanceof Guideline) && !(s7 instanceof h)) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            h hVar = new h();
            layoutParams.f1873n0 = hVar;
            layoutParams.Z = true;
            hVar.S0(layoutParams.S);
        }
        if (view instanceof ConstraintHelper) {
            ConstraintHelper constraintHelper = (ConstraintHelper) view;
            constraintHelper.u();
            ((LayoutParams) view.getLayoutParams()).f1847a0 = true;
            if (!this.f1825f.contains(constraintHelper)) {
                this.f1825f.add(constraintHelper);
            }
        }
        this.f1824e.put(view.getId(), view);
        this.f1831l = true;
    }

    @Override // android.view.ViewGroup
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        this.f1824e.remove(view.getId());
        this.f1826g.N0(s(view));
        this.f1825f.remove(view);
        this.f1831l = true;
    }

    public Object p(int i10, Object obj) {
        if (i10 != 0 || !(obj instanceof String)) {
            return null;
        }
        String str = (String) obj;
        HashMap<String, Integer> hashMap = this.f1836q;
        if (hashMap == null || !hashMap.containsKey(str)) {
            return null;
        }
        return this.f1836q.get(str);
    }

    public View r(int i10) {
        return this.f1824e.get(i10);
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        super.removeView(view);
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        v();
        super.requestLayout();
    }

    public final ConstraintWidget s(View view) {
        if (view == this) {
            return this.f1826g;
        }
        if (view == null) {
            return null;
        }
        return ((LayoutParams) view.getLayoutParams()).f1873n0;
    }

    public void setConstraintSet(ConstraintSet constraintSet) {
        this.f1833n = constraintSet;
    }

    @Override // android.view.View
    public void setId(int i10) {
        this.f1824e.remove(getId());
        super.setId(i10);
        this.f1824e.put(getId(), this);
    }

    public void setMaxHeight(int i10) {
        if (i10 == this.f1830k) {
            return;
        }
        this.f1830k = i10;
        requestLayout();
    }

    public void setMaxWidth(int i10) {
        if (i10 == this.f1829j) {
            return;
        }
        this.f1829j = i10;
        requestLayout();
    }

    public void setMinHeight(int i10) {
        if (i10 == this.f1828i) {
            return;
        }
        this.f1828i = i10;
        requestLayout();
    }

    public void setMinWidth(int i10) {
        if (i10 == this.f1827h) {
            return;
        }
        this.f1827h = i10;
        requestLayout();
    }

    public void setOnConstraintsChanged(ConstraintsChangedListener constraintsChangedListener) {
        ConstraintLayoutStates constraintLayoutStates = this.f1834o;
        if (constraintLayoutStates != null) {
            constraintLayoutStates.c(constraintsChangedListener);
        }
    }

    public void setOptimizationLevel(int i10) {
        this.f1832m = i10;
        this.f1826g.i1(i10);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean u() {
        return ((getContext().getApplicationInfo().flags & 4194304) != 0) && 1 == getLayoutDirection();
    }

    protected void w(int i10) {
        this.f1834o = new ConstraintLayoutStates(getContext(), this, i10);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void x(int i10, int i11, int i12, int i13, boolean z10, boolean z11) {
        b bVar = this.f1844y;
        int i14 = bVar.f1893e;
        int resolveSizeAndState = ViewGroup.resolveSizeAndState(i12 + bVar.f1892d, i10, 0);
        int resolveSizeAndState2 = ViewGroup.resolveSizeAndState(i13 + i14, i11, 0) & 16777215;
        int min = Math.min(this.f1829j, resolveSizeAndState & 16777215);
        int min2 = Math.min(this.f1830k, resolveSizeAndState2);
        if (z10) {
            min |= 16777216;
        }
        if (z11) {
            min2 |= 16777216;
        }
        setMeasuredDimension(min, min2);
        this.f1837r = min;
        this.f1838s = min2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void y(ConstraintWidgetContainer constraintWidgetContainer, int i10, int i11, int i12) {
        int mode = View.MeasureSpec.getMode(i11);
        int size = View.MeasureSpec.getSize(i11);
        int mode2 = View.MeasureSpec.getMode(i12);
        int size2 = View.MeasureSpec.getSize(i12);
        int max = Math.max(0, getPaddingTop());
        int max2 = Math.max(0, getPaddingBottom());
        int i13 = max + max2;
        int paddingWidth = getPaddingWidth();
        this.f1844y.c(i11, i12, max, max2, paddingWidth, i13);
        int max3 = Math.max(0, getPaddingStart());
        int max4 = Math.max(0, getPaddingEnd());
        if (max3 <= 0 && max4 <= 0) {
            max4 = Math.max(0, getPaddingLeft());
        } else if (!u()) {
            max4 = max3;
        }
        int i14 = size - paddingWidth;
        int i15 = size2 - i13;
        B(constraintWidgetContainer, mode, i14, mode2, i15);
        constraintWidgetContainer.e1(i10, mode, i14, mode2, i15, this.f1837r, this.f1838s, max4, max);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public ConstraintLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f1824e = new SparseArray<>();
        this.f1825f = new ArrayList<>(4);
        this.f1826g = new ConstraintWidgetContainer();
        this.f1827h = 0;
        this.f1828i = 0;
        this.f1829j = Integer.MAX_VALUE;
        this.f1830k = Integer.MAX_VALUE;
        this.f1831l = true;
        this.f1832m = 263;
        this.f1833n = null;
        this.f1834o = null;
        this.f1835p = -1;
        this.f1836q = new HashMap<>();
        this.f1837r = -1;
        this.f1838s = -1;
        this.f1839t = -1;
        this.f1840u = -1;
        this.f1841v = 0;
        this.f1842w = 0;
        this.f1843x = new SparseArray<>();
        this.f1844y = new b(this);
        this.f1845z = 0;
        this.A = 0;
        t(attributeSet, 0, 0);
    }

    public ConstraintLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f1824e = new SparseArray<>();
        this.f1825f = new ArrayList<>(4);
        this.f1826g = new ConstraintWidgetContainer();
        this.f1827h = 0;
        this.f1828i = 0;
        this.f1829j = Integer.MAX_VALUE;
        this.f1830k = Integer.MAX_VALUE;
        this.f1831l = true;
        this.f1832m = 263;
        this.f1833n = null;
        this.f1834o = null;
        this.f1835p = -1;
        this.f1836q = new HashMap<>();
        this.f1837r = -1;
        this.f1838s = -1;
        this.f1839t = -1;
        this.f1840u = -1;
        this.f1841v = 0;
        this.f1842w = 0;
        this.f1843x = new SparseArray<>();
        this.f1844y = new b(this);
        this.f1845z = 0;
        this.A = 0;
        t(attributeSet, i10, 0);
    }

    @TargetApi(21)
    public ConstraintLayout(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f1824e = new SparseArray<>();
        this.f1825f = new ArrayList<>(4);
        this.f1826g = new ConstraintWidgetContainer();
        this.f1827h = 0;
        this.f1828i = 0;
        this.f1829j = Integer.MAX_VALUE;
        this.f1830k = Integer.MAX_VALUE;
        this.f1831l = true;
        this.f1832m = 263;
        this.f1833n = null;
        this.f1834o = null;
        this.f1835p = -1;
        this.f1836q = new HashMap<>();
        this.f1837r = -1;
        this.f1838s = -1;
        this.f1839t = -1;
        this.f1840u = -1;
        this.f1841v = 0;
        this.f1842w = 0;
        this.f1843x = new SparseArray<>();
        this.f1844y = new b(this);
        this.f1845z = 0;
        this.A = 0;
        t(attributeSet, i10, i11);
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public float A;
        public String B;
        float C;
        int D;
        public float E;
        public float F;
        public int G;
        public int H;
        public int I;
        public int J;
        public int K;
        public int L;
        public int M;
        public int N;
        public float O;
        public float P;
        public int Q;
        public int R;
        public int S;
        public boolean T;
        public boolean U;
        public String V;
        boolean W;
        boolean X;
        boolean Y;
        boolean Z;

        /* renamed from: a, reason: collision with root package name */
        public int f1846a;

        /* renamed from: a0, reason: collision with root package name */
        boolean f1847a0;

        /* renamed from: b, reason: collision with root package name */
        public int f1848b;

        /* renamed from: b0, reason: collision with root package name */
        boolean f1849b0;

        /* renamed from: c, reason: collision with root package name */
        public float f1850c;

        /* renamed from: c0, reason: collision with root package name */
        boolean f1851c0;

        /* renamed from: d, reason: collision with root package name */
        public int f1852d;

        /* renamed from: d0, reason: collision with root package name */
        int f1853d0;

        /* renamed from: e, reason: collision with root package name */
        public int f1854e;

        /* renamed from: e0, reason: collision with root package name */
        int f1855e0;

        /* renamed from: f, reason: collision with root package name */
        public int f1856f;

        /* renamed from: f0, reason: collision with root package name */
        int f1857f0;

        /* renamed from: g, reason: collision with root package name */
        public int f1858g;

        /* renamed from: g0, reason: collision with root package name */
        int f1859g0;

        /* renamed from: h, reason: collision with root package name */
        public int f1860h;

        /* renamed from: h0, reason: collision with root package name */
        int f1861h0;

        /* renamed from: i, reason: collision with root package name */
        public int f1862i;

        /* renamed from: i0, reason: collision with root package name */
        int f1863i0;

        /* renamed from: j, reason: collision with root package name */
        public int f1864j;

        /* renamed from: j0, reason: collision with root package name */
        float f1865j0;

        /* renamed from: k, reason: collision with root package name */
        public int f1866k;

        /* renamed from: k0, reason: collision with root package name */
        int f1867k0;

        /* renamed from: l, reason: collision with root package name */
        public int f1868l;

        /* renamed from: l0, reason: collision with root package name */
        int f1869l0;

        /* renamed from: m, reason: collision with root package name */
        public int f1870m;

        /* renamed from: m0, reason: collision with root package name */
        float f1871m0;

        /* renamed from: n, reason: collision with root package name */
        public int f1872n;

        /* renamed from: n0, reason: collision with root package name */
        ConstraintWidget f1873n0;

        /* renamed from: o, reason: collision with root package name */
        public float f1874o;

        /* renamed from: o0, reason: collision with root package name */
        public boolean f1875o0;

        /* renamed from: p, reason: collision with root package name */
        public int f1876p;

        /* renamed from: q, reason: collision with root package name */
        public int f1877q;

        /* renamed from: r, reason: collision with root package name */
        public int f1878r;

        /* renamed from: s, reason: collision with root package name */
        public int f1879s;

        /* renamed from: t, reason: collision with root package name */
        public int f1880t;

        /* renamed from: u, reason: collision with root package name */
        public int f1881u;

        /* renamed from: v, reason: collision with root package name */
        public int f1882v;

        /* renamed from: w, reason: collision with root package name */
        public int f1883w;

        /* renamed from: x, reason: collision with root package name */
        public int f1884x;

        /* renamed from: y, reason: collision with root package name */
        public int f1885y;

        /* renamed from: z, reason: collision with root package name */
        public float f1886z;

        /* loaded from: classes.dex */
        private static class a {

            /* renamed from: a, reason: collision with root package name */
            public static final SparseIntArray f1887a;

            static {
                SparseIntArray sparseIntArray = new SparseIntArray();
                f1887a = sparseIntArray;
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_android_orientation, 1);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintTag, 51);
            }
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            int i10;
            this.f1846a = -1;
            this.f1848b = -1;
            this.f1850c = -1.0f;
            this.f1852d = -1;
            this.f1854e = -1;
            this.f1856f = -1;
            this.f1858g = -1;
            this.f1860h = -1;
            this.f1862i = -1;
            this.f1864j = -1;
            this.f1866k = -1;
            this.f1868l = -1;
            this.f1870m = -1;
            this.f1872n = 0;
            this.f1874o = 0.0f;
            this.f1876p = -1;
            this.f1877q = -1;
            this.f1878r = -1;
            this.f1879s = -1;
            this.f1880t = -1;
            this.f1881u = -1;
            this.f1882v = -1;
            this.f1883w = -1;
            this.f1884x = -1;
            this.f1885y = -1;
            this.f1886z = 0.5f;
            this.A = 0.5f;
            this.B = null;
            this.C = 0.0f;
            this.D = 1;
            this.E = -1.0f;
            this.F = -1.0f;
            this.G = 0;
            this.H = 0;
            this.I = 0;
            this.J = 0;
            this.K = 0;
            this.L = 0;
            this.M = 0;
            this.N = 0;
            this.O = 1.0f;
            this.P = 1.0f;
            this.Q = -1;
            this.R = -1;
            this.S = -1;
            this.T = false;
            this.U = false;
            this.V = null;
            this.W = true;
            this.X = true;
            this.Y = false;
            this.Z = false;
            this.f1847a0 = false;
            this.f1849b0 = false;
            this.f1851c0 = false;
            this.f1853d0 = -1;
            this.f1855e0 = -1;
            this.f1857f0 = -1;
            this.f1859g0 = -1;
            this.f1861h0 = -1;
            this.f1863i0 = -1;
            this.f1865j0 = 0.5f;
            this.f1873n0 = new ConstraintWidget();
            this.f1875o0 = false;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ConstraintLayout_Layout);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i11 = 0; i11 < indexCount; i11++) {
                int index = obtainStyledAttributes.getIndex(i11);
                int i12 = a.f1887a.get(index);
                switch (i12) {
                    case 1:
                        this.S = obtainStyledAttributes.getInt(index, this.S);
                        break;
                    case 2:
                        int resourceId = obtainStyledAttributes.getResourceId(index, this.f1870m);
                        this.f1870m = resourceId;
                        if (resourceId == -1) {
                            this.f1870m = obtainStyledAttributes.getInt(index, -1);
                            break;
                        } else {
                            break;
                        }
                    case 3:
                        this.f1872n = obtainStyledAttributes.getDimensionPixelSize(index, this.f1872n);
                        break;
                    case 4:
                        float f10 = obtainStyledAttributes.getFloat(index, this.f1874o) % 360.0f;
                        this.f1874o = f10;
                        if (f10 < 0.0f) {
                            this.f1874o = (360.0f - f10) % 360.0f;
                            break;
                        } else {
                            break;
                        }
                    case 5:
                        this.f1846a = obtainStyledAttributes.getDimensionPixelOffset(index, this.f1846a);
                        break;
                    case 6:
                        this.f1848b = obtainStyledAttributes.getDimensionPixelOffset(index, this.f1848b);
                        break;
                    case 7:
                        this.f1850c = obtainStyledAttributes.getFloat(index, this.f1850c);
                        break;
                    case 8:
                        int resourceId2 = obtainStyledAttributes.getResourceId(index, this.f1852d);
                        this.f1852d = resourceId2;
                        if (resourceId2 == -1) {
                            this.f1852d = obtainStyledAttributes.getInt(index, -1);
                            break;
                        } else {
                            break;
                        }
                    case 9:
                        int resourceId3 = obtainStyledAttributes.getResourceId(index, this.f1854e);
                        this.f1854e = resourceId3;
                        if (resourceId3 == -1) {
                            this.f1854e = obtainStyledAttributes.getInt(index, -1);
                            break;
                        } else {
                            break;
                        }
                    case 10:
                        int resourceId4 = obtainStyledAttributes.getResourceId(index, this.f1856f);
                        this.f1856f = resourceId4;
                        if (resourceId4 == -1) {
                            this.f1856f = obtainStyledAttributes.getInt(index, -1);
                            break;
                        } else {
                            break;
                        }
                    case 11:
                        int resourceId5 = obtainStyledAttributes.getResourceId(index, this.f1858g);
                        this.f1858g = resourceId5;
                        if (resourceId5 == -1) {
                            this.f1858g = obtainStyledAttributes.getInt(index, -1);
                            break;
                        } else {
                            break;
                        }
                    case 12:
                        int resourceId6 = obtainStyledAttributes.getResourceId(index, this.f1860h);
                        this.f1860h = resourceId6;
                        if (resourceId6 == -1) {
                            this.f1860h = obtainStyledAttributes.getInt(index, -1);
                            break;
                        } else {
                            break;
                        }
                    case 13:
                        int resourceId7 = obtainStyledAttributes.getResourceId(index, this.f1862i);
                        this.f1862i = resourceId7;
                        if (resourceId7 == -1) {
                            this.f1862i = obtainStyledAttributes.getInt(index, -1);
                            break;
                        } else {
                            break;
                        }
                    case 14:
                        int resourceId8 = obtainStyledAttributes.getResourceId(index, this.f1864j);
                        this.f1864j = resourceId8;
                        if (resourceId8 == -1) {
                            this.f1864j = obtainStyledAttributes.getInt(index, -1);
                            break;
                        } else {
                            break;
                        }
                    case 15:
                        int resourceId9 = obtainStyledAttributes.getResourceId(index, this.f1866k);
                        this.f1866k = resourceId9;
                        if (resourceId9 == -1) {
                            this.f1866k = obtainStyledAttributes.getInt(index, -1);
                            break;
                        } else {
                            break;
                        }
                    case 16:
                        int resourceId10 = obtainStyledAttributes.getResourceId(index, this.f1868l);
                        this.f1868l = resourceId10;
                        if (resourceId10 == -1) {
                            this.f1868l = obtainStyledAttributes.getInt(index, -1);
                            break;
                        } else {
                            break;
                        }
                    case 17:
                        int resourceId11 = obtainStyledAttributes.getResourceId(index, this.f1876p);
                        this.f1876p = resourceId11;
                        if (resourceId11 == -1) {
                            this.f1876p = obtainStyledAttributes.getInt(index, -1);
                            break;
                        } else {
                            break;
                        }
                    case 18:
                        int resourceId12 = obtainStyledAttributes.getResourceId(index, this.f1877q);
                        this.f1877q = resourceId12;
                        if (resourceId12 == -1) {
                            this.f1877q = obtainStyledAttributes.getInt(index, -1);
                            break;
                        } else {
                            break;
                        }
                    case 19:
                        int resourceId13 = obtainStyledAttributes.getResourceId(index, this.f1878r);
                        this.f1878r = resourceId13;
                        if (resourceId13 == -1) {
                            this.f1878r = obtainStyledAttributes.getInt(index, -1);
                            break;
                        } else {
                            break;
                        }
                    case 20:
                        int resourceId14 = obtainStyledAttributes.getResourceId(index, this.f1879s);
                        this.f1879s = resourceId14;
                        if (resourceId14 == -1) {
                            this.f1879s = obtainStyledAttributes.getInt(index, -1);
                            break;
                        } else {
                            break;
                        }
                    case 21:
                        this.f1880t = obtainStyledAttributes.getDimensionPixelSize(index, this.f1880t);
                        break;
                    case 22:
                        this.f1881u = obtainStyledAttributes.getDimensionPixelSize(index, this.f1881u);
                        break;
                    case 23:
                        this.f1882v = obtainStyledAttributes.getDimensionPixelSize(index, this.f1882v);
                        break;
                    case 24:
                        this.f1883w = obtainStyledAttributes.getDimensionPixelSize(index, this.f1883w);
                        break;
                    case 25:
                        this.f1884x = obtainStyledAttributes.getDimensionPixelSize(index, this.f1884x);
                        break;
                    case 26:
                        this.f1885y = obtainStyledAttributes.getDimensionPixelSize(index, this.f1885y);
                        break;
                    case 27:
                        this.T = obtainStyledAttributes.getBoolean(index, this.T);
                        break;
                    case 28:
                        this.U = obtainStyledAttributes.getBoolean(index, this.U);
                        break;
                    case 29:
                        this.f1886z = obtainStyledAttributes.getFloat(index, this.f1886z);
                        break;
                    case 30:
                        this.A = obtainStyledAttributes.getFloat(index, this.A);
                        break;
                    case 31:
                        int i13 = obtainStyledAttributes.getInt(index, 0);
                        this.I = i13;
                        if (i13 == 1) {
                            Log.e("ConstraintLayout", "layout_constraintWidth_default=\"wrap\" is deprecated.\nUse layout_width=\"WRAP_CONTENT\" and layout_constrainedWidth=\"true\" instead.");
                            break;
                        } else {
                            break;
                        }
                    case 32:
                        int i14 = obtainStyledAttributes.getInt(index, 0);
                        this.J = i14;
                        if (i14 == 1) {
                            Log.e("ConstraintLayout", "layout_constraintHeight_default=\"wrap\" is deprecated.\nUse layout_height=\"WRAP_CONTENT\" and layout_constrainedHeight=\"true\" instead.");
                            break;
                        } else {
                            break;
                        }
                    case 33:
                        try {
                            this.K = obtainStyledAttributes.getDimensionPixelSize(index, this.K);
                            break;
                        } catch (Exception unused) {
                            if (obtainStyledAttributes.getInt(index, this.K) == -2) {
                                this.K = -2;
                                break;
                            } else {
                                break;
                            }
                        }
                    case 34:
                        try {
                            this.M = obtainStyledAttributes.getDimensionPixelSize(index, this.M);
                            break;
                        } catch (Exception unused2) {
                            if (obtainStyledAttributes.getInt(index, this.M) == -2) {
                                this.M = -2;
                                break;
                            } else {
                                break;
                            }
                        }
                    case 35:
                        this.O = Math.max(0.0f, obtainStyledAttributes.getFloat(index, this.O));
                        this.I = 2;
                        break;
                    case 36:
                        try {
                            this.L = obtainStyledAttributes.getDimensionPixelSize(index, this.L);
                            break;
                        } catch (Exception unused3) {
                            if (obtainStyledAttributes.getInt(index, this.L) == -2) {
                                this.L = -2;
                                break;
                            } else {
                                break;
                            }
                        }
                    case 37:
                        try {
                            this.N = obtainStyledAttributes.getDimensionPixelSize(index, this.N);
                            break;
                        } catch (Exception unused4) {
                            if (obtainStyledAttributes.getInt(index, this.N) == -2) {
                                this.N = -2;
                                break;
                            } else {
                                break;
                            }
                        }
                    case 38:
                        this.P = Math.max(0.0f, obtainStyledAttributes.getFloat(index, this.P));
                        this.J = 2;
                        break;
                    default:
                        switch (i12) {
                            case 44:
                                String string = obtainStyledAttributes.getString(index);
                                this.B = string;
                                this.C = Float.NaN;
                                this.D = -1;
                                if (string != null) {
                                    int length = string.length();
                                    int indexOf = this.B.indexOf(44);
                                    if (indexOf <= 0 || indexOf >= length - 1) {
                                        i10 = 0;
                                    } else {
                                        String substring = this.B.substring(0, indexOf);
                                        if (substring.equalsIgnoreCase("W")) {
                                            this.D = 0;
                                        } else if (substring.equalsIgnoreCase("H")) {
                                            this.D = 1;
                                        }
                                        i10 = indexOf + 1;
                                    }
                                    int indexOf2 = this.B.indexOf(58);
                                    if (indexOf2 >= 0 && indexOf2 < length - 1) {
                                        String substring2 = this.B.substring(i10, indexOf2);
                                        String substring3 = this.B.substring(indexOf2 + 1);
                                        if (substring2.length() > 0 && substring3.length() > 0) {
                                            try {
                                                float parseFloat = Float.parseFloat(substring2);
                                                float parseFloat2 = Float.parseFloat(substring3);
                                                if (parseFloat > 0.0f && parseFloat2 > 0.0f) {
                                                    if (this.D == 1) {
                                                        this.C = Math.abs(parseFloat2 / parseFloat);
                                                        break;
                                                    } else {
                                                        this.C = Math.abs(parseFloat / parseFloat2);
                                                        break;
                                                    }
                                                }
                                            } catch (NumberFormatException unused5) {
                                                break;
                                            }
                                        }
                                    } else {
                                        String substring4 = this.B.substring(i10);
                                        if (substring4.length() > 0) {
                                            this.C = Float.parseFloat(substring4);
                                            break;
                                        } else {
                                            break;
                                        }
                                    }
                                } else {
                                    break;
                                }
                                break;
                            case 45:
                                this.E = obtainStyledAttributes.getFloat(index, this.E);
                                break;
                            case 46:
                                this.F = obtainStyledAttributes.getFloat(index, this.F);
                                break;
                            case 47:
                                this.G = obtainStyledAttributes.getInt(index, 0);
                                break;
                            case 48:
                                this.H = obtainStyledAttributes.getInt(index, 0);
                                break;
                            case 49:
                                this.Q = obtainStyledAttributes.getDimensionPixelOffset(index, this.Q);
                                break;
                            case 50:
                                this.R = obtainStyledAttributes.getDimensionPixelOffset(index, this.R);
                                break;
                            case 51:
                                this.V = obtainStyledAttributes.getString(index);
                                break;
                        }
                }
            }
            obtainStyledAttributes.recycle();
            c();
        }

        public String a() {
            return this.V;
        }

        public ConstraintWidget b() {
            return this.f1873n0;
        }

        public void c() {
            this.Z = false;
            this.W = true;
            this.X = true;
            int i10 = ((ViewGroup.MarginLayoutParams) this).width;
            if (i10 == -2 && this.T) {
                this.W = false;
                if (this.I == 0) {
                    this.I = 1;
                }
            }
            int i11 = ((ViewGroup.MarginLayoutParams) this).height;
            if (i11 == -2 && this.U) {
                this.X = false;
                if (this.J == 0) {
                    this.J = 1;
                }
            }
            if (i10 == 0 || i10 == -1) {
                this.W = false;
                if (i10 == 0 && this.I == 1) {
                    ((ViewGroup.MarginLayoutParams) this).width = -2;
                    this.T = true;
                }
            }
            if (i11 == 0 || i11 == -1) {
                this.X = false;
                if (i11 == 0 && this.J == 1) {
                    ((ViewGroup.MarginLayoutParams) this).height = -2;
                    this.U = true;
                }
            }
            if (this.f1850c == -1.0f && this.f1846a == -1 && this.f1848b == -1) {
                return;
            }
            this.Z = true;
            this.W = true;
            this.X = true;
            if (!(this.f1873n0 instanceof h)) {
                this.f1873n0 = new h();
            }
            ((h) this.f1873n0).S0(this.S);
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x0048  */
        /* JADX WARN: Removed duplicated region for block: B:15:0x004f  */
        /* JADX WARN: Removed duplicated region for block: B:18:0x0056  */
        /* JADX WARN: Removed duplicated region for block: B:21:0x005c  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0062  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x0074  */
        /* JADX WARN: Removed duplicated region for block: B:32:0x007c  */
        @Override // android.view.ViewGroup.MarginLayoutParams, android.view.ViewGroup.LayoutParams
        @TargetApi(17)
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void resolveLayoutDirection(int i10) {
            int i11;
            int i12;
            int i13;
            int i14;
            int i15 = ((ViewGroup.MarginLayoutParams) this).leftMargin;
            int i16 = ((ViewGroup.MarginLayoutParams) this).rightMargin;
            super.resolveLayoutDirection(i10);
            boolean z10 = false;
            boolean z11 = 1 == getLayoutDirection();
            this.f1857f0 = -1;
            this.f1859g0 = -1;
            this.f1853d0 = -1;
            this.f1855e0 = -1;
            this.f1861h0 = this.f1880t;
            this.f1863i0 = this.f1882v;
            float f10 = this.f1886z;
            this.f1865j0 = f10;
            int i17 = this.f1846a;
            this.f1867k0 = i17;
            int i18 = this.f1848b;
            this.f1869l0 = i18;
            float f11 = this.f1850c;
            this.f1871m0 = f11;
            if (z11) {
                int i19 = this.f1876p;
                if (i19 != -1) {
                    this.f1857f0 = i19;
                } else {
                    int i20 = this.f1877q;
                    if (i20 != -1) {
                        this.f1859g0 = i20;
                    }
                    i11 = this.f1878r;
                    if (i11 != -1) {
                        this.f1855e0 = i11;
                        z10 = true;
                    }
                    i12 = this.f1879s;
                    if (i12 != -1) {
                        this.f1853d0 = i12;
                        z10 = true;
                    }
                    i13 = this.f1884x;
                    if (i13 != -1) {
                        this.f1863i0 = i13;
                    }
                    i14 = this.f1885y;
                    if (i14 != -1) {
                        this.f1861h0 = i14;
                    }
                    if (z10) {
                        this.f1865j0 = 1.0f - f10;
                    }
                    if (this.Z && this.S == 1) {
                        if (f11 == -1.0f) {
                            this.f1871m0 = 1.0f - f11;
                            this.f1867k0 = -1;
                            this.f1869l0 = -1;
                        } else if (i17 != -1) {
                            this.f1869l0 = i17;
                            this.f1867k0 = -1;
                            this.f1871m0 = -1.0f;
                        } else if (i18 != -1) {
                            this.f1867k0 = i18;
                            this.f1869l0 = -1;
                            this.f1871m0 = -1.0f;
                        }
                    }
                }
                z10 = true;
                i11 = this.f1878r;
                if (i11 != -1) {
                }
                i12 = this.f1879s;
                if (i12 != -1) {
                }
                i13 = this.f1884x;
                if (i13 != -1) {
                }
                i14 = this.f1885y;
                if (i14 != -1) {
                }
                if (z10) {
                }
                if (this.Z) {
                    if (f11 == -1.0f) {
                    }
                }
            } else {
                int i21 = this.f1876p;
                if (i21 != -1) {
                    this.f1855e0 = i21;
                }
                int i22 = this.f1877q;
                if (i22 != -1) {
                    this.f1853d0 = i22;
                }
                int i23 = this.f1878r;
                if (i23 != -1) {
                    this.f1857f0 = i23;
                }
                int i24 = this.f1879s;
                if (i24 != -1) {
                    this.f1859g0 = i24;
                }
                int i25 = this.f1884x;
                if (i25 != -1) {
                    this.f1861h0 = i25;
                }
                int i26 = this.f1885y;
                if (i26 != -1) {
                    this.f1863i0 = i26;
                }
            }
            if (this.f1878r == -1 && this.f1879s == -1 && this.f1877q == -1 && this.f1876p == -1) {
                int i27 = this.f1856f;
                if (i27 != -1) {
                    this.f1857f0 = i27;
                    if (((ViewGroup.MarginLayoutParams) this).rightMargin <= 0 && i16 > 0) {
                        ((ViewGroup.MarginLayoutParams) this).rightMargin = i16;
                    }
                } else {
                    int i28 = this.f1858g;
                    if (i28 != -1) {
                        this.f1859g0 = i28;
                        if (((ViewGroup.MarginLayoutParams) this).rightMargin <= 0 && i16 > 0) {
                            ((ViewGroup.MarginLayoutParams) this).rightMargin = i16;
                        }
                    }
                }
                int i29 = this.f1852d;
                if (i29 != -1) {
                    this.f1853d0 = i29;
                    if (((ViewGroup.MarginLayoutParams) this).leftMargin > 0 || i15 <= 0) {
                        return;
                    }
                    ((ViewGroup.MarginLayoutParams) this).leftMargin = i15;
                    return;
                }
                int i30 = this.f1854e;
                if (i30 != -1) {
                    this.f1855e0 = i30;
                    if (((ViewGroup.MarginLayoutParams) this).leftMargin > 0 || i15 <= 0) {
                        return;
                    }
                    ((ViewGroup.MarginLayoutParams) this).leftMargin = i15;
                }
            }
        }

        public LayoutParams(int i10, int i11) {
            super(i10, i11);
            this.f1846a = -1;
            this.f1848b = -1;
            this.f1850c = -1.0f;
            this.f1852d = -1;
            this.f1854e = -1;
            this.f1856f = -1;
            this.f1858g = -1;
            this.f1860h = -1;
            this.f1862i = -1;
            this.f1864j = -1;
            this.f1866k = -1;
            this.f1868l = -1;
            this.f1870m = -1;
            this.f1872n = 0;
            this.f1874o = 0.0f;
            this.f1876p = -1;
            this.f1877q = -1;
            this.f1878r = -1;
            this.f1879s = -1;
            this.f1880t = -1;
            this.f1881u = -1;
            this.f1882v = -1;
            this.f1883w = -1;
            this.f1884x = -1;
            this.f1885y = -1;
            this.f1886z = 0.5f;
            this.A = 0.5f;
            this.B = null;
            this.C = 0.0f;
            this.D = 1;
            this.E = -1.0f;
            this.F = -1.0f;
            this.G = 0;
            this.H = 0;
            this.I = 0;
            this.J = 0;
            this.K = 0;
            this.L = 0;
            this.M = 0;
            this.N = 0;
            this.O = 1.0f;
            this.P = 1.0f;
            this.Q = -1;
            this.R = -1;
            this.S = -1;
            this.T = false;
            this.U = false;
            this.V = null;
            this.W = true;
            this.X = true;
            this.Y = false;
            this.Z = false;
            this.f1847a0 = false;
            this.f1849b0 = false;
            this.f1851c0 = false;
            this.f1853d0 = -1;
            this.f1855e0 = -1;
            this.f1857f0 = -1;
            this.f1859g0 = -1;
            this.f1861h0 = -1;
            this.f1863i0 = -1;
            this.f1865j0 = 0.5f;
            this.f1873n0 = new ConstraintWidget();
            this.f1875o0 = false;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f1846a = -1;
            this.f1848b = -1;
            this.f1850c = -1.0f;
            this.f1852d = -1;
            this.f1854e = -1;
            this.f1856f = -1;
            this.f1858g = -1;
            this.f1860h = -1;
            this.f1862i = -1;
            this.f1864j = -1;
            this.f1866k = -1;
            this.f1868l = -1;
            this.f1870m = -1;
            this.f1872n = 0;
            this.f1874o = 0.0f;
            this.f1876p = -1;
            this.f1877q = -1;
            this.f1878r = -1;
            this.f1879s = -1;
            this.f1880t = -1;
            this.f1881u = -1;
            this.f1882v = -1;
            this.f1883w = -1;
            this.f1884x = -1;
            this.f1885y = -1;
            this.f1886z = 0.5f;
            this.A = 0.5f;
            this.B = null;
            this.C = 0.0f;
            this.D = 1;
            this.E = -1.0f;
            this.F = -1.0f;
            this.G = 0;
            this.H = 0;
            this.I = 0;
            this.J = 0;
            this.K = 0;
            this.L = 0;
            this.M = 0;
            this.N = 0;
            this.O = 1.0f;
            this.P = 1.0f;
            this.Q = -1;
            this.R = -1;
            this.S = -1;
            this.T = false;
            this.U = false;
            this.V = null;
            this.W = true;
            this.X = true;
            this.Y = false;
            this.Z = false;
            this.f1847a0 = false;
            this.f1849b0 = false;
            this.f1851c0 = false;
            this.f1853d0 = -1;
            this.f1855e0 = -1;
            this.f1857f0 = -1;
            this.f1859g0 = -1;
            this.f1861h0 = -1;
            this.f1863i0 = -1;
            this.f1865j0 = 0.5f;
            this.f1873n0 = new ConstraintWidget();
            this.f1875o0 = false;
        }
    }
}
