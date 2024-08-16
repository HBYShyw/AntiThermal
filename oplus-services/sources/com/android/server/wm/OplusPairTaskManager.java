package com.android.server.wm;

import android.content.ComponentName;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutServiceInternal;
import android.os.Bundle;
import android.os.SystemProperties;
import android.text.TextUtils;
import com.oplus.splitscreen.ISplitScreenManagerExt;
import java.util.ArrayList;
import java.util.List;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class OplusPairTaskManager {
    public static final boolean DEBUG = true;
    public static final OplusPairTaskManager EMPTY = new OplusPairTaskManager();
    private static final boolean PAIR_TASK_ENABLED = SystemProperties.getBoolean("persist.sys.pair_split_feature_enable", true);

    public static boolean isPairTaskEnabled() {
        return PAIR_TASK_ENABLED && ((ISplitScreenManagerExt) ExtLoader.type(ISplitScreenManagerExt.class).create()).hasLargeScreenFeature();
    }

    public static OplusPairTaskManager getInstance(ActivityTaskManagerService activityTaskManagerService) {
        if (isPairTaskEnabled()) {
            try {
                return (OplusPairTaskManager) Class.forName("com.android.server.wm.OplusPairTaskManagerImpl").getDeclaredConstructor(ActivityTaskManagerService.class).newInstance(activityTaskManagerService);
            } catch (Exception unused) {
                return EMPTY;
            }
        }
        return EMPTY;
    }

    public static Bundle prepareOptionsBeforeStartShortcut(Bundle bundle, ShortcutServiceInternal shortcutServiceInternal, String str, int i, String str2, String str3, int i2, int i3, int i4) {
        ShortcutInfo shortcutInfo;
        if (!isPairTaskEnabled()) {
            return bundle;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        List shortcuts = shortcutServiceInternal.getShortcuts(i, str2, 0L, str3, arrayList, (List) null, (ComponentName) null, 1026, i2, i3, i4);
        if (shortcuts == null || shortcuts.size() <= 0 || (shortcutInfo = (ShortcutInfo) shortcuts.get(0)) == null || shortcutInfo.getExtras() == null) {
            return bundle;
        }
        Bundle bundle2 = bundle == null ? new Bundle() : bundle;
        bundle2.putAll(shortcutInfo.getExtras());
        return bundle2;
    }

    public static boolean isSplitScreenCombination(String str, ShortcutInfo shortcutInfo) {
        if (isPairTaskEnabled() && !TextUtils.isEmpty(str) && shortcutInfo != null && "com.android.systemui".equals(str)) {
            return isSplitScreenCombination(new Bundle(shortcutInfo.getExtras()));
        }
        return false;
    }

    public static boolean isSplitScreenCombination(Bundle bundle) {
        if (bundle != null) {
            return bundle.getBoolean("isSplitScreenCombination", false) || bundle.getBoolean("isPsSplitScreenCombination", false);
        }
        return false;
    }
}
