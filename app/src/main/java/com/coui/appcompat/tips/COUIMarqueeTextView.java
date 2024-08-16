package com.coui.appcompat.tips;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.hardware.display.DisplayManager;
import android.util.AttributeSet;
import android.view.Display;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import com.support.control.R$dimen;
import kotlin.Metadata;
import m1.COUILinearInterpolator;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: COUIMarqueeTextView.kt */
@Metadata(bv = {}, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0000\u0018\u0000 G2\u00020\u0001:\u0002HIB)\b\u0007\u0012\b\u0010A\u001a\u0004\u0018\u00010@\u0012\n\b\u0002\u0010C\u001a\u0004\u0018\u00010B\u0012\b\b\u0002\u0010D\u001a\u00020\u0016¢\u0006\u0004\bE\u0010FJ\b\u0010\u0003\u001a\u00020\u0002H\u0002J\b\u0010\u0004\u001a\u00020\u0002H\u0002J\b\u0010\u0006\u001a\u00020\u0005H\u0002J\b\u0010\u0007\u001a\u00020\u0002H\u0002J\u001c\u0010\f\u001a\u00020\u00022\b\u0010\t\u001a\u0004\u0018\u00010\b2\b\u0010\u000b\u001a\u0004\u0018\u00010\nH\u0016J\b\u0010\u000e\u001a\u00020\rH\u0014J\b\u0010\u000f\u001a\u00020\rH\u0014J\b\u0010\u0010\u001a\u00020\u0002H\u0014J\u0010\u0010\u0013\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0011H\u0014J\u0006\u0010\u0014\u001a\u00020\u0002J\u0006\u0010\u0015\u001a\u00020\u0002J\u0018\u0010\u0019\u001a\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0016H\u0014R\u0016\u0010\u001c\u001a\u00020\u00058\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001a\u0010\u001bR\u0016\u0010\u001f\u001a\u00020\u00168\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001d\u0010\u001eR\u0016\u0010\"\u001a\u00020\r8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b \u0010!R\u0016\u0010$\u001a\u00020\r8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b#\u0010!R\u0016\u0010(\u001a\u00020%8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b&\u0010'R\u0016\u0010)\u001a\u00020\u00168\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0014\u0010\u001eR\u0016\u0010+\u001a\u00020\u00058\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b*\u0010\u001bR\u0016\u0010,\u001a\u00020\u00168\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0006\u0010\u001eR\u0018\u0010/\u001a\u0004\u0018\u00010-8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0003\u0010.R\u001c\u00102\u001a\b\u0018\u000100R\u00020\u00008\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0004\u00101R\u0014\u00103\u001a\u00020\u00168\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0007\u0010\u001eR$\u00107\u001a\u00020\r2\u0006\u00104\u001a\u00020\r8\u0002@BX\u0082\u000e¢\u0006\f\n\u0004\b\u0015\u0010!\"\u0004\b5\u00106R*\u00109\u001a\u00020%2\u0006\u00104\u001a\u00020%8\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b8\u0010'\u001a\u0004\b9\u0010:\"\u0004\b;\u0010<R\u0014\u0010?\u001a\u00020\r8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b=\u0010>¨\u0006J"}, d2 = {"Lcom/coui/appcompat/tips/COUIMarqueeTextView;", "Landroidx/appcompat/widget/AppCompatTextView;", "Lma/f0;", "t", "u", "", "s", "v", "", "text", "Landroid/widget/TextView$BufferType;", "type", "setText", "", "getLeftFadingEdgeStrength", "getRightFadingEdgeStrength", "onDetachedFromWindow", "Landroid/graphics/Canvas;", "canvas", "onDraw", "q", "w", "", "widthMeasureSpec", "heightMeasureSpec", "onMeasure", "l", "Ljava/lang/String;", "mFinalDrawText", "m", "I", "mInitStringWidth", "n", "F", "mScrollerSpeed", "o", "mCurrentScrollLocation", "", "p", "Z", "mContinueScrollingEnable", "mScrollRepeatCount", "r", "mIndividuallyAssembledText", "mIndividuallyAssembledTextWidth", "Landroid/animation/ValueAnimator;", "Landroid/animation/ValueAnimator;", "mScroller", "Lcom/coui/appcompat/tips/COUIMarqueeTextView$b;", "Lcom/coui/appcompat/tips/COUIMarqueeTextView$b;", "mStartScrollRunnable", "mTextViewScrollDistance", ThermalBaseConfig.Item.ATTR_VALUE, "setFadingEdgeStrength", "(F)V", "fadingEdgeStrength", "x", "isMarqueeEnable", "()Z", "setMarqueeEnable", "(Z)V", "getMContentHeight", "()F", "mContentHeight", "Landroid/content/Context;", "context", "Landroid/util/AttributeSet;", "attrs", "defStyleAttr", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "y", "a", "b", "coui-support-controls_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class COUIMarqueeTextView extends AppCompatTextView {

    /* renamed from: l, reason: collision with root package name and from kotlin metadata */
    private String mFinalDrawText;

    /* renamed from: m, reason: collision with root package name and from kotlin metadata */
    private int mInitStringWidth;

    /* renamed from: n, reason: collision with root package name and from kotlin metadata */
    private float mScrollerSpeed;

    /* renamed from: o, reason: collision with root package name and from kotlin metadata */
    private float mCurrentScrollLocation;

    /* renamed from: p, reason: collision with root package name and from kotlin metadata */
    private boolean mContinueScrollingEnable;

    /* renamed from: q, reason: collision with root package name and from kotlin metadata */
    private int mScrollRepeatCount;

    /* renamed from: r, reason: collision with root package name and from kotlin metadata */
    private String mIndividuallyAssembledText;

    /* renamed from: s, reason: collision with root package name and from kotlin metadata */
    private int mIndividuallyAssembledTextWidth;

    /* renamed from: t, reason: collision with root package name and from kotlin metadata */
    private ValueAnimator mScroller;

    /* renamed from: u, reason: collision with root package name and from kotlin metadata */
    private b mStartScrollRunnable;

    /* renamed from: v, reason: collision with root package name and from kotlin metadata */
    private final int mTextViewScrollDistance;

    /* renamed from: w, reason: collision with root package name and from kotlin metadata */
    private float fadingEdgeStrength;

    /* renamed from: x, reason: collision with root package name and from kotlin metadata */
    private boolean isMarqueeEnable;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: COUIMarqueeTextView.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0004\u0010\u0005J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0006"}, d2 = {"Lcom/coui/appcompat/tips/COUIMarqueeTextView$b;", "Ljava/lang/Runnable;", "Lma/f0;", "run", "<init>", "(Lcom/coui/appcompat/tips/COUIMarqueeTextView;)V", "coui-support-controls_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public final class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ COUIMarqueeTextView f7948e;

        public b(COUIMarqueeTextView cOUIMarqueeTextView) {
            k.e(cOUIMarqueeTextView, "this$0");
            this.f7948e = cOUIMarqueeTextView;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f7948e.q();
        }
    }

    public COUIMarqueeTextView(Context context) {
        this(context, null, 0, 6, null);
    }

    public COUIMarqueeTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIMarqueeTextView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        k.b(context);
        this.mFinalDrawText = "";
        this.mScrollerSpeed = getResources().getDimensionPixelOffset(R$dimen.coui_top_tips_scroll_speed);
        this.mCurrentScrollLocation = getResources().getDimensionPixelOffset(R$dimen.coui_top_tips_scroll_text_start_location);
        this.mIndividuallyAssembledText = "";
        this.mTextViewScrollDistance = getResources().getDimensionPixelOffset(R$dimen.coui_top_tips_scroll_text_interval);
        t();
        u();
        if (this.isMarqueeEnable) {
            postDelayed(this.mStartScrollRunnable, 1000L);
        }
    }

    private final float getMContentHeight() {
        return Math.abs(getPaint().getFontMetrics().bottom - getPaint().getFontMetrics().top) / 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void r(COUIMarqueeTextView cOUIMarqueeTextView, ValueAnimator valueAnimator) {
        k.e(cOUIMarqueeTextView, "this$0");
        cOUIMarqueeTextView.mCurrentScrollLocation -= cOUIMarqueeTextView.mScrollerSpeed;
        cOUIMarqueeTextView.invalidate();
    }

    private final String s() {
        int ceil = (int) Math.ceil(this.mTextViewScrollDistance / getPaint().measureText(" "));
        String str = this.mTextViewScrollDistance == 0 ? " " : "";
        int i10 = 0;
        if (ceil >= 0) {
            while (true) {
                int i11 = i10 + 1;
                str = k.l(str, " ");
                if (i10 == ceil) {
                    break;
                }
                i10 = i11;
            }
        }
        return str;
    }

    private final void setFadingEdgeStrength(float f10) {
        this.fadingEdgeStrength = Math.signum(f10);
    }

    private final void t() {
        Display display = ((DisplayManager) getContext().getSystemService(DisplayManager.class)).getDisplay(0);
        getResources().getDisplayMetrics();
        this.mScrollerSpeed = getResources().getDimensionPixelOffset(R$dimen.coui_top_tips_scroll_speed) / display.getRefreshRate();
        this.mStartScrollRunnable = new b(this);
    }

    private final void u() {
        setHorizontalFadingEdgeEnabled(true);
        setFadingEdgeLength(getResources().getDimensionPixelSize(R$dimen.coui_top_tips_fading_edge_size));
        this.mCurrentScrollLocation = getResources().getDimensionPixelOffset(R$dimen.coui_top_tips_scroll_text_start_location);
        getPaint().setColor(getCurrentTextColor());
        this.mFinalDrawText = getText().toString();
    }

    private final void v() {
        String obj = getText().toString();
        this.mIndividuallyAssembledText = obj;
        this.mIndividuallyAssembledText = k.l(obj, s());
        int i10 = 0;
        this.mScrollRepeatCount = 0;
        this.mIndividuallyAssembledTextWidth = (int) getPaint().measureText(this.mIndividuallyAssembledText);
        int ceil = (int) Math.ceil((getMeasuredWidth() / this.mIndividuallyAssembledTextWidth) + 1.0d);
        this.mFinalDrawText = k.l(this.mFinalDrawText, s());
        if (ceil >= 0) {
            while (true) {
                int i11 = i10 + 1;
                this.mFinalDrawText = k.l(this.mFinalDrawText, this.mIndividuallyAssembledText);
                if (i10 == ceil) {
                    break;
                } else {
                    i10 = i11;
                }
            }
        }
        this.mInitStringWidth = (int) getPaint().measureText(this.mFinalDrawText);
    }

    @Override // android.widget.TextView, android.view.View
    protected float getLeftFadingEdgeStrength() {
        return this.fadingEdgeStrength;
    }

    @Override // android.widget.TextView, android.view.View
    protected float getRightFadingEdgeStrength() {
        return this.fadingEdgeStrength;
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.isMarqueeEnable) {
            w();
            removeCallbacks(this.mStartScrollRunnable);
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        k.e(canvas, "canvas");
        if (!this.isMarqueeEnable) {
            super.onDraw(canvas);
            return;
        }
        float f10 = this.mCurrentScrollLocation;
        if (f10 < 0.0f) {
            int abs = (int) Math.abs(f10 / this.mIndividuallyAssembledTextWidth);
            int i10 = this.mScrollRepeatCount;
            if (abs >= i10) {
                this.mScrollRepeatCount = i10 + 1;
                if (this.mCurrentScrollLocation <= (-this.mInitStringWidth)) {
                    String substring = this.mFinalDrawText.substring(this.mIndividuallyAssembledText.length());
                    k.d(substring, "this as java.lang.String).substring(startIndex)");
                    this.mFinalDrawText = substring;
                    this.mCurrentScrollLocation += this.mIndividuallyAssembledTextWidth;
                    this.mScrollRepeatCount--;
                }
                this.mFinalDrawText = k.l(this.mFinalDrawText, this.mIndividuallyAssembledText);
            }
        }
        canvas.drawText(this.mFinalDrawText, this.mCurrentScrollLocation, (getHeight() + getMContentHeight()) / 2, getPaint());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatTextView, android.widget.TextView, android.view.View
    public void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        if (getPaint().measureText(getText().toString()) > getMeasuredWidth()) {
            if (this.isMarqueeEnable) {
                v();
                return;
            }
            return;
        }
        setMarqueeEnable(false);
    }

    public final void q() {
        setMarqueeEnable(true);
        if (getPaint().measureText(getText().toString()) <= getMeasuredWidth() || this.mContinueScrollingEnable) {
            return;
        }
        ValueAnimator valueAnimator = this.mScroller;
        if (valueAnimator != null) {
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            this.mScroller = null;
        }
        this.mContinueScrollingEnable = true;
        ValueAnimator ofInt = ValueAnimator.ofInt(Integer.MAX_VALUE);
        this.mScroller = ofInt;
        if (ofInt == null) {
            return;
        }
        ofInt.setDuration(2147483647L);
        ofInt.setInterpolator(new COUILinearInterpolator());
        ofInt.setRepeatCount(-1);
        ofInt.setRepeatMode(1);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.tips.a
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                COUIMarqueeTextView.r(COUIMarqueeTextView.this, valueAnimator2);
            }
        });
        ofInt.start();
    }

    public final void setMarqueeEnable(boolean z10) {
        float f10;
        if (z10) {
            setSingleLine(true);
            setMaxLines(1);
            f10 = 1.0f;
        } else {
            setSingleLine(false);
            setMaxLines(Integer.MAX_VALUE);
            f10 = 0.0f;
        }
        setFadingEdgeStrength(f10);
        this.isMarqueeEnable = z10;
    }

    @Override // android.widget.TextView
    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        this.mFinalDrawText = String.valueOf(charSequence);
        super.setText(charSequence, bufferType);
    }

    public final void w() {
        this.mContinueScrollingEnable = false;
        this.mCurrentScrollLocation = getResources().getDimensionPixelOffset(R$dimen.coui_top_tips_scroll_text_start_location);
        ValueAnimator valueAnimator = this.mScroller;
        if (valueAnimator != null && valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.mScroller = null;
    }

    public /* synthetic */ COUIMarqueeTextView(Context context, AttributeSet attributeSet, int i10, int i11, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i11 & 2) != 0 ? null : attributeSet, (i11 & 4) != 0 ? 0 : i10);
    }
}
