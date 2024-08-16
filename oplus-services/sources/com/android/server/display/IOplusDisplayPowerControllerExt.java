package com.android.server.display;

import android.animation.Animator;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.display.BrightnessInfo;
import android.hardware.display.DisplayManagerInternal;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import com.android.server.display.AutomaticBrightnessController;
import com.android.server.display.RampAnimator;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusDisplayPowerControllerExt {
    public static final int ADJUSTMENT_GALLERY_IN = 16385;
    public static final int ADJUSTMENT_GALLERY_OUT = 32769;
    public static final int MAX_BRIGHTNESS = 8191;

    default void animateLongTakeStateChange(DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, int i, int i2, int i3) {
    }

    default void animateScreenBrightness(RampAnimator.DualRampAnimator<DisplayPowerState> dualRampAnimator, float f, float f2, float f3, DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, DisplayPowerState displayPowerState) {
    }

    default boolean applyOplusProximitySensorLocked(DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, int i, boolean z, boolean z2, boolean z3, int i2, boolean z4, int i3) {
        return false;
    }

    default int applydimmingbrightness(int i) {
        return i;
    }

    default void blockScreenOnByBiometrics(String str) {
    }

    default float caculateautoBrightnessAdjustment(float f) {
        return f;
    }

    default void cancelPwkBecauseProximity() {
    }

    default void configure(boolean z, boolean z2, float f, boolean z3, boolean z4, int i, int i2) {
    }

    default void dismissEglContext(ColorFade colorFade, int i) {
    }

    default void dump(PrintWriter printWriter) {
    }

    default int getAIBrightness() {
        return 0;
    }

    default int getAdjustmentGalleryIn() {
        return ADJUSTMENT_GALLERY_IN;
    }

    default int getAdjustmentGalleryOut() {
        return ADJUSTMENT_GALLERY_OUT;
    }

    default float getAdjustmentSetting(Context context, int i, int i2, float f) {
        return 0.0f;
    }

    default int getAutomaticScreenBrightness() {
        return 0;
    }

    default float getBrightnessByNit(float f) {
        return -1.0f;
    }

    default boolean getBrightnessInfoByAccessibility() {
        return false;
    }

    default float getCurrentScreenBrightnessSetting(int i, float f) {
        return 0.0f;
    }

    default int getDozeScreenStateWhenUnKnown(int i, int i2) {
        return 3;
    }

    default int getGlobalHbmSellMode() {
        return -1;
    }

    default int getLastBrightnessMode() {
        return 0;
    }

    default float getLowPowerModeBtnExp(float f, float f2) {
        return f * f2;
    }

    default int getMaximumScreenBrightnessSetting() {
        return 0;
    }

    default float getMinimumScreenBrightnessSetting(float f) {
        return f;
    }

    default float getNitByBrightness(float f) {
        return -1.0f;
    }

    default IBinder getPhysicalDisplayToken(long j) {
        return null;
    }

    default float getRealBrightness(float f) {
        return f;
    }

    default float getScreenNormalMaxBrightness() {
        return 0.0f;
    }

    default int getScreenState() {
        return -1;
    }

    default boolean getUseProximityForceSuspendState(int i) {
        return false;
    }

    default void handleBrightnessTotalRateType(int i) {
    }

    default void handleDisplayChanged(boolean z, boolean z2, int i, int i2, int i3, int i4) {
    }

    default void handlePwkMonitorForTheia(int i, boolean z) {
    }

    default float handleScreenBrightnessSettingChange(float f) {
        return f;
    }

    default float handleSetTemporaryBrightnessMessage(float f, String str, int i) {
        return f;
    }

    default boolean hasBiometricsBlockedReason(String str) {
        return false;
    }

    default boolean hasRemapDisable() {
        return false;
    }

    default void init(Context context, int i) {
    }

    default IColorAutomaticBrightnessController initAutomaticBrightnessController(AutomaticBrightnessController.Callbacks callbacks, Looper looper, SensorManager sensorManager, Sensor sensor, BrightnessMappingStrategy brightnessMappingStrategy, float f, int i, long j) {
        return null;
    }

    default void initParameters(Handler handler) {
    }

    default boolean interceptProximityEvent() {
        return false;
    }

    default boolean isBlockDisplayByBiometrics() {
        return false;
    }

    default boolean isBlockScreenOnByBiometrics() {
        return false;
    }

    default boolean isBlockedBySideFingerprint() {
        return false;
    }

    default boolean isDCMode() {
        return false;
    }

    default boolean isFolding() {
        return false;
    }

    default boolean isGalleryBrightnessEnhanceSupport() {
        return false;
    }

    default boolean isIgnoreProximity() {
        return false;
    }

    default boolean isPrimaryDisplay(String str) {
        return false;
    }

    default boolean isSilentRebootFirstGoToSleep(int i) {
        return false;
    }

    default boolean isSpecialAdj(float f) {
        return false;
    }

    default boolean isUseProximityForceSuspendStateChanged(int i) {
        return false;
    }

    default void notifyBrightnessChange(float f) {
    }

    default boolean notifyBrightnessSetting(int i, boolean z, boolean z2, int i2, boolean z3) {
        return z3;
    }

    default void onAnimationChanged(Animator animator, int i) {
    }

    default void onChange(Context context, int i, boolean z, Uri uri, int i2) {
    }

    default void onDisplayControllerHandler(Message message, Handler handler) {
    }

    default void onProximityDebounceTimeArrived(int i) {
    }

    default boolean onSwitchUser(int i) {
        return false;
    }

    default void onUpdatePowerState(int i, int i2, int i3) {
    }

    default void pokeDynamicVsyncAnimation(int i, String str) {
    }

    default boolean postBrightnessChanged(float f, float f2) {
        return true;
    }

    default int putBrightnessTodatabase(int i) {
        return i;
    }

    default void recoverOriginRateType() {
    }

    default void registerEdrListener(IBinder iBinder) {
    }

    default boolean registerPSensor(SensorManager sensorManager, SensorEventListener sensorEventListener, int i, Handler handler) {
        return false;
    }

    default void removeFaceBlockReasonFromBlockReasonList() {
    }

    default void removeMessageWhenScreenOn(Handler handler, int i) {
    }

    default boolean sendMessageWhenScreenOnUnblocker(Handler handler, Message message) {
        return false;
    }

    default void setAnimating(boolean z, boolean z2) {
    }

    default void setAuxFakeLux(boolean z, int i) {
    }

    default boolean setBrightnessByAccessibility() {
        return false;
    }

    default void setBrightnessExt(float f) {
    }

    default void setByUser(boolean z) {
    }

    default void setDCMode() {
    }

    default void setDimRateType() {
    }

    default void setDisplayPowerControlHandler(Handler handler) {
    }

    default void setDisplayPowerController(DisplayPowerController displayPowerController) {
    }

    default void setDuration(int i) {
    }

    default void setFakeLux(boolean z, int i) {
    }

    default void setGlobalHbmSellMode() {
    }

    default void setHDRAnimatingState(boolean z) {
    }

    default void setLoggingEnabled(boolean z) {
    }

    default void setLowPowerAnimatingState(boolean z) {
    }

    default void setMainFakeLux(boolean z, int i) {
    }

    default void setOplusDisplayPowerControllerCallback(DisplayManagerInternal.DisplayPowerCallbacks displayPowerCallbacks) {
    }

    default void setPowerRequestPolicy(int i) {
    }

    default void setPowerState(DisplayPowerState displayPowerState) {
    }

    default void setQuickDarkToBrightStatus(DisplayManagerInternal.DisplayPowerRequest displayPowerRequest) {
    }

    default void setReason(int i) {
    }

    default void setRmMode() {
    }

    default void setSavePowerMode(int i) {
    }

    default void setScreenState(int i) {
    }

    default void setScreenStateExt(int i, DisplayPowerState displayPowerState, DisplayManagerInternal.DisplayPowerRequest displayPowerRequest) {
    }

    default void setSpecBrightness(int i, String str, int i2, int i3) {
    }

    default void setTemporaryAutoBrightnessAdjustment(float f) {
    }

    default void setUniqueDisplayId(boolean z, String str) {
    }

    default void setUseProximityForceSuspend(boolean z, int i) {
    }

    default void setWinOverride(boolean z) {
    }

    default boolean shouldIgnoreDoze(int i) {
        return false;
    }

    default void stop(boolean z) {
    }

    default void unblockDisplayReady() {
    }

    default void unblockScreenOnByBiometrics(String str) {
    }

    default boolean updateAutoBrightnessEnabled(LogicalDisplayMapper logicalDisplayMapper, boolean z, int i, DisplayManagerInternal.DisplayPowerRequest displayPowerRequest) {
        return z;
    }

    default void updateBrightnessAnimationStatus(DisplayPowerState displayPowerState, int i, LogicalDisplay logicalDisplay, int i2) {
    }

    default void updateBrightnessTotalRateType(int i) {
    }

    default void updateFpsWhenDcChange(boolean z) {
    }

    default void updateScreenBrightnessOverride(float f, boolean z) {
    }

    default boolean useSoftwareAutoBrightnessConfigInOtherDisplay(int i) {
        return false;
    }

    default BrightnessInfo getAccessibilityBrightnessInfo(float f) {
        return new BrightnessInfo(f, f, 0.0f, 8191.0f, 0, 0.0f, 0);
    }
}
