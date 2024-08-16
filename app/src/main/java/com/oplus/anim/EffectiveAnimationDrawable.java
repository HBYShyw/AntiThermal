package com.oplus.anim;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import k4.FontAssetManager;
import k4.ImageAssetManager;
import l4.KeyPath;
import l4.Marker;
import o4.CompositionLayer;
import q4.LayerParser;
import s4.EffectiveValueAnimator;
import s4.MiscUtils;
import t4.EffectiveValueCallback;

/* compiled from: EffectiveAnimationDrawable.java */
/* renamed from: com.oplus.anim.b, reason: use source file name */
/* loaded from: classes.dex */
public class EffectiveAnimationDrawable extends Drawable implements Drawable.Callback, Animatable {

    /* renamed from: e, reason: collision with root package name */
    private final Matrix f9626e = new Matrix();

    /* renamed from: f, reason: collision with root package name */
    private EffectiveAnimationComposition f9627f;

    /* renamed from: g, reason: collision with root package name */
    private final EffectiveValueAnimator f9628g;

    /* renamed from: h, reason: collision with root package name */
    private float f9629h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f9630i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f9631j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f9632k;

    /* renamed from: l, reason: collision with root package name */
    private final ArrayList<o> f9633l;

    /* renamed from: m, reason: collision with root package name */
    private final ValueAnimator.AnimatorUpdateListener f9634m;

    /* renamed from: n, reason: collision with root package name */
    private ImageAssetManager f9635n;

    /* renamed from: o, reason: collision with root package name */
    private String f9636o;

    /* renamed from: p, reason: collision with root package name */
    private ImageAssetDelegate f9637p;

    /* renamed from: q, reason: collision with root package name */
    private FontAssetManager f9638q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f9639r;

    /* renamed from: s, reason: collision with root package name */
    private CompositionLayer f9640s;

    /* renamed from: t, reason: collision with root package name */
    private int f9641t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f9642u;

    /* renamed from: v, reason: collision with root package name */
    private boolean f9643v;

    /* renamed from: w, reason: collision with root package name */
    private boolean f9644w;

    /* renamed from: x, reason: collision with root package name */
    private boolean f9645x;

    /* renamed from: y, reason: collision with root package name */
    private boolean f9646y;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveAnimationDrawable.java */
    /* renamed from: com.oplus.anim.b$a */
    /* loaded from: classes.dex */
    public class a implements o {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f9647a;

        a(String str) {
            this.f9647a = str;
        }

        @Override // com.oplus.anim.EffectiveAnimationDrawable.o
        public void a(EffectiveAnimationComposition effectiveAnimationComposition) {
            EffectiveAnimationDrawable.this.a0(this.f9647a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveAnimationDrawable.java */
    /* renamed from: com.oplus.anim.b$b */
    /* loaded from: classes.dex */
    public class b implements o {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f9649a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f9650b;

        b(int i10, int i11) {
            this.f9649a = i10;
            this.f9650b = i11;
        }

        @Override // com.oplus.anim.EffectiveAnimationDrawable.o
        public void a(EffectiveAnimationComposition effectiveAnimationComposition) {
            EffectiveAnimationDrawable.this.Z(this.f9649a, this.f9650b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveAnimationDrawable.java */
    /* renamed from: com.oplus.anim.b$c */
    /* loaded from: classes.dex */
    public class c implements o {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f9652a;

        c(int i10) {
            this.f9652a = i10;
        }

        @Override // com.oplus.anim.EffectiveAnimationDrawable.o
        public void a(EffectiveAnimationComposition effectiveAnimationComposition) {
            EffectiveAnimationDrawable.this.S(this.f9652a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveAnimationDrawable.java */
    /* renamed from: com.oplus.anim.b$d */
    /* loaded from: classes.dex */
    public class d implements o {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ float f9654a;

        d(float f10) {
            this.f9654a = f10;
        }

        @Override // com.oplus.anim.EffectiveAnimationDrawable.o
        public void a(EffectiveAnimationComposition effectiveAnimationComposition) {
            EffectiveAnimationDrawable.this.g0(this.f9654a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveAnimationDrawable.java */
    /* renamed from: com.oplus.anim.b$e */
    /* loaded from: classes.dex */
    public class e implements o {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ KeyPath f9656a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ Object f9657b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ EffectiveValueCallback f9658c;

        e(KeyPath keyPath, Object obj, EffectiveValueCallback effectiveValueCallback) {
            this.f9656a = keyPath;
            this.f9657b = obj;
            this.f9658c = effectiveValueCallback;
        }

        @Override // com.oplus.anim.EffectiveAnimationDrawable.o
        public void a(EffectiveAnimationComposition effectiveAnimationComposition) {
            EffectiveAnimationDrawable.this.d(this.f9656a, this.f9657b, this.f9658c);
        }
    }

    /* compiled from: EffectiveAnimationDrawable.java */
    /* renamed from: com.oplus.anim.b$f */
    /* loaded from: classes.dex */
    class f implements ValueAnimator.AnimatorUpdateListener {
        f() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (EffectiveAnimationDrawable.this.f9640s != null) {
                EffectiveAnimationDrawable.this.f9640s.K(EffectiveAnimationDrawable.this.f9628g.h());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveAnimationDrawable.java */
    /* renamed from: com.oplus.anim.b$g */
    /* loaded from: classes.dex */
    public class g implements o {
        g() {
        }

        @Override // com.oplus.anim.EffectiveAnimationDrawable.o
        public void a(EffectiveAnimationComposition effectiveAnimationComposition) {
            EffectiveAnimationDrawable.this.L();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveAnimationDrawable.java */
    /* renamed from: com.oplus.anim.b$h */
    /* loaded from: classes.dex */
    public class h implements o {
        h() {
        }

        @Override // com.oplus.anim.EffectiveAnimationDrawable.o
        public void a(EffectiveAnimationComposition effectiveAnimationComposition) {
            EffectiveAnimationDrawable.this.O();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveAnimationDrawable.java */
    /* renamed from: com.oplus.anim.b$i */
    /* loaded from: classes.dex */
    public class i implements o {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f9663a;

        i(int i10) {
            this.f9663a = i10;
        }

        @Override // com.oplus.anim.EffectiveAnimationDrawable.o
        public void a(EffectiveAnimationComposition effectiveAnimationComposition) {
            EffectiveAnimationDrawable.this.b0(this.f9663a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveAnimationDrawable.java */
    /* renamed from: com.oplus.anim.b$j */
    /* loaded from: classes.dex */
    public class j implements o {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ float f9665a;

        j(float f10) {
            this.f9665a = f10;
        }

        @Override // com.oplus.anim.EffectiveAnimationDrawable.o
        public void a(EffectiveAnimationComposition effectiveAnimationComposition) {
            EffectiveAnimationDrawable.this.d0(this.f9665a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveAnimationDrawable.java */
    /* renamed from: com.oplus.anim.b$k */
    /* loaded from: classes.dex */
    public class k implements o {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f9667a;

        k(int i10) {
            this.f9667a = i10;
        }

        @Override // com.oplus.anim.EffectiveAnimationDrawable.o
        public void a(EffectiveAnimationComposition effectiveAnimationComposition) {
            EffectiveAnimationDrawable.this.W(this.f9667a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveAnimationDrawable.java */
    /* renamed from: com.oplus.anim.b$l */
    /* loaded from: classes.dex */
    public class l implements o {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ float f9669a;

        l(float f10) {
            this.f9669a = f10;
        }

        @Override // com.oplus.anim.EffectiveAnimationDrawable.o
        public void a(EffectiveAnimationComposition effectiveAnimationComposition) {
            EffectiveAnimationDrawable.this.Y(this.f9669a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveAnimationDrawable.java */
    /* renamed from: com.oplus.anim.b$m */
    /* loaded from: classes.dex */
    public class m implements o {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f9671a;

        m(String str) {
            this.f9671a = str;
        }

        @Override // com.oplus.anim.EffectiveAnimationDrawable.o
        public void a(EffectiveAnimationComposition effectiveAnimationComposition) {
            EffectiveAnimationDrawable.this.c0(this.f9671a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveAnimationDrawable.java */
    /* renamed from: com.oplus.anim.b$n */
    /* loaded from: classes.dex */
    public class n implements o {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f9673a;

        n(String str) {
            this.f9673a = str;
        }

        @Override // com.oplus.anim.EffectiveAnimationDrawable.o
        public void a(EffectiveAnimationComposition effectiveAnimationComposition) {
            EffectiveAnimationDrawable.this.X(this.f9673a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: EffectiveAnimationDrawable.java */
    /* renamed from: com.oplus.anim.b$o */
    /* loaded from: classes.dex */
    public interface o {
        void a(EffectiveAnimationComposition effectiveAnimationComposition);
    }

    public EffectiveAnimationDrawable() {
        EffectiveValueAnimator effectiveValueAnimator = new EffectiveValueAnimator();
        this.f9628g = effectiveValueAnimator;
        this.f9629h = 1.0f;
        this.f9630i = true;
        this.f9631j = false;
        this.f9632k = false;
        this.f9633l = new ArrayList<>();
        f fVar = new f();
        this.f9634m = fVar;
        this.f9641t = 255;
        this.f9645x = true;
        this.f9646y = false;
        effectiveValueAnimator.addUpdateListener(fVar);
    }

    private boolean e() {
        return this.f9630i || this.f9631j;
    }

    private float f(Rect rect) {
        return rect.width() / rect.height();
    }

    private boolean g() {
        EffectiveAnimationComposition effectiveAnimationComposition = this.f9627f;
        return effectiveAnimationComposition == null || getBounds().isEmpty() || f(getBounds()) == f(effectiveAnimationComposition.b());
    }

    private void h() {
        CompositionLayer compositionLayer = new CompositionLayer(this, LayerParser.a(this.f9627f), this.f9627f.l(), this.f9627f);
        this.f9640s = compositionLayer;
        if (this.f9643v) {
            compositionLayer.I(true);
        }
    }

    private void k(Canvas canvas) {
        if (!g()) {
            l(canvas);
        } else {
            m(canvas);
        }
    }

    private void l(Canvas canvas) {
        float f10;
        CompositionLayer compositionLayer = this.f9640s;
        EffectiveAnimationComposition effectiveAnimationComposition = this.f9627f;
        if (compositionLayer == null || effectiveAnimationComposition == null) {
            return;
        }
        int i10 = -1;
        Rect bounds = getBounds();
        float width = bounds.width() / effectiveAnimationComposition.b().width();
        float height = bounds.height() / effectiveAnimationComposition.b().height();
        if (this.f9645x) {
            float min = Math.min(width, height);
            if (min < 1.0f) {
                f10 = 1.0f / min;
                width /= f10;
                height /= f10;
            } else {
                f10 = 1.0f;
            }
            if (f10 > 1.0f) {
                i10 = canvas.save();
                float width2 = bounds.width() / 2.0f;
                float height2 = bounds.height() / 2.0f;
                float f11 = width2 * min;
                float f12 = min * height2;
                canvas.translate(width2 - f11, height2 - f12);
                canvas.scale(f10, f10, f11, f12);
            }
        }
        this.f9626e.reset();
        this.f9626e.preScale(width, height);
        compositionLayer.g(canvas, this.f9626e, this.f9641t);
        if (i10 > 0) {
            canvas.restoreToCount(i10);
        }
    }

    private void m(Canvas canvas) {
        float f10;
        CompositionLayer compositionLayer = this.f9640s;
        EffectiveAnimationComposition effectiveAnimationComposition = this.f9627f;
        if (compositionLayer == null || effectiveAnimationComposition == null) {
            return;
        }
        float f11 = this.f9629h;
        float y4 = y(canvas, effectiveAnimationComposition);
        if (f11 > y4) {
            f10 = this.f9629h / y4;
        } else {
            y4 = f11;
            f10 = 1.0f;
        }
        int i10 = -1;
        if (f10 > 1.0f) {
            i10 = canvas.save();
            float width = effectiveAnimationComposition.b().width() / 2.0f;
            float height = effectiveAnimationComposition.b().height() / 2.0f;
            float f12 = width * y4;
            float f13 = height * y4;
            canvas.translate((E() * width) - f12, (E() * height) - f13);
            canvas.scale(f10, f10, f12, f13);
        }
        this.f9626e.reset();
        this.f9626e.preScale(y4, y4);
        compositionLayer.g(canvas, this.f9626e, this.f9641t);
        if (i10 > 0) {
            canvas.restoreToCount(i10);
        }
    }

    private Context r() {
        Drawable.Callback callback = getCallback();
        if (callback != null && (callback instanceof View)) {
            return ((View) callback).getContext();
        }
        return null;
    }

    private FontAssetManager s() {
        if (getCallback() == null) {
            return null;
        }
        if (this.f9638q == null) {
            this.f9638q = new FontAssetManager(getCallback(), null);
        }
        return this.f9638q;
    }

    private ImageAssetManager v() {
        if (getCallback() == null) {
            return null;
        }
        ImageAssetManager imageAssetManager = this.f9635n;
        if (imageAssetManager != null && !imageAssetManager.b(r())) {
            this.f9635n = null;
        }
        if (this.f9635n == null) {
            this.f9635n = new ImageAssetManager(getCallback(), this.f9636o, this.f9637p, this.f9627f.k());
        }
        return this.f9635n;
    }

    private float y(Canvas canvas, EffectiveAnimationComposition effectiveAnimationComposition) {
        return Math.min(canvas.getWidth() / effectiveAnimationComposition.b().width(), canvas.getHeight() / effectiveAnimationComposition.b().height());
    }

    public PerformanceTracker A() {
        EffectiveAnimationComposition effectiveAnimationComposition = this.f9627f;
        if (effectiveAnimationComposition != null) {
            return effectiveAnimationComposition.o();
        }
        return null;
    }

    public float B() {
        return this.f9628g.h();
    }

    public int C() {
        return this.f9628g.getRepeatCount();
    }

    @SuppressLint({"WrongConstant"})
    public int D() {
        return this.f9628g.getRepeatMode();
    }

    public float E() {
        return this.f9629h;
    }

    public float F() {
        return this.f9628g.n();
    }

    public TextDelegate G() {
        return null;
    }

    public Typeface H(String str, String str2) {
        FontAssetManager s7 = s();
        if (s7 != null) {
            return s7.b(str, str2);
        }
        return null;
    }

    public boolean I() {
        EffectiveValueAnimator effectiveValueAnimator = this.f9628g;
        if (effectiveValueAnimator == null) {
            return false;
        }
        return effectiveValueAnimator.isRunning();
    }

    public boolean J() {
        return this.f9644w;
    }

    public void K() {
        this.f9633l.clear();
        this.f9628g.p();
    }

    public void L() {
        if (this.f9640s == null) {
            this.f9633l.add(new g());
            return;
        }
        if (e() || C() == 0) {
            this.f9628g.q();
        }
        if (e()) {
            return;
        }
        S((int) (F() < 0.0f ? z() : x()));
        this.f9628g.g();
    }

    public void M() {
        this.f9628g.removeAllListeners();
    }

    public List<KeyPath> N(KeyPath keyPath) {
        if (this.f9640s == null) {
            s4.e.c("Cannot resolve KeyPath. Composition is not set yet.");
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        this.f9640s.d(keyPath, 0, arrayList, new KeyPath(new String[0]));
        return arrayList;
    }

    public void O() {
        if (this.f9640s == null) {
            this.f9633l.add(new h());
            return;
        }
        if (e() || C() == 0) {
            this.f9628g.u();
        }
        if (e()) {
            return;
        }
        S((int) (F() < 0.0f ? z() : x()));
        this.f9628g.g();
    }

    public void P(boolean z10) {
        this.f9644w = z10;
    }

    public boolean Q(EffectiveAnimationComposition effectiveAnimationComposition) {
        if (this.f9627f == effectiveAnimationComposition) {
            return false;
        }
        this.f9646y = false;
        j();
        this.f9627f = effectiveAnimationComposition;
        h();
        this.f9628g.w(effectiveAnimationComposition);
        g0(this.f9628g.getAnimatedFraction());
        k0(this.f9629h);
        Iterator it = new ArrayList(this.f9633l).iterator();
        while (it.hasNext()) {
            o oVar = (o) it.next();
            if (oVar != null) {
                oVar.a(effectiveAnimationComposition);
            }
            it.remove();
        }
        this.f9633l.clear();
        effectiveAnimationComposition.w(this.f9642u);
        Drawable.Callback callback = getCallback();
        if (!(callback instanceof ImageView)) {
            return true;
        }
        ImageView imageView = (ImageView) callback;
        imageView.setImageDrawable(null);
        imageView.setImageDrawable(this);
        return true;
    }

    public void R(FontAssetDelegate fontAssetDelegate) {
        FontAssetManager fontAssetManager = this.f9638q;
        if (fontAssetManager != null) {
            fontAssetManager.c(fontAssetDelegate);
        }
    }

    public void S(int i10) {
        if (this.f9627f == null) {
            this.f9633l.add(new c(i10));
        } else {
            this.f9628g.x(i10);
        }
    }

    public void T(boolean z10) {
        this.f9631j = z10;
    }

    public void U(ImageAssetDelegate imageAssetDelegate) {
        this.f9637p = imageAssetDelegate;
        ImageAssetManager imageAssetManager = this.f9635n;
        if (imageAssetManager != null) {
            imageAssetManager.d(imageAssetDelegate);
        }
    }

    public void V(String str) {
        this.f9636o = str;
    }

    public void W(int i10) {
        if (this.f9627f == null) {
            this.f9633l.add(new k(i10));
        } else {
            this.f9628g.y(i10 + 0.99f);
        }
    }

    public void X(String str) {
        EffectiveAnimationComposition effectiveAnimationComposition = this.f9627f;
        if (effectiveAnimationComposition == null) {
            this.f9633l.add(new n(str));
            return;
        }
        Marker m10 = effectiveAnimationComposition.m(str);
        if (m10 != null) {
            W((int) (m10.f14627b + m10.f14628c));
            return;
        }
        throw new IllegalArgumentException("Cannot find marker with name " + str + ".");
    }

    public void Y(float f10) {
        EffectiveAnimationComposition effectiveAnimationComposition = this.f9627f;
        if (effectiveAnimationComposition == null) {
            this.f9633l.add(new l(f10));
        } else {
            W((int) MiscUtils.k(effectiveAnimationComposition.q(), this.f9627f.g(), f10));
        }
    }

    public void Z(int i10, int i11) {
        if (this.f9627f == null) {
            this.f9633l.add(new b(i10, i11));
        } else {
            this.f9628g.z(i10, i11 + 0.99f);
        }
    }

    public void a0(String str) {
        EffectiveAnimationComposition effectiveAnimationComposition = this.f9627f;
        if (effectiveAnimationComposition == null) {
            this.f9633l.add(new a(str));
            return;
        }
        Marker m10 = effectiveAnimationComposition.m(str);
        if (m10 != null) {
            int i10 = (int) m10.f14627b;
            Z(i10, ((int) m10.f14628c) + i10);
        } else {
            throw new IllegalArgumentException("Cannot find marker with name " + str + ".");
        }
    }

    public void b0(int i10) {
        if (this.f9627f == null) {
            this.f9633l.add(new i(i10));
        } else {
            this.f9628g.A(i10);
        }
    }

    public void c(Animator.AnimatorListener animatorListener) {
        this.f9628g.addListener(animatorListener);
    }

    public void c0(String str) {
        EffectiveAnimationComposition effectiveAnimationComposition = this.f9627f;
        if (effectiveAnimationComposition == null) {
            this.f9633l.add(new m(str));
            return;
        }
        Marker m10 = effectiveAnimationComposition.m(str);
        if (m10 != null) {
            b0((int) m10.f14627b);
            return;
        }
        throw new IllegalArgumentException("Cannot find marker with name " + str + ".");
    }

    public <T> void d(KeyPath keyPath, T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        CompositionLayer compositionLayer = this.f9640s;
        if (compositionLayer == null) {
            this.f9633l.add(new e(keyPath, t7, effectiveValueCallback));
            return;
        }
        boolean z10 = true;
        if (keyPath == KeyPath.f14623c) {
            compositionLayer.c(t7, effectiveValueCallback);
        } else if (keyPath.d() != null) {
            keyPath.d().c(t7, effectiveValueCallback);
        } else {
            List<KeyPath> N = N(keyPath);
            for (int i10 = 0; i10 < N.size(); i10++) {
                N.get(i10).d().c(t7, effectiveValueCallback);
                s4.e.a("EffectiveAnimationDrawable::KeyPath = " + N.get(i10));
            }
            z10 = true ^ N.isEmpty();
        }
        if (z10) {
            invalidateSelf();
            if (t7 == EffectiveAnimationProperty.E) {
                g0(B());
            }
        }
    }

    public void d0(float f10) {
        EffectiveAnimationComposition effectiveAnimationComposition = this.f9627f;
        if (effectiveAnimationComposition == null) {
            this.f9633l.add(new j(f10));
        } else {
            b0((int) MiscUtils.k(effectiveAnimationComposition.q(), this.f9627f.g(), f10));
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.f9646y = false;
        L.a("Drawable#draw");
        if (this.f9632k) {
            try {
                k(canvas);
            } catch (Throwable th) {
                s4.e.b("anim crashed in draw!", th);
            }
        } else {
            k(canvas);
        }
        L.b("Drawable#draw");
    }

    public void e0(boolean z10) {
        if (this.f9643v == z10) {
            return;
        }
        this.f9643v = z10;
        CompositionLayer compositionLayer = this.f9640s;
        if (compositionLayer != null) {
            compositionLayer.I(z10);
        }
    }

    public void f0(boolean z10) {
        this.f9642u = z10;
        EffectiveAnimationComposition effectiveAnimationComposition = this.f9627f;
        if (effectiveAnimationComposition != null) {
            effectiveAnimationComposition.w(z10);
        }
    }

    public void g0(float f10) {
        if (this.f9627f == null) {
            this.f9633l.add(new d(f10));
            return;
        }
        L.a("Drawable#setProgress");
        this.f9628g.x(this.f9627f.i(f10));
        L.b("Drawable#setProgress");
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.f9641t;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        if (this.f9627f == null) {
            return -1;
        }
        return (int) (r0.b().height() * E());
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        if (this.f9627f == null) {
            return -1;
        }
        return (int) (r0.b().width() * E());
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public void h0(int i10) {
        this.f9628g.setRepeatCount(i10);
    }

    public void i() {
        this.f9633l.clear();
        this.f9628g.cancel();
    }

    public void i0(int i10) {
        this.f9628g.setRepeatMode(i10);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        Drawable.Callback callback = getCallback();
        if (callback == null) {
            return;
        }
        callback.invalidateDrawable(this);
    }

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        if (this.f9646y) {
            return;
        }
        this.f9646y = true;
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @Override // android.graphics.drawable.Animatable
    public boolean isRunning() {
        return I();
    }

    public void j() {
        if (this.f9628g.isRunning()) {
            this.f9628g.cancel();
        }
        this.f9627f = null;
        this.f9640s = null;
        this.f9635n = null;
        this.f9628g.f();
        invalidateSelf();
    }

    public void j0(boolean z10) {
        this.f9632k = z10;
    }

    public void k0(float f10) {
        this.f9629h = f10;
    }

    public void l0(float f10) {
        this.f9628g.B(f10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m0(Boolean bool) {
        this.f9630i = bool.booleanValue();
    }

    public void n(boolean z10) {
        if (this.f9639r == z10) {
            return;
        }
        this.f9639r = z10;
        if (this.f9627f != null) {
            h();
        }
    }

    public void n0(TextDelegate textDelegate) {
    }

    public boolean o() {
        return this.f9639r;
    }

    public boolean o0() {
        return this.f9627f.c().k() > 0;
    }

    public void p() {
        this.f9633l.clear();
        this.f9628g.g();
    }

    public EffectiveAnimationComposition q() {
        return this.f9627f;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j10) {
        Drawable.Callback callback = getCallback();
        if (callback == null) {
            return;
        }
        callback.scheduleDrawable(this, runnable, j10);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i10) {
        this.f9641t = i10;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        s4.e.c("Use addColorFilter instead.");
    }

    @Override // android.graphics.drawable.Animatable
    public void start() {
        Drawable.Callback callback = getCallback();
        if (!(callback instanceof View) || ((View) callback).isInEditMode()) {
            return;
        }
        L();
    }

    @Override // android.graphics.drawable.Animatable
    public void stop() {
        p();
    }

    public int t() {
        return (int) this.f9628g.i();
    }

    public Bitmap u(String str) {
        ImageAssetManager v7 = v();
        if (v7 != null) {
            return v7.a(str);
        }
        EffectiveAnimationComposition effectiveAnimationComposition = this.f9627f;
        EffectiveImageAsset effectiveImageAsset = effectiveAnimationComposition == null ? null : effectiveAnimationComposition.k().get(str);
        if (effectiveImageAsset != null) {
            return effectiveImageAsset.a();
        }
        return null;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        Drawable.Callback callback = getCallback();
        if (callback == null) {
            return;
        }
        callback.unscheduleDrawable(this, runnable);
    }

    public String w() {
        return this.f9636o;
    }

    public float x() {
        return this.f9628g.k();
    }

    public float z() {
        return this.f9628g.l();
    }
}
