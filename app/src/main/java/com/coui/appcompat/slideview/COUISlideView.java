package com.coui.appcompat.slideview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.animation.PathInterpolatorCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.coui.appcompat.list.AbsListViewNative;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$drawable;
import com.support.appcompat.R$string;
import com.support.list.R$color;
import com.support.list.R$dimen;
import com.support.list.R$styleable;
import java.util.ArrayList;
import java.util.List;
import m1.COUILinearInterpolator;
import m1.COUIPhysicalAnimationUtil;
import m2.COUIShapePath;
import s.DynamicAnimation;
import s.SpringAnimation;
import s.SpringForce;
import v1.COUIContextUtil;
import z2.COUIChangeTextUtil;

/* loaded from: classes.dex */
public class COUISlideView extends LinearLayout {
    private static final int ANIM_DURATION = 200;
    private static final int APPEAR_DURATION = 150;
    private static final int BIT_NUMBER_24 = 24;
    private static final int BIT_NUMBER_32 = 32;
    private static final int COLOR_MASK = 16777215;
    private static final float DAMPING_1 = 0.42857143f;
    private static final float DAMPING_2 = 0.5714286f;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_270 = 270;
    private static final int DEGREE_360 = 360;
    private static final int DEGREE_90 = 90;
    private static final int DELETAY_VALUE = 4;
    private static final int DISAPPEAR_DURATION = 367;
    private static final int EIGHT = 8;
    private static final int FADE_ANIM_DURATION = 210;
    private static final int FOUR = 4;
    private static final int INVALID_POINTER = -1;
    private static final int LISTVIEW_TOUCH_MODE_SCROLL = 3;
    public static final int MENU_ITEM_NORMAL_RECT_STYLE = 0;
    public static final int MENU_ITEM_ROUND_RECT_STYLE = 1;
    private static final float ONE = 1.0f;
    private static final int ONLY_ONE_ITEM_BACKGROUND_COLOR = 1;
    private static final float POINT_133 = 0.133f;
    private static final float POINT_EIGHT = 0.8f;
    private static final float POINT_FIVE = 0.5f;
    private static final float POINT_THREE = 0.3f;
    private static final int SEVEN = 7;
    private static final int SIX = 6;
    private static final int STATE_BACKGROUND_APPEAR = 1;
    private static final int STATE_BACKGROUND_DISAPPEAR = 2;
    private static final String TAG = "COUISlideView";
    private static final int THREE = 3;
    private static final int VELOCITY_SCALE = 1000;
    private static final int VERTICAL_LINE_WIDTH = 1;
    private static final float ZERO = 0.0f;
    private static Rect sTempRect = new Rect();
    private AccessibilityTouchHelper mAccessibilityTouchHelper;
    private int mActivePointerId;
    private int mAlpha;
    private Interpolator mAppearInterpolator;
    private Paint mBackGroundPaint;
    private ValueAnimator mBackgroundAppearAnimator;
    private ValueAnimator mBackgroundDisappearAnimator;
    private int mBackgroundPadding;
    private boolean mCanCopy;
    private boolean mCanDelete;
    private boolean mCanRename;
    private Context mContext;
    private int mCurColor;
    private int mCurrStatus;
    private int mCurrentTranslateX;
    private boolean mDisableBackgroundAnimator;
    private Interpolator mDisappearInterpolator;
    private Drawable mDiver;
    private boolean mDiverEnable;
    private boolean mDrawItemEnable;
    private boolean mEnableFastDelete;
    private ValueAnimator mFadeAnim;
    private int mFastDelHolderWidth;
    private int mGroupStyle;
    private int mHolderWidth;
    private int mIconCount;
    private int mInitialHeight;
    private int mInitialMotionX;
    private int mInitialMotionY;
    private Interpolator mInterpolator;
    private boolean mIsBeingDragged;
    private boolean mIsMenuRoundStyle;
    private boolean mIsUnableToDrag;
    private int mItemBackgroundColor;
    private int mItemCount;
    private ArrayList<COUISlideMenuItem> mItems;
    private List<Integer> mItemsBackgroundColors;
    private ArrayList<Rect> mItemsRect;
    private int mLastMotionX;
    private int mLastMotionY;
    private int mLastX;
    private int mLastY;
    private Layout mLayout;
    private Paint mLinePaint;
    private int mMaximumVelocity;
    private boolean mMenuDividerEnable;
    private boolean mNeedAutoStartDisAppear;
    private OnDeleteItemClickListener mOnDeleteItemClickListener;
    private OnSlideListener mOnSlideListener;
    private OnSlideMenuItemClickListener mOnSlideMenuItemClickListener;
    private OnSmoothScrollListener mOnSmoothScrollListener;
    private int mOverSlideDeleteSlop;
    private int mPaddingRight;
    private Paint mPaint;
    private Path mPath;
    private Path mPath1;
    private Path mPathArc;
    private int mQuickDeleteSlop;
    private int mRadius;
    int mRefreshStyle;
    private int mRoundRectMenuItemGap;
    private int mRoundRectMenuItemRadius;
    private int mRoundRectMenuLeftMargin;
    private int mRoundRectMenuRightMargin;
    private boolean mScrollAll;
    private Scroller mScroller;
    private int mSlideBackColor;
    private Drawable mSlideColorDrawable;
    private boolean mSlideDelete;
    private boolean mSlideDeleteInRoundMode;
    private boolean mSlideEnable;
    private int mSlideItemPadding;
    private int mSlideTextColor;
    private int mSlideTouchSlop;
    private View mSlideView;
    private Runnable mSmoothScrollRunnable;
    private SpringAnimation mSpringAnimation;
    private boolean mStartDeleteAnimation;
    private int mState;
    private String mStringDelete;
    private int mTargetTranslateX;
    private int mTextPadding;
    private boolean mTouchAllRound;
    private int mTouchSlop;
    private int mUpScrollX;
    private boolean mUseDefaultBackGround;
    private VelocityTracker mVelocityTracker;
    private LinearLayout mViewContent;
    private boolean mhasStartAnimation;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AccessibilityTouchHelper extends ExploreByTouchHelper {
        public AccessibilityTouchHelper(View view) {
            super(view);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected int getVirtualViewAt(float f10, float f11) {
            int i10 = (int) f10;
            int i11 = (int) f11;
            for (int i12 = 0; i12 < COUISlideView.this.mItemsRect.size(); i12++) {
                if (((Rect) COUISlideView.this.mItemsRect.get(i12)).contains(i10, i11)) {
                    return i12;
                }
            }
            return Integer.MIN_VALUE;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void getVisibleVirtualViews(List<Integer> list) {
            for (int i10 = 0; i10 < COUISlideView.this.mItems.size(); i10++) {
                list.add(Integer.valueOf(i10));
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected boolean onPerformActionForVirtualView(int i10, int i11, Bundle bundle) {
            if (i11 != 16) {
                return false;
            }
            if (i10 == 0 && COUISlideView.this.mOnSlideMenuItemClickListener == null) {
                COUISlideView cOUISlideView = COUISlideView.this;
                cOUISlideView.startDeleteAnimation(cOUISlideView.mSlideView);
                return true;
            }
            if (COUISlideView.this.mOnSlideMenuItemClickListener == null) {
                return true;
            }
            COUISlideView.this.mOnSlideMenuItemClickListener.onSlideMenuItemClick((COUISlideMenuItem) COUISlideView.this.mItems.get(i10), i10);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void onPopulateEventForVirtualView(int i10, AccessibilityEvent accessibilityEvent) {
            String str = (String) ((COUISlideMenuItem) COUISlideView.this.mItems.get(i10)).getText();
            if (str == null) {
                str = "菜单";
            }
            accessibilityEvent.setContentDescription(str);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onPopulateNodeForVirtualView(int i10, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (i10 < COUISlideView.this.mItemsRect.size()) {
                String str = (String) ((COUISlideMenuItem) COUISlideView.this.mItems.get(i10)).getText();
                if (str == null) {
                    str = "菜单";
                }
                accessibilityNodeInfoCompat.Z(str);
                accessibilityNodeInfoCompat.Q((Rect) COUISlideView.this.mItemsRect.get(i10));
                accessibilityNodeInfoCompat.a(16);
                return;
            }
            accessibilityNodeInfoCompat.Z("");
            accessibilityNodeInfoCompat.Q(new Rect());
            accessibilityNodeInfoCompat.a(16);
        }
    }

    /* loaded from: classes.dex */
    public interface OnDeleteItemClickListener {
        void onDeleteItemClick();
    }

    /* loaded from: classes.dex */
    public interface OnSlideListener {
        public static final int SLIDE_STATUS_OFF = 0;
        public static final int SLIDE_STATUS_ON = 2;
        public static final int SLIDE_STATUS_START_SCROLL = 1;

        void onSlide(View view, int i10);
    }

    /* loaded from: classes.dex */
    public interface OnSlideMenuItemClickListener {
        void onSlideMenuItemClick(COUISlideMenuItem cOUISlideMenuItem, int i10);
    }

    /* loaded from: classes.dex */
    public interface OnSmoothScrollListener {
        void onSmoothScroll(View view);
    }

    public COUISlideView(Context context) {
        this(context, null);
    }

    private RectF checkRect(RectF rectF) {
        float f10 = rectF.left;
        float f11 = rectF.right;
        if (f10 > f11) {
            rectF.left = f11;
            rectF.right = f10;
        }
        return rectF;
    }

    private void clipBottomRound(Canvas canvas) {
        Path path = this.mPath1;
        if (path == null) {
            this.mPath1 = new Path();
        } else {
            path.reset();
        }
        if (isLayoutRtl()) {
            this.mPath1.moveTo(0.0f, getHeight() - (this.mRadius / 2));
            this.mPath1.lineTo(0.0f, getHeight());
            this.mPath1.lineTo(this.mRadius / 2, getHeight());
        } else {
            this.mPath1.moveTo(this.mHolderWidth, getHeight() - (this.mRadius / 2));
            this.mPath1.lineTo(this.mHolderWidth, getHeight());
            this.mPath1.lineTo(this.mHolderWidth - (this.mRadius / 2), getHeight());
        }
        this.mPath1.close();
        canvas.clipPath(this.mPath1, Region.Op.DIFFERENCE);
        Path path2 = this.mPathArc;
        if (path2 == null) {
            this.mPathArc = new Path();
        } else {
            path2.reset();
        }
        if (isLayoutRtl()) {
            int height = getHeight();
            this.mPathArc.addArc(new RectF(0.0f, height - r4, this.mRadius, getHeight()), 90.0f, 180.0f);
        } else {
            this.mPathArc.addArc(new RectF(this.mHolderWidth - this.mRadius, getHeight() - this.mRadius, this.mHolderWidth, getHeight()), 0.0f, 90.0f);
        }
        canvas.clipPath(this.mPathArc, Region.Op.UNION);
    }

    private void clipTopRound(Canvas canvas) {
        Path path = this.mPath1;
        if (path == null) {
            this.mPath1 = new Path();
        } else {
            path.reset();
        }
        if (isLayoutRtl()) {
            this.mPath1.moveTo(this.mRadius / 2, 0.0f);
            this.mPath1.lineTo(0.0f, 0.0f);
            this.mPath1.lineTo(0.0f, this.mRadius / 2);
        } else {
            this.mPath1.moveTo(this.mHolderWidth, this.mRadius / 2);
            this.mPath1.lineTo(this.mHolderWidth, 0.0f);
            this.mPath1.lineTo(this.mHolderWidth - (this.mRadius / 2), 0.0f);
        }
        this.mPath1.close();
        canvas.clipPath(this.mPath1, Region.Op.DIFFERENCE);
        Path path2 = this.mPathArc;
        if (path2 == null) {
            this.mPathArc = new Path();
        } else {
            path2.reset();
        }
        if (isLayoutRtl()) {
            int i10 = this.mRadius;
            this.mPathArc.addArc(new RectF(0.0f, 0.0f, i10, i10), -90.0f, -180.0f);
        } else {
            this.mPathArc.addArc(new RectF(r3 - r4, 0.0f, this.mHolderWidth, this.mRadius), 0.0f, -90.0f);
        }
        canvas.clipPath(this.mPathArc, Region.Op.UNION);
    }

    private void drawBackGround(Canvas canvas) {
        boolean z10;
        boolean z11;
        if (this.mIsMenuRoundStyle) {
            this.mBackGroundPaint.setColor(this.mCurColor);
            RectF rectF = new RectF((isLayoutRtl() || this.mTouchAllRound) ? (-this.mBackgroundPadding) - getSlideViewScrollX() : 0, 0.0f, (!isLayoutRtl() || this.mTouchAllRound) ? (getWidth() + this.mBackgroundPadding) - getSlideViewScrollX() : getWidth(), getHeight());
            boolean isLayoutRtl = isLayoutRtl();
            boolean z12 = !isLayoutRtl();
            if (this.mTouchAllRound) {
                z10 = true;
                z11 = true;
            } else {
                z10 = isLayoutRtl;
                z11 = z12;
            }
            Path b10 = COUIShapePath.b(this.mPath, rectF, Math.min(getHeight() / 2, this.mRoundRectMenuItemRadius), z10, z11, z10, z11);
            this.mPath = b10;
            canvas.drawPath(b10, this.mBackGroundPaint);
        }
    }

    private void drawDiver(Canvas canvas) {
        canvas.save();
        this.mDiver.setBounds(0, getHeight() - this.mDiver.getIntrinsicHeight(), getWidth(), getHeight());
        this.mDiver.draw(canvas);
        canvas.restore();
    }

    private void drawItemBackground(Canvas canvas) {
        int i10;
        int i11;
        boolean z10;
        float f10;
        int i12;
        float f11;
        int i13;
        int i14;
        if (this.mItemCount <= 0) {
            return;
        }
        canvas.save();
        int i15 = this.mAlpha;
        if (i15 > 0) {
            canvas.drawColor((i15 << 24) | this.mSlideBackColor);
        }
        int i16 = 1;
        int i17 = isLayoutRtl() ? -1 : 1;
        if (isLayoutRtl()) {
            canvas.translate(getWidth(), 0.0f);
        }
        if (this.mLayout == null) {
            this.mLayout = new StaticLayout(this.mStringDelete, (TextPaint) this.mPaint, this.mHolderWidth, Layout.Alignment.ALIGN_CENTER, ONE, 0.0f, false);
        }
        int unpackRangeStartFromLong = unpackRangeStartFromLong(getLineRangeForDraw(canvas));
        if (unpackRangeStartFromLong < 0) {
            canvas.restore();
            return;
        }
        Paint paint = new Paint();
        int i18 = this.mItemBackgroundColor;
        int i19 = this.mAlpha;
        if (i19 > 0) {
            paint.setColor((i18 & COLOR_MASK) | (i19 << 24));
        } else {
            paint.setColor(i18);
        }
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        int width = getWidth() - (getSlideViewScrollX() * i17);
        int i20 = 0;
        if (this.mItemsBackgroundColors.size() == 1) {
            RectF rectF = new RectF(width * i17, 0.0f, getWidth() * i17, getHeight());
            if (this.mIsMenuRoundStyle) {
                float f12 = rectF.left + (this.mRoundRectMenuLeftMargin * i17);
                rectF.left = f12;
                if (this.mSlideDeleteInRoundMode || this.mSlideDelete) {
                    rectF.right -= this.mRoundRectMenuRightMargin * i17;
                } else {
                    rectF.right = f12 + (this.mItems.get(0).getWidth() * i17);
                }
                Path a10 = COUIShapePath.a(this.mPath, checkRect(rectF), Math.min(getHeight() / 2, this.mRoundRectMenuItemRadius));
                this.mPath = a10;
                canvas.drawPath(a10, paint);
            } else {
                canvas.drawRect(rectF, paint);
            }
        }
        int lineTop = this.mLayout.getLineTop(unpackRangeStartFromLong + 1) - this.mLayout.getLineDescent(unpackRangeStartFromLong);
        Paint.FontMetrics fontMetrics = this.mPaint.getFontMetrics();
        int ceil = ((int) Math.ceil(fontMetrics.descent)) - ((int) Math.ceil(fontMetrics.ascent));
        int i21 = 0;
        while (i21 < this.mItemCount) {
            this.mItems.get(i21).getBackground();
            Drawable icon = this.mItems.get(i21).getIcon();
            int slideViewScrollX = (this.mIsMenuRoundStyle || getSlideViewScrollX() * i17 <= this.mHolderWidth || this.mhasStartAnimation) ? i20 : (getSlideViewScrollX() * i17) - this.mHolderWidth;
            int slideViewScrollX2 = (getSlideViewScrollX() * i17 <= this.mHolderWidth || !this.mhasStartAnimation) ? i20 : (getSlideViewScrollX() * i17) - this.mHolderWidth;
            if (this.mEnableFastDelete && this.mSlideDelete) {
                if (this.mItemCount + i16 == 0 || getWidth() - (this.mUpScrollX * i17) == 0) {
                    i10 = i20;
                } else {
                    int width2 = getWidth();
                    int i22 = this.mUpScrollX;
                    int i23 = this.mItemCount;
                    int i24 = this.mHolderWidth;
                    i10 = (width2 - (i22 * i17)) + (((i23 - i21) * ((i22 * i17) - i24)) / (i23 + 1)) + ((((((i23 - i21) * ((i22 * i17) - i24)) / (i23 + i16)) * (getSlideViewScrollX() - this.mUpScrollX)) * i17) / (getWidth() - (this.mUpScrollX * i17)));
                }
            } else {
                int width3 = getWidth() - (getSlideViewScrollX() * i17);
                int i25 = this.mItemCount;
                i10 = slideViewScrollX2 + width3 + (((i25 - i21) * slideViewScrollX) / (i25 + i16));
            }
            int i26 = i10 * i17;
            for (int i27 = this.mItemCount - i16; i27 > i21; i27--) {
                i26 += this.mItems.get(i27).getWidth() * i17;
            }
            int height = getHeight();
            int width4 = this.mItems.get(i21).getWidth() + i26;
            if (this.mItems.get(i21).getText() != null) {
                canvas.drawText((String) this.mItems.get(i21).getText(), ((this.mItems.get(i21).getWidth() * i17) / 2) + i26, (lineTop + (height / 2)) - (ceil / 2), this.mPaint);
            }
            if (this.mItemsRect.size() != this.mItems.size()) {
                this.mItemsRect = new ArrayList<>();
                for (int i28 = 0; i28 < this.mItems.size(); i28++) {
                    this.mItemsRect.add(i28, new Rect());
                }
            }
            if (this.mItemsRect.size() > 0) {
                this.mItemsRect.get(i21).set(i26, 0, width4, height);
            }
            if (icon != null) {
                if (this.mIsMenuRoundStyle) {
                    i26 += (this.mRoundRectMenuLeftMargin + (((this.mItemCount - 1) - i21) * this.mRoundRectMenuItemGap)) * i17;
                }
                int intrinsicWidth = icon.getIntrinsicWidth();
                int intrinsicHeight = icon.getIntrinsicHeight();
                int width5 = (((this.mItems.get(i21).getWidth() - intrinsicWidth) * i17) / 2) + i26;
                int i29 = (height - intrinsicHeight) / 2;
                int i30 = (intrinsicWidth * i17) + width5;
                if (width5 > i30) {
                    width5 = i30;
                    i30 = width5;
                }
                if (this.mItemsBackgroundColors.size() == 1 || this.mItemsBackgroundColors.size() != this.mItems.size() || i21 >= this.mItemsBackgroundColors.size()) {
                    i11 = lineTop;
                    z10 = false;
                } else {
                    paint.setColor(this.mItemsBackgroundColors.get(i21).intValue());
                    int width6 = (this.mItems.get(i21).getWidth() * i17) + i26;
                    float round = Math.round(slideViewScrollX / (this.mItemCount + ONE));
                    if (i21 == 0) {
                        i14 = (int) (i26 - ((round / 2.0f) * i17));
                        i13 = getWidth() * i17;
                    } else {
                        if (i21 == this.mItems.size() - 1) {
                            float f13 = i17;
                            i12 = (int) (i26 - (round * f13));
                            f11 = width6;
                            f10 = (round / 2.0f) * f13;
                        } else {
                            f10 = (round / 2.0f) * i17;
                            i12 = (int) (i26 - f10);
                            f11 = width6;
                        }
                        i13 = (int) (f11 + f10);
                        i14 = i12;
                    }
                    i11 = lineTop;
                    z10 = false;
                    RectF rectF2 = new RectF(i14, 0.0f, i13, getHeight());
                    if (this.mIsMenuRoundStyle) {
                        rectF2.right = rectF2.left + (this.mItems.get(i21).getWidth() * i17);
                        COUIShapePath.a(this.mPath, checkRect(rectF2), Math.min(getHeight() / 2, this.mRoundRectMenuItemRadius));
                        canvas.drawPath(this.mPath, paint);
                    } else {
                        canvas.drawRect(rectF2, paint);
                    }
                }
                icon.setBounds(width5, i29, i30, intrinsicHeight + i29);
                icon.draw(canvas);
            } else {
                i11 = lineTop;
                z10 = false;
            }
            i21++;
            lineTop = i11;
            i16 = 1;
            i20 = 0;
        }
        canvas.restore();
        if (ViewCompat.j(this) == null) {
            ViewCompat.l0(this, this.mAccessibilityTouchHelper);
            ViewCompat.w0(this, 1);
        }
    }

    private void endDrag() {
        recycleVelocityTracker();
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
    }

    private void initAnimation(Context context) {
        final int a10 = COUIContextUtil.a(context, R$attr.couiColorPressBackground);
        int alpha = Color.alpha(a10);
        ValueAnimator ofInt = ObjectAnimator.ofInt(0, alpha);
        this.mBackgroundAppearAnimator = ofInt;
        ofInt.setDuration(150L);
        this.mBackgroundAppearAnimator.setInterpolator(this.mAppearInterpolator);
        this.mBackgroundAppearAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.slideview.COUISlideView.3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                COUISlideView.this.mCurColor = Color.argb(((Integer) valueAnimator.getAnimatedValue()).intValue(), Color.red(a10), Color.green(a10), Color.blue(a10));
                COUISlideView.this.invalidate();
            }
        });
        this.mBackgroundAppearAnimator.addListener(new Animator.AnimatorListener() { // from class: com.coui.appcompat.slideview.COUISlideView.4
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                COUISlideView.this.mState = 1;
                if (COUISlideView.this.mNeedAutoStartDisAppear) {
                    COUISlideView.this.mNeedAutoStartDisAppear = false;
                    COUISlideView.this.mBackgroundDisappearAnimator.start();
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }
        });
        ValueAnimator ofInt2 = ObjectAnimator.ofInt(alpha, 0);
        this.mBackgroundDisappearAnimator = ofInt2;
        ofInt2.setDuration(367L);
        this.mBackgroundDisappearAnimator.setInterpolator(this.mDisappearInterpolator);
        this.mBackgroundDisappearAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.slideview.COUISlideView.5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                COUISlideView.this.mCurColor = Color.argb(((Integer) valueAnimator.getAnimatedValue()).intValue(), Color.red(a10), Color.green(a10), Color.blue(a10));
                COUISlideView.this.invalidate();
            }
        });
        this.mBackgroundDisappearAnimator.addListener(new Animator.AnimatorListener() { // from class: com.coui.appcompat.slideview.COUISlideView.6
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                COUISlideView.this.mState = 2;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }
        });
    }

    private void initOrResetVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void initView() {
        this.mContext = getContext();
        int e10 = (int) COUIChangeTextUtil.e(getResources().getDimensionPixelSize(R$dimen.coui_slide_view_text_size), getResources().getConfiguration().fontScale, 2);
        this.mSlideTouchSlop = getResources().getDimensionPixelSize(R$dimen.coui_slideview_touch_slop);
        this.mOverSlideDeleteSlop = getResources().getDimensionPixelSize(R$dimen.coui_slideview_over_slide_delete);
        this.mQuickDeleteSlop = getResources().getDimensionPixelSize(R$dimen.coui_slideview_quick_delete);
        TextPaint textPaint = new TextPaint();
        this.mPaint = textPaint;
        textPaint.setColor(this.mSlideTextColor);
        this.mPaint.setTextSize(e10);
        this.mTextPadding = this.mContext.getResources().getDimensionPixelSize(R$dimen.coui_slide_view_text_padding);
        this.mPaddingRight = this.mContext.getResources().getDimensionPixelSize(R$dimen.coui_slide_view_padding_right);
        this.mRadius = this.mContext.getResources().getDimensionPixelSize(R$dimen.coui_slideview_group_round_radius);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setTextAlign(Paint.Align.CENTER);
        this.mItems = new ArrayList<>();
        this.mItemsRect = new ArrayList<>();
        this.mAccessibilityTouchHelper = new AccessibilityTouchHelper(this);
        this.mMaximumVelocity = ViewConfiguration.get(this.mContext).getScaledMaximumFlingVelocity();
        setDeleteEnable(true);
        TextPaint textPaint2 = new TextPaint();
        this.mLinePaint = textPaint2;
        textPaint2.setStrokeWidth(ONE);
        this.mLinePaint.setColor(this.mContext.getResources().getColor(R$color.coui_slideview_delete_divider_color));
        this.mLinePaint.setAntiAlias(true);
        this.mDiver = getContext().getResources().getDrawable(R$drawable.coui_divider_horizontal_default);
        this.mInterpolator = PathInterpolatorCompat.a(POINT_133, 0.0f, POINT_THREE, ONE);
        this.mScroller = new Scroller(this.mContext, this.mInterpolator);
        setOrientation(0);
        setLayoutParams(new AbsListView.LayoutParams(-1, -1));
        itemWidthChange();
        this.mStringDelete = this.mContext.getString(R$string.coui_slide_delete);
        this.mSlideBackColor = this.mContext.getResources().getColor(R$color.coui_slideview_backcolor);
        ColorDrawable colorDrawable = new ColorDrawable(this.mSlideBackColor);
        this.mSlideColorDrawable = colorDrawable;
        this.mSlideBackColor &= COLOR_MASK;
        ObjectAnimator ofInt = ObjectAnimator.ofInt(colorDrawable, "Alpha", 0, 210);
        this.mFadeAnim = ofInt;
        ofInt.setInterpolator(this.mInterpolator);
        this.mFadeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.slideview.COUISlideView.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                COUISlideView.this.mAlpha = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            }
        });
        this.mSlideItemPadding = getResources().getDimensionPixelSize(R$dimen.coui_slide_view_item_padding);
        setWillNotDraw(false);
    }

    private void itemWidthChange() {
        int i10;
        int i11 = 0;
        this.mHolderWidth = 0;
        this.mItemCount = this.mItems.size();
        while (true) {
            i10 = this.mItemCount;
            if (i11 >= i10) {
                break;
            }
            this.mHolderWidth += this.mItems.get(i11).getWidth();
            i11++;
        }
        int i12 = this.mHolderWidth;
        this.mFastDelHolderWidth = i12;
        if (this.mIsMenuRoundStyle) {
            this.mHolderWidth = i12 + ((i10 - 1) * this.mRoundRectMenuItemGap) + this.mRoundRectMenuLeftMargin + this.mRoundRectMenuRightMargin;
        }
    }

    public static long packRangeInLong(int i10, int i11) {
        return i11 | (i10 << 32);
    }

    private void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void requestParentDisallowInterceptTouchEvent(boolean z10) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(z10);
        }
    }

    public static int unpackRangeStartFromLong(long j10) {
        return (int) (j10 >>> 32);
    }

    public void addColor(int i10) {
        addColor(-1, i10);
    }

    public void addItem(COUISlideMenuItem cOUISlideMenuItem) {
        addItem(-1, cOUISlideMenuItem);
    }

    public void animationScrollTo(int i10, int i11) {
        SpringAnimation v7 = new SpringAnimation(this.mSlideView, DynamicAnimation.f17879y).v(new SpringForce(i10).d(ONE).f(200.0f));
        this.mSpringAnimation = v7;
        v7.n();
        this.mSpringAnimation.a(new DynamicAnimation.q() { // from class: com.coui.appcompat.slideview.COUISlideView.10
            @Override // s.DynamicAnimation.q
            public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z10, float f10, float f11) {
            }
        });
    }

    public void cancelBackgroundAnimators() {
        if (this.mBackgroundDisappearAnimator.isRunning()) {
            this.mBackgroundDisappearAnimator.cancel();
        }
        if (this.mBackgroundAppearAnimator.isRunning()) {
            this.mNeedAutoStartDisAppear = false;
            this.mBackgroundAppearAnimator.cancel();
        }
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            if (this.mScrollAll) {
                scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            } else {
                this.mSlideView.scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            }
            postInvalidate();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        AccessibilityTouchHelper accessibilityTouchHelper = this.mAccessibilityTouchHelper;
        if (accessibilityTouchHelper == null || !accessibilityTouchHelper.dispatchHoverEvent(motionEvent)) {
            return super.dispatchHoverEvent(motionEvent);
        }
        return true;
    }

    public void enableFastDelete(boolean z10) {
        this.mEnableFastDelete = z10;
    }

    public View getContentView() {
        return this.mSlideView;
    }

    public CharSequence getDeleteItemText() {
        if (this.mCanDelete) {
            return this.mItems.get(0).getText();
        }
        return null;
    }

    public Drawable getDiver() {
        return this.mDiver;
    }

    public boolean getDiverEnable() {
        return this.mDiverEnable;
    }

    public boolean getDrawItemEnable() {
        return this.mDrawItemEnable;
    }

    public int getHolderWidth() {
        return this.mHolderWidth;
    }

    public int getLineForVertical(int i10) {
        int lineCount = this.mLayout.getLineCount();
        int i11 = -1;
        while (lineCount - i11 > 1) {
            int i12 = (lineCount + i11) / 2;
            if (this.mLayout.getLineTop(i12) > i10) {
                lineCount = i12;
            } else {
                i11 = i12;
            }
        }
        if (i11 < 0) {
            return 0;
        }
        return i11;
    }

    public long getLineRangeForDraw(Canvas canvas) {
        synchronized (sTempRect) {
            if (!canvas.getClipBounds(sTempRect)) {
                return packRangeInLong(0, -1);
            }
            Rect rect = sTempRect;
            int i10 = rect.top;
            int i11 = rect.bottom;
            int max = Math.max(i10, 0);
            Layout layout = this.mLayout;
            int min = Math.min(layout.getLineTop(layout.getLineCount()), i11);
            if (max >= min) {
                return packRangeInLong(0, -1);
            }
            return packRangeInLong(getLineForVertical(max), getLineForVertical(min));
        }
    }

    public Scroller getScroll() {
        return this.mScroller;
    }

    public boolean getSlideEnable() {
        return this.mSlideEnable;
    }

    public int getSlideViewScrollX() {
        if (this.mScrollAll) {
            return getScrollX();
        }
        return this.mSlideView.getScrollX();
    }

    @Override // android.view.View
    public boolean hasFocusable() {
        return getVisibility() == 0 && isFocusable();
    }

    public boolean isLayoutRtl() {
        return getLayoutDirection() == 1;
    }

    public boolean isSliding() {
        return this.mhasStartAnimation;
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mSlideEnable || this.mDrawItemEnable) {
            drawBackGround(canvas);
            drawItemBackground(canvas);
        }
        if (this.mDiverEnable) {
            drawDiver(canvas);
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int scrollX;
        int i10;
        int i11;
        int i12;
        if (!this.mSlideEnable) {
            return false;
        }
        int action = motionEvent.getAction() & 255;
        if (action != 3 && action != 1) {
            if (action != 0) {
                if (this.mIsBeingDragged) {
                    return true;
                }
                if (this.mIsUnableToDrag) {
                    return false;
                }
            }
            if (this.mScrollAll) {
                scrollX = getScrollX();
            } else {
                scrollX = this.mSlideView.getScrollX();
            }
            if (action == 0) {
                this.mActivePointerId = motionEvent.getPointerId(0);
                initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(motionEvent);
                int x10 = (int) motionEvent.getX();
                this.mLastMotionX = x10;
                this.mInitialMotionX = x10;
                int y4 = (int) motionEvent.getY();
                this.mLastMotionY = y4;
                this.mInitialMotionY = y4;
                this.mIsUnableToDrag = false;
                OnSlideListener onSlideListener = this.mOnSlideListener;
                if (onSlideListener != null) {
                    onSlideListener.onSlide(this, 1);
                }
            } else if (action == 2 && (i10 = this.mActivePointerId) != -1) {
                int findPointerIndex = motionEvent.findPointerIndex(i10);
                int x11 = (int) motionEvent.getX(findPointerIndex);
                int i13 = x11 - this.mLastMotionX;
                int abs = Math.abs(i13);
                int y10 = (int) motionEvent.getY(findPointerIndex);
                int abs2 = Math.abs(y10 - this.mInitialMotionY);
                this.mLastMotionX = x11;
                this.mLastMotionY = y10;
                int i14 = this.mTouchSlop;
                if (abs > i14 && abs * POINT_FIVE > abs2) {
                    this.mIsBeingDragged = true;
                    requestParentDisallowInterceptTouchEvent(true);
                    if (i13 > 0) {
                        i12 = this.mInitialMotionX + this.mTouchSlop;
                    } else {
                        i12 = this.mInitialMotionX - this.mTouchSlop;
                    }
                    this.mLastMotionX = i12;
                    this.mLastMotionY = y10;
                } else if (abs2 > i14) {
                    this.mIsUnableToDrag = true;
                }
                if (this.mIsBeingDragged) {
                    initVelocityTrackerIfNotExists();
                    this.mVelocityTracker.addMovement(motionEvent);
                    if (Math.abs(scrollX) < this.mHolderWidth && this.mItemCount != 1) {
                        i11 = (i13 * 4) / 7;
                    } else {
                        i11 = (i13 * 3) / 7;
                    }
                    int i15 = scrollX - i11;
                    if ((getLayoutDirection() != 1 && i15 < 0) || (getLayoutDirection() == 1 && i15 > 0)) {
                        i15 = 0;
                    } else if (Math.abs(i15) > this.mHolderWidth) {
                        i15 = getLayoutDirection() == 1 ? -this.mHolderWidth : this.mHolderWidth;
                    }
                    if (this.mScrollAll) {
                        scrollTo(i15, 0);
                    } else {
                        this.mSlideView.scrollTo(i15, 0);
                    }
                }
            }
            return this.mIsBeingDragged;
        }
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        this.mActivePointerId = -1;
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:120:0x02ca, code lost:
    
        if (r0 < r4) goto L178;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x02cc, code lost:
    
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0032, code lost:
    
        if (r0 < (getWidth() - getSlideViewScrollX())) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x02e2, code lost:
    
        if (r0 > (getWidth() - r14.mHolderWidth)) goto L178;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:166:0x03a6  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x03b5  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x029e  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x02a8  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x02af  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int scrollX;
        int i10;
        int i11;
        int i12;
        OnSlideListener onSlideListener;
        boolean z10;
        boolean z11;
        ViewParent parent;
        int i13;
        int i14;
        int actionMasked = motionEvent.getActionMasked();
        boolean z12 = false;
        if (!this.mSlideEnable) {
            if (this.mDrawItemEnable && !this.mhasStartAnimation) {
                if (actionMasked == 0) {
                    float x10 = motionEvent.getX();
                    if (isLayoutRtl()) {
                        if (x10 > this.mHolderWidth) {
                            return false;
                        }
                    }
                }
            }
            return false;
        }
        int x11 = (int) motionEvent.getX();
        int y4 = (int) motionEvent.getY();
        if (this.mScrollAll) {
            scrollX = getScrollX();
        } else {
            scrollX = this.mSlideView.getScrollX();
        }
        initVelocityTrackerIfNotExists();
        int action = motionEvent.getAction();
        if (action == 0) {
            startAppearAnimation();
            if (this.mhasStartAnimation) {
                return false;
            }
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
            }
            OnSlideListener onSlideListener2 = this.mOnSlideListener;
            if (onSlideListener2 != null) {
                onSlideListener2.onSlide(this, 1);
            }
            this.mActivePointerId = motionEvent.getPointerId(0);
            int x12 = (int) motionEvent.getX();
            this.mLastMotionX = x12;
            this.mInitialMotionX = x12;
            int y10 = (int) motionEvent.getY();
            this.mLastMotionY = y10;
            this.mInitialMotionY = y10;
            ViewParent parent2 = getParent();
            if (parent2 != null && this.mSlideEnable) {
                ViewGroup viewGroup = (ViewGroup) parent2;
                float scrollX2 = viewGroup.getScrollX() - getLeft();
                float scrollY = viewGroup.getScrollY() - getTop();
                motionEvent.offsetLocation(-scrollX2, -scrollY);
                viewGroup.onTouchEvent(motionEvent);
                motionEvent.offsetLocation(scrollX2, scrollY);
                parent2.requestDisallowInterceptTouchEvent(true);
            }
        } else if (action == 1) {
            if (this.mSlideEnable || (this.mDrawItemEnable && getSlideViewScrollX() != 0 && Math.abs(getSlideViewScrollX()) != this.mHolderWidth)) {
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
                int xVelocity = (int) velocityTracker.getXVelocity(this.mActivePointerId);
                if (this.mEnableFastDelete) {
                    if ((xVelocity < -1000 && !isLayoutRtl() && Math.abs(getSlideViewScrollX()) > this.mHolderWidth + this.mQuickDeleteSlop) || Math.abs(getSlideViewScrollX()) > this.mHolderWidth + this.mOverSlideDeleteSlop) {
                        this.mhasStartAnimation = true;
                        this.mUpScrollX = getSlideViewScrollX();
                        i10 = getWidth();
                        startDeleteSlideAnimation(this.mSlideView);
                    } else if ((xVelocity <= 1000 || !isLayoutRtl() || Math.abs(getSlideViewScrollX()) <= this.mFastDelHolderWidth + this.mQuickDeleteSlop) && Math.abs(getSlideViewScrollX()) <= this.mFastDelHolderWidth + this.mOverSlideDeleteSlop) {
                        if (Math.abs(scrollX) - (this.mHolderWidth * POINT_FIVE) > 0.0f) {
                            if (isLayoutRtl()) {
                                i11 = this.mHolderWidth;
                                i10 = -i11;
                            } else {
                                i10 = this.mHolderWidth;
                            }
                        }
                        i10 = 0;
                    } else {
                        this.mhasStartAnimation = true;
                        this.mUpScrollX = getSlideViewScrollX();
                        i10 = -getWidth();
                        startDeleteSlideAnimation(this.mSlideView);
                    }
                    if (!this.mhasStartAnimation) {
                        animationScrollTo(i10, 0);
                        if (i10 == 0) {
                            startDisAppearAnimationOrNot();
                        }
                    }
                    i12 = i10 == 0 ? 0 : 2;
                    this.mCurrStatus = i12;
                    onSlideListener = this.mOnSlideListener;
                    if (onSlideListener != null) {
                        onSlideListener.onSlide(this, i12);
                    }
                } else {
                    if (this.mSlideEnable || (this.mDrawItemEnable && getSlideViewScrollX() != 0 && Math.abs(getSlideViewScrollX()) != this.mHolderWidth)) {
                        if (xVelocity < -1000) {
                            if (!isLayoutRtl()) {
                                i10 = this.mHolderWidth;
                                if (!this.mhasStartAnimation) {
                                }
                                if (i10 == 0) {
                                }
                                this.mCurrStatus = i12;
                                onSlideListener = this.mOnSlideListener;
                                if (onSlideListener != null) {
                                }
                            }
                        } else {
                            if (xVelocity > 1000) {
                                if (isLayoutRtl()) {
                                    i11 = this.mHolderWidth;
                                    i10 = -i11;
                                }
                            } else if (Math.abs(scrollX) - (this.mHolderWidth * POINT_FIVE) > 0.0f) {
                                if (isLayoutRtl()) {
                                    i11 = this.mHolderWidth;
                                    i10 = -i11;
                                } else {
                                    i10 = this.mHolderWidth;
                                }
                            }
                            if (!this.mhasStartAnimation) {
                            }
                            if (i10 == 0) {
                            }
                            this.mCurrStatus = i12;
                            onSlideListener = this.mOnSlideListener;
                            if (onSlideListener != null) {
                            }
                        }
                    }
                    i10 = 0;
                    if (!this.mhasStartAnimation) {
                    }
                    if (i10 == 0) {
                    }
                    this.mCurrStatus = i12;
                    onSlideListener = this.mOnSlideListener;
                    if (onSlideListener != null) {
                    }
                }
            }
            if (Math.abs(getSlideViewScrollX()) == this.mHolderWidth) {
                if (isLayoutRtl()) {
                    int i15 = this.mInitialMotionX;
                    int i16 = this.mHolderWidth;
                    if (i15 < i16) {
                    }
                    z10 = false;
                } else {
                    if (this.mInitialMotionX > getWidth() - this.mHolderWidth) {
                    }
                    z10 = false;
                }
                if (z10) {
                    int i17 = 0;
                    while (true) {
                        if (i17 >= this.mItemCount) {
                            break;
                        }
                        int i18 = 0;
                        for (int i19 = 0; i19 < i17; i19++) {
                            i18 += this.mItems.get(i19).getWidth();
                        }
                        if (!isLayoutRtl() ? this.mInitialMotionX <= (getWidth() - i18) - this.mItems.get(i17).getWidth() || x11 <= (getWidth() - i18) - this.mItems.get(i17).getWidth() : this.mInitialMotionX >= this.mItems.get(i17).getWidth() + i18 || x11 >= i18 + this.mItems.get(i17).getWidth()) {
                            if (this.mCanDelete && i17 == 0 && !this.mhasStartAnimation && this.mStartDeleteAnimation) {
                                this.mhasStartAnimation = true;
                                startDeleteAnimation(this.mSlideView);
                            }
                            playSoundEffect(0);
                            OnSlideMenuItemClickListener onSlideMenuItemClickListener = this.mOnSlideMenuItemClickListener;
                            if (onSlideMenuItemClickListener != null) {
                                onSlideMenuItemClickListener.onSlideMenuItemClick(this.mItems.get(i17), i17);
                            }
                        } else {
                            i17++;
                        }
                    }
                } else {
                    if (this.mInitialMotionX < getWidth() - this.mHolderWidth) {
                        int width = getWidth();
                        int i20 = this.mHolderWidth;
                        if (x11 < width - i20 && this.mInitialMotionX - x11 < (-i20)) {
                            z11 = true;
                            if (isLayoutRtl()) {
                                int i21 = this.mInitialMotionX;
                                int i22 = this.mHolderWidth;
                                if (i21 < i22 && x11 > i22 && i21 - x11 > i22) {
                                    z12 = true;
                                }
                                z11 = z12;
                            }
                            if (z11) {
                                shrink();
                            }
                        }
                    }
                    z11 = false;
                    if (isLayoutRtl()) {
                    }
                    if (z11) {
                    }
                }
            }
            ViewParent parent3 = getParent();
            if (parent3 != null && this.mSlideEnable) {
                ViewGroup viewGroup2 = (ViewGroup) parent3;
                float scrollX3 = viewGroup2.getScrollX() - getLeft();
                float scrollY2 = viewGroup2.getScrollY() - getTop();
                motionEvent.offsetLocation(-scrollX3, -scrollY2);
                if (!this.mIsBeingDragged && (!isLayoutRtl() ? getSlideViewScrollX() <= 0 : getSlideViewScrollX() >= 0)) {
                    viewGroup2.onTouchEvent(motionEvent);
                } else {
                    MotionEvent obtain = MotionEvent.obtain(motionEvent);
                    obtain.setAction(3);
                    viewGroup2.onTouchEvent(obtain);
                    obtain.recycle();
                }
                motionEvent.offsetLocation(scrollX3, scrollY2);
            }
            endDrag();
        } else if (action == 2) {
            int i23 = x11 - this.mLastMotionX;
            int i24 = y4 - this.mLastMotionY;
            int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
            int x13 = (int) motionEvent.getX(findPointerIndex);
            int i25 = x13 - this.mLastMotionX;
            int abs = Math.abs(i25);
            int y11 = (int) motionEvent.getY(findPointerIndex);
            int abs2 = Math.abs(y11 - this.mInitialMotionY);
            this.mLastMotionX = x13;
            this.mLastMotionY = y11;
            if (abs > 8 && abs * POINT_EIGHT > abs2) {
                this.mIsBeingDragged = true;
                if (i25 > 0) {
                    i13 = this.mInitialMotionX + 8;
                } else {
                    i13 = this.mInitialMotionX - 8;
                }
                this.mLastMotionX = i13;
                this.mLastMotionY = y4;
            }
            if (this.mIsBeingDragged && i23 != 0 && this.mSlideEnable) {
                if (Math.abs(scrollX) >= this.mHolderWidth) {
                    if (isLayoutRtl()) {
                        i23 = COUIPhysicalAnimationUtil.b(i23, -scrollX, getWidth() - this.mFastDelHolderWidth);
                    } else {
                        i23 = COUIPhysicalAnimationUtil.b(i23, -(scrollX - this.mHolderWidth), getWidth() - this.mHolderWidth);
                    }
                }
                int i26 = scrollX - i23;
                ViewParent parent4 = getParent();
                if (parent4 != 0) {
                    parent4.requestDisallowInterceptTouchEvent(true);
                    ((View) parent4).setPressed(false);
                }
                setPressed(false);
                if ((getLayoutDirection() == 1 || i26 >= 0) && (getLayoutDirection() != 1 || i26 <= 0)) {
                    Math.abs(i26);
                } else {
                    i26 = 0;
                }
                if (this.mScrollAll) {
                    scrollTo(i26, 0);
                } else {
                    this.mSlideView.scrollTo(i26, 0);
                }
                this.mLastMotionX = x11;
                this.mLastMotionY = y4;
                VelocityTracker velocityTracker2 = this.mVelocityTracker;
                if (velocityTracker2 != null) {
                    velocityTracker2.addMovement(motionEvent);
                }
                return true;
            }
            if (i24 != 0 && (parent = getParent()) != 0) {
                if (!this.mIsBeingDragged && (i24 > 6 || i24 < -6)) {
                    parent.requestDisallowInterceptTouchEvent(false);
                    if (parent instanceof ListView) {
                        try {
                            AbsListViewNative.b((AbsListView) parent, 0);
                        } catch (Exception e10) {
                            e10.printStackTrace();
                        }
                    }
                }
                ((View) parent).setPressed(false);
                setPressed(false);
            }
        } else if (action == 3) {
            if (getSlideViewScrollX() == 0) {
                startDisAppearAnimationOrNot();
            }
            if (scrollX - (this.mHolderWidth * POINT_FIVE) > 0.0f) {
                i14 = isLayoutRtl() ? -this.mHolderWidth : this.mHolderWidth;
            } else {
                i14 = 0;
            }
            smoothScrollTo(i14, 0);
            i12 = i14 == 0 ? 0 : 2;
            this.mCurrStatus = i12;
            OnSlideListener onSlideListener3 = this.mOnSlideListener;
            if (onSlideListener3 != null) {
                onSlideListener3.onSlide(this, i12);
            }
            ViewParent parent5 = getParent();
            if (parent5 != null) {
                parent5.requestDisallowInterceptTouchEvent(false);
            }
            endDrag();
        }
        this.mLastX = x11;
        this.mLastY = y4;
        VelocityTracker velocityTracker3 = this.mVelocityTracker;
        if (velocityTracker3 != null) {
            velocityTracker3.addMovement(motionEvent);
        }
        return true;
    }

    public void refresh() {
        String resourceTypeName = getResources().getResourceTypeName(this.mRefreshStyle);
        TypedArray typedArray = null;
        if (TextUtils.equals(resourceTypeName, "attr")) {
            typedArray = getContext().getTheme().obtainStyledAttributes(null, R$styleable.COUISlideView, this.mRefreshStyle, 0);
        } else if (TextUtils.equals(resourceTypeName, "style")) {
            typedArray = getContext().getTheme().obtainStyledAttributes(null, R$styleable.COUISlideView, 0, this.mRefreshStyle);
        }
        if (typedArray != null) {
            int color = typedArray.getColor(R$styleable.COUISlideView_itemBackgroundColor, this.mItemBackgroundColor);
            this.mItemBackgroundColor = color;
            this.mItemsBackgroundColors.set(0, Integer.valueOf(color));
            this.mSlideTextColor = typedArray.getColor(R$styleable.COUISlideView_slideTextColor, getContext().getResources().getColor(R$color.coui_slideview_textcolor));
            invalidate();
            typedArray.recycle();
        }
    }

    public void removeColor(int i10) {
        if (i10 < 0 || this.mItemsBackgroundColors.isEmpty() || i10 >= this.mItemsBackgroundColors.size()) {
            return;
        }
        this.mItemsBackgroundColors.remove(i10);
        postInvalidate();
    }

    public void removeItem(int i10) {
        if (i10 < 0 || i10 >= this.mItems.size()) {
            return;
        }
        this.mItems.remove(i10);
        itemWidthChange();
    }

    public void restoreLayout() {
        this.mAlpha = 0;
        this.mSlideView.setTranslationX(0.0f);
        getContentView().getLayoutParams().height = this.mInitialHeight;
        setVisibility(0);
        clearAnimation();
        this.mhasStartAnimation = false;
    }

    public void setBackgroundPadding(int i10) {
        this.mBackgroundPadding = i10;
    }

    public void setCanStartDeleteAnimation(boolean z10) {
        this.mStartDeleteAnimation = z10;
    }

    public void setContentView(View view) {
        if (this.mScrollAll) {
            this.mViewContent.addView(view, new LinearLayout.LayoutParams(-1, -1));
            this.mSlideView = this;
        } else {
            addView(view, new LinearLayout.LayoutParams(-1, -1));
            this.mSlideView = view;
        }
    }

    public void setDeleteEnable(boolean z10) {
        if (this.mCanDelete == z10) {
            return;
        }
        this.mCanDelete = z10;
        if (z10) {
            ArrayList<COUISlideMenuItem> arrayList = this.mItems;
            Context context = this.mContext;
            arrayList.add(0, new COUISlideMenuItem(context, context.getDrawable(com.support.list.R$drawable.coui_slide_view_delete)));
            if (this.mPaint != null) {
                COUISlideMenuItem cOUISlideMenuItem = this.mItems.get(0);
                int measureText = cOUISlideMenuItem.getText() != null ? ((int) this.mPaint.measureText((String) cOUISlideMenuItem.getText())) + (this.mTextPadding * 2) : 0;
                if (measureText > cOUISlideMenuItem.getWidth()) {
                    cOUISlideMenuItem.setWidth(measureText);
                }
            }
        } else {
            this.mItems.remove(0);
        }
        itemWidthChange();
    }

    public void setDeleteItemIcon(int i10) {
        if (this.mCanDelete) {
            this.mItems.get(0).setIcon(i10);
        }
    }

    public void setDeleteItemText(CharSequence charSequence) {
        int measureText;
        if (this.mCanDelete) {
            COUISlideMenuItem cOUISlideMenuItem = this.mItems.get(0);
            cOUISlideMenuItem.setText(charSequence);
            Paint paint = this.mPaint;
            if (paint == null || (measureText = ((int) paint.measureText((String) cOUISlideMenuItem.getText())) + (this.mTextPadding * 2)) <= cOUISlideMenuItem.getWidth()) {
                return;
            }
            cOUISlideMenuItem.setWidth(measureText);
            itemWidthChange();
        }
    }

    public void setDisableBackgroundAnimator(boolean z10) {
        this.mDisableBackgroundAnimator = z10;
    }

    public void setDiver(int i10) {
        setDiver(getContext().getResources().getDrawable(i10));
    }

    public void setDiverEnable(boolean z10) {
        this.mDiverEnable = z10;
        invalidate();
    }

    public void setDrawItemEnable(boolean z10) {
        this.mDrawItemEnable = z10;
    }

    public void setGroupOffset(int i10) {
        this.mPaddingRight = i10;
    }

    public void setItemBackgroundColor(int i10) {
        if (this.mItemBackgroundColor != i10) {
            this.mItemBackgroundColor = i10;
            this.mItemsBackgroundColors.set(0, Integer.valueOf(i10));
            invalidate();
        }
    }

    public void setMenuDividerEnable(boolean z10) {
        this.mMenuDividerEnable = z10;
    }

    public void setMenuItemStyle(int i10) {
        if (i10 == 1) {
            this.mIsMenuRoundStyle = true;
        } else {
            this.mIsMenuRoundStyle = false;
        }
        itemWidthChange();
        initAnimation(getContext());
    }

    public void setOnDeleteItemClickListener(OnDeleteItemClickListener onDeleteItemClickListener) {
        this.mOnDeleteItemClickListener = onDeleteItemClickListener;
    }

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        this.mOnSlideListener = onSlideListener;
    }

    public void setOnSlideMenuItemClickListener(OnSlideMenuItemClickListener onSlideMenuItemClickListener) {
        this.mOnSlideMenuItemClickListener = onSlideMenuItemClickListener;
    }

    public void setOnSmoothScrollListener(OnSmoothScrollListener onSmoothScrollListener) {
        this.mOnSmoothScrollListener = onSmoothScrollListener;
    }

    public void setRoundRectMenuLeftMargin(int i10) {
        this.mRoundRectMenuLeftMargin = i10;
    }

    public void setRoundRectMenuRightMargin(int i10) {
        this.mRoundRectMenuRightMargin = i10;
    }

    public void setSlideEnable(boolean z10) {
        this.mSlideEnable = z10;
    }

    public void setSlideTextColor(int i10) {
        if (this.mSlideTextColor != i10) {
            this.mSlideTextColor = i10;
            this.mPaint.setColor(i10);
            invalidate();
        }
    }

    public void setSlideViewScrollX(int i10) {
        if (this.mScrollAll) {
            scrollTo(i10, getScrollY());
        } else {
            View view = this.mSlideView;
            view.scrollTo(i10, view.getScrollY());
        }
    }

    public void setTouchAllRound(boolean z10) {
        this.mTouchAllRound = z10;
    }

    public void setUseDefaultBackground(boolean z10) {
        this.mUseDefaultBackGround = z10;
    }

    public void shrink() {
        SpringAnimation springAnimation = this.mSpringAnimation;
        if (springAnimation != null) {
            springAnimation.w();
        }
        if (getSlideViewScrollX() != 0) {
            if (this.mOnSmoothScrollListener != null) {
                Runnable runnable = this.mSmoothScrollRunnable;
                if (runnable != null) {
                    removeCallbacks(runnable);
                }
                Runnable runnable2 = new Runnable() { // from class: com.coui.appcompat.slideview.COUISlideView.2
                    @Override // java.lang.Runnable
                    public void run() {
                        COUISlideView.this.mSmoothScrollRunnable = null;
                        if (COUISlideView.this.mOnSmoothScrollListener != null) {
                            COUISlideView.this.mOnSmoothScrollListener.onSmoothScroll(COUISlideView.this);
                        }
                    }
                };
                this.mSmoothScrollRunnable = runnable2;
                postDelayed(runnable2, 200L);
            }
            smoothScrollTo(0, 0);
            this.mCurrStatus = 0;
            OnSlideListener onSlideListener = this.mOnSlideListener;
            if (onSlideListener != null) {
                onSlideListener.onSlide(this, 0);
            }
            startDisAppearAnimationOrNot();
        }
    }

    public void smoothScrollTo(int i10, int i11) {
        int slideViewScrollX = getSlideViewScrollX();
        int i12 = i10 - slideViewScrollX;
        int abs = Math.abs(i12) * 3;
        this.mScroller.startScroll(slideViewScrollX, 0, i12, 0, abs > ANIM_DURATION ? ANIM_DURATION : abs);
        invalidate();
    }

    public void startAppearAnimation() {
        startAppearAnimation(this.mDisableBackgroundAnimator);
    }

    public void startDeleteAnimation(View view) {
        if (this.mIsMenuRoundStyle) {
            this.mSlideDeleteInRoundMode = true;
        }
        int i10 = getLayoutDirection() == 1 ? -this.mHolderWidth : this.mHolderWidth;
        int width = getLayoutDirection() == 1 ? -getWidth() : getWidth();
        this.mInitialHeight = getMeasuredHeight();
        new COUISlideDeleteAnimation(view, this, i10, width, getHeight(), 0) { // from class: com.coui.appcompat.slideview.COUISlideView.7
            @Override // com.coui.appcompat.slideview.COUISlideDeleteAnimation
            public void itemViewDelete() {
                if (COUISlideView.this.mOnDeleteItemClickListener != null) {
                    COUISlideView.this.mhasStartAnimation = false;
                    COUISlideView.this.mOnDeleteItemClickListener.onDeleteItemClick();
                }
            }
        }.startAnimation();
    }

    public void startDeleteSlideAnimation(View view) {
        this.mSlideDelete = true;
        this.mCurrentTranslateX = getSlideViewScrollX();
        this.mTargetTranslateX = getLayoutDirection() == 1 ? -getWidth() : getWidth();
        this.mInitialHeight = getMeasuredHeight();
        new COUISlideDeleteAnimation(view, this, this.mCurrentTranslateX, this.mTargetTranslateX, getHeight(), 0) { // from class: com.coui.appcompat.slideview.COUISlideView.8
            @Override // com.coui.appcompat.slideview.COUISlideDeleteAnimation
            public void itemViewDelete() {
                if (COUISlideView.this.mOnDeleteItemClickListener != null) {
                    COUISlideView.this.mhasStartAnimation = false;
                    COUISlideView.this.mOnDeleteItemClickListener.onDeleteItemClick();
                }
            }
        }.startAnimation();
    }

    public void startDisAppearAnimationOrNot() {
        startDisAppearAnimationOrNot(this.mDisableBackgroundAnimator);
    }

    public COUISlideView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, com.support.list.R$attr.couiSlideView);
    }

    public void addColor(int i10, int i11) {
        if (i10 < 0) {
            this.mItemsBackgroundColors.add(Integer.valueOf(i11));
        } else {
            this.mItemsBackgroundColors.add(i10, Integer.valueOf(i11));
        }
        postInvalidate();
    }

    public void addItem(int i10, COUISlideMenuItem cOUISlideMenuItem) {
        if (this.mPaint != null) {
            int measureText = cOUISlideMenuItem.getText() != null ? ((int) this.mPaint.measureText((String) cOUISlideMenuItem.getText())) + (this.mTextPadding * 2) : 0;
            if (measureText > cOUISlideMenuItem.getWidth()) {
                cOUISlideMenuItem.setWidth(measureText);
            }
        }
        if (i10 < 0) {
            this.mItems.add(cOUISlideMenuItem);
        } else {
            this.mItems.add(i10, cOUISlideMenuItem);
        }
        itemWidthChange();
        postInvalidate();
    }

    public void setDiver(Drawable drawable) {
        if (drawable != null) {
            this.mDiverEnable = true;
        } else {
            this.mDiverEnable = false;
        }
        if (this.mDiver != drawable) {
            this.mDiver = drawable;
            invalidate();
        }
    }

    public void startAppearAnimation(boolean z10) {
        if (this.mIsMenuRoundStyle && this.mCurrStatus == 0) {
            cancelBackgroundAnimators();
            if (z10) {
                this.mCurColor = COUIContextUtil.a(getContext(), R$attr.couiColorPressBackground);
                this.mState = 1;
                invalidate();
                return;
            }
            this.mBackgroundAppearAnimator.start();
        }
    }

    public void startDisAppearAnimationOrNot(boolean z10) {
        if (this.mIsMenuRoundStyle) {
            if (z10) {
                cancelBackgroundAnimators();
                int a10 = COUIContextUtil.a(getContext(), R$attr.couiColorPressBackground);
                this.mCurColor = Color.argb(0, Color.red(a10), Color.green(a10), Color.blue(a10));
                this.mState = 2;
                invalidate();
                return;
            }
            if (this.mBackgroundAppearAnimator.isRunning()) {
                this.mNeedAutoStartDisAppear = true;
            } else {
                if (this.mBackgroundDisappearAnimator.isRunning() || this.mState != 1) {
                    return;
                }
                this.mBackgroundDisappearAnimator.start();
            }
        }
    }

    public COUISlideView(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public void setDeleteItemIcon(Drawable drawable) {
        if (this.mCanDelete) {
            this.mItems.get(0).setIcon(drawable);
        }
    }

    public COUISlideView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.mHolderWidth = 0;
        this.mFastDelHolderWidth = 0;
        this.mCanDelete = false;
        this.mCanCopy = true;
        this.mCanRename = false;
        this.mSlideEnable = true;
        this.mDrawItemEnable = false;
        this.mDiverEnable = false;
        this.mIconCount = 0;
        this.mItemCount = 0;
        this.mAlpha = 0;
        this.mTextPadding = 0;
        this.mLayout = null;
        this.mLastX = 0;
        this.mLastY = 0;
        this.mSlideTouchSlop = 8;
        this.mVelocityTracker = null;
        this.mActivePointerId = -1;
        this.mScrollAll = false;
        this.mIsUnableToDrag = false;
        this.mIsBeingDragged = false;
        this.mhasStartAnimation = false;
        this.mStartDeleteAnimation = true;
        this.mCurrStatus = 0;
        this.mGroupStyle = -1;
        this.mPaddingRight = 18;
        this.mRadius = 20;
        this.mUseDefaultBackGround = true;
        this.mMenuDividerEnable = true;
        this.mRoundRectMenuItemRadius = getResources().getDimensionPixelSize(R$dimen.coui_slide_view_menuitem_round_rect_radius);
        this.mRoundRectMenuItemGap = getResources().getDimensionPixelSize(R$dimen.coui_slide_view_menuitem_gap_size);
        this.mRoundRectMenuLeftMargin = getResources().getDimensionPixelSize(R$dimen.coui_slide_view_menuitem_start_margin);
        this.mRoundRectMenuRightMargin = getResources().getDimensionPixelSize(R$dimen.coui_slide_view_menuitem_end_margin);
        this.mBackGroundPaint = new Paint();
        this.mPath = new Path();
        this.mIsMenuRoundStyle = false;
        this.mAppearInterpolator = new COUILinearInterpolator();
        this.mNeedAutoStartDisAppear = false;
        this.mState = 2;
        this.mCurColor = 0;
        this.mDisappearInterpolator = new PathInterpolator(0.17f, 0.17f, 0.67f, ONE);
        this.mTouchAllRound = false;
        this.mBackgroundPadding = this.mRoundRectMenuItemRadius;
        this.mDisableBackgroundAnimator = false;
        if (attributeSet != null) {
            this.mRefreshStyle = attributeSet.getStyleAttribute();
        }
        if (this.mRefreshStyle == 0) {
            this.mRefreshStyle = i10;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUISlideView, i10, 0);
        this.mItemBackgroundColor = obtainStyledAttributes.getColor(R$styleable.COUISlideView_itemBackgroundColor, COUIContextUtil.a(context, R$attr.couiColorError));
        this.mSlideTextColor = obtainStyledAttributes.getColor(R$styleable.COUISlideView_slideTextColor, context.getColor(R$color.coui_slideview_textcolor));
        this.mTouchAllRound = obtainStyledAttributes.getBoolean(R$styleable.COUISlideView_touchAllRound, false);
        this.mBackgroundPadding = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUISlideView_backgroundPadding, this.mRoundRectMenuItemRadius);
        this.mDisableBackgroundAnimator = obtainStyledAttributes.getBoolean(R$styleable.COUISlideView_disableBackgroundAnimator, false);
        obtainStyledAttributes.recycle();
        ArrayList arrayList = new ArrayList();
        this.mItemsBackgroundColors = arrayList;
        arrayList.add(Integer.valueOf(this.mItemBackgroundColor));
        initView();
    }

    public void startDeleteAnimation(View view, float f10, float f11, float f12, float f13) {
        if (this.mhasStartAnimation) {
            return;
        }
        this.mhasStartAnimation = true;
        COUIDeleteAnimation cOUIDeleteAnimation = new COUIDeleteAnimation(view, f10, f11, f12, f13) { // from class: com.coui.appcompat.slideview.COUISlideView.9
            @Override // com.coui.appcompat.slideview.COUIDeleteAnimation, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                if (COUISlideView.this.mOnDeleteItemClickListener != null) {
                    COUISlideView cOUISlideView = COUISlideView.this;
                    cOUISlideView.mInitialHeight = cOUISlideView.getMeasuredHeight();
                    COUISlideView.this.mFadeAnim.setDuration(200L);
                    COUISlideView.this.mFadeAnim.start();
                    COUISlideView.this.startAnimation(new COUISlideCollapseAnimation(COUISlideView.this) { // from class: com.coui.appcompat.slideview.COUISlideView.9.1
                        @Override // com.coui.appcompat.slideview.COUISlideCollapseAnimation
                        public void onItemDelete() {
                            COUISlideView.this.mhasStartAnimation = false;
                            COUISlideView.this.mOnDeleteItemClickListener.onDeleteItemClick();
                        }
                    });
                }
            }
        };
        cOUIDeleteAnimation.setDuration(200L);
        cOUIDeleteAnimation.start();
    }

    public void setDeleteItemText(int i10) {
        setDeleteItemText(this.mContext.getText(i10));
    }
}
