package com.android.server.wm;

import android.app.compat.CompatChanges;
import android.view.InputApplicationHandle;
import android.view.InputWindowHandle;
import android.view.SurfaceControl;
import vendor.oplus.hardware.charger.ChargerErrorCode;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityRecordInputSink {
    static final long ENABLE_TOUCH_OPAQUE_ACTIVITIES = 194480991;
    private final ActivityRecord mActivityRecord;
    private InputWindowHandleWrapper mInputWindowHandleWrapper;
    private final boolean mIsCompatEnabled;
    private final String mName;
    private SurfaceControl mSurfaceControl;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecordInputSink(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        this.mActivityRecord = activityRecord;
        this.mIsCompatEnabled = CompatChanges.isChangeEnabled(ENABLE_TOUCH_OPAQUE_ACTIVITIES, activityRecord.getUid());
        this.mName = Integer.toHexString(System.identityHashCode(this)) + " ActivityRecordInputSink " + activityRecord.mActivityComponent.flattenToShortString();
        if (activityRecord2 != null) {
            activityRecord2.mAllowedTouchUid = activityRecord.getUid();
        }
    }

    public void applyChangesToSurfaceIfChanged(SurfaceControl.Transaction transaction) {
        InputWindowHandleWrapper inputWindowHandleWrapper = getInputWindowHandleWrapper();
        if (this.mSurfaceControl == null) {
            this.mSurfaceControl = createSurface(transaction);
        }
        if (inputWindowHandleWrapper.isChanged()) {
            inputWindowHandleWrapper.applyChangesToSurface(transaction, this.mSurfaceControl);
        }
    }

    private SurfaceControl createSurface(SurfaceControl.Transaction transaction) {
        SurfaceControl build = this.mActivityRecord.makeChildSurface(null).setName(this.mName).setHidden(false).setCallsite("ActivityRecordInputSink.createSurface").build();
        transaction.setLayer(build, ChargerErrorCode.ERR_FILE_FAILURE_ACCESS);
        return build;
    }

    private InputWindowHandleWrapper getInputWindowHandleWrapper() {
        if (this.mInputWindowHandleWrapper == null) {
            this.mInputWindowHandleWrapper = new InputWindowHandleWrapper(createInputWindowHandle());
        }
        ActivityRecord activityBelow = this.mActivityRecord.getTask() != null ? this.mActivityRecord.getTask().getActivityBelow(this.mActivityRecord) : null;
        if ((activityBelow != null && (activityBelow.mAllowedTouchUid == this.mActivityRecord.getUid() || activityBelow.isUid(this.mActivityRecord.getUid()))) || !this.mIsCompatEnabled || this.mActivityRecord.isInTransition()) {
            this.mInputWindowHandleWrapper.setInputConfigMasked(8, 8);
        } else {
            this.mInputWindowHandleWrapper.setInputConfigMasked(0, 8);
        }
        this.mInputWindowHandleWrapper.setDisplayId(this.mActivityRecord.getDisplayId());
        return this.mInputWindowHandleWrapper;
    }

    private InputWindowHandle createInputWindowHandle() {
        InputWindowHandle inputWindowHandle = new InputWindowHandle((InputApplicationHandle) null, this.mActivityRecord.getDisplayId());
        inputWindowHandle.replaceTouchableRegionWithCrop = true;
        inputWindowHandle.name = this.mName;
        inputWindowHandle.layoutParamsType = 2022;
        inputWindowHandle.ownerPid = WindowManagerService.MY_PID;
        inputWindowHandle.ownerUid = WindowManagerService.MY_UID;
        inputWindowHandle.inputConfig = 5;
        return inputWindowHandle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void releaseSurfaceControl() {
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl != null) {
            surfaceControl.release();
            this.mSurfaceControl = null;
        }
    }
}
