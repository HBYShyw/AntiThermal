package android.bluetooth;

import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.oplus.view.IJankManager;
import java.util.List;

/* loaded from: classes.dex */
public class OplusBluetoothA2dpHelper {
    private static final String TAG = "OplusBluetoothA2dpHelper";
    private static OplusBluetoothA2dpHelper sInstance;
    private volatile BluetoothA2dp mBluetoothA2dp;
    private final BroadcastReceiver mBluetoothA2dpReceiver = new BroadcastReceiver() { // from class: android.bluetooth.OplusBluetoothA2dpHelper.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            BluetoothCodecConfig codecConfig;
            Log.d(OplusBluetoothA2dpHelper.TAG, "mBluetoothA2dpReceiver.onReceive intent=" + intent);
            String action = intent.getAction();
            if ("android.bluetooth.a2dp.profile.action.CODEC_CONFIG_CHANGED".equals(action)) {
                BluetoothCodecStatus codecStatus = (BluetoothCodecStatus) intent.getParcelableExtra("android.bluetooth.extra.CODEC_STATUS");
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                Log.d(OplusBluetoothA2dpHelper.TAG, "Received BluetoothCodecStatus=" + codecStatus);
                if (codecStatus != null && (codecConfig = codecStatus.getCodecConfig()) != null && OplusBluetoothA2dpHelper.this.mCallback != null) {
                    OplusBluetoothA2dpHelper.this.mCallback.onBluetoothCodecChanged(device, codecConfig.getCodecType());
                }
            }
        }
    };
    private final BluetoothProfile.ServiceListener mBluetoothA2dpServiceListener = new BluetoothProfile.ServiceListener() { // from class: android.bluetooth.OplusBluetoothA2dpHelper.2
        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            synchronized (this) {
                OplusBluetoothA2dpHelper.this.mBluetoothA2dp = (BluetoothA2dp) proxy;
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int profile) {
            synchronized (this) {
                OplusBluetoothA2dpHelper.this.mBluetoothA2dp = null;
            }
        }
    };
    private BluetoothAdapter mBluetoothAdapter;
    private Callback mCallback;
    private Context mContext;

    /* loaded from: classes.dex */
    public interface Callback {
        void onBluetoothCodecChanged(BluetoothDevice bluetoothDevice, int i);
    }

    private OplusBluetoothA2dpHelper(Context context, Callback callback) {
        this.mBluetoothAdapter = ((BluetoothManager) context.getSystemService(BluetoothManager.class)).getAdapter();
        this.mContext = context;
        this.mCallback = callback;
    }

    public static synchronized OplusBluetoothA2dpHelper getInstance(Context context, Callback callback) {
        OplusBluetoothA2dpHelper oplusBluetoothA2dpHelper;
        synchronized (OplusBluetoothA2dpHelper.class) {
            if (sInstance == null) {
                sInstance = new OplusBluetoothA2dpHelper(context, callback);
            }
            oplusBluetoothA2dpHelper = sInstance;
        }
        return oplusBluetoothA2dpHelper;
    }

    public void init() {
        registerReceivers();
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null) {
            bluetoothAdapter.getProfileProxy(this.mContext.getApplicationContext(), this.mBluetoothA2dpServiceListener, 2);
        }
    }

    public void cleanUp() {
        unregisterReceivers();
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null) {
            bluetoothAdapter.closeProfileProxy(2, this.mBluetoothA2dp);
            this.mBluetoothA2dp = null;
        }
    }

    private void registerReceivers() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.a2dp.profile.action.CODEC_CONFIG_CHANGED");
        this.mContext.getApplicationContext().registerReceiver(this.mBluetoothA2dpReceiver, filter);
    }

    private void unregisterReceivers() {
        this.mContext.getApplicationContext().unregisterReceiver(this.mBluetoothA2dpReceiver);
    }

    private BluetoothDevice getA2dpActiveDevice() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return null;
        }
        List<BluetoothDevice> activeDevices = bluetoothAdapter.getActiveDevices(2);
        if (activeDevices.size() > 0) {
            return activeDevices.get(0);
        }
        return null;
    }

    public int getCodecType(BluetoothDevice device) {
        BluetoothCodecStatus codecStatus;
        BluetoothCodecConfig codecConfig;
        BluetoothA2dp bluetoothA2dp = this.mBluetoothA2dp;
        return (bluetoothA2dp == null || (codecStatus = bluetoothA2dp.getCodecStatus(device)) == null || (codecConfig = codecStatus.getCodecConfig()) == null) ? IJankManager.MS_TO_NS : codecConfig.getCodecType();
    }

    public String getCodecName(BluetoothDevice device) {
        return BluetoothCodecConfig.getCodecName(getCodecType(device));
    }
}
