package com.oplus.wrapper.view;

import android.telephony.OplusTelephonyManager;
import android.view.WindowManager;

/* loaded from: classes.dex */
public class WindowManager {

    /* loaded from: classes.dex */
    public static class LayoutParams {
        private final WindowManager.LayoutParams mLayoutParams;
        public static final int TYPE_VOLUME_OVERLAY = getTypeVolumeOverlay();
        public static final int SYSTEM_FLAG_SHOW_FOR_ALL_USERS = getSystemFlagShowForAllUsers();
        public static final int TYPE_NAVIGATION_BAR_PANEL = getTypeNavigationBarPanel();
        public static final int TYPE_NAVIGATION_BAR = getTypeNavigationBar();
        public static final int TYPE_ACCESSIBILITY_MAGNIFICATION_OVERLAY = getTypeAccessibilityMagnificationOverlay();
        public static final int TYPE_DISPLAY_OVERLAY = getTypeDisplayOverlay();
        public static final int TYPE_DRAG = getTypeDrag();
        public static final int TYPE_MAGNIFICATION_OVERLAY = getTypeMagnificationOverlay();
        public static final int TYPE_SCREENSHOT = getTypeScreenshot();
        public static final int TYPE_SECURE_SYSTEM_OVERLAY = getTypeSecureSystemOverlay();
        public static final int TYPE_STATUS_BAR_SUB_PANEL = getTypeStatusBarSubPanel();
        public static final int PRIVATE_FLAG_NO_MOVE_ANIMATION = getPrivateFlagNoMoveAnimation();
        public static final int PRIVATE_FLAG_FORCE_HARDWARE_ACCELERATED = getPrivateFlagForceHardwareAccelerated();
        public static final int SYSTEM_FLAG_HIDE_NON_SYSTEM_OVERLAY_WINDOWS = getSystemFlagHideNonSystemOverlayWindows();

        public LayoutParams(WindowManager.LayoutParams layoutParams) {
            this.mLayoutParams = layoutParams;
        }

        private static int getTypeVolumeOverlay() {
            return 2020;
        }

        private static int getSystemFlagShowForAllUsers() {
            return 16;
        }

        private static int getTypeNavigationBarPanel() {
            return 2024;
        }

        private static int getTypeNavigationBar() {
            return 2019;
        }

        private static int getTypeAccessibilityMagnificationOverlay() {
            return 2039;
        }

        private static int getTypeDisplayOverlay() {
            return 2026;
        }

        private static int getTypeDrag() {
            return 2016;
        }

        private static int getTypeMagnificationOverlay() {
            return 2027;
        }

        private static int getTypeScreenshot() {
            return 2036;
        }

        private static int getTypeSecureSystemOverlay() {
            return OplusTelephonyManager.EVENT_REG_IS_CAPABILITY_SWITCH;
        }

        private static int getTypeStatusBarSubPanel() {
            return 2017;
        }

        private static int getPrivateFlagNoMoveAnimation() {
            return 64;
        }

        private static int getPrivateFlagForceHardwareAccelerated() {
            return 2;
        }

        private static int getSystemFlagHideNonSystemOverlayWindows() {
            return 524288;
        }

        public void addPrivateFlags(int flag) {
            this.mLayoutParams.privateFlags |= flag;
        }

        public int getPrivateFlags() {
            return this.mLayoutParams.privateFlags;
        }

        public boolean getInsetsRoundedCornerFrame() {
            return this.mLayoutParams.insetsRoundedCornerFrame;
        }

        public void setInsetsRoundedCornerFrame(boolean value) {
            this.mLayoutParams.insetsRoundedCornerFrame = value;
        }

        public void setSystemApplicationOverlay(boolean isSystemApplicationOverlay) {
            this.mLayoutParams.setSystemApplicationOverlay(isSystemApplicationOverlay);
        }

        public boolean getReceiveInsetsIgnoringZOrder() {
            return this.mLayoutParams.receiveInsetsIgnoringZOrder;
        }

        public void setReceiveInsetsIgnoringZOrder(boolean value) {
            this.mLayoutParams.receiveInsetsIgnoringZOrder = value;
        }
    }
}
