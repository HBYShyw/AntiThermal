package com.android.server.notification;

import android.app.INotificationChannelExt;
import android.content.Context;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.IOException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPreferencesHelperExt {
    public static final int MAX_MESSGAES = 1000;

    default boolean canShowBanner(String str, int i) {
        return false;
    }

    default boolean canShowIcon(String str, int i) {
        return true;
    }

    default boolean getAppBanner(String str, int i) {
        return true;
    }

    default int getAppVisibility(String str, int i) {
        return 1;
    }

    default int getBadgeOption(String str, int i) {
        return 2;
    }

    default boolean getFold(String str, int i) {
        return false;
    }

    default int getMaxMessages(String str, int i) {
        return 1000;
    }

    default String getMigMappingPkgName(Context context, boolean z, String str) {
        return str;
    }

    default int getMigMappingPkgUid(Context context, String str, int i) {
        return i;
    }

    default int getStowOption(String str, int i) {
        return 0;
    }

    default boolean getSupportNumBadge(String str, int i) {
        return false;
    }

    default boolean isAppRingtonePermissionGranted(String str, int i) {
        return false;
    }

    default boolean isAppVibrationPermissionGranted(String str, int i) {
        return false;
    }

    default boolean isChangeAbleShowIcon(String str, int i) {
        return true;
    }

    default boolean isChangeableFold(String str, int i) {
        return true;
    }

    default boolean isOpush(String str, int i) {
        return false;
    }

    default boolean isPkgChanged() {
        return false;
    }

    default boolean isPreferencesExtDefault(IPackagePreferencesExt iPackagePreferencesExt) {
        return false;
    }

    default void readXml(IPackagePreferencesExt iPackagePreferencesExt, TypedXmlPullParser typedXmlPullParser) {
    }

    default void setAppBanner(String str, int i, boolean z) {
    }

    default void setAppRingtonePermission(String str, int i, boolean z) {
    }

    default void setAppVibrationPermission(String str, int i, boolean z) {
    }

    default void setAppVisibility(String str, int i, int i2) {
    }

    default void setBadgeOption(String str, int i, int i2) {
    }

    default void setChangeableFold(String str, int i, boolean z) {
    }

    default void setChangeableShowIcon(String str, int i, boolean z) {
    }

    default void setFold(String str, int i, boolean z) {
    }

    default void setMaxMessages(String str, int i, int i2) {
    }

    default void setOpush(String str, int i, boolean z) {
    }

    default void setPkgChanged(boolean z) {
    }

    default void setShowBanner(String str, int i, boolean z) {
    }

    default void setShowIcon(String str, int i, boolean z) {
    }

    default void setStowOption(String str, int i, int i2) {
    }

    default void setSupportNumBadge(String str, int i, boolean z) {
    }

    default void updateNotificationChannel(INotificationChannelExt iNotificationChannelExt, IPackagePreferencesExt iPackagePreferencesExt) {
    }

    default void writeAttrbute(TypedXmlSerializer typedXmlSerializer, IPackagePreferencesExt iPackagePreferencesExt) throws IOException {
    }
}
