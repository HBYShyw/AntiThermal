package com.android.server.location.interfaces;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.location.GeocoderParams;
import android.location.GnssStatus;
import android.location.IGeocodeListener;
import android.location.LastLocationRequest;
import android.location.Location;
import android.location.LocationRequest;
import android.location.LocationResult;
import android.location.provider.ProviderRequest;
import android.location.util.identity.CallerIdentity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.WorkSource;
import com.android.server.FgThread;
import com.android.server.location.LocationManagerService;
import com.android.server.location.common.IOplusCommonFeature;
import com.android.server.location.common.OplusLbsFeatureList;
import com.android.server.location.gnss.GnssLocationProvider;
import com.android.server.location.gnss.GnssMeasurementsProvider;
import com.android.server.location.provider.AbstractLocationProvider;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IOplusLBSMainClass extends IOplusCommonFeature {
    public static final IOplusLBSMainClass DEFAULT = new IOplusLBSMainClass() { // from class: com.android.server.location.interfaces.IOplusLBSMainClass.1
    };
    public static final int DEFAULT_GEOCODER_TASK_CANCEL_TIME = 30000;
    public static final String Name = "IOplusLBSMainClass";

    default Location addCoarseLocationExtra(Location location) {
        return location;
    }

    default LocationResult addCoarseLocationExtra(LocationResult locationResult) {
        return locationResult;
    }

    @Deprecated
    default boolean checkDumpCommand(String[] strArr) {
        return false;
    }

    default boolean checkInHighFreqLocationBlacklist(String str, String str2) {
        return false;
    }

    default void checkLocationHasChanged(String str, String str2, int i) {
    }

    default boolean checkOpNoThrow(AppOpsManager appOpsManager, int i, CallerIdentity callerIdentity, long j) {
        return false;
    }

    default boolean checkRequestBlocked(String str, String str2) {
        return false;
    }

    default void collectLbsData(int i, Bundle bundle) {
    }

    default String collectPowerStatistics() {
        return "";
    }

    default void collectSvStatus(GnssStatus gnssStatus) {
    }

    default boolean dealDumpCommand(PrintWriter printWriter, String[] strArr) {
        return false;
    }

    default void dump(PrintWriter printWriter, String[] strArr) {
    }

    default void dumpOplusContent(PrintWriter printWriter) {
    }

    default void forceStopStatistics() {
    }

    default int generateStatusChangedExtra(String str, String str2, Bundle bundle, int i) {
        return 0;
    }

    default boolean geocoderIsPresent() {
        return false;
    }

    default void getAppInfoForTr(String str, String str2, int i, String str3) {
    }

    default int getFlpResId(String str) {
        return -1;
    }

    default void getFromLocation(double d, double d2, int i, GeocoderParams geocoderParams, IGeocodeListener iGeocodeListener) {
    }

    default void getFromLocationName(String str, double d, double d2, double d3, double d4, int i, GeocoderParams geocoderParams, IGeocodeListener iGeocodeListener) {
    }

    default int getGeoTaskCancelTimeMs() {
        return 30000;
    }

    default boolean getInPowerSaveMode() {
        return false;
    }

    default List<String> getInUsePackagesList() {
        return null;
    }

    default Location getLastLocation(Location location, LastLocationRequest lastLocationRequest, int i) {
        return null;
    }

    default AbstractLocationProvider getLocationProvider() {
        return null;
    }

    default int getNavigateMode() {
        return -1;
    }

    default String getNlpId() {
        return null;
    }

    default boolean getOplusLocationMode(int i) {
        return true;
    }

    default void getProviderStatus(String str, boolean z, boolean z2, boolean z3, int i) {
    }

    default int getRec() {
        return -1;
    }

    default boolean handleCommand(String str, String str2, Bundle bundle) {
        return false;
    }

    default void handleLocationChanged(LocationResult locationResult, boolean z) {
    }

    default boolean ignoreDisabled(String str, boolean z) {
        return false;
    }

    default void incomingNewGpsUsingApp(String str, String str2) {
    }

    default void initFlpCoordinator(Context context) {
    }

    default void initGnssPowerSaver(GnssLocationProvider gnssLocationProvider) {
    }

    default void initLocationStatusMonitor(Context context) {
    }

    default void initOplusNlp() {
    }

    default boolean isAllowedChangeChipData(String str, String str2) {
        return false;
    }

    default boolean isAllowedPassLocationAccess(String str) {
        return false;
    }

    default boolean isFlpReqLimited(String str) {
        return false;
    }

    default boolean isForceAgpsEnabled(boolean z) {
        return true;
    }

    default boolean isForceGnssDisabled() {
        return false;
    }

    default boolean isForegroundActivity(int i) {
        return false;
    }

    default boolean isForegroundActivity(String str) {
        return false;
    }

    default boolean isGpsEnableForSpecialApp(String str, int i, String str2) {
        return false;
    }

    default boolean isMetalCaseDetectEnabled() {
        return false;
    }

    default boolean isPackageBlocked(String str, String str2) {
        return false;
    }

    default boolean isPdrActive() {
        return false;
    }

    default boolean isPreciseLocationSupported() {
        return false;
    }

    default boolean isStealthSecurity() {
        return false;
    }

    default void listenEmergencyCallStatus() {
    }

    default void locationStatisticsInit(Context context) {
    }

    default boolean logoutLbsConfigListener(IOplusConfigListener iOplusConfigListener) {
        return false;
    }

    default boolean needChangeNotifyStatus(String str, boolean z) {
        return false;
    }

    default void onActiveDataSubscriptionIdChanged() {
    }

    default void onAddMockProvider(String str, String str2) {
    }

    default void onFirstFix(int i) {
    }

    default void onGnssLocationProviderInit(Context context, GnssLocationProvider gnssLocationProvider) {
    }

    default void onGnssMeasurementsProviderInit(GnssMeasurementsProvider gnssMeasurementsProvider) {
    }

    default GnssStatus onGnssSvStrategy(GnssStatus gnssStatus) {
        return gnssStatus;
    }

    default void onRemoveMockProvider(String str, String str2) {
    }

    default void onSetRequest(ProviderRequest providerRequest) {
    }

    default void onStartNavigating(int i) {
    }

    default void onStatusChanged(boolean z) {
    }

    default void onStopNavigating() {
    }

    default void onSvStatusChanged(GnssStatus gnssStatus) {
    }

    default boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        return false;
    }

    default void oplusSystemReady(LocationManagerService locationManagerService) {
    }

    default void oplusSystemThirdPartyAppsCanStart() {
    }

    @Deprecated
    default boolean powerSaveEnabled() {
        return false;
    }

    default void receiveSvInfo(GnssStatus gnssStatus) {
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

    default void recordLocationBlocked(String str) {
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

    default void recordPackagesLocationStatus(String str, int i, int i2, String str2) {
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

    default Location reduceAccuracyOfLocation(Location location) {
        return location;
    }

    default String reduceAccuracyOfNmeaSentences(String str) {
        return str;
    }

    default void refreshRequestTimer() {
    }

    default boolean registerLbsConfigListener(IOplusConfigListener iOplusConfigListener) {
        return false;
    }

    default boolean registerLocationListener(LocationRequest locationRequest, CallerIdentity callerIdentity, int i) {
        return true;
    }

    default boolean registerLocationListener(String str, CallerIdentity callerIdentity, int i) {
        return true;
    }

    default boolean releaseTaskMark(String str) {
        return false;
    }

    default void removePackagesLocationStatus(String str, int i, int i2, String str2) {
    }

    default void removingGpsUsingApp(String str, String str2) {
    }

    default void resetPowerStatistics() {
    }

    default boolean resistStartGps() {
        return false;
    }

    default boolean sendExtraCommand(String str, String str2, Bundle bundle) {
        return true;
    }

    default void setDebug(boolean z) {
    }

    default void setGnssLocationProvider(GnssLocationProvider gnssLocationProvider) {
    }

    default void setGpsBackgroundFlag(String str, boolean z) {
    }

    default void setOlsPackageName(Intent intent) {
    }

    default void setUp() {
    }

    default boolean shouldReportFlpAsGps(Location location, String str) {
        return false;
    }

    default void startController() {
    }

    default void startFlpAiding() {
    }

    default void startPowerStatistics() {
    }

    default void startRecordMonitor() {
    }

    default void startRequesting(CallerIdentity callerIdentity, String str, LocationRequest locationRequest, boolean z, String str2) {
    }

    default void stopController() {
    }

    default void stopFlpAiding() {
    }

    default void stopLogCollect(int i) {
    }

    default void stopPowerStatistics() {
    }

    default void stopRecordMonitor() {
    }

    default void stopRequesting(CallerIdentity callerIdentity, String str, LocationRequest locationRequest, String str2) {
    }

    default void storeAppSvInfo(int i, float f) {
    }

    default void storeSatellitesInfo(int i, int i2, int i3) {
    }

    default void storeWorkSource(WorkSource workSource) {
    }

    default void triggerLogCollect(int i) {
    }

    default void updateBindStatus(boolean z) {
    }

    default void updateForeground(String str, String str2, boolean z) {
    }

    default void updateGpsWorksourceStatus(WorkSource workSource) {
    }

    default void updateSettings(String str, int i) {
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default OplusLbsFeatureList.OplusIndex index() {
        return OplusLbsFeatureList.OplusIndex.IOplusLBSMainClass;
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default HandlerThread getThread(int i) {
        return FgThread.get();
    }

    default Handler getHandler(int i) {
        return FgThread.getHandler();
    }

    default Executor getExecutor(int i) {
        return FgThread.getExecutor();
    }

    default List<String> getNfwProxyApps(List<String> list) {
        return new ArrayList();
    }
}
