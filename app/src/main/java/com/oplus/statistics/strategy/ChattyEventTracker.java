package com.oplus.statistics.strategy;

import android.content.Context;
import android.util.ArrayMap;
import com.oplus.statistics.agent.CommonAgent;
import com.oplus.statistics.data.CommonBean;
import com.oplus.statistics.util.LogUtil;
import com.oplus.statistics.util.Supplier;
import java.util.Map;

/* loaded from: classes2.dex */
public class ChattyEventTracker {
    private static final String CHATTY_EVENT_ID = "chatty_event";
    private static final String MAP_KEY_APP_ID = "app_id";
    private static final String MAP_KEY_EVENT_ID = "event_id";
    private static final String MAP_KEY_LOG_TAG = "log_tag";
    private static final String MAP_KEY_TIMES = "times";
    private static final long MAX_VALUE_TO_COMMIT = 100;
    private static final long MIN_TIME_TO_COMMIT = 300000;
    private static final String SELF_TRACK_APP_ID = "21000";
    private static final String SELF_TRACK_LOG_TAG = "001";
    private static final String TAG = "ChattyEventTracker";
    private int mCacheCount;
    private final Map<String, ChattyEvent> mChattyEventMap;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class ChattyEvent {
        private final String appId;
        private final String eventId;
        private final String logTag;
        private int times;

        public ChattyEvent(String str, String str2, String str3) {
            this.appId = str;
            this.logTag = str2;
            this.eventId = str3;
        }

        public int increment() {
            int i10 = this.times;
            this.times = i10 + 1;
            return i10;
        }
    }

    /* loaded from: classes2.dex */
    private static class SingletonHolder {
        private static final ChattyEventTracker instance = new ChattyEventTracker();

        private SingletonHolder() {
        }
    }

    public static ChattyEventTracker getInstance() {
        return SingletonHolder.instance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onChattyEvent$0() {
        return "context is empty.";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onChattyEvent$1(Context context, CommonBean commonBean) {
        onChattyEvent(context, commonBean.getAppId(), commonBean.getLogTag(), commonBean.getEventID());
    }

    /* renamed from: commit, reason: merged with bridge method [inline-methods] */
    public void lambda$onChattyEvent$2(Context context) {
        for (ChattyEvent chattyEvent : this.mChattyEventMap.values()) {
            CommonBean commonBean = new CommonBean(context, SELF_TRACK_APP_ID, SELF_TRACK_LOG_TAG, CHATTY_EVENT_ID);
            ArrayMap arrayMap = new ArrayMap();
            arrayMap.put(MAP_KEY_APP_ID, String.valueOf(chattyEvent.appId));
            arrayMap.put(MAP_KEY_LOG_TAG, chattyEvent.logTag);
            arrayMap.put("event_id", chattyEvent.eventId);
            arrayMap.put(MAP_KEY_TIMES, String.valueOf(chattyEvent.times));
            commonBean.setLogMap(arrayMap);
            CommonAgent.recordCommon(context, commonBean);
        }
        this.mCacheCount = 0;
        this.mChattyEventMap.clear();
        WorkThread.getInstance().removeMessages(1);
    }

    public void onChattyEvent(final CommonBean commonBean) {
        final Context applicationContext = commonBean.getContext().getApplicationContext();
        if (applicationContext == null) {
            LogUtil.e(TAG, new Supplier() { // from class: com.oplus.statistics.strategy.a
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onChattyEvent$0;
                    lambda$onChattyEvent$0 = ChattyEventTracker.lambda$onChattyEvent$0();
                    return lambda$onChattyEvent$0;
                }
            });
        } else {
            WorkThread.execute(new Runnable() { // from class: com.oplus.statistics.strategy.c
                @Override // java.lang.Runnable
                public final void run() {
                    ChattyEventTracker.this.lambda$onChattyEvent$1(applicationContext, commonBean);
                }
            });
        }
    }

    private ChattyEventTracker() {
        this.mChattyEventMap = new ArrayMap();
    }

    private void onChattyEvent(final Context context, String str, String str2, String str3) {
        String str4 = str + str2 + str3;
        ChattyEvent chattyEvent = this.mChattyEventMap.get(str4);
        if (chattyEvent == null) {
            ChattyEvent chattyEvent2 = new ChattyEvent(str, str2, str3);
            chattyEvent2.increment();
            this.mChattyEventMap.put(str4, chattyEvent2);
        } else {
            chattyEvent.increment();
        }
        int i10 = this.mCacheCount + 1;
        this.mCacheCount = i10;
        if (i10 >= MAX_VALUE_TO_COMMIT) {
            lambda$onChattyEvent$2(context);
        } else {
            if (i10 != 1 || WorkThread.getInstance().hasMessages(1)) {
                return;
            }
            WorkThread.getInstance().postDelay(1, new Runnable() { // from class: com.oplus.statistics.strategy.b
                @Override // java.lang.Runnable
                public final void run() {
                    ChattyEventTracker.this.lambda$onChattyEvent$2(context);
                }
            }, MIN_TIME_TO_COMMIT);
        }
    }
}
