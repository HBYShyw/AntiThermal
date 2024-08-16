package android.os.storage;

import android.app.AppGlobals;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.storage.IOplusStorageManagerService;
import android.util.Log;
import android.util.Slog;
import com.oplus.view.OplusWindowUtils;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusStorageManager {
    private static final int NUM_SYSTEM_ID = 1000;
    private static final String SERVICE_NAME = "oplusstoragemanagerservice";
    private static final String TAG = "OplusStorageManager";
    private static OplusStorageManager sInstance = new OplusStorageManager();
    private int mCallingUid = 0;
    private IOplusStorageManagerService mOplusStorageManagerService;

    private OplusStorageManager() {
    }

    public static synchronized OplusStorageManager getInstance() {
        OplusStorageManager oplusStorageManager;
        synchronized (OplusStorageManager.class) {
            oplusStorageManager = sInstance;
        }
        return oplusStorageManager;
    }

    public void addAuthResultInfo(Context context, int uid, int pid, int permBits, String packageName) {
        if (uid == 0) {
            throw new IllegalArgumentException("uid was 0, which is illegal.");
        }
        if (pid == 0) {
            throw new IllegalArgumentException("pid was 0, which is illegal.");
        }
        if (packageName == null) {
            throw new IllegalArgumentException("packageName was null, which is illegal.");
        }
        Log.d(TAG, "addAuthResultInfo permBits = " + permBits + "  packageName = " + packageName);
        IBinder binder = ServiceManager.getService(SERVICE_NAME);
        if (binder == null) {
            Slog.d(TAG, "get oplusstoragemanagerservice is null");
        } else if (this.mOplusStorageManagerService != IOplusStorageManagerService.Stub.asInterface(binder)) {
            this.mOplusStorageManagerService = IOplusStorageManagerService.Stub.asInterface(binder);
        }
        try {
            IOplusStorageManagerService iOplusStorageManagerService = this.mOplusStorageManagerService;
            if (iOplusStorageManagerService != null) {
                iOplusStorageManagerService.addAuthResultInfo(uid, pid, permBits, packageName);
            } else {
                Slog.d(TAG, "oplusstoragemanagerservice addAuthResultInfo is null");
            }
            Log.d(TAG, "addAuthResultInfo");
        } catch (RemoteException e) {
            Log.e(TAG, "mount service not found");
        }
    }

    public Map<String, byte[]> encryptDek(byte[] dek, int protectType, byte[] protectedKek, byte[] deviceNonce, byte[] kekID) {
        if (dek == null || protectedKek == null || deviceNonce == null || kekID == null) {
            Log.d(TAG, "Failure to encrypt data, encryptDek input byte is null");
            return null;
        }
        IBinder binder = ServiceManager.getService(SERVICE_NAME);
        if (binder == null) {
            Slog.d(TAG, "Failure to encrypt data, get oplusstoragemanagerservice is null");
        } else if (this.mOplusStorageManagerService != IOplusStorageManagerService.Stub.asInterface(binder)) {
            this.mOplusStorageManagerService = IOplusStorageManagerService.Stub.asInterface(binder);
        }
        Map<String, byte[]> kekData = null;
        try {
            IOplusStorageManagerService iOplusStorageManagerService = this.mOplusStorageManagerService;
            if (iOplusStorageManagerService == null) {
                Slog.d(TAG, "Failure to encrypt data, oplusstoragemanagerservice encryptDek is null");
            } else {
                kekData = iOplusStorageManagerService.encryptDek(dek, protectType, protectedKek, deviceNonce, kekID);
            }
            Log.d(TAG, "encryptDek");
        } catch (RemoteException e) {
            Log.e(TAG, "Failure to encrypt data, mount service not found");
        }
        return kekData;
    }

    public byte[] decryptDek(byte[] protectedDek, int protectType, byte[] protectedKek, byte[] deviceNonce, byte[] kekID, byte[] appNonce) {
        if (protectedDek == null || protectedKek == null || deviceNonce == null || kekID == null) {
            Log.d(TAG, "Failure to decrypt data, decryptDek input byte is null");
            return null;
        }
        IBinder binder = ServiceManager.getService(SERVICE_NAME);
        if (binder == null) {
            Slog.d(TAG, "Failure to decrypt data, get oplusstoragemanagerservice is null");
        } else if (this.mOplusStorageManagerService != IOplusStorageManagerService.Stub.asInterface(binder)) {
            this.mOplusStorageManagerService = IOplusStorageManagerService.Stub.asInterface(binder);
        }
        byte[] dekData = null;
        try {
            IOplusStorageManagerService iOplusStorageManagerService = this.mOplusStorageManagerService;
            if (iOplusStorageManagerService != null) {
                dekData = iOplusStorageManagerService.decryptDek(protectedDek, protectType, protectedKek, deviceNonce, kekID, appNonce);
            } else {
                Slog.d(TAG, "Failure to decrypt data, oplusstoragemanagerservice decryptDek is null");
            }
            Log.d(TAG, "decryptDek");
        } catch (RemoteException e) {
            Log.e(TAG, "Failure to decrypt data, mount service not found");
        }
        return dekData;
    }

    public Map<String, byte[]> initAeKek() {
        IBinder binder = ServiceManager.getService(SERVICE_NAME);
        if (binder == null) {
            Slog.d(TAG, "get oplusstoragemanagerservice is null");
        } else if (this.mOplusStorageManagerService != IOplusStorageManagerService.Stub.asInterface(binder)) {
            this.mOplusStorageManagerService = IOplusStorageManagerService.Stub.asInterface(binder);
        }
        Map<String, byte[]> aeKekData = null;
        try {
            IOplusStorageManagerService iOplusStorageManagerService = this.mOplusStorageManagerService;
            if (iOplusStorageManagerService == null) {
                Slog.d(TAG, "oplusstoragemanagerservice initAeKek is null");
            } else {
                aeKekData = iOplusStorageManagerService.initAeKek();
            }
            Log.d(TAG, "InitAeKek");
        } catch (RemoteException e) {
            Log.e(TAG, "mount service not found");
        }
        if (aeKekData == null) {
            Log.e(TAG, "aeKekData is null, phone maybe locked");
            return null;
        }
        return aeKekData;
    }

    public Map<String, byte[]> initBeKek() {
        IBinder binder = ServiceManager.getService(SERVICE_NAME);
        if (binder == null) {
            Slog.d(TAG, "get oplusstoragemanagerservice is null");
        } else if (this.mOplusStorageManagerService != IOplusStorageManagerService.Stub.asInterface(binder)) {
            this.mOplusStorageManagerService = IOplusStorageManagerService.Stub.asInterface(binder);
        }
        Map<String, byte[]> beKekData = null;
        try {
            IOplusStorageManagerService iOplusStorageManagerService = this.mOplusStorageManagerService;
            if (iOplusStorageManagerService == null) {
                Slog.d(TAG, "oplusstoragemanagerservice initBeKek is null");
            } else {
                beKekData = iOplusStorageManagerService.initBeKek();
            }
            Log.d(TAG, "InitBeKek");
        } catch (RemoteException e) {
            Log.e(TAG, "mount service not found");
        }
        if (beKekData == null) {
            Log.e(TAG, "beKekData is null, phone maybe locked");
            return null;
        }
        return beKekData;
    }

    public int getStorageData() {
        String[] pkgs = null;
        int callingUid = Binder.getCallingUid();
        getOplusStorageManagerService();
        try {
            pkgs = AppGlobals.getPackageManager().getPackagesForUid(callingUid);
        } catch (RemoteException e) {
            Slog.e(TAG, "getPackagesForUid failed for uid:" + callingUid, e);
        }
        if (pkgs == null) {
            Slog.d(TAG, "pkgs is null");
            return -1;
        }
        if (!pkgs[0].equals("com.coloros.phonemanager") && !pkgs[0].equals(OplusWindowUtils.PACKAGE_ASSISTANTSCREEN)) {
            Slog.d(TAG, "not allowed " + pkgs[0] + " to get the interface");
        } else {
            try {
                IOplusStorageManagerService iOplusStorageManagerService = this.mOplusStorageManagerService;
                if (iOplusStorageManagerService == null) {
                    Slog.d(TAG, "oplusstoragemanagerservice is null");
                } else {
                    return iOplusStorageManagerService.getStorageData();
                }
            } catch (RemoteException e2) {
                Slog.e(TAG, "err = " + e2.toString());
                throw e2.rethrowFromSystemServer();
            }
        }
        return -1;
    }

    private String[] getPkgs() {
        this.mCallingUid = Binder.getCallingUid();
        getOplusStorageManagerService();
        try {
            String[] pkgs = AppGlobals.getPackageManager().getPackagesForUid(this.mCallingUid);
            return pkgs;
        } catch (RemoteException e) {
            Slog.e(TAG, "getPackagesForUid failed for uid:" + this.mCallingUid, e);
            return null;
        }
    }

    private void getOplusStorageManagerService() {
        try {
            if (this.mOplusStorageManagerService == null) {
                this.mOplusStorageManagerService = IOplusStorageManagerService.Stub.asInterface(ServiceManager.getService(SERVICE_NAME));
            }
        } catch (Exception e) {
            Slog.e(TAG, "get oplusstoragemanagerservice error");
        }
    }

    public int setSDLockPassword(String pw) {
        String[] pkgs = getPkgs();
        if (pkgs == null) {
            Slog.d(TAG, "pkgs is null");
            return -1;
        }
        try {
            if (this.mOplusStorageManagerService != null) {
                if (pkgs[0].equals("com.coloros.movetosdcard")) {
                    return this.mOplusStorageManagerService.setSDLockPassword(pw);
                }
                Slog.d(TAG, "pkgs Permission denied");
                return -1;
            }
            Slog.d(TAG, "setSDLockPassword oplusstoragemanagerservice is null");
            return -1;
        } catch (RemoteException e) {
            Slog.e(TAG, "err = " + e.toString());
            throw e.rethrowFromSystemServer();
        }
    }

    public int clearSDLockPassword() {
        String[] pkgs = getPkgs();
        if (pkgs == null) {
            Slog.d(TAG, "pkgs is null");
            return -1;
        }
        try {
            if (this.mOplusStorageManagerService != null) {
                if (!pkgs[0].equals("com.coloros.movetosdcard") && this.mCallingUid != 1000) {
                    Slog.d(TAG, "pkgs Permission denied");
                    return -1;
                }
                return this.mOplusStorageManagerService.clearSDLockPassword();
            }
            Slog.d(TAG, "clearSDLockPassword oplusstoragemanagerservice is null");
            return -1;
        } catch (RemoteException e) {
            Slog.e(TAG, "err = " + e.toString());
            throw e.rethrowFromSystemServer();
        }
    }

    public int unlockSDCard(String pw) {
        String[] pkgs = getPkgs();
        if (pkgs == null) {
            Slog.d(TAG, "pkgs is null");
            return -1;
        }
        try {
            if (this.mOplusStorageManagerService != null) {
                if (pkgs[0].equals("com.coloros.movetosdcard")) {
                    Slog.d(TAG, "clearSDLockPassword oplusstoragemanagerservice is null");
                    return this.mOplusStorageManagerService.unlockSDCard(pw);
                }
                Slog.d(TAG, "pkgs Permission denied");
                return -1;
            }
            Slog.d(TAG, "unlockSDCard oplusstoragemanagerservice is null");
            return -1;
        } catch (RemoteException e) {
            Slog.e(TAG, "err = " + e.toString());
            throw e.rethrowFromSystemServer();
        }
    }

    public long getUnlockSdcardDeadline() {
        String[] pkgs = getPkgs();
        if (pkgs == null) {
            Slog.d(TAG, "pkgs is null");
            return -1L;
        }
        try {
            if (this.mOplusStorageManagerService != null) {
                if (pkgs[0].equals("com.coloros.movetosdcard")) {
                    return this.mOplusStorageManagerService.getUnlockSdcardDeadline();
                }
                Slog.d(TAG, "pkgs Permission denied");
                return -1L;
            }
            Slog.d(TAG, "getUnlockSdcardDeadline oplusstoragemanagerservice is null");
            return -1L;
        } catch (RemoteException e) {
            Slog.e(TAG, "err = " + e.toString());
            throw e.rethrowFromSystemServer();
        }
    }

    public String getSDCardId() {
        String[] pkgs = getPkgs();
        if (pkgs == null) {
            Slog.d(TAG, "pkgs is null");
            return null;
        }
        try {
            if (this.mOplusStorageManagerService != null) {
                if (pkgs[0].equals("com.coloros.movetosdcard")) {
                    return this.mOplusStorageManagerService.getSDCardId();
                }
                Slog.d(TAG, "pkgs Permission denied");
                return null;
            }
            Slog.d(TAG, "getSDCardId oplusstoragemanagerservice is null");
            return null;
        } catch (RemoteException e) {
            Slog.e(TAG, "err = " + e.toString());
            throw e.rethrowFromSystemServer();
        }
    }

    public int getSDLockState() {
        String[] pkgs = getPkgs();
        if (pkgs == null) {
            Slog.d(TAG, "pkgs is null");
            return -1;
        }
        try {
            if (this.mOplusStorageManagerService != null) {
                if (!pkgs[0].equals("com.coloros.movetosdcard") && this.mCallingUid != 1000) {
                    Slog.d(TAG, "pkgs Permission denied");
                    return -1;
                }
                return this.mOplusStorageManagerService.getSDLockState();
            }
            Slog.d(TAG, "getSDLockState oplusstoragemanagerservice is null");
            return -1;
        } catch (RemoteException e) {
            Slog.e(TAG, "err = " + e.toString());
            throw e.rethrowFromSystemServer();
        }
    }

    public void eraseSDLock() {
        String[] pkgs = getPkgs();
        if (pkgs == null) {
            Slog.d(TAG, "pkgs is null");
            return;
        }
        try {
            if (this.mOplusStorageManagerService == null) {
                Slog.d(TAG, "getSDLockState oplusstoragemanagerservice is null");
            } else if (!pkgs[0].equals("com.coloros.movetosdcard")) {
                Slog.d(TAG, "pkgs Permission denied");
            } else {
                this.mOplusStorageManagerService.eraseSDLock();
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "err = " + e.toString());
            throw e.rethrowFromSystemServer();
        }
    }

    public void voldTBExt() {
        Slog.d(TAG, "voldTBExt start");
        getOplusStorageManagerService();
        try {
            IOplusStorageManagerService iOplusStorageManagerService = this.mOplusStorageManagerService;
            if (iOplusStorageManagerService != null) {
                iOplusStorageManagerService.voldTBExt();
            } else {
                Slog.d(TAG, "voldTBExt oplusstoragemanagerservice is null");
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "err = " + e.toString());
            throw e.rethrowFromSystemServer();
        }
    }

    public ParcelFileDescriptor mountDfsFuse(String path, String opts) throws RemoteException {
        Slog.d(TAG, "mountDfsFuse start");
        getOplusStorageManagerService();
        if (path != null && opts != null) {
            IOplusStorageManagerService iOplusStorageManagerService = this.mOplusStorageManagerService;
            if (iOplusStorageManagerService != null) {
                try {
                    return iOplusStorageManagerService.mountDfsFuse(path, opts);
                } catch (RemoteException e) {
                    Slog.e(TAG, "err = " + e.toString());
                    throw e.rethrowFromSystemServer();
                }
            }
            Slog.d(TAG, "mountDfsFuse oplusstoragemanagerservice is null");
            return null;
        }
        return null;
    }

    public int umountDfsFuse(String path) {
        Slog.d(TAG, "umountDfsFuse start");
        getOplusStorageManagerService();
        if (path != null) {
            try {
                IOplusStorageManagerService iOplusStorageManagerService = this.mOplusStorageManagerService;
                if (iOplusStorageManagerService != null) {
                    return iOplusStorageManagerService.umountDfsFuse(path);
                }
                Slog.d(TAG, "umountDfsFuse oplusstoragemanagerservice is null");
                return -1;
            } catch (RemoteException e) {
                Slog.e(TAG, "err = " + e.toString());
                throw e.rethrowFromSystemServer();
            }
        }
        return -1;
    }

    public int configDfsFuse(String path, int readAheadBlocks, int maxDirtyRatio) {
        Slog.d(TAG, "umountDfsFuse start");
        getOplusStorageManagerService();
        if (path != null && readAheadBlocks >= 0 && maxDirtyRatio >= 0) {
            try {
                IOplusStorageManagerService iOplusStorageManagerService = this.mOplusStorageManagerService;
                if (iOplusStorageManagerService != null) {
                    return iOplusStorageManagerService.configDfsFuse(path, readAheadBlocks, maxDirtyRatio);
                }
                Slog.d(TAG, "configDfsFuse oplusstoragemanagerservice is null");
                return -1;
            } catch (RemoteException e) {
                Slog.e(TAG, "err = " + e.toString());
                throw e.rethrowFromSystemServer();
            }
        }
        return -1;
    }
}
