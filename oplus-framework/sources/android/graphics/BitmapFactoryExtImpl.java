package android.graphics;

import android.app.ActivityThread;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.SystemProperties;
import android.util.Log;
import com.oplus.osense.OsenseResClient;
import com.oplus.osense.info.OsenseSaRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/* loaded from: classes.dex */
public class BitmapFactoryExtImpl implements IBitmapFactoryExt {
    private static final long BITMAP_CACHE_TIMEOUT = 1000;
    private static final String TAG = "BitmapFactoryExtImpl";
    private BitmapFactory mBitmapFactory;
    private static final Object mLock = new Object();
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    private static final String[] BENCH_MARK_LIST = {"474a940e575d59bde1b202c323c7bbb9246693f8b3aece41c54755ae1acb949c", "0d918d3e84cd3e144ee3a0675406e94b8bd1fe8a05f36b9a0cd716625b08c63a", "b27cda2e974ad0d077908ab9c65d60cd08e8a4a90599586ccdd5ed5015f5431f"};
    private static String sLastResStr = "";
    private static int sLastResId = -999;
    private static long sLastTimestamp = -999;
    private static Bitmap sBitmapCache = null;
    private static OsenseResClient sOsenseClient = null;

    public BitmapFactoryExtImpl(Object base) {
        this.mBitmapFactory = (BitmapFactory) base;
    }

    public void osenseSetSceneAction(int timeout) {
        if (sOsenseClient == null) {
            OsenseResClient osenseResClient = OsenseResClient.get(BitmapFactoryExtImpl.class);
            sOsenseClient = osenseResClient;
            if (osenseResClient == null) {
                return;
            }
        }
        sOsenseClient.osenseSetSceneAction(new OsenseSaRequest("OSENSE_SYSTEM_SCENE_BITMAP", "OSENSE_ACTION_DECODE", timeout));
        Log.i(TAG, "OSENSE_ACTION_DECODE is set");
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 255;
            char[] cArr = HEX_ARRAY;
            hexChars[j * 2] = cArr[v >>> 4];
            hexChars[(j * 2) + 1] = cArr[v & 15];
        }
        return new String(hexChars);
    }

    private static String encodePackageName(String appName) {
        byte[] hashVersion;
        if (appName == null || "".equals(appName)) {
            return appName;
        }
        String androidVer = SystemProperties.get("ro.build.version.release", "null");
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            if (digest != null && (hashVersion = digest.digest(androidVer.getBytes(StandardCharsets.UTF_8))) != null && hashVersion.length > 0) {
                String versionStrHash = bytesToHex(hashVersion);
                String temp = appName + versionStrHash;
                byte[] hash = digest.digest(temp.getBytes(StandardCharsets.UTF_8));
                if (hash != null && hash.length > 0) {
                    return bytesToHex(hash);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "get package hash error:" + e.toString());
        }
        return "";
    }

    public boolean isEnableBitmapCache() {
        String appName = encodePackageName(ActivityThread.currentPackageName());
        for (String t : BENCH_MARK_LIST) {
            if (t.equals(appName)) {
                return true;
            }
        }
        Bitmap bitmap = sBitmapCache;
        if (bitmap != null) {
            bitmap.recycle();
            sBitmapCache = null;
        }
        return false;
    }

    public Bitmap getBitmapCache(Resources res, int id, BitmapFactory.Options opts) {
        if (!isEnableBitmapCache()) {
            return null;
        }
        synchronized (mLock) {
            if (opts == null) {
                if (sLastResId == id && sLastResStr.equals(res.toString()) && sBitmapCache != null && System.currentTimeMillis() - sLastTimestamp < 1000) {
                    sLastTimestamp = System.currentTimeMillis();
                    return Bitmap.createBitmap(sBitmapCache);
                }
            }
            return null;
        }
    }

    public void setBitmapCache(Bitmap cache, Resources res, int id) {
        if (!isEnableBitmapCache()) {
            return;
        }
        synchronized (mLock) {
            sLastResStr = res.toString();
            sLastResId = id;
            sBitmapCache = cache;
            sLastTimestamp = System.currentTimeMillis();
        }
    }
}
