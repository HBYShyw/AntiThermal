package com.oplus.dynamicframerate;

import android.app.ActivityThread;
import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;
import android.view.animation.AnimationUtils;
import java.lang.reflect.Array;

/* loaded from: classes.dex */
public class UserActionStats {
    public static final int COL;
    public static final int PRINT_COL_WIDTH = 18;
    public static final String[] PRINT_ROW_NAMES;
    public static final int PRINT_TIME_INTERVAL_MS = 20000;
    public static final int ROW;
    public static final String TAG = "UserActionStats";
    public long mCurFrameTimeMs;
    public long mCurTime;
    public String mCurrentProcessName;
    public long mLastTime;
    public long mLaunchTime;
    public int mPkgIndex;
    public long mPrevFrameTimeMs;
    public int[][] mVecStats;
    public static final String STRING_PRINT_FRAMES_INTERVAL = "debug.dynamicframerate.printframesinterval";
    public static final int PRINT_FRAMES_INTERVAL = SystemProperties.getInt(STRING_PRINT_FRAMES_INTERVAL, 10000);
    public static boolean DEBUG_USERACTIONSTATS_PRINT_BY_TIME = SystemProperties.getBoolean("debug.os.perf.adfr.mydebugforUserActionStatsPrintByTime", false);
    public static final String[] TOP_PKG_LIST = {"com.qiyi.video", "com.tencent.qqlive", "com.youku.phone", "tv.danmaku.bili", "com.ss.android.ugc.aweme", "com.ss.android.ugc.aweme.lite", "com.smile.gifmaker", "com.kuaishou.nebula", "com.tencent.mobileqq", "com.tencent.mm", "com.taobao.taobao", "com.xunmeng.pinduoduo", "com.jingdong.app.mall", "com.UCMobile", "com.ss.android.article.news", "com.ss.android.article.lite", "com.sina.weibo", "com.sankuai.meituan", "com.eg.android.AlipayGphone", "com.xingin.xhs"};
    public static final String[] PRINT_COL_NAMES = {"", "IDLE", "INPUT_IDLE", "SCROLL", "FLING", "SCROLL_BAR_FADE", "WINDOW", "SUM_ALL"};
    public long[] mAllStateDurTimeStatsMs = {0, 0, 0, 0, 0, 0, 0};
    public int mFramesCount = 0;

    static {
        String[] strArr = {"vel[0,100):       ", "vel[100,200):     ", "vel[200,300):     ", "vel[300,400):     ", "vel[400,500):     ", "vel[500,600):     ", "vel[600,700):     ", "vel[700,800):     ", "vel[800,900):     ", "vel[900,1000):    ", "vel[1000,∞):      ", "sum(frames)       ", "durtime(ms):      "};
        PRINT_ROW_NAMES = strArr;
        ROW = strArr.length;
        COL = r1.length - 1;
    }

    public UserActionStats() {
        String packageName = getPackageName();
        this.mCurrentProcessName = packageName;
        this.mPkgIndex = checkPkgIsInList(packageName);
        this.mVecStats = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, ROW, COL);
        this.mPrevFrameTimeMs = AnimationUtils.currentAnimationTimeMillis();
        this.mCurFrameTimeMs = AnimationUtils.currentAnimationTimeMillis();
        this.mLaunchTime = AnimationUtils.currentAnimationTimeMillis();
        if (this.mPkgIndex >= 0) {
            Log.i(TAG, "Start collecting user action stats data in " + this.mCurrentProcessName + " BY TIME every 20000 ms and cur time = " + this.mLaunchTime);
            if (DEBUG_USERACTIONSTATS_PRINT_BY_TIME) {
                Log.i(TAG, "Start collecting user action stats data in " + this.mCurrentProcessName + " BY FRAME every " + PRINT_FRAMES_INTERVAL + " frames and cur time = " + this.mLaunchTime);
            }
        }
    }

    private String getPackageName() {
        Context context = ActivityThread.currentApplication().getApplicationContext();
        if (context != null) {
            return context.getPackageName();
        }
        return "";
    }

    private int checkPkgIsInList(String currentProcessName) {
        int i = 0;
        while (true) {
            String[] strArr = TOP_PKG_LIST;
            if (i < strArr.length) {
                if (!strArr[i].equals(currentProcessName)) {
                    i++;
                } else {
                    return i;
                }
            } else {
                return -1;
            }
        }
    }

    public String vecStatsToString(int pkgIndex) {
        int i;
        String temp;
        StringBuilder strStats = new StringBuilder();
        strStats.append(TOP_PKG_LIST[pkgIndex]);
        strStats.append("\r\n");
        int i2 = 0;
        while (true) {
            String[] strArr = PRINT_COL_NAMES;
            if (i2 >= strArr.length) {
                break;
            }
            strStats.append(strArr[i2]);
            for (int j = 0; j < 18 - PRINT_COL_NAMES[i2].length(); j++) {
                strStats.append(" ");
            }
            i2++;
        }
        strStats.append("\r\n");
        long sumofallstatetime = 0;
        int j2 = 0;
        while (true) {
            i = COL;
            if (j2 >= i - 1) {
                break;
            }
            sumofallstatetime += this.mAllStateDurTimeStatsMs[j2];
            j2++;
        }
        this.mAllStateDurTimeStatsMs[i - 1] = sumofallstatetime;
        for (int i3 = 0; i3 < ROW; i3++) {
            strStats.append(PRINT_ROW_NAMES[i3]);
            for (int j3 = 0; j3 < COL; j3++) {
                if (i3 == ROW - 1) {
                    temp = Long.toString(this.mAllStateDurTimeStatsMs[j3]);
                } else {
                    temp = Integer.toString(this.mVecStats[i3][j3]);
                }
                int templength = temp.length();
                for (int k = 0; k < 18 - templength; k++) {
                    temp = temp + " ";
                }
                strStats.append(temp);
            }
            strStats.append("\r\n");
        }
        return strStats.toString();
    }

    public void changeVecStats(int velocityStats, int state, int pkgIndex) {
        int velrow = velocityStats >= 1000 ? 10 : velocityStats / 100;
        int[][] iArr = this.mVecStats;
        int[] iArr2 = iArr[velrow];
        iArr2[state] = iArr2[state] + 1;
        int i = ROW;
        int[] iArr3 = iArr[i - 2];
        iArr3[state] = iArr3[state] + 1;
        int[] iArr4 = iArr[i - 2];
        int i2 = COL;
        int i3 = i2 - 1;
        iArr4[i3] = iArr4[i3] + 1;
        int i4 = i2 - 1;
        iArr2[i4] = iArr2[i4] + 1;
        if (!DEBUG_USERACTIONSTATS_PRINT_BY_TIME) {
            int velrow2 = this.mFramesCount;
            if (velrow2 % PRINT_FRAMES_INTERVAL == 0) {
                String ans = vecStatsToString(pkgIndex);
                Log.i(TAG, ans);
                return;
            }
            return;
        }
        long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
        this.mCurTime = currentAnimationTimeMillis;
        long durtimesincelaunch = currentAnimationTimeMillis - this.mLaunchTime;
        long temp = durtimesincelaunch % 20000;
        if (temp <= 20 && currentAnimationTimeMillis - this.mLastTime > 20) {
            String ans2 = vecStatsToString(pkgIndex);
            Log.i(TAG, ans2);
            this.mLastTime = this.mCurTime;
        }
    }
}
