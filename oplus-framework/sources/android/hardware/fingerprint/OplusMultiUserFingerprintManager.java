package android.hardware.fingerprint;

import android.hardware.fingerprint.IOplusFingerprintManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Singleton;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusMultiUserFingerprintManager {
    public static final int FLAG_FINGERPRINT_SECOND_SYSTEM = 268435456;
    private static final Singleton<IOplusFingerprintManager> IOplusFingerprintManagerSingleton = new Singleton<IOplusFingerprintManager>() { // from class: android.hardware.fingerprint.OplusMultiUserFingerprintManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public IOplusFingerprintManager m112create() {
            try {
                IBinder b = ServiceManager.getService("fingerprint");
                IOplusFingerprintManager oplusFingerprintManager = IOplusFingerprintManager.Stub.asInterface(b.getExtension());
                return oplusFingerprintManager;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    };
    private static final Singleton<OplusMultiUserFingerprintManager> sOplusMultiUserSingleton = new Singleton<OplusMultiUserFingerprintManager>() { // from class: android.hardware.fingerprint.OplusMultiUserFingerprintManager.2
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public OplusMultiUserFingerprintManager m113create() {
            return new OplusMultiUserFingerprintManager();
        }
    };

    public static IOplusFingerprintManager getService() {
        return (IOplusFingerprintManager) IOplusFingerprintManagerSingleton.get();
    }

    public static OplusMultiUserFingerprintManager getInstance() {
        return (OplusMultiUserFingerprintManager) sOplusMultiUserSingleton.get();
    }

    public List<OplusFingerprint> getEnrolledFingerprints(int userId, int sensorId) throws RemoteException {
        new ArrayList();
        List<OplusFingerprint> list = getService().getEnrolledFingerprints(userId, sensorId);
        return list;
    }

    public int setFingerprintFlags(int fingerId, int groupId, int flags, int userId, int sensorId) throws RemoteException {
        int ret = getService().setFingerprintFlags(fingerId, groupId, flags, userId, sensorId);
        return ret;
    }
}
