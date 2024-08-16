package i9;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.sceneservice.sdk.dataprovider.bean.SceneStatusInfo;
import h9.SceneStatusColumns;
import l9.CursorUtils;

/* compiled from: SceneStatusManager.java */
/* renamed from: i9.d, reason: use source file name */
/* loaded from: classes2.dex */
public class SceneStatusManager extends BaseManager<SceneStatusInfo> {

    /* renamed from: b, reason: collision with root package name */
    private static volatile SceneStatusManager f12682b;

    private SceneStatusManager(Context context) {
        super(context);
    }

    public static SceneStatusManager g(Context context) {
        if (f12682b == null) {
            synchronized (SceneStatusManager.class) {
                if (f12682b == null) {
                    f12682b = new SceneStatusManager(context);
                }
            }
        }
        return f12682b;
    }

    @Override // i9.BaseManager
    public Uri b() {
        return SceneStatusColumns.f12018a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // i9.BaseManager
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public SceneStatusInfo a(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        SceneStatusInfo sceneStatusInfo = new SceneStatusInfo();
        sceneStatusInfo.mSceneId = CursorUtils.b(cursor, TriggerEvent.GAME_SCENE_ID);
        sceneStatusInfo.mSceneName = CursorUtils.d(cursor, "scene_name");
        sceneStatusInfo.mSceneStatus = CursorUtils.b(cursor, "scene_status");
        sceneStatusInfo.mSceneEndTime = CursorUtils.d(cursor, "scene_end_time");
        sceneStatusInfo.mSceneStartTime = CursorUtils.d(cursor, "scene_start_time");
        sceneStatusInfo.mBusinessId = CursorUtils.d(cursor, "business_id");
        sceneStatusInfo.mExtraData = CursorUtils.d(cursor, "extra_data");
        return sceneStatusInfo;
    }

    public SceneStatusInfo h(int i10) {
        return e("scene_id=" + i10, null, null);
    }

    public SceneStatusInfo i(String str) {
        return e("scene_name=\"" + str + "\"", null, null);
    }
}
