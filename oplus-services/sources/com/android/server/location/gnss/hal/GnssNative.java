package com.android.server.location.gnss.hal;

import android.location.GnssAntennaInfo;
import android.location.GnssCapabilities;
import android.location.GnssMeasurementCorrections;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssSignalType;
import android.location.GnssStatus;
import android.location.Location;
import android.os.Binder;
import android.os.SystemClock;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.internal.util.Preconditions;
import com.android.server.FgThread;
import com.android.server.location.common.OplusLbsFactory;
import com.android.server.location.gnss.GnssConfiguration;
import com.android.server.location.gnss.GnssManagerService;
import com.android.server.location.gnss.GnssPowerStats;
import com.android.server.location.injector.EmergencyHelper;
import com.android.server.location.injector.Injector;
import com.android.server.location.interfaces.IOplusLBSMainClass;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class GnssNative {
    public static final int AGPS_REF_LOCATION_TYPE_GSM_CELLID = 1;
    public static final int AGPS_REF_LOCATION_TYPE_LTE_CELLID = 4;
    public static final int AGPS_REF_LOCATION_TYPE_NR_CELLID = 8;
    public static final int AGPS_REF_LOCATION_TYPE_UMTS_CELLID = 2;
    public static final int AGPS_SETID_TYPE_IMSI = 1;
    public static final int AGPS_SETID_TYPE_MSISDN = 2;
    public static final int AGPS_SETID_TYPE_NONE = 0;
    public static final int GNSS_AIDING_TYPE_ALL = 65535;
    public static final int GNSS_AIDING_TYPE_ALMANAC = 2;
    public static final int GNSS_AIDING_TYPE_CELLDB_INFO = 32768;
    public static final int GNSS_AIDING_TYPE_EPHEMERIS = 1;
    public static final int GNSS_AIDING_TYPE_HEALTH = 64;
    public static final int GNSS_AIDING_TYPE_IONO = 16;
    public static final int GNSS_AIDING_TYPE_POSITION = 4;
    public static final int GNSS_AIDING_TYPE_RTI = 1024;
    public static final int GNSS_AIDING_TYPE_SADATA = 512;
    public static final int GNSS_AIDING_TYPE_SVDIR = 128;
    public static final int GNSS_AIDING_TYPE_SVSTEER = 256;
    public static final int GNSS_AIDING_TYPE_TIME = 8;
    public static final int GNSS_AIDING_TYPE_UTC = 32;
    public static final int GNSS_LOCATION_HAS_ALTITUDE = 2;
    public static final int GNSS_LOCATION_HAS_BEARING = 8;
    public static final int GNSS_LOCATION_HAS_BEARING_ACCURACY = 128;
    public static final int GNSS_LOCATION_HAS_HORIZONTAL_ACCURACY = 16;
    public static final int GNSS_LOCATION_HAS_LAT_LONG = 1;
    public static final int GNSS_LOCATION_HAS_SPEED = 4;
    public static final int GNSS_LOCATION_HAS_SPEED_ACCURACY = 64;
    public static final int GNSS_LOCATION_HAS_VERTICAL_ACCURACY = 32;
    public static final int GNSS_POSITION_MODE_MS_ASSISTED = 2;
    public static final int GNSS_POSITION_MODE_MS_BASED = 1;
    public static final int GNSS_POSITION_MODE_STANDALONE = 0;
    public static final int GNSS_POSITION_RECURRENCE_PERIODIC = 0;
    public static final int GNSS_POSITION_RECURRENCE_SINGLE = 1;
    public static final int GNSS_REALTIME_HAS_TIMESTAMP_NS = 1;
    public static final int GNSS_REALTIME_HAS_TIME_UNCERTAINTY_NS = 2;
    private static final float ITAR_SPEED_LIMIT_METERS_PER_SECOND = 400.0f;

    @GuardedBy({"GnssNative.class"})
    private static GnssHal sGnssHal;

    @GuardedBy({"GnssNative.class"})
    private static boolean sGnssHalInitialized;

    @GuardedBy({"GnssNative.class"})
    private static GnssNative sInstance;
    private AGpsCallbacks mAGpsCallbacks;
    private final GnssConfiguration mConfiguration;
    private final EmergencyHelper mEmergencyHelper;
    private GeofenceCallbacks mGeofenceCallbacks;
    private final GnssHal mGnssHal;
    private volatile boolean mItarSpeedLimitExceeded;
    private LocationRequestCallbacks mLocationRequestCallbacks;
    private NotificationCallbacks mNotificationCallbacks;
    private PsdsCallbacks mPsdsCallbacks;
    private boolean mRegistered;
    private TimeCallbacks mTimeCallbacks;
    private int mTopFlags;
    private BaseCallbacks[] mBaseCallbacks = new BaseCallbacks[0];
    private StatusCallbacks[] mStatusCallbacks = new StatusCallbacks[0];
    private SvStatusCallbacks[] mSvStatusCallbacks = new SvStatusCallbacks[0];
    private NmeaCallbacks[] mNmeaCallbacks = new NmeaCallbacks[0];
    private LocationCallbacks[] mLocationCallbacks = new LocationCallbacks[0];
    private MeasurementCallbacks[] mMeasurementCallbacks = new MeasurementCallbacks[0];
    private AntennaInfoCallbacks[] mAntennaInfoCallbacks = new AntennaInfoCallbacks[0];
    private NavigationMessageCallbacks[] mNavigationMessageCallbacks = new NavigationMessageCallbacks[0];
    private GnssCapabilities mCapabilities = new GnssCapabilities.Builder().build();
    private GnssPowerStats mPowerStats = null;
    private int mHardwareYear = 0;
    private String mHardwareModelName = null;
    private long mStartRealtimeMs = 0;
    private boolean mHasFirstFix = false;
    private IOplusLBSMainClass mOplusLbsClass = null;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface AGpsCallbacks {
        public static final int AGPS_REQUEST_SETID_IMSI = 1;
        public static final int AGPS_REQUEST_SETID_MSISDN = 2;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public @interface AgpsSetIdFlags {
        }

        void onReportAGpsStatus(int i, int i2, byte[] bArr);

        void onRequestSetID(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface AgpsReferenceLocationType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface AgpsSetIdType {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface AntennaInfoCallbacks {
        void onReportAntennaInfo(List<GnssAntennaInfo> list);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface BaseCallbacks {
        default void onCapabilitiesChanged(GnssCapabilities gnssCapabilities, GnssCapabilities gnssCapabilities2) {
        }

        void onHalRestarted();

        default void onHalStarted() {
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface GeofenceCallbacks {
        public static final int GEOFENCE_AVAILABILITY_AVAILABLE = 2;
        public static final int GEOFENCE_AVAILABILITY_UNAVAILABLE = 1;
        public static final int GEOFENCE_STATUS_ERROR_GENERIC = -149;
        public static final int GEOFENCE_STATUS_ERROR_ID_EXISTS = -101;
        public static final int GEOFENCE_STATUS_ERROR_ID_UNKNOWN = -102;
        public static final int GEOFENCE_STATUS_ERROR_INVALID_TRANSITION = -103;
        public static final int GEOFENCE_STATUS_ERROR_TOO_MANY_GEOFENCES = 100;
        public static final int GEOFENCE_STATUS_OPERATION_SUCCESS = 0;
        public static final int GEOFENCE_TRANSITION_ENTERED = 1;
        public static final int GEOFENCE_TRANSITION_EXITED = 2;
        public static final int GEOFENCE_TRANSITION_UNCERTAIN = 4;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public @interface GeofenceAvailability {
        }

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public @interface GeofenceStatus {
        }

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public @interface GeofenceTransition {
        }

        void onReportGeofenceAddStatus(int i, int i2);

        void onReportGeofencePauseStatus(int i, int i2);

        void onReportGeofenceRemoveStatus(int i, int i2);

        void onReportGeofenceResumeStatus(int i, int i2);

        void onReportGeofenceStatus(int i, Location location);

        void onReportGeofenceTransition(int i, Location location, int i2, long j);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface GnssAidingTypeFlags {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface GnssLocationFlags {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface GnssPositionMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface GnssPositionRecurrence {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface GnssRealtimeFlags {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface LocationCallbacks {
        void onReportLocation(boolean z, Location location);

        void onReportLocations(Location[] locationArr);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface LocationRequestCallbacks {
        void onRequestLocation(boolean z, boolean z2);

        void onRequestRefLocation();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface MeasurementCallbacks {
        void onReportMeasurements(GnssMeasurementsEvent gnssMeasurementsEvent);
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private @interface NativeEntryPoint {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface NavigationMessageCallbacks {
        void onReportNavigationMessage(GnssNavigationMessage gnssNavigationMessage);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface NmeaCallbacks {
        void onReportNmea(long j);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface NotificationCallbacks {
        void onReportNfwNotification(String str, byte b, String str2, byte b2, String str3, byte b3, boolean z, boolean z2);

        void onReportNiNotification(int i, int i2, int i3, int i4, int i5, String str, String str2, int i6, int i7);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface PsdsCallbacks {
        void onRequestPsdsDownload(int i);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface StatusCallbacks {
        public static final int GNSS_STATUS_ENGINE_OFF = 4;
        public static final int GNSS_STATUS_ENGINE_ON = 3;
        public static final int GNSS_STATUS_NONE = 0;
        public static final int GNSS_STATUS_SESSION_BEGIN = 1;
        public static final int GNSS_STATUS_SESSION_END = 2;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public @interface GnssStatusValue {
        }

        void onReportFirstFix(int i);

        void onReportStatus(int i);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface SvStatusCallbacks {
        void onReportSvStatus(GnssStatus gnssStatus);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface TimeCallbacks {
        void onRequestUtcTime();
    }

    /* renamed from: -$$Nest$smnative_get_batch_size, reason: not valid java name */
    static /* bridge */ /* synthetic */ int m890$$Nest$smnative_get_batch_size() {
        return native_get_batch_size();
    }

    /* renamed from: -$$Nest$smnative_get_internal_state, reason: not valid java name */
    static /* bridge */ /* synthetic */ String m891$$Nest$smnative_get_internal_state() {
        return native_get_internal_state();
    }

    /* renamed from: -$$Nest$smnative_init, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m892$$Nest$smnative_init() {
        return native_init();
    }

    /* renamed from: -$$Nest$smnative_init_batching, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m893$$Nest$smnative_init_batching() {
        return native_init_batching();
    }

    /* renamed from: -$$Nest$smnative_is_antenna_info_supported, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m900$$Nest$smnative_is_antenna_info_supported() {
        return native_is_antenna_info_supported();
    }

    /* renamed from: -$$Nest$smnative_is_geofence_supported, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m901$$Nest$smnative_is_geofence_supported() {
        return native_is_geofence_supported();
    }

    /* renamed from: -$$Nest$smnative_is_gnss_visibility_control_supported, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m902$$Nest$smnative_is_gnss_visibility_control_supported() {
        return native_is_gnss_visibility_control_supported();
    }

    /* renamed from: -$$Nest$smnative_is_measurement_corrections_supported, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m903$$Nest$smnative_is_measurement_corrections_supported() {
        return native_is_measurement_corrections_supported();
    }

    /* renamed from: -$$Nest$smnative_is_measurement_supported, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m904$$Nest$smnative_is_measurement_supported() {
        return native_is_measurement_supported();
    }

    /* renamed from: -$$Nest$smnative_is_navigation_message_supported, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m905$$Nest$smnative_is_navigation_message_supported() {
        return native_is_navigation_message_supported();
    }

    /* renamed from: -$$Nest$smnative_is_supported, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m906$$Nest$smnative_is_supported() {
        return native_is_supported();
    }

    /* renamed from: -$$Nest$smnative_start, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m915$$Nest$smnative_start() {
        return native_start();
    }

    /* renamed from: -$$Nest$smnative_start_antenna_info_listening, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m916$$Nest$smnative_start_antenna_info_listening() {
        return native_start_antenna_info_listening();
    }

    /* renamed from: -$$Nest$smnative_start_navigation_message_collection, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m919$$Nest$smnative_start_navigation_message_collection() {
        return native_start_navigation_message_collection();
    }

    /* renamed from: -$$Nest$smnative_start_nmea_message_collection, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m920$$Nest$smnative_start_nmea_message_collection() {
        return native_start_nmea_message_collection();
    }

    /* renamed from: -$$Nest$smnative_start_sv_status_collection, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m921$$Nest$smnative_start_sv_status_collection() {
        return native_start_sv_status_collection();
    }

    /* renamed from: -$$Nest$smnative_stop, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m922$$Nest$smnative_stop() {
        return native_stop();
    }

    /* renamed from: -$$Nest$smnative_stop_antenna_info_listening, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m923$$Nest$smnative_stop_antenna_info_listening() {
        return native_stop_antenna_info_listening();
    }

    /* renamed from: -$$Nest$smnative_stop_batch, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m924$$Nest$smnative_stop_batch() {
        return native_stop_batch();
    }

    /* renamed from: -$$Nest$smnative_stop_measurement_collection, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m925$$Nest$smnative_stop_measurement_collection() {
        return native_stop_measurement_collection();
    }

    /* renamed from: -$$Nest$smnative_stop_navigation_message_collection, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m926$$Nest$smnative_stop_navigation_message_collection() {
        return native_stop_navigation_message_collection();
    }

    /* renamed from: -$$Nest$smnative_stop_nmea_message_collection, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m927$$Nest$smnative_stop_nmea_message_collection() {
        return native_stop_nmea_message_collection();
    }

    /* renamed from: -$$Nest$smnative_stop_sv_status_collection, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m928$$Nest$smnative_stop_sv_status_collection() {
        return native_stop_sv_status_collection();
    }

    /* renamed from: -$$Nest$smnative_supports_psds, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m929$$Nest$smnative_supports_psds() {
        return native_supports_psds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_add_geofence(int i, double d, double d2, double d3, int i2, int i3, int i4, int i5);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_agps_set_id(int i, String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_agps_set_ref_location_cellid(int i, int i2, int i3, int i4, long j, int i5, int i6, int i7);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_class_init_once();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_cleanup();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_cleanup_batching();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_delete_aiding_data(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_flush_batch();

    private static native int native_get_batch_size();

    private static native String native_get_internal_state();

    private static native boolean native_init();

    private static native boolean native_init_batching();

    /* JADX INFO: Access modifiers changed from: private */
    public native void native_init_once(boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_inject_best_location(int i, double d, double d2, double d3, float f, float f2, float f3, float f4, float f5, float f6, long j, int i2, long j2, double d4);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_inject_location(int i, double d, double d2, double d3, float f, float f2, float f3, float f4, float f5, float f6, long j, int i2, long j2, double d4);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_inject_measurement_corrections(GnssMeasurementCorrections gnssMeasurementCorrections);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_inject_ni_supl_message_data(byte[] bArr, int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_inject_psds_data(byte[] bArr, int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_inject_time(long j, long j2, int i);

    private static native boolean native_is_antenna_info_supported();

    private static native boolean native_is_geofence_supported();

    private static native boolean native_is_gnss_visibility_control_supported();

    private static native boolean native_is_measurement_corrections_supported();

    private static native boolean native_is_measurement_supported();

    private static native boolean native_is_navigation_message_supported();

    private static native boolean native_is_supported();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_pause_geofence(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int native_read_nmea(byte[] bArr, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_remove_geofence(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_request_power_stats();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_resume_geofence(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_send_ni_response(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_set_agps_server(int i, String str, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_position_mode(int i, int i2, int i3, int i4, int i5, boolean z);

    private static native boolean native_start();

    private static native boolean native_start_antenna_info_listening();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_start_batch(long j, float f, boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_start_measurement_collection(boolean z, boolean z2, int i);

    private static native boolean native_start_navigation_message_collection();

    private static native boolean native_start_nmea_message_collection();

    private static native boolean native_start_sv_status_collection();

    private static native boolean native_stop();

    private static native boolean native_stop_antenna_info_listening();

    private static native boolean native_stop_batch();

    private static native boolean native_stop_measurement_collection();

    private static native boolean native_stop_navigation_message_collection();

    private static native boolean native_stop_nmea_message_collection();

    private static native boolean native_stop_sv_status_collection();

    private static native boolean native_supports_psds();

    @VisibleForTesting
    public static synchronized void setGnssHalForTest(GnssHal gnssHal) {
        synchronized (GnssNative.class) {
            Objects.requireNonNull(gnssHal);
            sGnssHal = gnssHal;
            sGnssHalInitialized = false;
            sInstance = null;
        }
    }

    private static synchronized void initializeHal() {
        synchronized (GnssNative.class) {
            if (!sGnssHalInitialized) {
                if (sGnssHal == null) {
                    sGnssHal = new GnssHal();
                }
                sGnssHal.classInitOnce();
                sGnssHalInitialized = true;
            }
        }
    }

    public static synchronized boolean isSupported() {
        boolean isSupported;
        synchronized (GnssNative.class) {
            initializeHal();
            isSupported = sGnssHal.isSupported();
        }
        return isSupported;
    }

    public static synchronized GnssNative create(Injector injector, GnssConfiguration gnssConfiguration) {
        GnssNative gnssNative;
        synchronized (GnssNative.class) {
            Preconditions.checkState(isSupported());
            Preconditions.checkState(sInstance == null);
            gnssNative = new GnssNative(sGnssHal, injector, gnssConfiguration);
            sInstance = gnssNative;
        }
        return gnssNative;
    }

    private GnssNative(GnssHal gnssHal, Injector injector, GnssConfiguration gnssConfiguration) {
        Objects.requireNonNull(gnssHal);
        this.mGnssHal = gnssHal;
        this.mEmergencyHelper = injector.getEmergencyHelper();
        this.mConfiguration = gnssConfiguration;
    }

    public void addBaseCallbacks(BaseCallbacks baseCallbacks) {
        Preconditions.checkState(!this.mRegistered);
        this.mBaseCallbacks = (BaseCallbacks[]) ArrayUtils.appendElement(BaseCallbacks.class, this.mBaseCallbacks, baseCallbacks);
    }

    public void addStatusCallbacks(StatusCallbacks statusCallbacks) {
        Preconditions.checkState(!this.mRegistered);
        this.mStatusCallbacks = (StatusCallbacks[]) ArrayUtils.appendElement(StatusCallbacks.class, this.mStatusCallbacks, statusCallbacks);
    }

    public void addSvStatusCallbacks(SvStatusCallbacks svStatusCallbacks) {
        Preconditions.checkState(!this.mRegistered);
        this.mSvStatusCallbacks = (SvStatusCallbacks[]) ArrayUtils.appendElement(SvStatusCallbacks.class, this.mSvStatusCallbacks, svStatusCallbacks);
    }

    public void addNmeaCallbacks(NmeaCallbacks nmeaCallbacks) {
        Preconditions.checkState(!this.mRegistered);
        this.mNmeaCallbacks = (NmeaCallbacks[]) ArrayUtils.appendElement(NmeaCallbacks.class, this.mNmeaCallbacks, nmeaCallbacks);
    }

    public void addLocationCallbacks(LocationCallbacks locationCallbacks) {
        Preconditions.checkState(!this.mRegistered);
        this.mLocationCallbacks = (LocationCallbacks[]) ArrayUtils.appendElement(LocationCallbacks.class, this.mLocationCallbacks, locationCallbacks);
    }

    public void addMeasurementCallbacks(MeasurementCallbacks measurementCallbacks) {
        Preconditions.checkState(!this.mRegistered);
        this.mMeasurementCallbacks = (MeasurementCallbacks[]) ArrayUtils.appendElement(MeasurementCallbacks.class, this.mMeasurementCallbacks, measurementCallbacks);
    }

    public void addAntennaInfoCallbacks(AntennaInfoCallbacks antennaInfoCallbacks) {
        Preconditions.checkState(!this.mRegistered);
        this.mAntennaInfoCallbacks = (AntennaInfoCallbacks[]) ArrayUtils.appendElement(AntennaInfoCallbacks.class, this.mAntennaInfoCallbacks, antennaInfoCallbacks);
    }

    public void addNavigationMessageCallbacks(NavigationMessageCallbacks navigationMessageCallbacks) {
        Preconditions.checkState(!this.mRegistered);
        this.mNavigationMessageCallbacks = (NavigationMessageCallbacks[]) ArrayUtils.appendElement(NavigationMessageCallbacks.class, this.mNavigationMessageCallbacks, navigationMessageCallbacks);
    }

    public void setGeofenceCallbacks(GeofenceCallbacks geofenceCallbacks) {
        Preconditions.checkState(!this.mRegistered);
        Preconditions.checkState(this.mGeofenceCallbacks == null);
        Objects.requireNonNull(geofenceCallbacks);
        this.mGeofenceCallbacks = geofenceCallbacks;
    }

    public void setTimeCallbacks(TimeCallbacks timeCallbacks) {
        Preconditions.checkState(!this.mRegistered);
        Preconditions.checkState(this.mTimeCallbacks == null);
        Objects.requireNonNull(timeCallbacks);
        this.mTimeCallbacks = timeCallbacks;
    }

    public void setLocationRequestCallbacks(LocationRequestCallbacks locationRequestCallbacks) {
        Preconditions.checkState(!this.mRegistered);
        Preconditions.checkState(this.mLocationRequestCallbacks == null);
        Objects.requireNonNull(locationRequestCallbacks);
        this.mLocationRequestCallbacks = locationRequestCallbacks;
    }

    public void setPsdsCallbacks(PsdsCallbacks psdsCallbacks) {
        Preconditions.checkState(!this.mRegistered);
        Preconditions.checkState(this.mPsdsCallbacks == null);
        Objects.requireNonNull(psdsCallbacks);
        this.mPsdsCallbacks = psdsCallbacks;
    }

    public void setAGpsCallbacks(AGpsCallbacks aGpsCallbacks) {
        Preconditions.checkState(!this.mRegistered);
        Preconditions.checkState(this.mAGpsCallbacks == null);
        Objects.requireNonNull(aGpsCallbacks);
        this.mAGpsCallbacks = aGpsCallbacks;
    }

    public void setNotificationCallbacks(NotificationCallbacks notificationCallbacks) {
        Preconditions.checkState(!this.mRegistered);
        Preconditions.checkState(this.mNotificationCallbacks == null);
        Objects.requireNonNull(notificationCallbacks);
        this.mNotificationCallbacks = notificationCallbacks;
    }

    public void register() {
        Preconditions.checkState(!this.mRegistered);
        this.mRegistered = true;
        int i = 0;
        initializeGnss(false);
        Log.i(GnssManagerService.TAG, "gnss hal started");
        while (true) {
            BaseCallbacks[] baseCallbacksArr = this.mBaseCallbacks;
            if (i >= baseCallbacksArr.length) {
                return;
            }
            baseCallbacksArr[i].onHalStarted();
            i++;
        }
    }

    private void initializeGnss(boolean z) {
        Preconditions.checkState(this.mRegistered);
        this.mTopFlags = 0;
        this.mGnssHal.initOnce(this, z);
        if (this.mGnssHal.init()) {
            this.mGnssHal.cleanup();
            Log.i(GnssManagerService.TAG, "gnss hal initialized");
        } else {
            Log.e(GnssManagerService.TAG, "gnss hal initialization failed");
        }
    }

    public GnssConfiguration getConfiguration() {
        return this.mConfiguration;
    }

    public boolean init() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.init();
    }

    public void cleanup() {
        Preconditions.checkState(this.mRegistered);
        this.mGnssHal.cleanup();
    }

    public GnssPowerStats getPowerStats() {
        return this.mPowerStats;
    }

    public GnssCapabilities getCapabilities() {
        return this.mCapabilities;
    }

    public int getHardwareYear() {
        return this.mHardwareYear;
    }

    public String getHardwareModelName() {
        return this.mHardwareModelName;
    }

    public boolean isItarSpeedLimitExceeded() {
        return this.mItarSpeedLimitExceeded;
    }

    public boolean start() {
        Preconditions.checkState(this.mRegistered);
        this.mStartRealtimeMs = SystemClock.elapsedRealtime();
        this.mHasFirstFix = false;
        return this.mGnssHal.start();
    }

    public boolean stop() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.stop();
    }

    public boolean setPositionMode(int i, int i2, int i3, int i4, int i5, boolean z) {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.setPositionMode(i, i2, i3, i4, i5, z);
    }

    public String getInternalState() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.getInternalState();
    }

    public void deleteAidingData(int i) {
        Preconditions.checkState(this.mRegistered);
        this.mGnssHal.deleteAidingData(i);
    }

    public int readNmea(byte[] bArr, int i) {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.readNmea(bArr, i);
    }

    public void injectLocation(Location location) {
        Preconditions.checkState(this.mRegistered);
        if (location.hasAccuracy()) {
            int i = (location.hasAltitude() ? 2 : 0) | 1 | (location.hasSpeed() ? 4 : 0) | (location.hasBearing() ? 8 : 0) | (location.hasAccuracy() ? 16 : 0) | (location.hasVerticalAccuracy() ? 32 : 0) | (location.hasSpeedAccuracy() ? 64 : 0) | (location.hasBearingAccuracy() ? 128 : 0);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double altitude = location.getAltitude();
            float speed = location.getSpeed();
            float bearing = location.getBearing();
            float accuracy = location.getAccuracy();
            float verticalAccuracyMeters = location.getVerticalAccuracyMeters();
            float speedAccuracyMetersPerSecond = location.getSpeedAccuracyMetersPerSecond();
            float bearingAccuracyDegrees = location.getBearingAccuracyDegrees();
            long time = location.getTime();
            int i2 = location.hasElapsedRealtimeUncertaintyNanos() ? 2 : 0;
            this.mGnssHal.injectLocation(i, latitude, longitude, altitude, speed, bearing, accuracy, verticalAccuracyMeters, speedAccuracyMetersPerSecond, bearingAccuracyDegrees, time, i2 | 1, location.getElapsedRealtimeNanos(), location.getElapsedRealtimeUncertaintyNanos());
        }
    }

    public void injectBestLocation(Location location) {
        Preconditions.checkState(this.mRegistered);
        int i = (location.hasAltitude() ? 2 : 0) | 1 | (location.hasSpeed() ? 4 : 0) | (location.hasBearing() ? 8 : 0) | (location.hasAccuracy() ? 16 : 0) | (location.hasVerticalAccuracy() ? 32 : 0) | (location.hasSpeedAccuracy() ? 64 : 0) | (location.hasBearingAccuracy() ? 128 : 0);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        double altitude = location.getAltitude();
        float speed = location.getSpeed();
        float bearing = location.getBearing();
        float accuracy = location.getAccuracy();
        float verticalAccuracyMeters = location.getVerticalAccuracyMeters();
        float speedAccuracyMetersPerSecond = location.getSpeedAccuracyMetersPerSecond();
        float bearingAccuracyDegrees = location.getBearingAccuracyDegrees();
        long time = location.getTime();
        int i2 = location.hasElapsedRealtimeUncertaintyNanos() ? 2 : 0;
        this.mGnssHal.injectBestLocation(i, latitude, longitude, altitude, speed, bearing, accuracy, verticalAccuracyMeters, speedAccuracyMetersPerSecond, bearingAccuracyDegrees, time, i2 | 1, location.getElapsedRealtimeNanos(), location.getElapsedRealtimeUncertaintyNanos());
    }

    public void injectTime(long j, long j2, int i) {
        Preconditions.checkState(this.mRegistered);
        this.mGnssHal.injectTime(j, j2, i);
    }

    public boolean isNavigationMessageCollectionSupported() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.isNavigationMessageCollectionSupported();
    }

    public boolean startNavigationMessageCollection() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.startNavigationMessageCollection();
    }

    public boolean stopNavigationMessageCollection() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.stopNavigationMessageCollection();
    }

    public boolean isAntennaInfoSupported() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.isAntennaInfoSupported();
    }

    public boolean startAntennaInfoListening() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.startAntennaInfoListening();
    }

    public boolean stopAntennaInfoListening() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.stopAntennaInfoListening();
    }

    public boolean isMeasurementSupported() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.isMeasurementSupported();
    }

    public boolean startMeasurementCollection(boolean z, boolean z2, int i) {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.startMeasurementCollection(z, z2, i);
    }

    public boolean stopMeasurementCollection() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.stopMeasurementCollection();
    }

    public boolean startSvStatusCollection() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.startSvStatusCollection();
    }

    public boolean stopSvStatusCollection() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.stopSvStatusCollection();
    }

    public boolean startNmeaMessageCollection() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.startNmeaMessageCollection();
    }

    public boolean stopNmeaMessageCollection() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.stopNmeaMessageCollection();
    }

    public boolean isMeasurementCorrectionsSupported() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.isMeasurementCorrectionsSupported();
    }

    public boolean injectMeasurementCorrections(GnssMeasurementCorrections gnssMeasurementCorrections) {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.injectMeasurementCorrections(gnssMeasurementCorrections);
    }

    public boolean initBatching() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.initBatching();
    }

    public void cleanupBatching() {
        Preconditions.checkState(this.mRegistered);
        this.mGnssHal.cleanupBatching();
    }

    public boolean startBatch(long j, float f, boolean z) {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.startBatch(j, f, z);
    }

    public void flushBatch() {
        Preconditions.checkState(this.mRegistered);
        this.mGnssHal.flushBatch();
    }

    public void stopBatch() {
        Preconditions.checkState(this.mRegistered);
        this.mGnssHal.stopBatch();
    }

    public int getBatchSize() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.getBatchSize();
    }

    public boolean isGeofencingSupported() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.isGeofencingSupported();
    }

    public boolean addGeofence(int i, double d, double d2, double d3, int i2, int i3, int i4, int i5) {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.addGeofence(i, d, d2, d3, i2, i3, i4, i5);
    }

    public boolean resumeGeofence(int i, int i2) {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.resumeGeofence(i, i2);
    }

    public boolean pauseGeofence(int i) {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.pauseGeofence(i);
    }

    public boolean removeGeofence(int i) {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.removeGeofence(i);
    }

    public boolean isGnssVisibilityControlSupported() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.isGnssVisibilityControlSupported();
    }

    public void sendNiResponse(int i, int i2) {
        Preconditions.checkState(this.mRegistered);
        this.mGnssHal.sendNiResponse(i, i2);
    }

    public void requestPowerStats() {
        Preconditions.checkState(this.mRegistered);
        this.mGnssHal.requestPowerStats();
    }

    public void setAgpsServer(int i, String str, int i2) {
        Preconditions.checkState(this.mRegistered);
        this.mGnssHal.setAgpsServer(i, str, i2);
    }

    public void setAgpsSetId(int i, String str) {
        Preconditions.checkState(this.mRegistered);
        this.mGnssHal.setAgpsSetId(i, str);
    }

    public void setAgpsReferenceLocationCellId(int i, int i2, int i3, int i4, long j, int i5, int i6, int i7) {
        Preconditions.checkState(this.mRegistered);
        this.mGnssHal.setAgpsReferenceLocationCellId(i, i2, i3, i4, j, i5, i6, i7);
    }

    public boolean isPsdsSupported() {
        Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.isPsdsSupported();
    }

    public void injectPsdsData(byte[] bArr, int i, int i2) {
        Preconditions.checkState(this.mRegistered);
        this.mGnssHal.injectPsdsData(bArr, i, i2);
    }

    public void injectNiSuplMessageData(byte[] bArr, int i, int i2) {
        Preconditions.checkState(this.mRegistered);
        this.mGnssHal.injectNiSuplMessageData(bArr, i, i2);
    }

    void reportGnssServiceDied() {
        Log.e(GnssManagerService.TAG, "gnss hal died - restarting shortly...");
        FgThread.getExecutor().execute(new Runnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                GnssNative.this.restartHal();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void restartHal() {
        initializeGnss(true);
        Log.e(GnssManagerService.TAG, "gnss hal restarted");
        int i = 0;
        while (true) {
            BaseCallbacks[] baseCallbacksArr = this.mBaseCallbacks;
            if (i >= baseCallbacksArr.length) {
                return;
            }
            baseCallbacksArr[i].onHalRestarted();
            i++;
        }
    }

    void reportLocation(final boolean z, final Location location) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda1
            public final void runOrThrow() {
                GnssNative.this.lambda$reportLocation$0(z, location);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportLocation$0(boolean z, Location location) throws Exception {
        int i = 0;
        if (z && !this.mHasFirstFix) {
            this.mHasFirstFix = true;
            int elapsedRealtime = (int) (SystemClock.elapsedRealtime() - this.mStartRealtimeMs);
            int i2 = 0;
            while (true) {
                StatusCallbacks[] statusCallbacksArr = this.mStatusCallbacks;
                if (i2 >= statusCallbacksArr.length) {
                    break;
                }
                statusCallbacksArr[i2].onReportFirstFix(elapsedRealtime);
                i2++;
            }
        }
        if (location.hasSpeed()) {
            boolean z2 = location.getSpeed() > ITAR_SPEED_LIMIT_METERS_PER_SECOND;
            if (!this.mItarSpeedLimitExceeded && z2) {
                Log.w(GnssManagerService.TAG, "speed nearing ITAR threshold - blocking further GNSS output");
            } else if (this.mItarSpeedLimitExceeded && !z2) {
                Log.w(GnssManagerService.TAG, "speed leaving ITAR threshold - allowing further GNSS output");
            }
            this.mItarSpeedLimitExceeded = z2;
        }
        if (this.mItarSpeedLimitExceeded) {
            return;
        }
        while (true) {
            LocationCallbacks[] locationCallbacksArr = this.mLocationCallbacks;
            if (i >= locationCallbacksArr.length) {
                return;
            }
            locationCallbacksArr[i].onReportLocation(z, location);
            i++;
        }
    }

    void reportStatus(final int i) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda21
            public final void runOrThrow() {
                GnssNative.this.lambda$reportStatus$1(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportStatus$1(int i) throws Exception {
        int i2 = 0;
        while (true) {
            StatusCallbacks[] statusCallbacksArr = this.mStatusCallbacks;
            if (i2 >= statusCallbacksArr.length) {
                return;
            }
            statusCallbacksArr[i2].onReportStatus(i);
            i2++;
        }
    }

    void reportSvStatus(final int i, final int[] iArr, final float[] fArr, final float[] fArr2, final float[] fArr3, final float[] fArr4, final float[] fArr5) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda19
            public final void runOrThrow() {
                GnssNative.this.lambda$reportSvStatus$2(i, iArr, fArr, fArr2, fArr3, fArr4, fArr5);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportSvStatus$2(int i, int[] iArr, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4, float[] fArr5) throws Exception {
        GnssStatus wrap = GnssStatus.wrap(i, iArr, fArr, fArr2, fArr3, fArr4, fArr5);
        IOplusLBSMainClass iOplusLBSMainClass = this.mOplusLbsClass;
        if (iOplusLBSMainClass != null) {
            wrap = iOplusLBSMainClass.onGnssSvStrategy(wrap);
        } else {
            this.mOplusLbsClass = (IOplusLBSMainClass) OplusLbsFactory.getInstance().getFeature(IOplusLBSMainClass.DEFAULT, null);
        }
        int i2 = 0;
        while (true) {
            SvStatusCallbacks[] svStatusCallbacksArr = this.mSvStatusCallbacks;
            if (i2 >= svStatusCallbacksArr.length) {
                return;
            }
            svStatusCallbacksArr[i2].onReportSvStatus(wrap);
            i2++;
        }
    }

    void reportAGpsStatus(final int i, final int i2, final byte[] bArr) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda17
            public final void runOrThrow() {
                GnssNative.this.lambda$reportAGpsStatus$3(i, i2, bArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportAGpsStatus$3(int i, int i2, byte[] bArr) throws Exception {
        this.mAGpsCallbacks.onReportAGpsStatus(i, i2, bArr);
    }

    void reportNmea(final long j) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda6
            public final void runOrThrow() {
                GnssNative.this.lambda$reportNmea$4(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportNmea$4(long j) throws Exception {
        if (this.mItarSpeedLimitExceeded) {
            return;
        }
        int i = 0;
        while (true) {
            NmeaCallbacks[] nmeaCallbacksArr = this.mNmeaCallbacks;
            if (i >= nmeaCallbacksArr.length) {
                return;
            }
            nmeaCallbacksArr[i].onReportNmea(j);
            i++;
        }
    }

    void reportMeasurementData(final GnssMeasurementsEvent gnssMeasurementsEvent) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda23
            public final void runOrThrow() {
                GnssNative.this.lambda$reportMeasurementData$5(gnssMeasurementsEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportMeasurementData$5(GnssMeasurementsEvent gnssMeasurementsEvent) throws Exception {
        if (this.mItarSpeedLimitExceeded) {
            return;
        }
        int i = 0;
        while (true) {
            MeasurementCallbacks[] measurementCallbacksArr = this.mMeasurementCallbacks;
            if (i >= measurementCallbacksArr.length) {
                return;
            }
            measurementCallbacksArr[i].onReportMeasurements(gnssMeasurementsEvent);
            i++;
        }
    }

    void reportAntennaInfo(final List<GnssAntennaInfo> list) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda10
            public final void runOrThrow() {
                GnssNative.this.lambda$reportAntennaInfo$6(list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportAntennaInfo$6(List list) throws Exception {
        int i = 0;
        while (true) {
            AntennaInfoCallbacks[] antennaInfoCallbacksArr = this.mAntennaInfoCallbacks;
            if (i >= antennaInfoCallbacksArr.length) {
                return;
            }
            antennaInfoCallbacksArr[i].onReportAntennaInfo(list);
            i++;
        }
    }

    void reportNavigationMessage(final GnssNavigationMessage gnssNavigationMessage) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda2
            public final void runOrThrow() {
                GnssNative.this.lambda$reportNavigationMessage$7(gnssNavigationMessage);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportNavigationMessage$7(GnssNavigationMessage gnssNavigationMessage) throws Exception {
        if (this.mItarSpeedLimitExceeded) {
            return;
        }
        int i = 0;
        while (true) {
            NavigationMessageCallbacks[] navigationMessageCallbacksArr = this.mNavigationMessageCallbacks;
            if (i >= navigationMessageCallbacksArr.length) {
                return;
            }
            navigationMessageCallbacksArr[i].onReportNavigationMessage(gnssNavigationMessage);
            i++;
        }
    }

    void setTopHalCapabilities(int i, boolean z) {
        int i2 = i | this.mTopFlags;
        this.mTopFlags = i2;
        GnssCapabilities gnssCapabilities = this.mCapabilities;
        GnssCapabilities withTopHalFlags = gnssCapabilities.withTopHalFlags(i2, z);
        this.mCapabilities = withTopHalFlags;
        onCapabilitiesChanged(gnssCapabilities, withTopHalFlags);
    }

    void setSubHalMeasurementCorrectionsCapabilities(int i) {
        GnssCapabilities gnssCapabilities = this.mCapabilities;
        GnssCapabilities withSubHalMeasurementCorrectionsFlags = gnssCapabilities.withSubHalMeasurementCorrectionsFlags(i);
        this.mCapabilities = withSubHalMeasurementCorrectionsFlags;
        onCapabilitiesChanged(gnssCapabilities, withSubHalMeasurementCorrectionsFlags);
    }

    void setSubHalPowerIndicationCapabilities(int i) {
        GnssCapabilities gnssCapabilities = this.mCapabilities;
        GnssCapabilities withSubHalPowerFlags = gnssCapabilities.withSubHalPowerFlags(i);
        this.mCapabilities = withSubHalPowerFlags;
        onCapabilitiesChanged(gnssCapabilities, withSubHalPowerFlags);
    }

    void setSignalTypeCapabilities(List<GnssSignalType> list) {
        GnssCapabilities gnssCapabilities = this.mCapabilities;
        GnssCapabilities withSignalTypes = gnssCapabilities.withSignalTypes(list);
        this.mCapabilities = withSignalTypes;
        onCapabilitiesChanged(gnssCapabilities, withSignalTypes);
    }

    private void onCapabilitiesChanged(final GnssCapabilities gnssCapabilities, final GnssCapabilities gnssCapabilities2) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda14
            public final void runOrThrow() {
                GnssNative.this.lambda$onCapabilitiesChanged$8(gnssCapabilities2, gnssCapabilities);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCapabilitiesChanged$8(GnssCapabilities gnssCapabilities, GnssCapabilities gnssCapabilities2) throws Exception {
        if (gnssCapabilities.equals(gnssCapabilities2)) {
            return;
        }
        Log.i(GnssManagerService.TAG, "gnss capabilities changed to " + gnssCapabilities);
        int i = 0;
        while (true) {
            BaseCallbacks[] baseCallbacksArr = this.mBaseCallbacks;
            if (i >= baseCallbacksArr.length) {
                return;
            }
            baseCallbacksArr[i].onCapabilitiesChanged(gnssCapabilities2, gnssCapabilities);
            i++;
        }
    }

    void reportGnssPowerStats(GnssPowerStats gnssPowerStats) {
        this.mPowerStats = gnssPowerStats;
    }

    void setGnssYearOfHardware(int i) {
        this.mHardwareYear = i;
    }

    private void setGnssHardwareModelName(String str) {
        this.mHardwareModelName = str;
    }

    void reportLocationBatch(final Location[] locationArr) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda18
            public final void runOrThrow() {
                GnssNative.this.lambda$reportLocationBatch$9(locationArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportLocationBatch$9(Location[] locationArr) throws Exception {
        int i = 0;
        while (true) {
            LocationCallbacks[] locationCallbacksArr = this.mLocationCallbacks;
            if (i >= locationCallbacksArr.length) {
                return;
            }
            locationCallbacksArr[i].onReportLocations(locationArr);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$psdsDownloadRequest$10(int i) throws Exception {
        this.mPsdsCallbacks.onRequestPsdsDownload(i);
    }

    void psdsDownloadRequest(final int i) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda22
            public final void runOrThrow() {
                GnssNative.this.lambda$psdsDownloadRequest$10(i);
            }
        });
    }

    void reportGeofenceTransition(final int i, final Location location, final int i2, final long j) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda0
            public final void runOrThrow() {
                GnssNative.this.lambda$reportGeofenceTransition$11(i, location, i2, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportGeofenceTransition$11(int i, Location location, int i2, long j) throws Exception {
        this.mGeofenceCallbacks.onReportGeofenceTransition(i, location, i2, j);
    }

    void reportGeofenceStatus(final int i, final Location location) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda15
            public final void runOrThrow() {
                GnssNative.this.lambda$reportGeofenceStatus$12(i, location);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportGeofenceStatus$12(int i, Location location) throws Exception {
        this.mGeofenceCallbacks.onReportGeofenceStatus(i, location);
    }

    void reportGeofenceAddStatus(final int i, final int i2) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda11
            public final void runOrThrow() {
                GnssNative.this.lambda$reportGeofenceAddStatus$13(i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportGeofenceAddStatus$13(int i, int i2) throws Exception {
        this.mGeofenceCallbacks.onReportGeofenceAddStatus(i, i2);
    }

    void reportGeofenceRemoveStatus(final int i, final int i2) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda12
            public final void runOrThrow() {
                GnssNative.this.lambda$reportGeofenceRemoveStatus$14(i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportGeofenceRemoveStatus$14(int i, int i2) throws Exception {
        this.mGeofenceCallbacks.onReportGeofenceRemoveStatus(i, i2);
    }

    void reportGeofencePauseStatus(final int i, final int i2) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda13
            public final void runOrThrow() {
                GnssNative.this.lambda$reportGeofencePauseStatus$15(i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportGeofencePauseStatus$15(int i, int i2) throws Exception {
        this.mGeofenceCallbacks.onReportGeofencePauseStatus(i, i2);
    }

    void reportGeofenceResumeStatus(final int i, final int i2) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda4
            public final void runOrThrow() {
                GnssNative.this.lambda$reportGeofenceResumeStatus$16(i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportGeofenceResumeStatus$16(int i, int i2) throws Exception {
        this.mGeofenceCallbacks.onReportGeofenceResumeStatus(i, i2);
    }

    void reportNiNotification(final int i, final int i2, final int i3, final int i4, final int i5, final String str, final String str2, final int i6, final int i7) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda24
            public final void runOrThrow() {
                GnssNative.this.lambda$reportNiNotification$17(i, i2, i3, i4, i5, str, str2, i6, i7);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportNiNotification$17(int i, int i2, int i3, int i4, int i5, String str, String str2, int i6, int i7) throws Exception {
        this.mNotificationCallbacks.onReportNiNotification(i, i2, i3, i4, i5, str, str2, i6, i7);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestSetID$18(int i) throws Exception {
        this.mAGpsCallbacks.onRequestSetID(i);
    }

    void requestSetID(final int i) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda8
            public final void runOrThrow() {
                GnssNative.this.lambda$requestSetID$18(i);
            }
        });
    }

    void requestLocation(final boolean z, final boolean z2) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda20
            public final void runOrThrow() {
                GnssNative.this.lambda$requestLocation$19(z, z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestLocation$19(boolean z, boolean z2) throws Exception {
        this.mLocationRequestCallbacks.onRequestLocation(z, z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestUtcTime$20() throws Exception {
        this.mTimeCallbacks.onRequestUtcTime();
    }

    void requestUtcTime() {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda3
            public final void runOrThrow() {
                GnssNative.this.lambda$requestUtcTime$20();
            }
        });
    }

    void requestRefLocation() {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda9
            public final void runOrThrow() {
                GnssNative.this.lambda$requestRefLocation$21();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestRefLocation$21() throws Exception {
        this.mLocationRequestCallbacks.onRequestRefLocation();
    }

    void reportNfwNotification(final String str, final byte b, final String str2, final byte b2, final String str3, final byte b3, final boolean z, final boolean z2) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda7
            public final void runOrThrow() {
                GnssNative.this.lambda$reportNfwNotification$22(str, b, str2, b2, str3, b3, z, z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportNfwNotification$22(String str, byte b, String str2, byte b2, String str3, byte b3, boolean z, boolean z2) throws Exception {
        this.mNotificationCallbacks.onReportNfwNotification(str, b, str2, b2, str3, b3, z, z2);
    }

    public boolean isInEmergencySession() {
        return ((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda16
            public final Object getOrThrow() {
                Boolean lambda$isInEmergencySession$23;
                lambda$isInEmergencySession$23 = GnssNative.this.lambda$isInEmergencySession$23();
                return lambda$isInEmergencySession$23;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$isInEmergencySession$23() throws Exception {
        return Boolean.valueOf(this.mEmergencyHelper.isInEmergency(TimeUnit.SECONDS.toMillis(this.mConfiguration.getEsExtensionSec())));
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class GnssHal {
        protected GnssHal() {
        }

        protected void classInitOnce() {
            GnssNative.native_class_init_once();
        }

        protected boolean isSupported() {
            return GnssNative.m906$$Nest$smnative_is_supported();
        }

        protected void initOnce(GnssNative gnssNative, boolean z) {
            gnssNative.native_init_once(z);
        }

        protected boolean init() {
            return GnssNative.m892$$Nest$smnative_init();
        }

        protected void cleanup() {
            GnssNative.native_cleanup();
        }

        protected boolean start() {
            return GnssNative.m915$$Nest$smnative_start();
        }

        protected boolean stop() {
            return GnssNative.m922$$Nest$smnative_stop();
        }

        protected boolean setPositionMode(int i, int i2, int i3, int i4, int i5, boolean z) {
            return GnssNative.native_set_position_mode(i, i2, i3, i4, i5, z);
        }

        protected String getInternalState() {
            return GnssNative.m891$$Nest$smnative_get_internal_state();
        }

        protected void deleteAidingData(int i) {
            GnssNative.native_delete_aiding_data(i);
        }

        protected int readNmea(byte[] bArr, int i) {
            return GnssNative.native_read_nmea(bArr, i);
        }

        protected void injectLocation(int i, double d, double d2, double d3, float f, float f2, float f3, float f4, float f5, float f6, long j, int i2, long j2, double d4) {
            GnssNative.native_inject_location(i, d, d2, d3, f, f2, f3, f4, f5, f6, j, i2, j2, d4);
        }

        protected void injectBestLocation(int i, double d, double d2, double d3, float f, float f2, float f3, float f4, float f5, float f6, long j, int i2, long j2, double d4) {
            GnssNative.native_inject_best_location(i, d, d2, d3, f, f2, f3, f4, f5, f6, j, i2, j2, d4);
        }

        protected void injectTime(long j, long j2, int i) {
            GnssNative.native_inject_time(j, j2, i);
        }

        protected boolean isNavigationMessageCollectionSupported() {
            return GnssNative.m905$$Nest$smnative_is_navigation_message_supported();
        }

        protected boolean startNavigationMessageCollection() {
            return GnssNative.m919$$Nest$smnative_start_navigation_message_collection();
        }

        protected boolean stopNavigationMessageCollection() {
            return GnssNative.m926$$Nest$smnative_stop_navigation_message_collection();
        }

        protected boolean isAntennaInfoSupported() {
            return GnssNative.m900$$Nest$smnative_is_antenna_info_supported();
        }

        protected boolean startAntennaInfoListening() {
            return GnssNative.m916$$Nest$smnative_start_antenna_info_listening();
        }

        protected boolean stopAntennaInfoListening() {
            return GnssNative.m923$$Nest$smnative_stop_antenna_info_listening();
        }

        protected boolean isMeasurementSupported() {
            return GnssNative.m904$$Nest$smnative_is_measurement_supported();
        }

        protected boolean startMeasurementCollection(boolean z, boolean z2, int i) {
            return GnssNative.native_start_measurement_collection(z, z2, i);
        }

        protected boolean stopMeasurementCollection() {
            return GnssNative.m925$$Nest$smnative_stop_measurement_collection();
        }

        protected boolean isMeasurementCorrectionsSupported() {
            return GnssNative.m903$$Nest$smnative_is_measurement_corrections_supported();
        }

        protected boolean injectMeasurementCorrections(GnssMeasurementCorrections gnssMeasurementCorrections) {
            return GnssNative.native_inject_measurement_corrections(gnssMeasurementCorrections);
        }

        protected boolean startSvStatusCollection() {
            return GnssNative.m921$$Nest$smnative_start_sv_status_collection();
        }

        protected boolean stopSvStatusCollection() {
            return GnssNative.m928$$Nest$smnative_stop_sv_status_collection();
        }

        protected boolean startNmeaMessageCollection() {
            return GnssNative.m920$$Nest$smnative_start_nmea_message_collection();
        }

        protected boolean stopNmeaMessageCollection() {
            return GnssNative.m927$$Nest$smnative_stop_nmea_message_collection();
        }

        protected int getBatchSize() {
            return GnssNative.m890$$Nest$smnative_get_batch_size();
        }

        protected boolean initBatching() {
            return GnssNative.m893$$Nest$smnative_init_batching();
        }

        protected void cleanupBatching() {
            GnssNative.native_cleanup_batching();
        }

        protected boolean startBatch(long j, float f, boolean z) {
            return GnssNative.native_start_batch(j, f, z);
        }

        protected void flushBatch() {
            GnssNative.native_flush_batch();
        }

        protected void stopBatch() {
            GnssNative.m924$$Nest$smnative_stop_batch();
        }

        protected boolean isGeofencingSupported() {
            return GnssNative.m901$$Nest$smnative_is_geofence_supported();
        }

        protected boolean addGeofence(int i, double d, double d2, double d3, int i2, int i3, int i4, int i5) {
            return GnssNative.native_add_geofence(i, d, d2, d3, i2, i3, i4, i5);
        }

        protected boolean resumeGeofence(int i, int i2) {
            return GnssNative.native_resume_geofence(i, i2);
        }

        protected boolean pauseGeofence(int i) {
            return GnssNative.native_pause_geofence(i);
        }

        protected boolean removeGeofence(int i) {
            return GnssNative.native_remove_geofence(i);
        }

        protected boolean isGnssVisibilityControlSupported() {
            return GnssNative.m902$$Nest$smnative_is_gnss_visibility_control_supported();
        }

        protected void sendNiResponse(int i, int i2) {
            GnssNative.native_send_ni_response(i, i2);
        }

        protected void requestPowerStats() {
            GnssNative.native_request_power_stats();
        }

        protected void setAgpsServer(int i, String str, int i2) {
            GnssNative.native_set_agps_server(i, str, i2);
        }

        protected void setAgpsSetId(int i, String str) {
            GnssNative.native_agps_set_id(i, str);
        }

        protected void setAgpsReferenceLocationCellId(int i, int i2, int i3, int i4, long j, int i5, int i6, int i7) {
            GnssNative.native_agps_set_ref_location_cellid(i, i2, i3, i4, j, i5, i6, i7);
        }

        protected boolean isPsdsSupported() {
            return GnssNative.m929$$Nest$smnative_supports_psds();
        }

        protected void injectPsdsData(byte[] bArr, int i, int i2) {
            GnssNative.native_inject_psds_data(bArr, i, i2);
        }

        protected void injectNiSuplMessageData(byte[] bArr, int i, int i2) {
            GnssNative.native_inject_ni_supl_message_data(bArr, i, i2);
        }
    }
}
