package com.oplus.wrapper.media.audiopolicy;

import android.content.Context;
import android.media.audiopolicy.AudioPolicy;
import android.os.Looper;

/* loaded from: classes.dex */
public class AudioPolicy {
    private final android.media.audiopolicy.AudioPolicy mTarget;

    private AudioPolicy(android.media.audiopolicy.AudioPolicy audioPolicy) {
        this.mTarget = audioPolicy;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private final AudioPolicy.Builder mTarget;

        public Builder(Context context) {
            this.mTarget = new AudioPolicy.Builder(context);
        }

        public Builder setLooper(Looper looper) throws IllegalArgumentException {
            this.mTarget.setLooper(looper);
            return this;
        }

        public Builder addMix(AudioMix audioMix) throws IllegalArgumentException {
            this.mTarget.addMix(audioMix.getAudioMix());
            return this;
        }

        public AudioPolicy build() {
            return new AudioPolicy(this.mTarget.build());
        }
    }

    public android.media.audiopolicy.AudioPolicy getAudioPolicy() {
        return this.mTarget;
    }
}
