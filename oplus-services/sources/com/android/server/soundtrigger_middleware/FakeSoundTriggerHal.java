package com.android.server.soundtrigger_middleware;

import android.hardware.soundtrigger3.ISoundTriggerHw;
import android.hardware.soundtrigger3.ISoundTriggerHwCallback;
import android.hardware.soundtrigger3.ISoundTriggerHwGlobalCallback;
import android.media.soundtrigger.ModelParameterRange;
import android.media.soundtrigger.Phrase;
import android.media.soundtrigger.PhraseRecognitionEvent;
import android.media.soundtrigger.PhraseRecognitionExtra;
import android.media.soundtrigger.PhraseSoundModel;
import android.media.soundtrigger.Properties;
import android.media.soundtrigger.RecognitionConfig;
import android.media.soundtrigger.RecognitionEvent;
import android.media.soundtrigger.SoundModel;
import android.media.soundtrigger_middleware.IAcknowledgeEvent;
import android.media.soundtrigger_middleware.IInjectGlobalEvent;
import android.media.soundtrigger_middleware.IInjectModelEvent;
import android.media.soundtrigger_middleware.IInjectRecognitionEvent;
import android.media.soundtrigger_middleware.ISoundTriggerInjection;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.FunctionalUtils;
import com.android.server.hdmi.HdmiCecKeycode;
import com.android.server.hdmi.HotplugDetectionAction;
import com.android.server.pm.PackageManagerService;
import com.android.server.soundtrigger_middleware.FakeSoundTriggerHal;
import com.android.server.usb.descriptors.UsbDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class FakeSoundTriggerHal extends ISoundTriggerHw.Stub {
    private static final String TAG = "FakeSoundTriggerHal";
    private static final int THRESHOLD_MAX = 10;
    private static final int THRESHOLD_MIN = -10;

    @GuardedBy({"mLock"})
    private IBinder.DeathRecipient mDeathRecipient;

    @GuardedBy({"mLock"})
    private GlobalCallbackDispatcher mGlobalCallbackDispatcher;
    private final IInjectGlobalEvent.Stub mGlobalEventSession;
    private final InjectionDispatcher mInjectionDispatcher;
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private boolean mIsResourceContended = false;

    @GuardedBy({"mLock"})
    private final Map<Integer, ModelSession> mModelSessionMap = new HashMap();

    @GuardedBy({"mLock"})
    private int mModelKeyCounter = HdmiCecKeycode.CEC_KEYCODE_MUTE_FUNCTION;

    @GuardedBy({"mLock"})
    private boolean mIsDead = false;
    private final Properties mProperties = createDefaultProperties();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ExecutorHolder {
        static final Executor CALLBACK_EXECUTOR = Executors.newSingleThreadExecutor();
        static final Executor INJECTION_EXECUTOR = Executors.newSingleThreadExecutor();

        ExecutorHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class ModelSession extends IInjectModelEvent.Stub {
        private final CallbackDispatcher mCallbackDispatcher;
        private final boolean mIsKeyphrase;

        @GuardedBy({"FakeSoundTriggerHal.this.mLock"})
        private boolean mIsUnloaded;
        private final int mModelHandle;

        @GuardedBy({"FakeSoundTriggerHal.this.mLock"})
        private RecognitionSession mRecognitionSession;

        @GuardedBy({"FakeSoundTriggerHal.this.mLock"})
        private int mThreshold;

        private ModelSession(int i, CallbackDispatcher callbackDispatcher, boolean z) {
            this.mThreshold = 0;
            this.mIsUnloaded = false;
            this.mModelHandle = i;
            this.mCallbackDispatcher = callbackDispatcher;
            this.mIsKeyphrase = z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public RecognitionSession startRecognitionForModel() {
            RecognitionSession recognitionSession;
            synchronized (FakeSoundTriggerHal.this.mLock) {
                recognitionSession = new RecognitionSession();
                this.mRecognitionSession = recognitionSession;
            }
            return recognitionSession;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public RecognitionSession stopRecognitionForModel() {
            RecognitionSession recognitionSession;
            synchronized (FakeSoundTriggerHal.this.mLock) {
                recognitionSession = this.mRecognitionSession;
                this.mRecognitionSession = null;
            }
            return recognitionSession;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void forceRecognitionForModel() {
            synchronized (FakeSoundTriggerHal.this.mLock) {
                if (this.mIsKeyphrase) {
                    final PhraseRecognitionEvent createDefaultKeyphraseEvent = FakeSoundTriggerHal.createDefaultKeyphraseEvent(3);
                    this.mCallbackDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$$ExternalSyntheticLambda0
                        public final void acceptOrThrow(Object obj) {
                            FakeSoundTriggerHal.ModelSession.this.lambda$forceRecognitionForModel$0(createDefaultKeyphraseEvent, (ISoundTriggerHwCallback) obj);
                        }
                    });
                } else {
                    final RecognitionEvent createDefaultEvent = FakeSoundTriggerHal.createDefaultEvent(3);
                    this.mCallbackDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$$ExternalSyntheticLambda1
                        public final void acceptOrThrow(Object obj) {
                            FakeSoundTriggerHal.ModelSession.this.lambda$forceRecognitionForModel$1(createDefaultEvent, (ISoundTriggerHwCallback) obj);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$forceRecognitionForModel$0(PhraseRecognitionEvent phraseRecognitionEvent, ISoundTriggerHwCallback iSoundTriggerHwCallback) throws Exception {
            iSoundTriggerHwCallback.phraseRecognitionCallback(this.mModelHandle, phraseRecognitionEvent);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$forceRecognitionForModel$1(RecognitionEvent recognitionEvent, ISoundTriggerHwCallback iSoundTriggerHwCallback) throws Exception {
            iSoundTriggerHwCallback.recognitionCallback(this.mModelHandle, recognitionEvent);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setThresholdFactor(int i) {
            synchronized (FakeSoundTriggerHal.this.mLock) {
                this.mThreshold = i;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getThresholdFactor() {
            int i;
            synchronized (FakeSoundTriggerHal.this.mLock) {
                i = this.mThreshold;
            }
            return i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean getIsUnloaded() {
            boolean z;
            synchronized (FakeSoundTriggerHal.this.mLock) {
                z = this.mIsUnloaded;
            }
            return z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public RecognitionSession getRecogSession() {
            RecognitionSession recognitionSession;
            synchronized (FakeSoundTriggerHal.this.mLock) {
                recognitionSession = this.mRecognitionSession;
            }
            return recognitionSession;
        }

        public void triggerUnloadModel() {
            synchronized (FakeSoundTriggerHal.this.mLock) {
                if (!FakeSoundTriggerHal.this.mIsDead && !this.mIsUnloaded) {
                    RecognitionSession recognitionSession = this.mRecognitionSession;
                    if (recognitionSession != null) {
                        recognitionSession.triggerAbortRecognition();
                    }
                    this.mIsUnloaded = true;
                    this.mCallbackDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$$ExternalSyntheticLambda2
                        public final void acceptOrThrow(Object obj) {
                            FakeSoundTriggerHal.ModelSession.this.lambda$triggerUnloadModel$2((ISoundTriggerHwCallback) obj);
                        }
                    });
                    if (FakeSoundTriggerHal.this.getNumLoadedModelsLocked() == FakeSoundTriggerHal.this.mProperties.maxSoundModels - 1 && !FakeSoundTriggerHal.this.mIsResourceContended) {
                        FakeSoundTriggerHal.this.mGlobalCallbackDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$$ExternalSyntheticLambda3
                            public final void acceptOrThrow(Object obj) {
                                ((ISoundTriggerHwGlobalCallback) obj).onResourcesAvailable();
                            }
                        });
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$triggerUnloadModel$2(ISoundTriggerHwCallback iSoundTriggerHwCallback) throws Exception {
            iSoundTriggerHwCallback.modelUnloaded(this.mModelHandle);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public class RecognitionSession extends IInjectRecognitionEvent.Stub {
            private RecognitionSession() {
            }

            public void triggerRecognitionEvent(byte[] bArr, PhraseRecognitionExtra[] phraseRecognitionExtraArr) {
                synchronized (FakeSoundTriggerHal.this.mLock) {
                    if (!FakeSoundTriggerHal.this.mIsDead && ModelSession.this.mRecognitionSession == this) {
                        ModelSession.this.mRecognitionSession = null;
                        if (ModelSession.this.mIsKeyphrase) {
                            final PhraseRecognitionEvent createDefaultKeyphraseEvent = FakeSoundTriggerHal.createDefaultKeyphraseEvent(0);
                            createDefaultKeyphraseEvent.common.data = bArr;
                            if (phraseRecognitionExtraArr != null) {
                                createDefaultKeyphraseEvent.phraseExtras = phraseRecognitionExtraArr;
                            }
                            ModelSession.this.mCallbackDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$RecognitionSession$$ExternalSyntheticLambda0
                                public final void acceptOrThrow(Object obj) {
                                    FakeSoundTriggerHal.ModelSession.RecognitionSession.this.lambda$triggerRecognitionEvent$0(createDefaultKeyphraseEvent, (ISoundTriggerHwCallback) obj);
                                }
                            });
                        } else {
                            final RecognitionEvent createDefaultEvent = FakeSoundTriggerHal.createDefaultEvent(0);
                            createDefaultEvent.data = bArr;
                            ModelSession.this.mCallbackDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$RecognitionSession$$ExternalSyntheticLambda1
                                public final void acceptOrThrow(Object obj) {
                                    FakeSoundTriggerHal.ModelSession.RecognitionSession.this.lambda$triggerRecognitionEvent$1(createDefaultEvent, (ISoundTriggerHwCallback) obj);
                                }
                            });
                        }
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$triggerRecognitionEvent$0(PhraseRecognitionEvent phraseRecognitionEvent, ISoundTriggerHwCallback iSoundTriggerHwCallback) throws Exception {
                iSoundTriggerHwCallback.phraseRecognitionCallback(ModelSession.this.mModelHandle, phraseRecognitionEvent);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$triggerRecognitionEvent$1(RecognitionEvent recognitionEvent, ISoundTriggerHwCallback iSoundTriggerHwCallback) throws Exception {
                iSoundTriggerHwCallback.recognitionCallback(ModelSession.this.mModelHandle, recognitionEvent);
            }

            public void triggerAbortRecognition() {
                synchronized (FakeSoundTriggerHal.this.mLock) {
                    if (!FakeSoundTriggerHal.this.mIsDead && ModelSession.this.mRecognitionSession == this) {
                        ModelSession.this.mRecognitionSession = null;
                        if (ModelSession.this.mIsKeyphrase) {
                            ModelSession.this.mCallbackDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$RecognitionSession$$ExternalSyntheticLambda2
                                public final void acceptOrThrow(Object obj) {
                                    FakeSoundTriggerHal.ModelSession.RecognitionSession.this.lambda$triggerAbortRecognition$2((ISoundTriggerHwCallback) obj);
                                }
                            });
                        } else {
                            ModelSession.this.mCallbackDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$ModelSession$RecognitionSession$$ExternalSyntheticLambda3
                                public final void acceptOrThrow(Object obj) {
                                    FakeSoundTriggerHal.ModelSession.RecognitionSession.this.lambda$triggerAbortRecognition$3((ISoundTriggerHwCallback) obj);
                                }
                            });
                        }
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$triggerAbortRecognition$2(ISoundTriggerHwCallback iSoundTriggerHwCallback) throws Exception {
                iSoundTriggerHwCallback.phraseRecognitionCallback(ModelSession.this.mModelHandle, FakeSoundTriggerHal.createDefaultKeyphraseEvent(1));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$triggerAbortRecognition$3(ISoundTriggerHwCallback iSoundTriggerHwCallback) throws Exception {
                iSoundTriggerHwCallback.recognitionCallback(ModelSession.this.mModelHandle, FakeSoundTriggerHal.createDefaultEvent(1));
            }
        }
    }

    public FakeSoundTriggerHal(ISoundTriggerInjection iSoundTriggerInjection) {
        this.mGlobalCallbackDispatcher = null;
        InjectionDispatcher injectionDispatcher = new InjectionDispatcher(iSoundTriggerInjection);
        this.mInjectionDispatcher = injectionDispatcher;
        this.mGlobalCallbackDispatcher = null;
        this.mGlobalEventSession = new AnonymousClass1();
        injectionDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda2
            public final void acceptOrThrow(Object obj) {
                FakeSoundTriggerHal.this.lambda$new$0((ISoundTriggerInjection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 extends IInjectGlobalEvent.Stub {
        AnonymousClass1() {
        }

        public void triggerRestart() {
            synchronized (FakeSoundTriggerHal.this.mLock) {
                if (FakeSoundTriggerHal.this.mIsDead) {
                    return;
                }
                FakeSoundTriggerHal.this.mIsDead = true;
                FakeSoundTriggerHal.this.mInjectionDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$1$$ExternalSyntheticLambda0
                    public final void acceptOrThrow(Object obj) {
                        FakeSoundTriggerHal.AnonymousClass1.this.lambda$triggerRestart$0((ISoundTriggerInjection) obj);
                    }
                });
                FakeSoundTriggerHal.this.mModelSessionMap.clear();
                if (FakeSoundTriggerHal.this.mDeathRecipient != null) {
                    final IBinder.DeathRecipient deathRecipient = FakeSoundTriggerHal.this.mDeathRecipient;
                    ExecutorHolder.CALLBACK_EXECUTOR.execute(new Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$1$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            FakeSoundTriggerHal.AnonymousClass1.this.lambda$triggerRestart$1(deathRecipient);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$triggerRestart$0(ISoundTriggerInjection iSoundTriggerInjection) throws Exception {
            iSoundTriggerInjection.onRestarted(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$triggerRestart$1(IBinder.DeathRecipient deathRecipient) {
            try {
                deathRecipient.binderDied(FakeSoundTriggerHal.this.asBinder());
            } catch (Throwable th) {
                Slog.wtf(FakeSoundTriggerHal.TAG, "Callback dispatch threw", th);
            }
        }

        public void setResourceContention(boolean z, final IAcknowledgeEvent iAcknowledgeEvent) {
            synchronized (FakeSoundTriggerHal.this.mLock) {
                if (FakeSoundTriggerHal.this.mIsDead) {
                    return;
                }
                boolean z2 = FakeSoundTriggerHal.this.mIsResourceContended;
                FakeSoundTriggerHal.this.mIsResourceContended = z;
                FakeSoundTriggerHal.this.mInjectionDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$1$$ExternalSyntheticLambda3
                    public final void acceptOrThrow(Object obj) {
                        iAcknowledgeEvent.eventReceived();
                    }
                });
                if (!FakeSoundTriggerHal.this.mIsResourceContended && z2) {
                    FakeSoundTriggerHal.this.mGlobalCallbackDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$1$$ExternalSyntheticLambda4
                        public final void acceptOrThrow(Object obj) {
                            ((ISoundTriggerHwGlobalCallback) obj).onResourcesAvailable();
                        }
                    });
                }
            }
        }

        public void triggerOnResourcesAvailable() {
            synchronized (FakeSoundTriggerHal.this.mLock) {
                if (FakeSoundTriggerHal.this.mIsDead) {
                    return;
                }
                FakeSoundTriggerHal.this.mGlobalCallbackDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$1$$ExternalSyntheticLambda2
                    public final void acceptOrThrow(Object obj) {
                        ((ISoundTriggerHwGlobalCallback) obj).onResourcesAvailable();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ISoundTriggerInjection iSoundTriggerInjection) throws Exception {
        iSoundTriggerInjection.registerGlobalEventInjection(this.mGlobalEventSession);
    }

    public IInjectGlobalEvent getGlobalEventInjection() {
        return this.mGlobalEventSession;
    }

    public void linkToDeath(IBinder.DeathRecipient deathRecipient, int i) {
        synchronized (this.mLock) {
            if (this.mDeathRecipient != null) {
                Slog.wtf(TAG, "Received two death recipients concurrently");
            }
            this.mDeathRecipient = deathRecipient;
        }
    }

    public boolean unlinkToDeath(IBinder.DeathRecipient deathRecipient, int i) {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                return false;
            }
            if (this.mDeathRecipient != deathRecipient) {
                throw new NoSuchElementException();
            }
            this.mDeathRecipient = null;
            return true;
        }
    }

    public Properties getProperties() throws RemoteException {
        Properties properties;
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new DeadObjectException();
            }
            Parcel obtain = Parcel.obtain();
            try {
                this.mProperties.writeToParcel(obtain, 0);
                obtain.setDataPosition(0);
                properties = (Properties) Properties.CREATOR.createFromParcel(obtain);
            } finally {
                obtain.recycle();
            }
        }
        return properties;
    }

    public void registerGlobalCallback(ISoundTriggerHwGlobalCallback iSoundTriggerHwGlobalCallback) throws RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new DeadObjectException();
            }
            this.mGlobalCallbackDispatcher = new GlobalCallbackDispatcher(iSoundTriggerHwGlobalCallback);
        }
    }

    public int loadSoundModel(final SoundModel soundModel, ISoundTriggerHwCallback iSoundTriggerHwCallback) throws RemoteException {
        int i;
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new DeadObjectException();
            }
            if (this.mIsResourceContended || getNumLoadedModelsLocked() == this.mProperties.maxSoundModels) {
                throw new ServiceSpecificException(1);
            }
            i = this.mModelKeyCounter;
            this.mModelKeyCounter = i + 1;
            final ModelSession modelSession = new ModelSession(i, new CallbackDispatcher(iSoundTriggerHwCallback), false);
            this.mModelSessionMap.put(Integer.valueOf(i), modelSession);
            this.mInjectionDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda5
                public final void acceptOrThrow(Object obj) {
                    FakeSoundTriggerHal.this.lambda$loadSoundModel$1(soundModel, modelSession, (ISoundTriggerInjection) obj);
                }
            });
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadSoundModel$1(SoundModel soundModel, ModelSession modelSession, ISoundTriggerInjection iSoundTriggerInjection) throws Exception {
        iSoundTriggerInjection.onSoundModelLoaded(soundModel, (Phrase[]) null, modelSession, this.mGlobalEventSession);
    }

    public int loadPhraseSoundModel(final PhraseSoundModel phraseSoundModel, ISoundTriggerHwCallback iSoundTriggerHwCallback) throws RemoteException {
        int i;
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new DeadObjectException();
            }
            if (this.mIsResourceContended || getNumLoadedModelsLocked() == this.mProperties.maxSoundModels) {
                throw new ServiceSpecificException(1);
            }
            i = this.mModelKeyCounter;
            this.mModelKeyCounter = i + 1;
            final ModelSession modelSession = new ModelSession(i, new CallbackDispatcher(iSoundTriggerHwCallback), true);
            this.mModelSessionMap.put(Integer.valueOf(i), modelSession);
            this.mInjectionDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda1
                public final void acceptOrThrow(Object obj) {
                    FakeSoundTriggerHal.this.lambda$loadPhraseSoundModel$2(phraseSoundModel, modelSession, (ISoundTriggerInjection) obj);
                }
            });
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPhraseSoundModel$2(PhraseSoundModel phraseSoundModel, ModelSession modelSession, ISoundTriggerInjection iSoundTriggerInjection) throws Exception {
        iSoundTriggerInjection.onSoundModelLoaded(phraseSoundModel.common, phraseSoundModel.phrases, modelSession, this.mGlobalEventSession);
    }

    public void unloadSoundModel(int i) throws RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new DeadObjectException();
            }
            final ModelSession modelSession = this.mModelSessionMap.get(Integer.valueOf(i));
            if (modelSession == null) {
                Slog.wtf(TAG, "Attempted to unload model which was never loaded");
            }
            if (modelSession.getRecogSession() != null) {
                Slog.wtf(TAG, "Session unloaded before recog stopped!");
            }
            if (modelSession.getIsUnloaded()) {
                return;
            }
            this.mInjectionDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda6
                public final void acceptOrThrow(Object obj) {
                    ((ISoundTriggerInjection) obj).onSoundModelUnloaded(FakeSoundTriggerHal.ModelSession.this);
                }
            });
            if (getNumLoadedModelsLocked() == this.mProperties.maxSoundModels - 1 && !this.mIsResourceContended) {
                this.mGlobalCallbackDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda7
                    public final void acceptOrThrow(Object obj) {
                        ((ISoundTriggerHwGlobalCallback) obj).onResourcesAvailable();
                    }
                });
            }
        }
    }

    public void startRecognition(int i, int i2, int i3, final RecognitionConfig recognitionConfig) throws RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new DeadObjectException();
            }
            final ModelSession modelSession = this.mModelSessionMap.get(Integer.valueOf(i));
            if (modelSession == null) {
                Slog.wtf(TAG, "Attempted to start recognition with invalid handle");
            }
            if (this.mIsResourceContended) {
                throw new ServiceSpecificException(1);
            }
            if (modelSession.getIsUnloaded()) {
                throw new ServiceSpecificException(1);
            }
            final ModelSession.RecognitionSession startRecognitionForModel = modelSession.startRecognitionForModel();
            this.mInjectionDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda4
                public final void acceptOrThrow(Object obj) {
                    ((ISoundTriggerInjection) obj).onRecognitionStarted(-1, recognitionConfig, startRecognitionForModel, modelSession);
                }
            });
        }
    }

    public void stopRecognition(int i) throws RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new DeadObjectException();
            }
            ModelSession modelSession = this.mModelSessionMap.get(Integer.valueOf(i));
            if (modelSession == null) {
                Slog.wtf(TAG, "Attempted to stop recognition with invalid handle");
            }
            final ModelSession.RecognitionSession stopRecognitionForModel = modelSession.stopRecognitionForModel();
            if (stopRecognitionForModel != null) {
                this.mInjectionDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda3
                    public final void acceptOrThrow(Object obj) {
                        ((ISoundTriggerInjection) obj).onRecognitionStopped(FakeSoundTriggerHal.ModelSession.RecognitionSession.this);
                    }
                });
            }
        }
    }

    public void forceRecognitionEvent(int i) throws RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new DeadObjectException();
            }
            ModelSession modelSession = this.mModelSessionMap.get(Integer.valueOf(i));
            if (modelSession == null) {
                Slog.wtf(TAG, "Attempted to force recognition with invalid handle");
            }
            if (modelSession.getRecogSession() == null) {
                return;
            }
            modelSession.forceRecognitionForModel();
        }
    }

    public ModelParameterRange queryParameter(int i, int i2) throws RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new DeadObjectException();
            }
            if (this.mModelSessionMap.get(Integer.valueOf(i)) == null) {
                Slog.wtf(TAG, "Attempted to get param with invalid handle");
            }
        }
        if (i2 != 0) {
            return null;
        }
        ModelParameterRange modelParameterRange = new ModelParameterRange();
        modelParameterRange.minInclusive = -10;
        modelParameterRange.maxInclusive = 10;
        return modelParameterRange;
    }

    public int getParameter(int i, int i2) throws RemoteException {
        int thresholdFactor;
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new DeadObjectException();
            }
            ModelSession modelSession = this.mModelSessionMap.get(Integer.valueOf(i));
            if (modelSession == null) {
                Slog.wtf(TAG, "Attempted to get param with invalid handle");
            }
            if (i2 != 0) {
                throw new IllegalArgumentException();
            }
            thresholdFactor = modelSession.getThresholdFactor();
        }
        return thresholdFactor;
    }

    public void setParameter(int i, final int i2, final int i3) throws RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new DeadObjectException();
            }
            final ModelSession modelSession = this.mModelSessionMap.get(Integer.valueOf(i));
            if (modelSession == null) {
                Slog.wtf(TAG, "Attempted to get param with invalid handle");
            }
            if (i2 != 0 && (i3 < -10 || i3 > 10)) {
                throw new IllegalArgumentException();
            }
            modelSession.setThresholdFactor(i3);
            this.mInjectionDispatcher.wrap(new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$$ExternalSyntheticLambda0
                public final void acceptOrThrow(Object obj) {
                    ((ISoundTriggerInjection) obj).onParamSet(i2, i3, modelSession);
                }
            });
        }
    }

    public int getInterfaceVersion() throws RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new DeadObjectException();
            }
        }
        return 1;
    }

    public String getInterfaceHash() throws RemoteException {
        synchronized (this.mLock) {
            if (this.mIsDead) {
                throw new DeadObjectException();
            }
        }
        return "7d8d63478cd50e766d2072140c8aa3457f9fb585";
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public int getNumLoadedModelsLocked() {
        Iterator<ModelSession> it = this.mModelSessionMap.values().iterator();
        int i = 0;
        while (it.hasNext()) {
            if (!it.next().getIsUnloaded()) {
                i++;
            }
        }
        return i;
    }

    private static Properties createDefaultProperties() {
        Properties properties = new Properties();
        properties.implementor = PackageManagerService.PLATFORM_PACKAGE_NAME;
        properties.description = "AOSP fake STHAL";
        properties.version = 1;
        properties.uuid = "00000001-0002-0003-0004-deadbeefabcd";
        properties.supportedModelArch = "injection";
        properties.maxSoundModels = 8;
        properties.maxKeyPhrases = 2;
        properties.maxUsers = 2;
        properties.recognitionModes = 9;
        properties.captureTransition = true;
        properties.maxBufferMs = HotplugDetectionAction.POLLING_INTERVAL_MS_FOR_TV;
        properties.concurrentCapture = true;
        properties.triggerInEvent = false;
        properties.powerConsumptionMw = 0;
        properties.audioCapabilities = 0;
        return properties;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static RecognitionEvent createDefaultEvent(int i) {
        RecognitionEvent recognitionEvent = new RecognitionEvent();
        recognitionEvent.status = i;
        recognitionEvent.type = 1;
        recognitionEvent.captureAvailable = true;
        recognitionEvent.captureDelayMs = 50;
        recognitionEvent.capturePreambleMs = UsbDescriptor.USB_CONTROL_TRANSFER_TIMEOUT_MS;
        recognitionEvent.triggerInData = false;
        recognitionEvent.audioConfig = null;
        recognitionEvent.data = new byte[0];
        recognitionEvent.recognitionStillActive = false;
        return recognitionEvent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static PhraseRecognitionEvent createDefaultKeyphraseEvent(int i) {
        RecognitionEvent createDefaultEvent = createDefaultEvent(i);
        createDefaultEvent.type = 0;
        PhraseRecognitionEvent phraseRecognitionEvent = new PhraseRecognitionEvent();
        phraseRecognitionEvent.common = createDefaultEvent;
        phraseRecognitionEvent.phraseExtras = new PhraseRecognitionExtra[0];
        return phraseRecognitionEvent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class CallbackDispatcher {
        private final ISoundTriggerHwCallback mCallback;

        private CallbackDispatcher(ISoundTriggerHwCallback iSoundTriggerHwCallback) {
            this.mCallback = iSoundTriggerHwCallback;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void wrap(final FunctionalUtils.ThrowingConsumer<ISoundTriggerHwCallback> throwingConsumer) {
            ExecutorHolder.CALLBACK_EXECUTOR.execute(new Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$CallbackDispatcher$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FakeSoundTriggerHal.CallbackDispatcher.this.lambda$wrap$0(throwingConsumer);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$wrap$0(FunctionalUtils.ThrowingConsumer throwingConsumer) {
            try {
                throwingConsumer.accept(this.mCallback);
            } catch (Throwable th) {
                Slog.wtf(FakeSoundTriggerHal.TAG, "Callback dispatch threw", th);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class GlobalCallbackDispatcher {
        private final ISoundTriggerHwGlobalCallback mCallback;

        private GlobalCallbackDispatcher(ISoundTriggerHwGlobalCallback iSoundTriggerHwGlobalCallback) {
            this.mCallback = iSoundTriggerHwGlobalCallback;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void wrap(final FunctionalUtils.ThrowingConsumer<ISoundTriggerHwGlobalCallback> throwingConsumer) {
            ExecutorHolder.CALLBACK_EXECUTOR.execute(new Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$GlobalCallbackDispatcher$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FakeSoundTriggerHal.GlobalCallbackDispatcher.this.lambda$wrap$0(throwingConsumer);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$wrap$0(FunctionalUtils.ThrowingConsumer throwingConsumer) {
            try {
                throwingConsumer.accept(this.mCallback);
            } catch (Throwable th) {
                Slog.wtf(FakeSoundTriggerHal.TAG, "Callback dispatch threw", th);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class InjectionDispatcher {
        private final ISoundTriggerInjection mInjection;

        private InjectionDispatcher(ISoundTriggerInjection iSoundTriggerInjection) {
            this.mInjection = iSoundTriggerInjection;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void wrap(final FunctionalUtils.ThrowingConsumer<ISoundTriggerInjection> throwingConsumer) {
            ExecutorHolder.INJECTION_EXECUTOR.execute(new Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeSoundTriggerHal$InjectionDispatcher$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FakeSoundTriggerHal.InjectionDispatcher.this.lambda$wrap$0(throwingConsumer);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$wrap$0(FunctionalUtils.ThrowingConsumer throwingConsumer) {
            try {
                throwingConsumer.accept(this.mInjection);
            } catch (Throwable th) {
                Slog.wtf(FakeSoundTriggerHal.TAG, "Callback dispatch threw", th);
            }
        }
    }
}
