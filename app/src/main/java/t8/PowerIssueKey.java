package t8;

import com.oplus.thermalcontrol.config.policy.ThermalPolicy;

/* compiled from: PowerIssueKey.java */
/* renamed from: t8.e, reason: use source file name */
/* loaded from: classes2.dex */
public enum PowerIssueKey {
    ABNORMAL_APP("abnormalapp"),
    APP("app"),
    GPS("gps"),
    AUTO_RUN("autorun"),
    ASSOCIATE("associate"),
    BACKGROUND("background"),
    HIGH_PERFORM("highPerform"),
    AON("aon"),
    FIVE_G("5g"),
    BRIGHTNESS(ThermalPolicy.KEY_BRIGHTNESS),
    HOTSPOT("hotspot"),
    SCREENREFRESHRATE("screenrefreshrate"),
    AOD("aod"),
    SCREEN_OFF("screenoff"),
    CURVEDDISPLAY("curveddisplay"),
    DARK_MODE("darkmode"),
    NOTIFICATIONBRIGHTSCREEN("notificationbrightscreen"),
    EARTHQUAKEWARNING("earthquakewarning");


    /* renamed from: e, reason: collision with root package name */
    private String f18677e;

    PowerIssueKey(String str) {
        this.f18677e = str;
    }

    public String b() {
        return this.f18677e;
    }

    @Override // java.lang.Enum
    public String toString() {
        return "PowerIssueKey{keyName='" + this.f18677e + "'}";
    }
}
