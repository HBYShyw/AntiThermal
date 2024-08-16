package com.android.server.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.util.LogPrinter;
import android.util.PrintStreamPrinter;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.util.Xml;
import com.android.internal.util.ArrayUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.net.NetworkPolicyManagerInternal;
import com.android.server.pm.PackageManagerService;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.snapshot.PackageDataSnapshot;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PreferredActivityHelper {
    private static final String TAG_DEFAULT_APPS = "da";
    private static final String TAG_PREFERRED_BACKUP = "pa";
    private final PackageManagerService mPm;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface BlobXmlRestorer {
        void apply(TypedXmlPullParser typedXmlPullParser, int i) throws IOException, XmlPullParserException;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PreferredActivityHelper(PackageManagerService packageManagerService) {
        this.mPm = packageManagerService;
    }

    private ResolveInfo findPreferredActivityNotLocked(Computer computer, Intent intent, String str, long j, List<ResolveInfo> list, boolean z, boolean z2, boolean z3, int i) {
        return findPreferredActivityNotLocked(computer, intent, str, j, list, z, z2, z3, i, UserHandle.getAppId(Binder.getCallingUid()) >= 10000);
    }

    public ResolveInfo findPreferredActivityNotLocked(Computer computer, Intent intent, String str, long j, List<ResolveInfo> list, boolean z, boolean z2, boolean z3, int i, boolean z4) {
        if (Thread.holdsLock(this.mPm.mLock) || this.mPm.mPackageManagerServiceExt.isHoldingLockInFindPreferredActivityNotLocked()) {
            Slog.wtf("PackageManager", "Calling thread " + Thread.currentThread().getName() + " is holding mLock", new Throwable());
        }
        if (!this.mPm.mUserManager.exists(i)) {
            return null;
        }
        PackageManagerService.FindPreferredActivityBodyResult findPreferredActivityInternal = computer.findPreferredActivityInternal(intent, str, j, list, z, z2, z3, i, z4);
        if (findPreferredActivityInternal.mChanged) {
            if (PackageManagerService.DEBUG_PREFERRED) {
                Slog.v("PackageManager", "Preferred activity bookkeeping changed; writing restrictions");
            }
            this.mPm.scheduleWritePackageRestrictions(i);
        }
        if ((PackageManagerService.DEBUG_PREFERRED || z3) && findPreferredActivityInternal.mPreferredResolveInfo == null) {
            Slog.v("PackageManager", "No preferred activity to return");
        }
        return findPreferredActivityInternal.mPreferredResolveInfo;
    }

    public void clearPackagePreferredActivities(String str, int i) {
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        synchronized (this.mPm.mLock) {
            this.mPm.clearPackagePreferredActivitiesLPw(str, sparseBooleanArray, i);
        }
        if (sparseBooleanArray.size() > 0) {
            updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), sparseBooleanArray);
            this.mPm.postPreferredActivityChangedBroadcast(i);
            this.mPm.scheduleWritePackageRestrictions(i);
        }
    }

    public boolean updateDefaultHomeNotLocked(Computer computer, final int i) {
        ActivityInfo activityInfo;
        if (Thread.holdsLock(this.mPm.mLock) || this.mPm.mPackageManagerServiceExt.isHoldingLockInUpdateDefaultHomeNotLocked()) {
            Slog.wtf("PackageManager", "Calling thread " + Thread.currentThread().getName() + " is holding mLock", new Throwable());
        }
        if (!this.mPm.isSystemReady()) {
            return false;
        }
        Intent homeIntent = computer.getHomeIntent();
        ResolveInfo findPreferredActivityNotLocked = findPreferredActivityNotLocked(computer, homeIntent, null, 0L, computer.queryIntentActivitiesInternal(homeIntent, null, 786432L, i), true, false, false, i);
        String str = (findPreferredActivityNotLocked == null || (activityInfo = findPreferredActivityNotLocked.activityInfo) == null) ? null : activityInfo.packageName;
        if (TextUtils.equals(this.mPm.getActiveLauncherPackageName(i), str)) {
            return false;
        }
        String[] packagesForUid = computer.getPackagesForUid(Binder.getCallingUid());
        if ((packagesForUid == null || !ArrayUtils.contains(packagesForUid, this.mPm.mRequiredPermissionControllerPackage)) && str != null) {
            return this.mPm.setActiveLauncherPackage(str, i, new Consumer() { // from class: com.android.server.pm.PreferredActivityHelper$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PreferredActivityHelper.this.lambda$updateDefaultHomeNotLocked$0(i, (Boolean) obj);
                }
            });
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDefaultHomeNotLocked$0(int i, Boolean bool) {
        if (bool.booleanValue()) {
            this.mPm.postPreferredActivityChangedBroadcast(i);
        }
    }

    public void addPreferredActivity(Computer computer, WatchedIntentFilter watchedIntentFilter, int i, ComponentName[] componentNameArr, ComponentName componentName, boolean z, int i2, String str, boolean z2) {
        if (this.mPm.mPackageManagerServiceExt.interceptAddPreferredActivity(watchedIntentFilter.getIntentFilter(), i, componentNameArr, componentName, i2, z2)) {
            return;
        }
        int callingUid = Binder.getCallingUid();
        computer.enforceCrossUserPermission(callingUid, i2, true, false, "add preferred activity");
        if (this.mPm.mContext.checkCallingOrSelfPermission("android.permission.SET_PREFERRED_APPLICATIONS") != 0) {
            if (computer.getUidTargetSdkVersion(callingUid) < 8) {
                Slog.w("PackageManager", "Ignoring addPreferredActivity() from uid " + callingUid);
                return;
            }
            this.mPm.mContext.enforceCallingOrSelfPermission("android.permission.SET_PREFERRED_APPLICATIONS", null);
        }
        if (watchedIntentFilter.countActions() == 0) {
            Slog.w("PackageManager", "Cannot set a preferred activity with no filter actions");
            return;
        }
        this.mPm.mPackageManagerServiceExt.beforeAddInAddPreferredActivityInternal(componentName, watchedIntentFilter, i2);
        if (PackageManagerService.DEBUG_PREFERRED) {
            Slog.i("PackageManager", str + " activity " + componentName.flattenToShortString() + " for user " + i2 + ":");
            watchedIntentFilter.dump(new LogPrinter(4, "PackageManager"), "  ");
        }
        synchronized (this.mPm.mLock) {
            PreferredIntentResolver editPreferredActivitiesLPw = this.mPm.mSettings.editPreferredActivitiesLPw(i2);
            ArrayList<PreferredActivity> findFilters = editPreferredActivitiesLPw.findFilters(watchedIntentFilter);
            if (z2 && findFilters != null) {
                Settings.removeFilters(editPreferredActivitiesLPw, watchedIntentFilter, findFilters);
            }
            editPreferredActivitiesLPw.addFilter((PackageDataSnapshot) this.mPm.snapshotComputer(), (Computer) new PreferredActivity(watchedIntentFilter, i, componentNameArr, componentName, z));
            this.mPm.scheduleWritePackageRestrictions(i2);
        }
        if (isHomeFilter(watchedIntentFilter) && updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), i2)) {
            return;
        }
        this.mPm.postPreferredActivityChangedBroadcast(i2);
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x01d7 A[Catch: all -> 0x01fb, TryCatch #1 {, blocks: (B:33:0x0091, B:35:0x009b, B:37:0x00a1, B:39:0x00a7, B:41:0x00b5, B:43:0x00ce, B:44:0x00d7, B:45:0x0175, B:47:0x017b, B:49:0x0185, B:51:0x018d, B:53:0x0195, B:55:0x0199, B:56:0x01cc, B:59:0x01d7, B:60:0x01df), top: B:32:0x0091 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void replacePreferredActivity(Computer computer, WatchedIntentFilter watchedIntentFilter, int i, ComponentName[] componentNameArr, ComponentName componentName, int i2) {
        if (this.mPm.mPackageManagerServiceExt.interceptReplacePreferredActivity(watchedIntentFilter.getIntentFilter())) {
            return;
        }
        if (watchedIntentFilter.countActions() != 1) {
            throw new IllegalArgumentException("replacePreferredActivity expects filter to have only 1 action.");
        }
        if (watchedIntentFilter.countDataAuthorities() != 0 || watchedIntentFilter.countDataPaths() != 0 || watchedIntentFilter.countDataSchemes() > 1 || watchedIntentFilter.countDataTypes() != 0) {
            throw new IllegalArgumentException("replacePreferredActivity expects filter to have no data authorities, paths, or types; and at most one scheme.");
        }
        int callingUid = Binder.getCallingUid();
        computer.enforceCrossUserPermission(callingUid, i2, true, false, "replace preferred activity");
        if (this.mPm.mContext.checkCallingOrSelfPermission("android.permission.SET_PREFERRED_APPLICATIONS") != 0) {
            synchronized (this.mPm.mLock) {
                if (this.mPm.snapshotComputer().getUidTargetSdkVersion(callingUid) < 8) {
                    Slog.w("PackageManager", "Ignoring replacePreferredActivity() from uid " + Binder.getCallingUid());
                    return;
                }
                this.mPm.mContext.enforceCallingOrSelfPermission("android.permission.SET_PREFERRED_APPLICATIONS", null);
            }
        }
        synchronized (this.mPm.mLock) {
            PreferredIntentResolver preferredActivities = this.mPm.mSettings.getPreferredActivities(i2);
            if (preferredActivities != null) {
                ArrayList<PreferredActivity> findFilters = preferredActivities.findFilters(watchedIntentFilter);
                if (findFilters != null && findFilters.size() == 1) {
                    PreferredActivity preferredActivity = findFilters.get(0);
                    if (PackageManagerService.DEBUG_PREFERRED) {
                        Slog.i("PackageManager", "Checking replace of preferred:");
                        watchedIntentFilter.dump(new LogPrinter(4, "PackageManager"), "  ");
                        if (!preferredActivity.mPref.mAlways) {
                            Slog.i("PackageManager", "  -- CUR; not mAlways!");
                        } else {
                            Slog.i("PackageManager", "  -- CUR: mMatch=" + preferredActivity.mPref.mMatch);
                            Slog.i("PackageManager", "  -- CUR: mSet=" + Arrays.toString(preferredActivity.mPref.mSetComponents));
                            Slog.i("PackageManager", "  -- CUR: mComponent=" + preferredActivity.mPref.mShortComponent);
                            Slog.i("PackageManager", "  -- NEW: mMatch=" + (i & 268369920));
                            Slog.i("PackageManager", "  -- CUR: mSet=" + Arrays.toString(componentNameArr));
                            Slog.i("PackageManager", "  -- CUR: mComponent=" + componentName.flattenToShortString());
                        }
                    }
                    PreferredComponent preferredComponent = preferredActivity.mPref;
                    if (preferredComponent.mAlways) {
                        if (preferredComponent.mComponent.equals(componentName)) {
                            PreferredComponent preferredComponent2 = preferredActivity.mPref;
                            if (preferredComponent2.mMatch == (i & 268369920)) {
                                if (preferredComponent2.sameSet(componentNameArr)) {
                                    if (PackageManagerService.DEBUG_PREFERRED) {
                                        Slog.i("PackageManager", "Replacing with same preferred activity " + preferredActivity.mPref.mShortComponent + " for user " + i2 + ":");
                                        watchedIntentFilter.dump(new LogPrinter(4, "PackageManager"), "  ");
                                    }
                                    return;
                                }
                                if (findFilters != null) {
                                    Settings.removeFilters(preferredActivities, watchedIntentFilter, findFilters);
                                }
                            }
                        }
                        if (findFilters != null) {
                        }
                    }
                }
                if (findFilters != null) {
                }
            }
            addPreferredActivity(this.mPm.snapshotComputer(), watchedIntentFilter, i, componentNameArr, componentName, true, i2, "Replacing preferred", false);
        }
    }

    public void clearPackagePreferredActivities(Computer computer, String str) {
        int callingUid = Binder.getCallingUid();
        if (computer.getInstantAppPackageName(callingUid) != null) {
            return;
        }
        PackageStateInternal packageStateInternal = computer.getPackageStateInternal(str);
        if ((packageStateInternal == null || !computer.isCallerSameApp(str, callingUid)) && this.mPm.mContext.checkCallingOrSelfPermission("android.permission.SET_PREFERRED_APPLICATIONS") != 0) {
            if (computer.getUidTargetSdkVersion(callingUid) < 8) {
                Slog.w("PackageManager", "Ignoring clearPackagePreferredActivities() from uid " + callingUid);
                return;
            }
            this.mPm.mContext.enforceCallingOrSelfPermission("android.permission.SET_PREFERRED_APPLICATIONS", null);
        }
        if (packageStateInternal == null || !computer.shouldFilterApplication(packageStateInternal, callingUid, UserHandle.getUserId(callingUid))) {
            clearPackagePreferredActivities(str, UserHandle.getCallingUserId());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateDefaultHomeNotLocked(Computer computer, SparseBooleanArray sparseBooleanArray) {
        if (Thread.holdsLock(this.mPm.mLock) || this.mPm.mPackageManagerServiceExt.isHoldingLockInUpdateDefaultHomeNotLockedMulti()) {
            Slog.wtf("PackageManager", "Calling thread " + Thread.currentThread().getName() + " is holding mLock", new Throwable());
        }
        for (int size = sparseBooleanArray.size() - 1; size >= 0; size--) {
            updateDefaultHomeNotLocked(computer, sparseBooleanArray.keyAt(size));
        }
    }

    public void setHomeActivity(Computer computer, ComponentName componentName, int i) {
        if (computer.getInstantAppPackageName(Binder.getCallingUid()) != null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        computer.getHomeActivitiesAsUser(arrayList, i);
        int size = arrayList.size();
        ComponentName[] componentNameArr = new ComponentName[size];
        boolean z = false;
        for (int i2 = 0; i2 < size; i2++) {
            ActivityInfo activityInfo = ((ResolveInfo) arrayList.get(i2)).activityInfo;
            ComponentName componentName2 = new ComponentName(activityInfo.packageName, activityInfo.name);
            componentNameArr[i2] = componentName2;
            if (!z && componentName2.equals(componentName)) {
                z = true;
            }
        }
        if (!z) {
            throw new IllegalArgumentException("Component " + componentName + " cannot be home on user " + i);
        }
        replacePreferredActivity(computer, getHomeFilter(), 1048576, componentNameArr, componentName, i);
    }

    private WatchedIntentFilter getHomeFilter() {
        WatchedIntentFilter watchedIntentFilter = new WatchedIntentFilter("android.intent.action.MAIN");
        watchedIntentFilter.addCategory("android.intent.category.HOME");
        watchedIntentFilter.addCategory("android.intent.category.DEFAULT");
        return watchedIntentFilter;
    }

    public void addPersistentPreferredActivity(WatchedIntentFilter watchedIntentFilter, ComponentName componentName, int i) {
        int callingUid = Binder.getCallingUid();
        if (callingUid != 1000) {
            throw new SecurityException("addPersistentPreferredActivity can only be run by the system");
        }
        if (!watchedIntentFilter.checkDataPathAndSchemeSpecificParts()) {
            EventLog.writeEvent(1397638484, "246749702", Integer.valueOf(callingUid));
            throw new IllegalArgumentException("Invalid intent data paths or scheme specific parts in the filter.");
        }
        if (watchedIntentFilter.countActions() == 0) {
            Slog.w("PackageManager", "Cannot set a preferred activity with no filter actions");
            return;
        }
        if (PackageManagerService.DEBUG_PREFERRED) {
            Slog.i("PackageManager", "Adding persistent preferred activity " + componentName + " for user " + i + ":");
            watchedIntentFilter.dump(new LogPrinter(4, "PackageManager"), "  ");
        }
        synchronized (this.mPm.mLock) {
            this.mPm.mSettings.editPersistentPreferredActivitiesLPw(i).addFilter((PackageDataSnapshot) this.mPm.snapshotComputer(), (Computer) new PersistentPreferredActivity(watchedIntentFilter, componentName, true));
            this.mPm.scheduleWritePackageRestrictions(i);
        }
        if (isHomeFilter(watchedIntentFilter)) {
            updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), i);
        }
        this.mPm.postPreferredActivityChangedBroadcast(i);
    }

    public void clearPackagePersistentPreferredActivities(String str, int i) {
        boolean clearPackagePersistentPreferredActivities;
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("clearPackagePersistentPreferredActivities can only be run by the system");
        }
        synchronized (this.mPm.mLock) {
            clearPackagePersistentPreferredActivities = this.mPm.mSettings.clearPackagePersistentPreferredActivities(str, i);
        }
        if (clearPackagePersistentPreferredActivities) {
            updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), i);
            this.mPm.postPreferredActivityChangedBroadcast(i);
            this.mPm.scheduleWritePackageRestrictions(i);
        }
    }

    public void clearPersistentPreferredActivity(IntentFilter intentFilter, int i) {
        boolean clearPersistentPreferredActivity;
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("clearPersistentPreferredActivity can only be run by the system");
        }
        synchronized (this.mPm.mLock) {
            clearPersistentPreferredActivity = this.mPm.mSettings.clearPersistentPreferredActivity(intentFilter, i);
        }
        if (clearPersistentPreferredActivity) {
            updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), i);
            this.mPm.postPreferredActivityChangedBroadcast(i);
            this.mPm.scheduleWritePackageRestrictions(i);
        }
    }

    private boolean isHomeFilter(WatchedIntentFilter watchedIntentFilter) {
        return watchedIntentFilter.hasAction("android.intent.action.MAIN") && watchedIntentFilter.hasCategory("android.intent.category.HOME") && watchedIntentFilter.hasCategory("android.intent.category.DEFAULT");
    }

    private void restoreFromXml(TypedXmlPullParser typedXmlPullParser, int i, String str, BlobXmlRestorer blobXmlRestorer) throws IOException, XmlPullParserException {
        int next;
        do {
            next = typedXmlPullParser.next();
            if (next == 2) {
                break;
            }
        } while (next != 1);
        if (next != 2) {
            if (PackageManagerService.DEBUG_BACKUP) {
                Slog.e("PackageManager", "Didn't find start tag during restore");
                return;
            }
            return;
        }
        if (!str.equals(typedXmlPullParser.getName())) {
            if (PackageManagerService.DEBUG_BACKUP) {
                Slog.e("PackageManager", "Found unexpected tag " + typedXmlPullParser.getName());
                return;
            }
            return;
        }
        do {
        } while (typedXmlPullParser.next() == 4);
        blobXmlRestorer.apply(typedXmlPullParser, i);
    }

    public byte[] getPreferredActivityBackup(int i) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("Only the system may call getPreferredActivityBackup()");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            TypedXmlSerializer newFastSerializer = Xml.newFastSerializer();
            newFastSerializer.setOutput(byteArrayOutputStream, StandardCharsets.UTF_8.name());
            newFastSerializer.startDocument((String) null, Boolean.TRUE);
            newFastSerializer.startTag((String) null, TAG_PREFERRED_BACKUP);
            synchronized (this.mPm.mLock) {
                this.mPm.mSettings.writePreferredActivitiesLPr(newFastSerializer, i, true);
            }
            newFastSerializer.endTag((String) null, TAG_PREFERRED_BACKUP);
            newFastSerializer.endDocument();
            newFastSerializer.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            if (PackageManagerService.DEBUG_BACKUP) {
                Slog.e("PackageManager", "Unable to write preferred activities for backup", e);
            }
            return null;
        }
    }

    public void restorePreferredActivities(byte[] bArr, int i) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("Only the system may call restorePreferredActivities()");
        }
        try {
            TypedXmlPullParser newFastPullParser = Xml.newFastPullParser();
            newFastPullParser.setInput(new ByteArrayInputStream(bArr), StandardCharsets.UTF_8.name());
            restoreFromXml(newFastPullParser, i, TAG_PREFERRED_BACKUP, new BlobXmlRestorer() { // from class: com.android.server.pm.PreferredActivityHelper$$ExternalSyntheticLambda0
                @Override // com.android.server.pm.PreferredActivityHelper.BlobXmlRestorer
                public final void apply(TypedXmlPullParser typedXmlPullParser, int i2) {
                    PreferredActivityHelper.this.lambda$restorePreferredActivities$1(typedXmlPullParser, i2);
                }
            });
        } catch (Exception e) {
            if (PackageManagerService.DEBUG_BACKUP) {
                Slog.e("PackageManager", "Exception restoring preferred activities: " + e.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restorePreferredActivities$1(TypedXmlPullParser typedXmlPullParser, int i) throws IOException, XmlPullParserException {
        synchronized (this.mPm.mLock) {
            this.mPm.mSettings.readPreferredActivitiesLPw(typedXmlPullParser, i);
        }
        updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), i);
    }

    public byte[] getDefaultAppsBackup(int i) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("Only the system may call getDefaultAppsBackup()");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            XmlSerializer newFastSerializer = Xml.newFastSerializer();
            newFastSerializer.setOutput(byteArrayOutputStream, StandardCharsets.UTF_8.name());
            newFastSerializer.startDocument((String) null, Boolean.TRUE);
            newFastSerializer.startTag((String) null, TAG_DEFAULT_APPS);
            synchronized (this.mPm.mLock) {
                this.mPm.mSettings.writeDefaultAppsLPr(newFastSerializer, i);
            }
            newFastSerializer.endTag((String) null, TAG_DEFAULT_APPS);
            newFastSerializer.endDocument();
            newFastSerializer.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            if (PackageManagerService.DEBUG_BACKUP) {
                Slog.e("PackageManager", "Unable to write default apps for backup", e);
            }
            return null;
        }
    }

    public void restoreDefaultApps(byte[] bArr, int i) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("Only the system may call restoreDefaultApps()");
        }
        try {
            TypedXmlPullParser newFastPullParser = Xml.newFastPullParser();
            newFastPullParser.setInput(new ByteArrayInputStream(bArr), StandardCharsets.UTF_8.name());
            restoreFromXml(newFastPullParser, i, TAG_DEFAULT_APPS, new BlobXmlRestorer() { // from class: com.android.server.pm.PreferredActivityHelper$$ExternalSyntheticLambda2
                @Override // com.android.server.pm.PreferredActivityHelper.BlobXmlRestorer
                public final void apply(TypedXmlPullParser typedXmlPullParser, int i2) {
                    PreferredActivityHelper.this.lambda$restoreDefaultApps$2(typedXmlPullParser, i2);
                }
            });
        } catch (Exception e) {
            if (PackageManagerService.DEBUG_BACKUP) {
                Slog.e("PackageManager", "Exception restoring default apps: " + e.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restoreDefaultApps$2(TypedXmlPullParser typedXmlPullParser, int i) throws IOException, XmlPullParserException {
        String removeDefaultBrowserPackageNameLPw;
        synchronized (this.mPm.mLock) {
            this.mPm.mSettings.readDefaultAppsLPw(typedXmlPullParser, i);
            removeDefaultBrowserPackageNameLPw = this.mPm.mSettings.removeDefaultBrowserPackageNameLPw(i);
        }
        if (removeDefaultBrowserPackageNameLPw != null) {
            this.mPm.setDefaultBrowser(removeDefaultBrowserPackageNameLPw, false, i);
        }
    }

    public void resetApplicationPreferences(int i) {
        this.mPm.mContext.enforceCallingOrSelfPermission("android.permission.SET_PREFERRED_APPLICATIONS", null);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
            synchronized (this.mPm.mLock) {
                this.mPm.clearPackagePreferredActivitiesLPw(null, sparseBooleanArray, i);
            }
            if (sparseBooleanArray.size() > 0) {
                this.mPm.postPreferredActivityChangedBroadcast(i);
            }
            synchronized (this.mPm.mLock) {
                this.mPm.mSettings.applyDefaultPreferredAppsLPw(i);
                this.mPm.mDomainVerificationManager.clearUser(i);
                this.mPm.mPermissionManager.resetRuntimePermissionsForUser(i);
            }
            updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), i);
            resetNetworkPolicies(i);
            this.mPm.scheduleWritePackageRestrictions(i);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void resetNetworkPolicies(int i) {
        ((NetworkPolicyManagerInternal) this.mPm.mInjector.getLocalService(NetworkPolicyManagerInternal.class)).resetUserState(i);
    }

    public int getPreferredActivities(Computer computer, List<IntentFilter> list, List<ComponentName> list2, String str) {
        List<WatchedIntentFilter> watchedIntentFilterList = WatchedIntentFilter.toWatchedIntentFilterList(list);
        int preferredActivitiesInternal = getPreferredActivitiesInternal(computer, watchedIntentFilterList, list2, str);
        list.clear();
        for (int i = 0; i < watchedIntentFilterList.size(); i++) {
            list.add(watchedIntentFilterList.get(i).getIntentFilter());
        }
        return preferredActivitiesInternal;
    }

    private int getPreferredActivitiesInternal(Computer computer, List<WatchedIntentFilter> list, List<ComponentName> list2, String str) {
        int callingUserId;
        PreferredIntentResolver preferredActivities;
        int callingUid = Binder.getCallingUid();
        if (computer.getInstantAppPackageName(callingUid) == null && (preferredActivities = computer.getPreferredActivities((callingUserId = UserHandle.getCallingUserId()))) != null) {
            Iterator filterIterator = preferredActivities.filterIterator();
            while (filterIterator.hasNext()) {
                PreferredActivity preferredActivity = (PreferredActivity) filterIterator.next();
                String packageName = preferredActivity.mPref.mComponent.getPackageName();
                if (str == null || (packageName.equals(str) && preferredActivity.mPref.mAlways)) {
                    if (!computer.shouldFilterApplication(computer.getPackageStateInternal(packageName), callingUid, callingUserId)) {
                        if (list != null) {
                            list.add(new WatchedIntentFilter(preferredActivity.getIntentFilter()));
                        }
                        if (list2 != null) {
                            list2.add(preferredActivity.mPref.mComponent);
                        }
                    }
                }
            }
        }
        return 0;
    }

    public ResolveInfo findPersistentPreferredActivity(Computer computer, Intent intent, int i) {
        if (!UserHandle.isSameApp(Binder.getCallingUid(), 1000)) {
            throw new SecurityException("findPersistentPreferredActivity can only be run by the system");
        }
        if (!this.mPm.mUserManager.exists(i)) {
            return null;
        }
        int callingUid = Binder.getCallingUid();
        Intent updateIntentForResolve = PackageManagerServiceUtils.updateIntentForResolve(intent);
        String resolveTypeIfNeeded = updateIntentForResolve.resolveTypeIfNeeded(this.mPm.mContext.getContentResolver());
        long updateFlagsForResolve = computer.updateFlagsForResolve(0L, i, callingUid, false, computer.isImplicitImageCaptureIntentAndNotSetByDpc(updateIntentForResolve, i, resolveTypeIfNeeded, 0L));
        return computer.findPersistentPreferredActivity(updateIntentForResolve, resolveTypeIfNeeded, updateFlagsForResolve, computer.queryIntentActivitiesInternal(updateIntentForResolve, resolveTypeIfNeeded, updateFlagsForResolve, i), false, i);
    }

    public void setLastChosenActivity(Computer computer, Intent intent, String str, int i, WatchedIntentFilter watchedIntentFilter, int i2, ComponentName componentName) {
        if (computer.getInstantAppPackageName(Binder.getCallingUid()) != null) {
            return;
        }
        int callingUserId = UserHandle.getCallingUserId();
        if (PackageManagerService.DEBUG_PREFERRED) {
            Log.v("PackageManager", "setLastChosenActivity intent=" + intent + " resolvedType=" + str + " flags=" + i + " filter=" + watchedIntentFilter + " match=" + i2 + " activity=" + componentName);
            watchedIntentFilter.dump(new PrintStreamPrinter(System.out), "    ");
        }
        intent.setComponent(null);
        long j = i;
        findPreferredActivityNotLocked(computer, intent, str, j, computer.queryIntentActivitiesInternal(intent, str, j, callingUserId), false, true, false, callingUserId);
        addPreferredActivity(computer, watchedIntentFilter, i2, null, componentName, false, callingUserId, "Setting last chosen", false);
    }

    public ResolveInfo getLastChosenActivity(Computer computer, Intent intent, String str, int i) {
        if (computer.getInstantAppPackageName(Binder.getCallingUid()) != null) {
            return null;
        }
        int callingUserId = UserHandle.getCallingUserId();
        if (PackageManagerService.DEBUG_PREFERRED) {
            Log.v("PackageManager", "Querying last chosen activity for " + intent);
        }
        long j = i;
        return findPreferredActivityNotLocked(computer, intent, str, j, computer.queryIntentActivitiesInternal(intent, str, j, callingUserId), false, false, false, callingUserId);
    }
}
