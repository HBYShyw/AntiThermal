package com.android.server.am;

import android.content.pm.UserInfo;
import android.os.Handler;
import android.util.ArraySet;
import android.util.SparseArray;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IUserControllerExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface IStaticExt {
        default Handler hookFgHandler(Handler handler) {
            return handler;
        }

        default Handler hookGetUiHandler(Handler.Callback callback) {
            return null;
        }
    }

    default boolean checkUserIfNeed(int i) {
        return false;
    }

    default void continueUserSwitch(UserState userState, int i, int i2) {
    }

    default int decreaseCountIfNeed(int i, int i2) {
        return i;
    }

    default void dispatchSwitch(UserState userState, int i, int i2) {
    }

    default void dispatchSwitchSendResult(long j, String str, int i, int i2) {
    }

    default boolean getWaitForKeyguardShown() {
        return true;
    }

    default void hookAgingUserBoot(int i) {
    }

    default void hookAgingUserUnlockedCompleted(int i) {
    }

    default Handler hookFgHandler(Handler handler) {
        return handler;
    }

    default Handler hookGetUiHandler(Handler.Callback callback) {
        return null;
    }

    default boolean hookHandleIncomingUser(int i, int i2) {
        return false;
    }

    default boolean hookShowUserSwitchDialog(UserInfo userInfo, UserInfo userInfo2) {
        return false;
    }

    default int increaseCountIfNeed(int i, int i2) {
        return i;
    }

    default boolean isMultiSystemUserId(int i) {
        return false;
    }

    default int modifyIfWorkProfileExist(int i, List<UserInfo> list) {
        return i;
    }

    default void onSystemReady() {
    }

    default void ormsSwitchUserBoost(int i) {
    }

    default void ormsUnlockUserBoost(int i) {
    }

    default void reUnlockMultiAppUser(int i) {
    }

    default void recordRootState() {
    }

    default void sendOplusBootCompleteBroadcast() {
    }

    default void sendOplusBootCompleteBroadcastAsUser(int i) {
    }

    default void setInjector(ActivityManagerService activityManagerService, Object obj, SparseArray<UserState> sparseArray) {
    }

    default void setUnlockedForDexopt() {
    }

    default void setWaitForKeyguardShown(boolean z) {
    }

    default void startFreezingScreenIfNeeded(int i, int i2) {
    }

    default void startFreezingScreenInStartUser(int i, int i2) {
    }

    default void startUserInternalEnter(boolean z, int i, int i2, long j, long j2, long j3, boolean z2) {
    }

    default void startUserInternalExit(long j, long j2, int i, int i2, long j3) {
    }

    default void stopFreezingScreenIfNeeded(int i, int i2) {
    }

    default void switchUser(int i, int i2) {
    }

    default void switchUser(boolean z, UserInfo userInfo, UserInfo userInfo2, int i) {
    }

    default void timeoutUserSwitch(ArraySet<String> arraySet, UserState userState, int i, int i2) {
    }

    default void triggerBootCompleteBroadcast(int i) {
    }

    default void userRemoved(int i) {
    }

    default void userStart(int i) {
    }
}
