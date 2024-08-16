package v1;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.ContextThemeWrapper;
import com.support.appcompat.R$style;
import com.support.appcompat.R$styleable;

/* compiled from: COUIContextUtil.java */
/* renamed from: v1.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIContextUtil {
    public static int a(Context context, int i10) {
        return b(context, i10, 0);
    }

    public static int b(Context context, int i10, int i11) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(new int[]{i10});
        int color = obtainStyledAttributes.getColor(0, i11);
        obtainStyledAttributes.recycle();
        return color;
    }

    public static Context c(Context context) {
        return f(context) ? context : new ContextThemeWrapper(context, R$style.Theme_COUI);
    }

    public static int d(Context context, int i10) {
        return context.getColor(i10);
    }

    public static boolean e(Context context) {
        if (context == null) {
            return false;
        }
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(R$styleable.COUITheme);
        boolean z10 = obtainStyledAttributes.getBoolean(R$styleable.COUITheme_isCOUIDarkTheme, false);
        obtainStyledAttributes.recycle();
        return z10;
    }

    public static boolean f(Context context) {
        if (context == null) {
            return false;
        }
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(R$styleable.COUITheme);
        boolean z10 = obtainStyledAttributes.getBoolean(R$styleable.COUITheme_isCOUITheme, false);
        obtainStyledAttributes.recycle();
        return z10;
    }
}
