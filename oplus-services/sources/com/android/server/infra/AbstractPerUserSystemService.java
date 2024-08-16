package com.android.server.infra;

import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.server.LocalServices;
import com.android.server.infra.AbstractMasterSystemService;
import com.android.server.infra.AbstractPerUserSystemService;
import com.android.server.pm.UserManagerInternal;
import java.io.PrintWriter;
import java.util.HashMap;
import oplus.util.IOplusStatisticsExt;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class AbstractPerUserSystemService<S extends AbstractPerUserSystemService<S, M>, M extends AbstractMasterSystemService<M, S>> {

    @GuardedBy({"mLock"})
    private boolean mDisabled;
    public final Object mLock;
    protected final M mMaster;

    @GuardedBy({"mLock"})
    private ServiceInfo mServiceInfo;

    @GuardedBy({"mLock"})
    private boolean mSetupComplete;
    protected final String mTag = getClass().getSimpleName();
    protected final int mUserId;

    /* JADX INFO: Access modifiers changed from: protected */
    public void handlePackageUpdateLocked(String str) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractPerUserSystemService(M m, Object obj, int i) {
        this.mMaster = m;
        this.mLock = obj;
        this.mUserId = i;
        updateIsSetupComplete(i);
    }

    private void updateIsSetupComplete(int i) {
        this.mSetupComplete = "1".equals(Settings.Secure.getStringForUser(getContext().getContentResolver(), "user_setup_complete", i));
    }

    protected ServiceInfo newServiceInfoLocked(ComponentName componentName) throws PackageManager.NameNotFoundException {
        throw new UnsupportedOperationException("not overridden");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @GuardedBy({"mLock"})
    public boolean isEnabledLocked() {
        return (!this.mSetupComplete || this.mServiceInfo == null || this.mDisabled) ? false : true;
    }

    protected final boolean isDisabledByUserRestrictionsLocked() {
        return this.mDisabled;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @GuardedBy({"mLock"})
    public boolean updateLocked(boolean z) {
        boolean isEnabledLocked = isEnabledLocked();
        if (this.mMaster.verbose) {
            Slog.v(this.mTag, "updateLocked(u=" + this.mUserId + "): wasEnabled=" + isEnabledLocked + ", mSetupComplete=" + this.mSetupComplete + ", disabled=" + z + ", mDisabled=" + this.mDisabled);
        }
        updateIsSetupComplete(this.mUserId);
        this.mDisabled = z;
        ServiceNameResolver serviceNameResolver = this.mMaster.mServiceNameResolver;
        if (serviceNameResolver != null && serviceNameResolver.isConfiguredInMultipleMode()) {
            if (this.mMaster.debug) {
                Slog.d(this.mTag, "Should not end up in updateLocked when isConfiguredInMultipleMode is true");
            }
        } else {
            updateServiceInfoLocked();
        }
        return isEnabledLocked != isEnabledLocked();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @GuardedBy({"mLock"})
    public final ComponentName updateServiceInfoLocked() {
        ComponentName[] updateServiceInfoListLocked = updateServiceInfoListLocked();
        if (updateServiceInfoListLocked == null || updateServiceInfoListLocked.length == 0) {
            return null;
        }
        return updateServiceInfoListLocked[0];
    }

    @GuardedBy({"mLock"})
    protected final ComponentName[] updateServiceInfoListLocked() {
        ServiceNameResolver serviceNameResolver = this.mMaster.mServiceNameResolver;
        if (serviceNameResolver == null) {
            return null;
        }
        if (!serviceNameResolver.isConfiguredInMultipleMode()) {
            return new ComponentName[]{getServiceComponent(getComponentNameLocked())};
        }
        String[] serviceNameList = this.mMaster.mServiceNameResolver.getServiceNameList(this.mUserId);
        if (serviceNameList == null) {
            return null;
        }
        ComponentName[] componentNameArr = new ComponentName[serviceNameList.length];
        for (int i = 0; i < serviceNameList.length; i++) {
            componentNameArr[i] = getServiceComponent(serviceNameList[i]);
        }
        return componentNameArr;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0067 A[Catch: Exception -> 0x00c7, all -> 0x00ea, TRY_ENTER, TryCatch #2 {Exception -> 0x00c7, blocks: (B:10:0x0067, B:12:0x0073, B:19:0x009e, B:21:0x00a6), top: B:8:0x0065, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x009e A[Catch: Exception -> 0x00c7, all -> 0x00ea, TryCatch #2 {Exception -> 0x00c7, blocks: (B:10:0x0067, B:12:0x0073, B:19:0x009e, B:21:0x00a6), top: B:8:0x0065, outer: #3 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private ComponentName getServiceComponent(String str) {
        ComponentName componentName;
        ServiceInfo serviceInfo;
        synchronized (this.mLock) {
            if (TextUtils.isEmpty(str)) {
                componentName = null;
                serviceInfo = null;
            } else {
                try {
                    componentName = ComponentName.unflattenFromString(str);
                } catch (RemoteException | RuntimeException e) {
                    e = e;
                    componentName = null;
                }
                try {
                    serviceInfo = AppGlobals.getPackageManager().getServiceInfo(componentName, 0L, this.mUserId);
                    if (serviceInfo == null) {
                        Slog.e(this.mTag, "Bad service name: " + str);
                        updateService(componentName);
                    } else {
                        M m = this.mMaster;
                        m.mMaxTime = 3;
                        m.mUpdated = false;
                    }
                } catch (RemoteException | RuntimeException e2) {
                    e = e2;
                    Slog.e(this.mTag, "Error getting service info for '" + str + "': " + e);
                    serviceInfo = null;
                    if (serviceInfo == null) {
                    }
                    return componentName;
                }
            }
            try {
                if (serviceInfo == null) {
                    this.mServiceInfo = newServiceInfoLocked(componentName);
                    if (this.mMaster.debug) {
                        Slog.d(this.mTag, "Set component for user " + this.mUserId + " as " + componentName + " and info as " + this.mServiceInfo);
                    }
                } else {
                    this.mServiceInfo = null;
                    if (this.mMaster.debug) {
                        Slog.d(this.mTag, "Reset component for user " + this.mUserId + ":" + str);
                    }
                }
            } catch (Exception e3) {
                Slog.e(this.mTag, "Bad ServiceInfo for '" + str + "': " + e3);
                this.mServiceInfo = null;
            }
        }
        return componentName;
    }

    private void updateService(ComponentName componentName) {
        if (getClass().getSimpleName().contains("AutofillManager") && this.mUserId == 0) {
            if (((UserManagerInternal) LocalServices.getService(UserManagerInternal.class)).isUserUnlockingOrUnlocked(this.mUserId)) {
                printInfo(componentName);
                this.mMaster.updateService(this.mUserId);
            } else {
                Slog.e(this.mTag, "isUserUnlockingOrUnlocked false ");
            }
        }
    }

    private void printInfo(ComponentName componentName) {
        ServiceInfo serviceInfo;
        if (componentName == null) {
            Slog.e(this.mTag, "serviceComponent == null ");
            return;
        }
        try {
            ServiceInfo serviceInfo2 = AppGlobals.getPackageManager().getServiceInfo(componentName, 786432L, this.mUserId);
            if (serviceInfo2 != null) {
                Slog.d(this.mTag, "get serviceComponent with flags: " + serviceInfo2);
                postStatisticEvent(getContext(), 1, serviceInfo2.toString());
            }
            if (serviceInfo2 != null || (serviceInfo = AppGlobals.getPackageManager().getServiceInfo(componentName, 786944L, this.mUserId)) == null) {
                return;
            }
            Slog.d(this.mTag, "get serviceComponent with flags2: " + serviceInfo);
            postStatisticEvent(getContext(), 2, serviceInfo.toString());
        } catch (Exception e) {
            Slog.e(this.mTag, "retry error: " + e);
        }
    }

    private void postStatisticEvent(Context context, int i, String str) {
        if (context == null) {
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("type", String.valueOf(i));
        hashMap.put("msg", str);
        ((IOplusStatisticsExt) ExtLoader.type(IOplusStatisticsExt.class).create()).onCommon(context, "AutoFill", "err_info", hashMap, false);
    }

    public final int getUserId() {
        return this.mUserId;
    }

    public final M getMaster() {
        return this.mMaster;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @GuardedBy({"mLock"})
    public final int getServiceUidLocked() {
        ServiceInfo serviceInfo = this.mServiceInfo;
        if (serviceInfo == null) {
            if (!this.mMaster.verbose) {
                return -1;
            }
            Slog.v(this.mTag, "getServiceUidLocked(): no mServiceInfo");
            return -1;
        }
        return serviceInfo.applicationInfo.uid;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @GuardedBy({"mLock"})
    public final String getComponentNameLocked() {
        return this.mMaster.mServiceNameResolver.getServiceName(this.mUserId);
    }

    @GuardedBy({"mLock"})
    protected final String getComponentNameForMultipleLocked(String str) {
        String[] serviceNameList = this.mMaster.mServiceNameResolver.getServiceNameList(this.mUserId);
        for (int i = 0; i < serviceNameList.length; i++) {
            if (str.equals(serviceNameList[i])) {
                return serviceNameList[i];
            }
        }
        return null;
    }

    @GuardedBy({"mLock"})
    public final boolean isTemporaryServiceSetLocked() {
        return this.mMaster.mServiceNameResolver.isTemporary(this.mUserId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @GuardedBy({"mLock"})
    public final void resetTemporaryServiceLocked() {
        this.mMaster.mServiceNameResolver.resetTemporaryService(this.mUserId);
    }

    public final ServiceInfo getServiceInfo() {
        return this.mServiceInfo;
    }

    public final ComponentName getServiceComponentName() {
        ComponentName componentName;
        synchronized (this.mLock) {
            ServiceInfo serviceInfo = this.mServiceInfo;
            componentName = serviceInfo == null ? null : serviceInfo.getComponentName();
        }
        return componentName;
    }

    public final String getServicePackageName() {
        ComponentName serviceComponentName = getServiceComponentName();
        if (serviceComponentName == null) {
            return null;
        }
        return serviceComponentName.getPackageName();
    }

    @GuardedBy({"mLock"})
    public final CharSequence getServiceLabelLocked() {
        ServiceInfo serviceInfo = this.mServiceInfo;
        if (serviceInfo == null) {
            return null;
        }
        return serviceInfo.loadSafeLabel(getContext().getPackageManager(), 0.0f, 5);
    }

    @GuardedBy({"mLock"})
    public final Drawable getServiceIconLocked() {
        ServiceInfo serviceInfo = this.mServiceInfo;
        if (serviceInfo == null) {
            return null;
        }
        return serviceInfo.loadIcon(getContext().getPackageManager());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void removeSelfFromCache() {
        synchronized (this.mMaster.mLock) {
            this.mMaster.removeCachedServiceListLocked(this.mUserId);
        }
    }

    public final boolean isDebug() {
        return this.mMaster.debug;
    }

    public final boolean isVerbose() {
        return this.mMaster.verbose;
    }

    @GuardedBy({"mLock"})
    public final int getTargedSdkLocked() {
        ServiceInfo serviceInfo = this.mServiceInfo;
        if (serviceInfo == null) {
            return 0;
        }
        return serviceInfo.applicationInfo.targetSdkVersion;
    }

    @GuardedBy({"mLock"})
    protected final boolean isSetupCompletedLocked() {
        return this.mSetupComplete;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Context getContext() {
        return this.mMaster.getContext();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @GuardedBy({"mLock"})
    public void dumpLocked(String str, PrintWriter printWriter) {
        printWriter.print(str);
        printWriter.print("User: ");
        printWriter.println(this.mUserId);
        if (this.mServiceInfo != null) {
            printWriter.print(str);
            printWriter.print("Service Label: ");
            printWriter.println(getServiceLabelLocked());
            printWriter.print(str);
            printWriter.print("Target SDK: ");
            printWriter.println(getTargedSdkLocked());
        }
        if (this.mMaster.mServiceNameResolver != null) {
            printWriter.print(str);
            printWriter.print("Name resolver: ");
            this.mMaster.mServiceNameResolver.dumpShort(printWriter, this.mUserId);
            printWriter.println();
        }
        printWriter.print(str);
        printWriter.print("Disabled by UserManager: ");
        printWriter.println(this.mDisabled);
        printWriter.print(str);
        printWriter.print("Setup complete: ");
        printWriter.println(this.mSetupComplete);
        if (this.mServiceInfo != null) {
            printWriter.print(str);
            printWriter.print("Service UID: ");
            printWriter.println(this.mServiceInfo.applicationInfo.uid);
        }
        printWriter.println();
    }
}
