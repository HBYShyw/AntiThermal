package com.oplus.util;

import android.content.res.OplusThemeResources;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.autolayout.IOplusAutoLayoutManager;

/* loaded from: classes.dex */
public class OplusResourcesUtil {
    public static String[][] loadStringArrays(Resources res, int array) {
        TypedArray a = res.obtainTypedArray(array);
        int length = a.length();
        String[][] arrays = new String[length];
        for (int i = 0; i < length; i++) {
            int id = a.getResourceId(i, 0);
            if (id != 0) {
                arrays[i] = res.getStringArray(id);
            }
        }
        a.recycle();
        return arrays;
    }

    public static String dumpResource(Resources res, int id) {
        StringBuilder out = new StringBuilder();
        out.append("[");
        dumpResourceInternal(res, id, out, false);
        out.append("]");
        return out.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void dumpResourceInternal(Resources res, int id, StringBuilder out, boolean usePackageName) {
        String packageName;
        if (res == null || !Resources.resourceHasPackage(id)) {
            return;
        }
        try {
            switch ((-16777216) & id) {
                case 16777216:
                    packageName = OplusThemeResources.FRAMEWORK_PACKAGE;
                    break;
                case 2130706432:
                    packageName = getAppPackageName(usePackageName, res, id);
                    break;
                default:
                    packageName = res.getResourcePackageName(id);
                    break;
            }
            String typeName = res.getResourceTypeName(id);
            String entryName = res.getResourceEntryName(id);
            out.append(packageName);
            out.append(":");
            out.append(typeName);
            out.append("/");
            out.append(entryName);
        } catch (Resources.NotFoundException e) {
        } catch (Exception e2) {
        }
    }

    private static String getAppPackageName(boolean usePackageName, Resources res, int id) {
        if (usePackageName) {
            return res.getResourcePackageName(id);
        }
        return IOplusAutoLayoutManager.APP_POLICY_NAME;
    }
}
