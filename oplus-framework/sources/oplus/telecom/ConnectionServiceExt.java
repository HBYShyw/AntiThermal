package oplus.telecom;

import android.os.Bundle;
import android.telecom.Conference;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.ParcelableConnection;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/* loaded from: classes.dex */
public abstract class ConnectionServiceExt extends ConnectionService {
    private static final String LOG_TAG = "ConnectionServiceExt";

    public void answerVideo(String callId, int videoState) {
        super.answerVideo(callId, videoState);
    }

    public void hold(String callId) {
        super.hold(callId);
    }

    public void unhold(String callId) {
        super.unhold(callId);
    }

    public void answer(String callId) {
        super.answer(callId);
    }

    public Connection findConnectionForAction(String callId, String action) {
        return super.findConnectionForAction(callId, action);
    }

    public Conference getNullConference() {
        return super.getNullConference();
    }

    public Conference findConferenceForAction(String conferenceId, String action) {
        return super.findConferenceForAction(conferenceId, action);
    }

    public void sendCallEvent(String callId, String event, Bundle extras) {
        super.sendCallEvent(callId, event, extras);
    }

    public synchronized Connection getNullConnection() {
        return super.getNullConnection();
    }

    public Conference addConferenceListener(Conference conference) {
        return conference.addListener(this.mConferenceListener);
    }

    public Conference removeConferenceListener(Conference conference) {
        return conference.removeListener(this.mConferenceListener);
    }

    public Connection addConnectionListener(Connection conncetion) {
        return conncetion.addConnectionListener(this.mConnectionListener);
    }

    public Connection removeConnectionListener(Connection conncetion) {
        return conncetion.removeConnectionListener(this.mConnectionListener);
    }

    public void handleCreateConnectionCompleteExt(String id, ConnectionRequest request, ParcelableConnection connection) {
        this.mAdapter.handleCreateConnectionComplete(id, request, connection);
    }

    public void setConferenceableConnectionsExt(String callId, List<String> conferenceableCallIds) {
        this.mAdapter.setConferenceableConnections(callId, conferenceableCallIds);
    }

    public boolean isAdaptersAvailable() {
        try {
            Field fieldAdapters = this.mAdapter.getClass().getDeclaredField("mAdapters");
            fieldAdapters.setAccessible(true);
            Object adapters = fieldAdapters.get(this.mAdapter);
            if (adapters != null) {
                Method method = adapters.getClass().getMethod("size", new Class[0]);
                Object size = method.invoke(adapters, new Object[0]);
                if (size != null && (size instanceof Integer) && ((Integer) size).intValue() == 0) {
                    Log.w(LOG_TAG, "isAdaptersAvailable, " + adapters + ", " + size);
                    return false;
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "isAdaptersAvailable Error" + e);
        }
        return true;
    }
}
