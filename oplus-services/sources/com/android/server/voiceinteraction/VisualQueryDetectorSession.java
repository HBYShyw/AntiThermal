package com.android.server.voiceinteraction;

import android.content.Context;
import android.media.AudioFormat;
import android.media.permission.Identity;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.service.voice.IDetectorSessionVisualQueryDetectionCallback;
import android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback;
import android.service.voice.ISandboxedDetectionService;
import android.service.voice.IVisualQueryDetectionVoiceInteractionCallback;
import android.service.voice.VisualQueryDetectionServiceFailure;
import android.util.Slog;
import com.android.internal.app.IHotwordRecognitionStatusCallback;
import com.android.internal.app.IVisualQueryDetectionAttentionListener;
import com.android.internal.infra.ServiceConnector;
import com.android.server.voiceinteraction.HotwordDetectionConnection;
import com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class VisualQueryDetectorSession extends DetectorSession {
    private static final String TAG = "VisualQueryDetectorSession";
    private IVisualQueryDetectionAttentionListener mAttentionListener;
    private boolean mEgressingData;
    private boolean mQueryStreaming;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VisualQueryDetectorSession(HotwordDetectionConnection.ServiceConnection serviceConnection, Object obj, Context context, IBinder iBinder, IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback, int i, Identity identity, ScheduledExecutorService scheduledExecutorService, boolean z, VoiceInteractionManagerServiceImpl.DetectorRemoteExceptionListener detectorRemoteExceptionListener) {
        super(serviceConnection, obj, context, iBinder, iHotwordRecognitionStatusCallback, i, identity, scheduledExecutorService, z, detectorRemoteExceptionListener);
        this.mEgressingData = false;
        this.mQueryStreaming = false;
        this.mAttentionListener = null;
    }

    @Override // com.android.server.voiceinteraction.DetectorSession
    void informRestartProcessLocked() {
        Slog.v(TAG, "informRestartProcessLocked");
        this.mUpdateStateAfterStartFinished.set(false);
        try {
            this.mCallback.onProcessRestarted();
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to communicate #onProcessRestarted", e);
            notifyOnDetectorRemoteException();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setVisualQueryDetectionAttentionListenerLocked(IVisualQueryDetectionAttentionListener iVisualQueryDetectionAttentionListener) {
        this.mAttentionListener = iVisualQueryDetectionAttentionListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startPerceivingLocked(final IVisualQueryDetectionVoiceInteractionCallback iVisualQueryDetectionVoiceInteractionCallback) {
        final IDetectorSessionVisualQueryDetectionCallback.Stub stub = new IDetectorSessionVisualQueryDetectionCallback.Stub() { // from class: com.android.server.voiceinteraction.VisualQueryDetectorSession.1
            public void onAttentionGained() {
                Slog.v(VisualQueryDetectorSession.TAG, "BinderCallback#onAttentionGained");
                VisualQueryDetectorSession.this.mEgressingData = true;
                if (VisualQueryDetectorSession.this.mAttentionListener == null) {
                    return;
                }
                try {
                    VisualQueryDetectorSession.this.mAttentionListener.onAttentionGained();
                } catch (RemoteException e) {
                    Slog.e(VisualQueryDetectorSession.TAG, "Error delivering attention gained event.", e);
                    try {
                        iVisualQueryDetectionVoiceInteractionCallback.onVisualQueryDetectionServiceFailure(new VisualQueryDetectionServiceFailure(3, "Attention listener failed to switch to GAINED state."));
                    } catch (RemoteException unused) {
                        Slog.v(VisualQueryDetectorSession.TAG, "Fail to call onVisualQueryDetectionServiceFailure");
                    }
                }
            }

            public void onAttentionLost() {
                Slog.v(VisualQueryDetectorSession.TAG, "BinderCallback#onAttentionLost");
                VisualQueryDetectorSession.this.mEgressingData = false;
                if (VisualQueryDetectorSession.this.mAttentionListener == null) {
                    return;
                }
                try {
                    VisualQueryDetectorSession.this.mAttentionListener.onAttentionLost();
                } catch (RemoteException e) {
                    Slog.e(VisualQueryDetectorSession.TAG, "Error delivering attention lost event.", e);
                    try {
                        iVisualQueryDetectionVoiceInteractionCallback.onVisualQueryDetectionServiceFailure(new VisualQueryDetectionServiceFailure(3, "Attention listener failed to switch to LOST state."));
                    } catch (RemoteException unused) {
                        Slog.v(VisualQueryDetectorSession.TAG, "Fail to call onVisualQueryDetectionServiceFailure");
                    }
                }
            }

            public void onQueryDetected(String str) throws RemoteException {
                Objects.requireNonNull(str);
                Slog.v(VisualQueryDetectorSession.TAG, "BinderCallback#onQueryDetected");
                if (!VisualQueryDetectorSession.this.mEgressingData) {
                    Slog.v(VisualQueryDetectorSession.TAG, "Query should not be egressed within the unattention state.");
                    iVisualQueryDetectionVoiceInteractionCallback.onVisualQueryDetectionServiceFailure(new VisualQueryDetectionServiceFailure(4, "Cannot stream queries without attention signals."));
                } else {
                    VisualQueryDetectorSession.this.mQueryStreaming = true;
                    iVisualQueryDetectionVoiceInteractionCallback.onQueryDetected(str);
                    Slog.i(VisualQueryDetectorSession.TAG, "Egressed from visual query detection process.");
                }
            }

            public void onQueryFinished() throws RemoteException {
                Slog.v(VisualQueryDetectorSession.TAG, "BinderCallback#onQueryFinished");
                if (!VisualQueryDetectorSession.this.mQueryStreaming) {
                    Slog.v(VisualQueryDetectorSession.TAG, "Query streaming state signal FINISHED is block since there is no active query being streamed.");
                    iVisualQueryDetectionVoiceInteractionCallback.onVisualQueryDetectionServiceFailure(new VisualQueryDetectionServiceFailure(4, "Cannot send FINISHED signal with no query streamed."));
                } else {
                    iVisualQueryDetectionVoiceInteractionCallback.onQueryFinished();
                    VisualQueryDetectorSession.this.mQueryStreaming = false;
                }
            }

            public void onQueryRejected() throws RemoteException {
                Slog.v(VisualQueryDetectorSession.TAG, "BinderCallback#onQueryRejected");
                if (!VisualQueryDetectorSession.this.mQueryStreaming) {
                    Slog.v(VisualQueryDetectorSession.TAG, "Query streaming state signal REJECTED is block since there is no active query being streamed.");
                    iVisualQueryDetectionVoiceInteractionCallback.onVisualQueryDetectionServiceFailure(new VisualQueryDetectionServiceFailure(4, "Cannot send REJECTED signal with no query streamed."));
                } else {
                    iVisualQueryDetectionVoiceInteractionCallback.onQueryRejected();
                    VisualQueryDetectorSession.this.mQueryStreaming = false;
                }
            }
        };
        this.mRemoteDetectionService.run(new ServiceConnector.VoidJob() { // from class: com.android.server.voiceinteraction.VisualQueryDetectorSession$$ExternalSyntheticLambda0
            public final void runNoResult(Object obj) {
                ((ISandboxedDetectionService) obj).detectWithVisualSignals(stub);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopPerceivingLocked() {
        this.mRemoteDetectionService.run(new SoftwareTrustedHotwordDetectorSession$$ExternalSyntheticLambda0());
    }

    @Override // com.android.server.voiceinteraction.DetectorSession
    void startListeningFromExternalSourceLocked(ParcelFileDescriptor parcelFileDescriptor, AudioFormat audioFormat, PersistableBundle persistableBundle, IMicrophoneHotwordDetectionVoiceInteractionCallback iMicrophoneHotwordDetectionVoiceInteractionCallback) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("HotwordDetectionService method should not be called from VisualQueryDetectorSession.");
    }

    @Override // com.android.server.voiceinteraction.DetectorSession
    public void dumpLocked(String str, PrintWriter printWriter) {
        super.dumpLocked(str, printWriter);
        printWriter.print(str);
    }
}
