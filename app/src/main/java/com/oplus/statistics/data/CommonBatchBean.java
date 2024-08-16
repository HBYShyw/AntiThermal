package com.oplus.statistics.data;

import android.content.Context;
import com.oplus.statistics.DataOverSizeException;
import com.oplus.statistics.DataTypeConstants;
import com.oplus.statistics.util.CastUtil;
import com.oplus.statistics.util.LogUtil;
import com.oplus.statistics.util.Supplier;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;

/* loaded from: classes2.dex */
public class CommonBatchBean extends CommonBean {
    private static final String MAP_LIST = "mapList";
    private static final int SINGLE_DATA_MAX_LENGTH = 131072;
    private static final String TAG = "CommonBatchBean";

    public CommonBatchBean(Context context) {
        super(context);
    }

    @Override // com.oplus.statistics.data.CommonBean, com.oplus.statistics.data.TrackEvent
    public int getEventType() {
        return DataTypeConstants.COMMON_BATCH;
    }

    public void setLogMap(List<Map<String, String>> list) {
        JSONArray jSONArray = new JSONArray();
        Iterator<Map<String, String>> it = list.iterator();
        while (it.hasNext()) {
            jSONArray.put(CastUtil.map2JsonObject(it.next()));
        }
        String jSONArray2 = jSONArray.toString();
        if (jSONArray2.length() < SINGLE_DATA_MAX_LENGTH) {
            this.mLogMap = jSONArray2;
            addTrackInfo(MAP_LIST, jSONArray2);
            return;
        }
        final String str = "DataOverSizeException :" + getAppId() + ", " + getLogTag() + ", " + getEventID();
        str.getClass();
        LogUtil.w(TAG, new Supplier() { // from class: com.oplus.statistics.data.a
            @Override // com.oplus.statistics.util.Supplier
            public final Object get() {
                return str.toString();
            }
        });
        throw new DataOverSizeException(str);
    }

    public CommonBatchBean(Context context, String str, String str2, String str3) {
        super(context, str, str2, str3);
    }
}
