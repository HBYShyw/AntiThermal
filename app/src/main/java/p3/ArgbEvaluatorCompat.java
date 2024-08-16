package p3;

import android.animation.TypeEvaluator;

/* compiled from: ArgbEvaluatorCompat.java */
/* renamed from: p3.c, reason: use source file name */
/* loaded from: classes.dex */
public class ArgbEvaluatorCompat implements TypeEvaluator<Integer> {

    /* renamed from: a, reason: collision with root package name */
    private static final ArgbEvaluatorCompat f16560a = new ArgbEvaluatorCompat();

    public static ArgbEvaluatorCompat b() {
        return f16560a;
    }

    @Override // android.animation.TypeEvaluator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public Integer evaluate(float f10, Integer num, Integer num2) {
        int intValue = num.intValue();
        float f11 = ((intValue >> 24) & 255) / 255.0f;
        int intValue2 = num2.intValue();
        float pow = (float) Math.pow(((intValue >> 16) & 255) / 255.0f, 2.2d);
        float pow2 = (float) Math.pow(((intValue >> 8) & 255) / 255.0f, 2.2d);
        float pow3 = (float) Math.pow((intValue & 255) / 255.0f, 2.2d);
        float pow4 = (float) Math.pow(((intValue2 >> 16) & 255) / 255.0f, 2.2d);
        float f12 = f11 + (((((intValue2 >> 24) & 255) / 255.0f) - f11) * f10);
        float pow5 = pow2 + ((((float) Math.pow(((intValue2 >> 8) & 255) / 255.0f, 2.2d)) - pow2) * f10);
        float pow6 = pow3 + (f10 * (((float) Math.pow((intValue2 & 255) / 255.0f, 2.2d)) - pow3));
        float pow7 = ((float) Math.pow(pow + ((pow4 - pow) * f10), 0.45454545454545453d)) * 255.0f;
        float pow8 = ((float) Math.pow(pow5, 0.45454545454545453d)) * 255.0f;
        return Integer.valueOf(Math.round(((float) Math.pow(pow6, 0.45454545454545453d)) * 255.0f) | (Math.round(pow7) << 16) | (Math.round(f12 * 255.0f) << 24) | (Math.round(pow8) << 8));
    }
}
