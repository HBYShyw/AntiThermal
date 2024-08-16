package com.android.server.wm;

import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.projection.IMediaProjectionManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.DeviceConfig;
import android.util.Slog;
import android.view.ContentRecordingSession;
import android.view.SurfaceControl;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ContentRecorder implements WindowContainerListener {

    @VisibleForTesting
    static final String KEY_RECORD_TASK_FEATURE = "record_task_content";
    private static final String TAG = "ContentRecorder";
    private ContentRecorderWrapper mCRrapper;
    public IContentRecorderExt mContentRecorderExt;
    private ContentRecordingSession mContentRecordingSession;
    private final DisplayContent mDisplayContent;
    private int mLastOrientation;
    private Rect mLastRecordedBounds;
    private final MediaProjectionManagerWrapper mMediaProjectionManager;
    private SurfaceControl mRecordedSurface;
    private WindowContainer mRecordedWindowContainer;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface MediaProjectionManagerWrapper {
        void notifyActiveProjectionCapturedContentResized(int i, int i2);

        void notifyActiveProjectionCapturedContentVisibilityChanged(boolean z);

        void stopActiveProjection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentRecorder(DisplayContent displayContent) {
        this(displayContent, new RemoteMediaProjectionManagerWrapper());
    }

    @VisibleForTesting
    ContentRecorder(DisplayContent displayContent, MediaProjectionManagerWrapper mediaProjectionManagerWrapper) {
        this.mContentRecordingSession = null;
        this.mRecordedWindowContainer = null;
        this.mRecordedSurface = null;
        this.mLastRecordedBounds = null;
        this.mLastOrientation = 0;
        this.mCRrapper = new ContentRecorderWrapper();
        this.mDisplayContent = displayContent;
        this.mMediaProjectionManager = mediaProjectionManagerWrapper;
        this.mContentRecorderExt = (IContentRecorderExt) ExtLoader.type(IContentRecorderExt.class).base(this).create();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setContentRecordingSession(ContentRecordingSession contentRecordingSession) {
        this.mContentRecordingSession = contentRecordingSession;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isContentRecordingSessionSet() {
        return this.mContentRecordingSession != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCurrentlyRecording() {
        return (this.mContentRecordingSession == null || this.mRecordedSurface == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void updateRecording() {
        if (isCurrentlyRecording() && (this.mDisplayContent.getLastHasContent() || this.mDisplayContent.getDisplayInfo().state == 1)) {
            pauseRecording();
        } else {
            startRecordingIfNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onConfigurationChanged(@Configuration.Orientation int i) {
        if (!isCurrentlyRecording() || this.mLastRecordedBounds == null) {
            return;
        }
        if (this.mRecordedWindowContainer == null) {
            if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -1156314529, 1, "Content Recording: Unexpectedly null window container; unable to update recording for display %d", new Object[]{Long.valueOf(this.mDisplayContent.getDisplayId())});
                return;
            }
            return;
        }
        if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 339482207, 1, "Content Recording: Display %d was already recording, so apply transformations if necessary", new Object[]{Long.valueOf(this.mDisplayContent.getDisplayId())});
        }
        Rect bounds = this.mRecordedWindowContainer.getBounds();
        int orientation = this.mRecordedWindowContainer.getOrientation();
        if (this.mLastRecordedBounds.equals(bounds) && i == orientation && !this.mContentRecorderExt.isSurfaceSizeChanged()) {
            return;
        }
        Point fetchSurfaceSizeIfPresent = fetchSurfaceSizeIfPresent();
        if (fetchSurfaceSizeIfPresent != null) {
            if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -452750194, 17, "Content Recording: Going ahead with updating recording for display %d to new bounds %s and/or orientation %d.", new Object[]{Long.valueOf(this.mDisplayContent.getDisplayId()), String.valueOf(bounds), Long.valueOf(orientation)});
            }
            if (this.mContentRecorderExt.synchronizeMirrorTransactionIfNeeded()) {
                updateMirroredSurface(this.mRecordedWindowContainer.getSyncTransaction(), bounds, fetchSurfaceSizeIfPresent);
                return;
            } else {
                updateMirroredSurface(this.mDisplayContent.mWmService.mTransactionFactory.get(), bounds, fetchSurfaceSizeIfPresent);
                return;
            }
        }
        if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -1480264178, 17, "Content Recording: Unable to update recording for display %d to new bounds %s and/or orientation %d, since the surface is not available.", new Object[]{Long.valueOf(this.mDisplayContent.getDisplayId()), String.valueOf(bounds), Long.valueOf(orientation)});
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pauseRecording() {
        if (this.mRecordedSurface == null) {
            return;
        }
        if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -517666355, 13, "Content Recording: Display %d has content (%b) so pause recording", new Object[]{Long.valueOf(this.mDisplayContent.getDisplayId()), Boolean.valueOf(this.mDisplayContent.getLastHasContent())});
        }
        this.mDisplayContent.mWmService.mTransactionFactory.get().remove(this.mRecordedSurface).reparent(this.mDisplayContent.getWindowingLayer(), this.mDisplayContent.getSurfaceControl()).reparent(this.mDisplayContent.getOverlayLayer(), this.mDisplayContent.getSurfaceControl()).apply();
        this.mRecordedSurface = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopRecording() {
        unregisterListener();
        if (this.mRecordedSurface != null) {
            this.mDisplayContent.mWmService.mTransactionFactory.get().remove(this.mRecordedSurface).apply();
            this.mRecordedSurface = null;
            clearContentRecordingSession();
        }
    }

    private void stopMediaProjection() {
        if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 612856628, 1, "Content Recording: Stop MediaProjection on virtual display %d", new Object[]{Long.valueOf(this.mDisplayContent.getDisplayId())});
        }
        MediaProjectionManagerWrapper mediaProjectionManagerWrapper = this.mMediaProjectionManager;
        if (mediaProjectionManagerWrapper != null) {
            mediaProjectionManagerWrapper.stopActiveProjection();
        }
    }

    private void clearContentRecordingSession() {
        this.mContentRecordingSession = null;
        WindowManagerService windowManagerService = this.mDisplayContent.mWmService;
        windowManagerService.mContentRecordingController.setContentRecordingSessionLocked(null, windowManagerService);
    }

    private void unregisterListener() {
        WindowContainer windowContainer = this.mRecordedWindowContainer;
        Task asTask = windowContainer != null ? windowContainer.asTask() : null;
        if (asTask == null || !isRecordingContentTask()) {
            return;
        }
        asTask.unregisterWindowContainerListener(this);
        this.mRecordedWindowContainer = null;
    }

    private void startRecordingIfNeeded() {
        ContentRecordingSession contentRecordingSession;
        if (this.mDisplayContent.getLastHasContent() || isCurrentlyRecording()) {
            return;
        }
        if (this.mDisplayContent.getDisplayInfo().state == 1 || (contentRecordingSession = this.mContentRecordingSession) == null) {
            return;
        }
        if (contentRecordingSession.isWaitingForConsent()) {
            if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -125383273, 0, "Content Recording: waiting to record, so do nothing", (Object[]) null);
                return;
            }
            return;
        }
        WindowContainer retrieveRecordedWindowContainer = retrieveRecordedWindowContainer();
        this.mRecordedWindowContainer = retrieveRecordedWindowContainer;
        if (retrieveRecordedWindowContainer == null) {
            return;
        }
        Point fetchSurfaceSizeIfPresent = fetchSurfaceSizeIfPresent();
        if (fetchSurfaceSizeIfPresent == null) {
            if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 1712935427, 1, "Content Recording: Unable to start recording for display %d since the surface is not available.", new Object[]{Long.valueOf(this.mDisplayContent.getDisplayId())});
                return;
            }
            return;
        }
        if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -1217596375, 5, "Content Recording: Display %d has no content and is on, so start recording for state %d", new Object[]{Long.valueOf(this.mDisplayContent.getDisplayId()), Long.valueOf(this.mDisplayContent.getDisplayInfo().state)});
        }
        this.mRecordedSurface = SurfaceControl.mirrorSurface(this.mRecordedWindowContainer.getSurfaceControl());
        SurfaceControl.Transaction reparent = this.mDisplayContent.mWmService.mTransactionFactory.get().reparent(this.mRecordedSurface, this.mDisplayContent.getSurfaceControl()).reparent(this.mDisplayContent.getWindowingLayer(), null).reparent(this.mDisplayContent.getOverlayLayer(), null);
        updateMirroredSurface(reparent, this.mRecordedWindowContainer.getBounds(), fetchSurfaceSizeIfPresent);
        reparent.apply();
        if (this.mContentRecordingSession.getContentToRecord() == 1) {
            this.mMediaProjectionManager.notifyActiveProjectionCapturedContentVisibilityChanged(this.mRecordedWindowContainer.asTask().isVisibleRequested());
        } else {
            this.mMediaProjectionManager.notifyActiveProjectionCapturedContentVisibilityChanged(this.mRecordedWindowContainer.asDisplayContent().getDisplayInfo().state != 1);
        }
    }

    private WindowContainer retrieveRecordedWindowContainer() {
        int contentToRecord = this.mContentRecordingSession.getContentToRecord();
        IBinder tokenToRecord = this.mContentRecordingSession.getTokenToRecord();
        if (contentToRecord == 0) {
            DisplayContent displayContent = this.mDisplayContent.mWmService.mRoot.getDisplayContent(this.mContentRecordingSession.getDisplayToRecord());
            if (displayContent != null) {
                return displayContent;
            }
            DisplayContent displayContent2 = this.mDisplayContent;
            displayContent2.mWmService.mDisplayManagerInternal.setWindowManagerMirroring(displayContent2.getDisplayId(), false);
            handleStartRecordingFailed();
            if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -732715767, 1, "Unable to retrieve window container to start recording for display %d", new Object[]{Long.valueOf(this.mDisplayContent.getDisplayId())});
            }
            return null;
        }
        if (contentToRecord == 1) {
            if (!DeviceConfig.getBoolean("window_manager", KEY_RECORD_TASK_FEATURE, false)) {
                handleStartRecordingFailed();
                if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 1563836923, 1, "Content Recording: Unable to record task since feature is disabled %d", new Object[]{Long.valueOf(this.mDisplayContent.getDisplayId())});
                }
                return null;
            }
            if (tokenToRecord == null) {
                handleStartRecordingFailed();
                if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -1097851684, 1, "Content Recording: Unable to start recording due to null token for display %d", new Object[]{Long.valueOf(this.mDisplayContent.getDisplayId())});
                }
                return null;
            }
            Task asTask = WindowContainer.fromBinder(tokenToRecord).asTask();
            if (asTask == null) {
                handleStartRecordingFailed();
                if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -2074882083, 1, "Content Recording: Unable to retrieve task to start recording for display %d", new Object[]{Long.valueOf(this.mDisplayContent.getDisplayId())});
                }
            } else {
                asTask.registerWindowContainerListener(this);
            }
            return asTask;
        }
        handleStartRecordingFailed();
        if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -869242375, 1, "Content Recording: Unable to start recording due to invalid region for display %d", new Object[]{Long.valueOf(this.mDisplayContent.getDisplayId())});
        }
        return null;
    }

    private void handleStartRecordingFailed() {
        boolean isRecordingContentTask = isRecordingContentTask();
        unregisterListener();
        clearContentRecordingSession();
        if (isRecordingContentTask) {
            stopMediaProjection();
        }
    }

    @VisibleForTesting
    void updateMirroredSurface(SurfaceControl.Transaction transaction, Rect rect, Point point) {
        float width;
        float f;
        int height;
        int rotation = this.mRecordedWindowContainer.getWindowConfiguration().getRotation();
        if ((rotation == 1 || rotation == 3) && this.mContentRecorderExt.ifNeedRotateSurfaceForOplus(this.mDisplayContent)) {
            width = point.y / rect.width();
            f = point.x;
            height = rect.height();
        } else {
            width = point.x / rect.width();
            f = point.y;
            height = rect.height();
        }
        float min = Math.min(width, f / height);
        int round = Math.round(rect.width() * min);
        int round2 = Math.round(rect.height() * min);
        int i = point.x;
        int i2 = round != i ? (i - round) / 2 : 0;
        int i3 = point.y;
        int i4 = round2 != i3 ? (i3 - round2) / 2 : 0;
        Slog.d(TAG, "Update mirror layer surface. curRotation =: " + rotation + "\n scale=: " + min + " shiftedX=: " + i2 + " shiftedY=: " + i4 + "\n surfaceSize.x=: " + point.x + "\n surfaceSize.y=: " + point.y + "\n recordedContentwidth=: " + rect.width() + "\n recordedContenthight=: " + rect.height());
        if (this.mContentRecorderExt.ifNeedRotateSurfaceForOplus(this.mDisplayContent)) {
            this.mContentRecorderExt.rotateSurface(this.mRecordedSurface, transaction, min, rect, point, rotation);
        } else {
            transaction.setWindowCrop(this.mRecordedSurface, rect.width(), rect.height()).setMatrix(this.mRecordedSurface, min, 0.0f, 0.0f, min).setPosition(this.mRecordedSurface, i2, i4);
        }
        if (!this.mContentRecorderExt.synchronizeMirrorTransactionIfNeeded()) {
            transaction.apply();
        }
        this.mLastRecordedBounds = new Rect(rect);
        if (!ActivityTaskManagerService.LTW_DISABLE) {
            this.mContentRecorderExt.setSurfaceSize(point);
        }
        this.mMediaProjectionManager.notifyActiveProjectionCapturedContentResized(this.mLastRecordedBounds.width(), this.mLastRecordedBounds.height());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Point fetchSurfaceSizeIfPresent() {
        DisplayContent displayContent = this.mDisplayContent;
        Point displaySurfaceDefaultSize = displayContent.mWmService.mDisplayManagerInternal.getDisplaySurfaceDefaultSize(displayContent.getDisplayId());
        if (displaySurfaceDefaultSize != null) {
            return displaySurfaceDefaultSize;
        }
        if (!ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
            return null;
        }
        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 1750878635, 1, "Content Recording: Provided surface for recording on display %d is not present, so do not update the surface", new Object[]{Long.valueOf(this.mDisplayContent.getDisplayId())});
        return null;
    }

    @Override // com.android.server.wm.WindowContainerListener
    public void onRemoved() {
        if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 937080808, 1, "Content Recording: Recorded task is removed, so stop recording on display %d", new Object[]{Long.valueOf(this.mDisplayContent.getDisplayId())});
        }
        unregisterListener();
        clearContentRecordingSession();
        stopMediaProjection();
    }

    @Override // com.android.server.wm.ConfigurationContainerListener
    public void onMergedOverrideConfigurationChanged(Configuration configuration) {
        super.onMergedOverrideConfigurationChanged(configuration);
        onConfigurationChanged(this.mLastOrientation);
        this.mLastOrientation = configuration.orientation;
    }

    @Override // com.android.server.wm.WindowContainerListener
    public void onVisibleRequestedChanged(boolean z) {
        if (!isCurrentlyRecording() || this.mLastRecordedBounds == null) {
            return;
        }
        this.mMediaProjectionManager.notifyActiveProjectionCapturedContentVisibilityChanged(z);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private static final class RemoteMediaProjectionManagerWrapper implements MediaProjectionManagerWrapper {
        private IMediaProjectionManager mIMediaProjectionManager;

        private RemoteMediaProjectionManagerWrapper() {
            this.mIMediaProjectionManager = null;
        }

        @Override // com.android.server.wm.ContentRecorder.MediaProjectionManagerWrapper
        public void stopActiveProjection() {
            fetchMediaProjectionManager();
            IMediaProjectionManager iMediaProjectionManager = this.mIMediaProjectionManager;
            if (iMediaProjectionManager == null) {
                return;
            }
            try {
                iMediaProjectionManager.stopActiveProjection();
            } catch (RemoteException e) {
                if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
                    ProtoLogImpl.e(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -88873335, 0, "Content Recording: Unable to tell MediaProjectionManagerService to stop the active projection: %s", new Object[]{String.valueOf(e)});
                }
            }
        }

        @Override // com.android.server.wm.ContentRecorder.MediaProjectionManagerWrapper
        public void notifyActiveProjectionCapturedContentResized(int i, int i2) {
            fetchMediaProjectionManager();
            IMediaProjectionManager iMediaProjectionManager = this.mIMediaProjectionManager;
            if (iMediaProjectionManager == null) {
                return;
            }
            try {
                iMediaProjectionManager.notifyActiveProjectionCapturedContentResized(i, i2);
            } catch (RemoteException e) {
                if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
                    ProtoLogImpl.e(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 1661414284, 0, "Content Recording: Unable to tell MediaProjectionManagerService about resizing the active projection: %s", new Object[]{String.valueOf(e)});
                }
            }
        }

        @Override // com.android.server.wm.ContentRecorder.MediaProjectionManagerWrapper
        public void notifyActiveProjectionCapturedContentVisibilityChanged(boolean z) {
            fetchMediaProjectionManager();
            IMediaProjectionManager iMediaProjectionManager = this.mIMediaProjectionManager;
            if (iMediaProjectionManager == null) {
                return;
            }
            try {
                iMediaProjectionManager.notifyActiveProjectionCapturedContentVisibilityChanged(z);
            } catch (RemoteException e) {
                if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
                    ProtoLogImpl.e(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -180594244, 0, "Content Recording: Unable to tell MediaProjectionManagerService about visibility change on the active projection: %s", new Object[]{String.valueOf(e)});
                }
            }
        }

        private void fetchMediaProjectionManager() {
            IBinder service;
            if (this.mIMediaProjectionManager == null && (service = ServiceManager.getService("media_projection")) != null) {
                this.mIMediaProjectionManager = IMediaProjectionManager.Stub.asInterface(service);
            }
        }
    }

    private boolean isRecordingContentTask() {
        ContentRecordingSession contentRecordingSession = this.mContentRecordingSession;
        return contentRecordingSession != null && contentRecordingSession.getContentToRecord() == 1;
    }

    public IContentRecorderWrapper getWrapper() {
        return this.mCRrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class ContentRecorderWrapper implements IContentRecorderWrapper {
        private ContentRecorderWrapper() {
        }

        @Override // com.android.server.wm.IContentRecorderWrapper
        public Rect getRectBounds() {
            return ContentRecorder.this.mLastRecordedBounds;
        }

        @Override // com.android.server.wm.IContentRecorderWrapper
        public WindowContainer getRecordedWindowContainer() {
            return ContentRecorder.this.mRecordedWindowContainer;
        }

        @Override // com.android.server.wm.IContentRecorderWrapper
        public DisplayContent getDisplayContent() {
            return ContentRecorder.this.mDisplayContent;
        }

        @Override // com.android.server.wm.IContentRecorderWrapper
        public Point fetchSurfaceSizeIfPresent() {
            return ContentRecorder.this.fetchSurfaceSizeIfPresent();
        }
    }
}
