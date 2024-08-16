package p1;

import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

/* compiled from: COUIMarginPageTransformer.java */
/* renamed from: p1.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIMarginPageTransformer implements ViewPager2.k {

    /* renamed from: a, reason: collision with root package name */
    private final int f16553a;

    public COUIMarginPageTransformer(int i10) {
        if (i10 >= 0) {
            this.f16553a = i10;
            return;
        }
        throw new IllegalArgumentException("Margin must be non-negative");
    }

    private ViewPager2 b(View view) {
        ViewParent parent = view.getParent();
        ViewParent parent2 = parent.getParent();
        if ((parent instanceof RecyclerView) && (parent2 instanceof ViewPager2)) {
            return (ViewPager2) parent2;
        }
        return null;
    }

    @Override // androidx.viewpager2.widget.ViewPager2.k
    public void a(View view, float f10) {
        ViewPager2 b10 = b(view);
        if (b10 == null) {
            Log.w("COUI-MPTransformer", "transformPage viewPager == null");
            return;
        }
        float f11 = this.f16553a * f10;
        if (b10.getOrientation() == 0) {
            if (ViewCompat.x(b10) == 1) {
                f11 = -f11;
            }
            view.setTranslationX(f11);
            return;
        }
        view.setTranslationY(f11);
    }
}
