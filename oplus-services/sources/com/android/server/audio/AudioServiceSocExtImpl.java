package com.android.server.audio;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioDeviceAttributes;
import android.media.AudioDeviceInfo;
import android.os.IBinder;
import android.os.Message;
import com.mediatek.server.MtkSystemServiceFactory;
import com.mediatek.server.audio.AudioServiceExt;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AudioServiceSocExtImpl implements IAudioServiceSocExt {
    private static final String TAG = "AudioServiceSocExtImpl";
    private AudioService mAudioService;
    private AudioServiceExt mAudioServiceExt;
    private final Object mAudioServiceExtLock = new Object();

    public AudioServiceSocExtImpl(Object obj) {
        this.mAudioService = (AudioService) obj;
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void onSystemReadyExt() {
        getAudioServiceExtInstance().onSystemReadyExt();
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void initAudioServiceExtInstance() {
        getAudioServiceExtInstance();
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void getBleIntentFilters(IntentFilter intentFilter) {
        getAudioServiceExtInstance().getBleIntentFilters(intentFilter);
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public boolean setCommunicationDeviceExt(IBinder iBinder, int i, AudioDeviceInfo audioDeviceInfo, String str) {
        return getAudioServiceExtInstance().setCommunicationDeviceExt(iBinder, i, audioDeviceInfo, str);
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void setBluetoothLeCgOn(boolean z) {
        getAudioServiceExtInstance().setBluetoothLeCgOn(z);
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public boolean isBluetoothLeTbsDeviceActive() {
        return getAudioServiceExtInstance().isBluetoothLeTbsDeviceActive();
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public boolean isBluetoothLeCgOn() {
        return getAudioServiceExtInstance().isBluetoothLeCgOn();
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void startBluetoothLeCg(int i, int i2, int i3, IBinder iBinder) {
        getAudioServiceExtInstance().startBluetoothLeCg(i, i2, i3, iBinder);
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void startBluetoothLeCg(IBinder iBinder, int i) {
        getAudioServiceExtInstance().startBluetoothLeCg(iBinder, i);
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public boolean stopBluetoothLeCg(IBinder iBinder) {
        return getAudioServiceExtInstance().stopBluetoothLeCg(iBinder);
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void stopBluetoothLeCgLater(IBinder iBinder) {
        getAudioServiceExtInstance().stopBluetoothLeCgLater(iBinder);
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void onReceiveExt(Context context, Intent intent) {
        getAudioServiceExtInstance().onReceiveExt(context, intent);
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public boolean isBleAudioFeatureSupported() {
        return getAudioServiceExtInstance().isBleAudioFeatureSupported();
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void handleMessageExt(Message message) {
        getAudioServiceExtInstance().handleMessageExt(message);
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public AudioDeviceAttributes preferredCommunicationDevice() {
        return getAudioServiceExtInstance().preferredCommunicationDevice();
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void restartScoInVoipCall() {
        getAudioServiceExtInstance().restartScoInVoipCall();
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void setPreferredDeviceForHfpInbandRinging(int i, int i2, int i3, IBinder iBinder, boolean z) {
        getAudioServiceExtInstance().setPreferredDeviceForHfpInbandRinging(i, i2, i3, iBinder, z);
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void startBluetoothLeCgForRecord(IBinder iBinder, int i, int i2) {
        getAudioServiceExtInstance().startBluetoothLeCgForRecord(iBinder, i, i2);
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public boolean stopBluetoothLeCgForRecord(IBinder iBinder, int i) {
        return getAudioServiceExtInstance().stopBluetoothLeCgForRecord(iBinder, i);
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public IBinder getModeCb() {
        return getAudioServiceExtInstance().getModeCb();
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void restartBleRecord() {
        getAudioServiceExtInstance().restartBleRecord();
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public AudioDeviceAttributes getLeAudioDevice() {
        return getAudioServiceExtInstance().getLeAudioDevice();
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public boolean isBluetoothLeCgStateOn() {
        return getAudioServiceExtInstance().isBluetoothLeCgStateOn();
    }

    public AudioServiceExt getAudioServiceExtInstance() {
        synchronized (this.mAudioServiceExtLock) {
            if (this.mAudioServiceExt == null) {
                this.mAudioServiceExt = MtkSystemServiceFactory.getInstance().makeAudioServiceExt();
            }
            if (!this.mAudioServiceExt.isSystemReady() && this.mAudioService.getWrapper().getDeviceBroker() != null) {
                AudioServiceExt audioServiceExt = this.mAudioServiceExt;
                AudioService audioService = this.mAudioService;
                audioServiceExt.init(audioService.mContext, audioService, audioService.getWrapper().getAudioSystem(), this.mAudioService.getWrapper().getSystemServer(), this.mAudioService.getWrapper().getDeviceBroker());
            }
        }
        return this.mAudioServiceExt;
    }
}
