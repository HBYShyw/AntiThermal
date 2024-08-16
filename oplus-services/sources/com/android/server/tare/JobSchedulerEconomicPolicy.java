package com.android.server.tare;

import android.app.tare.EconomyManager;
import android.provider.DeviceConfig;
import android.util.IndentingPrintWriter;
import android.util.KeyValueListParser;
import android.util.Slog;
import android.util.SparseArray;
import com.android.server.tare.EconomicPolicy;
import com.android.server.usage.UnixCalendar;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class JobSchedulerEconomicPolicy extends EconomicPolicy {
    public static final int ACTION_JOB_DEFAULT_RUNNING = 1610612741;
    public static final int ACTION_JOB_DEFAULT_START = 1610612740;
    public static final int ACTION_JOB_HIGH_RUNNING = 1610612739;
    public static final int ACTION_JOB_HIGH_START = 1610612738;
    public static final int ACTION_JOB_LOW_RUNNING = 1610612743;
    public static final int ACTION_JOB_LOW_START = 1610612742;
    public static final int ACTION_JOB_MAX_RUNNING = 1610612737;
    public static final int ACTION_JOB_MAX_START = 1610612736;
    public static final int ACTION_JOB_MIN_RUNNING = 1610612745;
    public static final int ACTION_JOB_MIN_START = 1610612744;
    public static final int ACTION_JOB_TIMEOUT = 1610612746;
    public static final int REWARD_APP_INSTALL = -1610612736;
    private final SparseArray<EconomicPolicy.Action> mActions;
    private long mInitialSatiatedConsumptionLimit;
    private final EconomicPolicy.Injector mInjector;
    private long mMaxSatiatedBalance;
    private long mMaxSatiatedConsumptionLimit;
    private long mMinSatiatedBalanceExempted;
    private long mMinSatiatedBalanceHeadlessSystemApp;
    private long mMinSatiatedBalanceIncrementalAppUpdater;
    private long mMinSatiatedBalanceOther;
    private long mMinSatiatedConsumptionLimit;
    private final KeyValueListParser mParser;
    private final SparseArray<EconomicPolicy.Reward> mRewards;
    private static final String TAG = "TARE- " + JobSchedulerEconomicPolicy.class.getSimpleName();
    private static final int[] COST_MODIFIERS = {0, 1, 2, 3};

    /* JADX INFO: Access modifiers changed from: package-private */
    public JobSchedulerEconomicPolicy(InternalResourceService internalResourceService, EconomicPolicy.Injector injector) {
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
        loadConstants(this.mInjector.getSettingsGlobalString(this.mIrs.getContext().getContentResolver(), "tare_job_scheduler_constants"), properties);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public long getMinSatiatedBalance(int i, String str) {
        long j;
        if (this.mIrs.isPackageRestricted(i, str)) {
            return 0L;
        }
        if (this.mIrs.isPackageExempted(i, str)) {
            j = this.mMinSatiatedBalanceExempted;
        } else if (this.mIrs.isHeadlessSystemApp(i, str)) {
            j = this.mMinSatiatedBalanceHeadlessSystemApp;
        } else {
            j = this.mMinSatiatedBalanceOther;
        }
        return Math.min(j + (this.mIrs.getAppUpdateResponsibilityCount(i, str) * this.mMinSatiatedBalanceIncrementalAppUpdater), this.mMaxSatiatedBalance);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public long getMaxSatiatedBalance(int i, String str) {
        if (this.mIrs.isPackageRestricted(i, str)) {
            return 0L;
        }
        InstalledPackageInfo installedPackageInfo = this.mIrs.getInstalledPackageInfo(i, str);
        if (installedPackageInfo == null) {
            Slog.wtfStack(TAG, "Tried to get max balance of invalid app: " + TareUtils.appToString(i, str));
        } else if (installedPackageInfo.isSystemInstaller) {
            if (this.mIrs.getRealtimeSinceFirstSetupMs() < UnixCalendar.WEEK_IN_MILLIS) {
                return this.mMaxSatiatedConsumptionLimit;
            }
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
        long constantAsCake = getConstantAsCake(this.mParser, properties, "js_min_satiated_balance_other_app", EconomyManager.DEFAULT_JS_MIN_SATIATED_BALANCE_OTHER_APP_CAKES);
        this.mMinSatiatedBalanceOther = constantAsCake;
        long constantAsCake2 = getConstantAsCake(this.mParser, properties, "js_min_satiated_balance_headless_system_app", EconomyManager.DEFAULT_JS_MIN_SATIATED_BALANCE_HEADLESS_SYSTEM_APP_CAKES, constantAsCake);
        this.mMinSatiatedBalanceHeadlessSystemApp = constantAsCake2;
        this.mMinSatiatedBalanceExempted = getConstantAsCake(this.mParser, properties, "js_min_satiated_balance_exempted", EconomyManager.DEFAULT_JS_MIN_SATIATED_BALANCE_EXEMPTED_CAKES, constantAsCake2);
        this.mMinSatiatedBalanceIncrementalAppUpdater = getConstantAsCake(this.mParser, properties, "js_min_satiated_balance_increment_updater", EconomyManager.DEFAULT_JS_MIN_SATIATED_BALANCE_INCREMENT_APP_UPDATER_CAKES);
        this.mMaxSatiatedBalance = getConstantAsCake(this.mParser, properties, "js_max_satiated_balance", EconomyManager.DEFAULT_JS_MAX_SATIATED_BALANCE_CAKES, Math.max(EconomyManager.arcToCake(1), this.mMinSatiatedBalanceExempted));
        long constantAsCake3 = getConstantAsCake(this.mParser, properties, "js_minimum_consumption_limit", EconomyManager.DEFAULT_JS_MIN_CONSUMPTION_LIMIT_CAKES, EconomyManager.arcToCake(1));
        this.mMinSatiatedConsumptionLimit = constantAsCake3;
        long constantAsCake4 = getConstantAsCake(this.mParser, properties, "js_initial_consumption_limit", EconomyManager.DEFAULT_JS_INITIAL_CONSUMPTION_LIMIT_CAKES, constantAsCake3);
        this.mInitialSatiatedConsumptionLimit = constantAsCake4;
        this.mMaxSatiatedConsumptionLimit = getConstantAsCake(this.mParser, properties, "js_maximum_consumption_limit", EconomyManager.DEFAULT_JS_MAX_CONSUMPTION_LIMIT_CAKES, constantAsCake4);
        this.mActions.put(ACTION_JOB_MAX_START, new EconomicPolicy.Action(ACTION_JOB_MAX_START, getConstantAsCake(this.mParser, properties, "js_action_job_max_start_ctp", EconomyManager.DEFAULT_JS_ACTION_JOB_MAX_START_CTP_CAKES), getConstantAsCake(this.mParser, properties, "js_action_job_max_start_base_price", EconomyManager.DEFAULT_JS_ACTION_JOB_MAX_START_BASE_PRICE_CAKES)));
        this.mActions.put(ACTION_JOB_MAX_RUNNING, new EconomicPolicy.Action(ACTION_JOB_MAX_RUNNING, getConstantAsCake(this.mParser, properties, "js_action_job_max_running_ctp", EconomyManager.DEFAULT_JS_ACTION_JOB_MAX_RUNNING_CTP_CAKES), getConstantAsCake(this.mParser, properties, "js_action_job_max_running_base_price", EconomyManager.DEFAULT_JS_ACTION_JOB_MAX_RUNNING_BASE_PRICE_CAKES)));
        this.mActions.put(ACTION_JOB_HIGH_START, new EconomicPolicy.Action(ACTION_JOB_HIGH_START, getConstantAsCake(this.mParser, properties, "js_action_job_high_start_ctp", EconomyManager.DEFAULT_JS_ACTION_JOB_HIGH_START_CTP_CAKES), getConstantAsCake(this.mParser, properties, "js_action_job_high_start_base_price", EconomyManager.DEFAULT_JS_ACTION_JOB_HIGH_START_BASE_PRICE_CAKES)));
        this.mActions.put(ACTION_JOB_HIGH_RUNNING, new EconomicPolicy.Action(ACTION_JOB_HIGH_RUNNING, getConstantAsCake(this.mParser, properties, "js_action_job_high_running_ctp", EconomyManager.DEFAULT_JS_ACTION_JOB_HIGH_RUNNING_CTP_CAKES), getConstantAsCake(this.mParser, properties, "js_action_job_high_running_base_price", EconomyManager.DEFAULT_JS_ACTION_JOB_HIGH_RUNNING_BASE_PRICE_CAKES)));
        this.mActions.put(ACTION_JOB_DEFAULT_START, new EconomicPolicy.Action(ACTION_JOB_DEFAULT_START, getConstantAsCake(this.mParser, properties, "js_action_job_default_start_ctp", EconomyManager.DEFAULT_JS_ACTION_JOB_DEFAULT_START_CTP_CAKES), getConstantAsCake(this.mParser, properties, "js_action_job_default_start_base_price", EconomyManager.DEFAULT_JS_ACTION_JOB_DEFAULT_START_BASE_PRICE_CAKES)));
        this.mActions.put(ACTION_JOB_DEFAULT_RUNNING, new EconomicPolicy.Action(ACTION_JOB_DEFAULT_RUNNING, getConstantAsCake(this.mParser, properties, "js_action_job_default_running_ctp", EconomyManager.DEFAULT_JS_ACTION_JOB_DEFAULT_RUNNING_CTP_CAKES), getConstantAsCake(this.mParser, properties, "js_action_job_default_running_base_price", EconomyManager.DEFAULT_JS_ACTION_JOB_DEFAULT_RUNNING_BASE_PRICE_CAKES)));
        this.mActions.put(ACTION_JOB_LOW_START, new EconomicPolicy.Action(ACTION_JOB_LOW_START, getConstantAsCake(this.mParser, properties, "js_action_job_low_start_ctp", EconomyManager.DEFAULT_JS_ACTION_JOB_LOW_START_CTP_CAKES), getConstantAsCake(this.mParser, properties, "js_action_job_low_start_base_price", EconomyManager.DEFAULT_JS_ACTION_JOB_LOW_START_BASE_PRICE_CAKES)));
        this.mActions.put(ACTION_JOB_LOW_RUNNING, new EconomicPolicy.Action(ACTION_JOB_LOW_RUNNING, getConstantAsCake(this.mParser, properties, "js_action_job_low_running_ctp", EconomyManager.DEFAULT_JS_ACTION_JOB_LOW_RUNNING_CTP_CAKES), getConstantAsCake(this.mParser, properties, "js_action_job_low_running_base_price", EconomyManager.DEFAULT_JS_ACTION_JOB_LOW_RUNNING_BASE_PRICE_CAKES)));
        this.mActions.put(ACTION_JOB_MIN_START, new EconomicPolicy.Action(ACTION_JOB_MIN_START, getConstantAsCake(this.mParser, properties, "js_action_job_min_start_ctp", EconomyManager.DEFAULT_JS_ACTION_JOB_MIN_START_CTP_CAKES), getConstantAsCake(this.mParser, properties, "js_action_job_min_start_base_price", EconomyManager.DEFAULT_JS_ACTION_JOB_MIN_START_BASE_PRICE_CAKES)));
        this.mActions.put(ACTION_JOB_MIN_RUNNING, new EconomicPolicy.Action(ACTION_JOB_MIN_RUNNING, getConstantAsCake(this.mParser, properties, "js_action_job_min_running_ctp", EconomyManager.DEFAULT_JS_ACTION_JOB_MIN_RUNNING_CTP_CAKES), getConstantAsCake(this.mParser, properties, "js_action_job_min_running_base_price", EconomyManager.DEFAULT_JS_ACTION_JOB_MIN_RUNNING_BASE_PRICE_CAKES)));
        this.mActions.put(ACTION_JOB_TIMEOUT, new EconomicPolicy.Action(ACTION_JOB_TIMEOUT, getConstantAsCake(this.mParser, properties, "js_action_job_timeout_penalty_ctp", EconomyManager.DEFAULT_JS_ACTION_JOB_TIMEOUT_PENALTY_CTP_CAKES), getConstantAsCake(this.mParser, properties, "js_action_job_timeout_penalty_base_price", EconomyManager.DEFAULT_JS_ACTION_JOB_TIMEOUT_PENALTY_BASE_PRICE_CAKES)));
        this.mRewards.put(-2147483646, new EconomicPolicy.Reward(-2147483646, getConstantAsCake(this.mParser, properties, "js_reward_top_activity_instant", EconomyManager.DEFAULT_JS_REWARD_TOP_ACTIVITY_INSTANT_CAKES), getConstantAsCake(this.mParser, properties, "js_reward_top_activity_ongoing", 500000000L), getConstantAsCake(this.mParser, properties, "js_reward_top_activity_max", EconomyManager.DEFAULT_JS_REWARD_TOP_ACTIVITY_MAX_CAKES)));
        this.mRewards.put(Integer.MIN_VALUE, new EconomicPolicy.Reward(Integer.MIN_VALUE, getConstantAsCake(this.mParser, properties, "js_reward_notification_seen_instant", EconomyManager.DEFAULT_JS_REWARD_NOTIFICATION_SEEN_INSTANT_CAKES), getConstantAsCake(this.mParser, properties, "js_reward_notification_seen_ongoing", EconomyManager.DEFAULT_JS_REWARD_NOTIFICATION_SEEN_ONGOING_CAKES), getConstantAsCake(this.mParser, properties, "js_reward_notification_seen_max", EconomyManager.DEFAULT_JS_REWARD_NOTIFICATION_SEEN_MAX_CAKES)));
        this.mRewards.put(-2147483647, new EconomicPolicy.Reward(-2147483647, getConstantAsCake(this.mParser, properties, "js_reward_notification_interaction_instant", EconomyManager.DEFAULT_JS_REWARD_NOTIFICATION_INTERACTION_INSTANT_CAKES), getConstantAsCake(this.mParser, properties, "js_reward_notification_interaction_ongoing", EconomyManager.DEFAULT_JS_REWARD_NOTIFICATION_INTERACTION_ONGOING_CAKES), getConstantAsCake(this.mParser, properties, "js_reward_notification_interaction_max", EconomyManager.DEFAULT_JS_REWARD_NOTIFICATION_INTERACTION_MAX_CAKES)));
        this.mRewards.put(-2147483645, new EconomicPolicy.Reward(-2147483645, getConstantAsCake(this.mParser, properties, "js_reward_widget_interaction_instant", EconomyManager.DEFAULT_JS_REWARD_WIDGET_INTERACTION_INSTANT_CAKES), getConstantAsCake(this.mParser, properties, "js_reward_widget_interaction_ongoing", EconomyManager.DEFAULT_JS_REWARD_WIDGET_INTERACTION_ONGOING_CAKES), getConstantAsCake(this.mParser, properties, "js_reward_widget_interaction_max", EconomyManager.DEFAULT_JS_REWARD_WIDGET_INTERACTION_MAX_CAKES)));
        this.mRewards.put(-2147483644, new EconomicPolicy.Reward(-2147483644, getConstantAsCake(this.mParser, properties, "js_reward_other_user_interaction_instant", EconomyManager.DEFAULT_JS_REWARD_OTHER_USER_INTERACTION_INSTANT_CAKES), getConstantAsCake(this.mParser, properties, "js_reward_other_user_interaction_ongoing", EconomyManager.DEFAULT_JS_REWARD_OTHER_USER_INTERACTION_ONGOING_CAKES), getConstantAsCake(this.mParser, properties, "js_reward_other_user_interaction_max", EconomyManager.DEFAULT_JS_REWARD_OTHER_USER_INTERACTION_MAX_CAKES)));
        this.mRewards.put(REWARD_APP_INSTALL, new EconomicPolicy.Reward(REWARD_APP_INSTALL, getConstantAsCake(this.mParser, properties, "js_reward_app_install_instant", EconomyManager.DEFAULT_JS_REWARD_APP_INSTALL_INSTANT_CAKES), getConstantAsCake(this.mParser, properties, "js_reward_app_install_ongoing", EconomyManager.DEFAULT_JS_REWARD_APP_INSTALL_ONGOING_CAKES), getConstantAsCake(this.mParser, properties, "js_reward_app_install_max", EconomyManager.DEFAULT_JS_REWARD_APP_INSTALL_MAX_CAKES)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.EconomicPolicy
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("Min satiated balance:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.print("Exempted", TareUtils.cakeToString(this.mMinSatiatedBalanceExempted)).println();
        indentingPrintWriter.print("Other", TareUtils.cakeToString(this.mMinSatiatedBalanceOther)).println();
        indentingPrintWriter.print("+App Updater", TareUtils.cakeToString(this.mMinSatiatedBalanceIncrementalAppUpdater)).println();
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
