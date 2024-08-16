package com.android.server.media;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.UserHandle;
import android.util.Log;
import android.util.Slog;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class MediaRoute2ProviderWatcher {
    private final Callback mCallback;
    private final Context mContext;
    private final Handler mHandler;
    private final PackageManager mPackageManager;
    private boolean mRunning;
    private final int mUserId;
    private static final String TAG = "MR2ProviderWatcher";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final PackageManager.ResolveInfoFlags RESOLVE_INFO_FLAGS = PackageManager.ResolveInfoFlags.of(64);
    private final ArrayList<MediaRoute2ProviderServiceProxy> mProxies = new ArrayList<>();
    private final BroadcastReceiver mScanPackagesReceiver = new BroadcastReceiver() { // from class: com.android.server.media.MediaRoute2ProviderWatcher.1
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (MediaRoute2ProviderWatcher.DEBUG) {
                Slog.d(MediaRoute2ProviderWatcher.TAG, "Received package manager broadcast: " + intent);
            }
            MediaRoute2ProviderWatcher.this.postScanPackagesIfNeeded();
        }
    };

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Callback {
        void onAddProviderService(MediaRoute2ProviderServiceProxy mediaRoute2ProviderServiceProxy);

        void onRemoveProviderService(MediaRoute2ProviderServiceProxy mediaRoute2ProviderServiceProxy);
    }

    public MediaRoute2ProviderWatcher(Context context, Callback callback, Handler handler, int i) {
        this.mContext = context;
        this.mCallback = callback;
        this.mHandler = handler;
        this.mUserId = i;
        this.mPackageManager = context.getPackageManager();
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + "MediaRoute2ProviderWatcher");
        String str2 = str + "  ";
        if (this.mProxies.isEmpty()) {
            printWriter.println(str2 + "<no provider service proxies>");
            return;
        }
        Iterator<MediaRoute2ProviderServiceProxy> it = this.mProxies.iterator();
        while (it.hasNext()) {
            it.next().dump(printWriter, str2);
        }
    }

    public void start() {
        if (this.mRunning) {
            return;
        }
        this.mRunning = true;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        intentFilter.addDataScheme("package");
        this.mContext.registerReceiverAsUser(this.mScanPackagesReceiver, new UserHandle(this.mUserId), intentFilter, null, this.mHandler);
        postScanPackagesIfNeeded();
    }

    public void stop() {
        if (this.mRunning) {
            this.mRunning = false;
            this.mContext.unregisterReceiver(this.mScanPackagesReceiver);
            this.mHandler.removeCallbacks(new MediaRoute2ProviderWatcher$$ExternalSyntheticLambda0(this));
            for (int size = this.mProxies.size() - 1; size >= 0; size--) {
                this.mProxies.get(size).stop();
            }
        }
    }

    public void scanPackages() {
        int i;
        if (this.mRunning) {
            int i2 = 0;
            for (ResolveInfo resolveInfo : this.mPackageManager.queryIntentServicesAsUser(new Intent("android.media.MediaRoute2ProviderService"), RESOLVE_INFO_FLAGS, this.mUserId)) {
                ServiceInfo serviceInfo = resolveInfo.serviceInfo;
                if (serviceInfo != null) {
                    Iterator<String> categoriesIterator = resolveInfo.filter.categoriesIterator();
                    boolean z = false;
                    if (categoriesIterator != null) {
                        while (categoriesIterator.hasNext()) {
                            z |= "android.media.MediaRoute2ProviderService.SELF_SCAN_ONLY".equals(categoriesIterator.next());
                        }
                    }
                    int findProvider = findProvider(serviceInfo.packageName, serviceInfo.name);
                    if (findProvider < 0) {
                        MediaRoute2ProviderServiceProxy mediaRoute2ProviderServiceProxy = new MediaRoute2ProviderServiceProxy(this.mContext, new ComponentName(serviceInfo.packageName, serviceInfo.name), z, this.mUserId);
                        mediaRoute2ProviderServiceProxy.start();
                        i = i2 + 1;
                        this.mProxies.add(i2, mediaRoute2ProviderServiceProxy);
                        this.mCallback.onAddProviderService(mediaRoute2ProviderServiceProxy);
                    } else if (findProvider >= i2) {
                        MediaRoute2ProviderServiceProxy mediaRoute2ProviderServiceProxy2 = this.mProxies.get(findProvider);
                        mediaRoute2ProviderServiceProxy2.start();
                        mediaRoute2ProviderServiceProxy2.rebindIfDisconnected();
                        i = i2 + 1;
                        Collections.swap(this.mProxies, findProvider, i2);
                    }
                    i2 = i;
                }
            }
            if (i2 < this.mProxies.size()) {
                for (int size = this.mProxies.size() - 1; size >= i2; size--) {
                    MediaRoute2ProviderServiceProxy mediaRoute2ProviderServiceProxy3 = this.mProxies.get(size);
                    this.mCallback.onRemoveProviderService(mediaRoute2ProviderServiceProxy3);
                    this.mProxies.remove(mediaRoute2ProviderServiceProxy3);
                    mediaRoute2ProviderServiceProxy3.stop();
                }
            }
        }
    }

    private int findProvider(String str, String str2) {
        int size = this.mProxies.size();
        for (int i = 0; i < size; i++) {
            if (this.mProxies.get(i).hasComponentName(str, str2)) {
                return i;
            }
        }
        return -1;
    }

    public void postScanPackagesIfNeeded() {
        if (this.mHandler.hasCallbacks(new MediaRoute2ProviderWatcher$$ExternalSyntheticLambda0(this))) {
            return;
        }
        this.mHandler.post(new MediaRoute2ProviderWatcher$$ExternalSyntheticLambda0(this));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.media.MediaRoute2ProviderWatcher$1 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 extends BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (MediaRoute2ProviderWatcher.DEBUG) {
                Slog.d(MediaRoute2ProviderWatcher.TAG, "Received package manager broadcast: " + intent);
            }
            MediaRoute2ProviderWatcher.this.postScanPackagesIfNeeded();
        }
    }
}
