package cn.teddymobile.free.anteater;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.provider.oplus.Telephony;
import android.telephony.OplusTelephonyManager;
import android.text.TextUtils;
import android.view.View;
import cn.teddymobile.free.anteater.logger.Logger;
import cn.teddymobile.free.anteater.resources.RuleResources;
import cn.teddymobile.free.anteater.resources.UriConstants;
import cn.teddymobile.free.anteater.rule.Rule;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Anteater {
    public static final String ACTION_CLICK = "click";
    public static final String ACTION_DISPLAY = "display";
    private static final String EMPTY = "";
    private static final String JSON_FIELD_PARSER = "parser";
    private static final String JSON_FIELD_RESULT = "result";
    private static final String TAG = Anteater.class.getSimpleName();
    private static volatile Anteater sInstance = null;
    private RuleResources mResources = null;
    private Handler mWorkHandler = null;

    /* loaded from: classes.dex */
    public interface CheckCallback {
        void onFailure();

        void onSuccess();
    }

    /* loaded from: classes.dex */
    public enum ErrorCode {
        NO_VIEW,
        NOT_INIT,
        NOT_FOUND,
        UNSUPPORT,
        SETTING_OFF,
        SAVE_FAILED,
        NONE
    }

    /* loaded from: classes.dex */
    public interface ProcessCallback {
        boolean isSettingOff(Context context);

        void onProcessResult(Context context, ResultData resultData);
    }

    /* loaded from: classes.dex */
    public interface SaveCallback {
        boolean isSettingOff(Context context);

        void onSaveResult(Context context, ResultData resultData);
    }

    public static Anteater getInstance() {
        if (sInstance == null) {
            synchronized (Anteater.class) {
                if (sInstance == null) {
                    sInstance = new Anteater();
                }
            }
        }
        return sInstance;
    }

    private Anteater() {
        Logger.d(TAG, "Anteater <init>()");
    }

    public void init(Context context, RuleResources.InitCallback callback) {
        try {
            if (this.mWorkHandler == null) {
                this.mWorkHandler = callback.createWorkHandler(context, "anteater", 1);
            }
            if (this.mResources == null) {
                this.mResources = new RuleResources(this.mWorkHandler);
            }
            this.mResources.init(context, callback);
            this.mResources.registerObserver(context);
        } catch (Exception e) {
            Logger.e(TAG, "init failed : " + e.toString());
        }
    }

    public void release() {
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        RuleResources ruleResources = this.mResources;
        if (ruleResources != null) {
            ruleResources.unregisterObserver();
        }
    }

    public Handler getWorkHandler() {
        return this.mWorkHandler;
    }

    public void process(final String action, final View view, final ProcessCallback callback) {
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: cn.teddymobile.free.anteater.Anteater.1
                @Override // java.lang.Runnable
                public void run() {
                    View view2 = view;
                    if (view2 == null) {
                        Logger.w(Anteater.TAG, "Process [No View]");
                        return;
                    }
                    Context context = view2.getContext();
                    String msg = "action=" + action + ", context=" + context + ", view=" + context.getPackageName() + "/" + view;
                    if (callback.isSettingOff(context)) {
                        Logger.w(Anteater.TAG, "Process [Setting Off] : " + msg);
                        ResultData result = new ResultData();
                        result.setError(ErrorCode.SETTING_OFF);
                        callback.onProcessResult(context, result);
                        return;
                    }
                    View view3 = view;
                    if (view3 != null) {
                        String viewName = view3.getClass().getName();
                        if (!Anteater.this.mResources.isInited()) {
                            Logger.w(Anteater.TAG, "Process [Not Init] : action=" + action + ", view=" + viewName);
                            ResultData result2 = new ResultData();
                            result2.setError(ErrorCode.NOT_INIT);
                            callback.onProcessResult(context, result2);
                            return;
                        }
                        Logger.d(Anteater.TAG, "Process Begin : View = " + viewName + ". Action = " + action);
                        ArrayList<QueryData> queryList = new ArrayList<>();
                        ArrayList<Rule> ruleList = Anteater.this.copyRuleList();
                        int ruleSize = ruleList.size();
                        for (int i = 0; i < ruleSize; i++) {
                            Rule rule = ruleList.get(i);
                            if (!rule.check(view)) {
                                Logger.i(Anteater.TAG, "\n----------Process Skip Rule " + i + "----------\n" + rule);
                            } else {
                                Logger.i(Anteater.TAG, "\n----------Process Apply Rule " + i + "----------\n" + rule);
                                try {
                                    JSONObject resultObject = rule.extract(action, view);
                                    if (resultObject != null) {
                                        QueryData data = new QueryData();
                                        data.setTitle(Anteater.this.extractTitle(resultObject));
                                        data.setUrl(Anteater.this.extractUrl(resultObject));
                                        if (data.isValid() && !queryList.contains(data)) {
                                            queryList.add(data);
                                        }
                                    }
                                } catch (JSONException e) {
                                    Logger.w(Anteater.TAG, e.getMessage(), e);
                                } catch (Exception e2) {
                                    Logger.e(Anteater.TAG, e2.getMessage(), e2);
                                }
                            }
                        }
                        int i2 = queryList.size();
                        if (i2 > 0) {
                            Anteater.this.processSuccess(context, callback, queryList);
                        } else {
                            Anteater.this.processFailed(context, callback, ruleSize);
                        }
                        Logger.d(Anteater.TAG, "Process Finish : View = " + viewName + ". Action = " + action);
                        return;
                    }
                    Logger.w(Anteater.TAG, "Process [No View] : " + msg);
                    ResultData result3 = new ResultData();
                    result3.setError(ErrorCode.NO_VIEW);
                    callback.onProcessResult(context, result3);
                }
            });
        } else {
            Logger.e(TAG, "Process : No WorkHandler");
        }
    }

    public void save(final String action, final View view, final SaveCallback callback) {
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: cn.teddymobile.free.anteater.Anteater.2
                @Override // java.lang.Runnable
                public void run() {
                    View view2 = view;
                    if (view2 == null) {
                        Logger.w(Anteater.TAG, "Save [No View] ");
                        return;
                    }
                    Context context = view2.getContext();
                    String msg = "action=" + action + ", context=" + context + ", view=" + context.getPackageName() + "/" + view;
                    View view3 = view;
                    if (view3 != null) {
                        view3.getClass().getName();
                        Context viewContext = view.getContext();
                        if (Anteater.this.mResources.isInited()) {
                            Logger.d(Anteater.TAG, "Save Begin : " + msg);
                            boolean result = false;
                            ArrayList<Rule> ruleList = Anteater.this.copyRuleList();
                            int ruleSize = ruleList.size();
                            for (int i = 0; i < ruleSize; i++) {
                                Rule rule = ruleList.get(i);
                                if (!rule.check(view)) {
                                    Logger.i(Anteater.TAG, "\n----------Save Skip Rule " + i + "----------\n" + rule);
                                } else {
                                    Logger.i(Anteater.TAG, "\n----------Save Apply Rule " + i + "----------\n" + rule);
                                    try {
                                        JSONObject resultObject = rule.extract(action, view);
                                        if (resultObject != null) {
                                            result |= Anteater.this.saveResult(viewContext, rule, resultObject);
                                        }
                                    } catch (JSONException e) {
                                        Logger.w(Anteater.TAG, e.getMessage(), e);
                                    } catch (Exception e2) {
                                        Logger.e(Anteater.TAG, e2.getMessage(), e2);
                                    }
                                }
                            }
                            if (result) {
                                Anteater.this.saveSuccess(viewContext, callback);
                            } else {
                                Anteater.this.saveFailed(viewContext, callback, ruleSize);
                            }
                            Logger.d(Anteater.TAG, "Save Finish : " + msg);
                            return;
                        }
                        Logger.w(Anteater.TAG, "Save [Not Init] : " + msg);
                        ResultData result2 = new ResultData();
                        result2.setError(ErrorCode.NOT_INIT);
                        callback.onSaveResult(viewContext, result2);
                        return;
                    }
                    Logger.w(Anteater.TAG, "Save [No View] : " + msg);
                    ResultData result3 = new ResultData();
                    result3.setError(ErrorCode.NO_VIEW);
                    callback.onSaveResult(context, result3);
                }
            });
        } else {
            Logger.e(TAG, "Save : No WorkHandler");
        }
    }

    public void check(final View view, final CheckCallback checkCallback) {
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: cn.teddymobile.free.anteater.Anteater.3
                @Override // java.lang.Runnable
                public void run() {
                    String viewName = view.getClass().getName();
                    if (Anteater.this.mResources.isInited()) {
                        Logger.i(Anteater.TAG, "Check View = " + viewName);
                        ArrayList<Rule> ruleList = Anteater.this.copyRuleList();
                        int ruleSize = ruleList.size();
                        for (int i = 0; i < ruleSize; i++) {
                            Rule rule = ruleList.get(i);
                            Logger.i(Anteater.TAG, "\n----------Check Rule " + i + "----------\n" + rule);
                            if (rule.check(view)) {
                                CheckCallback checkCallback2 = checkCallback;
                                if (checkCallback2 != null) {
                                    checkCallback2.onSuccess();
                                    return;
                                }
                                return;
                            }
                        }
                        CheckCallback checkCallback3 = checkCallback;
                        if (checkCallback3 != null) {
                            checkCallback3.onFailure();
                            return;
                        }
                        return;
                    }
                    Logger.d(Anteater.TAG, "Check [Not Init] : view=" + viewName);
                    CheckCallback checkCallback4 = checkCallback;
                    if (checkCallback4 != null) {
                        checkCallback4.onFailure();
                    }
                }
            });
        } else {
            Logger.e(TAG, "Check : No WorkHandler");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<Rule> copyRuleList() {
        ArrayList<Rule> arrayList;
        ArrayList<Rule> ruleList = this.mResources.getRuleList();
        synchronized (ruleList) {
            arrayList = new ArrayList<>(ruleList);
        }
        return arrayList;
    }

    private String formatText(String text) {
        if (text == null || "true".equals(text) || "false".equals(text)) {
            return "";
        }
        return text;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String extractTitle(JSONObject resultObject) {
        String data = null;
        try {
            data = resultObject.getString("title");
        } catch (JSONException e) {
            Logger.w(TAG, e.getMessage(), e);
        } catch (Exception e2) {
            Logger.e(TAG, e2.getMessage(), e2);
        }
        if (TextUtils.isEmpty(data)) {
            Logger.d(TAG, "loadFromWindowList-->title");
            data = extractValueFromWindowList(resultObject, "title");
        }
        return formatText(data);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String extractUrl(JSONObject resultObject) {
        String data = null;
        try {
            data = resultObject.getString(Telephony.WapPush.URL);
        } catch (JSONException e) {
            Logger.w(TAG, e.getMessage(), e);
        } catch (Exception e2) {
            Logger.e(TAG, e2.getMessage(), e2);
        }
        if (TextUtils.isEmpty(data)) {
            Logger.d(TAG, "loadFromWindowList-->url");
            data = extractValueFromWindowList(resultObject, Telephony.WapPush.URL);
        }
        return formatText(data);
    }

    private String extractValueFromWindowList(JSONObject resultObject, String key) {
        String result;
        JSONArray jsonArray = null;
        try {
            jsonArray = resultObject.getJSONArray("window_list");
        } catch (Exception ex) {
            Logger.w(TAG, ex.getMessage(), ex);
        }
        if (jsonArray != null) {
            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    result = object.getString(key);
                } catch (Exception e) {
                }
                if (!TextUtils.isEmpty(result)) {
                    Logger.d(TAG, "loadFromWindowList-->" + key + "-->" + result);
                    return result;
                }
                continue;
            }
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSuccess(Context context, ProcessCallback callback, ArrayList<QueryData> queryList) {
        String packageName = context.getPackageName();
        Logger.i(TAG, "processSuccess : " + packageName + ", queryList=" + queryList.size());
        ResultData result = new ResultData();
        result.setQueryList(queryList);
        callback.onProcessResult(context, result);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processFailed(Context context, ProcessCallback callback, int ruleSize) {
        String packageName = context.getPackageName();
        Logger.i(TAG, "processFailed : " + packageName);
        ResultData result = new ResultData();
        if (ruleSize > 0) {
            result.setError(ErrorCode.NOT_FOUND);
        } else {
            result.setError(ErrorCode.UNSUPPORT);
        }
        callback.onProcessResult(context, result);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean saveResult(Context context, Rule rule, JSONObject resultObject) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("result", resultObject);
        object.put(JSON_FIELD_PARSER, rule.getParser());
        Uri uri = new Uri.Builder().scheme(OplusTelephonyManager.BUNDLE_CONTENT).authority(UriConstants.AUTHORITY).path("result").build();
        ContentValues values = new ContentValues();
        values.put("package_name", rule.getPackageName());
        values.put("version", rule.getVersion());
        values.put(UriConstants.RESULT_COLUMN_SCENE, rule.getScene());
        values.put("data", object.toString());
        Uri insertUri = context.getContentResolver().insert(uri, values);
        String str = TAG;
        Logger.i(str, "\n----------saveResult : uri=" + uri + ", insertUri=" + insertUri + "\n");
        Logger.i(str, "\n----------saveResult : values=" + values + "\n");
        return insertUri != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveSuccess(Context context, SaveCallback callback) {
        ResultData result = new ResultData();
        callback.onSaveResult(context, result);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveFailed(Context context, SaveCallback callback, int ruleSize) {
        ResultData result = new ResultData();
        if (ruleSize > 0) {
            result.setError(ErrorCode.SAVE_FAILED);
        } else {
            result.setError(ErrorCode.UNSUPPORT);
        }
        callback.onSaveResult(context, result);
    }

    /* loaded from: classes.dex */
    public static class ResultData {
        private final ArrayList<QueryData> mQueryList = new ArrayList<>();
        private ErrorCode mError = ErrorCode.NONE;

        public void setQueryList(ArrayList<QueryData> queryList) {
            synchronized (this.mQueryList) {
                this.mQueryList.clear();
                if (queryList != null) {
                    synchronized (queryList) {
                        this.mQueryList.addAll(queryList);
                    }
                }
            }
        }

        public void setError(ErrorCode error) {
            this.mError = error;
        }

        public ArrayList<QueryData> getQueryList() {
            ArrayList<QueryData> arrayList;
            synchronized (this.mQueryList) {
                arrayList = this.mQueryList;
            }
            return arrayList;
        }

        public ErrorCode getError() {
            return this.mError;
        }
    }

    /* loaded from: classes.dex */
    public static class QueryData {
        private String mTitle = null;
        private String mUrl = null;

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            QueryData other = (QueryData) object;
            if (TextUtils.equals(this.mTitle, other.mTitle) && TextUtils.equals(this.mUrl, other.mUrl)) {
                return true;
            }
            return false;
        }

        public boolean isValid() {
            return (TextUtils.isEmpty(this.mTitle) && TextUtils.isEmpty(this.mUrl)) ? false : true;
        }

        public void setTitle(String title) {
            this.mTitle = title;
        }

        public void setUrl(String url) {
            this.mUrl = url;
        }

        public String getTitle() {
            return this.mTitle;
        }

        public String getUrl() {
            return this.mUrl;
        }
    }
}
