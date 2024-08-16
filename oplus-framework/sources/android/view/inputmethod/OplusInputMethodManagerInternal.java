package android.view.inputmethod;

import android.content.ComponentName;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.util.Singleton;
import android.view.inputmethod.IOplusInputMethodManager;
import com.android.internal.inputmethod.IRemoteInputConnection;
import com.android.internal.view.IInputMethodManager;
import com.oplus.util.OplusInputMethodUtil;

/* loaded from: classes.dex */
public class OplusInputMethodManagerInternal {
    private static final Singleton<IOplusInputMethodManager> I_OPLUS_INPUTMETHOD_MANAGER_SINGLETON = new Singleton<IOplusInputMethodManager>() { // from class: android.view.inputmethod.OplusInputMethodManagerInternal.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public IOplusInputMethodManager m309create() {
            try {
                IInputMethodManager inputMethodManager = IInputMethodManager.Stub.asInterface(ServiceManager.getService("input_method"));
                IBinder b = inputMethodManager.asBinder().getExtension();
                OplusInputMethodUtil.slog(OplusInputMethodManagerInternal.TAG, "get inputMethodManager extension: " + inputMethodManager);
                return IOplusInputMethodManager.Stub.asInterface(b);
            } catch (Exception e) {
                OplusInputMethodUtil.slog(OplusInputMethodManagerInternal.TAG, "create IOplusInputMethodManager singleton failed: " + e.getMessage());
                return null;
            }
        }
    };
    private static final String TAG = "OplusInputMethodManagerInternal";

    /* loaded from: classes.dex */
    private static final class OplusInputMethodManagerHolder {
        private static final OplusInputMethodManagerInternal INSTANCE = new OplusInputMethodManagerInternal();

        private OplusInputMethodManagerHolder() {
        }
    }

    private OplusInputMethodManagerInternal() {
    }

    private static IOplusInputMethodManager getService() {
        return (IOplusInputMethodManager) I_OPLUS_INPUTMETHOD_MANAGER_SINGLETON.get();
    }

    public static OplusInputMethodManagerInternal getInstance() {
        return OplusInputMethodManagerHolder.INSTANCE;
    }

    public void hideCurrentInputMethod() {
        if (getService() != null) {
            try {
                getService().hideCurrentInputMethod();
                OplusInputMethodUtil.logMethodCallers(TAG, "hideCurrentInputMethod");
            } catch (RemoteException e) {
                OplusInputMethodUtil.log(TAG, "hideCurrentInputMethod failed..");
            }
        }
    }

    public boolean setDefaultInputMethodByCustomize(String packageName) {
        if (getService() == null) {
            return false;
        }
        try {
            boolean result = getService().setDefaultInputMethodByCustomize(packageName);
            return result;
        } catch (RemoteException e) {
            OplusInputMethodUtil.log(TAG, "setDefaultInputMethodByCustomize failed..");
            return false;
        }
    }

    public String getDefaultInputMethodByCustomize() {
        if (getService() == null) {
            return "";
        }
        try {
            String result = getService().getDefaultInputMethodByCustomize();
            return result;
        } catch (RemoteException e) {
            OplusInputMethodUtil.log(TAG, "getDefaultInputMethodByCustomize failed..");
            return "";
        }
    }

    public boolean clearDefaultInputMethodByCustomize() {
        if (getService() != null) {
            try {
                return getService().clearDefaultInputMethodByCustomize();
            } catch (RemoteException e) {
                OplusInputMethodUtil.log(TAG, "clearDefaultInputMethodByCustomize failed..");
                return false;
            }
        }
        return false;
    }

    public void updateTouchDeviceId(int deviceId) {
        if (getService() != null) {
            try {
                getService().updateTouchDeviceId(deviceId);
            } catch (RemoteException e) {
                OplusInputMethodUtil.log(TAG, "updateTouchDeviceId failed..");
            }
        }
    }

    public void updateCursorAnchorInfoToSynergy(CursorAnchorInfo cursorAnchorInfo) {
        if (getService() != null) {
            try {
                getService().updateCursorAnchorInfoToSynergy(cursorAnchorInfo);
            } catch (RemoteException e) {
                OplusInputMethodUtil.log(TAG, "updateCursorAnchorInfoToSynergy failed..");
            }
        }
    }

    public void invalidateInputToSynergy(EditorInfo editorInfo, IRemoteInputConnection inputConnection, int sessionId) {
        if (getService() != null) {
            try {
                getService().invalidateInputToSynergy(editorInfo, inputConnection, sessionId);
            } catch (RemoteException e) {
                OplusInputMethodUtil.log(TAG, "invalidateInputToSynergy failed..");
            }
        }
    }

    public void registerInputMethodSynergyService(ComponentName synergyName, boolean register) {
        if (synergyName != null && getService() != null) {
            try {
                getService().registerInputMethodSynergyService(synergyName, register);
                OplusInputMethodUtil.logMethodCallers(TAG, "registerInputMethodSynergyService name = " + synergyName.getShortClassName() + " register = " + register);
            } catch (RemoteException e) {
                OplusInputMethodUtil.log(TAG, "registerInputMethodSynergyService failed..");
            }
        }
    }

    public void commitTextByOtherSide() {
        if (getService() != null) {
            try {
                getService().commitTextByOtherSide();
                OplusInputMethodUtil.logMethodCallers(TAG, "commitTextByOtherSide");
            } catch (RemoteException e) {
                OplusInputMethodUtil.log(TAG, "commitTextByOtherSide failed..");
            }
        }
    }

    public int getInputMethodWindowVisibleHeight(int displayId) {
        if (getService() != null) {
            try {
                return getService().getInputMethodWindowVisibleHeight(displayId);
            } catch (RemoteException e) {
                OplusInputMethodUtil.log(TAG, "getInputMethodWindowVisibleHeight failed..");
                return 0;
            }
        }
        return 0;
    }

    public void registerCursorAnchorInfoListener(ResultReceiver receiver) {
        if (getService() != null) {
            try {
                getService().registerCursorAnchorInfoListener(receiver);
            } catch (RemoteException e) {
                OplusInputMethodUtil.log(TAG, "registerCursorAnchorInfoListener failed.");
            }
        }
    }

    public void unregisterCursorAnchorInfoListener(ResultReceiver receiver) {
        if (getService() != null) {
            try {
                getService().unregisterCursorAnchorInfoListener(receiver);
            } catch (RemoteException e) {
                OplusInputMethodUtil.log(TAG, "unregisterCursorAnchorInfoListener failed.");
            }
        }
    }

    public void setAlwaysLogOn(long maxSize, String param) {
        if (getService() != null) {
            try {
                getService().setAlwaysLogOn(maxSize, param);
            } catch (RemoteException e) {
                OplusInputMethodUtil.log(TAG, "setAlwaysLogOn failed..");
            }
        }
    }

    public void setAlwaysLogOff() {
        if (getService() != null) {
            try {
                getService().setAlwaysLogOff();
            } catch (RemoteException e) {
                OplusInputMethodUtil.log(TAG, "setAlwaysLogOff failed..");
            }
        }
    }
}
