package a4;

import android.R;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.StateSet;
import androidx.core.graphics.ColorUtils;

/* compiled from: RippleUtils.java */
/* renamed from: a4.b, reason: use source file name */
/* loaded from: classes.dex */
public class RippleUtils {

    /* renamed from: a, reason: collision with root package name */
    public static final boolean f36a = true;

    /* renamed from: b, reason: collision with root package name */
    private static final int[] f37b = {R.attr.state_pressed};

    /* renamed from: c, reason: collision with root package name */
    private static final int[] f38c = {R.attr.state_hovered, R.attr.state_focused};

    /* renamed from: d, reason: collision with root package name */
    private static final int[] f39d = {R.attr.state_focused};

    /* renamed from: e, reason: collision with root package name */
    private static final int[] f40e = {R.attr.state_hovered};

    /* renamed from: f, reason: collision with root package name */
    private static final int[] f41f = {R.attr.state_selected, R.attr.state_pressed};

    /* renamed from: g, reason: collision with root package name */
    private static final int[] f42g = {R.attr.state_selected, R.attr.state_hovered, R.attr.state_focused};

    /* renamed from: h, reason: collision with root package name */
    private static final int[] f43h = {R.attr.state_selected, R.attr.state_focused};

    /* renamed from: i, reason: collision with root package name */
    private static final int[] f44i = {R.attr.state_selected, R.attr.state_hovered};

    /* renamed from: j, reason: collision with root package name */
    private static final int[] f45j = {R.attr.state_selected};

    /* renamed from: k, reason: collision with root package name */
    private static final int[] f46k = {R.attr.state_enabled, R.attr.state_pressed};

    /* renamed from: l, reason: collision with root package name */
    static final String f47l = RippleUtils.class.getSimpleName();

    private RippleUtils() {
    }

    public static ColorStateList a(ColorStateList colorStateList) {
        if (f36a) {
            return new ColorStateList(new int[][]{f45j, StateSet.NOTHING}, new int[]{c(colorStateList, f41f), c(colorStateList, f37b)});
        }
        int[] iArr = f41f;
        int[] iArr2 = f42g;
        int[] iArr3 = f43h;
        int[] iArr4 = f44i;
        int[] iArr5 = f37b;
        int[] iArr6 = f38c;
        int[] iArr7 = f39d;
        int[] iArr8 = f40e;
        return new ColorStateList(new int[][]{iArr, iArr2, iArr3, iArr4, f45j, iArr5, iArr6, iArr7, iArr8, StateSet.NOTHING}, new int[]{c(colorStateList, iArr), c(colorStateList, iArr2), c(colorStateList, iArr3), c(colorStateList, iArr4), 0, c(colorStateList, iArr5), c(colorStateList, iArr6), c(colorStateList, iArr7), c(colorStateList, iArr8), 0});
    }

    @TargetApi(21)
    private static int b(int i10) {
        return ColorUtils.n(i10, Math.min(Color.alpha(i10) * 2, 255));
    }

    private static int c(ColorStateList colorStateList, int[] iArr) {
        int colorForState = colorStateList != null ? colorStateList.getColorForState(iArr, colorStateList.getDefaultColor()) : 0;
        return f36a ? b(colorForState) : colorForState;
    }

    public static ColorStateList d(ColorStateList colorStateList) {
        return colorStateList != null ? colorStateList : ColorStateList.valueOf(0);
    }

    public static boolean e(int[] iArr) {
        boolean z10 = false;
        boolean z11 = false;
        for (int i10 : iArr) {
            if (i10 == 16842910) {
                z10 = true;
            } else if (i10 == 16842908 || i10 == 16842919 || i10 == 16843623) {
                z11 = true;
            }
        }
        return z10 && z11;
    }
}
