package com.oplus.cust;

import android.os.SystemProperties;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Slog;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class OplusCfgFilePolicy {
    private static final String CARRIER_ID_PROP = "sys.carrier_id";
    public static final int DEFAULT_SLOT = -2;
    private static final String DELIMITER = ":";
    private static final String TAG = "OplusCfgFilePolicy";
    private static final int SIM_NUM = TelephonyManager.getDefault().getActiveModemCount();
    private static String[] sCfgDirs = null;

    static {
        String policy = System.getenv("CUST_LEVEL_LIST");
        refreshCustDirPolicy(policy);
    }

    private OplusCfgFilePolicy() {
    }

    public static List<File> getCfgFileList(String fileName, String pathPrefix, int slotId) {
        return getCfgFileListCommon(fileName, pathPrefix, slotId);
    }

    private static List<File> getCfgFileListCommon(String fileName, String pathPrefix, int slotId) {
        List<File> res = new ArrayList<>();
        if (TextUtils.isEmpty(fileName)) {
            Slog.e(TAG, "Error: file = [" + fileName + "]");
            return res;
        }
        List<String> cfgPolicyDirs = getCfgLevelList(pathPrefix, slotId);
        for (String str : cfgPolicyDirs) {
            File file = new File(str, fileName);
            if (file.exists()) {
                res.add(file);
            }
        }
        return res;
    }

    public static File getCfgTopPriorityFile(String fileName, String pathPrefix, int slotId) {
        return getCfgTopPriorityFileCommon(fileName, pathPrefix, slotId);
    }

    private static File getCfgTopPriorityFileCommon(String fileName, String pathPrefix, int slotId) {
        List<String> dirs = getCfgLevelList(pathPrefix, slotId);
        for (int idx = dirs.size() - 1; idx >= 0; idx--) {
            File file = new File(dirs.get(idx), fileName);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    public static List<String> getCfgLevelList(String pathPrefix, int slotId) {
        return getCfgLevelListCommon(pathPrefix, slotId);
    }

    private static List<String> getCfgLevelListCommon(String pathPrefix, int slotId) {
        String[] strArr = sCfgDirs;
        if (strArr == null) {
            return new ArrayList(0);
        }
        String[] dirs = (String[]) strArr.clone();
        return parseCarrierPath(dirs, pathPrefix, getCarrierId(slotId));
    }

    public static String getCarrierId(int slotId) {
        int theSlotId = slotId;
        if (theSlotId == -2) {
            theSlotId = getDefaultSlotId();
        }
        if (isValidSlot(theSlotId)) {
            return SystemProperties.get(CARRIER_ID_PROP + theSlotId, (String) null);
        }
        return null;
    }

    private static boolean isValidSlot(int slotId) {
        return slotId >= 0 && slotId < SIM_NUM;
    }

    private static int getDefaultSlotId() {
        int slotId = SubscriptionManager.getSlotIndex(SubscriptionManager.getDefaultSubscriptionId());
        if (slotId == -1) {
            return 0;
        }
        return slotId;
    }

    private static List<String> parseCarrierPath(String[] dirs, String pathPrefix, String carrierId) {
        if (TextUtils.isEmpty(pathPrefix)) {
            return Arrays.asList(dirs);
        }
        boolean emptyCarrierId = TextUtils.isEmpty(carrierId);
        String parentCarrierId = getParentCarrierId(carrierId);
        boolean hasParentCarrierId = !TextUtils.isEmpty(parentCarrierId);
        List<String> paths = new ArrayList<>();
        for (int i = 0; i < dirs.length; i++) {
            File pathPrefixFullPath = new File(dirs[i], pathPrefix);
            if (pathPrefixFullPath.exists()) {
                try {
                    paths.add(pathPrefixFullPath.getCanonicalPath());
                    if (!emptyCarrierId) {
                        if (hasParentCarrierId) {
                            addCarrierPathIfExist(paths, dirs[i], pathPrefix, parentCarrierId);
                        }
                        addCarrierPathIfExist(paths, dirs[i], pathPrefix, carrierId);
                    }
                } catch (IOException e) {
                }
            }
        }
        return paths;
    }

    private static void addCarrierPathIfExist(List<String> paths, String rootPath, String pathPrefix, String carrierId) {
        File carrierListPath = new File(rootPath, concactDirPath(pathPrefix, carrierId) + "/carrierReady");
        if (carrierListPath.exists()) {
            try {
                paths.add(carrierListPath.getCanonicalPath());
            } catch (IOException e) {
            }
        }
    }

    private static String getParentCarrierId(String carrierId) {
        int idxS;
        int idxUnderline;
        if (TextUtils.isEmpty(carrierId) || (idxS = carrierId.indexOf(83)) == -1 || idxS < 1 || (idxUnderline = carrierId.indexOf(95)) <= idxS || idxUnderline == carrierId.length() - 1) {
            return null;
        }
        return carrierId.substring(0, idxS) + carrierId.substring(idxUnderline, carrierId.length());
    }

    private static String concactDirPath(String rootPath, String subPath) {
        if (TextUtils.isEmpty(rootPath)) {
            return subPath;
        }
        if (TextUtils.isEmpty(subPath)) {
            return rootPath;
        }
        char rootPathLastChar = rootPath.charAt(rootPath.length() - 1);
        char subPathFirstChar = subPath.charAt(0);
        if (rootPathLastChar == '/') {
            if (subPathFirstChar == '/') {
                return rootPath + subPath.substring(1, subPath.length());
            }
            return rootPath + subPath;
        }
        if (subPathFirstChar != '/') {
            return rootPath + '/' + subPath;
        }
        return rootPath + subPath;
    }

    private static void refreshCustDirPolicy(String policy) {
        if (TextUtils.isEmpty(policy)) {
            return;
        }
        sCfgDirs = policy.split(DELIMITER);
    }
}
