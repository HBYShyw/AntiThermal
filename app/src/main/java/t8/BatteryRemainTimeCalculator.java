package t8;

import a9.SuperEnduranceRemainCalculator;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import com.oplus.battery.R;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import f6.CommonUtil;
import java.util.ArrayList;
import w4.Affair;
import w4.IAffairCallback;
import y5.AppFeature;

/* compiled from: BatteryRemainTimeCalculator.java */
/* renamed from: t8.b, reason: use source file name */
/* loaded from: classes2.dex */
public class BatteryRemainTimeCalculator implements IAffairCallback {

    /* renamed from: h, reason: collision with root package name */
    private static ArrayList<Integer> f18635h = new ArrayList<>();

    /* renamed from: i, reason: collision with root package name */
    private static ArrayList<Double> f18636i = new ArrayList<>();

    /* renamed from: j, reason: collision with root package name */
    private static ArrayList<Double> f18637j = new ArrayList<>();

    /* renamed from: k, reason: collision with root package name */
    private static ArrayList<Double> f18638k = new ArrayList<>();

    /* renamed from: l, reason: collision with root package name */
    private static volatile BatteryRemainTimeCalculator f18639l;

    /* renamed from: e, reason: collision with root package name */
    private Context f18640e;

    /* renamed from: f, reason: collision with root package name */
    private int f18641f = 0;

    /* renamed from: g, reason: collision with root package name */
    private int f18642g = 0;

    static {
        f18635h.add(17);
        f18635h.add(22);
        f18635h.add(30);
        f18635h.add(36);
        f18635h.add(39);
        f18636i.add(Double.valueOf(-0.6d));
        f18636i.add(Double.valueOf(-0.95d));
        f18636i.add(Double.valueOf(-1.3d));
        f18636i.add(Double.valueOf(-1.5d));
        f18636i.add(Double.valueOf(-1.7d));
        f18637j.add(Double.valueOf(1.0d));
        f18637j.add(Double.valueOf(1.2d));
        f18637j.add(Double.valueOf(1.5d));
        f18637j.add(Double.valueOf(1.8d));
        f18637j.add(Double.valueOf(2.0d));
        f18638k.add(Double.valueOf(10.0d));
        f18638k.add(Double.valueOf(12.0d));
        f18638k.add(Double.valueOf(14.0d));
        f18638k.add(Double.valueOf(16.0d));
        f18638k.add(Double.valueOf(18.0d));
        f18639l = null;
    }

    private BatteryRemainTimeCalculator(Context context) {
        this.f18640e = null;
        this.f18640e = context.getApplicationContext();
    }

    private long b(int i10, int i11, int i12, int i13) {
        double d10;
        double doubleValue;
        if (i12 == 1) {
            d10 = i10;
            doubleValue = f18637j.get(i13).doubleValue();
        } else {
            if (i12 != 2) {
                if (i12 == 3) {
                    d10 = i10;
                    doubleValue = f18638k.get(i13).doubleValue();
                }
                return i10;
            }
            d10 = i10;
            doubleValue = f18636i.get(i13).doubleValue();
        }
        i10 = (int) (d10 + (doubleValue * i11));
        return i10;
    }

    public static String d(Context context, long j10) {
        long j11 = j10 % 60;
        long j12 = j10 / 60;
        long j13 = j12 / 24;
        long j14 = j12 % 24;
        int i10 = j11 > 0 ? 1 : 0;
        if (j14 > 0) {
            i10 += 2;
        }
        if (j13 > 0) {
            i10 += 4;
        }
        int i11 = 0;
        while (i10 > 0) {
            i10 &= i10 - 1;
            i11++;
        }
        return i11 > 2 ? String.format(context.getString(R.string.one_key_time_day_hour_minute), Long.valueOf(j13), Long.valueOf(j14), Long.valueOf(j11)) : i11 > 1 ? j13 > 0 ? j11 > 0 ? String.format(context.getString(R.string.one_key_time_day_minute), Long.valueOf(j13), Long.valueOf(j11)) : String.format(context.getString(R.string.one_key_time_day_hour), Long.valueOf(j13), Long.valueOf(j14)) : String.format(context.getString(R.string.one_key_time_hour_minute), Long.valueOf(j14), Long.valueOf(j11)) : i11 > 0 ? j13 > 0 ? String.format(context.getString(R.string.one_key_time_day), Long.valueOf(j13)) : j14 > 0 ? String.format(context.getString(R.string.one_key_time_hour), Long.valueOf(j14)) : String.format(context.getString(R.string.battery_history_minutes_no_seconds), Long.valueOf(j11)) : j11 <= 0 ? String.format(context.getString(R.string.one_key_time_minute), 0) : "";
    }

    public static BatteryRemainTimeCalculator e(Context context) {
        if (f18639l == null) {
            synchronized (BatteryRemainTimeCalculator.class) {
                if (f18639l == null) {
                    f18639l = new BatteryRemainTimeCalculator(context);
                }
            }
        }
        return f18639l;
    }

    private int f(int i10) {
        int intValue = i10 > 15 ? 0 + (f18635h.get(0).intValue() * (i10 - 15)) : 0;
        for (int i11 = 3; i11 <= Math.min(15, i10); i11++) {
            int i12 = i11 / 3;
            intValue += (i12 * 5) + i12;
        }
        return intValue + (Math.min(2, i10) * 5);
    }

    private int g() {
        boolean f02 = f6.f.f0(this.f18640e);
        boolean a12 = f6.f.a1(this.f18640e);
        boolean z10 = CommonUtil.A() == 1;
        if (a12) {
            return 3;
        }
        if (z10) {
            return 2;
        }
        return f02 ? 1 : 0;
    }

    private void j(int i10) {
        Settings.System.putLongForUser(this.f18640e.getContentResolver(), "remain_time_on_super_powersave", c(i10, 3), 0);
        Settings.System.putLongForUser(this.f18640e.getContentResolver(), "remain_time_on_powersave", c(i10, 1), UserHandle.myUserId());
    }

    public void a() {
        Intent registerReceiver = this.f18640e.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"), 2);
        if (registerReceiver != null) {
            int intExtra = registerReceiver.getIntExtra("level", 0);
            this.f18642g = intExtra;
            j(intExtra);
        }
        registerAction();
    }

    public long c(int i10, int i11) {
        long b10;
        int min = Math.min(i10, 100);
        if (i11 < 0) {
            i11 = g();
        }
        if (AppFeature.G() && i11 == 3) {
            return SuperEnduranceRemainCalculator.a(min);
        }
        int size = 100 / f18635h.size();
        int i12 = min / size;
        int i13 = min - (i12 * size);
        if (i12 > 0) {
            b10 = b(f(size), size, i11, 0);
        } else {
            b10 = b(f(min), min, i11, 0);
        }
        long j10 = 0 + b10;
        for (int i14 = 1; i14 < i12; i14++) {
            j10 += b(f18635h.get(i14).intValue() * size, size, i11, i14);
        }
        return (i12 <= 0 || i12 > f18635h.size() - 1) ? j10 : j10 + b(f18635h.get(i12).intValue() * i13, i13, i11, i12);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        if (i10 != 204) {
            return;
        }
        i(intent.getIntExtra("level", 0));
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    public long h(int i10, int i11) {
        long b10;
        if (i11 < 0) {
            i11 = g();
        }
        int size = 100 / f18635h.size();
        int i12 = i10 / size;
        int i13 = i10 - (i12 * size);
        if (i12 > 0) {
            b10 = b(f(size), size, i11, 0);
        } else {
            b10 = b(f(i10), i10, i11, 0);
        }
        long j10 = b10 + 0;
        for (int i14 = 1; i14 < i12; i14++) {
            j10 += b(f18635h.get(i14).intValue() * size, size, i11, i14);
        }
        return (i12 <= 0 || i12 > f18635h.size() - 1) ? j10 : j10 + b(f18635h.get(i12).intValue() * i13, i13, i11, i12);
    }

    public void i(int i10) {
        int i11 = this.f18642g;
        this.f18641f = i11;
        this.f18642g = i10;
        if (i10 == i11) {
            return;
        }
        j(i10);
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_CAMERA);
    }
}
