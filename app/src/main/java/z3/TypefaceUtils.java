package z3;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;

/* compiled from: TypefaceUtils.java */
/* renamed from: z3.g, reason: use source file name */
/* loaded from: classes.dex */
public class TypefaceUtils {
    public static Typeface a(Context context, Typeface typeface) {
        return b(context.getResources().getConfiguration(), typeface);
    }

    public static Typeface b(Configuration configuration, Typeface typeface) {
        int i10 = configuration.fontWeightAdjustment;
        if (i10 == Integer.MAX_VALUE || i10 == 0) {
            return null;
        }
        return Typeface.create(typeface, q.a.b(typeface.getWeight() + configuration.fontWeightAdjustment, 1, 1000), typeface.isItalic());
    }
}
