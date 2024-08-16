package com.itgsa.opensdk.mediaunit;

/* loaded from: classes.dex */
public interface MediaInterface {
    void closeKTVDevice();

    void openKTVDevice();

    void setEqualizerType(int i);

    void setListenRecordSame(int i);

    void setMicVolParam(int i);

    void setMixerSoundType(int i);

    void setPlayFeedbackParam(int i);

    void setToneMode(int i);
}
