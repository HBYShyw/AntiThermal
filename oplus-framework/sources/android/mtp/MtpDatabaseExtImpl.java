package android.mtp;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.OplusPropertyList;
import android.os.Process;
import android.os.SystemProperties;
import android.provider.MediaStore;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/* loaded from: classes.dex */
public class MtpDatabaseExtImpl implements IMtpDatabaseExt {
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final int EXIT_MESSAGE = 2;
    public static final int HANDLE_MESSAGE = 1;
    public static final int INIT_MESSAGE = 0;
    private static final String NO_MEDIA = ".nomedia";
    private static final String TAG = "MtpDatabaseExtImpl";
    private static MtpDatabaseExtImpl sInstance;
    protected ScanHandler mHandler;
    protected HandlerThread mHandlerThread;
    private final Context sContext;

    public MtpDatabaseExtImpl(Context context) {
        this.sContext = (Context) Objects.requireNonNull(context);
        initHandlerThread();
    }

    private void initHandlerThread() {
        HandlerThread handlerThread = new HandlerThread(TAG, 10);
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mHandler = new ScanHandler(this.mHandlerThread.getLooper(), this.sContext);
    }

    public static MtpDatabaseExtImpl getInstance(Object obj) {
        if (sInstance == null) {
            sInstance = new MtpDatabaseExtImpl((Context) obj);
        }
        return sInstance;
    }

    /* loaded from: classes.dex */
    public class HandlerParams {
        String path;

        HandlerParams(String path) {
            this.path = path;
        }
    }

    public void rescanFile(String path, int handle, int format) {
        try {
            boolean msg = sendMessage(path, handle, format);
            if (msg) {
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, "rescanFile failed : " + e.getMessage());
        }
        Log.e(TAG, "rescanFile retry");
        try {
            initHandlerThread();
            sendMessage(path, handle, format);
        } catch (Exception e2) {
            Log.e(TAG, "rescanFile retry failed : " + e2.getMessage());
            e2.printStackTrace();
        }
    }

    private boolean sendMessage(String path, int handle, int format) throws Exception {
        Message msg = this.mHandler.obtainMessage(0);
        msg.obj = new HandlerParams(path);
        return this.mHandler.sendMessage(msg);
    }

    /* loaded from: classes.dex */
    public class ScanHandler extends Handler {
        private ArrayList<HandlerParams> mPendingInstalls;
        private final Context scanContext;

        ScanHandler(Looper looper, Context context) {
            super(looper);
            this.mPendingInstalls = new ArrayList<>();
            this.scanContext = context;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            try {
                doHandleMessage(msg);
            } finally {
                Process.setThreadPriority(10);
            }
        }

        private void doHandleMessage(Message msg) {
            HandlerParams params;
            switch (msg.what) {
                case 0:
                    HandlerParams param = (HandlerParams) msg.obj;
                    int idx = this.mPendingInstalls.size();
                    this.mPendingInstalls.add(idx, param);
                    if (idx == 0) {
                        MtpDatabaseExtImpl.this.mHandler.sendEmptyMessage(1);
                        return;
                    }
                    return;
                case 1:
                    if (this.mPendingInstalls.size() > 0 && (params = this.mPendingInstalls.get(0)) != null) {
                        MtpDatabaseExtImpl.updateMediaStore(this.scanContext, new File(params.path));
                        if (this.mPendingInstalls.size() > 0) {
                            this.mPendingInstalls.remove(0);
                        }
                        if (this.mPendingInstalls.size() != 0) {
                            MtpDatabaseExtImpl.this.mHandler.sendEmptyMessage(1);
                            return;
                        }
                        return;
                    }
                    return;
                case 2:
                    if (this.mPendingInstalls.size() == 0) {
                        if (MtpDatabaseExtImpl.this.mHandlerThread != null) {
                            boolean quitsafe = MtpDatabaseExtImpl.this.mHandlerThread.quitSafely();
                            if (MtpDatabaseExtImpl.DEBUG) {
                                Log.d(MtpDatabaseExtImpl.TAG, "mHandlerThread.quitSafely ?" + quitsafe);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    if (MtpDatabaseExtImpl.DEBUG) {
                        Log.d(MtpDatabaseExtImpl.TAG, "sendEmptyMessageDelayed EXIT_MESSAGE");
                    }
                    MtpDatabaseExtImpl.this.mHandler.sendEmptyMessageDelayed(2, 500L);
                    return;
                default:
                    return;
            }
        }
    }

    public void releaseScanThread() {
        this.mHandler.sendEmptyMessage(2);
    }

    public String getOplusMarketName(int length, int property) {
        Log.i(TAG, "getDeviceProperty  length = " + length);
        if (property != 54274) {
            return null;
        }
        String deviceName = SystemProperties.get(OplusPropertyList.PROPERTY_OPLUS_VENDOR_MARKET_NAME, "OPLUS MTP Device");
        int lengthDeviceName = deviceName.length();
        if (lengthDeviceName > 255) {
            lengthDeviceName = 255;
        }
        if (lengthDeviceName > 0) {
            Log.d(TAG, "getDeviceProperty  deviceName = " + deviceName + ", lengthDeviceName = " + lengthDeviceName);
            return deviceName;
        }
        Log.d(TAG, "getDeviceProperty  lengthDeviceName = " + lengthDeviceName);
        return null;
    }

    public void quitSafely() {
        HandlerThread handlerThread = this.mHandlerThread;
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }
    }

    public boolean getMtpDeviceProperty(int length, int property, char[] outStringValue) {
        if (length > 0) {
            return true;
        }
        if (getOplusMarketName(length, property) != null) {
            String deviceName = getOplusMarketName(length, property);
            int lengthDeviceName = deviceName.length();
            deviceName.getChars(0, lengthDeviceName, outStringValue, 0);
            outStringValue[lengthDeviceName] = 0;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateMediaStore(Context context, File file) {
        ContentResolver resolver = context.getContentResolver();
        if (!file.isDirectory() && file.getName().toLowerCase(Locale.ROOT).endsWith(NO_MEDIA)) {
            MediaStore.scanFile(resolver, file.getParentFile());
        } else {
            MediaStore.scanFile(resolver, file);
        }
    }
}
