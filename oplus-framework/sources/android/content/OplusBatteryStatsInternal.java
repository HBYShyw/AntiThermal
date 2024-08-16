package android.content;

import android.os.OplusThermalState;

/* loaded from: classes.dex */
public abstract class OplusBatteryStatsInternal {
    public abstract void noteScreenBrightnessModeChangedImpl(boolean z);

    public abstract void setThermalConfigImpl();

    public abstract void setThermalStateImpl(OplusThermalState oplusThermalState);
}
