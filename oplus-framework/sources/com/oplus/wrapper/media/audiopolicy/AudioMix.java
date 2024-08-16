package com.oplus.wrapper.media.audiopolicy;

import android.media.AudioDeviceInfo;
import android.media.AudioFormat;
import android.media.audiopolicy.AudioMix;

/* loaded from: classes.dex */
public class AudioMix {
    public static final int ROUTE_FLAG_RENDER = getRouteFlagRender();
    private final android.media.audiopolicy.AudioMix mTarget;

    private AudioMix(android.media.audiopolicy.AudioMix audioMix) {
        this.mTarget = audioMix;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private final AudioMix.Builder mTarget;

        public Builder(AudioMixingRule rule) {
            this.mTarget = new AudioMix.Builder(rule.getAudioMixingRule());
        }

        public Builder setFormat(AudioFormat format) {
            this.mTarget.setFormat(format);
            return this;
        }

        public Builder setDevice(AudioDeviceInfo device) throws IllegalArgumentException {
            this.mTarget.setDevice(device);
            return this;
        }

        public Builder setRouteFlags(int routeFlags) {
            this.mTarget.setRouteFlags(routeFlags);
            return this;
        }

        public AudioMix build() throws IllegalArgumentException {
            return new AudioMix(this.mTarget.build());
        }
    }

    private static int getRouteFlagRender() {
        return 1;
    }

    public android.media.audiopolicy.AudioMix getAudioMix() {
        return this.mTarget;
    }
}
