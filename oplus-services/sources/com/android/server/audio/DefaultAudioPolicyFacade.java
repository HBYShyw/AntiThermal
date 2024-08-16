package com.android.server.audio;

import android.media.IAudioPolicyService;
import android.media.permission.ClearCallingIdentityContext;
import android.media.permission.SafeCloseable;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DefaultAudioPolicyFacade implements AudioPolicyFacade, IBinder.DeathRecipient {
    private static final String AUDIO_POLICY_SERVICE_NAME = "media.audio_policy";
    private static final String TAG = "DefaultAudioPolicyFacade";

    @GuardedBy({"mServiceLock"})
    private IAudioPolicyService mAudioPolicy;
    private final Object mServiceLock = new Object();

    public DefaultAudioPolicyFacade() {
        try {
            getAudioPolicyOrInit();
        } catch (IllegalStateException e) {
            Log.e(TAG, "Failed to initialize APM connection", e);
        }
    }

    @Override // com.android.server.audio.AudioPolicyFacade
    public boolean isHotwordStreamSupported(boolean z) {
        IAudioPolicyService audioPolicyOrInit = getAudioPolicyOrInit();
        try {
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                boolean isHotwordStreamSupported = audioPolicyOrInit.isHotwordStreamSupported(z);
                if (create != null) {
                    create.close();
                }
                return isHotwordStreamSupported;
            } finally {
            }
        } catch (RemoteException e) {
            resetServiceConnection(audioPolicyOrInit.asBinder());
            throw new IllegalStateException(e);
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        Log.wtf(TAG, "Unexpected binderDied without IBinder object");
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied(IBinder iBinder) {
        resetServiceConnection(iBinder);
    }

    private void resetServiceConnection(IBinder iBinder) {
        synchronized (this.mServiceLock) {
            IAudioPolicyService iAudioPolicyService = this.mAudioPolicy;
            if (iAudioPolicyService != null && iAudioPolicyService.asBinder().equals(iBinder)) {
                this.mAudioPolicy.asBinder().unlinkToDeath(this, 0);
                this.mAudioPolicy = null;
            }
        }
    }

    private IAudioPolicyService getAudioPolicy() {
        IAudioPolicyService iAudioPolicyService;
        synchronized (this.mServiceLock) {
            iAudioPolicyService = this.mAudioPolicy;
        }
        return iAudioPolicyService;
    }

    private IAudioPolicyService getAudioPolicyOrInit() {
        synchronized (this.mServiceLock) {
            IAudioPolicyService iAudioPolicyService = this.mAudioPolicy;
            if (iAudioPolicyService != null) {
                return iAudioPolicyService;
            }
            IAudioPolicyService asInterface = IAudioPolicyService.Stub.asInterface(ServiceManager.checkService(AUDIO_POLICY_SERVICE_NAME));
            if (asInterface == null) {
                throw new IllegalStateException("DefaultAudioPolicyFacade: Unable to connect to AudioPolicy");
            }
            try {
                asInterface.asBinder().linkToDeath(this, 0);
                this.mAudioPolicy = asInterface;
                return asInterface;
            } catch (RemoteException e) {
                throw new IllegalStateException("DefaultAudioPolicyFacade: Unable to link deathListener to AudioPolicy", e);
            }
        }
    }
}
