package com.android.server.soundtrigger;

import com.android.internal.annotations.GuardedBy;
import com.android.server.soundtrigger.DeviceStateHandler;
import com.android.server.soundtrigger.PhoneCallStateHandler;
import com.android.server.utils.EventLogger;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class DeviceStateHandler implements PhoneCallStateHandler.Callback {
    public static final long CALL_INACTIVE_MSG_DELAY_MS = 1000;
    private final Executor mCallbackExecutor;
    private final EventLogger mEventLogger;
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    SoundTriggerDeviceState mSoundTriggerDeviceState = SoundTriggerDeviceState.ENABLE;

    @GuardedBy({"mLock"})
    private int mSoundTriggerPowerSaveMode = 0;

    @GuardedBy({"mLock"})
    private boolean mIsPhoneCallOngoing = false;

    @GuardedBy({"mLock"})
    private NotificationTask mPhoneStateChangePendingNotify = null;
    private Set<DeviceStateListener> mCallbackSet = ConcurrentHashMap.newKeySet(4);
    private final Executor mDelayedNotificationExecutor = Executors.newSingleThreadExecutor();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface DeviceStateListener {
        void onSoundTriggerDeviceStateUpdate(SoundTriggerDeviceState soundTriggerDeviceState);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public enum SoundTriggerDeviceState {
        DISABLE,
        CRITICAL,
        ENABLE
    }

    public void onPowerModeChanged(int i) {
        this.mEventLogger.enqueue(new SoundTriggerPowerEvent(i));
        synchronized (this.mLock) {
            if (i == this.mSoundTriggerPowerSaveMode) {
                return;
            }
            this.mSoundTriggerPowerSaveMode = i;
            evaluateStateChange();
        }
    }

    @Override // com.android.server.soundtrigger.PhoneCallStateHandler.Callback
    public void onPhoneCallStateChanged(boolean z) {
        this.mEventLogger.enqueue(new PhoneCallEvent(z));
        synchronized (this.mLock) {
            if (this.mIsPhoneCallOngoing == z) {
                return;
            }
            NotificationTask notificationTask = this.mPhoneStateChangePendingNotify;
            if (notificationTask != null) {
                notificationTask.cancel();
                this.mPhoneStateChangePendingNotify = null;
            }
            this.mIsPhoneCallOngoing = z;
            if (!z) {
                NotificationTask notificationTask2 = new NotificationTask(new Runnable() { // from class: com.android.server.soundtrigger.DeviceStateHandler.1
                    @Override // java.lang.Runnable
                    public void run() {
                        synchronized (DeviceStateHandler.this.mLock) {
                            if (DeviceStateHandler.this.mPhoneStateChangePendingNotify != null && DeviceStateHandler.this.mPhoneStateChangePendingNotify.runnableEquals(this)) {
                                DeviceStateHandler.this.mPhoneStateChangePendingNotify = null;
                                DeviceStateHandler.this.evaluateStateChange();
                            }
                        }
                    }
                }, 1000L);
                this.mPhoneStateChangePendingNotify = notificationTask2;
                this.mDelayedNotificationExecutor.execute(notificationTask2);
            } else {
                evaluateStateChange();
            }
        }
    }

    public DeviceStateHandler(Executor executor, EventLogger eventLogger) {
        Objects.requireNonNull(executor);
        this.mCallbackExecutor = executor;
        Objects.requireNonNull(eventLogger);
        this.mEventLogger = eventLogger;
    }

    public SoundTriggerDeviceState getDeviceState() {
        SoundTriggerDeviceState soundTriggerDeviceState;
        synchronized (this.mLock) {
            soundTriggerDeviceState = this.mSoundTriggerDeviceState;
        }
        return soundTriggerDeviceState;
    }

    public void registerListener(final DeviceStateListener deviceStateListener) {
        final SoundTriggerDeviceState deviceState = getDeviceState();
        this.mCallbackExecutor.execute(new Runnable() { // from class: com.android.server.soundtrigger.DeviceStateHandler$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                DeviceStateHandler.DeviceStateListener.this.onSoundTriggerDeviceStateUpdate(deviceState);
            }
        });
        this.mCallbackSet.add(deviceStateListener);
    }

    public void unregisterListener(DeviceStateListener deviceStateListener) {
        this.mCallbackSet.remove(deviceStateListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        synchronized (this.mLock) {
            printWriter.println("DeviceState: " + this.mSoundTriggerDeviceState.name());
            printWriter.println("PhoneState: " + this.mIsPhoneCallOngoing);
            printWriter.println("PowerSaveMode: " + this.mSoundTriggerPowerSaveMode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void evaluateStateChange() {
        SoundTriggerDeviceState computeState = computeState();
        if (this.mPhoneStateChangePendingNotify != null || this.mSoundTriggerDeviceState == computeState) {
            return;
        }
        this.mSoundTriggerDeviceState = computeState;
        this.mEventLogger.enqueue(new DeviceStateEvent(this.mSoundTriggerDeviceState));
        final SoundTriggerDeviceState soundTriggerDeviceState = this.mSoundTriggerDeviceState;
        for (final DeviceStateListener deviceStateListener : this.mCallbackSet) {
            this.mCallbackExecutor.execute(new Runnable() { // from class: com.android.server.soundtrigger.DeviceStateHandler$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DeviceStateHandler.DeviceStateListener.this.onSoundTriggerDeviceStateUpdate(soundTriggerDeviceState);
                }
            });
        }
    }

    @GuardedBy({"mLock"})
    private SoundTriggerDeviceState computeState() {
        if (this.mIsPhoneCallOngoing) {
            return SoundTriggerDeviceState.DISABLE;
        }
        int i = this.mSoundTriggerPowerSaveMode;
        if (i == 0) {
            return SoundTriggerDeviceState.ENABLE;
        }
        if (i == 1) {
            return SoundTriggerDeviceState.CRITICAL;
        }
        if (i == 2) {
            return SoundTriggerDeviceState.DISABLE;
        }
        throw new IllegalStateException("Received unexpected power state code" + this.mSoundTriggerPowerSaveMode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class NotificationTask implements Runnable {
        private final CountDownLatch mCancelLatch = new CountDownLatch(1);
        private final Runnable mRunnable;
        private final long mWaitInMillis;

        NotificationTask(Runnable runnable, long j) {
            this.mRunnable = runnable;
            this.mWaitInMillis = j;
        }

        void cancel() {
            this.mCancelLatch.countDown();
        }

        boolean runnableEquals(Runnable runnable) {
            return this.mRunnable == runnable;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (this.mCancelLatch.await(this.mWaitInMillis, TimeUnit.MILLISECONDS)) {
                    return;
                }
                this.mRunnable.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new AssertionError("Unexpected InterruptedException", e);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class PhoneCallEvent extends EventLogger.Event {
        final boolean mIsInPhoneCall;

        PhoneCallEvent(boolean z) {
            this.mIsInPhoneCall = z;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public String eventToString() {
            return "PhoneCallChange - inPhoneCall: " + this.mIsInPhoneCall;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class SoundTriggerPowerEvent extends EventLogger.Event {
        final int mSoundTriggerPowerState;

        SoundTriggerPowerEvent(int i) {
            this.mSoundTriggerPowerState = i;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public String eventToString() {
            return "SoundTriggerPowerChange: " + stateToString();
        }

        private String stateToString() {
            int i = this.mSoundTriggerPowerState;
            if (i == 0) {
                return "All enabled";
            }
            if (i == 1) {
                return "Critical only";
            }
            if (i == 2) {
                return "All disabled";
            }
            return "Unknown power state: " + this.mSoundTriggerPowerState;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class DeviceStateEvent extends EventLogger.Event {
        final SoundTriggerDeviceState mSoundTriggerDeviceState;

        DeviceStateEvent(SoundTriggerDeviceState soundTriggerDeviceState) {
            this.mSoundTriggerDeviceState = soundTriggerDeviceState;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public String eventToString() {
            return "DeviceStateChange: " + this.mSoundTriggerDeviceState.name();
        }
    }
}
