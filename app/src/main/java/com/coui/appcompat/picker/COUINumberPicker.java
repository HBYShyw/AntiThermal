package com.coui.appcompat.picker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.PathInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import com.oplus.os.LinearmotorVibrator;
import com.support.control.R$attr;
import com.support.control.R$dimen;
import com.support.control.R$raw;
import com.support.control.R$style;
import com.support.control.R$styleable;
import d2.COUIMathUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import k3.VibrateUtils;
import t2.COUISoundLoadUtil;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUINumberPicker extends LinearLayout {

    /* renamed from: t1, reason: collision with root package name */
    private static final PathInterpolator f6756t1 = new PathInterpolator(0.0f, 0.0f, 0.4f, 1.0f);

    /* renamed from: u1, reason: collision with root package name */
    private static final PathInterpolator f6757u1 = new PathInterpolator(0.0f, 0.23f, 0.1f, 1.0f);

    /* renamed from: v1, reason: collision with root package name */
    private static final float f6758v1 = (float) (Math.log(0.78d) / Math.log(0.9d));
    private i A;
    private int A0;
    private boolean B;
    private int B0;
    private boolean C;
    private int C0;
    private c D;
    private int D0;
    private long E;
    private int E0;
    private int[] F;
    private int F0;
    private int G;
    private float G0;
    private int H;
    private float H0;
    private int I;
    private float I0;
    private int J;
    private String J0;
    private b K;
    private String K0;
    private float L;
    private boolean L0;
    private long M;
    private boolean M0;
    private float N;
    private float N0;
    private VelocityTracker O;
    private float O0;
    private int P;
    private float P0;
    private int Q;
    int Q0;
    private int R;
    int R0;
    private int S;
    int S0;
    private boolean T;
    private int T0;
    private int U;
    private int U0;
    private int V;
    private int V0;
    private int W;
    private int W0;
    private int X0;
    private int Y0;
    private int Z0;

    /* renamed from: a0, reason: collision with root package name */
    private boolean f6759a0;

    /* renamed from: a1, reason: collision with root package name */
    private int f6760a1;

    /* renamed from: b0, reason: collision with root package name */
    private boolean f6761b0;

    /* renamed from: b1, reason: collision with root package name */
    private int f6762b1;

    /* renamed from: c0, reason: collision with root package name */
    private a f6763c0;

    /* renamed from: c1, reason: collision with root package name */
    private boolean f6764c1;

    /* renamed from: d0, reason: collision with root package name */
    private int f6765d0;

    /* renamed from: d1, reason: collision with root package name */
    private boolean f6766d1;

    /* renamed from: e, reason: collision with root package name */
    private final float f6767e;

    /* renamed from: e0, reason: collision with root package name */
    private AccessibilityManager f6768e0;

    /* renamed from: e1, reason: collision with root package name */
    private boolean f6769e1;

    /* renamed from: f, reason: collision with root package name */
    private final int f6770f;

    /* renamed from: f0, reason: collision with root package name */
    private COUISoundLoadUtil f6771f0;

    /* renamed from: f1, reason: collision with root package name */
    private boolean f6772f1;

    /* renamed from: g, reason: collision with root package name */
    private final int f6773g;

    /* renamed from: g0, reason: collision with root package name */
    private HandlerThread f6774g0;

    /* renamed from: g1, reason: collision with root package name */
    private Paint f6775g1;

    /* renamed from: h, reason: collision with root package name */
    private final int f6776h;

    /* renamed from: h0, reason: collision with root package name */
    private Handler f6777h0;

    /* renamed from: h1, reason: collision with root package name */
    private Object f6778h1;

    /* renamed from: i, reason: collision with root package name */
    private final int f6779i;

    /* renamed from: i0, reason: collision with root package name */
    private int f6780i0;

    /* renamed from: i1, reason: collision with root package name */
    private int f6781i1;

    /* renamed from: j, reason: collision with root package name */
    private final SparseArray<String> f6782j;

    /* renamed from: j0, reason: collision with root package name */
    private int f6783j0;

    /* renamed from: j1, reason: collision with root package name */
    private long f6784j1;

    /* renamed from: k, reason: collision with root package name */
    private final Paint f6785k;

    /* renamed from: k0, reason: collision with root package name */
    private int f6786k0;

    /* renamed from: k1, reason: collision with root package name */
    private int f6787k1;

    /* renamed from: l, reason: collision with root package name */
    private final Paint f6788l;

    /* renamed from: l0, reason: collision with root package name */
    private int f6789l0;

    /* renamed from: l1, reason: collision with root package name */
    private int f6790l1;

    /* renamed from: m, reason: collision with root package name */
    private final Paint f6791m;

    /* renamed from: m0, reason: collision with root package name */
    private int f6792m0;

    /* renamed from: m1, reason: collision with root package name */
    private final float f6793m1;

    /* renamed from: n, reason: collision with root package name */
    private final Scroller f6794n;

    /* renamed from: n0, reason: collision with root package name */
    private int f6795n0;

    /* renamed from: n1, reason: collision with root package name */
    private final float f6796n1;

    /* renamed from: o, reason: collision with root package name */
    private final Scroller f6797o;

    /* renamed from: o0, reason: collision with root package name */
    private int f6798o0;

    /* renamed from: o1, reason: collision with root package name */
    private int f6799o1;

    /* renamed from: p, reason: collision with root package name */
    private final g f6800p;

    /* renamed from: p0, reason: collision with root package name */
    private int f6801p0;

    /* renamed from: p1, reason: collision with root package name */
    private int f6802p1;

    /* renamed from: q, reason: collision with root package name */
    private int f6803q;

    /* renamed from: q0, reason: collision with root package name */
    private int f6804q0;

    /* renamed from: q1, reason: collision with root package name */
    private float f6805q1;

    /* renamed from: r, reason: collision with root package name */
    private int f6806r;

    /* renamed from: r0, reason: collision with root package name */
    private int f6807r0;

    /* renamed from: r1, reason: collision with root package name */
    private int f6808r1;

    /* renamed from: s, reason: collision with root package name */
    private int f6809s;

    /* renamed from: s0, reason: collision with root package name */
    private int f6810s0;

    /* renamed from: s1, reason: collision with root package name */
    private int f6811s1;

    /* renamed from: t, reason: collision with root package name */
    private String[] f6812t;

    /* renamed from: t0, reason: collision with root package name */
    private int f6813t0;

    /* renamed from: u, reason: collision with root package name */
    private int f6814u;

    /* renamed from: u0, reason: collision with root package name */
    private int f6815u0;

    /* renamed from: v, reason: collision with root package name */
    private int f6816v;

    /* renamed from: v0, reason: collision with root package name */
    private int f6817v0;

    /* renamed from: w, reason: collision with root package name */
    private int f6818w;

    /* renamed from: w0, reason: collision with root package name */
    private int f6819w0;

    /* renamed from: x, reason: collision with root package name */
    private f f6820x;

    /* renamed from: x0, reason: collision with root package name */
    private int f6821x0;

    /* renamed from: y, reason: collision with root package name */
    private e f6822y;

    /* renamed from: y0, reason: collision with root package name */
    private int f6823y0;

    /* renamed from: z, reason: collision with root package name */
    private d f6824z;

    /* renamed from: z0, reason: collision with root package name */
    private int f6825z0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a extends AccessibilityNodeProvider {

        /* renamed from: a, reason: collision with root package name */
        private final Rect f6826a = new Rect();

        /* renamed from: b, reason: collision with root package name */
        private final int[] f6827b = new int[2];

        /* renamed from: c, reason: collision with root package name */
        private int f6828c = Integer.MIN_VALUE;

        a() {
        }

        private AccessibilityNodeInfo a(int i10, String str, int i11, int i12, int i13, int i14) {
            AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain();
            obtain.setPackageName(COUINumberPicker.this.getContext().getPackageName());
            obtain.setSource(COUINumberPicker.this, i10);
            obtain.setParent(COUINumberPicker.this);
            if (!TextUtils.isEmpty(COUINumberPicker.this.J0)) {
                str = str + COUINumberPicker.this.J0;
            }
            obtain.setText(str);
            obtain.setClickable(true);
            obtain.setLongClickable(true);
            obtain.setEnabled(COUINumberPicker.this.isEnabled());
            Rect rect = this.f6826a;
            rect.set(i11, i12, i13, i14);
            obtain.setBoundsInParent(rect);
            int[] iArr = this.f6827b;
            COUINumberPicker.this.getLocationOnScreen(iArr);
            rect.offset(iArr[0], iArr[1]);
            obtain.setBoundsInScreen(rect);
            if (this.f6828c != i10) {
                obtain.addAction(64);
            }
            if (this.f6828c == i10) {
                obtain.addAction(128);
            }
            if (COUINumberPicker.this.isEnabled()) {
                obtain.addAction(16);
            }
            return obtain;
        }

        private AccessibilityNodeInfo b(String str, int i10, int i11, int i12, int i13) {
            AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain();
            obtain.setPackageName(COUINumberPicker.this.getContext().getPackageName());
            obtain.setParent(COUINumberPicker.this);
            obtain.setSource(COUINumberPicker.this);
            if (!TextUtils.isEmpty(COUINumberPicker.this.J0)) {
                str = str + COUINumberPicker.this.J0;
            }
            obtain.setText(str);
            obtain.setClickable(true);
            obtain.setLongClickable(true);
            obtain.setEnabled(COUINumberPicker.this.isEnabled());
            obtain.setScrollable(true);
            if (this.f6828c != 2) {
                obtain.addAction(64);
            }
            if (this.f6828c == 2) {
                obtain.addAction(128);
            }
            if (COUINumberPicker.this.isEnabled()) {
                obtain.addAction(16);
            }
            Rect rect = this.f6826a;
            rect.set(i10, i11, i12, i13);
            obtain.setBoundsInParent(rect);
            int[] iArr = this.f6827b;
            COUINumberPicker.this.getLocationOnScreen(iArr);
            rect.offset(iArr[0], iArr[1]);
            obtain.setBoundsInScreen(rect);
            return obtain;
        }

        private void c(String str, int i10, List<AccessibilityNodeInfo> list) {
            if (i10 == 1) {
                String d10 = d(COUINumberPicker.this.f6818w + 1);
                if (TextUtils.isEmpty(d10) || !d10.toString().toLowerCase().contains(str)) {
                    return;
                }
                list.add(createAccessibilityNodeInfo(1));
                return;
            }
            if (i10 != 3) {
                return;
            }
            String d11 = d(COUINumberPicker.this.f6818w - 1);
            if (TextUtils.isEmpty(d11) || !d11.toString().toLowerCase().contains(str)) {
                return;
            }
            list.add(createAccessibilityNodeInfo(3));
        }

        private String d(int i10) {
            if (COUINumberPicker.this.C) {
                i10 = COUINumberPicker.this.N(i10);
            }
            if (i10 > COUINumberPicker.this.f6816v || i10 < COUINumberPicker.this.f6814u) {
                return null;
            }
            return COUINumberPicker.this.f6812t == null ? COUINumberPicker.this.F(i10) : COUINumberPicker.this.f6812t[i10 - COUINumberPicker.this.f6814u];
        }

        private boolean e() {
            return COUINumberPicker.this.getWrapSelectorWheel() || COUINumberPicker.this.getValue() > COUINumberPicker.this.getMinValue();
        }

        private boolean f() {
            return COUINumberPicker.this.getWrapSelectorWheel() || COUINumberPicker.this.getValue() < COUINumberPicker.this.getMaxValue();
        }

        private void g(int i10, int i11, String str) {
            if (COUINumberPicker.this.f6768e0.isEnabled()) {
                AccessibilityEvent obtain = AccessibilityEvent.obtain(i11);
                obtain.setPackageName(COUINumberPicker.this.getContext().getPackageName());
                obtain.getText().add(str);
                obtain.setEnabled(COUINumberPicker.this.isEnabled());
                obtain.setSource(COUINumberPicker.this, i10);
                COUINumberPicker cOUINumberPicker = COUINumberPicker.this;
                cOUINumberPicker.requestSendAccessibilityEvent(cOUINumberPicker, obtain);
            }
        }

        private void h(int i10, String str) {
            if (COUINumberPicker.this.f6768e0.isEnabled()) {
                AccessibilityEvent obtain = AccessibilityEvent.obtain(i10);
                obtain.setPackageName(COUINumberPicker.this.getContext().getPackageName());
                obtain.getText().add(str);
                obtain.setEnabled(COUINumberPicker.this.isEnabled());
                obtain.setSource(COUINumberPicker.this, 2);
                COUINumberPicker cOUINumberPicker = COUINumberPicker.this;
                cOUINumberPicker.requestSendAccessibilityEvent(cOUINumberPicker, obtain);
            }
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public AccessibilityNodeInfo createAccessibilityNodeInfo(int i10) {
            if (i10 == -1) {
                return b(d(COUINumberPicker.this.f6818w), COUINumberPicker.this.getScrollX(), COUINumberPicker.this.getScrollY(), COUINumberPicker.this.getScrollX() + (COUINumberPicker.this.getRight() - COUINumberPicker.this.getLeft()), COUINumberPicker.this.getScrollY() + (COUINumberPicker.this.getBottom() - COUINumberPicker.this.getTop()));
            }
            if (i10 == 1) {
                return a(1, d(COUINumberPicker.this.f6818w + 1), COUINumberPicker.this.getScrollX(), COUINumberPicker.this.V, COUINumberPicker.this.getScrollX() + (COUINumberPicker.this.getRight() - COUINumberPicker.this.getLeft()), COUINumberPicker.this.getScrollY() + (COUINumberPicker.this.getBottom() - COUINumberPicker.this.getTop()));
            }
            if (i10 == 2) {
                return b(d(COUINumberPicker.this.f6818w), COUINumberPicker.this.getScrollX(), COUINumberPicker.this.U, COUINumberPicker.this.getScrollX() + (COUINumberPicker.this.getRight() - COUINumberPicker.this.getLeft()), COUINumberPicker.this.V);
            }
            if (i10 != 3) {
                return super.createAccessibilityNodeInfo(i10);
            }
            return a(3, d(COUINumberPicker.this.f6818w - 1), COUINumberPicker.this.getScrollX(), COUINumberPicker.this.getScrollY(), COUINumberPicker.this.getScrollX() + (COUINumberPicker.this.getRight() - COUINumberPicker.this.getLeft()), COUINumberPicker.this.U);
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String str, int i10) {
            if (TextUtils.isEmpty(str)) {
                return Collections.emptyList();
            }
            String lowerCase = str.toLowerCase();
            ArrayList arrayList = new ArrayList();
            if (i10 == -1) {
                c(lowerCase, 3, arrayList);
                c(lowerCase, 2, arrayList);
                c(lowerCase, 1, arrayList);
                return arrayList;
            }
            if (i10 != 1 && i10 != 2 && i10 != 3) {
                return super.findAccessibilityNodeInfosByText(str, i10);
            }
            c(lowerCase, i10, arrayList);
            return arrayList;
        }

        void i(int i10, int i11) {
            if (i10 == 1) {
                if (f()) {
                    g(i10, i11, d(COUINumberPicker.this.f6818w + 1));
                }
            } else {
                if (i10 != 2) {
                    if (i10 == 3 && e()) {
                        g(i10, i11, d(COUINumberPicker.this.f6818w - 1));
                        return;
                    }
                    return;
                }
                h(i11, d(COUINumberPicker.this.f6818w));
            }
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public boolean performAction(int i10, int i11, Bundle bundle) {
            if (i10 != -1) {
                if (i10 == 1) {
                    if (i11 == 16) {
                        if (!COUINumberPicker.this.isEnabled()) {
                            return false;
                        }
                        COUINumberPicker.this.y(true);
                        i(i10, 1);
                        return true;
                    }
                    if (i11 != 64) {
                        if (i11 != 128 || this.f6828c != i10) {
                            return false;
                        }
                        this.f6828c = Integer.MIN_VALUE;
                        i(i10, 65536);
                        COUINumberPicker cOUINumberPicker = COUINumberPicker.this;
                        cOUINumberPicker.invalidate(0, cOUINumberPicker.V, COUINumberPicker.this.getRight(), COUINumberPicker.this.getBottom());
                        return true;
                    }
                    if (this.f6828c == i10) {
                        return false;
                    }
                    this.f6828c = i10;
                    i(i10, 32768);
                    COUINumberPicker cOUINumberPicker2 = COUINumberPicker.this;
                    cOUINumberPicker2.invalidate(0, cOUINumberPicker2.V, COUINumberPicker.this.getRight(), COUINumberPicker.this.getBottom());
                    return true;
                }
                if (i10 == 2) {
                    if (i11 == 16) {
                        if (!COUINumberPicker.this.isEnabled()) {
                            return false;
                        }
                        COUINumberPicker.this.performClick();
                        return true;
                    }
                    if (i11 == 32) {
                        if (!COUINumberPicker.this.isEnabled()) {
                            return false;
                        }
                        COUINumberPicker.this.performLongClick();
                        return true;
                    }
                    if (i11 != 64) {
                        if (i11 != 128 || this.f6828c != i10) {
                            return false;
                        }
                        this.f6828c = Integer.MIN_VALUE;
                        i(i10, 65536);
                        return true;
                    }
                    if (this.f6828c == i10) {
                        return false;
                    }
                    this.f6828c = i10;
                    i(i10, 32768);
                    COUINumberPicker cOUINumberPicker3 = COUINumberPicker.this;
                    cOUINumberPicker3.invalidate(0, 0, cOUINumberPicker3.getRight(), COUINumberPicker.this.U);
                    return true;
                }
                if (i10 == 3) {
                    if (i11 == 16) {
                        if (!COUINumberPicker.this.isEnabled()) {
                            return false;
                        }
                        COUINumberPicker.this.y(i10 == 1);
                        i(i10, 1);
                        return true;
                    }
                    if (i11 != 64) {
                        if (i11 != 128 || this.f6828c != i10) {
                            return false;
                        }
                        this.f6828c = Integer.MIN_VALUE;
                        i(i10, 65536);
                        COUINumberPicker cOUINumberPicker4 = COUINumberPicker.this;
                        cOUINumberPicker4.invalidate(0, 0, cOUINumberPicker4.getRight(), COUINumberPicker.this.U);
                        return true;
                    }
                    if (this.f6828c == i10) {
                        return false;
                    }
                    this.f6828c = i10;
                    i(i10, 32768);
                    COUINumberPicker cOUINumberPicker5 = COUINumberPicker.this;
                    cOUINumberPicker5.invalidate(0, 0, cOUINumberPicker5.getRight(), COUINumberPicker.this.U);
                    return true;
                }
            } else {
                if (i11 == 64) {
                    if (this.f6828c == i10) {
                        return false;
                    }
                    this.f6828c = i10;
                    return true;
                }
                if (i11 == 128) {
                    if (this.f6828c != i10) {
                        return false;
                    }
                    this.f6828c = Integer.MIN_VALUE;
                    return true;
                }
                if (i11 == 4096) {
                    if (!COUINumberPicker.this.isEnabled()) {
                        return false;
                    }
                    COUINumberPicker.this.y(true);
                    return true;
                }
                if (i11 == 8192) {
                    if (!COUINumberPicker.this.isEnabled()) {
                        return false;
                    }
                    COUINumberPicker.this.y(false);
                    return true;
                }
            }
            return super.performAction(i10, i11, bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private boolean f6830e;

        b() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void b(boolean z10) {
            this.f6830e = z10;
        }

        @Override // java.lang.Runnable
        public void run() {
            COUINumberPicker.this.y(this.f6830e);
            COUINumberPicker cOUINumberPicker = COUINumberPicker.this;
            cOUINumberPicker.postDelayed(this, cOUINumberPicker.E);
        }
    }

    /* loaded from: classes.dex */
    public interface c {
        String a(int i10);
    }

    /* loaded from: classes.dex */
    public interface d {
        void a(COUINumberPicker cOUINumberPicker, int i10);
    }

    /* loaded from: classes.dex */
    public interface e {
        void a();
    }

    /* loaded from: classes.dex */
    public interface f {
        void a(COUINumberPicker cOUINumberPicker, int i10, int i11);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class g implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private final int f6832e = 1;

        /* renamed from: f, reason: collision with root package name */
        private final int f6833f = 2;

        /* renamed from: g, reason: collision with root package name */
        private int f6834g;

        /* renamed from: h, reason: collision with root package name */
        private int f6835h;

        g() {
        }

        public void a(int i10) {
            c();
            this.f6835h = 1;
            this.f6834g = i10;
            COUINumberPicker.this.postDelayed(this, ViewConfiguration.getTapTimeout());
        }

        public void b(int i10) {
            c();
            this.f6835h = 2;
            this.f6834g = i10;
            COUINumberPicker.this.post(this);
        }

        public void c() {
            this.f6835h = 0;
            this.f6834g = 0;
            COUINumberPicker.this.removeCallbacks(this);
            if (COUINumberPicker.this.f6759a0) {
                COUINumberPicker.this.f6759a0 = false;
                COUINumberPicker cOUINumberPicker = COUINumberPicker.this;
                cOUINumberPicker.invalidate(0, cOUINumberPicker.V, COUINumberPicker.this.getRight(), COUINumberPicker.this.getBottom());
            }
            COUINumberPicker.this.f6761b0 = false;
            if (COUINumberPicker.this.f6761b0) {
                COUINumberPicker cOUINumberPicker2 = COUINumberPicker.this;
                cOUINumberPicker2.invalidate(0, 0, cOUINumberPicker2.getRight(), COUINumberPicker.this.U);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            int i10 = this.f6835h;
            if (i10 == 1) {
                int i11 = this.f6834g;
                if (i11 == 1) {
                    COUINumberPicker.this.f6759a0 = true;
                    COUINumberPicker cOUINumberPicker = COUINumberPicker.this;
                    cOUINumberPicker.invalidate(0, cOUINumberPicker.V, COUINumberPicker.this.getRight(), COUINumberPicker.this.getBottom());
                    return;
                } else {
                    if (i11 != 2) {
                        return;
                    }
                    COUINumberPicker.this.f6761b0 = true;
                    COUINumberPicker cOUINumberPicker2 = COUINumberPicker.this;
                    cOUINumberPicker2.invalidate(0, 0, cOUINumberPicker2.getRight(), COUINumberPicker.this.U);
                    return;
                }
            }
            if (i10 != 2) {
                return;
            }
            int i12 = this.f6834g;
            if (i12 == 1) {
                if (!COUINumberPicker.this.f6759a0) {
                    COUINumberPicker.this.postDelayed(this, ViewConfiguration.getPressedStateDuration());
                }
                COUINumberPicker.k(COUINumberPicker.this, 1);
                COUINumberPicker cOUINumberPicker3 = COUINumberPicker.this;
                cOUINumberPicker3.invalidate(0, cOUINumberPicker3.V, COUINumberPicker.this.getRight(), COUINumberPicker.this.getBottom());
                return;
            }
            if (i12 != 2) {
                return;
            }
            if (!COUINumberPicker.this.f6761b0) {
                COUINumberPicker.this.postDelayed(this, ViewConfiguration.getPressedStateDuration());
            }
            COUINumberPicker.q(COUINumberPicker.this, 1);
            COUINumberPicker cOUINumberPicker4 = COUINumberPicker.this;
            cOUINumberPicker4.invalidate(0, 0, cOUINumberPicker4.getRight(), COUINumberPicker.this.U);
        }
    }

    /* loaded from: classes.dex */
    private class h extends Handler {
        h(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i10 = message.what;
            if (i10 == 0) {
                COUINumberPicker.this.g0();
            } else if (i10 == 1) {
                String str = (String) COUINumberPicker.this.f6782j.get(((Integer) message.obj).intValue());
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                if (!TextUtils.isEmpty(COUINumberPicker.this.J0)) {
                    str = str + COUINumberPicker.this.J0;
                }
                if (COUINumberPicker.this.S == 0) {
                    COUINumberPicker.this.announceForAccessibility(str);
                    if (COUINumberPicker.this.f6822y != null) {
                        COUINumberPicker.this.f6822y.a();
                    }
                }
            }
            super.handleMessage(message);
        }
    }

    /* loaded from: classes.dex */
    private class i implements c {

        /* renamed from: a, reason: collision with root package name */
        final StringBuilder f6838a = new StringBuilder();

        /* renamed from: b, reason: collision with root package name */
        final Object[] f6839b = new Object[1];

        /* renamed from: c, reason: collision with root package name */
        Formatter f6840c;

        /* renamed from: d, reason: collision with root package name */
        DecimalFormat f6841d;

        i() {
            b(Locale.getDefault());
        }

        private void b(Locale locale) {
            this.f6840c = new Formatter(this.f6838a, locale);
            this.f6841d = new DecimalFormat("00");
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.c
        public String a(int i10) {
            this.f6839b[0] = Integer.valueOf(i10);
            StringBuilder sb2 = this.f6838a;
            sb2.delete(0, sb2.length());
            return this.f6841d.format(i10);
        }
    }

    public COUINumberPicker(Context context) {
        this(context, null);
    }

    private float A(float f10) {
        return this.f6793m1 * 386.0878f * f10;
    }

    private void B(int[] iArr) {
        for (int i10 = 0; i10 < iArr.length; i10++) {
            iArr[i10] = O(iArr[i10], -1);
        }
        C(iArr[0]);
    }

    private void C(int i10) {
        String str;
        SparseArray<String> sparseArray = this.f6782j;
        if (sparseArray.get(i10) != null) {
            return;
        }
        int i11 = this.f6814u;
        if (i10 < i11 || i10 > this.f6816v) {
            str = "";
        } else {
            String[] strArr = this.f6812t;
            if (strArr != null) {
                str = strArr[i10 - i11];
            } else {
                str = F(i10);
            }
        }
        sparseArray.put(i10, str);
    }

    private boolean D() {
        int i10 = this.H - this.I;
        if (i10 == 0) {
            return false;
        }
        this.J = 0;
        L(this.f6787k1);
        Math.signum(this.f6787k1);
        M(this.f6787k1);
        float abs = Math.abs(i10);
        int i11 = this.G;
        float f10 = this.I0;
        if (abs > (i11 + f10) / 2.0f) {
            i10 = (int) (i10 + (i10 > 0 ? (-i11) - f10 : i11 + f10));
        }
        this.f6797o.startScroll(0, 0, 0, i10, 300);
        invalidate();
        return true;
    }

    private void E(int i10) {
        double d10;
        this.f6787k1 = i10;
        this.J = 0;
        double L = L(i10);
        double d11 = L > ((double) (((float) this.G) + this.I0)) ? L - (L % (r3 + r5)) : L % (r3 + r5);
        double d12 = d11 + this.f6790l1;
        if (i10 < 0) {
            d10 = -(d12 + ((this.I - r4) % (r3 + r5)));
        } else {
            d10 = d12 - ((this.I + r4) % (r3 + r5));
        }
        this.f6794n.startScroll(0, 0, 0, (int) d10, (int) (M(r0) * 1.5f));
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String F(int i10) {
        c cVar = this.D;
        return cVar != null ? cVar.a(i10) : G(i10);
    }

    private static String G(int i10) {
        return String.format(Locale.getDefault(), "%d", Integer.valueOf(i10));
    }

    private String H(StackTraceElement[] stackTraceElementArr, int i10) {
        int i11 = i10 + 4;
        if (i11 >= stackTraceElementArr.length) {
            return "<bottom of call stack>";
        }
        StackTraceElement stackTraceElement = stackTraceElementArr[i11];
        return stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber();
    }

    private String I(int i10) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i11 = 0; i11 < i10; i11++) {
            stringBuffer.append(H(stackTrace, i11));
            stringBuffer.append(" ");
        }
        return stringBuffer.toString();
    }

    private int J(int i10) {
        return Math.abs((i10 - this.H) - (this.f6786k0 * this.G)) / this.G;
    }

    private double K(float f10) {
        return Math.log((Math.abs(f10) * 0.35f) / (this.f6767e * this.f6796n1));
    }

    private double L(float f10) {
        double K = K(f10);
        float f11 = f6758v1;
        return this.f6767e * this.f6796n1 * Math.exp((f11 / (f11 - 1.0d)) * K);
    }

    private int M(float f10) {
        return (int) (Math.exp(K(f10) / (f6758v1 - 1.0d)) * 1000.0d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int N(int i10) {
        return O(i10, 0);
    }

    private int O(int i10, int i11) {
        int i12 = this.f6816v;
        int i13 = this.f6814u;
        if (i12 - i13 <= 0) {
            return -1;
        }
        if (i10 == Integer.MIN_VALUE) {
            i10 = i13 - 1;
        }
        int b10 = COUIMathUtils.b((i10 - i13) + i11, (i12 - i13) + 1 + (this.L0 ? 1 : 0));
        int i14 = this.f6816v;
        int i15 = this.f6814u;
        if (b10 < (i14 - i15) + 1) {
            return i15 + b10;
        }
        return Integer.MIN_VALUE;
    }

    private int P(int i10, int i11, float f10) {
        return i11 - ((int) (((i11 - i10) * 2) * f10));
    }

    private float Q(int i10, int i11, int i12, int i13, int i14) {
        int i15 = this.H;
        int i16 = this.f6786k0 - 1;
        int i17 = this.G;
        int i18 = (i16 * i17) + i15;
        int length = ((this.F.length - 3) * i17) + i15;
        double d10 = i14;
        double d11 = i18;
        if (d10 <= d11 - (i17 * 0.5d) || d10 >= d11 + (i17 * 0.5d)) {
            return i14 <= i18 - i17 ? i12 + (((((i13 - i12) * 1.0f) * (i14 - i15)) / i17) / 2.0f) : i14 >= i18 + i17 ? i12 + (((((i13 - i12) * 1.0f) * (length - i14)) / i17) / 2.0f) : i13;
        }
        return i11 - ((((i11 - i10) * 2.0f) * Math.abs(i14 - i18)) / this.G);
    }

    private void R(int[] iArr) {
        for (int i10 = 0; i10 < iArr.length; i10++) {
            iArr[i10] = O(iArr[i10], 1);
        }
        C(iArr[iArr.length - 1]);
    }

    private void S() {
        int i10 = this.H;
        int i11 = this.G;
        int i12 = this.f6786k0;
        this.f6792m0 = (int) (i10 + (i11 * (i12 - 0.5d)));
        this.f6795n0 = (int) (i10 + (i11 * (i12 + 0.5d)));
    }

    private void T() {
        VelocityTracker velocityTracker = this.O;
        if (velocityTracker == null) {
            this.O = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void U() {
        if (this.O == null) {
            this.O = VelocityTracker.obtain();
        }
    }

    private void V() {
        setVerticalFadingEdgeEnabled(this.f6764c1);
        setFadingEdgeLength(((getBottom() - getTop()) - this.f6825z0) / 2);
    }

    private void W() {
        X();
        int[] iArr = this.F;
        int max = (int) ((Math.max(0, ((getBottom() - getTop()) - ((iArr.length - 2) * this.f6825z0)) - this.W0) / (iArr.length - 2)) + 0.5f);
        this.f6809s = max;
        this.G = this.f6825z0 + max;
        this.H = 0;
        this.I = 0;
        this.U = (getHeight() / 2) - (this.G / 2);
        this.V = (getHeight() / 2) + (this.G / 2);
    }

    private void X() {
        this.f6782j.clear();
        int[] iArr = this.F;
        int value = getValue();
        for (int i10 = 0; i10 < this.F.length; i10++) {
            int i11 = i10 - this.f6786k0;
            int O = this.L0 ? O(value, i11) : i11 + value;
            if (this.C) {
                O = N(O);
            }
            iArr[i10] = O;
            C(iArr[i10]);
        }
    }

    private int Z(int i10, int i11) {
        if (i11 == -1) {
            return i10;
        }
        int size = View.MeasureSpec.getSize(i10);
        int mode = View.MeasureSpec.getMode(i10);
        if (mode != Integer.MIN_VALUE) {
            if (mode == 0) {
                return View.MeasureSpec.makeMeasureSpec(i11, 1073741824);
            }
            if (mode == 1073741824) {
                return i10;
            }
            throw new IllegalArgumentException("Unknown measure mode: " + mode);
        }
        String str = this.K0;
        if (str != null) {
            float measureText = this.f6788l.measureText(str);
            int i12 = this.X0;
            if (measureText > i12) {
                i12 = (int) this.f6788l.measureText(this.K0);
            }
            int i13 = this.Z0;
            size = i12 + (i13 - this.X0) + i13 + this.Y0;
        }
        return View.MeasureSpec.makeMeasureSpec(Math.min(size, i11), 1073741824);
    }

    private boolean a0(Scroller scroller) {
        scroller.forceFinished(true);
        int finalY = scroller.getFinalY() - scroller.getCurrY();
        int i10 = this.H - ((this.I + finalY) % this.G);
        if (i10 == 0) {
            return false;
        }
        int abs = Math.abs(i10);
        int i11 = this.G;
        if (abs > i11 / 2) {
            i10 = i10 > 0 ? i10 - i11 : i10 + i11;
        }
        scrollBy(0, finalY + i10);
        return true;
    }

    private void b0(int i10, int i11) {
        f fVar = this.f6820x;
        if (fVar != null) {
            fVar.a(this, i10, this.f6818w);
        }
    }

    private void c0(int i10) {
        if (this.S == i10) {
            return;
        }
        this.S = i10;
        d dVar = this.f6824z;
        if (dVar != null) {
            dVar.a(this, i10);
        }
        if (this.S == 0) {
            announceForAccessibility(this.f6782j.get(getValue()));
            e eVar = this.f6822y;
            if (eVar != null) {
                eVar.a();
            }
        }
    }

    private void d0(Scroller scroller) {
        if (scroller == this.f6794n) {
            D();
            c0(0);
        }
    }

    private boolean e0() {
        int abs;
        if (this.f6778h1 == null) {
            LinearmotorVibrator e10 = VibrateUtils.e(getContext());
            this.f6778h1 = e10;
            this.f6772f1 = e10 != null;
        }
        if (this.f6778h1 == null) {
            return false;
        }
        VelocityTracker velocityTracker = this.O;
        if (velocityTracker != null) {
            velocityTracker.computeCurrentVelocity(1000, this.R);
            abs = (int) Math.abs(this.O.getYVelocity());
        } else {
            abs = Math.abs(this.f6811s1);
        }
        int i10 = abs;
        VibrateUtils.k((LinearmotorVibrator) this.f6778h1, i10 > 2000 ? 0 : 1, i10, this.R, 1200, 1600, this.f6802p1, this.f6805q1);
        return true;
    }

    private void f0() {
        if ((this.f6772f1 && this.f6769e1 && e0()) || performHapticFeedback(308)) {
            return;
        }
        performHapticFeedback(302);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g0() {
        this.f6771f0.d(getContext(), this.f6789l0, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    private float getDampRatio() {
        return Math.min(1.8f, 1.6f);
    }

    private void h0(boolean z10, long j10) {
        b bVar = this.K;
        if (bVar == null) {
            this.K = new b();
        } else {
            removeCallbacks(bVar);
        }
        this.K.b(z10);
        postDelayed(this.K, j10);
    }

    private void i0() {
        VelocityTracker velocityTracker = this.O;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.O = null;
        }
    }

    private void j0() {
        b bVar = this.K;
        if (bVar != null) {
            removeCallbacks(bVar);
        }
        this.f6800p.c();
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [boolean, byte] */
    static /* synthetic */ boolean k(COUINumberPicker cOUINumberPicker, int i10) {
        ?? r22 = (byte) (i10 ^ (cOUINumberPicker.f6759a0 ? 1 : 0));
        cOUINumberPicker.f6759a0 = r22;
        return r22;
    }

    private void k0() {
        b bVar = this.K;
        if (bVar != null) {
            removeCallbacks(bVar);
        }
    }

    private int l0(int i10, int i11, int i12) {
        return i10 != -1 ? LinearLayout.resolveSizeAndState(Math.max(i10, i11), i12, 0) : i11;
    }

    private void p0(int i10, boolean z10) {
        int min;
        if (this.f6784j1 == -1) {
            this.f6784j1 = System.currentTimeMillis();
            this.f6781i1 = 0;
        } else if (System.currentTimeMillis() - this.f6784j1 < 1000) {
            int i11 = this.f6781i1 + 1;
            this.f6781i1 = i11;
            if (i11 >= 100) {
                this.f6781i1 = 0;
                Log.d("COUINumberPicker", I(30) + "\nmCurrentScrollOffset = " + this.I + " ,mInitialScrollOffset = " + this.H + " ,mSelectorTextGapHeight = " + this.f6809s + " ,mSelectorElementHeight = " + this.G + " ,mSelectorMiddleItemIndex = " + this.f6786k0 + " ,mWrapSelectorWheel = " + this.C + " ,mDebugY = " + this.f6799o1 + " ,mMinValue = " + this.f6814u);
            }
        } else {
            this.f6784j1 = -1L;
        }
        Log.d("COUINumberPicker", "setValueInternal current = " + i10);
        if (this.f6818w == i10) {
            X();
            return;
        }
        if (this.C) {
            min = N(i10);
        } else {
            min = Math.min(Math.max(i10, this.f6814u), this.f6816v);
        }
        int i12 = this.f6818w;
        this.f6818w = min;
        if (z10) {
            b0(i12, min);
            f0();
            this.f6777h0.removeMessages(0);
            this.f6777h0.sendEmptyMessage(0);
            AccessibilityManager accessibilityManager = this.f6768e0;
            if (accessibilityManager != null && accessibilityManager.isEnabled()) {
                Message message = new Message();
                message.what = 1;
                message.obj = Integer.valueOf(min);
                this.f6777h0.removeMessages(1);
                this.f6777h0.sendMessageDelayed(message, 300L);
            }
        }
        X();
        invalidate();
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [boolean, byte] */
    static /* synthetic */ boolean q(COUINumberPicker cOUINumberPicker, int i10) {
        ?? r22 = (byte) (i10 ^ (cOUINumberPicker.f6761b0 ? 1 : 0));
        cOUINumberPicker.f6761b0 = r22;
        return r22;
    }

    private void q0() {
        this.C = (this.f6816v - this.f6814u >= this.F.length + (-2)) && this.B;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void y(boolean z10) {
        if (!a0(this.f6794n)) {
            a0(this.f6797o);
        }
        this.J = 0;
        if (z10) {
            this.f6794n.startScroll(0, 0, 0, (int) ((-this.G) - this.I0), 300);
        } else {
            this.f6794n.startScroll(0, 0, 0, (int) (this.G + this.I0), 300);
        }
        invalidate();
    }

    public boolean Y() {
        AccessibilityManager accessibilityManager = this.f6768e0;
        return accessibilityManager != null && accessibilityManager.isEnabled();
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.f6794n.isFinished()) {
            if (this.f6797o.isFinished()) {
                this.f6811s1 = 0;
                return;
            }
            this.f6797o.computeScrollOffset();
            int currY = this.f6797o.getCurrY();
            if (this.J == 0) {
                this.J = this.f6797o.getStartY();
            }
            scrollBy(0, currY - this.J);
            this.J = currY;
            if (this.f6797o.isFinished()) {
                return;
            }
            invalidate();
            return;
        }
        this.f6794n.computeScrollOffset();
        int currY2 = this.f6794n.getCurrY();
        if (this.J == 0) {
            this.J = this.f6794n.getStartY();
        }
        int uptimeMillis = (int) (SystemClock.uptimeMillis() - this.f6808r1);
        int abs = Math.abs(currY2 - this.J);
        if (uptimeMillis != 0) {
            this.f6811s1 = Math.min(this.R, (int) (((abs * 1.0f) / uptimeMillis) * 1000.0f));
        }
        scrollBy(0, currY2 - this.J);
        this.J = currY2;
        this.f6808r1 = (int) SystemClock.uptimeMillis();
        if (this.f6794n.isFinished()) {
            d0(this.f6794n);
        } else {
            invalidate();
        }
    }

    @Override // android.view.View
    protected int computeVerticalScrollExtent() {
        return getHeight();
    }

    @Override // android.view.View
    protected int computeVerticalScrollOffset() {
        return this.I;
    }

    @Override // android.view.View
    protected int computeVerticalScrollRange() {
        return ((this.f6816v - this.f6814u) + 1) * this.G;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        int i10;
        if (!this.f6768e0.isEnabled()) {
            return false;
        }
        int y4 = (int) motionEvent.getY();
        if (y4 < this.U) {
            i10 = 3;
        } else {
            i10 = y4 > this.V ? 1 : 2;
        }
        int actionMasked = motionEvent.getActionMasked();
        a aVar = (a) getAccessibilityNodeProvider();
        if (actionMasked == 7) {
            int i11 = this.W;
            if (i11 == i10 || i11 == -1) {
                return false;
            }
            aVar.i(i11, 256);
            aVar.i(i10, 128);
            this.W = i10;
            aVar.performAction(i10, 64, null);
            return false;
        }
        if (actionMasked == 9) {
            aVar.i(i10, 128);
            this.W = i10;
            aVar.performAction(i10, 64, null);
            return false;
        }
        if (actionMasked != 10) {
            return false;
        }
        aVar.i(i10, 256);
        this.W = -1;
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == 19 || keyCode == 20) {
            int action = keyEvent.getAction();
            if (action != 0) {
                if (action == 1 && this.f6765d0 == keyCode) {
                    this.f6765d0 = -1;
                    return true;
                }
            } else {
                if (!this.C) {
                    if (keyCode == 20) {
                    }
                }
                requestFocus();
                this.f6765d0 = keyCode;
                j0();
                if (this.f6794n.isFinished()) {
                    y(keyCode == 20);
                }
                return true;
            }
        } else if (keyCode == 23 || keyCode == 66) {
            j0();
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        Log.d("COUINumberPicker", "dispatchTouchEvent event = " + motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 3) {
            j0();
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 3) {
            j0();
        }
        return super.dispatchTrackballEvent(motionEvent);
    }

    @Override // android.view.View
    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        if (this.f6763c0 == null) {
            this.f6763c0 = new a();
        }
        return this.f6763c0;
    }

    public int getBackgroundColor() {
        return this.T0;
    }

    @Override // android.view.View
    protected float getBottomFadingEdgeStrength() {
        return 0.9f;
    }

    public String[] getDisplayedValues() {
        return this.f6812t;
    }

    public int getMaxValue() {
        return this.f6816v;
    }

    public int getMinValue() {
        return this.f6814u;
    }

    public int getNumberPickerPaddingLeft() {
        return this.C0;
    }

    public int getNumberPickerPaddingRight() {
        return this.D0;
    }

    public Paint getSelectorTextPaint() {
        return this.f6785k;
    }

    public float getTextSize() {
        return this.f6785k.getTextSize();
    }

    @Override // android.view.View
    protected float getTopFadingEdgeStrength() {
        return 0.9f;
    }

    public int getTouchEffectInterval() {
        return this.f6780i0;
    }

    public int getValue() {
        return this.f6818w;
    }

    public boolean getWrapSelectorWheel() {
        return this.C;
    }

    public boolean isLayoutRtl() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
    }

    public void m0(int i10, int i11) {
        this.f6798o0 = Color.alpha(i10);
        this.f6810s0 = Color.alpha(i11);
        this.f6801p0 = Color.red(i10);
        this.f6813t0 = Color.red(i11);
        this.f6804q0 = Color.green(i10);
        this.f6815u0 = Color.green(i11);
        this.f6807r0 = Color.blue(i10);
        this.f6817v0 = Color.blue(i11);
    }

    public void n0(int i10, int i11) {
        m0(i10, i11);
        invalidate();
    }

    public void o0() {
        if (this.A == null) {
            this.A = new i();
        }
        this.D = this.A;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        HandlerThread handlerThread = new HandlerThread("touchEffect", -16);
        this.f6774g0 = handlerThread;
        handlerThread.start();
        if (this.f6774g0.getLooper() != null) {
            this.f6777h0 = new h(this.f6774g0.getLooper());
        }
        VibrateUtils.i(getContext());
        T();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        j0();
        HandlerThread handlerThread = this.f6774g0;
        if (handlerThread != null) {
            handlerThread.quit();
            this.f6774g0 = null;
        }
        Handler handler = this.f6777h0;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        VibrateUtils.l();
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onDraw(Canvas canvas) {
        float f10;
        int i10;
        if (this.f6766d1) {
            canvas.drawRect(this.V0, (int) (((getHeight() / 2.0f) - this.U0) - this.I0), getWidth() - this.V0, r0 + this.E0, this.f6775g1);
            canvas.drawRect(this.V0, (int) ((getHeight() / 2.0f) + this.U0 + this.I0), getWidth() - this.V0, r0 + this.E0, this.f6775g1);
        }
        float right = (((getRight() - getLeft()) - this.C0) - this.D0) / 2.0f;
        if (this.K0 != null) {
            right = this.f6760a1 + (this.Y0 / 2.0f);
            if (isLayoutRtl()) {
                right = ((getMeasuredWidth() - right) - this.D0) - this.C0;
            }
        }
        int i11 = this.I;
        int i12 = this.f6819w0;
        boolean z10 = true;
        if (i12 != -1 && i12 < getRight() - getLeft()) {
            int i13 = this.f6821x0;
            if (i13 == 1) {
                i10 = this.f6819w0 / 2;
            } else if (i13 == 2) {
                int right2 = getRight() - getLeft();
                int i14 = this.f6819w0;
                i10 = (right2 - i14) + (i14 / 2);
            }
            right = i10;
        }
        int i15 = this.C0;
        if (i15 != 0) {
            right += i15;
        }
        float f11 = right;
        int[] iArr = this.F;
        boolean z11 = false;
        int i16 = i11 - this.G;
        float f12 = f11;
        int i17 = 0;
        float f13 = 0.0f;
        while (i17 < iArr.length) {
            int i18 = iArr[i17];
            if (i16 > this.f6792m0 && i16 < this.f6795n0) {
                float J = J(i16);
                P(this.f6798o0, this.f6810s0, J);
                P(this.f6801p0, this.f6813t0, J);
                P(this.f6804q0, this.f6815u0, J);
                P(this.f6807r0, this.f6817v0, J);
            }
            int argb = Color.argb(this.f6798o0, this.f6801p0, this.f6804q0, this.f6807r0);
            int argb2 = Color.argb(this.f6810s0, this.f6813t0, this.f6815u0, this.f6817v0);
            int i19 = this.f6825z0;
            int i20 = i17;
            float Q = Q(i19, this.f6823y0, i19, i19, i16);
            this.f6785k.setColor(argb);
            String str = this.f6782j.get(i18);
            this.f6785k.setTextSize(this.f6825z0);
            if (this.f6791m.measureText(str) >= getMeasuredWidth()) {
                this.M0 = z10;
                this.f6785k.setTextAlign(Paint.Align.LEFT);
                f10 = 0.0f;
            } else {
                this.M0 = z11;
                this.f6785k.setTextAlign(Paint.Align.CENTER);
                f10 = f11;
            }
            if (i18 != Integer.MIN_VALUE) {
                int round = ((int) ((((((i16 + i16) + this.G) - this.G0) - this.H0) / 2.0f) + (this.W0 / 2) + (this.I0 * (i20 - Math.round((this.F.length / 2) - 0.01f))))) + this.F0;
                this.f6788l.setTextSize(this.f6825z0);
                Paint.FontMetrics fontMetrics = this.f6788l.getFontMetrics();
                int i21 = this.G;
                float f14 = (int) ((((i21 - fontMetrics.top) - fontMetrics.bottom) / 2.0f) + (this.W0 / 2) + i21);
                int save = canvas.save();
                canvas.clipOutRect(0.0f, ((getHeight() / 2.0f) - this.U0) - this.I0, getWidth(), (getHeight() / 2.0f) + this.U0 + this.I0);
                float f15 = round;
                canvas.drawText(str != null ? str : "", f10, f15, this.f6785k);
                canvas.restoreToCount(save);
                int save2 = canvas.save();
                canvas.clipRect(0.0f, ((getHeight() / 2.0f) - this.U0) - this.I0, getWidth(), (getHeight() / 2.0f) + this.U0 + this.I0);
                this.f6785k.setColor(argb2);
                this.f6785k.setTextSize(this.f6823y0);
                if (str == null) {
                    str = "";
                }
                canvas.drawText(str, f10, f15, this.f6785k);
                canvas.restoreToCount(save2);
                f13 = f14;
            } else {
                float f16 = Q / this.f6823y0;
                for (float f17 = -0.5f; f17 < 1.0f; f17 += 1.0f) {
                    float f18 = this.N0;
                    float f19 = (this.P0 + f18) * f17 * f16;
                    float f20 = this.O0 * f16;
                    float f21 = f19 + f10;
                    float f22 = (f18 * f16) / 2.0f;
                    float f23 = i16;
                    int i22 = this.G;
                    float f24 = f20 / 2.0f;
                    canvas.drawRect(f21 - f22, (((i22 / 2.0f) + f23) - f24) + 33.75f, f21 + f22, f23 + (i22 / 2.0f) + f24 + 33.75f, this.f6785k);
                }
            }
            i16 += this.G;
            i17 = i20 + 1;
            f12 = f10;
            z10 = true;
            z11 = false;
        }
        if (this.K0 != null) {
            if (isLayoutRtl()) {
                f12 = (f12 + this.D0) - this.C0;
            }
            float f25 = f12 + (this.Y0 / 2) + this.f6762b1;
            if (isLayoutRtl()) {
                f25 = (getMeasuredWidth() - f25) - this.f6788l.measureText(this.K0);
            }
            this.f6788l.setTextSize(this.A0);
            canvas.drawText(this.K0, f25, f13 - this.B0, this.f6788l);
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled() || motionEvent.getActionMasked() != 0) {
            return false;
        }
        j0();
        float y4 = motionEvent.getY();
        this.L = y4;
        this.N = y4;
        this.M = motionEvent.getEventTime();
        this.T = false;
        float f10 = this.L;
        if (f10 < this.U) {
            if (this.S == 0) {
                this.f6800p.a(2);
            }
        } else if (f10 > this.V && this.S == 0) {
            this.f6800p.a(1);
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        if (!this.f6794n.isFinished()) {
            this.f6794n.abortAnimation();
            this.f6797o.forceFinished(true);
            c0(0);
        } else if (!this.f6797o.isFinished()) {
            this.f6794n.abortAnimation();
            this.f6797o.forceFinished(true);
        } else {
            float f11 = this.L;
            if (f11 < this.U) {
                h0(false, ViewConfiguration.getLongPressTimeout());
            } else if (f11 > this.V) {
                h0(true, ViewConfiguration.getLongPressTimeout());
            } else {
                this.T = true;
            }
        }
        return true;
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        if (z10) {
            W();
            V();
        }
        S();
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        int Z = Z(i10, this.f6803q);
        super.onMeasure(Z, Z(i11, this.f6776h));
        if (View.MeasureSpec.getMode(Z) != Integer.MIN_VALUE) {
            this.f6760a1 = (getMeasuredWidth() - this.Y0) / 2;
        }
        int l02 = l0(this.f6779i, getMeasuredWidth(), i10) + this.D0 + this.C0;
        int i12 = this.f6806r;
        if (i12 > 0 && l02 > i12) {
            l02 = i12;
        }
        setMeasuredDimension(l02, l0(this.f6773g, getMeasuredHeight(), i11));
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            T();
            this.O.addMovement(motionEvent);
        } else if (actionMasked == 1) {
            k0();
            this.f6800p.c();
            int y4 = (int) motionEvent.getY();
            int abs = (int) Math.abs(y4 - this.L);
            this.O.computeCurrentVelocity(1000, this.R);
            int yVelocity = (int) this.O.getYVelocity();
            if (Math.abs(yVelocity) > this.Q) {
                E((int) (yVelocity * getDampRatio()));
                c0(2);
            } else {
                long eventTime = motionEvent.getEventTime() - this.M;
                if (abs <= this.P && eventTime < ViewConfiguration.getLongPressTimeout()) {
                    if (this.T) {
                        this.T = false;
                        performClick();
                    } else {
                        int i10 = ((y4 / this.G) - this.f6786k0) + 1;
                        if (i10 > 0) {
                            y(true);
                            this.f6800p.b(1);
                        } else if (i10 < 0) {
                            y(false);
                            this.f6800p.b(2);
                        }
                        D();
                    }
                } else {
                    D();
                }
                c0(0);
            }
            i0();
        } else if (actionMasked == 2) {
            U();
            this.O.addMovement(motionEvent);
            float y10 = motionEvent.getY();
            if (this.S != 1) {
                if (((int) Math.abs(y10 - this.L)) > this.P) {
                    j0();
                    c0(1);
                }
            } else {
                int i11 = (int) (y10 - this.N);
                this.f6790l1 = i11;
                scrollBy(0, i11);
                invalidate();
            }
            this.N = y10;
        } else if (actionMasked == 3) {
            D();
            i0();
        }
        return true;
    }

    @Override // android.view.View
    public void scrollBy(int i10, int i11) {
        int i12;
        int[] iArr = this.F;
        int i13 = this.I;
        boolean z10 = this.C;
        if (!z10 && i11 > 0 && iArr[this.f6786k0] <= this.f6814u && i13 + i11 >= 0) {
            this.I = this.H;
            return;
        }
        if (!z10 && i11 < 0 && iArr[this.f6786k0] >= this.f6816v && i13 + i11 <= 0) {
            this.I = this.H;
            return;
        }
        if (i11 > 65535) {
            this.f6799o1 = i11;
            return;
        }
        this.I = i11 + i13;
        while (true) {
            int i14 = this.I;
            float f10 = i14 - this.H;
            float f11 = this.f6809s + (this.W0 / 2);
            int i15 = this.G;
            float f12 = f11 + (i15 * 0.05f);
            float f13 = this.I0;
            if (f10 <= f12 + f13) {
                break;
            }
            this.I = (int) (i14 - (i15 + f13));
            B(iArr);
            p0(iArr[this.f6786k0], true);
            if (!this.C && iArr[this.f6786k0] < this.f6814u) {
                this.I = this.H;
            }
        }
        while (true) {
            i12 = this.I;
            float f14 = i12 - this.H;
            float f15 = (-this.f6809s) - (this.W0 / 2);
            int i16 = this.G;
            float f16 = f15 - (i16 * 0.05f);
            float f17 = this.I0;
            if (f14 >= f16 - f17) {
                break;
            }
            this.I = (int) (i12 + i16 + f17);
            R(iArr);
            p0(iArr[this.f6786k0], true);
            if (!this.C && iArr[this.f6786k0] > this.f6816v) {
                this.I = this.H;
            }
        }
        if (i13 != i12) {
            onScrollChanged(0, i12, 0, i13);
        }
    }

    public void setAlignPosition(int i10) {
        this.f6821x0 = i10;
    }

    public void setBackgroundRadius(int i10) {
        this.U0 = i10;
        invalidate();
    }

    public void setDiffusion(int i10) {
        this.I0 = i10;
        invalidate();
    }

    public void setDisplayedValues(String[] strArr) {
        if (this.f6812t == strArr) {
            return;
        }
        this.f6812t = strArr;
        X();
    }

    public void setDrawItemVerticalOffset(int i10) {
        this.F0 = i10;
        invalidate();
    }

    public void setEnableAdaptiveVibrator(boolean z10) {
        this.f6769e1 = z10;
    }

    public void setFocusTextSize(int i10) {
        this.f6823y0 = i10;
        invalidate();
    }

    public void setFormatter(c cVar) {
        if (cVar == this.D) {
            return;
        }
        this.D = cVar;
        X();
    }

    public void setHasBackground(boolean z10) {
        this.f6766d1 = z10;
    }

    public void setIgnorable(boolean z10) {
        if (this.L0 == z10) {
            return;
        }
        this.L0 = z10;
        X();
        invalidate();
    }

    public void setMaxValue(int i10) {
        if (this.f6816v == i10) {
            return;
        }
        if (i10 >= 0) {
            this.f6816v = i10;
            if (i10 < this.f6818w) {
                this.f6818w = i10;
            }
            X();
            invalidate();
            return;
        }
        throw new IllegalArgumentException("maxValue must be >= 0");
    }

    public void setMinValue(int i10) {
        if (this.f6814u == i10) {
            return;
        }
        if (i10 >= 0) {
            this.f6814u = i10;
            if (i10 > this.f6818w) {
                this.f6818w = i10;
            }
            X();
            invalidate();
            return;
        }
        throw new IllegalArgumentException("minValue must be >= 0");
    }

    public void setNormalTextColor(int i10) {
        if (this.R0 != i10) {
            this.R0 = i10;
            n0(i10, this.S0);
        }
    }

    public void setNormalTextSize(int i10) {
        this.f6825z0 = i10;
        invalidate();
    }

    public void setNumberPickerPaddingLeft(int i10) {
        this.C0 = i10;
        requestLayout();
    }

    public void setNumberPickerPaddingRight(int i10) {
        this.D0 = i10;
        requestLayout();
    }

    public void setOnLongPressUpdateInterval(long j10) {
        this.E = j10;
    }

    public void setOnScrollListener(d dVar) {
        this.f6824z = dVar;
    }

    public void setOnScrollingStopListener(e eVar) {
        this.f6822y = eVar;
    }

    public void setOnValueChangedListener(f fVar) {
        this.f6820x = fVar;
    }

    public void setPickerFocusColor(int i10) {
        this.f6810s0 = Color.alpha(i10);
        this.f6813t0 = Color.red(i10);
        this.f6815u0 = Color.green(i10);
        this.f6817v0 = Color.green(i10);
    }

    public void setPickerNormalColor(int i10) {
        this.f6798o0 = Color.alpha(i10);
        this.f6801p0 = Color.red(i10);
        this.f6804q0 = Color.green(i10);
        this.f6807r0 = Color.green(i10);
    }

    public void setPickerOffset(int i10) {
        this.W0 = i10;
        invalidate();
    }

    public void setPickerRowNumber(int i10) {
        int i11 = i10 + 2;
        this.f6783j0 = i11;
        this.f6786k0 = i11 / 2;
        this.F = new int[i11];
    }

    public void setSelectedValueWidth(int i10) {
        this.Y0 = i10;
    }

    public void setTouchEffectInterval(int i10) {
        this.f6780i0 = i10;
    }

    public void setUnitText(String str) {
        this.K0 = str;
    }

    public void setValue(int i10) {
        p0(i10, false);
    }

    public void setVerticalFadingEdgeEnable(boolean z10) {
        this.f6764c1 = z10;
        requestLayout();
    }

    public void setVibrateIntensity(float f10) {
        this.f6805q1 = f10;
    }

    public void setVibrateLevel(int i10) {
        this.f6802p1 = i10;
    }

    public void setWrapSelectorWheel(boolean z10) {
        this.B = z10;
        q0();
    }

    public void x(String str) {
        this.J0 = str;
    }

    public void z() {
        this.C0 = 0;
        this.D0 = 0;
        requestLayout();
    }

    public COUINumberPicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiNumberPickerStyle);
    }

    public COUINumberPicker(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, COUIContextUtil.e(context) ? R$style.COUINumberPicker_Dark : R$style.COUINumberPicker);
        Log.d("beck", "COUIContextUtil.isCOUIDarkTheme(context) = " + COUIContextUtil.e(context));
    }

    public COUINumberPicker(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f6767e = ViewConfiguration.getScrollFriction();
        this.f6770f = 65535;
        this.f6782j = new SparseArray<>();
        this.B = true;
        this.E = 300L;
        this.H = Integer.MIN_VALUE;
        this.S = 0;
        this.f6765d0 = -1;
        this.F0 = 0;
        this.f6764c1 = false;
        this.f6766d1 = false;
        this.f6769e1 = true;
        this.f6772f1 = true;
        this.f6778h1 = null;
        this.f6784j1 = -1L;
        this.f6805q1 = 1.0f;
        this.f6808r1 = 0;
        this.f6811s1 = 0;
        COUIDarkModeUtil.b(this, false);
        this.f6768e0 = (AccessibilityManager) getContext().getSystemService("accessibility");
        COUISoundLoadUtil a10 = COUISoundLoadUtil.a();
        this.f6771f0 = a10;
        this.f6789l0 = a10.c(context, R$raw.coui_numberpicker_click);
        if (attributeSet != null) {
            this.Q0 = attributeSet.getStyleAttribute();
        }
        if (this.Q0 == 0) {
            this.Q0 = i10;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUINumberPicker, i10, i11);
        int integer = obtainStyledAttributes.getInteger(R$styleable.COUINumberPicker_couiPickerRowNumber, 5) + 2;
        this.f6783j0 = integer;
        this.f6786k0 = integer / 2;
        this.F = new int[integer];
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUINumberPicker_internalMinHeight, -1);
        this.f6773g = dimensionPixelSize;
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUINumberPicker_internalMaxHeight, -1);
        this.f6776h = dimensionPixelSize2;
        if (dimensionPixelSize != -1 && dimensionPixelSize2 != -1 && dimensionPixelSize > dimensionPixelSize2) {
            throw new IllegalArgumentException("minHeight > maxHeight");
        }
        int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUINumberPicker_internalMinWidth, -1);
        this.f6779i = dimensionPixelSize3;
        int dimensionPixelSize4 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUINumberPicker_internalMaxWidth, -1);
        this.f6803q = dimensionPixelSize4;
        if (dimensionPixelSize3 != -1 && dimensionPixelSize4 != -1 && dimensionPixelSize3 > dimensionPixelSize4) {
            throw new IllegalArgumentException("minWidth > maxWidth");
        }
        this.f6821x0 = obtainStyledAttributes.getInteger(R$styleable.COUINumberPicker_couiPickerAlignPosition, -1);
        this.f6823y0 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUINumberPicker_focusTextSize, -1);
        this.f6825z0 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUINumberPicker_startTextSize, -1);
        this.f6819w0 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUINumberPicker_couiPickerVisualWidth, -1);
        this.C0 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUINumberPicker_couiNOPickerPaddingLeft, 0);
        this.D0 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUINumberPicker_couiNOPickerPaddingRight, 0);
        this.R0 = obtainStyledAttributes.getColor(R$styleable.COUINumberPicker_couiNormalTextColor, -1);
        this.S0 = obtainStyledAttributes.getColor(R$styleable.COUINumberPicker_couiFocusTextColor, -1);
        this.T0 = obtainStyledAttributes.getColor(R$styleable.COUINumberPicker_couiPickerBackgroundColor, -1);
        this.f6780i0 = obtainStyledAttributes.getInt(R$styleable.COUINumberPicker_couiPickerTouchEffectInterval, 100);
        m0(this.R0, this.S0);
        this.f6769e1 = obtainStyledAttributes.getBoolean(R$styleable.COUINumberPicker_couiPickerAdaptiveVibrator, true);
        this.f6802p1 = obtainStyledAttributes.getInteger(R$styleable.COUINumberPicker_couiVibrateLevel, 0);
        this.f6772f1 = VibrateUtils.h(context);
        this.f6764c1 = obtainStyledAttributes.getBoolean(R$styleable.COUINumberPicker_couiPickerVerticalFading, false);
        this.I0 = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUINumberPicker_couiPickerDiffusion, 0);
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPickersCommonAttrs, i10, 0);
        this.f6806r = obtainStyledAttributes2.getDimensionPixelSize(R$styleable.COUIPickersCommonAttrs_couiPickersMaxWidth, 0);
        obtainStyledAttributes2.recycle();
        this.N0 = getResources().getDimension(R$dimen.coui_numberpicker_ignore_bar_width);
        this.O0 = getResources().getDimension(R$dimen.coui_numberpicker_ignore_bar_height);
        this.P0 = getResources().getDimension(R$dimen.coui_numberpicker_ignore_bar_spacing);
        this.X0 = getResources().getDimensionPixelOffset(R$dimen.coui_number_picker_unit_min_width);
        this.A0 = getResources().getDimensionPixelSize(R$dimen.coui_numberpicker_unit_textSize);
        this.B0 = getResources().getDimensionPixelSize(R$dimen.coui_numberpicker_unit_margin_bottom);
        this.Y0 = getResources().getDimensionPixelOffset(R$dimen.coui_number_picker_text_width);
        this.f6762b1 = getResources().getDimensionPixelOffset(R$dimen.coui_number_picker_text_margin_start);
        this.E0 = Math.max(getResources().getDimensionPixelSize(R$dimen.coui_number_picker_background_divider_height), 1);
        this.f6793m1 = getContext().getResources().getDisplayMetrics().density * 160.0f;
        this.f6796n1 = A(0.84f);
        int i12 = ((dimensionPixelSize3 - this.Y0) - this.X0) - (this.f6762b1 * 2);
        this.Z0 = i12;
        this.f6760a1 = i12;
        this.P = ViewConfiguration.get(context).getScaledTouchSlop();
        this.Q = 750;
        this.R = 5000;
        Paint paint = new Paint();
        paint.setTextSize(this.f6825z0);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        paint.setTypeface(Typeface.create("sans-serif-medium", 0));
        this.G0 = fontMetrics.top;
        this.H0 = fontMetrics.bottom;
        this.f6785k = paint;
        this.f6791m = paint;
        paint.setTextSize(getResources().getDimensionPixelSize(R$dimen.coui_numberpicker_textSize_big));
        this.f6794n = new Scroller(getContext(), f6757u1);
        this.f6797o = new Scroller(getContext(), f6756t1);
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
        this.f6800p = new g();
        setWillNotDraw(false);
        setVerticalScrollBarEnabled(false);
        Paint paint2 = new Paint();
        this.f6788l = paint2;
        paint2.setAntiAlias(true);
        paint2.setTextSize(this.A0);
        paint2.setColor(this.S0);
        paint2.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        this.U0 = context.getResources().getDimensionPixelOffset(R$dimen.coui_selected_background_radius);
        this.V0 = context.getResources().getDimensionPixelOffset(R$dimen.coui_selected_background_horizontal_padding);
        this.W0 = 0;
        Paint paint3 = new Paint(1);
        this.f6775g1 = paint3;
        paint3.setColor(this.T0);
    }
}
