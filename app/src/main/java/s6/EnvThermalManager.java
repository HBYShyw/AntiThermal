package s6;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import b6.LocalLog;
import s6.ThermalFactory;

/* compiled from: EnvThermalManager.java */
/* renamed from: s6.b, reason: use source file name */
/* loaded from: classes.dex */
public class EnvThermalManager extends ThermalManager {
    private int S;

    public EnvThermalManager(ThermalFactory.b bVar, Context context, Looper looper) {
        super(bVar, context, looper);
        this.S = 0;
    }

    @Override // s6.ThermalManager
    protected void b() {
        if (this.S == 1) {
            this.f18142w = this.f18134o;
            this.f18143x = this.f18135p;
            this.f18144y = this.f18136q;
            this.f18145z = this.f18137r;
        } else {
            this.f18142w = this.f18138s;
            this.f18143x = this.f18139t;
            this.f18144y = this.f18140u;
            this.f18145z = this.f18141v;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("configThreshold  isEnvTempHigh=");
        sb2.append(this.S == 1);
        sb2.append(", firstStepTempIn=");
        sb2.append(this.f18142w);
        sb2.append(", firstStepTempOut=");
        sb2.append(this.f18143x);
        sb2.append(", secondStepTempIn=");
        sb2.append(this.f18144y);
        sb2.append(", secondStepTempOut=");
        sb2.append(this.f18145z);
        LocalLog.l("EnvThermalManager", sb2.toString());
    }

    @Override // s6.ThermalManager
    public String d() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(super.d());
        sb2.append("isEnvTempHigh=");
        sb2.append(this.S == 1);
        sb2.append("\n\nOriginalParameter:\nOriginalFirstStepIn=");
        sb2.append(this.f18138s);
        sb2.append("\nOriginalFirstStepOut=");
        sb2.append(this.f18139t);
        sb2.append("\nOriginalSecondStepIn=");
        sb2.append(this.f18140u);
        sb2.append("\nOriginalSecondStepOut=");
        sb2.append(this.f18141v);
        sb2.append("\nEnvTempHighFirstStepIn=");
        sb2.append(this.f18134o);
        sb2.append("\nEnvTempHighFirstStepOut=");
        sb2.append(this.f18135p);
        sb2.append("\nEnvTempHighSecondStepIn=");
        sb2.append(this.f18136q);
        sb2.append("\nEnvTempHighSecondStepOut=");
        sb2.append(this.f18137r);
        sb2.append("\n");
        return sb2.toString();
    }

    @Override // s6.ThermalManager
    public void v(Bundle bundle) {
        int i10 = bundle.getInt("environment_temp_type");
        if (this.O) {
            i10 = 1;
            LocalLog.l("EnvThermalManager", "onTemperatureChanged: SimuEnvTempHigh");
        } else if (this.P) {
            i10 = 0;
            LocalLog.l("EnvThermalManager", "onTemperatureChanged: SimuEnvTempLow");
        }
        if (this.S != i10) {
            this.S = i10;
            b();
        }
        super.v(bundle);
    }
}
