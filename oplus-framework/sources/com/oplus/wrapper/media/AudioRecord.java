package com.oplus.wrapper.media;

import android.media.AudioFormat;

/* loaded from: classes.dex */
public class AudioRecord {
    private final android.media.AudioRecord mAudioRecord;

    public AudioRecord(android.media.AudioAttributes attributes, AudioFormat format, int bufferSizeInBytes, int sessionId) throws IllegalArgumentException {
        this.mAudioRecord = new android.media.AudioRecord(attributes, format, bufferSizeInBytes, sessionId);
    }

    public android.media.AudioRecord getAudioRecord() {
        return this.mAudioRecord;
    }
}
