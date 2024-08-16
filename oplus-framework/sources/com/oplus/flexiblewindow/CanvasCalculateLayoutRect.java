package com.oplus.flexiblewindow;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.OplusThemeResources;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.OplusViewRootUtil;
import android.view.WindowManager;
import com.oplus.flexiblewindow.CanvasCalculateLayoutRect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class CanvasCalculateLayoutRect {
    private static final int ALIGN_BOTTOM = 8;
    private static final int ALIGN_BOTTOM_LEFT = 12;
    private static final int ALIGN_BOTTOM_RIGHT = 9;
    private static final int ALIGN_LEFT = 4;
    private static final int ALIGN_RIGHT = 1;
    private static final int ALIGN_TOP = 2;
    private static final int ALIGN_TOP_LEFT = 6;
    private static final int ALIGN_TOP_RIGHT = 3;
    private static final float ERROR_FLOAT = 0.01f;
    private static final int FOCUS_END = 2;
    private static final int FOCUS_FRONT = 1;
    private static final int FOCUS_UNSET = 0;
    private static final int HORIZONTAL = 2;
    public static final String IN_VERTICAL_WHITE_LIST = "androidx.flexible.window.white.vertical.list";
    private static final float ONE_FLOAT = 1.0f;
    private static final int OUT_SCREEN_RANGE = 56;
    public static final String PS_APP_USE_MAX = "ps_app_use_max";
    private static final float SIXTEEN_TO_NINE = 1.78f;
    private static final float SORT_SAME_ORIENTATION_SCALE = 2.1f;
    public static final String TASK_FLEXIBLE_CURRENT_RECT = "androidx.flexible.window.flexible.current.rect";
    public static final String TASK_FLEXIBLE_USE_MAX = "androidx.flexible.window.flexible.use.max";
    private static final int THREE = 3;
    private static final int THREE_LAYOUT_BIG_DOWN = 5;
    private static final int THREE_LAYOUT_BIG_LEFT = 6;
    private static final int THREE_LAYOUT_BIG_RIGHT = 7;
    private static final int THREE_LAYOUT_BIG_UP = 4;
    public static final int THREE_LINE_LAYOUT = 3;
    private static final int TWO_SPLIT_PADDING = 8;
    private static final int VERTICAL = 1;
    private static final int WITHIN_SCREEN_RANGE = 56;
    private static final String TAG = CanvasCalculateLayoutRect.class.getSimpleName();
    private static int sLayoutOrientation = 0;
    private static Rect sStableRect = new Rect();
    private static Rect sWindowMetricsBounds = new Rect();
    private static List<Rect> sOldRectList = new ArrayList();
    private static float sDensity = 0.0f;

    public static List<Rect> layoutRectCalculate(Context context, List<Intent> intentList, int focus, boolean globalMode) {
        return layoutRectCalculateWithOrientation(context, intentList, 0, focus, globalMode);
    }

    public static List<Rect> layoutRectCalculate(Context context, List<Intent> intentList, List<Intent> oldIntentList, int index, boolean globalMode) {
        sOldRectList.clear();
        List<Rect> rectList = layoutRectCalculate(context, oldIntentList, index, globalMode);
        for (Rect rect : rectList) {
            Log.d(TAG, "layoutRectCalculate old rect " + rect);
            sOldRectList.add(rect);
        }
        return layoutRectCalculate(context, intentList, index, globalMode);
    }

    public static List<Rect> layoutRectCalculateWithOrientation(Context context, List<Intent> intentList, int layoutOrientation, int focusIndex, boolean globalMode) {
        List<Rect> orderRectList = new ArrayList<>();
        if (context != null && intentList != null) {
            if (intentList.size() <= 3) {
                int focus = focusIndex;
                if (focus >= 0 && focus <= 2) {
                    List<TaskData> taskDataList = getTaskDataList(intentList, layoutOrientation, context);
                    if (taskDataList.size() <= 1) {
                        Log.e(TAG, "task data list is less two");
                        return orderRectList;
                    }
                    if (focus >= taskDataList.size()) {
                        focus = taskDataList.size() - 1;
                    }
                    TaskData focusTaskData = taskDataList.get(focus);
                    int[] orientationCount = new int[2];
                    if ((layoutOrientation != 1 && layoutOrientation != 2) || taskDataList.size() == 3) {
                        sLayoutOrientation = computeLayoutOrientation(taskDataList, 0, orientationCount);
                    } else {
                        sLayoutOrientation = layoutOrientation;
                    }
                    if (taskDataList.size() == 3) {
                        sortTaskDataList(taskDataList, focusTaskData, layoutOrientation, sLayoutOrientation);
                    }
                    int focus2 = getFocusIndexAfterSort(taskDataList, focusTaskData);
                    List<Rect> rectList = getCanvasLayoutRect(context, taskDataList, sLayoutOrientation);
                    List<Rect> scaleRectList = scaleRectForFocusOrPanorama(context, rectList, sLayoutOrientation, focus2, globalMode);
                    Log.d(TAG, "scaleRectList:" + scaleRectList);
                    List<Rect> orderRectList2 = new ArrayList<>();
                    for (Intent intent : intentList) {
                        for (int i = 0; i < taskDataList.size(); i++) {
                            if (intent.equals(taskDataList.get(i).mIntent) && intent.getIntExtra(FlexibleWindowManager.KEY_CANVAS_USER_ID, 0) == taskDataList.get(i).mIntent.getIntExtra(FlexibleWindowManager.KEY_CANVAS_USER_ID, 0)) {
                                orderRectList2.add(scaleRectList.get(i));
                            }
                        }
                    }
                    return orderRectList2;
                }
                Log.e(TAG, "invalid focus index:" + focus);
                return orderRectList;
            }
        }
        Log.e(TAG, "invalid intent list:" + intentList);
        return orderRectList;
    }

    public static List<Rect> layoutReplaceRectCalculate(Context context, List<Intent> intentList, List<Rect> oldRectList, int replaceIndex, int focusIndex, boolean globalMode) {
        if (oldRectList != null && oldRectList.size() == 2 && intentList != null) {
            if (intentList.size() == 2) {
                if (replaceIndex >= 0 && replaceIndex < intentList.size() && focusIndex >= 0) {
                    if (focusIndex < intentList.size()) {
                        if (oldRectList.get(1).left > oldRectList.get(0).right) {
                            sLayoutOrientation = 2;
                        } else {
                            sLayoutOrientation = 1;
                        }
                        int reserve = 0;
                        if (replaceIndex == 0) {
                            reserve = 1;
                        }
                        TaskData replaceTaskData = taskDataGetFromIntent(intentList.get(replaceIndex), -2);
                        if (replaceTaskData != null) {
                            if (replaceTaskData.getResizeMode() == 1 && replaceTaskData != null) {
                                if (sLayoutOrientation == 2 && replaceTaskData.getRect().width() > replaceTaskData.getRect().height() && !ActivityInfo.isFixedOrientationLandscape(replaceTaskData.getOrientation())) {
                                    replaceTaskData = taskDataGetFromIntent(intentList.get(replaceIndex), 1);
                                }
                                if (sLayoutOrientation == 1 && replaceTaskData.getRect().width() < replaceTaskData.getRect().height() && replaceTaskData.getIntent().getBooleanExtra(IN_VERTICAL_WHITE_LIST, false)) {
                                    replaceTaskData = taskDataGetFromIntent(intentList.get(replaceIndex), 0);
                                }
                            }
                            int orientation = 1;
                            if (oldRectList.get(reserve).width() > oldRectList.get(reserve).height()) {
                                orientation = 0;
                            }
                            TaskData reserveTaskData = taskDataGetFromIntent(intentList.get(reserve), orientation);
                            List<TaskData> taskDataList = new ArrayList<>();
                            if (reserve == 0) {
                                taskDataList.add(reserveTaskData);
                                taskDataList.add(replaceTaskData);
                            } else {
                                taskDataList.add(replaceTaskData);
                                taskDataList.add(reserveTaskData);
                            }
                            boolean[] useMax = isResizeTwoSplitHorizontalVertical(taskDataList);
                            if (taskDataList.get(reserve).getResizeMode() == 1) {
                                Rect maxRect = taskDataList.get(reserve).getMaxRect();
                                float diff = ((oldRectList.get(reserve).width() * 1.0f) / oldRectList.get(reserve).height()) - ((maxRect.width() * 1.0f) / maxRect.height());
                                if (Math.abs(diff) <= 0.01d) {
                                    useMax[reserve] = true;
                                }
                            }
                            ArrayList<TaskData> needSetMaxList = new ArrayList<>();
                            needSetMaxList.add(replaceTaskData);
                            setAppUseMaxByStoreData(context, needSetMaxList);
                            float f = context.getResources().getDisplayMetrics().density;
                            sDensity = f;
                            List<Rect> rectList = twoFixedSplitLayout(taskDataList, useMax, sLayoutOrientation, f);
                            List<Rect> scaleRectList = scaleRectForFocusOrPanorama(context, rectList, sLayoutOrientation, focusIndex, globalMode);
                            return scaleRectList;
                        }
                        Log.e(TAG, "replace task data is null");
                        return new ArrayList();
                    }
                }
                Log.e(TAG, "invalid replay index:" + replaceIndex + " focus index:" + focusIndex);
                return new ArrayList();
            }
        }
        Log.e(TAG, "invalid rect list:" + oldRectList + " intent list:" + intentList);
        return new ArrayList();
    }

    public static List<Rect> layoutReplaceRectCalculateThree(Context context, List<Intent> intentList, List<Rect> oldRectList, int threeLayoutOrientation, int replaceIndex, int focusIndex, boolean globalMode) {
        TaskData taskData;
        List<TaskData> taskDataListCopy;
        if (replaceIndex >= 0 && replaceIndex < intentList.size()) {
            if (intentList.size() == oldRectList.size() && focusIndex >= 0) {
                if (focusIndex < intentList.size()) {
                    List<TaskData> taskDataList = new ArrayList<>();
                    for (int i = 0; i < intentList.size(); i++) {
                        int orientation = -2;
                        if (i != replaceIndex && oldRectList.get(i).height() < oldRectList.get(i).width()) {
                            orientation = 0;
                        }
                        taskDataList.add(taskDataGetFromIntent(intentList.get(i), orientation));
                    }
                    TaskData replaceTaskData = taskDataList.get(replaceIndex);
                    List<TaskData> replaceTaskDataList = new ArrayList<>();
                    replaceTaskDataList.add(replaceTaskData);
                    List<TaskData> taskDataListCopy2 = new ArrayList<>(taskDataList);
                    taskDataListCopy2.remove(replaceTaskData);
                    setAppUseMaxByStoreData(context, replaceTaskDataList);
                    setAppUseMaxByStoreData(context, taskDataListCopy2);
                    int[] orientationCount = new int[2];
                    List<TaskData> taskDataListCopy3 = new ArrayList<>(taskDataList);
                    List<TaskData> originTaskDataList = new ArrayList<>(taskDataList);
                    int newLayoutOrientation = computeLayoutOrientation(taskDataListCopy3, threeLayoutOrientation, orientationCount);
                    sortTaskDataList(taskDataListCopy3, replaceTaskData, threeLayoutOrientation, newLayoutOrientation);
                    List<Rect> rectList = getCanvasLayoutRect(context, taskDataListCopy3, newLayoutOrientation);
                    List<Rect> orderRectList = new ArrayList<>();
                    Iterator<TaskData> it = originTaskDataList.iterator();
                    while (it.hasNext()) {
                        TaskData taskData2 = it.next();
                        Iterator<TaskData> it2 = it;
                        int i2 = 0;
                        while (i2 < taskDataListCopy3.size()) {
                            if (!taskData2.getIntent().equals(taskDataListCopy3.get(i2).getIntent())) {
                                taskData = taskData2;
                                taskDataListCopy = taskDataListCopy3;
                            } else {
                                taskData = taskData2;
                                taskDataListCopy = taskDataListCopy3;
                                if (taskData2.getIntent().getIntExtra(FlexibleWindowManager.KEY_CANVAS_USER_ID, 0) == taskDataListCopy3.get(i2).getIntent().getIntExtra(FlexibleWindowManager.KEY_CANVAS_USER_ID, 0)) {
                                    orderRectList.add(rectList.get(i2));
                                }
                            }
                            i2++;
                            taskData2 = taskData;
                            taskDataListCopy3 = taskDataListCopy;
                        }
                        it = it2;
                    }
                    int threeLineFocusSide = calculateThreeLineFocusSide(intentList);
                    List<Rect> temp = scaleRectForFocusOrPanorama(context, orderRectList, newLayoutOrientation, threeLineFocusSide, focusIndex, globalMode);
                    return temp;
                }
            }
        }
        Log.e(TAG, "invalid input when calculate three replace!");
        return new ArrayList();
    }

    private static int calculateThreeLineFocusSide(List<Intent> intentList) {
        if (intentList.size() != 3) {
            return 0;
        }
        Rect first = (Rect) intentList.get(0).getParcelableExtra(TASK_FLEXIBLE_CURRENT_RECT, Rect.class);
        if (first == null) {
            return 1;
        }
        if (first.left < 0 || first.top < 0) {
            return 2;
        }
        return 1;
    }

    private static List<Rect> twoFixedSplitLayout(List<TaskData> taskDataList, boolean[] useMax, int layoutOrientation, float density) {
        List<Rect> rectList = new ArrayList<>();
        for (int i = 0; i < taskDataList.size(); i++) {
            Rect rect = new Rect(taskDataList.get(i).getRect());
            Rect launchBound = new Rect(rect);
            Rect layoutRect = new Rect();
            int left = 0;
            int right = 0;
            int top = 0;
            int bottom = 0;
            if (useMax[i]) {
                rect.set(taskDataList.get(i).getMaxRect());
                launchBound.set(rect);
            }
            if (i == 0) {
                Rect secondRect = new Rect();
                if (useMax[1]) {
                    secondRect.set(taskDataList.get(1).getMaxRect());
                } else {
                    secondRect.set(taskDataList.get(1).getRect());
                }
                if (!useMax[0] && taskDataList.get(i).getResizeMode() == 2) {
                    if (sLayoutOrientation == 2 && !taskDataList.get(0).isVertical()) {
                        bottom = 0 + Math.max(rect.height(), secondRect.height());
                    }
                    if (sLayoutOrientation == 1 && taskDataList.get(0).isVertical()) {
                        right = 0 + Math.max(rect.width(), secondRect.width());
                    }
                }
                if (right == 0) {
                    right = rect.width() + 0;
                }
                if (bottom == 0) {
                    bottom = rect.height() + 0;
                }
            } else if (i == 1) {
                float scale = 1.0f;
                TaskData taskData = taskDataList.get(1);
                boolean fullScreen = taskData.getResizeMode() != 2;
                if (layoutOrientation == 2) {
                    if (rectList.get(0).height() != rect.height() && (fullScreen || taskData.isVertical())) {
                        scale = rectList.get(0).height() / rect.height();
                    }
                    left = rectList.get(0).right + ((int) (density * 8.0f));
                    top = rectList.get(0).top;
                    right = left + ((int) (rect.width() * scale));
                    bottom = rectList.get(0).bottom;
                } else {
                    if (rectList.get(0).width() != rect.width() && (fullScreen || !taskData.isVertical())) {
                        scale = rectList.get(0).width() / rect.width();
                    }
                    left = rectList.get(0).left;
                    top = rectList.get(0).bottom + ((int) (density * 8.0f));
                    right = rectList.get(0).right;
                    bottom = top + ((int) (rect.height() * scale));
                }
            }
            layoutRect.set(left, top, right, bottom);
            rectList.add(layoutRect);
        }
        return rectList;
    }

    private static int getFocusIndexAfterSort(List<TaskData> taskDataList, TaskData focusTaskData) {
        int focusIndex = 0;
        if (focusTaskData != null) {
            for (int i = 0; i < taskDataList.size(); i++) {
                if (taskDataList.get(i) == focusTaskData) {
                    focusIndex = i;
                }
            }
        }
        return focusIndex;
    }

    private static List<TaskData> getTaskDataList(List<Intent> intentList, int layoutOrientation, Context context) {
        if (intentList == null || intentList.size() == 0) {
            Log.e(TAG, "intentList is null");
            return new ArrayList();
        }
        List<TaskData> taskDataList = new ArrayList<>();
        for (int i = 0; i < intentList.size(); i++) {
            Rect rect = (Rect) intentList.get(i).getParcelableExtra(TASK_FLEXIBLE_CURRENT_RECT, Rect.class);
            int appOrientation = -2;
            if (intentList.size() == 2) {
                boolean vSplit = intentList.get(i).getBooleanExtra(IN_VERTICAL_WHITE_LIST, false);
                if (layoutOrientation == 0 && taskDataList.size() == 1 && !taskDataList.get(0).isVertical() && vSplit) {
                    appOrientation = 0;
                }
                if (layoutOrientation == 1 && vSplit) {
                    appOrientation = 0;
                }
            }
            TaskData taskData = taskDataGetFromIntent(intentList.get(i), appOrientation);
            if (taskData != null && taskData.getResizeMode() == 1 && rect != null && !rect.isEmpty()) {
                if (taskData.isVertical() && rect.height() < rect.width()) {
                    taskData = taskDataGetFromIntent(intentList.get(i), 0);
                } else if (!taskData.isVertical() && rect.height() > rect.width()) {
                    taskData = taskDataGetFromIntent(intentList.get(i), 1);
                }
            }
            if (taskData != null) {
                taskDataList.add(taskData);
            }
        }
        setAppUseMaxByStoreData(context, taskDataList);
        if (taskDataList.size() == 0) {
            Log.e(TAG, "intent not support pocket studio");
            return new ArrayList();
        }
        return taskDataList;
    }

    private static List<Rect> getCanvasLayoutRect(Context context, List<TaskData> taskDataList, int layoutOrientation) {
        sDensity = context.getResources().getDisplayMetrics().density;
        List<Rect> rectList = new ArrayList<>();
        if (taskDataList.size() == 2) {
            rectList.addAll(twoSplitLayout(taskDataList, layoutOrientation, sDensity));
        } else if (taskDataList.size() == 3) {
            rectList.addAll(threeSplitLayout(taskDataList, layoutOrientation, sDensity));
        } else if (taskDataList.size() == 1) {
            rectList.add(taskDataList.get(0).getRect());
        }
        Log.d(TAG, "layoutRectCalculate rectList:" + rectList);
        return rectList;
    }

    private static List<Rect> scaleRectForFocusOrPanorama(Context context, List<Rect> rectList, int layoutOrientation, int focusIndex, boolean globalMode) {
        return scaleRectForFocusOrPanorama(context, rectList, layoutOrientation, 2, focusIndex, globalMode);
    }

    private static List<Rect> scaleRectForFocusOrPanorama(Context context, List<Rect> rectList, int layoutOrientation, int threeLineLayoutFocusSide, int focusIndex, boolean globalMode) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        Rect displayRect = windowManager.getMaximumWindowMetrics().getBounds();
        Rect globalSecureRect = new Rect(sStableRect);
        if (globalSecureRect.isEmpty()) {
            globalSecureRect.set(displayRect);
        }
        ArrayList<Rect> screenRectList = new ArrayList<>();
        if (!globalMode) {
            List<RectF> rectFList = getLocationOnScreenWithFocus(context, rectList, layoutOrientation, threeLineLayoutFocusSide, focusIndex);
            if (rectFList == null) {
                return new ArrayList();
            }
            for (RectF rectF : rectFList) {
                Rect finalRect = new Rect((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                screenRectList.add(finalRect);
            }
        } else {
            List<Rect> globalRectList = globalViewScale(rectList, globalSecureRect, sDensity);
            screenRectList.addAll(globalRectList);
        }
        return screenRectList;
    }

    private static List<Rect> globalViewScale(List<Rect> rectList, Rect secureRect, float density) {
        Rect globalRect = new Rect(0, 0, 0, 0);
        Iterator<Rect> it = rectList.iterator();
        while (it.hasNext()) {
            globalRect.union(it.next());
        }
        if (secureRect.contains(globalRect)) {
            return rectList;
        }
        float globalMargin = 16.0f * density;
        float scaleX = ((secureRect.width() - (globalMargin * 2.0f)) * 1.0f) / globalRect.width();
        float scaleY = ((secureRect.height() - (2.0f * globalMargin)) * 1.0f) / globalRect.height();
        float scale = scaleX;
        int scaleOrientation = 0;
        if (scaleX > scaleY) {
            scale = scaleY;
            scaleOrientation = 1;
        } else if (scaleX < scaleY) {
            scale = scaleX;
            scaleOrientation = 2;
        }
        int translationX = (int) globalMargin;
        int translationY = (int) globalMargin;
        if (scaleOrientation == 1) {
            globalRect.scale(scale);
            translationX = (secureRect.width() - globalRect.width()) / 2;
        } else if (scaleOrientation == 2) {
            globalRect.scale(scale);
            translationY = (secureRect.height() - globalRect.height()) / 2;
        }
        for (Rect rect : rectList) {
            rect.scale(scale);
            rect.offset(translationX, translationY);
        }
        return rectList;
    }

    private static void sortTaskDataList(List<TaskData> taskDataList, TaskData focusTaskData, int oldLayoutOrientation, final int layoutOrientation) {
        if (taskDataList.size() < 3 || layoutOrientation == 3) {
            return;
        }
        int oldIndex = taskDataList.indexOf(focusTaskData);
        final boolean finalSortSameOrientation = getSortSameOrientation(layoutOrientation, taskDataList);
        Collections.sort(taskDataList, new Comparator() { // from class: com.oplus.flexiblewindow.CanvasCalculateLayoutRect$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return CanvasCalculateLayoutRect.lambda$sortTaskDataList$0(layoutOrientation, finalSortSameOrientation, (CanvasCalculateLayoutRect.TaskData) obj, (CanvasCalculateLayoutRect.TaskData) obj2);
            }
        });
        if (focusTaskData == null || taskDataList.indexOf(focusTaskData) != 0) {
            return;
        }
        if (oldIndex == 0 && oldLayoutOrientation == layoutOrientation) {
            return;
        }
        if (layoutOrientation == 5 || layoutOrientation == 7) {
            taskDataList.remove(focusTaskData);
            taskDataList.add(1, focusTaskData);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$sortTaskDataList$0(int layoutOrientation, boolean finalSortSameOrientation, TaskData t1, TaskData t2) {
        switch (layoutOrientation) {
            case 4:
                return sortThreeLayoutBigDownOrUp(t1, t2, finalSortSameOrientation, -1);
            case 5:
                return sortThreeLayoutBigDownOrUp(t1, t2, finalSortSameOrientation, 1);
            case 6:
                return sortThreeLayoutBigLeftOrRight(t1, t2, finalSortSameOrientation, -1);
            case 7:
                return sortThreeLayoutBigLeftOrRight(t1, t2, finalSortSameOrientation, 1);
            default:
                return 0;
        }
    }

    private static boolean getSortSameOrientation(int layoutOrientation, List<TaskData> taskDataList) {
        boolean sortSameOrientation = false;
        if (layoutOrientation == 5 && isThreeVertical(taskDataList)) {
            float scale = ((taskDataList.get(0).getRect().width() + taskDataList.get(1).getRect().width()) * 1.0f) / taskDataList.get(2).getRect().width();
            if (scale > SORT_SAME_ORIENTATION_SCALE) {
                sortSameOrientation = true;
            }
        }
        if (layoutOrientation == 7 && isThreeHorizontal(taskDataList)) {
            float scale2 = ((taskDataList.get(0).getRect().height() + taskDataList.get(1).getRect().height()) * 1.0f) / taskDataList.get(2).getRect().height();
            if (scale2 > SORT_SAME_ORIENTATION_SCALE) {
                sortSameOrientation = true;
            }
        }
        if (layoutOrientation == 6 && isThreeHorizontal(taskDataList)) {
            float scale3 = ((taskDataList.get(1).getRect().height() + taskDataList.get(2).getRect().height()) * 1.0f) / taskDataList.get(0).getRect().height();
            if (scale3 > SORT_SAME_ORIENTATION_SCALE) {
                sortSameOrientation = true;
            }
        }
        if (layoutOrientation == 4 && isThreeVertical(taskDataList)) {
            float scale4 = ((taskDataList.get(1).getRect().width() + taskDataList.get(2).getRect().width()) * 1.0f) / taskDataList.get(0).getRect().width();
            if (scale4 > SORT_SAME_ORIENTATION_SCALE) {
                return true;
            }
            return sortSameOrientation;
        }
        return sortSameOrientation;
    }

    private static int sortThreeLayoutBigDownOrUp(TaskData t1, TaskData t2, boolean finalSortSameOrientation, int ascending) {
        boolean vertical1 = t1.isVertical();
        boolean vertical2 = t2.isVertical();
        if (vertical1 && vertical2) {
            if (!finalSortSameOrientation || t1.getRect().width() == t2.getRect().width()) {
                return 0;
            }
            if (t1.getRect().width() > t2.getRect().width()) {
                return ascending;
            }
            return -ascending;
        }
        if (vertical1) {
            return -ascending;
        }
        if (!vertical2) {
            return 0;
        }
        return ascending;
    }

    private static int sortThreeLayoutBigLeftOrRight(TaskData t1, TaskData t2, boolean finalSortSameOrientation, int ascending) {
        boolean vertical1 = t1.isVertical();
        boolean vertical2 = t2.isVertical();
        if (!vertical1 && !vertical2) {
            if (!finalSortSameOrientation || t1.getRect().height() == t2.getRect().height()) {
                return 0;
            }
            if (t1.getRect().height() > t2.getRect().height()) {
                return ascending;
            }
            return -ascending;
        }
        if (!vertical1) {
            return -ascending;
        }
        if (vertical2) {
            return 0;
        }
        return ascending;
    }

    private static void sortTaskDataLargeRatio(List<TaskData> taskDataList, int resizeCount, int nineDivideSixteenCount) {
        if (resizeCount + nineDivideSixteenCount == taskDataList.size() && taskDataList.size() == 3 && resizeCount > 0 && nineDivideSixteenCount > 0) {
            if (resizeCount == 1 && nineDivideSixteenCount == 2) {
                TaskData taskData = taskDataList.get(0);
                if (taskData.getResizeMode() == 1) {
                    taskDataList.remove(taskData);
                    taskDataList.add(taskData);
                }
            }
            if (resizeCount == 2 && nineDivideSixteenCount == 1) {
                TaskData taskData2 = taskDataList.get(0);
                if (taskData2.getResizeMode() != 1 && taskData2.getRatio() - SIXTEEN_TO_NINE > 0.0f) {
                    taskDataList.remove(taskData2);
                    taskDataList.add(taskData2);
                }
            }
        }
    }

    private static boolean isThreeSpacialLargeRatioRect(List<TaskData> taskDataList) {
        int splitScreenCount = 0;
        int nineDivideSixteen = 0;
        for (int i = 0; i < taskDataList.size(); i++) {
            TaskData taskData = taskDataList.get(i);
            if (taskData.getResizeMode() == 1 && !taskData.getUseMax()) {
                splitScreenCount++;
            }
            if (taskData.getResizeMode() == 2 && Math.abs(taskData.getRatio() - SIXTEEN_TO_NINE) <= 0.01d) {
                nineDivideSixteen++;
            }
        }
        int i2 = splitScreenCount + nineDivideSixteen;
        return i2 == 3;
    }

    private static int computeLayoutOrientation(List<TaskData> taskDataList, int oldLayoutOrientation, int[] orientationCount) {
        int horizontalCount = 0;
        int verticalCount = 0;
        if (taskDataList.size() == 2) {
            if (taskDataList.get(0).isVertical()) {
                return 2;
            }
            return 1;
        }
        if (taskDataList.size() != 3) {
            return 0;
        }
        for (int i = 0; i < taskDataList.size(); i++) {
            if (taskDataList.get(i).isVertical()) {
                verticalCount++;
            } else {
                horizontalCount++;
            }
        }
        orientationCount[0] = horizontalCount;
        orientationCount[1] = verticalCount;
        boolean isThreeLarge = false;
        if (orientationCount[0] == 3 || orientationCount[1] == 3) {
            isThreeLarge = isThreeSpacialLargeRatioRect(taskDataList);
        }
        if (isThreeLarge) {
            return 3;
        }
        if (verticalCount >= 2) {
            if (oldLayoutOrientation == 5 || oldLayoutOrientation == 4) {
                return oldLayoutOrientation;
            }
            return 5;
        }
        if (oldLayoutOrientation == 7 || oldLayoutOrientation == 6) {
            return oldLayoutOrientation;
        }
        return 7;
    }

    private static boolean[] isResizeTwoSplitHorizontalVertical(List<TaskData> taskDataList) {
        boolean[] useMaxRect = new boolean[2];
        if (isTwoSplitLayout(taskDataList)) {
            if (sLayoutOrientation == 2) {
                if (!taskDataList.get(0).isVertical() && taskDataList.get(0).getResizeMode() == 1) {
                    useMaxRect[0] = true;
                }
                if (!taskDataList.get(1).isVertical() && taskDataList.get(1).getResizeMode() == 1) {
                    useMaxRect[1] = true;
                }
            } else {
                if (taskDataList.get(0).isVertical() && taskDataList.get(0).getResizeMode() == 1) {
                    useMaxRect[0] = true;
                }
                if (taskDataList.get(1).isVertical() && taskDataList.get(1).getResizeMode() == 1) {
                    useMaxRect[1] = true;
                }
            }
        }
        return useMaxRect;
    }

    private static boolean isTwoSplitLayout(List<TaskData> taskDataList) {
        if (taskDataList != null && taskDataList.size() == 2) {
            return true;
        }
        return false;
    }

    private static List<Rect> twoSplitLayout(List<TaskData> taskDataList, int layoutOrientation, float density) {
        List<TaskData> list = taskDataList;
        List<Rect> rectList = new ArrayList<>();
        boolean[] useMax = isResizeTwoSplitHorizontalVertical(taskDataList);
        int i = 0;
        while (i < taskDataList.size()) {
            Rect rect = new Rect(list.get(i).getRect());
            Rect launchBound = new Rect(rect);
            Rect layoutRect = new Rect();
            int left = 0;
            int right = 0;
            int top = 0;
            int bottom = 0;
            if (useMax[i]) {
                rect.set(list.get(i).getMaxRect());
                launchBound.set(rect);
            }
            if (i == 0) {
                Rect secondRect = new Rect();
                if (useMax[1]) {
                    secondRect.set(list.get(1).getMaxRect());
                } else {
                    secondRect.set(list.get(1).getRect());
                }
                if (layoutOrientation == 2) {
                    bottom = 0 + Math.max(rect.height(), secondRect.height());
                    right = (list.get(i).getResizeMode() != 2 || list.get(i).isVertical()) ? ((int) ((((bottom - 0) * 1.0f) / rect.height()) * rect.width())) + 0 : rect.width() + 0;
                }
                if (layoutOrientation == 1) {
                    int right2 = Math.max(rect.width(), secondRect.width()) + 0;
                    if (list.get(i).getResizeMode() != 2 || !list.get(i).isVertical()) {
                        bottom = ((int) ((((right2 - 0) * 1.0f) / rect.width()) * rect.height())) + 0;
                        right = right2;
                    } else {
                        bottom = rect.height() + 0;
                        right = right2;
                    }
                }
            } else if (i == 1) {
                float scale = 1.0f;
                TaskData taskData = list.get(1);
                boolean fullScreen = taskData.getResizeMode() != 2;
                if (layoutOrientation == 2) {
                    if (rectList.get(0).height() != rect.height() && (fullScreen || taskData.isVertical())) {
                        scale = rectList.get(0).height() / rect.height();
                    }
                    left = rectList.get(0).right + ((int) (density * 8.0f));
                    top = rectList.get(0).top;
                    right = left + ((int) (rect.width() * scale));
                    bottom = rectList.get(0).bottom;
                } else {
                    if (rectList.get(0).width() != rect.width() && (fullScreen || !taskData.isVertical())) {
                        scale = rectList.get(0).width() / rect.width();
                    }
                    left = rectList.get(0).left;
                    top = rectList.get(0).bottom + ((int) (density * 8.0f));
                    right = rectList.get(0).right;
                    bottom = top + ((int) (rect.height() * scale));
                }
            }
            layoutRect.set(left, top, right, bottom);
            rectList.add(layoutRect);
            i++;
            list = taskDataList;
        }
        return rectList;
    }

    private static List<Rect> threeSplitLayout(List<TaskData> taskDataList, int layoutOrientation, float density) {
        char c;
        List<Rect> rectList = new ArrayList<>();
        taskDataList.get(1);
        char c2 = 2;
        taskDataList.get(2);
        int i = 0;
        while (i < taskDataList.size()) {
            Rect rect = taskDataList.get(i).getRect();
            Rect layoutRect = new Rect();
            String str = TAG;
            Log.d(str, "layoutFlexibleTask i=" + i + " " + rect + " layoutOrientation:" + layoutOrientation);
            if (i != 0) {
                if (i == 1) {
                    if (layoutOrientation == 5) {
                        layoutRect.set(layoutRightOfRect(rectList.get(0), rect, true));
                        c = 2;
                    } else if (layoutOrientation == 7) {
                        layoutRect.set(layoutBottomOfRect(rectList.get(0), rect, true));
                        c = 2;
                    } else if (layoutOrientation == 3) {
                        if (taskDataList.get(i).isVertical()) {
                            layoutRect.set(layoutRightOfRect(rectList.get(0), rect, true));
                            c = 2;
                        } else {
                            layoutRect.set(layoutBottomOfRect(rectList.get(0), rect, true));
                            c = 2;
                        }
                    } else if (layoutOrientation == 6) {
                        layoutRect.set(layoutRightOfRect(rectList.get(0), rect, false));
                        c = 2;
                    } else if (layoutOrientation != 4) {
                        c = 2;
                    } else {
                        layoutRect.set(layoutBottomOfRect(rectList.get(0), rect, false));
                        c = 2;
                    }
                } else {
                    c = 2;
                    if (i == 2) {
                        if (layoutOrientation == 3) {
                            if (taskDataList.get(i).isVertical()) {
                                layoutRect.set(layoutRightOfRect(rectList.get(1), rect, true));
                            } else {
                                layoutRect.set(layoutBottomOfRect(rectList.get(1), rect, true));
                            }
                        } else if (layoutOrientation == 7) {
                            Rect unionRect = new Rect(rectList.get(0));
                            unionRect.union(rectList.get(1));
                            layoutRect.set(layoutRightOfRect(unionRect, rect, true));
                        } else if (layoutOrientation == 5) {
                            Rect unionRect2 = new Rect(rectList.get(0));
                            unionRect2.union(rectList.get(1));
                            layoutRect.set(layoutBottomOfRect(unionRect2, rect, true));
                        } else if (layoutOrientation == 6) {
                            layoutRect.set(layoutBottomOfRect(rectList.get(1), rect, true));
                        } else if (layoutOrientation == 4) {
                            layoutRect.set(layoutRightOfRect(rectList.get(1), rect, true));
                        }
                    }
                }
            } else {
                layoutRect.set(firstLayoutInThreeSplit(taskDataList, layoutOrientation, density));
                c = c2;
            }
            rectList.add(layoutRect);
            Log.d(str, "layoutFlexibleTask " + layoutRect);
            i++;
            c2 = c;
        }
        return rectList;
    }

    private static Rect firstLayoutInThreeSplit(List<TaskData> taskDataList, int layoutOrientation, float density) {
        TaskData secondTaskData = taskDataList.get(1);
        TaskData thirdTaskData = taskDataList.get(2);
        Rect rect = taskDataList.get(0).getRect();
        Rect layoutRect = new Rect();
        layoutRect.left = 0;
        layoutRect.top = 0;
        layoutRect.right = rect.width();
        layoutRect.bottom = rect.height();
        if (layoutOrientation == 4) {
            if (secondTaskData.getRect().height() == thirdTaskData.getRect().height()) {
                layoutRect.right = secondTaskData.getRect().width() + thirdTaskData.getRect().width() + ((int) (10.0f * density));
            } else {
                int thirdLayoutWidth = (int) (((secondTaskData.getRect().height() * 1.0f) / thirdTaskData.getRect().height()) * thirdTaskData.getRect().width());
                layoutRect.right = secondTaskData.getRect().width() + thirdLayoutWidth + ((int) (10.0f * density));
            }
            int thirdLayoutWidth2 = layoutRect.width();
            layoutRect.bottom = (int) (((thirdLayoutWidth2 * 1.0f) / rect.width()) * rect.height());
        } else if (layoutOrientation == 6) {
            if (secondTaskData.getRect().width() == thirdTaskData.getRect().width()) {
                layoutRect.bottom = secondTaskData.getRect().height() + ((int) (10.0f * density)) + thirdTaskData.getRect().height();
            } else {
                int thirdLayoutHeight = (int) (((secondTaskData.getRect().width() * 1.0f) / thirdTaskData.getRect().width()) * thirdTaskData.getRect().height());
                layoutRect.bottom = secondTaskData.getRect().height() + thirdLayoutHeight + ((int) (10.0f * density));
            }
            int thirdLayoutHeight2 = layoutRect.height();
            layoutRect.right = (int) (((thirdLayoutHeight2 * 1.0f) / rect.height()) * rect.width());
        }
        return layoutRect;
    }

    private static Rect layoutRightOfRect(Rect regionalRect, Rect rect, boolean align) {
        Rect layoutRect = new Rect();
        layoutRect.left = regionalRect.right + ((int) (sDensity * 10.0f));
        layoutRect.top = regionalRect.top;
        if (align) {
            float scale = 1.0f;
            if (regionalRect.height() != rect.height() && !rect.isEmpty()) {
                scale = regionalRect.height() / rect.height();
            }
            layoutRect.right = layoutRect.left + ((int) (rect.width() * scale));
            layoutRect.bottom = regionalRect.bottom;
        } else {
            layoutRect.right = layoutRect.left + rect.width();
            layoutRect.bottom = layoutRect.top + rect.height();
        }
        return layoutRect;
    }

    private static Rect layoutBottomOfRect(Rect regionalRect, Rect rect, boolean align) {
        Rect layoutRect = new Rect();
        layoutRect.left = regionalRect.left;
        layoutRect.top = regionalRect.bottom + ((int) (sDensity * 10.0f));
        if (align) {
            float scale = 1.0f;
            layoutRect.right = regionalRect.right;
            if (regionalRect.width() != rect.width() && !rect.isEmpty()) {
                scale = regionalRect.width() / rect.width();
            }
            layoutRect.bottom = layoutRect.top + ((int) (rect.height() * scale));
        } else {
            layoutRect.right = layoutRect.left + rect.width();
            layoutRect.bottom = layoutRect.top + rect.height();
        }
        return layoutRect;
    }

    private static boolean isThreeVertical(List<TaskData> taskDataList) {
        if (taskDataList.size() != 3) {
            return false;
        }
        for (int i = 0; i < taskDataList.size(); i++) {
            if (!taskDataList.get(i).isVertical()) {
                return false;
            }
        }
        return true;
    }

    private static boolean isThreeHorizontal(List<TaskData> taskDataList) {
        if (taskDataList.size() != 3) {
            return false;
        }
        for (int i = 0; i < taskDataList.size(); i++) {
            if (taskDataList.get(i).isVertical()) {
                return false;
            }
        }
        return true;
    }

    private static TaskData taskDataGetFromIntent(Intent intent, int appOrientation) {
        if (intent == null) {
            return null;
        }
        Bundle bundle = FlexibleWindowManager.getInstance().calculateFlexibleWindowBounds(intent, appOrientation, 0);
        int resizeMode = ((Integer) bundle.get(FlexibleWindowManager.KEY_FLEXIBLE_RESIZE_MODE)).intValue();
        if (resizeMode == 0) {
            Log.e(TAG, intent + " not support pocket studio");
            return null;
        }
        Rect rect = (Rect) bundle.get(FlexibleWindowManager.KEY_FLEXIBLE_LAUNCH_BOUNDS);
        String str = TAG;
        Log.d(str, "taskDataGetFromIntent:" + intent + " rect:" + rect);
        TaskData taskData = new TaskData(rect, 0, intent);
        if (rect.isEmpty()) {
            Log.e(str, "rect is empty");
            return null;
        }
        taskData.setResizeMode(resizeMode);
        if (resizeMode == 2) {
            taskData.setRatio(((Float) bundle.get(FlexibleWindowManager.KEY_FLEXIBLE_COMPAT_RATIO)).floatValue());
        }
        if (resizeMode == 1) {
            taskData.setMaxRect((Rect) bundle.get(FlexibleWindowManager.KEY_FLEXIBLE_LAUNCH_MAX_BOUNDS));
        }
        int orientation = ((Integer) bundle.get(FlexibleWindowManager.KEY_FLEXIBLE_SCREEN_ORIENTATION)).intValue();
        taskData.setOrientation(orientation);
        Object stableRect = bundle.get(FlexibleWindowManager.KEY_FLEXIBLE_STABLE_RECT);
        if (stableRect instanceof Rect) {
            sStableRect.set((Rect) stableRect);
        }
        Rect windowMetricsBounds = (Rect) bundle.get(FlexibleWindowManager.KEY_FLEXIBLE_WINDOW_METRICS_BOUNDS);
        sWindowMetricsBounds.set(windowMetricsBounds);
        return taskData;
    }

    private static int getNavBarHeightRes(Context context) {
        Resources res = context.getResources();
        int id = res.getIdentifier("navigation_bar_height", "dimen", OplusThemeResources.FRAMEWORK_PACKAGE);
        if (id <= 0) {
            return 0;
        }
        int height = res.getDimensionPixelSize(id);
        return height;
    }

    private static boolean isGestureNavMode(Context context) {
        int navMode = Settings.Secure.getInt(context.getContentResolver(), OplusViewRootUtil.KEY_NAVIGATIONBAR_MODE, 0);
        return navMode == 2 || navMode == 3;
    }

    public static List<RectF> getLocationOnScreenWithFocus(Context context, List<Rect> locationOnCanvas, int layoutOrientation, int threeLineLayoutFocusSide, int focus) {
        if (focus >= 0 && focus < locationOnCanvas.size()) {
            float density = context.getResources().getDisplayMetrics().density;
            Rect focusedChild = new Rect(locationOnCanvas.get(focus));
            if (layoutOrientation == 3 && locationOnCanvas.size() == 3) {
                switch (focus) {
                    case 0:
                    case 2:
                        focusedChild.union(locationOnCanvas.get(1));
                        break;
                    case 1:
                        if (threeLineLayoutFocusSide == 1) {
                            focusedChild.union(locationOnCanvas.get(0));
                            break;
                        } else if (threeLineLayoutFocusSide == 2) {
                            focusedChild.union(locationOnCanvas.get(2));
                            break;
                        } else {
                            Log.e(TAG, "threeLineLayoutFocusSide unknown");
                            break;
                        }
                    default:
                        Log.e(TAG, "unknown focus " + focus);
                        break;
                }
            }
            int align = getAlignOfFocusView(locationOnCanvas, focusedChild);
            int minWidth = focusedChild.width();
            int minHeight = focusedChild.height();
            Rect displayRect = new Rect(sWindowMetricsBounds);
            if (displayRect.isEmpty()) {
                WindowManager windowManager = (WindowManager) context.getSystemService("window");
                Rect windowBounds = windowManager.getMaximumWindowMetrics().getBounds();
                displayRect.set(windowBounds);
            }
            int withinScreenRangeHeight = (int) (displayRect.height() * 0.08d);
            int withinScreenRangeWidth = (int) (displayRect.width() * 0.08d);
            Rect currentViewDisplayRect = new Rect(displayRect);
            switch (align) {
                case 1:
                    currentViewDisplayRect.left += withinScreenRangeWidth;
                    break;
                case 2:
                    currentViewDisplayRect.bottom -= withinScreenRangeHeight;
                    break;
                case 3:
                    currentViewDisplayRect.left += withinScreenRangeWidth;
                    currentViewDisplayRect.bottom -= withinScreenRangeHeight;
                    break;
                case 4:
                    currentViewDisplayRect.right -= withinScreenRangeWidth;
                    break;
                case 5:
                case 7:
                case 10:
                case 11:
                default:
                    Log.e(TAG, "unknown align result");
                    break;
                case 6:
                    currentViewDisplayRect.right -= withinScreenRangeWidth;
                    currentViewDisplayRect.bottom -= withinScreenRangeHeight;
                    break;
                case 8:
                    currentViewDisplayRect.top += withinScreenRangeHeight;
                    break;
                case 9:
                    currentViewDisplayRect.top += withinScreenRangeHeight;
                    currentViewDisplayRect.left += withinScreenRangeWidth;
                    break;
                case 12:
                    currentViewDisplayRect.right -= withinScreenRangeWidth;
                    currentViewDisplayRect.top += withinScreenRangeHeight;
                    break;
            }
            double scaleX = (currentViewDisplayRect.width() * 1.0d) / minWidth;
            double scaleY = (currentViewDisplayRect.height() * 1.0d) / minHeight;
            double scale = Math.min(scaleX, scaleY);
            double[] result = updateAlign(locationOnCanvas, displayRect, align, scale, density);
            int align2 = (int) result[0];
            double scale2 = result[1];
            List<RectF> res = getLocationRect(align2, addMargin(locationOnCanvas, currentViewDisplayRect, displayRect, focusedChild, align2, scale2, density), focusedChild, locationOnCanvas, displayRect);
            return res;
        }
        return null;
    }

    private static double addMargin(List<Rect> locationOnCanvas, Rect currentViewDisplayRect, Rect displayRect, Rect targetRect, int align, double scale, float density) {
        Rect rectListUnion = new Rect();
        for (Rect rect : locationOnCanvas) {
            rectListUnion.union(rect);
        }
        boolean isInFullView = ((double) rectListUnion.width()) * scale < ((double) displayRect.width()) + 1.0d && ((double) rectListUnion.height()) * scale < ((double) displayRect.height()) + 1.0d;
        boolean needMargin = false;
        switch (align) {
            case 1:
            case 4:
                needMargin = ((double) rectListUnion.height()) * scale < ((double) displayRect.height()) - 1.0d;
                break;
            case 2:
            case 8:
                needMargin = ((double) rectListUnion.width()) * scale < ((double) displayRect.width()) - 1.0d;
                break;
            default:
                Log.e(TAG, "unknown align result");
                break;
        }
        int margin = (int) (8.0f * density);
        if (!needMargin) {
            return scale;
        }
        if (isInFullView) {
            switch (align) {
                case 1:
                case 4:
                    displayRect.left += margin;
                    displayRect.right -= margin;
                    double newScale = (displayRect.width() * 1.0f) / rectListUnion.width();
                    return newScale;
                case 2:
                case 8:
                    displayRect.top += margin;
                    displayRect.bottom -= margin;
                    double newScale2 = (displayRect.height() * 1.0f) / rectListUnion.height();
                    return newScale2;
                default:
                    Log.e(TAG, "unknown align result");
                    return scale;
            }
        }
        switch (align) {
            case 1:
                displayRect.right -= margin;
                displayRect.top += margin;
                displayRect.bottom -= margin;
                currentViewDisplayRect.right -= margin;
                double newScale3 = Math.min((currentViewDisplayRect.width() * 1.0f) / targetRect.width(), (displayRect.height() * 1.0f) / rectListUnion.height());
                return newScale3;
            case 2:
                displayRect.top += margin;
                displayRect.left += margin;
                displayRect.right -= margin;
                currentViewDisplayRect.top += margin;
                double newScale4 = Math.min((currentViewDisplayRect.height() * 1.0f) / targetRect.height(), (displayRect.width() * 1.0f) / rectListUnion.width());
                return newScale4;
            case 4:
                displayRect.left += margin;
                displayRect.top += margin;
                displayRect.bottom -= margin;
                currentViewDisplayRect.left += margin;
                double newScale5 = Math.min((currentViewDisplayRect.width() * 1.0f) / targetRect.width(), (displayRect.height() * 1.0f) / rectListUnion.height());
                return newScale5;
            case 8:
                displayRect.bottom -= margin;
                displayRect.left += margin;
                displayRect.right -= margin;
                currentViewDisplayRect.bottom -= margin;
                double newScale6 = Math.min((currentViewDisplayRect.height() * 1.0f) / targetRect.height(), (displayRect.width() * 1.0f) / rectListUnion.width());
                return newScale6;
            default:
                Log.e(TAG, "unknown align result");
                return scale;
        }
    }

    private static double[] updateAlign(List<Rect> locationOnCanvas, Rect displayRect, int align, double scale, float density) {
        Rect rectListUnion = new Rect();
        for (Rect rect : locationOnCanvas) {
            rectListUnion.union(rect);
        }
        double outScreenLengthX = (rectListUnion.width() * scale) - displayRect.width();
        double outScreenLengthY = (rectListUnion.height() * scale) - displayRect.height();
        double newScaleX = scale;
        double newScaleY = scale;
        int outScreenRangePixel = (int) ((56.0f * density) + 0.5f);
        if (outScreenLengthX >= 0.0d && outScreenLengthX <= outScreenRangePixel) {
            newScaleX = (displayRect.width() * 1.0d) / rectListUnion.width();
        }
        if (outScreenLengthY >= 0.0d && outScreenLengthY <= outScreenRangePixel) {
            newScaleY = (displayRect.height() * 1.0d) / rectListUnion.height();
        }
        double newScale = Math.min(newScaleX, newScaleY);
        int newAlign = align;
        switch (align) {
            case 1:
            case 4:
                if (rectListUnion.width() * newScale < displayRect.width()) {
                    newAlign = 2;
                    break;
                }
                break;
            case 2:
            case 8:
                if (rectListUnion.height() * newScale < displayRect.height()) {
                    newAlign = 4;
                    break;
                }
                break;
            case 3:
                if (rectListUnion.width() * newScale < displayRect.width()) {
                    newAlign = 2;
                }
                if (rectListUnion.height() * newScale < displayRect.height()) {
                    newAlign = 1;
                    break;
                }
                break;
            case 5:
            case 7:
            case 10:
            case 11:
            default:
                Log.e(TAG, "unknown align result");
                break;
            case 6:
                if (rectListUnion.width() * newScale < displayRect.width()) {
                    newAlign = 2;
                }
                if (rectListUnion.height() * newScale < displayRect.height()) {
                    newAlign = 4;
                    break;
                }
                break;
            case 9:
                if (rectListUnion.width() * newScale < displayRect.width()) {
                    newAlign = 8;
                }
                if (rectListUnion.height() * newScale < displayRect.height()) {
                    newAlign = 1;
                    break;
                }
                break;
            case 12:
                if (rectListUnion.width() * newScale < displayRect.width()) {
                    newAlign = 8;
                }
                if (rectListUnion.height() * newScale < displayRect.height()) {
                    newAlign = 4;
                    break;
                }
                break;
        }
        return new double[]{newAlign, newScale};
    }

    private static int getAlignOfFocusView(List<Rect> rectList, Rect focusedChild) {
        int res = 0;
        if (focusedChild.isEmpty()) {
            return -1;
        }
        for (Rect temp : rectList) {
            if (temp.bottom <= focusedChild.top) {
                res |= 8;
            }
            if (temp.left >= focusedChild.right) {
                res |= 4;
            }
            if (temp.top >= focusedChild.bottom) {
                res |= 2;
            }
            if (temp.right <= focusedChild.left) {
                res |= 1;
            }
        }
        return res;
    }

    private static List<RectF> getLocationRect(int align, double scale, Rect focusPosition, List<Rect> locationOnCanvas, Rect displayRect) {
        Rect rectListUnion = new Rect();
        Iterator<Rect> it = locationOnCanvas.iterator();
        while (it.hasNext()) {
            rectListUnion.union(it.next());
        }
        int startX = 0;
        int startY = 0;
        int targetX = 0;
        int targetY = 0;
        switch (align) {
            case 1:
                targetX = displayRect.right;
                targetY = displayRect.centerY();
                startX = rectListUnion.right;
                startY = rectListUnion.centerY();
                break;
            case 2:
                targetX = displayRect.centerX();
                targetY = displayRect.top;
                startX = rectListUnion.centerX();
                startY = rectListUnion.top;
                break;
            case 3:
                targetX = displayRect.right;
                targetY = displayRect.top;
                startX = focusPosition.right;
                startY = focusPosition.top;
                break;
            case 4:
                targetX = displayRect.left;
                targetY = displayRect.centerY();
                startX = rectListUnion.left;
                startY = rectListUnion.centerY();
                break;
            case 5:
            case 7:
            case 10:
            case 11:
            default:
                Log.e(TAG, "unknown align result");
                break;
            case 6:
                targetX = displayRect.left;
                targetY = displayRect.top;
                startX = focusPosition.left;
                startY = focusPosition.top;
                break;
            case 8:
                targetX = displayRect.centerX();
                targetY = displayRect.bottom;
                startX = rectListUnion.centerX();
                startY = rectListUnion.bottom;
                break;
            case 9:
                targetX = displayRect.right;
                targetY = displayRect.bottom;
                startX = focusPosition.right;
                startY = focusPosition.bottom;
                break;
            case 12:
                targetX = displayRect.left;
                targetY = displayRect.bottom;
                startX = focusPosition.left;
                startY = focusPosition.bottom;
                break;
        }
        List<RectF> locationOnScreen = new ArrayList<>();
        for (Rect rect : locationOnCanvas) {
            locationOnScreen.add(new RectF((float) (targetX + ((rect.left - startX) * scale)), (float) (targetY + ((rect.top - startY) * scale)), (float) (targetX + ((rect.right - startX) * scale)), (float) (targetY + ((rect.bottom - startY) * scale))));
            rectListUnion = rectListUnion;
        }
        return locationOnScreen;
    }

    private static void setAppUseMaxByStoreData(Context context, List<TaskData> taskDataList) {
        List<TaskData> resizableList;
        String useMaxData;
        boolean useMax;
        List<TaskData> resizableList2 = resizableTaskData(taskDataList);
        if (resizableList2.size() == 0) {
            return;
        }
        String useMaxData2 = Settings.Global.getString(context.getContentResolver(), PS_APP_USE_MAX);
        for (TaskData taskData : resizableList2) {
            String packageName = taskData.getPackage();
            int userId = taskData.mIntent.getIntExtra(FlexibleWindowManager.KEY_CANVAS_USER_ID, 0);
            if (!TextUtils.isEmpty(packageName)) {
                Rect rect = (Rect) taskData.mIntent.getParcelableExtra(TASK_FLEXIBLE_CURRENT_RECT, Rect.class);
                boolean useMax2 = false;
                String appUseMax = packageName + "," + userId;
                boolean currentUseMaxRect = false;
                if (rect != null && !rect.isEmpty() && taskData.getMaxRect().height() != 0) {
                    currentUseMaxRect = Math.abs(((((float) rect.width()) * 1.0f) / ((float) rect.height())) - ((((float) taskData.getMaxRect().width()) * 1.0f) / ((float) taskData.getMaxRect().height()))) <= ERROR_FLOAT;
                    useMax2 = currentUseMaxRect;
                }
                taskData.setUseMax(getCurrentUseMax(taskDataList, taskData, useMax2));
                if (TextUtils.isEmpty(useMaxData2) || !useMaxData2.contains(appUseMax)) {
                    resizableList = resizableList2;
                    useMaxData = useMaxData2;
                    if (taskData.mIntent.getBooleanExtra(TASK_FLEXIBLE_USE_MAX, false) && rect == null) {
                        taskData.setUseMax(true);
                    }
                } else {
                    int start = useMaxData2.indexOf(appUseMax);
                    int end = useMaxData2.indexOf(";", appUseMax.length() + start);
                    if (end < start) {
                        end = useMaxData2.length();
                    }
                    String appUseMaxString = useMaxData2.substring(start, end);
                    String[] data = appUseMaxString.split(",");
                    boolean tempUseMax = false;
                    if (data != null) {
                        resizableList = resizableList2;
                        useMaxData = useMaxData2;
                        if (data.length >= 3) {
                            tempUseMax = Boolean.parseBoolean(data[2]);
                        }
                    } else {
                        resizableList = resizableList2;
                        useMaxData = useMaxData2;
                    }
                    if (tempUseMax && !currentUseMaxRect && rect != null && !rect.isEmpty()) {
                        useMax = false;
                    } else {
                        useMax = tempUseMax;
                    }
                    taskData.setUseMax(useMax);
                }
                resizableList2 = resizableList;
                useMaxData2 = useMaxData;
            }
        }
    }

    private static boolean getCurrentUseMax(List<TaskData> taskDataList, TaskData currentTaskData, boolean useMax) {
        boolean tmpUseMax = useMax;
        List<Rect> nowRectList = new ArrayList<>();
        if (taskDataList.size() == 3) {
            for (TaskData taskData : taskDataList) {
                Rect rect = (Rect) taskData.mIntent.getParcelableExtra(TASK_FLEXIBLE_CURRENT_RECT, Rect.class);
                if (rect != null && !rect.isEmpty()) {
                    nowRectList.add(rect);
                }
            }
        }
        if (nowRectList.size() == 2 && ((nowRectList.get(0).left > nowRectList.get(1).right || nowRectList.get(1).left > nowRectList.get(0).right) && !currentTaskData.isVertical())) {
            tmpUseMax = false;
        }
        if (nowRectList.size() == 2) {
            if ((nowRectList.get(0).top > nowRectList.get(1).bottom || nowRectList.get(1).top > nowRectList.get(0).bottom) && currentTaskData.isVertical()) {
                return false;
            }
            return tmpUseMax;
        }
        return tmpUseMax;
    }

    private static List<TaskData> resizableTaskData(List<TaskData> taskDataList) {
        List<TaskData> resizableList = new ArrayList<>();
        for (TaskData taskData : taskDataList) {
            if (taskData.getResizeMode() == 1) {
                resizableList.add(taskData);
            }
        }
        return resizableList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class TaskData {
        private Intent mIntent;
        private Rect mMaxRect;
        private int mOrientation;
        private float mRatio = 1.0f;
        private Rect mRect;
        private int mResizeMode;
        private int mTaskId;
        private boolean mUseMax;

        TaskData(Rect rect, int taskId, Intent intent) {
            this.mTaskId = -1;
            this.mRect = rect;
            this.mTaskId = taskId;
            this.mIntent = intent;
        }

        Intent getIntent() {
            return this.mIntent;
        }

        Rect getRect() {
            if (this.mUseMax && this.mResizeMode == 1) {
                return this.mMaxRect;
            }
            return this.mRect;
        }

        void setResizeMode(int resizeMode) {
            this.mResizeMode = resizeMode;
        }

        int getResizeMode() {
            return this.mResizeMode;
        }

        void setRatio(float ratio) {
            this.mRatio = ratio;
        }

        float getRatio() {
            return this.mRatio;
        }

        void setOrientation(int orientation) {
            this.mOrientation = orientation;
        }

        int getOrientation() {
            return this.mOrientation;
        }

        Rect getMaxRect() {
            return this.mMaxRect;
        }

        public boolean getUseMax() {
            return this.mUseMax;
        }

        void setMaxRect(Rect mMaxRect) {
            this.mMaxRect = mMaxRect;
        }

        boolean isUseMax() {
            return this.mUseMax;
        }

        void setUseMax(boolean useMax) {
            this.mUseMax = useMax;
        }

        boolean isVertical() {
            int orientation = getOrientation();
            if (ActivityInfo.isFixedOrientationLandscape(orientation)) {
                return false;
            }
            return ActivityInfo.isFixedOrientationPortrait(orientation) || this.mRect.width() <= this.mRect.height();
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("TaskData{");
            Intent intent = this.mIntent;
            if (intent != null) {
                builder.append(intent.getComponent());
            }
            builder.append(" rect:" + this.mRect);
            builder.append(" maxRect:" + this.mMaxRect);
            builder.append(" taskId:" + this.mTaskId);
            builder.append(" resizeMode:" + this.mResizeMode);
            builder.append(" ratio:" + this.mRatio);
            builder.append(" orientation:" + this.mOrientation);
            builder.append("}");
            return builder.toString();
        }

        public String getPackage() {
            Intent intent = this.mIntent;
            if (intent == null) {
                return null;
            }
            if (intent.getComponent() != null) {
                return this.mIntent.getComponent().getPackageName();
            }
            if (TextUtils.isEmpty(this.mIntent.getPackage())) {
                return null;
            }
            return this.mIntent.getPackage();
        }
    }
}
