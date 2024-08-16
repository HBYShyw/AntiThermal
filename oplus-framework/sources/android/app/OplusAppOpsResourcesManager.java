package android.app;

import android.app.IOplusAppOpsResourcesManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Singleton;
import android.util.Slog;
import com.android.internal.app.IAppOpsService;
import java.util.List;

/* loaded from: classes.dex */
public class OplusAppOpsResourcesManager {
    private static final Singleton<IOplusAppOpsResourcesManager> IOplusAppOpsResourcesManagerSingleton = new Singleton<IOplusAppOpsResourcesManager>() { // from class: android.app.OplusAppOpsResourcesManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public IOplusAppOpsResourcesManager m12create() {
            try {
                IAppOpsService aos = IAppOpsService.Stub.asInterface(ServiceManager.getService("appops"));
                IBinder b = aos.asBinder().getExtension();
                Slog.d(OplusAppOpsResourcesManager.TAG, "get aos extension: " + b);
                return IOplusAppOpsResourcesManager.Stub.asInterface(b);
            } catch (Exception e) {
                Slog.e(OplusAppOpsResourcesManager.TAG, "create OplusAppOpsManagerServiceEnhance singleton failed: " + e.getMessage());
                return null;
            }
        }
    };
    private static final String TAG = "OplusAppOpsResourcesManager";

    private static IOplusAppOpsResourcesManager getService() {
        return (IOplusAppOpsResourcesManager) IOplusAppOpsResourcesManagerSingleton.get();
    }

    public List<String> readCustomizedAppOps(int opcode) throws RemoteException {
        if (getService() != null) {
            return getService().readCustomizedAppOps(opcode);
        }
        Slog.w(TAG, "readCustomizedAppOps failed because service has not been created");
        return null;
    }
}
