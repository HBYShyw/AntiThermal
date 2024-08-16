package cn.teddymobile.free.anteater.resources;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import cn.teddymobile.free.anteater.logger.Logger;
import cn.teddymobile.free.anteater.rule.Rule;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class RuleResourcesClient {
    private static final String POSTFIX_PARSER = "Parser";
    private static final String TAG = RuleResourcesClient.class.getSimpleName();
    private final ArrayList<Rule> mRuleList = new ArrayList<>();
    private final ArrayList<String> mSceneList = new ArrayList<>();
    private boolean mInited = false;

    /* loaded from: classes.dex */
    public interface LoadCallback {
        void onLoadResult(Context context, boolean z, boolean z2, ArrayList<String> arrayList);
    }

    public ArrayList<Rule> getRuleList() {
        ArrayList<Rule> arrayList;
        synchronized (this.mRuleList) {
            arrayList = this.mRuleList;
        }
        return arrayList;
    }

    public ArrayList<String> getSceneList() {
        ArrayList<String> arrayList;
        synchronized (this.mSceneList) {
            arrayList = this.mSceneList;
        }
        return arrayList;
    }

    public boolean isInited() {
        boolean z;
        synchronized (RuleResourcesClient.class) {
            z = this.mInited;
        }
        return z;
    }

    public void loadRule(Context context, String data, LoadCallback callback) {
        String str;
        String str2;
        synchronized (RuleResourcesClient.class) {
            long start = SystemClock.uptimeMillis();
            boolean noRule = true;
            try {
                try {
                    synchronized (this.mRuleList) {
                        this.mRuleList.clear();
                    }
                    synchronized (this.mSceneList) {
                        this.mSceneList.clear();
                    }
                    if (!TextUtils.isEmpty(data)) {
                        JSONArray ruleArray = new JSONArray(data);
                        for (int i = 0; i < ruleArray.length(); i++) {
                            JSONObject ruleObject = ruleArray.getJSONObject(i);
                            Rule rule = new Rule();
                            rule.loadFromJSON(ruleObject);
                            synchronized (this.mRuleList) {
                                this.mRuleList.add(rule);
                            }
                            String scene = extractScene(rule);
                            synchronized (this.mSceneList) {
                                if (!this.mSceneList.contains(scene)) {
                                    this.mSceneList.add(scene);
                                }
                            }
                        }
                        noRule = false;
                    }
                } catch (JSONException e) {
                    str = TAG;
                    Logger.w(str, e.getMessage(), e);
                    long spend = SystemClock.uptimeMillis() - start;
                    str2 = "loadRule : spend=" + spend + "ms";
                    Logger.d(str, str2);
                    onLoadResult(context, noRule, callback);
                } catch (Exception e2) {
                    str = TAG;
                    Logger.e(str, e2.getMessage(), e2);
                    long spend2 = SystemClock.uptimeMillis() - start;
                    str2 = "loadRule : spend=" + spend2 + "ms";
                    Logger.d(str, str2);
                    onLoadResult(context, noRule, callback);
                }
                onLoadResult(context, noRule, callback);
            } finally {
                long spend3 = SystemClock.uptimeMillis() - start;
                Logger.d(TAG, "loadRule : spend=" + spend3 + "ms");
            }
        }
    }

    private String extractScene(Rule rule) {
        String parser = rule.getParser();
        if (parser.endsWith(POSTFIX_PARSER)) {
            return parser.substring(0, parser.length() - POSTFIX_PARSER.length());
        }
        return parser;
    }

    private void onLoadResult(Context context, boolean noRule, LoadCallback callback) {
        int ruleSize;
        synchronized (this.mRuleList) {
            ruleSize = this.mRuleList.size();
        }
        Logger.i(TAG, "onLoadResult : Size = " + ruleSize);
        this.mInited = true;
        boolean emptyRule = ruleSize < 1;
        if (callback != null) {
            synchronized (this.mSceneList) {
                callback.onLoadResult(context, noRule, emptyRule, this.mSceneList);
            }
        }
    }
}
