package com.coui.appcompat.picker;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.coui.appcompat.calendar.COUIDateMonthView;
import com.coui.appcompat.picker.COUINumberPicker;
import com.support.control.R$array;
import com.support.control.R$attr;
import com.support.control.R$dimen;
import com.support.control.R$id;
import com.support.control.R$layout;
import com.support.control.R$string;
import com.support.control.R$style;
import com.support.control.R$styleable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUIDatePicker extends FrameLayout {
    private static final String G = COUIDatePicker.class.getSimpleName();
    private static char[] H = {'d', 'M', 'y'};
    private int A;
    private int B;
    private int C;
    private boolean D;
    private Date E;
    private int F;

    /* renamed from: e, reason: collision with root package name */
    private final LinearLayout f6691e;

    /* renamed from: f, reason: collision with root package name */
    private final COUINumberPicker f6692f;

    /* renamed from: g, reason: collision with root package name */
    private final COUINumberPicker f6693g;

    /* renamed from: h, reason: collision with root package name */
    private final COUINumberPicker f6694h;

    /* renamed from: i, reason: collision with root package name */
    private final DateFormat f6695i;

    /* renamed from: j, reason: collision with root package name */
    int f6696j;

    /* renamed from: k, reason: collision with root package name */
    int f6697k;

    /* renamed from: l, reason: collision with root package name */
    private Context f6698l;

    /* renamed from: m, reason: collision with root package name */
    private Locale f6699m;

    /* renamed from: n, reason: collision with root package name */
    private e f6700n;

    /* renamed from: o, reason: collision with root package name */
    private String[] f6701o;

    /* renamed from: p, reason: collision with root package name */
    private int f6702p;

    /* renamed from: q, reason: collision with root package name */
    private d f6703q;

    /* renamed from: r, reason: collision with root package name */
    private Calendar f6704r;

    /* renamed from: s, reason: collision with root package name */
    private Calendar f6705s;

    /* renamed from: t, reason: collision with root package name */
    private d f6706t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f6707u;

    /* renamed from: v, reason: collision with root package name */
    private c f6708v;

    /* renamed from: w, reason: collision with root package name */
    private c f6709w;

    /* renamed from: x, reason: collision with root package name */
    private c f6710x;

    /* renamed from: y, reason: collision with root package name */
    private int f6711y;

    /* renamed from: z, reason: collision with root package name */
    private int f6712z;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        private final int f6713e;

        /* renamed from: f, reason: collision with root package name */
        private final int f6714f;

        /* renamed from: g, reason: collision with root package name */
        private final int f6715g;

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
            parcel.writeInt(this.f6713e);
            parcel.writeInt(this.f6714f);
            parcel.writeInt(this.f6715g);
        }

        /* synthetic */ SavedState(Parcelable parcelable, int i10, int i11, int i12, a aVar) {
            this(parcelable, i10, i11, i12);
        }

        private SavedState(Parcelable parcelable, int i10, int i11, int i12) {
            super(parcelable);
            this.f6713e = i10;
            this.f6714f = i11;
            this.f6715g = i12;
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.f6713e = parcel.readInt();
            this.f6714f = parcel.readInt();
            this.f6715g = parcel.readInt();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements COUINumberPicker.f {
        a() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.f
        public void a(COUINumberPicker cOUINumberPicker, int i10, int i11) {
            COUIDatePicker.this.f6703q.o(COUIDatePicker.this.f6706t);
            if (cOUINumberPicker == COUIDatePicker.this.f6692f) {
                COUIDatePicker.this.f6703q.l(5, i11);
            } else if (cOUINumberPicker == COUIDatePicker.this.f6693g) {
                COUIDatePicker.this.f6703q.l(2, i11);
            } else if (cOUINumberPicker == COUIDatePicker.this.f6694h) {
                COUIDatePicker.this.f6703q.l(1, i11);
            } else {
                throw new IllegalArgumentException();
            }
            COUIDatePicker cOUIDatePicker = COUIDatePicker.this;
            cOUIDatePicker.setDate(cOUIDatePicker.f6703q);
            COUIDatePicker.this.z();
            COUIDatePicker.this.w();
            COUIDatePicker.this.s();
        }
    }

    /* loaded from: classes.dex */
    class b implements COUINumberPicker.e {
        b() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.e
        public void a() {
            COUIDatePicker.this.sendAccessibilityEvent(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements COUINumberPicker.c {

        /* renamed from: a, reason: collision with root package name */
        int f6718a;

        /* renamed from: b, reason: collision with root package name */
        String f6719b;

        c(int i10, String str) {
            this.f6718a = i10;
            this.f6719b = str;
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.c
        public String a(int i10) {
            if (this.f6719b.equals("MONTH")) {
                COUIDatePicker.this.E.setMonth(i10);
                return DateUtils.formatDateTime(COUIDatePicker.this.getContext(), COUIDatePicker.this.E.getTime(), 65576);
            }
            if (!Locale.getDefault().getLanguage().equals("zh")) {
                Formatter formatter = new Formatter(new StringBuilder(), COUIDatePicker.this.f6699m);
                if (this.f6719b.equals("YEAR")) {
                    formatter.format("%d", Integer.valueOf(i10));
                    return formatter.toString();
                }
                if (this.f6719b.equals("DAY")) {
                    formatter.format("%02d", Integer.valueOf(i10));
                    return formatter.toString();
                }
            }
            return i10 + COUIDatePicker.this.getResources().getString(this.f6718a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class d {

        /* renamed from: a, reason: collision with root package name */
        private Calendar f6721a;

        /* renamed from: b, reason: collision with root package name */
        private boolean f6722b;

        public d(Locale locale) {
            this.f6721a = Calendar.getInstance(locale);
        }

        public boolean c(Calendar calendar) {
            if (this.f6722b) {
                return false;
            }
            return this.f6721a.after(calendar);
        }

        public boolean d(Calendar calendar) {
            if (this.f6722b) {
                return false;
            }
            return this.f6721a.before(calendar);
        }

        void e(Calendar calendar, Calendar calendar2) {
            if (this.f6722b) {
                return;
            }
            if (this.f6721a.before(calendar)) {
                l(1, calendar.get(1));
                l(2, calendar.get(2));
                l(5, calendar.get(5));
            } else if (this.f6721a.after(calendar2)) {
                l(1, calendar2.get(1));
                l(2, calendar2.get(2));
                l(5, calendar2.get(5));
            }
        }

        int f(int i10) {
            int actualMaximum = this.f6721a.getActualMaximum(5);
            return i10 > actualMaximum ? actualMaximum : i10;
        }

        public void g() {
            this.f6721a.clear();
            this.f6722b = false;
        }

        public int h(int i10) {
            if (!this.f6722b) {
                return this.f6721a.get(i10);
            }
            if (i10 == 5) {
                return this.f6721a.get(i10);
            }
            if (i10 == 2) {
                return this.f6721a.get(i10);
            }
            if (i10 == 1) {
                return Integer.MIN_VALUE;
            }
            return this.f6721a.get(i10);
        }

        int i(int i10) {
            return this.f6721a.getActualMaximum(i10);
        }

        int j(int i10) {
            return this.f6721a.getActualMinimum(i10);
        }

        public long k() {
            return this.f6721a.getTimeInMillis();
        }

        public void l(int i10, int i11) {
            if (i10 != 1) {
                if (i10 != 2) {
                    if (i10 == 5) {
                        this.f6721a.set(5, f(i11));
                        return;
                    }
                    return;
                } else {
                    int i12 = this.f6721a.get(1);
                    int i13 = this.f6721a.get(5);
                    this.f6721a.clear();
                    this.f6721a.set(1, i12);
                    this.f6721a.set(2, i11);
                    this.f6721a.set(5, f(i13));
                    return;
                }
            }
            if (i11 != Integer.MIN_VALUE) {
                this.f6722b = false;
                int i14 = this.f6721a.get(2);
                int i15 = this.f6721a.get(5);
                this.f6721a.clear();
                this.f6721a.set(1, i11);
                this.f6721a.set(2, i14);
                this.f6721a.set(5, f(i15));
                return;
            }
            this.f6722b = true;
            int i16 = this.f6721a.get(2);
            int i17 = this.f6721a.get(5);
            this.f6721a.clear();
            this.f6721a.set(i10, 2020);
            this.f6721a.set(2, i16);
            this.f6721a.set(5, f(i17));
        }

        public void m(int i10, int i11, int i12) {
            l(1, i10);
            l(2, i11);
            l(5, i12);
        }

        public void n(long j10) {
            this.f6721a.setTimeInMillis(j10);
            this.f6722b = false;
        }

        public void o(d dVar) {
            this.f6721a.setTimeInMillis(dVar.f6721a.getTimeInMillis());
            this.f6722b = dVar.f6722b;
        }
    }

    /* loaded from: classes.dex */
    public interface e {
        void onDateChanged(COUIDatePicker cOUIDatePicker, int i10, int i11, int i12);
    }

    public COUIDatePicker(Context context) {
        this(context, null);
    }

    private void l() {
        this.f6706t.e(this.f6704r, this.f6705s);
    }

    private String m() {
        if (!this.f6706t.f6722b) {
            return DateUtils.formatDateTime(this.f6698l, this.f6706t.f6721a.getTimeInMillis(), 20);
        }
        return DateUtils.formatDateTime(this.f6698l, this.f6706t.f6721a.getTimeInMillis(), 24);
    }

    private d n(d dVar, Locale locale) {
        if (dVar == null) {
            return new d(locale);
        }
        d dVar2 = new d(locale);
        if (!dVar.f6722b) {
            dVar2.n(dVar.k());
        } else {
            dVar2.o(dVar);
        }
        return dVar2;
    }

    private Calendar o(Calendar calendar, Locale locale) {
        if (calendar == null) {
            return Calendar.getInstance(locale);
        }
        long timeInMillis = calendar.getTimeInMillis();
        Calendar calendar2 = Calendar.getInstance(locale);
        calendar2.setTimeInMillis(timeInMillis);
        return calendar2;
    }

    private boolean q(int i10, int i11, int i12) {
        return (this.f6706t.h(1) == i10 && this.f6706t.h(2) == i11 && this.f6706t.h(5) == i12) ? false : true;
    }

    private void r(View view, int i10, int i11) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        view.measure(FrameLayout.getChildMeasureSpec(i10, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin, marginLayoutParams.width), FrameLayout.getChildMeasureSpec(i11, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, marginLayoutParams.height));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s() {
        e eVar = this.f6700n;
        if (eVar != null) {
            eVar.onDateChanged(this, getYear(), getMonth(), getDayOfMonth());
        }
    }

    private void setCurrentLocale(Locale locale) {
        if (locale.equals(this.f6699m)) {
            return;
        }
        this.f6699m = locale;
        this.f6703q = n(this.f6703q, locale);
        this.f6704r = o(this.f6704r, locale);
        this.f6705s = o(this.f6705s, locale);
        this.f6706t = n(this.f6706t, locale);
        int i10 = this.f6703q.i(2) + 1;
        this.f6702p = i10;
        this.f6701o = new String[i10];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDate(d dVar) {
        this.f6706t.o(dVar);
        l();
    }

    private boolean t(String str, Calendar calendar) {
        try {
            calendar.setTime(this.f6695i.parse(str));
            return true;
        } catch (ParseException unused) {
            return false;
        }
    }

    private void u() {
        String bestDateTimePattern = android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyyMMMMdd");
        if (isLayoutRtl()) {
            bestDateTimePattern = TextUtils.getReverse(bestDateTimePattern, 0, bestDateTimePattern.length()).toString();
        }
        this.f6691e.removeAllViews();
        ArrayList arrayList = new ArrayList();
        for (int i10 = 0; i10 < bestDateTimePattern.length(); i10++) {
            char charAt = bestDateTimePattern.charAt(i10);
            if (charAt != 'M') {
                if (charAt != 'd') {
                    if (charAt == 'y' && this.f6694h.getParent() == null) {
                        this.f6691e.addView(this.f6694h);
                        arrayList.add("y");
                    }
                } else if (this.f6692f.getParent() == null) {
                    this.f6691e.addView(this.f6692f);
                    arrayList.add("d");
                }
            } else if (this.f6693g.getParent() == null) {
                this.f6691e.addView(this.f6693g);
                arrayList.add("M");
            }
            if (this.f6696j == -1) {
                this.f6696j = this.f6691e.getChildCount() - 1;
            }
            this.f6697k = this.f6691e.getChildCount() - 1;
        }
    }

    private void v(int i10, int i11, int i12) {
        this.f6706t.m(i10, i11, i12);
        l();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void w() {
    }

    private void y() {
        int i10 = this.f6711y;
        if (i10 != -1) {
            this.f6692f.setPickerNormalColor(i10);
            this.f6693g.setPickerNormalColor(this.f6711y);
            this.f6694h.setPickerNormalColor(this.f6711y);
        }
        int i11 = this.f6712z;
        if (i11 != -1) {
            this.f6692f.setPickerFocusColor(i11);
            this.f6693g.setPickerFocusColor(this.f6712z);
            this.f6694h.setPickerFocusColor(this.f6712z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void z() {
        this.f6693g.setFormatter(this.f6709w);
        if (this.f6706t.h(1) == this.f6704r.get(1) && this.f6706t.h(1) != this.f6705s.get(1)) {
            this.f6693g.setMinValue(this.f6704r.get(2));
            this.f6693g.setMaxValue(this.f6704r.getActualMaximum(2));
            this.f6693g.setWrapSelectorWheel(this.f6704r.get(2) == 0);
        } else if (this.f6706t.h(1) != this.f6704r.get(1) && this.f6706t.h(1) == this.f6705s.get(1)) {
            this.f6693g.setMinValue(0);
            this.f6693g.setMaxValue(this.f6705s.get(2));
            this.f6693g.setWrapSelectorWheel(this.f6705s.get(2) == this.f6705s.getActualMaximum(2));
        } else if (this.f6706t.h(1) == this.f6704r.get(1) && this.f6706t.h(1) == this.f6705s.get(1)) {
            this.f6693g.setMinValue(this.f6704r.get(2));
            this.f6693g.setMaxValue(this.f6705s.get(2));
            this.f6693g.setWrapSelectorWheel(this.f6705s.get(2) == this.f6705s.getActualMaximum(2) && this.f6704r.get(2) == 0);
        } else {
            this.f6693g.setMinValue(this.f6706t.j(2));
            this.f6693g.setMaxValue(this.f6706t.i(2));
            this.f6693g.setWrapSelectorWheel(true);
        }
        if (this.f6706t.h(1) == this.f6704r.get(1) && this.f6706t.h(2) == this.f6704r.get(2) && (this.f6706t.h(1) != this.f6705s.get(1) || this.f6706t.h(2) != this.f6705s.get(2))) {
            this.f6692f.setMinValue(this.f6704r.get(5));
            this.f6692f.setMaxValue(this.f6704r.getActualMaximum(5));
            this.f6692f.setWrapSelectorWheel(this.f6704r.get(5) == 1);
        } else if ((this.f6706t.h(1) != this.f6704r.get(1) || this.f6706t.h(2) != this.f6704r.get(2)) && this.f6706t.h(1) == this.f6705s.get(1) && this.f6706t.h(2) == this.f6705s.get(2)) {
            this.f6692f.setMinValue(1);
            this.f6692f.setMaxValue(this.f6705s.get(5));
            this.f6692f.setWrapSelectorWheel(this.f6705s.get(5) == this.f6705s.getActualMaximum(5));
        } else if (this.f6706t.h(1) == this.f6704r.get(1) && this.f6706t.h(2) == this.f6704r.get(2) && this.f6706t.h(1) == this.f6705s.get(1) && this.f6706t.h(2) == this.f6705s.get(2)) {
            this.f6692f.setMinValue(this.f6704r.get(5));
            this.f6692f.setMaxValue(this.f6705s.get(5));
            COUINumberPicker cOUINumberPicker = this.f6692f;
            if (this.f6705s.get(5) == this.f6705s.getActualMaximum(5) && this.f6704r.get(5) == 1) {
                r3 = true;
            }
            cOUINumberPicker.setWrapSelectorWheel(r3);
        } else {
            this.f6692f.setMinValue(this.f6706t.j(5));
            this.f6692f.setMaxValue(this.f6706t.i(5));
            this.f6692f.setWrapSelectorWheel(true);
        }
        this.f6694h.setMinValue(this.f6704r.get(1));
        this.f6694h.setMaxValue(this.f6705s.get(1));
        this.f6694h.setWrapSelectorWheel(true);
        this.f6694h.setFormatter(this.f6708v);
        this.f6694h.setValue(this.f6706t.h(1));
        this.f6693g.setValue(this.f6706t.h(2));
        this.f6692f.setValue(this.f6706t.h(5));
        this.f6692f.setFormatter(this.f6710x);
        if (this.f6692f.getValue() > 27) {
            this.f6692f.invalidate();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(this.f6692f.getBackgroundColor());
        canvas.drawRect(this.B, (int) ((getHeight() / 2.0f) - this.A), getWidth() - this.B, r0 + this.C, paint);
        canvas.drawRect(this.B, (int) ((getHeight() / 2.0f) + this.A), getWidth() - this.B, r0 + this.C, paint);
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
        return this.f6706t.h(5);
    }

    public long getMaxDate() {
        return this.f6705s.getTimeInMillis();
    }

    public long getMinDate() {
        return this.f6704r.getTimeInMillis();
    }

    public int getMonth() {
        return this.f6706t.h(2);
    }

    public e getOnDateChangedListener() {
        return this.f6700n;
    }

    public boolean getSpinnersShown() {
        return this.f6691e.isShown();
    }

    public int getYear() {
        return this.f6706t.h(1);
    }

    @Override // android.view.View
    public boolean isEnabled() {
        return this.f6707u;
    }

    public boolean isLayoutRtl() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
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
        int i12 = this.F;
        if (i12 > 0 && size > i12) {
            size = i12;
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, mode);
        this.f6692f.z();
        this.f6693g.z();
        this.f6694h.z();
        r(this.f6692f, i10, i11);
        r(this.f6693g, i10, i11);
        r(this.f6694h, i10, i11);
        int measuredWidth = (((size - this.f6692f.getMeasuredWidth()) - this.f6693g.getMeasuredWidth()) - this.f6694h.getMeasuredWidth()) / 2;
        if (this.f6691e.getChildAt(this.f6696j) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.f6691e.getChildAt(this.f6696j)).setNumberPickerPaddingLeft(measuredWidth);
        }
        if (this.f6691e.getChildAt(this.f6697k) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.f6691e.getChildAt(this.f6697k)).setNumberPickerPaddingRight(measuredWidth);
        }
        super.onMeasure(makeMeasureSpec, i11);
    }

    @Override // android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.getText().add(m());
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        v(savedState.f6713e, savedState.f6714f, savedState.f6715g);
        z();
        w();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), getYear(), getMonth(), getDayOfMonth(), null);
    }

    public void p(int i10, int i11, int i12, e eVar) {
        v(i10, i11, i12);
        z();
        w();
        this.f6700n = eVar;
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    public void setCalendarViewShown(boolean z10) {
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        if (this.f6707u == z10) {
            return;
        }
        super.setEnabled(z10);
        this.f6692f.setEnabled(z10);
        this.f6693g.setEnabled(z10);
        this.f6694h.setEnabled(z10);
        this.f6707u = z10;
    }

    public void setFocusColor(int i10) {
        this.f6712z = i10;
        y();
    }

    public void setMaxDate(long j10) {
        this.f6703q.n(j10);
        if (this.f6703q.h(1) != this.f6705s.get(1) || this.f6703q.h(6) == this.f6705s.get(6)) {
            this.f6705s.setTimeInMillis(j10);
            if (this.f6706t.c(this.f6705s)) {
                this.f6706t.n(this.f6705s.getTimeInMillis());
                w();
            }
            z();
        }
    }

    public void setMinDate(long j10) {
        this.f6703q.n(j10);
        if (this.f6703q.h(1) != this.f6704r.get(1) || this.f6703q.h(6) == this.f6704r.get(6)) {
            this.f6704r.setTimeInMillis(j10);
            if (this.f6706t.d(this.f6704r)) {
                this.f6706t.n(this.f6704r.getTimeInMillis());
                w();
            }
            z();
        }
    }

    public void setNormalColor(int i10) {
        this.f6711y = i10;
        y();
    }

    public void setNormalTextColor(int i10) {
        COUINumberPicker cOUINumberPicker = this.f6692f;
        if (cOUINumberPicker != null) {
            cOUINumberPicker.setNormalTextColor(i10);
        }
        COUINumberPicker cOUINumberPicker2 = this.f6693g;
        if (cOUINumberPicker2 != null) {
            cOUINumberPicker2.setNormalTextColor(i10);
        }
        COUINumberPicker cOUINumberPicker3 = this.f6694h;
        if (cOUINumberPicker3 != null) {
            cOUINumberPicker3.setNormalTextColor(i10);
        }
    }

    public void setOnDateChangedListener(e eVar) {
        this.f6700n = eVar;
    }

    public void setSpinnersShown(boolean z10) {
        this.f6691e.setVisibility(z10 ? 0 : 8);
    }

    public void setVibrateIntensity(float f10) {
        this.f6692f.setVibrateIntensity(f10);
        this.f6693g.setVibrateIntensity(f10);
        this.f6694h.setVibrateIntensity(f10);
    }

    public void setVibrateLevel(int i10) {
        this.f6692f.setVibrateLevel(i10);
        this.f6693g.setVibrateLevel(i10);
        this.f6694h.setVibrateLevel(i10);
    }

    public void x(int i10, int i11, int i12) {
        if (q(i10, i11, i12)) {
            v(i10, i11, i12);
            z();
            w();
            s();
        }
    }

    public COUIDatePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiDatePickerStyle);
    }

    public void setBackground(int i10) {
        setBackgroundDrawable(getContext().getResources().getDrawable(i10));
    }

    public COUIDatePicker(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.DatePickerStyle);
    }

    public COUIDatePicker(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f6695i = new SimpleDateFormat("MM/dd/yyyy");
        this.f6696j = -1;
        this.f6697k = -1;
        this.f6707u = true;
        COUIDarkModeUtil.b(this, false);
        this.f6698l = context;
        setCurrentLocale(Locale.getDefault());
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIDatePicker, i10, i11);
        boolean z10 = obtainStyledAttributes.getBoolean(R$styleable.COUIDatePicker_spinnerShown, true);
        boolean z11 = obtainStyledAttributes.getBoolean(R$styleable.COUIDatePicker_calendarViewShown, true);
        int i12 = obtainStyledAttributes.getInt(R$styleable.COUIDatePicker_beginYear, COUIDateMonthView.MIN_YEAR);
        int i13 = obtainStyledAttributes.getInt(R$styleable.COUIDatePicker_endYear, COUIDateMonthView.MAX_YEAR);
        String string = obtainStyledAttributes.getString(R$styleable.COUIDatePicker_minDate);
        String string2 = obtainStyledAttributes.getString(R$styleable.COUIDatePicker_maxDate);
        this.f6701o = getResources().getStringArray(R$array.coui_solor_mounth);
        this.f6711y = obtainStyledAttributes.getColor(R$styleable.COUIDatePicker_calendarTextColor, -1);
        this.f6712z = obtainStyledAttributes.getColor(R$styleable.COUIDatePicker_calendarSelectedTextColor, -1);
        int i14 = R$layout.coui_date_picker;
        this.D = obtainStyledAttributes.getBoolean(R$styleable.COUIDatePicker_couiYearIgnorable, false);
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPickersCommonAttrs, i10, 0);
        this.F = obtainStyledAttributes2.getDimensionPixelSize(R$styleable.COUIPickersCommonAttrs_couiPickersMaxWidth, 0);
        obtainStyledAttributes2.recycle();
        this.C = Math.max(getResources().getDimensionPixelOffset(R$dimen.coui_number_picker_background_divider_height), 1);
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(i14, (ViewGroup) this, true);
        a aVar = new a();
        b bVar = new b();
        this.f6691e = (LinearLayout) findViewById(R$id.pickers);
        this.f6708v = new c(R$string.coui_year, "YEAR");
        this.f6709w = new c(R$string.coui_month, "MONTH");
        this.f6710x = new c(R$string.coui_day, "DAY");
        this.E = new Date();
        COUINumberPicker cOUINumberPicker = (COUINumberPicker) findViewById(R$id.day);
        this.f6692f = cOUINumberPicker;
        cOUINumberPicker.setOnLongPressUpdateInterval(100L);
        cOUINumberPicker.setOnValueChangedListener(aVar);
        cOUINumberPicker.setOnScrollingStopListener(bVar);
        COUINumberPicker cOUINumberPicker2 = (COUINumberPicker) findViewById(R$id.month);
        this.f6693g = cOUINumberPicker2;
        cOUINumberPicker2.setMinValue(0);
        cOUINumberPicker2.setMaxValue(this.f6702p - 1);
        cOUINumberPicker2.setOnLongPressUpdateInterval(200L);
        cOUINumberPicker2.setOnValueChangedListener(aVar);
        cOUINumberPicker2.setOnScrollingStopListener(bVar);
        COUINumberPicker cOUINumberPicker3 = (COUINumberPicker) findViewById(R$id.year);
        this.f6694h = cOUINumberPicker3;
        cOUINumberPicker3.setOnLongPressUpdateInterval(100L);
        cOUINumberPicker3.setOnValueChangedListener(aVar);
        cOUINumberPicker3.setOnScrollingStopListener(bVar);
        cOUINumberPicker3.setIgnorable(this.D);
        y();
        if (!z10 && !z11) {
            setSpinnersShown(true);
        } else {
            setSpinnersShown(z10);
            setCalendarViewShown(z11);
        }
        this.f6703q.g();
        if (!TextUtils.isEmpty(string)) {
            if (!t(string, this.f6703q.f6721a)) {
                this.f6703q.m(i12, 0, 1);
            }
        } else {
            this.f6703q.m(i12, 0, 1);
        }
        setMinDate(this.f6703q.f6721a.getTimeInMillis());
        this.f6703q.g();
        if (!TextUtils.isEmpty(string2)) {
            if (!t(string2, this.f6703q.f6721a)) {
                this.f6703q.m(i13, 11, 31);
            }
        } else {
            this.f6703q.m(i13, 11, 31);
        }
        setMaxDate(this.f6703q.f6721a.getTimeInMillis());
        this.f6706t.n(System.currentTimeMillis());
        p(this.f6706t.h(1), this.f6706t.h(2), this.f6706t.h(5), null);
        u();
        if (cOUINumberPicker3.Y()) {
            String string3 = context.getResources().getString(R$string.picker_talkback_tip);
            cOUINumberPicker3.x(string3);
            cOUINumberPicker2.x(string3);
            cOUINumberPicker.x(string3);
        }
        this.A = context.getResources().getDimensionPixelOffset(R$dimen.coui_selected_background_radius);
        this.B = context.getResources().getDimensionPixelOffset(R$dimen.coui_selected_background_horizontal_padding);
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
    }
}
