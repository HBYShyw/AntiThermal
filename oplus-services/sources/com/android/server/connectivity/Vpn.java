package com.android.server.connectivity;

import android.R;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.net.ConnectivityDiagnosticsManager;
import android.net.ConnectivityManager;
import android.net.DnsResolver;
import android.net.INetd;
import android.net.INetworkManagementEventObserver;
import android.net.Ikev2VpnProfile;
import android.net.InetAddresses;
import android.net.IpPrefix;
import android.net.IpSecManager;
import android.net.IpSecTransform;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.Network;
import android.net.NetworkAgent;
import android.net.NetworkAgentConfig;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkProvider;
import android.net.NetworkRequest;
import android.net.NetworkScore;
import android.net.NetworkSpecifier;
import android.net.RouteInfo;
import android.net.TelephonyNetworkSpecifier;
import android.net.UidRangeParcel;
import android.net.UnderlyingNetworkInfo;
import android.net.Uri;
import android.net.VpnProfileState;
import android.net.VpnTransportInfo;
import android.net.ipsec.ike.ChildSaProposal;
import android.net.ipsec.ike.ChildSessionCallback;
import android.net.ipsec.ike.ChildSessionConfiguration;
import android.net.ipsec.ike.ChildSessionParams;
import android.net.ipsec.ike.IkeSession;
import android.net.ipsec.ike.IkeSessionCallback;
import android.net.ipsec.ike.IkeSessionConfiguration;
import android.net.ipsec.ike.IkeSessionConnectionInfo;
import android.net.ipsec.ike.IkeSessionParams;
import android.net.ipsec.ike.IkeTunnelConnectionParams;
import android.net.ipsec.ike.exceptions.IkeIOException;
import android.net.ipsec.ike.exceptions.IkeNetworkLostException;
import android.net.ipsec.ike.exceptions.IkeNonProtocolException;
import android.net.ipsec.ike.exceptions.IkeProtocolException;
import android.net.ipsec.ike.exceptions.IkeTimeoutException;
import android.net.vcn.VcnTransportInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.INetworkManagementService;
import android.os.Looper;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemService;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.security.Credentials;
import android.security.KeyStore2;
import android.security.KeyStoreException;
import android.system.keystore2.KeyDescriptor;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.util.LocalLog;
import android.util.Log;
import android.util.Range;
import android.util.SparseArray;
import com.android.bluetooth.BluetoothStatsLog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.net.LegacyVpnInfo;
import com.android.internal.net.VpnConfig;
import com.android.internal.net.VpnProfile;
import com.android.modules.utils.build.SdkLevel;
import com.android.net.module.util.BinderUtils;
import com.android.net.module.util.LinkPropertiesUtils;
import com.android.net.module.util.NetdUtils;
import com.android.net.module.util.NetworkStackConstants;
import com.android.server.DeviceIdleInternal;
import com.android.server.LocalServices;
import com.android.server.am.IOplusSceneManager;
import com.android.server.autofill.HintsHelper;
import com.android.server.connectivity.Vpn;
import com.android.server.connectivity.VpnIkev2Utils;
import com.android.server.net.BaseNetworkObserver;
import com.android.server.vcn.util.MtuUtils;
import com.android.server.vcn.util.PersistableBundleUtils;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import libcore.io.IoUtils;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Vpn {
    private static final String ANDROID_KEYSTORE_PROVIDER = "AndroidKeyStore";

    @VisibleForTesting
    public static final int AUTOMATIC_KEEPALIVE_DELAY_SECONDS = 30;

    @VisibleForTesting
    static final int DEFAULT_LONG_LIVED_TCP_CONNS_EXPENSIVE_TIMEOUT_SEC = 60;

    @VisibleForTesting
    static final int DEFAULT_UDP_PORT_4500_NAT_TIMEOUT_SEC_INT = 300;
    private static final long IKE_DELAY_ON_NC_LP_CHANGE_MS = 300;
    private static final String LOCKDOWN_ALLOWLIST_SETTING_NAME = "always_on_vpn_lockdown_whitelist";
    private static final boolean LOGD = true;
    private static final int MAX_EVENTS_LOGS = 100;

    @VisibleForTesting
    static final int MAX_VPN_PROFILE_SIZE_BYTES = 131072;
    private static final String NETWORKTYPE = "VPN";

    @VisibleForTesting
    public static final int PREFERRED_IKE_PROTOCOL_AUTO = 0;

    @VisibleForTesting
    public static final int PREFERRED_IKE_PROTOCOL_IPV4_UDP = 40;

    @VisibleForTesting
    public static final int PREFERRED_IKE_PROTOCOL_IPV6_ESP = 61;

    @VisibleForTesting
    public static final int PREFERRED_IKE_PROTOCOL_IPV6_UDP = 60;
    private static final int PREFERRED_IKE_PROTOCOL_UNKNOWN = -1;
    private static final long RETRY_DELAY_AUTO_BACKOFF = -1;
    private static final int STARTING_TOKEN = -1;
    private static final String TAG = "Vpn";

    @VisibleForTesting
    static final String VPN_APP_EXCLUDED = "VPNAPPEXCLUDED_";
    private static final int VPN_DEFAULT_SCORE = 101;
    private static final long VPN_LAUNCH_IDLE_ALLOWLIST_DURATION_MS = 60000;
    private static final long VPN_MANAGER_EVENT_ALLOWLIST_DURATION_MS = 30000;
    private static final String VPN_PROVIDER_NAME_BASE = "VpnNetworkProvider:";

    @GuardedBy({"this"})
    @VisibleForTesting
    protected boolean mAlwaysOn;
    private final AppOpsManager mAppOpsManager;

    @GuardedBy({"this"})
    private final Set<UidRangeParcel> mBlockedUidsAsToldToConnectivity;

    @GuardedBy({"this"})
    private final SparseArray<CarrierConfigInfo> mCachedCarrierConfigInfoPerSubId;
    private final CarrierConfigManager mCarrierConfigManager;

    @VisibleForTesting
    protected VpnConfig mConfig;
    private Connection mConnection;
    private final ConnectivityDiagnosticsManager mConnectivityDiagnosticsManager;
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    protected boolean mDataStallSuspected;

    @VisibleForTesting
    final Dependencies mDeps;
    private volatile boolean mEnableTeardown;
    private final LocalLog mEventChanges;
    private final Ikev2SessionCreator mIkev2SessionCreator;

    @VisibleForTesting
    protected String mInterface;
    private boolean mIsPackageTargetingAtLeastQ;

    @GuardedBy({"this"})
    private int mLegacyState;

    @GuardedBy({"this"})
    @VisibleForTesting
    protected boolean mLockdown;
    private List<String> mLockdownAllowlist;
    private final Looper mLooper;
    private final INetd mNetd;

    @VisibleForTesting
    protected NetworkAgent mNetworkAgent;

    @VisibleForTesting
    protected NetworkCapabilities mNetworkCapabilities;
    private final NetworkInfo mNetworkInfo;
    private final NetworkProvider mNetworkProvider;
    private final INetworkManagementService mNms;
    private INetworkManagementEventObserver mObserver;
    private int mOwnerUID;

    @GuardedBy({"this"})
    @VisibleForTesting
    protected String mPackage;
    private PendingIntent mStatusIntent;
    private final SubscriptionManager mSubscriptionManager;
    private final SystemServices mSystemServices;
    private final TelephonyManager mTelephonyManager;
    private final int mUserId;
    private final Context mUserIdContext;
    private final UserManager mUserManager;
    IVpnExt mVpnExt;
    private final VpnProfileStore mVpnProfileStore;

    @VisibleForTesting
    protected VpnRunner mVpnRunner;
    private static final long[] IKEV2_VPN_RETRY_DELAYS_MS = {1000, 2000, 5000, 30000, 60000, 300000, 900000};
    private static final long[] DATA_STALL_RESET_DELAYS_SEC = {30, 60, 120, 240, 480, 960};

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface IkeV2VpnRunnerCallback {
        void onChildMigrated(int i, IpSecTransform ipSecTransform, IpSecTransform ipSecTransform2);

        void onChildOpened(int i, ChildSessionConfiguration childSessionConfiguration);

        void onChildTransformCreated(int i, IpSecTransform ipSecTransform, int i2);

        void onDefaultNetworkCapabilitiesChanged(NetworkCapabilities networkCapabilities);

        void onDefaultNetworkChanged(Network network);

        void onDefaultNetworkLinkPropertiesChanged(LinkProperties linkProperties);

        void onDefaultNetworkLost(Network network);

        void onIkeConnectionInfoChanged(int i, IkeSessionConnectionInfo ikeSessionConnectionInfo);

        void onIkeOpened(int i, IkeSessionConfiguration ikeSessionConfiguration);

        void onSessionLost(int i, Exception exc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface RetryScheduler {
        void checkInterruptAndDelay(boolean z) throws InterruptedException;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface ValidationStatusCallback {
        void onValidationStatus(int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean areLongLivedTcpConnectionsExpensive(int i) {
        return i < 60;
    }

    private String getVpnManagerEventClassName(int i) {
        return i != 1 ? i != 2 ? "UNKNOWN_CLASS" : "ERROR_CLASS_RECOVERABLE" : "ERROR_CLASS_NOT_RECOVERABLE";
    }

    private String getVpnManagerEventErrorName(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? "UNKNOWN_ERROR" : "ERROR_CODE_NETWORK_IO" : "ERROR_CODE_NETWORK_LOST" : "ERROR_CODE_NETWORK_PROTOCOL_TIMEOUT" : "ERROR_CODE_NETWORK_UNKNOWN_HOST";
    }

    private native boolean jniAddAddress(String str, String str2, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public native int jniCheck(String str);

    /* JADX INFO: Access modifiers changed from: private */
    public native int jniCreate(int i);

    private native boolean jniDelAddress(String str, String str2, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public native String jniGetName(int i);

    private native void jniReset(String str);

    /* JADX INFO: Access modifiers changed from: private */
    public native int jniSetAddresses(String str, String str2);

    @VisibleForTesting
    VpnProfileStore getVpnProfileStore() {
        return this.mVpnProfileStore;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class CarrierConfigInfo {
        public final int encapType;
        public final int ipVersion;
        public final int keepaliveDelaySec;
        public final String mccMnc;

        CarrierConfigInfo(String str, int i, int i2, int i3) {
            this.mccMnc = str;
            this.keepaliveDelaySec = i;
            this.encapType = i2;
            this.ipVersion = i3;
        }

        public String toString() {
            return "CarrierConfigInfo(" + this.mccMnc + ") [keepaliveDelaySec=" + this.keepaliveDelaySec + ", encapType=" + this.encapType + ", ipVersion=" + this.ipVersion + "]";
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Dependencies {
        public boolean isCallerSystem() {
            return Binder.getCallingUid() == 1000;
        }

        public void startService(String str) {
            SystemService.start(str);
        }

        public void stopService(String str) {
            SystemService.stop(str);
        }

        public boolean isServiceRunning(String str) {
            return SystemService.isRunning(str);
        }

        public boolean isServiceStopped(String str) {
            return SystemService.isStopped(str);
        }

        public File getStateFile() {
            return new File("/data/misc/vpn/state");
        }

        public DeviceIdleInternal getDeviceIdleInternal() {
            return (DeviceIdleInternal) LocalServices.getService(DeviceIdleInternal.class);
        }

        public PendingIntent getIntentForStatusPanel(Context context) {
            return VpnConfig.getIntentForStatusPanel(context);
        }

        public void sendArgumentsToDaemon(String str, LocalSocket localSocket, String[] strArr, RetryScheduler retryScheduler) throws IOException, InterruptedException {
            LocalSocketAddress localSocketAddress = new LocalSocketAddress(str, LocalSocketAddress.Namespace.RESERVED);
            while (true) {
                try {
                    localSocket.connect(localSocketAddress);
                    break;
                } catch (Exception unused) {
                    retryScheduler.checkInterruptAndDelay(true);
                }
            }
            localSocket.setSoTimeout(500);
            OutputStream outputStream = localSocket.getOutputStream();
            for (String str2 : strArr) {
                byte[] bytes = str2.getBytes(StandardCharsets.UTF_8);
                if (bytes.length >= 65535) {
                    throw new IllegalArgumentException("Argument is too large");
                }
                outputStream.write(bytes.length >> 8);
                outputStream.write(bytes.length);
                outputStream.write(bytes);
                retryScheduler.checkInterruptAndDelay(false);
            }
            outputStream.write(255);
            outputStream.write(255);
            InputStream inputStream = localSocket.getInputStream();
            while (inputStream.read() != -1) {
                retryScheduler.checkInterruptAndDelay(true);
            }
        }

        public InetAddress resolve(final String str) throws ExecutionException, InterruptedException {
            try {
                return InetAddresses.parseNumericAddress(str);
            } catch (IllegalArgumentException unused) {
                CancellationSignal cancellationSignal = new CancellationSignal();
                try {
                    DnsResolver dnsResolver = DnsResolver.getInstance();
                    final CompletableFuture completableFuture = new CompletableFuture();
                    dnsResolver.query(null, str, 0, new Executor() { // from class: com.android.server.connectivity.Vpn$Dependencies$$ExternalSyntheticLambda0
                        @Override // java.util.concurrent.Executor
                        public final void execute(Runnable runnable) {
                            runnable.run();
                        }
                    }, cancellationSignal, new DnsResolver.Callback<List<InetAddress>>() { // from class: com.android.server.connectivity.Vpn.Dependencies.1
                        @Override // android.net.DnsResolver.Callback
                        public void onAnswer(List<InetAddress> list, int i) {
                            if (list.size() > 0) {
                                completableFuture.complete(list.get(0));
                            } else {
                                completableFuture.completeExceptionally(new UnknownHostException(str));
                            }
                        }

                        @Override // android.net.DnsResolver.Callback
                        public void onError(DnsResolver.DnsException dnsException) {
                            Log.e(Vpn.TAG, "Async dns resolver error : " + dnsException);
                            completableFuture.completeExceptionally(new UnknownHostException(str));
                        }
                    });
                    return (InetAddress) completableFuture.get();
                } catch (InterruptedException e) {
                    Log.e(Vpn.TAG, "Legacy VPN was interrupted while resolving the endpoint", e);
                    cancellationSignal.cancel();
                    throw e;
                } catch (ExecutionException e2) {
                    Log.e(Vpn.TAG, "Cannot resolve VPN endpoint : " + str + ".", e2);
                    throw e2;
                }
            }
        }

        public boolean isInterfacePresent(Vpn vpn, String str) {
            return vpn.jniCheck(str) != 0;
        }

        public ParcelFileDescriptor adoptFd(Vpn vpn, int i) {
            return ParcelFileDescriptor.adoptFd(jniCreate(vpn, i));
        }

        public int jniCreate(Vpn vpn, int i) {
            return vpn.jniCreate(i);
        }

        public String jniGetName(Vpn vpn, int i) {
            return vpn.jniGetName(i);
        }

        public int jniSetAddresses(Vpn vpn, String str, String str2) {
            return vpn.jniSetAddresses(str, str2);
        }

        public void setBlocking(FileDescriptor fileDescriptor, boolean z) {
            try {
                IoUtils.setBlocking(fileDescriptor, z);
            } catch (IOException e) {
                throw new IllegalStateException("Cannot set tunnel's fd as blocking=" + z, e);
            }
        }

        public long getNextRetryDelayMs(int i) {
            if (i >= Vpn.IKEV2_VPN_RETRY_DELAYS_MS.length) {
                return Vpn.IKEV2_VPN_RETRY_DELAYS_MS[Vpn.IKEV2_VPN_RETRY_DELAYS_MS.length - 1];
            }
            return Vpn.IKEV2_VPN_RETRY_DELAYS_MS[i];
        }

        public ScheduledThreadPoolExecutor newScheduledThreadPoolExecutor() {
            return new ScheduledThreadPoolExecutor(1);
        }

        public NetworkAgent newNetworkAgent(Context context, Looper looper, String str, NetworkCapabilities networkCapabilities, LinkProperties linkProperties, NetworkScore networkScore, NetworkAgentConfig networkAgentConfig, NetworkProvider networkProvider, ValidationStatusCallback validationStatusCallback) {
            return new VpnNetworkAgentWrapper(context, looper, str, networkCapabilities, linkProperties, networkScore, networkAgentConfig, networkProvider, validationStatusCallback);
        }

        public long getDataStallResetSessionSeconds(int i) {
            if (i >= Vpn.DATA_STALL_RESET_DELAYS_SEC.length) {
                return Vpn.DATA_STALL_RESET_DELAYS_SEC[Vpn.DATA_STALL_RESET_DELAYS_SEC.length - 1];
            }
            return Vpn.DATA_STALL_RESET_DELAYS_SEC[i];
        }

        public int getJavaNetworkInterfaceMtu(String str, int i) throws SocketException {
            NetworkInterface byName;
            return (str == null || (byName = NetworkInterface.getByName(str)) == null) ? i : byName.getMTU();
        }

        public int calculateVpnMtu(List<ChildSaProposal> list, int i, int i2, boolean z) {
            return MtuUtils.getMtu(list, i, i2, z);
        }

        public void verifyCallingUidAndPackage(Context context, String str, int i) {
            int callingUid = Binder.getCallingUid();
            if (Vpn.getAppUid(context, str, i) == callingUid) {
                return;
            }
            throw new SecurityException(str + " does not belong to uid " + callingUid);
        }
    }

    public Vpn(Looper looper, Context context, INetworkManagementService iNetworkManagementService, INetd iNetd, int i, VpnProfileStore vpnProfileStore) {
        this(looper, context, new Dependencies(), iNetworkManagementService, iNetd, i, vpnProfileStore, new SystemServices(context), new Ikev2SessionCreator());
    }

    @VisibleForTesting
    public Vpn(Looper looper, Context context, Dependencies dependencies, INetworkManagementService iNetworkManagementService, INetd iNetd, int i, VpnProfileStore vpnProfileStore) {
        this(looper, context, dependencies, iNetworkManagementService, iNetd, i, vpnProfileStore, new SystemServices(context), new Ikev2SessionCreator());
    }

    @VisibleForTesting
    protected Vpn(Looper looper, Context context, Dependencies dependencies, INetworkManagementService iNetworkManagementService, INetd iNetd, int i, VpnProfileStore vpnProfileStore, SystemServices systemServices, Ikev2SessionCreator ikev2SessionCreator) {
        this.mEnableTeardown = true;
        this.mDataStallSuspected = false;
        this.mEventChanges = new LocalLog(100);
        this.mCachedCarrierConfigInfoPerSubId = new SparseArray<>();
        this.mAlwaysOn = false;
        this.mLockdown = false;
        this.mLockdownAllowlist = Collections.emptyList();
        this.mBlockedUidsAsToldToConnectivity = new ArraySet();
        this.mObserver = new BaseNetworkObserver() { // from class: com.android.server.connectivity.Vpn.1
            public void interfaceStatusChanged(String str, boolean z) {
                synchronized (Vpn.this) {
                    if (!z) {
                        VpnRunner vpnRunner = Vpn.this.mVpnRunner;
                        if (vpnRunner != null && (vpnRunner instanceof LegacyVpnRunner)) {
                            ((LegacyVpnRunner) vpnRunner).exitIfOuterInterfaceIs(str);
                        }
                    }
                }
            }

            public void interfaceRemoved(String str) {
                synchronized (Vpn.this) {
                    if (str.equals(Vpn.this.mInterface) && Vpn.this.jniCheck(str) == 0) {
                        if (Vpn.this.mConnection != null) {
                            Vpn.this.mAppOpsManager.finishOp("android:establish_vpn_service", Vpn.this.mOwnerUID, Vpn.this.mPackage, null);
                            Vpn.this.mContext.unbindService(Vpn.this.mConnection);
                            Vpn.this.cleanupVpnStateLocked();
                        } else {
                            Vpn vpn = Vpn.this;
                            if (vpn.mVpnRunner != null) {
                                if (!"[Legacy VPN]".equals(vpn.mPackage)) {
                                    Vpn.this.mAppOpsManager.finishOp("android:establish_vpn_manager", Vpn.this.mOwnerUID, Vpn.this.mPackage, null);
                                }
                                Vpn.this.mVpnRunner.exit();
                            }
                        }
                    }
                }
            }
        };
        this.mVpnProfileStore = vpnProfileStore;
        this.mContext = context;
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
        this.mAppOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        this.mUserIdContext = context.createContextAsUser(UserHandle.of(i), 0);
        this.mConnectivityDiagnosticsManager = (ConnectivityDiagnosticsManager) context.getSystemService(ConnectivityDiagnosticsManager.class);
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mDeps = dependencies;
        this.mNms = iNetworkManagementService;
        this.mNetd = iNetd;
        this.mUserId = i;
        this.mLooper = looper;
        this.mSystemServices = systemServices;
        this.mIkev2SessionCreator = ikev2SessionCreator;
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        this.mPackage = "[Legacy VPN]";
        this.mOwnerUID = getAppUid(context, "[Legacy VPN]", i);
        this.mIsPackageTargetingAtLeastQ = doesPackageTargetAtLeastQ(this.mPackage);
        try {
            iNetworkManagementService.registerObserver(this.mObserver);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Problem registering observer", e);
        }
        NetworkProvider networkProvider = new NetworkProvider(context, looper, VPN_PROVIDER_NAME_BASE + this.mUserId);
        this.mNetworkProvider = networkProvider;
        this.mConnectivityManager.registerNetworkProvider(networkProvider);
        this.mLegacyState = 0;
        this.mNetworkInfo = new NetworkInfo(17, 0, NETWORKTYPE, "");
        this.mNetworkCapabilities = new NetworkCapabilities.Builder().addTransportType(4).removeCapability(15).addCapability(28).setTransportInfo(new VpnTransportInfo(-1, (String) null, false, false)).build();
        loadAlwaysOnPackage();
        try {
            IVpnExt iVpnExt = (IVpnExt) ExtLoader.type(IVpnExt.class).base((Object) null).create();
            this.mVpnExt = iVpnExt;
            iVpnExt.init(this.mContext);
        } catch (Exception e2) {
            this.mVpnExt = null;
            Log.d(TAG, "mVpnExt init error" + e2.toString());
        }
    }

    public void setEnableTeardown(boolean z) {
        this.mEnableTeardown = z;
    }

    @VisibleForTesting
    public boolean getEnableTeardown() {
        return this.mEnableTeardown;
    }

    @GuardedBy({"this"})
    @VisibleForTesting
    protected void updateState(NetworkInfo.DetailedState detailedState, String str) {
        Log.d(TAG, "setting state=" + detailedState + ", reason=" + str);
        this.mLegacyState = LegacyVpnInfo.stateFromNetworkInfo(detailedState);
        this.mNetworkInfo.setDetailedState(detailedState, str, null);
        int i = AnonymousClass2.$SwitchMap$android$net$NetworkInfo$DetailedState[detailedState.ordinal()];
        if (i == 1) {
            NetworkAgent networkAgent = this.mNetworkAgent;
            if (networkAgent != null) {
                networkAgent.markConnected();
            }
        } else if (i == 2 || i == 3) {
            NetworkAgent networkAgent2 = this.mNetworkAgent;
            if (networkAgent2 != null) {
                networkAgent2.unregister();
                this.mNetworkAgent = null;
            }
        } else if (i == 4) {
            if (this.mNetworkAgent != null) {
                throw new IllegalStateException("VPN can only go to CONNECTING state when the agent is null.");
            }
        } else {
            throw new IllegalArgumentException("Illegal state argument " + detailedState);
        }
        updateAlwaysOnNotification(detailedState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.connectivity.Vpn$2, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$android$net$NetworkInfo$DetailedState;

        static {
            int[] iArr = new int[NetworkInfo.DetailedState.values().length];
            $SwitchMap$android$net$NetworkInfo$DetailedState = iArr;
            try {
                iArr[NetworkInfo.DetailedState.CONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[NetworkInfo.DetailedState.DISCONNECTED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[NetworkInfo.DetailedState.FAILED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[NetworkInfo.DetailedState.CONNECTING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    private void resetNetworkCapabilities() {
        this.mNetworkCapabilities = new NetworkCapabilities.Builder(this.mNetworkCapabilities).setUids((Set) null).setTransportInfo(new VpnTransportInfo(-1, (String) null, false, false)).build();
    }

    public synchronized void setLockdown(boolean z) {
        enforceControlPermissionOrInternalCaller();
        setVpnForcedLocked(z);
        this.mLockdown = z;
        if (this.mAlwaysOn) {
            saveAlwaysOnPackage();
        }
    }

    public synchronized String getPackage() {
        return this.mPackage;
    }

    public synchronized boolean getLockdown() {
        return this.mLockdown;
    }

    public synchronized boolean getAlwaysOn() {
        return this.mAlwaysOn;
    }

    public boolean isAlwaysOnPackageSupported(String str) {
        ApplicationInfo applicationInfo;
        enforceSettingsPermission();
        if (str == null) {
            return false;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (getVpnProfilePrivileged(str) != null) {
                return true;
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            PackageManager packageManager = this.mContext.getPackageManager();
            try {
                applicationInfo = packageManager.getApplicationInfoAsUser(str, 0, this.mUserId);
            } catch (PackageManager.NameNotFoundException unused) {
                Log.w(TAG, "Can't find \"" + str + "\" when checking always-on support");
                applicationInfo = null;
            }
            if (applicationInfo != null && applicationInfo.targetSdkVersion >= 24) {
                Intent intent = new Intent("android.net.VpnService");
                intent.setPackage(str);
                List queryIntentServicesAsUser = packageManager.queryIntentServicesAsUser(intent, 128, this.mUserId);
                if (queryIntentServicesAsUser != null && queryIntentServicesAsUser.size() != 0) {
                    Iterator it = queryIntentServicesAsUser.iterator();
                    while (it.hasNext()) {
                        Bundle bundle = ((ResolveInfo) it.next()).serviceInfo.metaData;
                        if (bundle != null && !bundle.getBoolean("android.net.VpnService.SUPPORTS_ALWAYS_ON", true)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private Intent buildVpnManagerEventIntent(String str, int i, int i2, String str2, String str3, VpnProfileState vpnProfileState, Network network, NetworkCapabilities networkCapabilities, LinkProperties linkProperties) {
        Log.d(TAG, "buildVpnManagerEventIntent: sessionKey = " + str3);
        Intent intent = new Intent("android.net.action.VPN_MANAGER_EVENT");
        intent.setPackage(str2);
        intent.addCategory(str);
        intent.putExtra("android.net.extra.VPN_PROFILE_STATE", vpnProfileState);
        intent.putExtra("android.net.extra.SESSION_KEY", str3);
        intent.putExtra("android.net.extra.UNDERLYING_NETWORK", network);
        intent.putExtra("android.net.extra.UNDERLYING_NETWORK_CAPABILITIES", networkCapabilities);
        intent.putExtra("android.net.extra.UNDERLYING_LINK_PROPERTIES", linkProperties);
        intent.putExtra("android.net.extra.TIMESTAMP_MILLIS", System.currentTimeMillis());
        if (!"android.net.category.EVENT_DEACTIVATED_BY_USER".equals(str) || !"android.net.category.EVENT_ALWAYS_ON_STATE_CHANGED".equals(str)) {
            intent.putExtra("android.net.extra.ERROR_CLASS", i);
            intent.putExtra("android.net.extra.ERROR_CODE", i2);
        }
        return intent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean sendEventToVpnManagerApp(String str, int i, int i2, String str2, String str3, VpnProfileState vpnProfileState, Network network, NetworkCapabilities networkCapabilities, LinkProperties linkProperties) {
        this.mEventChanges.log("[VMEvent] Event class=" + getVpnManagerEventClassName(i) + ", err=" + getVpnManagerEventErrorName(i2) + " for " + str2 + " on session " + str3);
        return sendEventToVpnManagerApp(buildVpnManagerEventIntent(str, i, i2, str2, str3, vpnProfileState, network, networkCapabilities, linkProperties), str2);
    }

    private boolean sendEventToVpnManagerApp(Intent intent, String str) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mDeps.getDeviceIdleInternal().addPowerSaveTempWhitelistApp(Process.myUid(), str, 30000L, this.mUserId, false, 309, "VpnManager event");
            return this.mUserIdContext.startService(intent) != null;
        } catch (RuntimeException e) {
            Log.e(TAG, "Service of VpnManager app " + intent + " failed to start", e);
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isVpnApp(String str) {
        return (str == null || "[Legacy VPN]".equals(str)) ? false : true;
    }

    public synchronized boolean setAlwaysOnPackage(String str, boolean z, List<String> list) {
        VpnProfileState makeVpnProfileStateLocked;
        enforceControlPermissionOrInternalCaller();
        String str2 = this.mPackage;
        boolean z2 = !Objects.equals(str, str2);
        boolean z3 = isVpnApp(str2) && this.mAlwaysOn && (z != this.mLockdown || z2);
        boolean z4 = isVpnApp(str) && z2;
        if (!setAlwaysOnPackageInternal(str, z, list)) {
            return false;
        }
        saveAlwaysOnPackage();
        if (!SdkLevel.isAtLeastT()) {
            return true;
        }
        if (z3) {
            if (z2) {
                makeVpnProfileStateLocked = makeDisconnectedVpnProfileState();
            } else {
                makeVpnProfileStateLocked = makeVpnProfileStateLocked();
            }
            sendEventToVpnManagerApp("android.net.category.EVENT_ALWAYS_ON_STATE_CHANGED", -1, -1, str2, null, makeVpnProfileStateLocked, null, null, null);
        }
        if (z4) {
            sendEventToVpnManagerApp("android.net.category.EVENT_ALWAYS_ON_STATE_CHANGED", -1, -1, str, getSessionKeyLocked(), makeVpnProfileStateLocked(), null, null, null);
        }
        return true;
    }

    @GuardedBy({"this"})
    private boolean setAlwaysOnPackageInternal(String str, boolean z, List<String> list) {
        List<String> emptyList;
        boolean z2 = false;
        if ("[Legacy VPN]".equals(str)) {
            Log.w(TAG, "Not setting legacy VPN \"" + str + "\" as always-on.");
            return false;
        }
        if (list != null) {
            for (String str2 : list) {
                if (str2.contains(",")) {
                    Log.w(TAG, "Not setting always-on vpn, invalid allowed package: " + str2);
                    return false;
                }
            }
        }
        if (str != null) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                if (!setPackageAuthorization(str, getVpnProfilePrivileged(str) == null ? 1 : 2)) {
                    return false;
                }
                this.mAlwaysOn = true;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        } else {
            this.mAlwaysOn = false;
            str = "[Legacy VPN]";
        }
        boolean z3 = this.mLockdown;
        if (this.mAlwaysOn && z) {
            z2 = true;
        }
        this.mLockdown = z2;
        if (z2 && list != null) {
            emptyList = Collections.unmodifiableList(new ArrayList(list));
        } else {
            emptyList = Collections.emptyList();
        }
        this.mLockdownAllowlist = emptyList;
        this.mEventChanges.log("[LockdownAlwaysOn] Mode changed: lockdown=" + this.mLockdown + " alwaysOn=" + this.mAlwaysOn + " calling from " + Binder.getCallingUid());
        if (isCurrentPreparedPackage(str)) {
            updateAlwaysOnNotification(this.mNetworkInfo.getDetailedState());
            setVpnForcedLocked(this.mLockdown);
            NetworkAgent networkAgent = this.mNetworkAgent;
            if (networkAgent != null && z3 != this.mLockdown) {
                startNewNetworkAgent(networkAgent, "Lockdown mode changed");
            }
        } else {
            prepareInternal(str);
        }
        return true;
    }

    private static boolean isNullOrLegacyVpn(String str) {
        return str == null || "[Legacy VPN]".equals(str);
    }

    public synchronized String getAlwaysOnPackage() {
        enforceControlPermissionOrInternalCaller();
        return this.mAlwaysOn ? this.mPackage : null;
    }

    public synchronized List<String> getLockdownAllowlist() {
        return this.mLockdown ? this.mLockdownAllowlist : null;
    }

    @GuardedBy({"this"})
    private void saveAlwaysOnPackage() {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSystemServices.settingsSecurePutStringForUser("always_on_vpn_app", getAlwaysOnPackage(), this.mUserId);
            this.mSystemServices.settingsSecurePutIntForUser("always_on_vpn_lockdown", (this.mAlwaysOn && this.mLockdown) ? 1 : 0, this.mUserId);
            this.mSystemServices.settingsSecurePutStringForUser(LOCKDOWN_ALLOWLIST_SETTING_NAME, String.join(",", this.mLockdownAllowlist), this.mUserId);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @GuardedBy({"this"})
    private void loadAlwaysOnPackage() {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String str = this.mSystemServices.settingsSecureGetStringForUser("always_on_vpn_app", this.mUserId);
            boolean z = this.mSystemServices.settingsSecureGetIntForUser("always_on_vpn_lockdown", 0, this.mUserId) != 0;
            String str2 = this.mSystemServices.settingsSecureGetStringForUser(LOCKDOWN_ALLOWLIST_SETTING_NAME, this.mUserId);
            setAlwaysOnPackageInternal(str, z, TextUtils.isEmpty(str2) ? Collections.emptyList() : Arrays.asList(str2.split(",")));
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean startAlwaysOnVpn() {
        synchronized (this) {
            String alwaysOnPackage = getAlwaysOnPackage();
            if (alwaysOnPackage == null) {
                return true;
            }
            if (!isAlwaysOnPackageSupported(alwaysOnPackage)) {
                setAlwaysOnPackage(null, false, null);
                return false;
            }
            if (getNetworkInfo().isConnected()) {
                return true;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                VpnProfile vpnProfilePrivileged = getVpnProfilePrivileged(alwaysOnPackage);
                if (vpnProfilePrivileged != null) {
                    startVpnProfilePrivileged(vpnProfilePrivileged, alwaysOnPackage);
                    return true;
                }
                this.mDeps.getDeviceIdleInternal().addPowerSaveTempWhitelistApp(Process.myUid(), alwaysOnPackage, 60000L, this.mUserId, false, 309, IOplusSceneManager.APP_SCENE_VPN);
                Intent intent = new Intent("android.net.VpnService");
                intent.setPackage(alwaysOnPackage);
                try {
                    return this.mUserIdContext.startService(intent) != null;
                } catch (RuntimeException e) {
                    Log.e(TAG, "VpnService " + intent + " failed to start", e);
                    return false;
                }
            } catch (Exception e2) {
                Log.e(TAG, "Error starting always-on VPN", e2);
                return false;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public synchronized boolean prepare(String str, String str2, int i) {
        if (doesHaveVPNAppWhiteList()) {
            if (str2 != null) {
                if (!isInVPNAppWhiteList(str2)) {
                    Log.d(TAG, "prepareVpn, the VPN can't be estabilshed as the app is not in the white list");
                    return false;
                }
            } else if (!isInVPNAppWhiteList(str)) {
                Log.d(TAG, "prepareVpn, the VPN can't be estabilshed as the app is not in the white list");
                return false;
            }
        } else if (isVpnDisabled()) {
            Log.d(TAG, "prepareVpn, the VPN can't be estabilshed as the switch disable");
            return false;
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.CONTROL_VPN") != 0) {
            if (str != null) {
                verifyCallingUidAndPackage(str);
            }
            if (str2 != null) {
                verifyCallingUidAndPackage(str2);
            }
        }
        if (str != null) {
            if (this.mAlwaysOn && !isCurrentPreparedPackage(str)) {
                return false;
            }
            if (!isCurrentPreparedPackage(str)) {
                if (str.equals("[Legacy VPN]") || !isVpnPreConsented(this.mContext, str, i)) {
                    return false;
                }
                prepareInternal(str);
                return true;
            }
            if (!str.equals("[Legacy VPN]") && !isVpnPreConsented(this.mContext, str, i)) {
                prepareInternal("[Legacy VPN]");
                return false;
            }
        }
        if (str2 != null && (str2.equals("[Legacy VPN]") || !isCurrentPreparedPackage(str2))) {
            enforceControlPermission();
            if (this.mAlwaysOn && !isCurrentPreparedPackage(str2)) {
                return false;
            }
            prepareInternal(str2);
            return true;
        }
        return true;
    }

    @GuardedBy({"this"})
    private boolean isCurrentPreparedPackage(String str) {
        return getAppUid(this.mContext, str, this.mUserId) == this.mOwnerUID && this.mPackage.equals(str);
    }

    @GuardedBy({"this"})
    private void prepareInternal(String str) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (this.mInterface != null) {
                this.mStatusIntent = null;
                agentDisconnect();
                jniReset(this.mInterface);
                this.mInterface = null;
                resetNetworkCapabilities();
            }
            Connection connection = this.mConnection;
            if (connection != null) {
                try {
                    connection.mService.transact(AudioFormat.SUB_MASK, Parcel.obtain(), null, 1);
                } catch (Exception unused) {
                }
                this.mAppOpsManager.finishOp("android:establish_vpn_service", this.mOwnerUID, this.mPackage, null);
                this.mContext.unbindService(this.mConnection);
                cleanupVpnStateLocked();
            } else if (this.mVpnRunner != null) {
                stopVpnRunnerAndNotifyAppLocked();
            }
            try {
                this.mNms.denyProtect(this.mOwnerUID);
            } catch (Exception e) {
                Log.wtf(TAG, "Failed to disallow UID " + this.mOwnerUID + " to call protect() " + e);
            }
            Log.i(TAG, "Switched from " + this.mPackage + " to " + str);
            this.mPackage = str;
            this.mOwnerUID = getAppUid(this.mContext, str, this.mUserId);
            this.mIsPackageTargetingAtLeastQ = doesPackageTargetAtLeastQ(str);
            try {
                this.mNms.allowProtect(this.mOwnerUID);
            } catch (Exception e2) {
                Log.wtf(TAG, "Failed to allow UID " + this.mOwnerUID + " to call protect() " + e2);
            }
            this.mConfig = null;
            updateState(NetworkInfo.DetailedState.DISCONNECTED, "prepare");
            setVpnForcedLocked(this.mLockdown);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean setPackageAuthorization(String str, int i) {
        String[] strArr;
        enforceControlPermissionOrInternalCaller();
        int appUid = getAppUid(this.mContext, str, this.mUserId);
        if (appUid == -1 || "[Legacy VPN]".equals(str)) {
            return false;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (i == -1) {
                strArr = new String[]{"android:activate_vpn", "android:activate_platform_vpn"};
            } else if (i == 1) {
                strArr = new String[]{"android:activate_vpn"};
            } else {
                if (i != 2) {
                    if (i == 3) {
                        return false;
                    }
                    Log.wtf(TAG, "Unrecognized VPN type while granting authorization");
                    return false;
                }
                strArr = new String[]{"android:activate_platform_vpn"};
            }
            for (String str2 : strArr) {
                this.mAppOpsManager.setMode(str2, appUid, str, i == -1 ? 1 : 0);
            }
            return true;
        } catch (Exception e) {
            Log.wtf(TAG, "Failed to set app ops for package " + str + ", uid " + appUid, e);
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private static boolean isVpnPreConsented(Context context, String str, int i) {
        if (i == 1) {
            return isVpnServicePreConsented(context, str);
        }
        if (i == 2) {
            return isVpnProfilePreConsented(context, str);
        }
        if (i != 3) {
            return false;
        }
        return "[Legacy VPN]".equals(str);
    }

    private static boolean doesPackageHaveAppop(Context context, String str, String str2) {
        return ((AppOpsManager) context.getSystemService("appops")).noteOpNoThrow(str2, Binder.getCallingUid(), str, null, null) == 0;
    }

    private static boolean isVpnServicePreConsented(Context context, String str) {
        return doesPackageHaveAppop(context, str, "android:activate_vpn");
    }

    private static boolean isVpnProfilePreConsented(Context context, String str) {
        return doesPackageHaveAppop(context, str, "android:activate_platform_vpn") || isVpnServicePreConsented(context, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getAppUid(Context context, String str, int i) {
        if ("[Legacy VPN]".equals(str)) {
            return Process.myUid();
        }
        PackageManager packageManager = context.getPackageManager();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int packageUidAsUser = packageManager.getPackageUidAsUser(str, i);
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return packageUidAsUser;
        } catch (PackageManager.NameNotFoundException unused) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return -1;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    private boolean doesPackageTargetAtLeastQ(String str) {
        if ("[Legacy VPN]".equals(str)) {
            return true;
        }
        try {
            return this.mContext.getPackageManager().getApplicationInfoAsUser(str, 0, this.mUserId).targetSdkVersion >= 29;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.w(TAG, "Can't find \"" + str + "\"");
            return false;
        }
    }

    public NetworkInfo getNetworkInfo() {
        return this.mNetworkInfo;
    }

    @VisibleForTesting
    public synchronized Network getNetwork() {
        NetworkAgent networkAgent = this.mNetworkAgent;
        if (networkAgent == null) {
            return null;
        }
        Network network = networkAgent.getNetwork();
        if (network == null) {
            return null;
        }
        return network;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LinkProperties makeLinkProperties() {
        boolean z = isIkev2VpnRunner() && this.mConfig.mtu < 1280;
        VpnConfig vpnConfig = this.mConfig;
        boolean z2 = vpnConfig.allowIPv4;
        boolean z3 = vpnConfig.allowIPv6;
        LinkProperties linkProperties = new LinkProperties();
        linkProperties.setInterfaceName(this.mInterface);
        List<LinkAddress> list = this.mConfig.addresses;
        if (list != null) {
            for (LinkAddress linkAddress : list) {
                if (!z || !linkAddress.isIpv6()) {
                    linkProperties.addLinkAddress(linkAddress);
                    z2 |= linkAddress.getAddress() instanceof Inet4Address;
                    z3 |= linkAddress.getAddress() instanceof Inet6Address;
                }
            }
        }
        List<RouteInfo> list2 = this.mConfig.routes;
        if (list2 != null) {
            for (RouteInfo routeInfo : list2) {
                InetAddress address = routeInfo.getDestination().getAddress();
                if (!z || !(address instanceof Inet6Address)) {
                    linkProperties.addRoute(routeInfo);
                    if (routeInfo.getType() == 1) {
                        z2 |= address instanceof Inet4Address;
                        z3 |= address instanceof Inet6Address;
                    }
                }
            }
        }
        List list3 = this.mConfig.dnsServers;
        if (list3 != null) {
            Iterator it = list3.iterator();
            while (it.hasNext()) {
                InetAddress parseNumericAddress = InetAddresses.parseNumericAddress((String) it.next());
                if (!z || !(parseNumericAddress instanceof Inet6Address)) {
                    linkProperties.addDnsServer(parseNumericAddress);
                    z2 |= parseNumericAddress instanceof Inet4Address;
                    z3 |= parseNumericAddress instanceof Inet6Address;
                }
            }
        }
        linkProperties.setHttpProxy(this.mConfig.proxyInfo);
        if (!z2) {
            linkProperties.addRoute(new RouteInfo(new IpPrefix(NetworkStackConstants.IPV4_ADDR_ANY, 0), null, null, 7));
        }
        if (!z3 || z) {
            linkProperties.addRoute(new RouteInfo(new IpPrefix(NetworkStackConstants.IPV6_ADDR_ANY, 0), null, null, 7));
        }
        StringBuilder sb = new StringBuilder();
        List list4 = this.mConfig.searchDomains;
        if (list4 != null) {
            Iterator it2 = list4.iterator();
            while (it2.hasNext()) {
                sb.append((String) it2.next());
                sb.append(' ');
            }
        }
        linkProperties.setDomains(sb.toString().trim());
        int i = this.mConfig.mtu;
        if (i > 0) {
            linkProperties.setMtu(i);
        }
        return linkProperties;
    }

    private boolean updateLinkPropertiesInPlaceIfPossible(NetworkAgent networkAgent, VpnConfig vpnConfig) {
        boolean z = vpnConfig.allowBypass;
        VpnConfig vpnConfig2 = this.mConfig;
        if (z != vpnConfig2.allowBypass) {
            Log.i(TAG, "Handover not possible due to changes to allowBypass");
            return false;
        }
        if (!Objects.equals(vpnConfig.allowedApplications, vpnConfig2.allowedApplications) || !Objects.equals(vpnConfig.disallowedApplications, this.mConfig.disallowedApplications)) {
            Log.i(TAG, "Handover not possible due to changes to allowed/denied apps");
            return false;
        }
        networkAgent.sendLinkProperties(makeLinkProperties());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"this"})
    public void agentConnect() {
        agentConnect(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"this"})
    public void agentConnect(ValidationStatusCallback validationStatusCallback) {
        IkeSessionWrapper ikeSessionWrapper;
        LinkProperties makeLinkProperties = makeLinkProperties();
        NetworkCapabilities.Builder builder = new NetworkCapabilities.Builder(this.mNetworkCapabilities);
        builder.addCapability(12);
        this.mLegacyState = 2;
        updateState(NetworkInfo.DetailedState.CONNECTING, "agentConnect");
        boolean z = this.mConfig.allowBypass && !this.mLockdown;
        NetworkAgentConfig build = new NetworkAgentConfig.Builder().setLegacyType(17).setLegacyTypeName(NETWORKTYPE).setBypassableVpn(z).setVpnRequiresValidation(this.mConfig.requiresInternetValidation).setLocalRoutesExcludedForVpn(this.mConfig.excludeLocalRoutes).build();
        builder.setOwnerUid(this.mOwnerUID);
        builder.setAdministratorUids(new int[]{this.mOwnerUID});
        int i = this.mUserId;
        VpnConfig vpnConfig = this.mConfig;
        builder.setUids(createUserAndRestrictedProfilesRanges(i, vpnConfig.allowedApplications, vpnConfig.disallowedApplications));
        builder.setTransportInfo(new VpnTransportInfo(getActiveVpnType(), this.mConfig.session, z, areLongLivedTcpConnectionsExpensive(this.mVpnRunner)));
        if (this.mIsPackageTargetingAtLeastQ && this.mConfig.isMetered) {
            builder.removeCapability(11);
        } else {
            builder.addCapability(11);
        }
        Network[] networkArr = this.mConfig.underlyingNetworks;
        builder.setUnderlyingNetworks(networkArr != null ? Arrays.asList(networkArr) : null);
        NetworkCapabilities build2 = builder.build();
        this.mNetworkCapabilities = build2;
        logUnderlyNetworkChanges(build2.getUnderlyingNetworks());
        this.mNetworkAgent = this.mDeps.newNetworkAgent(this.mContext, this.mLooper, NETWORKTYPE, this.mNetworkCapabilities, makeLinkProperties, new NetworkScore.Builder().setLegacyInt(101).build(), build, this.mNetworkProvider, validationStatusCallback);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                this.mNetworkAgent.register();
                Binder.restoreCallingIdentity(clearCallingIdentity);
                IVpnExt iVpnExt = this.mVpnExt;
                if (iVpnExt != null) {
                    this.mConfig = iVpnExt.parseApplicationsFromXml(this.mConfig);
                }
                IVpnExt iVpnExt2 = this.mVpnExt;
                if (iVpnExt2 != null) {
                    PendingIntent prepareStatusIntent = iVpnExt2.prepareStatusIntent(this.mStatusIntent);
                    this.mStatusIntent = prepareStatusIntent;
                    this.mVpnExt.showNotification(null, 0, this.mUserId, this.mPackage, prepareStatusIntent, this.mConfig);
                }
                updateState(NetworkInfo.DetailedState.CONNECTED, "agentConnect");
                if (!isIkev2VpnRunner() || (ikeSessionWrapper = ((IkeV2VpnRunner) this.mVpnRunner).mSession) == null) {
                    return;
                }
                ikeSessionWrapper.setUnderpinnedNetwork(this.mNetworkAgent.getNetwork());
            } catch (Exception e) {
                this.mNetworkAgent = null;
                throw e;
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    private static boolean areLongLivedTcpConnectionsExpensive(VpnRunner vpnRunner) {
        if (vpnRunner instanceof IkeV2VpnRunner) {
            return areLongLivedTcpConnectionsExpensive(((IkeV2VpnRunner) vpnRunner).getOrGuessKeepaliveDelaySeconds());
        }
        return false;
    }

    private boolean canHaveRestrictedProfile(int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ((UserManager) this.mContext.createContextAsUser(UserHandle.of(i), 0).getSystemService(UserManager.class)).canHaveRestrictedProfile();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void logUnderlyNetworkChanges(List<Network> list) {
        LocalLog localLog = this.mEventChanges;
        StringBuilder sb = new StringBuilder();
        sb.append("[UnderlyingNW] Switch to ");
        sb.append(list != null ? TextUtils.join(", ", list) : "null");
        localLog.log(sb.toString());
    }

    private void agentDisconnect(NetworkAgent networkAgent) {
        if (networkAgent != null) {
            networkAgent.unregister();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void agentDisconnect() {
        updateState(NetworkInfo.DetailedState.DISCONNECTED, "agentDisconnect");
        IVpnExt iVpnExt = this.mVpnExt;
        if (iVpnExt != null) {
            iVpnExt.hideNotification(this.mUserId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"this"})
    public void startNewNetworkAgent(NetworkAgent networkAgent, String str) {
        this.mNetworkAgent = null;
        updateState(NetworkInfo.DetailedState.CONNECTING, str);
        agentConnect();
        agentDisconnect(networkAgent);
    }

    public synchronized ParcelFileDescriptor establish(VpnConfig vpnConfig) {
        if (Binder.getCallingUid() != this.mOwnerUID) {
            return null;
        }
        if (!isVpnServicePreConsented(this.mContext, this.mPackage)) {
            return null;
        }
        if (doesHaveVPNAppWhiteList()) {
            if (!isInVPNAppWhiteList(this.mPackage)) {
                Log.d(TAG, "prepareVpn, the VPN can't be estabilshed as the app is not in the white list");
                return null;
            }
        } else if (isVpnDisabled()) {
            Log.d(TAG, "prepareVpn, the VPN can't be estabilshed as the switch disable");
            return null;
        }
        Intent intent = new Intent("android.net.VpnService");
        intent.setClassName(this.mPackage, vpnConfig.user);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            enforceNotRestrictedUser();
            PackageManager packageManager = this.mUserIdContext.getPackageManager();
            if (packageManager == null) {
                throw new IllegalStateException("Cannot get PackageManager.");
            }
            ResolveInfo resolveService = packageManager.resolveService(intent, 0);
            if (resolveService == null) {
                throw new SecurityException("Cannot find " + vpnConfig.user);
            }
            if (!"android.permission.BIND_VPN_SERVICE".equals(resolveService.serviceInfo.permission)) {
                throw new SecurityException(vpnConfig.user + " does not require android.permission.BIND_VPN_SERVICE");
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            VpnConfig vpnConfig2 = this.mConfig;
            String str = this.mInterface;
            Connection connection = this.mConnection;
            NetworkAgent networkAgent = this.mNetworkAgent;
            Set uids = this.mNetworkCapabilities.getUids();
            ParcelFileDescriptor adoptFd = this.mDeps.adoptFd(this, vpnConfig.mtu);
            try {
                String jniGetName = this.mDeps.jniGetName(this, adoptFd.getFd());
                StringBuilder sb = new StringBuilder();
                for (LinkAddress linkAddress : vpnConfig.addresses) {
                    sb.append(" ");
                    sb.append(linkAddress);
                }
                if (this.mDeps.jniSetAddresses(this, jniGetName, sb.toString()) < 1) {
                    throw new IllegalArgumentException("At least one address must be specified");
                }
                Connection connection2 = new Connection();
                if (!this.mContext.bindServiceAsUser(intent, connection2, AudioFormat.AAC_MAIN, new UserHandle(this.mUserId))) {
                    throw new IllegalStateException("Cannot bind " + vpnConfig.user);
                }
                this.mConnection = connection2;
                this.mInterface = jniGetName;
                vpnConfig.user = this.mPackage;
                vpnConfig.interfaze = jniGetName;
                vpnConfig.startTime = SystemClock.elapsedRealtime();
                this.mConfig = vpnConfig;
                if (vpnConfig2 != null && updateLinkPropertiesInPlaceIfPossible(this.mNetworkAgent, vpnConfig2)) {
                    if (!Arrays.equals(vpnConfig2.underlyingNetworks, vpnConfig.underlyingNetworks)) {
                        setUnderlyingNetworks(vpnConfig.underlyingNetworks);
                    }
                } else {
                    startNewNetworkAgent(networkAgent, "establish");
                }
                if (connection != null) {
                    this.mContext.unbindService(connection);
                }
                if (str != null && !str.equals(jniGetName)) {
                    jniReset(str);
                }
                this.mDeps.setBlocking(adoptFd.getFileDescriptor(), vpnConfig.blocking);
                if (networkAgent != this.mNetworkAgent) {
                    this.mAppOpsManager.startOp("android:establish_vpn_service", this.mOwnerUID, this.mPackage, null, null);
                }
                Log.i(TAG, "Established by " + vpnConfig.user + " on " + this.mInterface);
                return adoptFd;
            } catch (RuntimeException e) {
                IoUtils.closeQuietly(adoptFd);
                if (networkAgent != this.mNetworkAgent) {
                    agentDisconnect();
                }
                this.mConfig = vpnConfig2;
                this.mConnection = connection;
                this.mNetworkCapabilities = new NetworkCapabilities.Builder(this.mNetworkCapabilities).setUids(uids).build();
                this.mNetworkAgent = networkAgent;
                this.mInterface = str;
                throw e;
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    private boolean isRunningLocked() {
        return (this.mNetworkAgent == null || this.mInterface == null) ? false : true;
    }

    @VisibleForTesting
    protected boolean isCallerEstablishedOwnerLocked() {
        return isRunningLocked() && Binder.getCallingUid() == this.mOwnerUID;
    }

    private SortedSet<Integer> getAppsUids(List<String> list, int i) {
        TreeSet treeSet = new TreeSet();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            int appUid = getAppUid(this.mContext, it.next(), i);
            if (appUid != -1) {
                treeSet.add(Integer.valueOf(appUid));
            }
            if (Process.isApplicationUid(appUid) && SdkLevel.isAtLeastT()) {
                treeSet.add(Integer.valueOf(Process.toSdkSandboxUid(appUid)));
            }
        }
        return treeSet;
    }

    @VisibleForTesting
    Set<Range<Integer>> createUserAndRestrictedProfilesRanges(int i, List<String> list, List<String> list2) {
        ArraySet arraySet = new ArraySet();
        addUserToRanges(arraySet, i, list, list2);
        if (canHaveRestrictedProfile(i)) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                List<UserInfo> aliveUsers = this.mUserManager.getAliveUsers();
                Binder.restoreCallingIdentity(clearCallingIdentity);
                for (UserInfo userInfo : aliveUsers) {
                    if (userInfo.isRestricted() && userInfo.restrictedProfileParentId == i) {
                        addUserToRanges(arraySet, userInfo.id, list, list2);
                    }
                }
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
        }
        return arraySet;
    }

    @VisibleForTesting
    void addUserToRanges(Set<Range<Integer>> set, int i, List<String> list, List<String> list2) {
        if (list != null) {
            Iterator<Integer> it = getAppsUids(list, i).iterator();
            int i2 = -1;
            int i3 = -1;
            while (it.hasNext()) {
                int intValue = it.next().intValue();
                if (i2 != -1) {
                    if (intValue != i3 + 1) {
                        set.add(new Range<>(Integer.valueOf(i2), Integer.valueOf(i3)));
                    } else {
                        i3 = intValue;
                    }
                }
                i2 = intValue;
                i3 = intValue;
            }
            if (i2 != -1) {
                set.add(new Range<>(Integer.valueOf(i2), Integer.valueOf(i3)));
                return;
            }
            return;
        }
        if (list2 != null) {
            Range<Integer> createUidRangeForUser = createUidRangeForUser(i);
            int intValue2 = createUidRangeForUser.getLower().intValue();
            Iterator<Integer> it2 = getAppsUids(list2, i).iterator();
            while (it2.hasNext()) {
                int intValue3 = it2.next().intValue();
                if (intValue3 == intValue2) {
                    intValue2++;
                } else {
                    set.add(new Range<>(Integer.valueOf(intValue2), Integer.valueOf(intValue3 - 1)));
                    intValue2 = intValue3 + 1;
                }
            }
            if (intValue2 <= createUidRangeForUser.getUpper().intValue()) {
                set.add(new Range<>(Integer.valueOf(intValue2), createUidRangeForUser.getUpper()));
                return;
            }
            return;
        }
        set.add(createUidRangeForUser(i));
    }

    private static List<Range<Integer>> uidRangesForUser(int i, Set<Range<Integer>> set) {
        Range<Integer> createUidRangeForUser = createUidRangeForUser(i);
        ArrayList arrayList = new ArrayList();
        for (Range<Integer> range : set) {
            if (createUidRangeForUser.contains(range)) {
                arrayList.add(range);
            }
        }
        return arrayList;
    }

    public void onUserAdded(int i) {
        UserInfo userInfo = this.mUserManager.getUserInfo(i);
        if (userInfo != null && userInfo.isRestricted() && userInfo.restrictedProfileParentId == this.mUserId) {
            synchronized (this) {
                Set uids = this.mNetworkCapabilities.getUids();
                if (uids != null) {
                    try {
                        IVpnExt iVpnExt = this.mVpnExt;
                        if (iVpnExt != null) {
                            this.mConfig = iVpnExt.parseApplicationsFromXml(this.mConfig);
                        }
                        VpnConfig vpnConfig = this.mConfig;
                        addUserToRanges(uids, i, vpnConfig.allowedApplications, vpnConfig.disallowedApplications);
                        this.mNetworkCapabilities = new NetworkCapabilities.Builder(this.mNetworkCapabilities).setUids(uids).build();
                        IVpnExt iVpnExt2 = this.mVpnExt;
                        if (iVpnExt2 != null) {
                            PendingIntent prepareStatusIntent = iVpnExt2.prepareStatusIntent(this.mStatusIntent);
                            this.mStatusIntent = prepareStatusIntent;
                            this.mVpnExt.showNotification(null, 0, this.mUserId, this.mPackage, prepareStatusIntent, this.mConfig);
                        }
                    } catch (Exception e) {
                        Log.wtf(TAG, "Failed to add restricted user to owner", e);
                    }
                    NetworkAgent networkAgent = this.mNetworkAgent;
                    if (networkAgent != null) {
                        doSendNetworkCapabilities(networkAgent, this.mNetworkCapabilities);
                    }
                }
                setVpnForcedLocked(this.mLockdown);
            }
        }
    }

    public void onUserRemoved(int i) {
        UserInfo userInfo = this.mUserManager.getUserInfo(i);
        if (userInfo != null && userInfo.isRestricted() && userInfo.restrictedProfileParentId == this.mUserId) {
            synchronized (this) {
                Set uids = this.mNetworkCapabilities.getUids();
                if (uids != null) {
                    try {
                        uids.removeAll(uidRangesForUser(i, uids));
                        this.mNetworkCapabilities = new NetworkCapabilities.Builder(this.mNetworkCapabilities).setUids(uids).build();
                    } catch (Exception e) {
                        Log.wtf(TAG, "Failed to remove restricted user to owner", e);
                    }
                    NetworkAgent networkAgent = this.mNetworkAgent;
                    if (networkAgent != null) {
                        doSendNetworkCapabilities(networkAgent, this.mNetworkCapabilities);
                    }
                }
                setVpnForcedLocked(this.mLockdown);
            }
        }
    }

    public synchronized void onUserStopped() {
        setVpnForcedLocked(false);
        this.mAlwaysOn = false;
        agentDisconnect();
        this.mConnectivityManager.unregisterNetworkProvider(this.mNetworkProvider);
    }

    @GuardedBy({"this"})
    private void setVpnForcedLocked(boolean z) {
        List<String> arrayList;
        Set emptySet;
        if (isNullOrLegacyVpn(this.mPackage)) {
            arrayList = null;
        } else {
            arrayList = new ArrayList<>(this.mLockdownAllowlist);
            arrayList.add(this.mPackage);
        }
        ArraySet arraySet = new ArraySet(this.mBlockedUidsAsToldToConnectivity);
        if (z) {
            Set<Range<Integer>> createUserAndRestrictedProfilesRanges = createUserAndRestrictedProfilesRanges(this.mUserId, null, arrayList);
            emptySet = new ArraySet();
            for (Range<Integer> range : createUserAndRestrictedProfilesRanges) {
                if (range.getLower().intValue() == 0 && range.getUpper().intValue() != 0) {
                    emptySet.add(new UidRangeParcel(1, range.getUpper().intValue()));
                } else if (range.getLower().intValue() != 0) {
                    emptySet.add(new UidRangeParcel(range.getLower().intValue(), range.getUpper().intValue()));
                }
            }
            arraySet.removeAll(emptySet);
            emptySet.removeAll(this.mBlockedUidsAsToldToConnectivity);
        } else {
            emptySet = Collections.emptySet();
        }
        setAllowOnlyVpnForUids(false, arraySet);
        setAllowOnlyVpnForUids(true, emptySet);
    }

    @GuardedBy({"this"})
    private boolean setAllowOnlyVpnForUids(boolean z, Collection<UidRangeParcel> collection) {
        if (collection.size() == 0) {
            return true;
        }
        ArrayList arrayList = new ArrayList(collection.size());
        for (UidRangeParcel uidRangeParcel : collection) {
            arrayList.add(new Range(Integer.valueOf(uidRangeParcel.start), Integer.valueOf(uidRangeParcel.stop)));
        }
        try {
            this.mConnectivityManager.setRequireVpnForUids(z, arrayList);
            if (z) {
                this.mBlockedUidsAsToldToConnectivity.addAll(collection);
            } else {
                this.mBlockedUidsAsToldToConnectivity.removeAll(collection);
            }
            return true;
        } catch (RuntimeException e) {
            Log.e(TAG, "Updating blocked=" + z + " for UIDs " + Arrays.toString(collection.toArray()) + " failed", e);
            return false;
        }
    }

    public synchronized VpnConfig getVpnConfig() {
        enforceControlPermission();
        return this.mConfig;
    }

    @Deprecated
    public synchronized void interfaceStatusChanged(String str, boolean z) {
        try {
            this.mObserver.interfaceStatusChanged(str, z);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupVpnStateLocked() {
        this.mStatusIntent = null;
        resetNetworkCapabilities();
        this.mConfig = null;
        this.mInterface = null;
        this.mVpnRunner = null;
        this.mConnection = null;
        agentDisconnect();
    }

    private void enforceControlPermission() {
        this.mContext.enforceCallingPermission("android.permission.CONTROL_VPN", "Unauthorized Caller");
    }

    private void enforceControlPermissionOrInternalCaller() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.CONTROL_VPN", "Unauthorized Caller");
    }

    private void enforceSettingsPermission() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.NETWORK_SETTINGS", "Unauthorized Caller");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class Connection implements ServiceConnection {
        private IBinder mService;

        private Connection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            this.mService = iBinder;
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            this.mService = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareStatusIntent() {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mStatusIntent = this.mDeps.getIntentForStatusPanel(this.mContext);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public synchronized boolean addAddress(String str, int i) {
        if (!isCallerEstablishedOwnerLocked()) {
            return false;
        }
        boolean jniAddAddress = jniAddAddress(this.mInterface, str, i);
        doSendLinkProperties(this.mNetworkAgent, makeLinkProperties());
        return jniAddAddress;
    }

    public synchronized boolean removeAddress(String str, int i) {
        if (!isCallerEstablishedOwnerLocked()) {
            return false;
        }
        boolean jniDelAddress = jniDelAddress(this.mInterface, str, i);
        doSendLinkProperties(this.mNetworkAgent, makeLinkProperties());
        return jniDelAddress;
    }

    public synchronized boolean setUnderlyingNetworks(Network[] networkArr) {
        if (!isCallerEstablishedOwnerLocked()) {
            return false;
        }
        this.mConfig.underlyingNetworks = networkArr != null ? (Network[]) Arrays.copyOf(networkArr, networkArr.length) : null;
        NetworkAgent networkAgent = this.mNetworkAgent;
        Network[] networkArr2 = this.mConfig.underlyingNetworks;
        doSetUnderlyingNetworks(networkAgent, networkArr2 != null ? Arrays.asList(networkArr2) : null);
        return true;
    }

    public synchronized UnderlyingNetworkInfo getUnderlyingNetworkInfo() {
        if (!isRunningLocked()) {
            return null;
        }
        return new UnderlyingNetworkInfo(this.mOwnerUID, this.mInterface, new ArrayList());
    }

    public synchronized boolean appliesToUid(int i) {
        if (!isRunningLocked()) {
            return false;
        }
        Set uids = this.mNetworkCapabilities.getUids();
        if (uids == null) {
            return true;
        }
        Iterator it = uids.iterator();
        while (it.hasNext()) {
            if (((Range) it.next()).contains((Range) Integer.valueOf(i))) {
                return true;
            }
        }
        return false;
    }

    public synchronized int getActiveVpnType() {
        if (!this.mNetworkInfo.isConnectedOrConnecting()) {
            return -1;
        }
        if (this.mVpnRunner == null) {
            return 1;
        }
        return isIkev2VpnRunner() ? 2 : 3;
    }

    @GuardedBy({"this"})
    private void updateAlwaysOnNotification(NetworkInfo.DetailedState detailedState) {
        boolean z = this.mAlwaysOn && detailedState != NetworkInfo.DetailedState.CONNECTED;
        UserHandle of = UserHandle.of(this.mUserId);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            NotificationManager notificationManager = (NotificationManager) this.mUserIdContext.getSystemService(NotificationManager.class);
            if (!z) {
                notificationManager.cancel(TAG, 17);
                return;
            }
            Intent intent = new Intent();
            intent.setComponent(ComponentName.unflattenFromString(this.mContext.getString(R.string.config_headlineFontFeatureSettings)));
            intent.putExtra("lockdown", this.mLockdown);
            intent.addFlags(AudioFormat.EVRC);
            notificationManager.notify(TAG, 17, new Notification.Builder(this.mContext, NETWORKTYPE).setSmallIcon(17303919).setContentTitle(this.mContext.getString(17041843)).setContentText(this.mContext.getString(17041840)).setContentIntent(this.mSystemServices.pendingIntentGetActivityAsUser(intent, AudioFormat.DTS_HD, of)).setCategory("sys").setVisibility(1).setOngoing(true).setColor(this.mContext.getColor(R.color.system_notification_accent_color)).build());
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class SystemServices {
        private final Context mContext;

        public SystemServices(Context context) {
            this.mContext = context;
        }

        public PendingIntent pendingIntentGetActivityAsUser(Intent intent, int i, UserHandle userHandle) {
            return PendingIntent.getActivity(this.mContext.createContextAsUser(userHandle, 0), 0, intent, i);
        }

        public void settingsSecurePutStringForUser(String str, String str2, int i) {
            Settings.Secure.putString(getContentResolverAsUser(i), str, str2);
        }

        public void settingsSecurePutIntForUser(String str, int i, int i2) {
            Settings.Secure.putInt(getContentResolverAsUser(i2), str, i);
        }

        public String settingsSecureGetStringForUser(String str, int i) {
            return Settings.Secure.getString(getContentResolverAsUser(i), str);
        }

        public int settingsSecureGetIntForUser(String str, int i, int i2) {
            return Settings.Secure.getInt(getContentResolverAsUser(i2), str, i);
        }

        private ContentResolver getContentResolverAsUser(int i) {
            return this.mContext.createContextAsUser(UserHandle.of(i), 0).getContentResolver();
        }
    }

    private static RouteInfo findIPv4DefaultRoute(LinkProperties linkProperties) {
        for (RouteInfo routeInfo : linkProperties.getAllRoutes()) {
            if (routeInfo.isDefaultRoute() && (routeInfo.getGateway() instanceof Inet4Address)) {
                return routeInfo;
            }
        }
        throw new IllegalStateException("Unable to find IPv4 default gateway");
    }

    private void enforceNotRestrictedUser() {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (this.mUserManager.getUserInfo(this.mUserId).isRestricted()) {
                throw new SecurityException("Restricted users cannot configure VPNs");
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void startLegacyVpn(VpnProfile vpnProfile, Network network, LinkProperties linkProperties) {
        enforceControlPermission();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            startLegacyVpnPrivileged(vpnProfile, network, linkProperties);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private String makeKeystoreEngineGrantString(String str) {
        if (str == null) {
            return null;
        }
        KeyStore2 keyStore2 = KeyStore2.getInstance();
        KeyDescriptor keyDescriptor = new KeyDescriptor();
        keyDescriptor.domain = 0;
        keyDescriptor.nspace = -1L;
        keyDescriptor.alias = str;
        keyDescriptor.blob = null;
        try {
            return KeyStore2.makeKeystoreEngineGrantString(keyStore2.grant(keyDescriptor, 1016, 260).nspace);
        } catch (KeyStoreException e) {
            Log.e(TAG, "Failed to get grant for keystore key.", e);
            throw new IllegalStateException("Failed to get grant for keystore key.", e);
        }
    }

    private String getCaCertificateFromKeystoreAsPem(KeyStore keyStore, String str) throws java.security.KeyStoreException, IOException, CertificateEncodingException {
        if (keyStore.isCertificateEntry(str)) {
            Certificate certificate = keyStore.getCertificate(str);
            if (certificate == null) {
                return null;
            }
            return new String(Credentials.convertToPem(new Certificate[]{certificate}), StandardCharsets.UTF_8);
        }
        Certificate[] certificateChain = keyStore.getCertificateChain(str);
        if (certificateChain == null || certificateChain.length <= 1) {
            return null;
        }
        return new String(Credentials.convertToPem((Certificate[]) Arrays.copyOfRange(certificateChain, 1, certificateChain.length)), StandardCharsets.UTF_8);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:33:0x00ae. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0160  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0252  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0267  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x027b  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01a6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void startLegacyVpnPrivileged(VpnProfile vpnProfile, Network network, LinkProperties linkProperties) {
        String str;
        String str2;
        String[] strArr;
        int i;
        boolean z;
        boolean z2;
        VpnProfile clone = vpnProfile.clone();
        UserInfo userInfo = this.mUserManager.getUserInfo(this.mUserId);
        if (userInfo == null) {
            Log.i(TAG, "startLegacyVpnPrivileged user == null");
            return;
        }
        if (userInfo.isRestricted() || this.mUserManager.hasUserRestriction("no_config_vpn", new UserHandle(this.mUserId))) {
            throw new SecurityException("Restricted users cannot establish VPNs");
        }
        RouteInfo findIPv4DefaultRoute = findIPv4DefaultRoute(linkProperties);
        String hostAddress = findIPv4DefaultRoute.getGateway().getHostAddress();
        String str3 = findIPv4DefaultRoute.getInterface();
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE_PROVIDER);
            String[] strArr2 = null;
            keyStore.load(null);
            String str4 = "";
            if (clone.ipsecUserCert.isEmpty()) {
                str = "";
                str2 = str;
            } else {
                str = clone.ipsecUserCert;
                Certificate certificate = keyStore.getCertificate(str);
                str2 = certificate == null ? null : new String(Credentials.convertToPem(new Certificate[]{certificate}), StandardCharsets.UTF_8);
            }
            String caCertificateFromKeystoreAsPem = !clone.ipsecCaCert.isEmpty() ? getCaCertificateFromKeystoreAsPem(keyStore, clone.ipsecCaCert) : "";
            if (!clone.ipsecServerCert.isEmpty()) {
                Certificate certificate2 = keyStore.getCertificate(clone.ipsecServerCert);
                str4 = certificate2 == null ? null : new String(Credentials.convertToPem(new Certificate[]{certificate2}), StandardCharsets.UTF_8);
            }
            String str5 = str4;
            if (str2 == null || caCertificateFromKeystoreAsPem == null || str5 == null) {
                throw new IllegalStateException("Cannot load credentials");
            }
            switch (clone.type) {
                case 1:
                    strArr = new String[]{str3, clone.server, "udppsk", clone.ipsecIdentifier, clone.ipsecSecret, "1701"};
                    i = clone.type;
                    if (i != 0) {
                        z = false;
                        strArr2 = new String[20];
                        strArr2[0] = str3;
                        z2 = true;
                        strArr2[1] = "pptp";
                        strArr2[2] = clone.server;
                        strArr2[3] = "1723";
                        strArr2[4] = "name";
                        strArr2[5] = clone.username;
                        strArr2[6] = HintsHelper.AUTOFILL_HINT_PASSWORD;
                        strArr2[7] = clone.password;
                        strArr2[8] = "linkname";
                        strArr2[9] = IOplusSceneManager.APP_SCENE_VPN;
                        strArr2[10] = "refuse-eap";
                        strArr2[11] = "nodefaultroute";
                        strArr2[12] = "usepeerdns";
                        strArr2[13] = "idle";
                        strArr2[14] = "1800";
                        strArr2[15] = "mtu";
                        strArr2[16] = "1270";
                        strArr2[17] = "mru";
                        strArr2[18] = "1270";
                        boolean z3 = clone.mppe;
                        strArr2[19] = z3 ? "+mppe" : "nomppe";
                        if (z3) {
                            strArr2 = (String[]) Arrays.copyOf(strArr2, 21);
                            strArr2[strArr2.length - 1] = "-pap";
                        }
                    } else if (i == 1 || i == 2) {
                        z = false;
                        strArr2 = new String[]{str3, "l2tp", clone.server, "1701", clone.l2tpSecret, "name", clone.username, HintsHelper.AUTOFILL_HINT_PASSWORD, clone.password, "linkname", IOplusSceneManager.APP_SCENE_VPN, "refuse-eap", "nodefaultroute", "usepeerdns", "idle", "1800", "mtu", "1270", "mru", "1270"};
                        z2 = true;
                    } else {
                        z2 = true;
                        z = false;
                    }
                    VpnConfig vpnConfig = new VpnConfig();
                    vpnConfig.legacy = z2;
                    vpnConfig.user = clone.key;
                    vpnConfig.interfaze = str3;
                    vpnConfig.session = clone.name;
                    vpnConfig.isMetered = z;
                    vpnConfig.proxyInfo = clone.proxy;
                    if (network != null) {
                        vpnConfig.underlyingNetworks = new Network[]{network};
                    }
                    vpnConfig.addLegacyRoutes(clone.routes);
                    if (!clone.dnsServers.isEmpty()) {
                        vpnConfig.dnsServers = Arrays.asList(clone.dnsServers.split(" +"));
                    }
                    if (!clone.searchDomains.isEmpty()) {
                        vpnConfig.searchDomains = Arrays.asList(clone.searchDomains.split(" +"));
                    }
                    startLegacyVpn(vpnConfig, strArr, strArr2, clone);
                    return;
                case 2:
                    strArr = new String[]{str3, clone.server, "udprsa", makeKeystoreEngineGrantString(str), str2, caCertificateFromKeystoreAsPem, str5, "1701"};
                    i = clone.type;
                    if (i != 0) {
                    }
                    VpnConfig vpnConfig2 = new VpnConfig();
                    vpnConfig2.legacy = z2;
                    vpnConfig2.user = clone.key;
                    vpnConfig2.interfaze = str3;
                    vpnConfig2.session = clone.name;
                    vpnConfig2.isMetered = z;
                    vpnConfig2.proxyInfo = clone.proxy;
                    if (network != null) {
                    }
                    vpnConfig2.addLegacyRoutes(clone.routes);
                    if (!clone.dnsServers.isEmpty()) {
                    }
                    if (!clone.searchDomains.isEmpty()) {
                    }
                    startLegacyVpn(vpnConfig2, strArr, strArr2, clone);
                    return;
                case 3:
                    strArr = new String[]{str3, clone.server, "xauthpsk", clone.ipsecIdentifier, clone.ipsecSecret, clone.username, clone.password, "", hostAddress};
                    i = clone.type;
                    if (i != 0) {
                    }
                    VpnConfig vpnConfig22 = new VpnConfig();
                    vpnConfig22.legacy = z2;
                    vpnConfig22.user = clone.key;
                    vpnConfig22.interfaze = str3;
                    vpnConfig22.session = clone.name;
                    vpnConfig22.isMetered = z;
                    vpnConfig22.proxyInfo = clone.proxy;
                    if (network != null) {
                    }
                    vpnConfig22.addLegacyRoutes(clone.routes);
                    if (!clone.dnsServers.isEmpty()) {
                    }
                    if (!clone.searchDomains.isEmpty()) {
                    }
                    startLegacyVpn(vpnConfig22, strArr, strArr2, clone);
                    return;
                case 4:
                    strArr = new String[]{str3, clone.server, "xauthrsa", makeKeystoreEngineGrantString(str), str2, caCertificateFromKeystoreAsPem, str5, clone.username, clone.password, "", hostAddress};
                    i = clone.type;
                    if (i != 0) {
                    }
                    VpnConfig vpnConfig222 = new VpnConfig();
                    vpnConfig222.legacy = z2;
                    vpnConfig222.user = clone.key;
                    vpnConfig222.interfaze = str3;
                    vpnConfig222.session = clone.name;
                    vpnConfig222.isMetered = z;
                    vpnConfig222.proxyInfo = clone.proxy;
                    if (network != null) {
                    }
                    vpnConfig222.addLegacyRoutes(clone.routes);
                    if (!clone.dnsServers.isEmpty()) {
                    }
                    if (!clone.searchDomains.isEmpty()) {
                    }
                    startLegacyVpn(vpnConfig222, strArr, strArr2, clone);
                    return;
                case 5:
                    strArr = new String[]{str3, clone.server, "hybridrsa", caCertificateFromKeystoreAsPem, str5, clone.username, clone.password, "", hostAddress};
                    i = clone.type;
                    if (i != 0) {
                    }
                    VpnConfig vpnConfig2222 = new VpnConfig();
                    vpnConfig2222.legacy = z2;
                    vpnConfig2222.user = clone.key;
                    vpnConfig2222.interfaze = str3;
                    vpnConfig2222.session = clone.name;
                    vpnConfig2222.isMetered = z;
                    vpnConfig2222.proxyInfo = clone.proxy;
                    if (network != null) {
                    }
                    vpnConfig2222.addLegacyRoutes(clone.routes);
                    if (!clone.dnsServers.isEmpty()) {
                    }
                    if (!clone.searchDomains.isEmpty()) {
                    }
                    startLegacyVpn(vpnConfig2222, strArr, strArr2, clone);
                    return;
                case 6:
                    clone.ipsecCaCert = caCertificateFromKeystoreAsPem;
                    clone.setAllowedAlgorithms(Ikev2VpnProfile.DEFAULT_ALGORITHMS);
                    startVpnProfilePrivileged(clone, "[Legacy VPN]");
                    return;
                case 7:
                    clone.ipsecSecret = Ikev2VpnProfile.encodeForIpsecSecret(clone.ipsecSecret.getBytes());
                    clone.setAllowedAlgorithms(Ikev2VpnProfile.DEFAULT_ALGORITHMS);
                    startVpnProfilePrivileged(clone, "[Legacy VPN]");
                    return;
                case 8:
                    clone.ipsecSecret = "KEYSTORE_ALIAS:" + str;
                    clone.ipsecUserCert = str2;
                    clone.ipsecCaCert = caCertificateFromKeystoreAsPem;
                    clone.setAllowedAlgorithms(Ikev2VpnProfile.DEFAULT_ALGORITHMS);
                    startVpnProfilePrivileged(clone, "[Legacy VPN]");
                    return;
                case 9:
                    startVpnProfilePrivileged(clone, "[Legacy VPN]");
                    return;
                default:
                    strArr = null;
                    i = clone.type;
                    if (i != 0) {
                    }
                    VpnConfig vpnConfig22222 = new VpnConfig();
                    vpnConfig22222.legacy = z2;
                    vpnConfig22222.user = clone.key;
                    vpnConfig22222.interfaze = str3;
                    vpnConfig22222.session = clone.name;
                    vpnConfig22222.isMetered = z;
                    vpnConfig22222.proxyInfo = clone.proxy;
                    if (network != null) {
                    }
                    vpnConfig22222.addLegacyRoutes(clone.routes);
                    if (!clone.dnsServers.isEmpty()) {
                    }
                    if (!clone.searchDomains.isEmpty()) {
                    }
                    startLegacyVpn(vpnConfig22222, strArr, strArr2, clone);
                    return;
            }
        } catch (IOException | java.security.KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            throw new IllegalStateException("Failed to load credentials from AndroidKeyStore", e);
        }
    }

    private synchronized void startLegacyVpn(VpnConfig vpnConfig, String[] strArr, String[] strArr2, VpnProfile vpnProfile) {
        if (doesHaveVPNAppWhiteList()) {
            if (!isInVPNAppWhiteList(this.mPackage)) {
                Log.d(TAG, "prepareVpn, the VPN can't be estabilshed as the app is not in the white list");
                return;
            }
        } else if (isVpnDisabled()) {
            Log.d(TAG, "prepareVpn, the VPN can't be estabilshed as the switch disable");
            return;
        }
        stopVpnRunnerPrivileged();
        prepareInternal("[Legacy VPN]");
        updateState(NetworkInfo.DetailedState.CONNECTING, "startLegacyVpn");
        this.mVpnRunner = new LegacyVpnRunner(vpnConfig, strArr, strArr2, vpnProfile);
        startLegacyVpnRunner();
    }

    @VisibleForTesting
    protected void startLegacyVpnRunner() {
        this.mVpnRunner.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSettingsVpnLocked() {
        return this.mVpnRunner != null && "[Legacy VPN]".equals(this.mPackage);
    }

    public synchronized void stopVpnRunnerPrivileged() {
        if (isSettingsVpnLocked()) {
            VpnRunner vpnRunner = this.mVpnRunner;
            boolean z = vpnRunner instanceof LegacyVpnRunner;
            vpnRunner.exit();
            this.mVpnRunner = null;
            if (z) {
                synchronized ("LegacyVpnRunner") {
                }
            }
        }
    }

    public synchronized LegacyVpnInfo getLegacyVpnInfo() {
        enforceControlPermission();
        return getLegacyVpnInfoPrivileged();
    }

    private synchronized LegacyVpnInfo getLegacyVpnInfoPrivileged() {
        if (!isSettingsVpnLocked()) {
            return null;
        }
        LegacyVpnInfo legacyVpnInfo = new LegacyVpnInfo();
        legacyVpnInfo.key = this.mConfig.user;
        legacyVpnInfo.state = this.mLegacyState;
        if (this.mNetworkInfo.isConnected()) {
            legacyVpnInfo.intent = this.mStatusIntent;
        }
        return legacyVpnInfo;
    }

    public synchronized VpnConfig getLegacyVpnConfig() {
        if (!isSettingsVpnLocked()) {
            return null;
        }
        return this.mConfig;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized NetworkCapabilities getRedactedNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities != null) {
            if (!isNullOrLegacyVpn(this.mPackage)) {
                return this.mConnectivityManager.getRedactedNetworkCapabilitiesForPackage(networkCapabilities, this.mOwnerUID, this.mPackage);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized LinkProperties getRedactedLinkProperties(LinkProperties linkProperties) {
        if (linkProperties == null) {
            return null;
        }
        return this.mConnectivityManager.getRedactedLinkPropertiesForPackage(linkProperties, this.mOwnerUID, this.mPackage);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public abstract class VpnRunner extends Thread {
        protected abstract void exitVpnRunner();

        @Override // java.lang.Thread, java.lang.Runnable
        public abstract void run();

        protected VpnRunner(String str) {
            super(str);
        }

        protected final void exit() {
            synchronized (Vpn.this) {
                exitVpnRunner();
                Vpn.this.cleanupVpnStateLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isIPv6Only(List<LinkAddress> list) {
        boolean z = false;
        boolean z2 = false;
        for (LinkAddress linkAddress : list) {
            z |= linkAddress.isIpv6();
            z2 |= linkAddress.isIpv4();
        }
        return z && !z2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVpnNetworkPreference(final String str, final Set<Range<Integer>> set) {
        BinderUtils.withCleanCallingIdentity(new BinderUtils.ThrowingRunnable() { // from class: com.android.server.connectivity.Vpn$$ExternalSyntheticLambda1
            public final void run() {
                Vpn.this.lambda$setVpnNetworkPreference$0(str, set);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setVpnNetworkPreference$0(String str, Set set) throws RuntimeException {
        this.mConnectivityManager.setVpnDefaultForUids(str, set);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearVpnNetworkPreference(final String str) {
        BinderUtils.withCleanCallingIdentity(new BinderUtils.ThrowingRunnable() { // from class: com.android.server.connectivity.Vpn$$ExternalSyntheticLambda0
            public final void run() {
                Vpn.this.lambda$clearVpnNetworkPreference$1(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearVpnNetworkPreference$1(String str) throws RuntimeException {
        this.mConnectivityManager.setVpnDefaultForUids(str, Collections.EMPTY_LIST);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class IkeV2VpnRunner extends VpnRunner implements IkeV2VpnRunnerCallback {
        private static final long NETWORK_LOST_TIMEOUT_MS = 5000;
        private static final String TAG = "IkeV2VpnRunner";
        private Network mActiveNetwork;
        private CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
        private int mCurrentToken;
        private int mDataStallRetryCount;
        private VpnConnectivityDiagnosticsCallback mDiagnosticsCallback;
        private final ScheduledThreadPoolExecutor mExecutor;
        private IkeSessionConnectionInfo mIkeConnectionInfo;
        private final IpSecManager mIpSecManager;
        private boolean mIsRunning;
        private boolean mMobikeEnabled;
        private final ConnectivityManager.NetworkCallback mNetworkCallback;
        private final Ikev2VpnProfile mProfile;
        private int mRetryCount;
        private ScheduledFuture<?> mScheduledHandleDataStallFuture;
        private ScheduledFuture<?> mScheduledHandleNetworkLostFuture;
        private ScheduledFuture<?> mScheduledHandleRetryIkeSessionFuture;
        private IkeSessionWrapper mSession;
        private final String mSessionKey;
        private IpSecManager.IpSecTunnelInterface mTunnelIface;
        private LinkProperties mUnderlyingLinkProperties;
        private NetworkCapabilities mUnderlyingNetworkCapabilities;

        IkeV2VpnRunner(Ikev2VpnProfile ikev2VpnProfile, ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
            super(TAG);
            this.mIsRunning = true;
            this.mCurrentToken = -1;
            this.mMobikeEnabled = false;
            this.mDataStallRetryCount = 0;
            this.mRetryCount = 0;
            this.mCarrierConfigChangeListener = new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.server.connectivity.Vpn.IkeV2VpnRunner.1
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                    Vpn.this.mEventChanges.log("[CarrierConfig] Changed on slot " + i + " subId=" + i2 + " carrerId=" + i3 + " specificCarrierId=" + i4);
                    synchronized (Vpn.this) {
                        Vpn.this.mCachedCarrierConfigInfoPerSubId.remove(i2);
                        IkeV2VpnRunner ikeV2VpnRunner = IkeV2VpnRunner.this;
                        if (Vpn.this.mVpnRunner != ikeV2VpnRunner) {
                            return;
                        }
                        ikeV2VpnRunner.maybeMigrateIkeSessionAndUpdateVpnTransportInfo(ikeV2VpnRunner.mActiveNetwork);
                    }
                }
            };
            this.mProfile = ikev2VpnProfile;
            this.mExecutor = scheduledThreadPoolExecutor;
            this.mIpSecManager = (IpSecManager) Vpn.this.mContext.getSystemService(INetd.IPSEC_INTERFACE_PREFIX);
            this.mNetworkCallback = new VpnIkev2Utils.Ikev2VpnNetworkCallback(TAG, this, scheduledThreadPoolExecutor);
            String uuid = UUID.randomUUID().toString();
            this.mSessionKey = uuid;
            Log.d(TAG, "Generate session key = " + uuid);
            scheduledThreadPoolExecutor.setRemoveOnCancelPolicy(true);
            scheduledThreadPoolExecutor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
            scheduledThreadPoolExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() { // from class: com.android.server.connectivity.Vpn$IkeV2VpnRunner$$ExternalSyntheticLambda5
                @Override // java.util.concurrent.RejectedExecutionHandler
                public final void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                    Vpn.IkeV2VpnRunner.lambda$new$0(runnable, threadPoolExecutor);
                }
            });
            int i = Vpn.this.mUserId;
            VpnConfig vpnConfig = Vpn.this.mConfig;
            Vpn.this.setVpnNetworkPreference(uuid, Vpn.this.createUserAndRestrictedProfilesRanges(i, vpnConfig.allowedApplications, vpnConfig.disallowedApplications));
            Vpn.this.mCarrierConfigManager.registerCarrierConfigChangeListener(scheduledThreadPoolExecutor, this.mCarrierConfigChangeListener);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$new$0(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            Log.d(TAG, "Runnable " + runnable + " rejected by the mExecutor");
        }

        @Override // com.android.server.connectivity.Vpn.VpnRunner, java.lang.Thread, java.lang.Runnable
        public void run() {
            if (this.mProfile.isRestrictedToTestNetworks()) {
                Vpn.this.mConnectivityManager.requestNetwork(new NetworkRequest.Builder().clearCapabilities().addTransportType(7).addCapability(15).build(), this.mNetworkCallback);
            } else {
                Vpn.this.mConnectivityManager.registerSystemDefaultNetworkCallback(this.mNetworkCallback, new Handler(Vpn.this.mLooper));
            }
            NetworkRequest build = new NetworkRequest.Builder().addTransportType(4).removeCapability(15).build();
            this.mDiagnosticsCallback = new VpnConnectivityDiagnosticsCallback();
            Vpn.this.mConnectivityDiagnosticsManager.registerConnectivityDiagnosticsCallback(build, this.mExecutor, this.mDiagnosticsCallback);
        }

        private boolean isActiveNetwork(Network network) {
            return Objects.equals(this.mActiveNetwork, network) && this.mIsRunning;
        }

        private boolean isActiveToken(int i) {
            return this.mCurrentToken == i && this.mIsRunning;
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onIkeOpened(int i, IkeSessionConfiguration ikeSessionConfiguration) {
            if (!isActiveToken(i)) {
                Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onIkeOpened obsolete token=" + i);
                StringBuilder sb = new StringBuilder();
                sb.append("onIkeOpened called for obsolete token ");
                sb.append(i);
                Log.d(TAG, sb.toString());
                return;
            }
            this.mMobikeEnabled = ikeSessionConfiguration.isIkeExtensionEnabled(2);
            IkeSessionConnectionInfo ikeSessionConnectionInfo = ikeSessionConfiguration.getIkeSessionConnectionInfo();
            Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onIkeOpened token=" + i + ", localAddr=" + ikeSessionConnectionInfo.getLocalAddress() + ", network=" + ikeSessionConnectionInfo.getNetwork() + ", mobikeEnabled= " + this.mMobikeEnabled);
            onIkeConnectionInfoChanged(i, ikeSessionConnectionInfo);
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onIkeConnectionInfoChanged(int i, IkeSessionConnectionInfo ikeSessionConnectionInfo) {
            if (!isActiveToken(i)) {
                Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onIkeConnectionInfoChanged obsolete token=" + i);
                StringBuilder sb = new StringBuilder();
                sb.append("onIkeConnectionInfoChanged called for obsolete token ");
                sb.append(i);
                Log.d(TAG, sb.toString());
                return;
            }
            Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onIkeConnectionInfoChanged token=" + i + ", localAddr=" + ikeSessionConnectionInfo.getLocalAddress() + ", network=" + ikeSessionConnectionInfo.getNetwork());
            this.mIkeConnectionInfo = ikeSessionConnectionInfo;
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onChildOpened(int i, ChildSessionConfiguration childSessionConfiguration) {
            if (!isActiveToken(i)) {
                Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onChildOpened obsolete token=" + i);
                StringBuilder sb = new StringBuilder();
                sb.append("onChildOpened called for obsolete token ");
                sb.append(i);
                Log.d(TAG, sb.toString());
                return;
            }
            Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onChildOpened token=" + i + ", addr=" + TextUtils.join(", ", childSessionConfiguration.getInternalAddresses()) + " dns=" + TextUtils.join(", ", childSessionConfiguration.getInternalDnsServers()));
            try {
                String interfaceName = this.mTunnelIface.getInterfaceName();
                List<LinkAddress> internalAddresses = childSessionConfiguration.getInternalAddresses();
                ArrayList arrayList = new ArrayList();
                int calculateVpnMtu = calculateVpnMtu();
                if (Vpn.isIPv6Only(internalAddresses) && calculateVpnMtu < 1280) {
                    onSessionLost(i, new IkeIOException(new IOException("No valid addresses for MTU < 1280")));
                    return;
                }
                Collection<RouteInfo> routesFromTrafficSelectors = VpnIkev2Utils.getRoutesFromTrafficSelectors(childSessionConfiguration.getOutboundTrafficSelectors());
                for (LinkAddress linkAddress : internalAddresses) {
                    this.mTunnelIface.addAddress(linkAddress.getAddress(), linkAddress.getPrefixLength());
                }
                Iterator it = childSessionConfiguration.getInternalDnsServers().iterator();
                while (it.hasNext()) {
                    arrayList.add(((InetAddress) it.next()).getHostAddress());
                }
                Network network = this.mIkeConnectionInfo.getNetwork();
                synchronized (Vpn.this) {
                    Vpn vpn = Vpn.this;
                    if (vpn.mVpnRunner != this) {
                        return;
                    }
                    vpn.mInterface = interfaceName;
                    VpnConfig vpnConfig = vpn.mConfig;
                    vpnConfig.mtu = calculateVpnMtu;
                    vpnConfig.interfaze = interfaceName;
                    vpnConfig.addresses.clear();
                    Vpn.this.mConfig.addresses.addAll(internalAddresses);
                    Vpn.this.mConfig.routes.clear();
                    Vpn.this.mConfig.routes.addAll(routesFromTrafficSelectors);
                    VpnConfig vpnConfig2 = Vpn.this.mConfig;
                    if (vpnConfig2.dnsServers == null) {
                        vpnConfig2.dnsServers = new ArrayList();
                    }
                    Vpn.this.mConfig.dnsServers.clear();
                    Vpn.this.mConfig.dnsServers.addAll(arrayList);
                    Vpn vpn2 = Vpn.this;
                    vpn2.mConfig.underlyingNetworks = new Network[]{network};
                    NetworkAgent networkAgent = vpn2.mNetworkAgent;
                    if (networkAgent == null) {
                        if (vpn2.isSettingsVpnLocked()) {
                            Vpn.this.prepareStatusIntent();
                        }
                        Vpn.this.agentConnect(new ValidationStatusCallback() { // from class: com.android.server.connectivity.Vpn$IkeV2VpnRunner$$ExternalSyntheticLambda6
                            @Override // com.android.server.connectivity.Vpn.ValidationStatusCallback
                            public final void onValidationStatus(int i2) {
                                Vpn.IkeV2VpnRunner.this.onValidationStatus(i2);
                            }
                        });
                    } else {
                        vpn2.doSetUnderlyingNetworks(networkAgent, Collections.singletonList(network));
                        Vpn.this.mNetworkCapabilities = new NetworkCapabilities.Builder(Vpn.this.mNetworkCapabilities).setUnderlyingNetworks(Collections.singletonList(network)).build();
                        Vpn.doSendLinkProperties(networkAgent, Vpn.this.makeLinkProperties());
                        this.mRetryCount = 0;
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "Error in ChildOpened for token " + i, e);
                onSessionLost(i, e);
            }
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onChildTransformCreated(int i, IpSecTransform ipSecTransform, int i2) {
            if (!isActiveToken(i)) {
                Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onChildTransformCreated obsolete token=" + i);
                StringBuilder sb = new StringBuilder();
                sb.append("ChildTransformCreated for obsolete token ");
                sb.append(i);
                Log.d(TAG, sb.toString());
                return;
            }
            Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onChildTransformCreated token=" + i + ", direction=" + i2 + ", transform=" + ipSecTransform);
            try {
                this.mTunnelIface.setUnderlyingNetwork(this.mIkeConnectionInfo.getNetwork());
                this.mIpSecManager.applyTunnelModeTransform(this.mTunnelIface, i2, ipSecTransform);
            } catch (IOException e) {
                Log.d(TAG, "Transform application failed for token " + i, e);
                onSessionLost(i, e);
            }
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onChildMigrated(int i, IpSecTransform ipSecTransform, IpSecTransform ipSecTransform2) {
            if (!isActiveToken(i)) {
                Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onChildMigrated obsolete token=" + i);
                StringBuilder sb = new StringBuilder();
                sb.append("onChildMigrated for obsolete token ");
                sb.append(i);
                Log.d(TAG, sb.toString());
                return;
            }
            Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onChildMigrated token=" + i + ", in=" + ipSecTransform + ", out=" + ipSecTransform2);
            Network network = this.mIkeConnectionInfo.getNetwork();
            try {
                synchronized (Vpn.this) {
                    Vpn vpn = Vpn.this;
                    if (vpn.mVpnRunner != this) {
                        return;
                    }
                    LinkProperties makeLinkProperties = vpn.makeLinkProperties();
                    boolean z = !Arrays.equals(Vpn.this.mConfig.underlyingNetworks, new Network[]{network});
                    VpnConfig vpnConfig = Vpn.this.mConfig;
                    vpnConfig.underlyingNetworks = new Network[]{network};
                    vpnConfig.mtu = calculateVpnMtu();
                    LinkProperties makeLinkProperties2 = Vpn.this.makeLinkProperties();
                    if (makeLinkProperties2.getLinkAddresses().isEmpty()) {
                        onSessionLost(i, new IkeIOException(new IOException("No valid addresses for MTU < 1280")));
                        return;
                    }
                    HashSet<LinkAddress> hashSet = new HashSet(makeLinkProperties.getLinkAddresses());
                    hashSet.removeAll(makeLinkProperties2.getLinkAddresses());
                    if (!hashSet.isEmpty()) {
                        Vpn vpn2 = Vpn.this;
                        vpn2.startNewNetworkAgent(vpn2.mNetworkAgent, "MTU too low for IPv6; restarting network agent");
                        for (LinkAddress linkAddress : hashSet) {
                            this.mTunnelIface.removeAddress(linkAddress.getAddress(), linkAddress.getPrefixLength());
                        }
                    } else {
                        if (!makeLinkProperties2.equals(makeLinkProperties)) {
                            Vpn.doSendLinkProperties(Vpn.this.mNetworkAgent, makeLinkProperties2);
                        }
                        if (z) {
                            Vpn.this.mNetworkCapabilities = new NetworkCapabilities.Builder(Vpn.this.mNetworkCapabilities).setUnderlyingNetworks(Collections.singletonList(network)).build();
                            Vpn vpn3 = Vpn.this;
                            vpn3.doSetUnderlyingNetworks(vpn3.mNetworkAgent, Collections.singletonList(network));
                        }
                    }
                    this.mTunnelIface.setUnderlyingNetwork(network);
                    this.mIpSecManager.applyTunnelModeTransform(this.mTunnelIface, 0, ipSecTransform);
                    this.mIpSecManager.applyTunnelModeTransform(this.mTunnelIface, 1, ipSecTransform2);
                }
            } catch (IOException e) {
                Log.d(TAG, "Transform application failed for token " + i, e);
                onSessionLost(i, e);
            }
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onDefaultNetworkChanged(Network network) {
            Vpn.this.mEventChanges.log("[UnderlyingNW] Default network changed to " + network);
            Log.d(TAG, "onDefaultNetworkChanged: " + network);
            cancelRetryNewIkeSessionFuture();
            cancelHandleNetworkLostTimeout();
            if (!this.mIsRunning) {
                Log.d(TAG, "onDefaultNetworkChanged after exit");
                return;
            }
            this.mActiveNetwork = network;
            this.mUnderlyingLinkProperties = null;
            this.mUnderlyingNetworkCapabilities = null;
            this.mRetryCount = 0;
        }

        private IkeSessionParams getIkeSessionParams(Network network) {
            IkeSessionParams.Builder makeIkeSessionParamsBuilder;
            IkeTunnelConnectionParams ikeTunnelConnectionParams = this.mProfile.getIkeTunnelConnectionParams();
            if (ikeTunnelConnectionParams != null) {
                makeIkeSessionParamsBuilder = new IkeSessionParams.Builder(ikeTunnelConnectionParams.getIkeSessionParams()).setNetwork(network);
            } else {
                makeIkeSessionParamsBuilder = VpnIkev2Utils.makeIkeSessionParamsBuilder(Vpn.this.mContext, this.mProfile, network);
            }
            if (this.mProfile.isAutomaticNattKeepaliveTimerEnabled()) {
                makeIkeSessionParamsBuilder.setNattKeepAliveDelaySeconds(guessNattKeepaliveTimerForNetwork());
            }
            if (this.mProfile.isAutomaticIpVersionSelectionEnabled()) {
                makeIkeSessionParamsBuilder.setIpVersion(guessEspIpVersionForNetwork());
                makeIkeSessionParamsBuilder.setEncapType(guessEspEncapTypeForNetwork());
            }
            return makeIkeSessionParamsBuilder.build();
        }

        private ChildSessionParams getChildSessionParams() {
            IkeTunnelConnectionParams ikeTunnelConnectionParams = this.mProfile.getIkeTunnelConnectionParams();
            if (ikeTunnelConnectionParams != null) {
                return ikeTunnelConnectionParams.getTunnelModeChildSessionParams();
            }
            return VpnIkev2Utils.buildChildSessionParams(this.mProfile.getAllowedAlgorithms());
        }

        private int calculateVpnMtu() {
            Network network = this.mIkeConnectionInfo.getNetwork();
            LinkProperties linkProperties = Vpn.this.mConnectivityManager.getLinkProperties(network);
            if (network == null || linkProperties == null) {
                return this.mProfile.getMaxMtu();
            }
            int mtu = linkProperties.getMtu();
            if (mtu == 0) {
                try {
                    mtu = Vpn.this.mDeps.getJavaNetworkInterfaceMtu(linkProperties.getInterfaceName(), this.mProfile.getMaxMtu());
                } catch (SocketException e) {
                    Log.d(TAG, "Got a SocketException when getting MTU from kernel: " + e);
                    return this.mProfile.getMaxMtu();
                }
            }
            return Vpn.this.mDeps.calculateVpnMtu(getChildSessionParams().getSaProposals(), this.mProfile.getMaxMtu(), mtu, this.mIkeConnectionInfo.getLocalAddress() instanceof Inet4Address);
        }

        private void startOrMigrateIkeSession(Network network) {
            if (network == null) {
                Log.d(TAG, "There is no active network for starting an IKE session");
            } else {
                if (maybeMigrateIkeSessionAndUpdateVpnTransportInfo(network)) {
                    return;
                }
                startIkeSession(network);
            }
        }

        private int guessEspIpVersionForNetwork() {
            if (this.mUnderlyingNetworkCapabilities.getTransportInfo() instanceof VcnTransportInfo) {
                Log.d(TAG, "Running over VCN, esp IP version is auto");
                return 0;
            }
            CarrierConfigInfo carrierConfigForUnderlyingNetwork = getCarrierConfigForUnderlyingNetwork();
            int i = carrierConfigForUnderlyingNetwork != null ? carrierConfigForUnderlyingNetwork.ipVersion : 0;
            if (carrierConfigForUnderlyingNetwork != null) {
                Log.d(TAG, "Get customized IP version (" + i + ") on SIM (mccmnc=" + carrierConfigForUnderlyingNetwork.mccMnc + ")");
            }
            return i;
        }

        private int guessEspEncapTypeForNetwork() {
            if (this.mUnderlyingNetworkCapabilities.getTransportInfo() instanceof VcnTransportInfo) {
                Log.d(TAG, "Running over VCN, encap type is auto");
                return 0;
            }
            CarrierConfigInfo carrierConfigForUnderlyingNetwork = getCarrierConfigForUnderlyingNetwork();
            int i = carrierConfigForUnderlyingNetwork != null ? carrierConfigForUnderlyingNetwork.encapType : 0;
            if (carrierConfigForUnderlyingNetwork != null) {
                Log.d(TAG, "Get customized encap type (" + i + ") on SIM (mccmnc=" + carrierConfigForUnderlyingNetwork.mccMnc + ")");
            }
            return i;
        }

        private int guessNattKeepaliveTimerForNetwork() {
            VcnTransportInfo transportInfo = this.mUnderlyingNetworkCapabilities.getTransportInfo();
            if (transportInfo instanceof VcnTransportInfo) {
                int minUdpPort4500NatTimeoutSeconds = transportInfo.getMinUdpPort4500NatTimeoutSeconds();
                Log.d(TAG, "Running over VCN, keepalive timer : " + minUdpPort4500NatTimeoutSeconds + "s");
                if (-1 != minUdpPort4500NatTimeoutSeconds) {
                    return minUdpPort4500NatTimeoutSeconds;
                }
            }
            CarrierConfigInfo carrierConfigForUnderlyingNetwork = getCarrierConfigForUnderlyingNetwork();
            int i = carrierConfigForUnderlyingNetwork != null ? carrierConfigForUnderlyingNetwork.keepaliveDelaySec : 30;
            if (carrierConfigForUnderlyingNetwork != null) {
                Log.d(TAG, "Get customized keepalive (" + i + "s) on SIM (mccmnc=" + carrierConfigForUnderlyingNetwork.mccMnc + ")");
            }
            return i;
        }

        private CarrierConfigInfo getCarrierConfigForUnderlyingNetwork() {
            int cellSubIdForNetworkCapabilities = Vpn.getCellSubIdForNetworkCapabilities(this.mUnderlyingNetworkCapabilities);
            if (cellSubIdForNetworkCapabilities == -1) {
                Log.d(TAG, "Underlying network is not a cellular network");
                return null;
            }
            synchronized (Vpn.this) {
                if (Vpn.this.mCachedCarrierConfigInfoPerSubId.contains(cellSubIdForNetworkCapabilities)) {
                    Log.d(TAG, "Get cached config");
                    return (CarrierConfigInfo) Vpn.this.mCachedCarrierConfigInfoPerSubId.get(cellSubIdForNetworkCapabilities);
                }
                TelephonyManager createForSubscriptionId = Vpn.this.mTelephonyManager.createForSubscriptionId(cellSubIdForNetworkCapabilities);
                if (createForSubscriptionId.getSimApplicationState() != 10) {
                    Log.d(TAG, "SIM card is not ready on sub " + cellSubIdForNetworkCapabilities);
                    return null;
                }
                PersistableBundle configForSubId = Vpn.this.mCarrierConfigManager.getConfigForSubId(cellSubIdForNetworkCapabilities);
                if (!CarrierConfigManager.isConfigForIdentifiedCarrier(configForSubId)) {
                    return null;
                }
                CarrierConfigInfo buildCarrierConfigInfo = buildCarrierConfigInfo(createForSubscriptionId.getSimOperator(cellSubIdForNetworkCapabilities), configForSubId.getInt("min_udp_port_4500_nat_timeout_sec_int"), configForSubId.getInt("preferred_ike_protocol_int", -1));
                synchronized (Vpn.this) {
                    Vpn.this.mCachedCarrierConfigInfoPerSubId.put(cellSubIdForNetworkCapabilities, buildCarrierConfigInfo);
                }
                return buildCarrierConfigInfo;
            }
        }

        private CarrierConfigInfo buildCarrierConfigInfo(String str, int i, int i2) {
            int i3;
            int i4;
            if (i2 != 0) {
                i3 = 4;
                if (i2 != 40) {
                    if (i2 != 60) {
                        i4 = i2 == 61 ? -1 : 17;
                    }
                    i3 = 6;
                }
            } else {
                i3 = 0;
                i4 = 0;
            }
            return new CarrierConfigInfo(str, i, i4, i3);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getOrGuessKeepaliveDelaySeconds() {
            if (this.mProfile.isAutomaticNattKeepaliveTimerEnabled()) {
                return guessNattKeepaliveTimerForNetwork();
            }
            if (this.mProfile.getIkeTunnelConnectionParams() != null) {
                return this.mProfile.getIkeTunnelConnectionParams().getIkeSessionParams().getNattKeepAliveDelaySeconds();
            }
            return 300;
        }

        boolean maybeMigrateIkeSessionAndUpdateVpnTransportInfo(Network network) {
            int orGuessKeepaliveDelaySeconds = getOrGuessKeepaliveDelaySeconds();
            boolean maybeMigrateIkeSession = maybeMigrateIkeSession(network, orGuessKeepaliveDelaySeconds);
            if (maybeMigrateIkeSession) {
                updateVpnTransportInfoAndNetCap(orGuessKeepaliveDelaySeconds);
            }
            return maybeMigrateIkeSession;
        }

        public void updateVpnTransportInfoAndNetCap(int i) {
            int activeVpnType = Vpn.this.getActiveVpnType();
            Vpn vpn = Vpn.this;
            VpnConfig vpnConfig = vpn.mConfig;
            VpnTransportInfo vpnTransportInfo = new VpnTransportInfo(activeVpnType, vpnConfig.session, vpnConfig.allowBypass && !vpn.mLockdown, Vpn.areLongLivedTcpConnectionsExpensive(i));
            if (!vpnTransportInfo.equals(Vpn.this.mNetworkCapabilities.getTransportInfo())) {
                Vpn.this.mNetworkCapabilities = new NetworkCapabilities.Builder(Vpn.this.mNetworkCapabilities).setTransportInfo(vpnTransportInfo).build();
                Vpn.this.mEventChanges.log("[VPNRunner] Update agent caps " + Vpn.this.mNetworkCapabilities);
                Vpn vpn2 = Vpn.this;
                Vpn.doSendNetworkCapabilities(vpn2.mNetworkAgent, vpn2.mNetworkCapabilities);
            }
        }

        private boolean maybeMigrateIkeSession(Network network, int i) {
            int i2;
            int i3 = 0;
            if (this.mSession == null || !this.mMobikeEnabled) {
                return false;
            }
            Log.d(TAG, "Migrate IKE Session with token " + this.mCurrentToken + " to network " + network);
            if (this.mProfile.isAutomaticIpVersionSelectionEnabled()) {
                i3 = guessEspIpVersionForNetwork();
                i2 = guessEspEncapTypeForNetwork();
            } else if (this.mProfile.getIkeTunnelConnectionParams() != null) {
                i3 = this.mProfile.getIkeTunnelConnectionParams().getIkeSessionParams().getIpVersion();
                i2 = this.mProfile.getIkeTunnelConnectionParams().getIkeSessionParams().getEncapType();
            } else {
                i2 = 0;
            }
            this.mSession.setNetwork(network, i3, i2, i);
            return true;
        }

        private void startIkeSession(Network network) {
            Log.d(TAG, "Start new IKE session on network " + network);
            Vpn.this.mEventChanges.log("[IKE] Start IKE session over " + network);
            try {
                synchronized (Vpn.this) {
                    Vpn vpn = Vpn.this;
                    if (vpn.mVpnRunner != this) {
                        return;
                    }
                    vpn.mInterface = null;
                    resetIkeState();
                    InetAddress localHost = InetAddress.getLocalHost();
                    this.mTunnelIface = this.mIpSecManager.createIpSecTunnelInterface(localHost, localHost, network);
                    NetdUtils.setInterfaceUp(Vpn.this.mNetd, this.mTunnelIface.getInterfaceName());
                    int i = this.mCurrentToken + 1;
                    this.mCurrentToken = i;
                    this.mSession = Vpn.this.mIkev2SessionCreator.createIkeSession(Vpn.this.mContext, getIkeSessionParams(network), getChildSessionParams(), this.mExecutor, new VpnIkev2Utils.IkeSessionCallbackImpl(TAG, this, i), new VpnIkev2Utils.ChildSessionCallbackImpl(TAG, this, i));
                    Log.d(TAG, "IKE session started for token " + i);
                }
            } catch (Exception e) {
                Log.i(TAG, "Setup failed for token " + this.mCurrentToken + ". Aborting", e);
                onSessionLost(this.mCurrentToken, e);
            }
        }

        private void scheduleStartIkeSession(long j) {
            if (this.mScheduledHandleRetryIkeSessionFuture != null) {
                Log.d(TAG, "There is a pending retrying task, skip the new retrying task");
                return;
            }
            if (-1 == j) {
                Dependencies dependencies = Vpn.this.mDeps;
                int i = this.mRetryCount;
                this.mRetryCount = i + 1;
                j = dependencies.getNextRetryDelayMs(i);
            }
            Log.d(TAG, "Retry new IKE session after " + j + " milliseconds.");
            this.mScheduledHandleRetryIkeSessionFuture = this.mExecutor.schedule(new Runnable() { // from class: com.android.server.connectivity.Vpn$IkeV2VpnRunner$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    Vpn.IkeV2VpnRunner.this.lambda$scheduleStartIkeSession$1();
                }
            }, j, TimeUnit.MILLISECONDS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$scheduleStartIkeSession$1() {
            startOrMigrateIkeSession(this.mActiveNetwork);
            this.mScheduledHandleRetryIkeSessionFuture = null;
        }

        private boolean significantCapsChange(NetworkCapabilities networkCapabilities, NetworkCapabilities networkCapabilities2) {
            if (networkCapabilities == networkCapabilities2) {
                return false;
            }
            return (networkCapabilities != null && networkCapabilities2 != null && Arrays.equals(networkCapabilities.getTransportTypes(), networkCapabilities2.getTransportTypes()) && Arrays.equals(networkCapabilities.getCapabilities(), networkCapabilities2.getCapabilities()) && Arrays.equals(networkCapabilities.getEnterpriseIds(), networkCapabilities2.getEnterpriseIds()) && Objects.equals(networkCapabilities.getTransportInfo(), networkCapabilities2.getTransportInfo()) && Objects.equals(networkCapabilities.getAllowedUids(), networkCapabilities2.getAllowedUids()) && Objects.equals(networkCapabilities.getUnderlyingNetworks(), networkCapabilities2.getUnderlyingNetworks()) && Objects.equals(networkCapabilities.getNetworkSpecifier(), networkCapabilities2.getNetworkSpecifier())) ? false : true;
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onDefaultNetworkCapabilitiesChanged(NetworkCapabilities networkCapabilities) {
            if (significantCapsChange(this.mUnderlyingNetworkCapabilities, networkCapabilities)) {
                Vpn.this.mEventChanges.log("[UnderlyingNW] Cap changed from " + this.mUnderlyingNetworkCapabilities + " to " + networkCapabilities);
            }
            NetworkCapabilities networkCapabilities2 = this.mUnderlyingNetworkCapabilities;
            this.mUnderlyingNetworkCapabilities = networkCapabilities;
            if (networkCapabilities2 == null || !networkCapabilities.getSubscriptionIds().equals(networkCapabilities2.getSubscriptionIds())) {
                scheduleStartIkeSession(Vpn.IKE_DELAY_ON_NC_LP_CHANGE_MS);
            }
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onDefaultNetworkLinkPropertiesChanged(LinkProperties linkProperties) {
            LinkProperties linkProperties2 = this.mUnderlyingLinkProperties;
            Vpn.this.mEventChanges.log("[UnderlyingNW] Lp changed from " + linkProperties2 + " to " + linkProperties);
            this.mUnderlyingLinkProperties = linkProperties;
            if (linkProperties2 == null || !LinkPropertiesUtils.isIdenticalAllLinkAddresses(linkProperties2, linkProperties)) {
                scheduleStartIkeSession(Vpn.IKE_DELAY_ON_NC_LP_CHANGE_MS);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public class VpnConnectivityDiagnosticsCallback extends ConnectivityDiagnosticsManager.ConnectivityDiagnosticsCallback {
            VpnConnectivityDiagnosticsCallback() {
            }

            @Override // android.net.ConnectivityDiagnosticsManager.ConnectivityDiagnosticsCallback
            public void onDataStallSuspected(ConnectivityDiagnosticsManager.DataStallReport dataStallReport) {
                synchronized (Vpn.this) {
                    IkeV2VpnRunner ikeV2VpnRunner = IkeV2VpnRunner.this;
                    Vpn vpn = Vpn.this;
                    if (vpn.mVpnRunner != ikeV2VpnRunner) {
                        return;
                    }
                    NetworkAgent networkAgent = vpn.mNetworkAgent;
                    if (networkAgent != null && networkAgent.getNetwork().equals(dataStallReport.getNetwork()) && !Vpn.this.mDataStallSuspected) {
                        Log.d(IkeV2VpnRunner.TAG, "Data stall suspected");
                        IkeV2VpnRunner ikeV2VpnRunner2 = IkeV2VpnRunner.this;
                        ikeV2VpnRunner2.maybeMigrateIkeSessionAndUpdateVpnTransportInfo(ikeV2VpnRunner2.mActiveNetwork);
                        Vpn.this.mDataStallSuspected = true;
                    }
                }
            }
        }

        public void onValidationStatus(int i) {
            Vpn.this.mEventChanges.log("[Validation] validation status " + i);
            if (i == 1) {
                this.mExecutor.execute(new Runnable() { // from class: com.android.server.connectivity.Vpn$IkeV2VpnRunner$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        Vpn.IkeV2VpnRunner.this.lambda$onValidationStatus$2();
                    }
                });
                return;
            }
            if (this.mScheduledHandleDataStallFuture != null) {
                return;
            }
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = this.mExecutor;
            Runnable runnable = new Runnable() { // from class: com.android.server.connectivity.Vpn$IkeV2VpnRunner$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    Vpn.IkeV2VpnRunner.this.lambda$onValidationStatus$3();
                }
            };
            Dependencies dependencies = Vpn.this.mDeps;
            int i2 = this.mDataStallRetryCount;
            this.mDataStallRetryCount = i2 + 1;
            this.mScheduledHandleDataStallFuture = scheduledThreadPoolExecutor.schedule(runnable, dependencies.getDataStallResetSessionSeconds(i2), TimeUnit.SECONDS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onValidationStatus$2() {
            Vpn.this.mDataStallSuspected = false;
            this.mDataStallRetryCount = 0;
            if (this.mScheduledHandleDataStallFuture != null) {
                Log.d(TAG, "Recovered from stall. Cancel pending reset action.");
                this.mScheduledHandleDataStallFuture.cancel(false);
                this.mScheduledHandleDataStallFuture = null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onValidationStatus$3() {
            if (Vpn.this.mDataStallSuspected) {
                Log.d(TAG, "Reset session to recover stalled network");
                startIkeSession(this.mActiveNetwork);
            }
            this.mScheduledHandleDataStallFuture = null;
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onDefaultNetworkLost(final Network network) {
            Vpn.this.mEventChanges.log("[UnderlyingNW] Network lost " + network);
            cancelRetryNewIkeSessionFuture();
            if (!isActiveNetwork(network)) {
                Log.d(TAG, "onDefaultNetworkLost called for obsolete network " + network);
                return;
            }
            this.mActiveNetwork = null;
            this.mUnderlyingNetworkCapabilities = null;
            this.mUnderlyingLinkProperties = null;
            if (this.mScheduledHandleNetworkLostFuture != null) {
                IllegalStateException illegalStateException = new IllegalStateException("Found a pending mScheduledHandleNetworkLostFuture");
                Log.i(TAG, "Unexpected error in onDefaultNetworkLost. Tear down session", illegalStateException);
                handleSessionLost(illegalStateException, network);
                return;
            }
            Log.d(TAG, "Schedule a delay handleSessionLost for losing network " + network + " on session with token " + this.mCurrentToken);
            final int i = this.mCurrentToken;
            this.mScheduledHandleNetworkLostFuture = this.mExecutor.schedule(new Runnable() { // from class: com.android.server.connectivity.Vpn$IkeV2VpnRunner$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Vpn.IkeV2VpnRunner.this.lambda$onDefaultNetworkLost$4(i, network);
                }
            }, NETWORK_LOST_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDefaultNetworkLost$4(int i, Network network) {
            if (isActiveToken(i)) {
                handleSessionLost(new IkeNetworkLostException(network), network);
                synchronized (Vpn.this) {
                    Vpn vpn = Vpn.this;
                    if (vpn.mVpnRunner != this) {
                        return;
                    } else {
                        vpn.updateState(NetworkInfo.DetailedState.DISCONNECTED, "Network lost");
                    }
                }
            } else {
                Log.d(TAG, "Scheduled handleSessionLost fired for obsolete token " + i);
            }
            this.mScheduledHandleNetworkLostFuture = null;
        }

        private void cancelHandleNetworkLostTimeout() {
            if (this.mScheduledHandleNetworkLostFuture != null) {
                Log.d(TAG, "Cancel the task for handling network lost timeout");
                this.mScheduledHandleNetworkLostFuture.cancel(false);
                this.mScheduledHandleNetworkLostFuture = null;
            }
        }

        private void cancelRetryNewIkeSessionFuture() {
            if (this.mScheduledHandleRetryIkeSessionFuture != null) {
                Log.d(TAG, "Cancel the task for handling new ike session timeout");
                this.mScheduledHandleRetryIkeSessionFuture.cancel(false);
                this.mScheduledHandleRetryIkeSessionFuture = null;
            }
        }

        private void markFailedAndDisconnect(Exception exc) {
            synchronized (Vpn.this) {
                Vpn vpn = Vpn.this;
                if (vpn.mVpnRunner != this) {
                    return;
                }
                vpn.updateState(NetworkInfo.DetailedState.FAILED, exc.getMessage());
                lambda$exitVpnRunner$5();
            }
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onSessionLost(int i, Exception exc) {
            String str;
            LocalLog localLog = Vpn.this.mEventChanges;
            StringBuilder sb = new StringBuilder();
            sb.append("[IKE] Session lost on network ");
            sb.append(this.mActiveNetwork);
            if (exc == null) {
                str = "";
            } else {
                str = " reason " + exc.getMessage();
            }
            sb.append(str);
            localLog.log(sb.toString());
            Log.d(TAG, "onSessionLost() called for token " + i);
            if (!isActiveToken(i)) {
                Log.d(TAG, "onSessionLost() called for obsolete token " + i);
                return;
            }
            handleSessionLost(exc, this.mActiveNetwork);
        }

        private void handleSessionLost(Exception exc, Network network) {
            String str;
            int i;
            cancelHandleNetworkLostTimeout();
            if (exc instanceof IllegalArgumentException) {
                markFailedAndDisconnect(exc);
                return;
            }
            if (exc instanceof IkeProtocolException) {
                IkeProtocolException ikeProtocolException = (IkeProtocolException) exc;
                int errorType = ikeProtocolException.getErrorType();
                int errorType2 = ikeProtocolException.getErrorType();
                r4 = (errorType2 == 14 || errorType2 == 17 || errorType2 == 24 || errorType2 == 34 || errorType2 == 37 || errorType2 == 38) ? 1 : 2;
                str = "android.net.category.EVENT_IKE_ERROR";
                i = errorType;
            } else if (exc instanceof IkeNetworkLostException) {
                i = 2;
                str = "android.net.category.EVENT_NETWORK_ERROR";
            } else {
                int i2 = -1;
                if (exc instanceof IkeNonProtocolException) {
                    if (exc.getCause() instanceof UnknownHostException) {
                        i2 = 0;
                    } else if (exc.getCause() instanceof IkeTimeoutException) {
                        str = "android.net.category.EVENT_NETWORK_ERROR";
                        i = 1;
                    } else if (exc.getCause() instanceof IOException) {
                        i2 = 3;
                    }
                    str = "android.net.category.EVENT_NETWORK_ERROR";
                    i = i2;
                } else {
                    if (exc != null) {
                        Log.wtf(TAG, "onSessionLost: exception = " + exc);
                    }
                    str = null;
                    r4 = -1;
                    i = -1;
                }
            }
            synchronized (Vpn.this) {
                if (Vpn.this.mVpnRunner != this) {
                    return;
                }
                if (SdkLevel.isAtLeastT() && str != null && Vpn.isVpnApp(Vpn.this.mPackage)) {
                    Vpn vpn = Vpn.this;
                    vpn.sendEventToVpnManagerApp(str, r4, i, vpn.getPackage(), this.mSessionKey, Vpn.this.makeVpnProfileStateLocked(), this.mActiveNetwork, Vpn.this.getRedactedNetworkCapabilities(this.mUnderlyingNetworkCapabilities), Vpn.this.getRedactedLinkProperties(this.mUnderlyingLinkProperties));
                }
                if (r4 == 1) {
                    markFailedAndDisconnect(exc);
                    return;
                }
                scheduleStartIkeSession(-1L);
                Log.d(TAG, "Resetting state for token: " + this.mCurrentToken);
                synchronized (Vpn.this) {
                    Vpn vpn2 = Vpn.this;
                    if (vpn2.mVpnRunner != this) {
                        return;
                    }
                    vpn2.mInterface = null;
                    VpnConfig vpnConfig = vpn2.mConfig;
                    if (vpnConfig != null) {
                        vpnConfig.interfaze = null;
                        if (vpnConfig.routes != null) {
                            ArrayList arrayList = new ArrayList(Vpn.this.mConfig.routes);
                            Vpn.this.mConfig.routes.clear();
                            Iterator it = arrayList.iterator();
                            while (it.hasNext()) {
                                Vpn.this.mConfig.routes.add(new RouteInfo(((RouteInfo) it.next()).getDestination(), null, null, 7));
                            }
                            Vpn vpn3 = Vpn.this;
                            NetworkAgent networkAgent = vpn3.mNetworkAgent;
                            if (networkAgent != null) {
                                Vpn.doSendLinkProperties(networkAgent, vpn3.makeLinkProperties());
                            }
                        }
                    }
                    resetIkeState();
                }
            }
        }

        private void resetIkeState() {
            IpSecManager.IpSecTunnelInterface ipSecTunnelInterface = this.mTunnelIface;
            if (ipSecTunnelInterface != null) {
                ipSecTunnelInterface.close();
                this.mTunnelIface = null;
            }
            IkeSessionWrapper ikeSessionWrapper = this.mSession;
            if (ikeSessionWrapper != null) {
                ikeSessionWrapper.kill();
                this.mSession = null;
            }
            this.mIkeConnectionInfo = null;
            this.mMobikeEnabled = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: disconnectVpnRunner, reason: merged with bridge method [inline-methods] */
        public void lambda$exitVpnRunner$5() {
            Vpn.this.mEventChanges.log("[VPNRunner] Disconnect runner, underlying network" + this.mActiveNetwork);
            this.mActiveNetwork = null;
            this.mUnderlyingNetworkCapabilities = null;
            this.mUnderlyingLinkProperties = null;
            this.mIsRunning = false;
            resetIkeState();
            Vpn.this.mCarrierConfigManager.unregisterCarrierConfigChangeListener(this.mCarrierConfigChangeListener);
            Vpn.this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
            Vpn.this.mConnectivityDiagnosticsManager.unregisterConnectivityDiagnosticsCallback(this.mDiagnosticsCallback);
            Vpn.this.clearVpnNetworkPreference(this.mSessionKey);
            this.mExecutor.shutdown();
        }

        @Override // com.android.server.connectivity.Vpn.VpnRunner
        public void exitVpnRunner() {
            try {
                this.mExecutor.execute(new Runnable() { // from class: com.android.server.connectivity.Vpn$IkeV2VpnRunner$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        Vpn.IkeV2VpnRunner.this.lambda$exitVpnRunner$5();
                    }
                });
            } catch (RejectedExecutionException unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class LegacyVpnRunner extends VpnRunner {
        private static final String TAG = "LegacyVpnRunner";
        private final String[][] mArguments;
        private long mBringupStartTime;
        private final BroadcastReceiver mBroadcastReceiver;
        private final String[] mDaemons;
        private final AtomicInteger mOuterConnection;
        private final String mOuterInterface;
        private final VpnProfile mProfile;
        private final LocalSocket[] mSockets;

        LegacyVpnRunner(VpnConfig vpnConfig, String[] strArr, String[] strArr2, VpnProfile vpnProfile) {
            super(TAG);
            NetworkInfo networkInfo;
            this.mOuterConnection = new AtomicInteger(-1);
            this.mBringupStartTime = -1L;
            this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.connectivity.Vpn.LegacyVpnRunner.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    NetworkInfo networkInfo2;
                    if (Vpn.this.mEnableTeardown && intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE") && intent.getIntExtra("networkType", -1) == LegacyVpnRunner.this.mOuterConnection.get() && (networkInfo2 = (NetworkInfo) intent.getExtra("networkInfo")) != null && !networkInfo2.isConnectedOrConnecting()) {
                        try {
                            Vpn.this.mObserver.interfaceStatusChanged(LegacyVpnRunner.this.mOuterInterface, false);
                        } catch (RemoteException unused) {
                        }
                    }
                }
            };
            if (strArr == null && strArr2 == null) {
                throw new IllegalArgumentException("Arguments to racoon and mtpd must not both be null");
            }
            Vpn.this.mConfig = vpnConfig;
            String[] strArr3 = {"racoon", "mtpd"};
            this.mDaemons = strArr3;
            this.mArguments = new String[][]{strArr, strArr2};
            this.mSockets = new LocalSocket[strArr3.length];
            String str = vpnConfig.interfaze;
            this.mOuterInterface = str;
            this.mProfile = vpnProfile;
            if (!TextUtils.isEmpty(str)) {
                Network[] allNetworks = Vpn.this.mConnectivityManager.getAllNetworks();
                int length = allNetworks.length;
                int i = 0;
                while (true) {
                    if (i < length) {
                        Network network = allNetworks[i];
                        LinkProperties linkProperties = Vpn.this.mConnectivityManager.getLinkProperties(network);
                        if (linkProperties != null && linkProperties.getAllInterfaceNames().contains(this.mOuterInterface) && (networkInfo = Vpn.this.mConnectivityManager.getNetworkInfo(network)) != null) {
                            this.mOuterConnection.set(networkInfo.getType());
                            break;
                        }
                        i++;
                    } else {
                        break;
                    }
                }
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            Vpn.this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter);
        }

        public void exitIfOuterInterfaceIs(String str) {
            if (str.equals(this.mOuterInterface)) {
                Log.i(TAG, "Legacy VPN is going down with " + str);
                exitVpnRunner();
            }
        }

        @Override // com.android.server.connectivity.Vpn.VpnRunner
        public void exitVpnRunner() {
            interrupt();
            Vpn.this.agentDisconnect();
            try {
                Vpn.this.mContext.unregisterReceiver(this.mBroadcastReceiver);
            } catch (IllegalArgumentException unused) {
            }
        }

        @Override // com.android.server.connectivity.Vpn.VpnRunner, java.lang.Thread, java.lang.Runnable
        public void run() {
            Log.v(TAG, "Waiting");
            synchronized (TAG) {
                Log.v(TAG, "Executing");
                int i = 0;
                try {
                    bringup();
                    waitForDaemonsToStop();
                    Thread.interrupted();
                    for (LocalSocket localSocket : this.mSockets) {
                        IoUtils.closeQuietly(localSocket);
                    }
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException unused) {
                    }
                    String[] strArr = this.mDaemons;
                    int length = strArr.length;
                    while (i < length) {
                        Vpn.this.mDeps.stopService(strArr[i]);
                        i++;
                    }
                } catch (InterruptedException unused2) {
                    for (LocalSocket localSocket2 : this.mSockets) {
                        IoUtils.closeQuietly(localSocket2);
                    }
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException unused3) {
                    }
                    String[] strArr2 = this.mDaemons;
                    int length2 = strArr2.length;
                    while (i < length2) {
                        Vpn.this.mDeps.stopService(strArr2[i]);
                        i++;
                    }
                } catch (Throwable th) {
                    for (LocalSocket localSocket3 : this.mSockets) {
                        IoUtils.closeQuietly(localSocket3);
                    }
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException unused4) {
                    }
                    String[] strArr3 = this.mDaemons;
                    int length3 = strArr3.length;
                    while (i < length3) {
                        Vpn.this.mDeps.stopService(strArr3[i]);
                        i++;
                    }
                    throw th;
                }
                Vpn.this.agentDisconnect();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void checkInterruptAndDelay(boolean z) throws InterruptedException {
            if (SystemClock.elapsedRealtime() - this.mBringupStartTime <= 60000) {
                Thread.sleep(z ? 200L : 1L);
            } else {
                Vpn.this.updateState(NetworkInfo.DetailedState.FAILED, "checkpoint");
                throw new IllegalStateException("VPN bringup took too long");
            }
        }

        private void checkAndFixupArguments(InetAddress inetAddress) {
            String hostAddress = inetAddress.getHostAddress();
            if (!"racoon".equals(this.mDaemons[0]) || !"mtpd".equals(this.mDaemons[1])) {
                throw new IllegalStateException("Unexpected daemons order");
            }
            String[] strArr = this.mArguments[0];
            if (strArr != null) {
                if (!this.mProfile.server.equals(strArr[1])) {
                    throw new IllegalStateException("Invalid server argument for racoon");
                }
                this.mArguments[0][1] = hostAddress;
            }
            String[] strArr2 = this.mArguments[1];
            if (strArr2 != null) {
                if (!this.mProfile.server.equals(strArr2[2])) {
                    throw new IllegalStateException("Invalid server argument for mtpd");
                }
                this.mArguments[1][2] = hostAddress;
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:48:0x00cd, code lost:
        
            checkInterruptAndDelay(true);
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void bringup() {
            try {
                InetAddress resolve = Vpn.this.mDeps.resolve(this.mProfile.server);
                checkAndFixupArguments(resolve);
                this.mBringupStartTime = SystemClock.elapsedRealtime();
                for (String str : this.mDaemons) {
                    while (!Vpn.this.mDeps.isServiceStopped(str)) {
                        checkInterruptAndDelay(true);
                    }
                }
                File stateFile = Vpn.this.mDeps.getStateFile();
                stateFile.delete();
                if (stateFile.exists()) {
                    throw new IllegalStateException("Cannot delete the state");
                }
                new File("/data/misc/vpn/abort").delete();
                Vpn.this.updateState(NetworkInfo.DetailedState.CONNECTING, "execute");
                int i = 0;
                while (true) {
                    String[] strArr = this.mDaemons;
                    if (i >= strArr.length) {
                        break;
                    }
                    String[] strArr2 = this.mArguments[i];
                    if (strArr2 != null) {
                        String str2 = strArr[i];
                        Vpn.this.mDeps.startService(str2);
                        while (!Vpn.this.mDeps.isServiceRunning(str2)) {
                            checkInterruptAndDelay(true);
                        }
                        this.mSockets[i] = new LocalSocket();
                        Vpn.this.mDeps.sendArgumentsToDaemon(str2, this.mSockets[i], strArr2, new RetryScheduler() { // from class: com.android.server.connectivity.Vpn$LegacyVpnRunner$$ExternalSyntheticLambda0
                            @Override // com.android.server.connectivity.Vpn.RetryScheduler
                            public final void checkInterruptAndDelay(boolean z) {
                                Vpn.LegacyVpnRunner.this.checkInterruptAndDelay(z);
                            }
                        });
                    }
                    i++;
                }
                while (!stateFile.exists()) {
                    int i2 = 0;
                    while (true) {
                        String[] strArr3 = this.mDaemons;
                        if (i2 < strArr3.length) {
                            String str3 = strArr3[i2];
                            if (this.mArguments[i2] != null && !Vpn.this.mDeps.isServiceRunning(str3)) {
                                throw new IllegalStateException(str3 + " is dead");
                            }
                            i2++;
                        }
                    }
                }
                String[] split = FileUtils.readTextFile(stateFile, 0, null).split("\n", -1);
                if (split.length != 7) {
                    throw new IllegalStateException("Cannot parse the state: '" + String.join("', '", split) + "'");
                }
                Vpn.this.mConfig.interfaze = split[0].trim();
                Vpn.this.mConfig.addLegacyAddresses(split[1]);
                List list = Vpn.this.mConfig.routes;
                if (list == null || list.isEmpty()) {
                    Vpn.this.mConfig.addLegacyRoutes(split[2]);
                }
                List list2 = Vpn.this.mConfig.dnsServers;
                if (list2 == null || list2.size() == 0) {
                    String trim = split[3].trim();
                    if (!trim.isEmpty()) {
                        Vpn.this.mConfig.dnsServers = Arrays.asList(trim.split(" "));
                    }
                }
                List list3 = Vpn.this.mConfig.searchDomains;
                if (list3 == null || list3.size() == 0) {
                    String trim2 = split[4].trim();
                    if (!trim2.isEmpty()) {
                        Vpn.this.mConfig.searchDomains = Arrays.asList(trim2.split(" "));
                    }
                }
                if (resolve instanceof Inet4Address) {
                    Vpn.this.mConfig.routes.add(new RouteInfo(new IpPrefix(resolve, 32), null, null, 9));
                } else if (resolve instanceof Inet6Address) {
                    Vpn.this.mConfig.routes.add(new RouteInfo(new IpPrefix(resolve, 128), null, null, 9));
                } else {
                    Log.e(TAG, "Unknown IP address family for VPN endpoint: " + resolve);
                }
                synchronized (Vpn.this) {
                    Vpn.this.mConfig.startTime = SystemClock.elapsedRealtime();
                    checkInterruptAndDelay(false);
                    Vpn vpn = Vpn.this;
                    if (!vpn.mDeps.isInterfacePresent(vpn, vpn.mConfig.interfaze)) {
                        throw new IllegalStateException(Vpn.this.mConfig.interfaze + " is gone");
                    }
                    Vpn vpn2 = Vpn.this;
                    vpn2.mInterface = vpn2.mConfig.interfaze;
                    vpn2.prepareStatusIntent();
                    Vpn.this.agentConnect();
                    Log.i(TAG, "Connected!");
                }
            } catch (Exception e) {
                Log.i(TAG, "Aborting", e);
                Vpn.this.updateState(NetworkInfo.DetailedState.FAILED, e.getMessage());
                exitVpnRunner();
            }
        }

        private void waitForDaemonsToStop() throws InterruptedException {
            if (!Vpn.this.mNetworkInfo.isConnected()) {
                return;
            }
            while (true) {
                Thread.sleep(2000L);
                int i = 0;
                while (true) {
                    String[] strArr = this.mDaemons;
                    if (i < strArr.length) {
                        if (this.mArguments[i] != null && Vpn.this.mDeps.isServiceStopped(strArr[i])) {
                            return;
                        } else {
                            i++;
                        }
                    }
                }
            }
        }
    }

    private void verifyCallingUidAndPackage(String str) {
        this.mDeps.verifyCallingUidAndPackage(this.mContext, str, this.mUserId);
    }

    @VisibleForTesting
    String getProfileNameForPackage(String str) {
        return "PLATFORM_VPN_" + this.mUserId + "_" + str;
    }

    @VisibleForTesting
    void validateRequiredFeatures(VpnProfile vpnProfile) {
        switch (vpnProfile.type) {
            case 6:
            case 7:
            case 8:
            case 9:
                if (!this.mContext.getPackageManager().hasSystemFeature("android.software.ipsec_tunnels")) {
                    throw new UnsupportedOperationException("Ikev2VpnProfile(s) requires PackageManager.FEATURE_IPSEC_TUNNELS");
                }
                return;
            default:
                return;
        }
    }

    public synchronized boolean provisionVpnProfile(String str, VpnProfile vpnProfile) {
        Objects.requireNonNull(str, "No package name provided");
        Objects.requireNonNull(vpnProfile, "No profile provided");
        verifyCallingUidAndPackage(str);
        enforceNotRestrictedUser();
        validateRequiredFeatures(vpnProfile);
        if (vpnProfile.isRestrictedToTestNetworks) {
            this.mContext.enforceCallingPermission("android.permission.MANAGE_TEST_NETWORKS", "Test-mode profiles require the MANAGE_TEST_NETWORKS permission");
        }
        byte[] encode = vpnProfile.encode();
        if (encode.length > 131072) {
            throw new IllegalArgumentException("Profile too big");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            getVpnProfileStore().put(getProfileNameForPackage(str), encode);
            Binder.restoreCallingIdentity(clearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
        return isVpnProfilePreConsented(this.mContext, str);
    }

    private boolean isCurrentIkev2VpnLocked(String str) {
        return isCurrentPreparedPackage(str) && isIkev2VpnRunner();
    }

    public synchronized void deleteVpnProfile(String str) {
        Objects.requireNonNull(str, "No package name provided");
        verifyCallingUidAndPackage(str);
        enforceNotRestrictedUser();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (isCurrentIkev2VpnLocked(str)) {
                if (this.mAlwaysOn) {
                    setAlwaysOnPackage(null, false, null);
                } else {
                    prepareInternal("[Legacy VPN]");
                }
            }
            getVpnProfileStore().remove(getProfileNameForPackage(str));
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @VisibleForTesting
    VpnProfile getVpnProfilePrivileged(String str) {
        if (!this.mDeps.isCallerSystem()) {
            Log.wtf(TAG, "getVpnProfilePrivileged called as non-System UID ");
            return null;
        }
        byte[] bArr = getVpnProfileStore().get(getProfileNameForPackage(str));
        if (bArr == null) {
            return null;
        }
        return VpnProfile.decode("", bArr);
    }

    private boolean isIkev2VpnRunner() {
        return this.mVpnRunner instanceof IkeV2VpnRunner;
    }

    @GuardedBy({"this"})
    private String getSessionKeyLocked() {
        boolean isIkev2VpnRunner = isIkev2VpnRunner();
        String str = isIkev2VpnRunner ? ((IkeV2VpnRunner) this.mVpnRunner).mSessionKey : null;
        Log.d(TAG, "getSessionKeyLocked: isIkev2VpnRunner = " + isIkev2VpnRunner + ", sessionKey = " + str);
        return str;
    }

    public synchronized String startVpnProfile(String str) {
        Objects.requireNonNull(str, "No package name provided");
        enforceNotRestrictedUser();
        if (!prepare(str, null, 2)) {
            throw new SecurityException("User consent not granted for package " + str);
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            VpnProfile vpnProfilePrivileged = getVpnProfilePrivileged(str);
            if (vpnProfilePrivileged == null) {
                throw new IllegalArgumentException("No profile found for " + str);
            }
            startVpnProfilePrivileged(vpnProfilePrivileged, str);
            if (!isIkev2VpnRunner()) {
                throw new IllegalStateException("mVpnRunner shouldn't be null and should also be an instance of Ikev2VpnRunner");
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
        return getSessionKeyLocked();
    }

    private synchronized void startVpnProfilePrivileged(VpnProfile vpnProfile, String str) {
        prepareInternal(str);
        updateState(NetworkInfo.DetailedState.CONNECTING, "startPlatformVpn");
        try {
            this.mConfig = new VpnConfig();
            if ("[Legacy VPN]".equals(str)) {
                VpnConfig vpnConfig = this.mConfig;
                vpnConfig.legacy = true;
                vpnConfig.session = vpnProfile.name;
                vpnConfig.user = vpnProfile.key;
                vpnConfig.isMetered = true;
            } else {
                VpnConfig vpnConfig2 = this.mConfig;
                vpnConfig2.user = str;
                vpnConfig2.isMetered = vpnProfile.isMetered;
            }
            this.mConfig.startTime = SystemClock.elapsedRealtime();
            VpnConfig vpnConfig3 = this.mConfig;
            vpnConfig3.proxyInfo = vpnProfile.proxy;
            vpnConfig3.requiresInternetValidation = vpnProfile.requiresInternetValidation;
            vpnConfig3.excludeLocalRoutes = vpnProfile.excludeLocalRoutes;
            vpnConfig3.allowBypass = vpnProfile.isBypassable;
            vpnConfig3.disallowedApplications = getAppExclusionList(this.mPackage);
            switch (vpnProfile.type) {
                case 6:
                case 7:
                case 8:
                case 9:
                    IkeV2VpnRunner ikeV2VpnRunner = new IkeV2VpnRunner(Ikev2VpnProfile.fromVpnProfile(vpnProfile), this.mDeps.newScheduledThreadPoolExecutor());
                    this.mVpnRunner = ikeV2VpnRunner;
                    ikeV2VpnRunner.start();
                    break;
                default:
                    updateState(NetworkInfo.DetailedState.FAILED, "Invalid platform VPN type");
                    Log.d(TAG, "Unknown VPN profile type: " + vpnProfile.type);
                    break;
            }
            if (!"[Legacy VPN]".equals(str)) {
                this.mAppOpsManager.startOp("android:establish_vpn_manager", this.mOwnerUID, this.mPackage, null, null);
            }
        } catch (GeneralSecurityException e) {
            this.mConfig = null;
            updateState(NetworkInfo.DetailedState.FAILED, "VPN startup failed");
            throw new IllegalArgumentException("VPN startup failed", e);
        }
    }

    @GuardedBy({"this"})
    private void stopVpnRunnerAndNotifyAppLocked() {
        int i = this.mOwnerUID;
        Intent buildVpnManagerEventIntent = (SdkLevel.isAtLeastT() && isVpnApp(this.mPackage)) ? buildVpnManagerEventIntent("android.net.category.EVENT_DEACTIVATED_BY_USER", -1, -1, this.mPackage, getSessionKeyLocked(), makeVpnProfileStateLocked(), null, null, null) : null;
        this.mVpnRunner.exit();
        if (buildVpnManagerEventIntent == null || !isVpnApp(this.mPackage)) {
            return;
        }
        notifyVpnManagerVpnStopped(this.mPackage, i, buildVpnManagerEventIntent);
    }

    public synchronized void stopVpnProfile(String str) {
        Objects.requireNonNull(str, "No package name provided");
        enforceNotRestrictedUser();
        if (isCurrentIkev2VpnLocked(str)) {
            stopVpnRunnerAndNotifyAppLocked();
        }
    }

    private synchronized void notifyVpnManagerVpnStopped(String str, int i, Intent intent) {
        this.mAppOpsManager.finishOp("android:establish_vpn_manager", i, str, null);
        if (SdkLevel.isAtLeastT()) {
            this.mEventChanges.log("[VMEvent] " + str + " stopped");
            sendEventToVpnManagerApp(intent, str);
        }
    }

    private boolean storeAppExclusionList(String str, List<String> list) {
        try {
            byte[] diskStableBytes = PersistableBundleUtils.toDiskStableBytes(PersistableBundleUtils.fromList(list, PersistableBundleUtils.STRING_SERIALIZER));
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                getVpnProfileStore().put(getVpnAppExcludedForPackage(str), diskStableBytes);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return true;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
        } catch (IOException e) {
            Log.e(TAG, "problem writing into stream", e);
            return false;
        }
    }

    @VisibleForTesting
    String getVpnAppExcludedForPackage(String str) {
        return VPN_APP_EXCLUDED + this.mUserId + "_" + str;
    }

    public synchronized boolean setAppExclusionList(String str, List<String> list) {
        enforceNotRestrictedUser();
        if (!storeAppExclusionList(str, list)) {
            return false;
        }
        updateAppExclusionList(list);
        return true;
    }

    public synchronized void refreshPlatformVpnAppExclusionList() {
        updateAppExclusionList(getAppExclusionList(this.mPackage));
    }

    private synchronized void updateAppExclusionList(List<String> list) {
        if (this.mNetworkAgent != null && isIkev2VpnRunner()) {
            this.mConfig.disallowedApplications = List.copyOf(list);
            this.mNetworkCapabilities = new NetworkCapabilities.Builder(this.mNetworkCapabilities).setUids(createUserAndRestrictedProfilesRanges(this.mUserId, null, list)).build();
            String sessionKeyLocked = getSessionKeyLocked();
            int i = this.mUserId;
            VpnConfig vpnConfig = this.mConfig;
            setVpnNetworkPreference(sessionKeyLocked, createUserAndRestrictedProfilesRanges(i, vpnConfig.allowedApplications, vpnConfig.disallowedApplications));
            doSendNetworkCapabilities(this.mNetworkAgent, this.mNetworkCapabilities);
        }
    }

    public synchronized List<String> getAppExclusionList(String str) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                byte[] bArr = getVpnProfileStore().get(getVpnAppExcludedForPackage(str));
                if (bArr != null && bArr.length != 0) {
                    return PersistableBundleUtils.toList(PersistableBundleUtils.fromDiskStableBytes(bArr), PersistableBundleUtils.STRING_DESERIALIZER);
                }
                return new ArrayList();
            } catch (IOException e) {
                Log.e(TAG, "problem reading from stream", e);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return new ArrayList();
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private int getStateFromLegacyState(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 5) {
            return 3;
        }
        if (i == 2) {
            return 1;
        }
        if (i == 3) {
            return 2;
        }
        Log.wtf(TAG, "Unhandled state " + i + ", treat it as STATE_DISCONNECTED");
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"this"})
    public VpnProfileState makeVpnProfileStateLocked() {
        return new VpnProfileState(getStateFromLegacyState(this.mLegacyState), isIkev2VpnRunner() ? getSessionKeyLocked() : null, this.mAlwaysOn, this.mLockdown);
    }

    private VpnProfileState makeDisconnectedVpnProfileState() {
        return new VpnProfileState(0, null, false, false);
    }

    public synchronized VpnProfileState getProvisionedVpnProfileState(String str) {
        Objects.requireNonNull(str, "No package name provided");
        enforceNotRestrictedUser();
        return isCurrentIkev2VpnLocked(str) ? makeVpnProfileStateLocked() : null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void doSendLinkProperties(NetworkAgent networkAgent, LinkProperties linkProperties) {
        if (networkAgent instanceof VpnNetworkAgentWrapper) {
            ((VpnNetworkAgentWrapper) networkAgent).doSendLinkProperties(linkProperties);
        } else {
            networkAgent.sendLinkProperties(linkProperties);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void doSendNetworkCapabilities(NetworkAgent networkAgent, NetworkCapabilities networkCapabilities) {
        if (networkAgent instanceof VpnNetworkAgentWrapper) {
            ((VpnNetworkAgentWrapper) networkAgent).doSendNetworkCapabilities(networkCapabilities);
        } else {
            networkAgent.sendNetworkCapabilities(networkCapabilities);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSetUnderlyingNetworks(NetworkAgent networkAgent, List<Network> list) {
        logUnderlyNetworkChanges(list);
        if (networkAgent instanceof VpnNetworkAgentWrapper) {
            ((VpnNetworkAgentWrapper) networkAgent).doSetUnderlyingNetworks(list);
        } else {
            networkAgent.setUnderlyingNetworks(list);
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class VpnNetworkAgentWrapper extends NetworkAgent {
        private final ValidationStatusCallback mCallback;

        public void onNetworkUnwanted() {
        }

        public VpnNetworkAgentWrapper(Context context, Looper looper, String str, NetworkCapabilities networkCapabilities, LinkProperties linkProperties, NetworkScore networkScore, NetworkAgentConfig networkAgentConfig, NetworkProvider networkProvider, ValidationStatusCallback validationStatusCallback) {
            super(context, looper, str, networkCapabilities, linkProperties, networkScore, networkAgentConfig, networkProvider);
            this.mCallback = validationStatusCallback;
        }

        public void doSendLinkProperties(LinkProperties linkProperties) {
            sendLinkProperties(linkProperties);
        }

        public void doSendNetworkCapabilities(NetworkCapabilities networkCapabilities) {
            sendNetworkCapabilities(networkCapabilities);
        }

        public void doSetUnderlyingNetworks(List<Network> list) {
            setUnderlyingNetworks(list);
        }

        public void onValidationStatus(int i, Uri uri) {
            ValidationStatusCallback validationStatusCallback = this.mCallback;
            if (validationStatusCallback != null) {
                validationStatusCallback.onValidationStatus(i);
            }
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class IkeSessionWrapper {
        private final IkeSession mImpl;

        public IkeSessionWrapper(IkeSession ikeSession) {
            this.mImpl = ikeSession;
        }

        public void setNetwork(Network network, int i, int i2, int i3) {
            this.mImpl.setNetwork(network, i, i2, i3);
        }

        public void setUnderpinnedNetwork(Network network) {
            this.mImpl.setUnderpinnedNetwork(network);
        }

        public void kill() {
            this.mImpl.kill();
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Ikev2SessionCreator {
        public IkeSessionWrapper createIkeSession(Context context, IkeSessionParams ikeSessionParams, ChildSessionParams childSessionParams, Executor executor, IkeSessionCallback ikeSessionCallback, ChildSessionCallback childSessionCallback) {
            return new IkeSessionWrapper(new IkeSession(context, ikeSessionParams, childSessionParams, executor, ikeSessionCallback, childSessionCallback));
        }
    }

    @VisibleForTesting
    static Range<Integer> createUidRangeForUser(int i) {
        return new Range<>(Integer.valueOf(i * BluetoothStatsLog.BLUETOOTH_CODE_PATH_COUNTER__KEY__L2CAP_SUCCESS), Integer.valueOf(((i + 1) * BluetoothStatsLog.BLUETOOTH_CODE_PATH_COUNTER__KEY__L2CAP_SUCCESS) - 1));
    }

    private boolean isVpnDisabled() {
        IVpnExt iVpnExt = this.mVpnExt;
        if (iVpnExt != null) {
            return iVpnExt.isVpnDisabled(null);
        }
        return false;
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        synchronized (this) {
            indentingPrintWriter.println("Active package name: " + this.mPackage);
            indentingPrintWriter.println("Active vpn type: " + getActiveVpnType());
            indentingPrintWriter.println("NetworkCapabilities: " + this.mNetworkCapabilities);
            if (isIkev2VpnRunner()) {
                IkeV2VpnRunner ikeV2VpnRunner = (IkeV2VpnRunner) this.mVpnRunner;
                indentingPrintWriter.println("SessionKey: " + ikeV2VpnRunner.mSessionKey);
                StringBuilder sb = new StringBuilder();
                sb.append("MOBIKE ");
                sb.append(ikeV2VpnRunner.mMobikeEnabled ? "enabled" : "disabled");
                indentingPrintWriter.println(sb.toString());
                indentingPrintWriter.println("Profile: " + ikeV2VpnRunner.mProfile);
                indentingPrintWriter.println("Token: " + ikeV2VpnRunner.mCurrentToken);
                if (this.mDataStallSuspected) {
                    indentingPrintWriter.println("Data stall suspected");
                }
                if (ikeV2VpnRunner.mScheduledHandleDataStallFuture != null) {
                    indentingPrintWriter.println("Reset session scheduled");
                }
            }
            indentingPrintWriter.println();
            indentingPrintWriter.println("mCachedCarrierConfigInfoPerSubId=" + this.mCachedCarrierConfigInfoPerSubId);
            indentingPrintWriter.println("mEventChanges (most recent first):");
            indentingPrintWriter.increaseIndent();
            this.mEventChanges.reverseDump(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getCellSubIdForNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities == null || !networkCapabilities.hasTransport(0)) {
            return -1;
        }
        NetworkSpecifier networkSpecifier = networkCapabilities.getNetworkSpecifier();
        if (networkSpecifier instanceof TelephonyNetworkSpecifier) {
            return ((TelephonyNetworkSpecifier) networkSpecifier).getSubscriptionId();
        }
        return -1;
    }

    private boolean doesHaveVPNAppWhiteList() {
        IVpnExt iVpnExt = this.mVpnExt;
        if (iVpnExt != null) {
            return iVpnExt.doesHaveVPNAppWhiteList(null);
        }
        return false;
    }

    private boolean isInVPNAppWhiteList(String str) {
        IVpnExt iVpnExt = this.mVpnExt;
        if (iVpnExt != null) {
            return iVpnExt.isInVPNAppWhiteList(null, str);
        }
        return false;
    }
}
