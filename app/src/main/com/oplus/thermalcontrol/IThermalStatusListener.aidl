// IThermalStatusListener.aidl
package com.oplus.thermalcontrol;

// Declare any non-default types here with import statements

interface IThermalStatusListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void empty1();
     void empty2();
     void notifyThermalStatus (int status);
     void notifyThermalBroadCast (int status, int temp0);
     void notifyThermalSource(int level, int temp, String heatInfo);
     void notifyTsensorTemp(int temp);
}
