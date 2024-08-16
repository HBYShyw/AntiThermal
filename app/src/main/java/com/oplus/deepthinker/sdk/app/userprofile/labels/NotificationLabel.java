package com.oplus.deepthinker.sdk.app.userprofile.labels;

import android.text.TextUtils;
import com.oplus.deepthinker.sdk.app.SDKLog;
import java.util.Map;

/* loaded from: classes.dex */
public class NotificationLabel {
    private static final String TAG = "NotificationLabel";
    public static final int TYPE_HIGH_IMPORTANT = 3;
    public static final int TYPE_IGNORE = 0;
    public static final int TYPE_IMPORTANT = 2;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_UNKNOWN = -1;
    private Details mDetails;
    private Map<String, Integer> mLabelResult;

    /* loaded from: classes.dex */
    public static class Details {
        public static final int DETAIL_SIZE = 5;
        public static final int INDEX_AVG_CLICK_DURATION = 3;
        public static final int INDEX_AVG_REMOVE_DURATION = 4;
        public static final int INDEX_CLICK_NUM = 1;
        public static final int INDEX_REMOVE_NUM = 2;
        public static final int INDEX_TOTAL_NUM = 0;
        public static final String SPLITTER = ",";
        private long mAvgClickDur;
        private long mAvgRemoveDur;
        private int mClickNum;
        private int mRemoveNum;
        private int mTotalNum;

        Details(int i10, int i11, int i12, long j10, long j11) {
            this.mTotalNum = i10;
            this.mClickNum = i11;
            this.mRemoveNum = i12;
            this.mAvgClickDur = j10;
            this.mAvgRemoveDur = j11;
        }

        public static Details parseDetailString(String str) {
            if (TextUtils.isEmpty(str)) {
                SDKLog.w(NotificationLabel.TAG, "parseDetailString error: detail is empty");
                return null;
            }
            String[] split = str.split(",");
            if (split.length != 5) {
                SDKLog.w(NotificationLabel.TAG, "parseDetailString error: split result unexpected");
                return null;
            }
            return new Details(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Long.parseLong(split[3]), Long.parseLong(split[4]));
        }

        public long getAvgClickDur() {
            return this.mAvgClickDur;
        }

        public long getAvgRemoveDur() {
            return this.mAvgRemoveDur;
        }

        public int getClickNum() {
            return this.mClickNum;
        }

        public int getRemoveNum() {
            return this.mRemoveNum;
        }

        public int getTotalNum() {
            return this.mTotalNum;
        }

        public String toString() {
            return "Details{mTotalNum=" + this.mTotalNum + ", mClickNum=" + this.mClickNum + ", mRemoveNum=" + this.mRemoveNum + ", mAvgClickDur=" + this.mAvgClickDur + ", mAvgRemoveDur=" + this.mAvgRemoveDur + '}';
        }
    }
}
