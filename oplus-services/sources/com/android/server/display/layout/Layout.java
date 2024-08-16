package com.android.server.display.layout;

import android.util.Slog;
import android.view.DisplayAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Layout {
    public static final String DEFAULT_DISPLAY_GROUP_NAME = "";
    public static final int NO_LEAD_DISPLAY = -1;
    public static final int SECOND_DEFAULT_DISPLAY = 1;
    private static final String TAG = "Layout";
    private static int sNextNonDefaultDisplayId = 2;
    private final List<Display> mDisplays = new ArrayList(2);

    public static int assignDisplayIdLocked(boolean z) {
        if (z) {
            return 0;
        }
        int i = sNextNonDefaultDisplayId;
        sNextNonDefaultDisplayId = i + 1;
        return i;
    }

    public static int assignDisplayIdLocked(boolean z, DisplayAddress displayAddress) {
        boolean z2 = false;
        if (displayAddress != null && (displayAddress instanceof DisplayAddress.Physical) && ((DisplayAddress.Physical) displayAddress).getPort() == 3) {
            z2 = true;
        }
        if (z || !z2) {
            return assignDisplayIdLocked(z);
        }
        return 1;
    }

    public String toString() {
        return this.mDisplays.toString();
    }

    public boolean equals(Object obj) {
        if (obj instanceof Layout) {
            return this.mDisplays.equals(((Layout) obj).mDisplays);
        }
        return false;
    }

    public int hashCode() {
        return this.mDisplays.hashCode();
    }

    public void createDefaultDisplayLocked(DisplayAddress displayAddress, DisplayIdProducer displayIdProducer) {
        createDisplayLocked(displayAddress, true, true, "", displayIdProducer, -1, -1, null, null, null);
    }

    public void createDisplayLocked(DisplayAddress displayAddress, boolean z, boolean z2, String str, DisplayIdProducer displayIdProducer, int i, int i2, String str2, String str3, String str4) {
        if (contains(displayAddress)) {
            Slog.w(TAG, "Attempting to add second definition for display-device: " + displayAddress);
            return;
        }
        if (z && getById(0) != null) {
            Slog.w(TAG, "Ignoring attempt to add a second default display: " + displayAddress);
            return;
        }
        String str5 = str == null ? "" : str;
        if (z && !str5.equals("")) {
            throw new IllegalArgumentException("Default display should own DEFAULT_DISPLAY_GROUP");
        }
        this.mDisplays.add(new Display(displayAddress, assignDisplayIdLocked(z, displayAddress), z2, str5, str2, i, z ? -1 : i2, str3, str4));
    }

    public void removeDisplayLocked(int i) {
        Display byId = getById(i);
        if (byId != null) {
            this.mDisplays.remove(byId);
        }
    }

    public boolean contains(DisplayAddress displayAddress) {
        int size = this.mDisplays.size();
        for (int i = 0; i < size; i++) {
            if (displayAddress.equals(this.mDisplays.get(i).getAddress())) {
                return true;
            }
        }
        return false;
    }

    public Display getById(int i) {
        for (int i2 = 0; i2 < this.mDisplays.size(); i2++) {
            Display display = this.mDisplays.get(i2);
            if (i == display.getLogicalDisplayId()) {
                return display;
            }
        }
        return null;
    }

    public Display getByAddress(DisplayAddress displayAddress) {
        for (int i = 0; i < this.mDisplays.size(); i++) {
            Display display = this.mDisplays.get(i);
            if (displayAddress.equals(display.getAddress())) {
                return display;
            }
        }
        return null;
    }

    public Display getAt(int i) {
        return this.mDisplays.get(i);
    }

    public int size() {
        return this.mDisplays.size();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Display {
        public static final int POSITION_FRONT = 0;
        public static final int POSITION_REAR = 1;
        public static final int POSITION_UNKNOWN = -1;
        private final DisplayAddress mAddress;
        private final String mDisplayGroupName;
        private final boolean mIsEnabled;
        private final int mLeadDisplayId;
        private final int mLogicalDisplayId;
        private final int mPosition;
        private final String mRefreshRateZoneId;
        private final String mThermalBrightnessThrottlingMapId;
        private final String mThermalRefreshRateThrottlingMapId;

        private Display(DisplayAddress displayAddress, int i, boolean z, String str, String str2, int i2, int i3, String str3, String str4) {
            this.mAddress = displayAddress;
            this.mLogicalDisplayId = i;
            this.mIsEnabled = z;
            this.mDisplayGroupName = str;
            this.mPosition = i2;
            this.mThermalBrightnessThrottlingMapId = str2;
            this.mRefreshRateZoneId = str3;
            this.mThermalRefreshRateThrottlingMapId = str4;
            this.mLeadDisplayId = i3;
        }

        public String toString() {
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("{dispId: ");
            sb.append(this.mLogicalDisplayId);
            sb.append("(");
            sb.append(this.mIsEnabled ? "ON" : "OFF");
            sb.append("), displayGroupName: ");
            sb.append(this.mDisplayGroupName);
            sb.append(", addr: ");
            sb.append(this.mAddress);
            if (this.mPosition == -1) {
                str = "";
            } else {
                str = ", position: " + this.mPosition;
            }
            sb.append(str);
            sb.append(", mThermalBrightnessThrottlingMapId: ");
            sb.append(this.mThermalBrightnessThrottlingMapId);
            sb.append(", mRefreshRateZoneId: ");
            sb.append(this.mRefreshRateZoneId);
            sb.append(", mLeadDisplayId: ");
            sb.append(this.mLeadDisplayId);
            sb.append(", mThermalRefreshRateThrottlingMapId: ");
            sb.append(this.mThermalRefreshRateThrottlingMapId);
            sb.append("}");
            return sb.toString();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Display)) {
                return false;
            }
            Display display = (Display) obj;
            return display.mIsEnabled == this.mIsEnabled && display.mPosition == this.mPosition && display.mLogicalDisplayId == this.mLogicalDisplayId && this.mDisplayGroupName.equals(display.mDisplayGroupName) && this.mAddress.equals(display.mAddress) && Objects.equals(this.mThermalBrightnessThrottlingMapId, display.mThermalBrightnessThrottlingMapId) && Objects.equals(display.mRefreshRateZoneId, this.mRefreshRateZoneId) && this.mLeadDisplayId == display.mLeadDisplayId && Objects.equals(this.mThermalRefreshRateThrottlingMapId, display.mThermalRefreshRateThrottlingMapId);
        }

        public int hashCode() {
            return ((((((((((((((((Boolean.hashCode(this.mIsEnabled) + 31) * 31) + this.mPosition) * 31) + this.mLogicalDisplayId) * 31) + this.mDisplayGroupName.hashCode()) * 31) + this.mAddress.hashCode()) * 31) + this.mThermalBrightnessThrottlingMapId.hashCode()) * 31) + Objects.hashCode(this.mRefreshRateZoneId)) * 31) + this.mLeadDisplayId) * 31) + Objects.hashCode(this.mThermalRefreshRateThrottlingMapId);
        }

        public DisplayAddress getAddress() {
            return this.mAddress;
        }

        public int getLogicalDisplayId() {
            return this.mLogicalDisplayId;
        }

        public boolean isEnabled() {
            return this.mIsEnabled;
        }

        public String getDisplayGroupName() {
            return this.mDisplayGroupName;
        }

        public String getRefreshRateZoneId() {
            return this.mRefreshRateZoneId;
        }

        public String getThermalBrightnessThrottlingMapId() {
            return this.mThermalBrightnessThrottlingMapId;
        }

        public int getPosition() {
            return this.mPosition;
        }

        public int getLeadDisplayId() {
            return this.mLeadDisplayId;
        }

        public String getRefreshRateThermalThrottlingMapId() {
            return this.mThermalRefreshRateThrottlingMapId;
        }
    }
}
