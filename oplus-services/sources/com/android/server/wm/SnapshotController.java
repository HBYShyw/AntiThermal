package com.android.server.wm;

import android.util.ArraySet;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class SnapshotController {
    static final int ACTIVITY_CLOSE = 2;
    static final int ACTIVITY_OPEN = 1;
    private static final boolean DEBUG = false;
    private static final String TAG = "WindowManager";
    static final int TASK_CLOSE = 8;
    static final int TASK_OPEN = 4;
    private int mActivatedType;
    final ActivitySnapshotController mActivitySnapshotController;
    private final SnapshotPersistQueue mSnapshotPersistQueue;
    final TaskSnapshotController mTaskSnapshotController;
    private final ArraySet<Task> mTmpCloseTasks = new ArraySet<>();
    private final ArraySet<Task> mTmpOpenTasks = new ArraySet<>();
    private final SparseArray<TransitionState> mTmpOpenCloseRecord = new SparseArray<>();
    private final ArraySet<Integer> mTmpAnalysisRecord = new ArraySet<>();
    private final SparseArray<ArrayList<Consumer<TransitionState>>> mTransitionStateConsumer = new SparseArray<>();
    private final ActivityOrderCheck mActivityOrderCheck = new ActivityOrderCheck();
    private final ActivityOrderCheck.AnalysisResult mResultHandler = new ActivityOrderCheck.AnalysisResult() { // from class: com.android.server.wm.SnapshotController$$ExternalSyntheticLambda0
        @Override // com.android.server.wm.SnapshotController.ActivityOrderCheck.AnalysisResult
        public final void onCheckResult(int i, ActivityRecord activityRecord, ActivityRecord activityRecord2) {
            SnapshotController.this.lambda$new$0(i, activityRecord, activityRecord2);
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface TransitionStateType {
    }

    private static boolean isTransitionClose(int i) {
        return i == 2 || i == 4;
    }

    private static boolean isTransitionOpen(int i) {
        return i == 1 || i == 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        addTransitionRecord(i, true, activityRecord2);
        addTransitionRecord(i, false, activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class ActivityOrderCheck {
        private ActivityRecord mCloseActivity;
        private int mCloseIndex;
        private ActivityRecord mOpenActivity;
        private int mOpenIndex;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public interface AnalysisResult {
            void onCheckResult(int i, ActivityRecord activityRecord, ActivityRecord activityRecord2);
        }

        private ActivityOrderCheck() {
            this.mOpenIndex = -1;
            this.mCloseIndex = -1;
        }

        private void reset() {
            this.mOpenActivity = null;
            this.mCloseActivity = null;
            this.mOpenIndex = -1;
            this.mCloseIndex = -1;
        }

        private void setTarget(boolean z, ActivityRecord activityRecord, int i) {
            if (z) {
                this.mOpenActivity = activityRecord;
                this.mOpenIndex = i;
            } else {
                this.mCloseActivity = activityRecord;
                this.mCloseIndex = i;
            }
        }

        void analysisOrder(ArraySet<ActivityRecord> arraySet, ArraySet<ActivityRecord> arraySet2, Task task, AnalysisResult analysisResult) {
            int size = arraySet.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                ActivityRecord valueAt = arraySet.valueAt(size);
                if (valueAt.getTask() == task) {
                    setTarget(false, valueAt, task.mChildren.indexOf(valueAt));
                    break;
                }
                size--;
            }
            int size2 = arraySet2.size() - 1;
            while (true) {
                if (size2 < 0) {
                    break;
                }
                ActivityRecord valueAt2 = arraySet2.valueAt(size2);
                if (valueAt2.getTask() == task) {
                    setTarget(true, valueAt2, task.mChildren.indexOf(valueAt2));
                    break;
                }
                size2--;
            }
            int i = this.mOpenIndex;
            int i2 = this.mCloseIndex;
            if (i > i2 && i2 != -1) {
                analysisResult.onCheckResult(1, this.mCloseActivity, this.mOpenActivity);
            } else if (i < i2 && i != -1) {
                analysisResult.onCheckResult(2, this.mCloseActivity, this.mOpenActivity);
            }
            reset();
        }
    }

    private void addTransitionRecord(int i, boolean z, WindowContainer windowContainer) {
        TransitionState transitionState = this.mTmpOpenCloseRecord.get(i);
        if (transitionState == null) {
            transitionState = new TransitionState();
            this.mTmpOpenCloseRecord.set(i, transitionState);
        }
        transitionState.addParticipant(windowContainer, z);
        this.mTmpAnalysisRecord.add(Integer.valueOf(i));
    }

    private void clearRecord() {
        this.mTmpOpenCloseRecord.clear();
        this.mTmpAnalysisRecord.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class TransitionState<TYPE extends WindowContainer> {
        private final ArraySet<TYPE> mOpenParticipant = new ArraySet<>();
        private final ArraySet<TYPE> mCloseParticipant = new ArraySet<>();

        TransitionState() {
        }

        void addParticipant(TYPE type, boolean z) {
            (z ? this.mOpenParticipant : this.mCloseParticipant).add(type);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ArraySet<TYPE> getParticipant(boolean z) {
            return z ? this.mOpenParticipant : this.mCloseParticipant;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SnapshotController(WindowManagerService windowManagerService) {
        SnapshotPersistQueue snapshotPersistQueue = new SnapshotPersistQueue();
        this.mSnapshotPersistQueue = snapshotPersistQueue;
        this.mTaskSnapshotController = new TaskSnapshotController(windowManagerService, snapshotPersistQueue);
        this.mActivitySnapshotController = new ActivitySnapshotController(windowManagerService, snapshotPersistQueue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerTransitionStateConsumer(int i, Consumer<TransitionState> consumer) {
        ArrayList<Consumer<TransitionState>> arrayList = this.mTransitionStateConsumer.get(i);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.mTransitionStateConsumer.set(i, arrayList);
        }
        if (!arrayList.contains(consumer)) {
            arrayList.add(consumer);
        }
        this.mActivatedType = i | this.mActivatedType;
    }

    void unregisterTransitionStateConsumer(int i, Consumer<TransitionState> consumer) {
        ArrayList<Consumer<TransitionState>> arrayList = this.mTransitionStateConsumer.get(i);
        if (arrayList == null) {
            return;
        }
        arrayList.remove(consumer);
        if (arrayList.size() == 0) {
            this.mActivatedType = (~i) & this.mActivatedType;
        }
    }

    private boolean hasTransitionStateConsumer(int i) {
        return (this.mActivatedType & i) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void systemReady() {
        this.mSnapshotPersistQueue.systemReady();
        this.mTaskSnapshotController.systemReady();
        this.mActivitySnapshotController.systemReady();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPause(boolean z) {
        this.mSnapshotPersistQueue.setPaused(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAppRemoved(ActivityRecord activityRecord) {
        this.mTaskSnapshotController.onAppRemoved(activityRecord);
        this.mActivitySnapshotController.onAppRemoved(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAppDied(ActivityRecord activityRecord) {
        this.mTaskSnapshotController.onAppDied(activityRecord);
        this.mActivitySnapshotController.onAppDied(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyAppVisibilityChanged(ActivityRecord activityRecord, boolean z) {
        Task task;
        if (z || !hasTransitionStateConsumer(8) || (task = activityRecord.getTask()) == null || task.isVisibleRequested()) {
            return;
        }
        addTransitionRecord(8, false, task);
        this.mActivitySnapshotController.preTransitionStart();
        notifyTransition(8);
        this.mActivitySnapshotController.postTransitionStart();
        clearRecord();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTransitionStarting(DisplayContent displayContent) {
        handleAppTransition(displayContent.mClosingApps, displayContent.mOpeningApps);
    }

    void onTransitionReady(int i, ArraySet<WindowContainer> arraySet) {
        boolean isTransitionOpen = isTransitionOpen(i);
        boolean isTransitionClose = isTransitionClose(i);
        if ((isTransitionOpen || isTransitionClose || i >= 1000) && this.mActivatedType != 0) {
            ArraySet<ActivityRecord> arraySet2 = new ArraySet<>();
            ArraySet<ActivityRecord> arraySet3 = new ArraySet<>();
            for (int size = arraySet.size() - 1; size >= 0; size--) {
                ActivityRecord asActivityRecord = arraySet.valueAt(size).asActivityRecord();
                if (asActivityRecord != null && asActivityRecord.getTask() != null) {
                    if (asActivityRecord.isVisibleRequested()) {
                        arraySet2.add(asActivityRecord);
                    } else {
                        arraySet3.add(asActivityRecord);
                    }
                }
            }
            handleAppTransition(arraySet3, arraySet2);
        }
    }

    @VisibleForTesting
    void handleAppTransition(ArraySet<ActivityRecord> arraySet, ArraySet<ActivityRecord> arraySet2) {
        if (this.mActivatedType == 0) {
            return;
        }
        analysisTransition(arraySet, arraySet2);
        this.mActivitySnapshotController.preTransitionStart();
        Iterator<Integer> it = this.mTmpAnalysisRecord.iterator();
        while (it.hasNext()) {
            notifyTransition(it.next().intValue());
        }
        this.mActivitySnapshotController.postTransitionStart();
        clearRecord();
    }

    private void notifyTransition(int i) {
        TransitionState transitionState = this.mTmpOpenCloseRecord.get(i);
        Iterator<Consumer<TransitionState>> it = this.mTransitionStateConsumer.get(i).iterator();
        while (it.hasNext()) {
            it.next().accept(transitionState);
        }
    }

    private void analysisTransition(ArraySet<ActivityRecord> arraySet, ArraySet<ActivityRecord> arraySet2) {
        getParticipantTasks(arraySet, this.mTmpCloseTasks, false);
        getParticipantTasks(arraySet2, this.mTmpOpenTasks, true);
        for (int size = this.mTmpCloseTasks.size() - 1; size >= 0; size--) {
            Task valueAt = this.mTmpCloseTasks.valueAt(size);
            if (this.mTmpOpenTasks.contains(valueAt)) {
                if (hasTransitionStateConsumer(1) || hasTransitionStateConsumer(2)) {
                    this.mActivityOrderCheck.analysisOrder(arraySet, arraySet2, valueAt, this.mResultHandler);
                }
            } else if (hasTransitionStateConsumer(8)) {
                addTransitionRecord(8, false, valueAt);
            }
        }
        if (hasTransitionStateConsumer(4)) {
            for (int size2 = this.mTmpOpenTasks.size() - 1; size2 >= 0; size2--) {
                WindowContainer windowContainer = (Task) this.mTmpOpenTasks.valueAt(size2);
                if (!this.mTmpCloseTasks.contains(windowContainer)) {
                    addTransitionRecord(4, true, windowContainer);
                }
            }
        }
        this.mTmpCloseTasks.clear();
        this.mTmpOpenTasks.clear();
    }

    private void getParticipantTasks(ArraySet<ActivityRecord> arraySet, ArraySet<Task> arraySet2, boolean z) {
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            ActivityRecord valueAt = arraySet.valueAt(size);
            Task task = valueAt.getTask();
            if (task != null && z == valueAt.isVisibleRequested()) {
                arraySet2.add(task);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        this.mTaskSnapshotController.dump(printWriter, str);
        this.mActivitySnapshotController.dump(printWriter, str);
    }
}
