package com.oplus.powermanager.fuelgaue.basic.customized;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.provider.Settings;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import b6.LocalLog;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.view.batteryUI.BatteryLevelView;
import f3.COUIToolTips;
import f6.ChargeUtil;
import f6.f;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import t8.BatteryRemainTimeCalculator;
import t8.PowerUsageManager;
import y5.AppFeature;
import y5.b;

/* loaded from: classes.dex */
public class BatteryLevelViewPreference extends Preference {

    /* renamed from: e, reason: collision with root package name */
    private Context f10208e;

    /* renamed from: f, reason: collision with root package name */
    private BatteryLevelView f10209f;

    /* renamed from: g, reason: collision with root package name */
    private Button f10210g;

    /* renamed from: h, reason: collision with root package name */
    private COUIToolTips f10211h;

    /* renamed from: i, reason: collision with root package name */
    private TextView f10212i;

    /* renamed from: j, reason: collision with root package name */
    private TextView f10213j;

    /* renamed from: k, reason: collision with root package name */
    private ImageView f10214k;

    /* renamed from: l, reason: collision with root package name */
    private TextView f10215l;

    /* renamed from: m, reason: collision with root package name */
    private TextView f10216m;

    /* renamed from: n, reason: collision with root package name */
    private int f10217n;

    /* loaded from: classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            BatteryLevelViewPreference.this.j(view);
        }
    }

    public BatteryLevelViewPreference(Context context) {
        this(context, null);
        this.f10208e = context;
    }

    private String d(int i10) {
        long s7 = PowerUsageManager.x(this.f10208e).s(PowerUsageManager.x(this.f10208e).r(), i10);
        Context context = this.f10208e;
        return context.getString(R.string.battery_ui_optimization_remain_time_new, BatteryRemainTimeCalculator.d(context, s7));
    }

    private int e() {
        return Settings.System.getIntForUser(this.f10208e.getContentResolver(), "oplus_battery_settings_bms_heat_status", 0, 0);
    }

    private String f() {
        BatteryManager batteryManager = (BatteryManager) this.f10208e.getSystemService("batterymanager");
        if (f.m0(this.f10208e) == 1) {
            return this.f10208e.getResources().getString(R.string.estimate_charge_full_with_full_charge);
        }
        if (f.H0(this.f10208e) == 1) {
            Context context = this.f10208e;
            return context.getString(R.string.estimate_charge_full_with_protect_time_minute, ChargeUtil.z(f.G0(context)));
        }
        long c10 = ChargeUtil.c(this.f10208e);
        if (c10 == -99) {
            c10 = batteryManager.computeChargeTimeRemaining();
        }
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        int hours = (int) (timeUnit.toHours(c10) - TimeUnit.DAYS.toHours(timeUnit.toDays(c10)));
        int minutes = (int) (timeUnit.toMinutes(c10) - TimeUnit.HOURS.toMinutes(timeUnit.toHours(c10)));
        LocalLog.a("BatteryLevelViewPreference", "chargeTimeRemaining = " + c10 + ", hour = " + hours + ", minute = " + minutes);
        if (c10 > 0) {
            return (hours <= 0 || minutes <= 0) ? (hours <= 0 || minutes != 0) ? (hours != 0 || minutes <= 0) ? (hours == 0 && minutes == 0) ? this.f10208e.getString(R.string.estimate_charge_full_time_minute, 1) : "" : this.f10208e.getString(R.string.estimate_charge_full_time_minute, Integer.valueOf(minutes)) : this.f10208e.getString(R.string.estimate_charge_full_time_hour, Integer.valueOf(hours)) : this.f10208e.getString(R.string.estimate_charge_full_time_hour_minute, Integer.valueOf(hours), Integer.valueOf(minutes));
        }
        return this.f10208e.getString(R.string.estimate_charge_full_with_too_slow_time_minute);
    }

    private int g() {
        return Settings.System.getIntForUser(this.f10208e.getContentResolver(), "oplus_battery_settings_plugged_type", -1, 0);
    }

    private String h(int i10) {
        return String.format(Locale.getDefault(), "%d", Integer.valueOf(i10));
    }

    private String i() {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(this.f10208e.getResources().getConfiguration().locale);
        String string = this.f10208e.getResources().getString(R.string.battery_percentage);
        if (!string.equals("%")) {
            LocalLog.a("BatteryLevelViewPreference", "percent = " + string);
            return string;
        }
        return String.valueOf(decimalFormatSymbols.getPercent());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j(View view) {
        if (this.f10211h == null) {
            this.f10211h = new COUIToolTips(this.f10208e, 1);
        }
        this.f10211h.z(new SpannableString(this.f10208e.getText(R.string.battery_life_mode_notificate_details)));
        this.f10211h.A(true);
        this.f10211h.B(view, 128);
    }

    private void l(int i10) {
        m();
        if (i10 == -1) {
            this.f10217n = -1;
            p();
        } else {
            o(i10);
        }
    }

    private void m() {
        if (this.f10212i == null) {
            LocalLog.a("BatteryLevelViewPreference", "LevelTextview is null");
            return;
        }
        this.f10212i.setText(h(PowerUsageManager.x(this.f10208e).r()) + i());
    }

    private void o(int i10) {
        String f10;
        Drawable drawable;
        String string;
        if (this.f10213j != null && this.f10214k != null) {
            if (i10 == 0 && this.f10217n > 0) {
                LocalLog.l("BatteryLevelViewPreference", "ignore charge type change from vooc to normal");
                return;
            }
            this.f10217n = i10;
            this.f10208e.getResources().getString(R.string.battery_view_graph_charging);
            if (f.M(this.f10208e)) {
                String.format(this.f10208e.getString(R.string.Expected_minute_full_time), 3);
            }
            int e10 = e();
            if (i10 == -2) {
                f10 = this.f10208e.getResources().getString(R.string.estimate_charge_full_with_full_charge);
            } else if (e10 == 1) {
                f10 = this.f10208e.getResources().getString(R.string.extremely_cold_mode_charge_status);
            } else {
                f10 = f();
            }
            LocalLog.a("BatteryLevelViewPreference", "charge type = " + i10 + " text = " + f10);
            this.f10216m.setText(f10);
            if (i10 != 0 && i10 != -2) {
                if ((this.f10208e.getResources().getConfiguration().getLocales().get(0) != null ? this.f10208e.getResources().getConfiguration().getLocales().get(0).getLanguage() : "").equals("zh")) {
                    if (i10 != 33) {
                        switch (i10) {
                            case 1:
                                string = this.f10208e.getString(R.string.flash_charge);
                                break;
                            case 2:
                            case 3:
                            case 5:
                                string = this.f10208e.getString(R.string.super_flash_charge);
                                break;
                            case 4:
                                string = this.f10208e.getString(R.string.wireless_super_flash_charge);
                                break;
                            case 6:
                                if (AppFeature.e()) {
                                    string = this.f10208e.getString(R.string.super_flash_charge);
                                    break;
                                } else {
                                    string = this.f10208e.getString(R.string.rm_battery_speed_charge);
                                    break;
                                }
                            default:
                                return;
                        }
                    } else if (AppFeature.e() && b.D()) {
                        string = this.f10208e.getString(R.string.super_flash_charge);
                    } else {
                        string = this.f10208e.getString(R.string.flash_charge);
                    }
                    this.f10213j.setText(string);
                    this.f10214k.setVisibility(8);
                    this.f10213j.setVisibility(0);
                    this.f10215l.setVisibility(0);
                    return;
                }
                if (i10 != 33) {
                    switch (i10) {
                        case 1:
                            if (b.D()) {
                                if (AppFeature.e()) {
                                    drawable = this.f10208e.getDrawable(R.drawable.battery_ui_charging_vooc);
                                    break;
                                } else {
                                    drawable = this.f10208e.getDrawable(R.drawable.rm_battery_ui_charging_dart);
                                    break;
                                }
                            } else {
                                drawable = this.f10208e.getDrawable(R.drawable.battery_ui_charging_vooc);
                                break;
                            }
                        case 2:
                        case 3:
                        case 5:
                            if (b.D()) {
                                if (AppFeature.e()) {
                                    drawable = this.f10208e.getDrawable(R.drawable.battery_ui_charging_supervooc);
                                    break;
                                } else {
                                    drawable = this.f10208e.getDrawable(R.drawable.rm_battery_ui_charging_superdart);
                                    break;
                                }
                            } else {
                                drawable = this.f10208e.getDrawable(R.drawable.battery_ui_charging_supervooc);
                                break;
                            }
                        case 4:
                            drawable = this.f10208e.getDrawable(R.drawable.battery_ui_charging_airvooc);
                            break;
                        case 6:
                            if (AppFeature.e()) {
                                drawable = getContext().getDrawable(R.drawable.battery_ui_charging_supervooc);
                                break;
                            } else {
                                drawable = getContext().getDrawable(R.drawable.rm_battery_ui_charging_ultradart);
                                break;
                            }
                        default:
                            return;
                    }
                } else if (AppFeature.e() && b.D()) {
                    drawable = this.f10208e.getDrawable(R.drawable.battery_ui_charging_supervooc);
                } else {
                    drawable = this.f10208e.getDrawable(R.drawable.rm_battery_ui_charging_dart);
                }
                if (drawable == null) {
                    return;
                }
                this.f10214k.setImageDrawable(drawable);
                this.f10214k.setVisibility(0);
                this.f10213j.setVisibility(8);
                this.f10215l.setVisibility(0);
                return;
            }
            this.f10214k.setVisibility(8);
            this.f10213j.setVisibility(8);
            this.f10215l.setVisibility(8);
            return;
        }
        LocalLog.a("BatteryLevelViewPreference", "mChargeTextview or mChargeImageView is null");
    }

    private void p() {
        if (this.f10216m == null) {
            LocalLog.a("BatteryLevelViewPreference", "LeftTimeTextview is null");
            return;
        }
        this.f10216m.setText(d(-1));
        this.f10214k.setVisibility(8);
        this.f10213j.setVisibility(8);
        this.f10215l.setVisibility(8);
    }

    public void n() {
        if (this.f10209f != null) {
            notifyChanged();
        } else {
            LocalLog.a("BatteryLevelViewPreference", "battery level view null");
        }
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        int g6 = g();
        if (LocalLog.f()) {
            LocalLog.a("BatteryLevelViewPreference", "onBindViewHolder " + g6);
        }
        f.V1(true);
        BatteryLevelView batteryLevelView = (BatteryLevelView) preferenceViewHolder.a(R.id.battery_ui_level_view);
        this.f10209f = batteryLevelView;
        batteryLevelView.setChargeType(g6);
        this.f10209f.invalidate();
        this.f10212i = (TextView) preferenceViewHolder.a(R.id.battery_level_text);
        this.f10213j = (TextView) preferenceViewHolder.a(R.id.battery_charge_text);
        this.f10215l = (TextView) preferenceViewHolder.a(R.id.battery_charge_divider);
        this.f10214k = (ImageView) preferenceViewHolder.a(R.id.battery_charge_icon);
        this.f10216m = (TextView) preferenceViewHolder.a(R.id.battery_left_time_text);
        l(g6);
        Button button = (Button) preferenceViewHolder.a(R.id.battery_life_mode_notificate);
        this.f10210g = button;
        if (button.getLineCount() == 2) {
            this.f10210g.setBackground(this.f10208e.getResources().getDrawable(R.drawable.ic_battery_life_mode_notificate_double));
        }
        this.f10210g.setOnClickListener(new a());
        if (!AppFeature.j() || AppFeature.k() != 1 || AppFeature.d()) {
            this.f10210g.setVisibility(8);
        }
        super.onBindViewHolder(preferenceViewHolder);
    }

    public BatteryLevelViewPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
        this.f10208e = context;
    }

    public BatteryLevelViewPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
        this.f10208e = context;
    }

    public BatteryLevelViewPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f10217n = -1;
        setLayoutResource(R.layout.battery_level_view_preference_layout);
        this.f10208e = context;
    }
}
