package com.android.server.translation;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.service.translation.TranslationServiceInfo;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Slog;
import android.view.autofill.AutofillId;
import android.view.inputmethod.InputMethodInfo;
import android.view.translation.ITranslationServiceCallback;
import android.view.translation.TranslationCapability;
import android.view.translation.TranslationContext;
import android.view.translation.TranslationSpec;
import android.view.translation.UiTranslationSpec;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.IResultReceiver;
import com.android.internal.os.TransferPipe;
import com.android.server.LocalServices;
import com.android.server.infra.AbstractPerUserSystemService;
import com.android.server.inputmethod.InputMethodManagerInternal;
import com.android.server.wm.ActivityTaskManagerInternal;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class TranslationManagerServiceImpl extends AbstractPerUserSystemService<TranslationManagerServiceImpl, TranslationManagerService> implements IBinder.DeathRecipient {

    @GuardedBy({"mLock"})
    private final ArrayMap<IBinder, ActiveTranslation> mActiveTranslations;
    private final ActivityTaskManagerInternal mActivityTaskManagerInternal;
    private final RemoteCallbackList<IRemoteCallback> mCallbacks;

    @GuardedBy({"mLock"})
    private WeakReference<ActivityTaskManagerInternal.ActivityTokens> mLastActivityTokens;
    private final TranslationServiceRemoteCallback mRemoteServiceCallback;

    @GuardedBy({"mLock"})
    private RemoteTranslationService mRemoteTranslationService;

    @GuardedBy({"mLock"})
    private ServiceInfo mRemoteTranslationServiceInfo;
    private final RemoteCallbackList<IRemoteCallback> mTranslationCapabilityCallbacks;

    @GuardedBy({"mLock"})
    private TranslationServiceInfo mTranslationServiceInfo;
    private final ArraySet<IBinder> mWaitingFinishedCallbackActivities;
    private static final String TAG = "TranslationManagerServiceImpl";

    @SuppressLint({"IsLoggableTagLength"})
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public TranslationManagerServiceImpl(TranslationManagerService translationManagerService, Object obj, int i, boolean z) {
        super(translationManagerService, obj, i);
        this.mRemoteServiceCallback = new TranslationServiceRemoteCallback();
        this.mTranslationCapabilityCallbacks = new RemoteCallbackList<>();
        this.mWaitingFinishedCallbackActivities = new ArraySet<>();
        this.mActiveTranslations = new ArrayMap<>();
        this.mCallbacks = new RemoteCallbackList<>();
        updateRemoteServiceLocked();
        this.mActivityTaskManagerInternal = (ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class);
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    @GuardedBy({"mLock"})
    protected ServiceInfo newServiceInfoLocked(ComponentName componentName) throws PackageManager.NameNotFoundException {
        TranslationServiceInfo translationServiceInfo = new TranslationServiceInfo(getContext(), componentName, isTemporaryServiceSetLocked(), this.mUserId);
        this.mTranslationServiceInfo = translationServiceInfo;
        this.mRemoteTranslationServiceInfo = translationServiceInfo.getServiceInfo();
        return this.mTranslationServiceInfo.getServiceInfo();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractPerUserSystemService
    @GuardedBy({"mLock"})
    public boolean updateLocked(boolean z) {
        boolean updateLocked = super.updateLocked(z);
        updateRemoteServiceLocked();
        return updateLocked;
    }

    @GuardedBy({"mLock"})
    private void updateRemoteServiceLocked() {
        if (this.mRemoteTranslationService != null) {
            if (((TranslationManagerService) this.mMaster).debug) {
                Slog.d(TAG, "updateRemoteService(): destroying old remote service");
            }
            this.mRemoteTranslationService.unbind();
            this.mRemoteTranslationService = null;
        }
    }

    @GuardedBy({"mLock"})
    private RemoteTranslationService ensureRemoteServiceLocked() {
        if (this.mRemoteTranslationService == null) {
            String componentNameLocked = getComponentNameLocked();
            if (componentNameLocked == null) {
                if (((TranslationManagerService) this.mMaster).verbose) {
                    Slog.v(TAG, "ensureRemoteServiceLocked(): no service component name.");
                }
                return null;
            }
            ComponentName unflattenFromString = ComponentName.unflattenFromString(componentNameLocked);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                boolean isServiceAvailableForUser = isServiceAvailableForUser(unflattenFromString);
                if (((TranslationManagerService) this.mMaster).verbose) {
                    Slog.v(TAG, "ensureRemoteServiceLocked(): isServiceAvailableForUser=" + isServiceAvailableForUser);
                }
                if (!isServiceAvailableForUser) {
                    Slog.w(TAG, "ensureRemoteServiceLocked(): " + unflattenFromString + " is not available,");
                    return null;
                }
                this.mRemoteTranslationService = new RemoteTranslationService(getContext(), unflattenFromString, this.mUserId, false, this.mRemoteServiceCallback);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        return this.mRemoteTranslationService;
    }

    private boolean isServiceAvailableForUser(ComponentName componentName) {
        ResolveInfo resolveServiceAsUser = getContext().getPackageManager().resolveServiceAsUser(new Intent("android.service.translation.TranslationService").setComponent(componentName), 132, this.mUserId);
        return (resolveServiceAsUser == null || resolveServiceAsUser.serviceInfo == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void onTranslationCapabilitiesRequestLocked(int i, int i2, ResultReceiver resultReceiver) {
        RemoteTranslationService ensureRemoteServiceLocked = ensureRemoteServiceLocked();
        if (ensureRemoteServiceLocked != null) {
            ensureRemoteServiceLocked.onTranslationCapabilitiesRequest(i, i2, resultReceiver);
        } else {
            Slog.v(TAG, "onTranslationCapabilitiesRequestLocked(): no remote service.");
            resultReceiver.send(2, null);
        }
    }

    public void registerTranslationCapabilityCallback(IRemoteCallback iRemoteCallback, int i) {
        this.mTranslationCapabilityCallbacks.register(iRemoteCallback, Integer.valueOf(i));
        ensureRemoteServiceLocked();
    }

    public void unregisterTranslationCapabilityCallback(IRemoteCallback iRemoteCallback) {
        this.mTranslationCapabilityCallbacks.unregister(iRemoteCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void onSessionCreatedLocked(TranslationContext translationContext, int i, IResultReceiver iResultReceiver) throws RemoteException {
        RemoteTranslationService ensureRemoteServiceLocked = ensureRemoteServiceLocked();
        if (ensureRemoteServiceLocked != null) {
            ensureRemoteServiceLocked.onSessionCreated(translationContext, i, iResultReceiver);
        } else {
            Slog.v(TAG, "onSessionCreatedLocked(): no remote service.");
            iResultReceiver.send(2, (Bundle) null);
        }
    }

    private int getAppUidByComponentName(Context context, ComponentName componentName, int i) {
        if (componentName == null) {
            return -1;
        }
        try {
            return context.getPackageManager().getApplicationInfoAsUser(componentName.getPackageName(), 0, i).uid;
        } catch (PackageManager.NameNotFoundException unused) {
            Slog.d(TAG, "Cannot find packageManager for" + componentName);
            return -1;
        }
    }

    @GuardedBy({"mLock"})
    public void onTranslationFinishedLocked(boolean z, IBinder iBinder, ComponentName componentName) {
        int appUidByComponentName = getAppUidByComponentName(getContext(), componentName, getUserId());
        String packageName = componentName.getPackageName();
        if (z || this.mWaitingFinishedCallbackActivities.contains(iBinder)) {
            invokeCallbacks(3, null, null, packageName, appUidByComponentName);
            this.mWaitingFinishedCallbackActivities.remove(iBinder);
            this.mActiveTranslations.remove(iBinder);
        }
    }

    @GuardedBy({"mLock"})
    public void updateUiTranslationStateLocked(int i, TranslationSpec translationSpec, TranslationSpec translationSpec2, List<AutofillId> list, IBinder iBinder, int i2, UiTranslationSpec uiTranslationSpec) {
        ActivityTaskManagerInternal.ActivityTokens attachedNonFinishingActivityForTask = this.mActivityTaskManagerInternal.getAttachedNonFinishingActivityForTask(i2, iBinder);
        if (attachedNonFinishingActivityForTask == null) {
            Slog.w(TAG, "Unknown activity or it was finished to query for update translation state for token=" + iBinder + " taskId=" + i2 + " for state= " + i);
            return;
        }
        this.mLastActivityTokens = new WeakReference<>(attachedNonFinishingActivityForTask);
        if (i == 3) {
            this.mWaitingFinishedCallbackActivities.add(iBinder);
        }
        IBinder activityToken = attachedNonFinishingActivityForTask.getActivityToken();
        try {
            attachedNonFinishingActivityForTask.getApplicationThread().updateUiTranslationState(activityToken, i, translationSpec, translationSpec2, list, uiTranslationSpec);
        } catch (RemoteException e) {
            Slog.w(TAG, "Update UiTranslationState fail: " + e);
        }
        ComponentName activityName = this.mActivityTaskManagerInternal.getActivityName(activityToken);
        int appUidByComponentName = getAppUidByComponentName(getContext(), activityName, getUserId());
        String packageName = activityName.getPackageName();
        invokeCallbacksIfNecessaryLocked(i, translationSpec, translationSpec2, packageName, iBinder, appUidByComponentName);
        updateActiveTranslationsLocked(i, translationSpec, translationSpec2, packageName, iBinder, appUidByComponentName);
    }

    @GuardedBy({"mLock"})
    private void updateActiveTranslationsLocked(int i, TranslationSpec translationSpec, TranslationSpec translationSpec2, String str, IBinder iBinder, int i2) {
        ActiveTranslation activeTranslation = this.mActiveTranslations.get(iBinder);
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (i == 3 && activeTranslation != null) {
                        this.mActiveTranslations.remove(iBinder);
                    }
                } else if (activeTranslation != null) {
                    activeTranslation.isPaused = false;
                }
            } else if (activeTranslation != null) {
                activeTranslation.isPaused = true;
            }
        } else if (activeTranslation == null) {
            try {
                iBinder.linkToDeath(this, 0);
                this.mActiveTranslations.put(iBinder, new ActiveTranslation(translationSpec, translationSpec2, i2, str));
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to call linkToDeath for translated app with uid=" + i2 + "; activity is already dead", e);
                invokeCallbacks(3, translationSpec, translationSpec2, str, i2);
                return;
            }
        }
        if (DEBUG) {
            Slog.d(TAG, "Updating to translation state=" + i + " for app with uid=" + i2 + " packageName=" + str);
        }
    }

    @GuardedBy({"mLock"})
    private void invokeCallbacksIfNecessaryLocked(int i, TranslationSpec translationSpec, TranslationSpec translationSpec2, String str, IBinder iBinder, int i2) {
        int i3;
        ActiveTranslation activeTranslation = this.mActiveTranslations.get(iBinder);
        boolean z = false;
        if (activeTranslation == null) {
            if (i != 0) {
                Slog.w(TAG, "Updating to translation state=" + i + " for app with uid=" + i2 + " packageName=" + str + " but no active translation was found for it");
                i3 = i;
            }
            i3 = i;
            z = true;
        } else if (i == 0) {
            if (activeTranslation.sourceSpec.getLocale().equals(translationSpec.getLocale()) && activeTranslation.targetSpec.getLocale().equals(translationSpec2.getLocale())) {
                if (activeTranslation.isPaused) {
                    z = true;
                    i3 = 2;
                }
                i3 = i;
            }
            i3 = i;
            z = true;
        } else if (i == 1) {
            i3 = i;
            z = true;
        } else {
            i3 = i;
            z = true;
        }
        if (z) {
            invokeCallbacks(i3, translationSpec, translationSpec2, str, i2);
        }
    }

    @GuardedBy({"mLock"})
    public void dumpLocked(String str, FileDescriptor fileDescriptor, PrintWriter printWriter) {
        WeakReference<ActivityTaskManagerInternal.ActivityTokens> weakReference = this.mLastActivityTokens;
        if (weakReference != null) {
            ActivityTaskManagerInternal.ActivityTokens activityTokens = weakReference.get();
            if (activityTokens == null) {
                return;
            }
            try {
                TransferPipe transferPipe = new TransferPipe();
                try {
                    activityTokens.getApplicationThread().dumpActivity(transferPipe.getWriteFd(), activityTokens.getActivityToken(), str, new String[]{"--dump-dumpable", "UiTranslationController"});
                    transferPipe.go(fileDescriptor);
                    transferPipe.close();
                } finally {
                }
            } catch (RemoteException unused) {
                printWriter.println(str + "Got a RemoteException while dumping the activity");
            } catch (IOException e) {
                printWriter.println(str + "Failure while dumping the activity: " + e);
            }
        } else {
            printWriter.print(str);
            printWriter.println("No requested UiTranslation Activity.");
        }
        int size = this.mWaitingFinishedCallbackActivities.size();
        if (size > 0) {
            printWriter.print(str);
            printWriter.print("number waiting finish callback activities: ");
            printWriter.println(size);
            Iterator<IBinder> it = this.mWaitingFinishedCallbackActivities.iterator();
            while (it.hasNext()) {
                IBinder next = it.next();
                printWriter.print(str);
                printWriter.print("shareableActivityToken: ");
                printWriter.println(next);
            }
        }
    }

    private void invokeCallbacks(int i, TranslationSpec translationSpec, TranslationSpec translationSpec2, String str, final int i2) {
        final Bundle createResultForCallback = createResultForCallback(i, translationSpec, translationSpec2, str);
        int registeredCallbackCount = this.mCallbacks.getRegisteredCallbackCount();
        if (DEBUG) {
            Slog.d(TAG, "Invoking " + registeredCallbackCount + " callbacks for translation state=" + i + " for app with uid=" + i2 + " packageName=" + str);
        }
        if (registeredCallbackCount == 0) {
            return;
        }
        final List<InputMethodInfo> enabledInputMethods = getEnabledInputMethods();
        this.mCallbacks.broadcast(new BiConsumer() { // from class: com.android.server.translation.TranslationManagerServiceImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                TranslationManagerServiceImpl.this.lambda$invokeCallbacks$0(i2, createResultForCallback, enabledInputMethods, (IRemoteCallback) obj, obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$invokeCallbacks$0(int i, Bundle bundle, List list, IRemoteCallback iRemoteCallback, Object obj) {
        invokeCallback(((Integer) obj).intValue(), i, iRemoteCallback, bundle, list);
    }

    private List<InputMethodInfo> getEnabledInputMethods() {
        return ((InputMethodManagerInternal) LocalServices.getService(InputMethodManagerInternal.class)).getEnabledInputMethodListAsUser(this.mUserId);
    }

    private Bundle createResultForCallback(int i, TranslationSpec translationSpec, TranslationSpec translationSpec2, String str) {
        Bundle bundle = new Bundle();
        bundle.putInt("state", i);
        if (translationSpec != null) {
            bundle.putSerializable("source_locale", translationSpec.getLocale());
            bundle.putSerializable("target_locale", translationSpec2.getLocale());
        }
        bundle.putString("package_name", str);
        return bundle;
    }

    private void invokeCallback(int i, int i2, IRemoteCallback iRemoteCallback, Bundle bundle, List<InputMethodInfo> list) {
        boolean z;
        if (i == i2) {
            try {
                iRemoteCallback.sendResult(bundle);
                return;
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to invoke UiTranslationStateCallback: " + e);
                return;
            }
        }
        Iterator<InputMethodInfo> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            } else if (i == it.next().getServiceInfo().applicationInfo.uid) {
                z = true;
                break;
            }
        }
        if (z) {
            try {
                iRemoteCallback.sendResult(bundle);
            } catch (RemoteException e2) {
                Slog.w(TAG, "Failed to invoke UiTranslationStateCallback: " + e2);
            }
        }
    }

    @GuardedBy({"mLock"})
    public void registerUiTranslationStateCallbackLocked(IRemoteCallback iRemoteCallback, int i) {
        this.mCallbacks.register(iRemoteCallback, Integer.valueOf(i));
        int size = this.mActiveTranslations.size();
        Slog.i(TAG, "New registered callback for sourceUid=" + i + " with currently " + size + " active translations");
        if (size == 0) {
            return;
        }
        List<InputMethodInfo> enabledInputMethods = getEnabledInputMethods();
        for (int i2 = 0; i2 < this.mActiveTranslations.size(); i2++) {
            ActiveTranslation valueAt = this.mActiveTranslations.valueAt(i2);
            int i3 = valueAt.translatedAppUid;
            String str = valueAt.packageName;
            if (DEBUG) {
                Slog.d(TAG, "Triggering callback for sourceUid=" + i + " for translated app with uid=" + i3 + "packageName=" + str + " isPaused=" + valueAt.isPaused);
            }
            invokeCallback(i, i3, iRemoteCallback, createResultForCallback(0, valueAt.sourceSpec, valueAt.targetSpec, str), enabledInputMethods);
            if (valueAt.isPaused) {
                invokeCallback(i, i3, iRemoteCallback, createResultForCallback(1, valueAt.sourceSpec, valueAt.targetSpec, str), enabledInputMethods);
            }
        }
    }

    public void unregisterUiTranslationStateCallback(IRemoteCallback iRemoteCallback) {
        this.mCallbacks.unregister(iRemoteCallback);
    }

    public ComponentName getServiceSettingsActivityLocked() {
        String settingsActivity;
        TranslationServiceInfo translationServiceInfo = this.mTranslationServiceInfo;
        if (translationServiceInfo == null || (settingsActivity = translationServiceInfo.getSettingsActivity()) == null) {
            return null;
        }
        return new ComponentName(this.mTranslationServiceInfo.getServiceInfo().packageName, settingsActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyClientsTranslationCapability(TranslationCapability translationCapability) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable("translation_capabilities", translationCapability);
        this.mTranslationCapabilityCallbacks.broadcast(new BiConsumer() { // from class: com.android.server.translation.TranslationManagerServiceImpl$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                TranslationManagerServiceImpl.lambda$notifyClientsTranslationCapability$1(bundle, (IRemoteCallback) obj, obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$notifyClientsTranslationCapability$1(Bundle bundle, IRemoteCallback iRemoteCallback, Object obj) {
        try {
            iRemoteCallback.sendResult(bundle);
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to invoke UiTranslationStateCallback: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class TranslationServiceRemoteCallback extends ITranslationServiceCallback.Stub {
        private TranslationServiceRemoteCallback() {
        }

        public void updateTranslationCapability(TranslationCapability translationCapability) {
            if (translationCapability == null) {
                Slog.wtf(TranslationManagerServiceImpl.TAG, "received a null TranslationCapability from TranslationService.");
            } else {
                TranslationManagerServiceImpl.this.notifyClientsTranslationCapability(translationCapability);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ActiveTranslation {
        public boolean isPaused;
        public final String packageName;
        public final TranslationSpec sourceSpec;
        public final TranslationSpec targetSpec;
        public final int translatedAppUid;

        private ActiveTranslation(TranslationSpec translationSpec, TranslationSpec translationSpec2, int i, String str) {
            this.isPaused = false;
            this.sourceSpec = translationSpec;
            this.targetSpec = translationSpec2;
            this.translatedAppUid = i;
            this.packageName = str;
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied(IBinder iBinder) {
        synchronized (this.mLock) {
            this.mWaitingFinishedCallbackActivities.remove(iBinder);
            ActiveTranslation remove = this.mActiveTranslations.remove(iBinder);
            if (remove != null) {
                invokeCallbacks(3, remove.sourceSpec, remove.targetSpec, remove.packageName, remove.translatedAppUid);
            }
        }
    }
}
