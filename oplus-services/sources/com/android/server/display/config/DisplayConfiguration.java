package com.android.server.display.config;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DisplayConfiguration {
    private Thresholds ambientBrightnessChangeThresholds;
    private Thresholds ambientBrightnessChangeThresholdsIdle;
    private BigInteger ambientLightHorizonLong;
    private BigInteger ambientLightHorizonShort;
    private AutoBrightness autoBrightness;
    private DensityMapping densityMapping;
    private Thresholds displayBrightnessChangeThresholds;
    private Thresholds displayBrightnessChangeThresholdsIdle;
    private HighBrightnessMode highBrightnessMode;
    private SensorDetails lightSensor;
    private String name;
    private SensorDetails proxSensor;
    private DisplayQuirks quirks;
    private RefreshRateConfigs refreshRate;
    private BigDecimal screenBrightnessDefault;
    private NitsMap screenBrightnessMap;
    private BigInteger screenBrightnessRampDecreaseMaxMillis;
    private BigDecimal screenBrightnessRampFastDecrease;
    private BigDecimal screenBrightnessRampFastIncrease;
    private BigInteger screenBrightnessRampIncreaseMaxMillis;
    private BigDecimal screenBrightnessRampSlowDecrease;
    private BigDecimal screenBrightnessRampSlowIncrease;
    private SensorDetails screenOffBrightnessSensor;
    private IntegerArray screenOffBrightnessSensorValueToLux;
    private ThermalThrottling thermalThrottling;
    private UsiVersion usiVersion;

    public final String getName() {
        return this.name;
    }

    boolean hasName() {
        return this.name != null;
    }

    public final void setName(String str) {
        this.name = str;
    }

    public final DensityMapping getDensityMapping() {
        return this.densityMapping;
    }

    boolean hasDensityMapping() {
        return this.densityMapping != null;
    }

    public final void setDensityMapping(DensityMapping densityMapping) {
        this.densityMapping = densityMapping;
    }

    public final NitsMap getScreenBrightnessMap() {
        return this.screenBrightnessMap;
    }

    boolean hasScreenBrightnessMap() {
        return this.screenBrightnessMap != null;
    }

    public final void setScreenBrightnessMap(NitsMap nitsMap) {
        this.screenBrightnessMap = nitsMap;
    }

    public final BigDecimal getScreenBrightnessDefault() {
        return this.screenBrightnessDefault;
    }

    boolean hasScreenBrightnessDefault() {
        return this.screenBrightnessDefault != null;
    }

    public final void setScreenBrightnessDefault(BigDecimal bigDecimal) {
        this.screenBrightnessDefault = bigDecimal;
    }

    public final ThermalThrottling getThermalThrottling() {
        return this.thermalThrottling;
    }

    boolean hasThermalThrottling() {
        return this.thermalThrottling != null;
    }

    public final void setThermalThrottling(ThermalThrottling thermalThrottling) {
        this.thermalThrottling = thermalThrottling;
    }

    public HighBrightnessMode getHighBrightnessMode() {
        return this.highBrightnessMode;
    }

    boolean hasHighBrightnessMode() {
        return this.highBrightnessMode != null;
    }

    public void setHighBrightnessMode(HighBrightnessMode highBrightnessMode) {
        this.highBrightnessMode = highBrightnessMode;
    }

    public DisplayQuirks getQuirks() {
        return this.quirks;
    }

    boolean hasQuirks() {
        return this.quirks != null;
    }

    public void setQuirks(DisplayQuirks displayQuirks) {
        this.quirks = displayQuirks;
    }

    public AutoBrightness getAutoBrightness() {
        return this.autoBrightness;
    }

    boolean hasAutoBrightness() {
        return this.autoBrightness != null;
    }

    public void setAutoBrightness(AutoBrightness autoBrightness) {
        this.autoBrightness = autoBrightness;
    }

    public RefreshRateConfigs getRefreshRate() {
        return this.refreshRate;
    }

    boolean hasRefreshRate() {
        return this.refreshRate != null;
    }

    public void setRefreshRate(RefreshRateConfigs refreshRateConfigs) {
        this.refreshRate = refreshRateConfigs;
    }

    public final BigDecimal getScreenBrightnessRampFastDecrease() {
        return this.screenBrightnessRampFastDecrease;
    }

    boolean hasScreenBrightnessRampFastDecrease() {
        return this.screenBrightnessRampFastDecrease != null;
    }

    public final void setScreenBrightnessRampFastDecrease(BigDecimal bigDecimal) {
        this.screenBrightnessRampFastDecrease = bigDecimal;
    }

    public final BigDecimal getScreenBrightnessRampFastIncrease() {
        return this.screenBrightnessRampFastIncrease;
    }

    boolean hasScreenBrightnessRampFastIncrease() {
        return this.screenBrightnessRampFastIncrease != null;
    }

    public final void setScreenBrightnessRampFastIncrease(BigDecimal bigDecimal) {
        this.screenBrightnessRampFastIncrease = bigDecimal;
    }

    public final BigDecimal getScreenBrightnessRampSlowDecrease() {
        return this.screenBrightnessRampSlowDecrease;
    }

    boolean hasScreenBrightnessRampSlowDecrease() {
        return this.screenBrightnessRampSlowDecrease != null;
    }

    public final void setScreenBrightnessRampSlowDecrease(BigDecimal bigDecimal) {
        this.screenBrightnessRampSlowDecrease = bigDecimal;
    }

    public final BigDecimal getScreenBrightnessRampSlowIncrease() {
        return this.screenBrightnessRampSlowIncrease;
    }

    boolean hasScreenBrightnessRampSlowIncrease() {
        return this.screenBrightnessRampSlowIncrease != null;
    }

    public final void setScreenBrightnessRampSlowIncrease(BigDecimal bigDecimal) {
        this.screenBrightnessRampSlowIncrease = bigDecimal;
    }

    public final BigInteger getScreenBrightnessRampIncreaseMaxMillis() {
        return this.screenBrightnessRampIncreaseMaxMillis;
    }

    boolean hasScreenBrightnessRampIncreaseMaxMillis() {
        return this.screenBrightnessRampIncreaseMaxMillis != null;
    }

    public final void setScreenBrightnessRampIncreaseMaxMillis(BigInteger bigInteger) {
        this.screenBrightnessRampIncreaseMaxMillis = bigInteger;
    }

    public final BigInteger getScreenBrightnessRampDecreaseMaxMillis() {
        return this.screenBrightnessRampDecreaseMaxMillis;
    }

    boolean hasScreenBrightnessRampDecreaseMaxMillis() {
        return this.screenBrightnessRampDecreaseMaxMillis != null;
    }

    public final void setScreenBrightnessRampDecreaseMaxMillis(BigInteger bigInteger) {
        this.screenBrightnessRampDecreaseMaxMillis = bigInteger;
    }

    public final SensorDetails getLightSensor() {
        return this.lightSensor;
    }

    boolean hasLightSensor() {
        return this.lightSensor != null;
    }

    public final void setLightSensor(SensorDetails sensorDetails) {
        this.lightSensor = sensorDetails;
    }

    public final SensorDetails getScreenOffBrightnessSensor() {
        return this.screenOffBrightnessSensor;
    }

    boolean hasScreenOffBrightnessSensor() {
        return this.screenOffBrightnessSensor != null;
    }

    public final void setScreenOffBrightnessSensor(SensorDetails sensorDetails) {
        this.screenOffBrightnessSensor = sensorDetails;
    }

    public final SensorDetails getProxSensor() {
        return this.proxSensor;
    }

    boolean hasProxSensor() {
        return this.proxSensor != null;
    }

    public final void setProxSensor(SensorDetails sensorDetails) {
        this.proxSensor = sensorDetails;
    }

    public final BigInteger getAmbientLightHorizonLong() {
        return this.ambientLightHorizonLong;
    }

    boolean hasAmbientLightHorizonLong() {
        return this.ambientLightHorizonLong != null;
    }

    public final void setAmbientLightHorizonLong(BigInteger bigInteger) {
        this.ambientLightHorizonLong = bigInteger;
    }

    public final BigInteger getAmbientLightHorizonShort() {
        return this.ambientLightHorizonShort;
    }

    boolean hasAmbientLightHorizonShort() {
        return this.ambientLightHorizonShort != null;
    }

    public final void setAmbientLightHorizonShort(BigInteger bigInteger) {
        this.ambientLightHorizonShort = bigInteger;
    }

    public final Thresholds getDisplayBrightnessChangeThresholds() {
        return this.displayBrightnessChangeThresholds;
    }

    boolean hasDisplayBrightnessChangeThresholds() {
        return this.displayBrightnessChangeThresholds != null;
    }

    public final void setDisplayBrightnessChangeThresholds(Thresholds thresholds) {
        this.displayBrightnessChangeThresholds = thresholds;
    }

    public final Thresholds getAmbientBrightnessChangeThresholds() {
        return this.ambientBrightnessChangeThresholds;
    }

    boolean hasAmbientBrightnessChangeThresholds() {
        return this.ambientBrightnessChangeThresholds != null;
    }

    public final void setAmbientBrightnessChangeThresholds(Thresholds thresholds) {
        this.ambientBrightnessChangeThresholds = thresholds;
    }

    public final Thresholds getDisplayBrightnessChangeThresholdsIdle() {
        return this.displayBrightnessChangeThresholdsIdle;
    }

    boolean hasDisplayBrightnessChangeThresholdsIdle() {
        return this.displayBrightnessChangeThresholdsIdle != null;
    }

    public final void setDisplayBrightnessChangeThresholdsIdle(Thresholds thresholds) {
        this.displayBrightnessChangeThresholdsIdle = thresholds;
    }

    public final Thresholds getAmbientBrightnessChangeThresholdsIdle() {
        return this.ambientBrightnessChangeThresholdsIdle;
    }

    boolean hasAmbientBrightnessChangeThresholdsIdle() {
        return this.ambientBrightnessChangeThresholdsIdle != null;
    }

    public final void setAmbientBrightnessChangeThresholdsIdle(Thresholds thresholds) {
        this.ambientBrightnessChangeThresholdsIdle = thresholds;
    }

    public final IntegerArray getScreenOffBrightnessSensorValueToLux() {
        return this.screenOffBrightnessSensorValueToLux;
    }

    boolean hasScreenOffBrightnessSensorValueToLux() {
        return this.screenOffBrightnessSensorValueToLux != null;
    }

    public final void setScreenOffBrightnessSensorValueToLux(IntegerArray integerArray) {
        this.screenOffBrightnessSensorValueToLux = integerArray;
    }

    public final UsiVersion getUsiVersion() {
        return this.usiVersion;
    }

    boolean hasUsiVersion() {
        return this.usiVersion != null;
    }

    public final void setUsiVersion(UsiVersion usiVersion) {
        this.usiVersion = usiVersion;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DisplayConfiguration read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        DisplayConfiguration displayConfiguration = new DisplayConfiguration();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("name")) {
                    displayConfiguration.setName(XmlParser.readText(xmlPullParser));
                } else if (name.equals("densityMapping")) {
                    displayConfiguration.setDensityMapping(DensityMapping.read(xmlPullParser));
                } else if (name.equals("screenBrightnessMap")) {
                    displayConfiguration.setScreenBrightnessMap(NitsMap.read(xmlPullParser));
                } else if (name.equals("screenBrightnessDefault")) {
                    displayConfiguration.setScreenBrightnessDefault(new BigDecimal(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("thermalThrottling")) {
                    displayConfiguration.setThermalThrottling(ThermalThrottling.read(xmlPullParser));
                } else if (name.equals("highBrightnessMode")) {
                    displayConfiguration.setHighBrightnessMode(HighBrightnessMode.read(xmlPullParser));
                } else if (name.equals("quirks")) {
                    displayConfiguration.setQuirks(DisplayQuirks.read(xmlPullParser));
                } else if (name.equals("autoBrightness")) {
                    displayConfiguration.setAutoBrightness(AutoBrightness.read(xmlPullParser));
                } else if (name.equals("refreshRate")) {
                    displayConfiguration.setRefreshRate(RefreshRateConfigs.read(xmlPullParser));
                } else if (name.equals("screenBrightnessRampFastDecrease")) {
                    displayConfiguration.setScreenBrightnessRampFastDecrease(new BigDecimal(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("screenBrightnessRampFastIncrease")) {
                    displayConfiguration.setScreenBrightnessRampFastIncrease(new BigDecimal(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("screenBrightnessRampSlowDecrease")) {
                    displayConfiguration.setScreenBrightnessRampSlowDecrease(new BigDecimal(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("screenBrightnessRampSlowIncrease")) {
                    displayConfiguration.setScreenBrightnessRampSlowIncrease(new BigDecimal(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("screenBrightnessRampIncreaseMaxMillis")) {
                    displayConfiguration.setScreenBrightnessRampIncreaseMaxMillis(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("screenBrightnessRampDecreaseMaxMillis")) {
                    displayConfiguration.setScreenBrightnessRampDecreaseMaxMillis(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("lightSensor")) {
                    displayConfiguration.setLightSensor(SensorDetails.read(xmlPullParser));
                } else if (name.equals("screenOffBrightnessSensor")) {
                    displayConfiguration.setScreenOffBrightnessSensor(SensorDetails.read(xmlPullParser));
                } else if (name.equals("proxSensor")) {
                    displayConfiguration.setProxSensor(SensorDetails.read(xmlPullParser));
                } else if (name.equals("ambientLightHorizonLong")) {
                    displayConfiguration.setAmbientLightHorizonLong(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("ambientLightHorizonShort")) {
                    displayConfiguration.setAmbientLightHorizonShort(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("displayBrightnessChangeThresholds")) {
                    displayConfiguration.setDisplayBrightnessChangeThresholds(Thresholds.read(xmlPullParser));
                } else if (name.equals("ambientBrightnessChangeThresholds")) {
                    displayConfiguration.setAmbientBrightnessChangeThresholds(Thresholds.read(xmlPullParser));
                } else if (name.equals("displayBrightnessChangeThresholdsIdle")) {
                    displayConfiguration.setDisplayBrightnessChangeThresholdsIdle(Thresholds.read(xmlPullParser));
                } else if (name.equals("ambientBrightnessChangeThresholdsIdle")) {
                    displayConfiguration.setAmbientBrightnessChangeThresholdsIdle(Thresholds.read(xmlPullParser));
                } else if (name.equals("screenOffBrightnessSensorValueToLux")) {
                    displayConfiguration.setScreenOffBrightnessSensorValueToLux(IntegerArray.read(xmlPullParser));
                } else if (name.equals("usiVersion")) {
                    displayConfiguration.setUsiVersion(UsiVersion.read(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return displayConfiguration;
        }
        throw new DatatypeConfigurationException("DisplayConfiguration is not closed");
    }
}
