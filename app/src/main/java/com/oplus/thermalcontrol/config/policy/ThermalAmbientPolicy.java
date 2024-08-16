package com.oplus.thermalcontrol.config.policy;

import org.w3c.dom.Element;

/* loaded from: classes2.dex */
public class ThermalAmbientPolicy {
    public static final String KEY_AMBIENT_LEVEL = "ambient_level";
    public static final String KEY_FALLBACK_TEMP = "fallBackTemp";
    public static final String KEY_LEVEL = "level";
    public static final String KEY_THERMAL_OFFSET_LEVEL = "thermalOffsetLevel";
    public static final String KEY_TRIGGER_TEMP = "triggerTemp";
    public final int fallBackTemp;
    public final int level;
    public final int thermalOffsetLevel;
    public final int triggerTemp;

    public ThermalAmbientPolicy(int i10, int i11, int i12, int i13) {
        this.level = i10;
        this.triggerTemp = i11;
        this.fallBackTemp = i12;
        this.thermalOffsetLevel = i13;
    }

    public static ThermalAmbientPolicy createFromElement(Element element) {
        return new ThermalAmbientPolicy(element.getAttribute("level") != "" ? Integer.parseInt(element.getAttribute("level")) : Integer.MIN_VALUE, element.getAttribute(KEY_TRIGGER_TEMP) != "" ? Integer.parseInt(element.getAttribute(KEY_TRIGGER_TEMP)) : 0, element.getAttribute(KEY_FALLBACK_TEMP) != "" ? Integer.parseInt(element.getAttribute(KEY_FALLBACK_TEMP)) : 0, element.getAttribute(KEY_THERMAL_OFFSET_LEVEL) != "" ? Integer.parseInt(element.getAttribute(KEY_THERMAL_OFFSET_LEVEL)) : 0);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ThermalAmbientPolicy thermalAmbientPolicy = (ThermalAmbientPolicy) obj;
        return this.level == thermalAmbientPolicy.level && this.triggerTemp == thermalAmbientPolicy.triggerTemp && this.fallBackTemp == thermalAmbientPolicy.fallBackTemp && this.thermalOffsetLevel == thermalAmbientPolicy.thermalOffsetLevel;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder(128);
        sb2.append("AmbientPolicy{");
        sb2.append("level:" + this.level);
        sb2.append(", triggerTemp:" + this.triggerTemp);
        sb2.append(", fallBackTemp:" + this.fallBackTemp);
        sb2.append(", thermalOffsetLevel:" + this.thermalOffsetLevel);
        sb2.append("}");
        return sb2.toString();
    }
}
