package p3;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/* compiled from: MotionTiming.java */
/* renamed from: p3.i, reason: use source file name */
/* loaded from: classes.dex */
public class MotionTiming {

    /* renamed from: a, reason: collision with root package name */
    private long f16570a;

    /* renamed from: b, reason: collision with root package name */
    private long f16571b;

    /* renamed from: c, reason: collision with root package name */
    private TimeInterpolator f16572c;

    /* renamed from: d, reason: collision with root package name */
    private int f16573d;

    /* renamed from: e, reason: collision with root package name */
    private int f16574e;

    public MotionTiming(long j10, long j11) {
        this.f16572c = null;
        this.f16573d = 0;
        this.f16574e = 1;
        this.f16570a = j10;
        this.f16571b = j11;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MotionTiming b(ValueAnimator valueAnimator) {
        MotionTiming motionTiming = new MotionTiming(valueAnimator.getStartDelay(), valueAnimator.getDuration(), f(valueAnimator));
        motionTiming.f16573d = valueAnimator.getRepeatCount();
        motionTiming.f16574e = valueAnimator.getRepeatMode();
        return motionTiming;
    }

    private static TimeInterpolator f(ValueAnimator valueAnimator) {
        TimeInterpolator interpolator = valueAnimator.getInterpolator();
        if (!(interpolator instanceof AccelerateDecelerateInterpolator) && interpolator != null) {
            if (interpolator instanceof AccelerateInterpolator) {
                return AnimationUtils.f16557c;
            }
            return interpolator instanceof DecelerateInterpolator ? AnimationUtils.f16558d : interpolator;
        }
        return AnimationUtils.f16556b;
    }

    public void a(Animator animator) {
        animator.setStartDelay(c());
        animator.setDuration(d());
        animator.setInterpolator(e());
        if (animator instanceof ValueAnimator) {
            ValueAnimator valueAnimator = (ValueAnimator) animator;
            valueAnimator.setRepeatCount(g());
            valueAnimator.setRepeatMode(h());
        }
    }

    public long c() {
        return this.f16570a;
    }

    public long d() {
        return this.f16571b;
    }

    public TimeInterpolator e() {
        TimeInterpolator timeInterpolator = this.f16572c;
        return timeInterpolator != null ? timeInterpolator : AnimationUtils.f16556b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MotionTiming)) {
            return false;
        }
        MotionTiming motionTiming = (MotionTiming) obj;
        if (c() == motionTiming.c() && d() == motionTiming.d() && g() == motionTiming.g() && h() == motionTiming.h()) {
            return e().getClass().equals(motionTiming.e().getClass());
        }
        return false;
    }

    public int g() {
        return this.f16573d;
    }

    public int h() {
        return this.f16574e;
    }

    public int hashCode() {
        return (((((((((int) (c() ^ (c() >>> 32))) * 31) + ((int) (d() ^ (d() >>> 32)))) * 31) + e().getClass().hashCode()) * 31) + g()) * 31) + h();
    }

    public String toString() {
        return '\n' + getClass().getName() + '{' + Integer.toHexString(System.identityHashCode(this)) + " delay: " + c() + " duration: " + d() + " interpolator: " + e().getClass() + " repeatCount: " + g() + " repeatMode: " + h() + "}\n";
    }

    public MotionTiming(long j10, long j11, TimeInterpolator timeInterpolator) {
        this.f16573d = 0;
        this.f16574e = 1;
        this.f16570a = j10;
        this.f16571b = j11;
        this.f16572c = timeInterpolator;
    }
}
