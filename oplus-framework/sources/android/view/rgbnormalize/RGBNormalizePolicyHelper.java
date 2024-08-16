package android.view.rgbnormalize;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IApplicationInfoExt;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oplus.util.OplusStatistics;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class RGBNormalizePolicyHelper {
    private static final int FLING_COUNT_APP_ID = 20080;
    private static final String FLING_COUNT_DURATION = "duration";
    private static final String FLING_COUNT_EVENT_END_TIME = "event_end_time";
    private static final String FLING_COUNT_EVENT_NAME = "gesture_fling_count";
    private static final String FLING_COUNT_EVENT_START_TIME = "event_start_time";
    private static final String FLING_COUNT_NUMBER = "fling_count";
    private static final String VIEW_INFO_MATRIX = "matrix";
    private static final String VIEW_INFO_NAME = "name";
    private static final String VIEW_PARAM_MAX_GRAY_VALUE = "max_gray_value";
    private static final String VIEW_PARAM_MIN_GRAY_VALUE = "min_gray_value";
    private static OplusRGBViewInfo sCurrentRenderingViewInfo;
    private static float[] sMatrix;
    private static final String TAG = RGBNormalizePolicyHelper.class.getSimpleName();
    private static HashMap<String, List<OplusRGBViewInfo>> sCandidateActivities = new HashMap<>(1);
    private static HashMap<String, List<OplusRGBViewInfo>> sHitActivities = new HashMap<>(1);

    public static boolean isViewInPolicy(String activityName, View view) {
        sCurrentRenderingViewInfo = null;
        if (!sCandidateActivities.containsKey(activityName)) {
            return false;
        }
        List<OplusRGBViewInfo> viewInfos = sCandidateActivities.get(activityName);
        if (viewInfos == null) {
            return true;
        }
        for (OplusRGBViewInfo viewInfo : viewInfos) {
            if (view.getClass().getSimpleName().equals(viewInfo.getViewName())) {
                sCurrentRenderingViewInfo = viewInfo;
                List<OplusRGBViewInfo> hitViewInfos = sHitActivities.get(activityName);
                if (!hitViewInfos.contains(viewInfo)) {
                    hitViewInfos.add(viewInfo);
                }
                return true;
            }
        }
        return false;
    }

    public static void initRGBNormalizeApplicationInfo(ApplicationInfo appInfo) {
        Bundle bundle;
        IApplicationInfoExt appInfoExt = appInfo == null ? null : appInfo.mApplicationInfoExt;
        if (appInfoExt != null && (bundle = appInfoExt.getRGBNormalizeExtraBundle()) != null && !bundle.isEmpty()) {
            String matrixStr = bundle.getString("matrix", "");
            String policy = bundle.getString("policy", "");
            try {
                parseConfigParams(matrixStr);
                parsePolicyParams(new JSONArray(policy));
            } catch (Exception ex) {
                Log.e(IOplusRGBNormalizeManager.TAG, ex.getMessage());
            }
        }
    }

    private static void parseConfigParams(String matrixStr) {
        String[] matrixList = matrixStr.split(",");
        float[] matrix = new float[matrixList.length];
        for (int index = 0; index < matrixList.length; index++) {
            matrix[index] = Float.parseFloat(matrixList[index]);
        }
        sMatrix = matrix;
    }

    private static void parsePolicyParams(JSONArray entities) {
        if (entities == null || entities.length() <= 0) {
            return;
        }
        for (int i = 0; i < entities.length(); i++) {
            JSONObject eachEntity = entities.optJSONObject(i);
            if (eachEntity != null) {
                String activityName = eachEntity.optString("activity_name", "");
                List<OplusRGBViewInfo> viewInfos = null;
                if (eachEntity.has(IOplusRGBNormalizeManager.VIEW_NAME) && eachEntity.has(IOplusRGBNormalizeManager.VIEW_PARAMS)) {
                    String[] views = eachEntity.optString(IOplusRGBNormalizeManager.VIEW_NAME).split(",");
                    JSONArray viewParams = eachEntity.optJSONArray(IOplusRGBNormalizeManager.VIEW_PARAMS);
                    viewInfos = new ArrayList<>();
                    for (int j = 0; j < views.length; j++) {
                        OplusRGBViewInfo viewInfo = new OplusRGBViewInfo();
                        String viewName = views[j];
                        viewInfo.setViewName(viewName);
                        JSONObject viewParam = viewParams.optJSONObject(j);
                        if (viewParam != null) {
                            int maxGrayValue = Integer.parseInt(viewParam.optString(VIEW_PARAM_MAX_GRAY_VALUE));
                            int minGrayValue = Integer.parseInt(viewParam.optString(VIEW_PARAM_MIN_GRAY_VALUE));
                            viewInfo.setMaxGrayScaleValue(maxGrayValue);
                            viewInfo.setMinGrayScaleValue(minGrayValue);
                        }
                        viewInfos.add(viewInfo);
                    }
                }
                sCandidateActivities.put(activityName, viewInfos);
                sHitActivities.put(activityName, new ArrayList());
            }
        }
    }

    private static void uploadStatistics(int candidateCount, int hitCount, Context context) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put(FLING_COUNT_EVENT_START_TIME, "0");
        dataMap.put(FLING_COUNT_EVENT_END_TIME, "0");
        dataMap.put(FLING_COUNT_DURATION, String.valueOf(candidateCount));
        dataMap.put(FLING_COUNT_NUMBER, String.valueOf(hitCount));
        OplusStatistics.onCommon(context, FLING_COUNT_APP_ID, FLING_COUNT_EVENT_NAME, FLING_COUNT_EVENT_NAME, dataMap, false);
    }

    public static void analyzeHitRate(Context context) {
        Set<String> candidateActivityNames = sCandidateActivities.keySet();
        int candidateCount = 0;
        int hitCount = 0;
        for (String activityName : candidateActivityNames) {
            List<OplusRGBViewInfo> candidateViewInfos = sCandidateActivities.get(activityName);
            List<OplusRGBViewInfo> hitViewInfos = sHitActivities.get(activityName);
            for (int i = 0; i < candidateViewInfos.size(); i++) {
                if (hitViewInfos.contains(candidateViewInfos.get(i))) {
                    Log.d(TAG, "Hit View " + candidateViewInfos.get(i).toString() + " for Activity " + activityName);
                }
            }
            int i2 = candidateViewInfos.size();
            candidateCount += i2;
            hitCount += hitViewInfos.size();
        }
        if (candidateCount != hitCount) {
            uploadStatistics(candidateCount, hitCount, context);
        }
    }

    public static float[] getMatrix() {
        return sMatrix;
    }

    public static OplusRGBViewInfo getCurrentRenderingViewInfo() {
        return sCurrentRenderingViewInfo;
    }

    /* loaded from: classes.dex */
    public static class OplusRGBViewInfo {
        private int mMaxGrayScaleValue = 255;
        private int mMinGrayScaleValue;
        private String mViewName;

        public String getViewName() {
            return this.mViewName;
        }

        public int getMaxGrayScaleValue() {
            return this.mMaxGrayScaleValue;
        }

        public int getMinGrayScaleValue() {
            return this.mMinGrayScaleValue;
        }

        public void setViewName(String name) {
            this.mViewName = name;
        }

        public void setMaxGrayScaleValue(int maxGrayScaleValue) {
            this.mMaxGrayScaleValue = maxGrayScaleValue;
        }

        public void setMinGrayScaleValue(int minGrayScaleValue) {
            this.mMinGrayScaleValue = minGrayScaleValue;
        }

        public String toString() {
            return "OplusRGBViewInfo{mViewName='" + this.mViewName + "', mMaxGrayScaleValue=" + this.mMaxGrayScaleValue + ", mMinGrayScaleValue=" + this.mMinGrayScaleValue + '}';
        }
    }
}
