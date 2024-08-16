package com.oplus.wrapper.media;

/* loaded from: classes.dex */
public class AudioSystem {
    public static boolean isStreamActive(int stream, int inPastMs) {
        return android.media.AudioSystem.isStreamActive(stream, inPastMs);
    }

    public static int setParameters(String keyValuePairs) {
        return android.media.AudioSystem.setParameters(keyValuePairs);
    }

    public static int setForceUse(int usage, int config) {
        return android.media.AudioSystem.setForceUse(usage, config);
    }

    public static int getForceUse(int usage) {
        return android.media.AudioSystem.getForceUse(usage);
    }

    public static String getParameters(String keys) {
        return android.media.AudioSystem.getParameters(keys);
    }

    public static String getOutputDeviceName(int device) {
        return android.media.AudioSystem.getOutputDeviceName(device);
    }
}
