package com.oplus.powermanager.fuelgaue.basic.customized;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import b6.LocalLog;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.picker.COUITimeLimitPicker;
import com.coui.appcompat.touchsearchview.COUIAccessibilityUtil;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.basic.customized.CustomTimePicker;
import f6.f;
import java.text.DateFormatSymbols;
import java.util.Locale;

/* loaded from: classes.dex */
public class LowPowerChargTimePicker extends Preference implements View.OnClickListener, CustomTimePicker.a {

    /* renamed from: e, reason: collision with root package name */
    private TextView f10232e;

    /* renamed from: f, reason: collision with root package name */
    private TextView f10233f;

    /* renamed from: g, reason: collision with root package name */
    private CustomTimePicker f10234g;

    /* renamed from: h, reason: collision with root package name */
    private CustomTimePicker f10235h;

    /* renamed from: i, reason: collision with root package name */
    private ContentResolver f10236i;

    /* renamed from: j, reason: collision with root package name */
    private int f10237j;

    /* renamed from: k, reason: collision with root package name */
    private int f10238k;

    /* renamed from: l, reason: collision with root package name */
    private int f10239l;

    /* renamed from: m, reason: collision with root package name */
    private int f10240m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f10241n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f10242o;

    /* renamed from: p, reason: collision with root package name */
    private d f10243p;

    /* renamed from: q, reason: collision with root package name */
    private ViewStub f10244q;

    /* renamed from: r, reason: collision with root package name */
    private ViewStub f10245r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f10246s;

    /* renamed from: t, reason: collision with root package name */
    private Context f10247t;

    /* renamed from: u, reason: collision with root package name */
    private Handler f10248u;

    /* loaded from: classes.dex */
    class a extends Handler {
        a() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 100) {
                return;
            }
            LowPowerChargTimePicker.this.v((CustomTimePicker) message.obj);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements COUITimeLimitPicker.i {
        b() {
        }

        @Override // com.coui.appcompat.picker.COUITimeLimitPicker.i
        public void a(COUITimeLimitPicker cOUITimeLimitPicker, int i10, int i11) {
            LowPowerChargTimePicker.this.f10237j = i10;
            LowPowerChargTimePicker.this.f10238k = i11;
            LowPowerChargTimePicker.this.s(cOUITimeLimitPicker);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements COUITimeLimitPicker.i {
        c() {
        }

        @Override // com.coui.appcompat.picker.COUITimeLimitPicker.i
        public void a(COUITimeLimitPicker cOUITimeLimitPicker, int i10, int i11) {
            LowPowerChargTimePicker.this.f10239l = i10;
            LowPowerChargTimePicker.this.f10240m = i11;
            LowPowerChargTimePicker.this.s(cOUITimeLimitPicker);
        }
    }

    /* loaded from: classes.dex */
    public interface d {
        void a();
    }

    public LowPowerChargTimePicker(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f10241n = false;
        this.f10242o = false;
        this.f10248u = new a();
        LocalLog.a("LowPowerChargTimePicker", "LowPowerChargTimePicker: oncreate ");
        this.f10247t = context.getApplicationContext();
        this.f10236i = context.getContentResolver();
        this.f10246s = DateFormat.is24HourFormat(getContext());
        setLayoutResource(R.layout.time_picker_layout);
    }

    private void m() {
        if (this.f10234g == null || r(this.f10244q)) {
            ViewGroup viewGroup = (ViewGroup) this.f10244q.getParent();
            this.f10244q.inflate();
            this.f10234g = (CustomTimePicker) viewGroup.findViewById(R.id.open_time_picker);
            o();
        }
        if (this.f10235h == null || r(this.f10245r)) {
            ViewGroup viewGroup2 = (ViewGroup) this.f10245r.getParent();
            this.f10245r.inflate();
            this.f10235h = (CustomTimePicker) viewGroup2.findViewById(R.id.close_time_picker);
            n();
        }
    }

    private void n() {
        if (this.f10235h != null) {
            this.f10235h.setIs24HourView(Boolean.valueOf(DateFormat.is24HourFormat(getContext())));
            this.f10235h.setVisibility(this.f10242o ? 0 : 8);
            this.f10235h.setTextVisibility(false);
            this.f10235h.setCurrentHour(Integer.valueOf(this.f10239l));
            this.f10235h.setCurrentMinute(Integer.valueOf(this.f10240m));
            this.f10235h.setOnTouchEndListener(this);
            this.f10235h.setOnTimeChangedListener(new c());
        }
    }

    private void o() {
        if (this.f10234g != null) {
            this.f10234g.setIs24HourView(Boolean.valueOf(DateFormat.is24HourFormat(getContext())));
            this.f10234g.setVisibility(this.f10241n ? 0 : 8);
            this.f10234g.setTextVisibility(false);
            this.f10234g.setCurrentHour(Integer.valueOf(this.f10237j));
            this.f10234g.setCurrentMinute(Integer.valueOf(this.f10238k));
            this.f10234g.setOnTouchEndListener(this);
            this.f10234g.setOnTimeChangedListener(new b());
        }
    }

    private void p() {
        this.f10237j = f.s(this.f10247t);
        this.f10238k = f.t(this.f10247t);
        this.f10239l = f.J(this.f10247t);
        this.f10240m = f.K(this.f10247t);
        LocalLog.a("LowPowerChargTimePicker", "initTimeData: mBeginHour is " + this.f10237j + " mBeginMinute is " + this.f10238k + " mEndHour is " + this.f10239l + " mEndMinute is " + this.f10240m);
        if (this.f10237j == 0 && this.f10238k == 0 && this.f10239l == 0 && this.f10240m == 0) {
            int D1 = f.D1("silent_mode_type_custom", "beginHour");
            this.f10237j = D1;
            f.W1(this.f10247t, D1);
        }
        this.f10232e.setText(i(this.f10237j, this.f10238k, this.f10246s));
        String i10 = i(this.f10239l, this.f10240m, this.f10246s);
        int i11 = this.f10237j;
        int i12 = this.f10239l;
        if (i11 > i12 || (i11 == i12 && this.f10238k > this.f10240m)) {
            i10 = this.f10247t.getString(R.string.power_save_open_time_format, i10);
        }
        this.f10233f.setText(i10);
        t();
    }

    private void q(View view) {
        View findViewById = view.findViewById(R.id.open_time_root);
        View findViewById2 = view.findViewById(R.id.close_time_root);
        findViewById.setOnClickListener(this);
        findViewById2.setOnClickListener(this);
        this.f10244q = (ViewStub) view.findViewById(R.id.time_set_open_stub);
        this.f10245r = (ViewStub) view.findViewById(R.id.time_set_close_stub);
        this.f10232e = (TextView) view.findViewById(R.id.open_time);
        this.f10233f = (TextView) view.findViewById(R.id.close_time);
        p();
        w();
        if (this.f10241n || this.f10242o) {
            m();
        }
    }

    private boolean r(ViewStub viewStub) {
        return (viewStub == null || viewStub.getParent() == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s(COUITimeLimitPicker cOUITimeLimitPicker) {
        if (this.f10248u.hasMessages(100)) {
            this.f10248u.removeMessages(100);
        }
        this.f10248u.sendMessageDelayed(Message.obtain(this.f10248u, 100, cOUITimeLimitPicker), 500L);
    }

    private void t() {
        f.E3("silent_mode_type_custom", this.f10237j + ":" + this.f10238k + "-" + this.f10239l + ":" + this.f10240m, this.f10247t);
        f.W1(this.f10247t, this.f10237j);
        f.X1(this.f10247t, this.f10238k);
        f.j2(this.f10247t, this.f10239l);
        f.k2(this.f10247t, this.f10240m);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void v(CustomTimePicker customTimePicker) {
        int i10;
        if (customTimePicker.k()) {
            CustomTimePicker customTimePicker2 = this.f10234g;
            if (customTimePicker == customTimePicker2) {
                int i11 = this.f10237j;
                if (i11 == this.f10239l && (i10 = this.f10238k) == this.f10240m) {
                    if (i10 == 0) {
                        this.f10238k = 59;
                        int i12 = i11 - 1;
                        this.f10237j = i12;
                        if (i12 == -1) {
                            this.f10237j = 23;
                        }
                    } else {
                        this.f10238k = i10 - 1;
                    }
                    int i13 = this.f10237j;
                    int i14 = this.f10238k;
                    customTimePicker2.setCurrentHour(Integer.valueOf(i13));
                    this.f10234g.setCurrentMinute(Integer.valueOf(i14));
                    this.f10232e.setText(i(i13, i14, this.f10234g.e()));
                } else {
                    this.f10232e.setText(i(i11, this.f10238k, customTimePicker2.e()));
                    String i15 = i(this.f10239l, this.f10240m, this.f10235h.e());
                    int i16 = this.f10237j;
                    int i17 = this.f10239l;
                    if (i16 > i17 || (i16 == i17 && this.f10238k > this.f10240m)) {
                        i15 = this.f10247t.getString(R.string.power_save_open_time_format, i15);
                    }
                    this.f10233f.setText(i15);
                }
            } else {
                int i18 = this.f10237j;
                int i19 = this.f10239l;
                if (i18 == i19) {
                    int i20 = this.f10238k;
                    int i21 = this.f10240m;
                    if (i20 == i21) {
                        if (i21 == 59) {
                            this.f10240m = 0;
                            int i22 = i19 + 1;
                            this.f10239l = i22;
                            if (i22 == 24) {
                                this.f10239l = 0;
                            }
                        } else {
                            this.f10240m = i21 + 1;
                        }
                        int i23 = this.f10239l;
                        int i24 = this.f10240m;
                        this.f10235h.setCurrentHour(Integer.valueOf(i23));
                        this.f10235h.setCurrentMinute(Integer.valueOf(i24));
                        this.f10233f.setText(i(i23, i24, this.f10235h.e()));
                    }
                }
                String i25 = i(i19, this.f10240m, this.f10235h.e());
                int i26 = this.f10237j;
                int i27 = this.f10239l;
                if (i26 > i27 || (i26 == i27 && this.f10238k > this.f10240m)) {
                    i25 = this.f10247t.getString(R.string.power_save_open_time_format, i25);
                }
                this.f10233f.setText(i25);
            }
            t();
            d dVar = this.f10243p;
            if (dVar != null) {
                dVar.a();
            }
        }
    }

    private void w() {
        this.f10232e.setSelected(this.f10241n);
        this.f10233f.setSelected(this.f10242o);
    }

    @Override // com.oplus.powermanager.fuelgaue.basic.customized.CustomTimePicker.a
    public void b(CustomTimePicker customTimePicker) {
        s(customTimePicker);
    }

    public String i(int i10, int i11, boolean z10) {
        String str;
        StringBuilder sb2 = new StringBuilder();
        boolean equals = "zh".equals(Locale.getDefault().getLanguage());
        if (z10) {
            sb2.append(String.format(Locale.getDefault(), "%02d", Integer.valueOf(i10)));
            str = "";
        } else {
            String str2 = new DateFormatSymbols().getAmPmStrings()[i10 < 12 ? (char) 0 : (char) 1];
            if (equals) {
                sb2.append(str2);
            }
            if (i10 == 0) {
                i10 = 12;
            }
            if (i10 > 12) {
                i10 -= 12;
            }
            sb2.append(String.format(Locale.getDefault(), "%d", Integer.valueOf(i10)));
            str = str2;
        }
        sb2.append(j());
        sb2.append(String.format(Locale.getDefault(), "%02d", Integer.valueOf(i11)));
        if (!equals) {
            sb2.append(str);
        }
        LocalLog.a("LowPowerChargTimePicker", "getFormatTimeString: " + sb2.toString());
        return sb2.toString();
    }

    public char j() {
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "Hm");
        int lastIndexOf = bestDateTimePattern.lastIndexOf(72) + 1;
        return lastIndexOf < bestDateTimePattern.length() ? bestDateTimePattern.charAt(lastIndexOf) : COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR;
    }

    public void l() {
        if (this.f10241n) {
            this.f10234g.setVisibility(8);
            this.f10241n = false;
        }
        if (this.f10242o) {
            this.f10235h.setVisibility(8);
            this.f10242o = false;
        }
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        q(preferenceViewHolder.itemView);
        COUICardListHelper.d(preferenceViewHolder.itemView, COUICardListHelper.b(this));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id2 = view.getId();
        if (id2 == R.id.open_time_root) {
            m();
            if (this.f10234g.getVisibility() != 0) {
                this.f10234g.setVisibility(0);
                this.f10235h.setVisibility(8);
                this.f10241n = true;
                this.f10242o = false;
            } else {
                this.f10234g.setVisibility(8);
                this.f10241n = false;
            }
        } else if (id2 == R.id.close_time_root) {
            m();
            if (this.f10235h.getVisibility() != 0) {
                this.f10235h.setVisibility(0);
                this.f10234g.setVisibility(8);
                this.f10241n = false;
                this.f10242o = true;
            } else {
                this.f10235h.setVisibility(8);
                this.f10242o = false;
            }
        }
        w();
    }

    public void u(d dVar) {
        this.f10243p = dVar;
    }

    public LowPowerChargTimePicker(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public LowPowerChargTimePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }
}
