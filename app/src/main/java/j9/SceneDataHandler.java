package j9;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import java.util.ArrayList;
import k9.ClientManager;
import l9.LogUtils;
import l9.NumberUtils;

/* compiled from: SceneDataHandler.java */
/* renamed from: j9.b, reason: use source file name */
/* loaded from: classes2.dex */
public class SceneDataHandler {
    public static void a(Intent intent) {
        if (intent == null) {
            LogUtils.c("SceneTriggerDataHandler", "parseSceneIntent intent is null, return");
            return;
        }
        String stringExtra = intent.getStringExtra("sceneId");
        if (TextUtils.isEmpty(stringExtra)) {
            LogUtils.c("SceneTriggerDataHandler", "onReceive: sceneId is empty");
            return;
        }
        LogUtils.a("SceneTriggerDataHandler", "parseSceneIntent:  sceneId:" + stringExtra);
        String[] split = stringExtra.split(",");
        if (split.length <= 0) {
            return;
        }
        Bundle extras = intent.getExtras();
        if (extras == null) {
            LogUtils.c("SceneTriggerDataHandler", "parseSceneIntent: sceneData is null!");
            return;
        }
        for (String str : split) {
            int b10 = NumberUtils.b(str, 0);
            if (b10 <= 0) {
                LogUtils.a("SceneTriggerDataHandler", "parseSceneIntent:sceneId invalid, continue");
            } else {
                ArrayList parcelableArrayList = extras.getParcelableArrayList(str + "_list");
                LogUtils.a("SceneTriggerDataHandler", "submitSceneTask begin");
                ClientManager.f14219a.a(b10, parcelableArrayList);
                LogUtils.a("SceneTriggerDataHandler", "submitSceneTask end");
            }
        }
    }
}
