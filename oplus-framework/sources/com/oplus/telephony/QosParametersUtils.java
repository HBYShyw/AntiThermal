package com.oplus.telephony;

import android.telephony.data.Qos;
import android.telephony.data.QosBearerFilter;
import android.util.Log;
import com.oplus.telephony.QosBearerFilter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class QosParametersUtils {
    public static final int QOS_TYPE_EPS = 1;
    public static final int QOS_TYPE_NR = 2;
    public static final String TAG = "QosParametersUtils";

    public static android.telephony.data.Qos convertToFrameworkQos(Qos sourceQos) {
        if (sourceQos == null) {
            Log.e(TAG, "convertToFrameworkQos, sourceQos is null");
            return null;
        }
        android.telephony.data.Qos frameworkQos = null;
        Qos.QosBandwidth downlink = new Qos.QosBandwidth(sourceQos.getDownlinkBandwidth().getMaxBitrateKbps(), sourceQos.getDownlinkBandwidth().getGuaranteedBitrateKbps());
        Qos.QosBandwidth uplink = new Qos.QosBandwidth(sourceQos.getUplinkBandwidth().getMaxBitrateKbps(), sourceQos.getUplinkBandwidth().getGuaranteedBitrateKbps());
        switch (sourceQos.getType()) {
            case 1:
                EpsQos sourceEpsQos = (EpsQos) sourceQos;
                frameworkQos = new android.telephony.data.EpsQos(downlink, uplink, sourceEpsQos.getQci());
                break;
            case 2:
                NrQos sourceNrQos = (NrQos) sourceQos;
                frameworkQos = new android.telephony.data.NrQos(downlink, uplink, sourceNrQos.getQfi(), sourceNrQos.get5Qi(), sourceNrQos.getAveragingWindow());
                break;
            default:
                Log.d(TAG, "convertToFrameworkQos, unknown QoS type: " + sourceQos.getType());
                break;
        }
        Log.d(TAG, "convertToFrameworkQos: source: " + sourceQos + ", target: " + frameworkQos);
        return frameworkQos;
    }

    public static android.telephony.data.QosBearerFilter convertToFrameworkQosBearerFilter(QosBearerFilter sourceQosFilter) {
        QosBearerFilter.PortRange targetLocalPort;
        QosBearerFilter.PortRange targetRemotePort;
        if (sourceQosFilter == null) {
            Log.e(TAG, "convertToFrameworkQosBearerFilter, sourceQosFilter is null");
            return null;
        }
        QosBearerFilter.PortRange sourceLocalPort = sourceQosFilter.getLocalPortRange();
        QosBearerFilter.PortRange sourceRemotePort = sourceQosFilter.getRemotePortRange();
        if (sourceLocalPort != null) {
            targetLocalPort = new QosBearerFilter.PortRange(sourceLocalPort.getStart(), sourceLocalPort.getEnd());
        } else {
            targetLocalPort = null;
        }
        if (sourceRemotePort != null) {
            targetRemotePort = new QosBearerFilter.PortRange(sourceRemotePort.getStart(), sourceRemotePort.getEnd());
        } else {
            targetRemotePort = null;
        }
        return new android.telephony.data.QosBearerFilter(sourceQosFilter.getLocalAddresses(), sourceQosFilter.getRemoteAddresses(), targetLocalPort, targetRemotePort, sourceQosFilter.getProtocol(), sourceQosFilter.getTypeOfServiceMask(), sourceQosFilter.getFlowLabel(), sourceQosFilter.getSpi(), sourceQosFilter.getDirection(), sourceQosFilter.getPrecedence());
    }

    public static List<android.telephony.data.QosBearerFilter> convertToFrameworkQosFilterList(List<QosBearerFilter> sourceBearerFilters) {
        List<android.telephony.data.QosBearerFilter> targetBearerFilters = new ArrayList<>();
        if (sourceBearerFilters == null) {
            return targetBearerFilters;
        }
        for (QosBearerFilter sourceFilter : sourceBearerFilters) {
            android.telephony.data.QosBearerFilter targetFilter = convertToFrameworkQosBearerFilter(sourceFilter);
            if (targetFilter != null) {
                targetBearerFilters.add(targetFilter);
            }
        }
        return targetBearerFilters;
    }

    public static android.telephony.data.QosBearerSession convertToFrameworkQosBearerSession(QosBearerSession sourceQosSession) {
        if (sourceQosSession == null) {
            return null;
        }
        return new android.telephony.data.QosBearerSession(sourceQosSession.getQosBearerSessionId(), convertToFrameworkQos(sourceQosSession.getQos()), convertToFrameworkQosFilterList(sourceQosSession.getQosBearerFilterList()));
    }

    public static List<android.telephony.data.QosBearerSession> convertToFrameworkQosBearerSessionsList(List<QosBearerSession> sourceBearerSessions) {
        List<android.telephony.data.QosBearerSession> targetBearerSessions = new ArrayList<>();
        if (sourceBearerSessions == null) {
            return targetBearerSessions;
        }
        for (QosBearerSession sourceSession : sourceBearerSessions) {
            android.telephony.data.QosBearerSession targetSession = convertToFrameworkQosBearerSession(sourceSession);
            if (targetSession != null) {
                targetBearerSessions.add(targetSession);
            }
        }
        return targetBearerSessions;
    }
}
