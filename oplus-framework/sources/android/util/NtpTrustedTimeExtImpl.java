package android.util;

import android.content.Context;
import android.content.pm.OplusPackageManager;
import android.net.Network;
import android.net.OplusHttpClient;
import android.net.SntpClient;
import android.os.OplusPropertyList;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.NtpTrustedTime;
import com.oplus.widget.OplusMaxLinearLayout;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/* loaded from: classes.dex */
public class NtpTrustedTimeExtImpl implements INtpTrustedTimeExt {
    private static final boolean IS_SUPPORT_OPLUS_NTP_TRUSTED_TIME = false;
    private static final boolean LOGD = true;
    private static final String TAG = "NtpTrustedTimeExtImpl";
    private Context mContext;
    private NtpTrustedTime mNtpTrustedTime;
    private OplusPackageManager mPm;
    private boolean mRefreshNeedReturn = false;
    protected String[] mOplusNTPserverArray = {"", "", ""};

    public NtpTrustedTimeExtImpl(Object base) {
        this.mNtpTrustedTime = (NtpTrustedTime) base;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mPm = new OplusPackageManager(context);
        Slog.d(TAG, "init");
    }

    private boolean isAutomaticTimeRequested() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "auto_time", 0) != 0;
    }

    private boolean foceRefreshForCnRegion(int localTimeout) {
        String region = SystemProperties.get(OplusPropertyList.PROPERTY_REGION, OplusPropertyList.OPLUS_VERSION);
        if (OplusPropertyList.OPLUS_VERSION.equals(region) || "OC".equals(region)) {
            Log.e(TAG, "foceRefreshForCnRegion, get feature: FEATURE_AUTOTIMEUPDATE_FORCE");
            OplusHttpClient oplusHttpClient = new OplusHttpClient();
            if (oplusHttpClient.requestTime(this.mContext, 0, localTimeout)) {
                Log.d(TAG, "Use oplus http server algin time success!");
                updateCacheStatus(oplusHttpClient.getHttpTime(), oplusHttpClient.getHttpTimeReference(), oplusHttpClient.getRoundTripTime() / 2, oplusHttpClient.getServerSocketAddress());
                return true;
            }
            InetAddress.clearDnsCache();
            if (oplusHttpClient.requestTime(this.mContext, 1, localTimeout)) {
                Log.d(TAG, "Use oplus http server1 algin time success!");
                updateCacheStatus(oplusHttpClient.getHttpTime(), oplusHttpClient.getHttpTimeReference(), oplusHttpClient.getRoundTripTime() / 2, oplusHttpClient.getServerSocketAddress());
                return true;
            }
        }
        return false;
    }

    public boolean refreshOplusNtpTrustedTime(Network network, int port, String server, int localTimeout) {
        int size;
        if (isClosedSuperFirewall()) {
            Log.w(TAG, "in test.");
            this.mRefreshNeedReturn = false;
            return true;
        }
        if (!isAutomaticTimeRequested()) {
            Log.d(TAG, "Settings.Global.AUTO_TIME = 0");
            this.mRefreshNeedReturn = true;
            return false;
        }
        if (foceRefreshForCnRegion(localTimeout)) {
            this.mRefreshNeedReturn = true;
            return true;
        }
        if (isSupportOplusNtpTrustedTime()) {
            this.mRefreshNeedReturn = true;
            SntpClient client = new SntpClient();
            String[] strArr = this.mOplusNTPserverArray;
            strArr[0] = server;
            int size2 = strArr.length;
            String backupServer = updateBackupStatus();
            if (backupServer != null) {
                this.mOplusNTPserverArray[2] = backupServer;
                size = size2;
            } else {
                int size3 = this.mOplusNTPserverArray.length - 1;
                size = size3;
            }
            for (int i = 0; i < size; i++) {
                boolean isNtpError = "1".equals(SystemProperties.get("sys.ntp.exception", "0"));
                if (isNtpError) {
                    SystemClock.sleep(localTimeout);
                } else if (client.requestTime(this.mOplusNTPserverArray[i], port, localTimeout, network)) {
                    updateCacheStatus(client.getNtpTime(), client.getNtpTimeReference(), client.getRoundTripTime() / 2, client.getServerSocketAddress());
                    return true;
                }
            }
            return false;
        }
        this.mRefreshNeedReturn = false;
        return false;
    }

    public boolean isRefreshNtpNeedReturn() {
        return this.mRefreshNeedReturn;
    }

    private boolean isSupportOplusNtpTrustedTime() {
        return false;
    }

    private void updateCacheStatus(long cacheNtpTime, long cacheNtpElapRealTim, long cacheNtpCertainty, InetSocketAddress ntpServerSocketAddress) {
        if (ntpServerSocketAddress != null) {
            this.mNtpTrustedTime.getWrapper().setTimeResult(new NtpTrustedTime.TimeResult(cacheNtpTime, cacheNtpElapRealTim, saturatedCast(cacheNtpCertainty), ntpServerSocketAddress));
        } else {
            Log.w(TAG, "ntpServerSocketAddress is null.");
        }
    }

    private String updateBackupStatus() {
        return null;
    }

    private boolean isClosedSuperFirewall() {
        OplusPackageManager oplusPackageManager = this.mPm;
        if (oplusPackageManager != null) {
            return oplusPackageManager.isClosedSuperFirewall();
        }
        return false;
    }

    private static int saturatedCast(long longValue) {
        if (longValue > 2147483647L) {
            return OplusMaxLinearLayout.INVALID_MAX_VALUE;
        }
        if (longValue < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        return (int) longValue;
    }
}
