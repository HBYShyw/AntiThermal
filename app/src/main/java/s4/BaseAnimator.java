package s4;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/* compiled from: BaseAnimator.java */
/* renamed from: s4.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class BaseAnimator extends ValueAnimator {

    /* renamed from: e, reason: collision with root package name */
    private final Set<ValueAnimator.AnimatorUpdateListener> f18043e = new CopyOnWriteArraySet();

    /* renamed from: f, reason: collision with root package name */
    private final Set<Animator.AnimatorListener> f18044f = new CopyOnWriteArraySet();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        Iterator<Animator.AnimatorListener> it = this.f18044f.iterator();
        while (it.hasNext()) {
            it.next().onAnimationCancel(this);
        }
    }

    @Override // android.animation.Animator
    public void addListener(Animator.AnimatorListener animatorListener) {
        this.f18044f.add(animatorListener);
    }

    @Override // android.animation.ValueAnimator
    public void addUpdateListener(ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        this.f18043e.add(animatorUpdateListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(boolean z10) {
        Iterator<Animator.AnimatorListener> it = this.f18044f.iterator();
        while (it.hasNext()) {
            it.next().onAnimationEnd(this, z10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c() {
        Iterator<Animator.AnimatorListener> it = this.f18044f.iterator();
        while (it.hasNext()) {
            it.next().onAnimationRepeat(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(boolean z10) {
        Iterator<Animator.AnimatorListener> it = this.f18044f.iterator();
        while (it.hasNext()) {
            it.next().onAnimationStart(this, z10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e() {
        Iterator<ValueAnimator.AnimatorUpdateListener> it = this.f18043e.iterator();
        while (it.hasNext()) {
            it.next().onAnimationUpdate(this);
        }
    }

    @Override // android.animation.ValueAnimator, android.animation.Animator
    public long getStartDelay() {
        throw new UnsupportedOperationException("EffectiveAnimator does not support getStartDelay.");
    }

    @Override // android.animation.Animator
    public void removeAllListeners() {
        this.f18044f.clear();
    }

    @Override // android.animation.ValueAnimator
    public void removeAllUpdateListeners() {
        this.f18043e.clear();
    }

    @Override // android.animation.Animator
    public void removeListener(Animator.AnimatorListener animatorListener) {
        this.f18044f.remove(animatorListener);
    }

    @Override // android.animation.ValueAnimator
    public void removeUpdateListener(ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        this.f18043e.remove(animatorUpdateListener);
    }

    @Override // android.animation.ValueAnimator, android.animation.Animator
    public void setInterpolator(TimeInterpolator timeInterpolator) {
        throw new UnsupportedOperationException("EffectiveAnimator does not support setInterpolator.");
    }

    @Override // android.animation.ValueAnimator, android.animation.Animator
    public void setStartDelay(long j10) {
        throw new UnsupportedOperationException("EffectiveAnimator does not support setStartDelay.");
    }

    @Override // android.animation.ValueAnimator, android.animation.Animator
    public ValueAnimator setDuration(long j10) {
        throw new UnsupportedOperationException("EffectiveAnimator does not support setDuration.");
    }
}
