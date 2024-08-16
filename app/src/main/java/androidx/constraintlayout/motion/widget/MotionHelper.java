package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.R$styleable;

/* loaded from: classes.dex */
public class MotionHelper extends ConstraintHelper implements MotionLayout.i {

    /* renamed from: m, reason: collision with root package name */
    private boolean f1393m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f1394n;

    /* renamed from: o, reason: collision with root package name */
    private float f1395o;

    /* renamed from: p, reason: collision with root package name */
    protected View[] f1396p;

    public MotionHelper(Context context) {
        super(context);
        this.f1393m = false;
        this.f1394n = false;
    }

    @Override // androidx.constraintlayout.motion.widget.MotionLayout.i
    public void a(MotionLayout motionLayout, int i10, int i11, float f10) {
    }

    @Override // androidx.constraintlayout.motion.widget.MotionLayout.i
    public void b(MotionLayout motionLayout, int i10, int i11) {
    }

    @Override // androidx.constraintlayout.motion.widget.MotionLayout.i
    public void d(MotionLayout motionLayout, int i10, boolean z10, float f10) {
    }

    @Override // androidx.constraintlayout.motion.widget.MotionLayout.i
    public void e(MotionLayout motionLayout, int i10) {
    }

    public float getProgress() {
        return this.f1395o;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void m(AttributeSet attributeSet) {
        super.m(attributeSet);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.MotionHelper);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.MotionHelper_onShow) {
                    this.f1393m = obtainStyledAttributes.getBoolean(index, this.f1393m);
                } else if (index == R$styleable.MotionHelper_onHide) {
                    this.f1394n = obtainStyledAttributes.getBoolean(index, this.f1394n);
                }
            }
        }
    }

    public void setProgress(float f10) {
        this.f1395o = f10;
        int i10 = 0;
        if (this.f1817f > 0) {
            this.f1396p = l((ConstraintLayout) getParent());
            while (i10 < this.f1817f) {
                x(this.f1396p[i10], f10);
                i10++;
            }
            return;
        }
        ViewGroup viewGroup = (ViewGroup) getParent();
        int childCount = viewGroup.getChildCount();
        while (i10 < childCount) {
            View childAt = viewGroup.getChildAt(i10);
            if (!(childAt instanceof MotionHelper)) {
                x(childAt, f10);
            }
            i10++;
        }
    }

    public boolean v() {
        return this.f1394n;
    }

    public boolean w() {
        return this.f1393m;
    }

    public void x(View view, float f10) {
    }

    public MotionHelper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f1393m = false;
        this.f1394n = false;
        m(attributeSet);
    }

    public MotionHelper(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f1393m = false;
        this.f1394n = false;
        m(attributeSet);
    }
}
