package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.R$styleable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

/* compiled from: KeyTrigger.java */
/* renamed from: androidx.constraintlayout.motion.widget.l, reason: use source file name */
/* loaded from: classes.dex */
public class KeyTrigger extends c {

    /* renamed from: g, reason: collision with root package name */
    private int f1586g = -1;

    /* renamed from: h, reason: collision with root package name */
    private String f1587h = null;

    /* renamed from: i, reason: collision with root package name */
    private int f1588i;

    /* renamed from: j, reason: collision with root package name */
    private String f1589j;

    /* renamed from: k, reason: collision with root package name */
    private String f1590k;

    /* renamed from: l, reason: collision with root package name */
    private int f1591l;

    /* renamed from: m, reason: collision with root package name */
    private int f1592m;

    /* renamed from: n, reason: collision with root package name */
    private View f1593n;

    /* renamed from: o, reason: collision with root package name */
    float f1594o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f1595p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f1596q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f1597r;

    /* renamed from: s, reason: collision with root package name */
    private float f1598s;

    /* renamed from: t, reason: collision with root package name */
    private Method f1599t;

    /* renamed from: u, reason: collision with root package name */
    private Method f1600u;

    /* renamed from: v, reason: collision with root package name */
    private Method f1601v;

    /* renamed from: w, reason: collision with root package name */
    private float f1602w;

    /* renamed from: x, reason: collision with root package name */
    private boolean f1603x;

    /* renamed from: y, reason: collision with root package name */
    RectF f1604y;

    /* renamed from: z, reason: collision with root package name */
    RectF f1605z;

    /* compiled from: KeyTrigger.java */
    /* renamed from: androidx.constraintlayout.motion.widget.l$a */
    /* loaded from: classes.dex */
    private static class a {

        /* renamed from: a, reason: collision with root package name */
        private static SparseIntArray f1606a;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f1606a = sparseIntArray;
            sparseIntArray.append(R$styleable.KeyTrigger_framePosition, 8);
            f1606a.append(R$styleable.KeyTrigger_onCross, 4);
            f1606a.append(R$styleable.KeyTrigger_onNegativeCross, 1);
            f1606a.append(R$styleable.KeyTrigger_onPositiveCross, 2);
            f1606a.append(R$styleable.KeyTrigger_motionTarget, 7);
            f1606a.append(R$styleable.KeyTrigger_triggerId, 6);
            f1606a.append(R$styleable.KeyTrigger_triggerSlack, 5);
            f1606a.append(R$styleable.KeyTrigger_motion_triggerOnCollision, 9);
            f1606a.append(R$styleable.KeyTrigger_motion_postLayoutCollision, 10);
            f1606a.append(R$styleable.KeyTrigger_triggerReceiver, 11);
        }

        public static void a(KeyTrigger keyTrigger, TypedArray typedArray, Context context) {
            int indexCount = typedArray.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = typedArray.getIndex(i10);
                switch (f1606a.get(index)) {
                    case 1:
                        keyTrigger.f1589j = typedArray.getString(index);
                        continue;
                    case 2:
                        keyTrigger.f1590k = typedArray.getString(index);
                        continue;
                    case 4:
                        keyTrigger.f1587h = typedArray.getString(index);
                        continue;
                    case 5:
                        keyTrigger.f1594o = typedArray.getFloat(index, keyTrigger.f1594o);
                        continue;
                    case 6:
                        keyTrigger.f1591l = typedArray.getResourceId(index, keyTrigger.f1591l);
                        continue;
                    case 7:
                        if (MotionLayout.R0) {
                            int resourceId = typedArray.getResourceId(index, keyTrigger.f1483b);
                            keyTrigger.f1483b = resourceId;
                            if (resourceId == -1) {
                                keyTrigger.f1484c = typedArray.getString(index);
                                break;
                            } else {
                                continue;
                            }
                        } else if (typedArray.peekValue(index).type == 3) {
                            keyTrigger.f1484c = typedArray.getString(index);
                            break;
                        } else {
                            keyTrigger.f1483b = typedArray.getResourceId(index, keyTrigger.f1483b);
                            break;
                        }
                    case 8:
                        int integer = typedArray.getInteger(index, keyTrigger.f1482a);
                        keyTrigger.f1482a = integer;
                        keyTrigger.f1598s = (integer + 0.5f) / 100.0f;
                        continue;
                    case 9:
                        keyTrigger.f1592m = typedArray.getResourceId(index, keyTrigger.f1592m);
                        continue;
                    case 10:
                        keyTrigger.f1603x = typedArray.getBoolean(index, keyTrigger.f1603x);
                        continue;
                    case 11:
                        keyTrigger.f1588i = typedArray.getResourceId(index, keyTrigger.f1588i);
                        break;
                }
                Log.e("KeyTrigger", "unused attribute 0x" + Integer.toHexString(index) + "   " + f1606a.get(index));
            }
        }
    }

    public KeyTrigger() {
        int i10 = c.f1481f;
        this.f1588i = i10;
        this.f1589j = null;
        this.f1590k = null;
        this.f1591l = i10;
        this.f1592m = i10;
        this.f1593n = null;
        this.f1594o = 0.1f;
        this.f1595p = true;
        this.f1596q = true;
        this.f1597r = true;
        this.f1598s = Float.NaN;
        this.f1603x = false;
        this.f1604y = new RectF();
        this.f1605z = new RectF();
        this.f1485d = 5;
        this.f1486e = new HashMap<>();
    }

    private void s(RectF rectF, View view, boolean z10) {
        rectF.top = view.getTop();
        rectF.bottom = view.getBottom();
        rectF.left = view.getLeft();
        rectF.right = view.getRight();
        if (z10) {
            view.getMatrix().mapRect(rectF);
        }
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void a(HashMap<String, SplineSet> hashMap) {
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void b(HashSet<String> hashSet) {
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void c(Context context, AttributeSet attributeSet) {
        a.a(this, context.obtainStyledAttributes(attributeSet, R$styleable.KeyTrigger), context);
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x00c9  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x00b6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void r(float f10, View view) {
        boolean z10;
        boolean z11;
        boolean z12;
        boolean z13;
        boolean z14 = true;
        if (this.f1592m != c.f1481f) {
            if (this.f1593n == null) {
                this.f1593n = ((ViewGroup) view.getParent()).findViewById(this.f1592m);
            }
            s(this.f1604y, this.f1593n, this.f1603x);
            s(this.f1605z, view, this.f1603x);
            if (this.f1604y.intersect(this.f1605z)) {
                if (this.f1595p) {
                    this.f1595p = false;
                    z10 = true;
                } else {
                    z10 = false;
                }
                if (this.f1597r) {
                    this.f1597r = false;
                    z13 = true;
                } else {
                    z13 = false;
                }
                this.f1596q = true;
                z14 = z13;
                z12 = false;
            } else {
                if (this.f1595p) {
                    z10 = false;
                } else {
                    this.f1595p = true;
                    z10 = true;
                }
                if (this.f1596q) {
                    this.f1596q = false;
                    z12 = true;
                } else {
                    z12 = false;
                }
                this.f1597r = true;
                z14 = false;
            }
        } else {
            if (this.f1595p) {
                float f11 = this.f1598s;
                if ((f10 - f11) * (this.f1602w - f11) < 0.0f) {
                    this.f1595p = false;
                    z10 = true;
                    if (!this.f1596q) {
                        float f12 = this.f1598s;
                        float f13 = f10 - f12;
                        if ((this.f1602w - f12) * f13 < 0.0f && f13 < 0.0f) {
                            this.f1596q = false;
                            z11 = true;
                            if (this.f1597r) {
                                float f14 = this.f1598s;
                                float f15 = f10 - f14;
                                if ((this.f1602w - f14) * f15 < 0.0f && f15 > 0.0f) {
                                    this.f1597r = false;
                                    z12 = z11;
                                }
                            } else if (Math.abs(f10 - this.f1598s) > this.f1594o) {
                                this.f1597r = true;
                            }
                            z14 = false;
                            z12 = z11;
                        }
                    } else if (Math.abs(f10 - this.f1598s) > this.f1594o) {
                        this.f1596q = true;
                    }
                    z11 = false;
                    if (this.f1597r) {
                    }
                    z14 = false;
                    z12 = z11;
                }
            } else if (Math.abs(f10 - this.f1598s) > this.f1594o) {
                this.f1595p = true;
            }
            z10 = false;
            if (!this.f1596q) {
            }
            z11 = false;
            if (this.f1597r) {
            }
            z14 = false;
            z12 = z11;
        }
        this.f1602w = f10;
        if (z12 || z10 || z14) {
            ((MotionLayout) view.getParent()).h0(this.f1591l, z14, f10);
        }
        if (this.f1588i != c.f1481f) {
            view = ((MotionLayout) view.getParent()).findViewById(this.f1588i);
        }
        if (z12 && this.f1589j != null) {
            if (this.f1600u == null) {
                try {
                    this.f1600u = view.getClass().getMethod(this.f1589j, new Class[0]);
                } catch (NoSuchMethodException unused) {
                    Log.e("KeyTrigger", "Could not find method \"" + this.f1589j + "\"on class " + view.getClass().getSimpleName() + " " + androidx.constraintlayout.motion.widget.a.c(view));
                }
            }
            try {
                this.f1600u.invoke(view, new Object[0]);
            } catch (Exception unused2) {
                Log.e("KeyTrigger", "Exception in call \"" + this.f1589j + "\"on class " + view.getClass().getSimpleName() + " " + androidx.constraintlayout.motion.widget.a.c(view));
            }
        }
        if (z14 && this.f1590k != null) {
            if (this.f1601v == null) {
                try {
                    this.f1601v = view.getClass().getMethod(this.f1590k, new Class[0]);
                } catch (NoSuchMethodException unused3) {
                    Log.e("KeyTrigger", "Could not find method \"" + this.f1590k + "\"on class " + view.getClass().getSimpleName() + " " + androidx.constraintlayout.motion.widget.a.c(view));
                }
            }
            try {
                this.f1601v.invoke(view, new Object[0]);
            } catch (Exception unused4) {
                Log.e("KeyTrigger", "Exception in call \"" + this.f1590k + "\"on class " + view.getClass().getSimpleName() + " " + androidx.constraintlayout.motion.widget.a.c(view));
            }
        }
        if (!z10 || this.f1587h == null) {
            return;
        }
        if (this.f1599t == null) {
            try {
                this.f1599t = view.getClass().getMethod(this.f1587h, new Class[0]);
            } catch (NoSuchMethodException unused5) {
                Log.e("KeyTrigger", "Could not find method \"" + this.f1587h + "\"on class " + view.getClass().getSimpleName() + " " + androidx.constraintlayout.motion.widget.a.c(view));
            }
        }
        try {
            this.f1599t.invoke(view, new Object[0]);
        } catch (Exception unused6) {
            Log.e("KeyTrigger", "Exception in call \"" + this.f1587h + "\"on class " + view.getClass().getSimpleName() + " " + androidx.constraintlayout.motion.widget.a.c(view));
        }
    }
}
