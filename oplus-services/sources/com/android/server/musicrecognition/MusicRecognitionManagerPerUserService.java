package com.android.server.musicrecognition;

import android.app.AppGlobals;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.media.AudioRecord;
import android.media.MediaMetadata;
import android.media.musicrecognition.IMusicRecognitionManagerCallback;
import android.media.musicrecognition.IMusicRecognitionServiceCallback;
import android.media.musicrecognition.RecognitionRequest;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Pair;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.server.infra.AbstractPerUserSystemService;
import com.android.server.musicrecognition.RemoteMusicRecognitionService;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class MusicRecognitionManagerPerUserService extends AbstractPerUserSystemService<MusicRecognitionManagerPerUserService, MusicRecognitionManagerService> implements RemoteMusicRecognitionService.Callbacks {
    private static final int BYTES_PER_SAMPLE = 2;
    private static final String KEY_MUSIC_RECOGNITION_SERVICE_ATTRIBUTION_TAG = "android.media.musicrecognition.attributiontag";
    private static final int MAX_STREAMING_SECONDS = 24;
    private static final String MUSIC_RECOGNITION_MANAGER_ATTRIBUTION_TAG = "MusicRecognitionManagerService";
    private static final String TAG = MusicRecognitionManagerPerUserService.class.getSimpleName();
    private final AppOpsManager mAppOpsManager;
    private final String mAttributionMessage;
    private CompletableFuture<String> mAttributionTagFuture;

    @GuardedBy({"mLock"})
    private RemoteMusicRecognitionService mRemoteService;
    private ServiceInfo mServiceInfo;

    private static int getBufferSizeInBytes(int i, int i2) {
        return i * 2 * i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MusicRecognitionManagerPerUserService(MusicRecognitionManagerService musicRecognitionManagerService, Object obj, int i) {
        super(musicRecognitionManagerService, obj, i);
        this.mAppOpsManager = (AppOpsManager) getContext().createAttributionContext(MUSIC_RECOGNITION_MANAGER_ATTRIBUTION_TAG).getSystemService(AppOpsManager.class);
        this.mAttributionMessage = String.format("MusicRecognitionManager.invokedByUid.%s", Integer.valueOf(i));
        this.mAttributionTagFuture = null;
        this.mServiceInfo = null;
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    @GuardedBy({"mLock"})
    protected ServiceInfo newServiceInfoLocked(ComponentName componentName) throws PackageManager.NameNotFoundException {
        try {
            ServiceInfo serviceInfo = AppGlobals.getPackageManager().getServiceInfo(componentName, 128L, this.mUserId);
            if ("android.permission.BIND_MUSIC_RECOGNITION_SERVICE".equals(serviceInfo.permission)) {
                return serviceInfo;
            }
            Slog.w(TAG, "MusicRecognitionService from '" + serviceInfo.packageName + "' does not require permission android.permission.BIND_MUSIC_RECOGNITION_SERVICE");
            throw new SecurityException("Service does not require permission android.permission.BIND_MUSIC_RECOGNITION_SERVICE");
        } catch (RemoteException unused) {
            throw new PackageManager.NameNotFoundException("Could not get service for " + componentName);
        }
    }

    @GuardedBy({"mLock"})
    private RemoteMusicRecognitionService ensureRemoteServiceLocked(IMusicRecognitionManagerCallback iMusicRecognitionManagerCallback) {
        if (this.mRemoteService == null) {
            String componentNameLocked = getComponentNameLocked();
            if (componentNameLocked == null) {
                if (((MusicRecognitionManagerService) this.mMaster).verbose) {
                    Slog.v(TAG, "ensureRemoteServiceLocked(): not set");
                }
                return null;
            }
            this.mRemoteService = new RemoteMusicRecognitionService(getContext(), ComponentName.unflattenFromString(componentNameLocked), this.mUserId, this, new MusicRecognitionServiceCallback(iMusicRecognitionManagerCallback), ((MusicRecognitionManagerService) this.mMaster).isBindInstantServiceAllowed(), ((MusicRecognitionManagerService) this.mMaster).verbose);
            try {
                this.mServiceInfo = getContext().getPackageManager().getServiceInfo(this.mRemoteService.getComponentName(), 128);
                this.mAttributionTagFuture = this.mRemoteService.getAttributionTag();
                Slog.i(TAG, "Remote service bound: " + this.mRemoteService.getComponentName());
            } catch (PackageManager.NameNotFoundException e) {
                Slog.e(TAG, "Service was not found.", e);
            }
        }
        return this.mRemoteService;
    }

    @GuardedBy({"mLock"})
    public void beginRecognitionLocked(final RecognitionRequest recognitionRequest, IBinder iBinder) {
        final IMusicRecognitionManagerCallback asInterface = IMusicRecognitionManagerCallback.Stub.asInterface(iBinder);
        RemoteMusicRecognitionService ensureRemoteServiceLocked = ensureRemoteServiceLocked(asInterface);
        this.mRemoteService = ensureRemoteServiceLocked;
        if (ensureRemoteServiceLocked == null) {
            try {
                asInterface.onRecognitionFailed(3);
                return;
            } catch (RemoteException unused) {
                return;
            }
        }
        Pair<ParcelFileDescriptor, ParcelFileDescriptor> createPipe = createPipe();
        if (createPipe == null) {
            try {
                asInterface.onRecognitionFailed(7);
            } catch (RemoteException unused2) {
            }
        } else {
            final ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor) createPipe.second;
            ParcelFileDescriptor parcelFileDescriptor2 = (ParcelFileDescriptor) createPipe.first;
            this.mAttributionTagFuture.thenAcceptAsync(new Consumer() { // from class: com.android.server.musicrecognition.MusicRecognitionManagerPerUserService$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    MusicRecognitionManagerPerUserService.this.lambda$beginRecognitionLocked$0(recognitionRequest, asInterface, parcelFileDescriptor, (String) obj);
                }
            }, (Executor) ((MusicRecognitionManagerService) this.mMaster).mExecutorService);
            this.mRemoteService.onAudioStreamStarted(parcelFileDescriptor2, recognitionRequest.getAudioFormat());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: streamAudio, reason: merged with bridge method [inline-methods] */
    public void lambda$beginRecognitionLocked$0(String str, RecognitionRequest recognitionRequest, IMusicRecognitionManagerCallback iMusicRecognitionManagerCallback, ParcelFileDescriptor parcelFileDescriptor) {
        int min = Math.min(recognitionRequest.getMaxAudioLengthSeconds(), MAX_STREAMING_SECONDS);
        if (min <= 0) {
            Slog.i(TAG, "No audio requested. Closing stream.");
            try {
                parcelFileDescriptor.close();
                iMusicRecognitionManagerCallback.onAudioStreamClosed();
                return;
            } catch (RemoteException unused) {
                return;
            } catch (IOException e) {
                Slog.e(TAG, "Problem closing stream.", e);
                return;
            }
        }
        try {
            startRecordAudioOp(str);
            AudioRecord createAudioRecord = createAudioRecord(recognitionRequest, min);
            try {
                try {
                    ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream = new ParcelFileDescriptor.AutoCloseOutputStream(parcelFileDescriptor);
                    try {
                        streamAudio(recognitionRequest, min, createAudioRecord, autoCloseOutputStream);
                        autoCloseOutputStream.close();
                    } catch (Throwable th) {
                        try {
                            autoCloseOutputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (IOException e2) {
                    Slog.e(TAG, "Audio streaming stopped.", e2);
                }
                try {
                    iMusicRecognitionManagerCallback.onAudioStreamClosed();
                } catch (RemoteException unused2) {
                }
            } finally {
                createAudioRecord.release();
                finishRecordAudioOp(str);
                try {
                    iMusicRecognitionManagerCallback.onAudioStreamClosed();
                } catch (RemoteException unused3) {
                }
            }
        } catch (SecurityException e3) {
            Slog.e(TAG, "RECORD_AUDIO op not permitted on behalf of " + this.mServiceInfo.getComponentName(), e3);
            try {
                iMusicRecognitionManagerCallback.onRecognitionFailed(7);
            } catch (RemoteException unused4) {
            }
        }
    }

    private void streamAudio(RecognitionRequest recognitionRequest, int i, AudioRecord audioRecord, OutputStream outputStream) throws IOException {
        int bufferSizeInFrames = audioRecord.getBufferSizeInFrames() / i;
        byte[] bArr = new byte[bufferSizeInFrames];
        int ignoreBeginningFrames = recognitionRequest.getIgnoreBeginningFrames() * 2;
        audioRecord.startRecording();
        int i2 = 0;
        int i3 = 0;
        while (i2 >= 0 && i3 < audioRecord.getBufferSizeInFrames() * 2 && this.mRemoteService != null) {
            i2 = audioRecord.read(bArr, 0, bufferSizeInFrames);
            if (i2 > 0) {
                i3 += i2;
                if (ignoreBeginningFrames > 0) {
                    ignoreBeginningFrames -= i2;
                    if (ignoreBeginningFrames < 0) {
                        outputStream.write(bArr, i2 + ignoreBeginningFrames, -ignoreBeginningFrames);
                    }
                } else {
                    outputStream.write(bArr);
                }
            }
        }
        Slog.i(TAG, String.format("Streamed %s bytes from audio record", Integer.valueOf(i3)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class MusicRecognitionServiceCallback extends IMusicRecognitionServiceCallback.Stub {
        private final IMusicRecognitionManagerCallback mClientCallback;

        private MusicRecognitionServiceCallback(IMusicRecognitionManagerCallback iMusicRecognitionManagerCallback) {
            this.mClientCallback = iMusicRecognitionManagerCallback;
        }

        public void onRecognitionSucceeded(MediaMetadata mediaMetadata, Bundle bundle) {
            try {
                MusicRecognitionManagerPerUserService.sanitizeBundle(bundle);
                this.mClientCallback.onRecognitionSucceeded(mediaMetadata, bundle);
            } catch (RemoteException unused) {
            }
            MusicRecognitionManagerPerUserService.this.destroyService();
        }

        public void onRecognitionFailed(int i) {
            try {
                this.mClientCallback.onRecognitionFailed(i);
            } catch (RemoteException unused) {
            }
            MusicRecognitionManagerPerUserService.this.destroyService();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IMusicRecognitionManagerCallback getClientCallback() {
            return this.mClientCallback;
        }
    }

    public void onServiceDied(RemoteMusicRecognitionService remoteMusicRecognitionService) {
        try {
            remoteMusicRecognitionService.getServerCallback().getClientCallback().onRecognitionFailed(5);
        } catch (RemoteException unused) {
        }
        Slog.w(TAG, "remote service died: " + remoteMusicRecognitionService);
        destroyService();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void destroyService() {
        synchronized (this.mLock) {
            RemoteMusicRecognitionService remoteMusicRecognitionService = this.mRemoteService;
            if (remoteMusicRecognitionService != null) {
                remoteMusicRecognitionService.destroy();
                this.mRemoteService = null;
            }
        }
    }

    private void startRecordAudioOp(String str) {
        AppOpsManager appOpsManager = this.mAppOpsManager;
        String permissionToOp = AppOpsManager.permissionToOp("android.permission.RECORD_AUDIO");
        Objects.requireNonNull(permissionToOp);
        ServiceInfo serviceInfo = this.mServiceInfo;
        int startProxyOp = appOpsManager.startProxyOp(permissionToOp, serviceInfo.applicationInfo.uid, serviceInfo.packageName, str, this.mAttributionMessage);
        if (startProxyOp != 0) {
            throw new SecurityException(String.format("Failed to obtain RECORD_AUDIO permission (status: %d) for receiving service: %s", Integer.valueOf(startProxyOp), this.mServiceInfo.getComponentName()));
        }
        String str2 = TAG;
        ServiceInfo serviceInfo2 = this.mServiceInfo;
        Slog.i(str2, String.format("Starting audio streaming. Attributing to %s (%d) with tag '%s'", serviceInfo2.packageName, Integer.valueOf(serviceInfo2.applicationInfo.uid), str));
    }

    private void finishRecordAudioOp(String str) {
        AppOpsManager appOpsManager = this.mAppOpsManager;
        String permissionToOp = AppOpsManager.permissionToOp("android.permission.RECORD_AUDIO");
        Objects.requireNonNull(permissionToOp);
        ServiceInfo serviceInfo = this.mServiceInfo;
        appOpsManager.finishProxyOp(permissionToOp, serviceInfo.applicationInfo.uid, serviceInfo.packageName, str);
    }

    private static AudioRecord createAudioRecord(RecognitionRequest recognitionRequest, int i) {
        return new AudioRecord(recognitionRequest.getAudioAttributes(), recognitionRequest.getAudioFormat(), getBufferSizeInBytes(recognitionRequest.getAudioFormat().getSampleRate(), i), recognitionRequest.getCaptureSession());
    }

    private static Pair<ParcelFileDescriptor, ParcelFileDescriptor> createPipe() {
        try {
            ParcelFileDescriptor[] createPipe = ParcelFileDescriptor.createPipe();
            if (createPipe.length != 2) {
                Slog.e(TAG, "Failed to create audio stream pipe, unexpected number of file descriptors");
                return null;
            }
            if (!createPipe[0].getFileDescriptor().valid() || !createPipe[1].getFileDescriptor().valid()) {
                Slog.e(TAG, "Failed to create audio stream pipe, didn't receive a pair of valid file descriptors.");
                return null;
            }
            return Pair.create(createPipe[0], createPipe[1]);
        } catch (IOException e) {
            Slog.e(TAG, "Failed to create audio stream pipe", e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sanitizeBundle(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            if (obj instanceof Bundle) {
                sanitizeBundle((Bundle) obj);
            } else if ((obj instanceof IBinder) || (obj instanceof ParcelFileDescriptor)) {
                bundle.remove(str);
            }
        }
    }
}
