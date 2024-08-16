package com.coui.appcompat.calendar;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.viewpager.widget.ViewPager;
import com.coui.appcompat.calendar.COUICalendarDayPagerAdapter;
import com.support.appcompat.R$attr;
import com.support.control.R$id;
import com.support.control.R$layout;
import com.support.control.R$style;
import com.support.control.R$styleable;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import v1.COUIContextUtil;

/* loaded from: classes.dex */
public class COUICalendarDayPickerView extends ViewGroup {
    private static final int DEFAULT_END_YEAR = 2100;
    private static final int DEFAULT_START_YEAR = 1900;
    private final COUICalendarDayPagerAdapter mAdapter;
    private boolean mClick;
    private boolean mHasNext;
    private boolean mHasPrev;
    private boolean mInit;
    private final Calendar mMaxDate;
    private final Calendar mMinDate;
    private TextView mMonthView;
    private ImageButton mNextButton;
    private final View.OnClickListener mOnClickListener;
    private OnDaySelectedListener mOnDaySelectedListener;
    private final ViewPager.i mOnPageChangedListener;
    private ImageButton mPrevButton;
    private COUICalendarViewPagerScroller mScroller;
    private final Calendar mSelectedDay;
    private Calendar mTempCalendar;
    private final ViewPager mViewPager;
    private static final int DEFAULT_LAYOUT = R$layout.coui_calendar_picker_content_material;
    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

    /* loaded from: classes.dex */
    public interface OnDaySelectedListener {
        void onDaySelected(COUICalendarDayPickerView cOUICalendarDayPickerView, Calendar calendar);
    }

    public COUICalendarDayPickerView(Context context) {
        this(context, null);
    }

    private void configViewPager() {
        this.mViewPager.setAdapter(this.mAdapter);
        this.mViewPager.setOnPageChangeListener(this.mOnPageChangedListener);
        try {
            Field declaredField = ViewPager.class.getDeclaredField("mScroller");
            declaredField.setAccessible(true);
            COUICalendarViewPagerScroller cOUICalendarViewPagerScroller = new COUICalendarViewPagerScroller(this.mViewPager.getContext());
            this.mScroller = cOUICalendarViewPagerScroller;
            declaredField.set(this.mViewPager, cOUICalendarViewPagerScroller);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
    }

    private int getDiffMonths(Calendar calendar, Calendar calendar2) {
        return (calendar2.get(2) - calendar.get(2)) + ((calendar2.get(1) - calendar.get(1)) * 12);
    }

    private int getPositionFromDay(long j10) {
        return COUIPickerMathUtils.constrain(getDiffMonths(this.mMinDate, getTempCalendarForTime(j10)), 0, getDiffMonths(this.mMinDate, this.mMaxDate));
    }

    private Calendar getTempCalendarForTime(long j10) {
        if (this.mTempCalendar == null) {
            this.mTempCalendar = Calendar.getInstance();
        }
        this.mTempCalendar.setTimeInMillis(j10);
        return this.mTempCalendar;
    }

    public static boolean parseDate(String str, Calendar calendar) {
        if (str != null && !str.isEmpty()) {
            try {
                calendar.setTime(DATE_FORMATTER.parse(str));
                return true;
            } catch (ParseException unused) {
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateButtonVisibility(int i10) {
        boolean z10 = true;
        this.mHasPrev = i10 > 0;
        if (i10 >= this.mAdapter.getCount() - 1 && !this.mInit) {
            z10 = false;
        }
        this.mHasNext = z10;
        ImageButton imageButton = this.mPrevButton;
        if (imageButton != null) {
            imageButton.setVisibility(this.mHasPrev ? 0 : 4);
        }
        ImageButton imageButton2 = this.mNextButton;
        if (imageButton2 != null) {
            imageButton2.setVisibility(this.mHasNext ? 0 : 4);
        }
        this.mInit = false;
    }

    public CharSequence getAdapterTitle() {
        return this.mAdapter.getPageTitle(0);
    }

    public boolean getBoundsForDate(long j10, Rect rect) {
        if (getPositionFromDay(j10) != this.mViewPager.getCurrentItem()) {
            return false;
        }
        this.mTempCalendar.setTimeInMillis(j10);
        return this.mAdapter.getBoundsForDate(this.mTempCalendar, rect);
    }

    public long getCurrentTimeMillis() {
        if (this.mAdapter.getCurrentView().size() > 1) {
            return this.mAdapter.getCurrentView().get(1).getTimeMillis();
        }
        return 0L;
    }

    public long getDate() {
        return this.mSelectedDay.getTimeInMillis();
    }

    public int getDayOfWeekTextAppearance() {
        return this.mAdapter.getDayOfWeekTextAppearance();
    }

    public int getDayTextAppearance() {
        return this.mAdapter.getDayTextAppearance();
    }

    public int getFirstDayOfWeek() {
        return this.mAdapter.getFirstDayOfWeek();
    }

    public long getMaxDate() {
        return this.mMaxDate.getTimeInMillis();
    }

    public long getMinDate() {
        return this.mMinDate.getTimeInMillis();
    }

    public int getMostVisiblePosition() {
        return this.mViewPager.getCurrentItem();
    }

    public boolean hasNext() {
        return this.mHasNext;
    }

    public boolean hasPre() {
        return this.mHasPrev;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        this.mViewPager.layout(0, 0, i12 - i10, i13 - i11);
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        ViewPager viewPager = this.mViewPager;
        measureChild(viewPager, i10, i11);
        setMeasuredDimension(viewPager.getMeasuredWidthAndState(), viewPager.getMeasuredHeightAndState());
    }

    public void onRangeChanged() {
        this.mAdapter.setRange(this.mMinDate, this.mMaxDate);
        setDate(this.mSelectedDay.getTimeInMillis(), false, false);
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i10) {
        super.onRtlPropertiesChanged(i10);
        requestLayout();
    }

    public void setClick(boolean z10) {
        this.mClick = z10;
    }

    public void setDate(long j10) {
        setDate(j10, false);
    }

    public void setDayOfWeekTextAppearance(int i10) {
        this.mAdapter.setDayOfWeekTextAppearance(i10);
    }

    public void setDayTextAppearance(int i10) {
        this.mAdapter.setDayTextAppearance(i10);
    }

    public void setFirstDayOfWeek(int i10) {
        this.mAdapter.setFirstDayOfWeek(i10);
    }

    public void setMaxDate(long j10) {
        this.mMaxDate.setTimeInMillis(j10);
        onRangeChanged();
    }

    public void setMinDate(long j10) {
        this.mMinDate.setTimeInMillis(j10);
        onRangeChanged();
    }

    public void setMonthView(TextView textView) {
        this.mMonthView = textView;
    }

    public void setNextButton(ImageButton imageButton) {
        this.mNextButton = imageButton;
        imageButton.setOnClickListener(this.mOnClickListener);
    }

    public void setOnDaySelectedListener(OnDaySelectedListener onDaySelectedListener) {
        this.mOnDaySelectedListener = onDaySelectedListener;
    }

    public void setPosition(int i10) {
        this.mViewPager.setCurrentItem(i10, false);
    }

    public void setPrevButton(ImageButton imageButton) {
        this.mPrevButton = imageButton;
        imageButton.setOnClickListener(this.mOnClickListener);
    }

    public COUICalendarDayPickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.calendarViewStyle);
    }

    public void setDate(long j10, boolean z10) {
        setDate(j10, z10, true);
    }

    public COUICalendarDayPickerView(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0040  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setDate(long j10, boolean z10, boolean z11) {
        boolean z12;
        int positionFromDay;
        if (j10 < this.mMinDate.getTimeInMillis()) {
            j10 = this.mMinDate.getTimeInMillis();
        } else if (j10 > this.mMaxDate.getTimeInMillis()) {
            j10 = this.mMaxDate.getTimeInMillis();
        } else {
            z12 = false;
            getTempCalendarForTime(j10);
            if (!z11 || z12) {
                this.mSelectedDay.setTimeInMillis(j10);
            }
            setClick(true);
            positionFromDay = getPositionFromDay(j10);
            if (positionFromDay != this.mViewPager.getCurrentItem()) {
                this.mViewPager.setCurrentItem(positionFromDay, z10);
            }
            this.mAdapter.setSelectedDay(this.mTempCalendar);
        }
        z12 = true;
        getTempCalendarForTime(j10);
        if (!z11) {
        }
        this.mSelectedDay.setTimeInMillis(j10);
        setClick(true);
        positionFromDay = getPositionFromDay(j10);
        if (positionFromDay != this.mViewPager.getCurrentItem()) {
        }
        this.mAdapter.setSelectedDay(this.mTempCalendar);
    }

    public COUICalendarDayPickerView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, 0);
        this.mSelectedDay = Calendar.getInstance();
        this.mMinDate = Calendar.getInstance();
        this.mMaxDate = Calendar.getInstance();
        this.mHasNext = true;
        this.mInit = true;
        this.mOnPageChangedListener = new ViewPager.i() { // from class: com.coui.appcompat.calendar.COUICalendarDayPickerView.2
            private int mPosition;

            @Override // androidx.viewpager.widget.ViewPager.i
            public void onPageScrollStateChanged(int i12) {
            }

            @Override // androidx.viewpager.widget.ViewPager.i
            public void onPageScrolled(int i12, float f10, int i13) {
                double d10 = f10;
                if (d10 > 0.99d || d10 < 0.01d) {
                    return;
                }
                COUICalendarDayPickerView.this.mClick = false;
            }

            @Override // androidx.viewpager.widget.ViewPager.i
            public void onPageSelected(int i12) {
                ArrayList<COUIDateMonthView> currentView = COUICalendarDayPickerView.this.mAdapter.getCurrentView();
                if (currentView.size() >= 3) {
                    if (COUICalendarDayPickerView.this.mClick) {
                        COUIDateMonthView cOUIDateMonthView = currentView.get(1);
                        if (COUICalendarDayPickerView.this.mMonthView != null) {
                            COUICalendarDayPickerView.this.mMonthView.setText(cOUIDateMonthView.getMonthYearLabel());
                        }
                    } else {
                        for (int i13 = 0; i13 < currentView.size(); i13++) {
                            COUIDateMonthView cOUIDateMonthView2 = currentView.get(i13);
                            if (((i13 == 0 && i12 - this.mPosition <= 0) || (i13 == 2 && i12 - this.mPosition > 0)) && COUICalendarDayPickerView.this.mMonthView != null) {
                                COUICalendarDayPickerView.this.mMonthView.setText(cOUIDateMonthView2.getMonthYearLabel());
                            }
                        }
                    }
                } else if (currentView.size() == 2) {
                    COUIDateMonthView cOUIDateMonthView3 = currentView.get(!COUICalendarDayPickerView.this.mClick ? 1 : 0);
                    if (cOUIDateMonthView3.getMonthYearLabel().contains(String.valueOf(COUICalendarDayPickerView.this.mMaxDate.get(1)))) {
                        cOUIDateMonthView3 = currentView.get(COUICalendarDayPickerView.this.mClick ? 1 : 0);
                    }
                    if (COUICalendarDayPickerView.this.mMonthView != null) {
                        COUICalendarDayPickerView.this.mMonthView.setText(cOUIDateMonthView3.getMonthYearLabel());
                    }
                }
                COUICalendarDayPickerView.this.updateButtonVisibility(i12);
                this.mPosition = i12;
            }
        };
        this.mOnClickListener = new View.OnClickListener() { // from class: com.coui.appcompat.calendar.COUICalendarDayPickerView.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                char c10;
                if (view == COUICalendarDayPickerView.this.mPrevButton) {
                    c10 = 65535;
                } else if (view != COUICalendarDayPickerView.this.mNextButton) {
                    return;
                } else {
                    c10 = 1;
                }
                COUICalendarDayPickerView.this.mClick = true;
                if (c10 == 65535) {
                    COUICalendarDayPickerView.this.mViewPager.arrowScroll(17);
                } else {
                    COUICalendarDayPickerView.this.mViewPager.arrowScroll(66);
                }
                COUICalendarDayPickerView.this.mScroller.setmDuration(300);
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUICalendarView, i10, 0);
        int i12 = obtainStyledAttributes.getInt(R$styleable.COUICalendarView_android_firstDayOfWeek, Calendar.getInstance().get(7));
        String string = obtainStyledAttributes.getString(R$styleable.COUICalendarView_android_minDate);
        String string2 = obtainStyledAttributes.getString(R$styleable.COUICalendarView_android_maxDate);
        int i13 = R$style.TextAppearance_Material_Widget_Calendar_Month;
        int i14 = R$style.TextAppearance_Material_Widget_Calendar_DayOfWeek;
        int i15 = R$style.TextAppearance_Material_Widget_Calendar_Day;
        int b10 = COUIContextUtil.b(context, R$attr.couiColorPrimary, 0);
        obtainStyledAttributes.recycle();
        COUICalendarDayPagerAdapter cOUICalendarDayPagerAdapter = new COUICalendarDayPagerAdapter(context, R$layout.coui_calendar_picker_month_item_material, R$id.month_view);
        this.mAdapter = cOUICalendarDayPagerAdapter;
        cOUICalendarDayPagerAdapter.setMonthTextAppearance(i13);
        cOUICalendarDayPagerAdapter.setDayOfWeekTextAppearance(i14);
        cOUICalendarDayPagerAdapter.setDayTextAppearance(i15);
        cOUICalendarDayPagerAdapter.setDaySelectorColor(b10);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(DEFAULT_LAYOUT, (ViewGroup) this, false);
        while (viewGroup.getChildCount() > 0) {
            View childAt = viewGroup.getChildAt(0);
            viewGroup.removeViewAt(0);
            addView(childAt);
        }
        this.mViewPager = (ViewPager) findViewById(R$id.day_picker_view_pager);
        configViewPager();
        Calendar calendar = Calendar.getInstance();
        if (!parseDate(string, calendar)) {
            calendar.set(1900, 0, 1);
        }
        long timeInMillis = calendar.getTimeInMillis();
        if (!parseDate(string2, calendar)) {
            calendar.set(2100, 11, 31);
        }
        long timeInMillis2 = calendar.getTimeInMillis();
        if (timeInMillis2 >= timeInMillis) {
            long constrain = COUIPickerMathUtils.constrain(System.currentTimeMillis(), timeInMillis, timeInMillis2);
            setFirstDayOfWeek(i12);
            setMinDate(timeInMillis);
            setMaxDate(timeInMillis2);
            setDate(constrain, false);
            this.mAdapter.setOnDaySelectedListener(new COUICalendarDayPagerAdapter.OnDaySelectedListener() { // from class: com.coui.appcompat.calendar.COUICalendarDayPickerView.1
                @Override // com.coui.appcompat.calendar.COUICalendarDayPagerAdapter.OnDaySelectedListener
                public void onDaySelected(COUICalendarDayPagerAdapter cOUICalendarDayPagerAdapter2, Calendar calendar2) {
                    if (COUICalendarDayPickerView.this.mOnDaySelectedListener != null) {
                        COUICalendarDayPickerView.this.mOnDaySelectedListener.onDaySelected(COUICalendarDayPickerView.this, calendar2);
                    }
                }
            });
            return;
        }
        throw new IllegalArgumentException("maxDate must be >= minDate");
    }
}
