package android.os.oplusdevicepolicy;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService;
import android.os.oplusdevicepolicy.IOplusDevicePolicyObserver;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import java.util.List;

/* loaded from: classes.dex */
public class OplusDevicepolicyManager {
    public static final int CUSTOMIZE_DATA_TYPE = 1;
    public static final String SERVICE_NAME = "oplusdevicepolicy";
    public static final int SYSTEM_DATA_TYPE = 0;
    private static final String TAG = "OplusDevicePolicyManager";
    private final ArrayMap<OplusDevicePolicyObserver, IOplusDevicePolicyObserver> mOplusDevicePolicyObservers = new ArrayMap<>();
    private IOplusDevicePolicyManagerService mOplusDevicepolicyManagerService;
    private static final Object mServiceLock = new Object();
    private static final Object mLock = new Object();
    private static volatile OplusDevicepolicyManager sInstance = null;

    /* loaded from: classes.dex */
    public interface OplusDevicePolicyObserver {
        void onOplusDevicePolicyUpdate(String str, String str2);

        void onOplusDevicePolicyUpdate(String str, List<String> list);
    }

    private OplusDevicepolicyManager() {
        getOplusDevicepolicyManagerService();
    }

    public static final OplusDevicepolicyManager getInstance() {
        OplusDevicepolicyManager oplusDevicepolicyManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusDevicepolicyManager();
                }
                oplusDevicepolicyManager = sInstance;
            }
            return oplusDevicepolicyManager;
        }
        return sInstance;
    }

    private IOplusDevicePolicyManagerService getOplusDevicepolicyManagerService() {
        IOplusDevicePolicyManagerService iOplusDevicePolicyManagerService;
        synchronized (mServiceLock) {
            if (this.mOplusDevicepolicyManagerService == null) {
                this.mOplusDevicepolicyManagerService = IOplusDevicePolicyManagerService.Stub.asInterface(ServiceManager.getService(SERVICE_NAME));
            }
            iOplusDevicePolicyManagerService = this.mOplusDevicepolicyManagerService;
        }
        return iOplusDevicePolicyManagerService;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean setData(String str, String str2, int i) {
        String str3 = TAG;
        boolean z = false;
        try {
            getOplusDevicepolicyManagerService();
            IOplusDevicePolicyManagerService iOplusDevicePolicyManagerService = this.mOplusDevicepolicyManagerService;
            if (iOplusDevicePolicyManagerService == null) {
                Slog.d(TAG, "mOplusDevicepolicyManagerService is null");
                str3 = str3;
            } else {
                boolean data = iOplusDevicePolicyManagerService.setData(str, str2, i);
                z = data;
                str3 = data;
            }
        } catch (RemoteException e) {
            Slog.e(str3, "setData fail!", e);
        } catch (Exception e2) {
            Slog.e(str3, "setData Error" + e2);
        }
        return z;
    }

    public String getData(String name, int datatype) {
        String str = TAG;
        String ret = null;
        try {
            getOplusDevicepolicyManagerService();
            if (this.mOplusDevicepolicyManagerService == null) {
                Slog.d(TAG, "mOplusDevicepolicyManagerService is null");
            } else {
                str = getOplusDevicepolicyManagerService().getData(name, datatype);
                ret = str;
            }
        } catch (RemoteException e) {
            Slog.e(str, "getData fail!", e);
        } catch (Exception e2) {
            Slog.e(str, "getData Error" + e2);
        }
        return ret;
    }

    public boolean getBoolean(String name, int datatype, boolean defaultvalue) {
        String mData = getData(name, datatype);
        if (mData == null) {
            return defaultvalue;
        }
        return Boolean.valueOf(mData).booleanValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean setList(String str, List list, int i) {
        String str2 = TAG;
        boolean z = false;
        try {
            getOplusDevicepolicyManagerService();
            if (this.mOplusDevicepolicyManagerService == null) {
                Slog.d(TAG, "mOplusDevicepolicyManagerService is null");
                str2 = str2;
            } else {
                boolean list2 = getOplusDevicepolicyManagerService().setList(str, list, i);
                z = list2;
                str2 = list2;
            }
        } catch (RemoteException e) {
            Slog.e(str2, "setList fail!", e);
        } catch (Exception e2) {
            Slog.e(str2, "setList Error" + e2);
        }
        return z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean addList(String str, List list, int i) {
        String str2 = TAG;
        boolean z = false;
        try {
            getOplusDevicepolicyManagerService();
            if (this.mOplusDevicepolicyManagerService == null) {
                Slog.d(TAG, "mOplusDevicepolicyManagerService is null");
                str2 = str2;
            } else {
                boolean addList = getOplusDevicepolicyManagerService().addList(str, list, i);
                z = addList;
                str2 = addList;
            }
        } catch (RemoteException e) {
            Slog.e(str2, "addList fail!", e);
        } catch (Exception e2) {
            Slog.e(str2, "addList Error" + e2);
        }
        return z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public List<String> getList(String str, int i) {
        String str2 = TAG;
        List<String> list = null;
        try {
            getOplusDevicepolicyManagerService();
            if (this.mOplusDevicepolicyManagerService == null) {
                Slog.d(TAG, "mOplusDevicepolicyManagerService is null");
                str2 = str2;
            } else {
                List<String> list2 = getOplusDevicepolicyManagerService().getList(str, i);
                list = list2;
                str2 = list2;
            }
        } catch (RemoteException e) {
            Slog.e(str2, "getList fail!", e);
        } catch (Exception e2) {
            Slog.e(str2, "getList Error" + e2);
        }
        return list;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean removeData(String str, int i) {
        String str2 = TAG;
        boolean z = false;
        try {
            getOplusDevicepolicyManagerService();
            if (this.mOplusDevicepolicyManagerService == null) {
                Slog.d(TAG, "mOplusDevicepolicyManagerService is null");
                str2 = str2;
            } else {
                boolean removeData = getOplusDevicepolicyManagerService().removeData(str, i);
                z = removeData;
                str2 = removeData;
            }
        } catch (RemoteException e) {
            Slog.e(str2, "removeData fail!", e);
        } catch (Exception e2) {
            Slog.e(str2, "removeData Error" + e2);
        }
        return z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean removeList(String str, int i) {
        String str2 = TAG;
        boolean z = false;
        try {
            getOplusDevicepolicyManagerService();
            if (this.mOplusDevicepolicyManagerService == null) {
                Slog.d(TAG, "mOplusDevicepolicyManagerService is null");
                str2 = str2;
            } else {
                boolean removeList = getOplusDevicepolicyManagerService().removeList(str, i);
                z = removeList;
                str2 = removeList;
            }
        } catch (RemoteException e) {
            Slog.e(str2, "removeList fail!", e);
        } catch (Exception e2) {
            Slog.e(str2, "removeList Error" + e2);
        }
        return z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean removePartListData(String str, List list, int i) {
        String str2 = TAG;
        boolean z = false;
        try {
            getOplusDevicepolicyManagerService();
            if (this.mOplusDevicepolicyManagerService == null) {
                Slog.d(TAG, "mOplusDevicepolicyManagerService is null");
                str2 = str2;
            } else {
                boolean removePartListData = getOplusDevicepolicyManagerService().removePartListData(str, list, i);
                z = removePartListData;
                str2 = removePartListData;
            }
        } catch (RemoteException e) {
            Slog.e(str2, "removePartListData fail!", e);
        } catch (Exception e2) {
            Slog.e(str2, "removePartListData Error" + e2);
        }
        return z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean clearData(int i) {
        String str = TAG;
        boolean z = false;
        try {
            getOplusDevicepolicyManagerService();
            if (this.mOplusDevicepolicyManagerService == null) {
                Slog.d(TAG, "mOplusDevicepolicyManagerService is null");
                str = str;
            } else {
                boolean clearData = getOplusDevicepolicyManagerService().clearData(i);
                z = clearData;
                str = clearData;
            }
        } catch (RemoteException e) {
            Slog.e(str, "clearData fail!", e);
        } catch (Exception e2) {
            Slog.e(str, "clearData Error" + e2);
        }
        return z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean clearList(int i) {
        String str = TAG;
        boolean z = false;
        try {
            getOplusDevicepolicyManagerService();
            if (this.mOplusDevicepolicyManagerService == null) {
                Slog.d(TAG, "mOplusDevicepolicyManagerService is null");
                str = str;
            } else {
                boolean clearList = getOplusDevicepolicyManagerService().clearList(i);
                z = clearList;
                str = clearList;
            }
        } catch (RemoteException e) {
            Slog.e(str, "clearList fail!", e);
        } catch (Exception e2) {
            Slog.e(str, "clearList Error" + e2);
        }
        return z;
    }

    public boolean registerOplusDevicepolicyObserver(String name, OplusDevicePolicyObserver observer) {
        boolean ret = false;
        if (observer == null) {
            Log.e(TAG, " registerOplusDevicePolicyObserver null observer");
            return false;
        }
        synchronized (this.mOplusDevicePolicyObservers) {
            if (this.mOplusDevicePolicyObservers.get(observer) != null) {
                Log.e(TAG, "already regiter before");
                return false;
            }
            OplusDevicePolicyObserverDelegate delegate = new OplusDevicePolicyObserverDelegate(observer);
            try {
                getOplusDevicepolicyManagerService();
                if (this.mOplusDevicepolicyManagerService != null) {
                    ret = getOplusDevicepolicyManagerService().registerOplusDevicePolicyObserver(name, delegate);
                } else {
                    Slog.d(TAG, "mOplusDevicepolicyManagerService is null");
                }
                if (ret) {
                    this.mOplusDevicePolicyObservers.put(observer, delegate);
                }
            } catch (RemoteException e) {
                Slog.e(TAG, "registerOplusDevicePolicyObserver fail!", e);
            } catch (Exception e2) {
                Slog.e(TAG, "registerOplusDevicePolicyObserver Error" + e2);
            }
            return ret;
        }
    }

    public boolean unregisterOplusDevicePolicyObserver(OplusDevicePolicyObserver observer) {
        boolean ret = false;
        if (observer == null) {
            Log.i(TAG, "unregisterOplusDevicepolicyObserver null observer");
            return false;
        }
        synchronized (this.mOplusDevicePolicyObservers) {
            IOplusDevicePolicyObserver delegate = this.mOplusDevicePolicyObservers.get(observer);
            if (delegate != null) {
                try {
                    try {
                        getOplusDevicepolicyManagerService();
                        if (this.mOplusDevicepolicyManagerService != null) {
                            ret = getOplusDevicepolicyManagerService().unregisterOplusDevicePolicyObserver(delegate);
                        } else {
                            Slog.d(TAG, "mOplusDevicepolicyManagerService is null");
                        }
                        if (ret) {
                            this.mOplusDevicePolicyObservers.remove(observer);
                        }
                    } catch (RemoteException e) {
                        Slog.e(TAG, "unregisterOplusDevicePolicyObserver fail!", e);
                    }
                } catch (Exception e2) {
                    Slog.e(TAG, "unregisterOplusDevicePolicyObserver Error" + e2);
                }
            }
        }
        return ret;
    }

    /* loaded from: classes.dex */
    private class OplusDevicePolicyObserverDelegate extends IOplusDevicePolicyObserver.Stub {
        private final OplusDevicePolicyObserver mObserver;

        public OplusDevicePolicyObserverDelegate(OplusDevicePolicyObserver observer) {
            this.mObserver = observer;
        }

        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyObserver
        public void onOplusDevicePolicyListUpdate(String name, List<String> list) {
            this.mObserver.onOplusDevicePolicyUpdate(name, list);
        }

        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyObserver
        public void onOplusDevicePolicyValueUpdate(String name, String value) {
            this.mObserver.onOplusDevicePolicyUpdate(name, value);
        }
    }
}
