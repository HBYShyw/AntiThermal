package com.android.server.audio;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioDeviceAttributes;
import android.media.AudioPlaybackConfiguration;
import android.media.IAudioModeDispatcher;
import android.os.IBinder;
import android.view.KeyEvent;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAudioServiceExt {
    public static final String ACTION_ATLAS_STREAM_MUTE_STATE = "atlas_stream_mute_state";
    public static final String ACTION_AUDIO_DEVICE_ROUTE_CHANGED = "android.media.ACTION_AUDIO_DEVICE_ROUTE_CHANGED";
    public static final String ALERT_SLIDER_MANAGER = "AlertSliderManager";
    public static final String AUDIO_INPUT_CHANNEL = "oplus_customize_sound_input_channel";
    public static final int CHECK_INACTIVE_ROUTE_CLIENT = 1;
    public static final int CHECK_MICROPHONE_MUTE_DELAY = 6000;
    public static final int CHECK_MODE_CHANGE_FOR_ROUTE = 0;
    public static final int CHECK_ROUTE_FOR_MODE_CHANGE = 0;
    public static final int CHECK_ROUTING_CLIENT_ACTIVE_DELAY = 6000;
    public static final int CHECK_SELECT_ROUTE_CLIENT = 1;
    public static final int CHECK_TIMEOUT_INACTIVE_ROUTE_CLIENT = 2;
    public static final String EXTRA_ATLAS_STREAM_MUTE_STATE = "ATLAS_STREAM_MUTE_STATE";
    public static final String GET_DEVICE_TYPE_FROM_ATLAS = "android.media.EXTRA_DEVICE_TYPE";
    public static final int KEY_VOICE_CALL_NC_STATE_CLOSE = 0;
    public static final int MSG_CHECK_INACTIVE_ROUTE_CLIENT = 82;
    public static final int MSG_CHECK_MICROPHONE_MUTE = 83;
    public static final int MSG_MUTE_PHONE = 62;
    public static final int MSG_PERSIST_SUPPER_VOLUME = 64;
    public static final int MSG_PER_SPEAKER_MUSIC_VOLUME = 63;
    public static final int MSG_VOLUME_FADE = 61;
    public static final String OPLUS_MODE_RINGER = "oplus_mode_ringer";
    public static final String PHONE_MUTE_STATE_FOR_CUSTOM = "phone_mute_state_for_custom";
    public static final int RECOVER_ROUTE_FOR_SCO = 0;

    default void addRouteClientActiveState(int i, int i2) {
    }

    default void audioDebugLogInit() {
    }

    default void audioRouteEventCheckForModeChange(int i, int i2, String str) {
    }

    default boolean canHeadsetFadeIn(int i, int i2) {
        return false;
    }

    default boolean cancelBroadcastVolumeChange(int i, int i2, String str) {
        return false;
    }

    default void checkAndClearMicMuteEvent(int i) {
    }

    default void checkSafeAndSetAvrcpAbsoluteVolume(int i, int i2, int i3, int i4, int i5, int i6) {
    }

    default void checkTimeoutInactiveRouteClient() {
    }

    default void clearBluetoothScoClientAfterPhonebypid(int i, String str) {
    }

    default void clearRouteSettingCheck(int i, int i2, int i3, Object obj) {
    }

    default void createAlerSliderManager() {
    }

    default int getA2dpVolume(boolean z, int i) {
        return 0;
    }

    default boolean getAvrcpSupportsAbsoluteVolume() {
        return false;
    }

    default boolean getBleRingPlaybackActive() {
        return false;
    }

    default int getBleVolume(boolean z, int i) {
        return 0;
    }

    default boolean getBluetoothVolSyncSupported() {
        return false;
    }

    default boolean getDialogOn() {
        return false;
    }

    default int getLatestModeOwnerPid() {
        return 0;
    }

    default int getLatestPreferredDeviceType() {
        return 0;
    }

    default boolean getMuteStateForUser(int i) {
        return false;
    }

    default int getOplusA2dpSmallVolumeIndex(int i, int i2) {
        return 0;
    }

    default int getOplusPlatformType(Context context) {
        return 0;
    }

    default boolean getOplusSpatializerSupported() {
        return false;
    }

    default int getOplusStreamVolume(int i, int i2, int i3) {
        return 0;
    }

    default boolean getPrivacyCallSupport() {
        return false;
    }

    default boolean getSpatializerSpeakerSupported() {
        return false;
    }

    default int getUidByPid(int i) {
        return -1;
    }

    default boolean getVocalProminenceSupport() {
        return false;
    }

    default boolean handleVolumeKey(KeyEvent keyEvent, String str) {
        return false;
    }

    default void init(Context context) {
    }

    default void initDeviceBroker() {
    }

    default boolean isAdjustVolumeForbidden(int i, String str) {
        return false;
    }

    default boolean isAlertSliderSupported() {
        return false;
    }

    default boolean isAudioRouteSupported() {
        return false;
    }

    default boolean isBleDeviceCommunicationDevice() {
        return false;
    }

    default boolean isCarkitBt() {
        return false;
    }

    default boolean isNeedGetOplusStreamVolume(int i, int i2) {
        return false;
    }

    default int isNeedShowUiWarnings(int i, String str) {
        return 0;
    }

    default boolean isOplusA2dpSmallVolumeCusEnable(int i) {
        return false;
    }

    default boolean isPhoneCallIdle() {
        return false;
    }

    default boolean isRingVolumeDefault() {
        return false;
    }

    default boolean isRingerModeContorlbyAlerSlider(int i, String str, int i2, boolean z) {
        return false;
    }

    default boolean isSpeakerA2dpDevice(int i) {
        return false;
    }

    default boolean isStreamSuperVolumeOn(int i) {
        return false;
    }

    default boolean isSuperVolumeApp(String str) {
        return false;
    }

    default boolean isSuperVolumeSupported() {
        return false;
    }

    default boolean isSuperVolumeSupported(int i, int i2) {
        return false;
    }

    default boolean isTriggerByVolumeKey() {
        return false;
    }

    default boolean manageRouteSettings(int i, int i2, boolean z) {
        return true;
    }

    default boolean needJudgeScoActualy(int i) {
        return false;
    }

    default void notifyAdjustVolumeUpdate(int i, String str) {
    }

    default void notifyAtlasServiceModesUpdate(boolean z) {
    }

    default void notifyAtlasServiceRingerModeUpdate(String str, int i) {
    }

    default void onIPDeviceConnectionChange(AudioDeviceAttributes audioDeviceAttributes, int i, String str, IBinder iBinder) {
    }

    default void onOplusRestoreVolumeBeforeSafeMediaVolume() {
    }

    default void onStorePreMediaVolume(int i) {
    }

    default int oplusCheckForRingerModeChangeHelper(int i, int i2, int i3, String str, int i4, NotificationManager notificationManager) {
        return 0;
    }

    default void oplusCheckNcAudioRoute() {
    }

    default void oplusCheckPrivacyCall(int i, int i2, int i3) {
    }

    default void oplusCheckVocalProminence(int i, int i2, int i3) {
    }

    default void oplusClosePrivacyCall() {
    }

    default int oplusGetNewRingMode(int i) {
        return 0;
    }

    default String oplusGetParameters(String str, int i) {
        return null;
    }

    default void oplusMediaVolumeUpdateNotifyDolby(int i, int i2, int i3) {
    }

    default boolean oplusReadCameraSoundForced() {
        return false;
    }

    default void oplusRegisterModeDispatcher(IAudioModeDispatcher iAudioModeDispatcher) {
    }

    default void oplusSetParameters(String str, int i) {
    }

    default boolean oplusSetStreamVolumePermission(int i, int i2) {
        return false;
    }

    default void oplusSetVocalProminence(int i, boolean z) {
    }

    default void persistSuperVolume(int i, String str) {
    }

    default void preDispatchMode(int i) {
    }

    default void putMuteStateForUser(int i, boolean z) {
    }

    default int reAdjustDirectionbyAlerSlider(int i, int i2, int i3, String str) {
        return 0;
    }

    default void readAbsA2dpVolume() {
    }

    default void readPersistSuperVolume() {
    }

    default int readRingModeSetting(int i, ContentResolver contentResolver) {
        return 0;
    }

    default void recordMicMuteEventInfo(boolean z, String str, int i, int i2) {
    }

    default void recoverRouteSetting(int i, int i2, int i3, Object obj) {
    }

    default void registerTelePhony() {
    }

    default boolean rejectBluetoothSco(int i, int i2) {
        return false;
    }

    default void removeRouteClientActiveState(int i) {
    }

    default int rescaleIndexByAlertSlider(int i, int i2, String str) {
        return 0;
    }

    default void resetSystemVolume() {
    }

    default void routeCheckForModeChange(int i, int i2, Object obj) {
    }

    default boolean selectRouteSetting(int i, int i2, int i3, Object obj) {
        return false;
    }

    default void setAtlasMusicMuteState(boolean z) {
    }

    default void setAvrcpAbsoluteVolumeIndexDelay(int i, int i2) {
    }

    default void setAvrcpSupportsAbsoluteVolume(boolean z) {
    }

    default void setBleForceStream(int i) {
    }

    default void setBleRingPlaybackActive(List<AudioPlaybackConfiguration> list) {
    }

    default void setBluetoothScoSpecialPid(String str, int i) {
    }

    default void setMusicDeviceVolumeStateDelay(int i, int i2, int i3) {
    }

    default void setNcPkgName(String str) {
    }

    default void setSpatializerSpeakerState(boolean z) {
    }

    default void setStreamVolumeForUser() {
    }

    default void setSuperVolume(boolean z, int i, int i2) {
    }

    default void storePreMediaVolume(int i, int i2, String str) {
    }

    default void unregisterModeDispatcher(IAudioModeDispatcher iAudioModeDispatcher) {
    }

    default void updateAllSuperVolumeToPolicy() {
    }

    default void updateDeviceChangeForMusic(int i, int i2) {
    }

    default void updateInputDevice(ContentResolver contentResolver) {
    }

    default void updateModeOwnerInfo(int i, int i2, int i3) {
    }

    default void updatePreferredCommunicationDevice(AudioDeviceAttributes audioDeviceAttributes) {
    }
}
