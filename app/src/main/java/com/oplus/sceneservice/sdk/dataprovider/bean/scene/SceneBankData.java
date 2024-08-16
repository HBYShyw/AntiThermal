package com.oplus.sceneservice.sdk.dataprovider.bean.scene;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.annotation.Keep;

@Keep
/* loaded from: classes2.dex */
public class SceneBankData extends SceneData {
    public static final Parcelable.Creator<SceneBankData> CREATOR = new a();
    private static final String KEY_ACCOUNT = "Account";
    private static final String KEY_ACCOUNT_BALANCE = "AccountBalance";
    private static final String KEY_ARREARS_MONEY = "ArrearsMoney";
    private static final String KEY_AVAILABLE_STAGE_AMOUNT = "AvailableStageAmount";
    private static final String KEY_BANK_CARD_TYPE = "BankCardType";
    private static final String KEY_BILL_MONEY = "BillMoney";
    private static final String KEY_BILL_TIME = "BillTime";
    private static final String KEY_CAN_STAGE_NUM = "CanStageNum";
    private static final String KEY_CURRENT_BILL_OVERDRAFT = "CurrentBillOverdraft";
    private static final String KEY_CURRENT_NUM = "CurrentNum";
    private static final String KEY_DEAD_LINE = "DeadLine";
    private static final String KEY_DEAL_MONEY = "DealMoney";
    private static final String KEY_DEAL_TIME = "DealTime";
    private static final String KEY_DEAL_TYPE = "DealType";
    private static final String KEY_DUE_DAY = "DueDay";
    private static final String KEY_EACH_PRINCIPLE = "EachPrinciple";
    private static final String KEY_EACH_REPAY = "EachRepay";
    private static final String KEY_FIRST_COUNTER_FEE = "FirstCounterFee";
    private static final String KEY_FIRST_PRINCIPLE = "FirstPrinciple";
    private static final String KEY_FIRST_STAGE_REPAY = "FirstStageRepay";
    private static final String KEY_INTEREST_MONEY = "InterestMoney";
    private static final String KEY_LATE_TIME = "LateTime";
    private static final String KEY_LOAN_MONEY = "LoanMoney";
    private static final String KEY_MIN_REPAY = "MinRepay";
    private static final String KEY_MONTH = "Month";
    private static final String KEY_NEED_SAVE_UP = "NeedSaveUp";
    private static final String KEY_NO_BILL = "NoBill";
    private static final String KEY_OVERDUE_AMOUNT = "OverdueAmount";
    private static final String KEY_RATE = "Rate";
    private static final String KEY_REMAIN_MIN_REPAY = "RemainMinRepay";
    private static final String KEY_REMAIN_REPAY = "RemainRepay";
    private static final String KEY_SMS_BODY = "SmsBody";
    private static final String KEY_STAGE_AMOUNT = "StageAmount";
    private static final String KEY_STAGE_COUNTER_FEE = "StageCounterFee";
    private static final String KEY_STAGE_NUM = "StageNum";
    private static final String KEY_STAGE_TIME = "StageTime";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_TOTAL_COUNTER_FEE = "TotalCounterFee";
    private static final String KEY_TOTAL_COUNTER_RATE = "TotalCounterRate";
    private static final String TAG = "SceneBankData";

    /* loaded from: classes2.dex */
    class a implements Parcelable.Creator<SceneBankData> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public SceneBankData createFromParcel(Parcel parcel) {
            return new SceneBankData(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public SceneBankData[] newArray(int i10) {
            return new SceneBankData[i10];
        }
    }

    public SceneBankData() {
        setType(64);
    }

    @Override // com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData
    protected String getDefaultMatchKey() {
        return null;
    }

    @Override // com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData
    public boolean isValid() {
        return (TextUtils.isEmpty(this.mContent.getString(KEY_BILL_MONEY)) && TextUtils.isEmpty(this.mContent.getString(KEY_ARREARS_MONEY)) && TextUtils.isEmpty(this.mContent.getString(KEY_DEAL_MONEY)) && TextUtils.isEmpty(this.mContent.getString(KEY_LOAN_MONEY))) ? false : true;
    }

    public void setAccount(String str) {
        this.mContent.putString(KEY_ACCOUNT, str);
    }

    public void setAccountBalance(String str) {
        this.mContent.putString(KEY_ACCOUNT_BALANCE, str);
    }

    public void setArrearsMoney(String str) {
        this.mContent.putString(KEY_ARREARS_MONEY, str);
    }

    public void setAvailableStageAmount(String str) {
        this.mContent.putString(KEY_AVAILABLE_STAGE_AMOUNT, str);
    }

    public void setBankCardType(String str) {
        this.mContent.putString(KEY_BANK_CARD_TYPE, str);
    }

    public void setBillMoney(String str) {
        this.mContent.putString(KEY_BILL_MONEY, str);
    }

    public void setBillTime(String str) {
        this.mContent.putString(KEY_BILL_TIME, str);
    }

    public void setCanStageNum(String str) {
        this.mContent.putString(KEY_CAN_STAGE_NUM, str);
    }

    public void setCurrentBillOverdraft(String str) {
        this.mContent.putString(KEY_CURRENT_BILL_OVERDRAFT, str);
    }

    public void setCurrentNum(String str) {
        this.mContent.putString(KEY_CURRENT_NUM, str);
    }

    public void setDeadLine(String str) {
        this.mContent.putString(KEY_DEAD_LINE, str);
    }

    public void setDealMoney(String str) {
        this.mContent.putString(KEY_DEAL_MONEY, str);
    }

    public void setDealTime(String str) {
        this.mContent.putString(KEY_DEAL_TIME, str);
    }

    public void setDealType(String str) {
        this.mContent.putString(KEY_DEAL_TYPE, str);
    }

    public void setDueDay(String str) {
        this.mContent.putString(KEY_DUE_DAY, str);
    }

    public void setEachPrinciple(String str) {
        this.mContent.putString(KEY_EACH_PRINCIPLE, str);
    }

    public void setEachRepay(String str) {
        this.mContent.putString(KEY_EACH_REPAY, str);
    }

    public void setFirstCounterFee(String str) {
        this.mContent.putString(KEY_FIRST_COUNTER_FEE, str);
    }

    public void setFirstPrinciple(String str) {
        this.mContent.putString(KEY_FIRST_PRINCIPLE, str);
    }

    public void setFirstStageRepay(String str) {
        this.mContent.putString(KEY_FIRST_STAGE_REPAY, str);
    }

    public void setInterestMoney(String str) {
        this.mContent.putString(KEY_INTEREST_MONEY, str);
    }

    public void setLateTime(String str) {
        this.mContent.putString(KEY_LATE_TIME, str);
    }

    public void setLoanMoney(String str) {
        this.mContent.putString(KEY_LOAN_MONEY, str);
    }

    public void setMinRepay(String str) {
        this.mContent.putString(KEY_MIN_REPAY, str);
    }

    public void setMonth(String str) {
        this.mContent.putString(KEY_MONTH, str);
    }

    public void setNeedSaveUp(String str) {
        this.mContent.putString(KEY_NEED_SAVE_UP, str);
    }

    public void setNoBill(String str) {
        this.mContent.putString(KEY_NO_BILL, str);
    }

    public void setOverdueAmount(String str) {
        this.mContent.putString(KEY_OVERDUE_AMOUNT, str);
    }

    public void setRate(String str) {
        this.mContent.putString(KEY_RATE, str);
    }

    public void setRemainMinRepay(String str) {
        this.mContent.putString(KEY_REMAIN_MIN_REPAY, str);
    }

    public void setRemainRepay(String str) {
        this.mContent.putString(KEY_REMAIN_REPAY, str);
    }

    public void setSmsBody(String str) {
        this.mContent.putString(KEY_SMS_BODY, str);
    }

    public void setStageAmount(String str) {
        this.mContent.putString(KEY_STAGE_AMOUNT, str);
    }

    public void setStageCounterFee(String str) {
        this.mContent.putString(KEY_STAGE_COUNTER_FEE, str);
    }

    public void setStageNum(String str) {
        this.mContent.putString(KEY_STAGE_NUM, str);
    }

    public void setStageTime(String str) {
        this.mContent.putString(KEY_STAGE_TIME, str);
    }

    public void setTitle(String str) {
        this.mContent.putString(KEY_TITLE, String.valueOf(str));
    }

    public void setTotalCounterFee(String str) {
        this.mContent.putString(KEY_TOTAL_COUNTER_FEE, str);
    }

    public void setTotalCounterRate(String str) {
        this.mContent.putString(KEY_TOTAL_COUNTER_RATE, str);
    }

    public SceneBankData(Parcel parcel) {
        super(parcel);
    }
}
