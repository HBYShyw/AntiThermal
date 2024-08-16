package com.android.server.compat;

import android.app.compat.PackageOverride;
import android.content.pm.ApplicationInfo;
import com.android.internal.compat.AndroidBuildClassifier;
import com.android.internal.compat.CompatibilityChangeInfo;
import com.android.internal.compat.OverrideAllowedState;
import com.android.server.compat.config.Change;
import com.android.server.compat.overrides.ChangeOverrides;
import com.android.server.compat.overrides.OverrideValue;
import com.android.server.compat.overrides.RawOverrideValue;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class CompatChange extends CompatibilityChangeInfo {
    static final long CTS_SYSTEM_API_CHANGEID = 149391281;
    static final long CTS_SYSTEM_API_OVERRIDABLE_CHANGEID = 174043039;
    private ConcurrentHashMap<String, Boolean> mEvaluatedOverrides;
    ChangeListener mListener;
    private ConcurrentHashMap<String, PackageOverride> mRawOverrides;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface ChangeListener {
        void onCompatChange(String str);
    }

    public CompatChange(long j) {
        this(j, null, -1, -1, false, false, null, false);
    }

    public CompatChange(Change change) {
        this(change.getId(), change.getName(), change.getEnableAfterTargetSdk(), change.getEnableSinceTargetSdk(), change.getDisabled(), change.getLoggingOnly(), change.getDescription(), change.getOverridable());
    }

    public CompatChange(long j, String str, int i, int i2, boolean z, boolean z2, String str2, boolean z3) {
        super(Long.valueOf(j), str, i, i2, z, z2, str2, z3);
        this.mListener = null;
        this.mEvaluatedOverrides = new ConcurrentHashMap<>();
        this.mRawOverrides = new ConcurrentHashMap<>();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void registerListener(ChangeListener changeListener) {
        if (this.mListener != null) {
            throw new IllegalStateException("Listener for change " + toString() + " already registered.");
        }
        this.mListener = changeListener;
    }

    private void addPackageOverrideInternal(String str, boolean z) {
        if (getLoggingOnly()) {
            throw new IllegalArgumentException("Can't add overrides for a logging only change " + toString());
        }
        this.mEvaluatedOverrides.put(str, Boolean.valueOf(z));
        notifyListener(str);
    }

    private void removePackageOverrideInternal(String str) {
        if (this.mEvaluatedOverrides.remove(str) != null) {
            notifyListener(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void addPackageOverride(String str, PackageOverride packageOverride, OverrideAllowedState overrideAllowedState, Long l) {
        if (getLoggingOnly()) {
            throw new IllegalArgumentException("Can't add overrides for a logging only change " + toString());
        }
        this.mRawOverrides.put(str, packageOverride);
        recheckOverride(str, overrideAllowedState, l);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean recheckOverride(String str, OverrideAllowedState overrideAllowedState, Long l) {
        if (str == null) {
            return false;
        }
        boolean z = overrideAllowedState.state == 0;
        if (l != null && this.mRawOverrides.containsKey(str) && z) {
            int evaluate = this.mRawOverrides.get(str).evaluate(l.longValue());
            if (evaluate == 0) {
                removePackageOverrideInternal(str);
            } else if (evaluate == 1) {
                addPackageOverrideInternal(str, true);
            } else if (evaluate == 2) {
                addPackageOverrideInternal(str, false);
            }
            return true;
        }
        removePackageOverrideInternal(str);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean removePackageOverride(String str, OverrideAllowedState overrideAllowedState, Long l) {
        if (!this.mRawOverrides.containsKey(str)) {
            return false;
        }
        overrideAllowedState.enforce(getId(), str);
        this.mRawOverrides.remove(str);
        recheckOverride(str, overrideAllowedState, l);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isEnabled(ApplicationInfo applicationInfo, AndroidBuildClassifier androidBuildClassifier) {
        Boolean bool;
        if (applicationInfo == null) {
            return defaultValue();
        }
        String str = applicationInfo.packageName;
        if (str != null && (bool = this.mEvaluatedOverrides.get(str)) != null) {
            return bool.booleanValue();
        }
        if (getDisabled()) {
            return false;
        }
        if (getEnableSinceTargetSdk() == -1) {
            return true;
        }
        int min = Math.min(applicationInfo.targetSdkVersion, androidBuildClassifier.platformTargetSdk());
        int i = applicationInfo.targetSdkVersion;
        if (min != i) {
            min = i;
        }
        return min >= getEnableSinceTargetSdk();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean willBeEnabled(String str) {
        if (str == null) {
            return defaultValue();
        }
        PackageOverride packageOverride = this.mRawOverrides.get(str);
        if (packageOverride != null) {
            int evaluateForAllVersions = packageOverride.evaluateForAllVersions();
            if (evaluateForAllVersions == 0) {
                return defaultValue();
            }
            if (evaluateForAllVersions == 1) {
                return true;
            }
            if (evaluateForAllVersions == 2) {
                return false;
            }
        }
        return defaultValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean defaultValue() {
        return !getDisabled();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void clearOverrides() {
        this.mRawOverrides.clear();
        this.mEvaluatedOverrides.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void loadOverrides(ChangeOverrides changeOverrides) {
        if (changeOverrides.getDeferred() != null) {
            for (OverrideValue overrideValue : changeOverrides.getDeferred().getOverrideValue()) {
                this.mRawOverrides.put(overrideValue.getPackageName(), new PackageOverride.Builder().setEnabled(overrideValue.getEnabled()).build());
            }
        }
        if (changeOverrides.getValidated() != null) {
            for (OverrideValue overrideValue2 : changeOverrides.getValidated().getOverrideValue()) {
                this.mEvaluatedOverrides.put(overrideValue2.getPackageName(), Boolean.valueOf(overrideValue2.getEnabled()));
                this.mRawOverrides.put(overrideValue2.getPackageName(), new PackageOverride.Builder().setEnabled(overrideValue2.getEnabled()).build());
            }
        }
        if (changeOverrides.getRaw() != null) {
            for (RawOverrideValue rawOverrideValue : changeOverrides.getRaw().getRawOverrideValue()) {
                this.mRawOverrides.put(rawOverrideValue.getPackageName(), new PackageOverride.Builder().setMinVersionCode(rawOverrideValue.getMinVersionCode()).setMaxVersionCode(rawOverrideValue.getMaxVersionCode()).setEnabled(rawOverrideValue.getEnabled()).build());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ChangeOverrides saveOverrides() {
        if (this.mRawOverrides.isEmpty()) {
            return null;
        }
        ChangeOverrides changeOverrides = new ChangeOverrides();
        changeOverrides.setChangeId(getId());
        ChangeOverrides.Raw raw = new ChangeOverrides.Raw();
        List<RawOverrideValue> rawOverrideValue = raw.getRawOverrideValue();
        for (Map.Entry<String, PackageOverride> entry : this.mRawOverrides.entrySet()) {
            RawOverrideValue rawOverrideValue2 = new RawOverrideValue();
            rawOverrideValue2.setPackageName(entry.getKey());
            rawOverrideValue2.setMinVersionCode(entry.getValue().getMinVersionCode());
            rawOverrideValue2.setMaxVersionCode(entry.getValue().getMaxVersionCode());
            rawOverrideValue2.setEnabled(entry.getValue().isEnabled());
            rawOverrideValue.add(rawOverrideValue2);
        }
        changeOverrides.setRaw(raw);
        ChangeOverrides.Validated validated = new ChangeOverrides.Validated();
        List<OverrideValue> overrideValue = validated.getOverrideValue();
        for (Map.Entry<String, Boolean> entry2 : this.mEvaluatedOverrides.entrySet()) {
            OverrideValue overrideValue2 = new OverrideValue();
            overrideValue2.setPackageName(entry2.getKey());
            overrideValue2.setEnabled(entry2.getValue().booleanValue());
            overrideValue.add(overrideValue2);
        }
        changeOverrides.setValidated(validated);
        return changeOverrides;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ChangeId(");
        sb.append(getId());
        if (getName() != null) {
            sb.append("; name=");
            sb.append(getName());
        }
        if (getEnableSinceTargetSdk() != -1) {
            sb.append("; enableSinceTargetSdk=");
            sb.append(getEnableSinceTargetSdk());
        }
        if (getDisabled()) {
            sb.append("; disabled");
        }
        if (getLoggingOnly()) {
            sb.append("; loggingOnly");
        }
        if (!this.mEvaluatedOverrides.isEmpty()) {
            sb.append("; packageOverrides=");
            sb.append(this.mEvaluatedOverrides);
        }
        if (!this.mRawOverrides.isEmpty()) {
            sb.append("; rawOverrides=");
            sb.append(this.mRawOverrides);
        }
        if (getOverridable()) {
            sb.append("; overridable");
        }
        sb.append(")");
        return sb.toString();
    }

    private synchronized void notifyListener(String str) {
        ChangeListener changeListener = this.mListener;
        if (changeListener != null) {
            changeListener.onCompatChange(str);
        }
    }
}
