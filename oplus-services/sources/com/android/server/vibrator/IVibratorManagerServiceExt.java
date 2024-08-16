package com.android.server.vibrator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.CombinedVibration;
import android.os.IBinder;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.SparseArray;
import com.android.server.vibrator.Vibration;
import java.io.PrintWriter;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IVibratorManagerServiceExt {
    default boolean blockVibrationForApplicationLocked(String str, boolean z, IBinder iBinder) {
        return false;
    }

    default void cancelLinearMotorVibrator() {
    }

    default boolean cancelScreenOffReceiver(Context context, BroadcastReceiver broadcastReceiver) {
        return false;
    }

    default void cancelVibrate(int i, int i2, int i3, IBinder iBinder, VibrationStepConductor vibrationStepConductor) {
    }

    default boolean checkIfRichTapEffect(CombinedVibration combinedVibration) {
        return false;
    }

    default boolean checkIfRichTapParameter(CombinedVibration combinedVibration) {
        return false;
    }

    default boolean checkIfRichtapPatternHeEffect(CombinedVibration combinedVibration) {
        return false;
    }

    default CombinedVibration convertVibrationEffect(CombinedVibration combinedVibration, VibrationAttributes vibrationAttributes, int i, String str, String str2) {
        return combinedVibration;
    }

    default boolean disposeRichtapEffectParams(CombinedVibration combinedVibration) {
        return false;
    }

    default void dynamicallyConfigLogTag(PrintWriter printWriter, String[] strArr) {
    }

    default CombinedVibration fixVibrationEffect(CombinedVibration combinedVibration) {
        return combinedVibration;
    }

    default void fixVibrationEffectStrength(CombinedVibration combinedVibration, VibrationAttributes vibrationAttributes) {
    }

    default int getEffectDuration(int i) {
        return -1;
    }

    default int getEffectType(int i) {
        return -1;
    }

    default int getRingtoneEffectId(String str) {
        return -1;
    }

    default int getStrength() {
        return 0;
    }

    default int getVibratorStatus() {
        return 0;
    }

    default int getVibratorTouchStyle() {
        return 0;
    }

    default SparseArray<VibratorController> getVibrators() {
        return null;
    }

    default int getWaveformIndex(int i) {
        return -1;
    }

    default boolean ignoreVibrateForOneShotEffect(CombinedVibration combinedVibration) {
        return false;
    }

    default boolean ignoreVibrateForRichTapVibrationEffect(CombinedVibration combinedVibration) {
        return false;
    }

    default boolean ignoreVibrationForCamera(int i, String str, CombinedVibration combinedVibration) {
        return false;
    }

    default void init(Context context) {
    }

    default boolean isBlockedByApplicationLocked() {
        return false;
    }

    default boolean isNativeVibrationEffect(VibrationEffect vibrationEffect) {
        return false;
    }

    default boolean isNativeWaveformEffect(VibrationEffect vibrationEffect) {
        return false;
    }

    default void logVibratorIgnoreStatus(Vibration.Status status, String str) {
    }

    default void onNoteVibratorOnLocked(int i, long j) {
    }

    default void onSystemReady() {
    }

    default void setVibratorStrength(int i) {
    }

    default void setVibratorTouchStyle(int i) {
    }

    @Deprecated
    default boolean startCustomizeVibratorLocked(CombinedVibration combinedVibration, int i, int i2, VibrationStepConductor vibrationStepConductor) {
        return false;
    }

    @Deprecated
    default boolean startCustomizeVibratorLocked(CombinedVibration combinedVibration, int i, int i2, VibrationThread vibrationThread) {
        return false;
    }

    @Deprecated
    default boolean startRichTapVibratorLocked(CombinedVibration combinedVibration, int i, int i2, ArrayList<Vibrator> arrayList) {
        return false;
    }

    default boolean startRichTapVibratorLocked(HalVibration halVibration) {
        return false;
    }

    default void stopRichtapVibration() {
    }

    default CombinedVibration transferEffectToWaveform(CombinedVibration combinedVibration) {
        return combinedVibration;
    }

    default void updateVibrationAmplitude(int i, String str, float f) {
    }

    default void updateVibrator() {
    }

    default void updateVibratorStopStatus(boolean z) {
    }

    default void vibrate(int i, int i2, String str, CombinedVibration combinedVibration, VibrationAttributes vibrationAttributes, IBinder iBinder) {
    }

    default int fixupVibrationAttributes(VibrationAttributes vibrationAttributes, CombinedVibration combinedVibration) {
        return vibrationAttributes.getUsage();
    }
}
