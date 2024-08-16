package com.android.server.soundtrigger_middleware;

import android.media.soundtrigger.ModelParameterRange;
import android.media.soundtrigger.PhraseSoundModel;
import android.media.soundtrigger.Properties;
import android.media.soundtrigger.RecognitionConfig;
import android.media.soundtrigger.RecognitionEvent;
import android.media.soundtrigger.SoundModel;
import android.media.soundtrigger_middleware.ISoundTriggerCallback;
import android.media.soundtrigger_middleware.ISoundTriggerModule;
import android.media.soundtrigger_middleware.PhraseRecognitionEventSys;
import android.media.soundtrigger_middleware.RecognitionEventSys;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.server.soundtrigger_middleware.ISoundTriggerHal;
import com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SoundTriggerModule implements IBinder.DeathRecipient, ISoundTriggerHal.GlobalCallback {
    private static final String TAG = "SoundTriggerModule";
    private final Set<Session> mActiveSessions = new HashSet();
    private final SoundTriggerMiddlewareImpl.AudioSessionProvider mAudioSessionProvider;
    private final HalFactory mHalFactory;
    private ISoundTriggerHal mHalService;
    private Properties mProperties;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public enum ModelState {
        INIT,
        LOADED,
        ACTIVE
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SoundTriggerModule(HalFactory halFactory, SoundTriggerMiddlewareImpl.AudioSessionProvider audioSessionProvider) {
        Objects.requireNonNull(halFactory);
        this.mHalFactory = halFactory;
        Objects.requireNonNull(audioSessionProvider);
        this.mAudioSessionProvider = audioSessionProvider;
        attachToHal();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ISoundTriggerModule attach(ISoundTriggerCallback iSoundTriggerCallback) {
        Session session;
        session = new Session(iSoundTriggerCallback);
        this.mActiveSessions.add(session);
        return session;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Properties getProperties() {
        return this.mProperties;
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        ArrayList arrayList;
        Log.w(TAG, "Underlying HAL driver died.");
        synchronized (this) {
            arrayList = new ArrayList(this.mActiveSessions.size());
            Iterator<Session> it = this.mActiveSessions.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().moduleDied());
            }
            this.mActiveSessions.clear();
            reset();
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            try {
                ((ISoundTriggerCallback) it2.next()).onModuleDied();
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            }
        }
    }

    private void reset() {
        this.mHalService.detach();
        attachToHal();
    }

    private void attachToHal() {
        Log.d(TAG, "attachToHal begin.");
        if (Build.isQcomPlatform()) {
            this.mHalService = null;
            while (this.mHalService == null) {
                try {
                    this.mHalService = new SoundTriggerHalEnforcer(new SoundTriggerHalWatchdog(new SoundTriggerDuplicateModelHandler(this.mHalFactory.create())));
                } catch (RuntimeException e) {
                    Log.w(TAG, "Failed to init SoundTriggerHal.", e);
                }
            }
        } else {
            this.mHalService = new SoundTriggerHalEnforcer(new SoundTriggerHalWatchdog(this.mHalFactory.create()));
        }
        this.mHalService.linkToDeath(this);
        this.mHalService.registerCallback(this);
        this.mProperties = this.mHalService.getProperties();
        Log.d(TAG, "attachToHal end.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSession(Session session) {
        this.mActiveSessions.remove(session);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback
    public void onResourcesAvailable() {
        ArrayList arrayList;
        synchronized (this) {
            arrayList = new ArrayList(this.mActiveSessions.size());
            Iterator<Session> it = this.mActiveSessions.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().mCallback);
            }
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            try {
                ((ISoundTriggerCallback) it2.next()).onResourcesAvailable();
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class Session implements ISoundTriggerModule {
        private ISoundTriggerCallback mCallback;
        private final Map<Integer, Model> mLoadedModels;
        private final IBinder mToken;

        private Session(ISoundTriggerCallback iSoundTriggerCallback) {
            Binder binder = new Binder();
            this.mToken = binder;
            this.mLoadedModels = new HashMap();
            this.mCallback = iSoundTriggerCallback;
            SoundTriggerModule.this.mHalService.clientAttached(binder);
        }

        public void detach() {
            synchronized (SoundTriggerModule.this) {
                if (this.mCallback == null) {
                    return;
                }
                SoundTriggerModule.this.removeSession(this);
                this.mCallback = null;
                SoundTriggerModule.this.mHalService.clientDetached(this.mToken);
            }
        }

        public int loadModel(SoundModel soundModel) {
            int load;
            synchronized (SoundTriggerModule.this) {
                SoundTriggerMiddlewareImpl.AudioSessionProvider.AudioSession acquireSession = SoundTriggerModule.this.mAudioSessionProvider.acquireSession();
                try {
                    checkValid();
                    load = new Model().load(soundModel, acquireSession);
                } catch (Exception e) {
                    try {
                        SoundTriggerModule.this.mAudioSessionProvider.releaseSession(acquireSession.mSessionHandle);
                    } catch (Exception e2) {
                        Log.e(SoundTriggerModule.TAG, "Failed to release session.", e2);
                    }
                    throw e;
                }
            }
            return load;
        }

        public int loadPhraseModel(PhraseSoundModel phraseSoundModel) {
            int load;
            synchronized (SoundTriggerModule.this) {
                SoundTriggerMiddlewareImpl.AudioSessionProvider.AudioSession acquireSession = SoundTriggerModule.this.mAudioSessionProvider.acquireSession();
                try {
                    checkValid();
                    load = new Model().load(phraseSoundModel, acquireSession);
                    Log.d(SoundTriggerModule.TAG, String.format("loadPhraseModel()->%d", Integer.valueOf(load)));
                } catch (Exception e) {
                    try {
                        SoundTriggerModule.this.mAudioSessionProvider.releaseSession(acquireSession.mSessionHandle);
                    } catch (Exception e2) {
                        Log.e(SoundTriggerModule.TAG, "Failed to release session.", e2);
                    }
                    throw e;
                }
            }
            return load;
        }

        public void unloadModel(int i) {
            synchronized (SoundTriggerModule.this) {
                checkValid();
                SoundTriggerModule.this.mAudioSessionProvider.releaseSession(this.mLoadedModels.get(Integer.valueOf(i)).unload());
            }
        }

        public IBinder startRecognition(int i, RecognitionConfig recognitionConfig) {
            IBinder startRecognition;
            synchronized (SoundTriggerModule.this) {
                checkValid();
                startRecognition = this.mLoadedModels.get(Integer.valueOf(i)).startRecognition(recognitionConfig);
            }
            return startRecognition;
        }

        public void stopRecognition(int i) {
            Model model;
            synchronized (SoundTriggerModule.this) {
                checkValid();
                model = this.mLoadedModels.get(Integer.valueOf(i));
            }
            model.stopRecognition();
        }

        public void forceRecognitionEvent(int i) {
            synchronized (SoundTriggerModule.this) {
                checkValid();
                this.mLoadedModels.get(Integer.valueOf(i)).forceRecognitionEvent();
            }
        }

        public void setModelParameter(int i, int i2, int i3) {
            synchronized (SoundTriggerModule.this) {
                checkValid();
                this.mLoadedModels.get(Integer.valueOf(i)).setParameter(i2, i3);
            }
        }

        public int getModelParameter(int i, int i2) {
            int parameter;
            synchronized (SoundTriggerModule.this) {
                checkValid();
                parameter = this.mLoadedModels.get(Integer.valueOf(i)).getParameter(i2);
            }
            return parameter;
        }

        public ModelParameterRange queryModelParameterSupport(int i, int i2) {
            ModelParameterRange queryModelParameterSupport;
            synchronized (SoundTriggerModule.this) {
                checkValid();
                queryModelParameterSupport = this.mLoadedModels.get(Integer.valueOf(i)).queryModelParameterSupport(i2);
            }
            return queryModelParameterSupport;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public ISoundTriggerCallback moduleDied() {
            ISoundTriggerCallback iSoundTriggerCallback = this.mCallback;
            this.mCallback = null;
            return iSoundTriggerCallback;
        }

        private void checkValid() {
            if (this.mCallback == null) {
                throw new RecoverableException(4);
            }
        }

        public IBinder asBinder() {
            throw new UnsupportedOperationException("This implementation is not intended to be used directly with Binder.");
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        private class Model implements ISoundTriggerHal.ModelCallback {
            public int mHandle;
            private boolean mIsStopping;
            private IBinder mRecognitionToken;
            private SoundTriggerMiddlewareImpl.AudioSessionProvider.AudioSession mSession;
            private ModelState mState;

            private Model() {
                this.mState = ModelState.INIT;
                this.mRecognitionToken = null;
                this.mIsStopping = false;
            }

            private ModelState getState() {
                return this.mState;
            }

            private void setState(ModelState modelState) {
                this.mState = modelState;
                SoundTriggerModule.this.notifyAll();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public int load(SoundModel soundModel, SoundTriggerMiddlewareImpl.AudioSessionProvider.AudioSession audioSession) {
                this.mSession = audioSession;
                this.mHandle = SoundTriggerModule.this.mHalService.loadSoundModel(soundModel, this);
                setState(ModelState.LOADED);
                Session.this.mLoadedModels.put(Integer.valueOf(this.mHandle), this);
                return this.mHandle;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public int load(PhraseSoundModel phraseSoundModel, SoundTriggerMiddlewareImpl.AudioSessionProvider.AudioSession audioSession) {
                this.mSession = audioSession;
                this.mHandle = SoundTriggerModule.this.mHalService.loadPhraseSoundModel(phraseSoundModel, this);
                setState(ModelState.LOADED);
                Session.this.mLoadedModels.put(Integer.valueOf(this.mHandle), this);
                return this.mHandle;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public int unload() {
                SoundTriggerModule.this.mHalService.unloadSoundModel(this.mHandle);
                Session.this.mLoadedModels.remove(Integer.valueOf(this.mHandle));
                return this.mSession.mSessionHandle;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public IBinder startRecognition(RecognitionConfig recognitionConfig) {
                if (this.mIsStopping) {
                    throw new RecoverableException(5, "Race occurred");
                }
                ISoundTriggerHal iSoundTriggerHal = SoundTriggerModule.this.mHalService;
                int i = this.mHandle;
                SoundTriggerMiddlewareImpl.AudioSessionProvider.AudioSession audioSession = this.mSession;
                iSoundTriggerHal.startRecognition(i, audioSession.mDeviceHandle, audioSession.mIoHandle, recognitionConfig);
                this.mRecognitionToken = new Binder();
                setState(ModelState.ACTIVE);
                return this.mRecognitionToken;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void stopRecognition() {
                synchronized (SoundTriggerModule.this) {
                    ModelState state = getState();
                    ModelState modelState = ModelState.LOADED;
                    if (state == modelState) {
                        return;
                    }
                    this.mRecognitionToken = null;
                    this.mIsStopping = true;
                    SoundTriggerModule.this.mHalService.stopRecognition(this.mHandle);
                    synchronized (SoundTriggerModule.this) {
                        this.mIsStopping = false;
                        setState(modelState);
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void forceRecognitionEvent() {
                if (getState() != ModelState.ACTIVE) {
                    return;
                }
                SoundTriggerModule.this.mHalService.forceRecognitionEvent(this.mHandle);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setParameter(int i, int i2) {
                SoundTriggerModule.this.mHalService.setModelParameter(this.mHandle, ConversionUtil.aidl2hidlModelParameter(i), i2);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public int getParameter(int i) {
                return SoundTriggerModule.this.mHalService.getModelParameter(this.mHandle, ConversionUtil.aidl2hidlModelParameter(i));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public ModelParameterRange queryModelParameterSupport(int i) {
                return SoundTriggerModule.this.mHalService.queryParameter(this.mHandle, i);
            }

            @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback
            public void recognitionCallback(int i, RecognitionEventSys recognitionEventSys) {
                synchronized (SoundTriggerModule.this) {
                    IBinder iBinder = this.mRecognitionToken;
                    if (iBinder == null) {
                        return;
                    }
                    recognitionEventSys.token = iBinder;
                    if (!recognitionEventSys.recognitionEvent.recognitionStillActive) {
                        setState(ModelState.LOADED);
                        this.mRecognitionToken = null;
                    }
                    ISoundTriggerCallback iSoundTriggerCallback = Session.this.mCallback;
                    if (iSoundTriggerCallback != null) {
                        try {
                            iSoundTriggerCallback.onRecognition(this.mHandle, recognitionEventSys, this.mSession.mSessionHandle);
                        } catch (RemoteException e) {
                            throw e.rethrowAsRuntimeException();
                        }
                    }
                }
            }

            @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback
            public void phraseRecognitionCallback(int i, PhraseRecognitionEventSys phraseRecognitionEventSys) {
                synchronized (SoundTriggerModule.this) {
                    IBinder iBinder = this.mRecognitionToken;
                    if (iBinder == null) {
                        return;
                    }
                    phraseRecognitionEventSys.token = iBinder;
                    if (Build.isMtkPlatform()) {
                        RecognitionEvent recognitionEvent = phraseRecognitionEventSys.phraseRecognitionEvent.common;
                        byte[] bArr = recognitionEvent.data;
                        int length = bArr != null ? bArr.length : 0;
                        if (!recognitionEvent.recognitionStillActive && (length == 0 || (length > 0 && bArr[0] != 1))) {
                            Log.i(SoundTriggerModule.TAG, "[phraseRecognitionCallback] ModelState.LOADED ");
                            setState(ModelState.LOADED);
                            this.mRecognitionToken = null;
                        }
                    } else if (!phraseRecognitionEventSys.phraseRecognitionEvent.common.recognitionStillActive) {
                        setState(ModelState.LOADED);
                        this.mRecognitionToken = null;
                    }
                    ISoundTriggerCallback iSoundTriggerCallback = Session.this.mCallback;
                    if (iSoundTriggerCallback != null) {
                        try {
                            Session.this.mCallback.onPhraseRecognition(this.mHandle, phraseRecognitionEventSys, this.mSession.mSessionHandle);
                        } catch (RemoteException e) {
                            throw e.rethrowAsRuntimeException();
                        }
                    }
                }
            }

            @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback
            public void modelUnloaded(int i) {
                ISoundTriggerCallback iSoundTriggerCallback;
                synchronized (SoundTriggerModule.this) {
                    iSoundTriggerCallback = Session.this.mCallback;
                }
                if (iSoundTriggerCallback != null) {
                    try {
                        iSoundTriggerCallback.onModelUnloaded(i);
                    } catch (RemoteException e) {
                        throw e.rethrowAsRuntimeException();
                    }
                }
            }
        }
    }
}
