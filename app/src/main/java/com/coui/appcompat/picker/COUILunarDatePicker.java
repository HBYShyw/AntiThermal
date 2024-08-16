package com.coui.appcompat.picker;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import c2.COUILunarUtil;
import com.coui.appcompat.picker.COUINumberPicker;
import com.support.control.R$array;
import com.support.control.R$attr;
import com.support.control.R$dimen;
import com.support.control.R$id;
import com.support.control.R$layout;
import com.support.control.R$string;
import com.support.control.R$style;
import com.support.control.R$styleable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUILunarDatePicker extends FrameLayout {

    /* renamed from: w, reason: collision with root package name */
    private static String f6725w;

    /* renamed from: e, reason: collision with root package name */
    private final LinearLayout f6728e;

    /* renamed from: f, reason: collision with root package name */
    private final COUINumberPicker f6729f;

    /* renamed from: g, reason: collision with root package name */
    private final COUINumberPicker f6730g;

    /* renamed from: h, reason: collision with root package name */
    private final COUINumberPicker f6731h;

    /* renamed from: i, reason: collision with root package name */
    private Locale f6732i;

    /* renamed from: j, reason: collision with root package name */
    private d f6733j;

    /* renamed from: k, reason: collision with root package name */
    private String[] f6734k;

    /* renamed from: l, reason: collision with root package name */
    private int f6735l;

    /* renamed from: m, reason: collision with root package name */
    private c f6736m;

    /* renamed from: n, reason: collision with root package name */
    private c f6737n;

    /* renamed from: o, reason: collision with root package name */
    private int f6738o;

    /* renamed from: p, reason: collision with root package name */
    private int f6739p;

    /* renamed from: q, reason: collision with root package name */
    private int f6740q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f6741r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f6742s;

    /* renamed from: t, reason: collision with root package name */
    private int f6743t;

    /* renamed from: u, reason: collision with root package name */
    private static final String f6723u = COUILunarDatePicker.class.getSimpleName();

    /* renamed from: v, reason: collision with root package name */
    private static final String[] f6724v = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};

    /* renamed from: x, reason: collision with root package name */
    private static Calendar f6726x = Calendar.getInstance();

    /* renamed from: y, reason: collision with root package name */
    private static Calendar f6727y = Calendar.getInstance();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        private final int f6744e;

        /* renamed from: f, reason: collision with root package name */
        private final int f6745f;

        /* renamed from: g, reason: collision with root package name */
        private final int f6746g;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<SavedState> {
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
            parcel.writeInt(this.f6744e);
            parcel.writeInt(this.f6745f);
            parcel.writeInt(this.f6746g);
        }

        /* synthetic */ SavedState(Parcelable parcelable, int i10, int i11, int i12, a aVar) {
            this(parcelable, i10, i11, i12);
        }

        private SavedState(Parcelable parcelable, int i10, int i11, int i12) {
            super(parcelable);
            this.f6744e = i10;
            this.f6745f = i11;
            this.f6746g = i12;
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.f6744e = parcel.readInt();
            this.f6745f = parcel.readInt();
            this.f6746g = parcel.readInt();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements COUINumberPicker.f {
        a() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.f
        public void a(COUINumberPicker cOUINumberPicker, int i10, int i11) {
            COUILunarDatePicker.this.f6736m.o(COUILunarDatePicker.this.f6737n);
            COUILunarUtil.a(COUILunarDatePicker.this.f6736m.i(1), COUILunarDatePicker.this.f6736m.i(2) + 1, COUILunarDatePicker.this.f6736m.i(5));
            if (cOUINumberPicker == COUILunarDatePicker.this.f6729f) {
                COUILunarDatePicker.this.f6736m.f(5, i10, i11);
            } else if (cOUINumberPicker == COUILunarDatePicker.this.f6730g) {
                COUILunarDatePicker.this.f6736m.f(2, i10, i11);
            } else if (cOUINumberPicker == COUILunarDatePicker.this.f6731h) {
                COUILunarDatePicker.this.f6736m.f(1, i10, i11);
            } else {
                throw new IllegalArgumentException();
            }
            COUILunarDatePicker cOUILunarDatePicker = COUILunarDatePicker.this;
            cOUILunarDatePicker.setDate(cOUILunarDatePicker.f6736m);
            COUILunarDatePicker.this.t();
            COUILunarDatePicker.this.s();
            COUILunarDatePicker.this.q();
        }
    }

    /* loaded from: classes.dex */
    class b implements COUINumberPicker.e {
        b() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.e
        public void a() {
            COUILunarDatePicker.this.sendAccessibilityEvent(4);
        }
    }

    /* loaded from: classes.dex */
    public interface d {
        void a(COUILunarDatePicker cOUILunarDatePicker, int i10, int i11, int i12);
    }

    static {
        f6726x.set(1910, 2, 10, 0, 0);
        f6727y.set(2036, 11, 31, 23, 59);
    }

    public COUILunarDatePicker(Context context) {
        this(context, null);
    }

    private void j() {
        this.f6737n.g(f6726x, f6727y);
    }

    private c k(c cVar, Locale locale) {
        if (cVar == null) {
            return new c(locale);
        }
        c cVar2 = new c(locale);
        if (!cVar.f6755g) {
            cVar2.n(cVar.j());
        } else {
            cVar2.o(cVar);
        }
        return cVar2;
    }

    private Calendar l(Calendar calendar, Locale locale) {
        if (calendar == null) {
            return Calendar.getInstance(locale);
        }
        long timeInMillis = calendar.getTimeInMillis();
        Calendar calendar2 = Calendar.getInstance(locale);
        calendar2.setTimeInMillis(timeInMillis);
        return calendar2;
    }

    private static String m(int i10, int i11, int i12, int i13) {
        if (i11 <= 0) {
            return "";
        }
        if (i10 != Integer.MIN_VALUE) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(i10);
            sb2.append("年");
            sb2.append(i13 == 0 ? f6725w : "");
            sb2.append(f6724v[i11 - 1]);
            sb2.append("月");
            sb2.append(COUILunarUtil.c(i12));
            return sb2.toString();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(i13 == 0 ? f6725w : "");
        sb3.append(f6724v[i11 - 1]);
        sb3.append("月");
        sb3.append(COUILunarUtil.c(i12));
        return sb3.toString();
    }

    private static String n(c cVar) {
        int[] a10 = COUILunarUtil.a(cVar.i(1), cVar.i(2) + 1, cVar.i(5));
        return m(a10[0], a10[1], a10[2], a10[3]);
    }

    private void p(View view, int i10, int i11) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        view.measure(FrameLayout.getChildMeasureSpec(i10, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin, marginLayoutParams.width), FrameLayout.getChildMeasureSpec(i11, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, marginLayoutParams.height));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q() {
        d dVar = this.f6733j;
        if (dVar != null) {
            dVar.a(this, getYear(), getMonth(), getDayOfMonth());
        }
    }

    private void r(int i10, int i11, int i12) {
        this.f6737n.l(i10, i11, i12);
        j();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s() {
    }

    private void setCurrentLocale(Locale locale) {
        if (locale.equals(this.f6732i)) {
            return;
        }
        this.f6732i = locale;
        this.f6736m = k(this.f6736m, locale);
        f6726x = l(f6726x, locale);
        f6727y = l(f6727y, locale);
        this.f6737n = k(this.f6737n, locale);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDate(c cVar) {
        this.f6737n.o(cVar);
        j();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x011a  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x01bd A[LOOP:1: B:34:0x01bb->B:35:0x01bd, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00b2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void t() {
        boolean z10;
        int f10;
        int maxValue;
        int i10;
        int i11 = this.f6737n.i(1);
        int[] a10 = COUILunarUtil.a(i11, this.f6737n.i(2) + 1, this.f6737n.i(5));
        int k10 = COUILunarUtil.k(a10[0]);
        int i12 = a10[1];
        String n10 = n(this.f6737n);
        if (k10 == 0 || ((i12 < k10 && k10 != 0) || (i12 == k10 && !n10.contains(f6725w)))) {
            i12--;
        }
        if (i11 == Integer.MIN_VALUE && a10[3] == 0) {
            i12 += 12;
        }
        if (i11 == Integer.MIN_VALUE) {
            this.f6735l = 24;
        } else {
            if (k10 != 0) {
                this.f6735l = 13;
                z10 = true;
                f10 = COUILunarUtil.f(a10[0], a10[1]);
                if (k10 != 0 && i12 == k10 && n10.contains(f6725w)) {
                    f10 = COUILunarUtil.g(a10[0]);
                }
                if (!this.f6737n.e(f6726x)) {
                    this.f6729f.setDisplayedValues(null);
                    this.f6729f.setMinValue(a10[2]);
                    this.f6729f.setMaxValue(f10);
                    this.f6729f.setWrapSelectorWheel(false);
                    this.f6730g.setDisplayedValues(null);
                    this.f6730g.setMinValue(i12);
                    this.f6730g.setMaxValue(this.f6735l - 1);
                    this.f6730g.setWrapSelectorWheel(false);
                } else if (this.f6737n.c(f6727y)) {
                    this.f6729f.setDisplayedValues(null);
                    this.f6729f.setMinValue(1);
                    this.f6729f.setMaxValue(a10[2]);
                    this.f6729f.setWrapSelectorWheel(false);
                    this.f6730g.setDisplayedValues(null);
                    this.f6730g.setMinValue(0);
                    this.f6730g.setMaxValue(i12);
                    this.f6730g.setWrapSelectorWheel(false);
                } else {
                    this.f6729f.setDisplayedValues(null);
                    this.f6729f.setMinValue(1);
                    this.f6729f.setMaxValue(f10);
                    this.f6729f.setWrapSelectorWheel(true);
                    this.f6730g.setDisplayedValues(null);
                    this.f6730g.setMinValue(0);
                    this.f6730g.setMaxValue(this.f6735l - 1);
                    this.f6730g.setWrapSelectorWheel(true);
                }
                int i13 = this.f6735l;
                String[] strArr = new String[i13];
                String[] strArr2 = new String[i13];
                if (i11 != Integer.MIN_VALUE) {
                    for (int i14 = 0; i14 < 24; i14++) {
                        if (i14 < 12) {
                            strArr[i14] = this.f6734k[i14];
                        } else {
                            strArr[i14] = f6725w + this.f6734k[i14 - 12];
                        }
                    }
                } else if (z10) {
                    int i15 = 0;
                    while (i15 < k10) {
                        strArr2[i15] = this.f6734k[i15];
                        i15++;
                    }
                    strArr2[k10] = f6725w + this.f6734k[k10 - 1];
                    for (int i16 = i15 + 1; i16 < 13; i16++) {
                        strArr2[i16] = this.f6734k[i16 - 1];
                    }
                    strArr = (String[]) Arrays.copyOfRange(strArr2, this.f6730g.getMinValue(), this.f6730g.getMaxValue() + 1);
                } else {
                    strArr = (String[]) Arrays.copyOfRange(this.f6734k, this.f6730g.getMinValue(), this.f6730g.getMaxValue() + 1);
                }
                this.f6730g.setDisplayedValues(strArr);
                maxValue = this.f6729f.getMaxValue();
                int minValue = this.f6729f.getMinValue();
                String[] strArr3 = new String[(maxValue - minValue) + 1];
                for (i10 = minValue; i10 <= maxValue; i10++) {
                    strArr3[i10 - minValue] = COUILunarUtil.c(i10);
                }
                this.f6729f.setDisplayedValues(strArr3);
                int[] a11 = COUILunarUtil.a(f6726x.get(1), f6726x.get(2) + 1, f6726x.get(5));
                int i17 = f6727y.get(1);
                int i18 = f6727y.get(2) + 1;
                int[] a12 = COUILunarUtil.a(i17, i18, i18);
                this.f6731h.setMinValue(a11[0]);
                this.f6731h.setMaxValue(a12[0]);
                this.f6731h.setWrapSelectorWheel(true);
                this.f6731h.setValue(a10[0]);
                this.f6730g.setValue(i12);
                this.f6729f.setValue(a10[2]);
            }
            this.f6735l = 12;
        }
        z10 = false;
        f10 = COUILunarUtil.f(a10[0], a10[1]);
        if (k10 != 0) {
            f10 = COUILunarUtil.g(a10[0]);
        }
        if (!this.f6737n.e(f6726x)) {
        }
        int i132 = this.f6735l;
        String[] strArr4 = new String[i132];
        String[] strArr22 = new String[i132];
        if (i11 != Integer.MIN_VALUE) {
        }
        this.f6730g.setDisplayedValues(strArr4);
        maxValue = this.f6729f.getMaxValue();
        int minValue2 = this.f6729f.getMinValue();
        String[] strArr32 = new String[(maxValue - minValue2) + 1];
        while (i10 <= maxValue) {
        }
        this.f6729f.setDisplayedValues(strArr32);
        int[] a112 = COUILunarUtil.a(f6726x.get(1), f6726x.get(2) + 1, f6726x.get(5));
        int i172 = f6727y.get(1);
        int i182 = f6727y.get(2) + 1;
        int[] a122 = COUILunarUtil.a(i172, i182, i182);
        this.f6731h.setMinValue(a112[0]);
        this.f6731h.setMaxValue(a122[0]);
        this.f6731h.setWrapSelectorWheel(true);
        this.f6731h.setValue(a10[0]);
        this.f6730g.setValue(i12);
        this.f6729f.setValue(a10[2]);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(this.f6729f.getBackgroundColor());
        canvas.drawRect(this.f6739p, (int) ((getHeight() / 2.0f) - this.f6738o), getWidth() - this.f6739p, r0 + this.f6740q, paint);
        canvas.drawRect(this.f6739p, (int) ((getHeight() / 2.0f) + this.f6738o), getWidth() - this.f6739p, r0 + this.f6740q, paint);
        super.dispatchDraw(canvas);
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        onPopulateAccessibilityEvent(accessibilityEvent);
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        dispatchThawSelfOnly(sparseArray);
    }

    public CalendarView getCalendarView() {
        return null;
    }

    public boolean getCalendarViewShown() {
        return false;
    }

    public int getDayOfMonth() {
        return this.f6737n.i(5);
    }

    public int getLeapMonth() {
        return COUILunarUtil.k(this.f6737n.i(1));
    }

    public int[] getLunarDate() {
        return COUILunarUtil.a(this.f6737n.i(1), this.f6737n.i(2) + 1, this.f6737n.i(5));
    }

    public long getMaxDate() {
        return f6727y.getTimeInMillis();
    }

    public long getMinDate() {
        return f6726x.getTimeInMillis();
    }

    public int getMonth() {
        return this.f6737n.i(2);
    }

    public d getOnDateChangedListener() {
        return this.f6733j;
    }

    public boolean getSpinnersShown() {
        return this.f6728e.isShown();
    }

    public int getYear() {
        return this.f6737n.i(1);
    }

    @Override // android.view.View
    public boolean isEnabled() {
        return this.f6741r;
    }

    public boolean isLayoutRtl() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
    }

    public void o(int i10, int i11, int i12, d dVar) {
        r(i10, i11, i12);
        t();
        s();
        this.f6733j = dVar;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setCurrentLocale(configuration.locale);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        int mode = View.MeasureSpec.getMode(i10);
        int size = View.MeasureSpec.getSize(i10);
        int i12 = this.f6743t;
        if (i12 > 0 && size > i12) {
            size = i12;
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, mode);
        this.f6729f.z();
        this.f6730g.z();
        this.f6731h.z();
        p(this.f6729f, i10, i11);
        p(this.f6730g, i10, i11);
        p(this.f6731h, i10, i11);
        int measuredWidth = (((size - this.f6729f.getMeasuredWidth()) - this.f6730g.getMeasuredWidth()) - this.f6731h.getMeasuredWidth()) / 2;
        int childCount = this.f6728e.getChildCount() - 1;
        if (this.f6728e.getChildAt(0) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.f6728e.getChildAt(0)).setNumberPickerPaddingLeft(measuredWidth);
        }
        if (this.f6728e.getChildAt(childCount) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.f6728e.getChildAt(childCount)).setNumberPickerPaddingRight(measuredWidth);
        }
        super.onMeasure(makeMeasureSpec, i11);
    }

    @Override // android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.getText().add(n(this.f6737n));
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        r(savedState.f6744e, savedState.f6745f, savedState.f6746g);
        t();
        s();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), getYear(), getMonth(), getDayOfMonth(), null);
    }

    public void setCalendarViewShown(boolean z10) {
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        if (this.f6741r == z10) {
            return;
        }
        super.setEnabled(z10);
        this.f6729f.setEnabled(z10);
        this.f6730g.setEnabled(z10);
        this.f6731h.setEnabled(z10);
        this.f6741r = z10;
    }

    public void setMaxDate(long j10) {
        this.f6736m.n(j10);
        if (this.f6736m.i(1) == f6727y.get(1) && this.f6736m.i(6) != f6727y.get(6)) {
            Log.w(f6723u, "setMaxDate failed!:" + this.f6736m.i(1) + "<->" + f6727y.get(1) + ":" + this.f6736m.i(6) + "<->" + f6727y.get(6));
            return;
        }
        f6727y.setTimeInMillis(j10);
        if (this.f6737n.b(f6727y)) {
            this.f6737n.n(f6727y.getTimeInMillis());
            s();
        }
        t();
    }

    public void setMinDate(long j10) {
        this.f6736m.n(j10);
        if (this.f6736m.i(1) == f6726x.get(1) && this.f6736m.i(6) != f6726x.get(6)) {
            Log.w(f6723u, "setMinDate failed!:" + this.f6736m.i(1) + "<->" + f6726x.get(1) + ":" + this.f6736m.i(6) + "<->" + f6726x.get(6));
            return;
        }
        f6726x.setTimeInMillis(j10);
        if (this.f6737n.d(f6726x)) {
            this.f6737n.n(f6726x.getTimeInMillis());
            s();
        }
        t();
    }

    public void setNormalTextColor(int i10) {
        COUINumberPicker cOUINumberPicker = this.f6729f;
        if (cOUINumberPicker != null) {
            cOUINumberPicker.setNormalTextColor(i10);
        }
        COUINumberPicker cOUINumberPicker2 = this.f6730g;
        if (cOUINumberPicker2 != null) {
            cOUINumberPicker2.setNormalTextColor(i10);
        }
        COUINumberPicker cOUINumberPicker3 = this.f6731h;
        if (cOUINumberPicker3 != null) {
            cOUINumberPicker3.setNormalTextColor(i10);
        }
    }

    public void setOnDateChangedListener(d dVar) {
        this.f6733j = dVar;
    }

    public void setSpinnersShown(boolean z10) {
        this.f6728e.setVisibility(z10 ? 0 : 8);
    }

    public void setVibrateIntensity(float f10) {
        this.f6729f.setVibrateIntensity(f10);
        this.f6730g.setVibrateIntensity(f10);
        this.f6731h.setVibrateIntensity(f10);
    }

    public void setVibrateLevel(int i10) {
        this.f6729f.setVibrateLevel(i10);
        this.f6730g.setVibrateLevel(i10);
        this.f6731h.setVibrateLevel(i10);
    }

    /* loaded from: classes.dex */
    public static class c {

        /* renamed from: a, reason: collision with root package name */
        private Calendar f6749a;

        /* renamed from: b, reason: collision with root package name */
        private int f6750b;

        /* renamed from: c, reason: collision with root package name */
        private int f6751c;

        /* renamed from: d, reason: collision with root package name */
        private int f6752d;

        /* renamed from: e, reason: collision with root package name */
        private int f6753e;

        /* renamed from: f, reason: collision with root package name */
        private int f6754f;

        /* renamed from: g, reason: collision with root package name */
        private boolean f6755g;

        public c() {
            k(Calendar.getInstance());
        }

        boolean b(Calendar calendar) {
            if (this.f6755g) {
                return false;
            }
            return this.f6749a.after(calendar);
        }

        public boolean c(Calendar calendar) {
            if (this.f6755g) {
                return false;
            }
            return this.f6749a.after(calendar) || this.f6749a.equals(calendar);
        }

        boolean d(Calendar calendar) {
            if (this.f6755g) {
                return false;
            }
            return this.f6749a.before(calendar);
        }

        public boolean e(Calendar calendar) {
            if (this.f6755g) {
                return false;
            }
            return this.f6749a.before(calendar) || this.f6749a.equals(calendar);
        }

        /* JADX WARN: Removed duplicated region for block: B:28:0x006e  */
        /* JADX WARN: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void f(int i10, int i11, int i12) {
            Date l10;
            int[] a10 = COUILunarUtil.a(i(1), i(2) + 1, i(5));
            if (i10 == 5) {
                if (this.f6755g) {
                    this.f6752d = i12;
                    return;
                }
                if (i11 > 27 && i12 == 1) {
                    this.f6749a.add(5, 1 - i11);
                    return;
                } else if (i11 == 1 && i12 > 27) {
                    this.f6749a.add(5, i12 - 1);
                    return;
                } else {
                    this.f6749a.add(5, i12 - i11);
                    return;
                }
            }
            if (i10 != 2) {
                if (i10 == 1) {
                    boolean z10 = this.f6755g;
                    if (!z10 && i12 != Integer.MIN_VALUE) {
                        o(COUILunarUtil.b(i12, a10[1], a10[2], a10[3]));
                        return;
                    }
                    if (!z10 && i12 == Integer.MIN_VALUE) {
                        this.f6755g = true;
                        this.f6750b = i12;
                        this.f6751c = (a10[1] - 1) + (a10[3] != 1 ? 12 : 0);
                        this.f6752d = a10[2];
                        this.f6753e = this.f6749a.get(11);
                        this.f6754f = this.f6749a.get(12);
                        return;
                    }
                    if (z10 && i12 != Integer.MIN_VALUE) {
                        this.f6755g = false;
                        this.f6750b = i12;
                        int i13 = this.f6751c;
                        int i14 = (i13 % 12) + 1;
                        r0 = i13 / 12 > 0 && COUILunarUtil.k(i12) == i14;
                        int d10 = COUILunarUtil.d(this.f6750b, i14, this.f6752d, r0);
                        this.f6752d = d10;
                        Date l11 = COUILunarUtil.l(this.f6750b, i14, d10, r0);
                        if (l11 != null) {
                            n(l11.getTime());
                            return;
                        }
                        return;
                    }
                    this.f6750b = i12;
                    return;
                }
                return;
            }
            if (!this.f6755g) {
                int i15 = i12 + 1;
                int k10 = COUILunarUtil.k(a10[0]);
                if (k10 != 0 && i15 > k10) {
                    if (i15 == k10 + 1) {
                        i15 = k10;
                        l10 = COUILunarUtil.l(a10[0], i15, COUILunarUtil.d(a10[0], i15, a10[2], r0), r0);
                        if (l10 == null) {
                            n(l10.getTime());
                            return;
                        }
                        return;
                    }
                    i15--;
                }
                r0 = false;
                l10 = COUILunarUtil.l(a10[0], i15, COUILunarUtil.d(a10[0], i15, a10[2], r0), r0);
                if (l10 == null) {
                }
            } else {
                this.f6751c = i12;
            }
        }

        void g(Calendar calendar, Calendar calendar2) {
            if (this.f6755g) {
                return;
            }
            if (this.f6749a.before(calendar)) {
                n(calendar.getTimeInMillis());
            } else if (this.f6749a.after(calendar2)) {
                n(calendar2.getTimeInMillis());
            }
        }

        void h() {
            this.f6749a.clear();
            this.f6750b = 0;
            this.f6751c = 0;
            this.f6752d = 0;
            this.f6753e = 0;
            this.f6754f = 0;
            this.f6755g = false;
        }

        int i(int i10) {
            if (!this.f6755g) {
                return this.f6749a.get(i10);
            }
            if (i10 == 5) {
                return this.f6752d;
            }
            if (i10 == 2) {
                return this.f6751c;
            }
            if (i10 == 1) {
                return this.f6750b;
            }
            return this.f6749a.get(i10);
        }

        long j() {
            return this.f6749a.getTimeInMillis();
        }

        void k(Calendar calendar) {
            this.f6749a = calendar;
            this.f6755g = false;
        }

        void l(int i10, int i11, int i12) {
            if (i10 != Integer.MIN_VALUE) {
                this.f6749a.set(1, i10);
                this.f6749a.set(2, i11);
                this.f6749a.set(5, i12);
                this.f6755g = false;
                return;
            }
            this.f6750b = Integer.MIN_VALUE;
            this.f6751c = i11;
            this.f6752d = i12;
            this.f6755g = true;
        }

        void m(int i10, int i11, int i12, int i13, int i14) {
            if (i10 != Integer.MIN_VALUE) {
                this.f6749a.set(1, i10);
                this.f6749a.set(2, i11);
                this.f6749a.set(5, i12);
                this.f6749a.set(11, i13);
                this.f6749a.set(12, i14);
                this.f6755g = false;
                return;
            }
            this.f6750b = Integer.MIN_VALUE;
            this.f6751c = i11;
            this.f6752d = i12;
            this.f6753e = i13;
            this.f6754f = i14;
            this.f6755g = true;
        }

        public void n(long j10) {
            this.f6749a.setTimeInMillis(j10);
            this.f6755g = false;
        }

        public void o(c cVar) {
            this.f6749a.setTimeInMillis(cVar.f6749a.getTimeInMillis());
            this.f6750b = cVar.f6750b;
            this.f6751c = cVar.f6751c;
            this.f6752d = cVar.f6752d;
            this.f6753e = cVar.f6753e;
            this.f6754f = cVar.f6754f;
            this.f6755g = cVar.f6755g;
        }

        c(Locale locale) {
            k(Calendar.getInstance(locale));
        }
    }

    public COUILunarDatePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiDatePickerStyle);
    }

    public COUILunarDatePicker(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.DatePickerStyle);
    }

    public COUILunarDatePicker(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f6735l = 12;
        this.f6741r = true;
        COUIDarkModeUtil.b(this, false);
        setCurrentLocale(Locale.getDefault());
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUILunarDatePicker, i10, i11);
        this.f6742s = obtainStyledAttributes.getBoolean(R$styleable.COUILunarDatePicker_couiYearIgnorable, false);
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPickersCommonAttrs, i10, i11);
        this.f6743t = obtainStyledAttributes2.getDimensionPixelSize(R$styleable.COUIPickersCommonAttrs_couiPickersMaxWidth, 0);
        obtainStyledAttributes2.recycle();
        this.f6740q = Math.max(getResources().getDimensionPixelOffset(R$dimen.coui_number_picker_background_divider_height), 1);
        int i12 = R$layout.coui_lunar_date_picker;
        this.f6734k = getResources().getStringArray(R$array.coui_lunar_month);
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(i12, (ViewGroup) this, true);
        f6725w = getResources().getString(R$string.coui_lunar_leap_string);
        a aVar = new a();
        b bVar = new b();
        this.f6728e = (LinearLayout) findViewById(R$id.pickers);
        COUINumberPicker cOUINumberPicker = (COUINumberPicker) findViewById(R$id.day);
        this.f6729f = cOUINumberPicker;
        cOUINumberPicker.setOnLongPressUpdateInterval(100L);
        cOUINumberPicker.setOnValueChangedListener(aVar);
        cOUINumberPicker.setOnScrollingStopListener(bVar);
        COUINumberPicker cOUINumberPicker2 = (COUINumberPicker) findViewById(R$id.month);
        this.f6730g = cOUINumberPicker2;
        cOUINumberPicker2.setMinValue(0);
        cOUINumberPicker2.setMaxValue(this.f6735l - 1);
        cOUINumberPicker2.setDisplayedValues(this.f6734k);
        cOUINumberPicker2.setOnLongPressUpdateInterval(200L);
        cOUINumberPicker2.setOnValueChangedListener(aVar);
        cOUINumberPicker2.setOnScrollingStopListener(bVar);
        COUINumberPicker cOUINumberPicker3 = (COUINumberPicker) findViewById(R$id.year);
        this.f6731h = cOUINumberPicker3;
        cOUINumberPicker3.setOnLongPressUpdateInterval(100L);
        cOUINumberPicker3.setOnValueChangedListener(aVar);
        cOUINumberPicker3.setOnScrollingStopListener(bVar);
        cOUINumberPicker3.setIgnorable(this.f6742s);
        setSpinnersShown(true);
        setCalendarViewShown(true);
        this.f6736m.h();
        this.f6736m.l(1910, 0, 1);
        setMinDate(this.f6736m.j());
        this.f6736m.h();
        this.f6736m.m(2036, 11, 31, 23, 59);
        setMaxDate(this.f6736m.j());
        this.f6737n.n(System.currentTimeMillis());
        o(this.f6737n.i(1), this.f6737n.i(2), this.f6737n.i(5), null);
        if (cOUINumberPicker3.Y()) {
            String string = context.getResources().getString(R$string.picker_talkback_tip);
            cOUINumberPicker3.x(string);
            cOUINumberPicker2.x(string);
            cOUINumberPicker.x(string);
        }
        this.f6738o = context.getResources().getDimensionPixelOffset(R$dimen.coui_selected_background_radius);
        this.f6739p = context.getResources().getDimensionPixelOffset(R$dimen.coui_selected_background_horizontal_padding);
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
    }
}
