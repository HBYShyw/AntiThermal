package com.oplus.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.LruCache;
import android.util.Slog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import oplus.util.OplusStatistics;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class OplusStatisticsImpl {
    private static final String APP_ID = "appId";
    private static final String APP_NAME = "appName";
    private static final String APP_PACKAGE = "appPackage";
    private static final String APP_VERSION = "appVersion";
    private static final int CHATTY_EVENT_APP_ID = 21000;
    private static final String CHATTY_EVENT_ID = "chatty_event";
    private static final String CHATTY_EVENT_LOG_TAG = "001";
    private static final String DATA_LIST = "dataList";
    private static final String DATA_TYPE = "dataType";
    private static final int DATA_TYPE_COMMON_MIX_LIST = 2000;
    private static final String EVENT_ID = "eventID";
    private static final int FIRE_WALL_LIMIT = 120;
    private static final long FIRE_WALL_PERIOD = 120000;
    private static final int FLAG_SEND_TO_ATOM = 2;
    private static final int FLAG_SEND_TO_DATA_CENTER = 1;
    private static final long GAP_TIME_LIMIT = 10000;
    private static final String LOG_MAP = "logMap";
    private static final String LOG_TAG = "logTag";
    private static final String MAP_KEY_APP_ID = "app_id";
    private static final String MAP_KEY_EVENT_ID = "event_id";
    private static final String MAP_KEY_LOG_TAG = "log_tag";
    private static final String MAP_KEY_TIMES = "times";
    private static final long SINGLE_MSG_MAX_LENGTH = 262144;
    private static final String SYSTEM = "system";
    private static final String TAG = "OplusStatistics--";
    private static final String THREAD_NAME = "OplusStatisticsCommonThread";
    private static final Uri DCS_URI = Uri.parse("content://com.oplus.statistics.provider/track_event");
    private static final Uri ATOM_DELEGATE = Uri.parse("content://com.oplus.atom.db_sys/atom_delegate");
    private final List<OplusStatistics.EventData> mEventDataCachePool = new ArrayList();
    private final ScheduledExecutorService mSingleThreadScheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() { // from class: com.oplus.util.OplusStatisticsImpl.1
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            Slog.d(OplusStatisticsImpl.TAG, "newThread.ThreadFactory");
            Thread thread = new Thread(r, "OplusStatisticsCommonThread#" + this.mCount.getAndIncrement());
            return thread;
        }
    });
    private final RequestFireWall mFireWall = new RequestFireWall(120, FIRE_WALL_PERIOD);
    private final Map<String, ChattyEvent> mChattyEventMap = new ArrayMap();
    private boolean mHasFlushJob = false;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCommonSync$0(Context context, OplusStatistics.EventData data, int flagSendTo) {
        handleData(context, data, flagSendTo, true);
    }

    public void onCommonSync(final Context context, final OplusStatistics.EventData data, final int flagSendTo) {
        addJobToWorkThread(new Runnable() { // from class: com.oplus.util.OplusStatisticsImpl$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                OplusStatisticsImpl.this.lambda$onCommonSync$0(context, data, flagSendTo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCommon$1(Context context, OplusStatistics.EventData data, int flagSendTo) {
        handleData(context, data, flagSendTo, false);
    }

    public void onCommon(final Context context, final OplusStatistics.EventData data, final int flagSendTo) {
        addJobToWorkThread(new Runnable() { // from class: com.oplus.util.OplusStatisticsImpl$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                OplusStatisticsImpl.this.lambda$onCommon$1(context, data, flagSendTo);
            }
        });
    }

    public void onCommon(final Context context, final List<OplusStatistics.EventData> dataList, int flagSendTo) {
        addJobToWorkThread(new Runnable() { // from class: com.oplus.util.OplusStatisticsImpl$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                OplusStatisticsImpl.this.lambda$onCommon$2(context, dataList);
            }
        });
    }

    public void flush(final Context context) {
        addJobToWorkThread(new Runnable() { // from class: com.oplus.util.OplusStatisticsImpl$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                OplusStatisticsImpl.this.lambda$flush$3(context);
            }
        });
    }

    private void addJobToWorkThread(Runnable runnable) {
        this.mSingleThreadScheduler.execute(runnable);
    }

    private void handleData(Context context, OplusStatistics.EventData data, int flagSendTo, boolean isSyncFlush) {
        if (SystemProperties.getBoolean("persist.sys.assert.panic", false)) {
            Slog.d(TAG, "onCommon: " + data);
        }
        if ((flagSendTo & 1) == 1) {
            handleDcsData(context, data, isSyncFlush);
        }
        if ((flagSendTo & 2) == 2) {
            sendDataToAtom(context, data);
        }
    }

    private void handleDcsData(Context context, OplusStatistics.EventData data, boolean isSyncFlush) {
        if (!this.mFireWall.handleRequest(data.appId + data.logTag + data.eventId)) {
            Slog.w(TAG, "Intercept frequent events(>120/2min): " + data);
            onChattyEvent(String.valueOf(data.appId), data.logTag, data.eventId);
            return;
        }
        this.mEventDataCachePool.add(data);
        if (isSyncFlush) {
            lambda$flush$3(context);
        } else {
            setDelayFlushJobIfNeed(context);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleDcsDataList, reason: merged with bridge method [inline-methods] */
    public void lambda$onCommon$2(Context context, List<OplusStatistics.EventData> dataList) {
        if (dataList.size() == 0) {
            Slog.w(TAG, "dataList size is empty.");
            return;
        }
        OplusStatistics.EventData data = dataList.get(0);
        if (!this.mFireWall.handleRequest(data.appId + data.logTag + data.eventId)) {
            Slog.w(TAG, "Intercept frequent events: " + data + ", count=" + dataList.size());
            onChattyEvent(String.valueOf(data.appId), data.logTag, data.eventId);
        } else {
            this.mEventDataCachePool.addAll(dataList);
            setDelayFlushJobIfNeed(context);
        }
    }

    private void setDelayFlushJobIfNeed(final Context context) {
        if (!this.mHasFlushJob) {
            this.mSingleThreadScheduler.schedule(new Runnable() { // from class: com.oplus.util.OplusStatisticsImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    OplusStatisticsImpl.this.lambda$setDelayFlushJobIfNeed$4(context);
                }
            }, GAP_TIME_LIMIT, TimeUnit.MILLISECONDS);
            this.mHasFlushJob = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setDelayFlushJobIfNeed$4(Context context) {
        this.mHasFlushJob = false;
        lambda$flush$3(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: flushInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$flush$3(Context context) {
        if (this.mEventDataCachePool.size() == 0) {
            Slog.d(TAG, "flushInternal failed. Has no data.");
            return;
        }
        JSONArray jsonArray = new JSONArray();
        long jsonStrSize = 0;
        for (OplusStatistics.EventData eventData : this.mEventDataCachePool) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(APP_ID, eventData.appId);
                jsonObject.put(LOG_TAG, eventData.logTag);
                jsonObject.put("eventId", eventData.eventId);
                jsonObject.put("eventTimeMs", eventData.eventTimeMs);
                String mapStr = mapToJsonStr(eventData.logMap);
                jsonObject.put(LOG_MAP, mapStr);
                if (mapStr.length() + jsonStrSize > SINGLE_MSG_MAX_LENGTH) {
                    Slog.w(TAG, "flushInternal, send: " + mapStr);
                    sendDataToDCS(context, jsonArray.toString());
                    jsonArray = new JSONArray();
                    jsonStrSize = 0;
                }
                jsonStrSize += mapStr.length();
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                Slog.w(TAG, "flushInternal, create json failed. " + e.toString());
            }
        }
        sendDataToDCS(context, jsonArray.toString());
        this.mEventDataCachePool.clear();
    }

    private void sendDataToAtom(Context context, OplusStatistics.EventData data) {
        ContentValues values = new ContentValues();
        values.put(APP_ID, Integer.valueOf(data.appId));
        values.put(APP_PACKAGE, SYSTEM);
        values.put(LOG_TAG, data.logTag);
        values.put(EVENT_ID, data.eventId);
        values.put(LOG_MAP, mapToJsonStr(data.logMap));
        try {
            context.getContentResolver().insert(ATOM_DELEGATE, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendDataToDCS(Context context, String dataListJson) {
        ContentValues values = new ContentValues();
        values.put(APP_PACKAGE, SYSTEM);
        values.put(APP_NAME, SYSTEM);
        values.put(APP_VERSION, SYSTEM);
        values.put(DATA_TYPE, (Integer) 2000);
        values.put(DATA_LIST, dataListJson);
        ContentResolver resolver = context.getContentResolver();
        try {
            resolver.insert(DCS_URI, values);
        } catch (IllegalArgumentException e) {
            Slog.e(TAG, "IllegalArgumentException:" + e);
        }
    }

    private String mapToJsonStr(Map<String, String> logMap) {
        JSONObject jsonObject = new JSONObject();
        if (logMap != null && !logMap.isEmpty()) {
            try {
                for (String key : logMap.keySet()) {
                    jsonObject.put(key, logMap.get(key));
                }
            } catch (Exception e) {
                Slog.w(TAG, "getCommonObject Exception: " + e);
            }
        }
        return jsonObject.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pollChattyEvent() {
        if (this.mChattyEventMap.size() == 0) {
            return;
        }
        for (ChattyEvent chattyEvent : this.mChattyEventMap.values()) {
            OplusStatistics.EventData eventData = new OplusStatistics.EventData(21000, CHATTY_EVENT_LOG_TAG, CHATTY_EVENT_ID);
            eventData.logMap = new HashMap();
            eventData.logMap.put(MAP_KEY_APP_ID, String.valueOf(chattyEvent.mAppId));
            eventData.logMap.put(MAP_KEY_LOG_TAG, chattyEvent.mLogTag);
            eventData.logMap.put("event_id", chattyEvent.mEventId);
            eventData.logMap.put(MAP_KEY_TIMES, String.valueOf(chattyEvent.mCount));
            this.mEventDataCachePool.add(eventData);
        }
        this.mChattyEventMap.clear();
    }

    private void onChattyEvent(String appId, String logTag, String eventId) {
        if (this.mChattyEventMap.size() == 0) {
            this.mSingleThreadScheduler.schedule(new Runnable() { // from class: com.oplus.util.OplusStatisticsImpl$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    OplusStatisticsImpl.this.pollChattyEvent();
                }
            }, FIRE_WALL_PERIOD, TimeUnit.MILLISECONDS);
        }
        String key = appId + logTag + eventId;
        ChattyEvent event = this.mChattyEventMap.get(key);
        if (event == null) {
            ChattyEvent event2 = new ChattyEvent(appId, logTag, eventId);
            event2.increment();
            this.mChattyEventMap.put(key, event2);
            return;
        }
        event.increment();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class RequestFireWall {
        private static final int CACHE_CAPACITY = 100;
        private static final String TAG = "RequestFireWall";
        private final int mLimit;
        private final long mPeriod;
        private final LruCache<String, Queue<Long>> mRequestQueueMap;

        private RequestFireWall(int limit, long period) {
            this.mLimit = limit;
            this.mPeriod = period;
            this.mRequestQueueMap = new LruCache<>(100);
        }

        public boolean handleRequest(String requestTag) {
            Queue<Long> requestQueue = getRequestQueue(requestTag);
            long currentTimeMillis = SystemClock.elapsedRealtime();
            requestQueue.add(Long.valueOf(currentTimeMillis));
            return getRequestCountInWindowImpl(requestQueue, currentTimeMillis) <= ((long) this.mLimit);
        }

        private long getRequestCountInWindowImpl(Queue<Long> requestQueue, long currentTime) {
            while (requestQueue.peek() != null && requestQueue.peek().longValue() < currentTime - this.mPeriod) {
                requestQueue.poll();
            }
            return requestQueue.size();
        }

        private Queue<Long> getRequestQueue(String requestTag) {
            Queue<Long> eventQueue = this.mRequestQueueMap.get(requestTag);
            if (eventQueue == null) {
                Queue<Long> eventQueue2 = new LinkedList<>();
                this.mRequestQueueMap.put(requestTag, eventQueue2);
                return eventQueue2;
            }
            return eventQueue;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ChattyEvent {
        private final String mAppId;
        private int mCount = 1;
        private final String mEventId;
        private final String mLogTag;

        public ChattyEvent(String appId, String logTag, String eventId) {
            this.mAppId = appId;
            this.mLogTag = logTag;
            this.mEventId = eventId;
        }

        public int increment() {
            int i = this.mCount;
            this.mCount = i + 1;
            return i;
        }
    }
}
