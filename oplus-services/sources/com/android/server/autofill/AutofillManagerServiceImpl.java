package com.android.server.autofill;

import android.R;
import android.app.ActivityManagerInternal;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.Rect;
import android.metrics.LogMaker;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.autofill.AutofillServiceInfo;
import android.service.autofill.FieldClassification;
import android.service.autofill.FillEventHistory;
import android.service.autofill.FillResponse;
import android.service.autofill.UserData;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.DebugUtils;
import android.util.LocalLog;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import android.view.autofill.IAutoFillManagerClient;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.os.IResultReceiver;
import com.android.server.LocalServices;
import com.android.server.autofill.AutofillManagerService;
import com.android.server.autofill.RemoteAugmentedAutofillService;
import com.android.server.autofill.RemoteInlineSuggestionRenderService;
import com.android.server.autofill.Session;
import com.android.server.autofill.ui.AutoFillUI;
import com.android.server.contentcapture.ContentCaptureManagerInternal;
import com.android.server.infra.AbstractMasterSystemService;
import com.android.server.infra.AbstractPerUserSystemService;
import com.android.server.inputmethod.InputMethodManagerInternal;
import com.android.server.wm.ActivityTaskManagerInternal;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AutofillManagerServiceImpl extends AbstractPerUserSystemService<AutofillManagerServiceImpl, AutofillManagerService> {
    private static final int MAX_ABANDONED_SESSION_MILLIS = 30000;
    private static final int MAX_SESSION_ID_CREATE_TRIES = 2048;
    private static final String TAG = "AutofillManagerServiceImpl";
    private static final Random sRandom = new Random();

    @GuardedBy({"mLock"})
    private FillEventHistory mAugmentedAutofillEventHistory;
    private final AutofillManagerService.AutofillCompatState mAutofillCompatState;
    private IAutofillManagerServiceImplExt mAutofillManagerServiceImplExt;

    @GuardedBy({"mLock"})
    private RemoteCallbackList<IAutoFillManagerClient> mClients;
    private final ContentCaptureManagerInternal mContentCaptureManagerInternal;
    private final AutofillManagerService.DisabledInfoCache mDisabledInfoCache;

    @GuardedBy({"mLock"})
    private FillEventHistory mEventHistory;
    private final FieldClassificationStrategy mFieldClassificationStrategy;
    private final Handler mHandler;

    @GuardedBy({"mLock"})
    private AutofillServiceInfo mInfo;
    private final InputMethodManagerInternal mInputMethodManagerInternal;
    private long mLastPrune;
    private final MetricsLogger mMetricsLogger;

    @GuardedBy({"mLock"})
    private RemoteAugmentedAutofillService mRemoteAugmentedAutofillService;

    @GuardedBy({"mLock"})
    private ServiceInfo mRemoteAugmentedAutofillServiceInfo;

    @GuardedBy({"mLock"})
    private RemoteFieldClassificationService mRemoteFieldClassificationService;

    @GuardedBy({"mLock"})
    private ServiceInfo mRemoteFieldClassificationServiceInfo;

    @GuardedBy({"mLock"})
    private RemoteInlineSuggestionRenderService mRemoteInlineSuggestionRenderService;

    @GuardedBy({"mLock"})
    private final SparseArray<Session> mSessions;
    private final AutoFillUI mUi;
    private final LocalLog mUiLatencyHistory;

    @GuardedBy({"mLock"})
    private UserData mUserData;
    private final LocalLog mWtfHistory;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutofillManagerServiceImpl(AutofillManagerService autofillManagerService, Object obj, LocalLog localLog, LocalLog localLog2, int i, AutoFillUI autoFillUI, AutofillManagerService.AutofillCompatState autofillCompatState, boolean z, AutofillManagerService.DisabledInfoCache disabledInfoCache) {
        super(autofillManagerService, obj, i);
        this.mMetricsLogger = new MetricsLogger();
        this.mHandler = new Handler(Looper.getMainLooper(), null, true);
        this.mSessions = new SparseArray<>();
        this.mLastPrune = 0L;
        this.mAutofillManagerServiceImplExt = (IAutofillManagerServiceImplExt) ExtLoader.type(IAutofillManagerServiceImplExt.class).base(this).create();
        this.mUiLatencyHistory = localLog;
        this.mWtfHistory = localLog2;
        this.mUi = autoFillUI;
        this.mFieldClassificationStrategy = new FieldClassificationStrategy(getContext(), i);
        this.mAutofillCompatState = autofillCompatState;
        this.mInputMethodManagerInternal = (InputMethodManagerInternal) LocalServices.getService(InputMethodManagerInternal.class);
        this.mContentCaptureManagerInternal = (ContentCaptureManagerInternal) LocalServices.getService(ContentCaptureManagerInternal.class);
        this.mDisabledInfoCache = disabledInfoCache;
        updateLocked(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean sendActivityAssistDataToContentCapture(IBinder iBinder, Bundle bundle) {
        ContentCaptureManagerInternal contentCaptureManagerInternal = this.mContentCaptureManagerInternal;
        if (contentCaptureManagerInternal == null) {
            return false;
        }
        contentCaptureManagerInternal.sendActivityAssistData(getUserId(), iBinder, bundle);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void onBackKeyPressed() {
        RemoteAugmentedAutofillService remoteAugmentedAutofillServiceLocked = getRemoteAugmentedAutofillServiceLocked();
        if (remoteAugmentedAutofillServiceLocked != null) {
            remoteAugmentedAutofillServiceLocked.onDestroyAutofillWindowsRequest();
        }
    }

    @GuardedBy({"mLock"})
    protected boolean updateLocked(boolean z) {
        forceRemoveAllSessionsLocked();
        boolean updateLocked = super.updateLocked(z);
        if (updateLocked) {
            if (!isEnabledLocked()) {
                for (int size = this.mSessions.size() - 1; size >= 0; size--) {
                    this.mSessions.valueAt(size).removeFromServiceLocked();
                }
            }
            sendStateToClients(false);
        }
        updateRemoteAugmentedAutofillService();
        getRemoteInlineSuggestionRenderServiceLocked();
        return updateLocked;
    }

    protected ServiceInfo newServiceInfoLocked(ComponentName componentName) throws PackageManager.NameNotFoundException {
        boolean z;
        Iterator it = getContext().getPackageManager().queryIntentServicesAsUser(new Intent("android.service.autofill.AutofillService"), 8388736, ((AbstractPerUserSystemService) this).mUserId).iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            if (((ResolveInfo) it.next()).serviceInfo.getComponentName().equals(componentName)) {
                z = true;
                break;
            }
        }
        if (!z) {
            Slog.w(TAG, "Autofill service from '" + componentName.getPackageName() + "' doesnot have intent filter android.service.autofill.AutofillService");
            throw new SecurityException("Service does not declare intent filter android.service.autofill.AutofillService");
        }
        AutofillServiceInfo autofillServiceInfo = new AutofillServiceInfo(getContext(), componentName, ((AbstractPerUserSystemService) this).mUserId);
        this.mInfo = autofillServiceInfo;
        return autofillServiceInfo.getServiceInfo();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String[] getUrlBarResourceIdsForCompatMode(String str) {
        return this.mAutofillCompatState.getUrlBarResourceIds(str, ((AbstractPerUserSystemService) this).mUserId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public int addClientLocked(IAutoFillManagerClient iAutoFillManagerClient, ComponentName componentName) {
        if (this.mClients == null) {
            this.mClients = new RemoteCallbackList<>();
        }
        this.mClients.register(iAutoFillManagerClient);
        if (isEnabledLocked()) {
            return 1;
        }
        return (componentName != null && isAugmentedAutofillServiceAvailableLocked() && isWhitelistedForAugmentedAutofillLocked(componentName)) ? 8 : 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void removeClientLocked(IAutoFillManagerClient iAutoFillManagerClient) {
        RemoteCallbackList<IAutoFillManagerClient> remoteCallbackList = this.mClients;
        if (remoteCallbackList != null) {
            remoteCallbackList.unregister(iAutoFillManagerClient);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void setAuthenticationResultLocked(Bundle bundle, int i, int i2, int i3) {
        Session session;
        if (isEnabledLocked() && (session = this.mSessions.get(i)) != null && i3 == session.uid) {
            synchronized (session.mLock) {
                session.setAuthenticationResultLocked(bundle, i2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHasCallback(int i, int i2, boolean z) {
        Session session;
        if (isEnabledLocked() && (session = this.mSessions.get(i)) != null && i2 == session.uid) {
            synchronized (((AbstractPerUserSystemService) this).mLock) {
                session.setHasCallbackLocked(z);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public long startSessionLocked(IBinder iBinder, int i, int i2, IBinder iBinder2, AutofillId autofillId, Rect rect, AutofillValue autofillValue, boolean z, ComponentName componentName, boolean z2, boolean z3, int i3) {
        boolean z4;
        boolean z5 = (i3 & 8) != 0;
        if (!isEnabledLocked() && !z5) {
            return 0L;
        }
        if (z5 || !isAutofillDisabledLocked(componentName)) {
            z4 = z5;
        } else if (isWhitelistedForAugmentedAutofillLocked(componentName)) {
            if (Helper.sDebug) {
                Slog.d(TAG, "startSession(" + componentName + "): disabled by service but whitelisted for augmented autofill");
            }
            z4 = true;
        } else {
            if (Helper.sDebug) {
                Slog.d(TAG, "startSession(" + componentName + "): ignored because disabled by service and not whitelisted for augmented autofill");
            }
            try {
                IAutoFillManagerClient.Stub.asInterface(iBinder2).setSessionFinished(4, (List) null);
            } catch (RemoteException e) {
                Slog.w(TAG, "Could not notify " + componentName + " that it's disabled: " + e);
            }
            return 2147483647L;
        }
        if (Helper.sVerbose) {
            Slog.v(TAG, "startSession(): token=" + iBinder + ", flags=" + i3 + ", forAugmentedAutofillOnly=" + z4);
        }
        pruneAbandonedSessionsLocked();
        boolean z6 = z4;
        Session createSessionByTokenLocked = createSessionByTokenLocked(iBinder, i, i2, iBinder2, z, componentName, z2, z3, z4, i3);
        if (createSessionByTokenLocked == null) {
            return 2147483647L;
        }
        AutofillServiceInfo autofillServiceInfo = this.mInfo;
        ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).logRequestLocked("id=" + createSessionByTokenLocked.id + " uid=" + i2 + " a=" + componentName.toShortString() + " s=" + (autofillServiceInfo == null ? null : autofillServiceInfo.getServiceInfo().packageName) + " u=" + ((AbstractPerUserSystemService) this).mUserId + " i=" + autofillId + " b=" + rect + " hc=" + z + " f=" + i3 + " aa=" + z6);
        synchronized (createSessionByTokenLocked.mLock) {
            createSessionByTokenLocked.updateLocked(autofillId, rect, autofillValue, 1, i3);
        }
        if (z6) {
            return createSessionByTokenLocked.id | 4294967296L;
        }
        return createSessionByTokenLocked.id;
    }

    @GuardedBy({"mLock"})
    private void pruneAbandonedSessionsLocked() {
        long currentTimeMillis = System.currentTimeMillis();
        if (this.mLastPrune < currentTimeMillis - 30000) {
            this.mLastPrune = currentTimeMillis;
            if (this.mSessions.size() > 0) {
                new PruneTask().execute(new Void[0]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void setAutofillFailureLocked(int i, int i2, List<AutofillId> list) {
        if (isEnabledLocked()) {
            Session session = this.mSessions.get(i);
            if (session == null || i2 != session.uid) {
                Slog.v(TAG, "setAutofillFailure(): no session for " + i + "(" + i2 + ")");
                return;
            }
            session.setAutofillFailureLocked(list);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void finishSessionLocked(int i, int i2, int i3) {
        if (isEnabledLocked()) {
            Session session = this.mSessions.get(i);
            if (session == null || i2 != session.uid) {
                if (Helper.sVerbose) {
                    Slog.v(TAG, "finishSessionLocked(): no session for " + i + "(" + i2 + ")");
                    return;
                }
                return;
            }
            int hookHandleCommitReason = this.mAutofillManagerServiceImplExt.hookHandleCommitReason(i3);
            if (i3 != hookHandleCommitReason) {
                ISessionExt sessionExt = session.getWrapper().getSessionExt();
                session.getWrapper().getSessionExt();
                sessionExt.hookSetOnSaveRequestReason(1);
            } else {
                ISessionExt sessionExt2 = session.getWrapper().getSessionExt();
                session.getWrapper().getSessionExt();
                sessionExt2.hookSetOnSaveRequestReason(0);
            }
            Session.SaveResult showSaveLocked = session.showSaveLocked();
            session.logContextCommitted(showSaveLocked.getNoSaveUiReason(), hookHandleCommitReason);
            if (showSaveLocked.isLogSaveShown()) {
                session.logSaveUiShown();
            }
            boolean isRemoveSession = showSaveLocked.isRemoveSession();
            if (Helper.sVerbose) {
                Slog.v(TAG, "finishSessionLocked(): session finished on save? " + isRemoveSession);
            }
            if (isRemoveSession) {
                session.removeFromServiceLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void cancelSessionLocked(int i, int i2) {
        if (isEnabledLocked()) {
            Session session = this.mSessions.get(i);
            if (session == null || i2 != session.uid) {
                Slog.w(TAG, "cancelSessionLocked(): no session for " + i + "(" + i2 + ")");
                return;
            }
            session.removeFromServiceLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void disableOwnedAutofillServicesLocked(int i) {
        Slog.i(TAG, "disableOwnedServices(" + i + "): " + this.mInfo);
        AutofillServiceInfo autofillServiceInfo = this.mInfo;
        if (autofillServiceInfo == null) {
            return;
        }
        ServiceInfo serviceInfo = autofillServiceInfo.getServiceInfo();
        if (serviceInfo.applicationInfo.uid != i) {
            Slog.w(TAG, "disableOwnedServices(): ignored when called by UID " + i + " instead of " + serviceInfo.applicationInfo.uid + " for service " + this.mInfo);
            return;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String componentNameLocked = getComponentNameLocked();
            ComponentName componentName = serviceInfo.getComponentName();
            if (componentName.equals(ComponentName.unflattenFromString(componentNameLocked))) {
                this.mMetricsLogger.action(1135, componentName.getPackageName());
                Settings.Secure.putStringForUser(getContext().getContentResolver(), "autofill_service", null, ((AbstractPerUserSystemService) this).mUserId);
                forceRemoveAllSessionsLocked();
            } else {
                Slog.w(TAG, "disableOwnedServices(): ignored because current service (" + serviceInfo + ") does not match Settings (" + componentNameLocked + ")");
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @GuardedBy({"mLock"})
    private Session createSessionByTokenLocked(IBinder iBinder, int i, int i2, IBinder iBinder2, boolean z, ComponentName componentName, boolean z2, boolean z3, boolean z4, int i3) {
        AutofillManagerServiceImpl autofillManagerServiceImpl = this;
        int i4 = 0;
        while (true) {
            i4++;
            if (i4 > 2048) {
                Slog.w(TAG, "Cannot create session in 2048 tries");
                return null;
            }
            int abs = Math.abs(sRandom.nextInt());
            if (abs != 0 && abs != Integer.MAX_VALUE && autofillManagerServiceImpl.mSessions.indexOfKey(abs) < 0) {
                autofillManagerServiceImpl.assertCallerLocked(componentName, z2);
                AutofillServiceInfo autofillServiceInfo = autofillManagerServiceImpl.mInfo;
                Session session = new Session(this, autofillManagerServiceImpl.mUi, getContext(), autofillManagerServiceImpl.mHandler, ((AbstractPerUserSystemService) autofillManagerServiceImpl).mUserId, ((AbstractPerUserSystemService) autofillManagerServiceImpl).mLock, abs, i, i2, iBinder, iBinder2, z, autofillManagerServiceImpl.mUiLatencyHistory, autofillManagerServiceImpl.mWtfHistory, autofillServiceInfo == null ? null : autofillServiceInfo.getServiceInfo().getComponentName(), componentName, z2, z3, z4, i3, autofillManagerServiceImpl.mInputMethodManagerInternal);
                this.mSessions.put(session.id, session);
                return session;
            }
            autofillManagerServiceImpl = autofillManagerServiceImpl;
        }
    }

    private void assertCallerLocked(ComponentName componentName, boolean z) {
        String str;
        String packageName = componentName.getPackageName();
        PackageManager packageManager = getContext().getPackageManager();
        int callingUid = Binder.getCallingUid();
        try {
            int packageUidAsUser = packageManager.getPackageUidAsUser(packageName, UserHandle.getCallingUserId());
            if (callingUid == packageUidAsUser || ((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).hasRunningActivity(callingUid, packageName)) {
                return;
            }
            String[] packagesForUid = packageManager.getPackagesForUid(callingUid);
            if (packagesForUid != null) {
                str = packagesForUid[0];
            } else {
                str = "uid-" + callingUid;
            }
            Slog.w(TAG, "App (package=" + str + ", UID=" + callingUid + ") passed component (" + componentName + ") owned by UID " + packageUidAsUser);
            LogMaker addTaggedData = new LogMaker(948).setPackageName(str).addTaggedData(908, getServicePackageName()).addTaggedData(949, componentName.flattenToShortString());
            if (z) {
                addTaggedData.addTaggedData(1414, 1);
            }
            this.mMetricsLogger.write(addTaggedData);
            throw new SecurityException("Invalid component: " + componentName);
        } catch (PackageManager.NameNotFoundException unused) {
            throw new SecurityException("Could not verify UID for " + componentName);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean restoreSession(int i, int i2, IBinder iBinder, IBinder iBinder2) {
        Session session = this.mSessions.get(i);
        if (session == null || i2 != session.uid) {
            return false;
        }
        session.switchActivity(iBinder, iBinder2);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean updateSessionLocked(int i, int i2, AutofillId autofillId, Rect rect, AutofillValue autofillValue, int i3, int i4) {
        Session session = this.mSessions.get(i);
        if (session != null && session.uid == i2) {
            session.updateLocked(autofillId, rect, autofillValue, i3, i4);
            return false;
        }
        if ((i4 & 1) != 0) {
            if (Helper.sDebug) {
                Slog.d(TAG, "restarting session " + i + " due to manual request on " + autofillId);
            }
            return true;
        }
        if (Helper.sVerbose) {
            Slog.v(TAG, "updateSessionLocked(): session gone for " + i + "(" + i2 + ")");
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void removeSessionLocked(int i) {
        this.mSessions.remove(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public ArrayList<Session> getPreviousSessionsLocked(Session session) {
        int size = this.mSessions.size();
        ArrayList<Session> arrayList = null;
        for (int i = 0; i < size; i++) {
            Session valueAt = this.mSessions.valueAt(i);
            if (valueAt.taskId == session.taskId && valueAt.id != session.id && (valueAt.getSaveInfoFlagsLocked() & 4) != 0) {
                if (arrayList == null) {
                    arrayList = new ArrayList<>(size);
                }
                arrayList.add(valueAt);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleSessionSave(Session session) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (this.mSessions.get(session.id) == null) {
                Slog.w(TAG, "handleSessionSave(): already gone: " + session.id);
                return;
            }
            session.callSaveLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPendingSaveUi(int i, IBinder iBinder) {
        if (Helper.sVerbose) {
            Slog.v(TAG, "onPendingSaveUi(" + i + "): " + iBinder);
        }
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            for (int size = this.mSessions.size() - 1; size >= 0; size--) {
                Session valueAt = this.mSessions.valueAt(size);
                if (valueAt.isSaveUiPendingForTokenLocked(iBinder)) {
                    valueAt.onPendingSaveUi(i, iBinder);
                    return;
                }
            }
            if (Helper.sDebug) {
                Slog.d(TAG, "No pending Save UI for token " + iBinder + " and operation " + DebugUtils.flagsToString(AutofillManager.class, "PENDING_UI_OPERATION_", i));
            }
        }
    }

    @GuardedBy({"mLock"})
    protected void handlePackageUpdateLocked(String str) {
        ServiceInfo serviceInfo = this.mFieldClassificationStrategy.getServiceInfo();
        if (serviceInfo == null || !serviceInfo.packageName.equals(str)) {
            return;
        }
        resetExtServiceLocked();
    }

    @GuardedBy({"mLock"})
    void resetExtServiceLocked() {
        if (Helper.sVerbose) {
            Slog.v(TAG, "reset autofill service in ExtServices.");
        }
        this.mFieldClassificationStrategy.reset();
        RemoteInlineSuggestionRenderService remoteInlineSuggestionRenderService = this.mRemoteInlineSuggestionRenderService;
        if (remoteInlineSuggestionRenderService != null) {
            remoteInlineSuggestionRenderService.destroy();
            this.mRemoteInlineSuggestionRenderService = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void destroyLocked() {
        if (Helper.sVerbose) {
            Slog.v(TAG, "destroyLocked()");
        }
        resetExtServiceLocked();
        int size = this.mSessions.size();
        ArraySet arraySet = new ArraySet(size);
        for (int i = 0; i < size; i++) {
            RemoteFillService destroyLocked = this.mSessions.valueAt(i).destroyLocked();
            if (destroyLocked != null) {
                arraySet.add(destroyLocked);
            }
        }
        this.mSessions.clear();
        for (int i2 = 0; i2 < arraySet.size(); i2++) {
            ((RemoteFillService) arraySet.valueAt(i2)).destroy();
        }
        sendStateToClients(true);
        RemoteCallbackList<IAutoFillManagerClient> remoteCallbackList = this.mClients;
        if (remoteCallbackList != null) {
            remoteCallbackList.kill();
            this.mClients = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLastResponse(int i, FillResponse fillResponse) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            this.mEventHistory = new FillEventHistory(i, fillResponse.getClientState());
        }
    }

    void setLastAugmentedAutofillResponse(int i) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            this.mAugmentedAutofillEventHistory = new FillEventHistory(i, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetLastResponse() {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            this.mEventHistory = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetLastAugmentedAutofillResponse() {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            this.mAugmentedAutofillEventHistory = null;
        }
    }

    @GuardedBy({"mLock"})
    private boolean isValidEventLocked(String str, int i) {
        FillEventHistory fillEventHistory = this.mEventHistory;
        if (fillEventHistory == null) {
            Slog.w(TAG, str + ": not logging event because history is null");
            return false;
        }
        if (i == fillEventHistory.getSessionId()) {
            return true;
        }
        if (Helper.sDebug) {
            Slog.d(TAG, str + ": not logging event for session " + i + " because tracked session is " + this.mEventHistory.getSessionId());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAuthenticationSelected(int i, Bundle bundle, int i2) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (isValidEventLocked("setAuthenticationSelected()", i)) {
                this.mEventHistory.addEvent(new FillEventHistory.Event(2, null, bundle, null, null, null, null, null, null, null, null, 0, i2));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logDatasetAuthenticationSelected(String str, int i, Bundle bundle, int i2) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (isValidEventLocked("logDatasetAuthenticationSelected()", i)) {
                this.mEventHistory.addEvent(new FillEventHistory.Event(1, str, bundle, null, null, null, null, null, null, null, null, 0, i2));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logSaveShown(int i, Bundle bundle) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (isValidEventLocked("logSaveShown()", i)) {
                this.mEventHistory.addEvent(new FillEventHistory.Event(3, null, bundle, null, null, null, null, null, null, null, null));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logDatasetSelected(String str, int i, Bundle bundle, int i2) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (isValidEventLocked("logDatasetSelected()", i)) {
                this.mEventHistory.addEvent(new FillEventHistory.Event(0, str, bundle, null, null, null, null, null, null, null, null, 0, i2));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logDatasetShown(int i, Bundle bundle, int i2) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (isValidEventLocked("logDatasetShown", i)) {
                this.mEventHistory.addEvent(new FillEventHistory.Event(5, null, bundle, null, null, null, null, null, null, null, null, 0, i2));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logViewEntered(int i, Bundle bundle) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (isValidEventLocked("logViewEntered", i)) {
                if (this.mEventHistory.getEvents() != null) {
                    Iterator<FillEventHistory.Event> it = this.mEventHistory.getEvents().iterator();
                    while (it.hasNext()) {
                        if (it.next().getType() == 6) {
                            Slog.v(TAG, "logViewEntered: already logged TYPE_VIEW_REQUESTED_AUTOFILL");
                            return;
                        }
                    }
                }
                this.mEventHistory.addEvent(new FillEventHistory.Event(6, null, bundle, null, null, null, null, null, null, null, null));
            }
        }
    }

    void logAugmentedAutofillAuthenticationSelected(int i, String str, Bundle bundle) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            FillEventHistory fillEventHistory = this.mAugmentedAutofillEventHistory;
            if (fillEventHistory != null && fillEventHistory.getSessionId() == i) {
                this.mAugmentedAutofillEventHistory.addEvent(new FillEventHistory.Event(1, str, bundle, null, null, null, null, null, null, null, null));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logAugmentedAutofillSelected(int i, String str, Bundle bundle) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            FillEventHistory fillEventHistory = this.mAugmentedAutofillEventHistory;
            if (fillEventHistory != null && fillEventHistory.getSessionId() == i) {
                this.mAugmentedAutofillEventHistory.addEvent(new FillEventHistory.Event(0, str, bundle, null, null, null, null, null, null, null, null));
            }
        }
    }

    void logAugmentedAutofillShown(int i, Bundle bundle) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            FillEventHistory fillEventHistory = this.mAugmentedAutofillEventHistory;
            if (fillEventHistory != null && fillEventHistory.getSessionId() == i) {
                this.mAugmentedAutofillEventHistory.addEvent(new FillEventHistory.Event(5, null, bundle, null, null, null, null, null, null, null, null, 0, 2));
            }
        }
    }

    @GuardedBy({"mLock"})
    void logContextCommittedLocked(int i, Bundle bundle, ArrayList<String> arrayList, ArraySet<String> arraySet, ArrayList<AutofillId> arrayList2, ArrayList<String> arrayList3, ArrayList<AutofillId> arrayList4, ArrayList<ArrayList<String>> arrayList5, ComponentName componentName, boolean z) {
        logContextCommittedLocked(i, bundle, arrayList, arraySet, arrayList2, arrayList3, arrayList4, arrayList5, null, null, componentName, z, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void logContextCommittedLocked(int i, Bundle bundle, ArrayList<String> arrayList, ArraySet<String> arraySet, ArrayList<AutofillId> arrayList2, ArrayList<String> arrayList3, ArrayList<AutofillId> arrayList4, ArrayList<ArrayList<String>> arrayList5, ArrayList<AutofillId> arrayList6, ArrayList<FieldClassification> arrayList7, ComponentName componentName, boolean z, int i2) {
        AutofillId[] autofillIdArr;
        FieldClassification[] fieldClassificationArr;
        if (isValidEventLocked("logDatasetNotSelected()", i)) {
            if (Helper.sVerbose) {
                Slog.v(TAG, "logContextCommitted() with FieldClassification: id=" + i + ", selectedDatasets=" + arrayList + ", ignoredDatasetIds=" + arraySet + ", changedAutofillIds=" + arrayList2 + ", changedDatasetIds=" + arrayList3 + ", manuallyFilledFieldIds=" + arrayList4 + ", detectedFieldIds=" + arrayList6 + ", detectedFieldClassifications=" + arrayList7 + ", appComponentName=" + componentName.toShortString() + ", compatMode=" + z + ", saveDialogNotShowReason=" + i2);
            }
            if (arrayList6 != null) {
                int size = arrayList6.size();
                AutofillId[] autofillIdArr2 = new AutofillId[size];
                arrayList6.toArray(autofillIdArr2);
                FieldClassification[] fieldClassificationArr2 = new FieldClassification[arrayList7.size()];
                arrayList7.toArray(fieldClassificationArr2);
                float f = 0.0f;
                int i3 = 0;
                int i4 = 0;
                while (i3 < size) {
                    List<FieldClassification.Match> matches = fieldClassificationArr2[i3].getMatches();
                    FieldClassification[] fieldClassificationArr3 = fieldClassificationArr2;
                    int size2 = matches.size();
                    i4 += size2;
                    for (int i5 = 0; i5 < size2; i5++) {
                        f += matches.get(i5).getScore();
                    }
                    i3++;
                    fieldClassificationArr2 = fieldClassificationArr3;
                }
                this.mMetricsLogger.write(Helper.newLogMaker(1273, componentName, getServicePackageName(), i, z).setCounterValue(size).addTaggedData(1274, Integer.valueOf((int) ((f * 100.0f) / i4))));
                autofillIdArr = autofillIdArr2;
                fieldClassificationArr = fieldClassificationArr2;
            } else {
                autofillIdArr = null;
                fieldClassificationArr = null;
            }
            this.mEventHistory.addEvent(new FillEventHistory.Event(4, null, bundle, arrayList, arraySet, arrayList2, arrayList3, arrayList4, arrayList5, autofillIdArr, fieldClassificationArr, i2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FillEventHistory getFillEventHistory(int i) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (this.mEventHistory != null && isCalledByServiceLocked("getFillEventHistory", i)) {
                return this.mEventHistory;
            }
            if (this.mAugmentedAutofillEventHistory == null || !isCalledByAugmentedAutofillServiceLocked("getFillEventHistory", i)) {
                return null;
            }
            return this.mAugmentedAutofillEventHistory;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserData getUserData() {
        UserData userData;
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            userData = this.mUserData;
        }
        return userData;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserData getUserData(int i) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (!isCalledByServiceLocked("getUserData", i)) {
                return null;
            }
            return this.mUserData;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setUserData(int i, UserData userData) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (isCalledByServiceLocked("setUserData", i)) {
                this.mUserData = userData;
                this.mMetricsLogger.write(new LogMaker(1272).setPackageName(getServicePackageName()).addTaggedData(914, Integer.valueOf(userData == null ? 0 : userData.getCategoryIds().length)));
            }
        }
    }

    @GuardedBy({"mLock"})
    private boolean isCalledByServiceLocked(String str, int i) {
        int serviceUidLocked = getServiceUidLocked();
        if (serviceUidLocked == i) {
            return true;
        }
        Slog.w(TAG, str + "() called by UID " + i + ", but service UID is " + serviceUidLocked);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public int getSupportedSmartSuggestionModesLocked() {
        return ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).getSupportedSmartSuggestionModesLocked();
    }

    @GuardedBy({"mLock"})
    protected void dumpLocked(String str, PrintWriter printWriter) {
        super.dumpLocked(str, printWriter);
        String str2 = str + "  ";
        printWriter.print(str);
        printWriter.print("UID: ");
        printWriter.println(getServiceUidLocked());
        printWriter.print(str);
        printWriter.print("Autofill Service Info: ");
        if (this.mInfo == null) {
            printWriter.println("N/A");
        } else {
            printWriter.println();
            this.mInfo.dump(str2, printWriter);
        }
        printWriter.print(str);
        printWriter.print("Default component: ");
        printWriter.println(getContext().getString(R.string.config_misprovisionedBrandValue));
        printWriter.println();
        printWriter.print(str);
        printWriter.println("mAugmentedAutofillName: ");
        printWriter.print(str2);
        ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).mAugmentedAutofillResolver.dumpShort(printWriter, ((AbstractPerUserSystemService) this).mUserId);
        printWriter.println();
        if (this.mRemoteAugmentedAutofillService != null) {
            printWriter.print(str);
            printWriter.println("RemoteAugmentedAutofillService: ");
            this.mRemoteAugmentedAutofillService.dump(str2, printWriter);
        }
        if (this.mRemoteAugmentedAutofillServiceInfo != null) {
            printWriter.print(str);
            printWriter.print("RemoteAugmentedAutofillServiceInfo: ");
            printWriter.println(this.mRemoteAugmentedAutofillServiceInfo);
        }
        printWriter.println();
        printWriter.print(str);
        printWriter.println("mFieldClassificationService for system detection");
        printWriter.print(str2);
        printWriter.print("Default component: ");
        printWriter.println(getContext().getString(R.string.config_networkOverLimitComponent));
        printWriter.print(str2);
        ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).mFieldClassificationResolver.dumpShort(printWriter, ((AbstractPerUserSystemService) this).mUserId);
        printWriter.println();
        if (this.mRemoteFieldClassificationService != null) {
            printWriter.print(str);
            printWriter.println("RemoteFieldClassificationService: ");
            this.mRemoteFieldClassificationService.dump(str2, printWriter);
        } else {
            printWriter.print(str);
            printWriter.println("mRemoteFieldClassificationService: null");
        }
        if (this.mRemoteFieldClassificationServiceInfo != null) {
            printWriter.print(str);
            printWriter.print("RemoteFieldClassificationServiceInfo: ");
            printWriter.println(this.mRemoteFieldClassificationServiceInfo);
        } else {
            printWriter.print(str);
            printWriter.println("mRemoteFieldClassificationServiceInfo: null");
        }
        printWriter.println();
        printWriter.print(str);
        printWriter.print("Field classification enabled: ");
        printWriter.println(isFieldClassificationEnabledLocked());
        printWriter.print(str);
        printWriter.print("Compat pkgs: ");
        ArrayMap<String, Long> compatibilityPackagesLocked = getCompatibilityPackagesLocked();
        if (compatibilityPackagesLocked == null) {
            printWriter.println("N/A");
        } else {
            printWriter.println(compatibilityPackagesLocked);
        }
        printWriter.print(str);
        printWriter.print("Inline Suggestions Enabled: ");
        printWriter.println(isInlineSuggestionsEnabledLocked());
        printWriter.print(str);
        printWriter.print("Last prune: ");
        printWriter.println(this.mLastPrune);
        this.mDisabledInfoCache.dump(((AbstractPerUserSystemService) this).mUserId, str, printWriter);
        int size = this.mSessions.size();
        if (size == 0) {
            printWriter.print(str);
            printWriter.println("No sessions");
        } else {
            printWriter.print(str);
            printWriter.print(size);
            printWriter.println(" sessions:");
            int i = 0;
            while (i < size) {
                printWriter.print(str);
                printWriter.print("#");
                int i2 = i + 1;
                printWriter.println(i2);
                this.mSessions.valueAt(i).dumpLocked(str2, printWriter);
                i = i2;
            }
        }
        printWriter.print(str);
        printWriter.print("Clients: ");
        if (this.mClients == null) {
            printWriter.println("N/A");
        } else {
            printWriter.println();
            this.mClients.dump(printWriter, str2);
        }
        FillEventHistory fillEventHistory = this.mEventHistory;
        if (fillEventHistory == null || fillEventHistory.getEvents() == null || this.mEventHistory.getEvents().size() == 0) {
            printWriter.print(str);
            printWriter.println("No event on last fill response");
        } else {
            printWriter.print(str);
            printWriter.println("Events of last fill response:");
            printWriter.print(str);
            int size2 = this.mEventHistory.getEvents().size();
            for (int i3 = 0; i3 < size2; i3++) {
                FillEventHistory.Event event = this.mEventHistory.getEvents().get(i3);
                printWriter.println("  " + i3 + ": eventType=" + event.getType() + " datasetId=" + event.getDatasetId());
            }
        }
        printWriter.print(str);
        printWriter.print("User data: ");
        if (this.mUserData == null) {
            printWriter.println("N/A");
        } else {
            printWriter.println();
            this.mUserData.dump(str2, printWriter);
        }
        printWriter.print(str);
        printWriter.println("Field Classification strategy: ");
        this.mFieldClassificationStrategy.dump(str2, printWriter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void forceRemoveAllSessionsLocked() {
        int size = this.mSessions.size();
        if (size == 0) {
            this.mUi.destroyAll(null, null, false);
            return;
        }
        for (int i = size - 1; i >= 0; i--) {
            this.mSessions.valueAt(i).forceRemoveFromServiceLocked();
        }
    }

    @GuardedBy({"mLock"})
    void forceRemoveForAugmentedOnlySessionsLocked() {
        for (int size = this.mSessions.size() - 1; size >= 0; size--) {
            this.mSessions.valueAt(size).forceRemoveFromServiceIfForAugmentedOnlyLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void forceRemoveFinishedSessionsLocked() {
        for (int size = this.mSessions.size() - 1; size >= 0; size--) {
            Session valueAt = this.mSessions.valueAt(size);
            if (valueAt.isSaveUiShowingLocked()) {
                if (Helper.sDebug) {
                    Slog.d(TAG, "destroyFinishedSessionsLocked(): " + valueAt.id);
                }
                valueAt.forceRemoveFromServiceLocked();
            } else {
                valueAt.destroyAugmentedAutofillWindowsLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void listSessionsLocked(ArrayList<String> arrayList) {
        int size = this.mSessions.size();
        if (size <= 0) {
            return;
        }
        for (int i = 0; i < size; i++) {
            int keyAt = this.mSessions.keyAt(i);
            AutofillServiceInfo autofillServiceInfo = this.mInfo;
            String flattenToShortString = autofillServiceInfo == null ? "no_svc" : autofillServiceInfo.getServiceInfo().getComponentName().flattenToShortString();
            ServiceInfo serviceInfo = this.mRemoteAugmentedAutofillServiceInfo;
            arrayList.add(String.format("%d:%s:%s", Integer.valueOf(keyAt), flattenToShortString, serviceInfo == null ? "no_aug" : serviceInfo.getComponentName().flattenToShortString()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public ArrayMap<String, Long> getCompatibilityPackagesLocked() {
        AutofillServiceInfo autofillServiceInfo = this.mInfo;
        if (autofillServiceInfo != null) {
            return autofillServiceInfo.getCompatibilityPackages();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean isInlineSuggestionsEnabledLocked() {
        AutofillServiceInfo autofillServiceInfo = this.mInfo;
        if (autofillServiceInfo != null) {
            return autofillServiceInfo.isInlineSuggestionsEnabled();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void requestSavedPasswordCount(IResultReceiver iResultReceiver) {
        new RemoteFillService(getContext(), this.mInfo.getServiceInfo().getComponentName(), ((AbstractPerUserSystemService) this).mUserId, null, ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).isInstantServiceAllowed()).onSavedPasswordCountRequest(iResultReceiver);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public RemoteAugmentedAutofillService getRemoteAugmentedAutofillServiceLocked() {
        if (this.mRemoteAugmentedAutofillService == null) {
            String serviceName = ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).mAugmentedAutofillResolver.getServiceName(((AbstractPerUserSystemService) this).mUserId);
            if (serviceName == null) {
                if (((AbstractMasterSystemService) ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster)).verbose) {
                    Slog.v(TAG, "getRemoteAugmentedAutofillServiceLocked(): not set");
                }
                return null;
            }
            int i = ((AbstractPerUserSystemService) this).mUserId;
            Pair<ServiceInfo, ComponentName> componentName = RemoteAugmentedAutofillService.getComponentName(serviceName, i, ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).mAugmentedAutofillResolver.isTemporary(i));
            if (componentName == null) {
                return null;
            }
            this.mRemoteAugmentedAutofillServiceInfo = (ServiceInfo) componentName.first;
            ComponentName componentName2 = (ComponentName) componentName.second;
            if (Helper.sVerbose) {
                Slog.v(TAG, "getRemoteAugmentedAutofillServiceLocked(): " + componentName2);
            }
            RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks remoteAugmentedAutofillServiceCallbacks = new RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks() { // from class: com.android.server.autofill.AutofillManagerServiceImpl.1
                @Override // com.android.server.autofill.RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks
                public void resetLastResponse() {
                    AutofillManagerServiceImpl.this.resetLastAugmentedAutofillResponse();
                }

                @Override // com.android.server.autofill.RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks
                public void setLastResponse(int i2) {
                    AutofillManagerServiceImpl.this.setLastAugmentedAutofillResponse(i2);
                }

                @Override // com.android.server.autofill.RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks
                public void logAugmentedAutofillShown(int i2, Bundle bundle) {
                    AutofillManagerServiceImpl.this.logAugmentedAutofillShown(i2, bundle);
                }

                @Override // com.android.server.autofill.RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks
                public void logAugmentedAutofillSelected(int i2, String str, Bundle bundle) {
                    AutofillManagerServiceImpl.this.logAugmentedAutofillSelected(i2, str, bundle);
                }

                @Override // com.android.server.autofill.RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks
                public void logAugmentedAutofillAuthenticationSelected(int i2, String str, Bundle bundle) {
                    AutofillManagerServiceImpl.this.logAugmentedAutofillAuthenticationSelected(i2, str, bundle);
                }

                public void onServiceDied(RemoteAugmentedAutofillService remoteAugmentedAutofillService) {
                    Slog.w(AutofillManagerServiceImpl.TAG, "remote augmented autofill service died");
                    RemoteAugmentedAutofillService remoteAugmentedAutofillService2 = AutofillManagerServiceImpl.this.mRemoteAugmentedAutofillService;
                    if (remoteAugmentedAutofillService2 != null) {
                        remoteAugmentedAutofillService2.unbind();
                    }
                    AutofillManagerServiceImpl.this.mRemoteAugmentedAutofillService = null;
                }
            };
            int i2 = this.mRemoteAugmentedAutofillServiceInfo.applicationInfo.uid;
            Context context = getContext();
            int i3 = ((AbstractPerUserSystemService) this).mUserId;
            boolean isInstantServiceAllowed = ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).isInstantServiceAllowed();
            AbstractMasterSystemService abstractMasterSystemService = ((AbstractPerUserSystemService) this).mMaster;
            this.mRemoteAugmentedAutofillService = new RemoteAugmentedAutofillService(context, i2, componentName2, i3, remoteAugmentedAutofillServiceCallbacks, isInstantServiceAllowed, ((AbstractMasterSystemService) ((AutofillManagerService) abstractMasterSystemService)).verbose, ((AutofillManagerService) abstractMasterSystemService).mAugmentedServiceIdleUnbindTimeoutMs, ((AutofillManagerService) abstractMasterSystemService).mAugmentedServiceRequestTimeoutMs);
        }
        return this.mRemoteAugmentedAutofillService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public RemoteAugmentedAutofillService getRemoteAugmentedAutofillServiceIfCreatedLocked() {
        return this.mRemoteAugmentedAutofillService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateRemoteAugmentedAutofillService() {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (this.mRemoteAugmentedAutofillService != null) {
                if (Helper.sVerbose) {
                    Slog.v(TAG, "updateRemoteAugmentedAutofillService(): destroying old remote service");
                }
                forceRemoveForAugmentedOnlySessionsLocked();
                this.mRemoteAugmentedAutofillService.unbind();
                this.mRemoteAugmentedAutofillService = null;
                this.mRemoteAugmentedAutofillServiceInfo = null;
                resetAugmentedAutofillWhitelistLocked();
            }
            boolean isAugmentedAutofillServiceAvailableLocked = isAugmentedAutofillServiceAvailableLocked();
            if (Helper.sVerbose) {
                Slog.v(TAG, "updateRemoteAugmentedAutofillService(): " + isAugmentedAutofillServiceAvailableLocked);
            }
            if (isAugmentedAutofillServiceAvailableLocked) {
                this.mRemoteAugmentedAutofillService = getRemoteAugmentedAutofillServiceLocked();
            }
        }
    }

    private boolean isAugmentedAutofillServiceAvailableLocked() {
        if (((AbstractMasterSystemService) ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster)).verbose) {
            Slog.v(TAG, "isAugmentedAutofillService(): setupCompleted=" + isSetupCompletedLocked() + ", disabled=" + isDisabledByUserRestrictionsLocked() + ", augmentedService=" + ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).mAugmentedAutofillResolver.getServiceName(((AbstractPerUserSystemService) this).mUserId));
        }
        return (!isSetupCompletedLocked() || isDisabledByUserRestrictionsLocked() || ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).mAugmentedAutofillResolver.getServiceName(((AbstractPerUserSystemService) this).mUserId) == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAugmentedAutofillServiceForUserLocked(int i) {
        ServiceInfo serviceInfo = this.mRemoteAugmentedAutofillServiceInfo;
        return serviceInfo != null && serviceInfo.applicationInfo.uid == i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean setAugmentedAutofillWhitelistLocked(List<String> list, List<ComponentName> list2, int i) {
        String str;
        if (!isCalledByAugmentedAutofillServiceLocked("setAugmentedAutofillWhitelistLocked", i)) {
            return false;
        }
        if (((AbstractMasterSystemService) ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster)).verbose) {
            Slog.v(TAG, "setAugmentedAutofillWhitelistLocked(packages=" + list + ", activities=" + list2 + ")");
        }
        whitelistForAugmentedAutofillPackages(list, list2);
        ServiceInfo serviceInfo = this.mRemoteAugmentedAutofillServiceInfo;
        if (serviceInfo != null) {
            str = serviceInfo.getComponentName().flattenToShortString();
        } else {
            Slog.e(TAG, "setAugmentedAutofillWhitelistLocked(): no service");
            str = "N/A";
        }
        LogMaker addTaggedData = new LogMaker(1721).addTaggedData(908, str);
        if (list != null) {
            addTaggedData.addTaggedData(1722, Integer.valueOf(list.size()));
        }
        if (list2 != null) {
            addTaggedData.addTaggedData(1723, Integer.valueOf(list2.size()));
        }
        this.mMetricsLogger.write(addTaggedData);
        return true;
    }

    @GuardedBy({"mLock"})
    private boolean isCalledByAugmentedAutofillServiceLocked(String str, int i) {
        if (getRemoteAugmentedAutofillServiceLocked() == null) {
            Slog.w(TAG, str + "() called by UID " + i + ", but there is no augmented autofill service defined for user " + getUserId());
            return false;
        }
        if (getAugmentedAutofillServiceUidLocked() == i) {
            return true;
        }
        Slog.w(TAG, str + "() called by UID " + i + ", but service UID is " + getAugmentedAutofillServiceUidLocked() + " for user " + getUserId());
        return false;
    }

    @GuardedBy({"mLock"})
    private int getAugmentedAutofillServiceUidLocked() {
        ServiceInfo serviceInfo = this.mRemoteAugmentedAutofillServiceInfo;
        if (serviceInfo != null) {
            return serviceInfo.applicationInfo.uid;
        }
        if (!((AbstractMasterSystemService) ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster)).verbose) {
            return -1;
        }
        Slog.v(TAG, "getAugmentedAutofillServiceUid(): no mRemoteAugmentedAutofillServiceInfo");
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean isWhitelistedForAugmentedAutofillLocked(ComponentName componentName) {
        return ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).mAugmentedAutofillState.isWhitelisted(((AbstractPerUserSystemService) this).mUserId, componentName);
    }

    private void whitelistForAugmentedAutofillPackages(List<String> list, List<ComponentName> list2) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (((AbstractMasterSystemService) ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster)).verbose) {
                Slog.v(TAG, "whitelisting packages: " + list + "and activities: " + list2);
            }
            ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).mAugmentedAutofillState.setWhitelist(((AbstractPerUserSystemService) this).mUserId, list, list2);
        }
    }

    @GuardedBy({"mLock"})
    void resetAugmentedAutofillWhitelistLocked() {
        if (((AbstractMasterSystemService) ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster)).verbose) {
            Slog.v(TAG, "resetting augmented autofill whitelist");
        }
        ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).mAugmentedAutofillState.resetWhitelist(((AbstractPerUserSystemService) this).mUserId);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v13 */
    /* JADX WARN: Type inference failed for: r7v14 */
    /* JADX WARN: Type inference failed for: r7v2 */
    /* JADX WARN: Type inference failed for: r7v7 */
    private void sendStateToClients(boolean z) {
        boolean z2;
        boolean isEnabledLocked;
        ?? r7;
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            RemoteCallbackList<IAutoFillManagerClient> remoteCallbackList = this.mClients;
            if (remoteCallbackList == null) {
                return;
            }
            int beginBroadcast = remoteCallbackList.beginBroadcast();
            for (int i = 0; i < beginBroadcast; i++) {
                try {
                    IAutoFillManagerClient broadcastItem = remoteCallbackList.getBroadcastItem(i);
                    try {
                        synchronized (((AbstractPerUserSystemService) this).mLock) {
                            if (!z) {
                                try {
                                    if (!isClientSessionDestroyedLocked(broadcastItem)) {
                                        z2 = false;
                                        isEnabledLocked = isEnabledLocked();
                                        r7 = isEnabledLocked;
                                    }
                                } catch (Throwable th) {
                                    throw th;
                                    break;
                                }
                            }
                            z2 = true;
                            isEnabledLocked = isEnabledLocked();
                            r7 = isEnabledLocked;
                        }
                        if (z2) {
                            r7 = (isEnabledLocked ? 1 : 0) | 2;
                        }
                        if (z) {
                            r7 = (r7 == true ? 1 : 0) | 4;
                        }
                        int i2 = r7;
                        if (Helper.sDebug) {
                            i2 = (r7 == true ? 1 : 0) | 8;
                        }
                        int i3 = i2;
                        if (Helper.sVerbose) {
                            i3 = (i2 == true ? 1 : 0) | 16;
                        }
                        broadcastItem.setState(i3);
                    } catch (RemoteException unused) {
                    }
                } finally {
                    remoteCallbackList.finishBroadcast();
                }
            }
        }
    }

    @GuardedBy({"mLock"})
    private boolean isClientSessionDestroyedLocked(IAutoFillManagerClient iAutoFillManagerClient) {
        int size = this.mSessions.size();
        for (int i = 0; i < size; i++) {
            Session valueAt = this.mSessions.valueAt(i);
            if (valueAt.getClient().equals(iAutoFillManagerClient)) {
                return valueAt.isDestroyed();
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void disableAutofillForApp(String str, long j, int i, boolean z) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            long elapsedRealtime = SystemClock.elapsedRealtime() + j;
            if (elapsedRealtime < 0) {
                elapsedRealtime = Long.MAX_VALUE;
            }
            this.mDisabledInfoCache.addDisabledAppLocked(((AbstractPerUserSystemService) this).mUserId, str, elapsedRealtime);
            this.mMetricsLogger.write(Helper.newLogMaker(1231, str, getServicePackageName(), i, z).addTaggedData(1145, Integer.valueOf(j > 2147483647L ? Integer.MAX_VALUE : (int) j)));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void disableAutofillForActivity(ComponentName componentName, long j, int i, boolean z) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            long elapsedRealtime = SystemClock.elapsedRealtime() + j;
            if (elapsedRealtime < 0) {
                elapsedRealtime = Long.MAX_VALUE;
            }
            this.mDisabledInfoCache.addDisabledActivityLocked(((AbstractPerUserSystemService) this).mUserId, componentName, elapsedRealtime);
            this.mMetricsLogger.write(Helper.newLogMaker(1232, componentName, getServicePackageName(), i, z).addTaggedData(1145, Integer.valueOf(j > 2147483647L ? Integer.MAX_VALUE : (int) j)));
        }
    }

    @GuardedBy({"mLock"})
    private boolean isAutofillDisabledLocked(ComponentName componentName) {
        return this.mDisabledInfoCache.isAutofillDisabledLocked(((AbstractPerUserSystemService) this).mUserId, componentName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFieldClassificationEnabled(int i) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (!isCalledByServiceLocked("isFieldClassificationEnabled", i)) {
                return false;
            }
            return isFieldClassificationEnabledLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFieldClassificationEnabledLocked() {
        return Settings.Secure.getIntForUser(getContext().getContentResolver(), "autofill_field_classification", 1, ((AbstractPerUserSystemService) this).mUserId) == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FieldClassificationStrategy getFieldClassificationStrategy() {
        return this.mFieldClassificationStrategy;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String[] getAvailableFieldClassificationAlgorithms(int i) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (isCalledByServiceLocked("getFCAlgorithms()", i)) {
                return this.mFieldClassificationStrategy.getAvailableAlgorithms();
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getDefaultFieldClassificationAlgorithm(int i) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (isCalledByServiceLocked("getDefaultFCAlgorithm()", i)) {
                return this.mFieldClassificationStrategy.getDefaultAlgorithm();
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteInlineSuggestionRenderService getRemoteInlineSuggestionRenderServiceLocked() {
        if (this.mRemoteInlineSuggestionRenderService == null) {
            ComponentName serviceComponentName = RemoteInlineSuggestionRenderService.getServiceComponentName(getContext(), ((AbstractPerUserSystemService) this).mUserId);
            if (serviceComponentName == null) {
                Slog.w(TAG, "No valid component found for InlineSuggestionRenderService");
                return null;
            }
            this.mRemoteInlineSuggestionRenderService = new RemoteInlineSuggestionRenderService(getContext(), serviceComponentName, "android.service.autofill.InlineSuggestionRenderService", ((AbstractPerUserSystemService) this).mUserId, new InlineSuggestionRenderCallbacksImpl(), ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).isBindInstantServiceAllowed(), ((AbstractMasterSystemService) ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster)).verbose);
        }
        return this.mRemoteInlineSuggestionRenderService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class InlineSuggestionRenderCallbacksImpl implements RemoteInlineSuggestionRenderService.InlineSuggestionRenderCallbacks {
        private InlineSuggestionRenderCallbacksImpl() {
        }

        public void onServiceDied(RemoteInlineSuggestionRenderService remoteInlineSuggestionRenderService) {
            Slog.w(AutofillManagerServiceImpl.TAG, "remote service died: " + remoteInlineSuggestionRenderService);
            AutofillManagerServiceImpl.this.mRemoteInlineSuggestionRenderService = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSwitchInputMethod() {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            int size = this.mSessions.size();
            for (int i = 0; i < size; i++) {
                this.mSessions.valueAt(i).onSwitchInputMethodLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public RemoteFieldClassificationService getRemoteFieldClassificationServiceLocked() {
        if (this.mRemoteFieldClassificationService == null) {
            String serviceName = ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).mFieldClassificationResolver.getServiceName(((AbstractPerUserSystemService) this).mUserId);
            if (serviceName == null) {
                if (((AbstractMasterSystemService) ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster)).verbose) {
                    Slog.v(TAG, "getRemoteFieldClassificationServiceLocked(): not set");
                }
                return null;
            }
            if (Helper.sVerbose) {
                Slog.v(TAG, "getRemoteFieldClassificationServiceLocked serviceName: " + serviceName);
            }
            Pair<ServiceInfo, ComponentName> componentName = RemoteFieldClassificationService.getComponentName(serviceName, ((AbstractPerUserSystemService) this).mUserId, ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).mFieldClassificationResolver.isTemporary(((AbstractPerUserSystemService) this).mUserId));
            if (componentName == null) {
                Slog.w(TAG, "RemoteFieldClassificationService.getComponentName returned null with serviceName: " + serviceName);
                return null;
            }
            this.mRemoteFieldClassificationServiceInfo = (ServiceInfo) componentName.first;
            ComponentName componentName2 = (ComponentName) componentName.second;
            if (Helper.sVerbose) {
                Slog.v(TAG, "getRemoteFieldClassificationServiceLocked(): " + componentName2);
            }
            this.mRemoteFieldClassificationService = new RemoteFieldClassificationService(getContext(), componentName2, this.mRemoteFieldClassificationServiceInfo.applicationInfo.uid, ((AbstractPerUserSystemService) this).mUserId);
        }
        return this.mRemoteFieldClassificationService;
    }

    @GuardedBy({"mLock"})
    RemoteFieldClassificationService getRemoteFieldClassificationServiceIfCreatedLocked() {
        return this.mRemoteFieldClassificationService;
    }

    public boolean isPccClassificationEnabled() {
        boolean isPccClassificationEnabledInternal = isPccClassificationEnabledInternal();
        if (Helper.sVerbose) {
            Slog.v(TAG, "pccEnabled: " + isPccClassificationEnabledInternal);
        }
        return isPccClassificationEnabledInternal;
    }

    public boolean isPccClassificationEnabledInternal() {
        boolean z;
        if (!((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).isPccClassificationFlagEnabled()) {
            return false;
        }
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            z = getRemoteFieldClassificationServiceLocked() != null;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateRemoteFieldClassificationService() {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            if (this.mRemoteFieldClassificationService != null) {
                if (Helper.sVerbose) {
                    Slog.v(TAG, "updateRemoteFieldClassificationService(): destroying old remote service");
                }
                this.mRemoteFieldClassificationService.unbind();
                this.mRemoteFieldClassificationService = null;
                this.mRemoteFieldClassificationServiceInfo = null;
            }
            boolean isFieldClassificationServiceAvailableLocked = isFieldClassificationServiceAvailableLocked();
            if (Helper.sVerbose) {
                Slog.v(TAG, "updateRemoteFieldClassificationService(): " + isFieldClassificationServiceAvailableLocked);
            }
            if (isFieldClassificationServiceAvailableLocked) {
                this.mRemoteFieldClassificationService = getRemoteFieldClassificationServiceLocked();
            }
        }
    }

    private boolean isFieldClassificationServiceAvailableLocked() {
        if (((AbstractMasterSystemService) ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster)).verbose) {
            Slog.v(TAG, "isFieldClassificationService(): setupCompleted=" + isSetupCompletedLocked() + ", disabled=" + isDisabledByUserRestrictionsLocked() + ", augmentedService=" + ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).mFieldClassificationResolver.getServiceName(((AbstractPerUserSystemService) this).mUserId));
        }
        return (!isSetupCompletedLocked() || isDisabledByUserRestrictionsLocked() || ((AutofillManagerService) ((AbstractPerUserSystemService) this).mMaster).mFieldClassificationResolver.getServiceName(((AbstractPerUserSystemService) this).mUserId) == null) ? false : true;
    }

    boolean isRemoteClassificationServiceForUserLocked(int i) {
        ServiceInfo serviceInfo = this.mRemoteFieldClassificationServiceInfo;
        return serviceInfo != null && serviceInfo.applicationInfo.uid == i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AutofillManagerServiceImpl: [userId=");
        sb.append(((AbstractPerUserSystemService) this).mUserId);
        sb.append(", component=");
        AutofillServiceInfo autofillServiceInfo = this.mInfo;
        sb.append(autofillServiceInfo != null ? autofillServiceInfo.getServiceInfo().getComponentName() : null);
        sb.append("]");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class PruneTask extends AsyncTask<Void, Void, Void> {
        private PruneTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voidArr) {
            int size;
            SparseArray sparseArray;
            int i;
            synchronized (((AbstractPerUserSystemService) AutofillManagerServiceImpl.this).mLock) {
                size = AutofillManagerServiceImpl.this.mSessions.size();
                sparseArray = new SparseArray(size);
                for (int i2 = 0; i2 < size; i2++) {
                    Session session = (Session) AutofillManagerServiceImpl.this.mSessions.valueAt(i2);
                    sparseArray.put(session.id, session.getActivityTokenLocked());
                }
            }
            ActivityTaskManagerInternal activityTaskManagerInternal = (ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class);
            int i3 = 0;
            while (i3 < size) {
                if (activityTaskManagerInternal.getActivityName((IBinder) sparseArray.valueAt(i3)) != null) {
                    sparseArray.removeAt(i3);
                    i3--;
                    size--;
                }
                i3++;
            }
            synchronized (((AbstractPerUserSystemService) AutofillManagerServiceImpl.this).mLock) {
                for (i = 0; i < size; i++) {
                    Session session2 = (Session) AutofillManagerServiceImpl.this.mSessions.get(sparseArray.keyAt(i));
                    if (session2 != null && sparseArray.valueAt(i) == session2.getActivityTokenLocked()) {
                        if (session2.isSaveUiShowingLocked()) {
                            if (Helper.sVerbose) {
                                Slog.v(AutofillManagerServiceImpl.TAG, "Session " + session2.id + " is saving");
                            }
                        } else {
                            if (Helper.sDebug) {
                                Slog.i(AutofillManagerServiceImpl.TAG, "Prune session " + session2.id + " (" + session2.getActivityTokenLocked() + ")");
                            }
                            session2.removeFromServiceLocked();
                        }
                    }
                }
            }
            return null;
        }
    }
}
