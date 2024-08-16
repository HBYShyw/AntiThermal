package com.oplus.wrapper.media;

/* loaded from: classes.dex */
public class AudioPort {
    private android.media.AudioPort mAudioPort;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioPort(android.media.AudioPort audioPort) {
        this.mAudioPort = audioPort;
    }

    public int id() {
        return this.mAudioPort.id();
    }

    public android.media.AudioPort get() {
        return this.mAudioPort;
    }
}
