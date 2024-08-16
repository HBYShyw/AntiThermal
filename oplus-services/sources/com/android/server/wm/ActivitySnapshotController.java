package com.android.server.wm;

import android.R;
import android.app.ActivityManager;
import android.os.SystemProperties;
import android.util.ArraySet;
import android.util.SparseArray;
import android.window.TaskSnapshot;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;
import com.android.server.pm.UserManagerInternal;
import com.android.server.wm.BaseAppSnapshotPersister;
import com.android.server.wm.SnapshotController;
import com.android.server.wm.SnapshotPersistQueue;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivitySnapshotController extends AbsAppSnapshotController<ActivityRecord, ActivitySnapshotCache> {
    private static final boolean DEBUG = false;
    private static final int MAX_PERSIST_SNAPSHOT_COUNT = 20;
    static final String SNAPSHOTS_DIRNAME = "activity_snapshots";
    private static final String TAG = "WindowManager";

    @VisibleForTesting
    final ArraySet<ActivityRecord> mPendingCaptureActivity;

    @VisibleForTesting
    final ArraySet<ActivityRecord> mPendingDeleteActivity;

    @VisibleForTesting
    final ArraySet<ActivityRecord> mPendingLoadActivity;

    @VisibleForTesting
    final ArraySet<ActivityRecord> mPendingRemoveActivity;
    private final BaseAppSnapshotPersister.PersistInfoProvider mPersistInfoProvider;
    private final TaskSnapshotPersister mPersister;
    private final ArrayList<UserSavedFile> mSavedFilesInOrder;
    private final AppSnapshotLoader mSnapshotLoader;
    private final SnapshotPersistQueue mSnapshotPersistQueue;
    private final SparseArray<SparseArray<UserSavedFile>> mUserSavedFiles;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public ActivityRecord getTopActivity(ActivityRecord activityRecord) {
        return activityRecord;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivitySnapshotController(WindowManagerService windowManagerService, SnapshotPersistQueue snapshotPersistQueue) {
        super(windowManagerService);
        this.mPendingCaptureActivity = new ArraySet<>();
        this.mPendingRemoveActivity = new ArraySet<>();
        this.mPendingDeleteActivity = new ArraySet<>();
        this.mPendingLoadActivity = new ArraySet<>();
        this.mUserSavedFiles = new SparseArray<>();
        this.mSavedFilesInOrder = new ArrayList<>();
        this.mSnapshotPersistQueue = snapshotPersistQueue;
        BaseAppSnapshotPersister.PersistInfoProvider createPersistInfoProvider = createPersistInfoProvider(windowManagerService, new ActivitySnapshotController$$ExternalSyntheticLambda0());
        this.mPersistInfoProvider = createPersistInfoProvider;
        this.mPersister = new TaskSnapshotPersister(snapshotPersistQueue, createPersistInfoProvider);
        this.mSnapshotLoader = new AppSnapshotLoader(createPersistInfoProvider);
        initialize(new ActivitySnapshotCache(windowManagerService));
        setSnapshotEnabled((windowManagerService.mContext.getResources().getBoolean(R.bool.use_lock_pattern_drawable) || !isSnapshotEnabled() || ActivityManager.isLowRamDeviceStatic()) ? false : true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void systemReady() {
        if (shouldDisableSnapshots()) {
            return;
        }
        this.mService.mSnapshotController.registerTransitionStateConsumer(1, new Consumer() { // from class: com.android.server.wm.ActivitySnapshotController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivitySnapshotController.this.handleOpenActivityTransition((SnapshotController.TransitionState) obj);
            }
        });
        this.mService.mSnapshotController.registerTransitionStateConsumer(2, new Consumer() { // from class: com.android.server.wm.ActivitySnapshotController$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivitySnapshotController.this.handleCloseActivityTransition((SnapshotController.TransitionState) obj);
            }
        });
        this.mService.mSnapshotController.registerTransitionStateConsumer(4, new Consumer() { // from class: com.android.server.wm.ActivitySnapshotController$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivitySnapshotController.this.handleOpenTaskTransition((SnapshotController.TransitionState) obj);
            }
        });
        this.mService.mSnapshotController.registerTransitionStateConsumer(8, new Consumer() { // from class: com.android.server.wm.ActivitySnapshotController$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivitySnapshotController.this.handleCloseTaskTransition((SnapshotController.TransitionState) obj);
            }
        });
    }

    @Override // com.android.server.wm.AbsAppSnapshotController
    protected float initSnapshotScale() {
        return Math.max(Math.min(this.mService.mContext.getResources().getFloat(R.dimen.conversation_face_pile_avatar_size), 1.0f), 0.1f);
    }

    static boolean isSnapshotEnabled() {
        return SystemProperties.getInt("persist.wm.debug.activity_screenshot", 0) != 0;
    }

    static BaseAppSnapshotPersister.PersistInfoProvider createPersistInfoProvider(WindowManagerService windowManagerService, BaseAppSnapshotPersister.DirectoryResolver directoryResolver) {
        return new BaseAppSnapshotPersister.PersistInfoProvider(directoryResolver, SNAPSHOTS_DIRNAME, false, 0.0f, windowManagerService.mContext.getResources().getBoolean(17891879));
    }

    TaskSnapshot getSnapshot(ActivityRecord activityRecord) {
        return ((ActivitySnapshotCache) this.mCache).getSnapshot(Integer.valueOf(getSystemHashCode(activityRecord)));
    }

    private void cleanUpUserFiles(final int i) {
        synchronized (this.mSnapshotPersistQueue.getLock()) {
            this.mSnapshotPersistQueue.sendToQueueLocked(new SnapshotPersistQueue.WriteQueueItem(this.mPersistInfoProvider) { // from class: com.android.server.wm.ActivitySnapshotController.1
                @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
                boolean isReady() {
                    return ((UserManagerInternal) LocalServices.getService(UserManagerInternal.class)).isUserUnlocked(i);
                }

                @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
                void write() {
                    File[] listFiles;
                    File directory = this.mPersistInfoProvider.getDirectory(i);
                    if (!directory.exists() || (listFiles = directory.listFiles()) == null) {
                        return;
                    }
                    for (int length = listFiles.length - 1; length >= 0; length--) {
                        listFiles[length].delete();
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void preTransitionStart() {
        if (shouldDisableSnapshots()) {
            return;
        }
        resetTmpFields();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postTransitionStart() {
        if (shouldDisableSnapshots()) {
            return;
        }
        onCommitTransition();
    }

    @VisibleForTesting
    void resetTmpFields() {
        this.mPendingCaptureActivity.clear();
        this.mPendingRemoveActivity.clear();
        this.mPendingDeleteActivity.clear();
        this.mPendingLoadActivity.clear();
    }

    private void onCommitTransition() {
        for (int size = this.mPendingCaptureActivity.size() - 1; size >= 0; size--) {
            recordSnapshot(this.mPendingCaptureActivity.valueAt(size));
        }
        for (int size2 = this.mPendingRemoveActivity.size() - 1; size2 >= 0; size2--) {
            ((ActivitySnapshotCache) this.mCache).onIdRemoved(Integer.valueOf(getSystemHashCode(this.mPendingRemoveActivity.valueAt(size2))));
        }
        for (int size3 = this.mPendingDeleteActivity.size() - 1; size3 >= 0; size3--) {
            ActivityRecord valueAt = this.mPendingDeleteActivity.valueAt(size3);
            int systemHashCode = getSystemHashCode(valueAt);
            ((ActivitySnapshotCache) this.mCache).onIdRemoved(Integer.valueOf(systemHashCode));
            removeIfUserSavedFileExist(systemHashCode, valueAt.mUserId);
        }
        for (int size4 = this.mPendingLoadActivity.size() - 1; size4 >= 0; size4--) {
            final ActivityRecord valueAt2 = this.mPendingLoadActivity.valueAt(size4);
            final int systemHashCode2 = getSystemHashCode(valueAt2);
            final int i = valueAt2.mUserId;
            if (((ActivitySnapshotCache) this.mCache).getSnapshot(Integer.valueOf(systemHashCode2)) == null && containsFile(systemHashCode2, i)) {
                synchronized (this.mSnapshotPersistQueue.getLock()) {
                    this.mSnapshotPersistQueue.sendToQueueLocked(new SnapshotPersistQueue.WriteQueueItem(this.mPersistInfoProvider) { // from class: com.android.server.wm.ActivitySnapshotController.2
                        @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
                        void write() {
                            TaskSnapshot loadTask = ActivitySnapshotController.this.mSnapshotLoader.loadTask(systemHashCode2, i, false);
                            synchronized (ActivitySnapshotController.this.mService.getWindowManagerLock()) {
                                if (loadTask != null) {
                                    ActivityRecord activityRecord = valueAt2;
                                    if (!activityRecord.finishing) {
                                        ((ActivitySnapshotCache) ActivitySnapshotController.this.mCache).putSnapshot(activityRecord, loadTask);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
        resetTmpFields();
    }

    private void recordSnapshot(ActivityRecord activityRecord) {
        TaskSnapshot recordSnapshotInner = recordSnapshotInner(activityRecord, false);
        if (recordSnapshotInner != null) {
            addUserSavedFile(getSystemHashCode(activityRecord), activityRecord.mUserId, recordSnapshotInner);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyAppVisibilityChanged(ActivityRecord activityRecord, boolean z) {
        if (shouldDisableSnapshots() || z) {
            return;
        }
        resetTmpFields();
        addBelowTopActivityIfExist(activityRecord.getTask(), this.mPendingRemoveActivity, "remove-snapshot");
        onCommitTransition();
    }

    private static int getSystemHashCode(ActivityRecord activityRecord) {
        return System.identityHashCode(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleOpenActivityTransition(SnapshotController.TransitionState<ActivityRecord> transitionState) {
        Iterator<ActivityRecord> it = transitionState.getParticipant(false).iterator();
        while (it.hasNext()) {
            ActivityRecord next = it.next();
            this.mPendingCaptureActivity.add(next);
            ActivityRecord activityBelow = next.getTask().getActivityBelow(next);
            if (activityBelow != null) {
                this.mPendingRemoveActivity.add(activityBelow);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleCloseActivityTransition(SnapshotController.TransitionState<ActivityRecord> transitionState) {
        Iterator<ActivityRecord> it = transitionState.getParticipant(true).iterator();
        while (it.hasNext()) {
            ActivityRecord next = it.next();
            this.mPendingDeleteActivity.add(next);
            ActivityRecord activityBelow = next.getTask().getActivityBelow(next);
            if (activityBelow != null) {
                this.mPendingLoadActivity.add(activityBelow);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleCloseTaskTransition(SnapshotController.TransitionState<Task> transitionState) {
        Iterator<Task> it = transitionState.getParticipant(false).iterator();
        while (it.hasNext()) {
            addBelowTopActivityIfExist(it.next(), this.mPendingRemoveActivity, "remove-snapshot");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleOpenTaskTransition(SnapshotController.TransitionState<Task> transitionState) {
        Iterator<Task> it = transitionState.getParticipant(true).iterator();
        while (it.hasNext()) {
            Task next = it.next();
            addBelowTopActivityIfExist(next, this.mPendingLoadActivity, "load-snapshot");
            adjustSavedFileOrder(next);
        }
    }

    private void addBelowTopActivityIfExist(Task task, ArraySet<ActivityRecord> arraySet, String str) {
        ActivityRecord activityBelow;
        ActivityRecord topMostActivity = task.getTopMostActivity();
        if (topMostActivity == null || (activityBelow = task.getActivityBelow(topMostActivity)) == null) {
            return;
        }
        arraySet.add(activityBelow);
    }

    private void adjustSavedFileOrder(Task task) {
        final int i = task.mUserId;
        task.forAllActivities(new Consumer() { // from class: com.android.server.wm.ActivitySnapshotController$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivitySnapshotController.this.lambda$adjustSavedFileOrder$0(i, (ActivityRecord) obj);
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$adjustSavedFileOrder$0(int i, ActivityRecord activityRecord) {
        UserSavedFile userSavedFile = getUserFiles(i).get(getSystemHashCode(activityRecord));
        if (userSavedFile != null) {
            this.mSavedFilesInOrder.remove(userSavedFile);
            this.mSavedFilesInOrder.add(userSavedFile);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public void onAppRemoved(ActivityRecord activityRecord) {
        super.onAppRemoved(activityRecord);
        removeIfUserSavedFileExist(getSystemHashCode(activityRecord), activityRecord.mUserId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public void onAppDied(ActivityRecord activityRecord) {
        super.onAppDied(activityRecord);
        removeIfUserSavedFileExist(getSystemHashCode(activityRecord), activityRecord.mUserId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public ActivityRecord getTopFullscreenActivity(ActivityRecord activityRecord) {
        WindowState findMainWindow = activityRecord.findMainWindow();
        if (findMainWindow == null || !findMainWindow.mAttrs.isFullscreen()) {
            return null;
        }
        return activityRecord;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public ActivityManager.TaskDescription getTaskDescription(ActivityRecord activityRecord) {
        return activityRecord.taskDescription;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public ActivityRecord findAppTokenForSnapshot(ActivityRecord activityRecord) {
        if (activityRecord != null && activityRecord.canCaptureSnapshot()) {
            return activityRecord;
        }
        return null;
    }

    @Override // com.android.server.wm.AbsAppSnapshotController
    protected boolean use16BitFormat() {
        return this.mPersistInfoProvider.use16BitFormat();
    }

    private SparseArray<UserSavedFile> getUserFiles(int i) {
        if (this.mUserSavedFiles.get(i) == null) {
            this.mUserSavedFiles.put(i, new SparseArray<>());
            cleanUpUserFiles(i);
        }
        return this.mUserSavedFiles.get(i);
    }

    private void removeIfUserSavedFileExist(int i, int i2) {
        UserSavedFile userSavedFile = getUserFiles(i2).get(i);
        if (userSavedFile != null) {
            this.mUserSavedFiles.remove(i);
            this.mSavedFilesInOrder.remove(userSavedFile);
            this.mPersister.removeSnap(i, i2);
        }
    }

    private boolean containsFile(int i, int i2) {
        return getUserFiles(i2).get(i) != null;
    }

    private void addUserSavedFile(int i, int i2, TaskSnapshot taskSnapshot) {
        SparseArray<UserSavedFile> userFiles = getUserFiles(i2);
        if (userFiles.get(i) == null) {
            UserSavedFile userSavedFile = new UserSavedFile(i, i2);
            userFiles.put(i, userSavedFile);
            this.mSavedFilesInOrder.add(userSavedFile);
            this.mPersister.persistSnapshot(i, i2, taskSnapshot);
            if (this.mSavedFilesInOrder.size() > 40) {
                purgeSavedFile();
            }
        }
    }

    private void purgeSavedFile() {
        int size = this.mSavedFilesInOrder.size();
        int i = size - 20;
        ArrayList<UserSavedFile> arrayList = new ArrayList<>();
        if (i > 0) {
            int i2 = size - i;
            for (int i3 = size - 1; i3 > i2; i3--) {
                UserSavedFile remove = this.mSavedFilesInOrder.remove(i3);
                if (remove != null) {
                    this.mUserSavedFiles.remove(remove.mFileId);
                    arrayList.add(remove);
                }
            }
        }
        if (arrayList.size() > 0) {
            removeSnapshotFiles(arrayList);
        }
    }

    private void removeSnapshotFiles(final ArrayList<UserSavedFile> arrayList) {
        synchronized (this.mSnapshotPersistQueue.getLock()) {
            this.mSnapshotPersistQueue.sendToQueueLocked(new SnapshotPersistQueue.WriteQueueItem(this.mPersistInfoProvider) { // from class: com.android.server.wm.ActivitySnapshotController.3
                @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
                void write() {
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        UserSavedFile userSavedFile = (UserSavedFile) arrayList.get(size);
                        ActivitySnapshotController.this.mSnapshotPersistQueue.deleteSnapshot(userSavedFile.mFileId, userSavedFile.mUserId, this.mPersistInfoProvider);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class UserSavedFile {
        int mFileId;
        int mUserId;

        UserSavedFile(int i, int i2) {
            this.mFileId = i;
            this.mUserId = i2;
        }
    }
}
