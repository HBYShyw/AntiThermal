package com.oplus.wrapper.media;

import android.media.AudioManager;
import com.oplus.wrapper.media.audiopolicy.AudioPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class AudioManager {
    private final android.media.AudioManager mAudioManager;
    private final Map<OnAudioPortUpdateListener, AudioPortUpdateListenerImpl> mRegisterAudioPortCache = new ConcurrentHashMap();
    public static final int STREAM_SYSTEM_ENFORCED = getStreamSystemEnforced();
    public static final int FLAG_FROM_KEY = getFlagFromKey();
    public static final String VOLUME_CHANGED_ACTION = getVolumeChangedAction();
    public static final int SUCCESS = getSuccess();

    /* loaded from: classes.dex */
    public interface OnAudioPortUpdateListener {
        void onAudioPatchListUpdate(AudioPatch[] audioPatchArr);

        void onAudioPortListUpdate(AudioPort[] audioPortArr);

        void onServiceDied();
    }

    public AudioManager(android.media.AudioManager audioManager) {
        this.mAudioManager = audioManager;
    }

    private static int getStreamSystemEnforced() {
        return 7;
    }

    private static int getFlagFromKey() {
        return 4096;
    }

    private static String getVolumeChangedAction() {
        return "android.media.VOLUME_CHANGED_ACTION";
    }

    private static int getSuccess() {
        return 0;
    }

    public int getRingerModeInternal() {
        return this.mAudioManager.getRingerModeInternal();
    }

    public int getDevicesForStream(int streamType) {
        return this.mAudioManager.getDevicesForStream(streamType);
    }

    public static int releaseAudioPatch(AudioPatch patch) {
        return android.media.AudioManager.releaseAudioPatch(patch.get());
    }

    public static int listAudioPorts(ArrayList<AudioPort> ports) {
        ArrayList<android.media.AudioPort> audioPorts = new ArrayList<>();
        int resultCode = android.media.AudioManager.listAudioPorts(audioPorts);
        if (ports != null) {
            ports.clear();
            Iterator<android.media.AudioPort> it = audioPorts.iterator();
            while (it.hasNext()) {
                android.media.AudioPort audioPort = it.next();
                ports.add(new AudioPort(audioPort));
            }
        }
        return resultCode;
    }

    public static int listAudioPatches(ArrayList<AudioPatch> patches) {
        ArrayList<android.media.AudioPatch> audioPatchs = new ArrayList<>();
        int resultCode = android.media.AudioManager.listAudioPatches(audioPatchs);
        if (patches != null) {
            patches.clear();
            Iterator<android.media.AudioPatch> it = audioPatchs.iterator();
            while (it.hasNext()) {
                android.media.AudioPatch audioPatch = it.next();
                patches.add(new AudioPatch(audioPatch));
            }
        }
        return resultCode;
    }

    public static int createAudioPatch(AudioPatch[] patch, AudioPortConfig[] sources, AudioPortConfig[] sinks) {
        android.media.AudioPatch[] audioPatchs = new android.media.AudioPatch[0];
        android.media.AudioPortConfig[] audioPortConfigSources = new android.media.AudioPortConfig[sources.length];
        android.media.AudioPortConfig[] audioPortConfigSinks = new android.media.AudioPortConfig[sinks.length];
        for (int i = 0; i < sources.length; i++) {
            audioPortConfigSources[i] = sources[i].get();
        }
        for (int i2 = 0; i2 < sinks.length; i2++) {
            audioPortConfigSinks[i2] = sinks[i2].get();
        }
        int resultCode = android.media.AudioManager.createAudioPatch(audioPatchs, audioPortConfigSources, audioPortConfigSinks);
        for (int i3 = 0; i3 < patch.length; i3++) {
            patch[i3] = new AudioPatch(audioPatchs[i3]);
        }
        return resultCode;
    }

    public void setWiredDeviceConnectionState(int device, int state, String address, String name) {
        this.mAudioManager.setWiredDeviceConnectionState(device, state, address, name);
    }

    public void setIPDeviceConnectionState(int device, int state, String address, String name, boolean suppressNoisyIntent) {
        this.mAudioManager.setIPDeviceConnectionState(device, state, address, name, suppressNoisyIntent);
    }

    public void registerAudioPortUpdateListener(OnAudioPortUpdateListener l) {
        if (this.mRegisterAudioPortCache.get(l) == null) {
            AudioPortUpdateListenerImpl audioPortUpdateListener = new AudioPortUpdateListenerImpl(l);
            this.mRegisterAudioPortCache.put(l, audioPortUpdateListener);
            this.mAudioManager.registerAudioPortUpdateListener(audioPortUpdateListener);
        }
    }

    public void unregisterAudioPortUpdateListener(OnAudioPortUpdateListener l) {
        AudioPortUpdateListenerImpl audioPortUpdateListener = this.mRegisterAudioPortCache.get(l);
        if (audioPortUpdateListener == null) {
            return;
        }
        this.mAudioManager.unregisterAudioPortUpdateListener(audioPortUpdateListener);
    }

    @Deprecated
    public List<android.media.AudioDeviceAttributes> getDevicesForAttributes(android.media.AudioAttributes attributes) {
        return this.mAudioManager.getDevicesForAttributes(attributes);
    }

    public List<AudioDeviceAttributes> getDevicesForAttributesNew(android.media.AudioAttributes attributes) {
        this.mAudioManager.getDevicesForAttributes(attributes);
        List<AudioDeviceAttributes> wrapperList = new ArrayList<>();
        for (android.media.AudioDeviceAttributes audioDeviceAttributes : this.mAudioManager.getDevicesForAttributes(attributes)) {
            wrapperList.add(new AudioDeviceAttributes(audioDeviceAttributes));
        }
        return wrapperList;
    }

    public int registerAudioPolicy(AudioPolicy policy) {
        return this.mAudioManager.registerAudioPolicy(policy.getAudioPolicy());
    }

    public void unregisterAudioPolicy(AudioPolicy policy) {
        this.mAudioManager.unregisterAudioPolicy(policy.getAudioPolicy());
    }

    /* loaded from: classes.dex */
    private static class AudioPortUpdateListenerImpl implements AudioManager.OnAudioPortUpdateListener {
        private final OnAudioPortUpdateListener mOnAudioPortUpdateListener;

        public AudioPortUpdateListenerImpl(OnAudioPortUpdateListener onAudioPortUpdateListener) {
            this.mOnAudioPortUpdateListener = onAudioPortUpdateListener;
        }

        public void onAudioPortListUpdate(android.media.AudioPort[] audioPorts) {
            AudioPort[] wrapperAudioPorts = new AudioPort[audioPorts.length];
            for (int i = 0; i < audioPorts.length; i++) {
                wrapperAudioPorts[i] = new AudioPort(audioPorts[i]);
            }
            this.mOnAudioPortUpdateListener.onAudioPortListUpdate(wrapperAudioPorts);
        }

        public void onAudioPatchListUpdate(android.media.AudioPatch[] audioPatches) {
            AudioPatch[] wrapperaudioPatchs = new AudioPatch[audioPatches.length];
            for (int i = 0; i < audioPatches.length; i++) {
                wrapperaudioPatchs[i] = new AudioPatch(audioPatches[i]);
            }
            this.mOnAudioPortUpdateListener.onAudioPatchListUpdate(wrapperaudioPatchs);
        }

        public void onServiceDied() {
            this.mOnAudioPortUpdateListener.onServiceDied();
        }
    }
}
