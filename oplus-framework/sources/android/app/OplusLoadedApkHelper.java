package android.app;

import android.content.pm.ApplicationInfo;
import android.content.pm.IApplicationInfoExt;
import android.util.Slog;
import dalvik.system.VMRuntime;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;

/* loaded from: classes.dex */
public class OplusLoadedApkHelper {
    public static void addSpecialLibraries(ApplicationInfo aInfo, List<String> outLibPaths) {
        int len;
        String dirPrefix;
        if (aInfo == null) {
            return;
        }
        Object libDirsObj = null;
        try {
            Class cls = Class.forName("android.content.pm.ApplicationInfo");
            Field field = cls.getDeclaredField("specialNativeLibraryDirs");
            field.setAccessible(true);
            libDirsObj = field.get(aInfo);
        } catch (Exception e) {
            Slog.e("OplusLoadedApkHelper", "addSpecialLibraries failed for get specialNativeLibraryDirs!", e);
        }
        if (libDirsObj != null && libDirsObj.getClass().isArray() && (len = Array.getLength(libDirsObj)) > 0) {
            if (VMRuntime.getRuntime().is64Bit()) {
                dirPrefix = "/system/lib64/";
            } else {
                dirPrefix = "/system/lib/";
            }
            for (int index = 0; index < len; index++) {
                Object item = Array.get(libDirsObj, index);
                if (item != null && (item instanceof String)) {
                    String itemValue = (String) item;
                    if (!outLibPaths.contains(itemValue)) {
                        outLibPaths.add(0, dirPrefix + itemValue);
                    }
                }
            }
        }
        int oppoPrivateFlagsValue = 0;
        IApplicationInfoExt appInfoExt = aInfo == null ? null : aInfo.mApplicationInfoExt;
        if (appInfoExt != null) {
            oppoPrivateFlagsValue = appInfoExt.getPrivateFlags();
        }
        if ((oppoPrivateFlagsValue & 4) != 0) {
            outLibPaths.add(System.getProperty("java.library.path"));
        }
    }

    public static void addSpecialZipPaths(ApplicationInfo aInfo, List<String> outZipPaths) {
    }
}
