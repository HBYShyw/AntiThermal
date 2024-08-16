package com.oplus.deepthinker.sdk.app.feature;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.DeviceEventResult;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.deepthinker.sdk.app.api.InternalApiCall;
import com.oplus.deepthinker.sdk.app.api.TransactionHandle;
import i6.IDeepThinkerBridge;
import java.util.Objects;
import java.util.Set;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: AppSwitchCallback.kt */
@Metadata(bv = {}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\b&\u0018\u0000 \u00132\u00020\u0001:\u0003\u0014\u0015\u0013B\u000f\u0012\u0006\u0010\u000f\u001a\u00020\b¢\u0006\u0004\b\u0011\u0010\u0012J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u001a\u0010\n\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00062\b\u0010\t\u001a\u0004\u0018\u00010\bH\u0016J\u001a\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00062\b\u0010\t\u001a\u0004\u0018\u00010\bH\u0016J\u0010\u0010\u000e\u001a\u00020\u00042\b\u0010\r\u001a\u0004\u0018\u00010\fR\u0014\u0010\u000f\u001a\u00020\b8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u000f\u0010\u0010¨\u0006\u0016"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/feature/AppSwitchCallback;", "Lcom/oplus/deepthinker/sdk/app/aidl/eventfountain/IEventCallback$Stub;", "Lcom/oplus/deepthinker/sdk/app/feature/AppSwitchCallback$AppSwitchEvent;", AppSwitchCallback.EVENT_TYPE, "Lma/f0;", "onAppSwitchEvent", "", "code", "", "msg", "onSuccess", "onFailure", "Lcom/oplus/deepthinker/sdk/app/aidl/eventfountain/DeviceEventResult;", "deviceEventResult", "onEventStateChanged", TriggerEvent.NOTIFICATION_TAG, "Ljava/lang/String;", "<init>", "(Ljava/lang/String;)V", "Companion", "AppSwitchEvent", "AppSwitchEventConfig", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public abstract class AppSwitchCallback extends IEventCallback.Stub {
    private static final String ACTIVITY_FILTER = "activity_filter";
    private static final String APP_FILTER = "app_filter";

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final String DISPATCH_DELAY = "dispatch_delay";
    public static final int EVENT_ACTIVITY_ENTER = 4;
    public static final int EVENT_ACTIVITY_EXIT = 8;
    public static final int EVENT_APP_ENTER = 1;
    public static final int EVENT_APP_EXIT = 2;
    private static final String EVENT_TYPE = "event";
    private static final String FEATURE = "AtomFeature";
    private static final String FIRST_START = "first";
    private static final String IS_RESUMING_FIRST_START = "is_resuming_first_start";
    private static final String IS_RESUMING_MULTI_APP = "is_resuming_multi_app";
    public static final int LISTEN_ALL = 15;
    public static final int LISTEN_EVENT_ACTIVITY = 12;
    public static final int LISTEN_EVENT_APP = 3;
    private static final String MULTI_APP = "multi";
    private static final String RESUMING_ACTIVITY_NAME = "resuming_activity_name";
    private static final String RESUMING_PACKAGE_NAME = "resuming_package_name";
    private static final String TAG = "AppSwitchCallback";
    private static final String TARGET_NAME = "target";
    private static final String TIMESTAMP = "timestamp";
    private static final String WINDOW_MODE = "windowMode";
    private final String tag;

    /* compiled from: AppSwitchCallback.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B7\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u000e\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u0012\u000e\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u0012\b\u0010\b\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\tJ\u0015\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001aH\u0000¢\u0006\u0002\b\u001cJ%\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020\u00032\u0006\u0010!\u001a\u00020\u0006H\u0000¢\u0006\u0002\b\"R\"\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\"\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000b\"\u0004\b\u000f\u0010\rR\u001e\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u0014\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018¨\u0006#"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/feature/AppSwitchCallback$AppSwitchEventConfig;", "", "eventConfig", "", "appFilter", "", "", "activityFilter", "dispatchDelay", "(ILjava/util/Set;Ljava/util/Set;Ljava/lang/Integer;)V", "getActivityFilter", "()Ljava/util/Set;", "setActivityFilter", "(Ljava/util/Set;)V", "getAppFilter", "setAppFilter", "getDispatchDelay", "()Ljava/lang/Integer;", "setDispatchDelay", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "getEventConfig", "()I", "setEventConfig", "(I)V", "extract", "Landroid/os/Bundle;", "bundle", "extract$com_oplus_deepthinker_sdk_release", "match", "", "app", AppSwitchCallback.EVENT_TYPE, AppSwitchCallback.TARGET_NAME, "match$com_oplus_deepthinker_sdk_release", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public static final class AppSwitchEventConfig {
        private Set<String> activityFilter;
        private Set<String> appFilter;
        private Integer dispatchDelay;
        private int eventConfig;

        public AppSwitchEventConfig(int i10, Set<String> set, Set<String> set2, Integer num) {
            this.eventConfig = i10;
            this.appFilter = set;
            this.activityFilter = set2;
            this.dispatchDelay = num;
        }

        public final Bundle extract$com_oplus_deepthinker_sdk_release(Bundle bundle) {
            k.e(bundle, "bundle");
            bundle.putInt(AppSwitchCallback.EVENT_TYPE, getEventConfig());
            Integer dispatchDelay = getDispatchDelay();
            if (dispatchDelay != null) {
                bundle.putInt(AppSwitchCallback.DISPATCH_DELAY, dispatchDelay.intValue());
            }
            Set<String> appFilter = getAppFilter();
            if (appFilter != null) {
                Object[] array = appFilter.toArray(new String[0]);
                Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                bundle.putStringArray(AppSwitchCallback.APP_FILTER, (String[]) array);
            }
            Set<String> activityFilter = getActivityFilter();
            if (activityFilter != null) {
                Object[] array2 = activityFilter.toArray(new String[0]);
                Objects.requireNonNull(array2, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                bundle.putStringArray(AppSwitchCallback.ACTIVITY_FILTER, (String[]) array2);
            }
            return bundle;
        }

        public final Set<String> getActivityFilter() {
            return this.activityFilter;
        }

        public final Set<String> getAppFilter() {
            return this.appFilter;
        }

        public final Integer getDispatchDelay() {
            return this.dispatchDelay;
        }

        public final int getEventConfig() {
            return this.eventConfig;
        }

        public final boolean match$com_oplus_deepthinker_sdk_release(boolean app, int event, String target) {
            Set<String> set;
            k.e(target, AppSwitchCallback.TARGET_NAME);
            if ((event & this.eventConfig) == 0) {
                return false;
            }
            if (app) {
                set = this.appFilter;
            } else {
                set = this.activityFilter;
            }
            if (set == null) {
                return true;
            }
            return set.contains(target);
        }

        public final void setActivityFilter(Set<String> set) {
            this.activityFilter = set;
        }

        public final void setAppFilter(Set<String> set) {
            this.appFilter = set;
        }

        public final void setDispatchDelay(Integer num) {
            this.dispatchDelay = num;
        }

        public final void setEventConfig(int i10) {
            this.eventConfig = i10;
        }
    }

    public AppSwitchCallback(String str) {
        k.e(str, TriggerEvent.NOTIFICATION_TAG);
        this.tag = str;
    }

    public void onAppSwitchEvent(AppSwitchEvent appSwitchEvent) {
        k.e(appSwitchEvent, EVENT_TYPE);
    }

    @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback
    public final void onEventStateChanged(DeviceEventResult deviceEventResult) {
        Bundle extraData;
        if (deviceEventResult == null || deviceEventResult.getEventType() != 3) {
            return;
        }
        int eventStateType = deviceEventResult.getEventStateType();
        if (eventStateType == 0) {
            InternalApiCall.Companion companion = InternalApiCall.INSTANCE;
            Bundle extraData2 = deviceEventResult.getExtraData();
            k.d(extraData2, "deviceEventResult.extraData");
            int resultCode = companion.getResultCode(extraData2);
            Bundle extraData3 = deviceEventResult.getExtraData();
            k.d(extraData3, "deviceEventResult.extraData");
            onFailure(resultCode, companion.getResultMsg(extraData3));
            return;
        }
        if (eventStateType != 1) {
            if (eventStateType == 2 && (extraData = deviceEventResult.getExtraData()) != null) {
                onAppSwitchEvent(new AppSwitchEvent(extraData));
                return;
            }
            return;
        }
        InternalApiCall.Companion companion2 = InternalApiCall.INSTANCE;
        Bundle extraData4 = deviceEventResult.getExtraData();
        k.d(extraData4, "deviceEventResult.extraData");
        int resultCode2 = companion2.getResultCode(extraData4);
        Bundle extraData5 = deviceEventResult.getExtraData();
        k.d(extraData5, "deviceEventResult.extraData");
        onSuccess(resultCode2, companion2.getResultMsg(extraData5));
    }

    public void onFailure(int i10, String str) {
    }

    public void onSuccess(int i10, String str) {
    }

    /* compiled from: AppSwitchCallback.kt */
    @Metadata(bv = {}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0017\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b'\u0010(J\u001e\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006J\u0016\u0010\n\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0006J$\u0010\t\u001a\u00020\b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006J\u001c\u0010\n\u001a\u00020\b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u0006R\u0014\u0010\u000e\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0010\u0010\u000fR\u0014\u0010\u0011\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0011\u0010\u000fR\u0014\u0010\u0013\u001a\u00020\u00128\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\u00020\u00128\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0015\u0010\u0014R\u0014\u0010\u0016\u001a\u00020\u00128\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0016\u0010\u0014R\u0014\u0010\u0017\u001a\u00020\u00128\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0017\u0010\u0014R\u0014\u0010\u0018\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0018\u0010\u000fR\u0014\u0010\u0019\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0019\u0010\u000fR\u0014\u0010\u001a\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u001a\u0010\u000fR\u0014\u0010\u001b\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u001b\u0010\u000fR\u0014\u0010\u001c\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u001c\u0010\u000fR\u0014\u0010\u001d\u001a\u00020\u00128\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u001d\u0010\u0014R\u0014\u0010\u001e\u001a\u00020\u00128\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u001e\u0010\u0014R\u0014\u0010\u001f\u001a\u00020\u00128\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u001f\u0010\u0014R\u0014\u0010 \u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b \u0010\u000fR\u0014\u0010!\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b!\u0010\u000fR\u0014\u0010\"\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\"\u0010\u000fR\u0014\u0010#\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b#\u0010\u000fR\u0014\u0010$\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b$\u0010\u000fR\u0014\u0010%\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b%\u0010\u000fR\u0014\u0010&\u001a\u00020\r8\u0002X\u0082T¢\u0006\u0006\n\u0004\b&\u0010\u000f¨\u0006)"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/feature/AppSwitchCallback$Companion;", "", "Li6/a;", "remote", "Lcom/oplus/deepthinker/sdk/app/feature/AppSwitchCallback$AppSwitchEventConfig;", "config", "Lcom/oplus/deepthinker/sdk/app/feature/AppSwitchCallback;", "callback", "Lma/f0;", "registerAppSwitchCallback", "unregisterAppSwitchCallback", "Lcom/oplus/deepthinker/sdk/app/api/TransactionHandle;", "transactionHandle", "", "ACTIVITY_FILTER", "Ljava/lang/String;", "APP_FILTER", "DISPATCH_DELAY", "", "EVENT_ACTIVITY_ENTER", "I", "EVENT_ACTIVITY_EXIT", "EVENT_APP_ENTER", "EVENT_APP_EXIT", "EVENT_TYPE", "FEATURE", "FIRST_START", "IS_RESUMING_FIRST_START", "IS_RESUMING_MULTI_APP", "LISTEN_ALL", "LISTEN_EVENT_ACTIVITY", "LISTEN_EVENT_APP", "MULTI_APP", "RESUMING_ACTIVITY_NAME", "RESUMING_PACKAGE_NAME", "TAG", "TARGET_NAME", "TIMESTAMP", "WINDOW_MODE", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void registerAppSwitchCallback(IDeepThinkerBridge iDeepThinkerBridge, AppSwitchEventConfig appSwitchEventConfig, AppSwitchCallback appSwitchCallback) {
            k.e(iDeepThinkerBridge, "remote");
            k.e(appSwitchEventConfig, "config");
            k.e(appSwitchCallback, "callback");
            InternalApiCall.apiCall$default(new InternalApiCall().setRemote(iDeepThinkerBridge).setApiCallback(appSwitchCallback.tag, appSwitchCallback).setParamsBuilder(new AppSwitchCallback$Companion$registerAppSwitchCallback$1(appSwitchEventConfig)), AppSwitchCallback.FEATURE, InternalApiCall.VERSION, false, 4, null);
        }

        public final void unregisterAppSwitchCallback(IDeepThinkerBridge iDeepThinkerBridge, AppSwitchCallback appSwitchCallback) {
            k.e(iDeepThinkerBridge, "remote");
            k.e(appSwitchCallback, "callback");
            InternalApiCall.apiCall$default(new InternalApiCall().setRemote(iDeepThinkerBridge).setApiCallback(appSwitchCallback.tag, appSwitchCallback).setParamsBuilder(AppSwitchCallback$Companion$unregisterAppSwitchCallback$1.INSTANCE), AppSwitchCallback.FEATURE, InternalApiCall.VERSION, false, 4, null);
        }

        public final void registerAppSwitchCallback(TransactionHandle<IDeepThinkerBridge> transactionHandle, AppSwitchEventConfig appSwitchEventConfig, AppSwitchCallback appSwitchCallback) {
            k.e(transactionHandle, "transactionHandle");
            k.e(appSwitchEventConfig, "config");
            k.e(appSwitchCallback, "callback");
            transactionHandle.transact$com_oplus_deepthinker_sdk_release(new AppSwitchCallback$Companion$registerAppSwitchCallback$2(appSwitchCallback, appSwitchEventConfig));
        }

        public final void unregisterAppSwitchCallback(TransactionHandle<IDeepThinkerBridge> transactionHandle, AppSwitchCallback appSwitchCallback) {
            k.e(transactionHandle, "transactionHandle");
            k.e(appSwitchCallback, "callback");
            transactionHandle.transact$com_oplus_deepthinker_sdk_release(new AppSwitchCallback$Companion$unregisterAppSwitchCallback$2(appSwitchCallback));
        }
    }

    /* compiled from: AppSwitchCallback.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b#\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B[\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\f\u0012\u0006\u0010\u000e\u001a\u00020\b\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\n\u0012\b\u0010\u0010\u001a\u0004\u0018\u00010\n\u0012\u0006\u0010\u0011\u001a\u00020\f\u0012\u0006\u0010\u0012\u001a\u00020\f¢\u0006\u0002\u0010\u0013J\t\u0010 \u001a\u00020\u0006HÆ\u0003J\t\u0010!\u001a\u00020\fHÆ\u0003J\t\u0010\"\u001a\u00020\bHÆ\u0003J\u000b\u0010#\u001a\u0004\u0018\u00010\nHÆ\u0003J\t\u0010$\u001a\u00020\fHÆ\u0003J\t\u0010%\u001a\u00020\fHÆ\u0003J\t\u0010&\u001a\u00020\bHÆ\u0003J\u000b\u0010'\u001a\u0004\u0018\u00010\nHÆ\u0003J\u000b\u0010(\u001a\u0004\u0018\u00010\nHÆ\u0003J\t\u0010)\u001a\u00020\fHÆ\u0003Js\u0010*\u001a\u00020\u00002\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\f2\b\b\u0002\u0010\u000e\u001a\u00020\b2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u0012\u001a\u00020\fHÆ\u0001J\u0013\u0010+\u001a\u00020\f2\b\u0010,\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010-\u001a\u00020\bHÖ\u0001J\t\u0010.\u001a\u00020\nHÖ\u0001R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u000b\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0012\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0017R\u0011\u0010\u0011\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0017R\u0011\u0010\r\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0017R\u0013\u0010\u0010\u001a\u0004\u0018\u00010\n¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0013\u0010\u000f\u001a\u0004\u0018\u00010\n¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001aR\u0013\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001aR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u000e\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0015¨\u0006/"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/feature/AppSwitchCallback$AppSwitchEvent;", "", "b", "Landroid/os/Bundle;", "(Landroid/os/Bundle;)V", "timeStamp", "", "eventType", "", "targetName", "", "firstStart", "", "multiApp", AppSwitchCallback.WINDOW_MODE, "resumingPackageName", "resumingActivityName", "isResumingMultiApp", "isResumingFirstStart", "(JILjava/lang/String;ZZILjava/lang/String;Ljava/lang/String;ZZ)V", "getEventType", "()I", "getFirstStart", "()Z", "getMultiApp", "getResumingActivityName", "()Ljava/lang/String;", "getResumingPackageName", "getTargetName", "getTimeStamp", "()J", "getWindowMode", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public static final /* data */ class AppSwitchEvent {
        private final int eventType;
        private final boolean firstStart;
        private final boolean isResumingFirstStart;
        private final boolean isResumingMultiApp;
        private final boolean multiApp;
        private final String resumingActivityName;
        private final String resumingPackageName;
        private final String targetName;
        private final long timeStamp;
        private final int windowMode;

        public AppSwitchEvent(long j10, int i10, String str, boolean z10, boolean z11, int i11, String str2, String str3, boolean z12, boolean z13) {
            this.timeStamp = j10;
            this.eventType = i10;
            this.targetName = str;
            this.firstStart = z10;
            this.multiApp = z11;
            this.windowMode = i11;
            this.resumingPackageName = str2;
            this.resumingActivityName = str3;
            this.isResumingMultiApp = z12;
            this.isResumingFirstStart = z13;
        }

        /* renamed from: component1, reason: from getter */
        public final long getTimeStamp() {
            return this.timeStamp;
        }

        /* renamed from: component10, reason: from getter */
        public final boolean getIsResumingFirstStart() {
            return this.isResumingFirstStart;
        }

        /* renamed from: component2, reason: from getter */
        public final int getEventType() {
            return this.eventType;
        }

        /* renamed from: component3, reason: from getter */
        public final String getTargetName() {
            return this.targetName;
        }

        /* renamed from: component4, reason: from getter */
        public final boolean getFirstStart() {
            return this.firstStart;
        }

        /* renamed from: component5, reason: from getter */
        public final boolean getMultiApp() {
            return this.multiApp;
        }

        /* renamed from: component6, reason: from getter */
        public final int getWindowMode() {
            return this.windowMode;
        }

        /* renamed from: component7, reason: from getter */
        public final String getResumingPackageName() {
            return this.resumingPackageName;
        }

        /* renamed from: component8, reason: from getter */
        public final String getResumingActivityName() {
            return this.resumingActivityName;
        }

        /* renamed from: component9, reason: from getter */
        public final boolean getIsResumingMultiApp() {
            return this.isResumingMultiApp;
        }

        public final AppSwitchEvent copy(long timeStamp, int eventType, String targetName, boolean firstStart, boolean multiApp, int windowMode, String resumingPackageName, String resumingActivityName, boolean isResumingMultiApp, boolean isResumingFirstStart) {
            return new AppSwitchEvent(timeStamp, eventType, targetName, firstStart, multiApp, windowMode, resumingPackageName, resumingActivityName, isResumingMultiApp, isResumingFirstStart);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AppSwitchEvent)) {
                return false;
            }
            AppSwitchEvent appSwitchEvent = (AppSwitchEvent) other;
            return this.timeStamp == appSwitchEvent.timeStamp && this.eventType == appSwitchEvent.eventType && k.a(this.targetName, appSwitchEvent.targetName) && this.firstStart == appSwitchEvent.firstStart && this.multiApp == appSwitchEvent.multiApp && this.windowMode == appSwitchEvent.windowMode && k.a(this.resumingPackageName, appSwitchEvent.resumingPackageName) && k.a(this.resumingActivityName, appSwitchEvent.resumingActivityName) && this.isResumingMultiApp == appSwitchEvent.isResumingMultiApp && this.isResumingFirstStart == appSwitchEvent.isResumingFirstStart;
        }

        public final int getEventType() {
            return this.eventType;
        }

        public final boolean getFirstStart() {
            return this.firstStart;
        }

        public final boolean getMultiApp() {
            return this.multiApp;
        }

        public final String getResumingActivityName() {
            return this.resumingActivityName;
        }

        public final String getResumingPackageName() {
            return this.resumingPackageName;
        }

        public final String getTargetName() {
            return this.targetName;
        }

        public final long getTimeStamp() {
            return this.timeStamp;
        }

        public final int getWindowMode() {
            return this.windowMode;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public int hashCode() {
            int hashCode = ((Long.hashCode(this.timeStamp) * 31) + Integer.hashCode(this.eventType)) * 31;
            String str = this.targetName;
            int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
            boolean z10 = this.firstStart;
            int i10 = z10;
            if (z10 != 0) {
                i10 = 1;
            }
            int i11 = (hashCode2 + i10) * 31;
            boolean z11 = this.multiApp;
            int i12 = z11;
            if (z11 != 0) {
                i12 = 1;
            }
            int hashCode3 = (((i11 + i12) * 31) + Integer.hashCode(this.windowMode)) * 31;
            String str2 = this.resumingPackageName;
            int hashCode4 = (hashCode3 + (str2 == null ? 0 : str2.hashCode())) * 31;
            String str3 = this.resumingActivityName;
            int hashCode5 = (hashCode4 + (str3 != null ? str3.hashCode() : 0)) * 31;
            boolean z12 = this.isResumingMultiApp;
            int i13 = z12;
            if (z12 != 0) {
                i13 = 1;
            }
            int i14 = (hashCode5 + i13) * 31;
            boolean z13 = this.isResumingFirstStart;
            return i14 + (z13 ? 1 : z13 ? 1 : 0);
        }

        public final boolean isResumingFirstStart() {
            return this.isResumingFirstStart;
        }

        public final boolean isResumingMultiApp() {
            return this.isResumingMultiApp;
        }

        public String toString() {
            return "AppSwitchEvent(timeStamp=" + this.timeStamp + ", eventType=" + this.eventType + ", targetName=" + ((Object) this.targetName) + ", firstStart=" + this.firstStart + ", multiApp=" + this.multiApp + ", windowMode=" + this.windowMode + ", resumingPackageName=" + ((Object) this.resumingPackageName) + ", resumingActivityName=" + ((Object) this.resumingActivityName) + ", isResumingMultiApp=" + this.isResumingMultiApp + ", isResumingFirstStart=" + this.isResumingFirstStart + ')';
        }

        /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
        public AppSwitchEvent(Bundle bundle) {
            this(bundle.getLong(AppSwitchCallback.TIMESTAMP), bundle.getInt(AppSwitchCallback.EVENT_TYPE), bundle.getString(AppSwitchCallback.TARGET_NAME), bundle.getBoolean(AppSwitchCallback.FIRST_START), bundle.getBoolean(AppSwitchCallback.MULTI_APP), bundle.getInt(AppSwitchCallback.WINDOW_MODE), bundle.getString(AppSwitchCallback.RESUMING_PACKAGE_NAME), bundle.getString(AppSwitchCallback.RESUMING_ACTIVITY_NAME), bundle.getBoolean(AppSwitchCallback.IS_RESUMING_MULTI_APP), bundle.getBoolean(AppSwitchCallback.IS_RESUMING_FIRST_START));
            k.e(bundle, "b");
        }
    }
}
