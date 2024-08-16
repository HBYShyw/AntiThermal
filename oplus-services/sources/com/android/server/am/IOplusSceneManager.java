package com.android.server.am;

import android.app.job.JobInfo;
import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.WorkSource;
import android.util.SparseArray;
import com.oplus.util.OplusPackageFreezeData;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusSceneManager extends IOplusCommonFeature {
    public static final String APP_SCENE_AUDIO_FOCUS = "audioFocus";
    public static final String APP_SCENE_AUDIO_RECORDER = "audioRecorder";
    public static final String APP_SCENE_DEFAULT_DIALER = "dialer";
    public static final String APP_SCENE_DEFAULT_INPUT = "input";
    public static final String APP_SCENE_DEFAULT_LAUNCHER = "launcher";
    public static final String APP_SCENE_DEFAULT_LIVE_WALLPAPER = "wallpaper";
    public static final String APP_SCENE_DEFAULT_SMS = "sms";
    public static final String APP_SCENE_GPS = "gps";
    public static final String APP_SCENE_PERSIST_NOTIFICATION = "persistNotification";
    public static final String APP_SCENE_SCREEN_RECORDER = "screenRecorder";
    public static final String APP_SCENE_SENSOR = "sensor";
    public static final String APP_SCENE_TOP_APP = "topApp";
    public static final String APP_SCENE_TRAFFIC = "traffic";
    public static final String APP_SCENE_VIDEO = "video";
    public static final String APP_SCENE_VISIBLE_WINDOW = "visibleWindow";
    public static final String APP_SCENE_VPN = "vpn";
    public static final String APP_SCENE_WIDGET = "widget";
    public static final String APP_STATE_CHANGE = "appState";
    public static final String TAG = "OplusSceneManager";
    public static final boolean LOG_DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final IOplusSceneManager DEFAULT = new IOplusSceneManager() { // from class: com.android.server.am.IOplusSceneManager.1
    };

    default boolean appAssociationCheck(int i, int i2) {
        return false;
    }

    default void bootCompleted() {
    }

    default boolean checkActivityIfRestricted(int i, String str, int i2, String str2, ComponentName componentName) {
        return false;
    }

    default boolean checkAlarmIfRestricted(int i, String str, String str2) {
        return false;
    }

    default boolean checkBumpServiceIfRestricted(int i, String str, String str2) {
        return false;
    }

    default boolean checkJobIfRestricted(int i, String str) {
        return false;
    }

    default boolean checkJobIfRestricted(int i, String str, JobInfo jobInfo) {
        return false;
    }

    default boolean checkProviderIfRestricted(int i, String str, int i2, String str2, String str3, String str4) {
        return false;
    }

    default void checkReStartServiceIfRestricted(int i, String str) {
    }

    default boolean checkReceiverIfRestricted(BroadcastRecord broadcastRecord, Object obj) {
        return false;
    }

    default boolean checkStartServiceIfRestricted(int i, int i2, String str, int i3, String str2, String str3, ComponentName componentName, String str4, boolean z) {
        return false;
    }

    default boolean checkSyncIfRestricted(int i, String str) {
        return false;
    }

    default void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    default ArrayList<Integer> getAudioFocusByAudioManager() {
        return null;
    }

    default ArrayList<Integer> getAudioFocusByHook() {
        return null;
    }

    default ArrayList<String> getAudioRecordList() {
        return null;
    }

    default List<Integer> getBluetoothList() {
        return null;
    }

    default List<OplusPackageFreezeData> getRunningProcesses() {
        return null;
    }

    default List<String> getTopLoadPidsInfos(int i) {
        return null;
    }

    default long getTotalCpuLoadPercent() {
        return 0L;
    }

    default SparseArray<Long> getTrafficBytesList(ArrayList<Integer> arrayList) {
        return null;
    }

    default SparseArray<Long> getTrafficPacketList(ArrayList<Integer> arrayList) {
        return null;
    }

    default ArrayList<Integer> getVideoListByHook() {
        return null;
    }

    default boolean isFloatWindowList(int i) {
        return false;
    }

    default boolean isVisibleWindow(int i) {
        return false;
    }

    default void noteAssociation(int i, int i2, boolean z) {
    }

    default void noteFgService(int i, String str, boolean z) {
    }

    default void noteIsolatedApp(int i, int i2, String str, boolean z) {
    }

    default void noteSysShutdown() {
    }

    default void noteSysStateChanged(int i, int i2, String str) {
    }

    default void noteWatchdog() {
    }

    default void onInit(IOplusActivityManagerServiceEx iOplusActivityManagerServiceEx) {
    }

    default void putAppScene(int i, String str, String str2) {
    }

    default void removeAppScene(int i, String str, String str2) {
    }

    default void requestServiceBinding(int i, String str, String str2, boolean z) {
    }

    default void resetAudioFocusInfo() {
    }

    default void resetVideoInfo() {
    }

    default void updateAppWidget(int i, int i2, String str, boolean z) {
    }

    default void updateAppWidgetTimeLocked(int i) {
    }

    default void updateAudioFocusInfo(int i, boolean z) {
    }

    default void updateNavigation(int i, String str, boolean z) {
    }

    default void updateNavigation(WorkSource workSource, WorkSource workSource2) {
    }

    default void updatePersistentNotification(int i, String str, String str2, boolean z) {
    }

    default void updatePersistentNotification(int i, String str, boolean z) {
    }

    default void updateSensorInfo(int i, int i2, boolean z) {
    }

    default boolean updateTrafficList(Bundle bundle) {
        return true;
    }

    default void updateVideoInfo(int i, boolean z) {
    }

    default void updateVisibleWindow(int i, int i2, int i3, int i4, boolean z, boolean z2) {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusSceneManager;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default ArrayList<String> getAppStateByUid(int i, String str) {
        return new ArrayList<>();
    }

    default SparseArray<ArrayList<String>> getAppStateByScene(String str) {
        return new SparseArray<>();
    }
}
