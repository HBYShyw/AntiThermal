package i3;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import androidx.viewpager2.widget.ViewPager2;
import java.lang.ref.WeakReference;
import l1.COUIAnimatorListener;

/* compiled from: COUIViewPager2SlideHelper.java */
/* renamed from: i3.c, reason: use source file name */
/* loaded from: classes.dex */
public class COUIViewPager2SlideHelper {

    /* renamed from: a, reason: collision with root package name */
    private final WeakReference<ViewPager2> f12494a;

    /* renamed from: b, reason: collision with root package name */
    private long f12495b = 200;

    /* renamed from: c, reason: collision with root package name */
    private Interpolator f12496c = new LinearInterpolator();

    /* renamed from: d, reason: collision with root package name */
    private int f12497d = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIViewPager2SlideHelper.java */
    /* renamed from: i3.c$a */
    /* loaded from: classes.dex */
    public class a extends COUIAnimatorListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ ViewPager2 f12498a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ float[] f12499b;

        a(ViewPager2 viewPager2, float[] fArr) {
            this.f12498a = viewPager2;
            this.f12499b = fArr;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f12498a.b();
            this.f12499b[0] = 0.0f;
        }
    }

    public COUIViewPager2SlideHelper(ViewPager2 viewPager2) {
        this.f12494a = new WeakReference<>(viewPager2);
    }

    private void b(final ViewPager2 viewPager2, final boolean z10, final int i10) {
        if (viewPager2.f()) {
            return;
        }
        viewPager2.a();
        final float[] fArr = {0.0f};
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(this.f12495b);
        ofFloat.setInterpolator(this.f12496c);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: i3.b
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                COUIViewPager2SlideHelper.d(i10, viewPager2, z10, fArr, valueAnimator);
            }
        });
        ofFloat.addListener(new a(viewPager2, fArr));
        ofFloat.start();
    }

    private int c() {
        int height;
        ViewPager2 viewPager2 = this.f12494a.get();
        if (viewPager2.getOrientation() == 0) {
            height = viewPager2.getWidth();
        } else {
            height = viewPager2.getHeight();
        }
        return height + this.f12497d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void d(int i10, ViewPager2 viewPager2, boolean z10, float[] fArr, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue() * i10;
        viewPager2.d(z10 ? (-floatValue) + fArr[0] : floatValue - fArr[0]);
        fArr[0] = floatValue;
    }

    public void e() {
        if (this.f12494a.get() == null) {
            return;
        }
        b(this.f12494a.get(), true, c());
    }

    public void f(long j10) {
        this.f12495b = j10;
    }

    public void g(Interpolator interpolator) {
        this.f12496c = interpolator;
    }

    public void h(int i10) {
        this.f12497d = i10;
    }
}
