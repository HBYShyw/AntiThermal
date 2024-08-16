package com.android.server.utils;

import android.util.Log;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class EventLogger {
    private static final String DUMP_TITLE_PREFIX = "Events log: ";
    private final ArrayDeque<Event> mEvents;
    private final int mMemSize;
    private final String mTag;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface DumpSink {
        void sink(String str, List<Event> list);
    }

    public EventLogger(int i, String str) {
        this.mEvents = new ArrayDeque<>(i);
        this.mMemSize = i;
        this.mTag = str;
    }

    public synchronized void enqueue(Event event) {
        if (this.mEvents.size() >= this.mMemSize) {
            this.mEvents.removeFirst();
        }
        this.mEvents.addLast(event);
    }

    public synchronized void enqueueAndLog(String str, int i, String str2) {
        enqueue(new StringEvent(str).printLog(i, str2));
    }

    public synchronized void dump(DumpSink dumpSink) {
        dumpSink.sink(this.mTag, new ArrayList(this.mEvents));
    }

    public synchronized void dump(PrintWriter printWriter) {
        dump(printWriter, "");
    }

    protected String getDumpTitle() {
        if (this.mTag == null) {
            return DUMP_TITLE_PREFIX;
        }
        return DUMP_TITLE_PREFIX + this.mTag;
    }

    public synchronized void dump(PrintWriter printWriter, String str) {
        printWriter.println(getDumpTitle());
        Iterator<Event> it = this.mEvents.iterator();
        while (it.hasNext()) {
            printWriter.println(str + it.next().toString());
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static abstract class Event {
        public static final int ALOGE = 1;
        public static final int ALOGI = 0;
        public static final int ALOGV = 3;
        public static final int ALOGW = 2;
        private static final SimpleDateFormat sFormat = new SimpleDateFormat("MM-dd HH:mm:ss:SSS", Locale.US);
        private final long mTimestamp = System.currentTimeMillis();

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public @interface LogType {
        }

        public abstract String eventToString();

        public String toString() {
            return sFormat.format(new Date(this.mTimestamp)) + " " + eventToString();
        }

        public Event printLog(String str) {
            return printLog(0, str);
        }

        public Event printLog(int i, String str) {
            if (i == 0) {
                Log.i(str, eventToString());
            } else if (i == 1) {
                Log.e(str, eventToString());
            } else if (i == 2) {
                Log.w(str, eventToString());
            } else {
                Log.v(str, eventToString());
            }
            return this;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class StringEvent extends Event {
        private final String mDescription;
        private final String mSource;

        public static StringEvent from(String str, String str2, Object... objArr) {
            return new StringEvent(str, String.format(Locale.US, str2, objArr));
        }

        public StringEvent(String str) {
            this(null, str);
        }

        public StringEvent(String str, String str2) {
            this.mSource = str;
            this.mDescription = str2;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public String eventToString() {
            String str = this.mSource;
            if (str == null) {
                return this.mDescription;
            }
            Object[] objArr = new Object[2];
            objArr[0] = str;
            String str2 = this.mDescription;
            if (str2 == null) {
                str2 = "";
            }
            objArr[1] = str2;
            return String.format("[%-40s] %s", objArr);
        }
    }
}
