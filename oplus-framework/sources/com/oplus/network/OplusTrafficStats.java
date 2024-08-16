package com.oplus.network;

import android.net.INetworkStatsService;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.oplus.network.IOplusNetworkStatsService;
import com.oplus.network.stats.AppFreezeConfig;
import com.oplus.network.stats.AppFreezeStatsTotal;
import com.oplus.network.stats.AppFreezeSyncTotal;
import com.oplus.network.stats.IfaceSpeedsValueTotal;
import com.oplus.network.stats.IfaceUidStatsTotal;
import com.oplus.network.stats.SpeedsValueTotal;
import com.oplus.network.stats.StatsValue;
import com.oplus.network.stats.StatsValueTotal;

/* loaded from: classes.dex */
public class OplusTrafficStats {
    private static final String OPLUS_NETWORK_STATS_SERVICE = "oplusnetworkstats";
    private static final int TYPE_TRANS_RX_BYTES = 6;
    private static final int TYPE_TRANS_TX_BYTES = 7;
    private static IOplusNetworkStatsService sOplusStatsService;
    private static INetworkStatsService sStatsService;

    private static synchronized INetworkStatsService getOrigStatsService() {
        INetworkStatsService iNetworkStatsService;
        synchronized (OplusTrafficStats.class) {
            if (sStatsService == null) {
                sStatsService = INetworkStatsService.Stub.asInterface(ServiceManager.getService("netstats"));
            }
            iNetworkStatsService = sStatsService;
        }
        return iNetworkStatsService;
    }

    private static synchronized IOplusNetworkStatsService getOplusStatsService() {
        IOplusNetworkStatsService iOplusNetworkStatsService;
        synchronized (OplusTrafficStats.class) {
            if (sOplusStatsService == null) {
                sOplusStatsService = IOplusNetworkStatsService.Stub.asInterface(ServiceManager.getService(OPLUS_NETWORK_STATS_SERVICE));
            }
            iOplusNetworkStatsService = sOplusStatsService;
        }
        return iOplusNetworkStatsService;
    }

    public static long getTransRxBytes(String iface) {
        try {
            return getOrigStatsService().getIfaceStats(iface, 6);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getTransTxBytes(String iface) {
        try {
            return getOrigStatsService().getIfaceStats(iface, 7);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static StatsValueTotal getUidPurStatsTotal() {
        try {
            return getOplusStatsService().getUidPurStatsTotal();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public static StatsValue getUidPurStats(int uid) {
        try {
            return getOplusStatsService().getUidPurStats(uid);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public static AppFreezeSyncTotal fetchAppFreezeSyns() {
        try {
            return getOplusStatsService().fetchAppFreezeSyns();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public static AppFreezeStatsTotal fetchAppFreezeStats() {
        try {
            return getOplusStatsService().fetchAppFreezeStats();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public static AppFreezeConfig getAppFreezeConfig() {
        try {
            return getOplusStatsService().getAppFreezeConfig();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public static boolean setAppFreezeConfig(AppFreezeConfig cfg) {
        try {
            return getOplusStatsService().setAppFreezeConfig(cfg);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public static SpeedsValueTotal getSocketSpeedsTotal(int clearIntervals, int uploadSpeed, long[] limitCookies) {
        try {
            return getOplusStatsService().getSocketSpeedsTotal(clearIntervals, uploadSpeed, limitCookies);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static IfaceSpeedsValueTotal getUidSpeedsTotal(int clearIntervals) {
        try {
            return getOplusStatsService().getUidSpeedsIfindex(clearIntervals);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static int startBpfSocketSpeedsCalc(int ifindex, boolean localBypass) {
        try {
            return getOplusStatsService().startBpfSocketSpeedsCalc(ifindex, localBypass);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static int stopBpfSocketSpeedsCalc(int ifindex) {
        try {
            return getOplusStatsService().stopBpfSocketSpeedsCalc(ifindex);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static int setBpfSocketSpeedsConfig(int speedsCalcInv) {
        try {
            return getOplusStatsService().setBpfSocketSpeedsConfig(speedsCalcInv);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static int getSocketIsLocal(long socketCookie) {
        try {
            return getOplusStatsService().getSocketIsLocal(socketCookie);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static StatsValueTotal getUidStatsTotal() {
        try {
            return getOplusStatsService().getUidStatsTotal();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static IfaceUidStatsTotal getIfaceUidStatsTotal() {
        try {
            return getOplusStatsService().getIfnameUidStatsTotal();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public static StatsValue getIfaceStats(String ifname) {
        try {
            return getOplusStatsService().getIfaceStats(ifname);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public static int getDenyFlag(int uid) {
        try {
            return getOplusStatsService().getDenyFlag(uid);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }
}
