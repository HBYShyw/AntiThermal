package com.android.server.location.altitude;

import android.content.Context;
import android.frameworks.location.altitude.AddMslAltitudeToLocationRequest;
import android.frameworks.location.altitude.AddMslAltitudeToLocationResponse;
import android.frameworks.location.altitude.IAltitudeService;
import android.location.Location;
import android.location.altitude.AltitudeConverter;
import android.os.IBinder;
import android.os.RemoteException;
import com.android.server.SystemService;
import java.io.IOException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AltitudeService extends IAltitudeService.Stub {
    private final AltitudeConverter mAltitudeConverter = new AltitudeConverter();
    private final Context mContext;

    public String getInterfaceHash() {
        return "763e0415cde10c922c590396b90bf622636470b1";
    }

    public int getInterfaceVersion() {
        return 1;
    }

    public AltitudeService(Context context) {
        this.mContext = context;
    }

    public AddMslAltitudeToLocationResponse addMslAltitudeToLocation(AddMslAltitudeToLocationRequest addMslAltitudeToLocationRequest) throws RemoteException {
        Location location = new Location("");
        location.setLatitude(addMslAltitudeToLocationRequest.latitudeDegrees);
        location.setLongitude(addMslAltitudeToLocationRequest.longitudeDegrees);
        location.setAltitude(addMslAltitudeToLocationRequest.altitudeMeters);
        location.setVerticalAccuracyMeters(addMslAltitudeToLocationRequest.verticalAccuracyMeters);
        try {
            this.mAltitudeConverter.addMslAltitudeToLocation(this.mContext, location);
            AddMslAltitudeToLocationResponse addMslAltitudeToLocationResponse = new AddMslAltitudeToLocationResponse();
            addMslAltitudeToLocationResponse.mslAltitudeMeters = location.getMslAltitudeMeters();
            addMslAltitudeToLocationResponse.mslAltitudeAccuracyMeters = location.getMslAltitudeAccuracyMeters();
            return addMslAltitudeToLocationResponse;
        } catch (IOException e) {
            throw new RemoteException(e);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Lifecycle extends SystemService {
        private static final String SERVICE_NAME = IAltitudeService.DESCRIPTOR + "/default";
        private AltitudeService mService;

        public Lifecycle(Context context) {
            super(context);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.location.altitude.AltitudeService, android.os.IBinder] */
        public void onStart() {
            ?? altitudeService = new AltitudeService(getContext());
            this.mService = altitudeService;
            publishBinderService(SERVICE_NAME, (IBinder) altitudeService);
        }
    }
}
