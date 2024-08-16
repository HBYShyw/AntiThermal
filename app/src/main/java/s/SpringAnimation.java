package s;

import android.os.Looper;
import android.util.AndroidRuntimeException;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import s.DynamicAnimation;

/* compiled from: SpringAnimation.java */
/* renamed from: s.f, reason: use source file name */
/* loaded from: classes.dex */
public final class SpringAnimation extends DynamicAnimation<SpringAnimation> {
    private SpringForce A;
    private float B;
    private boolean C;

    public SpringAnimation(e eVar) {
        super(eVar);
        this.A = null;
        this.B = Float.MAX_VALUE;
        this.C = false;
    }

    private void u() {
        SpringForce springForce = this.A;
        if (springForce != null) {
            double a10 = springForce.a();
            if (a10 > this.f17887g) {
                throw new UnsupportedOperationException("Final position of the spring cannot be greater than the max value.");
            }
            if (a10 < this.f17888h) {
                throw new UnsupportedOperationException("Final position of the spring cannot be less than the min value.");
            }
            return;
        }
        throw new UnsupportedOperationException("Incomplete SpringAnimation: Either final position or a spring force needs to be set.");
    }

    @Override // s.DynamicAnimation
    public void n() {
        u();
        this.A.g(f());
        super.n();
    }

    @Override // s.DynamicAnimation
    boolean p(long j10) {
        if (this.C) {
            float f10 = this.B;
            if (f10 != Float.MAX_VALUE) {
                this.A.e(f10);
                this.B = Float.MAX_VALUE;
            }
            this.f17882b = this.A.a();
            this.f17881a = 0.0f;
            this.C = false;
            return true;
        }
        if (this.B != Float.MAX_VALUE) {
            this.A.a();
            long j11 = j10 / 2;
            DynamicAnimation.p h10 = this.A.h(this.f17882b, this.f17881a, j11);
            this.A.e(this.B);
            this.B = Float.MAX_VALUE;
            DynamicAnimation.p h11 = this.A.h(h10.f17895a, h10.f17896b, j11);
            this.f17882b = h11.f17895a;
            this.f17881a = h11.f17896b;
        } else {
            DynamicAnimation.p h12 = this.A.h(this.f17882b, this.f17881a, j10);
            this.f17882b = h12.f17895a;
            this.f17881a = h12.f17896b;
        }
        float max = Math.max(this.f17882b, this.f17888h);
        this.f17882b = max;
        float min = Math.min(max, this.f17887g);
        this.f17882b = min;
        if (!t(min, this.f17881a)) {
            return false;
        }
        this.f17882b = this.A.a();
        this.f17881a = 0.0f;
        return true;
    }

    public void q(float f10) {
        if (g()) {
            this.B = f10;
            return;
        }
        if (this.A == null) {
            this.A = new SpringForce(f10);
        }
        this.A.e(f10);
        n();
    }

    public boolean r() {
        return this.A.f17900b > UserProfileInfo.Constant.NA_LAT_LON;
    }

    public SpringForce s() {
        return this.A;
    }

    boolean t(float f10, float f11) {
        return this.A.c(f10, f11);
    }

    public SpringAnimation v(SpringForce springForce) {
        this.A = springForce;
        return this;
    }

    public void w() {
        if (r()) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                if (this.f17886f) {
                    this.C = true;
                    return;
                }
                return;
            }
            throw new AndroidRuntimeException("Animations may only be started on the main thread");
        }
        throw new UnsupportedOperationException("Spring animations can only come to an end when there is damping");
    }

    public <K> SpringAnimation(K k10, FloatPropertyCompat<K> floatPropertyCompat) {
        super(k10, floatPropertyCompat);
        this.A = null;
        this.B = Float.MAX_VALUE;
        this.C = false;
    }

    public <K> SpringAnimation(K k10, FloatPropertyCompat<K> floatPropertyCompat, float f10) {
        super(k10, floatPropertyCompat);
        this.A = null;
        this.B = Float.MAX_VALUE;
        this.C = false;
        this.A = new SpringForce(f10);
    }
}
