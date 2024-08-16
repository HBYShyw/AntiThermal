package com.android.server.stats.pull;

import android.content.ContentResolver;
import android.content.Context;
import android.os.SystemProperties;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Base64;
import android.util.Slog;
import android.util.StatsEvent;
import com.android.internal.annotations.VisibleForTesting;
import com.android.service.nano.StringListParamProto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class SettingsStatsUtil {
    private static final String NAMESPACE_OPLUS_SETTINGS_STATS = "settings_ostats";
    private static final String TAG = "SettingsStatsUtil";
    private static final FlagsData[] GLOBAL_SETTINGS = {new FlagsData("GlobalFeature__boolean_whitelist", 1), new FlagsData("GlobalFeature__integer_whitelist", 2), new FlagsData("GlobalFeature__float_whitelist", 3), new FlagsData("GlobalFeature__string_whitelist", 4)};
    private static final FlagsData[] SECURE_SETTINGS = {new FlagsData("SecureFeature__boolean_whitelist", 1), new FlagsData("SecureFeature__integer_whitelist", 2), new FlagsData("SecureFeature__float_whitelist", 3), new FlagsData("SecureFeature__string_whitelist", 4)};
    private static final FlagsData[] SYSTEM_SETTINGS = {new FlagsData("SystemFeature__boolean_whitelist", 1), new FlagsData("SystemFeature__integer_whitelist", 2), new FlagsData("SystemFeature__float_whitelist", 3), new FlagsData("SystemFeature__string_whitelist", 4)};
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.settings.ostats.debug", false);

    SettingsStatsUtil() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public static List<StatsEvent> logGlobalSettings(Context context, int i, int i2) {
        ArrayList arrayList = new ArrayList();
        ContentResolver contentResolver = context.getContentResolver();
        for (FlagsData flagsData : GLOBAL_SETTINGS) {
            StringListParamProto list = getList(flagsData.mFlagName);
            if (list != null) {
                for (String str : list.element) {
                    arrayList.add(createStatsEvent(i, str, Settings.Global.getStringForUser(contentResolver, str, i2), i2, flagsData.mDataType));
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<StatsEvent> logSystemSettings(Context context, int i, int i2) {
        ArrayList arrayList = new ArrayList();
        ContentResolver contentResolver = context.getContentResolver();
        for (FlagsData flagsData : SYSTEM_SETTINGS) {
            StringListParamProto list = getList(flagsData.mFlagName);
            if (list != null) {
                for (String str : list.element) {
                    arrayList.add(createStatsEvent(i, str, Settings.System.getStringForUser(contentResolver, str, i2), i2, flagsData.mDataType));
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<StatsEvent> logSecureSettings(Context context, int i, int i2) {
        ArrayList arrayList = new ArrayList();
        ContentResolver contentResolver = context.getContentResolver();
        for (FlagsData flagsData : SECURE_SETTINGS) {
            StringListParamProto list = getList(flagsData.mFlagName);
            if (list != null) {
                for (String str : list.element) {
                    arrayList.add(createStatsEvent(i, str, Settings.Secure.getStringForUser(contentResolver, str, i2), i2, flagsData.mDataType));
                }
            }
        }
        return arrayList;
    }

    @VisibleForTesting
    static StringListParamProto getList(String str) {
        ArraySet arraySet = new ArraySet();
        StringListParamProto list = getList(str, "settings_stats");
        if (list != null) {
            if (DEBUG) {
                logDetails("settings_stats", str, list.element);
            }
            arraySet.addAll((Collection) Arrays.stream(list.element).collect(Collectors.toList()));
        }
        StringListParamProto list2 = getList(str, NAMESPACE_OPLUS_SETTINGS_STATS);
        if (list2 != null) {
            if (DEBUG) {
                logDetails(NAMESPACE_OPLUS_SETTINGS_STATS, str, list2.element);
            }
            arraySet.addAll((Collection) Arrays.stream(list2.element).collect(Collectors.toList()));
        }
        if (arraySet.isEmpty()) {
            return null;
        }
        StringListParamProto stringListParamProto = new StringListParamProto();
        stringListParamProto.element = (String[]) arraySet.toArray(new IntFunction() { // from class: com.android.server.stats.pull.SettingsStatsUtil$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                String[] lambda$getList$0;
                lambda$getList$0 = SettingsStatsUtil.lambda$getList$0(i);
                return lambda$getList$0;
            }
        });
        if (DEBUG) {
            Slog.d(TAG, "Get string list=" + Arrays.toString(stringListParamProto.element));
        }
        return stringListParamProto;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String[] lambda$getList$0(int i) {
        return new String[i];
    }

    private static void logDetails(String str, String str2, String[] strArr) {
        Slog.d(TAG, String.format("Receive list with namespace:%s flag:%s with elements=%s", str, str2, Arrays.toString(strArr)));
    }

    @VisibleForTesting
    static StringListParamProto getList(String str, String str2) {
        String property = DeviceConfig.getProperty(str2, str);
        if (TextUtils.isEmpty(property)) {
            return null;
        }
        try {
            return StringListParamProto.parseFrom(Base64.decode(property, 3));
        } catch (Exception e) {
            Slog.e(TAG, "Error parsing string list proto", e);
            return null;
        }
    }

    private static StatsEvent createStatsEvent(int i, String str, String str2, int i2, int i3) {
        int i4;
        StatsEvent.Builder writeString = StatsEvent.newBuilder().setAtomId(i).writeString(str);
        boolean z = false;
        float f = 0.0f;
        if (TextUtils.isEmpty(str2)) {
            writeString.writeInt(0).writeBoolean(false).writeInt(0).writeFloat(0.0f).writeString("").writeInt(i2);
        } else {
            if (i3 != 1) {
                if (i3 == 2) {
                    try {
                        i4 = Integer.parseInt(str2);
                    } catch (NumberFormatException unused) {
                        Slog.w(TAG, "Can not parse value to float: " + str2);
                    }
                    str2 = "";
                } else if (i3 == 3) {
                    try {
                        f = Float.parseFloat(str2);
                    } catch (NumberFormatException unused2) {
                        Slog.w(TAG, "Can not parse value to float: " + str2);
                    }
                } else if (i3 != 4) {
                    Slog.w(TAG, "Unexpected value type " + i3);
                } else {
                    i4 = 0;
                }
                i4 = 0;
                str2 = "";
            } else {
                boolean equals = "1".equals(str2);
                str2 = "";
                z = equals;
                i4 = 0;
            }
            writeString.writeInt(i3).writeBoolean(z).writeInt(i4).writeFloat(f).writeString(str2).writeInt(i2);
        }
        return writeString.build();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static final class FlagsData {
        int mDataType;
        String mFlagName;

        FlagsData(String str, int i) {
            this.mFlagName = str;
            this.mDataType = i;
        }
    }
}
