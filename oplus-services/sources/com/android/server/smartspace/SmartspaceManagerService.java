package com.android.server.smartspace;

import android.R;
import android.app.ActivityManagerInternal;
import android.app.smartspace.ISmartspaceCallback;
import android.app.smartspace.ISmartspaceManager;
import android.app.smartspace.SmartspaceConfig;
import android.app.smartspace.SmartspaceSessionId;
import android.app.smartspace.SmartspaceTargetEvent;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.util.Slog;
import com.android.server.LocalServices;
import com.android.server.infra.AbstractMasterSystemService;
import com.android.server.infra.FrameworkResourcesServiceNameResolver;
import com.android.server.wm.ActivityTaskManagerInternal;
import java.io.FileDescriptor;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SmartspaceManagerService extends AbstractMasterSystemService<SmartspaceManagerService, SmartspacePerUserService> {
    private static final boolean DEBUG = false;
    private static final int MAX_TEMP_SERVICE_DURATION_MS = 120000;
    private static final String TAG = "SmartspaceManagerService";
    private final ActivityTaskManagerInternal mActivityTaskManagerInternal;

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected int getMaximumTemporaryServiceDurationMs() {
        return MAX_TEMP_SERVICE_DURATION_MS;
    }

    public SmartspaceManagerService(Context context) {
        super(context, new FrameworkResourcesServiceNameResolver(context, R.string.config_recentsComponentName), null, 17);
        this.mActivityTaskManagerInternal = (ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public SmartspacePerUserService newServiceLocked(int i, boolean z) {
        return new SmartspacePerUserService(this, this.mLock, i);
    }

    public void onStart() {
        publishBinderService("smartspace", new SmartspaceManagerStub());
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.MANAGE_SMARTSPACE", TAG);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageUpdatedLocked(int i) {
        SmartspacePerUserService peekServiceForUserLocked = peekServiceForUserLocked(i);
        if (peekServiceForUserLocked != null) {
            peekServiceForUserLocked.onPackageUpdatedLocked();
        }
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageRestartedLocked(int i) {
        SmartspacePerUserService peekServiceForUserLocked = peekServiceForUserLocked(i);
        if (peekServiceForUserLocked != null) {
            peekServiceForUserLocked.onPackageRestartedLocked();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class SmartspaceManagerStub extends ISmartspaceManager.Stub {
        private SmartspaceManagerStub() {
        }

        public void createSmartspaceSession(final SmartspaceConfig smartspaceConfig, final SmartspaceSessionId smartspaceSessionId, final IBinder iBinder) {
            runForUserLocked("createSmartspaceSession", smartspaceSessionId, new Consumer() { // from class: com.android.server.smartspace.SmartspaceManagerService$SmartspaceManagerStub$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((SmartspacePerUserService) obj).onCreateSmartspaceSessionLocked(smartspaceConfig, smartspaceSessionId, iBinder);
                }
            });
        }

        public void notifySmartspaceEvent(final SmartspaceSessionId smartspaceSessionId, final SmartspaceTargetEvent smartspaceTargetEvent) {
            runForUserLocked("notifySmartspaceEvent", smartspaceSessionId, new Consumer() { // from class: com.android.server.smartspace.SmartspaceManagerService$SmartspaceManagerStub$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((SmartspacePerUserService) obj).notifySmartspaceEventLocked(smartspaceSessionId, smartspaceTargetEvent);
                }
            });
        }

        public void requestSmartspaceUpdate(final SmartspaceSessionId smartspaceSessionId) {
            runForUserLocked("requestSmartspaceUpdate", smartspaceSessionId, new Consumer() { // from class: com.android.server.smartspace.SmartspaceManagerService$SmartspaceManagerStub$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((SmartspacePerUserService) obj).requestSmartspaceUpdateLocked(smartspaceSessionId);
                }
            });
        }

        public void registerSmartspaceUpdates(final SmartspaceSessionId smartspaceSessionId, final ISmartspaceCallback iSmartspaceCallback) {
            runForUserLocked("registerSmartspaceUpdates", smartspaceSessionId, new Consumer() { // from class: com.android.server.smartspace.SmartspaceManagerService$SmartspaceManagerStub$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((SmartspacePerUserService) obj).registerSmartspaceUpdatesLocked(smartspaceSessionId, iSmartspaceCallback);
                }
            });
        }

        public void unregisterSmartspaceUpdates(final SmartspaceSessionId smartspaceSessionId, final ISmartspaceCallback iSmartspaceCallback) {
            runForUserLocked("unregisterSmartspaceUpdates", smartspaceSessionId, new Consumer() { // from class: com.android.server.smartspace.SmartspaceManagerService$SmartspaceManagerStub$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((SmartspacePerUserService) obj).unregisterSmartspaceUpdatesLocked(smartspaceSessionId, iSmartspaceCallback);
                }
            });
        }

        public void destroySmartspaceSession(final SmartspaceSessionId smartspaceSessionId) {
            runForUserLocked("destroySmartspaceSession", smartspaceSessionId, new Consumer() { // from class: com.android.server.smartspace.SmartspaceManagerService$SmartspaceManagerStub$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((SmartspacePerUserService) obj).onDestroyLocked(smartspaceSessionId);
                }
            });
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
            new SmartspaceManagerServiceShellCommand(SmartspaceManagerService.this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }

        private void runForUserLocked(String str, SmartspaceSessionId smartspaceSessionId, Consumer<SmartspacePerUserService> consumer) {
            int handleIncomingUser = ((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), smartspaceSessionId.getUserHandle().getIdentifier(), false, 0, (String) null, (String) null);
            if (SmartspaceManagerService.this.getContext().checkCallingPermission("android.permission.MANAGE_SMARTSPACE") != 0 && !((AbstractMasterSystemService) SmartspaceManagerService.this).mServiceNameResolver.isTemporary(handleIncomingUser) && !SmartspaceManagerService.this.mActivityTaskManagerInternal.isCallerRecents(Binder.getCallingUid())) {
                String str2 = "Permission Denial: Cannot call " + str + " from pid=" + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid();
                Slog.w(SmartspaceManagerService.TAG, str2);
                throw new SecurityException(str2);
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (((AbstractMasterSystemService) SmartspaceManagerService.this).mLock) {
                    consumer.accept((SmartspacePerUserService) SmartspaceManagerService.this.getServiceForUserLocked(handleIncomingUser));
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }
}
