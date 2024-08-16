package com.android.server.companion.datatransfer.contextsync;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CallMetadataSyncData {
    final Map<String, Call> mCalls = new HashMap();
    final List<CallCreateRequest> mCallCreateRequests = new ArrayList();
    final List<CallControlRequest> mCallControlRequests = new ArrayList();
    final List<CallFacilitator> mCallFacilitators = new ArrayList();

    public void addCall(Call call) {
        this.mCalls.put(call.getId(), call);
    }

    public boolean hasCall(String str) {
        return this.mCalls.containsKey(str);
    }

    public Collection<Call> getCalls() {
        return this.mCalls.values();
    }

    public void addCallCreateRequest(CallCreateRequest callCreateRequest) {
        this.mCallCreateRequests.add(callCreateRequest);
    }

    public List<CallCreateRequest> getCallCreateRequests() {
        return this.mCallCreateRequests;
    }

    public void addCallControlRequest(CallControlRequest callControlRequest) {
        this.mCallControlRequests.add(callControlRequest);
    }

    public List<CallControlRequest> getCallControlRequests() {
        return this.mCallControlRequests;
    }

    public void addFacilitator(CallFacilitator callFacilitator) {
        this.mCallFacilitators.add(callFacilitator);
    }

    public List<CallFacilitator> getFacilitators() {
        return this.mCallFacilitators;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class CallFacilitator {
        private String mExtendedIdentifier;
        private String mIdentifier;
        private boolean mIsTel;
        private String mName;

        /* JADX INFO: Access modifiers changed from: package-private */
        public CallFacilitator() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public CallFacilitator(String str, String str2, String str3) {
            this.mName = str;
            this.mIdentifier = str2;
            this.mExtendedIdentifier = str3;
        }

        public String getName() {
            return this.mName;
        }

        public String getIdentifier() {
            return this.mIdentifier;
        }

        public String getExtendedIdentifier() {
            return this.mExtendedIdentifier;
        }

        public boolean isTel() {
            return this.mIsTel;
        }

        public void setName(String str) {
            this.mName = str;
        }

        public void setIdentifier(String str) {
            this.mIdentifier = str;
        }

        public void setExtendedIdentifier(String str) {
            this.mExtendedIdentifier = str;
        }

        public void setIsTel(boolean z) {
            this.mIsTel = z;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class CallControlRequest {
        private int mControl;
        private String mId;

        public void setId(String str) {
            this.mId = str;
        }

        public void setControl(int i) {
            this.mControl = i;
        }

        public String getId() {
            return this.mId;
        }

        public int getControl() {
            return this.mControl;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class CallCreateRequest {
        private String mAddress;
        private CallFacilitator mFacilitator;
        private String mId;

        public void setId(String str) {
            this.mId = str;
        }

        public void setAddress(String str) {
            this.mAddress = str;
        }

        public void setFacilitator(CallFacilitator callFacilitator) {
            this.mFacilitator = callFacilitator;
        }

        public String getId() {
            return this.mId;
        }

        public String getAddress() {
            return this.mAddress;
        }

        public CallFacilitator getFacilitator() {
            return this.mFacilitator;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Call {
        private static final String EXTRA_APP_ICON = "com.android.server.companion.datatransfer.contextsync.extra.APP_ICON";
        private static final String EXTRA_CALLER_ID = "com.android.server.companion.datatransfer.contextsync.extra.CALLER_ID";
        private static final String EXTRA_CONTROLS = "com.android.server.companion.datatransfer.contextsync.extra.CONTROLS";
        private static final String EXTRA_DIRECTION = "com.android.server.companion.datatransfer.contextsync.extra.DIRECTION";
        private static final String EXTRA_FACILITATOR_EXT_ID = "com.android.server.companion.datatransfer.contextsync.extra.FACILITATOR_EXT_ID";
        private static final String EXTRA_FACILITATOR_ID = "com.android.server.companion.datatransfer.contextsync.extra.FACILITATOR_ID";
        private static final String EXTRA_FACILITATOR_NAME = "com.android.server.companion.datatransfer.contextsync.extra.FACILITATOR_NAME";
        private static final String EXTRA_STATUS = "com.android.server.companion.datatransfer.contextsync.extra.STATUS";
        private byte[] mAppIcon;
        private String mCallerId;
        private final Set<Integer> mControls = new HashSet();
        private int mDirection;
        private CallFacilitator mFacilitator;
        private String mId;
        private int mStatus;

        public static Call fromBundle(Bundle bundle) {
            Call call = new Call();
            if (bundle != null) {
                call.setId(bundle.getString(CrossDeviceSyncController.EXTRA_CALL_ID));
                call.setCallerId(bundle.getString(EXTRA_CALLER_ID));
                call.setAppIcon(bundle.getByteArray(EXTRA_APP_ICON));
                call.setFacilitator(new CallFacilitator(bundle.getString(EXTRA_FACILITATOR_NAME), bundle.getString(EXTRA_FACILITATOR_ID), bundle.getString(EXTRA_FACILITATOR_EXT_ID)));
                call.setStatus(bundle.getInt(EXTRA_STATUS));
                call.setDirection(bundle.getInt(EXTRA_DIRECTION));
                call.setControls(new HashSet(bundle.getIntegerArrayList(EXTRA_CONTROLS)));
            }
            return call;
        }

        public Bundle writeToBundle() {
            Bundle bundle = new Bundle();
            bundle.putString(CrossDeviceSyncController.EXTRA_CALL_ID, this.mId);
            bundle.putString(EXTRA_CALLER_ID, this.mCallerId);
            bundle.putByteArray(EXTRA_APP_ICON, this.mAppIcon);
            bundle.putString(EXTRA_FACILITATOR_NAME, this.mFacilitator.getName());
            bundle.putString(EXTRA_FACILITATOR_ID, this.mFacilitator.getIdentifier());
            bundle.putString(EXTRA_FACILITATOR_EXT_ID, this.mFacilitator.getExtendedIdentifier());
            bundle.putInt(EXTRA_STATUS, this.mStatus);
            bundle.putInt(EXTRA_DIRECTION, this.mDirection);
            bundle.putIntegerArrayList(EXTRA_CONTROLS, new ArrayList<>(this.mControls));
            return bundle;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setId(String str) {
            this.mId = str;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setCallerId(String str) {
            this.mCallerId = str;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setAppIcon(byte[] bArr) {
            this.mAppIcon = bArr;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setFacilitator(CallFacilitator callFacilitator) {
            this.mFacilitator = callFacilitator;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setStatus(int i) {
            this.mStatus = i;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setDirection(int i) {
            this.mDirection = i;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void addControl(int i) {
            this.mControls.add(Integer.valueOf(i));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setControls(Set<Integer> set) {
            this.mControls.clear();
            this.mControls.addAll(set);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public String getId() {
            return this.mId;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public String getCallerId() {
            return this.mCallerId;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public byte[] getAppIcon() {
            return this.mAppIcon;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public CallFacilitator getFacilitator() {
            return this.mFacilitator;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getStatus() {
            return this.mStatus;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getDirection() {
            return this.mDirection;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Set<Integer> getControls() {
            return this.mControls;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean hasControl(int i) {
            return this.mControls.contains(Integer.valueOf(i));
        }

        public boolean equals(Object obj) {
            String str;
            return (obj instanceof Call) && (str = this.mId) != null && str.equals(((Call) obj).getId());
        }

        public int hashCode() {
            return Objects.hashCode(this.mId);
        }
    }
}
