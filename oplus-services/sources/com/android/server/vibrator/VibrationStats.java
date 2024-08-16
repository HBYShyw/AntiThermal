package com.android.server.vibrator;

import android.os.SystemClock;
import android.os.vibrator.PrebakedSegment;
import android.os.vibrator.PrimitiveSegment;
import android.os.vibrator.RampSegment;
import android.util.Slog;
import android.util.SparseBooleanArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.vibrator.Vibration;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class VibrationStats {
    static final String TAG = "VibrationStats";
    private long mEndTimeDebug;
    private long mEndUptimeMillis;
    private int mRepeatCount;
    private long mStartTimeDebug;
    private long mStartUptimeMillis;
    private int mVibrationCompositionTotalSize;
    private int mVibrationPwleTotalSize;
    private int mVibratorComposeCount;
    private int mVibratorComposePwleCount;
    private int mVibratorOffCount;
    private int mVibratorOnCount;
    private int mVibratorOnTotalDurationMillis;
    private int mVibratorPerformCount;
    private int mVibratorSetAmplitudeCount;
    private int mVibratorSetExternalControlCount;
    private SparseBooleanArray mVibratorEffectsUsed = new SparseBooleanArray();
    private SparseBooleanArray mVibratorPrimitivesUsed = new SparseBooleanArray();
    private final IVibrationStatsWrapper mVibrationSettingsWrapper = new VibrationStatsWrapper();
    private long mCreateUptimeMillis = SystemClock.uptimeMillis();
    private long mCreateTimeDebug = System.currentTimeMillis();
    private int mEndedByUid = -1;
    private int mEndedByUsage = -1;
    private int mInterruptedUsage = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getCreateUptimeMillis() {
        return this.mCreateUptimeMillis;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getStartUptimeMillis() {
        return this.mStartUptimeMillis;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getEndUptimeMillis() {
        return this.mEndUptimeMillis;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getCreateTimeDebug() {
        return this.mCreateTimeDebug;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getStartTimeDebug() {
        return this.mStartTimeDebug;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getEndTimeDebug() {
        return this.mEndTimeDebug;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getDurationDebug() {
        if (hasEnded()) {
            return this.mEndUptimeMillis - this.mCreateUptimeMillis;
        }
        return -1L;
    }

    boolean hasEnded() {
        return this.mEndUptimeMillis > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasStarted() {
        return this.mStartUptimeMillis > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportStarted() {
        if (hasEnded() || this.mStartUptimeMillis != 0) {
            return;
        }
        this.mStartUptimeMillis = SystemClock.uptimeMillis();
        this.mStartTimeDebug = System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean reportEnded(Vibration.CallerInfo callerInfo) {
        if (hasEnded()) {
            return false;
        }
        if (callerInfo != null) {
            this.mEndedByUid = callerInfo.uid;
            this.mEndedByUsage = callerInfo.attrs.getUsage();
        }
        this.mEndUptimeMillis = SystemClock.uptimeMillis();
        this.mEndTimeDebug = System.currentTimeMillis();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportInterruptedAnotherVibration(Vibration.CallerInfo callerInfo) {
        if (this.mInterruptedUsage < 0) {
            this.mInterruptedUsage = callerInfo.attrs.getUsage();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportRepetition(int i) {
        this.mRepeatCount += i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportVibratorOn(long j) {
        this.mVibratorOnCount++;
        if (j > 0) {
            this.mVibratorOnTotalDurationMillis += (int) j;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportVibratorOff() {
        this.mVibratorOffCount++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportSetAmplitude() {
        this.mVibratorSetAmplitudeCount++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportPerformEffect(long j, PrebakedSegment prebakedSegment) {
        this.mVibratorPerformCount++;
        if (j > 0) {
            this.mVibratorEffectsUsed.put(prebakedSegment.getEffectId(), true);
            this.mVibratorOnTotalDurationMillis += (int) j;
        } else {
            this.mVibratorEffectsUsed.put(prebakedSegment.getEffectId(), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportComposePrimitives(long j, PrimitiveSegment[] primitiveSegmentArr) {
        this.mVibratorComposeCount++;
        this.mVibrationCompositionTotalSize += primitiveSegmentArr.length;
        if (j > 0) {
            for (PrimitiveSegment primitiveSegment : primitiveSegmentArr) {
                j -= r5.getDelay();
                this.mVibratorPrimitivesUsed.put(primitiveSegment.getPrimitiveId(), true);
            }
            if (j > 0) {
                this.mVibratorOnTotalDurationMillis += (int) j;
                return;
            }
            return;
        }
        for (PrimitiveSegment primitiveSegment2 : primitiveSegmentArr) {
            this.mVibratorPrimitivesUsed.put(primitiveSegment2.getPrimitiveId(), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportComposePwle(long j, RampSegment[] rampSegmentArr) {
        this.mVibratorComposePwleCount++;
        this.mVibrationPwleTotalSize += rampSegmentArr.length;
        if (j > 0) {
            for (RampSegment rampSegment : rampSegmentArr) {
                if (rampSegment.getStartAmplitude() == 0.0f && rampSegment.getEndAmplitude() == 0.0f) {
                    j -= rampSegment.getDuration();
                }
            }
            if (j > 0) {
                this.mVibratorOnTotalDurationMillis += (int) j;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportSetExternalControl() {
        this.mVibratorSetExternalControlCount++;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static final class StatsInfo {
        public final int endLatencyMillis;
        public final boolean endedBySameUid;
        public final int endedByUsage;
        public final int halComposeCount;
        public final int halComposePwleCount;
        public final int halCompositionSize;
        public final int halOffCount;
        public final int halOnCount;
        public final int halPerformCount;
        public final int halPwleSize;
        public final int halSetAmplitudeCount;
        public final int halSetExternalControlCount;
        public final int[] halSupportedCompositionPrimitivesUsed;
        public final int[] halSupportedEffectsUsed;
        public final int[] halUnsupportedCompositionPrimitivesUsed;
        public final int[] halUnsupportedEffectsUsed;
        public final int interruptedUsage;
        private boolean mIsWritten;
        public final int repeatCount;
        public final int startLatencyMillis;
        public final int status;
        public final int totalDurationMillis;
        public final int uid;
        public final int usage;
        public final int vibrationType;
        public final int vibratorOnMillis;

        /* JADX INFO: Access modifiers changed from: package-private */
        public StatsInfo(int i, int i2, int i3, Vibration.Status status, VibrationStats vibrationStats, long j) {
            this.uid = i;
            this.vibrationType = i2;
            this.usage = i3;
            this.status = status.getProtoEnumValue();
            this.endedBySameUid = i == vibrationStats.mEndedByUid;
            this.endedByUsage = vibrationStats.mEndedByUsage;
            this.interruptedUsage = vibrationStats.mInterruptedUsage;
            this.repeatCount = vibrationStats.mRepeatCount;
            this.totalDurationMillis = (int) Math.max(0L, j - vibrationStats.mCreateUptimeMillis);
            this.vibratorOnMillis = vibrationStats.mVibratorOnTotalDurationMillis;
            if (vibrationStats.hasStarted()) {
                this.startLatencyMillis = (int) Math.max(0L, vibrationStats.mStartUptimeMillis - vibrationStats.mCreateUptimeMillis);
                this.endLatencyMillis = (int) Math.max(0L, j - vibrationStats.mEndUptimeMillis);
            } else {
                this.endLatencyMillis = 0;
                this.startLatencyMillis = 0;
            }
            this.halComposeCount = vibrationStats.mVibratorComposeCount;
            this.halComposePwleCount = vibrationStats.mVibratorComposePwleCount;
            this.halOnCount = vibrationStats.mVibratorOnCount;
            this.halOffCount = vibrationStats.mVibratorOffCount;
            this.halPerformCount = vibrationStats.mVibratorPerformCount;
            this.halSetAmplitudeCount = vibrationStats.mVibratorSetAmplitudeCount;
            this.halSetExternalControlCount = vibrationStats.mVibratorSetExternalControlCount;
            this.halCompositionSize = vibrationStats.mVibrationCompositionTotalSize;
            this.halPwleSize = vibrationStats.mVibrationPwleTotalSize;
            this.halSupportedCompositionPrimitivesUsed = filteredKeys(vibrationStats.mVibratorPrimitivesUsed, true);
            this.halSupportedEffectsUsed = filteredKeys(vibrationStats.mVibratorEffectsUsed, true);
            this.halUnsupportedCompositionPrimitivesUsed = filteredKeys(vibrationStats.mVibratorPrimitivesUsed, false);
            this.halUnsupportedEffectsUsed = filteredKeys(vibrationStats.mVibratorEffectsUsed, false);
        }

        @VisibleForTesting
        boolean isWritten() {
            return this.mIsWritten;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void writeVibrationReported() {
            if (this.mIsWritten) {
                Slog.wtf(VibrationStats.TAG, "Writing same vibration stats multiple times for uid=" + this.uid);
            }
            this.mIsWritten = true;
            FrameworkStatsLog.write_non_chained(487, this.uid, (String) null, this.vibrationType, this.usage, this.status, this.endedBySameUid, this.endedByUsage, this.interruptedUsage, this.repeatCount, this.totalDurationMillis, this.vibratorOnMillis, this.startLatencyMillis, this.endLatencyMillis, this.halComposeCount, this.halComposePwleCount, this.halOnCount, this.halOffCount, this.halPerformCount, this.halSetAmplitudeCount, this.halSetExternalControlCount, this.halSupportedCompositionPrimitivesUsed, this.halSupportedEffectsUsed, this.halUnsupportedCompositionPrimitivesUsed, this.halUnsupportedEffectsUsed, this.halCompositionSize, this.halPwleSize);
        }

        private static int[] filteredKeys(SparseBooleanArray sparseBooleanArray, boolean z) {
            int i = 0;
            for (int i2 = 0; i2 < sparseBooleanArray.size(); i2++) {
                if (sparseBooleanArray.valueAt(i2) == z) {
                    i++;
                }
            }
            if (i == 0) {
                return null;
            }
            int[] iArr = new int[i];
            int i3 = 0;
            for (int i4 = 0; i4 < sparseBooleanArray.size(); i4++) {
                if (sparseBooleanArray.valueAt(i4) == z) {
                    iArr[i3] = sparseBooleanArray.keyAt(i4);
                    i3++;
                }
            }
            return iArr;
        }
    }

    public IVibrationStatsWrapper getWrapper() {
        return this.mVibrationSettingsWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class VibrationStatsWrapper implements IVibrationStatsWrapper {
        private VibrationStatsWrapper() {
        }

        @Override // com.android.server.vibrator.IVibrationStatsWrapper
        public void reportPerformOplusPrebaked(int i, long j) {
            VibrationStats.this.mVibratorPerformCount++;
            VibrationStats.this.mVibratorOnTotalDurationMillis += (int) j;
        }

        @Override // com.android.server.vibrator.IVibrationStatsWrapper
        public void reportPerformRichtapPrebaked(int i, long j) {
            VibrationStats.this.mVibratorPerformCount++;
            VibrationStats.this.mVibratorOnTotalDurationMillis += (int) j;
        }
    }
}
