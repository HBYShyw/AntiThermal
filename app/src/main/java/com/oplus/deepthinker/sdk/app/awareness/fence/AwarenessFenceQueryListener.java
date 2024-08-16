package com.oplus.deepthinker.sdk.app.awareness.fence;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.DeviceEventResult;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventQueryListener;
import kotlin.Metadata;
import ma.Unit;

/* compiled from: AwarenessFenceQueryListener.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b&\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\r\u0010\u000eJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H&J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006H&J\u0010\u0010\u000b\u001a\u00020\u00042\b\u0010\n\u001a\u0004\u0018\u00010\tJ\u000e\u0010\f\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006¨\u0006\u000f"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFenceQueryListener;", "Lcom/oplus/deepthinker/sdk/app/aidl/eventfountain/EventQueryListener;", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFenceState;", "fenceState", "Lma/f0;", "onQueryFenceSuccess", "", "errorCode", "onQueryFenceFailure", "Lcom/oplus/deepthinker/sdk/app/aidl/eventfountain/DeviceEventResult;", "result", "onSuccess", "onFailure", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public abstract class AwarenessFenceQueryListener extends EventQueryListener {
    @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventQueryListener, com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener
    public final void onFailure(int i10) {
        onQueryFenceFailure(i10);
    }

    public abstract void onQueryFenceFailure(int i10);

    public abstract void onQueryFenceSuccess(AwarenessFenceState awarenessFenceState);

    @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventQueryListener, com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener
    public final void onSuccess(DeviceEventResult deviceEventResult) {
        Unit unit = null;
        Bundle extraData = deviceEventResult == null ? null : deviceEventResult.getExtraData();
        AwarenessFenceState awarenessFenceState = extraData == null ? null : (AwarenessFenceState) extraData.getParcelable(AwarenessFenceState.BUNDLE_KEY_FENCE_STATE);
        if (awarenessFenceState != null) {
            onQueryFenceSuccess(awarenessFenceState);
            unit = Unit.f15173a;
        }
        if (unit == null) {
            onQueryFenceFailure(32);
        }
    }
}
