package com.coui.appcompat.scanview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.oplus.anim.EffectiveAnimationView;
import fb._Ranges;
import kotlin.Metadata;
import ma.h;
import ma.j;
import n2.CameraOrientationListener;
import za.Lambda;
import za.k;

/* compiled from: RotateLottieAnimationView.kt */
@Metadata(bv = {}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 52\u00020\u0001:\u00016B\u0011\b\u0016\u0012\u0006\u0010-\u001a\u00020,¢\u0006\u0004\b.\u0010/B\u0019\b\u0016\u0012\u0006\u0010-\u001a\u00020,\u0012\u0006\u00101\u001a\u000200¢\u0006\u0004\b.\u00102B!\b\u0016\u0012\u0006\u0010-\u001a\u00020,\u0012\u0006\u00101\u001a\u000200\u0012\u0006\u00103\u001a\u00020\u0002¢\u0006\u0004\b.\u00104J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\u0018\u0010\b\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\u0010\u0010\u000b\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\u0002H\u0014J\b\u0010\f\u001a\u00020\nH\u0014J\b\u0010\r\u001a\u00020\nH\u0014J\u0006\u0010\u000e\u001a\u00020\nJ\u0006\u0010\u000f\u001a\u00020\nJ\u0006\u0010\u0010\u001a\u00020\nJ\u000e\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u0002J\u0010\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\u0013H\u0014R\u0016\u0010\u0017\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0010\u0010\u0016R\u0016\u0010\u0019\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0018\u0010\u0016R\u0016\u0010\u0011\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001a\u0010\u0016R\u0016\u0010\u001d\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001b\u0010\u001cR\u0016\u0010\u001f\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001e\u0010\u001cR\u0016\u0010#\u001a\u00020 8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b!\u0010\"R\u0016\u0010$\u001a\u00020 8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0016\u0010\"R\u0016\u0010%\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\"\u0010\u001cR\u001b\u0010+\u001a\u00020&8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b'\u0010(\u001a\u0004\b)\u0010*¨\u00067"}, d2 = {"Lcom/coui/appcompat/scanview/RotateLottieAnimationView;", "Lcom/oplus/anim/EffectiveAnimationView;", "", "keyCode", "Landroid/view/KeyEvent;", "event", "", "onKeyDown", "onKeyUp", "visibility", "Lma/f0;", "onWindowVisibilityChanged", "onAttachedToWindow", "onDetachedFromWindow", "A", "B", "C", "degree", "setOrientation", "Landroid/graphics/Canvas;", "canvas", "onDraw", "I", "currentDegree", "D", "startDegree", "E", "F", "Z", "clockwise", "G", "enableAnimation", "", "H", "J", "animationStartTime", "animationEndTime", "forceDisable", "Ln2/a;", "orientationListener$delegate", "Lma/h;", "getOrientationListener", "()Ln2/a;", "orientationListener", "Landroid/content/Context;", "context", "<init>", "(Landroid/content/Context;)V", "Landroid/util/AttributeSet;", "attrs", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "defStyle", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "L", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class RotateLottieAnimationView extends EffectiveAnimationView {

    /* renamed from: C, reason: from kotlin metadata */
    private int currentDegree;

    /* renamed from: D, reason: from kotlin metadata */
    private int startDegree;

    /* renamed from: E, reason: from kotlin metadata */
    private int degree;

    /* renamed from: F, reason: from kotlin metadata */
    private boolean clockwise;

    /* renamed from: G, reason: from kotlin metadata */
    private boolean enableAnimation;

    /* renamed from: H, reason: from kotlin metadata */
    private long animationStartTime;

    /* renamed from: I, reason: from kotlin metadata */
    private long animationEndTime;

    /* renamed from: J, reason: from kotlin metadata */
    private boolean forceDisable;
    private final h K;

    /* compiled from: RotateLottieAnimationView.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0007\n\u0002\b\u0002*\u0001\u0000\u0010\u0001\u001a\u00020\u0000H\n"}, d2 = {"com/coui/appcompat/scanview/RotateLottieAnimationView$b$a", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    static final class b extends Lambda implements ya.a<a> {

        /* compiled from: RotateLottieAnimationView.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0006"}, d2 = {"com/coui/appcompat/scanview/RotateLottieAnimationView$b$a", "Ln2/a;", "", "orientation", "Lma/f0;", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
        /* loaded from: classes.dex */
        public static final class a extends CameraOrientationListener {

            /* renamed from: c, reason: collision with root package name */
            final /* synthetic */ RotateLottieAnimationView f7322c;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(RotateLottieAnimationView rotateLottieAnimationView, Context context) {
                super(context);
                this.f7322c = rotateLottieAnimationView;
                k.d(context, "context");
            }

            @Override // n2.CameraOrientationListener
            public void a(int i10) {
                this.f7322c.setOrientation(i10);
            }
        }

        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final a invoke() {
            return new a(RotateLottieAnimationView.this, RotateLottieAnimationView.this.getContext());
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public RotateLottieAnimationView(Context context) {
        super(context);
        h b10;
        k.e(context, "context");
        this.enableAnimation = true;
        b10 = j.b(new b());
        this.K = b10;
    }

    private final CameraOrientationListener getOrientationListener() {
        return (CameraOrientationListener) this.K.getValue();
    }

    public final void A() {
        this.forceDisable = true;
        getOrientationListener().disable();
    }

    public final void B() {
        this.forceDisable = false;
        getOrientationListener().enable();
    }

    public final void C() {
        setOrientation(0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.anim.EffectiveAnimationView, android.widget.ImageView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.forceDisable) {
            return;
        }
        getOrientationListener().enable();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.anim.EffectiveAnimationView, android.widget.ImageView, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.forceDisable) {
            return;
        }
        getOrientationListener().disable();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        float e10;
        k.e(canvas, "canvas");
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        Rect bounds = drawable.getBounds();
        int i10 = bounds.right - bounds.left;
        int i11 = bounds.bottom - bounds.top;
        if (i10 == 0 || i11 == 0) {
            return;
        }
        if (this.currentDegree != this.degree) {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            if (currentAnimationTimeMillis < this.animationEndTime) {
                int i12 = (int) (currentAnimationTimeMillis - this.animationStartTime);
                int i13 = this.startDegree;
                if (!this.clockwise) {
                    i12 = -i12;
                }
                int i14 = i13 + ((i12 * 270) / 1000);
                this.currentDegree = i14 >= 0 ? i14 % 360 : (i14 % 360) + 360;
                invalidate();
            } else {
                this.currentDegree = this.degree;
            }
        }
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int width = (getWidth() - paddingLeft) - paddingRight;
        int height = (getHeight() - paddingTop) - paddingBottom;
        int saveCount = canvas.getSaveCount();
        if (getScaleType() == ImageView.ScaleType.FIT_CENTER && (width < i10 || height < i11)) {
            float f10 = width;
            float f11 = height;
            e10 = _Ranges.e(f10 / i10, f11 / i11);
            canvas.scale(e10, e10, f10 / 2.0f, f11 / 2.0f);
        }
        canvas.translate(paddingLeft + (width / 2.0f), paddingTop + (height / 2.0f));
        canvas.rotate(-this.currentDegree);
        canvas.translate((-i10) / 2.0f, (-i11) / 2.0f);
        drawable.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        k.e(event, "event");
        return false;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        k.e(event, "event");
        return false;
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i10) {
        super.onWindowVisibilityChanged(i10);
        if (this.forceDisable) {
            return;
        }
        if (i10 == 0) {
            getOrientationListener().enable();
        } else {
            getOrientationListener().disable();
        }
    }

    public final void setOrientation(int i10) {
        boolean z10 = getVisibility() == 0;
        this.enableAnimation = z10;
        int i11 = i10 >= 0 ? i10 % 360 : (i10 % 360) + 360;
        if (i11 == this.degree) {
            return;
        }
        this.degree = i11;
        if (z10) {
            this.startDegree = this.currentDegree;
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.animationStartTime = currentAnimationTimeMillis;
            int i12 = this.degree - this.currentDegree;
            if (i12 < 0) {
                i12 += 360;
            }
            if (i12 > 180) {
                i12 -= 360;
            }
            this.clockwise = i12 >= 0;
            this.animationEndTime = currentAnimationTimeMillis + ((Math.abs(i12) * 1000) / 270);
        } else {
            this.currentDegree = i11;
        }
        invalidate();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public RotateLottieAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        h b10;
        k.e(context, "context");
        k.e(attributeSet, "attrs");
        this.enableAnimation = true;
        b10 = j.b(new b());
        this.K = b10;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public RotateLottieAnimationView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        h b10;
        k.e(context, "context");
        k.e(attributeSet, "attrs");
        this.enableAnimation = true;
        b10 = j.b(new b());
        this.K = b10;
    }
}
