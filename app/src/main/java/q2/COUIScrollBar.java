package q2;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Interpolator;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import com.support.appcompat.R$color;
import com.support.appcompat.R$dimen;
import java.util.Locale;
import w1.COUIDarkModeUtil;

/* compiled from: COUIScrollBar.java */
/* renamed from: q2.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIScrollBar {

    /* renamed from: m, reason: collision with root package name */
    public static long f16798m = 2000;

    /* renamed from: n, reason: collision with root package name */
    private static final int[] f16799n = {R.attr.state_pressed};

    /* renamed from: o, reason: collision with root package name */
    private static final int[] f16800o = new int[0];

    /* renamed from: a, reason: collision with root package name */
    private View f16801a;

    /* renamed from: b, reason: collision with root package name */
    private final float f16802b;

    /* renamed from: c, reason: collision with root package name */
    private float f16803c;

    /* renamed from: d, reason: collision with root package name */
    private final Rect f16804d;

    /* renamed from: e, reason: collision with root package name */
    private Drawable f16805e;

    /* renamed from: f, reason: collision with root package name */
    private final c f16806f;

    /* renamed from: g, reason: collision with root package name */
    private int f16807g;

    /* renamed from: h, reason: collision with root package name */
    private final e f16808h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f16809i;

    /* renamed from: j, reason: collision with root package name */
    private d f16810j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f16811k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f16812l;

    /* compiled from: COUIScrollBar.java */
    /* renamed from: q2.a$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        private final c f16813a;

        /* renamed from: b, reason: collision with root package name */
        private int f16814b;

        /* renamed from: c, reason: collision with root package name */
        private int f16815c;

        /* renamed from: d, reason: collision with root package name */
        private boolean f16816d = true;

        /* renamed from: e, reason: collision with root package name */
        private Drawable f16817e;

        /* renamed from: f, reason: collision with root package name */
        private int f16818f;

        /* renamed from: g, reason: collision with root package name */
        private int f16819g;

        /* renamed from: h, reason: collision with root package name */
        public int f16820h;

        /* renamed from: i, reason: collision with root package name */
        public int f16821i;

        /* renamed from: j, reason: collision with root package name */
        public int f16822j;

        /* renamed from: k, reason: collision with root package name */
        public int f16823k;

        public b(c cVar) {
            this.f16813a = cVar;
            this.f16814b = cVar.getCOUIScrollableView().getContext().getResources().getDimensionPixelSize(R$dimen.coui_scrollbar_wight);
            this.f16815c = cVar.getCOUIScrollableView().getContext().getResources().getDimensionPixelSize(R$dimen.coui_scrollbar_min_height);
            this.f16820h = cVar.getCOUIScrollableView().getContext().getResources().getDimensionPixelSize(R$dimen.coui_scrollbar_margin_top);
            this.f16821i = cVar.getCOUIScrollableView().getContext().getResources().getDimensionPixelSize(R$dimen.coui_scrollbar_margin_bottom);
            this.f16822j = cVar.getCOUIScrollableView().getContext().getResources().getDimensionPixelSize(R$dimen.coui_scrollbar_drawable_default_inset);
            this.f16823k = cVar.getCOUIScrollableView().getContext().getResources().getDimensionPixelSize(R$dimen.coui_scrollbar_drawable_pressed_inset);
            Context context = cVar.getCOUIScrollableView().getContext();
            int i10 = R$color.coui_scrollbar_color;
            this.f16818f = ContextCompat.c(context, i10);
            this.f16819g = ContextCompat.c(cVar.getCOUIScrollableView().getContext(), i10);
        }

        private Drawable b() {
            StateListDrawable stateListDrawable = new StateListDrawable();
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(this.f16819g);
            float f10 = this.f16814b / 2.0f;
            gradientDrawable.setCornerRadius(f10);
            int[] iArr = COUIScrollBar.f16799n;
            int i10 = this.f16823k;
            stateListDrawable.addState(iArr, new InsetDrawable((Drawable) gradientDrawable, i10, this.f16820h, i10, this.f16821i));
            GradientDrawable gradientDrawable2 = new GradientDrawable();
            gradientDrawable2.setColor(this.f16818f);
            gradientDrawable2.setCornerRadius(f10);
            int[] iArr2 = COUIScrollBar.f16800o;
            int i11 = this.f16822j;
            stateListDrawable.addState(iArr2, new InsetDrawable((Drawable) gradientDrawable2, i11, this.f16820h, i11, this.f16821i));
            return stateListDrawable;
        }

        public COUIScrollBar a() {
            if (this.f16817e == null) {
                this.f16817e = b();
            }
            return new COUIScrollBar(this.f16813a, this.f16814b, this.f16815c, this.f16817e, this.f16816d);
        }
    }

    /* compiled from: COUIScrollBar.java */
    /* renamed from: q2.a$c */
    /* loaded from: classes.dex */
    public interface c {
        View getCOUIScrollableView();

        int superComputeVerticalScrollExtent();

        int superComputeVerticalScrollOffset();

        int superComputeVerticalScrollRange();

        void superOnTouchEvent(MotionEvent motionEvent);
    }

    /* compiled from: COUIScrollBar.java */
    /* renamed from: q2.a$d */
    /* loaded from: classes.dex */
    public interface d {
        void a(View view, COUIScrollBar cOUIScrollBar);

        void b(View view, COUIScrollBar cOUIScrollBar);

        void c(View view, COUIScrollBar cOUIScrollBar, int i10, int i11, float f10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: COUIScrollBar.java */
    /* renamed from: q2.a$e */
    /* loaded from: classes.dex */
    public static class e implements Runnable {

        /* renamed from: l, reason: collision with root package name */
        private static final float[] f16824l = {255.0f};

        /* renamed from: m, reason: collision with root package name */
        private static final float[] f16825m = {0.0f};

        /* renamed from: g, reason: collision with root package name */
        public float[] f16828g;

        /* renamed from: h, reason: collision with root package name */
        public View f16829h;

        /* renamed from: j, reason: collision with root package name */
        public long f16831j;

        /* renamed from: i, reason: collision with root package name */
        public final Interpolator f16830i = new Interpolator(1, 2);

        /* renamed from: k, reason: collision with root package name */
        public int f16832k = 0;

        /* renamed from: e, reason: collision with root package name */
        public final int f16826e = ViewConfiguration.getScrollDefaultDelay();

        /* renamed from: f, reason: collision with root package name */
        public final int f16827f = ViewConfiguration.getScrollBarFadeDuration();

        public e(ViewConfiguration viewConfiguration, View view) {
            this.f16829h = view;
        }

        @Override // java.lang.Runnable
        public void run() {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            if (currentAnimationTimeMillis >= this.f16831j) {
                int i10 = (int) currentAnimationTimeMillis;
                Interpolator interpolator = this.f16830i;
                interpolator.setKeyFrame(0, i10, f16824l);
                interpolator.setKeyFrame(1, i10 + this.f16827f, f16825m);
                this.f16832k = 2;
                this.f16829h.invalidate();
            }
        }
    }

    private void f(StateListDrawable stateListDrawable, int i10, int i11) {
        Drawable stateDrawable = stateListDrawable.getStateDrawable(i10);
        if (stateDrawable instanceof InsetDrawable) {
            Drawable drawable = ((InsetDrawable) stateDrawable).getDrawable();
            if (drawable instanceof GradientDrawable) {
                ((GradientDrawable) drawable).setColor(i11);
            }
        }
    }

    private boolean g() {
        return d(this.f16808h.f16826e * 4);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x006d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void i(Canvas canvas) {
        boolean z10 = true;
        if (this.f16811k) {
            this.f16805e.setAlpha(255);
        } else {
            e eVar = this.f16808h;
            int i10 = eVar.f16832k;
            if (i10 == 0) {
                return;
            }
            if (i10 == 2) {
                if (eVar.f16828g == null) {
                    eVar.f16828g = new float[1];
                }
                float[] fArr = eVar.f16828g;
                if (eVar.f16830i.timeToValues(fArr) == Interpolator.Result.FREEZE_END) {
                    eVar.f16832k = 0;
                } else {
                    this.f16805e.setAlpha(Math.round(fArr[0]));
                }
                if (u(0)) {
                    int scrollY = this.f16801a.getScrollY();
                    int scrollX = this.f16801a.getScrollX();
                    Drawable drawable = this.f16805e;
                    Rect rect = this.f16804d;
                    drawable.setBounds(rect.left + scrollX, rect.top + scrollY, rect.right + scrollX, rect.bottom + scrollY);
                    this.f16805e.draw(canvas);
                }
                if (z10) {
                    return;
                }
                this.f16801a.invalidate();
                return;
            }
            this.f16805e.setAlpha(255);
        }
        z10 = false;
        if (u(0)) {
        }
        if (z10) {
        }
    }

    private boolean k(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            return m(motionEvent);
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0012, code lost:
    
        if (r0 != 3) goto L32;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean m(MotionEvent motionEvent) {
        int round;
        int actionMasked = motionEvent.getActionMasked();
        float y4 = motionEvent.getY();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    if (this.f16811k && (round = Math.round(y4 - this.f16803c)) != 0) {
                        u(round);
                        this.f16803c = y4;
                    }
                }
            }
            if (this.f16811k) {
                r(false);
                this.f16811k = false;
                c();
            }
        } else {
            if (this.f16808h.f16832k == 0) {
                this.f16811k = false;
                return false;
            }
            if (!this.f16811k) {
                u(0);
                float x10 = motionEvent.getX();
                Rect rect = this.f16804d;
                if (y4 >= rect.top && y4 <= rect.bottom && x10 >= rect.left && x10 <= rect.right) {
                    this.f16811k = true;
                    this.f16803c = y4;
                    this.f16806f.superOnTouchEvent(motionEvent);
                    MotionEvent obtain = MotionEvent.obtain(motionEvent);
                    obtain.setAction(3);
                    this.f16806f.superOnTouchEvent(obtain);
                    obtain.recycle();
                    r(true);
                    v(0, true);
                    this.f16801a.removeCallbacks(this.f16808h);
                }
            }
        }
        if (!this.f16811k) {
            return false;
        }
        this.f16801a.invalidate();
        this.f16801a.getParent().requestDisallowInterceptTouchEvent(true);
        return true;
    }

    private void r(boolean z10) {
        this.f16805e.setState(z10 ? f16799n : f16800o);
        this.f16801a.invalidate();
        d dVar = this.f16810j;
        if (dVar != null) {
            if (z10) {
                dVar.b(this.f16801a, this);
            } else {
                dVar.a(this.f16801a, this);
            }
        }
    }

    private boolean u(int i10) {
        return v(i10, false);
    }

    private boolean v(int i10, boolean z10) {
        d dVar;
        int width = this.f16804d.width();
        this.f16804d.right = this.f16812l ? width : this.f16801a.getWidth();
        Rect rect = this.f16804d;
        rect.left = this.f16812l ? 0 : rect.right - width;
        int superComputeVerticalScrollRange = this.f16806f.superComputeVerticalScrollRange();
        if (superComputeVerticalScrollRange <= 0) {
            return false;
        }
        int superComputeVerticalScrollOffset = this.f16806f.superComputeVerticalScrollOffset();
        int superComputeVerticalScrollExtent = this.f16806f.superComputeVerticalScrollExtent();
        int i11 = superComputeVerticalScrollRange - superComputeVerticalScrollExtent;
        if (i11 <= 0) {
            return false;
        }
        float f10 = i11;
        float f11 = (superComputeVerticalScrollOffset * 1.0f) / f10;
        float f12 = (superComputeVerticalScrollExtent * 1.0f) / superComputeVerticalScrollRange;
        int height = this.f16801a.getHeight();
        int max = this.f16809i ? Math.max(this.f16807g, Math.round(f12 * height)) : this.f16807g;
        Rect rect2 = this.f16804d;
        rect2.bottom = rect2.top + max;
        int i12 = height - max;
        float f13 = i12;
        int round = Math.round(f13 * f11);
        Rect rect3 = this.f16804d;
        rect3.offsetTo(rect3.left, round);
        if (i10 == 0) {
            if (!z10 || (dVar = this.f16810j) == null) {
                return true;
            }
            dVar.c(this.f16801a, this, 0, 0, f11);
            return true;
        }
        int i13 = round + i10;
        if (i13 <= i12) {
            i12 = i13 < 0 ? 0 : i13;
        }
        float f14 = (i12 * 1.0f) / f13;
        int round2 = Math.round(f10 * f14) - superComputeVerticalScrollOffset;
        View view = this.f16801a;
        if (view instanceof AbsListView) {
            ((AbsListView) view).smoothScrollBy(round2, 0);
        } else {
            view.scrollBy(0, round2);
        }
        d dVar2 = this.f16810j;
        if (dVar2 == null) {
            return true;
        }
        dVar2.c(this.f16801a, this, i10, round2, f14);
        return true;
    }

    public boolean c() {
        return d(f16798m);
    }

    public boolean d(long j10) {
        ViewCompat.b0(this.f16801a);
        if (this.f16811k) {
            return false;
        }
        if (this.f16808h.f16832k == 0) {
            j10 = Math.max(750L, j10);
        }
        long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis() + j10;
        e eVar = this.f16808h;
        eVar.f16831j = currentAnimationTimeMillis;
        eVar.f16832k = 1;
        this.f16801a.removeCallbacks(eVar);
        this.f16801a.postDelayed(this.f16808h, currentAnimationTimeMillis - AnimationUtils.currentAnimationTimeMillis());
        return false;
    }

    public void e(Canvas canvas) {
        i(canvas);
    }

    public void h() {
        g();
    }

    public boolean j(MotionEvent motionEvent) {
        return k(motionEvent);
    }

    public boolean l(MotionEvent motionEvent) {
        return m(motionEvent);
    }

    public void n(View view, int i10) {
        if (i10 == 0 && ViewCompat.P(this.f16801a)) {
            g();
        }
    }

    public void o(int i10) {
        if (i10 == 0) {
            g();
        }
    }

    public void p() {
        Drawable drawable = this.f16805e;
        if (drawable instanceof StateListDrawable) {
            StateListDrawable stateListDrawable = (StateListDrawable) drawable;
            if (stateListDrawable.getStateCount() < 1) {
                return;
            }
            Context context = this.f16801a.getContext();
            int i10 = R$color.coui_scrollbar_color;
            f(stateListDrawable, 0, ContextCompat.c(context, i10));
            f(stateListDrawable, 1, ContextCompat.c(this.f16801a.getContext(), i10));
        }
    }

    public void q() {
        this.f16801a = null;
    }

    public void s(Drawable drawable) {
        if (drawable != null) {
            this.f16805e = drawable;
            u(0);
            return;
        }
        throw new IllegalArgumentException("setThumbDrawable must NOT be NULL");
    }

    public void t(int i10) {
        Rect rect = this.f16804d;
        rect.left = rect.right - i10;
        u(0);
    }

    private COUIScrollBar(c cVar, int i10, int i11, Drawable drawable, boolean z10) {
        this.f16811k = false;
        this.f16812l = false;
        View cOUIScrollableView = cVar.getCOUIScrollableView();
        this.f16801a = cOUIScrollableView;
        cOUIScrollableView.setVerticalScrollBarEnabled(false);
        COUIDarkModeUtil.b(this.f16801a, false);
        Context context = this.f16801a.getContext();
        this.f16812l = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
        this.f16802b = context.getResources().getDisplayMetrics().density;
        this.f16807g = this.f16801a.getContext().getResources().getDimensionPixelSize(R$dimen.coui_scrollbar_min_height);
        this.f16804d = new Rect(0, 0, i10, i11);
        this.f16805e = drawable;
        this.f16806f = cVar;
        this.f16808h = new e(ViewConfiguration.get(context), this.f16801a);
        this.f16809i = z10;
    }
}
