package u3;

import android.content.Context;
import android.graphics.Color;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.R$attr;
import r3.MaterialColors;
import z3.MaterialAttributes;

/* compiled from: ElevationOverlayProvider.java */
/* renamed from: u3.a, reason: use source file name */
/* loaded from: classes.dex */
public class ElevationOverlayProvider {

    /* renamed from: f, reason: collision with root package name */
    private static final int f18849f = (int) Math.round(5.1000000000000005d);

    /* renamed from: a, reason: collision with root package name */
    private final boolean f18850a;

    /* renamed from: b, reason: collision with root package name */
    private final int f18851b;

    /* renamed from: c, reason: collision with root package name */
    private final int f18852c;

    /* renamed from: d, reason: collision with root package name */
    private final int f18853d;

    /* renamed from: e, reason: collision with root package name */
    private final float f18854e;

    public ElevationOverlayProvider(Context context) {
        this(MaterialAttributes.b(context, R$attr.elevationOverlayEnabled, false), MaterialColors.b(context, R$attr.elevationOverlayColor, 0), MaterialColors.b(context, R$attr.elevationOverlayAccentColor, 0), MaterialColors.b(context, R$attr.colorSurface, 0), context.getResources().getDisplayMetrics().density);
    }

    private boolean f(int i10) {
        return ColorUtils.n(i10, 255) == this.f18853d;
    }

    public float a(float f10) {
        if (this.f18854e <= 0.0f || f10 <= 0.0f) {
            return 0.0f;
        }
        return Math.min(((((float) Math.log1p(f10 / r2)) * 4.5f) + 2.0f) / 100.0f, 1.0f);
    }

    public int b(int i10, float f10) {
        int i11;
        float a10 = a(f10);
        int alpha = Color.alpha(i10);
        int h10 = MaterialColors.h(ColorUtils.n(i10, 255), this.f18851b, a10);
        if (a10 > 0.0f && (i11 = this.f18852c) != 0) {
            h10 = MaterialColors.g(h10, ColorUtils.n(i11, f18849f));
        }
        return ColorUtils.n(h10, alpha);
    }

    public int c(int i10, float f10) {
        return (this.f18850a && f(i10)) ? b(i10, f10) : i10;
    }

    public int d(float f10) {
        return c(this.f18853d, f10);
    }

    public boolean e() {
        return this.f18850a;
    }

    public ElevationOverlayProvider(boolean z10, int i10, int i11, int i12, float f10) {
        this.f18850a = z10;
        this.f18851b = i10;
        this.f18852c = i11;
        this.f18853d = i12;
        this.f18854e = f10;
    }
}
