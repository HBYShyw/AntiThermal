package com.android.server.audio;

import android.media.AudioDeviceAttributes;
import android.util.Slog;
import java.util.UUID;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UuidUtils {
    private static final long LSB_PREFIX_BT = 4779445104546938880L;
    private static final long LSB_PREFIX_MASK = -281474976710656L;
    private static final long LSB_SUFFIX_MASK = 281474976710655L;
    public static final UUID STANDALONE_UUID = new UUID(0, 0);
    private static final String TAG = "AudioService.UuidUtils";

    UuidUtils() {
    }

    public static UUID uuidFromAudioDeviceAttributes(AudioDeviceAttributes audioDeviceAttributes) {
        int internalType = audioDeviceAttributes.getInternalType();
        if (internalType != 128 && internalType != 536870912) {
            return null;
        }
        String replace = audioDeviceAttributes.getAddress().replace(":", "");
        if (replace.length() != 12) {
            return null;
        }
        try {
            long longValue = Long.decode("0x" + replace).longValue() | LSB_PREFIX_BT;
            if (AudioService.DEBUG_DEVICES) {
                Slog.i(TAG, "uuidFromAudioDeviceAttributes lsb: " + Long.toHexString(longValue));
            }
            return new UUID(0L, longValue);
        } catch (NumberFormatException unused) {
            return null;
        }
    }
}
