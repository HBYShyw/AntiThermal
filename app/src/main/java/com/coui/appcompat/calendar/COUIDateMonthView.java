package com.coui.appcompat.calendar;

import android.R;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.PathInterpolator;
import androidx.appcompat.R$styleable;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.support.appcompat.R$attr;
import com.support.control.R$dimen;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import m1.COUIEaseInterpolator;
import m1.COUIMoveEaseInterpolator;
import v1.COUIContextUtil;
import z2.COUIChangeTextUtil;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class COUIDateMonthView extends View implements View.OnFocusChangeListener {
    private static final int DAYS_IN_WEEK = 7;
    private static final int DEFAULT_SELECTED_DAY = -1;
    private static final int DEFAULT_WEEK_START = 1;
    private static final int DURATION_OF_DISMISS_ANIMATOR = 150;
    private static final int DURATION_OF_SELECT_ANIMATOR = 280;
    private static final int MAX_WEEKS_IN_MONTH = 6;
    public static final int MAX_YEAR = 2100;
    private static final int MIN_WEEKS_IN_MONTH = 5;
    public static final int MIN_YEAR = 1900;
    private static final String MONTH_YEAR_FORMAT = "MMMMy";
    private static final int SELECTED_HIGHLIGHT_ALPHA = 176;
    private int mActivatedDay;
    private int mActivatedMonth;
    private final int mBackgroundColor;
    private final Calendar mCalendar;
    private int mCellWidth;
    private ValueAnimator mCircleInAnimator;
    private ValueAnimator mCircleOutAnimator;
    private Paint mCirclePaint;
    private Context mContext;
    private float mCurrentDayStrokeRadius;
    private float mDayCircleRadius;
    private final NumberFormat mDayFormatter;
    private int mDayHeight;
    private final Paint mDayHighlightPaint;
    private final Paint mDayHighlightSelectorPaint;
    private int mDayOfWeekHeight;
    private final String[] mDayOfWeekLabels;
    private final TextPaint mDayOfWeekPaint;
    private int mDayOfWeekStart;
    private final TextPaint mDayPaint;
    private float mDaySelectRadius;
    private final Paint mDaySelectorPaint;
    private ColorStateList mDayTextColor;
    private int mDaysInMonth;
    private final int mDesiredCellWidth;
    private final int mDesiredDayHeight;
    private final int mDesiredDayOfWeekHeight;
    private final int mDesiredDayPadding;
    private final int mDesiredMonthHeight;
    private int mEnabledDayEnd;
    private int mEnabledDayStart;
    private int mHighlightedDay;
    private final int mHintColor;
    private int mInitColor;
    private boolean mIsMaxCol;
    private boolean mIsSelectYear;
    private boolean mIsShowAnimaor;
    private boolean mIsTouchHighlighted;
    private final Locale mLocale;
    private int mMonth;
    private int mMonthHeight;
    private final TextPaint mMonthPaint;
    private int mMonthWidth;
    private String mMonthYearLabel;
    private int mOldMonth;
    private int mOldSelectDay;
    private final Paint mOldSelectorPaint;
    private OnDayClickListener mOnDayClickListener;
    private int mPaddedHeight;
    private int mPaddedWidth;
    private int mPaddingStart;
    private int mPreviouslyHighlightedDay;
    private final int mPrimaryColor;
    private int mToday;
    private final MonthViewTouchHelper mTouchHelper;
    private int mWeekStart;
    private int mYear;
    private static final PathInterpolator SELECT_ANIMATOR_INTERPOLATOR = new COUIMoveEaseInterpolator();
    private static final PathInterpolator CIRCLE_OUT_ANIMATOR_INTERPOLATOR = new COUIEaseInterpolator();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class MonthViewTouchHelper extends ExploreByTouchHelper {
        private static final String CN_DATE_FORMAT = "MMMM dd 日 EE";
        private static final String CN_LOCAL = "CN";
        private static final String DATE_FORMAT = "EE dd MMMM";
        private static final String HK_LOCAL = "HK";
        private static final String TW_LOCAL = "TW";
        private final Calendar mTempCalendar;
        private final Rect mTempRect;

        public MonthViewTouchHelper(View view) {
            super(view);
            this.mTempRect = new Rect();
            this.mTempCalendar = Calendar.getInstance();
        }

        private CharSequence getDayDescription(int i10) {
            if (!COUIDateMonthView.this.isValidDayOfMonth(i10)) {
                return "";
            }
            this.mTempCalendar.set(COUIDateMonthView.this.mYear, COUIDateMonthView.this.mMonth, i10);
            return DateFormat.format(isChinese() ? CN_DATE_FORMAT : DATE_FORMAT, this.mTempCalendar.getTimeInMillis());
        }

        private CharSequence getDayText(int i10) {
            if (COUIDateMonthView.this.isValidDayOfMonth(i10)) {
                return COUIDateMonthView.this.mDayFormatter.format(i10);
            }
            return null;
        }

        private boolean isChinese() {
            String country = COUIDateMonthView.this.mContext.getResources().getConfiguration().locale.getCountry();
            if (country != null) {
                return country.equalsIgnoreCase(CN_LOCAL) || country.equalsIgnoreCase(TW_LOCAL) || country.equalsIgnoreCase(HK_LOCAL);
            }
            return false;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected int getVirtualViewAt(float f10, float f11) {
            int dayAtLocation = COUIDateMonthView.this.getDayAtLocation((int) (f10 + 0.5f), (int) (f11 + 0.5f));
            if (dayAtLocation != -1) {
                return dayAtLocation;
            }
            return Integer.MIN_VALUE;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void getVisibleVirtualViews(List<Integer> list) {
            for (int i10 = 1; i10 <= COUIDateMonthView.this.mDaysInMonth; i10++) {
                list.add(Integer.valueOf(i10));
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected boolean onPerformActionForVirtualView(int i10, int i11, Bundle bundle) {
            if (i11 != 16) {
                return false;
            }
            return COUIDateMonthView.this.onDayClicked(i10);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void onPopulateEventForVirtualView(int i10, AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.setContentDescription(getDayDescription(i10));
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onPopulateNodeForVirtualView(int i10, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (!COUIDateMonthView.this.getBoundsForDay(i10, this.mTempRect)) {
                this.mTempRect.setEmpty();
                accessibilityNodeInfoCompat.Z("");
                accessibilityNodeInfoCompat.Q(this.mTempRect);
                accessibilityNodeInfoCompat.A0(false);
                return;
            }
            accessibilityNodeInfoCompat.y0(getDayText(i10));
            accessibilityNodeInfoCompat.Z(getDayDescription(i10));
            accessibilityNodeInfoCompat.Q(this.mTempRect);
            boolean isDayEnabled = COUIDateMonthView.this.isDayEnabled(i10);
            if (isDayEnabled) {
                accessibilityNodeInfoCompat.a(16);
            }
            accessibilityNodeInfoCompat.b0(isDayEnabled);
            if (i10 == COUIDateMonthView.this.mActivatedDay) {
                accessibilityNodeInfoCompat.U(true);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface OnDayClickListener {
        void onDayClick(COUIDateMonthView cOUIDateMonthView, Calendar calendar);
    }

    /* loaded from: classes.dex */
    public interface OnMonthChangeListener {
        void onMonthChange(String str);
    }

    public COUIDateMonthView(Context context) {
        this(context, null);
    }

    @SuppressLint({"WrongConstant"})
    private ColorStateList applyTextAppearance(Paint paint, int i10) {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(null, R$styleable.TextAppearance, 0, i10);
        String string = obtainStyledAttributes.getString(R$styleable.TextAppearance_android_fontFamily);
        if (string != null) {
            paint.setTypeface(Typeface.create(string, 0));
        }
        paint.setTextSize((int) COUIChangeTextUtil.d(obtainStyledAttributes.getDimensionPixelSize(R$styleable.TextAppearance_android_textSize, (int) paint.getTextSize()), getContext().getResources().getConfiguration().fontScale));
        ColorStateList a10 = MaterialResources.a(getContext(), obtainStyledAttributes, R$styleable.TextAppearance_android_textColor);
        if (a10 != null) {
            paint.setColor(a10.getColorForState(View.ENABLED_STATE_SET, 0));
        }
        obtainStyledAttributes.recycle();
        return a10;
    }

    private void configAnimator() {
        ValueAnimator valueAnimator = new ValueAnimator();
        this.mCircleInAnimator = valueAnimator;
        valueAnimator.setFloatValues(0.0f, 1.0f);
        this.mCircleInAnimator.setDuration(280L);
        this.mCircleInAnimator.setInterpolator(SELECT_ANIMATOR_INTERPOLATOR);
        this.mCircleInAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.calendar.COUIDateMonthView.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                float animatedFraction = valueAnimator2.getAnimatedFraction();
                COUIDateMonthView.this.mDaySelectorPaint.setAlpha((int) (255.0f * animatedFraction));
                COUIDateMonthView cOUIDateMonthView = COUIDateMonthView.this;
                cOUIDateMonthView.mDaySelectRadius = (cOUIDateMonthView.mDayCircleRadius * 0.8f) + (0.2f * animatedFraction * COUIDateMonthView.this.mDayCircleRadius);
                COUIDateMonthView.this.invalidate();
                if (animatedFraction == 1.0f) {
                    COUIDateMonthView.this.mIsShowAnimaor = false;
                }
            }
        });
        ValueAnimator valueAnimator2 = new ValueAnimator();
        this.mCircleOutAnimator = valueAnimator2;
        valueAnimator2.setFloatValues(0.0f, 1.0f);
        this.mCircleOutAnimator.setDuration(150L);
        this.mCircleOutAnimator.setInterpolator(CIRCLE_OUT_ANIMATOR_INTERPOLATOR);
        this.mCircleOutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.calendar.COUIDateMonthView.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator3) {
                COUIDateMonthView.this.mOldSelectorPaint.setAlpha((int) ((1.0f - valueAnimator3.getAnimatedFraction()) * 255.0f));
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:115:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x00c2  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x011e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void drawDays(Canvas canvas) {
        boolean z10;
        int daysInMonth;
        int i10;
        int i11;
        int i12;
        int colorForState;
        Paint paint;
        int i13;
        int i14;
        TextPaint textPaint = this.mDayPaint;
        int i15 = this.mMonthHeight + this.mDayOfWeekHeight;
        int i16 = 1;
        boolean z11 = false;
        boolean z12 = (this.mDayOfWeekStart + getDaysInMonth(this.mMonth, this.mYear)) - 1 > 35;
        this.mIsMaxCol = z12;
        int i17 = this.mDayHeight + (z12 ? 0 : this.mDesiredDayPadding);
        int i18 = this.mCellWidth;
        float ascent = (textPaint.ascent() + textPaint.descent()) / 2.0f;
        int i19 = i15 + (i17 / 2);
        boolean z13 = this.mWeekStart == 2;
        int i20 = 7;
        if (z13) {
            int i21 = this.mDayOfWeekStart;
            if (i21 == 1) {
                this.mDayOfWeekStart = 7;
                z10 = true;
                if (this.mDayOfWeekStart > 1) {
                    int i22 = 1;
                    while (i22 <= this.mDayOfWeekStart - i16) {
                        int i23 = (i18 / 2) + ((i22 - 1) * i18);
                        if (COUIPickerMathUtils.isLayoutRtl(this)) {
                            i23 = (this.mCellWidth * 7) - i23;
                        }
                        textPaint.setFakeBoldText(z11);
                        textPaint.setColor(this.mHintColor);
                        int i24 = this.mMonth;
                        if (i24 == 0) {
                            i13 = 11;
                            i14 = this.mYear - i16;
                        } else {
                            i13 = i24 - 1;
                            i14 = this.mYear;
                        }
                        canvas.drawText(this.mDayFormatter.format((getDaysInMonth(i13, i14) - this.mDayOfWeekStart) + i22 + i16), i23, i19 - ascent, textPaint);
                        i22++;
                        i16 = 1;
                        z11 = false;
                    }
                }
                daysInMonth = ((((!this.mIsMaxCol ? 6 : 5) * 7) - getDaysInMonth(this.mMonth, this.mYear)) - this.mDayOfWeekStart) + 1;
                int i25 = (i17 * 4) + i19;
                boolean z14 = this.mIsMaxCol;
                int i26 = i25 + (!z14 ? i17 : 0);
                int findEndDayOffset = findEndDayOffset(z14);
                int i27 = i26;
                i10 = 1;
                while (i10 <= daysInMonth) {
                    int i28 = (i18 / 2) + (i18 * findEndDayOffset);
                    if (COUIPickerMathUtils.isLayoutRtl(this)) {
                        i28 = (this.mCellWidth * i20) - i28;
                    }
                    textPaint.setColor(this.mHintColor);
                    boolean z15 = z10;
                    canvas.drawText(this.mDayFormatter.format(i10), i28, i27 - ascent, textPaint);
                    findEndDayOffset++;
                    if (findEndDayOffset == 7) {
                        i27 += i17;
                        findEndDayOffset = 0;
                    }
                    i10++;
                    i20 = 7;
                    z10 = z15;
                }
                int i29 = i20;
                boolean z16 = z10;
                if (z13) {
                    i11 = 1;
                } else {
                    int i30 = this.mDayOfWeekStart;
                    if (i30 == i29 && z16) {
                        i11 = 1;
                        this.mDayOfWeekStart = 1;
                    } else {
                        i11 = 1;
                        this.mDayOfWeekStart = i30 + 1;
                    }
                }
                int findDayOffset = findDayOffset();
                i12 = i11;
                while (i12 <= this.mDaysInMonth) {
                    int i31 = (i18 * findDayOffset) + (i18 / 2);
                    if (COUIPickerMathUtils.isLayoutRtl(this)) {
                        i31 = (this.mCellWidth * 7) - i31;
                    }
                    boolean isDayEnabled = isDayEnabled(i12);
                    int i32 = isDayEnabled ? 8 : 0;
                    int i33 = (this.mActivatedDay == i12 && this.mIsSelectYear) ? i11 : 0;
                    boolean z17 = this.mOldSelectDay == i12 && this.mActivatedMonth == this.mOldMonth && this.mIsShowAnimaor;
                    boolean z18 = this.mHighlightedDay == i12;
                    if (i33 != 0) {
                        i32 |= 32;
                        if (z18) {
                            paint = this.mDayHighlightSelectorPaint;
                        } else {
                            paint = this.mDaySelectorPaint;
                        }
                        canvas.drawCircle(i31, i19, this.mDaySelectRadius / 2.0f, paint);
                    } else if (z18) {
                        i32 |= 16;
                        if (isDayEnabled) {
                            canvas.drawCircle(i31, i19, this.mDayCircleRadius / 2.0f, this.mDayHighlightPaint);
                        }
                    } else if (z17 && isDayEnabled) {
                        canvas.drawCircle(i31, i19, this.mDayCircleRadius / 2.0f, this.mOldSelectorPaint);
                    }
                    if ((this.mToday == i12) && i33 == 0) {
                        colorForState = this.mPrimaryColor;
                        this.mCirclePaint.setColor(colorForState);
                        canvas.drawCircle(i31, i19, this.mDayCircleRadius / 2.0f, this.mCirclePaint);
                    } else {
                        colorForState = this.mDayTextColor.getColorForState(COUIPickerMathUtils.getViewState(i32), 0);
                    }
                    textPaint.setColor(colorForState);
                    canvas.drawText(this.mDayFormatter.format(i12), i31, i19 - ascent, textPaint);
                    findDayOffset++;
                    if (findDayOffset == 7) {
                        i19 += i17;
                        findDayOffset = 0;
                    }
                    i12++;
                    i11 = 1;
                }
            }
            this.mDayOfWeekStart = i21 - 1;
        }
        z10 = false;
        if (this.mDayOfWeekStart > 1) {
        }
        daysInMonth = ((((!this.mIsMaxCol ? 6 : 5) * 7) - getDaysInMonth(this.mMonth, this.mYear)) - this.mDayOfWeekStart) + 1;
        int i252 = (i17 * 4) + i19;
        boolean z142 = this.mIsMaxCol;
        int i262 = i252 + (!z142 ? i17 : 0);
        int findEndDayOffset2 = findEndDayOffset(z142);
        int i272 = i262;
        i10 = 1;
        while (i10 <= daysInMonth) {
        }
        int i292 = i20;
        boolean z162 = z10;
        if (z13) {
        }
        int findDayOffset2 = findDayOffset();
        i12 = i11;
        while (i12 <= this.mDaysInMonth) {
        }
    }

    private void drawDaysOfWeek(Canvas canvas) {
        TextPaint textPaint = this.mDayOfWeekPaint;
        int i10 = this.mMonthHeight;
        int i11 = this.mDayOfWeekHeight;
        int i12 = this.mCellWidth;
        float ascent = (textPaint.ascent() + textPaint.descent()) / 2.0f;
        int i13 = i10 + (i11 / 2);
        for (int i14 = 0; i14 < 7; i14++) {
            int i15 = (i12 * i14) + (i12 / 2);
            if (COUIPickerMathUtils.isLayoutRtl(this)) {
                i15 = (this.mCellWidth * 7) - i15;
            }
            canvas.drawText(this.mDayOfWeekLabels[i14], i15, i13 - ascent, textPaint);
        }
    }

    private void drawMonth(Canvas canvas) {
        canvas.drawText(this.mMonthYearLabel, this.mPaddingStart * 2, (this.mMonthHeight - (this.mMonthPaint.ascent() + this.mMonthPaint.descent())) / 2.0f, this.mMonthPaint);
    }

    private void ensureFocusedDay() {
        if (this.mHighlightedDay != -1) {
            return;
        }
        int i10 = this.mPreviouslyHighlightedDay;
        if (i10 != -1) {
            this.mHighlightedDay = i10;
            return;
        }
        int i11 = this.mActivatedDay;
        if (i11 != -1) {
            this.mHighlightedDay = i11;
        } else {
            this.mHighlightedDay = 1;
        }
    }

    private int findClosestColumn(Rect rect) {
        if (rect == null) {
            return 3;
        }
        int centerX = rect.centerX() - getPaddingLeft();
        int i10 = this.mCellWidth;
        if (i10 == 0) {
            return 3;
        }
        return COUIPickerMathUtils.isLayoutRtl(this) ? (7 - r3) - 1 : COUIPickerMathUtils.constrain(centerX / i10, 0, 6);
    }

    private int findClosestRow(Rect rect) {
        if (rect == null) {
            return 3;
        }
        int centerY = rect.centerY();
        TextPaint textPaint = this.mDayPaint;
        int i10 = this.mMonthHeight + this.mDayOfWeekHeight;
        int round = Math.round(((int) (centerY - ((i10 + (r2 / 2)) - ((textPaint.ascent() + textPaint.descent()) / 2.0f)))) / this.mDayHeight);
        int findDayOffset = findDayOffset() + this.mDaysInMonth;
        return COUIPickerMathUtils.constrain(round, 0, (findDayOffset / 7) - (findDayOffset % 7 == 0 ? 1 : 0));
    }

    private int findDayOffset() {
        int i10 = this.mDayOfWeekStart;
        int i11 = this.mWeekStart;
        int i12 = i10 - i11;
        return i10 < i11 ? i12 + 7 : i12;
    }

    private int findEndDayOffset(boolean z10) {
        int daysInMonth = ((z10 ? 6 : 5) * 7) - ((getDaysInMonth(this.mMonth, this.mYear) + this.mDayOfWeekStart) - 1);
        if (daysInMonth > 7) {
            return Math.abs(daysInMonth - 14);
        }
        return Math.abs(daysInMonth - 7);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDayAtLocation(int i10, int i11) {
        int i12;
        int paddingTop;
        int paddingLeft = i10 - getPaddingLeft();
        if (paddingLeft < 0 || paddingLeft >= this.mCellWidth * 7 || (paddingTop = i11 - getPaddingTop()) < (i12 = this.mMonthHeight + this.mDayOfWeekHeight) || paddingTop >= this.mPaddedHeight) {
            return -100;
        }
        if (COUIPickerMathUtils.isLayoutRtl(this)) {
            paddingLeft = (this.mCellWidth * 7) - paddingLeft;
        }
        return (((paddingLeft / this.mCellWidth) + (((paddingTop - i12) / (this.mDayHeight + (this.mIsMaxCol ? 0 : this.mDesiredDayPadding))) * 7)) + 1) - findDayOffset();
    }

    private static int getDaysInMonth(int i10, int i11) {
        switch (i10) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 1:
                return i11 % 4 == 0 ? 29 : 28;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    @SuppressLint({"WrongConstant"})
    private void initPaints(Resources resources) {
        int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.calendar_picker_month_text_size);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R$dimen.calendar_picker_day_of_week_text_size);
        int dimensionPixelSize3 = resources.getDimensionPixelSize(R$dimen.calendar_picker_day_text_size);
        int d10 = (int) COUIChangeTextUtil.d(dimensionPixelSize, getContext().getResources().getConfiguration().fontScale);
        int d11 = (int) COUIChangeTextUtil.d(dimensionPixelSize2, getContext().getResources().getConfiguration().fontScale);
        int d12 = (int) COUIChangeTextUtil.d(dimensionPixelSize3, getContext().getResources().getConfiguration().fontScale);
        this.mMonthPaint.setAntiAlias(true);
        this.mMonthPaint.setTextSize(d10);
        this.mMonthPaint.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        this.mMonthPaint.setTextAlign(Paint.Align.CENTER);
        this.mMonthPaint.setStyle(Paint.Style.FILL);
        this.mDayOfWeekPaint.setAntiAlias(true);
        this.mDayOfWeekPaint.setTextSize(d11);
        this.mDayOfWeekPaint.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        this.mDayOfWeekPaint.setTextAlign(Paint.Align.CENTER);
        this.mDayOfWeekPaint.setStyle(Paint.Style.FILL);
        this.mDaySelectorPaint.setAntiAlias(true);
        this.mDaySelectorPaint.setStyle(Paint.Style.FILL);
        this.mOldSelectorPaint.setAntiAlias(true);
        this.mOldSelectorPaint.setStyle(Paint.Style.FILL);
        this.mDayHighlightPaint.setAntiAlias(true);
        this.mDayHighlightPaint.setStyle(Paint.Style.FILL);
        this.mDayHighlightSelectorPaint.setAntiAlias(true);
        this.mDayHighlightSelectorPaint.setStyle(Paint.Style.FILL);
        this.mDayPaint.setAntiAlias(true);
        this.mDayPaint.setTextSize(d12);
        this.mDayPaint.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        this.mDayPaint.setTextAlign(Paint.Align.CENTER);
        this.mDayPaint.setStyle(Paint.Style.FILL);
        Paint paint = new Paint();
        this.mCirclePaint = paint;
        paint.setAntiAlias(true);
        this.mCirclePaint.setStyle(Paint.Style.STROKE);
        this.mCirclePaint.setStrokeWidth(this.mCurrentDayStrokeRadius);
        configAnimator();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDayEnabled(int i10) {
        return i10 >= this.mEnabledDayStart && i10 <= this.mEnabledDayEnd;
    }

    private boolean isFirstDayOfWeek(int i10) {
        return ((findDayOffset() + i10) - 1) % 7 == 0;
    }

    private boolean isLastDayOfWeek(int i10) {
        return (findDayOffset() + i10) % 7 == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isValidDayOfMonth(int i10) {
        return i10 >= 1 && i10 <= this.mDaysInMonth;
    }

    private static boolean isValidDayOfWeek(int i10) {
        return i10 >= 1 && i10 <= 7;
    }

    private static boolean isValidMonth(int i10) {
        return i10 >= 0 && i10 <= 11;
    }

    private boolean moveOneDay(boolean z10) {
        int i10;
        int i11;
        ensureFocusedDay();
        if (z10) {
            if (!isLastDayOfWeek(this.mHighlightedDay) && (i11 = this.mHighlightedDay) < this.mDaysInMonth) {
                this.mHighlightedDay = i11 + 1;
                return true;
            }
        } else if (!isFirstDayOfWeek(this.mHighlightedDay) && (i10 = this.mHighlightedDay) > 1) {
            this.mHighlightedDay = i10 - 1;
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onDayClicked(int i10) {
        if (i10 == this.mActivatedDay) {
            return false;
        }
        this.mIsShowAnimaor = true;
        if (this.mOnDayClickListener != null) {
            Calendar calendar = Calendar.getInstance();
            if (i10 <= 0) {
                int i11 = this.mMonth;
                if (i11 > 0) {
                    int i12 = this.mYear;
                    calendar.set(i12, i11 - 1, getDaysInMonth(i11 - 1, i12) + i10);
                } else {
                    int i13 = this.mYear;
                    calendar.set(i13 - 1, 11, getDaysInMonth(i11, i13 - 1) + i10);
                }
            } else if (i10 > getDaysInMonth(this.mMonth, this.mYear)) {
                int i14 = this.mMonth;
                if (i14 < 11) {
                    int i15 = this.mYear;
                    calendar.set(i15, i14 + 1, i10 - getDaysInMonth(i14, i15));
                } else {
                    int i16 = this.mYear;
                    calendar.set(i16 + 1, 0, i10 - getDaysInMonth(i14, i16));
                }
            } else {
                calendar.set(this.mYear, this.mMonth, i10);
            }
            if (calendar.get(1) < 1900 || calendar.get(1) > 2100) {
                return false;
            }
            this.mOnDayClickListener.onDayClick(this, calendar);
        }
        this.mTouchHelper.sendEventForVirtualView(i10, 1);
        return true;
    }

    private boolean sameDay(int i10, Calendar calendar) {
        return this.mYear == calendar.get(1) && this.mMonth == calendar.get(2) && i10 == calendar.get(5);
    }

    private void updateDayOfWeekLabels() {
        ArrayList arrayList = new ArrayList();
        for (int i10 = 1; i10 < 8; i10++) {
            arrayList.add(DateUtils.getDayOfWeekString(i10, 50));
        }
        for (int i11 = 0; i11 < 7; i11++) {
            this.mDayOfWeekLabels[i11] = (String) arrayList.get(((this.mWeekStart + i11) - 1) % 7);
        }
    }

    private void updateMonthYearLabel() {
        this.mMonthYearLabel = new SimpleDateFormat(DateFormat.getBestDateTimePattern(this.mLocale, MONTH_YEAR_FORMAT), this.mLocale).format(this.mCalendar.getTime());
    }

    @Override // android.view.View
    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return this.mTouchHelper.dispatchHoverEvent(motionEvent) || super.dispatchHoverEvent(motionEvent);
    }

    public boolean getBoundsForDay(int i10, Rect rect) {
        int paddingLeft;
        if (!isValidDayOfMonth(i10)) {
            return false;
        }
        int findDayOffset = (i10 - 1) + findDayOffset();
        int i11 = findDayOffset % 7;
        int i12 = this.mCellWidth;
        if (COUIPickerMathUtils.isLayoutRtl(this)) {
            paddingLeft = (getWidth() - getPaddingRight()) - ((i11 + 1) * i12);
        } else {
            paddingLeft = getPaddingLeft() + (i11 * i12);
        }
        int i13 = findDayOffset / 7;
        int i14 = this.mDayHeight + (this.mIsMaxCol ? 0 : this.mDesiredDayPadding);
        int paddingTop = getPaddingTop() + this.mMonthHeight + this.mDayOfWeekHeight + (i13 * i14);
        rect.set(paddingLeft, paddingTop, i12 + paddingLeft, i14 + paddingTop);
        return true;
    }

    public int getCellWidth() {
        return this.mCellWidth;
    }

    @Override // android.view.View
    public void getFocusedRect(Rect rect) {
        int i10 = this.mHighlightedDay;
        if (i10 > 0) {
            getBoundsForDay(i10, rect);
        } else {
            super.getFocusedRect(rect);
        }
    }

    public int getMonthHeight() {
        return this.mMonthHeight;
    }

    public int getMonthWidth() {
        return this.mMonthWidth;
    }

    public String getMonthYearLabel() {
        return this.mMonthYearLabel;
    }

    public long getTimeMillis() {
        return this.mCalendar.getTimeInMillis();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.translate(getPaddingLeft(), getPaddingTop());
        drawDaysOfWeek(canvas);
        drawDays(canvas);
        canvas.translate(-r0, -r1);
    }

    @Override // android.view.View.OnFocusChangeListener
    public void onFocusChange(View view, boolean z10) {
        if (z10 || this.mIsTouchHighlighted) {
            return;
        }
        this.mPreviouslyHighlightedDay = this.mHighlightedDay;
        this.mHighlightedDay = -1;
        invalidate();
    }

    @Override // android.view.View
    protected void onFocusChanged(boolean z10, int i10, Rect rect) {
        if (z10) {
            int findDayOffset = findDayOffset();
            if (i10 == 17) {
                this.mHighlightedDay = Math.min(this.mDaysInMonth, ((findClosestRow(rect) + 1) * 7) - findDayOffset);
            } else if (i10 == 33) {
                int findClosestColumn = findClosestColumn(rect);
                int i11 = this.mDaysInMonth;
                int i12 = (findClosestColumn - findDayOffset) + (((findDayOffset + i11) / 7) * 7) + 1;
                if (i12 > i11) {
                    i12 -= 7;
                }
                this.mHighlightedDay = i12;
            } else if (i10 == 66) {
                int findClosestRow = findClosestRow(rect);
                this.mHighlightedDay = findClosestRow != 0 ? 1 + ((findClosestRow * 7) - findDayOffset) : 1;
            } else if (i10 == 130) {
                int findClosestColumn2 = (findClosestColumn(rect) - findDayOffset) + 1;
                if (findClosestColumn2 < 1) {
                    findClosestColumn2 += 7;
                }
                this.mHighlightedDay = findClosestColumn2;
            }
            ensureFocusedDay();
            invalidate();
        }
        super.onFocusChanged(z10, i10, rect);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x000f. Please report as an issue. */
    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i10, KeyEvent keyEvent) {
        int i11;
        int keyCode = keyEvent.getKeyCode();
        boolean z10 = false;
        if (keyCode != 61) {
            if (keyCode != 66) {
                switch (keyCode) {
                    case 19:
                        if (keyEvent.hasNoModifiers()) {
                            ensureFocusedDay();
                            int i12 = this.mHighlightedDay;
                            if (i12 > 7) {
                                this.mHighlightedDay = i12 - 7;
                                z10 = true;
                                break;
                            }
                        }
                        break;
                    case 20:
                        if (keyEvent.hasNoModifiers()) {
                            ensureFocusedDay();
                            int i13 = this.mHighlightedDay;
                            if (i13 <= this.mDaysInMonth - 7) {
                                this.mHighlightedDay = i13 + 7;
                                z10 = true;
                                break;
                            }
                        }
                        break;
                    case 21:
                        if (keyEvent.hasNoModifiers()) {
                            z10 = moveOneDay(COUIPickerMathUtils.isLayoutRtl(this));
                            break;
                        }
                        break;
                    case 22:
                        if (keyEvent.hasNoModifiers()) {
                            z10 = moveOneDay(!COUIPickerMathUtils.isLayoutRtl(this));
                            break;
                        }
                        break;
                }
            }
            int i14 = this.mHighlightedDay;
            if (i14 != -1) {
                onDayClicked(i14);
                return true;
            }
        } else {
            if (keyEvent.hasNoModifiers()) {
                i11 = 2;
            } else {
                i11 = keyEvent.hasModifiers(1) ? 1 : 0;
            }
            if (i11 != 0) {
                ViewParent parent = getParent();
                View view = this;
                do {
                    view = view.focusSearch(i11);
                    if (view == null || view == this) {
                        break;
                    }
                } while (view.getParent() == parent);
                if (view != null) {
                    view.requestFocus();
                    return true;
                }
            }
        }
        if (z10) {
            invalidate();
            return true;
        }
        return super.onKeyDown(i10, keyEvent);
    }

    @Override // android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        if (z10) {
            int i14 = i12 - i10;
            int i15 = i13 - i11;
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int paddingRight = getPaddingRight();
            int i16 = (i14 - paddingRight) - paddingLeft;
            int paddingBottom = (i15 - getPaddingBottom()) - paddingTop;
            if (i16 == this.mPaddedWidth || paddingBottom == this.mPaddedHeight || i16 < 0 || paddingBottom < 0) {
                return;
            }
            this.mPaddedWidth = i16;
            this.mPaddedHeight = paddingBottom;
            float measuredHeight = paddingBottom / ((getMeasuredHeight() - paddingTop) - r0);
            this.mMonthHeight = 0;
            this.mMonthWidth = (int) this.mMonthPaint.measureText(this.mMonthYearLabel);
            this.mDayOfWeekHeight = (int) (this.mDesiredDayOfWeekHeight * measuredHeight);
            this.mDayHeight = (int) (this.mDesiredDayHeight * measuredHeight);
            this.mTouchHelper.invalidateRoot();
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        int paddingTop = (this.mDesiredDayHeight * 6) + this.mDesiredDayOfWeekHeight + this.mDesiredMonthHeight + getPaddingTop() + getPaddingBottom();
        int resolveSize = View.resolveSize((this.mDesiredCellWidth * 7) + getPaddingStart() + getPaddingEnd(), i10);
        int resolveSize2 = View.resolveSize(paddingTop, i11);
        this.mCellWidth = ((resolveSize - getPaddingRight()) - getPaddingLeft()) / 7;
        setMeasuredDimension(resolveSize, resolveSize2);
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i10) {
        super.onRtlPropertiesChanged(i10);
        requestLayout();
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x001e, code lost:
    
        if (r7 != 3) goto L20;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int x10 = (int) (motionEvent.getX() + 0.5f);
        int y4 = (int) (motionEvent.getY() + 0.5f);
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1) {
                int dayAtLocation = getDayAtLocation(x10, y4);
                if (dayAtLocation != -100) {
                    onDayClicked(dayAtLocation);
                }
            } else if (action != 2) {
            }
            this.mHighlightedDay = -1;
            this.mIsTouchHighlighted = false;
            invalidate();
        }
        int dayAtLocation2 = getDayAtLocation(x10, y4);
        this.mIsTouchHighlighted = true;
        if (this.mHighlightedDay != dayAtLocation2) {
            this.mHighlightedDay = dayAtLocation2;
            this.mPreviouslyHighlightedDay = dayAtLocation2;
            invalidate();
        }
        return (action == 0 && dayAtLocation2 == -100) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDayHighlightColor(ColorStateList colorStateList) {
        this.mDayHighlightPaint.setColor(colorStateList.getColorForState(COUIPickerMathUtils.getViewState(24), 0));
        invalidate();
    }

    public void setDayOfWeekTextAppearance(int i10) {
        applyTextAppearance(this.mDayOfWeekPaint, i10);
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDayOfWeekTextColor(ColorStateList colorStateList) {
        this.mDayOfWeekPaint.setColor(colorStateList.getColorForState(View.ENABLED_STATE_SET, 0));
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDaySelectorColor(int i10) {
        this.mDaySelectorPaint.setColor(i10);
        this.mOldSelectorPaint.setColor(i10);
        this.mDayHighlightSelectorPaint.setColor(i10);
        this.mDayHighlightSelectorPaint.setAlpha(SELECTED_HIGHLIGHT_ALPHA);
        invalidate();
    }

    public void setDayTextAppearance(int i10) {
        ColorStateList applyTextAppearance = applyTextAppearance(this.mDayPaint, i10);
        if (applyTextAppearance != null) {
            this.mDayTextColor = applyTextAppearance;
        }
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDayTextColor(ColorStateList colorStateList) {
        this.mDayTextColor = colorStateList;
        invalidate();
    }

    public void setFirstDayOfWeek(int i10) {
        if (isValidDayOfWeek(i10)) {
            this.mWeekStart = i10;
        } else {
            this.mWeekStart = this.mCalendar.getFirstDayOfWeek();
        }
        updateDayOfWeekLabels();
        this.mTouchHelper.invalidateRoot();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMonthParams(int i10, int i11, int i12, int i13, int i14, int i15, boolean z10) {
        this.mActivatedDay = i10;
        if (isValidMonth(i11)) {
            this.mMonth = i11;
        }
        this.mYear = i12;
        this.mIsSelectYear = z10;
        this.mCalendar.set(2, this.mMonth);
        this.mCalendar.set(1, this.mYear);
        this.mCalendar.set(5, 1);
        this.mDayOfWeekStart = this.mCalendar.get(7);
        if (isValidDayOfWeek(i13)) {
            this.mWeekStart = i13;
        } else {
            this.mWeekStart = this.mCalendar.getFirstDayOfWeek();
        }
        Calendar calendar = Calendar.getInstance();
        this.mToday = -1;
        this.mDaysInMonth = getDaysInMonth(this.mMonth, this.mYear);
        int i16 = 0;
        while (true) {
            int i17 = this.mDaysInMonth;
            if (i16 < i17) {
                i16++;
                if (sameDay(i16, calendar)) {
                    this.mToday = i16;
                }
            } else {
                int constrain = COUIPickerMathUtils.constrain(i14, 1, i17);
                this.mEnabledDayStart = constrain;
                this.mEnabledDayEnd = COUIPickerMathUtils.constrain(i15, constrain, this.mDaysInMonth);
                updateMonthYearLabel();
                updateDayOfWeekLabels();
                this.mTouchHelper.invalidateRoot();
                invalidate();
                return;
            }
        }
    }

    public void setMonthTextAlpha(int i10) {
        int i11 = this.mInitColor;
        if (Integer.toHexString(i11).length() > 2) {
            this.mMonthPaint.setColor(new ColorStateList(new int[][]{new int[]{R.attr.state_enabled}, new int[0]}, new int[]{new BigInteger(Integer.toHexString((i10 * new BigInteger(Integer.toHexString(i11).substring(0, 2), 16).intValue()) / 255) + Integer.toHexString(i11).substring(2), 16).intValue(), i11}).getColorForState(View.ENABLED_STATE_SET, 0));
            invalidate();
        }
    }

    public void setMonthTextAppearance(int i10) {
        applyTextAppearance(this.mMonthPaint, i10);
        this.mInitColor = this.mMonthPaint.getColor();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMonthTextColor(ColorStateList colorStateList) {
        this.mMonthPaint.setColor(COUIContextUtil.a(getContext(), R$attr.couiColorPrimary));
        invalidate();
    }

    public void setOldDay(int i10, int i11) {
        int i12 = this.mActivatedDay;
        if (i12 == -1 || i12 == i10) {
            return;
        }
        this.mOldSelectDay = i10;
        this.mOldMonth = i11;
    }

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        this.mOnDayClickListener = onDayClickListener;
    }

    public void setSelectedDay(int i10, int i11, int i12) {
        this.mActivatedDay = i10;
        this.mActivatedMonth = i11;
        this.mIsSelectYear = this.mYear == i12;
        this.mTouchHelper.invalidateRoot();
        this.mCircleInAnimator.start();
        this.mCircleOutAnimator.start();
    }

    public COUIDateMonthView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.datePickerStyle);
    }

    public COUIDateMonthView(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public COUIDateMonthView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.mMonthPaint = new TextPaint();
        this.mDayOfWeekPaint = new TextPaint();
        this.mDayPaint = new TextPaint();
        this.mDaySelectorPaint = new Paint();
        this.mOldSelectorPaint = new Paint();
        this.mDayHighlightPaint = new Paint();
        this.mDayHighlightSelectorPaint = new Paint();
        this.mDayOfWeekLabels = new String[7];
        this.mActivatedDay = -1;
        this.mActivatedMonth = -1;
        this.mOldSelectDay = -1;
        this.mOldMonth = -1;
        this.mToday = -1;
        this.mWeekStart = 1;
        this.mEnabledDayStart = 1;
        this.mEnabledDayEnd = 31;
        this.mHighlightedDay = -1;
        this.mPreviouslyHighlightedDay = -1;
        this.mIsTouchHighlighted = false;
        this.mContext = context;
        Resources resources = context.getResources();
        this.mDesiredMonthHeight = resources.getDimensionPixelSize(R$dimen.calendar_picker_month_height);
        this.mPaddingStart = resources.getDimensionPixelSize(R$dimen.calendar_picker_month_padding_start);
        this.mDesiredDayOfWeekHeight = resources.getDimensionPixelSize(R$dimen.calendar_picker_day_of_week_height);
        this.mDesiredDayHeight = resources.getDimensionPixelSize(R$dimen.calendar_picker_day_height);
        this.mDesiredDayPadding = resources.getDimensionPixelSize(R$dimen.calendar_picker_day_min_col_padding);
        this.mDesiredCellWidth = resources.getDimensionPixelSize(R$dimen.calendar_picker_day_width);
        this.mDayCircleRadius = resources.getDimensionPixelSize(R$dimen.calendar_picker_current_day_radius);
        this.mCurrentDayStrokeRadius = resources.getDimensionPixelSize(R$dimen.calendar_picker_current_day_stroke_radius);
        this.mDaySelectRadius = this.mDayCircleRadius;
        this.mHintColor = COUIContextUtil.a(context, R$attr.couiColorDisabledNeutral);
        this.mPrimaryColor = COUIContextUtil.a(context, R$attr.couiColorPrimary);
        this.mBackgroundColor = COUIContextUtil.a(context, R$attr.couiColorBackground);
        MonthViewTouchHelper monthViewTouchHelper = new MonthViewTouchHelper(this);
        this.mTouchHelper = monthViewTouchHelper;
        ViewCompat.l0(this, monthViewTouchHelper);
        setImportantForAccessibility(1);
        Locale locale = resources.getConfiguration().locale;
        this.mLocale = locale;
        this.mCalendar = Calendar.getInstance(locale);
        this.mDayFormatter = NumberFormat.getIntegerInstance(locale);
        updateMonthYearLabel();
        updateDayOfWeekLabels();
        initPaints(resources);
    }
}
