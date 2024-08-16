package com.android.server.health;

import android.hardware.health.V1_0.HealthInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class Utils {
    private Utils() {
    }

    public static void copy(HealthInfo healthInfo, HealthInfo healthInfo2) {
        healthInfo.chargerAcOnline = healthInfo2.chargerAcOnline;
        healthInfo.chargerUsbOnline = healthInfo2.chargerUsbOnline;
        healthInfo.chargerWirelessOnline = healthInfo2.chargerWirelessOnline;
        healthInfo.maxChargingCurrent = healthInfo2.maxChargingCurrent;
        healthInfo.maxChargingVoltage = healthInfo2.maxChargingVoltage;
        healthInfo.batteryStatus = healthInfo2.batteryStatus;
        healthInfo.batteryHealth = healthInfo2.batteryHealth;
        healthInfo.batteryPresent = healthInfo2.batteryPresent;
        healthInfo.batteryLevel = healthInfo2.batteryLevel;
        healthInfo.batteryVoltage = healthInfo2.batteryVoltage;
        healthInfo.batteryTemperature = healthInfo2.batteryTemperature;
        healthInfo.batteryCurrent = healthInfo2.batteryCurrent;
        healthInfo.batteryCycleCount = healthInfo2.batteryCycleCount;
        healthInfo.batteryFullCharge = healthInfo2.batteryFullCharge;
        healthInfo.batteryChargeCounter = healthInfo2.batteryChargeCounter;
        healthInfo.batteryTechnology = healthInfo2.batteryTechnology;
    }

    public static void copyV1Battery(android.hardware.health.HealthInfo healthInfo, android.hardware.health.HealthInfo healthInfo2) {
        healthInfo.chargerAcOnline = healthInfo2.chargerAcOnline;
        healthInfo.chargerUsbOnline = healthInfo2.chargerUsbOnline;
        healthInfo.chargerWirelessOnline = healthInfo2.chargerWirelessOnline;
        healthInfo.maxChargingCurrentMicroamps = healthInfo2.maxChargingCurrentMicroamps;
        healthInfo.maxChargingVoltageMicrovolts = healthInfo2.maxChargingVoltageMicrovolts;
        healthInfo.batteryStatus = healthInfo2.batteryStatus;
        healthInfo.batteryHealth = healthInfo2.batteryHealth;
        healthInfo.batteryPresent = healthInfo2.batteryPresent;
        healthInfo.batteryLevel = healthInfo2.batteryLevel;
        healthInfo.batteryVoltageMillivolts = healthInfo2.batteryVoltageMillivolts;
        healthInfo.batteryTemperatureTenthsCelsius = healthInfo2.batteryTemperatureTenthsCelsius;
        healthInfo.batteryCurrentMicroamps = healthInfo2.batteryCurrentMicroamps;
        healthInfo.batteryCycleCount = healthInfo2.batteryCycleCount;
        healthInfo.batteryFullChargeUah = healthInfo2.batteryFullChargeUah;
        healthInfo.batteryChargeCounterUah = healthInfo2.batteryChargeCounterUah;
        healthInfo.batteryTechnology = healthInfo2.batteryTechnology;
        healthInfo.batteryCurrentAverageMicroamps = healthInfo2.batteryCurrentAverageMicroamps;
        healthInfo.batteryCapacityLevel = healthInfo2.batteryCapacityLevel;
        healthInfo.batteryChargeTimeToFullNowSeconds = healthInfo2.batteryChargeTimeToFullNowSeconds;
        healthInfo.batteryFullChargeDesignCapacityUah = healthInfo2.batteryFullChargeDesignCapacityUah;
    }
}
