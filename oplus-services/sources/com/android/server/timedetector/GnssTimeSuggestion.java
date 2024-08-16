package com.android.server.timedetector;

import android.app.time.UnixEpochTime;
import android.app.timedetector.TimeSuggestionHelper;
import android.os.ShellCommand;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class GnssTimeSuggestion {
    private final TimeSuggestionHelper mTimeSuggestionHelper;

    public GnssTimeSuggestion(UnixEpochTime unixEpochTime) {
        this.mTimeSuggestionHelper = new TimeSuggestionHelper(GnssTimeSuggestion.class, unixEpochTime);
    }

    private GnssTimeSuggestion(TimeSuggestionHelper timeSuggestionHelper) {
        Objects.requireNonNull(timeSuggestionHelper);
        this.mTimeSuggestionHelper = timeSuggestionHelper;
    }

    public UnixEpochTime getUnixEpochTime() {
        return this.mTimeSuggestionHelper.getUnixEpochTime();
    }

    public List<String> getDebugInfo() {
        return this.mTimeSuggestionHelper.getDebugInfo();
    }

    public void addDebugInfo(String... strArr) {
        this.mTimeSuggestionHelper.addDebugInfo(strArr);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || GnssTimeSuggestion.class != obj.getClass()) {
            return false;
        }
        return this.mTimeSuggestionHelper.handleEquals(((GnssTimeSuggestion) obj).mTimeSuggestionHelper);
    }

    public int hashCode() {
        return this.mTimeSuggestionHelper.hashCode();
    }

    public String toString() {
        return this.mTimeSuggestionHelper.handleToString();
    }

    public static GnssTimeSuggestion parseCommandLineArg(ShellCommand shellCommand) throws IllegalArgumentException {
        return new GnssTimeSuggestion(TimeSuggestionHelper.handleParseCommandLineArg(GnssTimeSuggestion.class, shellCommand));
    }

    public static void printCommandLineOpts(PrintWriter printWriter) {
        TimeSuggestionHelper.handlePrintCommandLineOpts(printWriter, "GNSS", GnssTimeSuggestion.class);
    }
}
