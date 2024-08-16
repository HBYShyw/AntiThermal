package com.oplus.wrapper.media;

/* loaded from: classes.dex */
public class AudioDeviceAttributes {
    private final android.media.AudioDeviceAttributes mTarget;

    public AudioDeviceAttributes(android.media.AudioDeviceAttributes audioDeviceAttributes) {
        this.mTarget = audioDeviceAttributes;
    }

    public String getAddress() {
        return this.mTarget.getAddress();
    }

    public int getType() {
        return this.mTarget.getType();
    }
}
