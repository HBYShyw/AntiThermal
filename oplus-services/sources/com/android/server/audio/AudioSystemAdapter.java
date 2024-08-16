package com.android.server.audio;

import android.media.AudioAttributes;
import android.media.AudioDeviceAttributes;
import android.media.AudioMixerAttributes;
import android.media.AudioSystem;
import android.media.IDevicesForAttributesCallback;
import android.media.ISoundDose;
import android.media.ISoundDoseCallback;
import android.media.audiopolicy.AudioMix;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import com.android.internal.annotations.GuardedBy;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AudioSystemAdapter implements AudioSystem.RoutingUpdateCallback, AudioSystem.VolumeRangeInitRequestCallback {
    private static final boolean DEBUG_CACHE = false;
    private static final boolean ENABLE_GETDEVICES_STATS = false;
    private static final int METHOD_GETDEVICESFORATTRIBUTES = 0;
    private static final int NB_MEASUREMENTS = 1;
    private static final String TAG = "AudioSystemAdapter";
    private static final boolean USE_CACHE_FOR_GETDEVICES = true;

    @GuardedBy({"sRoutingListenerLock"})
    private static OnRoutingUpdatedListener sRoutingListener;
    private static AudioSystemAdapter sSingletonDefaultAdapter;

    @GuardedBy({"sVolRangeInitReqListenerLock"})
    private static OnVolRangeInitRequestListener sVolRangeInitReqListener;

    @GuardedBy({"sDeviceCacheLock"})
    private ConcurrentHashMap<Pair<AudioAttributes, Boolean>, ArrayList<AudioDeviceAttributes>> mDevicesForAttrCache;
    private int[] mMethodCacheHit;
    private int[] mMethodCallCounter;
    private long[] mMethodTimeNs;
    private static final Object sDeviceCacheLock = new Object();
    private static final Object sRoutingListenerLock = new Object();
    private static final Object sVolRangeInitReqListenerLock = new Object();
    private static final boolean mSupportFm = SystemProperties.getBoolean("ro.oplus.audio.support.fm", false);
    private String[] mMethodNames = {"getDevicesForAttributes"};
    private ConcurrentHashMap<Pair<AudioAttributes, Boolean>, ArrayList<AudioDeviceAttributes>> mLastDevicesForAttr = new ConcurrentHashMap<>();

    @GuardedBy({"sDeviceCacheLock"})
    private long mDevicesForAttributesCacheClearTimeMs = System.currentTimeMillis();
    private final ArrayMap<IBinder, List<Pair<AudioAttributes, Boolean>>> mRegisteredAttributesMap = new ArrayMap<>();
    private final RemoteCallbackList<IDevicesForAttributesCallback> mDevicesForAttributesCallbacks = new RemoteCallbackList<>();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    interface OnRoutingUpdatedListener {
        void onRoutingUpdatedFromNative();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    interface OnVolRangeInitRequestListener {
        void onVolumeRangeInitRequestFromNative();
    }

    public void onRoutingUpdated() {
        OnRoutingUpdatedListener onRoutingUpdatedListener;
        invalidateRoutingCache();
        synchronized (sRoutingListenerLock) {
            onRoutingUpdatedListener = sRoutingListener;
        }
        if (onRoutingUpdatedListener != null) {
            onRoutingUpdatedListener.onRoutingUpdatedFromNative();
        }
        synchronized (this.mRegisteredAttributesMap) {
            int beginBroadcast = this.mDevicesForAttributesCallbacks.beginBroadcast();
            for (int i = 0; i < beginBroadcast; i++) {
                IDevicesForAttributesCallback broadcastItem = this.mDevicesForAttributesCallbacks.getBroadcastItem(i);
                List<Pair<AudioAttributes, Boolean>> list = this.mRegisteredAttributesMap.get(broadcastItem.asBinder());
                if (list == null) {
                    throw new IllegalStateException("Attribute list must not be null");
                }
                for (Pair<AudioAttributes, Boolean> pair : list) {
                    ArrayList<AudioDeviceAttributes> devicesForAttributes = getDevicesForAttributes((AudioAttributes) pair.first, ((Boolean) pair.second).booleanValue());
                    if (!this.mLastDevicesForAttr.containsKey(pair) || !sameDeviceList(devicesForAttributes, this.mLastDevicesForAttr.get(pair))) {
                        try {
                            broadcastItem.onDevicesForAttributesChanged((AudioAttributes) pair.first, ((Boolean) pair.second).booleanValue(), devicesForAttributes);
                        } catch (RemoteException unused) {
                        }
                    }
                }
            }
            this.mDevicesForAttributesCallbacks.finishBroadcast();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setRoutingListener(OnRoutingUpdatedListener onRoutingUpdatedListener) {
        synchronized (sRoutingListenerLock) {
            sRoutingListener = onRoutingUpdatedListener;
        }
    }

    public void clearRoutingCache() {
        invalidateRoutingCache();
    }

    public void addOnDevicesForAttributesChangedListener(AudioAttributes audioAttributes, boolean z, IDevicesForAttributesCallback iDevicesForAttributesCallback) {
        Pair<AudioAttributes, Boolean> pair = new Pair<>(audioAttributes, Boolean.valueOf(z));
        synchronized (this.mRegisteredAttributesMap) {
            List<Pair<AudioAttributes, Boolean>> list = this.mRegisteredAttributesMap.get(iDevicesForAttributesCallback.asBinder());
            if (list == null) {
                list = new ArrayList<>();
                this.mRegisteredAttributesMap.put(iDevicesForAttributesCallback.asBinder(), list);
                this.mDevicesForAttributesCallbacks.register(iDevicesForAttributesCallback);
            }
            if (!list.contains(pair)) {
                list.add(pair);
            }
        }
        getDevicesForAttributes(audioAttributes, z);
    }

    public void removeOnDevicesForAttributesChangedListener(IDevicesForAttributesCallback iDevicesForAttributesCallback) {
        synchronized (this.mRegisteredAttributesMap) {
            if (!this.mRegisteredAttributesMap.containsKey(iDevicesForAttributesCallback.asBinder())) {
                Log.w(TAG, "listener to be removed is not found.");
            } else {
                this.mRegisteredAttributesMap.remove(iDevicesForAttributesCallback.asBinder());
                this.mDevicesForAttributesCallbacks.unregister(iDevicesForAttributesCallback);
            }
        }
    }

    private boolean sameDeviceList(List<AudioDeviceAttributes> list, List<AudioDeviceAttributes> list2) {
        Iterator<AudioDeviceAttributes> it = list.iterator();
        while (it.hasNext()) {
            if (!list2.contains(it.next())) {
                return false;
            }
        }
        Iterator<AudioDeviceAttributes> it2 = list2.iterator();
        while (it2.hasNext()) {
            if (!list.contains(it2.next())) {
                return false;
            }
        }
        return true;
    }

    public void onVolumeRangeInitializationRequested() {
        OnVolRangeInitRequestListener onVolRangeInitRequestListener;
        synchronized (sVolRangeInitReqListenerLock) {
            onVolRangeInitRequestListener = sVolRangeInitReqListener;
        }
        if (onVolRangeInitRequestListener != null) {
            onVolRangeInitRequestListener.onVolumeRangeInitRequestFromNative();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setVolRangeInitReqListener(OnVolRangeInitRequestListener onVolRangeInitRequestListener) {
        synchronized (sVolRangeInitReqListenerLock) {
            sVolRangeInitReqListener = onVolRangeInitRequestListener;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final synchronized AudioSystemAdapter getDefaultAdapter() {
        AudioSystemAdapter audioSystemAdapter;
        synchronized (AudioSystemAdapter.class) {
            if (sSingletonDefaultAdapter == null) {
                AudioSystemAdapter audioSystemAdapter2 = new AudioSystemAdapter();
                sSingletonDefaultAdapter = audioSystemAdapter2;
                AudioSystem.setRoutingCallback(audioSystemAdapter2);
                AudioSystem.setVolumeRangeInitRequestCallback(sSingletonDefaultAdapter);
                synchronized (sDeviceCacheLock) {
                    sSingletonDefaultAdapter.mDevicesForAttrCache = new ConcurrentHashMap<>(AudioSystem.getNumStreamTypes());
                    sSingletonDefaultAdapter.mMethodCacheHit = new int[1];
                }
            }
            audioSystemAdapter = sSingletonDefaultAdapter;
        }
        return audioSystemAdapter;
    }

    private void invalidateRoutingCache() {
        synchronized (sDeviceCacheLock) {
            if (this.mDevicesForAttrCache != null) {
                this.mDevicesForAttributesCacheClearTimeMs = System.currentTimeMillis();
                this.mLastDevicesForAttr.putAll(this.mDevicesForAttrCache);
                this.mDevicesForAttrCache.clear();
            }
        }
    }

    public ArrayList<AudioDeviceAttributes> getDevicesForAttributes(AudioAttributes audioAttributes, boolean z) {
        return getDevicesForAttributesImpl(audioAttributes, z);
    }

    private ArrayList<AudioDeviceAttributes> getDevicesForAttributesImpl(AudioAttributes audioAttributes, boolean z) {
        if (!mSupportFm) {
            Pair<AudioAttributes, Boolean> pair = new Pair<>(audioAttributes, Boolean.valueOf(z));
            synchronized (sDeviceCacheLock) {
                ArrayList<AudioDeviceAttributes> arrayList = this.mDevicesForAttrCache.get(pair);
                if (arrayList == null) {
                    ArrayList<AudioDeviceAttributes> devicesForAttributes = AudioSystem.getDevicesForAttributes(audioAttributes, z);
                    this.mDevicesForAttrCache.put(pair, devicesForAttributes);
                    return devicesForAttributes;
                }
                int[] iArr = this.mMethodCacheHit;
                iArr[0] = iArr[0] + 1;
                return arrayList;
            }
        }
        return AudioSystem.getDevicesForAttributes(audioAttributes, z);
    }

    private static String attrDeviceToDebugString(AudioAttributes audioAttributes, List<AudioDeviceAttributes> list) {
        return " attrUsage=" + audioAttributes.getSystemUsage() + " " + AudioSystem.deviceSetToString(AudioSystem.generateAudioDeviceTypesSet(list));
    }

    public int setDeviceConnectionState(AudioDeviceAttributes audioDeviceAttributes, int i, int i2) {
        invalidateRoutingCache();
        return AudioSystem.setDeviceConnectionState(audioDeviceAttributes, i, i2);
    }

    public int getDeviceConnectionState(int i, String str) {
        return AudioSystem.getDeviceConnectionState(i, str);
    }

    public int handleDeviceConfigChange(int i, String str, String str2, int i2) {
        invalidateRoutingCache();
        return AudioSystem.handleDeviceConfigChange(i, str, str2, i2);
    }

    public int setDevicesRoleForStrategy(int i, int i2, List<AudioDeviceAttributes> list) {
        invalidateRoutingCache();
        return AudioSystem.setDevicesRoleForStrategy(i, i2, list);
    }

    public int removeDevicesRoleForStrategy(int i, int i2, List<AudioDeviceAttributes> list) {
        invalidateRoutingCache();
        return AudioSystem.removeDevicesRoleForStrategy(i, i2, list);
    }

    public int clearDevicesRoleForStrategy(int i, int i2) {
        invalidateRoutingCache();
        return AudioSystem.clearDevicesRoleForStrategy(i, i2);
    }

    public int setDevicesRoleForCapturePreset(int i, int i2, List<AudioDeviceAttributes> list) {
        invalidateRoutingCache();
        return AudioSystem.setDevicesRoleForCapturePreset(i, i2, list);
    }

    public int removeDevicesRoleForCapturePreset(int i, int i2, List<AudioDeviceAttributes> list) {
        invalidateRoutingCache();
        return AudioSystem.removeDevicesRoleForCapturePreset(i, i2, list);
    }

    public int addDevicesRoleForCapturePreset(int i, int i2, List<AudioDeviceAttributes> list) {
        invalidateRoutingCache();
        return AudioSystem.addDevicesRoleForCapturePreset(i, i2, list);
    }

    public int clearDevicesRoleForCapturePreset(int i, int i2) {
        invalidateRoutingCache();
        return AudioSystem.clearDevicesRoleForCapturePreset(i, i2);
    }

    public int setParameters(String str) {
        invalidateRoutingCache();
        return AudioSystem.setParameters(str);
    }

    public boolean isMicrophoneMuted() {
        return AudioSystem.isMicrophoneMuted();
    }

    public int muteMicrophone(boolean z) {
        return AudioSystem.muteMicrophone(z);
    }

    public int setCurrentImeUid(int i) {
        return AudioSystem.setCurrentImeUid(i);
    }

    public boolean isStreamActive(int i, int i2) {
        return AudioSystem.isStreamActive(i, i2);
    }

    public boolean isStreamActiveRemotely(int i, int i2) {
        return AudioSystem.isStreamActiveRemotely(i, i2);
    }

    public int setStreamVolumeIndexAS(int i, int i2, int i3) {
        return AudioSystem.setStreamVolumeIndexAS(i, i2, i3);
    }

    public int setPhoneState(int i, int i2) {
        invalidateRoutingCache();
        return AudioSystem.setPhoneState(i, i2);
    }

    public int setAllowedCapturePolicy(int i, int i2) {
        return AudioSystem.setAllowedCapturePolicy(i, i2);
    }

    public int setForceUse(int i, int i2) {
        invalidateRoutingCache();
        return AudioSystem.setForceUse(i, i2);
    }

    public int getForceUse(int i) {
        return AudioSystem.getForceUse(i);
    }

    public int registerPolicyMixes(ArrayList<AudioMix> arrayList, boolean z) {
        invalidateRoutingCache();
        return AudioSystem.registerPolicyMixes(arrayList, z);
    }

    public int setUidDeviceAffinities(int i, int[] iArr, String[] strArr) {
        invalidateRoutingCache();
        return AudioSystem.setUidDeviceAffinities(i, iArr, strArr);
    }

    public int removeUidDeviceAffinities(int i) {
        invalidateRoutingCache();
        return AudioSystem.removeUidDeviceAffinities(i);
    }

    public int setUserIdDeviceAffinities(int i, int[] iArr, String[] strArr) {
        invalidateRoutingCache();
        return AudioSystem.setUserIdDeviceAffinities(i, iArr, strArr);
    }

    public int removeUserIdDeviceAffinities(int i) {
        invalidateRoutingCache();
        return AudioSystem.removeUserIdDeviceAffinities(i);
    }

    public ISoundDose getSoundDoseInterface(ISoundDoseCallback iSoundDoseCallback) {
        return AudioSystem.getSoundDoseInterface(iSoundDoseCallback);
    }

    public int setPreferredMixerAttributes(AudioAttributes audioAttributes, int i, int i2, AudioMixerAttributes audioMixerAttributes) {
        return AudioSystem.setPreferredMixerAttributes(audioAttributes, i, i2, audioMixerAttributes);
    }

    public int clearPreferredMixerAttributes(AudioAttributes audioAttributes, int i, int i2) {
        return AudioSystem.clearPreferredMixerAttributes(audioAttributes, i, i2);
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("\nAudioSystemAdapter:");
        DateTimeFormatter withZone = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss:SSS").withLocale(Locale.US).withZone(ZoneId.systemDefault());
        synchronized (sDeviceCacheLock) {
            printWriter.println(" last cache clear time: " + withZone.format(Instant.ofEpochMilli(this.mDevicesForAttributesCacheClearTimeMs)));
            printWriter.println(" mDevicesForAttrCache:");
            ConcurrentHashMap<Pair<AudioAttributes, Boolean>, ArrayList<AudioDeviceAttributes>> concurrentHashMap = this.mDevicesForAttrCache;
            if (concurrentHashMap != null) {
                for (Map.Entry<Pair<AudioAttributes, Boolean>, ArrayList<AudioDeviceAttributes>> entry : concurrentHashMap.entrySet()) {
                    AudioAttributes audioAttributes = (AudioAttributes) entry.getKey().first;
                    try {
                        int volumeControlStream = audioAttributes.getVolumeControlStream();
                        printWriter.println("\t" + audioAttributes + " forVolume: " + entry.getKey().second + " stream: " + AudioSystem.STREAM_NAMES[volumeControlStream] + "(" + volumeControlStream + ")");
                        Iterator<AudioDeviceAttributes> it = entry.getValue().iterator();
                        while (it.hasNext()) {
                            printWriter.println("\t\t" + it.next());
                        }
                    } catch (IllegalArgumentException e) {
                        printWriter.println("\t dump failed for attributes: " + audioAttributes);
                        Log.e(TAG, "dump failed", e);
                    }
                }
            }
        }
    }
}
