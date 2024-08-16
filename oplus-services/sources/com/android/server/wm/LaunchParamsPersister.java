package com.android.server.wm;

import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManagerInternal;
import android.graphics.Rect;
import android.os.Environment;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.SparseArray;
import android.util.Xml;
import android.view.DisplayInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.LocalServices;
import com.android.server.pm.PackageList;
import com.android.server.wm.LaunchParamsController;
import com.android.server.wm.LaunchParamsPersister;
import com.android.server.wm.PersisterQueue;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class LaunchParamsPersister {
    private static final char ESCAPED_COMPONENT_SEPARATOR = '-';
    private static final String LAUNCH_PARAMS_DIRNAME = "launch_params";
    private static final String LAUNCH_PARAMS_FILE_SUFFIX = ".xml";
    private static final char OLD_ESCAPED_COMPONENT_SEPARATOR = '_';
    private static final char ORIGINAL_COMPONENT_SEPARATOR = '/';
    private static final String TAG = "LaunchParamsPersister";
    private static final String TAG_LAUNCH_PARAMS = "launch_params";
    private final SparseArray<ArrayMap<ComponentName, PersistableLaunchParams>> mLaunchParamsMap;
    private PackageList mPackageList;
    private final PersisterQueue mPersisterQueue;
    private final ActivityTaskSupervisor mSupervisor;
    private final IntFunction<File> mUserFolderGetter;
    private final ArrayMap<String, ArraySet<ComponentName>> mWindowLayoutAffinityMap;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LaunchParamsPersister(PersisterQueue persisterQueue, ActivityTaskSupervisor activityTaskSupervisor) {
        this(persisterQueue, activityTaskSupervisor, new IntFunction() { // from class: com.android.server.wm.LaunchParamsPersister$$ExternalSyntheticLambda2
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return Environment.getDataSystemCeDirectory(i);
            }
        });
    }

    @VisibleForTesting
    LaunchParamsPersister(PersisterQueue persisterQueue, ActivityTaskSupervisor activityTaskSupervisor, IntFunction<File> intFunction) {
        this.mLaunchParamsMap = new SparseArray<>();
        this.mWindowLayoutAffinityMap = new ArrayMap<>();
        this.mPersisterQueue = persisterQueue;
        this.mSupervisor = activityTaskSupervisor;
        this.mUserFolderGetter = intFunction;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSystemReady() {
        this.mPackageList = ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).getPackageList(new PackageListObserver());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUnlockUser(int i) {
        loadLaunchParams(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onCleanupUser(int i) {
        this.mLaunchParamsMap.remove(i);
    }

    private void loadLaunchParams(int i) {
        File file;
        ArrayList arrayList = new ArrayList();
        File launchParamFolder = getLaunchParamFolder(i);
        if (!launchParamFolder.isDirectory()) {
            Slog.i(TAG, "Didn't find launch param folder for user " + i);
            return;
        }
        ArraySet arraySet = new ArraySet(this.mPackageList.getPackageNames());
        File[] listFiles = launchParamFolder.listFiles();
        ArrayMap<ComponentName, PersistableLaunchParams> arrayMap = new ArrayMap<>(listFiles.length);
        this.mLaunchParamsMap.put(i, arrayMap);
        int length = listFiles.length;
        int i2 = 0;
        int i3 = 0;
        while (i3 < length) {
            File file2 = listFiles[i3];
            if (!file2.isFile()) {
                Slog.w(TAG, file2.getAbsolutePath() + " is not a file.");
            } else if (!file2.getName().endsWith(LAUNCH_PARAMS_FILE_SUFFIX)) {
                Slog.w(TAG, "Unexpected params file name: " + file2.getName());
                arrayList.add(file2);
            } else {
                String name = file2.getName();
                int indexOf = name.indexOf(95);
                if (indexOf != -1) {
                    if (name.indexOf(95, indexOf + 1) != -1) {
                        arrayList.add(file2);
                    } else {
                        name = name.replace(OLD_ESCAPED_COMPONENT_SEPARATOR, ESCAPED_COMPONENT_SEPARATOR);
                        File file3 = new File(launchParamFolder, name);
                        if (file2.renameTo(file3)) {
                            file2 = file3;
                        } else {
                            arrayList.add(file2);
                        }
                    }
                }
                ComponentName unflattenFromString = ComponentName.unflattenFromString(name.substring(i2, name.length() - 4).replace(ESCAPED_COMPONENT_SEPARATOR, ORIGINAL_COMPONENT_SEPARATOR));
                if (unflattenFromString == null) {
                    Slog.w(TAG, "Unexpected file name: " + name);
                    arrayList.add(file2);
                } else if (!arraySet.contains(unflattenFromString.getPackageName())) {
                    arrayList.add(file2);
                } else {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file2);
                        try {
                            PersistableLaunchParams persistableLaunchParams = new PersistableLaunchParams();
                            TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(fileInputStream);
                            while (true) {
                                int next = resolvePullParser.next();
                                if (next == 1 || next == 3) {
                                    break;
                                }
                                if (next == 2) {
                                    String name2 = resolvePullParser.getName();
                                    if (!"launch_params".equals(name2)) {
                                        StringBuilder sb = new StringBuilder();
                                        file = launchParamFolder;
                                        try {
                                            sb.append("Unexpected tag name: ");
                                            sb.append(name2);
                                            Slog.w(TAG, sb.toString());
                                        } catch (Throwable th) {
                                            th = th;
                                            Throwable th2 = th;
                                            try {
                                                fileInputStream.close();
                                            } catch (Throwable th3) {
                                                th2.addSuppressed(th3);
                                            }
                                            throw th2;
                                            break;
                                        }
                                    } else {
                                        file = launchParamFolder;
                                        persistableLaunchParams.restore(file2, resolvePullParser);
                                    }
                                    launchParamFolder = file;
                                }
                            }
                            file = launchParamFolder;
                            arrayMap.put(unflattenFromString, persistableLaunchParams);
                            addComponentNameToLaunchParamAffinityMapIfNotNull(unflattenFromString, persistableLaunchParams.mWindowLayoutAffinity);
                            try {
                                fileInputStream.close();
                            } catch (Exception e) {
                                e = e;
                                Slog.w(TAG, "Failed to restore launch params for " + unflattenFromString, e);
                                arrayList.add(file2);
                                i3++;
                                launchParamFolder = file;
                                i2 = 0;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            file = launchParamFolder;
                        }
                    } catch (Exception e2) {
                        e = e2;
                        file = launchParamFolder;
                    }
                    i3++;
                    launchParamFolder = file;
                    i2 = 0;
                }
            }
            file = launchParamFolder;
            i3++;
            launchParamFolder = file;
            i2 = 0;
        }
        if (arrayList.isEmpty()) {
            return;
        }
        this.mPersisterQueue.addItem(new CleanUpComponentQueueItem(arrayList), true);
    }

    void saveTask(Task task) {
        saveTask(task, task.getDisplayContent());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveTask(Task task, DisplayContent displayContent) {
        ComponentName componentName = task.realActivity;
        if (componentName == null) {
            return;
        }
        int i = task.mUserId;
        ArrayMap<ComponentName, PersistableLaunchParams> arrayMap = this.mLaunchParamsMap.get(i);
        if (arrayMap == null) {
            arrayMap = new ArrayMap<>();
            this.mLaunchParamsMap.put(i, arrayMap);
        }
        PersistableLaunchParams computeIfAbsent = arrayMap.computeIfAbsent(componentName, new Function() { // from class: com.android.server.wm.LaunchParamsPersister$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                LaunchParamsPersister.PersistableLaunchParams lambda$saveTask$0;
                lambda$saveTask$0 = LaunchParamsPersister.this.lambda$saveTask$0((ComponentName) obj);
                return lambda$saveTask$0;
            }
        });
        boolean saveTaskToLaunchParam = saveTaskToLaunchParam(task, displayContent, computeIfAbsent);
        addComponentNameToLaunchParamAffinityMapIfNotNull(componentName, computeIfAbsent.mWindowLayoutAffinity);
        if (saveTaskToLaunchParam) {
            this.mPersisterQueue.updateLastOrAddItem(new LaunchParamsWriteQueueItem(i, componentName, computeIfAbsent), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ PersistableLaunchParams lambda$saveTask$0(ComponentName componentName) {
        return new PersistableLaunchParams();
    }

    private boolean saveTaskToLaunchParam(Task task, DisplayContent displayContent, PersistableLaunchParams persistableLaunchParams) {
        boolean z;
        DisplayInfo displayInfo = new DisplayInfo();
        displayContent.mDisplay.getDisplayInfo(displayInfo);
        boolean z2 = !Objects.equals(persistableLaunchParams.mDisplayUniqueId, displayInfo.uniqueId);
        persistableLaunchParams.mDisplayUniqueId = displayInfo.uniqueId;
        boolean z3 = (persistableLaunchParams.mWindowingMode != task.getWindowingMode()) | z2;
        persistableLaunchParams.mWindowingMode = task.getWindowingMode();
        if (task.mLastNonFullscreenBounds != null) {
            z = z3 | (!Objects.equals(persistableLaunchParams.mBounds, r4));
            persistableLaunchParams.mBounds.set(task.mLastNonFullscreenBounds);
        } else {
            z = z3 | (!persistableLaunchParams.mBounds.isEmpty());
            persistableLaunchParams.mBounds.setEmpty();
        }
        String str = task.mWindowLayoutAffinity;
        boolean equals = z | Objects.equals(str, persistableLaunchParams.mWindowLayoutAffinity);
        persistableLaunchParams.mWindowLayoutAffinity = str;
        if (equals) {
            persistableLaunchParams.mTimestamp = System.currentTimeMillis();
        }
        return equals;
    }

    private void addComponentNameToLaunchParamAffinityMapIfNotNull(ComponentName componentName, String str) {
        if (str == null) {
            return;
        }
        this.mWindowLayoutAffinityMap.computeIfAbsent(str, new Function() { // from class: com.android.server.wm.LaunchParamsPersister$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                ArraySet lambda$addComponentNameToLaunchParamAffinityMapIfNotNull$1;
                lambda$addComponentNameToLaunchParamAffinityMapIfNotNull$1 = LaunchParamsPersister.lambda$addComponentNameToLaunchParamAffinityMapIfNotNull$1((String) obj);
                return lambda$addComponentNameToLaunchParamAffinityMapIfNotNull$1;
            }
        }).add(componentName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ArraySet lambda$addComponentNameToLaunchParamAffinityMapIfNotNull$1(String str) {
        return new ArraySet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getLaunchParams(Task task, ActivityRecord activityRecord, LaunchParamsController.LaunchParams launchParams) {
        String str;
        ComponentName componentName = task != null ? task.realActivity : activityRecord.mActivityComponent;
        int i = task != null ? task.mUserId : activityRecord.mUserId;
        if (task != null) {
            str = task.mWindowLayoutAffinity;
        } else {
            ActivityInfo.WindowLayout windowLayout = activityRecord.info.windowLayout;
            str = windowLayout == null ? null : windowLayout.windowLayoutAffinity;
        }
        launchParams.reset();
        ArrayMap<ComponentName, PersistableLaunchParams> arrayMap = this.mLaunchParamsMap.get(i);
        if (arrayMap == null) {
            return;
        }
        PersistableLaunchParams persistableLaunchParams = arrayMap.get(componentName);
        if (str != null && this.mWindowLayoutAffinityMap.get(str) != null) {
            ArraySet<ComponentName> arraySet = this.mWindowLayoutAffinityMap.get(str);
            for (int i2 = 0; i2 < arraySet.size(); i2++) {
                PersistableLaunchParams persistableLaunchParams2 = arrayMap.get(arraySet.valueAt(i2));
                if (persistableLaunchParams2 != null && (persistableLaunchParams == null || persistableLaunchParams2.mTimestamp > persistableLaunchParams.mTimestamp)) {
                    persistableLaunchParams = persistableLaunchParams2;
                }
            }
        }
        if (persistableLaunchParams == null) {
            return;
        }
        DisplayContent displayContent = this.mSupervisor.mRootWindowContainer.getDisplayContent(persistableLaunchParams.mDisplayUniqueId);
        if (displayContent != null) {
            launchParams.mPreferredTaskDisplayArea = displayContent.getDefaultTaskDisplayArea();
        }
        launchParams.mWindowingMode = persistableLaunchParams.mWindowingMode;
        launchParams.mBounds.set(persistableLaunchParams.mBounds);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeRecordForPackage(final String str) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.mLaunchParamsMap.size(); i++) {
            File launchParamFolder = getLaunchParamFolder(this.mLaunchParamsMap.keyAt(i));
            ArrayMap<ComponentName, PersistableLaunchParams> valueAt = this.mLaunchParamsMap.valueAt(i);
            for (int size = valueAt.size() - 1; size >= 0; size--) {
                ComponentName keyAt = valueAt.keyAt(size);
                if (keyAt.getPackageName().equals(str)) {
                    valueAt.removeAt(size);
                    arrayList.add(getParamFile(launchParamFolder, keyAt));
                }
            }
        }
        synchronized (this.mPersisterQueue) {
            this.mPersisterQueue.removeItems(new Predicate() { // from class: com.android.server.wm.LaunchParamsPersister$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$removeRecordForPackage$2;
                    lambda$removeRecordForPackage$2 = LaunchParamsPersister.lambda$removeRecordForPackage$2(str, (LaunchParamsPersister.LaunchParamsWriteQueueItem) obj);
                    return lambda$removeRecordForPackage$2;
                }
            }, LaunchParamsWriteQueueItem.class);
            this.mPersisterQueue.addItem(new CleanUpComponentQueueItem(arrayList), true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$removeRecordForPackage$2(String str, LaunchParamsWriteQueueItem launchParamsWriteQueueItem) {
        return launchParamsWriteQueueItem.mComponentName.getPackageName().equals(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public File getParamFile(File file, ComponentName componentName) {
        return new File(file, componentName.flattenToShortString().replace(ORIGINAL_COMPONENT_SEPARATOR, ESCAPED_COMPONENT_SEPARATOR) + LAUNCH_PARAMS_FILE_SUFFIX);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public File getLaunchParamFolder(int i) {
        return new File(this.mUserFolderGetter.apply(i), "launch_params");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class PackageListObserver implements PackageManagerInternal.PackageListObserver {
        public void onPackageAdded(String str, int i) {
        }

        private PackageListObserver() {
        }

        public void onPackageRemoved(String str, int i) {
            WindowManagerGlobalLock globalLock = LaunchParamsPersister.this.mSupervisor.mService.getGlobalLock();
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (globalLock) {
                try {
                    LaunchParamsPersister.this.removeRecordForPackage(str);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class LaunchParamsWriteQueueItem implements PersisterQueue.WriteQueueItem<LaunchParamsWriteQueueItem> {
        private final ComponentName mComponentName;
        private PersistableLaunchParams mLaunchParams;
        private final int mUserId;

        private LaunchParamsWriteQueueItem(int i, ComponentName componentName, PersistableLaunchParams persistableLaunchParams) {
            this.mUserId = i;
            this.mComponentName = componentName;
            this.mLaunchParams = persistableLaunchParams;
        }

        private byte[] saveParamsToXml() {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(byteArrayOutputStream);
                resolveSerializer.startDocument((String) null, Boolean.TRUE);
                resolveSerializer.startTag((String) null, "launch_params");
                this.mLaunchParams.saveToXml(resolveSerializer);
                resolveSerializer.endTag((String) null, "launch_params");
                resolveSerializer.endDocument();
                resolveSerializer.flush();
                return byteArrayOutputStream.toByteArray();
            } catch (IOException unused) {
                return null;
            }
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void process() {
            FileOutputStream fileOutputStream;
            byte[] saveParamsToXml = saveParamsToXml();
            File launchParamFolder = LaunchParamsPersister.this.getLaunchParamFolder(this.mUserId);
            if (!launchParamFolder.isDirectory() && !launchParamFolder.mkdirs()) {
                Slog.w(LaunchParamsPersister.TAG, "Failed to create folder for " + this.mUserId);
                return;
            }
            AtomicFile atomicFile = new AtomicFile(LaunchParamsPersister.this.getParamFile(launchParamFolder, this.mComponentName));
            try {
                fileOutputStream = atomicFile.startWrite();
                try {
                    fileOutputStream.write(saveParamsToXml);
                    atomicFile.finishWrite(fileOutputStream);
                } catch (Exception e) {
                    e = e;
                    Slog.e(LaunchParamsPersister.TAG, "Failed to write param file for " + this.mComponentName, e);
                    if (fileOutputStream != null) {
                        atomicFile.failWrite(fileOutputStream);
                    }
                }
            } catch (Exception e2) {
                e = e2;
                fileOutputStream = null;
            }
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public boolean matches(LaunchParamsWriteQueueItem launchParamsWriteQueueItem) {
            return this.mUserId == launchParamsWriteQueueItem.mUserId && this.mComponentName.equals(launchParamsWriteQueueItem.mComponentName);
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void updateFrom(LaunchParamsWriteQueueItem launchParamsWriteQueueItem) {
            this.mLaunchParams = launchParamsWriteQueueItem.mLaunchParams;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class CleanUpComponentQueueItem implements PersisterQueue.WriteQueueItem {
        private final List<File> mComponentFiles;

        private CleanUpComponentQueueItem(List<File> list) {
            this.mComponentFiles = list;
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void process() {
            for (File file : this.mComponentFiles) {
                if (!file.delete()) {
                    Slog.w(LaunchParamsPersister.TAG, "Failed to delete " + file.getAbsolutePath());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class PersistableLaunchParams {
        private static final String ATTR_BOUNDS = "bounds";
        private static final String ATTR_DISPLAY_UNIQUE_ID = "display_unique_id";
        private static final String ATTR_WINDOWING_MODE = "windowing_mode";
        private static final String ATTR_WINDOW_LAYOUT_AFFINITY = "window_layout_affinity";
        final Rect mBounds;
        String mDisplayUniqueId;
        long mTimestamp;
        String mWindowLayoutAffinity;
        int mWindowingMode;

        private PersistableLaunchParams() {
            this.mBounds = new Rect();
        }

        void saveToXml(TypedXmlSerializer typedXmlSerializer) throws IOException {
            typedXmlSerializer.attribute((String) null, ATTR_DISPLAY_UNIQUE_ID, this.mDisplayUniqueId);
            typedXmlSerializer.attributeInt((String) null, ATTR_WINDOWING_MODE, this.mWindowingMode);
            typedXmlSerializer.attribute((String) null, ATTR_BOUNDS, this.mBounds.flattenToString());
            String str = this.mWindowLayoutAffinity;
            if (str != null) {
                typedXmlSerializer.attribute((String) null, ATTR_WINDOW_LAYOUT_AFFINITY, str);
            }
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:5:0x0018. Please report as an issue. */
        void restore(File file, TypedXmlPullParser typedXmlPullParser) {
            for (int i = 0; i < typedXmlPullParser.getAttributeCount(); i++) {
                String attributeValue = typedXmlPullParser.getAttributeValue(i);
                String attributeName = typedXmlPullParser.getAttributeName(i);
                attributeName.hashCode();
                char c = 65535;
                switch (attributeName.hashCode()) {
                    case -1499361012:
                        if (attributeName.equals(ATTR_DISPLAY_UNIQUE_ID)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1383205195:
                        if (attributeName.equals(ATTR_BOUNDS)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 748872656:
                        if (attributeName.equals(ATTR_WINDOWING_MODE)) {
                            c = 2;
                            break;
                        }
                        break;
                    case 1999609934:
                        if (attributeName.equals(ATTR_WINDOW_LAYOUT_AFFINITY)) {
                            c = 3;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        this.mDisplayUniqueId = attributeValue;
                        break;
                    case 1:
                        Rect unflattenFromString = Rect.unflattenFromString(attributeValue);
                        if (unflattenFromString != null) {
                            this.mBounds.set(unflattenFromString);
                            break;
                        } else {
                            break;
                        }
                    case 2:
                        this.mWindowingMode = Integer.parseInt(attributeValue);
                        break;
                    case 3:
                        this.mWindowLayoutAffinity = attributeValue;
                        break;
                }
            }
            this.mTimestamp = file.lastModified();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("PersistableLaunchParams{");
            sb.append(" windowingMode=" + this.mWindowingMode);
            sb.append(" displayUniqueId=" + this.mDisplayUniqueId);
            sb.append(" bounds=" + this.mBounds);
            if (this.mWindowLayoutAffinity != null) {
                sb.append(" launchParamsAffinity=" + this.mWindowLayoutAffinity);
            }
            sb.append(" timestamp=" + this.mTimestamp);
            sb.append(" }");
            return sb.toString();
        }
    }
}
