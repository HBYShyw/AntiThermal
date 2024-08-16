package com.android.server.musicrecognition;

import android.content.ComponentName;
import android.content.Context;
import android.media.AudioFormat;
import android.media.musicrecognition.IMusicRecognitionAttributionTagCallback;
import android.media.musicrecognition.IMusicRecognitionService;
import android.os.IBinder;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService;
import com.android.internal.infra.AbstractRemoteService;
import com.android.server.musicrecognition.MusicRecognitionManagerPerUserService;
import com.android.server.pm.DumpState;
import java.util.concurrent.CompletableFuture;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RemoteMusicRecognitionService extends AbstractMultiplePendingRequestsRemoteService<RemoteMusicRecognitionService, IMusicRecognitionService> {
    private static final long TIMEOUT_IDLE_BIND_MILLIS = 40000;
    private final MusicRecognitionManagerPerUserService.MusicRecognitionServiceCallback mServerCallback;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    interface Callbacks extends AbstractRemoteService.VultureCallback<RemoteMusicRecognitionService> {
    }

    protected long getTimeoutIdleBindMillis() {
        return TIMEOUT_IDLE_BIND_MILLIS;
    }

    public RemoteMusicRecognitionService(Context context, ComponentName componentName, int i, MusicRecognitionManagerPerUserService musicRecognitionManagerPerUserService, MusicRecognitionManagerPerUserService.MusicRecognitionServiceCallback musicRecognitionServiceCallback, boolean z, boolean z2) {
        super(context, "android.service.musicrecognition.MUSIC_RECOGNITION", componentName, i, musicRecognitionManagerPerUserService, context.getMainThreadHandler(), (z ? DumpState.DUMP_CHANGES : 0) | 4096, z2, 1);
        this.mServerCallback = musicRecognitionServiceCallback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: getServiceInterface, reason: merged with bridge method [inline-methods] */
    public IMusicRecognitionService m1373getServiceInterface(IBinder iBinder) {
        return IMusicRecognitionService.Stub.asInterface(iBinder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MusicRecognitionManagerPerUserService.MusicRecognitionServiceCallback getServerCallback() {
        return this.mServerCallback;
    }

    public void onAudioStreamStarted(final ParcelFileDescriptor parcelFileDescriptor, final AudioFormat audioFormat) {
        scheduleAsyncRequest(new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.musicrecognition.RemoteMusicRecognitionService$$ExternalSyntheticLambda1
            public final void run(IInterface iInterface) {
                RemoteMusicRecognitionService.this.lambda$onAudioStreamStarted$0(parcelFileDescriptor, audioFormat, (IMusicRecognitionService) iInterface);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAudioStreamStarted$0(ParcelFileDescriptor parcelFileDescriptor, AudioFormat audioFormat, IMusicRecognitionService iMusicRecognitionService) throws RemoteException {
        iMusicRecognitionService.onAudioStreamStarted(parcelFileDescriptor, audioFormat, this.mServerCallback);
    }

    public CompletableFuture<String> getAttributionTag() {
        final CompletableFuture<String> completableFuture = new CompletableFuture<>();
        scheduleAsyncRequest(new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.musicrecognition.RemoteMusicRecognitionService$$ExternalSyntheticLambda0
            public final void run(IInterface iInterface) {
                RemoteMusicRecognitionService.this.lambda$getAttributionTag$1(completableFuture, (IMusicRecognitionService) iInterface);
            }
        });
        return completableFuture;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getAttributionTag$1(final CompletableFuture completableFuture, IMusicRecognitionService iMusicRecognitionService) throws RemoteException {
        iMusicRecognitionService.getAttributionTag(new IMusicRecognitionAttributionTagCallback.Stub() { // from class: com.android.server.musicrecognition.RemoteMusicRecognitionService.1
            public void onAttributionTag(String str) throws RemoteException {
                completableFuture.complete(str);
            }
        });
    }
}
