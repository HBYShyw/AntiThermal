// IPhoneManagerInterface.aidl
package com.oplus.remote.policy;

// Declare any non-default types here with import statements
import com.oplus.remote.policy.IRemoteClientCallback;

interface IRemoteClientInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int getUserMode();
    long getLeftUsetime();
    void registerCallback(IRemoteClientCallback callback);
    void unregisterCallback(IRemoteClientCallback callback);
    int getPowerSaveAdvice();
    Bundle getTopThreeSippers();
    int getBatteryHealthValue();
}
