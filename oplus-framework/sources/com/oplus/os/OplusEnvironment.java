package com.oplus.os;

import android.content.Context;
import android.os.EnvironmentExtImpl;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import java.io.File;

/* loaded from: classes.dex */
public class OplusEnvironment {
    private static final String DIR_OPLUS_RESERVE = "/mnt/vendor/oplusreserve";
    private static final String ENV_MY_BIGBALL_ROOT = "MY_BIGBALL_ROOT";
    private static final String ENV_MY_CARRIER_ROOT = "MY_CARRIER_ROOT";
    private static final String ENV_MY_COMPANY_ROOT = "MY_COMPANY_ROOT";
    private static final String ENV_MY_COUNTRY_ROOT = "MY_REGION_ROOT";
    private static final String ENV_MY_ENGINEER_ROOT = "MY_ENGINEERING_ROOT";
    private static final String ENV_MY_HEYTAP_ROOT = "MY_HEYTAP_ROOT";
    private static final String ENV_MY_OPERATOR_ROOT = "MY_CARRIER_ROOT";
    private static final String ENV_MY_PRELOAD_ROOT = "MY_PRELOAD_ROOT";
    private static final String ENV_MY_PRODUCT_ROOT = "MY_PRODUCT_ROOT";
    private static final String ENV_MY_REGION_ROOT = "MY_REGION_ROOT";
    private static final String ENV_MY_RESERVE_ROOT = "MY_RESERVE_ROOT";
    private static final String ENV_MY_STOCK_ROOT = "MY_STOCK_ROOT";
    private static final String ENV_OPLUS_CUSTOM_ROOT = "MY_CUSTOM_ROOT";
    private static final String ENV_OPLUS_ENGINEER_ROOT = "MY_ENGINEERING_ROOT";
    private static final String ENV_OPLUS_OPERATOR_ROOT = "MY_OPERATOR_ROOT";
    private static final String ENV_OPLUS_PRODUCT_ROOT = "MY_PRODUCT_ROOT";
    private static final String ENV_OPLUS_VERSION_ROOT = "MY_VERSION_ROOT";
    public static final boolean NOT_ALLOW_EXT4_ACCESS = true;
    private static String externalSdDir;
    private static String internalSdDir;
    private static StorageManager mStorageManager;
    private static File sDirMyBigballRoot = null;
    private static File DIR_MY_PRELOAD_ROOT = null;
    private static File DIR_MY_HEYTAP_ROOT = null;
    private static File DIR_MY_STOCK_ROOT = null;
    private static File DIR_MY_PRODUCT_ROOT = null;
    private static File DIR_MY_COUNTRY_ROOT = null;
    private static File DIR_MY_REGION_ROOT = null;
    private static File DIR_MY_OPERATOR_ROOT = null;
    private static File DIR_MY_CARRIER_ROOT = null;
    private static File DIR_MY_COMPANY_ROOT = null;
    private static File DIR_MY_ENGINEER_ROOT = null;
    private static File DIR_MY_RESERVE_ROOT = null;
    private static File sDirOplusVersionRoot = null;
    private static File sDirOplusProductRoot = null;
    private static File sDirOplusEngineerRoot = null;
    private static File sDirOplusOperatorRoot = null;
    private static File sDirOplusCustomRoot = null;
    public static final File PARENT_STORAGE_DIRECTORY = getDirectory("EXTERNAL_STORAGE", "/storage/sdcard0");
    public static final File SUB_STORAGE_DIRECTORY = getDirectory("INTERNAL_STORAGE", "/storage/sdcard0/external_sd");

    private static void update(Context context) {
        StorageVolume[] volumes;
        StorageManager storageManager = (StorageManager) context.getSystemService("storage");
        mStorageManager = storageManager;
        if (storageManager == null || (volumes = storageManager.getVolumeList()) == null) {
            return;
        }
        for (int i = 0; i < volumes.length; i++) {
            if (volumes[i].isRemovable()) {
                externalSdDir = volumes[i].getPath();
            } else {
                internalSdDir = volumes[i].getPath();
            }
        }
    }

    public static File getInternalSdDirectory(Context context) {
        update(context);
        if (internalSdDir == null) {
            return null;
        }
        return new File(internalSdDir);
    }

    public static File getExternalSdDirectory(Context context) {
        update(context);
        if (externalSdDir == null) {
            return null;
        }
        return new File(externalSdDir);
    }

    public static String getInternalSdState(Context context) {
        update(context);
        String str = internalSdDir;
        if (str == null) {
            return null;
        }
        return mStorageManager.getVolumeState(str);
    }

    public static String getExternalSdState(Context context) {
        update(context);
        String str = externalSdDir;
        if (str == null) {
            return null;
        }
        return mStorageManager.getVolumeState(str);
    }

    public static boolean isExternalSDRemoved(Context context) {
        update(context);
        String str = externalSdDir;
        if (str == null) {
            return true;
        }
        return "removed".equals(mStorageManager.getVolumeState(str));
    }

    public static File getReserveDirectory() {
        return new File(DIR_OPLUS_RESERVE);
    }

    public static boolean isWhiteListMcp() {
        return EnvironmentExtImpl.getInstance(null).isWhiteListMcp();
    }

    private static File getDirectorySup(String variableName, String defaultPath) {
        String path = System.getenv(variableName);
        return path == null ? new File(defaultPath) : new File(path);
    }

    public static File getOplusCustomDirectory() {
        if (sDirOplusCustomRoot == null) {
            sDirOplusCustomRoot = getDirectorySup(ENV_OPLUS_CUSTOM_ROOT, "/my_company");
        }
        return sDirOplusCustomRoot;
    }

    public static File getOplusCotaDirectory() {
        if (sDirOplusOperatorRoot == null) {
            sDirOplusOperatorRoot = getDirectorySup(ENV_OPLUS_OPERATOR_ROOT, "/my_operator");
        }
        return sDirOplusOperatorRoot;
    }

    public static boolean isOplusCustomDirectoryEmpty() {
        File file = getOplusCustomDirectory();
        File[] listFiles = file.listFiles();
        if (listFiles != null && listFiles.length > 0) {
            return false;
        }
        return true;
    }

    public static boolean isOplusCotaDirectoryEmpty() {
        File file = getOplusCotaDirectory();
        File[] listFiles = file.listFiles();
        if (listFiles != null && listFiles.length > 0) {
            return false;
        }
        return true;
    }

    public static File getResourceDirectory() {
        if (!isOplusCustomDirectoryEmpty()) {
            return getOplusCustomDirectory();
        }
        if (!isOplusCotaDirectoryEmpty()) {
            return getOplusCotaDirectory();
        }
        return getOplusCustomDirectory();
    }

    public static File getOplusProductDirectory() {
        if (sDirOplusProductRoot == null) {
            sDirOplusProductRoot = getDirectorySup("MY_PRODUCT_ROOT", "/oplus_product");
        }
        return sDirOplusProductRoot;
    }

    public static File getOplusEngineerDirectory() {
        if (sDirOplusEngineerRoot == null) {
            sDirOplusEngineerRoot = getDirectorySup("MY_ENGINEERING_ROOT", "/oplus_engineering");
        }
        return sDirOplusEngineerRoot;
    }

    public static File getOplusVersionDirectory() {
        if (sDirOplusVersionRoot == null) {
            sDirOplusVersionRoot = getDirectorySup(ENV_OPLUS_VERSION_ROOT, "/oplus_version");
        }
        return sDirOplusVersionRoot;
    }

    public static File getMyPreloadDirectory() {
        if (DIR_MY_PRELOAD_ROOT == null) {
            DIR_MY_PRELOAD_ROOT = getDirectorySup(ENV_MY_PRELOAD_ROOT, "/my_preload");
        }
        return DIR_MY_PRELOAD_ROOT;
    }

    public static File getMyBigballDirectory() {
        if (sDirMyBigballRoot == null) {
            sDirMyBigballRoot = getDirectorySup(ENV_MY_BIGBALL_ROOT, "/my_bigball");
        }
        return sDirMyBigballRoot;
    }

    public static File getMyHeytapDirectory() {
        if (DIR_MY_HEYTAP_ROOT == null) {
            DIR_MY_HEYTAP_ROOT = getDirectorySup(ENV_MY_HEYTAP_ROOT, "/my_heytap");
        }
        return DIR_MY_HEYTAP_ROOT;
    }

    public static File getMyStockDirectory() {
        if (DIR_MY_STOCK_ROOT == null) {
            DIR_MY_STOCK_ROOT = getDirectorySup(ENV_MY_STOCK_ROOT, "/my_stock");
        }
        return DIR_MY_STOCK_ROOT;
    }

    public static File getMyProductDirectory() {
        if (DIR_MY_PRODUCT_ROOT == null) {
            DIR_MY_PRODUCT_ROOT = getDirectorySup("MY_PRODUCT_ROOT", "/my_product");
        }
        return DIR_MY_PRODUCT_ROOT;
    }

    public static File getMyCountryDirectory() {
        if (DIR_MY_COUNTRY_ROOT == null) {
            DIR_MY_COUNTRY_ROOT = getDirectorySup("MY_REGION_ROOT", "/my_region");
        }
        return DIR_MY_COUNTRY_ROOT;
    }

    public static File getMyRegionDirectory() {
        if (DIR_MY_REGION_ROOT == null) {
            DIR_MY_REGION_ROOT = getDirectorySup("MY_REGION_ROOT", "/my_region");
        }
        return DIR_MY_REGION_ROOT;
    }

    public static File getMyOperatorDirectory() {
        if (DIR_MY_OPERATOR_ROOT == null) {
            DIR_MY_OPERATOR_ROOT = getDirectorySup("MY_CARRIER_ROOT", "/my_carrier");
        }
        return DIR_MY_OPERATOR_ROOT;
    }

    public static File getMyCarrierDirectory() {
        if (DIR_MY_CARRIER_ROOT == null) {
            DIR_MY_CARRIER_ROOT = getDirectorySup("MY_CARRIER_ROOT", "/my_carrier");
        }
        return DIR_MY_CARRIER_ROOT;
    }

    public static File getMyCompanyDirectory() {
        if (DIR_MY_COMPANY_ROOT == null) {
            DIR_MY_COMPANY_ROOT = getDirectorySup(ENV_MY_COMPANY_ROOT, "/my_company");
        }
        return DIR_MY_COMPANY_ROOT;
    }

    public static File getMyEngineeringDirectory() {
        if (DIR_MY_ENGINEER_ROOT == null) {
            DIR_MY_ENGINEER_ROOT = getDirectorySup("MY_ENGINEERING_ROOT", "/my_engineering");
        }
        return DIR_MY_ENGINEER_ROOT;
    }

    public static File getMyReserveDirectory() {
        if (DIR_MY_RESERVE_ROOT == null) {
            DIR_MY_RESERVE_ROOT = getDirectorySup(ENV_MY_RESERVE_ROOT, "/my_reserve");
        }
        return DIR_MY_RESERVE_ROOT;
    }

    static File getDirectory(String variableName, String defaultPath) {
        String path = System.getenv(variableName);
        return path == null ? new File(defaultPath) : new File(path);
    }
}
