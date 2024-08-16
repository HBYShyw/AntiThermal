package com.android.server.display;

import android.util.ArrayMap;
import android.util.Slog;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class HighBrightnessModeMetadataMapper {
    private static final String TAG = "HighBrightnessModeMetadataMapper";
    private final ArrayMap<String, HighBrightnessModeMetadata> mHighBrightnessModeMetadataMap = new ArrayMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public HighBrightnessModeMetadata getHighBrightnessModeMetadataLocked(LogicalDisplay logicalDisplay) {
        DisplayDevice primaryDisplayDeviceLocked = logicalDisplay.getPrimaryDisplayDeviceLocked();
        if (primaryDisplayDeviceLocked == null) {
            Slog.wtf(TAG, "Display Device is null in DisplayPowerController for display: " + logicalDisplay.getDisplayIdLocked());
            return null;
        }
        String uniqueId = primaryDisplayDeviceLocked.getUniqueId();
        if (this.mHighBrightnessModeMetadataMap.containsKey(uniqueId)) {
            return this.mHighBrightnessModeMetadataMap.get(uniqueId);
        }
        HighBrightnessModeMetadata highBrightnessModeMetadata = new HighBrightnessModeMetadata();
        this.mHighBrightnessModeMetadataMap.put(uniqueId, highBrightnessModeMetadata);
        return highBrightnessModeMetadata;
    }
}
