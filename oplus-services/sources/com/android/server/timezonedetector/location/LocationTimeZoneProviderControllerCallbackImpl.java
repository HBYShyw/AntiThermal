package com.android.server.timezonedetector.location;

import com.android.server.LocalServices;
import com.android.server.timezonedetector.LocationAlgorithmEvent;
import com.android.server.timezonedetector.TimeZoneDetectorInternal;
import com.android.server.timezonedetector.location.LocationTimeZoneProviderController;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LocationTimeZoneProviderControllerCallbackImpl extends LocationTimeZoneProviderController.Callback {
    /* JADX INFO: Access modifiers changed from: package-private */
    public LocationTimeZoneProviderControllerCallbackImpl(ThreadingDomain threadingDomain) {
        super(threadingDomain);
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Callback
    void sendEvent(LocationAlgorithmEvent locationAlgorithmEvent) {
        this.mThreadingDomain.assertCurrentThread();
        ((TimeZoneDetectorInternal) LocalServices.getService(TimeZoneDetectorInternal.class)).handleLocationAlgorithmEvent(locationAlgorithmEvent);
    }
}
