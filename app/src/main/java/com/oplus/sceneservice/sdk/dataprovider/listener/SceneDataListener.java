package com.oplus.sceneservice.sdk.dataprovider.listener;

import androidx.annotation.Keep;
import com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData;
import java.util.ArrayList;

@Keep
/* loaded from: classes2.dex */
public interface SceneDataListener {
    void onSceneDataChanged(ArrayList<SceneData> arrayList);
}
