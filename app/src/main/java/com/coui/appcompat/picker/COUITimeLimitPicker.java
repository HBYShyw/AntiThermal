package com.coui.appcompat.picker;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.coui.appcompat.picker.COUINumberPicker;
import com.support.control.R$array;
import com.support.control.R$attr;
import com.support.control.R$dimen;
import com.support.control.R$id;
import com.support.control.R$layout;
import com.support.control.R$string;
import com.support.control.R$style;
import com.support.control.R$styleable;
import java.util.Calendar;
import java.util.Locale;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUITimeLimitPicker extends FrameLayout {

    /* renamed from: z, reason: collision with root package name */
    private static final i f6843z = new a();

    /* renamed from: e, reason: collision with root package name */
    private final COUINumberPicker f6844e;

    /* renamed from: f, reason: collision with root package name */
    private final COUINumberPicker f6845f;

    /* renamed from: g, reason: collision with root package name */
    private final COUINumberPicker f6846g;

    /* renamed from: h, reason: collision with root package name */
    private final Button f6847h;

    /* renamed from: i, reason: collision with root package name */
    private final String[] f6848i;

    /* renamed from: j, reason: collision with root package name */
    int f6849j;

    /* renamed from: k, reason: collision with root package name */
    int f6850k;

    /* renamed from: l, reason: collision with root package name */
    private LinearLayout f6851l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f6852m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f6853n;

    /* renamed from: o, reason: collision with root package name */
    private TextView f6854o;

    /* renamed from: p, reason: collision with root package name */
    private TextView f6855p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f6856q;

    /* renamed from: r, reason: collision with root package name */
    private i f6857r;

    /* renamed from: s, reason: collision with root package name */
    private Calendar f6858s;

    /* renamed from: t, reason: collision with root package name */
    private Locale f6859t;

    /* renamed from: u, reason: collision with root package name */
    private int f6860u;

    /* renamed from: v, reason: collision with root package name */
    private int f6861v;

    /* renamed from: w, reason: collision with root package name */
    private Context f6862w;

    /* renamed from: x, reason: collision with root package name */
    private int f6863x;

    /* renamed from: y, reason: collision with root package name */
    private int f6864y;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        private final int f6865e;

        /* renamed from: f, reason: collision with root package name */
        private final int f6866f;

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

        int j() {
            return this.f6865e;
        }

        int k() {
            return this.f6866f;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f6865e);
            parcel.writeInt(this.f6866f);
        }

        /* synthetic */ SavedState(Parcelable parcelable, int i10, int i11, a aVar) {
            this(parcelable, i10, i11);
        }

        private SavedState(Parcelable parcelable, int i10, int i11) {
            super(parcelable);
            this.f6865e = i10;
            this.f6866f = i11;
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.f6865e = parcel.readInt();
            this.f6866f = parcel.readInt();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements i {
        a() {
        }

        @Override // com.coui.appcompat.picker.COUITimeLimitPicker.i
        public void a(COUITimeLimitPicker cOUITimeLimitPicker, int i10, int i11) {
        }
    }

    /* loaded from: classes.dex */
    class b implements COUINumberPicker.f {
        b() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.f
        public void a(COUINumberPicker cOUINumberPicker, int i10, int i11) {
            COUITimeLimitPicker.this.g();
        }
    }

    /* loaded from: classes.dex */
    class c implements COUINumberPicker.e {
        c() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.e
        public void a() {
            COUITimeLimitPicker.this.sendAccessibilityEvent(4);
        }
    }

    /* loaded from: classes.dex */
    class d implements COUINumberPicker.f {
        d() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.f
        public void a(COUINumberPicker cOUINumberPicker, int i10, int i11) {
            COUITimeLimitPicker.this.g();
        }
    }

    /* loaded from: classes.dex */
    class e implements COUINumberPicker.e {
        e() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.e
        public void a() {
            COUITimeLimitPicker.this.sendAccessibilityEvent(4);
        }
    }

    /* loaded from: classes.dex */
    class f implements View.OnClickListener {
        f() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            view.requestFocus();
            COUITimeLimitPicker.this.f6853n = !r2.f6853n;
            COUITimeLimitPicker.this.i();
        }
    }

    /* loaded from: classes.dex */
    class g implements COUINumberPicker.f {
        g() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.f
        public void a(COUINumberPicker cOUINumberPicker, int i10, int i11) {
            cOUINumberPicker.requestFocus();
            COUITimeLimitPicker.this.f6853n = !r1.f6853n;
            COUITimeLimitPicker.this.i();
            COUITimeLimitPicker.this.g();
        }
    }

    /* loaded from: classes.dex */
    class h implements COUINumberPicker.e {
        h() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.e
        public void a() {
            COUITimeLimitPicker.this.sendAccessibilityEvent(4);
        }
    }

    /* loaded from: classes.dex */
    public interface i {
        void a(COUITimeLimitPicker cOUITimeLimitPicker, int i10, int i11);
    }

    public COUITimeLimitPicker(Context context) {
        this(context, null);
    }

    private void f(View view, int i10, int i11) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        view.measure(FrameLayout.getChildMeasureSpec(i10, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin, marginLayoutParams.width), FrameLayout.getChildMeasureSpec(i11, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, marginLayoutParams.height));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        i iVar = this.f6857r;
        if (iVar != null) {
            iVar.a(this, getCurrentHour().intValue(), getCurrentMinute().intValue());
        }
    }

    private void h() {
        COUINumberPicker cOUINumberPicker;
        if (DateFormat.getBestDateTimePattern(Locale.getDefault(), "hm").startsWith("a") || (cOUINumberPicker = this.f6846g) == null) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) cOUINumberPicker.getParent();
        viewGroup.removeView(this.f6846g);
        viewGroup.addView(this.f6846g);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        if (e()) {
            COUINumberPicker cOUINumberPicker = this.f6846g;
            if (cOUINumberPicker != null) {
                cOUINumberPicker.setVisibility(8);
                return;
            } else {
                this.f6847h.setVisibility(8);
                return;
            }
        }
        int i10 = !this.f6853n ? 1 : 0;
        COUINumberPicker cOUINumberPicker2 = this.f6846g;
        if (cOUINumberPicker2 != null) {
            cOUINumberPicker2.setValue(i10);
            this.f6846g.setVisibility(0);
        } else {
            this.f6847h.setText(this.f6848i[i10]);
            this.f6847h.setVisibility(0);
        }
    }

    private void j() {
        if (e()) {
            this.f6844e.setMinValue(0);
            this.f6844e.setMaxValue(23);
            this.f6844e.o0();
        } else {
            this.f6844e.setMinValue(1);
            this.f6844e.setMaxValue(12);
        }
        this.f6844e.setWrapSelectorWheel(true);
        this.f6845f.setWrapSelectorWheel(true);
    }

    private void setCurrentLocale(Locale locale) {
        if (locale.equals(this.f6859t)) {
            return;
        }
        this.f6859t = locale;
        this.f6858s = Calendar.getInstance(locale);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(this.f6844e.getBackgroundColor());
        canvas.drawRect(this.f6861v, (int) ((getHeight() / 2.0f) - this.f6860u), getWidth() - this.f6861v, r0 + this.f6864y, paint);
        canvas.drawRect(this.f6861v, (int) ((getHeight() / 2.0f) + this.f6860u), getWidth() - this.f6861v, r0 + this.f6864y, paint);
        super.dispatchDraw(canvas);
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        onPopulateAccessibilityEvent(accessibilityEvent);
        return true;
    }

    public boolean e() {
        return this.f6852m;
    }

    public COUINumberPicker getAmPmSpinner() {
        return this.f6846g;
    }

    @Override // android.view.View
    public int getBaseline() {
        return this.f6844e.getBaseline();
    }

    public Integer getCurrentHour() {
        int value = this.f6844e.getValue();
        if (e()) {
            return Integer.valueOf(value);
        }
        if (this.f6853n) {
            return Integer.valueOf(value % 12);
        }
        return Integer.valueOf((value % 12) + 12);
    }

    public Integer getCurrentMinute() {
        return Integer.valueOf(this.f6845f.getValue());
    }

    public COUINumberPicker getHourSpinner() {
        return this.f6844e;
    }

    public COUINumberPicker getMinuteSpinner() {
        return this.f6845f;
    }

    @Override // android.view.View
    public boolean isEnabled() {
        return this.f6856q;
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
        int i12 = this.f6863x;
        if (i12 > 0 && size > i12) {
            size = i12;
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, mode);
        this.f6849j = -1;
        for (int i13 = 0; i13 < this.f6851l.getChildCount(); i13++) {
            View childAt = this.f6851l.getChildAt(i13);
            if ((childAt instanceof COUINumberPicker) && childAt.getVisibility() == 0) {
                if (this.f6849j == -1) {
                    this.f6849j = i13;
                }
                this.f6850k = i13;
                ((COUINumberPicker) childAt).z();
                f(childAt, i10, i11);
                size -= childAt.getMeasuredWidth();
            }
        }
        int i14 = size / 2;
        if (isLayoutRtl()) {
            int i15 = this.f6849j;
            this.f6849j = this.f6850k;
            this.f6850k = i15;
        }
        if (this.f6851l.getChildAt(this.f6849j) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.f6851l.getChildAt(this.f6849j)).setNumberPickerPaddingLeft(i14);
        }
        if (this.f6851l.getChildAt(this.f6850k) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.f6851l.getChildAt(this.f6850k)).setNumberPickerPaddingRight(i14);
        }
        super.onMeasure(makeMeasureSpec, i11);
    }

    @Override // android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        String str;
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        String str2 = this.f6853n ? this.f6848i[0] : this.f6848i[1];
        if (this.f6852m) {
            str = getCurrentHour().toString() + this.f6862w.getString(R$string.coui_hour) + getCurrentMinute() + this.f6862w.getString(R$string.coui_minute);
        } else {
            str = str2 + this.f6844e.getValue() + this.f6862w.getString(R$string.coui_hour) + getCurrentMinute() + this.f6862w.getString(R$string.coui_minute);
        }
        accessibilityEvent.getText().add(str);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setCurrentHour(Integer.valueOf(savedState.j()));
        setCurrentMinute(Integer.valueOf(savedState.k()));
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), getCurrentHour().intValue(), getCurrentMinute().intValue(), null);
    }

    public void setCurrentHour(Integer num) {
        if (num == null || num.intValue() == getCurrentHour().intValue()) {
            return;
        }
        if (!e()) {
            if (num.intValue() >= 12) {
                this.f6853n = false;
                if (num.intValue() > 12) {
                    num = Integer.valueOf(num.intValue() - 12);
                }
            } else {
                this.f6853n = true;
                if (num.intValue() == 0) {
                    num = 12;
                }
            }
            i();
        }
        this.f6844e.setValue(num.intValue());
        g();
    }

    public void setCurrentMinute(Integer num) {
        if (num.equals(getCurrentMinute())) {
            return;
        }
        this.f6845f.setValue(num.intValue());
        g();
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        if (this.f6856q == z10) {
            return;
        }
        super.setEnabled(z10);
        this.f6845f.setEnabled(z10);
        this.f6844e.setEnabled(z10);
        COUINumberPicker cOUINumberPicker = this.f6846g;
        if (cOUINumberPicker != null) {
            cOUINumberPicker.setEnabled(z10);
        } else {
            this.f6847h.setEnabled(z10);
        }
        this.f6856q = z10;
    }

    public void setIs24HourView(Boolean bool) {
        if (this.f6852m == bool.booleanValue()) {
            return;
        }
        int intValue = getCurrentHour().intValue();
        this.f6852m = bool.booleanValue();
        j();
        setCurrentHour(Integer.valueOf(intValue));
        i();
        this.f6844e.requestLayout();
    }

    public void setNormalTextColor(int i10) {
        COUINumberPicker cOUINumberPicker = this.f6844e;
        if (cOUINumberPicker != null) {
            cOUINumberPicker.setNormalTextColor(i10);
        }
        COUINumberPicker cOUINumberPicker2 = this.f6845f;
        if (cOUINumberPicker2 != null) {
            cOUINumberPicker2.setNormalTextColor(i10);
        }
        COUINumberPicker cOUINumberPicker3 = this.f6846g;
        if (cOUINumberPicker3 != null) {
            cOUINumberPicker3.setNormalTextColor(i10);
        }
    }

    public void setOnTimeChangedListener(i iVar) {
        this.f6857r = iVar;
    }

    public void setRowNumber(int i10) {
        COUINumberPicker cOUINumberPicker;
        if (i10 <= 0 || (cOUINumberPicker = this.f6844e) == null || this.f6845f == null || this.f6846g == null) {
            return;
        }
        cOUINumberPicker.setPickerRowNumber(i10);
        this.f6845f.setPickerRowNumber(i10);
        this.f6846g.setPickerRowNumber(i10);
    }

    public void setTextVisibility(boolean z10) {
        if (z10) {
            this.f6854o.setVisibility(0);
            this.f6855p.setVisibility(0);
        } else {
            this.f6854o.setVisibility(8);
            this.f6855p.setVisibility(8);
        }
    }

    public void setVibrateIntensity(float f10) {
        this.f6844e.setVibrateIntensity(f10);
        this.f6845f.setVibrateIntensity(f10);
        this.f6846g.setVibrateIntensity(f10);
    }

    public void setVibrateLevel(int i10) {
        this.f6844e.setVibrateLevel(i10);
        this.f6845f.setVibrateLevel(i10);
        this.f6846g.setVibrateLevel(i10);
    }

    public COUITimeLimitPicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiTimePickerStyle);
    }

    public COUITimeLimitPicker(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.TimePickerStyle);
    }

    public COUITimeLimitPicker(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f6849j = -1;
        this.f6850k = -1;
        this.f6856q = true;
        COUIDarkModeUtil.b(this, false);
        setCurrentLocale(Locale.getDefault());
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPickersCommonAttrs, i10, i11);
        this.f6863x = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIPickersCommonAttrs_couiPickersMaxWidth, 0);
        obtainStyledAttributes.recycle();
        this.f6864y = Math.max(getResources().getDimensionPixelOffset(R$dimen.coui_number_picker_background_divider_height), 1);
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R$layout.coui_time_limit_picker, (ViewGroup) this, true);
        this.f6854o = (TextView) findViewById(R$id.coui_timepicker_minute_text);
        this.f6855p = (TextView) findViewById(R$id.coui_timepicker_hour_text);
        COUINumberPicker cOUINumberPicker = (COUINumberPicker) findViewById(R$id.hour);
        this.f6844e = cOUINumberPicker;
        cOUINumberPicker.setOnValueChangedListener(new b());
        cOUINumberPicker.setOnScrollingStopListener(new c());
        cOUINumberPicker.setUnitText(getResources().getString(R$string.coui_hour_abbreviation));
        this.f6854o.setTextAlignment(5);
        this.f6855p.setTextAlignment(5);
        this.f6851l = (LinearLayout) findViewById(R$id.time_pickers);
        COUINumberPicker cOUINumberPicker2 = (COUINumberPicker) findViewById(R$id.minute);
        this.f6845f = cOUINumberPicker2;
        cOUINumberPicker2.o0();
        cOUINumberPicker2.setMinValue(0);
        cOUINumberPicker2.setMaxValue(59);
        cOUINumberPicker2.setUnitText(getResources().getString(R$string.coui_minute_abbreviation));
        cOUINumberPicker2.setOnLongPressUpdateInterval(100L);
        cOUINumberPicker2.setOnValueChangedListener(new d());
        cOUINumberPicker2.setOnScrollingStopListener(new e());
        String[] stringArray = getContext().getResources().getStringArray(R$array.coui_time_picker_ampm);
        this.f6848i = stringArray;
        View findViewById = findViewById(R$id.amPm);
        if (findViewById instanceof Button) {
            this.f6846g = null;
            Button button = (Button) findViewById;
            this.f6847h = button;
            button.setOnClickListener(new f());
        } else {
            this.f6847h = null;
            COUINumberPicker cOUINumberPicker3 = (COUINumberPicker) findViewById;
            this.f6846g = cOUINumberPicker3;
            cOUINumberPicker3.setMinValue(0);
            cOUINumberPicker3.setMaxValue(1);
            cOUINumberPicker3.setDisplayedValues(stringArray);
            cOUINumberPicker3.setOnValueChangedListener(new g());
            cOUINumberPicker3.setOnScrollingStopListener(new h());
        }
        j();
        i();
        setOnTimeChangedListener(f6843z);
        setCurrentHour(Integer.valueOf(this.f6858s.get(11)));
        setCurrentMinute(Integer.valueOf(this.f6858s.get(12)));
        if (!isEnabled()) {
            setEnabled(false);
        }
        h();
        if (cOUINumberPicker.Y()) {
            String string = context.getResources().getString(R$string.picker_talkback_tip);
            cOUINumberPicker.x(context.getString(R$string.coui_hour) + string);
            cOUINumberPicker2.x(context.getString(R$string.coui_minute) + string);
            COUINumberPicker cOUINumberPicker4 = this.f6846g;
            if (cOUINumberPicker4 != null) {
                cOUINumberPicker4.x(string);
            }
        }
        this.f6860u = context.getResources().getDimensionPixelOffset(R$dimen.coui_selected_background_radius);
        this.f6861v = context.getResources().getDimensionPixelOffset(R$dimen.coui_selected_background_horizontal_padding);
        setImportantForAccessibility(1);
        this.f6862w = context;
    }
}
