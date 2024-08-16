package android.media;

import android.content.Context;
import android.content.res.OplusThemeResources;
import android.media.IAudioService;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import com.oplus.atlas.OplusAtlasManager;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.util.OplusResolverIntentUtil;
import java.util.Arrays;

/* loaded from: classes.dex */
public class AudioManagerExtImpl implements IAudioManagerExt {
    private static final int ADJUST_LOWER = -1;
    private static final int ADJUST_MUTE = -100;
    private static final int ADJUST_RAISE = 1;
    private static final int ADJUST_SAME = 0;
    private static final int ADJUST_UNMUTE = 100;
    private static final String AJSP_INTERFACE_NAME = "adjustStreamVolumePermission";
    private static final String AUDIO_PARAMETER_KEY_OPLUS_OCAR_MODE = "ocar_mode";
    private static final String AUDIO_PARAMETER_KEY_OPLUS_SET_VC_DOWNLINK_MUTE = "OPLUS_VC_DOWNLINK_MUTE_MODE";
    private static final String CONTROL_MODULE_NAME = "control";
    private static final String CONTROL_PACKAGE_NAME = "adjustStreamVolume";
    private static final String IN_CALL_MUSIC_SESSION_KEY = "incall_music_sessionId";
    private static final int MIN_CALL_INTERVAL_TIME = 20;
    private static final String OPLUS_CAST_PACKAGE_NAME = "com.oplus.cast";
    private static final String OPLUS_GAMES_PACKAGE_NAME = "com.oplus.games";
    private static final String OPLUS_SET_TRACKVOLUME = "OPLUS_AUDIO_SET_TRACKVOLUME";
    private static final String OPLUS_SYSTEMUI_PACKAGE_NAME = "com.android.systemui";
    private static final String PARAMETER_SUPER_VOLUME = "super_volume";
    private static final String PLAYBACK_CAPTURE_UID = "playbackCaptureUid";
    private static final int RINGER_MODE_SILENT = 0;
    private static final String TAG = "AudioManagerExtImpl";
    private static final String VDC_SERVICE_PACKAGE_NAME = "com.oplus.vdc";
    private static final String mDebugkeyValuePairs = "OPLUS_AUDIO_SET_DEBUG_LOG";
    private static final String mDebugkeyValuePairsOff = "off";
    private static final String mDebugkeyValuePairsOn = "on";
    private static final String mDumpkeyAudioDumpAAudio = "vendor.aaudio.pcm";
    private static final String mDumpkeyAudioDumpDrc = "vendor.af.mixer.drc.pcm";
    private static final String mDumpkeyAudioDumpEffect = "vendor.af.effect.pcm";
    private static final String mDumpkeyAudioDumpMixer = "vendor.af.mixer.pcm";
    private static final String mDumpkeyAudioDumpMixerEnd = "vendor.af.mixer.end.pcm";
    private static final String mDumpkeyAudioDumpOffload = "vendor.af.offload.write.raw";
    private static final String mDumpkeyAudioDumpRecord = "vendor.af.record.dump.pcm";
    private static final String mDumpkeyAudioDumpResampler = "vendor.af.resampler.pcm";
    private static final String mDumpkeyAudioDumpTrack = "vendor.af.track.pcm";
    private static final String mDumpkeyValuePairs = "pcm_dump";
    private static final String mDumpkeyValuePairsMtkOn = "1";
    private static final String mDumpkeyValuePairsOff = "0";
    private static final String mDumpkeyValuePairsQcomOn = "2047";
    private static AudioManagerExtImpl sInstance;
    private static IAudioService sService;
    private boolean mOplusMultiAppVolumeAdjustSupported;
    public static boolean mDebugLog = false;
    private static final String[] LimitGetStreamVolumePackageName = {"com.tencent.mobileqq", "com.tencent.mtt", "com.tencent.mm", "com.o.AtlasTest", "com.google.android.youtube", "com.facebook.katana", "com.google.android.apps.youtube.music", "com.google.android.googlequicksearchbox", "com.instagram.android", "com.truecaller", OplusThemeResources.FRAMEWORK_PACKAGE, "com.miHoYo.hkrpg", "com.spotify.music", "com.twitter.android", "ahaflix.tv", "com.vkontakte.android", "com.vk.equals", "com.microsoft.appmanager", "com.tencent.KiHan", "com.jio.media.ondemand"};
    private final Object mGetStreamVolumeLock = new Object();
    private int mStreamVolumeIndex = -1;
    private long mPreTimeMills = 0;
    private String mPrePackageName = "";

    public AudioManagerExtImpl() {
        this.mOplusMultiAppVolumeAdjustSupported = false;
        if (OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_MULTI_APP_VOLUME_ADJUST_SUPPORT)) {
            this.mOplusMultiAppVolumeAdjustSupported = true;
        }
    }

    public static AudioManagerExtImpl getInstance(Object obj) {
        AudioManagerExtImpl audioManagerExtImpl;
        synchronized (AudioManagerExtImpl.class) {
            if (sInstance == null) {
                sInstance = new AudioManagerExtImpl();
            }
            audioManagerExtImpl = sInstance;
        }
        return audioManagerExtImpl;
    }

    private static IAudioService getService() {
        IAudioService iAudioService = sService;
        if (iAudioService != null) {
            return iAudioService;
        }
        IBinder b = ServiceManager.getService(OplusResolverIntentUtil.DEFAULT_APP_AUDIO);
        IAudioService asInterface = IAudioService.Stub.asInterface(b);
        sService = asInterface;
        return asInterface;
    }

    public boolean getDebugLog() {
        boolean z = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        mDebugLog = z;
        return z;
    }

    public void setLogOn() {
        Log.d(TAG, "setDebugLogOn +++");
        setParametersForCommonExtends("OPLUS_AUDIO_SET_DEBUG_LOG=on");
        if (Build.isQcomPlatform()) {
            setQcomDump(true);
        } else if (Build.isMtkPlatform()) {
            setMtkDump(true);
        }
    }

    public void setLogOff() {
        Log.d(TAG, "setDebugLogOff +++");
        setParametersForCommonExtends("OPLUS_AUDIO_SET_DEBUG_LOG=off");
        if (Build.isQcomPlatform()) {
            setQcomDump(false);
        } else if (Build.isMtkPlatform()) {
            setMtkDump(false);
        }
    }

    public void setLogDump() {
        Log.d(TAG, "setDebugLogDump +++");
    }

    public void setMtkDump(boolean on) {
        if (on) {
            AudioSystem.setParameters("vendor.af.mixer.pcm=1");
            AudioSystem.setParameters("vendor.af.track.pcm=1");
            AudioSystem.setParameters("vendor.af.offload.write.raw=1");
            AudioSystem.setParameters("vendor.af.resampler.pcm=1");
            AudioSystem.setParameters("vendor.af.mixer.end.pcm=1");
            AudioSystem.setParameters("vendor.af.record.dump.pcm=1");
            AudioSystem.setParameters("vendor.af.effect.pcm=1");
            AudioSystem.setParameters("vendor.af.mixer.drc.pcm=1");
            AudioSystem.setParameters("vendor.aaudio.pcm=1");
            return;
        }
        AudioSystem.setParameters("vendor.af.mixer.pcm=0");
        AudioSystem.setParameters("vendor.af.track.pcm=0");
        AudioSystem.setParameters("vendor.af.offload.write.raw=0");
        AudioSystem.setParameters("vendor.af.resampler.pcm=0");
        AudioSystem.setParameters("vendor.af.mixer.end.pcm=0");
        AudioSystem.setParameters("vendor.af.record.dump.pcm=0");
        AudioSystem.setParameters("vendor.af.effect.pcm=0");
        AudioSystem.setParameters("vendor.af.mixer.drc.pcm=0");
        AudioSystem.setParameters("vendor.aaudio.pcm=0");
    }

    public void setQcomDump(boolean on) {
        if (on) {
            AudioSystem.setParameters("pcm_dump=2047");
        } else {
            AudioSystem.setParameters("pcm_dump=0");
        }
    }

    public boolean adjustStreamVolumePermission(Context context, int streamType, int direction) {
        IAudioService service = getService();
        if (streamType == 3) {
            try {
                if (!canSetStreamVolumeFromAudioServer()) {
                    String result = OplusAtlasManager.getInstance().getAttributeByAppName(CONTROL_MODULE_NAME, CONTROL_PACKAGE_NAME);
                    if (result != null && result.equalsIgnoreCase("true") && !OplusAtlasManager.getInstance().interfaceCallPermissionCheck(AJSP_INTERFACE_NAME, context.getOpPackageName())) {
                        Log.d(TAG, context.getOpPackageName() + " do not have adjustStreamVolume permission.");
                        return false;
                    }
                    boolean disableMute = SystemProperties.getBoolean("ro.config.disable_mute", false);
                    if (!disableMute || streamType != 3 || ((direction != -100 && direction != 100) || !service.isMusicActive(false) || OplusAtlasManager.getInstance().interfaceCallPermissionCheck("adjustStreamVolumeMute", context.getOpPackageName()))) {
                        return true;
                    }
                    Log.d(TAG, context.getOpPackageName() + " can not set STREAM_MUSIC mute.");
                    return false;
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return true;
    }

    private int oplusCheckApsAndGetStreamVolume(int streamType) {
        int index = AudioSystem.DEFAULT_STREAM_VOLUME[streamType];
        try {
            if (AudioSystem.checkAudioPolicyService() == 0 && AudioSystem.checkAudioFlinger() == 0) {
                int index2 = getService().getStreamVolume(streamType);
                return index2;
            }
            Log.d(TAG, "check_AudioPolicyService = false");
            return index;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int oplusGetStreamVolume(Context context, int streamType) {
        String packageName = context.getOpPackageName();
        if (Arrays.asList(LimitGetStreamVolumePackageName).contains(packageName) && streamType == 3) {
            if (isGetStreamVolumeFrequently(packageName)) {
                int index = getStreamVolumeInDaemon();
                Log.d(TAG, "getStreamVolume too fast, volume index=" + index + ", package=" + packageName);
                return index;
            }
            int index2 = oplusCheckApsAndGetStreamVolume(streamType);
            setStreamVolumeInDaemon(index2, packageName);
            Log.d(TAG, "getStreamVolume normal, volume index=" + index2);
            return index2;
        }
        int index3 = oplusCheckApsAndGetStreamVolume(streamType);
        Log.d(TAG, "getStreamVolume packageName=" + packageName + ", index=" + index3 + ", streamType=" + streamType);
        return index3;
    }

    public int oplusGetStreamMaxVolume(Context context, int streamType) {
        IAudioService service = getService();
        if (streamType == 3) {
            try {
                if (Process.myUid() > 10000) {
                    String value = OplusAtlasManager.getInstance().getAttributeByAppName("oplus-getStreamMaxVolume", context.getOpPackageName());
                    if (value != null) {
                        Log.d(TAG, "getStreamMaxVolume, value = " + value + " streamType " + streamType + " from " + context.getOpPackageName());
                        try {
                            int maxVolume = Integer.parseInt(value);
                            return maxVolume;
                        } catch (NumberFormatException e) {
                            return service.getStreamMaxVolume(streamType);
                        }
                    }
                    return service.getStreamMaxVolume(streamType);
                }
            } catch (RemoteException e2) {
                throw e2.rethrowFromSystemServer();
            }
        }
        return service.getStreamMaxVolume(streamType);
    }

    public boolean setRingerModePermission(Context context, int ringerMode) {
        if (ringerMode == 0 && !OplusAtlasManager.getInstance().interfaceCallPermissionCheck("setRingerModePermission", context.getOpPackageName())) {
            Log.d(TAG, context.getOpPackageName() + "do not setRingerModePermission to RINGER_MODE_SILENT");
            return false;
        }
        return true;
    }

    private boolean canSetStreamVolumeFromAudioServer() {
        IAudioService service = getService();
        try {
            return service.setStreamVolumePermission();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setStreamVolumePermission(Context context, int streamType) {
        String result;
        if (streamType == 3) {
            context.getOpPackageName();
            if (!canSetStreamVolumeFromAudioServer() && (result = OplusAtlasManager.getInstance().getAttributeByAppName(CONTROL_MODULE_NAME, "setStreamVolume")) != null && result.equalsIgnoreCase("true") && !OplusAtlasManager.getInstance().interfaceCallPermissionCheck("setStreamVolumePermission", context.getOpPackageName())) {
                Log.d(TAG, context.getOpPackageName() + " do not have setStreamVolume Permission ");
                return false;
            }
        }
        return true;
    }

    public boolean setSpeakerphoneOnPermission() {
        return true;
    }

    public boolean setBluetoothScoOnPermission() {
        return true;
    }

    public boolean setMicrophoneMutePermission(Context context) {
        String result = OplusAtlasManager.getInstance().getAttributeByAppName(CONTROL_MODULE_NAME, "setMicrophoneMute");
        if (result != null && result.equalsIgnoreCase("true") && !OplusAtlasManager.getInstance().interfaceCallPermissionCheck("setMicrophoneMutePermission", context.getOpPackageName())) {
            Log.d(TAG, context.getOpPackageName() + " do not setMicrophoneMute in call");
            return false;
        }
        return true;
    }

    public boolean setMuteUidVolumePermission(Context context, String keyValuePairs) {
        String value;
        String state = OplusAtlasManager.getInstance().getAttributeByAppName(CONTROL_MODULE_NAME, "muteUidVolume");
        if (state != null && state.equalsIgnoreCase("true") && (value = OplusAtlasManager.getInstance().getAttributeByAppName("oplus-muteStreamVolume", context.getOpPackageName())) != null && value.equals("true")) {
            Log.d(TAG, "setParameters: " + keyValuePairs + " PackageName: " + context.getOpPackageName());
            return true;
        }
        return false;
    }

    public boolean setCustomApiParametersPermission(Context context, String keyValuePairs) {
        String value;
        String state = OplusAtlasManager.getInstance().getAttributeByAppName(CONTROL_MODULE_NAME, "customApiParamSet");
        if (state == null || !state.equalsIgnoreCase("true") || (value = OplusAtlasManager.getInstance().getAttributeByAppName("customApi-setParameters", context.getOpPackageName())) == null || !value.equals("true")) {
            return false;
        }
        Log.d(TAG, "setParameters: " + keyValuePairs + " PackageName: " + context.getOpPackageName());
        return true;
    }

    public boolean setParametersPermission(Context context, String keyValuePairs) {
        if (keyValuePairs.startsWith("oplusMuteStream")) {
            return setMuteUidVolumePermission(context, keyValuePairs);
        }
        if (Binder.getCallingUid() >= 10000 && keyValuePairs.startsWith(PARAMETER_SUPER_VOLUME)) {
            Log.e(TAG, "apps that don't have permission set super volume, uid=" + Binder.getCallingUid() + ", pid=" + Binder.getCallingPid());
            return false;
        }
        if (isCustomApiParameters(keyValuePairs)) {
            return "com.oplus.customize.coreapp".equals(context.getOpPackageName()) || setCustomApiParametersPermission(context, keyValuePairs);
        }
        if (keyValuePairs.startsWith(IN_CALL_MUSIC_SESSION_KEY)) {
            return VDC_SERVICE_PACKAGE_NAME.equals(context.getOpPackageName());
        }
        if (keyValuePairs.startsWith(OPLUS_SET_TRACKVOLUME)) {
            boolean hasTrackVolumePermission = OPLUS_GAMES_PACKAGE_NAME.equals(context.getOpPackageName());
            if (this.mOplusMultiAppVolumeAdjustSupported) {
                return hasTrackVolumePermission || "com.android.systemui".equals(context.getOpPackageName());
            }
            return hasTrackVolumePermission;
        }
        if (keyValuePairs.startsWith(AUDIO_PARAMETER_KEY_OPLUS_SET_VC_DOWNLINK_MUTE)) {
            return VDC_SERVICE_PACKAGE_NAME.equals(context.getOpPackageName());
        }
        if (keyValuePairs.startsWith(AUDIO_PARAMETER_KEY_OPLUS_OCAR_MODE)) {
            return VDC_SERVICE_PACKAGE_NAME.equals(context.getOpPackageName());
        }
        if (keyValuePairs.startsWith(PLAYBACK_CAPTURE_UID)) {
            return OPLUS_CAST_PACKAGE_NAME.equals(context.getOpPackageName()) || (getDebugLog() && Binder.getCallingUid() < 10000);
        }
        return true;
    }

    public boolean setParametersForCommonExtends(String keyValuePairs) {
        if (keyValuePairs.startsWith("OPLUS_AUDIO_SET_") || keyValuePairs.startsWith("DualHeadPh")) {
            IAudioService service = getService();
            try {
                service.setParameters(keyValuePairs);
                return true;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        if (keyValuePairs.startsWith("binaural_recording_switch")) {
            IAudioService service2 = getService();
            try {
                service2.cacheBinauralRecordParameters(keyValuePairs);
                return false;
            } catch (RemoteException e2) {
                throw e2.rethrowFromSystemServer();
            }
        }
        if (keyValuePairs.startsWith("OplusLosslessFlag") || keyValuePairs.startsWith(PLAYBACK_CAPTURE_UID)) {
            IAudioService service3 = getService();
            try {
                service3.setParameters(keyValuePairs);
                return false;
            } catch (RemoteException e3) {
                throw e3.rethrowFromSystemServer();
            }
        }
        if (keyValuePairs.startsWith(AUDIO_PARAMETER_KEY_OPLUS_SET_VC_DOWNLINK_MUTE)) {
            IAudioService service4 = getService();
            try {
                service4.setParameters(keyValuePairs);
                return false;
            } catch (RemoteException e4) {
                throw e4.rethrowFromSystemServer();
            }
        }
        return false;
    }

    public String getParametersForCommonExtends(String keys) {
        IAudioService service = getService();
        try {
            return service.getParameters(keys);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private boolean isCustomApiParameters(String keyValuePairs) {
        if (keyValuePairs.startsWith("record_forbid") || keyValuePairs.startsWith("oplus_power_analysis_command")) {
            return true;
        }
        return false;
    }

    private void setStreamVolumeInDaemon(int volume, String currentPackageName) {
        synchronized (this.mGetStreamVolumeLock) {
            this.mStreamVolumeIndex = volume;
            this.mPrePackageName = currentPackageName;
            this.mPreTimeMills = System.currentTimeMillis();
        }
    }

    private boolean isGetStreamVolumeFrequently(String currentPackageName) {
        long currentTimeMills = System.currentTimeMillis();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(this.mPrePackageName) && currentTimeMills - this.mPreTimeMills < 20) {
            return true;
        }
        return false;
    }

    private int getStreamVolumeInDaemon() {
        int i;
        synchronized (this.mGetStreamVolumeLock) {
            i = this.mStreamVolumeIndex;
        }
        return i;
    }
}
