package com.android.server.audio;

import android.content.ContentResolver;
import android.media.AudioDeviceAttributes;
import android.media.VolumePolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAudioServiceWrapper {
    default void avrcpSupportsAbsoluteVolume(String str, boolean z) {
    }

    default void broadcastSafeVolume() {
    }

    default void checkCarkitBtAndSetValue(boolean z) {
    }

    default boolean getAudioModeOwnerHandlerCheck() {
        return false;
    }

    default AudioSystemAdapter getAudioSystem() {
        return null;
    }

    default boolean getAvrcpAbsVolSupported() {
        return false;
    }

    default ContentResolver getContentResolver() {
        return null;
    }

    default Object getCurrentAudioModeOwnerCb() {
        return null;
    }

    default int getCurrentAudioModeOwnerPid() {
        return -1;
    }

    default int getCurrentAudioModeOwnerUid() {
        return -1;
    }

    default int getDesiredHeadTrackingMode() {
        return 0;
    }

    default AudioDeviceBroker getDeviceBroker() {
        return null;
    }

    default int getDeviceForStream(int i) {
        return 0;
    }

    default int getFlagAdjustVolume() {
        return 0;
    }

    default boolean getHasVibrator() {
        return false;
    }

    default int getIndex(int i, int i2) {
        return 0;
    }

    default int[] getMaxStreamVolume() {
        return null;
    }

    default MediaFocusControl getMediaFocusControl() {
        return null;
    }

    default int getMediaVolumeIndexByDevice(int i) {
        return 0;
    }

    default boolean getMicMuteFromApi() {
        return false;
    }

    default int[] getMinStreamVolume() {
        return null;
    }

    default int getModeOfSetModeDeathHandler(int i) {
        return 0;
    }

    default int getPidOfSetModeDeathHandler(int i) {
        return 0;
    }

    default int getPlatformType() {
        return 0;
    }

    default PlaybackActivityMonitor getPlaybackMonitor() {
        return null;
    }

    default RecordingActivityMonitor getRecordMonitor() {
        return null;
    }

    default int getRingerModeInternal() {
        return 0;
    }

    default int getSetModeDeathHandlersLength() {
        return 0;
    }

    default SoundDoseHelper getSoundDoseHelper() {
        return null;
    }

    default int getStreamMaxIndex(int i) {
        return 0;
    }

    default boolean getStreamMuteState(int i) {
        return false;
    }

    default int[] getStreamVolumeAlias() {
        return null;
    }

    default SystemServerAdapter getSystemServer() {
        return null;
    }

    default String getVolumeIndexSettingNameForIsMutedForStream(int i) {
        return null;
    }

    default VolumePolicy getVolumePolicy() {
        return null;
    }

    default boolean mutePhoneForStream(int i, boolean z) {
        return false;
    }

    default void oplusSendMsg(int i, int i2, int i3, int i4, Object obj, int i5) {
    }

    default void sendMessage(int i, int i2, int i3, int i4, Object obj, int i5) {
    }

    default void sendMsgForStream(int i, int i2, int i3, int i4, int i5, int i6) {
    }

    default void setDebugLog(boolean z) {
    }

    default void setDesiredHeadTrackingMode(int i) {
    }

    default void setHeadTrackerEnabled(boolean z, AudioDeviceAttributes audioDeviceAttributes) {
    }

    default boolean setIndexForStream(int i, int i2, int i3, String str, boolean z) {
        return false;
    }

    default void setPrevVolDirection(int i) {
    }

    default void setRingerMode(int i, String str, boolean z) {
    }

    default void setSpatializerEnabled(boolean z) {
    }

    default void setStreamVolumeInt(int i, int i2, int i3, boolean z, String str, boolean z2) {
    }

    default void updateDeviceChangeForMusic(int i, int i2) {
    }

    default IAudioServiceExt getExtImpl() {
        return new IAudioServiceExt() { // from class: com.android.server.audio.IAudioServiceWrapper.1
        };
    }
}
