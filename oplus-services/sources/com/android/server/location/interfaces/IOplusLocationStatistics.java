package com.android.server.location.interfaces;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.location.util.identity.CallerIdentity;
import android.os.Bundle;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IOplusLocationStatistics extends IOplusCommonFeature {
    public static final IOplusLocationStatistics DEFAULT = new IOplusLocationStatistics() { // from class: com.android.server.location.interfaces.IOplusLocationStatistics.1
    };
    public static final int GEOCODER_ERROR_FAILED = 3;
    public static final int GEOCODER_ERROR_NO_RESULT = 2;
    public static final int GEOCODER_ERROR_NO_SERVICE = 1;
    public static final int GEOCODER_ERROR_REPEAT_REQUEST = 4;
    public static final int GEOCODER_ERROR_REQUEST_REJECTED = 5;
    public static final int GNSS_STRATEGY_OF_ACTIVITY_STOP = 301;
    public static final int GNSS_STRATEGY_OF_ACTIVITY_TOTAL = 3;
    public static final int GNSS_STRATEGY_OF_ACTIVITY_WIFI_STOP = 501;
    public static final int GNSS_STRATEGY_OF_ACTIVITY_WIFI_TOTAL = 5;
    public static final int GNSS_STRATEGY_OF_GPS_IN_DOOR = 302;
    public static final int GNSS_STRATEGY_OF_LIGHT_STILL_CONDITION = 2;
    public static final int GNSS_STRATEGY_OF_STILL_CONDITION = 1;
    public static final int GNSS_STRATEGY_OF_WIFI_INLIST = 402;
    public static final int GNSS_STRATEGY_OF_WIFI_STOP = 401;
    public static final int GNSS_STRATEGY_OF_WIFI_TOTAL = 4;
    public static final int NLP_ERROR_FAILED = 3;
    public static final int NLP_ERROR_NO_NETWORK = 5;
    public static final int NLP_ERROR_NO_RESPONSE = 2;
    public static final int NLP_ERROR_NO_SERVICE = 1;
    public static final int NLP_ERROR_REPEAT_REQUEST = 4;
    public static final String Name = "IOplusLocationStatistics";
    public static final int TYPE_CACHE_OPLUS = 1;
    public static final int TYPE_CACHE_ORIGIN = 0;
    public static final int TYPE_CACHE_UNREPORTED = 2;

    default String collectPowerStatistics() {
        return "";
    }

    default void forceStopStatistics() {
    }

    default boolean handleCommand(String str, String str2, Bundle bundle) {
        return false;
    }

    default void recordCacheReported(int i, boolean z, long j, int i2) {
    }

    default void recordGeocoderError(int i) {
    }

    default void recordGeocoderRequestStarted() {
    }

    default void recordGeocoderRequestStopped(long j) {
    }

    default void recordGnssNavigatingStarted(long j) {
    }

    default void recordGnssNavigatingStopped() {
    }

    default void recordGnssPowerSaveStarted(int i) {
    }

    default void recordGnssPowerSaveStopped(int i) {
    }

    default void recordHeldWakelock(String str) {
    }

    default void recordMtkMetalCaseStat(String str) {
    }

    default void recordNlpError(int i) {
    }

    default void recordNlpNavigatingStarted() {
    }

    default void recordNlpNavigatingStopped() {
    }

    default void recordNlpScanWifiSucceed(String str) {
    }

    default void recordNlpScanWifiTotal(String str) {
    }

    default void recordOplusCacheRequestNlp() {
    }

    default void recordReleaseWakelock(String str) {
    }

    default void recordRgcError(int i) {
    }

    default void recordRgcRequestStarted() {
    }

    default void recordRgcRequestStopped(long j) {
    }

    default boolean recordTaskMark(String str) {
        return false;
    }

    default boolean recordTaskTime(String str, Long l) {
        return false;
    }

    default boolean releaseTaskMark(String str) {
        return false;
    }

    default void resetPowerStatistics() {
    }

    default void startPowerStatistics() {
    }

    default void startRequesting(CallerIdentity callerIdentity, String str, long j, boolean z, String str2) {
    }

    default void stopPowerStatistics() {
    }

    default void stopRequesting(CallerIdentity callerIdentity, String str, long j, String str2) {
    }

    default void updateForeground(CallerIdentity callerIdentity, String str, boolean z) {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusLocationStatistics;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
