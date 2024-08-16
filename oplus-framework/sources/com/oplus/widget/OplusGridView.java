package com.oplus.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.IntArray;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OplusBezierInterpolator;
import android.view.animation.PathInterpolator;
import com.android.internal.widget.ExploreByTouchHelper;
import com.oplus.util.OplusChangeTextUtil;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusGridView extends View {
    private static final float APPNAME_TEXT_FIRST_SCALEMULTIPLIER = 0.88f;
    private static final float APPNAME_TEXT_SECOND_SCALE_MULTIPLIER = 0.7744f;
    public static final int COLUMN_SIZE = 4;
    private static final int PFLAG_PREPRESSED = 33554432;
    private static final int PFLAG_PRESSED = 16384;
    private static final Interpolator POLATOR = new PathInterpolator(0.32f, 1.22f, 0.32f, 1.0f);
    public static final String TAG = "OplusGridView";
    private static final int TOUCH_END_DURATION = 300;
    private static final float TOUCH_MODE_BRIGHTNESS = 1.09f;
    private static final int TOUCH_START_DURATION = 66;
    private int dotViewHeight;
    private boolean isSelected;
    private int mAppIconMarginBottom;
    private int mAppIconMarginTop;
    private Integer[][] mAppIcons;
    private OplusItem[][] mAppInfos;
    private int mAppNameSize;
    private String[][] mAppNames;
    private boolean[][] mCanDraw;
    private Runnable mCancelclickRunnable;
    private int mChineseLength;
    private int mColumnCounts;
    private Context mContext;
    private float mCurrentBrightness;
    private int mCurrentIconWidth;
    private int mCurrentPosition;
    private float mDownX;
    private float mDownY;
    private int mEnglishLength;
    private GestureDetector mGestureDetector;
    private int mIconDistance;
    private int mIconHeight;
    private int mIconWidth;
    private boolean mIsLandscape;
    private int mItemBgPaddingRight;
    private int mItemCounts;
    private int mItemHeight;
    private int mItemWidth;
    private Drawable mMoreIcon;
    private int mMoreIconAlpha;
    private int mMoreIconIndex;
    private int mNavBarHeight;
    private boolean mNeedExpandAnim;
    private int mOShareIconMarginBottom;
    private int mOShareIconMarginTop;
    private OnItemClickListener mOnItemClickListener;
    private Runnable mOnclickRunnable;
    private int[][] mOpacity;
    private int mPaddingLeft;
    private int mPaddingTop;
    public int mPageNumber;
    private int mPagerSize;
    private TextPaint mPaint1;
    private Paint mPaint2;
    private int mPrivateFlags;
    private List<ResolveInfo> mResolveInfoList;
    private int mRowCounts;
    private Float[][] mScale;
    private int mSelectColor;
    private ColorMatrixColorFilter mSelectColorFilter;
    private ColorMatrix mSelectColorMatrix;
    private int mSelectHeight;
    private int mSelectWidth;
    private int mTextColor;
    private int mTextPaddingBottom;
    private int mTextPaddingLeft;
    private int mTotalHeight;
    private OplusBezierInterpolator mTouchEndInterpolator;
    private final OplusViewTouchHelper mTouchHelper;
    private int mTouchModeWidth;
    private OplusBezierInterpolator mTouchStartInterpolator;
    private int mTwoLineDistance;
    private Rect selRect;
    private Rect selRect2;
    private int selectX;
    private int selectY;

    /* loaded from: classes.dex */
    public interface OnItemClickListener {
        void OnItemClick(int i);

        void OnItemLongClick(int i);
    }

    public OplusGridView(Context context) {
        this(context, null);
    }

    public OplusGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v49, types: [android.view.View$AccessibilityDelegate, com.oplus.widget.OplusGridView$OplusViewTouchHelper] */
    public OplusGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPagerSize = 4;
        this.mColumnCounts = 4;
        this.mRowCounts = 2;
        this.mItemCounts = 0;
        this.mPageNumber = 1;
        this.isSelected = false;
        this.selRect = new Rect();
        this.selRect2 = new Rect();
        this.mSelectColorMatrix = new ColorMatrix();
        this.mResolveInfoList = new ArrayList();
        this.mAppInfos = null;
        Float valueOf = Float.valueOf(1.0f);
        this.mScale = new Float[][]{new Float[]{valueOf, valueOf, valueOf, valueOf}, new Float[]{valueOf, valueOf, valueOf, valueOf}};
        this.mOpacity = new int[][]{new int[]{255, 255, 255, 255}, new int[]{255, 255, 255, 255}};
        this.mMoreIconAlpha = 255;
        this.mCanDraw = (boolean[][]) Array.newInstance((Class<?>) Boolean.TYPE, 2, 4);
        this.mMoreIconIndex = 1;
        this.mTouchStartInterpolator = new OplusBezierInterpolator(0.25d, 0.1d, 0.1d, 1.0d, true);
        this.mTouchEndInterpolator = new OplusBezierInterpolator(0.25d, 0.1d, 0.25d, 1.0d, true);
        this.mCurrentBrightness = 1.0f;
        this.mOnclickRunnable = new Runnable() { // from class: com.oplus.widget.OplusGridView.6
            @Override // java.lang.Runnable
            public void run() {
                OplusGridView.this.mPrivateFlags |= -33554433;
                OplusGridView oplusGridView = OplusGridView.this;
                oplusGridView.invalidate(oplusGridView.selRect);
            }
        };
        this.mCancelclickRunnable = new Runnable() { // from class: com.oplus.widget.OplusGridView.7
            @Override // java.lang.Runnable
            public void run() {
                OplusGridView.this.mPrivateFlags |= -33554433;
                OplusGridView oplusGridView = OplusGridView.this;
                oplusGridView.removeCallbacks(oplusGridView.mOnclickRunnable);
                OplusGridView.this.isSelected = false;
                OplusGridView oplusGridView2 = OplusGridView.this;
                oplusGridView2.invalidate(oplusGridView2.selRect);
            }
        };
        this.mCurrentPosition = -1;
        setLayerType(1, null);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.mContext = context;
        context.getResources().getConfiguration();
        initGetureDetecor();
        this.mSelectColor = getResources().getColor(201719848);
        this.mTextColor = getResources().getColor(201719851);
        int textSize = getResources().getDimensionPixelSize(201654341);
        float fontScale = this.mContext.getResources().getConfiguration().fontScale;
        this.mAppNameSize = (int) OplusChangeTextUtil.getSuitableFontSize(textSize, fontScale, 2);
        this.dotViewHeight = (int) getResources().getDimension(201654318);
        this.mItemHeight = (int) getResources().getDimension(201654320);
        this.mSelectHeight = (int) getResources().getDimension(201654315);
        this.mSelectWidth = (int) getResources().getDimension(201654316);
        this.mPaddingLeft = (int) getResources().getDimension(201654321);
        this.mPaddingTop = (int) getResources().getDimension(201654322);
        this.mOShareIconMarginTop = (int) getResources().getDimension(201654339);
        this.mAppIconMarginTop = (int) getResources().getDimension(201654340);
        this.mIconHeight = getResources().getDimensionPixelSize(201654323);
        int dimensionPixelSize = getResources().getDimensionPixelSize(201654324);
        this.mIconWidth = dimensionPixelSize;
        this.mCurrentIconWidth = dimensionPixelSize;
        this.mTouchModeWidth = dimensionPixelSize + dip2px(getContext(), 3.0f);
        this.mItemBgPaddingRight = getResources().getDimensionPixelSize(201654335);
        this.mTextPaddingBottom = getResources().getDimensionPixelSize(201654336);
        this.mTwoLineDistance = getResources().getDimensionPixelSize(201654337);
        this.mChineseLength = getResources().getDimensionPixelSize(201654343);
        this.mEnglishLength = getResources().getDimensionPixelSize(201654344);
        this.mMoreIcon = this.mContext.getDrawable(201850947);
        this.mSelectColorFilter = new ColorMatrixColorFilter(this.mSelectColorMatrix);
        ?? oplusViewTouchHelper = new OplusViewTouchHelper(this);
        this.mTouchHelper = oplusViewTouchHelper;
        setAccessibilityDelegate(oplusViewTouchHelper);
        setImportantForAccessibility(1);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Configuration cfg = this.mContext.getResources().getConfiguration();
        if (this.mAppInfos == null) {
            return;
        }
        if (cfg.orientation == 2) {
            this.mRowCounts = 1;
            this.mIsLandscape = true;
        } else {
            this.mRowCounts = Math.min(2, this.mAppInfos.length);
            this.mIsLandscape = false;
        }
        Context context = this.mContext;
        if ((context instanceof Activity) && ((Activity) context).isInMultiWindowMode()) {
            this.mRowCounts = 1;
            this.mIsLandscape = true;
        }
        this.mTextPaddingLeft = 0;
        this.mItemWidth = getWidth() / this.mColumnCounts;
        TextPaint textPaint = new TextPaint();
        this.mPaint1 = textPaint;
        textPaint.setColor(this.mTextColor);
        this.mPaint1.setTextSize(this.mAppNameSize);
        this.mPaint1.setTextAlign(Paint.Align.CENTER);
        this.mPaint1.setAntiAlias(true);
        this.mPaint1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        Paint paint = new Paint();
        this.mPaint2 = paint;
        paint.setColor(this.mSelectColor);
        for (int i = 0; i < this.mRowCounts; i++) {
            for (int j = 0; j < this.mAppInfos[i].length; j++) {
                int position = (this.mColumnCounts * i) + j;
                getRect2(j, i, this.selRect2, position);
                OplusItem oplusItem = this.mAppInfos[i][j];
                if (oplusItem != null) {
                    oplusItem.getIcon().setBounds(this.selRect2);
                    if (this.mNeedExpandAnim) {
                        if (position == this.mMoreIconIndex) {
                            if (position == this.mCurrentPosition) {
                                ColorMatrix colorMatrix = this.mSelectColorMatrix;
                                float f = this.mCurrentBrightness;
                                colorMatrix.setScale(f, f, f, 1.0f);
                                this.mSelectColorFilter.setColorMatrix(this.mSelectColorMatrix);
                                this.mMoreIcon.setColorFilter(this.mSelectColorFilter);
                            } else {
                                this.mSelectColorMatrix.setScale(1.0f, 1.0f, 1.0f, 1.0f);
                                this.mSelectColorFilter.setColorMatrix(this.mSelectColorMatrix);
                                this.mMoreIcon.setColorFilter(this.mSelectColorFilter);
                            }
                            this.mMoreIcon.setBounds(this.selRect2);
                            this.mMoreIcon.setAlpha(this.mMoreIconAlpha);
                            this.mMoreIcon.draw(canvas);
                            Log.d(TAG, "moreIcon = " + position + ", alpha = " + this.mMoreIconAlpha);
                        }
                        if (this.mCanDraw[i][j]) {
                            canvas.save();
                            float scale = this.mScale[i][j].floatValue();
                            canvas.scale(scale, scale, this.selRect2.exactCenterX(), this.selRect2.exactCenterY());
                            if (position == this.mCurrentPosition) {
                                ColorMatrix colorMatrix2 = this.mSelectColorMatrix;
                                float f2 = this.mCurrentBrightness;
                                colorMatrix2.setScale(f2, f2, f2, 1.0f);
                                this.mSelectColorFilter.setColorMatrix(this.mSelectColorMatrix);
                                this.mAppInfos[i][j].getIcon().setColorFilter(this.mSelectColorFilter);
                            } else {
                                this.mSelectColorMatrix.setScale(1.0f, 1.0f, 1.0f, 1.0f);
                                this.mSelectColorFilter.setColorMatrix(this.mSelectColorMatrix);
                                this.mAppInfos[i][j].getIcon().setColorFilter(this.mSelectColorFilter);
                            }
                            this.mAppInfos[i][j].getIcon().setAlpha(this.mOpacity[i][j]);
                            this.mAppInfos[i][j].getIcon().draw(canvas);
                            canvas.restore();
                            this.mPaint1.setAlpha((int) (this.mOpacity[i][j] * 0.7f));
                            drawText(canvas, this.mAppInfos[i][j].getText(), i, j);
                        }
                    } else {
                        if (position == this.mCurrentPosition) {
                            ColorMatrix colorMatrix3 = this.mSelectColorMatrix;
                            float f3 = this.mCurrentBrightness;
                            colorMatrix3.setScale(f3, f3, f3, 1.0f);
                            this.mSelectColorFilter.setColorMatrix(this.mSelectColorMatrix);
                            this.mAppInfos[i][j].getIcon().setColorFilter(this.mSelectColorFilter);
                        } else {
                            this.mSelectColorMatrix.setScale(1.0f, 1.0f, 1.0f, 1.0f);
                            this.mSelectColorFilter.setColorMatrix(this.mSelectColorMatrix);
                            this.mAppInfos[i][j].getIcon().setColorFilter(this.mSelectColorFilter);
                        }
                        this.mAppInfos[i][j].getIcon().setAlpha(255);
                        this.mAppInfos[i][j].getIcon().draw(canvas);
                        drawText(canvas, this.mAppInfos[i][j].getText(), i, j);
                    }
                }
            }
        }
    }

    public void setMoreIconIndex(int index) {
        this.mMoreIconIndex = index;
    }

    public void startExpandAnimation() {
        int i;
        this.mNeedExpandAnim = true;
        for (int i2 = 0; i2 < this.mRowCounts; i2++) {
            int j = 0;
            while (true) {
                OplusItem[] oplusItemArr = this.mAppInfos[i2];
                if (j < oplusItemArr.length) {
                    int position = (this.mColumnCounts * i2) + j;
                    this.mCanDraw[i2][j] = true;
                    if (oplusItemArr[j] != null && position >= (i = this.mMoreIconIndex)) {
                        ValueAnimator opacityAnimation = getAlphaAnim(i2, j, position - i);
                        ValueAnimator scaleAnimation = getScaleAnim(i2, j, position - this.mMoreIconIndex);
                        ValueAnimator moreIconAnim = getMoreIconAnim();
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(opacityAnimation, scaleAnimation, moreIconAnim);
                        animatorSet.start();
                    }
                    j++;
                }
            }
        }
    }

    private ValueAnimator getAlphaAnim(final int i, final int j, int position) {
        ValueAnimator opacityAnimation = ValueAnimator.ofInt(0, 255);
        this.mCanDraw[i][j] = false;
        opacityAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.oplus.widget.OplusGridView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
                OplusGridView.this.mCanDraw[i][j] = true;
            }
        });
        opacityAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.widget.OplusGridView.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animatior) {
                OplusGridView.this.mOpacity[i][j] = ((Integer) animatior.getAnimatedValue()).intValue();
                OplusGridView.this.invalidate();
            }
        });
        opacityAnimation.setDuration(150L);
        opacityAnimation.setInterpolator(new LinearInterpolator());
        int delay = (int) (((position - ((Math.floor(position + 1) * 2.0d) / 4.0d)) * 30.0d) + 100.0d);
        opacityAnimation.setStartDelay(delay);
        return opacityAnimation;
    }

    private ValueAnimator getScaleAnim(final int i, final int j, int position) {
        ValueAnimator scaleAnimation = ValueAnimator.ofFloat(0.0f, 1.0f);
        scaleAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.oplus.widget.OplusGridView.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
                OplusGridView.this.mCanDraw[i][j] = true;
            }
        });
        scaleAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.widget.OplusGridView.4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animatior) {
                OplusGridView.this.mScale[i][j] = (Float) animatior.getAnimatedValue();
                OplusGridView.this.invalidate();
            }
        });
        scaleAnimation.setDuration(300L);
        scaleAnimation.setInterpolator(POLATOR);
        int delay = (int) (((position - ((Math.floor(position + 1) * 2.0d) / 4.0d)) * 30.0d) + 100.0d);
        scaleAnimation.setStartDelay(delay);
        Log.d(TAG, "getScaleAnim : " + i + ", " + j + ", position : " + position + ", delay : " + delay);
        return scaleAnimation;
    }

    private ValueAnimator getMoreIconAnim() {
        ValueAnimator animation = ValueAnimator.ofInt(255, 0);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.widget.OplusGridView.5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animatior) {
                OplusGridView.this.mMoreIconAlpha = ((Integer) animatior.getAnimatedValue()).intValue();
                OplusGridView.this.invalidate();
            }
        });
        animation.setDuration(150L);
        animation.setInterpolator(new LinearInterpolator());
        return animation;
    }

    private void drawText(Canvas canvas, String str, int i, int j) {
        if (!isChinese(str)) {
            handleTooLongAppNameStr(str, false, this.mAppNameSize);
            drawTextExp(canvas, str, i, j);
            return;
        }
        int drawLineWidth = this.mChineseLength;
        if (!needFullSpaceForChinese(str)) {
            handleTooLongAppNameStr(str, true, this.mAppNameSize);
        } else {
            drawLineWidth = this.mEnglishLength;
        }
        String string = this.mAppInfos[i][j].getText();
        int breakIndex = this.mPaint1.breakText(string, true, drawLineWidth, null);
        if (breakIndex == string.length()) {
            if (getLayoutDirection() == 1) {
                canvas.drawText(str, (getWidth() - (((float) (j + 0.5d)) * this.mItemWidth)) - this.mTextPaddingLeft, ((i + 1) * this.mItemHeight) - this.mTextPaddingBottom, this.mPaint1);
                return;
            } else {
                canvas.drawText(str, (((float) (j + 0.5d)) * this.mItemWidth) + this.mTextPaddingLeft, ((i + 1) * this.mItemHeight) - this.mTextPaddingBottom, this.mPaint1);
                return;
            }
        }
        if (getLayoutDirection() == 1) {
            canvas.drawText(str.substring(0, breakIndex), (getWidth() - (((float) (j + 0.5d)) * this.mItemWidth)) - this.mTextPaddingLeft, ((i + 1) * this.mItemHeight) - this.mTextPaddingBottom, this.mPaint1);
        } else {
            canvas.drawText(str.substring(0, breakIndex), (((float) (j + 0.5d)) * this.mItemWidth) + this.mTextPaddingLeft, ((i + 1) * this.mItemHeight) - this.mTextPaddingBottom, this.mPaint1);
        }
        String stringLine2Old = str.substring(breakIndex);
        int breakIndex2 = this.mPaint1.breakText(stringLine2Old, true, drawLineWidth, null);
        Paint.FontMetricsInt fmi = this.mPaint1.getFontMetricsInt();
        int textLineHeight = fmi.descent - fmi.ascent;
        String stringLine2New = breakIndex2 == stringLine2Old.length() ? stringLine2Old : stringLine2Old.substring(0, breakIndex2) + "...";
        if (getLayoutDirection() == 1) {
            canvas.drawText(stringLine2New, (getWidth() - (((float) (j + 0.5d)) * this.mItemWidth)) - this.mTextPaddingLeft, (((i + 1) * this.mItemHeight) - this.mTextPaddingBottom) + textLineHeight, this.mPaint1);
        } else {
            canvas.drawText(stringLine2New, (((float) (j + 0.5d)) * this.mItemWidth) + this.mTextPaddingLeft, (((i + 1) * this.mItemHeight) - this.mTextPaddingBottom) + textLineHeight, this.mPaint1);
        }
    }

    private void drawTextExp(Canvas canvas, String str, int i, int j) {
        String line2;
        int breakIndex1 = this.mPaint1.breakText(str, true, this.mEnglishLength, null);
        if (breakIndex1 == str.length()) {
            if (getLayoutDirection() == 1) {
                canvas.drawText(str, (getWidth() - (((float) (j + 0.5d)) * this.mItemWidth)) - this.mTextPaddingLeft, ((i + 1) * this.mItemHeight) - this.mTextPaddingBottom, this.mPaint1);
                return;
            } else {
                canvas.drawText(str, (((float) (j + 0.5d)) * this.mItemWidth) + this.mTextPaddingLeft, ((i + 1) * this.mItemHeight) - this.mTextPaddingBottom, this.mPaint1);
                return;
            }
        }
        String line1 = str.substring(0, breakIndex1);
        int index = line1.lastIndexOf(32);
        Paint.FontMetricsInt fmi = this.mPaint1.getFontMetricsInt();
        int textLineHeight = fmi.descent - fmi.ascent;
        if (index > 0) {
            line1 = str.substring(0, index);
            String line22 = str.substring(index);
            int breakIndex2 = this.mPaint1.breakText(line22, true, this.mEnglishLength, null);
            line2 = breakIndex2 == line22.length() ? line22 : line22.substring(0, breakIndex2) + "...";
        } else {
            String line23 = str.substring(breakIndex1);
            int breakIndex22 = this.mPaint1.breakText(line23, true, this.mEnglishLength, null);
            line2 = breakIndex22 == line23.length() ? line23 : line23.substring(0, breakIndex22) + "...";
        }
        if (getLayoutDirection() == 1) {
            canvas.drawText(line1, (getWidth() - (((float) (j + 0.5d)) * this.mItemWidth)) - this.mTextPaddingLeft, ((i + 1) * this.mItemHeight) - this.mTextPaddingBottom, this.mPaint1);
            canvas.drawText(line2, (getWidth() - (((float) (j + 0.5d)) * this.mItemWidth)) - this.mTextPaddingLeft, (((i + 1) * this.mItemHeight) - this.mTextPaddingBottom) + textLineHeight, this.mPaint1);
        } else {
            canvas.drawText(line1, (((float) (j + 0.5d)) * this.mItemWidth) + this.mTextPaddingLeft, ((i + 1) * this.mItemHeight) - this.mTextPaddingBottom, this.mPaint1);
            canvas.drawText(line2, (((float) (j + 0.5d)) * this.mItemWidth) + this.mTextPaddingLeft, (((i + 1) * this.mItemHeight) - this.mTextPaddingBottom) + textLineHeight, this.mPaint1);
        }
    }

    private boolean needFullSpaceForChinese(String str) {
        this.mPaint1.setTextSize(this.mAppNameSize);
        int firstLinebreakIndex = this.mPaint1.breakText(str, true, this.mChineseLength, null);
        if (firstLinebreakIndex < str.length()) {
            String stringLine2Old = str.substring(firstLinebreakIndex);
            int secondLineBreakIndex = this.mPaint1.breakText(stringLine2Old, true, this.mChineseLength, null);
            if (secondLineBreakIndex < stringLine2Old.length()) {
                String stringLine2Old2 = str.substring(this.mPaint1.breakText(str, true, this.mEnglishLength, null));
                int secondLineBreakIndex2 = this.mPaint1.breakText(stringLine2Old2, true, this.mEnglishLength, null);
                int secondLineBreakIndex3 = stringLine2Old2.length();
                return secondLineBreakIndex2 == secondLineBreakIndex3;
            }
            return false;
        }
        return false;
    }

    private void handleTooLongAppNameStr(String str, boolean isChinese, float fontSize) {
        String line2;
        int breakIndex2;
        this.mPaint1.setTextSize(fontSize);
        if (Math.abs(fontSize - (this.mAppNameSize * APPNAME_TEXT_SECOND_SCALE_MULTIPLIER)) < 0.01d) {
            return;
        }
        int breakIndex1 = this.mPaint1.breakText(str, true, isChinese ? this.mChineseLength : this.mEnglishLength, null);
        if (breakIndex1 < str.length()) {
            if (!isChinese) {
                String line1 = str.substring(0, breakIndex1);
                int index = line1.lastIndexOf(32);
                if (index > 0) {
                    line2 = str.substring(index);
                    breakIndex2 = this.mPaint1.breakText(line2, true, this.mEnglishLength, null);
                } else {
                    line2 = str.substring(breakIndex1);
                    breakIndex2 = this.mPaint1.breakText(line2, true, this.mEnglishLength, null);
                }
            } else {
                line2 = str.substring(breakIndex1);
                breakIndex2 = this.mPaint1.breakText(line2, true, this.mChineseLength, null);
            }
            if (breakIndex2 < line2.length()) {
                handleTooLongAppNameStr(str, isChinese, APPNAME_TEXT_FIRST_SCALEMULTIPLIER * fontSize);
            }
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return true;
        }
        event.getPointerCount();
        this.mGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case 0:
                this.mPrivateFlags |= 33554432;
                performTouchStartAnim();
                return true;
            case 1:
                boolean prepressed = (this.mPrivateFlags & 33554432) == 0;
                if (prepressed) {
                    this.isSelected = true;
                    invalidate(this.selRect);
                } else {
                    this.isSelected = false;
                    invalidate(this.selRect);
                }
                postDelayed(this.mCancelclickRunnable, ViewConfiguration.getTapTimeout());
                this.selRect = new Rect();
                performTouchEndAnim();
                return true;
            case 2:
            default:
                return true;
            case 3:
                this.isSelected = false;
                invalidate(this.selRect);
                this.mPrivateFlags |= -33554433;
                removeCallbacks(this.mOnclickRunnable);
                this.selRect = new Rect();
                performTouchEndAnim();
                return true;
        }
    }

    public void initGetureDetecor() {
        this.mGestureDetector = new GestureDetector(this.mContext, new GestureDetector.OnGestureListener() { // from class: com.oplus.widget.OplusGridView.8
            float downX;
            float downY;
            int position = -1;

            @Override // android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent e) {
                this.downX = e.getX();
                this.downY = e.getY();
                if (OplusGridView.this.isLayoutRtl()) {
                    Log.d("View", "GestureDetector --> ondown getwidth = " + OplusGridView.this.getWidth() + " --> downX = " + this.downX);
                    this.position = (((int) (this.downY / OplusGridView.this.mItemHeight)) * OplusGridView.this.mColumnCounts) + ((int) ((OplusGridView.this.getWidth() - this.downX) / OplusGridView.this.mItemWidth));
                    OplusGridView.this.select((int) ((r0.getWidth() - this.downX) / OplusGridView.this.mItemWidth), (int) (this.downY / OplusGridView.this.mItemHeight), OplusGridView.this.selRect);
                    if (OplusGridView.this.selRect.contains((int) this.downX, (int) this.downY)) {
                        OplusGridView.this.isSelected = true;
                    }
                } else {
                    OplusGridView.this.select((int) (this.downX / r0.mItemWidth), (int) (this.downY / OplusGridView.this.mItemHeight), OplusGridView.this.selRect);
                    Configuration cfg = OplusGridView.this.mContext.getResources().getConfiguration();
                    OplusGridView.this.isSelected = false;
                    if (cfg.orientation == 2 && OplusGridView.this.selRect.contains((int) this.downX, (int) this.downY)) {
                        OplusGridView.this.isSelected = true;
                        this.position = (((int) (this.downY / OplusGridView.this.mItemHeight)) * OplusGridView.this.mColumnCounts) + ((int) (this.downX / OplusGridView.this.mItemWidth));
                    } else if (cfg.orientation == 1) {
                        OplusGridView.this.isSelected = true;
                        this.position = (((int) (this.downY / OplusGridView.this.mItemHeight)) * OplusGridView.this.mColumnCounts) + ((int) (this.downX / OplusGridView.this.mItemWidth));
                    }
                }
                OplusGridView.this.mCurrentPosition = this.position;
                OplusGridView oplusGridView = OplusGridView.this;
                oplusGridView.postDelayed(oplusGridView.mOnclickRunnable, ViewConfiguration.getTapTimeout());
                return true;
            }

            @Override // android.view.GestureDetector.OnGestureListener
            public void onShowPress(MotionEvent e) {
            }

            @Override // android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent e) {
                OplusGridView.this.click(this.position, false);
                return false;
            }

            @Override // android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (OplusGridView.this.isSelected) {
                    OplusGridView.this.isSelected = false;
                    OplusGridView oplusGridView = OplusGridView.this;
                    oplusGridView.invalidate(oplusGridView.selRect);
                }
                return false;
            }

            @Override // android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent e) {
                OplusGridView.this.click(this.position, true);
            }

            @Override // android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (OplusGridView.this.isSelected) {
                    OplusGridView.this.isSelected = false;
                    OplusGridView oplusGridView = OplusGridView.this;
                    oplusGridView.invalidate(oplusGridView.selRect);
                }
                return false;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void click(int position, boolean isLongClick) {
        Log.i(TAG, "Item click :position = " + position + "; isLongClick = " + isLongClick);
        if (position < this.mItemCounts && position >= 0) {
            int i = this.mPagerSize;
            int i2 = this.mPageNumber;
            if (((i2 - 1) * i) + position >= 0) {
                if (!isLongClick) {
                    this.mOnItemClickListener.OnItemClick((i * (i2 - 1)) + position);
                    this.mTouchHelper.sendEventForVirtualView(position, 1);
                } else {
                    this.mOnItemClickListener.OnItemLongClick((i * (i2 - 1)) + position);
                }
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int measureSpec) {
        int result = this.mTotalHeight;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        View.MeasureSpec.getSize(measureSpec);
        if (specMode == 1073741824) {
            int result2 = this.mTotalHeight;
            return result2;
        }
        if (specMode == Integer.MIN_VALUE) {
            int result3 = this.mTotalHeight;
            return result3;
        }
        return result;
    }

    private int measureWidth(int measureSpec) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        if (specMode != 1073741824) {
            return 0;
        }
        return specSize;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getRect(int x, int y, Rect rect) {
        int left;
        if (isLayoutRtl()) {
            left = getWidth() - (Math.min(x + 1, this.mColumnCounts) * this.mItemWidth);
        } else {
            int left2 = this.mItemWidth;
            left = left2 * x;
        }
        int top = (this.mItemHeight * y) + this.mPaddingTop;
        int right = this.mItemWidth + left;
        int bottom = this.mSelectHeight + top;
        rect.set(left, top, right, bottom);
    }

    private void getRect2(int x, int y, Rect rect, int position) {
        int left;
        int top;
        int bottom;
        int right;
        if (position == this.mCurrentPosition) {
            left = (x * this.mItemWidth) + ((int) (((r0 - this.mCurrentIconWidth) * 1.0f) / 2.0f));
            if (isLayoutRtl()) {
                if (this.mIsLandscape) {
                    int width = getWidth();
                    int min = Math.min(x + 1, this.mColumnCounts);
                    int i = this.mItemWidth;
                    left = (width - (min * i)) + ((i - this.mPaddingLeft) - this.mCurrentIconWidth);
                } else {
                    left = (getWidth() - (Math.min(x + 1, this.mColumnCounts) * this.mItemWidth)) + this.mPaddingLeft;
                }
            }
            int i2 = (this.mItemHeight * y) + this.mAppIconMarginTop;
            int i3 = this.mCurrentIconWidth;
            top = i2 - ((int) (((i3 - this.mIconWidth) * 1.0f) / 2.0f));
            right = left + i3;
            bottom = i3 + top;
        } else {
            int top2 = this.mItemWidth;
            left = (x * top2) + ((int) (((top2 - this.mIconWidth) * 1.0f) / 2.0f));
            if (isLayoutRtl()) {
                if (this.mIsLandscape) {
                    int width2 = getWidth();
                    int min2 = Math.min(x + 1, this.mColumnCounts);
                    int i4 = this.mItemWidth;
                    left = (width2 - (min2 * i4)) + ((i4 - this.mPaddingLeft) - this.mIconWidth);
                } else {
                    int left2 = getWidth();
                    left = (left2 - (Math.min(x + 1, this.mColumnCounts) * this.mItemWidth)) + this.mPaddingLeft;
                }
            }
            top = (this.mItemHeight * y) + this.mAppIconMarginTop;
            int i5 = this.mIconWidth;
            int right2 = left + i5;
            bottom = top + i5;
            right = right2;
        }
        rect.set(left, top, right, bottom);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void select(int x, int y, Rect rect) {
        this.selectX = Math.min(x, this.mColumnCounts - 1);
        int min = Math.min(y, this.mRowCounts - 1);
        this.selectY = min;
        int i = this.mColumnCounts * min;
        int i2 = this.selectX;
        int position = i + i2;
        if (position < this.mItemCounts) {
            getRect(i2, min, rect);
        }
    }

    public void setPageCount(int pagecount) {
        this.mPageNumber = pagecount;
    }

    public void setPagerSize(int size) {
        this.mPagerSize = size;
    }

    public void setOnItemClickListener(OnItemClickListener e) {
        this.mOnItemClickListener = e;
    }

    public void setAppInfo(OplusItem[][] AppInfos) {
        this.mAppInfos = AppInfos;
        this.mRowCounts = AppInfos.length;
        this.mItemCounts = get2DimenArrayCounts(AppInfos);
        this.mTotalHeight = (this.mItemHeight * this.mRowCounts) + this.mPaddingTop;
        this.mMoreIcon.setAlpha(255);
        this.mNeedExpandAnim = false;
        this.mTouchHelper.invalidateRoot();
        Log.d(TAG, "mTotalHeight = " + this.mTotalHeight);
    }

    public OplusItem[][] getAppInfo() {
        return this.mAppInfos;
    }

    public static String trancateText(String msg, int maxWordsPerLine) {
        String[] lines = msg.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            if (line.length() > maxWordsPerLine) {
                sb.append(line.subSequence(0, maxWordsPerLine - 1));
                sb.append("...\n");
            } else {
                sb.append(line);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public static int get2DimenArrayCounts(OplusItem[][] AppInfos) {
        int counts = 0;
        for (int i = 0; i < AppInfos.length; i++) {
            for (int j = 0; j < AppInfos[i].length; j++) {
                if (AppInfos[i][j] != null) {
                    counts++;
                }
            }
        }
        return counts;
    }

    public static int getNum(String text) {
        int result = 0;
        int english = 0;
        for (int i = 0; i < text.length(); i++) {
            String b = Character.toString(text.charAt(i));
            if (b.matches("^[一-龥]{1}$")) {
                result++;
            }
            char c = text.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                english++;
            }
        }
        return (int) (result + Math.ceil(english / 2.0d));
    }

    public static boolean isChinese(String text) {
        int chinese = 0;
        for (int i = 0; i < text.length(); i++) {
            String b = Character.toString(text.charAt(i));
            if (b.matches("^[一-龥]{1}$")) {
                chinese++;
            }
        }
        if (chinese > 0) {
            return true;
        }
        return false;
    }

    @Override // android.view.View
    protected boolean dispatchHoverEvent(MotionEvent event) {
        if (this.mTouchHelper.dispatchHoverEvent(event)) {
            return true;
        }
        return super.dispatchHoverEvent(event);
    }

    OplusItem getAppinfo(int position) {
        OplusItem[][] oplusItemArr = this.mAppInfos;
        if (oplusItemArr == null || position <= -1 || position >= get2DimenArrayCounts(oplusItemArr)) {
            return null;
        }
        OplusItem[][] oplusItemArr2 = this.mAppInfos;
        OplusItem[] oplusItemArr3 = oplusItemArr2[0];
        OplusItem oplusItem = oplusItemArr2[position / oplusItemArr3.length][position % oplusItemArr3.length];
        return oplusItem;
    }

    OplusItem getAccessibilityFocus() {
        int position = this.mTouchHelper.getFocusedVirtualView();
        if (position < 0) {
            return null;
        }
        OplusItem drawItem = getAppinfo(position);
        return drawItem;
    }

    public void clearAccessibilityFocus() {
        OplusViewTouchHelper oplusViewTouchHelper = this.mTouchHelper;
        if (oplusViewTouchHelper != null) {
            oplusViewTouchHelper.clearFocusedVirtualView();
        }
    }

    boolean restoreAccessibilityFocus(int position) {
        if (position < 0 || position >= this.mItemCounts) {
            return false;
        }
        OplusViewTouchHelper oplusViewTouchHelper = this.mTouchHelper;
        if (oplusViewTouchHelper != null) {
            oplusViewTouchHelper.setFocusedVirtualView(position);
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CharSequence getItemDescription(int virtualViewId) {
        OplusItem drawItem;
        if (virtualViewId < this.mItemCounts && (drawItem = getAppinfo(virtualViewId)) != null && drawItem.getText() != null) {
            return drawItem.getText();
        }
        return getClass().getSimpleName();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class OplusViewTouchHelper extends ExploreByTouchHelper {
        private final Rect mTempRect;

        public OplusViewTouchHelper(View host) {
            super(host);
            this.mTempRect = new Rect();
        }

        public void setFocusedVirtualView(int virtualViewId) {
            getAccessibilityNodeProvider(OplusGridView.this).performAction(virtualViewId, 64, null);
        }

        public void clearFocusedVirtualView() {
            int focusedVirtualView = getFocusedVirtualView();
            if (focusedVirtualView != Integer.MIN_VALUE) {
                getAccessibilityNodeProvider(OplusGridView.this).performAction(focusedVirtualView, 128, null);
            }
        }

        protected int getVirtualViewAt(float x, float y) {
            int position;
            Log.d("View", "getVirtualViewAt --> ondown getwidth = " + OplusGridView.this.getWidth() + " --> downX = " + x);
            if (OplusGridView.this.isLayoutRtl()) {
                position = (((int) (y / OplusGridView.this.mItemHeight)) * OplusGridView.this.mColumnCounts) + ((int) ((OplusGridView.this.getWidth() - x) / OplusGridView.this.mItemWidth));
            } else {
                position = (((int) (y / OplusGridView.this.mItemHeight)) * OplusGridView.this.mColumnCounts) + ((int) (x / OplusGridView.this.mItemWidth));
            }
            if (position >= 0) {
                return position;
            }
            return Integer.MIN_VALUE;
        }

        protected void getVisibleVirtualViews(IntArray virtualViewIds) {
            for (int day = 0; day < OplusGridView.this.mItemCounts; day++) {
                virtualViewIds.add(day);
            }
        }

        protected void onPopulateEventForVirtualView(int virtualViewId, AccessibilityEvent event) {
            event.setContentDescription(OplusGridView.this.getItemDescription(virtualViewId));
        }

        protected void onPopulateNodeForVirtualView(int virtualViewId, AccessibilityNodeInfo node) {
            getItemBounds(virtualViewId, this.mTempRect);
            node.setContentDescription(OplusGridView.this.getItemDescription(virtualViewId));
            node.setBoundsInParent(this.mTempRect);
            node.addAction(16);
            if (virtualViewId == OplusGridView.this.mCurrentPosition) {
                node.setSelected(true);
            }
        }

        protected boolean onPerformActionForVirtualView(int virtualViewId, int action, Bundle arguments) {
            switch (action) {
                case 16:
                    OplusGridView.this.click(virtualViewId, false);
                    return true;
                default:
                    return false;
            }
        }

        private void getItemBounds(int position, Rect rect) {
            if (position >= 0 && position < OplusGridView.this.mItemCounts) {
                OplusGridView oplusGridView = OplusGridView.this;
                oplusGridView.getRect(position % oplusGridView.mAppInfos[0].length, position / OplusGridView.this.mAppInfos[0].length, rect);
            }
        }
    }

    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dpValue * scale) + 0.5f);
    }

    private void performTouchStartAnim() {
        PropertyValuesHolder widthHolder = PropertyValuesHolder.ofInt("widthHolder", this.mCurrentIconWidth, this.mTouchModeWidth);
        PropertyValuesHolder brightnessHolder = PropertyValuesHolder.ofFloat("brightnessHolder", this.mCurrentBrightness, TOUCH_MODE_BRIGHTNESS);
        ValueAnimator animator = ValueAnimator.ofPropertyValuesHolder(widthHolder, brightnessHolder);
        animator.setInterpolator(this.mTouchStartInterpolator);
        animator.setDuration(66L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.widget.OplusGridView.9
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                OplusGridView.this.mCurrentIconWidth = ((Integer) animation.getAnimatedValue("widthHolder")).intValue();
                OplusGridView.this.mCurrentBrightness = ((Float) animation.getAnimatedValue("brightnessHolder")).floatValue();
                OplusGridView.this.invalidate();
            }
        });
        animator.start();
    }

    private void performTouchEndAnim() {
        Log.i(TAG, "Item touched end,performTouchEndAnim.");
        PropertyValuesHolder widthHolder = PropertyValuesHolder.ofInt("widthHolder", this.mCurrentIconWidth, this.mIconWidth);
        PropertyValuesHolder brightnessHolder = PropertyValuesHolder.ofFloat("brightnessHolder", this.mCurrentBrightness, 1.0f);
        ValueAnimator animator = ValueAnimator.ofPropertyValuesHolder(widthHolder, brightnessHolder);
        animator.setInterpolator(this.mTouchEndInterpolator);
        animator.setDuration(300L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.widget.OplusGridView.10
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                OplusGridView.this.mCurrentIconWidth = ((Integer) animation.getAnimatedValue("widthHolder")).intValue();
                OplusGridView.this.mCurrentBrightness = ((Float) animation.getAnimatedValue("brightnessHolder")).floatValue();
                OplusGridView.this.invalidate();
            }
        });
        animator.start();
    }
}
