package android.hardware.fingerprint;

import android.R;
import android.app.ActivityManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintAuthenticateOptions;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.IFingerprintCommandCallback;
import android.hardware.fingerprint.IFingerprintService;
import android.hardware.fingerprint.IOplusFingerprintManager;
import android.hardware.fingerprint.IOplusFingerprintServiceReceiver;
import android.hardware.fingerprint.util.OplusFingerprintSupportUtils;
import android.os.Binder;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import android.util.Singleton;

/* loaded from: classes.dex */
public class OplusFingerprintManager implements OplusBiometricFingerprintConstantsEx {
    private static final String BIOMETRICS_FINGERPRINTMANAGER_CLASS = "android.hardware.fingerprint.FingerprintManager";
    private static final String FIELD_AUTHENTICATE_CALLBACK = "mAuthenticationCallback";
    private static final String FIELD_SERVICE_RECEIVER = "mServiceReceiver";
    private static final Singleton<IOplusFingerprintManager> IOplusFingerprintManagerSingleton = new Singleton<IOplusFingerprintManager>() { // from class: android.hardware.fingerprint.OplusFingerprintManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public IOplusFingerprintManager m111create() {
            try {
                return IOplusFingerprintManager.Stub.asInterface(ServiceManager.getService("fingerprint").getExtension());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    };
    private static final String MOTHED_USE_HANDLER = "useHandler";
    private static final int MSG_ERROR = 104;
    private static final String TAG = "Biometrics/Fingerprint21/OplusFingerprintManager";
    private final Context mContext;
    private FingerprintExtraInfoCallback mFingerprintExtraInfoCallback;
    private FingerprintInputCallback mFingerprintInputCallback;
    private MyHandler mHandler;
    private IBinder mRemote;
    private IFingerprintService mService;
    private final IBinder mToken = new Binder();
    private IOplusFingerprintServiceReceiver mServiceReceiver = new IOplusFingerprintServiceReceiver.Stub() { // from class: android.hardware.fingerprint.OplusFingerprintManager.4
        @Override // android.hardware.fingerprint.IOplusFingerprintServiceReceiver
        public void onFingerprintEngineeringInfoUpdated(EngineeringInfo info) {
            Log.d(OplusFingerprintManager.TAG, "onFingerprintEngineeringInfoUpdated： " + info);
            OplusFingerprintManager.this.mHandler.obtainMessage(1005, 0, 0, info).sendToTarget();
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintServiceReceiver
        public void onError(int error, int vendorCode) {
            Log.d(OplusFingerprintManager.TAG, "onError error:" + error + " vendorCode:" + vendorCode);
            OplusFingerprintManager.this.mHandler.obtainMessage(104, error, vendorCode).sendToTarget();
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintServiceReceiver
        public void onTouchDown(int sensorId) {
            OplusFingerprintManager.this.mHandler.obtainMessage(1001, sensorId, 0).sendToTarget();
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintServiceReceiver
        public void onTouchUp(int sensorId) {
            OplusFingerprintManager.this.mHandler.obtainMessage(1002, sensorId, 0).sendToTarget();
        }
    };

    /* loaded from: classes.dex */
    public interface FingerprintCommandCallback {
        void onFingerprintCmd(int i, byte[] bArr);
    }

    /* loaded from: classes.dex */
    public interface FingerprintExtraInfoCallback {
        void onError(int i, CharSequence charSequence);

        void onFingerprintEngineeringInfoUpdated(EngineeringInfo engineeringInfo);
    }

    /* loaded from: classes.dex */
    public interface FingerprintInputCallback {
        void onTouchDown();

        void onTouchUp();
    }

    public static IOplusFingerprintManager getService() {
        return (IOplusFingerprintManager) IOplusFingerprintManagerSingleton.get();
    }

    public OplusFingerprintManager(Context context) {
        this.mContext = context;
        if (context != null) {
            this.mHandler = new MyHandler(context);
        }
        ensureRemoteFingerprintService();
    }

    private void ensureRemoteFingerprintService() {
        if (this.mRemote == null) {
            IBinder service = ServiceManager.getService("fingerprint");
            this.mRemote = service;
            this.mService = IFingerprintService.Stub.asInterface(service);
            Log.d(TAG, "mRemote:" + this.mRemote);
        }
    }

    public int regsiterFingerprintCmdCallback(final FingerprintCommandCallback callback) {
        IFingerprintCommandCallback mIFingerprintCommandCallback = new IFingerprintCommandCallback.Stub() { // from class: android.hardware.fingerprint.OplusFingerprintManager.2
            @Override // android.hardware.fingerprint.IFingerprintCommandCallback
            public void onFingerprintCmd(int cmdId, byte[] result) {
                callback.onFingerprintCmd(cmdId, result);
            }
        };
        try {
            int res = getService().regsiterFingerprintCmdCallback(mIFingerprintCommandCallback);
            return res;
        } catch (RemoteException e) {
            Log.e(TAG, "regsiterFingerprintCmdCallback : " + e);
            return -1;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return -1;
        }
    }

    public int unregsiterFingerprintCmdCallback(final FingerprintCommandCallback callback) {
        IFingerprintCommandCallback mIFingerprintCommandCallback = new IFingerprintCommandCallback.Stub() { // from class: android.hardware.fingerprint.OplusFingerprintManager.3
            @Override // android.hardware.fingerprint.IFingerprintCommandCallback
            public void onFingerprintCmd(int cmdId, byte[] result) {
                callback.onFingerprintCmd(cmdId, result);
            }
        };
        try {
            int res = getService().unregsiterFingerprintCmdCallback(mIFingerprintCommandCallback);
            return res;
        } catch (RemoteException e) {
            Log.e(TAG, "unregsiterFingerprintCmdCallback : " + e);
            return -1;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return -1;
        }
    }

    public void setFingerKeymode(int enable, int sensorId) {
        try {
            getService().setFingerKeymode(enable, sensorId);
        } catch (RemoteException e) {
            Log.e(TAG, "setFingerKeymode : " + e);
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
        }
    }

    public int pauseEnroll(int sensorId) {
        try {
            int result = getService().pauseEnroll(sensorId);
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "pauseEnroll : " + e);
            return 0;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return 0;
        }
    }

    public int continueEnroll(int sensorId) {
        try {
            int result = getService().continueEnroll(sensorId);
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "continueEnroll : " + e);
            return 0;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return 0;
        }
    }

    public int getEnrollmentTotalTimes(int sensorId) {
        try {
            int result = getService().getEnrollmentTotalTimes(sensorId);
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "getEnrollmentTotalTimes : " + e);
            return 0;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return 0;
        }
    }

    public long getLockoutAttemptDeadline(int userId) {
        try {
            long result = getService().getLockoutAttemptDeadline(userId);
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "getLockoutAttemptDeadline : " + e);
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
            Log.e(TAG, "getFailedAttempts : " + e);
            return -1;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return -1;
        }
    }

    public int sendFingerprintCmd(int sensorId, int cmdId, byte[] inbuf) {
        try {
            int res = getService().sendFingerprintCmd(sensorId, cmdId, inbuf);
            return res;
        } catch (RemoteException e) {
            Log.e(TAG, "Remote exception in sendFingerprintCmd(): " + e);
            return -1;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return -1;
        }
    }

    /* loaded from: classes.dex */
    private class OnEngineeringMonitorCancelListener implements CancellationSignal.OnCancelListener {
        private final long mAuthRequestId;

        public OnEngineeringMonitorCancelListener(long authId) {
            this.mAuthRequestId = authId;
        }

        @Override // android.os.CancellationSignal.OnCancelListener
        public void onCancel() {
            OplusFingerprintManager.this.cancelGetFingerprintExtraInfo(this.mAuthRequestId);
        }
    }

    public void getFingerprintExtraInfo(FingerprintExtraInfoCallback callback, CancellationSignal cancel, int type, int sensorId) {
        if (callback == null) {
            throw new IllegalArgumentException("Must supply an getFingerprintExtraInfo callback");
        }
        this.mFingerprintExtraInfoCallback = callback;
        if (cancel != null && cancel.isCanceled()) {
            Log.w(TAG, "authentication already canceled");
            return;
        }
        try {
            long authId = getService().getFingerprintExtraInfo(this.mToken, this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), UserHandle.myUserId(), this.mServiceReceiver, type, sensorId);
            if (cancel != null) {
                cancel.setOnCancelListener(new OnEngineeringMonitorCancelListener(authId));
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Remote exception in getFingerprintExtraInfo(): " + e);
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
        }
    }

    public void cancelGetFingerprintExtraInfo(long requestId) {
        try {
            getService().cancelFingerprintExtraInfo(this.mToken, this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), requestId);
            this.mFingerprintExtraInfoCallback = null;
        } catch (RemoteException e) {
            Log.e(TAG, "Remote exception in cancelGetFingerprintExtraInfo(): " + e);
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
        }
    }

    /* loaded from: classes.dex */
    private class OnTouchEventMonitorCancelListener implements CancellationSignal.OnCancelListener {
        private final long mAuthRequestId;

        public OnTouchEventMonitorCancelListener(long authId) {
            this.mAuthRequestId = authId;
        }

        @Override // android.os.CancellationSignal.OnCancelListener
        public void onCancel() {
            OplusFingerprintManager.this.cancelTouchEventListener(this.mAuthRequestId);
        }
    }

    public void setTouchEventListener(FingerprintInputCallback callback, CancellationSignal cancel, int sensorId) {
        if (callback == null) {
            throw new IllegalArgumentException("Must supply an setTouchEventListener callback");
        }
        this.mFingerprintInputCallback = callback;
        if (cancel != null && cancel.isCanceled()) {
            Log.w(TAG, "authentication already canceled");
            return;
        }
        try {
            long authId = getService().setTouchEventListener(this.mToken, this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), ActivityManager.getCurrentUser(), this.mServiceReceiver, sensorId);
            if (cancel != null) {
                cancel.setOnCancelListener(new OnTouchEventMonitorCancelListener(authId));
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Remote exception in setTouchEventListener(): " + e);
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
        }
    }

    public void cancelTouchEventListener(long requestId) {
        try {
            getService().cancelTouchEventListener(this.mToken, this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), requestId);
            this.mFingerprintInputCallback = null;
        } catch (RemoteException e) {
            Log.e(TAG, "Remote exception in setTouchEventListener(): " + e);
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
        }
    }

    public void showFingerprintIcon() {
        try {
            getService().showFingerprintIcon(this.mToken, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote exception in showFingerprintIcon(): " + e);
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
        }
    }

    public void hideFingerprintIcon() {
        try {
            getService().hideFingerprintIcon(this.mToken, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote exception in hideFingerprintIcon(): " + e);
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
        }
    }

    public int getCurrentIconStatus() {
        try {
            int res = getService().getCurrentIconStatus(this.mToken, this.mContext.getOpPackageName());
            return res;
        } catch (RemoteException e) {
            Log.e(TAG, "Remote exception in getCurrentIconStatus(): " + e);
            return -1;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return -1;
        }
    }

    public boolean needForceUseFingerprintFirst(String pkgName) {
        try {
            boolean result = getService().needForceUseFingerprintFirst(pkgName);
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "needForceUseFingerprintFirst : " + e);
            return false;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return false;
        }
    }

    public boolean isFingerprintPay(String pkgName) {
        try {
            boolean result = getService().isFingerprintPay(pkgName);
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "isFingerprintPay : " + e);
            return false;
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
            return false;
        }
    }

    /* loaded from: classes.dex */
    private class MyHandler extends Handler {
        private MyHandler(Context context) {
            super(context.getMainLooper());
        }

        private MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 104:
                    OplusFingerprintManager.this.sendErrorResult(msg.arg1, msg.arg2);
                    return;
                case 1001:
                    OplusFingerprintManager.this.sendTouchDownEvent(msg.arg1);
                    return;
                case 1002:
                    OplusFingerprintManager.this.sendTouchUpEvent(msg.arg1);
                    return;
                case 1005:
                    OplusFingerprintManager.this.sendFingerprintEngineeringInfo((EngineeringInfo) msg.obj);
                    return;
                default:
                    Log.w(OplusFingerprintManager.TAG, "Unknown message: " + msg.what);
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendErrorResult(int errMsgId, int vendorCode) {
        int clientErrMsgId = errMsgId == 8 ? vendorCode + 1000 : errMsgId;
        FingerprintExtraInfoCallback fingerprintExtraInfoCallback = this.mFingerprintExtraInfoCallback;
        if (fingerprintExtraInfoCallback != null) {
            fingerprintExtraInfoCallback.onError(clientErrMsgId, FingerprintManager.getErrorString(this.mContext, errMsgId, vendorCode));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendTouchDownEvent(int sensorId) {
        FingerprintInputCallback fingerprintInputCallback = this.mFingerprintInputCallback;
        if (fingerprintInputCallback != null) {
            fingerprintInputCallback.onTouchDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendTouchUpEvent(int sensorId) {
        FingerprintInputCallback fingerprintInputCallback = this.mFingerprintInputCallback;
        if (fingerprintInputCallback != null) {
            fingerprintInputCallback.onTouchUp();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendFingerprintEngineeringInfo(EngineeringInfo info) {
        Log.d(TAG, "sendFingerprintEngineeringInfo");
        FingerprintExtraInfoCallback fingerprintExtraInfoCallback = this.mFingerprintExtraInfoCallback;
        if (fingerprintExtraInfoCallback != null) {
            fingerprintExtraInfoCallback.onFingerprintEngineeringInfoUpdated(info);
        }
    }

    public void authenticateFido(long challenge, CancellationSignal cancel, int flags, FingerprintManager.AuthenticationCallback callback, Handler handler) {
        Log.d(TAG, "authenticateFido called by " + this.mContext.getOpPackageName() + " AttributionTag:" + this.mContext.getAttributionTag());
        FingerprintManager mFingerprintManager = (FingerprintManager) this.mContext.getSystemService("fingerprint");
        int userId = this.mContext.getUserId();
        if (callback == null) {
            throw new IllegalArgumentException("Must supply an authentication callback");
        }
        if (cancel != null && cancel.isCanceled()) {
            Log.w(TAG, "authentication already canceled");
            return;
        }
        try {
            OplusFingerprintSupportUtils.invokeDeclaredMethod(mFingerprintManager, BIOMETRICS_FINGERPRINTMANAGER_CLASS, MOTHED_USE_HANDLER, new Class[]{Handler.class}, new Object[]{handler});
            OplusFingerprintSupportUtils.setDeclaredField(mFingerprintManager, BIOMETRICS_FINGERPRINTMANAGER_CLASS, FIELD_AUTHENTICATE_CALLBACK, callback);
            IFingerprintServiceReceiver mServiceReceiver = (IFingerprintServiceReceiver) OplusFingerprintSupportUtils.getDeclaredField(mFingerprintManager, BIOMETRICS_FINGERPRINTMANAGER_CLASS, FIELD_SERVICE_RECEIVER);
            IFingerprintService iFingerprintService = this.mService;
            if (iFingerprintService != null) {
                long authId = iFingerprintService.authenticate(this.mToken, challenge, mServiceReceiver, new FingerprintAuthenticateOptions.Builder().setSensorId(-1).setUserId(userId).setOpPackageName(this.mContext.getOpPackageName()).setAttributionTag(this.mContext.getAttributionTag()).setIgnoreEnrollmentState(flags != 0).build());
                if (cancel != null) {
                    cancel.setOnCancelListener(new OnAuthenticationCancelListener(authId));
                }
            }
        } catch (RemoteException e) {
            Log.w(TAG, "Remote exception while authenticating: ", e);
            callback.onAuthenticationError(1, this.mContext.getString(R.string.kg_failed_attempts_almost_at_wipe));
        }
    }

    /* loaded from: classes.dex */
    private class OnAuthenticationCancelListener implements CancellationSignal.OnCancelListener {
        private final long mAuthRequestId;

        public OnAuthenticationCancelListener(long authId) {
            this.mAuthRequestId = authId;
        }

        @Override // android.os.CancellationSignal.OnCancelListener
        public void onCancel() {
            Log.d(OplusFingerprintManager.TAG, "onCancel fingerprint authentication requested for RequestId: " + this.mAuthRequestId);
            OplusFingerprintManager.this.cancelAuthenticationFido(this.mAuthRequestId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAuthenticationFido(long requestId) {
        Log.d(TAG, "cancelAuthenticationFido called by " + this.mContext.getOpPackageName() + " AttributionTag:" + this.mContext.getAttributionTag() + " requestId:" + requestId + " mService: " + this.mService);
        IFingerprintService iFingerprintService = this.mService;
        if (iFingerprintService != null) {
            try {
                iFingerprintService.cancelAuthentication(this.mToken, this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), requestId);
            } catch (RemoteException e) {
                Log.e(TAG, "Remote exception in setTouchEventListener(): " + e);
            } catch (Exception e2) {
                Log.e(TAG, Log.getStackTraceString(e2));
            }
        }
    }
}
