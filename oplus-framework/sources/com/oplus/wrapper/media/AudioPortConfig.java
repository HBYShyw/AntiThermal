package com.oplus.wrapper.media;

/* loaded from: classes.dex */
public class AudioPortConfig {
    private android.media.AudioPortConfig mAudioPortConfig;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioPortConfig(android.media.AudioPortConfig audioPortConfig) {
        this.mAudioPortConfig = audioPortConfig;
    }

    public AudioPort port() {
        return new AudioPort(this.mAudioPortConfig.port());
    }

    public android.media.AudioPortConfig get() {
        return this.mAudioPortConfig;
    }
}
