package com.oplus.internal.telephony.imsphone;

import com.android.internal.telephony.Connection;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ImsPhoneConnectionExt {

    /* loaded from: classes.dex */
    public static abstract class ListenerExtBase extends Connection.ListenerBase {
        public void onConferenceParticipantsInvited(boolean isSuccess) {
        }

        public void onConferenceConnectionsConfigured(ArrayList<Connection> radioConnections) {
        }

        public void onAddressDisplayChanged() {
        }

        public void onTextCapabilityChanged(int localCapability, int remoteCapability, int localTextStatus, int realRemoteTextCapability) {
        }

        public void onRedialEcc(boolean isNeedUserConfirm) {
        }
    }
}
