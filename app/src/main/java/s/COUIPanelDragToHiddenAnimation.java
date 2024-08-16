package s;

import s.DynamicAnimation;

/* compiled from: COUIPanelDragToHiddenAnimation.java */
/* renamed from: s.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUIPanelDragToHiddenAnimation extends DynamicAnimation<COUIPanelDragToHiddenAnimation> {
    private final a A;
    private float B;
    private float C;
    private long D;
    private long E;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIPanelDragToHiddenAnimation.java */
    /* renamed from: s.b$a */
    /* loaded from: classes.dex */
    public static final class a {

        /* renamed from: c, reason: collision with root package name */
        private float f17860c;

        /* renamed from: a, reason: collision with root package name */
        private final DynamicAnimation.p f17858a = new DynamicAnimation.p();

        /* renamed from: b, reason: collision with root package name */
        private float f17859b = -4.2f;

        /* renamed from: d, reason: collision with root package name */
        private long f17861d = 0;

        /* renamed from: e, reason: collision with root package name */
        private long f17862e = 0;

        /* renamed from: f, reason: collision with root package name */
        private float f17863f = 0.0f;

        /* renamed from: g, reason: collision with root package name */
        private float f17864g = 0.0f;

        /* renamed from: h, reason: collision with root package name */
        private float f17865h = 0.0f;

        /* renamed from: i, reason: collision with root package name */
        private float f17866i = 0.0f;

        a() {
        }

        private float g(long j10) {
            long j11 = this.f17862e;
            if (j10 >= j11) {
                return this.f17866i;
            }
            long j12 = this.f17861d;
            float f10 = ((float) (j10 - j12)) / ((float) (j11 - j12));
            float f11 = this.f17865h;
            return f11 + ((this.f17866i - f11) * f10);
        }

        private float h(long j10) {
            long j11 = this.f17862e;
            if (j10 >= j11) {
                return this.f17864g;
            }
            long j12 = this.f17861d;
            float f10 = ((float) (j10 - j12)) / ((float) (j11 - j12));
            float f11 = this.f17863f;
            return f11 + ((this.f17864g - f11) * f10);
        }

        public boolean i(float f10, float f11) {
            return Math.abs(f11) < this.f17860c;
        }

        void j(float f10) {
            this.f17860c = f10 * 62.5f;
        }

        DynamicAnimation.p k(float f10, float f11, long j10, long j11) {
            if (this.f17864g < 0.0f) {
                float f12 = (float) j11;
                this.f17858a.f17896b = (float) (f11 * Math.exp((f12 / 1000.0f) * this.f17859b));
                DynamicAnimation.p pVar = this.f17858a;
                float f13 = this.f17859b;
                pVar.f17895a = (float) ((f10 - (f11 / f13)) + ((f11 / f13) * Math.exp((f13 * f12) / 1000.0f)));
            } else {
                this.f17858a.f17896b = h(j10);
                this.f17858a.f17895a = g(j10);
            }
            DynamicAnimation.p pVar2 = this.f17858a;
            if (i(pVar2.f17895a, pVar2.f17896b)) {
                this.f17858a.f17896b = 0.0f;
            }
            return this.f17858a;
        }
    }

    public <K> COUIPanelDragToHiddenAnimation(K k10, FloatPropertyCompat<K> floatPropertyCompat) {
        super(k10, floatPropertyCompat);
        a aVar = new a();
        this.A = aVar;
        this.B = 0.0f;
        this.C = -1.0f;
        this.D = 0L;
        this.E = 120L;
        aVar.j(f());
    }

    @Override // s.DynamicAnimation
    public void n() {
        long currentTimeMillis = System.currentTimeMillis();
        this.D = currentTimeMillis;
        this.A.f17861d = currentTimeMillis;
        this.A.f17862e = this.D + this.E;
        this.A.f17863f = this.B;
        this.A.f17864g = this.C;
        this.A.f17865h = 0.0f;
        this.A.f17866i = this.f17887g;
        super.n();
    }

    @Override // s.DynamicAnimation
    boolean p(long j10) {
        long currentTimeMillis = System.currentTimeMillis();
        DynamicAnimation.p k10 = this.A.k(this.f17882b, this.f17881a, currentTimeMillis, j10);
        float f10 = k10.f17895a;
        this.f17882b = f10;
        float f11 = k10.f17896b;
        this.f17881a = f11;
        float f12 = this.C;
        if (f12 >= 0.0f && (f11 <= f12 || currentTimeMillis >= this.D + this.E)) {
            this.f17882b = this.f17887g;
            return true;
        }
        float f13 = this.f17888h;
        if (f10 < f13) {
            this.f17882b = f13;
            return true;
        }
        float f14 = this.f17887g;
        if (f10 <= f14) {
            return q(f10, f11);
        }
        this.f17882b = f14;
        return true;
    }

    boolean q(float f10, float f11) {
        return f10 >= this.f17887g || f10 <= this.f17888h || this.A.i(f10, f11);
    }

    public COUIPanelDragToHiddenAnimation r(float f10) {
        if (f10 > 0.0f) {
            this.C = f10;
            return this;
        }
        throw new IllegalArgumentException("Velocity must be positive");
    }

    public COUIPanelDragToHiddenAnimation s(float f10) {
        super.i(f10);
        return this;
    }

    public COUIPanelDragToHiddenAnimation t(float f10) {
        super.j(f10);
        return this;
    }

    public COUIPanelDragToHiddenAnimation u(float f10) {
        super.m(f10);
        this.B = f10;
        return this;
    }
}
