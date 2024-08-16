package com.android.server.am;

import android.util.TimeUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.am.CachedAppOptimizer;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ProcessCachedOptimizerRecord {

    @VisibleForTesting
    static final String IS_FROZEN = "isFrozen";
    private final ProcessRecord mApp;

    @GuardedBy({"mProcLock"})
    private long mEarliestFreezableTimeMillis;

    @GuardedBy({"mProcLock"})
    private boolean mForceCompact;

    @GuardedBy({"mProcLock"})
    private boolean mFreezeExempt;

    @GuardedBy({"mProcLock"})
    private boolean mFreezeSticky;

    @GuardedBy({"mProcLock"})
    private long mFreezeUnfreezeTime;

    @GuardedBy({"mProcLock"})
    boolean mFreezerOverride;

    @GuardedBy({"mProcLock"})
    private boolean mFrozen;
    private boolean mHasCollectedFrozenPSS;

    @GuardedBy({"mProcLock"})
    private CachedAppOptimizer.CompactProfile mLastCompactProfile;

    @GuardedBy({"mProcLock"})
    private long mLastCompactTime;

    @GuardedBy({"mProcLock"})
    private int mLastOomAdjChangeReason;

    @GuardedBy({"mProcLock"})
    private long mLastUsedTimeout;

    @GuardedBy({"mProcLock"})
    private boolean mPendingCompact;

    @GuardedBy({"mProcLock"})
    private boolean mPendingFreeze;
    private final ActivityManagerGlobalLock mProcLock;

    @GuardedBy({"mProcLock"})
    private CachedAppOptimizer.CompactProfile mReqCompactProfile;

    @GuardedBy({"mProcLock"})
    private CachedAppOptimizer.CompactSource mReqCompactSource;

    @GuardedBy({"mProcLock"})
    private boolean mShouldNotFreeze;

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public long getLastCompactTime() {
        return this.mLastCompactTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setLastCompactTime(long j) {
        this.mLastCompactTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public CachedAppOptimizer.CompactProfile getReqCompactProfile() {
        return this.mReqCompactProfile;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setReqCompactProfile(CachedAppOptimizer.CompactProfile compactProfile) {
        this.mReqCompactProfile = compactProfile;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public CachedAppOptimizer.CompactSource getReqCompactSource() {
        return this.mReqCompactSource;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setReqCompactSource(CachedAppOptimizer.CompactSource compactSource) {
        this.mReqCompactSource = compactSource;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setLastOomAdjChangeReason(int i) {
        this.mLastOomAdjChangeReason = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public int getLastOomAdjChangeReason() {
        return this.mLastOomAdjChangeReason;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public CachedAppOptimizer.CompactProfile getLastCompactProfile() {
        if (this.mLastCompactProfile == null) {
            this.mLastCompactProfile = CachedAppOptimizer.CompactProfile.SOME;
        }
        return this.mLastCompactProfile;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setLastCompactProfile(CachedAppOptimizer.CompactProfile compactProfile) {
        this.mLastCompactProfile = compactProfile;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean hasPendingCompact() {
        return this.mPendingCompact;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setHasPendingCompact(boolean z) {
        this.mPendingCompact = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean isForceCompact() {
        return this.mForceCompact;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setForceCompact(boolean z) {
        this.mForceCompact = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean isFrozen() {
        return this.mFrozen;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setFrozen(boolean z) {
        this.mFrozen = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setFreezeSticky(boolean z) {
        this.mFreezeSticky = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean isFreezeSticky() {
        return this.mFreezeSticky;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean skipPSSCollectionBecauseFrozen() {
        boolean z = this.mHasCollectedFrozenPSS;
        if (!this.mFrozen) {
            return false;
        }
        this.mHasCollectedFrozenPSS = true;
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHasCollectedFrozenPSS(boolean z) {
        this.mHasCollectedFrozenPSS = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean hasFreezerOverride() {
        return this.mFreezerOverride;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setFreezerOverride(boolean z) {
        this.mFreezerOverride = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public long getFreezeUnfreezeTime() {
        return this.mFreezeUnfreezeTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setFreezeUnfreezeTime(long j) {
        this.mFreezeUnfreezeTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean shouldNotFreeze() {
        return this.mShouldNotFreeze;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setShouldNotFreeze(boolean z) {
        this.mShouldNotFreeze = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public long getEarliestFreezableTime() {
        return this.mEarliestFreezableTimeMillis;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setEarliestFreezableTime(long j) {
        this.mEarliestFreezableTimeMillis = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public long getLastUsedTimeout() {
        return this.mLastUsedTimeout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setLastUsedTimeout(long j) {
        this.mLastUsedTimeout = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean isFreezeExempt() {
        return this.mFreezeExempt;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setPendingFreeze(boolean z) {
        this.mPendingFreeze = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean isPendingFreeze() {
        return this.mPendingFreeze;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setFreezeExempt(boolean z) {
        this.mFreezeExempt = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProcessCachedOptimizerRecord(ProcessRecord processRecord) {
        this.mApp = processRecord;
        this.mProcLock = processRecord.mService.mProcLock;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(long j) {
        this.mFreezeUnfreezeTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void dump(PrintWriter printWriter, String str, long j) {
        printWriter.print(str);
        printWriter.print("lastCompactTime=");
        printWriter.print(this.mLastCompactTime);
        printWriter.print(" lastCompactProfile=");
        printWriter.println(this.mLastCompactProfile);
        printWriter.print(str);
        printWriter.print("hasPendingCompaction=");
        printWriter.print(this.mPendingCompact);
        printWriter.print(str);
        printWriter.print("isFreezeExempt=");
        printWriter.print(this.mFreezeExempt);
        printWriter.print(" isPendingFreeze=");
        printWriter.print(this.mPendingFreeze);
        printWriter.print(" isFrozen=");
        printWriter.println(this.mFrozen);
        printWriter.print(str);
        printWriter.print("earliestFreezableTimeMs=");
        TimeUtils.formatDuration(this.mEarliestFreezableTimeMillis, j, printWriter);
        printWriter.println();
    }
}
