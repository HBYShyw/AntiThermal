package com.oplus.wrapper.media;

import android.media.AudioAttributes;

/* loaded from: classes.dex */
public class AudioAttributes {
    private AudioAttributes() {
    }

    public static int toLegacyStreamType(android.media.AudioAttributes aa) {
        return android.media.AudioAttributes.toLegacyStreamType(aa);
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private final AudioAttributes.Builder mBuilder;

        public Builder(AudioAttributes.Builder builder) {
            this.mBuilder = builder;
        }

        @Deprecated
        public AudioAttributes.Builder setInternalCapturePreset(int preset) {
            this.mBuilder.setInternalCapturePreset(preset);
            return this.mBuilder;
        }

        public Builder setInternalCapturePresetNew(int preset) {
            this.mBuilder.setInternalCapturePreset(preset);
            return this;
        }

        public Builder setSystemUsage(int systemUsage) {
            this.mBuilder.setInternalCapturePreset(systemUsage);
            return this;
        }
    }

    public static boolean isSystemUsage(int usage) {
        return android.media.AudioAttributes.isSystemUsage(usage);
    }
}
