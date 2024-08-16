package q9;

import android.net.INetworkStatsSession;
import android.net.NetworkStatsHistory;
import android.net.NetworkTemplate;
import android.os.Bundle;

/* compiled from: NetWork.java */
/* renamed from: q9.i, reason: use source file name */
/* loaded from: classes2.dex */
public class NetWork {

    /* renamed from: a, reason: collision with root package name */
    private final INetworkStatsSession f17055a;

    /* renamed from: b, reason: collision with root package name */
    private final Bundle f17056b;

    /* renamed from: c, reason: collision with root package name */
    private long f17057c = 0;

    public NetWork(INetworkStatsSession iNetworkStatsSession, Bundle bundle) {
        this.f17055a = iNetworkStatsSession;
        this.f17056b = bundle;
    }

    public static Bundle a(NetworkTemplate networkTemplate, int i10) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("template", networkTemplate);
        bundle.putInt("fields", i10);
        return bundle;
    }

    private NetworkStatsHistory b(NetworkTemplate networkTemplate, int i10, int i11, NetworkStatsHistory networkStatsHistory) {
        NetworkStatsHistory historyForUid = this.f17055a.getHistoryForUid(networkTemplate, i10, i11, 0, 30);
        if (networkStatsHistory == null) {
            return historyForUid;
        }
        networkStatsHistory.recordEntireHistory(historyForUid);
        return networkStatsHistory;
    }

    public NetworkStatsHistory.Entry c(int i10) {
        NetworkTemplate networkTemplate = (NetworkTemplate) this.f17056b.getParcelable("template");
        int i11 = this.f17056b.getInt("fields");
        long currentTimeMillis = System.currentTimeMillis();
        this.f17055a.getHistoryForNetwork(networkTemplate, i11);
        b(networkTemplate, i10, 0, null);
        b(networkTemplate, i10, 1, null);
        NetworkStatsHistory b10 = b(networkTemplate, i10, -1, null);
        NetworkStatsHistory networkStatsHistory = new NetworkStatsHistory(b10.getBucketDuration());
        networkStatsHistory.recordEntireHistory(b10);
        return networkStatsHistory.getValues(0L, currentTimeMillis, (NetworkStatsHistory.Entry) null);
    }

    public NetworkStatsHistory.Entry d(int i10, long j10, long j11) {
        NetworkTemplate networkTemplate = (NetworkTemplate) this.f17056b.getParcelable("template");
        this.f17055a.getHistoryForNetwork(networkTemplate, this.f17056b.getInt("fields"));
        b(networkTemplate, i10, 0, null);
        b(networkTemplate, i10, 1, null);
        NetworkStatsHistory b10 = b(networkTemplate, i10, -1, null);
        NetworkStatsHistory networkStatsHistory = new NetworkStatsHistory(b10.getBucketDuration());
        networkStatsHistory.recordEntireHistory(b10);
        return networkStatsHistory.getValues(j10, j11, (NetworkStatsHistory.Entry) null);
    }
}
