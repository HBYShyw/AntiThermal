package android.media;

import android.os.SystemProperties;

/* loaded from: classes.dex */
public class AudioSystemExtImpl implements IAudioSystemExt {
    public static AudioSystemExtImpl sInstance;

    public void getDefaultVolume(int streamType) {
        int defaultCallVolume;
        if (streamType == 3) {
            int defaultMusicVolume = SystemProperties.getInt("ro.config.media_vol_default", -1);
            if (defaultMusicVolume != -1) {
                AudioSystem.DEFAULT_STREAM_VOLUME[3] = defaultMusicVolume;
                return;
            }
            return;
        }
        if (streamType == 0 && (defaultCallVolume = SystemProperties.getInt("ro.config.vc_call_vol_default", -1)) != -1) {
            AudioSystem.DEFAULT_STREAM_VOLUME[0] = defaultCallVolume;
        }
    }

    public static AudioSystemExtImpl getInstance(Object obj) {
        AudioSystemExtImpl audioSystemExtImpl;
        synchronized (AudioSystemExtImpl.class) {
            if (sInstance == null) {
                sInstance = new AudioSystemExtImpl();
            }
            audioSystemExtImpl = sInstance;
        }
        return audioSystemExtImpl;
    }
}
