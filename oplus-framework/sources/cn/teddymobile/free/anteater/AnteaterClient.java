package cn.teddymobile.free.anteater;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.provider.oplus.Telephony;
import android.telephony.OplusTelephonyManager;
import android.text.TextUtils;
import android.view.View;
import cn.teddymobile.free.anteater.helper.AnteaterHelper;
import cn.teddymobile.free.anteater.logger.Logger;
import cn.teddymobile.free.anteater.resources.RuleResourcesClient;
import cn.teddymobile.free.anteater.resources.UriConstants;
import cn.teddymobile.free.anteater.rule.Rule;
import cn.teddymobile.free.anteater.rule.html.javascript.JavaScriptRule;
import cn.teddymobile.free.anteater.rule.utils.DetectionFileUtils;
import cn.teddymobile.free.anteater.rule.utils.ReflectionUtils;
import cn.teddymobile.free.anteater.rule.utils.RuleUtils;
import cn.teddymobile.free.anteater.rule.utils.ViewHierarchyUtils;
import com.oplus.direct.OplusDirectFindCmd;
import com.oplus.direct.OplusDirectFindCmds;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class AnteaterClient {
    public static final String ACTION_CLICK = "click";
    public static final String ACTION_DISPLAY = "display";
    private static final String EMPTY = "";
    private static final String JSON_FIELD_PARSER = "parser";
    private static final String JSON_FIELD_RESULT = "result";
    private static final String TAG = AnteaterClient.class.getSimpleName();
    private static volatile AnteaterClient sInstance = null;
    private Handler mWorkHandler = null;

    /* loaded from: classes.dex */
    public interface BaseCallback {
        Handler createWorkHandler(Context context, String str, int i);

        boolean isSettingOff(Context context);
    }

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
    public interface ProcessCallback extends BaseCallback {
        void onProcessResult(Context context, ResultData resultData);
    }

    /* loaded from: classes.dex */
    public interface SaveCallback extends BaseCallback {
        void onSaveResult(Context context, ResultData resultData);
    }

    public static AnteaterClient getInstance() {
        if (sInstance == null) {
            synchronized (AnteaterClient.class) {
                if (sInstance == null) {
                    sInstance = new AnteaterClient();
                }
            }
        }
        return sInstance;
    }

    private AnteaterClient() {
        Logger.d(TAG, "<init>()");
    }

    public void init() {
    }

    public void release() {
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public Handler getWorkHandler() {
        return this.mWorkHandler;
    }

    public void loadRule(Context context, String data, RuleResourcesClient.LoadCallback callback) {
    }

    /* renamed from: cn.teddymobile.free.anteater.AnteaterClient$11, reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass11 {
        static final /* synthetic */ int[] $SwitchMap$com$oplus$direct$OplusDirectFindCmds;

        static {
            int[] iArr = new int[OplusDirectFindCmds.values().length];
            $SwitchMap$com$oplus$direct$OplusDirectFindCmds = iArr;
            try {
                iArr[OplusDirectFindCmds.FIND_FAVORITE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.FIND_CONTENT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.FIND_ACTION_CLICK.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.FIND_ACTION_DOUBLE_CLICK.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.FIND_ACTION_LONG_CLICK.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.FIND_ACTIVITY_INFO.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.FIND_VIEW_INFO.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.FIND_REFLECTION_INFO.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    public void process(String action, View view, ProcessCallback callback, String param) {
        OplusDirectFindCmds cmds = OplusDirectFindCmd.getCmdTypeFromJSON(param);
        switch (AnonymousClass11.$SwitchMap$com$oplus$direct$OplusDirectFindCmds[cmds.ordinal()]) {
            case 1:
                findFavorite(action, view, callback, param);
                return;
            case 2:
                findContent(action, view, callback, param);
                return;
            case 3:
                findActionClick("click", view, callback, param);
                return;
            case 4:
                findActionDoubleClick("click", view, callback, param);
                return;
            case 5:
                findActionLongClick("click", view, callback, param);
                return;
            case 6:
                findActivityInfo(action, view, callback, param);
                return;
            case 7:
                findViewInfo(action, view, callback, param);
                return;
            case 8:
                findReflectionInfo(action, view, callback, param);
                return;
            default:
                return;
        }
    }

    public void findFavorite(final String action, final View view, final ProcessCallback callback, final String param) {
        if (view == null) {
            Logger.w(TAG, "Process [No View]");
            return;
        }
        final Context context = view.getContext();
        ensureWorkHandler(context, callback);
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: cn.teddymobile.free.anteater.AnteaterClient.1
                @Override // java.lang.Runnable
                public void run() {
                    List<Rule> ruleList;
                    String msg = "action=" + action + ", context=" + context + ", view=" + context.getPackageName() + "/" + view;
                    String viewName = view.getClass().getName();
                    if (callback.isSettingOff(context)) {
                        Context viewContext = context;
                        Logger.w(AnteaterClient.TAG, "Process [Setting Off] : " + msg);
                        ResultData result = new ResultData();
                        result.setError(ErrorCode.SETTING_OFF);
                        callback.onProcessResult(viewContext, result);
                        return;
                    }
                    Logger.d(AnteaterClient.TAG, "Process Begin : View = " + viewName + ". Action = " + action);
                    ArrayList<QueryData> queryList = new ArrayList<>();
                    String rule_param = OplusDirectFindCmd.getRuleParamFromJSON(param);
                    if (!TextUtils.isEmpty(rule_param)) {
                        ruleList = RuleUtils.parseRule(rule_param);
                    } else {
                        ruleList = AnteaterClient.this.getRuleList(context);
                    }
                    int ruleSize = ruleList.size();
                    for (int i = 0; i < ruleSize; i++) {
                        Rule rule = ruleList.get(i);
                        if (!rule.check(action, view)) {
                            Logger.i(AnteaterClient.TAG, "\n----------Process Skip Rule " + i + "----------\n" + rule);
                        } else {
                            Logger.i(AnteaterClient.TAG, "\n----------Process Apply Rule " + i + "----------\n" + rule);
                            try {
                                JSONObject resultObject = rule.extract(action, view);
                                if (resultObject != null) {
                                    resultObject.put("stamp", AnteaterClient.this.getStampFromParam(param));
                                    QueryData data = new QueryData();
                                    data.setTitle(AnteaterClient.this.extractTitle(resultObject));
                                    data.setUrl(AnteaterClient.this.extractUrl(resultObject));
                                    data.setExtra(AnteaterClient.this.extractExtra(resultObject));
                                    if (data.isValid() && !queryList.contains(data)) {
                                        queryList.add(data);
                                    }
                                }
                            } catch (JSONException e) {
                                Logger.w(AnteaterClient.TAG, e.getMessage(), e);
                            } catch (Exception e2) {
                                Logger.e(AnteaterClient.TAG, e2.getMessage(), e2);
                            }
                        }
                    }
                    int i2 = queryList.size();
                    if (i2 > 0) {
                        AnteaterClient.this.processSuccess(context, callback, queryList);
                    } else {
                        AnteaterClient.this.processFailed(context, callback, ruleSize);
                    }
                    Logger.d(AnteaterClient.TAG, "Process Finish : View = " + viewName + ". Action = " + action);
                }
            });
        } else {
            Logger.e(TAG, "Process : No WorkHandler");
        }
    }

    public void save(final String action, final View view, final SaveCallback callback) {
        if (view == null) {
            Logger.w(TAG, "Save [No View] ");
            return;
        }
        final Context context = view.getContext();
        ensureWorkHandler(context, callback);
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: cn.teddymobile.free.anteater.AnteaterClient.2
                @Override // java.lang.Runnable
                public void run() {
                    String msg = "action=" + action + ", context=" + context + ", view=" + context.getPackageName() + "/" + view;
                    view.getClass().getName();
                    Context viewContext = view.getContext();
                    Logger.d(AnteaterClient.TAG, "Save Begin : " + msg);
                    boolean result = false;
                    List<Rule> ruleList = AnteaterClient.this.getRuleList(context);
                    int ruleSize = ruleList.size();
                    for (int i = 0; i < ruleSize; i++) {
                        Rule rule = ruleList.get(i);
                        if (!rule.check(action, view)) {
                            Logger.i(AnteaterClient.TAG, "\n----------Save Skip Rule " + i + "----------\n" + rule);
                        } else {
                            Logger.i(AnteaterClient.TAG, "\n----------Save Apply Rule " + i + "----------\n" + rule);
                            try {
                                JSONObject resultObject = rule.extract(action, view);
                                if (resultObject != null) {
                                    result |= AnteaterClient.this.saveResult(viewContext, rule, resultObject);
                                }
                            } catch (JSONException e) {
                                Logger.w(AnteaterClient.TAG, e.getMessage(), e);
                            } catch (Exception e2) {
                                Logger.e(AnteaterClient.TAG, e2.getMessage(), e2);
                            }
                        }
                    }
                    if (result) {
                        AnteaterClient.this.saveSuccess(viewContext, callback);
                    } else {
                        AnteaterClient.this.saveFailed(viewContext, callback, ruleSize);
                    }
                    Logger.d(AnteaterClient.TAG, "Save Finish : " + msg);
                }
            });
        } else {
            Logger.e(TAG, "Save : No WorkHandler");
        }
    }

    public void check(final View view, final String action, final CheckCallback checkCallback) {
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: cn.teddymobile.free.anteater.AnteaterClient.3
                @Override // java.lang.Runnable
                public void run() {
                    View view2 = view;
                    if (view2 == null) {
                        Logger.w(AnteaterClient.TAG, "Check [No View] ");
                        return;
                    }
                    String viewName = view2.getClass().getName();
                    Logger.i(AnteaterClient.TAG, "Check View = " + viewName);
                    List<Rule> ruleList = AnteaterClient.this.getRuleList(view.getContext());
                    int ruleSize = ruleList.size();
                    for (int i = 0; i < ruleSize; i++) {
                        Rule rule = ruleList.get(i);
                        Logger.i(AnteaterClient.TAG, "\n----------Check Rule " + i + "----------\n" + rule);
                        if (rule.check(action, view)) {
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
                    }
                }
            });
        } else {
            Logger.e(TAG, "Check : No WorkHandler");
        }
    }

    public void findContent(final String action, final View view, final ProcessCallback callback, final String param) {
        if (view == null) {
            Logger.w(TAG, "Process [No View]");
            return;
        }
        final Context context = view.getContext();
        ensureWorkHandler(context, callback);
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: cn.teddymobile.free.anteater.AnteaterClient.4
                @Override // java.lang.Runnable
                public void run() {
                    String msg = "action=" + action + ", context=" + context + ", view=" + context.getPackageName() + "/" + view;
                    String viewName = view.getClass().getName();
                    if (callback.isSettingOff(context)) {
                        Context viewContext = context;
                        Logger.w(AnteaterClient.TAG, "Process [Setting Off] : " + msg);
                        ResultData result = new ResultData();
                        result.setError(ErrorCode.SETTING_OFF);
                        callback.onProcessResult(viewContext, result);
                        return;
                    }
                    Logger.d(AnteaterClient.TAG, "Process Begin : View = " + viewName + ". Action = " + action);
                    ArrayList<QueryData> queryList = new ArrayList<>();
                    List<Rule> ruleList = RuleUtils.parseRule(OplusDirectFindCmd.getRuleParamFromJSON(param));
                    int ruleSize = ruleList.size();
                    for (int i = 0; i < ruleSize; i++) {
                        Rule rule = ruleList.get(i);
                        if (!rule.check(action, view)) {
                            Logger.i(AnteaterClient.TAG, "\n----------Process Skip Rule " + i + "----------\n" + rule);
                        } else {
                            Logger.i(AnteaterClient.TAG, "\n----------Process Apply Rule " + i + "----------\n" + rule);
                            try {
                                JSONObject resultObject = rule.extract(action, view);
                                if (resultObject != null) {
                                    QueryData data = new QueryData();
                                    data.setTitle(AnteaterClient.this.extractTitle(resultObject));
                                    data.setUrl(AnteaterClient.this.extractUrl(resultObject));
                                    data.setContent(AnteaterClient.this.extractContent(resultObject));
                                    if (data.isValid() && !queryList.contains(data)) {
                                        queryList.add(data);
                                    }
                                }
                            } catch (JSONException e) {
                                Logger.w(AnteaterClient.TAG, e.getMessage(), e);
                            } catch (Exception e2) {
                                Logger.e(AnteaterClient.TAG, e2.getMessage(), e2);
                            }
                        }
                    }
                    int i2 = queryList.size();
                    if (i2 > 0) {
                        AnteaterClient.this.processSuccess(context, callback, queryList);
                    } else {
                        AnteaterClient.this.processFailed(context, callback, ruleSize);
                    }
                    Logger.d(AnteaterClient.TAG, "Process Finish : View = " + viewName + ". Action = " + action);
                }
            });
        } else {
            Logger.e(TAG, "Process : No WorkHandler");
        }
    }

    public void findActionClick(String action, View view, ProcessCallback callback, String param) {
        if (view == null) {
            Logger.w(TAG, "Process [No View]");
            return;
        }
        Context context = view.getContext();
        ensureWorkHandler(context, callback);
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: cn.teddymobile.free.anteater.AnteaterClient.5
                @Override // java.lang.Runnable
                public void run() {
                }
            });
        } else {
            Logger.e(TAG, "Process : No WorkHandler");
        }
    }

    public void findActionDoubleClick(String action, View view, ProcessCallback callback, String param) {
        if (view == null) {
            Logger.w(TAG, "Process [No View]");
            return;
        }
        Context context = view.getContext();
        ensureWorkHandler(context, callback);
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: cn.teddymobile.free.anteater.AnteaterClient.6
                @Override // java.lang.Runnable
                public void run() {
                }
            });
        } else {
            Logger.e(TAG, "Process : No WorkHandler");
        }
    }

    public void findActionLongClick(String action, View view, ProcessCallback callback, String param) {
        if (view == null) {
            Logger.w(TAG, "Process [No View]");
            return;
        }
        Context context = view.getContext();
        ensureWorkHandler(context, callback);
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: cn.teddymobile.free.anteater.AnteaterClient.7
                @Override // java.lang.Runnable
                public void run() {
                }
            });
        } else {
            Logger.e(TAG, "Process : No WorkHandler");
        }
    }

    public void findActivityInfo(String action, View view, ProcessCallback callback, String param) {
        if (view == null) {
            Logger.w(TAG, "Process [No View]");
            return;
        }
        final Context context = view.getContext();
        ensureWorkHandler(context, callback);
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: cn.teddymobile.free.anteater.AnteaterClient.8
                @Override // java.lang.Runnable
                public void run() {
                    AnteaterHelper helper = AnteaterHelper.getInstance();
                    helper.logActivityInfo((Activity) context);
                }
            });
        } else {
            Logger.e(TAG, "Process : No WorkHandler");
        }
    }

    public void findViewInfo(final String action, final View view, final ProcessCallback callback, final String param) {
        if (view == null) {
            Logger.w(TAG, "Process [No View]");
            return;
        }
        final Context context = view.getContext();
        ensureWorkHandler(context, callback);
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: cn.teddymobile.free.anteater.AnteaterClient.9
                @Override // java.lang.Runnable
                public void run() {
                    String viewName = view.getClass().getName();
                    ArrayList<QueryData> queryList = new ArrayList<>();
                    try {
                        ArrayList<String> list = JavaScriptRule.getWebViewClassNameFromJSON(param);
                        ViewHierarchyUtils.addThirdPartyWebViewClassNames(list);
                        JavaScriptRule rule = new JavaScriptRule();
                        String title = rule.getTitle(view);
                        String url = rule.getUrl(view);
                        String html = rule.getHtml(view);
                        String extra = rule.getExtra(view, JavaScriptRule.getH5ExtraCodeFromJSON(param));
                        JSONObject result = new JSONObject();
                        result.put("stamp", AnteaterClient.this.getStampFromParam(param));
                        result.put(JavaScriptRule.JSON_FIELD_ATTRIBUTE_HTML, html);
                        result.put(JavaScriptRule.JSON_FIELD_ATTRIBUTE_TITLE, title);
                        result.put(JavaScriptRule.JSON_FIELD_ATTRIBUTE_URL, url);
                        result.put(JavaScriptRule.JSON_FIELD_ATTRIBUTE_EXTRA, extra);
                        QueryData data = new QueryData();
                        data.setTitle(title);
                        data.setUrl(url);
                        data.setExtra(AnteaterClient.this.extractExtra(result));
                        queryList.add(data);
                    } catch (JSONException e) {
                        Logger.w(AnteaterClient.TAG, e.getMessage(), e);
                    } catch (Exception e2) {
                        Logger.e(AnteaterClient.TAG, e2.getMessage(), e2);
                    }
                    if (queryList.size() > 0) {
                        AnteaterClient.this.processSuccess(context, callback, queryList);
                    } else {
                        AnteaterClient.this.processFailed(context, callback, 1);
                    }
                    Logger.d(AnteaterClient.TAG, "Process Finish : View = " + viewName + ". Action = " + action);
                }
            });
        } else {
            Logger.e(TAG, "Process : No WorkHandler");
        }
    }

    public void findReflectionInfo(String action, View view, ProcessCallback callback, String param) {
        if (view == null) {
            Logger.w(TAG, "Process [No View]");
            return;
        }
        final Context context = view.getContext();
        ensureWorkHandler(context, callback);
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: cn.teddymobile.free.anteater.AnteaterClient.10
                @Override // java.lang.Runnable
                public void run() {
                    List<String> excludeDeepFirstClassList = DetectionFileUtils.loadRestrictions(context.getFilesDir().getAbsolutePath() + File.separator + "excludeDeepFirstClassList.txt");
                    List<String> excludeDeepFirstPackagePrefixList = DetectionFileUtils.loadRestrictions(context.getFilesDir().getAbsolutePath() + File.separator + "excludeDeepFirstPackagePrefixList.txt");
                    ReflectionUtils.addExcludeDeepFirstClassList(excludeDeepFirstClassList);
                    ReflectionUtils.addExcludeDeepFirstPackagePrefixList(excludeDeepFirstPackagePrefixList);
                    List<String> excludeFieldClassList = DetectionFileUtils.loadRestrictions(context.getFilesDir().getAbsolutePath() + File.separator + "excludeFieldClassList.txt");
                    List<String> excludeFieldPackagePrefixList = DetectionFileUtils.loadRestrictions(context.getFilesDir().getAbsolutePath() + File.separator + "excludeFieldPackagePrefixList.txt");
                    ReflectionUtils.addExcludeFieldClassList(excludeFieldClassList);
                    ReflectionUtils.addExcludeFieldPackagePrefixList(excludeFieldPackagePrefixList);
                    ReflectionUtils.printReflectionResult(context, context.getFilesDir().getAbsolutePath() + File.separator + "Reflection.json");
                }
            });
        } else {
            Logger.e(TAG, "Process : No WorkHandler");
        }
    }

    private void ensureWorkHandler(Context context, BaseCallback callback) {
        if (this.mWorkHandler == null) {
            this.mWorkHandler = callback.createWorkHandler(context, "anteater", 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<Rule> getRuleList(Context context) {
        String result = RuleUtils.queryRuleFromProvider(context);
        return RuleUtils.parseRule(result);
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

    /* JADX INFO: Access modifiers changed from: private */
    public String extractContent(JSONObject resultObject) {
        String data = null;
        try {
            data = resultObject.getString(OplusTelephonyManager.BUNDLE_CONTENT);
        } catch (JSONException e) {
            Logger.w(TAG, e.getMessage(), e);
        } catch (Exception e2) {
            Logger.e(TAG, e2.getMessage(), e2);
        }
        if (TextUtils.isEmpty(data)) {
            Logger.d(TAG, "loadFromWindowList-->content");
            data = extractValueFromWindowList(resultObject, OplusTelephonyManager.BUNDLE_CONTENT);
        }
        return formatText(data);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String extractExtra(JSONObject resultObject) {
        JSONObject jsonObject = new JSONObject();
        try {
            Iterator<String> keys = resultObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (!key.equals("title") && !key.equals(Telephony.WapPush.URL) && !key.equals(OplusTelephonyManager.BUNDLE_CONTENT)) {
                    jsonObject.put(key, resultObject.getString(key));
                }
            }
        } catch (JSONException e) {
            Logger.w(TAG, e.getMessage(), e);
        } catch (Exception e2) {
            Logger.e(TAG, e2.getMessage(), e2);
        }
        return jsonObject.toString();
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

    /* JADX INFO: Access modifiers changed from: private */
    public String getStampFromParam(String json) {
        try {
            JSONObject paramObj = new JSONObject(json);
            return paramObj.getString("stamp");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
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
        private String mContent = "";
        private String mExtra = "";

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            QueryData other = (QueryData) object;
            if (TextUtils.equals(this.mTitle, other.mTitle) && TextUtils.equals(this.mUrl, other.mUrl) && TextUtils.equals(this.mContent, other.mContent)) {
                return true;
            }
            return false;
        }

        public boolean isValid() {
            return (TextUtils.isEmpty(this.mTitle) && TextUtils.isEmpty(this.mUrl) && TextUtils.isEmpty(this.mContent)) ? false : true;
        }

        public void setTitle(String title) {
            this.mTitle = title;
        }

        public void setUrl(String url) {
            this.mUrl = url;
        }

        public void setContent(String content) {
            this.mContent = content;
        }

        public void setExtra(String json) {
            this.mExtra = json;
        }

        public String getTitle() {
            return this.mTitle;
        }

        public String getUrl() {
            return this.mUrl;
        }

        public String getContent() {
            return this.mContent;
        }

        public String getExtra() {
            return this.mExtra;
        }
    }
}
