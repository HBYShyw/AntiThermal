package com.oplus.deepthinker.sdk.app.api;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.DeviceEventResult;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.thermalcontrol.ThermalControlConfig;
import i6.IDeepThinkerBridge;
import java.util.Objects;
import kotlin.Metadata;
import ma.Unit;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: InternalApiCall.kt */
@Metadata(bv = {}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\b\u0000\u0018\u0000 \"2\u00020\u0001:\u0001\"B\u0007¢\u0006\u0004\b \u0010!J\b\u0010\u0003\u001a\u00020\u0002H\u0002J\u000e\u0010\u0006\u001a\u00020\u00002\u0006\u0010\u0005\u001a\u00020\u0004J\u0010\u0010\u0006\u001a\u00020\u00002\b\u0010\u0005\u001a\u0004\u0018\u00010\u0001J\u000e\u0010\u0006\u001a\u00020\u00002\u0006\u0010\u0005\u001a\u00020\u0007J\u0016\u0010\f\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\nJ\u0016\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u000fJ\u0014\u0010\u0014\u001a\u00020\u00002\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00020\u0012J$\u0010\u001a\u001a\u00020\u00192\b\b\u0002\u0010\u0015\u001a\u00020\b2\b\b\u0002\u0010\u0016\u001a\u00020\b2\b\b\u0002\u0010\u0018\u001a\u00020\u0017R\u0018\u0010\u001b\u001a\u0004\u0018\u00010\b8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001b\u0010\u001cR\u0018\u0010\u000b\u001a\u0004\u0018\u00010\n8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u000b\u0010\u001dR\u0018\u0010\u000e\u001a\u0004\u0018\u00010\r8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u000e\u0010\u001eR\u0018\u0010\u0010\u001a\u0004\u0018\u00010\u000f8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0010\u0010\u001f¨\u0006#"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/api/InternalApiCall;", "", "Landroid/os/Bundle;", "buildParams", "Landroid/os/IBinder;", "remote", "setRemote", "Li6/a;", "", TriggerEvent.NOTIFICATION_TAG, "Lcom/oplus/deepthinker/sdk/app/aidl/eventfountain/IEventCallback$Stub;", "callback", "setApiCallback", "Landroid/content/Context;", "context", "Landroid/app/PendingIntent;", "pendingIntent", "setPendingIntent", "Lkotlin/Function0;", "paramsBuilder", "setParamsBuilder", ThermalControlConfig.CONFIG_TYPE_FEATURE, InternalApiCall.FUNCTION, "", "oneway", "Lma/f0;", "apiCall", "callbackTag", "Ljava/lang/String;", "Lcom/oplus/deepthinker/sdk/app/aidl/eventfountain/IEventCallback$Stub;", "Landroid/content/Context;", "Landroid/app/PendingIntent;", "<init>", "()V", "Companion", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class InternalApiCall {
    public static final String API_CODE = "api_code";
    public static final String CALLBACK = "cb";
    public static final String CALLBACK_TAG = "cb_tag";

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final String EVENT_TYPE = "event_type";
    public static final String FEATURE_API_REQUEST = "api";
    public static final String FUNCTION = "func";
    public static final String FUNCTION_DESCR = "func_descr";
    public static final int FUNCTION_REGISTER = 101;
    public static final int FUNCTION_UNREGISTER = 102;
    public static final String PENDING_INTENT = "pdintent";
    public static final String PID_CALLBACK_TAG = "cb_ptag";
    public static final String RESULT_CODE = "rep_code";
    public static final String RESULT_FUNC = "rep_func";
    public static final int RESULT_FUNC_EVENT = 2;
    public static final int RESULT_FUNC_FAILURE = 0;
    public static final int RESULT_FUNC_SUCCESS = 1;
    public static final String RESULT_MSG = "rep_msg";
    public static final String TAG = "InternalApiCall";
    public static final String VERSION = "v1";
    private IEventCallback.Stub callback;
    private String callbackTag;
    private Context context;
    private ya.a<Bundle> params;
    private PendingIntent pendingIntent;
    private IDeepThinkerBridge remote;

    /* compiled from: InternalApiCall.kt */
    @Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b*\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b1\u00102J\u0014\u0010\u0006\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0003H\u0007J\u0014\u0010\b\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0003H\u0007J\u0014\u0010\u000b\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\n\u001a\u00020\tH\u0007J\u0014\u0010\r\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0003H\u0007J\u0014\u0010\u000f\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u0003H\u0007J\u0014\u0010\u0011\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\tH\u0007J\f\u0010\u0012\u001a\u00020\u0005*\u00020\u0002H\u0007J\f\u0010\u0013\u001a\u00020\u0003*\u00020\u0002H\u0007J\f\u0010\u0014\u001a\u00020\u0003*\u00020\u0002H\u0007J\u000e\u0010\u0015\u001a\u0004\u0018\u00010\t*\u00020\u0002H\u0007J\f\u0010\u0016\u001a\u00020\u0003*\u00020\u0002H\u0007J\u000e\u0010\u0017\u001a\u0004\u0018\u00010\t*\u00020\u0002H\u0007J\u000e\u0010\u0018\u001a\u0004\u0018\u00010\t*\u00020\u0002H\u0007J\f\u0010\u0019\u001a\u00020\u0002*\u00020\u0002H\u0007J\f\u0010\u001a\u001a\u00020\u0003*\u00020\u0002H\u0007J\f\u0010\u001b\u001a\u00020\u0003*\u00020\u0002H\u0007R\u0014\u0010\u001c\u001a\u00020\t8\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u001c\u0010\u001dR\u0014\u0010\u001e\u001a\u00020\t8\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u001e\u0010\u001dR\u0014\u0010\u001f\u001a\u00020\t8\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u001f\u0010\u001dR\u0014\u0010 \u001a\u00020\t8\u0006X\u0086T¢\u0006\u0006\n\u0004\b \u0010\u001dR\u0014\u0010!\u001a\u00020\t8\u0006X\u0086T¢\u0006\u0006\n\u0004\b!\u0010\u001dR\u0014\u0010\"\u001a\u00020\t8\u0006X\u0086T¢\u0006\u0006\n\u0004\b\"\u0010\u001dR\u0014\u0010#\u001a\u00020\t8\u0006X\u0086T¢\u0006\u0006\n\u0004\b#\u0010\u001dR\u0014\u0010$\u001a\u00020\u00038\u0006X\u0086T¢\u0006\u0006\n\u0004\b$\u0010%R\u0014\u0010&\u001a\u00020\u00038\u0006X\u0086T¢\u0006\u0006\n\u0004\b&\u0010%R\u0014\u0010'\u001a\u00020\t8\u0006X\u0086T¢\u0006\u0006\n\u0004\b'\u0010\u001dR\u0014\u0010(\u001a\u00020\t8\u0006X\u0086T¢\u0006\u0006\n\u0004\b(\u0010\u001dR\u0014\u0010)\u001a\u00020\t8\u0006X\u0086T¢\u0006\u0006\n\u0004\b)\u0010\u001dR\u0014\u0010*\u001a\u00020\t8\u0006X\u0086T¢\u0006\u0006\n\u0004\b*\u0010\u001dR\u0014\u0010+\u001a\u00020\u00038\u0006X\u0086T¢\u0006\u0006\n\u0004\b+\u0010%R\u0014\u0010,\u001a\u00020\u00038\u0006X\u0086T¢\u0006\u0006\n\u0004\b,\u0010%R\u0014\u0010-\u001a\u00020\u00038\u0006X\u0086T¢\u0006\u0006\n\u0004\b-\u0010%R\u0014\u0010.\u001a\u00020\t8\u0006X\u0086T¢\u0006\u0006\n\u0004\b.\u0010\u001dR\u0014\u0010/\u001a\u00020\t8\u0006X\u0086T¢\u0006\u0006\n\u0004\b/\u0010\u001dR\u0014\u00100\u001a\u00020\t8\u0006X\u0086T¢\u0006\u0006\n\u0004\b0\u0010\u001d¨\u00063"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/api/InternalApiCall$Companion;", "", "Landroid/os/Bundle;", "", "code", "Lma/f0;", "putApiCode", "function", "putFunction", "", "descr", "putFunctionDescription", "resultCode", "putResultCode", "eventType", "putEventType", "pidCallbackTag", "putCallbackTagByPid", "autofill", "getApiCode", "getFunction", "getFunctionDescription", "getResultCode", "getResultMsg", "getCallbackTag", "callOnFailure", "getResultFunction", "getEventType", "API_CODE", "Ljava/lang/String;", "CALLBACK", "CALLBACK_TAG", "EVENT_TYPE", "FEATURE_API_REQUEST", "FUNCTION", "FUNCTION_DESCR", "FUNCTION_REGISTER", "I", "FUNCTION_UNREGISTER", "PENDING_INTENT", "PID_CALLBACK_TAG", "RESULT_CODE", "RESULT_FUNC", "RESULT_FUNC_EVENT", "RESULT_FUNC_FAILURE", "RESULT_FUNC_SUCCESS", "RESULT_MSG", "TAG", "VERSION", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void autofill(Bundle bundle) {
            k.e(bundle, "<this>");
            int myPid = Process.myPid();
            StringBuilder sb2 = new StringBuilder();
            sb2.append((Object) bundle.getString(InternalApiCall.CALLBACK_TAG));
            sb2.append('_');
            sb2.append(myPid);
            putCallbackTagByPid(bundle, sb2.toString());
        }

        public final Bundle callOnFailure(Bundle bundle) {
            k.e(bundle, "<this>");
            bundle.putInt(InternalApiCall.RESULT_FUNC, 0);
            return bundle;
        }

        public final int getApiCode(Bundle bundle) {
            k.e(bundle, "<this>");
            return bundle.getInt(InternalApiCall.API_CODE);
        }

        public final String getCallbackTag(Bundle bundle) {
            k.e(bundle, "<this>");
            return bundle.getString(InternalApiCall.CALLBACK_TAG);
        }

        public final int getEventType(Bundle bundle) {
            k.e(bundle, "<this>");
            return bundle.getInt("event_type");
        }

        public final int getFunction(Bundle bundle) {
            k.e(bundle, "<this>");
            return bundle.getInt(InternalApiCall.FUNCTION);
        }

        public final String getFunctionDescription(Bundle bundle) {
            k.e(bundle, "<this>");
            return bundle.getString(InternalApiCall.FUNCTION_DESCR);
        }

        public final int getResultCode(Bundle bundle) {
            k.e(bundle, "<this>");
            return bundle.getInt(InternalApiCall.RESULT_CODE);
        }

        public final int getResultFunction(Bundle bundle) {
            k.e(bundle, "<this>");
            return bundle.getInt(InternalApiCall.RESULT_FUNC);
        }

        public final String getResultMsg(Bundle bundle) {
            k.e(bundle, "<this>");
            return bundle.getString(InternalApiCall.RESULT_MSG);
        }

        public final void putApiCode(Bundle bundle, int i10) {
            k.e(bundle, "<this>");
            bundle.putInt(InternalApiCall.API_CODE, i10);
        }

        public final void putCallbackTagByPid(Bundle bundle, String str) {
            k.e(bundle, "<this>");
            k.e(str, "pidCallbackTag");
            bundle.putString(InternalApiCall.PID_CALLBACK_TAG, str);
        }

        public final void putEventType(Bundle bundle, int i10) {
            k.e(bundle, "<this>");
            bundle.putInt("event_type", i10);
        }

        public final void putFunction(Bundle bundle, int i10) {
            k.e(bundle, "<this>");
            bundle.putInt(InternalApiCall.FUNCTION, i10);
        }

        public final void putFunctionDescription(Bundle bundle, String str) {
            k.e(bundle, "<this>");
            k.e(str, "descr");
            bundle.putString(InternalApiCall.FUNCTION_DESCR, str);
        }

        public final void putResultCode(Bundle bundle, int i10) {
            k.e(bundle, "<this>");
            bundle.putInt(InternalApiCall.RESULT_CODE, i10);
        }
    }

    public static /* synthetic */ void apiCall$default(InternalApiCall internalApiCall, String str, String str2, boolean z10, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            str = FEATURE_API_REQUEST;
        }
        if ((i10 & 2) != 0) {
            str2 = VERSION;
        }
        if ((i10 & 4) != 0) {
            z10 = false;
        }
        internalApiCall.apiCall(str, str2, z10);
    }

    public static final void autofill(Bundle bundle) {
        INSTANCE.autofill(bundle);
    }

    private final Bundle buildParams() {
        ya.a<Bundle> aVar = this.params;
        Bundle invoke = aVar == null ? null : aVar.invoke();
        if (invoke != null) {
            String str = this.callbackTag;
            if (str != null) {
                invoke.putString(CALLBACK_TAG, str);
            }
            IEventCallback.Stub stub = this.callback;
            if (stub != null) {
                invoke.putBinder(CALLBACK, stub);
            }
            PendingIntent pendingIntent = this.pendingIntent;
            if (pendingIntent != null) {
                invoke.putParcelable(PENDING_INTENT, pendingIntent);
            }
            INSTANCE.autofill(invoke);
            return invoke;
        }
        throw new IllegalArgumentException("Build Api Request failed!");
    }

    public static final Bundle callOnFailure(Bundle bundle) {
        return INSTANCE.callOnFailure(bundle);
    }

    public static final int getApiCode(Bundle bundle) {
        return INSTANCE.getApiCode(bundle);
    }

    public static final String getCallbackTag(Bundle bundle) {
        return INSTANCE.getCallbackTag(bundle);
    }

    public static final int getEventType(Bundle bundle) {
        return INSTANCE.getEventType(bundle);
    }

    public static final int getFunction(Bundle bundle) {
        return INSTANCE.getFunction(bundle);
    }

    public static final String getFunctionDescription(Bundle bundle) {
        return INSTANCE.getFunctionDescription(bundle);
    }

    public static final int getResultCode(Bundle bundle) {
        return INSTANCE.getResultCode(bundle);
    }

    public static final int getResultFunction(Bundle bundle) {
        return INSTANCE.getResultFunction(bundle);
    }

    public static final String getResultMsg(Bundle bundle) {
        return INSTANCE.getResultMsg(bundle);
    }

    public static final void putApiCode(Bundle bundle, int i10) {
        INSTANCE.putApiCode(bundle, i10);
    }

    public static final void putCallbackTagByPid(Bundle bundle, String str) {
        INSTANCE.putCallbackTagByPid(bundle, str);
    }

    public static final void putEventType(Bundle bundle, int i10) {
        INSTANCE.putEventType(bundle, i10);
    }

    public static final void putFunction(Bundle bundle, int i10) {
        INSTANCE.putFunction(bundle, i10);
    }

    public static final void putFunctionDescription(Bundle bundle, String str) {
        INSTANCE.putFunctionDescription(bundle, str);
    }

    public static final void putResultCode(Bundle bundle, int i10) {
        INSTANCE.putResultCode(bundle, i10);
    }

    public final void apiCall(String str, String str2, boolean z10) {
        k.e(str, ThermalControlConfig.CONFIG_TYPE_FEATURE);
        k.e(str2, FUNCTION);
        Bundle buildParams = buildParams();
        if (z10) {
            IDeepThinkerBridge iDeepThinkerBridge = this.remote;
            if (iDeepThinkerBridge == null) {
                return;
            }
            iDeepThinkerBridge.onewayCall(str, str2, buildParams);
            return;
        }
        IDeepThinkerBridge iDeepThinkerBridge2 = this.remote;
        Bundle call = iDeepThinkerBridge2 == null ? null : iDeepThinkerBridge2.call(str, str2, buildParams);
        Companion companion = INSTANCE;
        int apiCode = companion.getApiCode(buildParams);
        if (call == null) {
            call = new Bundle();
            call.putInt(RESULT_FUNC, 0);
        }
        call.setClassLoader(InternalApiCall.class.getClassLoader());
        DeviceEventResult deviceEventResult = new DeviceEventResult(apiCode, companion.getResultFunction(call), 0, null, call);
        IEventCallback.Stub stub = this.callback;
        if (stub != null) {
            stub.onEventStateChanged(deviceEventResult);
        }
        PendingIntent pendingIntent = this.pendingIntent;
        if (pendingIntent == null) {
            return;
        }
        Context context = this.context;
        Intent intent = new Intent();
        intent.putExtras(call);
        Unit unit = Unit.f15173a;
        pendingIntent.send(context, 0, intent);
    }

    public final InternalApiCall setApiCallback(String tag, IEventCallback.Stub callback) {
        k.e(tag, TriggerEvent.NOTIFICATION_TAG);
        k.e(callback, "callback");
        this.callbackTag = tag;
        this.callback = callback;
        return this;
    }

    public final InternalApiCall setParamsBuilder(ya.a<Bundle> aVar) {
        k.e(aVar, "paramsBuilder");
        this.params = aVar;
        return this;
    }

    public final InternalApiCall setPendingIntent(Context context, PendingIntent pendingIntent) {
        k.e(context, "context");
        k.e(pendingIntent, "pendingIntent");
        this.context = context;
        this.pendingIntent = pendingIntent;
        return this;
    }

    public final InternalApiCall setRemote(IBinder remote) {
        k.e(remote, "remote");
        this.remote = (IDeepThinkerBridge) remote;
        return this;
    }

    public final InternalApiCall setRemote(Object remote) {
        Objects.requireNonNull(remote, "null cannot be cast to non-null type com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
        this.remote = (IDeepThinkerBridge) remote;
        return this;
    }

    public final InternalApiCall setRemote(IDeepThinkerBridge remote) {
        k.e(remote, "remote");
        this.remote = remote;
        return this;
    }
}
