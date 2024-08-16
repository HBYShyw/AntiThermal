package com.oplus.telephony;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class FrameworkQosParameters {
    android.telephony.data.Qos mDefaultQos;
    List<android.telephony.data.QosBearerSession> mQosBearerSessions;

    public FrameworkQosParameters() {
        this.mDefaultQos = null;
        this.mQosBearerSessions = new ArrayList();
    }

    public FrameworkQosParameters(QosParametersResult qtiQosParameters) {
        this.mDefaultQos = QosParametersUtils.convertToFrameworkQos(qtiQosParameters.getDefaultQos());
        this.mQosBearerSessions = QosParametersUtils.convertToFrameworkQosBearerSessionsList(qtiQosParameters.getQosBearerSessions());
    }

    public FrameworkQosParameters(android.telephony.data.Qos defaultQos, List<android.telephony.data.QosBearerSession> bearerSessions) {
        this.mDefaultQos = defaultQos;
        this.mQosBearerSessions = bearerSessions;
    }

    public android.telephony.data.Qos getDefaultQos() {
        return this.mDefaultQos;
    }

    public List<android.telephony.data.QosBearerSession> getQosBearerSessions() {
        return this.mQosBearerSessions;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("FrameworkQosParameters: {").append(" defaultQos=").append(this.mDefaultQos).append(" qosBearerSessions=").append(this.mQosBearerSessions).append("}");
        return sb.toString();
    }
}
