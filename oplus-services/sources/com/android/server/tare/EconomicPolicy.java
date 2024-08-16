package com.android.server.tare;

import android.app.tare.EconomyManager;
import android.content.ContentResolver;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.util.IndentingPrintWriter;
import android.util.KeyValueListParser;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class EconomicPolicy {
    static final int ALL_POLICIES = 805306368;
    static final int MASK_EVENT = 268435455;
    static final int MASK_POLICY = 805306368;
    static final int MASK_TYPE = -1073741824;
    public static final int POLICY_ALARM = 268435456;
    public static final int POLICY_JOB = 536870912;
    static final int REGULATION_BASIC_INCOME = 0;
    static final int REGULATION_BG_RESTRICTED = 5;
    static final int REGULATION_BG_UNRESTRICTED = 6;
    static final int REGULATION_BIRTHRIGHT = 1;
    static final int REGULATION_DEMOTION = 4;
    static final int REGULATION_FORCE_STOP = 8;
    static final int REGULATION_PROMOTION = 3;
    static final int REGULATION_WEALTH_RECLAMATION = 2;
    static final int REWARD_NOTIFICATION_INTERACTION = -2147483647;
    static final int REWARD_NOTIFICATION_SEEN = Integer.MIN_VALUE;
    static final int REWARD_OTHER_USER_INTERACTION = -2147483644;
    static final int REWARD_TOP_ACTIVITY = -2147483646;
    static final int REWARD_WIDGET_INTERACTION = -2147483645;
    private static final int SHIFT_POLICY = 28;
    private static final int SHIFT_TYPE = 30;
    static final int TYPE_ACTION = 1073741824;
    static final int TYPE_REGULATION = 0;
    static final int TYPE_REWARD = Integer.MIN_VALUE;
    protected final InternalResourceService mIrs;
    private static final String TAG = "TARE-" + EconomicPolicy.class.getSimpleName();
    private static final Modifier[] COST_MODIFIER_BY_INDEX = new Modifier[4];

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface AppAction {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface EventType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface Policy {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface UtilityReward {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getEventType(int i) {
        return i & MASK_TYPE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Action getAction(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int[] getCostModifiers();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract long getInitialSatiatedConsumptionLimit();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract long getMaxSatiatedBalance(int i, String str);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract long getMaxSatiatedConsumptionLimit();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract long getMinSatiatedBalance(int i, String str);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract long getMinSatiatedConsumptionLimit();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Reward getReward(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Action {
        public final long basePrice;
        public final long costToProduce;
        public final int id;
        public final boolean respectsStockLimit;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Action(int i, long j, long j2) {
            this(i, j, j2, true);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Action(int i, long j, long j2, boolean z) {
            this.id = i;
            this.costToProduce = j;
            this.basePrice = j2;
            this.respectsStockLimit = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Reward {
        public final int id;
        public final long instantReward;
        public final long maxDailyReward;
        public final long ongoingRewardPerSecond;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Reward(int i, long j, long j2, long j3) {
            this.id = i;
            this.instantReward = j;
            this.ongoingRewardPerSecond = j2;
            this.maxDailyReward = j3;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Cost {
        public final long costToProduce;
        public final long price;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Cost(long j, long j2) {
            this.costToProduce = j;
            this.price = j2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EconomicPolicy(InternalResourceService internalResourceService) {
        this.mIrs = internalResourceService;
        for (int i : getCostModifiers()) {
            initModifier(i, internalResourceService);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setup(DeviceConfig.Properties properties) {
        for (int i = 0; i < 4; i++) {
            Modifier modifier = COST_MODIFIER_BY_INDEX[i];
            if (modifier != null) {
                modifier.setup();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void tearDown() {
        for (int i = 0; i < 4; i++) {
            Modifier modifier = COST_MODIFIER_BY_INDEX[i];
            if (modifier != null) {
                modifier.tearDown();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Cost getCostOfAction(int i, int i2, String str) {
        Action action = getAction(i);
        if (action == null || this.mIrs.isVip(i2, str)) {
            return new Cost(0L, 0L);
        }
        long j = action.costToProduce;
        long j2 = action.basePrice;
        long j3 = j2;
        boolean z = false;
        for (int i3 : getCostModifiers()) {
            if (i3 == 3) {
                z = true;
            } else {
                Modifier modifier = getModifier(i3);
                j = modifier.getModifiedCostToProduce(j);
                j3 = modifier.getModifiedPrice(j3);
            }
        }
        if (z) {
            j3 = ((ProcessStateModifier) getModifier(3)).getModifiedPrice(i2, str, j, j3);
        }
        return new Cost(j, j3);
    }

    private static void initModifier(int i, InternalResourceService internalResourceService) {
        Modifier chargingModifier;
        if (i >= 0) {
            Modifier[] modifierArr = COST_MODIFIER_BY_INDEX;
            if (i < modifierArr.length) {
                if (modifierArr[i] == null) {
                    if (i == 0) {
                        chargingModifier = new ChargingModifier(internalResourceService);
                    } else if (i == 1) {
                        chargingModifier = new DeviceIdleModifier(internalResourceService);
                    } else if (i == 2) {
                        chargingModifier = new PowerSaveModeModifier(internalResourceService);
                    } else if (i == 3) {
                        chargingModifier = new ProcessStateModifier(internalResourceService);
                    } else {
                        throw new IllegalArgumentException("Invalid modifier id " + i);
                    }
                    modifierArr[i] = chargingModifier;
                    return;
                }
                return;
            }
        }
        throw new IllegalArgumentException("Invalid modifier id " + i);
    }

    private static Modifier getModifier(int i) {
        if (i >= 0) {
            Modifier[] modifierArr = COST_MODIFIER_BY_INDEX;
            if (i < modifierArr.length) {
                Modifier modifier = modifierArr[i];
                if (modifier != null) {
                    return modifier;
                }
                throw new IllegalStateException("Modifier #" + i + " was never initialized");
            }
        }
        throw new IllegalArgumentException("Invalid modifier id " + i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isReward(int i) {
        return getEventType(i) == Integer.MIN_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String eventToString(int i) {
        int i2 = MASK_TYPE & i;
        if (i2 == Integer.MIN_VALUE) {
            return rewardToString(i);
        }
        if (i2 == 0) {
            return regulationToString(i);
        }
        if (i2 == 1073741824) {
            return actionToString(i);
        }
        return "UNKNOWN_EVENT:" + Integer.toHexString(i);
    }

    static String actionToString(int i) {
        int i2 = 805306368 & i;
        if (i2 == 268435456) {
            switch (i) {
                case AlarmManagerEconomicPolicy.ACTION_ALARM_WAKEUP_EXACT_ALLOW_WHILE_IDLE /* 1342177280 */:
                    return "ALARM_WAKEUP_EXACT_ALLOW_WHILE_IDLE";
                case AlarmManagerEconomicPolicy.ACTION_ALARM_WAKEUP_EXACT /* 1342177281 */:
                    return "ALARM_WAKEUP_EXACT";
                case AlarmManagerEconomicPolicy.ACTION_ALARM_WAKEUP_INEXACT_ALLOW_WHILE_IDLE /* 1342177282 */:
                    return "ALARM_WAKEUP_INEXACT_ALLOW_WHILE_IDLE";
                case AlarmManagerEconomicPolicy.ACTION_ALARM_WAKEUP_INEXACT /* 1342177283 */:
                    return "ALARM_WAKEUP_INEXACT";
                case AlarmManagerEconomicPolicy.ACTION_ALARM_NONWAKEUP_EXACT_ALLOW_WHILE_IDLE /* 1342177284 */:
                    return "ALARM_NONWAKEUP_EXACT_ALLOW_WHILE_IDLE";
                case AlarmManagerEconomicPolicy.ACTION_ALARM_NONWAKEUP_EXACT /* 1342177285 */:
                    return "ALARM_NONWAKEUP_EXACT";
                case AlarmManagerEconomicPolicy.ACTION_ALARM_NONWAKEUP_INEXACT_ALLOW_WHILE_IDLE /* 1342177286 */:
                    return "ALARM_NONWAKEUP_INEXACT_ALLOW_WHILE_IDLE";
                case AlarmManagerEconomicPolicy.ACTION_ALARM_NONWAKEUP_INEXACT /* 1342177287 */:
                    return "ALARM_NONWAKEUP_INEXACT";
                case AlarmManagerEconomicPolicy.ACTION_ALARM_CLOCK /* 1342177288 */:
                    return "ALARM_CLOCK";
            }
        }
        if (i2 == 536870912) {
            switch (i) {
                case JobSchedulerEconomicPolicy.ACTION_JOB_MAX_START /* 1610612736 */:
                    return "JOB_MAX_START";
                case JobSchedulerEconomicPolicy.ACTION_JOB_MAX_RUNNING /* 1610612737 */:
                    return "JOB_MAX_RUNNING";
                case JobSchedulerEconomicPolicy.ACTION_JOB_HIGH_START /* 1610612738 */:
                    return "JOB_HIGH_START";
                case JobSchedulerEconomicPolicy.ACTION_JOB_HIGH_RUNNING /* 1610612739 */:
                    return "JOB_HIGH_RUNNING";
                case JobSchedulerEconomicPolicy.ACTION_JOB_DEFAULT_START /* 1610612740 */:
                    return "JOB_DEFAULT_START";
                case JobSchedulerEconomicPolicy.ACTION_JOB_DEFAULT_RUNNING /* 1610612741 */:
                    return "JOB_DEFAULT_RUNNING";
                case JobSchedulerEconomicPolicy.ACTION_JOB_LOW_START /* 1610612742 */:
                    return "JOB_LOW_START";
                case JobSchedulerEconomicPolicy.ACTION_JOB_LOW_RUNNING /* 1610612743 */:
                    return "JOB_LOW_RUNNING";
                case JobSchedulerEconomicPolicy.ACTION_JOB_MIN_START /* 1610612744 */:
                    return "JOB_MIN_START";
                case JobSchedulerEconomicPolicy.ACTION_JOB_MIN_RUNNING /* 1610612745 */:
                    return "JOB_MIN_RUNNING";
                case JobSchedulerEconomicPolicy.ACTION_JOB_TIMEOUT /* 1610612746 */:
                    return "JOB_TIMEOUT";
            }
        }
        return "UNKNOWN_ACTION:" + Integer.toHexString(i);
    }

    static String regulationToString(int i) {
        switch (i) {
            case 0:
                return "BASIC_INCOME";
            case 1:
                return "BIRTHRIGHT";
            case 2:
                return "WEALTH_RECLAMATION";
            case 3:
                return "PROMOTION";
            case 4:
                return "DEMOTION";
            case 5:
                return "BG_RESTRICTED";
            case 6:
                return "BG_UNRESTRICTED";
            case 7:
            default:
                return "UNKNOWN_REGULATION:" + Integer.toHexString(i);
            case 8:
                return "FORCE_STOP";
        }
    }

    static String rewardToString(int i) {
        if (i == -1610612736) {
            return "REWARD_JOB_APP_INSTALL";
        }
        switch (i) {
            case Integer.MIN_VALUE:
                return "REWARD_NOTIFICATION_SEEN";
            case REWARD_NOTIFICATION_INTERACTION /* -2147483647 */:
                return "REWARD_NOTIFICATION_INTERACTION";
            case REWARD_TOP_ACTIVITY /* -2147483646 */:
                return "REWARD_TOP_ACTIVITY";
            case REWARD_WIDGET_INTERACTION /* -2147483645 */:
                return "REWARD_WIDGET_INTERACTION";
            case REWARD_OTHER_USER_INTERACTION /* -2147483644 */:
                return "REWARD_OTHER_USER_INTERACTION";
            default:
                return "UNKNOWN_REWARD:" + Integer.toHexString(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long getConstantAsCake(KeyValueListParser keyValueListParser, DeviceConfig.Properties properties, String str, long j) {
        return getConstantAsCake(keyValueListParser, properties, str, j, 0L);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long getConstantAsCake(KeyValueListParser keyValueListParser, DeviceConfig.Properties properties, String str, long j, long j2) {
        if (keyValueListParser.size() > 0) {
            return Math.max(j2, EconomyManager.parseCreditValue(keyValueListParser.getString(str, (String) null), j));
        }
        if (properties != null) {
            return Math.max(j2, EconomyManager.parseCreditValue(properties.getString(str, (String) null), j));
        }
        return Math.max(j2, j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Injector {
        /* JADX INFO: Access modifiers changed from: package-private */
        public String getSettingsGlobalString(ContentResolver contentResolver, String str) {
            return Settings.Global.getString(contentResolver, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void dumpActiveModifiers(IndentingPrintWriter indentingPrintWriter) {
        for (int i = 0; i < 4; i++) {
            indentingPrintWriter.print("Modifier ");
            indentingPrintWriter.println(i);
            indentingPrintWriter.increaseIndent();
            Modifier modifier = COST_MODIFIER_BY_INDEX[i];
            if (modifier != null) {
                modifier.dump(indentingPrintWriter);
            } else {
                indentingPrintWriter.println("NOT ACTIVE");
            }
            indentingPrintWriter.decreaseIndent();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void dumpAction(IndentingPrintWriter indentingPrintWriter, Action action) {
        indentingPrintWriter.print(actionToString(action.id));
        indentingPrintWriter.print(": ");
        indentingPrintWriter.print("ctp=");
        indentingPrintWriter.print(TareUtils.cakeToString(action.costToProduce));
        indentingPrintWriter.print(", basePrice=");
        indentingPrintWriter.print(TareUtils.cakeToString(action.basePrice));
        indentingPrintWriter.println();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void dumpReward(IndentingPrintWriter indentingPrintWriter, Reward reward) {
        indentingPrintWriter.print(rewardToString(reward.id));
        indentingPrintWriter.print(": ");
        indentingPrintWriter.print("instant=");
        indentingPrintWriter.print(TareUtils.cakeToString(reward.instantReward));
        indentingPrintWriter.print(", ongoing/sec=");
        indentingPrintWriter.print(TareUtils.cakeToString(reward.ongoingRewardPerSecond));
        indentingPrintWriter.print(", maxDaily=");
        indentingPrintWriter.print(TareUtils.cakeToString(reward.maxDailyReward));
        indentingPrintWriter.println();
    }
}
