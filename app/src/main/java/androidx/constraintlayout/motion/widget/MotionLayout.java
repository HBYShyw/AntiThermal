package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintLayoutStates;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.constraintlayout.widget.R$styleable;
import androidx.constraintlayout.widget.StateSet;
import androidx.core.view.NestedScrollingParent3;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import k.StopLogic;
import m.ConstraintWidget;
import m.ConstraintWidgetContainer;
import m.Helper;
import m.HelperWidget;

/* loaded from: classes.dex */
public class MotionLayout extends ConstraintLayout implements NestedScrollingParent3 {
    public static boolean R0;
    protected boolean A0;
    MotionScene B;
    int B0;
    Interpolator C;
    int C0;
    float D;
    int D0;
    private int E;
    int E0;
    int F;
    int F0;
    private int G;
    int G0;
    private int H;
    float H0;
    private int I;
    private KeyCache I0;
    private boolean J;
    private boolean J0;
    HashMap<View, MotionController> K;
    private h K0;
    private long L;
    j L0;
    private float M;
    e M0;
    float N;
    private boolean N0;
    float O;
    private RectF O0;
    private long P;
    private View P0;
    float Q;
    ArrayList<Integer> Q0;
    private boolean R;
    boolean S;
    boolean T;
    private i U;
    private float V;
    private float W;

    /* renamed from: a0, reason: collision with root package name */
    int f1397a0;

    /* renamed from: b0, reason: collision with root package name */
    d f1398b0;

    /* renamed from: c0, reason: collision with root package name */
    private boolean f1399c0;

    /* renamed from: d0, reason: collision with root package name */
    private StopLogic f1400d0;

    /* renamed from: e0, reason: collision with root package name */
    private c f1401e0;

    /* renamed from: f0, reason: collision with root package name */
    private DesignTool f1402f0;

    /* renamed from: g0, reason: collision with root package name */
    boolean f1403g0;

    /* renamed from: h0, reason: collision with root package name */
    int f1404h0;

    /* renamed from: i0, reason: collision with root package name */
    int f1405i0;

    /* renamed from: j0, reason: collision with root package name */
    int f1406j0;

    /* renamed from: k0, reason: collision with root package name */
    int f1407k0;

    /* renamed from: l0, reason: collision with root package name */
    boolean f1408l0;

    /* renamed from: m0, reason: collision with root package name */
    float f1409m0;

    /* renamed from: n0, reason: collision with root package name */
    float f1410n0;

    /* renamed from: o0, reason: collision with root package name */
    long f1411o0;

    /* renamed from: p0, reason: collision with root package name */
    float f1412p0;

    /* renamed from: q0, reason: collision with root package name */
    private boolean f1413q0;

    /* renamed from: r0, reason: collision with root package name */
    private ArrayList<MotionHelper> f1414r0;

    /* renamed from: s0, reason: collision with root package name */
    private ArrayList<MotionHelper> f1415s0;

    /* renamed from: t0, reason: collision with root package name */
    private ArrayList<i> f1416t0;

    /* renamed from: u0, reason: collision with root package name */
    private int f1417u0;

    /* renamed from: v0, reason: collision with root package name */
    private long f1418v0;

    /* renamed from: w0, reason: collision with root package name */
    private float f1419w0;

    /* renamed from: x0, reason: collision with root package name */
    private int f1420x0;

    /* renamed from: y0, reason: collision with root package name */
    private float f1421y0;

    /* renamed from: z0, reason: collision with root package name */
    boolean f1422z0;

    /* loaded from: classes.dex */
    class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ View f1423e;

        a(View view) {
            this.f1423e = view;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f1423e.setNestedScrollingEnabled(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static /* synthetic */ class b {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f1425a;

        static {
            int[] iArr = new int[j.values().length];
            f1425a = iArr;
            try {
                iArr[j.UNDEFINED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f1425a[j.SETUP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f1425a[j.MOVING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f1425a[j.FINISHED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* loaded from: classes.dex */
    class c extends MotionInterpolator {

        /* renamed from: a, reason: collision with root package name */
        float f1426a = 0.0f;

        /* renamed from: b, reason: collision with root package name */
        float f1427b = 0.0f;

        /* renamed from: c, reason: collision with root package name */
        float f1428c;

        c() {
        }

        @Override // androidx.constraintlayout.motion.widget.MotionInterpolator
        public float a() {
            return MotionLayout.this.D;
        }

        public void b(float f10, float f11, float f12) {
            this.f1426a = f10;
            this.f1427b = f11;
            this.f1428c = f12;
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f10) {
            float f11;
            float f12;
            float f13 = this.f1426a;
            if (f13 > 0.0f) {
                float f14 = this.f1428c;
                if (f13 / f14 < f10) {
                    f10 = f13 / f14;
                }
                MotionLayout.this.D = f13 - (f14 * f10);
                f11 = (f13 * f10) - (((f14 * f10) * f10) / 2.0f);
                f12 = this.f1427b;
            } else {
                float f15 = this.f1428c;
                if ((-f13) / f15 < f10) {
                    f10 = (-f13) / f15;
                }
                MotionLayout.this.D = (f15 * f10) + f13;
                f11 = (f13 * f10) + (((f15 * f10) * f10) / 2.0f);
                f12 = this.f1427b;
            }
            return f11 + f12;
        }
    }

    /* loaded from: classes.dex */
    private class d {

        /* renamed from: a, reason: collision with root package name */
        float[] f1430a;

        /* renamed from: b, reason: collision with root package name */
        int[] f1431b;

        /* renamed from: c, reason: collision with root package name */
        float[] f1432c;

        /* renamed from: d, reason: collision with root package name */
        Path f1433d;

        /* renamed from: e, reason: collision with root package name */
        Paint f1434e;

        /* renamed from: f, reason: collision with root package name */
        Paint f1435f;

        /* renamed from: g, reason: collision with root package name */
        Paint f1436g;

        /* renamed from: h, reason: collision with root package name */
        Paint f1437h;

        /* renamed from: i, reason: collision with root package name */
        Paint f1438i;

        /* renamed from: j, reason: collision with root package name */
        private float[] f1439j;

        /* renamed from: p, reason: collision with root package name */
        DashPathEffect f1445p;

        /* renamed from: q, reason: collision with root package name */
        int f1446q;

        /* renamed from: t, reason: collision with root package name */
        int f1449t;

        /* renamed from: k, reason: collision with root package name */
        final int f1440k = -21965;

        /* renamed from: l, reason: collision with root package name */
        final int f1441l = -2067046;

        /* renamed from: m, reason: collision with root package name */
        final int f1442m = -13391360;

        /* renamed from: n, reason: collision with root package name */
        final int f1443n = 1996488704;

        /* renamed from: o, reason: collision with root package name */
        final int f1444o = 10;

        /* renamed from: r, reason: collision with root package name */
        Rect f1447r = new Rect();

        /* renamed from: s, reason: collision with root package name */
        boolean f1448s = false;

        public d() {
            this.f1449t = 1;
            Paint paint = new Paint();
            this.f1434e = paint;
            paint.setAntiAlias(true);
            this.f1434e.setColor(-21965);
            this.f1434e.setStrokeWidth(2.0f);
            this.f1434e.setStyle(Paint.Style.STROKE);
            Paint paint2 = new Paint();
            this.f1435f = paint2;
            paint2.setAntiAlias(true);
            this.f1435f.setColor(-2067046);
            this.f1435f.setStrokeWidth(2.0f);
            this.f1435f.setStyle(Paint.Style.STROKE);
            Paint paint3 = new Paint();
            this.f1436g = paint3;
            paint3.setAntiAlias(true);
            this.f1436g.setColor(-13391360);
            this.f1436g.setStrokeWidth(2.0f);
            this.f1436g.setStyle(Paint.Style.STROKE);
            Paint paint4 = new Paint();
            this.f1437h = paint4;
            paint4.setAntiAlias(true);
            this.f1437h.setColor(-13391360);
            this.f1437h.setTextSize(MotionLayout.this.getContext().getResources().getDisplayMetrics().density * 12.0f);
            this.f1439j = new float[8];
            Paint paint5 = new Paint();
            this.f1438i = paint5;
            paint5.setAntiAlias(true);
            DashPathEffect dashPathEffect = new DashPathEffect(new float[]{4.0f, 8.0f}, 0.0f);
            this.f1445p = dashPathEffect;
            this.f1436g.setPathEffect(dashPathEffect);
            this.f1432c = new float[100];
            this.f1431b = new int[50];
            if (this.f1448s) {
                this.f1434e.setStrokeWidth(8.0f);
                this.f1438i.setStrokeWidth(8.0f);
                this.f1435f.setStrokeWidth(8.0f);
                this.f1449t = 4;
            }
        }

        private void c(Canvas canvas) {
            canvas.drawLines(this.f1430a, this.f1434e);
        }

        private void d(Canvas canvas) {
            boolean z10 = false;
            boolean z11 = false;
            for (int i10 = 0; i10 < this.f1446q; i10++) {
                int[] iArr = this.f1431b;
                if (iArr[i10] == 1) {
                    z10 = true;
                }
                if (iArr[i10] == 2) {
                    z11 = true;
                }
            }
            if (z10) {
                g(canvas);
            }
            if (z11) {
                e(canvas);
            }
        }

        private void e(Canvas canvas) {
            float[] fArr = this.f1430a;
            float f10 = fArr[0];
            float f11 = fArr[1];
            float f12 = fArr[fArr.length - 2];
            float f13 = fArr[fArr.length - 1];
            canvas.drawLine(Math.min(f10, f12), Math.max(f11, f13), Math.max(f10, f12), Math.max(f11, f13), this.f1436g);
            canvas.drawLine(Math.min(f10, f12), Math.min(f11, f13), Math.min(f10, f12), Math.max(f11, f13), this.f1436g);
        }

        private void f(Canvas canvas, float f10, float f11) {
            float[] fArr = this.f1430a;
            float f12 = fArr[0];
            float f13 = fArr[1];
            float f14 = fArr[fArr.length - 2];
            float f15 = fArr[fArr.length - 1];
            float min = Math.min(f12, f14);
            float max = Math.max(f13, f15);
            float min2 = f10 - Math.min(f12, f14);
            float max2 = Math.max(f13, f15) - f11;
            String str = "" + (((int) (((min2 * 100.0f) / Math.abs(f14 - f12)) + 0.5d)) / 100.0f);
            l(str, this.f1437h);
            canvas.drawText(str, ((min2 / 2.0f) - (this.f1447r.width() / 2)) + min, f11 - 20.0f, this.f1437h);
            canvas.drawLine(f10, f11, Math.min(f12, f14), f11, this.f1436g);
            String str2 = "" + (((int) (((max2 * 100.0f) / Math.abs(f15 - f13)) + 0.5d)) / 100.0f);
            l(str2, this.f1437h);
            canvas.drawText(str2, f10 + 5.0f, max - ((max2 / 2.0f) - (this.f1447r.height() / 2)), this.f1437h);
            canvas.drawLine(f10, f11, f10, Math.max(f13, f15), this.f1436g);
        }

        private void g(Canvas canvas) {
            float[] fArr = this.f1430a;
            canvas.drawLine(fArr[0], fArr[1], fArr[fArr.length - 2], fArr[fArr.length - 1], this.f1436g);
        }

        private void h(Canvas canvas, float f10, float f11) {
            float[] fArr = this.f1430a;
            float f12 = fArr[0];
            float f13 = fArr[1];
            float f14 = fArr[fArr.length - 2];
            float f15 = fArr[fArr.length - 1];
            float hypot = (float) Math.hypot(f12 - f14, f13 - f15);
            float f16 = f14 - f12;
            float f17 = f15 - f13;
            float f18 = (((f10 - f12) * f16) + ((f11 - f13) * f17)) / (hypot * hypot);
            float f19 = f12 + (f16 * f18);
            float f20 = f13 + (f18 * f17);
            Path path = new Path();
            path.moveTo(f10, f11);
            path.lineTo(f19, f20);
            float hypot2 = (float) Math.hypot(f19 - f10, f20 - f11);
            String str = "" + (((int) ((hypot2 * 100.0f) / hypot)) / 100.0f);
            l(str, this.f1437h);
            canvas.drawTextOnPath(str, path, (hypot2 / 2.0f) - (this.f1447r.width() / 2), -20.0f, this.f1437h);
            canvas.drawLine(f10, f11, f19, f20, this.f1436g);
        }

        private void i(Canvas canvas, float f10, float f11, int i10, int i11) {
            String str = "" + (((int) ((((f10 - (i10 / 2)) * 100.0f) / (MotionLayout.this.getWidth() - i10)) + 0.5d)) / 100.0f);
            l(str, this.f1437h);
            canvas.drawText(str, ((f10 / 2.0f) - (this.f1447r.width() / 2)) + 0.0f, f11 - 20.0f, this.f1437h);
            canvas.drawLine(f10, f11, Math.min(0.0f, 1.0f), f11, this.f1436g);
            String str2 = "" + (((int) ((((f11 - (i11 / 2)) * 100.0f) / (MotionLayout.this.getHeight() - i11)) + 0.5d)) / 100.0f);
            l(str2, this.f1437h);
            canvas.drawText(str2, f10 + 5.0f, 0.0f - ((f11 / 2.0f) - (this.f1447r.height() / 2)), this.f1437h);
            canvas.drawLine(f10, f11, f10, Math.max(0.0f, 1.0f), this.f1436g);
        }

        private void j(Canvas canvas, MotionController motionController) {
            this.f1433d.reset();
            for (int i10 = 0; i10 <= 50; i10++) {
                motionController.e(i10 / 50, this.f1439j, 0);
                Path path = this.f1433d;
                float[] fArr = this.f1439j;
                path.moveTo(fArr[0], fArr[1]);
                Path path2 = this.f1433d;
                float[] fArr2 = this.f1439j;
                path2.lineTo(fArr2[2], fArr2[3]);
                Path path3 = this.f1433d;
                float[] fArr3 = this.f1439j;
                path3.lineTo(fArr3[4], fArr3[5]);
                Path path4 = this.f1433d;
                float[] fArr4 = this.f1439j;
                path4.lineTo(fArr4[6], fArr4[7]);
                this.f1433d.close();
            }
            this.f1434e.setColor(1140850688);
            canvas.translate(2.0f, 2.0f);
            canvas.drawPath(this.f1433d, this.f1434e);
            canvas.translate(-2.0f, -2.0f);
            this.f1434e.setColor(-65536);
            canvas.drawPath(this.f1433d, this.f1434e);
        }

        private void k(Canvas canvas, int i10, int i11, MotionController motionController) {
            int i12;
            int i13;
            int i14;
            float f10;
            float f11;
            View view = motionController.f1629a;
            if (view != null) {
                i12 = view.getWidth();
                i13 = motionController.f1629a.getHeight();
            } else {
                i12 = 0;
                i13 = 0;
            }
            for (int i15 = 1; i15 < i11 - 1; i15++) {
                if (i10 != 4 || this.f1431b[i15 - 1] != 0) {
                    float[] fArr = this.f1432c;
                    int i16 = i15 * 2;
                    float f12 = fArr[i16];
                    float f13 = fArr[i16 + 1];
                    this.f1433d.reset();
                    this.f1433d.moveTo(f12, f13 + 10.0f);
                    this.f1433d.lineTo(f12 + 10.0f, f13);
                    this.f1433d.lineTo(f12, f13 - 10.0f);
                    this.f1433d.lineTo(f12 - 10.0f, f13);
                    this.f1433d.close();
                    int i17 = i15 - 1;
                    motionController.k(i17);
                    if (i10 == 4) {
                        int[] iArr = this.f1431b;
                        if (iArr[i17] == 1) {
                            h(canvas, f12 - 0.0f, f13 - 0.0f);
                        } else if (iArr[i17] == 2) {
                            f(canvas, f12 - 0.0f, f13 - 0.0f);
                        } else if (iArr[i17] == 3) {
                            i14 = 3;
                            f10 = f13;
                            f11 = f12;
                            i(canvas, f12 - 0.0f, f13 - 0.0f, i12, i13);
                            canvas.drawPath(this.f1433d, this.f1438i);
                        }
                        i14 = 3;
                        f10 = f13;
                        f11 = f12;
                        canvas.drawPath(this.f1433d, this.f1438i);
                    } else {
                        i14 = 3;
                        f10 = f13;
                        f11 = f12;
                    }
                    if (i10 == 2) {
                        h(canvas, f11 - 0.0f, f10 - 0.0f);
                    }
                    if (i10 == i14) {
                        f(canvas, f11 - 0.0f, f10 - 0.0f);
                    }
                    if (i10 == 6) {
                        i(canvas, f11 - 0.0f, f10 - 0.0f, i12, i13);
                    }
                    canvas.drawPath(this.f1433d, this.f1438i);
                }
            }
            float[] fArr2 = this.f1430a;
            if (fArr2.length > 1) {
                canvas.drawCircle(fArr2[0], fArr2[1], 8.0f, this.f1435f);
                float[] fArr3 = this.f1430a;
                canvas.drawCircle(fArr3[fArr3.length - 2], fArr3[fArr3.length - 1], 8.0f, this.f1435f);
            }
        }

        public void a(Canvas canvas, HashMap<View, MotionController> hashMap, int i10, int i11) {
            if (hashMap == null || hashMap.size() == 0) {
                return;
            }
            canvas.save();
            if (!MotionLayout.this.isInEditMode() && (i11 & 1) == 2) {
                String str = MotionLayout.this.getContext().getResources().getResourceName(MotionLayout.this.G) + ":" + MotionLayout.this.getProgress();
                canvas.drawText(str, 10.0f, MotionLayout.this.getHeight() - 30, this.f1437h);
                canvas.drawText(str, 11.0f, MotionLayout.this.getHeight() - 29, this.f1434e);
            }
            for (MotionController motionController : hashMap.values()) {
                int h10 = motionController.h();
                if (i11 > 0 && h10 == 0) {
                    h10 = 1;
                }
                if (h10 != 0) {
                    this.f1446q = motionController.c(this.f1432c, this.f1431b);
                    if (h10 >= 1) {
                        int i12 = i10 / 16;
                        float[] fArr = this.f1430a;
                        if (fArr == null || fArr.length != i12 * 2) {
                            this.f1430a = new float[i12 * 2];
                            this.f1433d = new Path();
                        }
                        int i13 = this.f1449t;
                        canvas.translate(i13, i13);
                        this.f1434e.setColor(1996488704);
                        this.f1438i.setColor(1996488704);
                        this.f1435f.setColor(1996488704);
                        this.f1436g.setColor(1996488704);
                        motionController.d(this.f1430a, i12);
                        b(canvas, h10, this.f1446q, motionController);
                        this.f1434e.setColor(-21965);
                        this.f1435f.setColor(-2067046);
                        this.f1438i.setColor(-2067046);
                        this.f1436g.setColor(-13391360);
                        int i14 = this.f1449t;
                        canvas.translate(-i14, -i14);
                        b(canvas, h10, this.f1446q, motionController);
                        if (h10 == 5) {
                            j(canvas, motionController);
                        }
                    }
                }
            }
            canvas.restore();
        }

        public void b(Canvas canvas, int i10, int i11, MotionController motionController) {
            if (i10 == 4) {
                d(canvas);
            }
            if (i10 == 2) {
                g(canvas);
            }
            if (i10 == 3) {
                e(canvas);
            }
            c(canvas);
            k(canvas, i10, i11, motionController);
        }

        void l(String str, Paint paint) {
            paint.getTextBounds(str, 0, str.length(), this.f1447r);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e {

        /* renamed from: a, reason: collision with root package name */
        ConstraintWidgetContainer f1451a = new ConstraintWidgetContainer();

        /* renamed from: b, reason: collision with root package name */
        ConstraintWidgetContainer f1452b = new ConstraintWidgetContainer();

        /* renamed from: c, reason: collision with root package name */
        ConstraintSet f1453c = null;

        /* renamed from: d, reason: collision with root package name */
        ConstraintSet f1454d = null;

        /* renamed from: e, reason: collision with root package name */
        int f1455e;

        /* renamed from: f, reason: collision with root package name */
        int f1456f;

        e() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void i(ConstraintWidgetContainer constraintWidgetContainer, ConstraintSet constraintSet) {
            SparseArray<ConstraintWidget> sparseArray = new SparseArray<>();
            Constraints.LayoutParams layoutParams = new Constraints.LayoutParams(-2, -2);
            sparseArray.clear();
            sparseArray.put(0, constraintWidgetContainer);
            sparseArray.put(MotionLayout.this.getId(), constraintWidgetContainer);
            Iterator<ConstraintWidget> it = constraintWidgetContainer.L0().iterator();
            while (it.hasNext()) {
                ConstraintWidget next = it.next();
                sparseArray.put(((View) next.r()).getId(), next);
            }
            Iterator<ConstraintWidget> it2 = constraintWidgetContainer.L0().iterator();
            while (it2.hasNext()) {
                ConstraintWidget next2 = it2.next();
                View view = (View) next2.r();
                constraintSet.g(view.getId(), layoutParams);
                next2.F0(constraintSet.w(view.getId()));
                next2.i0(constraintSet.r(view.getId()));
                if (view instanceof ConstraintHelper) {
                    constraintSet.e((ConstraintHelper) view, next2, layoutParams, sparseArray);
                    if (view instanceof Barrier) {
                        ((Barrier) view).u();
                    }
                }
                layoutParams.resolveLayoutDirection(MotionLayout.this.getLayoutDirection());
                MotionLayout.this.g(false, view, next2, layoutParams, sparseArray);
                if (constraintSet.v(view.getId()) == 1) {
                    next2.E0(view.getVisibility());
                } else {
                    next2.E0(constraintSet.u(view.getId()));
                }
            }
            Iterator<ConstraintWidget> it3 = constraintWidgetContainer.L0().iterator();
            while (it3.hasNext()) {
                ConstraintWidget next3 = it3.next();
                if (next3 instanceof m.l) {
                    ConstraintHelper constraintHelper = (ConstraintHelper) next3.r();
                    Helper helper = (Helper) next3;
                    constraintHelper.t(constraintWidgetContainer, helper, sparseArray);
                    ((m.l) helper).M0();
                }
            }
        }

        public void a() {
            int childCount = MotionLayout.this.getChildCount();
            MotionLayout.this.K.clear();
            for (int i10 = 0; i10 < childCount; i10++) {
                View childAt = MotionLayout.this.getChildAt(i10);
                MotionLayout.this.K.put(childAt, new MotionController(childAt));
            }
            for (int i11 = 0; i11 < childCount; i11++) {
                View childAt2 = MotionLayout.this.getChildAt(i11);
                MotionController motionController = MotionLayout.this.K.get(childAt2);
                if (motionController != null) {
                    if (this.f1453c != null) {
                        ConstraintWidget c10 = c(this.f1451a, childAt2);
                        if (c10 != null) {
                            motionController.t(c10, this.f1453c);
                        } else if (MotionLayout.this.f1397a0 != 0) {
                            Log.e("MotionLayout", androidx.constraintlayout.motion.widget.a.a() + "no widget for  " + androidx.constraintlayout.motion.widget.a.c(childAt2) + " (" + childAt2.getClass().getName() + ")");
                        }
                    }
                    if (this.f1454d != null) {
                        ConstraintWidget c11 = c(this.f1452b, childAt2);
                        if (c11 != null) {
                            motionController.q(c11, this.f1454d);
                        } else if (MotionLayout.this.f1397a0 != 0) {
                            Log.e("MotionLayout", androidx.constraintlayout.motion.widget.a.a() + "no widget for  " + androidx.constraintlayout.motion.widget.a.c(childAt2) + " (" + childAt2.getClass().getName() + ")");
                        }
                    }
                }
            }
        }

        void b(ConstraintWidgetContainer constraintWidgetContainer, ConstraintWidgetContainer constraintWidgetContainer2) {
            ConstraintWidget constraintWidget;
            ArrayList<ConstraintWidget> L0 = constraintWidgetContainer.L0();
            HashMap<ConstraintWidget, ConstraintWidget> hashMap = new HashMap<>();
            hashMap.put(constraintWidgetContainer, constraintWidgetContainer2);
            constraintWidgetContainer2.L0().clear();
            constraintWidgetContainer2.l(constraintWidgetContainer, hashMap);
            Iterator<ConstraintWidget> it = L0.iterator();
            while (it.hasNext()) {
                ConstraintWidget next = it.next();
                if (next instanceof m.a) {
                    constraintWidget = new m.a();
                } else if (next instanceof m.h) {
                    constraintWidget = new m.h();
                } else if (next instanceof m.g) {
                    constraintWidget = new m.g();
                } else if (next instanceof Helper) {
                    constraintWidget = new HelperWidget();
                } else {
                    constraintWidget = new ConstraintWidget();
                }
                constraintWidgetContainer2.b(constraintWidget);
                hashMap.put(next, constraintWidget);
            }
            Iterator<ConstraintWidget> it2 = L0.iterator();
            while (it2.hasNext()) {
                ConstraintWidget next2 = it2.next();
                hashMap.get(next2).l(next2, hashMap);
            }
        }

        ConstraintWidget c(ConstraintWidgetContainer constraintWidgetContainer, View view) {
            if (constraintWidgetContainer.r() == view) {
                return constraintWidgetContainer;
            }
            ArrayList<ConstraintWidget> L0 = constraintWidgetContainer.L0();
            int size = L0.size();
            for (int i10 = 0; i10 < size; i10++) {
                ConstraintWidget constraintWidget = L0.get(i10);
                if (constraintWidget.r() == view) {
                    return constraintWidget;
                }
            }
            return null;
        }

        void d(ConstraintWidgetContainer constraintWidgetContainer, ConstraintSet constraintSet, ConstraintSet constraintSet2) {
            this.f1453c = constraintSet;
            this.f1454d = constraintSet2;
            this.f1451a = new ConstraintWidgetContainer();
            this.f1452b = new ConstraintWidgetContainer();
            this.f1451a.h1(((ConstraintLayout) MotionLayout.this).f1826g.W0());
            this.f1452b.h1(((ConstraintLayout) MotionLayout.this).f1826g.W0());
            this.f1451a.O0();
            this.f1452b.O0();
            b(((ConstraintLayout) MotionLayout.this).f1826g, this.f1451a);
            b(((ConstraintLayout) MotionLayout.this).f1826g, this.f1452b);
            if (MotionLayout.this.O > 0.5d) {
                if (constraintSet != null) {
                    i(this.f1451a, constraintSet);
                }
                i(this.f1452b, constraintSet2);
            } else {
                i(this.f1452b, constraintSet2);
                if (constraintSet != null) {
                    i(this.f1451a, constraintSet);
                }
            }
            this.f1451a.j1(MotionLayout.this.u());
            this.f1451a.l1();
            this.f1452b.j1(MotionLayout.this.u());
            this.f1452b.l1();
            ViewGroup.LayoutParams layoutParams = MotionLayout.this.getLayoutParams();
            if (layoutParams != null) {
                if (layoutParams.width == -2) {
                    ConstraintWidgetContainer constraintWidgetContainer2 = this.f1451a;
                    ConstraintWidget.b bVar = ConstraintWidget.b.WRAP_CONTENT;
                    constraintWidgetContainer2.m0(bVar);
                    this.f1452b.m0(bVar);
                }
                if (layoutParams.height == -2) {
                    ConstraintWidgetContainer constraintWidgetContainer3 = this.f1451a;
                    ConstraintWidget.b bVar2 = ConstraintWidget.b.WRAP_CONTENT;
                    constraintWidgetContainer3.B0(bVar2);
                    this.f1452b.B0(bVar2);
                }
            }
        }

        public boolean e(int i10, int i11) {
            return (i10 == this.f1455e && i11 == this.f1456f) ? false : true;
        }

        public void f(int i10, int i11) {
            int mode = View.MeasureSpec.getMode(i10);
            int mode2 = View.MeasureSpec.getMode(i11);
            MotionLayout motionLayout = MotionLayout.this;
            motionLayout.F0 = mode;
            motionLayout.G0 = mode2;
            int optimizationLevel = motionLayout.getOptimizationLevel();
            MotionLayout motionLayout2 = MotionLayout.this;
            if (motionLayout2.F == motionLayout2.getStartState()) {
                MotionLayout.this.y(this.f1452b, optimizationLevel, i10, i11);
                if (this.f1453c != null) {
                    MotionLayout.this.y(this.f1451a, optimizationLevel, i10, i11);
                }
            } else {
                if (this.f1453c != null) {
                    MotionLayout.this.y(this.f1451a, optimizationLevel, i10, i11);
                }
                MotionLayout.this.y(this.f1452b, optimizationLevel, i10, i11);
            }
            if (((MotionLayout.this.getParent() instanceof MotionLayout) && mode == 1073741824 && mode2 == 1073741824) ? false : true) {
                MotionLayout motionLayout3 = MotionLayout.this;
                motionLayout3.F0 = mode;
                motionLayout3.G0 = mode2;
                if (motionLayout3.F == motionLayout3.getStartState()) {
                    MotionLayout.this.y(this.f1452b, optimizationLevel, i10, i11);
                    if (this.f1453c != null) {
                        MotionLayout.this.y(this.f1451a, optimizationLevel, i10, i11);
                    }
                } else {
                    if (this.f1453c != null) {
                        MotionLayout.this.y(this.f1451a, optimizationLevel, i10, i11);
                    }
                    MotionLayout.this.y(this.f1452b, optimizationLevel, i10, i11);
                }
                MotionLayout.this.B0 = this.f1451a.Q();
                MotionLayout.this.C0 = this.f1451a.w();
                MotionLayout.this.D0 = this.f1452b.Q();
                MotionLayout.this.E0 = this.f1452b.w();
                MotionLayout motionLayout4 = MotionLayout.this;
                motionLayout4.A0 = (motionLayout4.B0 == motionLayout4.D0 && motionLayout4.C0 == motionLayout4.E0) ? false : true;
            }
            MotionLayout motionLayout5 = MotionLayout.this;
            int i12 = motionLayout5.B0;
            int i13 = motionLayout5.C0;
            int i14 = motionLayout5.F0;
            if (i14 == Integer.MIN_VALUE || i14 == 0) {
                i12 = (int) (i12 + (motionLayout5.H0 * (motionLayout5.D0 - i12)));
            }
            int i15 = motionLayout5.G0;
            if (i15 == Integer.MIN_VALUE || i15 == 0) {
                i13 = (int) (i13 + (motionLayout5.H0 * (motionLayout5.E0 - i13)));
            }
            MotionLayout.this.x(i10, i11, i12, i13, this.f1451a.d1() || this.f1452b.d1(), this.f1451a.b1() || this.f1452b.b1());
        }

        public void g() {
            f(MotionLayout.this.H, MotionLayout.this.I);
            MotionLayout.this.w0();
        }

        public void h(int i10, int i11) {
            this.f1455e = i10;
            this.f1456f = i11;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public interface f {
        void a();

        void b(MotionEvent motionEvent);

        float c();

        float d();

        void e(int i10);
    }

    /* loaded from: classes.dex */
    private static class g implements f {

        /* renamed from: b, reason: collision with root package name */
        private static g f1458b = new g();

        /* renamed from: a, reason: collision with root package name */
        VelocityTracker f1459a;

        private g() {
        }

        public static g f() {
            f1458b.f1459a = VelocityTracker.obtain();
            return f1458b;
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.f
        public void a() {
            this.f1459a.recycle();
            this.f1459a = null;
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.f
        public void b(MotionEvent motionEvent) {
            VelocityTracker velocityTracker = this.f1459a;
            if (velocityTracker != null) {
                velocityTracker.addMovement(motionEvent);
            }
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.f
        public float c() {
            return this.f1459a.getYVelocity();
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.f
        public float d() {
            return this.f1459a.getXVelocity();
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.f
        public void e(int i10) {
            this.f1459a.computeCurrentVelocity(i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class h {

        /* renamed from: a, reason: collision with root package name */
        float f1460a = Float.NaN;

        /* renamed from: b, reason: collision with root package name */
        float f1461b = Float.NaN;

        /* renamed from: c, reason: collision with root package name */
        int f1462c = -1;

        /* renamed from: d, reason: collision with root package name */
        int f1463d = -1;

        /* renamed from: e, reason: collision with root package name */
        final String f1464e = "motion.progress";

        /* renamed from: f, reason: collision with root package name */
        final String f1465f = "motion.velocity";

        /* renamed from: g, reason: collision with root package name */
        final String f1466g = "motion.StartState";

        /* renamed from: h, reason: collision with root package name */
        final String f1467h = "motion.EndState";

        h() {
        }

        void a() {
            int i10 = this.f1462c;
            if (i10 != -1 || this.f1463d != -1) {
                if (i10 == -1) {
                    MotionLayout.this.A0(this.f1463d);
                } else {
                    int i11 = this.f1463d;
                    if (i11 == -1) {
                        MotionLayout.this.u0(i10, -1, -1);
                    } else {
                        MotionLayout.this.v0(i10, i11);
                    }
                }
                MotionLayout.this.setState(j.SETUP);
            }
            if (Float.isNaN(this.f1461b)) {
                if (Float.isNaN(this.f1460a)) {
                    return;
                }
                MotionLayout.this.setProgress(this.f1460a);
            } else {
                MotionLayout.this.t0(this.f1460a, this.f1461b);
                this.f1460a = Float.NaN;
                this.f1461b = Float.NaN;
                this.f1462c = -1;
                this.f1463d = -1;
            }
        }

        public Bundle b() {
            Bundle bundle = new Bundle();
            bundle.putFloat("motion.progress", this.f1460a);
            bundle.putFloat("motion.velocity", this.f1461b);
            bundle.putInt("motion.StartState", this.f1462c);
            bundle.putInt("motion.EndState", this.f1463d);
            return bundle;
        }

        public void c() {
            this.f1463d = MotionLayout.this.G;
            this.f1462c = MotionLayout.this.E;
            this.f1461b = MotionLayout.this.getVelocity();
            this.f1460a = MotionLayout.this.getProgress();
        }

        public void d(int i10) {
            this.f1463d = i10;
        }

        public void e(float f10) {
            this.f1460a = f10;
        }

        public void f(int i10) {
            this.f1462c = i10;
        }

        public void g(Bundle bundle) {
            this.f1460a = bundle.getFloat("motion.progress");
            this.f1461b = bundle.getFloat("motion.velocity");
            this.f1462c = bundle.getInt("motion.StartState");
            this.f1463d = bundle.getInt("motion.EndState");
        }

        public void h(float f10) {
            this.f1461b = f10;
        }
    }

    /* loaded from: classes.dex */
    public interface i {
        void a(MotionLayout motionLayout, int i10, int i11, float f10);

        void b(MotionLayout motionLayout, int i10, int i11);

        void d(MotionLayout motionLayout, int i10, boolean z10, float f10);

        void e(MotionLayout motionLayout, int i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum j {
        UNDEFINED,
        SETUP,
        MOVING,
        FINISHED
    }

    public MotionLayout(Context context) {
        super(context);
        this.D = 0.0f;
        this.E = -1;
        this.F = -1;
        this.G = -1;
        this.H = 0;
        this.I = 0;
        this.J = true;
        this.K = new HashMap<>();
        this.L = 0L;
        this.M = 1.0f;
        this.N = 0.0f;
        this.O = 0.0f;
        this.Q = 0.0f;
        this.S = false;
        this.T = false;
        this.f1397a0 = 0;
        this.f1399c0 = false;
        this.f1400d0 = new StopLogic();
        this.f1401e0 = new c();
        this.f1403g0 = true;
        this.f1408l0 = false;
        this.f1413q0 = false;
        this.f1414r0 = null;
        this.f1415s0 = null;
        this.f1416t0 = null;
        this.f1417u0 = 0;
        this.f1418v0 = -1L;
        this.f1419w0 = 0.0f;
        this.f1420x0 = 0;
        this.f1421y0 = 0.0f;
        this.f1422z0 = false;
        this.A0 = false;
        this.I0 = new KeyCache();
        this.J0 = false;
        this.L0 = j.UNDEFINED;
        this.M0 = new e();
        this.N0 = false;
        this.O0 = new RectF();
        this.P0 = null;
        this.Q0 = new ArrayList<>();
        n0(null);
    }

    private static boolean C0(float f10, float f11, float f12) {
        if (f10 > 0.0f) {
            float f13 = f10 / f12;
            return f11 + ((f10 * f13) - (((f12 * f13) * f13) / 2.0f)) > 1.0f;
        }
        float f14 = (-f10) / f12;
        return f11 + ((f10 * f14) + (((f12 * f14) * f14) / 2.0f)) < 0.0f;
    }

    private void Z() {
        MotionScene motionScene = this.B;
        if (motionScene == null) {
            Log.e("MotionLayout", "CHECK: motion scene not set! set \"app:layoutDescription=\"@xml/file\"");
            return;
        }
        int z10 = motionScene.z();
        MotionScene motionScene2 = this.B;
        a0(z10, motionScene2.j(motionScene2.z()));
        SparseIntArray sparseIntArray = new SparseIntArray();
        SparseIntArray sparseIntArray2 = new SparseIntArray();
        Iterator<MotionScene.b> it = this.B.m().iterator();
        while (it.hasNext()) {
            MotionScene.b next = it.next();
            if (next == this.B.f1673c) {
                Log.v("MotionLayout", "CHECK: CURRENT");
            }
            b0(next);
            int A = next.A();
            int y4 = next.y();
            String b10 = androidx.constraintlayout.motion.widget.a.b(getContext(), A);
            String b11 = androidx.constraintlayout.motion.widget.a.b(getContext(), y4);
            if (sparseIntArray.get(A) == y4) {
                Log.e("MotionLayout", "CHECK: two transitions with the same start and end " + b10 + "->" + b11);
            }
            if (sparseIntArray2.get(y4) == A) {
                Log.e("MotionLayout", "CHECK: you can't have reverse transitions" + b10 + "->" + b11);
            }
            sparseIntArray.put(A, y4);
            sparseIntArray2.put(y4, A);
            if (this.B.j(A) == null) {
                Log.e("MotionLayout", " no such constraintSetStart " + b10);
            }
            if (this.B.j(y4) == null) {
                Log.e("MotionLayout", " no such constraintSetEnd " + b10);
            }
        }
    }

    private void a0(int i10, ConstraintSet constraintSet) {
        String b10 = androidx.constraintlayout.motion.widget.a.b(getContext(), i10);
        int childCount = getChildCount();
        for (int i11 = 0; i11 < childCount; i11++) {
            View childAt = getChildAt(i11);
            int id2 = childAt.getId();
            if (id2 == -1) {
                Log.w("MotionLayout", "CHECK: " + b10 + " ALL VIEWS SHOULD HAVE ID's " + childAt.getClass().getName() + " does not!");
            }
            if (constraintSet.q(id2) == null) {
                Log.w("MotionLayout", "CHECK: " + b10 + " NO CONSTRAINTS for " + androidx.constraintlayout.motion.widget.a.c(childAt));
            }
        }
        int[] s7 = constraintSet.s();
        for (int i12 = 0; i12 < s7.length; i12++) {
            int i13 = s7[i12];
            String b11 = androidx.constraintlayout.motion.widget.a.b(getContext(), i13);
            if (findViewById(s7[i12]) == null) {
                Log.w("MotionLayout", "CHECK: " + b10 + " NO View matches id " + b11);
            }
            if (constraintSet.r(i13) == -1) {
                Log.w("MotionLayout", "CHECK: " + b10 + "(" + b11 + ") no LAYOUT_HEIGHT");
            }
            if (constraintSet.w(i13) == -1) {
                Log.w("MotionLayout", "CHECK: " + b10 + "(" + b11 + ") no LAYOUT_HEIGHT");
            }
        }
    }

    private void b0(MotionScene.b bVar) {
        Log.v("MotionLayout", "CHECK: transition = " + bVar.u(getContext()));
        Log.v("MotionLayout", "CHECK: transition.setDuration = " + bVar.x());
        if (bVar.A() == bVar.y()) {
            Log.e("MotionLayout", "CHECK: start and end constraint set should not be the same!");
        }
    }

    private void c0() {
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            MotionController motionController = this.K.get(childAt);
            if (motionController != null) {
                motionController.s(childAt);
            }
        }
    }

    private void e0() {
        boolean z10;
        float signum = Math.signum(this.Q - this.O);
        long nanoTime = getNanoTime();
        Interpolator interpolator = this.C;
        float f10 = this.O + (!(interpolator instanceof StopLogic) ? ((((float) (nanoTime - this.P)) * signum) * 1.0E-9f) / this.M : 0.0f);
        if (this.R) {
            f10 = this.Q;
        }
        if ((signum <= 0.0f || f10 < this.Q) && (signum > 0.0f || f10 > this.Q)) {
            z10 = false;
        } else {
            f10 = this.Q;
            z10 = true;
        }
        if (interpolator != null && !z10) {
            if (this.f1399c0) {
                f10 = interpolator.getInterpolation(((float) (nanoTime - this.L)) * 1.0E-9f);
            } else {
                f10 = interpolator.getInterpolation(f10);
            }
        }
        if ((signum > 0.0f && f10 >= this.Q) || (signum <= 0.0f && f10 <= this.Q)) {
            f10 = this.Q;
        }
        this.H0 = f10;
        int childCount = getChildCount();
        long nanoTime2 = getNanoTime();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            MotionController motionController = this.K.get(childAt);
            if (motionController != null) {
                motionController.o(childAt, f10, nanoTime2, this.I0);
            }
        }
        if (this.A0) {
            requestLayout();
        }
    }

    private void f0() {
        ArrayList<i> arrayList;
        if ((this.U == null && ((arrayList = this.f1416t0) == null || arrayList.isEmpty())) || this.f1421y0 == this.N) {
            return;
        }
        if (this.f1420x0 != -1) {
            i iVar = this.U;
            if (iVar != null) {
                iVar.b(this, this.E, this.G);
            }
            ArrayList<i> arrayList2 = this.f1416t0;
            if (arrayList2 != null) {
                Iterator<i> it = arrayList2.iterator();
                while (it.hasNext()) {
                    it.next().b(this, this.E, this.G);
                }
            }
            this.f1422z0 = true;
        }
        this.f1420x0 = -1;
        float f10 = this.N;
        this.f1421y0 = f10;
        i iVar2 = this.U;
        if (iVar2 != null) {
            iVar2.a(this, this.E, this.G, f10);
        }
        ArrayList<i> arrayList3 = this.f1416t0;
        if (arrayList3 != null) {
            Iterator<i> it2 = arrayList3.iterator();
            while (it2.hasNext()) {
                it2.next().a(this, this.E, this.G, this.N);
            }
        }
        this.f1422z0 = true;
    }

    private boolean m0(float f10, float f11, View view, MotionEvent motionEvent) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i10 = 0; i10 < childCount; i10++) {
                if (m0(view.getLeft() + f10, view.getTop() + f11, viewGroup.getChildAt(i10), motionEvent)) {
                    return true;
                }
            }
        }
        this.O0.set(view.getLeft() + f10, view.getTop() + f11, f10 + view.getRight(), f11 + view.getBottom());
        if (motionEvent.getAction() == 0) {
            if (this.O0.contains(motionEvent.getX(), motionEvent.getY()) && view.onTouchEvent(motionEvent)) {
                return true;
            }
        } else if (view.onTouchEvent(motionEvent)) {
            return true;
        }
        return false;
    }

    private void n0(AttributeSet attributeSet) {
        MotionScene motionScene;
        R0 = isInEditMode();
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.MotionLayout);
            int indexCount = obtainStyledAttributes.getIndexCount();
            boolean z10 = true;
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.MotionLayout_layoutDescription) {
                    this.B = new MotionScene(getContext(), this, obtainStyledAttributes.getResourceId(index, -1));
                } else if (index == R$styleable.MotionLayout_currentState) {
                    this.F = obtainStyledAttributes.getResourceId(index, -1);
                } else if (index == R$styleable.MotionLayout_motionProgress) {
                    this.Q = obtainStyledAttributes.getFloat(index, 0.0f);
                    this.S = true;
                } else if (index == R$styleable.MotionLayout_applyMotionScene) {
                    z10 = obtainStyledAttributes.getBoolean(index, z10);
                } else if (index == R$styleable.MotionLayout_showPaths) {
                    if (this.f1397a0 == 0) {
                        this.f1397a0 = obtainStyledAttributes.getBoolean(index, false) ? 2 : 0;
                    }
                } else if (index == R$styleable.MotionLayout_motionDebug) {
                    this.f1397a0 = obtainStyledAttributes.getInt(index, 0);
                }
            }
            obtainStyledAttributes.recycle();
            if (this.B == null) {
                Log.e("MotionLayout", "WARNING NO app:layoutDescription tag");
            }
            if (!z10) {
                this.B = null;
            }
        }
        if (this.f1397a0 != 0) {
            Z();
        }
        if (this.F != -1 || (motionScene = this.B) == null) {
            return;
        }
        this.F = motionScene.z();
        this.E = this.B.z();
        this.G = this.B.o();
    }

    private void q0() {
        MotionScene motionScene = this.B;
        if (motionScene == null) {
            return;
        }
        if (motionScene.g(this, this.F)) {
            requestLayout();
            return;
        }
        int i10 = this.F;
        if (i10 != -1) {
            this.B.e(this, i10);
        }
        if (this.B.T()) {
            this.B.R();
        }
    }

    private void r0() {
        ArrayList<i> arrayList;
        if (this.U == null && ((arrayList = this.f1416t0) == null || arrayList.isEmpty())) {
            return;
        }
        this.f1422z0 = false;
        Iterator<Integer> it = this.Q0.iterator();
        while (it.hasNext()) {
            Integer next = it.next();
            i iVar = this.U;
            if (iVar != null) {
                iVar.e(this, next.intValue());
            }
            ArrayList<i> arrayList2 = this.f1416t0;
            if (arrayList2 != null) {
                Iterator<i> it2 = arrayList2.iterator();
                while (it2.hasNext()) {
                    it2.next().e(this, next.intValue());
                }
            }
        }
        this.Q0.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void w0() {
        int childCount = getChildCount();
        this.M0.a();
        boolean z10 = true;
        this.S = true;
        int width = getWidth();
        int height = getHeight();
        int i10 = this.B.i();
        int i11 = 0;
        if (i10 != -1) {
            for (int i12 = 0; i12 < childCount; i12++) {
                MotionController motionController = this.K.get(getChildAt(i12));
                if (motionController != null) {
                    motionController.r(i10);
                }
            }
        }
        for (int i13 = 0; i13 < childCount; i13++) {
            MotionController motionController2 = this.K.get(getChildAt(i13));
            if (motionController2 != null) {
                this.B.s(motionController2);
                motionController2.v(width, height, this.M, getNanoTime());
            }
        }
        float y4 = this.B.y();
        if (y4 != 0.0f) {
            boolean z11 = ((double) y4) < UserProfileInfo.Constant.NA_LAT_LON;
            float abs = Math.abs(y4);
            float f10 = -3.4028235E38f;
            float f11 = Float.MAX_VALUE;
            float f12 = -3.4028235E38f;
            float f13 = Float.MAX_VALUE;
            int i14 = 0;
            while (true) {
                if (i14 >= childCount) {
                    z10 = false;
                    break;
                }
                MotionController motionController3 = this.K.get(getChildAt(i14));
                if (!Float.isNaN(motionController3.f1639k)) {
                    break;
                }
                float i15 = motionController3.i();
                float j10 = motionController3.j();
                float f14 = z11 ? j10 - i15 : j10 + i15;
                f13 = Math.min(f13, f14);
                f12 = Math.max(f12, f14);
                i14++;
            }
            if (!z10) {
                while (i11 < childCount) {
                    MotionController motionController4 = this.K.get(getChildAt(i11));
                    float i16 = motionController4.i();
                    float j11 = motionController4.j();
                    float f15 = z11 ? j11 - i16 : j11 + i16;
                    motionController4.f1641m = 1.0f / (1.0f - abs);
                    motionController4.f1640l = abs - (((f15 - f13) * abs) / (f12 - f13));
                    i11++;
                }
                return;
            }
            for (int i17 = 0; i17 < childCount; i17++) {
                MotionController motionController5 = this.K.get(getChildAt(i17));
                if (!Float.isNaN(motionController5.f1639k)) {
                    f11 = Math.min(f11, motionController5.f1639k);
                    f10 = Math.max(f10, motionController5.f1639k);
                }
            }
            while (i11 < childCount) {
                MotionController motionController6 = this.K.get(getChildAt(i11));
                if (!Float.isNaN(motionController6.f1639k)) {
                    motionController6.f1641m = 1.0f / (1.0f - abs);
                    if (z11) {
                        motionController6.f1640l = abs - (((f10 - motionController6.f1639k) / (f10 - f11)) * abs);
                    } else {
                        motionController6.f1640l = abs - (((motionController6.f1639k - f11) * abs) / (f10 - f11));
                    }
                }
                i11++;
            }
        }
    }

    public void A0(int i10) {
        if (!isAttachedToWindow()) {
            if (this.K0 == null) {
                this.K0 = new h();
            }
            this.K0.d(i10);
            return;
        }
        B0(i10, -1, -1);
    }

    public void B0(int i10, int i11, int i12) {
        StateSet stateSet;
        int a10;
        MotionScene motionScene = this.B;
        if (motionScene != null && (stateSet = motionScene.f1672b) != null && (a10 = stateSet.a(this.F, i10, i11, i12)) != -1) {
            i10 = a10;
        }
        int i13 = this.F;
        if (i13 == i10) {
            return;
        }
        if (this.E == i10) {
            Y(0.0f);
            return;
        }
        if (this.G == i10) {
            Y(1.0f);
            return;
        }
        this.G = i10;
        if (i13 != -1) {
            v0(i13, i10);
            Y(1.0f);
            this.O = 0.0f;
            y0();
            return;
        }
        this.f1399c0 = false;
        this.Q = 1.0f;
        this.N = 0.0f;
        this.O = 0.0f;
        this.P = getNanoTime();
        this.L = getNanoTime();
        this.R = false;
        this.C = null;
        this.M = this.B.n() / 1000.0f;
        this.E = -1;
        this.B.P(-1, this.G);
        this.B.z();
        int childCount = getChildCount();
        this.K.clear();
        for (int i14 = 0; i14 < childCount; i14++) {
            View childAt = getChildAt(i14);
            this.K.put(childAt, new MotionController(childAt));
        }
        this.S = true;
        this.M0.d(this.f1826g, null, this.B.j(i10));
        s0();
        this.M0.a();
        c0();
        int width = getWidth();
        int height = getHeight();
        for (int i15 = 0; i15 < childCount; i15++) {
            MotionController motionController = this.K.get(getChildAt(i15));
            this.B.s(motionController);
            motionController.v(width, height, this.M, getNanoTime());
        }
        float y4 = this.B.y();
        if (y4 != 0.0f) {
            float f10 = Float.MAX_VALUE;
            float f11 = -3.4028235E38f;
            for (int i16 = 0; i16 < childCount; i16++) {
                MotionController motionController2 = this.K.get(getChildAt(i16));
                float j10 = motionController2.j() + motionController2.i();
                f10 = Math.min(f10, j10);
                f11 = Math.max(f11, j10);
            }
            for (int i17 = 0; i17 < childCount; i17++) {
                MotionController motionController3 = this.K.get(getChildAt(i17));
                float i18 = motionController3.i();
                float j11 = motionController3.j();
                motionController3.f1641m = 1.0f / (1.0f - y4);
                motionController3.f1640l = y4 - ((((i18 + j11) - f10) * y4) / (f11 - f10));
            }
        }
        this.N = 0.0f;
        this.O = 0.0f;
        this.S = true;
        invalidate();
    }

    void Y(float f10) {
        if (this.B == null) {
            return;
        }
        float f11 = this.O;
        float f12 = this.N;
        if (f11 != f12 && this.R) {
            this.O = f12;
        }
        float f13 = this.O;
        if (f13 == f10) {
            return;
        }
        this.f1399c0 = false;
        this.Q = f10;
        this.M = r0.n() / 1000.0f;
        setProgress(this.Q);
        this.C = this.B.r();
        this.R = false;
        this.L = getNanoTime();
        this.S = true;
        this.N = f13;
        this.O = f13;
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d0(boolean z10) {
        float f10;
        boolean z11;
        int i10;
        float interpolation;
        boolean z12;
        if (this.P == -1) {
            this.P = getNanoTime();
        }
        float f11 = this.O;
        if (f11 > 0.0f && f11 < 1.0f) {
            this.F = -1;
        }
        boolean z13 = false;
        if (this.f1413q0 || (this.S && (z10 || this.Q != f11))) {
            float signum = Math.signum(this.Q - f11);
            long nanoTime = getNanoTime();
            Interpolator interpolator = this.C;
            if (interpolator instanceof MotionInterpolator) {
                f10 = 0.0f;
            } else {
                f10 = ((((float) (nanoTime - this.P)) * signum) * 1.0E-9f) / this.M;
                this.D = f10;
            }
            float f12 = this.O + f10;
            if (this.R) {
                f12 = this.Q;
            }
            if ((signum <= 0.0f || f12 < this.Q) && (signum > 0.0f || f12 > this.Q)) {
                z11 = false;
            } else {
                f12 = this.Q;
                this.S = false;
                z11 = true;
            }
            this.O = f12;
            this.N = f12;
            this.P = nanoTime;
            if (interpolator != null && !z11) {
                if (this.f1399c0) {
                    interpolation = interpolator.getInterpolation(((float) (nanoTime - this.L)) * 1.0E-9f);
                    this.O = interpolation;
                    this.P = nanoTime;
                    Interpolator interpolator2 = this.C;
                    if (interpolator2 instanceof MotionInterpolator) {
                        float a10 = ((MotionInterpolator) interpolator2).a();
                        this.D = a10;
                        if (Math.abs(a10) * this.M <= 1.0E-5f) {
                            this.S = false;
                        }
                        if (a10 > 0.0f && interpolation >= 1.0f) {
                            this.O = 1.0f;
                            this.S = false;
                            interpolation = 1.0f;
                        }
                        if (a10 < 0.0f && interpolation <= 0.0f) {
                            this.O = 0.0f;
                            this.S = false;
                            f12 = 0.0f;
                        }
                    }
                } else {
                    interpolation = interpolator.getInterpolation(f12);
                    Interpolator interpolator3 = this.C;
                    if (interpolator3 instanceof MotionInterpolator) {
                        this.D = ((MotionInterpolator) interpolator3).a();
                    } else {
                        this.D = ((interpolator3.getInterpolation(f12 + f10) - interpolation) * signum) / f10;
                    }
                }
                f12 = interpolation;
            }
            if (Math.abs(this.D) > 1.0E-5f) {
                setState(j.MOVING);
            }
            if ((signum > 0.0f && f12 >= this.Q) || (signum <= 0.0f && f12 <= this.Q)) {
                f12 = this.Q;
                this.S = false;
            }
            if (f12 >= 1.0f || f12 <= 0.0f) {
                this.S = false;
                setState(j.FINISHED);
            }
            int childCount = getChildCount();
            this.f1413q0 = false;
            long nanoTime2 = getNanoTime();
            this.H0 = f12;
            for (int i11 = 0; i11 < childCount; i11++) {
                View childAt = getChildAt(i11);
                MotionController motionController = this.K.get(childAt);
                if (motionController != null) {
                    this.f1413q0 = motionController.o(childAt, f12, nanoTime2, this.I0) | this.f1413q0;
                }
            }
            boolean z14 = (signum > 0.0f && f12 >= this.Q) || (signum <= 0.0f && f12 <= this.Q);
            if (!this.f1413q0 && !this.S && z14) {
                setState(j.FINISHED);
            }
            if (this.A0) {
                requestLayout();
            }
            this.f1413q0 = (!z14) | this.f1413q0;
            if (f12 <= 0.0f && (i10 = this.E) != -1 && this.F != i10) {
                this.F = i10;
                this.B.j(i10).c(this);
                setState(j.FINISHED);
                z13 = true;
            }
            if (f12 >= 1.0d) {
                int i12 = this.F;
                int i13 = this.G;
                if (i12 != i13) {
                    this.F = i13;
                    this.B.j(i13).c(this);
                    setState(j.FINISHED);
                    z13 = true;
                }
            }
            if (this.f1413q0 || this.S) {
                invalidate();
            } else if ((signum > 0.0f && f12 == 1.0f) || (signum < 0.0f && f12 == 0.0f)) {
                setState(j.FINISHED);
            }
            if ((!this.f1413q0 && this.S && signum > 0.0f && f12 == 1.0f) || (signum < 0.0f && f12 == 0.0f)) {
                q0();
            }
        }
        float f13 = this.O;
        if (f13 >= 1.0f) {
            int i14 = this.F;
            int i15 = this.G;
            z12 = i14 == i15 ? z13 : true;
            this.F = i15;
        } else {
            if (f13 <= 0.0f) {
                int i16 = this.F;
                int i17 = this.E;
                z12 = i16 == i17 ? z13 : true;
                this.F = i17;
            }
            this.N0 |= z13;
            if (z13 && !this.J0) {
                requestLayout();
            }
            this.N = this.O;
        }
        z13 = z12;
        this.N0 |= z13;
        if (z13) {
            requestLayout();
        }
        this.N = this.O;
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        d0(false);
        super.dispatchDraw(canvas);
        if (this.B == null) {
            return;
        }
        if ((this.f1397a0 & 1) == 1 && !isInEditMode()) {
            this.f1417u0++;
            long nanoTime = getNanoTime();
            long j10 = this.f1418v0;
            if (j10 != -1) {
                if (nanoTime - j10 > 200000000) {
                    this.f1419w0 = ((int) ((this.f1417u0 / (((float) r5) * 1.0E-9f)) * 100.0f)) / 100.0f;
                    this.f1417u0 = 0;
                    this.f1418v0 = nanoTime;
                }
            } else {
                this.f1418v0 = nanoTime;
            }
            Paint paint = new Paint();
            paint.setTextSize(42.0f);
            String str = this.f1419w0 + " fps " + androidx.constraintlayout.motion.widget.a.d(this, this.E) + " -> ";
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(androidx.constraintlayout.motion.widget.a.d(this, this.G));
            sb2.append(" (progress: ");
            sb2.append(((int) (getProgress() * 1000.0f)) / 10.0f);
            sb2.append(" ) state=");
            int i10 = this.F;
            sb2.append(i10 == -1 ? "undefined" : androidx.constraintlayout.motion.widget.a.d(this, i10));
            String sb3 = sb2.toString();
            paint.setColor(-16777216);
            canvas.drawText(sb3, 11.0f, getHeight() - 29, paint);
            paint.setColor(-7864184);
            canvas.drawText(sb3, 10.0f, getHeight() - 30, paint);
        }
        if (this.f1397a0 > 1) {
            if (this.f1398b0 == null) {
                this.f1398b0 = new d();
            }
            this.f1398b0.a(canvas, this.K, this.B.n(), this.f1397a0);
        }
    }

    protected void g0() {
        int i10;
        ArrayList<i> arrayList;
        if ((this.U != null || ((arrayList = this.f1416t0) != null && !arrayList.isEmpty())) && this.f1420x0 == -1) {
            this.f1420x0 = this.F;
            if (this.Q0.isEmpty()) {
                i10 = -1;
            } else {
                i10 = this.Q0.get(r0.size() - 1).intValue();
            }
            int i11 = this.F;
            if (i10 != i11 && i11 != -1) {
                this.Q0.add(Integer.valueOf(i11));
            }
        }
        r0();
    }

    public int[] getConstraintSetIds() {
        MotionScene motionScene = this.B;
        if (motionScene == null) {
            return null;
        }
        return motionScene.l();
    }

    public int getCurrentState() {
        return this.F;
    }

    public ArrayList<MotionScene.b> getDefinedTransitions() {
        MotionScene motionScene = this.B;
        if (motionScene == null) {
            return null;
        }
        return motionScene.m();
    }

    public DesignTool getDesignTool() {
        if (this.f1402f0 == null) {
            this.f1402f0 = new DesignTool(this);
        }
        return this.f1402f0;
    }

    public int getEndState() {
        return this.G;
    }

    protected long getNanoTime() {
        return System.nanoTime();
    }

    public float getProgress() {
        return this.O;
    }

    public int getStartState() {
        return this.E;
    }

    public float getTargetPosition() {
        return this.Q;
    }

    public Bundle getTransitionState() {
        if (this.K0 == null) {
            this.K0 = new h();
        }
        this.K0.c();
        return this.K0.b();
    }

    public long getTransitionTimeMs() {
        if (this.B != null) {
            this.M = r0.n() / 1000.0f;
        }
        return this.M * 1000.0f;
    }

    public float getVelocity() {
        return this.D;
    }

    public void h0(int i10, boolean z10, float f10) {
        i iVar = this.U;
        if (iVar != null) {
            iVar.d(this, i10, z10, f10);
        }
        ArrayList<i> arrayList = this.f1416t0;
        if (arrayList != null) {
            Iterator<i> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().d(this, i10, z10, f10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i0(int i10, float f10, float f11, float f12, float[] fArr) {
        String resourceName;
        HashMap<View, MotionController> hashMap = this.K;
        View r10 = r(i10);
        MotionController motionController = hashMap.get(r10);
        if (motionController != null) {
            motionController.g(f10, f11, f12, fArr);
            float y4 = r10.getY();
            this.V = f10;
            this.W = y4;
            return;
        }
        if (r10 == null) {
            resourceName = "" + i10;
        } else {
            resourceName = r10.getContext().getResources().getResourceName(i10);
        }
        Log.w("MotionLayout", "WARNING could not find view id " + resourceName);
    }

    @Override // android.view.View
    public boolean isAttachedToWindow() {
        return super.isAttachedToWindow();
    }

    @Override // androidx.core.view.NestedScrollingParent3
    public void j(View view, int i10, int i11, int i12, int i13, int i14, int[] iArr) {
        if (this.f1408l0 || i10 != 0 || i11 != 0) {
            iArr[0] = iArr[0] + i12;
            iArr[1] = iArr[1] + i13;
        }
        this.f1408l0 = false;
    }

    public ConstraintSet j0(int i10) {
        MotionScene motionScene = this.B;
        if (motionScene == null) {
            return null;
        }
        return motionScene.j(i10);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void k(View view, int i10, int i11, int i12, int i13, int i14) {
    }

    public MotionScene.b k0(int i10) {
        return this.B.A(i10);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public boolean l(View view, View view2, int i10, int i11) {
        MotionScene.b bVar;
        MotionScene motionScene = this.B;
        return (motionScene == null || (bVar = motionScene.f1673c) == null || bVar.B() == null || (this.B.f1673c.B().d() & 2) != 0) ? false : true;
    }

    public void l0(View view, float f10, float f11, float[] fArr, int i10) {
        float f12;
        float f13 = this.D;
        float f14 = this.O;
        if (this.C != null) {
            float signum = Math.signum(this.Q - f14);
            float interpolation = this.C.getInterpolation(this.O + 1.0E-5f);
            float interpolation2 = this.C.getInterpolation(this.O);
            f13 = (signum * ((interpolation - interpolation2) / 1.0E-5f)) / this.M;
            f12 = interpolation2;
        } else {
            f12 = f14;
        }
        Interpolator interpolator = this.C;
        if (interpolator instanceof MotionInterpolator) {
            f13 = ((MotionInterpolator) interpolator).a();
        }
        MotionController motionController = this.K.get(view);
        if ((i10 & 1) == 0) {
            motionController.l(f12, view.getWidth(), view.getHeight(), f10, f11, fArr);
        } else {
            motionController.g(f12, f10, f11, fArr);
        }
        if (i10 < 2) {
            fArr[0] = fArr[0] * f13;
            fArr[1] = fArr[1] * f13;
        }
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void m(View view, View view2, int i10, int i11) {
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void n(View view, int i10) {
        MotionScene motionScene = this.B;
        if (motionScene == null) {
            return;
        }
        float f10 = this.f1409m0;
        float f11 = this.f1412p0;
        motionScene.I(f10 / f11, this.f1410n0 / f11);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void o(View view, int i10, int i11, int[] iArr, int i12) {
        MotionScene.b bVar;
        TouchResponse B;
        int k10;
        MotionScene motionScene = this.B;
        if (motionScene == null || (bVar = motionScene.f1673c) == null || !bVar.C()) {
            return;
        }
        MotionScene.b bVar2 = this.B.f1673c;
        if (bVar2 == null || !bVar2.C() || (B = bVar2.B()) == null || (k10 = B.k()) == -1 || view.getId() == k10) {
            MotionScene motionScene2 = this.B;
            if (motionScene2 != null && motionScene2.v()) {
                float f10 = this.N;
                if ((f10 == 1.0f || f10 == 0.0f) && view.canScrollVertically(-1)) {
                    return;
                }
            }
            if (bVar2.B() != null && (this.B.f1673c.B().d() & 1) != 0) {
                float w10 = this.B.w(i10, i11);
                float f11 = this.O;
                if ((f11 <= 0.0f && w10 < 0.0f) || (f11 >= 1.0f && w10 > 0.0f)) {
                    view.setNestedScrollingEnabled(false);
                    view.post(new a(view));
                    return;
                }
            }
            float f12 = this.N;
            long nanoTime = getNanoTime();
            float f13 = i10;
            this.f1409m0 = f13;
            float f14 = i11;
            this.f1410n0 = f14;
            this.f1412p0 = (float) ((nanoTime - this.f1411o0) * 1.0E-9d);
            this.f1411o0 = nanoTime;
            this.B.H(f13, f14);
            if (f12 != this.N) {
                iArr[0] = i10;
                iArr[1] = i11;
            }
            d0(false);
            if (iArr[0] == 0 && iArr[1] == 0) {
                return;
            }
            this.f1408l0 = true;
        }
    }

    public boolean o0() {
        return this.J;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        int i10;
        super.onAttachedToWindow();
        MotionScene motionScene = this.B;
        if (motionScene != null && (i10 = this.F) != -1) {
            ConstraintSet j10 = motionScene.j(i10);
            this.B.L(this);
            if (j10 != null) {
                j10.d(this);
            }
            this.E = this.F;
        }
        q0();
        h hVar = this.K0;
        if (hVar != null) {
            hVar.a();
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        MotionScene.b bVar;
        TouchResponse B;
        int k10;
        RectF j10;
        MotionScene motionScene = this.B;
        if (motionScene != null && this.J && (bVar = motionScene.f1673c) != null && bVar.C() && (B = bVar.B()) != null && ((motionEvent.getAction() != 0 || (j10 = B.j(this, new RectF())) == null || j10.contains(motionEvent.getX(), motionEvent.getY())) && (k10 = B.k()) != -1)) {
            View view = this.P0;
            if (view == null || view.getId() != k10) {
                this.P0 = findViewById(k10);
            }
            if (this.P0 != null) {
                this.O0.set(r0.getLeft(), this.P0.getTop(), this.P0.getRight(), this.P0.getBottom());
                if (this.O0.contains(motionEvent.getX(), motionEvent.getY()) && !m0(0.0f, 0.0f, this.P0, motionEvent)) {
                    return onTouchEvent(motionEvent);
                }
            }
        }
        return false;
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        this.J0 = true;
        try {
            if (this.B == null) {
                super.onLayout(z10, i10, i11, i12, i13);
                return;
            }
            int i14 = i12 - i10;
            int i15 = i13 - i11;
            if (this.f1406j0 != i14 || this.f1407k0 != i15) {
                s0();
                d0(true);
            }
            this.f1406j0 = i14;
            this.f1407k0 = i15;
            this.f1404h0 = i14;
            this.f1405i0 = i15;
        } finally {
            this.J0 = false;
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        if (this.B == null) {
            super.onMeasure(i10, i11);
            return;
        }
        boolean z10 = false;
        boolean z11 = (this.H == i10 && this.I == i11) ? false : true;
        if (this.N0) {
            this.N0 = false;
            q0();
            r0();
            z11 = true;
        }
        if (this.f1831l) {
            z11 = true;
        }
        this.H = i10;
        this.I = i11;
        int z12 = this.B.z();
        int o10 = this.B.o();
        if ((z11 || this.M0.e(z12, o10)) && this.E != -1) {
            super.onMeasure(i10, i11);
            this.M0.d(this.f1826g, this.B.j(z12), this.B.j(o10));
            this.M0.g();
            this.M0.h(z12, o10);
        } else {
            z10 = true;
        }
        if (this.A0 || z10) {
            int paddingTop = getPaddingTop() + getPaddingBottom();
            int Q = this.f1826g.Q() + getPaddingLeft() + getPaddingRight();
            int w10 = this.f1826g.w() + paddingTop;
            int i12 = this.F0;
            if (i12 == Integer.MIN_VALUE || i12 == 0) {
                Q = (int) (this.B0 + (this.H0 * (this.D0 - r7)));
                requestLayout();
            }
            int i13 = this.G0;
            if (i13 == Integer.MIN_VALUE || i13 == 0) {
                w10 = (int) (this.C0 + (this.H0 * (this.E0 - r7)));
                requestLayout();
            }
            setMeasuredDimension(Q, w10);
        }
        e0();
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedFling(View view, float f10, float f11, boolean z10) {
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedPreFling(View view, float f10, float f11) {
        return false;
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i10) {
        MotionScene motionScene = this.B;
        if (motionScene != null) {
            motionScene.O(u());
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        MotionScene motionScene = this.B;
        if (motionScene != null && this.J && motionScene.T()) {
            MotionScene.b bVar = this.B.f1673c;
            if (bVar != null && !bVar.C()) {
                return super.onTouchEvent(motionEvent);
            }
            this.B.J(motionEvent, getCurrentState(), this);
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup
    public void onViewAdded(View view) {
        super.onViewAdded(view);
        if (view instanceof MotionHelper) {
            MotionHelper motionHelper = (MotionHelper) view;
            if (this.f1416t0 == null) {
                this.f1416t0 = new ArrayList<>();
            }
            this.f1416t0.add(motionHelper);
            if (motionHelper.w()) {
                if (this.f1414r0 == null) {
                    this.f1414r0 = new ArrayList<>();
                }
                this.f1414r0.add(motionHelper);
            }
            if (motionHelper.v()) {
                if (this.f1415s0 == null) {
                    this.f1415s0 = new ArrayList<>();
                }
                this.f1415s0.add(motionHelper);
            }
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        ArrayList<MotionHelper> arrayList = this.f1414r0;
        if (arrayList != null) {
            arrayList.remove(view);
        }
        ArrayList<MotionHelper> arrayList2 = this.f1415s0;
        if (arrayList2 != null) {
            arrayList2.remove(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public f p0() {
        return g.f();
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View, android.view.ViewParent
    public void requestLayout() {
        MotionScene motionScene;
        MotionScene.b bVar;
        if (this.A0 || this.F != -1 || (motionScene = this.B) == null || (bVar = motionScene.f1673c) == null || bVar.z() != 0) {
            super.requestLayout();
        }
    }

    public void s0() {
        this.M0.g();
        invalidate();
    }

    public void setDebugMode(int i10) {
        this.f1397a0 = i10;
        invalidate();
    }

    public void setInteractionEnabled(boolean z10) {
        this.J = z10;
    }

    public void setInterpolatedProgress(float f10) {
        if (this.B != null) {
            setState(j.MOVING);
            Interpolator r10 = this.B.r();
            if (r10 != null) {
                setProgress(r10.getInterpolation(f10));
                return;
            }
        }
        setProgress(f10);
    }

    public void setOnHide(float f10) {
        ArrayList<MotionHelper> arrayList = this.f1415s0;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i10 = 0; i10 < size; i10++) {
                this.f1415s0.get(i10).setProgress(f10);
            }
        }
    }

    public void setOnShow(float f10) {
        ArrayList<MotionHelper> arrayList = this.f1414r0;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i10 = 0; i10 < size; i10++) {
                this.f1414r0.get(i10).setProgress(f10);
            }
        }
    }

    public void setProgress(float f10) {
        if (!isAttachedToWindow()) {
            if (this.K0 == null) {
                this.K0 = new h();
            }
            this.K0.e(f10);
            return;
        }
        if (f10 <= 0.0f) {
            this.F = this.E;
            if (this.O == 0.0f) {
                setState(j.FINISHED);
            }
        } else if (f10 >= 1.0f) {
            this.F = this.G;
            if (this.O == 1.0f) {
                setState(j.FINISHED);
            }
        } else {
            this.F = -1;
            setState(j.MOVING);
        }
        if (this.B == null) {
            return;
        }
        this.R = true;
        this.Q = f10;
        this.N = f10;
        this.P = -1L;
        this.L = -1L;
        this.C = null;
        this.S = true;
        invalidate();
    }

    public void setScene(MotionScene motionScene) {
        this.B = motionScene;
        motionScene.O(u());
        s0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setState(j jVar) {
        j jVar2 = j.FINISHED;
        if (jVar == jVar2 && this.F == -1) {
            return;
        }
        j jVar3 = this.L0;
        this.L0 = jVar;
        j jVar4 = j.MOVING;
        if (jVar3 == jVar4 && jVar == jVar4) {
            f0();
        }
        int i10 = b.f1425a[jVar3.ordinal()];
        if (i10 != 1 && i10 != 2) {
            if (i10 == 3 && jVar == jVar2) {
                g0();
                return;
            }
            return;
        }
        if (jVar == jVar4) {
            f0();
        }
        if (jVar == jVar2) {
            g0();
        }
    }

    public void setTransition(int i10) {
        if (this.B != null) {
            MotionScene.b k02 = k0(i10);
            this.E = k02.A();
            this.G = k02.y();
            if (!isAttachedToWindow()) {
                if (this.K0 == null) {
                    this.K0 = new h();
                }
                this.K0.f(this.E);
                this.K0.d(this.G);
                return;
            }
            float f10 = Float.NaN;
            int i11 = this.F;
            if (i11 == this.E) {
                f10 = 0.0f;
            } else if (i11 == this.G) {
                f10 = 1.0f;
            }
            this.B.Q(k02);
            this.M0.d(this.f1826g, this.B.j(this.E), this.B.j(this.G));
            s0();
            this.O = Float.isNaN(f10) ? 0.0f : f10;
            if (Float.isNaN(f10)) {
                Log.v("MotionLayout", androidx.constraintlayout.motion.widget.a.a() + " transitionToStart ");
                z0();
                return;
            }
            setProgress(f10);
        }
    }

    public void setTransitionDuration(int i10) {
        MotionScene motionScene = this.B;
        if (motionScene == null) {
            Log.e("MotionLayout", "MotionScene not defined");
        } else {
            motionScene.N(i10);
        }
    }

    public void setTransitionListener(i iVar) {
        this.U = iVar;
    }

    public void setTransitionState(Bundle bundle) {
        if (this.K0 == null) {
            this.K0 = new h();
        }
        this.K0.g(bundle);
        if (isAttachedToWindow()) {
            this.K0.a();
        }
    }

    public void t0(float f10, float f11) {
        if (!isAttachedToWindow()) {
            if (this.K0 == null) {
                this.K0 = new h();
            }
            this.K0.e(f10);
            this.K0.h(f11);
            return;
        }
        setProgress(f10);
        setState(j.MOVING);
        this.D = f11;
        Y(1.0f);
    }

    @Override // android.view.View
    public String toString() {
        Context context = getContext();
        return androidx.constraintlayout.motion.widget.a.b(context, this.E) + "->" + androidx.constraintlayout.motion.widget.a.b(context, this.G) + " (pos:" + this.O + " Dpos/Dt:" + this.D;
    }

    public void u0(int i10, int i11, int i12) {
        setState(j.SETUP);
        this.F = i10;
        this.E = -1;
        this.G = -1;
        ConstraintLayoutStates constraintLayoutStates = this.f1834o;
        if (constraintLayoutStates != null) {
            constraintLayoutStates.d(i10, i11, i12);
            return;
        }
        MotionScene motionScene = this.B;
        if (motionScene != null) {
            motionScene.j(i10).d(this);
        }
    }

    public void v0(int i10, int i11) {
        if (!isAttachedToWindow()) {
            if (this.K0 == null) {
                this.K0 = new h();
            }
            this.K0.f(i10);
            this.K0.d(i11);
            return;
        }
        MotionScene motionScene = this.B;
        if (motionScene != null) {
            this.E = i10;
            this.G = i11;
            motionScene.P(i10, i11);
            this.M0.d(this.f1826g, this.B.j(i10), this.B.j(i11));
            s0();
            this.O = 0.0f;
            z0();
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout
    protected void w(int i10) {
        this.f1834o = null;
    }

    public void x0(int i10, float f10, float f11) {
        if (this.B == null || this.O == f10) {
            return;
        }
        this.f1399c0 = true;
        this.L = getNanoTime();
        float n10 = this.B.n() / 1000.0f;
        this.M = n10;
        this.Q = f10;
        this.S = true;
        if (i10 == 0 || i10 == 1 || i10 == 2) {
            if (i10 == 1) {
                f10 = 0.0f;
            } else if (i10 == 2) {
                f10 = 1.0f;
            }
            this.f1400d0.c(this.O, f10, f11, n10, this.B.t(), this.B.u());
            int i11 = this.F;
            this.Q = f10;
            this.F = i11;
            this.C = this.f1400d0;
        } else if (i10 == 4) {
            this.f1401e0.b(f11, this.O, this.B.t());
            this.C = this.f1401e0;
        } else if (i10 == 5) {
            if (C0(f11, this.O, this.B.t())) {
                this.f1401e0.b(f11, this.O, this.B.t());
                this.C = this.f1401e0;
            } else {
                this.f1400d0.c(this.O, f10, f11, this.M, this.B.t(), this.B.u());
                this.D = 0.0f;
                int i12 = this.F;
                this.Q = f10;
                this.F = i12;
                this.C = this.f1400d0;
            }
        }
        this.R = false;
        this.L = getNanoTime();
        invalidate();
    }

    public void y0() {
        Y(1.0f);
    }

    public void z0() {
        Y(0.0f);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTransition(MotionScene.b bVar) {
        this.B.Q(bVar);
        setState(j.SETUP);
        if (this.F == this.B.o()) {
            this.O = 1.0f;
            this.N = 1.0f;
            this.Q = 1.0f;
        } else {
            this.O = 0.0f;
            this.N = 0.0f;
            this.Q = 0.0f;
        }
        this.P = bVar.D(1) ? -1L : getNanoTime();
        int z10 = this.B.z();
        int o10 = this.B.o();
        if (z10 == this.E && o10 == this.G) {
            return;
        }
        this.E = z10;
        this.G = o10;
        this.B.P(z10, o10);
        this.M0.d(this.f1826g, this.B.j(this.E), this.B.j(this.G));
        this.M0.h(this.E, this.G);
        this.M0.g();
        s0();
    }

    public MotionLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.D = 0.0f;
        this.E = -1;
        this.F = -1;
        this.G = -1;
        this.H = 0;
        this.I = 0;
        this.J = true;
        this.K = new HashMap<>();
        this.L = 0L;
        this.M = 1.0f;
        this.N = 0.0f;
        this.O = 0.0f;
        this.Q = 0.0f;
        this.S = false;
        this.T = false;
        this.f1397a0 = 0;
        this.f1399c0 = false;
        this.f1400d0 = new StopLogic();
        this.f1401e0 = new c();
        this.f1403g0 = true;
        this.f1408l0 = false;
        this.f1413q0 = false;
        this.f1414r0 = null;
        this.f1415s0 = null;
        this.f1416t0 = null;
        this.f1417u0 = 0;
        this.f1418v0 = -1L;
        this.f1419w0 = 0.0f;
        this.f1420x0 = 0;
        this.f1421y0 = 0.0f;
        this.f1422z0 = false;
        this.A0 = false;
        this.I0 = new KeyCache();
        this.J0 = false;
        this.L0 = j.UNDEFINED;
        this.M0 = new e();
        this.N0 = false;
        this.O0 = new RectF();
        this.P0 = null;
        this.Q0 = new ArrayList<>();
        n0(attributeSet);
    }

    public MotionLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.D = 0.0f;
        this.E = -1;
        this.F = -1;
        this.G = -1;
        this.H = 0;
        this.I = 0;
        this.J = true;
        this.K = new HashMap<>();
        this.L = 0L;
        this.M = 1.0f;
        this.N = 0.0f;
        this.O = 0.0f;
        this.Q = 0.0f;
        this.S = false;
        this.T = false;
        this.f1397a0 = 0;
        this.f1399c0 = false;
        this.f1400d0 = new StopLogic();
        this.f1401e0 = new c();
        this.f1403g0 = true;
        this.f1408l0 = false;
        this.f1413q0 = false;
        this.f1414r0 = null;
        this.f1415s0 = null;
        this.f1416t0 = null;
        this.f1417u0 = 0;
        this.f1418v0 = -1L;
        this.f1419w0 = 0.0f;
        this.f1420x0 = 0;
        this.f1421y0 = 0.0f;
        this.f1422z0 = false;
        this.A0 = false;
        this.I0 = new KeyCache();
        this.J0 = false;
        this.L0 = j.UNDEFINED;
        this.M0 = new e();
        this.N0 = false;
        this.O0 = new RectF();
        this.P0 = null;
        this.Q0 = new ArrayList<>();
        n0(attributeSet);
    }
}
