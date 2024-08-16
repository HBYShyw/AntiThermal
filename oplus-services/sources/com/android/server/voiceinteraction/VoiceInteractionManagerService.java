package com.android.server.voiceinteraction;

import android.R;
import android.annotation.EnforcePermission;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.AppGlobals;
import android.app.role.OnRoleHoldersChangedListener;
import android.app.role.RoleManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.ShortcutServiceInternal;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.hardware.soundtrigger.IRecognitionStatusCallback;
import android.hardware.soundtrigger.KeyphraseMetadata;
import android.hardware.soundtrigger.ModelParams;
import android.hardware.soundtrigger.SoundTrigger;
import android.media.AudioFormat;
import android.media.permission.Identity;
import android.media.permission.PermissionUtil;
import android.media.permission.SafeCloseable;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.RemoteCallback;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SharedMemory;
import android.os.ShellCallback;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback;
import android.service.voice.IVisualQueryDetectionVoiceInteractionCallback;
import android.service.voice.IVoiceInteractionSession;
import android.service.voice.VoiceInteractionManagerInternal;
import android.service.voice.VoiceInteractionServiceInfo;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.IHotwordRecognitionStatusCallback;
import com.android.internal.app.IVisualQueryDetectionAttentionListener;
import com.android.internal.app.IVoiceActionCheckCallback;
import com.android.internal.app.IVoiceInteractionManagerService;
import com.android.internal.app.IVoiceInteractionSessionListener;
import com.android.internal.app.IVoiceInteractionSessionShowCallback;
import com.android.internal.app.IVoiceInteractionSoundTriggerSession;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.content.PackageMonitor;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.server.FgThread;
import com.android.server.LocalServices;
import com.android.server.SoundTriggerInternal;
import com.android.server.SystemService;
import com.android.server.UiThread;
import com.android.server.pm.UserManagerInternal;
import com.android.server.pm.permission.LegacyPermissionManagerInternal;
import com.android.server.policy.AppOpsPolicy;
import com.android.server.slice.SliceClientPermissions;
import com.android.server.utils.Slogf;
import com.android.server.voiceinteraction.VoiceInteractionManagerService;
import com.android.server.wm.ActivityTaskManagerInternal;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class VoiceInteractionManagerService extends SystemService {
    static final boolean DEBUG = false;
    static final String TAG = "VoiceInteractionManager";
    final ActivityManagerInternal mAmInternal;
    final ActivityTaskManagerInternal mAtmInternal;
    final Context mContext;
    private IEnrolledModelDb mDbHelper;
    private final IVoiceInteractionSessionListener mLatencyLoggingListener;
    final ArrayMap<Integer, VoiceInteractionManagerServiceStub.SoundTriggerSession> mLoadedKeyphraseIds;
    private final IEnrolledModelDb mRealDbHelper;
    final ContentResolver mResolver;
    private final VoiceInteractionManagerServiceStub mServiceStub;
    ShortcutServiceInternal mShortcutServiceInternal;
    SoundTriggerInternal mSoundTriggerInternal;
    final UserManagerInternal mUserManagerInternal;
    private final RemoteCallbackList<IVoiceInteractionSessionListener> mVoiceInteractionSessionListeners;

    public VoiceInteractionManagerService(Context context) {
        super(context);
        this.mLoadedKeyphraseIds = new ArrayMap<>();
        this.mVoiceInteractionSessionListeners = new RemoteCallbackList<>();
        this.mLatencyLoggingListener = new IVoiceInteractionSessionListener.Stub() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService.3
            public void onSetUiHints(Bundle bundle) throws RemoteException {
            }

            public void onVoiceSessionHidden() throws RemoteException {
            }

            public void onVoiceSessionShown() throws RemoteException {
            }

            public void onVoiceSessionWindowVisibilityChanged(boolean z) throws RemoteException {
                if (z) {
                    HotwordMetricsLogger.stopHotwordTriggerToUiLatencySession(VoiceInteractionManagerService.this.mContext);
                }
            }

            public IBinder asBinder() {
                return VoiceInteractionManagerService.this.mServiceStub;
            }
        };
        this.mContext = context;
        this.mResolver = context.getContentResolver();
        UserManagerInternal userManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        Objects.requireNonNull(userManagerInternal);
        this.mUserManagerInternal = userManagerInternal;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        this.mRealDbHelper = databaseHelper;
        this.mDbHelper = databaseHelper;
        this.mServiceStub = new VoiceInteractionManagerServiceStub();
        ActivityManagerInternal activityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        Objects.requireNonNull(activityManagerInternal);
        this.mAmInternal = activityManagerInternal;
        ActivityTaskManagerInternal activityTaskManagerInternal = (ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class);
        Objects.requireNonNull(activityTaskManagerInternal);
        this.mAtmInternal = activityTaskManagerInternal;
        ((LegacyPermissionManagerInternal) LocalServices.getService(LegacyPermissionManagerInternal.class)).setVoiceInteractionPackagesProvider(new LegacyPermissionManagerInternal.PackagesProvider() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService.1
            @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider
            public String[] getPackages(int i) {
                VoiceInteractionManagerService.this.mServiceStub.initForUser(i);
                ComponentName curInteractor = VoiceInteractionManagerService.this.mServiceStub.getCurInteractor(i);
                if (curInteractor != null) {
                    return new String[]{curInteractor.getPackageName()};
                }
                return null;
            }
        });
    }

    public void onStart() {
        publishBinderService("voiceinteraction", this.mServiceStub);
        publishLocalService(VoiceInteractionManagerInternal.class, new LocalService());
        this.mAmInternal.setVoiceInteractionManagerProvider(new ActivityManagerInternal.VoiceInteractionManagerProvider() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService.2
            public void notifyActivityDestroyed(IBinder iBinder) {
                VoiceInteractionManagerService.this.mServiceStub.notifyActivityDestroyed(iBinder);
            }
        });
    }

    public void onBootPhase(int i) {
        if (500 == i) {
            ShortcutServiceInternal shortcutServiceInternal = (ShortcutServiceInternal) LocalServices.getService(ShortcutServiceInternal.class);
            Objects.requireNonNull(shortcutServiceInternal);
            this.mShortcutServiceInternal = shortcutServiceInternal;
            this.mSoundTriggerInternal = (SoundTriggerInternal) LocalServices.getService(SoundTriggerInternal.class);
            return;
        }
        if (i == 600) {
            this.mServiceStub.systemRunning(isSafeMode());
        } else if (i == 1000) {
            this.mServiceStub.registerVoiceInteractionSessionListener(this.mLatencyLoggingListener);
        }
    }

    public boolean isUserSupported(SystemService.TargetUser targetUser) {
        return targetUser.isFull();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUserSupported(UserInfo userInfo) {
        return userInfo.isFull();
    }

    public void onUserStarting(SystemService.TargetUser targetUser) {
        this.mServiceStub.initForUser(targetUser.getUserIdentifier());
    }

    public void onUserUnlocking(SystemService.TargetUser targetUser) {
        this.mServiceStub.initForUser(targetUser.getUserIdentifier());
        this.mServiceStub.switchImplementationIfNeeded(false);
    }

    public void onUserSwitching(SystemService.TargetUser targetUser, SystemService.TargetUser targetUser2) {
        this.mServiceStub.switchUser(targetUser2.getUserIdentifier());
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class LocalService extends VoiceInteractionManagerInternal {
        LocalService() {
        }

        public void startLocalVoiceInteraction(IBinder iBinder, String str, Bundle bundle) {
            VoiceInteractionManagerService.this.mServiceStub.startLocalVoiceInteraction(iBinder, str, bundle);
        }

        public boolean supportsLocalVoiceInteraction() {
            return VoiceInteractionManagerService.this.mServiceStub.supportsLocalVoiceInteraction();
        }

        public void stopLocalVoiceInteraction(IBinder iBinder) {
            VoiceInteractionManagerService.this.mServiceStub.stopLocalVoiceInteraction(iBinder);
        }

        public boolean hasActiveSession(String str) {
            VoiceInteractionSessionConnection voiceInteractionSessionConnection;
            VoiceInteractionManagerServiceImpl voiceInteractionManagerServiceImpl = VoiceInteractionManagerService.this.mServiceStub.mImpl;
            if (voiceInteractionManagerServiceImpl == null || (voiceInteractionSessionConnection = voiceInteractionManagerServiceImpl.mActiveSession) == null) {
                return false;
            }
            return TextUtils.equals(str, voiceInteractionSessionConnection.mSessionComponentName.getPackageName());
        }

        public String getVoiceInteractorPackageName(IBinder iBinder) {
            VoiceInteractionSessionConnection voiceInteractionSessionConnection;
            IVoiceInteractor iVoiceInteractor;
            VoiceInteractionManagerServiceImpl voiceInteractionManagerServiceImpl = VoiceInteractionManagerService.this.mServiceStub.mImpl;
            if (voiceInteractionManagerServiceImpl == null || (voiceInteractionSessionConnection = voiceInteractionManagerServiceImpl.mActiveSession) == null || (iVoiceInteractor = voiceInteractionSessionConnection.mInteractor) == null || iVoiceInteractor.asBinder() != iBinder) {
                return null;
            }
            return voiceInteractionSessionConnection.mSessionComponentName.getPackageName();
        }

        public VoiceInteractionManagerInternal.HotwordDetectionServiceIdentity getHotwordDetectionServiceIdentity() {
            HotwordDetectionConnection hotwordDetectionConnection;
            VoiceInteractionManagerServiceImpl voiceInteractionManagerServiceImpl = VoiceInteractionManagerService.this.mServiceStub.mImpl;
            if (voiceInteractionManagerServiceImpl == null || (hotwordDetectionConnection = voiceInteractionManagerServiceImpl.mHotwordDetectionConnection) == null) {
                return null;
            }
            return hotwordDetectionConnection.mIdentity;
        }

        public void onPreCreatedUserConversion(int i) {
            Slogf.d(VoiceInteractionManagerService.TAG, "onPreCreatedUserConversion(%d): calling onRoleHoldersChanged() again", Integer.valueOf(i));
            VoiceInteractionManagerService.this.mServiceStub.mRoleObserver.onRoleHoldersChanged("android.app.role.ASSISTANT", UserHandle.of(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class VoiceInteractionManagerServiceStub extends IVoiceInteractionManagerService.Stub {
        private static final int SHOW_SESSION_START_ID = 0;
        private final boolean IS_HDS_REQUIRED;
        private int mCurUser;
        private boolean mCurUserSupported;
        private final boolean mEnableService;
        volatile VoiceInteractionManagerServiceImpl mImpl;
        private final RoleObserver mRoleObserver;
        private boolean mSafeMode;

        @GuardedBy({"this"})
        private boolean mTemporarilyDisabled;

        @GuardedBy({"this"})
        private int mShowSessionId = 0;
        PackageMonitor mPackageMonitor = new AnonymousClass2();

        VoiceInteractionManagerServiceStub() {
            this.IS_HDS_REQUIRED = AppOpsPolicy.isHotwordDetectionServiceRequired(VoiceInteractionManagerService.this.mContext.getPackageManager());
            this.mEnableService = shouldEnableService(VoiceInteractionManagerService.this.mContext);
            this.mRoleObserver = new RoleObserver(VoiceInteractionManagerService.this.mContext.getMainExecutor());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void handleUserStop(String str, int i) {
            synchronized (this) {
                ComponentName curInteractor = getCurInteractor(i);
                if (curInteractor != null && str.equals(curInteractor.getPackageName())) {
                    Slog.d(VoiceInteractionManagerService.TAG, "switchImplementation for user stop.");
                    switchImplementationIfNeededLocked(true);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getNextShowSessionId() {
            int i;
            synchronized (this) {
                if (this.mShowSessionId == 2147483646) {
                    this.mShowSessionId = 0;
                }
                i = this.mShowSessionId + 1;
                this.mShowSessionId = i;
            }
            return i;
        }

        int getShowSessionId() {
            int i;
            synchronized (this) {
                i = this.mShowSessionId;
            }
            return i;
        }

        public IVoiceInteractionSoundTriggerSession createSoundTriggerSessionAsOriginator(Identity identity, IBinder iBinder, SoundTrigger.ModuleProperties moduleProperties) {
            boolean z;
            boolean z2;
            Objects.requireNonNull(identity);
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                z = true;
                z2 = (this.mImpl == null || this.mImpl.mHotwordDetectionConnection == null) ? false : true;
            }
            SafeCloseable establishIdentityDirect = PermissionUtil.establishIdentityDirect(identity);
            try {
                if (this.IS_HDS_REQUIRED) {
                    z = z2;
                }
                SoundTriggerSession soundTriggerSession = new SoundTriggerSession(VoiceInteractionManagerService.this.mSoundTriggerInternal.attach(iBinder, moduleProperties, z), identity);
                if (establishIdentityDirect != null) {
                    establishIdentityDirect.close();
                }
                return soundTriggerSession;
            } catch (Throwable th) {
                if (establishIdentityDirect != null) {
                    try {
                        establishIdentityDirect.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public List<SoundTrigger.ModuleProperties> listModuleProperties(Identity identity) {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
            }
            return VoiceInteractionManagerService.this.mSoundTriggerInternal.listModuleProperties(identity);
        }

        void startLocalVoiceInteraction(final IBinder iBinder, String str, Bundle bundle) {
            if (this.mImpl == null) {
                return;
            }
            final int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                HotwordMetricsLogger.cancelHotwordTriggerToUiLatencySession(VoiceInteractionManagerService.this.mContext);
                this.mImpl.showSessionLocked(bundle, 16, str, new IVoiceInteractionSessionShowCallback.Stub() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.1
                    public void onFailed() {
                    }

                    public void onShown() {
                        synchronized (VoiceInteractionManagerServiceStub.this) {
                            if (VoiceInteractionManagerServiceStub.this.mImpl != null) {
                                VoiceInteractionManagerServiceStub.this.mImpl.grantImplicitAccessLocked(callingUid, null);
                            }
                        }
                        VoiceInteractionManagerServiceStub voiceInteractionManagerServiceStub = VoiceInteractionManagerServiceStub.this;
                        VoiceInteractionManagerService.this.mAtmInternal.onLocalVoiceInteractionStarted(iBinder, voiceInteractionManagerServiceStub.mImpl.mActiveSession.mSession, VoiceInteractionManagerServiceStub.this.mImpl.mActiveSession.mInteractor);
                    }
                }, iBinder);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void stopLocalVoiceInteraction(IBinder iBinder) {
            if (this.mImpl == null) {
                return;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mImpl.finishLocked(iBinder, true);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public boolean supportsLocalVoiceInteraction() {
            if (this.mImpl == null) {
                return false;
            }
            return this.mImpl.supportsLocalVoiceInteraction();
        }

        void notifyActivityDestroyed(final IBinder iBinder) {
            synchronized (this) {
                if (this.mImpl != null && iBinder != null) {
                    Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda7
                        public final void runOrThrow() {
                            VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.lambda$notifyActivityDestroyed$0(iBinder);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyActivityDestroyed$0(IBinder iBinder) throws Exception {
            this.mImpl.notifyActivityDestroyedLocked(iBinder);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            try {
                return super.onTransact(i, parcel, parcel2, i2);
            } catch (RuntimeException e) {
                if (!(e instanceof SecurityException)) {
                    Slog.wtf(VoiceInteractionManagerService.TAG, "VoiceInteractionManagerService Crash", e);
                }
                throw e;
            }
        }

        public void initForUser(int i) {
            initForUserNoTracing(i);
        }

        private void initForUserNoTracing(int i) {
            VoiceInteractionServiceInfo voiceInteractionServiceInfo;
            ServiceInfo serviceInfo;
            ServiceInfo serviceInfo2;
            String stringForUser = Settings.Secure.getStringForUser(VoiceInteractionManagerService.this.mContext.getContentResolver(), "voice_interaction_service", i);
            ComponentName curRecognizer = getCurRecognizer(i);
            if (stringForUser == null && curRecognizer != null && this.mEnableService) {
                voiceInteractionServiceInfo = findAvailInteractor(i, curRecognizer.getPackageName());
                if (voiceInteractionServiceInfo != null) {
                    curRecognizer = null;
                }
            } else {
                voiceInteractionServiceInfo = null;
            }
            String forceVoiceInteractionServicePackage = getForceVoiceInteractionServicePackage(VoiceInteractionManagerService.this.mContext.getResources());
            if (forceVoiceInteractionServicePackage != null && (voiceInteractionServiceInfo = findAvailInteractor(i, forceVoiceInteractionServicePackage)) != null) {
                curRecognizer = null;
            }
            if (!this.mEnableService && stringForUser != null && !TextUtils.isEmpty(stringForUser)) {
                setCurInteractor(null, i);
                stringForUser = "";
            }
            if (curRecognizer != null) {
                IPackageManager packageManager = AppGlobals.getPackageManager();
                ComponentName unflattenFromString = !TextUtils.isEmpty(stringForUser) ? ComponentName.unflattenFromString(stringForUser) : null;
                try {
                    serviceInfo = packageManager.getServiceInfo(curRecognizer, 786560L, i);
                    if (serviceInfo != null) {
                        try {
                            RecognitionServiceInfo parseInfo = RecognitionServiceInfo.parseInfo(VoiceInteractionManagerService.this.mContext.getPackageManager(), serviceInfo);
                            if (!TextUtils.isEmpty(parseInfo.getParseError())) {
                                Log.w(VoiceInteractionManagerService.TAG, "Parse error in getAvailableServices: " + parseInfo.getParseError());
                            }
                            if (!parseInfo.isSelectableAsDefault()) {
                                serviceInfo = null;
                            }
                        } catch (RemoteException unused) {
                        }
                    }
                } catch (RemoteException unused2) {
                    serviceInfo = null;
                }
                if (unflattenFromString != null) {
                    serviceInfo2 = packageManager.getServiceInfo(unflattenFromString, 786432L, i);
                    if (serviceInfo != null && (unflattenFromString == null || serviceInfo2 != null)) {
                        return;
                    }
                }
                serviceInfo2 = null;
                if (serviceInfo != null) {
                    return;
                }
            }
            if (voiceInteractionServiceInfo == null && this.mEnableService && !"".equals(stringForUser)) {
                voiceInteractionServiceInfo = findAvailInteractor(i, null);
            }
            if (voiceInteractionServiceInfo != null) {
                setCurInteractor(new ComponentName(voiceInteractionServiceInfo.getServiceInfo().packageName, voiceInteractionServiceInfo.getServiceInfo().name), i);
            } else {
                setCurInteractor(null, i);
            }
            initRecognizer(i);
        }

        public void initRecognizer(int i) {
            ComponentName findAvailRecognizer = findAvailRecognizer(null, i);
            if (findAvailRecognizer != null) {
                setCurRecognizer(findAvailRecognizer, i);
            }
        }

        private boolean shouldEnableService(Context context) {
            if (getForceVoiceInteractionServicePackage(context.getResources()) != null) {
                return true;
            }
            return context.getPackageManager().hasSystemFeature("android.software.voice_recognizers");
        }

        private String getForceVoiceInteractionServicePackage(Resources resources) {
            String string = resources.getString(R.string.config_wwan_network_service_package);
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            return string;
        }

        public void systemRunning(boolean z) {
            this.mSafeMode = z;
            this.mPackageMonitor.getWrapper().getExtImpl().register(VoiceInteractionManagerService.this.mContext, BackgroundThread.getHandler().getLooper(), UserHandle.ALL, true, new int[]{12, 14, 5, 19});
            new SettingsObserver(UiThread.getHandler());
            synchronized (this) {
                setCurrentUserLocked(ActivityManager.getCurrentUser());
                switchImplementationIfNeededLocked(false);
            }
        }

        private void setCurrentUserLocked(int i) {
            this.mCurUser = i;
            this.mCurUserSupported = VoiceInteractionManagerService.this.isUserSupported(VoiceInteractionManagerService.this.mUserManagerInternal.getUserInfo(i));
        }

        public void switchUser(final int i) {
            FgThread.getHandler().post(new Runnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.lambda$switchUser$1(i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$switchUser$1(int i) {
            synchronized (this) {
                setCurrentUserLocked(i);
                switchImplementationIfNeededLocked(false);
            }
        }

        void switchImplementationIfNeeded(boolean z) {
            synchronized (this) {
                switchImplementationIfNeededLocked(z);
            }
        }

        void switchImplementationIfNeededLocked(boolean z) {
            if (!this.mCurUserSupported) {
                if (this.mImpl != null) {
                    this.mImpl.shutdownLocked();
                    setImplLocked(null);
                    return;
                }
                return;
            }
            switchImplementationIfNeededNoTracingLocked(z);
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x0058  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x00a7  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x00ae  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x00c8  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        void switchImplementationIfNeededNoTracingLocked(boolean z) {
            ServiceInfo serviceInfo;
            ComponentName componentName;
            boolean z2;
            if (this.mSafeMode) {
                return;
            }
            String stringForUser = Settings.Secure.getStringForUser(VoiceInteractionManagerService.this.mResolver, "voice_interaction_service", this.mCurUser);
            if (stringForUser != null && !stringForUser.isEmpty()) {
                try {
                    ComponentName unflattenFromString = ComponentName.unflattenFromString(stringForUser);
                    serviceInfo = AppGlobals.getPackageManager().getServiceInfo(unflattenFromString, 0L, this.mCurUser);
                    componentName = unflattenFromString;
                } catch (RemoteException | RuntimeException e) {
                    Slog.wtf(VoiceInteractionManagerService.TAG, "Bad voice interaction service name " + stringForUser, e);
                }
                z2 = componentName == null && serviceInfo != null;
                if (VoiceInteractionManagerService.this.mUserManagerInternal.isUserUnlockingOrUnlocked(this.mCurUser)) {
                    if (z2) {
                        VoiceInteractionManagerService.this.mShortcutServiceInternal.setShortcutHostPackage(VoiceInteractionManagerService.TAG, componentName.getPackageName(), this.mCurUser);
                        VoiceInteractionManagerService.this.mAtmInternal.setAllowAppSwitches(VoiceInteractionManagerService.TAG, serviceInfo.applicationInfo.uid, this.mCurUser);
                    } else {
                        VoiceInteractionManagerService.this.mShortcutServiceInternal.setShortcutHostPackage(VoiceInteractionManagerService.TAG, (String) null, this.mCurUser);
                        VoiceInteractionManagerService.this.mAtmInternal.setAllowAppSwitches(VoiceInteractionManagerService.TAG, -1, this.mCurUser);
                    }
                }
                if (z && this.mImpl != null && this.mImpl.mUser == this.mCurUser && this.mImpl.mComponent.equals(componentName)) {
                    return;
                }
                unloadAllKeyphraseModels();
                if (this.mImpl != null) {
                    this.mImpl.shutdownLocked();
                }
                if (!z2) {
                    setImplLocked(new VoiceInteractionManagerServiceImpl(VoiceInteractionManagerService.this.mContext, UiThread.getHandler(), this, this.mCurUser, componentName));
                    this.mImpl.startLocked();
                    return;
                } else {
                    setImplLocked(null);
                    return;
                }
            }
            serviceInfo = null;
            componentName = null;
            if (componentName == null) {
            }
            if (VoiceInteractionManagerService.this.mUserManagerInternal.isUserUnlockingOrUnlocked(this.mCurUser)) {
            }
            if (z) {
            }
            unloadAllKeyphraseModels();
            if (this.mImpl != null) {
            }
            if (!z2) {
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public List<ResolveInfo> queryInteractorServices(int i, String str) {
            return VoiceInteractionManagerService.this.mContext.getPackageManager().queryIntentServicesAsUser(new Intent("android.service.voice.VoiceInteractionService").setPackage(str), 786560, i);
        }

        VoiceInteractionServiceInfo findAvailInteractor(int i, String str) {
            List<ResolveInfo> queryInteractorServices = queryInteractorServices(i, str);
            int size = queryInteractorServices.size();
            VoiceInteractionServiceInfo voiceInteractionServiceInfo = null;
            if (size == 0) {
                Slog.w(VoiceInteractionManagerService.TAG, "no available voice interaction services found for user " + i);
                return null;
            }
            for (int i2 = 0; i2 < size; i2++) {
                ServiceInfo serviceInfo = queryInteractorServices.get(i2).serviceInfo;
                if ((serviceInfo.applicationInfo.flags & 1) != 0) {
                    VoiceInteractionServiceInfo voiceInteractionServiceInfo2 = new VoiceInteractionServiceInfo(VoiceInteractionManagerService.this.mContext.getPackageManager(), serviceInfo);
                    if (voiceInteractionServiceInfo2.getParseError() != null) {
                        Slog.w(VoiceInteractionManagerService.TAG, "Bad interaction service " + serviceInfo.packageName + SliceClientPermissions.SliceAuthority.DELIMITER + serviceInfo.name + ": " + voiceInteractionServiceInfo2.getParseError());
                    } else if (voiceInteractionServiceInfo == null) {
                        voiceInteractionServiceInfo = voiceInteractionServiceInfo2;
                    } else {
                        Slog.w(VoiceInteractionManagerService.TAG, "More than one voice interaction service, picking first " + new ComponentName(voiceInteractionServiceInfo.getServiceInfo().packageName, voiceInteractionServiceInfo.getServiceInfo().name) + " over " + new ComponentName(serviceInfo.packageName, serviceInfo.name));
                    }
                }
            }
            return voiceInteractionServiceInfo;
        }

        ComponentName getCurInteractor(int i) {
            String stringForUser = Settings.Secure.getStringForUser(VoiceInteractionManagerService.this.mContext.getContentResolver(), "voice_interaction_service", i);
            if (TextUtils.isEmpty(stringForUser)) {
                return null;
            }
            return ComponentName.unflattenFromString(stringForUser);
        }

        void setCurInteractor(ComponentName componentName, int i) {
            Settings.Secure.putStringForUser(VoiceInteractionManagerService.this.mContext.getContentResolver(), "voice_interaction_service", componentName != null ? componentName.flattenToShortString() : "", i);
        }

        ComponentName findAvailRecognizer(String str, int i) {
            if (str == null) {
                str = getDefaultRecognizer();
            }
            List<RecognitionServiceInfo> availableServices = RecognitionServiceInfo.getAvailableServices(VoiceInteractionManagerService.this.mContext, i);
            if (availableServices.size() == 0) {
                Slog.w(VoiceInteractionManagerService.TAG, "no available voice recognition services found for user " + i);
                return null;
            }
            List<RecognitionServiceInfo> removeNonSelectableAsDefault = removeNonSelectableAsDefault(availableServices);
            if (availableServices.size() == 0) {
                Slog.w(VoiceInteractionManagerService.TAG, "No selectableAsDefault recognition services found for user " + i + ". Falling back to non selectableAsDefault ones.");
                availableServices = removeNonSelectableAsDefault;
            }
            int size = availableServices.size();
            if (str != null) {
                for (int i2 = 0; i2 < size; i2++) {
                    ServiceInfo serviceInfo = availableServices.get(i2).getServiceInfo();
                    if (str.equals(serviceInfo.packageName)) {
                        return new ComponentName(serviceInfo.packageName, serviceInfo.name);
                    }
                }
            }
            if (size > 1) {
                Slog.w(VoiceInteractionManagerService.TAG, "more than one voice recognition service found, picking first");
            }
            ServiceInfo serviceInfo2 = availableServices.get(0).getServiceInfo();
            return new ComponentName(serviceInfo2.packageName, serviceInfo2.name);
        }

        private List<RecognitionServiceInfo> removeNonSelectableAsDefault(List<RecognitionServiceInfo> list) {
            ArrayList arrayList = new ArrayList();
            for (int size = list.size() - 1; size >= 0; size--) {
                if (!list.get(size).isSelectableAsDefault()) {
                    arrayList.add(list.remove(size));
                }
            }
            return arrayList;
        }

        public String getDefaultRecognizer() {
            String string = VoiceInteractionManagerService.this.mContext.getString(R.string.CfMmi);
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            return string;
        }

        ComponentName getCurRecognizer(int i) {
            String stringForUser = Settings.Secure.getStringForUser(VoiceInteractionManagerService.this.mContext.getContentResolver(), "voice_recognition_service", i);
            if (TextUtils.isEmpty(stringForUser)) {
                return null;
            }
            return ComponentName.unflattenFromString(stringForUser);
        }

        void setCurRecognizer(ComponentName componentName, int i) {
            Settings.Secure.putStringForUser(VoiceInteractionManagerService.this.mContext.getContentResolver(), "voice_recognition_service", componentName != null ? componentName.flattenToShortString() : "", i);
        }

        ComponentName getCurAssistant(int i) {
            String stringForUser = Settings.Secure.getStringForUser(VoiceInteractionManagerService.this.mContext.getContentResolver(), "assistant", i);
            if (TextUtils.isEmpty(stringForUser)) {
                return null;
            }
            return ComponentName.unflattenFromString(stringForUser);
        }

        void resetCurAssistant(int i) {
            Settings.Secure.putStringForUser(VoiceInteractionManagerService.this.mContext.getContentResolver(), "assistant", null, i);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void forceRestartHotwordDetector() {
            this.mImpl.forceRestartHotwordDetector();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setDebugHotwordLogging(boolean z) {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "setTemporaryLogging without running voice interaction service");
                } else {
                    this.mImpl.setDebugHotwordLoggingLocked(z);
                }
            }
        }

        public void showSession(Bundle bundle, int i, String str) {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.showSessionLocked(bundle, i, str, null, null);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public boolean deliverNewSession(IBinder iBinder, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor) {
            boolean deliverNewSessionLocked;
            synchronized (this) {
                if (this.mImpl == null) {
                    throw new SecurityException("deliverNewSession without running voice interaction service");
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    deliverNewSessionLocked = this.mImpl.deliverNewSessionLocked(iBinder, iVoiceInteractionSession, iVoiceInteractor);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
            return deliverNewSessionLocked;
        }

        public boolean showSessionFromSession(IBinder iBinder, Bundle bundle, int i, String str) {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "showSessionFromSession without running voice interaction service");
                    return false;
                }
                if (iBinder == null) {
                    HotwordMetricsLogger.cancelHotwordTriggerToUiLatencySession(VoiceInteractionManagerService.this.mContext);
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return this.mImpl.showSessionLocked(bundle, i, str, null, null);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public boolean hideSessionFromSession(IBinder iBinder) {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "hideSessionFromSession without running voice interaction service");
                    return false;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return this.mImpl.hideSessionLocked();
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public int startVoiceActivity(IBinder iBinder, Intent intent, String str, String str2) {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "startVoiceActivity without running voice interaction service");
                    return -96;
                }
                int callingPid = Binder.getCallingPid();
                int callingUid = Binder.getCallingUid();
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    ActivityInfo resolveActivityInfo = intent.resolveActivityInfo(VoiceInteractionManagerService.this.mContext.getPackageManager(), 131072);
                    if (resolveActivityInfo != null) {
                        this.mImpl.grantImplicitAccessLocked(resolveActivityInfo.applicationInfo.uid, intent);
                    } else {
                        Slog.w(VoiceInteractionManagerService.TAG, "Cannot find ActivityInfo in startVoiceActivity.");
                    }
                    return this.mImpl.startVoiceActivityLocked(str2, callingPid, callingUid, iBinder, intent, str);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public int startAssistantActivity(IBinder iBinder, Intent intent, String str, String str2, Bundle bundle) {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "startAssistantActivity without running voice interaction service");
                    return -96;
                }
                int callingPid = Binder.getCallingPid();
                int callingUid = Binder.getCallingUid();
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return this.mImpl.startAssistantActivityLocked(str2, callingPid, callingUid, iBinder, intent, str, bundle);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void requestDirectActions(IBinder iBinder, int i, IBinder iBinder2, RemoteCallback remoteCallback, RemoteCallback remoteCallback2) {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "requestDirectActions without running voice interaction service");
                    remoteCallback2.sendResult((Bundle) null);
                } else {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        this.mImpl.requestDirectActionsLocked(iBinder, i, iBinder2, remoteCallback, remoteCallback2);
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
            }
        }

        public void performDirectAction(IBinder iBinder, String str, Bundle bundle, int i, IBinder iBinder2, RemoteCallback remoteCallback, RemoteCallback remoteCallback2) {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "performDirectAction without running voice interaction service");
                    remoteCallback2.sendResult((Bundle) null);
                } else {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        this.mImpl.performDirectActionLocked(iBinder, str, bundle, i, iBinder2, remoteCallback, remoteCallback2);
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
            }
        }

        public void setKeepAwake(IBinder iBinder, boolean z) {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "setKeepAwake without running voice interaction service");
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.setKeepAwakeLocked(iBinder, z);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void closeSystemDialogs(IBinder iBinder) {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "closeSystemDialogs without running voice interaction service");
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.closeSystemDialogsLocked(iBinder);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void finish(IBinder iBinder) {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "finish without running voice interaction service");
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.finishLocked(iBinder, false);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void setDisabledShowContext(int i) {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "setDisabledShowContext without running voice interaction service");
                    return;
                }
                int callingUid = Binder.getCallingUid();
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.setDisabledShowContextLocked(callingUid, i);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public int getDisabledShowContext() {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "getDisabledShowContext without running voice interaction service");
                    return 0;
                }
                int callingUid = Binder.getCallingUid();
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return this.mImpl.getDisabledShowContextLocked(callingUid);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public int getUserDisabledShowContext() {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "getUserDisabledShowContext without running voice interaction service");
                    return 0;
                }
                int callingUid = Binder.getCallingUid();
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return this.mImpl.getUserDisabledShowContextLocked(callingUid);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        @EnforcePermission("android.permission.ACCESS_VOICE_INTERACTION_SERVICE")
        public void setDisabled(boolean z) {
            super.setDisabled_enforcePermission();
            synchronized (this) {
                if (this.mTemporarilyDisabled == z) {
                    return;
                }
                this.mTemporarilyDisabled = z;
                if (z) {
                    Slog.i(VoiceInteractionManagerService.TAG, "setDisabled(): temporarily disabling and hiding current session");
                    try {
                        hideCurrentSession();
                    } catch (RemoteException e) {
                        Log.w(VoiceInteractionManagerService.TAG, "Failed to call hideCurrentSession", e);
                    }
                } else {
                    Slog.i(VoiceInteractionManagerService.TAG, "setDisabled(): re-enabling");
                }
            }
        }

        public void startListeningVisibleActivityChanged(IBinder iBinder) {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "startListeningVisibleActivityChanged without running voice interaction service");
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.startListeningVisibleActivityChangedLocked(iBinder);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void stopListeningVisibleActivityChanged(IBinder iBinder) {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "stopListeningVisibleActivityChanged without running voice interaction service");
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.stopListeningVisibleActivityChangedLocked(iBinder);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void notifyActivityEventChanged(final IBinder iBinder, final int i) {
            synchronized (this) {
                if (this.mImpl != null && iBinder != null) {
                    Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda5
                        public final void runOrThrow() {
                            VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.lambda$notifyActivityEventChanged$2(iBinder, i);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyActivityEventChanged$2(IBinder iBinder, int i) throws Exception {
            this.mImpl.notifyActivityEventChangedLocked(iBinder, i);
        }

        @EnforcePermission("android.permission.MANAGE_HOTWORD_DETECTION")
        public void updateState(final PersistableBundle persistableBundle, final SharedMemory sharedMemory, final IBinder iBinder) {
            super.updateState_enforcePermission();
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda6
                    public final void runOrThrow() {
                        VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.lambda$updateState$3(persistableBundle, sharedMemory, iBinder);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateState$3(PersistableBundle persistableBundle, SharedMemory sharedMemory, IBinder iBinder) throws Exception {
            this.mImpl.updateStateLocked(persistableBundle, sharedMemory, iBinder);
        }

        @EnforcePermission("android.permission.MANAGE_HOTWORD_DETECTION")
        public void initAndVerifyDetector(final Identity identity, final PersistableBundle persistableBundle, final SharedMemory sharedMemory, final IBinder iBinder, final IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback, final int i) {
            super.initAndVerifyDetector_enforcePermission();
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                identity.uid = Binder.getCallingUid();
                identity.pid = Binder.getCallingPid();
                Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda4
                    public final void runOrThrow() {
                        VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.lambda$initAndVerifyDetector$4(identity, persistableBundle, sharedMemory, iBinder, iHotwordRecognitionStatusCallback, i);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$initAndVerifyDetector$4(Identity identity, PersistableBundle persistableBundle, SharedMemory sharedMemory, IBinder iBinder, IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback, int i) throws Exception {
            this.mImpl.initAndVerifyDetectorLocked(identity, persistableBundle, sharedMemory, iBinder, iHotwordRecognitionStatusCallback, i);
        }

        public void destroyDetector(final IBinder iBinder) {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "destroyDetector without running voice interaction service");
                } else {
                    Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda1
                        public final void runOrThrow() {
                            VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.lambda$destroyDetector$5(iBinder);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$destroyDetector$5(IBinder iBinder) throws Exception {
            this.mImpl.destroyDetectorLocked(iBinder);
        }

        public void shutdownHotwordDetectionService() {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "shutdownHotwordDetectionService without running voice interaction service");
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.shutdownHotwordDetectionServiceLocked();
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        @EnforcePermission("android.permission.ACCESS_VOICE_INTERACTION_SERVICE")
        public void enableVisualQueryDetection(IVisualQueryDetectionAttentionListener iVisualQueryDetectionAttentionListener) {
            super.enableVisualQueryDetection_enforcePermission();
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "enableVisualQueryDetection without running voice interaction service");
                } else {
                    this.mImpl.setVisualQueryDetectionAttentionListenerLocked(iVisualQueryDetectionAttentionListener);
                }
            }
        }

        @EnforcePermission("android.permission.ACCESS_VOICE_INTERACTION_SERVICE")
        public void disableVisualQueryDetection() {
            super.disableVisualQueryDetection_enforcePermission();
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "disableVisualQueryDetection without running voice interaction service");
                } else {
                    this.mImpl.setVisualQueryDetectionAttentionListenerLocked(null);
                }
            }
        }

        public void startPerceiving(IVisualQueryDetectionVoiceInteractionCallback iVisualQueryDetectionVoiceInteractionCallback) throws RemoteException {
            enforceCallingPermission("android.permission.RECORD_AUDIO");
            enforceCallingPermission("android.permission.CAMERA");
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "startPerceiving without running voice interaction service");
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.startPerceivingLocked(iVisualQueryDetectionVoiceInteractionCallback);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void stopPerceiving() throws RemoteException {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "stopPerceiving without running voice interaction service");
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.stopPerceivingLocked();
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void startListeningFromMic(AudioFormat audioFormat, IMicrophoneHotwordDetectionVoiceInteractionCallback iMicrophoneHotwordDetectionVoiceInteractionCallback) throws RemoteException {
            enforceCallingPermission("android.permission.RECORD_AUDIO");
            enforceCallingPermission("android.permission.CAPTURE_AUDIO_HOTWORD");
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "startListeningFromMic without running voice interaction service");
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.startListeningFromMicLocked(audioFormat, iMicrophoneHotwordDetectionVoiceInteractionCallback);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void startListeningFromExternalSource(ParcelFileDescriptor parcelFileDescriptor, AudioFormat audioFormat, PersistableBundle persistableBundle, IBinder iBinder, IMicrophoneHotwordDetectionVoiceInteractionCallback iMicrophoneHotwordDetectionVoiceInteractionCallback) throws RemoteException {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "startListeningFromExternalSource without running voice interaction service");
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.startListeningFromExternalSourceLocked(parcelFileDescriptor, audioFormat, persistableBundle, iBinder, iMicrophoneHotwordDetectionVoiceInteractionCallback);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void stopListeningFromMic() throws RemoteException {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "stopListeningFromMic without running voice interaction service");
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.stopListeningFromMicLocked();
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void triggerHardwareRecognitionEventForTest(SoundTrigger.KeyphraseRecognitionEvent keyphraseRecognitionEvent, IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback) throws RemoteException {
            enforceCallingPermission("android.permission.RECORD_AUDIO");
            enforceCallingPermission("android.permission.CAPTURE_AUDIO_HOTWORD");
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "triggerHardwareRecognitionEventForTest without running voice interaction service");
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.triggerHardwareRecognitionEventForTestLocked(keyphraseRecognitionEvent, iHotwordRecognitionStatusCallback);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(int i, String str) {
            enforceCallerAllowedToEnrollVoiceModel();
            if (str == null) {
                throw new IllegalArgumentException("Illegal argument(s) in getKeyphraseSoundModel");
            }
            int callingUserId = UserHandle.getCallingUserId();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return VoiceInteractionManagerService.this.mDbHelper.getKeyphraseSoundModel(i, callingUserId, str);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int updateKeyphraseSoundModel(SoundTrigger.KeyphraseSoundModel keyphraseSoundModel) {
            enforceCallerAllowedToEnrollVoiceModel();
            if (keyphraseSoundModel == null) {
                throw new IllegalArgumentException("Model must not be null");
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                if (VoiceInteractionManagerService.this.mDbHelper.updateKeyphraseSoundModel(keyphraseSoundModel)) {
                    synchronized (this) {
                        if (this.mImpl != null && this.mImpl.mService != null) {
                            this.mImpl.notifySoundModelsChangedLocked();
                        }
                    }
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return 0;
                }
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return Integer.MIN_VALUE;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
        }

        public int deleteKeyphraseSoundModel(int i, String str) {
            int unloadKeyphraseModel;
            enforceCallerAllowedToEnrollVoiceModel();
            if (str == null) {
                throw new IllegalArgumentException("Illegal argument(s) in deleteKeyphraseSoundModel");
            }
            int callingUserId = UserHandle.getCallingUserId();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                SoundTriggerSession soundTriggerSession = VoiceInteractionManagerService.this.mLoadedKeyphraseIds.get(Integer.valueOf(i));
                if (soundTriggerSession != null && (unloadKeyphraseModel = soundTriggerSession.unloadKeyphraseModel(i)) != 0) {
                    Slog.w(VoiceInteractionManagerService.TAG, "Unable to unload keyphrase sound model:" + unloadKeyphraseModel);
                }
                boolean deleteKeyphraseSoundModel = VoiceInteractionManagerService.this.mDbHelper.deleteKeyphraseSoundModel(i, callingUserId, str);
                int i2 = deleteKeyphraseSoundModel ? 0 : Integer.MIN_VALUE;
                if (deleteKeyphraseSoundModel) {
                    synchronized (this) {
                        if (this.mImpl != null && this.mImpl.mService != null) {
                            this.mImpl.notifySoundModelsChangedLocked();
                        }
                        VoiceInteractionManagerService.this.mLoadedKeyphraseIds.remove(Integer.valueOf(i));
                    }
                }
                return i2;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        @EnforcePermission("android.permission.MANAGE_VOICE_KEYPHRASES")
        public void setModelDatabaseForTestEnabled(boolean z, IBinder iBinder) {
            super.setModelDatabaseForTestEnabled_enforcePermission();
            enforceCallerAllowedToEnrollVoiceModel();
            synchronized (this) {
                if (z) {
                    final TestModelEnrollmentDatabase testModelEnrollmentDatabase = new TestModelEnrollmentDatabase();
                    try {
                        iBinder.linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda2
                            @Override // android.os.IBinder.DeathRecipient
                            public final void binderDied() {
                                VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.lambda$setModelDatabaseForTestEnabled$6(testModelEnrollmentDatabase);
                            }
                        }, 0);
                        VoiceInteractionManagerService.this.mDbHelper = testModelEnrollmentDatabase;
                        this.mImpl.notifySoundModelsChangedLocked();
                    } catch (RemoteException unused) {
                    }
                } else if (VoiceInteractionManagerService.this.mDbHelper != VoiceInteractionManagerService.this.mRealDbHelper) {
                    VoiceInteractionManagerService voiceInteractionManagerService = VoiceInteractionManagerService.this;
                    voiceInteractionManagerService.mDbHelper = voiceInteractionManagerService.mRealDbHelper;
                    this.mImpl.notifySoundModelsChangedLocked();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setModelDatabaseForTestEnabled$6(TestModelEnrollmentDatabase testModelEnrollmentDatabase) {
            synchronized (this) {
                if (VoiceInteractionManagerService.this.mDbHelper == testModelEnrollmentDatabase) {
                    VoiceInteractionManagerService voiceInteractionManagerService = VoiceInteractionManagerService.this;
                    voiceInteractionManagerService.mDbHelper = voiceInteractionManagerService.mRealDbHelper;
                    this.mImpl.notifySoundModelsChangedLocked();
                }
            }
        }

        public boolean isEnrolledForKeyphrase(int i, String str) {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
            }
            if (str == null) {
                throw new IllegalArgumentException("Illegal argument(s) in isEnrolledForKeyphrase");
            }
            int callingUserId = UserHandle.getCallingUserId();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return VoiceInteractionManagerService.this.mDbHelper.getKeyphraseSoundModel(i, callingUserId, str) != null;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public KeyphraseMetadata getEnrolledKeyphraseMetadata(String str, String str2) {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
            }
            if (str2 == null) {
                throw new IllegalArgumentException("Illegal argument(s) in isEnrolledForKeyphrase");
            }
            int callingUserId = UserHandle.getCallingUserId();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                SoundTrigger.KeyphraseSoundModel keyphraseSoundModel = VoiceInteractionManagerService.this.mDbHelper.getKeyphraseSoundModel(str, callingUserId, str2);
                if (keyphraseSoundModel == null) {
                    return null;
                }
                for (SoundTrigger.Keyphrase keyphrase : keyphraseSoundModel.getKeyphrases()) {
                    if (str.equals(keyphrase.getText())) {
                        ArraySet arraySet = new ArraySet();
                        arraySet.add(keyphrase.getLocale());
                        return new KeyphraseMetadata(keyphrase.getId(), keyphrase.getText(), arraySet, keyphrase.getRecognitionModes());
                    }
                }
                return null;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public class SoundTriggerSession extends IVoiceInteractionSoundTriggerSession.Stub {
            final SoundTriggerInternal.Session mSession;
            private IHotwordRecognitionStatusCallback mSessionExternalCallback;
            private IRecognitionStatusCallback mSessionInternalCallback;
            private final Identity mVoiceInteractorIdentity;

            SoundTriggerSession(SoundTriggerInternal.Session session, Identity identity) {
                this.mSession = session;
                this.mVoiceInteractorIdentity = identity;
            }

            public SoundTrigger.ModuleProperties getDspModuleProperties() {
                SoundTrigger.ModuleProperties moduleProperties;
                synchronized (VoiceInteractionManagerServiceStub.this) {
                    VoiceInteractionManagerServiceStub.this.enforceIsCurrentVoiceInteractionService();
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        moduleProperties = this.mSession.getModuleProperties();
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
                return moduleProperties;
            }

            public int startRecognition(int i, String str, IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback, SoundTrigger.RecognitionConfig recognitionConfig, boolean z) {
                synchronized (VoiceInteractionManagerServiceStub.this) {
                    VoiceInteractionManagerServiceStub.this.enforceIsCurrentVoiceInteractionService();
                    if (iHotwordRecognitionStatusCallback == null || recognitionConfig == null || str == null) {
                        throw new IllegalArgumentException("Illegal argument(s) in startRecognition");
                    }
                    if (z) {
                        VoiceInteractionManagerServiceStub.this.enforceCallingPermission("android.permission.SOUND_TRIGGER_RUN_IN_BATTERY_SAVER");
                    }
                }
                int callingUserId = UserHandle.getCallingUserId();
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    SoundTrigger.KeyphraseSoundModel keyphraseSoundModel = VoiceInteractionManagerService.this.mDbHelper.getKeyphraseSoundModel(i, callingUserId, str);
                    if (keyphraseSoundModel != null && keyphraseSoundModel.getUuid() != null && keyphraseSoundModel.getKeyphrases() != null) {
                        synchronized (VoiceInteractionManagerServiceStub.this) {
                            VoiceInteractionManagerService.this.mLoadedKeyphraseIds.put(Integer.valueOf(i), this);
                            if (this.mSessionExternalCallback == null || this.mSessionInternalCallback == null || iHotwordRecognitionStatusCallback.asBinder() != this.mSessionExternalCallback.asBinder()) {
                                this.mSessionInternalCallback = VoiceInteractionManagerServiceStub.this.createSoundTriggerCallbackLocked(iHotwordRecognitionStatusCallback, this.mVoiceInteractorIdentity);
                                this.mSessionExternalCallback = iHotwordRecognitionStatusCallback;
                            }
                        }
                        return this.mSession.startRecognition(i, keyphraseSoundModel, this.mSessionInternalCallback, recognitionConfig, z);
                    }
                    Slog.w(VoiceInteractionManagerService.TAG, "No matching sound model found in startRecognition");
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return Integer.MIN_VALUE;
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }

            public int stopRecognition(int i, IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback) {
                IRecognitionStatusCallback createSoundTriggerCallbackLocked;
                synchronized (VoiceInteractionManagerServiceStub.this) {
                    VoiceInteractionManagerServiceStub.this.enforceIsCurrentVoiceInteractionService();
                    if (this.mSessionExternalCallback != null && this.mSessionInternalCallback != null && iHotwordRecognitionStatusCallback.asBinder() == this.mSessionExternalCallback.asBinder()) {
                        createSoundTriggerCallbackLocked = this.mSessionInternalCallback;
                        this.mSessionExternalCallback = null;
                        this.mSessionInternalCallback = null;
                    }
                    createSoundTriggerCallbackLocked = VoiceInteractionManagerServiceStub.this.createSoundTriggerCallbackLocked(iHotwordRecognitionStatusCallback, this.mVoiceInteractorIdentity);
                    Slog.w(VoiceInteractionManagerService.TAG, "stopRecognition() called with a different callback thanstartRecognition()");
                    this.mSessionExternalCallback = null;
                    this.mSessionInternalCallback = null;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return this.mSession.stopRecognition(i, createSoundTriggerCallbackLocked);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }

            public int setParameter(int i, @ModelParams int i2, int i3) {
                synchronized (VoiceInteractionManagerServiceStub.this) {
                    VoiceInteractionManagerServiceStub.this.enforceIsCurrentVoiceInteractionService();
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return this.mSession.setParameter(i, i2, i3);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }

            public int getParameter(int i, @ModelParams int i2) {
                synchronized (VoiceInteractionManagerServiceStub.this) {
                    VoiceInteractionManagerServiceStub.this.enforceIsCurrentVoiceInteractionService();
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return this.mSession.getParameter(i, i2);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }

            public SoundTrigger.ModelParamRange queryParameter(int i, @ModelParams int i2) {
                synchronized (VoiceInteractionManagerServiceStub.this) {
                    VoiceInteractionManagerServiceStub.this.enforceIsCurrentVoiceInteractionService();
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return this.mSession.queryParameter(i, i2);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }

            public void detach() {
                this.mSession.detach();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public int unloadKeyphraseModel(int i) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return this.mSession.unloadKeyphraseModel(i);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void unloadAllKeyphraseModels() {
            for (int i = 0; i < VoiceInteractionManagerService.this.mLoadedKeyphraseIds.size(); i++) {
                int intValue = VoiceInteractionManagerService.this.mLoadedKeyphraseIds.keyAt(i).intValue();
                int unloadKeyphraseModel = VoiceInteractionManagerService.this.mLoadedKeyphraseIds.valueAt(i).unloadKeyphraseModel(intValue);
                if (unloadKeyphraseModel != 0) {
                    Slog.w(VoiceInteractionManagerService.TAG, "Failed to unload keyphrase " + intValue + ":" + unloadKeyphraseModel);
                }
            }
            VoiceInteractionManagerService.this.mLoadedKeyphraseIds.clear();
        }

        public ComponentName getActiveServiceComponentName() {
            ComponentName componentName;
            synchronized (this) {
                componentName = this.mImpl != null ? this.mImpl.mComponent : null;
            }
            return componentName;
        }

        @EnforcePermission("android.permission.ACCESS_VOICE_INTERACTION_SERVICE")
        public boolean showSessionForActiveService(Bundle bundle, int i, String str, IVoiceInteractionSessionShowCallback iVoiceInteractionSessionShowCallback, IBinder iBinder) {
            super.showSessionForActiveService_enforcePermission();
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "showSessionForActiveService without running voice interactionservice");
                    return false;
                }
                if (this.mTemporarilyDisabled) {
                    Slog.i(VoiceInteractionManagerService.TAG, "showSessionForActiveService(): ignored while temporarily disabled");
                    return false;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    HotwordMetricsLogger.cancelHotwordTriggerToUiLatencySession(VoiceInteractionManagerService.this.mContext);
                    return this.mImpl.showSessionLocked(bundle, i | 1 | 2, str, iVoiceInteractionSessionShowCallback, iBinder);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        @EnforcePermission("android.permission.ACCESS_VOICE_INTERACTION_SERVICE")
        public void hideCurrentSession() throws RemoteException {
            super.hideCurrentSession_enforcePermission();
            if (this.mImpl == null) {
                return;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                if (this.mImpl.mActiveSession != null && this.mImpl.mActiveSession.mSession != null) {
                    try {
                        this.mImpl.mActiveSession.mSession.closeSystemDialogs();
                    } catch (RemoteException e) {
                        Log.w(VoiceInteractionManagerService.TAG, "Failed to call closeSystemDialogs", e);
                    }
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        @EnforcePermission("android.permission.ACCESS_VOICE_INTERACTION_SERVICE")
        public void launchVoiceAssistFromKeyguard() {
            super.launchVoiceAssistFromKeyguard_enforcePermission();
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "launchVoiceAssistFromKeyguard without running voice interactionservice");
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.launchVoiceAssistFromKeyguard();
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        @EnforcePermission("android.permission.ACCESS_VOICE_INTERACTION_SERVICE")
        public boolean isSessionRunning() {
            boolean z;
            super.isSessionRunning_enforcePermission();
            synchronized (this) {
                z = (this.mImpl == null || this.mImpl.mActiveSession == null) ? false : true;
            }
            return z;
        }

        @EnforcePermission("android.permission.ACCESS_VOICE_INTERACTION_SERVICE")
        public boolean activeServiceSupportsAssist() {
            boolean z;
            super.activeServiceSupportsAssist_enforcePermission();
            synchronized (this) {
                z = (this.mImpl == null || this.mImpl.mInfo == null || !this.mImpl.mInfo.getSupportsAssist()) ? false : true;
            }
            return z;
        }

        @EnforcePermission("android.permission.ACCESS_VOICE_INTERACTION_SERVICE")
        public boolean activeServiceSupportsLaunchFromKeyguard() throws RemoteException {
            boolean z;
            super.activeServiceSupportsLaunchFromKeyguard_enforcePermission();
            synchronized (this) {
                z = (this.mImpl == null || this.mImpl.mInfo == null || !this.mImpl.mInfo.getSupportsLaunchFromKeyguard()) ? false : true;
            }
            return z;
        }

        @EnforcePermission("android.permission.ACCESS_VOICE_INTERACTION_SERVICE")
        public void onLockscreenShown() {
            super.onLockscreenShown_enforcePermission();
            synchronized (this) {
                if (this.mImpl == null) {
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (this.mImpl.mActiveSession != null && this.mImpl.mActiveSession.mSession != null) {
                        try {
                            this.mImpl.mActiveSession.mSession.onLockscreenShown();
                        } catch (RemoteException e) {
                            Log.w(VoiceInteractionManagerService.TAG, "Failed to call onLockscreenShown", e);
                        }
                    }
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        @EnforcePermission("android.permission.ACCESS_VOICE_INTERACTION_SERVICE")
        public void registerVoiceInteractionSessionListener(IVoiceInteractionSessionListener iVoiceInteractionSessionListener) {
            super.registerVoiceInteractionSessionListener_enforcePermission();
            synchronized (this) {
                VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.register(iVoiceInteractionSessionListener);
            }
        }

        @EnforcePermission("android.permission.ACCESS_VOICE_INTERACTION_SERVICE")
        public void getActiveServiceSupportedActions(List<String> list, IVoiceActionCheckCallback iVoiceActionCheckCallback) {
            super.getActiveServiceSupportedActions_enforcePermission();
            synchronized (this) {
                if (this.mImpl == null) {
                    try {
                        iVoiceActionCheckCallback.onComplete((List) null);
                    } catch (RemoteException unused) {
                    }
                    return;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mImpl.getActiveServiceSupportedActions(list, iVoiceActionCheckCallback);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void onSessionShown() {
            synchronized (this) {
                int beginBroadcast = VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.beginBroadcast();
                for (int i = 0; i < beginBroadcast; i++) {
                    try {
                        VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.getBroadcastItem(i).onVoiceSessionShown();
                    } catch (RemoteException e) {
                        Slog.e(VoiceInteractionManagerService.TAG, "Error delivering voice interaction open event.", e);
                    }
                }
                VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.finishBroadcast();
            }
        }

        public void onSessionHidden() {
            synchronized (this) {
                int beginBroadcast = VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.beginBroadcast();
                for (int i = 0; i < beginBroadcast; i++) {
                    try {
                        VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.getBroadcastItem(i).onVoiceSessionHidden();
                    } catch (RemoteException e) {
                        Slog.e(VoiceInteractionManagerService.TAG, "Error delivering voice interaction closed event.", e);
                    }
                }
                VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.finishBroadcast();
            }
        }

        public void setSessionWindowVisible(IBinder iBinder, final boolean z) {
            synchronized (this) {
                if (this.mImpl == null) {
                    Slog.w(VoiceInteractionManagerService.TAG, "setSessionWindowVisible called without running voice interaction service");
                    return;
                }
                if (this.mImpl.mActiveSession != null && iBinder == this.mImpl.mActiveSession.mToken) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.broadcast(new Consumer() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda3
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.lambda$setSessionWindowVisible$7(z, (IVoiceInteractionSessionListener) obj);
                            }
                        });
                        return;
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
                Slog.w(VoiceInteractionManagerService.TAG, "setSessionWindowVisible does not match active session");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$setSessionWindowVisible$7(boolean z, IVoiceInteractionSessionListener iVoiceInteractionSessionListener) {
            try {
                iVoiceInteractionSessionListener.onVoiceSessionWindowVisibilityChanged(z);
            } catch (RemoteException e) {
                Slog.e(VoiceInteractionManagerService.TAG, "Error delivering window visibility event to listener.", e);
            }
        }

        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(VoiceInteractionManagerService.this.mContext, VoiceInteractionManagerService.TAG, printWriter)) {
                synchronized (this) {
                    printWriter.println("VOICE INTERACTION MANAGER (dumpsys voiceinteraction)");
                    printWriter.println("  mEnableService: " + this.mEnableService);
                    printWriter.println("  mTemporarilyDisabled: " + this.mTemporarilyDisabled);
                    printWriter.println("  mCurUser: " + this.mCurUser);
                    printWriter.println("  mCurUserSupported: " + this.mCurUserSupported);
                    printWriter.println("  mIsHdsRequired: " + this.IS_HDS_REQUIRED);
                    VoiceInteractionManagerService.this.dumpSupportedUsers(printWriter, "  ");
                    VoiceInteractionManagerService.this.mDbHelper.dump(printWriter);
                    if (this.mImpl == null) {
                        printWriter.println("  (No active implementation)");
                    } else {
                        this.mImpl.dumpLocked(fileDescriptor, printWriter, strArr);
                    }
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
            new VoiceInteractionManagerServiceShellCommand(VoiceInteractionManagerService.this.mServiceStub).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }

        public void setUiHints(Bundle bundle) {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                int beginBroadcast = VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.beginBroadcast();
                for (int i = 0; i < beginBroadcast; i++) {
                    try {
                        VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.getBroadcastItem(i).onSetUiHints(bundle);
                    } catch (RemoteException e) {
                        Slog.e(VoiceInteractionManagerService.TAG, "Error delivering UI hints.", e);
                    }
                }
                VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.finishBroadcast();
            }
        }

        private boolean isCallerHoldingPermission(String str) {
            return VoiceInteractionManagerService.this.mContext.checkCallingOrSelfPermission(str) == 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void enforceCallingPermission(String str) {
            if (isCallerHoldingPermission(str)) {
                return;
            }
            throw new SecurityException("Caller does not hold the permission " + str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void enforceIsCurrentVoiceInteractionService() {
            if (!isCallerCurrentVoiceInteractionService()) {
                throw new SecurityException("Caller is not the current voice interaction service");
            }
        }

        private void enforceCallerAllowedToEnrollVoiceModel() {
            if (isCallerHoldingPermission("android.permission.KEYPHRASE_ENROLLMENT_APPLICATION")) {
                return;
            }
            enforceCallingPermission("android.permission.MANAGE_VOICE_KEYPHRASES");
            enforceIsCurrentVoiceInteractionService();
        }

        private boolean isCallerCurrentVoiceInteractionService() {
            return this.mImpl != null && this.mImpl.mInfo.getServiceInfo().applicationInfo.uid == Binder.getCallingUid();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setImplLocked(VoiceInteractionManagerServiceImpl voiceInteractionManagerServiceImpl) {
            this.mImpl = voiceInteractionManagerServiceImpl;
            VoiceInteractionManagerService.this.mAtmInternal.notifyActiveVoiceInteractionServiceChanged(getActiveServiceComponentName());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IRecognitionStatusCallback createSoundTriggerCallbackLocked(IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback, Identity identity) {
            if (this.mImpl == null) {
                return null;
            }
            return this.mImpl.createSoundTriggerCallbackLocked(VoiceInteractionManagerService.this.mContext, iHotwordRecognitionStatusCallback, identity);
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        class RoleObserver implements OnRoleHoldersChangedListener {
            private PackageManager mPm;
            private RoleManager mRm;

            RoleObserver(Executor executor) {
                this.mPm = VoiceInteractionManagerService.this.mContext.getPackageManager();
                RoleManager roleManager = (RoleManager) VoiceInteractionManagerService.this.mContext.getSystemService(RoleManager.class);
                this.mRm = roleManager;
                roleManager.addOnRoleHoldersChangedListenerAsUser(executor, this, UserHandle.ALL);
                if (this.mRm.isRoleAvailable("android.app.role.ASSISTANT")) {
                    onRoleHoldersChanged("android.app.role.ASSISTANT", UserHandle.of(((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).getCurrentUserId()));
                }
            }

            public void onRoleHoldersChanged(String str, UserHandle userHandle) {
                UserInfo userInfo;
                if (str.equals("android.app.role.ASSISTANT")) {
                    List roleHoldersAsUser = this.mRm.getRoleHoldersAsUser(str, userHandle);
                    if (roleHoldersAsUser.isEmpty() && (userInfo = VoiceInteractionManagerService.this.mUserManagerInternal.getUserInfo(userHandle.getIdentifier())) != null && userInfo.preCreated) {
                        Slogf.d(VoiceInteractionManagerService.TAG, "onRoleHoldersChanged(): ignoring pre-created user %s for now, this method will be called again when it's converted to a real user", userInfo.toFullString());
                        return;
                    }
                    int identifier = userHandle.getIdentifier();
                    String str2 = "";
                    if (roleHoldersAsUser.isEmpty()) {
                        Settings.Secure.putStringForUser(VoiceInteractionManagerService.this.getContext().getContentResolver(), "assistant", "", identifier);
                        Settings.Secure.putStringForUser(VoiceInteractionManagerService.this.getContext().getContentResolver(), "voice_interaction_service", "", identifier);
                        return;
                    }
                    String str3 = (String) roleHoldersAsUser.get(0);
                    Iterator it = VoiceInteractionManagerServiceStub.this.queryInteractorServices(identifier, str3).iterator();
                    while (it.hasNext()) {
                        ServiceInfo serviceInfo = ((ResolveInfo) it.next()).serviceInfo;
                        VoiceInteractionServiceInfo voiceInteractionServiceInfo = new VoiceInteractionServiceInfo(this.mPm, serviceInfo);
                        if (voiceInteractionServiceInfo.getSupportsAssist()) {
                            String flattenToShortString = serviceInfo.getComponentName().flattenToShortString();
                            if (voiceInteractionServiceInfo.getRecognitionService() == null) {
                                Slog.e(VoiceInteractionManagerService.TAG, "The RecognitionService must be set to avoid boot loop on earlier platform version. Also make sure that this is a valid RecognitionService when running on Android 11 or earlier.");
                            } else {
                                str2 = flattenToShortString;
                            }
                            Settings.Secure.putStringForUser(VoiceInteractionManagerService.this.getContext().getContentResolver(), "assistant", str2, identifier);
                            Settings.Secure.putStringForUser(VoiceInteractionManagerService.this.getContext().getContentResolver(), "voice_interaction_service", str2, identifier);
                            return;
                        }
                    }
                    Iterator it2 = this.mPm.queryIntentActivitiesAsUser(new Intent("android.intent.action.ASSIST").setPackage(str3), 851968, identifier).iterator();
                    if (it2.hasNext()) {
                        Settings.Secure.putStringForUser(VoiceInteractionManagerService.this.getContext().getContentResolver(), "assistant", ((ResolveInfo) it2.next()).activityInfo.getComponentName().flattenToShortString(), identifier);
                        Settings.Secure.putStringForUser(VoiceInteractionManagerService.this.getContext().getContentResolver(), "voice_interaction_service", "", identifier);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public class SettingsObserver extends ContentObserver {
            SettingsObserver(Handler handler) {
                super(handler);
                VoiceInteractionManagerService.this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("voice_interaction_service"), false, this, -1);
            }

            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                synchronized (VoiceInteractionManagerServiceStub.this) {
                    VoiceInteractionManagerServiceStub.this.switchImplementationIfNeededLocked(false);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void resetServicesIfNoRecognitionService(ComponentName componentName, int i) {
            Iterator<ResolveInfo> it = queryInteractorServices(i, componentName.getPackageName()).iterator();
            while (it.hasNext()) {
                VoiceInteractionServiceInfo voiceInteractionServiceInfo = new VoiceInteractionServiceInfo(VoiceInteractionManagerService.this.mContext.getPackageManager(), it.next().serviceInfo);
                if (voiceInteractionServiceInfo.getSupportsAssist() && voiceInteractionServiceInfo.getRecognitionService() == null) {
                    Slog.e(VoiceInteractionManagerService.TAG, "The RecognitionService must be set to avoid boot loop on earlier platform version. Also make sure that this is a valid RecognitionService when running on Android 11 or earlier.");
                    setCurInteractor(null, i);
                    resetCurAssistant(i);
                }
            }
        }

        /* renamed from: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$2, reason: invalid class name */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        class AnonymousClass2 extends PackageMonitor {
            public void onHandleUserStop(Intent intent, int i) {
            }

            AnonymousClass2() {
            }

            public boolean onHandleForceStop(Intent intent, String[] strArr, int i, boolean z) {
                boolean z2;
                boolean z3;
                int userId = UserHandle.getUserId(i);
                ComponentName curInteractor = VoiceInteractionManagerServiceStub.this.getCurInteractor(userId);
                ComponentName curRecognizer = VoiceInteractionManagerServiceStub.this.getCurRecognizer(userId);
                int length = strArr.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        z2 = false;
                        z3 = false;
                        break;
                    }
                    String str = strArr[i2];
                    if (curInteractor != null && str.equals(curInteractor.getPackageName())) {
                        z3 = false;
                        z2 = true;
                        break;
                    }
                    if (curRecognizer != null && str.equals(curRecognizer.getPackageName())) {
                        z2 = false;
                        z3 = true;
                        break;
                    }
                    i2++;
                }
                if (z2 && z) {
                    synchronized (VoiceInteractionManagerServiceStub.this) {
                        Slog.i(VoiceInteractionManagerService.TAG, "Force stopping current voice interactor: " + VoiceInteractionManagerServiceStub.this.getCurInteractor(userId));
                        VoiceInteractionManagerServiceStub.this.unloadAllKeyphraseModels();
                        if (VoiceInteractionManagerServiceStub.this.mImpl != null) {
                            VoiceInteractionManagerServiceStub.this.mImpl.shutdownLocked();
                            VoiceInteractionManagerServiceStub.this.setImplLocked(null);
                        }
                        VoiceInteractionManagerServiceStub.this.setCurInteractor(null, userId);
                        VoiceInteractionManagerServiceStub.this.setCurRecognizer(null, userId);
                        VoiceInteractionManagerServiceStub.this.resetCurAssistant(userId);
                        VoiceInteractionManagerServiceStub.this.initForUser(userId);
                        VoiceInteractionManagerServiceStub.this.switchImplementationIfNeededLocked(true);
                        Context context = VoiceInteractionManagerService.this.getContext();
                        ((RoleManager) context.getSystemService(RoleManager.class)).clearRoleHoldersAsUser("android.app.role.ASSISTANT", 0, UserHandle.of(userId), context.getMainExecutor(), new Consumer() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$2$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.AnonymousClass2.lambda$onHandleForceStop$0((Boolean) obj);
                            }
                        });
                    }
                } else if (z3 && z) {
                    synchronized (VoiceInteractionManagerServiceStub.this) {
                        Slog.i(VoiceInteractionManagerService.TAG, "Force stopping current voice recognizer: " + VoiceInteractionManagerServiceStub.this.getCurRecognizer(userId));
                        VoiceInteractionManagerServiceStub.this.setCurInteractor(null, userId);
                        VoiceInteractionManagerServiceStub.this.initRecognizer(userId);
                    }
                }
                return z2 || z3;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static /* synthetic */ void lambda$onHandleForceStop$0(Boolean bool) {
                if (bool.booleanValue()) {
                    return;
                }
                Slog.e(VoiceInteractionManagerService.TAG, "Failed to clear default assistant for force stop");
            }

            public void onPackageModified(String str) {
                if (VoiceInteractionManagerServiceStub.this.mCurUser == getChangingUserId() && isPackageAppearing(str) == 0) {
                    VoiceInteractionManagerServiceStub voiceInteractionManagerServiceStub = VoiceInteractionManagerServiceStub.this;
                    if (voiceInteractionManagerServiceStub.getCurRecognizer(voiceInteractionManagerServiceStub.mCurUser) == null) {
                        VoiceInteractionManagerServiceStub voiceInteractionManagerServiceStub2 = VoiceInteractionManagerServiceStub.this;
                        voiceInteractionManagerServiceStub2.initRecognizer(voiceInteractionManagerServiceStub2.mCurUser);
                    }
                    String stringForUser = Settings.Secure.getStringForUser(VoiceInteractionManagerService.this.mContext.getContentResolver(), "voice_interaction_service", VoiceInteractionManagerServiceStub.this.mCurUser);
                    VoiceInteractionManagerServiceStub voiceInteractionManagerServiceStub3 = VoiceInteractionManagerServiceStub.this;
                    ComponentName curInteractor = voiceInteractionManagerServiceStub3.getCurInteractor(voiceInteractionManagerServiceStub3.mCurUser);
                    if (curInteractor == null && !"".equals(stringForUser)) {
                        VoiceInteractionManagerServiceStub voiceInteractionManagerServiceStub4 = VoiceInteractionManagerServiceStub.this;
                        VoiceInteractionServiceInfo findAvailInteractor = voiceInteractionManagerServiceStub4.findAvailInteractor(voiceInteractionManagerServiceStub4.mCurUser, str);
                        if (findAvailInteractor != null) {
                            ComponentName componentName = new ComponentName(findAvailInteractor.getServiceInfo().packageName, findAvailInteractor.getServiceInfo().name);
                            VoiceInteractionManagerServiceStub voiceInteractionManagerServiceStub5 = VoiceInteractionManagerServiceStub.this;
                            voiceInteractionManagerServiceStub5.setCurInteractor(componentName, voiceInteractionManagerServiceStub5.mCurUser);
                            return;
                        }
                        return;
                    }
                    if (didSomePackagesChange()) {
                        if (curInteractor == null || !str.equals(curInteractor.getPackageName())) {
                            return;
                        }
                        VoiceInteractionManagerServiceStub.this.switchImplementationIfNeeded(true);
                        return;
                    }
                    if (curInteractor == null || !isComponentModified(curInteractor.getClassName())) {
                        return;
                    }
                    VoiceInteractionManagerServiceStub.this.switchImplementationIfNeeded(true);
                }
            }

            public void onSomePackagesChanged() {
                int changingUserId = getChangingUserId();
                synchronized (VoiceInteractionManagerServiceStub.this) {
                    ComponentName curInteractor = VoiceInteractionManagerServiceStub.this.getCurInteractor(changingUserId);
                    ComponentName curRecognizer = VoiceInteractionManagerServiceStub.this.getCurRecognizer(changingUserId);
                    ComponentName curAssistant = VoiceInteractionManagerServiceStub.this.getCurAssistant(changingUserId);
                    if (curRecognizer == null) {
                        if (anyPackagesAppearing()) {
                            VoiceInteractionManagerServiceStub.this.initRecognizer(changingUserId);
                        }
                        return;
                    }
                    if (curInteractor != null) {
                        if (isPackageDisappearing(curInteractor.getPackageName()) == 3) {
                            VoiceInteractionManagerServiceStub.this.setCurInteractor(null, changingUserId);
                            VoiceInteractionManagerServiceStub.this.setCurRecognizer(null, changingUserId);
                            VoiceInteractionManagerServiceStub.this.resetCurAssistant(changingUserId);
                            VoiceInteractionManagerServiceStub.this.initForUser(changingUserId);
                            return;
                        }
                        if (isPackageAppearing(curInteractor.getPackageName()) != 0) {
                            VoiceInteractionManagerServiceStub.this.resetServicesIfNoRecognitionService(curInteractor, changingUserId);
                            if (VoiceInteractionManagerServiceStub.this.mImpl != null && curInteractor.getPackageName().equals(VoiceInteractionManagerServiceStub.this.mImpl.mComponent.getPackageName())) {
                                VoiceInteractionManagerServiceStub.this.switchImplementationIfNeededLocked(true);
                            }
                        }
                        return;
                    }
                    if (curAssistant != null) {
                        if (isPackageDisappearing(curAssistant.getPackageName()) == 3) {
                            VoiceInteractionManagerServiceStub.this.setCurInteractor(null, changingUserId);
                            VoiceInteractionManagerServiceStub.this.setCurRecognizer(null, changingUserId);
                            VoiceInteractionManagerServiceStub.this.resetCurAssistant(changingUserId);
                            VoiceInteractionManagerServiceStub.this.initForUser(changingUserId);
                            return;
                        }
                        if (isPackageAppearing(curAssistant.getPackageName()) != 0) {
                            VoiceInteractionManagerServiceStub.this.resetServicesIfNoRecognitionService(curAssistant, changingUserId);
                        }
                    }
                    int isPackageDisappearing = isPackageDisappearing(curRecognizer.getPackageName());
                    if (isPackageDisappearing != 3 && isPackageDisappearing != 2) {
                        if (isPackageModified(curRecognizer.getPackageName())) {
                            VoiceInteractionManagerServiceStub voiceInteractionManagerServiceStub = VoiceInteractionManagerServiceStub.this;
                            voiceInteractionManagerServiceStub.setCurRecognizer(voiceInteractionManagerServiceStub.findAvailRecognizer(curRecognizer.getPackageName(), changingUserId), changingUserId);
                        }
                    }
                    VoiceInteractionManagerServiceStub voiceInteractionManagerServiceStub2 = VoiceInteractionManagerServiceStub.this;
                    voiceInteractionManagerServiceStub2.setCurRecognizer(voiceInteractionManagerServiceStub2.findAvailRecognizer(null, changingUserId), changingUserId);
                }
            }
        }
    }
}
