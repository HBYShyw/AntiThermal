package com.android.server.uri;

import android.annotation.RequiresPermission;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.AppGlobals;
import android.app.GrantedUriPermission;
import android.app.IUriGrantsManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ParceledListSlice;
import android.content.pm.PathPermission;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PatternMatcher;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.IoThread;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.SystemServiceManager;
import com.android.server.job.controllers.JobStatus;
import com.android.server.pm.verify.domain.DomainVerificationLegacySettings;
import com.android.server.slice.SliceClientPermissions;
import com.android.server.uri.UriMetricsHelper;
import com.android.server.uri.UriPermission;
import com.android.server.usage.IntervalStats;
import com.google.android.collect.Lists;
import com.google.android.collect.Maps;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UriGrantsManagerService extends IUriGrantsManager.Stub implements UriMetricsHelper.PersistentUriGrantsProvider {
    private static final String ATTR_CREATED_TIME = "createdTime";
    private static final String ATTR_MODE_FLAGS = "modeFlags";
    private static final String ATTR_PREFIX = "prefix";
    private static final String ATTR_SOURCE_PKG = "sourcePkg";
    private static final String ATTR_SOURCE_USER_ID = "sourceUserId";
    private static final String ATTR_TARGET_PKG = "targetPkg";
    private static final String ATTR_TARGET_USER_ID = "targetUserId";
    private static final String ATTR_URI = "uri";
    private static final String ATTR_USER_HANDLE = "userHandle";
    private static final boolean DEBUG = false;
    private static final boolean ENABLE_DYNAMIC_PERMISSIONS = true;
    private static final int MAX_PERSISTED_URI_GRANTS = 512;
    private static final String TAG = "UriGrantsManagerService";
    private static final String TAG_URI_GRANT = "uri-grant";
    private static final String TAG_URI_GRANTS = "uri-grants";
    ActivityManagerInternal mAmInternal;
    private final AtomicFile mGrantFile;

    @GuardedBy({"mLock"})
    private final SparseArray<ArrayMap<GrantUri, UriPermission>> mGrantedUriPermissions;
    private final H mH;
    private final Object mLock;
    UriMetricsHelper mMetricsHelper;
    PackageManagerInternal mPmInternal;
    private IUriGrantsManagerServiceExt mUriGrantsManagerServiceExt;

    private UriGrantsManagerService() {
        this(SystemServiceManager.ensureSystemDir());
    }

    private UriGrantsManagerService(File file) {
        this.mLock = new Object();
        this.mUriGrantsManagerServiceExt = (IUriGrantsManagerServiceExt) ExtLoader.type(IUriGrantsManagerServiceExt.class).base(this).create();
        this.mGrantedUriPermissions = new SparseArray<>();
        this.mH = new H(IoThread.get().getLooper());
        this.mGrantFile = new AtomicFile(new File(file, "urigrants.xml"), TAG_URI_GRANTS);
    }

    @VisibleForTesting
    static UriGrantsManagerService createForTest(File file) {
        UriGrantsManagerService uriGrantsManagerService = new UriGrantsManagerService(file);
        uriGrantsManagerService.mAmInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        uriGrantsManagerService.mPmInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        return uriGrantsManagerService;
    }

    @VisibleForTesting
    UriGrantsManagerInternal getLocalService() {
        return new LocalService();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void start() {
        LocalServices.addService(UriGrantsManagerInternal.class, new LocalService());
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Lifecycle extends SystemService {
        private final Context mContext;
        private final UriGrantsManagerService mService;

        public Lifecycle(Context context) {
            super(context);
            this.mContext = context;
            this.mService = new UriGrantsManagerService();
        }

        public void onStart() {
            publishBinderService("uri_grants", this.mService);
            this.mService.mMetricsHelper = new UriMetricsHelper(this.mContext, this.mService);
            this.mService.start();
        }

        public void onBootPhase(int i) {
            if (i == 500) {
                this.mService.mAmInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
                this.mService.mPmInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
                this.mService.mMetricsHelper.registerPuller();
            }
        }

        public UriGrantsManagerService getService() {
            return this.mService;
        }
    }

    private int checkUidPermission(String str, int i) {
        try {
            return AppGlobals.getPackageManager().checkUidPermission(str, i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void grantUriPermissionFromOwner(IBinder iBinder, int i, String str, Uri uri, int i2, int i3, int i4) {
        grantUriPermissionFromOwnerUnlocked(iBinder, i, str, uri, i2, i3, i4);
    }

    private void grantUriPermissionFromOwnerUnlocked(IBinder iBinder, int i, String str, Uri uri, int i2, int i3, int i4) {
        int handleIncomingUser = this.mAmInternal.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i4, false, 2, "grantUriPermissionFromOwner", (String) null);
        UriPermissionOwner fromExternalToken = UriPermissionOwner.fromExternalToken(iBinder);
        if (fromExternalToken == null) {
            throw new IllegalArgumentException("Unknown owner: " + iBinder);
        }
        if (i != Binder.getCallingUid() && Binder.getCallingUid() != Process.myUid()) {
            throw new SecurityException("nice try");
        }
        if (str == null) {
            throw new IllegalArgumentException("null target");
        }
        if (uri == null) {
            throw new IllegalArgumentException("null uri");
        }
        grantUriPermissionUnlocked(i, str, new GrantUri(i3, uri, i2), i2, fromExternalToken, handleIncomingUser);
    }

    public ParceledListSlice<android.content.UriPermission> getUriPermissions(String str, boolean z, boolean z2) {
        enforceNotIsolatedCaller("getUriPermissions");
        Objects.requireNonNull(str, DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        int callingUid = Binder.getCallingUid();
        if (((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).getPackageUid(str, 786432L, UserHandle.getUserId(callingUid)) != callingUid) {
            throw new SecurityException("Package " + str + " does not belong to calling UID " + callingUid);
        }
        ArrayList newArrayList = Lists.newArrayList();
        synchronized (this.mLock) {
            if (z) {
                ArrayMap<GrantUri, UriPermission> arrayMap = this.mGrantedUriPermissions.get(callingUid);
                if (arrayMap == null) {
                    Slog.w(TAG, "No permission grants found for " + str);
                } else {
                    for (int i = 0; i < arrayMap.size(); i++) {
                        UriPermission valueAt = arrayMap.valueAt(i);
                        if (str.equals(valueAt.targetPkg) && (!z2 || valueAt.persistedModeFlags != 0)) {
                            newArrayList.add(valueAt.buildPersistedPublicApiObject());
                        }
                    }
                }
            } else {
                int size = this.mGrantedUriPermissions.size();
                for (int i2 = 0; i2 < size; i2++) {
                    ArrayMap<GrantUri, UriPermission> valueAt2 = this.mGrantedUriPermissions.valueAt(i2);
                    for (int i3 = 0; i3 < valueAt2.size(); i3++) {
                        UriPermission valueAt3 = valueAt2.valueAt(i3);
                        if (str.equals(valueAt3.sourcePkg) && (!z2 || valueAt3.persistedModeFlags != 0)) {
                            newArrayList.add(valueAt3.buildPersistedPublicApiObject());
                        }
                    }
                }
            }
        }
        return new ParceledListSlice<>(newArrayList);
    }

    public ParceledListSlice<GrantedUriPermission> getGrantedUriPermissions(String str, int i) {
        this.mAmInternal.enforceCallingPermission("android.permission.GET_APP_GRANTED_URI_PERMISSIONS", "getGrantedUriPermissions");
        ArrayList arrayList = new ArrayList();
        synchronized (this.mLock) {
            int size = this.mGrantedUriPermissions.size();
            for (int i2 = 0; i2 < size; i2++) {
                ArrayMap<GrantUri, UriPermission> valueAt = this.mGrantedUriPermissions.valueAt(i2);
                for (int i3 = 0; i3 < valueAt.size(); i3++) {
                    UriPermission valueAt2 = valueAt.valueAt(i3);
                    if ((str == null || str.equals(valueAt2.targetPkg)) && valueAt2.targetUserId == i && valueAt2.persistedModeFlags != 0) {
                        arrayList.add(valueAt2.buildGrantedUriPermission());
                    }
                }
            }
        }
        return new ParceledListSlice<>(arrayList);
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0079, code lost:
    
        r2 = false | r1.takePersistableModes(r8);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void takePersistableUriPermission(Uri uri, int i, String str, int i2) {
        int callingUid;
        if (str != null) {
            this.mAmInternal.enforceCallingPermission("android.permission.FORCE_PERSISTABLE_URI_PERMISSIONS", "takePersistableUriPermission");
            callingUid = this.mPmInternal.getPackageUid(str, 0L, i2);
        } else {
            enforceNotIsolatedCaller("takePersistableUriPermission");
            callingUid = Binder.getCallingUid();
        }
        Preconditions.checkFlagsArgument(i, 3);
        synchronized (this.mLock) {
            boolean z = false;
            UriPermission findUriPermissionLocked = findUriPermissionLocked(callingUid, new GrantUri(i2, uri, 0));
            UriPermission findUriPermissionLocked2 = findUriPermissionLocked(callingUid, new GrantUri(i2, uri, 128));
            boolean z2 = true;
            boolean z3 = findUriPermissionLocked != null && (findUriPermissionLocked.persistableModeFlags & i) == i;
            if (findUriPermissionLocked2 == null || (findUriPermissionLocked2.persistableModeFlags & i) != i) {
                z2 = false;
            }
            if (!z3 && !z2) {
                throw new SecurityException("No persistable permission grants found for UID " + callingUid + " and Uri " + uri.toSafeString());
            }
            if (z2) {
                z |= findUriPermissionLocked2.takePersistableModes(i);
            }
            if (maybePrunePersistedUriGrantsLocked(callingUid) | z) {
                schedulePersistUriGrants();
            }
        }
    }

    public void clearGrantedUriPermissions(String str, int i) {
        this.mAmInternal.enforceCallingPermission("android.permission.CLEAR_APP_GRANTED_URI_PERMISSIONS", "clearGrantedUriPermissions");
        synchronized (this.mLock) {
            removeUriPermissionsForPackageLocked(str, i, true, true);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0067, code lost:
    
        r3 = false | r2.releasePersistableModes(r8);
        removeUriPermissionIfNeededLocked(r2);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void releasePersistableUriPermission(Uri uri, int i, String str, int i2) {
        int callingUid;
        if (str != null) {
            this.mAmInternal.enforceCallingPermission("android.permission.FORCE_PERSISTABLE_URI_PERMISSIONS", "releasePersistableUriPermission");
            callingUid = this.mPmInternal.getPackageUid(str, 0L, i2);
        } else {
            enforceNotIsolatedCaller("releasePersistableUriPermission");
            callingUid = Binder.getCallingUid();
        }
        Preconditions.checkFlagsArgument(i, 3);
        synchronized (this.mLock) {
            boolean z = false;
            UriPermission findUriPermissionLocked = findUriPermissionLocked(callingUid, new GrantUri(i2, uri, 0));
            UriPermission findUriPermissionLocked2 = findUriPermissionLocked(callingUid, new GrantUri(i2, uri, 128));
            if (findUriPermissionLocked == null && findUriPermissionLocked2 == null && str == null) {
                throw new SecurityException("No permission grants found for UID " + callingUid + " and Uri " + uri.toSafeString());
            }
            if (findUriPermissionLocked2 != null) {
                z |= findUriPermissionLocked2.releasePersistableModes(i);
                removeUriPermissionIfNeededLocked(findUriPermissionLocked2);
            }
            if (z) {
                schedulePersistUriGrants();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void removeUriPermissionsForPackageLocked(String str, int i, boolean z, boolean z2) {
        if (i == -1 && str == null) {
            throw new IllegalArgumentException("Must narrow by either package or user");
        }
        int size = this.mGrantedUriPermissions.size();
        int i2 = 0;
        boolean z3 = false;
        while (i2 < size) {
            int keyAt = this.mGrantedUriPermissions.keyAt(i2);
            ArrayMap<GrantUri, UriPermission> valueAt = this.mGrantedUriPermissions.valueAt(i2);
            if (i == -1 || i == UserHandle.getUserId(keyAt)) {
                Iterator<UriPermission> it = valueAt.values().iterator();
                while (it.hasNext()) {
                    UriPermission next = it.next();
                    if (str == null || ((!z2 && next.sourcePkg.equals(str)) || next.targetPkg.equals(str))) {
                        if (!"downloads".equals(next.uri.uri.getAuthority()) || z) {
                            z3 |= next.revokeModes(z ? -1 : -65, true);
                            if (next.modeFlags == 0) {
                                it.remove();
                            }
                        }
                    }
                }
                if (valueAt.isEmpty()) {
                    this.mGrantedUriPermissions.remove(keyAt);
                    size--;
                    i2--;
                }
            }
            i2++;
        }
        if (z3) {
            schedulePersistUriGrants();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public boolean checkAuthorityGrantsLocked(int i, ProviderInfo providerInfo, int i2, boolean z) {
        if (providerInfo != null && this.mUriGrantsManagerServiceExt.needChangeUid(this.mGrantedUriPermissions, providerInfo.authority, i) && 999 == UserHandle.getUserId(i)) {
            i = UserHandle.getUid(0, UserHandle.getAppId(i));
            i2 = 0;
        }
        ArrayMap<GrantUri, UriPermission> arrayMap = this.mGrantedUriPermissions.get(i);
        if (arrayMap != null) {
            for (int size = arrayMap.size() - 1; size >= 0; size--) {
                GrantUri keyAt = arrayMap.keyAt(size);
                if ((keyAt.sourceUserId == i2 || !z) && matchesProvider(keyAt.uri, providerInfo)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesProvider(Uri uri, ProviderInfo providerInfo) {
        String authority = uri.getAuthority();
        String str = providerInfo.authority;
        if (str.indexOf(59) == -1) {
            return str.equals(authority);
        }
        for (String str2 : str.split(";")) {
            if (str2.equals(authority)) {
                return true;
            }
        }
        return false;
    }

    @GuardedBy({"mLock"})
    private boolean maybePrunePersistedUriGrantsLocked(int i) {
        ArrayMap<GrantUri, UriPermission> arrayMap = this.mGrantedUriPermissions.get(i);
        if (arrayMap == null || arrayMap.size() < 512) {
            return false;
        }
        ArrayList newArrayList = Lists.newArrayList();
        for (UriPermission uriPermission : arrayMap.values()) {
            if (uriPermission.persistedModeFlags != 0) {
                newArrayList.add(uriPermission);
            }
        }
        int size = newArrayList.size() - 512;
        if (size <= 0) {
            return false;
        }
        Collections.sort(newArrayList, new UriPermission.PersistedTimeComparator());
        for (int i2 = 0; i2 < size; i2++) {
            UriPermission uriPermission2 = (UriPermission) newArrayList.get(i2);
            uriPermission2.releasePersistableModes(-1);
            removeUriPermissionIfNeededLocked(uriPermission2);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NeededUriGrants checkGrantUriPermissionFromIntentUnlocked(int i, String str, Intent intent, int i2, NeededUriGrants neededUriGrants, int i3) {
        int i4;
        NeededUriGrants checkGrantUriPermissionFromIntentUnlocked;
        NeededUriGrants neededUriGrants2 = neededUriGrants;
        if (str == null) {
            throw new NullPointerException(ATTR_TARGET_PKG);
        }
        if (intent == null) {
            return null;
        }
        Uri data = intent.getData();
        ClipData clipData = intent.getClipData();
        if (data == null && clipData == null) {
            return null;
        }
        int contentUserHint = intent.getContentUserHint();
        if (contentUserHint == -2) {
            contentUserHint = UserHandle.getUserId(i);
        }
        if (neededUriGrants2 != null) {
            i4 = neededUriGrants2.targetUid;
        } else {
            int packageUid = this.mPmInternal.getPackageUid(str, 268435456L, i3);
            if (packageUid < 0) {
                return null;
            }
            i4 = packageUid;
        }
        if (data != null) {
            int changeUserIdInUriGrantsManagerService = this.mUriGrantsManagerServiceExt.changeUserIdInUriGrantsManagerService(contentUserHint, data);
            GrantUri resolve = GrantUri.resolve(changeUserIdInUriGrantsManagerService, data, i2);
            i4 = checkGrantUriPermissionUnlocked(i, str, resolve, i2, i4);
            if (i4 > 0) {
                NeededUriGrants neededUriGrants3 = neededUriGrants2 == null ? new NeededUriGrants(str, i4, i2) : neededUriGrants2;
                neededUriGrants3.uris.add(resolve);
                neededUriGrants2 = neededUriGrants3;
            }
            contentUserHint = changeUserIdInUriGrantsManagerService;
        }
        if (clipData == null) {
            return neededUriGrants2;
        }
        int i5 = contentUserHint;
        int i6 = i4;
        NeededUriGrants neededUriGrants4 = neededUriGrants2;
        for (int i7 = 0; i7 < clipData.getItemCount(); i7++) {
            Uri uri = clipData.getItemAt(i7).getUri();
            if (uri != null) {
                int changeUserIdInUriGrantsManagerService2 = this.mUriGrantsManagerServiceExt.changeUserIdInUriGrantsManagerService(i5, uri);
                GrantUri resolve2 = GrantUri.resolve(changeUserIdInUriGrantsManagerService2, uri, i2);
                i6 = checkGrantUriPermissionUnlocked(i, str, resolve2, i2, i6);
                if (i6 > 0) {
                    if (neededUriGrants4 == null) {
                        neededUriGrants4 = new NeededUriGrants(str, i6, i2);
                    }
                    neededUriGrants4.uris.add(resolve2);
                }
                i5 = changeUserIdInUriGrantsManagerService2;
            } else {
                Intent intent2 = clipData.getItemAt(i7).getIntent();
                if (intent2 != null && (checkGrantUriPermissionFromIntentUnlocked = checkGrantUriPermissionFromIntentUnlocked(i, str, intent2, i2, neededUriGrants4, i3)) != null) {
                    neededUriGrants4 = checkGrantUriPermissionFromIntentUnlocked;
                }
            }
        }
        return neededUriGrants4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void readGrantedUriPermissionsLocked() {
        String str;
        FileInputStream fileInputStream;
        long j;
        int attributeInt;
        String str2 = "Failed reading Uri grants";
        String str3 = TAG;
        long currentTimeMillis = System.currentTimeMillis();
        FileInputStream fileInputStream2 = null;
        String str4 = null;
        try {
            try {
                FileInputStream openRead = this.mGrantFile.openRead();
                try {
                    try {
                        TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(openRead);
                        while (true) {
                            int next = resolvePullParser.next();
                            if (next != 1) {
                                String name = resolvePullParser.getName();
                                if (next == 2 && TAG_URI_GRANT.equals(name)) {
                                    int attributeInt2 = resolvePullParser.getAttributeInt(str4, ATTR_USER_HANDLE, -10000);
                                    if (attributeInt2 != -10000) {
                                        attributeInt = attributeInt2;
                                    } else {
                                        attributeInt2 = resolvePullParser.getAttributeInt(str4, ATTR_SOURCE_USER_ID);
                                        attributeInt = resolvePullParser.getAttributeInt(str4, ATTR_TARGET_USER_ID);
                                    }
                                    String attributeValue = resolvePullParser.getAttributeValue(str4, ATTR_SOURCE_PKG);
                                    String attributeValue2 = resolvePullParser.getAttributeValue(str4, ATTR_TARGET_PKG);
                                    Uri parse = Uri.parse(resolvePullParser.getAttributeValue(str4, ATTR_URI));
                                    boolean attributeBoolean = resolvePullParser.getAttributeBoolean(str4, ATTR_PREFIX, false);
                                    int attributeInt3 = resolvePullParser.getAttributeInt(str4, ATTR_MODE_FLAGS);
                                    str = str2;
                                    String str5 = str3;
                                    try {
                                        try {
                                            long attributeLong = resolvePullParser.getAttributeLong(str4, ATTR_CREATED_TIME, currentTimeMillis);
                                            j = currentTimeMillis;
                                            ProviderInfo providerInfo = getProviderInfo(parse.getAuthority(), attributeInt2, 786432, 1000);
                                            if (providerInfo != null) {
                                                try {
                                                    if (attributeValue.equals(providerInfo.packageName)) {
                                                        fileInputStream = openRead;
                                                        try {
                                                            try {
                                                                try {
                                                                    int packageUid = this.mPmInternal.getPackageUid(attributeValue2, 8192L, attributeInt);
                                                                    if (packageUid != -1) {
                                                                        findOrCreateUriPermissionLocked(attributeValue, attributeValue2, packageUid, new GrantUri(attributeInt2, parse, attributeBoolean ? 128 : 0)).initPersistedModes(attributeInt3, attributeLong);
                                                                        this.mPmInternal.grantImplicitAccess(attributeInt, (Intent) null, UserHandle.getAppId(packageUid), providerInfo.applicationInfo.uid, false, true);
                                                                    }
                                                                    str3 = str5;
                                                                } catch (FileNotFoundException unused) {
                                                                    fileInputStream2 = fileInputStream;
                                                                    IoUtils.closeQuietly(fileInputStream2);
                                                                    return;
                                                                } catch (Throwable th) {
                                                                    th = th;
                                                                    fileInputStream2 = fileInputStream;
                                                                    IoUtils.closeQuietly(fileInputStream2);
                                                                    throw th;
                                                                }
                                                            } catch (IOException e) {
                                                                e = e;
                                                                str2 = str;
                                                                str3 = str5;
                                                                fileInputStream2 = fileInputStream;
                                                                Slog.wtf(str3, str2, e);
                                                                IoUtils.closeQuietly(fileInputStream2);
                                                                return;
                                                            }
                                                        } catch (XmlPullParserException e2) {
                                                            e = e2;
                                                            str3 = str5;
                                                            fileInputStream2 = fileInputStream;
                                                            Slog.wtf(str3, str, e);
                                                            IoUtils.closeQuietly(fileInputStream2);
                                                            return;
                                                        }
                                                    }
                                                } catch (IOException e3) {
                                                    e = e3;
                                                    fileInputStream = openRead;
                                                }
                                            }
                                            fileInputStream = openRead;
                                            try {
                                                str3 = str5;
                                                try {
                                                    Slog.w(str3, "Persisted grant for " + parse + " had source " + attributeValue + " but instead found " + providerInfo);
                                                } catch (IOException e4) {
                                                    e = e4;
                                                    str2 = str;
                                                    fileInputStream2 = fileInputStream;
                                                    Slog.wtf(str3, str2, e);
                                                    IoUtils.closeQuietly(fileInputStream2);
                                                    return;
                                                } catch (XmlPullParserException e5) {
                                                    e = e5;
                                                    fileInputStream2 = fileInputStream;
                                                    Slog.wtf(str3, str, e);
                                                    IoUtils.closeQuietly(fileInputStream2);
                                                    return;
                                                }
                                            } catch (IOException e6) {
                                                e = e6;
                                                str3 = str5;
                                                str2 = str;
                                                fileInputStream2 = fileInputStream;
                                                Slog.wtf(str3, str2, e);
                                                IoUtils.closeQuietly(fileInputStream2);
                                                return;
                                            }
                                        } catch (XmlPullParserException e7) {
                                            e = e7;
                                            fileInputStream = openRead;
                                        }
                                    } catch (IOException e8) {
                                        e = e8;
                                        fileInputStream = openRead;
                                    }
                                } else {
                                    str = str2;
                                    j = currentTimeMillis;
                                    fileInputStream = openRead;
                                }
                                str2 = str;
                                currentTimeMillis = j;
                                openRead = fileInputStream;
                                str4 = null;
                            } else {
                                IoUtils.closeQuietly(openRead);
                                return;
                            }
                        }
                    } catch (FileNotFoundException unused2) {
                        fileInputStream = openRead;
                    } catch (Throwable th2) {
                        th = th2;
                        fileInputStream = openRead;
                    }
                } catch (IOException e9) {
                    e = e9;
                    fileInputStream = openRead;
                } catch (XmlPullParserException e10) {
                    e = e10;
                    str = str2;
                    fileInputStream = openRead;
                }
            } catch (FileNotFoundException unused3) {
                fileInputStream2 = null;
            } catch (IOException e11) {
                e = e11;
                fileInputStream2 = null;
            } catch (XmlPullParserException e12) {
                e = e12;
                str = "Failed reading Uri grants";
                fileInputStream2 = null;
            } catch (Throwable th3) {
                th = th3;
                fileInputStream2 = null;
            }
        } catch (Throwable th4) {
            th = th4;
        }
    }

    @GuardedBy({"mLock"})
    private UriPermission findOrCreateUriPermissionLocked(String str, String str2, int i, GrantUri grantUri) {
        ArrayMap<GrantUri, UriPermission> arrayMap = this.mGrantedUriPermissions.get(i);
        int changeTargetUid = this.mUriGrantsManagerServiceExt.changeTargetUid(i, Binder.getCallingUid(), str2, grantUri.uri.getAuthority());
        if (arrayMap == null) {
            arrayMap = Maps.newArrayMap();
            this.mGrantedUriPermissions.put(changeTargetUid, arrayMap);
        }
        UriPermission uriPermission = arrayMap.get(grantUri);
        if (uriPermission != null) {
            return uriPermission;
        }
        UriPermission uriPermission2 = new UriPermission(str, str2, changeTargetUid, grantUri);
        arrayMap.put(grantUri, uriPermission2);
        return uriPermission2;
    }

    private void grantUriPermissionUnchecked(int i, String str, GrantUri grantUri, int i2, UriPermissionOwner uriPermissionOwner) {
        UriPermission findOrCreateUriPermissionLocked;
        if (Intent.isAccessUriMode(i2)) {
            ProviderInfo providerInfo = getProviderInfo(grantUri.uri.getAuthority(), grantUri.sourceUserId, 268435456, 1000);
            if (providerInfo == null) {
                Slog.w(TAG, "No content provider found for grant: " + grantUri.toSafeString());
                return;
            }
            synchronized (this.mLock) {
                findOrCreateUriPermissionLocked = findOrCreateUriPermissionLocked(providerInfo.packageName, str, i, grantUri);
            }
            findOrCreateUriPermissionLocked.grantModes(i2, uriPermissionOwner);
            this.mPmInternal.grantImplicitAccess(UserHandle.getUserId(i), (Intent) null, UserHandle.getAppId(i), providerInfo.applicationInfo.uid, false, (i2 & 64) != 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void grantUriPermissionUncheckedFromIntent(NeededUriGrants neededUriGrants, UriPermissionOwner uriPermissionOwner) {
        if (neededUriGrants == null) {
            return;
        }
        int size = neededUriGrants.uris.size();
        for (int i = 0; i < size; i++) {
            grantUriPermissionUnchecked(neededUriGrants.targetUid, neededUriGrants.targetPkg, neededUriGrants.uris.valueAt(i), neededUriGrants.flags, uriPermissionOwner);
        }
    }

    private void grantUriPermissionUnlocked(int i, String str, GrantUri grantUri, int i2, UriPermissionOwner uriPermissionOwner, int i3) {
        if (str == null) {
            throw new NullPointerException(ATTR_TARGET_PKG);
        }
        int checkGrantUriPermissionUnlocked = checkGrantUriPermissionUnlocked(i, str, grantUri, i2, this.mPmInternal.getPackageUid(str, 268435456L, i3));
        if (checkGrantUriPermissionUnlocked < 0) {
            return;
        }
        grantUriPermissionUnchecked(checkGrantUriPermissionUnlocked, str, grantUri, i2, uriPermissionOwner);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void revokeUriPermission(String str, int i, GrantUri grantUri, int i2) {
        ProviderInfo providerInfo = getProviderInfo(grantUri.uri.getAuthority(), grantUri.sourceUserId, 786432, i);
        if (providerInfo == null) {
            Slog.w(TAG, "No content provider found for permission revoke: " + grantUri.toSafeString());
            return;
        }
        boolean checkHoldingPermissionsUnlocked = checkHoldingPermissionsUnlocked(providerInfo, grantUri, i, i2);
        synchronized (this.mLock) {
            revokeUriPermissionLocked(str, i, grantUri, i2, checkHoldingPermissionsUnlocked);
        }
    }

    @GuardedBy({"mLock"})
    private void revokeUriPermissionLocked(String str, int i, GrantUri grantUri, int i2, boolean z) {
        if (!z) {
            ArrayMap<GrantUri, UriPermission> arrayMap = this.mGrantedUriPermissions.get(i);
            if (arrayMap != null) {
                boolean z2 = false;
                for (int size = arrayMap.size() - 1; size >= 0; size--) {
                    UriPermission valueAt = arrayMap.valueAt(size);
                    if (str == null || str.equals(valueAt.targetPkg)) {
                        GrantUri grantUri2 = valueAt.uri;
                        if (grantUri2.sourceUserId == grantUri.sourceUserId && grantUri2.uri.isPathPrefixMatch(grantUri.uri)) {
                            z2 |= valueAt.revokeModes(i2 | 64, false);
                            if (valueAt.modeFlags == 0) {
                                arrayMap.removeAt(size);
                            }
                        }
                    }
                }
                if (arrayMap.isEmpty()) {
                    this.mGrantedUriPermissions.remove(i);
                }
                if (z2) {
                    schedulePersistUriGrants();
                    return;
                }
                return;
            }
            return;
        }
        boolean z3 = false;
        for (int size2 = this.mGrantedUriPermissions.size() - 1; size2 >= 0; size2--) {
            this.mGrantedUriPermissions.keyAt(size2);
            ArrayMap<GrantUri, UriPermission> valueAt2 = this.mGrantedUriPermissions.valueAt(size2);
            for (int size3 = valueAt2.size() - 1; size3 >= 0; size3--) {
                UriPermission valueAt3 = valueAt2.valueAt(size3);
                if (str == null || str.equals(valueAt3.targetPkg)) {
                    GrantUri grantUri3 = valueAt3.uri;
                    if (grantUri3.sourceUserId == grantUri.sourceUserId && grantUri3.uri.isPathPrefixMatch(grantUri.uri)) {
                        z3 |= valueAt3.revokeModes(i2 | 64, str == null);
                        if (valueAt3.modeFlags == 0) {
                            valueAt2.removeAt(size3);
                        }
                    }
                }
            }
            if (valueAt2.isEmpty()) {
                this.mGrantedUriPermissions.removeAt(size2);
            }
        }
        if (z3) {
            schedulePersistUriGrants();
        }
    }

    private boolean checkHoldingPermissionsUnlocked(ProviderInfo providerInfo, GrantUri grantUri, int i, int i2) {
        if (UserHandle.getUserId(i) == grantUri.sourceUserId || ActivityManager.checkComponentPermission("android.permission.INTERACT_ACROSS_USERS", i, -1, true) == 0) {
            return checkHoldingPermissionsInternalUnlocked(providerInfo, grantUri, i, i2, true);
        }
        return false;
    }

    private boolean checkHoldingPermissionsInternalUnlocked(ProviderInfo providerInfo, GrantUri grantUri, int i, int i2, boolean z) {
        boolean z2;
        String writePermission;
        String readPermission;
        String str;
        String str2;
        if (Thread.holdsLock(this.mLock)) {
            throw new IllegalStateException("Must never hold local mLock");
        }
        if (providerInfo.applicationInfo.uid == i) {
            return true;
        }
        if (!providerInfo.exported) {
            return false;
        }
        boolean z3 = (i2 & 1) == 0;
        boolean z4 = (i2 & 2) == 0;
        if (!z3 && (str2 = providerInfo.readPermission) != null && z && checkUidPermission(str2, i) == 0) {
            z3 = true;
        }
        if (!z4 && (str = providerInfo.writePermission) != null && z && checkUidPermission(str, i) == 0) {
            z4 = true;
        }
        boolean z5 = providerInfo.readPermission == null;
        boolean z6 = providerInfo.writePermission == null;
        PathPermission[] pathPermissionArr = providerInfo.pathPermissions;
        if (pathPermissionArr != null) {
            String path = grantUri.uri.getPath();
            int length = pathPermissionArr.length;
            while (length > 0 && (!z3 || !z4)) {
                length--;
                PathPermission pathPermission = pathPermissionArr[length];
                if (pathPermission.match(path)) {
                    if (!z3 && (readPermission = pathPermission.getReadPermission()) != null) {
                        if (z && checkUidPermission(readPermission, i) == 0) {
                            z3 = true;
                        } else {
                            z5 = false;
                        }
                    }
                    if (!z4 && (writePermission = pathPermission.getWritePermission()) != null) {
                        if (z && checkUidPermission(writePermission, i) == 0) {
                            z4 = true;
                        } else {
                            z6 = false;
                        }
                    }
                }
            }
        }
        if (z5) {
            z3 = true;
        }
        if (z6) {
            z4 = true;
        }
        boolean skipMultiappHandleUri = this.mUriGrantsManagerServiceExt.skipMultiappHandleUri(UserHandle.getUserId(i), grantUri.uri);
        if (providerInfo.forceUriPermissions && !skipMultiappHandleUri) {
            int userId = UserHandle.getUserId(providerInfo.applicationInfo.uid);
            int userId2 = UserHandle.getUserId(i);
            Uri uri = grantUri.uri;
            String authority = uri != null ? uri.getAuthority() : "";
            if ((1027 != i % IntervalStats.MAX_EVENTS || !"media".equals(authority)) && !this.mUriGrantsManagerServiceExt.isGrantedSystemApp(this.mPmInternal.getNameForUid(i)) && (userId != userId2 || this.mAmInternal.checkContentProviderUriPermission(grantUri.uri, userId, i, i2) != 0)) {
                z2 = false;
                return !z3 && z4 && z2;
            }
        }
        z2 = true;
        if (z3) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void removeUriPermissionIfNeededLocked(UriPermission uriPermission) {
        ArrayMap<GrantUri, UriPermission> arrayMap;
        if (uriPermission.modeFlags == 0 && (arrayMap = this.mGrantedUriPermissions.get(uriPermission.targetUid)) != null) {
            arrayMap.remove(uriPermission.uri);
            if (arrayMap.isEmpty()) {
                this.mGrantedUriPermissions.remove(uriPermission.targetUid);
            }
        }
    }

    @GuardedBy({"mLock"})
    private UriPermission findUriPermissionLocked(int i, GrantUri grantUri) {
        ArrayMap<GrantUri, UriPermission> arrayMap = this.mGrantedUriPermissions.get(i);
        if (arrayMap != null) {
            return arrayMap.get(grantUri);
        }
        return null;
    }

    private void schedulePersistUriGrants() {
        if (this.mH.hasMessages(1)) {
            return;
        }
        H h = this.mH;
        h.sendMessageDelayed(h.obtainMessage(1), JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceNotIsolatedCaller(String str) {
        if (UserHandle.isIsolated(Binder.getCallingUid())) {
            throw new SecurityException("Isolated process not allowed to call " + str);
        }
    }

    private ProviderInfo getProviderInfo(String str, int i, int i2, int i3) {
        return this.mPmInternal.resolveContentProvider(str, i2 | 2048, i, i3);
    }

    /* JADX WARN: Code restructure failed: missing block: B:117:0x0101, code lost:
    
        if (r1 != false) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00b1, code lost:
    
        if (checkHoldingPermissionsUnlocked(r10, r19, r11, r20) != false) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00b3, code lost:
    
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0104, code lost:
    
        r1 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int checkGrantUriPermissionUnlocked(int i, String str, GrantUri grantUri, int i2, int i3) {
        int i4;
        boolean z;
        boolean checkUriPermissionLocked;
        if (!Intent.isAccessUriMode(i2) || !"content".equals(grantUri.uri.getScheme())) {
            return -1;
        }
        int appId = UserHandle.getAppId(i);
        if ((appId == 1000 || appId == 0) && !"com.android.settings.files".equals(grantUri.uri.getAuthority()) && !"com.android.settings.module_licenses".equals(grantUri.uri.getAuthority())) {
            Slog.w(TAG, "For security reasons, the system cannot issue a Uri permission grant to " + grantUri + "; use startActivityAsCaller() instead");
            return -1;
        }
        ProviderInfo providerInfo = getProviderInfo(grantUri.uri.getAuthority(), grantUri.sourceUserId, 268435456, i);
        if (providerInfo == null) {
            Slog.w(TAG, "No content provider found for permission check: " + grantUri.uri.toSafeString());
            return -1;
        }
        if (i3 >= 0 || str == null) {
            i4 = i3;
        } else {
            int packageUid = this.mPmInternal.getPackageUid(str, 268435456L, UserHandle.getUserId(i));
            if (packageUid < 0) {
                return -1;
            }
            i4 = packageUid;
        }
        boolean z2 = true;
        if (i4 < 0) {
            boolean z3 = providerInfo.exported;
            int i5 = i2 & 1;
            if (i5 != 0 && providerInfo.readPermission != null) {
                z3 = false;
            }
            int i6 = i2 & 2;
            if (i6 != 0 && providerInfo.writePermission != null) {
                z3 = false;
            }
            PathPermission[] pathPermissionArr = providerInfo.pathPermissions;
            if (pathPermissionArr != null) {
                int length = pathPermissionArr.length;
                int i7 = 0;
                while (true) {
                    if (i7 >= length) {
                        break;
                    }
                    PathPermission pathPermission = providerInfo.pathPermissions[i7];
                    if (pathPermission == null || !pathPermission.match(grantUri.uri.getPath())) {
                        i7++;
                    } else {
                        if (i5 != 0 && providerInfo.pathPermissions[i7].getReadPermission() != null) {
                            z3 = false;
                        }
                        if (i6 != 0 && providerInfo.pathPermissions[i7].getWritePermission() != null) {
                            z3 = false;
                        }
                    }
                }
            }
        }
        if (providerInfo.forceUriPermissions) {
            z = false;
        }
        boolean z4 = (i2 & 192) == 0;
        if (z4 && z) {
            this.mPmInternal.grantImplicitAccess(UserHandle.getUserId(i4), (Intent) null, UserHandle.getAppId(i4), providerInfo.applicationInfo.uid, false);
            return -1;
        }
        boolean z5 = i4 >= 0 && UserHandle.getUserId(i4) != grantUri.sourceUserId && checkHoldingPermissionsInternalUnlocked(providerInfo, grantUri, i, i2, false);
        boolean z6 = providerInfo.grantUriPermissions;
        if (!ArrayUtils.isEmpty(providerInfo.uriPermissionPatterns)) {
            int length2 = providerInfo.uriPermissionPatterns.length;
            int i8 = 0;
            while (true) {
                if (i8 < length2) {
                    PatternMatcher patternMatcher = providerInfo.uriPermissionPatterns[i8];
                    if (patternMatcher != null && patternMatcher.match(grantUri.uri.getPath())) {
                        break;
                    }
                    i8++;
                } else {
                    z2 = false;
                    break;
                }
            }
        } else {
            z2 = z6;
        }
        if (!z2) {
            if (!z5) {
                throw new SecurityException("Provider " + providerInfo.packageName + SliceClientPermissions.SliceAuthority.DELIMITER + providerInfo.name + " does not allow granting of Uri permissions (uri " + grantUri + ")");
            }
            if (!z4) {
                throw new SecurityException("Provider " + providerInfo.packageName + SliceClientPermissions.SliceAuthority.DELIMITER + providerInfo.name + " does not allow granting of advanced Uri permissions (uri " + grantUri + ")");
            }
        }
        if (!checkHoldingPermissionsUnlocked(providerInfo, grantUri, i, i2)) {
            synchronized (this.mLock) {
                checkUriPermissionLocked = checkUriPermissionLocked(grantUri, i, i2);
            }
            if (!checkUriPermissionLocked) {
                if ("android.permission.MANAGE_DOCUMENTS".equals(providerInfo.readPermission)) {
                    throw new SecurityException("UID " + i + " does not have permission to " + grantUri + "; you could obtain access using ACTION_OPEN_DOCUMENT or related APIs");
                }
                throw new SecurityException("UID " + i + " does not have permission to " + grantUri);
            }
        }
        return i4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int checkGrantUriPermissionUnlocked(int i, String str, Uri uri, int i2, int i3) {
        return checkGrantUriPermissionUnlocked(i, str, new GrantUri(i3, uri, i2), i2, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public boolean checkUriPermissionLocked(GrantUri grantUri, int i, int i2) {
        Uri uri;
        int i3 = (i2 & 64) != 0 ? 3 : 1;
        if (i == 0) {
            return true;
        }
        if (grantUri != null && (uri = grantUri.uri) != null && this.mUriGrantsManagerServiceExt.needChangeUid(this.mGrantedUriPermissions, uri.getAuthority(), i) && 999 == UserHandle.getUserId(i)) {
            i = UserHandle.getUid(0, UserHandle.getAppId(i));
        }
        ArrayMap<GrantUri, UriPermission> arrayMap = this.mGrantedUriPermissions.get(i);
        if (arrayMap == null) {
            return false;
        }
        UriPermission uriPermission = arrayMap.get(grantUri);
        if (uriPermission != null && uriPermission.getStrength(i2) >= i3) {
            return true;
        }
        int size = arrayMap.size();
        for (int i4 = 0; i4 < size; i4++) {
            UriPermission valueAt = arrayMap.valueAt(i4);
            GrantUri grantUri2 = valueAt.uri;
            if (grantUri2.prefix && grantUri.uri.isPathPrefixMatch(grantUri2.uri) && valueAt.getStrength(i2) >= i3) {
                return true;
            }
        }
        return false;
    }

    @RequiresPermission("android.permission.INTERACT_ACROSS_USERS_FULL")
    public int checkGrantUriPermission_ignoreNonSystem(int i, String str, Uri uri, int i2, int i3) {
        if (!isCallerIsSystemOrPrivileged()) {
            return -1;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return checkGrantUriPermissionUnlocked(i, str, uri, i2, i3);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean isCallerIsSystemOrPrivileged() {
        int callingUid = Binder.getCallingUid();
        return callingUid == 1000 || callingUid == 0 || ActivityManager.checkComponentPermission("android.permission.INTERACT_ACROSS_USERS_FULL", callingUid, -1, true) == 0;
    }

    @Override // com.android.server.uri.UriMetricsHelper.PersistentUriGrantsProvider
    public ArrayList<UriPermission> providePersistentUriGrants() {
        ArrayList<UriPermission> arrayList = new ArrayList<>();
        synchronized (this.mLock) {
            int size = this.mGrantedUriPermissions.size();
            for (int i = 0; i < size; i++) {
                ArrayMap<GrantUri, UriPermission> valueAt = this.mGrantedUriPermissions.valueAt(i);
                int size2 = valueAt.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    UriPermission valueAt2 = valueAt.valueAt(i2);
                    if (valueAt2.persistedModeFlags != 0) {
                        arrayList.add(valueAt2);
                    }
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeGrantedUriPermissions() {
        int i;
        FileOutputStream startWrite;
        long uptimeMillis = SystemClock.uptimeMillis();
        ArrayList newArrayList = Lists.newArrayList();
        synchronized (this.mLock) {
            int size = this.mGrantedUriPermissions.size();
            i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                ArrayMap<GrantUri, UriPermission> valueAt = this.mGrantedUriPermissions.valueAt(i2);
                int size2 = valueAt.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    UriPermission valueAt2 = valueAt.valueAt(i3);
                    if (valueAt2.persistedModeFlags != 0) {
                        i++;
                        newArrayList.add(valueAt2.snapshot());
                    }
                }
            }
        }
        FileOutputStream fileOutputStream = null;
        try {
            startWrite = this.mGrantFile.startWrite(uptimeMillis);
        } catch (IOException unused) {
        }
        try {
            TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
            resolveSerializer.startDocument((String) null, Boolean.TRUE);
            resolveSerializer.startTag((String) null, TAG_URI_GRANTS);
            Iterator it = newArrayList.iterator();
            while (it.hasNext()) {
                UriPermission.Snapshot snapshot = (UriPermission.Snapshot) it.next();
                resolveSerializer.startTag((String) null, TAG_URI_GRANT);
                resolveSerializer.attributeInt((String) null, ATTR_SOURCE_USER_ID, snapshot.uri.sourceUserId);
                resolveSerializer.attributeInt((String) null, ATTR_TARGET_USER_ID, snapshot.targetUserId);
                resolveSerializer.attributeInterned((String) null, ATTR_SOURCE_PKG, snapshot.sourcePkg);
                resolveSerializer.attributeInterned((String) null, ATTR_TARGET_PKG, snapshot.targetPkg);
                resolveSerializer.attribute((String) null, ATTR_URI, String.valueOf(snapshot.uri.uri));
                XmlUtils.writeBooleanAttribute(resolveSerializer, ATTR_PREFIX, snapshot.uri.prefix);
                resolveSerializer.attributeInt((String) null, ATTR_MODE_FLAGS, snapshot.persistedModeFlags);
                resolveSerializer.attributeLong((String) null, ATTR_CREATED_TIME, snapshot.persistedCreateTime);
                resolveSerializer.endTag((String) null, TAG_URI_GRANT);
            }
            resolveSerializer.endTag((String) null, TAG_URI_GRANTS);
            resolveSerializer.endDocument();
            this.mGrantFile.finishWrite(startWrite);
        } catch (IOException unused2) {
            fileOutputStream = startWrite;
            if (fileOutputStream != null) {
                this.mGrantFile.failWrite(fileOutputStream);
            }
            this.mMetricsHelper.reportPersistentUriFlushed(i);
        }
        this.mMetricsHelper.reportPersistentUriFlushed(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class H extends Handler {
        static final int PERSIST_URI_GRANTS_MSG = 1;

        public H(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            UriGrantsManagerService.this.writeGrantedUriPermissions();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class LocalService implements UriGrantsManagerInternal {
        private LocalService() {
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void removeUriPermissionIfNeeded(UriPermission uriPermission) {
            synchronized (UriGrantsManagerService.this.mLock) {
                UriGrantsManagerService.this.removeUriPermissionIfNeededLocked(uriPermission);
            }
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void revokeUriPermission(String str, int i, GrantUri grantUri, int i2) {
            UriGrantsManagerService.this.revokeUriPermission(str, i, grantUri, i2);
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public boolean checkUriPermission(GrantUri grantUri, int i, int i2) {
            boolean checkUriPermissionLocked;
            synchronized (UriGrantsManagerService.this.mLock) {
                checkUriPermissionLocked = UriGrantsManagerService.this.checkUriPermissionLocked(grantUri, i, i2);
            }
            return checkUriPermissionLocked;
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public int checkGrantUriPermission(int i, String str, Uri uri, int i2, int i3) {
            UriGrantsManagerService.this.enforceNotIsolatedCaller("checkGrantUriPermission");
            return UriGrantsManagerService.this.checkGrantUriPermissionUnlocked(i, str, uri, i2, i3);
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public NeededUriGrants checkGrantUriPermissionFromIntent(Intent intent, int i, String str, int i2) {
            return UriGrantsManagerService.this.checkGrantUriPermissionFromIntentUnlocked(i, str, intent, intent != null ? intent.getFlags() : 0, null, i2);
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void grantUriPermissionUncheckedFromIntent(NeededUriGrants neededUriGrants, UriPermissionOwner uriPermissionOwner) {
            UriGrantsManagerService.this.grantUriPermissionUncheckedFromIntent(neededUriGrants, uriPermissionOwner);
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void onSystemReady() {
            synchronized (UriGrantsManagerService.this.mLock) {
                UriGrantsManagerService.this.readGrantedUriPermissionsLocked();
            }
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public IBinder newUriPermissionOwner(String str) {
            UriGrantsManagerService.this.enforceNotIsolatedCaller("newUriPermissionOwner");
            return new UriPermissionOwner(this, str).getExternalToken();
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void removeUriPermissionsForPackage(String str, int i, boolean z, boolean z2) {
            synchronized (UriGrantsManagerService.this.mLock) {
                UriGrantsManagerService.this.removeUriPermissionsForPackageLocked(str, i, z, z2);
            }
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void revokeUriPermissionFromOwner(IBinder iBinder, Uri uri, int i, int i2) {
            revokeUriPermissionFromOwner(iBinder, uri, i, i2, null, -1);
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void revokeUriPermissionFromOwner(IBinder iBinder, Uri uri, int i, int i2, String str, int i3) {
            GrantUri grantUri;
            UriPermissionOwner fromExternalToken = UriPermissionOwner.fromExternalToken(iBinder);
            if (fromExternalToken == null) {
                throw new IllegalArgumentException("Unknown owner: " + iBinder);
            }
            if (uri == null) {
                grantUri = null;
            } else {
                try {
                    grantUri = new GrantUri(i2, uri, i);
                } catch (Exception e) {
                    Slog.e(UriGrantsManagerService.TAG, "Failed remove uri permission", e);
                    return;
                }
            }
            fromExternalToken.removeUriPermission(grantUri, i, str, i3);
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public boolean checkAuthorityGrants(int i, ProviderInfo providerInfo, int i2, boolean z) {
            boolean checkAuthorityGrantsLocked;
            synchronized (UriGrantsManagerService.this.mLock) {
                checkAuthorityGrantsLocked = UriGrantsManagerService.this.checkAuthorityGrantsLocked(i, providerInfo, i2, z);
            }
            return checkAuthorityGrantsLocked;
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void dump(PrintWriter printWriter, boolean z, String str) {
            synchronized (UriGrantsManagerService.this.mLock) {
                int i = 0;
                if (UriGrantsManagerService.this.mGrantedUriPermissions.size() > 0) {
                    int packageUid = str != null ? UriGrantsManagerService.this.mPmInternal.getPackageUid(str, 4194304L, 0) : -2;
                    int i2 = 0;
                    boolean z2 = false;
                    boolean z3 = false;
                    while (i < UriGrantsManagerService.this.mGrantedUriPermissions.size()) {
                        int keyAt = UriGrantsManagerService.this.mGrantedUriPermissions.keyAt(i);
                        if (packageUid < -1 || UserHandle.getAppId(keyAt) == packageUid) {
                            ArrayMap arrayMap = (ArrayMap) UriGrantsManagerService.this.mGrantedUriPermissions.valueAt(i);
                            if (!z2) {
                                if (z3) {
                                    printWriter.println();
                                }
                                printWriter.println("  Granted Uri Permissions:");
                                z2 = true;
                                i2 = 1;
                                z3 = true;
                            }
                            printWriter.print("  * UID ");
                            printWriter.print(keyAt);
                            printWriter.println(" holds:");
                            for (UriPermission uriPermission : arrayMap.values()) {
                                printWriter.print("    ");
                                printWriter.println(uriPermission);
                                if (z) {
                                    uriPermission.dump(printWriter, "      ");
                                }
                            }
                        }
                        i++;
                    }
                    i = i2;
                }
                if (i == 0) {
                    printWriter.println("  (nothing)");
                }
            }
        }
    }
}
