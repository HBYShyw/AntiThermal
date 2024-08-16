package com.mediatek.server.audio;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioDeviceAttributes;
import android.media.AudioDeviceInfo;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import com.android.server.audio.AudioService;
import com.android.server.audio.AudioSystemAdapter;
import com.android.server.audio.SystemServerAdapter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class AudioServiceExt {
    protected static final boolean LOGD;
    private static final String TAG = "AS.AudioServiceExt";

    static {
        String str = Build.TYPE;
        LOGD = "eng".equals(str) || "userdebug".equals(str);
    }

    private void logd(String str) {
        if (LOGD) {
            Log.d(TAG, str);
        }
    }

    public void init(Context context, AudioService audioService, AudioSystemAdapter audioSystemAdapter, SystemServerAdapter systemServerAdapter, Object obj) {
        logd("[Default] setBluetoothLeAudioDeviceConnectionState()");
    }

    public void setBluetoothLeAudioDeviceConnectionState(BluetoothDevice bluetoothDevice, int i, boolean z, int i2) {
        logd("[Default] setBluetoothLeAudioDeviceConnectionState()");
    }

    public void leVcSupportsAbsoluteVolume(String str, boolean z) {
        logd("[Default] leVcSupportsAbsoluteVolume() false");
    }

    public boolean isBluetoothLeOn() {
        logd("[Default] isBluetoothLeOn() false");
        return false;
    }

    public boolean isBleAudioFeatureSupported() {
        logd("[Default] isBleAudioFeatureSupported() false");
        return false;
    }

    public void handleMessageExt(Message message) {
        Log.wtf(TAG, "Invalid message " + message.what);
    }

    public void onReceiveExt(Context context, Intent intent) {
        logd("[Default] onReceiveExt");
    }

    public void postSetLeCgVcIndex(int i) {
        logd("[Default] postSetLeCgVcIndex, index=" + i);
    }

    public void onSystemReadyExt() {
        logd("[Default] onSystemReadyExt");
    }

    public void getBleIntentFilters(IntentFilter intentFilter) {
        logd("[Default] getBleIntentFilters");
    }

    public boolean isBluetoothLeCgOn() {
        logd("[Default] isBluetoothLeCgOn");
        return false;
    }

    public boolean isBluetoothLeCgStateOn() {
        logd("[Default] isBluetoothLeCgStateOn");
        return false;
    }

    public boolean isBluetoothLeTbsDeviceActive() {
        logd("[Default] isBluetoothLeTbsDeviceActive()");
        return false;
    }

    public AudioDeviceAttributes preferredCommunicationDevice() {
        logd("[Default] preferredCommunicationDevice()");
        return null;
    }

    public AudioDeviceAttributes getLeAudioDevice() {
        logd("[Default] getLeAudioDevice()");
        return null;
    }

    public void startBluetoothLeCg(IBinder iBinder, int i) {
        logd("[Default] startBluetoothLeCg()");
    }

    public void startBluetoothLeCg(int i, int i2, int i3, IBinder iBinder) {
        logd("[Default] startBluetoothLeCg()");
    }

    public boolean stopBluetoothLeCg(IBinder iBinder) {
        logd("[Default] stopBluetoothLeCg()");
        return false;
    }

    public void stopBluetoothLeCgLater(IBinder iBinder) {
        logd("[Default] stopBluetoothLeCgLater()");
    }

    public void startBluetoothLeCgVirtualCall(IBinder iBinder) {
        logd("[Default] startBluetoothLeCgVirtualCall()");
    }

    public boolean isBluetoothLeCgActive() {
        logd("[Default] isBluetoothLeCgActive() false");
        return false;
    }

    public void setBluetoothLeCgOn(boolean z) {
        logd("[Default] setBluetoothLeCgOn() false");
    }

    public boolean isSystemReady() {
        logd("[Default] isSystemReady() false");
        return false;
    }

    public int getBleCgVolume() {
        logd("[Default] getBleCgVolume()");
        return 0;
    }

    public boolean setCommunicationDeviceExt(IBinder iBinder, int i, AudioDeviceInfo audioDeviceInfo, String str) {
        logd("[Default] setCommunicationDeviceExt()");
        return false;
    }

    public void onUpdateAudioModeExt(int i, int i2, IBinder iBinder) {
        logd("[Default] setCommunicationDevice()");
    }

    public void restartScoInVoipCall() {
        logd("[Default] restartScoInVoipCall()");
    }

    public void setPreferredDeviceForHfpInbandRinging(int i, int i2, int i3, IBinder iBinder, boolean z) {
        logd("[Default] setPreferredDevicesForHfpInbandRinging()");
    }

    public void startBluetoothLeCgForRecord(IBinder iBinder, int i, int i2) {
        logd("[Default] startBluetoothLeCgForRecord()");
    }

    public IBinder getModeCb() {
        logd("[Default] getModeCb()");
        return null;
    }

    public boolean stopBluetoothLeCgForRecord(IBinder iBinder, int i) {
        logd("[Default] stopBluetoothLeCgForRecord()");
        return false;
    }

    public void restartBleRecord() {
        logd("[Default] restartBleRecord()");
    }
}
