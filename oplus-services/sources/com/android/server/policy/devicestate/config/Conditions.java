package com.android.server.policy.devicestate.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class Conditions {
    private CameraCondition camera;
    private DisplayCondition display;
    private KeyguardCondition keyguard;
    private LidSwitchCondition lidSwitch;
    private List<SensorCondition> sensor;

    public LidSwitchCondition getLidSwitch() {
        return this.lidSwitch;
    }

    boolean hasLidSwitch() {
        return this.lidSwitch != null;
    }

    public void setLidSwitch(LidSwitchCondition lidSwitchCondition) {
        this.lidSwitch = lidSwitchCondition;
    }

    public List<SensorCondition> getSensor() {
        if (this.sensor == null) {
            this.sensor = new ArrayList();
        }
        return this.sensor;
    }

    public KeyguardCondition getKeyguard() {
        return this.keyguard;
    }

    boolean hasKeyguard() {
        return this.keyguard != null;
    }

    public void setKeyguard(KeyguardCondition keyguardCondition) {
        this.keyguard = keyguardCondition;
    }

    public CameraCondition getCamera() {
        return this.camera;
    }

    boolean hasCamera() {
        return this.camera != null;
    }

    public void setCamera(CameraCondition cameraCondition) {
        this.camera = cameraCondition;
    }

    public DisplayCondition getDisplay() {
        return this.display;
    }

    boolean hasDisplay() {
        return this.display != null;
    }

    public void setDisplay(DisplayCondition displayCondition) {
        this.display = displayCondition;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Conditions read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        Conditions conditions = new Conditions();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("lid-switch")) {
                    conditions.setLidSwitch(LidSwitchCondition.read(xmlPullParser));
                } else if (name.equals("sensor")) {
                    conditions.getSensor().add(SensorCondition.read(xmlPullParser));
                } else if (name.equals("keyguard")) {
                    conditions.setKeyguard(KeyguardCondition.read(xmlPullParser));
                } else if (name.equals("camera")) {
                    conditions.setCamera(CameraCondition.read(xmlPullParser));
                } else if (name.equals("display")) {
                    conditions.setDisplay(DisplayCondition.read(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return conditions;
        }
        throw new DatatypeConfigurationException("Conditions is not closed");
    }
}
