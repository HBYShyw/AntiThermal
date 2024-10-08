package com.oplus.deepthinker.sdk.app.awareness.intent;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.DeviceEventResult;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventQueryListener;
import com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.IntentResult;
import kotlin.Metadata;
import m6.StatusCodeUtils;
import ma.Unit;

/* compiled from: AwarenessIntentQueryListener.kt */
@Metadata(bv = {}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b&\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u000e\u0010\u000fJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H&J\u0018\u0010\n\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\bH&J\u0010\u0010\f\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u000bJ\u000e\u0010\r\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006¨\u0006\u0010"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/intent/AwarenessIntentQueryListener;", "Lcom/oplus/deepthinker/sdk/app/aidl/eventfountain/EventQueryListener;", "Lcom/oplus/deepthinker/sdk/app/aidl/proton/intentdecision/IntentResult;", "result", "Lma/f0;", "onQueryIntentSuccess", "", "errorCode", "", "errorMessage", "onQueryIntentFailure", "Lcom/oplus/deepthinker/sdk/app/aidl/eventfountain/DeviceEventResult;", "onSuccess", "onFailure", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public abstract class AwarenessIntentQueryListener extends EventQueryListener {
    @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventQueryListener, com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener
    public final void onFailure(int i10) {
        onQueryIntentFailure(i10, StatusCodeUtils.a(i10));
    }

    public abstract void onQueryIntentFailure(int i10, String str);

    public abstract void onQueryIntentSuccess(IntentResult intentResult);

    @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventQueryListener, com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener
    public final void onSuccess(DeviceEventResult deviceEventResult) {
        Unit unit = null;
        Bundle extraData = deviceEventResult == null ? null : deviceEventResult.getExtraData();
        IntentResult intentResult = extraData == null ? null : (IntentResult) extraData.getParcelable(IntentResult.BUNDLE_KEY_INTENT_RESULT);
        if (intentResult != null) {
            onQueryIntentSuccess(intentResult);
            unit = Unit.f15173a;
        }
        if (unit == null) {
            onFailure(32);
        }
    }
}
