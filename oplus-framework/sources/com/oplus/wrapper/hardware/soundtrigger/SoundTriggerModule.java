package com.oplus.wrapper.hardware.soundtrigger;

import android.media.soundtrigger_middleware.ISoundTriggerMiddlewareService;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.os.ServiceManager;
import android.util.Log;
import com.oplus.wrapper.hardware.soundtrigger.SoundTrigger;
import com.oplus.wrapper.media.permission.Identity;

/* loaded from: classes.dex */
public class SoundTriggerModule {
    private static final String TAG = "WrapperSoundTriggerModule";
    private static Object sSERVICELOCK = new Object();
    private final android.hardware.soundtrigger.SoundTriggerModule mSoundTriggerModule;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SoundTriggerModule(android.hardware.soundtrigger.SoundTriggerModule soundTriggerModule) {
        this.mSoundTriggerModule = soundTriggerModule;
    }

    public SoundTriggerModule(int moduleId, SoundTrigger.StatusListener listener, Looper looper, Identity middlemanIdentity, Identity originatorIdentity, boolean isTrusted) {
        SoundTrigger.StatusListenerImpl statusListener = new SoundTrigger.StatusListenerImpl(listener);
        middlemanIdentity.setPid(Binder.getCallingPid());
        middlemanIdentity.setUid(Binder.getCallingUid());
        this.mSoundTriggerModule = new android.hardware.soundtrigger.SoundTriggerModule(getService(), moduleId, statusListener, looper, middlemanIdentity.getIdentity(), originatorIdentity.getIdentity(), isTrusted);
    }

    private static ISoundTriggerMiddlewareService getService() {
        ISoundTriggerMiddlewareService asInterface;
        synchronized (sSERVICELOCK) {
            while (true) {
                try {
                    IBinder binder = ServiceManager.getServiceOrThrow("soundtrigger_middleware");
                    asInterface = ISoundTriggerMiddlewareService.Stub.asInterface(binder);
                } catch (Exception e) {
                    try {
                        Log.e(TAG, "Failed to bind to soundtrigger service", e);
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }
        return asInterface;
    }

    @Deprecated
    public synchronized void detach() {
        this.mSoundTriggerModule.detach();
    }

    @Deprecated
    public synchronized int startRecognition(int soundModelHandle, SoundTrigger.RecognitionConfig config) {
        return this.mSoundTriggerModule.startRecognition(soundModelHandle, config.getRecognitionConfig());
    }

    @Deprecated
    public synchronized int stopRecognition(int soundModelHandle) {
        return this.mSoundTriggerModule.stopRecognition(soundModelHandle);
    }

    @Deprecated
    public synchronized int loadSoundModel(SoundTrigger.SoundModel model, int[] soundModelHandle) {
        return this.mSoundTriggerModule.loadSoundModel(model.getSoundModel(), soundModelHandle);
    }

    @Deprecated
    public synchronized int unloadSoundModel(int soundModelHandle) {
        return this.mSoundTriggerModule.unloadSoundModel(soundModelHandle);
    }
}
