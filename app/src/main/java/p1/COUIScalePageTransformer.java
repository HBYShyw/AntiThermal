package p1;

import android.view.View;
import androidx.viewpager2.widget.ViewPager2;

/* compiled from: COUIScalePageTransformer.java */
/* renamed from: p1.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUIScalePageTransformer implements ViewPager2.k {

    /* renamed from: a, reason: collision with root package name */
    private float f16554a;

    public COUIScalePageTransformer(float f10) {
        this.f16554a = f10;
    }

    @Override // androidx.viewpager2.widget.ViewPager2.k
    public void a(View view, float f10) {
        int width = view.getWidth();
        view.setPivotY(view.getHeight() / 2);
        view.setPivotX(width / 2);
        if (f10 < -1.0f) {
            view.setScaleX(this.f16554a);
            view.setScaleY(this.f16554a);
            view.setPivotX(width);
            return;
        }
        if (f10 > 1.0f) {
            view.setPivotX(0.0f);
            view.setScaleX(this.f16554a);
            view.setScaleY(this.f16554a);
        } else {
            if (f10 < 0.0f) {
                float f11 = this.f16554a;
                float f12 = ((f10 + 1.0f) * (1.0f - f11)) + f11;
                view.setScaleX(f12);
                view.setScaleY(f12);
                view.setPivotX(width * (((-f10) * 0.5f) + 0.5f));
                return;
            }
            float f13 = 1.0f - f10;
            float f14 = this.f16554a;
            float f15 = ((1.0f - f14) * f13) + f14;
            view.setScaleX(f15);
            view.setScaleY(f15);
            view.setPivotX(width * f13 * 0.5f);
        }
    }
}
