package com.oplus.wrapper.jank;

import android.util.Singleton;
import android.view.View;
import com.android.internal.jank.InteractionJankMonitor;

/* loaded from: classes.dex */
public class InteractionJankMonitor {
    private com.android.internal.jank.InteractionJankMonitor mInteractionJankMonitor;
    public static final int CUJ_NOTIFICATION_SHADE_EXPAND_COLLAPSE = getCujNotificationShadeExpandCollapse();
    public static final int CUJ_NOTIFICATION_SHADE_SCROLL_FLING = getCujNotificationShadeScrollFling();
    public static final int CUJ_NOTIFICATION_SHADE_ROW_EXPAND = getCujNotificationShadeRowExpand();
    public static final int CUJ_NOTIFICATION_SHADE_ROW_SWIPE = getCujNotificationShadeRowSwipe();
    public static final int CUJ_NOTIFICATION_SHADE_QS_EXPAND_COLLAPSE = getCujNotificationShadeQsExpandCollapse();
    public static final int CUJ_NOTIFICATION_SHADE_QS_SCROLL_SWIPE = getCujNotificationShadeQsScrollSwipe();
    public static final int CUJ_LAUNCHER_APP_LAUNCH_FROM_RECENTS = getCujLauncherAppLaunchFromRecents();
    public static final int CUJ_LAUNCHER_APP_LAUNCH_FROM_ICON = getCujLauncherAppLaunchFromIcon();
    public static final int CUJ_LAUNCHER_APP_CLOSE_TO_HOME = getCujLauncherAppCloseToHome();
    public static final int CUJ_LAUNCHER_APP_CLOSE_TO_PIP = getCujLauncherAppCloseToPip();
    public static final int CUJ_LAUNCHER_QUICK_SWITCH = getCujLauncherQuickSwitch();
    public static final int CUJ_NOTIFICATION_HEADS_UP_APPEAR = getCujNotificationHeadsUpAppear();
    public static final int CUJ_NOTIFICATION_HEADS_UP_DISAPPEAR = getCujNotificationHeadsUpDisappear();
    public static final int CUJ_NOTIFICATION_ADD = getCujNotificationAdd();
    public static final int CUJ_NOTIFICATION_REMOVE = getCujNotificationRemove();
    public static final int CUJ_NOTIFICATION_APP_START = getCujNotificationAppStart();
    public static final int CUJ_LOCKSCREEN_PASSWORD_APPEAR = getCujLockscreenPasswordAppear();
    public static final int CUJ_LOCKSCREEN_PATTERN_APPEAR = getCujLockscreenPatternAppear();
    public static final int CUJ_LOCKSCREEN_PIN_APPEAR = getCujLockscreenPinAppear();
    public static final int CUJ_LOCKSCREEN_PASSWORD_DISAPPEAR = getCujLockscreenPasswordDisappear();
    public static final int CUJ_LOCKSCREEN_PATTERN_DISAPPEAR = getCujLockscreenPatternDisappear();
    public static final int CUJ_LOCKSCREEN_PIN_DISAPPEAR = getCujLockscreenPinDisappear();
    public static final int CUJ_LOCKSCREEN_TRANSITION_FROM_AOD = getCujLockscreenTransitionFromAod();
    public static final int CUJ_LOCKSCREEN_TRANSITION_TO_AOD = getCujLockscreenTransitionToAod();
    public static final int CUJ_LAUNCHER_OPEN_ALL_APPS = getCujLauncherOpenAllApps();
    public static final int CUJ_LAUNCHER_ALL_APPS_SCROLL = getCujLauncherAllAppsScroll();
    public static final int CUJ_LAUNCHER_APP_LAUNCH_FROM_WIDGET = getCujLauncherAppLaunchFromWidget();
    public static final int CUJ_SETTINGS_PAGE_SCROLL = getCujSettingsPageScroll();
    public static final int CUJ_LOCKSCREEN_UNLOCK_ANIMATION = getCujLockscreenUnlockAnimation();
    public static final int CUJ_SHADE_APP_LAUNCH_FROM_HISTORY_BUTTON = getCujShadeAppLaunchFromHistoryButton();
    public static final int CUJ_SHADE_APP_LAUNCH_FROM_MEDIA_PLAYER = getCujShadeAppLaunchFromMediaPlayer();
    public static final int CUJ_SHADE_APP_LAUNCH_FROM_QS_TILE = getCujShadeAppLaunchFromQsTile();
    public static final int CUJ_SHADE_APP_LAUNCH_FROM_SETTINGS_BUTTON = getCujShadeAppLaunchFromSettingsButton();
    public static final int CUJ_STATUS_BAR_APP_LAUNCH_FROM_CALL_CHIP = getCujStatusBarAppLaunchFromCallChip();
    public static final int CUJ_PIP_TRANSITION = getCujPipTransition();
    public static final int CUJ_WALLPAPER_TRANSITION = getCujWallpaperTransition();
    public static final int CUJ_USER_SWITCH = getCujUserSwitch();
    public static final int CUJ_SPLASHSCREEN_AVD = getCujSplashscreenAvd();
    public static final int CUJ_SPLASHSCREEN_EXIT_ANIM = getCujSplashscreenExitAnim();
    public static final int CUJ_SCREEN_OFF = getCujScreenOff();
    public static final int CUJ_SCREEN_OFF_SHOW_AOD = getCujScreenOffShowAod();
    public static final int CUJ_ONE_HANDED_ENTER_TRANSITION = getCujOneHandedEnterTransition();
    public static final int CUJ_ONE_HANDED_EXIT_TRANSITION = getCujOneHandedExitTransition();
    public static final int CUJ_UNFOLD_ANIM = getCujUnfoldAnim();
    public static final int CUJ_SUW_LOADING_TO_SHOW_INFO_WITH_ACTIONS = getCujSuwLoadingToShowInfoWithActions();
    public static final int CUJ_SUW_SHOW_FUNCTION_SCREEN_WITH_ACTIONS = getCujSuwShowFunctionScreenWithActions();
    public static final int CUJ_SUW_LOADING_TO_NEXT_FLOW = getCujSuwLoadingToNextFlow();
    public static final int CUJ_SUW_LOADING_SCREEN_FOR_STATUS = getCujSuwLoadingScreenForStatus();
    public static final int CUJ_SPLIT_SCREEN_ENTER = getCujSplitScreenEnter();
    public static final int CUJ_SPLIT_SCREEN_EXIT = getCujSplitScreenExit();
    public static final int CUJ_LOCKSCREEN_LAUNCH_CAMERA = getCujLockscreenLaunchCamera();
    public static final int CUJ_SPLIT_SCREEN_RESIZE = getCujSplitScreenResize();
    public static final int CUJ_SETTINGS_SLIDER = getCujSettingsSlider();
    public static final int CUJ_TAKE_SCREENSHOT = getCujTakeScreenshot();
    public static final int CUJ_VOLUME_CONTROL = getCujVolumeControl();
    public static final int CUJ_BIOMETRIC_PROMPT_TRANSITION = getCujBiometricPromptTransition();
    public static final int CUJ_SETTINGS_TOGGLE = getCujSettingsToggle();
    public static final int CUJ_SHADE_DIALOG_OPEN = getCujShadeDialogOpen();
    public static final int CUJ_USER_DIALOG_OPEN = getCujUserDialogOpen();
    public static final int CUJ_TASKBAR_EXPAND = getCujTaskbarExpand();
    public static final int CUJ_TASKBAR_COLLAPSE = getCujTaskbarCollapse();
    public static final int CUJ_SHADE_CLEAR_ALL = getCujShadeClearAll();
    public static final int CUJ_LAUNCHER_UNLOCK_ENTRANCE_ANIMATION = getCujLauncherUnlockEntranceAnimation();
    public static final int CUJ_LOCKSCREEN_OCCLUSION = getCujLockscreenOcclusion();
    public static final int CUJ_RECENTS_SCROLLING = getCujRecentsScrolling();
    public static final int CUJ_LAUNCHER_APP_SWIPE_TO_RECENTS = getCujLauncherAppSwipeToRecents();
    public static final int CUJ_LAUNCHER_CLOSE_ALL_APPS_SWIPE = getCujLauncherCloseAllAppsSwipe();
    public static final int CUJ_LAUNCHER_CLOSE_ALL_APPS_TO_HOME = getCujLauncherCloseAllAppsToHome();
    public static final int CUJ_IME_INSETS_ANIMATION = getCujImeInsetsAnimation();
    public static final int CUJ_LOCKSCREEN_CLOCK_MOVE_ANIMATION = getCujLockscreenClockMoveAnimation();
    public static final int CUJ_LAUNCHER_OPEN_SEARCH_RESULT = getCujLauncherOpenSearchResult();
    private static final Singleton<InteractionJankMonitor> SINSTANCE = new Singleton<InteractionJankMonitor>() { // from class: com.oplus.wrapper.jank.InteractionJankMonitor.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public InteractionJankMonitor m1149create() {
            InteractionJankMonitor interactionJankMonitor = new InteractionJankMonitor();
            return interactionJankMonitor;
        }
    };

    private InteractionJankMonitor() {
        this.mInteractionJankMonitor = com.android.internal.jank.InteractionJankMonitor.getInstance();
    }

    public static InteractionJankMonitor getInstance() {
        return (InteractionJankMonitor) SINSTANCE.get();
    }

    private static int getCujNotificationShadeExpandCollapse() {
        return 0;
    }

    private static int getCujNotificationShadeScrollFling() {
        return 2;
    }

    private static int getCujNotificationShadeRowExpand() {
        return 3;
    }

    private static int getCujNotificationShadeRowSwipe() {
        return 4;
    }

    private static int getCujNotificationShadeQsExpandCollapse() {
        return 5;
    }

    private static int getCujNotificationShadeQsScrollSwipe() {
        return 6;
    }

    private static int getCujLauncherAppLaunchFromRecents() {
        return 7;
    }

    private static int getCujLauncherAppLaunchFromIcon() {
        return 8;
    }

    private static int getCujLauncherAppCloseToHome() {
        return 9;
    }

    private static int getCujLauncherAppCloseToPip() {
        return 10;
    }

    private static int getCujLauncherQuickSwitch() {
        return 11;
    }

    private static int getCujNotificationHeadsUpAppear() {
        return 12;
    }

    private static int getCujNotificationHeadsUpDisappear() {
        return 13;
    }

    private static int getCujNotificationAdd() {
        return 14;
    }

    private static int getCujNotificationRemove() {
        return 15;
    }

    private static int getCujNotificationAppStart() {
        return 16;
    }

    private static int getCujLockscreenPasswordAppear() {
        return 17;
    }

    private static int getCujLockscreenPatternAppear() {
        return 18;
    }

    private static int getCujLockscreenPinAppear() {
        return 19;
    }

    private static int getCujLockscreenPasswordDisappear() {
        return 20;
    }

    private static int getCujLockscreenPatternDisappear() {
        return 21;
    }

    private static int getCujLockscreenPinDisappear() {
        return 22;
    }

    private static int getCujLockscreenTransitionFromAod() {
        return 23;
    }

    private static int getCujLockscreenTransitionToAod() {
        return 24;
    }

    private static int getCujLauncherOpenAllApps() {
        return 25;
    }

    private static int getCujLauncherAllAppsScroll() {
        return 26;
    }

    private static int getCujLauncherAppLaunchFromWidget() {
        return 27;
    }

    private static int getCujSettingsPageScroll() {
        return 28;
    }

    private static int getCujLockscreenUnlockAnimation() {
        return 29;
    }

    private static int getCujShadeAppLaunchFromHistoryButton() {
        return 30;
    }

    private static int getCujShadeAppLaunchFromMediaPlayer() {
        return 31;
    }

    private static int getCujShadeAppLaunchFromQsTile() {
        return 32;
    }

    private static int getCujShadeAppLaunchFromSettingsButton() {
        return 33;
    }

    private static int getCujStatusBarAppLaunchFromCallChip() {
        return 34;
    }

    private static int getCujPipTransition() {
        return 35;
    }

    private static int getCujWallpaperTransition() {
        return 36;
    }

    private static int getCujUserSwitch() {
        return 37;
    }

    private static int getCujSplashscreenAvd() {
        return 38;
    }

    private static int getCujSplashscreenExitAnim() {
        return 39;
    }

    private static int getCujScreenOff() {
        return 40;
    }

    private static int getCujScreenOffShowAod() {
        return 41;
    }

    private static int getCujOneHandedEnterTransition() {
        return 42;
    }

    private static int getCujOneHandedExitTransition() {
        return 43;
    }

    private static int getCujUnfoldAnim() {
        return 44;
    }

    private static int getCujSuwLoadingToShowInfoWithActions() {
        return 45;
    }

    private static int getCujSuwShowFunctionScreenWithActions() {
        return 46;
    }

    private static int getCujSuwLoadingToNextFlow() {
        return 47;
    }

    private static int getCujSuwLoadingScreenForStatus() {
        return 48;
    }

    private static int getCujSplitScreenEnter() {
        return 49;
    }

    private static int getCujSplitScreenExit() {
        return 50;
    }

    private static int getCujLockscreenLaunchCamera() {
        return 51;
    }

    private static int getCujSplitScreenResize() {
        return 52;
    }

    private static int getCujSettingsSlider() {
        return 53;
    }

    private static int getCujTakeScreenshot() {
        return 54;
    }

    private static int getCujVolumeControl() {
        return 55;
    }

    private static int getCujBiometricPromptTransition() {
        return 56;
    }

    private static int getCujSettingsToggle() {
        return 57;
    }

    private static int getCujShadeDialogOpen() {
        return 58;
    }

    private static int getCujUserDialogOpen() {
        return 59;
    }

    private static int getCujTaskbarExpand() {
        return 60;
    }

    private static int getCujTaskbarCollapse() {
        return 61;
    }

    private static int getCujShadeClearAll() {
        return 62;
    }

    private static int getCujLauncherUnlockEntranceAnimation() {
        return 63;
    }

    private static int getCujLockscreenOcclusion() {
        return 64;
    }

    private static int getCujRecentsScrolling() {
        return 65;
    }

    private static int getCujLauncherAppSwipeToRecents() {
        return 66;
    }

    private static int getCujLauncherCloseAllAppsSwipe() {
        return 67;
    }

    private static int getCujLauncherCloseAllAppsToHome() {
        return 68;
    }

    private static int getCujImeInsetsAnimation() {
        return 69;
    }

    private static int getCujLockscreenClockMoveAnimation() {
        return 70;
    }

    private static int getCujLauncherOpenSearchResult() {
        return 71;
    }

    public boolean begin(View v, int cujType) {
        return this.mInteractionJankMonitor.begin(v, cujType);
    }

    public boolean begin(Configuration.Builder builder) {
        return this.mInteractionJankMonitor.begin(builder.mBuilder);
    }

    public boolean cancel(int cujType) {
        return this.mInteractionJankMonitor.cancel(cujType);
    }

    public boolean end(int cujType) {
        return this.mInteractionJankMonitor.end(cujType);
    }

    /* loaded from: classes.dex */
    public static class Configuration {

        /* loaded from: classes.dex */
        public static class Builder {
            private final InteractionJankMonitor.Configuration.Builder mBuilder;

            private Builder(InteractionJankMonitor.Configuration.Builder builder) {
                this.mBuilder = builder;
            }

            public static Builder withView(int cuj, View view) {
                return new Builder(InteractionJankMonitor.Configuration.Builder.withView(cuj, view));
            }

            public Builder setTag(String tag) {
                this.mBuilder.setTag(tag);
                return this;
            }

            public Builder setTimeout(long timeout) {
                this.mBuilder.setTimeout(timeout);
                return this;
            }
        }
    }
}
