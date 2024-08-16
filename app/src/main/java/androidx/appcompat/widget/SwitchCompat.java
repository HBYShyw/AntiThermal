package androidx.appcompat.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Property;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$string;
import androidx.appcompat.R$styleable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.emoji2.text.EmojiCompat;
import c.AppCompatResources;
import f.AllCapsTransformationMethod;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class SwitchCompat extends CompoundButton {
    private static final Property<SwitchCompat, Float> W = new a(Float.class, "thumbPos");

    /* renamed from: a0, reason: collision with root package name */
    private static final int[] f1104a0 = {16842912};
    private float A;
    private VelocityTracker B;
    private int C;
    float D;
    private int E;
    private int F;
    private int G;
    private int H;
    private int I;
    private int J;
    private int K;
    private boolean L;
    private final TextPaint M;
    private ColorStateList N;
    private Layout O;
    private Layout P;
    private TransformationMethod Q;
    ObjectAnimator R;
    private final AppCompatTextHelper S;
    private AppCompatEmojiTextHelper T;
    private c U;
    private final Rect V;

    /* renamed from: e, reason: collision with root package name */
    private Drawable f1105e;

    /* renamed from: f, reason: collision with root package name */
    private ColorStateList f1106f;

    /* renamed from: g, reason: collision with root package name */
    private PorterDuff.Mode f1107g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f1108h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f1109i;

    /* renamed from: j, reason: collision with root package name */
    private Drawable f1110j;

    /* renamed from: k, reason: collision with root package name */
    private ColorStateList f1111k;

    /* renamed from: l, reason: collision with root package name */
    private PorterDuff.Mode f1112l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f1113m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f1114n;

    /* renamed from: o, reason: collision with root package name */
    private int f1115o;

    /* renamed from: p, reason: collision with root package name */
    private int f1116p;

    /* renamed from: q, reason: collision with root package name */
    private int f1117q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f1118r;

    /* renamed from: s, reason: collision with root package name */
    private CharSequence f1119s;

    /* renamed from: t, reason: collision with root package name */
    private CharSequence f1120t;

    /* renamed from: u, reason: collision with root package name */
    private CharSequence f1121u;

    /* renamed from: v, reason: collision with root package name */
    private CharSequence f1122v;

    /* renamed from: w, reason: collision with root package name */
    private boolean f1123w;

    /* renamed from: x, reason: collision with root package name */
    private int f1124x;

    /* renamed from: y, reason: collision with root package name */
    private int f1125y;

    /* renamed from: z, reason: collision with root package name */
    private float f1126z;

    /* loaded from: classes.dex */
    class a extends Property<SwitchCompat, Float> {
        a(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Float get(SwitchCompat switchCompat) {
            return Float.valueOf(switchCompat.D);
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(SwitchCompat switchCompat, Float f10) {
            switchCompat.setThumbPosition(f10.floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class b {
        static void a(ObjectAnimator objectAnimator, boolean z10) {
            objectAnimator.setAutoCancel(z10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class c extends EmojiCompat.e {

        /* renamed from: a, reason: collision with root package name */
        private final Reference<SwitchCompat> f1127a;

        c(SwitchCompat switchCompat) {
            this.f1127a = new WeakReference(switchCompat);
        }

        @Override // androidx.emoji2.text.EmojiCompat.e
        public void a(Throwable th) {
            SwitchCompat switchCompat = this.f1127a.get();
            if (switchCompat != null) {
                switchCompat.j();
            }
        }

        @Override // androidx.emoji2.text.EmojiCompat.e
        public void b() {
            SwitchCompat switchCompat = this.f1127a.get();
            if (switchCompat != null) {
                switchCompat.j();
            }
        }
    }

    public SwitchCompat(Context context) {
        this(context, null);
    }

    private void a(boolean z10) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, W, z10 ? 1.0f : 0.0f);
        this.R = ofFloat;
        ofFloat.setDuration(250L);
        b.a(this.R, true);
        this.R.start();
    }

    private void b() {
        Drawable drawable = this.f1105e;
        if (drawable != null) {
            if (this.f1108h || this.f1109i) {
                Drawable mutate = DrawableCompat.l(drawable).mutate();
                this.f1105e = mutate;
                if (this.f1108h) {
                    DrawableCompat.i(mutate, this.f1106f);
                }
                if (this.f1109i) {
                    DrawableCompat.j(this.f1105e, this.f1107g);
                }
                if (this.f1105e.isStateful()) {
                    this.f1105e.setState(getDrawableState());
                }
            }
        }
    }

    private void c() {
        Drawable drawable = this.f1110j;
        if (drawable != null) {
            if (this.f1113m || this.f1114n) {
                Drawable mutate = DrawableCompat.l(drawable).mutate();
                this.f1110j = mutate;
                if (this.f1113m) {
                    DrawableCompat.i(mutate, this.f1111k);
                }
                if (this.f1114n) {
                    DrawableCompat.j(this.f1110j, this.f1112l);
                }
                if (this.f1110j.isStateful()) {
                    this.f1110j.setState(getDrawableState());
                }
            }
        }
    }

    private void d() {
        ObjectAnimator objectAnimator = this.R;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    private void e(MotionEvent motionEvent) {
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        obtain.setAction(3);
        super.onTouchEvent(obtain);
        obtain.recycle();
    }

    private static float f(float f10, float f11, float f12) {
        return f10 < f11 ? f11 : f10 > f12 ? f12 : f10;
    }

    private CharSequence g(CharSequence charSequence) {
        TransformationMethod f10 = getEmojiTextViewHelper().f(this.Q);
        return f10 != null ? f10.getTransformation(charSequence, this) : charSequence;
    }

    private AppCompatEmojiTextHelper getEmojiTextViewHelper() {
        if (this.T == null) {
            this.T = new AppCompatEmojiTextHelper(this);
        }
        return this.T;
    }

    private boolean getTargetCheckedState() {
        return this.D > 0.5f;
    }

    private int getThumbOffset() {
        float f10;
        if (n0.b(this)) {
            f10 = 1.0f - this.D;
        } else {
            f10 = this.D;
        }
        return (int) ((f10 * getThumbScrollRange()) + 0.5f);
    }

    private int getThumbScrollRange() {
        Rect rect;
        Drawable drawable = this.f1110j;
        if (drawable == null) {
            return 0;
        }
        Rect rect2 = this.V;
        drawable.getPadding(rect2);
        Drawable drawable2 = this.f1105e;
        if (drawable2 != null) {
            rect = t.c(drawable2);
        } else {
            rect = t.f1312c;
        }
        return ((((this.E - this.G) - rect2.left) - rect2.right) - rect.left) - rect.right;
    }

    private boolean h(float f10, float f11) {
        if (this.f1105e == null) {
            return false;
        }
        int thumbOffset = getThumbOffset();
        this.f1105e.getPadding(this.V);
        int i10 = this.I;
        int i11 = this.f1125y;
        int i12 = i10 - i11;
        int i13 = (this.H + thumbOffset) - i11;
        int i14 = this.G + i13;
        Rect rect = this.V;
        return f10 > ((float) i13) && f10 < ((float) (((i14 + rect.left) + rect.right) + i11)) && f11 > ((float) i12) && f11 < ((float) (this.K + i11));
    }

    private Layout i(CharSequence charSequence) {
        return new StaticLayout(charSequence, this.M, charSequence != null ? (int) Math.ceil(Layout.getDesiredWidth(charSequence, r2)) : 0, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
    }

    private void k() {
        CharSequence charSequence = this.f1121u;
        if (charSequence == null) {
            charSequence = getResources().getString(R$string.abc_capital_off);
        }
        ViewCompat.D0(this, charSequence);
    }

    private void l() {
        CharSequence charSequence = this.f1119s;
        if (charSequence == null) {
            charSequence = getResources().getString(R$string.abc_capital_on);
        }
        ViewCompat.D0(this, charSequence);
    }

    private void o(int i10, int i11) {
        Typeface typeface;
        if (i10 == 1) {
            typeface = Typeface.SANS_SERIF;
        } else if (i10 != 2) {
            typeface = i10 != 3 ? null : Typeface.MONOSPACE;
        } else {
            typeface = Typeface.SERIF;
        }
        n(typeface, i11);
    }

    private void p() {
        if (this.U == null && this.T.b() && EmojiCompat.h()) {
            EmojiCompat b10 = EmojiCompat.b();
            int d10 = b10.d();
            if (d10 == 3 || d10 == 0) {
                c cVar = new c(this);
                this.U = cVar;
                b10.s(cVar);
            }
        }
    }

    private void q(MotionEvent motionEvent) {
        this.f1124x = 0;
        boolean z10 = true;
        boolean z11 = motionEvent.getAction() == 1 && isEnabled();
        boolean isChecked = isChecked();
        if (z11) {
            this.B.computeCurrentVelocity(1000);
            float xVelocity = this.B.getXVelocity();
            if (Math.abs(xVelocity) > this.C) {
                if (!n0.b(this) ? xVelocity <= 0.0f : xVelocity >= 0.0f) {
                    z10 = false;
                }
            } else {
                z10 = getTargetCheckedState();
            }
        } else {
            z10 = isChecked;
        }
        if (z10 != isChecked) {
            playSoundEffect(0);
        }
        setChecked(z10);
        e(motionEvent);
    }

    private void setTextOffInternal(CharSequence charSequence) {
        this.f1121u = charSequence;
        this.f1122v = g(charSequence);
        this.P = null;
        if (this.f1123w) {
            p();
        }
    }

    private void setTextOnInternal(CharSequence charSequence) {
        this.f1119s = charSequence;
        this.f1120t = g(charSequence);
        this.O = null;
        if (this.f1123w) {
            p();
        }
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        Rect rect;
        int i10;
        int i11;
        Rect rect2 = this.V;
        int i12 = this.H;
        int i13 = this.I;
        int i14 = this.J;
        int i15 = this.K;
        int thumbOffset = getThumbOffset() + i12;
        Drawable drawable = this.f1105e;
        if (drawable != null) {
            rect = t.c(drawable);
        } else {
            rect = t.f1312c;
        }
        Drawable drawable2 = this.f1110j;
        if (drawable2 != null) {
            drawable2.getPadding(rect2);
            int i16 = rect2.left;
            thumbOffset += i16;
            if (rect != null) {
                int i17 = rect.left;
                if (i17 > i16) {
                    i12 += i17 - i16;
                }
                int i18 = rect.top;
                int i19 = rect2.top;
                i10 = i18 > i19 ? (i18 - i19) + i13 : i13;
                int i20 = rect.right;
                int i21 = rect2.right;
                if (i20 > i21) {
                    i14 -= i20 - i21;
                }
                int i22 = rect.bottom;
                int i23 = rect2.bottom;
                if (i22 > i23) {
                    i11 = i15 - (i22 - i23);
                    this.f1110j.setBounds(i12, i10, i14, i11);
                }
            } else {
                i10 = i13;
            }
            i11 = i15;
            this.f1110j.setBounds(i12, i10, i14, i11);
        }
        Drawable drawable3 = this.f1105e;
        if (drawable3 != null) {
            drawable3.getPadding(rect2);
            int i24 = thumbOffset - rect2.left;
            int i25 = thumbOffset + this.G + rect2.right;
            this.f1105e.setBounds(i24, i13, i25, i15);
            Drawable background = getBackground();
            if (background != null) {
                DrawableCompat.f(background, i24, i13, i25, i15);
            }
        }
        super.draw(canvas);
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void drawableHotspotChanged(float f10, float f11) {
        super.drawableHotspotChanged(f10, f11);
        Drawable drawable = this.f1105e;
        if (drawable != null) {
            DrawableCompat.e(drawable, f10, f11);
        }
        Drawable drawable2 = this.f1110j;
        if (drawable2 != null) {
            DrawableCompat.e(drawable2, f10, f11);
        }
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        Drawable drawable = this.f1105e;
        boolean z10 = false;
        if (drawable != null && drawable.isStateful()) {
            z10 = false | drawable.setState(drawableState);
        }
        Drawable drawable2 = this.f1110j;
        if (drawable2 != null && drawable2.isStateful()) {
            z10 |= drawable2.setState(drawableState);
        }
        if (z10) {
            invalidate();
        }
    }

    @Override // android.widget.CompoundButton, android.widget.TextView
    public int getCompoundPaddingLeft() {
        if (!n0.b(this)) {
            return super.getCompoundPaddingLeft();
        }
        int compoundPaddingLeft = super.getCompoundPaddingLeft() + this.E;
        return !TextUtils.isEmpty(getText()) ? compoundPaddingLeft + this.f1117q : compoundPaddingLeft;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView
    public int getCompoundPaddingRight() {
        if (n0.b(this)) {
            return super.getCompoundPaddingRight();
        }
        int compoundPaddingRight = super.getCompoundPaddingRight() + this.E;
        return !TextUtils.isEmpty(getText()) ? compoundPaddingRight + this.f1117q : compoundPaddingRight;
    }

    @Override // android.widget.TextView
    public ActionMode.Callback getCustomSelectionActionModeCallback() {
        return TextViewCompat.p(super.getCustomSelectionActionModeCallback());
    }

    public boolean getShowText() {
        return this.f1123w;
    }

    public boolean getSplitTrack() {
        return this.f1118r;
    }

    public int getSwitchMinWidth() {
        return this.f1116p;
    }

    public int getSwitchPadding() {
        return this.f1117q;
    }

    public CharSequence getTextOff() {
        return this.f1121u;
    }

    public CharSequence getTextOn() {
        return this.f1119s;
    }

    public Drawable getThumbDrawable() {
        return this.f1105e;
    }

    protected final float getThumbPosition() {
        return this.D;
    }

    public int getThumbTextPadding() {
        return this.f1115o;
    }

    public ColorStateList getThumbTintList() {
        return this.f1106f;
    }

    public PorterDuff.Mode getThumbTintMode() {
        return this.f1107g;
    }

    public Drawable getTrackDrawable() {
        return this.f1110j;
    }

    public ColorStateList getTrackTintList() {
        return this.f1111k;
    }

    public PorterDuff.Mode getTrackTintMode() {
        return this.f1112l;
    }

    void j() {
        setTextOnInternal(this.f1119s);
        setTextOffInternal(this.f1121u);
        requestLayout();
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.f1105e;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        Drawable drawable2 = this.f1110j;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
        }
        ObjectAnimator objectAnimator = this.R;
        if (objectAnimator == null || !objectAnimator.isStarted()) {
            return;
        }
        this.R.end();
        this.R = null;
    }

    public void m(Context context, int i10) {
        TintTypedArray u7 = TintTypedArray.u(context, i10, R$styleable.TextAppearance);
        ColorStateList c10 = u7.c(R$styleable.TextAppearance_android_textColor);
        if (c10 != null) {
            this.N = c10;
        } else {
            this.N = getTextColors();
        }
        int f10 = u7.f(R$styleable.TextAppearance_android_textSize, 0);
        if (f10 != 0) {
            float f11 = f10;
            if (f11 != this.M.getTextSize()) {
                this.M.setTextSize(f11);
                requestLayout();
            }
        }
        o(u7.k(R$styleable.TextAppearance_android_typeface, -1), u7.k(R$styleable.TextAppearance_android_textStyle, -1));
        if (u7.a(R$styleable.TextAppearance_textAllCaps, false)) {
            this.Q = new AllCapsTransformationMethod(getContext());
        } else {
            this.Q = null;
        }
        setTextOnInternal(this.f1119s);
        setTextOffInternal(this.f1121u);
        u7.x();
    }

    public void n(Typeface typeface, int i10) {
        Typeface create;
        if (i10 > 0) {
            if (typeface == null) {
                create = Typeface.defaultFromStyle(i10);
            } else {
                create = Typeface.create(typeface, i10);
            }
            setSwitchTypeface(create);
            int i11 = (~(create != null ? create.getStyle() : 0)) & i10;
            this.M.setFakeBoldText((i11 & 1) != 0);
            this.M.setTextSkewX((i11 & 2) != 0 ? -0.25f : 0.0f);
            return;
        }
        this.M.setFakeBoldText(false);
        this.M.setTextSkewX(0.0f);
        setSwitchTypeface(typeface);
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected int[] onCreateDrawableState(int i10) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i10 + 1);
        if (isChecked()) {
            CompoundButton.mergeDrawableStates(onCreateDrawableState, f1104a0);
        }
        return onCreateDrawableState;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        int width;
        super.onDraw(canvas);
        Rect rect = this.V;
        Drawable drawable = this.f1110j;
        if (drawable != null) {
            drawable.getPadding(rect);
        } else {
            rect.setEmpty();
        }
        int i10 = this.I;
        int i11 = this.K;
        int i12 = i10 + rect.top;
        int i13 = i11 - rect.bottom;
        Drawable drawable2 = this.f1105e;
        if (drawable != null) {
            if (this.f1118r && drawable2 != null) {
                Rect c10 = t.c(drawable2);
                drawable2.copyBounds(rect);
                rect.left += c10.left;
                rect.right -= c10.right;
                int save = canvas.save();
                canvas.clipRect(rect, Region.Op.DIFFERENCE);
                drawable.draw(canvas);
                canvas.restoreToCount(save);
            } else {
                drawable.draw(canvas);
            }
        }
        int save2 = canvas.save();
        if (drawable2 != null) {
            drawable2.draw(canvas);
        }
        Layout layout = getTargetCheckedState() ? this.O : this.P;
        if (layout != null) {
            int[] drawableState = getDrawableState();
            ColorStateList colorStateList = this.N;
            if (colorStateList != null) {
                this.M.setColor(colorStateList.getColorForState(drawableState, 0));
            }
            this.M.drawableState = drawableState;
            if (drawable2 != null) {
                Rect bounds = drawable2.getBounds();
                width = bounds.left + bounds.right;
            } else {
                width = getWidth();
            }
            canvas.translate((width / 2) - (layout.getWidth() / 2), ((i12 + i13) / 2) - (layout.getHeight() / 2));
            layout.draw(canvas);
        }
        canvas.restoreToCount(save2);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName("android.widget.Switch");
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("android.widget.Switch");
    }

    @Override // android.widget.TextView, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        int i14;
        int width;
        int i15;
        int i16;
        int i17;
        int i18;
        super.onLayout(z10, i10, i11, i12, i13);
        int i19 = 0;
        if (this.f1105e != null) {
            Rect rect = this.V;
            Drawable drawable = this.f1110j;
            if (drawable != null) {
                drawable.getPadding(rect);
            } else {
                rect.setEmpty();
            }
            Rect c10 = t.c(this.f1105e);
            i14 = Math.max(0, c10.left - rect.left);
            i19 = Math.max(0, c10.right - rect.right);
        } else {
            i14 = 0;
        }
        if (n0.b(this)) {
            i15 = getPaddingLeft() + i14;
            width = ((this.E + i15) - i14) - i19;
        } else {
            width = (getWidth() - getPaddingRight()) - i19;
            i15 = (width - this.E) + i14 + i19;
        }
        int gravity = getGravity() & 112;
        if (gravity == 16) {
            int paddingTop = ((getPaddingTop() + getHeight()) - getPaddingBottom()) / 2;
            i16 = this.F;
            i17 = paddingTop - (i16 / 2);
        } else if (gravity != 80) {
            i17 = getPaddingTop();
            i16 = this.F;
        } else {
            i18 = getHeight() - getPaddingBottom();
            i17 = i18 - this.F;
            this.H = i15;
            this.I = i17;
            this.K = i18;
            this.J = width;
        }
        i18 = i16 + i17;
        this.H = i15;
        this.I = i17;
        this.K = i18;
        this.J = width;
    }

    @Override // android.widget.TextView, android.view.View
    public void onMeasure(int i10, int i11) {
        int i12;
        int i13;
        int i14;
        if (this.f1123w) {
            if (this.O == null) {
                this.O = i(this.f1120t);
            }
            if (this.P == null) {
                this.P = i(this.f1122v);
            }
        }
        Rect rect = this.V;
        Drawable drawable = this.f1105e;
        int i15 = 0;
        if (drawable != null) {
            drawable.getPadding(rect);
            i12 = (this.f1105e.getIntrinsicWidth() - rect.left) - rect.right;
            i13 = this.f1105e.getIntrinsicHeight();
        } else {
            i12 = 0;
            i13 = 0;
        }
        this.G = Math.max(this.f1123w ? Math.max(this.O.getWidth(), this.P.getWidth()) + (this.f1115o * 2) : 0, i12);
        Drawable drawable2 = this.f1110j;
        if (drawable2 != null) {
            drawable2.getPadding(rect);
            i15 = this.f1110j.getIntrinsicHeight();
        } else {
            rect.setEmpty();
        }
        int i16 = rect.left;
        int i17 = rect.right;
        Drawable drawable3 = this.f1105e;
        if (drawable3 != null) {
            Rect c10 = t.c(drawable3);
            i16 = Math.max(i16, c10.left);
            i17 = Math.max(i17, c10.right);
        }
        if (this.L) {
            i14 = Math.max(this.f1116p, (this.G * 2) + i16 + i17);
        } else {
            i14 = this.f1116p;
        }
        int max = Math.max(i15, i13);
        this.E = i14;
        this.F = max;
        super.onMeasure(i10, i11);
        if (getMeasuredHeight() < max) {
            setMeasuredDimension(getMeasuredWidthAndState(), max);
        }
    }

    @Override // android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        CharSequence charSequence = isChecked() ? this.f1119s : this.f1121u;
        if (charSequence != null) {
            accessibilityEvent.getText().add(charSequence);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0012, code lost:
    
        if (r0 != 3) goto L44;
     */
    @Override // android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.B.addMovement(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    int i10 = this.f1124x;
                    if (i10 == 1) {
                        float x10 = motionEvent.getX();
                        float y4 = motionEvent.getY();
                        if (Math.abs(x10 - this.f1126z) > this.f1125y || Math.abs(y4 - this.A) > this.f1125y) {
                            this.f1124x = 2;
                            getParent().requestDisallowInterceptTouchEvent(true);
                            this.f1126z = x10;
                            this.A = y4;
                            return true;
                        }
                    } else if (i10 == 2) {
                        float x11 = motionEvent.getX();
                        int thumbScrollRange = getThumbScrollRange();
                        float f10 = x11 - this.f1126z;
                        float f11 = thumbScrollRange != 0 ? f10 / thumbScrollRange : f10 > 0.0f ? 1.0f : -1.0f;
                        if (n0.b(this)) {
                            f11 = -f11;
                        }
                        float f12 = f(this.D + f11, 0.0f, 1.0f);
                        if (f12 != this.D) {
                            this.f1126z = x11;
                            setThumbPosition(f12);
                        }
                        return true;
                    }
                }
            }
            if (this.f1124x == 2) {
                q(motionEvent);
                super.onTouchEvent(motionEvent);
                return true;
            }
            this.f1124x = 0;
            this.B.clear();
        } else {
            float x12 = motionEvent.getX();
            float y10 = motionEvent.getY();
            if (isEnabled() && h(x12, y10)) {
                this.f1124x = 1;
                this.f1126z = x12;
                this.A = y10;
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.widget.TextView
    public void setAllCaps(boolean z10) {
        super.setAllCaps(z10);
        getEmojiTextViewHelper().d(z10);
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z10) {
        super.setChecked(z10);
        boolean isChecked = isChecked();
        if (isChecked) {
            l();
        } else {
            k();
        }
        if (getWindowToken() != null && ViewCompat.Q(this)) {
            a(isChecked);
        } else {
            d();
            setThumbPosition(isChecked ? 1.0f : 0.0f);
        }
    }

    @Override // android.widget.TextView
    public void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        super.setCustomSelectionActionModeCallback(TextViewCompat.q(this, callback));
    }

    public void setEmojiCompatEnabled(boolean z10) {
        getEmojiTextViewHelper().e(z10);
        setTextOnInternal(this.f1119s);
        setTextOffInternal(this.f1121u);
        requestLayout();
    }

    protected final void setEnforceSwitchWidth(boolean z10) {
        this.L = z10;
        invalidate();
    }

    @Override // android.widget.TextView
    public void setFilters(InputFilter[] inputFilterArr) {
        super.setFilters(getEmojiTextViewHelper().a(inputFilterArr));
    }

    public void setShowText(boolean z10) {
        if (this.f1123w != z10) {
            this.f1123w = z10;
            requestLayout();
            if (z10) {
                p();
            }
        }
    }

    public void setSplitTrack(boolean z10) {
        this.f1118r = z10;
        invalidate();
    }

    public void setSwitchMinWidth(int i10) {
        this.f1116p = i10;
        requestLayout();
    }

    public void setSwitchPadding(int i10) {
        this.f1117q = i10;
        requestLayout();
    }

    public void setSwitchTypeface(Typeface typeface) {
        if ((this.M.getTypeface() == null || this.M.getTypeface().equals(typeface)) && (this.M.getTypeface() != null || typeface == null)) {
            return;
        }
        this.M.setTypeface(typeface);
        requestLayout();
        invalidate();
    }

    public void setTextOff(CharSequence charSequence) {
        setTextOffInternal(charSequence);
        requestLayout();
        if (isChecked()) {
            return;
        }
        k();
    }

    public void setTextOn(CharSequence charSequence) {
        setTextOnInternal(charSequence);
        requestLayout();
        if (isChecked()) {
            l();
        }
    }

    public void setThumbDrawable(Drawable drawable) {
        Drawable drawable2 = this.f1105e;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.f1105e = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        requestLayout();
    }

    void setThumbPosition(float f10) {
        this.D = f10;
        invalidate();
    }

    public void setThumbResource(int i10) {
        setThumbDrawable(AppCompatResources.b(getContext(), i10));
    }

    public void setThumbTextPadding(int i10) {
        this.f1115o = i10;
        requestLayout();
    }

    public void setThumbTintList(ColorStateList colorStateList) {
        this.f1106f = colorStateList;
        this.f1108h = true;
        b();
    }

    public void setThumbTintMode(PorterDuff.Mode mode) {
        this.f1107g = mode;
        this.f1109i = true;
        b();
    }

    public void setTrackDrawable(Drawable drawable) {
        Drawable drawable2 = this.f1110j;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.f1110j = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        requestLayout();
    }

    public void setTrackResource(int i10) {
        setTrackDrawable(AppCompatResources.b(getContext(), i10));
    }

    public void setTrackTintList(ColorStateList colorStateList) {
        this.f1111k = colorStateList;
        this.f1113m = true;
        c();
    }

    public void setTrackTintMode(PorterDuff.Mode mode) {
        this.f1112l = mode;
        this.f1114n = true;
        c();
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void toggle() {
        setChecked(!isChecked());
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.f1105e || drawable == this.f1110j;
    }

    public SwitchCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.switchStyle);
    }

    public SwitchCompat(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f1106f = null;
        this.f1107g = null;
        this.f1108h = false;
        this.f1109i = false;
        this.f1111k = null;
        this.f1112l = null;
        this.f1113m = false;
        this.f1114n = false;
        this.B = VelocityTracker.obtain();
        this.L = true;
        this.V = new Rect();
        ThemeUtils.a(this, getContext());
        TextPaint textPaint = new TextPaint(1);
        this.M = textPaint;
        textPaint.density = getResources().getDisplayMetrics().density;
        int[] iArr = R$styleable.SwitchCompat;
        TintTypedArray w10 = TintTypedArray.w(context, attributeSet, iArr, i10, 0);
        ViewCompat.j0(this, context, iArr, attributeSet, w10.r(), i10, 0);
        Drawable g6 = w10.g(R$styleable.SwitchCompat_android_thumb);
        this.f1105e = g6;
        if (g6 != null) {
            g6.setCallback(this);
        }
        Drawable g10 = w10.g(R$styleable.SwitchCompat_track);
        this.f1110j = g10;
        if (g10 != null) {
            g10.setCallback(this);
        }
        setTextOnInternal(w10.p(R$styleable.SwitchCompat_android_textOn));
        setTextOffInternal(w10.p(R$styleable.SwitchCompat_android_textOff));
        this.f1123w = w10.a(R$styleable.SwitchCompat_showText, true);
        this.f1115o = w10.f(R$styleable.SwitchCompat_thumbTextPadding, 0);
        this.f1116p = w10.f(R$styleable.SwitchCompat_switchMinWidth, 0);
        this.f1117q = w10.f(R$styleable.SwitchCompat_switchPadding, 0);
        this.f1118r = w10.a(R$styleable.SwitchCompat_splitTrack, false);
        ColorStateList c10 = w10.c(R$styleable.SwitchCompat_thumbTint);
        if (c10 != null) {
            this.f1106f = c10;
            this.f1108h = true;
        }
        PorterDuff.Mode d10 = t.d(w10.k(R$styleable.SwitchCompat_thumbTintMode, -1), null);
        if (this.f1107g != d10) {
            this.f1107g = d10;
            this.f1109i = true;
        }
        if (this.f1108h || this.f1109i) {
            b();
        }
        ColorStateList c11 = w10.c(R$styleable.SwitchCompat_trackTint);
        if (c11 != null) {
            this.f1111k = c11;
            this.f1113m = true;
        }
        PorterDuff.Mode d11 = t.d(w10.k(R$styleable.SwitchCompat_trackTintMode, -1), null);
        if (this.f1112l != d11) {
            this.f1112l = d11;
            this.f1114n = true;
        }
        if (this.f1113m || this.f1114n) {
            c();
        }
        int n10 = w10.n(R$styleable.SwitchCompat_switchTextAppearance, 0);
        if (n10 != 0) {
            m(context, n10);
        }
        AppCompatTextHelper appCompatTextHelper = new AppCompatTextHelper(this);
        this.S = appCompatTextHelper;
        appCompatTextHelper.m(attributeSet, i10);
        w10.x();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.f1125y = viewConfiguration.getScaledTouchSlop();
        this.C = viewConfiguration.getScaledMinimumFlingVelocity();
        getEmojiTextViewHelper().c(attributeSet, i10);
        refreshDrawableState();
        setChecked(isChecked());
    }
}
