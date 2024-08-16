package com.coui.appcompat.picker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.coui.appcompat.calendar.COUIDateMonthView;
import com.coui.appcompat.picker.COUINumberPicker;
import com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneFlightData;
import com.oplus.statistics.util.TimeInfoUtil;
import com.support.control.R$array;
import com.support.control.R$attr;
import com.support.control.R$dimen;
import com.support.control.R$id;
import com.support.control.R$layout;
import com.support.control.R$string;
import com.support.control.R$style;
import com.support.control.R$styleable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUITimePicker extends FrameLayout {
    private boolean A;
    private int B;
    private int C;
    private String D;
    private i E;
    int F;
    int G;
    private int H;
    private int I;
    private j J;

    /* renamed from: e, reason: collision with root package name */
    private String[] f6874e;

    /* renamed from: f, reason: collision with root package name */
    private Calendar f6875f;

    /* renamed from: g, reason: collision with root package name */
    private Calendar f6876g;

    /* renamed from: h, reason: collision with root package name */
    private Calendar f6877h;

    /* renamed from: i, reason: collision with root package name */
    private SimpleDateFormat f6878i;

    /* renamed from: j, reason: collision with root package name */
    private int f6879j;

    /* renamed from: k, reason: collision with root package name */
    private int f6880k;

    /* renamed from: l, reason: collision with root package name */
    private int f6881l;

    /* renamed from: m, reason: collision with root package name */
    private int f6882m;

    /* renamed from: n, reason: collision with root package name */
    private long f6883n;

    /* renamed from: o, reason: collision with root package name */
    private Date f6884o;

    /* renamed from: p, reason: collision with root package name */
    private Context f6885p;

    /* renamed from: q, reason: collision with root package name */
    private String[] f6886q;

    /* renamed from: r, reason: collision with root package name */
    private String[] f6887r;

    /* renamed from: s, reason: collision with root package name */
    private String f6888s;

    /* renamed from: t, reason: collision with root package name */
    private String f6889t;

    /* renamed from: u, reason: collision with root package name */
    private COUINumberPicker f6890u;

    /* renamed from: v, reason: collision with root package name */
    private COUINumberPicker f6891v;

    /* renamed from: w, reason: collision with root package name */
    private COUINumberPicker f6892w;

    /* renamed from: x, reason: collision with root package name */
    private COUINumberPicker f6893x;

    /* renamed from: y, reason: collision with root package name */
    private LinearLayout f6894y;

    /* renamed from: z, reason: collision with root package name */
    private int f6895z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements COUINumberPicker.f {
        a() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.f
        public void a(COUINumberPicker cOUINumberPicker, int i10, int i11) {
            COUITimePicker.this.f6895z = cOUINumberPicker.getValue();
            COUITimePicker.this.f6875f.set(9, cOUINumberPicker.getValue());
            if (COUITimePicker.this.J != null) {
                j jVar = COUITimePicker.this.J;
                COUITimePicker cOUITimePicker = COUITimePicker.this;
                jVar.a(cOUITimePicker, cOUITimePicker.f6875f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements COUINumberPicker.e {
        b() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.e
        public void a() {
            COUITimePicker.this.sendAccessibilityEvent(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements COUINumberPicker.f {
        c() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.f
        public void a(COUINumberPicker cOUINumberPicker, int i10, int i11) {
            if (!COUITimePicker.this.t() && COUITimePicker.this.f6895z != 0) {
                if (COUITimePicker.this.f6895z == 1) {
                    if (cOUINumberPicker.getValue() != 12) {
                        COUITimePicker.this.f6875f.set(11, cOUINumberPicker.getValue() + 12);
                    } else {
                        COUITimePicker.this.f6875f.set(11, 0);
                    }
                }
            } else {
                COUITimePicker.this.f6875f.set(11, cOUINumberPicker.getValue());
            }
            if (!COUITimePicker.this.t() && cOUINumberPicker.getValue() == 12) {
                COUITimePicker cOUITimePicker = COUITimePicker.this;
                cOUITimePicker.f6895z = 1 - cOUITimePicker.f6895z;
                COUITimePicker.this.f6893x.setValue(COUITimePicker.this.f6895z);
            }
            if (COUITimePicker.this.J != null) {
                j jVar = COUITimePicker.this.J;
                COUITimePicker cOUITimePicker2 = COUITimePicker.this;
                jVar.a(cOUITimePicker2, cOUITimePicker2.f6875f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements COUINumberPicker.e {
        d() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.e
        public void a() {
            COUITimePicker.this.sendAccessibilityEvent(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements COUINumberPicker.f {
        e() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.f
        public void a(COUINumberPicker cOUINumberPicker, int i10, int i11) {
            if (COUITimePicker.this.A) {
                COUITimePicker.this.f6875f.set(12, cOUINumberPicker.getValue() * 5);
            } else {
                COUITimePicker.this.f6875f.set(12, cOUINumberPicker.getValue());
            }
            if (COUITimePicker.this.J != null) {
                j jVar = COUITimePicker.this.J;
                COUITimePicker cOUITimePicker = COUITimePicker.this;
                jVar.a(cOUITimePicker, cOUITimePicker.f6875f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f implements COUINumberPicker.e {
        f() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.e
        public void a() {
            COUITimePicker.this.sendAccessibilityEvent(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class g implements COUINumberPicker.f {
        g() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.f
        public void a(COUINumberPicker cOUINumberPicker, int i10, int i11) {
            Date q10 = COUITimePicker.this.q(cOUINumberPicker.getValue());
            if (q10 != null) {
                COUITimePicker.this.f6875f.set(2, q10.getMonth());
                COUITimePicker.this.f6875f.set(5, q10.getDate());
                COUITimePicker.this.f6875f.set(1, q10.getYear() + COUIDateMonthView.MIN_YEAR);
                if (COUITimePicker.this.J != null) {
                    j jVar = COUITimePicker.this.J;
                    COUITimePicker cOUITimePicker = COUITimePicker.this;
                    jVar.a(cOUITimePicker, cOUITimePicker.f6875f);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class h implements COUINumberPicker.e {
        h() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.e
        public void a() {
            COUITimePicker.this.sendAccessibilityEvent(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class i implements COUINumberPicker.c {
        i() {
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.c
        public String a(int i10) {
            int i11 = i10 - 1;
            COUITimePicker.this.f6874e[i11] = COUITimePicker.this.r(i10);
            if (i10 == COUITimePicker.this.f6882m) {
                COUITimePicker.this.f6886q[i11] = COUITimePicker.this.f6888s;
                return COUITimePicker.this.f6886q[i11];
            }
            if (Locale.getDefault().getLanguage().equals("zh")) {
                return new SimpleDateFormat("MMMdd" + COUITimePicker.this.f6889t + " E", Locale.getDefault()).format(Long.valueOf(COUITimePicker.this.f6884o.getTime()));
            }
            return DateUtils.formatDateTime(COUITimePicker.this.getContext(), COUITimePicker.this.f6884o.getTime(), 524314);
        }
    }

    /* loaded from: classes.dex */
    public interface j {
        void a(View view, Calendar calendar);
    }

    public COUITimePicker(Context context) {
        this(context, null);
    }

    private String p(String str) {
        String valueOf = String.valueOf(str.charAt(0));
        for (int i10 = 1; i10 < str.length(); i10++) {
            char charAt = str.charAt(i10);
            if (charAt != str.charAt(i10 - 1)) {
                valueOf = valueOf + charAt;
            }
        }
        return valueOf;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Date q(int i10) {
        try {
            return this.f6878i.parse(this.f6874e[i10 - 1]);
        } catch (ParseException e10) {
            e10.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String r(int i10) {
        this.f6884o.setTime(this.f6883n + (i10 * TimeInfoUtil.MILLISECOND_OF_A_DAY));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.f6884o);
        if (v(calendar.get(1), calendar.get(2), calendar.get(5))) {
            this.f6882m = i10;
        } else {
            this.f6882m = -1;
        }
        return this.f6878i.format(Long.valueOf(this.f6884o.getTime()));
    }

    private int s(int i10) {
        return u(i10) ? 366 : 365;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean t() {
        String string = Settings.System.getString(this.f6885p.getContentResolver(), "time_12_24");
        return string != null && string.equals("24");
    }

    private boolean u(int i10) {
        return (i10 % 4 == 0 && i10 % 100 != 0) || i10 % SceneFlightData.INVALID_LATITUDE_LONGITUDE == 0;
    }

    private boolean v(int i10, int i11, int i12) {
        return i10 == this.f6879j && i11 == this.f6880k && i12 == this.f6881l;
    }

    private void w(View view, int i10, int i11, float f10) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (f10 < 1.0f) {
            marginLayoutParams.width = (int) (marginLayoutParams.width * f10);
        }
        view.measure(FrameLayout.getChildMeasureSpec(i10, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin, marginLayoutParams.width), FrameLayout.getChildMeasureSpec(i11, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, marginLayoutParams.height));
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0045, code lost:
    
        if (r5 != 'y') goto L25;
     */
    /* JADX WARN: Removed duplicated region for block: B:20:0x007d  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0090  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void x() {
        String p10 = p(DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyyMMMddhm"));
        ViewGroup viewGroup = (ViewGroup) this.f6890u.getParent();
        viewGroup.removeAllViews();
        ArrayList arrayList = new ArrayList();
        boolean z10 = false;
        for (int i10 = 0; i10 < p10.length(); i10++) {
            char charAt = p10.charAt(i10);
            if (charAt != 'K') {
                if (charAt != 'M') {
                    if (charAt == 'a') {
                        viewGroup.addView(this.f6893x);
                        arrayList.add("a");
                    } else if (charAt != 'd') {
                        if (charAt != 'h') {
                            if (charAt == 'm') {
                                viewGroup.addView(this.f6892w);
                                arrayList.add("m");
                            }
                        }
                    }
                    if (!t()) {
                        if (this.F == -1) {
                            this.F = viewGroup.getChildCount() - 1;
                        }
                        this.G = viewGroup.getChildCount() - 1;
                    } else if (viewGroup.getChildAt(viewGroup.getChildCount() - 1) != this.f6893x) {
                        if (this.F == -1) {
                            this.F = viewGroup.getChildCount() - 1;
                        }
                        this.G = viewGroup.getChildCount() - 1;
                    }
                }
                if (!z10) {
                    viewGroup.addView(this.f6890u);
                    arrayList.add("D");
                    z10 = true;
                }
                if (!t()) {
                }
            }
            viewGroup.addView(this.f6891v);
            arrayList.add("h");
            if (!t()) {
            }
        }
        if (isLayoutRtl()) {
            int i11 = this.F;
            this.F = this.G;
            this.G = i11;
        }
    }

    private void y() {
        this.D = "";
        String p10 = p(DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyyMMMddhm"));
        boolean z10 = false;
        for (int i10 = 0; i10 < p10.length(); i10++) {
            char charAt = p10.charAt(i10);
            if (charAt != 'K') {
                if (charAt != 'M') {
                    if (charAt != 'a') {
                        if (charAt != 'd') {
                            if (charAt != 'h') {
                                if (charAt == 'm') {
                                    this.D += this.f6892w.getValue() + this.f6885p.getString(R$string.coui_minute);
                                } else if (charAt != 'y') {
                                }
                            }
                        }
                    } else if (!t()) {
                        this.D += (t() ? this.f6887r[0] : this.f6887r[1]);
                    }
                }
                if (!z10) {
                    this.D += this.E.a(this.f6890u.getValue());
                    z10 = true;
                }
            }
            this.D += this.f6891v.getValue() + this.f6885p.getString(R$string.coui_hour);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (!t()) {
            this.C = 0;
        }
        Paint paint = new Paint();
        paint.setColor(this.f6890u.getBackgroundColor());
        canvas.drawRect(this.C, (int) ((getHeight() / 2.0f) - this.B), getWidth() - this.C, r1 + this.I, paint);
        canvas.drawRect(this.C, (int) ((getHeight() / 2.0f) + this.B), getWidth() - this.C, r1 + this.I, paint);
        super.dispatchDraw(canvas);
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        onPopulateAccessibilityEvent(accessibilityEvent);
        return true;
    }

    public View getTimePicker() {
        int i10;
        StringBuilder sb2;
        Calendar calendar = this.f6877h;
        if (calendar != null) {
            i10 = calendar.get(1);
        } else {
            calendar = this.f6876g;
            i10 = calendar.get(1);
        }
        int i11 = i10;
        int i12 = calendar.get(2) + 1;
        int i13 = calendar.get(5);
        int i14 = calendar.get(11);
        int i15 = calendar.get(9);
        int i16 = calendar.get(12);
        this.f6875f.setTimeZone(calendar.getTimeZone());
        this.f6878i.setTimeZone(calendar.getTimeZone());
        int i17 = i12 - 1;
        this.f6875f.set(i11, i17, i13, i14, i16);
        int i18 = 36500;
        for (int i19 = 0; i19 < 100; i19++) {
            i18 += s((i11 - 50) + i19);
        }
        int i20 = 0;
        for (int i21 = 0; i21 < 50; i21++) {
            i20 += s((i11 - 50) + i21);
        }
        String[] strArr = new String[i18];
        this.f6886q = strArr;
        this.f6874e = (String[]) strArr.clone();
        if (i12 > 2 && !u(i11 - 50) && u(i11)) {
            i20++;
        }
        if (i12 > 2 && u(i11 - 50)) {
            i20--;
        }
        int i22 = i20;
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeZone(calendar.getTimeZone());
        int i23 = i18;
        calendar2.set(i11, i17, i13, i14, i16);
        if (u(i11) && i12 == 2 && i13 == 29) {
            calendar2.add(5, 1);
        }
        calendar2.add(1, -50);
        this.f6883n = calendar2.getTimeInMillis();
        this.f6884o = new Date();
        if (t()) {
            this.f6891v.setMaxValue(23);
            this.f6891v.setMinValue(0);
            this.f6891v.o0();
            this.f6893x.setVisibility(8);
        } else {
            this.f6891v.setMaxValue(12);
            this.f6891v.setMinValue(1);
            this.f6893x.setMaxValue(this.f6887r.length - 1);
            this.f6893x.setMinValue(0);
            this.f6893x.setDisplayedValues(this.f6887r);
            this.f6893x.setVisibility(0);
            this.f6893x.setWrapSelectorWheel(false);
        }
        this.f6891v.setWrapSelectorWheel(true);
        if (t()) {
            this.f6891v.setValue(i14);
        } else {
            if (i15 > 0) {
                this.f6891v.setValue(i14 - 12);
            } else {
                this.f6891v.setValue(i14);
            }
            this.f6893x.setValue(i15);
            this.f6895z = i15;
        }
        this.f6893x.setOnValueChangedListener(new a());
        this.f6893x.setOnScrollingStopListener(new b());
        this.f6891v.setOnValueChangedListener(new c());
        this.f6891v.setOnScrollingStopListener(new d());
        this.f6892w.setMinValue(0);
        if (this.A) {
            this.f6892w.setMinValue(0);
            this.f6892w.setMaxValue(11);
            String[] strArr2 = new String[12];
            int i24 = 0;
            for (int i25 = 12; i24 < i25; i25 = 12) {
                int i26 = i24 * 5;
                if (i26 < 10) {
                    sb2 = new StringBuilder();
                    sb2.append("0");
                    sb2.append(i26);
                } else {
                    sb2 = new StringBuilder();
                    sb2.append(i26);
                    sb2.append("");
                }
                strArr2[i24] = sb2.toString();
                i24++;
            }
            this.f6892w.setDisplayedValues(strArr2);
            int i27 = i16 / 5;
            this.f6892w.setValue(i27);
            this.f6875f.set(12, Integer.parseInt(strArr2[i27]));
        } else {
            this.f6892w.setMaxValue(59);
            this.f6892w.setValue(i16);
        }
        this.f6892w.o0();
        this.f6892w.setWrapSelectorWheel(true);
        this.f6892w.setOnValueChangedListener(new e());
        this.f6892w.setOnScrollingStopListener(new f());
        this.f6890u.setMinValue(1);
        this.f6890u.setMaxValue(i23);
        this.f6890u.setWrapSelectorWheel(false);
        this.f6890u.setValue(i22);
        i iVar = new i();
        this.E = iVar;
        this.f6890u.setFormatter(iVar);
        this.f6890u.setOnValueChangedListener(new g());
        this.f6890u.setOnScrollingStopListener(new h());
        return this;
    }

    public boolean isLayoutRtl() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        int mode = View.MeasureSpec.getMode(i10);
        int size = View.MeasureSpec.getSize(i10);
        int i12 = this.H;
        if (i12 > 0 && size > i12) {
            size = i12;
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, mode);
        this.f6892w.z();
        this.f6891v.z();
        this.f6890u.z();
        this.f6893x.z();
        float f10 = size / (((this.f6892w.getLayoutParams().width + this.f6891v.getLayoutParams().width) + this.f6890u.getLayoutParams().width) + this.f6893x.getLayoutParams().width);
        w(this.f6892w, i10, i11, f10);
        w(this.f6891v, i10, i11, f10);
        w(this.f6890u, i10, i11, f10);
        w(this.f6893x, i10, i11, f10);
        int measuredWidth = ((((size - this.f6892w.getMeasuredWidth()) - this.f6891v.getMeasuredWidth()) - this.f6890u.getMeasuredWidth()) - (t() ? 0 : this.f6893x.getMeasuredWidth())) / 2;
        if (this.f6894y.getChildAt(this.F) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.f6894y.getChildAt(this.F)).setNumberPickerPaddingLeft(measuredWidth);
        }
        if (this.f6894y.getChildAt(this.G) instanceof COUINumberPicker) {
            ((COUINumberPicker) this.f6894y.getChildAt(this.G)).setNumberPickerPaddingRight(measuredWidth);
        }
        super.onMeasure(makeMeasureSpec, i11);
    }

    @Override // android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        y();
        accessibilityEvent.getText().add(this.D);
    }

    public void setNormalTextColor(int i10) {
        COUINumberPicker cOUINumberPicker = this.f6890u;
        if (cOUINumberPicker != null) {
            cOUINumberPicker.setNormalTextColor(i10);
        }
        COUINumberPicker cOUINumberPicker2 = this.f6891v;
        if (cOUINumberPicker2 != null) {
            cOUINumberPicker2.setNormalTextColor(i10);
        }
        COUINumberPicker cOUINumberPicker3 = this.f6892w;
        if (cOUINumberPicker3 != null) {
            cOUINumberPicker3.setNormalTextColor(i10);
        }
        COUINumberPicker cOUINumberPicker4 = this.f6893x;
        if (cOUINumberPicker4 != null) {
            cOUINumberPicker4.setNormalTextColor(i10);
        }
    }

    public void setOnTimeChangeListener(j jVar) {
        this.J = jVar;
    }

    public void setTimePicker(Calendar calendar) {
        this.f6877h = calendar;
        getTimePicker();
    }

    public void setVibrateIntensity(float f10) {
        this.f6890u.setVibrateIntensity(f10);
        this.f6891v.setVibrateIntensity(f10);
        this.f6892w.setVibrateIntensity(f10);
        this.f6893x.setVibrateIntensity(f10);
    }

    public void setVibrateLevel(int i10) {
        this.f6890u.setVibrateLevel(i10);
        this.f6891v.setVibrateLevel(i10);
        this.f6892w.setVibrateLevel(i10);
        this.f6893x.setVibrateLevel(i10);
    }

    public COUITimePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiTimePickerStyle);
    }

    public COUITimePicker(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.TimePickerStyle);
    }

    public COUITimePicker(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f6882m = -1;
        this.f6895z = -1;
        this.D = "";
        this.F = -1;
        this.G = -1;
        COUIDarkModeUtil.b(this, false);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPickersCommonAttrs, i10, i11);
        this.H = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIPickersCommonAttrs_couiPickersMaxWidth, 0);
        obtainStyledAttributes.recycle();
        this.f6885p = context;
        this.f6887r = context.getResources().getStringArray(R$array.coui_time_picker_ampm);
        this.f6888s = this.f6885p.getResources().getString(R$string.coui_time_picker_today);
        this.f6889t = this.f6885p.getResources().getString(R$string.coui_time_picker_day);
        this.f6875f = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        this.f6876g = calendar;
        this.f6879j = calendar.get(1);
        this.f6880k = this.f6876g.get(2);
        this.f6881l = this.f6876g.get(5);
        this.f6878i = new SimpleDateFormat("yyyy MMM dd" + this.f6889t + " E", Locale.getDefault());
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.f6885p).inflate(R$layout.coui_time_picker, (ViewGroup) this, true);
        this.f6890u = (COUINumberPicker) viewGroup.findViewById(R$id.coui_time_picker_date);
        this.f6891v = (COUINumberPicker) viewGroup.findViewById(R$id.coui_time_picker_hour);
        this.f6892w = (COUINumberPicker) viewGroup.findViewById(R$id.coui_time_picker_minute);
        this.f6893x = (COUINumberPicker) viewGroup.findViewById(R$id.coui_time_picker_ampm);
        this.f6894y = (LinearLayout) viewGroup.findViewById(R$id.pickers);
        this.B = context.getResources().getDimensionPixelOffset(R$dimen.coui_selected_background_radius);
        this.C = context.getResources().getDimensionPixelOffset(R$dimen.coui_selected_background_horizontal_padding);
        this.I = Math.max(getResources().getDimensionPixelOffset(R$dimen.coui_number_picker_background_divider_height), 1);
        if (!Locale.getDefault().getLanguage().equals("zh") && !Locale.getDefault().getLanguage().equals("en")) {
            this.f6890u.getLayoutParams().width = getResources().getDimensionPixelOffset(R$dimen.coui_number_picker_width_biggest);
        }
        x();
        COUINumberPicker cOUINumberPicker = this.f6891v;
        if (cOUINumberPicker != null && cOUINumberPicker.Y()) {
            String string = context.getResources().getString(R$string.picker_talkback_tip);
            COUINumberPicker cOUINumberPicker2 = this.f6890u;
            if (cOUINumberPicker2 != null) {
                cOUINumberPicker2.x(string);
            }
            this.f6891v.x(context.getResources().getString(R$string.coui_hour) + string);
            COUINumberPicker cOUINumberPicker3 = this.f6892w;
            if (cOUINumberPicker3 != null) {
                cOUINumberPicker3.x(context.getResources().getString(R$string.coui_minute) + string);
            }
            COUINumberPicker cOUINumberPicker4 = this.f6893x;
            if (cOUINumberPicker4 != null) {
                cOUINumberPicker4.x(context.getResources().getString(R$string.coui_minute) + string);
            }
        }
        setImportantForAccessibility(1);
    }
}
