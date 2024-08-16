package com.oplus.wrapper.location;

import android.app.PendingIntent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geofence;
import android.location.GnssAntennaInfo;
import android.location.GnssCapabilities;
import android.location.GnssMeasurementRequest;
import android.location.IGeocodeListener;
import android.location.IGnssAntennaInfoListener;
import android.location.IGnssMeasurementsListener;
import android.location.IGnssNavigationMessageListener;
import android.location.IGnssNmeaListener;
import android.location.IGnssStatusListener;
import android.location.ILocationCallback;
import android.location.ILocationListener;
import android.location.ILocationManager;
import android.location.LastLocationRequest;
import android.location.Location;
import android.location.LocationTime;
import android.location.provider.IProviderRequestListener;
import android.location.provider.ProviderProperties;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.IInterface;
import android.os.PackageTagsList;
import android.os.RemoteException;
import com.oplus.wrapper.location.IGeocodeListener;
import java.util.List;

/* loaded from: classes.dex */
public interface ILocationManager {
    void getFromLocation(double d, double d2, int i, GeocoderParams geocoderParams, IGeocodeListener iGeocodeListener) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, ILocationManager {
        private final android.location.ILocationManager mTarget = new ILocationManager.Stub() { // from class: com.oplus.wrapper.location.ILocationManager.Stub.1
            public Location getLastLocation(String s, LastLocationRequest lastLocationRequest, String s1, String s2) throws RemoteException {
                return null;
            }

            public ICancellationSignal getCurrentLocation(String s, android.location.LocationRequest locationRequest, ILocationCallback iLocationCallback, String s1, String s2, String s3) throws RemoteException {
                return null;
            }

            public void registerLocationListener(String s, android.location.LocationRequest locationRequest, ILocationListener iLocationListener, String s1, String s2, String s3) throws RemoteException {
            }

            public void unregisterLocationListener(ILocationListener iLocationListener) throws RemoteException {
            }

            public void registerLocationPendingIntent(String s, android.location.LocationRequest locationRequest, PendingIntent pendingIntent, String s1, String s2) throws RemoteException {
            }

            public void unregisterLocationPendingIntent(PendingIntent pendingIntent) throws RemoteException {
            }

            public void injectLocation(Location location) throws RemoteException {
            }

            public void requestListenerFlush(String s, ILocationListener iLocationListener, int i) throws RemoteException {
            }

            public void requestPendingIntentFlush(String s, PendingIntent pendingIntent, int i) throws RemoteException {
            }

            public void requestGeofence(Geofence geofence, PendingIntent pendingIntent, String s, String s1) throws RemoteException {
            }

            public void removeGeofence(PendingIntent pendingIntent) throws RemoteException {
            }

            public boolean geocoderIsPresent() throws RemoteException {
                return false;
            }

            public void getFromLocation(double v, double v1, int i, android.location.GeocoderParams geocoderParams, final android.location.IGeocodeListener iGeocodeListener) throws RemoteException {
                IGeocodeListener wrapperGeocodeListener = iGeocodeListener != null ? new IGeocodeListener.Stub() { // from class: com.oplus.wrapper.location.ILocationManager.Stub.1.1
                    @Override // com.oplus.wrapper.location.IGeocodeListener
                    public void onResults(String error, List<Address> results) throws RemoteException {
                        iGeocodeListener.onResults(error, results);
                    }
                } : null;
                Stub.this.getFromLocation(v, v1, i, new GeocoderParams(geocoderParams), wrapperGeocodeListener);
            }

            public void getFromLocationName(String s, double v, double v1, double v2, double v3, int i, android.location.GeocoderParams geocoderParams, android.location.IGeocodeListener iGeocodeListener) throws RemoteException {
            }

            public GnssCapabilities getGnssCapabilities() throws RemoteException {
                return null;
            }

            public int getGnssYearOfHardware() throws RemoteException {
                return 0;
            }

            public String getGnssHardwareModelName() throws RemoteException {
                return null;
            }

            public List<GnssAntennaInfo> getGnssAntennaInfos() throws RemoteException {
                return null;
            }

            public void registerGnssStatusCallback(IGnssStatusListener iGnssStatusListener, String s, String s1, String s2) throws RemoteException {
            }

            public void unregisterGnssStatusCallback(IGnssStatusListener iGnssStatusListener) throws RemoteException {
            }

            public void registerGnssNmeaCallback(IGnssNmeaListener iGnssNmeaListener, String s, String s1, String s2) throws RemoteException {
            }

            public void unregisterGnssNmeaCallback(IGnssNmeaListener iGnssNmeaListener) throws RemoteException {
            }

            public void addGnssMeasurementsListener(GnssMeasurementRequest gnssMeasurementRequest, IGnssMeasurementsListener iGnssMeasurementsListener, String s, String s1, String s2) throws RemoteException {
            }

            public void removeGnssMeasurementsListener(IGnssMeasurementsListener iGnssMeasurementsListener) throws RemoteException {
            }

            public void injectGnssMeasurementCorrections(android.location.GnssMeasurementCorrections gnssMeasurementCorrections) throws RemoteException {
            }

            public void addGnssNavigationMessageListener(IGnssNavigationMessageListener iGnssNavigationMessageListener, String s, String s1, String s2) throws RemoteException {
            }

            public void removeGnssNavigationMessageListener(IGnssNavigationMessageListener iGnssNavigationMessageListener) throws RemoteException {
            }

            public void addGnssAntennaInfoListener(IGnssAntennaInfoListener iGnssAntennaInfoListener, String s, String s1, String s2) throws RemoteException {
            }

            public void removeGnssAntennaInfoListener(IGnssAntennaInfoListener iGnssAntennaInfoListener) throws RemoteException {
            }

            public void addProviderRequestListener(IProviderRequestListener iProviderRequestListener) throws RemoteException {
            }

            public void removeProviderRequestListener(IProviderRequestListener iProviderRequestListener) throws RemoteException {
            }

            public int getGnssBatchSize() throws RemoteException {
                return 0;
            }

            public void startGnssBatch(long l, ILocationListener iLocationListener, String s, String s1, String s2) throws RemoteException {
            }

            public void flushGnssBatch() throws RemoteException {
            }

            public void stopGnssBatch() throws RemoteException {
            }

            public boolean hasProvider(String s) throws RemoteException {
                return false;
            }

            public List<String> getAllProviders() throws RemoteException {
                return null;
            }

            public List<String> getProviders(Criteria criteria, boolean b) throws RemoteException {
                return null;
            }

            public String getBestProvider(Criteria criteria, boolean b) throws RemoteException {
                return null;
            }

            public ProviderProperties getProviderProperties(String s) throws RemoteException {
                return null;
            }

            public boolean isProviderPackage(String s, String s1, String s2) throws RemoteException {
                return false;
            }

            public List<String> getProviderPackages(String s) throws RemoteException {
                return null;
            }

            public void setExtraLocationControllerPackage(String s) throws RemoteException {
            }

            public String getExtraLocationControllerPackage() throws RemoteException {
                return null;
            }

            public void setExtraLocationControllerPackageEnabled(boolean b) throws RemoteException {
            }

            public boolean isExtraLocationControllerPackageEnabled() throws RemoteException {
                return false;
            }

            public boolean isProviderEnabledForUser(String s, int i) throws RemoteException {
                return false;
            }

            public boolean isLocationEnabledForUser(int i) throws RemoteException {
                return false;
            }

            public void setLocationEnabledForUser(boolean b, int i) throws RemoteException {
            }

            public boolean isAdasGnssLocationEnabledForUser(int i) throws RemoteException {
                return false;
            }

            public void setAdasGnssLocationEnabledForUser(boolean b, int i) throws RemoteException {
            }

            public boolean isAutomotiveGnssSuspended() throws RemoteException {
                return false;
            }

            public void setAutomotiveGnssSuspended(boolean b) throws RemoteException {
            }

            public void addTestProvider(String s, ProviderProperties providerProperties, List<String> list, String s1, String s2) throws RemoteException {
            }

            public void removeTestProvider(String s, String s1, String s2) throws RemoteException {
            }

            public void setTestProviderLocation(String s, Location location, String s1, String s2) throws RemoteException {
            }

            public void setTestProviderEnabled(String s, boolean b, String s1, String s2) throws RemoteException {
            }

            public LocationTime getGnssTimeMillis() throws RemoteException {
                return null;
            }

            public void sendExtraCommand(String s, String s1, Bundle bundle) throws RemoteException {
            }

            public String[] getBackgroundThrottlingWhitelist() throws RemoteException {
                return new String[0];
            }

            public PackageTagsList getIgnoreSettingsAllowlist() throws RemoteException {
                return null;
            }

            public PackageTagsList getAdasAllowlist() throws RemoteException {
                return null;
            }
        };

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        public static ILocationManager asInterface(IBinder obj) {
            return new Proxy(ILocationManager.Stub.asInterface(obj));
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ILocationManager {
            private final android.location.ILocationManager mTarget;

            Proxy(android.location.ILocationManager target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.location.ILocationManager
            public void getFromLocation(double latitude, double longitude, int maxResults, GeocoderParams params, final IGeocodeListener listener) throws RemoteException {
                this.mTarget.getFromLocation(latitude, longitude, maxResults, params.get(), listener != null ? new IGeocodeListener.Stub() { // from class: com.oplus.wrapper.location.ILocationManager.Stub.Proxy.1
                    public void onResults(String error, List<Address> results) throws RemoteException {
                        listener.onResults(error, results);
                    }
                } : null);
            }
        }
    }
}
