package i9;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneBankData;
import com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneData;
import com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneExpressageData;
import com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneFlightData;
import com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneHotelData;
import com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneMovieData;
import com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneTrainData;
import h9.SceneColumns;
import java.util.List;
import l9.LogUtils;
import l9.SceneDataUtils;

/* compiled from: SceneDataManager.java */
/* renamed from: i9.c, reason: use source file name */
/* loaded from: classes2.dex */
public class SceneDataManager extends BaseManager<SceneData> {

    /* renamed from: b, reason: collision with root package name */
    private static final int[] f12680b = {1, 2, 4, 8, 64};

    /* renamed from: c, reason: collision with root package name */
    private static volatile SceneDataManager f12681c;

    private SceneDataManager(Context context) {
        super(context);
    }

    private SceneData f(int i10) {
        if (i10 == 1) {
            return new SceneFlightData();
        }
        if (i10 == 2) {
            return new SceneTrainData();
        }
        if (i10 == 4) {
            return new SceneHotelData();
        }
        if (i10 == 8) {
            return new SceneMovieData();
        }
        if (i10 == 16) {
            return new SceneExpressageData();
        }
        if (i10 != 64) {
            return null;
        }
        return new SceneBankData();
    }

    public static SceneDataManager h(Context context) {
        if (f12681c == null) {
            synchronized (SceneDataManager.class) {
                if (f12681c == null) {
                    f12681c = new SceneDataManager(context);
                }
            }
        }
        return f12681c;
    }

    private String i(int[] iArr) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("type in (");
        for (int i10 : iArr) {
            sb2.append(i10);
            sb2.append(",");
        }
        sb2.deleteCharAt(sb2.length() - 1);
        sb2.append(")");
        return sb2.toString();
    }

    @Override // i9.BaseManager
    public Uri b() {
        return SceneColumns.f12017a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // i9.BaseManager
    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public SceneData a(Cursor cursor) {
        SceneData f10 = f(g9.a.a(cursor, "type"));
        if (f10 != null) {
            try {
                f10.setId(String.valueOf(g9.a.a(cursor, "_id")));
                f10.setType(g9.a.a(cursor, "type"));
                f10.setMatchKey(g9.a.c(cursor, "match_key"));
                f10.setOccurTime(g9.a.b(cursor, "occur_time").longValue());
                f10.setExpireTime(g9.a.b(cursor, "expire_time").longValue());
                f10.setLastOnlineTime(g9.a.b(cursor, "last_online_time").longValue());
                f10.setDeleted(g9.a.a(cursor, "deleted") == 1);
                f10.setProcessed(g9.a.a(cursor, "processed") == 1);
                f10.setProcessStep(g9.a.a(cursor, "process_step"));
                f10.setSource(g9.a.a(cursor, "source"));
                f10.setContent(SceneDataUtils.a(g9.a.c(cursor, "content")));
                String c10 = g9.a.c(cursor, "data1");
                f10.setLastUpdateSource(TextUtils.isEmpty(c10) ? -1 : Integer.parseInt(c10));
                f10.setData2(SceneDataUtils.a(g9.a.c(cursor, "data2")));
                f10.setData3(g9.a.c(cursor, "data3"));
                f10.setTargetTel(g9.a.c(cursor, "target_tel"));
                f10.setOccurTimezone(g9.a.c(cursor, "occur_timezone"));
                f10.setExpireTimezone(g9.a.c(cursor, "expire_timezone"));
                f10.setDataChangedState(g9.a.a(cursor, "data_changed_state"));
            } catch (Throwable th) {
                LogUtils.b("SceneDataManager", "cursorToObject e = " + th);
            }
        }
        return f10;
    }

    public SceneData j(int i10, String str) {
        if (TextUtils.isEmpty(str)) {
            LogUtils.a("SceneDataManager", "querySceneData matchKey is empty");
            return null;
        }
        return e("match_key = ? and type = ?", new String[]{str, i10 + ""}, null);
    }

    public List<SceneData> k(int[] iArr) {
        if (iArr != null && iArr.length > 0) {
            return d(null, i(iArr), null, "occur_time ASC ");
        }
        LogUtils.a("SceneDataManager", "querySceneDataWithType type is null");
        return null;
    }
}
