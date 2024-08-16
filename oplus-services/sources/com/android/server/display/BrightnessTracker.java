package com.android.server.display;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ParceledListSlice;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.display.AmbientBrightnessDayStats;
import android.hardware.display.BrightnessChangeEvent;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManagerInternal;
import android.hardware.display.DisplayedContentSample;
import android.hardware.display.DisplayedContentSamplingAttributes;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserManager;
import android.provider.Settings;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.RingBuffer;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.LocalServices;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BrightnessTracker {
    private static final String AMBIENT_BRIGHTNESS_STATS_FILE = "ambient_brightness_stats.xml";
    private static final String ATTR_BATTERY_LEVEL = "batteryLevel";
    private static final String ATTR_COLOR_SAMPLE_DURATION = "colorSampleDuration";
    private static final String ATTR_COLOR_TEMPERATURE = "colorTemperature";
    private static final String ATTR_COLOR_VALUE_BUCKETS = "colorValueBuckets";
    private static final String ATTR_DEFAULT_CONFIG = "defaultConfig";
    private static final String ATTR_LAST_NITS = "lastNits";
    private static final String ATTR_LUX = "lux";
    private static final String ATTR_LUX_TIMESTAMPS = "luxTimestamps";
    private static final String ATTR_NIGHT_MODE = "nightMode";
    private static final String ATTR_NITS = "nits";
    private static final String ATTR_PACKAGE_NAME = "packageName";
    private static final String ATTR_POWER_SAVE = "powerSaveFactor";
    private static final String ATTR_REDUCE_BRIGHT_COLORS = "reduceBrightColors";
    private static final String ATTR_REDUCE_BRIGHT_COLORS_OFFSET = "reduceBrightColorsOffset";
    private static final String ATTR_REDUCE_BRIGHT_COLORS_STRENGTH = "reduceBrightColorsStrength";
    private static final String ATTR_TIMESTAMP = "timestamp";
    private static final String ATTR_UNIQUE_DISPLAY_ID = "uniqueDisplayId";
    private static final String ATTR_USER = "user";
    private static final String ATTR_USER_POINT = "userPoint";
    private static final int COLOR_SAMPLE_COMPONENT_MASK = 4;
    private static final String EVENTS_FILE = "brightness_events.xml";
    private static final int MAX_EVENTS = 100;
    private static final int MSG_BACKGROUND_START = 0;
    private static final int MSG_BRIGHTNESS_CHANGED = 1;
    private static final int MSG_SENSOR_CHANGED = 5;
    private static final int MSG_SHOULD_COLLECT_COLOR_SAMPLE_CHANGED = 4;
    private static final int MSG_START_SENSOR_LISTENER = 3;
    private static final int MSG_STOP_SENSOR_LISTENER = 2;
    static final String TAG = "BrightnessTracker";
    private static final String TAG_EVENT = "event";
    private static final String TAG_EVENTS = "events";
    private AmbientBrightnessStatsTracker mAmbientBrightnessStatsTracker;
    private final Handler mBgHandler;
    private BroadcastReceiver mBroadcastReceiver;
    private boolean mColorSamplingEnabled;
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private DisplayListener mDisplayListener;

    @GuardedBy({"mEventsLock"})
    private boolean mEventsDirty;
    private float mFrameRate;
    private final Injector mInjector;
    private Sensor mLightSensor;
    private int mNoFramesToSample;
    private SensorListener mSensorListener;
    private boolean mSensorRegistered;
    private SettingsObserver mSettingsObserver;

    @GuardedBy({"mDataCollectionLock"})
    private boolean mStarted;
    private final UserManager mUserManager;
    private volatile boolean mWriteBrightnessTrackerStateScheduled;
    static final boolean DEBUG = SystemProperties.getBoolean("dbg.dms.brighttrack", false);
    private static final long MAX_EVENT_AGE = TimeUnit.DAYS.toMillis(30);
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    private static final long COLOR_SAMPLE_DURATION = TimeUnit.SECONDS.toSeconds(10);
    private final Object mEventsLock = new Object();

    @GuardedBy({"mEventsLock"})
    private RingBuffer<BrightnessChangeEvent> mEvents = new RingBuffer<>(BrightnessChangeEvent.class, 100);
    private boolean mShouldCollectColorSample = false;
    private boolean mRegistedFlag = false;
    private int mCurrentUserId = -10000;
    private final Object mDataCollectionLock = new Object();

    @GuardedBy({"mDataCollectionLock"})
    private float mLastBatteryLevel = Float.NaN;

    @GuardedBy({"mDataCollectionLock"})
    private float mLastBrightness = -1.0f;

    public BrightnessTracker(Context context, Injector injector) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        if (injector != null) {
            this.mInjector = injector;
        } else {
            this.mInjector = new Injector();
        }
        this.mBgHandler = new TrackerHandler(this.mInjector.getBackgroundHandler().getLooper());
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
    }

    public void start(float f) {
        if (DEBUG) {
            Slog.d(TAG, "Start");
        }
        this.mCurrentUserId = ActivityManager.getCurrentUser();
        this.mBgHandler.obtainMessage(0, Float.valueOf(f)).sendToTarget();
    }

    public void setShouldCollectColorSample(boolean z) {
        this.mBgHandler.obtainMessage(4, Boolean.valueOf(z)).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void backgroundStart(float f) {
        synchronized (this.mDataCollectionLock) {
            if (this.mStarted) {
                return;
            }
            if (DEBUG) {
                Slog.d(TAG, "Background start");
            }
            readEvents();
            readAmbientBrightnessStats();
            this.mSensorListener = new SensorListener();
            SettingsObserver settingsObserver = new SettingsObserver(this.mBgHandler);
            this.mSettingsObserver = settingsObserver;
            this.mInjector.registerBrightnessModeObserver(this.mContentResolver, settingsObserver);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
            intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            Receiver receiver = new Receiver();
            this.mBroadcastReceiver = receiver;
            this.mInjector.registerReceiver(this.mContext, receiver, intentFilter);
            this.mRegistedFlag = true;
            this.mInjector.scheduleIdleJob(this.mContext);
            synchronized (this.mDataCollectionLock) {
                this.mLastBrightness = f;
                this.mStarted = true;
            }
            enableColorSampling();
        }
    }

    public void screenOffAction() {
        this.mBgHandler.obtainMessage(2).sendToTarget();
    }

    void stop() {
        synchronized (this.mDataCollectionLock) {
            if (this.mStarted) {
                if (DEBUG) {
                    Slog.d(TAG, "Stop");
                }
                this.mBgHandler.removeMessages(0);
                if (this.mRegistedFlag) {
                    this.mInjector.unregisterSensorListener(this.mContext, this.mSensorListener);
                    this.mInjector.unregisterBrightnessModeObserver(this.mContext, this.mSettingsObserver);
                    this.mInjector.unregisterReceiver(this.mContext, this.mBroadcastReceiver);
                }
                this.mRegistedFlag = false;
                this.mInjector.cancelIdleJob(this.mContext);
                synchronized (this.mDataCollectionLock) {
                    this.mStarted = false;
                }
                disableColorSampling();
            }
        }
    }

    public void onSwitchUser(int i) {
        if (DEBUG) {
            Slog.d(TAG, "Used id updated from " + this.mCurrentUserId + " to " + i);
        }
        this.mCurrentUserId = i;
    }

    public ParceledListSlice<BrightnessChangeEvent> getEvents(int i, boolean z) {
        BrightnessChangeEvent[] brightnessChangeEventArr;
        synchronized (this.mEventsLock) {
            brightnessChangeEventArr = (BrightnessChangeEvent[]) this.mEvents.toArray();
        }
        int[] profileIds = this.mInjector.getProfileIds(this.mUserManager, i);
        HashMap hashMap = new HashMap();
        int i2 = 0;
        while (true) {
            boolean z2 = true;
            if (i2 >= profileIds.length) {
                break;
            }
            int i3 = profileIds[i2];
            if (z && i3 == i) {
                z2 = false;
            }
            hashMap.put(Integer.valueOf(i3), Boolean.valueOf(z2));
            i2++;
        }
        ArrayList arrayList = new ArrayList(brightnessChangeEventArr.length);
        for (int i4 = 0; i4 < brightnessChangeEventArr.length; i4++) {
            Boolean bool = (Boolean) hashMap.get(Integer.valueOf(brightnessChangeEventArr[i4].userId));
            if (bool != null) {
                if (!bool.booleanValue()) {
                    arrayList.add(brightnessChangeEventArr[i4]);
                } else {
                    arrayList.add(new BrightnessChangeEvent(brightnessChangeEventArr[i4], true));
                }
            }
        }
        return new ParceledListSlice<>(arrayList);
    }

    public void persistBrightnessTrackerState() {
        scheduleWriteBrightnessTrackerState();
    }

    public void notifyBrightnessChanged(float f, boolean z, float f2, boolean z2, boolean z3, String str, float[] fArr, long[] jArr) {
        if (DEBUG) {
            Slog.d(TAG, String.format("notifyBrightnessChanged(brightness=%f, userInitiated=%b)", Float.valueOf(f), Boolean.valueOf(z)));
        }
        this.mBgHandler.obtainMessage(1, z ? 1 : 0, 0, new BrightnessChangeValues(f, f2, z2, z3, this.mInjector.currentTimeMillis(), str, fArr, jArr)).sendToTarget();
    }

    public void setLightSensor(Sensor sensor) {
        this.mBgHandler.obtainMessage(5, 0, 0, sensor).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBrightnessChanged(float f, boolean z, float f2, boolean z2, boolean z3, long j, String str, float[] fArr, long[] jArr) {
        DisplayedContentSample sampleColor;
        synchronized (this.mDataCollectionLock) {
            if (this.mStarted) {
                float f3 = this.mLastBrightness;
                this.mLastBrightness = f;
                if (z) {
                    BrightnessChangeEvent.Builder builder = new BrightnessChangeEvent.Builder();
                    builder.setBrightness(f);
                    builder.setTimeStamp(j);
                    builder.setPowerBrightnessFactor(f2);
                    builder.setUserBrightnessPoint(z2);
                    builder.setIsDefaultBrightnessConfig(z3);
                    builder.setUniqueDisplayId(str);
                    if (fArr.length == 0) {
                        return;
                    }
                    long[] jArr2 = new long[jArr.length];
                    long currentTimeMillis = this.mInjector.currentTimeMillis();
                    long elapsedRealtimeNanos = this.mInjector.elapsedRealtimeNanos();
                    for (int i = 0; i < jArr.length; i++) {
                        jArr2[i] = currentTimeMillis - (TimeUnit.NANOSECONDS.toMillis(elapsedRealtimeNanos) - jArr[i]);
                    }
                    builder.setLuxValues(fArr);
                    builder.setLuxTimestamps(jArr2);
                    builder.setBatteryLevel(this.mLastBatteryLevel);
                    builder.setLastBrightness(f3);
                    try {
                        ActivityTaskManager.RootTaskInfo focusedStack = this.mInjector.getFocusedStack();
                        if (focusedStack != null && focusedStack.topActivity != null) {
                            builder.setUserId(focusedStack.userId);
                            builder.setPackageName(focusedStack.topActivity.getPackageName());
                            builder.setNightMode(this.mInjector.isNightDisplayActivated(this.mContext));
                            builder.setColorTemperature(this.mInjector.getNightDisplayColorTemperature(this.mContext));
                            builder.setReduceBrightColors(this.mInjector.isReduceBrightColorsActivated(this.mContext));
                            builder.setReduceBrightColorsStrength(this.mInjector.getReduceBrightColorsStrength(this.mContext));
                            builder.setReduceBrightColorsOffset(this.mInjector.getReduceBrightColorsOffsetFactor(this.mContext) * f);
                            if (this.mColorSamplingEnabled && (sampleColor = this.mInjector.sampleColor(this.mNoFramesToSample)) != null && sampleColor.getSampleComponent(DisplayedContentSample.ColorComponent.CHANNEL2) != null) {
                                builder.setColorValues(sampleColor.getSampleComponent(DisplayedContentSample.ColorComponent.CHANNEL2), Math.round((((float) sampleColor.getNumFrames()) / this.mFrameRate) * 1000.0f));
                            }
                            BrightnessChangeEvent build = builder.build();
                            if (DEBUG) {
                                Slog.d(TAG, "Event: " + build.toString());
                            }
                            synchronized (this.mEventsLock) {
                                this.mEventsDirty = true;
                                this.mEvents.append(build);
                            }
                            return;
                        }
                        if (DEBUG) {
                            Slog.d(TAG, "Ignoring event due to null focusedTask.");
                        }
                    } catch (RemoteException unused) {
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSensorChanged(Sensor sensor) {
        if (this.mLightSensor != sensor) {
            this.mLightSensor = sensor;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleWriteBrightnessTrackerState() {
        if (this.mWriteBrightnessTrackerStateScheduled) {
            return;
        }
        this.mBgHandler.post(new Runnable() { // from class: com.android.server.display.BrightnessTracker$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                BrightnessTracker.this.lambda$scheduleWriteBrightnessTrackerState$0();
            }
        });
        this.mWriteBrightnessTrackerStateScheduled = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleWriteBrightnessTrackerState$0() {
        this.mWriteBrightnessTrackerStateScheduled = false;
        writeEvents();
        writeAmbientBrightnessStats();
    }

    private void writeEvents() {
        FileOutputStream fileOutputStream;
        synchronized (this.mEventsLock) {
            if (this.mEventsDirty) {
                AtomicFile file = this.mInjector.getFile(EVENTS_FILE);
                if (file == null) {
                    return;
                }
                if (this.mEvents.isEmpty()) {
                    if (file.exists()) {
                        file.delete();
                    }
                    this.mEventsDirty = false;
                } else {
                    try {
                        fileOutputStream = file.startWrite();
                        try {
                            writeEventsLocked(fileOutputStream);
                            file.finishWrite(fileOutputStream);
                            this.mEventsDirty = false;
                        } catch (IOException e) {
                            e = e;
                            file.failWrite(fileOutputStream);
                            Slog.e(TAG, "Failed to write change mEvents.", e);
                        }
                    } catch (IOException e2) {
                        e = e2;
                        fileOutputStream = null;
                    }
                }
            }
        }
    }

    private void writeAmbientBrightnessStats() {
        FileOutputStream fileOutputStream;
        AtomicFile file = this.mInjector.getFile(AMBIENT_BRIGHTNESS_STATS_FILE);
        if (file == null) {
            return;
        }
        try {
            fileOutputStream = file.startWrite();
            try {
                this.mAmbientBrightnessStatsTracker.writeStats(fileOutputStream);
                file.finishWrite(fileOutputStream);
            } catch (IOException e) {
                e = e;
                file.failWrite(fileOutputStream);
                Slog.e(TAG, "Failed to write ambient brightness stats.", e);
            }
        } catch (IOException e2) {
            e = e2;
            fileOutputStream = null;
        }
    }

    private AtomicFile getFileWithLegacyFallback(String str) {
        AtomicFile legacyFile;
        AtomicFile file = this.mInjector.getFile(str);
        if (file == null || file.exists() || (legacyFile = this.mInjector.getLegacyFile(str)) == null || !legacyFile.exists()) {
            return file;
        }
        Slog.i(TAG, "Reading " + str + " from old location");
        return legacyFile;
    }

    private void readEvents() {
        synchronized (this.mEventsLock) {
            this.mEventsDirty = true;
            this.mEvents.clear();
            AtomicFile fileWithLegacyFallback = getFileWithLegacyFallback(EVENTS_FILE);
            if (fileWithLegacyFallback != null && fileWithLegacyFallback.exists()) {
                FileInputStream fileInputStream = null;
                try {
                    try {
                        fileInputStream = fileWithLegacyFallback.openRead();
                        readEventsLocked(fileInputStream);
                    } catch (IOException e) {
                        fileWithLegacyFallback.delete();
                        Slog.e(TAG, "Failed to read change mEvents.", e);
                    }
                } finally {
                    IoUtils.closeQuietly(fileInputStream);
                }
            }
        }
    }

    private void readAmbientBrightnessStats() {
        FileInputStream fileInputStream = null;
        this.mAmbientBrightnessStatsTracker = new AmbientBrightnessStatsTracker(this.mUserManager, null);
        AtomicFile fileWithLegacyFallback = getFileWithLegacyFallback(AMBIENT_BRIGHTNESS_STATS_FILE);
        if (fileWithLegacyFallback == null || !fileWithLegacyFallback.exists()) {
            return;
        }
        try {
            try {
                fileInputStream = fileWithLegacyFallback.openRead();
                this.mAmbientBrightnessStatsTracker.readStats(fileInputStream);
            } catch (IOException e) {
                fileWithLegacyFallback.delete();
                Slog.e(TAG, "Failed to read ambient brightness stats.", e);
            }
        } finally {
            IoUtils.closeQuietly(fileInputStream);
        }
    }

    @GuardedBy({"mEventsLock"})
    @VisibleForTesting
    void writeEventsLocked(OutputStream outputStream) throws IOException {
        TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(outputStream);
        resolveSerializer.startDocument((String) null, Boolean.TRUE);
        resolveSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        resolveSerializer.startTag((String) null, TAG_EVENTS);
        BrightnessChangeEvent[] brightnessChangeEventArr = (BrightnessChangeEvent[]) this.mEvents.toArray();
        this.mEvents.clear();
        if (DEBUG) {
            Slog.d(TAG, "Writing events " + brightnessChangeEventArr.length);
        }
        long currentTimeMillis = this.mInjector.currentTimeMillis() - MAX_EVENT_AGE;
        for (int i = 0; i < brightnessChangeEventArr.length; i++) {
            int userSerialNumber = this.mInjector.getUserSerialNumber(this.mUserManager, brightnessChangeEventArr[i].userId);
            if (userSerialNumber != -1) {
                BrightnessChangeEvent brightnessChangeEvent = brightnessChangeEventArr[i];
                if (brightnessChangeEvent.timeStamp > currentTimeMillis) {
                    this.mEvents.append(brightnessChangeEvent);
                    resolveSerializer.startTag((String) null, TAG_EVENT);
                    resolveSerializer.attributeFloat((String) null, ATTR_NITS, brightnessChangeEventArr[i].brightness);
                    resolveSerializer.attributeLong((String) null, ATTR_TIMESTAMP, brightnessChangeEventArr[i].timeStamp);
                    resolveSerializer.attribute((String) null, ATTR_PACKAGE_NAME, brightnessChangeEventArr[i].packageName);
                    resolveSerializer.attributeInt((String) null, ATTR_USER, userSerialNumber);
                    String str = brightnessChangeEventArr[i].uniqueDisplayId;
                    if (str == null) {
                        str = "";
                    }
                    resolveSerializer.attribute((String) null, ATTR_UNIQUE_DISPLAY_ID, str);
                    resolveSerializer.attributeFloat((String) null, ATTR_BATTERY_LEVEL, brightnessChangeEventArr[i].batteryLevel);
                    resolveSerializer.attributeBoolean((String) null, ATTR_NIGHT_MODE, brightnessChangeEventArr[i].nightMode);
                    resolveSerializer.attributeInt((String) null, ATTR_COLOR_TEMPERATURE, brightnessChangeEventArr[i].colorTemperature);
                    resolveSerializer.attributeBoolean((String) null, ATTR_REDUCE_BRIGHT_COLORS, brightnessChangeEventArr[i].reduceBrightColors);
                    resolveSerializer.attributeInt((String) null, ATTR_REDUCE_BRIGHT_COLORS_STRENGTH, brightnessChangeEventArr[i].reduceBrightColorsStrength);
                    resolveSerializer.attributeFloat((String) null, ATTR_REDUCE_BRIGHT_COLORS_OFFSET, brightnessChangeEventArr[i].reduceBrightColorsOffset);
                    resolveSerializer.attributeFloat((String) null, ATTR_LAST_NITS, brightnessChangeEventArr[i].lastBrightness);
                    resolveSerializer.attributeBoolean((String) null, ATTR_DEFAULT_CONFIG, brightnessChangeEventArr[i].isDefaultBrightnessConfig);
                    resolveSerializer.attributeFloat((String) null, ATTR_POWER_SAVE, brightnessChangeEventArr[i].powerBrightnessFactor);
                    resolveSerializer.attributeBoolean((String) null, ATTR_USER_POINT, brightnessChangeEventArr[i].isUserSetBrightness);
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    for (int i2 = 0; i2 < brightnessChangeEventArr[i].luxValues.length; i2++) {
                        if (i2 > 0) {
                            sb.append(',');
                            sb2.append(',');
                        }
                        sb.append(Float.toString(brightnessChangeEventArr[i].luxValues[i2]));
                        sb2.append(Long.toString(brightnessChangeEventArr[i].luxTimestamps[i2]));
                    }
                    resolveSerializer.attribute((String) null, ATTR_LUX, sb.toString());
                    resolveSerializer.attribute((String) null, ATTR_LUX_TIMESTAMPS, sb2.toString());
                    BrightnessChangeEvent brightnessChangeEvent2 = brightnessChangeEventArr[i];
                    long[] jArr = brightnessChangeEvent2.colorValueBuckets;
                    if (jArr != null && jArr.length > 0) {
                        resolveSerializer.attributeLong((String) null, ATTR_COLOR_SAMPLE_DURATION, brightnessChangeEvent2.colorSampleDuration);
                        StringBuilder sb3 = new StringBuilder();
                        for (int i3 = 0; i3 < brightnessChangeEventArr[i].colorValueBuckets.length; i3++) {
                            if (i3 > 0) {
                                sb3.append(',');
                            }
                            sb3.append(Long.toString(brightnessChangeEventArr[i].colorValueBuckets[i3]));
                        }
                        resolveSerializer.attribute((String) null, ATTR_COLOR_VALUE_BUCKETS, sb3.toString());
                    }
                    resolveSerializer.endTag((String) null, TAG_EVENT);
                }
            }
        }
        resolveSerializer.endTag((String) null, TAG_EVENTS);
        resolveSerializer.endDocument();
        outputStream.flush();
    }

    @GuardedBy({"mEventsLock"})
    @VisibleForTesting
    void readEventsLocked(InputStream inputStream) throws IOException {
        int next;
        int i;
        try {
            TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(inputStream);
            do {
                next = resolvePullParser.next();
                i = 1;
                if (next == 1) {
                    break;
                }
            } while (next != 2);
            String name = resolvePullParser.getName();
            if (!TAG_EVENTS.equals(name)) {
                throw new XmlPullParserException("Events not found in brightness tracker file " + name);
            }
            long currentTimeMillis = this.mInjector.currentTimeMillis() - MAX_EVENT_AGE;
            int depth = resolvePullParser.getDepth();
            while (true) {
                int next2 = resolvePullParser.next();
                if (next2 == i) {
                    return;
                }
                if (next2 == 3 && resolvePullParser.getDepth() <= depth) {
                    return;
                }
                if (next2 != 3 && next2 != 4 && TAG_EVENT.equals(resolvePullParser.getName())) {
                    BrightnessChangeEvent.Builder builder = new BrightnessChangeEvent.Builder();
                    builder.setBrightness(resolvePullParser.getAttributeFloat((String) null, ATTR_NITS));
                    builder.setTimeStamp(resolvePullParser.getAttributeLong((String) null, ATTR_TIMESTAMP));
                    builder.setPackageName(resolvePullParser.getAttributeValue((String) null, ATTR_PACKAGE_NAME));
                    builder.setUserId(this.mInjector.getUserId(this.mUserManager, resolvePullParser.getAttributeInt((String) null, ATTR_USER)));
                    String attributeValue = resolvePullParser.getAttributeValue((String) null, ATTR_UNIQUE_DISPLAY_ID);
                    if (attributeValue == null) {
                        attributeValue = "";
                    }
                    builder.setUniqueDisplayId(attributeValue);
                    builder.setBatteryLevel(resolvePullParser.getAttributeFloat((String) null, ATTR_BATTERY_LEVEL));
                    builder.setNightMode(resolvePullParser.getAttributeBoolean((String) null, ATTR_NIGHT_MODE));
                    builder.setColorTemperature(resolvePullParser.getAttributeInt((String) null, ATTR_COLOR_TEMPERATURE));
                    builder.setReduceBrightColors(resolvePullParser.getAttributeBoolean((String) null, ATTR_REDUCE_BRIGHT_COLORS));
                    builder.setReduceBrightColorsStrength(resolvePullParser.getAttributeInt((String) null, ATTR_REDUCE_BRIGHT_COLORS_STRENGTH));
                    builder.setReduceBrightColorsOffset(resolvePullParser.getAttributeFloat((String) null, ATTR_REDUCE_BRIGHT_COLORS_OFFSET));
                    builder.setLastBrightness(resolvePullParser.getAttributeFloat((String) null, ATTR_LAST_NITS));
                    String attributeValue2 = resolvePullParser.getAttributeValue((String) null, ATTR_LUX);
                    String attributeValue3 = resolvePullParser.getAttributeValue((String) null, ATTR_LUX_TIMESTAMPS);
                    String[] split = attributeValue2.split(",");
                    String[] split2 = attributeValue3.split(",");
                    if (split.length == split2.length) {
                        int length = split.length;
                        float[] fArr = new float[length];
                        long[] jArr = new long[split.length];
                        for (int i2 = 0; i2 < length; i2++) {
                            fArr[i2] = Float.parseFloat(split[i2]);
                            jArr[i2] = Long.parseLong(split2[i2]);
                        }
                        builder.setLuxValues(fArr);
                        builder.setLuxTimestamps(jArr);
                        builder.setIsDefaultBrightnessConfig(resolvePullParser.getAttributeBoolean((String) null, ATTR_DEFAULT_CONFIG, false));
                        builder.setPowerBrightnessFactor(resolvePullParser.getAttributeFloat((String) null, ATTR_POWER_SAVE, 1.0f));
                        builder.setUserBrightnessPoint(resolvePullParser.getAttributeBoolean((String) null, ATTR_USER_POINT, false));
                        long attributeLong = resolvePullParser.getAttributeLong((String) null, ATTR_COLOR_SAMPLE_DURATION, -1L);
                        String attributeValue4 = resolvePullParser.getAttributeValue((String) null, ATTR_COLOR_VALUE_BUCKETS);
                        if (attributeLong != -1 && attributeValue4 != null) {
                            String[] split3 = attributeValue4.split(",");
                            int length2 = split3.length;
                            long[] jArr2 = new long[length2];
                            for (int i3 = 0; i3 < length2; i3++) {
                                jArr2[i3] = Long.parseLong(split3[i3]);
                            }
                            builder.setColorValues(jArr2, attributeLong);
                        }
                        BrightnessChangeEvent build = builder.build();
                        if (DEBUG) {
                            Slog.i(TAG, "Read event " + build.brightness + " " + build.packageName);
                        }
                        if (build.userId != -1 && build.timeStamp > currentTimeMillis && build.luxValues.length > 0) {
                            this.mEvents.append(build);
                        }
                        i = 1;
                    }
                }
            }
        } catch (IOException | NullPointerException | NumberFormatException | XmlPullParserException e) {
            this.mEvents = new RingBuffer<>(BrightnessChangeEvent.class, 100);
            Slog.e(TAG, "Failed to parse brightness event", e);
            throw new IOException("failed to parse file", e);
        }
    }

    public void dump(final PrintWriter printWriter) {
        printWriter.println("BrightnessTracker state:");
        synchronized (this.mDataCollectionLock) {
            printWriter.println("  mStarted=" + this.mStarted);
            printWriter.println("  mLightSensor=" + this.mLightSensor);
            printWriter.println("  mLastBatteryLevel=" + this.mLastBatteryLevel);
            printWriter.println("  mLastBrightness=" + this.mLastBrightness);
        }
        synchronized (this.mEventsLock) {
            printWriter.println("  mEventsDirty=" + this.mEventsDirty);
            printWriter.println("  mEvents.size=" + this.mEvents.size());
            BrightnessChangeEvent[] brightnessChangeEventArr = (BrightnessChangeEvent[]) this.mEvents.toArray();
            for (int i = 0; i < brightnessChangeEventArr.length; i++) {
                printWriter.print("    " + FORMAT.format(new Date(brightnessChangeEventArr[i].timeStamp)));
                printWriter.print(", userId=" + brightnessChangeEventArr[i].userId);
                printWriter.print(", " + brightnessChangeEventArr[i].lastBrightness + "->" + brightnessChangeEventArr[i].brightness);
                StringBuilder sb = new StringBuilder();
                sb.append(", isUserSetBrightness=");
                sb.append(brightnessChangeEventArr[i].isUserSetBrightness);
                printWriter.print(sb.toString());
                printWriter.print(", powerBrightnessFactor=" + brightnessChangeEventArr[i].powerBrightnessFactor);
                printWriter.print(", isDefaultBrightnessConfig=" + brightnessChangeEventArr[i].isDefaultBrightnessConfig);
                printWriter.print(", recent lux values=");
                printWriter.print(" {");
                for (int i2 = 0; i2 < brightnessChangeEventArr[i].luxValues.length; i2++) {
                    if (i2 != 0) {
                        printWriter.print(", ");
                    }
                    printWriter.print("(" + brightnessChangeEventArr[i].luxValues[i2] + "," + brightnessChangeEventArr[i].luxTimestamps[i2] + ")");
                }
                printWriter.println("}");
            }
        }
        printWriter.println("  mWriteBrightnessTrackerStateScheduled=" + this.mWriteBrightnessTrackerStateScheduled);
        this.mBgHandler.runWithScissors(new Runnable() { // from class: com.android.server.display.BrightnessTracker$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BrightnessTracker.this.lambda$dump$1(printWriter);
            }
        }, 1000L);
        if (this.mAmbientBrightnessStatsTracker != null) {
            printWriter.println();
            this.mAmbientBrightnessStatsTracker.dump(printWriter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: dumpLocal, reason: merged with bridge method [inline-methods] */
    public void lambda$dump$1(PrintWriter printWriter) {
        printWriter.println("  mSensorRegistered=" + this.mSensorRegistered);
        printWriter.println("  mColorSamplingEnabled=" + this.mColorSamplingEnabled);
        printWriter.println("  mNoFramesToSample=" + this.mNoFramesToSample);
        printWriter.println("  mFrameRate=" + this.mFrameRate);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableColorSampling() {
        if (this.mInjector.isBrightnessModeAutomatic(this.mContentResolver) && this.mInjector.isInteractive(this.mContext) && !this.mColorSamplingEnabled && this.mShouldCollectColorSample) {
            float frameRate = this.mInjector.getFrameRate(this.mContext);
            this.mFrameRate = frameRate;
            if (frameRate <= 0.0f) {
                Slog.wtf(TAG, "Default display has a zero or negative framerate.");
                return;
            }
            this.mNoFramesToSample = (int) (frameRate * ((float) COLOR_SAMPLE_DURATION));
            DisplayedContentSamplingAttributes samplingAttributes = this.mInjector.getSamplingAttributes();
            boolean z = DEBUG;
            if (z && samplingAttributes != null) {
                Slog.d(TAG, "Color sampling mask=0x" + Integer.toHexString(samplingAttributes.getComponentMask()) + " dataSpace=0x" + Integer.toHexString(samplingAttributes.getDataspace()) + " pixelFormat=0x" + Integer.toHexString(samplingAttributes.getPixelFormat()));
            }
            if (samplingAttributes != null && samplingAttributes.getPixelFormat() == 55 && (samplingAttributes.getComponentMask() & 4) != 0) {
                this.mColorSamplingEnabled = this.mInjector.enableColorSampling(true, this.mNoFramesToSample);
                if (z) {
                    Slog.i(TAG, "turning on color sampling for " + this.mNoFramesToSample + " frames, success=" + this.mColorSamplingEnabled);
                }
            }
            if (this.mColorSamplingEnabled && this.mDisplayListener == null) {
                DisplayListener displayListener = new DisplayListener();
                this.mDisplayListener = displayListener;
                this.mInjector.registerDisplayListener(this.mContext, displayListener, this.mBgHandler);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableColorSampling() {
        if (this.mColorSamplingEnabled) {
            this.mInjector.enableColorSampling(false, 0);
            this.mColorSamplingEnabled = false;
            DisplayListener displayListener = this.mDisplayListener;
            if (displayListener != null) {
                this.mInjector.unRegisterDisplayListener(this.mContext, displayListener);
                this.mDisplayListener = null;
            }
            if (DEBUG) {
                Slog.i(TAG, "turning off color sampling");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateColorSampling() {
        if (this.mColorSamplingEnabled && this.mInjector.getFrameRate(this.mContext) != this.mFrameRate) {
            disableColorSampling();
            enableColorSampling();
        }
    }

    public ParceledListSlice<AmbientBrightnessDayStats> getAmbientBrightnessStats(int i) {
        ArrayList<AmbientBrightnessDayStats> userStats;
        AmbientBrightnessStatsTracker ambientBrightnessStatsTracker = this.mAmbientBrightnessStatsTracker;
        if (ambientBrightnessStatsTracker != null && (userStats = ambientBrightnessStatsTracker.getUserStats(i)) != null) {
            return new ParceledListSlice<>(userStats);
        }
        return ParceledListSlice.emptyList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordAmbientBrightnessStats(SensorEvent sensorEvent) {
        this.mAmbientBrightnessStatsTracker.add(this.mCurrentUserId, sensorEvent.values[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void batteryLevelChanged(int i, int i2) {
        synchronized (this.mDataCollectionLock) {
            this.mLastBatteryLevel = i / i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class SensorListener implements SensorEventListener {
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        private SensorListener() {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            BrightnessTracker.this.recordAmbientBrightnessStats(sensorEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DisplayListener implements DisplayManager.DisplayListener {
        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int i) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int i) {
        }

        private DisplayListener() {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int i) {
            if (i == 0) {
                BrightnessTracker.this.updateColorSampling();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class SettingsObserver extends ContentObserver {
        public SettingsObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            if (BrightnessTracker.DEBUG) {
                Slog.v(BrightnessTracker.TAG, "settings change " + uri);
            }
            if (BrightnessTracker.this.mInjector.isBrightnessModeAutomatic(BrightnessTracker.this.mContentResolver)) {
                BrightnessTracker.this.mBgHandler.obtainMessage(3).sendToTarget();
            } else {
                BrightnessTracker.this.mBgHandler.obtainMessage(2).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class Receiver extends BroadcastReceiver {
        private Receiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (BrightnessTracker.DEBUG) {
                Slog.d(BrightnessTracker.TAG, "Received " + intent.getAction());
            }
            String action = intent.getAction();
            if ("android.intent.action.ACTION_SHUTDOWN".equals(action)) {
                BrightnessTracker.this.stop();
                BrightnessTracker.this.scheduleWriteBrightnessTrackerState();
                return;
            }
            if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                int intExtra = intent.getIntExtra("level", -1);
                int intExtra2 = intent.getIntExtra("scale", 0);
                if (intExtra == -1 || intExtra2 == 0) {
                    return;
                }
                BrightnessTracker.this.batteryLevelChanged(intExtra, intExtra2);
                return;
            }
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                BrightnessTracker.this.mBgHandler.obtainMessage(2).sendToTarget();
            } else if ("android.intent.action.SCREEN_ON".equals(action)) {
                BrightnessTracker.this.mBgHandler.obtainMessage(3).sendToTarget();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class TrackerHandler extends Handler {
        public TrackerHandler(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                BrightnessTracker.this.backgroundStart(((Float) message.obj).floatValue());
                return;
            }
            if (i == 1) {
                BrightnessChangeValues brightnessChangeValues = (BrightnessChangeValues) message.obj;
                BrightnessTracker.this.handleBrightnessChanged(brightnessChangeValues.brightness, message.arg1 == 1, brightnessChangeValues.powerBrightnessFactor, brightnessChangeValues.wasShortTermModelActive, brightnessChangeValues.isDefaultBrightnessConfig, brightnessChangeValues.timestamp, brightnessChangeValues.uniqueDisplayId, brightnessChangeValues.luxValues, brightnessChangeValues.luxTimestamps);
                return;
            }
            if (i == 2) {
                BrightnessTracker.this.disableColorSampling();
                return;
            }
            if (i == 3) {
                BrightnessTracker.this.enableColorSampling();
                return;
            }
            if (i != 4) {
                if (i != 5) {
                    return;
                }
                BrightnessTracker.this.handleSensorChanged((Sensor) message.obj);
                return;
            }
            BrightnessTracker.this.mShouldCollectColorSample = ((Boolean) message.obj).booleanValue();
            if (BrightnessTracker.this.mShouldCollectColorSample && !BrightnessTracker.this.mColorSamplingEnabled) {
                BrightnessTracker.this.enableColorSampling();
            } else {
                if (BrightnessTracker.this.mShouldCollectColorSample || !BrightnessTracker.this.mColorSamplingEnabled) {
                    return;
                }
                BrightnessTracker.this.disableColorSampling();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static class BrightnessChangeValues {
        public final float brightness;
        public final boolean isDefaultBrightnessConfig;
        public final long[] luxTimestamps;
        public final float[] luxValues;
        public final float powerBrightnessFactor;
        public final long timestamp;
        public final String uniqueDisplayId;
        public final boolean wasShortTermModelActive;

        BrightnessChangeValues(float f, float f2, boolean z, boolean z2, long j, String str, float[] fArr, long[] jArr) {
            this.brightness = f;
            this.powerBrightnessFactor = f2;
            this.wasShortTermModelActive = z;
            this.isDefaultBrightnessConfig = z2;
            this.timestamp = j;
            this.uniqueDisplayId = str;
            this.luxValues = fArr;
            this.luxTimestamps = jArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Injector {
        Injector() {
        }

        public void registerSensorListener(Context context, SensorEventListener sensorEventListener, Sensor sensor, Handler handler) {
            ((SensorManager) context.getSystemService(SensorManager.class)).registerListener(sensorEventListener, sensor, 3, handler);
        }

        public void unregisterSensorListener(Context context, SensorEventListener sensorEventListener) {
            ((SensorManager) context.getSystemService(SensorManager.class)).unregisterListener(sensorEventListener);
        }

        public void registerBrightnessModeObserver(ContentResolver contentResolver, ContentObserver contentObserver) {
            contentResolver.registerContentObserver(Settings.System.getUriFor("screen_brightness_mode"), false, contentObserver, -1);
        }

        public void unregisterBrightnessModeObserver(Context context, ContentObserver contentObserver) {
            context.getContentResolver().unregisterContentObserver(contentObserver);
        }

        public void registerReceiver(Context context, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
            context.registerReceiver(broadcastReceiver, intentFilter, 2);
        }

        public void unregisterReceiver(Context context, BroadcastReceiver broadcastReceiver) {
            context.unregisterReceiver(broadcastReceiver);
        }

        public Handler getBackgroundHandler() {
            return BackgroundThread.getHandler();
        }

        public boolean isBrightnessModeAutomatic(ContentResolver contentResolver) {
            return Settings.System.getIntForUser(contentResolver, "screen_brightness_mode", 0, -2) == 1;
        }

        public int getSecureIntForUser(ContentResolver contentResolver, String str, int i, int i2) {
            return Settings.Secure.getIntForUser(contentResolver, str, i, i2);
        }

        public AtomicFile getFile(String str) {
            return new AtomicFile(new File(Environment.getDataSystemDirectory(), str));
        }

        public AtomicFile getLegacyFile(String str) {
            return new AtomicFile(new File(Environment.getDataSystemDeDirectory(), str));
        }

        public long currentTimeMillis() {
            return System.currentTimeMillis();
        }

        public long elapsedRealtimeNanos() {
            return SystemClock.elapsedRealtimeNanos();
        }

        public int getUserSerialNumber(UserManager userManager, int i) {
            return userManager.getUserSerialNumber(i);
        }

        public int getUserId(UserManager userManager, int i) {
            return userManager.getUserHandle(i);
        }

        public int[] getProfileIds(UserManager userManager, int i) {
            if (userManager != null) {
                return userManager.getProfileIds(i, false);
            }
            return new int[]{i};
        }

        public ActivityTaskManager.RootTaskInfo getFocusedStack() throws RemoteException {
            return ActivityTaskManager.getService().getFocusedRootTaskInfo();
        }

        public void scheduleIdleJob(Context context) {
            BrightnessIdleJob.scheduleJob(context);
        }

        public void cancelIdleJob(Context context) {
            BrightnessIdleJob.cancelJob(context);
        }

        public boolean isInteractive(Context context) {
            return ((PowerManager) context.getSystemService(PowerManager.class)).isInteractive();
        }

        public int getNightDisplayColorTemperature(Context context) {
            return ((ColorDisplayManager) context.getSystemService(ColorDisplayManager.class)).getNightDisplayColorTemperature();
        }

        public boolean isNightDisplayActivated(Context context) {
            return ((ColorDisplayManager) context.getSystemService(ColorDisplayManager.class)).isNightDisplayActivated();
        }

        public int getReduceBrightColorsStrength(Context context) {
            return ((ColorDisplayManager) context.getSystemService(ColorDisplayManager.class)).getReduceBrightColorsStrength();
        }

        public float getReduceBrightColorsOffsetFactor(Context context) {
            return ((ColorDisplayManager) context.getSystemService(ColorDisplayManager.class)).getReduceBrightColorsOffsetFactor();
        }

        public boolean isReduceBrightColorsActivated(Context context) {
            return ((ColorDisplayManager) context.getSystemService(ColorDisplayManager.class)).isReduceBrightColorsActivated();
        }

        public DisplayedContentSample sampleColor(int i) {
            return ((DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class)).getDisplayedContentSample(0, i, 0L);
        }

        public float getFrameRate(Context context) {
            return ((DisplayManager) context.getSystemService(DisplayManager.class)).getDisplay(0).getRefreshRate();
        }

        public DisplayedContentSamplingAttributes getSamplingAttributes() {
            return ((DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class)).getDisplayedContentSamplingAttributes(0);
        }

        public boolean enableColorSampling(boolean z, int i) {
            return ((DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class)).setDisplayedContentSamplingEnabled(0, z, 4, i);
        }

        public void registerDisplayListener(Context context, DisplayManager.DisplayListener displayListener, Handler handler) {
            ((DisplayManager) context.getSystemService(DisplayManager.class)).registerDisplayListener(displayListener, handler);
        }

        public void unRegisterDisplayListener(Context context, DisplayManager.DisplayListener displayListener) {
            ((DisplayManager) context.getSystemService(DisplayManager.class)).unregisterDisplayListener(displayListener);
        }
    }
}
