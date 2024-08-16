package com.itgsa.opensdk.mediaunit;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.oplus.karaoke.framework.IKaraokeServiceAidlInterface;

/* loaded from: classes.dex */
public class MediaUnitClientImpl implements MediaInterface {
    private static final String KARAOKE_SERVICE = "oplus.intent.action.FKARAOKE_SERVICE";
    private static final String PACKGE_NAME = "com.coloros.karaoke";
    private IBinder mBinder;
    private KaraokeMediaHelper mCallback;
    private Context mContext;
    private IKaraokeServiceAidlInterface mKaraokeService;
    private String mPackageName;
    private final String TAG = "MediaUnitClientImpl";
    private ServiceConnection mConnection = new ServiceConnection() { // from class: com.itgsa.opensdk.mediaunit.MediaUnitClientImpl.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            MediaUnitClientImpl.this.mKaraokeService = IKaraokeServiceAidlInterface.Stub.asInterface(service);
            MediaUnitClientImpl.this.mBinder = service;
            Log.d("MediaUnitClientImpl", "onServiceConnected " + service + ", " + MediaUnitClientImpl.this.mKaraokeService + ", mIsActive:" + MediaUnitClientImpl.this.mIsActive);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            if (MediaUnitClientImpl.this.mKaraokeService != null) {
                MediaUnitClientImpl.this.restartService();
            }
        }
    };
    private boolean mIsActive = false;

    public MediaUnitClientImpl(Context context) {
        this.mContext = context;
        this.mPackageName = context.getPackageName();
        bindKaraokeService();
    }

    @Override // com.itgsa.opensdk.mediaunit.MediaInterface
    public void openKTVDevice() {
        bindKaraokeService();
        resetActiveClient(this.mPackageName, false);
    }

    private void bindKaraokeService() {
        if (this.mContext == null) {
            Log.w("MediaUnitClientImpl", "context is null");
        } else if (this.mKaraokeService == null) {
            Intent serviceIntent = new Intent(KARAOKE_SERVICE);
            serviceIntent.setPackage(PACKGE_NAME);
            boolean res = this.mContext.bindService(serviceIntent, this.mConnection, 1);
            Log.i("MediaUnitClientImpl", "bindService " + (res ? "succeeded" : "failed"));
        }
    }

    @Override // com.itgsa.opensdk.mediaunit.MediaInterface
    public void closeKTVDevice() {
        if (this.mBinder != null) {
            resetActiveClient("", false);
            ServiceConnection serviceConnection = this.mConnection;
            if (serviceConnection != null) {
                this.mContext.unbindService(serviceConnection);
                this.mBinder = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restartService() {
        bindKaraokeService();
    }

    private void resetActiveClient(String pkgName, boolean isShowDialog) {
        Log.i("MediaUnitClientImpl", "resetActiveClient+++ " + pkgName + " isShowDialog = " + isShowDialog);
        String name = pkgName == null ? "null" : pkgName;
        try {
            Log.i("MediaUnitClientImpl", "resetActiveClient " + name);
            IKaraokeServiceAidlInterface iKaraokeServiceAidlInterface = this.mKaraokeService;
            if (iKaraokeServiceAidlInterface != null) {
                iKaraokeServiceAidlInterface.resetActiveClient(name, isShowDialog);
                Log.d("MediaUnitClientImpl", "resetActiveClient mKaraokeService");
            }
        } catch (RemoteException e) {
            Log.w("MediaUnitClientImpl", "resetActiveClient() could not link to " + this.mKaraokeService + " binder death");
        }
        this.mIsActive = pkgName != null;
        Log.i("MediaUnitClientImpl", "resetActiveClient--- " + this.mIsActive);
    }

    @Override // com.itgsa.opensdk.mediaunit.MediaInterface
    public void setListenRecordSame(int param) {
        if (param != 1 && param != 0) {
            Log.w("MediaUnitClientImpl", "setListenRecordSame param invalid !");
            return;
        }
        boolean isWetSound = param != 0;
        try {
            IKaraokeServiceAidlInterface iKaraokeServiceAidlInterface = this.mKaraokeService;
            if (iKaraokeServiceAidlInterface != null) {
                iKaraokeServiceAidlInterface.setRecordingWetSound(isWetSound);
            }
        } catch (RemoteException e) {
            Log.w("MediaUnitClientImpl", "setListenRecordSame() could not link to " + this.mKaraokeService + " binder death");
        }
    }

    @Override // com.itgsa.opensdk.mediaunit.MediaInterface
    public void setMixerSoundType(int param) {
        try {
            IKaraokeServiceAidlInterface iKaraokeServiceAidlInterface = this.mKaraokeService;
            if (iKaraokeServiceAidlInterface != null) {
                iKaraokeServiceAidlInterface.setMixSoundType(param);
            }
        } catch (RemoteException e) {
            Log.w("MediaUnitClientImpl", "setMixSoundType() could not link to " + this.mKaraokeService + " binder death");
        }
    }

    @Override // com.itgsa.opensdk.mediaunit.MediaInterface
    public void setPlayFeedbackParam(int param) {
        try {
            IKaraokeServiceAidlInterface iKaraokeServiceAidlInterface = this.mKaraokeService;
            if (iKaraokeServiceAidlInterface != null) {
                if (param == 1) {
                    iKaraokeServiceAidlInterface.setAudioLoopbackOn(true);
                } else if (param == 0) {
                    iKaraokeServiceAidlInterface.setAudioLoopbackOn(false);
                }
            }
        } catch (RemoteException e) {
            Log.w("MediaUnitClientImpl", "setAudioLoopbackOn() could not link to " + this.mKaraokeService + " binder death");
        }
    }

    @Override // com.itgsa.opensdk.mediaunit.MediaInterface
    public void setMicVolParam(int param) {
        try {
            IKaraokeServiceAidlInterface iKaraokeServiceAidlInterface = this.mKaraokeService;
            if (iKaraokeServiceAidlInterface != null) {
                iKaraokeServiceAidlInterface.setVolume(param);
            }
        } catch (RemoteException e) {
            Log.w("MediaUnitClientImpl", "setVolume() could not link to " + this.mKaraokeService + " binder death");
        }
    }

    @Override // com.itgsa.opensdk.mediaunit.MediaInterface
    public void setToneMode(int toneValue) {
        try {
            IKaraokeServiceAidlInterface iKaraokeServiceAidlInterface = this.mKaraokeService;
            if (iKaraokeServiceAidlInterface != null) {
                iKaraokeServiceAidlInterface.setTones(toneValue);
            }
        } catch (RemoteException e) {
            Log.w("MediaUnitClientImpl", "setTones() could not link to " + this.mKaraokeService + " binder death");
        }
    }

    @Override // com.itgsa.opensdk.mediaunit.MediaInterface
    public void setEqualizerType(int equalizerType) {
        try {
            IKaraokeServiceAidlInterface iKaraokeServiceAidlInterface = this.mKaraokeService;
            if (iKaraokeServiceAidlInterface != null) {
                iKaraokeServiceAidlInterface.setEqualizerType(equalizerType);
            }
        } catch (RemoteException e) {
            Log.w("MediaUnitClientImpl", "setEqualizerType() could not link to " + this.mKaraokeService + " binder death");
        }
    }
}
