package com.android.server.audio;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioDeviceAttributes;
import android.media.AudioDeviceInfo;
import android.os.IBinder;
import android.os.Message;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAudioServiceSocExt {
    default void getBleIntentFilters(IntentFilter intentFilter) {
    }

    default AudioDeviceAttributes getLeAudioDevice() {
        return null;
    }

    default IBinder getModeCb() {
        return null;
    }

    default void handleMessageExt(Message message) {
    }

    default void initAudioServiceExtInstance() {
    }

    default boolean isBleAudioFeatureSupported() {
        return false;
    }

    default boolean isBluetoothLeCgOn() {
        return false;
    }

    default boolean isBluetoothLeCgStateOn() {
        return false;
    }

    default boolean isBluetoothLeTbsDeviceActive() {
        return false;
    }

    default void onReceiveExt(Context context, Intent intent) {
    }

    default void onSystemReadyExt() {
    }

    default AudioDeviceAttributes preferredCommunicationDevice() {
        return null;
    }

    default void restartBleRecord() {
    }

    default void restartScoInVoipCall() {
    }

    default void setBluetoothLeCgOn(boolean z) {
    }

    default boolean setCommunicationDeviceExt(IBinder iBinder, int i, AudioDeviceInfo audioDeviceInfo, String str) {
        return false;
    }

    default void setPreferredDeviceForHfpInbandRinging(int i, int i2, int i3, IBinder iBinder, boolean z) {
    }

    default void startBluetoothLeCg(int i, int i2, int i3, IBinder iBinder) {
    }

    default void startBluetoothLeCg(IBinder iBinder, int i) {
    }

    default void startBluetoothLeCgForRecord(IBinder iBinder, int i, int i2) {
    }

    default boolean stopBluetoothLeCg(IBinder iBinder) {
        return false;
    }

    default boolean stopBluetoothLeCgForRecord(IBinder iBinder, int i) {
        return false;
    }

    default void stopBluetoothLeCgLater(IBinder iBinder) {
    }
}
