package com.android.server.deviceidle;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DeviceIdleConstraintTracker {
    public final int minState;
    public final String name;
    public boolean active = false;
    public boolean monitoring = false;

    public DeviceIdleConstraintTracker(String str, int i) {
        this.name = str;
        this.minState = i;
    }
}
