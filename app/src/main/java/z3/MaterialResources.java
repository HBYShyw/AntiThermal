package z3;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import androidx.appcompat.widget.TintTypedArray;
import c.AppCompatResources;
import com.google.android.material.R$styleable;

/* compiled from: MaterialResources.java */
/* renamed from: z3.c, reason: use source file name */
/* loaded from: classes.dex */
public class MaterialResources {
    public static ColorStateList a(Context context, TypedArray typedArray, int i10) {
        int resourceId;
        ColorStateList a10;
        return (!typedArray.hasValue(i10) || (resourceId = typedArray.getResourceId(i10, 0)) == 0 || (a10 = AppCompatResources.a(context, resourceId)) == null) ? typedArray.getColorStateList(i10) : a10;
    }

    public static ColorStateList b(Context context, TintTypedArray tintTypedArray, int i10) {
        int n10;
        ColorStateList a10;
        return (!tintTypedArray.s(i10) || (n10 = tintTypedArray.n(i10, 0)) == 0 || (a10 = AppCompatResources.a(context, n10)) == null) ? tintTypedArray.c(i10) : a10;
    }

    private static int c(TypedValue typedValue) {
        return typedValue.getComplexUnit();
    }

    public static int d(Context context, TypedArray typedArray, int i10, int i11) {
        TypedValue typedValue = new TypedValue();
        if (typedArray.getValue(i10, typedValue) && typedValue.type == 2) {
            TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(new int[]{typedValue.data});
            int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(0, i11);
            obtainStyledAttributes.recycle();
            return dimensionPixelSize;
        }
        return typedArray.getDimensionPixelSize(i10, i11);
    }

    public static Drawable e(Context context, TypedArray typedArray, int i10) {
        int resourceId;
        Drawable b10;
        return (!typedArray.hasValue(i10) || (resourceId = typedArray.getResourceId(i10, 0)) == 0 || (b10 = AppCompatResources.b(context, resourceId)) == null) ? typedArray.getDrawable(i10) : b10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int f(TypedArray typedArray, int i10, int i11) {
        return typedArray.hasValue(i10) ? i10 : i11;
    }

    public static TextAppearance g(Context context, TypedArray typedArray, int i10) {
        int resourceId;
        if (!typedArray.hasValue(i10) || (resourceId = typedArray.getResourceId(i10, 0)) == 0) {
            return null;
        }
        return new TextAppearance(context, resourceId);
    }

    public static int h(Context context, int i10, int i11) {
        if (i10 == 0) {
            return i11;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(i10, R$styleable.TextAppearance);
        TypedValue typedValue = new TypedValue();
        boolean value = obtainStyledAttributes.getValue(R$styleable.TextAppearance_android_textSize, typedValue);
        obtainStyledAttributes.recycle();
        if (!value) {
            return i11;
        }
        if (c(typedValue) == 2) {
            return Math.round(TypedValue.complexToFloat(typedValue.data) * context.getResources().getDisplayMetrics().density);
        }
        return TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
    }

    public static boolean i(Context context) {
        return context.getResources().getConfiguration().fontScale >= 1.3f;
    }

    public static boolean j(Context context) {
        return context.getResources().getConfiguration().fontScale >= 2.0f;
    }
}
