package android.app;

import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.oplus.customize.coreapp.aidl.IOplusCoreAppService;
import java.util.List;

/* loaded from: classes.dex */
public class OplusEnterpriseAndOperatorFeature implements IOplusEnterpriseAndOperatorFeature {
    private static final String TAG = "OplusEnterpriseAndOperatorFeature";

    @Override // android.app.IOplusEnterpriseAndOperatorFeature
    public void addCustomMdmJarToPath(List<String> outPaths) {
        outPaths.add("/system_ext/framework/OplusMdmInterface.jar");
        outPaths.add("/system_ext/framework/OplusMdmAdapter.jar");
    }

    @Override // android.app.IOplusEnterpriseAndOperatorFeature
    public boolean isPackageContainsOplusCertificates(String packageName) throws RemoteException {
        IOplusCoreAppService service;
        IBinder mRemote = ServiceManager.getService("opluscoreappservice");
        if (mRemote == null || (service = IOplusCoreAppService.Stub.asInterface(mRemote)) == null) {
            return false;
        }
        return service.isPackageContainsOplusCertificates(packageName);
    }

    @Override // android.app.IOplusEnterpriseAndOperatorFeature
    public void addCustomMdmJarToPath(boolean isActivityThreadExist, ApplicationInfo aInfo, List<String> outZipPaths) {
        if (isActivityThreadExist) {
            try {
                IPackageManager iPackageManager = ActivityThread.getPackageManager();
                if (iPackageManager != null && isPackageContainsOplusCertificates(aInfo.packageName)) {
                    addCustomMdmJarToPath(outZipPaths);
                }
            } catch (Exception e) {
                Log.w(TAG, "addCustomMdmJarToPath errror");
            }
        }
    }
}
