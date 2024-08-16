package s;

import android.os.Looper;
import android.util.AndroidRuntimeException;
import android.view.View;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import s.AnimationHandler;
import s.DynamicAnimation;

/* compiled from: DynamicAnimation.java */
/* renamed from: s.c, reason: use source file name */
/* loaded from: classes.dex */
public abstract class DynamicAnimation<T extends DynamicAnimation<T>> implements AnimationHandler.b {

    /* renamed from: m, reason: collision with root package name */
    public static final s f17867m = new g("translationX");

    /* renamed from: n, reason: collision with root package name */
    public static final s f17868n = new h("translationY");

    /* renamed from: o, reason: collision with root package name */
    public static final s f17869o = new i("translationZ");

    /* renamed from: p, reason: collision with root package name */
    public static final s f17870p = new j("scaleX");

    /* renamed from: q, reason: collision with root package name */
    public static final s f17871q = new k("scaleY");

    /* renamed from: r, reason: collision with root package name */
    public static final s f17872r = new l("rotation");

    /* renamed from: s, reason: collision with root package name */
    public static final s f17873s = new m("rotationX");

    /* renamed from: t, reason: collision with root package name */
    public static final s f17874t = new n("rotationY");

    /* renamed from: u, reason: collision with root package name */
    public static final s f17875u = new o("x");

    /* renamed from: v, reason: collision with root package name */
    public static final s f17876v = new a("y");

    /* renamed from: w, reason: collision with root package name */
    public static final s f17877w = new b("z");

    /* renamed from: x, reason: collision with root package name */
    public static final s f17878x = new c("alpha");

    /* renamed from: y, reason: collision with root package name */
    public static final s f17879y = new d("scrollX");

    /* renamed from: z, reason: collision with root package name */
    public static final s f17880z = new e("scrollY");

    /* renamed from: a, reason: collision with root package name */
    float f17881a;

    /* renamed from: b, reason: collision with root package name */
    float f17882b;

    /* renamed from: c, reason: collision with root package name */
    boolean f17883c;

    /* renamed from: d, reason: collision with root package name */
    final Object f17884d;

    /* renamed from: e, reason: collision with root package name */
    final FloatPropertyCompat f17885e;

    /* renamed from: f, reason: collision with root package name */
    boolean f17886f;

    /* renamed from: g, reason: collision with root package name */
    float f17887g;

    /* renamed from: h, reason: collision with root package name */
    float f17888h;

    /* renamed from: i, reason: collision with root package name */
    private long f17889i;

    /* renamed from: j, reason: collision with root package name */
    private float f17890j;

    /* renamed from: k, reason: collision with root package name */
    private final ArrayList<q> f17891k;

    /* renamed from: l, reason: collision with root package name */
    private final ArrayList<r> f17892l;

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$a */
    /* loaded from: classes.dex */
    static class a extends s {
        a(String str) {
            super(str, null);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(View view) {
            return view.getY();
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(View view, float f10) {
            view.setY(f10);
        }
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$b */
    /* loaded from: classes.dex */
    static class b extends s {
        b(String str) {
            super(str, null);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(View view) {
            return ViewCompat.J(view);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(View view, float f10) {
            ViewCompat.H0(view, f10);
        }
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$c */
    /* loaded from: classes.dex */
    static class c extends s {
        c(String str) {
            super(str, null);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(View view) {
            return view.getAlpha();
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(View view, float f10) {
            view.setAlpha(f10);
        }
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$d */
    /* loaded from: classes.dex */
    static class d extends s {
        d(String str) {
            super(str, null);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(View view) {
            return view.getScrollX();
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(View view, float f10) {
            view.setScrollX((int) f10);
        }
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$e */
    /* loaded from: classes.dex */
    static class e extends s {
        e(String str) {
            super(str, null);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(View view) {
            return view.getScrollY();
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(View view, float f10) {
            view.setScrollY((int) f10);
        }
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$f */
    /* loaded from: classes.dex */
    class f extends FloatPropertyCompat {

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ s.e f17893b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        f(String str, s.e eVar) {
            super(str);
            this.f17893b = eVar;
        }

        @Override // s.FloatPropertyCompat
        public float a(Object obj) {
            return this.f17893b.a();
        }

        @Override // s.FloatPropertyCompat
        public void b(Object obj, float f10) {
            this.f17893b.b(f10);
        }
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$g */
    /* loaded from: classes.dex */
    static class g extends s {
        g(String str) {
            super(str, null);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(View view) {
            return view.getTranslationX();
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(View view, float f10) {
            view.setTranslationX(f10);
        }
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$h */
    /* loaded from: classes.dex */
    static class h extends s {
        h(String str) {
            super(str, null);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(View view) {
            return view.getTranslationY();
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(View view, float f10) {
            view.setTranslationY(f10);
        }
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$i */
    /* loaded from: classes.dex */
    static class i extends s {
        i(String str) {
            super(str, null);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(View view) {
            return ViewCompat.H(view);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(View view, float f10) {
            ViewCompat.F0(view, f10);
        }
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$j */
    /* loaded from: classes.dex */
    static class j extends s {
        j(String str) {
            super(str, null);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(View view) {
            return view.getScaleX();
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(View view, float f10) {
            view.setScaleX(f10);
        }
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$k */
    /* loaded from: classes.dex */
    static class k extends s {
        k(String str) {
            super(str, null);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(View view) {
            return view.getScaleY();
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(View view, float f10) {
            view.setScaleY(f10);
        }
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$l */
    /* loaded from: classes.dex */
    static class l extends s {
        l(String str) {
            super(str, null);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(View view) {
            return view.getRotation();
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(View view, float f10) {
            view.setRotation(f10);
        }
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$m */
    /* loaded from: classes.dex */
    static class m extends s {
        m(String str) {
            super(str, null);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(View view) {
            return view.getRotationX();
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(View view, float f10) {
            view.setRotationX(f10);
        }
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$n */
    /* loaded from: classes.dex */
    static class n extends s {
        n(String str) {
            super(str, null);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(View view) {
            return view.getRotationY();
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(View view, float f10) {
            view.setRotationY(f10);
        }
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$o */
    /* loaded from: classes.dex */
    static class o extends s {
        o(String str) {
            super(str, null);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(View view) {
            return view.getX();
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(View view, float f10) {
            view.setX(f10);
        }
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$p */
    /* loaded from: classes.dex */
    static class p {

        /* renamed from: a, reason: collision with root package name */
        float f17895a;

        /* renamed from: b, reason: collision with root package name */
        float f17896b;
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$q */
    /* loaded from: classes.dex */
    public interface q {
        void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z10, float f10, float f11);
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$r */
    /* loaded from: classes.dex */
    public interface r {
        void a(DynamicAnimation dynamicAnimation, float f10, float f11);
    }

    /* compiled from: DynamicAnimation.java */
    /* renamed from: s.c$s */
    /* loaded from: classes.dex */
    public static abstract class s extends FloatPropertyCompat<View> {
        /* synthetic */ s(String str, g gVar) {
            this(str);
        }

        private s(String str) {
            super(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DynamicAnimation(s.e eVar) {
        this.f17881a = 0.0f;
        this.f17882b = Float.MAX_VALUE;
        this.f17883c = false;
        this.f17886f = false;
        this.f17887g = Float.MAX_VALUE;
        this.f17888h = -Float.MAX_VALUE;
        this.f17889i = 0L;
        this.f17891k = new ArrayList<>();
        this.f17892l = new ArrayList<>();
        this.f17884d = null;
        this.f17885e = new f("FloatValueHolder", eVar);
        this.f17890j = 1.0f;
    }

    private void d(boolean z10) {
        this.f17886f = false;
        AnimationHandler.d().g(this);
        this.f17889i = 0L;
        this.f17883c = false;
        for (int i10 = 0; i10 < this.f17891k.size(); i10++) {
            if (this.f17891k.get(i10) != null) {
                this.f17891k.get(i10).onAnimationEnd(this, z10, this.f17882b, this.f17881a);
            }
        }
        h(this.f17891k);
    }

    private float e() {
        return this.f17885e.a(this.f17884d);
    }

    private static <T> void h(ArrayList<T> arrayList) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            if (arrayList.get(size) == null) {
                arrayList.remove(size);
            }
        }
    }

    private void o() {
        if (this.f17886f) {
            return;
        }
        this.f17886f = true;
        if (!this.f17883c) {
            this.f17882b = e();
        }
        float f10 = this.f17882b;
        if (f10 <= this.f17887g && f10 >= this.f17888h) {
            AnimationHandler.d().a(this, 0L);
            return;
        }
        throw new IllegalArgumentException("Starting value need to be in between min value and max value");
    }

    public T a(q qVar) {
        if (!this.f17891k.contains(qVar)) {
            this.f17891k.add(qVar);
        }
        return this;
    }

    public T b(r rVar) {
        if (!g()) {
            if (!this.f17892l.contains(rVar)) {
                this.f17892l.add(rVar);
            }
            return this;
        }
        throw new UnsupportedOperationException("Error: Update listeners must be added beforethe animation.");
    }

    public void c() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (this.f17886f) {
                d(true);
                return;
            }
            return;
        }
        throw new AndroidRuntimeException("Animations may only be canceled on the main thread");
    }

    @Override // s.AnimationHandler.b
    public boolean doAnimationFrame(long j10) {
        long j11 = this.f17889i;
        if (j11 == 0) {
            this.f17889i = j10;
            k(this.f17882b);
            return false;
        }
        this.f17889i = j10;
        boolean p10 = p(j10 - j11);
        float min = Math.min(this.f17882b, this.f17887g);
        this.f17882b = min;
        float max = Math.max(min, this.f17888h);
        this.f17882b = max;
        k(max);
        if (p10) {
            d(false);
        }
        return p10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float f() {
        return this.f17890j * 0.75f;
    }

    public boolean g() {
        return this.f17886f;
    }

    public T i(float f10) {
        this.f17887g = f10;
        return this;
    }

    public T j(float f10) {
        this.f17888h = f10;
        return this;
    }

    void k(float f10) {
        this.f17885e.b(this.f17884d, f10);
        for (int i10 = 0; i10 < this.f17892l.size(); i10++) {
            if (this.f17892l.get(i10) != null) {
                this.f17892l.get(i10).a(this, this.f17882b, this.f17881a);
            }
        }
        h(this.f17892l);
    }

    public T l(float f10) {
        this.f17882b = f10;
        this.f17883c = true;
        return this;
    }

    public T m(float f10) {
        this.f17881a = f10;
        return this;
    }

    public void n() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (this.f17886f) {
                return;
            }
            o();
            return;
        }
        throw new AndroidRuntimeException("Animations may only be started on the main thread");
    }

    abstract boolean p(long j10);

    /* JADX INFO: Access modifiers changed from: package-private */
    public <K> DynamicAnimation(K k10, FloatPropertyCompat<K> floatPropertyCompat) {
        this.f17881a = 0.0f;
        this.f17882b = Float.MAX_VALUE;
        this.f17883c = false;
        this.f17886f = false;
        this.f17887g = Float.MAX_VALUE;
        this.f17888h = -Float.MAX_VALUE;
        this.f17889i = 0L;
        this.f17891k = new ArrayList<>();
        this.f17892l = new ArrayList<>();
        this.f17884d = k10;
        this.f17885e = floatPropertyCompat;
        if (floatPropertyCompat != f17872r && floatPropertyCompat != f17873s && floatPropertyCompat != f17874t) {
            if (floatPropertyCompat == f17878x) {
                this.f17890j = 0.00390625f;
                return;
            } else if (floatPropertyCompat != f17870p && floatPropertyCompat != f17871q) {
                this.f17890j = 1.0f;
                return;
            } else {
                this.f17890j = 0.00390625f;
                return;
            }
        }
        this.f17890j = 0.1f;
    }
}
