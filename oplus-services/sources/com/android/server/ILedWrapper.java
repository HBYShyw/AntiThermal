package com.android.server;

import com.android.server.lights.LogicalLight;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ILedWrapper {
    default ILedExt getExtImpl() {
        return new ILedExt() { // from class: com.android.server.ILedWrapper.1
        };
    }

    default LogicalLight getBatteryLight() {
        return new LogicalLight() { // from class: com.android.server.ILedWrapper.2
            public void pulse() {
            }

            public void pulse(int i, int i2) {
            }

            public void setBrightness(float f) {
            }

            public void setBrightness(float f, int i) {
            }

            public void setColor(int i) {
            }

            public void setFlashing(int i, int i2, int i3, int i4) {
            }

            public void setVrMode(boolean z) {
            }

            public void turnOff() {
            }
        };
    }
}
