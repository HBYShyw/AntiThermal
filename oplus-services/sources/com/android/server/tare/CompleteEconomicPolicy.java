package com.android.server.tare;

import android.provider.DeviceConfig;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.server.tare.EconomicPolicy;
import libcore.util.EmptyArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class CompleteEconomicPolicy extends EconomicPolicy {
    private static final String TAG = "TARE-" + CompleteEconomicPolicy.class.getSimpleName();
    private final SparseArray<EconomicPolicy.Action> mActions;
    private int[] mCostModifiers;
    private final ArraySet<EconomicPolicy> mEnabledEconomicPolicies;
    private int mEnabledEconomicPolicyIds;
    private long mInitialConsumptionLimit;
    private final CompleteInjector mInjector;
    private long mMaxConsumptionLimit;
    private long mMinConsumptionLimit;
    private final SparseArray<EconomicPolicy.Reward> mRewards;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompleteEconomicPolicy(InternalResourceService internalResourceService) {
        this(internalResourceService, new CompleteInjector());
    }

    @VisibleForTesting
    CompleteEconomicPolicy(InternalResourceService internalResourceService, CompleteInjector completeInjector) {
        super(internalResourceService);
        ArraySet<EconomicPolicy> arraySet = new ArraySet<>();
        this.mEnabledEconomicPolicies = arraySet;
        this.mActions = new SparseArray<>();
        this.mRewards = new SparseArray<>();
        this.mEnabledEconomicPolicyIds = 0;
        this.mCostModifiers = EmptyArray.INT;
        this.mInjector = completeInjector;
        if (completeInjector.isPolicyEnabled(268435456, null)) {
            this.mEnabledEconomicPolicyIds = 268435456 | this.mEnabledEconomicPolicyIds;
            arraySet.add(new AlarmManagerEconomicPolicy(this.mIrs, completeInjector));
        }
        if (completeInjector.isPolicyEnabled(536870912, null)) {
            this.mEnabledEconomicPolicyIds = 536870912 | this.mEnabledEconomicPolicyIds;
            arraySet.add(new JobSchedulerEconomicPolicy(this.mIrs, completeInjector));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public void setup(DeviceConfig.Properties properties) {
        super.setup(properties);
        this.mActions.clear();
        this.mRewards.clear();
        this.mEnabledEconomicPolicies.clear();
        this.mEnabledEconomicPolicyIds = 0;
        if (this.mInjector.isPolicyEnabled(268435456, properties)) {
            this.mEnabledEconomicPolicyIds |= 268435456;
            this.mEnabledEconomicPolicies.add(new AlarmManagerEconomicPolicy(this.mIrs, this.mInjector));
        }
        if (this.mInjector.isPolicyEnabled(536870912, properties)) {
            this.mEnabledEconomicPolicyIds |= 536870912;
            this.mEnabledEconomicPolicies.add(new JobSchedulerEconomicPolicy(this.mIrs, this.mInjector));
        }
        ArraySet arraySet = new ArraySet();
        for (int i = 0; i < this.mEnabledEconomicPolicies.size(); i++) {
            for (int i2 : this.mEnabledEconomicPolicies.valueAt(i).getCostModifiers()) {
                arraySet.add(Integer.valueOf(i2));
            }
        }
        this.mCostModifiers = ArrayUtils.convertToIntArray(arraySet);
        for (int i3 = 0; i3 < this.mEnabledEconomicPolicies.size(); i3++) {
            this.mEnabledEconomicPolicies.valueAt(i3).setup(properties);
        }
        updateLimits();
    }

    private void updateLimits() {
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        for (int i = 0; i < this.mEnabledEconomicPolicies.size(); i++) {
            EconomicPolicy valueAt = this.mEnabledEconomicPolicies.valueAt(i);
            j += valueAt.getInitialSatiatedConsumptionLimit();
            j3 += valueAt.getMinSatiatedConsumptionLimit();
            j2 += valueAt.getMaxSatiatedConsumptionLimit();
        }
        this.mInitialConsumptionLimit = j;
        this.mMinConsumptionLimit = j3;
        this.mMaxConsumptionLimit = j2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public long getMinSatiatedBalance(int i, String str) {
        long j = 0;
        for (int i2 = 0; i2 < this.mEnabledEconomicPolicies.size(); i2++) {
            j += this.mEnabledEconomicPolicies.valueAt(i2).getMinSatiatedBalance(i, str);
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public long getMaxSatiatedBalance(int i, String str) {
        long j = 0;
        for (int i2 = 0; i2 < this.mEnabledEconomicPolicies.size(); i2++) {
            j += this.mEnabledEconomicPolicies.valueAt(i2).getMaxSatiatedBalance(i, str);
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public long getInitialSatiatedConsumptionLimit() {
        return this.mInitialConsumptionLimit;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public long getMinSatiatedConsumptionLimit() {
        return this.mMinConsumptionLimit;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public long getMaxSatiatedConsumptionLimit() {
        return this.mMaxConsumptionLimit;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public int[] getCostModifiers() {
        int[] iArr = this.mCostModifiers;
        return iArr == null ? EmptyArray.INT : iArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public EconomicPolicy.Action getAction(int i) {
        if (this.mActions.contains(i)) {
            return this.mActions.get(i);
        }
        long j = 0;
        long j2 = 0;
        boolean z = false;
        for (int i2 = 0; i2 < this.mEnabledEconomicPolicies.size(); i2++) {
            EconomicPolicy.Action action = this.mEnabledEconomicPolicies.valueAt(i2).getAction(i);
            if (action != null) {
                j += action.costToProduce;
                j2 += action.basePrice;
                z = true;
            }
        }
        EconomicPolicy.Action action2 = z ? new EconomicPolicy.Action(i, j, j2) : null;
        this.mActions.put(i, action2);
        return action2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public EconomicPolicy.Reward getReward(int i) {
        if (this.mRewards.contains(i)) {
            return this.mRewards.get(i);
        }
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        boolean z = false;
        for (int i2 = 0; i2 < this.mEnabledEconomicPolicies.size(); i2++) {
            EconomicPolicy.Reward reward = this.mEnabledEconomicPolicies.valueAt(i2).getReward(i);
            if (reward != null) {
                j += reward.instantReward;
                j2 += reward.ongoingRewardPerSecond;
                j3 += reward.maxDailyReward;
                z = true;
            }
        }
        EconomicPolicy.Reward reward2 = z ? new EconomicPolicy.Reward(i, j, j2, j3) : null;
        this.mRewards.put(i, reward2);
        return reward2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPolicyEnabled(int i) {
        return (this.mEnabledEconomicPolicyIds & i) == i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getEnabledPolicyIds() {
        return this.mEnabledEconomicPolicyIds;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class CompleteInjector extends EconomicPolicy.Injector {
        CompleteInjector() {
        }

        boolean isPolicyEnabled(int i, DeviceConfig.Properties properties) {
            String str;
            if (i == 268435456) {
                str = "enable_policy_alarm";
            } else {
                if (i != 536870912) {
                    Slog.wtf(CompleteEconomicPolicy.TAG, "Unknown policy: " + i);
                    return false;
                }
                str = "enable_policy_job";
            }
            if (properties == null) {
                return true;
            }
            return properties.getBoolean(str, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        EconomicPolicy.dumpActiveModifiers(indentingPrintWriter);
        indentingPrintWriter.println();
        indentingPrintWriter.println(getClass().getSimpleName() + ":");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("Cached actions:");
        indentingPrintWriter.increaseIndent();
        for (int i = 0; i < this.mActions.size(); i++) {
            EconomicPolicy.Action valueAt = this.mActions.valueAt(i);
            if (valueAt != null) {
                EconomicPolicy.dumpAction(indentingPrintWriter, valueAt);
            }
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.println("Cached rewards:");
        indentingPrintWriter.increaseIndent();
        for (int i2 = 0; i2 < this.mRewards.size(); i2++) {
            EconomicPolicy.Reward valueAt2 = this.mRewards.valueAt(i2);
            if (valueAt2 != null) {
                EconomicPolicy.dumpReward(indentingPrintWriter, valueAt2);
            }
        }
        indentingPrintWriter.decreaseIndent();
        for (int i3 = 0; i3 < this.mEnabledEconomicPolicies.size(); i3++) {
            EconomicPolicy valueAt3 = this.mEnabledEconomicPolicies.valueAt(i3);
            indentingPrintWriter.println();
            indentingPrintWriter.print("(Includes) ");
            indentingPrintWriter.println(valueAt3.getClass().getSimpleName() + ":");
            indentingPrintWriter.increaseIndent();
            valueAt3.dump(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.decreaseIndent();
    }
}
