package com.oplus.sceneservice.sdk.dataprovider.bean.scene;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.annotation.Keep;
import java.util.Calendar;
import l9.LogUtils;

@Keep
/* loaded from: classes2.dex */
public abstract class SceneData implements Parcelable {
    public static final int DATA_CHANGED = 1;
    public static final int DATA_CHANGED_NO = 0;
    public static final String DATA_TIME_FORMAT = "MM月dd日 HH:mm";
    public static final long DISAPPEAR_TIME_STAMP = -1;
    public static final long INVALID_TIME_STAMP = 0;
    public static final String KEY_ADDRESS_LIST = "AddressList";
    public static final String KEY_ARRIVE_TIME_STAMP = "TravelArriveTime";
    public static final String KEY_DETAIL_TYPE = "DetailType";
    private static final String KEY_MANUAL_ADD_RESULT = "ManualAddResult";
    public static final String KEY_MATCH_KEY = "MatchKey";
    public static final String KEY_NAV_DESTINATION = "NavDestination";
    public static final String KEY_OCCUR_TIME = "OccurTime";
    public static final String KEY_ORDER_INFO = "OrderInfo";
    public static final String KEY_RECEIVED_TIME = "ReceivedTime";
    public static final String KEY_SUCCESS_ONLINE = "SuccessOnline";
    public static final String KEY_TIME_STAMP = "TimeStamp";
    public static final int PROCESSED_NO = 0;
    public static final int PROCESSED_YES = 1;
    public static final int SCENE_DELETED = 1;
    public static final int SCENE_NO_DELETED = 0;
    public static final int SCENE_SOURCE_AI = 2;
    public static final int SCENE_SOURCE_EVENT = 5;
    public static final int SCENE_SOURCE_NONE = -1;
    public static final int SCENE_SOURCE_ONLINE = 3;
    public static final int SCENE_SOURCE_SMS = 0;
    public static final int SCENE_SOURCE_SUB_TYPE_SMS = 1;
    public static final int SCENE_SOURCE_USER = 1;
    private static final String TAG = "SceneData";
    protected Bundle mContent;
    private Bundle mData2;
    private String mData3;
    private int mDataChangedState;
    private long mExpireTime;
    private String mExpireTimezone;
    private String mId;
    private boolean mIsDeleted;
    private String mLang;
    private long mLastOnlineTime;
    private int mLastUpdateSource;
    private String mMatchKey;
    private long mOccurTime;
    private String mOccurTimezone;
    private int mProcessStep;
    private boolean mProcessed;
    private int mSource;
    private String mSourceDataKey;
    private int mState;
    private String mSubtractId;
    private String mTargetTel;
    private int mType;

    public SceneData() {
        this.mContent = new Bundle();
        this.mLang = "zh-rCN";
        this.mOccurTime = 0L;
        this.mExpireTime = 0L;
        this.mLastOnlineTime = 0L;
        this.mIsDeleted = false;
        this.mLastUpdateSource = -1;
        this.mDataChangedState = 0;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public long getArriveTimeStamp() {
        String string = this.mContent.getString(KEY_ARRIVE_TIME_STAMP);
        if (!TextUtils.isEmpty(string)) {
            try {
                return Long.parseLong(string);
            } catch (NumberFormatException unused) {
                LogUtils.b(TAG, "getReceivedTime strRet str = " + string);
            }
        }
        return 0L;
    }

    public Bundle getContent() {
        return this.mContent;
    }

    public Bundle getData2() {
        return this.mData2;
    }

    public String getData3() {
        return this.mData3;
    }

    public int getDataChangedState() {
        return this.mDataChangedState;
    }

    protected long getDefaultExpireTime() {
        return 0L;
    }

    protected abstract String getDefaultMatchKey();

    public int getDetailType() {
        try {
            return Integer.parseInt(this.mContent.getString(KEY_DETAIL_TYPE));
        } catch (NumberFormatException e10) {
            LogUtils.b(TAG, "getDetailType e = " + e10);
            return -1;
        }
    }

    public long getExpireTime() {
        long j10 = this.mExpireTime;
        return j10 == 0 ? getDefaultExpireTime() : j10;
    }

    public String getExpireTimezone() {
        return this.mExpireTimezone;
    }

    public String getId() {
        return this.mId;
    }

    public final String getLanguage() {
        return this.mLang;
    }

    public long getLastOnlineTime() {
        return this.mLastOnlineTime;
    }

    public int getLastUpdateSource() {
        return this.mLastUpdateSource;
    }

    public String getManualAddResult() {
        return this.mContent.getString(KEY_MANUAL_ADD_RESULT);
    }

    public final String getMatchKey() {
        return TextUtils.isEmpty(this.mMatchKey) ? getDefaultMatchKey() : this.mMatchKey;
    }

    public final String getNavDestination() {
        return this.mContent.getString(KEY_NAV_DESTINATION);
    }

    public final long getOccurTime() {
        return this.mOccurTime;
    }

    public final String getOccurTimezone() {
        return this.mOccurTimezone;
    }

    public String getOnlineKey() {
        return null;
    }

    public String getOrderInfo() {
        return this.mContent.getString(KEY_ORDER_INFO);
    }

    public int getProcessStep() {
        return this.mProcessStep;
    }

    public long getReceivedTime() {
        String string = this.mContent.getString(KEY_RECEIVED_TIME);
        if (!TextUtils.isEmpty(string)) {
            try {
                return Long.parseLong(string);
            } catch (NumberFormatException unused) {
                LogUtils.b(TAG, "getReceivedTime strRet str = " + string);
            }
        }
        return 0L;
    }

    public int getSource() {
        return this.mSource;
    }

    public final String getSourceKey() {
        return this.mSourceDataKey;
    }

    public int getState() {
        return this.mState;
    }

    public String getSubtractId() {
        return this.mSubtractId;
    }

    public String getTargetTel() {
        return this.mTargetTel;
    }

    public int getType() {
        return this.mType;
    }

    public boolean isDeleted() {
        return this.mIsDeleted;
    }

    public final boolean isExpired() {
        return Calendar.getInstance().getTimeInMillis() - getExpireTime() > 0;
    }

    public boolean isOnlineConvertSuccess() {
        return this.mContent.getBoolean(KEY_SUCCESS_ONLINE);
    }

    public boolean isProcessed() {
        return this.mProcessed;
    }

    public boolean isUsable() {
        return true;
    }

    public boolean isValid() {
        return false;
    }

    public void setArriveTimeStamp(long j10) {
        this.mContent.putString(KEY_ARRIVE_TIME_STAMP, String.valueOf(j10));
    }

    public void setContent(Bundle bundle) {
        if (bundle == null) {
            this.mContent = new Bundle();
        } else {
            this.mContent = bundle;
        }
    }

    public void setData2(Bundle bundle) {
        this.mData2 = bundle;
    }

    public void setData3(String str) {
        this.mData3 = str;
    }

    public void setDataChangedState(int i10) {
        this.mDataChangedState = i10;
    }

    public void setDeleted(boolean z10) {
        this.mIsDeleted = z10;
    }

    public void setDetailType(int i10) {
        this.mContent.putString(KEY_DETAIL_TYPE, String.valueOf(i10));
    }

    public void setExpireTime(long j10) {
        this.mExpireTime = j10;
    }

    public void setExpireTimezone(String str) {
        this.mExpireTimezone = str;
    }

    public void setId(String str) {
        this.mId = str;
    }

    public final void setLanguage(String str) {
        this.mLang = str;
    }

    public void setLastOnlineTime(long j10) {
        this.mLastOnlineTime = j10;
    }

    public void setLastUpdateSource(int i10) {
        this.mLastUpdateSource = i10;
    }

    public void setManualAddResult(String str) {
        this.mContent.putString(KEY_MANUAL_ADD_RESULT, str);
    }

    public final void setMatchKey(String str) {
        this.mMatchKey = str;
    }

    public final void setNavDestination(String str) {
        this.mContent.putString(KEY_NAV_DESTINATION, str);
    }

    public final void setOccurTime(long j10) {
        this.mOccurTime = j10;
    }

    public final void setOccurTimezone(String str) {
        this.mOccurTimezone = str;
    }

    public void setOnlineConvertSuccess(boolean z10) {
        this.mContent.putBoolean(KEY_SUCCESS_ONLINE, z10);
    }

    public void setOrderInfo(String str) {
        this.mContent.putString(KEY_ORDER_INFO, str);
    }

    public void setProcessStep(int i10) {
        this.mProcessStep = i10;
    }

    public void setProcessed(boolean z10) {
        this.mProcessed = z10;
    }

    public void setReceivedTime(long j10) {
        this.mContent.putString(KEY_RECEIVED_TIME, String.valueOf(j10));
    }

    public void setSource(int i10) {
        this.mSource = i10;
    }

    public final void setSourceDataKey(String str) {
        this.mSourceDataKey = str;
    }

    public void setState(int i10) {
        this.mState = i10;
    }

    public void setSubtractId(String str) {
        this.mSubtractId = str;
    }

    public void setTargetTel(String str) {
        this.mTargetTel = str;
    }

    public void setTimeStamp(long j10) {
        this.mContent.putString(KEY_TIME_STAMP, String.valueOf(j10));
    }

    public void setType(int i10) {
        this.mType = i10;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("SceneData {");
        sb2.append("mId:" + getId());
        sb2.append(", mType:" + getType());
        sb2.append(", mMatchKey:" + getMatchKey());
        sb2.append(", mOccurTime:" + getOccurTime());
        sb2.append(", mExpireTime:" + getExpireTime());
        sb2.append(", mLastOnlineTime:" + getLastOnlineTime());
        sb2.append(", mIsOnlineConvertSuccess:" + isOnlineConvertSuccess());
        sb2.append(", mIsDeleted:" + isDeleted());
        sb2.append(",mSource:" + this.mSource);
        sb2.append(", mProcessed:" + isProcessed());
        sb2.append(", mProcessStep:" + getProcessStep());
        sb2.append(", mSource:" + getSource());
        sb2.append(", mContent:" + this.mContent);
        sb2.append(", DATA2:" + this.mData2);
        sb2.append(", DATA3:" + this.mData3);
        sb2.append(", TARGET_TEL:" + this.mTargetTel);
        sb2.append(", mOccurTimezone:" + getOccurTimezone());
        sb2.append(", mExpireTimezone:" + getExpireTimezone());
        sb2.append(", mDataChangedState:" + getDataChangedState());
        sb2.append("}");
        return sb2.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i10) {
        parcel.writeString(this.mMatchKey);
        parcel.writeString(this.mLang);
        parcel.writeLong(this.mOccurTime);
        parcel.writeString(this.mOccurTimezone);
        parcel.writeBundle(this.mContent);
    }

    public SceneData(Parcel parcel) {
        this.mContent = new Bundle();
        this.mLang = "zh-rCN";
        this.mOccurTime = 0L;
        this.mExpireTime = 0L;
        this.mLastOnlineTime = 0L;
        this.mIsDeleted = false;
        this.mLastUpdateSource = -1;
        this.mDataChangedState = 0;
        this.mMatchKey = parcel.readString();
        this.mLang = parcel.readString();
        this.mOccurTime = parcel.readLong();
        this.mOccurTimezone = parcel.readString();
        this.mContent.putAll(parcel.readBundle(getClass().getClassLoader()));
    }
}
