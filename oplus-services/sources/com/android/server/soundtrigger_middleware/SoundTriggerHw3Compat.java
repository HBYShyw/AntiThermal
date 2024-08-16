package com.android.server.soundtrigger_middleware;

import android.hardware.soundtrigger3.ISoundTriggerHw;
import android.hardware.soundtrigger3.ISoundTriggerHwCallback;
import android.hardware.soundtrigger3.ISoundTriggerHwGlobalCallback;
import android.media.soundtrigger.ModelParameterRange;
import android.media.soundtrigger.PhraseRecognitionEvent;
import android.media.soundtrigger.PhraseSoundModel;
import android.media.soundtrigger.Properties;
import android.media.soundtrigger.RecognitionConfig;
import android.media.soundtrigger.RecognitionEvent;
import android.media.soundtrigger.SoundModel;
import android.media.soundtrigger_middleware.PhraseRecognitionEventSys;
import android.media.soundtrigger_middleware.RecognitionEventSys;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.os.SystemClock;
import com.android.server.soundtrigger_middleware.ISoundTriggerHal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SoundTriggerHw3Compat implements ISoundTriggerHal {
    private final ISoundTriggerHw mDriver;
    private final Runnable mRebootRunnable;

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void clientAttached(IBinder iBinder) {
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void clientDetached(IBinder iBinder) {
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void detach() {
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void flushCallbacks() {
    }

    public SoundTriggerHw3Compat(IBinder iBinder, Runnable runnable) {
        this.mDriver = ISoundTriggerHw.Stub.asInterface(iBinder);
        this.mRebootRunnable = runnable;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public Properties getProperties() {
        try {
            return this.mDriver.getProperties();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void registerCallback(ISoundTriggerHal.GlobalCallback globalCallback) {
        try {
            this.mDriver.registerGlobalCallback(new GlobalCallbackAdaper(globalCallback));
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadSoundModel(SoundModel soundModel, ISoundTriggerHal.ModelCallback modelCallback) {
        try {
            return this.mDriver.loadSoundModel(soundModel, new ModelCallbackAdaper(modelCallback));
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        } catch (ServiceSpecificException e2) {
            if (e2.errorCode == 1) {
                throw new RecoverableException(1);
            }
            throw e2;
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadPhraseSoundModel(PhraseSoundModel phraseSoundModel, ISoundTriggerHal.ModelCallback modelCallback) {
        try {
            return this.mDriver.loadPhraseSoundModel(phraseSoundModel, new ModelCallbackAdaper(modelCallback));
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        } catch (ServiceSpecificException e2) {
            if (e2.errorCode == 1) {
                throw new RecoverableException(1);
            }
            throw e2;
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unloadSoundModel(int i) {
        try {
            this.mDriver.unloadSoundModel(i);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void startRecognition(int i, int i2, int i3, RecognitionConfig recognitionConfig) {
        try {
            this.mDriver.startRecognition(i, i2, i3, recognitionConfig);
        } catch (ServiceSpecificException e) {
            if (e.errorCode == 1) {
                throw new RecoverableException(1);
            }
            throw e;
        } catch (RemoteException e2) {
            throw e2.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void stopRecognition(int i) {
        try {
            this.mDriver.stopRecognition(i);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void forceRecognitionEvent(int i) {
        try {
            this.mDriver.forceRecognitionEvent(i);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public ModelParameterRange queryParameter(int i, int i2) {
        try {
            return this.mDriver.queryParameter(i, i2);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int getModelParameter(int i, int i2) {
        try {
            return this.mDriver.getParameter(i, i2);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void setModelParameter(int i, int i2, int i3) {
        try {
            this.mDriver.setParameter(i, i2, i3);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public String interfaceDescriptor() {
        try {
            return this.mDriver.asBinder().getInterfaceDescriptor();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void linkToDeath(IBinder.DeathRecipient deathRecipient) {
        try {
            this.mDriver.asBinder().linkToDeath(deathRecipient, 0);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unlinkToDeath(IBinder.DeathRecipient deathRecipient) {
        this.mDriver.asBinder().unlinkToDeath(deathRecipient, 0);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void reboot() {
        this.mRebootRunnable.run();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class GlobalCallbackAdaper extends ISoundTriggerHwGlobalCallback.Stub {
        private final ISoundTriggerHal.GlobalCallback mDelegate;

        public String getInterfaceHash() {
            return "7d8d63478cd50e766d2072140c8aa3457f9fb585";
        }

        public int getInterfaceVersion() {
            return 1;
        }

        public GlobalCallbackAdaper(ISoundTriggerHal.GlobalCallback globalCallback) {
            this.mDelegate = globalCallback;
        }

        public void onResourcesAvailable() {
            this.mDelegate.onResourcesAvailable();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class ModelCallbackAdaper extends ISoundTriggerHwCallback.Stub {
        private final ISoundTriggerHal.ModelCallback mDelegate;

        public String getInterfaceHash() {
            return "7d8d63478cd50e766d2072140c8aa3457f9fb585";
        }

        public int getInterfaceVersion() {
            return 1;
        }

        public ModelCallbackAdaper(ISoundTriggerHal.ModelCallback modelCallback) {
            this.mDelegate = modelCallback;
        }

        public void modelUnloaded(int i) {
            this.mDelegate.modelUnloaded(i);
        }

        public void phraseRecognitionCallback(int i, PhraseRecognitionEvent phraseRecognitionEvent) {
            RecognitionEvent recognitionEvent = phraseRecognitionEvent.common;
            recognitionEvent.recognitionStillActive |= recognitionEvent.status == 3;
            PhraseRecognitionEventSys phraseRecognitionEventSys = new PhraseRecognitionEventSys();
            phraseRecognitionEventSys.phraseRecognitionEvent = phraseRecognitionEvent;
            phraseRecognitionEventSys.halEventReceivedMillis = SystemClock.elapsedRealtimeNanos();
            this.mDelegate.phraseRecognitionCallback(i, phraseRecognitionEventSys);
        }

        public void recognitionCallback(int i, RecognitionEvent recognitionEvent) {
            recognitionEvent.recognitionStillActive |= recognitionEvent.status == 3;
            RecognitionEventSys recognitionEventSys = new RecognitionEventSys();
            recognitionEventSys.recognitionEvent = recognitionEvent;
            recognitionEventSys.halEventReceivedMillis = SystemClock.elapsedRealtimeNanos();
            this.mDelegate.recognitionCallback(i, recognitionEventSys);
        }
    }
}
