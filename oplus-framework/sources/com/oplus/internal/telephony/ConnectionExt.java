package com.oplus.internal.telephony;

/* loaded from: classes.dex */
public class ConnectionExt {
    public static final int STATE_AUDIO_RINGTONE = 1;
    public static final int STATE_VIDEO_RINGTONE = 2;
    public static final int SUPPORTS_AUDIO_RINGTONE = 4096;
    public static final int SUPPORTS_VIDEO_RINGTONE = 8192;

    public static final int getSupportsAudioRingtone() {
        return 4096;
    }

    public static final int getSupportsVideoRingtone() {
        return 8192;
    }
}
