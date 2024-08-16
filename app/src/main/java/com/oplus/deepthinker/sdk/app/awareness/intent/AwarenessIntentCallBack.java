package com.oplus.deepthinker.sdk.app.awareness.intent;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.DeviceEventResult;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.IntentResult;
import com.oplus.deepthinker.sdk.app.api.ApiCallBack;
import com.oplus.deepthinker.sdk.app.api.InternalApiCall;
import com.oplus.deepthinker.sdk.app.awareness.AwarenessConstants;
import kotlin.Metadata;
import ma.Unit;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: AwarenessIntentCallBack.kt */
@Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\b&\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0011\u0012\b\b\u0002\u0010\t\u001a\u00020\b¢\u0006\u0004\b\r\u0010\u000eJ\u0010\u0010\u0005\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0003H&J\u0010\u0010\u0007\u001a\u00020\u00022\b\u0010\u0004\u001a\u0004\u0018\u00010\u0006R\u0017\u0010\t\u001a\u00020\b8\u0006¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\f¨\u0006\u000f"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/intent/AwarenessIntentCallBack;", "Lcom/oplus/deepthinker/sdk/app/api/ApiCallBack;", "Lma/f0;", "Lcom/oplus/deepthinker/sdk/app/aidl/proton/intentdecision/IntentResult;", "result", "onIntentResultChanged", "Lcom/oplus/deepthinker/sdk/app/aidl/eventfountain/DeviceEventResult;", "onEventStateChanged", "", TriggerEvent.NOTIFICATION_TAG, "Ljava/lang/String;", "getTag", "()Ljava/lang/String;", "<init>", "(Ljava/lang/String;)V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public abstract class AwarenessIntentCallBack extends ApiCallBack<Unit> {
    private final String tag;

    public AwarenessIntentCallBack() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public /* synthetic */ AwarenessIntentCallBack(String str, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((i10 & 1) != 0 ? AwarenessConstants.INTENT_NAME : str);
    }

    public final String getTag() {
        return this.tag;
    }

    @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventCallback, com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback
    public final void onEventStateChanged(DeviceEventResult deviceEventResult) {
        IntentResult intentResult;
        if (deviceEventResult == null || deviceEventResult.getEventType() != 525) {
            return;
        }
        int eventStateType = deviceEventResult.getEventStateType();
        if (eventStateType == 0) {
            InternalApiCall.Companion companion = InternalApiCall.INSTANCE;
            Bundle extraData = deviceEventResult.getExtraData();
            k.d(extraData, "result.extraData");
            int resultCode = companion.getResultCode(extraData);
            Bundle extraData2 = deviceEventResult.getExtraData();
            k.d(extraData2, "result.extraData");
            onFailure(resultCode, companion.getResultMsg(extraData2));
            return;
        }
        if (eventStateType == 1) {
            onSuccess(Unit.f15173a);
            return;
        }
        if (eventStateType != 2) {
            SDKLog.w("AwarenessIntentCallBack: unknown event state type");
            return;
        }
        Bundle extraData3 = deviceEventResult.getExtraData();
        if (extraData3 == null || (intentResult = (IntentResult) extraData3.getParcelable(IntentResult.BUNDLE_KEY_INTENT_RESULT)) == null) {
            return;
        }
        onIntentResultChanged(intentResult);
    }

    public abstract void onIntentResultChanged(IntentResult intentResult);

    public AwarenessIntentCallBack(String str) {
        k.e(str, TriggerEvent.NOTIFICATION_TAG);
        this.tag = str;
    }
}
