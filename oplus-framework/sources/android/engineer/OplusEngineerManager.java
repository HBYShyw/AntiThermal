package android.engineer;

import android.engineer.IOplusEngineerManager;
import android.os.RemoteException;
import android.os.ServiceManager;

/* loaded from: classes.dex */
public class OplusEngineerManager {
    private static final String ENGINEER_SERVICE_NAME = "engineer";

    private OplusEngineerManager() {
    }

    private static IOplusEngineerManager init() {
        return IOplusEngineerManager.Stub.asInterface(ServiceManager.getService(ENGINEER_SERVICE_NAME));
    }

    public static String getDownloadStatus() {
        IOplusEngineerManager manager = init();
        if (manager != null) {
            try {
                return manager.getDownloadStatus();
            } catch (RemoteException e) {
                return null;
            }
        }
        return null;
    }

    public static String getCarrierVersion() {
        IOplusEngineerManager manager = init();
        if (manager != null) {
            try {
                return manager.getCarrierVersion();
            } catch (RemoteException e) {
                return null;
            }
        }
        return null;
    }

    public static boolean setCarrierVersion(String version) {
        IOplusEngineerManager manager = init();
        if (manager != null) {
            try {
                return manager.setCarrierVersion(version);
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    public static byte[] getCarrierVersionFromNvram() {
        IOplusEngineerManager manager = init();
        if (manager != null) {
            try {
                return manager.getCarrierVersionFromNvram();
            } catch (RemoteException e) {
                return null;
            }
        }
        return null;
    }

    public static boolean saveCarrierVersionToNvram(byte[] version) {
        IOplusEngineerManager manager = init();
        if (manager != null) {
            try {
                return manager.saveCarrierVersionToNvram(version);
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    public static byte[] getCalibrationStatusFromNvram() {
        IOplusEngineerManager manager = init();
        if (manager != null) {
            try {
                return manager.getCalibrationStatusFromNvram();
            } catch (RemoteException e) {
                return null;
            }
        }
        return null;
    }

    public static String getSimOperatorSwitchStatus() {
        IOplusEngineerManager manager = init();
        if (manager != null) {
            try {
                return manager.getSimOperatorSwitchStatus();
            } catch (RemoteException e) {
                return null;
            }
        }
        return null;
    }

    public static boolean setSimOperatorSwitch(String state) {
        IOplusEngineerManager manager = init();
        if (manager != null) {
            try {
                return manager.setSimOperatorSwitch(state);
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    public static String getBootImgWaterMark() {
        IOplusEngineerManager manager = init();
        if (manager != null) {
            try {
                return manager.getBootImgWaterMark();
            } catch (RemoteException e) {
                return null;
            }
        }
        return null;
    }

    public static byte[] readEngineerData(int type) {
        IOplusEngineerManager manager = init();
        if (manager != null) {
            try {
                return manager.readEngineerData(type);
            } catch (RemoteException e) {
                return null;
            }
        }
        return null;
    }

    public static boolean saveEngineerData(int type, byte[] engineerData, int length) {
        IOplusEngineerManager manager = init();
        if (manager != null) {
            try {
                return manager.saveEngineerData(type, engineerData, length);
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean fastbootUnlock(byte[] data, int length) {
        IOplusEngineerManager manager = init();
        if (manager != null) {
            try {
                return manager.fastbootUnlock(data, length);
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    public static void setSystemProperties(String key, String val) {
        IOplusEngineerManager manager = init();
        if (manager != null) {
            try {
                manager.setSystemProperties(key, val);
            } catch (RemoteException e) {
            }
        }
    }

    public static String getSystemProperties(String key, String val) {
        IOplusEngineerManager manager = init();
        if (manager != null) {
            try {
                return manager.getSystemProperties(key, val);
            } catch (RemoteException e) {
                return null;
            }
        }
        return null;
    }

    public static boolean isEngineerItemInBlackList(int type, String item) {
        IOplusEngineerManager manager = init();
        if (manager != null) {
            try {
                return manager.isEngineerItemInBlackList(type, item);
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }
}
