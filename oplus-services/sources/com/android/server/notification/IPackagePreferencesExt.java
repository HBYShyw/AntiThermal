package com.android.server.notification;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPackagePreferencesExt {
    public static final int APP_VISIBILITY = 1;
    public static final int BADEGE_OPTION = 2;
    public static final int MAX_MESSAGES = 1000;
    public static final int STOW_OPTION = 0;

    default boolean getAppBanner() {
        return true;
    }

    default int getAppVisibility() {
        return 1;
    }

    default int getBadgeOption() {
        return 2;
    }

    default boolean getChangeableFold() {
        return true;
    }

    default boolean getChangeableShowIcon() {
        return true;
    }

    default boolean getFold() {
        return false;
    }

    default int getMaxMessages() {
        return 1000;
    }

    default boolean getOpush() {
        return false;
    }

    default boolean getShowBanner() {
        return false;
    }

    default boolean getShowIcon() {
        return true;
    }

    default int getStowOption() {
        return 0;
    }

    default boolean getSupportNumBadge() {
        return false;
    }

    default boolean isAppRingtonePermissionGranted() {
        return false;
    }

    default boolean isAppVibrationPermissionGranted() {
        return false;
    }

    default void reset() {
    }

    default void setAppBanner(boolean z) {
    }

    default void setAppRingtonePermission(boolean z) {
    }

    default void setAppVibrationPermission(boolean z) {
    }

    default void setAppVisibility(int i) {
    }

    default void setBadgeOption(int i) {
    }

    default void setChangeableFold(boolean z) {
    }

    default void setChangeableShowIcon(boolean z) {
    }

    default void setFold(boolean z) {
    }

    default void setMaxMessages(int i) {
    }

    default void setOpush(boolean z) {
    }

    default void setShowBanner(boolean z) {
    }

    default void setShowIcon(boolean z) {
    }

    default void setStowOption(int i) {
    }

    default void setSupportNumBadge(boolean z) {
    }
}
