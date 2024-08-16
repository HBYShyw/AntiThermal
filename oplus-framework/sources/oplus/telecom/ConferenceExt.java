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
public class ConferenceExt extends Conference {
    public ConferenceExt(PhoneAccountHandle phoneAccount) {
        super(phoneAccount);
    }

    public void fireonConferenceableConnectionsChanged() {
        super.fireOnConferenceableConnectionsChanged();
    }

    public Connection addConnectionDeathListener(Connection connection) {
        return connection.addConnectionListener(this.mConnectionDeathListener);
    }

    public Connection removeConnectionDeathListener(Connection connection) {
        return connection.removeConnectionListener(this.mConnectionDeathListener);
    }

    /* loaded from: classes.dex */
    public static class Listener {
        private Conference.Listener mListener = new Conference.Listener() { // from class: oplus.telecom.ConferenceExt.Listener.1
            public void onStateChanged(Conference conference, int oldState, int newState) {
                Listener.this.onStateChanged(conference, oldState, newState);
            }

            public void onDisconnected(Conference conference, DisconnectCause disconnectCause) {
                Listener.this.onDisconnected(conference, disconnectCause);
            }

            public void onConnectionAdded(Conference conference, Connection connection) {
                Listener.this.onConnectionAdded(conference, connection);
            }

            public void onConnectionRemoved(Conference conference, Connection connection) {
                Listener.this.onConnectionRemoved(conference, connection);
            }

            public void onConferenceableConnectionsChanged(Conference conference, List<Connection> conferenceableConnections) {
                Listener.this.onConferenceableConnectionsChanged(conference, conferenceableConnections);
            }

            public void onConferenceablesChanged(Conference conference, List<Conferenceable> conferenceables) {
                Listener.this.onConferenceablesChanged(conference, conferenceables);
            }

            public void onDestroyed(Conference conference) {
                Listener.this.onDestroyed(conference);
            }

            public void onConnectionCapabilitiesChanged(Conference conference, int connectionCapabilities) {
                Listener.this.onConnectionCapabilitiesChanged(conference, connectionCapabilities);
            }

            public void onConnectionPropertiesChanged(Conference conference, int connectionProperties) {
                Listener.this.onConnectionPropertiesChanged(conference, connectionProperties);
            }

            public void onVideoStateChanged(Conference c, int videoState) {
                Listener.this.onVideoStateChanged(c, videoState);
            }

            public void onVideoProviderChanged(Conference c, Connection.VideoProvider videoProvider) {
                Listener.this.onVideoProviderChanged(c, videoProvider);
            }

            public void onStatusHintsChanged(Conference conference, StatusHints statusHints) {
                Listener.this.onStatusHintsChanged(conference, statusHints);
            }

            public void onExtrasChanged(Conference c, Bundle extras) {
                Listener.this.onExtrasChanged(c, extras);
            }

            public void onExtrasRemoved(Conference c, List<String> keys) {
                Listener.this.onExtrasRemoved(c, keys);
            }

            public void onConferenceStateChanged(Conference c, boolean isConference) {
                Listener.this.onConferenceStateChanged(c, isConference);
            }

            public void onAddressChanged(Conference c, Uri newAddress, int presentation) {
                Listener.this.onAddressChanged(c, newAddress, presentation);
            }

            public void onConnectionEvent(Conference c, String event, Bundle extras) {
                Listener.this.onConnectionEvent(c, event, extras);
            }

            public void onCallerDisplayNameChanged(Conference c, String callerDisplayName, int presentation) {
                Listener.this.onCallerDisplayNameChanged(c, callerDisplayName, presentation);
            }

            public void onCallDirectionChanged(Conference c, int callDirection) {
                Listener.this.onCallDirectionChanged(c, callDirection);
            }

            public void onRingbackRequested(Conference c, boolean ringback) {
                Listener.this.onRingbackRequested(c, ringback);
            }
        };

        public void onStateChanged(Conference conference, int oldState, int newState) {
        }

        public void onDisconnected(Conference conference, DisconnectCause disconnectCause) {
        }

        public void onConnectionAdded(Conference conference, Connection connection) {
        }

        public void onConnectionRemoved(Conference conference, Connection connection) {
        }

        public void onConferenceableConnectionsChanged(Conference conference, List<Connection> conferenceableConnections) {
        }

        public void onConferenceablesChanged(Conference conference, List<Conferenceable> conferenceables) {
        }

        public void onDestroyed(Conference conference) {
        }

        public void onConnectionCapabilitiesChanged(Conference conference, int connectionCapabilities) {
        }

        public void onConnectionPropertiesChanged(Conference conference, int connectionProperties) {
        }

        public void onVideoStateChanged(Conference c, int videoState) {
        }

        public void onVideoProviderChanged(Conference c, Connection.VideoProvider videoProvider) {
        }

        public void onStatusHintsChanged(Conference conference, StatusHints statusHints) {
        }

        public void onExtrasChanged(Conference c, Bundle extras) {
        }

        public void onExtrasRemoved(Conference c, List<String> keys) {
        }

        public void onConferenceStateChanged(Conference c, boolean isConference) {
        }

        public void onAddressChanged(Conference c, Uri newAddress, int presentation) {
        }

        public void onConnectionEvent(Conference c, String event, Bundle extras) {
        }

        public void onCallerDisplayNameChanged(Conference c, String callerDisplayName, int presentation) {
        }

        public void onCallDirectionChanged(Conference c, int callDirection) {
        }

        public void onRingbackRequested(Conference c, boolean ringback) {
        }

        public Conference addListener(Conference conference) {
            return conference.addListener(this.mListener);
        }

        public Conference removeListener(Conference conference) {
            return conference.removeListener(this.mListener);
        }
    }
}
