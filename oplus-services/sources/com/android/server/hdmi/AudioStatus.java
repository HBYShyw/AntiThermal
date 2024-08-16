package com.android.server.hdmi;

import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AudioStatus {
    public static final int MAX_VOLUME = 100;
    public static final int MIN_VOLUME = 0;
    boolean mMute;
    int mVolume;

    public AudioStatus(int i, boolean z) {
        this.mVolume = Math.max(Math.min(i, 100), 0);
        this.mMute = z;
    }

    public int getVolume() {
        return this.mVolume;
    }

    public boolean getMute() {
        return this.mMute;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AudioStatus)) {
            return false;
        }
        AudioStatus audioStatus = (AudioStatus) obj;
        return this.mVolume == audioStatus.mVolume && this.mMute == audioStatus.mMute;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mVolume), Boolean.valueOf(this.mMute));
    }

    public String toString() {
        return "AudioStatus mVolume:" + this.mVolume + " mMute:" + this.mMute;
    }
}
