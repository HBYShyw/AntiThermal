package com.android.server.devicestate;

import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DeviceState {
    public static final int FLAG_APP_INACCESSIBLE = 2;
    public static final int FLAG_CANCEL_OVERRIDE_REQUESTS = 1;
    public static final int FLAG_CANCEL_WHEN_REQUESTER_NOT_ON_TOP = 8;
    public static final int FLAG_EMULATED_ONLY = 4;
    public static final int FLAG_UNSUPPORTED_WHEN_POWER_SAVE_MODE = 32;
    public static final int FLAG_UNSUPPORTED_WHEN_THERMAL_STATUS_CRITICAL = 16;
    private final int mFlags;
    private final int mIdentifier;
    private final String mName;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface DeviceStateFlags {
    }

    public DeviceState(int i, String str, int i2) {
        Preconditions.checkArgumentInRange(i, 0, 255, "identifier");
        this.mIdentifier = i;
        this.mName = str;
        this.mFlags = i2;
    }

    public int getIdentifier() {
        return this.mIdentifier;
    }

    public String getName() {
        return this.mName;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DeviceState{identifier=");
        sb.append(this.mIdentifier);
        sb.append(", name='");
        sb.append(this.mName);
        sb.append('\'');
        sb.append(", app_accessible=");
        sb.append(!hasFlag(2));
        sb.append(", cancel_when_requester_not_on_top=");
        sb.append(hasFlag(8));
        sb.append("}");
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || DeviceState.class != obj.getClass()) {
            return false;
        }
        DeviceState deviceState = (DeviceState) obj;
        return this.mIdentifier == deviceState.mIdentifier && Objects.equals(this.mName, deviceState.mName) && this.mFlags == deviceState.mFlags;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mIdentifier), this.mName, Integer.valueOf(this.mFlags));
    }

    public boolean hasFlag(int i) {
        return (this.mFlags & i) == i;
    }
}
