package com.oplus.powermanager.fuelgaue.basic.customized;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import b6.LocalLog;
import com.oplus.battery.R;
import f6.CommonUtil;
import f6.f;
import v1.COUIContextUtil;

/* loaded from: classes.dex */
public class BatteryUseTimePreference extends Preference {

    /* renamed from: e, reason: collision with root package name */
    private TextView f10219e;

    /* renamed from: f, reason: collision with root package name */
    private TextView f10220f;

    /* renamed from: g, reason: collision with root package name */
    private TextView f10221g;

    /* renamed from: h, reason: collision with root package name */
    private TextView f10222h;

    /* renamed from: i, reason: collision with root package name */
    private Typeface f10223i;

    /* renamed from: j, reason: collision with root package name */
    private LinearLayout f10224j;

    /* renamed from: k, reason: collision with root package name */
    private RelativeLayout f10225k;

    /* renamed from: l, reason: collision with root package name */
    private String f10226l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f10227m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f10228n;

    /* renamed from: o, reason: collision with root package name */
    private int f10229o;

    /* renamed from: p, reason: collision with root package name */
    private Context f10230p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f10231q;

    public BatteryUseTimePreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f10223i = null;
        this.f10224j = null;
        this.f10226l = "";
        this.f10227m = false;
        this.f10228n = false;
        this.f10229o = 0;
        this.f10230p = null;
        this.f10231q = false;
        setLayoutResource(R.layout.battery_use_time_preference_layout);
    }

    public void c(boolean z10) {
        this.f10231q = z10;
    }

    public void d(Context context) {
        this.f10230p = context;
    }

    public void e(boolean z10) {
        LocalLog.a("BatteryUseTimePreference", "setFirstVisibleUnplug：" + z10);
        this.f10228n = z10;
    }

    public void f() {
        LocalLog.a("BatteryUseTimePreference", "set message");
        if (this.f10220f != null && this.f10219e != null) {
            Log.d("BatteryUseTimePreference", "mShowZero:" + this.f10228n);
            long S = this.f10228n ? 0L : f.S(this.f10230p);
            String f10 = CommonUtil.f(this.f10230p, Math.min(this.f10228n ? 0L : f.q0(this.f10230p), S), false);
            String f11 = CommonUtil.f(this.f10230p, S, false);
            this.f10228n = false;
            try {
                this.f10223i = Typeface.createFromAsset(getContext().getAssets(), "font/OplusSans2.14_No.ttf");
            } catch (Exception e10) {
                LocalLog.b("BatteryUseTimePreference", e10.toString());
            }
            this.f10219e.setText(f10);
            this.f10220f.setText(f11);
            this.f10219e.setTypeface(Typeface.create("sans-serif-medium", 0));
            this.f10220f.setTypeface(Typeface.create("sans-serif-medium", 0));
            if (!this.f10227m || this.f10229o <= 0) {
                return;
            }
            StringBuilder sb2 = new StringBuilder(this.f10226l);
            LocalLog.a("BatteryUseTimePreference", "2all " + this.f10222h.getLineCount() + ", screen " + this.f10229o);
            for (int i10 = 0; i10 < this.f10222h.getLineCount() - this.f10229o; i10++) {
                sb2.append("\n");
            }
            this.f10221g.setText(sb2.toString());
            return;
        }
        LocalLog.a("BatteryUseTimePreference", "text view null");
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.f10219e = (TextView) preferenceViewHolder.a(R.id.battery_ui_screen_on_time);
        this.f10221g = (TextView) preferenceViewHolder.a(R.id.battery_ui_screen_on_text);
        this.f10220f = (TextView) preferenceViewHolder.a(R.id.battery_ui_all_use_time);
        this.f10222h = (TextView) preferenceViewHolder.a(R.id.battery_ui_all_use_text);
        this.f10224j = (LinearLayout) preferenceViewHolder.a(R.id.battery_ui_use_time_view);
        RelativeLayout relativeLayout = (RelativeLayout) preferenceViewHolder.a(R.id.battery_ui_use_time_layout);
        this.f10225k = relativeLayout;
        if (this.f10231q) {
            LocalLog.a("BatteryUseTimePreference", "setAsSinglePreference");
            this.f10225k.setBackground(getContext().getDrawable(R.drawable.ic_radius16_background));
        } else {
            relativeLayout.setBackgroundColor(COUIContextUtil.a(getContext(), R.attr.couiColorCardBackground));
        }
        LocalLog.a("BatteryUseTimePreference", "1all " + this.f10222h.getLineCount() + ", screen " + this.f10221g.getLineCount() + ", init " + this.f10227m + ", " + ((Object) this.f10221g.getText()));
        if (!this.f10227m) {
            this.f10226l = this.f10221g.getText().toString();
        }
        int i10 = this.f10229o;
        if (i10 == 0 && !this.f10227m) {
            this.f10227m = true;
            if (i10 == 0) {
                this.f10229o = this.f10221g.getLineCount();
                f();
                return;
            }
            return;
        }
        f();
    }

    public BatteryUseTimePreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public BatteryUseTimePreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BatteryUseTimePreference(Context context) {
        this(context, null);
    }
}
