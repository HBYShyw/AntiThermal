package com.oplus.statistics.agent;

import android.content.Context;
import android.text.TextUtils;
import com.oplus.statistics.data.PageVisitBean;
import com.oplus.statistics.g0;
import com.oplus.statistics.record.ProxyRecorder;
import com.oplus.statistics.storage.PreferenceHandler;
import com.oplus.statistics.strategy.WorkThread;
import com.oplus.statistics.util.LogUtil;
import com.oplus.statistics.util.Supplier;
import com.oplus.statistics.util.TimeInfoUtil;
import org.json.JSONArray;
import org.json.JSONException;

/* loaded from: classes2.dex */
public class PageVisitAgent {
    private static final int PAGE_VISIT_MAX_COUNT = 10;
    private static final int PAUSE = 1;
    private static final int RESUME = 0;
    private static final String TAG = "PageVisitAgent";

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class HandlePageVisitRunnable implements Runnable {
        private String mClassName;
        private Context mContext;
        private long mCurrentTimeMills;
        private int mType;

        public HandlePageVisitRunnable(Context context, String str, long j10, int i10) {
            this.mContext = context;
            this.mClassName = str;
            this.mCurrentTimeMills = j10;
            this.mType = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            int i10 = this.mType;
            if (i10 == 0) {
                PageVisitAgent.recordResume(this.mContext, this.mClassName, this.mCurrentTimeMills);
            } else {
                if (i10 != 1) {
                    return;
                }
                PageVisitAgent.recordPause(this.mContext, this.mClassName, this.mCurrentTimeMills);
            }
        }
    }

    private static String getClassName(Context context) {
        return context != null ? context.getClass().getSimpleName() : "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onPause$0(String str) {
        return "onPause: " + str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onPause$1() {
        return "onPause() called without context.";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onResume$2(String str) {
        return "onResume: " + str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onResume$3() {
        return "onPause() called without context.";
    }

    private static void recordPageVisit(Context context) {
        String pageVisitRoutes = PreferenceHandler.getPageVisitRoutes(context);
        int pageVisitDuration = PreferenceHandler.getPageVisitDuration(context);
        if (!TextUtils.isEmpty(pageVisitRoutes)) {
            PageVisitBean pageVisitBean = new PageVisitBean(context);
            pageVisitBean.setActivities(pageVisitRoutes);
            pageVisitBean.setDuration(pageVisitDuration);
            pageVisitBean.setTime(TimeInfoUtil.getFormatTime());
            ProxyRecorder.getInstance().addTrackEvent(context, pageVisitBean);
        }
        PreferenceHandler.setPageVisitDuration(context, 0);
        PreferenceHandler.setPageVisitRoutes(context, "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void recordPause(Context context, String str, long j10) {
        JSONArray jSONArray;
        long activityStartTime = PreferenceHandler.getActivityStartTime(context);
        int i10 = (int) ((j10 - activityStartTime) / 1000);
        if (str.equals(PreferenceHandler.getCurrentActivity(context)) && i10 >= 0 && -1 != activityStartTime) {
            try {
                String pageVisitRoutes = PreferenceHandler.getPageVisitRoutes(context);
                int pageVisitDuration = PreferenceHandler.getPageVisitDuration(context);
                if (!TextUtils.isEmpty(pageVisitRoutes)) {
                    jSONArray = new JSONArray(pageVisitRoutes);
                    if (jSONArray.length() >= 10) {
                        recordPageVisit(context);
                        jSONArray = new JSONArray();
                    }
                } else {
                    jSONArray = new JSONArray();
                }
                JSONArray jSONArray2 = new JSONArray();
                jSONArray2.put(str);
                jSONArray2.put(i10);
                jSONArray.put(jSONArray2);
                PreferenceHandler.setPageVisitDuration(context, pageVisitDuration + i10);
                PreferenceHandler.setPageVisitRoutes(context, jSONArray.toString());
            } catch (JSONException e10) {
                LogUtil.e(TAG, new Supplier() { // from class: com.oplus.statistics.agent.e
                    @Override // com.oplus.statistics.util.Supplier
                    public final Object get() {
                        return e10.toString();
                    }
                });
            } catch (Exception e11) {
                LogUtil.e(TAG, new g0(e11));
                PreferenceHandler.setPageVisitRoutes(context, "");
                PreferenceHandler.setPageVisitDuration(context, 0);
            }
        }
        PreferenceHandler.setActivityEndTime(context, j10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void recordResume(Context context, String str, long j10) {
        long activityEndTime = PreferenceHandler.getActivityEndTime(context);
        long sessionTimeout = PreferenceHandler.getSessionTimeout(context) * 1000;
        if (j10 - PreferenceHandler.getActivityStartTime(context) >= sessionTimeout && (-1 == activityEndTime || activityEndTime >= j10 || j10 - activityEndTime >= sessionTimeout)) {
            AppStartAgent.recordAppStart(context);
            recordPageVisit(context);
        }
        PreferenceHandler.setActivityStartTime(context, j10);
        PreferenceHandler.setCurrentActivity(context, str);
    }

    public void onPause(Context context) {
        if (context != null) {
            long currentTimeMillis = System.currentTimeMillis();
            final String className = getClassName(context);
            LogUtil.i(TAG, new Supplier() { // from class: com.oplus.statistics.agent.c
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onPause$0;
                    lambda$onPause$0 = PageVisitAgent.lambda$onPause$0(className);
                    return lambda$onPause$0;
                }
            });
            WorkThread.execute(new HandlePageVisitRunnable(context, className, currentTimeMillis, 1));
            return;
        }
        LogUtil.e(TAG, new Supplier() { // from class: com.oplus.statistics.agent.g
            @Override // com.oplus.statistics.util.Supplier
            public final Object get() {
                String lambda$onPause$1;
                lambda$onPause$1 = PageVisitAgent.lambda$onPause$1();
                return lambda$onPause$1;
            }
        });
    }

    public void onResume(Context context) {
        if (context != null) {
            long currentTimeMillis = System.currentTimeMillis();
            final String className = getClassName(context);
            LogUtil.i(TAG, new Supplier() { // from class: com.oplus.statistics.agent.d
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onResume$2;
                    lambda$onResume$2 = PageVisitAgent.lambda$onResume$2(className);
                    return lambda$onResume$2;
                }
            });
            WorkThread.execute(new HandlePageVisitRunnable(context, className, currentTimeMillis, 0));
            return;
        }
        LogUtil.e(TAG, new Supplier() { // from class: com.oplus.statistics.agent.f
            @Override // com.oplus.statistics.util.Supplier
            public final Object get() {
                String lambda$onResume$3;
                lambda$onResume$3 = PageVisitAgent.lambda$onResume$3();
                return lambda$onResume$3;
            }
        });
    }
}
