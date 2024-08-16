package com.android.server.media.projection;

import android.R;
import android.annotation.EnforcePermission;
import android.app.ActivityManagerInternal;
import android.app.AppOpsManager;
import android.app.IProcessObserver;
import android.app.compat.CompatChanges;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.MediaRouter;
import android.media.projection.IMediaProjection;
import android.media.projection.IMediaProjectionCallback;
import android.media.projection.IMediaProjectionManager;
import android.media.projection.IMediaProjectionManagerServiceExt;
import android.media.projection.IMediaProjectionWatcherCallback;
import android.media.projection.MediaProjectionInfo;
import android.media.projection.ReviewGrantedConsentResult;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PermissionEnforcer;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.ContentRecordingSession;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.DumpUtils;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.Watchdog;
import com.android.server.media.projection.MediaProjectionManagerService;
import com.android.server.wm.WindowManagerInternal;
import com.oplus.permission.IOplusPermissionCheckInjectorExt;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class MediaProjectionManagerService extends SystemService implements Watchdog.Monitor {

    @VisibleForTesting
    static final long MEDIA_PROJECTION_PREVENTS_REUSING_CONSENT = 266201607;
    private static final boolean REQUIRE_FG_SERVICE_FOR_PROJECTION = true;
    private static final String TAG = "MediaProjectionManagerService";
    private final String OPLUSSCREENRECORDER;
    private final ActivityManagerInternal mActivityManagerInternal;
    private final AppOpsManager mAppOps;
    private final CallbackDelegate mCallbackDelegate;
    private final Clock mClock;
    private final Context mContext;
    private final Map<IBinder, IBinder.DeathRecipient> mDeathEaters;
    private final Handler mHandler;
    private final Injector mInjector;
    private final Object mLock;
    private IMediaProjectionManagerServiceExt mMediaProjectionManagerServiceExt;
    private MediaRouter.RouteInfo mMediaRouteInfo;
    private final MediaRouter mMediaRouter;
    private final MediaRouterCallback mMediaRouterCallback;
    private final CallbackDelegate mOplusCallbackDelegate;
    private MediaProjection mOplusProjectionGrant;
    private IBinder mOplusProjectionToken;
    private final PackageManager mPackageManager;

    @GuardedBy({"mLock"})
    private MediaProjection mProjectionGrant;

    @GuardedBy({"mLock"})
    private IBinder mProjectionToken;
    private final WindowManagerInternal mWmInternal;

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    interface Clock {
        long uptimeMillis();
    }

    public MediaProjectionManagerService(Context context) {
        this(context, new Injector());
    }

    @VisibleForTesting
    MediaProjectionManagerService(Context context, Injector injector) {
        super(context);
        this.mLock = new Object();
        this.OPLUSSCREENRECORDER = "com.oplus.screenrecorder";
        this.mContext = context;
        this.mInjector = injector;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mClock = injector.createClock();
        this.mDeathEaters = new ArrayMap();
        this.mCallbackDelegate = new CallbackDelegate();
        this.mOplusCallbackDelegate = new CallbackDelegate();
        this.mAppOps = (AppOpsManager) context.getSystemService("appops");
        this.mActivityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        this.mPackageManager = context.getPackageManager();
        this.mWmInternal = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);
        this.mMediaRouter = (MediaRouter) context.getSystemService("media_router");
        this.mMediaRouterCallback = new MediaRouterCallback();
        Watchdog.getInstance().addMonitor(this);
        this.mMediaProjectionManagerServiceExt = (IMediaProjectionManagerServiceExt) ExtLoader.type(IMediaProjectionManagerServiceExt.class).create();
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class Injector {
        Injector() {
        }

        boolean shouldMediaProjectionPreventReusingConsent(MediaProjection mediaProjection) {
            return CompatChanges.isChangeEnabled(MediaProjectionManagerService.MEDIA_PROJECTION_PREVENTS_REUSING_CONSENT, mediaProjection.packageName, UserHandle.getUserHandleForUid(mediaProjection.uid));
        }

        Clock createClock() {
            return new Clock() { // from class: com.android.server.media.projection.MediaProjectionManagerService$Injector$$ExternalSyntheticLambda0
                @Override // com.android.server.media.projection.MediaProjectionManagerService.Clock
                public final long uptimeMillis() {
                    return SystemClock.uptimeMillis();
                }
            };
        }
    }

    public void onStart() {
        publishBinderService("media_projection", new BinderService(this.mContext), false);
        this.mMediaRouter.addCallback(4, this.mMediaRouterCallback, 8);
        this.mActivityManagerInternal.registerProcessObserver(new IProcessObserver.Stub() { // from class: com.android.server.media.projection.MediaProjectionManagerService.1
            public void onForegroundActivitiesChanged(int i, int i2, boolean z) {
            }

            public void onProcessDied(int i, int i2) {
            }

            public void onForegroundServicesChanged(int i, int i2, int i3) {
                MediaProjectionManagerService.this.handleForegroundServicesChanged(i, i2, i3);
            }
        });
    }

    public void onUserSwitching(SystemService.TargetUser targetUser, SystemService.TargetUser targetUser2) {
        this.mMediaRouter.rebindAsUser(targetUser2.getUserIdentifier());
        synchronized (this.mLock) {
            MediaProjection mediaProjection = this.mProjectionGrant;
            if (mediaProjection != null) {
                mediaProjection.stop();
            }
            MediaProjection mediaProjection2 = this.mOplusProjectionGrant;
            if (mediaProjection2 != null) {
                mediaProjection2.stop();
            }
        }
    }

    public void monitor() {
        synchronized (this.mLock) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleForegroundServicesChanged(int i, int i2, int i3) {
        synchronized (this.mLock) {
            MediaProjection mediaProjection = this.mProjectionGrant;
            if (mediaProjection != null && mediaProjection.uid == i2) {
                if (mediaProjection.requiresForegroundService()) {
                    MediaProjection mediaProjection2 = this.mOplusProjectionGrant;
                    if (mediaProjection2 == null || mediaProjection2.uid != i2 || !mediaProjection2.requiresForegroundService() || this.mActivityManagerInternal.hasRunningForegroundService(i2, 32)) {
                        return;
                    }
                    synchronized (this.mLock) {
                        MediaProjection mediaProjection3 = this.mProjectionGrant;
                        if (mediaProjection3 != null) {
                            mediaProjection3.stop();
                        }
                        this.mOplusProjectionGrant.stop();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startProjectionLocked(MediaProjection mediaProjection) {
        if (this.mProjectionGrant != null && !mediaProjection.packageName.equals("com.oplus.screenrecorder")) {
            this.mProjectionGrant.stop();
        }
        if (this.mOplusProjectionGrant != null && mediaProjection.packageName.equals("com.oplus.screenrecorder")) {
            this.mOplusProjectionGrant.stop();
        }
        if (this.mMediaRouteInfo != null) {
            Slog.d(TAG, "connect status is " + ((Object) this.mMediaRouteInfo.getStatus()));
            if (this.mMediaRouteInfo.getStatusCode() != 6) {
                this.mMediaRouter.getFallbackRoute().select();
            }
        }
        if (mediaProjection.packageName.equals("com.oplus.screenrecorder")) {
            this.mOplusProjectionGrant = mediaProjection;
            this.mOplusProjectionToken = mediaProjection.asBinder();
        } else {
            this.mProjectionGrant = mediaProjection;
            this.mProjectionToken = mediaProjection.asBinder();
        }
        dispatchStart(mediaProjection);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopProjectionLocked(MediaProjection mediaProjection) {
        if (mediaProjection.packageName.equals("com.oplus.screenrecorder")) {
            this.mOplusProjectionGrant = null;
            this.mOplusProjectionToken = null;
        } else {
            this.mProjectionToken = null;
            this.mProjectionGrant = null;
        }
        dispatchStop(mediaProjection);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addCallback(final IMediaProjectionWatcherCallback iMediaProjectionWatcherCallback) {
        IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() { // from class: com.android.server.media.projection.MediaProjectionManagerService.2
            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                MediaProjectionManagerService.this.removeCallback(iMediaProjectionWatcherCallback);
            }
        };
        synchronized (this.mLock) {
            this.mCallbackDelegate.add(iMediaProjectionWatcherCallback);
            linkDeathRecipientLocked(iMediaProjectionWatcherCallback, deathRecipient);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeCallback(IMediaProjectionWatcherCallback iMediaProjectionWatcherCallback) {
        synchronized (this.mLock) {
            unlinkDeathRecipientLocked(iMediaProjectionWatcherCallback);
            this.mCallbackDelegate.remove(iMediaProjectionWatcherCallback);
        }
    }

    private void linkDeathRecipientLocked(IMediaProjectionWatcherCallback iMediaProjectionWatcherCallback, IBinder.DeathRecipient deathRecipient) {
        try {
            IBinder asBinder = iMediaProjectionWatcherCallback.asBinder();
            asBinder.linkToDeath(deathRecipient, 0);
            this.mDeathEaters.put(asBinder, deathRecipient);
        } catch (RemoteException e) {
            Slog.e(TAG, "Unable to link to death for media projection monitoring callback", e);
        }
    }

    private void unlinkDeathRecipientLocked(IMediaProjectionWatcherCallback iMediaProjectionWatcherCallback) {
        IBinder asBinder = iMediaProjectionWatcherCallback.asBinder();
        IBinder.DeathRecipient remove = this.mDeathEaters.remove(asBinder);
        if (remove != null) {
            asBinder.unlinkToDeath(remove, 0);
        }
    }

    private void dispatchStart(MediaProjection mediaProjection) {
        if (mediaProjection.packageName.equals("com.oplus.screenrecorder")) {
            this.mOplusCallbackDelegate.dispatchStart(mediaProjection);
        } else {
            this.mCallbackDelegate.dispatchStart(mediaProjection);
        }
        this.mMediaProjectionManagerServiceExt.start(this.mContext, mediaProjection.packageName, IMediaProjection.Stub.getCallingUserHandle());
    }

    private void dispatchStop(MediaProjection mediaProjection) {
        if (mediaProjection.packageName.equals("com.oplus.screenrecorder")) {
            this.mOplusCallbackDelegate.dispatchStop(mediaProjection);
        } else {
            this.mCallbackDelegate.dispatchStop(mediaProjection);
        }
        this.mMediaProjectionManagerServiceExt.stop(this.mContext);
    }

    @VisibleForTesting
    boolean setContentRecordingSession(ContentRecordingSession contentRecordingSession) {
        boolean contentRecordingSession2 = this.mWmInternal.setContentRecordingSession(contentRecordingSession);
        synchronized (this.mLock) {
            if (!contentRecordingSession2) {
                MediaProjection mediaProjection = this.mProjectionGrant;
                if (mediaProjection != null) {
                    mediaProjection.stop();
                }
                return false;
            }
            MediaProjection mediaProjection2 = this.mProjectionGrant;
            if (mediaProjection2 != null) {
                mediaProjection2.mSession = contentRecordingSession;
            }
            return true;
        }
    }

    @VisibleForTesting
    boolean isCurrentProjection(IBinder iBinder) {
        synchronized (this.mLock) {
            IBinder iBinder2 = this.mProjectionToken;
            if (iBinder2 != null && this.mOplusProjectionToken != null) {
                return iBinder2.equals(iBinder) || this.mOplusProjectionToken.equals(iBinder);
            }
            IBinder iBinder3 = this.mOplusProjectionToken;
            if (iBinder3 != null) {
                return iBinder3.equals(iBinder);
            }
            if (iBinder2 != null) {
                return iBinder2.equals(iBinder);
            }
            return false;
        }
    }

    @VisibleForTesting
    void requestConsentForInvalidProjection() {
        Intent buildReviewGrantedConsentIntentLocked;
        int i;
        synchronized (this.mLock) {
            buildReviewGrantedConsentIntentLocked = buildReviewGrantedConsentIntentLocked();
            i = this.mProjectionGrant.uid;
        }
        Slog.v(TAG, "Reusing token: Reshow dialog for due to invalid projection.");
        this.mContext.startActivityAsUser(buildReviewGrantedConsentIntentLocked, UserHandle.getUserHandleForUid(i));
    }

    private Intent buildReviewGrantedConsentIntentLocked() {
        return new Intent().setComponent(ComponentName.unflattenFromString(this.mContext.getResources().getString(R.string.data_usage_rapid_body))).putExtra("extra_media_projection_user_consent_required", true).putExtra("extra_media_projection_package_reusing_consent", this.mProjectionGrant.packageName).setFlags(276824064);
    }

    @VisibleForTesting
    void setUserReviewGrantedConsentResult(@ReviewGrantedConsentResult int i, IMediaProjection iMediaProjection) {
        synchronized (this.mLock) {
            if (i == 1 || i == 2) {
                if (!isCurrentProjection(iMediaProjection == null ? null : iMediaProjection.asBinder())) {
                    Slog.v(TAG, "Reusing token: Ignore consent result of " + i + " for a token that isn't current");
                    return;
                }
            }
            MediaProjection mediaProjection = this.mProjectionGrant;
            if (mediaProjection == null) {
                Slog.w(TAG, "Reusing token: Can't review consent with no ongoing projection.");
                return;
            }
            if (mediaProjection.mSession != null && this.mProjectionGrant.mSession.isWaitingForConsent()) {
                Slog.v(TAG, "Reusing token: Handling user consent result " + i);
                if (i == -1 || i == 0) {
                    setReviewedConsentSessionLocked(null);
                    MediaProjection mediaProjection2 = this.mProjectionGrant;
                    if (mediaProjection2 != null) {
                        mediaProjection2.stop();
                    }
                } else if (i == 1) {
                    setReviewedConsentSessionLocked(ContentRecordingSession.createDisplaySession(0));
                } else if (i == 2) {
                    setReviewedConsentSessionLocked(ContentRecordingSession.createTaskSession(this.mProjectionGrant.getLaunchCookie()));
                }
                return;
            }
            Slog.w(TAG, "Reusing token: Ignore consent result " + i + " if not waiting for the result.");
        }
    }

    private void setReviewedConsentSessionLocked(ContentRecordingSession contentRecordingSession) {
        if (contentRecordingSession != null) {
            contentRecordingSession.setWaitingForConsent(false);
            contentRecordingSession.setVirtualDisplayId(this.mProjectionGrant.mVirtualDisplayId);
        }
        Slog.v(TAG, "Reusing token: Processed consent so set the session " + contentRecordingSession);
        if (setContentRecordingSession(contentRecordingSession)) {
            return;
        }
        Slog.e(TAG, "Reusing token: Failed to set session for reused consent, so stop");
    }

    @VisibleForTesting
    MediaProjection createProjectionInternal(int i, String str, int i2, boolean z, UserHandle userHandle) {
        try {
            ApplicationInfo applicationInfoAsUser = this.mPackageManager.getApplicationInfoAsUser(str, PackageManager.ApplicationInfoFlags.of(0L), userHandle);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                MediaProjection mediaProjection = new MediaProjection(i2, i, str, applicationInfoAsUser.targetSdkVersion, applicationInfoAsUser.isPrivilegedApp());
                if (z) {
                    this.mAppOps.setMode(46, mediaProjection.uid, mediaProjection.packageName, 0);
                }
                return mediaProjection;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        } catch (PackageManager.NameNotFoundException unused) {
            throw new IllegalArgumentException("No package matching :" + str);
        }
    }

    @VisibleForTesting
    MediaProjection getProjectionInternal(int i, String str) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                MediaProjection mediaProjection = this.mProjectionGrant;
                if (mediaProjection != null && mediaProjection.mSession != null && this.mProjectionGrant.mSession.isWaitingForConsent()) {
                    MediaProjection mediaProjection2 = this.mProjectionGrant;
                    if (mediaProjection2.uid != i || !Objects.equals(mediaProjection2.packageName, str)) {
                        Slog.e(TAG, "Reusing token: Not possible to reuse the current projection instance due to package details mismatching");
                        return null;
                    }
                    Slog.v(TAG, "Reusing token: getProjection can reuse the current projection");
                    return this.mProjectionGrant;
                }
                Slog.e(TAG, "Reusing token: Not possible to reuse the current projection instance");
                return null;
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @VisibleForTesting
    MediaProjectionInfo getActiveProjectionInfo() {
        synchronized (this.mLock) {
            MediaProjection mediaProjection = this.mProjectionGrant;
            if (mediaProjection != null) {
                return mediaProjection.getProjectionInfo();
            }
            MediaProjection mediaProjection2 = this.mOplusProjectionGrant;
            if (mediaProjection2 == null) {
                return null;
            }
            return mediaProjection2.getProjectionInfo();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dump(PrintWriter printWriter) {
        printWriter.println("MEDIA PROJECTION MANAGER (dumpsys media_projection)");
        synchronized (this.mLock) {
            printWriter.println("Media Projection: ");
            MediaProjection mediaProjection = this.mProjectionGrant;
            if (mediaProjection != null) {
                mediaProjection.dump(printWriter);
            } else {
                MediaProjection mediaProjection2 = this.mOplusProjectionGrant;
                if (mediaProjection2 != null) {
                    mediaProjection2.dump(printWriter);
                } else {
                    printWriter.println("null");
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class BinderService extends IMediaProjectionManager.Stub {
        BinderService(Context context) {
            super(PermissionEnforcer.fromContext(context));
        }

        public boolean hasProjectionPermission(int i, String str) {
            boolean z;
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                if (!checkPermission(str, "android.permission.CAPTURE_VIDEO_OUTPUT")) {
                    if (MediaProjectionManagerService.this.mAppOps.noteOpNoThrow(46, i, str) != 0) {
                        z = false;
                        return z | false;
                    }
                }
                z = true;
                return z | false;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public IMediaProjection createProjection(int i, String str, int i2, boolean z) {
            if (MediaProjectionManagerService.this.mContext.checkCallingPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new SecurityException("Requires MANAGE_MEDIA_PROJECTION in order to grant projection permission");
            }
            if (str == null || str.isEmpty()) {
                throw new IllegalArgumentException("package name must not be empty");
            }
            return MediaProjectionManagerService.this.createProjectionInternal(i, str, i2, z, Binder.getCallingUserHandle());
        }

        @EnforcePermission("android.permission.MANAGE_MEDIA_PROJECTION")
        public IMediaProjection getProjection(int i, String str) {
            getProjection_enforcePermission();
            if (str == null || str.isEmpty()) {
                throw new IllegalArgumentException("package name must not be empty");
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return MediaProjectionManagerService.this.getProjectionInternal(i, str);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public boolean isCurrentProjection(IMediaProjection iMediaProjection) {
            if (MediaProjectionManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new SecurityException("Requires MANAGE_MEDIA_PROJECTION in order to check if the given projection is current.");
            }
            return MediaProjectionManagerService.this.isCurrentProjection(iMediaProjection == null ? null : iMediaProjection.asBinder());
        }

        public MediaProjectionInfo getActiveProjectionInfo() {
            if (MediaProjectionManagerService.this.mContext.checkCallingPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new SecurityException("Requires MANAGE_MEDIA_PROJECTION in order to get active projection info");
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return MediaProjectionManagerService.this.getActiveProjectionInfo();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void stopActiveProjection() {
            if (MediaProjectionManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new SecurityException("Requires MANAGE_MEDIA_PROJECTION in order to stop the active projection");
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (MediaProjectionManagerService.this.mLock) {
                    if (MediaProjectionManagerService.this.mProjectionGrant != null) {
                        MediaProjectionManagerService.this.mProjectionGrant.stop();
                    }
                }
                if (MediaProjectionManagerService.this.mOplusProjectionGrant != null) {
                    MediaProjectionManagerService.this.mOplusProjectionGrant.stop();
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void notifyActiveProjectionCapturedContentResized(int i, int i2) {
            if (MediaProjectionManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new SecurityException("Requires MANAGE_MEDIA_PROJECTION in order to notify on captured content resize");
            }
            synchronized (MediaProjectionManagerService.this.mLock) {
                if (isCurrentProjection(MediaProjectionManagerService.this.mProjectionGrant)) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        synchronized (MediaProjectionManagerService.this.mLock) {
                            if (MediaProjectionManagerService.this.mProjectionGrant != null && MediaProjectionManagerService.this.mCallbackDelegate != null) {
                                MediaProjectionManagerService.this.mCallbackDelegate.dispatchResize(MediaProjectionManagerService.this.mProjectionGrant, i, i2);
                            }
                        }
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
            }
        }

        public void notifyActiveProjectionCapturedContentVisibilityChanged(boolean z) {
            if (MediaProjectionManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new SecurityException("Requires MANAGE_MEDIA_PROJECTION in order to notify on captured content visibility changed");
            }
            synchronized (MediaProjectionManagerService.this.mLock) {
                if (isCurrentProjection(MediaProjectionManagerService.this.mProjectionGrant)) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        synchronized (MediaProjectionManagerService.this.mLock) {
                            if (MediaProjectionManagerService.this.mProjectionGrant != null && MediaProjectionManagerService.this.mCallbackDelegate != null) {
                                MediaProjectionManagerService.this.mCallbackDelegate.dispatchVisibilityChanged(MediaProjectionManagerService.this.mProjectionGrant, z);
                            }
                        }
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
            }
        }

        public void addCallback(IMediaProjectionWatcherCallback iMediaProjectionWatcherCallback) {
            if (MediaProjectionManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new SecurityException("Requires MANAGE_MEDIA_PROJECTION in order to add projection callbacks");
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                MediaProjectionManagerService.this.addCallback(iMediaProjectionWatcherCallback);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void removeCallback(IMediaProjectionWatcherCallback iMediaProjectionWatcherCallback) {
            if (MediaProjectionManagerService.this.mContext.checkCallingPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new SecurityException("Requires MANAGE_MEDIA_PROJECTION in order to remove projection callbacks");
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                MediaProjectionManagerService.this.removeCallback(iMediaProjectionWatcherCallback);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public boolean setContentRecordingSession(ContentRecordingSession contentRecordingSession, IMediaProjection iMediaProjection) {
            if (MediaProjectionManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new SecurityException("Requires MANAGE_MEDIA_PROJECTION to set session details.");
            }
            synchronized (MediaProjectionManagerService.this.mLock) {
                if (!isCurrentProjection(iMediaProjection)) {
                    throw new SecurityException("Unable to set ContentRecordingSession on non-current MediaProjection");
                }
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return MediaProjectionManagerService.this.setContentRecordingSession(contentRecordingSession);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void requestConsentForInvalidProjection(IMediaProjection iMediaProjection) {
            if (MediaProjectionManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new SecurityException("Requires MANAGE_MEDIA_PROJECTION to check if the givenprojection is valid.");
            }
            synchronized (MediaProjectionManagerService.this.mLock) {
                if (!isCurrentProjection(iMediaProjection)) {
                    Slog.v(MediaProjectionManagerService.TAG, "Reusing token: Won't request consent again for a token that isn't current");
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    MediaProjectionManagerService.this.requestConsentForInvalidProjection();
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        @EnforcePermission("android.permission.MANAGE_MEDIA_PROJECTION")
        public void setUserReviewGrantedConsentResult(@ReviewGrantedConsentResult int i, IMediaProjection iMediaProjection) {
            setUserReviewGrantedConsentResult_enforcePermission();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                MediaProjectionManagerService.this.setUserReviewGrantedConsentResult(i, iMediaProjection);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(MediaProjectionManagerService.this.mContext, MediaProjectionManagerService.TAG, printWriter)) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    MediaProjectionManagerService.this.dump(printWriter);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        private boolean checkPermission(String str, String str2) {
            return MediaProjectionManagerService.this.mContext.getPackageManager().checkPermission(str2, str) == 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class MediaProjection extends IMediaProjection.Stub {
        private IMediaProjectionCallback mCallback;
        private int mCountStarts;
        private final long mCreateTimeMs;
        private IBinder.DeathRecipient mDeathEater;
        final long mDefaultTimeoutMs;
        private final boolean mIsPrivileged;
        private IBinder mLaunchCookie;
        private boolean mRestoreSystemAlertWindow;
        private ContentRecordingSession mSession;
        private final int mTargetSdkVersion;
        private long mTimeoutMs;
        private IBinder mToken;
        private final int mType;
        private int mVirtualDisplayId;
        public final String packageName;
        public final int uid;
        public final UserHandle userHandle;

        public boolean canProjectSecureVideo() {
            return false;
        }

        MediaProjection(int i, int i2, String str, int i3, boolean z) {
            long millis = Duration.ofMinutes(5L).toMillis();
            this.mDefaultTimeoutMs = millis;
            this.mLaunchCookie = null;
            this.mTimeoutMs = millis;
            this.mCountStarts = 0;
            this.mVirtualDisplayId = -1;
            this.mType = i;
            this.uid = i2;
            this.packageName = str;
            this.userHandle = new UserHandle(UserHandle.getUserId(i2));
            this.mTargetSdkVersion = i3;
            this.mIsPrivileged = z;
            this.mCreateTimeMs = MediaProjectionManagerService.this.mClock.uptimeMillis();
            MediaProjectionManagerService.this.mActivityManagerInternal.notifyMediaProjectionEvent(i2, asBinder(), 0);
        }

        public boolean canProjectVideo() {
            int i = this.mType;
            return i == 1 || i == 0;
        }

        public boolean canProjectAudio() {
            int i = this.mType;
            return i == 1 || i == 2 || i == 0;
        }

        public int applyVirtualDisplayFlags(int i) {
            if (MediaProjectionManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new SecurityException("Requires MANAGE_MEDIA_PROJECTION to apply virtual display flags.");
            }
            int i2 = this.mType;
            if (i2 == 0) {
                return (i & (-9)) | 18;
            }
            if (i2 == 1) {
                return (i & (-18)) | 10;
            }
            if (i2 == 2) {
                return (i & (-9)) | 19;
            }
            throw new RuntimeException("Unknown MediaProjection type");
        }

        public void start(final IMediaProjectionCallback iMediaProjectionCallback) {
            if (iMediaProjectionCallback == null) {
                throw new IllegalArgumentException("callback must not be null");
            }
            if (this.mType == 0) {
                ((IOplusPermissionCheckInjectorExt) ExtLoader.type(IOplusPermissionCheckInjectorExt.class).create()).checkPermission("capture_or_mirror_screen", IMediaProjection.Stub.getCallingPid(), this.uid, "recordMirrorScreenBehavior");
                Slog.d(MediaProjectionManagerService.TAG, "start capture_or_mirror_screen uid:" + this.uid + " pid:" + IMediaProjection.Stub.getCallingPid());
            }
            boolean hasRunningForegroundService = MediaProjectionManagerService.this.mActivityManagerInternal.hasRunningForegroundService(this.uid, 32);
            synchronized (MediaProjectionManagerService.this.mLock) {
                if (MediaProjectionManagerService.this.isCurrentProjection(asBinder())) {
                    Slog.w(MediaProjectionManagerService.TAG, "UID " + Binder.getCallingUid() + " attempted to start already started MediaProjection");
                    this.mCountStarts = this.mCountStarts + 1;
                    return;
                }
                if (requiresForegroundService() && !hasRunningForegroundService) {
                    throw new SecurityException("Media projections require a foreground service of type ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION");
                }
                this.mCallback = iMediaProjectionCallback;
                registerCallback(iMediaProjectionCallback);
                try {
                    this.mToken = iMediaProjectionCallback.asBinder();
                    IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() { // from class: com.android.server.media.projection.MediaProjectionManagerService.MediaProjection.1
                        @Override // android.os.IBinder.DeathRecipient
                        public void binderDied() {
                            MediaProjection.this.unregisterCallback(iMediaProjectionCallback);
                            MediaProjection.this.stop();
                        }
                    };
                    this.mDeathEater = deathRecipient;
                    this.mToken.linkToDeath(deathRecipient, 0);
                    if (this.mType == 0) {
                        long clearCallingIdentity = Binder.clearCallingIdentity();
                        try {
                            if (ArrayUtils.contains(MediaProjectionManagerService.this.mPackageManager.getPackageInfoAsUser(this.packageName, 4096, UserHandle.getUserId(this.uid)).requestedPermissions, "android.permission.SYSTEM_ALERT_WINDOW") && MediaProjectionManagerService.this.mAppOps.unsafeCheckOpRawNoThrow(24, this.uid, this.packageName) == 3) {
                                MediaProjectionManagerService.this.mAppOps.setMode(24, this.uid, this.packageName, 0);
                                this.mRestoreSystemAlertWindow = true;
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            Slog.w(MediaProjectionManagerService.TAG, "Package not found, aborting MediaProjection", e);
                            return;
                        } finally {
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                        }
                    }
                    MediaProjectionManagerService.this.startProjectionLocked(this);
                    this.mCountStarts++;
                } catch (RemoteException e2) {
                    Slog.w(MediaProjectionManagerService.TAG, "MediaProjectionCallbacks must be valid, aborting MediaProjection", e2);
                }
            }
        }

        public void stop() {
            synchronized (MediaProjectionManagerService.this.mLock) {
                if (!MediaProjectionManagerService.this.isCurrentProjection(asBinder())) {
                    Slog.w(MediaProjectionManagerService.TAG, "Attempted to stop inactive MediaProjection (uid=" + Binder.getCallingUid() + ", pid=" + Binder.getCallingPid() + ")");
                    return;
                }
                if (this.mRestoreSystemAlertWindow) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        if (MediaProjectionManagerService.this.mAppOps.unsafeCheckOpRawNoThrow(24, this.uid, this.packageName) == 0) {
                            MediaProjectionManagerService.this.mAppOps.setMode(24, this.uid, this.packageName, 3);
                        }
                        this.mRestoreSystemAlertWindow = false;
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    } catch (Throwable th) {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        throw th;
                    }
                }
                MediaProjectionManagerService.this.stopProjectionLocked(this);
                this.mToken.unlinkToDeath(this.mDeathEater, 0);
                this.mToken = null;
                unregisterCallback(this.mCallback);
                this.mCallback = null;
                MediaProjectionManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.media.projection.MediaProjectionManagerService$MediaProjection$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaProjectionManagerService.MediaProjection.this.lambda$stop$0();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$stop$0() {
            MediaProjectionManagerService.this.mActivityManagerInternal.notifyMediaProjectionEvent(this.uid, asBinder(), 1);
        }

        public void registerCallback(IMediaProjectionCallback iMediaProjectionCallback) {
            if (iMediaProjectionCallback == null) {
                throw new IllegalArgumentException("callback must not be null");
            }
            if (this.packageName.equals("com.oplus.screenrecorder")) {
                MediaProjectionManagerService.this.mOplusCallbackDelegate.add(iMediaProjectionCallback);
            } else {
                MediaProjectionManagerService.this.mCallbackDelegate.add(iMediaProjectionCallback);
            }
        }

        public void unregisterCallback(IMediaProjectionCallback iMediaProjectionCallback) {
            if (iMediaProjectionCallback == null) {
                throw new IllegalArgumentException("callback must not be null");
            }
            if (this.packageName.equals("com.oplus.screenrecorder")) {
                MediaProjectionManagerService.this.mOplusCallbackDelegate.remove(iMediaProjectionCallback);
            } else {
                MediaProjectionManagerService.this.mCallbackDelegate.remove(iMediaProjectionCallback);
            }
        }

        public void setLaunchCookie(IBinder iBinder) {
            if (MediaProjectionManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new SecurityException("Requires MANAGE_MEDIA_PROJECTION to set launch cookie.");
            }
            this.mLaunchCookie = iBinder;
        }

        public IBinder getLaunchCookie() {
            if (MediaProjectionManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new SecurityException("Requires MANAGE_MEDIA_PROJECTION to get launch cookie.");
            }
            return this.mLaunchCookie;
        }

        public boolean isValid() {
            if (MediaProjectionManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new SecurityException("Requires MANAGE_MEDIA_PROJECTION to check if thisprojection is valid.");
            }
            synchronized (MediaProjectionManagerService.this.mLock) {
                if (((((MediaProjectionManagerService.this.mClock.uptimeMillis() - this.mCreateTimeMs) > this.mTimeoutMs ? 1 : ((MediaProjectionManagerService.this.mClock.uptimeMillis() - this.mCreateTimeMs) == this.mTimeoutMs ? 0 : -1)) > 0) || this.mCountStarts > 1 || (this.mVirtualDisplayId != -1)) ? false : true) {
                    return true;
                }
                if (!MediaProjectionManagerService.this.mInjector.shouldMediaProjectionPreventReusingConsent(MediaProjectionManagerService.this.mProjectionGrant)) {
                    return false;
                }
                if (MediaProjectionManagerService.this.mProjectionGrant.packageName.equals("com.oplus.cast")) {
                    Slog.i(MediaProjectionManagerService.TAG, "Reusing token: heycast is allways valid projection.");
                    return true;
                }
                Slog.v(MediaProjectionManagerService.TAG, "Reusing token: Throw exception due to invalid projection.");
                MediaProjectionManagerService.this.mProjectionGrant.stop();
                throw new SecurityException("Don't re-use the resultData to retrieve the same projection instance, and don't use a token that has timed out. Don't take multiple captures by invoking MediaProjection#createVirtualDisplay multiple times on the same instance.");
            }
        }

        public void notifyVirtualDisplayCreated(int i) {
            if (MediaProjectionManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new SecurityException("Requires MANAGE_MEDIA_PROJECTION to notify virtual display created.");
            }
            synchronized (MediaProjectionManagerService.this.mLock) {
                this.mVirtualDisplayId = i;
                ContentRecordingSession contentRecordingSession = this.mSession;
                if (contentRecordingSession != null && contentRecordingSession.getVirtualDisplayId() == -1) {
                    Slog.v(MediaProjectionManagerService.TAG, "Virtual display now created, so update session with the virtual display id");
                    this.mSession.setVirtualDisplayId(this.mVirtualDisplayId);
                    if (!MediaProjectionManagerService.this.setContentRecordingSession(this.mSession)) {
                        Slog.e(MediaProjectionManagerService.TAG, "Failed to set session for virtual display id");
                    }
                }
            }
        }

        public MediaProjectionInfo getProjectionInfo() {
            return new MediaProjectionInfo(this.packageName, this.userHandle);
        }

        boolean requiresForegroundService() {
            return this.mTargetSdkVersion >= 29 && !this.mIsPrivileged;
        }

        public void dump(PrintWriter printWriter) {
            printWriter.println("(" + this.packageName + ", uid=" + this.uid + "): " + MediaProjectionManagerService.typeToString(this.mType));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class MediaRouterCallback extends MediaRouter.SimpleCallback {
        private MediaRouterCallback() {
        }

        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public void onRouteSelected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            synchronized (MediaProjectionManagerService.this.mLock) {
                if ((i & 4) != 0) {
                    MediaProjectionManagerService.this.mMediaRouteInfo = routeInfo;
                    if (MediaProjectionManagerService.this.mProjectionGrant != null) {
                        MediaProjectionManagerService.this.mProjectionGrant.stop();
                    }
                    if (MediaProjectionManagerService.this.mOplusProjectionGrant != null) {
                        MediaProjectionManagerService.this.mOplusProjectionGrant.stop();
                    }
                }
            }
        }

        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public void onRouteUnselected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            if (MediaProjectionManagerService.this.mMediaRouteInfo == routeInfo) {
                MediaProjectionManagerService.this.mMediaRouteInfo = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class CallbackDelegate {
        private final Object mLock = new Object();
        private Handler mHandler = new Handler(Looper.getMainLooper(), null, true);
        private Map<IBinder, IMediaProjectionCallback> mClientCallbacks = new ArrayMap();
        private Map<IBinder, IMediaProjectionWatcherCallback> mWatcherCallbacks = new ArrayMap();

        public void add(IMediaProjectionCallback iMediaProjectionCallback) {
            synchronized (this.mLock) {
                this.mClientCallbacks.put(iMediaProjectionCallback.asBinder(), iMediaProjectionCallback);
            }
        }

        public void add(IMediaProjectionWatcherCallback iMediaProjectionWatcherCallback) {
            synchronized (this.mLock) {
                this.mWatcherCallbacks.put(iMediaProjectionWatcherCallback.asBinder(), iMediaProjectionWatcherCallback);
            }
        }

        public void remove(IMediaProjectionCallback iMediaProjectionCallback) {
            synchronized (this.mLock) {
                this.mClientCallbacks.remove(iMediaProjectionCallback.asBinder());
            }
        }

        public void remove(IMediaProjectionWatcherCallback iMediaProjectionWatcherCallback) {
            synchronized (this.mLock) {
                this.mWatcherCallbacks.remove(iMediaProjectionWatcherCallback.asBinder());
            }
        }

        public void dispatchStart(MediaProjection mediaProjection) {
            if (mediaProjection == null) {
                Slog.e(MediaProjectionManagerService.TAG, "Tried to dispatch start notification for a null media projection. Ignoring!");
                return;
            }
            synchronized (this.mLock) {
                for (IMediaProjectionWatcherCallback iMediaProjectionWatcherCallback : this.mWatcherCallbacks.values()) {
                    this.mHandler.post(new WatcherStartCallback(mediaProjection.getProjectionInfo(), iMediaProjectionWatcherCallback));
                }
            }
        }

        public void dispatchStop(MediaProjection mediaProjection) {
            if (mediaProjection == null) {
                Slog.e(MediaProjectionManagerService.TAG, "Tried to dispatch stop notification for a null media projection. Ignoring!");
                return;
            }
            synchronized (this.mLock) {
                Iterator<IMediaProjectionCallback> it = this.mClientCallbacks.values().iterator();
                while (it.hasNext()) {
                    this.mHandler.post(new ClientStopCallback(it.next()));
                }
                for (IMediaProjectionWatcherCallback iMediaProjectionWatcherCallback : this.mWatcherCallbacks.values()) {
                    this.mHandler.post(new WatcherStopCallback(mediaProjection.getProjectionInfo(), iMediaProjectionWatcherCallback));
                }
            }
        }

        public void dispatchResize(MediaProjection mediaProjection, final int i, final int i2) {
            if (mediaProjection == null) {
                Slog.e(MediaProjectionManagerService.TAG, "Tried to dispatch resize notification for a null media projection. Ignoring!");
                return;
            }
            synchronized (this.mLock) {
                for (final IMediaProjectionCallback iMediaProjectionCallback : this.mClientCallbacks.values()) {
                    this.mHandler.post(new Runnable() { // from class: com.android.server.media.projection.MediaProjectionManagerService$CallbackDelegate$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaProjectionManagerService.CallbackDelegate.lambda$dispatchResize$0(iMediaProjectionCallback, i, i2);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$dispatchResize$0(IMediaProjectionCallback iMediaProjectionCallback, int i, int i2) {
            try {
                iMediaProjectionCallback.onCapturedContentResize(i, i2);
            } catch (RemoteException e) {
                Slog.w(MediaProjectionManagerService.TAG, "Failed to notify media projection has resized to " + i + " x " + i2, e);
            }
        }

        public void dispatchVisibilityChanged(MediaProjection mediaProjection, final boolean z) {
            if (mediaProjection == null) {
                Slog.e(MediaProjectionManagerService.TAG, "Tried to dispatch visibility changed notification for a null media projection. Ignoring!");
                return;
            }
            synchronized (this.mLock) {
                for (final IMediaProjectionCallback iMediaProjectionCallback : this.mClientCallbacks.values()) {
                    this.mHandler.post(new Runnable() { // from class: com.android.server.media.projection.MediaProjectionManagerService$CallbackDelegate$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaProjectionManagerService.CallbackDelegate.lambda$dispatchVisibilityChanged$1(iMediaProjectionCallback, z);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$dispatchVisibilityChanged$1(IMediaProjectionCallback iMediaProjectionCallback, boolean z) {
            try {
                iMediaProjectionCallback.onCapturedContentVisibilityChanged(z);
            } catch (RemoteException e) {
                Slog.w(MediaProjectionManagerService.TAG, "Failed to notify media projection has captured content visibility change to " + z, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class WatcherStartCallback implements Runnable {
        private IMediaProjectionWatcherCallback mCallback;
        private MediaProjectionInfo mInfo;

        public WatcherStartCallback(MediaProjectionInfo mediaProjectionInfo, IMediaProjectionWatcherCallback iMediaProjectionWatcherCallback) {
            this.mInfo = mediaProjectionInfo;
            this.mCallback = iMediaProjectionWatcherCallback;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.mCallback.onStart(this.mInfo);
            } catch (RemoteException e) {
                Slog.w(MediaProjectionManagerService.TAG, "Failed to notify media projection has stopped", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class WatcherStopCallback implements Runnable {
        private IMediaProjectionWatcherCallback mCallback;
        private MediaProjectionInfo mInfo;

        public WatcherStopCallback(MediaProjectionInfo mediaProjectionInfo, IMediaProjectionWatcherCallback iMediaProjectionWatcherCallback) {
            this.mInfo = mediaProjectionInfo;
            this.mCallback = iMediaProjectionWatcherCallback;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.mCallback.onStop(this.mInfo);
            } catch (RemoteException e) {
                Slog.w(MediaProjectionManagerService.TAG, "Failed to notify media projection has stopped", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ClientStopCallback implements Runnable {
        private IMediaProjectionCallback mCallback;

        public ClientStopCallback(IMediaProjectionCallback iMediaProjectionCallback) {
            this.mCallback = iMediaProjectionCallback;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.mCallback.onStop();
            } catch (RemoteException e) {
                Slog.w(MediaProjectionManagerService.TAG, "Failed to notify media projection has stopped", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String typeToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? Integer.toString(i) : "TYPE_PRESENTATION" : "TYPE_MIRRORING" : "TYPE_SCREEN_CAPTURE";
    }
}
