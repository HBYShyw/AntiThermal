package android.os;

import android.os.IHwBinder;
import android.util.Log;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;
import vendor.oplus.hardware.handlefactory.V1_0.IHandleFactory;

/* loaded from: classes.dex */
public class OplusHandleFactory {
    private static final long HWSERVICE_COOKIE_HANDLEFACTORY = 0;
    private static final int O_RDONLY = 0;
    private static final int O_RDWR = 2;
    private static final String TAG = "HandleFactory";
    private static IHandleFactory sIHandleFactory = null;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class HwServiceDeathCallback implements IHwBinder.DeathRecipient {
        private HwServiceDeathCallback() {
        }

        public void serviceDied(long var1) {
            if (OplusHandleFactory.HWSERVICE_COOKIE_HANDLEFACTORY == var1) {
                OplusHandleFactory.sIHandleFactory = null;
                Log.w(OplusHandleFactory.TAG, "HwServiceDeathCallback sIHandleFactory die!");
            }
        }
    }

    private static IHandleFactory getService() {
        if (sIHandleFactory == null) {
            try {
                IHandleFactory service = IHandleFactory.getService();
                sIHandleFactory = service;
                if (service != null) {
                    service.linkToDeath(new HwServiceDeathCallback(), HWSERVICE_COOKIE_HANDLEFACTORY);
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to get IHandleFactory hal service, " + e.getMessage());
            }
        }
        return sIHandleFactory;
    }

    public static boolean exist() {
        boolean z;
        synchronized (OplusHandleFactory.class) {
            z = getService() != null;
        }
        return z;
    }

    /* loaded from: classes.dex */
    private static class IOCtrlCallback implements IHandleFactory.ioctrlCallback {
        private int mParam;
        private int mResult = -1;

        IOCtrlCallback() {
        }

        public void onValues(int result, int param) {
            this.mResult = result;
            this.mParam = param;
        }

        public int result() {
            return this.mResult;
        }

        public int param() {
            return this.mParam;
        }
    }

    public static int[] ioctrl(String key, int cmd, int param) {
        synchronized (OplusHandleFactory.class) {
            if (getService() == null) {
                return null;
            }
            try {
                IOCtrlCallback callback = new IOCtrlCallback();
                getService().ioctrl(key, 2, cmd, param, callback);
                List<Integer> retList = Arrays.asList(Integer.valueOf(callback.result()), Integer.valueOf(callback.param()));
                return retList.stream().mapToInt(new ToIntFunction() { // from class: android.os.OplusHandleFactory$$ExternalSyntheticLambda0
                    @Override // java.util.function.ToIntFunction
                    public final int applyAsInt(Object obj) {
                        int intValue;
                        intValue = ((Integer) obj).intValue();
                        return intValue;
                    }
                }).toArray();
            } catch (RemoteException e) {
                Log.w(TAG, "ioctrl failed! " + e.getMessage());
                return null;
            }
        }
    }

    public static int write(String key, String value) {
        synchronized (OplusHandleFactory.class) {
            if (getService() == null) {
                return -1;
            }
            try {
                return getService().write(key, 2, value);
            } catch (RemoteException e) {
                Log.w(TAG, "write failed! " + e.getMessage());
                return -1;
            }
        }
    }

    /* loaded from: classes.dex */
    private static class ReadCallback implements IHandleFactory.readCallback {
        private int mResult = -1;
        private String mData = "";

        ReadCallback() {
        }

        public void onValues(int result, String data) {
            this.mResult = result;
            this.mData = data;
        }

        public int result() {
            return this.mResult;
        }

        public String data() {
            return this.mData;
        }
    }

    public static String read(String key) {
        synchronized (OplusHandleFactory.class) {
            if (getService() == null) {
                return null;
            }
            try {
                ReadCallback cbReadCallback = new ReadCallback();
                getService().read(key, 0, cbReadCallback);
                if (cbReadCallback.result() < 0) {
                    return null;
                }
                return cbReadCallback.data();
            } catch (RemoteException e) {
                Log.w(TAG, "read failed! " + e.getMessage());
                return null;
            }
        }
    }

    public static int getHandle(String name, int flag) {
        synchronized (OplusHandleFactory.class) {
            if (getService() == null) {
                return -1;
            }
            try {
                NativeHandle nhandle = getService().getHandle(name, flag);
                NativeHandle cpHandle = nhandle.dup();
                if (cpHandle == null) {
                    return -1;
                }
                if (cpHandle.getFileDescriptors() != null && cpHandle.getFileDescriptors().length > 0) {
                    return cpHandle.getFileDescriptors()[0].getInt$();
                }
                cpHandle.close();
                return -1;
            } catch (RemoteException e) {
                Log.w(TAG, "getHandle failed! " + e.getMessage());
                return -1;
            } catch (IOException e2) {
                Log.w(TAG, "getHandle failed! " + e2.getMessage());
                return -1;
            }
        }
    }
}
