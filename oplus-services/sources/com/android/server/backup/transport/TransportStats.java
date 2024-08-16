package com.android.server.backup.transport;

import android.content.ComponentName;
import com.android.server.backup.transport.TransportStats;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class TransportStats {
    private final Object mStatsLock = new Object();
    private final Map<ComponentName, Stats> mTransportStats = new HashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerConnectionTime(ComponentName componentName, long j) {
        synchronized (this.mStatsLock) {
            Stats stats = this.mTransportStats.get(componentName);
            if (stats == null) {
                stats = new Stats();
                this.mTransportStats.put(componentName, stats);
            }
            stats.register(j);
        }
    }

    public Stats getStatsForTransport(ComponentName componentName) {
        synchronized (this.mStatsLock) {
            Stats stats = this.mTransportStats.get(componentName);
            if (stats == null) {
                return null;
            }
            return new Stats(stats);
        }
    }

    public void dump(PrintWriter printWriter) {
        synchronized (this.mStatsLock) {
            Optional<Stats> reduce = this.mTransportStats.values().stream().reduce(new BinaryOperator() { // from class: com.android.server.backup.transport.TransportStats$$ExternalSyntheticLambda0
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    return TransportStats.Stats.merge((TransportStats.Stats) obj, (TransportStats.Stats) obj2);
                }
            });
            if (reduce.isPresent()) {
                dumpStats(printWriter, "", reduce.get());
            }
            if (!this.mTransportStats.isEmpty()) {
                printWriter.println("Per transport:");
                for (ComponentName componentName : this.mTransportStats.keySet()) {
                    Stats stats = this.mTransportStats.get(componentName);
                    printWriter.println("    " + componentName.flattenToShortString());
                    dumpStats(printWriter, "        ", stats);
                }
            }
        }
    }

    private static void dumpStats(PrintWriter printWriter, String str, Stats stats) {
        Locale locale = Locale.US;
        printWriter.println(String.format(locale, "%sAverage connection time: %.2f ms", str, Double.valueOf(stats.average)));
        printWriter.println(String.format(locale, "%sMax connection time: %d ms", str, Long.valueOf(stats.max)));
        printWriter.println(String.format(locale, "%sMin connection time: %d ms", str, Long.valueOf(stats.min)));
        printWriter.println(String.format(locale, "%sNumber of connections: %d ", str, Integer.valueOf(stats.n)));
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class Stats {
        public double average;
        public long max;
        public long min;
        public int n;

        public static Stats merge(Stats stats, Stats stats2) {
            int i = stats.n;
            int i2 = stats2.n;
            return new Stats(i + i2, ((stats.average * i) + (stats2.average * i2)) / (i + i2), Math.max(stats.max, stats2.max), Math.min(stats.min, stats2.min));
        }

        public Stats() {
            this.n = 0;
            this.average = 0.0d;
            this.max = 0L;
            this.min = Long.MAX_VALUE;
        }

        private Stats(int i, double d, long j, long j2) {
            this.n = i;
            this.average = d;
            this.max = j;
            this.min = j2;
        }

        private Stats(Stats stats) {
            this(stats.n, stats.average, stats.max, stats.min);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void register(long j) {
            double d = this.average;
            int i = this.n;
            this.average = ((d * i) + j) / (i + 1);
            this.n = i + 1;
            this.max = Math.max(this.max, j);
            this.min = Math.min(this.min, j);
        }
    }
}
