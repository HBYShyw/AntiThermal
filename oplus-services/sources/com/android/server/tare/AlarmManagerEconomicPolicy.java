package com.android.server.tare;

import android.app.tare.EconomyManager;
import android.provider.DeviceConfig;
import android.util.IndentingPrintWriter;
import android.util.KeyValueListParser;
import android.util.Slog;
import android.util.SparseArray;
import com.android.server.tare.EconomicPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AlarmManagerEconomicPolicy extends EconomicPolicy {
    public static final int ACTION_ALARM_CLOCK = 1342177288;
    public static final int ACTION_ALARM_NONWAKEUP_EXACT = 1342177285;
    public static final int ACTION_ALARM_NONWAKEUP_EXACT_ALLOW_WHILE_IDLE = 1342177284;
    public static final int ACTION_ALARM_NONWAKEUP_INEXACT = 1342177287;
    public static final int ACTION_ALARM_NONWAKEUP_INEXACT_ALLOW_WHILE_IDLE = 1342177286;
    public static final int ACTION_ALARM_WAKEUP_EXACT = 1342177281;
    public static final int ACTION_ALARM_WAKEUP_EXACT_ALLOW_WHILE_IDLE = 1342177280;
    public static final int ACTION_ALARM_WAKEUP_INEXACT = 1342177283;
    public static final int ACTION_ALARM_WAKEUP_INEXACT_ALLOW_WHILE_IDLE = 1342177282;
    private final SparseArray<EconomicPolicy.Action> mActions;
    private long mInitialSatiatedConsumptionLimit;
    private final EconomicPolicy.Injector mInjector;
    private long mMaxSatiatedBalance;
    private long mMaxSatiatedConsumptionLimit;
    private long mMinSatiatedBalanceExempted;
    private long mMinSatiatedBalanceHeadlessSystemApp;
    private long mMinSatiatedBalanceOther;
    private long mMinSatiatedConsumptionLimit;
    private final KeyValueListParser mParser;
    private final SparseArray<EconomicPolicy.Reward> mRewards;
    private static final String TAG = "TARE- " + AlarmManagerEconomicPolicy.class.getSimpleName();
    private static final int[] COST_MODIFIERS = {0, 1, 2, 3};

    /* JADX INFO: Access modifiers changed from: package-private */
    public AlarmManagerEconomicPolicy(InternalResourceService internalResourceService, EconomicPolicy.Injector injector) {
        super(internalResourceService);
        this.mParser = new KeyValueListParser(',');
        this.mActions = new SparseArray<>();
        this.mRewards = new SparseArray<>();
        this.mInjector = injector;
        loadConstants("", null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public void setup(DeviceConfig.Properties properties) {
        super.setup(properties);
        loadConstants(this.mInjector.getSettingsGlobalString(this.mIrs.getContext().getContentResolver(), "tare_alarm_manager_constants"), properties);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public long getMinSatiatedBalance(int i, String str) {
        if (this.mIrs.isPackageRestricted(i, str)) {
            return 0L;
        }
        if (this.mIrs.isPackageExempted(i, str)) {
            return this.mMinSatiatedBalanceExempted;
        }
        if (this.mIrs.isHeadlessSystemApp(i, str)) {
            return this.mMinSatiatedBalanceHeadlessSystemApp;
        }
        return this.mMinSatiatedBalanceOther;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public long getMaxSatiatedBalance(int i, String str) {
        if (this.mIrs.isPackageRestricted(i, str)) {
            return 0L;
        }
        return this.mMaxSatiatedBalance;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public long getInitialSatiatedConsumptionLimit() {
        return this.mInitialSatiatedConsumptionLimit;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public long getMinSatiatedConsumptionLimit() {
        return this.mMinSatiatedConsumptionLimit;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public long getMaxSatiatedConsumptionLimit() {
        return this.mMaxSatiatedConsumptionLimit;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public int[] getCostModifiers() {
        return COST_MODIFIERS;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public EconomicPolicy.Action getAction(int i) {
        return this.mActions.get(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public EconomicPolicy.Reward getReward(int i) {
        return this.mRewards.get(i);
    }

    private void loadConstants(String str, DeviceConfig.Properties properties) {
        this.mActions.clear();
        this.mRewards.clear();
        try {
            this.mParser.setString(str);
        } catch (IllegalArgumentException e) {
            Slog.e(TAG, "Global setting key incorrect: ", e);
        }
        long constantAsCake = getConstantAsCake(this.mParser, properties, "am_min_satiated_balance_other_app", EconomyManager.DEFAULT_AM_MIN_SATIATED_BALANCE_OTHER_APP_CAKES);
        this.mMinSatiatedBalanceOther = constantAsCake;
        long constantAsCake2 = getConstantAsCake(this.mParser, properties, "am_min_satiated_balance_headless_system_app", EconomyManager.DEFAULT_AM_MIN_SATIATED_BALANCE_HEADLESS_SYSTEM_APP_CAKES, constantAsCake);
        this.mMinSatiatedBalanceHeadlessSystemApp = constantAsCake2;
        this.mMinSatiatedBalanceExempted = getConstantAsCake(this.mParser, properties, "am_min_satiated_balance_exempted", EconomyManager.DEFAULT_AM_MIN_SATIATED_BALANCE_EXEMPTED_CAKES, constantAsCake2);
        this.mMaxSatiatedBalance = getConstantAsCake(this.mParser, properties, "am_max_satiated_balance", EconomyManager.DEFAULT_AM_MAX_SATIATED_BALANCE_CAKES, Math.max(EconomyManager.arcToCake(1), this.mMinSatiatedBalanceExempted));
        long constantAsCake3 = getConstantAsCake(this.mParser, properties, "am_minimum_consumption_limit", EconomyManager.DEFAULT_AM_MIN_CONSUMPTION_LIMIT_CAKES, EconomyManager.arcToCake(1));
        this.mMinSatiatedConsumptionLimit = constantAsCake3;
        long constantAsCake4 = getConstantAsCake(this.mParser, properties, "am_initial_consumption_limit", EconomyManager.DEFAULT_AM_INITIAL_CONSUMPTION_LIMIT_CAKES, constantAsCake3);
        this.mInitialSatiatedConsumptionLimit = constantAsCake4;
        this.mMaxSatiatedConsumptionLimit = getConstantAsCake(this.mParser, properties, "am_maximum_consumption_limit", EconomyManager.DEFAULT_AM_MAX_CONSUMPTION_LIMIT_CAKES, constantAsCake4);
        this.mActions.put(ACTION_ALARM_WAKEUP_EXACT_ALLOW_WHILE_IDLE, new EconomicPolicy.Action(ACTION_ALARM_WAKEUP_EXACT_ALLOW_WHILE_IDLE, getConstantAsCake(this.mParser, properties, "am_action_alarm_allow_while_idle_exact_wakeup_ctp", EconomyManager.DEFAULT_AM_ACTION_ALARM_ALLOW_WHILE_IDLE_EXACT_WAKEUP_CTP_CAKES), getConstantAsCake(this.mParser, properties, "am_action_alarm_allow_while_idle_exact_wakeup_base_price", EconomyManager.DEFAULT_AM_ACTION_ALARM_ALLOW_WHILE_IDLE_EXACT_WAKEUP_BASE_PRICE_CAKES), false));
        this.mActions.put(ACTION_ALARM_WAKEUP_EXACT, new EconomicPolicy.Action(ACTION_ALARM_WAKEUP_EXACT, getConstantAsCake(this.mParser, properties, "am_action_alarm_exact_wakeup_ctp", EconomyManager.DEFAULT_AM_ACTION_ALARM_EXACT_WAKEUP_CTP_CAKES), getConstantAsCake(this.mParser, properties, "am_action_alarm_exact_wakeup_base_price", EconomyManager.DEFAULT_AM_ACTION_ALARM_EXACT_WAKEUP_BASE_PRICE_CAKES), false));
        this.mActions.put(ACTION_ALARM_WAKEUP_INEXACT_ALLOW_WHILE_IDLE, new EconomicPolicy.Action(ACTION_ALARM_WAKEUP_INEXACT_ALLOW_WHILE_IDLE, getConstantAsCake(this.mParser, properties, "am_action_alarm_allow_while_idle_inexact_wakeup_ctp", EconomyManager.DEFAULT_AM_ACTION_ALARM_ALLOW_WHILE_IDLE_INEXACT_WAKEUP_CTP_CAKES), getConstantAsCake(this.mParser, properties, "am_action_alarm_allow_while_idle_inexact_wakeup_base_price", EconomyManager.DEFAULT_AM_ACTION_ALARM_ALLOW_WHILE_IDLE_INEXACT_WAKEUP_BASE_PRICE_CAKES), false));
        this.mActions.put(ACTION_ALARM_WAKEUP_INEXACT, new EconomicPolicy.Action(ACTION_ALARM_WAKEUP_INEXACT, getConstantAsCake(this.mParser, properties, "am_action_alarm_inexact_wakeup_ctp", EconomyManager.DEFAULT_AM_ACTION_ALARM_INEXACT_WAKEUP_CTP_CAKES), getConstantAsCake(this.mParser, properties, "am_action_alarm_inexact_wakeup_base_price", EconomyManager.DEFAULT_AM_ACTION_ALARM_INEXACT_WAKEUP_BASE_PRICE_CAKES), false));
        this.mActions.put(ACTION_ALARM_NONWAKEUP_EXACT_ALLOW_WHILE_IDLE, new EconomicPolicy.Action(ACTION_ALARM_NONWAKEUP_EXACT_ALLOW_WHILE_IDLE, getConstantAsCake(this.mParser, properties, "am_action_alarm_allow_while_idle_exact_nonwakeup_ctp", EconomyManager.DEFAULT_AM_ACTION_ALARM_ALLOW_WHILE_IDLE_EXACT_NONWAKEUP_CTP_CAKES), getConstantAsCake(this.mParser, properties, "am_action_alarm_allow_while_idle_exact_nonwakeup_base_price", EconomyManager.DEFAULT_AM_ACTION_ALARM_ALLOW_WHILE_IDLE_INEXACT_NONWAKEUP_BASE_PRICE_CAKES), false));
        this.mActions.put(ACTION_ALARM_NONWAKEUP_EXACT, new EconomicPolicy.Action(ACTION_ALARM_NONWAKEUP_EXACT, getConstantAsCake(this.mParser, properties, "am_action_alarm_exact_nonwakeup_ctp", EconomyManager.DEFAULT_AM_ACTION_ALARM_EXACT_NONWAKEUP_CTP_CAKES), getConstantAsCake(this.mParser, properties, "am_action_alarm_exact_nonwakeup_base_price", EconomyManager.DEFAULT_AM_ACTION_ALARM_EXACT_NONWAKEUP_BASE_PRICE_CAKES), false));
        long constantAsCake5 = getConstantAsCake(this.mParser, properties, "am_action_alarm_allow_while_idle_inexact_nonwakeup_base_price", EconomyManager.DEFAULT_AM_ACTION_ALARM_ALLOW_WHILE_IDLE_INEXACT_NONWAKEUP_BASE_PRICE_CAKES);
        this.mActions.put(ACTION_ALARM_NONWAKEUP_INEXACT_ALLOW_WHILE_IDLE, new EconomicPolicy.Action(ACTION_ALARM_NONWAKEUP_INEXACT_ALLOW_WHILE_IDLE, getConstantAsCake(this.mParser, properties, "am_action_alarm_allow_while_idle_inexact_nonwakeup_ctp", EconomyManager.DEFAULT_AM_ACTION_ALARM_ALLOW_WHILE_IDLE_INEXACT_NONWAKEUP_CTP_CAKES), constantAsCake5));
        this.mActions.put(ACTION_ALARM_NONWAKEUP_INEXACT, new EconomicPolicy.Action(ACTION_ALARM_NONWAKEUP_INEXACT, getConstantAsCake(this.mParser, properties, "am_action_alarm_inexact_nonwakeup_ctp", EconomyManager.DEFAULT_AM_ACTION_ALARM_INEXACT_NONWAKEUP_CTP_CAKES), getConstantAsCake(this.mParser, properties, "am_action_alarm_inexact_nonwakeup_base_price", EconomyManager.DEFAULT_AM_ACTION_ALARM_INEXACT_NONWAKEUP_BASE_PRICE_CAKES)));
        this.mActions.put(ACTION_ALARM_CLOCK, new EconomicPolicy.Action(ACTION_ALARM_CLOCK, getConstantAsCake(this.mParser, properties, "am_action_alarm_alarmclock_ctp", EconomyManager.DEFAULT_AM_ACTION_ALARM_ALARMCLOCK_CTP_CAKES), getConstantAsCake(this.mParser, properties, "am_action_alarm_alarmclock_base_price", EconomyManager.DEFAULT_AM_ACTION_ALARM_ALARMCLOCK_BASE_PRICE_CAKES), false));
        this.mRewards.put(-2147483646, new EconomicPolicy.Reward(-2147483646, getConstantAsCake(this.mParser, properties, "am_reward_top_activity_instant", EconomyManager.DEFAULT_AM_REWARD_TOP_ACTIVITY_INSTANT_CAKES), getConstantAsCake(this.mParser, properties, "am_reward_top_activity_ongoing", 10000000L), getConstantAsCake(this.mParser, properties, "am_reward_top_activity_max", EconomyManager.DEFAULT_AM_REWARD_TOP_ACTIVITY_MAX_CAKES)));
        this.mRewards.put(Integer.MIN_VALUE, new EconomicPolicy.Reward(Integer.MIN_VALUE, getConstantAsCake(this.mParser, properties, "am_reward_notification_seen_instant", EconomyManager.DEFAULT_AM_REWARD_NOTIFICATION_SEEN_INSTANT_CAKES), getConstantAsCake(this.mParser, properties, "am_reward_notification_seen_ongoing", EconomyManager.DEFAULT_AM_REWARD_NOTIFICATION_SEEN_ONGOING_CAKES), getConstantAsCake(this.mParser, properties, "am_reward_notification_seen_max", EconomyManager.DEFAULT_AM_REWARD_NOTIFICATION_SEEN_MAX_CAKES)));
        this.mRewards.put(-2147483647, new EconomicPolicy.Reward(-2147483647, getConstantAsCake(this.mParser, properties, "am_reward_notification_interaction_instant", EconomyManager.DEFAULT_AM_REWARD_NOTIFICATION_INTERACTION_INSTANT_CAKES), getConstantAsCake(this.mParser, properties, "am_reward_notification_interaction_ongoing", EconomyManager.DEFAULT_AM_REWARD_NOTIFICATION_INTERACTION_ONGOING_CAKES), getConstantAsCake(this.mParser, properties, "am_reward_notification_interaction_max", EconomyManager.DEFAULT_AM_REWARD_NOTIFICATION_INTERACTION_MAX_CAKES)));
        this.mRewards.put(-2147483645, new EconomicPolicy.Reward(-2147483645, getConstantAsCake(this.mParser, properties, "am_reward_widget_interaction_instant", EconomyManager.DEFAULT_AM_REWARD_WIDGET_INTERACTION_INSTANT_CAKES), getConstantAsCake(this.mParser, properties, "am_reward_widget_interaction_ongoing", EconomyManager.DEFAULT_AM_REWARD_WIDGET_INTERACTION_ONGOING_CAKES), getConstantAsCake(this.mParser, properties, "am_reward_widget_interaction_max", EconomyManager.DEFAULT_AM_REWARD_WIDGET_INTERACTION_MAX_CAKES)));
        this.mRewards.put(-2147483644, new EconomicPolicy.Reward(-2147483644, getConstantAsCake(this.mParser, properties, "am_reward_other_user_interaction_instant", EconomyManager.DEFAULT_AM_REWARD_OTHER_USER_INTERACTION_INSTANT_CAKES), getConstantAsCake(this.mParser, properties, "am_reward_other_user_interaction_ongoing", EconomyManager.DEFAULT_AM_REWARD_OTHER_USER_INTERACTION_ONGOING_CAKES), getConstantAsCake(this.mParser, properties, "am_reward_other_user_interaction_max", EconomyManager.DEFAULT_AM_REWARD_OTHER_USER_INTERACTION_MAX_CAKES)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("Min satiated balances:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.print("Exempted", TareUtils.cakeToString(this.mMinSatiatedBalanceExempted)).println();
        indentingPrintWriter.print("Other", TareUtils.cakeToString(this.mMinSatiatedBalanceOther)).println();
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.print("Max satiated balance", TareUtils.cakeToString(this.mMaxSatiatedBalance)).println();
        indentingPrintWriter.print("Consumption limits: [");
        indentingPrintWriter.print(TareUtils.cakeToString(this.mMinSatiatedConsumptionLimit));
        indentingPrintWriter.print(", ");
        indentingPrintWriter.print(TareUtils.cakeToString(this.mInitialSatiatedConsumptionLimit));
        indentingPrintWriter.print(", ");
        indentingPrintWriter.print(TareUtils.cakeToString(this.mMaxSatiatedConsumptionLimit));
        indentingPrintWriter.println("]");
        indentingPrintWriter.println();
        indentingPrintWriter.println("Actions:");
        indentingPrintWriter.increaseIndent();
        for (int i = 0; i < this.mActions.size(); i++) {
            EconomicPolicy.dumpAction(indentingPrintWriter, this.mActions.valueAt(i));
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.println("Rewards:");
        indentingPrintWriter.increaseIndent();
        for (int i2 = 0; i2 < this.mRewards.size(); i2++) {
            EconomicPolicy.dumpReward(indentingPrintWriter, this.mRewards.valueAt(i2));
        }
        indentingPrintWriter.decreaseIndent();
    }
}
