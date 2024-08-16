package com.oplus.wrapper.media;

/* loaded from: classes.dex */
public class AudioPatch {
    private android.media.AudioPatch mAudioPatch;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioPatch(android.media.AudioPatch audioPatch) {
        this.mAudioPatch = audioPatch;
    }

    public AudioPortConfig[] sources() {
        android.media.AudioPortConfig[] audioPortConfig = this.mAudioPatch.sources();
        AudioPortConfig[] sources = new AudioPortConfig[audioPortConfig.length];
        for (int i = 0; i < audioPortConfig.length; i++) {
            sources[i] = new AudioPortConfig(audioPortConfig[i]);
        }
        return sources;
    }

    public AudioPortConfig[] sinks() {
        android.media.AudioPortConfig[] audioPortConfig = this.mAudioPatch.sinks();
        AudioPortConfig[] sinks = new AudioPortConfig[audioPortConfig.length];
        for (int i = 0; i < audioPortConfig.length; i++) {
            sinks[i] = new AudioPortConfig(audioPortConfig[i]);
        }
        return sinks;
    }

    public android.media.AudioPatch get() {
        return this.mAudioPatch;
    }
}
