package androidx.core.content.res;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import org.xmlpull.v1.XmlPullParser;

/* compiled from: TypedArrayUtils.java */
/* renamed from: androidx.core.content.res.i, reason: use source file name */
/* loaded from: classes.dex */
public class TypedArrayUtils {
    public static int a(Context context, int i10, int i11) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(i10, typedValue, true);
        return typedValue.resourceId != 0 ? i10 : i11;
    }

    public static boolean b(TypedArray typedArray, int i10, int i11, boolean z10) {
        return typedArray.getBoolean(i10, typedArray.getBoolean(i11, z10));
    }

    public static Drawable c(TypedArray typedArray, int i10, int i11) {
        Drawable drawable = typedArray.getDrawable(i10);
        return drawable == null ? typedArray.getDrawable(i11) : drawable;
    }

    public static int d(TypedArray typedArray, int i10, int i11, int i12) {
        return typedArray.getInt(i10, typedArray.getInt(i11, i12));
    }

    public static boolean e(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i10, boolean z10) {
        return !n(xmlPullParser, str) ? z10 : typedArray.getBoolean(i10, z10);
    }

    public static float f(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i10, float f10) {
        return !n(xmlPullParser, str) ? f10 : typedArray.getFloat(i10, f10);
    }

    public static int g(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i10, int i11) {
        return !n(xmlPullParser, str) ? i11 : typedArray.getInt(i10, i11);
    }

    public static int h(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i10, int i11) {
        return !n(xmlPullParser, str) ? i11 : typedArray.getResourceId(i10, i11);
    }

    public static String i(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i10) {
        if (n(xmlPullParser, str)) {
            return typedArray.getString(i10);
        }
        return null;
    }

    public static int j(TypedArray typedArray, int i10, int i11, int i12) {
        return typedArray.getResourceId(i10, typedArray.getResourceId(i11, i12));
    }

    public static String k(TypedArray typedArray, int i10, int i11) {
        String string = typedArray.getString(i10);
        return string == null ? typedArray.getString(i11) : string;
    }

    public static CharSequence l(TypedArray typedArray, int i10, int i11) {
        CharSequence text = typedArray.getText(i10);
        return text == null ? typedArray.getText(i11) : text;
    }

    public static CharSequence[] m(TypedArray typedArray, int i10, int i11) {
        CharSequence[] textArray = typedArray.getTextArray(i10);
        return textArray == null ? typedArray.getTextArray(i11) : textArray;
    }

    public static boolean n(XmlPullParser xmlPullParser, String str) {
        return xmlPullParser.getAttributeValue("http://schemas.android.com/apk/res/android", str) != null;
    }
}
