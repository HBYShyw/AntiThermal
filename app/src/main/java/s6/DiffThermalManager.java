package s6;

import android.content.Context;
import android.os.BatteryManager;
import android.os.Looper;
import b6.LocalLog;
import java.lang.reflect.Method;
import s6.ThermalFactory;

/* compiled from: DiffThermalManager.java */
/* renamed from: s6.a, reason: use source file name */
/* loaded from: classes.dex */
public class DiffThermalManager extends ToleranceThermalManager {
    private BatteryManager Z;

    public DiffThermalManager(ThermalFactory.b bVar, Context context, Looper looper) {
        super(bVar, context, looper);
        this.Z = (BatteryManager) context.getSystemService("batterymanager");
    }

    private float K() {
        try {
            Method method = this.Z.getClass().getMethod("getThermalZoneTemperatureFromSensor", null);
            method.setAccessible(true);
            Object invoke = method.invoke(this.Z, new Object[0]);
            if (invoke != null) {
                return ((Float) invoke).floatValue() * 10.0f;
            }
            return -1.0f;
        } catch (Exception e10) {
            LocalLog.b("DiffThermalManager", "getPanelTemperatureInner: Fail to get temperature e=" + e10);
            return -1.0f;
        }
    }

    @Override // s6.ToleranceThermalManager, s6.ThermalManager
    public int o() {
        if (this.M > 0) {
            LocalLog.l("DiffThermalManager", "getToleranceComparedTemp sumi " + this.M);
            return this.M;
        }
        return (int) K();
    }
}
