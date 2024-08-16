package com.oplus.thermalcontrol.config.policy;

import android.text.TextUtils;
import b6.LocalLog;
import org.w3c.dom.Element;

/* loaded from: classes2.dex */
public class ThermalPolicy implements Cloneable {
    public static final int BOOST_BREAK_FIRST_DEFAULT = -2;
    public static final int BOOST_BREAK_SECOND_DEFAULT = -2;
    public static final int CAMERA_BRIGHTNESS_DEFAULT = 255;
    public static final int CPU_DEFAULT = -2;
    public static final int CPU_NO_LIMIT = -1;
    public static final int EMPTY_VALUE = -2;
    public static final int GPU_DEFAULT = -2;
    public static final int GPU_NO_LIMIT = -1;
    public static final String IPA_WEIGHT_DEFAULT = "-2,-2,-2";
    public static final String KEY_BOOST_BREAK_FIRST = "boostBreak1";
    public static final String KEY_BOOST_BREAK_SECOND = "boostBreak2";
    public static final String KEY_BOOST_IPA_WEIGHT = "ipaweight";
    public static final String KEY_BRIGHTNESS = "brightness";
    public static final String KEY_CAMERA = "disCamera";
    public static final String KEY_CAMERA_BRIGHTNESS = "cameraBrightness";
    public static final String KEY_CAMERA_VIDEO = "stopCameraVideo";
    public static final String KEY_CHARGE = "charge";
    public static final String KEY_CPU = "cpu";
    public static final String KEY_CPUPOWER = "cpuPower";
    public static final String KEY_FLASHLIGHT = "disFlashlight";
    public static final String KEY_FPS = "fps";
    public static final String KEY_FRAME_INSERT = "disFrameInsert";
    public static final String KEY_GPU = "gpu";
    public static final String KEY_HEATOFF_POLICY = "heatoff_policy";
    public static final String KEY_MODEM = "modem";
    public static final String KEY_REFRESH_RATE = "refreshRate";
    public static final String KEY_RESTRICT = "restrict";
    public static final String KEY_SPEED_CHARGE_ADD = "speedChargeAdd";
    public static final String KEY_TEMP_GEAR = "tempGear";
    public static final String KEY_THERMAL_ENVIRONMENT = "environment_normal";
    public static final String KEY_THERMAL_SERIOUS = "thermalSerious";
    public static final String KEY_TORCH = "disTorch";
    public static final String KEY_VIDEOSR = "disVideoSR";
    public static final String KEY_WIFI = "wifi";
    public static final String KEY_WIFIHOTSPOT = "disWifiHotSpot";
    private static final String TAG = "ThermalPolicy";
    public static final int THERMAL_SERIOUS_DEFAULT = -2;
    public static final int THERMAL_SERIOUS_NO_LEVEL = 0;
    public int boostBreak1;
    public int boostBreak2;
    public int brightness;
    public int cameraBrightness;
    public String categoryName;
    public int charge;
    public String configItemName;
    public int cpu;
    public int cpuPower;
    public int disCamera;
    public int disFlashlight;
    public int disFrameInsert;
    public int disTorch;
    public int disVideoSR;
    public int disWifiHotSpot;
    public int fps;
    public int gearLevel;
    public int gpu;
    public int heatoffPolicy;
    public String ipaweight;
    public int modem;
    public int refreshRate;
    public int restrict;
    public int speedChargeAdd;
    public int stopCameraVideo;
    public int thermalSerious;
    public int wifi;

    public ThermalPolicy(String str, String str2, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, int i20, int i21, int i22, int i23, int i24, int i25, int i26, int i27, int i28, int i29, int i30, String str3, int i31, int i32, int i33) {
        this.categoryName = str;
        this.configItemName = str2;
        this.gearLevel = i10;
        this.cpu = i11;
        this.gpu = i12;
        this.restrict = i13;
        this.brightness = i14;
        this.charge = i15;
        this.modem = i16;
        this.disFlashlight = i17;
        this.stopCameraVideo = i18;
        this.cameraBrightness = i19;
        this.disCamera = i20;
        this.disWifiHotSpot = i21;
        this.disTorch = i22;
        this.disFrameInsert = i23;
        this.fps = i24;
        this.refreshRate = i25;
        this.cpuPower = i26;
        this.disVideoSR = i27;
        this.wifi = i28;
        this.thermalSerious = i29;
        this.speedChargeAdd = i30;
        this.ipaweight = str3;
        this.boostBreak1 = i31;
        this.boostBreak2 = i32;
        this.heatoffPolicy = i33;
    }

    public static ThermalPolicy createFromElement(Element element, String str, String str2) {
        int parseInt = element.getAttribute(KEY_TEMP_GEAR) != "" ? Integer.parseInt(element.getAttribute(KEY_TEMP_GEAR)) : 0;
        int parseInt2 = element.getAttribute(KEY_CPU) != "" ? Integer.parseInt(element.getAttribute(KEY_CPU)) : -2;
        int parseInt3 = element.getAttribute(KEY_GPU) != "" ? Integer.parseInt(element.getAttribute(KEY_GPU)) : -2;
        int parseInt4 = element.getAttribute(KEY_RESTRICT) != "" ? Integer.parseInt(element.getAttribute(KEY_RESTRICT)) : -2;
        int parseInt5 = element.getAttribute(KEY_BRIGHTNESS) != "" ? Integer.parseInt(element.getAttribute(KEY_BRIGHTNESS)) : -2;
        int parseInt6 = element.getAttribute(KEY_CHARGE) != "" ? Integer.parseInt(element.getAttribute(KEY_CHARGE)) : -2;
        int parseInt7 = element.getAttribute(KEY_MODEM) != "" ? Integer.parseInt(element.getAttribute(KEY_MODEM)) : -2;
        int parseInt8 = element.getAttribute(KEY_FLASHLIGHT) != "" ? Integer.parseInt(element.getAttribute(KEY_FLASHLIGHT)) : -2;
        int parseInt9 = element.getAttribute(KEY_CAMERA_VIDEO) != "" ? Integer.parseInt(element.getAttribute(KEY_CAMERA_VIDEO)) : -2;
        int parseInt10 = element.getAttribute(KEY_CAMERA_BRIGHTNESS) != "" ? Integer.parseInt(element.getAttribute(KEY_CAMERA_BRIGHTNESS)) : -2;
        int parseInt11 = element.getAttribute(KEY_CAMERA) != "" ? Integer.parseInt(element.getAttribute(KEY_CAMERA)) : -2;
        int parseInt12 = element.getAttribute(KEY_WIFIHOTSPOT) != "" ? Integer.parseInt(element.getAttribute(KEY_WIFIHOTSPOT)) : -2;
        int parseInt13 = element.getAttribute(KEY_TORCH) != "" ? Integer.parseInt(element.getAttribute(KEY_TORCH)) : -2;
        int parseInt14 = element.getAttribute(KEY_FRAME_INSERT) != "" ? Integer.parseInt(element.getAttribute(KEY_FRAME_INSERT)) : -2;
        int parseInt15 = element.getAttribute(KEY_FPS) != "" ? Integer.parseInt(element.getAttribute(KEY_FPS)) : -2;
        int parseInt16 = element.getAttribute(KEY_REFRESH_RATE) != "" ? Integer.parseInt(element.getAttribute(KEY_REFRESH_RATE)) : -2;
        int parseInt17 = element.getAttribute(KEY_CPUPOWER) != "" ? Integer.parseInt(element.getAttribute(KEY_CPUPOWER)) : -2;
        int parseInt18 = element.getAttribute(KEY_VIDEOSR) != "" ? Integer.parseInt(element.getAttribute(KEY_VIDEOSR)) : -2;
        int parseInt19 = element.getAttribute(KEY_WIFI) != "" ? Integer.parseInt(element.getAttribute(KEY_WIFI)) : -2;
        int parseInt20 = element.getAttribute(KEY_THERMAL_SERIOUS) != "" ? Integer.parseInt(element.getAttribute(KEY_THERMAL_SERIOUS)) : -2;
        int parseInt21 = element.getAttribute(KEY_SPEED_CHARGE_ADD) != "" ? Integer.parseInt(element.getAttribute(KEY_SPEED_CHARGE_ADD)) : -2;
        String attribute = element.getAttribute(KEY_BOOST_IPA_WEIGHT) != "" ? element.getAttribute(KEY_BOOST_IPA_WEIGHT) : IPA_WEIGHT_DEFAULT;
        int parseInt22 = element.getAttribute(KEY_BOOST_BREAK_FIRST) != "" ? Integer.parseInt(element.getAttribute(KEY_BOOST_BREAK_FIRST)) : -2;
        int parseInt23 = element.getAttribute(KEY_BOOST_BREAK_SECOND) != "" ? Integer.parseInt(element.getAttribute(KEY_BOOST_BREAK_SECOND)) : -2;
        int parseInt24 = element.getAttribute(KEY_HEATOFF_POLICY) != "" ? Integer.parseInt(element.getAttribute(KEY_HEATOFF_POLICY)) : -2;
        int i10 = parseInt21;
        int i11 = parseInt22;
        int i12 = parseInt23;
        int i13 = parseInt20;
        int i14 = parseInt12;
        int i15 = parseInt11;
        int i16 = parseInt10;
        int i17 = parseInt9;
        int i18 = parseInt8;
        int i19 = parseInt6;
        int i20 = parseInt7;
        ThermalPolicy thermalPolicy = new ThermalPolicy(str, str2, parseInt, parseInt2, parseInt3, parseInt4, parseInt5, i19, i20, i18, i17, i16, i15, i14, parseInt13, parseInt14, parseInt15, parseInt16, parseInt17, parseInt18, parseInt19, i13, i10, attribute, i11, i12, parseInt24);
        LocalLog.a(TAG, "preConditionTagName: " + str + ", conditionTagName: " + str2 + ", adding gearVal:" + parseInt + ", cpuVal:" + parseInt2 + ", gpuVal:" + parseInt3 + ", restrictVal:" + parseInt4 + ", brightnessVal:" + parseInt5 + ", chargeVal:" + i19 + ", modemVal:" + i20 + ", disFlashVal:" + i18 + ", stopCameraVideoVal:" + i17 + ", cameraBrightnessVal:" + i16 + ", disCamera:" + i15 + ", disWifiHotSpot:" + i14 + ", disTorch:" + parseInt13 + ", disFrameInsert:" + parseInt14 + ", fps:" + parseInt15 + ", refreshRate:" + parseInt16 + ", cpuPower:" + parseInt17 + ", videoSR:" + parseInt18 + ", wifi:" + parseInt19 + ", thermalSerious:" + i13 + ", speedChargeAdd:" + i10 + ", ipaweight:" + attribute + ", boostBreak1:" + i11 + ", boostBreak2:" + i12 + ", heatoffPolicy:" + parseInt24);
        return thermalPolicy;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            ThermalPolicy thermalPolicy = (ThermalPolicy) obj;
            if (this.gearLevel == thermalPolicy.gearLevel && this.cpu == thermalPolicy.cpu && this.gpu == thermalPolicy.gpu && this.restrict == thermalPolicy.restrict && this.brightness == thermalPolicy.brightness && this.charge == thermalPolicy.charge && this.modem == thermalPolicy.modem && this.disFlashlight == thermalPolicy.disFlashlight && this.stopCameraVideo == thermalPolicy.stopCameraVideo && this.cameraBrightness == thermalPolicy.cameraBrightness && this.disCamera == thermalPolicy.disCamera && this.disWifiHotSpot == thermalPolicy.disWifiHotSpot && this.disTorch == thermalPolicy.disTorch && this.disFrameInsert == thermalPolicy.disFrameInsert && this.fps == thermalPolicy.fps && this.refreshRate == thermalPolicy.refreshRate && this.cpuPower == thermalPolicy.cpuPower && this.disVideoSR == thermalPolicy.disVideoSR && this.wifi == thermalPolicy.wifi && this.thermalSerious == thermalPolicy.thermalSerious && this.speedChargeAdd == thermalPolicy.speedChargeAdd && TextUtils.equals(this.ipaweight, thermalPolicy.ipaweight) && this.boostBreak1 == thermalPolicy.boostBreak1 && this.boostBreak2 == thermalPolicy.boostBreak2 && this.heatoffPolicy == thermalPolicy.heatoffPolicy) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public void overridePolicy(ThermalPolicy thermalPolicy) {
        this.categoryName = thermalPolicy.categoryName;
        this.configItemName = thermalPolicy.configItemName;
        this.gearLevel = thermalPolicy.gearLevel;
        int i10 = thermalPolicy.cpu;
        if (i10 != -2) {
            this.cpu = i10;
        }
        int i11 = thermalPolicy.gpu;
        if (i11 != -2) {
            this.gpu = i11;
        }
        int i12 = thermalPolicy.restrict;
        if (i12 != -2) {
            this.restrict = i12;
        }
        int i13 = thermalPolicy.brightness;
        if (i13 != -2) {
            this.brightness = i13;
        }
        int i14 = thermalPolicy.charge;
        if (i14 != -2) {
            this.charge = i14;
        }
        int i15 = thermalPolicy.modem;
        if (i15 != -2) {
            this.modem = i15;
        }
        int i16 = thermalPolicy.disFlashlight;
        if (i16 != -2) {
            this.disFlashlight = i16;
        }
        int i17 = thermalPolicy.stopCameraVideo;
        if (i17 != -2) {
            this.stopCameraVideo = i17;
        }
        int i18 = thermalPolicy.cameraBrightness;
        if (i18 != -2) {
            this.cameraBrightness = i18;
        }
        int i19 = thermalPolicy.disCamera;
        if (i19 != -2) {
            this.disCamera = i19;
        }
        int i20 = thermalPolicy.disWifiHotSpot;
        if (i20 != -2) {
            this.disWifiHotSpot = i20;
        }
        int i21 = thermalPolicy.disTorch;
        if (i21 != -2) {
            this.disTorch = i21;
        }
        int i22 = thermalPolicy.disFrameInsert;
        if (i22 != -2) {
            this.disFrameInsert = i22;
        }
        int i23 = thermalPolicy.fps;
        if (i23 != -2) {
            this.fps = i23;
        }
        int i24 = thermalPolicy.refreshRate;
        if (i24 != -2) {
            this.refreshRate = i24;
        }
        int i25 = thermalPolicy.cpuPower;
        if (i25 != -2) {
            this.cpuPower = i25;
        }
        int i26 = thermalPolicy.disVideoSR;
        if (i26 != -2) {
            this.disVideoSR = i26;
        }
        int i27 = thermalPolicy.wifi;
        if (i27 != -2) {
            this.wifi = i27;
        }
        int i28 = thermalPolicy.thermalSerious;
        if (i28 != -2) {
            this.thermalSerious = i28;
        }
        int i29 = thermalPolicy.speedChargeAdd;
        if (i29 != -2) {
            this.speedChargeAdd = i29;
        }
        if (!TextUtils.equals(thermalPolicy.ipaweight, IPA_WEIGHT_DEFAULT)) {
            this.ipaweight = thermalPolicy.ipaweight;
        }
        int i30 = thermalPolicy.boostBreak1;
        if (i30 != -2) {
            this.boostBreak1 = i30;
        }
        int i31 = thermalPolicy.boostBreak2;
        if (i31 != -2) {
            this.boostBreak2 = i31;
        }
        int i32 = thermalPolicy.heatoffPolicy;
        if (i32 != -2) {
            this.heatoffPolicy = i32;
        }
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder(128);
        sb2.append("Policy info\n{\n");
        sb2.append(" categoryName: ");
        sb2.append(this.categoryName);
        sb2.append("\n configItemName: ");
        sb2.append(this.configItemName);
        sb2.append(" \n");
        sb2.append(" gearVal:" + this.gearLevel + ", cpuVal:" + this.cpu + ", gpuVal:" + this.gpu + ", restrictVal:" + this.restrict + ", brightnessVal:" + this.brightness + ", chargeVal:" + this.charge + ", modemVal:" + this.modem + ", disFlashVal:" + this.disFlashlight + ", stopCameraVideoVal:" + this.stopCameraVideo + ", cameraBrightnessVal:" + this.cameraBrightness + ", disCameraVal:" + this.disCamera + ", disWifiHotSpotVal:" + this.disWifiHotSpot + ", disTorchVal:" + this.disTorch + ", disFrameInsertVal:" + this.disFrameInsert + ", fpsVal:" + this.fps + ", refreshRateVal:" + this.refreshRate + ", cpuPowerVal:" + this.cpuPower + ", disVideoSRVal:" + this.disVideoSR + ", wifiVal:" + this.wifi + ", thermalSerious:" + this.thermalSerious + ", speedChargeAdd:" + this.speedChargeAdd + ", ipaweight:" + this.ipaweight + ", boostBreak1:" + this.boostBreak1 + ", boostBreak2:" + this.boostBreak2 + ", heatoffPolicy:" + this.heatoffPolicy);
        sb2.append(" \n} \n");
        return sb2.toString();
    }

    public String toStringLite() {
        StringBuilder sb2 = new StringBuilder(128);
        sb2.append(this.gearLevel + "-{ ");
        sb2.append(this.cpu + "." + this.gpu + "." + this.restrict + "." + this.brightness + "." + this.charge + "." + this.modem + "." + this.disFlashlight + "." + this.stopCameraVideo + "." + this.cameraBrightness + "." + this.disCamera + "." + this.disWifiHotSpot + "." + this.disTorch + "." + this.disFrameInsert + "." + this.fps + "." + this.refreshRate + "." + this.cpuPower + "." + this.disVideoSR + "." + this.wifi + "." + this.thermalSerious + "." + this.speedChargeAdd + "." + this.ipaweight + "." + this.boostBreak1 + "." + this.boostBreak2 + "." + this.heatoffPolicy);
        sb2.append(" }");
        return sb2.toString();
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ThermalPolicy m26clone() {
        try {
            return (ThermalPolicy) super.clone();
        } catch (CloneNotSupportedException e10) {
            e10.printStackTrace();
            return null;
        }
    }
}
