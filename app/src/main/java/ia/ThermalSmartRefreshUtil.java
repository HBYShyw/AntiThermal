package ia;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import b6.LocalLog;
import com.oplus.battery.R;
import com.oplus.thermalcontrol.ThermalControlUtils;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import java.util.ArrayList;

/* compiled from: ThermalSmartRefreshUtil.java */
/* renamed from: ia.d, reason: use source file name */
/* loaded from: classes2.dex */
public class ThermalSmartRefreshUtil {

    /* renamed from: e, reason: collision with root package name */
    private static String f12697e = "ThermalSmartRefreshUtil";

    /* renamed from: f, reason: collision with root package name */
    private static volatile ThermalSmartRefreshUtil f12698f;

    /* renamed from: a, reason: collision with root package name */
    private Context f12699a;

    /* renamed from: b, reason: collision with root package name */
    private int f12700b = -2;

    /* renamed from: c, reason: collision with root package name */
    private ArrayList<Float> f12701c = new ArrayList<>();

    /* renamed from: d, reason: collision with root package name */
    private Handler f12702d = new a();

    /* compiled from: ThermalSmartRefreshUtil.java */
    /* renamed from: ia.d$a */
    /* loaded from: classes2.dex */
    class a extends Handler {
        a() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 22 || ThermalSmartRefreshUtil.this.f12701c == null || ThermalSmartRefreshUtil.this.f12701c.size() > 24) {
                return;
            }
            float currentTemperature = ThermalControlUtils.getInstance(ThermalSmartRefreshUtil.this.f12699a).getCurrentTemperature(false);
            LocalLog.a(ThermalSmartRefreshUtil.f12697e, "MSG_THERMAL_CONTROL_TEMPERATURE_MONITOR temperature =" + currentTemperature);
            ThermalSmartRefreshUtil.this.f12701c.add(Float.valueOf(currentTemperature));
            if (ThermalSmartRefreshUtil.this.f12701c.size() != 12 && ThermalSmartRefreshUtil.this.f12701c.size() != 24) {
                Message obtainMessage = ThermalSmartRefreshUtil.this.f12702d.obtainMessage();
                obtainMessage.what = 22;
                ThermalSmartRefreshUtil.this.f12702d.sendMessageDelayed(obtainMessage, 5000L);
                return;
            }
            long j10 = 0;
            for (int i10 = 0; i10 < ThermalSmartRefreshUtil.this.f12701c.size(); i10++) {
                j10 = ((float) j10) + ((Float) ThermalSmartRefreshUtil.this.f12701c.get(i10)).floatValue();
            }
            if (j10 / ThermalSmartRefreshUtil.this.f12701c.size() > 45) {
                ThermalControlUtils.getInstance(ThermalSmartRefreshUtil.this.f12699a).setRefreshRate(ThermalSmartRefreshUtil.this.f12700b);
                ThermalSmartRefreshUtil.this.f12701c.clear();
            } else if (ThermalSmartRefreshUtil.this.f12701c.size() == 24) {
                ThermalControlUtils.getInstance(ThermalSmartRefreshUtil.this.f12699a).setRefreshRate(ThermalSmartRefreshUtil.this.f12700b);
                ThermalSmartRefreshUtil.this.f12701c.clear();
            } else {
                Message obtainMessage2 = ThermalSmartRefreshUtil.this.f12702d.obtainMessage();
                obtainMessage2.what = 22;
                ThermalSmartRefreshUtil.this.f12702d.sendMessageDelayed(obtainMessage2, 5000L);
            }
        }
    }

    public ThermalSmartRefreshUtil(Context context) {
        this.f12699a = context;
    }

    public static ThermalSmartRefreshUtil f(Context context) {
        if (f12698f == null) {
            synchronized (ThermalSmartRefreshUtil.class) {
                if (f12698f == null) {
                    f12698f = new ThermalSmartRefreshUtil(context);
                }
            }
        }
        return f12698f;
    }

    private boolean g(ThermalPolicy thermalPolicy) {
        this.f12700b = thermalPolicy.refreshRate;
        LocalLog.a(f12697e, "isNeedThermalMonitor mRefreshRate =" + this.f12700b + "gearLevel =" + thermalPolicy.gearLevel + "categoryName =" + thermalPolicy.categoryName);
        return thermalPolicy.gearLevel < 12 && thermalPolicy.refreshRate != 0 && h(thermalPolicy.categoryName);
    }

    private boolean h(String str) {
        String[] stringArray = this.f12699a.getResources().getStringArray(R.array.smart_refresh_app_list);
        if (stringArray == null) {
            return false;
        }
        for (String str2 : stringArray) {
            if (TextUtils.equals(str, str2)) {
                return true;
            }
        }
        return false;
    }

    private void j(boolean z10) {
        if (!this.f12701c.isEmpty()) {
            this.f12701c.clear();
        }
        if (this.f12702d.hasMessages(22)) {
            this.f12702d.removeMessages(22);
        }
        if (z10) {
            ThermalControlUtils.getInstance(this.f12699a).setRefreshRate(0);
            Message obtainMessage = this.f12702d.obtainMessage();
            obtainMessage.what = 22;
            this.f12702d.sendMessageDelayed(obtainMessage, 10000L);
        }
    }

    public void i(ThermalPolicy thermalPolicy, boolean z10) {
        if (!g(thermalPolicy)) {
            if (z10) {
                ThermalControlUtils.getInstance(this.f12699a).setRefreshRate(thermalPolicy.refreshRate);
            }
            j(false);
            return;
        }
        j(true);
    }
}
