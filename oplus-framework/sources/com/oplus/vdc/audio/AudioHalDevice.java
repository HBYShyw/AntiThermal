package com.oplus.vdc.audio;

import android.media.AudioTimestamp;
import android.util.Log;

/* loaded from: classes.dex */
public class AudioHalDevice {
    private static final String AUDIO_JNI_NAME = "OplusVirtualAudio";
    private static final int ERROR_NOT_SUPPORTED = -100;
    private static final String TAG = "AudioHalDevice";
    private volatile boolean mHasLoaded;

    /* loaded from: classes.dex */
    public interface IReadableCallback {
        void onReadableChanged(int i);

        void onReadableChanged(boolean z);
    }

    private native int native_close();

    private native int native_closeStream(int i);

    private native String native_getChannelAddress(int i);

    private native int native_getStreamBufferSize(int i);

    private native int native_get_out_timestamp(int i, AudioTimestamp audioTimestamp, int i2);

    private native boolean native_isAllowMuteCallDownLink();

    private native boolean native_isSupportAudioPatch();

    private native int native_open();

    private native int native_openStream(int i, int i2, int i3, int i4);

    private native int native_read(int i, int i2, int i3, byte[] bArr);

    private native boolean native_registerReadableCallback(IReadableCallback iReadableCallback);

    private native int native_standby(int i);

    private native boolean native_unregisterReadableCallback(IReadableCallback iReadableCallback);

    private native int native_write(int i, int i2, byte[] bArr);

    /* loaded from: classes.dex */
    private static class AudioHalDeviceHolder {
        static AudioHalDevice sHolder = new AudioHalDevice();

        private AudioHalDeviceHolder() {
        }
    }

    private AudioHalDevice() {
        this.mHasLoaded = false;
    }

    private void loadLibrary() {
        try {
            System.loadLibrary(AUDIO_JNI_NAME);
            this.mHasLoaded = true;
            Log.d(TAG, "osdk load library done");
        } catch (Exception | UnsatisfiedLinkError e) {
            Log.e(TAG, "fail to load lib, " + e.getMessage());
        }
    }

    public static AudioHalDevice getInstance() {
        if (!AudioHalDeviceHolder.sHolder.mHasLoaded) {
            AudioHalDeviceHolder.sHolder.loadLibrary();
        }
        return AudioHalDeviceHolder.sHolder;
    }

    public boolean isValid() {
        return this.mHasLoaded;
    }

    public boolean registerReadableCallback(IReadableCallback callback) {
        if (this.mHasLoaded) {
            return native_registerReadableCallback(callback);
        }
        return true;
    }

    public boolean unregisterReadableCallback(IReadableCallback callback) {
        if (this.mHasLoaded) {
            return native_unregisterReadableCallback(callback);
        }
        return true;
    }

    public boolean isSupportAudioPatch() {
        if (this.mHasLoaded) {
            return native_isSupportAudioPatch();
        }
        return false;
    }

    public boolean isAllowMuteCallDownLink() {
        if (this.mHasLoaded) {
            return native_isAllowMuteCallDownLink();
        }
        return true;
    }

    public String getChannelAddress(int channel) {
        if (this.mHasLoaded) {
            return native_getChannelAddress(channel);
        }
        return "";
    }

    public synchronized int open() {
        if (!this.mHasLoaded) {
            return -100;
        }
        return native_open();
    }

    public synchronized int close() {
        if (!this.mHasLoaded) {
            return 0;
        }
        return native_close();
    }

    public int openStream(int channel, int sampleRateInHz, int channelConfig, int audioFormat) {
        if (this.mHasLoaded) {
            return native_openStream(channel, sampleRateInHz, channelConfig, audioFormat);
        }
        return -100;
    }

    public int closeStream(int channel) {
        if (this.mHasLoaded) {
            return native_closeStream(channel);
        }
        return 0;
    }

    public int getStreamBufferSize(int channel) {
        if (this.mHasLoaded) {
            return native_getStreamBufferSize(channel);
        }
        return 0;
    }

    public int write(int channel, int bufferSizeInBytes, byte[] buffer) {
        if (this.mHasLoaded) {
            return native_write(channel, bufferSizeInBytes, buffer);
        }
        return 0;
    }

    public int read(int channel, int offsetInBytes, int sizeInBytes, byte[] buffer) {
        if (this.mHasLoaded) {
            return native_read(channel, offsetInBytes, sizeInBytes, buffer);
        }
        return -100;
    }

    public int standby(int channel) {
        if (this.mHasLoaded) {
            return native_standby(channel);
        }
        return 0;
    }

    public int getOutTimestamp(int channel, AudioTimestamp outTimestamp, int timebase) {
        if (this.mHasLoaded) {
            return native_get_out_timestamp(channel, outTimestamp, timebase);
        }
        return -100;
    }
}
