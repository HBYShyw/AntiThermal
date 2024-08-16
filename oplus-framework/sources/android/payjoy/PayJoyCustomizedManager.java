package android.payjoy;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.payjoy.IPayJoyCustomizedService;

/* loaded from: classes.dex */
public class PayJoyCustomizedManager {
    private static volatile PayJoyCustomizedManager mPayJoyCustomizedManager;
    private IPayJoyCustomizedService mService;

    public static PayJoyCustomizedManager getInstance() {
        IPayJoyCustomizedService pjCustomService = IPayJoyCustomizedService.Stub.asInterface(ServiceManager.getService("payjoy_customized_services"));
        if (pjCustomService == null) {
            return null;
        }
        if (mPayJoyCustomizedManager == null) {
            synchronized (PayJoyCustomizedManager.class) {
                if (mPayJoyCustomizedManager == null) {
                    mPayJoyCustomizedManager = new PayJoyCustomizedManager(pjCustomService);
                }
            }
        }
        return mPayJoyCustomizedManager;
    }

    public PayJoyCustomizedManager(IPayJoyCustomizedService service) {
        this.mService = service;
    }

    public void activatePayJoyControl() {
        try {
            this.mService.activatePayJoyControl();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isSupportPayJoy() {
        try {
            return this.mService.isSupportPayJoy();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isPayJoyCustState() {
        try {
            return this.mService.isPayJoyCustState();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
