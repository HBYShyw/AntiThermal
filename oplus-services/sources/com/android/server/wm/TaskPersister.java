package com.android.server.wm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.FileUtils;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.Xml;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.wm.PersisterQueue;
import com.android.server.wm.TaskPersister;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class TaskPersister implements PersisterQueue.Listener {
    static final boolean DEBUG = false;
    private static final String IMAGES_DIRNAME = "recent_images";
    static final String IMAGE_EXTENSION = ".png";
    private static final String PERSISTED_TASK_IDS_FILENAME = "persisted_taskIds.txt";
    static final String TAG = "TaskPersister";
    private static final String TAG_TASK = "task";
    private static final String TASKS_DIRNAME = "recent_tasks";
    private static final String TASK_FILENAME_SUFFIX = "_task.xml";
    private final Object mIoLock;
    private final PersisterQueue mPersisterQueue;
    private final RecentTasks mRecentTasks;
    private final ActivityTaskManagerService mService;
    private final File mTaskIdsDir;
    private final SparseArray<SparseBooleanArray> mTaskIdsInFile;
    private final ActivityTaskSupervisor mTaskSupervisor;
    private final ArraySet<Integer> mTmpTaskIds;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskPersister(File file, ActivityTaskSupervisor activityTaskSupervisor, ActivityTaskManagerService activityTaskManagerService, RecentTasks recentTasks, PersisterQueue persisterQueue) {
        this.mTaskIdsInFile = new SparseArray<>();
        this.mIoLock = new Object();
        this.mTmpTaskIds = new ArraySet<>();
        File file2 = new File(file, IMAGES_DIRNAME);
        if (file2.exists() && (!FileUtils.deleteContents(file2) || !file2.delete())) {
            Slog.i(TAG, "Failure deleting legacy images directory: " + file2);
        }
        File file3 = new File(file, TASKS_DIRNAME);
        if (file3.exists() && (!FileUtils.deleteContents(file3) || !file3.delete())) {
            Slog.i(TAG, "Failure deleting legacy tasks directory: " + file3);
        }
        this.mTaskIdsDir = new File(Environment.getDataDirectory(), "system_de");
        this.mTaskSupervisor = activityTaskSupervisor;
        this.mService = activityTaskManagerService;
        this.mRecentTasks = recentTasks;
        this.mPersisterQueue = persisterQueue;
        persisterQueue.addListener(this);
    }

    @VisibleForTesting
    TaskPersister(File file) {
        this.mTaskIdsInFile = new SparseArray<>();
        this.mIoLock = new Object();
        this.mTmpTaskIds = new ArraySet<>();
        this.mTaskIdsDir = file;
        this.mTaskSupervisor = null;
        this.mService = null;
        this.mRecentTasks = null;
        PersisterQueue persisterQueue = new PersisterQueue();
        this.mPersisterQueue = persisterQueue;
        persisterQueue.addListener(this);
    }

    private void removeThumbnails(final Task task) {
        this.mPersisterQueue.removeItems(new Predicate() { // from class: com.android.server.wm.TaskPersister$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$removeThumbnails$0;
                lambda$removeThumbnails$0 = TaskPersister.lambda$removeThumbnails$0(Task.this, (TaskPersister.ImageWriteQueueItem) obj);
                return lambda$removeThumbnails$0;
            }
        }, ImageWriteQueueItem.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$removeThumbnails$0(Task task, ImageWriteQueueItem imageWriteQueueItem) {
        return new File(imageWriteQueueItem.mFilePath).getName().startsWith(Integer.toString(task.mTaskId));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Not initialized variable reg: 3, insn: 0x006f: MOVE (r2 I:??[OBJECT, ARRAY]) = (r3 I:??[OBJECT, ARRAY]), block:B:43:0x006f */
    public SparseBooleanArray loadPersistedTaskIdsForUser(int i) {
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2;
        Exception e;
        if (this.mTaskIdsInFile.get(i) != null) {
            return this.mTaskIdsInFile.get(i).clone();
        }
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        synchronized (this.mIoLock) {
            BufferedReader bufferedReader3 = null;
            try {
                try {
                    bufferedReader2 = new BufferedReader(new FileReader(getUserPersistedTaskIdsFile(i)));
                    while (true) {
                        try {
                            String readLine = bufferedReader2.readLine();
                            if (readLine == null) {
                                break;
                            }
                            for (String str : readLine.split("\\s+")) {
                                sparseBooleanArray.put(Integer.parseInt(str), true);
                            }
                        } catch (FileNotFoundException unused) {
                            bufferedReader3 = bufferedReader2;
                            IoUtils.closeQuietly(bufferedReader3);
                            this.mTaskIdsInFile.put(i, sparseBooleanArray);
                            return sparseBooleanArray.clone();
                        } catch (Exception e2) {
                            e = e2;
                            Slog.e(TAG, "Error while reading taskIds file for user " + i, e);
                            IoUtils.closeQuietly(bufferedReader2);
                            this.mTaskIdsInFile.put(i, sparseBooleanArray);
                            return sparseBooleanArray.clone();
                        }
                    }
                } catch (FileNotFoundException unused2) {
                } catch (Exception e3) {
                    bufferedReader2 = null;
                    e = e3;
                } catch (Throwable th) {
                    th = th;
                    IoUtils.closeQuietly(bufferedReader3);
                    throw th;
                }
                IoUtils.closeQuietly(bufferedReader2);
            } catch (Throwable th2) {
                th = th2;
                bufferedReader3 = bufferedReader;
            }
        }
        this.mTaskIdsInFile.put(i, sparseBooleanArray);
        return sparseBooleanArray.clone();
    }

    @VisibleForTesting
    void writePersistedTaskIdsForUser(SparseBooleanArray sparseBooleanArray, int i) {
        if (i < 0) {
            return;
        }
        File userPersistedTaskIdsFile = getUserPersistedTaskIdsFile(i);
        synchronized (this.mIoLock) {
            BufferedWriter bufferedWriter = null;
            try {
                try {
                    BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(userPersistedTaskIdsFile));
                    for (int i2 = 0; i2 < sparseBooleanArray.size(); i2++) {
                        try {
                            if (sparseBooleanArray.valueAt(i2)) {
                                bufferedWriter2.write(String.valueOf(sparseBooleanArray.keyAt(i2)));
                                bufferedWriter2.newLine();
                            }
                        } catch (Exception e) {
                            e = e;
                            bufferedWriter = bufferedWriter2;
                            Slog.e(TAG, "Error while writing taskIds file for user " + i, e);
                            IoUtils.closeQuietly(bufferedWriter);
                        } catch (Throwable th) {
                            th = th;
                            bufferedWriter = bufferedWriter2;
                            IoUtils.closeQuietly(bufferedWriter);
                            throw th;
                        }
                    }
                    IoUtils.closeQuietly(bufferedWriter2);
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e2) {
                e = e2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unloadUserDataFromMemory(int i) {
        this.mTaskIdsInFile.delete(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void wakeup(final Task task, boolean z) {
        synchronized (this.mPersisterQueue) {
            if (task != null) {
                TaskWriteQueueItem taskWriteQueueItem = (TaskWriteQueueItem) this.mPersisterQueue.findLastItem(new Predicate() { // from class: com.android.server.wm.TaskPersister$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$wakeup$1;
                        lambda$wakeup$1 = TaskPersister.lambda$wakeup$1(Task.this, (TaskPersister.TaskWriteQueueItem) obj);
                        return lambda$wakeup$1;
                    }
                }, TaskWriteQueueItem.class);
                if (taskWriteQueueItem != null && !task.inRecents) {
                    removeThumbnails(task);
                }
                if (taskWriteQueueItem == null && task.isPersistable) {
                    this.mPersisterQueue.addItem(new TaskWriteQueueItem(task, this.mService), z);
                }
            } else {
                this.mPersisterQueue.addItem(PersisterQueue.EMPTY_ITEM, z);
            }
        }
        this.mPersisterQueue.yieldIfQueueTooDeep();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$wakeup$1(Task task, TaskWriteQueueItem taskWriteQueueItem) {
        return task == taskWriteQueueItem.mTask;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void flush() {
        this.mPersisterQueue.flush();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveImage(Bitmap bitmap, String str) {
        this.mPersisterQueue.updateLastOrAddItem(new ImageWriteQueueItem(str, bitmap), false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bitmap getTaskDescriptionIcon(String str) {
        Bitmap imageFromWriteQueue = getImageFromWriteQueue(str);
        return imageFromWriteQueue != null ? imageFromWriteQueue : restoreImage(str);
    }

    private Bitmap getImageFromWriteQueue(final String str) {
        ImageWriteQueueItem imageWriteQueueItem = (ImageWriteQueueItem) this.mPersisterQueue.findLastItem(new Predicate() { // from class: com.android.server.wm.TaskPersister$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getImageFromWriteQueue$2;
                lambda$getImageFromWriteQueue$2 = TaskPersister.lambda$getImageFromWriteQueue$2(str, (TaskPersister.ImageWriteQueueItem) obj);
                return lambda$getImageFromWriteQueue$2;
            }
        }, ImageWriteQueueItem.class);
        if (imageWriteQueueItem != null) {
            return imageWriteQueueItem.mImage;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getImageFromWriteQueue$2(String str, ImageWriteQueueItem imageWriteQueueItem) {
        return imageWriteQueueItem.mFilePath.equals(str);
    }

    private String fileToString(File file) {
        String lineSeparator = System.lineSeparator();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuffer stringBuffer = new StringBuffer(((int) file.length()) * 2);
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    stringBuffer.append(readLine + lineSeparator);
                } else {
                    bufferedReader.close();
                    return stringBuffer.toString();
                }
            }
        } catch (IOException unused) {
            Slog.e(TAG, "Couldn't read file " + file.getName());
            return null;
        }
    }

    private Task taskIdToTask(int i, ArrayList<Task> arrayList) {
        if (i < 0) {
            return null;
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            Task task = arrayList.get(size);
            if (task.mTaskId == i) {
                return task;
            }
        }
        Slog.e(TAG, "Restore affiliation error looking for taskId=" + i);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v0 */
    /* JADX WARN: Type inference failed for: r8v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r8v21 */
    public List<Task> restoreTasksForUserLocked(int i, SparseBooleanArray sparseBooleanArray) {
        File[] fileArr;
        int parseInt;
        FileInputStream fileInputStream;
        ArrayList<Task> arrayList = new ArrayList<>();
        ArraySet arraySet = new ArraySet();
        File userTasksDir = getUserTasksDir(i);
        File[] listFiles = userTasksDir.listFiles();
        if (listFiles == null) {
            Slog.e(TAG, "restoreTasksForUserLocked: Unable to list files from " + userTasksDir);
            return arrayList;
        }
        ?? r8 = 0;
        int i2 = 0;
        while (true) {
            int i3 = 1;
            if (i2 < listFiles.length) {
                File file = listFiles[i2];
                if (file.getName().endsWith(TASK_FILENAME_SUFFIX)) {
                    try {
                        parseInt = Integer.parseInt(file.getName().substring(r8, file.getName().length() - 9));
                    } catch (NumberFormatException e) {
                        e = e;
                    }
                    try {
                    } catch (NumberFormatException e2) {
                        e = e2;
                        fileArr = listFiles;
                        Slog.w(TAG, "Unexpected task file name", e);
                        i2++;
                        listFiles = fileArr;
                        r8 = 0;
                    }
                    if (sparseBooleanArray.get(parseInt, r8)) {
                        Slog.w(TAG, "Task #" + parseInt + " has already been created so we don't restore again");
                    } else {
                        try {
                            fileInputStream = new FileInputStream(file);
                        } catch (Exception e3) {
                            e = e3;
                            fileArr = listFiles;
                        }
                        try {
                            TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(fileInputStream);
                            while (true) {
                                int next = resolvePullParser.next();
                                if (next == i3 || next == 3) {
                                    break;
                                }
                                String name = resolvePullParser.getName();
                                if (next != 2) {
                                    fileArr = listFiles;
                                } else if (TAG_TASK.equals(name)) {
                                    Task restoreFromXml = Task.restoreFromXml(resolvePullParser, this.mTaskSupervisor);
                                    if (restoreFromXml != null) {
                                        int i4 = restoreFromXml.mTaskId;
                                        boolean hasActivity = restoreFromXml.hasActivity();
                                        if (hasActivity) {
                                            fileArr = listFiles;
                                            try {
                                                if (this.mRecentTasks.getTask(i4) != null) {
                                                    Slog.wtf(TAG, "Existing persisted task with taskId " + i4 + " found");
                                                }
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
                                            fileArr = listFiles;
                                        }
                                        if (!hasActivity && this.mService.mRootWindowContainer.anyTaskForId(i4, 1) != null) {
                                            Slog.wtf(TAG, "Existing task with taskId " + i4 + " found");
                                        } else if (i != restoreFromXml.mUserId) {
                                            Slog.wtf(TAG, "Task with userId " + restoreFromXml.mUserId + " found in " + userTasksDir.getAbsolutePath());
                                        } else {
                                            this.mTaskSupervisor.setNextTaskIdForUser(i4, i);
                                            restoreFromXml.isPersistable = true;
                                            arrayList.add(restoreFromXml);
                                            arraySet.add(Integer.valueOf(i4));
                                        }
                                    } else {
                                        fileArr = listFiles;
                                        Slog.e(TAG, "restoreTasksForUserLocked: Unable to restore taskFile=" + file + ": " + fileToString(file));
                                    }
                                } else {
                                    fileArr = listFiles;
                                    Slog.wtf(TAG, "restoreTasksForUserLocked: Unknown xml event=" + next + " name=" + name);
                                }
                                XmlUtils.skipCurrentTag(resolvePullParser);
                                listFiles = fileArr;
                                i3 = 1;
                            }
                            fileArr = listFiles;
                            try {
                                fileInputStream.close();
                            } catch (Exception e4) {
                                e = e4;
                                Slog.wtf(TAG, "Unable to parse " + file + ". Error ", e);
                                StringBuilder sb = new StringBuilder();
                                sb.append("Failing file: ");
                                sb.append(fileToString(file));
                                Slog.e(TAG, sb.toString());
                                file.delete();
                                i2++;
                                listFiles = fileArr;
                                r8 = 0;
                            }
                            i2++;
                            listFiles = fileArr;
                            r8 = 0;
                        } catch (Throwable th4) {
                            th = th4;
                            fileArr = listFiles;
                        }
                    }
                }
                fileArr = listFiles;
                i2++;
                listFiles = fileArr;
                r8 = 0;
            } else {
                removeObsoleteFiles(arraySet, userTasksDir.listFiles());
                for (int size = arrayList.size() - 1; size >= 0; size--) {
                    Task task = arrayList.get(size);
                    task.setPrevAffiliate(taskIdToTask(task.mPrevAffiliateTaskId, arrayList));
                    task.setNextAffiliate(taskIdToTask(task.mNextAffiliateTaskId, arrayList));
                }
                Collections.sort(arrayList, new Comparator<Task>() { // from class: com.android.server.wm.TaskPersister.1
                    @Override // java.util.Comparator
                    public int compare(Task task2, Task task3) {
                        long j = task3.mLastTimeMoved - task2.mLastTimeMoved;
                        if (j < 0) {
                            return -1;
                        }
                        return j > 0 ? 1 : 0;
                    }
                });
                return arrayList;
            }
        }
    }

    @Override // com.android.server.wm.PersisterQueue.Listener
    public void onPreProcessItem(boolean z) {
        if (z) {
            this.mTmpTaskIds.clear();
            WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mRecentTasks.getPersistableTaskIds(this.mTmpTaskIds);
                    this.mService.mWindowManager.removeObsoleteTaskFiles(this.mTmpTaskIds, this.mRecentTasks.usersWithRecentsLoadedLocked());
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            removeObsoleteFiles(this.mTmpTaskIds);
        }
        writeTaskIdsFiles();
    }

    private static void removeObsoleteFiles(ArraySet<Integer> arraySet, File[] fileArr) {
        if (fileArr == null) {
            Slog.e(TAG, "File error accessing recents directory (directory doesn't exist?).");
            return;
        }
        for (File file : fileArr) {
            String name = file.getName();
            int indexOf = name.indexOf(95);
            if (indexOf > 0) {
                try {
                    if (!arraySet.contains(Integer.valueOf(Integer.parseInt(name.substring(0, indexOf))))) {
                        file.delete();
                    }
                } catch (Exception unused) {
                    Slog.wtf(TAG, "removeObsoleteFiles: Can't parse file=" + file.getName());
                    file.delete();
                }
            }
        }
    }

    private void writeTaskIdsFiles() {
        int i;
        SparseArray sparseArray = new SparseArray();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                for (int i2 : this.mRecentTasks.usersWithRecentsLoadedLocked()) {
                    SparseBooleanArray taskIdsForUser = this.mRecentTasks.getTaskIdsForUser(i2);
                    SparseBooleanArray sparseBooleanArray = this.mTaskIdsInFile.get(i2);
                    if (sparseBooleanArray == null || !sparseBooleanArray.equals(taskIdsForUser)) {
                        SparseBooleanArray clone = taskIdsForUser.clone();
                        this.mTaskIdsInFile.put(i2, clone);
                        sparseArray.put(i2, clone);
                    }
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        for (i = 0; i < sparseArray.size(); i++) {
            writePersistedTaskIdsForUser((SparseBooleanArray) sparseArray.valueAt(i), sparseArray.keyAt(i));
        }
    }

    private void removeObsoleteFiles(ArraySet<Integer> arraySet) {
        int[] usersWithRecentsLoadedLocked;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                usersWithRecentsLoadedLocked = this.mRecentTasks.usersWithRecentsLoadedLocked();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        for (int i : usersWithRecentsLoadedLocked) {
            removeObsoleteFiles(arraySet, getUserImagesDir(i).listFiles());
            removeObsoleteFiles(arraySet, getUserTasksDir(i).listFiles());
        }
    }

    static Bitmap restoreImage(String str) {
        return BitmapFactory.decodeFile(str);
    }

    private File getUserPersistedTaskIdsFile(int i) {
        File file = new File(this.mTaskIdsDir, String.valueOf(i));
        if (!file.exists() && !file.mkdirs()) {
            Slog.e(TAG, "Error while creating user directory: " + file);
        }
        return new File(file, PERSISTED_TASK_IDS_FILENAME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File getUserTasksDir(int i) {
        return new File(Environment.getDataSystemCeDirectory(i), TASKS_DIRNAME);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static File getUserImagesDir(int i) {
        return new File(Environment.getDataSystemCeDirectory(i), IMAGES_DIRNAME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean createParentDirectory(String str) {
        File parentFile = new File(str).getParentFile();
        return parentFile.exists() || parentFile.mkdirs();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class TaskWriteQueueItem implements PersisterQueue.WriteQueueItem {
        private final ActivityTaskManagerService mService;
        private final Task mTask;

        TaskWriteQueueItem(Task task, ActivityTaskManagerService activityTaskManagerService) {
            this.mTask = task;
            this.mService = activityTaskManagerService;
        }

        private byte[] saveToXml(Task task) throws Exception {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(byteArrayOutputStream);
            resolveSerializer.startDocument((String) null, Boolean.TRUE);
            resolveSerializer.startTag((String) null, TaskPersister.TAG_TASK);
            task.saveToXml(resolveSerializer);
            resolveSerializer.endTag((String) null, TaskPersister.TAG_TASK);
            resolveSerializer.endDocument();
            resolveSerializer.flush();
            return byteArrayOutputStream.toByteArray();
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void process() {
            FileOutputStream fileOutputStream;
            byte[] saveToXml;
            AtomicFile atomicFile;
            Task task = this.mTask;
            WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    fileOutputStream = null;
                    if (task.inRecents) {
                        try {
                            saveToXml = saveToXml(task);
                        } catch (Exception unused) {
                        }
                    }
                    saveToXml = null;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            if (saveToXml == null) {
                return;
            }
            try {
                File userTasksDir = TaskPersister.getUserTasksDir(task.mUserId);
                if (!userTasksDir.isDirectory() && !userTasksDir.mkdirs()) {
                    Slog.e(TaskPersister.TAG, "Failure creating tasks directory for user " + task.mUserId + ": " + userTasksDir + " Dropping persistence for task " + task);
                    return;
                }
                atomicFile = new AtomicFile(new File(userTasksDir, String.valueOf(task.mTaskId) + TaskPersister.TASK_FILENAME_SUFFIX));
                try {
                    fileOutputStream = atomicFile.startWrite();
                    fileOutputStream.write(saveToXml);
                    atomicFile.finishWrite(fileOutputStream);
                } catch (IOException e) {
                    e = e;
                    if (fileOutputStream != null) {
                        atomicFile.failWrite(fileOutputStream);
                    }
                    Slog.e(TaskPersister.TAG, "Unable to open " + atomicFile + " for persisting. " + e);
                }
            } catch (IOException e2) {
                e = e2;
                atomicFile = null;
            }
        }

        public String toString() {
            return "TaskWriteQueueItem{task=" + this.mTask + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class ImageWriteQueueItem implements PersisterQueue.WriteQueueItem<ImageWriteQueueItem> {
        final String mFilePath;
        Bitmap mImage;

        ImageWriteQueueItem(String str, Bitmap bitmap) {
            this.mFilePath = str;
            this.mImage = bitmap;
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void process() {
            String str = this.mFilePath;
            if (!TaskPersister.createParentDirectory(str)) {
                Slog.e(TaskPersister.TAG, "Error while creating images directory for file: " + str);
                return;
            }
            Bitmap bitmap = this.mImage;
            FileOutputStream fileOutputStream = null;
            try {
                try {
                    FileOutputStream fileOutputStream2 = new FileOutputStream(new File(str));
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream2);
                        IoUtils.closeQuietly(fileOutputStream2);
                    } catch (Exception e) {
                        e = e;
                        fileOutputStream = fileOutputStream2;
                        Slog.e(TaskPersister.TAG, "saveImage: unable to save " + str, e);
                        IoUtils.closeQuietly(fileOutputStream);
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                        IoUtils.closeQuietly(fileOutputStream);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e2) {
                e = e2;
            }
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public boolean matches(ImageWriteQueueItem imageWriteQueueItem) {
            return this.mFilePath.equals(imageWriteQueueItem.mFilePath);
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void updateFrom(ImageWriteQueueItem imageWriteQueueItem) {
            this.mImage = imageWriteQueueItem.mImage;
        }

        public String toString() {
            return "ImageWriteQueueItem{path=" + this.mFilePath + ", image=(" + this.mImage.getWidth() + "x" + this.mImage.getHeight() + ")}";
        }
    }
}
