package c4;

import android.graphics.drawable.Drawable;
import android.view.View;
import com.google.android.material.internal.ViewUtils;

/* compiled from: MaterialShapeUtils.java */
/* renamed from: c4.i, reason: use source file name */
/* loaded from: classes.dex */
public class MaterialShapeUtils {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static CornerTreatment a(int i10) {
        if (i10 == 0) {
            return new RoundedCornerTreatment();
        }
        if (i10 != 1) {
            return b();
        }
        return new CutCornerTreatment();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CornerTreatment b() {
        return new RoundedCornerTreatment();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static EdgeTreatment c() {
        return new EdgeTreatment();
    }

    public static void d(View view, float f10) {
        Drawable background = view.getBackground();
        if (background instanceof MaterialShapeDrawable) {
            ((MaterialShapeDrawable) background).Z(f10);
        }
    }

    public static void e(View view) {
        Drawable background = view.getBackground();
        if (background instanceof MaterialShapeDrawable) {
            f(view, (MaterialShapeDrawable) background);
        }
    }

    public static void f(View view, MaterialShapeDrawable materialShapeDrawable) {
        if (materialShapeDrawable.R()) {
            materialShapeDrawable.e0(ViewUtils.getParentAbsoluteElevation(view));
        }
    }
}
