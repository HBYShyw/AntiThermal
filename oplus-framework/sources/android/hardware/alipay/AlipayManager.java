package android.hardware.alipay;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;

/* loaded from: classes.dex */
public class AlipayManager {
    public static final String ALIPAY_SERVICE = "alipay";
    public static final int AUTH_TYPE_FACE = 4;
    public static final int AUTH_TYPE_FINGERPRINT = 1;
    public static final int AUTH_TYPE_IRIS = 2;
    public static final int AUTH_TYPE_NOT_SUPPORT = 0;
    public static final int AUTH_TYPE_OPTICAL_FINGERPRINT = 17;
    public static final int OPLUS_DEFAULT_FINGERPRINT_ICON_DIAMETER = 190;
    public static final int OPLUS_DEFAULT_FINGERPRINT_ICON_LOCATION_X = 445;
    public static final int OPLUS_DEFAULT_FINGERPRINT_ICON_LOCATION_Y = 1967;
    public static final String OPLUS_DEFAULT_MODEL = "OPLUS-Default";
    private static final String TAG = "AlipayManager";
    private Context mContext;
    private IAlipayService mService;
    private IBinder mToken = new Binder();

    public AlipayManager(Context context, IAlipayService service) {
        this.mContext = context;
        this.mService = service;
        if (service == null) {
            Slog.v(TAG, "AlipayService was null");
        }
    }

    public byte[] alipayInvokeCommand(byte[] inbuf) {
        IAlipayService iAlipayService = this.mService;
        if (iAlipayService != null) {
            try {
                byte[] outbuf = iAlipayService.alipayInvokeCommand(inbuf);
                return outbuf;
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in alipayInvokeCommand(): ", e);
                return null;
            }
        }
        Log.w(TAG, "alipayInvokeCommand(): Service not connected!");
        return null;
    }

    public int cancel(String reqId) {
        IAlipayService iAlipayService = this.mService;
        if (iAlipayService != null) {
            try {
                int result = iAlipayService.cancel(reqId);
                return result;
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in cancel(): ", e);
                return -1;
            }
        }
        Log.w(TAG, "cancel(): Service not connected!");
        return -1;
    }

    public int getSupportBIOTypes() {
        IAlipayService iAlipayService = this.mService;
        if (iAlipayService != null) {
            try {
                int type = iAlipayService.getSupportBIOTypes();
                return type;
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in getSupportBIOTypes(): ", e);
                return 0;
            }
        }
        Log.w(TAG, "Service not connected!");
        return 0;
    }

    public int getSupportIFAAVersion() {
        IAlipayService iAlipayService = this.mService;
        if (iAlipayService != null) {
            try {
                int type = iAlipayService.getSupportIFAAVersion();
                return type;
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in getSupportIFAAVersion(): ", e);
                return 0;
            }
        }
        Log.w(TAG, "Service not connected!");
        return 0;
    }

    public String getDeviceModel() {
        IAlipayService iAlipayService = this.mService;
        if (iAlipayService != null) {
            try {
                String model = iAlipayService.getDeviceModel();
                return model;
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in getDeviceModel(): ", e);
                return "OPLUS-Default";
            }
        }
        Log.w(TAG, "Service not connected!");
        return "OPLUS-Default";
    }

    public int getFingerprintIconDiameter() {
        IAlipayService iAlipayService = this.mService;
        if (iAlipayService != null) {
            try {
                int iconDiameter = iAlipayService.getFingerprintIconDiameter();
                return iconDiameter;
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in getFingerprintIconDiameter(): ", e);
                return 190;
            }
        }
        Log.w(TAG, "Service not connected!");
        return 190;
    }

    public int getFingerprintIconExternalCircleXY(String coordinate) {
        int coord;
        if ("X".equals(coordinate)) {
            coord = OPLUS_DEFAULT_FINGERPRINT_ICON_LOCATION_X;
        } else {
            coord = OPLUS_DEFAULT_FINGERPRINT_ICON_LOCATION_Y;
        }
        IAlipayService iAlipayService = this.mService;
        if (iAlipayService != null) {
            try {
                int coord2 = iAlipayService.getFingerprintIconExternalCircleXY(coordinate);
                return coord2;
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in getFingerprintIconExternalCircleXY(): ", e);
                return coord;
            }
        }
        Log.w(TAG, "Service not connected!");
        return coord;
    }
}
