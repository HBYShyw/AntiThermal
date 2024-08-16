package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.R$styleable;
import androidx.core.widget.NestedScrollView;
import org.xmlpull.v1.XmlPullParser;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: TouchResponse.java */
/* renamed from: androidx.constraintlayout.motion.widget.t, reason: use source file name */
/* loaded from: classes.dex */
public class TouchResponse {

    /* renamed from: v, reason: collision with root package name */
    private static final float[][] f1739v = {new float[]{0.5f, 0.0f}, new float[]{0.0f, 0.5f}, new float[]{1.0f, 0.5f}, new float[]{0.5f, 1.0f}, new float[]{0.5f, 0.5f}, new float[]{0.0f, 0.5f}, new float[]{1.0f, 0.5f}};

    /* renamed from: w, reason: collision with root package name */
    private static final float[][] f1740w = {new float[]{0.0f, -1.0f}, new float[]{0.0f, 1.0f}, new float[]{-1.0f, 0.0f}, new float[]{1.0f, 0.0f}, new float[]{-1.0f, 0.0f}, new float[]{1.0f, 0.0f}};

    /* renamed from: m, reason: collision with root package name */
    private float f1753m;

    /* renamed from: n, reason: collision with root package name */
    private float f1754n;

    /* renamed from: o, reason: collision with root package name */
    private final MotionLayout f1755o;

    /* renamed from: a, reason: collision with root package name */
    private int f1741a = 0;

    /* renamed from: b, reason: collision with root package name */
    private int f1742b = 0;

    /* renamed from: c, reason: collision with root package name */
    private int f1743c = 0;

    /* renamed from: d, reason: collision with root package name */
    private int f1744d = -1;

    /* renamed from: e, reason: collision with root package name */
    private int f1745e = -1;

    /* renamed from: f, reason: collision with root package name */
    private int f1746f = -1;

    /* renamed from: g, reason: collision with root package name */
    private float f1747g = 0.5f;

    /* renamed from: h, reason: collision with root package name */
    private float f1748h = 0.5f;

    /* renamed from: i, reason: collision with root package name */
    private float f1749i = 0.0f;

    /* renamed from: j, reason: collision with root package name */
    private float f1750j = 1.0f;

    /* renamed from: k, reason: collision with root package name */
    private boolean f1751k = false;

    /* renamed from: l, reason: collision with root package name */
    private float[] f1752l = new float[2];

    /* renamed from: p, reason: collision with root package name */
    private float f1756p = 4.0f;

    /* renamed from: q, reason: collision with root package name */
    private float f1757q = 1.2f;

    /* renamed from: r, reason: collision with root package name */
    private boolean f1758r = true;

    /* renamed from: s, reason: collision with root package name */
    private float f1759s = 1.0f;

    /* renamed from: t, reason: collision with root package name */
    private int f1760t = 0;

    /* renamed from: u, reason: collision with root package name */
    private float f1761u = 10.0f;

    /* compiled from: TouchResponse.java */
    /* renamed from: androidx.constraintlayout.motion.widget.t$a */
    /* loaded from: classes.dex */
    class a implements View.OnTouchListener {
        a() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    }

    /* compiled from: TouchResponse.java */
    /* renamed from: androidx.constraintlayout.motion.widget.t$b */
    /* loaded from: classes.dex */
    class b implements NestedScrollView.c {
        b() {
        }

        @Override // androidx.core.widget.NestedScrollView.c
        public void a(NestedScrollView nestedScrollView, int i10, int i11, int i12, int i13) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TouchResponse(Context context, MotionLayout motionLayout, XmlPullParser xmlPullParser) {
        this.f1755o = motionLayout;
        c(context, Xml.asAttributeSet(xmlPullParser));
    }

    private void b(TypedArray typedArray) {
        int indexCount = typedArray.getIndexCount();
        for (int i10 = 0; i10 < indexCount; i10++) {
            int index = typedArray.getIndex(i10);
            if (index == R$styleable.OnSwipe_touchAnchorId) {
                this.f1744d = typedArray.getResourceId(index, this.f1744d);
            } else if (index == R$styleable.OnSwipe_touchAnchorSide) {
                int i11 = typedArray.getInt(index, this.f1741a);
                this.f1741a = i11;
                float[][] fArr = f1739v;
                this.f1748h = fArr[i11][0];
                this.f1747g = fArr[i11][1];
            } else if (index == R$styleable.OnSwipe_dragDirection) {
                int i12 = typedArray.getInt(index, this.f1742b);
                this.f1742b = i12;
                float[][] fArr2 = f1740w;
                this.f1749i = fArr2[i12][0];
                this.f1750j = fArr2[i12][1];
            } else if (index == R$styleable.OnSwipe_maxVelocity) {
                this.f1756p = typedArray.getFloat(index, this.f1756p);
            } else if (index == R$styleable.OnSwipe_maxAcceleration) {
                this.f1757q = typedArray.getFloat(index, this.f1757q);
            } else if (index == R$styleable.OnSwipe_moveWhenScrollAtTop) {
                this.f1758r = typedArray.getBoolean(index, this.f1758r);
            } else if (index == R$styleable.OnSwipe_dragScale) {
                this.f1759s = typedArray.getFloat(index, this.f1759s);
            } else if (index == R$styleable.OnSwipe_dragThreshold) {
                this.f1761u = typedArray.getFloat(index, this.f1761u);
            } else if (index == R$styleable.OnSwipe_touchRegionId) {
                this.f1745e = typedArray.getResourceId(index, this.f1745e);
            } else if (index == R$styleable.OnSwipe_onTouchUp) {
                this.f1743c = typedArray.getInt(index, this.f1743c);
            } else if (index == R$styleable.OnSwipe_nestedScrollFlags) {
                this.f1760t = typedArray.getInteger(index, 0);
            } else if (index == R$styleable.OnSwipe_limitBoundsTo) {
                this.f1746f = typedArray.getResourceId(index, 0);
            }
        }
    }

    private void c(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.OnSwipe);
        b(obtainStyledAttributes);
        obtainStyledAttributes.recycle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float a(float f10, float f11) {
        return (f10 * this.f1749i) + (f11 * this.f1750j);
    }

    public int d() {
        return this.f1760t;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RectF e(ViewGroup viewGroup, RectF rectF) {
        View findViewById;
        int i10 = this.f1746f;
        if (i10 == -1 || (findViewById = viewGroup.findViewById(i10)) == null) {
            return null;
        }
        rectF.set(findViewById.getLeft(), findViewById.getTop(), findViewById.getRight(), findViewById.getBottom());
        return rectF;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float f() {
        return this.f1757q;
    }

    public float g() {
        return this.f1756p;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean h() {
        return this.f1758r;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float i(float f10, float f11) {
        this.f1755o.i0(this.f1744d, this.f1755o.getProgress(), this.f1748h, this.f1747g, this.f1752l);
        float f12 = this.f1749i;
        if (f12 != 0.0f) {
            float[] fArr = this.f1752l;
            if (fArr[0] == 0.0f) {
                fArr[0] = 1.0E-7f;
            }
            return (f10 * f12) / fArr[0];
        }
        float[] fArr2 = this.f1752l;
        if (fArr2[1] == 0.0f) {
            fArr2[1] = 1.0E-7f;
        }
        return (f11 * this.f1750j) / fArr2[1];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RectF j(ViewGroup viewGroup, RectF rectF) {
        View findViewById;
        int i10 = this.f1745e;
        if (i10 == -1 || (findViewById = viewGroup.findViewById(i10)) == null) {
            return null;
        }
        rectF.set(findViewById.getLeft(), findViewById.getTop(), findViewById.getRight(), findViewById.getBottom());
        return rectF;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int k() {
        return this.f1745e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(MotionEvent motionEvent, MotionLayout.f fVar, int i10, MotionScene motionScene) {
        float f10;
        int i11;
        float f11;
        fVar.b(motionEvent);
        int action = motionEvent.getAction();
        if (action == 0) {
            this.f1753m = motionEvent.getRawX();
            this.f1754n = motionEvent.getRawY();
            this.f1751k = false;
            return;
        }
        if (action == 1) {
            this.f1751k = false;
            fVar.e(1000);
            float d10 = fVar.d();
            float c10 = fVar.c();
            float progress = this.f1755o.getProgress();
            int i12 = this.f1744d;
            if (i12 != -1) {
                this.f1755o.i0(i12, progress, this.f1748h, this.f1747g, this.f1752l);
            } else {
                float min = Math.min(this.f1755o.getWidth(), this.f1755o.getHeight());
                float[] fArr = this.f1752l;
                fArr[1] = this.f1750j * min;
                fArr[0] = min * this.f1749i;
            }
            float f12 = this.f1749i;
            float[] fArr2 = this.f1752l;
            float f13 = fArr2[0];
            float f14 = fArr2[1];
            if (f12 != 0.0f) {
                f10 = d10 / fArr2[0];
            } else {
                f10 = c10 / fArr2[1];
            }
            float f15 = !Float.isNaN(f10) ? (f10 / 3.0f) + progress : progress;
            if (f15 == 0.0f || f15 == 1.0f || (i11 = this.f1743c) == 3) {
                if (0.0f >= f15 || 1.0f <= f15) {
                    this.f1755o.setState(MotionLayout.j.FINISHED);
                    return;
                }
                return;
            }
            this.f1755o.x0(i11, ((double) f15) < 0.5d ? 0.0f : 1.0f, f10);
            if (0.0f >= progress || 1.0f <= progress) {
                this.f1755o.setState(MotionLayout.j.FINISHED);
                return;
            }
            return;
        }
        if (action != 2) {
            return;
        }
        float rawY = motionEvent.getRawY() - this.f1754n;
        float rawX = motionEvent.getRawX() - this.f1753m;
        if (Math.abs((this.f1749i * rawX) + (this.f1750j * rawY)) > this.f1761u || this.f1751k) {
            float progress2 = this.f1755o.getProgress();
            if (!this.f1751k) {
                this.f1751k = true;
                this.f1755o.setProgress(progress2);
            }
            int i13 = this.f1744d;
            if (i13 != -1) {
                this.f1755o.i0(i13, progress2, this.f1748h, this.f1747g, this.f1752l);
            } else {
                float min2 = Math.min(this.f1755o.getWidth(), this.f1755o.getHeight());
                float[] fArr3 = this.f1752l;
                fArr3[1] = this.f1750j * min2;
                fArr3[0] = min2 * this.f1749i;
            }
            float f16 = this.f1749i;
            float[] fArr4 = this.f1752l;
            if (Math.abs(((f16 * fArr4[0]) + (this.f1750j * fArr4[1])) * this.f1759s) < 0.01d) {
                float[] fArr5 = this.f1752l;
                fArr5[0] = 0.01f;
                fArr5[1] = 0.01f;
            }
            if (this.f1749i != 0.0f) {
                f11 = rawX / this.f1752l[0];
            } else {
                f11 = rawY / this.f1752l[1];
            }
            float max = Math.max(Math.min(progress2 + f11, 1.0f), 0.0f);
            if (max != this.f1755o.getProgress()) {
                this.f1755o.setProgress(max);
                fVar.e(1000);
                this.f1755o.D = this.f1749i != 0.0f ? fVar.d() / this.f1752l[0] : fVar.c() / this.f1752l[1];
            } else {
                this.f1755o.D = 0.0f;
            }
            this.f1753m = motionEvent.getRawX();
            this.f1754n = motionEvent.getRawY();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(float f10, float f11) {
        float f12;
        float progress = this.f1755o.getProgress();
        if (!this.f1751k) {
            this.f1751k = true;
            this.f1755o.setProgress(progress);
        }
        this.f1755o.i0(this.f1744d, progress, this.f1748h, this.f1747g, this.f1752l);
        float f13 = this.f1749i;
        float[] fArr = this.f1752l;
        if (Math.abs((f13 * fArr[0]) + (this.f1750j * fArr[1])) < 0.01d) {
            float[] fArr2 = this.f1752l;
            fArr2[0] = 0.01f;
            fArr2[1] = 0.01f;
        }
        float f14 = this.f1749i;
        if (f14 != 0.0f) {
            f12 = (f10 * f14) / this.f1752l[0];
        } else {
            f12 = (f11 * this.f1750j) / this.f1752l[1];
        }
        float max = Math.max(Math.min(progress + f12, 1.0f), 0.0f);
        if (max != this.f1755o.getProgress()) {
            this.f1755o.setProgress(max);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void n(float f10, float f11) {
        float f12;
        this.f1751k = false;
        float progress = this.f1755o.getProgress();
        this.f1755o.i0(this.f1744d, progress, this.f1748h, this.f1747g, this.f1752l);
        float f13 = this.f1749i;
        float[] fArr = this.f1752l;
        float f14 = fArr[0];
        float f15 = this.f1750j;
        float f16 = fArr[1];
        if (f13 != 0.0f) {
            f12 = (f10 * f13) / fArr[0];
        } else {
            f12 = (f11 * f15) / fArr[1];
        }
        if (!Float.isNaN(f12)) {
            progress += f12 / 3.0f;
        }
        if (progress != 0.0f) {
            boolean z10 = progress != 1.0f;
            int i10 = this.f1743c;
            if ((i10 != 3) && z10) {
                this.f1755o.x0(i10, ((double) progress) >= 0.5d ? 1.0f : 0.0f, f12);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(float f10, float f11) {
        this.f1753m = f10;
        this.f1754n = f11;
    }

    public void p(boolean z10) {
        if (z10) {
            float[][] fArr = f1740w;
            fArr[4] = fArr[3];
            fArr[5] = fArr[2];
            float[][] fArr2 = f1739v;
            fArr2[5] = fArr2[2];
            fArr2[6] = fArr2[1];
        } else {
            float[][] fArr3 = f1740w;
            fArr3[4] = fArr3[2];
            fArr3[5] = fArr3[3];
            float[][] fArr4 = f1739v;
            fArr4[5] = fArr4[1];
            fArr4[6] = fArr4[2];
        }
        float[][] fArr5 = f1739v;
        int i10 = this.f1741a;
        this.f1748h = fArr5[i10][0];
        this.f1747g = fArr5[i10][1];
        float[][] fArr6 = f1740w;
        int i11 = this.f1742b;
        this.f1749i = fArr6[i11][0];
        this.f1750j = fArr6[i11][1];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(float f10, float f11) {
        this.f1753m = f10;
        this.f1754n = f11;
        this.f1751k = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r() {
        View view;
        int i10 = this.f1744d;
        if (i10 != -1) {
            view = this.f1755o.findViewById(i10);
            if (view == null) {
                Log.e("TouchResponse", "cannot find TouchAnchorId @id/" + androidx.constraintlayout.motion.widget.a.b(this.f1755o.getContext(), this.f1744d));
            }
        } else {
            view = null;
        }
        if (view instanceof NestedScrollView) {
            NestedScrollView nestedScrollView = (NestedScrollView) view;
            nestedScrollView.setOnTouchListener(new a());
            nestedScrollView.setOnScrollChangeListener(new b());
        }
    }

    public String toString() {
        return this.f1749i + " , " + this.f1750j;
    }
}
