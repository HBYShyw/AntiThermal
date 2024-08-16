package com.android.server.usage;

import android.app.usage.ConfigurationStats;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.content.res.Configuration;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.util.XmlUtils;
import com.android.server.usage.IntervalStats;
import java.io.IOException;
import java.net.ProtocolException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class UsageStatsXmlV1 {
    private static final String ACTIVE_ATTR = "active";
    private static final String APP_LAUNCH_COUNT_ATTR = "appLaunchCount";
    private static final String CATEGORY_TAG = "category";
    private static final String CHOOSER_COUNT_TAG = "chosen_action";
    private static final String CLASS_ATTR = "class";
    private static final String CONFIGURATIONS_TAG = "configurations";
    private static final String CONFIG_TAG = "config";
    private static final String COUNT = "count";
    private static final String COUNT_ATTR = "count";
    private static final String END_TIME_ATTR = "endTime";
    private static final String EVENT_LOG_TAG = "event-log";
    private static final String EVENT_TAG = "event";
    private static final String FLAGS_ATTR = "flags";
    private static final String INSTANCE_ID_ATTR = "instanceId";
    private static final String INTERACTIVE_TAG = "interactive";
    private static final String KEYGUARD_HIDDEN_TAG = "keyguard-hidden";
    private static final String KEYGUARD_SHOWN_TAG = "keyguard-shown";
    private static final String LAST_EVENT_ATTR = "lastEvent";
    private static final String LAST_TIME_ACTIVE_ATTR = "lastTimeActive";
    private static final String LAST_TIME_SERVICE_USED_ATTR = "lastTimeServiceUsed";
    private static final String LAST_TIME_VISIBLE_ATTR = "lastTimeVisible";
    private static final String MAJOR_VERSION_ATTR = "majorVersion";
    private static final String MINOR_VERSION_ATTR = "minorVersion";
    private static final String NAME = "name";
    private static final String NON_INTERACTIVE_TAG = "non-interactive";
    private static final String NOTIFICATION_CHANNEL_ATTR = "notificationChannel";
    private static final String PACKAGES_TAG = "packages";
    private static final String PACKAGE_ATTR = "package";
    private static final String PACKAGE_TAG = "package";
    private static final String SHORTCUT_ID_ATTR = "shortcutId";
    private static final String STANDBY_BUCKET_ATTR = "standbyBucket";
    private static final String TAG = "UsageStatsXmlV1";
    private static final String TIME_ATTR = "time";
    private static final String TOTAL_TIME_ACTIVE_ATTR = "timeActive";
    private static final String TOTAL_TIME_SERVICE_USED_ATTR = "timeServiceUsed";
    private static final String TOTAL_TIME_VISIBLE_ATTR = "timeVisible";
    private static final String TYPE_ATTR = "type";

    private static void loadUsageStats(XmlPullParser xmlPullParser, IntervalStats intervalStats) throws XmlPullParserException, IOException {
        String attributeValue = xmlPullParser.getAttributeValue(null, "package");
        if (attributeValue == null) {
            throw new ProtocolException("no package attribute present");
        }
        UsageStats orCreateUsageStats = intervalStats.getOrCreateUsageStats(attributeValue);
        orCreateUsageStats.mLastTimeUsed = intervalStats.beginTime + XmlUtils.readLongAttribute(xmlPullParser, LAST_TIME_ACTIVE_ATTR);
        try {
            orCreateUsageStats.mLastTimeVisible = intervalStats.beginTime + XmlUtils.readLongAttribute(xmlPullParser, LAST_TIME_VISIBLE_ATTR);
        } catch (IOException unused) {
            Log.i(TAG, "Failed to parse mLastTimeVisible");
        }
        try {
            orCreateUsageStats.mLastTimeForegroundServiceUsed = intervalStats.beginTime + XmlUtils.readLongAttribute(xmlPullParser, LAST_TIME_SERVICE_USED_ATTR);
        } catch (IOException unused2) {
            Log.i(TAG, "Failed to parse mLastTimeForegroundServiceUsed");
        }
        orCreateUsageStats.mTotalTimeInForeground = XmlUtils.readLongAttribute(xmlPullParser, TOTAL_TIME_ACTIVE_ATTR);
        try {
            orCreateUsageStats.mTotalTimeVisible = XmlUtils.readLongAttribute(xmlPullParser, TOTAL_TIME_VISIBLE_ATTR);
        } catch (IOException unused3) {
            Log.i(TAG, "Failed to parse mTotalTimeVisible");
        }
        try {
            orCreateUsageStats.mTotalTimeForegroundServiceUsed = XmlUtils.readLongAttribute(xmlPullParser, TOTAL_TIME_SERVICE_USED_ATTR);
        } catch (IOException unused4) {
            Log.i(TAG, "Failed to parse mTotalTimeForegroundServiceUsed");
        }
        orCreateUsageStats.mLastEvent = XmlUtils.readIntAttribute(xmlPullParser, LAST_EVENT_ATTR);
        orCreateUsageStats.mAppLaunchCount = XmlUtils.readIntAttribute(xmlPullParser, APP_LAUNCH_COUNT_ATTR, 0);
        while (true) {
            int next = xmlPullParser.next();
            if (next == 1) {
                return;
            }
            String name = xmlPullParser.getName();
            if (next == 3 && name.equals("package")) {
                return;
            }
            if (next == 2 && name.equals(CHOOSER_COUNT_TAG)) {
                loadChooserCounts(xmlPullParser, orCreateUsageStats, XmlUtils.readStringAttribute(xmlPullParser, "name"));
            }
        }
    }

    private static void loadCountAndTime(XmlPullParser xmlPullParser, IntervalStats.EventTracker eventTracker) throws IOException, XmlPullParserException {
        eventTracker.count = XmlUtils.readIntAttribute(xmlPullParser, "count", 0);
        eventTracker.duration = XmlUtils.readLongAttribute(xmlPullParser, TIME_ATTR, 0L);
        XmlUtils.skipCurrentTag(xmlPullParser);
    }

    private static void loadChooserCounts(XmlPullParser xmlPullParser, UsageStats usageStats, String str) throws XmlPullParserException, IOException {
        if (str == null) {
            return;
        }
        if (usageStats.mChooserCounts == null) {
            usageStats.mChooserCounts = new ArrayMap();
        }
        if (!usageStats.mChooserCounts.containsKey(str)) {
            usageStats.mChooserCounts.put(str, new ArrayMap());
        }
        while (true) {
            int next = xmlPullParser.next();
            if (next == 1) {
                return;
            }
            String name = xmlPullParser.getName();
            if (next == 3 && name.equals(CHOOSER_COUNT_TAG)) {
                return;
            }
            if (next == 2 && name.equals(CATEGORY_TAG)) {
                ((ArrayMap) usageStats.mChooserCounts.get(str)).put(XmlUtils.readStringAttribute(xmlPullParser, "name"), Integer.valueOf(XmlUtils.readIntAttribute(xmlPullParser, "count")));
            }
        }
    }

    private static void loadConfigStats(XmlPullParser xmlPullParser, IntervalStats intervalStats) throws XmlPullParserException, IOException {
        Configuration configuration = new Configuration();
        Configuration.readXmlAttrs(xmlPullParser, configuration);
        ConfigurationStats orCreateConfigurationStats = intervalStats.getOrCreateConfigurationStats(configuration);
        orCreateConfigurationStats.mLastTimeActive = intervalStats.beginTime + XmlUtils.readLongAttribute(xmlPullParser, LAST_TIME_ACTIVE_ATTR);
        orCreateConfigurationStats.mTotalTimeActive = XmlUtils.readLongAttribute(xmlPullParser, TOTAL_TIME_ACTIVE_ATTR);
        orCreateConfigurationStats.mActivationCount = XmlUtils.readIntAttribute(xmlPullParser, "count");
        if (XmlUtils.readBooleanAttribute(xmlPullParser, "active")) {
            intervalStats.activeConfiguration = orCreateConfigurationStats.mConfiguration;
        }
    }

    private static void loadEvent(XmlPullParser xmlPullParser, IntervalStats intervalStats) throws XmlPullParserException, IOException {
        String readStringAttribute = XmlUtils.readStringAttribute(xmlPullParser, "package");
        if (readStringAttribute == null) {
            throw new ProtocolException("no package attribute present");
        }
        UsageEvents.Event buildEvent = intervalStats.buildEvent(readStringAttribute, XmlUtils.readStringAttribute(xmlPullParser, CLASS_ATTR));
        buildEvent.mFlags = XmlUtils.readIntAttribute(xmlPullParser, FLAGS_ATTR, 0);
        buildEvent.mTimeStamp = intervalStats.beginTime + XmlUtils.readLongAttribute(xmlPullParser, TIME_ATTR);
        buildEvent.mEventType = XmlUtils.readIntAttribute(xmlPullParser, "type");
        try {
            buildEvent.mInstanceId = XmlUtils.readIntAttribute(xmlPullParser, INSTANCE_ID_ATTR);
        } catch (IOException unused) {
            Log.i(TAG, "Failed to parse mInstanceId");
        }
        int i = buildEvent.mEventType;
        if (i != 5) {
            if (i == 8) {
                String readStringAttribute2 = XmlUtils.readStringAttribute(xmlPullParser, SHORTCUT_ID_ATTR);
                buildEvent.mShortcutId = readStringAttribute2 != null ? readStringAttribute2.intern() : null;
            } else if (i == 11) {
                buildEvent.mBucketAndReason = XmlUtils.readIntAttribute(xmlPullParser, STANDBY_BUCKET_ATTR, 0);
            } else if (i == 12) {
                String readStringAttribute3 = XmlUtils.readStringAttribute(xmlPullParser, NOTIFICATION_CHANNEL_ATTR);
                buildEvent.mNotificationChannelId = readStringAttribute3 != null ? readStringAttribute3.intern() : null;
            }
        } else {
            Configuration configuration = new Configuration();
            buildEvent.mConfiguration = configuration;
            Configuration.readXmlAttrs(xmlPullParser, configuration);
        }
        intervalStats.addEvent(buildEvent);
    }

    /* JADX WARN: Code restructure failed: missing block: B:63:0x00a5, code lost:
    
        if (r1.equals(com.android.server.usage.UsageStatsXmlV1.KEYGUARD_HIDDEN_TAG) == false) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void read(XmlPullParser xmlPullParser, IntervalStats intervalStats) throws XmlPullParserException, IOException {
        intervalStats.packageStats.clear();
        intervalStats.configurations.clear();
        intervalStats.activeConfiguration = null;
        intervalStats.events.clear();
        intervalStats.endTime = intervalStats.beginTime + XmlUtils.readLongAttribute(xmlPullParser, END_TIME_ATTR);
        try {
            intervalStats.majorVersion = XmlUtils.readIntAttribute(xmlPullParser, MAJOR_VERSION_ATTR);
        } catch (IOException unused) {
            Log.i(TAG, "Failed to parse majorVersion");
        }
        try {
            intervalStats.minorVersion = XmlUtils.readIntAttribute(xmlPullParser, MINOR_VERSION_ATTR);
        } catch (IOException unused2) {
            Log.i(TAG, "Failed to parse minorVersion");
        }
        int depth = xmlPullParser.getDepth();
        while (true) {
            int next = xmlPullParser.next();
            char c = 1;
            if (next == 1) {
                return;
            }
            if (next == 3 && xmlPullParser.getDepth() <= depth) {
                return;
            }
            if (next == 2) {
                String name = xmlPullParser.getName();
                name.hashCode();
                switch (name.hashCode()) {
                    case -1354792126:
                        if (name.equals(CONFIG_TAG)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1169351247:
                        break;
                    case -807157790:
                        if (name.equals(NON_INTERACTIVE_TAG)) {
                            c = 2;
                            break;
                        }
                        break;
                    case -807062458:
                        if (name.equals("package")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 96891546:
                        if (name.equals(EVENT_TAG)) {
                            c = 4;
                            break;
                        }
                        break;
                    case 526608426:
                        if (name.equals(KEYGUARD_SHOWN_TAG)) {
                            c = 5;
                            break;
                        }
                        break;
                    case 1844104930:
                        if (name.equals(INTERACTIVE_TAG)) {
                            c = 6;
                            break;
                        }
                        break;
                }
                c = 65535;
                switch (c) {
                    case 0:
                        loadConfigStats(xmlPullParser, intervalStats);
                        break;
                    case 1:
                        loadCountAndTime(xmlPullParser, intervalStats.keyguardHiddenTracker);
                        break;
                    case 2:
                        loadCountAndTime(xmlPullParser, intervalStats.nonInteractiveTracker);
                        break;
                    case 3:
                        loadUsageStats(xmlPullParser, intervalStats);
                        break;
                    case 4:
                        loadEvent(xmlPullParser, intervalStats);
                        break;
                    case 5:
                        loadCountAndTime(xmlPullParser, intervalStats.keyguardShownTracker);
                        break;
                    case 6:
                        loadCountAndTime(xmlPullParser, intervalStats.interactiveTracker);
                        break;
                }
            }
        }
    }

    private UsageStatsXmlV1() {
    }
}
