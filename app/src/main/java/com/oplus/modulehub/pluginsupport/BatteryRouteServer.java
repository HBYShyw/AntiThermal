package com.oplus.modulehub.pluginsupport;

import android.content.Context;
import android.os.Bundle;
import b6.LocalLog;
import com.oplus.epona.Call;
import com.oplus.epona.DynamicProvider;
import com.oplus.epona.Epona;
import com.oplus.epona.Request;
import com.oplus.epona.Response;
import com.oplus.thermalcontrol.ThermalControlUtils;
import f6.CommonUtil;
import x6.BMDiaUtil;

/* loaded from: classes.dex */
public class BatteryRouteServer implements DynamicProvider {
    private static final String STR_BM_PLUGIN_CALL_CMD = "bmmonitor_plugincall_command";
    private static final String STR_BM_PLUGIN_CALL_METHOD_1 = "getHighPerDiaFlag";
    private static final String STR_BM_PLUGIN_CALL_METHOD_2 = "CommonUtil.sDebug";
    private static final String STR_BM_PLUGIN_CALL_METHOD_3 = "getPreHighFlag";
    private static final String STR_BM_PLUGIN_CALL_METHOD_4 = "closeHighPerformanceMode";
    private static final String STR_BM_PLUGIN_CALL_METHOD_5 = "cancelHighPerDialog";
    private static final String STR_BM_PLUGIN_CALL_METHOD_6 = "getHighPerformanceModeOn";
    private static final String STR_BM_PLUGIN_CALL_METHOD_7 = "setHighPerDialog";
    private static final String STR_BM_PLUGIN_CALL_METHOD_8 = "setBenchModeOn";
    private static final String STR_BM_PLUGIN_CALL_METHOD_9 = "setBenchModeOff";
    private static final String STR_BM_PLUGIN_CALL_RESULT = "bm_result";
    private static final String TAG = "BatteryRouteServer";
    private static volatile BatteryRouteServer mRouteServer;
    private Context mContext;

    private BatteryRouteServer(Context context) {
        this.mContext = context;
        registerProvider();
    }

    public static BatteryRouteServer getInstance(Context context) {
        if (mRouteServer == null) {
            synchronized (BatteryRouteServer.class) {
                if (mRouteServer == null) {
                    mRouteServer = new BatteryRouteServer(context);
                }
            }
        }
        return mRouteServer;
    }

    private void registerProvider() {
        Epona.register(this);
    }

    @Override // com.oplus.epona.DynamicProvider
    public String getName() {
        return TAG;
    }

    @Override // com.oplus.epona.DynamicProvider
    public Response onCall(Request request) {
        String string = request.getBundle().getString(STR_BM_PLUGIN_CALL_CMD);
        if (LocalLog.f()) {
            LocalLog.a(TAG, "commandBM=" + string);
        }
        Bundle bundle = new Bundle();
        if (string != null) {
            char c10 = 65535;
            switch (string.hashCode()) {
                case -1183433872:
                    if (string.equals(STR_BM_PLUGIN_CALL_METHOD_8)) {
                        c10 = 0;
                        break;
                    }
                    break;
                case -588895231:
                    if (string.equals(STR_BM_PLUGIN_CALL_METHOD_7)) {
                        c10 = 1;
                        break;
                    }
                    break;
                case -136865181:
                    if (string.equals(STR_BM_PLUGIN_CALL_METHOD_1)) {
                        c10 = 2;
                        break;
                    }
                    break;
                case 316173306:
                    if (string.equals(STR_BM_PLUGIN_CALL_METHOD_6)) {
                        c10 = 3;
                        break;
                    }
                    break;
                case 528806401:
                    if (string.equals(STR_BM_PLUGIN_CALL_METHOD_2)) {
                        c10 = 4;
                        break;
                    }
                    break;
                case 1644290281:
                    if (string.equals(STR_BM_PLUGIN_CALL_METHOD_5)) {
                        c10 = 5;
                        break;
                    }
                    break;
                case 1736785625:
                    if (string.equals(STR_BM_PLUGIN_CALL_METHOD_4)) {
                        c10 = 6;
                        break;
                    }
                    break;
                case 1968255486:
                    if (string.equals(STR_BM_PLUGIN_CALL_METHOD_9)) {
                        c10 = 7;
                        break;
                    }
                    break;
            }
            switch (c10) {
                case 0:
                    ThermalControlUtils.getInstance(this.mContext).setBenchMode(true);
                    break;
                case 1:
                    BMDiaUtil.f(this.mContext).d();
                    break;
                case 2:
                    bundle.putBoolean(STR_BM_PLUGIN_CALL_RESULT, BMDiaUtil.f(this.mContext).e());
                    break;
                case 3:
                    bundle.putInt(STR_BM_PLUGIN_CALL_RESULT, CommonUtil.A());
                    break;
                case 4:
                    bundle.putBoolean(STR_BM_PLUGIN_CALL_RESULT, CommonUtil.f11386a);
                    break;
                case 5:
                    BMDiaUtil.f(this.mContext).c();
                    break;
                case 6:
                    CommonUtil.b(this.mContext);
                    break;
                case 7:
                    ThermalControlUtils.getInstance(this.mContext).setBenchMode(false);
                    break;
            }
        }
        return Response.newResponse(bundle);
    }

    @Override // com.oplus.epona.DynamicProvider
    public void onCall(Request request, Call.Callback callback) {
    }
}
