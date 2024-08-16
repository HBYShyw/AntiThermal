package android.net;

/* loaded from: classes.dex */
public class NetworkStatsHistoryExtImpl {
    public static long hookRecordHistory(long val, long def) {
        return val < 0 ? def : val;
    }
}
