package com.android.server.timedetector;

import android.app.time.ExternalTimeSuggestion;
import android.app.time.TimeState;
import android.app.time.UnixEpochTime;
import android.app.timedetector.ManualTimeSuggestion;
import android.app.timedetector.TelephonyTimeSuggestion;
import com.android.internal.util.Preconditions;
import com.android.server.timezonedetector.Dumpable;
import com.android.server.timezonedetector.StateChangeListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface TimeDetectorStrategy extends Dumpable {
    public static final int ORIGIN_EXTERNAL = 5;
    public static final int ORIGIN_GNSS = 4;
    public static final int ORIGIN_MANUAL = 2;
    public static final int ORIGIN_NETWORK = 3;
    public static final int ORIGIN_TELEPHONY = 1;

    @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface Origin {
    }

    void addNetworkTimeUpdateListener(StateChangeListener stateChangeListener);

    void clearLatestNetworkSuggestion();

    boolean confirmTime(UnixEpochTime unixEpochTime);

    NetworkTimeSuggestion getLatestNetworkSuggestion();

    TimeState getTimeState();

    void setTimeState(TimeState timeState);

    void suggestExternalTime(ExternalTimeSuggestion externalTimeSuggestion);

    void suggestGnssTime(GnssTimeSuggestion gnssTimeSuggestion);

    boolean suggestManualTime(int i, ManualTimeSuggestion manualTimeSuggestion, boolean z);

    void suggestNetworkTime(NetworkTimeSuggestion networkTimeSuggestion);

    void suggestTelephonyTime(TelephonyTimeSuggestion telephonyTimeSuggestion);

    static String originToString(int i) {
        if (i == 1) {
            return "telephony";
        }
        if (i == 2) {
            return "manual";
        }
        if (i == 3) {
            return "network";
        }
        if (i == 4) {
            return "gnss";
        }
        if (i == 5) {
            return "external";
        }
        throw new IllegalArgumentException("origin=" + i);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x004f, code lost:
    
        if (r7.equals("external") == false) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static int stringToOrigin(String str) {
        boolean z = false;
        Preconditions.checkArgument(str != null);
        str.hashCode();
        switch (str.hashCode()) {
            case -1820761141:
                break;
            case -1081415738:
                if (str.equals("manual")) {
                    z = true;
                    break;
                }
                z = -1;
                break;
            case 3177863:
                if (str.equals("gnss")) {
                    z = 2;
                    break;
                }
                z = -1;
                break;
            case 783201304:
                if (str.equals("telephony")) {
                    z = 3;
                    break;
                }
                z = -1;
                break;
            case 1843485230:
                if (str.equals("network")) {
                    z = 4;
                    break;
                }
                z = -1;
                break;
            default:
                z = -1;
                break;
        }
        switch (z) {
            case false:
                return 5;
            case true:
                return 2;
            case true:
                return 4;
            case true:
                return 1;
            case true:
                return 3;
            default:
                throw new IllegalArgumentException("originString=" + str);
        }
    }
}
