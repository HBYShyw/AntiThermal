package com.oplus.oms.split.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes.dex */
public final class SplitAABInfoProvider {
    private static final String KEY_MODULES = "shadow.bundletool.com.android.dynamic.apk.fused.modules";
    private static final String REGEX_RULE = "\\.config\\.";
    private static final String START_WITH_CONFIG = "config.";
    private static final String TAG = "SplitAABInfoProvider";
    private Context mContext;
    private String mPackageName;

    public SplitAABInfoProvider(Context context) {
        if (context != null) {
            this.mPackageName = context.getPackageName();
            this.mContext = context;
        }
    }

    public Set<String> getInstalledSplitsForAAB() {
        Set<String> installedModules = getFusedModules();
        String[] splitNames = getSplitInstallInfo();
        if (splitNames == null) {
            SplitLog.d(TAG, "No splits are found or app cannot be found in package manager.", new Object[0]);
            return installedModules;
        }
        String splitNamesStr = Arrays.toString(splitNames);
        SplitLog.d(TAG, splitNamesStr.length() != 0 ? "Split names are: ".concat(splitNamesStr) : "Split names are: ", new Object[0]);
        for (String splitName : splitNames) {
            if (!splitName.startsWith(START_WITH_CONFIG)) {
                installedModules.add(cutSplitName(splitName));
            }
        }
        return installedModules;
    }

    private Set<String> getFusedModules() {
        Set<String> fusedModules = new HashSet<>();
        Context context = this.mContext;
        if (context == null) {
            return fusedModules;
        }
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(this.mPackageName, 128);
            if (appInfo == null || appInfo.metaData == null) {
                SplitLog.d(TAG, "App has no applicationInfo or metaData", new Object[0]);
            } else {
                String fusedName = appInfo.metaData.getString(KEY_MODULES);
                if (fusedName == null || fusedName.isEmpty()) {
                    SplitLog.d(TAG, "App has no fused modules.", new Object[0]);
                } else {
                    Collections.addAll(fusedModules, fusedName.split(",", -1));
                    fusedModules.remove("");
                }
            }
            return fusedModules;
        } catch (PackageManager.NameNotFoundException e) {
            SplitLog.printErrStackTrace(TAG, e, "App is not found in PackageManager", new Object[0]);
            return fusedModules;
        }
    }

    private String cutSplitName(String splitName) {
        if (TextUtils.isEmpty(splitName)) {
            return null;
        }
        return splitName.split(REGEX_RULE)[0];
    }

    private String[] getSplitInstallInfo() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(this.mPackageName, 0);
            if (packageInfo != null) {
                return packageInfo.splitNames;
            }
            return null;
        } catch (PackageManager.NameNotFoundException var2) {
            SplitLog.printErrStackTrace(TAG, var2, "App is not found in PackageManager", new Object[0]);
            return null;
        }
    }
}
