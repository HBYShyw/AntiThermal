package com.android.server.voiceinteraction;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AppGlobals;
import android.app.ApplicationExitInfo;
import android.app.IActivityManager;
import android.app.IActivityTaskManager;
import android.app.ProfilerInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ParceledListSlice;
import android.content.pm.ServiceInfo;
import android.hardware.soundtrigger.IRecognitionStatusCallback;
import android.hardware.soundtrigger.SoundTrigger;
import android.media.AudioFormat;
import android.media.permission.Identity;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SharedMemory;
import android.os.UserHandle;
import android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback;
import android.service.voice.IVisualQueryDetectionVoiceInteractionCallback;
import android.service.voice.IVoiceInteractionService;
import android.service.voice.IVoiceInteractionSession;
import android.service.voice.VoiceInteractionServiceInfo;
import android.system.OsConstants;
import android.text.TextUtils;
import android.util.PrintWriterPrinter;
import android.util.Slog;
import android.view.IWindowManager;
import com.android.internal.app.IHotwordRecognitionStatusCallback;
import com.android.internal.app.IVisualQueryDetectionAttentionListener;
import com.android.internal.app.IVoiceActionCheckCallback;
import com.android.internal.app.IVoiceInteractionSessionShowCallback;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.util.function.HexConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.LocalServices;
import com.android.server.policy.PhoneWindowManager;
import com.android.server.voiceinteraction.HotwordDetectionConnection;
import com.android.server.voiceinteraction.VoiceInteractionManagerService;
import com.android.server.voiceinteraction.VoiceInteractionSessionConnection;
import com.android.server.wm.ActivityAssistInfo;
import com.android.server.wm.ActivityTaskManagerInternal;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class VoiceInteractionManagerServiceImpl implements VoiceInteractionSessionConnection.Callback {
    static final String CLOSE_REASON_VOICE_INTERACTION = "voiceinteraction";
    static final boolean DEBUG = false;
    private static final long REQUEST_DIRECT_ACTIONS_RETRY_TIME_MS = 200;
    static final String TAG = "VoiceInteractionServiceManager";
    VoiceInteractionSessionConnection mActiveSession;
    final IActivityManager mAm;
    final IActivityTaskManager mAtm;
    boolean mBound = false;
    final BroadcastReceiver mBroadcastReceiver;
    final ComponentName mComponent;
    final ServiceConnection mConnection;
    final Context mContext;
    final Handler mDirectActionsHandler;
    int mDisabledShowContext;
    final Handler mHandler;
    final ComponentName mHotwordDetectionComponentName;
    volatile HotwordDetectionConnection mHotwordDetectionConnection;
    final IWindowManager mIWindowManager;
    final VoiceInteractionServiceInfo mInfo;
    final PackageManagerInternal mPackageManagerInternal;
    IVoiceInteractionService mService;
    final VoiceInteractionManagerService.VoiceInteractionManagerServiceStub mServiceStub;
    final ComponentName mSessionComponentName;
    final int mUser;
    final boolean mValid;
    final ComponentName mVisualQueryDetectionComponentName;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface DetectorRemoteExceptionListener {
        void onDetectorRemoteException(IBinder iBinder, int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VoiceInteractionManagerServiceImpl(Context context, Handler handler, VoiceInteractionManagerService.VoiceInteractionManagerServiceStub voiceInteractionManagerServiceStub, int i, ComponentName componentName) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                    String stringExtra = intent.getStringExtra(PhoneWindowManager.SYSTEM_DIALOG_REASON_KEY);
                    if (VoiceInteractionManagerServiceImpl.CLOSE_REASON_VOICE_INTERACTION.equals(stringExtra) || TextUtils.equals("dream", stringExtra) || PhoneWindowManager.SYSTEM_DIALOG_REASON_ASSIST.equals(stringExtra)) {
                        return;
                    }
                    synchronized (VoiceInteractionManagerServiceImpl.this.mServiceStub) {
                        VoiceInteractionSessionConnection voiceInteractionSessionConnection = VoiceInteractionManagerServiceImpl.this.mActiveSession;
                        if (voiceInteractionSessionConnection != null && voiceInteractionSessionConnection.mSession != null) {
                            try {
                                if (!voiceInteractionSessionConnection.mShown && PhoneWindowManager.SYSTEM_DIALOG_REASON_ASSIST.equals(stringExtra)) {
                                    Slog.e(VoiceInteractionManagerServiceImpl.TAG, "no active session show when closing system dialogs");
                                    return;
                                }
                                VoiceInteractionManagerServiceImpl.this.mActiveSession.mSession.closeSystemDialogs();
                            } catch (RemoteException unused) {
                            }
                        }
                    }
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mConnection = new ServiceConnection() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.2
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName2, IBinder iBinder) {
                synchronized (VoiceInteractionManagerServiceImpl.this.mServiceStub) {
                    VoiceInteractionManagerServiceImpl.this.mService = IVoiceInteractionService.Stub.asInterface(iBinder);
                    try {
                        VoiceInteractionManagerServiceImpl.this.mService.ready();
                    } catch (RemoteException unused) {
                    }
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName2) {
                synchronized (VoiceInteractionManagerServiceImpl.this.mServiceStub) {
                    VoiceInteractionManagerServiceImpl voiceInteractionManagerServiceImpl = VoiceInteractionManagerServiceImpl.this;
                    voiceInteractionManagerServiceImpl.mService = null;
                    voiceInteractionManagerServiceImpl.resetHotwordDetectionConnectionLocked();
                }
            }

            @Override // android.content.ServiceConnection
            public void onBindingDied(ComponentName componentName2) {
                ParceledListSlice parceledListSlice;
                Slog.d(VoiceInteractionManagerServiceImpl.TAG, "onBindingDied to " + componentName2);
                String packageName = componentName2.getPackageName();
                try {
                    VoiceInteractionManagerServiceImpl voiceInteractionManagerServiceImpl = VoiceInteractionManagerServiceImpl.this;
                    parceledListSlice = voiceInteractionManagerServiceImpl.mAm.getHistoricalProcessExitReasons(packageName, 0, 1, voiceInteractionManagerServiceImpl.mUser);
                } catch (RemoteException unused) {
                    parceledListSlice = null;
                }
                if (parceledListSlice == null) {
                    return;
                }
                List list = parceledListSlice.getList();
                if (list.isEmpty()) {
                    return;
                }
                ApplicationExitInfo applicationExitInfo = (ApplicationExitInfo) list.get(0);
                if (applicationExitInfo.getReason() == 10 && applicationExitInfo.getSubReason() == 23) {
                    VoiceInteractionManagerServiceImpl voiceInteractionManagerServiceImpl2 = VoiceInteractionManagerServiceImpl.this;
                    voiceInteractionManagerServiceImpl2.mServiceStub.handleUserStop(packageName, voiceInteractionManagerServiceImpl2.mUser);
                }
            }
        };
        this.mContext = context;
        this.mHandler = handler;
        this.mDirectActionsHandler = new Handler(true);
        this.mServiceStub = voiceInteractionManagerServiceStub;
        this.mUser = i;
        this.mComponent = componentName;
        this.mAm = ActivityManager.getService();
        this.mAtm = ActivityTaskManager.getService();
        PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        Objects.requireNonNull(packageManagerInternal);
        this.mPackageManagerInternal = packageManagerInternal;
        try {
            VoiceInteractionServiceInfo voiceInteractionServiceInfo = new VoiceInteractionServiceInfo(context.getPackageManager(), componentName, i);
            this.mInfo = voiceInteractionServiceInfo;
            if (voiceInteractionServiceInfo.getParseError() != null) {
                Slog.w(TAG, "Bad voice interaction service: " + voiceInteractionServiceInfo.getParseError());
                this.mSessionComponentName = null;
                this.mHotwordDetectionComponentName = null;
                this.mVisualQueryDetectionComponentName = null;
                this.mIWindowManager = null;
                this.mValid = false;
                return;
            }
            this.mValid = true;
            this.mSessionComponentName = new ComponentName(componentName.getPackageName(), voiceInteractionServiceInfo.getSessionService());
            String hotwordDetectionService = voiceInteractionServiceInfo.getHotwordDetectionService();
            this.mHotwordDetectionComponentName = hotwordDetectionService != null ? new ComponentName(componentName.getPackageName(), hotwordDetectionService) : null;
            String visualQueryDetectionService = voiceInteractionServiceInfo.getVisualQueryDetectionService();
            this.mVisualQueryDetectionComponentName = visualQueryDetectionService != null ? new ComponentName(componentName.getPackageName(), visualQueryDetectionService) : null;
            this.mIWindowManager = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
            context.registerReceiver(broadcastReceiver, intentFilter, null, handler, 2);
        } catch (PackageManager.NameNotFoundException e) {
            Slog.w(TAG, "Voice interaction service not found: " + componentName, e);
            this.mInfo = null;
            this.mSessionComponentName = null;
            this.mHotwordDetectionComponentName = null;
            this.mVisualQueryDetectionComponentName = null;
            this.mIWindowManager = null;
            this.mValid = false;
        }
    }

    public void grantImplicitAccessLocked(int i, Intent intent) {
        int appId = UserHandle.getAppId(i);
        this.mPackageManagerInternal.grantImplicitAccess(UserHandle.getUserId(i), intent, appId, this.mInfo.getServiceInfo().applicationInfo.uid, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0034  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x009f  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0058 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean showSessionLocked(Bundle bundle, int i, String str, IVoiceInteractionSessionShowCallback iVoiceInteractionSessionShowCallback, IBinder iBinder) {
        List<ActivityAssistInfo> list;
        int nextShowSessionId = this.mServiceStub.getNextShowSessionId();
        Bundle bundle2 = bundle == null ? new Bundle() : bundle;
        bundle2.putInt("android.service.voice.SHOW_SESSION_ID", nextShowSessionId);
        try {
            IVoiceInteractionService iVoiceInteractionService = this.mService;
            if (iVoiceInteractionService != null) {
                try {
                    iVoiceInteractionService.prepareToShowSession(bundle2, i);
                } catch (RemoteException e) {
                    e = e;
                    Slog.w(TAG, "RemoteException while calling prepareToShowSession", e);
                    if (this.mActiveSession == null) {
                    }
                    if (!this.mActiveSession.mBound) {
                    }
                    List<ActivityAssistInfo> topVisibleActivities = ((ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class)).getTopVisibleActivities();
                    if (iBinder == null) {
                    }
                    return this.mActiveSession.showLocked(bundle2, i, str, this.mDisabledShowContext, iVoiceInteractionSessionShowCallback, list);
                }
            }
        } catch (RemoteException e2) {
            e = e2;
        }
        if (this.mActiveSession == null) {
            this.mActiveSession = new VoiceInteractionSessionConnection(this.mServiceStub, this.mSessionComponentName, this.mUser, this.mContext, this, this.mInfo.getServiceInfo().applicationInfo.uid, this.mHandler);
        }
        if (!this.mActiveSession.mBound) {
            try {
                if (this.mService != null) {
                    Bundle bundle3 = new Bundle();
                    bundle3.putInt("android.service.voice.SHOW_SESSION_ID", nextShowSessionId);
                    this.mService.showSessionFailed(bundle3);
                }
            } catch (RemoteException e3) {
                Slog.w(TAG, "RemoteException while calling showSessionFailed", e3);
            }
        }
        List<ActivityAssistInfo> topVisibleActivities2 = ((ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class)).getTopVisibleActivities();
        if (iBinder == null) {
            ArrayList arrayList = new ArrayList();
            int size = topVisibleActivities2.size();
            int i2 = 0;
            while (true) {
                if (i2 >= size) {
                    break;
                }
                ActivityAssistInfo activityAssistInfo = topVisibleActivities2.get(i2);
                if (activityAssistInfo.getActivityToken() == iBinder) {
                    arrayList.add(activityAssistInfo);
                    break;
                }
                i2++;
            }
            list = arrayList;
        } else {
            list = topVisibleActivities2;
        }
        return this.mActiveSession.showLocked(bundle2, i, str, this.mDisabledShowContext, iVoiceInteractionSessionShowCallback, list);
    }

    public void getActiveServiceSupportedActions(List<String> list, IVoiceActionCheckCallback iVoiceActionCheckCallback) {
        IVoiceInteractionService iVoiceInteractionService = this.mService;
        if (iVoiceInteractionService == null) {
            Slog.w(TAG, "Not bound to voice interaction service " + this.mComponent);
            try {
                iVoiceActionCheckCallback.onComplete((List) null);
                return;
            } catch (RemoteException unused) {
                return;
            }
        }
        try {
            iVoiceInteractionService.getActiveServiceSupportedActions(list, iVoiceActionCheckCallback);
        } catch (RemoteException e) {
            Slog.w(TAG, "RemoteException while calling getActiveServiceSupportedActions", e);
        }
    }

    public boolean hideSessionLocked() {
        VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
        if (voiceInteractionSessionConnection != null) {
            return voiceInteractionSessionConnection.hideLocked();
        }
        return false;
    }

    public boolean deliverNewSessionLocked(IBinder iBinder, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor) {
        VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
        if (voiceInteractionSessionConnection == null || iBinder != voiceInteractionSessionConnection.mToken) {
            Slog.w(TAG, "deliverNewSession does not match active session");
            return false;
        }
        voiceInteractionSessionConnection.deliverNewSessionLocked(iVoiceInteractionSession, iVoiceInteractor);
        return true;
    }

    public int startVoiceActivityLocked(String str, int i, int i2, IBinder iBinder, Intent intent, String str2) {
        try {
            VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
            if (voiceInteractionSessionConnection != null && iBinder == voiceInteractionSessionConnection.mToken) {
                if (!voiceInteractionSessionConnection.mShown) {
                    Slog.w(TAG, "startVoiceActivity not allowed on hidden session");
                    return -100;
                }
                Intent intent2 = new Intent(intent);
                intent2.addCategory("android.intent.category.VOICE");
                intent2.addFlags(402653184);
                IActivityTaskManager iActivityTaskManager = this.mAtm;
                String packageName = this.mComponent.getPackageName();
                VoiceInteractionSessionConnection voiceInteractionSessionConnection2 = this.mActiveSession;
                return iActivityTaskManager.startVoiceActivity(packageName, str, i, i2, intent2, str2, voiceInteractionSessionConnection2.mSession, voiceInteractionSessionConnection2.mInteractor, 0, (ProfilerInfo) null, (Bundle) null, this.mUser);
            }
            Slog.w(TAG, "startVoiceActivity does not match active session");
            return -99;
        } catch (RemoteException e) {
            throw new IllegalStateException("Unexpected remote error", e);
        }
    }

    public int startAssistantActivityLocked(String str, int i, int i2, IBinder iBinder, Intent intent, String str2, Bundle bundle) {
        try {
            VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
            if (voiceInteractionSessionConnection != null && iBinder == voiceInteractionSessionConnection.mToken) {
                if (!voiceInteractionSessionConnection.mShown) {
                    Slog.w(TAG, "startAssistantActivity not allowed on hidden session");
                    return -90;
                }
                Intent intent2 = new Intent(intent);
                intent2.addFlags(268435456);
                bundle.putInt("android.activity.activityType", 4);
                return this.mAtm.startAssistantActivity(this.mComponent.getPackageName(), str, i, i2, intent2, str2, bundle, this.mUser);
            }
            Slog.w(TAG, "startAssistantActivity does not match active session");
            return -89;
        } catch (RemoteException e) {
            throw new IllegalStateException("Unexpected remote error", e);
        }
    }

    public void requestDirectActionsLocked(IBinder iBinder, int i, IBinder iBinder2, RemoteCallback remoteCallback, RemoteCallback remoteCallback2) {
        VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
        if (voiceInteractionSessionConnection == null || iBinder != voiceInteractionSessionConnection.mToken) {
            Slog.w(TAG, "requestDirectActionsLocked does not match active session");
            remoteCallback2.sendResult((Bundle) null);
            return;
        }
        ActivityTaskManagerInternal.ActivityTokens attachedNonFinishingActivityForTask = ((ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class)).getAttachedNonFinishingActivityForTask(i, (IBinder) null);
        if (attachedNonFinishingActivityForTask == null || attachedNonFinishingActivityForTask.getAssistToken() != iBinder2) {
            Slog.w(TAG, "Unknown activity to query for direct actions");
            this.mDirectActionsHandler.sendMessageDelayed(PooledLambda.obtainMessage(new HexConsumer() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl$$ExternalSyntheticLambda0
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
                    ((VoiceInteractionManagerServiceImpl) obj).retryRequestDirectActions((IBinder) obj2, ((Integer) obj3).intValue(), (IBinder) obj4, (RemoteCallback) obj5, (RemoteCallback) obj6);
                }
            }, this, iBinder, Integer.valueOf(i), iBinder2, remoteCallback, remoteCallback2), REQUEST_DIRECT_ACTIONS_RETRY_TIME_MS);
            return;
        }
        grantImplicitAccessLocked(attachedNonFinishingActivityForTask.getUid(), null);
        try {
            attachedNonFinishingActivityForTask.getApplicationThread().requestDirectActions(attachedNonFinishingActivityForTask.getActivityToken(), this.mActiveSession.mInteractor, remoteCallback, remoteCallback2);
        } catch (RemoteException e) {
            Slog.w("Unexpected remote error", e);
            remoteCallback2.sendResult((Bundle) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retryRequestDirectActions(IBinder iBinder, int i, IBinder iBinder2, RemoteCallback remoteCallback, RemoteCallback remoteCallback2) {
        synchronized (this.mServiceStub) {
            VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
            if (voiceInteractionSessionConnection != null && iBinder == voiceInteractionSessionConnection.mToken) {
                ActivityTaskManagerInternal.ActivityTokens attachedNonFinishingActivityForTask = ((ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class)).getAttachedNonFinishingActivityForTask(i, (IBinder) null);
                if (attachedNonFinishingActivityForTask == null || attachedNonFinishingActivityForTask.getAssistToken() != iBinder2) {
                    Slog.w(TAG, "Unknown activity to query for direct actions during retrying");
                    remoteCallback2.sendResult((Bundle) null);
                } else {
                    try {
                        attachedNonFinishingActivityForTask.getApplicationThread().requestDirectActions(attachedNonFinishingActivityForTask.getActivityToken(), this.mActiveSession.mInteractor, remoteCallback, remoteCallback2);
                    } catch (RemoteException e) {
                        Slog.w("Unexpected remote error", e);
                        remoteCallback2.sendResult((Bundle) null);
                    }
                }
                return;
            }
            Slog.w(TAG, "retryRequestDirectActions does not match active session");
            remoteCallback2.sendResult((Bundle) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void performDirectActionLocked(IBinder iBinder, String str, Bundle bundle, int i, IBinder iBinder2, RemoteCallback remoteCallback, RemoteCallback remoteCallback2) {
        VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
        if (voiceInteractionSessionConnection == null || iBinder != voiceInteractionSessionConnection.mToken) {
            Slog.w(TAG, "performDirectActionLocked does not match active session");
            remoteCallback2.sendResult((Bundle) null);
            return;
        }
        ActivityTaskManagerInternal.ActivityTokens attachedNonFinishingActivityForTask = ((ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class)).getAttachedNonFinishingActivityForTask(i, (IBinder) null);
        if (attachedNonFinishingActivityForTask == null || attachedNonFinishingActivityForTask.getAssistToken() != iBinder2) {
            Slog.w(TAG, "Unknown activity to perform a direct action");
            remoteCallback2.sendResult((Bundle) null);
            return;
        }
        try {
            attachedNonFinishingActivityForTask.getApplicationThread().performDirectAction(attachedNonFinishingActivityForTask.getActivityToken(), str, bundle, remoteCallback, remoteCallback2);
        } catch (RemoteException e) {
            Slog.w("Unexpected remote error", e);
            remoteCallback2.sendResult((Bundle) null);
        }
    }

    public void setKeepAwakeLocked(IBinder iBinder, boolean z) {
        try {
            VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
            if (voiceInteractionSessionConnection != null && iBinder == voiceInteractionSessionConnection.mToken) {
                this.mAtm.setVoiceKeepAwake(voiceInteractionSessionConnection.mSession, z);
                return;
            }
            Slog.w(TAG, "setKeepAwake does not match active session");
        } catch (RemoteException e) {
            throw new IllegalStateException("Unexpected remote error", e);
        }
    }

    public void closeSystemDialogsLocked(IBinder iBinder) {
        try {
            VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
            if (voiceInteractionSessionConnection != null && iBinder == voiceInteractionSessionConnection.mToken) {
                this.mAm.closeSystemDialogs(CLOSE_REASON_VOICE_INTERACTION);
                return;
            }
            Slog.w(TAG, "closeSystemDialogs does not match active session");
        } catch (RemoteException e) {
            throw new IllegalStateException("Unexpected remote error", e);
        }
    }

    public void finishLocked(IBinder iBinder, boolean z) {
        VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
        if (voiceInteractionSessionConnection == null || (!z && iBinder != voiceInteractionSessionConnection.mToken)) {
            Slog.w(TAG, "finish does not match active session");
        } else {
            voiceInteractionSessionConnection.cancelLocked(z);
            this.mActiveSession = null;
        }
    }

    public void setDisabledShowContextLocked(int i, int i2) {
        int i3 = this.mInfo.getServiceInfo().applicationInfo.uid;
        if (i != i3) {
            throw new SecurityException("Calling uid " + i + " does not match active uid " + i3);
        }
        this.mDisabledShowContext = i2;
    }

    public int getDisabledShowContextLocked(int i) {
        int i2 = this.mInfo.getServiceInfo().applicationInfo.uid;
        if (i != i2) {
            throw new SecurityException("Calling uid " + i + " does not match active uid " + i2);
        }
        return this.mDisabledShowContext;
    }

    public int getUserDisabledShowContextLocked(int i) {
        int i2 = this.mInfo.getServiceInfo().applicationInfo.uid;
        if (i != i2) {
            throw new SecurityException("Calling uid " + i + " does not match active uid " + i2);
        }
        VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
        if (voiceInteractionSessionConnection != null) {
            return voiceInteractionSessionConnection.getUserDisabledShowContextLocked();
        }
        return 0;
    }

    public boolean supportsLocalVoiceInteraction() {
        return this.mInfo.getSupportsLocalInteraction();
    }

    public void startListeningVisibleActivityChangedLocked(IBinder iBinder) {
        VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
        if (voiceInteractionSessionConnection == null || iBinder != voiceInteractionSessionConnection.mToken) {
            Slog.w(TAG, "startListeningVisibleActivityChangedLocked does not match active session");
        } else {
            voiceInteractionSessionConnection.startListeningVisibleActivityChangedLocked();
        }
    }

    public void stopListeningVisibleActivityChangedLocked(IBinder iBinder) {
        VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
        if (voiceInteractionSessionConnection == null || iBinder != voiceInteractionSessionConnection.mToken) {
            Slog.w(TAG, "stopListeningVisibleActivityChangedLocked does not match active session");
        } else {
            voiceInteractionSessionConnection.stopListeningVisibleActivityChangedLocked();
        }
    }

    public void notifyActivityDestroyedLocked(IBinder iBinder) {
        VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
        if (voiceInteractionSessionConnection == null || !voiceInteractionSessionConnection.mShown) {
            return;
        }
        voiceInteractionSessionConnection.notifyActivityDestroyedLocked(iBinder);
    }

    public void notifyActivityEventChangedLocked(IBinder iBinder, int i) {
        VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
        if (voiceInteractionSessionConnection == null || !voiceInteractionSessionConnection.mShown) {
            return;
        }
        voiceInteractionSessionConnection.notifyActivityEventChangedLocked(iBinder, i);
    }

    public void updateStateLocked(PersistableBundle persistableBundle, SharedMemory sharedMemory, IBinder iBinder) {
        Slog.v(TAG, "updateStateLocked");
        if (sharedMemory != null && !sharedMemory.setProtect(OsConstants.PROT_READ)) {
            Slog.w(TAG, "Can't set sharedMemory to be read-only");
            throw new IllegalStateException("Can't set sharedMemory to be read-only");
        }
        if (this.mHotwordDetectionConnection == null) {
            Slog.w(TAG, "update State, but no hotword detection connection");
            throw new IllegalStateException("Hotword detection connection not found");
        }
        synchronized (this.mHotwordDetectionConnection.mLock) {
            this.mHotwordDetectionConnection.updateStateLocked(persistableBundle, sharedMemory, iBinder);
        }
    }

    private void verifyDetectorForHotwordDetectionLocked(SharedMemory sharedMemory, IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback, int i) {
        Slog.v(TAG, "verifyDetectorForHotwordDetectionLocked");
        int i2 = this.mInfo.getServiceInfo().applicationInfo.uid;
        ComponentName componentName = this.mHotwordDetectionComponentName;
        if (componentName == null) {
            Slog.w(TAG, "Hotword detection service name not found");
            logDetectorCreateEventIfNeeded(iHotwordRecognitionStatusCallback, i, false, i2);
            throw new IllegalStateException("Hotword detection service name not found");
        }
        ServiceInfo serviceInfoLocked = getServiceInfoLocked(componentName, this.mUser);
        if (serviceInfoLocked == null) {
            Slog.w(TAG, "Hotword detection service info not found");
            logDetectorCreateEventIfNeeded(iHotwordRecognitionStatusCallback, i, false, i2);
            throw new IllegalStateException("Hotword detection service info not found");
        }
        if (!isIsolatedProcessLocked(serviceInfoLocked)) {
            Slog.w(TAG, "Hotword detection service not in isolated process");
            logDetectorCreateEventIfNeeded(iHotwordRecognitionStatusCallback, i, false, i2);
            throw new IllegalStateException("Hotword detection service not in isolated process");
        }
        if (!"android.permission.BIND_HOTWORD_DETECTION_SERVICE".equals(serviceInfoLocked.permission)) {
            Slog.w(TAG, "Hotword detection service does not require permission android.permission.BIND_HOTWORD_DETECTION_SERVICE");
            logDetectorCreateEventIfNeeded(iHotwordRecognitionStatusCallback, i, false, i2);
            throw new SecurityException("Hotword detection service does not require permission android.permission.BIND_HOTWORD_DETECTION_SERVICE");
        }
        if (this.mContext.getPackageManager().checkPermission("android.permission.BIND_HOTWORD_DETECTION_SERVICE", this.mInfo.getServiceInfo().packageName) == 0) {
            Slog.w(TAG, "Voice interaction service should not hold permission android.permission.BIND_HOTWORD_DETECTION_SERVICE");
            logDetectorCreateEventIfNeeded(iHotwordRecognitionStatusCallback, i, false, i2);
            throw new SecurityException("Voice interaction service should not hold permission android.permission.BIND_HOTWORD_DETECTION_SERVICE");
        }
        if (sharedMemory != null && !sharedMemory.setProtect(OsConstants.PROT_READ)) {
            Slog.w(TAG, "Can't set sharedMemory to be read-only");
            logDetectorCreateEventIfNeeded(iHotwordRecognitionStatusCallback, i, false, i2);
            throw new IllegalStateException("Can't set sharedMemory to be read-only");
        }
        logDetectorCreateEventIfNeeded(iHotwordRecognitionStatusCallback, i, true, i2);
    }

    private void verifyDetectorForVisualQueryDetectionLocked(SharedMemory sharedMemory) {
        Slog.v(TAG, "verifyDetectorForVisualQueryDetectionLocked");
        ComponentName componentName = this.mVisualQueryDetectionComponentName;
        if (componentName == null) {
            Slog.w(TAG, "Visual query detection service name not found");
            throw new IllegalStateException("Visual query detection service name not found");
        }
        ServiceInfo serviceInfoLocked = getServiceInfoLocked(componentName, this.mUser);
        if (serviceInfoLocked == null) {
            Slog.w(TAG, "Visual query detection service info not found");
            throw new IllegalStateException("Visual query detection service name not found");
        }
        if (!isIsolatedProcessLocked(serviceInfoLocked)) {
            Slog.w(TAG, "Visual query detection service not in isolated process");
            throw new IllegalStateException("Visual query detection not in isolated process");
        }
        if (!"android.permission.BIND_VISUAL_QUERY_DETECTION_SERVICE".equals(serviceInfoLocked.permission)) {
            Slog.w(TAG, "Visual query detection does not require permission android.permission.BIND_VISUAL_QUERY_DETECTION_SERVICE");
            throw new SecurityException("Visual query detection does not require permission android.permission.BIND_VISUAL_QUERY_DETECTION_SERVICE");
        }
        if (this.mContext.getPackageManager().checkPermission("android.permission.BIND_VISUAL_QUERY_DETECTION_SERVICE", this.mInfo.getServiceInfo().packageName) == 0) {
            Slog.w(TAG, "Voice interaction service should not hold permission android.permission.BIND_VISUAL_QUERY_DETECTION_SERVICE");
            throw new SecurityException("Voice interaction service should not hold permission android.permission.BIND_VISUAL_QUERY_DETECTION_SERVICE");
        }
        if (sharedMemory == null || sharedMemory.setProtect(OsConstants.PROT_READ)) {
            return;
        }
        Slog.w(TAG, "Can't set sharedMemory to be read-only");
        throw new IllegalStateException("Can't set sharedMemory to be read-only");
    }

    public void initAndVerifyDetectorLocked(Identity identity, PersistableBundle persistableBundle, SharedMemory sharedMemory, IBinder iBinder, IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback, int i) {
        if (i != 3) {
            verifyDetectorForHotwordDetectionLocked(sharedMemory, iHotwordRecognitionStatusCallback, i);
        } else {
            verifyDetectorForVisualQueryDetectionLocked(sharedMemory);
        }
        if (!verifyProcessSharingLocked()) {
            Slog.w(TAG, "Sandboxed detection service not in shared isolated process");
            throw new IllegalStateException("VisualQueryDetectionService or HotworDetectionService not in a shared isolated process. Please make sure to set android:allowSharedIsolatedProcess and android:isolatedProcess to be true and android:externalService to be false in the manifest file");
        }
        if (this.mHotwordDetectionConnection == null) {
            this.mHotwordDetectionConnection = new HotwordDetectionConnection(this.mServiceStub, this.mContext, this.mInfo.getServiceInfo().applicationInfo.uid, identity, this.mHotwordDetectionComponentName, this.mVisualQueryDetectionComponentName, this.mUser, false, i, new DetectorRemoteExceptionListener() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl$$ExternalSyntheticLambda1
                @Override // com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.DetectorRemoteExceptionListener
                public final void onDetectorRemoteException(IBinder iBinder2, int i2) {
                    VoiceInteractionManagerServiceImpl.this.lambda$initAndVerifyDetectorLocked$0(iBinder2, i2);
                }
            });
        } else if (i != 3) {
            this.mHotwordDetectionConnection.setDetectorType(i);
        }
        this.mHotwordDetectionConnection.createDetectorLocked(persistableBundle, sharedMemory, iBinder, iHotwordRecognitionStatusCallback, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initAndVerifyDetectorLocked$0(IBinder iBinder, int i) {
        try {
            this.mService.detectorRemoteExceptionOccurred(iBinder, i);
        } catch (RemoteException unused) {
            Slog.w(TAG, "Fail to notify client detector remote exception occurred.");
        }
    }

    public void destroyDetectorLocked(IBinder iBinder) {
        Slog.v(TAG, "destroyDetectorLocked");
        if (this.mHotwordDetectionConnection == null) {
            Slog.w(TAG, "destroy detector callback, but no hotword detection connection");
        } else {
            this.mHotwordDetectionConnection.destroyDetectorLocked(iBinder);
        }
    }

    private void logDetectorCreateEventIfNeeded(IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback, int i, boolean z, int i2) {
        if (iHotwordRecognitionStatusCallback != null) {
            HotwordMetricsLogger.writeDetectorCreateEvent(i, z, i2);
        }
    }

    public void shutdownHotwordDetectionServiceLocked() {
        if (this.mHotwordDetectionConnection == null) {
            Slog.w(TAG, "shutdown, but no hotword detection connection");
        } else {
            this.mHotwordDetectionConnection.cancelLocked();
            this.mHotwordDetectionConnection = null;
        }
    }

    public void setVisualQueryDetectionAttentionListenerLocked(IVisualQueryDetectionAttentionListener iVisualQueryDetectionAttentionListener) {
        if (this.mHotwordDetectionConnection == null) {
            return;
        }
        this.mHotwordDetectionConnection.setVisualQueryDetectionAttentionListenerLocked(iVisualQueryDetectionAttentionListener);
    }

    public void startPerceivingLocked(IVisualQueryDetectionVoiceInteractionCallback iVisualQueryDetectionVoiceInteractionCallback) {
        if (this.mHotwordDetectionConnection == null) {
            return;
        }
        this.mHotwordDetectionConnection.startPerceivingLocked(iVisualQueryDetectionVoiceInteractionCallback);
    }

    public void stopPerceivingLocked() {
        if (this.mHotwordDetectionConnection == null) {
            Slog.w(TAG, "stopPerceivingLocked() called but connection isn't established");
        } else {
            this.mHotwordDetectionConnection.stopPerceivingLocked();
        }
    }

    public void startListeningFromMicLocked(AudioFormat audioFormat, IMicrophoneHotwordDetectionVoiceInteractionCallback iMicrophoneHotwordDetectionVoiceInteractionCallback) {
        if (this.mHotwordDetectionConnection == null) {
            return;
        }
        this.mHotwordDetectionConnection.startListeningFromMicLocked(audioFormat, iMicrophoneHotwordDetectionVoiceInteractionCallback);
    }

    public void startListeningFromExternalSourceLocked(ParcelFileDescriptor parcelFileDescriptor, AudioFormat audioFormat, PersistableBundle persistableBundle, IBinder iBinder, IMicrophoneHotwordDetectionVoiceInteractionCallback iMicrophoneHotwordDetectionVoiceInteractionCallback) {
        if (this.mHotwordDetectionConnection == null) {
            return;
        }
        if (parcelFileDescriptor == null) {
            Slog.w(TAG, "External source is null for hotword detector");
            throw new IllegalStateException("External source is null for hotword detector");
        }
        this.mHotwordDetectionConnection.startListeningFromExternalSourceLocked(parcelFileDescriptor, audioFormat, persistableBundle, iBinder, iMicrophoneHotwordDetectionVoiceInteractionCallback);
    }

    public void stopListeningFromMicLocked() {
        if (this.mHotwordDetectionConnection == null) {
            Slog.w(TAG, "stopListeningFromMicLocked() called but connection isn't established");
        } else {
            this.mHotwordDetectionConnection.stopListeningFromMicLocked();
        }
    }

    public void triggerHardwareRecognitionEventForTestLocked(SoundTrigger.KeyphraseRecognitionEvent keyphraseRecognitionEvent, IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback) {
        if (this.mHotwordDetectionConnection == null) {
            Slog.w(TAG, "triggerHardwareRecognitionEventForTestLocked() called but connection isn't established");
        } else {
            this.mHotwordDetectionConnection.triggerHardwareRecognitionEventForTestLocked(keyphraseRecognitionEvent, iHotwordRecognitionStatusCallback);
        }
    }

    public IRecognitionStatusCallback createSoundTriggerCallbackLocked(Context context, IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback, Identity identity) {
        return new HotwordDetectionConnection.SoundTriggerCallback(context, iHotwordRecognitionStatusCallback, this.mHotwordDetectionConnection, identity);
    }

    private static ServiceInfo getServiceInfoLocked(ComponentName componentName, int i) {
        try {
            return AppGlobals.getPackageManager().getServiceInfo(componentName, 786560L, i);
        } catch (RemoteException unused) {
            return null;
        }
    }

    boolean isIsolatedProcessLocked(ServiceInfo serviceInfo) {
        int i = serviceInfo.flags;
        return (i & 2) != 0 && (i & 4) == 0;
    }

    boolean verifyProcessSharingLocked() {
        ServiceInfo serviceInfoLocked = getServiceInfoLocked(this.mHotwordDetectionComponentName, this.mUser);
        ServiceInfo serviceInfoLocked2 = getServiceInfoLocked(this.mVisualQueryDetectionComponentName, this.mUser);
        if (serviceInfoLocked == null || serviceInfoLocked2 == null) {
            return true;
        }
        return ((serviceInfoLocked.flags & 16) == 0 || (serviceInfoLocked2.flags & 16) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forceRestartHotwordDetector() {
        if (this.mHotwordDetectionConnection == null) {
            Slog.w(TAG, "Failed to force-restart hotword detection: no hotword detection active");
        } else {
            this.mHotwordDetectionConnection.forceRestart();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDebugHotwordLoggingLocked(boolean z) {
        if (this.mHotwordDetectionConnection == null) {
            Slog.w(TAG, "Failed to set temporary debug logging: no hotword detection active");
        } else {
            this.mHotwordDetectionConnection.setDebugHotwordLoggingLocked(z);
        }
    }

    void resetHotwordDetectionConnectionLocked() {
        if (this.mHotwordDetectionConnection == null) {
            return;
        }
        this.mHotwordDetectionConnection.cancelLocked();
        this.mHotwordDetectionConnection = null;
    }

    public void dumpLocked(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (!this.mValid) {
            printWriter.print("  NOT VALID: ");
            VoiceInteractionServiceInfo voiceInteractionServiceInfo = this.mInfo;
            if (voiceInteractionServiceInfo == null) {
                printWriter.println("no info");
                return;
            } else {
                printWriter.println(voiceInteractionServiceInfo.getParseError());
                return;
            }
        }
        printWriter.print("  mUser=");
        printWriter.println(this.mUser);
        printWriter.print("  mComponent=");
        printWriter.println(this.mComponent.flattenToShortString());
        printWriter.print("  Session service=");
        printWriter.println(this.mInfo.getSessionService());
        printWriter.println("  Service info:");
        this.mInfo.getServiceInfo().dump(new PrintWriterPrinter(printWriter), "    ");
        printWriter.print("  Recognition service=");
        printWriter.println(this.mInfo.getRecognitionService());
        printWriter.print("  Hotword detection service=");
        printWriter.println(this.mInfo.getHotwordDetectionService());
        printWriter.print("  Settings activity=");
        printWriter.println(this.mInfo.getSettingsActivity());
        printWriter.print("  Supports assist=");
        printWriter.println(this.mInfo.getSupportsAssist());
        printWriter.print("  Supports launch from keyguard=");
        printWriter.println(this.mInfo.getSupportsLaunchFromKeyguard());
        if (this.mDisabledShowContext != 0) {
            printWriter.print("  mDisabledShowContext=");
            printWriter.println(Integer.toHexString(this.mDisabledShowContext));
        }
        printWriter.print("  mBound=");
        printWriter.print(this.mBound);
        printWriter.print(" mService=");
        printWriter.println(this.mService);
        if (this.mHotwordDetectionConnection != null) {
            printWriter.println("  Hotword detection connection:");
            this.mHotwordDetectionConnection.dump("    ", printWriter);
        } else {
            printWriter.println("  No Hotword detection connection");
        }
        if (this.mActiveSession != null) {
            printWriter.println("  Active session:");
            this.mActiveSession.dump("    ", printWriter);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startLocked() {
        Intent intent = new Intent("android.service.voice.VoiceInteractionService");
        intent.setComponent(this.mComponent);
        boolean bindServiceAsUser = this.mContext.bindServiceAsUser(intent, this.mConnection, 68161537, new UserHandle(this.mUser));
        this.mBound = bindServiceAsUser;
        if (bindServiceAsUser) {
            return;
        }
        Slog.w(TAG, "Failed binding to voice interaction service " + this.mComponent);
    }

    public void launchVoiceAssistFromKeyguard() {
        IVoiceInteractionService iVoiceInteractionService = this.mService;
        if (iVoiceInteractionService == null) {
            Slog.w(TAG, "Not bound to voice interaction service " + this.mComponent);
            return;
        }
        try {
            iVoiceInteractionService.launchVoiceAssistFromKeyguard();
        } catch (RemoteException e) {
            Slog.w(TAG, "RemoteException while calling launchVoiceAssistFromKeyguard", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void shutdownLocked() {
        VoiceInteractionSessionConnection voiceInteractionSessionConnection = this.mActiveSession;
        if (voiceInteractionSessionConnection != null) {
            voiceInteractionSessionConnection.cancelLocked(false);
            this.mActiveSession = null;
        }
        try {
            IVoiceInteractionService iVoiceInteractionService = this.mService;
            if (iVoiceInteractionService != null) {
                iVoiceInteractionService.shutdown();
            }
        } catch (RemoteException e) {
            Slog.w(TAG, "RemoteException in shutdown", e);
        }
        if (this.mHotwordDetectionConnection != null) {
            this.mHotwordDetectionConnection.cancelLocked();
            this.mHotwordDetectionConnection = null;
        }
        if (this.mBound) {
            this.mContext.unbindService(this.mConnection);
            this.mBound = false;
        }
        if (this.mValid) {
            this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifySoundModelsChangedLocked() {
        IVoiceInteractionService iVoiceInteractionService = this.mService;
        if (iVoiceInteractionService == null) {
            Slog.w(TAG, "Not bound to voice interaction service " + this.mComponent);
            return;
        }
        try {
            iVoiceInteractionService.soundModelsChanged();
        } catch (RemoteException e) {
            Slog.w(TAG, "RemoteException while calling soundModelsChanged", e);
        }
    }

    @Override // com.android.server.voiceinteraction.VoiceInteractionSessionConnection.Callback
    public void sessionConnectionGone(VoiceInteractionSessionConnection voiceInteractionSessionConnection) {
        synchronized (this.mServiceStub) {
            finishLocked(voiceInteractionSessionConnection.mToken, false);
        }
    }

    @Override // com.android.server.voiceinteraction.VoiceInteractionSessionConnection.Callback
    public void onSessionShown(VoiceInteractionSessionConnection voiceInteractionSessionConnection) {
        this.mServiceStub.onSessionShown();
    }

    @Override // com.android.server.voiceinteraction.VoiceInteractionSessionConnection.Callback
    public void onSessionHidden(VoiceInteractionSessionConnection voiceInteractionSessionConnection) {
        this.mServiceStub.onSessionHidden();
        this.mServiceStub.setSessionWindowVisible(voiceInteractionSessionConnection.mToken, false);
    }
}
