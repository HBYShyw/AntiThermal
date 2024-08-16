package com.oplus.ovoiceskillservice.utils;

import android.content.Context;
import android.util.Log;
import com.oplus.ovoiceskillservice.Autowired;
import com.oplus.ovoiceskillservice.CardObserver;
import com.oplus.ovoiceskillservice.ISkillSession;
import com.oplus.ovoiceskillservice.RegAction;
import com.oplus.ovoiceskillservice.SkillActionListener;
import com.oplus.ovoiceskillservice.SkillActionListenerWrapper;
import com.oplus.ovoiceskillservice.SkillActions;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class SkillActionUtils {
    private static final String TAG = "OVSS.SkillActionUtils";
    private static Map<String, SkillActionListener> skillActionListenerMap = new HashMap();
    private static Map<String, Map<String, Method>> skillRegActionMap = new HashMap();
    private static Map<String, Map<String, Method>> skillCardObserverMap = new HashMap();

    private static void autowiredContext(Object obj, Context context) {
        if (obj == null || context == null) {
            return;
        }
        try {
            Log.d(TAG, "autowiredContext class: " + obj.getClass());
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class) && field.getType().equals(Context.class)) {
                    field.setAccessible(true);
                    field.set(obj, context);
                }
            }
        } catch (Exception e10) {
            Log.e(TAG, "Autowired error", e10);
        }
    }

    public static SkillActionListener getActionListener(String str) {
        return skillActionListenerMap.get(str);
    }

    public static Map<String, SkillActionListener> getActionListenerMap() {
        return skillActionListenerMap;
    }

    public static Map<String, Method> getCardObserverMap(String str) {
        return skillCardObserverMap.get(str);
    }

    public static Method getRegAction(String str, String str2) {
        if (skillRegActionMap.get(str) != null) {
            return skillRegActionMap.get(str).get(str2);
        }
        return null;
    }

    public static Map<String, Method> getRegActionMap(String str) {
        return skillRegActionMap.get(str);
    }

    private static boolean isNeedClass(String str, String... strArr) {
        for (String str2 : strArr) {
            if (str.startsWith(str2)) {
                return true;
            }
        }
        return false;
    }

    public static void load(Context context) {
        List<Class> scanAllClass;
        Iterator<Class> it;
        Class<?>[] parameterTypes;
        Context context2 = context;
        try {
            if (skillActionListenerMap.isEmpty() && (scanAllClass = scanAllClass(context2, SkillActions.class)) != null) {
                if (scanAllClass.isEmpty()) {
                    Log.d(TAG, "classes is empty");
                    return;
                }
                Iterator<Class> it2 = scanAllClass.iterator();
                while (it2.hasNext()) {
                    Class next = it2.next();
                    SkillActions skillActions = (SkillActions) next.getAnnotation(SkillActions.class);
                    SkillActionListenerWrapper skillActionListenerWrapper = new SkillActionListenerWrapper((SkillActionListener) next.newInstance());
                    autowiredContext(skillActionListenerWrapper.getListener(), context2);
                    HashMap hashMap = new HashMap();
                    HashMap hashMap2 = new HashMap();
                    Method[] declaredMethods = next.getDeclaredMethods();
                    int length = declaredMethods.length;
                    int i10 = 0;
                    while (i10 < length) {
                        Method method = declaredMethods[i10];
                        method.setAccessible(true);
                        Annotation[] annotations = method.getAnnotations();
                        int length2 = annotations.length;
                        int i11 = 0;
                        while (i11 < length2) {
                            Annotation annotation = annotations[i11];
                            if (annotation.annotationType().equals(RegAction.class)) {
                                Class<?>[] parameterTypes2 = method.getParameterTypes();
                                if (parameterTypes2 != null) {
                                    it = it2;
                                    if (parameterTypes2.length == 2 && parameterTypes2[0].equals(ISkillSession.class) && parameterTypes2[1].equals(String.class)) {
                                        hashMap.put(((RegAction) annotation).value(), method);
                                    }
                                } else {
                                    it = it2;
                                }
                            } else {
                                it = it2;
                                if (annotation.annotationType().equals(CardObserver.class) && (parameterTypes = method.getParameterTypes()) != null) {
                                    if (parameterTypes.length >= 1 && parameterTypes[0].equals(ISkillSession.class)) {
                                        hashMap2.put(((CardObserver) annotation).value(), method);
                                    }
                                    i11++;
                                    it2 = it;
                                }
                            }
                            i11++;
                            it2 = it;
                        }
                        i10++;
                        it2 = it2;
                    }
                    Iterator<Class> it3 = it2;
                    skillActionListenerWrapper.setRegActions(hashMap);
                    skillActionListenerWrapper.setCardObservers(hashMap2);
                    skillActionListenerMap.put(skillActions.path(), skillActionListenerWrapper);
                    skillRegActionMap.put(skillActions.path(), hashMap);
                    skillCardObserverMap.put(skillActions.path(), hashMap2);
                    context2 = context;
                    it2 = it3;
                }
            }
        } catch (Exception unused) {
            Log.e(TAG, "SkillActionUtils.load error");
        }
    }

    private static List<Class> scanAllClass(Context context, Class cls) {
        Log.d(TAG, "packageName: " + context.getPackageName());
        ArrayList arrayList = new ArrayList();
        try {
        } catch (Exception e10) {
            Log.e(TAG, e10.getMessage(), e10);
        }
        if (context.getPackageManager() == null) {
            return null;
        }
        DexFile dexFile = new DexFile(context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).sourceDir);
        PathClassLoader pathClassLoader = (PathClassLoader) context.getApplicationContext().getClassLoader();
        Enumeration<String> entries = dexFile.entries();
        while (entries.hasMoreElements()) {
            String nextElement = entries.nextElement();
            if (isNeedClass(nextElement, context.getPackageName())) {
                Log.d(TAG, "element: " + nextElement);
                Class loadClass = pathClassLoader.loadClass(nextElement);
                if (loadClass != null) {
                    for (Annotation annotation : loadClass.getAnnotations()) {
                        if (annotation.annotationType().equals(cls) && SkillActionListener.class.isAssignableFrom(loadClass)) {
                            arrayList.add(loadClass);
                        }
                    }
                }
            }
        }
        return arrayList;
    }
}
