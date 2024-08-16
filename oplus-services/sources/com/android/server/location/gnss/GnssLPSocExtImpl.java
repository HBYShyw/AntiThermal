package com.android.server.location.gnss;

import android.content.Context;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.android.server.location.common.OplusLbsFactory;
import com.android.server.location.gnss.hal.GnssNative;
import com.android.server.location.interfaces.IOplusConfigListener;
import com.android.server.location.interfaces.IOplusLBSMainClass;
import java.lang.reflect.Field;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class GnssLPSocExtImpl implements IGnssLocationProviderSocExt {
    private static final int GPS_DELETE_EPO = 16384;
    private static final int GPS_DELETE_HOT_STILL = 8192;
    private static final int LAST_LOCATION_EXPIRED_TIMEOUT = 600000;
    private static final String TAG = "GnssLocationProvider";
    private GnssLocationProvider mGnssLocationProvider;
    private boolean mDebug = false;
    private GnssLPSocExtHelper mGnssLPSocExtHelper = null;
    private Context mContext = null;
    private Handler mHandler = null;
    private NtpNetworkTimeHelper mNtpNetworkTimeHelper = null;
    private IOplusLBSMainClass mOplusLbsClass = IOplusLBSMainClass.DEFAULT;

    @Override // com.android.server.location.gnss.IGnssLocationProviderSocExt
    public void onGnssLocationProviderInitialize() {
    }

    public GnssLPSocExtImpl(Object obj) {
        this.mGnssLocationProvider = null;
        this.mGnssLocationProvider = (GnssLocationProvider) obj;
    }

    @Override // com.android.server.location.gnss.IGnssLocationProviderSocExt
    public void init(Context context, Handler handler, NtpNetworkTimeHelper ntpNetworkTimeHelper) {
        this.mContext = context;
        this.mHandler = handler;
        this.mNtpNetworkTimeHelper = ntpNetworkTimeHelper;
        IOplusLBSMainClass iOplusLBSMainClass = (IOplusLBSMainClass) OplusLbsFactory.getInstance().getFeature(IOplusLBSMainClass.DEFAULT, this.mContext);
        this.mOplusLbsClass = iOplusLBSMainClass;
        iOplusLBSMainClass.registerLbsConfigListener(new IOplusConfigListener() { // from class: com.android.server.location.gnss.GnssLPSocExtImpl.1
            @Override // com.android.server.location.interfaces.IOplusConfigListener
            public void onDebugLevelChanged(int i) {
                GnssLPSocExtImpl.this.mDebug = i > 0;
            }
        });
        initMtkGnssLocProvider();
    }

    @Override // com.android.server.location.gnss.IGnssLocationProviderSocExt
    public void onReportSvStatus(GnssStatus gnssStatus) {
        if (this.mDebug) {
            logSvStatus(gnssStatus);
        }
    }

    @Override // com.android.server.location.gnss.IGnssLocationProviderSocExt
    public int onDeleteAidingData(Bundle bundle, int i) {
        if (this.mGnssLPSocExtHelper != null) {
            if (bundle != null) {
                if (bundle.getBoolean("hot-still")) {
                    i |= 8192;
                }
                if (bundle.getBoolean("epo")) {
                    i |= 16384;
                }
            }
            Log.d(TAG, "mtkDeleteAidingData extras:" + bundle + "flag:" + i);
        }
        return i;
    }

    @Override // com.android.server.location.gnss.IGnssLocationProviderSocExt
    public void onRequestLocation(GnssNative gnssNative) {
        mtkInjectLastKnownLocation(gnssNative);
    }

    private void logSvStatus(GnssStatus gnssStatus) {
        if (gnssStatus != null) {
            for (int i = 0; i < gnssStatus.getSatelliteCount(); i++) {
                StringBuilder sb = new StringBuilder();
                sb.append("svid: ");
                sb.append(gnssStatus.getSvid(i));
                sb.append(" cn0: ");
                sb.append(gnssStatus.getCn0DbHz(i));
                sb.append(" basebandCn0: ");
                sb.append(gnssStatus.getBasebandCn0DbHz(i));
                sb.append(" elev: ");
                sb.append(gnssStatus.getElevationDegrees(i));
                sb.append(" azimuth: ");
                sb.append(gnssStatus.getAzimuthDegrees(i));
                sb.append(" carrier frequency: ");
                sb.append(gnssStatus.getCarrierFrequencyHz(i));
                sb.append(gnssStatus.hasEphemerisData(i) ? " E" : "  ");
                sb.append(gnssStatus.hasAlmanacData(i) ? " A" : "  ");
                String str = "";
                sb.append(gnssStatus.usedInFix(i) ? "U" : "");
                sb.append(gnssStatus.hasCarrierFrequencyHz(i) ? "F" : "");
                if (gnssStatus.hasBasebandCn0DbHz(i)) {
                    str = "B";
                }
                sb.append(str);
                Log.v(TAG, sb.toString());
            }
        }
    }

    private void initMtkGnssLocProvider() {
        this.mGnssLPSocExtHelper = new GnssLPSocExtHelper(this.mContext, this.mHandler);
        setNtpTimeStateIdle(this.mNtpNetworkTimeHelper);
    }

    private void mtkInjectLastKnownLocation(GnssNative gnssNative) {
        Location lastKnownLocation;
        if (this.mGnssLPSocExtHelper == null || (lastKnownLocation = ((LocationManager) this.mContext.getSystemService("location")).getLastKnownLocation("network")) == null || System.currentTimeMillis() - lastKnownLocation.getTime() >= 600000 || lastKnownLocation.isMock()) {
            return;
        }
        gnssNative.injectLocation(lastKnownLocation);
    }

    private void setNtpTimeStateIdle(NtpNetworkTimeHelper ntpNetworkTimeHelper) {
        Field field;
        Field field2 = null;
        try {
            field = NtpNetworkTimeHelper.class.getDeclaredField("mInjectNtpTimeState");
        } catch (NoSuchFieldException e) {
            e = e;
            field = null;
        }
        try {
            try {
                field2 = NtpNetworkTimeHelper.class.getDeclaredField("STATE_IDLE");
            } catch (NoSuchFieldException e2) {
                e = e2;
                e.printStackTrace();
                field.setAccessible(true);
                field2.setAccessible(true);
                Object obj = field2.get(ntpNetworkTimeHelper);
                field.set(ntpNetworkTimeHelper, obj);
                Log.d(TAG, "set ntpTimeState to " + obj);
                return;
            }
            Object obj2 = field2.get(ntpNetworkTimeHelper);
            field.set(ntpNetworkTimeHelper, obj2);
            Log.d(TAG, "set ntpTimeState to " + obj2);
            return;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return;
        }
        field.setAccessible(true);
        field2.setAccessible(true);
    }
}
