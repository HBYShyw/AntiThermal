package com.oplus.neuron;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.OplusPropertyList;
import android.os.OplusSystemProperties;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.util.Slog;
import com.oplus.network.OlkConstants;
import com.oplus.neuron.INeuronSystemService;
import java.util.List;

/* loaded from: classes.dex */
public final class NeuronSystemManager {
    public static final int DEFAULT_PROP = 1;
    public static final boolean LOG_ON = false;
    public static final int NS_LOG_ON = 2;
    public static final int NS_ON = 1;
    private static final String TAG = "NeuronSystem";
    private INeuronSystemService mService;
    public static int sNsProp = OplusSystemProperties.getInt(OplusPropertyList.PROPERTY_NEURON_SYSTEM, 1);
    private static NeuronSystemManager sNeuronSystemManager = null;

    private NeuronSystemManager() {
        this.mService = null;
        if (isEnable()) {
            INeuronSystemService asInterface = INeuronSystemService.Stub.asInterface(ServiceManager.getService("neuronsystem"));
            this.mService = asInterface;
            if (asInterface == null) {
                Slog.d(TAG, "can not get service neuronsystem");
            }
        }
    }

    public static NeuronSystemManager getInstance() {
        if (sNeuronSystemManager == null) {
            synchronized (NeuronSystemManager.class) {
                if (sNeuronSystemManager == null) {
                    sNeuronSystemManager = new NeuronSystemManager();
                }
            }
        }
        return sNeuronSystemManager;
    }

    public static boolean isEnable() {
        return (sNsProp & 1) != 0;
    }

    public void publishEvent(int type, ContentValues contentValues) {
        INeuronSystemService iNeuronSystemService;
        if (!isEnable() || (iNeuronSystemService = this.mService) == null) {
            return;
        }
        try {
            iNeuronSystemService.publishEvent(type, contentValues);
        } catch (Exception e) {
            Slog.d(TAG, "NeuronSystemManager publishEvent err: " + e);
        }
    }

    public void enableRecommendedApps(boolean enable, List<String> pkgs) {
        INeuronSystemService iNeuronSystemService;
        if (!isEnable() || (iNeuronSystemService = this.mService) == null) {
            Slog.d(TAG, "NeuronSystemManager enableRecommendedApps can not get service neuronsystem");
            return;
        }
        try {
            iNeuronSystemService.enableRecommendedApps(enable, pkgs);
        } catch (RemoteException e) {
            Slog.e(TAG, "Exception happend while enableRecommendedApps", e);
        }
    }

    public List<String> getRecommendedApps(int topK) {
        INeuronSystemService iNeuronSystemService;
        if (!isEnable() || (iNeuronSystemService = this.mService) == null) {
            Slog.d(TAG, "NeuronSystemManager getRecommendedApps can not get service neuronsystem");
            return null;
        }
        try {
            return iNeuronSystemService.getRecommendedApps(topK);
        } catch (RemoteException e) {
            Slog.e(TAG, "Exception happend while getRecommendedApps", e);
            return null;
        }
    }

    public static void notifyCellToNeuronSystem(int subId, List<CellInfo> cells, boolean is5GNsa) {
        if (isEnable()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("date", Long.valueOf(System.currentTimeMillis()));
            for (CellInfo cell : cells) {
                if (cell.isRegistered() && (cell instanceof CellInfoLte)) {
                    CellIdentityLte identity = ((CellInfoLte) cell).getCellIdentity();
                    String key = String.format("subid-%d-mcc-%s-mnc-%s-ci-%d-pci-%d-tac-%d", Integer.valueOf(subId), identity.getMccString(), identity.getMncString(), Integer.valueOf(identity.getCi()), Integer.valueOf(identity.getPci()), Integer.valueOf(identity.getTac()));
                    if (is5GNsa) {
                        contentValues.put("type", "5GNSA");
                    } else {
                        contentValues.put("type", "4G");
                    }
                    contentValues.put("data", key);
                    getInstance().publishEvent(107, contentValues);
                    return;
                }
                if (cell.isRegistered() && (cell instanceof CellInfoNr)) {
                    CellIdentityNr identity2 = (CellIdentityNr) ((CellInfoNr) cell).getCellIdentity();
                    String key2 = String.format("subid-%d-mcc-%s-mnc-%s-ci-%d-pci-%d-tac-%d", Integer.valueOf(subId), identity2.getMccString(), identity2.getMncString(), Long.valueOf(identity2.getNci()), Integer.valueOf(identity2.getPci()), Integer.valueOf(identity2.getTac()));
                    contentValues.put("type", "5GSA");
                    contentValues.put("data", key2);
                    getInstance().publishEvent(107, contentValues);
                    return;
                }
            }
        }
    }

    public static void notifyTextViewContent(Context context, int id, String text) {
        if (isEnable() && OplusSystemProperties.getBoolean(OplusPropertyList.PROPERTY_FEATURE_OCA_ON, false) && "com.app.shanghai.metro".equals(context.getPackageName())) {
            Resources r = context.getResources();
            if (id > 0 && Resources.resourceHasPackage(id) && r != null) {
                String entryname = r.getResourceEntryName(id);
                if ("tvInStationName".equals(entryname)) {
                    bringUpContextAwareService(context, "type_metro_app", text);
                } else if ("tvOutName".equals(entryname)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("SubwayOutStationName", text);
                    getInstance().publishEvent(108, contentValues);
                }
            }
        }
    }

    private static void bringUpContextAwareService(Context context, String type, String name) {
        Intent intent = new Intent();
        intent.setClassName("com.oplus.oca", "com.oppo.oca.MobileContextAwareService");
        intent.putExtra("SubwayInStationName", name);
        intent.putExtra("type", type);
        context.startService(intent);
    }

    public static void notifyBrightness(float brightnessState) {
        if (isEnable()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NsConstants.SCREEN_BRIGHTNESS, Integer.valueOf((int) brightnessState));
            getInstance().publishEvent(105, contentValues);
        }
    }

    public static void notifyLocationChange(String provider, Location fineLocation) {
        if (isEnable()) {
            ContentValues contentValues = new ContentValues();
            if ("gps".equals(provider)) {
                contentValues.put(NsConstants.LOCATION_PROVIDER_TYPE, (Integer) 0);
            } else if (OlkConstants.EXT_NETWORK.equals(provider)) {
                contentValues.put(NsConstants.LOCATION_PROVIDER_TYPE, (Integer) 1);
            } else if ("passive".equals(provider)) {
                contentValues.put(NsConstants.LOCATION_PROVIDER_TYPE, (Integer) 2);
            } else if ("fused".equals(provider)) {
                contentValues.put(NsConstants.LOCATION_PROVIDER_TYPE, (Integer) 3);
            } else {
                contentValues.put(NsConstants.LOCATION_PROVIDER_TYPE, (Integer) (-1));
            }
            contentValues.put("provider", provider);
            contentValues.put(NsConstants.GPS_LOCATION_ACCURACY, Float.valueOf(fineLocation.getAccuracy()));
            contentValues.put(NsConstants.GPS_LOCATION_ALTITUDE, Double.valueOf(fineLocation.getAltitude()));
            contentValues.put(NsConstants.GPS_LOCATION_LATITUDE, Double.valueOf(fineLocation.getLatitude()));
            contentValues.put(NsConstants.GPS_LOCATION_LONGITUDE, Double.valueOf(fineLocation.getLongitude()));
            contentValues.put(NsConstants.GPS_EVENT, (Integer) 3);
            getInstance().publishEvent(103, contentValues);
        }
    }

    public static void notifyActivityChange(String packageName, String activity, int uid, int pid) {
        if (isEnable()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NsConstants.PKGNAME, packageName);
            contentValues.put("activity", activity);
            contentValues.put("date", Long.valueOf(System.currentTimeMillis()));
            contentValues.put("uid", Integer.valueOf(uid));
            contentValues.put(NsConstants.PID, Integer.valueOf(pid));
            getInstance().publishEvent(101, contentValues);
        }
    }

    public static void notifyNotificationContent(Context context, String packageName, String title, String content) {
        if (isEnable() && OplusSystemProperties.getBoolean(OplusPropertyList.PROPERTY_FEATURE_OCA_ON, false) && packageName.equals("com.tencent.mm") && title.equals("腾讯乘车码") && content.contains("乘车通知")) {
            bringUpContextAwareService(context, "type_notification", null);
        }
    }
}
