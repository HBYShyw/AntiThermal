package com.android.server.contentcapture;

import android.content.ComponentName;
import android.content.Context;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.os.IBinder;
import android.os.IInterface;
import android.service.contentcapture.ActivityEvent;
import android.service.contentcapture.IContentCaptureService;
import android.service.contentcapture.IContentCaptureServiceCallback;
import android.service.contentcapture.IDataShareCallback;
import android.service.contentcapture.SnapshotData;
import android.util.EventLog;
import android.util.Slog;
import android.view.contentcapture.ContentCaptureContext;
import android.view.contentcapture.ContentCaptureHelper;
import android.view.contentcapture.DataRemovalRequest;
import android.view.contentcapture.DataShareRequest;
import com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService;
import com.android.internal.infra.AbstractRemoteService;
import com.android.internal.os.IResultReceiver;
import com.android.internal.util.CollectionUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class RemoteContentCaptureService extends AbstractMultiplePendingRequestsRemoteService<RemoteContentCaptureService, IContentCaptureService> {
    private final int mIdleUnbindTimeoutMs;
    private final ContentCapturePerUserService mPerUserService;
    private final IBinder mServerCallback;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface ContentCaptureServiceCallbacks extends AbstractRemoteService.VultureCallback<RemoteContentCaptureService> {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteContentCaptureService(Context context, String str, ComponentName componentName, IContentCaptureServiceCallback iContentCaptureServiceCallback, int i, ContentCapturePerUserService contentCapturePerUserService, boolean z, boolean z2, int i2) {
        super(context, str, componentName, i, contentCapturePerUserService, context.getMainThreadHandler(), (z ? AudioDevice.OUT_SPEAKER_SAFE : 0) | 4096, z2, 2);
        this.mPerUserService = contentCapturePerUserService;
        this.mServerCallback = iContentCaptureServiceCallback.asBinder();
        this.mIdleUnbindTimeoutMs = i2;
        ensureBoundLocked();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IContentCaptureService getServiceInterface(IBinder iBinder) {
        return IContentCaptureService.Stub.asInterface(iBinder);
    }

    protected long getTimeoutIdleBindMillis() {
        return this.mIdleUnbindTimeoutMs;
    }

    protected void handleOnConnectedStateChanged(boolean z) {
        if (z && getTimeoutIdleBindMillis() != 0) {
            scheduleUnbind();
        }
        try {
            if (z) {
                try {
                    ((AbstractMultiplePendingRequestsRemoteService) this).mService.onConnected(this.mServerCallback, ContentCaptureHelper.sVerbose, ContentCaptureHelper.sDebug);
                    ContentCaptureMetricsLogger.writeServiceEvent(1, ((AbstractMultiplePendingRequestsRemoteService) this).mComponentName);
                    EventLog.writeEvent(EventLogTags.CC_CONNECT_STATE_CHANGED, Integer.valueOf(this.mPerUserService.getUserId()), 1, Integer.valueOf(CollectionUtils.size(this.mPerUserService.getContentCaptureAllowlist())));
                    this.mPerUserService.onConnected();
                    return;
                } catch (Throwable th) {
                    this.mPerUserService.onConnected();
                    throw th;
                }
            }
            ((AbstractMultiplePendingRequestsRemoteService) this).mService.onDisconnected();
            ContentCaptureMetricsLogger.writeServiceEvent(2, ((AbstractMultiplePendingRequestsRemoteService) this).mComponentName);
            EventLog.writeEvent(EventLogTags.CC_CONNECT_STATE_CHANGED, Integer.valueOf(this.mPerUserService.getUserId()), 2, 0);
        } catch (Exception e) {
            Slog.w(((AbstractMultiplePendingRequestsRemoteService) this).mTag, "Exception calling onConnectedStateChanged(" + z + "): " + e);
        }
    }

    public void ensureBoundLocked() {
        scheduleBind();
    }

    public void onSessionStarted(final ContentCaptureContext contentCaptureContext, final int i, final int i2, final IResultReceiver iResultReceiver, final int i3) {
        scheduleAsyncRequest(new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentcapture.RemoteContentCaptureService$$ExternalSyntheticLambda1
            public final void run(IInterface iInterface) {
                ((IContentCaptureService) iInterface).onSessionStarted(contentCaptureContext, i, i2, iResultReceiver, i3);
            }
        });
        ContentCaptureMetricsLogger.writeSessionEvent(i, 1, i3, getComponentName(), false);
    }

    public void onSessionFinished(final int i) {
        scheduleAsyncRequest(new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentcapture.RemoteContentCaptureService$$ExternalSyntheticLambda0
            public final void run(IInterface iInterface) {
                ((IContentCaptureService) iInterface).onSessionFinished(i);
            }
        });
        ContentCaptureMetricsLogger.writeSessionEvent(i, 2, 0, getComponentName(), false);
    }

    public void onActivitySnapshotRequest(final int i, final SnapshotData snapshotData) {
        scheduleAsyncRequest(new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentcapture.RemoteContentCaptureService$$ExternalSyntheticLambda4
            public final void run(IInterface iInterface) {
                ((IContentCaptureService) iInterface).onActivitySnapshot(i, snapshotData);
            }
        });
    }

    public void onDataRemovalRequest(final DataRemovalRequest dataRemovalRequest) {
        scheduleAsyncRequest(new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentcapture.RemoteContentCaptureService$$ExternalSyntheticLambda2
            public final void run(IInterface iInterface) {
                ((IContentCaptureService) iInterface).onDataRemovalRequest(dataRemovalRequest);
            }
        });
        ContentCaptureMetricsLogger.writeServiceEvent(5, ((AbstractMultiplePendingRequestsRemoteService) this).mComponentName);
    }

    public void onDataShareRequest(final DataShareRequest dataShareRequest, final IDataShareCallback.Stub stub) {
        scheduleAsyncRequest(new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentcapture.RemoteContentCaptureService$$ExternalSyntheticLambda3
            public final void run(IInterface iInterface) {
                ((IContentCaptureService) iInterface).onDataShared(dataShareRequest, stub);
            }
        });
        ContentCaptureMetricsLogger.writeServiceEvent(6, ((AbstractMultiplePendingRequestsRemoteService) this).mComponentName);
    }

    public void onActivityLifecycleEvent(final ActivityEvent activityEvent) {
        scheduleAsyncRequest(new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentcapture.RemoteContentCaptureService$$ExternalSyntheticLambda5
            public final void run(IInterface iInterface) {
                ((IContentCaptureService) iInterface).onActivityEvent(activityEvent);
            }
        });
    }
}
