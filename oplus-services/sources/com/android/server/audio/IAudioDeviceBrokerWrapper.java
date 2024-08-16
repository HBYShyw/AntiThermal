package com.android.server.audio;

import android.bluetooth.BluetoothDevice;
import android.media.AudioDeviceAttributes;
import android.os.IBinder;
import android.os.Looper;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAudioDeviceBrokerWrapper {
    default void checkBtDeviceForCarkitBt(boolean z, boolean z2) {
    }

    default void checkBuildRouteForSco(int i, Object obj) {
    }

    default void checkClearSpeakerDevice(int i) {
    }

    default void checkTimeoutInactiveRouteClient() {
    }

    default void clearRedundancyClient(int i, IBinder iBinder) {
    }

    default int getA2dpVolume(boolean z, int i) {
        return 0;
    }

    default int getBleVolume(boolean z, int i) {
        return 0;
    }

    default BluetoothDevice getBluetoothDevice() {
        return null;
    }

    default boolean getBluetoothVolSyncSupported() {
        return false;
    }

    default Looper getBrokerLooper() {
        return null;
    }

    default AudioDeviceInventory getDeviceInventory() {
        return null;
    }

    default int getLatestModeOwnerPid() {
        return 0;
    }

    default int getLatestPreferredDeviceType() {
        return 0;
    }

    default int getSetAvrcpAbsVolMsg() {
        return 0;
    }

    default int getUidByPid(int i) {
        return -1;
    }

    default boolean isAudioRouteSupported() {
        return false;
    }

    default boolean isDeviceConnected(AudioDeviceAttributes audioDeviceAttributes) {
        return false;
    }

    default boolean isSpeakerA2dpDevice() {
        return false;
    }

    default void removeInactiveRouteClientForPid(int i) {
    }

    default void removeRouteClientForPid(int i) {
    }

    default void sendIILMsg(int i, int i2, int i3, int i4, Object obj, int i5) {
    }

    default void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
    }

    default void stopBluethoothScoToBT(String str) {
    }

    default IAudioDeviceBrokerExt getExtImpl() {
        return new IAudioDeviceBrokerExt() { // from class: com.android.server.audio.IAudioDeviceBrokerWrapper.1
        };
    }
}
