package com.oplus.oshare;

import android.bluetooth.OplusBluetoothMonitor;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import com.oplus.network.OlkConstants;
import com.oplus.oshare.IOplusOshareService;

/* loaded from: classes.dex */
public class OplusOshareServiceUtil extends IOplusOshareService.Stub {
    public static final String ACTION_OSHARE_STATE = "coloros.intent.action.OSHARE_STATE";
    private static final String KEY_SECURITY_CHECK_AGAIN = "key_security_check_again";
    public static final int OSHARE_OFF = 0;
    public static final int OSHARE_ON = 1;
    public static final String OSHARE_STATE = "oshare_state";
    private static final String PREFERENCE_PACKAGE = "com.coloros.oshare";
    private static final String SHARED_PREFERENCES_NAME = "oshare_preferences";
    protected static final String TAG = "OShareServiceUtil";
    private Context mContext;
    private IOplusOshareInitListener mInitListener;
    private IOplusOshareCallback mOShareCallback;
    private IOplusOshareService mService;
    private volatile boolean mServiceConnected = false;
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oplus.oshare.OplusOshareServiceUtil.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            OplusOshareServiceUtil.this.initShareEngine();
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.oplus.oshare.OplusOshareServiceUtil.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(OplusOshareServiceUtil.TAG, "onServiceConnected");
            OplusOshareServiceUtil.this.mServiceConnected = true;
            OplusOshareServiceUtil.this.mService = IOplusOshareService.Stub.asInterface(service);
            try {
                if (OplusOshareServiceUtil.this.mInitListener != null) {
                    OplusOshareServiceUtil.this.mInitListener.onShareInit();
                }
                if (OplusOshareServiceUtil.this.mService != null) {
                    OplusOshareServiceUtil.this.mService.scan();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            Log.d(OplusOshareServiceUtil.TAG, "onServiceDisconnected");
            OplusOshareServiceUtil.this.mServiceConnected = false;
            try {
                if (OplusOshareServiceUtil.this.mInitListener != null) {
                    OplusOshareServiceUtil.this.mInitListener.onShareUninit();
                }
                if (OplusOshareServiceUtil.this.mService != null) {
                    OplusOshareServiceUtil.this.mService.unregisterCallback(OplusOshareServiceUtil.this.mOShareCallback);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            OplusOshareServiceUtil.this.mService = null;
        }
    };

    public OplusOshareServiceUtil(Context context, IOplusOshareInitListener listener) {
        this.mContext = context;
        this.mInitListener = listener;
    }

    public void initShareEngine() {
        Log.d(TAG, "initShareEngine");
        Intent intent = new Intent("com.coloros.oshare.OShareClient");
        if (this.mServiceConnection != null && !this.mServiceConnected) {
            intent.setPackage(PREFERENCE_PACKAGE);
            this.mContext.bindService(intent, this.mServiceConnection, 1);
        }
    }

    @Override // com.oplus.oshare.IOplusOshareService
    public void scan() {
        Log.d(TAG, OplusBluetoothMonitor.SCAN_MONIT_EVENT);
        IOplusOshareService iOplusOshareService = this.mService;
        if (iOplusOshareService != null) {
            try {
                iOplusOshareService.scan();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.oplus.oshare.IOplusOshareService
    public void registerCallback(IOplusOshareCallback callback) {
        Log.d(TAG, OlkConstants.FUN_REGISTER_CALLBACK);
        IOplusOshareService iOplusOshareService = this.mService;
        if (iOplusOshareService != null) {
            try {
                this.mOShareCallback = callback;
                iOplusOshareService.registerCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.oplus.oshare.IOplusOshareService
    public void unregisterCallback(IOplusOshareCallback callback) {
        Log.d(TAG, "unregisterCallback");
        IOplusOshareService iOplusOshareService = this.mService;
        if (iOplusOshareService != null) {
            try {
                iOplusOshareService.unregisterCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.oplus.oshare.IOplusOshareService
    public void sendData(Intent intent, OplusOshareDevice target) {
        Log.d(TAG, "sendData");
        IOplusOshareService iOplusOshareService = this.mService;
        if (iOplusOshareService != null) {
            try {
                iOplusOshareService.sendData(intent, target);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.oplus.oshare.IOplusOshareService
    public void cancelTask(OplusOshareDevice device) {
        Log.d(TAG, "cancelTask");
        IOplusOshareService iOplusOshareService = this.mService;
        if (iOplusOshareService != null) {
            try {
                iOplusOshareService.cancelTask(device);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.oplus.oshare.IOplusOshareService
    public void stop() {
        Log.d(TAG, "stop : mServiceConnected = " + this.mServiceConnected);
        IOplusOshareService iOplusOshareService = this.mService;
        if (iOplusOshareService != null) {
            try {
                iOplusOshareService.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if (this.mServiceConnection != null && this.mServiceConnected) {
            try {
                this.mContext.unbindService(this.mServiceConnection);
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            }
        }
        this.mInitListener = null;
    }

    public static boolean isOshareOn(Context context) {
        ContentResolver cr = context.getContentResolver();
        return checkRuntimePermission(context) && Settings.System.getInt(cr, OSHARE_STATE, 0) == 1;
    }

    public static void switchOshare(Context context, boolean isOn) {
        ContentResolver cr = context.getContentResolver();
        if (isOn) {
            Intent intent = new Intent("coloros.intent.action.SECURITY_CHECK");
            intent.setPackage(PREFERENCE_PACKAGE);
            intent.addFlags(268435456);
            try {
                context.startActivity(intent);
                return;
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
        sendOshareStateBroadcast(context, isOn, cr);
    }

    private static void sendOshareStateBroadcast(Context context, boolean z, ContentResolver contentResolver) {
        Intent intent = new Intent(ACTION_OSHARE_STATE);
        intent.putExtra(OSHARE_STATE, z ? 1 : 0);
        context.sendBroadcast(intent);
        Settings.System.putInt(contentResolver, OSHARE_STATE, z ? 1 : 0);
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, 7);
    }

    public static void setSecurityCheckAgain(Context context, boolean needCheckAgain) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(KEY_SECURITY_CHECK_AGAIN, needCheckAgain);
        editor.commit();
    }

    public static boolean isSecurityCheckAgain(Context context) {
        Context c = null;
        try {
            c = context.createPackageContext(PREFERENCE_PACKAGE, 2);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (c != null) {
            return getSharedPreferences(c).getBoolean(KEY_SECURITY_CHECK_AGAIN, true);
        }
        return true;
    }

    @Override // com.oplus.oshare.IOplusOshareService
    public boolean isSendOn() {
        IOplusOshareService iOplusOshareService = this.mService;
        if (iOplusOshareService != null) {
            try {
                return iOplusOshareService.isSendOn();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override // com.oplus.oshare.IOplusOshareService
    public void switchSend(boolean isOn) {
        IOplusOshareService iOplusOshareService = this.mService;
        if (iOplusOshareService != null) {
            try {
                iOplusOshareService.switchSend(isOn);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.oplus.oshare.IOplusOshareService
    public void pause() throws RemoteException {
        Log.d(TAG, "pause : mServiceConnected = " + this.mServiceConnected);
        IOplusOshareService iOplusOshareService = this.mService;
        if (iOplusOshareService != null) {
            try {
                iOplusOshareService.pause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.oplus.oshare.IOplusOshareService
    public void resume() throws RemoteException {
        Log.d(TAG, "resume : mServiceConnected = " + this.mServiceConnected);
        if (!this.mServiceConnected) {
            initShareEngine();
        }
        IOplusOshareService iOplusOshareService = this.mService;
        if (iOplusOshareService != null) {
            try {
                iOplusOshareService.resume();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean checkRuntimePermission(Context context) {
        boolean checkSelfPermissionResult = true;
        if (context.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            checkSelfPermissionResult = false;
        }
        if (context.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
            checkSelfPermissionResult = false;
        }
        if (context.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0) {
            checkSelfPermissionResult = false;
        }
        if (context.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            return false;
        }
        return checkSelfPermissionResult;
    }
}
