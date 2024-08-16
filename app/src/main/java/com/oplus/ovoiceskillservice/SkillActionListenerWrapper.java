package com.oplus.ovoiceskillservice;

import android.util.Log;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SkillActionListenerWrapper extends SkillActionListener {
    private static final String TAG = "OVSS.SkillActionListenerWrapper";
    private Map<String, Method> cardObservers;
    private SkillActionListener listener;
    private Map<String, String> parameters;
    private Map<String, Method> regActions;

    public SkillActionListenerWrapper(SkillActionListener skillActionListener) {
        this.listener = skillActionListener;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x004d, code lost:
    
        if (r1 == 1) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:?, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void invokeCardObserver(SkillSession skillSession, String str) {
        Class<?>[] parameterTypes;
        try {
            Map<String, Method> map = this.cardObservers;
            if (map != null && !map.isEmpty() && str != null) {
                JSONObject jSONObject = new JSONObject(str);
                String string = jSONObject.getString("cardType");
                JSONObject jSONObject2 = new JSONObject(jSONObject.getString("payload"));
                char c10 = 65535;
                int hashCode = string.hashCode();
                if (hashCode != 1242559665) {
                    if (hashCode == 1966849656 && string.equals("SeekbarResult")) {
                        c10 = 1;
                    }
                } else if (string.equals("ToggleResult")) {
                    c10 = 0;
                }
                String string2 = jSONObject2.getString("itemName");
                String string3 = jSONObject2.getString("itemValue");
                Method method = this.cardObservers.get(string2);
                if (method == null || (parameterTypes = method.getParameterTypes()) == null || parameterTypes.length != 2) {
                    return;
                }
                if (!parameterTypes[1].equals(Boolean.class) && !parameterTypes[1].equals(Boolean.TYPE)) {
                    if (!parameterTypes[1].equals(Integer.class) && !parameterTypes[1].equals(Integer.TYPE)) {
                        if (!parameterTypes[1].equals(Float.class) && !parameterTypes[1].equals(Float.TYPE)) {
                            if (parameterTypes[1].equals(String.class)) {
                                method.invoke(this.listener, skillSession, string3);
                                return;
                            } else {
                                if (parameterTypes[1].equals(List.class)) {
                                    return;
                                }
                                parameterTypes[1].equals(Set.class);
                                return;
                            }
                        }
                        method.invoke(this.listener, skillSession, Float.valueOf(Float.parseFloat(string3)));
                        return;
                    }
                    method.invoke(this.listener, skillSession, Integer.valueOf(Integer.parseInt(string3)));
                    return;
                }
                method.invoke(this.listener, skillSession, Boolean.valueOf(Boolean.parseBoolean(string3)));
                return;
            }
            Log.e(TAG, "cardObservers or json is empty");
        } catch (Exception e10) {
            Log.e(TAG, "invokeCardObserver error", e10);
        }
    }

    public SkillActionListener getListener() {
        return this.listener;
    }

    @Override // com.oplus.ovoiceskillservice.SkillActionListener
    public void onActionExecution(ISkillSession iSkillSession, String str) {
        Method method;
        Log.d(TAG, "onActionExecution");
        try {
            SkillActionListener skillActionListener = this.listener;
            if (skillActionListener != null) {
                skillActionListener.onActionExecution(iSkillSession, str);
            }
            String actionID = iSkillSession.getActionID();
            Log.d(TAG, "actionId: " + actionID);
            if (actionID == null || (method = this.regActions.get(actionID)) == null) {
                return;
            }
            try {
                method.invoke(this.listener, iSkillSession, str);
            } catch (Exception e10) {
                Log.e(TAG, "method.invoke error", e10);
            }
        } catch (Exception e11) {
            Log.e(TAG, "error", e11);
        }
    }

    @Override // com.oplus.ovoiceskillservice.SkillActionListener
    public void onCancel(ISkillSession iSkillSession) {
        Log.d(TAG, "onCancel");
        try {
            SkillActionListener skillActionListener = this.listener;
            if (skillActionListener != null) {
                skillActionListener.onCancel(iSkillSession);
            }
        } catch (Exception e10) {
            Log.e(TAG, "error", e10);
        }
    }

    @Override // com.oplus.ovoiceskillservice.SkillActionListener
    public void onSessionCreated(ISkillSession iSkillSession) {
        Log.d(TAG, "skillSession:" + iSkillSession);
        try {
            iSkillSession.setParameters(this.parameters);
            Log.d(TAG, "listener:" + this.listener);
            SkillActionListener skillActionListener = this.listener;
            if (skillActionListener != null) {
                skillActionListener.onSessionCreated(iSkillSession);
            }
        } catch (Exception e10) {
            Log.e(TAG, "error", e10);
        }
    }

    @Override // com.oplus.ovoiceskillservice.SkillActionListener
    public void onValueChanged(ISkillSession iSkillSession, String str) {
        Log.d(TAG, "onValueChanged, json:" + str);
        try {
            SkillActionListener skillActionListener = this.listener;
            if (skillActionListener != null) {
                skillActionListener.onValueChanged(iSkillSession, str);
                invokeCardObserver((SkillSession) iSkillSession, str);
            }
        } catch (Exception e10) {
            Log.e(TAG, "error", e10);
        }
    }

    public void setCardObservers(Map<String, Method> map) {
        this.cardObservers = map;
    }

    public void setParameters(Map<String, String> map) {
        this.parameters = map;
    }

    public void setRegActions(Map<String, Method> map) {
        this.regActions = map;
    }
}
