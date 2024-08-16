package com.oplus.deepthinker.sdk.app.aidl.eventfountain;

/* loaded from: classes.dex */
public class EventType {
    public static final int ACTIVITY_MODE_IN_ELEVATOR = 312;
    public static final int ACTIVITY_MODE_IN_FOUR_WHEELER_VEHICLE = 311;
    public static final int ACTIVITY_MODE_IN_RAIL_VEHICLE = 309;
    public static final int ACTIVITY_MODE_IN_ROAD_VEHICLE = 308;
    public static final int ACTIVITY_MODE_IN_TRANSPORTATION = 313;
    public static final int ACTIVITY_MODE_IN_TWO_WHEELER_VEHICLE = 310;
    public static final int ACTIVITY_MODE_IN_VEHICLE = 300;
    public static final int ACTIVITY_MODE_ON_BICYCLE = 301;
    public static final int ACTIVITY_MODE_ON_FOOT = 302;
    public static final int ACTIVITY_MODE_RUNNING = 307;
    public static final int ACTIVITY_MODE_STILL = 303;
    public static final int ACTIVITY_MODE_TILTING = 305;
    public static final int ACTIVITY_MODE_UNKNOWN_ACTIVITY = 304;
    public static final int ACTIVITY_MODE_WALKING = 306;
    public static final int ARRIVE_COMPANY = 10005;
    public static final int ARRIVE_HOME = 10003;
    public static final int AWARENESS_CAPABILITY = 10008;
    public static final int AWARENESS_FENCE = 10009;
    public static final int AWARENESS_INTENT = 10014;
    public static final int AWARENESS_SCENE = 10013;
    public static final int AWARENESS_SERVICE = 10015;
    public static final String CHANGED_REASON = "changed_reason";
    public static final String CHANGED_STATE = "changed_state";
    public static final String CHANGED_STATE_VALUE = "changed_state_value";
    public static final String CHANGED_TYPE = "changed_type";
    public static final int CHANGED_TYPE_ENTER = 1;
    public static final int CHANGED_TYPE_EXIT = 2;
    public static final int CHANGED_TYPE_STATE = 8;
    public static final int CHANGED_TYPE_UPDATE = 4;
    public static final int CITY_FENCE = 10010;
    public static final int COMMON_STATE_KEY = 0;
    public static final int DEVICE_EVENT_BATTERY_CHANGED = 101;
    public static final int DEVICE_EVENT_CHARGING = 102;
    public static final int DEVICE_EVENT_EBOOK_FRONT = 110;
    public static final int DEVICE_EVENT_GPS = 103;
    public static final int DEVICE_EVENT_NFC = 108;
    public static final int DEVICE_EVENT_NOTIFICATION = 106;
    public static final int DEVICE_EVENT_OTG = 107;
    public static final int DEVICE_EVENT_PACKAGE_ADD = 109;
    public static final int DEVICE_EVENT_POWER_SAVING = 100;
    public static final int DEVICE_EVENT_SENSOR = 104;
    public static final int DEVICE_EVENT_WAKELOCK = 105;
    public static final int EVENT_ASSOCIATION = 10007;
    public static final int FILE_TRANSFER_SCENE_STATE_KEY_FROM_EVENT = 1;
    public static final int FILE_TRANSFER_SCENE_STATE_KEY_FROM_TRAFFIC = 0;
    public static final int GAME_SCENE_STATE_DEFAULT = 0;
    public static final int GAME_SCENE_STATE_GAMING = 7;
    public static final int GAME_SCENE_STATE_HALL = 4;
    public static final int GAME_SCENE_STATE_LOADING = 5;
    public static final int GAME_SCENE_STATE_LOADING_WAIT = 6;
    public static final int GAME_SCENE_STATE_LOGIN = 3;
    public static final int GAME_SCENE_STATE_SPECTATE = 8;
    public static final int GAME_SCENE_STATE_START = 1;
    public static final int GAME_SCENE_STATE_UPDATE = 2;
    public static final int GEOFENCE = 10002;
    public static final int INVALID = -1;
    public static final int LEAVE_COMPANY = 10006;
    public static final int LEAVE_HOME = 10004;
    public static final int NAVIGATE_SCENE_STATE_BACKGROUND = 0;
    public static final int NAVIGATE_SCENE_STATE_FOREGROUND = 1;
    public static final int NEXT_APP_EVENT = 10001;
    public static final int OFF = 0;
    public static final int ON = 1;
    public static final int PROVINCE_FENCE = 10011;
    public static final String REASON_CHANGE_STATE = "scene_state_change";
    public static final String REASON_PROCESS_DIE = "scene_process_die";
    public static final String REASON_PROCESS_FRONT = "scene_process_front";
    public static final String REASON_SCENE_RESUME = "scene_resume";
    public static final String REASON_SCENE_TIMEOUT = "scene_timeout";
    public static final int RECORDING_SCENE_STATE_CAMERA_PREVIEW = 1;
    public static final int RECORDING_SCENE_STATE_SCREEN_RECORD = 2;
    public static final int RECORDING_SCENE_STATE_VIDEO_PREVIEW = 3;
    public static final int RECORDING_SCENE_STATE_VIDEO_UPLOAD = 4;
    public static final int RESIDENCE_FENCE = 10012;
    public static final int SCENE_MODE_AUDIO_CALL = 208;
    public static final int SCENE_MODE_AUDIO_IN = 203;
    public static final int SCENE_MODE_AUDIO_OUT = 202;
    public static final int SCENE_MODE_BT_DEVICE = 216;
    public static final int SCENE_MODE_CAMERA = 204;
    public static final int SCENE_MODE_CONFERENCE = 222;
    public static final int SCENE_MODE_DOWNLOAD = 206;
    public static final int SCENE_MODE_FILE_DOWNLOAD = 210;
    public static final int SCENE_MODE_FILE_UPLOAD = 214;
    public static final int SCENE_MODE_GAME = 211;
    public static final int SCENE_MODE_HOLIDAY = 213;
    public static final int SCENE_MODE_LEARNING = 217;
    public static final int SCENE_MODE_LOCATION = 201;
    public static final int SCENE_MODE_MUSIC_PLAY = 215;
    public static final int SCENE_MODE_NAVIGATION = 218;
    public static final int SCENE_MODE_READING = 207;
    public static final int SCENE_MODE_RECORDING = 221;
    public static final int SCENE_MODE_VIDEO = 205;
    public static final int SCENE_MODE_VIDEO_CALL = 209;
    public static final int SCENE_MODE_VIDEO_LIVE = 212;
    public static final int SCENE_MODE_VPN = 219;
    public static final int SCENE_SHORT_VIDEO = 220;
    public static final String STATE_LIST = "state_list";
    public static final String STATE_PACKAGE_CHANGED_ADD = "add";
    public static final String STATE_PACKAGE_CHANGED_REMOVE = "remove";
    public static final int STATE_PACKAGE_CHANGE_KEY = 1;

    /* loaded from: classes.dex */
    public class CityFenceExtra {
        public static final String BUNDLE_KEY_NEW = "new";
        public static final String BUNDLE_KEY_OLD = "old";

        public CityFenceExtra() {
        }
    }

    /* loaded from: classes.dex */
    public class EventAssociationExtra {
        public static final String BUSINESS_ID = "businessId";
        public static final String EVENTASSOCIATION_CONFIG = "EventAssociationConfig";
        public static final int EVENT_INDEX = 0;
        public static final int EVENT_SIZE = 2;
        public static final int EVENT_STATUS_INDEX = 1;
        public static final int HABIT_LEARNT_FAILED = 0;
        public static final int HABIT_LEARNT_SUCCESS = 1;
        public static final int HABIT_PARAMETER_ERROR = 2;
        public static final int HABIT_QUERY_FAILED = -1;
        public static final int HABIT_RESULT_EMPTY = 3;
        public static final int HABIT_UNKNOWN_EXCEPTION = 4;
        public static final String JOINT = ":";
        public static final String LEARN_PERIOD = "learnPeriod";
        public static final String LEARN_STATUS = "learnStatus";
        public static final String MEITUAN_FOR_MONTH = "meituanParaForMonth";
        public static final String MEITUAN_FOR_TIME_WEEK = "meituanParaForTimeWeek";
        public static final String MEITUAN_FOR_WEEKDAY = "meituanParaForWeekday";
        public static final String PREDICT_RESULT = "eventAssociation";
        public static final String PREDICT_RESULT_UNDER_CONFIG = "resultUnderConfig";
        public static final String QUERY_TYPE = "queryType";
        public static final String RELATED_EVENTS_ID = "relatedEventsId";
        public static final String RESULT_TYPE = "resultTag";
        public static final String THRESHOLD = "threshold";
        public static final String TIME = "TIME";
        public static final int TYPE_GET_RESULT = 1;
        public static final int TYPE_QUERY_HABIT = 0;
        public static final String UNDERSCORE = "_";
        public static final String WEEKDAY = "WEEKDAY";

        public EventAssociationExtra() {
        }
    }

    /* loaded from: classes.dex */
    public class GeoFenceExtra {
        public static final String BUNDLE_KEY_GEOFENCE_EVENT = "geofence_event";
        public static final String BUNDLE_KEY_GEOFENCE_IDS = "geofence_ids";
        public static final String BUNDLE_KEY_GEOFENCE_REQUEST = "geofence_request";

        public GeoFenceExtra() {
        }
    }

    /* loaded from: classes.dex */
    public class NextAppExtra {
        public static final String PREDICT_RESULT = "next_app";

        public NextAppExtra() {
        }
    }

    /* loaded from: classes.dex */
    public static class ResidenceFenceExtra {
        public static final String BUNDLE_KEY_CARE_COMPANY_STATES = "company_states";
        public static final String BUNDLE_KEY_CARE_HOME_STATES = "home_states";
        public static final String BUNDLE_KEY_CARE_OTHER_RESIDENCE_STATES = "other_states";
        public static final String BUNDLE_KEY_RESIDENCE_TYPE = "residence_type";
        public static final String BUNDLE_KEY_RESULT_STATE = "result_state";
        public static final String BUNDLE_KEY_SINGLE_RESIDENCE_SWITCH = "single_residence_switch";
        public static final String BUNDLE_KEY_USE_WIFI_SWITCH = "use_wifi_switch";
        public static final int STATE_DWELL = 4;
        public static final int STATE_ENTER = 1;
        public static final int STATE_EXIT = 2;
        public static final int STATE_UNKNOWN = 8;
        public static final String TYPE_COMPANY = "company";
        public static final String TYPE_HOME = "home";
        public static final String TYPE_OTHER_RESIDENCE = "other residence";
    }

    /* loaded from: classes.dex */
    public class State {
        public static final int ENTER = 0;
        public static final int EXIT = 1;
        public static final int UPDATE = 2;

        public State() {
        }
    }
}
