package z2;

import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.TextView;
import j3.COUIVersionUtil;

/* compiled from: COUIChangeTextUtil.java */
/* renamed from: z2.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIChangeTextUtil {

    /* renamed from: a, reason: collision with root package name */
    public static final float[] f20206a = {0.9f, 1.0f, 1.15f, 1.35f, 1.6f};

    public static void a(Paint paint, boolean z10) {
        if (paint != null) {
            if (COUIVersionUtil.b() < 12) {
                paint.setFakeBoldText(z10);
            } else {
                paint.setTypeface(z10 ? Typeface.create("sans-serif-medium", 0) : Typeface.DEFAULT);
            }
        }
    }

    public static void b(TextView textView, boolean z10) {
        if (textView != null) {
            if (COUIVersionUtil.b() < 12) {
                textView.getPaint().setFakeBoldText(z10);
            } else {
                textView.setTypeface(z10 ? Typeface.create("sans-serif-medium", 0) : Typeface.DEFAULT);
            }
        }
    }

    public static void c(TextView textView, int i10) {
        float textSize = textView.getTextSize();
        Configuration configuration = textView.getResources().getConfiguration();
        textView.getResources().getDisplayMetrics();
        float f10 = configuration.fontScale;
        int i11 = configuration.densityDpi;
        if (i11 == 300 || i11 == 296 || configuration.smallestScreenWidthDp <= 210) {
            f10 = 1.0f;
        }
        textView.setTextSize(0, e(textSize, f10, i10));
    }

    public static float d(float f10, float f11) {
        float round = Math.round(f10 / f11);
        return f11 <= 1.0f ? f10 : f11 < 1.6f ? round * 1.15f : round * 1.15f;
    }

    public static float e(float f10, float f11, int i10) {
        float f12;
        if (i10 < 2) {
            return f10;
        }
        float[] fArr = f20206a;
        if (i10 > fArr.length) {
            i10 = fArr.length;
        }
        float round = Math.round(f10 / f11);
        if (i10 == 2) {
            return f11 < 1.15f ? round * 1.0f : round * 1.15f;
        }
        if (i10 != 3) {
            int i11 = i10 - 1;
            if (f11 <= fArr[i11]) {
                return round * f11;
            }
            f12 = fArr[i11];
        } else {
            if (f11 < 1.15f) {
                return round * 1.0f;
            }
            if (f11 < 1.6f) {
                return round * 1.15f;
            }
            f12 = 1.35f;
        }
        return round * f12;
    }
}
