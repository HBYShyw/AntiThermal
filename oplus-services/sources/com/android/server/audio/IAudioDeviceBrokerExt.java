package com.android.server.audio;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.media.AudioDeviceAttributes;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAudioDeviceBrokerExt {
    public static final long ACTION_CHECK_INTERVAL_TIME = 3000;
    public static final int ACTION_SCO_DEVICE_DISCONNECT = 2;
    public static final int ACTION_SET_MODE = 0;
    public static final int ACTION_SET_ROUTE = 1;
    public static final int EVENTID_IDLE_ROUTE_CLEAR_INFO = 20007;
    public static final int EVENTID_INCALL_ROUTE_EXCEPTION_INFO = 20006;
    public static final int MSG_SCO_DEVICE_DISCONNECT = 81;
    public static final String REMOVE_INACTIVE_ROUTE_CLIENT = "removeInactiveRouteClient";
    public static final int SENDMSG_QUEUE = 2;
    public static final String SET_BLUETOOTH_ACTIVE_DEVICE = "setBluetoothActiveDevice";

    default void addAudioRouteEventTrack(int i, int i2, int i3, int i4) {
    }

    default boolean checkPreviousDeviceIsConnected(BluetoothDevice bluetoothDevice, int i) {
        return false;
    }

    default AudioDeviceAttributes checkWhetherAnotherLeDevice(AudioDeviceAttributes audioDeviceAttributes) {
        return null;
    }

    default void clearAvrcpAbsoluteVolume() {
    }

    default void clearAvrcpAbsoluteVolumeSupportedwithAddr(String str) {
    }

    default void initAdbExtInner(Context context, AudioDeviceBroker audioDeviceBroker) {
    }

    default boolean isLeVcAbsoluteVolumeSupported() {
        return false;
    }

    default void oplusHeadsetFadeBeginFadeIn() {
    }

    default boolean oplusHeadsetFadeInit(int i) {
        return false;
    }

    default void oplusHeadsetFadeInstantiate(Context context, AudioService audioService) {
    }

    default void oplusHeadsetFadeSkipFadeIn(int i) {
    }

    default void reportIncallExceptionRouteInfo() {
    }

    default void reportRemovedInactiveRouteInfo(int i, int i2, long j) {
    }

    default void sendBtDeviceConnectedEvent(BluetoothDevice bluetoothDevice) {
    }

    default void setAvrcpAbsoluteVolumeSupportedwithAddr(String str, boolean z) {
    }

    default void setLeVcAbsoluteVolumeSupported(boolean z) {
    }
}
