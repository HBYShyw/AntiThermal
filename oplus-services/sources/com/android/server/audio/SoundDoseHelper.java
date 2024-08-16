package com.android.server.audio;

import android.R;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.media.AudioSystem;
import android.media.ISoundDose;
import android.media.ISoundDoseCallback;
import android.media.SoundDoseRecord;
import android.os.Binder;
import android.os.HandlerExecutor;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import android.util.Log;
import android.util.MathUtils;
import android.util.SparseIntArray;
import com.android.internal.annotations.GuardedBy;
import com.android.server.am.HostingRecord;
import com.android.server.audio.AudioService;
import com.android.server.audio.AudioServiceEvents;
import com.android.server.utils.EventLogger;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SoundDoseHelper {
    static final String ACTION_CHECK_MUSIC_ACTIVE = "com.android.server.audio.action.CHECK_MUSIC_ACTIVE";
    private static final int CSD_WARNING_TIMEOUT_MS_ACCUMULATION_START = -1;
    private static final int CSD_WARNING_TIMEOUT_MS_DOSE_1X = 7000;
    private static final int CSD_WARNING_TIMEOUT_MS_DOSE_5X = 5000;
    private static final int CSD_WARNING_TIMEOUT_MS_MOMENTARY_EXPOSURE = 5000;
    private static final String FEATURE_FLAG_ENABLE_CSD = "enable_csd";
    private static final long GLOBAL_TIME_OFFSET_UNINITIALIZED = -1;
    private static final int MOMENTARY_EXPOSURE_TIMEOUT_MS = 72000000;
    private static final int MOMENTARY_EXPOSURE_TIMEOUT_UNINITIALIZED = -1;
    private static final int MSG_CONFIGURE_SAFE_MEDIA = 1001;
    private static final int MSG_CONFIGURE_SAFE_MEDIA_FORCED = 1002;
    static final int MSG_CSD_UPDATE_ATTENUATION = 1006;
    private static final int MSG_PERSIST_CSD_VALUES = 1005;
    private static final int MSG_PERSIST_MUSIC_ACTIVE_MS = 1004;
    private static final int MSG_PERSIST_SAFE_VOLUME_STATE = 1003;
    private static final int MUSIC_ACTIVE_POLL_PERIOD_MS = 60000;
    private static final String PERSIST_CSD_RECORD_FIELD_SEPARATOR = ",";
    private static final String PERSIST_CSD_RECORD_SEPARATOR = "\\|";
    private static final String PERSIST_CSD_RECORD_SEPARATOR_CHAR = "|";
    private static final int REQUEST_CODE_CHECK_MUSIC_ACTIVE = 1;
    private static final int SAFE_MEDIA_VOLUME_ACTIVE = 3;
    private static final int SAFE_MEDIA_VOLUME_DISABLED = 1;
    private static final int SAFE_MEDIA_VOLUME_INACTIVE = 2;
    private static final int SAFE_MEDIA_VOLUME_NOT_CONFIGURED = 0;
    private static final int SAFE_MEDIA_VOLUME_UNINITIALIZED = -1;
    private static final int SAFE_VOLUME_CONFIGURE_TIMEOUT_MS = 30000;
    private static final String SYSTEM_PROPERTY_SAFEMEDIA_BYPASS = "audio.safemedia.bypass";
    private static final String SYSTEM_PROPERTY_SAFEMEDIA_CSD_FORCE = "audio.safemedia.csd.force";
    private static final String SYSTEM_PROPERTY_SAFEMEDIA_FORCE = "audio.safemedia.force";
    private static final String TAG = "AS.SoundDoseHelper";
    private static final int UNSAFE_VOLUME_MUSIC_ACTIVE_MS_MAX = 72000000;
    private final AlarmManager mAlarmManager;
    private final AudioService.AudioHandler mAudioHandler;
    private final AudioService mAudioService;
    private final Context mContext;

    @GuardedBy({"mCsdStateLock"})
    private float mCurrentCsd;

    @GuardedBy({"mCsdStateLock"})
    private final List<SoundDoseRecord> mDoseRecords;

    @GuardedBy({"mCsdStateLock"})
    private long mGlobalTimeOffsetInSecs;

    @GuardedBy({"mCsdStateLock"})
    private long mLastMomentaryExposureTimeMs;
    private int mMusicActiveMs;

    @GuardedBy({"mCsdStateLock"})
    private float mNextCsdWarning;
    private StreamVolumeCommand mPendingVolumeCommand;
    private float mSafeMediaVolumeDbfs;
    private int mSafeMediaVolumeIndex;
    private int mSafeMediaVolumeState;
    private final SettingsAdapter mSettings;
    private final AtomicReference<ISoundDose> mSoundDose;
    private final ISoundDoseCallback.Stub mSoundDoseCallback;
    private final AudioService.ISafeHearingVolumeController mVolumeController;
    private final EventLogger mLogger = new EventLogger(30, "CSD updates");
    private int mMcc = 0;
    private final Object mSafeMediaVolumeStateLock = new Object();
    private final SparseIntArray mSafeMediaVolumeDevices = new SparseIntArray();
    private long mLastMusicActiveTimeMs = 0;
    private PendingIntent mMusicActiveIntent = null;
    private final AtomicBoolean mEnableCsd = new AtomicBoolean(false);
    private SoundDoseHelperWrapper mSdhWrapper = new SoundDoseHelperWrapper();
    private final Object mCsdStateLock = new Object();

    private static long convertToBootTime(long j, long j2) {
        return j - j2;
    }

    private static long convertToGlobalTime(long j, long j2) {
        return j + j2;
    }

    private static String safeMediaVolumeStateToString(int i) {
        if (i == 0) {
            return "SAFE_MEDIA_VOLUME_NOT_CONFIGURED";
        }
        if (i == 1) {
            return "SAFE_MEDIA_VOLUME_DISABLED";
        }
        if (i == 2) {
            return "SAFE_MEDIA_VOLUME_INACTIVE";
        }
        if (i != 3) {
            return null;
        }
        return "SAFE_MEDIA_VOLUME_ACTIVE";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SoundDoseHelper(AudioService audioService, Context context, AudioService.AudioHandler audioHandler, SettingsAdapter settingsAdapter, AudioService.ISafeHearingVolumeController iSafeHearingVolumeController) {
        AtomicReference<ISoundDose> atomicReference = new AtomicReference<>();
        this.mSoundDose = atomicReference;
        this.mCurrentCsd = 0.0f;
        this.mLastMomentaryExposureTimeMs = -1L;
        this.mNextCsdWarning = 1.0f;
        this.mDoseRecords = new ArrayList();
        this.mGlobalTimeOffsetInSecs = -1L;
        ISoundDoseCallback.Stub stub = new ISoundDoseCallback.Stub() { // from class: com.android.server.audio.SoundDoseHelper.1
            public void onMomentaryExposure(float f, int i) {
                boolean z;
                if (!SoundDoseHelper.this.mEnableCsd.get()) {
                    Log.w(SoundDoseHelper.TAG, "onMomentaryExposure: csd not supported, ignoring callback");
                    return;
                }
                Log.w(SoundDoseHelper.TAG, "DeviceId " + i + " triggered momentary exposure with value: " + f);
                SoundDoseHelper.this.mLogger.enqueue(AudioServiceEvents.SoundDoseEvent.getMomentaryExposureEvent(f));
                synchronized (SoundDoseHelper.this.mCsdStateLock) {
                    z = SoundDoseHelper.this.mLastMomentaryExposureTimeMs < 0 || System.currentTimeMillis() - SoundDoseHelper.this.mLastMomentaryExposureTimeMs >= 72000000;
                    SoundDoseHelper.this.mLastMomentaryExposureTimeMs = System.currentTimeMillis();
                }
                if (z) {
                    SoundDoseHelper.this.mVolumeController.postDisplayCsdWarning(3, SoundDoseHelper.this.getTimeoutMsForWarning(3));
                }
            }

            public void onNewCsdValue(float f, SoundDoseRecord[] soundDoseRecordArr) {
                if (!SoundDoseHelper.this.mEnableCsd.get()) {
                    Log.w(SoundDoseHelper.TAG, "onNewCsdValue: csd not supported, ignoring value");
                    return;
                }
                Log.i(SoundDoseHelper.TAG, "onNewCsdValue: " + f);
                synchronized (SoundDoseHelper.this.mCsdStateLock) {
                    if (SoundDoseHelper.this.mCurrentCsd < f) {
                        if (SoundDoseHelper.this.mCurrentCsd < SoundDoseHelper.this.mNextCsdWarning && f >= SoundDoseHelper.this.mNextCsdWarning) {
                            if (SoundDoseHelper.this.mNextCsdWarning == 5.0f) {
                                SoundDoseHelper.this.mVolumeController.postDisplayCsdWarning(2, SoundDoseHelper.this.getTimeoutMsForWarning(2));
                                SoundDoseHelper.this.mAudioService.postLowerVolumeToRs1();
                            } else {
                                SoundDoseHelper.this.mVolumeController.postDisplayCsdWarning(1, SoundDoseHelper.this.getTimeoutMsForWarning(1));
                            }
                            SoundDoseHelper.this.mNextCsdWarning += 1.0f;
                        }
                    } else if (f < SoundDoseHelper.this.mNextCsdWarning - 1.0f && SoundDoseHelper.this.mNextCsdWarning >= 2.0f) {
                        SoundDoseHelper.this.mNextCsdWarning -= 1.0f;
                    }
                    SoundDoseHelper.this.mCurrentCsd = f;
                    SoundDoseHelper.this.updateSoundDoseRecords_l(soundDoseRecordArr, f);
                }
            }
        };
        this.mSoundDoseCallback = stub;
        this.mAudioService = audioService;
        this.mAudioHandler = audioHandler;
        this.mSettings = settingsAdapter;
        this.mVolumeController = iSafeHearingVolumeController;
        this.mContext = context;
        initSafeVolumes();
        this.mSafeMediaVolumeState = settingsAdapter.getGlobalInt(audioService.getContentResolver(), "audio_safe_volume_state", 0);
        this.mSafeMediaVolumeIndex = context.getResources().getInteger(R.integer.leanback_setup_alpha_forward_in_content_delay) * 10;
        atomicReference.set(AudioSystem.getSoundDoseInterface(stub));
        initCsd();
        this.mAlarmManager = (AlarmManager) context.getSystemService(HostingRecord.TRIGGER_TYPE_ALARM);
        DeviceConfig.addOnPropertiesChangedListener("media", new HandlerExecutor(audioHandler), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.audio.SoundDoseHelper$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                SoundDoseHelper.this.lambda$new$0(properties);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(DeviceConfig.Properties properties) {
        updateCsdEnabled("onPropertiesChanged");
    }

    void initSafeVolumes() {
        this.mSafeMediaVolumeDevices.append(4, -1);
        this.mSafeMediaVolumeDevices.append(8, -1);
        this.mSafeMediaVolumeDevices.append(67108864, -1);
        this.mSafeMediaVolumeDevices.append(AudioFormat.APTX, -1);
        this.mSafeMediaVolumeDevices.append(536870914, -1);
        this.mSafeMediaVolumeDevices.append(256, -1);
        this.mSafeMediaVolumeDevices.append(128, -1);
        this.mSafeMediaVolumeDevices.append(16, -1);
        this.mSafeMediaVolumeDevices.append(32, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getOutputRs2UpperBound() {
        if (!this.mEnableCsd.get()) {
            return 0.0f;
        }
        ISoundDose iSoundDose = this.mSoundDose.get();
        if (iSoundDose == null) {
            Log.w(TAG, "Sound dose interface not initialized");
            return 0.0f;
        }
        try {
            return iSoundDose.getOutputRs2UpperBound();
        } catch (RemoteException e) {
            Log.e(TAG, "Exception while getting the RS2 exposure value", e);
            return 0.0f;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOutputRs2UpperBound(float f) {
        if (this.mEnableCsd.get()) {
            ISoundDose iSoundDose = this.mSoundDose.get();
            if (iSoundDose == null) {
                Log.w(TAG, "Sound dose interface not initialized");
                return;
            }
            try {
                iSoundDose.setOutputRs2UpperBound(f);
            } catch (RemoteException e) {
                Log.e(TAG, "Exception while setting the RS2 exposure value", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getCsd() {
        if (!this.mEnableCsd.get()) {
            return -1.0f;
        }
        ISoundDose iSoundDose = this.mSoundDose.get();
        if (iSoundDose == null) {
            Log.w(TAG, "Sound dose interface not initialized");
            return -1.0f;
        }
        try {
            return iSoundDose.getCsd();
        } catch (RemoteException e) {
            Log.e(TAG, "Exception while getting the CSD value", e);
            return -1.0f;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCsd(float f) {
        SoundDoseRecord[] soundDoseRecordArr;
        if (this.mEnableCsd.get()) {
            synchronized (this.mCsdStateLock) {
                this.mCurrentCsd = f;
                this.mNextCsdWarning = (float) Math.floor(f + 1.0d);
                this.mDoseRecords.clear();
                if (this.mCurrentCsd > 0.0f) {
                    SoundDoseRecord soundDoseRecord = new SoundDoseRecord();
                    soundDoseRecord.timestamp = SystemClock.elapsedRealtime() / 1000;
                    soundDoseRecord.value = f;
                    this.mDoseRecords.add(soundDoseRecord);
                }
                soundDoseRecordArr = (SoundDoseRecord[]) this.mDoseRecords.toArray(new SoundDoseRecord[0]);
            }
            ISoundDose iSoundDose = this.mSoundDose.get();
            if (iSoundDose == null) {
                Log.w(TAG, "Sound dose interface not initialized");
                return;
            }
            try {
                iSoundDose.resetCsd(f, soundDoseRecordArr);
            } catch (RemoteException e) {
                Log.e(TAG, "Exception while setting the CSD value", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetCsdTimeouts() {
        if (this.mEnableCsd.get()) {
            synchronized (this.mCsdStateLock) {
                this.mLastMomentaryExposureTimeMs = -1L;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forceUseFrameworkMel(boolean z) {
        if (this.mEnableCsd.get()) {
            ISoundDose iSoundDose = this.mSoundDose.get();
            if (iSoundDose == null) {
                Log.w(TAG, "Sound dose interface not initialized");
                return;
            }
            try {
                iSoundDose.forceUseFrameworkMel(z);
            } catch (RemoteException e) {
                Log.e(TAG, "Exception while forcing the internal MEL computation", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forceComputeCsdOnAllDevices(boolean z) {
        if (this.mEnableCsd.get()) {
            ISoundDose iSoundDose = this.mSoundDose.get();
            if (iSoundDose == null) {
                Log.w(TAG, "Sound dose interface not initialized");
                return;
            }
            try {
                iSoundDose.forceComputeCsdOnAllDevices(z);
            } catch (RemoteException e) {
                Log.e(TAG, "Exception while forcing CSD computation on all devices", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCsdEnabled() {
        if (!this.mEnableCsd.get()) {
            return false;
        }
        ISoundDose iSoundDose = this.mSoundDose.get();
        if (iSoundDose == null) {
            Log.w(TAG, "Sound dose interface not initialized");
            return false;
        }
        try {
            return iSoundDose.isSoundDoseHalSupported();
        } catch (RemoteException e) {
            Log.e(TAG, "Exception while forcing CSD computation on all devices", e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int safeMediaVolumeIndex(int i) {
        int i2 = this.mSafeMediaVolumeDevices.get(i);
        return i2 == -1 ? AudioService.MAX_STREAM_VOLUME[3] : i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restoreMusicActiveMs() {
        synchronized (this.mSafeMediaVolumeStateLock) {
            this.mMusicActiveMs = MathUtils.constrain(this.mSettings.getSecureIntForUser(this.mAudioService.getContentResolver(), "unsafe_volume_music_active_ms", 0, -2), 0, 72000000);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enforceSafeMediaVolumeIfActive(String str) {
        synchronized (this.mSafeMediaVolumeStateLock) {
            if (this.mSafeMediaVolumeState == 3) {
                enforceSafeMediaVolume(str);
            }
        }
    }

    void enforceSafeMediaVolume(String str) {
        AudioService.VolumeStreamState vssVolumeForStream = this.mAudioService.getVssVolumeForStream(3);
        for (int i = 0; i < this.mSafeMediaVolumeDevices.size(); i++) {
            int keyAt = this.mSafeMediaVolumeDevices.keyAt(i);
            int index = vssVolumeForStream.getIndex(keyAt);
            int safeMediaVolumeIndex = safeMediaVolumeIndex(keyAt);
            if (index > safeMediaVolumeIndex) {
                vssVolumeForStream.storeVolume(keyAt, index);
                vssVolumeForStream.setIndex(safeMediaVolumeIndex, keyAt, str, true);
                AudioService.AudioHandler audioHandler = this.mAudioHandler;
                audioHandler.sendMessageAtTime(audioHandler.obtainMessage(0, keyAt, 0, vssVolumeForStream), 0L);
                if (AudioSystem.isStreamActive(3, 0) && AudioSystem.DEVICE_OUT_ALL_A2DP_SET.contains(Integer.valueOf(keyAt)) && this.mAudioService.getWrapper().getDeviceBroker() != null) {
                    this.mAudioService.getWrapper().getDeviceBroker().postSetAvrcpAbsoluteVolumeIndex(vssVolumeForStream.getIndex(keyAt) / 10);
                }
                if (this.mAudioService.getWrapper().getExtImpl().getBluetoothVolSyncSupported() && AudioSystem.isStreamActive(3, 0) && keyAt == 536870912) {
                    this.mAudioService.getWrapper().getDeviceBroker().postSetLeAudioVolumeIndex(vssVolumeForStream.getIndex(keyAt), this.mAudioService.getMaxVssVolumeForStream(3), 3);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean checkSafeMediaVolume(int i, int i2, int i3) {
        boolean checkSafeMediaVolume_l;
        synchronized (this.mSafeMediaVolumeStateLock) {
            checkSafeMediaVolume_l = checkSafeMediaVolume_l(i, i2, i3);
        }
        return checkSafeMediaVolume_l;
    }

    @GuardedBy({"mSafeMediaVolumeStateLock"})
    private boolean checkSafeMediaVolume_l(int i, int i2, int i3) {
        return this.mSafeMediaVolumeState == 3 && AudioService.mStreamVolumeAlias[i] == 3 && safeDevicesContains(i3) && i2 > safeMediaVolumeIndex(i3) && !this.mAudioService.getWrapper().getExtImpl().isSpeakerA2dpDevice(i3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean willDisplayWarningAfterCheckVolume(int i, int i2, int i3, int i4) {
        synchronized (this.mSafeMediaVolumeStateLock) {
            if (!checkSafeMediaVolume_l(i, i2, i3)) {
                return false;
            }
            this.mVolumeController.postDisplaySafeVolumeWarning(this.mAudioService.getWrapper().getExtImpl().isNeedShowUiWarnings(i4, this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid())));
            this.mPendingVolumeCommand = new StreamVolumeCommand(i, i2, i4, i3);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void disableSafeMediaVolume(String str) {
        synchronized (this.mSafeMediaVolumeStateLock) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            setSafeMediaVolumeEnabled(false, str);
            Binder.restoreCallingIdentity(clearCallingIdentity);
            StreamVolumeCommand streamVolumeCommand = this.mPendingVolumeCommand;
            if (streamVolumeCommand != null) {
                if (AudioService.mStreamVolumeAlias[streamVolumeCommand.mStreamType] == 3 && ((AudioSystem.DEVICE_OUT_ALL_A2DP_SET.contains(Integer.valueOf(streamVolumeCommand.mDevice)) || (this.mAudioService.getWrapper().getExtImpl().getBluetoothVolSyncSupported() && this.mPendingVolumeCommand.mDevice == 536870912)) && (this.mPendingVolumeCommand.mFlags & 64) == 0 && this.mAudioService.getWrapper().getDeviceBroker() != null && this.mAudioService.getWrapper().getExtImpl().getBluetoothVolSyncSupported())) {
                    if (this.mPendingVolumeCommand.mDevice == 536870912) {
                        this.mAudioService.getWrapper().getDeviceBroker().postSetLeAudioVolumeIndex(this.mPendingVolumeCommand.mIndex, this.mAudioService.getMaxVssVolumeForStream(3), 3);
                    } else {
                        this.mAudioService.getWrapper().getDeviceBroker().postSetAvrcpAbsoluteVolumeIndex(this.mPendingVolumeCommand.mIndex / 10);
                    }
                }
                AudioService audioService = this.mAudioService;
                StreamVolumeCommand streamVolumeCommand2 = this.mPendingVolumeCommand;
                audioService.onSetStreamVolume(streamVolumeCommand2.mStreamType, streamVolumeCommand2.mIndex, streamVolumeCommand2.mFlags, streamVolumeCommand2.mDevice, str, true, true);
                this.mPendingVolumeCommand = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleMusicActiveCheck() {
        synchronized (this.mSafeMediaVolumeStateLock) {
            cancelMusicActiveCheck();
            this.mMusicActiveIntent = PendingIntent.getBroadcast(this.mContext, 1, new Intent(ACTION_CHECK_MUSIC_ACTIVE), AudioFormat.DTS_HD);
            this.mAlarmManager.setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + 60000, this.mMusicActiveIntent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onCheckMusicActive(String str, boolean z) {
        synchronized (this.mSafeMediaVolumeStateLock) {
            if (this.mSafeMediaVolumeState == 2) {
                int deviceForStream = this.mAudioService.getDeviceForStream(3);
                if (safeDevicesContains(deviceForStream) && z) {
                    scheduleMusicActiveCheck();
                    if (this.mAudioService.getVssVolumeForDevice(3, deviceForStream) > safeMediaVolumeIndex(deviceForStream)) {
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        long j = this.mLastMusicActiveTimeMs;
                        if (j != 0) {
                            this.mMusicActiveMs += (int) (elapsedRealtime - j);
                        }
                        this.mLastMusicActiveTimeMs = elapsedRealtime;
                        Log.d(TAG, "20H onCheckMusicActive:" + this.mMusicActiveMs);
                        if (this.mMusicActiveMs > 72000000) {
                            setSafeMediaVolumeEnabled(true, str);
                            this.mMusicActiveMs = 0;
                        }
                        saveMusicActiveMs();
                    }
                } else {
                    cancelMusicActiveCheck();
                    this.mLastMusicActiveTimeMs = 0L;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void configureSafeMedia(boolean z, String str) {
        long j;
        int i = z ? 1002 : 1001;
        this.mAudioHandler.removeMessages(i);
        if (z) {
            j = SystemClock.uptimeMillis() + (SystemProperties.getBoolean(SYSTEM_PROPERTY_SAFEMEDIA_BYPASS, false) ? 0 : 30000);
        } else {
            j = 0;
        }
        AudioService.AudioHandler audioHandler = this.mAudioHandler;
        audioHandler.sendMessageAtTime(audioHandler.obtainMessage(i, 0, 0, str), j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initSafeMediaVolumeIndex() {
        for (int i = 0; i < this.mSafeMediaVolumeDevices.size(); i++) {
            int keyAt = this.mSafeMediaVolumeDevices.keyAt(i);
            this.mSafeMediaVolumeDevices.put(keyAt, getSafeDeviceMediaVolumeIndex(keyAt));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSafeMediaVolumeIndex(int i) {
        if (this.mSafeMediaVolumeState == 3 && safeDevicesContains(i)) {
            return safeMediaVolumeIndex(i);
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean raiseVolumeDisplaySafeMediaVolume(int i, int i2, int i3, int i4) {
        if (!checkSafeMediaVolume(i, i2, i3)) {
            return false;
        }
        this.mVolumeController.postDisplaySafeVolumeWarning(i4);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean safeDevicesContains(int i) {
        return this.mSafeMediaVolumeDevices.indexOfKey(i) >= 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invalidatPendingVolumeCommand() {
        synchronized (this.mSafeMediaVolumeStateLock) {
            this.mPendingVolumeCommand = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleMessage(Message message) {
        int i = message.what;
        switch (i) {
            case 1001:
            case 1002:
                onConfigureSafeMedia(i == 1002, (String) message.obj);
                return;
            case 1003:
                onPersistSafeVolumeState(message.arg1);
                return;
            case 1004:
                this.mSettings.putSecureIntForUser(this.mAudioService.getContentResolver(), "unsafe_volume_music_active_ms", message.arg1, -2);
                return;
            case 1005:
                onPersistSoundDoseRecords();
                return;
            case 1006:
                int i2 = message.arg1;
                boolean z = message.arg2 == 1;
                AudioService.VolumeStreamState volumeStreamState = (AudioService.VolumeStreamState) message.obj;
                updateDoseAttenuation(volumeStreamState.getIndex(i2), i2, volumeStreamState.getStreamType(), z);
                return;
            default:
                Log.e(TAG, "Unexpected msg to handle: " + message.what);
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        printWriter.print("  mEnableCsd=");
        printWriter.println(this.mEnableCsd.get());
        if (this.mEnableCsd.get()) {
            synchronized (this.mCsdStateLock) {
                printWriter.print("  mCurrentCsd=");
                printWriter.println(this.mCurrentCsd);
            }
        }
        printWriter.print("  mSafeMediaVolumeState=");
        printWriter.println(safeMediaVolumeStateToString(this.mSafeMediaVolumeState));
        printWriter.print("  mSafeMediaVolumeIndex=");
        printWriter.println(this.mSafeMediaVolumeIndex);
        for (int i = 0; i < this.mSafeMediaVolumeDevices.size(); i++) {
            printWriter.print("  mSafeMediaVolumeIndex[");
            printWriter.print(this.mSafeMediaVolumeDevices.keyAt(i));
            printWriter.print("]=");
            printWriter.println(this.mSafeMediaVolumeDevices.valueAt(i));
        }
        printWriter.print("  mSafeMediaVolumeDbfs=");
        printWriter.println(this.mSafeMediaVolumeDbfs);
        printWriter.print("  mMusicActiveMs=");
        printWriter.println(this.mMusicActiveMs);
        printWriter.print("  mMcc=");
        printWriter.println(this.mMcc);
        printWriter.print("  mPendingVolumeCommand=");
        printWriter.println(this.mPendingVolumeCommand);
        printWriter.println();
        this.mLogger.dump(printWriter);
        printWriter.println();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reset() {
        Log.d(TAG, "Reset the sound dose helper");
        this.mSoundDose.compareAndExchange(null, AudioSystem.getSoundDoseInterface(this.mSoundDoseCallback));
        synchronized (this.mCsdStateLock) {
            try {
                ISoundDose iSoundDose = this.mSoundDose.get();
                if (iSoundDose != null && iSoundDose.asBinder().isBinderAlive() && this.mCurrentCsd != 0.0f) {
                    Log.d(TAG, "Resetting the saved sound dose value " + this.mCurrentCsd);
                    iSoundDose.resetCsd(this.mCurrentCsd, (SoundDoseRecord[]) this.mDoseRecords.toArray(new SoundDoseRecord[0]));
                }
            } catch (RemoteException unused) {
            }
        }
    }

    private void updateDoseAttenuation(int i, int i2, int i3, boolean z) {
        if (this.mEnableCsd.get()) {
            ISoundDose iSoundDose = this.mSoundDose.get();
            if (iSoundDose == null) {
                Log.w(TAG, "Can not apply attenuation. ISoundDose itf is null.");
                return;
            }
            try {
                if (!z) {
                    iSoundDose.updateAttenuation(0.0f, i2);
                } else if (AudioService.mStreamVolumeAlias[i3] == 3 && safeDevicesContains(i2)) {
                    iSoundDose.updateAttenuation(AudioSystem.getStreamVolumeDB(3, (i + 5) / 10, i2), i2);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Could not apply the attenuation for MEL calculation with volume index " + i, e);
            }
        }
    }

    private void initCsd() {
        ISoundDose iSoundDose = this.mSoundDose.get();
        if (iSoundDose == null) {
            Log.w(TAG, "ISoundDose instance is null.");
            return;
        }
        try {
            iSoundDose.setCsdEnabled(this.mEnableCsd.get());
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot disable CSD", e);
        }
        if (this.mEnableCsd.get()) {
            Log.v(TAG, "Initializing sound dose");
            synchronized (this.mCsdStateLock) {
                if (this.mGlobalTimeOffsetInSecs == -1) {
                    this.mGlobalTimeOffsetInSecs = System.currentTimeMillis() / 1000;
                }
                float f = this.mCurrentCsd;
                float parseGlobalSettingFloat = parseGlobalSettingFloat("audio_safe_csd_current_value", 0.0f);
                this.mCurrentCsd = parseGlobalSettingFloat;
                if (parseGlobalSettingFloat != f) {
                    this.mNextCsdWarning = parseGlobalSettingFloat("audio_safe_csd_next_warning", 1.0f);
                    List<SoundDoseRecord> persistedStringToRecordList = persistedStringToRecordList(this.mSettings.getGlobalString(this.mAudioService.getContentResolver(), "audio_safe_csd_dose_records"), this.mGlobalTimeOffsetInSecs);
                    if (persistedStringToRecordList != null) {
                        this.mDoseRecords.addAll(persistedStringToRecordList);
                    }
                }
            }
            reset();
        }
    }

    private void onConfigureSafeMedia(boolean z, String str) {
        updateCsdEnabled(str);
        synchronized (this.mSafeMediaVolumeStateLock) {
            int i = this.mContext.getResources().getConfiguration().mcc;
            int i2 = this.mMcc;
            if (i2 != i || (i2 == 0 && z)) {
                this.mSafeMediaVolumeIndex = this.mContext.getResources().getInteger(R.integer.leanback_setup_alpha_forward_in_content_delay) * 10;
                initSafeMediaVolumeIndex();
                updateSafeMediaVolume_l(str);
                this.mMcc = i;
            }
        }
    }

    @GuardedBy({"mSafeMediaVolumeStateLock"})
    private void updateSafeMediaVolume_l(String str) {
        int i = 1;
        boolean z = SystemProperties.getBoolean(SYSTEM_PROPERTY_SAFEMEDIA_FORCE, false) || (this.mContext.getResources().getBoolean(17891800) && !this.mEnableCsd.get());
        boolean z2 = SystemProperties.getBoolean(SYSTEM_PROPERTY_SAFEMEDIA_BYPASS, false);
        if (z && !z2) {
            if (this.mSafeMediaVolumeState != 2) {
                if (this.mMusicActiveMs == 0) {
                    this.mSafeMediaVolumeState = 3;
                    enforceSafeMediaVolume(str);
                } else {
                    this.mLastMusicActiveTimeMs = 0L;
                }
            }
            i = 3;
        } else {
            this.mSafeMediaVolumeState = 1;
        }
        AudioService.AudioHandler audioHandler = this.mAudioHandler;
        audioHandler.sendMessageAtTime(audioHandler.obtainMessage(1003, i, 0, null), 0L);
    }

    private void updateCsdEnabled(String str) {
        boolean z = SystemProperties.getBoolean(SYSTEM_PROPERTY_SAFEMEDIA_CSD_FORCE, false);
        if (!z) {
            String property = DeviceConfig.getProperty("media", FEATURE_FLAG_ENABLE_CSD);
            if (property != null) {
                z = Boolean.parseBoolean(property);
            } else {
                z = this.mContext.getResources().getBoolean(17891801);
            }
        }
        if (this.mEnableCsd.compareAndSet(!z, z)) {
            Log.i(TAG, str + ": enable CSD " + z);
            initCsd();
            synchronized (this.mSafeMediaVolumeStateLock) {
                updateSafeMediaVolume_l(str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTimeoutMsForWarning(int i) {
        if (i == 1) {
            return CSD_WARNING_TIMEOUT_MS_DOSE_1X;
        }
        int i2 = 5000;
        if (i != 2 && i != 3) {
            i2 = -1;
            if (i != 4) {
                Log.e(TAG, "Invalid CSD warning " + i, new Exception());
            }
        }
        return i2;
    }

    @GuardedBy({"mSafeMediaVolumeStateLock"})
    private void setSafeMediaVolumeEnabled(boolean z, String str) {
        int i = this.mSafeMediaVolumeState;
        if (i == 0 || i == 1) {
            return;
        }
        if (z && i == 2) {
            this.mSafeMediaVolumeState = 3;
            this.mAudioService.getWrapper().broadcastSafeVolume();
            enforceSafeMediaVolume(str);
        } else {
            if (z || i != 3) {
                return;
            }
            this.mSafeMediaVolumeState = 2;
            this.mMusicActiveMs = 1;
            this.mLastMusicActiveTimeMs = 0L;
            saveMusicActiveMs();
            scheduleMusicActiveCheck();
        }
    }

    @GuardedBy({"mSafeMediaVolumeStateLock"})
    private void cancelMusicActiveCheck() {
        PendingIntent pendingIntent = this.mMusicActiveIntent;
        if (pendingIntent != null) {
            this.mAlarmManager.cancel(pendingIntent);
            this.mMusicActiveIntent = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mSafeMediaVolumeStateLock"})
    public void saveMusicActiveMs() {
        this.mAudioHandler.obtainMessage(1004, this.mMusicActiveMs, 0).sendToTarget();
    }

    private int getSafeDeviceMediaVolumeIndex(int i) {
        if ((i == 8 || i == 4) && !this.mEnableCsd.get()) {
            return this.mSafeMediaVolumeIndex;
        }
        int i2 = AudioService.MIN_STREAM_VOLUME[3];
        int i3 = AudioService.MAX_STREAM_VOLUME[3];
        this.mSafeMediaVolumeDbfs = this.mContext.getResources().getInteger(R.integer.leanback_setup_alpha_forward_in_content_duration) / 100.0f;
        while (Math.abs(i3 - i2) > 1) {
            int i4 = (i3 + i2) / 2;
            float streamVolumeDB = AudioSystem.getStreamVolumeDB(3, i4, i);
            if (Float.isNaN(streamVolumeDB)) {
                break;
            }
            float f = this.mSafeMediaVolumeDbfs;
            if (streamVolumeDB == f) {
                break;
            }
            if (streamVolumeDB < f) {
                i2 = i4;
            } else {
                i3 = i4;
            }
        }
        return this.mContext.getResources().getInteger(R.integer.leanback_setup_alpha_forward_in_content_delay) * 10;
    }

    private void onPersistSafeVolumeState(int i) {
        this.mSettings.putGlobalInt(this.mAudioService.getContentResolver(), "audio_safe_volume_state", i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mCsdStateLock"})
    public void updateSoundDoseRecords_l(SoundDoseRecord[] soundDoseRecordArr, float f) {
        long j = 0;
        for (final SoundDoseRecord soundDoseRecord : soundDoseRecordArr) {
            Log.i(TAG, "  new record: " + soundDoseRecord);
            j += (long) soundDoseRecord.duration;
            if (soundDoseRecord.value < 0.0f) {
                if (!this.mDoseRecords.removeIf(new Predicate() { // from class: com.android.server.audio.SoundDoseHelper$$ExternalSyntheticLambda4
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$updateSoundDoseRecords_l$1;
                        lambda$updateSoundDoseRecords_l$1 = SoundDoseHelper.lambda$updateSoundDoseRecords_l$1(soundDoseRecord, (SoundDoseRecord) obj);
                        return lambda$updateSoundDoseRecords_l$1;
                    }
                })) {
                    Log.w(TAG, "Could not find cached record to remove: " + soundDoseRecord);
                }
            } else {
                this.mDoseRecords.add(soundDoseRecord);
            }
        }
        AudioService.AudioHandler audioHandler = this.mAudioHandler;
        audioHandler.sendMessageAtTime(audioHandler.obtainMessage(1005, 0, 0, null), 0L);
        this.mLogger.enqueue(AudioServiceEvents.SoundDoseEvent.getDoseUpdateEvent(f, j));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateSoundDoseRecords_l$1(SoundDoseRecord soundDoseRecord, SoundDoseRecord soundDoseRecord2) {
        return soundDoseRecord2.value == (-soundDoseRecord.value) && soundDoseRecord2.timestamp == soundDoseRecord.timestamp && soundDoseRecord2.averageMel == soundDoseRecord.averageMel && soundDoseRecord2.duration == soundDoseRecord.duration;
    }

    private void onPersistSoundDoseRecords() {
        synchronized (this.mCsdStateLock) {
            if (this.mGlobalTimeOffsetInSecs == -1) {
                this.mGlobalTimeOffsetInSecs = System.currentTimeMillis() / 1000;
            }
            this.mSettings.putGlobalString(this.mAudioService.getContentResolver(), "audio_safe_csd_current_value", Float.toString(this.mCurrentCsd));
            this.mSettings.putGlobalString(this.mAudioService.getContentResolver(), "audio_safe_csd_next_warning", Float.toString(this.mNextCsdWarning));
            this.mSettings.putGlobalString(this.mAudioService.getContentResolver(), "audio_safe_csd_dose_records", (String) this.mDoseRecords.stream().map(new Function() { // from class: com.android.server.audio.SoundDoseHelper$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    String lambda$onPersistSoundDoseRecords$2;
                    lambda$onPersistSoundDoseRecords$2 = SoundDoseHelper.this.lambda$onPersistSoundDoseRecords$2((SoundDoseRecord) obj);
                    return lambda$onPersistSoundDoseRecords$2;
                }
            }).collect(Collectors.joining(PERSIST_CSD_RECORD_SEPARATOR_CHAR)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$onPersistSoundDoseRecords$2(SoundDoseRecord soundDoseRecord) {
        return recordToPersistedString(soundDoseRecord, this.mGlobalTimeOffsetInSecs);
    }

    private static String recordToPersistedString(SoundDoseRecord soundDoseRecord, long j) {
        return convertToGlobalTime(soundDoseRecord.timestamp, j) + PERSIST_CSD_RECORD_FIELD_SEPARATOR + soundDoseRecord.duration + PERSIST_CSD_RECORD_FIELD_SEPARATOR + soundDoseRecord.value + PERSIST_CSD_RECORD_FIELD_SEPARATOR + soundDoseRecord.averageMel;
    }

    private static List<SoundDoseRecord> persistedStringToRecordList(String str, final long j) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return (List) Arrays.stream(TextUtils.split(str, PERSIST_CSD_RECORD_SEPARATOR)).map(new Function() { // from class: com.android.server.audio.SoundDoseHelper$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                SoundDoseRecord lambda$persistedStringToRecordList$3;
                lambda$persistedStringToRecordList$3 = SoundDoseHelper.lambda$persistedStringToRecordList$3(j, (String) obj);
                return lambda$persistedStringToRecordList$3;
            }
        }).filter(new Predicate() { // from class: com.android.server.audio.SoundDoseHelper$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.nonNull((SoundDoseRecord) obj);
            }
        }).collect(Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ SoundDoseRecord lambda$persistedStringToRecordList$3(long j, String str) {
        return persistedStringToRecord(str, j);
    }

    private static SoundDoseRecord persistedStringToRecord(String str, long j) {
        if (str != null && !str.isEmpty()) {
            String[] split = TextUtils.split(str, PERSIST_CSD_RECORD_FIELD_SEPARATOR);
            if (split.length != 4) {
                Log.w(TAG, "Expecting 4 fields for a SoundDoseRecord, parsed " + split.length);
                return null;
            }
            SoundDoseRecord soundDoseRecord = new SoundDoseRecord();
            try {
                soundDoseRecord.timestamp = convertToBootTime(Long.parseLong(split[0]), j);
                soundDoseRecord.duration = Integer.parseInt(split[1]);
                soundDoseRecord.value = Float.parseFloat(split[2]);
                soundDoseRecord.averageMel = Float.parseFloat(split[3]);
                return soundDoseRecord;
            } catch (NumberFormatException e) {
                Log.e(TAG, "Unable to parse persisted SoundDoseRecord: " + str, e);
            }
        }
        return null;
    }

    private float parseGlobalSettingFloat(String str, float f) {
        String globalString = this.mSettings.getGlobalString(this.mAudioService.getContentResolver(), str);
        if (globalString == null || globalString.isEmpty()) {
            Log.v(TAG, "No value stored in settings " + str);
            return f;
        }
        try {
            return Float.parseFloat(globalString);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing float from settings " + str, e);
            return f;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class StreamVolumeCommand {
        public final int mDevice;
        public final int mFlags;
        public final int mIndex;
        public final int mStreamType;

        StreamVolumeCommand(int i, int i2, int i3, int i4) {
            this.mStreamType = i;
            this.mIndex = i2;
            this.mFlags = i3;
            this.mDevice = i4;
        }

        public String toString() {
            return "{streamType=" + this.mStreamType + ",index=" + this.mIndex + ",flags=" + this.mFlags + ",device=" + this.mDevice + '}';
        }
    }

    public ISoundDoseHelperWrapper getWrapper() {
        return this.mSdhWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class SoundDoseHelperWrapper implements ISoundDoseHelperWrapper {
        private SoundDoseHelperWrapper() {
        }

        @Override // com.android.server.audio.ISoundDoseHelperWrapper
        public int getSafeMediaVolumeState() {
            int i;
            synchronized (SoundDoseHelper.this.mSafeMediaVolumeStateLock) {
                i = SoundDoseHelper.this.mSafeMediaVolumeState;
            }
            return i;
        }

        @Override // com.android.server.audio.ISoundDoseHelperWrapper
        public void setSafeMediaVolumeState(int i) {
            synchronized (SoundDoseHelper.this.mSafeMediaVolumeStateLock) {
                SoundDoseHelper.this.mSafeMediaVolumeState = i;
            }
        }

        @Override // com.android.server.audio.ISoundDoseHelperWrapper
        public void setMusicActiveMs(int i) {
            synchronized (SoundDoseHelper.this.mSafeMediaVolumeStateLock) {
                SoundDoseHelper.this.mMusicActiveMs = i;
            }
        }

        @Override // com.android.server.audio.ISoundDoseHelperWrapper
        public void saveMusicActiveMs() {
            SoundDoseHelper.this.saveMusicActiveMs();
        }

        @Override // com.android.server.audio.ISoundDoseHelperWrapper
        public void checkMusicActive() {
            SoundDoseHelper.this.scheduleMusicActiveCheck();
        }
    }
}
