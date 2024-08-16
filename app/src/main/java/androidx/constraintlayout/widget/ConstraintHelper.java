package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import java.util.Arrays;
import java.util.HashMap;
import m.ConstraintWidget;
import m.ConstraintWidgetContainer;
import m.Helper;
import m.HelperWidget;

/* loaded from: classes.dex */
public abstract class ConstraintHelper extends View {

    /* renamed from: e, reason: collision with root package name */
    protected int[] f1816e;

    /* renamed from: f, reason: collision with root package name */
    protected int f1817f;

    /* renamed from: g, reason: collision with root package name */
    protected Context f1818g;

    /* renamed from: h, reason: collision with root package name */
    protected Helper f1819h;

    /* renamed from: i, reason: collision with root package name */
    protected boolean f1820i;

    /* renamed from: j, reason: collision with root package name */
    protected String f1821j;

    /* renamed from: k, reason: collision with root package name */
    private View[] f1822k;

    /* renamed from: l, reason: collision with root package name */
    private HashMap<Integer, String> f1823l;

    public ConstraintHelper(Context context) {
        super(context);
        this.f1816e = new int[32];
        this.f1820i = false;
        this.f1822k = null;
        this.f1823l = new HashMap<>();
        this.f1818g = context;
        m(null);
    }

    private void c(String str) {
        if (str == null || str.length() == 0 || this.f1818g == null) {
            return;
        }
        String trim = str.trim();
        if (getParent() instanceof ConstraintLayout) {
        }
        int k10 = k(trim);
        if (k10 != 0) {
            this.f1823l.put(Integer.valueOf(k10), trim);
            f(k10);
            return;
        }
        Log.w("ConstraintHelper", "Could not find id of \"" + trim + "\"");
    }

    private void f(int i10) {
        if (i10 == getId()) {
            return;
        }
        int i11 = this.f1817f + 1;
        int[] iArr = this.f1816e;
        if (i11 > iArr.length) {
            this.f1816e = Arrays.copyOf(iArr, iArr.length * 2);
        }
        int[] iArr2 = this.f1816e;
        int i12 = this.f1817f;
        iArr2[i12] = i10;
        this.f1817f = i12 + 1;
    }

    private int[] i(View view, String str) {
        String[] split = str.split(",");
        view.getContext();
        int[] iArr = new int[split.length];
        int i10 = 0;
        for (String str2 : split) {
            int k10 = k(str2.trim());
            if (k10 != 0) {
                iArr[i10] = k10;
                i10++;
            }
        }
        return i10 != split.length ? Arrays.copyOf(iArr, i10) : iArr;
    }

    private int j(ConstraintLayout constraintLayout, String str) {
        Resources resources;
        if (str == null || constraintLayout == null || (resources = this.f1818g.getResources()) == null) {
            return 0;
        }
        int childCount = constraintLayout.getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = constraintLayout.getChildAt(i10);
            if (childAt.getId() != -1) {
                String str2 = null;
                try {
                    str2 = resources.getResourceEntryName(childAt.getId());
                } catch (Resources.NotFoundException unused) {
                }
                if (str.equals(str2)) {
                    return childAt.getId();
                }
            }
        }
        return 0;
    }

    private int k(String str) {
        ConstraintLayout constraintLayout = getParent() instanceof ConstraintLayout ? (ConstraintLayout) getParent() : null;
        int i10 = 0;
        if (isInEditMode() && constraintLayout != null) {
            Object p10 = constraintLayout.p(0, str);
            if (p10 instanceof Integer) {
                i10 = ((Integer) p10).intValue();
            }
        }
        if (i10 == 0 && constraintLayout != null) {
            i10 = j(constraintLayout, str);
        }
        if (i10 == 0) {
            try {
                i10 = R$id.class.getField(str).getInt(null);
            } catch (Exception unused) {
            }
        }
        return i10 == 0 ? this.f1818g.getResources().getIdentifier(str, "id", this.f1818g.getPackageName()) : i10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void g() {
        ViewParent parent = getParent();
        if (parent == null || !(parent instanceof ConstraintLayout)) {
            return;
        }
        h((ConstraintLayout) parent);
    }

    public int[] getReferencedIds() {
        return Arrays.copyOf(this.f1816e, this.f1817f);
    }

    protected void h(ConstraintLayout constraintLayout) {
        int visibility = getVisibility();
        float elevation = getElevation();
        for (int i10 = 0; i10 < this.f1817f; i10++) {
            View r10 = constraintLayout.r(this.f1816e[i10]);
            if (r10 != null) {
                r10.setVisibility(visibility);
                if (elevation > 0.0f) {
                    r10.setTranslationZ(r10.getTranslationZ() + elevation);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public View[] l(ConstraintLayout constraintLayout) {
        View[] viewArr = this.f1822k;
        if (viewArr == null || viewArr.length != this.f1817f) {
            this.f1822k = new View[this.f1817f];
        }
        for (int i10 = 0; i10 < this.f1817f; i10++) {
            this.f1822k[i10] = constraintLayout.r(this.f1816e[i10]);
        }
        return this.f1822k;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void m(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.ConstraintLayout_Layout);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.ConstraintLayout_Layout_constraint_referenced_ids) {
                    String string = obtainStyledAttributes.getString(index);
                    this.f1821j = string;
                    setIds(string);
                }
            }
        }
    }

    public void n(ConstraintSet.a aVar, HelperWidget helperWidget, ConstraintLayout.LayoutParams layoutParams, SparseArray<ConstraintWidget> sparseArray) {
        ConstraintSet.b bVar = aVar.f1955d;
        int[] iArr = bVar.f1968e0;
        if (iArr != null) {
            setReferencedIds(iArr);
        } else {
            String str = bVar.f1970f0;
            if (str != null && str.length() > 0) {
                ConstraintSet.b bVar2 = aVar.f1955d;
                bVar2.f1968e0 = i(this, bVar2.f1970f0);
            }
        }
        helperWidget.c();
        if (aVar.f1955d.f1968e0 == null) {
            return;
        }
        int i10 = 0;
        while (true) {
            int[] iArr2 = aVar.f1955d.f1968e0;
            if (i10 >= iArr2.length) {
                return;
            }
            ConstraintWidget constraintWidget = sparseArray.get(iArr2[i10]);
            if (constraintWidget != null) {
                helperWidget.b(constraintWidget);
            }
            i10++;
        }
    }

    public void o(ConstraintWidget constraintWidget, boolean z10) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        String str = this.f1821j;
        if (str != null) {
            setIds(str);
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        if (this.f1820i) {
            super.onMeasure(i10, i11);
        } else {
            setMeasuredDimension(0, 0);
        }
    }

    public void p(ConstraintLayout constraintLayout) {
    }

    public void q(ConstraintLayout constraintLayout) {
    }

    public void r(ConstraintLayout constraintLayout) {
    }

    public void s(ConstraintLayout constraintLayout) {
        String str;
        int j10;
        if (isInEditMode()) {
            setIds(this.f1821j);
        }
        Helper helper = this.f1819h;
        if (helper == null) {
            return;
        }
        helper.c();
        for (int i10 = 0; i10 < this.f1817f; i10++) {
            int i11 = this.f1816e[i10];
            View r10 = constraintLayout.r(i11);
            if (r10 == null && (j10 = j(constraintLayout, (str = this.f1823l.get(Integer.valueOf(i11))))) != 0) {
                this.f1816e[i10] = j10;
                this.f1823l.put(Integer.valueOf(j10), str);
                r10 = constraintLayout.r(j10);
            }
            if (r10 != null) {
                this.f1819h.b(constraintLayout.s(r10));
            }
        }
        this.f1819h.a(constraintLayout.f1826g);
    }

    protected void setIds(String str) {
        this.f1821j = str;
        if (str == null) {
            return;
        }
        int i10 = 0;
        this.f1817f = 0;
        while (true) {
            int indexOf = str.indexOf(44, i10);
            if (indexOf == -1) {
                c(str.substring(i10));
                return;
            } else {
                c(str.substring(i10, indexOf));
                i10 = indexOf + 1;
            }
        }
    }

    public void setReferencedIds(int[] iArr) {
        this.f1821j = null;
        this.f1817f = 0;
        for (int i10 : iArr) {
            f(i10);
        }
    }

    public void t(ConstraintWidgetContainer constraintWidgetContainer, Helper helper, SparseArray<ConstraintWidget> sparseArray) {
        helper.c();
        for (int i10 = 0; i10 < this.f1817f; i10++) {
            helper.b(sparseArray.get(this.f1816e[i10]));
        }
    }

    public void u() {
        if (this.f1819h == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
            ((ConstraintLayout.LayoutParams) layoutParams).f1873n0 = (ConstraintWidget) this.f1819h;
        }
    }

    public ConstraintHelper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f1816e = new int[32];
        this.f1820i = false;
        this.f1822k = null;
        this.f1823l = new HashMap<>();
        this.f1818g = context;
        m(attributeSet);
    }

    public ConstraintHelper(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f1816e = new int[32];
        this.f1820i = false;
        this.f1822k = null;
        this.f1823l = new HashMap<>();
        this.f1818g = context;
        m(attributeSet);
    }
}
