package com.oppo.omedia;

import android.hardware.camera2.utils.SurfaceUtils;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Size;
import android.util.Slog;
import android.view.Surface;
import java.util.List;

/* loaded from: classes.dex */
public class OMediaProxy {
    private static final String DESCRIPTOR = "com.oppo.omedia.IOMediaService";
    private static final int NORMAL_OPERATING_MODE = 0;
    private static final int OMEDIA_OFF = 0;
    private static final String TAG = "OMediaProxy";
    private static final int TRANSACTION_CLOSE_SESSION = 6;
    private static final int TRANSACTION_GET_OPERATING_MODE = 5;
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oppo.omedia.OMediaProxy.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Slog.e(OMediaProxy.TAG, "omedia service binder die.");
            if (OMediaProxy.this.mRemote != null) {
                OMediaProxy.this.mRemote.unlinkToDeath(OMediaProxy.this.mDeathRecipient, 0);
                OMediaProxy.this.mRemote = null;
            }
        }
    };
    private IBinder mRemote;
    private static int sOmediaSysEnabledProperty = SystemProperties.getInt("persist.sys.omedia.enable", 0);
    private static OMediaProxy sMediaProxyService = null;

    private OMediaProxy() {
        connectService();
    }

    private boolean connectService() {
        if (sOmediaSysEnabledProperty == 0) {
            return false;
        }
        IBinder checkService = ServiceManager.checkService("omedia");
        this.mRemote = checkService;
        if (checkService == null) {
            return false;
        }
        try {
            checkService.linkToDeath(this.mDeathRecipient, 0);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public static synchronized OMediaProxy getInstance() {
        OMediaProxy oMediaProxy;
        synchronized (OMediaProxy.class) {
            if (sMediaProxyService == null) {
                sMediaProxyService = new OMediaProxy();
            }
            oMediaProxy = sMediaProxyService;
        }
        return oMediaProxy;
    }

    public int getOperatingMode(List<Surface> surfaces, String camId) {
        if (sOmediaSysEnabledProperty == 0) {
            return 0;
        }
        if (this.mRemote == null && !connectService()) {
            return 0;
        }
        try {
            String params = getStreamInfoFromSurface(surfaces, camId);
            int tmpmode = getOperatingModeRemote(params);
            if (tmpmode > 0) {
                Slog.d(TAG, "omedia mode is " + tmpmode);
                return tmpmode;
            }
            return 0;
        } catch (Exception e) {
            Slog.e(TAG, "catch a omedia 'get operating mode' Exception");
            return 0;
        }
    }

    private int getOperatingModeRemote(String param) throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(param);
            this.mRemote.transact(5, _data, _reply, 0);
            _reply.readException();
            int _result = _reply.readInt();
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    private static String getStreamInfoFromSurface(List<Surface> surfaces, String camId) {
        String type;
        if (surfaces.size() == 0) {
            return "{}";
        }
        try {
            String strStreamCnt = Integer.toString(surfaces.size());
            String streamSize = "";
            for (Surface surface : surfaces) {
                Size size = SurfaceUtils.getSurfaceSize(surface);
                if (streamSize != null && !streamSize.isEmpty()) {
                    streamSize = streamSize + ",";
                }
                int format = SurfaceUtils.getSurfaceFormat(surface);
                switch (format) {
                    case 35:
                        type = "PreviewYuv";
                        break;
                    default:
                        type = "UnKown" + format;
                        break;
                }
                if (streamSize != null && !streamSize.isEmpty() && streamSize.contains(type)) {
                    type = type + surfaces.indexOf(surface);
                }
                streamSize = streamSize + "\"" + type + "\":\"" + size.getWidth() + "x" + size.getHeight() + "\"";
            }
            String params = "{\"CamId\":" + camId + ",\"StreamCount\":" + strStreamCnt + "," + streamSize + "}";
            return params;
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public boolean sendCameraDeviceClose(String param) {
        if (sOmediaSysEnabledProperty == 0 || (this.mRemote == null && !connectService())) {
            return false;
        }
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        boolean ret = false;
        try {
            try {
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeString(param);
                this.mRemote.transact(6, _data, _reply, 0);
                _reply.readException();
                ret = _reply.readInt() > 0;
            } catch (Exception e) {
                Slog.e(TAG, "catch a omedia 'send close time' Exception");
            }
            return ret;
        } finally {
            _data.recycle();
            _reply.recycle();
        }
    }
}
