package s4;

import android.view.Choreographer;
import com.oplus.anim.EffectiveAnimationComposition;
import com.oplus.anim.L;

/* compiled from: EffectiveValueAnimator.java */
/* renamed from: s4.b, reason: use source file name */
/* loaded from: classes.dex */
public class EffectiveValueAnimator extends BaseAnimator implements Choreographer.FrameCallback {

    /* renamed from: n, reason: collision with root package name */
    private EffectiveAnimationComposition f18052n;

    /* renamed from: g, reason: collision with root package name */
    private float f18045g = 1.0f;

    /* renamed from: h, reason: collision with root package name */
    private boolean f18046h = false;

    /* renamed from: i, reason: collision with root package name */
    private long f18047i = 0;

    /* renamed from: j, reason: collision with root package name */
    private float f18048j = 0.0f;

    /* renamed from: k, reason: collision with root package name */
    private int f18049k = 0;

    /* renamed from: l, reason: collision with root package name */
    private float f18050l = -2.14748365E9f;

    /* renamed from: m, reason: collision with root package name */
    private float f18051m = 2.14748365E9f;

    /* renamed from: o, reason: collision with root package name */
    protected boolean f18053o = false;

    private void C() {
        if (this.f18052n == null) {
            return;
        }
        float f10 = this.f18048j;
        if (f10 < this.f18050l || f10 > this.f18051m) {
            throw new IllegalStateException(String.format("Frame must be [%f,%f]. It is %f", Float.valueOf(this.f18050l), Float.valueOf(this.f18051m), Float.valueOf(this.f18048j)));
        }
    }

    private float j() {
        EffectiveAnimationComposition effectiveAnimationComposition = this.f18052n;
        if (effectiveAnimationComposition == null) {
            return Float.MAX_VALUE;
        }
        return (1.0E9f / effectiveAnimationComposition.j()) / Math.abs(this.f18045g);
    }

    private boolean o() {
        return n() < 0.0f;
    }

    public void A(int i10) {
        z(i10, (int) this.f18051m);
    }

    public void B(float f10) {
        this.f18045g = f10;
    }

    @Override // android.animation.ValueAnimator, android.animation.Animator
    public void cancel() {
        a();
        s();
    }

    @Override // android.view.Choreographer.FrameCallback
    public void doFrame(long j10) {
        r();
        if (this.f18052n == null || !isRunning()) {
            return;
        }
        L.a("EffectiveValueAnimator#doFrame");
        long j11 = this.f18047i;
        float j12 = ((float) (j11 != 0 ? j10 - j11 : 0L)) / j();
        float f10 = this.f18048j;
        if (o()) {
            j12 = -j12;
        }
        float f11 = f10 + j12;
        this.f18048j = f11;
        boolean z10 = !MiscUtils.e(f11, l(), k());
        this.f18048j = MiscUtils.c(this.f18048j, l(), k());
        this.f18047i = j10;
        e();
        if (z10) {
            if (getRepeatCount() != -1 && this.f18049k >= getRepeatCount()) {
                this.f18048j = this.f18045g < 0.0f ? l() : k();
                s();
                b(o());
            } else {
                c();
                this.f18049k++;
                if (getRepeatMode() == 2) {
                    this.f18046h = !this.f18046h;
                    v();
                } else {
                    this.f18048j = o() ? k() : l();
                }
                this.f18047i = j10;
            }
        }
        C();
        L.b("EffectiveValueAnimator#doFrame");
    }

    public void f() {
        this.f18052n = null;
        this.f18050l = -2.14748365E9f;
        this.f18051m = 2.14748365E9f;
    }

    public void g() {
        s();
        b(o());
    }

    @Override // android.animation.ValueAnimator
    public float getAnimatedFraction() {
        float l10;
        float k10;
        float l11;
        if (this.f18052n == null) {
            return 0.0f;
        }
        if (o()) {
            l10 = k() - this.f18048j;
            k10 = k();
            l11 = l();
        } else {
            l10 = this.f18048j - l();
            k10 = k();
            l11 = l();
        }
        return l10 / (k10 - l11);
    }

    @Override // android.animation.ValueAnimator
    public Object getAnimatedValue() {
        return Float.valueOf(h());
    }

    @Override // android.animation.ValueAnimator, android.animation.Animator
    public long getDuration() {
        if (this.f18052n == null) {
            return 0L;
        }
        return r2.e();
    }

    public float h() {
        EffectiveAnimationComposition effectiveAnimationComposition = this.f18052n;
        if (effectiveAnimationComposition == null) {
            return 0.0f;
        }
        return (this.f18048j - effectiveAnimationComposition.q()) / (this.f18052n.g() - this.f18052n.q());
    }

    public float i() {
        return this.f18048j;
    }

    @Override // android.animation.ValueAnimator, android.animation.Animator
    public boolean isRunning() {
        return this.f18053o;
    }

    public float k() {
        EffectiveAnimationComposition effectiveAnimationComposition = this.f18052n;
        if (effectiveAnimationComposition == null) {
            return 0.0f;
        }
        float f10 = this.f18051m;
        return f10 == 2.14748365E9f ? effectiveAnimationComposition.g() : f10;
    }

    public float l() {
        EffectiveAnimationComposition effectiveAnimationComposition = this.f18052n;
        if (effectiveAnimationComposition == null) {
            return 0.0f;
        }
        float f10 = this.f18050l;
        return f10 == -2.14748365E9f ? effectiveAnimationComposition.q() : f10;
    }

    public float n() {
        return this.f18045g;
    }

    public void p() {
        s();
    }

    public void q() {
        this.f18053o = true;
        d(o());
        x((int) (o() ? k() : l()));
        this.f18047i = 0L;
        this.f18049k = 0;
        r();
    }

    protected void r() {
        if (isRunning()) {
            t(false);
            if (Choreographer.getInstance() == null) {
                e.a("Gets the choreographer is null");
            } else {
                Choreographer.getInstance().postFrameCallback(this);
            }
        }
    }

    protected void s() {
        if (Choreographer.getInstance() == null) {
            e.a("Gets the choreographer is null");
        } else {
            t(true);
        }
    }

    @Override // android.animation.ValueAnimator
    public void setRepeatMode(int i10) {
        super.setRepeatMode(i10);
        if (i10 == 2 || !this.f18046h) {
            return;
        }
        this.f18046h = false;
        v();
    }

    protected void t(boolean z10) {
        Choreographer.getInstance().removeFrameCallback(this);
        if (z10) {
            this.f18053o = false;
        }
    }

    public void u() {
        this.f18053o = true;
        r();
        this.f18047i = 0L;
        if (o() && i() == l()) {
            this.f18048j = k();
        } else {
            if (o() || i() != k()) {
                return;
            }
            this.f18048j = l();
        }
    }

    public void v() {
        B(-n());
    }

    public void w(EffectiveAnimationComposition effectiveAnimationComposition) {
        boolean z10 = this.f18052n == null;
        this.f18052n = effectiveAnimationComposition;
        if (z10) {
            z((int) Math.max(this.f18050l, effectiveAnimationComposition.q()), (int) Math.min(this.f18051m, effectiveAnimationComposition.g()));
        } else {
            z((int) effectiveAnimationComposition.q(), (int) effectiveAnimationComposition.g());
        }
        float f10 = this.f18048j;
        this.f18048j = 0.0f;
        x((int) f10);
        e();
    }

    public void x(float f10) {
        if (this.f18048j == f10) {
            return;
        }
        this.f18048j = MiscUtils.c(f10, l(), k());
        this.f18047i = 0L;
        e();
    }

    public void y(float f10) {
        z(this.f18050l, f10);
    }

    public void z(float f10, float f11) {
        if (f10 <= f11) {
            EffectiveAnimationComposition effectiveAnimationComposition = this.f18052n;
            float q10 = effectiveAnimationComposition == null ? -3.4028235E38f : effectiveAnimationComposition.q();
            EffectiveAnimationComposition effectiveAnimationComposition2 = this.f18052n;
            float g6 = effectiveAnimationComposition2 == null ? Float.MAX_VALUE : effectiveAnimationComposition2.g();
            float c10 = MiscUtils.c(f10, q10, g6);
            float c11 = MiscUtils.c(f11, q10, g6);
            if (c10 == this.f18050l && c11 == this.f18051m) {
                return;
            }
            this.f18050l = c10;
            this.f18051m = c11;
            x((int) MiscUtils.c(this.f18048j, c10, c11));
            return;
        }
        throw new IllegalArgumentException(String.format("minFrame (%s) must be <= maxFrame (%s)", Float.valueOf(f10), Float.valueOf(f11)));
    }
}
