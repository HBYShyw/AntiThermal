package android.graphics;

import android.os.Build;
import android.os.Process;
import android.util.Log;
import com.oplus.android.internal.util.OplusFrameworkStatsLog;
import com.oplus.osense.OsenseResClient;
import com.oplus.osense.info.OsenseSaRequest;

/* loaded from: classes.dex */
public class BitmapExtImpl implements IBitmapExt {
    private static final int THRESHOLD_OF_COMPRESS = 100000000;
    private static OsenseResClient sOsenseClient = null;
    private boolean mIsAssetSource = false;
    private boolean mIsCalculatedColor = false;
    private int mColorState = 0;
    private boolean mIsCanvasBaseBitmap = false;
    private boolean mIsViewSrc = false;
    private int mBitmapWidth = 1000;
    private int mBitmapHeight = 1000;
    private long mCompressStartTime = 0;

    public BitmapExtImpl(Object base) {
    }

    public boolean hasCalculatedColor() {
        return this.mIsCalculatedColor;
    }

    public void setHasCalculatedColor(boolean isCalculatedColor) {
        this.mIsCalculatedColor = isCalculatedColor;
    }

    public boolean isAssetSource() {
        return this.mIsAssetSource;
    }

    public void setIsAssetSource(boolean isAssetSource) {
        this.mIsAssetSource = isAssetSource;
    }

    public int getColorState() {
        return this.mColorState;
    }

    public void setColorState(int colorState) {
        this.mColorState = colorState;
    }

    public void setIsCanvasBaseBitmap(boolean isCanvasBaseBitmap) {
        this.mIsCanvasBaseBitmap = isCanvasBaseBitmap;
    }

    public boolean isCanvasBaseBitmap() {
        return this.mIsCanvasBaseBitmap;
    }

    public boolean isViewSrc() {
        return this.mIsViewSrc;
    }

    public void setIsViewSrc(boolean isViewSrc) {
        this.mIsViewSrc = isViewSrc;
    }

    public long osenseSetSceneAction(int timeout, int mWidth, int mHeight) {
        if (!"holi".equals(Build.BOARD)) {
            return -1L;
        }
        if (mWidth > this.mBitmapWidth || mHeight > this.mBitmapHeight) {
            if (sOsenseClient == null) {
                OsenseResClient osenseResClient = OsenseResClient.get(BitmapExtImpl.class);
                sOsenseClient = osenseResClient;
                if (osenseResClient == null) {
                    return -1L;
                }
            }
            return sOsenseClient.osenseSetSceneAction(new OsenseSaRequest("OSENSE_SYSTEM_SCENE_BITMAP", "OSENSE_ACTION_COMPRESS", timeout));
        }
        return -1L;
    }

    public void osenseClrSceneAction(long request, int mWidth, int mHeight) {
        OsenseResClient osenseResClient;
        if ("holi".equals(Build.BOARD)) {
            if ((mWidth > this.mBitmapWidth || mHeight > this.mBitmapHeight) && (osenseResClient = sOsenseClient) != null) {
                osenseResClient.osenseClrSceneAction(request);
            }
        }
    }

    public void compressStart() {
        this.mCompressStartTime = System.nanoTime();
    }

    public void compressEnd(int width, int height, int format, int quality) {
        long compressTime = System.nanoTime() - this.mCompressStartTime;
        if (compressTime > 100000000) {
            Log.p("Quality", "BitmapCompressInfo:" + width + "," + height + "," + format + "," + quality + "," + compressTime);
            OplusFrameworkStatsLog.write(100030, System.currentTimeMillis(), Process.myPid(), Process.myTid(), width, height, format, quality, compressTime);
        }
    }
}
