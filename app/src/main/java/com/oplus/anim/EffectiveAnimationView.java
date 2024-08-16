package com.oplus.anim;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;
import c.AppCompatResources;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import l4.KeyPath;
import t4.EffectiveValueCallback;

/* loaded from: classes.dex */
public class EffectiveAnimationView extends AppCompatImageView {
    private static final String B = EffectiveAnimationView.class.getSimpleName();
    private EffectiveAnimationComposition A;

    /* renamed from: h, reason: collision with root package name */
    private final EffectiveAnimationListener<Throwable> f9576h;

    /* renamed from: i, reason: collision with root package name */
    private final EffectiveAnimationListener<EffectiveAnimationComposition> f9577i;

    /* renamed from: j, reason: collision with root package name */
    private final EffectiveAnimationListener<Throwable> f9578j;

    /* renamed from: k, reason: collision with root package name */
    private EffectiveAnimationListener<Throwable> f9579k;

    /* renamed from: l, reason: collision with root package name */
    private int f9580l;

    /* renamed from: m, reason: collision with root package name */
    private final EffectiveAnimationDrawable f9581m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f9582n;

    /* renamed from: o, reason: collision with root package name */
    private String f9583o;

    /* renamed from: p, reason: collision with root package name */
    private int f9584p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f9585q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f9586r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f9587s;

    /* renamed from: t, reason: collision with root package name */
    private boolean f9588t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f9589u;

    /* renamed from: v, reason: collision with root package name */
    private boolean f9590v;

    /* renamed from: w, reason: collision with root package name */
    private RenderMode f9591w;

    /* renamed from: x, reason: collision with root package name */
    private final Set<EffectiveOnCompositionLoadedListener> f9592x;

    /* renamed from: y, reason: collision with root package name */
    private int f9593y;

    /* renamed from: z, reason: collision with root package name */
    private EffectiveAnimationTask<EffectiveAnimationComposition> f9594z;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        String f9595e;

        /* renamed from: f, reason: collision with root package name */
        int f9596f;

        /* renamed from: g, reason: collision with root package name */
        float f9597g;

        /* renamed from: h, reason: collision with root package name */
        boolean f9598h;

        /* renamed from: i, reason: collision with root package name */
        String f9599i;

        /* renamed from: j, reason: collision with root package name */
        int f9600j;

        /* renamed from: k, reason: collision with root package name */
        int f9601k;

        /* loaded from: classes.dex */
        static class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        }

        /* synthetic */ SavedState(Parcel parcel, a aVar) {
            this(parcel);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeString(this.f9595e);
            parcel.writeFloat(this.f9597g);
            parcel.writeInt(this.f9598h ? 1 : 0);
            parcel.writeString(this.f9599i);
            parcel.writeInt(this.f9600j);
            parcel.writeInt(this.f9601k);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.f9595e = parcel.readString();
            this.f9597g = parcel.readFloat();
            this.f9598h = parcel.readInt() == 1;
            this.f9599i = parcel.readString();
            this.f9600j = parcel.readInt();
            this.f9601k = parcel.readInt();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements EffectiveAnimationListener<Throwable> {
        a() {
        }

        @Override // com.oplus.anim.EffectiveAnimationListener
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(Throwable th) {
            EffectiveAnimationView.this.k();
            if (s4.h.l(th)) {
                s4.e.d("Unable to load composition.", th);
                return;
            }
            throw new IllegalStateException("Unable to parse composition", th);
        }
    }

    /* loaded from: classes.dex */
    class b implements EffectiveAnimationListener<EffectiveAnimationComposition> {
        b() {
        }

        @Override // com.oplus.anim.EffectiveAnimationListener
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(EffectiveAnimationComposition effectiveAnimationComposition) {
            EffectiveAnimationView.this.k();
            EffectiveAnimationView.this.setComposition(effectiveAnimationComposition);
        }
    }

    /* loaded from: classes.dex */
    class c implements EffectiveAnimationListener<Throwable> {
        c() {
        }

        @Override // com.oplus.anim.EffectiveAnimationListener
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(Throwable th) {
            if (EffectiveAnimationView.this.f9580l != 0) {
                EffectiveAnimationView effectiveAnimationView = EffectiveAnimationView.this;
                effectiveAnimationView.setImageResource(effectiveAnimationView.f9580l);
            }
            EffectiveAnimationListener effectiveAnimationListener = EffectiveAnimationView.this.f9579k;
            EffectiveAnimationView effectiveAnimationView2 = EffectiveAnimationView.this;
            (effectiveAnimationListener == null ? effectiveAnimationView2.f9576h : effectiveAnimationView2.f9579k).a(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements Callable<EffectiveAnimationResult<EffectiveAnimationComposition>> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f9605a;

        d(int i10) {
            this.f9605a = i10;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public EffectiveAnimationResult<EffectiveAnimationComposition> call() {
            return EffectiveAnimationView.this.f9590v ? EffectiveCompositionFactory.o(EffectiveAnimationView.this.getContext(), this.f9605a) : EffectiveCompositionFactory.p(EffectiveAnimationView.this.getContext(), this.f9605a, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements Callable<EffectiveAnimationResult<EffectiveAnimationComposition>> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f9607a;

        e(String str) {
            this.f9607a = str;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public EffectiveAnimationResult<EffectiveAnimationComposition> call() {
            return EffectiveAnimationView.this.f9590v ? EffectiveCompositionFactory.f(EffectiveAnimationView.this.getContext(), this.f9607a) : EffectiveCompositionFactory.g(EffectiveAnimationView.this.getContext(), this.f9607a, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static /* synthetic */ class f {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f9609a;

        static {
            int[] iArr = new int[RenderMode.values().length];
            f9609a = iArr;
            try {
                iArr[RenderMode.HARDWARE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f9609a[RenderMode.SOFTWARE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f9609a[RenderMode.NONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f9609a[RenderMode.AUTOMATIC.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public EffectiveAnimationView(Context context) {
        super(context);
        this.f9576h = new a();
        this.f9577i = new b();
        this.f9578j = new c();
        this.f9580l = 0;
        this.f9581m = new EffectiveAnimationDrawable();
        this.f9585q = false;
        this.f9586r = false;
        this.f9587s = false;
        this.f9588t = false;
        this.f9589u = false;
        this.f9590v = true;
        this.f9591w = RenderMode.AUTOMATIC;
        this.f9592x = new HashSet();
        this.f9593y = 0;
        q(null, R$attr.effectiveAnimationViewStyle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() {
        EffectiveAnimationTask<EffectiveAnimationComposition> effectiveAnimationTask = this.f9594z;
        if (effectiveAnimationTask != null) {
            effectiveAnimationTask.k(this.f9577i);
            this.f9594z.j(this.f9578j);
        }
    }

    private void l() {
        this.A = null;
        this.f9581m.j();
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0047, code lost:
    
        if (((r0 == null || r0.n() <= 4) ? 1 : 0) != 0) goto L20;
     */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void n() {
        s4.e.a("Render mode : " + this.f9591w.name());
        int i10 = f.f9609a[this.f9591w.ordinal()];
        if (i10 != 1) {
            if (i10 != 2) {
                if (i10 != 3) {
                    if (i10 == 4) {
                        EffectiveAnimationComposition effectiveAnimationComposition = this.A;
                        if (effectiveAnimationComposition != null) {
                            effectiveAnimationComposition.r();
                        }
                        EffectiveAnimationComposition effectiveAnimationComposition2 = this.A;
                    }
                }
                if (r1 != getLayerType()) {
                    setLayerType(r1, null);
                    return;
                }
                return;
            }
            r1 = 1;
            if (r1 != getLayerType()) {
            }
        }
        r1 = 2;
        if (r1 != getLayerType()) {
        }
    }

    private EffectiveAnimationTask<EffectiveAnimationComposition> o(String str) {
        if (isInEditMode()) {
            return new EffectiveAnimationTask<>(new e(str), true);
        }
        return this.f9590v ? EffectiveCompositionFactory.d(getContext(), str) : EffectiveCompositionFactory.e(getContext(), str, null);
    }

    private EffectiveAnimationTask<EffectiveAnimationComposition> p(int i10) {
        if (isInEditMode()) {
            return new EffectiveAnimationTask<>(new d(i10), true);
        }
        return this.f9590v ? EffectiveCompositionFactory.m(getContext(), i10) : EffectiveCompositionFactory.n(getContext(), i10, null);
    }

    private void q(AttributeSet attributeSet, int i10) {
        String string;
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.EffectiveAnimationView, i10, 0);
        this.f9590v = obtainStyledAttributes.getBoolean(R$styleable.EffectiveAnimationView_anim_cacheComposition, true);
        int i11 = R$styleable.EffectiveAnimationView_anim_rawRes;
        boolean hasValue = obtainStyledAttributes.hasValue(i11);
        int i12 = R$styleable.EffectiveAnimationView_anim_fileName;
        boolean hasValue2 = obtainStyledAttributes.hasValue(i12);
        int i13 = R$styleable.EffectiveAnimationView_anim_url;
        boolean hasValue3 = obtainStyledAttributes.hasValue(i13);
        if (hasValue && hasValue2) {
            throw new IllegalArgumentException("anim_rawRes and anim_fileName cannot be used at the same time. Please use only one at once.");
        }
        if (hasValue) {
            int resourceId = obtainStyledAttributes.getResourceId(i11, 0);
            if (resourceId != 0) {
                setAnimation(resourceId);
            }
        } else if (hasValue2) {
            String string2 = obtainStyledAttributes.getString(i12);
            if (string2 != null) {
                setAnimation(string2);
            }
        } else if (hasValue3 && (string = obtainStyledAttributes.getString(i13)) != null) {
            setAnimationFromUrl(string);
        }
        setFallbackResource(obtainStyledAttributes.getResourceId(R$styleable.EffectiveAnimationView_anim_fallbackRes, 0));
        if (obtainStyledAttributes.getBoolean(R$styleable.EffectiveAnimationView_anim_autoPlay, false)) {
            this.f9587s = true;
            this.f9589u = true;
        }
        if (obtainStyledAttributes.getBoolean(R$styleable.EffectiveAnimationView_anim_loop, false)) {
            this.f9581m.h0(-1);
        }
        int i14 = R$styleable.EffectiveAnimationView_anim_repeatMode;
        if (obtainStyledAttributes.hasValue(i14)) {
            setRepeatMode(obtainStyledAttributes.getInt(i14, 1));
        }
        int i15 = R$styleable.EffectiveAnimationView_anim_repeatCount;
        if (obtainStyledAttributes.hasValue(i15)) {
            setRepeatCount(obtainStyledAttributes.getInt(i15, -1));
        }
        int i16 = R$styleable.EffectiveAnimationView_anim_speed;
        if (obtainStyledAttributes.hasValue(i16)) {
            setSpeed(obtainStyledAttributes.getFloat(i16, 1.0f));
        }
        setImageAssetsFolder(obtainStyledAttributes.getString(R$styleable.EffectiveAnimationView_anim_imageAssetsFolder));
        setProgress(obtainStyledAttributes.getFloat(R$styleable.EffectiveAnimationView_anim_progress, 0.0f));
        m(obtainStyledAttributes.getBoolean(R$styleable.EffectiveAnimationView_anim_enableMergePathsForKitKatAndAbove, false));
        int i17 = R$styleable.EffectiveAnimationView_anim_colorFilter;
        if (obtainStyledAttributes.hasValue(i17)) {
            i(new KeyPath("**"), EffectiveAnimationProperty.K, new EffectiveValueCallback(new SimpleColorFilter(AppCompatResources.a(getContext(), obtainStyledAttributes.getResourceId(i17, -1)).getDefaultColor())));
        }
        int i18 = R$styleable.EffectiveAnimationView_anim_scale;
        if (obtainStyledAttributes.hasValue(i18)) {
            this.f9581m.k0(obtainStyledAttributes.getFloat(i18, 1.0f));
        }
        int i19 = R$styleable.EffectiveAnimationView_anim_renderMode;
        if (obtainStyledAttributes.hasValue(i19)) {
            RenderMode renderMode = RenderMode.AUTOMATIC;
            int i20 = obtainStyledAttributes.getInt(i19, renderMode.ordinal());
            if (i20 >= RenderMode.values().length) {
                i20 = renderMode.ordinal();
            }
            setRenderMode(RenderMode.values()[i20]);
        }
        setIgnoreDisabledSystemAnimations(obtainStyledAttributes.getBoolean(R$styleable.EffectiveAnimationView_anim_ignoreDisabledSystemAnimations, false));
        obtainStyledAttributes.recycle();
        this.f9581m.m0(Boolean.valueOf(s4.h.g(getContext()) != 0.0f));
        n();
        this.f9582n = true;
    }

    private void setCompositionTask(EffectiveAnimationTask<EffectiveAnimationComposition> effectiveAnimationTask) {
        l();
        k();
        this.f9594z = effectiveAnimationTask.f(this.f9577i).e(this.f9578j);
    }

    private void z() {
        boolean r10 = r();
        setImageDrawable(null);
        setImageDrawable(this.f9581m);
        if (r10) {
            this.f9581m.O();
        }
    }

    @Override // android.view.View
    public void buildDrawingCache(boolean z10) {
        L.a("buildDrawingCache");
        this.f9593y++;
        super.buildDrawingCache(z10);
        if (this.f9593y == 1 && getWidth() > 0 && getHeight() > 0 && getLayerType() == 1 && getDrawingCache(z10) == null) {
            setRenderMode(RenderMode.HARDWARE);
        }
        this.f9593y--;
        L.b("buildDrawingCache");
    }

    public EffectiveAnimationComposition getComposition() {
        return this.A;
    }

    public long getDuration() {
        if (this.A != null) {
            return r2.e();
        }
        return 0L;
    }

    public int getFrame() {
        return this.f9581m.t();
    }

    public String getImageAssetsFolder() {
        return this.f9581m.w();
    }

    public float getMaxFrame() {
        return this.f9581m.x();
    }

    public float getMinFrame() {
        return this.f9581m.z();
    }

    public PerformanceTracker getPerformanceTracker() {
        return this.f9581m.A();
    }

    public float getProgress() {
        return this.f9581m.B();
    }

    public int getRepeatCount() {
        return this.f9581m.C();
    }

    public int getRepeatMode() {
        return this.f9581m.D();
    }

    public float getScale() {
        return this.f9581m.E();
    }

    public float getSpeed() {
        return this.f9581m.F();
    }

    public void h(Animator.AnimatorListener animatorListener) {
        this.f9581m.c(animatorListener);
    }

    public <T> void i(KeyPath keyPath, T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        this.f9581m.d(keyPath, t7, effectiveValueCallback);
    }

    @Override // android.widget.ImageView, android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        Drawable drawable2 = getDrawable();
        EffectiveAnimationDrawable effectiveAnimationDrawable = this.f9581m;
        if (drawable2 == effectiveAnimationDrawable) {
            super.invalidateDrawable(effectiveAnimationDrawable);
        } else {
            super.invalidateDrawable(drawable);
        }
    }

    public void j() {
        this.f9587s = false;
        this.f9586r = false;
        this.f9585q = false;
        this.f9581m.i();
        n();
    }

    public void m(boolean z10) {
        this.f9581m.n(z10);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.ImageView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) {
            return;
        }
        if (this.f9589u || this.f9587s) {
            u();
            this.f9589u = false;
            this.f9587s = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.ImageView, android.view.View
    public void onDetachedFromWindow() {
        if (r()) {
            j();
            this.f9587s = true;
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        String str = savedState.f9595e;
        this.f9583o = str;
        if (!TextUtils.isEmpty(str)) {
            setAnimation(this.f9583o);
        }
        int i10 = savedState.f9596f;
        this.f9584p = i10;
        if (i10 != 0) {
            setAnimation(i10);
        }
        setProgress(savedState.f9597g);
        if (savedState.f9598h) {
            u();
        }
        this.f9581m.V(savedState.f9599i);
        setRepeatMode(savedState.f9600j);
        setRepeatCount(savedState.f9601k);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f9595e = this.f9583o;
        savedState.f9596f = this.f9584p;
        savedState.f9597g = this.f9581m.B();
        savedState.f9598h = this.f9581m.I() || (!ViewCompat.P(this) && this.f9587s);
        savedState.f9599i = this.f9581m.w();
        savedState.f9600j = this.f9581m.D();
        savedState.f9601k = this.f9581m.C();
        return savedState;
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i10) {
        if (this.f9582n) {
            if (isShown()) {
                if (this.f9586r) {
                    w();
                } else if (this.f9585q) {
                    u();
                }
                this.f9586r = false;
                this.f9585q = false;
                return;
            }
            if (r()) {
                t();
                this.f9586r = true;
            }
        }
    }

    public boolean r() {
        return this.f9581m.I();
    }

    @Deprecated
    public void s(boolean z10) {
        this.f9581m.h0(z10 ? -1 : 0);
    }

    public void setAnimation(int i10) {
        this.f9584p = i10;
        this.f9583o = null;
        setCompositionTask(p(i10));
    }

    @Deprecated
    public void setAnimationFromJson(String str) {
        y(str, null);
    }

    public void setAnimationFromUrl(String str) {
        setCompositionTask(this.f9590v ? EffectiveCompositionFactory.q(getContext(), str) : EffectiveCompositionFactory.r(getContext(), str, null));
    }

    public void setApplyingOpacityToLayersEnabled(boolean z10) {
        this.f9581m.P(z10);
    }

    public void setCacheComposition(boolean z10) {
        this.f9590v = z10;
    }

    public void setComposition(EffectiveAnimationComposition effectiveAnimationComposition) {
        if (L.f9733a) {
            Log.v(B, "Set Composition \n" + effectiveAnimationComposition);
        }
        this.f9581m.setCallback(this);
        this.A = effectiveAnimationComposition;
        this.f9588t = true;
        boolean Q = this.f9581m.Q(effectiveAnimationComposition);
        this.f9588t = false;
        n();
        if (getDrawable() != this.f9581m || Q) {
            if (!Q) {
                z();
            }
            onVisibilityChanged(this, getVisibility());
            requestLayout();
            Iterator<EffectiveOnCompositionLoadedListener> it = this.f9592x.iterator();
            while (it.hasNext()) {
                it.next().a(effectiveAnimationComposition);
            }
        }
    }

    public void setFailureListener(EffectiveAnimationListener<Throwable> effectiveAnimationListener) {
        this.f9579k = effectiveAnimationListener;
    }

    public void setFallbackResource(int i10) {
        this.f9580l = i10;
    }

    public void setFontAssetDelegate(FontAssetDelegate fontAssetDelegate) {
        this.f9581m.R(fontAssetDelegate);
    }

    public void setFrame(int i10) {
        this.f9581m.S(i10);
    }

    public void setIgnoreDisabledSystemAnimations(boolean z10) {
        this.f9581m.T(z10);
    }

    public void setImageAssetDelegate(ImageAssetDelegate imageAssetDelegate) {
        this.f9581m.U(imageAssetDelegate);
    }

    public void setImageAssetsFolder(String str) {
        this.f9581m.V(str);
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageBitmap(Bitmap bitmap) {
        k();
        super.setImageBitmap(bitmap);
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        k();
        super.setImageDrawable(drawable);
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageResource(int i10) {
        k();
        super.setImageResource(i10);
    }

    public void setMaxFrame(int i10) {
        this.f9581m.W(i10);
    }

    public void setMaxProgress(float f10) {
        this.f9581m.Y(f10);
    }

    public void setMinAndMaxFrame(String str) {
        this.f9581m.a0(str);
    }

    public void setMinFrame(int i10) {
        this.f9581m.b0(i10);
    }

    public void setMinProgress(float f10) {
        this.f9581m.d0(f10);
    }

    public void setOutlineMasksAndMattes(boolean z10) {
        this.f9581m.e0(z10);
    }

    public void setPerformanceTrackingEnabled(boolean z10) {
        this.f9581m.f0(z10);
    }

    public void setProgress(float f10) {
        this.f9581m.g0(f10);
    }

    public void setRenderMode(RenderMode renderMode) {
        this.f9591w = renderMode;
        n();
    }

    public void setRepeatCount(int i10) {
        this.f9581m.h0(i10);
    }

    public void setRepeatMode(int i10) {
        this.f9581m.i0(i10);
    }

    public void setSafeMode(boolean z10) {
        this.f9581m.j0(z10);
    }

    public void setScale(float f10) {
        this.f9581m.k0(f10);
        if (getDrawable() == this.f9581m) {
            z();
        }
    }

    public void setSpeed(float f10) {
        this.f9581m.l0(f10);
    }

    public void setTextDelegate(TextDelegate textDelegate) {
        this.f9581m.n0(textDelegate);
    }

    public void t() {
        this.f9589u = false;
        this.f9587s = false;
        this.f9586r = false;
        this.f9585q = false;
        this.f9581m.K();
        n();
    }

    public void u() {
        if (isShown()) {
            this.f9581m.L();
            n();
        } else {
            this.f9585q = true;
        }
    }

    @Override // android.view.View
    public void unscheduleDrawable(Drawable drawable) {
        EffectiveAnimationDrawable effectiveAnimationDrawable;
        if (!this.f9588t && drawable == (effectiveAnimationDrawable = this.f9581m) && effectiveAnimationDrawable.I()) {
            t();
        } else if (!this.f9588t && (drawable instanceof EffectiveAnimationDrawable)) {
            EffectiveAnimationDrawable effectiveAnimationDrawable2 = (EffectiveAnimationDrawable) drawable;
            if (effectiveAnimationDrawable2.I()) {
                effectiveAnimationDrawable2.K();
            }
        }
        super.unscheduleDrawable(drawable);
    }

    public void v() {
        this.f9581m.M();
    }

    public void w() {
        if (isShown()) {
            this.f9581m.O();
            n();
        } else {
            this.f9585q = false;
            this.f9586r = true;
        }
    }

    public void x(InputStream inputStream, String str) {
        setCompositionTask(EffectiveCompositionFactory.h(inputStream, str));
    }

    public void y(String str, String str2) {
        x(new ByteArrayInputStream(str.getBytes()), str2);
    }

    public void setMaxFrame(String str) {
        this.f9581m.X(str);
    }

    public void setMinFrame(String str) {
        this.f9581m.c0(str);
    }

    public void setAnimation(String str) {
        this.f9583o = str;
        this.f9584p = 0;
        setCompositionTask(o(str));
    }

    public EffectiveAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f9576h = new a();
        this.f9577i = new b();
        this.f9578j = new c();
        this.f9580l = 0;
        this.f9581m = new EffectiveAnimationDrawable();
        this.f9585q = false;
        this.f9586r = false;
        this.f9587s = false;
        this.f9588t = false;
        this.f9589u = false;
        this.f9590v = true;
        this.f9591w = RenderMode.AUTOMATIC;
        this.f9592x = new HashSet();
        this.f9593y = 0;
        q(attributeSet, R$attr.effectiveAnimationViewStyle);
    }

    public EffectiveAnimationView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f9576h = new a();
        this.f9577i = new b();
        this.f9578j = new c();
        this.f9580l = 0;
        this.f9581m = new EffectiveAnimationDrawable();
        this.f9585q = false;
        this.f9586r = false;
        this.f9587s = false;
        this.f9588t = false;
        this.f9589u = false;
        this.f9590v = true;
        this.f9591w = RenderMode.AUTOMATIC;
        this.f9592x = new HashSet();
        this.f9593y = 0;
        q(attributeSet, i10);
    }
}
