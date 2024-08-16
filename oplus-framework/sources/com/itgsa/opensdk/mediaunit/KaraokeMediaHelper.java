package com.itgsa.opensdk.mediaunit;

import android.content.Context;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;

/* loaded from: classes.dex */
public class KaraokeMediaHelper {
    private static final int SDK_VERSION = 10000;
    private static final String TAG = KaraokeMediaHelper.class.getSimpleName();
    private static final int UNSUPPORT = -1;
    private MediaInterface mediaInterface;

    public KaraokeMediaHelper(Context context) {
        this.mediaInterface = new MediaUnitClientImpl(context);
    }

    public int getVersion() {
        return 10000;
    }

    public boolean isDeviceSupportKaraoke() {
        boolean isKaraokeSupport = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_KARAOKE_V2_SUPPORT);
        return isKaraokeSupport;
    }

    public String getKaraokeSupportParameters() {
        return "{ \"audioTrackParam\":[\n                      {\"streamType\":\"3\",\n                       \"sampleRate\":\"48000\",\n                       \"format\":\"2\",\n                       \"flag\":\"8\"\n                      }],\n         \"audioRecordParam\":[\n                      {\"streamType\":\"default\",\n                       \"sampleRate\":\"48000\",\n                       \"format\":\"2\",\n                       \"flag\":\"8\",\n                       \"source\":\"1\"}],\n         \"isSupportExtSpeakerParam\":false,\n         \"isAppSupportKaraoke\":false,\n         \"setExtSpeakerParam\":false,\n         \"getExtSpeakerParam\":false,\n         \"getMicVolParam\":false,\n         \"getPlayFeedbackParam\":false,\n         \"getListenRecordSame\":false,\n         \"getExtEqualizerType\":false,\n         \"getExtMixerSoundType\":false     }";
    }

    public void openKTVDevice() {
        MediaInterface mediaInterface = this.mediaInterface;
        if (mediaInterface == null) {
            return;
        }
        mediaInterface.openKTVDevice();
    }

    public void closeKTVDevice() {
        MediaInterface mediaInterface = this.mediaInterface;
        if (mediaInterface == null) {
            return;
        }
        mediaInterface.closeKTVDevice();
    }

    public boolean isAppSupportKaraoke(String appName) {
        return true;
    }

    public void setListenRecordSame(int param) {
        MediaInterface mediaInterface = this.mediaInterface;
        if (mediaInterface == null) {
            return;
        }
        mediaInterface.setListenRecordSame(param);
    }

    public void setMixerSoundType(int param) {
        MediaInterface mediaInterface = this.mediaInterface;
        if (mediaInterface == null) {
            return;
        }
        mediaInterface.setMixerSoundType(param);
    }

    public void setPlayFeedbackParam(int param) {
        MediaInterface mediaInterface = this.mediaInterface;
        if (mediaInterface == null) {
            return;
        }
        mediaInterface.setPlayFeedbackParam(param);
    }

    public void setMicVolParam(int param) {
        MediaInterface mediaInterface = this.mediaInterface;
        if (mediaInterface == null) {
            return;
        }
        mediaInterface.setMicVolParam(param);
    }

    public void setExtSpeakerParam(int param) {
        throw new UnsupportedOperationException("method not support!");
    }

    public int getExtSpeakerParam() {
        throw new UnsupportedOperationException("method not support!");
    }

    public int getPlayFeedbackParam() {
        throw new UnsupportedOperationException("method not support!");
    }

    public int getMicVolParam() {
        throw new UnsupportedOperationException("method not support!");
    }

    public int getListenRecordSame() {
        throw new UnsupportedOperationException("method not support!");
    }

    public void setToneMode(int toneValue) {
        MediaInterface mediaInterface = this.mediaInterface;
        if (mediaInterface == null) {
            return;
        }
        mediaInterface.setToneMode(toneValue);
    }

    public void setEqualizerType(int equalizerType) {
        MediaInterface mediaInterface = this.mediaInterface;
        if (mediaInterface == null) {
            return;
        }
        mediaInterface.setEqualizerType(equalizerType);
    }
}
