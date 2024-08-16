package com.android.internal.policy;

import android.R;
import android.content.res.Resources;
import android.graphics.Rect;
import com.android.internal.policy.DividerSnapAlgorithm;
import com.oplus.splitscreen.OplusSplitScreenManager;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class DividerSnapAlgorithmExtImpl implements IDividerSnapAlgorithmExt {
    public static final int MINIMAL_TARGET_SIZE_DP = 80;
    private static final int MINIMAL_TASK_SIZE_DP = 300;
    private static final int RATIO_PERCENT_40 = 40;
    private static final int RATIO_PERCENT_60 = 60;
    private static final int SNAP_MODE_MINIMIZED = 3;
    private static final String TAG = "DividerSnapAlgorithmExtImpl";

    public DividerSnapAlgorithmExtImpl(Object base) {
    }

    private boolean isLargeScreen(Resources res) {
        return res.getConfiguration().smallestScreenWidthDp >= 590;
    }

    public boolean hasLargeScreenFeature() {
        return OplusSplitScreenManager.getInstance().hasLargeScreenFeature();
    }

    public int getSnapMode(Resources res, boolean isMinimizedMode) {
        if (hasLargeScreenFeature() && OplusSplitScreenManager.getInstance().hasColorSplitFeature()) {
            return isMinimizedMode ? 3 : 0;
        }
        if (isMinimizedMode) {
            return 3;
        }
        return res.getInteger(R.integer.config_lowBatteryCloseWarningBump);
    }

    public boolean getFreeSnapMode(Resources res) {
        if (hasLargeScreenFeature() && !isLargeScreen(res)) {
            return false;
        }
        return OplusSplitScreenManager.getInstance().hasColorSplitFeature();
    }

    public boolean addRatioTargets(Resources res, ArrayList<DividerSnapAlgorithm.SnapTarget> targets, int dividerMax, int dividerSize, boolean isHorizontalDivision, Rect insets) {
        if (!OplusSplitScreenManager.getInstance().hasColorSplitFeature() || !hasLargeScreenFeature()) {
            return false;
        }
        if (OplusSplitScreenManager.getInstance().isFoldDevice()) {
            if (isLargeScreen(res)) {
                if (isHorizontalDivision) {
                    int positionMiddle = (dividerMax / 2) - (dividerSize / 2);
                    targets.add(new DividerSnapAlgorithm.SnapTarget(positionMiddle, positionMiddle, 0));
                } else {
                    addDefaultRatioTargetsForLargeScreen(res, targets, dividerMax, dividerSize, false, insets);
                }
            } else {
                int minimalSize = (int) (res.getDisplayMetrics().density * 80.0f);
                int positionMinimalTop = (isHorizontalDivision ? insets.top : insets.left) + minimalSize;
                int positionMinimalBottom = ((dividerMax - minimalSize) - dividerSize) - (isHorizontalDivision ? insets.bottom : insets.right);
                targets.add(new DividerSnapAlgorithm.SnapTarget(positionMinimalTop, positionMinimalTop, 0));
                addDefaultRatioTargetsForLargeScreen(res, targets, dividerMax, dividerSize, isHorizontalDivision, insets);
                targets.add(new DividerSnapAlgorithm.SnapTarget(positionMinimalBottom, positionMinimalBottom, 0));
            }
            return true;
        }
        if (!OplusSplitScreenManager.getInstance().isTabletDevice()) {
            return false;
        }
        addDefaultRatioTargetsForLargeScreen(res, targets, dividerMax, dividerSize, isHorizontalDivision, insets);
        return true;
    }

    private void addDefaultRatioTargetsForLargeScreen(Resources res, ArrayList<DividerSnapAlgorithm.SnapTarget> targets, int dividerMax, int dividerSize, boolean isHorizontalDivision, Rect insets) {
        int i = (dividerMax * 60) / 100;
        int positionMiddle = (dividerMax - dividerSize) / 2;
        int minimalTaskSize = (int) (res.getDisplayMetrics().density * 300.0f);
        int positionTop = Math.max((dividerMax * 40) / 100, minimalTaskSize);
        int positionBottom = (dividerMax - positionTop) - dividerSize;
        targets.add(new DividerSnapAlgorithm.SnapTarget(positionTop, positionTop, 0));
        targets.add(new DividerSnapAlgorithm.SnapTarget(positionMiddle, positionMiddle, 0));
        targets.add(new DividerSnapAlgorithm.SnapTarget(positionBottom, positionBottom, 0));
    }

    public boolean addMinimizedTarget(ArrayList<DividerSnapAlgorithm.SnapTarget> targets, int dockedSide, int dividerSize, int taskHeightInMinimized, Rect insets, int displayWidth, int displayHeight) {
        if (hasLargeScreenFeature()) {
            Rect minimizedBounds = OplusSplitScreenManager.getInstance().getMinimizedBounds(dockedSide);
            int position = 0;
            switch (dockedSide) {
                case 1:
                    position = minimizedBounds.right;
                    break;
                case 2:
                    position = minimizedBounds.bottom;
                    break;
                case 3:
                    position = minimizedBounds.left - dividerSize;
                    break;
                case 4:
                    position = minimizedBounds.top - dividerSize;
                    break;
            }
            targets.add(new DividerSnapAlgorithm.SnapTarget(position, position, 0));
            return true;
        }
        if (!OplusSplitScreenManager.getInstance().hasColorSplitFeature()) {
            return false;
        }
        int position2 = 0;
        switch (dockedSide) {
            case 1:
                position2 = taskHeightInMinimized + insets.left;
                break;
            case 2:
                position2 = taskHeightInMinimized + insets.top;
                break;
            case 3:
                position2 = ((displayWidth - insets.right) - taskHeightInMinimized) - dividerSize;
                break;
            case 4:
                position2 = ((displayHeight - insets.bottom) - taskHeightInMinimized) - dividerSize;
                break;
        }
        targets.add(new DividerSnapAlgorithm.SnapTarget(position2, position2, 0));
        return true;
    }

    public boolean addMiddleTarget(ArrayList<DividerSnapAlgorithm.SnapTarget> targets, boolean isHorizontalDivision, int displayWidth, int displayHeight, int dividerSize) {
        if (!OplusSplitScreenManager.getInstance().hasColorSplitFeature()) {
            return false;
        }
        int dividerMax = isHorizontalDivision ? displayHeight : displayWidth;
        int position = (dividerMax - dividerSize) / 2;
        targets.add(new DividerSnapAlgorithm.SnapTarget(position, position, 0));
        return true;
    }
}
