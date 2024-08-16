package android.hardware.display;

/* loaded from: classes.dex */
public interface IOplusDisplayManagerInternalEx {
    void blockScreenOnByBiometrics(String str);

    int getScreenState();

    boolean hasBiometricsBlockedReason(String str);

    boolean isBlockDisplayByBiometrics();

    boolean isBlockScreenOnByBiometrics();

    void removeFaceBlockReasonFromBlockReasonList();

    void setUseProximityForceSuspend(boolean z);

    void unblockScreenOnByBiometrics(String str);
}
