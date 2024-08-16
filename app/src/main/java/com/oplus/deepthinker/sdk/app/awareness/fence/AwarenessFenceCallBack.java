package com.oplus.deepthinker.sdk.app.awareness.fence;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.DeviceEventResult;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.deepthinker.sdk.app.api.ApiCallBack;
import com.oplus.deepthinker.sdk.app.api.InternalApiCall;
import kotlin.Metadata;
import ma.Unit;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: AwarenessFenceCallBack.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\b&\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0011\u0012\b\b\u0002\u0010\n\u001a\u00020\t¢\u0006\u0004\b\u000e\u0010\u000fJ\u0010\u0010\u0005\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0003H&J\u0010\u0010\b\u001a\u00020\u00022\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006R\u0017\u0010\n\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\r¨\u0006\u0010"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFenceCallBack;", "Lcom/oplus/deepthinker/sdk/app/api/ApiCallBack;", "Lma/f0;", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFenceState;", "fenceState", "onFenceStateChanged", "Lcom/oplus/deepthinker/sdk/app/aidl/eventfountain/DeviceEventResult;", "result", "onEventStateChanged", "", TriggerEvent.NOTIFICATION_TAG, "Ljava/lang/String;", "getTag", "()Ljava/lang/String;", "<init>", "(Ljava/lang/String;)V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public abstract class AwarenessFenceCallBack extends ApiCallBack<Unit> {
    private final String tag;

    public AwarenessFenceCallBack() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public /* synthetic */ AwarenessFenceCallBack(String str, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((i10 & 1) != 0 ? "awareness_fence" : str);
    }

    public final String getTag() {
        return this.tag;
    }

    @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventCallback, com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback
    public final void onEventStateChanged(DeviceEventResult deviceEventResult) {
        Bundle extraData;
        AwarenessFenceState awarenessFenceState;
        if (deviceEventResult == null || deviceEventResult.getEventType() != 523) {
            return;
        }
        int eventStateType = deviceEventResult.getEventStateType();
        if (eventStateType == 0) {
            InternalApiCall.Companion companion = InternalApiCall.INSTANCE;
            Bundle extraData2 = deviceEventResult.getExtraData();
            k.d(extraData2, "result.extraData");
            int resultCode = companion.getResultCode(extraData2);
            Bundle extraData3 = deviceEventResult.getExtraData();
            k.d(extraData3, "result.extraData");
            onFailure(resultCode, companion.getResultMsg(extraData3));
            return;
        }
        if (eventStateType == 1) {
            onSuccess(Unit.f15173a);
        } else {
            if (eventStateType != 2 || (extraData = deviceEventResult.getExtraData()) == null || (awarenessFenceState = (AwarenessFenceState) extraData.getParcelable(AwarenessFenceState.BUNDLE_KEY_FENCE_STATE)) == null) {
                return;
            }
            onFenceStateChanged(awarenessFenceState);
        }
    }

    public abstract void onFenceStateChanged(AwarenessFenceState awarenessFenceState);

    public AwarenessFenceCallBack(String str) {
        k.e(str, TriggerEvent.NOTIFICATION_TAG);
        this.tag = str;
    }
}
