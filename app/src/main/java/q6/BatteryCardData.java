package q6;

import android.content.Context;
import android.provider.Settings;
import b6.LocalLog;
import com.oplus.battery.R;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import t8.BatteryRemainTimeCalculator;
import t8.PowerUsageManager;
import t9.StartActivityClickEntity;
import v4.BatteryStatsManager;
import v4.GuardElfContext;

/* compiled from: BatteryCardData.java */
/* renamed from: q6.a, reason: use source file name */
/* loaded from: classes.dex */
public class BatteryCardData {

    /* renamed from: b, reason: collision with root package name */
    private int f16899b;

    /* renamed from: c, reason: collision with root package name */
    private int f16900c;

    /* renamed from: d, reason: collision with root package name */
    private int f16901d;

    /* renamed from: a, reason: collision with root package name */
    private Context f16898a = GuardElfContext.e().c();

    /* renamed from: e, reason: collision with root package name */
    private String f16902e = a(-1);

    public BatteryCardData(int i10, int i11, int i12) {
        this.f16899b = -1;
        this.f16900c = -1;
        this.f16901d = -1;
        this.f16899b = i10 < 0 ? BatteryStatsManager.i().c() : i10;
        this.f16900c = i11 < 0 ? BatteryStatsManager.i().d() : i11;
        this.f16901d = i12 < 0 ? BatteryStatsManager.i().j() : i12;
    }

    private String a(int i10) {
        return BatteryRemainTimeCalculator.d(this.f16898a, PowerUsageManager.x(this.f16898a).s(PowerUsageManager.x(this.f16898a).r(), i10));
    }

    public int b() {
        return this.f16899b;
    }

    public String c() {
        return String.format(Locale.getDefault(), "%d", Integer.valueOf(this.f16899b));
    }

    public int d() {
        return this.f16900c;
    }

    public String e() {
        if (this.f16900c == 5) {
            return this.f16898a.getResources().getString(R.string.one_key_oncharge_full);
        }
        return this.f16898a.getResources().getString(R.string.battery_view_graph_charging);
    }

    public StartActivityClickEntity f() {
        StartActivityClickEntity startActivityClickEntity = new StartActivityClickEntity();
        startActivityClickEntity.d("com.oplus.action.powermanager");
        startActivityClickEntity.e("com.oplus.battery");
        startActivityClickEntity.f("reason", "battery_card");
        startActivityClickEntity.c(268468224);
        return startActivityClickEntity;
    }

    public String g() {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(this.f16898a.getResources().getConfiguration().locale);
        String string = this.f16898a.getResources().getString(R.string.battery_percentage);
        if (!string.equals("%")) {
            if (LocalLog.f()) {
                LocalLog.a("BatteryCardData", "percent = " + string);
            }
            return string;
        }
        return String.valueOf(decimalFormatSymbols.getPercent());
    }

    public int h() {
        return this.f16901d;
    }

    public String i() {
        return this.f16902e;
    }

    public boolean j() {
        return Settings.Global.getInt(this.f16898a.getContentResolver(), "low_power", 0) == 1;
    }
}
