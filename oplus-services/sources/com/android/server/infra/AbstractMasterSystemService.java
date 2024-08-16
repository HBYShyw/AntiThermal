package com.android.server.infra;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.content.PackageMonitor;
import com.android.internal.os.BackgroundThread;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.hdmi.HdmiCecKeycode;
import com.android.server.infra.AbstractMasterSystemService;
import com.android.server.infra.AbstractPerUserSystemService;
import com.android.server.infra.ServiceNameResolver;
import com.android.server.job.controllers.JobStatus;
import com.android.server.pm.UserManagerInternal;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class AbstractMasterSystemService<M extends AbstractMasterSystemService<M, S>, S extends AbstractPerUserSystemService<S, M>> extends SystemService {
    public static final int PACKAGE_RESTART_POLICY_NO_REFRESH = 16;
    public static final int PACKAGE_RESTART_POLICY_REFRESH_EAGER = 64;
    public static final int PACKAGE_RESTART_POLICY_REFRESH_LAZY = 32;
    public static final int PACKAGE_UPDATE_POLICY_NO_REFRESH = 1;
    public static final int PACKAGE_UPDATE_POLICY_REFRESH_EAGER = 4;
    public static final int PACKAGE_UPDATE_POLICY_REFRESH_LAZY = 2;
    public boolean debug;

    @GuardedBy({"mLock"})
    protected boolean mAllowInstantService;

    @GuardedBy({"mLock"})
    private final SparseBooleanArray mDisabledByUserRestriction;
    protected final Object mLock;
    protected int mMaxTime;
    protected final ServiceNameResolver mServiceNameResolver;
    private final int mServicePackagePolicyFlags;

    @GuardedBy({"mLock"})
    private final SparseArray<List<S>> mServicesCacheList;
    protected final String mTag;
    private UserManagerInternal mUm;
    protected boolean mUpdated;

    @GuardedBy({"mLock"})
    private SparseArray<String> mUpdatingPackageNames;
    public boolean verbose;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface ServicePackagePolicyFlags {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Visitor<S> {
        void visit(S s);
    }

    protected String getServiceSettingsProperty() {
        return null;
    }

    protected abstract S newServiceLocked(int i, boolean z);

    @GuardedBy({"mLock"})
    protected void onServiceEnabledLocked(S s, int i) {
    }

    protected void onServiceRemoved(S s, int i) {
    }

    protected void onSettingsChanged(int i, String str) {
    }

    protected void registerForExtraSettingsChanges(ContentResolver contentResolver, ContentObserver contentObserver) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractMasterSystemService(Context context, ServiceNameResolver serviceNameResolver, String str) {
        this(context, serviceNameResolver, str, 34);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractMasterSystemService(Context context, ServiceNameResolver serviceNameResolver, final String str, int i) {
        super(context);
        this.mTag = getClass().getSimpleName();
        this.mLock = new Object();
        this.verbose = false;
        this.debug = false;
        this.mServicesCacheList = new SparseArray<>();
        this.mMaxTime = 3;
        this.mUpdated = false;
        i = (i & 7) == 0 ? i | 2 : i;
        this.mServicePackagePolicyFlags = (i & HdmiCecKeycode.UI_BROADCAST_DIGITAL_CABLE) == 0 ? i | 32 : i;
        this.mServiceNameResolver = serviceNameResolver;
        if (serviceNameResolver != null) {
            serviceNameResolver.setOnTemporaryServiceNameChangedCallback(new ServiceNameResolver.NameResolverListener() { // from class: com.android.server.infra.AbstractMasterSystemService$$ExternalSyntheticLambda1
                @Override // com.android.server.infra.ServiceNameResolver.NameResolverListener
                public final void onNameResolved(int i2, String str2, boolean z) {
                    AbstractMasterSystemService.this.onServiceNameChanged(i2, str2, z);
                }
            });
        }
        if (str == null) {
            this.mDisabledByUserRestriction = null;
        } else {
            this.mDisabledByUserRestriction = new SparseBooleanArray();
            UserManagerInternal userManagerInternal = getUserManagerInternal();
            List<UserInfo> supportedUsers = getSupportedUsers();
            for (int i2 = 0; i2 < supportedUsers.size(); i2++) {
                int i3 = supportedUsers.get(i2).id;
                boolean userRestriction = userManagerInternal.getUserRestriction(i3, str);
                if (userRestriction) {
                    Slog.i(this.mTag, "Disabling by restrictions user " + i3);
                    this.mDisabledByUserRestriction.put(i3, userRestriction);
                }
            }
            userManagerInternal.addUserRestrictionsListener(new UserManagerInternal.UserRestrictionsListener() { // from class: com.android.server.infra.AbstractMasterSystemService$$ExternalSyntheticLambda2
                @Override // com.android.server.pm.UserManagerInternal.UserRestrictionsListener
                public final void onUserRestrictionsChanged(int i4, Bundle bundle, Bundle bundle2) {
                    AbstractMasterSystemService.this.lambda$new$0(str, i4, bundle, bundle2);
                }
            });
        }
        startTrackingPackageChanges();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(String str, int i, Bundle bundle, Bundle bundle2) {
        boolean z = bundle.getBoolean(str, false);
        synchronized (this.mLock) {
            if (this.mDisabledByUserRestriction.get(i) == z && this.debug) {
                Slog.d(this.mTag, "Restriction did not change for user " + i);
                return;
            }
            Slog.i(this.mTag, "Updating for user " + i + ": disabled=" + z);
            this.mDisabledByUserRestriction.put(i, z);
            updateCachedServiceLocked(i, z);
        }
    }

    public void onBootPhase(int i) {
        if (i == 600) {
            new SettingsObserver(BackgroundThread.getHandler());
        }
    }

    public void onUserUnlocking(SystemService.TargetUser targetUser) {
        synchronized (this.mLock) {
            updateCachedServiceLocked(targetUser.getUserIdentifier());
        }
    }

    public void onUserStopped(SystemService.TargetUser targetUser) {
        synchronized (this.mLock) {
            removeCachedServiceListLocked(targetUser.getUserIdentifier());
        }
    }

    public final boolean getAllowInstantService() {
        boolean z;
        enforceCallingPermissionForManagement();
        synchronized (this.mLock) {
            z = this.mAllowInstantService;
        }
        return z;
    }

    public final boolean isBindInstantServiceAllowed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mAllowInstantService;
        }
        return z;
    }

    public final void setAllowInstantService(boolean z) {
        Slog.i(this.mTag, "setAllowInstantService(): " + z);
        enforceCallingPermissionForManagement();
        synchronized (this.mLock) {
            this.mAllowInstantService = z;
        }
    }

    public final void setTemporaryService(int i, String str, int i2) {
        Slog.i(this.mTag, "setTemporaryService(" + i + ") to " + str + " for " + i2 + "ms");
        if (this.mServiceNameResolver == null) {
            return;
        }
        enforceCallingPermissionForManagement();
        Objects.requireNonNull(str);
        int maximumTemporaryServiceDurationMs = getMaximumTemporaryServiceDurationMs();
        if (i2 > maximumTemporaryServiceDurationMs) {
            throw new IllegalArgumentException("Max duration is " + maximumTemporaryServiceDurationMs + " (called with " + i2 + ")");
        }
        synchronized (this.mLock) {
            S peekServiceForUserLocked = peekServiceForUserLocked(i);
            if (peekServiceForUserLocked != null) {
                peekServiceForUserLocked.removeSelfFromCache();
            }
            this.mServiceNameResolver.setTemporaryService(i, str, i2);
        }
    }

    public final void setTemporaryServices(int i, String[] strArr, int i2) {
        Slog.i(this.mTag, "setTemporaryService(" + i + ") to " + Arrays.toString(strArr) + " for " + i2 + "ms");
        if (this.mServiceNameResolver == null) {
            return;
        }
        enforceCallingPermissionForManagement();
        Objects.requireNonNull(strArr);
        int maximumTemporaryServiceDurationMs = getMaximumTemporaryServiceDurationMs();
        if (i2 > maximumTemporaryServiceDurationMs) {
            throw new IllegalArgumentException("Max duration is " + maximumTemporaryServiceDurationMs + " (called with " + i2 + ")");
        }
        synchronized (this.mLock) {
            S peekServiceForUserLocked = peekServiceForUserLocked(i);
            if (peekServiceForUserLocked != null) {
                peekServiceForUserLocked.removeSelfFromCache();
            }
            this.mServiceNameResolver.setTemporaryServices(i, strArr, i2);
        }
    }

    public final boolean setDefaultServiceEnabled(int i, boolean z) {
        Slog.i(this.mTag, "setDefaultServiceEnabled() for userId " + i + ": " + z);
        enforceCallingPermissionForManagement();
        synchronized (this.mLock) {
            ServiceNameResolver serviceNameResolver = this.mServiceNameResolver;
            if (serviceNameResolver == null) {
                return false;
            }
            if (!serviceNameResolver.setDefaultServiceEnabled(i, z)) {
                if (this.verbose) {
                    Slog.v(this.mTag, "setDefaultServiceEnabled(" + i + "): already " + z);
                }
                return false;
            }
            S peekServiceForUserLocked = peekServiceForUserLocked(i);
            if (peekServiceForUserLocked != null) {
                peekServiceForUserLocked.removeSelfFromCache();
            }
            updateCachedServiceLocked(i);
            return true;
        }
    }

    public final boolean isDefaultServiceEnabled(int i) {
        boolean isDefaultServiceEnabled;
        enforceCallingPermissionForManagement();
        if (this.mServiceNameResolver == null) {
            return false;
        }
        synchronized (this.mLock) {
            isDefaultServiceEnabled = this.mServiceNameResolver.isDefaultServiceEnabled(i);
        }
        return isDefaultServiceEnabled;
    }

    protected int getMaximumTemporaryServiceDurationMs() {
        throw new UnsupportedOperationException("Not implemented by " + getClass());
    }

    public final void resetTemporaryService(int i) {
        Slog.i(this.mTag, "resetTemporaryService(): " + i);
        enforceCallingPermissionForManagement();
        synchronized (this.mLock) {
            S serviceForUserLocked = getServiceForUserLocked(i);
            if (serviceForUserLocked != null) {
                serviceForUserLocked.resetTemporaryServiceLocked();
            }
        }
    }

    protected void enforceCallingPermissionForManagement() {
        throw new UnsupportedOperationException("Not implemented by " + getClass());
    }

    @GuardedBy({"mLock"})
    protected List<S> newServiceListLocked(int i, boolean z, String[] strArr) {
        throw new UnsupportedOperationException("newServiceListLocked not implemented. ");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @GuardedBy({"mLock"})
    public S getServiceForUserLocked(int i) {
        List<S> serviceListForUserLocked = getServiceListForUserLocked(i);
        if (serviceListForUserLocked == null || serviceListForUserLocked.size() == 0) {
            return null;
        }
        return serviceListForUserLocked.get(0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @GuardedBy({"mLock"})
    protected List<S> getServiceListForUserLocked(int i) {
        List arrayList;
        int handleIncomingUser = ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, false, false, null, null);
        List<S> list = this.mServicesCacheList.get(handleIncomingUser);
        if (list != null && list.size() != 0) {
            return list;
        }
        boolean isDisabledLocked = isDisabledLocked(i);
        ServiceNameResolver serviceNameResolver = this.mServiceNameResolver;
        if (serviceNameResolver != null && serviceNameResolver.isConfiguredInMultipleMode()) {
            arrayList = newServiceListLocked(handleIncomingUser, isDisabledLocked, this.mServiceNameResolver.getServiceNameList(i));
        } else {
            arrayList = new ArrayList();
            arrayList.add(newServiceLocked(handleIncomingUser, isDisabledLocked));
        }
        if (!isDisabledLocked) {
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                onServiceEnabledLocked((AbstractPerUserSystemService) arrayList.get(i2), handleIncomingUser);
            }
        }
        this.mServicesCacheList.put(i, arrayList);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @GuardedBy({"mLock"})
    public S peekServiceForUserLocked(int i) {
        List<S> peekServiceListForUserLocked = peekServiceListForUserLocked(i);
        if (peekServiceListForUserLocked == null || peekServiceListForUserLocked.size() == 0) {
            return null;
        }
        return peekServiceListForUserLocked.get(0);
    }

    @GuardedBy({"mLock"})
    protected List<S> peekServiceListForUserLocked(int i) {
        return this.mServicesCacheList.get(ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, false, false, null, null));
    }

    @GuardedBy({"mLock"})
    protected void updateCachedServiceLocked(int i) {
        updateCachedServiceListLocked(i, isDisabledLocked(i));
    }

    @GuardedBy({"mLock"})
    protected boolean isDisabledLocked(int i) {
        SparseBooleanArray sparseBooleanArray = this.mDisabledByUserRestriction;
        return sparseBooleanArray != null && sparseBooleanArray.get(i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateService(final int i) {
        if (this.mMaxTime <= 0 || this.mUpdated) {
            return;
        }
        this.mUpdated = true;
        BackgroundThread.getHandler().postDelayed(new Runnable() { // from class: com.android.server.infra.AbstractMasterSystemService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AbstractMasterSystemService.this.lambda$updateService$1(i);
            }
        }, JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateService$1(int i) {
        synchronized (this.mLock) {
            this.mMaxTime--;
            this.mUpdated = false;
            Slog.e(this.mTag, "updateCachedServiceLocked, mMaxTime: " + this.mMaxTime);
            updateCachedServiceLocked(i);
        }
    }

    @GuardedBy({"mLock"})
    protected S updateCachedServiceLocked(int i, boolean z) {
        S serviceForUserLocked = getServiceForUserLocked(i);
        updateCachedServiceListLocked(i, z);
        return serviceForUserLocked;
    }

    @GuardedBy({"mLock"})
    protected List<S> updateCachedServiceListLocked(int i, boolean z) {
        ServiceNameResolver serviceNameResolver = this.mServiceNameResolver;
        if (serviceNameResolver != null && serviceNameResolver.isConfiguredInMultipleMode()) {
            return updateCachedServiceListMultiModeLocked(i, z);
        }
        List<S> serviceListForUserLocked = getServiceListForUserLocked(i);
        if (serviceListForUserLocked == null) {
            return null;
        }
        for (int i2 = 0; i2 < serviceListForUserLocked.size(); i2++) {
            S s = serviceListForUserLocked.get(i2);
            if (s != null) {
                synchronized (s.mLock) {
                    s.updateLocked(z);
                    if (!s.isEnabledLocked()) {
                        removeCachedServiceListLocked(i);
                    } else {
                        onServiceEnabledLocked(serviceListForUserLocked.get(i2), i);
                    }
                }
            }
        }
        return serviceListForUserLocked;
    }

    @GuardedBy({"mLock"})
    private List<S> updateCachedServiceListMultiModeLocked(int i, boolean z) {
        List<S> serviceListForUserLocked;
        int handleIncomingUser = ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, false, false, null, null);
        new ArrayList();
        synchronized (this.mLock) {
            removeCachedServiceListLocked(handleIncomingUser);
            serviceListForUserLocked = getServiceListForUserLocked(i);
        }
        return serviceListForUserLocked;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @GuardedBy({"mLock"})
    public final List<S> removeCachedServiceListLocked(int i) {
        List<S> peekServiceListForUserLocked = peekServiceListForUserLocked(i);
        if (peekServiceListForUserLocked != null) {
            this.mServicesCacheList.delete(i);
            for (int i2 = 0; i2 < peekServiceListForUserLocked.size(); i2++) {
                onServiceRemoved(peekServiceListForUserLocked.get(i2), i);
            }
        }
        return peekServiceListForUserLocked;
    }

    @GuardedBy({"mLock"})
    protected void onServicePackageUpdatingLocked(int i) {
        if (this.verbose) {
            Slog.v(this.mTag, "onServicePackageUpdatingLocked(" + i + ")");
        }
    }

    @GuardedBy({"mLock"})
    protected void onServicePackageUpdatedLocked(int i) {
        if (this.verbose) {
            Slog.v(this.mTag, "onServicePackageUpdated(" + i + ")");
        }
    }

    @GuardedBy({"mLock"})
    protected void onServicePackageDataClearedLocked(int i) {
        if (this.verbose) {
            Slog.v(this.mTag, "onServicePackageDataCleared(" + i + ")");
        }
    }

    @GuardedBy({"mLock"})
    protected void onServicePackageRestartedLocked(int i) {
        if (this.verbose) {
            Slog.v(this.mTag, "onServicePackageRestarted(" + i + ")");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onServiceNameChanged(int i, String str, boolean z) {
        synchronized (this.mLock) {
            updateCachedServiceListLocked(i, isDisabledLocked(i));
        }
    }

    protected void onServiceNameListChanged(int i, String[] strArr, boolean z) {
        synchronized (this.mLock) {
            updateCachedServiceListLocked(i, isDisabledLocked(i));
        }
    }

    @GuardedBy({"mLock"})
    protected void visitServicesLocked(Visitor<S> visitor) {
        int size = this.mServicesCacheList.size();
        for (int i = 0; i < size; i++) {
            List<S> valueAt = this.mServicesCacheList.valueAt(i);
            for (int i2 = 0; i2 < valueAt.size(); i2++) {
                visitor.visit(valueAt.get(i2));
            }
        }
    }

    @GuardedBy({"mLock"})
    protected void clearCacheLocked() {
        this.mServicesCacheList.clear();
    }

    protected UserManagerInternal getUserManagerInternal() {
        if (this.mUm == null) {
            if (this.verbose) {
                Slog.v(this.mTag, "lazy-loading UserManagerInternal");
            }
            this.mUm = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        }
        return this.mUm;
    }

    protected List<UserInfo> getSupportedUsers() {
        UserInfo[] userInfos = getUserManagerInternal().getUserInfos();
        ArrayList arrayList = new ArrayList(userInfos.length);
        for (UserInfo userInfo : userInfos) {
            if (isUserSupported(new SystemService.TargetUser(userInfo))) {
                arrayList.add(userInfo);
            }
        }
        return arrayList;
    }

    protected void assertCalledByPackageOwner(String str) {
        Objects.requireNonNull(str);
        int callingUid = Binder.getCallingUid();
        String[] packagesForUid = getContext().getPackageManager().getPackagesForUid(callingUid);
        if (packagesForUid != null) {
            for (String str2 : packagesForUid) {
                if (str.equals(str2)) {
                    return;
                }
            }
        }
        throw new SecurityException("UID " + callingUid + " does not own " + str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @GuardedBy({"mLock"})
    public void dumpLocked(String str, PrintWriter printWriter) {
        boolean z = this.debug;
        boolean z2 = this.verbose;
        try {
            this.verbose = true;
            this.debug = true;
            int size = this.mServicesCacheList.size();
            printWriter.print(str);
            printWriter.print("Debug: ");
            printWriter.print(z);
            printWriter.print(" Verbose: ");
            printWriter.println(z2);
            printWriter.print("Package policy flags: ");
            printWriter.println(this.mServicePackagePolicyFlags);
            if (this.mUpdatingPackageNames != null) {
                printWriter.print("Packages being updated: ");
                printWriter.println(this.mUpdatingPackageNames);
            }
            dumpSupportedUsers(printWriter, str);
            if (this.mServiceNameResolver != null) {
                printWriter.print(str);
                printWriter.print("Name resolver: ");
                this.mServiceNameResolver.dumpShort(printWriter);
                printWriter.println();
                List<UserInfo> supportedUsers = getSupportedUsers();
                for (int i = 0; i < supportedUsers.size(); i++) {
                    int i2 = supportedUsers.get(i).id;
                    printWriter.print("    ");
                    printWriter.print(i2);
                    printWriter.print(": ");
                    this.mServiceNameResolver.dumpShort(printWriter, i2);
                    printWriter.println();
                }
            }
            printWriter.print(str);
            printWriter.print("Users disabled by restriction: ");
            printWriter.println(this.mDisabledByUserRestriction);
            printWriter.print(str);
            printWriter.print("Allow instant service: ");
            printWriter.println(this.mAllowInstantService);
            String serviceSettingsProperty = getServiceSettingsProperty();
            if (serviceSettingsProperty != null) {
                printWriter.print(str);
                printWriter.print("Settings property: ");
                printWriter.println(serviceSettingsProperty);
            }
            printWriter.print(str);
            printWriter.print("Cached services: ");
            if (size == 0) {
                printWriter.println("none");
            } else {
                printWriter.println(size);
                for (int i3 = 0; i3 < size; i3++) {
                    printWriter.print(str);
                    printWriter.print("Service at ");
                    printWriter.print(i3);
                    printWriter.println(": ");
                    List<S> valueAt = this.mServicesCacheList.valueAt(i3);
                    for (int i4 = 0; i4 < valueAt.size(); i4++) {
                        S s = valueAt.get(i4);
                        synchronized (s.mLock) {
                            s.dumpLocked("    ", printWriter);
                        }
                    }
                    printWriter.println();
                }
            }
        } finally {
            this.debug = z;
            this.verbose = z2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.infra.AbstractMasterSystemService$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 extends PackageMonitor {
        AnonymousClass1() {
        }

        public void onPackageUpdateStarted(String str, int i) {
            AbstractMasterSystemService abstractMasterSystemService = AbstractMasterSystemService.this;
            if (abstractMasterSystemService.verbose) {
                Slog.v(abstractMasterSystemService.mTag, "onPackageUpdateStarted(): " + str);
            }
            String activeServicePackageNameLocked = getActiveServicePackageNameLocked();
            if (str.equals(activeServicePackageNameLocked)) {
                int changingUserId = getChangingUserId();
                synchronized (AbstractMasterSystemService.this.mLock) {
                    if (AbstractMasterSystemService.this.mUpdatingPackageNames == null) {
                        AbstractMasterSystemService.this.mUpdatingPackageNames = new SparseArray(AbstractMasterSystemService.this.mServicesCacheList.size());
                    }
                    AbstractMasterSystemService.this.mUpdatingPackageNames.put(changingUserId, str);
                    AbstractMasterSystemService.this.onServicePackageUpdatingLocked(changingUserId);
                    if ((AbstractMasterSystemService.this.mServicePackagePolicyFlags & 1) != 0) {
                        AbstractMasterSystemService abstractMasterSystemService2 = AbstractMasterSystemService.this;
                        if (abstractMasterSystemService2.debug) {
                            Slog.d(abstractMasterSystemService2.mTag, "Holding service for user " + changingUserId + " while package " + activeServicePackageNameLocked + " is being updated");
                        }
                    } else {
                        AbstractMasterSystemService abstractMasterSystemService3 = AbstractMasterSystemService.this;
                        if (abstractMasterSystemService3.debug) {
                            Slog.d(abstractMasterSystemService3.mTag, "Removing service for user " + changingUserId + " because package " + activeServicePackageNameLocked + " is being updated");
                        }
                        AbstractMasterSystemService.this.removeCachedServiceListLocked(changingUserId);
                        if ((AbstractMasterSystemService.this.mServicePackagePolicyFlags & 4) != 0) {
                            AbstractMasterSystemService abstractMasterSystemService4 = AbstractMasterSystemService.this;
                            if (abstractMasterSystemService4.debug) {
                                Slog.d(abstractMasterSystemService4.mTag, "Eagerly recreating service for user " + changingUserId);
                            }
                            AbstractMasterSystemService.this.getServiceForUserLocked(changingUserId);
                        }
                    }
                }
            }
        }

        public void onPackageUpdateFinished(String str, int i) {
            AbstractMasterSystemService abstractMasterSystemService = AbstractMasterSystemService.this;
            if (abstractMasterSystemService.verbose) {
                Slog.v(abstractMasterSystemService.mTag, "onPackageUpdateFinished(): " + str);
            }
            int changingUserId = getChangingUserId();
            synchronized (AbstractMasterSystemService.this.mLock) {
                if (str.equals(AbstractMasterSystemService.this.mUpdatingPackageNames == null ? null : (String) AbstractMasterSystemService.this.mUpdatingPackageNames.get(changingUserId))) {
                    if (AbstractMasterSystemService.this.mUpdatingPackageNames != null) {
                        AbstractMasterSystemService.this.mUpdatingPackageNames.remove(changingUserId);
                        if (AbstractMasterSystemService.this.mUpdatingPackageNames.size() == 0) {
                            AbstractMasterSystemService.this.mUpdatingPackageNames = null;
                        }
                    }
                    AbstractMasterSystemService.this.onServicePackageUpdatedLocked(changingUserId);
                } else {
                    handlePackageUpdateLocked(str);
                }
            }
        }

        public void onPackageRemoved(String str, int i) {
            ComponentName serviceComponentName;
            ServiceNameResolver serviceNameResolver = AbstractMasterSystemService.this.mServiceNameResolver;
            if (serviceNameResolver != null && serviceNameResolver.isConfiguredInMultipleMode()) {
                int changingUserId = getChangingUserId();
                synchronized (AbstractMasterSystemService.this.mLock) {
                    AbstractMasterSystemService.this.handlePackageRemovedMultiModeLocked(str, changingUserId);
                }
                return;
            }
            synchronized (AbstractMasterSystemService.this.mLock) {
                int changingUserId2 = getChangingUserId();
                AbstractPerUserSystemService peekServiceForUserLocked = AbstractMasterSystemService.this.peekServiceForUserLocked(changingUserId2);
                if (peekServiceForUserLocked != null && (serviceComponentName = peekServiceForUserLocked.getServiceComponentName()) != null && str.equals(serviceComponentName.getPackageName())) {
                    handleActiveServiceRemoved(changingUserId2);
                }
            }
        }

        public boolean onHandleForceStop(Intent intent, String[] strArr, int i, boolean z) {
            synchronized (AbstractMasterSystemService.this.mLock) {
                String activeServicePackageNameLocked = getActiveServicePackageNameLocked();
                for (String str : strArr) {
                    if (str.equals(activeServicePackageNameLocked)) {
                        if (!z) {
                            return true;
                        }
                        String action = intent.getAction();
                        int changingUserId = getChangingUserId();
                        if ("android.intent.action.PACKAGE_RESTARTED".equals(action)) {
                            handleActiveServiceRestartedLocked(activeServicePackageNameLocked, changingUserId);
                        } else {
                            AbstractMasterSystemService.this.removeCachedServiceListLocked(changingUserId);
                        }
                    } else {
                        handlePackageUpdateLocked(str);
                    }
                }
                return false;
            }
        }

        public void onPackageDataCleared(String str, int i) {
            ComponentName serviceComponentName;
            AbstractMasterSystemService abstractMasterSystemService = AbstractMasterSystemService.this;
            if (abstractMasterSystemService.verbose) {
                Slog.v(abstractMasterSystemService.mTag, "onPackageDataCleared(): " + str);
            }
            int changingUserId = getChangingUserId();
            ServiceNameResolver serviceNameResolver = AbstractMasterSystemService.this.mServiceNameResolver;
            if (serviceNameResolver != null && serviceNameResolver.isConfiguredInMultipleMode()) {
                synchronized (AbstractMasterSystemService.this.mLock) {
                    AbstractMasterSystemService.this.onServicePackageDataClearedMultiModeLocked(str, changingUserId);
                }
                return;
            }
            synchronized (AbstractMasterSystemService.this.mLock) {
                AbstractPerUserSystemService peekServiceForUserLocked = AbstractMasterSystemService.this.peekServiceForUserLocked(changingUserId);
                if (peekServiceForUserLocked != null && (serviceComponentName = peekServiceForUserLocked.getServiceComponentName()) != null && str.equals(serviceComponentName.getPackageName())) {
                    AbstractMasterSystemService.this.onServicePackageDataClearedLocked(changingUserId);
                }
            }
        }

        private void handleActiveServiceRemoved(int i) {
            synchronized (AbstractMasterSystemService.this.mLock) {
                AbstractMasterSystemService.this.removeCachedServiceListLocked(i);
            }
            String serviceSettingsProperty = AbstractMasterSystemService.this.getServiceSettingsProperty();
            if (serviceSettingsProperty != null) {
                Settings.Secure.putStringForUser(AbstractMasterSystemService.this.getContext().getContentResolver(), serviceSettingsProperty, null, i);
            }
        }

        @GuardedBy({"mLock"})
        private void handleActiveServiceRestartedLocked(String str, int i) {
            if ((AbstractMasterSystemService.this.mServicePackagePolicyFlags & 16) != 0) {
                AbstractMasterSystemService abstractMasterSystemService = AbstractMasterSystemService.this;
                if (abstractMasterSystemService.debug) {
                    Slog.d(abstractMasterSystemService.mTag, "Holding service for user " + i + " while package " + str + " is being restarted");
                }
            } else {
                AbstractMasterSystemService abstractMasterSystemService2 = AbstractMasterSystemService.this;
                if (abstractMasterSystemService2.debug) {
                    Slog.d(abstractMasterSystemService2.mTag, "Removing service for user " + i + " because package " + str + " is being restarted");
                }
                AbstractMasterSystemService.this.removeCachedServiceListLocked(i);
                if ((AbstractMasterSystemService.this.mServicePackagePolicyFlags & 64) != 0) {
                    AbstractMasterSystemService abstractMasterSystemService3 = AbstractMasterSystemService.this;
                    if (abstractMasterSystemService3.debug) {
                        Slog.d(abstractMasterSystemService3.mTag, "Eagerly recreating service for user " + i);
                    }
                    AbstractMasterSystemService.this.updateCachedServiceLocked(i);
                }
            }
            AbstractMasterSystemService.this.onServicePackageRestartedLocked(i);
        }

        public void onPackageModified(String str) {
            synchronized (AbstractMasterSystemService.this.mLock) {
                AbstractMasterSystemService abstractMasterSystemService = AbstractMasterSystemService.this;
                if (abstractMasterSystemService.verbose) {
                    Slog.v(abstractMasterSystemService.mTag, "onPackageModified(): " + str);
                }
                if (AbstractMasterSystemService.this.mServiceNameResolver == null) {
                    return;
                }
                int changingUserId = getChangingUserId();
                String[] defaultServiceNameList = AbstractMasterSystemService.this.mServiceNameResolver.getDefaultServiceNameList(changingUserId);
                if (defaultServiceNameList != null) {
                    for (String str2 : defaultServiceNameList) {
                        peekAndUpdateCachedServiceLocked(str, changingUserId, str2);
                    }
                }
            }
        }

        @GuardedBy({"mLock"})
        private void peekAndUpdateCachedServiceLocked(String str, int i, String str2) {
            ComponentName unflattenFromString;
            AbstractPerUserSystemService peekServiceForUserLocked;
            if (str2 == null || (unflattenFromString = ComponentName.unflattenFromString(str2)) == null || !unflattenFromString.getPackageName().equals(str) || (peekServiceForUserLocked = AbstractMasterSystemService.this.peekServiceForUserLocked(i)) == null || peekServiceForUserLocked.getServiceComponentName() != null) {
                return;
            }
            AbstractMasterSystemService abstractMasterSystemService = AbstractMasterSystemService.this;
            if (abstractMasterSystemService.verbose) {
                Slog.v(abstractMasterSystemService.mTag, "update cached");
            }
            AbstractMasterSystemService.this.updateCachedServiceLocked(i);
        }

        @GuardedBy({"mLock"})
        private String getActiveServicePackageNameLocked() {
            ComponentName serviceComponentName;
            AbstractPerUserSystemService peekServiceForUserLocked = AbstractMasterSystemService.this.peekServiceForUserLocked(getChangingUserId());
            if (peekServiceForUserLocked == null || (serviceComponentName = peekServiceForUserLocked.getServiceComponentName()) == null) {
                return null;
            }
            return serviceComponentName.getPackageName();
        }

        @GuardedBy({"mLock"})
        private void handlePackageUpdateLocked(final String str) {
            AbstractMasterSystemService.this.visitServicesLocked(new Visitor() { // from class: com.android.server.infra.AbstractMasterSystemService$1$$ExternalSyntheticLambda0
                @Override // com.android.server.infra.AbstractMasterSystemService.Visitor
                public final void visit(Object obj) {
                    ((AbstractPerUserSystemService) obj).handlePackageUpdateLocked(str);
                }
            });
        }
    }

    private void startTrackingPackageChanges() {
        new AnonymousClass1().getWrapper().getExtImpl().register(getContext(), (Looper) null, UserHandle.ALL, true, new int[]{6, 3, 7, 12, 11, 5});
    }

    @GuardedBy({"mLock"})
    protected void onServicePackageDataClearedMultiModeLocked(String str, int i) {
        if (this.verbose) {
            Slog.v(this.mTag, "onServicePackageDataClearedMultiModeLocked(" + i + ")");
        }
    }

    @GuardedBy({"mLock"})
    protected void handlePackageRemovedMultiModeLocked(String str, int i) {
        if (this.verbose) {
            Slog.v(this.mTag, "handlePackageRemovedMultiModeLocked(" + i + ")");
        }
    }

    @GuardedBy({"mLock"})
    protected void removeServiceFromCache(S s, int i) {
        if (this.mServicesCacheList.get(i) != null) {
            this.mServicesCacheList.get(i).remove(s);
        }
    }

    @GuardedBy({"mLock"})
    protected void removeServiceFromMultiModeSettings(String str, int i) {
        ServiceNameResolver serviceNameResolver;
        if (getServiceSettingsProperty() == null || (serviceNameResolver = this.mServiceNameResolver) == null || !serviceNameResolver.isConfiguredInMultipleMode()) {
            if (this.verbose) {
                Slog.v(this.mTag, "removeServiceFromSettings not implemented  for single backend implementation");
                return;
            }
            return;
        }
        String[] serviceNameList = this.mServiceNameResolver.getServiceNameList(i);
        ArrayList arrayList = new ArrayList();
        for (String str2 : serviceNameList) {
            if (!str2.equals(str)) {
                arrayList.add(str2);
            }
        }
        this.mServiceNameResolver.setServiceNameList(arrayList, i);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
            ContentResolver contentResolver = AbstractMasterSystemService.this.getContext().getContentResolver();
            String serviceSettingsProperty = AbstractMasterSystemService.this.getServiceSettingsProperty();
            if (serviceSettingsProperty != null) {
                contentResolver.registerContentObserver(Settings.Secure.getUriFor(serviceSettingsProperty), false, this, -1);
            }
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("user_setup_complete"), false, this, -1);
            AbstractMasterSystemService.this.registerForExtraSettingsChanges(contentResolver, this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri, int i) {
            AbstractMasterSystemService abstractMasterSystemService = AbstractMasterSystemService.this;
            if (abstractMasterSystemService.verbose) {
                Slog.v(abstractMasterSystemService.mTag, "onChange(): uri=" + uri + ", userId=" + i);
            }
            String lastPathSegment = uri.getLastPathSegment();
            if (lastPathSegment == null) {
                return;
            }
            if (lastPathSegment.equals(AbstractMasterSystemService.this.getServiceSettingsProperty()) || lastPathSegment.equals("user_setup_complete")) {
                synchronized (AbstractMasterSystemService.this.mLock) {
                    AbstractMasterSystemService.this.updateCachedServiceLocked(i);
                }
                return;
            }
            AbstractMasterSystemService.this.onSettingsChanged(i, lastPathSegment);
        }
    }
}
