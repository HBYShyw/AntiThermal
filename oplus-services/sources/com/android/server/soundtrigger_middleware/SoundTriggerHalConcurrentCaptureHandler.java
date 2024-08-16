package com.android.server.soundtrigger_middleware;

import android.media.soundtrigger.ModelParameterRange;
import android.media.soundtrigger.PhraseSoundModel;
import android.media.soundtrigger.Properties;
import android.media.soundtrigger.RecognitionConfig;
import android.media.soundtrigger.RecognitionEvent;
import android.media.soundtrigger.SoundModel;
import android.media.soundtrigger_middleware.PhraseRecognitionEventSys;
import android.media.soundtrigger_middleware.RecognitionEventSys;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import com.android.server.soundtrigger_middleware.ICaptureStateNotifier;
import com.android.server.soundtrigger_middleware.ISoundTriggerHal;
import com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SoundTriggerHalConcurrentCaptureHandler implements ISoundTriggerHal, ICaptureStateNotifier.Listener {
    private static final String TAG = "SoundTriggerHalConcurrentCaptureHandler";
    private boolean mCaptureState;
    private final ISoundTriggerHal mDelegate;
    private ISoundTriggerHal.GlobalCallback mGlobalCallback;
    private final ICaptureStateNotifier mNotifier;
    private final Object mStartStopLock = new Object();
    private final Map<Integer, LoadedModel> mLoadedModels = new ConcurrentHashMap();
    private final Set<Integer> mActiveModels = new HashSet();
    private final Map<IBinder.DeathRecipient, IBinder.DeathRecipient> mDeathRecipientMap = new ConcurrentHashMap();
    private final CallbackThread mCallbackThread = new CallbackThread();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class LoadedModel {
        public final ISoundTriggerHal.ModelCallback callback;
        public final int type;

        LoadedModel(int i, ISoundTriggerHal.ModelCallback modelCallback) {
            this.type = i;
            this.callback = modelCallback;
        }
    }

    public SoundTriggerHalConcurrentCaptureHandler(ISoundTriggerHal iSoundTriggerHal, ICaptureStateNotifier iCaptureStateNotifier) {
        this.mDelegate = iSoundTriggerHal;
        this.mNotifier = iCaptureStateNotifier;
        this.mCaptureState = iCaptureStateNotifier.registerListener(this);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void startRecognition(int i, int i2, int i3, RecognitionConfig recognitionConfig) {
        synchronized (this.mStartStopLock) {
            synchronized (this.mActiveModels) {
                Log.d(TAG, "startRecognition, mCaptureState = " + this.mCaptureState);
                if (this.mCaptureState) {
                    throw new RecoverableException(1);
                }
                this.mDelegate.startRecognition(i, i2, i3, recognitionConfig);
                this.mActiveModels.add(Integer.valueOf(i));
            }
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void stopRecognition(int i) {
        boolean remove;
        synchronized (this.mStartStopLock) {
            synchronized (this.mActiveModels) {
                remove = this.mActiveModels.remove(Integer.valueOf(i));
            }
            if (remove) {
                this.mDelegate.stopRecognition(i);
            }
        }
        this.mCallbackThread.flush();
    }

    @Override // com.android.server.soundtrigger_middleware.ICaptureStateNotifier.Listener
    public void onCaptureStateChange(boolean z) {
        synchronized (this.mStartStopLock) {
            if (z) {
                abortAllActiveModels();
            } else {
                ISoundTriggerHal.GlobalCallback globalCallback = this.mGlobalCallback;
                if (globalCallback != null) {
                    globalCallback.onResourcesAvailable();
                }
            }
            this.mCaptureState = z;
            Log.d(TAG, "onCaptureStateChange, mCaptureState = " + this.mCaptureState);
        }
    }

    private void abortAllActiveModels() {
        final int intValue;
        while (true) {
            synchronized (this.mActiveModels) {
                Iterator<Integer> it = this.mActiveModels.iterator();
                if (!it.hasNext()) {
                    return;
                }
                intValue = it.next().intValue();
                this.mActiveModels.remove(Integer.valueOf(intValue));
            }
            this.mDelegate.stopRecognition(intValue);
            final LoadedModel loadedModel = this.mLoadedModels.get(Integer.valueOf(intValue));
            this.mCallbackThread.push(new Runnable() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    SoundTriggerHalConcurrentCaptureHandler.notifyAbort(intValue, loadedModel);
                }
            });
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadSoundModel(SoundModel soundModel, ISoundTriggerHal.ModelCallback modelCallback) {
        int loadSoundModel = this.mDelegate.loadSoundModel(soundModel, new CallbackWrapper(modelCallback));
        this.mLoadedModels.put(Integer.valueOf(loadSoundModel), new LoadedModel(1, modelCallback));
        return loadSoundModel;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int loadPhraseSoundModel(PhraseSoundModel phraseSoundModel, ISoundTriggerHal.ModelCallback modelCallback) {
        int loadPhraseSoundModel = this.mDelegate.loadPhraseSoundModel(phraseSoundModel, new CallbackWrapper(modelCallback));
        this.mLoadedModels.put(Integer.valueOf(loadPhraseSoundModel), new LoadedModel(0, modelCallback));
        return loadPhraseSoundModel;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unloadSoundModel(int i) {
        this.mLoadedModels.remove(Integer.valueOf(i));
        this.mDelegate.unloadSoundModel(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerCallback$1(final ISoundTriggerHal.GlobalCallback globalCallback) {
        CallbackThread callbackThread = this.mCallbackThread;
        Objects.requireNonNull(globalCallback);
        callbackThread.push(new Runnable() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ISoundTriggerHal.GlobalCallback.this.onResourcesAvailable();
            }
        });
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void registerCallback(final ISoundTriggerHal.GlobalCallback globalCallback) {
        ISoundTriggerHal.GlobalCallback globalCallback2 = new ISoundTriggerHal.GlobalCallback() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$$ExternalSyntheticLambda2
            @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback
            public final void onResourcesAvailable() {
                SoundTriggerHalConcurrentCaptureHandler.this.lambda$registerCallback$1(globalCallback);
            }
        };
        this.mGlobalCallback = globalCallback2;
        this.mDelegate.registerCallback(globalCallback2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$linkToDeath$2(final IBinder.DeathRecipient deathRecipient) {
        CallbackThread callbackThread = this.mCallbackThread;
        Objects.requireNonNull(deathRecipient);
        callbackThread.push(new Runnable() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                deathRecipient.binderDied();
            }
        });
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void linkToDeath(final IBinder.DeathRecipient deathRecipient) {
        IBinder.DeathRecipient deathRecipient2 = new IBinder.DeathRecipient() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$$ExternalSyntheticLambda1
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                SoundTriggerHalConcurrentCaptureHandler.this.lambda$linkToDeath$2(deathRecipient);
            }
        };
        this.mDelegate.linkToDeath(deathRecipient2);
        this.mDeathRecipientMap.put(deathRecipient, deathRecipient2);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void unlinkToDeath(IBinder.DeathRecipient deathRecipient) {
        this.mDelegate.unlinkToDeath(this.mDeathRecipientMap.remove(deathRecipient));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class CallbackWrapper implements ISoundTriggerHal.ModelCallback {
        private final ISoundTriggerHal.ModelCallback mDelegateCallback;

        private CallbackWrapper(ISoundTriggerHal.ModelCallback modelCallback) {
            this.mDelegateCallback = modelCallback;
        }

        @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback
        public void recognitionCallback(final int i, final RecognitionEventSys recognitionEventSys) {
            synchronized (SoundTriggerHalConcurrentCaptureHandler.this.mActiveModels) {
                if (SoundTriggerHalConcurrentCaptureHandler.this.mActiveModels.contains(Integer.valueOf(i))) {
                    if (!recognitionEventSys.recognitionEvent.recognitionStillActive) {
                        SoundTriggerHalConcurrentCaptureHandler.this.mActiveModels.remove(Integer.valueOf(i));
                    }
                    SoundTriggerHalConcurrentCaptureHandler.this.mCallbackThread.push(new Runnable() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$CallbackWrapper$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            SoundTriggerHalConcurrentCaptureHandler.CallbackWrapper.this.lambda$recognitionCallback$0(i, recognitionEventSys);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$recognitionCallback$0(int i, RecognitionEventSys recognitionEventSys) {
            this.mDelegateCallback.recognitionCallback(i, recognitionEventSys);
        }

        @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback
        public void phraseRecognitionCallback(final int i, final PhraseRecognitionEventSys phraseRecognitionEventSys) {
            synchronized (SoundTriggerHalConcurrentCaptureHandler.this.mActiveModels) {
                if (SoundTriggerHalConcurrentCaptureHandler.this.mActiveModels.contains(Integer.valueOf(i))) {
                    if (Build.isMtkPlatform()) {
                        RecognitionEvent recognitionEvent = phraseRecognitionEventSys.phraseRecognitionEvent.common;
                        byte[] bArr = recognitionEvent.data;
                        int length = bArr != null ? bArr.length : 0;
                        if (!recognitionEvent.recognitionStillActive && (length == 0 || bArr[0] != 1)) {
                            SoundTriggerHalConcurrentCaptureHandler.this.mActiveModels.remove(Integer.valueOf(i));
                        }
                    } else if (!phraseRecognitionEventSys.phraseRecognitionEvent.common.recognitionStillActive) {
                        SoundTriggerHalConcurrentCaptureHandler.this.mActiveModels.remove(Integer.valueOf(i));
                    }
                    SoundTriggerHalConcurrentCaptureHandler.this.mCallbackThread.push(new Runnable() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$CallbackWrapper$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            SoundTriggerHalConcurrentCaptureHandler.CallbackWrapper.this.lambda$phraseRecognitionCallback$1(i, phraseRecognitionEventSys);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$phraseRecognitionCallback$1(int i, PhraseRecognitionEventSys phraseRecognitionEventSys) {
            this.mDelegateCallback.phraseRecognitionCallback(i, phraseRecognitionEventSys);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$modelUnloaded$2(int i) {
            this.mDelegateCallback.modelUnloaded(i);
        }

        @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal.ModelCallback
        public void modelUnloaded(final int i) {
            SoundTriggerHalConcurrentCaptureHandler.this.mCallbackThread.push(new Runnable() { // from class: com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler$CallbackWrapper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SoundTriggerHalConcurrentCaptureHandler.CallbackWrapper.this.lambda$modelUnloaded$2(i);
                }
            });
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void flushCallbacks() {
        this.mDelegate.flushCallbacks();
        this.mCallbackThread.flush();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void clientAttached(IBinder iBinder) {
        this.mDelegate.clientAttached(iBinder);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void clientDetached(IBinder iBinder) {
        this.mDelegate.clientDetached(iBinder);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class CallbackThread implements Runnable {
        private static final String TAG = "SoundTriggerMiddlewareCallbackThread";
        private final Condition mCondition;
        private final Queue<Runnable> mList = new LinkedList();
        private int mProcessedCount;
        private int mPushCount;
        private boolean mQuitting;
        private final ReentrantLock mRtLock;
        private final Thread mThread;

        CallbackThread() {
            ReentrantLock reentrantLock = new ReentrantLock();
            this.mRtLock = reentrantLock;
            this.mCondition = reentrantLock.newCondition();
            this.mPushCount = 0;
            this.mProcessedCount = 0;
            this.mQuitting = false;
            Thread thread = new Thread(this, "STHAL Concurrent Capture Handler Callback");
            this.mThread = thread;
            thread.start();
        }

        @Override // java.lang.Runnable
        public void run() {
            while (true) {
                try {
                    Runnable pop = pop();
                    if (pop == null) {
                        return;
                    }
                    pop.run();
                    this.mRtLock.lock();
                    try {
                        this.mProcessedCount++;
                        this.mCondition.signalAll();
                        this.mRtLock.unlock();
                    } finally {
                    }
                } catch (InterruptedException unused) {
                    return;
                }
            }
        }

        boolean push(Runnable runnable) {
            this.mRtLock.lock();
            try {
                if (!this.mQuitting) {
                    this.mList.add(runnable);
                    this.mPushCount++;
                    this.mCondition.signalAll();
                    return true;
                }
                this.mRtLock.unlock();
                return false;
            } finally {
                this.mRtLock.unlock();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:9:0x0017, code lost:
        
            android.util.Log.w(com.android.server.soundtrigger_middleware.SoundTriggerHalConcurrentCaptureHandler.CallbackThread.TAG, "Wait for flush timed out.");
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        void flush() {
            this.mRtLock.lock();
            try {
                int i = this.mPushCount;
                while (true) {
                    if (this.mProcessedCount == i) {
                        break;
                    } else if (!this.mCondition.await(1L, TimeUnit.SECONDS)) {
                        break;
                    }
                }
            } catch (InterruptedException unused) {
            } catch (Throwable th) {
                this.mRtLock.unlock();
                throw th;
            }
            this.mRtLock.unlock();
        }

        void quit() {
            this.mRtLock.lock();
            try {
                this.mQuitting = true;
                this.mCondition.signalAll();
            } finally {
                this.mRtLock.unlock();
            }
        }

        private Runnable pop() throws InterruptedException {
            this.mRtLock.lock();
            while (this.mList.isEmpty() && !this.mQuitting) {
                try {
                    this.mCondition.await();
                } finally {
                    this.mRtLock.unlock();
                }
            }
            if (!this.mList.isEmpty() || !this.mQuitting) {
                return this.mList.remove();
            }
            this.mRtLock.unlock();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyAbort(int i, LoadedModel loadedModel) {
        int i2 = loadedModel.type;
        if (i2 == 0) {
            loadedModel.callback.phraseRecognitionCallback(i, AidlUtil.newAbortPhraseEvent());
        } else {
            if (i2 != 1) {
                return;
            }
            loadedModel.callback.recognitionCallback(i, AidlUtil.newAbortEvent());
        }
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void detach() {
        this.mDelegate.detach();
        this.mNotifier.unregisterListener(this);
        this.mCallbackThread.quit();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void reboot() {
        this.mDelegate.reboot();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public Properties getProperties() {
        return this.mDelegate.getProperties();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void forceRecognitionEvent(int i) {
        this.mDelegate.forceRecognitionEvent(i);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public int getModelParameter(int i, int i2) {
        return this.mDelegate.getModelParameter(i, i2);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public void setModelParameter(int i, int i2, int i3) {
        this.mDelegate.setModelParameter(i, i2, i3);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public ModelParameterRange queryParameter(int i, int i2) {
        return this.mDelegate.queryParameter(i, i2);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerHal
    public String interfaceDescriptor() {
        return this.mDelegate.interfaceDescriptor();
    }
}
