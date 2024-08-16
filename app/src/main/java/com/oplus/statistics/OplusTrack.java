package com.oplus.statistics;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.oplus.statistics.agent.AtomAgent;
import com.oplus.statistics.agent.CommonAgent;
import com.oplus.statistics.agent.DebugAgent;
import com.oplus.statistics.agent.OnEventAgent;
import com.oplus.statistics.agent.PageVisitAgent;
import com.oplus.statistics.agent.StaticPeriodDataRecord;
import com.oplus.statistics.data.CommonBatchBean;
import com.oplus.statistics.data.CommonBean;
import com.oplus.statistics.data.PeriodDataBean;
import com.oplus.statistics.data.SettingKeyBean;
import com.oplus.statistics.data.SettingKeyDataBean;
import com.oplus.statistics.record.AppLifecycleCallbacks;
import com.oplus.statistics.storage.PreferenceHandler;
import com.oplus.statistics.strategy.ChattyEventTracker;
import com.oplus.statistics.strategy.RequestFireWall;
import com.oplus.statistics.strategy.WorkThread;
import com.oplus.statistics.util.ApkInfoUtil;
import com.oplus.statistics.util.LogUtil;
import com.oplus.statistics.util.Supplier;
import com.oplus.statistics.util.VersionUtil;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class OplusTrack {
    private static final String CLIENT_START = "ClientStart";
    private static final int FIREWALL_LIMIT = 120;
    public static final int FLAG_SEND_TO_ATOM = 2;
    public static final int FLAG_SEND_TO_DCS = 1;
    private static final int MAX_EVENT_COUNT = 10000;
    private static final int MIN_EVENT_COUNT = 1;
    private static final String TAG = "OplusTrack";
    private static volatile StatisticsExceptionHandler sExceptionHandler;
    private static final Pattern EVENTID_PATTERN = Pattern.compile("^[a-zA-Z0-9\\_\\-]{1,64}$");
    private static final PageVisitAgent sPageVisitAgent = new PageVisitAgent();
    private static final long FIREWALL_LIMIT_TIME = 120000;
    private static final RequestFireWall sFireWall = new RequestFireWall.Builder(120, FIREWALL_LIMIT_TIME).build();

    private static boolean formatCheck(String str, String str2, int i10) {
        if (str == null) {
            LogUtil.e(TAG, new Supplier() { // from class: com.oplus.statistics.s
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$formatCheck$27;
                    lambda$formatCheck$27 = OplusTrack.lambda$formatCheck$27();
                    return lambda$formatCheck$27;
                }
            });
            return false;
        }
        if (!EVENTID_PATTERN.matcher(str).find()) {
            LogUtil.e(TAG, new Supplier() { // from class: com.oplus.statistics.l
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$formatCheck$28;
                    lambda$formatCheck$28 = OplusTrack.lambda$formatCheck$28();
                    return lambda$formatCheck$28;
                }
            });
            return false;
        }
        if (str2 == null) {
            LogUtil.e(TAG, new Supplier() { // from class: com.oplus.statistics.p
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$formatCheck$29;
                    lambda$formatCheck$29 = OplusTrack.lambda$formatCheck$29();
                    return lambda$formatCheck$29;
                }
            });
            return false;
        }
        if (i10 <= 10000 && i10 >= 1) {
            return true;
        }
        LogUtil.e(TAG, new Supplier() { // from class: com.oplus.statistics.v
            @Override // com.oplus.statistics.util.Supplier
            public final Object get() {
                String lambda$formatCheck$30;
                lambda$formatCheck$30 = OplusTrack.lambda$formatCheck$30();
                return lambda$formatCheck$30;
            }
        });
        return false;
    }

    public static void init(Context context) {
        init(context, null);
    }

    public static boolean isSupportStaticData(Context context) {
        return VersionUtil.isSupportPeriodData(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$formatCheck$27() {
        return "EventID is null!";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$formatCheck$28() {
        return "EventID format error!";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$formatCheck$29() {
        return "EventTag format error!";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$formatCheck$30() {
        return "EventCount format error!";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$init$0() {
        return "AppCode is empty.";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onCommon$1(CommonBean commonBean, int i10) {
        return "onCommon logTag is " + commonBean.getLogTag() + ",eventID:" + commonBean.getEventID() + ",flagSendTo:" + i10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onCommon$2(CommonBean commonBean) {
        CommonAgent.recordCommon(commonBean.getContext(), commonBean);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onCommon$3(CommonBean commonBean) {
        AtomAgent.recordAtomCommon(commonBean.getContext(), commonBean);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onDebug$22(Context context, boolean z10) {
        return "packageName:" + context.getPackageName() + ",isDebug:" + z10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onDynamicEvent$13(int i10, int i11) {
        return "onDynamicEvent uploadMode:" + i10 + ",statId:" + i11;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onError$21() {
        return "onError...";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onEventEnd$11(String str, String str2) {
        return "onEventEnd eventID:" + str + ",eventTag:" + str2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onEventEnd$12(String str) {
        return "onEventEnd eventID:" + str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onEventStart$10(String str) {
        return "onEventStart eventID:" + str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onEventStart$9(String str, String str2) {
        return "onEventStart eventID:" + str + ",eventTag:" + str2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onKVEventEnd$16(String str, String str2) {
        return "onKVEventEnd eventID:" + str + ",eventTag:" + str2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onKVEventEnd$18(String str) {
        return "onKVEventEnd eventID:" + str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onKVEventStart$15(String str, String str2, Map map) {
        return "onKVEventStart eventID:" + str + ",eventTag:" + str2 + ",eventMap:" + map;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onKVEventStart$17(String str) {
        return "onKVEventStart eventID:" + str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onPause$19() {
        return "onPause...";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onResume$20() {
        return "onResume...";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onSettingKeyUpdate$6(SettingKeyDataBean settingKeyDataBean) {
        return "onSettingKeyUpdate logTag:" + settingKeyDataBean.getLogTag() + ", eventID:" + settingKeyDataBean.getEventID() + ", keys:" + settingKeyDataBean.getLogMap();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onSettingKeyUpdate$7() {
        return "Send data failed! logTag is null.";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onSpecialAppStart$8(int i10) {
        return "onSpecialAppStart appCode:" + i10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onStaticDataUpdate$4(PeriodDataBean periodDataBean) {
        return "onStaticDataUpdate logTag:" + periodDataBean.getLogTag() + ", eventID:" + periodDataBean.getEventID();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onStaticEvent$14(int i10, int i11, String str, String str2, String str3) {
        return "onStaticEvent uploadMode:" + i10 + ",statId:" + i11 + ",setId:" + str + ",setValue:" + str2 + ",remark:" + str3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$removeSsoID$26() {
        return "removeSsoID";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$setDebug$23(boolean z10) {
        return "onDebug (no context) sdk and dcs isDebug:" + z10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$setSessionTimeOut$24(int i10) {
        return "setSession timeout is " + i10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$setSsoID$25(String str) {
        return "setSsoid ssoid is " + str;
    }

    public static boolean onCommon(Context context, String str, String str2, Map<String, String> map) {
        CommonBean commonBean = new CommonBean(context);
        commonBean.setLogTag(str);
        commonBean.setEventID(str2);
        commonBean.setLogMap(map);
        return onCommon(commonBean, 1);
    }

    public static boolean onCommonBatch(Context context, String str, String str2, List<Map<String, String>> list, int i10) {
        return onCommonBatch(context, "", str, str2, list, i10);
    }

    public static void onDebug(final Context context, final boolean z10) {
        try {
            LogUtil.setDebug(z10);
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.c0
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onDebug$22;
                    lambda$onDebug$22 = OplusTrack.lambda$onDebug$22(context, z10);
                    return lambda$onDebug$22;
                }
            });
            if (LogUtil.isDebug()) {
                WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.OplusTrack.12
                    @Override // java.lang.Runnable
                    public void run() {
                        DebugAgent.setDebug(context, z10);
                    }
                });
            }
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    @Deprecated
    public static void onDynamicEvent(final Context context, final int i10, final int i11, final Map<String, String> map, final Map<String, String> map2) {
        try {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.y
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onDynamicEvent$13;
                    lambda$onDynamicEvent$13 = OplusTrack.lambda$onDynamicEvent$13(i10, i11);
                    return lambda$onDynamicEvent$13;
                }
            });
            WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.OplusTrack.6
                @Override // java.lang.Runnable
                public void run() {
                    OnEventAgent.onDynamicEvent(context, i10, i11, map, map2);
                }
            });
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static synchronized void onError(Context context) {
        synchronized (OplusTrack.class) {
            try {
                LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.t
                    @Override // com.oplus.statistics.util.Supplier
                    public final Object get() {
                        String lambda$onError$21;
                        lambda$onError$21 = OplusTrack.lambda$onError$21();
                        return lambda$onError$21;
                    }
                });
                if (sExceptionHandler == null) {
                    sExceptionHandler = new StatisticsExceptionHandler(context);
                    sExceptionHandler.setStatisticsExceptionHandler();
                }
            } catch (Exception e10) {
                LogUtil.e(TAG, new g0(e10));
            }
        }
    }

    @Deprecated
    public static void onEventEnd(final Context context, final String str, final String str2) {
        try {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.h
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onEventEnd$11;
                    lambda$onEventEnd$11 = OplusTrack.lambda$onEventEnd$11(str, str2);
                    return lambda$onEventEnd$11;
                }
            });
            if (formatCheck(str, str2, 1)) {
                WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.OplusTrack.4
                    @Override // java.lang.Runnable
                    public void run() {
                        OnEventAgent.onEventEnd(context, str, str2);
                    }
                });
            }
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    @Deprecated
    public static void onEventStart(final Context context, final String str, final String str2) {
        try {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.i
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onEventStart$9;
                    lambda$onEventStart$9 = OplusTrack.lambda$onEventStart$9(str, str2);
                    return lambda$onEventStart$9;
                }
            });
            if (formatCheck(str, str2, 1)) {
                WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.OplusTrack.2
                    @Override // java.lang.Runnable
                    public void run() {
                        OnEventAgent.onEventStart(context, str, str2);
                    }
                });
            }
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static void onKVEventEnd(final Context context, final String str, final String str2) {
        try {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.g
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onKVEventEnd$16;
                    lambda$onKVEventEnd$16 = OplusTrack.lambda$onKVEventEnd$16(str, str2);
                    return lambda$onKVEventEnd$16;
                }
            });
            if (formatCheck(str, str2, 1)) {
                WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.OplusTrack.9
                    @Override // java.lang.Runnable
                    public void run() {
                        OnEventAgent.onKVEventEnd(context, str, str2);
                    }
                });
            }
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static void onKVEventStart(final Context context, final String str, final Map<String, String> map, final String str2) {
        try {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.j
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onKVEventStart$15;
                    lambda$onKVEventStart$15 = OplusTrack.lambda$onKVEventStart$15(str, str2, map);
                    return lambda$onKVEventStart$15;
                }
            });
            if (formatCheck(str, str2, 1)) {
                WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.OplusTrack.8
                    @Override // java.lang.Runnable
                    public void run() {
                        OnEventAgent.onKVEventStart(context, str, map, str2);
                    }
                });
            }
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static void onPause(Context context) {
        try {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.n
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onPause$19;
                    lambda$onPause$19 = OplusTrack.lambda$onPause$19();
                    return lambda$onPause$19;
                }
            });
            sPageVisitAgent.onPause(context);
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static void onResume(Context context) {
        try {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.o
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onResume$20;
                    lambda$onResume$20 = OplusTrack.lambda$onResume$20();
                    return lambda$onResume$20;
                }
            });
            sPageVisitAgent.onResume(context);
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static void onSettingKeyUpdate(Context context, String str, String str2, List<SettingKeyBean> list) {
        SettingKeyDataBean settingKeyDataBean = new SettingKeyDataBean(context);
        settingKeyDataBean.setLogTag(str);
        settingKeyDataBean.setEventID(str2);
        settingKeyDataBean.setLogMap(list);
        onSettingKeyUpdate(context, settingKeyDataBean);
    }

    @Deprecated
    public static boolean onSpecialAppStart(Context context, final int i10) {
        LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.b
            @Override // com.oplus.statistics.util.Supplier
            public final Object get() {
                String lambda$onSpecialAppStart$8;
                lambda$onSpecialAppStart$8 = OplusTrack.lambda$onSpecialAppStart$8(i10);
                return lambda$onSpecialAppStart$8;
            }
        });
        return onCommon(context, CLIENT_START, CLIENT_START, null);
    }

    public static void onStaticDataUpdate(Context context, String str, String str2, Map<String, String> map) {
        PeriodDataBean periodDataBean = new PeriodDataBean(context);
        periodDataBean.setLogTag(str);
        periodDataBean.setEventID(str2);
        periodDataBean.setLogMap(map);
        onStaticDataUpdate(context, periodDataBean);
    }

    @Deprecated
    public static void onStaticEvent(final Context context, final int i10, final int i11, final String str, final String str2, final String str3, final Map<String, String> map) {
        try {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.b0
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onStaticEvent$14;
                    lambda$onStaticEvent$14 = OplusTrack.lambda$onStaticEvent$14(i10, i11, str, str2, str3);
                    return lambda$onStaticEvent$14;
                }
            });
            WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.OplusTrack.7
                @Override // java.lang.Runnable
                public void run() {
                    OnEventAgent.onStaticEvent(context, i10, i11, str, str2, str3, map);
                }
            });
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static void removeSsoID(Context context) {
        try {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.q
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$removeSsoID$26;
                    lambda$removeSsoID$26 = OplusTrack.lambda$removeSsoID$26();
                    return lambda$removeSsoID$26;
                }
            });
            PreferenceHandler.setSsoID(context);
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static void setDebug(final boolean z10) {
        try {
            LogUtil.setDebug(z10);
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.k
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$setDebug$23;
                    lambda$setDebug$23 = OplusTrack.lambda$setDebug$23(z10);
                    return lambda$setDebug$23;
                }
            });
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static void setSessionTimeOut(Context context, final int i10) {
        LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.m
            @Override // com.oplus.statistics.util.Supplier
            public final Object get() {
                String lambda$setSessionTimeOut$24;
                lambda$setSessionTimeOut$24 = OplusTrack.lambda$setSessionTimeOut$24(i10);
                return lambda$setSessionTimeOut$24;
            }
        });
        if (i10 > 0) {
            try {
                PreferenceHandler.setSessionTimeout(context, i10);
            } catch (Exception e10) {
                LogUtil.e(TAG, new g0(e10));
            }
        }
    }

    public static void setSsoID(Context context, final String str) {
        LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.h0
            @Override // com.oplus.statistics.util.Supplier
            public final Object get() {
                String lambda$setSsoID$25;
                lambda$setSsoID$25 = OplusTrack.lambda$setSsoID$25(str);
                return lambda$setSsoID$25;
            }
        });
        if (TextUtils.isEmpty(str) || str.equals("null")) {
            str = "0";
        }
        try {
            PreferenceHandler.setSsoID(context, str);
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static void init(Context context, OTrackConfig oTrackConfig) {
        init(context, ApkInfoUtil.getAppCode(context), oTrackConfig);
    }

    public static boolean onCommonBatch(Context context, String str, String str2, String str3, List<Map<String, String>> list, int i10) {
        CommonBatchBean commonBatchBean = new CommonBatchBean(context);
        commonBatchBean.setAppId(str);
        commonBatchBean.setLogTag(str2);
        commonBatchBean.setEventID(str3);
        commonBatchBean.setLogMap(list);
        return onCommon(commonBatchBean, i10);
    }

    public static void init(Context context, String str, OTrackConfig oTrackConfig) {
        Context applicationContext = context.getApplicationContext();
        if (applicationContext != null) {
            AppLifecycleCallbacks.getInstance().init((Application) applicationContext);
        }
        if (TextUtils.isEmpty(str)) {
            LogUtil.w(TAG, new Supplier() { // from class: com.oplus.statistics.u
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$init$0;
                    lambda$init$0 = OplusTrack.lambda$init$0();
                    return lambda$init$0;
                }
            });
        }
        ApkInfoUtil.putAppCodeToCache(context, str);
        OTrackContext.createIfNeed(str, context, oTrackConfig);
        if (oTrackConfig != null) {
            LogUtil.setDebug(oTrackConfig.getEnv() == 1);
        }
    }

    public static boolean onCommon(Context context, String str, String str2, Map<String, String> map, int i10) {
        CommonBean commonBean = new CommonBean(context);
        commonBean.setLogTag(str);
        commonBean.setEventID(str2);
        commonBean.setLogMap(map);
        return onCommon(commonBean, i10);
    }

    @Deprecated
    public static void onEventEnd(final Context context, final String str) {
        try {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.c
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onEventEnd$12;
                    lambda$onEventEnd$12 = OplusTrack.lambda$onEventEnd$12(str);
                    return lambda$onEventEnd$12;
                }
            });
            if (formatCheck(str, "", 1)) {
                WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.OplusTrack.5
                    @Override // java.lang.Runnable
                    public void run() {
                        OnEventAgent.onEventEnd(context, str, "");
                    }
                });
            }
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    @Deprecated
    public static void onEventStart(final Context context, final String str) {
        try {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.f
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onEventStart$10;
                    lambda$onEventStart$10 = OplusTrack.lambda$onEventStart$10(str);
                    return lambda$onEventStart$10;
                }
            });
            if (formatCheck(str, "", 1)) {
                WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.OplusTrack.3
                    @Override // java.lang.Runnable
                    public void run() {
                        OnEventAgent.onEventStart(context, str, "");
                    }
                });
            }
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static void onKVEventEnd(final Context context, final String str) {
        try {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.d
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onKVEventEnd$18;
                    lambda$onKVEventEnd$18 = OplusTrack.lambda$onKVEventEnd$18(str);
                    return lambda$onKVEventEnd$18;
                }
            });
            if (formatCheck(str, "", 1)) {
                WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.OplusTrack.11
                    @Override // java.lang.Runnable
                    public void run() {
                        OnEventAgent.onKVEventEnd(context, str, "");
                    }
                });
            }
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static void onKVEventStart(final Context context, final String str, final Map<String, String> map) {
        try {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.e
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onKVEventStart$17;
                    lambda$onKVEventStart$17 = OplusTrack.lambda$onKVEventStart$17(str);
                    return lambda$onKVEventStart$17;
                }
            });
            if (formatCheck(str, "", 1)) {
                WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.OplusTrack.10
                    @Override // java.lang.Runnable
                    public void run() {
                        OnEventAgent.onKVEventStart(context, str, map, "");
                    }
                });
            }
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static void onSettingKeyUpdate(final Context context, final SettingKeyDataBean settingKeyDataBean) {
        try {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.f0
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onSettingKeyUpdate$6;
                    lambda$onSettingKeyUpdate$6 = OplusTrack.lambda$onSettingKeyUpdate$6(SettingKeyDataBean.this);
                    return lambda$onSettingKeyUpdate$6;
                }
            });
            if (!TextUtils.isEmpty(settingKeyDataBean.getLogTag())) {
                WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.OplusTrack.1
                    @Override // java.lang.Runnable
                    public void run() {
                        StaticPeriodDataRecord.updateSettingKeyList(context, settingKeyDataBean);
                    }
                });
            } else {
                LogUtil.e(TAG, new Supplier() { // from class: com.oplus.statistics.w
                    @Override // com.oplus.statistics.util.Supplier
                    public final Object get() {
                        String lambda$onSettingKeyUpdate$7;
                        lambda$onSettingKeyUpdate$7 = OplusTrack.lambda$onSettingKeyUpdate$7();
                        return lambda$onSettingKeyUpdate$7;
                    }
                });
            }
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static void onStaticDataUpdate(final Context context, final PeriodDataBean periodDataBean) {
        try {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.e0
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onStaticDataUpdate$4;
                    lambda$onStaticDataUpdate$4 = OplusTrack.lambda$onStaticDataUpdate$4(PeriodDataBean.this);
                    return lambda$onStaticDataUpdate$4;
                }
            });
            WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.x
                @Override // java.lang.Runnable
                public final void run() {
                    StaticPeriodDataRecord.updateData(context, periodDataBean);
                }
            });
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static boolean onCommon(Context context, String str, String str2, Map<String, String> map, int i10, int i11) {
        CommonBean commonBean = new CommonBean(context);
        commonBean.setLogTag(str);
        commonBean.setEventID(str2);
        commonBean.setLogMap(map);
        commonBean.setAppId(i10);
        return onCommon(commonBean, i11);
    }

    public static boolean onCommon(Context context, String str, String str2, String str3, Map<String, String> map) {
        CommonBean commonBean = new CommonBean(context);
        commonBean.setAppId(str);
        commonBean.setLogTag(str2);
        commonBean.setEventID(str3);
        commonBean.setLogMap(map);
        return onCommon(commonBean, 1);
    }

    public static boolean onCommon(CommonBean commonBean) {
        return onCommon(commonBean, 1);
    }

    public static boolean onCommon(final CommonBean commonBean, final int i10) {
        if (!sFireWall.handleRequest(commonBean.getAppId() + "_" + commonBean.getLogTag() + "_" + commonBean.getEventID())) {
            ChattyEventTracker.getInstance().onChattyEvent(commonBean);
            return false;
        }
        try {
            LogUtil.v(TAG, new Supplier() { // from class: com.oplus.statistics.d0
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onCommon$1;
                    lambda$onCommon$1 = OplusTrack.lambda$onCommon$1(CommonBean.this, i10);
                    return lambda$onCommon$1;
                }
            });
            if ((i10 & 1) == 1) {
                WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.z
                    @Override // java.lang.Runnable
                    public final void run() {
                        OplusTrack.lambda$onCommon$2(CommonBean.this);
                    }
                });
            }
            if ((i10 & 2) == 2) {
                WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.a0
                    @Override // java.lang.Runnable
                    public final void run() {
                        OplusTrack.lambda$onCommon$3(CommonBean.this);
                    }
                });
            }
            return true;
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
            return false;
        }
    }
}
