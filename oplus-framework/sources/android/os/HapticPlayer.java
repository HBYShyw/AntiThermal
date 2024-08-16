package android.os;

import android.app.ActivityThread;
import android.os.IVibratorManagerService;
import android.os.VibrationAttributes;
import android.text.TextUtils;
import android.util.Log;
import com.oplus.widget.OplusMaxLinearLayout;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class HapticPlayer {
    public static final int ANDROID_VERSIONCODE_O = 26;
    public static final int CONTINUOUS_EVENT = 4096;
    public static final String EVENT_KEY_DURATION = "Duration";
    public static final String EVENT_KEY_EVENT = "Event";
    public static final String EVENT_KEY_HE_CURVE = "Curve";
    public static final String EVENT_KEY_HE_CURVE_POINT_TIME = "Time";
    public static final String EVENT_KEY_HE_FREQUENCY = "Frequency";
    public static final String EVENT_KEY_HE_INTENSITY = "Intensity";
    public static final String EVENT_KEY_HE_PARAMETERS = "Parameters";
    public static final String EVENT_KEY_HE_TYPE = "Type";
    public static final String EVENT_KEY_RELATIVE_TIME = "RelativeTime";
    public static final String EVENT_TYPE_HE_CONTINUOUS_NAME = "continuous";
    public static final String EVENT_TYPE_HE_TRANSIENT_NAME = "transient";
    public static final int FORMAT_VERSION = 2;
    public static final int HE2_0_PATTERN_WRAP_NUM = 10;
    public static final int HE_CURVE_POINT_0_FREQUENCY = 9;
    public static final int HE_CURVE_POINT_0_INTENSITY = 8;
    public static final int HE_CURVE_POINT_0_TIME = 7;
    public static final int HE_DEFAULT_DURATION = 0;
    public static final int HE_DEFAULT_RELATIVE_TIME = 400;
    public static final int HE_DURATION = 4;
    public static final int HE_FREQUENCY = 3;
    public static final int HE_INTENSITY = 2;
    private static final String HE_META_DATA_KEY = "Metadata";
    public static final int HE_POINT_COUNT = 6;
    public static final int HE_RELATIVE_TIME = 1;
    public static final int HE_TYPE = 0;
    public static final int HE_VALUE_LENGTH = 55;
    private static final String HE_VERSION_KEY = "Version";
    public static final int HE_VIB_INDEX = 5;
    private static final int MAX_EVENT_COUNT = 16;
    private static final int MAX_FREQ = 100;
    private static final int MAX_INTENSITY = 100;
    private static final int MAX_PATERN_EVENT_LAST_TIME = 5000;
    private static final int MAX_PATERN_LAST_TIME = 50000;
    private static final int MAX_POINT_COUNT = 16;
    public static final String PATTERN_KEY_EVENT_VIB_ID = "Index";
    public static final String PATTERN_KEY_PATTERN = "Pattern";
    public static final String PATTERN_KEY_PATTERN_ABS_TIME = "AbsoluteTime";
    private static final String PATTERN_KEY_PATTERN_LIST = "PatternList";
    private static final String TAG = "HapticPlayer";
    public static final int TRANSIENT_EVENT = 4097;
    private static final int VIBRATION_EFFECT_SUPPORT_NO = 2;
    private final boolean DEBUG;
    private DynamicEffect mEffect;
    private final String mPackageName;
    private final IVibratorManagerService mService;
    private boolean mStarted;
    private final Binder mToken;
    private static boolean mAvailable = isSupportRichtap();
    private static ExecutorService mExcutor = Executors.newSingleThreadExecutor();
    private static AtomicInteger mSeq = new AtomicInteger();
    private static int mRichtapMajorVersion = 0;

    private HapticPlayer() {
        this.DEBUG = false;
        this.mToken = new Binder();
        this.mStarted = false;
        this.mPackageName = ActivityThread.currentPackageName();
        this.mService = IVibratorManagerService.Stub.asInterface(ServiceManager.getService("vibrator_manager"));
    }

    public HapticPlayer(DynamicEffect effect) {
        this();
        this.mEffect = effect;
    }

    private boolean isInTheInterval(int data, int a, int b) {
        return data >= a && data <= b;
    }

    private static boolean isSupportRichtap() {
        int support = RichTapVibrationEffect.checkIfRichTapSupport();
        if (support == 2) {
            return false;
        }
        Log.d(TAG, "Support Richtap");
        return true;
    }

    public static boolean isAvailable() {
        return mAvailable;
    }

    public static int getMajorVersion() {
        int support = RichTapVibrationEffect.checkIfRichTapSupport();
        if (support == 2) {
            return 0;
        }
        int clientCode = (16711680 & support) >> 16;
        int majorVersion = (65280 & support) >> 8;
        int minorVersion = (support & 255) >> 0;
        Log.d(TAG, "clientCode:" + clientCode + " majorVersion:" + majorVersion + " minorVersion:" + minorVersion);
        return majorVersion;
    }

    public static int getMinorVersion() {
        int support = RichTapVibrationEffect.checkIfRichTapSupport();
        if (support == 2) {
            return 0;
        }
        int clientCode = (16711680 & support) >> 16;
        int majorVersion = (65280 & support) >> 8;
        int minorVersion = (support & 255) >> 0;
        Log.d(TAG, "clientCode:" + clientCode + " majorVersion:" + majorVersion + " minorVersion:" + minorVersion);
        return minorVersion;
    }

    private static boolean checkSdkSupport(int richTapMajorVersion, int richTapMinorVersion, int heVersion) {
        Log.d(TAG, "check richtap richTapMajorVersion:" + richTapMajorVersion + " heVersion:" + heVersion);
        if (richTapMajorVersion < 22) {
            Log.e(TAG, "can not support he in richtap version:" + String.format("%x", Integer.valueOf(richTapMajorVersion)));
            return false;
        }
        if (richTapMajorVersion == 22) {
            if (heVersion != 1) {
                Log.e(TAG, "RichTap version is " + String.format("%x", Integer.valueOf(richTapMajorVersion)) + " can not support he version: " + heVersion);
                return false;
            }
        } else if (richTapMajorVersion == 23 && heVersion != 1 && heVersion != 2) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:102:0x0138, code lost:
    
        android.util.Log.e(android.os.HapticPlayer.TAG, "duration must be less than 5000");
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x013d, code lost:
    
        r14 = false;
        r5 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x0281, code lost:
    
        android.util.Log.e(android.os.HapticPlayer.TAG, "intensity or frequency must between 0 and 100");
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x0286, code lost:
    
        r14 = false;
        r6 = r20;
        r5 = r35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x028c, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x00a2, code lost:
    
        android.util.Log.e(android.os.HapticPlayer.TAG, "relativeTime must between 0 and 50000");
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x00a7, code lost:
    
        r14 = false;
        r5 = r9;
        r28 = r11;
        r6 = r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x029c, code lost:
    
        r28 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x02a8, code lost:
    
        android.util.Log.e(android.os.HapticPlayer.TAG, "haven't get type value");
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x02ad, code lost:
    
        r14 = false;
        r5 = r18;
        r6 = r20;
     */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int[] getSerializationDataHe_1_0(String patternString) {
        JSONObject hapticObject;
        JSONArray pattern;
        int eventNumberTmp;
        int len;
        boolean isCompliance;
        int ind;
        int eventNumberTmp2;
        int durationLast;
        JSONObject eventObject;
        JSONObject hapticObject2;
        int type;
        JSONArray pattern2;
        int relativeTimeLast;
        String str;
        int relativeTimeLast2;
        String str2;
        String str3;
        String str4;
        int relativeTimeLast3;
        String str5 = EVENT_KEY_DURATION;
        String str6 = EVENT_KEY_HE_FREQUENCY;
        String str7 = EVENT_KEY_HE_INTENSITY;
        String str8 = EVENT_KEY_RELATIVE_TIME;
        int relativeTimeLast4 = 0;
        int durationLast2 = 0;
        int[] patternHeInfo = null;
        try {
            hapticObject = new JSONObject(patternString);
            pattern = hapticObject.getJSONArray(PATTERN_KEY_PATTERN);
            eventNumberTmp = Math.min(pattern.length(), 16);
            len = eventNumberTmp * 55;
            patternHeInfo = new int[len];
            isCompliance = true;
            ind = 0;
        } catch (Exception e) {
            e = e;
        }
        while (true) {
            if (ind >= eventNumberTmp) {
                eventNumberTmp2 = eventNumberTmp;
                break;
            }
            try {
                JSONObject patternObject = pattern.getJSONObject(ind);
                int relativeTimeLast5 = relativeTimeLast4;
                durationLast = durationLast2;
                try {
                    eventObject = patternObject.getJSONObject(EVENT_KEY_EVENT);
                    String name = eventObject.getString(EVENT_KEY_HE_TYPE);
                    hapticObject2 = hapticObject;
                    if (!TextUtils.equals(EVENT_TYPE_HE_CONTINUOUS_NAME, name)) {
                        if (!TextUtils.equals(EVENT_TYPE_HE_TRANSIENT_NAME, name)) {
                            break;
                        }
                        type = TRANSIENT_EVENT;
                    } else {
                        type = 4096;
                    }
                    if (eventObject.has(str8)) {
                        pattern2 = pattern;
                        relativeTimeLast = eventObject.getInt(str8);
                    } else {
                        pattern2 = pattern;
                        Log.e(TAG, "event:" + ind + " don't have relativeTime parameters,set default:" + (ind * 400));
                        relativeTimeLast = ind * 400;
                    }
                    str = str8;
                    try {
                    } catch (Exception e2) {
                        e = e2;
                    }
                } catch (Exception e3) {
                    e = e3;
                }
            } catch (Exception e4) {
                e = e4;
            }
            if (!isInTheInterval(relativeTimeLast, 0, MAX_PATERN_LAST_TIME)) {
                break;
            }
            JSONObject parametersObject = eventObject.getJSONObject(EVENT_KEY_HE_PARAMETERS);
            int intensity = parametersObject.getInt(str7);
            int frequency = parametersObject.getInt(str6);
            int len2 = len;
            boolean isCompliance2 = isCompliance;
            if (!isInTheInterval(intensity, 0, 100)) {
                relativeTimeLast2 = relativeTimeLast;
                eventNumberTmp2 = eventNumberTmp;
                break;
            }
            eventNumberTmp2 = eventNumberTmp;
            if (!isInTheInterval(frequency, 0, 100)) {
                relativeTimeLast2 = relativeTimeLast;
                break;
            }
            patternHeInfo[(ind * 55) + 0] = type;
            patternHeInfo[(ind * 55) + 1] = relativeTimeLast;
            patternHeInfo[(ind * 55) + 2] = intensity;
            patternHeInfo[(ind * 55) + 3] = frequency;
            if (4096 == type) {
                if (eventObject.has(str5)) {
                    durationLast2 = eventObject.getInt(str5);
                } else {
                    try {
                        Log.e(TAG, "event:" + ind + " don't have duration parameters,set default:0");
                        durationLast2 = 0;
                    } catch (Exception e5) {
                        e = e5;
                    }
                }
                try {
                    if (isInTheInterval(durationLast2, 0, 5000)) {
                        patternHeInfo[(ind * 55) + 4] = durationLast2;
                        patternHeInfo[(ind * 55) + 5] = 0;
                        JSONArray curve = parametersObject.getJSONArray(EVENT_KEY_HE_CURVE);
                        str2 = str5;
                        int pointCount = Math.min(curve.length(), 16);
                        patternHeInfo[(ind * 55) + 6] = pointCount;
                        int i = 0;
                        while (i < pointCount) {
                            JSONObject curveObject = curve.getJSONObject(i);
                            JSONObject parametersObject2 = parametersObject;
                            JSONObject eventObject2 = eventObject;
                            int pointTime = curveObject.getInt(EVENT_KEY_HE_CURVE_POINT_TIME);
                            int type2 = type;
                            relativeTimeLast3 = relativeTimeLast;
                            int pointIntensity = (int) (curveObject.getDouble(str7) * 100.0d);
                            try {
                                int pointFrequency = curveObject.getInt(str6);
                                str3 = str6;
                                if (i == 0) {
                                    if (pointTime == 0 && pointIntensity == 0) {
                                        str4 = str7;
                                        if (!isInTheInterval(pointFrequency, -100, 100)) {
                                        }
                                    } else {
                                        str4 = str7;
                                    }
                                    Log.e(TAG, "first point's time,  intensity must be 0, frequency must between -100 and 100");
                                    isCompliance = false;
                                    break;
                                }
                                str4 = str7;
                                if (i > 0 && i < pointCount - 1 && (!isInTheInterval(pointTime, 0, 5000) || !isInTheInterval(pointIntensity, 0, 100) || !isInTheInterval(pointFrequency, -100, 100))) {
                                    Log.e(TAG, "point's time must be less than 5000, intensity must between 0~1, frequency must between -100 and 100");
                                    isCompliance = false;
                                    break;
                                }
                                if (pointCount - 1 == i && (pointTime != durationLast2 || pointIntensity != 0 || !isInTheInterval(pointFrequency, -100, 100))) {
                                    Log.e(TAG, "last point's time must equal with duration, and intensity must be 0, frequency must between -100 and 100");
                                    isCompliance = false;
                                    break;
                                }
                                patternHeInfo[(ind * 55) + (i * 3) + 7] = pointTime;
                                patternHeInfo[(ind * 55) + (i * 3) + 8] = pointIntensity;
                                patternHeInfo[(ind * 55) + (i * 3) + 9] = pointFrequency;
                                i++;
                                parametersObject = parametersObject2;
                                type = type2;
                                eventObject = eventObject2;
                                str6 = str3;
                                str7 = str4;
                                relativeTimeLast = relativeTimeLast3;
                            } catch (Exception e6) {
                                e = e6;
                            }
                        }
                        str3 = str6;
                        str4 = str7;
                        relativeTimeLast3 = relativeTimeLast;
                        isCompliance = isCompliance2;
                    } else {
                        try {
                            break;
                        } catch (Exception e7) {
                            e = e7;
                        }
                    }
                } catch (Exception e8) {
                    e = e8;
                }
                e.printStackTrace();
            } else {
                str2 = str5;
                str3 = str6;
                str4 = str7;
                relativeTimeLast3 = relativeTimeLast;
                durationLast2 = durationLast;
                isCompliance = isCompliance2;
            }
            if (!isCompliance) {
                relativeTimeLast4 = relativeTimeLast3;
                break;
            }
            ind++;
            hapticObject = hapticObject2;
            pattern = pattern2;
            str8 = str;
            str5 = str2;
            len = len2;
            eventNumberTmp = eventNumberTmp2;
            str6 = str3;
            str7 = str4;
            relativeTimeLast4 = relativeTimeLast3;
            return patternHeInfo;
        }
        if (!isCompliance) {
            Log.e(TAG, "current he file data, isn't compliance!!!!!!!");
            return null;
        }
        int lastEventIndex = ((eventNumberTmp2 - 1) * 55) + 0;
        if (4096 == patternHeInfo[lastEventIndex]) {
            int totalDuration = relativeTimeLast4 + durationLast2;
            Log.d(TAG, "last event type is continuous, totalDuration:" + totalDuration);
        } else {
            int totalDuration2 = relativeTimeLast4 + 80;
            Log.d(TAG, "last event type is transient, totalDuration:" + totalDuration2);
        }
        return patternHeInfo;
    }

    int[] generateSerializationDataHe_2_0(int formatVersion, int heVersion, int totalPattern, int pid, int seq, int indexBase, Pattern[] pattern) {
        int totalPatternLen = 0;
        for (Pattern patternTmp : pattern) {
            totalPatternLen += patternTmp.getPatternDataLen();
        }
        int[] data = new int[5 + totalPatternLen];
        Arrays.fill(data, 0);
        data[0] = formatVersion;
        data[1] = heVersion;
        data[2] = pid;
        data[3] = seq;
        data[4] = data[4] | (totalPattern & 65535);
        int patternNum = pattern.length;
        data[4] = data[4] | ((patternNum << 16) & (-65536));
        int patternOffset = 5;
        int patternOffset2 = indexBase;
        for (Pattern patternTmp2 : pattern) {
            int[] patternData = patternTmp2.generateSerializationPatternData(patternOffset2);
            System.arraycopy(patternData, 0, data, patternOffset, patternData.length);
            patternOffset += patternData.length;
            patternOffset2++;
        }
        return data;
    }

    void sendPatternWrapper(int seq, int pid, int heVersion, int loop, int interval, int amplitude, int freq, int totalPatternNum, int patternIndexOffset, Pattern[] list) {
        int[] patternHe = generateSerializationDataHe_2_0(2, heVersion, totalPatternNum, pid, seq, patternIndexOffset, list);
        try {
            VibrationEffect createPatternHe = RichTapVibrationEffect.createPatternHeWithParam(patternHe, loop, interval, amplitude, freq);
            VibrationAttributes atr = new VibrationAttributes.Builder().build();
            CombinedVibration combined = CombinedVibration.createParallel(createPatternHe);
            this.mService.vibrate(Process.myUid(), 0, this.mPackageName, combined, atr, "DynamicEffect", this.mToken);
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "for createPatternHe, The system doesn't integrate richTap software");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x03c1, code lost:
    
        r1[r1] = r35[r30 + r1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x03c5, code lost:
    
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x03a3, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x03a4, code lost:
    
        r3 = r5;
        r2 = r18;
        r4 = r35;
        r1 = r38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x03c8, code lost:
    
        r33 = r13;
        r27 = r28;
        r13 = r35;
        r28 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x03ee, code lost:
    
        sendPatternWrapper(r47, r48, r49, r50, r51, r52, r53, r21, r30, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x03f1, code lost:
    
        r20 = r20 + 1;
        r0 = r20 * 10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x03f7, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x03f8, code lost:
    
        r4 = r13;
        r3 = r5;
        r2 = r18;
        r1 = r38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x0401, code lost:
    
        r33 = r13;
        r27 = r28;
        r13 = r35;
        r28 = r0;
        r0 = r30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x0429, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x042a, code lost:
    
        r4 = r35;
        r3 = r5;
        r2 = r18;
        r1 = r38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x039d, code lost:
    
        android.util.Log.e(android.os.HapticPlayer.TAG, "current he file data, isn't compliance!!!!!!!");
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x03a2, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x01fd, code lost:
    
        android.util.Log.e(android.os.HapticPlayer.TAG, "duration must be less than 5000");
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x0202, code lost:
    
        r19 = false;
        r38 = r6;
        r18 = r15;
        r13 = r33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x0334, code lost:
    
        android.util.Log.e(android.os.HapticPlayer.TAG, "intensity or frequency must between 0 and 100");
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x0339, code lost:
    
        r19 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x015c, code lost:
    
        android.util.Log.e(android.os.HapticPlayer.TAG, "relativeTime must between 0 and 50000");
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x0161, code lost:
    
        r34 = r0;
        r19 = false;
        r38 = r6;
        r35 = r7;
        r32 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x035a, code lost:
    
        r34 = r0;
        r30 = r6;
        r35 = r7;
        r32 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x0366, code lost:
    
        android.util.Log.e(android.os.HapticPlayer.TAG, "haven't get type value");
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x036b, code lost:
    
        r19 = false;
        r38 = r17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x0373, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x0374, code lost:
    
        r3 = r5;
        r1 = r17;
        r2 = r18;
        r4 = r35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x043f, code lost:
    
        r0 = r6;
        r13 = r7;
        r21 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x0448, code lost:
    
        if (r0 >= r13.length) goto L147;
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x044a, code lost:
    
        r12 = r13.length - r0;
        r1 = new android.os.HapticPlayer.Pattern[r12];
        r1 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x0452, code lost:
    
        if (r1 >= r1.length) goto L206;
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x0454, code lost:
    
        r1[r1] = r13[r0 + r1];
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x045d, code lost:
    
        sendPatternWrapper(r47, r48, r49, r50, r51, r52, r53, r21, r0, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:199:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0476, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0477, code lost:
    
        r4 = r13;
        r3 = r5;
        r1 = r17;
        r2 = r18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x039b, code lost:
    
        if (r19 != false) goto L162;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x03ae, code lost:
    
        r35[r1] = r2;
        r0 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x03b7, code lost:
    
        if (r0 < ((r20 + 1) * 10)) goto L133;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x03b9, code lost:
    
        r1 = new android.os.HapticPlayer.Pattern[10];
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x03bb, code lost:
    
        r1 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x03bd, code lost:
    
        if (r1 >= 10) goto L205;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void parseAndSendDataHe_2_0(int seq, int pid, int heVersion, int loop, int interval, int amplitude, int freq, String patternString) {
        JSONObject hapticObject;
        JSONArray patternArray;
        int patternNum;
        byte[] patternHeInfo;
        Pattern[] patternList;
        int eventRelativeTime;
        int durationLast;
        boolean isCompliance;
        int relativeTimeLast;
        int wrapperOffset;
        int patternNum2;
        JSONArray patternArray2;
        String str;
        JSONArray eventArray;
        int wrapperOffset2;
        Pattern[] patternList2;
        JSONObject hapticObject2;
        String str2;
        int relativeTimeLast2;
        String str3;
        JSONObject hapticObject3;
        Pattern[] patternList3;
        int ind;
        int wrapperOffset3;
        int type;
        HapticPlayer hapticPlayer = this;
        String str4 = EVENT_KEY_DURATION;
        String str5 = EVENT_KEY_HE_FREQUENCY;
        String str6 = EVENT_KEY_RELATIVE_TIME;
        int relativeTimeLast3 = 0;
        int durationLast2 = 0;
        byte[] patternHeInfo2 = null;
        Pattern[] patternList4 = null;
        try {
            hapticObject = new JSONObject(patternString);
            patternArray = hapticObject.getJSONArray(PATTERN_KEY_PATTERN_LIST);
            patternNum = patternArray.length();
            patternHeInfo = new byte[patternNum * 64];
            try {
                patternList = new Pattern[patternNum];
                eventRelativeTime = 0;
                durationLast = 0;
                isCompliance = true;
                int wrapperIndex = 0;
                relativeTimeLast = 0;
                wrapperOffset = 0;
            } catch (Exception e) {
                e = e;
                patternHeInfo2 = patternHeInfo;
            }
        } catch (Exception e2) {
            e = e2;
        }
        while (true) {
            if (relativeTimeLast >= patternNum) {
                break;
            }
            try {
                Pattern pattern = new Pattern();
                JSONObject patternObject = patternArray.getJSONObject(relativeTimeLast);
                int patternRelativeTime = patternObject.getInt(PATTERN_KEY_PATTERN_ABS_TIME);
                pattern.mRelativeTime = patternRelativeTime;
                int patternDurationTime = eventRelativeTime + durationLast;
                patternNum2 = patternNum;
                if (relativeTimeLast > 0 && patternRelativeTime < patternDurationTime) {
                    Log.e(TAG, "Bad pattern relative time in int:" + relativeTimeLast);
                    return;
                }
                JSONArray eventArray2 = patternObject.getJSONArray(PATTERN_KEY_PATTERN);
                pattern.mEvent = new Event[eventArray2.length()];
                int eventRelativeTime2 = -1;
                int event = 0;
                while (true) {
                    patternArray2 = patternArray;
                    if (event >= eventArray2.length()) {
                        str = str4;
                        eventArray = eventArray2;
                        wrapperOffset2 = wrapperOffset;
                        patternList2 = patternList;
                        hapticObject2 = hapticObject;
                        str2 = str6;
                        relativeTimeLast2 = eventRelativeTime;
                        break;
                    }
                    try {
                        JSONObject eventObject = eventArray2.getJSONObject(event);
                        eventArray = eventArray2;
                        JSONObject eventTemp = eventObject.getJSONObject(EVENT_KEY_EVENT);
                        String name = eventTemp.getString(EVENT_KEY_HE_TYPE);
                        hapticObject2 = hapticObject;
                        if (!TextUtils.equals(EVENT_TYPE_HE_CONTINUOUS_NAME, name)) {
                            if (!TextUtils.equals(EVENT_TYPE_HE_TRANSIENT_NAME, name)) {
                                break;
                            }
                            type = TRANSIENT_EVENT;
                            pattern.mEvent[event] = new TransientEvent();
                        } else {
                            try {
                                pattern.mEvent[event] = new ContinuousEvent();
                                type = 4096;
                            } catch (Exception e3) {
                                e = e3;
                                patternList4 = patternList;
                                patternHeInfo2 = patternHeInfo;
                                relativeTimeLast3 = eventRelativeTime;
                                durationLast2 = durationLast;
                            }
                        }
                        int vibId = eventTemp.getInt(PATTERN_KEY_EVENT_VIB_ID);
                        wrapperOffset2 = wrapperOffset;
                        int wrapperOffset4 = (byte) vibId;
                        pattern.mEvent[event].mVibId = wrapperOffset4;
                        if (!eventTemp.has(str6)) {
                            Log.e(TAG, "event:" + relativeTimeLast + " don't have relativeTime parameters,BAD he!");
                            return;
                        }
                        int relativeTimeLast4 = eventTemp.getInt(str6);
                        if (event > 0 && relativeTimeLast4 < eventRelativeTime2) {
                            Log.e(TAG, "pattern ind:" + relativeTimeLast + " event:" + event + " relative time is not right!");
                            return;
                        }
                        try {
                            if (hapticPlayer.isInTheInterval(relativeTimeLast4, 0, MAX_PATERN_LAST_TIME)) {
                                JSONObject parametersObject = eventTemp.getJSONObject(EVENT_KEY_HE_PARAMETERS);
                                int intensity = parametersObject.getInt(EVENT_KEY_HE_INTENSITY);
                                int frequency = parametersObject.getInt(str5);
                                str2 = str6;
                                patternList2 = patternList;
                                int intensity2 = intensity;
                                try {
                                    if (!hapticPlayer.isInTheInterval(intensity2, 0, 100)) {
                                        relativeTimeLast2 = relativeTimeLast4;
                                        str = str4;
                                        break;
                                    }
                                    String str7 = str5;
                                    int frequency2 = frequency;
                                    if (!hapticPlayer.isInTheInterval(frequency2, 0, 100)) {
                                        str = str4;
                                        relativeTimeLast2 = relativeTimeLast4;
                                        str5 = str7;
                                        break;
                                    }
                                    pattern.mEvent[event].mType = type;
                                    pattern.mEvent[event].mRelativeTime = relativeTimeLast4;
                                    pattern.mEvent[event].mIntensity = intensity2;
                                    pattern.mEvent[event].mFreq = frequency2;
                                    if (4096 != type) {
                                        str = str4;
                                        relativeTimeLast2 = relativeTimeLast4;
                                        str5 = str7;
                                    } else if (eventTemp.has(str4)) {
                                        int durationLast3 = eventTemp.getInt(str4);
                                        str = str4;
                                        try {
                                            if (hapticPlayer.isInTheInterval(durationLast3, 0, 5000)) {
                                                pattern.mEvent[event].mDuration = durationLast3;
                                                JSONArray curve = parametersObject.getJSONArray(EVENT_KEY_HE_CURVE);
                                                ((ContinuousEvent) pattern.mEvent[event]).mPointNum = (byte) curve.length();
                                                Point[] piontArray = new Point[curve.length()];
                                                int prevPointTime = -1;
                                                relativeTimeLast2 = relativeTimeLast4;
                                                int pointLastTime = 0;
                                                int i = 0;
                                                while (true) {
                                                    int intensity3 = intensity2;
                                                    try {
                                                        int intensity4 = curve.length();
                                                        if (i < intensity4) {
                                                            JSONObject curveObject = curve.getJSONObject(i);
                                                            JSONArray curve2 = curve;
                                                            piontArray[i] = new Point();
                                                            int pointTime = curveObject.getInt(EVENT_KEY_HE_CURVE_POINT_TIME);
                                                            int frequency3 = frequency2;
                                                            int pointIntensity = (int) (curveObject.getDouble(EVENT_KEY_HE_INTENSITY) * 100.0d);
                                                            String str8 = str7;
                                                            int pointFrequency = curveObject.getInt(str8);
                                                            if (i == 0 && pointTime != 0) {
                                                                Log.d(TAG, "time of first point is not 0,bad he!");
                                                                return;
                                                            }
                                                            if (i > 0 && pointTime < prevPointTime) {
                                                                Log.d(TAG, "point times did not arrange in order,bad he!");
                                                                return;
                                                            }
                                                            piontArray[i].mTime = pointTime;
                                                            piontArray[i].mIntensity = pointIntensity;
                                                            piontArray[i].mFreq = pointFrequency;
                                                            pointLastTime = pointTime;
                                                            i++;
                                                            hapticPlayer = this;
                                                            str7 = str8;
                                                            curve = curve2;
                                                            intensity2 = intensity3;
                                                            prevPointTime = pointTime;
                                                            frequency2 = frequency3;
                                                        } else {
                                                            str5 = str7;
                                                            if (pointLastTime != durationLast3) {
                                                                Log.e(TAG, "event:" + relativeTimeLast + " point last time do not match duration parameter");
                                                                return;
                                                            } else if (piontArray.length > 0) {
                                                                ((ContinuousEvent) pattern.mEvent[event]).mPoint = piontArray;
                                                                durationLast = durationLast3;
                                                            } else {
                                                                Log.d(TAG, "continuous event has nothing in point");
                                                                isCompliance = false;
                                                                durationLast = durationLast3;
                                                            }
                                                        }
                                                    } catch (Exception e4) {
                                                        e = e4;
                                                        durationLast2 = durationLast3;
                                                        patternHeInfo2 = patternHeInfo;
                                                        patternList4 = patternList2;
                                                        relativeTimeLast3 = relativeTimeLast2;
                                                    }
                                                }
                                            } else {
                                                try {
                                                    break;
                                                } catch (Exception e5) {
                                                    e = e5;
                                                    relativeTimeLast3 = relativeTimeLast4;
                                                    durationLast2 = durationLast3;
                                                    patternHeInfo2 = patternHeInfo;
                                                    patternList4 = patternList2;
                                                }
                                            }
                                        } catch (Exception e6) {
                                            e = e6;
                                            durationLast2 = durationLast3;
                                            patternHeInfo2 = patternHeInfo;
                                            patternList4 = patternList2;
                                            relativeTimeLast3 = relativeTimeLast4;
                                        }
                                    } else {
                                        try {
                                            Log.e(TAG, "event:" + relativeTimeLast + " don't have duration parameters");
                                            return;
                                        } catch (Exception e7) {
                                            e = e7;
                                            relativeTimeLast3 = relativeTimeLast4;
                                            patternHeInfo2 = patternHeInfo;
                                            durationLast2 = durationLast;
                                            patternList4 = patternList2;
                                        }
                                    }
                                    if (!isCompliance) {
                                        break;
                                    }
                                    event++;
                                    hapticPlayer = this;
                                    eventRelativeTime2 = relativeTimeLast4;
                                    patternArray = patternArray2;
                                    eventArray2 = eventArray;
                                    hapticObject = hapticObject2;
                                    wrapperOffset = wrapperOffset2;
                                    str6 = str2;
                                    str4 = str;
                                    patternList = patternList2;
                                    eventRelativeTime = relativeTimeLast2;
                                } catch (Exception e8) {
                                    e = e8;
                                    patternHeInfo2 = patternHeInfo;
                                    durationLast2 = durationLast;
                                    patternList4 = patternList2;
                                    relativeTimeLast3 = relativeTimeLast4;
                                }
                            } else {
                                try {
                                    break;
                                } catch (Exception e9) {
                                    e = e9;
                                    relativeTimeLast3 = relativeTimeLast4;
                                    patternList4 = patternList;
                                    patternHeInfo2 = patternHeInfo;
                                    durationLast2 = durationLast;
                                }
                            }
                        } catch (Exception e10) {
                            e = e10;
                            patternHeInfo2 = patternHeInfo;
                            durationLast2 = durationLast;
                            patternList4 = patternList;
                            relativeTimeLast3 = relativeTimeLast4;
                        }
                        e = e9;
                        relativeTimeLast3 = relativeTimeLast4;
                        patternList4 = patternList;
                        patternHeInfo2 = patternHeInfo;
                        durationLast2 = durationLast;
                    } catch (Exception e11) {
                        e = e11;
                        patternHeInfo2 = patternHeInfo;
                        relativeTimeLast3 = eventRelativeTime;
                        durationLast2 = durationLast;
                        patternList4 = patternList;
                    }
                }
            } catch (Exception e12) {
                e = e12;
                patternList4 = patternList;
                patternHeInfo2 = patternHeInfo;
                relativeTimeLast3 = eventRelativeTime;
                durationLast2 = durationLast;
            }
            e.printStackTrace();
            return;
            wrapperOffset = wrapperOffset3;
            hapticPlayer = this;
            patternList = patternList3;
            patternNum = patternNum2;
            patternArray = patternArray2;
            hapticObject = hapticObject3;
            relativeTimeLast = ind;
            str6 = str2;
            str5 = str3;
            str4 = str;
            eventRelativeTime = relativeTimeLast2;
        }
    }

    public void applyPatternHeWithString(String patternString, int loop, int interval, int amplitude, int freq) {
        int heVersion;
        Log.d(TAG, "play new he api");
        if (loop < 1) {
            Log.e(TAG, "The minimum count of loop pattern is 1");
            return;
        }
        try {
            try {
                JSONObject hapticObject = new JSONObject(patternString);
                if (!mAvailable) {
                    heVersion = 0;
                } else {
                    JSONObject metaData = hapticObject.getJSONObject(HE_META_DATA_KEY);
                    int heVersion2 = metaData.getInt(HE_VERSION_KEY);
                    int richTapMajorVersion = getMajorVersion();
                    int richTapMinorVersion = getMinorVersion();
                    boolean checkPass = checkSdkSupport(richTapMajorVersion, richTapMinorVersion, heVersion2);
                    if (checkPass) {
                        heVersion = heVersion2;
                    } else {
                        Log.e(TAG, "richtap version check failed, richTapMajorVersion:" + String.format("%x02", Integer.valueOf(richTapMajorVersion)) + " heVersion:" + heVersion2);
                        return;
                    }
                }
                if (heVersion != 1) {
                    if (heVersion == 2) {
                        int seq = mSeq.getAndIncrement();
                        int pid = Process.myPid();
                        parseAndSendDataHe_2_0(seq, pid, heVersion, loop, interval, amplitude, freq, patternString);
                        return;
                    }
                    Log.e(TAG, "unsupport he version heVersion:" + heVersion);
                    return;
                }
                try {
                    int[] patternHeInfo = getSerializationDataHe_1_0(patternString);
                    if (patternHeInfo == null) {
                        Log.e(TAG, "serialize he failed!! ,heVersion:" + heVersion);
                        return;
                    }
                    int len = patternHeInfo.length;
                    try {
                        int[] realPatternHeInfo = new int[len + 1];
                        realPatternHeInfo[0] = 3;
                        System.arraycopy(patternHeInfo, 0, realPatternHeInfo, 1, patternHeInfo.length);
                        try {
                            VibrationEffect createPatternHe = RichTapVibrationEffect.createPatternHeWithParam(realPatternHeInfo, loop, interval, amplitude, freq);
                            CombinedVibration combined = CombinedVibration.createParallel(createPatternHe);
                            VibrationAttributes atr = new VibrationAttributes.Builder().build();
                            this.mService.vibrate(Process.myUid(), 0, this.mPackageName, combined, atr, "DynamicEffect", this.mToken);
                        } catch (Exception e) {
                            e = e;
                            e.printStackTrace();
                            Log.w(TAG, "for createPatternHe, The system doesn't integrate richTap software");
                        }
                    } catch (Exception e2) {
                        e = e2;
                    }
                } catch (Exception e3) {
                    e = e3;
                    e.printStackTrace();
                }
            } catch (Exception e4) {
                e = e4;
            }
        } catch (Exception e5) {
            e = e5;
        }
    }

    public int getRealLooper(int looper) {
        if (looper < 0) {
            if (looper == -1) {
                return OplusMaxLinearLayout.INVALID_MAX_VALUE;
            }
            return 0;
        }
        if (looper == 0) {
            return 1;
        }
        return looper;
    }

    public void start(int loop) {
        Log.d(TAG, "start play pattern loop:" + loop);
        if (this.mEffect == null) {
            Log.e(TAG, "effect is null,do nothing");
            return;
        }
        final int realLooper = getRealLooper(loop);
        if (realLooper < 0) {
            Log.e(TAG, "looper is not correct realLooper:" + realLooper);
        } else {
            mExcutor.execute(new Runnable() { // from class: android.os.HapticPlayer.1
                @Override // java.lang.Runnable
                public void run() {
                    String patternJson;
                    Log.d(HapticPlayer.TAG, "haptic play start!");
                    long startRunTime = System.currentTimeMillis();
                    try {
                        HapticPlayer.this.mStarted = true;
                        patternJson = HapticPlayer.this.mEffect.getPatternInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.w(HapticPlayer.TAG, "for createPatternHe, The system doesn't integrate richTap software");
                    }
                    if (patternJson == null) {
                        Log.d(HapticPlayer.TAG, "pattern is null,can not play!");
                        return;
                    }
                    HapticPlayer.this.applyPatternHeWithString(patternJson, realLooper, 0, 255, 0);
                    long useTime = System.currentTimeMillis() - startRunTime;
                    Log.d(HapticPlayer.TAG, "run vibrate thread use time:" + useTime);
                }
            });
        }
    }

    public void start(int loop, final int interval, final int amplitude) {
        Log.d(TAG, "start with loop:" + loop + " interval:" + interval + " amplitude:" + amplitude);
        boolean checkResult = checkParam(interval, amplitude, -1);
        if (!checkResult) {
            Log.e(TAG, "wrong start param");
            return;
        }
        if (this.mEffect == null) {
            Log.e(TAG, "effect is null,do nothing");
            return;
        }
        final int realLooper = getRealLooper(loop);
        if (realLooper < 0) {
            Log.e(TAG, "looper is not correct realLooper:" + realLooper);
        } else {
            mExcutor.execute(new Runnable() { // from class: android.os.HapticPlayer.2
                @Override // java.lang.Runnable
                public void run() {
                    String patternJson;
                    Log.d(HapticPlayer.TAG, "haptic play start!");
                    long startRunTime = System.currentTimeMillis();
                    try {
                        HapticPlayer.this.mStarted = true;
                        patternJson = HapticPlayer.this.mEffect.getPatternInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.w(HapticPlayer.TAG, "for createPatternHe, The system doesn't integrate richTap software");
                    }
                    if (patternJson == null) {
                        Log.d(HapticPlayer.TAG, "pattern is null,can not play!");
                        return;
                    }
                    HapticPlayer.this.applyPatternHeWithString(patternJson, realLooper, interval, amplitude, 0);
                    long useTime = System.currentTimeMillis() - startRunTime;
                    Log.d(HapticPlayer.TAG, "run vibrate thread use time:" + useTime);
                }
            });
        }
    }

    public void start(int loop, final int interval, final int amplitude, final int freq) {
        Log.d(TAG, "start with loop:" + loop + " interval:" + interval + " amplitude:" + amplitude + " freq:" + freq);
        boolean checkResult = checkParam(interval, amplitude, freq);
        if (!checkResult) {
            Log.e(TAG, "wrong start param");
            return;
        }
        if (this.mEffect == null) {
            Log.e(TAG, "effect is null,do nothing");
            return;
        }
        final int realLooper = getRealLooper(loop);
        if (realLooper < 0) {
            Log.e(TAG, "looper is not correct realLooper:" + realLooper);
        } else {
            mExcutor.execute(new Runnable() { // from class: android.os.HapticPlayer.3
                @Override // java.lang.Runnable
                public void run() {
                    String patternJson;
                    Log.d(HapticPlayer.TAG, "haptic play start!");
                    long startRunTime = System.currentTimeMillis();
                    try {
                        HapticPlayer.this.mStarted = true;
                        patternJson = HapticPlayer.this.mEffect.getPatternInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.w(HapticPlayer.TAG, "for createPatternHe, The system doesn't integrate richTap software");
                    }
                    if (patternJson == null) {
                        Log.d(HapticPlayer.TAG, "pattern is null,can not play!");
                        return;
                    }
                    HapticPlayer.this.applyPatternHeWithString(patternJson, realLooper, interval, amplitude, freq);
                    long useTime = System.currentTimeMillis() - startRunTime;
                    Log.d(HapticPlayer.TAG, "run vibrate thread use time:" + useTime);
                }
            });
        }
    }

    private boolean checkParam(int interval, int amplitude, int freq) {
        if (interval < 0 && interval != -1) {
            Log.e(TAG, "wrong interval param");
            return false;
        }
        if (freq < 0 && freq != -1) {
            Log.e(TAG, "wrong freq param");
            return false;
        }
        if ((amplitude < 0 && amplitude != -1) || amplitude > 255) {
            Log.e(TAG, "wrong amplitude param");
            return false;
        }
        return true;
    }

    public void applyPatternHeParam(final int interval, final int amplitude, final int freq) {
        Log.d(TAG, "start with  interval:" + interval + " amplitude:" + amplitude + " freq:" + freq);
        boolean checkResult = checkParam(interval, amplitude, freq);
        if (!checkResult) {
            Log.e(TAG, "wrong param!");
            return;
        }
        try {
            if (this.mStarted) {
                mExcutor.execute(new Runnable() { // from class: android.os.HapticPlayer.4
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            VibrationEffect createPatternHe = RichTapVibrationEffect.createPatternHeParameter(interval, amplitude, freq);
                            VibrationAttributes attributes = new VibrationAttributes.Builder().build();
                            CombinedVibration combined = CombinedVibration.createParallel(createPatternHe);
                            HapticPlayer.this.mService.vibrate(Process.myUid(), 0, HapticPlayer.this.mPackageName, combined, attributes, "DynamicEffect", HapticPlayer.this.mToken);
                            Log.d(HapticPlayer.TAG, "haptic apply param");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.w(HapticPlayer.TAG, "for createPatternHe, The system doesn't integrate richTap software");
                        }
                    }
                });
            } else {
                Log.d(TAG, "haptic player has not started");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "The system doesn't integrate richTap software");
        }
    }

    public void updateInterval(int interval) {
        applyPatternHeParam(interval, -1, -1);
    }

    public void updateAmplitude(int amplitude) {
        applyPatternHeParam(-1, amplitude, -1);
    }

    public void updateFrequency(int freq) {
        applyPatternHeParam(-1, -1, freq);
    }

    public void updateParameter(int interval, int amplitude, int freq) {
        applyPatternHeParam(interval, amplitude, freq);
    }

    public void stop() {
        if (this.mStarted) {
            mExcutor.execute(new Runnable() { // from class: android.os.HapticPlayer.5
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        VibrationEffect createPatternHe = RichTapVibrationEffect.createPatternHeParameter(0, 0, 0);
                        CombinedVibration combined = CombinedVibration.createParallel(createPatternHe);
                        HapticPlayer.this.mService.vibrate(Process.myUid(), 0, HapticPlayer.this.mPackageName, combined, (VibrationAttributes) null, "DynamicEffect", HapticPlayer.this.mToken);
                        Log.d(HapticPlayer.TAG, "haptic play stop");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.w(HapticPlayer.TAG, "for createPatternHe, The system doesn't integrate richTap software");
                    }
                }
            });
        } else {
            Log.d(TAG, "haptic player has not started");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public abstract class Event {
        int mDuration;
        int mFreq;
        int mIntensity;
        int mLen;
        int mRelativeTime;
        int mType;
        int mVibId;

        abstract int[] generateData();

        Event() {
        }

        public String toString() {
            return "Event{mType=" + this.mType + ", mVibId=" + this.mVibId + ", mRelativeTime=" + this.mRelativeTime + ", mIntensity=" + this.mIntensity + ", mFreq=" + this.mFreq + ", mDuration=" + this.mDuration + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class TransientEvent extends Event {
        TransientEvent() {
            super();
            this.mLen = 7;
        }

        @Override // android.os.HapticPlayer.Event
        int[] generateData() {
            int[] data = new int[this.mLen];
            Arrays.fill(data, 0);
            data[0] = this.mType;
            data[1] = this.mLen - 2;
            data[2] = this.mVibId;
            data[3] = this.mRelativeTime;
            data[4] = this.mIntensity;
            data[5] = this.mFreq;
            data[6] = this.mDuration;
            return data;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class Point {
        int mFreq;
        int mIntensity;
        int mTime;

        Point() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ContinuousEvent extends Event {
        Point[] mPoint;
        int mPointNum;

        ContinuousEvent() {
            super();
        }

        @Override // android.os.HapticPlayer.Event
        int[] generateData() {
            int[] data = new int[(this.mPointNum * 3) + 8];
            Arrays.fill(data, 0);
            data[0] = this.mType;
            data[1] = ((this.mPointNum * 3) + 8) - 2;
            data[2] = this.mVibId;
            data[3] = this.mRelativeTime;
            data[4] = this.mIntensity;
            data[5] = this.mFreq;
            data[6] = this.mDuration;
            data[7] = this.mPointNum;
            int offset = 8;
            for (int i = 0; i < this.mPointNum; i++) {
                data[offset] = this.mPoint[i].mTime;
                int offset2 = offset + 1;
                data[offset2] = this.mPoint[i].mIntensity;
                int offset3 = offset2 + 1;
                data[offset3] = this.mPoint[i].mFreq;
                offset = offset3 + 1;
            }
            return data;
        }

        @Override // android.os.HapticPlayer.Event
        public String toString() {
            return "Continuous{mPointNum=" + this.mPointNum + ", mPoint=" + Arrays.toString(this.mPoint) + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class Pattern {
        Event[] mEvent;
        int mRelativeTime;

        Pattern() {
        }

        int getPatternEventLen() {
            int len = 0;
            for (Event event : this.mEvent) {
                if (event.mType == 4096) {
                    len += (((ContinuousEvent) event).mPointNum * 3) + 8;
                } else if (event.mType == 4097) {
                    len += 7;
                }
            }
            return len;
        }

        int getPatternDataLen() {
            int eventLen = getPatternEventLen();
            return eventLen + 3;
        }

        int[] generateSerializationPatternData(int index) {
            int dataLen = getPatternDataLen();
            int[] data = new int[dataLen];
            Arrays.fill(data, 0);
            data[0] = index;
            data[1] = this.mRelativeTime;
            Event[] eventArr = this.mEvent;
            data[2] = eventArr.length;
            int offset = 3;
            for (Event event : eventArr) {
                int[] eventData = event.generateData();
                System.arraycopy(eventData, 0, data, offset, eventData.length);
                offset += eventData.length;
            }
            return data;
        }
    }
}
