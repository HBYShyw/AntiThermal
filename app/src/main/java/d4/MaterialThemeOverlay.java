package d4;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.view.ContextThemeWrapper;
import com.google.android.material.R$attr;

/* compiled from: MaterialThemeOverlay.java */
/* renamed from: d4.a, reason: use source file name */
/* loaded from: classes.dex */
public class MaterialThemeOverlay {

    /* renamed from: a, reason: collision with root package name */
    private static final int[] f10711a = {R.attr.theme, R$attr.theme};

    /* renamed from: b, reason: collision with root package name */
    private static final int[] f10712b = {R$attr.materialThemeOverlay};

    private static int a(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, f10711a);
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        int resourceId2 = obtainStyledAttributes.getResourceId(1, 0);
        obtainStyledAttributes.recycle();
        return resourceId != 0 ? resourceId : resourceId2;
    }

    private static int b(Context context, AttributeSet attributeSet, int i10, int i11) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, f10712b, i10, i11);
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        obtainStyledAttributes.recycle();
        return resourceId;
    }

    public static Context c(Context context, AttributeSet attributeSet, int i10, int i11) {
        int b10 = b(context, attributeSet, i10, i11);
        boolean z10 = (context instanceof ContextThemeWrapper) && ((ContextThemeWrapper) context).getThemeResId() == b10;
        if (b10 == 0 || z10) {
            return context;
        }
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, b10);
        int a10 = a(context, attributeSet);
        if (a10 != 0) {
            contextThemeWrapper.getTheme().applyStyle(a10, true);
        }
        return contextThemeWrapper;
    }
}
