package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.constraintlayout.widget.R$styleable;
import java.util.HashMap;
import k.Easing;

/* compiled from: KeyPosition.java */
/* renamed from: androidx.constraintlayout.motion.widget.i, reason: use source file name */
/* loaded from: classes.dex */
public class KeyPosition extends KeyPositionBase {

    /* renamed from: h, reason: collision with root package name */
    String f1554h = null;

    /* renamed from: i, reason: collision with root package name */
    int f1555i = c.f1481f;

    /* renamed from: j, reason: collision with root package name */
    int f1556j = 0;

    /* renamed from: k, reason: collision with root package name */
    float f1557k = Float.NaN;

    /* renamed from: l, reason: collision with root package name */
    float f1558l = Float.NaN;

    /* renamed from: m, reason: collision with root package name */
    float f1559m = Float.NaN;

    /* renamed from: n, reason: collision with root package name */
    float f1560n = Float.NaN;

    /* renamed from: o, reason: collision with root package name */
    float f1561o = Float.NaN;

    /* renamed from: p, reason: collision with root package name */
    float f1562p = Float.NaN;

    /* renamed from: q, reason: collision with root package name */
    int f1563q = 0;

    /* renamed from: r, reason: collision with root package name */
    private float f1564r = Float.NaN;

    /* renamed from: s, reason: collision with root package name */
    private float f1565s = Float.NaN;

    /* compiled from: KeyPosition.java */
    /* renamed from: androidx.constraintlayout.motion.widget.i$a */
    /* loaded from: classes.dex */
    private static class a {

        /* renamed from: a, reason: collision with root package name */
        private static SparseIntArray f1566a;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f1566a = sparseIntArray;
            sparseIntArray.append(R$styleable.KeyPosition_motionTarget, 1);
            f1566a.append(R$styleable.KeyPosition_framePosition, 2);
            f1566a.append(R$styleable.KeyPosition_transitionEasing, 3);
            f1566a.append(R$styleable.KeyPosition_curveFit, 4);
            f1566a.append(R$styleable.KeyPosition_drawPath, 5);
            f1566a.append(R$styleable.KeyPosition_percentX, 6);
            f1566a.append(R$styleable.KeyPosition_percentY, 7);
            f1566a.append(R$styleable.KeyPosition_keyPositionType, 9);
            f1566a.append(R$styleable.KeyPosition_sizePercent, 8);
            f1566a.append(R$styleable.KeyPosition_percentWidth, 11);
            f1566a.append(R$styleable.KeyPosition_percentHeight, 12);
            f1566a.append(R$styleable.KeyPosition_pathMotionArc, 10);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void b(KeyPosition keyPosition, TypedArray typedArray) {
            int indexCount = typedArray.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = typedArray.getIndex(i10);
                switch (f1566a.get(index)) {
                    case 1:
                        if (MotionLayout.R0) {
                            int resourceId = typedArray.getResourceId(index, keyPosition.f1483b);
                            keyPosition.f1483b = resourceId;
                            if (resourceId == -1) {
                                keyPosition.f1484c = typedArray.getString(index);
                                break;
                            } else {
                                break;
                            }
                        } else if (typedArray.peekValue(index).type == 3) {
                            keyPosition.f1484c = typedArray.getString(index);
                            break;
                        } else {
                            keyPosition.f1483b = typedArray.getResourceId(index, keyPosition.f1483b);
                            break;
                        }
                    case 2:
                        keyPosition.f1482a = typedArray.getInt(index, keyPosition.f1482a);
                        break;
                    case 3:
                        if (typedArray.peekValue(index).type == 3) {
                            keyPosition.f1554h = typedArray.getString(index);
                            break;
                        } else {
                            keyPosition.f1554h = Easing.f13959c[typedArray.getInteger(index, 0)];
                            break;
                        }
                    case 4:
                        keyPosition.f1567g = typedArray.getInteger(index, keyPosition.f1567g);
                        break;
                    case 5:
                        keyPosition.f1556j = typedArray.getInt(index, keyPosition.f1556j);
                        break;
                    case 6:
                        keyPosition.f1559m = typedArray.getFloat(index, keyPosition.f1559m);
                        break;
                    case 7:
                        keyPosition.f1560n = typedArray.getFloat(index, keyPosition.f1560n);
                        break;
                    case 8:
                        float f10 = typedArray.getFloat(index, keyPosition.f1558l);
                        keyPosition.f1557k = f10;
                        keyPosition.f1558l = f10;
                        break;
                    case 9:
                        keyPosition.f1563q = typedArray.getInt(index, keyPosition.f1563q);
                        break;
                    case 10:
                        keyPosition.f1555i = typedArray.getInt(index, keyPosition.f1555i);
                        break;
                    case 11:
                        keyPosition.f1557k = typedArray.getFloat(index, keyPosition.f1557k);
                        break;
                    case 12:
                        keyPosition.f1558l = typedArray.getFloat(index, keyPosition.f1558l);
                        break;
                    default:
                        Log.e("KeyPosition", "unused attribute 0x" + Integer.toHexString(index) + "   " + f1566a.get(index));
                        break;
                }
            }
            if (keyPosition.f1482a == -1) {
                Log.e("KeyPosition", "no frame position");
            }
        }
    }

    public KeyPosition() {
        this.f1485d = 2;
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void a(HashMap<String, SplineSet> hashMap) {
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void c(Context context, AttributeSet attributeSet) {
        a.b(this, context.obtainStyledAttributes(attributeSet, R$styleable.KeyPosition));
    }
}
