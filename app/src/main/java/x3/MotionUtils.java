package x3;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.TypedValue;
import androidx.core.graphics.d;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.oplus.backup.sdk.common.utils.Constants;
import z3.MaterialAttributes;

/* compiled from: MotionUtils.java */
/* renamed from: x3.a, reason: use source file name */
/* loaded from: classes.dex */
public class MotionUtils {
    private static float a(String[] strArr, int i10) {
        float parseFloat = Float.parseFloat(strArr[i10]);
        if (parseFloat >= 0.0f && parseFloat <= 1.0f) {
            return parseFloat;
        }
        throw new IllegalArgumentException("Motion easing control point value must be between 0 and 1; instead got: " + parseFloat);
    }

    private static String b(String str, String str2) {
        return str.substring(str2.length() + 1, str.length() - 1);
    }

    private static boolean c(String str, String str2) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str2);
        sb2.append("(");
        return str.startsWith(sb2.toString()) && str.endsWith(")");
    }

    public static int d(Context context, int i10, int i11) {
        return MaterialAttributes.c(context, i10, i11);
    }

    public static TimeInterpolator e(Context context, int i10, TimeInterpolator timeInterpolator) {
        TypedValue typedValue = new TypedValue();
        if (!context.getTheme().resolveAttribute(i10, typedValue, true)) {
            return timeInterpolator;
        }
        if (typedValue.type == 3) {
            String valueOf = String.valueOf(typedValue.string);
            if (c(valueOf, "cubic-bezier")) {
                String[] split = b(valueOf, "cubic-bezier").split(",");
                if (split.length == 4) {
                    return PathInterpolatorCompat.a(a(split, 0), a(split, 1), a(split, 2), a(split, 3));
                }
                throw new IllegalArgumentException("Motion easing theme attribute must have 4 control points if using bezier curve format; instead got: " + split.length);
            }
            if (c(valueOf, Constants.MessagerConstants.PATH_KEY)) {
                return PathInterpolatorCompat.b(d.d(b(valueOf, Constants.MessagerConstants.PATH_KEY)));
            }
            throw new IllegalArgumentException("Invalid motion easing type: " + valueOf);
        }
        throw new IllegalArgumentException("Motion easing theme attribute must be a string");
    }
}
