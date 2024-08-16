package com.oplus.wrapper.net;

import android.net.INetworkStatsSession;
import android.net.NetworkStatsHistory;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface INetworkStatsSession {
    android.net.NetworkStats getSummaryForNetwork(NetworkTemplate networkTemplate, long j, long j2) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, INetworkStatsSession {
        private final android.net.INetworkStatsSession mTarget = new INetworkStatsSession.Stub() { // from class: com.oplus.wrapper.net.INetworkStatsSession.Stub.1
            public android.net.NetworkStats getDeviceSummaryForNetwork(android.net.NetworkTemplate networkTemplate, long l, long l1) throws RemoteException {
                return null;
            }

            public android.net.NetworkStats getSummaryForNetwork(android.net.NetworkTemplate networkTemplate, long l, long l1) throws RemoteException {
                return Stub.this.getSummaryForNetwork(new NetworkTemplate(networkTemplate), l, l1);
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

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        public static INetworkStatsSession asInterface(IBinder obj) {
            return new Proxy(INetworkStatsSession.Stub.asInterface(obj));
        }

        /* loaded from: classes.dex */
        private static class Proxy implements INetworkStatsSession {
            private final android.net.INetworkStatsSession mTarget;

            Proxy(android.net.INetworkStatsSession target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.net.INetworkStatsSession
            public android.net.NetworkStats getSummaryForNetwork(NetworkTemplate template, long start, long end) throws RemoteException {
                return this.mTarget.getSummaryForNetwork(template.getNetworkTemplate(), start, end);
            }
        }
    }
}
