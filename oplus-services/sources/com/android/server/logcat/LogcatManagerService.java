package com.android.server.logcat;

import android.app.ActivityManagerInternal;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.ILogd;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.logcat.ILogcatManagerService;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.ILogAccessDialogCallback;
import com.android.internal.util.ArrayUtils;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.policy.EventLogTags;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class LogcatManagerService extends SystemService {
    private static final boolean DEBUG = false;
    public static final String EXTRA_CALLBACK = "EXTRA_CALLBACK";
    private static final int MSG_APPROVE_LOG_ACCESS = 1;
    private static final int MSG_DECLINE_LOG_ACCESS = 2;
    private static final int MSG_LOG_ACCESS_FINISHED = 3;
    private static final int MSG_LOG_ACCESS_REQUESTED = 0;
    private static final int MSG_LOG_ACCESS_STATUS_EXPIRED = 5;
    private static final int MSG_PENDING_TIMEOUT = 4;

    @VisibleForTesting
    static final int PENDING_CONFIRMATION_TIMEOUT_MILLIS;
    private static final int STATUS_APPROVED = 2;
    private static final int STATUS_DECLINED = 3;

    @VisibleForTesting
    static final int STATUS_EXPIRATION_TIMEOUT_MILLIS = 60000;
    private static final int STATUS_NEW_REQUEST = 0;
    private static final int STATUS_PENDING = 1;
    private static final String TAG = "LogcatManagerService";
    private static final String TARGET_ACTIVITY_NAME = "com.android.systemui.logcat.LogAccessDialogActivity";
    private static final String TARGET_PACKAGE_NAME = "com.android.systemui";
    private final Map<LogAccessClient, Integer> mActiveLogAccessCount;
    private ActivityManagerInternal mActivityManagerInternal;
    private final BinderService mBinderService;
    private final Supplier<Long> mClock;
    private final Context mContext;
    private final LogAccessDialogCallback mDialogCallback;
    private final Handler mHandler;
    private final Injector mInjector;
    private final Map<LogAccessClient, LogAccessStatus> mLogAccessStatus;
    private ILogd mLogdService;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface LogAccessRequestStatus {
    }

    static {
        PENDING_CONFIRMATION_TIMEOUT_MILLIS = Build.IS_DEBUGGABLE ? EventLogTags.SCREEN_TOGGLED : 400000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class LogAccessClient {
        final String mPackageName;
        final int mUid;

        LogAccessClient(int i, String str) {
            this.mUid = i;
            this.mPackageName = str;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof LogAccessClient)) {
                return false;
            }
            LogAccessClient logAccessClient = (LogAccessClient) obj;
            return this.mUid == logAccessClient.mUid && Objects.equals(this.mPackageName, logAccessClient.mPackageName);
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mUid), this.mPackageName);
        }

        public String toString() {
            return "LogAccessClient{mUid=" + this.mUid + ", mPackageName=" + this.mPackageName + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class LogAccessRequest {
        final int mFd;
        final int mGid;
        final int mPid;
        final int mUid;

        private LogAccessRequest(int i, int i2, int i3, int i4) {
            this.mUid = i;
            this.mGid = i2;
            this.mPid = i3;
            this.mFd = i4;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof LogAccessRequest)) {
                return false;
            }
            LogAccessRequest logAccessRequest = (LogAccessRequest) obj;
            return this.mUid == logAccessRequest.mUid && this.mGid == logAccessRequest.mGid && this.mPid == logAccessRequest.mPid && this.mFd == logAccessRequest.mFd;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mUid), Integer.valueOf(this.mGid), Integer.valueOf(this.mPid), Integer.valueOf(this.mFd));
        }

        public String toString() {
            return "LogAccessRequest{mUid=" + this.mUid + ", mGid=" + this.mGid + ", mPid=" + this.mPid + ", mFd=" + this.mFd + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class LogAccessStatus {
        final List<LogAccessRequest> mPendingRequests;
        int mStatus;

        private LogAccessStatus() {
            this.mStatus = 0;
            this.mPendingRequests = new ArrayList();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class BinderService extends ILogcatManagerService.Stub {
        private BinderService() {
        }

        public void startThread(int i, int i2, int i3, int i4) {
            LogcatManagerService.this.mHandler.sendMessageAtTime(LogcatManagerService.this.mHandler.obtainMessage(0, new LogAccessRequest(i, i2, i3, i4)), ((Long) LogcatManagerService.this.mClock.get()).longValue());
        }

        public void finishThread(int i, int i2, int i3, int i4) {
            LogcatManagerService.this.mHandler.sendMessageAtTime(LogcatManagerService.this.mHandler.obtainMessage(3, new LogAccessRequest(i, i2, i3, i4)), ((Long) LogcatManagerService.this.mClock.get()).longValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class LogAccessDialogCallback extends ILogAccessDialogCallback.Stub {
        LogAccessDialogCallback() {
        }

        public void approveAccessForClient(int i, String str) {
            LogcatManagerService.this.mHandler.sendMessageAtTime(LogcatManagerService.this.mHandler.obtainMessage(1, new LogAccessClient(i, str)), ((Long) LogcatManagerService.this.mClock.get()).longValue());
        }

        public void declineAccessForClient(int i, String str) {
            LogcatManagerService.this.mHandler.sendMessageAtTime(LogcatManagerService.this.mHandler.obtainMessage(2, new LogAccessClient(i, str)), ((Long) LogcatManagerService.this.mClock.get()).longValue());
        }
    }

    private ILogd getLogdService() {
        if (this.mLogdService == null) {
            this.mLogdService = this.mInjector.getLogdService();
        }
        return this.mLogdService;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class LogAccessRequestHandler extends Handler {
        private final LogcatManagerService mService;

        LogAccessRequestHandler(Looper looper, LogcatManagerService logcatManagerService) {
            super(looper);
            this.mService = logcatManagerService;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                this.mService.onLogAccessRequested((LogAccessRequest) message.obj);
                return;
            }
            if (i == 1) {
                this.mService.onAccessApprovedForClient((LogAccessClient) message.obj);
                return;
            }
            if (i == 2) {
                this.mService.onAccessDeclinedForClient((LogAccessClient) message.obj);
                return;
            }
            if (i == 3) {
                this.mService.onLogAccessFinished((LogAccessRequest) message.obj);
            } else if (i == 4) {
                this.mService.onPendingTimeoutExpired((LogAccessClient) message.obj);
            } else {
                if (i != 5) {
                    return;
                }
                this.mService.onAccessStatusExpired((LogAccessClient) message.obj);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Injector {
        Injector() {
        }

        protected Supplier<Long> createClock() {
            return new Supplier() { // from class: com.android.server.logcat.LogcatManagerService$Injector$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final Object get() {
                    return Long.valueOf(SystemClock.uptimeMillis());
                }
            };
        }

        protected Looper getLooper() {
            return Looper.getMainLooper();
        }

        protected ILogd getLogdService() {
            return ILogd.Stub.asInterface(ServiceManager.getService("logd"));
        }
    }

    public LogcatManagerService(Context context) {
        this(context, new Injector());
    }

    public LogcatManagerService(Context context, Injector injector) {
        super(context);
        this.mLogAccessStatus = new ArrayMap();
        this.mActiveLogAccessCount = new ArrayMap();
        this.mContext = context;
        this.mInjector = injector;
        this.mClock = injector.createClock();
        this.mBinderService = new BinderService();
        this.mDialogCallback = new LogAccessDialogCallback();
        this.mHandler = new LogAccessRequestHandler(injector.getLooper(), this);
    }

    public void onStart() {
        try {
            this.mActivityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
            publishBinderService("logcat", this.mBinderService);
        } catch (Throwable th) {
            Slog.e(TAG, "Could not start the LogcatManagerService.", th);
        }
    }

    @VisibleForTesting
    LogAccessDialogCallback getDialogCallback() {
        return this.mDialogCallback;
    }

    @VisibleForTesting
    ILogcatManagerService getBinderService() {
        return this.mBinderService;
    }

    private LogAccessClient getClientForRequest(LogAccessRequest logAccessRequest) {
        String packageName = getPackageName(logAccessRequest);
        if (packageName == null) {
            return null;
        }
        return new LogAccessClient(logAccessRequest.mUid, packageName);
    }

    private String getPackageName(LogAccessRequest logAccessRequest) {
        PackageManager packageManager = this.mContext.getPackageManager();
        if (packageManager == null) {
            Slog.e(TAG, "PackageManager is null, declining the logd access");
            return null;
        }
        String[] packagesForUid = packageManager.getPackagesForUid(logAccessRequest.mUid);
        if (ArrayUtils.isEmpty(packagesForUid)) {
            Slog.e(TAG, "Unknown calling package name, declining the logd access");
            return null;
        }
        ActivityManagerInternal activityManagerInternal = this.mActivityManagerInternal;
        if (activityManagerInternal != null) {
            int i = logAccessRequest.mPid;
            String packageNameByPid = activityManagerInternal.getPackageNameByPid(i);
            while (true) {
                if ((packageNameByPid == null || !ArrayUtils.contains(packagesForUid, packageNameByPid)) && i != -1) {
                    i = Process.getParentPid(i);
                    packageNameByPid = this.mActivityManagerInternal.getPackageNameByPid(i);
                }
            }
            if (packageNameByPid != null && ArrayUtils.contains(packagesForUid, packageNameByPid)) {
                return packageNameByPid;
            }
        }
        Arrays.sort(packagesForUid);
        String str = packagesForUid[0];
        if (str != null && !str.isEmpty()) {
            return str;
        }
        Slog.e(TAG, "Unknown calling package name, declining the logd access");
        return null;
    }

    void onLogAccessRequested(LogAccessRequest logAccessRequest) {
        LogAccessClient clientForRequest = getClientForRequest(logAccessRequest);
        if (clientForRequest == null) {
            declineRequest(logAccessRequest);
            return;
        }
        LogAccessStatus logAccessStatus = this.mLogAccessStatus.get(clientForRequest);
        if (logAccessStatus == null) {
            logAccessStatus = new LogAccessStatus();
            this.mLogAccessStatus.put(clientForRequest, logAccessStatus);
        }
        int i = logAccessStatus.mStatus;
        if (i == 0) {
            logAccessStatus.mPendingRequests.add(logAccessRequest);
            processNewLogAccessRequest(clientForRequest);
        } else if (i == 1) {
            logAccessStatus.mPendingRequests.add(logAccessRequest);
        } else if (i == 2) {
            approveRequest(clientForRequest, logAccessRequest);
        } else {
            if (i != 3) {
                return;
            }
            declineRequest(logAccessRequest);
        }
    }

    private boolean shouldShowConfirmationDialog(LogAccessClient logAccessClient) {
        return this.mActivityManagerInternal.getUidProcessState(logAccessClient.mUid) == 2;
    }

    private void processNewLogAccessRequest(LogAccessClient logAccessClient) {
        if (this.mActivityManagerInternal.getInstrumentationSourceUid(logAccessClient.mUid) != -1) {
            onAccessApprovedForClient(logAccessClient);
            return;
        }
        if (!shouldShowConfirmationDialog(logAccessClient)) {
            onAccessDeclinedForClient(logAccessClient);
            return;
        }
        this.mLogAccessStatus.get(logAccessClient).mStatus = 1;
        Handler handler = this.mHandler;
        handler.sendMessageAtTime(handler.obtainMessage(4, logAccessClient), this.mClock.get().longValue() + PENDING_CONFIRMATION_TIMEOUT_MILLIS);
        Intent createIntent = createIntent(logAccessClient);
        createIntent.setFlags(268435456);
        createIntent.setComponent(new ComponentName(TARGET_PACKAGE_NAME, TARGET_ACTIVITY_NAME));
        this.mContext.startActivityAsUser(createIntent, UserHandle.SYSTEM);
    }

    void onAccessApprovedForClient(LogAccessClient logAccessClient) {
        scheduleStatusExpiry(logAccessClient);
        LogAccessStatus logAccessStatus = this.mLogAccessStatus.get(logAccessClient);
        if (logAccessStatus != null) {
            Iterator<LogAccessRequest> it = logAccessStatus.mPendingRequests.iterator();
            while (it.hasNext()) {
                approveRequest(logAccessClient, it.next());
            }
            logAccessStatus.mStatus = 2;
            logAccessStatus.mPendingRequests.clear();
        }
    }

    void onAccessDeclinedForClient(LogAccessClient logAccessClient) {
        scheduleStatusExpiry(logAccessClient);
        LogAccessStatus logAccessStatus = this.mLogAccessStatus.get(logAccessClient);
        if (logAccessStatus != null) {
            Iterator<LogAccessRequest> it = logAccessStatus.mPendingRequests.iterator();
            while (it.hasNext()) {
                declineRequest(it.next());
            }
            logAccessStatus.mStatus = 3;
            logAccessStatus.mPendingRequests.clear();
        }
    }

    private void scheduleStatusExpiry(LogAccessClient logAccessClient) {
        this.mHandler.removeMessages(4, logAccessClient);
        this.mHandler.removeMessages(5, logAccessClient);
        Handler handler = this.mHandler;
        handler.sendMessageAtTime(handler.obtainMessage(5, logAccessClient), this.mClock.get().longValue() + 60000);
    }

    void onPendingTimeoutExpired(LogAccessClient logAccessClient) {
        LogAccessStatus logAccessStatus = this.mLogAccessStatus.get(logAccessClient);
        if (logAccessStatus == null || logAccessStatus.mStatus != 1) {
            return;
        }
        onAccessDeclinedForClient(logAccessClient);
    }

    void onAccessStatusExpired(LogAccessClient logAccessClient) {
        this.mLogAccessStatus.remove(logAccessClient);
    }

    void onLogAccessFinished(LogAccessRequest logAccessRequest) {
        LogAccessClient clientForRequest = getClientForRequest(logAccessRequest);
        int intValue = this.mActiveLogAccessCount.getOrDefault(clientForRequest, 1).intValue() - 1;
        if (intValue == 0) {
            this.mActiveLogAccessCount.remove(clientForRequest);
        } else {
            this.mActiveLogAccessCount.put(clientForRequest, Integer.valueOf(intValue));
        }
    }

    private void approveRequest(LogAccessClient logAccessClient, LogAccessRequest logAccessRequest) {
        try {
            try {
                getLogdService().approve(logAccessRequest.mUid, logAccessRequest.mGid, logAccessRequest.mPid, logAccessRequest.mFd);
            } catch (DeadObjectException unused) {
                Slog.w(TAG, "Logd connection no longer valid while approving, trying once more.");
                this.mLogdService = null;
                getLogdService().approve(logAccessRequest.mUid, logAccessRequest.mGid, logAccessRequest.mPid, logAccessRequest.mFd);
            }
            this.mActiveLogAccessCount.put(logAccessClient, Integer.valueOf(this.mActiveLogAccessCount.getOrDefault(logAccessClient, 0).intValue() + 1));
        } catch (RemoteException e) {
            Slog.e(TAG, "Fails to call remote functions", e);
        }
    }

    private void declineRequest(LogAccessRequest logAccessRequest) {
        try {
            try {
                getLogdService().decline(logAccessRequest.mUid, logAccessRequest.mGid, logAccessRequest.mPid, logAccessRequest.mFd);
            } catch (DeadObjectException unused) {
                Slog.w(TAG, "Logd connection no longer valid while declining, trying once more.");
                this.mLogdService = null;
                getLogdService().decline(logAccessRequest.mUid, logAccessRequest.mGid, logAccessRequest.mPid, logAccessRequest.mFd);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Fails to call remote functions", e);
        }
    }

    public Intent createIntent(LogAccessClient logAccessClient) {
        Intent intent = new Intent();
        intent.setFlags(268468224);
        intent.putExtra("android.intent.extra.PACKAGE_NAME", logAccessClient.mPackageName);
        intent.putExtra("android.intent.extra.UID", logAccessClient.mUid);
        intent.putExtra(EXTRA_CALLBACK, this.mDialogCallback.asBinder());
        return intent;
    }
}
