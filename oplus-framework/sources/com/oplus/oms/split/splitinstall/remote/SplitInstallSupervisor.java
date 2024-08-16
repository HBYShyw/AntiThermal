package com.oplus.oms.split.splitinstall.remote;

import android.os.Bundle;
import android.os.RemoteException;
import android.provider.oplus.Telephony;
import android.text.TextUtils;
import com.oplus.oms.split.common.Identifiable;
import com.oplus.oms.split.core.splitinstall.SplitInstallListenerRegistry;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

/* loaded from: classes.dex */
public abstract class SplitInstallSupervisor {
    public static final int LENGTH = 2;

    /* loaded from: classes.dex */
    public interface Callback {
        void onCancelInstall(int i, Bundle bundle);

        void onDeferredInstall(Bundle bundle);

        void onDeferredUninstall(Bundle bundle);

        void onError(Bundle bundle);

        void onGetSession(int i, Bundle bundle);

        void onGetSessionStates(List<Bundle> list);

        void onStartInstall(int i, Bundle bundle);
    }

    public abstract void cancelInstall(int i, Callback callback) throws RemoteException;

    public abstract boolean cancelInstallWithoutUserConfirmation(int i);

    public abstract boolean continueInstallWithUserConfirmation(int i);

    public abstract void deferredInstall(List<Bundle> list, Callback callback) throws RemoteException;

    public abstract void deferredUninstall(List<Bundle> list, Callback callback) throws RemoteException;

    public abstract void getSessionState(int i, Callback callback) throws RemoteException;

    public abstract void getSessionStates(Callback callback) throws RemoteException;

    public abstract void setInstallListenerRegister(SplitInstallListenerRegistry splitInstallListenerRegistry);

    public abstract void startInstall(List<String> list, Callback callback);

    public static Bundle bundleErrorCode(int errorCode) {
        Bundle bundle = new Bundle();
        bundle.putInt(Telephony.TextBasedSmsColumns.ERROR_CODE, errorCode);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int createSessionId(Collection<? extends Identifiable> splitInfoList) {
        int sessionId = 0;
        if (splitInfoList == null || splitInfoList.isEmpty()) {
            return 0;
        }
        for (Identifiable info : splitInfoList) {
            String key = info.getIdentify();
            sessionId += createSessionId(key);
        }
        return sessionId;
    }

    protected static int createSessionId(String key) {
        if (TextUtils.isEmpty(key)) {
            return -1;
        }
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest((key).getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 255) < 16) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 255));
            }
            return hex.toString().hashCode();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException("NoSuchAlgorithmException", e2);
        }
    }
}
