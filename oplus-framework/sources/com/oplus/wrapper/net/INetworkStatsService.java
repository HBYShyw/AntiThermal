package com.oplus.wrapper.net;

import android.net.DataUsageRequest;
import android.net.INetworkStatsService;
import android.net.INetworkStatsSession;
import android.net.Network;
import android.net.NetworkStateSnapshot;
import android.net.NetworkStatsHistory;
import android.net.UnderlyingNetworkInfo;
import android.net.netstats.IUsageCallback;
import android.net.netstats.provider.INetworkStatsProvider;
import android.net.netstats.provider.INetworkStatsProviderCallback;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.oplus.wrapper.net.INetworkStatsSession;

/* loaded from: classes.dex */
public interface INetworkStatsService {
    void forceUpdate() throws RemoteException;

    INetworkStatsSession openSession() throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, INetworkStatsService {
        private final android.net.INetworkStatsService mTarget = new INetworkStatsService.Stub() { // from class: com.oplus.wrapper.net.INetworkStatsService.Stub.1
            public android.net.INetworkStatsSession openSession() throws RemoteException {
                final INetworkStatsSession wrapperNetworkStatsSession = Stub.this.openSession();
                if (wrapperNetworkStatsSession == null) {
                    return null;
                }
                return new INetworkStatsSession.Stub() { // from class: com.oplus.wrapper.net.INetworkStatsService.Stub.1.1
                    public android.net.NetworkStats getDeviceSummaryForNetwork(android.net.NetworkTemplate networkTemplate, long l, long l1) throws RemoteException {
                        return null;
                    }

                    public android.net.NetworkStats getSummaryForNetwork(android.net.NetworkTemplate networkTemplate, long l, long l1) throws RemoteException {
                        return wrapperNetworkStatsSession.getSummaryForNetwork(new NetworkTemplate(networkTemplate), l, l1);
                    }

                    public NetworkStatsHistory getHistoryForNetwork(android.net.NetworkTemplate networkTemplate, int i) throws RemoteException {
                        return null;
                    }

                    public NetworkStatsHistory getHistoryIntervalForNetwork(android.net.NetworkTemplate networkTemplate, int i, long l, long l1) throws RemoteException {
                        return null;
                    }

                    public android.net.NetworkStats getSummaryForAllUid(android.net.NetworkTemplate networkTemplate, long l, long l1, boolean b) throws RemoteException {
                        return null;
                    }

                    public android.net.NetworkStats getTaggedSummaryForAllUid(android.net.NetworkTemplate networkTemplate, long l, long l1) throws RemoteException {
                        return null;
                    }

                    public NetworkStatsHistory getHistoryForUid(android.net.NetworkTemplate networkTemplate, int i, int i1, int i2, int i3) throws RemoteException {
                        return null;
                    }

                    public NetworkStatsHistory getHistoryIntervalForUid(android.net.NetworkTemplate networkTemplate, int i, int i1, int i2, int i3, long l, long l1) throws RemoteException {
                        return null;
                    }

                    public int[] getRelevantUids() throws RemoteException {
                        return new int[0];
                    }

                    public void close() throws RemoteException {
                    }
                };
            }

            public android.net.INetworkStatsSession openSessionForUsageStats(int i, String s) throws RemoteException {
                return null;
            }

            public android.net.NetworkStats getDataLayerSnapshotForUid(int i) throws RemoteException {
                return null;
            }

            public android.net.NetworkStats getUidStatsForTransport(int i) throws RemoteException {
                return null;
            }

            public String[] getMobileIfaces() throws RemoteException {
                return new String[0];
            }

            public void incrementOperationCount(int i, int i1, int i2) throws RemoteException {
            }

            public void notifyNetworkStatus(Network[] networks, NetworkStateSnapshot[] networkStateSnapshots, String s, UnderlyingNetworkInfo[] underlyingNetworkInfos) throws RemoteException {
            }

            public void forceUpdate() throws RemoteException {
                Stub.this.forceUpdate();
            }

            public DataUsageRequest registerUsageCallback(String s, DataUsageRequest dataUsageRequest, IUsageCallback iUsageCallback) throws RemoteException {
                return null;
            }

            public void unregisterUsageRequest(DataUsageRequest dataUsageRequest) throws RemoteException {
            }

            public long getUidStats(int i, int i1) throws RemoteException {
                return 0L;
            }

            public long getIfaceStats(String s, int i) throws RemoteException {
                return 0L;
            }

            public long getTotalStats(int i) throws RemoteException {
                return 0L;
            }

            public INetworkStatsProviderCallback registerNetworkStatsProvider(String s, INetworkStatsProvider iNetworkStatsProvider) throws RemoteException {
                return null;
            }

            public void noteUidForeground(int i, boolean b) throws RemoteException {
            }

            public void advisePersistThreshold(long l) throws RemoteException {
            }

            public void setStatsProviderWarningAndLimitAsync(String s, long l, long l1) throws RemoteException {
            }
        };

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        public static INetworkStatsService asInterface(IBinder obj) {
            return new Proxy(INetworkStatsService.Stub.asInterface(obj));
        }

        /* loaded from: classes.dex */
        private static class Proxy implements INetworkStatsService {
            private final android.net.INetworkStatsService mTarget;

            Proxy(android.net.INetworkStatsService target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.net.INetworkStatsService
            public void forceUpdate() throws RemoteException {
                this.mTarget.forceUpdate();
            }

            @Override // com.oplus.wrapper.net.INetworkStatsService
            public INetworkStatsSession openSession() throws RemoteException {
                final android.net.INetworkStatsSession iNetworkStatsSession = this.mTarget.openSession();
                if (iNetworkStatsSession == null) {
                    return null;
                }
                INetworkStatsSession wrapperNetworkStatsSession = new INetworkStatsSession.Stub() { // from class: com.oplus.wrapper.net.INetworkStatsService.Stub.Proxy.1
                    @Override // com.oplus.wrapper.net.INetworkStatsSession
                    public android.net.NetworkStats getSummaryForNetwork(NetworkTemplate template, long start, long end) throws RemoteException {
                        return iNetworkStatsSession.getSummaryForNetwork(template.getNetworkTemplate(), start, end);
                    }
                };
                return wrapperNetworkStatsSession;
            }
        }
    }
}
