package com.android.server.companion.virtual.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioRecordingConfiguration;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class AudioRecordingDetector extends AudioManager.AudioRecordingCallback {
    private final AudioManager mAudioManager;
    private AudioRecordingCallback mAudioRecordingCallback;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    interface AudioRecordingCallback {
        void onRecordingConfigChanged(List<AudioRecordingConfiguration> list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioRecordingDetector(Context context) {
        this.mAudioManager = (AudioManager) context.getSystemService(AudioManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void register(AudioRecordingCallback audioRecordingCallback) {
        this.mAudioRecordingCallback = audioRecordingCallback;
        this.mAudioManager.registerAudioRecordingCallback(this, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregister() {
        if (this.mAudioRecordingCallback != null) {
            this.mAudioRecordingCallback = null;
            this.mAudioManager.unregisterAudioRecordingCallback(this);
        }
    }

    @Override // android.media.AudioManager.AudioRecordingCallback
    public void onRecordingConfigChanged(List<AudioRecordingConfiguration> list) {
        super.onRecordingConfigChanged(list);
        AudioRecordingCallback audioRecordingCallback = this.mAudioRecordingCallback;
        if (audioRecordingCallback != null) {
            audioRecordingCallback.onRecordingConfigChanged(list);
        }
    }
}
