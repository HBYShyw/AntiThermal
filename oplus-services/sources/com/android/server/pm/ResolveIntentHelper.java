package com.android.server.pm;

import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.IUnsafeIntentStrictModeCallback;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.AuxiliaryResolveInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Trace;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import com.android.internal.app.ResolverActivity;
import com.android.internal.util.ArrayUtils;
import com.android.server.LocalServices;
import com.android.server.am.ActivityManagerUtils;
import com.android.server.compat.PlatformCompat;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.resolution.ComponentResolverApi;
import com.android.server.pm.verify.domain.DomainVerificationManagerInternal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class ResolveIntentHelper {
    private final Context mContext;
    private final DomainVerificationManagerInternal mDomainVerificationManager;
    private final Handler mHandler;
    private final Supplier<ActivityInfo> mInstantAppInstallerActivitySupplier;
    private final PlatformCompat mPlatformCompat;
    private final PreferredActivityHelper mPreferredActivityHelper;
    private final Supplier<ResolveInfo> mResolveInfoSupplier;
    private final IResolveIntentHelperExt mResolveIntentHelperExt = (IResolveIntentHelperExt) ExtLoader.type(IResolveIntentHelperExt.class).base(this).create();
    private final UserManagerService mUserManager;
    private final UserNeedsBadgingCache mUserNeedsBadging;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ResolveIntentHelper(Context context, PreferredActivityHelper preferredActivityHelper, PlatformCompat platformCompat, UserManagerService userManagerService, DomainVerificationManagerInternal domainVerificationManagerInternal, UserNeedsBadgingCache userNeedsBadgingCache, Supplier<ResolveInfo> supplier, Supplier<ActivityInfo> supplier2, Handler handler) {
        this.mContext = context;
        this.mPreferredActivityHelper = preferredActivityHelper;
        this.mPlatformCompat = platformCompat;
        this.mUserManager = userManagerService;
        this.mDomainVerificationManager = domainVerificationManagerInternal;
        this.mUserNeedsBadging = userNeedsBadgingCache;
        this.mResolveInfoSupplier = supplier;
        this.mInstantAppInstallerActivitySupplier = supplier2;
        this.mHandler = handler;
    }

    private static void filterNonExportedComponents(final Intent intent, int i, final int i2, List<ResolveInfo> list, PlatformCompat platformCompat, String str, Computer computer, Handler handler) {
        if (list == null || intent.getPackage() != null || intent.getComponent() != null || ActivityManager.canAccessUnexportedComponents(i)) {
            return;
        }
        AndroidPackage androidPackage = computer.getPackage(i);
        if (androidPackage != null) {
            androidPackage.getPackageName();
        }
        final ActivityManagerInternal activityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        final IUnsafeIntentStrictModeCallback registeredStrictModeCallback = activityManagerInternal.getRegisteredStrictModeCallback(i2);
        for (int size = list.size() - 1; size >= 0; size--) {
            if (!list.get(size).getComponentInfo().exported) {
                boolean isChangeEnabledByUid = platformCompat.isChangeEnabledByUid(229362273L, i);
                ActivityManagerUtils.logUnsafeIntentEvent(2, i, intent, str, isChangeEnabledByUid);
                if (registeredStrictModeCallback != null) {
                    handler.post(new Runnable() { // from class: com.android.server.pm.ResolveIntentHelper$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            ResolveIntentHelper.lambda$filterNonExportedComponents$0(registeredStrictModeCallback, intent, activityManagerInternal, i2);
                        }
                    });
                }
                if (!isChangeEnabledByUid) {
                    return;
                } else {
                    list.remove(size);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$filterNonExportedComponents$0(IUnsafeIntentStrictModeCallback iUnsafeIntentStrictModeCallback, Intent intent, ActivityManagerInternal activityManagerInternal, int i) {
        Trace.traceBegin(262144L, "PackageManagerBg onImplicitIntentMatchedInternalComponent");
        try {
            iUnsafeIntentStrictModeCallback.onImplicitIntentMatchedInternalComponent(intent.cloneFilter());
        } catch (RemoteException unused) {
            activityManagerInternal.unregisterStrictModeCallback(i);
        }
        Trace.traceEnd(262144L);
    }

    public ResolveInfo resolveIntentInternal(Computer computer, Intent intent, String str, long j, long j2, int i, boolean z, int i2) {
        return resolveIntentInternal(computer, intent, str, j, j2, i, z, i2, false, 0);
    }

    public ResolveInfo resolveIntentInternal(Computer computer, Intent intent, String str, long j, long j2, int i, boolean z, int i2, boolean z2, int i3) {
        try {
            Trace.traceBegin(262144L, "resolveIntent");
            if (!this.mUserManager.exists(i)) {
                return null;
            }
            int callingUid = Binder.getCallingUid();
            long updateFlagsForResolve = computer.updateFlagsForResolve(j, i, i2, z, computer.isImplicitImageCaptureIntentAndNotSetByDpc(intent, i, str, j));
            computer.enforceCrossUserPermission(callingUid, i, false, false, "resolve intent");
            this.mResolveIntentHelperExt.interceptHandler(intent);
            long j3 = this.mResolveIntentHelperExt.interceptHttpAppDetails(intent) ? 0L : updateFlagsForResolve;
            Trace.traceBegin(262144L, "queryIntentActivities");
            List<ResolveInfo> queryIntentActivitiesInternal = computer.queryIntentActivitiesInternal(intent, str, j3, j2, i2, i, z, true);
            if (z2) {
                filterNonExportedComponents(intent, i2, i3, queryIntentActivitiesInternal, this.mPlatformCompat, str, computer, this.mHandler);
            }
            Trace.traceEnd(262144L);
            boolean z3 = true;
            ResolveInfo chooseBestActivity = chooseBestActivity(computer, intent, str, j3, j2, queryIntentActivitiesInternal, i, UserHandle.getAppId(i2) >= 10000 && !z);
            if ((j2 & 1) == 0) {
                z3 = false;
            }
            if (z3 && chooseBestActivity != null) {
                if (chooseBestActivity.handleAllWebDataURI) {
                    return null;
                }
            }
            return chooseBestActivity;
        } finally {
            Trace.traceEnd(262144L);
        }
    }

    private ResolveInfo chooseBestActivity(Computer computer, Intent intent, String str, long j, long j2, List<ResolveInfo> list, int i, boolean z) {
        int i2;
        boolean z2;
        PackageStateInternal packageStateInternal;
        if (list != null) {
            int size = list.size();
            if (size == 1) {
                return list.get(0);
            }
            if (size > 1) {
                boolean z3 = (intent.getFlags() & 8) != 0;
                ResolveInfo interceptAppDetailsToMarket = this.mResolveIntentHelperExt.interceptAppDetailsToMarket(intent, list, computer, i);
                if (interceptAppDetailsToMarket != null) {
                    return interceptAppDetailsToMarket;
                }
                ResolveInfo resolveInfo = list.get(0);
                ResolveInfo resolveInfo2 = list.get(1);
                if (PackageManagerService.DEBUG_INTENT_MATCHING || z3) {
                    Slog.v("PackageManager", resolveInfo.activityInfo.name + "=" + resolveInfo.priority + " vs " + resolveInfo2.activityInfo.name + "=" + resolveInfo2.priority);
                }
                if (resolveInfo.priority != resolveInfo2.priority || resolveInfo.preferredOrder != resolveInfo2.preferredOrder || resolveInfo.isDefault != resolveInfo2.isDefault) {
                    return list.get(0);
                }
                ResolveInfo findPriorBeforeUsePreferenceInChooseBestActivity = this.mResolveIntentHelperExt.findPriorBeforeUsePreferenceInChooseBestActivity(intent, list);
                if (findPriorBeforeUsePreferenceInChooseBestActivity != null) {
                    return findPriorBeforeUsePreferenceInChooseBestActivity;
                }
                int changeUserIdInChooseBestActivity = this.mResolveIntentHelperExt.changeUserIdInChooseBestActivity(i, resolveInfo);
                ResolveInfo adjustQueryAndResultForUsePrefInChooseBestActivity = this.mResolveIntentHelperExt.adjustQueryAndResultForUsePrefInChooseBestActivity(computer, intent, list, this.mPreferredActivityHelper.findPreferredActivityNotLocked(computer, intent, str, j, list, true, false, z3, changeUserIdInChooseBestActivity, z));
                if (adjustQueryAndResultForUsePrefInChooseBestActivity != null) {
                    return adjustQueryAndResultForUsePrefInChooseBestActivity;
                }
                int size2 = list.size();
                if (size2 == 0) {
                    return null;
                }
                int i3 = 0;
                int i4 = 0;
                while (i4 < size2) {
                    ResolveInfo resolveInfo3 = list.get(i4);
                    if (resolveInfo3.handleAllWebDataURI) {
                        i3++;
                    }
                    int i5 = i3;
                    if (resolveInfo3.activityInfo.applicationInfo.isInstantApp() && (packageStateInternal = computer.getPackageStateInternal(resolveInfo3.activityInfo.packageName)) != null && PackageManagerServiceUtils.hasAnyDomainApproval(this.mDomainVerificationManager, packageStateInternal, intent, j, changeUserIdInChooseBestActivity)) {
                        return resolveInfo3;
                    }
                    i4++;
                    i3 = i5;
                }
                if ((j2 & 2) != 0) {
                    return null;
                }
                ResolveInfo resolveInfo4 = new ResolveInfo(this.mResolveInfoSupplier.get());
                resolveInfo4.handleAllWebDataURI = i3 == size2;
                ActivityInfo activityInfo = new ActivityInfo(resolveInfo4.activityInfo);
                resolveInfo4.activityInfo = activityInfo;
                activityInfo.labelRes = ResolverActivity.getLabelRes(intent.getAction());
                if (resolveInfo4.userHandle == null) {
                    resolveInfo4.userHandle = UserHandle.of(changeUserIdInChooseBestActivity);
                }
                String str2 = intent.getPackage();
                if (TextUtils.isEmpty(str2) || !allHavePackage(list, str2)) {
                    i2 = changeUserIdInChooseBestActivity;
                    z2 = true;
                } else {
                    ApplicationInfo applicationInfo = list.get(0).activityInfo.applicationInfo;
                    resolveInfo4.resolvePackageName = str2;
                    i2 = changeUserIdInChooseBestActivity;
                    if (this.mUserNeedsBadging.get(i2)) {
                        z2 = true;
                        resolveInfo4.noResourceId = true;
                    } else {
                        z2 = true;
                        resolveInfo4.icon = applicationInfo.icon;
                    }
                    resolveInfo4.iconResourceId = applicationInfo.icon;
                    resolveInfo4.labelRes = applicationInfo.labelRes;
                }
                resolveInfo4.activityInfo.applicationInfo = new ApplicationInfo(resolveInfo4.activityInfo.applicationInfo);
                if (i2 != 0) {
                    ApplicationInfo applicationInfo2 = resolveInfo4.activityInfo.applicationInfo;
                    applicationInfo2.uid = UserHandle.getUid(i2, UserHandle.getAppId(applicationInfo2.uid));
                }
                ActivityInfo activityInfo2 = resolveInfo4.activityInfo;
                if (activityInfo2.metaData == null) {
                    activityInfo2.metaData = new Bundle();
                }
                resolveInfo4.activityInfo.metaData.putBoolean("android.dock_home", z2);
                return resolveInfo4;
            }
        }
        return null;
    }

    private boolean allHavePackage(List<ResolveInfo> list, String str) {
        if (ArrayUtils.isEmpty(list)) {
            return false;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ResolveInfo resolveInfo = list.get(i);
            ActivityInfo activityInfo = resolveInfo != null ? resolveInfo.activityInfo : null;
            if (activityInfo == null || !str.equals(activityInfo.packageName)) {
                return false;
            }
        }
        return true;
    }

    public IntentSender getLaunchIntentSenderForPackage(Computer computer, String str, String str2, String str3, int i) throws RemoteException {
        Objects.requireNonNull(str);
        int callingUid = Binder.getCallingUid();
        computer.enforceCrossUserPermission(callingUid, i, false, false, "get launch intent sender for package");
        if (!UserHandle.isSameApp(callingUid, computer.getPackageUid(str2, 0L, i))) {
            throw new SecurityException("getLaunchIntentSenderForPackage() from calling uid: " + callingUid + " does not own package: " + str2);
        }
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.INFO");
        intent.setPackage(str);
        ContentResolver contentResolver = this.mContext.getContentResolver();
        String resolveTypeIfNeeded = intent.resolveTypeIfNeeded(contentResolver);
        List<ResolveInfo> queryIntentActivitiesInternal = computer.queryIntentActivitiesInternal(intent, resolveTypeIfNeeded, 0L, 0L, callingUid, i, true, false);
        if (queryIntentActivitiesInternal == null || queryIntentActivitiesInternal.size() <= 0) {
            intent.removeCategory("android.intent.category.INFO");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setPackage(str);
            resolveTypeIfNeeded = intent.resolveTypeIfNeeded(contentResolver);
            queryIntentActivitiesInternal = computer.queryIntentActivitiesInternal(intent, resolveTypeIfNeeded, 0L, 0L, callingUid, i, true, false);
        }
        Intent intent2 = new Intent(intent);
        intent2.setFlags(268435456);
        if (queryIntentActivitiesInternal != null && !queryIntentActivitiesInternal.isEmpty()) {
            intent2.setPackage(null);
            intent2.setClassName(queryIntentActivitiesInternal.get(0).activityInfo.packageName, queryIntentActivitiesInternal.get(0).activityInfo.name);
        }
        return new IntentSender(ActivityManager.getService().getIntentSenderWithFeature(2, str2, str3, (IBinder) null, (String) null, 1, new Intent[]{intent2}, resolveTypeIfNeeded != null ? new String[]{resolveTypeIfNeeded} : null, 67108864, (Bundle) null, i));
    }

    public List<ResolveInfo> queryIntentReceiversInternal(Computer computer, Intent intent, String str, long j, int i, int i2) {
        return queryIntentReceiversInternal(computer, intent, str, j, i, i2, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:73:0x0139, code lost:
    
        if (r1 != null) goto L76;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public List<ResolveInfo> queryIntentReceiversInternal(Computer computer, Intent intent, String str, long j, int i, int i2, boolean z) {
        Intent intent2;
        Intent intent3;
        List<ResolveInfo> list;
        List<ResolveInfo> queryReceivers;
        if (!this.mUserManager.exists(i)) {
            return Collections.emptyList();
        }
        int i3 = z ? 1000 : i2;
        computer.enforceCrossUserPermission(i3, i, false, false, "query intent receivers");
        String instantAppPackageName = computer.getInstantAppPackageName(i3);
        long updateFlagsForResolve = computer.updateFlagsForResolve(j, i, i3, false, computer.isImplicitImageCaptureIntentAndNotSetByDpc(intent, i, str, j));
        ComponentName component = intent.getComponent();
        if (component != null || intent.getSelector() == null) {
            intent2 = intent;
            intent3 = null;
        } else {
            Intent selector = intent.getSelector();
            intent3 = intent;
            intent2 = selector;
            component = selector.getComponent();
        }
        ComponentResolverApi componentResolver = computer.getComponentResolver();
        List<ResolveInfo> emptyList = Collections.emptyList();
        if (component != null) {
            ActivityInfo receiverInfo = computer.getReceiverInfo(component, updateFlagsForResolve, i);
            if (receiverInfo != null) {
                boolean z2 = false;
                boolean z3 = (8388608 & updateFlagsForResolve) != 0;
                boolean z4 = (updateFlagsForResolve & 16777216) != 0;
                boolean z5 = (updateFlagsForResolve & 33554432) != 0;
                boolean z6 = instantAppPackageName != null;
                boolean equals = component.getPackageName().equals(instantAppPackageName);
                boolean z7 = (receiverInfo.applicationInfo.privateFlags & 128) != 0;
                int i4 = receiverInfo.flags;
                boolean z8 = (i4 & 1048576) != 0;
                boolean z9 = !z8 || (z5 && !(z8 && (i4 & DumpState.DUMP_COMPILER_STATS) == 0));
                if (!equals && ((!z3 && !z6 && z7) || (z4 && z6 && z9))) {
                    z2 = true;
                }
                if (!z2) {
                    ResolveInfo resolveInfo = new ResolveInfo();
                    resolveInfo.activityInfo = receiverInfo;
                    ArrayList arrayList = new ArrayList(1);
                    arrayList.add(resolveInfo);
                    PackageManagerServiceUtils.applyEnforceIntentFilterMatching(this.mPlatformCompat, componentResolver, arrayList, true, intent2, str, i2);
                    emptyList = arrayList;
                }
            }
            list = emptyList;
        } else {
            String str2 = intent2.getPackage();
            List<ResolveInfo> list2 = (str2 != null || (queryReceivers = componentResolver.queryReceivers(computer, intent2, str, updateFlagsForResolve, i)) == null) ? emptyList : queryReceivers;
            AndroidPackage androidPackage = computer.getPackage(str2);
            if (androidPackage != null) {
                list = componentResolver.queryReceivers(computer, intent2, str, updateFlagsForResolve, androidPackage.getReceivers(), i);
            }
            list = list2;
        }
        if (intent3 != null) {
            PackageManagerServiceUtils.applyEnforceIntentFilterMatching(this.mPlatformCompat, componentResolver, list, true, intent3, str, i2);
        }
        return computer.applyPostResolutionFilter(list, instantAppPackageName, false, i3, false, i, intent2);
    }

    public ResolveInfo resolveServiceInternal(Computer computer, Intent intent, String str, long j, int i, int i2) {
        List<ResolveInfo> queryIntentServicesInternal;
        if (this.mUserManager.exists(i) && (queryIntentServicesInternal = computer.queryIntentServicesInternal(intent, str, computer.updateFlagsForResolve(j, i, i2, false, false), i, i2, false)) != null && queryIntentServicesInternal.size() >= 1) {
            return queryIntentServicesInternal.get(0);
        }
        return null;
    }

    public List<ResolveInfo> queryIntentContentProvidersInternal(Computer computer, Intent intent, String str, long j, int i) {
        Intent intent2;
        if (!this.mUserManager.exists(i)) {
            return Collections.emptyList();
        }
        int callingUid = Binder.getCallingUid();
        String instantAppPackageName = computer.getInstantAppPackageName(callingUid);
        long updateFlagsForResolve = computer.updateFlagsForResolve(j, i, callingUid, false, false);
        ComponentName component = intent.getComponent();
        if (component != null || intent.getSelector() == null) {
            intent2 = intent;
        } else {
            Intent selector = intent.getSelector();
            intent2 = selector;
            component = selector.getComponent();
        }
        if (component != null) {
            ArrayList arrayList = new ArrayList(1);
            ProviderInfo providerInfo = computer.getProviderInfo(component, updateFlagsForResolve, i);
            if (providerInfo != null) {
                boolean z = (8388608 & updateFlagsForResolve) != 0;
                boolean z2 = (updateFlagsForResolve & 16777216) != 0;
                boolean z3 = instantAppPackageName != null;
                boolean equals = component.getPackageName().equals(instantAppPackageName);
                ApplicationInfo applicationInfo = providerInfo.applicationInfo;
                boolean z4 = (applicationInfo.privateFlags & 128) != 0;
                boolean z5 = !equals && (!(z || z3 || !z4) || (z2 && z3 && ((providerInfo.flags & 1048576) == 0)));
                boolean z6 = (z4 || z3 || !computer.shouldFilterApplication(computer.getPackageStateInternal(applicationInfo.packageName, 1000), callingUid, i)) ? false : true;
                if (!z5 && !z6) {
                    ResolveInfo resolveInfo = new ResolveInfo();
                    resolveInfo.providerInfo = providerInfo;
                    arrayList.add(resolveInfo);
                }
            }
            return arrayList;
        }
        ComponentResolverApi componentResolver = computer.getComponentResolver();
        String str2 = intent2.getPackage();
        if (str2 == null) {
            List<ResolveInfo> queryProviders = componentResolver.queryProviders(computer, intent2, str, updateFlagsForResolve, i);
            if (queryProviders == null) {
                return Collections.emptyList();
            }
            return applyPostContentProviderResolutionFilter(computer, queryProviders, instantAppPackageName, i, callingUid);
        }
        AndroidPackage androidPackage = computer.getPackage(str2);
        if (androidPackage != null) {
            List<ResolveInfo> queryProviders2 = componentResolver.queryProviders(computer, intent2, str, updateFlagsForResolve, androidPackage.getProviders(), i);
            if (queryProviders2 == null) {
                return Collections.emptyList();
            }
            return applyPostContentProviderResolutionFilter(computer, queryProviders2, instantAppPackageName, i, callingUid);
        }
        return Collections.emptyList();
    }

    private List<ResolveInfo> applyPostContentProviderResolutionFilter(Computer computer, List<ResolveInfo> list, String str, int i, int i2) {
        for (int size = list.size() - 1; size >= 0; size--) {
            ResolveInfo resolveInfo = list.get(size);
            if (str != null || computer.shouldFilterApplication(computer.getPackageStateInternal(resolveInfo.providerInfo.packageName, 0), i2, i)) {
                boolean isInstantApp = resolveInfo.providerInfo.applicationInfo.isInstantApp();
                if (isInstantApp && str.equals(resolveInfo.providerInfo.packageName)) {
                    ProviderInfo providerInfo = resolveInfo.providerInfo;
                    String str2 = providerInfo.splitName;
                    if (str2 != null && !ArrayUtils.contains(providerInfo.applicationInfo.splitNames, str2)) {
                        if (this.mInstantAppInstallerActivitySupplier.get() == null) {
                            if (PackageManagerService.DEBUG_INSTANT) {
                                Slog.v("PackageManager", "No installer - not adding it to the ResolveInfo list");
                            }
                            list.remove(size);
                        } else {
                            if (PackageManagerService.DEBUG_INSTANT) {
                                Slog.v("PackageManager", "Adding ephemeral installer to the ResolveInfo list");
                            }
                            ResolveInfo resolveInfo2 = new ResolveInfo(computer.getInstantAppInstallerInfo());
                            ProviderInfo providerInfo2 = resolveInfo.providerInfo;
                            resolveInfo2.auxiliaryInfo = new AuxiliaryResolveInfo((ComponentName) null, providerInfo2.packageName, providerInfo2.applicationInfo.longVersionCode, providerInfo2.splitName);
                            resolveInfo2.filter = new IntentFilter();
                            resolveInfo2.resolvePackageName = resolveInfo.getComponentInfo().packageName;
                            list.set(size, resolveInfo2);
                        }
                    }
                } else if (isInstantApp || (resolveInfo.providerInfo.flags & 1048576) == 0) {
                    list.remove(size);
                }
            }
        }
        return list;
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x016d  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0201  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x015d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public List<ResolveInfo> queryIntentActivityOptionsInternal(Computer computer, ComponentName componentName, Intent[] intentArr, String[] strArr, Intent intent, String str, long j, int i) {
        String str2;
        String str3;
        String str4;
        int i2;
        Iterator<String> actionsIterator;
        String str5;
        String str6;
        int i3;
        int i4;
        String str7;
        String str8;
        String str9;
        List<ResolveInfo> list;
        String str10;
        ActivityInfo activityInfo;
        ComponentName componentName2;
        ResolveInfo resolveInfo;
        int i5;
        String str11;
        String str12;
        ResolveInfo resolveInfo2;
        int size;
        int i6;
        String str13;
        String str14;
        ComponentName componentName3;
        int i7;
        String str15;
        Intent[] intentArr2 = intentArr;
        if (!this.mUserManager.exists(i)) {
            return Collections.emptyList();
        }
        int callingUid = Binder.getCallingUid();
        long updateFlagsForResolve = computer.updateFlagsForResolve(j, i, callingUid, false, computer.isImplicitImageCaptureIntentAndNotSetByDpc(intent, i, str, j));
        computer.enforceCrossUserPermission(callingUid, i, false, false, "query intent activity options");
        String action = intent.getAction();
        List<ResolveInfo> queryIntentActivitiesInternal = computer.queryIntentActivitiesInternal(intent, str, updateFlagsForResolve | 64, i);
        String str16 = ": ";
        String str17 = "PackageManager";
        if (PackageManagerService.DEBUG_INTENT_MATCHING) {
            Log.v("PackageManager", "Query " + intent + ": " + queryIntentActivitiesInternal);
        }
        String str18 = "Removing duplicate item from ";
        if (intentArr2 != null) {
            i2 = 0;
            int i8 = 0;
            while (i8 < intentArr2.length) {
                Intent intent2 = intentArr2[i8];
                if (intent2 == null) {
                    i3 = i2;
                    i4 = i8;
                    str7 = str17;
                    str8 = str18;
                    str9 = str16;
                    list = queryIntentActivitiesInternal;
                    str10 = action;
                } else {
                    if (PackageManagerService.DEBUG_INTENT_MATCHING) {
                        Log.v(str17, "Specific #" + i8 + str16 + intent2);
                    }
                    String action2 = intent2.getAction();
                    if (action != null && action.equals(action2)) {
                        action2 = null;
                    }
                    ComponentName component = intent2.getComponent();
                    if (component == null) {
                        str5 = action2;
                        str6 = "Specific #";
                        i3 = i2;
                        i4 = i8;
                        str7 = str17;
                        str8 = str18;
                        str9 = str16;
                        list = queryIntentActivitiesInternal;
                        str10 = action;
                        resolveInfo = resolveIntentInternal(computer, intent2, strArr != null ? strArr[i8] : null, updateFlagsForResolve, 0L, i, false, Binder.getCallingUid());
                        if (resolveInfo != null) {
                            this.mResolveInfoSupplier.get();
                            ActivityInfo activityInfo2 = resolveInfo.activityInfo;
                            componentName2 = new ComponentName(activityInfo2.applicationInfo.packageName, activityInfo2.name);
                            activityInfo = activityInfo2;
                            if (PackageManagerService.DEBUG_INTENT_MATCHING) {
                                i5 = i4;
                                str11 = str7;
                                str12 = str9;
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append(str6);
                                i5 = i4;
                                sb.append(i5);
                                str12 = str9;
                                sb.append(str12);
                                sb.append(activityInfo);
                                str11 = str7;
                                Log.v(str11, sb.toString());
                            }
                            resolveInfo2 = resolveInfo;
                            size = list.size();
                            i6 = i3;
                            while (i6 < size) {
                                List<ResolveInfo> list2 = list;
                                ResolveInfo resolveInfo3 = list2.get(i6);
                                String str19 = str12;
                                if (resolveInfo3.activityInfo.name.equals(componentName2.getClassName()) && resolveInfo3.activityInfo.applicationInfo.packageName.equals(componentName2.getPackageName())) {
                                    str14 = str5;
                                } else {
                                    str14 = str5;
                                    if (str14 == null || !resolveInfo3.filter.matchAction(str14)) {
                                        str5 = str14;
                                        componentName3 = componentName2;
                                        i7 = i3;
                                        str15 = str8;
                                        i6++;
                                        str8 = str15;
                                        i3 = i7;
                                        str12 = str19;
                                        componentName2 = componentName3;
                                        list = list2;
                                    }
                                }
                                list2.remove(i6);
                                if (PackageManagerService.DEBUG_INTENT_MATCHING) {
                                    StringBuilder sb2 = new StringBuilder();
                                    str5 = str14;
                                    str15 = str8;
                                    sb2.append(str15);
                                    sb2.append(i6);
                                    componentName3 = componentName2;
                                    sb2.append(" due to specific ");
                                    i7 = i3;
                                    sb2.append(i7);
                                    Log.v(str11, sb2.toString());
                                } else {
                                    str5 = str14;
                                    componentName3 = componentName2;
                                    i7 = i3;
                                    str15 = str8;
                                }
                                if (resolveInfo2 == null) {
                                    resolveInfo2 = resolveInfo3;
                                }
                                i6--;
                                size--;
                                i6++;
                                str8 = str15;
                                i3 = i7;
                                str12 = str19;
                                componentName2 = componentName3;
                                list = list2;
                            }
                            queryIntentActivitiesInternal = list;
                            str9 = str12;
                            int i9 = i3;
                            str13 = str8;
                            if (resolveInfo2 == null) {
                                resolveInfo2 = new ResolveInfo();
                                resolveInfo2.activityInfo = activityInfo;
                            }
                            queryIntentActivitiesInternal.add(i9, resolveInfo2);
                            resolveInfo2.specificIndex = i5;
                            i2 = i9 + 1;
                        }
                    } else {
                        str5 = action2;
                        str6 = "Specific #";
                        i3 = i2;
                        i4 = i8;
                        str7 = str17;
                        str8 = str18;
                        str9 = str16;
                        list = queryIntentActivitiesInternal;
                        str10 = action;
                        activityInfo = computer.getActivityInfo(component, updateFlagsForResolve, i);
                        if (activityInfo != null) {
                            componentName2 = component;
                            resolveInfo = null;
                            if (PackageManagerService.DEBUG_INTENT_MATCHING) {
                            }
                            resolveInfo2 = resolveInfo;
                            size = list.size();
                            i6 = i3;
                            while (i6 < size) {
                            }
                            queryIntentActivitiesInternal = list;
                            str9 = str12;
                            int i92 = i3;
                            str13 = str8;
                            if (resolveInfo2 == null) {
                            }
                            queryIntentActivitiesInternal.add(i92, resolveInfo2);
                            resolveInfo2.specificIndex = i5;
                            i2 = i92 + 1;
                        }
                    }
                    i8 = i5 + 1;
                    str17 = str11;
                    action = str10;
                    str16 = str9;
                    intentArr2 = intentArr;
                    str18 = str13;
                }
                queryIntentActivitiesInternal = list;
                i2 = i3;
                i5 = i4;
                str13 = str8;
                str11 = str7;
                i8 = i5 + 1;
                str17 = str11;
                action = str10;
                str16 = str9;
                intentArr2 = intentArr;
                str18 = str13;
            }
            str2 = str18;
            str3 = action;
            str4 = str17;
        } else {
            str2 = "Removing duplicate item from ";
            str3 = action;
            str4 = "PackageManager";
            i2 = 0;
        }
        int size2 = queryIntentActivitiesInternal.size();
        while (i2 < size2 - 1) {
            ResolveInfo resolveInfo4 = queryIntentActivitiesInternal.get(i2);
            IntentFilter intentFilter = resolveInfo4.filter;
            if (intentFilter != null && (actionsIterator = intentFilter.actionsIterator()) != null) {
                while (actionsIterator.hasNext()) {
                    String next = actionsIterator.next();
                    if (str3 == null || !str3.equals(next)) {
                        int i10 = i2 + 1;
                        while (i10 < size2) {
                            IntentFilter intentFilter2 = queryIntentActivitiesInternal.get(i10).filter;
                            if (intentFilter2 != null && intentFilter2.hasAction(next)) {
                                queryIntentActivitiesInternal.remove(i10);
                                if (PackageManagerService.DEBUG_INTENT_MATCHING) {
                                    Log.v(str4, str2 + i10 + " due to action " + next + " at " + i2);
                                }
                                i10--;
                                size2--;
                            }
                            i10++;
                        }
                    }
                }
                if ((updateFlagsForResolve & 64) == 0) {
                    resolveInfo4.filter = null;
                    i2++;
                }
            }
            i2++;
        }
        if (componentName != null) {
            int size3 = queryIntentActivitiesInternal.size();
            int i11 = 0;
            while (true) {
                if (i11 >= size3) {
                    break;
                }
                ActivityInfo activityInfo3 = queryIntentActivitiesInternal.get(i11).activityInfo;
                if (componentName.getPackageName().equals(activityInfo3.applicationInfo.packageName) && componentName.getClassName().equals(activityInfo3.name)) {
                    queryIntentActivitiesInternal.remove(i11);
                    break;
                }
                i11++;
            }
        }
        if ((updateFlagsForResolve & 64) == 0) {
            int size4 = queryIntentActivitiesInternal.size();
            for (int i12 = 0; i12 < size4; i12++) {
                queryIntentActivitiesInternal.get(i12).filter = null;
            }
        }
        if (PackageManagerService.DEBUG_INTENT_MATCHING) {
            Log.v(str4, "Result: " + queryIntentActivitiesInternal);
        }
        return queryIntentActivitiesInternal;
    }
}
