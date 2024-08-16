package com.oplus.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.oplus.view.IOplusScrollBarEffect;

/* loaded from: classes.dex */
public class OplusScrollBarEffect implements IOplusScrollBarEffect {
    public static final IOplusScrollBarEffect NO_EFFECT = new NoEffect();
    private final int mMinHeightNormal;
    private final int mMinHeightOverScroll;
    private final int mPadding;
    private final IOplusScrollBarEffect.ViewCallback mViewCallback;
    private int mOverScrollLengthY = 0;
    private boolean mIsTouchPressed = false;

    public OplusScrollBarEffect(Context context, IOplusScrollBarEffect.ViewCallback viewCallback) {
        this.mViewCallback = viewCallback;
        Resources res = context.getResources();
        this.mPadding = res.getDimensionPixelSize(201654378);
        this.mMinHeightOverScroll = res.getDimensionPixelSize(201654379);
        this.mMinHeightNormal = res.getDimensionPixelSize(201654380);
    }

    public OplusScrollBarEffect(Resources res, IOplusScrollBarEffect.ViewCallback viewCallback) {
        this.mViewCallback = viewCallback;
        this.mPadding = res.getDimensionPixelSize(201654378);
        this.mMinHeightOverScroll = res.getDimensionPixelSize(201654379);
        this.mMinHeightNormal = res.getDimensionPixelSize(201654380);
    }

    @Override // com.oplus.view.IOplusScrollBarEffect
    public void getDrawRect(Rect rect) {
        rect.inset(0, this.mPadding);
        rect.offset(getOffsetX(), 0);
    }

    @Override // com.oplus.view.IOplusScrollBarEffect
    public int getThumbLength(int size, int thickness, int extent, int range) {
        int minLength = this.mOverScrollLengthY != 0 ? this.mMinHeightOverScroll : this.mMinHeightNormal;
        int length = (Math.max(Math.round((size * extent) / range), this.mMinHeightNormal) - this.mOverScrollLengthY) - (this.mPadding * 2);
        if (length < minLength) {
            return minLength;
        }
        return length;
    }

    @Override // com.oplus.view.IOplusScrollBarEffect
    public void onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case 0:
                this.mIsTouchPressed = true;
                return;
            case 1:
            case 3:
                this.mIsTouchPressed = false;
                this.mViewCallback.awakenScrollBars();
                return;
            case 2:
            default:
                return;
        }
    }

    @Override // com.oplus.view.IOplusScrollBarEffect
    public void onOverScrolled(int scrollX, int scrollY, int scrollRangeX, int scrollRangeY) {
        this.mOverScrollLengthY = getOverScrollLength(scrollY, scrollRangeY);
    }

    @Override // com.oplus.view.IOplusScrollBarEffect
    public boolean isTouchPressed() {
        return this.mIsTouchPressed;
    }

    private int getOverScrollLength(int scrollPos, int scrollRange) {
        if (scrollPos < 0) {
            return -scrollPos;
        }
        if (scrollPos > scrollRange) {
            return scrollPos - scrollRange;
        }
        return 0;
    }

    private int getOffsetX() {
        return this.mViewCallback.isLayoutRtl() ? this.mPadding : -this.mPadding;
    }

    /* loaded from: classes.dex */
    private static class NoEffect implements IOplusScrollBarEffect {
        private NoEffect() {
        }

        @Override // com.oplus.view.IOplusScrollBarEffect
        public void getDrawRect(Rect rect) {
        }

        @Override // com.oplus.view.IOplusScrollBarEffect
        public int getThumbLength(int size, int thickness, int extent, int range) {
            return OplusScrollBarUtils.getThumbLength(size, thickness, extent, range);
        }

        @Override // com.oplus.view.IOplusScrollBarEffect
        public void onTouchEvent(MotionEvent event) {
        }

        @Override // com.oplus.view.IOplusScrollBarEffect
        public void onOverScrolled(int scrollX, int scrollY, int scrollRangeX, int scrollRangeY) {
        }

        @Override // com.oplus.view.IOplusScrollBarEffect
        public boolean isTouchPressed() {
            return false;
        }
    }
}
