package j4;

import android.view.animation.Interpolator;
import com.oplus.anim.L;
import java.util.ArrayList;
import java.util.List;
import t4.EffectiveValueCallback;
import t4.Keyframe;

/* compiled from: BaseKeyframeAnimation.java */
/* renamed from: j4.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class BaseKeyframeAnimation<K, A> {

    /* renamed from: c, reason: collision with root package name */
    private final d<K> f12952c;

    /* renamed from: e, reason: collision with root package name */
    protected EffectiveValueCallback<A> f12954e;

    /* renamed from: a, reason: collision with root package name */
    final List<b> f12950a = new ArrayList(1);

    /* renamed from: b, reason: collision with root package name */
    private boolean f12951b = false;

    /* renamed from: d, reason: collision with root package name */
    protected float f12953d = 0.0f;

    /* renamed from: f, reason: collision with root package name */
    private A f12955f = null;

    /* renamed from: g, reason: collision with root package name */
    private float f12956g = -1.0f;

    /* renamed from: h, reason: collision with root package name */
    private float f12957h = -1.0f;

    /* compiled from: BaseKeyframeAnimation.java */
    /* renamed from: j4.a$b */
    /* loaded from: classes.dex */
    public interface b {
        void a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: BaseKeyframeAnimation.java */
    /* renamed from: j4.a$c */
    /* loaded from: classes.dex */
    public static final class c<T> implements d<T> {
        private c() {
        }

        @Override // j4.BaseKeyframeAnimation.d
        public boolean a(float f10) {
            throw new IllegalStateException("not implemented");
        }

        @Override // j4.BaseKeyframeAnimation.d
        public Keyframe<T> b() {
            throw new IllegalStateException("not implemented");
        }

        @Override // j4.BaseKeyframeAnimation.d
        public boolean c(float f10) {
            return false;
        }

        @Override // j4.BaseKeyframeAnimation.d
        public float d() {
            return 1.0f;
        }

        @Override // j4.BaseKeyframeAnimation.d
        public float e() {
            return 0.0f;
        }

        @Override // j4.BaseKeyframeAnimation.d
        public boolean isEmpty() {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: BaseKeyframeAnimation.java */
    /* renamed from: j4.a$d */
    /* loaded from: classes.dex */
    public interface d<T> {
        boolean a(float f10);

        Keyframe<T> b();

        boolean c(float f10);

        float d();

        float e();

        boolean isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: BaseKeyframeAnimation.java */
    /* renamed from: j4.a$e */
    /* loaded from: classes.dex */
    public static final class e<T> implements d<T> {

        /* renamed from: a, reason: collision with root package name */
        private final List<? extends Keyframe<T>> f12958a;

        /* renamed from: c, reason: collision with root package name */
        private Keyframe<T> f12960c = null;

        /* renamed from: d, reason: collision with root package name */
        private float f12961d = -1.0f;

        /* renamed from: b, reason: collision with root package name */
        private Keyframe<T> f12959b = f(0.0f);

        e(List<? extends Keyframe<T>> list) {
            this.f12958a = list;
        }

        private Keyframe<T> f(float f10) {
            List<? extends Keyframe<T>> list = this.f12958a;
            Keyframe<T> keyframe = list.get(list.size() - 1);
            if (f10 >= keyframe.e()) {
                return keyframe;
            }
            for (int size = this.f12958a.size() - 2; size >= 1; size--) {
                Keyframe<T> keyframe2 = this.f12958a.get(size);
                if (this.f12959b != keyframe2 && keyframe2.a(f10)) {
                    return keyframe2;
                }
            }
            return this.f12958a.get(0);
        }

        @Override // j4.BaseKeyframeAnimation.d
        public boolean a(float f10) {
            Keyframe<T> keyframe = this.f12960c;
            Keyframe<T> keyframe2 = this.f12959b;
            if (keyframe == keyframe2 && this.f12961d == f10) {
                return true;
            }
            this.f12960c = keyframe2;
            this.f12961d = f10;
            return false;
        }

        @Override // j4.BaseKeyframeAnimation.d
        public Keyframe<T> b() {
            return this.f12959b;
        }

        @Override // j4.BaseKeyframeAnimation.d
        public boolean c(float f10) {
            if (this.f12959b.a(f10)) {
                return !this.f12959b.h();
            }
            this.f12959b = f(f10);
            return true;
        }

        @Override // j4.BaseKeyframeAnimation.d
        public float d() {
            return this.f12958a.get(r1.size() - 1).b();
        }

        @Override // j4.BaseKeyframeAnimation.d
        public float e() {
            return this.f12958a.get(0).e();
        }

        @Override // j4.BaseKeyframeAnimation.d
        public boolean isEmpty() {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: BaseKeyframeAnimation.java */
    /* renamed from: j4.a$f */
    /* loaded from: classes.dex */
    public static final class f<T> implements d<T> {

        /* renamed from: a, reason: collision with root package name */
        private final Keyframe<T> f12962a;

        /* renamed from: b, reason: collision with root package name */
        private float f12963b = -1.0f;

        f(List<? extends Keyframe<T>> list) {
            this.f12962a = list.get(0);
        }

        @Override // j4.BaseKeyframeAnimation.d
        public boolean a(float f10) {
            if (this.f12963b == f10) {
                return true;
            }
            this.f12963b = f10;
            return false;
        }

        @Override // j4.BaseKeyframeAnimation.d
        public Keyframe<T> b() {
            return this.f12962a;
        }

        @Override // j4.BaseKeyframeAnimation.d
        public boolean c(float f10) {
            return !this.f12962a.h();
        }

        @Override // j4.BaseKeyframeAnimation.d
        public float d() {
            return this.f12962a.b();
        }

        @Override // j4.BaseKeyframeAnimation.d
        public float e() {
            return this.f12962a.e();
        }

        @Override // j4.BaseKeyframeAnimation.d
        public boolean isEmpty() {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseKeyframeAnimation(List<? extends Keyframe<K>> list) {
        this.f12952c = o(list);
    }

    private float g() {
        if (this.f12956g == -1.0f) {
            this.f12956g = this.f12952c.e();
        }
        return this.f12956g;
    }

    private static <T> d<T> o(List<? extends Keyframe<T>> list) {
        if (list.isEmpty()) {
            return new c();
        }
        if (list.size() == 1) {
            return new f(list);
        }
        return new e(list);
    }

    public void a(b bVar) {
        this.f12950a.add(bVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Keyframe<K> b() {
        L.a("BaseKeyframeAnimation#getCurrentKeyframe");
        Keyframe<K> b10 = this.f12952c.b();
        L.b("BaseKeyframeAnimation#getCurrentKeyframe");
        return b10;
    }

    float c() {
        if (this.f12957h == -1.0f) {
            this.f12957h = this.f12952c.d();
        }
        return this.f12957h;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float d() {
        Keyframe<K> b10 = b();
        if (b10.h()) {
            return 0.0f;
        }
        return b10.f18572d.getInterpolation(e());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float e() {
        if (this.f12951b) {
            return 0.0f;
        }
        Keyframe<K> b10 = b();
        if (b10.h()) {
            return 0.0f;
        }
        return (this.f12953d - b10.e()) / (b10.b() - b10.e());
    }

    public float f() {
        return this.f12953d;
    }

    public A h() {
        A i10;
        float e10 = e();
        if (this.f12954e == null && this.f12952c.a(e10)) {
            return this.f12955f;
        }
        Keyframe<K> b10 = b();
        Interpolator interpolator = b10.f18573e;
        if (interpolator != null && b10.f18574f != null) {
            i10 = j(b10, e10, interpolator.getInterpolation(e10), b10.f18574f.getInterpolation(e10));
        } else {
            i10 = i(b10, d());
        }
        this.f12955f = i10;
        return i10;
    }

    abstract A i(Keyframe<K> keyframe, float f10);

    protected A j(Keyframe<K> keyframe, float f10, float f11, float f12) {
        throw new UnsupportedOperationException("This animation does not support split dimensions!");
    }

    public void k() {
        for (int i10 = 0; i10 < this.f12950a.size(); i10++) {
            this.f12950a.get(i10).a();
        }
    }

    public void l() {
        this.f12951b = true;
    }

    public void m(float f10) {
        if (this.f12952c.isEmpty()) {
            return;
        }
        if (f10 < g()) {
            f10 = g();
        } else if (f10 > c()) {
            f10 = c();
        }
        if (f10 == this.f12953d) {
            return;
        }
        this.f12953d = f10;
        if (this.f12952c.c(f10)) {
            k();
        }
    }

    public void n(EffectiveValueCallback<A> effectiveValueCallback) {
        EffectiveValueCallback<A> effectiveValueCallback2 = this.f12954e;
        if (effectiveValueCallback2 != null) {
            effectiveValueCallback2.c(null);
        }
        this.f12954e = effectiveValueCallback;
        if (effectiveValueCallback != null) {
            effectiveValueCallback.c(this);
        }
    }
}
