package com.android.server.om;

import android.os.FabricatedOverlayInfo;
import android.os.FabricatedOverlayInternal;
import android.os.IBinder;
import android.os.IIdmap2;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemService;
import android.text.TextUtils;
import android.util.Slog;
import com.android.server.FgThread;
import com.android.server.job.controllers.JobStatus;
import com.android.server.om.IdmapDaemon;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class IdmapDaemon {
    private static final String IDMAP_DAEMON = "idmap2d";
    private static final int SERVICE_CONNECT_INTERVAL_SLEEP_MS = 5;
    private static final int SERVICE_CONNECT_TIMEOUT_MS = 5000;
    private static final int SERVICE_TIMEOUT_MS = 10000;
    private static IdmapDaemon sInstance;
    private volatile IIdmap2 mService;
    private final AtomicInteger mOpenedCount = new AtomicInteger();
    private final Object mIdmapToken = new Object();

    IdmapDaemon() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class Connection implements AutoCloseable {
        private final IIdmap2 mIdmap2;
        private boolean mOpened;

        private Connection(IIdmap2 iIdmap2) {
            this.mOpened = true;
            synchronized (IdmapDaemon.this.mIdmapToken) {
                IdmapDaemon.this.mOpenedCount.incrementAndGet();
                this.mIdmap2 = iIdmap2;
            }
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            synchronized (IdmapDaemon.this.mIdmapToken) {
                if (this.mOpened) {
                    this.mOpened = false;
                    if (IdmapDaemon.this.mOpenedCount.decrementAndGet() != 0) {
                        return;
                    }
                    FgThread.getHandler().postDelayed(new Runnable() { // from class: com.android.server.om.IdmapDaemon$Connection$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            IdmapDaemon.Connection.this.lambda$close$0();
                        }
                    }, IdmapDaemon.this.mIdmapToken, JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$close$0() {
            synchronized (IdmapDaemon.this.mIdmapToken) {
                if (IdmapDaemon.this.mService != null && IdmapDaemon.this.mOpenedCount.get() == 0) {
                    IdmapDaemon.stopIdmapService();
                    IdmapDaemon.this.mService = null;
                }
            }
        }

        public IIdmap2 getIdmap2() {
            return this.mIdmap2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IdmapDaemon getInstance() {
        if (sInstance == null) {
            sInstance = new IdmapDaemon();
        }
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String createIdmap(String str, String str2, String str3, int i, boolean z, int i2) throws TimeoutException, RemoteException {
        Connection connect = connect();
        try {
            IIdmap2 idmap2 = connect.getIdmap2();
            if (idmap2 == null) {
                Slog.w("OverlayManager", "idmap2d service is not ready for createIdmap(\"" + str + "\", \"" + str2 + "\", \"" + str3 + "\", " + i + ", " + z + ", " + i2 + ")");
                connect.close();
                return null;
            }
            String createIdmap = idmap2.createIdmap(str, str2, TextUtils.emptyIfNull(str3), i, z, i2);
            connect.close();
            return createIdmap;
        } catch (Throwable th) {
            if (connect != null) {
                try {
                    connect.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removeIdmap(String str, int i) throws TimeoutException, RemoteException {
        Connection connect = connect();
        try {
            IIdmap2 idmap2 = connect.getIdmap2();
            if (idmap2 == null) {
                Slog.w("OverlayManager", "idmap2d service is not ready for removeIdmap(\"" + str + "\", " + i + ")");
                connect.close();
                return false;
            }
            boolean removeIdmap = idmap2.removeIdmap(str, i);
            connect.close();
            return removeIdmap;
        } catch (Throwable th) {
            if (connect != null) {
                try {
                    connect.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean verifyIdmap(String str, String str2, String str3, int i, boolean z, int i2) throws Exception {
        Connection connect = connect();
        try {
            IIdmap2 idmap2 = connect.getIdmap2();
            if (idmap2 == null) {
                Slog.w("OverlayManager", "idmap2d service is not ready for verifyIdmap(\"" + str + "\", \"" + str2 + "\", \"" + str3 + "\", " + i + ", " + z + ", " + i2 + ")");
                connect.close();
                return false;
            }
            boolean verifyIdmap = idmap2.verifyIdmap(str, str2, TextUtils.emptyIfNull(str3), i, z, i2);
            connect.close();
            return verifyIdmap;
        } catch (Throwable th) {
            if (connect != null) {
                try {
                    connect.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean idmapExists(String str, int i) {
        try {
            Connection connect = connect();
            try {
                IIdmap2 idmap2 = connect.getIdmap2();
                if (idmap2 == null) {
                    Slog.w("OverlayManager", "idmap2d service is not ready for idmapExists(\"" + str + "\", " + i + ")");
                    connect.close();
                    return false;
                }
                boolean isFile = new File(idmap2.getIdmapPath(str, i)).isFile();
                connect.close();
                return isFile;
            } finally {
            }
        } catch (Exception e) {
            Slog.wtf("OverlayManager", "failed to check if idmap exists for " + str, e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FabricatedOverlayInfo createFabricatedOverlay(FabricatedOverlayInternal fabricatedOverlayInternal) {
        try {
            Connection connect = connect();
            try {
                IIdmap2 idmap2 = connect.getIdmap2();
                if (idmap2 == null) {
                    Slog.w("OverlayManager", "idmap2d service is not ready for createFabricatedOverlay()");
                    connect.close();
                    return null;
                }
                FabricatedOverlayInfo createFabricatedOverlay = idmap2.createFabricatedOverlay(fabricatedOverlayInternal);
                connect.close();
                return createFabricatedOverlay;
            } finally {
            }
        } catch (Exception e) {
            Slog.wtf("OverlayManager", "failed to fabricate overlay " + fabricatedOverlayInternal, e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean deleteFabricatedOverlay(String str) {
        try {
            Connection connect = connect();
            try {
                IIdmap2 idmap2 = connect.getIdmap2();
                if (idmap2 == null) {
                    Slog.w("OverlayManager", "idmap2d service is not ready for deleteFabricatedOverlay(\"" + str + "\")");
                    connect.close();
                    return false;
                }
                boolean deleteFabricatedOverlay = idmap2.deleteFabricatedOverlay(str);
                connect.close();
                return deleteFabricatedOverlay;
            } finally {
            }
        } catch (Exception e) {
            Slog.wtf("OverlayManager", "failed to delete fabricated overlay '" + str + "'", e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0073 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public synchronized List<FabricatedOverlayInfo> getFabricatedOverlayInfos() {
        int i;
        ArrayList arrayList = new ArrayList();
        Connection connection = null;
        try {
            try {
                connection = connect();
                IIdmap2 idmap2 = connection.getIdmap2();
                if (idmap2 == null) {
                    Slog.w("OverlayManager", "idmap2d service is not ready for getFabricatedOverlayInfos()");
                    List<FabricatedOverlayInfo> emptyList = Collections.emptyList();
                    try {
                        connection.getIdmap2();
                    } catch (RemoteException unused) {
                    }
                    connection.close();
                    return emptyList;
                }
                i = idmap2.acquireFabricatedOverlayIterator();
                while (true) {
                    try {
                        List nextFabricatedOverlayInfos = idmap2.nextFabricatedOverlayInfos(i);
                        if (nextFabricatedOverlayInfos.isEmpty()) {
                            try {
                                break;
                            } catch (RemoteException unused2) {
                            }
                        } else {
                            arrayList.addAll(nextFabricatedOverlayInfos);
                        }
                    } catch (Exception e) {
                        e = e;
                        Slog.wtf("OverlayManager", "failed to get all fabricated overlays", e);
                        if (connection != null) {
                            try {
                                if (connection.getIdmap2() != null && i != -1) {
                                    connection.getIdmap2().releaseFabricatedOverlayIterator(i);
                                }
                            } catch (RemoteException unused3) {
                            }
                            connection.close();
                        }
                        return arrayList;
                    }
                }
                if (connection.getIdmap2() != null && i != -1) {
                    connection.getIdmap2().releaseFabricatedOverlayIterator(i);
                }
                connection.close();
                return arrayList;
            } catch (Exception e2) {
                e = e2;
                i = -1;
            } catch (Throwable th) {
                th = th;
                if (0 != 0) {
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            if (0 != 0) {
                try {
                    if (connection.getIdmap2() != null && -1 != -1) {
                        connection.getIdmap2().releaseFabricatedOverlayIterator(-1);
                    }
                } catch (RemoteException unused4) {
                }
                connection.close();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String dumpIdmap(String str) {
        try {
            Connection connect = connect();
            try {
                IIdmap2 idmap2 = connect.getIdmap2();
                if (idmap2 == null) {
                    Slog.w("OverlayManager", "idmap2d service is not ready for dumpIdmap()");
                    connect.close();
                    return "idmap2d service is not ready for dumpIdmap()";
                }
                String nullIfEmpty = TextUtils.nullIfEmpty(idmap2.dumpIdmap(str));
                connect.close();
                return nullIfEmpty;
            } finally {
            }
        } catch (Exception e) {
            Slog.wtf("OverlayManager", "failed to dump idmap", e);
            return null;
        }
    }

    private IBinder getIdmapService() throws TimeoutException, RemoteException {
        try {
            SystemService.start(IDMAP_DAEMON);
        } catch (RuntimeException e) {
            Slog.wtf("OverlayManager", "Failed to enable idmap2 daemon", e);
            if (e.getMessage().contains("failed to set system property")) {
                return null;
            }
        }
        long uptimeMillis = SystemClock.uptimeMillis() + 5000;
        do {
            IBinder service = ServiceManager.getService("idmap");
            if (service != null) {
                service.linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.om.IdmapDaemon$$ExternalSyntheticLambda0
                    @Override // android.os.IBinder.DeathRecipient
                    public final void binderDied() {
                        IdmapDaemon.lambda$getIdmapService$0();
                    }
                }, 0);
                return service;
            }
            SystemClock.sleep(5L);
        } while (SystemClock.uptimeMillis() <= uptimeMillis);
        throw new TimeoutException(String.format("Failed to connect to '%s' in %d milliseconds", "idmap", 5000));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getIdmapService$0() {
        Slog.w("OverlayManager", String.format("service '%s' died", "idmap"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void stopIdmapService() {
        try {
            SystemService.stop(IDMAP_DAEMON);
        } catch (RuntimeException e) {
            Slog.w("OverlayManager", "Failed to disable idmap2 daemon", e);
        }
    }

    private Connection connect() throws TimeoutException, RemoteException {
        synchronized (this.mIdmapToken) {
            FgThread.getHandler().removeCallbacksAndMessages(this.mIdmapToken);
            ConnectionIA connectionIA = null;
            if (this.mService != null) {
                return new Connection(this.mService);
            }
            IBinder idmapService = getIdmapService();
            if (idmapService == null) {
                return new Connection(connectionIA);
            }
            this.mService = IIdmap2.Stub.asInterface(idmapService);
            return new Connection(this.mService);
        }
    }
}
