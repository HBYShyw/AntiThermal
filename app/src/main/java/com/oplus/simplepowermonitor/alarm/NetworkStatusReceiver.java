package com.oplus.simplepowermonitor.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import b6.LocalLog;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class NetworkStatusReceiver extends BroadcastReceiver {

    /* renamed from: b, reason: collision with root package name */
    public static boolean f10476b = false;

    /* renamed from: c, reason: collision with root package name */
    public static boolean f10477c = false;

    /* renamed from: a, reason: collision with root package name */
    public ConcurrentHashMap<String, Object> f10478a = new ConcurrentHashMap<>();

    public void a() {
    }

    public void b() {
    }

    public void c() {
    }

    public void d() {
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        LocalLog.d("NetworkStatusReceiver", "NetworkStatusReceiver onReceive");
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            LocalLog.d("NetworkStatusReceiver", "CONNECTIVITY_ACTION");
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.isConnected()) {
                    if (activeNetworkInfo.getType() == 1) {
                        LocalLog.b("NetworkStatusReceiver", "The current WiFi connection is available ");
                        f10476b = true;
                        d();
                        return;
                    } else {
                        if (activeNetworkInfo.getType() == 0) {
                            LocalLog.b("NetworkStatusReceiver", "Current mobile network connection is available ");
                            f10477c = true;
                            c();
                            return;
                        }
                        return;
                    }
                }
                return;
            }
            LocalLog.b("NetworkStatusReceiver", "There is currently no network connection, please make sure you have turned on the network ");
            if (f10476b) {
                LocalLog.b("NetworkStatusReceiver", "There is currently no wifi, please make sure you have turned on wifi");
                f10476b = false;
                b();
            }
            if (f10477c) {
                LocalLog.b("NetworkStatusReceiver", "There is currently no mobile, please make sure you have turned on mobile");
                f10477c = false;
                a();
            }
        }
    }
}
