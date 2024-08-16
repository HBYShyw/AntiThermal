package com.android.server.compat;

import android.app.compat.ChangeIdStateCache;
import android.app.compat.PackageOverride;
import android.compat.Compatibility;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.os.Environment;
import android.text.TextUtils;
import android.util.LongArray;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.compat.AndroidBuildClassifier;
import com.android.internal.compat.CompatibilityChangeConfig;
import com.android.internal.compat.CompatibilityChangeInfo;
import com.android.internal.compat.CompatibilityOverrideConfig;
import com.android.internal.compat.CompatibilityOverridesByPackageConfig;
import com.android.internal.compat.CompatibilityOverridesToRemoveByPackageConfig;
import com.android.internal.compat.CompatibilityOverridesToRemoveConfig;
import com.android.internal.compat.IOverrideValidator;
import com.android.internal.compat.OverrideAllowedState;
import com.android.server.compat.CompatChange;
import com.android.server.compat.config.Change;
import com.android.server.compat.config.XmlParser;
import com.android.server.compat.overrides.ChangeOverrides;
import com.android.server.compat.overrides.Overrides;
import com.android.server.compat.overrides.XmlWriter;
import com.android.server.pm.ApexManager;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class CompatConfig {
    private static final String APP_COMPAT_DATA_DIR = "/data/misc/appcompat";
    private static final String OVERRIDES_FILE = "compat_framework_overrides.xml";
    private static final String STATIC_OVERRIDES_PRODUCT_DIR = "/product/etc/appcompat";
    private static final String TAG = "CompatConfig";
    private final AndroidBuildClassifier mAndroidBuildClassifier;

    @GuardedBy({"mOverridesFileLock"})
    private File mBackupOverridesFile;
    private Context mContext;
    private final OverrideValidatorImpl mOverrideValidator;

    @GuardedBy({"mOverridesFileLock"})
    private File mOverridesFile;
    private final ConcurrentHashMap<Long, CompatChange> mChanges = new ConcurrentHashMap<>();
    private final Object mOverridesFileLock = new Object();

    @VisibleForTesting
    CompatConfig(AndroidBuildClassifier androidBuildClassifier, Context context) {
        this.mOverrideValidator = new OverrideValidatorImpl(androidBuildClassifier, context, this);
        this.mAndroidBuildClassifier = androidBuildClassifier;
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CompatConfig create(AndroidBuildClassifier androidBuildClassifier, Context context) {
        CompatConfig compatConfig = new CompatConfig(androidBuildClassifier, context);
        compatConfig.initConfigFromLib(Environment.buildPath(Environment.getRootDirectory(), new String[]{"etc", "compatconfig"}));
        compatConfig.initConfigFromLib(Environment.buildPath(Environment.getRootDirectory(), new String[]{"system_ext", "etc", "compatconfig"}));
        Iterator it = ApexManager.getInstance().getActiveApexInfos().iterator();
        while (it.hasNext()) {
            compatConfig.initConfigFromLib(Environment.buildPath(((ApexManager.ActiveApexInfo) it.next()).apexDirectory, new String[]{"etc", "compatconfig"}));
        }
        compatConfig.initOverrides();
        compatConfig.invalidateCache();
        return compatConfig;
    }

    @VisibleForTesting
    void addChange(CompatChange compatChange) {
        this.mChanges.put(Long.valueOf(compatChange.getId()), compatChange);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long[] getDisabledChanges(ApplicationInfo applicationInfo) {
        LongArray longArray = new LongArray();
        for (CompatChange compatChange : this.mChanges.values()) {
            if (!compatChange.isEnabled(applicationInfo, this.mAndroidBuildClassifier)) {
                longArray.add(compatChange.getId());
            }
        }
        long[] array = longArray.toArray();
        Arrays.sort(array);
        return array;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long lookupChangeId(String str) {
        for (CompatChange compatChange : this.mChanges.values()) {
            if (TextUtils.equals(compatChange.getName(), str)) {
                return compatChange.getId();
            }
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isChangeEnabled(long j, ApplicationInfo applicationInfo) {
        CompatChange compatChange = this.mChanges.get(Long.valueOf(j));
        if (compatChange == null) {
            return true;
        }
        return compatChange.isEnabled(applicationInfo, this.mAndroidBuildClassifier);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean willChangeBeEnabled(long j, String str) {
        CompatChange compatChange = this.mChanges.get(Long.valueOf(j));
        if (compatChange == null) {
            return true;
        }
        return compatChange.willBeEnabled(str);
    }

    synchronized boolean addOverride(long j, String str, boolean z) {
        boolean addOverrideUnsafe;
        addOverrideUnsafe = addOverrideUnsafe(j, str, new PackageOverride.Builder().setEnabled(z).build());
        saveOverrides();
        invalidateCache();
        return addOverrideUnsafe;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void addAllPackageOverrides(CompatibilityOverridesByPackageConfig compatibilityOverridesByPackageConfig, boolean z) {
        for (String str : compatibilityOverridesByPackageConfig.packageNameToOverrides.keySet()) {
            addPackageOverridesWithoutSaving((CompatibilityOverrideConfig) compatibilityOverridesByPackageConfig.packageNameToOverrides.get(str), str, z);
        }
        saveOverrides();
        invalidateCache();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void addPackageOverrides(CompatibilityOverrideConfig compatibilityOverrideConfig, String str, boolean z) {
        addPackageOverridesWithoutSaving(compatibilityOverrideConfig, str, z);
        saveOverrides();
        invalidateCache();
    }

    private void addPackageOverridesWithoutSaving(CompatibilityOverrideConfig compatibilityOverrideConfig, String str, boolean z) {
        for (Long l : compatibilityOverrideConfig.overrides.keySet()) {
            if (z && !isKnownChangeId(l.longValue())) {
                Slog.w(TAG, "Trying to add overrides for unknown Change ID " + l + ". Skipping Change ID.");
            } else {
                addOverrideUnsafe(l.longValue(), str, (PackageOverride) compatibilityOverrideConfig.overrides.get(l));
            }
        }
    }

    private boolean addOverrideUnsafe(final long j, String str, PackageOverride packageOverride) {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        OverrideAllowedState overrideAllowedState = this.mOverrideValidator.getOverrideAllowedState(j, str);
        overrideAllowedState.enforce(j, str);
        this.mChanges.computeIfAbsent(Long.valueOf(j), new Function() { // from class: com.android.server.compat.CompatConfig$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                CompatChange lambda$addOverrideUnsafe$0;
                lambda$addOverrideUnsafe$0 = CompatConfig.lambda$addOverrideUnsafe$0(atomicBoolean, j, (Long) obj);
                return lambda$addOverrideUnsafe$0;
            }
        }).addPackageOverride(str, packageOverride, overrideAllowedState, getVersionCodeOrNull(str));
        invalidateCache();
        return atomicBoolean.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ CompatChange lambda$addOverrideUnsafe$0(AtomicBoolean atomicBoolean, long j, Long l) {
        atomicBoolean.set(false);
        return new CompatChange(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isKnownChangeId(long j) {
        return this.mChanges.containsKey(Long.valueOf(j));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int maxTargetSdkForChangeIdOptIn(long j) {
        CompatChange compatChange = this.mChanges.get(Long.valueOf(j));
        if (compatChange == null || compatChange.getEnableSinceTargetSdk() == -1) {
            return -1;
        }
        return compatChange.getEnableSinceTargetSdk() - 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLoggingOnly(long j) {
        CompatChange compatChange = this.mChanges.get(Long.valueOf(j));
        return compatChange != null && compatChange.getLoggingOnly();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDisabled(long j) {
        CompatChange compatChange = this.mChanges.get(Long.valueOf(j));
        return compatChange != null && compatChange.getDisabled();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isOverridable(long j) {
        CompatChange compatChange = this.mChanges.get(Long.valueOf(j));
        return compatChange != null && compatChange.getOverridable();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean removeOverride(long j, String str) {
        boolean removeOverrideUnsafe;
        removeOverrideUnsafe = removeOverrideUnsafe(j, str);
        if (removeOverrideUnsafe) {
            saveOverrides();
            invalidateCache();
        }
        return removeOverrideUnsafe;
    }

    private boolean removeOverrideUnsafe(long j, String str) {
        Long versionCodeOrNull = getVersionCodeOrNull(str);
        CompatChange compatChange = this.mChanges.get(Long.valueOf(j));
        if (compatChange != null) {
            return removeOverrideUnsafe(compatChange, str, versionCodeOrNull);
        }
        return false;
    }

    private boolean removeOverrideUnsafe(CompatChange compatChange, String str, Long l) {
        return compatChange.removePackageOverride(str, this.mOverrideValidator.getOverrideAllowedState(compatChange.getId(), str), l);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void removeAllPackageOverrides(CompatibilityOverridesToRemoveByPackageConfig compatibilityOverridesToRemoveByPackageConfig) {
        boolean z = false;
        for (String str : compatibilityOverridesToRemoveByPackageConfig.packageNameToOverridesToRemove.keySet()) {
            z |= removePackageOverridesWithoutSaving((CompatibilityOverridesToRemoveConfig) compatibilityOverridesToRemoveByPackageConfig.packageNameToOverridesToRemove.get(str), str);
        }
        if (z) {
            saveOverrides();
            invalidateCache();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void removePackageOverrides(String str) {
        Long versionCodeOrNull = getVersionCodeOrNull(str);
        Iterator<CompatChange> it = this.mChanges.values().iterator();
        boolean z = false;
        while (it.hasNext()) {
            z |= removeOverrideUnsafe(it.next(), str, versionCodeOrNull);
        }
        if (z) {
            saveOverrides();
            invalidateCache();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void removePackageOverrides(CompatibilityOverridesToRemoveConfig compatibilityOverridesToRemoveConfig, String str) {
        if (removePackageOverridesWithoutSaving(compatibilityOverridesToRemoveConfig, str)) {
            saveOverrides();
            invalidateCache();
        }
    }

    private boolean removePackageOverridesWithoutSaving(CompatibilityOverridesToRemoveConfig compatibilityOverridesToRemoveConfig, String str) {
        boolean z = false;
        for (Long l : compatibilityOverridesToRemoveConfig.changeIds) {
            if (isKnownChangeId(l.longValue())) {
                z |= removeOverrideUnsafe(l.longValue(), str);
            } else {
                Slog.w(TAG, "Trying to remove overrides for unknown Change ID " + l + ". Skipping Change ID.");
            }
        }
        return z;
    }

    private long[] getAllowedChangesSinceTargetSdkForPackage(String str, int i) {
        LongArray longArray = new LongArray();
        for (CompatChange compatChange : this.mChanges.values()) {
            if (compatChange.getEnableSinceTargetSdk() == i && this.mOverrideValidator.getOverrideAllowedState(compatChange.getId(), str).state == 0) {
                longArray.add(compatChange.getId());
            }
        }
        return longArray.toArray();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int enableTargetSdkChangesForPackage(String str, int i) {
        long[] allowedChangesSinceTargetSdkForPackage = getAllowedChangesSinceTargetSdkForPackage(str, i);
        boolean z = false;
        for (long j : allowedChangesSinceTargetSdkForPackage) {
            z |= addOverrideUnsafe(j, str, new PackageOverride.Builder().setEnabled(true).build());
        }
        if (z) {
            saveOverrides();
            invalidateCache();
        }
        return allowedChangesSinceTargetSdkForPackage.length;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int disableTargetSdkChangesForPackage(String str, int i) {
        long[] allowedChangesSinceTargetSdkForPackage = getAllowedChangesSinceTargetSdkForPackage(str, i);
        boolean z = false;
        for (long j : allowedChangesSinceTargetSdkForPackage) {
            z |= addOverrideUnsafe(j, str, new PackageOverride.Builder().setEnabled(false).build());
        }
        if (z) {
            saveOverrides();
            invalidateCache();
        }
        return allowedChangesSinceTargetSdkForPackage.length;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean registerListener(final long j, CompatChange.ChangeListener changeListener) {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        this.mChanges.computeIfAbsent(Long.valueOf(j), new Function() { // from class: com.android.server.compat.CompatConfig$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                CompatChange lambda$registerListener$1;
                lambda$registerListener$1 = CompatConfig.this.lambda$registerListener$1(atomicBoolean, j, (Long) obj);
                return lambda$registerListener$1;
            }
        }).registerListener(changeListener);
        return atomicBoolean.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ CompatChange lambda$registerListener$1(AtomicBoolean atomicBoolean, long j, Long l) {
        atomicBoolean.set(false);
        invalidateCache();
        return new CompatChange(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean defaultChangeIdValue(long j) {
        CompatChange compatChange = this.mChanges.get(Long.valueOf(j));
        if (compatChange == null) {
            return true;
        }
        return compatChange.defaultValue();
    }

    @VisibleForTesting
    void forceNonDebuggableFinalForTest(boolean z) {
        this.mOverrideValidator.forceNonDebuggableFinalForTest(z);
    }

    @VisibleForTesting
    void clearChanges() {
        this.mChanges.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpConfig(PrintWriter printWriter) {
        if (this.mChanges.size() == 0) {
            printWriter.println("No compat overrides.");
            return;
        }
        Iterator<CompatChange> it = this.mChanges.values().iterator();
        while (it.hasNext()) {
            printWriter.println(it.next().toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompatibilityChangeConfig getAppConfig(ApplicationInfo applicationInfo) {
        HashSet hashSet = new HashSet();
        HashSet hashSet2 = new HashSet();
        for (CompatChange compatChange : this.mChanges.values()) {
            if (compatChange.isEnabled(applicationInfo, this.mAndroidBuildClassifier)) {
                hashSet.add(Long.valueOf(compatChange.getId()));
            } else {
                hashSet2.add(Long.valueOf(compatChange.getId()));
            }
        }
        return new CompatibilityChangeConfig(new Compatibility.ChangeConfig(hashSet, hashSet2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompatibilityChangeInfo[] dumpChanges() {
        CompatibilityChangeInfo[] compatibilityChangeInfoArr = new CompatibilityChangeInfo[this.mChanges.size()];
        Iterator<CompatChange> it = this.mChanges.values().iterator();
        int i = 0;
        while (it.hasNext()) {
            compatibilityChangeInfoArr[i] = new CompatibilityChangeInfo(it.next());
            i++;
        }
        return compatibilityChangeInfoArr;
    }

    void initConfigFromLib(File file) {
        if (!file.exists() || !file.isDirectory()) {
            Slog.d(TAG, "No directory " + file + ", skipping");
            return;
        }
        for (File file2 : file.listFiles()) {
            Slog.d(TAG, "Found a config file: " + file2.getPath());
            readConfig(file2);
        }
    }

    private void readConfig(File file) {
        BufferedInputStream bufferedInputStream;
        try {
            try {
                bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            } catch (IOException | DatatypeConfigurationException | XmlPullParserException e) {
                Slog.e(TAG, "Encountered an error while reading/parsing compat config file", e);
            }
            try {
                for (Change change : XmlParser.read(bufferedInputStream).getCompatChange()) {
                    Slog.d(TAG, "Adding: " + change.toString());
                    this.mChanges.put(Long.valueOf(change.getId()), new CompatChange(change));
                }
                bufferedInputStream.close();
            } catch (Throwable th) {
                try {
                    bufferedInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } finally {
            invalidateCache();
        }
    }

    private void initOverrides() {
        initOverrides(new File(APP_COMPAT_DATA_DIR, OVERRIDES_FILE), new File(STATIC_OVERRIDES_PRODUCT_DIR, OVERRIDES_FILE));
    }

    @VisibleForTesting
    void initOverrides(File file, File file2) {
        Iterator<CompatChange> it = this.mChanges.values().iterator();
        while (it.hasNext()) {
            it.next().clearOverrides();
        }
        loadOverrides(file2);
        synchronized (this.mOverridesFileLock) {
            this.mOverridesFile = file;
            File makeBackupFile = makeBackupFile(file);
            this.mBackupOverridesFile = makeBackupFile;
            if (makeBackupFile.exists()) {
                this.mOverridesFile.delete();
                this.mBackupOverridesFile.renameTo(this.mOverridesFile);
            }
            loadOverrides(this.mOverridesFile);
        }
        if (file2.exists()) {
            saveOverrides();
        }
    }

    private File makeBackupFile(File file) {
        return new File(file.getPath() + ".bak");
    }

    private void loadOverrides(File file) {
        if (file.exists()) {
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                try {
                    Overrides read = com.android.server.compat.overrides.XmlParser.read(bufferedInputStream);
                    if (read == null) {
                        Slog.w(TAG, "Parsing " + file.getPath() + " failed");
                        bufferedInputStream.close();
                        return;
                    }
                    for (ChangeOverrides changeOverrides : read.getChangeOverrides()) {
                        long changeId = changeOverrides.getChangeId();
                        CompatChange compatChange = this.mChanges.get(Long.valueOf(changeId));
                        if (compatChange == null) {
                            Slog.w(TAG, "Change ID " + changeId + " not found. Skipping overrides for it.");
                        } else {
                            compatChange.loadOverrides(changeOverrides);
                        }
                    }
                    bufferedInputStream.close();
                } catch (Throwable th) {
                    try {
                        bufferedInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException | DatatypeConfigurationException | XmlPullParserException e) {
                Slog.w(TAG, "Error processing " + file + " " + e.toString());
            }
        }
    }

    void saveOverrides() {
        PrintWriter printWriter;
        synchronized (this.mOverridesFileLock) {
            if (this.mOverridesFile != null && this.mBackupOverridesFile != null) {
                Overrides overrides = new Overrides();
                List<ChangeOverrides> changeOverrides = overrides.getChangeOverrides();
                Iterator<CompatChange> it = this.mChanges.values().iterator();
                while (it.hasNext()) {
                    ChangeOverrides saveOverrides = it.next().saveOverrides();
                    if (saveOverrides != null) {
                        changeOverrides.add(saveOverrides);
                    }
                }
                if (this.mOverridesFile.exists()) {
                    if (this.mBackupOverridesFile.exists()) {
                        this.mOverridesFile.delete();
                    } else if (!this.mOverridesFile.renameTo(this.mBackupOverridesFile)) {
                        Slog.e(TAG, "Couldn't rename file " + this.mOverridesFile + " to " + this.mBackupOverridesFile);
                        return;
                    }
                }
                try {
                    this.mOverridesFile.createNewFile();
                    try {
                        printWriter = new PrintWriter(this.mOverridesFile);
                    } catch (IOException e) {
                        Slog.e(TAG, e.toString());
                    }
                    try {
                        XmlWriter.write(new XmlWriter(printWriter), overrides);
                        printWriter.close();
                        this.mBackupOverridesFile.delete();
                    } catch (Throwable th) {
                        try {
                            printWriter.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (IOException e2) {
                    Slog.e(TAG, "Could not create override config file: " + e2.toString());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IOverrideValidator getOverrideValidator() {
        return this.mOverrideValidator;
    }

    private void invalidateCache() {
        ChangeIdStateCache.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void recheckOverrides(String str) {
        Long versionCodeOrNull = getVersionCodeOrNull(str);
        boolean z = false;
        for (CompatChange compatChange : this.mChanges.values()) {
            z |= compatChange.recheckOverride(str, this.mOverrideValidator.getOverrideAllowedStateForRecheck(compatChange.getId(), str), versionCodeOrNull);
        }
        if (z) {
            invalidateCache();
        }
    }

    private Long getVersionCodeOrNull(String str) {
        try {
            return Long.valueOf(this.mContext.getPackageManager().getApplicationInfo(str, AudioDevice.OUT_SPEAKER_SAFE).longVersionCode);
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerContentObserver() {
        this.mOverrideValidator.registerContentObserver();
    }
}
