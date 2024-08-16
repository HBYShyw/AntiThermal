package com.oplus.ovoiceskillservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.oplus.ovoicemanager.service.ActionRequest;
import com.oplus.ovoicemanager.service.IOVoiceSkillService;
import com.oplus.ovoicemanager.service.ISkillListener;
import com.oplus.ovoiceskillservice.utils.SkillActionUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SkillActionsService extends Service {
    private static final String TAG = "OVSS.SkillActionsService";

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return new ISkillListener.a() { // from class: com.oplus.ovoiceskillservice.SkillActionsService.1
            @Override // com.oplus.ovoicemanager.service.ISkillListener
            public void onActionExecution(String str, String str2, String str3) {
                SkillActionListener skillActionListener;
                Log.d(SkillActionsService.TAG, "ISkillListener.Stub.onActionExecution");
                try {
                    SkillSession skillSession = (SkillSession) OVoiceSkillProxy.getInstance().getSession(str);
                    if (skillSession == null || (skillActionListener = skillSession.mSkillActionListener) == null) {
                        return;
                    }
                    skillActionListener.onActionExecution(skillSession, str3);
                } catch (Exception e10) {
                    e10.printStackTrace();
                }
            }

            @Override // com.oplus.ovoicemanager.service.ISkillListener
            public void onCancel(String str) {
                SkillActionListener skillActionListener;
                Log.d(SkillActionsService.TAG, "ISkillListener.Stub.onCancel");
                try {
                    SkillSession skillSession = (SkillSession) OVoiceSkillProxy.getInstance().getSession(str);
                    if (skillSession == null || (skillActionListener = skillSession.mSkillActionListener) == null) {
                        return;
                    }
                    skillActionListener.onCancel(skillSession);
                } catch (Exception e10) {
                    e10.printStackTrace();
                }
            }

            @Override // com.oplus.ovoicemanager.service.ISkillListener
            public void onValueChanged(String str, String str2) {
                SkillActionListener skillActionListener;
                Log.d(SkillActionsService.TAG, "ISkillListener.Stub.onValueChanged");
                try {
                    SkillSession skillSession = (SkillSession) OVoiceSkillProxy.getInstance().getSession(str);
                    if (skillSession == null || (skillActionListener = skillSession.mSkillActionListener) == null) {
                        return;
                    }
                    skillActionListener.onValueChanged(skillSession, str2);
                } catch (Exception e10) {
                    e10.printStackTrace();
                }
            }

            @Override // com.oplus.ovoicemanager.service.ISkillListener
            public void startAction(ActionRequest actionRequest, IOVoiceSkillService iOVoiceSkillService) {
                Log.d(SkillActionsService.TAG, "startAction");
                try {
                    OVoiceSkillProxy.getInstance().initializeByOVoiceSkillService(this, iOVoiceSkillService, new OVoiceConnectionCallback() { // from class: com.oplus.ovoiceskillservice.SkillActionsService.1.1
                        @Override // com.oplus.ovoiceskillservice.OVoiceConnectionCallback
                        public void onServiceConnected() {
                            Log.d(SkillActionsService.TAG, "onServiceConnected");
                            try {
                                for (String str : SkillActionUtils.getActionListenerMap().keySet()) {
                                    Map<String, Method> regActionMap = SkillActionUtils.getRegActionMap(str);
                                    if (regActionMap != null && regActionMap.size() > 0) {
                                        OVoiceSkillSDK.registerActionExecutionCallback(new ArrayList(regActionMap.keySet()), (SkillActionListenerWrapper) SkillActionUtils.getActionListener(str));
                                    }
                                }
                            } catch (Exception e10) {
                                Log.e(SkillActionsService.TAG, "onServiceConnected error", e10);
                            }
                        }

                        @Override // com.oplus.ovoiceskillservice.OVoiceConnectionCallback
                        public void onServiceDisconnected() {
                            Log.d(SkillActionsService.TAG, "onServiceDisconnected");
                        }
                    });
                    String m10 = actionRequest.m();
                    if (m10 == null) {
                        m10 = "";
                    }
                    SkillActionListenerWrapper skillActionListenerWrapper = (SkillActionListenerWrapper) SkillActionUtils.getActionListener(m10);
                    String k10 = actionRequest.k();
                    if (skillActionListenerWrapper != null && k10 != null) {
                        try {
                            JSONObject jSONObject = new JSONObject(k10);
                            HashMap hashMap = new HashMap();
                            for (String str : jSONObject.keySet()) {
                                hashMap.put(str, jSONObject.getString(str));
                            }
                            skillActionListenerWrapper.setParameters(hashMap);
                        } catch (JSONException e10) {
                            Log.e(SkillActionsService.TAG, "jackson error", e10);
                        }
                    }
                    OVoiceSkillProxy.getInstance().newSkillSession(actionRequest, skillActionListenerWrapper);
                } catch (Exception e11) {
                    Log.e(SkillActionsService.TAG, "startAction error", e11);
                }
            }
        }.asBinder();
    }

    @Override // android.app.Service
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        SkillActionUtils.load(this);
    }
}
