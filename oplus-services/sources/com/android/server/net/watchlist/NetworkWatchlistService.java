package com.android.server.net.watchlist;

import android.content.Context;
import android.net.IIpConnectivityMetrics;
import android.net.INetdEventCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ShellCallback;
import android.provider.Settings;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.net.INetworkWatchlistManager;
import com.android.internal.util.DumpUtils;
import com.android.server.ServiceThread;
import com.android.server.SystemService;
import com.android.server.net.BaseNetdEventCallback;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NetworkWatchlistService extends INetworkWatchlistManager.Stub {
    static final boolean DEBUG = false;
    private static final int MAX_NUM_OF_WATCHLIST_DIGESTS = 10000;
    private static final String TAG = NetworkWatchlistService.class.getSimpleName();
    private final Context mContext;
    private final ServiceThread mHandlerThread;

    @VisibleForTesting
    IIpConnectivityMetrics mIpConnectivityMetrics;

    @VisibleForTesting
    WatchlistLoggingHandler mNetworkWatchlistHandler;

    @GuardedBy({"mLoggingSwitchLock"})
    private volatile boolean mIsLoggingEnabled = false;
    private final Object mLoggingSwitchLock = new Object();
    private final INetdEventCallback mNetdEventCallback = new BaseNetdEventCallback() { // from class: com.android.server.net.watchlist.NetworkWatchlistService.1
        public void onDnsEvent(int i, int i2, int i3, String str, String[] strArr, int i4, long j, int i5) {
            if (NetworkWatchlistService.this.mIsLoggingEnabled) {
                NetworkWatchlistService.this.mNetworkWatchlistHandler.asyncNetworkEvent(str, strArr, i5);
            }
        }

        public void onConnectEvent(String str, int i, long j, int i2) {
            if (NetworkWatchlistService.this.mIsLoggingEnabled) {
                NetworkWatchlistService.this.mNetworkWatchlistHandler.asyncNetworkEvent(null, new String[]{str}, i2);
            }
        }
    };
    private final WatchlistConfig mConfig = WatchlistConfig.getInstance();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Lifecycle extends SystemService {
        private NetworkWatchlistService mService;

        public Lifecycle(Context context) {
            super(context);
        }

        /* JADX WARN: Type inference failed for: r0v3, types: [com.android.server.net.watchlist.NetworkWatchlistService, android.os.IBinder] */
        public void onStart() {
            if (Settings.Global.getInt(getContext().getContentResolver(), "network_watchlist_enabled", 1) == 0) {
                Slog.i(NetworkWatchlistService.TAG, "Network Watchlist service is disabled");
                return;
            }
            ?? networkWatchlistService = new NetworkWatchlistService(getContext());
            this.mService = networkWatchlistService;
            publishBinderService("network_watchlist", (IBinder) networkWatchlistService);
        }

        public void onBootPhase(int i) {
            if (i == 550) {
                if (Settings.Global.getInt(getContext().getContentResolver(), "network_watchlist_enabled", 1) == 0) {
                    Slog.i(NetworkWatchlistService.TAG, "Network Watchlist service is disabled");
                    return;
                }
                try {
                    this.mService.init();
                    this.mService.initIpConnectivityMetrics();
                    this.mService.startWatchlistLogging();
                } catch (RemoteException unused) {
                }
                ReportWatchlistJobService.schedule(getContext());
            }
        }
    }

    public NetworkWatchlistService(Context context) {
        this.mContext = context;
        ServiceThread serviceThread = new ServiceThread(TAG, 10, false);
        this.mHandlerThread = serviceThread;
        serviceThread.start();
        WatchlistLoggingHandler watchlistLoggingHandler = new WatchlistLoggingHandler(context, serviceThread.getLooper());
        this.mNetworkWatchlistHandler = watchlistLoggingHandler;
        watchlistLoggingHandler.reportWatchlistIfNecessary();
    }

    @VisibleForTesting
    NetworkWatchlistService(Context context, ServiceThread serviceThread, WatchlistLoggingHandler watchlistLoggingHandler, IIpConnectivityMetrics iIpConnectivityMetrics) {
        this.mContext = context;
        this.mHandlerThread = serviceThread;
        this.mNetworkWatchlistHandler = watchlistLoggingHandler;
        this.mIpConnectivityMetrics = iIpConnectivityMetrics;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        this.mConfig.removeTestModeConfig();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initIpConnectivityMetrics() {
        this.mIpConnectivityMetrics = IIpConnectivityMetrics.Stub.asInterface(ServiceManager.getService("connmetrics"));
    }

    private boolean isCallerShell() {
        int callingUid = Binder.getCallingUid();
        return callingUid == 2000 || callingUid == 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
        if (!isCallerShell()) {
            Slog.w(TAG, "Only shell is allowed to call network watchlist shell commands");
        } else {
            new NetworkWatchlistShellCommand(this, this.mContext).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }
    }

    @VisibleForTesting
    protected boolean startWatchlistLoggingImpl() throws RemoteException {
        synchronized (this.mLoggingSwitchLock) {
            if (this.mIsLoggingEnabled) {
                Slog.w(TAG, "Watchlist logging is already running");
                return true;
            }
            try {
                if (!this.mIpConnectivityMetrics.addNetdEventCallback(2, this.mNetdEventCallback)) {
                    return false;
                }
                this.mIsLoggingEnabled = true;
                return true;
            } catch (RemoteException unused) {
                return false;
            }
        }
    }

    public boolean startWatchlistLogging() throws RemoteException {
        enforceWatchlistLoggingPermission();
        return startWatchlistLoggingImpl();
    }

    @VisibleForTesting
    protected boolean stopWatchlistLoggingImpl() {
        synchronized (this.mLoggingSwitchLock) {
            if (!this.mIsLoggingEnabled) {
                Slog.w(TAG, "Watchlist logging is not running");
                return true;
            }
            this.mIsLoggingEnabled = false;
            try {
                return this.mIpConnectivityMetrics.removeNetdEventCallback(2);
            } catch (RemoteException unused) {
                return false;
            }
        }
    }

    public boolean stopWatchlistLogging() throws RemoteException {
        enforceWatchlistLoggingPermission();
        return stopWatchlistLoggingImpl();
    }

    public byte[] getWatchlistConfigHash() {
        return this.mConfig.getWatchlistConfigHash();
    }

    private void enforceWatchlistLoggingPermission() {
        int callingUid = Binder.getCallingUid();
        if (callingUid != 1000) {
            throw new SecurityException(String.format("Uid %d has no permission to change watchlist setting.", Integer.valueOf(callingUid)));
        }
    }

    public void reloadWatchlist() throws RemoteException {
        enforceWatchlistLoggingPermission();
        Slog.i(TAG, "Reloading watchlist");
        this.mConfig.reloadConfig();
    }

    public void reportWatchlistIfNecessary() {
        this.mNetworkWatchlistHandler.reportWatchlistIfNecessary();
    }

    public boolean forceReportWatchlistForTest(long j) {
        if (this.mConfig.isConfigSecure()) {
            return false;
        }
        this.mNetworkWatchlistHandler.forceReportWatchlistForTest(j);
        return true;
    }

    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            this.mConfig.dump(fileDescriptor, printWriter, strArr);
        }
    }
}
