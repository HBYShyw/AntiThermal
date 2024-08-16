package s6;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HardwarePropertiesManager;
import android.os.Looper;
import android.os.SystemClock;
import b6.LocalLog;
import s6.ThermalFactory;

/* compiled from: ToleranceThermalManager.java */
/* renamed from: s6.j, reason: use source file name */
/* loaded from: classes.dex */
public class ToleranceThermalManager extends ThermalManager {
    private HardwarePropertiesManager S;
    private String T;
    private long U;
    private boolean V;
    private long W;
    private Handler X;
    private Runnable Y;

    /* compiled from: ToleranceThermalManager.java */
    /* renamed from: s6.j$a */
    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ToleranceThermalManager.this.V = true;
            int o10 = ToleranceThermalManager.this.o();
            LocalLog.l("ToleranceThermalManager", "mCheckRun temperature=" + ToleranceThermalManager.this.W + " comparedTemp=" + o10 + " threshold=" + ToleranceThermalManager.this.f18129j + " mConfigType=" + ToleranceThermalManager.this.T);
            if (ToleranceThermalManager.this.T != "TypeTolerance") {
                long j10 = o10 - ToleranceThermalManager.this.W;
                ToleranceThermalManager toleranceThermalManager = ToleranceThermalManager.this;
                if (j10 < toleranceThermalManager.f18129j) {
                    long j11 = toleranceThermalManager.W;
                    ToleranceThermalManager toleranceThermalManager2 = ToleranceThermalManager.this;
                    if (j11 < toleranceThermalManager2.f18130k) {
                        toleranceThermalManager2.T = "TypeTolerance";
                        ToleranceThermalManager.this.b();
                    }
                }
            }
        }
    }

    public ToleranceThermalManager(ThermalFactory.b bVar, Context context, Looper looper) {
        super(bVar, context, looper);
        this.T = "TypeNormal";
        this.U = 0L;
        this.V = false;
        this.Y = new a();
        this.S = (HardwarePropertiesManager) context.getSystemService("hardware_properties");
        this.X = new Handler(looper);
    }

    private float K() {
        float[] deviceTemperatures = this.S.getDeviceTemperatures(3, 0);
        if (deviceTemperatures.length <= 0) {
            LocalLog.a("ToleranceThermalManager", "getToleranceComparedTempInner  return 1000");
            return 1000.0f;
        }
        return deviceTemperatures[0] * 10.0f;
    }

    @Override // s6.ThermalManager
    protected void b() {
        if (this.T == "TypeTolerance") {
            int i10 = this.f18125f;
            if (i10 != -999) {
                this.f18142w = i10;
            } else {
                this.f18142w = this.f18138s;
                LocalLog.l("ToleranceThermalManager", "configThreshold  ToleranceFirstStepIn unconfiged.");
            }
            int i11 = this.f18126g;
            if (i11 != -999) {
                this.f18143x = i11;
            } else {
                this.f18143x = this.f18139t;
                LocalLog.l("ToleranceThermalManager", "configThreshold  ToleranceFirstStepOut unconfiged.");
            }
            int i12 = this.f18127h;
            if (i12 != -999) {
                this.f18144y = i12;
            } else {
                this.f18144y = this.f18140u;
                LocalLog.l("ToleranceThermalManager", "configThreshold  ToleranceSecondStepIn unconfiged.");
            }
            int i13 = this.f18128i;
            if (i13 != -999) {
                this.f18145z = i13;
            } else {
                this.f18145z = this.f18141v;
                LocalLog.l("ToleranceThermalManager", "configThreshold  ToleranceSecondStepOut unconfiged.");
            }
        } else {
            this.f18142w = this.f18138s;
            this.f18143x = this.f18139t;
            this.f18144y = this.f18140u;
            this.f18145z = this.f18141v;
        }
        LocalLog.l("ToleranceThermalManager", "configThreshold  configType=" + this.T + ", firstStepTempIn=" + this.f18142w + ", firstStepTempOut=" + this.f18143x + ", secondStepTempIn=" + this.f18144y + ", secondStepTempOut=" + this.f18145z);
    }

    @Override // s6.ThermalManager
    public String d() {
        return super.d() + "ConfigType=" + this.T + "\nToleranceStart=" + this.f18131l + "\nToleranceStop=" + this.f18132m + "\nToleranceExceptionTemp=" + this.f18130k + "\nToleranceTime=" + this.f18133n + "\nToleranceThreshold=" + this.f18129j + "\n\nOriginalParameter:\nOriginalFirstStepIn=" + this.f18138s + "\nOriginalFirstStepOut=" + this.f18139t + "\nOriginalSecondStepIn=" + this.f18140u + "\nOriginalSecondStepOut=" + this.f18141v + "\nToleranceFirstStepIn=" + this.f18125f + "\nToleranceFirstStepOut=" + this.f18126g + "\nToleranceSecondStepIn=" + this.f18127h + "\nToleranceSecondStepOut=" + this.f18128i + "\n";
    }

    @Override // s6.ThermalManager
    public int o() {
        if (this.M > 0) {
            LocalLog.l("ToleranceThermalManager", "getToleranceComparedTemp simu " + this.M);
            return this.M;
        }
        return (int) K();
    }

    @Override // s6.ThermalManager
    public void v(Bundle bundle) {
        if (!this.L) {
            LocalLog.a("ToleranceThermalManager", "skip onTemperatureChanged for other user");
            return;
        }
        int i10 = bundle.getInt("temperature");
        int o10 = o();
        this.W = i10;
        long uptimeMillis = SystemClock.uptimeMillis();
        if (uptimeMillis - this.U >= 60000) {
            LocalLog.a("ToleranceThermalManager", "onTemperatureChanged battery=" + this.W + ", comparedTemp=" + o10 + ", treshold=" + this.f18129j + ", configType=" + this.T);
            this.U = uptimeMillis;
        }
        if (i10 >= this.f18131l && !this.V) {
            if (this.T != "TypeTolerance" && o10 - this.W < this.f18129j) {
                this.X.postDelayed(this.Y, this.f18133n);
                LocalLog.l("ToleranceThermalManager", "onTemperatureChanged: postDelayed config to tolerance. batttemp=" + i10 + ", comparedTemp=" + o10 + ", threshold=" + this.f18129j);
            } else {
                this.V = true;
            }
        }
        if (i10 <= this.f18132m) {
            this.V = false;
            if (this.T != "TypeNormal") {
                this.T = "TypeNormal";
                b();
                LocalLog.l("ToleranceThermalManager", "onTemperatureChanged: config to normal. batttemp=" + i10 + ", stop=" + this.f18132m);
            }
        }
        super.v(bundle);
    }
}
