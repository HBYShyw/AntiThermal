package com.oplus.uifirst;

import android.os.Handler;
import android.os.PerformanceManager;
import android.os.Process;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import java.io.StringReader;
import java.util.Arrays;
import java.util.function.Predicate;

/* loaded from: classes.dex */
public class UagManager {
    private static final String FILTER_UAG = "uag";
    private static final int MAX_CLUSTER_NUMBER = 3;
    private static final int MAX_CPU_NUMBER = 8;
    private static final String TAG = "OplusUIFirst_UAG";
    private int[] mCpuClusters;
    private final Handler mHandler;
    private final OplusUIFirstManager mUifManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UagManager(OplusUIFirstManager uifManager, Handler handler) {
        this.mUifManager = uifManager;
        this.mHandler = handler;
        int[] iArr = new int[4];
        this.mCpuClusters = iArr;
        Process.getCpuClusters(iArr);
        if (Utils.DEBUG) {
            Log.d(TAG, "cpu clusters: " + Arrays.toString(this.mCpuClusters));
        }
        updateConfig();
    }

    private UagOp parseUagOp() {
        char c;
        String config = this.mUifManager.getFilterConfig(FILTER_UAG, "def");
        if (Utils.DEBUG) {
            Log.d(TAG, "parseUagOp:  " + config);
        }
        if (config == null) {
            return null;
        }
        final String localPrjNum = Utils.getProjectNumber();
        try {
            JsonReader reader = new JsonReader(new StringReader(config));
            try {
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();
                    UagOp para = new UagOp();
                    while (reader.peek() == JsonToken.NAME) {
                        String jsonName = reader.nextName();
                        switch (jsonName.hashCode()) {
                            case -309310695:
                                if (jsonName.equals("project")) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case 102542:
                                if (jsonName.equals("gov")) {
                                    c = 1;
                                    break;
                                }
                                break;
                            case 1549239730:
                                if (jsonName.equals("audio_perf")) {
                                    c = 2;
                                    break;
                                }
                                break;
                        }
                        c = 65535;
                        switch (c) {
                            case 0:
                                para.mPrjNum = reader.nextString();
                                break;
                            case 1:
                                para.mGov = reader.nextString();
                                break;
                            case 2:
                                para.mAudioPerf = reader.nextString();
                                break;
                            default:
                                reader.skipValue();
                                Log.w(TAG, "unknown para: " + jsonName);
                                break;
                        }
                    }
                    String[] projectNums = para.mPrjNum.split("\\|");
                    boolean contains_porject = Arrays.stream(projectNums).anyMatch(new Predicate() { // from class: com.oplus.uifirst.UagManager$$ExternalSyntheticLambda0
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            boolean equals;
                            equals = ((String) obj).equals(localPrjNum);
                            return equals;
                        }
                    });
                    if (contains_porject) {
                        Log.d(TAG, para.toString());
                        reader.close();
                        reader.close();
                        return para;
                    }
                    reader.endObject();
                }
                reader.endArray();
                reader.close();
                if (Utils.DEBUG) {
                    Log.d(TAG, "parseUagOp failed: can't find config list for this project");
                }
                return null;
            } finally {
            }
        } catch (Exception e) {
            Log.e(TAG, "para json config error:" + config);
            return null;
        }
    }

    private synchronized void setFreqGoverner(String value, int[] clusters) {
        PerformanceManager.setFreqGoverner(value, clusters);
        if (Utils.DEBUG) {
            Log.d(TAG, "setFreqGoverner: " + value);
        }
    }

    public final synchronized void updateConfig() {
        UagOp op = parseUagOp();
        if (op != null) {
            if (op.mGov != null) {
                setFreqGoverner(op.mGov, this.mCpuClusters);
            }
            if (op.mAudioPerf != null) {
                PerformanceManager.enableAudioPerf(op.mAudioPerf);
                if (Utils.DEBUG) {
                    Log.d(TAG, "enableAudioPerf: " + op.mAudioPerf);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class UagOp {
        String mAudioPerf;
        String mGov;
        String mPrjNum;

        UagOp() {
        }

        public String toString() {
            return "UagOp: project:" + this.mPrjNum + ", gov:" + this.mGov + ", audio_perf:" + this.mAudioPerf;
        }
    }
}
