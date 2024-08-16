package com.android.ims;

import android.net.Uri;
import android.telephony.Rlog;
import android.telephony.ims.ImsCallProfile;
import android.util.Log;
import com.android.ims.ImsCall;
import com.android.ims.internal.ConferenceParticipant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class ImsCallExtImpl implements IImsCallExt {
    private static final boolean DBG;
    private static final boolean FORCE_DEBUG = false;
    private static final String TAG;
    private static boolean mIsCEPPresent;
    private static List<ConferenceParticipant> mLocalConferenceParticipants;
    private static HashMap<String, String> mRestoreMap;
    private static boolean mShowConfListWithoutCep;
    private Object mLockObj = new Object();

    static {
        String simpleName = ImsCallExtImpl.class.getSimpleName();
        TAG = simpleName;
        DBG = Log.isLoggable(simpleName, 3);
        mRestoreMap = new HashMap<>();
        mLocalConferenceParticipants = new ArrayList();
        mIsCEPPresent = false;
        mShowConfListWithoutCep = false;
    }

    public ImsCallExtImpl() {
        logd("Create ImsCallExtImpl....");
    }

    public ImsCallExtImpl(Object base) {
        logd("Create ImsCallExtImpl");
    }

    public void removeParticipants(ImsCall imsCall, String[] participants) {
        ImsCall.Listener listener = imsCall.getListener();
        logi("mIsCEPPresent = " + mIsCEPPresent);
        if (!mIsCEPPresent && participants != null && mLocalConferenceParticipants != null) {
            for (String participant : participants) {
                logi("Looping for participant " + Rlog.pii(TAG, participant));
                Iterator<ConferenceParticipant> it = mLocalConferenceParticipants.iterator();
                while (true) {
                    if (it.hasNext()) {
                        ConferenceParticipant c = it.next();
                        StringBuilder append = new StringBuilder().append("Check handle for c = ");
                        String str = TAG;
                        logi(append.append(Rlog.pii(str, c.getHandle())).toString());
                        if (participant != null && Uri.parse(participant).equals(c.getHandle())) {
                            logi("Remove participant " + Rlog.pii(str, participant));
                            it.remove();
                            break;
                        }
                    }
                }
            }
            if (listener != null) {
                try {
                    listener.onConferenceParticipantsStateChanged(imsCall, mLocalConferenceParticipants);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

    public void updateConferenceParticipantsList(ImsCall curCall, ImsCall bgCall) {
        if (!mShowConfListWithoutCep || bgCall == null) {
            return;
        }
        ImsCall confCall = curCall;
        ImsCall childCall = bgCall;
        if (bgCall.isMultiparty()) {
            logi("updateConferenceParticipantsList: BG call is conference");
            confCall = bgCall;
            childCall = curCall;
        } else if (!curCall.isMultiparty()) {
            logi("updateConferenceParticipantsList: Make this call as conference and add child");
            addToConferenceParticipantList(confCall, confCall);
        }
        addToConferenceParticipantList(confCall, childCall);
    }

    private void addToConferenceParticipantList(ImsCall confCall, ImsCall childCall) {
        if (childCall == null) {
            return;
        }
        ImsCallProfile profile = childCall.getCallProfile();
        if (profile == null) {
            logd("addToConferenceParticipantList: null profile for childcall");
            return;
        }
        String handle = profile.getCallExtra("oi", (String) null);
        String name = profile.getCallExtra("cna", "");
        if (handle == null) {
            logd("addToConferenceParticipantList: Invalid number for childcall");
            return;
        }
        Uri userUri = Uri.parse(handle);
        ConferenceParticipant participant = new ConferenceParticipant(userUri, name, userUri, childCall.getState(), 4);
        synchronized (this.mLockObj) {
            if (DBG) {
                logi("Adding participant: " + participant + " to list");
            }
            logi("addToConferenceParticipantList:mIsCEPPresent = " + mIsCEPPresent);
            mLocalConferenceParticipants.add(participant);
            if (confCall.isMultiparty() && !mIsCEPPresent && !mLocalConferenceParticipants.isEmpty() && confCall.getListener() != null) {
                try {
                    confCall.getListener().onConferenceParticipantsStateChanged(confCall, mLocalConferenceParticipants);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

    public void setShowConfListWithoutCep(boolean showConfListWithoutCep) {
        logd("showConfListWithoutCep = " + showConfListWithoutCep);
        mShowConfListWithoutCep = showConfListWithoutCep;
    }

    public void setCepPresent(boolean b) {
        logd("setCepPresent: b = " + b);
        mIsCEPPresent = b;
    }

    public void clearRestorMap() {
        mRestoreMap.clear();
    }

    public HashMap<String, String> getRestorMap() {
        return mRestoreMap;
    }

    public void putParticipants(String key, String user) {
        mRestoreMap.put(key, user);
    }

    public void clearLocalParticipants() {
        mLocalConferenceParticipants.clear();
    }

    public boolean isCEPPresent() {
        return mIsCEPPresent;
    }

    public List<ConferenceParticipant> getLocalParticipants() {
        return mLocalConferenceParticipants;
    }

    public void removeLocalParticipants(ImsCall mergeHost, ImsCall mergePeer) {
        if (mergePeer == null || mergeHost == null || !mShowConfListWithoutCep || mIsCEPPresent) {
            return;
        }
        ImsCallProfile profile = mergePeer.getCallProfile();
        if (profile == null) {
            logd("removeLocalParticipants: null profile for mergePeer");
            return;
        }
        String handle = profile.getCallExtra("oi", (String) null);
        Iterator<ConferenceParticipant> it = mLocalConferenceParticipants.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ConferenceParticipant c = it.next();
            StringBuilder append = new StringBuilder().append("Check handle for c = ");
            String str = TAG;
            logi(append.append(Rlog.pii(str, c.getHandle())).toString());
            if (handle != null && Uri.parse(handle).equals(c.getHandle())) {
                logi("Remove participant " + Rlog.pii(str, handle));
                it.remove();
                break;
            }
        }
        if (mergeHost.isMultiparty() && !mLocalConferenceParticipants.isEmpty() && mergeHost.getListener() != null) {
            try {
                mergeHost.getListener().onConferenceParticipantsStateChanged(mergeHost, mLocalConferenceParticipants);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    private void logd(String str) {
        Rlog.d(TAG, str);
    }

    private void logi(String str) {
        Rlog.i(TAG, str);
    }
}
