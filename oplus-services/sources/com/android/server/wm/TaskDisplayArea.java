package com.android.server.wm;

import android.app.ActivityOptions;
import android.app.WindowConfiguration;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.IntArray;
import android.util.Slog;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.internal.util.function.pooled.PooledPredicate;
import com.android.server.wm.ActivityRecord;
import com.android.server.wm.DisplayArea;
import com.android.server.wm.LaunchParamsController;
import com.android.server.wm.RemoteAnimationController;
import com.android.server.wm.Task;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;
import vendor.oplus.hardware.charger.ChargerErrorCode;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class TaskDisplayArea extends DisplayArea<WindowContainer> {
    private ActivityTaskManagerService mAtmService;
    private int mBackgroundColor;
    private final boolean mCanHostHomeTask;
    private int mColorLayerCounter;
    final boolean mCreatedByOrganizer;
    DisplayContent mDisplayContent;
    Task mLastFocusedRootTask;
    private int mLastLeafTaskToFrontId;

    @VisibleForTesting
    Task mLaunchAdjacentFlagRootTask;
    private final ArrayList<LaunchRootTaskDef> mLaunchRootTasks;

    @VisibleForTesting
    Task mPreferredTopFocusableRootTask;
    private boolean mRemoved;
    private Task mRootHomeTask;
    private Task mRootPinnedTask;
    private ArrayList<OnRootTaskOrderChangedListener> mRootTaskOrderChangedCallbacks;
    private RootWindowContainer mRootWindowContainer;
    ITaskDisplayAreaExt mTaskDisplayAreaExt;
    private final Configuration mTempConfiguration;
    private final ArrayList<WindowContainer> mTmpAlwaysOnTopChildren;
    private final ArrayList<WindowContainer> mTmpHomeChildren;
    private final IntArray mTmpNeedsZBoostIndexes;
    private final ArrayList<WindowContainer> mTmpNormalChildren;
    private ArrayList<Task> mTmpTasks;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface OnRootTaskOrderChangedListener {
        void onRootTaskOrderChanged(Task task);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isWindowingModeSupported(int i, boolean z, boolean z2, boolean z3) {
        if (i != 0 && i != 1) {
            if (!z) {
                return false;
            }
            if (i == 6) {
                return true;
            }
            if (!z2 && i == 5) {
                return false;
            }
            if (!z3 && i == 2) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getTopRootTask$1(Task task) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public TaskDisplayArea asTaskDisplayArea() {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public TaskDisplayArea getTaskDisplayArea() {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean isOnTop() {
        return true;
    }

    @Override // com.android.server.wm.DisplayArea
    boolean isTaskDisplayArea() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class LaunchRootTaskDef {
        int[] activityTypes;
        Task task;
        int[] windowingModes;

        private LaunchRootTaskDef() {
        }

        boolean contains(int i, int i2) {
            return ArrayUtils.contains(this.windowingModes, i) && ArrayUtils.contains(this.activityTypes, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskDisplayArea(DisplayContent displayContent, WindowManagerService windowManagerService, String str, int i) {
        this(displayContent, windowManagerService, str, i, false, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskDisplayArea(DisplayContent displayContent, WindowManagerService windowManagerService, String str, int i, boolean z) {
        this(displayContent, windowManagerService, str, i, z, true);
    }

    TaskDisplayArea(DisplayContent displayContent, WindowManagerService windowManagerService, String str, int i, boolean z, boolean z2) {
        super(windowManagerService, DisplayArea.Type.ANY, str, i);
        this.mBackgroundColor = 0;
        this.mColorLayerCounter = 0;
        this.mTmpAlwaysOnTopChildren = new ArrayList<>();
        this.mTmpNormalChildren = new ArrayList<>();
        this.mTmpHomeChildren = new ArrayList<>();
        this.mTmpNeedsZBoostIndexes = new IntArray();
        this.mTmpTasks = new ArrayList<>();
        this.mLaunchRootTasks = new ArrayList<>();
        this.mRootTaskOrderChangedCallbacks = new ArrayList<>();
        this.mTempConfiguration = new Configuration();
        this.mDisplayContent = displayContent;
        this.mRootWindowContainer = windowManagerService.mRoot;
        this.mAtmService = windowManagerService.mAtmService;
        this.mCreatedByOrganizer = z;
        this.mCanHostHomeTask = z2;
        this.mTaskDisplayAreaExt = (ITaskDisplayAreaExt) ExtLoader.type(ITaskDisplayAreaExt.class).base(this).create();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getRootTask(final int i, final int i2) {
        if (i2 == 2) {
            return this.mRootHomeTask;
        }
        if (i == 2) {
            return this.mRootPinnedTask;
        }
        return getRootTask(new Predicate() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getRootTask$0;
                lambda$getRootTask$0 = TaskDisplayArea.lambda$getRootTask$0(i2, i, (Task) obj);
                return lambda$getRootTask$0;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getRootTask$0(int i, int i2, Task task) {
        if (i == 0 && i2 == task.getWindowingMode()) {
            return true;
        }
        return task.isCompatible(i2, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public Task getTopRootTask() {
        return getRootTask(new Predicate() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getTopRootTask$1;
                lambda$getTopRootTask$1 = TaskDisplayArea.lambda$getTopRootTask$1((Task) obj);
                return lambda$getTopRootTask$1;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getRootHomeTask() {
        return this.mRootHomeTask;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getRootPinnedTask() {
        return this.mRootPinnedTask;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<Task> getVisibleTasks() {
        final ArrayList<Task> arrayList = new ArrayList<>();
        forAllTasks(new Consumer() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TaskDisplayArea.lambda$getVisibleTasks$2(arrayList, (Task) obj);
            }
        });
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getVisibleTasks$2(ArrayList arrayList, Task task) {
        if (task.isLeafTask() && task.isVisible()) {
            arrayList.add(task);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onRootTaskWindowingModeChanged(Task task) {
        removeRootTaskReferenceIfNeeded(task);
        addRootTaskReferenceIfNeeded(task);
        if (task != this.mRootPinnedTask || getTopRootTask() == task) {
            return;
        }
        positionChildAt(Integer.MAX_VALUE, task, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addRootTaskReferenceIfNeeded(Task task) {
        if (task.isActivityTypeHome()) {
            Task task2 = this.mRootHomeTask;
            if (task2 != null) {
                if (!task.isDescendantOf(task2)) {
                    throw new IllegalArgumentException("addRootTaskReferenceIfNeeded: root home task=" + this.mRootHomeTask + " already exist on display=" + this + " rootTask=" + task);
                }
            } else {
                this.mRootHomeTask = task;
            }
        } else if (this.mTaskDisplayAreaExt.isMultiSearchActivityType(task.getActivityType())) {
            this.mTaskDisplayAreaExt.setMultiSearchTask(task);
        }
        if (task.isRootTask() && task.getWindowingMode() == 2) {
            if (this.mRootPinnedTask != null) {
                throw new IllegalArgumentException("addRootTaskReferenceIfNeeded: root pinned task=" + this.mRootPinnedTask + " already exist on display=" + this + " rootTask=" + task);
            }
            this.mRootPinnedTask = task;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeRootTaskReferenceIfNeeded(Task task) {
        if (task == this.mRootHomeTask) {
            this.mRootHomeTask = null;
        } else if (task == this.mRootPinnedTask) {
            this.mRootPinnedTask = null;
        } else if (this.mTaskDisplayAreaExt.isMultiSearchTask(task)) {
            this.mTaskDisplayAreaExt.setMultiSearchTask(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void setInitialSurfaceControlProperties(SurfaceControl.Builder builder) {
        builder.setEffectLayer();
        super.setInitialSurfaceControlProperties(builder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void addChild(WindowContainer windowContainer, int i) {
        if (windowContainer.asTaskDisplayArea() != null) {
            if (WindowManagerDebugConfig.DEBUG_ROOT_TASK) {
                Slog.d("WindowManager", "Set TaskDisplayArea=" + windowContainer + " on taskDisplayArea=" + this);
            }
            super.addChild((TaskDisplayArea) windowContainer, i);
            return;
        }
        if (windowContainer.asTask() != null) {
            addChildTask(windowContainer.asTask(), i);
            return;
        }
        throw new IllegalArgumentException("TaskDisplayArea can only add Task and TaskDisplayArea, but found " + windowContainer);
    }

    private void addChildTask(Task task, int i) {
        if (WindowManagerDebugConfig.DEBUG_ROOT_TASK) {
            Slog.d("WindowManager", "Set task=" + task + " on taskDisplayArea=" + this);
        }
        addRootTaskReferenceIfNeeded(task);
        super.addChild((TaskDisplayArea) task, findPositionForRootTask(i, task, true));
        if (this.mPreferredTopFocusableRootTask != null && task.isFocusable() && this.mPreferredTopFocusableRootTask.compareTo((WindowContainer) task) < 0) {
            this.mPreferredTopFocusableRootTask = null;
        }
        this.mAtmService.mTaskSupervisor.updateTopResumedActivityIfNeeded("addChildTask");
        this.mAtmService.updateSleepIfNeededLocked();
        onRootTaskOrderChanged(task);
        ((IMirageWindowManagerExt) ExtLoader.type(IMirageWindowManagerExt.class).create()).onTaskAdded(getDisplayId(), task);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.WindowContainer
    public void removeChild(WindowContainer windowContainer) {
        if (windowContainer.asTaskDisplayArea() != null) {
            super.removeChild(windowContainer);
        } else {
            if (windowContainer.asTask() != null) {
                removeChildTask(windowContainer.asTask());
                return;
            }
            throw new IllegalArgumentException("TaskDisplayArea can only remove Task and TaskDisplayArea, but found " + windowContainer);
        }
    }

    private void removeChildTask(Task task) {
        ((IMirageWindowManagerExt) ExtLoader.type(IMirageWindowManagerExt.class).create()).onTaskRemoved(getDisplayId(), task);
        super.removeChild(task);
        onRootTaskRemoved(task);
        this.mAtmService.updateSleepIfNeededLocked();
        removeRootTaskReferenceIfNeeded(task);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    public void positionChildAt(int i, WindowContainer windowContainer, boolean z) {
        if (windowContainer.asTaskDisplayArea() != null) {
            super.positionChildAt(i, windowContainer, z);
        } else {
            if (windowContainer.asTask() != null) {
                positionChildTaskAt(i, windowContainer.asTask(), z);
                return;
            }
            throw new IllegalArgumentException("TaskDisplayArea can only position Task and TaskDisplayArea, but found " + windowContainer);
        }
    }

    private void positionChildTaskAt(int i, Task task, boolean z) {
        boolean z2 = i >= getChildCount() - 1;
        boolean z3 = i <= 0;
        int indexOf = this.mChildren.indexOf(task);
        if (task.isAlwaysOnTop() && !z2) {
            Slog.w("WindowManager", "Ignoring move of always-on-top root task=" + this + " to bottom");
            super.positionChildAt(indexOf, task, false);
            return;
        }
        if ((!this.mDisplayContent.isTrusted() || this.mDisplayContent.mDontMoveToTop) && !getParent().isOnTop()) {
            z = false;
        }
        int findPositionForRootTask = findPositionForRootTask(i, task, false);
        super.positionChildAt(findPositionForRootTask, task, false);
        if (z && getParent() != null && (z2 || z3)) {
            getParent().positionChildAt(z2 ? Integer.MAX_VALUE : ChargerErrorCode.ERR_FILE_FAILURE_ACCESS, this, true);
        }
        task.updateTaskMovement(z2, z3, findPositionForRootTask);
        if (z2 && task.isTopActivityFocusable()) {
            this.mPreferredTopFocusableRootTask = task.shouldBeVisible(null) ? task : null;
        } else if (this.mPreferredTopFocusableRootTask == task) {
            this.mPreferredTopFocusableRootTask = null;
        }
        this.mAtmService.mTaskSupervisor.updateTopResumedActivityIfNeeded("positionChildTaskAt");
        if (this.mChildren.indexOf(task) != indexOf) {
            onRootTaskOrderChanged(task);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLeafTaskRemoved(int i) {
        if (this.mLastLeafTaskToFrontId == i) {
            this.mLastLeafTaskToFrontId = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLeafTaskMoved(Task task, boolean z, boolean z2) {
        if (z2) {
            this.mAtmService.getTaskChangeNotificationController().notifyTaskMovedToBack(task.getTaskInfo());
        }
        if (!z) {
            if (task.mTaskId == this.mLastLeafTaskToFrontId) {
                this.mLastLeafTaskToFrontId = -1;
                ActivityRecord topMostActivity = getTopMostActivity();
                if (topMostActivity != null) {
                    this.mAtmService.getTaskChangeNotificationController().notifyTaskMovedToFront(this.mTaskDisplayAreaExt.replaceByMultiSearchIfNeed(topMostActivity.getTask()).getTaskInfo());
                    return;
                }
                return;
            }
            return;
        }
        if (task.mTaskId == this.mLastLeafTaskToFrontId || task.topRunningActivityLocked() == null) {
            return;
        }
        int i = task.mTaskId;
        this.mLastLeafTaskToFrontId = i;
        EventLogTags.writeWmTaskToFront(task.mUserId, i, getDisplayId());
        if (this.mTaskDisplayAreaExt.isActivityPreloadDisplay(this.mDisplayContent)) {
            if (ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
                Slog.v(ActivityTaskManagerService.TAG_ROOT_TASK, "Tasks move to front in actpreload display( " + this.mDisplayContent.mDisplayId + ") do not notify");
                return;
            }
            return;
        }
        this.mAtmService.getTaskChangeNotificationController().notifyTaskMovedToFront(this.mTaskDisplayAreaExt.replaceByMultiSearchIfNeed(task).getTaskInfo());
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    void onChildPositionChanged(WindowContainer windowContainer) {
        super.onChildPositionChanged(windowContainer);
        this.mRootWindowContainer.invalidateTaskLayers();
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    boolean forAllTaskDisplayAreas(Predicate<TaskDisplayArea> predicate, boolean z) {
        return z ? super.forAllTaskDisplayAreas(predicate, z) || predicate.test(this) : predicate.test(this) || super.forAllTaskDisplayAreas(predicate, z);
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    void forAllTaskDisplayAreas(Consumer<TaskDisplayArea> consumer, boolean z) {
        if (z) {
            super.forAllTaskDisplayAreas(consumer, z);
            consumer.accept(this);
        } else {
            consumer.accept(this);
            super.forAllTaskDisplayAreas(consumer, z);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    <R> R reduceOnAllTaskDisplayAreas(BiFunction<TaskDisplayArea, R, R> biFunction, R r, boolean z) {
        if (z) {
            return (R) biFunction.apply(this, super.reduceOnAllTaskDisplayAreas(biFunction, r, z));
        }
        return (R) super.reduceOnAllTaskDisplayAreas(biFunction, biFunction.apply(this, r), z);
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    <R> R getItemFromTaskDisplayAreas(Function<TaskDisplayArea, R> function, boolean z) {
        if (z) {
            R r = (R) super.getItemFromTaskDisplayAreas(function, z);
            return r != null ? r : function.apply(this);
        }
        R apply = function.apply(this);
        return apply != null ? apply : (R) super.getItemFromTaskDisplayAreas(function, z);
    }

    private int getPriority(WindowContainer windowContainer) {
        TaskDisplayArea asTaskDisplayArea = windowContainer.asTaskDisplayArea();
        if (asTaskDisplayArea != null) {
            return asTaskDisplayArea.getPriority(asTaskDisplayArea.getTopChild());
        }
        Task asTask = windowContainer.asTask();
        if (this.mWmService.mAssistantOnTopOfDream && asTask.isActivityTypeAssistant()) {
            return 4;
        }
        if (asTask.isActivityTypeDream()) {
            return 3;
        }
        if (asTask.inPinnedWindowingMode()) {
            return 2;
        }
        return asTask.isAlwaysOnTop() ? 1 : 0;
    }

    private int findMinPositionForRootTask(Task task) {
        int i;
        int indexOf;
        int i2 = ChargerErrorCode.ERR_FILE_FAILURE_ACCESS;
        int i3 = 0;
        while (true) {
            int i4 = i3;
            i = i2;
            i2 = i4;
            if (i2 >= this.mChildren.size() || getPriority((WindowContainer) this.mChildren.get(i2)) >= getPriority(task)) {
                break;
            }
            i3 = i2 + 1;
        }
        return (!task.isAlwaysOnTop() || (indexOf = this.mChildren.indexOf(task)) <= i) ? i : indexOf;
    }

    private int findMaxPositionForRootTask(Task task) {
        int size = this.mChildren.size() - 1;
        while (true) {
            if (size < 0) {
                return 0;
            }
            WindowContainer windowContainer = (WindowContainer) this.mChildren.get(size);
            boolean z = windowContainer == task;
            if (getPriority(windowContainer) > getPriority(task) || z || (task.isAlwaysOnTop() && this.mTaskDisplayAreaExt.isFlexibleTaskPriorityLower(windowContainer, task))) {
                size--;
            }
        }
        return size;
    }

    private int findPositionForRootTask(int i, Task task, boolean z) {
        int adjustMaxPositionForSplitRootTask = this.mTaskDisplayAreaExt.adjustMaxPositionForSplitRootTask(task, findMaxPositionForRootTask(task));
        int findMinPositionForRootTask = findMinPositionForRootTask(task);
        if (i == Integer.MAX_VALUE) {
            i = this.mChildren.size();
        } else if (i == Integer.MIN_VALUE) {
            i = 0;
        }
        int max = Math.max(Math.min(i, adjustMaxPositionForSplitRootTask), findMinPositionForRootTask);
        return max != i ? (z || max < this.mChildren.indexOf(task)) ? max + 1 : max : max;
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    int getOrientation(final int i) {
        int orientation = super.getOrientation(i);
        if (!canSpecifyOrientation(orientation)) {
            this.mLastOrientationSource = null;
            return ((Integer) reduceOnAllTaskDisplayAreas(new BiFunction() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda6
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    Integer lambda$getOrientation$3;
                    lambda$getOrientation$3 = TaskDisplayArea.this.lambda$getOrientation$3(i, (TaskDisplayArea) obj, (Integer) obj2);
                    return lambda$getOrientation$3;
                }
            }, -2)).intValue();
        }
        if (this.mTaskDisplayAreaExt.shouldIgnoreRotationForSplitMini()) {
            Slog.d("WindowManager", "IsSplitScreenMini, getOrientation UNSPECIFIED.");
            return -1;
        }
        if (orientation != -2 && orientation != 3) {
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 1381227466, 5, (String) null, new Object[]{Long.valueOf(orientation), Long.valueOf(this.mDisplayContent.mDisplayId)});
            }
            return orientation;
        }
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 1640436199, 5, (String) null, new Object[]{Long.valueOf(this.mDisplayContent.getLastOrientation()), Long.valueOf(this.mDisplayContent.mDisplayId)});
        }
        return this.mDisplayContent.getLastOrientation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$getOrientation$3(int i, TaskDisplayArea taskDisplayArea, Integer num) {
        return (taskDisplayArea == this || num.intValue() != -2) ? num : Integer.valueOf(taskDisplayArea.getOrientation(i));
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    void assignChildLayers(SurfaceControl.Transaction transaction) {
        assignRootTaskOrdering(transaction);
        for (int i = 0; i < this.mChildren.size(); i++) {
            ((WindowContainer) this.mChildren.get(i)).assignChildLayers(transaction);
        }
    }

    void assignRootTaskOrdering(SurfaceControl.Transaction transaction) {
        if (getParent() == null) {
            return;
        }
        this.mTmpAlwaysOnTopChildren.clear();
        this.mTmpHomeChildren.clear();
        this.mTmpNormalChildren.clear();
        this.mTaskDisplayAreaExt.clearZoomChildren();
        for (int i = 0; i < this.mChildren.size(); i++) {
            WindowContainer windowContainer = (WindowContainer) this.mChildren.get(i);
            TaskDisplayArea asTaskDisplayArea = windowContainer.asTaskDisplayArea();
            if (asTaskDisplayArea != null) {
                Task topRootTask = asTaskDisplayArea.getTopRootTask();
                if (topRootTask == null) {
                    this.mTmpNormalChildren.add(asTaskDisplayArea);
                } else if (topRootTask.isAlwaysOnTop()) {
                    this.mTmpAlwaysOnTopChildren.add(asTaskDisplayArea);
                } else if (topRootTask.isActivityTypeHome()) {
                    this.mTmpHomeChildren.add(asTaskDisplayArea);
                } else {
                    this.mTmpNormalChildren.add(asTaskDisplayArea);
                }
            } else {
                Task asTask = windowContainer.asTask();
                if (asTask.isAlwaysOnTop()) {
                    this.mTmpAlwaysOnTopChildren.add(asTask);
                    if (this.mTaskDisplayAreaExt.isZoomMode(asTask.getWindowingMode())) {
                        this.mTaskDisplayAreaExt.addZoomChildren(asTask);
                    }
                } else if (asTask.isActivityTypeHome()) {
                    this.mTmpHomeChildren.add(asTask);
                } else if (this.mTaskDisplayAreaExt.isZoomMode(asTask.getWindowingMode())) {
                    this.mTaskDisplayAreaExt.addZoomChildren(asTask);
                } else {
                    this.mTmpNormalChildren.add(asTask);
                }
            }
        }
        int adjustRootTaskLayer = adjustRootTaskLayer(transaction, this.mTmpAlwaysOnTopChildren, adjustRootTaskLayer(transaction, this.mTmpNormalChildren, adjustRootTaskLayer(transaction, this.mTmpHomeChildren, 0)));
        ArrayList<WindowContainer> arrayList = new ArrayList<>();
        arrayList.addAll(this.mTaskDisplayAreaExt.getZoomChildren());
        if (hasPinnedTask()) {
            arrayList.add(getRootPinnedTask());
        }
        adjustRootTaskLayer(transaction, arrayList, adjustRootTaskLayer);
    }

    private int adjustRootTaskLayer(SurfaceControl.Transaction transaction, ArrayList<WindowContainer> arrayList, int i) {
        boolean needsZBoost;
        this.mTmpNeedsZBoostIndexes.clear();
        int size = arrayList.size();
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i3 >= size) {
                break;
            }
            if (i3 >= size) {
                Slog.e("WindowManager", "childCount OutOfBoundsException break");
                break;
            }
            WindowContainer windowContainer = arrayList.get(i3);
            TaskDisplayArea asTaskDisplayArea = windowContainer.asTaskDisplayArea();
            if (asTaskDisplayArea != null) {
                needsZBoost = asTaskDisplayArea.childrenNeedZBoost();
            } else {
                needsZBoost = windowContainer.needsZBoost();
            }
            if (needsZBoost) {
                this.mTmpNeedsZBoostIndexes.add(i3);
            } else {
                windowContainer.assignLayer(transaction, i);
                i++;
            }
            i3++;
        }
        int size2 = this.mTmpNeedsZBoostIndexes.size();
        while (i2 < size2) {
            arrayList.get(this.mTmpNeedsZBoostIndexes.get(i2)).assignLayer(transaction, i);
            i2++;
            i++;
        }
        return i;
    }

    private boolean childrenNeedZBoost() {
        final boolean[] zArr = new boolean[1];
        forAllRootTasks(new Consumer() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TaskDisplayArea.lambda$childrenNeedZBoost$4(zArr, (Task) obj);
            }
        });
        return zArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$childrenNeedZBoost$4(boolean[] zArr, Task task) {
        zArr[0] = task.needsZBoost() | zArr[0];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public RemoteAnimationTarget createRemoteAnimationTarget(RemoteAnimationController.RemoteAnimationRecord remoteAnimationRecord) {
        ActivityRecord topMostActivity = getTopMostActivity();
        if (topMostActivity != null) {
            return topMostActivity.createRemoteAnimationTarget(remoteAnimationRecord);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBackgroundColor(int i) {
        setBackgroundColor(i, false);
    }

    void setBackgroundColor(int i, boolean z) {
        this.mBackgroundColor = i;
        Color valueOf = Color.valueOf(i);
        if (!z) {
            this.mColorLayerCounter++;
        }
        if (this.mSurfaceControl != null) {
            getPendingTransaction().setColor(this.mSurfaceControl, new float[]{valueOf.red(), valueOf.green(), valueOf.blue()});
            scheduleAnimation();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearBackgroundColor() {
        int i = this.mColorLayerCounter - 1;
        this.mColorLayerCounter = i;
        if (i != 0 || this.mSurfaceControl == null) {
            return;
        }
        getPendingTransaction().unsetColor(this.mSurfaceControl);
        scheduleAnimation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void migrateToNewSurfaceControl(SurfaceControl.Transaction transaction) {
        super.migrateToNewSurfaceControl(transaction);
        if (this.mColorLayerCounter > 0) {
            setBackgroundColor(this.mBackgroundColor, true);
        }
        reassignLayer(transaction);
        scheduleAnimation();
    }

    void onRootTaskRemoved(Task task) {
        if (ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
            Slog.v(ActivityTaskManagerService.TAG_ROOT_TASK, "onRootTaskRemoved: detaching " + task + " from displayId=" + this.mDisplayContent.mDisplayId);
        }
        this.mTaskDisplayAreaExt.onRootTaskRemoved(task);
        if (this.mPreferredTopFocusableRootTask == task) {
            this.mPreferredTopFocusableRootTask = null;
        }
        if (this.mLaunchAdjacentFlagRootTask == task) {
            this.mLaunchAdjacentFlagRootTask = null;
        }
        this.mDisplayContent.releaseSelfIfNeeded();
        onRootTaskOrderChanged(task);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void positionTaskBehindHome(Task task) {
        WindowContainer parent = getOrCreateRootHomeTask().getParent();
        Task asTask = parent != null ? parent.asTask() : null;
        if (asTask == null) {
            if (task.getParent() == this) {
                positionChildAt(ChargerErrorCode.ERR_FILE_FAILURE_ACCESS, task, false);
                return;
            } else {
                task.reparent(this, false);
                return;
            }
        }
        if (asTask == task.getParent()) {
            asTask.positionChildAtBottom(task);
        } else {
            task.reparent(asTask, false, 2, false, false, "positionTaskBehindHome");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getOrCreateRootTask(int i, int i2, boolean z) {
        return getOrCreateRootTask(i, i2, z, null, null, null, 0);
    }

    Task getOrCreateRootTask(int i, int i2, boolean z, Task task, Task task2, ActivityOptions activityOptions, int i3) {
        int windowingMode = i == 0 ? getWindowingMode() : i;
        if (!DisplayContent.alwaysCreateRootTask(windowingMode, i2)) {
            Task rootTask = getRootTask(windowingMode, i2);
            if (rootTask != null) {
                return rootTask;
            }
        } else if (task != null) {
            int i4 = z ? Integer.MAX_VALUE : ChargerErrorCode.ERR_FILE_FAILURE_ACCESS;
            Task launchRootTask = getLaunchRootTask(windowingMode, i2, activityOptions, task2, i3, task);
            if (launchRootTask != null) {
                if (task.getParent() == null) {
                    launchRootTask.addChild(task, i4);
                } else if (task.getParent() != launchRootTask) {
                    task.reparent(launchRootTask, i4);
                }
            } else if (task.getDisplayArea() != this || task.getRootTask().mReparentLeafTaskIfRelaunch) {
                if (task.getParent() == null) {
                    addChild(task, i4);
                } else {
                    task.reparent(this, z);
                }
            }
            if (i != 0 && task.isRootTask() && task.getWindowingMode() != i) {
                task.mTransitionController.collect(task);
                task.setWindowingMode(i);
            }
            return task.getRootTask();
        }
        return new Task.Builder(this.mAtmService).setWindowingMode(i).setActivityType(i2).setOnTop(z).setParent(this).setSourceTask(task2).setActivityOptions(activityOptions).setLaunchFlags(i3).build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getOrCreateRootTask(ActivityRecord activityRecord, ActivityOptions activityOptions, Task task, Task task2, LaunchParamsController.LaunchParams launchParams, int i, int i2, boolean z) {
        int launchWindowingMode;
        if (launchParams != null) {
            launchWindowingMode = launchParams.mWindowingMode;
        } else {
            launchWindowingMode = activityOptions != null ? activityOptions.getLaunchWindowingMode() : 0;
        }
        return getOrCreateRootTask(validateWindowingMode((!this.mTaskDisplayAreaExt.isComactValidWindowingMode(launchWindowingMode) || task == null) ? launchWindowingMode : 0, activityRecord, task), i2, z, task, task2, activityOptions, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public int getNextRootTaskId() {
        return this.mAtmService.mTaskSupervisor.getNextTaskIdForUser();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task createRootTask(int i, int i2, boolean z) {
        return createRootTask(i, i2, z, null);
    }

    Task createRootTask(int i, int i2, boolean z, ActivityOptions activityOptions) {
        return new Task.Builder(this.mAtmService).setWindowingMode(i).setActivityType(i2).setParent(this).setOnTop(z).setActivityOptions(activityOptions).build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLaunchRootTask(Task task, int[] iArr, int[] iArr2) {
        if (!task.mCreatedByOrganizer) {
            throw new IllegalArgumentException("Can't set not mCreatedByOrganizer as launch root tr=" + task);
        }
        LaunchRootTaskDef launchRootTaskDef = getLaunchRootTaskDef(task);
        if (launchRootTaskDef != null) {
            this.mLaunchRootTasks.remove(launchRootTaskDef);
        } else {
            launchRootTaskDef = new LaunchRootTaskDef();
            launchRootTaskDef.task = task;
        }
        launchRootTaskDef.activityTypes = iArr2;
        launchRootTaskDef.windowingModes = iArr;
        if (ArrayUtils.isEmpty(iArr) && ArrayUtils.isEmpty(iArr2)) {
            return;
        }
        this.mLaunchRootTasks.add(launchRootTaskDef);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeLaunchRootTask(Task task) {
        LaunchRootTaskDef launchRootTaskDef = getLaunchRootTaskDef(task);
        if (launchRootTaskDef != null) {
            this.mLaunchRootTasks.remove(launchRootTaskDef);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLaunchAdjacentFlagRootTask(Task task) {
        if (task != null) {
            if (!task.mCreatedByOrganizer) {
                throw new IllegalArgumentException("Can't set not mCreatedByOrganizer as launch adjacent flag root tr=" + task);
            }
            if (task.getAdjacentTaskFragment() == null) {
                throw new UnsupportedOperationException("Can't set non-adjacent root as launch adjacent flag root tr=" + task);
            }
        }
        this.mLaunchAdjacentFlagRootTask = task;
    }

    private LaunchRootTaskDef getLaunchRootTaskDef(Task task) {
        for (int size = this.mLaunchRootTasks.size() - 1; size >= 0; size--) {
            if (this.mLaunchRootTasks.get(size).task.mTaskId == task.mTaskId) {
                return this.mLaunchRootTasks.get(size);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getLaunchRootTask(int i, int i2, ActivityOptions activityOptions, Task task, int i3) {
        return getLaunchRootTask(i, i2, activityOptions, task, i3, null);
    }

    Task getLaunchRootTask(int i, int i2, ActivityOptions activityOptions, Task task, int i3, Task task2) {
        Task adjacentTask;
        Task task3;
        Task task4;
        Task fromWindowContainerToken;
        if (activityOptions != null && (fromWindowContainerToken = Task.fromWindowContainerToken(activityOptions.getLaunchRootTask())) != null && fromWindowContainerToken.mCreatedByOrganizer) {
            return fromWindowContainerToken;
        }
        if ((i3 & 4096) != 0 && (task3 = this.mLaunchAdjacentFlagRootTask) != null && (task == null || task != task2)) {
            if (task != null && task3.getAdjacentTask() != null && (task == (task4 = this.mLaunchAdjacentFlagRootTask) || task.isDescendantOf(task4))) {
                return this.mLaunchAdjacentFlagRootTask.getAdjacentTask();
            }
            return this.mLaunchAdjacentFlagRootTask;
        }
        int size = this.mLaunchRootTasks.size();
        do {
            size--;
            if (size < 0) {
                if (task == null || ((task2 != null && task2.getWindowingMode() == 2) || (adjacentTask = task.getAdjacentTask()) == null)) {
                    return null;
                }
                if (task2 != null && (task2 == adjacentTask || task2.isDescendantOf(adjacentTask))) {
                    Task createdByOrganizerTask = task2.getCreatedByOrganizerTask();
                    return (createdByOrganizerTask == null && task.getWindowingMode() == 6 && this.mTaskDisplayAreaExt.isZoomMode(task2.getWindowingMode())) ? createdByOrganizerTask : adjacentTask;
                }
                return task.getCreatedByOrganizerTask();
            }
        } while (!this.mLaunchRootTasks.get(size).contains(i, i2));
        Task task5 = this.mLaunchRootTasks.get(size).task;
        Task adjacentTask2 = task5 != null ? task5.getAdjacentTask() : null;
        return (task == null || adjacentTask2 == null || !(task == adjacentTask2 || task.isDescendantOf(adjacentTask2))) ? task5 : adjacentTask2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getFocusedRootTask() {
        Task task = this.mPreferredTopFocusableRootTask;
        if (task != null) {
            return task;
        }
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            WindowContainer windowContainer = (WindowContainer) this.mChildren.get(size);
            if (windowContainer.asTaskDisplayArea() != null) {
                Task focusedRootTask = windowContainer.asTaskDisplayArea().getFocusedRootTask();
                if (focusedRootTask != null) {
                    return focusedRootTask;
                }
            } else {
                Task asTask = ((WindowContainer) this.mChildren.get(size)).asTask();
                if (asTask.isFocusableAndVisible()) {
                    return asTask;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getNextFocusableRootTask(Task task, boolean z) {
        if (task != null) {
            task.getWindowingMode();
        }
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            WindowContainer windowContainer = (WindowContainer) this.mChildren.get(size);
            if (windowContainer.asTaskDisplayArea() != null) {
                Task nextFocusableRootTask = windowContainer.asTaskDisplayArea().getNextFocusableRootTask(task, z);
                if (nextFocusableRootTask != null) {
                    return nextFocusableRootTask;
                }
            } else {
                Task asTask = ((WindowContainer) this.mChildren.get(size)).asTask();
                if ((!z || asTask != task) && asTask.isFocusableAndVisible()) {
                    return asTask;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getFocusedActivity() {
        Task focusedRootTask = getFocusedRootTask();
        if (focusedRootTask == null) {
            return null;
        }
        ActivityRecord topResumedActivity = focusedRootTask.getTopResumedActivity();
        if (topResumedActivity != null && topResumedActivity.app != null) {
            return topResumedActivity;
        }
        ActivityRecord topPausingActivity = focusedRootTask.getTopPausingActivity();
        return (topPausingActivity == null || topPausingActivity.app == null) ? focusedRootTask.topRunningActivity(true) : topPausingActivity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getLastFocusedRootTask() {
        return this.mLastFocusedRootTask;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateLastFocusedRootTask(Task task, String str) {
        Task focusedRootTask;
        if (str == null || (focusedRootTask = getFocusedRootTask()) == task) {
            return;
        }
        if (this.mDisplayContent.isSleeping() && focusedRootTask != null) {
            focusedRootTask.clearLastPausedActivity();
        }
        this.mLastFocusedRootTask = task;
        if (focusedRootTask != null && focusedRootTask.getTopActivity(false, false) != null && focusedRootTask.getTopActivity(false, false).app != null && !inFreeformWindowingMode() && !str.contains("finish-top")) {
            this.mTaskDisplayAreaExt.notifySysActivityStackChange(TaskDisplayArea.class, focusedRootTask.getTopActivity(false, false).mActivityComponent);
        }
        int i = this.mRootWindowContainer.mCurrentUser;
        int i2 = this.mDisplayContent.mDisplayId;
        int rootTaskId = focusedRootTask == null ? -1 : focusedRootTask.getRootTaskId();
        Task task2 = this.mLastFocusedRootTask;
        EventLogTags.writeWmFocusedRootTask(i, i2, rootTaskId, task2 != null ? task2.getRootTaskId() : -1, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean allResumedActivitiesComplete() {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            WindowContainer windowContainer = (WindowContainer) this.mChildren.get(size);
            if (windowContainer.asTaskDisplayArea() != null) {
                if (!windowContainer.asTaskDisplayArea().allResumedActivitiesComplete()) {
                    return false;
                }
            } else {
                ActivityRecord topResumedActivity = ((WindowContainer) this.mChildren.get(size)).asTask().getTopResumedActivity();
                if (topResumedActivity != null && !topResumedActivity.isState(ActivityRecord.State.RESUMED)) {
                    return false;
                }
            }
        }
        Task focusedRootTask = getFocusedRootTask();
        if (ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
            Slog.d(ActivityTaskManagerService.TAG_ROOT_TASK, "allResumedActivitiesComplete: currentFocusedRootTask changing from=" + this.mLastFocusedRootTask + " to=" + focusedRootTask);
        }
        this.mTaskDisplayAreaExt.onFocusedTaskChanged(this.mLastFocusedRootTask, focusedRootTask);
        this.mLastFocusedRootTask = focusedRootTask;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean pauseBackTasks(final ActivityRecord activityRecord) {
        final int[] iArr = {0};
        forAllLeafTasks(new Consumer() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TaskDisplayArea.this.lambda$pauseBackTasks$6(activityRecord, iArr, (Task) obj);
            }
        }, true);
        return iArr[0] > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pauseBackTasks$6(final ActivityRecord activityRecord, final int[] iArr, Task task) {
        if (!task.isLeafTaskFragment()) {
            ActivityRecord activityRecord2 = topRunningActivity();
            if (task.getResumedActivity() != null && activityRecord2.getTaskFragment() != task && task.startPausing(false, activityRecord, "pauseBackTasks")) {
                iArr[0] = iArr[0] + 1;
            }
        }
        task.forAllLeafTaskFragments(new Consumer() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TaskDisplayArea.lambda$pauseBackTasks$5(ActivityRecord.this, iArr, (TaskFragment) obj);
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$pauseBackTasks$5(ActivityRecord activityRecord, int[] iArr, TaskFragment taskFragment) {
        if (taskFragment.getResumedActivity() == null || taskFragment.canBeResumed(activityRecord) || !taskFragment.startPausing(false, activityRecord, "pauseBackTasks")) {
            return;
        }
        iArr[0] = iArr[0] + 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int resolveWindowingMode(ActivityRecord activityRecord, ActivityOptions activityOptions, Task task) {
        int launchWindowingMode = activityOptions != null ? activityOptions.getLaunchWindowingMode() : 0;
        if (launchWindowingMode == 0) {
            if (task != null) {
                launchWindowingMode = task.getWindowingMode();
            }
            if (launchWindowingMode == 0 && activityRecord != null) {
                launchWindowingMode = activityRecord.getWindowingMode();
            }
            if (launchWindowingMode == 0) {
                launchWindowingMode = getWindowingMode();
            }
        }
        int validateWindowingMode = validateWindowingMode(launchWindowingMode, activityRecord, task);
        if (validateWindowingMode != 0) {
            return validateWindowingMode;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isValidWindowingMode(int i, ActivityRecord activityRecord, Task task) {
        if (this.mTaskDisplayAreaExt.isValidWindowingMode(i, activityRecord, task)) {
            return i != 0;
        }
        if (this.mTaskDisplayAreaExt.isComactValidWindowingMode(i) || this.mTaskDisplayAreaExt.isBracketValidWindowingMode(i)) {
            return true;
        }
        ActivityTaskManagerService activityTaskManagerService = this.mAtmService;
        boolean z = activityTaskManagerService.mSupportsMultiWindow;
        boolean z2 = activityTaskManagerService.mSupportsFreeformWindowManagement;
        boolean z3 = activityTaskManagerService.mSupportsPictureInPicture;
        if (z) {
            if (task != null) {
                z2 = task.supportsFreeformInDisplayArea(this);
                z = task.supportsMultiWindowInDisplayArea(this) || (i == 2 && z3);
            } else if (activityRecord != null) {
                z2 = activityRecord.supportsFreeformInDisplayArea(this);
                z3 = activityRecord.supportsPictureInPicture();
                z = activityRecord.supportsMultiWindowInDisplayArea(this);
            }
        }
        return i != 0 && isWindowingModeSupported(i, z, z2, z3);
    }

    int validateWindowingMode(int i, ActivityRecord activityRecord, Task task) {
        if (isValidWindowingMode(i, activityRecord, task)) {
            return i;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean supportsNonResizableMultiWindow() {
        ActivityTaskManagerService activityTaskManagerService = this.mAtmService;
        int i = activityTaskManagerService.mSupportsNonResizableMultiWindow;
        if (activityTaskManagerService.mDevEnableNonResizableMultiWindow || i == 1) {
            return true;
        }
        if (i == -1) {
            return false;
        }
        return isLargeEnoughForMultiWindow();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean supportsActivityMinWidthHeightMultiWindow(int i, int i2, ActivityInfo activityInfo) {
        int i3;
        if (activityInfo != null && !activityInfo.shouldCheckMinWidthHeightForMultiWindow()) {
            return true;
        }
        if ((i <= 0 && i2 <= 0) || (i3 = this.mAtmService.mRespectsActivityMinWidthHeightMultiWindow) == -1) {
            return true;
        }
        if (i3 == 0 && isLargeEnoughForMultiWindow()) {
            return true;
        }
        Configuration configuration = getConfiguration();
        return configuration.orientation == 2 ? i <= ((int) ((this.mAtmService.mMinPercentageMultiWindowSupportWidth * ((float) configuration.screenWidthDp)) * this.mDisplayContent.getDisplayMetrics().density)) : i2 <= ((int) ((this.mAtmService.mMinPercentageMultiWindowSupportHeight * ((float) configuration.screenHeightDp)) * this.mDisplayContent.getDisplayMetrics().density));
    }

    private boolean isLargeEnoughForMultiWindow() {
        return getConfiguration().smallestScreenWidthDp >= 600;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTopRootTask(Task task) {
        return task == getTopRootTask();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord topRunningActivity() {
        return topRunningActivity(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord topRunningActivity(boolean z) {
        Task focusedRootTask = getFocusedRootTask();
        ActivityRecord activityRecord = focusedRootTask != null ? focusedRootTask.topRunningActivity() : null;
        if (activityRecord == null) {
            for (int size = this.mChildren.size() - 1; size >= 0; size--) {
                WindowContainer windowContainer = (WindowContainer) this.mChildren.get(size);
                if (windowContainer.asTaskDisplayArea() != null) {
                    activityRecord = windowContainer.asTaskDisplayArea().topRunningActivity(z);
                    if (activityRecord != null) {
                        break;
                    }
                } else {
                    Task asTask = ((WindowContainer) this.mChildren.get(size)).asTask();
                    if (asTask != focusedRootTask && asTask.isTopActivityFocusable() && (activityRecord = asTask.topRunningActivity()) != null) {
                        break;
                    }
                }
            }
        }
        if (activityRecord == null || !z || !this.mRootWindowContainer.mTaskSupervisor.getKeyguardController().isKeyguardLocked(activityRecord.getDisplayId()) || activityRecord.canShowWhenLocked()) {
            return activityRecord;
        }
        return null;
    }

    protected int getRootTaskCount() {
        final int[] iArr = new int[1];
        forAllRootTasks(new Consumer() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TaskDisplayArea.lambda$getRootTaskCount$7(iArr, (Task) obj);
            }
        });
        return iArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getRootTaskCount$7(int[] iArr, Task task) {
        iArr[0] = iArr[0] + 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getOrCreateRootHomeTask() {
        return getOrCreateRootHomeTask(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getOrCreateRootHomeTask(boolean z) {
        Task rootHomeTask = getRootHomeTask();
        return (rootHomeTask == null && canHostHomeTask()) ? createRootTask(0, 2, z) : rootHomeTask;
    }

    Task getTopRootTaskInWindowingMode(int i) {
        return getRootTask(i, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveHomeRootTaskToFront(String str) {
        Task orCreateRootHomeTask = getOrCreateRootHomeTask();
        if (orCreateRootHomeTask != null) {
            orCreateRootHomeTask.moveToFront(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveHomeActivityToTop(String str) {
        ActivityRecord homeActivity = getHomeActivity();
        if (homeActivity == null) {
            moveHomeRootTaskToFront(str);
        } else {
            homeActivity.moveFocusableActivityToTop(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getHomeActivity() {
        return getHomeActivityForUser(this.mRootWindowContainer.mCurrentUser);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getHomeActivityForUser(int i) {
        Task rootHomeTask = getRootHomeTask();
        if (rootHomeTask == null) {
            return null;
        }
        PooledPredicate obtainPredicate = PooledLambda.obtainPredicate(new BiPredicate() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda5
            @Override // java.util.function.BiPredicate
            public final boolean test(Object obj, Object obj2) {
                boolean isHomeActivityForUser;
                isHomeActivityForUser = TaskDisplayArea.isHomeActivityForUser((ActivityRecord) obj, ((Integer) obj2).intValue());
                return isHomeActivityForUser;
            }
        }, PooledLambda.__(ActivityRecord.class), Integer.valueOf(i));
        ActivityRecord activity = rootHomeTask.getActivity(obtainPredicate);
        obtainPredicate.recycle();
        return activity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isHomeActivityForUser(ActivityRecord activityRecord, int i) {
        return activityRecord.isActivityTypeHome() && (i == -1 || activityRecord.mUserId == i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveRootTaskBehindBottomMostVisibleRootTask(Task task) {
        Task asTask;
        if (task.shouldBeVisible(null)) {
            return;
        }
        task.getParent().positionChildAt(ChargerErrorCode.ERR_FILE_FAILURE_ACCESS, task, false);
        boolean isRootTask = task.isRootTask();
        int size = isRootTask ? this.mChildren.size() : task.getParent().getChildCount();
        for (int i = 0; i < size; i++) {
            if (isRootTask) {
                WindowContainer windowContainer = (WindowContainer) this.mChildren.get(i);
                if (windowContainer.asTaskDisplayArea() != null) {
                    asTask = windowContainer.asTaskDisplayArea().getBottomMostVisibleRootTask(task);
                } else {
                    asTask = windowContainer.asTask();
                }
            } else {
                asTask = task.getParent().getChildAt(i).asTask();
            }
            if (asTask != task && asTask != null) {
                int windowingMode = asTask.getWindowingMode();
                boolean z = windowingMode == 1 || this.mTaskDisplayAreaExt.isComactValidWindowingMode(windowingMode);
                if (asTask.shouldBeVisible(null) && z) {
                    task.getParent().positionChildAt(Math.max(0, i - 1), task, false);
                    return;
                }
            }
        }
    }

    private Task getBottomMostVisibleRootTask(Task task) {
        return getRootTask(new Predicate() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getBottomMostVisibleRootTask$8;
                lambda$getBottomMostVisibleRootTask$8 = TaskDisplayArea.lambda$getBottomMostVisibleRootTask$8((Task) obj);
                return lambda$getBottomMostVisibleRootTask$8;
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getBottomMostVisibleRootTask$8(Task task) {
        return task.shouldBeVisible(null) && (task.getWindowingMode() == 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveRootTaskBehindRootTask(Task task, Task task2) {
        WindowContainer parent;
        if (task2 == null || task2 == task || (parent = task.getParent()) == null || parent != task2.getParent()) {
            return;
        }
        int indexOf = parent.mChildren.indexOf(task);
        int indexOf2 = parent.mChildren.indexOf(task2);
        if (indexOf <= indexOf2) {
            indexOf2--;
        }
        parent.positionChildAt(Math.max(0, indexOf2), task, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasPinnedTask() {
        return getRootPinnedTask() != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Task getRootTaskAbove(Task task) {
        int indexOf;
        WindowContainer parent = task.getParent();
        if (parent != null && (indexOf = parent.mChildren.indexOf(task) + 1) < parent.mChildren.size()) {
            return (Task) parent.mChildren.get(indexOf);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRootTaskVisible(int i) {
        Task topRootTaskInWindowingMode = getTopRootTaskInWindowingMode(i);
        return topRootTaskInWindowingMode != null && topRootTaskInWindowingMode.isVisible();
    }

    void removeRootTask(Task task) {
        removeChild(task);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDisplayId() {
        return this.mDisplayContent.getDisplayId();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRemoved() {
        return this.mRemoved;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerRootTaskOrderChangedListener(OnRootTaskOrderChangedListener onRootTaskOrderChangedListener) {
        if (this.mRootTaskOrderChangedCallbacks.contains(onRootTaskOrderChangedListener)) {
            return;
        }
        this.mRootTaskOrderChangedCallbacks.add(onRootTaskOrderChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterRootTaskOrderChangedListener(OnRootTaskOrderChangedListener onRootTaskOrderChangedListener) {
        this.mRootTaskOrderChangedCallbacks.remove(onRootTaskOrderChangedListener);
    }

    void onRootTaskOrderChanged(Task task) {
        for (int size = this.mRootTaskOrderChangedCallbacks.size() - 1; size >= 0; size--) {
            this.mRootTaskOrderChangedCallbacks.get(size).onRootTaskOrderChanged(task);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean canCreateRemoteAnimationTarget() {
        return WindowManagerService.sEnableShellTransitions;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canHostHomeTask() {
        return this.mDisplayContent.supportsSystemDecorations() && this.mCanHostHomeTask;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void ensureActivitiesVisible(final ActivityRecord activityRecord, final int i, final boolean z, final boolean z2) {
        this.mAtmService.mTaskSupervisor.beginActivityVisibilityUpdate();
        try {
            forAllRootTasks(new Consumer() { // from class: com.android.server.wm.TaskDisplayArea$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((Task) obj).ensureActivitiesVisible(ActivityRecord.this, i, z, z2);
                }
            });
        } finally {
            this.mAtmService.mTaskSupervisor.endActivityVisibilityUpdate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task remove() {
        Task task = null;
        this.mPreferredTopFocusableRootTask = null;
        boolean shouldDestroyContentOnRemove = this.mDisplayContent.shouldDestroyContentOnRemove();
        TaskDisplayArea defaultTaskDisplayArea = this.mRootWindowContainer.getDefaultTaskDisplayArea();
        int size = this.mChildren.size();
        int i = 0;
        while (i < size) {
            WindowContainer windowContainer = (WindowContainer) this.mChildren.get(i);
            if (windowContainer.asTaskDisplayArea() != null) {
                task = windowContainer.asTaskDisplayArea().remove();
            } else {
                Task asTask = ((WindowContainer) this.mChildren.get(i)).asTask();
                if (shouldDestroyContentOnRemove || !asTask.isActivityTypeStandardOrUndefined() || asTask.mCreatedByOrganizer) {
                    asTask.remove(false, "removeTaskDisplayArea");
                } else {
                    Task launchRootTask = defaultTaskDisplayArea.getLaunchRootTask(asTask.getWindowingMode(), asTask.getActivityType(), null, null, 0);
                    if (launchRootTask == null) {
                        launchRootTask = defaultTaskDisplayArea;
                    }
                    asTask.reparent(launchRootTask, Integer.MAX_VALUE);
                    asTask.setWindowingMode(0);
                    task = asTask;
                }
                i -= size - this.mChildren.size();
                size = this.mChildren.size();
            }
            i++;
        }
        if (task != null && !task.isRootTask()) {
            task.getRootTask().moveToFront("display-removed");
        }
        this.mRemoved = true;
        return task;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canSpecifyOrientation(int i) {
        return this.mDisplayContent.getOrientationRequestingTaskDisplayArea() == this && !shouldIgnoreOrientationRequest(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearPreferredTopFocusableRootTask() {
        this.mPreferredTopFocusableRootTask = null;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public void setWindowingMode(int i) {
        this.mTempConfiguration.setTo(getRequestedOverrideConfiguration());
        WindowConfiguration windowConfiguration = this.mTempConfiguration.windowConfiguration;
        windowConfiguration.setWindowingMode(i);
        windowConfiguration.setDisplayWindowingMode(i);
        onRequestedOverrideConfigurationChanged(this.mTempConfiguration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.server.wm.WindowContainer] */
    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    public void dump(PrintWriter printWriter, String str, boolean z) {
        printWriter.println(str + "TaskDisplayArea " + getName());
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("  ");
        String sb2 = sb.toString();
        super.dump(printWriter, sb2, z);
        if (this.mPreferredTopFocusableRootTask != null) {
            printWriter.println(sb2 + "mPreferredTopFocusableRootTask=" + this.mPreferredTopFocusableRootTask);
        }
        if (this.mLastFocusedRootTask != null) {
            printWriter.println(sb2 + "mLastFocusedRootTask=" + this.mLastFocusedRootTask);
        }
        String str2 = sb2 + "  ";
        if (this.mLaunchRootTasks.size() > 0) {
            printWriter.println(sb2 + "mLaunchRootTasks:");
            for (int size = this.mLaunchRootTasks.size() + (-1); size >= 0; size += -1) {
                LaunchRootTaskDef launchRootTaskDef = this.mLaunchRootTasks.get(size);
                printWriter.println(str2 + Arrays.toString(launchRootTaskDef.activityTypes) + " " + Arrays.toString(launchRootTaskDef.windowingModes) + "  task=" + launchRootTaskDef.task);
            }
        }
        printWriter.println(sb2 + "Application tokens in top down Z order:");
        for (int childCount = getChildCount() + (-1); childCount >= 0; childCount--) {
            ?? childAt = getChildAt(childCount);
            if (childAt.asTaskDisplayArea() != null) {
                childAt.dump(printWriter, sb2, z);
            } else {
                Task asTask = childAt.asTask();
                printWriter.println(sb2 + "* " + asTask.toFullString());
                asTask.dump(printWriter, str2, z);
            }
        }
    }
}
