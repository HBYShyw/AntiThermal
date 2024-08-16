package com.android.server.ambientcontext;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.AppGlobals;
import android.app.BroadcastOptions;
import android.app.PendingIntent;
import android.app.ambientcontext.AmbientContextEvent;
import android.app.ambientcontext.AmbientContextEventRequest;
import android.app.ambientcontext.AmbientContextManager;
import android.app.ambientcontext.IAmbientContextObserver;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.service.ambientcontext.AmbientContextDetectionResult;
import android.service.ambientcontext.AmbientContextDetectionServiceStatus;
import android.text.TextUtils;
import android.util.IndentingPrintWriter;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.infra.AbstractPerUserSystemService;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class AmbientContextManagerPerUserService extends AbstractPerUserSystemService<AmbientContextManagerPerUserService, AmbientContextManagerService> {
    private static final String TAG = "AmbientContextManagerPerUserService";

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public enum ServiceType {
        DEFAULT,
        WEARABLE
    }

    abstract void clearRemoteService();

    abstract void ensureRemoteServiceInitiated();

    abstract int getAmbientContextEventArrayExtraKeyConfig();

    abstract int getAmbientContextPackageNameExtraKeyConfig();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract ComponentName getComponentName();

    abstract int getConsentComponentConfig();

    abstract String getProtectedBindPermission();

    abstract RemoteAmbientDetectionService getRemoteService();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract ServiceType getServiceType();

    abstract void setComponentName(ComponentName componentName);

    /* JADX INFO: Access modifiers changed from: package-private */
    public AmbientContextManagerPerUserService(AmbientContextManagerService ambientContextManagerService, Object obj, int i) {
        super(ambientContextManagerService, obj, i);
    }

    public void onQueryServiceStatus(int[] iArr, String str, final RemoteCallback remoteCallback) {
        String str2 = TAG;
        Slog.d(str2, "Query event status of " + Arrays.toString(iArr) + " for " + str);
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (!setUpServiceIfNeeded()) {
                Slog.w(str2, "Detection service is not available at this moment.");
                sendStatusCallback(remoteCallback, 3);
            } else {
                ensureRemoteServiceInitiated();
                getRemoteService().queryServiceStatus(iArr, str, getServerStatusCallback(new Consumer() { // from class: com.android.server.ambientcontext.AmbientContextManagerPerUserService$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        AmbientContextManagerPerUserService.this.lambda$onQueryServiceStatus$0(remoteCallback, (Integer) obj);
                    }
                }));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onQueryServiceStatus$0(RemoteCallback remoteCallback, Integer num) {
        sendStatusCallback(remoteCallback, num.intValue());
    }

    public void onUnregisterObserver(String str) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            stopDetection(str);
            ((AmbientContextManagerService) ((AbstractPerUserSystemService) this).mMaster).clientRemoved(((AbstractPerUserSystemService) this).mUserId, str);
        }
    }

    public void onStartConsentActivity(int[] iArr, String str) {
        String str2 = TAG;
        Slog.d(str2, "Opening consent activity of " + Arrays.toString(iArr) + " for " + str);
        try {
            ParceledListSlice recentTasks = ActivityTaskManager.getService().getRecentTasks(1, 0, getUserId());
            if (recentTasks == null || recentTasks.getList().isEmpty()) {
                Slog.e(str2, "Recent task list is empty!");
                return;
            }
            ActivityManager.RecentTaskInfo recentTaskInfo = (ActivityManager.RecentTaskInfo) recentTasks.getList().get(0);
            if (!str.equals(recentTaskInfo.topActivityInfo.packageName)) {
                Slog.e(str2, "Recent task package name: " + recentTaskInfo.topActivityInfo.packageName + " doesn't match with client package name: " + str);
                return;
            }
            ComponentName consentComponent = getConsentComponent();
            if (consentComponent == null) {
                Slog.e(str2, "Consent component not found!");
                return;
            }
            Slog.d(str2, "Starting consent activity for " + str);
            Intent intent = new Intent();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                try {
                    Context context = getContext();
                    String string = context.getResources().getString(getAmbientContextPackageNameExtraKeyConfig());
                    String string2 = context.getResources().getString(getAmbientContextEventArrayExtraKeyConfig());
                    intent.setComponent(consentComponent);
                    if (string != null) {
                        intent.putExtra(string, str);
                    } else {
                        Slog.d(str2, "Missing packageNameExtraKey for consent activity");
                    }
                    if (string2 != null) {
                        intent.putExtra(string2, iArr);
                    } else {
                        Slog.d(str2, "Missing eventArrayExtraKey for consent activity");
                    }
                    ActivityOptions makeBasic = ActivityOptions.makeBasic();
                    makeBasic.setLaunchTaskId(recentTaskInfo.taskId);
                    context.startActivityAsUser(intent, makeBasic.toBundle(), context.getUser());
                } catch (ActivityNotFoundException unused) {
                    Slog.e(TAG, "unable to start consent activity");
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        } catch (RemoteException unused2) {
            Slog.e(TAG, "Failed to query recent tasks!");
        }
    }

    public void onRegisterObserver(AmbientContextEventRequest ambientContextEventRequest, String str, IAmbientContextObserver iAmbientContextObserver) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (!setUpServiceIfNeeded()) {
                Slog.w(TAG, "Detection service is not available at this moment.");
                completeRegistration(iAmbientContextObserver, 3);
            } else {
                startDetection(ambientContextEventRequest, str, iAmbientContextObserver);
                ((AmbientContextManagerService) ((AbstractPerUserSystemService) this).mMaster).newClientAdded(((AbstractPerUserSystemService) this).mUserId, ambientContextEventRequest, str, iAmbientContextObserver);
            }
        }
    }

    protected ServiceInfo newServiceInfoLocked(ComponentName componentName) throws PackageManager.NameNotFoundException {
        String str = TAG;
        Slog.d(str, "newServiceInfoLocked with component name: " + componentName.getClassName());
        if (getComponentName() == null || !componentName.getClassName().equals(getComponentName().getClassName())) {
            Slog.d(str, "service name does not match this per user, returning...");
            return null;
        }
        try {
            ServiceInfo serviceInfo = AppGlobals.getPackageManager().getServiceInfo(componentName, 0L, ((AbstractPerUserSystemService) this).mUserId);
            if (serviceInfo != null && !getProtectedBindPermission().equals(serviceInfo.permission)) {
                throw new SecurityException(String.format("Service %s requires %s permission. Found %s permission", serviceInfo.getComponentName(), getProtectedBindPermission(), serviceInfo.permission));
            }
            return serviceInfo;
        } catch (RemoteException unused) {
            throw new PackageManager.NameNotFoundException("Could not get service for " + componentName);
        }
    }

    protected void dumpLocked(String str, PrintWriter printWriter) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            super.dumpLocked(str, printWriter);
        }
        RemoteAmbientDetectionService remoteService = getRemoteService();
        if (remoteService != null) {
            remoteService.dump("", new IndentingPrintWriter(printWriter, "  "));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    public void stopDetection(String str) {
        Slog.d(TAG, "Stop detection for " + str);
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (getComponentName() != null) {
                ensureRemoteServiceInitiated();
                getRemoteService().stopDetection(str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void destroyLocked() {
        Slog.d(TAG, "Trying to cancel the remote request. Reason: Service destroyed.");
        RemoteAmbientDetectionService remoteService = getRemoteService();
        if (remoteService != null) {
            synchronized (((AbstractPerUserSystemService) this).mLock) {
                remoteService.unbind();
                clearRemoteService();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void startDetection(AmbientContextEventRequest ambientContextEventRequest, String str, final IAmbientContextObserver iAmbientContextObserver) {
        String str2 = TAG;
        Slog.d(str2, "Requested detection of " + ambientContextEventRequest.getEventTypes());
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (setUpServiceIfNeeded()) {
                ensureRemoteServiceInitiated();
                getRemoteService().startDetection(ambientContextEventRequest, str, createDetectionResultRemoteCallback(), getServerStatusCallback(new Consumer() { // from class: com.android.server.ambientcontext.AmbientContextManagerPerUserService$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        AmbientContextManagerPerUserService.this.lambda$startDetection$1(iAmbientContextObserver, (Integer) obj);
                    }
                }));
            } else {
                Slog.w(str2, "No valid component found for AmbientContextDetectionService");
                completeRegistration(iAmbientContextObserver, 2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDetection$1(IAmbientContextObserver iAmbientContextObserver, Integer num) {
        completeRegistration(iAmbientContextObserver, num.intValue());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void completeRegistration(IAmbientContextObserver iAmbientContextObserver, int i) {
        try {
            iAmbientContextObserver.onRegistrationComplete(i);
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to call IAmbientContextObserver.onRegistrationComplete: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendStatusCallback(RemoteCallback remoteCallback, @AmbientContextManager.StatusCode int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("android.app.ambientcontext.AmbientContextStatusBundleKey", i);
        remoteCallback.sendResult(bundle);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendDetectionResultIntent(PendingIntent pendingIntent, List<AmbientContextEvent> list) {
        Intent intent = new Intent();
        intent.putExtra("android.app.ambientcontext.extra.AMBIENT_CONTEXT_EVENTS", new ArrayList(list));
        BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
        makeBasic.setPendingIntentBackgroundActivityLaunchAllowed(false);
        try {
            pendingIntent.send(getContext(), 0, intent, null, null, null, makeBasic.toBundle());
            Slog.i(TAG, "Sending PendingIntent to " + pendingIntent.getCreatorPackage() + ": " + list);
        } catch (PendingIntent.CanceledException unused) {
            Slog.w(TAG, "Couldn't deliver pendingIntent:" + pendingIntent);
        }
    }

    protected RemoteCallback createDetectionResultRemoteCallback() {
        return new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: com.android.server.ambientcontext.AmbientContextManagerPerUserService$$ExternalSyntheticLambda1
            public final void onResult(Bundle bundle) {
                AmbientContextManagerPerUserService.this.lambda$createDetectionResultRemoteCallback$2(bundle);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createDetectionResultRemoteCallback$2(Bundle bundle) {
        AmbientContextDetectionResult ambientContextDetectionResult = (AmbientContextDetectionResult) bundle.get("android.app.ambientcontext.AmbientContextDetectionResultBundleKey");
        String packageName = ambientContextDetectionResult.getPackageName();
        IAmbientContextObserver clientRequestObserver = ((AmbientContextManagerService) ((AbstractPerUserSystemService) this).mMaster).getClientRequestObserver(((AbstractPerUserSystemService) this).mUserId, packageName);
        if (clientRequestObserver == null) {
            return;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                clientRequestObserver.onEvents(ambientContextDetectionResult.getEvents());
                Slog.i(TAG, "Got detection result of " + ambientContextDetectionResult.getEvents() + " for " + packageName);
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to call IAmbientContextObserver.onEvents: " + e.getMessage());
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    private boolean setUpServiceIfNeeded() {
        if (getComponentName() == null) {
            ComponentName[] updateServiceInfoListLocked = updateServiceInfoListLocked();
            if (updateServiceInfoListLocked == null || updateServiceInfoListLocked.length != 2) {
                Slog.d(TAG, "updateServiceInfoListLocked returned incorrect componentNames");
                return false;
            }
            int i = AnonymousClass1.$SwitchMap$com$android$server$ambientcontext$AmbientContextManagerPerUserService$ServiceType[getServiceType().ordinal()];
            if (i == 1) {
                setComponentName(updateServiceInfoListLocked[0]);
            } else if (i == 2) {
                setComponentName(updateServiceInfoListLocked[1]);
            } else {
                Slog.d(TAG, "updateServiceInfoListLocked returned unknown service types.");
                return false;
            }
        }
        if (getComponentName() == null) {
            return false;
        }
        try {
            return AppGlobals.getPackageManager().getServiceInfo(getComponentName(), 0L, ((AbstractPerUserSystemService) this).mUserId) != null;
        } catch (RemoteException unused) {
            Slog.w(TAG, "RemoteException while setting up service");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.ambientcontext.AmbientContextManagerPerUserService$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$server$ambientcontext$AmbientContextManagerPerUserService$ServiceType;

        static {
            int[] iArr = new int[ServiceType.values().length];
            $SwitchMap$com$android$server$ambientcontext$AmbientContextManagerPerUserService$ServiceType = iArr;
            try {
                iArr[ServiceType.DEFAULT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$server$ambientcontext$AmbientContextManagerPerUserService$ServiceType[ServiceType.WEARABLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    private RemoteCallback getServerStatusCallback(final Consumer<Integer> consumer) {
        return new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: com.android.server.ambientcontext.AmbientContextManagerPerUserService$$ExternalSyntheticLambda0
            public final void onResult(Bundle bundle) {
                AmbientContextManagerPerUserService.lambda$getServerStatusCallback$3(consumer, bundle);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getServerStatusCallback$3(Consumer consumer, Bundle bundle) {
        AmbientContextDetectionServiceStatus ambientContextDetectionServiceStatus = (AmbientContextDetectionServiceStatus) bundle.get("android.app.ambientcontext.AmbientContextServiceStatusBundleKey");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int statusCode = ambientContextDetectionServiceStatus.getStatusCode();
            consumer.accept(Integer.valueOf(statusCode));
            Slog.i(TAG, "Got detection status of " + statusCode + " for " + ambientContextDetectionServiceStatus.getPackageName());
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private ComponentName getConsentComponent() {
        String string = getContext().getResources().getString(getConsentComponentConfig());
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        Slog.i(TAG, "Consent component name: " + string);
        return ComponentName.unflattenFromString(string);
    }
}
