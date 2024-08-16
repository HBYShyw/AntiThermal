package com.android.server.pm;

import android.app.Person;
import android.app.appsearch.AppSearchBatchResult;
import android.app.appsearch.AppSearchManager;
import android.app.appsearch.AppSearchResult;
import android.app.appsearch.AppSearchSession;
import android.app.appsearch.BatchResultCallback;
import android.app.appsearch.GenericDocument;
import android.app.appsearch.GetByDocumentIdRequest;
import android.app.appsearch.PutDocumentsRequest;
import android.app.appsearch.RemoveByDocumentIdRequest;
import android.app.appsearch.ReportUsageRequest;
import android.app.appsearch.SearchResult;
import android.app.appsearch.SearchSpec;
import android.app.appsearch.SetSchemaRequest;
import android.app.usage.UsageStatsManagerInternal;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.AppSearchShortcutInfo;
import android.content.pm.AppSearchShortcutPerson;
import android.content.pm.PackageInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.Log;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.infra.AndroidFuture;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.Preconditions;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.pm.ShareTargetInfo;
import com.android.server.pm.ShortcutPackage;
import com.android.server.pm.ShortcutService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import libcore.io.IoUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ShortcutPackage extends ShortcutPackageItem {
    private static final String ATTR_ACTIVITY = "activity";
    private static final String ATTR_BITMAP_PATH = "bitmap-path";
    private static final String ATTR_CALL_COUNT = "call-count";
    private static final String ATTR_DISABLED_MESSAGE = "dmessage";
    private static final String ATTR_DISABLED_MESSAGE_RES_ID = "dmessageid";
    private static final String ATTR_DISABLED_MESSAGE_RES_NAME = "dmessagename";
    private static final String ATTR_DISABLED_REASON = "disabled-reason";
    private static final String ATTR_FLAGS = "flags";
    private static final String ATTR_ICON_RES_ID = "icon-res";
    private static final String ATTR_ICON_RES_NAME = "icon-resname";
    private static final String ATTR_ICON_URI = "icon-uri";
    private static final String ATTR_ID = "id";
    private static final String ATTR_INTENT_LEGACY = "intent";
    private static final String ATTR_INTENT_NO_EXTRA = "intent-base";
    private static final String ATTR_LAST_RESET = "last-reset";
    private static final String ATTR_LOCUS_ID = "locus-id";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_NAME_XMLUTILS = "name";
    private static final String ATTR_PERSON_IS_BOT = "is-bot";
    private static final String ATTR_PERSON_IS_IMPORTANT = "is-important";
    private static final String ATTR_PERSON_KEY = "key";
    private static final String ATTR_PERSON_NAME = "name";
    private static final String ATTR_PERSON_URI = "uri";
    private static final String ATTR_RANK = "rank";
    private static final String ATTR_SCHEMA_VERSON = "schema-version";
    private static final String ATTR_SPLASH_SCREEN_THEME_NAME = "splash-screen-theme-name";
    private static final String ATTR_TEXT = "text";
    private static final String ATTR_TEXT_RES_ID = "textid";
    private static final String ATTR_TEXT_RES_NAME = "textname";
    private static final String ATTR_TIMESTAMP = "timestamp";
    private static final String ATTR_TITLE = "title";
    private static final String ATTR_TITLE_RES_ID = "titleid";
    private static final String ATTR_TITLE_RES_NAME = "titlename";
    private static final String KEY_BITMAPS = "bitmaps";
    private static final String KEY_BITMAP_BYTES = "bitmapBytes";
    private static final String KEY_DYNAMIC = "dynamic";
    private static final String KEY_MANIFEST = "manifest";
    private static final String KEY_PINNED = "pinned";
    private static final String NAME_CAPABILITY = "capability";
    private static final String NAME_CATEGORIES = "categories";

    @VisibleForTesting
    public static final int REPORT_USAGE_BUFFER_SIZE = 3;
    private static final String TAG = "ShortcutService";
    private static final String TAG_CATEGORIES = "categories";
    private static final String TAG_EXTRAS = "extras";
    private static final String TAG_INTENT = "intent";
    private static final String TAG_INTENT_EXTRAS_LEGACY = "intent-extras";
    private static final String TAG_MAP_XMLUTILS = "map";
    private static final String TAG_PERSON = "person";
    static final String TAG_ROOT = "package";
    private static final String TAG_SHARE_TARGET = "share-target";
    private static final String TAG_SHORTCUT = "shortcut";
    private static final String TAG_STRING_ARRAY_XMLUTILS = "string-array";
    private static final String TAG_VERIFY = "ShortcutService.verify";
    private int mApiCallCount;
    private final Executor mExecutor;

    @GuardedBy({"mLock"})
    private boolean mIsAppSearchSchemaUpToDate;
    private long mLastKnownForegroundElapsedTime;

    @GuardedBy({"mLock"})
    private List<Long> mLastReportedTime;
    private long mLastResetTime;
    private final int mPackageUid;

    @GuardedBy({"mLock"})
    private final ArrayList<ShareTargetInfo> mShareTargets;
    final Comparator<ShortcutInfo> mShortcutRankComparator;
    final Comparator<ShortcutInfo> mShortcutTypeAndRankComparator;
    final Comparator<ShortcutInfo> mShortcutTypeRankAndTimeComparator;

    @GuardedBy({"mLock"})
    private final ArrayMap<String, ShortcutInfo> mShortcuts;

    @GuardedBy({"mLock"})
    private final ArrayMap<String, ShortcutInfo> mTransientShortcuts;

    @Override // com.android.server.pm.ShortcutPackageItem
    protected boolean canRestoreAnyVersion() {
        return false;
    }

    private ShortcutPackage(ShortcutUser shortcutUser, int i, String str, ShortcutPackageInfo shortcutPackageInfo) {
        super(shortcutUser, i, str, shortcutPackageInfo == null ? ShortcutPackageInfo.newEmpty() : shortcutPackageInfo);
        this.mShortcuts = new ArrayMap<>();
        this.mTransientShortcuts = new ArrayMap<>(0);
        this.mShareTargets = new ArrayList<>(0);
        this.mLastReportedTime = new ArrayList();
        this.mShortcutTypeAndRankComparator = new Comparator() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda19
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$new$19;
                lambda$new$19 = ShortcutPackage.lambda$new$19((ShortcutInfo) obj, (ShortcutInfo) obj2);
                return lambda$new$19;
            }
        };
        this.mShortcutTypeRankAndTimeComparator = new Comparator() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda20
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$new$20;
                lambda$new$20 = ShortcutPackage.lambda$new$20((ShortcutInfo) obj, (ShortcutInfo) obj2);
                return lambda$new$20;
            }
        };
        this.mShortcutRankComparator = new Comparator() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda21
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$new$25;
                lambda$new$25 = ShortcutPackage.lambda$new$25((ShortcutInfo) obj, (ShortcutInfo) obj2);
                return lambda$new$25;
            }
        };
        this.mPackageUid = shortcutUser.mService.injectGetPackageUid(str, i);
        this.mExecutor = BackgroundThread.getExecutor();
    }

    public ShortcutPackage(ShortcutUser shortcutUser, int i, String str) {
        this(shortcutUser, i, str, null);
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    public int getOwnerUserId() {
        return getPackageUserId();
    }

    public int getPackageUid() {
        return this.mPackageUid;
    }

    public Resources getPackageResources() {
        return this.mShortcutUser.mService.injectGetResourcesForApplicationAsUser(getPackageName(), getPackageUserId());
    }

    private boolean isAppSearchEnabled() {
        return this.mShortcutUser.mService.isAppSearchEnabled();
    }

    public int getShortcutCount() {
        int size;
        synchronized (this.mLock) {
            size = this.mShortcuts.size();
        }
        return size;
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    protected void onRestored(final int i) {
        String.format("%s:-%s AND %s:%s", ATTR_FLAGS, 4096, "disabledReason", Integer.valueOf(i));
        forEachShortcutMutate(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda57
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.lambda$onRestored$0(i, (ShortcutInfo) obj);
            }
        });
        refreshPinnedFlags();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onRestored$0(int i, ShortcutInfo shortcutInfo) {
        if (i == 0 && !shortcutInfo.hasFlags(4096) && shortcutInfo.getDisabledReason() == i) {
            return;
        }
        shortcutInfo.clearFlags(4096);
        shortcutInfo.setDisabledReason(i);
        if (i != 0) {
            shortcutInfo.addFlags(64);
        }
    }

    public ShortcutInfo findShortcutById(String str) {
        ShortcutInfo shortcutInfo;
        if (str == null) {
            return null;
        }
        synchronized (this.mLock) {
            shortcutInfo = this.mShortcuts.get(str);
        }
        return shortcutInfo;
    }

    public boolean isShortcutExistsAndInvisibleToPublisher(String str) {
        ShortcutInfo findShortcutById = findShortcutById(str);
        return (findShortcutById == null || findShortcutById.isVisibleToPublisher()) ? false : true;
    }

    public boolean isShortcutExistsAndVisibleToPublisher(String str) {
        ShortcutInfo findShortcutById = findShortcutById(str);
        return findShortcutById != null && findShortcutById.isVisibleToPublisher();
    }

    private void ensureNotImmutable(ShortcutInfo shortcutInfo, boolean z) {
        if (shortcutInfo == null || !shortcutInfo.isImmutable()) {
            return;
        }
        if (!z || shortcutInfo.isVisibleToPublisher()) {
            throw new IllegalArgumentException("Manifest shortcut ID=" + shortcutInfo.getId() + " may not be manipulated via APIs");
        }
    }

    public void ensureNotImmutable(String str, boolean z) {
        ensureNotImmutable(findShortcutById(str), z);
    }

    public void ensureImmutableShortcutsNotIncludedWithIds(List<String> list, boolean z) {
        for (int size = list.size() - 1; size >= 0; size--) {
            ensureNotImmutable(list.get(size), z);
        }
    }

    public void ensureImmutableShortcutsNotIncluded(List<ShortcutInfo> list, boolean z) {
        for (int size = list.size() - 1; size >= 0; size--) {
            ensureNotImmutable(list.get(size).getId(), z);
        }
    }

    public void ensureNoBitmapIconIfShortcutIsLongLived(List<ShortcutInfo> list) {
        Icon icon;
        for (int size = list.size() - 1; size >= 0; size--) {
            ShortcutInfo shortcutInfo = list.get(size);
            if (shortcutInfo.isLongLived() && (((icon = shortcutInfo.getIcon()) == null || icon.getType() == 1 || icon.getType() == 5) && (icon != null || shortcutInfo.hasIconFile()))) {
                Slog.e(TAG, "Invalid icon type in shortcut " + shortcutInfo.getId() + ". Bitmaps are not allowed in long-lived shortcuts. Use Resource icons, or Uri-based icons instead.");
                return;
            }
        }
    }

    public void ensureAllShortcutsVisibleToLauncher(List<ShortcutInfo> list) {
        for (ShortcutInfo shortcutInfo : list) {
            if (shortcutInfo.isExcludedFromSurfaces(1)) {
                throw new IllegalArgumentException("Shortcut ID=" + shortcutInfo.getId() + " is hidden from launcher and may not be manipulated via APIs");
            }
        }
    }

    private ShortcutInfo forceDeleteShortcutInner(String str) {
        ShortcutInfo remove;
        synchronized (this.mLock) {
            remove = this.mShortcuts.remove(str);
            if (remove != null) {
                removeIcon(remove);
                remove.clearFlags(1610629155);
            }
        }
        return remove;
    }

    private void forceReplaceShortcutInner(ShortcutInfo shortcutInfo) {
        ShortcutService shortcutService = this.mShortcutUser.mService;
        forceDeleteShortcutInner(shortcutInfo.getId());
        shortcutService.saveIconAndFixUpShortcutLocked(this, shortcutInfo);
        shortcutService.fixUpShortcutResourceNamesAndValues(shortcutInfo);
        ensureShortcutCountBeforePush();
        saveShortcut(shortcutInfo);
    }

    public boolean addOrReplaceDynamicShortcut(ShortcutInfo shortcutInfo) {
        Preconditions.checkArgument(shortcutInfo.isEnabled(), "add/setDynamicShortcuts() cannot publish disabled shortcuts");
        shortcutInfo.addFlags(1);
        ShortcutInfo findShortcutById = findShortcutById(shortcutInfo.getId());
        if (findShortcutById != null) {
            findShortcutById.ensureUpdatableWith(shortcutInfo, false);
            shortcutInfo.addFlags(findShortcutById.getFlags() & 1610629122);
        }
        if (shortcutInfo.isExcludedFromSurfaces(1)) {
            if (isAppSearchEnabled()) {
                synchronized (this.mLock) {
                    this.mTransientShortcuts.put(shortcutInfo.getId(), shortcutInfo);
                }
            }
        } else {
            forceReplaceShortcutInner(shortcutInfo);
        }
        return findShortcutById != null;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x00a4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean pushDynamicShortcut(final ShortcutInfo shortcutInfo, List<ShortcutInfo> list) {
        boolean z;
        Preconditions.checkArgument(shortcutInfo.isEnabled(), "pushDynamicShortcuts() cannot publish disabled shortcuts");
        shortcutInfo.addFlags(1);
        list.clear();
        ShortcutInfo findShortcutById = findShortcutById(shortcutInfo.getId());
        if (findShortcutById == null || !findShortcutById.isDynamic()) {
            ShortcutService shortcutService = this.mShortcutUser.mService;
            int maxActivityShortcuts = shortcutService.getMaxActivityShortcuts();
            ArrayList<ShortcutInfo> arrayList = sortShortcutsToActivities().get(shortcutInfo.getActivity());
            if (arrayList != null && arrayList.size() > maxActivityShortcuts) {
                shortcutService.wtf("Error pushing shortcut. There are already " + arrayList.size() + " shortcuts.");
            }
            if (arrayList != null && arrayList.size() >= maxActivityShortcuts) {
                Collections.sort(arrayList, this.mShortcutTypeAndRankComparator);
                ShortcutInfo shortcutInfo2 = arrayList.get(maxActivityShortcuts - 1);
                if (shortcutInfo2.isManifestShortcut()) {
                    Slog.e(TAG, "Failed to remove manifest shortcut while pushing dynamic shortcut " + shortcutInfo.getId());
                    return true;
                }
                list.add(shortcutInfo2);
                if (deleteDynamicWithId(shortcutInfo2.getId(), true, true) != null) {
                    z = true;
                    if (findShortcutById != null) {
                        findShortcutById.ensureUpdatableWith(shortcutInfo, false);
                        shortcutInfo.addFlags(findShortcutById.getFlags() & 1610629122);
                    }
                    if (!shortcutInfo.isExcludedFromSurfaces(1)) {
                        if (isAppSearchEnabled()) {
                            synchronized (this.mLock) {
                                this.mTransientShortcuts.put(shortcutInfo.getId(), shortcutInfo);
                            }
                        }
                    } else {
                        forceReplaceShortcutInner(shortcutInfo);
                    }
                    if (isAppSearchEnabled()) {
                        runAsSystem(new Runnable() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda27
                            @Override // java.lang.Runnable
                            public final void run() {
                                ShortcutPackage.this.lambda$pushDynamicShortcut$3(shortcutInfo);
                            }
                        });
                    }
                    return z;
                }
            }
        }
        z = false;
        if (findShortcutById != null) {
        }
        if (!shortcutInfo.isExcludedFromSurfaces(1)) {
        }
        if (isAppSearchEnabled()) {
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pushDynamicShortcut$3(final ShortcutInfo shortcutInfo) {
        fromAppSearch().thenAccept(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.this.lambda$pushDynamicShortcut$2(shortcutInfo, (AppSearchSession) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pushDynamicShortcut$2(ShortcutInfo shortcutInfo, AppSearchSession appSearchSession) {
        appSearchSession.reportUsage(new ReportUsageRequest.Builder(getPackageName(), shortcutInfo.getId()).build(), this.mExecutor, new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda39
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.lambda$pushDynamicShortcut$1((AppSearchResult) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$pushDynamicShortcut$1(AppSearchResult appSearchResult) {
        if (appSearchResult.isSuccess()) {
            return;
        }
        Slog.e(TAG, "Failed to report usage via AppSearch. ");
    }

    private void ensureShortcutCountBeforePush() {
        int maxAppShortcuts = this.mShortcutUser.mService.getMaxAppShortcuts();
        synchronized (this.mLock) {
            List list = (List) this.mShortcuts.values().stream().filter(new Predicate() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda50
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$ensureShortcutCountBeforePush$4;
                    lambda$ensureShortcutCountBeforePush$4 = ShortcutPackage.lambda$ensureShortcutCountBeforePush$4((ShortcutInfo) obj);
                    return lambda$ensureShortcutCountBeforePush$4;
                }
            }).collect(Collectors.toList());
            if (list.size() >= maxAppShortcuts) {
                Collections.sort(list, this.mShortcutTypeRankAndTimeComparator);
                while (list.size() >= maxAppShortcuts) {
                    ShortcutInfo shortcutInfo = (ShortcutInfo) list.remove(list.size() - 1);
                    if (shortcutInfo.isDeclaredInManifest()) {
                        throw new IllegalArgumentException(getPackageName() + " has published " + list.size() + " manifest shortcuts across different activities.");
                    }
                    forceDeleteShortcutInner(shortcutInfo.getId());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$ensureShortcutCountBeforePush$4(ShortcutInfo shortcutInfo) {
        return !shortcutInfo.isPinned();
    }

    private List<ShortcutInfo> removeOrphans() {
        final ArrayList arrayList = new ArrayList(1);
        forEachShortcut(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda35
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.lambda$removeOrphans$5(arrayList, (ShortcutInfo) obj);
            }
        });
        if (!arrayList.isEmpty()) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                forceDeleteShortcutInner(((ShortcutInfo) arrayList.get(size)).getId());
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$removeOrphans$5(List list, ShortcutInfo shortcutInfo) {
        if (shortcutInfo.isAlive()) {
            return;
        }
        list.add(shortcutInfo);
    }

    public List<ShortcutInfo> deleteAllDynamicShortcuts() {
        boolean z;
        long injectCurrentTimeMillis = this.mShortcutUser.mService.injectCurrentTimeMillis();
        synchronized (this.mLock) {
            z = false;
            for (int size = this.mShortcuts.size() - 1; size >= 0; size--) {
                ShortcutInfo valueAt = this.mShortcuts.valueAt(size);
                if (valueAt.isDynamic() && valueAt.isVisibleToPublisher()) {
                    valueAt.setTimestamp(injectCurrentTimeMillis);
                    valueAt.clearFlags(1);
                    valueAt.setRank(0);
                    z = true;
                }
            }
        }
        removeAllShortcutsAsync();
        if (z) {
            return removeOrphans();
        }
        return null;
    }

    public ShortcutInfo deleteDynamicWithId(String str, boolean z, boolean z2) {
        return deleteOrDisableWithId(str, false, false, z, 0, z2);
    }

    private ShortcutInfo disableDynamicWithId(String str, boolean z, int i, boolean z2) {
        return deleteOrDisableWithId(str, true, false, z, i, z2);
    }

    public ShortcutInfo deleteLongLivedWithId(String str, boolean z) {
        if (findShortcutById(str) != null) {
            mutateShortcut(str, null, new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((ShortcutInfo) obj).clearFlags(1610629120);
                }
            });
        }
        return deleteOrDisableWithId(str, false, false, z, 0, false);
    }

    public ShortcutInfo disableWithId(String str, final String str2, final int i, boolean z, boolean z2, int i2) {
        ShortcutInfo deleteOrDisableWithId = deleteOrDisableWithId(str, true, z, z2, i2, false);
        mutateShortcut(str, null, new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda17
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.this.lambda$disableWithId$7(str2, i, (ShortcutInfo) obj);
            }
        });
        return deleteOrDisableWithId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$disableWithId$7(String str, int i, ShortcutInfo shortcutInfo) {
        if (shortcutInfo != null) {
            if (str != null) {
                shortcutInfo.setDisabledMessage(str);
            } else if (i != 0) {
                shortcutInfo.setDisabledMessageResId(i);
                this.mShortcutUser.mService.fixUpShortcutResourceNamesAndValues(shortcutInfo);
            }
        }
    }

    private ShortcutInfo deleteOrDisableWithId(String str, final boolean z, boolean z2, boolean z3, final int i, boolean z4) {
        Preconditions.checkState(z == (i != 0), "disable and disabledReason disagree: " + z + " vs " + i);
        ShortcutInfo findShortcutById = findShortcutById(str);
        if (findShortcutById != null && (findShortcutById.isEnabled() || !z3 || findShortcutById.isVisibleToPublisher())) {
            if (!z2) {
                ensureNotImmutable(findShortcutById, true);
            }
            if (!z4) {
                removeShortcutAsync(str);
            }
            if (findShortcutById.isPinned() || findShortcutById.isCached()) {
                mutateShortcut(findShortcutById.getId(), findShortcutById, new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda49
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ShortcutPackage.this.lambda$deleteOrDisableWithId$8(z, i, (ShortcutInfo) obj);
                    }
                });
            } else {
                forceDeleteShortcutInner(str);
                return findShortcutById;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteOrDisableWithId$8(boolean z, int i, ShortcutInfo shortcutInfo) {
        shortcutInfo.setRank(0);
        shortcutInfo.clearFlags(33);
        if (z) {
            shortcutInfo.addFlags(64);
            if (shortcutInfo.getDisabledReason() == 0) {
                shortcutInfo.setDisabledReason(i);
            }
        }
        shortcutInfo.setTimestamp(this.mShortcutUser.mService.injectCurrentTimeMillis());
        if (this.mShortcutUser.mService.isDummyMainActivity(shortcutInfo.getActivity())) {
            shortcutInfo.setActivity(null);
        }
    }

    public void enableWithId(String str) {
        mutateShortcut(str, null, new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda56
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.this.lambda$enableWithId$9((ShortcutInfo) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enableWithId$9(ShortcutInfo shortcutInfo) {
        ensureNotImmutable(shortcutInfo, true);
        shortcutInfo.clearFlags(64);
        shortcutInfo.setDisabledReason(0);
    }

    public void updateInvisibleShortcutForPinRequestWith(ShortcutInfo shortcutInfo) {
        Objects.requireNonNull(findShortcutById(shortcutInfo.getId()));
        this.mShortcutUser.mService.validateShortcutForPinRequest(shortcutInfo);
        shortcutInfo.addFlags(2);
        forceReplaceShortcutInner(shortcutInfo);
        adjustRanks();
    }

    public void refreshPinnedFlags() {
        final ArraySet arraySet = new ArraySet();
        this.mShortcutUser.forAllLaunchers(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda30
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.this.lambda$refreshPinnedFlags$10(arraySet, (ShortcutLauncher) obj);
            }
        });
        List<ShortcutInfo> findAll = findAll(arraySet);
        if (findAll != null) {
            findAll.forEach(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda31
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ShortcutPackage.lambda$refreshPinnedFlags$11((ShortcutInfo) obj);
                }
            });
        }
        forEachShortcutMutate(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda32
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.lambda$refreshPinnedFlags$12(arraySet, (ShortcutInfo) obj);
            }
        });
        this.mShortcutUser.forAllLaunchers(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda33
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ShortcutLauncher) obj).scheduleSave();
            }
        });
        removeOrphans();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshPinnedFlags$10(Set set, ShortcutLauncher shortcutLauncher) {
        ArraySet<String> pinnedShortcutIds = shortcutLauncher.getPinnedShortcutIds(getPackageName(), getPackageUserId());
        if (pinnedShortcutIds == null || pinnedShortcutIds.size() == 0) {
            return;
        }
        set.addAll(pinnedShortcutIds);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$refreshPinnedFlags$11(ShortcutInfo shortcutInfo) {
        if (shortcutInfo.isPinned()) {
            return;
        }
        shortcutInfo.addFlags(2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$refreshPinnedFlags$12(Set set, ShortcutInfo shortcutInfo) {
        if (set.contains(shortcutInfo.getId()) || !shortcutInfo.isPinned()) {
            return;
        }
        shortcutInfo.clearFlags(2);
    }

    public int getApiCallCount(boolean z) {
        ShortcutService shortcutService = this.mShortcutUser.mService;
        if (shortcutService.isUidForegroundLocked(this.mPackageUid) || this.mLastKnownForegroundElapsedTime < shortcutService.getUidLastForegroundElapsedTimeLocked(this.mPackageUid) || z) {
            this.mLastKnownForegroundElapsedTime = shortcutService.injectElapsedRealtime();
            resetRateLimiting();
        }
        long lastResetTimeLocked = shortcutService.getLastResetTimeLocked();
        long injectCurrentTimeMillis = shortcutService.injectCurrentTimeMillis();
        if (ShortcutService.isClockValid(injectCurrentTimeMillis) && this.mLastResetTime > injectCurrentTimeMillis) {
            Slog.w(TAG, "Clock rewound");
            this.mLastResetTime = injectCurrentTimeMillis;
            this.mApiCallCount = 0;
            return 0;
        }
        if (this.mLastResetTime < lastResetTimeLocked) {
            this.mApiCallCount = 0;
            this.mLastResetTime = lastResetTimeLocked;
        }
        return this.mApiCallCount;
    }

    public boolean tryApiCall(boolean z) {
        if (getApiCallCount(z) >= this.mShortcutUser.mService.mMaxUpdatesPerInterval) {
            return false;
        }
        this.mApiCallCount++;
        scheduleSave();
        return true;
    }

    public void resetRateLimiting() {
        if (this.mApiCallCount > 0) {
            this.mApiCallCount = 0;
            scheduleSave();
        }
    }

    public void resetRateLimitingForCommandLineNoSaving() {
        this.mApiCallCount = 0;
        this.mLastResetTime = 0L;
    }

    public void findAll(List<ShortcutInfo> list, Predicate<ShortcutInfo> predicate, int i) {
        findAll(list, predicate, i, null, 0, false);
    }

    public void findAll(final List<ShortcutInfo> list, final Predicate<ShortcutInfo> predicate, final int i, final String str, int i2, final boolean z) {
        if (getPackageInfo().isShadow()) {
            return;
        }
        final ArraySet<String> pinnedShortcutIds = str == null ? null : this.mShortcutUser.mService.getLauncherShortcutsLocked(str, getPackageUserId(), i2).getPinnedShortcutIds(getPackageName(), getPackageUserId());
        forEachShortcut(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.this.lambda$findAll$13(list, predicate, i, str, pinnedShortcutIds, z, (ShortcutInfo) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: filter, reason: merged with bridge method [inline-methods] */
    public void lambda$findAll$13(List<ShortcutInfo> list, Predicate<ShortcutInfo> predicate, int i, String str, ArraySet<String> arraySet, boolean z, ShortcutInfo shortcutInfo) {
        boolean z2 = str == null || (arraySet != null && arraySet.contains(shortcutInfo.getId()));
        if (z || !shortcutInfo.isFloating() || shortcutInfo.isCached() || z2) {
            ShortcutInfo clone = shortcutInfo.clone(i);
            if (!z && !z2) {
                clone.clearFlags(2);
            }
            if (predicate == null || predicate.test(clone)) {
                if (!z2) {
                    clone.clearFlags(2);
                }
                list.add(clone);
            }
        }
    }

    public void resetThrottling() {
        this.mApiCallCount = 0;
    }

    public List<ShortcutManager.ShareShortcutInfo> getMatchingShareTargets(IntentFilter intentFilter) {
        return getMatchingShareTargets(intentFilter, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<ShortcutManager.ShareShortcutInfo> getMatchingShareTargets(IntentFilter intentFilter, String str) {
        boolean z;
        synchronized (this.mLock) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.mShareTargets.size(); i++) {
                ShareTargetInfo shareTargetInfo = this.mShareTargets.get(i);
                ShareTargetInfo.TargetData[] targetDataArr = shareTargetInfo.mTargetData;
                int length = targetDataArr.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    if (intentFilter.hasDataType(targetDataArr[i2].mMimeType)) {
                        arrayList.add(shareTargetInfo);
                        break;
                    }
                    i2++;
                }
            }
            if (arrayList.isEmpty()) {
                return new ArrayList();
            }
            ArrayList arrayList2 = new ArrayList();
            findAll(arrayList2, new ShortcutPackage$$ExternalSyntheticLambda8(), 9, str, 0, false);
            ArrayList arrayList3 = new ArrayList();
            for (int i3 = 0; i3 < arrayList2.size(); i3++) {
                Set<String> categories = ((ShortcutInfo) arrayList2.get(i3)).getCategories();
                if (categories != null && !categories.isEmpty()) {
                    int i4 = 0;
                    while (true) {
                        if (i4 < arrayList.size()) {
                            ShareTargetInfo shareTargetInfo2 = (ShareTargetInfo) arrayList.get(i4);
                            int i5 = 0;
                            while (true) {
                                String[] strArr = shareTargetInfo2.mCategories;
                                if (i5 >= strArr.length) {
                                    z = true;
                                    break;
                                }
                                if (!categories.contains(strArr[i5])) {
                                    z = false;
                                    break;
                                }
                                i5++;
                            }
                            if (z) {
                                arrayList3.add(new ShortcutManager.ShareShortcutInfo((ShortcutInfo) arrayList2.get(i3), new ComponentName(getPackageName(), shareTargetInfo2.mTargetClass)));
                                break;
                            }
                            i4++;
                        }
                    }
                }
            }
            return arrayList3;
        }
    }

    public boolean hasShareTargets() {
        boolean z;
        synchronized (this.mLock) {
            z = !this.mShareTargets.isEmpty();
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSharingShortcutCount() {
        boolean z;
        synchronized (this.mLock) {
            if (this.mShareTargets.isEmpty()) {
                return 0;
            }
            ArrayList arrayList = new ArrayList();
            findAll(arrayList, new ShortcutPackage$$ExternalSyntheticLambda8(), 27);
            int i = 0;
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                Set<String> categories = ((ShortcutInfo) arrayList.get(i2)).getCategories();
                if (categories != null && !categories.isEmpty()) {
                    int i3 = 0;
                    while (true) {
                        if (i3 < this.mShareTargets.size()) {
                            ShareTargetInfo shareTargetInfo = this.mShareTargets.get(i3);
                            int i4 = 0;
                            while (true) {
                                String[] strArr = shareTargetInfo.mCategories;
                                if (i4 >= strArr.length) {
                                    z = true;
                                    break;
                                }
                                if (!categories.contains(strArr[i4])) {
                                    z = false;
                                    break;
                                }
                                i4++;
                            }
                            if (z) {
                                i++;
                                break;
                            }
                            i3++;
                        }
                    }
                }
            }
            return i;
        }
    }

    @GuardedBy({"mLock"})
    private ArraySet<String> getUsedBitmapFilesLocked() {
        final ArraySet<String> arraySet = new ArraySet<>(1);
        forEachShortcut(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.lambda$getUsedBitmapFilesLocked$14(arraySet, (ShortcutInfo) obj);
            }
        });
        return arraySet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getUsedBitmapFilesLocked$14(ArraySet arraySet, ShortcutInfo shortcutInfo) {
        if (shortcutInfo.getBitmapPath() != null) {
            arraySet.add(getFileName(shortcutInfo.getBitmapPath()));
        }
    }

    public void cleanupDanglingBitmapFiles(File file) {
        synchronized (this.mLock) {
            this.mShortcutBitmapSaver.waitForAllSavesLocked();
            ArraySet<String> usedBitmapFilesLocked = getUsedBitmapFilesLocked();
            for (File file2 : file.listFiles()) {
                if (file2.isFile() && !usedBitmapFilesLocked.contains(file2.getName())) {
                    file2.delete();
                }
            }
        }
    }

    private static String getFileName(String str) {
        int lastIndexOf = str.lastIndexOf(File.separatorChar);
        return lastIndexOf == -1 ? str : str.substring(lastIndexOf + 1);
    }

    private boolean areAllActivitiesStillEnabled() {
        final ShortcutService shortcutService = this.mShortcutUser.mService;
        final ArrayList arrayList = new ArrayList(4);
        final boolean[] zArr = new boolean[1];
        forEachShortcutStopWhen(new Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda29
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Boolean lambda$areAllActivitiesStillEnabled$15;
                lambda$areAllActivitiesStillEnabled$15 = ShortcutPackage.this.lambda$areAllActivitiesStillEnabled$15(arrayList, shortcutService, zArr, (ShortcutInfo) obj);
                return lambda$areAllActivitiesStillEnabled$15;
            }
        });
        return !zArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$areAllActivitiesStillEnabled$15(ArrayList arrayList, ShortcutService shortcutService, boolean[] zArr, ShortcutInfo shortcutInfo) {
        ComponentName activity = shortcutInfo.getActivity();
        if (arrayList.contains(activity)) {
            return Boolean.FALSE;
        }
        arrayList.add(activity);
        if (activity != null && !shortcutService.injectIsActivityEnabledAndExported(activity, getOwnerUserId())) {
            zArr[0] = true;
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public boolean rescanPackageIfNeeded(boolean z, boolean z2) {
        List<ShortcutInfo> list;
        final ShortcutService shortcutService = this.mShortcutUser.mService;
        long statStartTime = shortcutService.getStatStartTime();
        try {
            PackageInfo packageInfo = this.mShortcutUser.mService.getPackageInfo(getPackageName(), getPackageUserId());
            if (packageInfo == null) {
                return false;
            }
            if (!z && !z2 && getPackageInfo().getVersionCode() == packageInfo.getLongVersionCode() && getPackageInfo().getLastUpdateTime() == packageInfo.lastUpdateTime) {
                if (areAllActivitiesStillEnabled()) {
                    return false;
                }
            }
            shortcutService.logDurationStat(14, statStartTime);
            synchronized (this.mLock) {
                try {
                    this.mShareTargets.size();
                    list = ShortcutParser.parseShortcuts(this.mShortcutUser.mService, getPackageName(), getPackageUserId(), this.mShareTargets);
                } catch (IOException | XmlPullParserException e) {
                    Slog.e(TAG, "Failed to load shortcuts from AndroidManifest.xml.", e);
                    list = null;
                }
            }
            int size = list == null ? 0 : list.size();
            if (z && size == 0) {
                return false;
            }
            getPackageInfo().updateFromPackageInfo(packageInfo);
            final long versionCode = getPackageInfo().getVersionCode();
            forEachShortcutMutate(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda59
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ShortcutPackage.this.lambda$rescanPackageIfNeeded$16(versionCode, (ShortcutInfo) obj);
                }
            });
            if (!z) {
                final Resources packageResources = getPackageResources();
                forEachShortcutMutate(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda60
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ShortcutPackage.this.lambda$rescanPackageIfNeeded$17(shortcutService, packageResources, (ShortcutInfo) obj);
                    }
                });
            }
            publishManifestShortcuts(list);
            if (list != null) {
                pushOutExcessShortcuts();
            }
            shortcutService.verifyStates();
            shortcutService.packageShortcutsChanged(this, null, null);
            return true;
        } finally {
            shortcutService.logDurationStat(14, statStartTime);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rescanPackageIfNeeded$16(long j, ShortcutInfo shortcutInfo) {
        if (shortcutInfo.getDisabledReason() == 100 && getPackageInfo().getBackupSourceVersionCode() <= j) {
            Slog.i(TAG, String.format("Restoring shortcut: %s", shortcutInfo.getId()));
            shortcutInfo.clearFlags(64);
            shortcutInfo.setDisabledReason(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rescanPackageIfNeeded$17(ShortcutService shortcutService, Resources resources, ShortcutInfo shortcutInfo) {
        if (shortcutInfo.isDynamic()) {
            if (shortcutInfo.getActivity() == null) {
                shortcutService.wtf("null activity detected.");
            } else if (!shortcutService.injectIsMainActivity(shortcutInfo.getActivity(), getPackageUserId())) {
                Slog.w(TAG, String.format("%s is no longer main activity. Disabling shorcut %s.", getPackageName(), shortcutInfo.getId()));
                if (disableDynamicWithId(shortcutInfo.getId(), false, 2, false) != null) {
                    return;
                }
            }
        }
        if (!shortcutInfo.hasAnyResources() || resources == null) {
            return;
        }
        if (!shortcutInfo.isOriginallyFromManifest()) {
            shortcutInfo.lookupAndFillInResourceIds(resources);
        }
        shortcutInfo.setTimestamp(shortcutService.injectCurrentTimeMillis());
    }

    private boolean publishManifestShortcuts(List<ShortcutInfo> list) {
        boolean z;
        final ArraySet arraySet = new ArraySet(1);
        forEachShortcut(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.lambda$publishManifestShortcuts$18(arraySet, (ShortcutInfo) obj);
            }
        });
        boolean z2 = false;
        if (list != null) {
            int size = list.size();
            int i = 0;
            boolean z3 = false;
            while (i < size) {
                ShortcutInfo shortcutInfo = list.get(i);
                boolean z4 = !shortcutInfo.isEnabled();
                String id = shortcutInfo.getId();
                ShortcutInfo findShortcutById = findShortcutById(id);
                if (findShortcutById != null) {
                    if (!findShortcutById.isOriginallyFromManifest()) {
                        Slog.e(TAG, "Shortcut with ID=" + shortcutInfo.getId() + " exists but is not from AndroidManifest.xml, not updating.");
                        i++;
                        z3 = true;
                    } else if (findShortcutById.isPinned()) {
                        shortcutInfo.addFlags(2);
                        z = true;
                        if (z4 || z) {
                            forceReplaceShortcutInner(shortcutInfo);
                            if (!z4 && !arraySet.isEmpty()) {
                                arraySet.remove(id);
                            }
                        }
                        i++;
                        z3 = true;
                    }
                }
                z = false;
                if (z4) {
                }
                forceReplaceShortcutInner(shortcutInfo);
                if (!z4) {
                    arraySet.remove(id);
                }
                i++;
                z3 = true;
            }
            z2 = z3;
        }
        if (!arraySet.isEmpty()) {
            int size2 = arraySet.size() - 1;
            while (size2 >= 0) {
                disableWithId((String) arraySet.valueAt(size2), null, 0, true, false, 2);
                size2--;
                z2 = true;
            }
            removeOrphans();
        }
        adjustRanks();
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$publishManifestShortcuts$18(ArraySet arraySet, ShortcutInfo shortcutInfo) {
        if (shortcutInfo.isManifestShortcut()) {
            arraySet.add(shortcutInfo.getId());
        }
    }

    private boolean pushOutExcessShortcuts() {
        ShortcutService shortcutService = this.mShortcutUser.mService;
        int maxActivityShortcuts = shortcutService.getMaxActivityShortcuts();
        ArrayMap<ComponentName, ArrayList<ShortcutInfo>> sortShortcutsToActivities = sortShortcutsToActivities();
        for (int size = sortShortcutsToActivities.size() - 1; size >= 0; size--) {
            ArrayList<ShortcutInfo> valueAt = sortShortcutsToActivities.valueAt(size);
            if (valueAt.size() > maxActivityShortcuts) {
                Collections.sort(valueAt, this.mShortcutTypeAndRankComparator);
                for (int size2 = valueAt.size() - 1; size2 >= maxActivityShortcuts; size2--) {
                    ShortcutInfo shortcutInfo = valueAt.get(size2);
                    if (shortcutInfo.isManifestShortcut()) {
                        shortcutService.wtf("Found manifest shortcuts in excess list.");
                    } else {
                        deleteDynamicWithId(shortcutInfo.getId(), true, true);
                    }
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$new$19(ShortcutInfo shortcutInfo, ShortcutInfo shortcutInfo2) {
        if (shortcutInfo.isManifestShortcut() && !shortcutInfo2.isManifestShortcut()) {
            return -1;
        }
        if (shortcutInfo.isManifestShortcut() || !shortcutInfo2.isManifestShortcut()) {
            return Integer.compare(shortcutInfo.getRank(), shortcutInfo2.getRank());
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$new$20(ShortcutInfo shortcutInfo, ShortcutInfo shortcutInfo2) {
        if (shortcutInfo.isDeclaredInManifest() && !shortcutInfo2.isDeclaredInManifest()) {
            return -1;
        }
        if (!shortcutInfo.isDeclaredInManifest() && shortcutInfo2.isDeclaredInManifest()) {
            return 1;
        }
        if (shortcutInfo.isDynamic() && shortcutInfo2.isDynamic()) {
            return Integer.compare(shortcutInfo.getRank(), shortcutInfo2.getRank());
        }
        if (shortcutInfo.isDynamic()) {
            return -1;
        }
        if (shortcutInfo2.isDynamic()) {
            return 1;
        }
        if (shortcutInfo.isCached() && shortcutInfo2.isCached()) {
            if (shortcutInfo.hasFlags(536870912) && !shortcutInfo2.hasFlags(536870912)) {
                return -1;
            }
            if (!shortcutInfo.hasFlags(536870912) && shortcutInfo2.hasFlags(536870912)) {
                return 1;
            }
            if (shortcutInfo.hasFlags(1073741824) && !shortcutInfo2.hasFlags(1073741824)) {
                return -1;
            }
            if (!shortcutInfo.hasFlags(1073741824) && shortcutInfo2.hasFlags(1073741824)) {
                return 1;
            }
        }
        if (shortcutInfo.isCached()) {
            return -1;
        }
        if (shortcutInfo2.isCached()) {
            return 1;
        }
        return Long.compare(shortcutInfo2.getLastChangedTimestamp(), shortcutInfo.getLastChangedTimestamp());
    }

    private ArrayMap<ComponentName, ArrayList<ShortcutInfo>> sortShortcutsToActivities() {
        final ArrayMap<ComponentName, ArrayList<ShortcutInfo>> arrayMap = new ArrayMap<>();
        forEachShortcut(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda52
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.this.lambda$sortShortcutsToActivities$22(arrayMap, (ShortcutInfo) obj);
            }
        });
        return arrayMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sortShortcutsToActivities$22(ArrayMap arrayMap, ShortcutInfo shortcutInfo) {
        if (shortcutInfo.isFloating()) {
            return;
        }
        ComponentName activity = shortcutInfo.getActivity();
        if (activity == null) {
            this.mShortcutUser.mService.wtf("null activity detected.");
        } else {
            ((ArrayList) arrayMap.computeIfAbsent(activity, new Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    ArrayList lambda$sortShortcutsToActivities$21;
                    lambda$sortShortcutsToActivities$21 = ShortcutPackage.lambda$sortShortcutsToActivities$21((ComponentName) obj);
                    return lambda$sortShortcutsToActivities$21;
                }
            })).add(shortcutInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ArrayList lambda$sortShortcutsToActivities$21(ComponentName componentName) {
        return new ArrayList();
    }

    private void incrementCountForActivity(ArrayMap<ComponentName, Integer> arrayMap, ComponentName componentName, int i) {
        Integer num = arrayMap.get(componentName);
        if (num == null) {
            num = 0;
        }
        arrayMap.put(componentName, Integer.valueOf(num.intValue() + i));
    }

    public void enforceShortcutCountsBeforeOperation(List<ShortcutInfo> list, final int i) {
        ShortcutService shortcutService = this.mShortcutUser.mService;
        final ArrayMap<ComponentName, Integer> arrayMap = new ArrayMap<>(4);
        forEachShortcut(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda55
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.this.lambda$enforceShortcutCountsBeforeOperation$23(arrayMap, i, (ShortcutInfo) obj);
            }
        });
        for (int size = list.size() - 1; size >= 0; size--) {
            ShortcutInfo shortcutInfo = list.get(size);
            ComponentName activity = shortcutInfo.getActivity();
            if (activity != null) {
                ShortcutInfo findShortcutById = findShortcutById(shortcutInfo.getId());
                if (findShortcutById == null) {
                    if (i != 2) {
                        incrementCountForActivity(arrayMap, activity, 1);
                    }
                } else if (!findShortcutById.isFloating() || i != 2) {
                    if (i != 0) {
                        ComponentName activity2 = findShortcutById.getActivity();
                        if (!findShortcutById.isFloating()) {
                            incrementCountForActivity(arrayMap, activity2, -1);
                        }
                    }
                    incrementCountForActivity(arrayMap, activity, 1);
                }
            } else if (i != 2) {
                shortcutService.wtf("Activity must not be null at this point");
            }
        }
        for (int size2 = arrayMap.size() - 1; size2 >= 0; size2--) {
            shortcutService.enforceMaxActivityShortcuts(arrayMap.valueAt(size2).intValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enforceShortcutCountsBeforeOperation$23(ArrayMap arrayMap, int i, ShortcutInfo shortcutInfo) {
        if (shortcutInfo.isManifestShortcut()) {
            incrementCountForActivity(arrayMap, shortcutInfo.getActivity(), 1);
        } else {
            if (!shortcutInfo.isDynamic() || i == 0) {
                return;
            }
            incrementCountForActivity(arrayMap, shortcutInfo.getActivity(), 1);
        }
    }

    public void resolveResourceStrings() {
        final ShortcutService shortcutService = this.mShortcutUser.mService;
        final Resources packageResources = getPackageResources();
        final ArrayList arrayList = new ArrayList(1);
        if (packageResources != null) {
            forEachShortcutMutate(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda18
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ShortcutPackage.lambda$resolveResourceStrings$24(packageResources, shortcutService, arrayList, (ShortcutInfo) obj);
                }
            });
        }
        if (CollectionUtils.isEmpty(arrayList)) {
            return;
        }
        shortcutService.packageShortcutsChanged(this, arrayList, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$resolveResourceStrings$24(Resources resources, ShortcutService shortcutService, List list, ShortcutInfo shortcutInfo) {
        if (shortcutInfo.hasStringResources()) {
            shortcutInfo.resolveResourceStrings(resources);
            shortcutInfo.setTimestamp(shortcutService.injectCurrentTimeMillis());
            list.add(shortcutInfo);
        }
    }

    public void clearAllImplicitRanks() {
        forEachShortcutMutate(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda48
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ShortcutInfo) obj).clearImplicitRankAndRankChangedFlag();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$new$25(ShortcutInfo shortcutInfo, ShortcutInfo shortcutInfo2) {
        int compare = Integer.compare(shortcutInfo.getRank(), shortcutInfo2.getRank());
        if (compare != 0) {
            return compare;
        }
        if (shortcutInfo.isRankChanged() != shortcutInfo2.isRankChanged()) {
            return shortcutInfo.isRankChanged() ? -1 : 1;
        }
        int compare2 = Integer.compare(shortcutInfo.getImplicitRank(), shortcutInfo2.getImplicitRank());
        return compare2 != 0 ? compare2 : shortcutInfo.getId().compareTo(shortcutInfo2.getId());
    }

    public void adjustRanks() {
        ShortcutService shortcutService = this.mShortcutUser.mService;
        final long injectCurrentTimeMillis = shortcutService.injectCurrentTimeMillis();
        forEachShortcutMutate(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.lambda$adjustRanks$26(injectCurrentTimeMillis, (ShortcutInfo) obj);
            }
        });
        ArrayMap<ComponentName, ArrayList<ShortcutInfo>> sortShortcutsToActivities = sortShortcutsToActivities();
        for (int size = sortShortcutsToActivities.size() - 1; size >= 0; size--) {
            ArrayList<ShortcutInfo> valueAt = sortShortcutsToActivities.valueAt(size);
            Collections.sort(valueAt, this.mShortcutRankComparator);
            int size2 = valueAt.size();
            final int i = 0;
            for (int i2 = 0; i2 < size2; i2++) {
                ShortcutInfo shortcutInfo = valueAt.get(i2);
                if (!shortcutInfo.isManifestShortcut()) {
                    if (shortcutInfo.isDynamic()) {
                        int i3 = i + 1;
                        if (shortcutInfo.getRank() != i) {
                            mutateShortcut(shortcutInfo.getId(), shortcutInfo, new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda11
                                @Override // java.util.function.Consumer
                                public final void accept(Object obj) {
                                    ShortcutPackage.lambda$adjustRanks$27(injectCurrentTimeMillis, i, (ShortcutInfo) obj);
                                }
                            });
                        }
                        i = i3;
                    } else {
                        shortcutService.wtf("Non-dynamic shortcut found. " + shortcutInfo.toInsecureString());
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$adjustRanks$26(long j, ShortcutInfo shortcutInfo) {
        if (!shortcutInfo.isFloating() || shortcutInfo.getRank() == 0) {
            return;
        }
        shortcutInfo.setTimestamp(j);
        shortcutInfo.setRank(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$adjustRanks$27(long j, int i, ShortcutInfo shortcutInfo) {
        shortcutInfo.setTimestamp(j);
        shortcutInfo.setRank(i);
    }

    public boolean hasNonManifestShortcuts() {
        final boolean[] zArr = new boolean[1];
        forEachShortcutStopWhen(new Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda53
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Boolean lambda$hasNonManifestShortcuts$28;
                lambda$hasNonManifestShortcuts$28 = ShortcutPackage.lambda$hasNonManifestShortcuts$28(zArr, (ShortcutInfo) obj);
                return lambda$hasNonManifestShortcuts$28;
            }
        });
        return zArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean lambda$hasNonManifestShortcuts$28(boolean[] zArr, ShortcutInfo shortcutInfo) {
        if (!shortcutInfo.isDeclaredInManifest()) {
            zArr[0] = true;
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportShortcutUsed(UsageStatsManagerInternal usageStatsManagerInternal, String str) {
        long injectClearCallingIdentity;
        synchronized (this.mLock) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            ShortcutService shortcutService = this.mShortcutUser.mService;
            try {
                if (!this.mLastReportedTime.isEmpty() && this.mLastReportedTime.size() >= 3) {
                    if (elapsedRealtime - this.mLastReportedTime.get(0).longValue() > shortcutService.mSaveDelayMillis) {
                        this.mLastReportedTime.remove(0);
                        this.mLastReportedTime.add(Long.valueOf(elapsedRealtime));
                        injectClearCallingIdentity = shortcutService.injectClearCallingIdentity();
                        usageStatsManagerInternal.reportShortcutUsage(getPackageName(), str, getUser().getUserId());
                        return;
                    }
                    return;
                }
                usageStatsManagerInternal.reportShortcutUsage(getPackageName(), str, getUser().getUserId());
                return;
            } finally {
                shortcutService.injectRestoreCallingIdentity(injectClearCallingIdentity);
            }
            this.mLastReportedTime.add(Long.valueOf(elapsedRealtime));
            injectClearCallingIdentity = shortcutService.injectClearCallingIdentity();
        }
    }

    public void dump(final PrintWriter printWriter, final String str, ShortcutService.DumpFilter dumpFilter) {
        printWriter.println();
        printWriter.print(str);
        printWriter.print("Package: ");
        printWriter.print(getPackageName());
        printWriter.print("  UID: ");
        printWriter.print(this.mPackageUid);
        printWriter.println();
        printWriter.print(str);
        printWriter.print("  ");
        printWriter.print("Calls: ");
        printWriter.print(getApiCallCount(false));
        printWriter.println();
        printWriter.print(str);
        printWriter.print("  ");
        printWriter.print("Last known FG: ");
        printWriter.print(this.mLastKnownForegroundElapsedTime);
        printWriter.println();
        printWriter.print(str);
        printWriter.print("  ");
        printWriter.print("Last reset: [");
        printWriter.print(this.mLastResetTime);
        printWriter.print("] ");
        printWriter.print(ShortcutService.formatTime(this.mLastResetTime));
        printWriter.println();
        getPackageInfo().dump(printWriter, str + "  ");
        printWriter.println();
        printWriter.print(str);
        printWriter.println("  Shortcuts:");
        final long[] jArr = new long[1];
        forEachShortcut(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.lambda$dump$29(printWriter, str, jArr, (ShortcutInfo) obj);
            }
        });
        printWriter.print(str);
        printWriter.print("  ");
        printWriter.print("Total bitmap size: ");
        printWriter.print(jArr[0]);
        printWriter.print(" (");
        printWriter.print(Formatter.formatFileSize(this.mShortcutUser.mService.mContext, jArr[0]));
        printWriter.println(")");
        printWriter.println();
        synchronized (this.mLock) {
            this.mShortcutBitmapSaver.dumpLocked(printWriter, "  ");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$29(PrintWriter printWriter, String str, long[] jArr, ShortcutInfo shortcutInfo) {
        printWriter.println(shortcutInfo.toDumpString(str + "    "));
        if (shortcutInfo.getBitmapPath() != null) {
            long length = new File(shortcutInfo.getBitmapPath()).length();
            printWriter.print(str);
            printWriter.print("      ");
            printWriter.print("bitmap size=");
            printWriter.println(length);
            jArr[0] = jArr[0] + length;
        }
    }

    public void dumpShortcuts(final PrintWriter printWriter, int i) {
        final int i2 = ((i & 4) != 0 ? 2 : 0) | ((i & 2) != 0 ? 1 : 0) | ((i & 1) != 0 ? 32 : 0) | ((i & 8) != 0 ? 1610629120 : 0);
        forEachShortcut(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.lambda$dumpShortcuts$30(i2, printWriter, (ShortcutInfo) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dumpShortcuts$30(int i, PrintWriter printWriter, ShortcutInfo shortcutInfo) {
        if ((i & shortcutInfo.getFlags()) != 0) {
            printWriter.println(shortcutInfo.toDumpString(""));
        }
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    public JSONObject dumpCheckin(boolean z) throws JSONException {
        JSONObject dumpCheckin = super.dumpCheckin(z);
        final int[] iArr = new int[1];
        final int[] iArr2 = new int[1];
        final int[] iArr3 = new int[1];
        final int[] iArr4 = new int[1];
        final long[] jArr = new long[1];
        forEachShortcut(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda22
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.lambda$dumpCheckin$31(iArr, iArr3, iArr2, iArr4, jArr, (ShortcutInfo) obj);
            }
        });
        dumpCheckin.put(KEY_DYNAMIC, iArr[0]);
        dumpCheckin.put("manifest", iArr3[0]);
        dumpCheckin.put(KEY_PINNED, iArr2[0]);
        dumpCheckin.put(KEY_BITMAPS, iArr4[0]);
        dumpCheckin.put(KEY_BITMAP_BYTES, jArr[0]);
        return dumpCheckin;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dumpCheckin$31(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, long[] jArr, ShortcutInfo shortcutInfo) {
        if (shortcutInfo.isDynamic()) {
            iArr[0] = iArr[0] + 1;
        }
        if (shortcutInfo.isDeclaredInManifest()) {
            iArr2[0] = iArr2[0] + 1;
        }
        if (shortcutInfo.isPinned()) {
            iArr3[0] = iArr3[0] + 1;
        }
        if (shortcutInfo.getBitmapPath() != null) {
            iArr4[0] = iArr4[0] + 1;
            jArr[0] = jArr[0] + new File(shortcutInfo.getBitmapPath()).length();
        }
    }

    private boolean hasNoShortcut() {
        if (!isAppSearchEnabled()) {
            return getShortcutCount() == 0;
        }
        final boolean[] zArr = new boolean[1];
        forEachShortcutStopWhen(new Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda13
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Boolean lambda$hasNoShortcut$32;
                lambda$hasNoShortcut$32 = ShortcutPackage.lambda$hasNoShortcut$32(zArr, (ShortcutInfo) obj);
                return lambda$hasNoShortcut$32;
            }
        });
        return !zArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean lambda$hasNoShortcut$32(boolean[] zArr, ShortcutInfo shortcutInfo) {
        zArr[0] = true;
        return Boolean.TRUE;
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    public void saveToXml(TypedXmlSerializer typedXmlSerializer, boolean z) throws IOException, XmlPullParserException {
        synchronized (this.mLock) {
            int size = this.mShortcuts.size();
            int size2 = this.mShareTargets.size();
            if (hasNoShortcut() && size2 == 0 && this.mApiCallCount == 0) {
                return;
            }
            typedXmlSerializer.startTag((String) null, "package");
            ShortcutService.writeAttr(typedXmlSerializer, "name", getPackageName());
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_CALL_COUNT, this.mApiCallCount);
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_LAST_RESET, this.mLastResetTime);
            if (!z) {
                ShortcutService.writeAttr(typedXmlSerializer, ATTR_SCHEMA_VERSON, this.mIsAppSearchSchemaUpToDate ? 3L : 0L);
            }
            getPackageInfo().saveToXml(this.mShortcutUser.mService, typedXmlSerializer, z);
            for (int i = 0; i < size; i++) {
                saveShortcut(typedXmlSerializer, this.mShortcuts.valueAt(i), z, getPackageInfo().isBackupAllowed());
            }
            if (!z) {
                for (int i2 = 0; i2 < size2; i2++) {
                    this.mShareTargets.get(i2).saveToXml(typedXmlSerializer);
                }
            }
            typedXmlSerializer.endTag((String) null, "package");
        }
    }

    private void saveShortcut(TypedXmlSerializer typedXmlSerializer, ShortcutInfo shortcutInfo, boolean z, boolean z2) throws IOException, XmlPullParserException {
        ShortcutService shortcutService = this.mShortcutUser.mService;
        if (!z || (shortcutInfo.isPinned() && shortcutInfo.isEnabled())) {
            boolean z3 = !z || z2;
            if (shortcutInfo.isIconPendingSave()) {
                removeIcon(shortcutInfo);
            }
            typedXmlSerializer.startTag((String) null, TAG_SHORTCUT);
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_ID, shortcutInfo.getId());
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_ACTIVITY, shortcutInfo.getActivity());
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_TITLE, shortcutInfo.getTitle());
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_TITLE_RES_ID, shortcutInfo.getTitleResId());
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_TITLE_RES_NAME, shortcutInfo.getTitleResName());
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_SPLASH_SCREEN_THEME_NAME, shortcutInfo.getStartingThemeResName());
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_TEXT, shortcutInfo.getText());
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_TEXT_RES_ID, shortcutInfo.getTextResId());
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_TEXT_RES_NAME, shortcutInfo.getTextResName());
            if (z3) {
                ShortcutService.writeAttr(typedXmlSerializer, ATTR_DISABLED_MESSAGE, shortcutInfo.getDisabledMessage());
                ShortcutService.writeAttr(typedXmlSerializer, ATTR_DISABLED_MESSAGE_RES_ID, shortcutInfo.getDisabledMessageResourceId());
                ShortcutService.writeAttr(typedXmlSerializer, ATTR_DISABLED_MESSAGE_RES_NAME, shortcutInfo.getDisabledMessageResName());
            }
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_DISABLED_REASON, shortcutInfo.getDisabledReason());
            ShortcutService.writeAttr(typedXmlSerializer, "timestamp", shortcutInfo.getLastChangedTimestamp());
            if (shortcutInfo.getLocusId() != null) {
                ShortcutService.writeAttr(typedXmlSerializer, ATTR_LOCUS_ID, shortcutInfo.getLocusId().getId());
            }
            if (z) {
                ShortcutService.writeAttr(typedXmlSerializer, ATTR_FLAGS, shortcutInfo.getFlags() & (-35342));
                if (getPackageInfo().getVersionCode() == 0) {
                    shortcutService.wtf("Package version code should be available at this point.");
                }
            } else {
                ShortcutService.writeAttr(typedXmlSerializer, ATTR_RANK, shortcutInfo.getRank());
                ShortcutService.writeAttr(typedXmlSerializer, ATTR_FLAGS, shortcutInfo.getFlags());
                ShortcutService.writeAttr(typedXmlSerializer, ATTR_ICON_RES_ID, shortcutInfo.getIconResourceId());
                ShortcutService.writeAttr(typedXmlSerializer, ATTR_ICON_RES_NAME, shortcutInfo.getIconResName());
                ShortcutService.writeAttr(typedXmlSerializer, ATTR_BITMAP_PATH, shortcutInfo.getBitmapPath());
                ShortcutService.writeAttr(typedXmlSerializer, ATTR_ICON_URI, shortcutInfo.getIconUri());
            }
            if (z3) {
                Set<String> categories = shortcutInfo.getCategories();
                if (categories != null && categories.size() > 0) {
                    typedXmlSerializer.startTag((String) null, "categories");
                    XmlUtils.writeStringArrayXml((String[]) categories.toArray(new String[categories.size()]), "categories", XmlUtils.makeTyped(typedXmlSerializer));
                    typedXmlSerializer.endTag((String) null, "categories");
                }
                if (!z) {
                    Person[] persons = shortcutInfo.getPersons();
                    if (!ArrayUtils.isEmpty(persons)) {
                        for (Person person : persons) {
                            typedXmlSerializer.startTag((String) null, TAG_PERSON);
                            ShortcutService.writeAttr(typedXmlSerializer, "name", person.getName());
                            ShortcutService.writeAttr(typedXmlSerializer, ATTR_PERSON_URI, person.getUri());
                            ShortcutService.writeAttr(typedXmlSerializer, ATTR_PERSON_KEY, person.getKey());
                            ShortcutService.writeAttr(typedXmlSerializer, ATTR_PERSON_IS_BOT, person.isBot());
                            ShortcutService.writeAttr(typedXmlSerializer, ATTR_PERSON_IS_IMPORTANT, person.isImportant());
                            typedXmlSerializer.endTag((String) null, TAG_PERSON);
                        }
                    }
                }
                Intent[] intentsNoExtras = shortcutInfo.getIntentsNoExtras();
                PersistableBundle[] intentPersistableExtrases = shortcutInfo.getIntentPersistableExtrases();
                if (intentsNoExtras != null && intentPersistableExtrases != null) {
                    int length = intentsNoExtras.length;
                    for (int i = 0; i < length; i++) {
                        typedXmlSerializer.startTag((String) null, "intent");
                        ShortcutService.writeAttr(typedXmlSerializer, ATTR_INTENT_NO_EXTRA, intentsNoExtras[i]);
                        ShortcutService.writeTagExtra(typedXmlSerializer, TAG_EXTRAS, intentPersistableExtrases[i]);
                        typedXmlSerializer.endTag((String) null, "intent");
                    }
                }
                ShortcutService.writeTagExtra(typedXmlSerializer, TAG_EXTRAS, shortcutInfo.getExtras());
                Map capabilityBindingsInternal = shortcutInfo.getCapabilityBindingsInternal();
                if (capabilityBindingsInternal != null && !capabilityBindingsInternal.isEmpty()) {
                    XmlUtils.writeMapXml(capabilityBindingsInternal, NAME_CAPABILITY, typedXmlSerializer);
                }
            }
            typedXmlSerializer.endTag((String) null, TAG_SHORTCUT);
        }
    }

    public static ShortcutPackage loadFromFile(ShortcutService shortcutService, ShortcutUser shortcutUser, File file, boolean z) {
        FileInputStream openRead;
        AtomicFile atomicFile = new AtomicFile(file);
        try {
            try {
                openRead = atomicFile.openRead();
                TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(openRead);
                ShortcutPackage shortcutPackage = null;
                while (true) {
                    int next = resolvePullParser.next();
                    if (next == 1) {
                        return shortcutPackage;
                    }
                    if (next == 2) {
                        int depth = resolvePullParser.getDepth();
                        String name = resolvePullParser.getName();
                        if (depth == 1 && "package".equals(name)) {
                            shortcutPackage = loadFromXml(shortcutService, shortcutUser, resolvePullParser, z);
                        } else {
                            ShortcutService.throwForInvalidTag(depth, name);
                        }
                    }
                }
            } catch (IOException | XmlPullParserException e) {
                Slog.e(TAG, "Failed to read file " + atomicFile.getBaseFile(), e);
                return null;
            } finally {
                IoUtils.closeQuietly(openRead);
            }
        } catch (FileNotFoundException unused) {
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00ba A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x008e A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ShortcutPackage loadFromXml(ShortcutService shortcutService, ShortcutUser shortcutUser, TypedXmlPullParser typedXmlPullParser, boolean z) throws IOException, XmlPullParserException {
        char c;
        String parseStringAttribute = ShortcutService.parseStringAttribute(typedXmlPullParser, "name");
        ShortcutPackage shortcutPackage = new ShortcutPackage(shortcutUser, shortcutUser.getUserId(), parseStringAttribute);
        synchronized (shortcutPackage.mLock) {
            shortcutPackage.mIsAppSearchSchemaUpToDate = ShortcutService.parseIntAttribute(typedXmlPullParser, ATTR_SCHEMA_VERSON, 0) == 3;
            shortcutPackage.mApiCallCount = ShortcutService.parseIntAttribute(typedXmlPullParser, ATTR_CALL_COUNT);
            shortcutPackage.mLastResetTime = ShortcutService.parseLongAttribute(typedXmlPullParser, ATTR_LAST_RESET);
            int depth = typedXmlPullParser.getDepth();
            while (true) {
                int next = typedXmlPullParser.next();
                if (next == 1 || (next == 3 && typedXmlPullParser.getDepth() <= depth)) {
                    break;
                }
                if (next == 2) {
                    int depth2 = typedXmlPullParser.getDepth();
                    String name = typedXmlPullParser.getName();
                    if (depth2 == depth + 1) {
                        int hashCode = name.hashCode();
                        if (hashCode == -1923478059) {
                            if (name.equals("package-info")) {
                                c = 0;
                                if (c != 0) {
                                }
                            }
                            c = 65535;
                            if (c != 0) {
                            }
                        } else if (hashCode != -1680817345) {
                            if (hashCode == -342500282 && name.equals(TAG_SHORTCUT)) {
                                c = 1;
                                if (c != 0) {
                                    shortcutPackage.getPackageInfo().loadFromXml(typedXmlPullParser, z);
                                } else if (c == 1) {
                                    try {
                                        ShortcutInfo parseShortcut = parseShortcut(typedXmlPullParser, parseStringAttribute, shortcutUser.getUserId(), z);
                                        shortcutPackage.mShortcuts.put(parseShortcut.getId(), parseShortcut);
                                    } catch (IOException e) {
                                        throw e;
                                    } catch (Exception e2) {
                                        Slog.e(TAG, "Failed parsing shortcut.", e2);
                                    }
                                } else if (c == 2) {
                                    shortcutPackage.mShareTargets.add(ShareTargetInfo.loadFromXml(typedXmlPullParser));
                                }
                            }
                            c = 65535;
                            if (c != 0) {
                            }
                        } else {
                            if (name.equals(TAG_SHARE_TARGET)) {
                                c = 2;
                                if (c != 0) {
                                }
                            }
                            c = 65535;
                            if (c != 0) {
                            }
                        }
                    }
                    ShortcutService.warnForInvalidTag(depth2, name);
                }
            }
        }
        return shortcutPackage;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x01d1, code lost:
    
        if (r5 == null) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x01d3, code lost:
    
        android.content.pm.ShortcutInfo.setIntentExtras(r5, r35);
        r1.clear();
        r1.add(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x01de, code lost:
    
        if (r3 != 0) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x01e2, code lost:
    
        if ((r7 & 64) == 0) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x01e4, code lost:
    
        r3 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x01e5, code lost:
    
        if (r45 == false) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x01e7, code lost:
    
        r0 = r7 | 4096;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x01eb, code lost:
    
        if (r9 != null) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x01ed, code lost:
    
        r35 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x023b, code lost:
    
        return new android.content.pm.ShortcutInfo(r44, r6, r43, r8, null, r10, r37, r39, r32, r14, r15, r16, r17, r18, r26, (android.content.Intent[]) r1.toArray(new android.content.Intent[r1.size()]), r15, r30, r23, r0, r15, r27, r28, r29, r3, (android.app.Person[]) r2.toArray(new android.app.Person[r2.size()]), r35, r33, r34);
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x01f0, code lost:
    
        r35 = new android.content.LocusId(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x01ea, code lost:
    
        r0 = r7;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:33:0x00f2. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:36:0x015b. Please report as an issue. */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static ShortcutInfo parseShortcut(TypedXmlPullParser typedXmlPullParser, String str, int i, boolean z) throws IOException, XmlPullParserException {
        int i2;
        String str2;
        int i3;
        int i4;
        String str3;
        int i5;
        char c;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        String parseStringAttribute = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_ID);
        ComponentName parseComponentNameAttribute = ShortcutService.parseComponentNameAttribute(typedXmlPullParser, ATTR_ACTIVITY);
        String parseStringAttribute2 = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_TITLE);
        int parseIntAttribute = ShortcutService.parseIntAttribute(typedXmlPullParser, ATTR_TITLE_RES_ID);
        String parseStringAttribute3 = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_TITLE_RES_NAME);
        String parseStringAttribute4 = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_SPLASH_SCREEN_THEME_NAME);
        String parseStringAttribute5 = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_TEXT);
        int parseIntAttribute2 = ShortcutService.parseIntAttribute(typedXmlPullParser, ATTR_TEXT_RES_ID);
        String parseStringAttribute6 = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_TEXT_RES_NAME);
        String parseStringAttribute7 = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_DISABLED_MESSAGE);
        int parseIntAttribute3 = ShortcutService.parseIntAttribute(typedXmlPullParser, ATTR_DISABLED_MESSAGE_RES_ID);
        String parseStringAttribute8 = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_DISABLED_MESSAGE_RES_NAME);
        int parseIntAttribute4 = ShortcutService.parseIntAttribute(typedXmlPullParser, ATTR_DISABLED_REASON);
        Intent parseIntentAttributeNoDefault = ShortcutService.parseIntentAttributeNoDefault(typedXmlPullParser, "intent");
        int parseLongAttribute = (int) ShortcutService.parseLongAttribute(typedXmlPullParser, ATTR_RANK);
        long parseLongAttribute2 = ShortcutService.parseLongAttribute(typedXmlPullParser, "timestamp");
        int parseLongAttribute3 = (int) ShortcutService.parseLongAttribute(typedXmlPullParser, ATTR_FLAGS);
        int parseLongAttribute4 = (int) ShortcutService.parseLongAttribute(typedXmlPullParser, ATTR_ICON_RES_ID);
        String parseStringAttribute9 = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_ICON_RES_NAME);
        String parseStringAttribute10 = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_BITMAP_PATH);
        String parseStringAttribute11 = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_ICON_URI);
        String parseStringAttribute12 = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_LOCUS_ID);
        int depth = typedXmlPullParser.getDepth();
        ArraySet arraySet = null;
        PersistableBundle persistableBundle = null;
        Map map = null;
        PersistableBundle persistableBundle2 = null;
        while (true) {
            int next = typedXmlPullParser.next();
            String str4 = parseStringAttribute5;
            if (next == 1) {
                i2 = parseIntAttribute;
                str2 = parseStringAttribute3;
                i3 = 1;
            } else if (next != 3 || typedXmlPullParser.getDepth() > depth) {
                if (next != 2) {
                    i5 = parseIntAttribute;
                    str3 = parseStringAttribute3;
                    i4 = depth;
                } else {
                    int depth2 = typedXmlPullParser.getDepth();
                    String name = typedXmlPullParser.getName();
                    name.hashCode();
                    i4 = depth;
                    str3 = parseStringAttribute3;
                    char c2 = 65535;
                    switch (name.hashCode()) {
                        case -1289032093:
                            i5 = parseIntAttribute;
                            if (name.equals(TAG_EXTRAS)) {
                                c2 = 0;
                                break;
                            }
                            break;
                        case -1183762788:
                            i5 = parseIntAttribute;
                            if (name.equals("intent")) {
                                c2 = 1;
                                break;
                            }
                            break;
                        case -1044333900:
                            i5 = parseIntAttribute;
                            if (name.equals(TAG_INTENT_EXTRAS_LEGACY)) {
                                c2 = 2;
                                break;
                            }
                            break;
                        case -1024600675:
                            i5 = parseIntAttribute;
                            if (name.equals(TAG_STRING_ARRAY_XMLUTILS)) {
                                c2 = 3;
                                break;
                            }
                            break;
                        case -991716523:
                            i5 = parseIntAttribute;
                            if (name.equals(TAG_PERSON)) {
                                c = 4;
                                c2 = c;
                                break;
                            }
                            break;
                        case 107868:
                            i5 = parseIntAttribute;
                            if (name.equals(TAG_MAP_XMLUTILS)) {
                                c = 5;
                                c2 = c;
                                break;
                            }
                            break;
                        case 1296516636:
                            if (name.equals("categories")) {
                                i5 = parseIntAttribute;
                                c2 = 6;
                                break;
                            }
                        default:
                            i5 = parseIntAttribute;
                            break;
                    }
                    switch (c2) {
                        case 0:
                            persistableBundle = PersistableBundle.restoreFromXml(typedXmlPullParser);
                            parseStringAttribute5 = str4;
                            parseIntAttribute = i5;
                            depth = i4;
                            parseStringAttribute3 = str3;
                        case 1:
                            arrayList.add(parseIntent(typedXmlPullParser));
                            parseStringAttribute5 = str4;
                            parseIntAttribute = i5;
                            depth = i4;
                            parseStringAttribute3 = str3;
                        case 2:
                            persistableBundle2 = PersistableBundle.restoreFromXml(typedXmlPullParser);
                            parseStringAttribute5 = str4;
                            parseIntAttribute = i5;
                            depth = i4;
                            parseStringAttribute3 = str3;
                        case 3:
                            if ("categories".equals(ShortcutService.parseStringAttribute(typedXmlPullParser, "name"))) {
                                String[] readThisStringArrayXml = XmlUtils.readThisStringArrayXml(XmlUtils.makeTyped(typedXmlPullParser), TAG_STRING_ARRAY_XMLUTILS, (String[]) null);
                                ArraySet arraySet2 = new ArraySet(readThisStringArrayXml.length);
                                for (String str5 : readThisStringArrayXml) {
                                    arraySet2.add(str5);
                                }
                                arraySet = arraySet2;
                                parseStringAttribute5 = str4;
                                parseIntAttribute = i5;
                                depth = i4;
                                parseStringAttribute3 = str3;
                            }
                            break;
                        case 4:
                            arrayList2.add(parsePerson(typedXmlPullParser));
                            break;
                        case 5:
                            if (NAME_CAPABILITY.equals(ShortcutService.parseStringAttribute(typedXmlPullParser, "name"))) {
                                map = (Map) XmlUtils.readValueXml(typedXmlPullParser, new String[1]);
                                parseStringAttribute5 = str4;
                                parseIntAttribute = i5;
                                depth = i4;
                                parseStringAttribute3 = str3;
                            }
                            break;
                        case 6:
                            break;
                        default:
                            throw ShortcutService.throwForInvalidTag(depth2, name);
                    }
                }
                parseStringAttribute5 = str4;
                parseIntAttribute = i5;
                depth = i4;
                parseStringAttribute3 = str3;
            } else {
                i2 = parseIntAttribute;
                str2 = parseStringAttribute3;
                i3 = 1;
            }
        }
    }

    private static Intent parseIntent(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException {
        Intent parseIntentAttribute = ShortcutService.parseIntentAttribute(typedXmlPullParser, ATTR_INTENT_NO_EXTRA);
        int depth = typedXmlPullParser.getDepth();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1 || (next == 3 && typedXmlPullParser.getDepth() <= depth)) {
                break;
            }
            if (next == 2) {
                int depth2 = typedXmlPullParser.getDepth();
                String name = typedXmlPullParser.getName();
                name.hashCode();
                if (name.equals(TAG_EXTRAS)) {
                    ShortcutInfo.setIntentExtras(parseIntentAttribute, PersistableBundle.restoreFromXml(typedXmlPullParser));
                } else {
                    throw ShortcutService.throwForInvalidTag(depth2, name);
                }
            }
        }
        return parseIntentAttribute;
    }

    private static Person parsePerson(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException {
        String parseStringAttribute = ShortcutService.parseStringAttribute(typedXmlPullParser, "name");
        String parseStringAttribute2 = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_PERSON_URI);
        String parseStringAttribute3 = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_PERSON_KEY);
        boolean parseBooleanAttribute = ShortcutService.parseBooleanAttribute(typedXmlPullParser, ATTR_PERSON_IS_BOT);
        boolean parseBooleanAttribute2 = ShortcutService.parseBooleanAttribute(typedXmlPullParser, ATTR_PERSON_IS_IMPORTANT);
        Person.Builder builder = new Person.Builder();
        builder.setName(parseStringAttribute).setUri(parseStringAttribute2).setKey(parseStringAttribute3).setBot(parseBooleanAttribute).setImportant(parseBooleanAttribute2);
        return builder.build();
    }

    @VisibleForTesting
    List<ShortcutInfo> getAllShortcutsForTest() {
        ArrayList arrayList = new ArrayList(1);
        forEachShortcut(new ShortcutPackage$$ExternalSyntheticLambda46(arrayList));
        return arrayList;
    }

    @VisibleForTesting
    List<ShareTargetInfo> getAllShareTargetsForTest() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mShareTargets);
        }
        return arrayList;
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    public void verifyStates() {
        super.verifyStates();
        final boolean[] zArr = new boolean[1];
        final ShortcutService shortcutService = this.mShortcutUser.mService;
        ArrayMap<ComponentName, ArrayList<ShortcutInfo>> sortShortcutsToActivities = sortShortcutsToActivities();
        for (int size = sortShortcutsToActivities.size() - 1; size >= 0; size--) {
            ArrayList<ShortcutInfo> valueAt = sortShortcutsToActivities.valueAt(size);
            if (valueAt.size() > this.mShortcutUser.mService.getMaxActivityShortcuts()) {
                zArr[0] = true;
                Log.e(TAG_VERIFY, "Package " + getPackageName() + ": activity " + sortShortcutsToActivities.keyAt(size) + " has " + sortShortcutsToActivities.valueAt(size).size() + " shortcuts.");
            }
            Collections.sort(valueAt, new Comparator() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda40
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$verifyStates$33;
                    lambda$verifyStates$33 = ShortcutPackage.lambda$verifyStates$33((ShortcutInfo) obj, (ShortcutInfo) obj2);
                    return lambda$verifyStates$33;
                }
            });
            ArrayList arrayList = new ArrayList(valueAt);
            arrayList.removeIf(new Predicate() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda41
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$verifyStates$34;
                    lambda$verifyStates$34 = ShortcutPackage.lambda$verifyStates$34((ShortcutInfo) obj);
                    return lambda$verifyStates$34;
                }
            });
            ArrayList arrayList2 = new ArrayList(valueAt);
            arrayList2.removeIf(new Predicate() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda42
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$verifyStates$35;
                    lambda$verifyStates$35 = ShortcutPackage.lambda$verifyStates$35((ShortcutInfo) obj);
                    return lambda$verifyStates$35;
                }
            });
            verifyRanksSequential(arrayList);
            verifyRanksSequential(arrayList2);
        }
        forEachShortcut(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda43
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.this.lambda$verifyStates$36(zArr, shortcutService, (ShortcutInfo) obj);
            }
        });
        if (zArr[0]) {
            throw new IllegalStateException("See logcat for errors");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$verifyStates$33(ShortcutInfo shortcutInfo, ShortcutInfo shortcutInfo2) {
        return Integer.compare(shortcutInfo.getRank(), shortcutInfo2.getRank());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$verifyStates$34(ShortcutInfo shortcutInfo) {
        return !shortcutInfo.isDynamic();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$verifyStates$35(ShortcutInfo shortcutInfo) {
        return !shortcutInfo.isManifestShortcut();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verifyStates$36(boolean[] zArr, ShortcutService shortcutService, ShortcutInfo shortcutInfo) {
        if (!shortcutInfo.isDeclaredInManifest() && !shortcutInfo.isDynamic() && !shortcutInfo.isPinned() && !shortcutInfo.isCached()) {
            zArr[0] = true;
            Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + shortcutInfo.getId() + " is not manifest, dynamic or pinned.");
        }
        if (shortcutInfo.isDeclaredInManifest() && shortcutInfo.isDynamic()) {
            zArr[0] = true;
            Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + shortcutInfo.getId() + " is both dynamic and manifest at the same time.");
        }
        if (shortcutInfo.getActivity() == null && !shortcutInfo.isFloating()) {
            zArr[0] = true;
            Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + shortcutInfo.getId() + " has null activity, but not floating.");
        }
        if ((shortcutInfo.isDynamic() || shortcutInfo.isManifestShortcut()) && !shortcutInfo.isEnabled()) {
            zArr[0] = true;
            Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + shortcutInfo.getId() + " is not floating, but is disabled.");
        }
        if (shortcutInfo.isFloating() && shortcutInfo.getRank() != 0) {
            zArr[0] = true;
            Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + shortcutInfo.getId() + " is floating, but has rank=" + shortcutInfo.getRank());
        }
        if (shortcutInfo.getIcon() != null) {
            zArr[0] = true;
            Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + shortcutInfo.getId() + " still has an icon");
        }
        if (shortcutInfo.hasAdaptiveBitmap() && !shortcutInfo.hasIconFile() && !shortcutInfo.hasIconUri()) {
            zArr[0] = true;
            Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + shortcutInfo.getId() + " has adaptive bitmap but was not saved to a file nor has icon uri.");
        }
        if (shortcutInfo.hasIconFile() && shortcutInfo.hasIconResource()) {
            zArr[0] = true;
            Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + shortcutInfo.getId() + " has both resource and bitmap icons");
        }
        if (shortcutInfo.hasIconFile() && shortcutInfo.hasIconUri()) {
            zArr[0] = true;
            Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + shortcutInfo.getId() + " has both url and bitmap icons");
        }
        if (shortcutInfo.hasIconUri() && shortcutInfo.hasIconResource()) {
            zArr[0] = true;
            Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + shortcutInfo.getId() + " has both url and resource icons");
        }
        if (shortcutInfo.isEnabled() != (shortcutInfo.getDisabledReason() == 0)) {
            zArr[0] = true;
            Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + shortcutInfo.getId() + " isEnabled() and getDisabledReason() disagree: " + shortcutInfo.isEnabled() + " vs " + shortcutInfo.getDisabledReason());
        }
        if (shortcutInfo.getDisabledReason() == 100 && getPackageInfo().getBackupSourceVersionCode() == -1) {
            zArr[0] = true;
            Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + shortcutInfo.getId() + " RESTORED_VERSION_LOWER with no backup source version code.");
        }
        if (shortcutService.isDummyMainActivity(shortcutInfo.getActivity())) {
            zArr[0] = true;
            Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + shortcutInfo.getId() + " has a dummy target activity");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void mutateShortcut(String str, ShortcutInfo shortcutInfo, Consumer<ShortcutInfo> consumer) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(consumer);
        synchronized (this.mLock) {
            if (shortcutInfo != null) {
                consumer.accept(shortcutInfo);
            }
            ShortcutInfo findShortcutById = findShortcutById(str);
            if (findShortcutById == null) {
                return;
            }
            consumer.accept(findShortcutById);
            saveShortcut(findShortcutById);
        }
    }

    private void saveShortcut(ShortcutInfo... shortcutInfoArr) {
        Objects.requireNonNull(shortcutInfoArr);
        saveShortcut(Arrays.asList(shortcutInfoArr));
    }

    private void saveShortcut(Collection<ShortcutInfo> collection) {
        Objects.requireNonNull(collection);
        synchronized (this.mLock) {
            for (ShortcutInfo shortcutInfo : collection) {
                this.mShortcuts.put(shortcutInfo.getId(), shortcutInfo);
            }
        }
    }

    List<ShortcutInfo> findAll(Collection<String> collection) {
        List<ShortcutInfo> list;
        synchronized (this.mLock) {
            Stream<String> stream = collection.stream();
            final ArrayMap<String, ShortcutInfo> arrayMap = this.mShortcuts;
            Objects.requireNonNull(arrayMap);
            list = (List) stream.map(new Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda25
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return (ShortcutInfo) arrayMap.get((String) obj);
                }
            }).filter(new Predicate() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda26
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return Objects.nonNull((ShortcutInfo) obj);
                }
            }).collect(Collectors.toList());
        }
        return list;
    }

    private void forEachShortcut(final Consumer<ShortcutInfo> consumer) {
        forEachShortcutStopWhen(new Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda58
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Boolean lambda$forEachShortcut$37;
                lambda$forEachShortcut$37 = ShortcutPackage.lambda$forEachShortcut$37(consumer, (ShortcutInfo) obj);
                return lambda$forEachShortcut$37;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean lambda$forEachShortcut$37(Consumer consumer, ShortcutInfo shortcutInfo) {
        consumer.accept(shortcutInfo);
        return Boolean.FALSE;
    }

    private void forEachShortcutMutate(Consumer<ShortcutInfo> consumer) {
        for (int size = this.mShortcuts.size() - 1; size >= 0; size--) {
            consumer.accept(this.mShortcuts.valueAt(size));
        }
    }

    private void forEachShortcutStopWhen(Function<ShortcutInfo, Boolean> function) {
        synchronized (this.mLock) {
            for (int size = this.mShortcuts.size() - 1; size >= 0; size--) {
                if (function.apply(this.mShortcuts.valueAt(size)).booleanValue()) {
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AndroidFuture<AppSearchSession> setupSchema(final AppSearchSession appSearchSession) {
        SetSchemaRequest.Builder addRequiredPermissionsForSchemaTypeVisibility = new SetSchemaRequest.Builder().addSchemas(AppSearchShortcutPerson.SCHEMA, AppSearchShortcutInfo.SCHEMA).setForceOverride(true).addRequiredPermissionsForSchemaTypeVisibility("Shortcut", Collections.singleton(5)).addRequiredPermissionsForSchemaTypeVisibility("Shortcut", Collections.singleton(6)).addRequiredPermissionsForSchemaTypeVisibility("ShortcutPerson", Collections.singleton(5)).addRequiredPermissionsForSchemaTypeVisibility("ShortcutPerson", Collections.singleton(6));
        final AndroidFuture<AppSearchSession> androidFuture = new AndroidFuture<>();
        appSearchSession.setSchema(addRequiredPermissionsForSchemaTypeVisibility.build(), this.mExecutor, this.mShortcutUser.mExecutor, new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.lambda$setupSchema$38(androidFuture, appSearchSession, (AppSearchResult) obj);
            }
        });
        return androidFuture;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setupSchema$38(AndroidFuture androidFuture, AppSearchSession appSearchSession, AppSearchResult appSearchResult) {
        if (!appSearchResult.isSuccess()) {
            androidFuture.completeExceptionally(new IllegalArgumentException(appSearchResult.getErrorMessage()));
        } else {
            androidFuture.complete(appSearchSession);
        }
    }

    private SearchSpec getSearchSpec() {
        return new SearchSpec.Builder().addFilterSchemas("Shortcut").addFilterNamespaces(getPackageName()).setTermMatch(1).setResultCountPerPage(this.mShortcutUser.mService.getMaxActivityShortcuts()).build();
    }

    private boolean verifyRanksSequential(List<ShortcutInfo> list) {
        boolean z = false;
        for (int i = 0; i < list.size(); i++) {
            ShortcutInfo shortcutInfo = list.get(i);
            if (shortcutInfo.getRank() != i) {
                Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + shortcutInfo.getId() + " rank=" + shortcutInfo.getRank() + " but expected to be " + i);
                z = true;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeAllShortcutsAsync() {
        if (isAppSearchEnabled()) {
            runAsSystem(new Runnable() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda37
                @Override // java.lang.Runnable
                public final void run() {
                    ShortcutPackage.this.lambda$removeAllShortcutsAsync$41();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeAllShortcutsAsync$41() {
        fromAppSearch().thenAccept(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda54
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.this.lambda$removeAllShortcutsAsync$40((AppSearchSession) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeAllShortcutsAsync$40(AppSearchSession appSearchSession) {
        appSearchSession.remove("", getSearchSpec(), this.mShortcutUser.mExecutor, new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda45
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.lambda$removeAllShortcutsAsync$39((AppSearchResult) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$removeAllShortcutsAsync$39(AppSearchResult appSearchResult) {
        if (appSearchResult.isSuccess()) {
            return;
        }
        Slog.e(TAG, "Failed to remove shortcuts from AppSearch. " + appSearchResult.getErrorMessage());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getShortcutByIdsAsync(final Set<String> set, final Consumer<List<ShortcutInfo>> consumer) {
        if (!isAppSearchEnabled()) {
            consumer.accept(Collections.emptyList());
        } else {
            runAsSystem(new Runnable() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    ShortcutPackage.this.lambda$getShortcutByIdsAsync$43(set, consumer);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getShortcutByIdsAsync$43(final Set set, final Consumer consumer) {
        fromAppSearch().thenAccept(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.this.lambda$getShortcutByIdsAsync$42(set, consumer, (AppSearchSession) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getShortcutByIdsAsync$42(Set set, Consumer consumer, AppSearchSession appSearchSession) {
        appSearchSession.getByDocumentId(new GetByDocumentIdRequest.Builder(getPackageName()).addIds(set).build(), this.mShortcutUser.mExecutor, new AnonymousClass1(consumer));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.pm.ShortcutPackage$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 implements BatchResultCallback<String, GenericDocument> {
        final /* synthetic */ Consumer val$cb;

        AnonymousClass1(Consumer consumer) {
            this.val$cb = consumer;
        }

        @Override // android.app.appsearch.BatchResultCallback
        public void onResult(AppSearchBatchResult<String, GenericDocument> appSearchBatchResult) {
            this.val$cb.accept((List) appSearchBatchResult.getSuccesses().values().stream().map(new Function() { // from class: com.android.server.pm.ShortcutPackage$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    ShortcutInfo lambda$onResult$0;
                    lambda$onResult$0 = ShortcutPackage.AnonymousClass1.this.lambda$onResult$0((GenericDocument) obj);
                    return lambda$onResult$0;
                }
            }).collect(Collectors.toList()));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ ShortcutInfo lambda$onResult$0(GenericDocument genericDocument) {
            return ShortcutInfo.createFromGenericDocument(ShortcutPackage.this.mShortcutUser.getUserId(), genericDocument);
        }

        @Override // android.app.appsearch.BatchResultCallback
        public void onSystemError(Throwable th) {
            Slog.d(ShortcutPackage.TAG, "Error retrieving shortcuts", th);
        }
    }

    private void removeShortcutAsync(String... strArr) {
        Objects.requireNonNull(strArr);
        removeShortcutAsync(Arrays.asList(strArr));
    }

    private void removeShortcutAsync(final Collection<String> collection) {
        if (isAppSearchEnabled()) {
            runAsSystem(new Runnable() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda51
                @Override // java.lang.Runnable
                public final void run() {
                    ShortcutPackage.this.lambda$removeShortcutAsync$45(collection);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeShortcutAsync$45(final Collection collection) {
        fromAppSearch().thenAccept(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.this.lambda$removeShortcutAsync$44(collection, (AppSearchSession) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeShortcutAsync$44(Collection collection, AppSearchSession appSearchSession) {
        appSearchSession.remove(new RemoveByDocumentIdRequest.Builder(getPackageName()).addIds((Collection<String>) collection).build(), this.mShortcutUser.mExecutor, new BatchResultCallback<String, Void>() { // from class: com.android.server.pm.ShortcutPackage.2
            @Override // android.app.appsearch.BatchResultCallback
            public void onResult(AppSearchBatchResult<String, Void> appSearchBatchResult) {
                if (appSearchBatchResult.isSuccess()) {
                    return;
                }
                Map<String, AppSearchResult<Void>> failures = appSearchBatchResult.getFailures();
                for (String str : failures.keySet()) {
                    Slog.e(ShortcutPackage.TAG, "Failed deleting " + str + ", error message:" + failures.get(str).getErrorMessage());
                }
            }

            @Override // android.app.appsearch.BatchResultCallback
            public void onSystemError(Throwable th) {
                Slog.e(ShortcutPackage.TAG, "Error removing shortcuts", th);
            }
        });
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    @GuardedBy({"mLock"})
    void scheduleSaveToAppSearchLocked() {
        ArrayMap arrayMap = new ArrayMap(this.mShortcuts);
        if (!this.mTransientShortcuts.isEmpty()) {
            arrayMap.putAll((Map) this.mTransientShortcuts);
            this.mTransientShortcuts.clear();
        }
        saveShortcutsAsync((Collection) arrayMap.values().stream().filter(new Predicate() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda34
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((ShortcutInfo) obj).usesQuota();
            }
        }).collect(Collectors.toList()));
    }

    private void saveShortcutsAsync(final Collection<ShortcutInfo> collection) {
        Objects.requireNonNull(collection);
        if (!isAppSearchEnabled() || collection.isEmpty()) {
            return;
        }
        runAsSystem(new Runnable() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                ShortcutPackage.this.lambda$saveShortcutsAsync$47(collection);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveShortcutsAsync$47(final Collection collection) {
        fromAppSearch().thenAccept(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda28
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.this.lambda$saveShortcutsAsync$46(collection, (AppSearchSession) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveShortcutsAsync$46(Collection collection, AppSearchSession appSearchSession) {
        if (collection.isEmpty()) {
            return;
        }
        appSearchSession.put(new PutDocumentsRequest.Builder().addGenericDocuments(AppSearchShortcutInfo.toGenericDocuments(collection)).build(), this.mShortcutUser.mExecutor, new BatchResultCallback<String, Void>() { // from class: com.android.server.pm.ShortcutPackage.3
            @Override // android.app.appsearch.BatchResultCallback
            public void onResult(AppSearchBatchResult<String, Void> appSearchBatchResult) {
                if (appSearchBatchResult.isSuccess()) {
                    return;
                }
                Iterator<AppSearchResult<Void>> it = appSearchBatchResult.getFailures().values().iterator();
                while (it.hasNext()) {
                    Slog.e(ShortcutPackage.TAG, it.next().getErrorMessage());
                }
            }

            @Override // android.app.appsearch.BatchResultCallback
            public void onSystemError(Throwable th) {
                Slog.d(ShortcutPackage.TAG, "Error persisting shortcuts", th);
            }
        });
    }

    @VisibleForTesting
    void getTopShortcutsFromPersistence(final AndroidFuture<List<ShortcutInfo>> androidFuture) {
        if (!isAppSearchEnabled()) {
            androidFuture.complete((Object) null);
        }
        runAsSystem(new Runnable() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                ShortcutPackage.this.lambda$getTopShortcutsFromPersistence$51(androidFuture);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getTopShortcutsFromPersistence$51(final AndroidFuture androidFuture) {
        fromAppSearch().thenAccept(new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.this.lambda$getTopShortcutsFromPersistence$50(androidFuture, (AppSearchSession) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getTopShortcutsFromPersistence$50(final AndroidFuture androidFuture, AppSearchSession appSearchSession) {
        appSearchSession.search("", getSearchSpec()).getNextPage(this.mShortcutUser.mExecutor, new Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutPackage.this.lambda$getTopShortcutsFromPersistence$49(androidFuture, (AppSearchResult) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getTopShortcutsFromPersistence$49(AndroidFuture androidFuture, AppSearchResult appSearchResult) {
        if (!appSearchResult.isSuccess()) {
            androidFuture.completeExceptionally(new IllegalStateException(appSearchResult.getErrorMessage()));
        } else {
            androidFuture.complete((List) ((List) appSearchResult.getResultValue()).stream().map(new Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda23
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ((SearchResult) obj).getGenericDocument();
                }
            }).map(new Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda24
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    ShortcutInfo lambda$getTopShortcutsFromPersistence$48;
                    lambda$getTopShortcutsFromPersistence$48 = ShortcutPackage.this.lambda$getTopShortcutsFromPersistence$48((GenericDocument) obj);
                    return lambda$getTopShortcutsFromPersistence$48;
                }
            }).collect(Collectors.toList()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ ShortcutInfo lambda$getTopShortcutsFromPersistence$48(GenericDocument genericDocument) {
        return ShortcutInfo.createFromGenericDocument(this.mShortcutUser.getUserId(), genericDocument);
    }

    private AndroidFuture<AppSearchSession> fromAppSearch() {
        StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
        AppSearchManager.SearchContext build = new AppSearchManager.SearchContext.Builder(getPackageName()).build();
        AndroidFuture<AppSearchSession> androidFuture = null;
        try {
            try {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
                androidFuture = this.mShortcutUser.getAppSearch(build);
                synchronized (this.mLock) {
                    if (!this.mIsAppSearchSchemaUpToDate) {
                        androidFuture = androidFuture.thenCompose(new Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda47
                            @Override // java.util.function.Function
                            public final Object apply(Object obj) {
                                AndroidFuture androidFuture2;
                                androidFuture2 = ShortcutPackage.this.setupSchema((AppSearchSession) obj);
                                return androidFuture2;
                            }
                        });
                    }
                    this.mIsAppSearchSchemaUpToDate = true;
                }
            } catch (Exception e) {
                Slog.e(TAG, "Failed to create app search session. pkg=" + getPackageName() + " user=" + this.mShortcutUser.getUserId(), e);
                Objects.requireNonNull(androidFuture);
                AndroidFuture<AppSearchSession> androidFuture2 = androidFuture;
                androidFuture.completeExceptionally(e);
            }
            Objects.requireNonNull(androidFuture);
            return androidFuture;
        } finally {
            StrictMode.setThreadPolicy(threadPolicy);
        }
    }

    private void runAsSystem(Runnable runnable) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            runnable.run();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    protected File getShortcutPackageItemFile() {
        ShortcutUser shortcutUser = this.mShortcutUser;
        return new File(new File(shortcutUser.mService.injectUserDataPath(shortcutUser.getUserId()), "packages"), getPackageName() + ".xml");
    }
}
