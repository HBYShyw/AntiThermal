package i6;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.Event;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventConfig;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener;
import com.oplus.deepthinker.sdk.app.aidl.proton.appactionpredict.PredictAABResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.appactionpredict.PredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.DeepSleepPredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.SleepRecord;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.TotalPredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.IntentResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.ServiceResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.periodtopapps.PeriodTopAppsResult;
import com.oplus.deepthinker.sdk.app.awareness.AwarenessStatusCodes;
import com.oplus.statistics.DataTypeConstants;
import java.util.List;
import java.util.Map;

/* compiled from: IDeepThinkerBridge.java */
/* renamed from: i6.a, reason: use source file name */
/* loaded from: classes.dex */
public interface IDeepThinkerBridge extends IInterface {

    /* compiled from: IDeepThinkerBridge.java */
    /* renamed from: i6.a$a */
    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements IDeepThinkerBridge {

        /* JADX INFO: Access modifiers changed from: private */
        /* compiled from: IDeepThinkerBridge.java */
        /* renamed from: i6.a$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        public static class C0052a implements IDeepThinkerBridge {

            /* renamed from: b, reason: collision with root package name */
            public static IDeepThinkerBridge f12651b;

            /* renamed from: a, reason: collision with root package name */
            private IBinder f12652a;

            C0052a(IBinder iBinder) {
                this.f12652a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f12652a;
            }

            @Override // i6.IDeepThinkerBridge
            public int availableState(int i10, String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeInt(i10);
                    obtain.writeString(str);
                    if (!this.f12652a.transact(4, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().availableState(i10, str);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public Bundle call(String str, String str2, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.f12652a.transact(7, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().call(str, str2, bundle);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public List capability() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (!this.f12652a.transact(5, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().capability();
                    }
                    obtain2.readException();
                    return obtain2.readArrayList(getClass().getClassLoader());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public Map checkPermission(int i10, String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeInt(i10);
                    obtain.writeString(str);
                    if (!this.f12652a.transact(9, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().checkPermission(i10, str);
                    }
                    obtain2.readException();
                    return obtain2.readHashMap(getClass().getClassLoader());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public List<PeriodTopAppsResult> getAllPeriodDurationTopApps(int i10) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeInt(i10);
                    if (!this.f12652a.transact(DataTypeConstants.DYNAMIC_EVENT_TYPE, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getAllPeriodDurationTopApps(i10);
                    }
                    obtain2.readException();
                    return obtain2.createTypedArrayList(PeriodTopAppsResult.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public List<PeriodTopAppsResult> getAllPeriodFrequencyTopApps(int i10) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeInt(i10);
                    if (!this.f12652a.transact(DataTypeConstants.COMMON, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getAllPeriodFrequencyTopApps(i10);
                    }
                    obtain2.readException();
                    return obtain2.createTypedArrayList(PeriodTopAppsResult.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public PredictResult getAppPredictResult(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeString(str);
                    if (!this.f12652a.transact(DataTypeConstants.COMMON_BATCH, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getAppPredictResult(str);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? PredictResult.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public List<PredictResult> getAppPredictResultMap(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeString(str);
                    if (!this.f12652a.transact(DataTypeConstants.DEBUG_TYPE, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getAppPredictResultMap(str);
                    }
                    obtain2.readException();
                    return obtain2.createTypedArrayList(PredictResult.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public List<String> getAppQueueSortedByComplex() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (!this.f12652a.transact(DataTypeConstants.PAGE_VISIT, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getAppQueueSortedByComplex();
                    }
                    obtain2.readException();
                    return obtain2.createStringArrayList();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public List<String> getAppQueueSortedByCount() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (!this.f12652a.transact(DataTypeConstants.APP_LOG, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getAppQueueSortedByCount();
                    }
                    obtain2.readException();
                    return obtain2.createStringArrayList();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public List<String> getAppQueueSortedByTime() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (!this.f12652a.transact(DataTypeConstants.USER_ACTION, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getAppQueueSortedByTime();
                    }
                    obtain2.readException();
                    return obtain2.createStringArrayList();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public int getAppType(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeString(str);
                    if (!this.f12652a.transact(1011, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getAppType(str);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public Map getAppTypeMap(List<String> list) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeStringList(list);
                    if (!this.f12652a.transact(1012, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getAppTypeMap(list);
                    }
                    obtain2.readException();
                    return obtain2.readHashMap(getClass().getClassLoader());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public PeriodTopAppsResult getCertainPeriodDurationTopApps(float f10, int i10) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeFloat(f10);
                    obtain.writeInt(i10);
                    if (!this.f12652a.transact(DataTypeConstants.SPECIAL_APP_START, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getCertainPeriodDurationTopApps(f10, i10);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? PeriodTopAppsResult.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public PeriodTopAppsResult getCertainPeriodFrequencyTopApps(float f10, int i10) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeFloat(f10);
                    obtain.writeInt(i10);
                    if (!this.f12652a.transact(DataTypeConstants.EXCEPTION, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getCertainPeriodFrequencyTopApps(f10, i10);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? PeriodTopAppsResult.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public DeepSleepPredictResult getDeepSleepPredictResult() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (!this.f12652a.transact(3001, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getDeepSleepPredictResult();
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? DeepSleepPredictResult.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public String getDeepSleepPredictResultWithPercentile() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (!this.f12652a.transact(3008, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getDeepSleepPredictResultWithPercentile();
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public TotalPredictResult getDeepSleepTotalPredictResult() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (!this.f12652a.transact(3003, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getDeepSleepTotalPredictResult();
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? TotalPredictResult.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public int getIdleScreenResultInLongTime() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (!this.f12652a.transact(3007, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getIdleScreenResultInLongTime();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public int getIdleScreenResultInMiddleTime() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (!this.f12652a.transact(3006, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getIdleScreenResultInMiddleTime();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public int getIdleScreenResultInShortTime() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (!this.f12652a.transact(3005, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getIdleScreenResultInShortTime();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public SleepRecord getLastDeepSleepRecord() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (!this.f12652a.transact(3002, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getLastDeepSleepRecord();
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? SleepRecord.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public int getPlatformVersion() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (!this.f12652a.transact(2, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getPlatformVersion();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public PredictAABResult getPredictAABResult() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (!this.f12652a.transact(DataTypeConstants.STATIC_EVENT_TYPE, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getPredictAABResult();
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? PredictAABResult.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public DeepSleepPredictResult getPredictResultWithFeedBack() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (!this.f12652a.transact(3004, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getPredictResultWithFeedBack();
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? DeepSleepPredictResult.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public List<String> getSmartGpsBssidList() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (!this.f12652a.transact(4001, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().getSmartGpsBssidList();
                    }
                    obtain2.readException();
                    return obtain2.createStringArrayList();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public void onewayCall(String str, String str2, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.f12652a.transact(10, obtain, null, 1) || a.A() == null) {
                        return;
                    }
                    a.A().onewayCall(str, str2, bundle);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public IntentResult queryAwarenessIntent(int i10) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeInt(i10);
                    if (!this.f12652a.transact(AwarenessStatusCodes.CAPABILITY_NOT_AVAILABLE, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().queryAwarenessIntent(i10);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? IntentResult.INSTANCE.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public ServiceResult queryAwarenessService(int i10) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeInt(i10);
                    if (!this.f12652a.transact(AwarenessStatusCodes.CAPABILITY_NOT_SUBSCRIBED, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().queryAwarenessService(i10);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? ServiceResult.INSTANCE.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public void queryEvent(Event event, IEventQueryListener iEventQueryListener) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (event != null) {
                        obtain.writeInt(1);
                        event.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iEventQueryListener != null ? iEventQueryListener.asBinder() : null);
                    if (this.f12652a.transact(2004, obtain, null, 1) || a.A() == null) {
                        return;
                    }
                    a.A().queryEvent(event, iEventQueryListener);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public void queryEvents(EventConfig eventConfig, IEventQueryListener iEventQueryListener) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    if (eventConfig != null) {
                        obtain.writeInt(1);
                        eventConfig.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iEventQueryListener != null ? iEventQueryListener.asBinder() : null);
                    if (this.f12652a.transact(2005, obtain, null, 1) || a.A() == null) {
                        return;
                    }
                    a.A().queryEvents(eventConfig, iEventQueryListener);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public int registerEventCallback(String str, IEventCallback iEventCallback, EventConfig eventConfig) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iEventCallback != null ? iEventCallback.asBinder() : null);
                    if (eventConfig != null) {
                        obtain.writeInt(1);
                        eventConfig.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.f12652a.transact(2001, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().registerEventCallback(str, iEventCallback, eventConfig);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public void requestGrantPermission(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeString(str);
                    if (!this.f12652a.transact(6, obtain, obtain2, 0) && a.A() != null) {
                        a.A().requestGrantPermission(str);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public IntentResult sortAwarenessIntent(IntentResult intentResult, boolean z10) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    int i10 = 1;
                    if (intentResult != null) {
                        obtain.writeInt(1);
                        intentResult.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!z10) {
                        i10 = 0;
                    }
                    obtain.writeInt(i10);
                    if (!this.f12652a.transact(AwarenessStatusCodes.CAPABILITY_NOT_REGISTERED, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().sortAwarenessIntent(intentResult, z10);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? IntentResult.INSTANCE.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public ServiceResult sortAwarenessService(ServiceResult serviceResult, boolean z10) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    int i10 = 1;
                    if (serviceResult != null) {
                        obtain.writeInt(1);
                        serviceResult.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!z10) {
                        i10 = 0;
                    }
                    obtain.writeInt(i10);
                    if (!this.f12652a.transact(AwarenessStatusCodes.CAPABILITY_REGISTERED_REPEAT, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().sortAwarenessService(serviceResult, z10);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? ServiceResult.INSTANCE.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public int unregisterEventCallback(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeString(str);
                    if (!this.f12652a.transact(2002, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().unregisterEventCallback(str);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // i6.IDeepThinkerBridge
            public int unregisterEventCallbackWithArgs(String str, EventConfig eventConfig) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
                    obtain.writeString(str);
                    if (eventConfig != null) {
                        obtain.writeInt(1);
                        eventConfig.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.f12652a.transact(2003, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().unregisterEventCallbackWithArgs(str, eventConfig);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static IDeepThinkerBridge A() {
            return C0052a.f12651b;
        }

        public static IDeepThinkerBridge z(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.oplus.deepthinker.platform.server.IDeepThinkerBridge");
            if (queryLocalInterface != null && (queryLocalInterface instanceof IDeepThinkerBridge)) {
                return (IDeepThinkerBridge) queryLocalInterface;
            }
            return new C0052a(iBinder);
        }
    }

    int availableState(int i10, String str);

    Bundle call(String str, String str2, Bundle bundle);

    List capability();

    Map checkPermission(int i10, String str);

    List<PeriodTopAppsResult> getAllPeriodDurationTopApps(int i10);

    List<PeriodTopAppsResult> getAllPeriodFrequencyTopApps(int i10);

    PredictResult getAppPredictResult(String str);

    List<PredictResult> getAppPredictResultMap(String str);

    List<String> getAppQueueSortedByComplex();

    List<String> getAppQueueSortedByCount();

    List<String> getAppQueueSortedByTime();

    int getAppType(String str);

    Map getAppTypeMap(List<String> list);

    PeriodTopAppsResult getCertainPeriodDurationTopApps(float f10, int i10);

    PeriodTopAppsResult getCertainPeriodFrequencyTopApps(float f10, int i10);

    DeepSleepPredictResult getDeepSleepPredictResult();

    String getDeepSleepPredictResultWithPercentile();

    TotalPredictResult getDeepSleepTotalPredictResult();

    int getIdleScreenResultInLongTime();

    int getIdleScreenResultInMiddleTime();

    int getIdleScreenResultInShortTime();

    SleepRecord getLastDeepSleepRecord();

    int getPlatformVersion();

    PredictAABResult getPredictAABResult();

    DeepSleepPredictResult getPredictResultWithFeedBack();

    List<String> getSmartGpsBssidList();

    void onewayCall(String str, String str2, Bundle bundle);

    IntentResult queryAwarenessIntent(int i10);

    ServiceResult queryAwarenessService(int i10);

    void queryEvent(Event event, IEventQueryListener iEventQueryListener);

    void queryEvents(EventConfig eventConfig, IEventQueryListener iEventQueryListener);

    int registerEventCallback(String str, IEventCallback iEventCallback, EventConfig eventConfig);

    void requestGrantPermission(String str);

    IntentResult sortAwarenessIntent(IntentResult intentResult, boolean z10);

    ServiceResult sortAwarenessService(ServiceResult serviceResult, boolean z10);

    int unregisterEventCallback(String str);

    int unregisterEventCallbackWithArgs(String str, EventConfig eventConfig);
}
