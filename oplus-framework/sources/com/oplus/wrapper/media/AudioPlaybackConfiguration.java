package com.oplus.wrapper.media;

/* loaded from: classes.dex */
public class AudioPlaybackConfiguration {
    private final android.media.AudioPlaybackConfiguration mAudioPlaybackConfiguration;

    public AudioPlaybackConfiguration(android.media.AudioPlaybackConfiguration audioPlaybackConfiguration) {
        this.mAudioPlaybackConfiguration = audioPlaybackConfiguration;
    }

    public boolean isActive() {
        return this.mAudioPlaybackConfiguration.isActive();
    }
}
