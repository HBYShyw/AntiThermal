package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.TypedValue;
import android.util.Xml;
import androidx.core.R$attr;
import androidx.core.R$styleable;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: ColorStateListInflaterCompat.java */
/* renamed from: androidx.core.content.res.c, reason: use source file name */
/* loaded from: classes.dex */
public final class ColorStateListInflaterCompat {

    /* renamed from: a, reason: collision with root package name */
    private static final ThreadLocal<TypedValue> f2148a = new ThreadLocal<>();

    public static ColorStateList a(Resources resources, XmlPullParser xmlPullParser, Resources.Theme theme) {
        int next;
        AttributeSet asAttributeSet = Xml.asAttributeSet(xmlPullParser);
        do {
            next = xmlPullParser.next();
            if (next == 2) {
                break;
            }
        } while (next != 1);
        if (next == 2) {
            return b(resources, xmlPullParser, asAttributeSet, theme);
        }
        throw new XmlPullParserException("No start tag found");
    }

    public static ColorStateList b(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        String name = xmlPullParser.getName();
        if (name.equals("selector")) {
            return d(resources, xmlPullParser, attributeSet, theme);
        }
        throw new XmlPullParserException(xmlPullParser.getPositionDescription() + ": invalid color state list tag " + name);
    }

    private static TypedValue c() {
        ThreadLocal<TypedValue> threadLocal = f2148a;
        TypedValue typedValue = threadLocal.get();
        if (typedValue != null) {
            return typedValue;
        }
        TypedValue typedValue2 = new TypedValue();
        threadLocal.set(typedValue2);
        return typedValue2;
    }

    private static ColorStateList d(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        int depth;
        int color;
        float f10;
        Resources resources2 = resources;
        int i10 = 1;
        int depth2 = xmlPullParser.getDepth() + 1;
        int[][] iArr = new int[20];
        int[] iArr2 = new int[20];
        int i11 = 0;
        while (true) {
            int next = xmlPullParser.next();
            if (next == i10 || ((depth = xmlPullParser.getDepth()) < depth2 && next == 3)) {
                break;
            }
            if (next == 2 && depth <= depth2 && xmlPullParser.getName().equals("item")) {
                TypedArray g6 = g(resources2, theme, attributeSet, R$styleable.ColorStateListItem);
                int i12 = R$styleable.ColorStateListItem_android_color;
                int resourceId = g6.getResourceId(i12, -1);
                if (resourceId != -1 && !e(resources2, resourceId)) {
                    try {
                        color = a(resources2, resources2.getXml(resourceId), theme).getDefaultColor();
                    } catch (Exception unused) {
                        color = g6.getColor(R$styleable.ColorStateListItem_android_color, -65281);
                    }
                } else {
                    color = g6.getColor(i12, -65281);
                }
                float f11 = 1.0f;
                int i13 = R$styleable.ColorStateListItem_android_alpha;
                if (g6.hasValue(i13)) {
                    f11 = g6.getFloat(i13, 1.0f);
                } else {
                    int i14 = R$styleable.ColorStateListItem_alpha;
                    if (g6.hasValue(i14)) {
                        f11 = g6.getFloat(i14, 1.0f);
                    }
                }
                int i15 = R$styleable.ColorStateListItem_android_lStar;
                if (g6.hasValue(i15)) {
                    f10 = g6.getFloat(i15, -1.0f);
                } else {
                    f10 = g6.getFloat(R$styleable.ColorStateListItem_lStar, -1.0f);
                }
                g6.recycle();
                int attributeCount = attributeSet.getAttributeCount();
                int[] iArr3 = new int[attributeCount];
                int i16 = 0;
                for (int i17 = 0; i17 < attributeCount; i17++) {
                    int attributeNameResource = attributeSet.getAttributeNameResource(i17);
                    if (attributeNameResource != 16843173 && attributeNameResource != 16843551 && attributeNameResource != R$attr.alpha && attributeNameResource != R$attr.lStar) {
                        int i18 = i16 + 1;
                        if (!attributeSet.getAttributeBooleanValue(i17, false)) {
                            attributeNameResource = -attributeNameResource;
                        }
                        iArr3[i16] = attributeNameResource;
                        i16 = i18;
                    }
                }
                int[] trimStateSet = StateSet.trimStateSet(iArr3, i16);
                iArr2 = GrowingArrayUtils.a(iArr2, i11, f(color, f11, f10));
                iArr = (int[][]) GrowingArrayUtils.b(iArr, i11, trimStateSet);
                i11++;
            }
            i10 = 1;
            resources2 = resources;
        }
        int[] iArr4 = new int[i11];
        int[][] iArr5 = new int[i11];
        System.arraycopy(iArr2, 0, iArr4, 0, i11);
        System.arraycopy(iArr, 0, iArr5, 0, i11);
        return new ColorStateList(iArr5, iArr4);
    }

    private static boolean e(Resources resources, int i10) {
        TypedValue c10 = c();
        resources.getValue(i10, c10, true);
        int i11 = c10.type;
        return i11 >= 28 && i11 <= 31;
    }

    private static int f(int i10, float f10, float f11) {
        boolean z10 = f11 >= 0.0f && f11 <= 100.0f;
        if (f10 == 1.0f && !z10) {
            return i10;
        }
        int b10 = q.a.b((int) ((Color.alpha(i10) * f10) + 0.5f), 0, 255);
        if (z10) {
            CamColor c10 = CamColor.c(i10);
            i10 = CamColor.m(c10.j(), c10.i(), f11);
        }
        return (i10 & 16777215) | (b10 << 24);
    }

    private static TypedArray g(Resources resources, Resources.Theme theme, AttributeSet attributeSet, int[] iArr) {
        if (theme == null) {
            return resources.obtainAttributes(attributeSet, iArr);
        }
        return theme.obtainStyledAttributes(attributeSet, iArr, 0, 0);
    }
}
