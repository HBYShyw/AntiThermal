package com.oplus.sceneservice.sdk.dataprovider.bean.scene;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.annotation.Keep;
import l9.LogUtils;

@Keep
/* loaded from: classes2.dex */
public class SceneExpressageData extends SceneData {
    private static final String COMPANY = "company";
    private static final String CONTENT_PROVIDER = "content_provider";
    public static final Parcelable.Creator<SceneExpressageData> CREATOR = new a();
    public static final long DEFAULT_EXPIRE_DELAY_IN_MILLISECOND = 2147483647000L;
    public static final int EXPIRE_DELAY_IN_DAY_SIGNED = 1;
    private static final String LAST_DETAIL = "last_detail";
    private static final String LAST_STATUS = "last_status";
    private static final String LAST_STATUS_TIME = "last_status_time";
    private static final String NUMBER = "number";
    private static final String PICKUP_ADDRESS = "pickup_address";
    private static final String PICKUP_CODE = "pickup_code";
    private static final String PROVIDER_ACTION = "provider_action";
    private static final String QUICK_APP_URL = "quick_app_url";
    public static final int RECOMMEND_EXPIRE_DELAY_IN_HOUR = 72;
    private static final String STATUS = "express_status";
    public static final int STATUS_NONE = 0;
    public static final int STATUS_PICK_UP_NOTIFY = 2;
    public static final int STATUS_SIGNED = 101;
    private static final String STEP_TRACE = "step_trace";
    private static final String TAG = "SceneExpressageData";

    /* loaded from: classes2.dex */
    class a implements Parcelable.Creator<SceneExpressageData> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public SceneExpressageData createFromParcel(Parcel parcel) {
            return new SceneExpressageData(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public SceneExpressageData[] newArray(int i10) {
            return new SceneExpressageData[i10];
        }
    }

    public SceneExpressageData() {
        setType(16);
    }

    public String getCompany() {
        return this.mContent.getString("company");
    }

    public String getContentProvider() {
        return this.mContent.getString(CONTENT_PROVIDER);
    }

    @Override // com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData
    protected long getDefaultExpireTime() {
        return DEFAULT_EXPIRE_DELAY_IN_MILLISECOND;
    }

    @Override // com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData
    protected String getDefaultMatchKey() {
        return this.mContent.getString(NUMBER);
    }

    public String getLastDetail() {
        return this.mContent.getString(LAST_DETAIL);
    }

    public String getLastStatus() {
        return this.mContent.getString(LAST_STATUS);
    }

    public String getLastStatusTime() {
        return this.mContent.getString(LAST_STATUS_TIME);
    }

    public String getNumber() {
        return this.mContent.getString(NUMBER);
    }

    @Override // com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData
    public String getOnlineKey() {
        return this.mContent.getString(NUMBER);
    }

    public String getPickupAddress() {
        return this.mContent.getString(PICKUP_ADDRESS);
    }

    public String getPickupCode() {
        return this.mContent.getString(PICKUP_CODE);
    }

    public String getProviderAction() {
        return this.mContent.getString(PROVIDER_ACTION, "");
    }

    public String getQuickAppUrl() {
        return this.mContent.getString(QUICK_APP_URL);
    }

    public int getStatus() {
        String string = this.mContent.getString(STATUS);
        if (!TextUtils.isEmpty(string)) {
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException unused) {
                LogUtils.b(TAG, "getStatus status str = " + string);
            }
        }
        return 0;
    }

    public String getStepTrace() {
        return this.mContent.getString(STEP_TRACE);
    }

    @Override // com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData
    public boolean isUsable() {
        return ((!TextUtils.isEmpty(getLastDetail()) && (getLastOnlineTime() > 0L ? 1 : (getLastOnlineTime() == 0L ? 0 : -1)) != 0) || (getStatus() == 2)) && super.isUsable();
    }

    @Override // com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData
    public boolean isValid() {
        return !TextUtils.isEmpty(this.mContent.getString(NUMBER)) || (getStatus() == 2 && !TextUtils.isEmpty(getPickupCode()));
    }

    public void setCompany(String str) {
        this.mContent.putString("company", str);
    }

    public void setContentProvider(String str) {
        this.mContent.putString(CONTENT_PROVIDER, str);
    }

    public void setLastDetail(String str) {
        this.mContent.putString(LAST_DETAIL, str);
    }

    public void setLastStatus(String str) {
        this.mContent.putString(LAST_STATUS, str);
    }

    public void setLastStatusTime(String str) {
        this.mContent.putString(LAST_STATUS_TIME, str);
    }

    public void setNumber(String str) {
        this.mContent.putString(NUMBER, str);
    }

    public void setPickupAddress(String str) {
        this.mContent.putString(PICKUP_ADDRESS, str);
    }

    public void setPickupCode(String str) {
        this.mContent.putString(PICKUP_CODE, str);
    }

    public void setProviderAction(String str) {
        this.mContent.putString(PROVIDER_ACTION, str);
    }

    public void setQuickAppUrl(String str) {
        this.mContent.putString(QUICK_APP_URL, str);
    }

    public void setStatus(int i10) {
        this.mContent.putString(STATUS, String.valueOf(i10));
    }

    public void setStepTrace(String str) {
        this.mContent.putString(STEP_TRACE, str);
    }

    public SceneExpressageData(Parcel parcel) {
        super(parcel);
    }
}
