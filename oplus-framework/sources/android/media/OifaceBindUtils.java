package android.media;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;

/* loaded from: classes.dex */
public class OifaceBindUtils {
    public static final int BIND_TASK = 2;
    private static final String OIFACE_DESCRIPTOR = "com.oplus.oiface.IOIfaceService";
    private static final String TAG = "OifaceBindUtils";
    private static final int TRANSACTION_BIND_TASK = 7;
    public static final int UNBIND_TASK = 0;
    private static OifaceBindUtils sInstance;
    private static boolean sOifaceProp = SystemProperties.getBoolean("persist.sys.oiface.enable", true);
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: android.media.OifaceBindUtils.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.d(OifaceBindUtils.TAG, "OifaceProxyUtils binderDied");
            OifaceBindUtils.this.mRemote = null;
        }
    };
    private IBinder mRemote;

    public static synchronized OifaceBindUtils getInstance() {
        OifaceBindUtils oifaceBindUtils;
        synchronized (OifaceBindUtils.class) {
            if (sInstance == null) {
                sInstance = new OifaceBindUtils();
            }
            oifaceBindUtils = sInstance;
        }
        return oifaceBindUtils;
    }

    private OifaceBindUtils() {
        connectOifaceService();
    }

    private IBinder connectOifaceService() {
        this.mRemote = ServiceManager.checkService("oplusoiface");
        Log.d(TAG, "connectOifaceService mRemote= " + this.mRemote);
        IBinder iBinder = this.mRemote;
        if (iBinder != null) {
            try {
                iBinder.linkToDeath(this.mDeathRecipient, 0);
            } catch (RemoteException e) {
                this.mRemote = null;
            }
        }
        return this.mRemote;
    }

    public void bindTaskWithOiface(int type) {
        bindTaskWithOiface(type, Process.myTid());
    }

    public void bindTaskWithOiface(int type, int threadId) {
        Log.d(TAG, "bindTaskWithOiface mRemote= " + this.mRemote + ",type =" + type + ",threadId =" + threadId);
        if (this.mRemote == null && connectOifaceService() == null) {
            this.mRemote = null;
            return;
        }
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            try {
                _data.writeInterfaceToken(OIFACE_DESCRIPTOR);
                _data.writeInt(type);
                _data.writeInt(threadId);
                this.mRemote.transact(7, _data, _reply, 1);
                Log.d(TAG, "bindTaskWithOiface transact");
            } catch (Exception e) {
                Log.e(TAG, "bindTaskWithOiface = " + e.getMessage());
            }
        } finally {
            _data.recycle();
            _reply.recycle();
        }
    }

    public static void bindTask() {
        try {
            Log.d(TAG, "bindTask");
            getInstance().bindTaskWithOiface(2);
        } catch (Throwable t) {
            Log.w(TAG, "bindTask, t=" + t);
        }
    }
}
