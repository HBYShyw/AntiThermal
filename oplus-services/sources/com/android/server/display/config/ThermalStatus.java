package com.android.server.display.config;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public enum ThermalStatus {
    none("none"),
    light("light"),
    moderate("moderate"),
    severe("severe"),
    critical("critical"),
    emergency("emergency"),
    shutdown("shutdown");

    private final String rawName;

    ThermalStatus(String str) {
        this.rawName = str;
    }

    public String getRawName() {
        return this.rawName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ThermalStatus fromString(String str) {
        for (ThermalStatus thermalStatus : values()) {
            if (thermalStatus.getRawName().equals(str)) {
                return thermalStatus;
            }
        }
        throw new IllegalArgumentException(str);
    }
}
