package com.android.server;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.storage.DiskInfo;
import android.os.storage.VolumeInfo;
import android.util.Log;
import java.io.File;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusStorageManagerFeature extends IOplusCommonFeature {
    public static final IOplusStorageManagerFeature DEFAULT = new IOplusStorageManagerFeature() { // from class: com.android.server.IOplusStorageManagerFeature.1
    };
    public static final int H_FSTRIM = 4;
    public static final int H_SHUTDOWN = 3;
    public static final int H_VOLUME_MOUNT = 5;
    public static final String NAME = "IOplusStorageManagerFeature";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusStorageManagerFeature;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void initFillNode() {
        Log.d(NAME, "default InitFillNode");
    }

    default int getFragScore() {
        Log.d(NAME, "default getFragScore");
        return 0;
    }

    default long getLastCalcTime() {
        Log.d(NAME, "default getLastCalcTime");
        return 0L;
    }

    default void setLastCalcTime(long j) {
        Log.d(NAME, "default setLastCalcTime");
    }

    default void setStorageManagerHandler(Handler handler) {
        Log.d(NAME, "default setStorageManagerHandler");
    }

    default void setOplusStorageManagerCallback(IOplusStorageManagerCallback iOplusStorageManagerCallback) {
        Log.d(NAME, "default setOplusStorageManagerCallback");
    }

    default boolean shouldHandleKeyguardStateChange(boolean z) {
        Log.d(NAME, "default shouldHandleKeyguardStateChange");
        return false;
    }

    default boolean changeVolumeReadOnlyStateLocked(VolumeInfo volumeInfo, int i, int i2) {
        Log.d(NAME, "default changeVolumeReadOnlyStateLocked");
        return false;
    }

    default boolean shouldNotifyVolumeStateChanged(String str, int i, VolumeInfo volumeInfo) {
        Log.d(NAME, "default shouldNotifyVolumeStateChanged");
        return false;
    }

    default void onVolumeCheckingLocked(VolumeInfo volumeInfo, int i) {
        Log.d(NAME, "default onVolumeCheckingLocked");
    }

    default void onUnlockUser(int i) {
        Log.d(NAME, "default onUnlockUser");
    }

    default boolean onStorageManagerMessageHandle(Message message) {
        Log.d(NAME, "default onStorageManagerMessageHandle");
        return true;
    }

    default void onDiskStateChangedLocked(DiskInfo diskInfo, int i, int i2) {
        Log.d(NAME, "default onDiskStateChanged");
    }

    default boolean idleMaintable() {
        Log.d(NAME, "default idleMaintable");
        return true;
    }

    default boolean maintAborted() {
        Log.d(NAME, "default maintAborted");
        return false;
    }

    default boolean isDeviceIdle() {
        Log.d(NAME, "default isDeviceIdle");
        return false;
    }

    default void setMaintPrepared(boolean z) {
        Log.d(NAME, "default setMaintPrepared");
    }

    default void setMaintAborted(boolean z) {
        Log.d(NAME, "default setMaintAborted");
    }

    default BroadcastReceiver getScreenReceiver() {
        Log.d(NAME, "default getScreenReceiver");
        return null;
    }

    default void killInputMethods(Context context, int i, String str) {
        Log.d(NAME, "default killInputMethods");
    }

    default int getStorageData() {
        Log.d(NAME, "default getStorageData");
        return -1;
    }

    default int setSDLockPassword(String str) {
        Log.d(NAME, "default setSDLockPassword");
        return -1;
    }

    default int clearSDLockPassword() {
        Log.d(NAME, "default clearSDLockPassword");
        return -1;
    }

    default int unlockSDCard(String str) {
        Log.d(NAME, "default unlockSDCard");
        return -1;
    }

    default long getUnlockSdcardDeadline() {
        Log.d(NAME, "default getUnlockSdcardDeadline");
        return -1L;
    }

    default String getSDCardId() {
        Log.d(NAME, "default getSDCardId");
        return null;
    }

    default int getSDLockState() {
        Log.d(NAME, "default getSDLockState");
        return -1;
    }

    default void eraseSDLock() {
        Log.d(NAME, "default eraseSDLock");
    }

    default byte[] exportSensitveFileBeKey(int i, int i2) throws RemoteException {
        Log.d(NAME, "default exportSensitveFileBeKey");
        return null;
    }

    default void unlockAndExportAllSensitiveFileKey(int i, int i2, byte[] bArr, byte[] bArr2) {
        Log.d(NAME, "default unlockAndExportAllSensitiveFileKey");
    }

    default void clearSensitiveKey(boolean z) {
        Log.d(NAME, "default clearSensitiveKey");
    }

    default void addAuthResultInfo(int i, int i2, int i3, String str) {
        Log.d(NAME, "default addAuthResultInfo");
    }

    default Map<String, byte[]> encryptDek(byte[] bArr, int i, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        Log.d(NAME, "default encryptDek");
        return null;
    }

    default byte[] decryptDek(byte[] bArr, int i, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5) {
        Log.d(NAME, "default decryptDek");
        return null;
    }

    default Map<String, byte[]> initAeKek() {
        Log.d(NAME, "default initAeKek");
        return null;
    }

    default Map<String, byte[]> initBeKek() {
        Log.d(NAME, "default initBeKek");
        return null;
    }

    default void initOplusStorageFeature(IOplusStorageManagerCallback iOplusStorageManagerCallback, Handler handler) {
        Log.d(NAME, "default initOplusStorageFeature");
    }

    default long setLastMaintenance(long j, File file) {
        Log.d(NAME, "default setLastMaintenance");
        return 1L;
    }

    default void voldTBExt() {
        Log.d(NAME, "default voldTBExt");
    }

    default void schedulePreFstrim() {
        Log.d(NAME, "default schedulePreFstrim");
    }

    default ParcelFileDescriptor mountDfsFuse(String str, String str2) {
        Log.d(NAME, "default mountDfsFuse");
        return null;
    }

    default int umountDfsFuse(String str) {
        Log.d(NAME, "default umountDfsFuse");
        return -1;
    }

    default int configDfsFuse(String str, int i, int i2) {
        Log.d(NAME, "default configDfsFuse");
        return -1;
    }
}
