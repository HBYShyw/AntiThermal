package com.android.server.voiceinteraction;

import android.annotation.RequiresPermission;
import android.app.AppOpsManager;
import android.app.compat.CompatChanges;
import android.attention.AttentionManagerInternal;
import android.content.Context;
import android.content.PermissionChecker;
import android.hardware.soundtrigger.SoundTrigger;
import android.media.AudioFormat;
import android.media.permission.Identity;
import android.media.permission.PermissionUtil;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.SharedMemory;
import android.service.voice.HotwordDetectedResult;
import android.service.voice.HotwordDetectionService;
import android.service.voice.HotwordDetectionServiceFailure;
import android.service.voice.HotwordDetector;
import android.service.voice.HotwordRejectedResult;
import android.service.voice.IDspHotwordDetectionCallback;
import android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback;
import android.service.voice.ISandboxedDetectionService;
import android.service.voice.VisualQueryDetectionServiceFailure;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.IHotwordRecognitionStatusCallback;
import com.android.internal.infra.AndroidFuture;
import com.android.internal.infra.ServiceConnector;
import com.android.internal.util.FunctionalUtils;
import com.android.server.LocalServices;
import com.android.server.policy.AppOpsPolicy;
import com.android.server.voiceinteraction.DetectorSession;
import com.android.server.voiceinteraction.HotwordDetectionConnection;
import com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class DetectorSession {
    static final boolean DEBUG = false;
    private static final long EXTERNAL_HOTWORD_CLEANUP_MILLIS = 2000;
    private static final int EXTERNAL_SOURCE_DETECT_SECURITY_EXCEPTION = 13;
    private static final String HOTWORD_DETECTION_OP_MESSAGE = "Providing hotword detection result to VoiceInteractionService";
    private static final int METRICS_CALLBACK_ON_STATUS_REPORTED_EXCEPTION = 14;
    private static final int METRICS_EXTERNAL_SOURCE_DETECTED = 11;
    private static final int METRICS_EXTERNAL_SOURCE_REJECTED = 12;
    private static final int METRICS_INIT_CALLBACK_STATE_ERROR = 1;
    private static final int METRICS_INIT_CALLBACK_STATE_SUCCESS = 0;
    private static final int METRICS_INIT_UNKNOWN_NO_VALUE = 2;
    private static final int METRICS_INIT_UNKNOWN_OVER_MAX_CUSTOM_VALUE = 3;
    private static final int METRICS_INIT_UNKNOWN_TIMEOUT = 4;
    static final int METRICS_KEYPHRASE_TRIGGERED_DETECT_SECURITY_EXCEPTION = 8;
    static final int METRICS_KEYPHRASE_TRIGGERED_DETECT_UNEXPECTED_CALLBACK = 7;
    static final int METRICS_KEYPHRASE_TRIGGERED_REJECT_UNEXPECTED_CALLBACK = 9;
    static final int ONDETECTED_GOT_SECURITY_EXCEPTION = 5;
    static final int ONDETECTED_STREAM_COPY_ERROR = 6;
    private static final String TAG = "DetectorSession";
    private final AppOpsManager mAppOpsManager;
    AttentionManagerInternal mAttentionManagerInternal;
    final IHotwordRecognitionStatusCallback mCallback;
    final Context mContext;

    @GuardedBy({"mLock"})
    ParcelFileDescriptor mCurrentAudioSink;
    boolean mDebugHotwordLogging;

    @GuardedBy({"mLock"})
    private boolean mDestroyed;
    final HotwordAudioStreamCopier mHotwordAudioStreamCopier;

    @GuardedBy({"mLock"})
    private boolean mInitialized;
    final Object mLock;

    @GuardedBy({"mLock"})
    boolean mPerformingExternalSourceHotwordDetection;
    final AttentionManagerInternal.ProximityUpdateCallbackInternal mProximityCallbackInternal;

    @GuardedBy({"mLock"})
    private double mProximityMeters;

    @GuardedBy({"mLock"})
    HotwordDetectionConnection.ServiceConnection mRemoteDetectionService;
    VoiceInteractionManagerServiceImpl.DetectorRemoteExceptionListener mRemoteExceptionListener;
    final ScheduledExecutorService mScheduledExecutorService;
    final IBinder mToken;
    final int mVoiceInteractionServiceUid;
    private final Identity mVoiceInteractorIdentity;
    private static final long MAX_UPDATE_TIMEOUT_MILLIS = 30000;
    private static final Duration MAX_UPDATE_TIMEOUT_DURATION = Duration.ofMillis(MAX_UPDATE_TIMEOUT_MILLIS);
    private final Executor mAudioCopyExecutor = Executors.newCachedThreadPool();
    final AtomicBoolean mUpdateStateAfterStartFinished = new AtomicBoolean(false);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void informRestartProcessLocked();

    /* JADX INFO: Access modifiers changed from: package-private */
    public DetectorSession(HotwordDetectionConnection.ServiceConnection serviceConnection, Object obj, Context context, IBinder iBinder, IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback, int i, Identity identity, ScheduledExecutorService scheduledExecutorService, boolean z, VoiceInteractionManagerServiceImpl.DetectorRemoteExceptionListener detectorRemoteExceptionListener) {
        this.mAttentionManagerInternal = null;
        AttentionManagerInternal.ProximityUpdateCallbackInternal proximityUpdateCallbackInternal = new AttentionManagerInternal.ProximityUpdateCallbackInternal() { // from class: com.android.server.voiceinteraction.DetectorSession$$ExternalSyntheticLambda1
            public final void onProximityUpdate(double d) {
                DetectorSession.this.setProximityValue(d);
            }
        };
        this.mProximityCallbackInternal = proximityUpdateCallbackInternal;
        this.mDebugHotwordLogging = false;
        this.mProximityMeters = -1.0d;
        this.mInitialized = false;
        this.mDestroyed = false;
        this.mRemoteExceptionListener = detectorRemoteExceptionListener;
        this.mRemoteDetectionService = serviceConnection;
        this.mLock = obj;
        this.mContext = context;
        this.mToken = iBinder;
        this.mCallback = iHotwordRecognitionStatusCallback;
        this.mVoiceInteractionServiceUid = i;
        this.mVoiceInteractorIdentity = identity;
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        this.mAppOpsManager = appOpsManager;
        if (getDetectorType() != 3) {
            this.mHotwordAudioStreamCopier = new HotwordAudioStreamCopier(appOpsManager, getDetectorType(), identity.uid, identity.packageName, identity.attributionTag);
        } else {
            this.mHotwordAudioStreamCopier = null;
        }
        this.mScheduledExecutorService = scheduledExecutorService;
        this.mDebugHotwordLogging = z;
        AttentionManagerInternal attentionManagerInternal = (AttentionManagerInternal) LocalServices.getService(AttentionManagerInternal.class);
        this.mAttentionManagerInternal = attentionManagerInternal;
        if (attentionManagerInternal != null) {
            attentionManagerInternal.onStartProximityUpdates(proximityUpdateCallbackInternal);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyOnDetectorRemoteException() {
        Slog.d(TAG, "notifyOnDetectorRemoteException: mRemoteExceptionListener=" + this.mRemoteExceptionListener);
        VoiceInteractionManagerServiceImpl.DetectorRemoteExceptionListener detectorRemoteExceptionListener = this.mRemoteExceptionListener;
        if (detectorRemoteExceptionListener != null) {
            detectorRemoteExceptionListener.onDetectorRemoteException(this.mToken, getDetectorType());
        }
    }

    private void updateStateAfterProcessStartLocked(final PersistableBundle persistableBundle, final SharedMemory sharedMemory) {
        if (this.mRemoteDetectionService.postAsync(new ServiceConnector.Job() { // from class: com.android.server.voiceinteraction.DetectorSession$$ExternalSyntheticLambda3
            public final Object run(Object obj) {
                CompletableFuture lambda$updateStateAfterProcessStartLocked$0;
                lambda$updateStateAfterProcessStartLocked$0 = DetectorSession.this.lambda$updateStateAfterProcessStartLocked$0(persistableBundle, sharedMemory, (ISandboxedDetectionService) obj);
                return lambda$updateStateAfterProcessStartLocked$0;
            }
        }).whenComplete(new BiConsumer() { // from class: com.android.server.voiceinteraction.DetectorSession$$ExternalSyntheticLambda4
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                DetectorSession.this.lambda$updateStateAfterProcessStartLocked$1((Void) obj, (Throwable) obj2);
            }
        }) == null) {
            Slog.w(TAG, "Failed to create AndroidFuture");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ CompletableFuture lambda$updateStateAfterProcessStartLocked$0(PersistableBundle persistableBundle, SharedMemory sharedMemory, ISandboxedDetectionService iSandboxedDetectionService) throws Exception {
        final AndroidFuture androidFuture = new AndroidFuture();
        try {
            iSandboxedDetectionService.updateState(persistableBundle, sharedMemory, new IRemoteCallback.Stub() { // from class: com.android.server.voiceinteraction.DetectorSession.1
                public void sendResult(Bundle bundle) throws RemoteException {
                    androidFuture.complete((Object) null);
                    if (DetectorSession.this.mUpdateStateAfterStartFinished.getAndSet(true)) {
                        Slog.w(DetectorSession.TAG, "call callback after timeout");
                        if (DetectorSession.this.getDetectorType() != 3) {
                            HotwordMetricsLogger.writeDetectorEvent(DetectorSession.this.getDetectorType(), 5, DetectorSession.this.mVoiceInteractionServiceUid);
                            return;
                        }
                        return;
                    }
                    Pair initStatusAndMetricsResult = DetectorSession.getInitStatusAndMetricsResult(bundle);
                    int intValue = ((Integer) initStatusAndMetricsResult.first).intValue();
                    int intValue2 = ((Integer) initStatusAndMetricsResult.second).intValue();
                    try {
                        DetectorSession.this.mCallback.onStatusReported(intValue);
                        if (DetectorSession.this.getDetectorType() != 3) {
                            HotwordMetricsLogger.writeServiceInitResultEvent(DetectorSession.this.getDetectorType(), intValue2, DetectorSession.this.mVoiceInteractionServiceUid);
                        }
                    } catch (RemoteException e) {
                        Slog.w(DetectorSession.TAG, "Failed to report initialization status: " + e);
                        if (DetectorSession.this.getDetectorType() != 3) {
                            HotwordMetricsLogger.writeDetectorEvent(DetectorSession.this.getDetectorType(), 14, DetectorSession.this.mVoiceInteractionServiceUid);
                        }
                        DetectorSession.this.notifyOnDetectorRemoteException();
                    }
                }
            });
            if (getDetectorType() != 3) {
                HotwordMetricsLogger.writeDetectorEvent(getDetectorType(), 4, this.mVoiceInteractionServiceUid);
            }
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to updateState for HotwordDetectionService", e);
            if (getDetectorType() != 3) {
                HotwordMetricsLogger.writeDetectorEvent(getDetectorType(), 19, this.mVoiceInteractionServiceUid);
            }
        }
        return androidFuture.orTimeout(MAX_UPDATE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateStateAfterProcessStartLocked$1(Void r4, Throwable th) {
        if (!(th instanceof TimeoutException)) {
            if (th != null) {
                Slog.w(TAG, "Failed to update state: " + th);
                return;
            }
            return;
        }
        Slog.w(TAG, "updateState timed out");
        if (this.mUpdateStateAfterStartFinished.getAndSet(true)) {
            return;
        }
        try {
            this.mCallback.onStatusReported(100);
            if (getDetectorType() != 3) {
                HotwordMetricsLogger.writeServiceInitResultEvent(getDetectorType(), 4, this.mVoiceInteractionServiceUid);
            }
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to report initialization status UNKNOWN", e);
            if (getDetectorType() != 3) {
                HotwordMetricsLogger.writeDetectorEvent(getDetectorType(), 14, this.mVoiceInteractionServiceUid);
            }
            notifyOnDetectorRemoteException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Pair<Integer, Integer> getInitStatusAndMetricsResult(Bundle bundle) {
        if (bundle == null) {
            return new Pair<>(100, 2);
        }
        int i = bundle.getInt("initialization_status", 100);
        if (i > HotwordDetectionService.getMaxCustomInitializationStatus()) {
            return new Pair<>(100, Integer.valueOf(i != 100 ? 3 : 2));
        }
        return new Pair<>(Integer.valueOf(i), Integer.valueOf(i == 0 ? 0 : 1));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateStateLocked(final PersistableBundle persistableBundle, final SharedMemory sharedMemory, Instant instant) {
        if (getDetectorType() != 3) {
            HotwordMetricsLogger.writeDetectorEvent(getDetectorType(), 8, this.mVoiceInteractionServiceUid);
        }
        if (!this.mUpdateStateAfterStartFinished.get() && Instant.now().minus((TemporalAmount) MAX_UPDATE_TIMEOUT_DURATION).isBefore(instant)) {
            Slog.v(TAG, "call updateStateAfterProcessStartLocked");
            updateStateAfterProcessStartLocked(persistableBundle, sharedMemory);
        } else {
            this.mRemoteDetectionService.run(new ServiceConnector.VoidJob() { // from class: com.android.server.voiceinteraction.DetectorSession$$ExternalSyntheticLambda0
                public final void runNoResult(Object obj) {
                    ((ISandboxedDetectionService) obj).updateState(persistableBundle, sharedMemory, null);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startListeningFromExternalSourceLocked(ParcelFileDescriptor parcelFileDescriptor, AudioFormat audioFormat, PersistableBundle persistableBundle, IMicrophoneHotwordDetectionVoiceInteractionCallback iMicrophoneHotwordDetectionVoiceInteractionCallback) {
        handleExternalSourceHotwordDetectionLocked(parcelFileDescriptor, audioFormat, persistableBundle, iMicrophoneHotwordDetectionVoiceInteractionCallback);
    }

    private void handleExternalSourceHotwordDetectionLocked(ParcelFileDescriptor parcelFileDescriptor, final AudioFormat audioFormat, final PersistableBundle persistableBundle, final IMicrophoneHotwordDetectionVoiceInteractionCallback iMicrophoneHotwordDetectionVoiceInteractionCallback) {
        if (this.mPerformingExternalSourceHotwordDetection) {
            Slog.i(TAG, "Hotword validation is already in progress for external source.");
            return;
        }
        final ParcelFileDescriptor.AutoCloseInputStream autoCloseInputStream = new ParcelFileDescriptor.AutoCloseInputStream(parcelFileDescriptor);
        Pair<ParcelFileDescriptor, ParcelFileDescriptor> createPipe = createPipe();
        if (createPipe == null) {
            return;
        }
        final ParcelFileDescriptor parcelFileDescriptor2 = (ParcelFileDescriptor) createPipe.second;
        final ParcelFileDescriptor parcelFileDescriptor3 = (ParcelFileDescriptor) createPipe.first;
        this.mCurrentAudioSink = parcelFileDescriptor2;
        this.mPerformingExternalSourceHotwordDetection = true;
        this.mAudioCopyExecutor.execute(new Runnable() { // from class: com.android.server.voiceinteraction.DetectorSession$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                DetectorSession.this.lambda$handleExternalSourceHotwordDetectionLocked$3(autoCloseInputStream, parcelFileDescriptor2, iMicrophoneHotwordDetectionVoiceInteractionCallback);
            }
        });
        this.mRemoteDetectionService.run(new ServiceConnector.VoidJob() { // from class: com.android.server.voiceinteraction.DetectorSession$$ExternalSyntheticLambda6
            public final void runNoResult(Object obj) {
                DetectorSession.this.lambda$handleExternalSourceHotwordDetectionLocked$4(parcelFileDescriptor3, audioFormat, persistableBundle, parcelFileDescriptor2, autoCloseInputStream, iMicrophoneHotwordDetectionVoiceInteractionCallback, (ISandboxedDetectionService) obj);
            }
        });
        HotwordMetricsLogger.writeDetectorEvent(getDetectorType(), 10, this.mVoiceInteractionServiceUid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleExternalSourceHotwordDetectionLocked$3(InputStream inputStream, ParcelFileDescriptor parcelFileDescriptor, IMicrophoneHotwordDetectionVoiceInteractionCallback iMicrophoneHotwordDetectionVoiceInteractionCallback) {
        try {
            try {
                try {
                    ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream = new ParcelFileDescriptor.AutoCloseOutputStream(parcelFileDescriptor);
                    try {
                        byte[] bArr = new byte[1024];
                        while (true) {
                            int read = inputStream.read(bArr, 0, 1024);
                            if (read < 0) {
                                Slog.i(TAG, "Reached end of stream for external hotword");
                                autoCloseOutputStream.close();
                                inputStream.close();
                                synchronized (this.mLock) {
                                    this.mPerformingExternalSourceHotwordDetection = false;
                                    closeExternalAudioStreamLocked("start external source");
                                }
                                return;
                            }
                            autoCloseOutputStream.write(bArr, 0, read);
                        }
                    } finally {
                    }
                } catch (Throwable th) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (IOException e) {
                Slog.w(TAG, "Failed supplying audio data to validator", e);
                try {
                    iMicrophoneHotwordDetectionVoiceInteractionCallback.onHotwordDetectionServiceFailure(new HotwordDetectionServiceFailure(3, "Copy audio data failure for external source detection."));
                } catch (RemoteException e2) {
                    Slog.w(TAG, "Failed to report onHotwordDetectionServiceFailure status: " + e2);
                    if (getDetectorType() != 3) {
                        HotwordMetricsLogger.writeDetectorEvent(getDetectorType(), 15, this.mVoiceInteractionServiceUid);
                    }
                    notifyOnDetectorRemoteException();
                }
                synchronized (this.mLock) {
                    this.mPerformingExternalSourceHotwordDetection = false;
                    closeExternalAudioStreamLocked("start external source");
                }
            }
        } catch (Throwable th3) {
            synchronized (this.mLock) {
                this.mPerformingExternalSourceHotwordDetection = false;
                closeExternalAudioStreamLocked("start external source");
                throw th3;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleExternalSourceHotwordDetectionLocked$4(ParcelFileDescriptor parcelFileDescriptor, AudioFormat audioFormat, PersistableBundle persistableBundle, ParcelFileDescriptor parcelFileDescriptor2, InputStream inputStream, IMicrophoneHotwordDetectionVoiceInteractionCallback iMicrophoneHotwordDetectionVoiceInteractionCallback, ISandboxedDetectionService iSandboxedDetectionService) throws Exception {
        iSandboxedDetectionService.detectFromMicrophoneSource(parcelFileDescriptor, 2, audioFormat, persistableBundle, new AnonymousClass2(parcelFileDescriptor2, inputStream, iMicrophoneHotwordDetectionVoiceInteractionCallback));
        bestEffortClose(parcelFileDescriptor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.voiceinteraction.DetectorSession$2, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass2 extends IDspHotwordDetectionCallback.Stub {
        final /* synthetic */ InputStream val$audioSource;
        final /* synthetic */ IMicrophoneHotwordDetectionVoiceInteractionCallback val$callback;
        final /* synthetic */ ParcelFileDescriptor val$serviceAudioSink;

        AnonymousClass2(ParcelFileDescriptor parcelFileDescriptor, InputStream inputStream, IMicrophoneHotwordDetectionVoiceInteractionCallback iMicrophoneHotwordDetectionVoiceInteractionCallback) {
            this.val$serviceAudioSink = parcelFileDescriptor;
            this.val$audioSource = inputStream;
            this.val$callback = iMicrophoneHotwordDetectionVoiceInteractionCallback;
        }

        public void onRejected(HotwordRejectedResult hotwordRejectedResult) throws RemoteException {
            synchronized (DetectorSession.this.mLock) {
                DetectorSession detectorSession = DetectorSession.this;
                detectorSession.mPerformingExternalSourceHotwordDetection = false;
                HotwordMetricsLogger.writeDetectorEvent(detectorSession.getDetectorType(), 12, DetectorSession.this.mVoiceInteractionServiceUid);
                ScheduledExecutorService scheduledExecutorService = DetectorSession.this.mScheduledExecutorService;
                final ParcelFileDescriptor parcelFileDescriptor = this.val$serviceAudioSink;
                final InputStream inputStream = this.val$audioSource;
                scheduledExecutorService.schedule(new Runnable() { // from class: com.android.server.voiceinteraction.DetectorSession$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        DetectorSession.AnonymousClass2.lambda$onRejected$0(parcelFileDescriptor, inputStream);
                    }
                }, DetectorSession.EXTERNAL_HOTWORD_CLEANUP_MILLIS, TimeUnit.MILLISECONDS);
                try {
                    this.val$callback.onRejected(hotwordRejectedResult);
                    if (hotwordRejectedResult != null) {
                        Slog.i(DetectorSession.TAG, "Egressed 'hotword rejected result' from hotword trusted process");
                        if (DetectorSession.this.mDebugHotwordLogging) {
                            Slog.i(DetectorSession.TAG, "Egressed detected result: " + hotwordRejectedResult);
                        }
                    }
                } catch (RemoteException e) {
                    DetectorSession.this.notifyOnDetectorRemoteException();
                    throw e;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onRejected$0(ParcelFileDescriptor parcelFileDescriptor, InputStream inputStream) {
            DetectorSession.bestEffortClose(parcelFileDescriptor, inputStream);
        }

        public void onDetected(HotwordDetectedResult hotwordDetectedResult) throws RemoteException {
            synchronized (DetectorSession.this.mLock) {
                DetectorSession detectorSession = DetectorSession.this;
                detectorSession.mPerformingExternalSourceHotwordDetection = false;
                HotwordMetricsLogger.writeDetectorEvent(detectorSession.getDetectorType(), 11, DetectorSession.this.mVoiceInteractionServiceUid);
                ScheduledExecutorService scheduledExecutorService = DetectorSession.this.mScheduledExecutorService;
                final ParcelFileDescriptor parcelFileDescriptor = this.val$serviceAudioSink;
                final InputStream inputStream = this.val$audioSource;
                scheduledExecutorService.schedule(new Runnable() { // from class: com.android.server.voiceinteraction.DetectorSession$2$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        DetectorSession.AnonymousClass2.lambda$onDetected$1(parcelFileDescriptor, inputStream);
                    }
                }, DetectorSession.EXTERNAL_HOTWORD_CLEANUP_MILLIS, TimeUnit.MILLISECONDS);
                try {
                    DetectorSession.this.enforcePermissionsForDataDelivery();
                    try {
                        HotwordDetectedResult startCopyingAudioStreams = DetectorSession.this.mHotwordAudioStreamCopier.startCopyingAudioStreams(hotwordDetectedResult);
                        try {
                            this.val$callback.onDetected(startCopyingAudioStreams, (AudioFormat) null, (ParcelFileDescriptor) null);
                            Slog.i(DetectorSession.TAG, "Egressed " + HotwordDetectedResult.getUsageSize(startCopyingAudioStreams) + " bits from hotword trusted process");
                            if (DetectorSession.this.mDebugHotwordLogging) {
                                Slog.i(DetectorSession.TAG, "Egressed detected result: " + startCopyingAudioStreams);
                            }
                        } catch (RemoteException e) {
                            DetectorSession.this.notifyOnDetectorRemoteException();
                            throw e;
                        }
                    } catch (IOException e2) {
                        Slog.w(DetectorSession.TAG, "Ignoring #onDetected due to a IOException", e2);
                        try {
                            this.val$callback.onHotwordDetectionServiceFailure(new HotwordDetectionServiceFailure(6, "Copy audio stream failure."));
                        } catch (RemoteException e3) {
                            DetectorSession.this.notifyOnDetectorRemoteException();
                            throw e3;
                        }
                    }
                } catch (SecurityException e4) {
                    Slog.w(DetectorSession.TAG, "Ignoring #onDetected due to a SecurityException", e4);
                    HotwordMetricsLogger.writeDetectorEvent(DetectorSession.this.getDetectorType(), 13, DetectorSession.this.mVoiceInteractionServiceUid);
                    try {
                        this.val$callback.onHotwordDetectionServiceFailure(new HotwordDetectionServiceFailure(5, "Security exception occurs in #onDetected method"));
                    } catch (RemoteException e5) {
                        DetectorSession.this.notifyOnDetectorRemoteException();
                        throw e5;
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onDetected$1(ParcelFileDescriptor parcelFileDescriptor, InputStream inputStream) {
            DetectorSession.bestEffortClose(parcelFileDescriptor, inputStream);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initialize(PersistableBundle persistableBundle, SharedMemory sharedMemory) {
        synchronized (this.mLock) {
            if (!this.mInitialized && !this.mDestroyed) {
                updateStateAfterProcessStartLocked(persistableBundle, sharedMemory);
                this.mInitialized = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroyLocked() {
        this.mDestroyed = true;
        this.mDebugHotwordLogging = false;
        this.mRemoteDetectionService = null;
        this.mRemoteExceptionListener = null;
        AttentionManagerInternal attentionManagerInternal = this.mAttentionManagerInternal;
        if (attentionManagerInternal != null) {
            attentionManagerInternal.onStopProximityUpdates(this.mProximityCallbackInternal);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDebugHotwordLoggingLocked(boolean z) {
        Slog.v(TAG, "setDebugHotwordLoggingLocked: " + z);
        this.mDebugHotwordLogging = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateRemoteSandboxedDetectionServiceLocked(HotwordDetectionConnection.ServiceConnection serviceConnection) {
        this.mRemoteDetectionService = serviceConnection;
    }

    private void reportErrorGetRemoteException() {
        if (getDetectorType() != 3) {
            HotwordMetricsLogger.writeDetectorEvent(getDetectorType(), 15, this.mVoiceInteractionServiceUid);
        }
        notifyOnDetectorRemoteException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportErrorLocked(HotwordDetectionServiceFailure hotwordDetectionServiceFailure) {
        try {
            this.mCallback.onHotwordDetectionServiceFailure(hotwordDetectionServiceFailure);
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to call onHotwordDetectionServiceFailure: " + e);
            reportErrorGetRemoteException();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportErrorLocked(VisualQueryDetectionServiceFailure visualQueryDetectionServiceFailure) {
        try {
            this.mCallback.onVisualQueryDetectionServiceFailure(visualQueryDetectionServiceFailure);
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to call onVisualQueryDetectionServiceFailure: " + e);
            reportErrorGetRemoteException();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportErrorLocked(String str) {
        try {
            this.mCallback.onUnknownFailure(str);
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to call onUnknownFailure: " + e);
            reportErrorGetRemoteException();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSameCallback(IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback) {
        synchronized (this.mLock) {
            if (iHotwordRecognitionStatusCallback == null) {
                return false;
            }
            return this.mCallback.asBinder().equals(iHotwordRecognitionStatusCallback.asBinder());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSameToken(IBinder iBinder) {
        synchronized (this.mLock) {
            if (iBinder == null) {
                return false;
            }
            return this.mToken == iBinder;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDestroyed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mDestroyed;
        }
        return z;
    }

    private static Pair<ParcelFileDescriptor, ParcelFileDescriptor> createPipe() {
        try {
            ParcelFileDescriptor[] createPipe = ParcelFileDescriptor.createPipe();
            return Pair.create(createPipe[0], createPipe[1]);
        } catch (IOException e) {
            Slog.e(TAG, "Failed to create audio stream pipe", e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveProximityValueToBundle(HotwordDetectedResult hotwordDetectedResult) {
        synchronized (this.mLock) {
            if (hotwordDetectedResult != null) {
                double d = this.mProximityMeters;
                if (d != -1.0d) {
                    hotwordDetectedResult.setProximity(d);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setProximityValue(double d) {
        synchronized (this.mLock) {
            this.mProximityMeters = d;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeExternalAudioStreamLocked(String str) {
        if (this.mCurrentAudioSink != null) {
            Slog.i(TAG, "Closing external audio stream to hotword detector: " + str);
            bestEffortClose(this.mCurrentAudioSink);
            this.mCurrentAudioSink = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void bestEffortClose(Closeable... closeableArr) {
        for (Closeable closeable : closeableArr) {
            bestEffortClose(closeable);
        }
    }

    private static void bestEffortClose(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enforcePermissionsForDataDelivery() {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.voiceinteraction.DetectorSession$$ExternalSyntheticLambda2
            public final void runOrThrow() {
                DetectorSession.this.lambda$enforcePermissionsForDataDelivery$5();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enforcePermissionsForDataDelivery$5() throws Exception {
        synchronized (this.mLock) {
            if (AppOpsPolicy.isHotwordDetectionServiceRequired(this.mContext.getPackageManager())) {
                Context context = this.mContext;
                Identity identity = this.mVoiceInteractorIdentity;
                if (PermissionChecker.checkPermissionForPreflight(context, "android.permission.RECORD_AUDIO", -1, identity.uid, identity.packageName) != 0) {
                    throw new SecurityException("Failed to obtain permission RECORD_AUDIO for identity " + this.mVoiceInteractorIdentity);
                }
                int strOpToOp = AppOpsManager.strOpToOp("android:record_audio_hotword");
                AppOpsManager appOpsManager = this.mAppOpsManager;
                Identity identity2 = this.mVoiceInteractorIdentity;
                appOpsManager.noteOpNoThrow(strOpToOp, identity2.uid, identity2.packageName, identity2.attributionTag, HOTWORD_DETECTION_OP_MESSAGE);
            } else {
                enforcePermissionForDataDelivery(this.mContext, this.mVoiceInteractorIdentity, "android.permission.RECORD_AUDIO", HOTWORD_DETECTION_OP_MESSAGE);
            }
            enforcePermissionForDataDelivery(this.mContext, this.mVoiceInteractorIdentity, "android.permission.CAPTURE_AUDIO_HOTWORD", HOTWORD_DETECTION_OP_MESSAGE);
        }
    }

    private static void enforcePermissionForDataDelivery(Context context, Identity identity, String str, String str2) {
        if (PermissionUtil.checkPermissionForDataDelivery(context, identity, str, str2) != 0) {
            throw new SecurityException(TextUtils.formatSimple("Failed to obtain permission %s for identity %s", new Object[]{str, identity}));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @RequiresPermission(allOf = {"android.permission.READ_COMPAT_CHANGE_CONFIG", "android.permission.LOG_COMPAT_CHANGE"})
    public void enforceExtraKeyphraseIdNotLeaked(HotwordDetectedResult hotwordDetectedResult, SoundTrigger.KeyphraseRecognitionEvent keyphraseRecognitionEvent) {
        if (CompatChanges.isChangeEnabled(HotwordDetectionConnection.ENFORCE_HOTWORD_PHRASE_ID, this.mVoiceInteractionServiceUid)) {
            for (SoundTrigger.KeyphraseRecognitionExtra keyphraseRecognitionExtra : keyphraseRecognitionEvent.keyphraseExtras) {
                if (keyphraseRecognitionExtra.getKeyphraseId() == hotwordDetectedResult.getHotwordPhraseId()) {
                    return;
                }
            }
            throw new SecurityException("Ignoring #onDetected due to trusted service sharing a keyphrase ID which the DSP did not detect");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDetectorType() {
        if (this instanceof DspTrustedHotwordDetectorSession) {
            return 1;
        }
        if (this instanceof SoftwareTrustedHotwordDetectorSession) {
            return 2;
        }
        if (this instanceof VisualQueryDetectorSession) {
            return 3;
        }
        Slog.v(TAG, "Unexpected detector type");
        return -1;
    }

    public void dumpLocked(String str, PrintWriter printWriter) {
        printWriter.print(str);
        printWriter.print("mCallback=");
        printWriter.println(this.mCallback);
        printWriter.print(str);
        printWriter.print("mUpdateStateAfterStartFinished=");
        printWriter.println(this.mUpdateStateAfterStartFinished);
        printWriter.print(str);
        printWriter.print("mInitialized=");
        printWriter.println(this.mInitialized);
        printWriter.print(str);
        printWriter.print("mDestroyed=");
        printWriter.println(this.mDestroyed);
        printWriter.print(str);
        printWriter.print("DetectorType=");
        printWriter.println(HotwordDetector.detectorTypeToString(getDetectorType()));
        printWriter.print(str);
        printWriter.print("mPerformingExternalSourceHotwordDetection=");
        printWriter.println(this.mPerformingExternalSourceHotwordDetection);
    }
}
