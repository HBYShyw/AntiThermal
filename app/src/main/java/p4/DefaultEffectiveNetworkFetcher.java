package p4;

import java.net.HttpURLConnection;
import java.net.URL;

/* compiled from: DefaultEffectiveNetworkFetcher.java */
/* renamed from: p4.b, reason: use source file name */
/* loaded from: classes.dex */
public class DefaultEffectiveNetworkFetcher implements EffectiveNetworkFetcher {
    @Override // p4.EffectiveNetworkFetcher
    public EffectiveFetchResult a(String str) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        return new DefaultEffectiveFetchResult(httpURLConnection);
    }
}
