package com.android.server.wm;

import android.R;
import android.content.Context;
import android.util.ArrayMap;
import android.util.Pair;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class DeviceStateController {
    private final int mConcurrentDisplayDeviceState;
    private int mCurrentState;
    private final int[] mFoldedDeviceStates;
    private final int[] mHalfFoldedDeviceStates;
    private final boolean mMatchBuiltInDisplayOrientationToDefaultDisplay;
    private final int[] mOpenDeviceStates;
    private final int[] mRearDisplayDeviceStates;
    private final int[] mReverseRotationAroundZAxisStates;
    private final WindowManagerGlobalLock mWmLock;

    @GuardedBy({"mWmLock"})
    @VisibleForTesting
    final Map<Consumer<DeviceState>, Executor> mDeviceStateCallbacks = new ArrayMap();
    private DeviceState mCurrentDeviceState = DeviceState.UNKNOWN;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public enum DeviceState {
        UNKNOWN,
        OPEN,
        FOLDED,
        HALF_FOLDED,
        REAR,
        CONCURRENT
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeviceStateController(Context context, WindowManagerGlobalLock windowManagerGlobalLock) {
        this.mWmLock = windowManagerGlobalLock;
        this.mOpenDeviceStates = context.getResources().getIntArray(R.array.preloaded_drawables);
        this.mHalfFoldedDeviceStates = context.getResources().getIntArray(R.array.config_system_condition_providers);
        this.mFoldedDeviceStates = context.getResources().getIntArray(R.array.config_screenThresholdLevels);
        this.mRearDisplayDeviceStates = context.getResources().getIntArray(R.array.resolver_target_actions_pin);
        this.mConcurrentDisplayDeviceState = context.getResources().getInteger(R.integer.config_immersive_mode_confirmation_panic);
        this.mReverseRotationAroundZAxisStates = context.getResources().getIntArray(R.array.config_emergency_iso_country_codes);
        this.mMatchBuiltInDisplayOrientationToDefaultDisplay = context.getResources().getBoolean(17891757);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerDeviceStateCallback(Consumer<DeviceState> consumer, Executor executor) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mWmLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mDeviceStateCallbacks.put(consumer, executor);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterDeviceStateCallback(Consumer<DeviceState> consumer) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mWmLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mDeviceStateCallbacks.remove(consumer);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldReverseRotationDirectionAroundZAxis(DisplayContent displayContent) {
        if (displayContent.isDefaultDisplay) {
            return ArrayUtils.contains(this.mReverseRotationAroundZAxisStates, this.mCurrentState);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldMatchBuiltInDisplayOrientationToReverseDefaultDisplay() {
        return this.mMatchBuiltInDisplayOrientationToDefaultDisplay;
    }

    public void onDeviceStateReceivedByDisplayManager(int i) {
        final DeviceState deviceState;
        this.mCurrentState = i;
        if (ArrayUtils.contains(this.mHalfFoldedDeviceStates, i)) {
            deviceState = DeviceState.HALF_FOLDED;
        } else if (ArrayUtils.contains(this.mFoldedDeviceStates, i)) {
            deviceState = DeviceState.FOLDED;
        } else if (ArrayUtils.contains(this.mRearDisplayDeviceStates, i)) {
            deviceState = DeviceState.REAR;
        } else if (ArrayUtils.contains(this.mOpenDeviceStates, i)) {
            deviceState = DeviceState.OPEN;
        } else if (i == this.mConcurrentDisplayDeviceState) {
            deviceState = DeviceState.CONCURRENT;
        } else {
            deviceState = DeviceState.UNKNOWN;
        }
        DeviceState deviceState2 = this.mCurrentDeviceState;
        if (deviceState2 == null || !deviceState2.equals(deviceState)) {
            this.mCurrentDeviceState = deviceState;
            List<Pair<Consumer<DeviceState>, Executor>> copyDeviceStateCallbacks = copyDeviceStateCallbacks();
            for (int i2 = 0; i2 < copyDeviceStateCallbacks.size(); i2++) {
                final Pair<Consumer<DeviceState>, Executor> pair = copyDeviceStateCallbacks.get(i2);
                ((Executor) pair.second).execute(new Runnable() { // from class: com.android.server.wm.DeviceStateController$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        DeviceStateController.lambda$onDeviceStateReceivedByDisplayManager$0(pair, deviceState);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onDeviceStateReceivedByDisplayManager$0(Pair pair, DeviceState deviceState) {
        ((Consumer) pair.first).accept(deviceState);
    }

    @VisibleForTesting
    List<Pair<Consumer<DeviceState>, Executor>> copyDeviceStateCallbacks() {
        final ArrayList arrayList = new ArrayList();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mWmLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mDeviceStateCallbacks.forEach(new BiConsumer() { // from class: com.android.server.wm.DeviceStateController$$ExternalSyntheticLambda1
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        DeviceStateController.lambda$copyDeviceStateCallbacks$1(arrayList, (Consumer) obj, (Executor) obj2);
                    }
                });
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$copyDeviceStateCallbacks$1(List list, Consumer consumer, Executor executor) {
        list.add(new Pair(consumer, executor));
    }
}
