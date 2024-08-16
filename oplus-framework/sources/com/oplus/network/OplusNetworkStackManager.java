package com.oplus.network;

import android.content.Context;
import android.net.Network;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import com.oplus.network.IOplusNetScoreChange;
import com.oplus.network.IOplusNetworkStack;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.WeakHashMap;

/* loaded from: classes.dex */
public class OplusNetworkStackManager {
    private static final int CACHE_INTERVAL = 1500;
    public static final boolean DBG = true;
    private static final long DELAY_REGISTER_TIME = 10000;
    private static final int INVAL_SCORE = -50;
    public static final String LOG_TAG = "OplusNetworkStackManager";
    private static final int MAX_CACHE_COUNT = 10;
    public static final int MAX_URL_LEN = 127;
    private static final int RECONNECT_NETWORK_SERVICE = 1;
    private static final long RECONNECT_SERVICE = 5000;
    public static final String SRV_NAME = "oplusnetworkstack";
    private static ArrayList<INetworkScoreCallback> allCallbacks = new ArrayList<>();
    private static OplusNetworkStackManager sInstance;
    public Context mContext;
    private Handler mHandler;
    private IOplusNetworkStack mNetworkStackService;
    private boolean showDebug = SystemProperties.getBoolean("persist.oplus.network.stack.show", false);
    private WeakHashMap<Network, NetworkScore> mNetworkScoreMap = new WeakHashMap<>();

    /* loaded from: classes.dex */
    public interface INetworkScoreCallback {
        void onNetworkQualityChange(int i, int i2, int i3, boolean z, int i4, int i5);

        default void onNetworkKPIChange(OplusNetworkKPI networkKPI) {
        }
    }

    public static void registerTcpCallback(INetworkScoreCallback nsc) {
        Log.e(LOG_TAG, "registerTcpCallback nsc " + nsc);
        synchronized (allCallbacks) {
            if (!allCallbacks.contains(nsc)) {
                allCallbacks.add(nsc);
            }
        }
    }

    public static void unregisterTcpCallback(INetworkScoreCallback nsc) {
        Log.e(LOG_TAG, "unregisterTcpCallback nsc " + nsc);
        synchronized (allCallbacks) {
            if (allCallbacks.contains(nsc)) {
                allCallbacks.remove(nsc);
            }
        }
    }

    public void startTest() {
        IOplusNetworkStack asInterface = IOplusNetworkStack.Stub.asInterface(ServiceManager.getService(SRV_NAME));
        this.mNetworkStackService = asInterface;
        if (asInterface != null) {
            try {
                Log.e(LOG_TAG, "registerTcpScoreChange start!");
                this.mNetworkStackService.registerTcpScoreChange(new MyCallBack());
                return;
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "registerTcpScoreChange fail!");
                return;
            }
        }
        Log.e(LOG_TAG, "mNetworkStackService is null!");
    }

    public String getOplusNetworkStackInfo() {
        this.mNetworkStackService = IOplusNetworkStack.Stub.asInterface(ServiceManager.getService(SRV_NAME));
        return "";
    }

    public void setOplusNetworkStackConfig(String config) {
        this.mNetworkStackService = IOplusNetworkStack.Stub.asInterface(ServiceManager.getService(SRV_NAME));
    }

    public int getFailRate(Network network) {
        this.mNetworkStackService = IOplusNetworkStack.Stub.asInterface(ServiceManager.getService(SRV_NAME));
        Log.e(LOG_TAG, "getFailRate is error rerurn 0!");
        return 0;
    }

    private IOplusNetworkStack getStackService() {
        if (this.mNetworkStackService == null) {
            this.mNetworkStackService = IOplusNetworkStack.Stub.asInterface(ServiceManager.getService(SRV_NAME));
        }
        return this.mNetworkStackService;
    }

    public int getNetworkScore(Network network) {
        try {
            if (network == null) {
                Log.e(LOG_TAG, "network is null");
                return -1;
            }
            int score = queryScoreFromCache(network);
            if (score == -50) {
                score = this.mNetworkStackService.getNetworkScore(network);
                addScoreToCache(network, score);
            }
            Log.d(LOG_TAG, "getNetworkScore " + network + " return " + score);
            return score;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "getNetworkScore exception: " + e.getMessage());
            return -1;
        }
    }

    public int getNetworkRtt(Network network) {
        try {
            if (network == null) {
                Log.e(LOG_TAG, "network is null");
                return -1;
            }
            int rtt = this.mNetworkStackService.getNetworkRtt(network);
            Log.d(LOG_TAG, "getNetworkRtt " + network + " return " + rtt);
            return rtt;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "getNetworkRtt exception: " + e.getMessage());
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class MyCallBack extends IOplusNetScoreChange.Stub {
        private MyCallBack() {
        }

        @Override // com.oplus.network.IOplusNetScoreChange
        public void networkScoreChange(int networkId, int oldScore, int newScore, boolean better, int dnsScore, int tcpScore) throws RemoteException {
            synchronized (OplusNetworkStackManager.allCallbacks) {
                Log.e(OplusNetworkStackManager.LOG_TAG, "callback len = " + OplusNetworkStackManager.allCallbacks.size());
                Iterator it = OplusNetworkStackManager.allCallbacks.iterator();
                while (it.hasNext()) {
                    INetworkScoreCallback scorechange = (INetworkScoreCallback) it.next();
                    scorechange.onNetworkQualityChange(networkId, oldScore, newScore, better, dnsScore, tcpScore);
                }
            }
        }

        @Override // com.oplus.network.IOplusNetScoreChange
        public void networkKPIChange(OplusNetworkKPI networkKPI) {
            synchronized (OplusNetworkStackManager.allCallbacks) {
                Iterator it = OplusNetworkStackManager.allCallbacks.iterator();
                while (it.hasNext()) {
                    INetworkScoreCallback scorechange = (INetworkScoreCallback) it.next();
                    scorechange.onNetworkKPIChange(networkKPI);
                }
            }
        }
    }

    public static OplusNetworkStackManager getInstance(Context c) {
        OplusNetworkStackManager oplusNetworkStackManager;
        synchronized (OplusNetworkStackManager.class) {
            if (sInstance == null) {
                sInstance = new OplusNetworkStackManager(c);
                Log.e(LOG_TAG, "OplusNetworkStackManager first new!");
            }
            oplusNetworkStackManager = sInstance;
        }
        return oplusNetworkStackManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerScoreChangeInit() {
        IBinder iBinder = ServiceManager.getService(SRV_NAME);
        IOplusNetworkStack asInterface = IOplusNetworkStack.Stub.asInterface(iBinder);
        this.mNetworkStackService = asInterface;
        if (asInterface != null) {
            try {
                Log.e(LOG_TAG, "registerTcpScoreChange start!");
                this.mNetworkStackService.registerTcpScoreChange(new MyCallBack());
                iBinder.linkToDeath(new RegManagerDeathRecipient(), 0);
                return;
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "registerTcpScoreChange fail!");
                return;
            }
        }
        Log.e(LOG_TAG, "mNetworkStackService is null!");
        this.mHandler.postDelayed(new Runnable() { // from class: com.oplus.network.OplusNetworkStackManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                OplusNetworkStackManager.this.registerScoreChangeInit();
            }
        }, DELAY_REGISTER_TIME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class RegManagerDeathRecipient implements IBinder.DeathRecipient {
        RegManagerDeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.e(OplusNetworkStackManager.LOG_TAG, "Network service died.");
            Handler handler = OplusNetworkStackManager.this.mHandler;
            final OplusNetworkStackManager oplusNetworkStackManager = OplusNetworkStackManager.this;
            handler.postDelayed(new Runnable() { // from class: com.oplus.network.OplusNetworkStackManager$RegManagerDeathRecipient$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    OplusNetworkStackManager.this.registerScoreChangeInit();
                }
            }, OplusNetworkStackManager.RECONNECT_SERVICE);
        }
    }

    protected OplusNetworkStackManager(Context context) {
        this.mContext = context;
        HandlerThread thread = new HandlerThread(LOG_TAG);
        thread.start();
        this.mHandler = new Handler(thread.getLooper());
        registerScoreChangeInit();
    }

    private int parseIpStr(String ipstr) {
        String[] ip = ipstr.split("\\.");
        return (Integer.parseInt(ip[0]) << 24) + (Integer.parseInt(ip[1]) << 16) + (Integer.parseInt(ip[2]) << 8) + Integer.parseInt(ip[3]);
    }

    public boolean oplusAddAppDnsConfig(String[] hostname, String[] ipaddr) {
        if (hostname != null) {
            try {
                if (hostname.length != 0 && ipaddr != null && ipaddr.length != 0 && hostname.length == ipaddr.length) {
                    for (String url : hostname) {
                        if (url == null) {
                            Log.e(LOG_TAG, "url is null!");
                            return false;
                        }
                        if (url.length() > 127) {
                            Log.e(LOG_TAG, "url " + url + "is len over 127");
                            return false;
                        }
                    }
                    int[] iplist = new int[ipaddr.length];
                    int i = 0;
                    int length = ipaddr.length;
                    int i2 = 0;
                    while (i2 < length) {
                        String ip = ipaddr[i2];
                        int i3 = i + 1;
                        try {
                            iplist[i] = parseIpStr(ip);
                            i2++;
                            i = i3;
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "parse ipstr " + ip + " failed!", e);
                            return false;
                        }
                    }
                    return getStackService().oplusAddAppDnsConfig(hostname, iplist);
                }
            } catch (Exception e2) {
                Log.e(LOG_TAG, "oplusAddAppDnsConfig failed!", e2);
                return false;
            }
        }
        Log.e(LOG_TAG, "param invalid!");
        return false;
    }

    public boolean oplusDelAppDnsConfig(String[] hostname) {
        if (hostname != null) {
            try {
                if (hostname.length != 0) {
                    for (String url : hostname) {
                        if (url == null) {
                            Log.e(LOG_TAG, "url is null!");
                            return false;
                        }
                        if (url.length() > 127) {
                            Log.e(LOG_TAG, "url " + url + "is len over 127");
                            return false;
                        }
                    }
                    return getStackService().oplusDelAppDnsConfig(hostname);
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "oplusAddAppDnsConfig failed!", e);
                return false;
            }
        }
        Log.e(LOG_TAG, "param invalid!");
        return false;
    }

    public boolean oplusClearAppDnsConfig() {
        try {
            return getStackService().oplusClearAppDnsConfig();
        } catch (Exception e) {
            Log.e(LOG_TAG, "oplusClearAppDnsConfig failed!", e);
            return false;
        }
    }

    public int oplusGetUidByPort(int port, String callPackageName) {
        try {
            return getStackService().oplusGetUidByPort(port, callPackageName);
        } catch (Exception e) {
            Log.e(LOG_TAG, "oplusGetUidByPort failed!", e);
            return -1;
        }
    }

    public boolean oplusSetRedirectPort(int action, int port) {
        try {
            return getStackService().oplusSetRedirectPort(action, port);
        } catch (Exception e) {
            Log.e(LOG_TAG, "oplusSetRedirectPort failed!", e);
            return false;
        }
    }

    public void videoStart() {
        try {
            Log.d(LOG_TAG, "videoStart");
            getStackService().videoStart();
        } catch (Exception e) {
            Log.e(LOG_TAG, "videoStartStop failed!", e);
        }
    }

    public void videoStop() {
        try {
            Log.d(LOG_TAG, "videoStop");
            getStackService().videoStop();
        } catch (Exception e) {
            Log.e(LOG_TAG, "videoStartStop failed!", e);
        }
    }

    public void videoFrameLag(boolean lag) {
        try {
            Log.d(LOG_TAG, "videoFrameLag " + lag);
            getStackService().videoFrameLag(lag);
        } catch (Exception e) {
            Log.e(LOG_TAG, "videoFrameRateUpdate failed!", e);
        }
    }

    public OplusNetworkKPI getUidKpi(int uid, Network network) {
        try {
            if (network == null) {
                Log.e(LOG_TAG, "network is null");
                return null;
            }
            OplusNetworkKPI networkKPI = this.mNetworkStackService.getUidKpi(uid, network);
            return networkKPI;
        } catch (Exception e) {
            Log.e(LOG_TAG, "getUidKpi exception: " + e.getMessage());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class NetworkScore {
        Network mNetwork;
        int mScore;
        long mTime;

        public NetworkScore(Network network, int score, long time) {
            this.mNetwork = network;
            this.mScore = score;
            this.mTime = time;
        }
    }

    private int queryScoreFromCache(Network network) {
        Log.d(LOG_TAG, "queryScoreFromCache " + network.getNetId());
        if (this.mNetworkScoreMap.size() > 10) {
            Log.d(LOG_TAG, "cache reach max count,clear cache");
            this.mNetworkScoreMap.clear();
            return -50;
        }
        NetworkScore ns = this.mNetworkScoreMap.get(network);
        if (ns != null) {
            if (System.currentTimeMillis() - ns.mTime < 1500) {
                Log.d(LOG_TAG, "queryScoreFromCache score " + ns.mScore);
                return ns.mScore;
            }
            this.mNetworkScoreMap.remove(network);
        }
        return -50;
    }

    private void addScoreToCache(Network network, int score) {
        Log.d(LOG_TAG, "addScoreToCache: " + network.getNetId() + " " + score);
        NetworkScore ns = new NetworkScore(network, score, System.currentTimeMillis());
        this.mNetworkScoreMap.put(network, ns);
    }
}
