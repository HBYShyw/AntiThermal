package com.android.server.updates;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkWatchlistManager;
import android.util.Slog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NetworkWatchlistInstallReceiver extends ConfigUpdateInstallReceiver {
    public NetworkWatchlistInstallReceiver() {
        super("/data/misc/network_watchlist/", "network_watchlist.xml", "metadata/", "version");
    }

    @Override // com.android.server.updates.ConfigUpdateInstallReceiver
    protected void postInstall(Context context, Intent intent) {
        try {
            ((NetworkWatchlistManager) context.getSystemService(NetworkWatchlistManager.class)).reloadWatchlist();
        } catch (Exception unused) {
            Slog.wtf("NetworkWatchlistInstallReceiver", "Unable to reload watchlist");
        }
    }
}
