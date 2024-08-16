package com.oplus.ovoiceskillservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.oplus.ovoiceskillservice.utils.SkillActionUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SkillActionsActivity extends Activity {
    private static final String TAG = "OVSS.SkillActionsActivity";

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        Log.d(TAG, "onCreate");
        super.onCreate(bundle);
        try {
            SkillActionUtils.load(this);
            OVoiceSkillSDK.initialize(this, new OVoiceConnectionCallback() { // from class: com.oplus.ovoiceskillservice.SkillActionsActivity.1
                @Override // com.oplus.ovoiceskillservice.OVoiceConnectionCallback
                public void onServiceConnected() {
                    Log.d(SkillActionsActivity.TAG, "onServiceConnected");
                    try {
                        for (String str : SkillActionUtils.getActionListenerMap().keySet()) {
                            Map<String, Method> regActionMap = SkillActionUtils.getRegActionMap(str);
                            if (regActionMap != null && regActionMap.size() > 0) {
                                OVoiceSkillSDK.registerActionExecutionCallback(new ArrayList(regActionMap.keySet()), (SkillActionListenerWrapper) SkillActionUtils.getActionListener(str));
                            }
                        }
                    } catch (Exception e10) {
                        Log.e(SkillActionsActivity.TAG, "onServiceConnected error", e10);
                    }
                }

                @Override // com.oplus.ovoiceskillservice.OVoiceConnectionCallback
                public void onServiceDisconnected() {
                    Log.d(SkillActionsActivity.TAG, "onServiceDisconnected");
                }
            });
        } catch (Exception e10) {
            Log.e(TAG, "onCreate error", e10);
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        OVoiceSkillSDK.deinitialize();
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "new intent received");
    }

    @Override // android.app.Activity
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        finish();
    }

    @Override // android.app.Activity
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        if (getIntent() == null) {
            return;
        }
        try {
            String stringExtra = getIntent().getStringExtra("ovms_uri_path");
            Log.d(TAG, "OVMS_URI_PATH: " + stringExtra);
            if (stringExtra == null) {
                stringExtra = "";
            }
            SkillActionListenerWrapper skillActionListenerWrapper = (SkillActionListenerWrapper) SkillActionUtils.getActionListener(stringExtra);
            String stringExtra2 = getIntent().getStringExtra("ovms_call_args");
            if (skillActionListenerWrapper != null && stringExtra2 != null) {
                try {
                    JSONObject jSONObject = new JSONObject(stringExtra2);
                    HashMap hashMap = new HashMap();
                    for (String str : jSONObject.keySet()) {
                        hashMap.put(str, jSONObject.getString(str));
                    }
                    skillActionListenerWrapper.setParameters(hashMap);
                } catch (JSONException e10) {
                    Log.e(TAG, "json parse error", e10);
                }
            }
            OVoiceSkillSDK.newSkillSession(getIntent(), skillActionListenerWrapper);
        } catch (Exception unused) {
            Log.e(TAG, "onStart error");
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }
}
