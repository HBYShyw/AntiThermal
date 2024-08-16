package com.oplus.wrapper.media;

/* loaded from: classes.dex */
public class AudioRecordingConfiguration {
    private android.media.AudioRecordingConfiguration mAudioRecordingConfiguration;

    public AudioRecordingConfiguration(android.media.AudioRecordingConfiguration audioRecordingConfiguration) {
        this.mAudioRecordingConfiguration = audioRecordingConfiguration;
    }

    public int getClientUid() {
        return this.mAudioRecordingConfiguration.getClientUid();
    }
}
