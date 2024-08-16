package android.app;

import android.app.OplusUxIconConstants;
import android.content.res.OplusThemeResources;
import android.content.res.Resources;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.collect.Sets;
import com.oplus.util.OplusUXIconData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes.dex */
public class OplusUxIconAppCheckUtils {
    private static final String TAG = "OplusUxIconAppCheckUtils";
    private static final int UX_DESK_APP_PACKAGE = 201785389;
    private static final int UX_PRESET_APP_PACKAGE = 201785388;
    private static HashSet<String> PRESET_APPS_LIST = null;
    private static Set<String> DESK_ACTIVITY_LIST = null;
    private static final Set<String> SYSTEM_APP_LIST = Sets.newHashSet(new String[]{"com.android.systemui", "com.android.settings", "com.android.browser", "com.android.calculator2", "com.android.calendar", OplusUxIconConstants.IconLoader.COM_ANDROID_CONTACTS, "com.android.launcher", "com.android.mms", "com.android.packageinstaller", "com.android.permissioncontroller", "com.oplus.eyeprotect", OplusThemeResources.FRAMEWORK_PACKAGE});

    public static boolean isDeskActivity(Resources resources, String packageName) {
        if (DESK_ACTIVITY_LIST == null) {
            String[] packages = resources.getStringArray(UX_DESK_APP_PACKAGE);
            DESK_ACTIVITY_LIST = new HashSet(Arrays.asList(packages));
        }
        return DESK_ACTIVITY_LIST.contains(packageName);
    }

    public static boolean isPresetApp(Resources resources, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        if (PRESET_APPS_LIST == null) {
            try {
                OplusActivityManager mOplusActivityManager = new OplusActivityManager();
                PRESET_APPS_LIST = new HashSet<>();
                OplusUXIconData data = mOplusActivityManager.getUXIconData();
                PRESET_APPS_LIST = new HashSet<>((ArrayList) data.getPresetAppList());
                Log.d(TAG, "preset list = " + PRESET_APPS_LIST);
            } catch (RemoteException e) {
                Log.e(TAG, "init data RemoteException , " + e);
                e.printStackTrace();
            } catch (Exception e2) {
                Log.e(TAG, "init data Exception , " + e2);
                e2.printStackTrace();
            }
            if (PRESET_APPS_LIST.isEmpty()) {
                String[] packages = resources.getStringArray(UX_PRESET_APP_PACKAGE);
                PRESET_APPS_LIST = new HashSet<>(Arrays.asList(packages));
            }
        }
        return PRESET_APPS_LIST.contains(packageName);
    }

    public static boolean isSystemApp(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        return SYSTEM_APP_LIST.contains(packageName);
    }

    public static void resetPresetAppsList() {
        PRESET_APPS_LIST = null;
    }
}
