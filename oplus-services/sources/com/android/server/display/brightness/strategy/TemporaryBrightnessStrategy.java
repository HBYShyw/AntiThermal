package com.android.server.display.brightness.strategy;

import android.hardware.display.DisplayManagerInternal;
import com.android.server.display.DisplayBrightnessState;
import com.android.server.display.brightness.BrightnessUtils;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class TemporaryBrightnessStrategy implements DisplayBrightnessStrategy {
    private float mTemporaryScreenBrightness = Float.NaN;

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public String getName() {
        return "TemporaryBrightnessStrategy";
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public DisplayBrightnessState updateBrightness(DisplayManagerInternal.DisplayPowerRequest displayPowerRequest) {
        float f = this.mTemporaryScreenBrightness;
        return BrightnessUtils.constructDisplayBrightnessState(7, f, f, getName());
    }

    public float getTemporaryScreenBrightness() {
        return this.mTemporaryScreenBrightness;
    }

    public void setTemporaryScreenBrightness(float f) {
        this.mTemporaryScreenBrightness = f;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("TemporaryBrightnessStrategy:");
        printWriter.println("  mTemporaryScreenBrightness:" + this.mTemporaryScreenBrightness);
    }
}
