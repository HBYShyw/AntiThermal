package oplus.telecom;

import android.net.Uri;
import android.os.Bundle;
import android.telecom.Conference;
import android.telecom.Conferenceable;
import android.telecom.Connection;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccountHandle;
import android.telecom.StatusHints;
import java.util.List;

/* loaded from: classes.dex */
public class ConnectionExt {
    public static final int CAPABILITY_AUDIO_RINGTONE = Integer.MIN_VALUE;
    public static final int CAPABILITY_BASE = 536870912;
    public static final int CAPABILITY_CAPABILITY_CALL_RECORDING = 536870912;
    public static final int CAPABILITY_VIDEO_RINGTONE = 1073741824;
    public static final String EVENT_CALL_ALERTING_NOTIFICATION = "mediatek.telecom.event.EVENT_CALL_ALERTING_NOTIFICATION";
    public static final String EVENT_CONNECTION_LOST = "mediatek.telecom.event.CONNECTION_LOST";
    public static final String EVENT_ECC_RETRY_FAIL = "mediatek.telecom.event.EVENT_ECC_RETRY_FAIL";
    public static final String EVENT_INCOMING_INFO_UPDATED = "mediatek.telecom.event.INCOMING_INFO_UPDATED";
    public static final String EVENT_NUMBER_UPDATED = "mediatek.telecom.event.NUMBER_UPDATED";
    public static final String EVENT_OPERATION_FAILED = "mediatek.telecom.event.OPERATION_FAILED";
    public static final String EVENT_PHONE_ACCOUNT_CHANGED = "mediatek.telecom.event.PHONE_ACCOUNT_CHANGED";
    public static final String EVENT_SS_NOTIFICATION = "mediatek.telecom.event.SS_NOTIFICATION";
    public static final String EVENT_VOLTE_MARKED_AS_EMERGENCY = "mediatek.telecom.event.EVENT_VOLTE_MARKED_AS_EMERGENCY";
    public static final String EXTRA_DISCONNECT_CAUSE = "mediatek.telecom.extra.EXTRA_DISCONNECT_CAUSE";
    public static final String EXTRA_FAILED_OPERATION = "mediatek.telecom.extra.FAILED_OPERATION";
    public static final String EXTRA_NEW_NUMBER = "mediatek.telecom.extra.NEW_NUMBER";
    public static final String EXTRA_PHONE_ACCOUNT_HANDLE = "mediatek.telecom.extra.PHONE_ACCOUNT_HANDLE";
    public static final String EXTRA_SS_NOTIFICATION_CODE = "mediatek.telecom.extra.SS_NOTIFICATION_CODE";
    public static final String EXTRA_SS_NOTIFICATION_INDEX = "mediatek.telecom.extra.SS_NOTIFICATION_INDEX";
    public static final String EXTRA_SS_NOTIFICATION_NOTITYPE = "mediatek.telecom.extra.SS_NOTIFICATION_NOTITYPE";
    public static final String EXTRA_SS_NOTIFICATION_NUMBER = "mediatek.telecom.extra.SS_NOTIFICATION_NUMBER";
    public static final String EXTRA_SS_NOTIFICATION_TYPE = "mediatek.telecom.extra.SS_NOTIFICATION_TYPE";
    public static final String EXTRA_UPDATED_INCOMING_INFO_ALPHAID = "mediatek.telecom.extra.UPDATED_INCOMING_INFO_ALPHAID";
    public static final String EXTRA_UPDATED_INCOMING_INFO_CLI_VALIDITY = "mediatek.telecom.extra.UPDATED_INCOMING_INFO_CLI_VALIDITY";
    public static final String EXTRA_UPDATED_INCOMING_INFO_TYPE = "mediatek.telecom.extra.UPDATED_INCOMING_INFO_TYPE";
    public static final String IMS_EVENT_NOTIFICATION_RINGTONE = "mediatek.telecom.event.IMS_EVENT_NOTIFICATION_RINGTONE";
    public static final String IMS_EVENT_VIDEO_RINGTONE = "mediatek.telecom.event.IMS_EVENT_VIDEO_RINGTONE";
    public static final int PROPERTY_BASE = 32768;
    public static final int PROPERTY_CDMA = 65536;
    public static final int PROPERTY_CONFERENCE_PARTICIPANT = 262144;
    public static final int PROPERTY_VOICE_RECORDING = 131072;
    public static final int PROPERTY_VOLTE = 32768;

    public static String propertiesToString(int properties) {
        StringBuilder sb = new StringBuilder(Connection.propertiesToString(properties));
        sb.setLength(sb.length() - 1);
        sb.append(propertiesToStringInternal(properties, true));
        sb.append("]");
        return sb.toString();
    }

    public static String capabilitiesToString(int capabilities) {
        StringBuilder sb = new StringBuilder(Connection.capabilitiesToString(capabilities));
        sb.setLength(sb.length() - 1);
        sb.append(capabilitiesToStringInternal(capabilities, true));
        sb.append("]");
        return sb.toString();
    }

    public static String capabilitiesToStringShort(int capabilities) {
        StringBuilder sb = new StringBuilder(Connection.capabilitiesToStringShort(capabilities));
        sb.setLength(sb.length() - 1);
        sb.append(capabilitiesToStringInternal(capabilities, false));
        sb.append("]");
        return sb.toString();
    }

    private static String propertiesToStringInternal(int properties, boolean isLong) {
        StringBuilder sb = new StringBuilder();
        if (can(properties, 32768)) {
            sb.append(isLong ? " M_PROPERTY_VOLTE" : " m_volte");
        }
        if (can(properties, 65536)) {
            sb.append(isLong ? " M_PROPERTY_CDMA" : " m_cdma");
        }
        if (can(properties, 131072)) {
            sb.append(isLong ? " M_PROPERTY_VOICE_RECORDING" : " m_rcrding");
        }
        if (can(properties, 262144)) {
            sb.append(isLong ? " M_PROPERTY_CONFERENCE_PARTICIPANT" : " m_conf_child");
        }
        return sb.toString();
    }

    private static String capabilitiesToStringInternal(int capabilities, boolean isLong) {
        StringBuilder sb = new StringBuilder();
        if (can(capabilities, 536870912)) {
            sb.append(isLong ? " M_CAPABILITY_CAPABILITY_CALL_RECORDING" : " m_rcrd");
        }
        if (can(capabilities, 1073741824)) {
            sb.append(isLong ? " M_CAPABILITY_VIDEO_RINGTONE" : " m_vt_tone");
        }
        if (can(capabilities, Integer.MIN_VALUE)) {
            sb.append(isLong ? " M_CAPABILITY_AUDIO_RINGTONE" : " m_ar_tone");
        }
        return sb.toString();
    }

    public static boolean can(int capabilities, int capability) {
        return (capabilities & capability) == capability;
    }

    public static Bundle buildParamsForOperationFailed(int operation) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_FAILED_OPERATION, operation);
        return bundle;
    }

    public static Bundle buildParamsForNumberUpdated(String number) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_NEW_NUMBER, number);
        return bundle;
    }

    public static Bundle buildParamsForIncomingInfoUpdated(int type, String alphaid, int cliValidity) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_UPDATED_INCOMING_INFO_TYPE, type);
        bundle.putString(EXTRA_UPDATED_INCOMING_INFO_ALPHAID, alphaid);
        bundle.putInt(EXTRA_UPDATED_INCOMING_INFO_CLI_VALIDITY, cliValidity);
        return bundle;
    }

    public static Bundle buildParamsForSsNotification(int notiType, int type, int code, String number, int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_SS_NOTIFICATION_NOTITYPE, notiType);
        bundle.putInt(EXTRA_SS_NOTIFICATION_TYPE, type);
        bundle.putInt(EXTRA_SS_NOTIFICATION_CODE, code);
        bundle.putString(EXTRA_SS_NOTIFICATION_NUMBER, number);
        bundle.putInt(EXTRA_SS_NOTIFICATION_INDEX, index);
        return bundle;
    }

    public static String toLogSafePhoneNumber(Connection connection, String number) {
        return Connection.toLogSafePhoneNumber(number);
    }

    /* loaded from: classes.dex */
    public static class Listener {
        private Connection.Listener mListener = new Connection.Listener() { // from class: oplus.telecom.ConnectionExt.Listener.1
            public void onStateChanged(Connection c, int state) {
                Listener.this.onStateChanged(c, state);
            }

            public void onAddressChanged(Connection c, Uri newAddress, int presentation) {
                Listener.this.onAddressChanged(c, newAddress, presentation);
            }

            public void onCallerDisplayNameChanged(Connection c, String callerDisplayName, int presentation) {
                Listener.this.onCallerDisplayNameChanged(c, callerDisplayName, presentation);
            }

            public void onVideoStateChanged(Connection c, int videoState) {
                Listener.this.onVideoStateChanged(c, videoState);
            }

            public void onDisconnected(Connection c, DisconnectCause disconnectCause) {
                Listener.this.onDisconnected(c, disconnectCause);
            }

            public void onPostDialWait(Connection c, String remaining) {
                Listener.this.onPostDialWait(c, remaining);
            }

            public void onPostDialChar(Connection c, char nextChar) {
                Listener.this.onPostDialChar(c, nextChar);
            }

            public void onRingbackRequested(Connection c, boolean ringback) {
                Listener.this.onRingbackRequested(c, ringback);
            }

            public void onDestroyed(Connection c) {
                Listener.this.onDestroyed(c);
            }

            public void onConnectionCapabilitiesChanged(Connection c, int capabilities) {
                Listener.this.onConnectionCapabilitiesChanged(c, capabilities);
            }

            public void onConnectionPropertiesChanged(Connection c, int properties) {
                Listener.this.onConnectionPropertiesChanged(c, properties);
            }

            public void onSupportedAudioRoutesChanged(Connection c, int supportedAudioRoutes) {
                Listener.this.onSupportedAudioRoutesChanged(c, supportedAudioRoutes);
            }

            public void onVideoProviderChanged(Connection c, Connection.VideoProvider videoProvider) {
                Listener.this.onVideoProviderChanged(c, videoProvider);
            }

            public void onAudioModeIsVoipChanged(Connection c, boolean isVoip) {
                Listener.this.onAudioModeIsVoipChanged(c, isVoip);
            }

            public void onStatusHintsChanged(Connection c, StatusHints statusHints) {
                Listener.this.onStatusHintsChanged(c, statusHints);
            }

            public void onConferenceablesChanged(Connection c, List<Conferenceable> conferenceables) {
                Listener.this.onConferenceablesChanged(c, conferenceables);
            }

            public void onConferenceChanged(Connection c, Conference conference) {
                Listener.this.onConferenceChanged(c, conference);
            }

            public void onConferenceMergeFailed(Connection c) {
                Listener.this.onConferenceMergeFailed(c);
            }

            public void onExtrasChanged(Connection c, Bundle extras) {
                Listener.this.onExtrasChanged(c, extras);
            }

            public void onExtrasRemoved(Connection c, List<String> keys) {
                Listener.this.onExtrasRemoved(c, keys);
            }

            public void onConnectionEvent(Connection c, String event, Bundle extras) {
                Listener.this.onConnectionEvent(c, event, extras);
            }

            public void onAudioRouteChanged(Connection c, int audioRoute, String bluetoothAddress) {
                Listener.this.onAudioRouteChanged(c, audioRoute, bluetoothAddress);
            }

            public void onRttInitiationSuccess(Connection c) {
                Listener.this.onRttInitiationSuccess(c);
            }

            public void onRttInitiationFailure(Connection c, int reason) {
                Listener.this.onRttInitiationFailure(c, reason);
            }

            public void onRttSessionRemotelyTerminated(Connection c) {
                Listener.this.onRttSessionRemotelyTerminated(c);
            }

            public void onRemoteRttRequest(Connection c) {
                Listener.this.onRemoteRttRequest(c);
            }

            public void onPhoneAccountChanged(Connection c, PhoneAccountHandle pHandle) {
                Listener.this.onPhoneAccountChanged(c, pHandle);
            }

            public void onConnectionTimeReset(Connection c) {
                Listener.this.onConnectionTimeReset(c);
            }
        };

        public void onStateChanged(Connection c, int state) {
        }

        public void onAddressChanged(Connection c, Uri newAddress, int presentation) {
        }

        public void onCallerDisplayNameChanged(Connection c, String callerDisplayName, int presentation) {
        }

        public void onVideoStateChanged(Connection c, int videoState) {
        }

        public void onDisconnected(Connection c, DisconnectCause disconnectCause) {
        }

        public void onPostDialWait(Connection c, String remaining) {
        }

        public void onPostDialChar(Connection c, char nextChar) {
        }

        public void onRingbackRequested(Connection c, boolean ringback) {
        }

        public void onDestroyed(Connection c) {
        }

        public void onConnectionCapabilitiesChanged(Connection c, int capabilities) {
        }

        public void onConnectionPropertiesChanged(Connection c, int properties) {
        }

        public void onSupportedAudioRoutesChanged(Connection c, int supportedAudioRoutes) {
        }

        public void onVideoProviderChanged(Connection c, Connection.VideoProvider videoProvider) {
        }

        public void onAudioModeIsVoipChanged(Connection c, boolean isVoip) {
        }

        public void onStatusHintsChanged(Connection c, StatusHints statusHints) {
        }

        public void onConferenceablesChanged(Connection c, List<Conferenceable> conferenceables) {
        }

        public void onConferenceChanged(Connection c, Conference conference) {
        }

        public void onConferenceMergeFailed(Connection c) {
        }

        public void onExtrasChanged(Connection c, Bundle extras) {
        }

        public void onExtrasRemoved(Connection c, List<String> keys) {
        }

        public void onConnectionEvent(Connection c, String event, Bundle extras) {
        }

        public void onAudioRouteChanged(Connection c, int audioRoute, String bluetoothAddress) {
        }

        public void onRttInitiationSuccess(Connection c) {
        }

        public void onRttInitiationFailure(Connection c, int reason) {
        }

        public void onRttSessionRemotelyTerminated(Connection c) {
        }

        public void onRemoteRttRequest(Connection c) {
        }

        public void onPhoneAccountChanged(Connection c, PhoneAccountHandle pHandle) {
        }

        public void onConnectionTimeReset(Connection c) {
        }

        public Connection addConnectionListener(Connection connection) {
            return connection.addConnectionListener(this.mListener);
        }

        public Connection removeConnectionListener(Connection connection) {
            return connection.removeConnectionListener(this.mListener);
        }
    }
}
