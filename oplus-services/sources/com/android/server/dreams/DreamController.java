package com.android.server.dreams;

import android.app.ActivityTaskManager;
import android.app.BroadcastOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.service.dreams.IDreamService;
import android.util.Slog;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.IDeviceIdleControllerExt;
import com.android.server.dreams.DreamController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DreamController {
    private static final int DREAM_CONNECTION_TIMEOUT = 10000;
    private static final int DREAM_FINISH_TIMEOUT = 5000;
    private static final String EXTRA_REASON_KEY = "reason";
    private static final String EXTRA_REASON_VALUE = "dream";
    private static final String TAG = "DreamController";
    private final ActivityTaskManager mActivityTaskManager;
    private final Intent mCloseNotificationShadeIntent;
    private final Bundle mCloseNotificationShadeOptions;
    private final Context mContext;
    private DreamRecord mCurrentDream;
    private final Handler mHandler;
    private final Listener mListener;
    private final PowerManager mPowerManager;
    private final boolean mResetScreenTimeoutOnUnexpectedDreamExit;
    private static final String DREAMING_DELIVERY_GROUP_NAMESPACE = UUID.randomUUID().toString();
    private static final String DREAMING_DELIVERY_GROUP_KEY = UUID.randomUUID().toString();
    private final Intent mDreamingStartedIntent = new Intent("android.intent.action.DREAMING_STARTED").addFlags(1342177280);
    private final Intent mDreamingStoppedIntent = new Intent("android.intent.action.DREAMING_STOPPED").addFlags(1342177280);
    private final Bundle mDreamingStartedStoppedOptions = createDreamingStartedStoppedOptions();
    private boolean mSentStartBroadcast = false;
    private final ArrayList<DreamRecord> mPreviousDreams = new ArrayList<>();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Listener {
        void onDreamStarted(Binder binder);

        void onDreamStopped(Binder binder);
    }

    public DreamController(Context context, Handler handler, Listener listener) {
        this.mContext = context;
        this.mHandler = handler;
        this.mListener = listener;
        this.mActivityTaskManager = (ActivityTaskManager) context.getSystemService(ActivityTaskManager.class);
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
        Intent intent = new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        this.mCloseNotificationShadeIntent = intent;
        intent.putExtra(EXTRA_REASON_KEY, EXTRA_REASON_VALUE);
        intent.addFlags(AudioFormat.EVRC);
        this.mCloseNotificationShadeOptions = BroadcastOptions.makeBasic().setDeliveryGroupPolicy(1).setDeliveryGroupMatchingKey("android.intent.action.CLOSE_SYSTEM_DIALOGS", EXTRA_REASON_VALUE).setDeferralPolicy(2).toBundle();
        this.mResetScreenTimeoutOnUnexpectedDreamExit = context.getResources().getBoolean(17891795);
    }

    private Bundle createDreamingStartedStoppedOptions() {
        BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
        makeBasic.setDeliveryGroupPolicy(1);
        makeBasic.setDeliveryGroupMatchingKey(DREAMING_DELIVERY_GROUP_NAMESPACE, DREAMING_DELIVERY_GROUP_KEY);
        makeBasic.setDeferralPolicy(2);
        return makeBasic.toBundle();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("Dreamland:");
        if (this.mCurrentDream != null) {
            printWriter.println("  mCurrentDream:");
            printWriter.println("    mToken=" + this.mCurrentDream.mToken);
            printWriter.println("    mName=" + this.mCurrentDream.mName);
            printWriter.println("    mIsPreviewMode=" + this.mCurrentDream.mIsPreviewMode);
            printWriter.println("    mCanDoze=" + this.mCurrentDream.mCanDoze);
            printWriter.println("    mUserId=" + this.mCurrentDream.mUserId);
            printWriter.println("    mBound=" + this.mCurrentDream.mBound);
            printWriter.println("    mService=" + this.mCurrentDream.mService);
            printWriter.println("    mWakingGently=" + this.mCurrentDream.mWakingGently);
        } else {
            printWriter.println("  mCurrentDream: null");
        }
        printWriter.println("  mSentStartBroadcast=" + this.mSentStartBroadcast);
    }

    public void startDream(Binder binder, ComponentName componentName, boolean z, boolean z2, int i, PowerManager.WakeLock wakeLock, ComponentName componentName2, String str) {
        long j;
        DreamRecord dreamRecord;
        Trace.traceBegin(131072L, "startDream");
        try {
            this.mContext.sendBroadcastAsUser(this.mCloseNotificationShadeIntent, UserHandle.ALL, null, this.mCloseNotificationShadeOptions);
            Slog.i(TAG, "Starting dream: name=" + componentName + ", isPreviewMode=" + z + ", canDoze=" + z2 + ", userId=" + i + ", reason='" + str + "'");
            dreamRecord = this.mCurrentDream;
        } catch (Throwable th) {
            th = th;
            j = 131072;
        }
        try {
            DreamRecord dreamRecord2 = new DreamRecord(binder, componentName, z, z2, i, wakeLock);
            this.mCurrentDream = dreamRecord2;
            if (dreamRecord != null) {
                if (Objects.equals(dreamRecord.mName, dreamRecord2.mName)) {
                    stopDreamInstance(true, "restarting same dream", dreamRecord);
                } else {
                    this.mPreviousDreams.add(dreamRecord);
                }
            }
            this.mCurrentDream.mDreamStartTime = SystemClock.elapsedRealtime();
            MetricsLogger.visible(this.mContext, this.mCurrentDream.mCanDoze ? FrameworkStatsLog.EXCLUSION_RECT_STATE_CHANGED : 222);
            Intent intent = new Intent("android.service.dreams.DreamService");
            intent.setComponent(componentName);
            intent.addFlags(AudioDevice.OUT_IP);
            intent.putExtra("android.service.dream.DreamService.dream_overlay_component", componentName2);
            try {
                if (!this.mContext.bindServiceAsUser(intent, this.mCurrentDream, 71303169, new UserHandle(i))) {
                    Slog.e(TAG, "Unable to bind dream service: " + intent);
                    stopDream(true, "bindService failed");
                    Trace.traceEnd(131072L);
                    return;
                }
                DreamRecord dreamRecord3 = this.mCurrentDream;
                dreamRecord3.mBound = true;
                this.mHandler.postDelayed(dreamRecord3.mStopUnconnectedDreamRunnable, IDeviceIdleControllerExt.ADVANCE_TIME);
                Trace.traceEnd(131072L);
            } catch (SecurityException e) {
                Slog.e(TAG, "Unable to bind dream service: " + intent, e);
                stopDream(true, "unable to bind service: SecExp.");
                Trace.traceEnd(131072L);
            }
        } catch (Throwable th2) {
            th = th2;
            j = 131072;
            Trace.traceEnd(j);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetScreenTimeout() {
        Slog.i(TAG, "Resetting screen timeout");
        this.mPowerManager.userActivity(SystemClock.uptimeMillis(), 0, 1);
    }

    public void stopDream(boolean z, String str) {
        stopPreviousDreams();
        stopDreamInstance(z, str, this.mCurrentDream);
    }

    private void stopDreamInstance(boolean z, String str, DreamRecord dreamRecord) {
        String str2;
        if (dreamRecord == null) {
            return;
        }
        Trace.traceBegin(131072L, "stopDream");
        if (!z) {
            try {
                if (dreamRecord.mWakingGently) {
                    return;
                }
                if (dreamRecord.mService != null) {
                    dreamRecord.mWakingGently = true;
                    try {
                        dreamRecord.mStopReason = str;
                        dreamRecord.mService.wakeUp();
                        this.mHandler.postDelayed(dreamRecord.mStopStubbornDreamRunnable, 5000L);
                        return;
                    } catch (RemoteException unused) {
                    }
                }
            } finally {
                Trace.traceEnd(131072L);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Stopping dream: name=");
        sb.append(dreamRecord.mName);
        sb.append(", isPreviewMode=");
        sb.append(dreamRecord.mIsPreviewMode);
        sb.append(", canDoze=");
        sb.append(dreamRecord.mCanDoze);
        sb.append(", userId=");
        sb.append(dreamRecord.mUserId);
        sb.append(", reason='");
        sb.append(str);
        sb.append("'");
        if (dreamRecord.mStopReason == null) {
            str2 = "";
        } else {
            str2 = "(from '" + dreamRecord.mStopReason + "')";
        }
        sb.append(str2);
        Slog.i(TAG, sb.toString());
        MetricsLogger.hidden(this.mContext, dreamRecord.mCanDoze ? FrameworkStatsLog.EXCLUSION_RECT_STATE_CHANGED : 222);
        MetricsLogger.histogram(this.mContext, dreamRecord.mCanDoze ? "dozing_minutes" : "dreaming_minutes", (int) ((SystemClock.elapsedRealtime() - dreamRecord.mDreamStartTime) / 60000));
        this.mHandler.removeCallbacks(dreamRecord.mStopUnconnectedDreamRunnable);
        this.mHandler.removeCallbacks(dreamRecord.mStopStubbornDreamRunnable);
        IDreamService iDreamService = dreamRecord.mService;
        if (iDreamService != null) {
            try {
                iDreamService.detach();
            } catch (RemoteException unused2) {
            }
            try {
                dreamRecord.mService.asBinder().unlinkToDeath(dreamRecord, 0);
            } catch (NoSuchElementException unused3) {
            }
            dreamRecord.mService = null;
        }
        if (dreamRecord.mBound) {
            this.mContext.unbindService(dreamRecord);
        }
        dreamRecord.releaseWakeLockIfNeeded();
        if (dreamRecord == this.mCurrentDream) {
            this.mCurrentDream = null;
            if (this.mSentStartBroadcast) {
                this.mContext.sendBroadcastAsUser(this.mDreamingStoppedIntent, UserHandle.ALL, null, this.mDreamingStartedStoppedOptions);
                this.mSentStartBroadcast = false;
            }
            this.mActivityTaskManager.removeRootTasksWithActivityTypes(new int[]{5});
            this.mListener.onDreamStopped(dreamRecord.mToken);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopPreviousDreams() {
        if (this.mPreviousDreams.isEmpty()) {
            return;
        }
        Iterator<DreamRecord> it = this.mPreviousDreams.iterator();
        while (it.hasNext()) {
            stopDreamInstance(true, "stop previous dream", it.next());
            it.remove();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attach(IDreamService iDreamService) {
        try {
            iDreamService.asBinder().linkToDeath(this.mCurrentDream, 0);
            DreamRecord dreamRecord = this.mCurrentDream;
            iDreamService.attach(dreamRecord.mToken, dreamRecord.mCanDoze, dreamRecord.mIsPreviewMode, dreamRecord.mDreamingStartedCallback);
            DreamRecord dreamRecord2 = this.mCurrentDream;
            dreamRecord2.mService = iDreamService;
            if (dreamRecord2.mIsPreviewMode || this.mSentStartBroadcast) {
                return;
            }
            this.mContext.sendBroadcastAsUser(this.mDreamingStartedIntent, UserHandle.ALL, null, this.mDreamingStartedStoppedOptions);
            this.mListener.onDreamStarted(this.mCurrentDream.mToken);
            this.mSentStartBroadcast = true;
        } catch (RemoteException e) {
            Slog.e(TAG, "The dream service died unexpectedly.", e);
            stopDream(true, "attach failed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DreamRecord implements IBinder.DeathRecipient, ServiceConnection {
        public boolean mBound;
        public final boolean mCanDoze;
        public boolean mConnected;
        private long mDreamStartTime;
        private final IRemoteCallback mDreamingStartedCallback;
        public final boolean mIsPreviewMode;
        public final ComponentName mName;
        private final Runnable mReleaseWakeLockIfNeeded;
        public IDreamService mService;
        private final Runnable mStopPreviousDreamsIfNeeded = new Runnable() { // from class: com.android.server.dreams.DreamController$DreamRecord$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DreamController.DreamRecord.this.stopPreviousDreamsIfNeeded();
            }
        };
        private String mStopReason;
        private final Runnable mStopStubbornDreamRunnable;
        private final Runnable mStopUnconnectedDreamRunnable;
        public final Binder mToken;
        public final int mUserId;
        public PowerManager.WakeLock mWakeLock;
        public boolean mWakingGently;

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            if (!this.mBound || this.mConnected) {
                return;
            }
            Slog.w(DreamController.TAG, "Bound dream did not connect in the time allotted");
            DreamController.this.stopDream(true, "slow to connect");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1() {
            Slog.w(DreamController.TAG, "Stubborn dream did not finish itself in the time allotted");
            DreamController.this.stopDream(true, "slow to finish");
            this.mStopReason = null;
        }

        DreamRecord(Binder binder, ComponentName componentName, boolean z, boolean z2, int i, PowerManager.WakeLock wakeLock) {
            Runnable runnable = new Runnable() { // from class: com.android.server.dreams.DreamController$DreamRecord$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    DreamController.DreamRecord.this.releaseWakeLockIfNeeded();
                }
            };
            this.mReleaseWakeLockIfNeeded = runnable;
            this.mStopUnconnectedDreamRunnable = new Runnable() { // from class: com.android.server.dreams.DreamController$DreamRecord$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    DreamController.DreamRecord.this.lambda$new$0();
                }
            };
            this.mStopStubbornDreamRunnable = new Runnable() { // from class: com.android.server.dreams.DreamController$DreamRecord$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    DreamController.DreamRecord.this.lambda$new$1();
                }
            };
            this.mDreamingStartedCallback = new IRemoteCallback.Stub() { // from class: com.android.server.dreams.DreamController.DreamRecord.1
                public void sendResult(Bundle bundle) {
                    DreamController.this.mHandler.post(DreamRecord.this.mStopPreviousDreamsIfNeeded);
                    DreamController.this.mHandler.post(DreamRecord.this.mReleaseWakeLockIfNeeded);
                }
            };
            this.mToken = binder;
            this.mName = componentName;
            this.mIsPreviewMode = z;
            this.mCanDoze = z2;
            this.mUserId = i;
            this.mWakeLock = wakeLock;
            if (wakeLock != null) {
                wakeLock.acquire();
            }
            DreamController.this.mHandler.postDelayed(runnable, IDeviceIdleControllerExt.ADVANCE_TIME);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            DreamController.this.mHandler.post(new Runnable() { // from class: com.android.server.dreams.DreamController$DreamRecord$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    DreamController.DreamRecord.this.lambda$binderDied$2();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$binderDied$2() {
            this.mService = null;
            if (DreamController.this.mCurrentDream == this) {
                if (DreamController.this.mResetScreenTimeoutOnUnexpectedDreamExit) {
                    DreamController.this.resetScreenTimeout();
                }
                DreamController.this.stopDream(true, "binder died");
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, final IBinder iBinder) {
            DreamController.this.mHandler.post(new Runnable() { // from class: com.android.server.dreams.DreamController$DreamRecord$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    DreamController.DreamRecord.this.lambda$onServiceConnected$3(iBinder);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceConnected$3(IBinder iBinder) {
            this.mConnected = true;
            if (DreamController.this.mCurrentDream == this && this.mService == null) {
                DreamController.this.attach(IDreamService.Stub.asInterface(iBinder));
            } else {
                releaseWakeLockIfNeeded();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            DreamController.this.mHandler.post(new Runnable() { // from class: com.android.server.dreams.DreamController$DreamRecord$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    DreamController.DreamRecord.this.lambda$onServiceDisconnected$4();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceDisconnected$4() {
            this.mService = null;
            if (DreamController.this.mCurrentDream == this) {
                if (DreamController.this.mResetScreenTimeoutOnUnexpectedDreamExit) {
                    DreamController.this.resetScreenTimeout();
                }
                DreamController.this.stopDream(true, "service disconnected");
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void stopPreviousDreamsIfNeeded() {
            if (DreamController.this.mCurrentDream == this) {
                DreamController.this.stopPreviousDreams();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void releaseWakeLockIfNeeded() {
            PowerManager.WakeLock wakeLock = this.mWakeLock;
            if (wakeLock != null) {
                wakeLock.release();
                this.mWakeLock = null;
                DreamController.this.mHandler.removeCallbacks(this.mReleaseWakeLockIfNeeded);
            }
        }
    }
}
