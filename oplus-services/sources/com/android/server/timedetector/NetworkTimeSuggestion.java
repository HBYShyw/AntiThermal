package com.android.server.timedetector;

import android.app.time.UnixEpochTime;
import android.os.ShellCommand;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class NetworkTimeSuggestion {
    private ArrayList<String> mDebugInfo;
    private final int mUncertaintyMillis;
    private final UnixEpochTime mUnixEpochTime;

    public NetworkTimeSuggestion(UnixEpochTime unixEpochTime, int i) {
        Objects.requireNonNull(unixEpochTime);
        this.mUnixEpochTime = unixEpochTime;
        if (i < 0) {
            throw new IllegalArgumentException("uncertaintyMillis < 0");
        }
        this.mUncertaintyMillis = i;
    }

    public UnixEpochTime getUnixEpochTime() {
        return this.mUnixEpochTime;
    }

    public int getUncertaintyMillis() {
        return this.mUncertaintyMillis;
    }

    public List<String> getDebugInfo() {
        ArrayList<String> arrayList = this.mDebugInfo;
        return arrayList == null ? Collections.emptyList() : Collections.unmodifiableList(arrayList);
    }

    public void addDebugInfo(String... strArr) {
        if (this.mDebugInfo == null) {
            this.mDebugInfo = new ArrayList<>();
        }
        this.mDebugInfo.addAll(Arrays.asList(strArr));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NetworkTimeSuggestion)) {
            return false;
        }
        NetworkTimeSuggestion networkTimeSuggestion = (NetworkTimeSuggestion) obj;
        return this.mUnixEpochTime.equals(networkTimeSuggestion.mUnixEpochTime) && this.mUncertaintyMillis == networkTimeSuggestion.mUncertaintyMillis;
    }

    public int hashCode() {
        return Objects.hash(this.mUnixEpochTime, Integer.valueOf(this.mUncertaintyMillis));
    }

    public String toString() {
        return "NetworkTimeSuggestion{mUnixEpochTime=" + this.mUnixEpochTime + ", mUncertaintyMillis=" + this.mUncertaintyMillis + ", mDebugInfo=" + this.mDebugInfo + '}';
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0040 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0064 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0071 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0057 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static NetworkTimeSuggestion parseCommandLineArg(ShellCommand shellCommand) throws IllegalArgumentException {
        Long l = null;
        Long l2 = null;
        Integer num = null;
        while (true) {
            String nextArg = shellCommand.getNextArg();
            if (nextArg == null) {
                if (l == null) {
                    throw new IllegalArgumentException("No elapsedRealtimeMillis specified.");
                }
                if (l2 == null) {
                    throw new IllegalArgumentException("No unixEpochTimeMillis specified.");
                }
                if (num == null) {
                    throw new IllegalArgumentException("No uncertaintyMillis specified.");
                }
                NetworkTimeSuggestion networkTimeSuggestion = new NetworkTimeSuggestion(new UnixEpochTime(l.longValue(), l2.longValue()), num.intValue());
                networkTimeSuggestion.addDebugInfo("Command line injection");
                return networkTimeSuggestion;
            }
            char c = 65535;
            switch (nextArg.hashCode()) {
                case 16142561:
                    if (nextArg.equals("--reference_time")) {
                        c = 0;
                    }
                    switch (c) {
                        case 0:
                        case 1:
                            l = Long.valueOf(Long.parseLong(shellCommand.getNextArgRequired()));
                            break;
                        case 2:
                            l2 = Long.valueOf(Long.parseLong(shellCommand.getNextArgRequired()));
                            break;
                        case 3:
                            num = Integer.valueOf(Integer.parseInt(shellCommand.getNextArgRequired()));
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown option: " + nextArg);
                    }
                case 48316014:
                    if (nextArg.equals("--elapsed_realtime")) {
                        c = 1;
                    }
                    switch (c) {
                    }
                    break;
                case 410278458:
                    if (nextArg.equals("--unix_epoch_time")) {
                        c = 2;
                    }
                    switch (c) {
                    }
                    break;
                case 1387445527:
                    if (nextArg.equals("--uncertainty_millis")) {
                        c = 3;
                    }
                    switch (c) {
                    }
                    break;
                default:
                    switch (c) {
                    }
                    break;
            }
        }
    }

    public static void printCommandLineOpts(PrintWriter printWriter) {
        printWriter.printf("%s suggestion options:\n", "Network");
        printWriter.println("  --elapsed_realtime <elapsed realtime millis> - the elapsed realtime millis when unix epoch time was read");
        printWriter.println("  --unix_epoch_time <Unix epoch time millis>");
        printWriter.println("  --uncertainty_millis <Uncertainty millis> - a positive error bound (+/-) estimate for unix epoch time");
        printWriter.println();
        printWriter.println("See " + NetworkTimeSuggestion.class.getName() + " for more information");
    }
}
