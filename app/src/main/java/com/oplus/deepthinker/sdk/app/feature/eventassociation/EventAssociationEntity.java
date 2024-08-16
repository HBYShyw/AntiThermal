package com.oplus.deepthinker.sdk.app.feature.eventassociation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class EventAssociationEntity {
    public static final String ACTIVITY_RECOGNITION_ENTITY = "ActivityRecognitionEntity";
    public static final String AIR_PLANE_MODE_ENTITY = "AirPlaneModeEntity";
    public static final String APP_ASSISTANT_SCREEN = "com.coloros.assistantscreen";
    public static final String APP_ENTITY = "AppEntity";
    public static final String APP_INSTALL_ENTITY = "AppInstallEntity";
    public static final String AUDIO_ENTITY = "AudioEntity";
    public static final String BATTERY_ENTITY = "BatteryEntity";
    public static final String BLUETOOTH_ENTITY = "BluetoothEntity";
    public static final String BOOT_ENTITY = "BootEntity";
    public static final String CALL_STATE_ENTITY = "CallStateEntity";
    public static final String CELL_SIGNAL_ENTITY = "CellSignalEntity";
    public static final String GPS_ENTITY = "GpsEntity";
    public static final String HEADSET_ENTITY = "HeadSetEntity";
    public static final String LOCATION_ENTITY = "LocationEntity";
    public static final String LONG_CHARGING_ENTITY = "longChargingEntity";
    public static final String LOW_BATT_ENTITY = "LowBattEntity";
    public static final String NOTIFICATION_ENTITY = "NotificationEntity";
    public static final String POWER_SAVE_ENTITY = "PowerSaveEntity";
    public static final String SCREEN_ENTITY = "ScreenEntity";
    public static final String STAT_INFO = "STAT_INFO";
    public static final String TIME = "TIME";
    public static final String USER_CARD_ENTITY = "UserCardEntity";
    public static final String VOLUME_ENTITY = "VolumeEntity";
    public static final String VPN_ENTITY = "VpnEntity";
    public static final String WEEKDAY = "WEEKDAY";
    public static final String WIFI_ENTITY = "WifiEntity";
    private String mCandidateType;
    private Map<String, Integer> mCandidates;
    private Map<String, String> mCurEvents = new HashMap();

    public EventAssociationEntity(List<String> list, Map<String, Integer> map, String str) {
        this.mCandidates = new HashMap();
        setEvents(list);
        if (map != null) {
            this.mCandidates = map;
        }
        this.mCandidateType = str;
    }

    public void addEvents(List<String> list) {
        if (list == null) {
            return;
        }
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String[] split = it.next().split(":");
            if (split.length == 2) {
                this.mCurEvents.put(split[0], split[1]);
            }
        }
    }

    public String getCandidateType() {
        return this.mCandidateType;
    }

    public Map<String, Integer> getCandidates() {
        return this.mCandidates;
    }

    public Map<String, String> getCurEvents() {
        return this.mCurEvents;
    }

    public void setCandidateType(String str) {
        this.mCandidateType = str;
    }

    public void setCandidates(Map<String, Integer> map) {
        if (map != null) {
            this.mCandidates = map;
        }
    }

    public void setEvents(List<String> list) {
        if (list == null) {
            return;
        }
        addEvents(list);
    }

    public EventAssociationEntity(Map<String, Integer> map, String str) {
        this.mCandidates = new HashMap();
        if (map != null) {
            this.mCandidates = map;
        }
        this.mCurEvents.put(STAT_INFO, STAT_INFO);
        this.mCandidateType = str;
    }
}
