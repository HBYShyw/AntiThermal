package com.android.server.compat.overrides;

import android.app.compat.PackageOverride;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.KeyValueListParser;
import android.util.Pair;
import android.util.Slog;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import libcore.util.HexEncoding;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class AppCompatOverridesParser {
    private static final Pattern BOOLEAN_PATTERN = Pattern.compile("true|false", 2);
    static final String FLAG_OWNED_CHANGE_IDS = "owned_change_ids";
    static final String FLAG_REMOVE_OVERRIDES = "remove_overrides";
    private static final String TAG = "AppCompatOverridesParser";
    private static final String WILDCARD_NO_OWNED_CHANGE_IDS_WARNING = "Wildcard can't be used in 'remove_overrides' flag with an empty owned_change_ids' flag";
    private static final String WILDCARD_SYMBOL = "*";
    private final PackageManager mPackageManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppCompatOverridesParser(PackageManager packageManager) {
        this.mPackageManager = packageManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<String, Set<Long>> parseRemoveOverrides(String str, Set<Long> set) {
        if (str.isEmpty()) {
            return Collections.emptyMap();
        }
        ArrayMap arrayMap = new ArrayMap();
        if (str.equals(WILDCARD_SYMBOL)) {
            if (set.isEmpty()) {
                Slog.w(TAG, WILDCARD_NO_OWNED_CHANGE_IDS_WARNING);
                return Collections.emptyMap();
            }
            Iterator<ApplicationInfo> it = this.mPackageManager.getInstalledApplications(AudioDevice.OUT_SPEAKER_SAFE).iterator();
            while (it.hasNext()) {
                arrayMap.put(it.next().packageName, set);
            }
            return arrayMap;
        }
        KeyValueListParser keyValueListParser = new KeyValueListParser(',');
        try {
            keyValueListParser.setString(str);
            for (int i = 0; i < keyValueListParser.size(); i++) {
                String keyAt = keyValueListParser.keyAt(i);
                String string = keyValueListParser.getString(keyAt, "");
                if (string.equals(WILDCARD_SYMBOL)) {
                    if (set.isEmpty()) {
                        Slog.w(TAG, WILDCARD_NO_OWNED_CHANGE_IDS_WARNING);
                    } else {
                        arrayMap.put(keyAt, set);
                    }
                } else {
                    for (String str2 : string.split(":")) {
                        try {
                            ((Set) arrayMap.computeIfAbsent(keyAt, new Function() { // from class: com.android.server.compat.overrides.AppCompatOverridesParser$$ExternalSyntheticLambda0
                                @Override // java.util.function.Function
                                public final Object apply(Object obj) {
                                    Set lambda$parseRemoveOverrides$0;
                                    lambda$parseRemoveOverrides$0 = AppCompatOverridesParser.lambda$parseRemoveOverrides$0((String) obj);
                                    return lambda$parseRemoveOverrides$0;
                                }
                            })).add(Long.valueOf(Long.parseLong(str2)));
                        } catch (NumberFormatException e) {
                            Slog.w(TAG, "Invalid change ID in 'remove_overrides' flag: " + str2, e);
                        }
                    }
                }
            }
            return arrayMap;
        } catch (IllegalArgumentException e2) {
            Slog.w(TAG, "Invalid format in 'remove_overrides' flag: " + str, e2);
            return Collections.emptyMap();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Set lambda$parseRemoveOverrides$0(String str) {
        return new ArraySet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Set<Long> parseOwnedChangeIds(String str) {
        if (str.isEmpty()) {
            return Collections.emptySet();
        }
        ArraySet arraySet = new ArraySet();
        for (String str2 : str.split(",")) {
            try {
                arraySet.add(Long.valueOf(Long.parseLong(str2)));
            } catch (NumberFormatException e) {
                Slog.w(TAG, "Invalid change ID in 'owned_change_ids' flag: " + str2, e);
            }
        }
        return arraySet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<Long, PackageOverride> parsePackageOverrides(String str, String str2, long j, Set<Long> set) {
        if (str.isEmpty()) {
            return Collections.emptyMap();
        }
        PackageOverrideComparator packageOverrideComparator = new PackageOverrideComparator(j);
        ArrayMap arrayMap = new ArrayMap();
        Pair<String, String> extractSignatureFromConfig = extractSignatureFromConfig(str);
        if (extractSignatureFromConfig == null) {
            return Collections.emptyMap();
        }
        String str3 = (String) extractSignatureFromConfig.first;
        String str4 = (String) extractSignatureFromConfig.second;
        if (!verifySignature(str2, str3)) {
            return Collections.emptyMap();
        }
        for (String str5 : str4.split(",")) {
            List asList = Arrays.asList(str5.split(":", 4));
            if (asList.size() != 4) {
                Slog.w(TAG, "Invalid change override entry: " + str5);
            } else {
                try {
                    long parseLong = Long.parseLong((String) asList.get(0));
                    if (!set.contains(Long.valueOf(parseLong))) {
                        String str6 = (String) asList.get(1);
                        String str7 = (String) asList.get(2);
                        String str8 = (String) asList.get(3);
                        if (BOOLEAN_PATTERN.matcher(str8).matches()) {
                            PackageOverride.Builder enabled = new PackageOverride.Builder().setEnabled(Boolean.parseBoolean(str8));
                            try {
                                if (!str6.isEmpty()) {
                                    enabled.setMinVersionCode(Long.parseLong(str6));
                                }
                                if (!str7.isEmpty()) {
                                    enabled.setMaxVersionCode(Long.parseLong(str7));
                                }
                                try {
                                    PackageOverride build = enabled.build();
                                    if (!arrayMap.containsKey(Long.valueOf(parseLong)) || packageOverrideComparator.compare(build, (PackageOverride) arrayMap.get(Long.valueOf(parseLong))) < 0) {
                                        arrayMap.put(Long.valueOf(parseLong), build);
                                    }
                                } catch (IllegalArgumentException e) {
                                    Slog.w(TAG, "Failed to build PackageOverride", e);
                                }
                            } catch (NumberFormatException e2) {
                                Slog.w(TAG, "Invalid min/max version code in override entry: " + str5, e2);
                            }
                        } else {
                            Slog.w(TAG, "Invalid enabled string in override entry: " + str5);
                        }
                    }
                } catch (NumberFormatException e3) {
                    Slog.w(TAG, "Invalid change ID in override entry: " + str5, e3);
                }
            }
        }
        return arrayMap;
    }

    private static Pair<String, String> extractSignatureFromConfig(String str) {
        List asList = Arrays.asList(str.split("~"));
        if (asList.size() == 1) {
            return Pair.create("", str);
        }
        if (asList.size() > 2) {
            Slog.w(TAG, "Only one signature per config is supported. Config: " + str);
            return null;
        }
        return Pair.create((String) asList.get(0), (String) asList.get(1));
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x001a, code lost:
    
        android.util.Slog.w(com.android.server.compat.overrides.AppCompatOverridesParser.TAG, r5 + " did not have expected signature: " + r6);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean verifySignature(String str, String str2) {
        try {
            boolean z = true;
            if (!str2.isEmpty() && !this.mPackageManager.hasSigningCertificate(str, HexEncoding.decode(str2), 1)) {
                z = false;
            }
            return z;
        } catch (IllegalArgumentException e) {
            Slog.w(TAG, "Unable to verify signature " + str2 + " for " + str, e);
            return false;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static final class PackageOverrideComparator implements Comparator<PackageOverride> {
        private final long mVersionCode;

        PackageOverrideComparator(long j) {
            this.mVersionCode = j;
        }

        @Override // java.util.Comparator
        public int compare(PackageOverride packageOverride, PackageOverride packageOverride2) {
            boolean isVersionInRange = isVersionInRange(packageOverride, this.mVersionCode);
            if (isVersionInRange != isVersionInRange(packageOverride2, this.mVersionCode)) {
                return isVersionInRange ? -1 : 1;
            }
            boolean isVersionAfterRange = isVersionAfterRange(packageOverride, this.mVersionCode);
            if (isVersionAfterRange != isVersionAfterRange(packageOverride2, this.mVersionCode)) {
                return isVersionAfterRange ? -1 : 1;
            }
            return Long.compare(getVersionProximity(packageOverride, this.mVersionCode), getVersionProximity(packageOverride2, this.mVersionCode));
        }

        private static boolean isVersionInRange(PackageOverride packageOverride, long j) {
            return packageOverride.getMinVersionCode() <= j && j <= packageOverride.getMaxVersionCode();
        }

        private static boolean isVersionAfterRange(PackageOverride packageOverride, long j) {
            return packageOverride.getMaxVersionCode() < j;
        }

        private static boolean isVersionBeforeRange(PackageOverride packageOverride, long j) {
            return packageOverride.getMinVersionCode() > j;
        }

        private static long getVersionProximity(PackageOverride packageOverride, long j) {
            if (isVersionAfterRange(packageOverride, j)) {
                return j - packageOverride.getMaxVersionCode();
            }
            if (isVersionBeforeRange(packageOverride, j)) {
                return packageOverride.getMinVersionCode() - j;
            }
            return 0L;
        }
    }
}
