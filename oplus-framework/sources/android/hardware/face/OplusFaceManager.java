package android.hardware.face;

import android.content.Context;
import android.hardware.biometrics.CryptoObject;
import android.hardware.face.FaceAuthenticateOptions;
import android.hardware.face.FaceManager;
import android.hardware.face.IFaceCommandCallback;
import android.hardware.face.IOplusFaceManager;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import android.util.Singleton;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.wrapper.hardware.face.Face;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusFaceManager {
    private static final int FACE_TYPE_SHIFT = 28;
    private static final boolean FEATURE_BIOMETRICS_PALMPRINT = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_BIOMETRICS_PALMPRINT);
    private static final boolean HASFEATUREPALMPRINT_UNIFY = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_BIOMETRICS_PALMPRINT_UNIFY);
    private static final Singleton<IOplusFaceManager> IOplusFaceManagerSingleton = new Singleton<IOplusFaceManager>() { // from class: android.hardware.face.OplusFaceManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public IOplusFaceManager m104create() {
            try {
                IBinder b = ServiceManager.getService("face");
                IOplusFaceManager oplusFaceManager = IOplusFaceManager.Stub.asInterface(b.getExtension());
                return oplusFaceManager;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    };
    public static final int PALMS_SENSOR_ID = 4;
    private static final int PALMS_SID = 255;
    public static final String TAG = "OplusFaceManager";
    public static final int TYPE_FACE = 0;
    public static final int TYPE_PALM = 1;
    private OplusAuthenticationCallback mClientCallback;
    private Context mContext;
    FaceManager.AuthenticationCallback mFaceAuthenticationCallback = new FaceManager.AuthenticationCallback() { // from class: android.hardware.face.OplusFaceManager.4
        public void onAuthenticationFailed() {
            OplusFaceManager.this.mClientCallback.onAuthenticationFailed();
        }

        public void onAuthenticationSucceeded(FaceManager.AuthenticationResult result) {
            OplusFaceManager.this.mClientCallback.onAuthenticationSucceeded();
        }

        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            OplusFaceManager.this.mClientCallback.onAuthenticationHelp(helpMsgId, helpString);
        }

        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            OplusFaceManager.this.mClientCallback.onAuthenticationError(errMsgId, errString);
        }

        public void onAuthenticationAcquired(int acquireInfo) {
            OplusFaceManager.this.mClientCallback.onAuthenticationAcquired(acquireInfo);
        }
    };
    private FaceManager mFaceManager;

    /* loaded from: classes.dex */
    public interface FaceCommandCallback {
        void onFaceCmd(int i, byte[] bArr);
    }

    public static IOplusFaceManager getService() {
        return (IOplusFaceManager) IOplusFaceManagerSingleton.get();
    }

    public OplusFaceManager(Context context) {
        this.mContext = context;
        this.mFaceManager = (FaceManager) context.getSystemService(FaceManager.class);
    }

    public int regsiterFaceCmdCallback(final FaceCommandCallback callback) {
        IFaceCommandCallback mIFaceCommandCallback = new IFaceCommandCallback.Stub() { // from class: android.hardware.face.OplusFaceManager.2
            public void onFaceCmd(int cmdId, byte[] result) {
                callback.onFaceCmd(cmdId, result);
            }
        };
        try {
            int res = getService().regsiterFaceCmdCallback(mIFaceCommandCallback);
            return res;
        } catch (RemoteException e) {
            Log.e(TAG, "regsiterFaceCmdCallback : " + e.toString());
            return -1;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return -1;
        }
    }

    public int unregsiterFaceCmdCallback(final FaceCommandCallback callback) {
        IFaceCommandCallback mIFaceCommandCallback = new IFaceCommandCallback.Stub() { // from class: android.hardware.face.OplusFaceManager.3
            public void onFaceCmd(int cmdId, byte[] result) {
                callback.onFaceCmd(cmdId, result);
            }
        };
        try {
            int res = getService().unregsiterFaceCmdCallback(mIFaceCommandCallback);
            return res;
        } catch (RemoteException e) {
            Log.e(TAG, "unregsiterFaceCmdCallback : " + e.toString());
            return -1;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return -1;
        }
    }

    public long getLockoutAttemptDeadline(int userId) {
        try {
            long result = getService().getLockoutAttemptDeadline(userId);
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "getLockoutAttemptDeadline : " + e.toString());
            return -1L;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return -1L;
        }
    }

    public int getFailedAttempts() {
        try {
            int result = getService().getFailedAttempts();
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "getFailedAttempts : " + e.toString());
            return -1;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return -1;
        }
    }

    public int sendFaceCmd(int sensorId, int cmdId, byte[] inbuf) {
        try {
            int res = getService().sendFaceCmd(sensorId, cmdId, inbuf);
            return res;
        } catch (RemoteException e) {
            Log.e(TAG, "Remote exception in sendFaceCmd(): " + e.toString());
            return -1;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return -1;
        }
    }

    public List<Face> getEnrolledPalms(int sensorId) {
        try {
            return getService().getEnrolledPalms(sensorId, UserHandle.myUserId(), this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<Face> getEnrolledPalmsUnify(int userId) {
        try {
            List<Face> faces = getService().getEnrolledPalms(4, userId, this.mContext.getOpPackageName());
            if (faces == null) {
                return null;
            }
            List<Face> oplusFaceList = new ArrayList<>(faces.size());
            for (Face face : faces) {
                if (face != null) {
                    oplusFaceList.add(new Face(face));
                }
            }
            return oplusFaceList;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static int distinguishFacesAndPalm(Face face) {
        int fid = face.getBiometricId();
        if (FEATURE_BIOMETRICS_PALMPRINT) {
            return HASFEATUREPALMPRINT_UNIFY ? (fid >> 28) == 0 ? 0 : 1 : fid < 255 ? 0 : 1;
        }
        return 0;
    }

    public boolean hasEnrolledPalms() {
        try {
            return getService().hasEnrolledPalms(4, UserHandle.myUserId(), this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void resetFaceDaemon() {
        try {
            getService().resetFaceDaemon();
        } catch (RemoteException e) {
            Log.e(TAG, "resetFaceDaemon : " + e.toString());
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
        }
    }

    public int getFaceProcessMemory() {
        try {
            int result = getService().getFaceProcessMemory();
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "getFaceProcessMemory : " + e.toString());
            return -1;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return -1;
        }
    }

    public void authenticateAON(CryptoObject crypto, CancellationSignal cancel, int flags, OplusAuthenticationCallback callback, int userId, byte[] nv21ImageData, Handler handler) {
        this.mClientCallback = callback;
        this.mFaceManager.authenticate(crypto, cancel, this.mFaceAuthenticationCallback, handler, new FaceAuthenticateOptions.Builder().setUserId(userId).build());
    }

    /* loaded from: classes.dex */
    public abstract class OplusAuthenticationCallback {
        public OplusAuthenticationCallback() {
        }

        public void onAuthenticationError(int errorCode, CharSequence errString) {
        }

        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        }

        public void onAuthenticationSucceeded() {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationAcquired(int acquireInfo) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAONAuthentication(CryptoObject cryptoObject) {
        Log.d(TAG, "OplusFaceManager#cancelAONAuthentication");
    }

    /* loaded from: classes.dex */
    protected class OnAONAuthenticationCancelListener implements CancellationSignal.OnCancelListener {
        private CryptoObject mCrypto;

        OnAONAuthenticationCancelListener(CryptoObject crypto) {
            this.mCrypto = crypto;
        }

        @Override // android.os.CancellationSignal.OnCancelListener
        public void onCancel() {
            OplusFaceManager.this.cancelAONAuthentication(this.mCrypto);
        }
    }
}
