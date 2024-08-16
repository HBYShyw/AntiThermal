package com.android.server.timedetector;

import android.R;
import android.app.ActivityManagerInternal;
import android.app.time.TimeCapabilitiesAndConfig;
import android.app.time.TimeConfiguration;
import android.app.timedetector.TimeDetectorHelper;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import com.android.server.LocalServices;
import com.android.server.timedetector.ConfigurationInternal;
import com.android.server.timezonedetector.StateChangeListener;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class ServiceConfigAccessorImpl implements ServiceConfigAccessor {
    private static final int[] DEFAULT_AUTOMATIC_TIME_ORIGIN_PRIORITIES = {1, 3};
    private static final Set<String> SERVER_FLAGS_KEYS_TO_WATCH = Set.of(ServerFlags.KEY_TIME_DETECTOR_LOWER_BOUND_MILLIS_OVERRIDE, ServerFlags.KEY_TIME_DETECTOR_ORIGIN_PRIORITIES_OVERRIDE);
    private static final Object SLOCK = new Object();
    private static final int SYSTEM_CLOCK_CONFIRMATION_THRESHOLD_MILLIS = 1000;
    private static final int SYSTEM_CLOCK_UPDATE_THRESHOLD_MILLIS_DEFAULT = 2000;

    @GuardedBy({"SLOCK"})
    private static ServiceConfigAccessor sInstance;
    private final ConfigOriginPrioritiesSupplier mConfigOriginPrioritiesSupplier;

    @GuardedBy({"this"})
    private final List<StateChangeListener> mConfigurationInternalListeners = new ArrayList();
    private final Context mContext;
    private final ContentResolver mCr;
    private final ServerFlags mServerFlags;
    private final ServerFlagsOriginPrioritiesSupplier mServerFlagsOriginPrioritiesSupplier;
    private final int mSystemClockUpdateThresholdMillis;
    private final UserManager mUserManager;

    private int getSystemClockConfidenceUpgradeThresholdMillis() {
        return 1000;
    }

    private ServiceConfigAccessorImpl(Context context) {
        Objects.requireNonNull(context);
        this.mContext = context;
        this.mCr = context.getContentResolver();
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        ServerFlags serverFlags = ServerFlags.getInstance(context);
        this.mServerFlags = serverFlags;
        this.mConfigOriginPrioritiesSupplier = new ConfigOriginPrioritiesSupplier(context);
        this.mServerFlagsOriginPrioritiesSupplier = new ServerFlagsOriginPrioritiesSupplier(serverFlags);
        this.mSystemClockUpdateThresholdMillis = SystemProperties.getInt("ro.sys.time_detector_update_diff", 2000);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        context.registerReceiverForAllUsers(new BroadcastReceiver() { // from class: com.android.server.timedetector.ServiceConfigAccessorImpl.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                ServiceConfigAccessorImpl.this.handleConfigurationInternalChangeOnMainThread();
            }
        }, intentFilter, null, null);
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("auto_time"), true, new ContentObserver(context.getMainThreadHandler()) { // from class: com.android.server.timedetector.ServiceConfigAccessorImpl.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                ServiceConfigAccessorImpl.this.handleConfigurationInternalChangeOnMainThread();
            }
        });
        serverFlags.addListener(new StateChangeListener() { // from class: com.android.server.timedetector.ServiceConfigAccessorImpl$$ExternalSyntheticLambda0
            @Override // com.android.server.timezonedetector.StateChangeListener
            public final void onChange() {
                ServiceConfigAccessorImpl.this.handleConfigurationInternalChangeOnMainThread();
            }
        }, SERVER_FLAGS_KEYS_TO_WATCH);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ServiceConfigAccessor getInstance(Context context) {
        ServiceConfigAccessor serviceConfigAccessor;
        synchronized (SLOCK) {
            if (sInstance == null) {
                sInstance = new ServiceConfigAccessorImpl(context);
            }
            serviceConfigAccessor = sInstance;
        }
        return serviceConfigAccessor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConfigurationInternalChangeOnMainThread() {
        ArrayList arrayList;
        synchronized (this) {
            arrayList = new ArrayList(this.mConfigurationInternalListeners);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((StateChangeListener) it.next()).onChange();
        }
    }

    @Override // com.android.server.timedetector.ServiceConfigAccessor
    public synchronized void addConfigurationInternalChangeListener(StateChangeListener stateChangeListener) {
        List<StateChangeListener> list = this.mConfigurationInternalListeners;
        Objects.requireNonNull(stateChangeListener);
        list.add(stateChangeListener);
    }

    @Override // com.android.server.timedetector.ServiceConfigAccessor
    public synchronized void removeConfigurationInternalChangeListener(StateChangeListener stateChangeListener) {
        List<StateChangeListener> list = this.mConfigurationInternalListeners;
        Objects.requireNonNull(stateChangeListener);
        list.remove(stateChangeListener);
    }

    @Override // com.android.server.timedetector.ServiceConfigAccessor
    public synchronized ConfigurationInternal getCurrentUserConfigurationInternal() {
        return getConfigurationInternal(((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).getCurrentUserId());
    }

    @Override // com.android.server.timedetector.ServiceConfigAccessor
    public synchronized boolean updateConfiguration(int i, TimeConfiguration timeConfiguration, boolean z) {
        Objects.requireNonNull(timeConfiguration);
        TimeCapabilitiesAndConfig createCapabilitiesAndConfig = getCurrentUserConfigurationInternal().createCapabilitiesAndConfig(z);
        TimeConfiguration tryApplyConfigChanges = createCapabilitiesAndConfig.getCapabilities().tryApplyConfigChanges(createCapabilitiesAndConfig.getConfiguration(), timeConfiguration);
        if (tryApplyConfigChanges == null) {
            return false;
        }
        storeConfiguration(i, tryApplyConfigChanges);
        return true;
    }

    @GuardedBy({"this"})
    private void storeConfiguration(int i, TimeConfiguration timeConfiguration) {
        Objects.requireNonNull(timeConfiguration);
        if (isAutoDetectionSupported()) {
            setAutoDetectionEnabledIfRequired(timeConfiguration.isAutoDetectionEnabled());
        }
    }

    @Override // com.android.server.timedetector.ServiceConfigAccessor
    public synchronized ConfigurationInternal getConfigurationInternal(int i) {
        TimeDetectorHelper timeDetectorHelper;
        timeDetectorHelper = TimeDetectorHelper.INSTANCE;
        return new ConfigurationInternal.Builder(i).setUserConfigAllowed(isUserConfigAllowed(i)).setAutoDetectionSupported(isAutoDetectionSupported()).setAutoDetectionEnabledSetting(getAutoDetectionEnabledSetting()).setSystemClockUpdateThresholdMillis(getSystemClockUpdateThresholdMillis()).setSystemClockConfidenceThresholdMillis(getSystemClockConfidenceUpgradeThresholdMillis()).setAutoSuggestionLowerBound(getAutoSuggestionLowerBound()).setManualSuggestionLowerBound(timeDetectorHelper.getManualSuggestionLowerBound()).setSuggestionUpperBound(timeDetectorHelper.getSuggestionUpperBound()).setOriginPriorities(getOriginPriorities()).build();
    }

    private void setAutoDetectionEnabledIfRequired(boolean z) {
        if (getAutoDetectionEnabledSetting() != z) {
            Settings.Global.putInt(this.mCr, "auto_time", z ? 1 : 0);
        }
    }

    private boolean isUserConfigAllowed(int i) {
        return !this.mUserManager.hasUserRestriction("no_config_date_time", UserHandle.of(i));
    }

    private boolean getAutoDetectionEnabledSetting() {
        return Settings.Global.getInt(this.mCr, "auto_time", 1) > 0;
    }

    private boolean isAutoDetectionSupported() {
        for (int i : getOriginPriorities()) {
            if (i == 3 || i == 5 || i == 4) {
                return true;
            }
            if (i == 1 && this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony")) {
                return true;
            }
        }
        return false;
    }

    private int getSystemClockUpdateThresholdMillis() {
        return this.mSystemClockUpdateThresholdMillis;
    }

    private Instant getAutoSuggestionLowerBound() {
        return this.mServerFlags.getOptionalInstant(ServerFlags.KEY_TIME_DETECTOR_LOWER_BOUND_MILLIS_OVERRIDE).orElse(TimeDetectorHelper.INSTANCE.getAutoSuggestionLowerBoundDefault());
    }

    private int[] getOriginPriorities() {
        int[] iArr = this.mServerFlagsOriginPrioritiesSupplier.get();
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = this.mConfigOriginPrioritiesSupplier.get();
        return iArr2 != null ? iArr2 : DEFAULT_AUTOMATIC_TIME_ORIGIN_PRIORITIES;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static abstract class BaseOriginPrioritiesSupplier implements Supplier<int[]> {

        @GuardedBy({"this"})
        private int[] mLastPriorityInts;

        @GuardedBy({"this"})
        private String[] mLastPriorityStrings;

        protected abstract String[] lookupPriorityStrings();

        private BaseOriginPrioritiesSupplier() {
        }

        @Override // java.util.function.Supplier
        public int[] get() {
            String[] lookupPriorityStrings = lookupPriorityStrings();
            synchronized (this) {
                if (Arrays.equals(this.mLastPriorityStrings, lookupPriorityStrings)) {
                    return this.mLastPriorityInts;
                }
                int[] iArr = null;
                if (lookupPriorityStrings != null) {
                    int length = lookupPriorityStrings.length;
                    int[] iArr2 = new int[length];
                    for (int i = 0; i < length; i++) {
                        try {
                            String str = lookupPriorityStrings[i];
                            Preconditions.checkArgument(str != null);
                            iArr2[i] = TimeDetectorStrategy.stringToOrigin(str.trim());
                        } catch (IllegalArgumentException unused) {
                        }
                    }
                    iArr = iArr2;
                }
                this.mLastPriorityStrings = lookupPriorityStrings;
                this.mLastPriorityInts = iArr;
                return iArr;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ConfigOriginPrioritiesSupplier extends BaseOriginPrioritiesSupplier {
        private final Context mContext;

        private ConfigOriginPrioritiesSupplier(Context context) {
            super();
            Objects.requireNonNull(context);
            this.mContext = context;
        }

        @Override // com.android.server.timedetector.ServiceConfigAccessorImpl.BaseOriginPrioritiesSupplier
        protected String[] lookupPriorityStrings() {
            return this.mContext.getResources().getStringArray(R.array.config_brightnessThresholdsOfPeakRefreshRate);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ServerFlagsOriginPrioritiesSupplier extends BaseOriginPrioritiesSupplier {
        private final ServerFlags mServerFlags;

        private ServerFlagsOriginPrioritiesSupplier(ServerFlags serverFlags) {
            super();
            Objects.requireNonNull(serverFlags);
            this.mServerFlags = serverFlags;
        }

        @Override // com.android.server.timedetector.ServiceConfigAccessorImpl.BaseOriginPrioritiesSupplier
        protected String[] lookupPriorityStrings() {
            return this.mServerFlags.getOptionalStringArray(ServerFlags.KEY_TIME_DETECTOR_ORIGIN_PRIORITIES_OVERRIDE).orElse(null);
        }
    }
}
